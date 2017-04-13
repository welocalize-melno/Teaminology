package com.teaminology.hp.bl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.globalsight.www.webservices.AmbassadorProxy;
import com.teaminology.hp.Enum.GlobalsightEnum;
import com.teaminology.hp.bo.Attributes;
import com.teaminology.hp.bo.Company;
import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.GsCreditials;
import com.teaminology.hp.bo.Tag;
import com.teaminology.hp.dao.ImpExp.hibernate.ExportGSDAOImpl;
import com.teaminology.hp.dao.ImpExp.hibernate.ImportGSDAOImpl;
import com.teaminology.hp.util.ImportExportProperty;
import com.teaminology.hp.util.Util;


public class ExportGSBL
{

    private static Logger logger = Logger.getLogger(ExportGSBL.class);


    private static final String UPLOAD_FILES_PATH = ImportExportProperty.UPLOAD_PATH.getValue();
    private static final String LOG_URL_DIR_PATH = ImportExportProperty.LOGS_PATH.getValue();

    public static void startExport(String fileId, Integer companyId) throws Exception {

        if (fileId == null)
            return;
        ExportGSDAOImpl exportDAOImpl = ExportGSDAOImpl.getInstance();
        ImportGSDAOImpl importDAOImpl = ImportGSDAOImpl.getInstance();
        FileInfo fileInfo = exportDAOImpl.getFileInfo(new Integer(fileId));
        String logfileName = "teaminologyexport" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + ".log";
        BufferedWriter bw = null;
        String logFile = LOG_URL_DIR_PATH + "/" + logfileName;
        try {
            logger.info("ExportBL for fileId" + fileId);

            bw = new BufferedWriter(new FileWriter(logFile));
            List<GlobalsightTermInfo> globalSightTermInfoList = null;
            File xlfFile = null;
            if (fileInfo != null) {
                fileInfo.setStatus(StatusEnum.IN_PROGRESS.getValue());
                exportDAOImpl.updateFileStatus(fileInfo);
            }

            globalSightTermInfoList = exportDAOImpl.gettermsUsingPageId(Integer.parseInt(fileId));
            if (globalSightTermInfoList != null && globalSightTermInfoList.size() > 0) {
                String file = globalSightTermInfoList.get(0).getFileInfo().getFileName();
                if (globalSightTermInfoList.get(0).getFileInfo().getFileName() != null) {
                    file = file.replaceAll(" ", "_");
                }

                String fileName = file + ".xlf";
                String filePath = UPLOAD_FILES_PATH + "/" + fileName;
                File xmlFile = exportXliff(globalSightTermInfoList, filePath);
                Company company = importDAOImpl.getCompanyById(companyId);
                if (company == null) {
                    company = importDAOImpl.getCompanyByLable(ImportExportProperty.ADMIN_COMPANY.getValue());

                }
                GsCreditials gscredentials = new GsCreditials();

                gscredentials.setUrl(company.getUrl());
                gscredentials.setUserName(company.getUserName());
                gscredentials.setPassword(company.getPassword());


                AmbassadorProxy proxy = Util.getProxyObject(gscredentials);
                String accesstoken = Util.getAccessToke(proxy, gscredentials);
                xlfFile = new File(filePath);
                byte[] bytes = null;
                ByteArrayOutputStream ous = new ByteArrayOutputStream();
                InputStream ios = new FileInputStream(xlfFile);
                Integer length = Integer.parseInt(String.valueOf(xmlFile.length()));
                try {
                    byte[] buffer = new byte[length];

                    int read = 0;
                    while ((read = ios.read(buffer)) != -1) {
                        ous.write(buffer, 0, read);
                    }
                    bytes = ous.toByteArray();
                }
                finally {
                    try {
                        if (ous != null)
                            ous.close();
                        if (ios != null)
                            ios.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                logger.info("Exported file path" + xlfFile);

                proxy.uploadEditionFileBack(accesstoken, fileInfo.getTaskId(), fileInfo.getFileName(), bytes);
                proxy.importOfflineTargetFiles(accesstoken, fileInfo.getTaskId());
                bw.write("Exported successfully");
                if (fileInfo != null) {
                    fileInfo.setExportLog(logFile);
                    fileInfo.setStatus(StatusEnum.EXPORTED.getValue());
                    fileInfo.setUpdateDate(new Date());
                    exportDAOImpl.updateFileStatus(fileInfo);
                }


            }
        }
        catch (Exception e) {
            bw.write(e.getMessage());
            if (fileInfo != null) {
                fileInfo.setExportLog(logFile);
                fileInfo.setStatus(StatusEnum.EXPORT_FAILED.getValue());
                exportDAOImpl.updateFileStatus(fileInfo);
            }
            e.printStackTrace();

            logger.warn("Failed to upload xlf file");
        }
        finally {
            bw.close();
        }
    }

    private static File exportXliff(
            List<GlobalsightTermInfo> globalSightTermInfoList, String absolutePath) {
        ExportGSDAOImpl exportDAOImpl = ExportGSDAOImpl.getInstance();
        Map<Integer, List<GlobalsightTermInfo>> globalSightMap = new HashMap<Integer, List<GlobalsightTermInfo>>();
        File newFile = null;
        for (GlobalsightTermInfo globalsightTermInfo : globalSightTermInfoList) {
            Integer termId = globalsightTermInfo.getTermInformationId().getTermId();
            List<GlobalsightTermInfo> newGlobalsightTermInfoList = new ArrayList<GlobalsightTermInfo>();
            if (!globalSightMap.containsKey(termId)) {
                newGlobalsightTermInfoList.add(globalsightTermInfo);
                globalSightMap.put(termId, newGlobalsightTermInfoList);
            } else {
                List<GlobalsightTermInfo> tempList = globalSightMap.get(termId);
                tempList.add(globalsightTermInfo);
                globalSightMap.put(termId, tempList);
            }
        }
        File x = null;
        String encodingType = "UTF-8";
        try {
            GlobalsightTermInfo gsInfo = globalSightTermInfoList.get(0);
            String filePath = absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1);
            filePath = filePath + "globalsight" + new Date().getTime() + ".xlf";
            String sourceLangLabel = null;
            String targetLangLabel = null;
            if (gsInfo != null) {
                x = new File(filePath);
                DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
                DocumentBuilder b = f.newDocumentBuilder();
                Document d = b.newDocument();
                encodingType = gsInfo.getFileInfo().getEncodingType();
                Element xliff = d.createElement(GlobalsightEnum.XLIFF.getValue());
                xliff.setAttribute(GlobalsightEnum.VERSION.getValue(), gsInfo.getFileInfo().getXliffVersion());
                d.appendChild(xliff);
                Element fileElement = d.createElement(GlobalsightEnum.FILE.getValue());
                if (gsInfo.getFileInfo().getFileName() != null) {
                    fileElement.setAttribute(GlobalsightEnum.ORIGINAL.getValue(), gsInfo.getFileInfo().getFileName());
                }
                if (gsInfo.getFileInfo().getSourceLang() != null) {
                    sourceLangLabel = exportDAOImpl.getLanguageName(gsInfo.getFileInfo().getSourceLang());
                    fileElement.setAttribute(GlobalsightEnum.SOURCE_LANGUAGE.getValue(), sourceLangLabel);
                }
                if (gsInfo.getFileInfo().getTargetLang() != null) {
                    targetLangLabel = exportDAOImpl.getLanguageName(gsInfo.getFileInfo().getTargetLang());
                    fileElement.setAttribute(GlobalsightEnum.TARGET_LANGUAGE.getValue(), targetLangLabel);
                }
                if (gsInfo.getFileInfo().getDocumentForm() != null) {
                    String documentForm = gsInfo.getFileInfo().getDocumentForm().trim();
                    fileElement.setAttribute(GlobalsightEnum.DATATYPE.getValue(), documentForm);
                }
                xliff.appendChild(fileElement);
                Element header = d.createElement(GlobalsightEnum.HEADER.getValue());
                Element phaseGroup = d.createElement(GlobalsightEnum.PHASE_GROUP.getValue());
                Element phase = d.createElement(GlobalsightEnum.PHASE.getValue());
                if (gsInfo.getFileInfo().getPhanesName() != null) {
                    phase.setAttribute(GlobalsightEnum.PHASE_NAME.getValue(), gsInfo.getFileInfo().getPhanesName());
                }
                if (gsInfo.getFileInfo().getProcessName() != null) {
                    phase.setAttribute(GlobalsightEnum.PROCESS_NAME.getValue(), gsInfo.getFileInfo().getProcessName());
                }
                phaseGroup.appendChild(phase);
                header.appendChild(phaseGroup);
                Element note = d.createElement(GlobalsightEnum.NOTE.getValue());
                String activityType = (gsInfo.getFileInfo().getActivityType() != null) ? "# " + GlobalsightEnum.ACTIVITY_TYPE.getValue() + gsInfo.getFileInfo().getActivityType() : null;
                String userName = (gsInfo.getFileInfo().getUserName() != null) ? "# " + GlobalsightEnum.USER_NAME.getValue() + gsInfo.getFileInfo().getUserName() : null;
                String acceptTime = (gsInfo.getFileInfo().getAcceptTime() != null) ? "# " + GlobalsightEnum.ACCEPT_TIME.getValue() + gsInfo.getFileInfo().getAcceptTime() : null;
                String encoding = (gsInfo.getFileInfo().getEncodingType() != null) ? "# " + GlobalsightEnum.ENCODING.getValue() + gsInfo.getFileInfo().getEncodingType() + "\n" : null;
                String gsDocumentForm = (gsInfo.getFileInfo().getDocumentForm() != null) ? "# " + GlobalsightEnum.DOCUMENT_FORMATE.getValue() + gsInfo.getFileInfo().getDocumentForm() : null;
                String placeHolder = (gsInfo.getFileInfo().getPlaceHolder() != null) ? "# " + GlobalsightEnum.PLACE_HOLDER_FORMATE.getValue() + gsInfo.getFileInfo().getPlaceHolder() : null;
                String sourceLocale = (gsInfo.getFileInfo().getSourceLang() != null) ? "# " + GlobalsightEnum.SOURCE_LOCALE.getValue() + sourceLangLabel + "\n" : null;
                String targetLocale = (gsInfo.getFileInfo().getTargetLang() != null) ? "# " + GlobalsightEnum.TARGETLOCALE.getValue() + targetLangLabel + "\n" : null;
                String pageId = (gsInfo.getFileInfo().getFileId() != null) ? "# " + GlobalsightEnum.PAGEID.getValue() + gsInfo.getFileInfo().getFileId() + "\n" : null;
                String workFlowId = (gsInfo.getFileInfo().getWorkFlowId() != null) ? "# " + GlobalsightEnum.WORKFLOW_ID.getValue() + gsInfo.getFileInfo().getWorkFlowId() + "\n" : null;
                String taskId = (gsInfo.getFileInfo().getTaskId() != null) ? "# " + GlobalsightEnum.TASK_ID.getValue() + gsInfo.getFileInfo().getTaskId() + "\n" : null;
                String exactMatch = (gsInfo.getFileInfo().getExactMatch() != null) ? "# " + GlobalsightEnum.EXACT_MATCH.getValue() + gsInfo.getFileInfo().getExactMatch() + "\n" : null;
                String fuzzyMatch = (gsInfo.getFileInfo().getFuzzyMatch() != null) ? "# " + GlobalsightEnum.FUZZY_MATCH.getValue() + gsInfo.getFileInfo().getFuzzyMatch() + "\n" : null;
                String noMatch = (gsInfo.getFileInfo().getNoMatch() != null) ? "# " + GlobalsightEnum.NO_MATCH.getValue() + gsInfo.getFileInfo().getNoMatch() + "\n" : null;
                String contextMatch = (gsInfo.getFileInfo().getInContextMatch() != null) ? "# " + GlobalsightEnum.IN_CONTEXT_MATCH.getValue() + gsInfo.getFileInfo().getInContextMatch() + "\n" : null;
                String editAll = (gsInfo.getFileInfo().getEditAll() != null) ? "# " + GlobalsightEnum.EDIT_ALL.getValue() + gsInfo.getFileInfo().getEditAll() : null;
                String tmProfileId = (gsInfo.getFileInfo().getGsTmProfileId() != null) ? "# " + GlobalsightEnum.GS_TM_PROFILE_ID.getValue() + gsInfo.getFileInfo().getGsTmProfileId() + "\n" : null;
                String tmProfileName = (gsInfo.getFileInfo().getGsTmProfileName() != null) ? "# " + GlobalsightEnum.GS_TM_PROFILE_NAME.getValue() + gsInfo.getFileInfo().getGsTmProfileName() : null;
                String termBase = (gsInfo.getFileInfo().getGsTermbase() != null) ? "# " + GlobalsightEnum.GS_TERMBASE.getValue() + gsInfo.getFileInfo().getGsTermbase() : null;

                String noteText = "\n" + "# GlobalSight Download File" + "\n";
                if (activityType != null) {
                    noteText = noteText + activityType;
                }
                if (userName != null) {
                    noteText = noteText + userName;
                }
                if (acceptTime != null) {
                    noteText = noteText + acceptTime;
                }
                if (encoding != null) {
                    noteText = noteText + encoding;
                }
                if (gsDocumentForm != null) {
                    noteText = noteText + gsDocumentForm;
                }
                if (placeHolder != null) {
                    noteText = noteText + placeHolder;
                }
                if (sourceLocale != null) {
                    noteText = noteText + sourceLocale;
                }
                if (targetLocale != null) {
                    noteText = noteText + targetLocale;
                }
                if (pageId != null) {
                    noteText = noteText + pageId;
                }
                if (workFlowId != null) {
                    noteText = noteText + workFlowId;
                }
                if (taskId != null) {
                    noteText = noteText + taskId;
                }
                if (exactMatch != null) {
                    noteText = noteText + exactMatch;
                }
                if (fuzzyMatch != null) {
                    noteText = noteText + fuzzyMatch;
                }
                if (noMatch != null) {
                    noteText = noteText + noMatch;
                }
                if (contextMatch != null) {
                    noteText = noteText + contextMatch;
                }
                if (editAll != null) {
                    noteText = noteText + editAll;
                }
                if (tmProfileId != null) {
                    noteText = noteText + tmProfileId;
                }
                if (tmProfileName != null) {
                    noteText = noteText + tmProfileName;
                }
                if (termBase != null) {
                    noteText = noteText + termBase;
                }
                if (noteText != null) {
                    note.appendChild(d.createTextNode(noteText));
                }
                header.appendChild(note);
                fileElement.appendChild(header);
                Element body = d.createElement(GlobalsightEnum.BODY.getValue());
                fileElement.appendChild(body);
                for (Integer termId : globalSightMap.keySet()) {
                    List<GlobalsightTermInfo> newGlobalsightTermInfoList = globalSightMap.get(termId);
                    GlobalsightTermInfo globalsightTermInfoObj = newGlobalsightTermInfoList.get(0);
                    Element transUnitElement = d.createElement(GlobalsightEnum.TRANS_UNIT.getValue());
                    if (globalsightTermInfoObj.getTransUnitId() != null)
                        transUnitElement.setAttribute(GlobalsightEnum.ID.getValue(), globalsightTermInfoObj.getTransUnitId().toString());
                    if (globalsightTermInfoObj.getTranslate() != null)
                        transUnitElement.setAttribute(GlobalsightEnum.TRANSLATE.getValue(), globalsightTermInfoObj.getTranslate());
                    Element source = d.createElement(GlobalsightEnum.SOURCE.getValue());
                    Element target = d.createElement(GlobalsightEnum.TARGET.getValue());

                    if (globalsightTermInfoObj.getTargetState() != null)
                        target.setAttribute(GlobalsightEnum.STATE.getValue(), globalsightTermInfoObj.getTargetState());
                    List<Tag> tagList = exportDAOImpl.getTagsByGSId(globalsightTermInfoObj.getGlobalsightTermInfoId());
                    if (tagList != null && tagList.size() > 0) {
                        for (Tag tagObj : tagList) {
                            if (tagObj.getTagName().equalsIgnoreCase("TextTag")) {
                                if (tagObj.getIsSource().equalsIgnoreCase("Y")) {
                                    source.appendChild(d.createTextNode(globalsightTermInfoObj.getTermInformationId().getTermBeingPolled()));
                                } else {
                                    target.appendChild(d.createTextNode(globalsightTermInfoObj.getTermInformationId().getSuggestedTerm()));
                                }
                            } else {
                                Element tagElement = d.createElement(tagObj.getTagName());
                                tagElement.appendChild(d.createTextNode(tagObj.getTagValue()));
                                List<Attributes> attributesList = exportDAOImpl.getAttributesByTagId(tagObj.getTagId());
                                if (attributesList != null && attributesList.size() > 0) {
                                    for (Attributes attrObj : attributesList) {
                                        tagElement.setAttribute(attrObj.getAttributeName(), attrObj.getAttributeValue());
                                    }
                                }
                                if (tagObj.getIsSource().equalsIgnoreCase("Y")) {
                                    source.appendChild(tagElement);
                                } else {
                                    target.appendChild(tagElement);
                                }
                            }
                        }
                    }

                    transUnitElement.appendChild(source);
                    transUnitElement.appendChild(target);
                    for (int i = 0; i < newGlobalsightTermInfoList.size(); i++) {
                        GlobalsightTermInfo gsObj = newGlobalsightTermInfoList.get(i);

                        Element altTransElement = d.createElement(GlobalsightEnum.ALT_TRANS.getValue());
                        if (gsObj.getMatchQuality() != null)
                            altTransElement.setAttribute(GlobalsightEnum.MATCH_QUALITY.getValue(), gsObj.getMatchQuality());
                        if (gsObj.getOrigin() != null)
                            altTransElement.setAttribute(GlobalsightEnum.ORIGIN.getValue(), gsObj.getOrigin());
                        Element tsource = d.createElement(GlobalsightEnum.SOURCE.getValue());
                        Element ttarget = d.createElement(GlobalsightEnum.TARGET.getValue());
                        tsource.appendChild(d.createTextNode(globalsightTermInfoObj.getSourceSegment()));
                        ttarget.appendChild(d.createTextNode(globalsightTermInfoObj.getTargetSegment()));
                        altTransElement.appendChild(tsource);
                        altTransElement.appendChild(ttarget);
                        transUnitElement.appendChild(altTransElement);

                    }
                    body.appendChild(transUnitElement);

                }

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer m = tf.newTransformer();
                m.setOutputProperty(OutputKeys.INDENT, "yes");
                if (encodingType.contains("UTF-16LE")) {
                    encodingType = "UTF-16";
                    //doc = dBuilder.parse(uploadedFile);
                }
                m.setOutputProperty(OutputKeys.ENCODING, encodingType);
                m.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");

                DOMSource source = new DOMSource(d);
                StreamResult result = new StreamResult(x);
                m.transform(source, result);
                newFile = removeExtraLine(filePath, absolutePath, encodingType);
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        catch (TransformerException e) {
            e.printStackTrace();
        }

        return newFile;

    }

    private static File removeExtraLine(String filePath, String absolutePath,
                                        String encodingType) {
        File f1 = new File(filePath);
        File f2 = new File(absolutePath);

        try {
            String data = readTextFile(filePath, encodingType);

            data = cleanUp(data);
            writeTextFile(absolutePath, data, encodingType);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return f2;
    }

    private static void writeTextFile(String fileName, String s, String encodingType) {
        FileWriter output;
        try {
            output = new FileWriter(fileName);
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(fileName), encodingType));
            //BufferedWriter writer = new BufferedWriter(output,"UTF-16");
            writer.write(s);
            writer.close();
            output.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String cleanUp(String xml) {
        final StringReader reader = new StringReader(xml.trim());
        final StringWriter writer = new StringWriter();
        try {
            XmlUtil.prettyFormat(reader, writer);
            reader.close();
            return writer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return xml.trim();
    }

    private static String readTextFile(String fileName, String encodingType) {
        String returnValue = "";
        FileReader file;
        String line = "";
        String temp = "";
        try {
            file = new FileReader(fileName);
            // BufferedReader reader = new BufferedReader(new FileInputStream(fileName),"UTF-16");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName), encodingType));

            try {
                boolean flag = true;
                line = reader.readLine();
                while (flag) {
                    temp = line;
                    line = reader.readLine();
                    returnValue += temp + "\n";

                    if (line == null)
                        break;


                }
                returnValue = returnValue.substring(0, returnValue.length());
            }
            finally {
                reader.close();
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }
        catch (IOException e) {
            throw new RuntimeException("IO Error occured");
        }
        return returnValue;

    }

    private static String createXliffZipFile(List<String> fileNameList) {
        String zipFile = UPLOAD_FILES_PATH + "/reports.zip";
        String[] sourceFiles = new String[fileNameList.size()];
        int j = 0;
        for (String fileName : fileNameList) {
            fileName = UPLOAD_FILES_PATH + "/" + fileName;
            sourceFiles[j] = fileName;
            j++;
        }
        try {
            //create byte buffer
            byte[] buffer = new byte[1024];

            FileOutputStream fout = new FileOutputStream(zipFile);

            //create object of ZipOutputStream from FileOutputStream
            ZipOutputStream zout = new ZipOutputStream(fout);

            for (int i = 0; i < sourceFiles.length; i++) {

                System.out.println("Adding " + sourceFiles[i]);
                //create object of FileInputStream for source file
                FileInputStream fin = new FileInputStream(sourceFiles[i]);

                zout.putNextEntry(new ZipEntry(fileNameList.get(i)));

                /*
                 * After creating entry in the zip file, actually
                 * write the file.
                 */
                int length;

                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }

                zout.closeEntry();

                fin.close();

            }


            //close the ZipOutputStream
            zout.close();

        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(" Failed to create zip file");
        }
        return zipFile;
    }
}
