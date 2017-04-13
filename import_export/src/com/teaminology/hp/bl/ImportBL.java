package com.teaminology.hp.bl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.xml.sax.InputSource;
import com.mysql.jdbc.Clob;
import com.sun.syndication.io.XmlReader;
import com.teaminology.hp.Enum.ImportExportEnum;
import com.teaminology.hp.ImpExp.dao.IUtilDAO;
import com.teaminology.hp.ImpExp.dao.hibernate.UtilDAOImpl;
import com.teaminology.hp.bo.ContentType;
import com.teaminology.hp.bo.Domain;
import com.teaminology.hp.bo.FileUploadStatusImp;
import com.teaminology.hp.bo.Language;
import com.teaminology.hp.bo.ProductGroup;
import com.teaminology.hp.bo.TMProfileList;
import com.teaminology.hp.bo.TempTMProperties;
import com.teaminology.hp.bo.TempTmProfileBo;
import com.teaminology.hp.bo.TmProfileBo;
import com.teaminology.hp.bo.UploadStatistics;
import com.teaminology.hp.dao.ImpExp.hibernate.ExportDAOImpl;
import com.teaminology.hp.dao.ImpExp.hibernate.ImportDAOImpl;
import com.teaminology.hp.thread.ImportDeleteTempTMXThread;
import com.teaminology.hp.util.HttpSolrServerInstance;
import com.teaminology.hp.util.ImportExportProperty;
import com.teaminology.hp.util.Util;

/**
 * used to read the content from uploaded file and save the data into tm_profile_info,tm_properties tables
 *
 * @author Sushma
 */
