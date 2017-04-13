package com.teaminology.hp.service.impl;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.teaminology.hp.service.enums.ExecuteStatusEnum;
import jodd.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.globalsight.www.webservices.AmbassadorProxy;
import com.sun.syndication.io.XmlReader;
import com.teaminology.hp.bo.Attributes;
import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.Tag;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.dao.ILookUpDAO;
import com.teaminology.hp.dao.IUserDAO;
import com.teaminology.hp.dao.TermDetailsDAO;
import com.teaminology.hp.data.CSVImportStatus;
import com.teaminology.hp.data.GSJobObject;
import com.teaminology.hp.data.GSJobStateEnum;
import com.teaminology.hp.data.QueryAppender;
import com.teaminology.hp.data.TeaminologyObject;
import com.teaminology.hp.gs.CountByState;
import com.teaminology.hp.gs.Counts;
import com.teaminology.hp.gs.Entries;
import com.teaminology.hp.gs.Entry;
import com.teaminology.hp.gs.Job;
import com.teaminology.hp.gs.Jobs;
import com.teaminology.hp.gs.LocalePairInformation;
import com.teaminology.hp.gs.LocalizedDocuments;
import com.teaminology.hp.gs.SourcePage;
import com.teaminology.hp.gs.SourcePages;
import com.teaminology.hp.gs.TMProfile;
import com.teaminology.hp.gs.TMProfileInformation;
import com.teaminology.hp.gs.Target;
import com.teaminology.hp.gs.Task;
import com.teaminology.hp.gs.Tasks;
import com.teaminology.hp.gs.TbEntries;
import com.teaminology.hp.gs.TbEntry;
import com.teaminology.hp.gs.Term;
import com.teaminology.hp.gs.Termbase;
import com.teaminology.hp.gs.TermbaseInformation;
import com.teaminology.hp.gs.Workflow;
import com.teaminology.hp.gs.Workflows;
import com.teaminology.hp.service.IGSService;
import com.teaminology.hp.service.enums.GlobalsightEnum;
import com.teaminology.hp.service.enums.TeaminologyEnum;
import com.teaminology.hp.service.enums.TeaminologyProperty;

/**
 * Contains methods for global sight import and export
 *
 * @author sayeedm
 */
@Service
public class GSServiceImpl implements IGSService
{
    private Logger logger = Logger.getLogger(GSServiceImpl.class);

    private static String targetContent = "";
    private static String sourceContent = "";
    private static Set<Tag> tagSet = new HashSet<Tag>();

    private ILookUpDAO lookUpDAO;
    private TermDetailsDAO termDAO;
    private IUserDAO userDAO;

    private AmbassadorProxy ambassadorProxy = null;

    @Autowired
    public GSServiceImpl(ILookUpDAO lookUpDAO, TermDetailsDAO termDAO, IUserDAO userDAO) {
        this.lookUpDAO = lookUpDAO;
        this.termDAO = termDAO;
        this.userDAO = userDAO;
    }

    @Transactional(readOnly = true)
    @Qualifier("txManager")
    /**
     * To get access token 
     * @param company Company to filter the access token
     * @return access token
     */
    @Override
    public String getAccessToken(Company company) {
        if (company == null || StringUtil.isEmpty(company.getUrl()) || StringUtil.isEmpty(company.getUserName()) || StringUtil.isEmpty(company.getPassword()))
            return null;

        String accessToken = null;
        try {
            ambassadorProxy = new AmbassadorProxy(company.getUrl(), company.getUserName(), company.getPassword());
            if (ambassadorProxy != null) {
                accessToken = ambassadorProxy.login(company.getUserName(), company.getPassword());
                accessToken = getRealAccessToken(accessToken);
                logger.debug("Got access token for GlobalSight WebService : " + accessToken);
            }
        }
        catch (Exception e) {
            logger.error("Error while getting access token for GlobalSight WebService", e);
        }

        return accessToken;
    }

    /**
     * transform "fullAccessToken" into "realToken"
     *
     * @param fullAccessToken access token
     * @return real token after trimming
     */
    private static String getRealAccessToken(String fullAccessToken) {
        String realToken = fullAccessToken;
        if (StringUtil.isNotEmpty(fullAccessToken)) {
            int index = fullAccessToken.indexOf("+_+");
            if (index > 0) {
                realToken = fullAccessToken.substring(0, index);
            }
        }

        return realToken;
    }

    /**
     * to get file profile information
     *
     * @param accessToken accessToken
     * @return file profile Information
     */
    @Override
    public String getFileProfileInfo(String accessToken) {
        if (StringUtil.isEmpty(accessToken))
            return null;

        String fileInfoObj = null;
        try {
            if (ambassadorProxy != null)
                fileInfoObj = ambassadorProxy.getFileProfileInformation(accessToken);
        }
        catch (Exception e) {
            logger.error("Error while getting file profile information", e);
        }
        return fileInfoObj;
    }


    /**
     * To get all jobs
     *
     * @param accessToken accessToken
     * @return all jobids
     */
    @Override
    public String[] fetchAllJobIds(String accessToken) {
        if (StringUtil.isEmpty(accessToken))
            return null;

        String[] jobIds = null;
        try {
            if (ambassadorProxy != null) {
                String jobs = ambassadorProxy.fetchJobIdsPerCompany(accessToken);
                if (StringUtil.isNotEmpty(jobs)) {
                    jobIds = StringUtil.split(jobs, ",");
                }
                logger.debug("successfully got all job ids" + jobs);
            }
        }
        catch (Exception e) {
            logger.error("Error while getting job ids", e);
        }
        return jobIds;
    }

    /**
     * To get all job attributes
     *
     * @param accessToken accessToken
     * @param jobIds      Integer List
     * @return all job attributes
     */
    @Override
    public String getJobAttributes(String accessToken, List<Integer> jobIds) {
        if (StringUtil.isEmpty(accessToken) || jobIds == null || jobIds.size() == 0)
            return null;

        String jobAttributes = null;
        try {
            if (ambassadorProxy != null) {
                for (Integer jobId : jobIds) {
                    String stringObj = ambassadorProxy.getAttributesByJobId(accessToken, jobId);
                    logger.debug("successfully got job Attributes" + stringObj);
                }
            }
        }
        catch (Exception e) {
            logger.error("Error while getting job Attributes ....... " + e);
        }
        return jobAttributes;
    }

    /**
     * To get all jobs
     *
     * @param accessToken accessToken
     * @return job obj
     */
    @Override
    public Jobs getJobs(String accessToken) {
        if (StringUtil.isEmpty(accessToken))
            return null;

        Jobs jobs = null;
        try {
            if (ambassadorProxy != null) {
                String xmlResponse = ambassadorProxy.fetchJobsPerCompany(accessToken);

                JAXBContext jaxbContext = JAXBContext.newInstance(Jobs.class);
                ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
                Reader reader = new InputStreamReader(input, "UTF-8");
                InputSource is = new InputSource(reader);
                is.setEncoding("UTF-8");
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                jobs = (Jobs) jaxbUnmarshaller.unmarshal(is);

                logger.debug("successfully got jobs : " + jobs);
            }
        }
        catch (Exception e) {
            logger.error("Error while getting jobs", e);
        }
        return jobs;
    }

    /**
     * To get  gs terms
     *
     * @param accessToken accessToken
     * @param jobs        Jobs
     * @return List of terms
     */
    @Override
    public List<GSJobObject> getGSTerms(String accessToken, Jobs jobs) {
        if (StringUtil.isEmpty(accessToken) || jobs == null)
            return null;

        List<Job> jobList = new ArrayList<Job>();
        for (Job job : jobs.getJob())
            jobList.add(job);

        return getGSTerms(accessToken, jobList);
    }


