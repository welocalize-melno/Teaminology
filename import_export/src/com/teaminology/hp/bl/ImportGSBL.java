package com.teaminology.hp.bl;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.globalsight.www.webservices.AmbassadorProxy;
import com.sun.syndication.io.XmlReader;
import com.teaminology.hp.Enum.GlobalsightEnum;
import com.teaminology.hp.Enum.ImportExportEnum;
import com.teaminology.hp.ImpExp.dao.IUtilDAO;
import com.teaminology.hp.ImpExp.dao.hibernate.UtilDAOImpl;
import com.teaminology.hp.bo.Attributes;
import com.teaminology.hp.bo.Company;
import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.GsCreditials;
import com.teaminology.hp.bo.Language;
import com.teaminology.hp.bo.Tag;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.dao.ImpExp.hibernate.ExportDAOImpl;
import com.teaminology.hp.dao.ImpExp.hibernate.ImportGSDAOImpl;
import com.teaminology.hp.gs.ErrorXml;
import com.teaminology.hp.gs.OfflineFiles;
import com.teaminology.hp.util.ImportExportProperty;
import com.teaminology.hp.util.Util;

/**
 * used to read the content from uploaded file and save the data into tm_profile_info,tm_properties tables
 *
 * @author Sushma
 */
public class ImportGSBL
{
    private static Logger logger = Logger.getLogger(ImportGSBL.class);
    static File uploadedFile = null;


    private static final String LOG_URL_DIR_PATH = ImportExportProperty.LOGS_PATH.getValue();
    private static final String IMPORT_FILES_PATH = ImportExportProperty.UPLOAD_PATH.getValue();
    private static final String DOWNLOAD_FILE_PATH = ImportExportProperty.GS_DOWNLOAD_PATH.getValue();
    private static final String FILE_SEPERATOR = System.getProperty("file.separator");