public class ImportBL
{
    private static Logger logger = Logger.getLogger(ImportBL.class);
    static File uploadedFile = null;
    private static final String LOG_URL_DIR_PATH = ImportExportProperty.LOGS_PATH.getValue();
    private static final Integer BATCHSIZE = ImportExportProperty.NO_OF_RECORDS_PER_BATCH.getIntValue();
    public static SolrIndex indexDataSolr = new SolrIndex();
    @SuppressWarnings("unused")
    public static TMProfileList startImport(String fileIdStr, HttpServletRequest req) throws Exception {
        long startTimeInMS = System.currentTimeMillis();

        ImportDAOImpl importDAOImpl = ImportDAOImpl.getInstance();
        ExportDAOImpl exportDAOImpl = ExportDAOImpl.getInstance();
        Session session = importDAOImpl.getSession();
        FileUploadStatusImp fileData = importDAOImpl.getFileUrlByFileId(fileIdStr, session);
        logger.info("Started processing for " + fileIdStr);
        boolean procedureStatus = true;
        if (fileData != null) {
            String fileUrl = fileData.getFileUrl();
            Integer fileId = fileData.getFileUploadStatusId();
            File logFileDir = new File(LOG_URL_DIR_PATH);
            if (!logFileDir.exists()) {
                logFileDir.mkdir();
            }
            String fileName = "teaminologyImport" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSS").format(new Date()) + ".log";
            logger.info("file url" + fileUrl);
            fileData.setFileLogUrl(LOG_URL_DIR_PATH + "/" + fileName);
            fileData.setFileStatus(ImportExportEnum.TEAMINOLOGY_IMPORT_PROGRESS.getValue());

            String logFile = LOG_URL_DIR_PATH + "/" + fileName;
            BufferedWriter bw = null;
            XmlReader encodingType = null;
            InputStream inputStream = null;
            Reader reader = null;
            try {

            	HttpSolrServer solrserver = HttpSolrServerInstance.INSTANCE.getServer();

                String errorDisp = "";
                List<String> rejectedTermList = new ArrayList<String>();
                bw = new BufferedWriter(new FileWriter(logFile));
                logger.info("Checking solr server running status: ");
                solrserver.ping();
                
                //**To get the uploaded File Url **/

                JAXBContext jaxbContext = JAXBContext.newInstance(TMX.class);
                encodingType = new XmlReader(new File(fileUrl));
                String encodingVal = (encodingType.getEncoding() != null || encodingType.getEncoding() != "") ? encodingType.getEncoding() : "UTF-8";
                if (encodingVal.contains("UTF-16LE")) {
                    encodingVal = "UTF-16";
                }
                inputStream = new FileInputStream(fileUrl);
                reader = new InputStreamReader(inputStream, encodingVal);
                InputSource is = new InputSource(reader);
                is.setEncoding(encodingVal);
                XMLInputFactory xif = XMLInputFactory.newFactory();
                xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
                XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(inputStream));

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                TMX fileProfileInformation = (TMX) jaxbUnmarshaller.unmarshal(xsr);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);


                int counter = 0;
                String domainLabel = null;
                String productLabel = null;
                String companyLabel = null;
                String contentTypeLabel = null;
                Integer companyId = null;
                Integer domainId = null;
                Integer productId = null;
                Integer contentTypeId = null;
                Integer targetLangId = null;
                Integer totalRecords = 0;

                Body body = null;

                if (fileProfileInformation != null) {
                    body = fileProfileInformation.getBody();

                    List<TU> tuList = body.getTu();
                    if (tuList != null && !tuList.isEmpty()) {
                        totalRecords = tuList.size();
                    }

                }
                try {
                    if (encodingType != null) {
                        encodingType.close();
                    }

                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                }
                catch (Exception e) {
                    logger.error("Exception in closing the IO objects");
                    logger.error(e, e);
                }
                fileData.setProcessedPercentage(2);
                fileData.setTotalRecords(totalRecords);
                importDAOImpl.updateFileUploadStatus(fileData, session);
                UploadStatistics upLoadStatistics = new UploadStatistics();
                Integer insertedCount = 0;
                Integer updateCount = 0;
                Integer rejectedCount = 0;
                Integer skippedCount = 0;
                upLoadStatistics.setInsertCount(insertedCount);
                upLoadStatistics.setUpdateCount(updateCount);
                upLoadStatistics.setSkippedCount(skippedCount);

                TMProfileList tmFinalObjList = new TMProfileList();
                List<TempTmProfileBo> tmProfileUpdatedList = new ArrayList<TempTmProfileBo>();
                List<TempTmProfileBo> tmProfileInsertedList = null;
                List<Integer> tmPropertiesDeleteList = new ArrayList<Integer>();
                List<TempTMProperties> tmPropertiesInsertedList = new ArrayList<TempTMProperties>();
                long startTm1TimeInMS = 0;

                if (fileProfileInformation != null) {

                    tmProfileInsertedList = new ArrayList<TempTmProfileBo>();

                    String uniqueId = UUID.randomUUID().toString();
                    fileData.setPropref(uniqueId);
                    Header header = fileProfileInformation.getHeader();

                    // Header Operation
                    if (header != null && header.getProp() != null && !header.getProp().isEmpty()) {
                        // Get the Attributes
                        for (Prop prop : header.getProp()) {
                            if (prop.getPropType().equals("x-companyteam")) {
                                companyLabel = prop.getProp();
                                companyId = importDAOImpl.getCompanyId(companyLabel, session);

                            } else if (prop.getPropType().equals("x-domainteam")) {
                                domainLabel = prop.getProp();
                                domainId = importDAOImpl.getDomainId(domainLabel, session);

                                // New Domain save it
                                if (domainId == null) {
                                    Domain domain = new Domain();
                                    domain.setDomain(domainLabel);
                                    domain.setCreateDate(new Date());
                                    domain.setIsActive("Y");
                                    importDAOImpl.addDomain(domain, session);
                                    domainId = domain.getDomainId();
                                    domain = null;
                                }

                            } else if (prop.getPropType().equals("x-productlineteam")) {
                                productLabel = prop.getProp();
                                productId = importDAOImpl.getProductId(productLabel, session);

                                // New Product save it
                                if (productId == null) {
                                    ProductGroup product = new ProductGroup();
                                    product.setProduct(productLabel);
                                    product.setCreateDate(new Date());
                                    product.setIsActive("Y");
                                    importDAOImpl.addProductGroup(product, session);
                                    productId = product.getProductId();
                                    product = null;
                                }

                            } else if (prop.getPropType().equals("x-contentteam")) {
                                contentTypeLabel = prop.getProp();
                                contentTypeId = importDAOImpl.getContentTypeId(contentTypeLabel, session);

                                //New ContentType save it
                                if (contentTypeId == null) {
                                    ContentType contentType = new ContentType();
                                    contentType.setContentType(contentTypeLabel);
                                    contentType.setCreateDate(new Date());
                                    contentType.setIsActive("Y");
                                    importDAOImpl.addContentType(contentType, session);
                                    contentTypeId = contentType.getContentTypeId();
                                    contentType = null;
                                }
                            } else {
                                TempTMProperties tmProperties = new TempTMProperties();
                                tmProperties.setType(prop.getPropType());
                                tmProperties.setDescription(prop.getProp());
                                tmProperties.setCreateDate(new Date());
                                tmProperties.setPropRef(uniqueId);
                                tmProperties.setIsTu("N");
                                tmPropertiesInsertedList.add(tmProperties);
                                //	importDAOImpl.saveTmproperties(tmProperties);
                            }
                        }


                    }
                    if (companyId == null)
                        companyId = importDAOImpl.getCompanyId(ImportExportProperty.ADMIN_COMPANY.getValue(), session);
                    IUtilDAO utilDAO = UtilDAOImpl.getInstance();

                    List<Language> languageList = exportDAOImpl.getLanguages(session);
                    Map<String/*LanguageCode*/, Language> languageMap = getLanguageMap(languageList);

                    long startTmTimeInMS = System.currentTimeMillis();

                    List<List<TU>> tuProfileBatch = createBatches(body.getTu(), BATCHSIZE);

                    for (List<TU> fileProfileList : tuProfileBatch) {
                        String uuId = fileId + UUID.randomUUID().toString();

                        for (TU fileProfile : fileProfileList) {
                            String tuId = null;
                            String tuCreationId = null;
                            String tuCreationDate = null;
                            String tuChangeId = null;
                            String tuChangeDate = null;
                            String prevLang = null;
                            Integer prevLangId = null;
                            String tuSourceCreationId = null;
                            String tuSourceCreationDate = null;
                            String tuSourceChangeId = null;
                            String tuSourceChangeDate = null;
                            String tuTargetCreationId = null;
                            String tuTargetCreationDate = null;
                            String tuTargetChangeId = null;
                            String tuTargetChangeDate = null;


                            TmProfileBo updateTargetTmprofile = null;
                            TmProfileBo updateTargetTermTmprofile = null;
                            Set<TempTMProperties> tuvtmPropertiesset = null;
                            String previousLang = null;
                            List<TempTmProfileBo> tmprofileList = new ArrayList<TempTmProfileBo>();

                            Set<TempTMProperties> tmPropertiesset = null;


                            counter++;


                            if (fileProfile.getProp() != null && !fileProfile.getProp().isEmpty()) {
                                if (tmPropertiesset == null) {
                                    tmPropertiesset = new HashSet<TempTMProperties>();
                                }


                                Iterator<Prop> iterate = fileProfile.getProp().iterator();

                                while (iterate.hasNext()) {
                                    Prop prop = (Prop) iterate.next();
                                    if (prop != null) {
                                        TempTMProperties tuvProperties = createTMProperty(prop, uniqueId);
                                        tuvProperties.setIsTu("Y");
                                        tmPropertiesset.add(tuvProperties);
                                    }
                                }
                            }

                            tuId = fileProfile.getTuId();
                            tuCreationId = fileProfile.getCreationId();
                            tuCreationDate = fileProfile.getCreationDate();
                            tuChangeId = fileProfile.getChangeId();
                            tuChangeDate = fileProfile.getChangeDate();
                            String Sterm = null;
                            Integer engLangId = null;
                            String SLang = null;

                            // Get the language Id
                            if (languageMap.get("EN_US") != null) {
                                Language language = languageMap.get("EN_US");
                                engLangId = language.getLanguageId();
                            }


                            for (TUV fileProfiles : fileProfile.getTuv()) {
                                TempTmProfileBo tmInformation = new TempTmProfileBo();

                                tuvtmPropertiesset = new HashSet<TempTMProperties>();

                                SLang = fileProfiles.getLang();
                                if (SLang != null) {
                                    if (SLang != null && SLang.contains("-")) {
                                        SLang = SLang.replaceAll("-", "_");

                                    }
                                } else {
                                    String SLang1 = fileProfiles.getLanguage();
                                    SLang = SLang1.replaceAll("-", "_");

                                }

                                if (SLang == null) {
                                    String errorMsg = "Language is null Or lang attribute in tuv tag should be xml:lang for" + fileProfiles.getSeg();
                                    bw.write(errorMsg);
                                    bw.newLine();
                                }


                                if (SLang.equalsIgnoreCase("EN_US")) {


                                    if (fileProfiles.getSeg() != null && fileProfiles.getSeg().getCompOrValue() != null && (fileProfiles.getSeg().getCompOrValue().size()) > 0) {

                                    	
                                        Sterm = getInnerTagsText(fileProfiles.getSeg().getCompOrValue());
                                    	
                                    }
                                    tuSourceCreationId = fileProfiles.getCreationId();
                                    tuSourceChangeDate = fileProfiles.getChangeDate();
                                    tuSourceChangeId = fileProfiles.getChangeId();
                                    tuSourceCreationDate = fileProfiles.getCreationDate();
                                    if (fileProfiles.getProp() != null && !fileProfiles.getProp().isEmpty()) {


                                        if (tmPropertiesset == null) {
                                            tmPropertiesset = new HashSet<TempTMProperties>();
                                        }


                                        Iterator<Prop> iterateTuvSource = fileProfiles.getProp().iterator();

                                        while (iterateTuvSource.hasNext()) {
                                            Prop prop = (Prop) iterateTuvSource.next();
                                            if (prop != null) {
                                                TempTMProperties tuvProperties = createTMProperty(prop, uniqueId);
                                                tuvProperties.setIsTuv("Y");
                                                tuvProperties.setIsTuvSource("Y");
                                                tmPropertiesset.add(tuvProperties);
                                            }
                                        }
                                    }
                                } else if (!SLang.equalsIgnoreCase("EN_US") && (previousLang == null || !previousLang.equalsIgnoreCase(SLang))) {

                                    if (prevLang == null || !(prevLang.equalsIgnoreCase(SLang))) {
                                        if (languageMap.get(SLang.toUpperCase()) != null) {
                                            Language language = languageMap.get(SLang.toUpperCase());

                                            targetLangId = language.getLanguageId();
                                            prevLang = SLang;
                                            prevLangId = targetLangId;
                                        }


                                    }
                                    if (prevLang != null && prevLang.equalsIgnoreCase(SLang)) {
                                        targetLangId = prevLangId;
                                    }

                                    String term = null;
                                    if (fileProfiles.getSeg() != null && fileProfiles.getSeg().getCompOrValue() != null && (fileProfiles.getSeg().getCompOrValue().size()) > 0) {

                                    	logger.info("source term data: "+fileProfiles.getSeg().getCompOrValue());
                                        term = getInnerTagsText(fileProfiles.getSeg().getCompOrValue());
                                        logger.info("final term data: "+term);
                                    }
                                    if (targetLangId == null) {
                                        rejectedCount++;
                                        errorDisp = "Language doesn't exist for Language Code: '" + SLang + "' for Source Term " + term;
                                        rejectedTermList.add(errorDisp);
                                    }
                                    if (engLangId != null) {
                                        tmInformation.setSourceLang(engLangId);
                                    }
                                    if (term != null) {
                                        tmInformation.setTarget(term.trim());
                                    }
                                    tmInformation.setTargetLang(targetLangId);
                                    tmInformation.setFileId(uuId);
                                    if (fileProfiles.getCreationId() != null) {
                                        tuTargetCreationId = fileProfiles.getCreationId();
                                        if (tuTargetCreationId != null) {
                                            tmInformation.setTuTargetCreationId(tuTargetCreationId);
                                        }
                                    }
                                    if (fileProfiles.getChangeDate() != null) {
                                        tuTargetCreationDate = fileProfiles.getChangeDate();
                                        if (tuTargetCreationDate != null) {
                                            tmInformation.setTuTargetChangeDate(tuTargetCreationDate);
                                        }
                                    }
                                    if (fileProfiles.getChangeId() != null) {
                                        tuTargetChangeId = fileProfiles.getChangeId();
                                        if (tuTargetChangeId != null) {
                                            tmInformation.setTuTargetChangeId(tuTargetChangeId);
                                        }
                                    }
                                    if (fileProfiles.getCreationDate() != null) {
                                        tuTargetCreationDate = fileProfiles.getCreationDate();
                                        if (tuTargetCreationDate != null) {
                                            tmInformation.setTuTargetCreationDate(tuTargetCreationDate);
                                        }
                                    }

                                    if (tmPropertiesset == null) {
                                        tmPropertiesset = new HashSet<TempTMProperties>();
                                    }


                                    if (fileProfiles.getProp() != null && !fileProfiles.getProp().isEmpty()) {
                                        Iterator<Prop> iterateTuvTarget = fileProfiles.getProp().iterator();

                                        while (iterateTuvTarget.hasNext()) {
                                            Prop prop = (Prop) iterateTuvTarget.next();
                                            if (prop != null) {
                                                TempTMProperties tuvProperties = createTMProperty(prop, uniqueId);
                                                tuvProperties.setIsTuv("Y");
                                                tuvProperties.setIsTuvTarget("Y");
                                                tmPropertiesset.add(tuvProperties);
                                            }
                                        }
                                    }


                                }
                                if (tuId != null) {

                                    tmInformation.setTuId(tuId);
                                }
                                if (tuCreationId != null) {
                                    tmInformation.setTuCreationDate(tuCreationId);
                                }
                                if (tuCreationDate != null) {
                                    tmInformation.setTuCreationDate(tuCreationDate);
                                }
                                if (tuChangeId != null) {
                                    tmInformation.setTuChangeId(tuChangeId);
                                }
                                if (tuChangeDate != null) {
                                    tmInformation.setTuChangeDate(tuChangeDate);
                                }
                                if (Sterm != null && Sterm.trim().length() > 1) {
                                    tmInformation.setSource(Sterm.trim());
                                }
                                if (tuSourceCreationId != null) {
                                    tmInformation.setTuSourceCreationId(tuSourceCreationId);
                                }
                                if (tuSourceChangeDate != null) {
                                    tmInformation.setTuSourceChangeDate(tuSourceChangeDate);
                                }
                                if (tuSourceChangeId != null) {
                                    tmInformation.setTuSourceChangeId(tuSourceChangeId);
                                }
                                if (tuSourceCreationDate != null) {
                                    tmInformation.setTuSourceCreationDate(tuSourceCreationDate);
                                }

                                tmInformation.setContentTypeId(contentTypeId);

                                tmInformation.setCompanyId(companyId);

                                tmInformation.setDomainId(domainId);
                                tmInformation.setProductGroupId(productId);
                                tmInformation.setCreateDate(new Date());
                                tmInformation.setPropRef(uniqueId);
                                tmInformation.setIsActive("Y");
                                if (tmPropertiesset != null) {
                                    Set<TempTMProperties> propertiesset = new HashSet<TempTMProperties>();
                                    for (TempTMProperties tmProp : tmPropertiesset) {
                                        TempTMProperties tempProp = new TempTMProperties();
                                        tempProp.setDescription(tmProp.getDescription());
                                        tempProp.setType(tmProp.getType());
                                        tempProp.setIsTuv(tmProp.getIsTuv());
                                        tempProp.setIsTuvSource(tmProp.getIsTuvSource());
                                        tempProp.setIsTuvTarget(tmProp.getIsTuvTarget());
                                        tempProp.setIsTu(tmProp.getIsTu());
                                        tempProp.setPropRef(tmProp.getPropRef());
                                        tempProp.setCreateDate(tmProp.getCreateDate());
                                        propertiesset.add(tempProp);
                                    }

                                    tuvtmPropertiesset.addAll(propertiesset);

                                }
                                tmInformation.setTmProperties(tuvtmPropertiesset);

                                tmprofileList.add(tmInformation);
                                if (previousLang == null && !SLang.equalsIgnoreCase("EN_US")) {
                                    previousLang = SLang;
                                }
                            }

                            if (tmprofileList != null && tmprofileList.size() > 0) {
                                for (TempTmProfileBo tmProfile : tmprofileList) {
                                    if (tmProfile != null && tmProfile.getTargetLang() != null) {
                                        Integer tmProfileId = null;
                                        boolean sourceExits = false;
                                        if (tmProfile.getSource() != null
                                                && tmProfile.getTargetLang() != null) {
                                            Set<TempTMProperties> temptmPropertiesset = new HashSet<TempTMProperties>();

                                            for (TempTMProperties tmPropSet : tmProfile.getTmProperties()) {
                                                tmPropSet.setTmProfileId(tmProfile);
                                                temptmPropertiesset.add(tmPropSet);
                                            }
                                            tmProfile.setTmProperties(temptmPropertiesset);
                                            tmProfileInsertedList.add(tmProfile);
                                        }

                                    }
                                }


                            }
                            //logger.info("Current Record>>"+counter);
                            //logger.info("Total Records>>"+totalRecords);

                            tuvtmPropertiesset.clear();
                            tuvtmPropertiesset = null;
                        }

                        try {
                            insertTMProfile(tmProfileInsertedList, session, importDAOImpl);
                            Integer currentInserted = 0;
                            if (tmProfileInsertedList != null) {
                                currentInserted = tmProfileInsertedList.size();
                            }
                            upLoadStatistics.setTotalcountForBatch(currentInserted);
                            tmPropertiesInsertedList.clear();
                            tmProfileInsertedList.clear();
                            logger.info("Calling.....Procedure >>>>" + counter + "/" + totalRecords + " fileId : " + fileIdStr + " uuid : " + uuId);
                            procedureStatus = callProcedure(session, uuId, fileIdStr, upLoadStatistics);
                            logger.info("Import Completed  " + counter + "/" + totalRecords + " records");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            session.close();
                            String successMsg = "Few records are skipped due to invalid data. " + e.getCause();
                            bw.write(successMsg);
                            session = null;
                            bw.newLine();
                        }

                    }
                    logger.info("total reocords" + totalRecords);
                    Transaction tx = null;
                    if (session == null)
                        session = importDAOImpl.getSession();

                    tx = session.beginTransaction();
                    fileData.setProcessedPercentage(100);
                    if (procedureStatus) {
                        fileData.setFileStatus("Import Completed");
                    } else {
                        fileData.setFileStatus("Import failed");
                    }

                    fileData.setEndTime(new Date());
                    importDAOImpl.updateFileUploadStatus(fileData, session);
                    tx.commit();
                    insertedCount = upLoadStatistics.getInsertCount();
                    updateCount = upLoadStatistics.getUpdateCount();
                    skippedCount = upLoadStatistics.getSkippedCount();
                    if (insertedCount > totalRecords) {
                        totalRecords = insertedCount;
                    }
                    if (updateCount > totalRecords) {
                        totalRecords = updateCount;
                    }
                    if (rejectedCount > totalRecords) {
                        totalRecords = rejectedCount;
                    }
                    if (insertedCount > 0) {
                        String successMsg = "Inserted  " + insertedCount + " records out of  " + totalRecords;
                        bw.write(successMsg);

                        bw.newLine();
                    }
                    if (updateCount > 0) {
                        String successMsg = "Updated  " + updateCount + " records out of  " + totalRecords;
                        bw.write(successMsg);

                        bw.newLine();

                    }
                    if (rejectedCount > 0) {
                        String rejectedMsg = "Rejected   " + rejectedCount + " records out of  " + totalRecords;
                        bw.write(rejectedMsg);
                        bw.newLine();
                        for (int i = 0; i < rejectedTermList.size(); i++) {
                            bw.write(rejectedTermList.get(i));
                            bw.newLine();
                        }
                    }
                    if (skippedCount > 0) {
                        String successMsg = "Skipped  " + skippedCount + " records out of  " + totalRecords;
                        bw.write(successMsg);

                        bw.newLine();

                    }

                }


                return null;
            }catch(SolrServerException e){
           		logger.info("solr server SolrServerException: "+e);
           	 
             Transaction tx = null;
             tx = session.beginTransaction();
             fileData.setProcessedPercentage(100);
           	 fileData.setFileStatus("Import failed");
             fileData.setEndTime(new Date());
              
             importDAOImpl.updateFileUploadStatus(fileData, session);
             tx.commit();
          
             logger.error("Solr problem");
             String errorMsg = "Solr problem";
             bw.write(errorMsg);
             
             
           	} catch (UnmarshalException e) {
                Transaction tx = null;
                tx = session.beginTransaction();
                fileData.setProcessedPercentage(100);
                fileData.setFileStatus("Import failed");
                fileData.setEndTime(new Date());

                importDAOImpl.updateFileUploadStatus(fileData, session);
                tx.commit();
                logger.info("Linked Exception" + e.getLinkedException().getMessage());
                logger.info("Error message: Error in jaxb" + e.getMessage());
                String errorMsg = e.getLinkedException().getMessage();
                if (e.getLinkedException().getMessage() != null) {
                    if (errorMsg.contains("Content is not allowed in prolog")) {
                        bw.write("Invalid TMX File");
                    } else {
                        bw.write(errorMsg);
                    }
                } else {
                    bw.write("Invalid TMX File");
                }
            }
            catch (XMLStreamException e) {
                Transaction tx = null;
                tx = session.beginTransaction();
                fileData.setProcessedPercentage(100);
                fileData.setFileStatus("Import failed");
                fileData.setEndTime(new Date());
                logger.info("Error message: error message in XML Stream" + e.getMessage());
                importDAOImpl.updateFileUploadStatus(fileData, session);
                tx.commit();
                if (e.getMessage() != null)
                    bw.write("Invalid TMX File");
            }