    /**
     * To filter the job ids by state
     *
     * @param accessToken accessToken
     * @param jobIds      String Array of filter the job ids
     * @return List of jobs
     */
    @Override
    @Deprecated
    public String[] filterJobIdsByState(String accessToken, String[] jobIds) {
        return null;
    }

    /**
     * To get jobs by state
     *
     * @param jobs Jobs
     * @return List of jobs
     */
    @Override
    public List<Job> getJobsByState(Jobs jobs) {
        if (jobs == null)
            return null;

        List<Job> filteredJobs = new ArrayList<Job>();
        for (Job job : jobs.getJob()) {
            if (GlobalsightEnum.JOB_STATUS_EXPORT.getValue().equalsIgnoreCase(job.getState()))
                filteredJobs.add(job);
        }

        return filteredJobs;
    }

    /**
     * To get  gs terms
     *
     * @param accessToken accessToken
     * @param jobs        List of Job
     * @return List of terms
     */
    @Override
    public List<GSJobObject> getGSTerms(String accessToken, List<Job> jobs) {
        if (StringUtil.isEmpty(accessToken) || jobs == null || jobs.size() == 0)
            return null;

        List<GSJobObject> gsJobObjectList = null;
        try {
            if (ambassadorProxy != null) {
                gsJobObjectList = new ArrayList<GSJobObject>();
                String xmlResponse = "";
                JAXBContext jaxbContext = null;
                ByteArrayInputStream input = null;
                Reader reader = null;
                InputSource is = null;
                Unmarshaller jaxbUnmarshaller = null;
                LocalizedDocuments localizedDocuments = null;
                SourcePages sourcePages = null;
                Workflows workFlows = null;
                String targetLang = null;

                for (Job job : jobs) {
                    if (GlobalsightEnum.JOB_STATUS_EXPORT.getValue().equalsIgnoreCase(job.getState())) {
                        xmlResponse = ambassadorProxy.getLocalizedDocuments(accessToken, job.getName());

                        jaxbContext = JAXBContext.newInstance(LocalizedDocuments.class);
                        input = new ByteArrayInputStream(xmlResponse.getBytes());
                        reader = new InputStreamReader(input, "UTF-8");
                        is = new InputSource(reader);
                        is.setEncoding("UTF-8");
                        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        localizedDocuments = (LocalizedDocuments) jaxbUnmarshaller.unmarshal(is);

                        sourcePages = job.getSourcePages();
                        workFlows = job.getWorkflows();
                        for (Workflow workflow : workFlows.getWorkflow()) {
                            targetLang = workflow.getTargetLang();
                        }
                        if (sourcePages != null) {
                            for (SourcePage sourcePage : sourcePages.getSourcePage()) {
                                String urlPrefix = localizedDocuments.getUrlPrefix();
                                if (sourcePage != null && urlPrefix != null) {
                                    urlPrefix = urlPrefix.substring(urlPrefix.indexOf("globalsight") + 11, urlPrefix.length());
                                    String externalPageId = sourcePage.getExternalPageId();
                                    GSJobObject gsObject = new GSJobObject();
                                    String ext = externalPageId.substring(externalPageId.lastIndexOf(".") + 1, externalPageId.length());
                                    if ("xml".equalsIgnoreCase(ext)) {
                                        String pageUrl = TeaminologyProperty.GS_DOMAIN.getValue() + urlPrefix + "/" + sourcePage.getExternalPageId();
                                        gsObject.setJobId(job.getId());
                                        gsObject.setJobName(job.getName());
                                        gsObject.setLangName(targetLang);
                                        URL url = new URL(pageUrl);
                                        try {
                                            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                                            String strLine = "";
                                            StringBuffer termBeingPolled = new StringBuffer();
                                            //Read File Line By Line
                                            while ((strLine = br.readLine()) != null) {
                                                // Print the content on the console
                                                termBeingPolled.append(strLine);
                                            }
                                            gsObject.setTermsBeingPolled(termBeingPolled.toString());
                                            gsJobObjectList.add(gsObject);
                                            br.close();
                                        }
                                        catch (Exception e) {//Catch exception if any
                                            logger.error("Error found in getGSTerms.", e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("Error while getting GlobalSight terms", e);
        }

        return gsJobObjectList;
    }

    /**
     * To get  gs In progress terms
     *
     * @param accessToken accessToken
     * @param jobs        list of jobs
     * @return List of terms
     */
    @Override
    public List<GSJobObject> getGSInProgressTerms(String accessToken, List<Job> jobs) {
        if (StringUtil.isEmpty(accessToken) || jobs == null || jobs.size() == 0)
            return null;

        List<GSJobObject> gsJobObjectList = null;
        try {
            if (ambassadorProxy != null) {
                gsJobObjectList = new ArrayList<GSJobObject>();
                for (Job job : jobs) {
                    SourcePages sourcePages = job.getSourcePages();
                    Workflows workFlows = job.getWorkflows();
                    String targetLang = null;
                    for (Workflow workflow : workFlows.getWorkflow()) {
                        targetLang = workflow.getTargetLang();
                    }

                    if (sourcePages != null) {
                        for (SourcePage sourcePage : sourcePages.getSourcePage()) {
                            GSJobObject gsObject = new GSJobObject();
                            gsObject.setJobId(job.getId());
                            gsObject.setJobName(job.getName());
                            gsObject.setTermsBeingPolled(sourcePage.getExternalPageId());
                            gsObject.setLangName(targetLang);
                            gsJobObjectList.add(gsObject);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("Error while getting GlobalSight in-progress terms ", e);
        }

        return gsJobObjectList;
    }

    /**
     * To search term entries
     *
     * @param accessToken   accessToken
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     */
    @Override
    @Deprecated
    public void searchTermEntries(String accessToken, QueryAppender queryAppender) {
    }

    /**
     * To get all tm profiles
     *
     * @param accessToken accessToken
     * @return TMProfileInformation obj
     */

    @Override
    public TMProfileInformation getAllTMProfiles(String accessToken) {
        if (StringUtil.isEmpty(accessToken))
            return null;

        TMProfileInformation tmProfileInformation = null;
        try {
            if (ambassadorProxy != null) {

                String xmlResponse = ambassadorProxy.getAllTMProfiles(accessToken);
                JAXBContext jaxbContext = JAXBContext.newInstance(TMProfileInformation.class);
                ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
                Reader reader = new InputStreamReader(input, "UTF-8");
                InputSource is = new InputSource(reader);
                is.setEncoding("UTF-8");
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                tmProfileInformation = (TMProfileInformation) jaxbUnmarshaller.unmarshal(is);

                logger.debug("successfully got tmProfileInfo" + tmProfileInformation);
            }
        }
        catch (Exception e) {
            logger.error("Error while getting all TM profiles ", e);
        }

        return tmProfileInformation;
    }

    /**
     * To get all term bases
     *
     * @param accessToken accessToken
     * @return TermbaseInformation obj
     */

    @Override
    public TermbaseInformation getAllTermbases(String accessToken) {
        if (StringUtil.isEmpty(accessToken))
            return null;

        TermbaseInformation termBaseInformation = null;
        try {
            if (ambassadorProxy != null) {

                String xmlResponse = ambassadorProxy.getAllTermbases(accessToken);
                JAXBContext jaxbContext = JAXBContext.newInstance(TermbaseInformation.class);
                ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
                Reader reader = new InputStreamReader(input, "UTF-8");
                InputSource is = new InputSource(reader);
                is.setEncoding("UTF-8");
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                termBaseInformation = (TermbaseInformation) jaxbUnmarshaller.unmarshal(is);

                logger.debug("successfully got tmProfileInfo" + termBaseInformation);
            }
        }
        catch (Exception e) {
            logger.error("Error while getting all termbases", e);
        }
        return termBaseInformation;

    }


    /**
     * To search term base
     *
     * @param accessToken   accessToken
     *                      param  termbaseInformation TermbaseInformation
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of terms
     */
    @Override
    public List<GSJobObject> searchTermBase(String accessToken, TermbaseInformation termbaseInformation, QueryAppender queryAppender) {
        if (StringUtil.isEmpty(accessToken) || termbaseInformation == null || queryAppender == null)
            return null;

        List<GSJobObject> gsJobObjectList = new ArrayList<GSJobObject>();
        String searchType = null;
        try {
            searchType = "0".equals(queryAppender.getSearchType()) ? "2" : queryAppender.getSearchType();

            if (ambassadorProxy != null) {
                List<Termbase> termBaseList = termbaseInformation.getTermbase();
                if (queryAppender.getTermbaseName() != null && !queryAppender.getTermbaseName().contains(TeaminologyEnum.SELECT.getValue())) {
                    gsJobObjectList = getGSJobObjectListByTermbase(accessToken, queryAppender.getTermbaseName(), queryAppender, searchType);
                } else {
                    for (Termbase tb : termBaseList) {
                        gsJobObjectList.addAll(getGSJobObjectListByTermbase(accessToken, tb.getName(), queryAppender, searchType));
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("Error while searching termbase", e);
        }

        return gsJobObjectList;
    }

    private List<GSJobObject> getGSJobObjectListByTermbase(String accessToken, String termbaseName, QueryAppender queryAppender, String searchType) {
        List<GSJobObject> gsJobObjectList = new ArrayList<GSJobObject>();

        if (StringUtil.isEmpty(accessToken) || StringUtil.isEmpty(termbaseName) || queryAppender == null)
            return gsJobObjectList;

        TbEntries tbEntries = null;
        String xmlResponse = "";
        GSJobObject gsObject = null;

        try {
            if (!"0".equals(queryAppender.getValues())) {
                xmlResponse = ambassadorProxy.searchTBEntries(accessToken, termbaseName, queryAppender.getSearchTerm(), TeaminologyEnum.ENGLISH_SOURCE.getValue(), queryAppender.getValues(), Double.parseDouble(searchType));
            } else {
                xmlResponse = ambassadorProxy.searchTBEntries(accessToken, termbaseName, queryAppender.getSearchTerm(), TeaminologyEnum.ENGLISH_SOURCE.getValue(), null, Double.parseDouble(searchType));
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(TbEntries.class);
            ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
            Reader reader = new InputStreamReader(input, "UTF-8");
            InputSource is = new InputSource(reader);
            is.setEncoding("UTF-8");
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            tbEntries = (TbEntries) jaxbUnmarshaller.unmarshal(is);

            if (tbEntries != null && tbEntries.getTbEntry() != null && tbEntries.getTbEntry().size() > 0) {
                for (TbEntry tbEntry : tbEntries.getTbEntry()) {
                    gsObject = new GSJobObject();

                    for (Term term : tbEntry.getTerm()) {
                        if ("true".equals(term.getIsSrc())) {
                            gsObject.setTermsBeingPolled(term.getTermContent());
                        } else if ("false".equals(term.getIsSrc())) {
                            gsObject.setSuggestedterms(term.getTermContent());
                        }
                    }
                    gsJobObjectList.add(gsObject);
                }
            }
        }
        catch (Exception e) {
            logger.error("Error found in getGSJobObjectListByTermbase.", e);
        }

        return gsJobObjectList;
    }

    /**
     * To search term information
     *
     * @param accessToken   accessToken
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of terms
     * @parm company Company to filter the terms
     */
    @Override
    public List<GSJobObject> searchTermBaseInfo(String accessToken, QueryAppender queryAppender) {
        List<GSJobObject> gsJobObjectList = new ArrayList<GSJobObject>();
        if (StringUtil.isEmpty(accessToken) || queryAppender == null)
            return gsJobObjectList;

        String searchType;
        try {
            if (ambassadorProxy != null && queryAppender != null) {
                searchType = "0".equals(queryAppender.getSearchType()) ? "2" : queryAppender.getSearchType();
                gsJobObjectList = getGSJobObjectListByTermbase(accessToken, queryAppender.getTermbaseName(), queryAppender, searchType);
            }
        }
        catch (Exception e) {
            logger.error("Error while searching termbase info", e);
        }

        return gsJobObjectList;
    }

    /**
     * To search tm
     *
     * @param accessToken   accessToken
     * @param tmProfileInfo TMProfileInformation
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List terms
     */

    @Override
    public List<GSJobObject> searchTM(String accessToken, TMProfileInformation tmProfileInfo, QueryAppender queryAppender) {
        List<GSJobObject> gsJobObjectList = new ArrayList<GSJobObject>();

        if (StringUtil.isEmpty(accessToken) || tmProfileInfo == null || queryAppender == null)
            return gsJobObjectList;

        try {
            List<TMProfile> tmProfileList = tmProfileInfo.gettMProfile();
            if (ambassadorProxy != null && accessToken != null) {
                if (queryAppender.getTmProfileName() != null && !queryAppender.getTmProfileName().contains(TeaminologyEnum.SELECT.getValue())) {
                    gsJobObjectList = getGSJobObjectListByTM(accessToken, queryAppender.getTmProfileName(), queryAppender);
                } else {
                    for (TMProfile tmProfile : tmProfileList) {
                        gsJobObjectList.addAll(getGSJobObjectListByTM(accessToken, tmProfile.getName(), queryAppender));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while searching TM", e);
        }

        return gsJobObjectList;
    }

    private List<GSJobObject> getGSJobObjectListByTM(String accessToken, String tmProfileName, QueryAppender queryAppender) {
        List<GSJobObject> gsJobObjectList = new ArrayList<GSJobObject>();

        if (StringUtil.isEmpty(tmProfileName))
            return gsJobObjectList;

        Entries entries;
        GSJobObject gsObject;

        try {
            String xmlResponse = ambassadorProxy.searchEntries(accessToken, tmProfileName, queryAppender.getSearchTerm(), TeaminologyEnum.ENGLISH_SOURCE.getValue());

            JAXBContext jaxbContext = JAXBContext.newInstance(Entries.class);
            ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
            Reader reader = new InputStreamReader(input, "UTF-8");
            InputSource is = new InputSource(reader);
            is.setEncoding("UTF-8");
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            entries = (Entries) jaxbUnmarshaller.unmarshal(is);

            if (entries.getEntry() != null && entries.getEntry().size() > 0) {
                for (Entry entry : entries.getEntry()) {
                    gsObject = new GSJobObject();
                    gsObject.setTermsBeingPolled(entry.getSource().getSegment());
                    gsObject.setSourceTermLanguage(entry.getSource().getLocale());
                    for (Target target : entry.getTarget()) {
                        gsObject.setSuggestedterms(target.getSegment());
                        gsObject.setTargetTermLanguage(target.getLocale());
                    }
                    gsJobObjectList.add(gsObject);
                }
            }
        }
        catch (Exception e) {
            logger.error("Error found in getGSJobOjbectListByTM", e);
        }

        return gsJobObjectList;
    }


    /**
     * To getall locale pairs
     *
     * @param accessToken
     * @return LocalePairInformation  w.r.t company
     */

    @Override
    public LocalePairInformation getAllLocalePairs(String accessToken) {
        if (StringUtil.isEmpty(accessToken))
            return null;

        LocalePairInformation localePairInformation = null;
        try {
            if (ambassadorProxy != null) {
                String xmlResponse = ambassadorProxy.getAllLocalePairs(accessToken);

                JAXBContext jaxbContext = JAXBContext.newInstance(LocalePairInformation.class);
                ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
                Reader reader = new InputStreamReader(input, "UTF-8");
                InputSource is = new InputSource(reader);
                is.setEncoding("UTF-8");
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                localePairInformation = (LocalePairInformation) jaxbUnmarshaller.unmarshal(is);

                logger.debug("successfully got localePairInformation" + localePairInformation);
            }
        }
        catch (Exception e) {
            logger.error("Error while getting all locale pairs", e);
        }

        return localePairInformation;
    }


    private Document getDocument(File file) throws Exception {
        Document document;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbFactory.setValidating(false);
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            XmlReader xmlReader = new XmlReader(file);
            String encoding = StringUtil.isEmpty(xmlReader.getEncoding()) ? "UTF-8" : xmlReader.getEncoding();
            if (encoding.contains("UTF-16LE")) {
                encoding = "UTF-16";
            }
            InputStream inputStream = new FileInputStream(file);
            InputSource is = new InputSource(new InputStreamReader(inputStream, encoding));
            document = documentBuilder.parse(is);
        }
        catch (Exception e) {
            throw new Exception("Cannot get document by file [" + file.getAbsolutePath() + "]");
        }

        return document;
    }

    /**
     * To store uploaded file info
     *
     * @param file     Uploaded file
     * @param document XML document by uploaded file
     * @return FileInfo           Stored file info
     */
    @Transactional
    private FileInfo saveUploadedFileInfo(File file, Document document) {
        String xliffVersion = null;
        String fileName = null;
        String sourceLocale = null;
        String targetLocale = null;
        String phanesName = null;
        String processName = null;
        String pageId = null;
        String activityType = null;
        String userName = null;
        String acceptTime = null;
        String documentForm = null;
        String placeHolder = null;
        Integer exactMatch = null;
        Integer fuzzyMatch = null;
        Integer noMatch = null;
        Integer inContextMatch = null;
        String editAll = null;
        Integer gsTmProfileId = null;
        String gsTmProfileName = null;
        String gsTermbase = null;
        String tmpValue = "";

        NodeList xliffList = document.getElementsByTagName(GlobalsightEnum.XLIFF.getValue());

        //Get xliff version
        if (xliffList != null && xliffList.item(0).getAttributes().getNamedItem(GlobalsightEnum.VERSION.getValue()) != null)
            xliffVersion = xliffList.item(0).getAttributes().getNamedItem(GlobalsightEnum.VERSION.getValue()).getNodeValue();

        //Get file info
        NodeList fileList = document.getElementsByTagName(GlobalsightEnum.FILE.getValue());
        if (fileList != null && fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.ORIGINAL.getValue()) != null)
            fileName = fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.ORIGINAL.getValue()).getNodeValue();
        if (fileList != null && fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.TARGET_LANGUAGE.getValue()) != null)
            targetLocale = fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.TARGET_LANGUAGE.getValue()).getNodeValue();
        if (fileList != null && fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.SOURCE_LANGUAGE.getValue()) != null)
            sourceLocale = fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.SOURCE_LANGUAGE.getValue()).getNodeValue();

        NodeList phaseList = document.getElementsByTagName(GlobalsightEnum.PHASE.getValue());
        if (phaseList != null && phaseList.item(0).getAttributes().getNamedItem(GlobalsightEnum.PHASE_NAME.getValue()) != null)
            phanesName = phaseList.item(0).getAttributes().getNamedItem(GlobalsightEnum.PHASE_NAME.getValue()).getNodeValue();

        if (phaseList != null && phaseList.item(0).getAttributes().getNamedItem(GlobalsightEnum.PROCESS_NAME.getValue()) != null)
            processName = phaseList.item(0).getAttributes().getNamedItem(GlobalsightEnum.PROCESS_NAME.getValue()).getNodeValue();

        //Get note info
        NodeList noteList = document.getElementsByTagName(GlobalsightEnum.NOTE.getValue());
        if (noteList != null) {
            for (int h = 0; h < noteList.getLength(); h++) {
                Node headerNode = noteList.item(h);
                if (headerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element transElement = (Element) headerNode;
                    String headerInfo = transElement.getFirstChild().getNodeValue();

                    StringTokenizer st = new StringTokenizer(headerInfo, "#");
                    while (st.hasMoreTokens()) {
                        String key = st.nextToken();
                        if (StringUtil.isEmpty(sourceLocale) && key.contains(GlobalsightEnum.SOURCE_LOCALE.getValue())) {
                            sourceLocale = key.substring(GlobalsightEnum.SOURCE_LOCALE.getValue().length() + 1);
                        } else if (StringUtil.isEmpty(targetLocale) && key.contains(GlobalsightEnum.TARGETLOCALE.getValue())) {
                            targetLocale = key.substring(GlobalsightEnum.TARGETLOCALE.getValue().length() + 1);
                        } else if (key.contains(GlobalsightEnum.PAGEID.getValue())) {
                            pageId = key.substring(GlobalsightEnum.PAGEID.getValue().length() + 1);
                        } else if (key.contains(GlobalsightEnum.ACTIVITY_TYPE.getValue())) {
                            activityType = key.substring(GlobalsightEnum.ACTIVITY_TYPE.getValue().length() + 1);
                        } else if (key.contains(GlobalsightEnum.USER_NAME.getValue())) {
                            userName = key.substring(GlobalsightEnum.USER_NAME.getValue().length() + 1);
                        } else if (key.contains(GlobalsightEnum.ACCEPT_TIME.getValue())) {
                            acceptTime = key.substring(GlobalsightEnum.ACCEPT_TIME.getValue().length() + 1);
                        } else if (key.contains(GlobalsightEnum.DOCUMENT_FORMATE.getValue())) {
                            documentForm = key.substring(GlobalsightEnum.DOCUMENT_FORMATE.getValue().length() + 1);
                        } else if (key.contains(GlobalsightEnum.PLACE_HOLDER_FORMATE.getValue())) {
                            placeHolder = key.substring(GlobalsightEnum.PLACE_HOLDER_FORMATE.getValue().length() + 1);
                        } else if (key.contains(GlobalsightEnum.EXACT_MATCH.getValue())) {
                            tmpValue = key.substring(GlobalsightEnum.EXACT_MATCH.getValue().length() + 1).trim();
                            exactMatch = StringUtil.isEmpty(tmpValue) ? Integer.valueOf(0) : Integer.valueOf(tmpValue);
                        } else if (key.contains(GlobalsightEnum.FUZZY_MATCH.getValue())) {
                            tmpValue = key.substring(GlobalsightEnum.FUZZY_MATCH.getValue().length() + 1).trim();
                            fuzzyMatch = StringUtil.isEmpty(tmpValue) ? Integer.valueOf(0) : Integer.valueOf(tmpValue);
                        } else if (key.contains(GlobalsightEnum.NO_MATCH.getValue())) {
                            tmpValue = key.substring(GlobalsightEnum.NO_MATCH.getValue().length() + 1).trim();
                            noMatch = StringUtil.isEmpty(tmpValue) ? Integer.valueOf(0) : Integer.valueOf(tmpValue);
                        } else if (key.contains(GlobalsightEnum.IN_CONTEXT_MATCH.getValue())) {
                            tmpValue = key.substring(GlobalsightEnum.IN_CONTEXT_MATCH.getValue().length() + 1).trim();
                            inContextMatch = StringUtil.isEmpty(tmpValue) ? Integer.valueOf(0) : Integer.valueOf(tmpValue);
                        } else if (key.contains(GlobalsightEnum.EDIT_ALL.getValue())) {
                            editAll = key.substring(GlobalsightEnum.EDIT_ALL.getValue().length() + 1);
                        } else if (key.contains(GlobalsightEnum.GS_TM_PROFILE_ID.getValue())) {
                            tmpValue = key.substring(GlobalsightEnum.GS_TM_PROFILE_ID.getValue().length() + 1).trim();
                            gsTmProfileId = StringUtil.isEmpty(tmpValue) ? Integer.valueOf(0) : Integer.valueOf(tmpValue);
                        } else if (key.contains(GlobalsightEnum.GS_TM_PROFILE_NAME.getValue())) {
                            gsTmProfileName = key.substring(GlobalsightEnum.GS_TM_PROFILE_NAME.getValue().length() + 1);
                        } else if (key.contains(GlobalsightEnum.GS_TERMBASE.getValue())) {
                            gsTermbase = key.substring(GlobalsightEnum.GS_TERMBASE.getValue().length() + 1);
                        }
                    }
                }
            }
        }

        Language sourceLanguage = lookUpDAO.getLanguageByCode(sourceLocale, true);
        Language targetLanguage = lookUpDAO.getLanguageByCode(targetLocale, true);

        if (StringUtil.isNotEmpty(fileName)) {
            fileName = fileName.replaceAll(" ", "_");
        } else {
            fileName = file.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }

        //Save uploaded file info
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileId(Integer.parseInt(pageId.trim()));
        fileInfo.setFileName(fileName);
        fileInfo.setSourceLang(sourceLanguage);
        fileInfo.setTargetLang(targetLanguage);
        fileInfo.setStatus("In progress");
        fileInfo.setCreateDate(new Date());
        fileInfo.setIsActive("Y");
        fileInfo.setActivityType(activityType);
        fileInfo.setUserName(userName);
        fileInfo.setAcceptTime(acceptTime);
        fileInfo.setDocumentForm(documentForm);
        fileInfo.setPlaceHolder(placeHolder);
        fileInfo.setExactMatch(exactMatch);
        fileInfo.setFuzzyMatch(fuzzyMatch);
        fileInfo.setNoMatch(noMatch);
        fileInfo.setInContextMatch(inContextMatch);
        fileInfo.setEditAll(editAll);
        fileInfo.setGsTmProfileId(gsTmProfileId);
        fileInfo.setGsTmProfileName(gsTmProfileName);
        fileInfo.setGsTermbase(gsTermbase);
        fileInfo.setXliffVersion(xliffVersion);
        fileInfo.setXmlVersion("1.0");
        fileInfo.setPhanesName(phanesName);
        fileInfo.setProcessName(processName);
        fileInfo.setEncodingType(document.getInputEncoding());
        termDAO.saveFile(fileInfo);

        return fileInfo;
    }

    /**
     * Save translation unit by imported xliff data
     *
     * @param document     Document of data
     * @param fileInfo     Uploaded file infomation
     * @param companyList  Company list
     * @param importStatus Importing status
     * @throws Exception
     */
    @Transactional
    private void saveXliffData(Document document, FileInfo fileInfo, List<Company> companyList, CSVImportStatus importStatus) throws Exception {
        try {
            NodeList transList = document.getElementsByTagName(GlobalsightEnum.TRANS_UNIT.getValue());
            if (transList == null)
                return;

            int insertedCount = 0;
            int rejectedCount = 0;
            List<String> rejectedTermList = new ArrayList<String>();

            String transUnitId = null;
            String translate = null;
            String targetState = null;
            Language sourceLanguage = fileInfo.getSourceLang();
            Language targetLanguage = fileInfo.getTargetLang();

            for (int i = 0; i < transList.getLength(); i++) {
                Node transUnitNode = transList.item(i);
                if (transList.item(i).getAttributes().getNamedItem(GlobalsightEnum.ID.getValue()) != null)
                    transUnitId = transList.item(i).getAttributes().getNamedItem(GlobalsightEnum.ID.getValue()).getNodeValue();
                if (transList.item(i).getAttributes().getNamedItem(GlobalsightEnum.TRANSLATE.getValue()) != null)
                    translate = transList.item(i).getAttributes().getNamedItem(GlobalsightEnum.TRANSLATE.getValue()).getNodeValue();

                if (transUnitNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element transElement = (Element) transUnitNode;
                    String sourceTransEle = getSegNodeForXliff(GlobalsightEnum.SOURCE.getValue(), transElement);
                    NodeList targetList = transElement.getElementsByTagName(GlobalsightEnum.TARGET.getValue());
                    if (targetList.item(0).getAttributes().getNamedItem(GlobalsightEnum.STATE.getValue()) != null)
                        targetState = targetList.item(0).getAttributes().getNamedItem(GlobalsightEnum.STATE.getValue()).getNodeValue();

                    String targetTransEle = getSegNodeForXliff(GlobalsightEnum.TARGET.getValue(), transElement);

                    TermInformation termInformation = new TermInformation();
                    termInformation.setTermBeingPolled(sourceContent);
                    termInformation.setCreateDate(new Date());
                    termInformation.setIsActive("Y");
                    if (sourceLanguage != null)
                        termInformation.setTermLangId(sourceLanguage.getLanguageId());
                    if (targetLanguage != null)
                        termInformation.setSuggestedTermLangId(targetLanguage.getLanguageId());
                    termInformation.setIsTM("Y");

                    NodeList altTransList = transElement.getElementsByTagName(GlobalsightEnum.ALT_TRANS.getValue());
                    String targetTerm = null;
                    Double currMatchQuality = null;
                    Double previousMatchQuality = null;
                    String prevOrigin = null;
                    String termSaveInformation = null;
                    for (int j = 0; j < altTransList.getLength(); j++) {
                        String origin = null;
                        String matchQuality = null;
                        Node targetNode = altTransList.item(j);
                        if (altTransList.item(j).getAttributes().getNamedItem(GlobalsightEnum.ORIGIN.getValue()) != null)
                            origin = altTransList.item(j).getAttributes().getNamedItem(GlobalsightEnum.ORIGIN.getValue()).getNodeValue();
                        if (altTransList.item(j).getAttributes().getNamedItem(GlobalsightEnum.MATCH_QUALITY.getValue()) != null)
                            matchQuality = altTransList.item(j).getAttributes().getNamedItem(GlobalsightEnum.MATCH_QUALITY.getValue()).getNodeValue();
                        if (matchQuality != null) {
                            currMatchQuality = Double.parseDouble(matchQuality);
                        }
                        String altTargetSeg = null;
                        if (targetNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element altTransUnitElement = (Element) altTransList.item(j);
                            NodeList altTargetNodeList = altTransUnitElement.getElementsByTagName(GlobalsightEnum.TARGET.getValue());
                            altTargetSeg = getSegNodeForAltTrans(altTargetNodeList);
                        }
                        if (j == 0) {
                            prevOrigin = origin;
                            targetTerm = altTargetSeg;
                            previousMatchQuality = currMatchQuality;
                        } else {
                            if (prevOrigin == null && origin != null) {
                                prevOrigin = origin;
                                targetTerm = altTargetSeg;
                                previousMatchQuality = currMatchQuality;
                            } else {
                                if ((currMatchQuality != null && previousMatchQuality != null) && currMatchQuality > previousMatchQuality && (origin != null || prevOrigin == null)) {
                                    prevOrigin = origin;
                                    targetTerm = altTargetSeg;
                                    previousMatchQuality = currMatchQuality;
                                }
                            }
                        }
                    }
                    if (targetTerm == null) {
                        targetTerm = targetContent;
                    }
                    termInformation.setSuggestedTerm(targetTerm);
                    for (Company company : companyList) {
                        termInformation.setTermCompany(company);
                        termSaveInformation = termDAO.addNewTerm(termInformation);

                        if (ExecuteStatusEnum.SUCCESS.getValue().equalsIgnoreCase(termSaveInformation)) {
                            GlobalsightTermInfo globalSightTermInfo = new GlobalsightTermInfo();
                            globalSightTermInfo.setOrigin(prevOrigin);
                            globalSightTermInfo.setSourceSegment(termInformation.getTermBeingPolled());
                            globalSightTermInfo.setTargetSegment(targetTerm);
                            globalSightTermInfo.setCreateDate(new Date());
                            globalSightTermInfo.setTermInformationId(termInformation);
                            globalSightTermInfo.setIsActive("Y");
                            if (previousMatchQuality != null)
                                globalSightTermInfo.setMatchQuality(previousMatchQuality.toString());
                            globalSightTermInfo.setTranslate(translate);
                            globalSightTermInfo.setTransUnitId(Integer.parseInt(transUnitId));
                            globalSightTermInfo.setTargetState(targetState);
                            globalSightTermInfo.setFileInfo(fileInfo);
                            globalSightTermInfo.setTargetContent(targetTransEle);
                            globalSightTermInfo.setSourceContent(sourceTransEle);
                            for (Tag tagObj : tagSet) {
                                tagObj.setGlobalsightTermInfo(globalSightTermInfo);
                            }
                            globalSightTermInfo.setTag(tagSet);
                            globalSightTermInfo.setCompany(company);
                            termDAO.saveGlobalSightTerm(globalSightTermInfo);

                            insertedCount++;
                            targetContent = "";
                            sourceContent = "";
                            tagSet = new HashSet<Tag>();
                        } else if (ExecuteStatusEnum.TERM_EXISTED.getValue().equalsIgnoreCase(termSaveInformation)) {
                            rejectedCount++;
                            rejectedTermList.add("Source Term already Exists for termEntry : '" + sourceTransEle + "' for Language Code : " + sourceLanguage.getLanguageLabel());
                        }
                    }
                }
            }
            importStatus.setInsertedCount(insertedCount);
            importStatus.setRejectedCount(rejectedCount);
            importStatus.setUserNames(rejectedTermList);
            importStatus.setTermInformationStatus(ExecuteStatusEnum.SUCCESS.getValue());
        }
        catch (Exception e) {
            throw new Exception("Error found in saveXliffData." + e.getMessage());
        }
    }

    /**
     * To  import Xliff data
     *
     * @param uploadedFile File to be read
     * @param companyIds   Set of company ID
     * @return CSVImportStatus object
     */
    @Transactional
    public <C extends TeaminologyObject> CSVImportStatus getImportXliffData(File uploadedFile, String companyIds) {
        CSVImportStatus csvImportStatus = new CSVImportStatus();
        csvImportStatus.setTermInformationStatus(ExecuteStatusEnum.ERROR.getValue());

        if (uploadedFile == null || StringUtil.isEmpty(companyIds))
            return csvImportStatus;

        try {
            List<Company> companyList = new ArrayList<Company>();
            String selectedIdsArray[] = null;

            selectedIdsArray = StringUtil.split(companyIds, ",");

            Document document = getDocument(uploadedFile);
            if (document == null)
                return csvImportStatus;

            document.getDocumentElement().normalize();

            FileInfo uploadedFileInfo = saveUploadedFileInfo(uploadedFile, document);

            //Get list of companies
            if (selectedIdsArray != null && selectedIdsArray.length > 0) {
                for (int i = 0; i < selectedIdsArray.length; i++) {
                    if (StringUtil.isNotEmpty(selectedIdsArray[i])) {
                        Company company = lookUpDAO.getCompany(Integer.parseInt(selectedIdsArray[i]));
                        if (company != null)
                            companyList.add(company);
                    }
                }
            }

            saveXliffData(document, uploadedFileInfo, companyList, csvImportStatus);
        }
        catch (Exception e) {
            csvImportStatus.setRejectedCount(-1);
            csvImportStatus.setTermInformationStatus(ExecuteStatusEnum.ERROR.getValue());
            logger.error("Error found in getImportXliffData.", e);
        }

        return csvImportStatus;
    }

    /**
     * To  get segment node for xliff
     *
     * @param tag,element
     * @return segment node
     */
    private static String getSegNodeForXliff(String tag, Element element) {
        Node node = element.getElementsByTagName(tag).item(0);
//        element.getAttributes();

        Integer sortOrder = 1;
        NodeList transChildNodeList = node.getChildNodes();

        StringBuilder segments = new StringBuilder();

        for (int i = 0; i < transChildNodeList.getLength(); i++) {
            Node transChildNode = transChildNodeList.item(i);
            Tag tagObj = new Tag();
            tagObj.setSortOrder(sortOrder);

            if (GlobalsightEnum.SOURCE.getValue().equalsIgnoreCase(tag)) {
                tagObj.setIsSource("Y");
            } else {
                tagObj.setIsSource("N");
            }

            Attr attribute;
            String attributeString = "";

            if (transChildNode.getNodeType() == Node.ELEMENT_NODE) {
                Set<Attributes> attributeSet = new HashSet<Attributes>();
                String transChildNodeName = transChildNode.getNodeName();
                tagObj.setTagName(transChildNodeName);

                Node transTextNode = transChildNode.getFirstChild();

                if (transChildNode.hasAttributes()) {
                    NamedNodeMap attrs = transChildNode.getAttributes();
                    for (int j = 0; j < attrs.getLength(); j++) {
                        Attributes attributesObj = new Attributes();
                        attribute = (Attr) attrs.item(j);
                        attributeString += attribute.getName() + " = " + '"'
                                + attribute.getValue() + '"' + " ";
                        attributesObj.setAttributeName(attribute.getName());
                        attributesObj.setAttributeValue(attribute.getValue());
                        attributesObj.setTag(tagObj);
                        attributeSet.add(attributesObj);
                    }
                    tagObj.setAttribute(attributeSet);
                }
                int no_of_childs = transChildNode.getChildNodes().getLength();
                if (no_of_childs == 1) {
                    tagObj.setTagValue(transTextNode.getNodeValue());
                    segments.append("<")
                            .append(transChildNodeName)
                            .append(" ")
                            .append(attributeString.trim())
                            .append(">")
                            .append(transTextNode.getNodeValue().trim())
                            .append("</")
                            .append(transChildNodeName)
                            .append(">");
                } else {
                    NodeList ChildNodeList = transChildNode.getChildNodes();
                    attribute = null;
                    attributeString = "";

                    if (transChildNode.hasAttributes()) {
                        NamedNodeMap attrs = transChildNode.getAttributes();
                        for (int j = 0; j < attrs.getLength(); j++) {
                            attribute = (Attr) attrs.item(j);
                            attributeString += attribute.getName() + " = "
                                    + '"' + attribute.getValue()
                                    + '"' + " ";
                        }
                    }
                    segments.append("<")
                            .append(transChildNode.getNodeName())
                            .append(" ")
                            .append(attributeString.trim())
                            .append(">");
                    for (int k = 0; k < ChildNodeList.getLength(); k++) {
                        Node ChildNode = ChildNodeList.item(k);

                        if (ChildNode.getNodeType() == Node.ELEMENT_NODE) {
                            String ChildNodeName = ChildNode.getNodeName();
                            Node TextNode = ChildNode.getFirstChild();
                            if (TextNode.getNodeName() != null) {
                                segments.append("<")
                                        .append(ChildNodeName)
                                        .append(">")
                                        .append(TextNode.getNodeValue())
                                        .append("</")
                                        .append(ChildNodeName)
                                        .append(">");
                            }
                        } else {
                            segments.append(ChildNode.getNodeValue());
                        }
                    }
                    segments.append("</").append(transChildNode.getNodeName()).append(">");
                }
            } else {
                segments.append(transChildNode.getNodeValue());
                tagObj.setTagName("TextTag");
                if (tag.equalsIgnoreCase("Source")) {
                    sourceContent = sourceContent + transChildNode.getNodeValue();
                } else {
                    targetContent = targetContent + transChildNode.getNodeValue();
                }
            }
            sortOrder++;
            tagSet.add(tagObj);
        }

        return segments.toString();
    }

    /**
     * To  get segment node  for alt Trans
     *
     * @param altTransNodeList NodeList
     * @return segment node
     */
    private static String getSegNodeForAltTrans(NodeList altTransNodeList) {
        StringBuilder segment = new StringBuilder();
        for (int i = 0; i < altTransNodeList.getLength(); i++) {
            Node transNodeNode = altTransNodeList.item(i);
            NodeList transChildNodeList = transNodeNode.getChildNodes();

            for (int j = 0; j < transChildNodeList.getLength(); j++) {
                Node transChildNode = transChildNodeList.item(j);

                if (transChildNode.getNodeType() != Node.ELEMENT_NODE)
                    segment.append(transChildNode.getNodeValue());
            }
        }

        return segment.toString();
    }

    /**
     * To get terms by using page id
     *
     * @param pageId An Integer to get the details
     * @return List of terms w.r.t pageId
     */
    @Override
    @Transactional
    public List<GlobalsightTermInfo> getTermsByPageId(Integer pageId) {
        if (pageId == null || pageId.intValue() < 1)
            return null;

        try {
            return termDAO.getTermsByPageId(pageId);
        }
        catch (Exception e) {
            logger.error("Error while getting terms with page id [" + pageId + "]", e);
        }
        return null;
    }

    /**
     * To update file status
     *
     * @param fileInfo FileInfo that has to be updated
     */
    @Override
    @Transactional
    public void updateFileStatus(FileInfo fileInfo) {
        if (fileInfo == null)
            return;

        try {
            termDAO.updateFileStatus(fileInfo);
        }
        catch (Exception e) {
            logger.error("Error while updateing file status", e);
        }
    }

    /**
     * To get file info
     *
     * @param pageId An Integer to get the fileInfo details
     * @return FileInfo w.r.t pageId
     */
    @Override
    @Transactional
    public FileInfo getFileInfo(Integer pageId) {
        if (pageId == null || pageId.intValue() < 1)
            return null;

        try {
            return termDAO.getFileInfo(pageId);
        }
        catch (Exception e) {
            logger.error("Error while getting  file info object", e);
        }

        return null;
    }

    /**
     * To save or update the gs credintials
     *
     * @param company Company to be saved
     * @return Returns "success" if it is registered else it returns "failure"
     */
    @Override
    public String saveOrUpdateGSCredintials(Company company) {
        String status = null;
        if (company == null)
            throw new IllegalArgumentException("Invalid user");

        try {
            status = userDAO.saveOrUpdateGSCredintials(company);
            logger.debug("update user");
        }
        catch (Exception e) {
            logger.error("Error found in saveOrUpdateGSCredintials.", e);
        }

        return status;
    }

    /**
     * To get GS credintials
     *
     * @param companyId Integer to get the credintials details
     * @return Company w.r.t companyId
     */
    @Override
    public Company getGSCredintails(Integer companyId) {
        if (companyId == null || companyId.intValue() < 1)
            return null;

        try {
            return userDAO.getGSCredintails(companyId);
        }
        catch (Exception e) {
            logger.error("Error found in getGSCredintails", e);
        }

        return null;
    }


    /**
     * Used to get Job Ids
     *
     * @param accessToken
     * @return String of job ids *
     */
    public String getJobIds(String accessToken) {
        if (StringUtil.isEmpty(accessToken))
            return null;

        String listOfJobs = null;
        try {
            if (ambassadorProxy != null) {
                listOfJobs = ambassadorProxy.fetchJobIdsPerCompany(accessToken);
                String arr[] = StringUtil.split(listOfJobs, ",");

                Arrays.sort(arr, new Comparator<String>()
                {
                    @Override
                    public int compare(String s1, String s2) {
                        return Integer.parseInt(s2) - Integer.parseInt(s1);
                    }
                });

                listOfJobs = Arrays.toString(arr).replaceAll("[\\[ \\]]", "");
                logger.debug("successfully got job Ids" + listOfJobs);
            }
        }
        catch (Exception e) {
            logger.error("Error found in getJobIds", e);
        }

        return listOfJobs;
    }

    /**
     * To get job count
     *
     * @param accessToken accessToken
     * @return an integer value holding the count jobs
     */

    public Integer getJobsCount(String accessToken) {
        if (StringUtil.isEmpty(accessToken))
            return 0;

        int jobCount = 0;
        try {
            if (ambassadorProxy != null) {
                String jobs = getJobIds(accessToken);
                if (jobs != null) {
                    String arr[] = jobs.split(",");
                    jobCount = arr.length;
                }
            }
        }
        catch (Exception e) {
            logger.error("Error found in getJobsCount", e);
        }

        return jobCount;
    }

    /**
     * Used to get the Job Details
     *
     * @param accessToken
     * @param pageNum     PathVariable Integer to limit the data
     * @return List of Jobs
     *         *
     */
    public List<Jobs> getGlobalSightJobList(String accessToken, int pageNum) {
        if (ambassadorProxy == null || StringUtil.isEmpty(accessToken))
            return null;

        List<Jobs> jobList = new ArrayList<Jobs>();
        Jobs jobs = null;
        try {
            String jobIds = getJobIds(accessToken);
            if (StringUtil.isEmpty(jobIds))
                return jobList;

            String[] jobArr = StringUtil.split(jobIds, ",");
            List jobsList = Arrays.asList(jobArr);

            int jobCount = jobsList.size();
            int fromIndex = 0;
            int toIndex = 0;
            if (pageNum == 0) {
                toIndex = jobCount > 10 ? 10 : jobCount;
            } else {
                pageNum--;
                fromIndex = pageNum * 10;
                toIndex = (pageNum + 1) * 10;
                toIndex = toIndex > jobCount ? jobCount : toIndex;
            }
            jobsList = jobsList.subList(fromIndex, toIndex);
            String[] jobsByPage = (String[]) jobsList.toArray();

            String xmlResponse = ambassadorProxy.fetchJobsPerCompany(accessToken, jobsByPage);

            JAXBContext jaxbContext = JAXBContext.newInstance(Jobs.class);
            ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
            Reader reader = new InputStreamReader(input, "UTF-8");
            InputSource is = new InputSource(reader);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            jobs = (Jobs) jaxbUnmarshaller.unmarshal(is);

            jobList.add(jobs);
            logger.debug("successfully got job List" + jobList.size());

        }
        catch (Exception e) {
            logger.error("Error found in getGlobalSightJobList", e);
        }

        return jobList;
    }

    /**
     * used to get Taks Details for a particular Job
     *
     * @param accessToken
     * @param jobId       Sting to filter the tasks
     * @return List of Taks
     *         *
     */
    public List<Tasks> getTasksInJob(String accessToken, String jobId) {
        List<Tasks> taskList = new ArrayList<Tasks>();

        if (ambassadorProxy == null || StringUtil.isEmpty(accessToken) || StringUtil.isEmpty(jobId))
            return taskList;

        Tasks tasks;
        Integer workFlowId;
        String sourceLang;
        String targetLang;
        Language langObj = null;

        try {
            String xmlResponseJobs = ambassadorProxy.fetchJobsPerCompany(accessToken, new String[]{jobId});

            JAXBContext jaxbContextJob = JAXBContext.newInstance(Jobs.class);
            ByteArrayInputStream inputJob = new ByteArrayInputStream(xmlResponseJobs.getBytes());
            Reader readerJob = new InputStreamReader(inputJob, "UTF-8");
            InputSource isJob = new InputSource(readerJob);
            Unmarshaller jaxbUnmarshallerJob = jaxbContextJob.createUnmarshaller();
            Jobs jobs = (Jobs) jaxbUnmarshallerJob.unmarshal(isJob);

            String xmlResponse = ambassadorProxy.getTasksInJob(accessToken, Long.valueOf(jobId), null);
            JAXBContext jaxbContext = JAXBContext.newInstance(Tasks.class);
            ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
            Reader reader = new InputStreamReader(input, "UTF-8");
            InputSource is = new InputSource(reader);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            tasks = (Tasks) jaxbUnmarshaller.unmarshal(is);

            if (tasks == null)
                return taskList;

            List<Task> newTaskList = new ArrayList<Task>();
            if (tasks != null) {
                for (Task task : tasks.getTask()) {
                    if (task.getName().toLowerCase().contains(GlobalsightEnum.TASK_NAME.getValue().toLowerCase())){
                    	newTaskList.add(task);
                    	logger.info("tasks::::::::::"+newTaskList.size());
                    }
                }
            }

            if (jobs != null) {
                List<FileInfo> fileInfoData = null;
                for (Job job : jobs.getJob()) {
                    fileInfoData = termDAO.getFileInfoByJobIds(new String[]{ String.valueOf(job.getId()) });
                    if (fileInfoData != null && fileInfoData.size() > 0) {
                        for (FileInfo fileData : fileInfoData) {
                            if (fileData.getTaskId() == null)
                                continue;

                            for (Task task : newTaskList) {
                                Integer task1 = Integer.valueOf(fileData.getTaskId());
                                Integer task2 = task.getId();

                                if (task1.intValue() == task2.intValue()) {
                                    task.setFileInfoStatus(fileData.getStatus());
                                    task.setLogUrl(fileData.getLogFileUrl());
                                }
                            }
                        }
                    }

                    if (job.getWorkflows() == null)
                        continue;

                    for (Workflow wrkFlw : job.getWorkflows().getWorkflow()) {
                        for (Task task : newTaskList) {
                            workFlowId = task.getWorkFlowId();
                            if (workFlowId.equals(wrkFlw.getWfId())) {
                                sourceLang = job.getSourceLang();
                                if (sourceLang != null) {
                                    langObj = lookUpDAO.getLanguageByCode(sourceLang, true);
                                }
                                if (langObj != null)
                                    task.setSourceLanguage(langObj.getLanguageLabel());
                                targetLang = wrkFlw.getTargetLang();
                                if (targetLang != null) {
                                    langObj = lookUpDAO.getLanguageByCode(targetLang, true);
                                }
                                if (langObj != null)
                                    task.setTargetLanguage(langObj.getLanguageLabel());
                            }
                        }
                    }
                }
            }
            Tasks newTasks = new Tasks();
            newTasks.setJobId(tasks.getJobId());
            newTasks.setTask(newTaskList);
            taskList.add(newTasks);
            logger.debug("successfully got job List" + taskList.size());
        }
        catch (Exception e) {
            logger.error("Error found in getting tasks of job", e);
        }

        return taskList;
    }

    /**
     * To get file information by task id
     *
     * @param taskId Integer of filter task ids
     * @return FileInfo w.r.t taskId
     */
    @Override
    public FileInfo getFileInfoByTaskId(Integer taskId) {
        if (taskId == null || taskId.intValue() < 1)
            return null;

        try {
            return termDAO.getFileInfoByTaskId(taskId);
        }
        catch (Exception e) {
            logger.error("Error found when getting file info by task [" + taskId + "]", e);
        }

        return null;
    }

    /**
     * return GlobalsightTermInfo object using gsTermids.
     *
     * @param gsids lis of Gs term ids.
     * @return Globalsight termInformation list
     */
    @Override
    @Transactional
    public List<GlobalsightTermInfo> getGSTermsByTermIdsList(List<Integer> gsids) {
        if (gsids == null || gsids.size() == 0)
            return null;

        try {
            return termDAO.getGSTermsByTermIdsList(gsids);
        }
        catch (Exception e) {
            logger.error("Error found when getting GS terms by term ids", e);
        }

        return null;
    }

    /**
     * To get jobs by 'DISPATCHED' state
     *
     * @param accessToken
     * @param pageNum     PathVariable Integer to limit the data
     * @return List of Jobs
     */
    @Override
    @Transactional
    public List<Jobs> getJobsByState(String accessToken, Integer pageNum) {
        if (ambassadorProxy == null || StringUtil.isEmpty(accessToken))
            return null;

        List<Jobs> jobList = null;
        Jobs jobs = null;
        int offSet = 1;
        int count = 10;
        try {
            if (pageNum != null && pageNum > 1) {
                offSet = (pageNum - 1) * 10 + 2;
            }

            jobList = new ArrayList<Jobs>();

            String xmlResponse = ambassadorProxy.fetchJobsByState(accessToken, GSJobStateEnum.DISPATCHED.getValue(), offSet, count, true);

            JAXBContext jaxbContext = JAXBContext.newInstance(Jobs.class);
            ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
            Reader reader = new InputStreamReader(input, "UTF-8");
            InputSource is = new InputSource(reader);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            jobs = (Jobs) jaxbUnmarshaller.unmarshal(is);

            jobList.add(jobs);
            logger.debug("successfully got job List" + jobList.size());
        }
        catch (Exception e) {
            logger.error("Error found when getting jobs by state", e);
        }

        return jobList;
    }

    /**
     * To get job count by state
     *
     * @return Integer holding  the total job count
     */
    @Override
    @Transactional
    public Integer getJobsCountByState(String accessToken) {
        if (ambassadorProxy == null || StringUtil.isEmpty(accessToken))
            return 0;

        int dispatchedStateCount = 0;
        try {
            String countsOfJobState = ambassadorProxy.getCountsByJobState(accessToken);
            JAXBContext jaxbContext = JAXBContext.newInstance(Counts.class);
            ByteArrayInputStream input = new ByteArrayInputStream(countsOfJobState.getBytes());
            Reader reader = new InputStreamReader(input, "UTF-8");
            InputSource is = new InputSource(reader);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Counts counts = (Counts) jaxbUnmarshaller.unmarshal(is);

            List<CountByState> countByStateList = counts == null ? null : counts.getCountByState();
            if (countByStateList != null && countByStateList.size() > 0) {
                for (CountByState countByState : countByStateList) {
                    if (GSJobStateEnum.DISPATCHED.getValue().equalsIgnoreCase(countByState.getState()))
                        dispatchedStateCount = countByState.getCount();
                }
            }
            logger.debug("successfully got job Ids" + dispatchedStateCount);
        }
        catch (Exception e) {
            logger.error("Error found when getting jobs count by state", e);
        }

        return dispatchedStateCount;
    }

}