    public static List<GlobalsightTermInfo> startImport(Integer fileId, Integer companyId) throws Exception {

        long startTimeInMS = System.currentTimeMillis();
        String targetContent = "";
        String sourceContent = "";

        Set<Tag> tagSet = new HashSet<Tag>();
        ImportGSDAOImpl importDAOImpl = ImportGSDAOImpl.getInstance();
        ExportDAOImpl exportDAOImpl = ExportDAOImpl.getInstance();
        List<GlobalsightTermInfo> globalSightTermInformationList = new ArrayList<GlobalsightTermInfo>();
        String logfileName = "teaminologyImport" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + ".log";
        String logFile = LOG_URL_DIR_PATH + "/" + logfileName;
        BufferedWriter bw = new BufferedWriter(new FileWriter(logFile));
        Company company = importDAOImpl.getCompanyById(companyId);
        if (company == null) {
            company = importDAOImpl.getCompanyByLable(ImportExportProperty.ADMIN_COMPANY.getValue());

        }

        FileInfo fileInfo = importDAOImpl.getFileInfo(fileId);
        GsCreditials gscredentials = new GsCreditials();
        gscredentials.setUrl(company.getUrl());
        gscredentials.setUserName(company.getUserName());
        gscredentials.setPassword(company.getPassword());
        AmbassadorProxy proxy = Util.getProxyObject(gscredentials);
        String accesstoken = Util.getAccessToke(proxy, gscredentials);
        String xmlResponse = null;
        try {
            xmlResponse = proxy.downloadXliffOfflineFile(accesstoken, fileInfo.getTaskId());
            JAXBContext jaxbContext = JAXBContext.newInstance(OfflineFiles.class);
            ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
            Reader xmlreader = new InputStreamReader(input, "UTF-8");
            InputSource inputSource = new InputSource(xmlreader);
            inputSource.setEncoding("UTF-8");
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            OfflineFiles offlineFile = (OfflineFiles) jaxbUnmarshaller.unmarshal(inputSource);
            logger.info("++++++++++ offlineFile +++++++++++ " + offlineFile);
            String fileUrl = offlineFile.getOfflineFiles();
            fileUrl = fileUrl.substring(fileUrl.indexOf(ImportExportEnum.RESPONSE_URL_SUBSTR.getValue()), fileUrl.length());
            String hakeUrl = company.getUrl().substring(0, company.getUrl().indexOf(ImportExportEnum.API_URL_SUBSTR.getValue()));
            String apiUrl = hakeUrl + fileUrl;
            String gsFileName = "Globalsight_" + fileInfo.getTaskId() + "_" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSS").format(new Date()) + ".zip";
            logger.info("++++++++++ gsFileName ++++++++++++++ " + gsFileName);
            File downloadPathDir = new File(IMPORT_FILES_PATH + "/" + fileInfo.getTaskId());
            logger.info("++++++++++ downloadPathDir ++++++++++++++ " + downloadPathDir + " path " + IMPORT_FILES_PATH + "/" + fileInfo.getTaskId());

            if (!downloadPathDir.exists()) {
                downloadPathDir.mkdir();
                logger.info(" IN the if condition +++++++++++++++++++++");
            }
            String downloadPath = downloadPathDir.getAbsolutePath() + FILE_SEPERATOR + gsFileName;
            logger.info(" downloadPath downloadPath +++++++++++++++++++++" + downloadPath);
            Util.saveFileFromUrlWithJavaIO(downloadPath, apiUrl);
            logger.info(" +++++ File saving completed +++++++++++++");
            String destinationDir = DOWNLOAD_FILE_PATH + FILE_SEPERATOR + fileInfo.getTaskId() + FILE_SEPERATOR;
            logger.info(" +++++ destinationDir  +++++++++++++ " + destinationDir);
            gsFileName = destinationDir + FILE_SEPERATOR + gsFileName;
            logger.info(" +++++ gsFileName  +++++++++++++ " + gsFileName);
            System.out.println("name:::::" + gsFileName);
            //unzipMyZip(fileName, destinationDir+"\\"+"Kaysi_demo_582533066_en_US_fr_FR");
            File outputFolder = Util.unzip(new File(downloadPath), new File(destinationDir));
            System.out.println("outputFolder:::::" + outputFolder.getPath());
            logger.info("outputFolder:::::" + outputFolder.getPath());
            if (outputFolder != null) {
                logger.info(" +++++ inside outputFolder ///////////////");
                gsFileName = fileUrl.substring(fileUrl.lastIndexOf(FILE_SEPERATOR) + 1);
            }
            System.out.println("outputDirectory:::::" + outputFolder.getParent());

            System.out.println(outputFolder.getParent().substring(0, outputFolder.getParent().lastIndexOf(FILE_SEPERATOR)));

            String dir = outputFolder.getParent().substring(0, outputFolder.getParent().lastIndexOf(FILE_SEPERATOR));

            String xliffFileLoc = dir + FILE_SEPERATOR + "inbox" + FILE_SEPERATOR;
            System.out.println(xliffFileLoc);
            File folder = new File(xliffFileLoc);
            File[] listOfFiles = folder.listFiles();
            String finalXliffFilepath = null;
            for (File listOfFile : listOfFiles) {
                //  if (listOfFile.isDirectory())
                System.out.println(listOfFile.getName());
                finalXliffFilepath = xliffFileLoc + listOfFile.getName();
                System.out.println(finalXliffFilepath);
                String ftaskId = fileInfo.getTaskId();
                String ftaskName = fileInfo.getTaskName();
                String fJobId = fileInfo.getJobId();
                String fJobName = fileInfo.getJobName();
                Integer fUserId = fileInfo.getCreatedBy();

                File logFileDir = new File(LOG_URL_DIR_PATH);
                if (!logFileDir.exists()) {
                    logFileDir.mkdir();
                }

                File uploadedFile = new File(finalXliffFilepath);


                String errorDisp = "";
                List<String> rejectedTermList = new ArrayList<String>();

                if (uploadedFile != null) {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    dbFactory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                    dbFactory.setValidating(false);
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = null;
                    XmlReader encodingType = new XmlReader(uploadedFile);
                    String encodingVal = (encodingType.getEncoding() != null || encodingType.getEncoding() != "") ? encodingType.getEncoding() : "UTF-8";
                    if (encodingVal.contains("UTF-16LE")) {
                        encodingVal = "UTF-16";

                    }

                    InputStream inputStream = new FileInputStream(uploadedFile);
                    Reader reader = new InputStreamReader(inputStream, encodingVal);

                    InputSource is = new InputSource(reader);
                    is.setEncoding(encodingVal);
                    doc = dBuilder.parse(is);


                    doc.getDocumentElement().normalize();
                    String sourceLocale = null;
                    String targetLocale = null;
                    String pageId = null;
                    String workFlowId = null;
                    String taskId = null;
                    String fileName = null;
                    String translate = null;
                    String transUnitId = null;
                    String targetState = null;
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
                    String xliffVersion = null;
                    String phanesName = null;
                    String processName = null;
                    NodeList xliffList = doc.getElementsByTagName(GlobalsightEnum.XLIFF.getValue());
                    if (xliffList != null && xliffList.item(0).getAttributes().getNamedItem(GlobalsightEnum.VERSION.getValue()) != null)
                        xliffVersion = xliffList.item(0).getAttributes().getNamedItem(GlobalsightEnum.VERSION.getValue()).getNodeValue();
                    NodeList fileList = doc.getElementsByTagName(GlobalsightEnum.FILE.getValue());
                    if (fileList != null && fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.ORIGINAL.getValue()) != null)
                        fileName = fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.ORIGINAL.getValue()).getNodeValue();
                    if (fileList != null && fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.TARGET_LANGUAGE.getValue()) != null)
                        targetLocale = fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.TARGET_LANGUAGE.getValue()).getNodeValue();
                    if (fileList != null && fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.SOURCE_LANGUAGE.getValue()) != null)
                        sourceLocale = fileList.item(0).getAttributes().getNamedItem(GlobalsightEnum.SOURCE_LANGUAGE.getValue()).getNodeValue();

                    NodeList phaseList = doc.getElementsByTagName(GlobalsightEnum.PHASE.getValue());
                    if (phaseList != null && phaseList.item(0).getAttributes().getNamedItem(GlobalsightEnum.PHASE_NAME.getValue()) != null)
                        phanesName = phaseList.item(0).getAttributes().getNamedItem(GlobalsightEnum.PHASE_NAME.getValue()).getNodeValue();

                    if (phaseList != null && phaseList.item(0).getAttributes().getNamedItem(GlobalsightEnum.PROCESS_NAME.getValue()) != null)
                        processName = phaseList.item(0).getAttributes().getNamedItem(GlobalsightEnum.PROCESS_NAME.getValue()).getNodeValue();

                    NodeList noteList = doc.getElementsByTagName(GlobalsightEnum.NOTE.getValue());
                    if (noteList != null) {
                        for (int h = 0; h < noteList.getLength(); h++) {
                            Node headerNode = noteList.item(h);
                            if (headerNode.getNodeType() == Node.ELEMENT_NODE) {

                                Element transElement = (Element) headerNode;
                                String headerInfo = transElement.getFirstChild().getNodeValue();

                                StringTokenizer st = new StringTokenizer(headerInfo, "#");
                                while (st.hasMoreTokens()) {
                                    String key = st.nextToken();
                                    if (sourceLocale == null) {
                                        if (key.contains(GlobalsightEnum.SOURCE_LOCALE.getValue())) {
                                            sourceLocale = key.substring(GlobalsightEnum.SOURCE_LOCALE.getValue().length() + 1);
                                        }
                                    }
                                    if (targetLocale == null) {
                                        if (key.contains(GlobalsightEnum.TARGETLOCALE.getValue())) {
                                            targetLocale = key.substring(GlobalsightEnum.TARGETLOCALE.getValue().length() + 1);
                                        }
                                    }
                                    if (key.contains(GlobalsightEnum.PAGEID.getValue())) {
                                        pageId = key.substring(GlobalsightEnum.PAGEID.getValue().length() + 1);
                                    }
                                    if (key.contains(GlobalsightEnum.WORKFLOW_ID.getValue())) {
                                        workFlowId = key.substring(GlobalsightEnum.WORKFLOW_ID.getValue().length() + 1);
                                    }
                                    if (key.contains(GlobalsightEnum.TASK_ID.getValue())) {
                                        taskId = key.substring(GlobalsightEnum.TASK_ID.getValue().length() + 1);
                                    }
                                    if (key.contains(GlobalsightEnum.ACTIVITY_TYPE.getValue())) {
                                        activityType = key.substring(GlobalsightEnum.ACTIVITY_TYPE.getValue().length() + 1);
                                    }
                                    if (key.contains(GlobalsightEnum.USER_NAME.getValue())) {
                                        userName = key.substring(GlobalsightEnum.USER_NAME.getValue().length() + 1);
                                    }
                                    if (key.contains(GlobalsightEnum.ACCEPT_TIME.getValue())) {
                                        acceptTime = key.substring(GlobalsightEnum.ACCEPT_TIME.getValue().length() + 1);
                                    }
                                    if (key.contains(GlobalsightEnum.DOCUMENT_FORMATE.getValue())) {
                                        documentForm = key.substring(GlobalsightEnum.DOCUMENT_FORMATE.getValue().length() + 1);
                                    }

                                    if (key.contains(GlobalsightEnum.PLACE_HOLDER_FORMATE.getValue())) {
                                        placeHolder = key.substring(GlobalsightEnum.PLACE_HOLDER_FORMATE.getValue().length() + 1);
                                    }
                                    if (key.contains(GlobalsightEnum.EXACT_MATCH.getValue())) {
                                        String strexactMatch = key.substring(GlobalsightEnum.EXACT_MATCH.getValue().length() + 1).trim();
                                        exactMatch = new Integer(strexactMatch);
                                    }
                                    if (key.contains(GlobalsightEnum.FUZZY_MATCH.getValue())) {
                                        String strfuzzyMatch = key.substring(GlobalsightEnum.FUZZY_MATCH.getValue().length() + 1).trim();
                                        fuzzyMatch = new Integer(strfuzzyMatch);
                                    }
                                    if (key.contains(GlobalsightEnum.NO_MATCH.getValue())) {
                                        String strnoMatch = key.substring(GlobalsightEnum.NO_MATCH.getValue().length() + 1).trim();
                                        noMatch = new Integer(strnoMatch);
                                    }
                                    if (key.contains(GlobalsightEnum.IN_CONTEXT_MATCH.getValue())) {
                                        String strinConyMatch = key.substring(GlobalsightEnum.IN_CONTEXT_MATCH.getValue().length() + 1).trim();
                                        inContextMatch = new Integer(strinConyMatch);
                                    }
                                    if (key.contains(GlobalsightEnum.EDIT_ALL.getValue())) {
                                        editAll = key.substring(GlobalsightEnum.EDIT_ALL.getValue().length() + 1);
                                        
                                    }
                                    if (key.contains(GlobalsightEnum.GS_TM_PROFILE_ID.getValue())) {
                                        String strtmProfileid = key.substring(GlobalsightEnum.GS_TM_PROFILE_ID.getValue().length() + 1).trim();
                                        gsTmProfileId = new Integer(strtmProfileid);
                                    }
                                    if (key.contains(GlobalsightEnum.GS_TM_PROFILE_NAME.getValue())) {
                                        gsTmProfileName = key.substring(GlobalsightEnum.GS_TM_PROFILE_NAME.getValue().length() + 1);
                                    }
                                    if (key.contains(GlobalsightEnum.GS_TERMBASE.getValue())) {
                                        gsTermbase = key.substring(GlobalsightEnum.GS_TERMBASE.getValue().length() + 1);
                                    }
                                }
                            }
                        }
                    }
                    if (sourceLocale != null && sourceLocale.contains("-")) {
                        sourceLocale = sourceLocale.replaceAll("-", "_");
                    }
                    if (targetLocale != null && targetLocale.contains("-")) {
                        targetLocale = targetLocale.replaceAll("-", "_");
                    }
                    IUtilDAO utilDAO = UtilDAOImpl.getInstance();