            catch (Exception e) {
                Transaction tx = null;
                tx = session.beginTransaction();
                fileData.setProcessedPercentage(100);
                fileData.setFileStatus("Import failed");
                fileData.setEndTime(new Date());
                importDAOImpl.updateFileUploadStatus(fileData, session);
                tx.commit();
                logger.info(e.getMessage());
                logger.info("Error message: Error" + e.getMessage());
                String msg = e.getMessage();
                e.printStackTrace();
                logger.error("Error in ImportBL");
                logger.error(e, e);
                String errorMsg = "Invalid TMX File";
                bw.write(msg);
            }
            catch (Throwable e) {
                Transaction tx = null;
                tx = session.beginTransaction();
                fileData.setProcessedPercentage(100);
                fileData.setFileStatus("Import failed");
                fileData.setEndTime(new Date());
                importDAOImpl.updateFileUploadStatus(fileData, session);
                tx.commit();
                logger.info("Error message: Throwable" + e.getMessage());
                logger.info(e.getMessage());
                String msg = e.getMessage();
                e.printStackTrace();
                logger.error("Error in ImportBL");
                logger.error(e, e);
                String errorMsg = "Invalid TMX File";
                bw.write(msg);
            }
            finally {
                if (session != null)
                    session.close();
                if (bw != null) {
                    bw.close();
                    bw = null;

                }

                if (encodingType != null) {
                    encodingType.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }

                Util.deleteFile(new File(fileUrl));


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

        }
        return null;
    }


    private static List<TempTmProfileBo> removeDuplicates(List<TempTmProfileBo> tmProfileList) {
        List<TempTmProfileBo> nonDuplicateList = new ArrayList<TempTmProfileBo>();
        for (TempTmProfileBo tmProfileMatch : tmProfileList) {
            Boolean execExistsFlag = Boolean.FALSE;

            if (tmProfileMatch != null && nonDuplicateList.size() == 0) {
                nonDuplicateList.add(tmProfileMatch);
                continue;
            }

            for (TempTmProfileBo innerObj : nonDuplicateList) {
                if ((innerObj.getSource().equalsIgnoreCase(tmProfileMatch.getSource()))
                        && (innerObj.getTargetLang().intValue() ==
                        tmProfileMatch.getTargetLang().intValue())) {
                    execExistsFlag = Boolean.TRUE;
                    break;
                }
            }
            if (!execExistsFlag) {
                nonDuplicateList.add(tmProfileMatch);
            }
        }
        return nonDuplicateList;
    }

    private static List<Integer> removeDuplicatesValues(List<Integer> integerList) {
        List<Integer> nonDuplicateList = new ArrayList<Integer>();
        for (Integer match : integerList) {
            Boolean execExistsFlag = Boolean.FALSE;

            if (match != null && nonDuplicateList.size() == 0) {
                nonDuplicateList.add(match);
                continue;
            }

            for (Integer innerObj : integerList) {
                if ((innerObj.intValue()) == (match.intValue())) {
                    execExistsFlag = Boolean.TRUE;
                    break;
                }
            }
            if (!execExistsFlag) {
                nonDuplicateList.add(match);
            }
        }
        return nonDuplicateList;
    }


    public static <C extends Object> List<List<C>> createBatches(
            Collection<C> collection, int batchSize) {
        if (batchSize < 1)
            throw new IllegalArgumentException("Batch size cannot be zero or less");

        List<C> objects = new ArrayList<C>(collection);
        int noOfBatches = (int) Math.ceil((double) objects.size() / batchSize);
        List<List<C>> batches = new ArrayList<List<C>>(noOfBatches);
        int index = 0;
        for (int i = 0; i < noOfBatches; i++) {
            List<C> list = new ArrayList<C>(batchSize);
            for (int j = 0; j < batchSize && index < objects.size(); j++, index++) {
                list.add(objects.get(index));
            }
            batches.add(list);
        }
        return batches;
    }


    /**
     * @param prop
     * @param uniqueId
     * @return
     */

    public static TempTMProperties createTMProperty(Prop prop, String uniqueId) {
        if (prop == null) {
            return null;
        }
        TempTMProperties tuvProperty = new TempTMProperties();

        tuvProperty.setDescription(prop.getProp());
        tuvProperty.setType(prop.getPropType());
        tuvProperty.setPropRef(uniqueId);
        tuvProperty.setCreateDate(new Date());

        return tuvProperty;
    }


    /**
     * @param tmProfileInsertedList
     * @param session
     */
    public static void insertTMProfile(List<TempTmProfileBo> tmProfileInsertedList, Session session, ImportDAOImpl importDAOImpl) throws Exception {
        if (tmProfileInsertedList == null || tmProfileInsertedList.isEmpty()) {
            return;
        }

        List<TempTmProfileBo> tmProfileNonDuplicateList = removeDuplicates(tmProfileInsertedList);

        Iterator<TempTmProfileBo> tmProfile = tmProfileNonDuplicateList.iterator();
        Transaction tx = null;
        tx = session.beginTransaction();

        while (tmProfile.hasNext()) {
            TempTmProfileBo tmProfileObj = tmProfile.next();
            importDAOImpl.saveTmProfileInfo(tmProfileObj, session);

            session.flush();
            session.clear();

        }
        tx.commit();

        tmProfileNonDuplicateList.clear();
        tmProfileNonDuplicateList = null;
    }


    /**
     * @param tmPropertiesDeleteList
     * @param session
     */
    public static void deleteTMProperties(List<Integer> tmPropertiesDeleteList, Session session, ImportDAOImpl importDAOImpl) {
        if (tmPropertiesDeleteList == null || tmPropertiesDeleteList.isEmpty()) {
            return;
        }
        List<Integer> tmPropNonDuplicateDeleteList = removeDuplicatesValues(tmPropertiesDeleteList);

        Transaction tx = null;
        tx = session.beginTransaction();

        importDAOImpl.deleteTmProperties(tmPropNonDuplicateDeleteList, session);
        session.flush();
        session.clear();
        tx.commit();

        tmPropNonDuplicateDeleteList.clear();
        tmPropNonDuplicateDeleteList = null;
    }

    /**
     * @param languageList
     * @return
     */
    public static Map<String, Language> getLanguageMap(List<Language> languageList) {
        Map<String, Language> languageMap = new HashMap<String, Language>();
        Iterator<Language> iterator = languageList.iterator();
        while (iterator.hasNext()) {
            Language language = (Language) iterator.next();

            languageMap.put(language.getLanguageCode().toUpperCase(), language);
        }
        return languageMap;
    }

    /**
     * @param o
     * @return
     */
    public static String getInnerTagsText(List<Object> objList) {
        StringBuffer term = new StringBuffer();
        
        Iterator<Object> iterate = objList.iterator();
        while (iterate.hasNext()) {
            Object o = (Object) iterate.next();

            if (o instanceof Ut) {
                Ut u = (Ut) o;
                if (u != null) {
                    term.append("<ut");
                    if (u.getX() != null) {
                        term.append(" x = ").append(u.getX());
                    }
                    term.append(">");
                    if (u.getValue() != null) {
                        term.append(u.getValue());
                    }
                    term.append("</ut>");
                }

            } else if (o instanceof Bpt) {
                Bpt u = (Bpt) o;
                if (u != null) {
                    term.append("<bpt");
                    if (u.getX() != null) {
                        term.append(" x = ").append(u.getX());
                    }
                    if (u.getType() != null) {
                        term.append(" type = ").append(u.getType());
                    }
                    if (u.getI() != null) {
                        term.append(" i = ").append(u.getI());
                    }
                    term.append(">");
                    if (u.getValue() != null) {
                    	
                        term.append(u.getValue());
                    }
                    term.append("</bpt>");
                }

            } else if (o instanceof It) {

                It u = (It) o;
                if (u != null) {
                    term.append("<it");
                    if (u.getX() != null) {
                        term.append(" x = ").append(u.getX());
                    }
                    if (u.getType() != null) {
                        term.append(" type = ").append(u.getType());
                    }
                    if (u.getPos() != null) {
                        term.append(" pos = ").append(u.getPos());
                    }
                    term.append(">");
                    if (u.getValue() != null) {
                        term.append(u.getValue());
                    }
                    term.append("</it>");
                }


            } else if (o instanceof Ept) {

                Ept u = (Ept) o;
                if (u != null) {
                    term.append("<ept");
                    if (u.getI() != null) {
                        term.append(" i = ").append(u.getI());
                    }

                    term.append(">");
                    if (u.getValue() != null) {
                        term.append(u.getValue());
                    }
                    term.append("</ept>");
                }


            } else if (o instanceof Hi) {

                Hi u = (Hi) o;
                if (u != null) {
                    term.append("<hi");
                    if (u.getX() != null) {
                        term.append(" x = ").append(u.getX());
                    }
                    if (u.getType() != null) {
                        term.append(" type = ").append(u.getType());
                    }

                    term.append(">");
                    if (u.getValue() != null) {
                        term.append(u.getValue());
                    }
                    term.append("</hi>");
                }


            } else if (o instanceof Ph) {

                Ph u = (Ph) o;
                if (u != null) {
                    term.append("<ph");
                    if (u.getX() != null) {
                        term.append(" x = ").append(u.getX());
                    }
                    if (u.getType() != null) {
                        term.append(" type = ").append(u.getType());
                    }
                    if (u.getAssoc() != null) {
                        term.append(" type = ").append(u.getAssoc());
                    }

                    term.append(">");
                    if (u.getValue() != null) {
                        term.append(u.getValue());
                    }
                    term.append("</ph>");
                }


            } else if (o instanceof Sub) {
                Sub u = (Sub) o;
                if (u != null) {
                    term.append("<sub");

                    term.append(">");
                    if (u.getValue() != null) {
                        term.append(u.getValue());
                    }
                    term.append("</sub>");
                }

            } else {
                if (o != null)
                    term.append(o);
            }

           
        }
        logger.info("final term data: "+term);
        return term.toString();
    }

    public static boolean callProcedure(Session session, String uuId, String fileIdStr, UploadStatistics upLoadStatistics) {
        boolean status = true;
        Integer insertCount = upLoadStatistics.getInsertCount();
        Integer updatedCount = upLoadStatistics.getUpdateCount();
        Integer skippedcount = upLoadStatistics.getSkippedCount();
        Integer currTotal = upLoadStatistics.getTotalcountForBatch();

        java.sql.Connection con = session.connection();
        Integer insert = 0;
        Integer update = 0;
        Integer skipped = 0;
        try {
            CallableStatement cstmt = con.prepareCall("{ call  InsertTmProfile (?,?,?)  }");
            cstmt.setString(1, uuId);
            cstmt.registerOutParameter(2, Types.CLOB);
            cstmt.registerOutParameter(3, Types.CLOB);
            //exists=call.execute();
            cstmt.execute();
            StringBuffer insertedData = new StringBuffer();
            StringBuffer updatedData = new StringBuffer();
            String insertedList = null;
            String updatedLsit = null;
            Clob insertedClobdata = (Clob) cstmt.getClob(2);
            if (insertedClobdata != null && insertedClobdata.length() > 0) {
                if (insertedClobdata.getCharacterStream() != null) {
                    Reader reader1 = insertedClobdata.getCharacterStream();
                    BufferedReader br = new BufferedReader(reader1);

                    String line;
                    while (null != (line = br.readLine())) {
                        insertedData.append(line);

                    }
                    br.close();
                    logger.info("Inserted Data");
                }
            }
            Clob updatedClobData = (Clob) cstmt.getClob(3);
            if (updatedClobData != null && updatedClobData.length() > 0) {
                if (updatedClobData.getCharacterStream() != null) {
                    Reader reader1 = updatedClobData.getCharacterStream();
                    BufferedReader br = new BufferedReader(reader1);

                    String line;
                    while (null != (line = br.readLine())) {
                        updatedData.append(line);

                    }
                    br.close();
                    logger.info("updated Data");
                }
            }
            if (insertedData != null && insertedData.length() > 0) {
                insertedList = insertedData.toString();
                if (insertedList.endsWith(",")) {
                    insertedList = insertedList.substring(0, insertedList.lastIndexOf(","));
                    String insertedRecords[] = null;
                    if (insertedList.contains(",")) {
                        insertedRecords = insertedList.split(",");
                    } else {
                        insertedRecords = new String[1];
                        insertedRecords[0] = insertedList;
                    }
                    insert = insertedRecords.length;

                }
            }
            if (updatedData != null && updatedData.length() > 0) {
                updatedLsit = updatedData.toString();
                if (updatedLsit.endsWith(",")) {
                    updatedLsit = updatedLsit.substring(0, updatedLsit.lastIndexOf(","));
                    String updatedRecords[] = null;
                    if (updatedLsit.contains(",")) {
                        updatedRecords = updatedLsit.split(",");
                    } else {
                        updatedRecords = new String[1];
                        updatedRecords[0] = updatedLsit;
                    }
                    update = updatedRecords.length;
                }
            }
            insertCount = insertCount + insert;
            updatedCount = updatedCount + update;
            skippedcount = skippedcount + (currTotal - (insert + update));
            upLoadStatistics.setInsertCount(insertCount);
            upLoadStatistics.setUpdateCount(updatedCount);
            upLoadStatistics.setSkippedCount(skippedcount);
            UpadateSolrThread(insertedList, updatedLsit, fileIdStr);
            logger.info("Updated solr database successfully");

        }

        catch (Exception e) {
            logger.info("Error message: Error in Procedure" + e.getMessage());
            e.printStackTrace();
            return false;

        }
        finally {
            try {
                if (con != null)
                    con.close();
            }
            catch (Exception e) {
                logger.error(e.getMessage());
            }

        }
        Runnable thread = new ImportDeleteTempTMXThread(uuId, session);
        thread.run();
        return status;
    }

    public static void UpadateSolrThread(String insetedTmIds, String updateTmIds, String fileIdStr) {
        String insertedRecords[] = null;
        if (insetedTmIds != null && insetedTmIds.contains(",")) {
            insertedRecords = insetedTmIds.split(",");
        } else {
            insertedRecords = new String[1];
            insertedRecords[0] = insetedTmIds;
        }
        String updatedRecords[] = null;
        if (updateTmIds != null && updateTmIds.contains(",")) {
            updatedRecords = updateTmIds.split(",");
        } else {
            updatedRecords = new String[1];
            updatedRecords[0] = updateTmIds;
        }
        List<String> insertedCountList = new ArrayList<String>();
        List<String> updatedCountList = new ArrayList<String>();
        if (insertedRecords != null) {
            for (int i = 0; i < insertedRecords.length; i++) {
                insertedCountList.add(insertedRecords[i]);
            }

        }
        if (updatedRecords != null) {
            for (int i = 0; i < updatedRecords.length; i++) {
                updatedCountList.add(updatedRecords[i]);
            }

        }
        List<List<String>> insertedBatchList = null;
        if (insertedCountList != null && insertedCountList.size() > 0)
            insertedBatchList = createBatches(insertedCountList, 100);
        List<List<String>> updatedBatchList = null;
        if (updatedCountList != null && updatedCountList.size() > 0)
            updatedBatchList = createBatches(updatedCountList, 100);

        updateTmIds = "";
        String ids = "";
        insetedTmIds = "";
        if (updatedBatchList != null && updatedBatchList.size() > 0) {
            for (List<String> updatedTMIdList : updatedBatchList) {
                if (updatedTMIdList != null && updatedTMIdList.size() > 0) {
                	 ids = "";
                    for (int i = 0; i < updatedTMIdList.size(); i++) {
                        if (updatedTMIdList.get(i) != null) {
                            if (i == 0) {
                                ids = ids + updatedTMIdList.get(i);
                            } else {
                                ids = ids + "," + updatedTMIdList.get(i);
                            }
                        }


                    }
                    updateTmIds = ids;
                }
                if (updateTmIds != null && !updateTmIds.isEmpty()) {
                    try {
                    	logger.info("update TM in solr");
                    	logger.info("");
                    	Set<Integer> tmIds = convertCommaSeparatedStringtoArray(updateTmIds);
                    	
                    	HttpSolrServer server = HttpSolrServerInstance.INSTANCE.getServer();
                    	server.deleteById(updatedTMIdList);
                    	server.commit();
                    	indexDataSolr.updateOrIndexTms(tmIds, fileIdStr);
                    	tmIds.clear();
                    	tmIds = null;
                    }
                    catch (Exception e) {
                        logger.info("Error message: Error in Updating Solr" + e.getMessage());
                        logger.error(e, e);
                    }
                }
            }
        }
        if (insertedBatchList != null && insertedBatchList.size() > 0) {
            for (List<String> insertedTMIdList : insertedBatchList) {
            	String insertedTmidsObj = "";
                if (insertedTMIdList != null && insertedTMIdList.size() > 0) {
                    ids = "";
                    for (int i = 0; i < insertedTMIdList.size(); i++) {
                        if (insertedTMIdList.get(i) != null) {
                            if (i == 0) {
                                ids = ids + insertedTMIdList.get(i);
                            } else {
                                ids = ids + "," + insertedTMIdList.get(i);
                            }
                        }


                    }
                    insertedTmidsObj = ids;
                }

                if (insertedTmidsObj != null && !insertedTmidsObj.isEmpty()) {
                    try {
                    	
                    	logger.info("insert TM in solr");
                    	Set<Integer> tmIds = convertCommaSeparatedStringtoArray(insertedTmidsObj);
                    	indexDataSolr.updateOrIndexTms(tmIds,fileIdStr);
                    	tmIds.clear();
                    	tmIds = null;
                    	
                    }
                    catch (Exception e) {
                    	
                        logger.info("Error message: Error in Updating Lucene" + e.getMessage());
                        logger.error("+++++++Error in connecting jetty server++++++ ");
                        logger.error(e, e);
                        
                    }
                }
            }
        }


    }

    public static void loadHibernate() {
        try {
            ImportDAOImpl importDAOImpl = ImportDAOImpl.getInstance();
            Session session = importDAOImpl.getSession();
            Integer companyId = importDAOImpl.getCompanyId(ImportExportProperty.ADMIN_COMPANY.getValue(), session);
        }
        catch (Exception e) {
            logger.info("Exception while loading Hibernate");
        }
    }
    
    public  static  Set<Integer> convertCommaSeparatedStringtoArray(String ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		Set<Integer> idsList = new HashSet<Integer>();
		String idsArray[] = ids.split(",");

		if (idsArray != null && idsArray.length > 0) {
			for (int i = 0; i < idsArray.length; i++) {
				if (idsArray[i] != null && !idsArray[i].trim().isEmpty()) {
					try {
						idsList.add(new Integer(idsArray[i].trim()));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
		return idsList;
	}
}