//							List<Language> languageList =utilDAO.getLookup(Language.class);
                    Session session = exportDAOImpl.getSession();
                    List<Language> languageList = exportDAOImpl.getLanguages(session);
                    Integer sourceLangId = null;
                    Integer targetLangId = null;
                    for (Language language : languageList) {
                        if (language.getLanguageCode().equalsIgnoreCase(sourceLocale)) {
                            sourceLangId = language.getLanguageId();
                            break;
                        }

                    }

                    for (Language language : languageList) {
                        if (language.getLanguageCode().equalsIgnoreCase(targetLocale)) {
                            targetLangId = language.getLanguageId();
                            break;
                        }

                    }


                    if (fileName != null) {
                        fileName = fileName.replaceAll(" ", "_");
                    } else {
                        fileName = uploadedFile.getName();
                        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                    }


                    fileInfo.setFileId(Integer.parseInt(pageId.trim()));
                    fileInfo.setFileName(fileName);
                    fileInfo.setSourceLang(sourceLangId);
                    fileInfo.setTargetLang(targetLangId);
                    fileInfo.setStatus(StatusEnum.IMPORTED.getValue());
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
                    fileInfo.setEncodingType(encodingVal);
                    fileInfo.setWorkFlowId(workFlowId);
                    fileInfo.setLogfileUrl(logFile);


                    importDAOImpl.updateFileInfo(fileInfo);
                    Integer totalRecords = 0;
                    Integer quotient = 0;
                    Integer percentage = 0;
                    Integer percentageCount = 0;
                    Integer insertedCount = 0;
                    Integer rejectedCount = 0;


                    NodeList transList = doc.getElementsByTagName(GlobalsightEnum.TRANS_UNIT.getValue());
                    totalRecords = transList.getLength();
                    quotient = totalRecords / 100;

                    if (transList != null) {
                        for (int s = 0; s < transList.getLength(); s++) {
                            percentageCount++;
                            Integer termId = 0;
                            Node transUnitNode = transList.item(s);
                            if (transList.item(s).getAttributes().getNamedItem(GlobalsightEnum.ID.getValue()) != null)
                                transUnitId = transList.item(s).getAttributes().getNamedItem(GlobalsightEnum.ID.getValue()).getNodeValue();
                            if (transList.item(s).getAttributes().getNamedItem(GlobalsightEnum.TRANSLATE.getValue()) != null)
                                translate = transList.item(s).getAttributes().getNamedItem(GlobalsightEnum.TRANSLATE.getValue()).getNodeValue();

                            if (transUnitNode.getNodeType() == Node.ELEMENT_NODE) {

                                Element transElement = (Element) transUnitNode;
                                //String sourceTransEle=	getSegNodeForXliff(GlobalsightEnum.SOURCE.getValue(),transElement,doc);

                                String tag = GlobalsightEnum.SOURCE.getValue();
                                Element element = transElement;


                                Node n = element.getElementsByTagName(tag).item(0);
                                element.getAttributes();

                                String segval = "";
                                StringBuffer strbuf = null;
                                Integer sortOrder = 1;
                                NodeList transChildNodeList = n.getChildNodes();

                                for (int j = 0; j < transChildNodeList.getLength(); j++) {
                                    Node transChildNode = transChildNodeList.item(j);
                                    Tag tagObj = new Tag();
                                    tagObj.setSortOrder(sortOrder);
                                    if (tag.equalsIgnoreCase(GlobalsightEnum.SOURCE.getValue())) {
                                        tagObj.setIsSource("Y");
                                    } else {
                                        tagObj.setIsSource("N");

                                    }
                                    if (transChildNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Set<Attributes> attributeSet = new HashSet<Attributes>();

                                        String transChildNodeName = transChildNode.getNodeName();
                                        Node transTextNode = transChildNode.getFirstChild();
                                        Attr attribute1 = null;
                                        String attrArray1 = new String();
                                        tagObj.setTagName(transChildNodeName);

                                        if (transChildNode.hasAttributes()) {
                                            NamedNodeMap attrs = transChildNode.getAttributes();
                                            for (int i = 0; i < attrs.getLength(); i++) {
                                                Attributes attributesObj = new Attributes();

                                                attribute1 = (Attr) attrs.item(i);

                                                attrArray1 = attrArray1
                                                        + attribute1.getName().toString() + " = " + '"'
                                                        + attribute1.getValue().toString() + '"' + " ";
                                                attributesObj.setAttributeName(attribute1.getName()
                                                        .toString());
                                                attributesObj.setAttributeValue(attribute1.getValue()
                                                        .toString());
                                                attributesObj.setTag(tagObj);
                                                attributeSet.add(attributesObj);

                                            }
                                            tagObj.setAttribute(attributeSet);
                                        }
                                        int no_of_childs = transChildNode.getChildNodes().getLength();
                                        if (no_of_childs == 1) {
                                            tagObj.setTagValue(transTextNode.getNodeValue().toString());
                                            segval = segval + "<" + transChildNodeName.toString() + " "
                                                    + attrArray1.trim() + ">"
                                                    + transTextNode.getNodeValue().trim().toString()
                                                    + "</" + transChildNodeName.toString() + ">";
                                        } else {
                                            NodeList ChildNodeList = transChildNode.getChildNodes();
                                            Attr attribute = null;
                                            String attrArray = new String();

                                            if (transChildNode.hasAttributes()) {
                                                NamedNodeMap attrs = transChildNode.getAttributes();
                                                for (int i = 0; i < attrs.getLength(); i++) {
                                                    attribute = (Attr) attrs.item(i);
                                                    attrArray = attrArray
                                                            + attribute.getName().toString() + " = "
                                                            + '"' + attribute.getValue().toString()
                                                            + '"' + " ";
                                                }
                                            }
                                            segval = segval + "<" + transChildNode.getNodeName() + " "
                                                    + attrArray.trim() + ">";
                                            for (int k = 0; k < ChildNodeList.getLength(); k++) {
                                                Node ChildNode = ChildNodeList.item(k);

                                                if (ChildNode.getNodeType() == Node.ELEMENT_NODE) {
                                                    String ChildNodeName = ChildNode.getNodeName();
                                                    Node TextNode = ChildNode.getFirstChild();
                                                    if (TextNode.getNodeName() != null) {
                                                        segval = segval + "<"
                                                                + ChildNodeName.toString() + ">"
                                                                + TextNode.getNodeValue() + "</"
                                                                + ChildNodeName.toString() + ">";
                                                    }
                                                } else {
                                                    segval = segval + ChildNode.getNodeValue();
                                                }
                                            }
                                            segval = segval + "</" + transChildNode.getNodeName() + ">";
                                        }

                                    } else {
                                        segval = segval + transChildNode.getNodeValue();
                                        tagObj.setTagName("TextTag");
                                        if (tag.equalsIgnoreCase("Source")) {
                                            sourceContent = sourceContent
                                                    + transChildNode.getNodeValue();
                                        } else {
                                            targetContent = targetContent
                                                    + transChildNode.getNodeValue();

                                        }

                                    }
                                    sortOrder++;
                                    tagSet.add(tagObj);
                                }

                                strbuf = new StringBuffer(segval);
                                String sourceTransEle = strbuf.toString();


                                NodeList targetList = transElement.getElementsByTagName(GlobalsightEnum.TARGET.getValue());
                                if (targetList.item(0).getAttributes().getNamedItem(GlobalsightEnum.STATE.getValue()) != null)
                                    targetState = targetList.item(0).getAttributes().getNamedItem(GlobalsightEnum.STATE.getValue()).getNodeValue();

                                //String targetTransEle=	getSegNodeForXliff(GlobalsightEnum.TARGET.getValue(),transElement,doc);


                                tag = GlobalsightEnum.TARGET.getValue();


                                n = element.getElementsByTagName(tag).item(0);
                                element.getAttributes();

                                segval = "";
                                strbuf = null;
                                sortOrder = 1;
                                transChildNodeList = n.getChildNodes();

                                for (int j = 0; j < transChildNodeList.getLength(); j++) {
                                    Node transChildNode = transChildNodeList.item(j);
                                    Tag tagObj = new Tag();
                                    tagObj.setSortOrder(sortOrder);
                                    if (tag.equalsIgnoreCase(GlobalsightEnum.SOURCE.getValue())) {
                                        tagObj.setIsSource("Y");
                                    } else {
                                        tagObj.setIsSource("N");

                                    }
                                    if (transChildNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Set<Attributes> attributeSet = new HashSet<Attributes>();

                                        String transChildNodeName = transChildNode.getNodeName();
                                        Node transTextNode = transChildNode.getFirstChild();
                                        Attr attribute1 = null;
                                        String attrArray1 = new String();
                                        tagObj.setTagName(transChildNodeName);

                                        if (transChildNode.hasAttributes()) {
                                            NamedNodeMap attrs = transChildNode.getAttributes();
                                            for (int i = 0; i < attrs.getLength(); i++) {
                                                Attributes attributesObj = new Attributes();

                                                attribute1 = (Attr) attrs.item(i);

                                                attrArray1 = attrArray1
                                                        + attribute1.getName().toString() + " = " + '"'
                                                        + attribute1.getValue().toString() + '"' + " ";
                                                attributesObj.setAttributeName(attribute1.getName()
                                                        .toString());
                                                attributesObj.setAttributeValue(attribute1.getValue()
                                                        .toString());
                                                attributesObj.setTag(tagObj);
                                                attributeSet.add(attributesObj);

                                            }
                                            tagObj.setAttribute(attributeSet);
                                        }
                                        int no_of_childs = transChildNode.getChildNodes().getLength();
                                        if (no_of_childs == 1) {
                                            tagObj.setTagValue(transTextNode.getNodeValue().toString());
                                            segval = segval + "<" + transChildNodeName.toString() + " "
                                                    + attrArray1.trim() + ">"
                                                    + transTextNode.getNodeValue().trim().toString()
                                                    + "</" + transChildNodeName.toString() + ">";
                                        } else {
                                            NodeList ChildNodeList = transChildNode.getChildNodes();
                                            Attr attribute = null;
                                            String attrArray = new String();

                                            if (transChildNode.hasAttributes()) {
                                                NamedNodeMap attrs = transChildNode.getAttributes();
                                                for (int i = 0; i < attrs.getLength(); i++) {
                                                    attribute = (Attr) attrs.item(i);
                                                    attrArray = attrArray
                                                            + attribute.getName().toString() + " = "
                                                            + '"' + attribute.getValue().toString()
                                                            + '"' + " ";
                                                }
                                            }
                                            segval = segval + "<" + transChildNode.getNodeName() + " "
                                                    + attrArray.trim() + ">";
                                            for (int k = 0; k < ChildNodeList.getLength(); k++) {
                                                Node ChildNode = ChildNodeList.item(k);

                                                if (ChildNode.getNodeType() == Node.ELEMENT_NODE) {
                                                    String ChildNodeName = ChildNode.getNodeName();
                                                    Node TextNode = ChildNode.getFirstChild();
                                                    if (TextNode.getNodeName() != null) {
                                                        segval = segval + "<"
                                                                + ChildNodeName.toString() + ">"
                                                                + TextNode.getNodeValue() + "</"
                                                                + ChildNodeName.toString() + ">";
                                                    }
                                                } else {
                                                    segval = segval + ChildNode.getNodeValue();
                                                }
                                            }
                                            segval = segval + "</" + transChildNode.getNodeName() + ">";
                                        }

                                    } else {
                                        segval = segval + transChildNode.getNodeValue();
                                        tagObj.setTagName("TextTag");
                                        if (tag.equalsIgnoreCase("Source")) {
                                            sourceContent = sourceContent
                                                    + transChildNode.getNodeValue();
                                        } else {
                                            targetContent = targetContent
                                                    + transChildNode.getNodeValue();

                                        }

                                    }
                                    sortOrder++;
                                    tagSet.add(tagObj);
                                }

                                strbuf = new StringBuffer(segval);
                                String targetTransEle = strbuf.toString();


                                TermInformation termInformation = new TermInformation();
                                termInformation.setTermBeingPolled(sourceContent);
                                termInformation.setCreateDate(new Date());
                                termInformation.setIsActive("Y");
                                if (sourceLangId != null)
                                    termInformation.setTermLangId(sourceLangId);
                                if (targetLangId != null)
                                    termInformation.setSuggestedTermLangId(targetLangId);
                                termInformation.setIsTM("Y");


                                NodeList altTransList = transElement.getElementsByTagName(GlobalsightEnum.ALT_TRANS.getValue());
                                String targetTerm = null;
                                Double currMatchQuality = null;
                                Double previousMatchQuality = null;
                                String prevOrigin = null;
                                String termSaveInformation = null;
                                for (int t = 0; t < altTransList.getLength(); t++) {
                                    String origin = null;
                                    String matchQuality = null;
                                    Node targetNode = altTransList.item(t);
                                    if (altTransList.item(t).getAttributes().getNamedItem(GlobalsightEnum.ORIGIN.getValue()) != null)
                                        origin = altTransList.item(t).getAttributes().getNamedItem(GlobalsightEnum.ORIGIN.getValue()).getNodeValue();
                                    if (altTransList.item(t).getAttributes().getNamedItem(GlobalsightEnum.MATCH_QUALITY.getValue()) != null)
                                        matchQuality = altTransList.item(t).getAttributes().getNamedItem(GlobalsightEnum.MATCH_QUALITY.getValue()).getNodeValue();
                                    if (matchQuality != null) {
                                        currMatchQuality = Double.parseDouble(matchQuality);
                                    }
                                    String altsourceSeg = null;
                                    String altTargetSeg = null;
                                    if (targetNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element altTransUnitElement = (Element) altTransList.item(t);
                                        NodeList altSourceNodeList = altTransUnitElement.getElementsByTagName(GlobalsightEnum.SOURCE.getValue());
                                        altsourceSeg = getSegNodeForAltTrans(altSourceNodeList);

                                        NodeList altTargetNodeList = altTransUnitElement.getElementsByTagName(GlobalsightEnum.TARGET.getValue());
                                        altTargetSeg = getSegNodeForAltTrans(altTargetNodeList);
                                    }
                                    if (t == 0) {
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
                                termInformation.setTermCompany(company);
                                termSaveInformation = importDAOImpl.addNewTerm(termInformation);

                                if (termSaveInformation.equalsIgnoreCase("success")) {
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

                                    //globalSightTermInfo.setTag(tagSet);
                                    globalSightTermInfo.setCompany(company);
                                    termId = importDAOImpl.saveGlobalSightTerm(globalSightTermInfo);
                                    for (Tag tagObj : tagSet) {
                                        Set<Attributes> attributesSet = tagObj.getAttribute();
                                        tagObj.setAttribute(null);
                                        tagObj.setGlobalsightTermInfo(globalSightTermInfo);
                                        importDAOImpl.saveTag(tagObj);
                                        if (attributesSet != null && attributesSet.size() > 0) {
                                            for (Attributes attr : attributesSet) {
                                                attr.setTag(tagObj);
                                                importDAOImpl.saveAttribute(attr);
                                            }
                                        }
                                    }
                                    globalSightTermInformationList.add(globalSightTermInfo);

                                    insertedCount++;
                                    targetContent = "";
                                    sourceContent = "";
                                    tagSet = new HashSet<Tag>();
                                }

                                if (termSaveInformation.equals("failedterm")) {
                                    rejectedCount++;
                                    errorDisp = "Source Term already Exists for termEntry : '" + sourceTransEle + "' for Language Code " + sourceLocale;
                                    rejectedTermList.add(errorDisp);
                                }
                            }


                            logger.info("Current Record>>" + s);
                            logger.info("Total Records>>" + totalRecords);

									
								/*	logger.info("present val>>"+s);
									logger.info("Percentage>>"+percentage);

									if(quotient.intValue()==0){
										
										percentage=(int) ((new Float(percentageCount)/new Float(totalRecords))*100);
										if(percentageCount.intValue()==totalRecords.intValue())
											percentage=100;
									
										if(percentage>5 && percentage<=100){
										fileData.setProcessedPercentage(percentage);
										importDAOImpl.updateFileUploadStatus(fileData);
										}
									
									}*/
                        }

                        //if(percentage==100){
                        logger.info("Percentage 100>>" + percentage);
                        String successMsg = "Successfully Inserted  " + insertedCount + " Records out of  " + totalRecords;
                        bw.write(successMsg);
                        bw.newLine();
                        if (rejectedCount > 0) {
                            String rejectedMsg = "Rejected Count  " + rejectedCount;
                            bw.write(rejectedMsg);
                            bw.newLine();
                            for (int i = 0; i < rejectedTermList.size(); i++) {
                                bw.write(rejectedTermList.get(i));
                                bw.newLine();
                            }
                        }
									
									
									/*FileUploadStatusImp fileData1=importDAOImpl.getFileUrlByFileId(fileIdStr);
									fileData1.setProcessedPercentage(100);
									fileData1.setFileStatus(ImportExportEnum.TEAMINOLOGY_FILE_IMPORT_STATUS.getValue());
									fileData1.setEndTime(new Date());
									importDAOImpl.updateFileUploadStatus(fileData1);*/
                        //}

                    }
                }
                fileInfo = new FileInfo();
                fileInfo.setJobId(fJobId);
                fileInfo.setJobName(fJobName);
                fileInfo.setTaskName(ftaskName);
                fileInfo.setTaskId(ftaskId);
                fileInfo.setCreateDate(new Date());
                fileInfo.setCreatedBy(fUserId);
                fileInfo.setIsActive("Y");
                fileInfo.setStatus("In progress");
            }


        }

        catch (Exception e) {

            fileInfo.setStatus(StatusEnum.IMPORT_FAILED.getValue());
            fileInfo.setLogfileUrl(logFile);
            logger.error(e, e);
            e.printStackTrace();
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(ErrorXml.class);
                ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes());
                Reader reader = new InputStreamReader(input, "UTF-8");
                InputSource is = new InputSource(reader);
                is.setEncoding("UTF-8");
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                ErrorXml errorXml = (ErrorXml) jaxbUnmarshaller.unmarshal(is);
                bw.write(errorXml.getError());
                importDAOImpl.updateFileInfo(fileInfo);
            }
            catch (Exception exception) {
                bw.write(exception.getMessage());
                importDAOImpl.updateFileInfo(fileInfo);
                e.printStackTrace();
            }

        }

        finally {
            bw.close();
            long endTimeInMS = System.currentTimeMillis();
            long totalTimeInMs = endTimeInMS - startTimeInMS;
            double totalTimeInSeconds = totalTimeInMs / 1000D;
            double totalTimeInMinutes = totalTimeInSeconds / 60D;
            String timeTook;
            if (totalTimeInMinutes >= 1D) {
                timeTook = totalTimeInMinutes + " minutes" + " or "
                        + totalTimeInSeconds + " seconds";
            } else {
                timeTook = totalTimeInSeconds + " seconds";
            }
            logger.info("Took total for Import TMX" + timeTook);
            logger.info("startTimeInMS:" + startTimeInMS / 1000
                    + " ending time:" + endTimeInMS / 1000);

        }
        return globalSightTermInformationList;
    }

    private static String getSegNodeForAltTrans(NodeList altTransNodeList) {

        String segval = "";
        for (int i = 0; i < altTransNodeList.getLength(); i++) {
            Node transNodeNode = altTransNodeList.item(i);
            NodeList transChildNodeList = transNodeNode.getChildNodes();

            for (int j = 0; j < transChildNodeList.getLength(); j++) {
                Node transChildNode = transChildNodeList.item(j);

                if (transChildNode.getNodeType() == Node.ELEMENT_NODE) {
                } else {
                    segval = segval + transChildNode.getNodeValue();
                }

            }

        }

        return segval;

    }

    private static String getSegNodeForXliff(String tag, Element element,
                                             Document doc) {/*
		Node n = element.getElementsByTagName(tag).item(0);
		element.getAttributes();

		String segval = "";
		StringBuffer strbuf = null;
		Integer sortOrder = 1;
		NodeList transChildNodeList = n.getChildNodes();

		for (int j = 0; j < transChildNodeList.getLength(); j++) {
			Node transChildNode = transChildNodeList.item(j);
			Tag tagObj = new Tag();
			tagObj.setSortOrder(sortOrder);
			if (tag.equalsIgnoreCase(GlobalsightEnum.SOURCE.getValue())) {
				tagObj.setIsSource("Y");
			} else {
				tagObj.setIsSource("N");

			}
			if (transChildNode.getNodeType() == Node.ELEMENT_NODE) {
				Set<Attributes> attributeSet = new HashSet<Attributes>();

				String transChildNodeName = transChildNode.getNodeName();
				Node transTextNode = transChildNode.getFirstChild();
				Attr attribute1 = null;
				String attrArray1 = new String();
				tagObj.setTagName(transChildNodeName);

				if (transChildNode.hasAttributes()) {
					NamedNodeMap attrs = transChildNode.getAttributes();
					for (int i = 0; i < attrs.getLength(); i++) {
						Attributes attributesObj = new Attributes();

						attribute1 = (Attr) attrs.item(i);

						attrArray1 = attrArray1
								+ attribute1.getName().toString() + " = " + '"'
								+ attribute1.getValue().toString() + '"' + " ";
						attributesObj.setAttributeName(attribute1.getName()
								.toString());
						attributesObj.setAttributeValue(attribute1.getValue()
								.toString());
						attributesObj.setTag(tagObj);
						attributeSet.add(attributesObj);

					}
					tagObj.setAttribute(attributeSet);
				}
				int no_of_childs = transChildNode.getChildNodes().getLength();
				if (no_of_childs == 1) {
					tagObj.setTagValue(transTextNode.getNodeValue().toString());
					segval = segval + "<" + transChildNodeName.toString() + " "
							+ attrArray1.trim() + ">"
							+ transTextNode.getNodeValue().trim().toString()
							+ "</" + transChildNodeName.toString() + ">";
				} else {
					NodeList ChildNodeList = transChildNode.getChildNodes();
					Attr attribute = null;
					String attrArray = new String();

					if (transChildNode.hasAttributes()) {
						NamedNodeMap attrs = transChildNode.getAttributes();
						for (int i = 0; i < attrs.getLength(); i++) {
							attribute = (Attr) attrs.item(i);
							attrArray = attrArray
									+ attribute.getName().toString() + " = "
									+ '"' + attribute.getValue().toString()
									+ '"' + " ";
						}
					}
					segval = segval + "<" + transChildNode.getNodeName() + " "
							+ attrArray.trim() + ">";
					for (int k = 0; k < ChildNodeList.getLength(); k++) {
						Node ChildNode = ChildNodeList.item(k);

						if (ChildNode.getNodeType() == Node.ELEMENT_NODE) {
							String ChildNodeName = ChildNode.getNodeName();
							Node TextNode = ChildNode.getFirstChild();
							if (TextNode.getNodeName() != null) {
								segval = segval + "<"
										+ ChildNodeName.toString() + ">"
										+ TextNode.getNodeValue() + "</"
										+ ChildNodeName.toString() + ">";
							}
						} else {
							segval = segval + ChildNode.getNodeValue();
						}
					}
					segval = segval + "</" + transChildNode.getNodeName() + ">";
				}

			} else {
				segval = segval + transChildNode.getNodeValue();
				tagObj.setTagName("TextTag");
				if (tag.equalsIgnoreCase("Source")) {
					sourceContent = sourceContent
							+ transChildNode.getNodeValue();
				} else {
					targetContent = targetContent
							+ transChildNode.getNodeValue();

				}

			}
			sortOrder++;
			tagSet.add(tagObj);
		}

		strbuf = new StringBuffer(segval);
		return strbuf.toString();
	}*/
        return null;
    }
}