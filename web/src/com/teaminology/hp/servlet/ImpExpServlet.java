package com.teaminology.hp.servlet;


//import String;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jodd.util.StringUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.teaminology.hp.TeaminologyService;
import com.teaminology.hp.Utils;
import com.teaminology.hp.bo.CompanyTransMgmt;
import com.teaminology.hp.bo.DeprecatedTermInformation;
import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.FileUploadStatus;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermVoteMaster;
import com.teaminology.hp.bo.TmProfileInfo;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.ContentType;
import com.teaminology.hp.bo.lookup.Domain;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.bo.lookup.ProductGroup;
import com.teaminology.hp.bo.lookup.Status;
import com.teaminology.hp.data.CSVImportStatus;
import com.teaminology.hp.data.LanguagePiChartData;
import com.teaminology.hp.data.LanguageReportData;
import com.teaminology.hp.data.LanguageReportTable;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.TermInformationTo;
import com.teaminology.hp.data.TermVotingTo;
import com.teaminology.hp.data.Terms;
import com.teaminology.hp.service.enums.ExportGlobalSightExecutor;
import com.teaminology.hp.service.enums.ExportTMXExecutor;
import com.teaminology.hp.service.enums.ImportTMXExecutor;
import com.teaminology.hp.service.enums.SessionKey;
import com.teaminology.hp.service.enums.TeaminologyEnum;
import com.teaminology.hp.service.enums.TeaminologyPage;
import com.teaminology.hp.service.enums.TeaminologyProperty;
import com.teaminology.hp.service.enums.UploadStatusMessages;
import com.teaminology.hp.service.thread.ExportGlobalSightThread;
import com.teaminology.hp.service.thread.ExportTMXThread;
import com.teaminology.hp.service.thread.ImportTMXThread;
import com.teaminology.hp.web.utils.Pair;
import com.teaminology.hp.web.utils.ParamKeyEnum;

/**
 * Contains methods to import and export the files.
 *
 * @author Sarvani
 */

public class ImpExpServlet extends HttpServlet
{
    private Logger logger = Logger.getLogger(ImpExpServlet.class);

    private final String TERM_CHART_DATA_EXCEL = TeaminologyProperty.TERM_CHART_DATA_EXCEL.getValue();
    private final String TM_CHART_GLOSSARY = TeaminologyProperty.TM_CHART_GLOSSARY.getValue();
    private final String TERM_CHART_GLOSSARY = TeaminologyProperty.TERM_CHART_GLOSSARY.getValue();
    private final String TERMS_EXCEL = TeaminologyProperty.TERMS_EXCEL.getValue();
    private final String LANGUAGE_EXCEL = TeaminologyProperty.LANGUAGE_EXCEL.getValue();
    private final String LANGUAGE_DISTRIBUTION = TeaminologyProperty.LANGUAGE_DISTRIBUTION.getValue();
    private final String EXPORT_USER = TeaminologyProperty.EXPORT_USER.getValue();
    private final String LANGUAGE_REPORT_TABLE = TeaminologyProperty.LANGUAGE_REPORT_TABLE.getValue();
    private final String UPLOAD_ROOT_DIR = TeaminologyProperty.UPLOAD_ROOT_DIR.getValue();
    private final String EXPORT_VOTE_RESULTS=TeaminologyProperty.EXPORT_VOTE_RESULTS.getValue();


    @Override
    public void init() throws ServletException {
        super.init();
    }

    /**
     * Overriding the super class service method
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public void service(HttpServletRequest request, HttpServletResponse response) {
        String fileId = null;
        HttpSession session = request.getSession();
        try {
            Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                    .getKey())) ? null : (Integer) session
                    .getAttribute(SessionKey.USER_ID.getKey());

            String userUploadRootDir = UPLOAD_ROOT_DIR;
            if (userId != null) {
                userUploadRootDir = userUploadRootDir + File.separator + userId;
            }

            logger.info("Memory before uploading.....");
            Utils.logMemoryUsage();

            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            String cmd = request.getParameter("c");
            fileId = request.getParameter("fileId");
            String fileIdExport = request.getParameter("fileIdExport");
            String companyIds = request.getParameter("selectedMultiCompanyIds");
            logger.info("uploading started>>>>>>>>>"+userId);

            String tmx = null;
            File uploadedFile = null;
            String type = null;
            String exportType = null;
            byte fileArray[] = null;
            String uploadedFileName = null;
            Long fileSize = null;

            if (isMultipart) {
                logger.info("In Multi part");
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = new ArrayList<FileItem>();
                items = upload.parseRequest(request);
                logger.info("Items upload" + items);

                Iterator<FileItem> iter = items.iterator();

                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        if (fileName != null) {
                        	fileName = FilenameUtils.getName(fileName);
                        }
                        if((fileName != null) && !((fileName.indexOf(".csv") != -1) || (fileName.indexOf(".txt") != -1) || (fileName.indexOf(".tbx") != -1) || (fileName.indexOf(".tmx") != -1))) {
                        	throw new IOException("Invalid File Format");
                        }
                        File userUploadRootDirFile = new File(userUploadRootDir);
                        if (!userUploadRootDirFile.exists() || userUploadRootDirFile.isFile())
                            userUploadRootDirFile.mkdir();
                        if (StringUtil.isEmpty(fileName) || "null".equalsIgnoreCase(fileName) || fileName.lastIndexOf(".") == -1)
                            continue;

                        logger.info("Final upload fileName -- " + fileName);

                        String extension = null;
                        int dotPos = fileName.lastIndexOf(".");
                        extension = fileName.substring(dotPos);
                        if (".csv".equalsIgnoreCase(extension) || ".txt".equalsIgnoreCase(extension)) {
                            cmd = "importCSV";
                        } else if (".tbx".equalsIgnoreCase(extension)) {
                            cmd = "importTBX";
                        }

                        uploadedFileName = fileName.substring(0, dotPos) + "_" + new Date().getTime() + extension;
                        uploadedFile = new File(userUploadRootDirFile, uploadedFileName);
                        fileArray = item.get();
                        uploadedFileName = uploadedFile.getAbsolutePath();

                        logger.info("Finished upload contact uploadedFile -- " + uploadedFileName);
                    }
                    if (item.getFieldName().equalsIgnoreCase("c") && item.getString().equalsIgnoreCase("importTMX")) {
                        tmx = "importTMX";
                    }
                    if (item.getFieldName().equalsIgnoreCase("type") && item.getString().equalsIgnoreCase("termInfo")) {
                        type = "termInfo";
                    }
                    if (item.getFieldName().equalsIgnoreCase("type") && item.getString().equalsIgnoreCase("user")) {
                        type = "user";
                    }
                    if (item.getFieldName().equalsIgnoreCase("fileId")) {
                        fileId = item.getString();
                    }
                }
                logger.info("Memory after uploading.....");
                Utils.logMemoryUsage();
            } else {
                logger.info("Else part of is multipart");
                exportType = request.getParameter("exportType");
            }
            if (fileArray != null && fileArray.length > 0) {
                if (uploadedFileName != null) {
                    FileOutputStream fos = new FileOutputStream(uploadedFileName);
                    fos.write(fileArray);
                    File newFile = new File(uploadedFileName);
                    fileSize = newFile.length();
                    logger.info("File size in Bytes " + fileSize);
                    if (fileSize != null && fileSize > 0) {
                        fileSize = fileSize / (1024 * 1024);
                    }
                    if (fos != null)
                        fos.close();
                }
            }
            Long fileMaxSize = Long.parseLong(TeaminologyProperty.IMPORT_MAXFILESIZE_IN_MB.getValue());
            if (fileSize != null && fileSize.longValue() > fileMaxSize.longValue()) {
                CSVImportStatus importStatus = new CSVImportStatus();
                String statusMsg = UploadStatusMessages.TOO_LARGE.getValue();
                statusMsg = statusMsg.replace("@@FILESIZE@@", TeaminologyProperty.IMPORT_MAXFILESIZE_IN_MB.getValue());
                importStatus.setTermInformationStatus(statusMsg);
                session.setAttribute(SessionKey.UPLOAD_STATUS.getKey(), importStatus);
                if (fileId != null) {
                    BufferedWriter bw = null;
                    try {
                        FileUploadStatus fileUploadData = TeaminologyService.getFileUploadStatus(fileId);

                        File logFileDir = new File(userUploadRootDir);
                        if (!logFileDir.exists()) {
                            logFileDir.mkdir();
                        }
                        String fileName = "teaminologyImport" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSS").format(new Date()) + ".log";
                        String logFile = userUploadRootDir + "/" + fileName;
                        fileUploadData.setFileLogUrl(logFile);
                        logger.info("uploading file path >>>>>>>>>" + fileId);
                        fileUploadData.setFileStatus("File upload failed");
                        fileUploadData.setProccesedPercentage(100);
                        TeaminologyService.updateFileUploadStatus(fileUploadData);
                        fileId = null;

                        bw = new BufferedWriter(new FileWriter(logFile));
                        String errorMsg = "The file was too large it can not be uploaded";
                        bw.write(errorMsg);
                        bw.newLine();
                        return;
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    finally {
                        if (bw != null)
                            try {
                                bw.close();
                            }
                            catch (IOException e1) {
                                logger.error("Cannot handle imported file", e1);
                            }
                    }
                }
            }
            logger.info("Import Export Service.....");
            logger.info("Value of cmd is " + cmd);
            if ("importCSV".equalsIgnoreCase(cmd)) {
                importCSV(request, response, type, uploadedFile, companyIds);
            } else if ("importTBX".equalsIgnoreCase(cmd) && tmx == null) {
                importTBX(request, response, uploadedFile, companyIds);
            } else if ("exportCSV".equalsIgnoreCase(cmd)) {
                exportCSV(request, response, exportType);
            } else if ("exportXLF".equalsIgnoreCase(cmd)) {
                exportXliff(request, response);
            } else if ("exportTMX".equalsIgnoreCase(cmd)) {
                exportTMXData(request, response, exportType, fileIdExport);
            } else if ("importTMX".equalsIgnoreCase(tmx)) {
                importTMX(request, response, uploadedFile, fileId);
            } else if ("exportTMXFile".equalsIgnoreCase(cmd)) {
                downloadTMXFile(request, response, exportType, fileId);
            } else if ("downloadLogFile".equalsIgnoreCase(cmd)) {
                downloadLogFile(request, response, exportType, fileId);
            } else if ("downloadGSLogFile".equalsIgnoreCase(cmd)) {
                downloadGSLogFile(request, response, exportType);
            } else if ("downloadTemplateFile".equalsIgnoreCase(cmd)) {
                downloadTemplateFile(request, response);
            } else if ("downloadAdminManual".equalsIgnoreCase(cmd)) {
                downloadAdminDocsTemplate(request, response);
            } else if ("downloadUserManual".equalsIgnoreCase(cmd)) {
                downloadUserDocsTemplate(request, response);
            } else if ("downloadTBXTmpltFile".equalsIgnoreCase(cmd)) {
                downloadTBXTmpltFile(request, response);
            } else if("downloadTABTmpltFile".equalsIgnoreCase(cmd)){
            	downloadTABTmpltFile(request,response);
            }
            else {
                CSVImportStatus importStatus = new CSVImportStatus();
                importStatus.setTermInformationStatus(UploadStatusMessages.INVALID_FILE_TYPE.getValue());
                session.setAttribute(SessionKey.UPLOAD_STATUS.getKey(), importStatus);
            }
        }
        catch (Exception e) {
            logger.info("Uploading failed. FileId is >>>>>>>>>" + fileId);
            if (request.getParameter("c") == null) {
                CSVImportStatus importStatus = new CSVImportStatus();
                importStatus.setTermInformationStatus(UploadStatusMessages.INVALID.getValue());
                session.setAttribute(SessionKey.UPLOAD_STATUS.getKey(), importStatus);
            }
            if (fileId != null) {
                BufferedWriter bw = null;
                try {
                    FileUploadStatus fileUploadData = TeaminologyService.getFileUploadStatus(fileId);

                    File logFileDir = new File(UPLOAD_ROOT_DIR);
                    if (!logFileDir.exists()) {
                        logFileDir.mkdir();
                    }
                    String fileName = "teaminologyImport" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSS").format(new Date()) + ".log";
                    fileUploadData.setFileLogUrl(UPLOAD_ROOT_DIR + "/" + fileName);
                    logger.info("uploading file path >>>>>>>>>" + fileId);
                    fileUploadData.setFileStatus("File upload failed");
                    fileUploadData.setProccesedPercentage(100);
                    TeaminologyService.updateFileUploadStatus(fileUploadData);

                    String logFile = UPLOAD_ROOT_DIR + "/" + fileName;

                    bw = new BufferedWriter(new FileWriter(logFile));
                    String errorMsg = "The file was too large it can not be uploaded";
                    bw.write(errorMsg);
                    bw.newLine();
                }
                catch (Exception exception) {
                    logger.error("Error found!", exception);
                }
                finally {
                    if (bw != null)
                        try {
                            bw.close();
                        }
                        catch (IOException e1) {
                            logger.error("Failed to import TMX file", e1);
                        }
                }
            }
        }
        catch (Throwable e) {
            logger.error("Error found in Throwable exception >>>>>>>>>" + fileId);
            logger.error(e,e);
            if (request.getParameter("c") == null) {
                CSVImportStatus importStatus = new CSVImportStatus();
                importStatus.setTermInformationStatus(UploadStatusMessages.INVALID.getValue());
                session.setAttribute(SessionKey.UPLOAD_STATUS.getKey(), importStatus);
            }
            if (fileId != null) {
                BufferedWriter bw = null;
                try {
                    FileUploadStatus fileUploadData = TeaminologyService.getFileUploadStatus(fileId);


                    File logFileDir = new File(UPLOAD_ROOT_DIR);
                    if (!logFileDir.exists()) {
                        logFileDir.mkdir();
                    }
                    String fileName = "teaminologyImport" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSS").format(new Date()) + ".log";
                    fileUploadData.setFileLogUrl(UPLOAD_ROOT_DIR + "/" + fileName);
                    logger.info("uploading file path >>>>>>>>>" + fileId);
                    fileUploadData.setFileStatus("File upload failed");
                    fileUploadData.setProccesedPercentage(100);
                    TeaminologyService.updateFileUploadStatus(fileUploadData);

                    String logFile = UPLOAD_ROOT_DIR + "/" + fileName;

                    bw = new BufferedWriter(new FileWriter(logFile));
                    String errorMsg = "The file was too large it can not be uploaded";
                    bw.write(errorMsg);
                    bw.newLine();
                }
                catch (Exception exception) {
                    e.printStackTrace();
                }
                finally {
                    if (bw != null)
                        try {
                            bw.close();
                        }
                        catch (IOException e1) {
                            logger.error("Failed to import TMX file", e1);
                        }
                }
            }
        }
    }

    /**
     * Used to download user docs template
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */

    private void downloadUserDocsTemplate(HttpServletRequest request,
                                          HttpServletResponse response) {

        try {


            String adminDocsTemplate = TeaminologyProperty.USER_MANUAL.getValue();
            File templateFile = new File(adminDocsTemplate);
            ServletOutputStream sos = null;
            String file = null;
            byte[] bytes = new byte[1];
            InputStream is = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int length = 0;

            sos = response.getOutputStream();
            response.setContentType("text/doc");

            String fileName = adminDocsTemplate;
            if (fileName != null && fileName.lastIndexOf("/") > 0) {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            }
            
            logger.info("file path  >>>" + fileName);
            System.out.println("file path  >>>" + fileName);
            System.out.println("file path" + adminDocsTemplate);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            System.out.println("File:::::::::" + adminDocsTemplate);
            is = new DataInputStream(new FileInputStream(adminDocsTemplate));
            length = Integer.parseInt(String.valueOf(templateFile.length()));

            response.setContentLength(length);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sos);

            bytes = new byte[length];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                bos.write(bytes, 0, bytesRead);
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            sos.flush();
            sos.close();

        }
        catch (Exception e) {
            logger.error("Failed to load Template file");
            logger.error(e,e);
        }

    }

    /**
     * Used to download admin docs template
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */

    private void downloadAdminDocsTemplate(HttpServletRequest request,
                                           HttpServletResponse response) {
        try {


            String userDocsTemplate = TeaminologyProperty.ADMIN_MANUAL.getValue();
            File templateFile = new File(userDocsTemplate);
            ServletOutputStream sos = null;
            String file = null;
            byte[] bytes = new byte[1];
            InputStream is = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int length = 0;

            sos = response.getOutputStream();
            response.setContentType("text/doc");

            String fileName = userDocsTemplate;
            if (fileName != null && fileName.lastIndexOf("/") > 0) {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            }
            
            logger.info("file path  >>>" + fileName);
            logger.info("file path" + userDocsTemplate);
            
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            logger.info("File:::::::::" + userDocsTemplate);
            is = new DataInputStream(new FileInputStream(userDocsTemplate));
            length = Integer.parseInt(String.valueOf(templateFile.length()));

            response.setContentLength(length);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sos);

            bytes = new byte[length];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                bos.write(bytes, 0, bytesRead);
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            sos.flush();
            sos.close();

        }
        catch (Exception e) {
            logger.error("Failed to load Template file");
            logger.error(e,e);
        }

    }


    /**
     * Used to import TBX files.
     *
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     * @param companyIds
     * @param uploadedFile Uploaded file name
     */
    private CSVImportStatus importTBX(HttpServletRequest request,
                                      HttpServletResponse response, File uploadedFile, String companyIds) {
        CSVImportStatus failedStatus = new CSVImportStatus();
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            

       	 logger.info("Called the Servlet /ImpExpServlet/ to Importy TBX .....");
            
            @SuppressWarnings("unchecked")
			List<Pair<String, String>> loggerMessages = Arrays.asList(
            		new Pair<String,String>(ParamKeyEnum.FILE_IMPORTED_BY.toString(),user.getUserName()),
            		new Pair<String,String>(ParamKeyEnum.FILE_IMPORTED_DATE.toString(),new Date().toString()),
            		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
            		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
            		new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
            		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
            		);
            
            logger.info("Import TBX Details ++++ "+loggerMessages);

            
            if (companyIds == null || companyIds.equalsIgnoreCase("null")) {
                companyIds = user.getCompany().getCompanyId().toString();
            }
            CSVImportStatus status = TeaminologyService.importTBX(uploadedFile, User.class, user, companyIds);
            session.setAttribute(SessionKey.UPLOAD_STATUS.getKey(), status);

            return status;

        }
        catch (Exception e) {
        	failedStatus.setTermInformationStatus("Failed to import TBX file");
            logger.error("Failed to import TBX file");
            logger.error(e,e);
        }
        return failedStatus;
    }


    /**
     * Used to import CSV files.
     *
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     * @param type         String holding the values termInfo/user to import the respective data
     * @param companyIds
     * @param uploadedFile File holding the uploaded file name
     */
    private void importCSV(HttpServletRequest request,
                           HttpServletResponse response, String type, File uploadedFile, String companyIds) {
        try {
            logger.info("companyIds ==== " + companyIds);
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute(SessionKey.USER_ID.getKey());
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            

            logger.info("Called the Servlet /ImpExpServlet/ to import termbase.....");
            
            @SuppressWarnings("unchecked")
			List<Pair<String, String>> loggerMessages = Arrays.asList(
            		new Pair<String,String>(ParamKeyEnum.FILE_IMPORTED_BY.toString(),user.getUserName()),
            		new Pair<String,String>(ParamKeyEnum.FILE_IMPORTED_DATE.toString(),new Date().toString()),
            		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
            		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
            		new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
            		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
            		);
            
            logger.info("Imported File Details ++++ "+loggerMessages);
            
            String pageUrl = null;
            if (session.getAttribute(SessionKey.COMPANY.getKey()) != null) {
                Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
                if (company != null && company.getContextRoot() != null)
                    pageUrl = request.getContextPath() + "/" + company.getContextRoot();
                else
                    pageUrl = request.getContextPath();

            } else {
                pageUrl = request.getContextPath();
            }
            CSVImportStatus importStatus = null;
            if ("termInfo".equalsIgnoreCase(type)) {
                if (companyIds == null || companyIds.equalsIgnoreCase("null")) {
                    companyIds = user.getCompany().getCompanyId().toString();
                }
                importStatus = TeaminologyService.importCSV(uploadedFile, TermInformation.class, user, companyIds);
                if ("success".equalsIgnoreCase(importStatus.getTermInformationStatus()))
                    pageUrl = pageUrl + TeaminologyPage.ADMIN_MANAGE_TERMS_SUCCESS.getPageUrl();
                else
                    pageUrl = pageUrl + TeaminologyPage.ADMIN_MANAGE_TERMS_FAILED.getPageUrl();
                response.sendRedirect(pageUrl);
            } else if ("user".equalsIgnoreCase(type)) {
                importStatus = TeaminologyService.importCSV(uploadedFile, User.class, user, companyIds);
            }

            session.setAttribute(SessionKey.UPLOAD_STATUS.getKey(), importStatus);
        }
        catch (Exception e) {
            logger.error("Failed to import CSV", e);
            logger.error(e,e);
        }
    }

    /**
     * Used to export TMX data.
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param fileId     String holding export tmx id
     * @param exportType String holding the type of data to export
     */

    private void exportTMXData(HttpServletRequest request,
                               HttpServletResponse response, String exportType, String fileId) {
        try {
            HttpSession session = request.getSession();
            //byte dataArray[] =null;
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            File csvFile = null;
            String fileName = "";
            String exportFileName = "";
            Integer[] intSelectedIds = null;
            Integer companyId = null;
            

        	logger.info("Called the Servlet /ImpExpServlet/ to Export TM.....");

        	@SuppressWarnings("unchecked")
        	List<Pair<String, String>> loggerMessages = Arrays.asList(
        			new Pair<String,String>(ParamKeyEnum.FILE_EXPORTED_BY.toString(),user.getUserName()),
        			new Pair<String,String>(ParamKeyEnum.FILE_EXPORTED_DATE.toString(),new Date().toString()),
        			new Pair<String,String>(ParamKeyEnum.FILE_ID.toString(),fileId),
        			new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
        			new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
        			new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
        			new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
        			);

        	logger.info("Export tmx File Details ++++ "+loggerMessages);
            
            if (user != null && user.getCompany() != null) {
                companyId = user.getCompany().getCompanyId();

            }

            if ("termInfo".equalsIgnoreCase(exportType)) {
                fileName = "/" + TERMS_EXCEL;
                exportFileName = TERMS_EXCEL;
                String[] selectedIds = request.getParameterValues("selectedIds");
                if (!"null".equalsIgnoreCase(selectedIds[0])) {
                    String[] ids = selectedIds[0].split(",");
                    intSelectedIds = new Integer[ids.length];
                    for (int i = 0; i < ids.length; i++) {
                        intSelectedIds[i] = Integer.parseInt(ids[i]);
                    }
                }
                FileUploadStatus fileUpload = new FileUploadStatus();
                fileUpload = TeaminologyService.getFileUploadStatus(fileId);
                if (fileUpload != null) {
                    TeaminologyService.updateFileUploadStatus(fileUpload);
                }
                String exportBy = request.getParameter("exportBy");
                if (exportBy != null && fileId != null) {
                    ExecutorService executorObj = ExportTMXExecutor.INSTANCE.get();
                    Runnable thread = new ExportTMXThread(new Integer(fileId), exportBy, intSelectedIds, companyId);
                    executorObj.execute(thread);
                }
            }
        }
        catch (Exception e) {
            logger.error("Error found in exportTBXData method.", e);
            logger.error(e,e);
        }
    }

    /**
     * Used to export CSV files.
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param exportType String holding the type of data to export
     */
    @SuppressWarnings("unchecked")
	private void exportCSV(HttpServletRequest request,
                           HttpServletResponse response, String exportType) {
        try {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            String fromDate=request.getParameter("fromDate");
            String toDate=request.getParameter("toDate");
            String topSugCheck=request.getParameter("topSug");
            String alterSugCheck=request.getParameter("alterSug");
            String votesCheck=request.getParameter("noofVotes");
            String exportBy = request.getParameter("exportBy");
            String exportFormat = request.getParameter("exportFormat");
			logger.info("Called the Servlet /ImpExpServlet/ to Export csv.....");
				    	List<Pair<String, String>> loggerMessages = new ArrayList<Pair<String,String>>();
        	if(exportType != null) {
        		if("exportAll".equalsIgnoreCase(exportType)) {
                    loggerMessages.add(new Pair<String,String>(ParamKeyEnum.EXPORT_REPORT_CONTENT.toString(),request.getParameter("reportType")));
                    loggerMessages.add(new Pair<String,String>(ParamKeyEnum.LANGUAGE_CODE.toString(),(request.getParameter("userLanguages") != null ? request.getParameter("userLanguages") : "All")));
        		} else if("languageReport".equalsIgnoreCase(exportType)){
        			loggerMessages.add(new Pair<String,String>(ParamKeyEnum.EXPORT_REPORT_CONTENT.toString(),"Monthly Language Activity"));
        		} else if("termGlosary".equalsIgnoreCase(exportType)){
        			loggerMessages.add(new Pair<String,String>(ParamKeyEnum.EXPORT_REPORT_CONTENT.toString(),"Terms in Glosary"));
        		} else if("term".equalsIgnoreCase(exportType)){
        			loggerMessages.add(new Pair<String,String>(ParamKeyEnum.EXPORT_REPORT_CONTENT.toString(),"Terms being Debated"));
        		} else if("languageDistribution".equalsIgnoreCase(exportType)){
        			loggerMessages.add(new Pair<String,String>(ParamKeyEnum.EXPORT_REPORT_CONTENT.toString(),"Language Distribution"));
        		} else if("tms".equalsIgnoreCase(exportType)){
        			loggerMessages.add(new Pair<String,String>(ParamKeyEnum.EXPORT_REPORT_CONTENT.toString(),"TM units"));
        		} else {
        			loggerMessages.add(new Pair<String,String>(ParamKeyEnum.EXPORT_FILE_CONTENT.toString(),exportType.toUpperCase()));
        		}
        	}
        	loggerMessages.add(new Pair<String,String>(ParamKeyEnum.FILE_EXPORTED_BY.toString(),user.getUserName()));
        	loggerMessages.add(new Pair<String,String>(ParamKeyEnum.FILE_EXPORTED_DATE.toString(),new Date().toString()));
        	loggerMessages.add(new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()));
        	loggerMessages.add(new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()));
        	loggerMessages.add(new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()));
        	loggerMessages.add(new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),request.getRemoteAddr()));
        	
        	logger.info("Export CSV File Details ++++ "+loggerMessages);
        	
            File csvFile = null;
            String fileName = "";
            String exportFileName = "";
            Integer[] intSelectedIds = null;
            List<String> fileNameList = null;
            Set<Integer> companyIds = null;
            Integer companyId = null;
            Set<CompanyTransMgmt> companyTransMgmtUsers = new HashSet<CompanyTransMgmt>();
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
                companyTransMgmtUsers = TeaminologyService.getCompanyTransMgmtUsers(user.getUserId());
                if (companyTransMgmtUsers != null && !companyTransMgmtUsers.isEmpty()) {
                    companyIds = new HashSet<Integer>();
                    for (CompanyTransMgmt companyTransMgmtObj : companyTransMgmtUsers) {
                        companyIds.add(companyTransMgmtObj.getCompanyId());
                    }
                }
            }
            if ("exportVote".equalsIgnoreCase(exportType)) {
            	String flag="Y";
                fileName = "/" + EXPORT_VOTE_RESULTS;
                exportFileName = EXPORT_VOTE_RESULTS;
                List<TermVotingTo> termList = TeaminologyService.getVoteResults(fromDate, toDate,companyId);
            @SuppressWarnings("rawtypes")
				List params=new ArrayList();
                params.add(topSugCheck);
                params.add(alterSugCheck);
                params.add(votesCheck);
                params.add(fromDate);
                params.add(toDate);
                params.add(flag);
                 if(termList !=null)
                csvFile = TeaminologyService.writeVoteCSV(termList, fileName, UPLOAD_ROOT_DIR, params);
                else
                {
                	flag="N";
                	params.add(5, flag);
                csvFile = TeaminologyService.writeVoteCSV(termList, fileName, UPLOAD_ROOT_DIR, params);
                }	
            }else if("termGlosary".equalsIgnoreCase(exportType)) {
                fileName = "/" + TERM_CHART_GLOSSARY;
                exportFileName = TERM_CHART_GLOSSARY;
                List<Terms> termList = TeaminologyService.getTermsInGlossary(companyIds);
                termList = TeaminologyService.getFinalisedTerms(termList, "Yearly");
                csvFile = TeaminologyService.writeCSV(termList, fileName, UPLOAD_ROOT_DIR,exportFormat);
            }else if ("term".equalsIgnoreCase(exportType)) {
                fileName = "/" + TERM_CHART_DATA_EXCEL;
                exportFileName = TERM_CHART_DATA_EXCEL;
                List<Terms> termList = TeaminologyService.getDebatedTerms(companyIds);
                termList = TeaminologyService.getFinalisedTerms(termList, "Yearly");

                csvFile = TeaminologyService.writeCSV(termList, fileName, UPLOAD_ROOT_DIR,exportFormat);
            } else if ("tms".equalsIgnoreCase(exportType)) {
                fileName = "/" + TM_CHART_GLOSSARY;
                exportFileName = TM_CHART_GLOSSARY;
                List<Terms> termList = TeaminologyService.getTmsInGlossary(companyIds);
                termList = TeaminologyService.getFinalisedTms(termList, "Yearly");

                csvFile = TeaminologyService.writeCSV(termList, fileName, UPLOAD_ROOT_DIR,exportFormat);
            } else if ("termInfo".equalsIgnoreCase(exportType)) {
                fileName = "/" + TERMS_EXCEL;
                exportFileName = TERMS_EXCEL;
                String[] selectedIds = request.getParameterValues("selectedIds");
                if (!selectedIds[0].equalsIgnoreCase("null")) {
                    String[] ids = selectedIds[0].split(",");
                    intSelectedIds = new Integer[ids.length];
                    for (int i = 0; i < ids.length; i++) {
                        intSelectedIds[i] = Integer.parseInt(ids[i]);
                    }
                }

               
                List<TermInformation> termInfoList = TeaminologyService.getTermInformation(exportBy, intSelectedIds,companyId);
                Map<TermInfo, List<TermInformation>> termInfoMap = new HashMap<TermInfo, List<TermInformation>>();

                if (termInfoList != null) {
                    for (TermInformation termInfo : termInfoList) {
                        String sourceTerm = termInfo ==null ? null:termInfo.getTermBeingPolled();
                        String pos = termInfo == null ? null : termInfo.getTermPOS() == null ? null :termInfo.getTermPOS().getPartOfSpeech();
                        String domain = termInfo == null ? null : termInfo.getTermDomain() == null ? null : termInfo.getTermDomain().getDomain() ;
                        String category = termInfo == null ? null : termInfo.getTermCategory() == null ? null : termInfo.getTermCategory().getCategory(); 
                        TermInfo termInfoObj = new TermInfo();
                        termInfoObj.setSource(sourceTerm);
                        termInfoObj.setPos(pos);
                        termInfoObj.setDomain(domain);
                        termInfoObj.setCategory(category);
                        
                        List<TermInformation> newTermInfoList = new ArrayList<TermInformation>();
                        if (!termInfoMap.containsKey(termInfoObj)) {
                            newTermInfoList.add(termInfo);
                            termInfoMap.put(termInfoObj, newTermInfoList);
                        } else {
                            List<TermInformation> tempList = termInfoMap.get(termInfoObj);
                            tempList.add(termInfo);
                            termInfoMap.put(termInfoObj, tempList);
                        }
                    }
                }
                List<TermInformationTo> termInformationToList = new ArrayList<TermInformationTo>();

                for (TermInfo termInfoObj : termInfoMap.keySet()) {
                    List<TermInformation> newTermInfoList = termInfoMap.get(termInfoObj);
                    TermInformationTo termInformationTo = new TermInformationTo();
                    if (!newTermInfoList.isEmpty()) {
                        List<String> suggestedTerms = new ArrayList<String>();
                        List<Integer> suggestedTermLangIds = new ArrayList<Integer>();
                        List<Integer> suggestedPosIds = new ArrayList<Integer>();
                        List<Integer> suggestedStatusIds = new ArrayList<Integer>();
                        List<Date> createDateList = new ArrayList<Date>();
                        List<Date> updatedDateList = new ArrayList<Date>();
                        List<Integer> createdBy = new ArrayList<Integer>();
                        List<Integer> updatedBy = new ArrayList<Integer>();
                        List<Integer> suggestedFormIds = new ArrayList<Integer>();
                        Set<DeprecatedTermInformation> deprecatedTermInfoset = null;
                        List<Set<DeprecatedTermInformation>> targetdeptermlist = new ArrayList<Set<DeprecatedTermInformation>>();

                        List<String> deprecatedSourceList = new ArrayList<String>();

                        for (TermInformation termInfo : newTermInfoList) {
                            String suggestedTerm = termInfo.getSuggestedTerm();
                            Integer suggestedLangTermId = termInfo.getSuggestedTermLangId();
                            Integer suggestedPosid = termInfo.getSuggestedTermPosId();
                            Integer suggestedSatusId = termInfo.getSuggestedTermStatusId();
                            Integer suggestedFormId = termInfo.getSuggestedTermFormId();

                            if (termInfo.getDeprecatedTermInfo() != null && termInfo.getDeprecatedTermInfo().size() > deprecatedSourceList.size()) {
                                deprecatedSourceList = new ArrayList<String>();
                                for (DeprecatedTermInformation deprecatedSourceObj : termInfo.getDeprecatedTermInfo()) {
                                    deprecatedSourceList.add(deprecatedSourceObj.getDeprecatedSource());
                                }

                            }
                            Date createDate = termInfo.getCreateDate();
                            deprecatedTermInfoset = termInfo.getDeprecatedTermInfo();
                            Date updateDate = termInfo.getUpdateDate();
                            Integer createdById = termInfo.getCreatedBy() == null ? null : termInfo.getCreatedBy();
                            Integer updatedById = termInfo.getUpdatedBy() == null ? null : termInfo.getUpdatedBy();
                            suggestedTerms.add(suggestedTerm);
                            suggestedTermLangIds.add(suggestedLangTermId);
                            suggestedPosIds.add(suggestedPosid);
                            suggestedStatusIds.add(suggestedSatusId);
                            suggestedFormIds.add(suggestedFormId);
                            createDateList.add(createDate);
                            updatedDateList.add(updateDate);
                            createdBy.add(createdById);
                            updatedBy.add(updatedById);
                            targetdeptermlist.add(termInfo.getDeprecatedTermInfo());
                            termInformationTo.setComments(termInfo.getComments());
                            termInformationTo.setConceptCatId(termInfo.getConceptCatId());
                            termInformationTo.setConceptNotes(termInfo.getConceptNotes());
                            termInformationTo.setConceptDefinition(termInfo.getConceptDefinition());
                            termInformationTo.setConceptProdGroup(termInfo.getConceptProdGroup());
                            termInformationTo.setSuggestedTermNotes(termInfo.getSuggestedTermNotes());
                            termInformationTo.setSuggestedTermPgmId(termInfo.getSuggestedTermPgmId());
                            termInformationTo.setSuggestedTermUsage(termInfo.getSuggestedTermUsage());
                            termInformationTo.setTermBeingPolled(termInfo.getTermBeingPolled());
                            termInformationTo.setTermCategory(termInfo.getTermCategory());
                            termInformationTo.setTermId(termInfo.getTermId());
                            termInformationTo.setTermLangId(termInfo.getTermLangId());
                            termInformationTo.setTermNotes(termInfo.getTermNotes());
                            termInformationTo.setTermProgram(termInfo.getTermProgram());
                            termInformationTo.setTermStatusId(termInfo.getTermStatusId());
                            termInformationTo.setTermUsage(termInfo.getTermUsage());
                            termInformationTo.setTermForm(termInfo.getTermForm());
                            termInformationTo.setTermPOS(termInfo.getTermPOS());
                            termInformationTo.setTermDomain(termInfo.getTermDomain());
                            termInformationTo.setCompany(termInfo.getTermCompany());
                        }
                        termInformationTo.setDeprecatedTermInfo(deprecatedTermInfoset);
                        termInformationTo.setDeprecatedsouceSet(deprecatedSourceList);
                        termInformationTo.setSuggestedTermLangIds(suggestedTermLangIds);
                        termInformationTo.setSuggestedTermPosIds(suggestedPosIds);
                        termInformationTo.setSuggestedTerms(suggestedTerms);
                        termInformationTo.setSuggestedTermStatusIds(suggestedStatusIds);
                        termInformationTo.setCreateDate(createDateList);
                        termInformationTo.setUpdateDate(updatedDateList);
                        termInformationTo.setCreatedBy(createdBy);
                        termInformationTo.setUpdatedBy(updatedBy);
                        termInformationTo.setSuggestedTermFormId(suggestedFormIds);
                        termInformationTo.setTargetDepterInfoList(targetdeptermlist);
                        termInformationToList.add(termInformationTo);
                    }
                }

                if ("TBX".equalsIgnoreCase(exportFormat))
                {
                	exportTBX(request, response, null, termInformationToList);
                    session.setAttribute(SessionKey.DOWNLOAD_STATUS.getKey(), 1);
                }
                else {
                	csvFile = TeaminologyService.writeCSV(termInformationToList, fileName, UPLOAD_ROOT_DIR,exportFormat);
                }
            }
            if ("languageReport".equalsIgnoreCase(exportType)) {
                fileName = "/" + LANGUAGE_EXCEL;
                exportFileName = LANGUAGE_EXCEL;
                List<LanguageReportData> languageReportDataList = (List<LanguageReportData>) session.getAttribute(SessionKey.MONTH_TO_MONTH.getKey());
                if (languageReportDataList == null) {
                    languageReportDataList = new ArrayList<LanguageReportData>();
                    List<Language> topRegLangs = TeaminologyService.getTopRegLangs(companyId);
                    Set<Integer> languageIds = new HashSet<Integer>();
                    List<Terms> termsList = new ArrayList<Terms>();
                    for (Language language : topRegLangs) {
                        languageIds.add(language.getLanguageId());
                        List<Integer> termInformationList = TeaminologyService.getTermInformationByLanguage(language.getLanguageId(), companyId);
                        List<TermVoteMaster> termVoteMasterList = TeaminologyService.getTermVoteMasterByTermInfo(termInformationList, "ChartData");
                        List<TermInformation> termInformationListPerMonth = TeaminologyService.getTermInformationPerMonth(languageIds, companyId);
                        termsList = TeaminologyService.getTermList(termVoteMasterList, termInformationListPerMonth);
                        LanguageReportData languageReportData = new LanguageReportData();
                        languageReportData.setLanguage(language);
                        languageReportData.setTermsList(termsList);
                        languageReportDataList.add(languageReportData);
                    }
                }
                csvFile = TeaminologyService.writeCSV(languageReportDataList, fileName, UPLOAD_ROOT_DIR,exportFormat);
            } else if ("languageDistribution".equalsIgnoreCase(exportType)) {
                fileName = "/" + LANGUAGE_DISTRIBUTION;
                exportFileName = LANGUAGE_DISTRIBUTION;
                List<LanguagePiChartData> languagePiChartDataList = (List<LanguagePiChartData>) session.getAttribute(SessionKey.DISTRIBUTION.getKey());
                if (languagePiChartDataList == null) {
                    languagePiChartDataList = new ArrayList<LanguagePiChartData>();
                    List<Language> topRegLangs = TeaminologyService.getTopRegLangs(companyId);
                    Set<Integer> languageIds = null;
                    Set<Integer> topRegLangIds = new HashSet<Integer>();
                    for (Language language : topRegLangs) {
                        topRegLangIds.add(language.getLanguageId());
                        List<Integer> termInformationList = TeaminologyService.getTermInformationByLanguage(language.getLanguageId(), companyId);
                        LanguagePiChartData languagePiChartData = new LanguagePiChartData();
                        languagePiChartData.setLanguage(language.getLanguageCode());
                        if (termInformationList != null) {
                            languagePiChartData.setNoOfTerms(termInformationList.size());
                        } else {
                            languagePiChartData.setNoOfTerms(0);
                        }

                        languagePiChartDataList.add(languagePiChartData);
                    }
                    Integer countLang = TeaminologyService.getTermInformationForOtherLanguage(topRegLangIds, companyId);
                    LanguagePiChartData languagePiChartData = new LanguagePiChartData();
                    languagePiChartData.setLanguage("Other");
                    languagePiChartData.setNoOfTerms(countLang);

                    languagePiChartDataList.add(languagePiChartData);
                }
                csvFile = TeaminologyService.writeCSV(languagePiChartDataList, fileName, UPLOAD_ROOT_DIR,exportFormat);
            } else if ("user".equalsIgnoreCase(exportType)) {
                fileName = "/" + EXPORT_USER;
                exportFileName = EXPORT_USER;
                String roles = request.getParameter("role");
                String languages = request.getParameter("userLanguages");
                String companies = request.getParameter("slctCompanies");
                String[] roleId = (roles == null) ? null : roles.split(",");
                String[] userLanguage = (languages == null) ? null : languages.split(",");
                String[] slctCompanie = (companies == null) ? null : companies.split(",");
                Set<Integer> roleIds = new HashSet<Integer>();
                Set<Integer> userLanguages = new HashSet<Integer>();
                Set<Integer> slctCompanies = new HashSet<Integer>();
                if (roleId != null)
                    for (String role : roleId) {
                        if (role != null)
                            roleIds.add(new Integer(role));
                    }
                if (userLanguage != null)
                    for (String language : userLanguage) {
                        if (language != null)
                            userLanguages.add(new Integer(language));
                    }
                if(slctCompanie != null)
                	for(String company : slctCompanie) {
                		if(company != null)
                			slctCompanies.add(new Integer(company));
                	}
                
                List<User> userList = TeaminologyService.getUserToExport(roleIds, userLanguages, user.getCompany(), slctCompanies);
                csvFile = TeaminologyService.writeCSV(userList, fileName, UPLOAD_ROOT_DIR,exportFormat);
            } else if ("exportAll".equalsIgnoreCase(exportType)) {
                String reportType = request.getParameter("reportType");
                String languages = request.getParameter("userLanguages");
                boolean isAllLanguages = false;
                fileNameList = new ArrayList<String>();
                String[] reportTypes = (reportType == null) ? null : reportType.split(",");
                String[] userLanguage = (languages == null) ? null : languages.split(",");
                Set<Integer> userLanguages = new HashSet<Integer>();
                List<Language> userLanguagesList = null;
                if (userLanguage != null) {
                    for (String language : userLanguage) {
                        if (language != null)
                            userLanguages.add(new Integer(language));
                    }
                } else {
                    isAllLanguages = true;
                    if (session.getAttribute(SessionKey.LANGUAGES.getKey()) == null) {
                        userLanguagesList = TeaminologyService.getLanguages();
                        session.setAttribute(SessionKey.LANGUAGES.getKey(), userLanguagesList);
                    } else {
                        userLanguagesList = (List<Language>) session.getAttribute(SessionKey.LANGUAGES.getKey());
                    }
                    for (Language language : userLanguagesList) {
                        userLanguages.add(language.getLanguageId());
                    }
                }
                if (reportTypes != null)
                    for (String report : reportTypes) {
                        if (report != null) {
                            if ("Overview".equalsIgnoreCase(report)) {
                                fileName = "/" + LANGUAGE_REPORT_TABLE;
                                exportFileName = LANGUAGE_REPORT_TABLE;
                                List<LanguageReportTable> languageReportDataList = new ArrayList<LanguageReportTable>();
                                try {
                                    Set<Integer> languageIds = new HashSet<Integer>();
                                    float totalAccuracyRate = 0;
                                    int listSize = 0;
                                    int tempCount = 0;
                                    List<Status> statusList = null;
                                    if (session.getAttribute(SessionKey.STATUS_LOOKUP.getKey()) == null) {
                                        statusList = TeaminologyService.getStatusLookUp();
                                        session.setAttribute(SessionKey.STATUS_LOOKUP.getKey(), statusList);
                                    } else {
                                        statusList = (List<Status>) session.getAttribute(SessionKey.STATUS_LOOKUP.getKey());
                                    }
                                    Integer APPROVED_STATUS = 0;
                                    for (Status status : statusList) {
                                        if (status.getStatus().equalsIgnoreCase("Approved")) {
                                            APPROVED_STATUS = status.getStatusId();
                                        }
                                    }
                                    for (Integer language : userLanguages) {
                                        totalAccuracyRate = 0;
                                        listSize = 0;
                                        LanguageReportTable langReportTableData = new LanguageReportTable();
                                        List<Integer> termInformationList = TeaminologyService.getTermInformationByLanguage(language, companyId);
                                        List<Member> teamMembersList = TeaminologyService.getTeamMemberDetails(language.toString(), null, null, null, 0, 0, companyId);
                                        Integer totalVotesPerLanguage = TeaminologyService.getTotalVotesPerLang(language, companyId);
                                        Integer monthlyVotesPerLanguage = TeaminologyService.getMonthlyVotesPerLang(language.toString(), companyId);
                                        for (Member teamMember : teamMembersList) {
                                            float accuracy = (teamMember.getAccuracy() == null) ? 0 : teamMember.getAccuracy().floatValue();

                                            totalAccuracyRate += accuracy;

                                        }
                                        List<Integer> approvedTermInformationList = TeaminologyService.getTermInformationByLanguageAndStatus(language, APPROVED_STATUS, companyId);

                                        List<TermVoteMaster> currentDebatedTermsList = TeaminologyService.getTermVoteMasterByTermInfo(approvedTermInformationList, "TableData");
                                        Integer activePolls = TeaminologyService.getActivePolls(currentDebatedTermsList);
                                        Language languageObj = TeaminologyService
                                                .getLanguage(language);
                                        //set language label
                                        langReportTableData.setLanguageLabel(languageObj.getLanguageLabel());

                                        //set total terms per language
                                        listSize = termInformationList == null ? 0 : termInformationList.size();
                                        langReportTableData.setTotalTerms(listSize);

                                        //set members registered per language
                                        listSize = teamMembersList == null ? 0 : teamMembersList.size();
                                        langReportTableData.setMembers(listSize);

                                        //set Accuracy average of all repondents
                                        float accuracyRate = listSize == 0 ? 0 : totalAccuracyRate / listSize;
                                        DecimalFormat df = new DecimalFormat("#.##");
                                        langReportTableData.setAccuracy(new BigDecimal(df.format(accuracyRate)));

                                        //set debated terms count
                                        listSize = currentDebatedTermsList == null ? 0 : currentDebatedTermsList.size();
                                        langReportTableData.setDebatedTerms(listSize);

                                        //set Monthly average votes
                                        tempCount = monthlyVotesPerLanguage == null ? 0 : monthlyVotesPerLanguage;
                                        langReportTableData.setMonthlyAvg(tempCount);

                                        //set total votes per language
                                        tempCount = totalVotesPerLanguage == null ? 0 : totalVotesPerLanguage;
                                        langReportTableData.setTotalVotes(tempCount);

                                        //set Active Polls
                                        tempCount = activePolls == null ? 0 : activePolls;
                                        langReportTableData.setActivePolls(tempCount);
                                        languageReportDataList.add(langReportTableData);
                                    }

                                }
                                catch (Exception e) {
                                    //e.printStackTrace();
                                    logger.warn("Failed to get language report table data", e);
                                }
                                TeaminologyService.writeCSV(languageReportDataList, fileName, UPLOAD_ROOT_DIR,exportFormat);
                                fileNameList.add(LANGUAGE_REPORT_TABLE);
                            } else if ("Distribution".equalsIgnoreCase(report)) {
                                exportFileName = LANGUAGE_DISTRIBUTION;
                                fileName = "/" + LANGUAGE_DISTRIBUTION;
                                fileNameList.add(LANGUAGE_DISTRIBUTION);
                                List<LanguagePiChartData> languagePiChartDataList = new ArrayList<LanguagePiChartData>();
                                Set<Integer> languageIds = null;
                                for (Integer language : userLanguages) {
                                    List<Integer> termInformationList = TeaminologyService.getTermInformationByLanguage(language, companyId);
                                    LanguagePiChartData languagePiChartData = new LanguagePiChartData();
                                    Language languageObj = TeaminologyService.getLanguage(language);
                                    languagePiChartData.setLanguage(languageObj.getLanguageLabel());
                                    if (termInformationList != null) {
                                        languagePiChartData.setNoOfTerms(termInformationList.size());
                                    } else {
                                        languagePiChartData.setNoOfTerms(0);
                                    }

                                    languagePiChartDataList.add(languagePiChartData);
                                }
                                Integer countLang = TeaminologyService.getTermInformationForOtherLanguage(userLanguages, companyId);
                                LanguagePiChartData languagePiChartData = new LanguagePiChartData();
                                languagePiChartData.setLanguage("Other");
                                languagePiChartData.setNoOfTerms(countLang);

                                languagePiChartDataList.add(languagePiChartData);
                                TeaminologyService.writeCSV(languagePiChartDataList, fileName, UPLOAD_ROOT_DIR,exportFormat);
                            } else if ("Terms Debated".equalsIgnoreCase(report)) {
                                exportFileName = TERM_CHART_DATA_EXCEL;
                                fileName = "/" + TERM_CHART_DATA_EXCEL;
                                fileNameList.add(TERM_CHART_DATA_EXCEL);
                                List<Terms> termList = TeaminologyService.getDebatedTerms(companyIds);
                                termList = TeaminologyService.getFinalisedTerms(termList, "Yearly");

                                TeaminologyService.writeCSV(termList, fileName, UPLOAD_ROOT_DIR,exportFormat);

                            } else if ("Terms in Glossary".equalsIgnoreCase(report)) {
                                exportFileName = TERM_CHART_GLOSSARY;
                                fileName = "/" + TERM_CHART_GLOSSARY;
                                fileNameList.add(TERM_CHART_GLOSSARY);

                                List<Terms> termList = TeaminologyService.getTermsInGlossary(companyIds);
                                termList = TeaminologyService.getFinalisedTerms(termList, "Yearly");

                                TeaminologyService.writeCSV(termList, fileName, UPLOAD_ROOT_DIR,exportFormat);

                            } else if ("Month to Month".equalsIgnoreCase(report)) {
                                exportFileName = LANGUAGE_EXCEL;
                                fileName = "/" + LANGUAGE_EXCEL;
                                fileNameList.add(LANGUAGE_EXCEL);
                                List<LanguageReportData> languageReportDataList = new ArrayList<LanguageReportData>();
                                Set<Integer> languageIds = new HashSet<Integer>();
                                List<Terms> termsList = new ArrayList<Terms>();
                                for (Integer language : userLanguages) {
                                    List<Integer> termInformationList = TeaminologyService.getTermInformationByLanguage(language, companyId);
                                    List<TermVoteMaster> termVoteMasterList = TeaminologyService.getTermVoteMasterByTermInfo(termInformationList, "ChartData");
                                    List<TermInformation> termInformationListPerMonth = TeaminologyService.getTermInformationPerMonth(languageIds, companyId);
                                    termsList = TeaminologyService.getTermList(termVoteMasterList, termInformationListPerMonth);
                                    LanguageReportData languageReportData = new LanguageReportData();
                                    Language languageObj = TeaminologyService.getLanguage(language);
                                    languageReportData.setLanguage(languageObj);
                                    languageReportData.setTermsList(termsList);
                                    languageReportDataList.add(languageReportData);
                                }
                                TeaminologyService.writeCSV(languageReportDataList, fileName, UPLOAD_ROOT_DIR,exportFormat);
                            } else if ("Tms in Glossary".equalsIgnoreCase(report)) {
                                exportFileName = TM_CHART_GLOSSARY;
                                fileName = "/" + TM_CHART_GLOSSARY;
                                fileNameList.add(TM_CHART_GLOSSARY);

                                List<Terms> termList = TeaminologyService.getTmsInGlossary(companyIds);
                                termList = TeaminologyService.getFinalisedTms(termList, "Yearly");

                                TeaminologyService.writeCSV(termList, fileName, UPLOAD_ROOT_DIR,exportFormat);
                            }
                        }
                    }
            }

            if (fileNameList != null && !fileNameList.isEmpty()) {
                if (fileNameList.size() == 1) {
                    csvFile = new File(UPLOAD_ROOT_DIR + "/" + exportFileName + ".csv");
                    fileNameList = null;
                } else {
                    createZipFile(fileNameList);
                }
            }
            if(exportFormat != null){
                  if ((csvFile != null || (fileNameList != null && !fileNameList.isEmpty())) &&  (exportFormat.equalsIgnoreCase("TAB"))) {
                      session.setAttribute(SessionKey.DOWNLOAD_STATUS.getKey(), 1);
                      ServletOutputStream sos = null;
                      exportFileName = exportFileName + ".csv";
                      String file = UPLOAD_ROOT_DIR + "/termTabData.txt";
                      byte[] bytes = new byte[1];
                      InputStream is = null;
                      BufferedInputStream bis = null;
                      BufferedOutputStream bos = null;
                      int length = 0;
                      String contentType = "text/plain";
                      if (fileNameList != null && !fileNameList.isEmpty()) {
                          contentType = "application/zip";
                          file = UPLOAD_ROOT_DIR + "/reports.zip";
                          exportFileName = "reports.zip";
                          csvFile = new File(file);
                      }

                      sos = response.getOutputStream();
                      response.setContentType(contentType);
                      response.setHeader("Content-Disposition", "attachment; filename=\"" + "termTabData.txt"  + "\"");
                      is = new DataInputStream(new FileInputStream(file));
                      length = Integer.parseInt(String.valueOf(csvFile.length()));
                      response.setContentLength(length);
                      bis = new BufferedInputStream(is);
                      bos = new BufferedOutputStream(sos);

                      bytes = new byte[length];
                      int bytesRead;
                      while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                          bos.write(bytes, 0, bytesRead);
                      }
                      ServletOutputStream out = response.getOutputStream();
                      out.write(bytes);
                      sos.flush();
                  }
                  
            else {
                session.setAttribute(SessionKey.DOWNLOAD_STATUS.getKey(), 1);
                ServletOutputStream sos = null;
                exportFileName = exportFileName + ".csv";
                String file = UPLOAD_ROOT_DIR + "/" + exportFileName;
                byte[] bytes = new byte[1];
                InputStream is = null;
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                int length = 0;
                String contentType = "text/csv";
                if (fileNameList != null && !fileNameList.isEmpty()) {
                    contentType = "application/zip";
                    file = UPLOAD_ROOT_DIR + "/reports.zip";
                    exportFileName = "reports.zip";
                    csvFile = new File(file);
                }

                sos = response.getOutputStream();
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + exportFileName + "\"");
                is = new DataInputStream(new FileInputStream(file));
                length = Integer.parseInt(String.valueOf(csvFile.length()));
                response.setContentLength(length);
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(sos);

                bytes = new byte[length];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                    bos.write(bytes, 0, bytesRead);
                }
                ServletOutputStream out = response.getOutputStream();
                out.write(bytes);
                sos.flush();
            
            }
            }
            if(!(exportType.equalsIgnoreCase("termInfo")) && !(exportType.equalsIgnoreCase("exportVote"))){
                session.setAttribute(SessionKey.DOWNLOAD_STATUS.getKey(), 1);
                ServletOutputStream sos = null;
                exportFileName = exportFileName + ".csv";
                String file = UPLOAD_ROOT_DIR + "/" + exportFileName;
                byte[] bytes = new byte[1];
                InputStream is = null;
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                int length = 0;
                String contentType = "text/csv";
                if (fileNameList != null && !fileNameList.isEmpty()) {
                    contentType = "application/zip";
                    file = UPLOAD_ROOT_DIR + "/reports.zip";
                    exportFileName = "reports.zip";
                    csvFile = new File(file);
                }

                sos = response.getOutputStream();
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + exportFileName + "\"");
                is = new DataInputStream(new FileInputStream(file));
                length = Integer.parseInt(String.valueOf(csvFile.length()));

                response.setContentLength(length);
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(sos);

                bytes = new byte[length];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                    bos.write(bytes, 0, bytesRead);
                }
                ServletOutputStream out = response.getOutputStream();
                out.write(bytes);
                sos.flush();
            
            }
            }
        catch (Exception e) {
            logger.error("Failed to get export CSV", e);
            logger.error(e,e);
        }
    }

    /**
     * Used to export TBX.
     *
     * @param request               HttpServletRequest
     * @param response              HttpServletResponse
     * @param uploadedFile          File holding the uploaded file name
     * @param termInformationToList Data holding the Terms data to export
     */
    private void exportTBX(HttpServletRequest request,
                           HttpServletResponse response, File uploadedFile, List<TermInformationTo> termInformationToList) {
        try {

            File xmlFile = null;
            xmlFile = TeaminologyService.writeTBX(termInformationToList, UPLOAD_ROOT_DIR + "/terminologyTemp.xml");

            ServletOutputStream sos = null;
            String file = null;
            byte[] bytes = new byte[1];
            InputStream is = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int length = 0;

            sos = response.getOutputStream();
            response.setContentType("text/xml");

            file = UPLOAD_ROOT_DIR + "/terminologyTemp.xml";
            response.setHeader("Content-Disposition", "attachment; filename=\"terminology.xml \"");
            is = new DataInputStream(new FileInputStream(file));
            length = Integer.parseInt(String.valueOf(xmlFile.length()));

            response.setContentLength(length);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sos);

            bytes = new byte[length];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {
                bos.write(bytes, 0, bytesRead);
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            sos.flush();
            sos.close();

        }
        catch (Exception e) {
            logger.error("Failed to import TBX file", e);
            logger.error(e,e);
        }
    }

    /**
     * to create a zip file for multiple file downloads.
     *
     * @param fileNameList
     */
    private void createZipFile(List<String> fileNameList) {
        if (Utils.isNull(fileNameList) || fileNameList.size() == 0)
            return;

        String zipFile = UPLOAD_ROOT_DIR + "/reports.zip";
        String[] sourceFiles = new String[fileNameList.size()];
        int j = 0;
        for (String fileName : fileNameList) {
            fileName = UPLOAD_ROOT_DIR + "/" + fileName + ".csv";
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

                zout.putNextEntry(new ZipEntry(fileNameList.get(i) + ".csv"));

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
            logger.error(" Failed to create zip file", e);
            logger.error(e,e);
        }
    }


    /**
     * to create a zip file for multiple file downloads.
     *
     * @param fileNameList
     */
    private void createXliffZipFile(List<String> fileNameList) {
        String zipFile = UPLOAD_ROOT_DIR + "/reports.zip";
        String[] sourceFiles = new String[fileNameList.size()];
        int j = 0;
        for (String fileName : fileNameList) {
            fileName = UPLOAD_ROOT_DIR + "/" + fileName;
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
            logger.error(" Failed to create zip file", e);
            logger.error(e,e);
        }
    }

    /**
     * Used to export xliff
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    private void exportXliff(HttpServletRequest request,
                             HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {


            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            List<GlobalsightTermInfo> globalSightTermInfoList = null;
            String pageId = request.getParameter("pageId");
            String pageIds[] = null;
            String exportFileName = null;
            File xlfFile = null;
            if (pageId.contains(",")) {
                pageIds = pageId.split(",");
            } else if (!pageId.contains(",")) {
                pageIds = new String[1];
                pageIds[0] = pageId;
            }
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }


            List<String> fileNameList = new ArrayList<String>();
            for (int i = 0; i < pageIds.length; i++) {


                ExecutorService executorObj = ExportGlobalSightExecutor.INSTANCE.get();
                Runnable thread = new ExportGlobalSightThread(pageIds[i], companyId);
                executorObj.execute(thread);
            }

            if (fileNameList != null && !fileNameList.isEmpty()) {
                if (fileNameList.size() == 1) {
                    xlfFile = new File(UPLOAD_ROOT_DIR + "/" + exportFileName);
                    fileNameList = null;
                } else {
                    createXliffZipFile(fileNameList);
                }
            }


            if (xlfFile != null || (fileNameList != null && !fileNameList.isEmpty())) {

                ServletOutputStream sos = null;
                String file = UPLOAD_ROOT_DIR + "/" + exportFileName;
                byte[] bytes = new byte[1];
                InputStream is = null;
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                int length = 0;
                String contentType = "text/xlf";
                if (fileNameList != null && !fileNameList.isEmpty()) {
                    contentType = "application/zip";
                    file = UPLOAD_ROOT_DIR + "/reports.zip";
                    exportFileName = "reports.zip";
                    xlfFile = new File(file);
                }

                sos = response.getOutputStream();
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + exportFileName + "\"");
                is = new DataInputStream(new FileInputStream(file));
                length = Integer.parseInt(String.valueOf(xlfFile.length()));

                response.setContentLength(length);
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(sos);

                bytes = new byte[length];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                    bos.write(bytes, 0, bytesRead);
                }
                ServletOutputStream out = response.getOutputStream();
                out.write(bytes);
                sos.flush();

            }
            session.setAttribute(SessionKey.DOWNLOAD_STATUS.getKey(), 1);


        }
        catch (Exception e) {
            session.setAttribute(SessionKey.DOWNLOAD_STATUS.getKey(), 1);
            logger.error("Failed to upload xlf file");
            logger.error(e,e);
        }
    }


    /**
     * Used to import TMX files.
     *
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     * @param fileId       String holding import tmx file id
     * @param uploadedFile Uploaded file name
     * @throws IOException
     */
    private void importTMX(HttpServletRequest request,
                           HttpServletResponse response, File uploadedFile, String fileId) throws IOException {
        CSVImportStatus failedStatus = new CSVImportStatus();
        FileUploadStatus fileUploadData = new FileUploadStatus();
        HttpSession session = request.getSession();
        try {
            CSVImportStatus status = new CSVImportStatus();
            status.setTermInformationStatus(UploadStatusMessages.SUCCESS.getValue());
            session.setAttribute(SessionKey.UPLOAD_STATUS.getKey(), status);
            String filepath = uploadedFile.getAbsolutePath();
            fileUploadData = TeaminologyService.getFileUploadStatus(fileId);
            fileUploadData.setFileStatus(TeaminologyEnum.TEAMINOLOGY_FILE_IMPORT.getValue());
            fileUploadData.setFileUrl(filepath);
            TeaminologyService.updateFileUploadStatus(fileUploadData);
            List<Language> languageList = TeaminologyService.getLanguages();
            List<ProductGroup> productGroupList = TeaminologyService.getproductGroupLookup();
            List<Domain> domainList = TeaminologyService.getDomainLookup();
            List<ContentType> contentTypeList = TeaminologyService.getContentTypeLookup();
            List<Company> companyList = TeaminologyService.getCompanyLookUp();
            ExecutorService executorObj = ImportTMXExecutor.INSTANCE.get();
            Runnable thread = new ImportTMXThread(new Integer(fileId), languageList, productGroupList, domainList, contentTypeList, companyList);
            executorObj.execute(thread);

        }
        catch (Exception e) {
            CSVImportStatus status = new CSVImportStatus();
            status.setTermInformationStatus(UploadStatusMessages.SUCCESS.getValue());
            session.setAttribute(SessionKey.UPLOAD_STATUS.getKey(), status);
            BufferedWriter bw = null;
            try {
                File logFileDir = new File(UPLOAD_ROOT_DIR);
                if (!logFileDir.exists()) {
                    logFileDir.mkdir();
                }
                String fileName = "teaminologyImport" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSS").format(new Date()) + ".log";
                fileUploadData.setFileLogUrl(UPLOAD_ROOT_DIR + "/" + fileName);
                fileUploadData.setFileStatus("Invalid file");
                fileUploadData.setProccesedPercentage(100);
                String logFile = UPLOAD_ROOT_DIR + "/" + fileName;
                bw = new BufferedWriter(new FileWriter(logFile));
                String errorMsg = "Invalid file";
                bw.write(errorMsg);
                bw.newLine();
                TeaminologyService.updateFileUploadStatus(fileUploadData);
            }
            catch (Exception exception) {
               logger.error(exception,exception);
            }
            finally {
                if (bw != null)
                    try {
                        bw.close();
                    }
                    catch (IOException e1) {
                    	logger.error(e,e);
                        logger.error("Failed to import TMX file");
                    }
            }
        }
    }


    /**
     * Used to export TBX.
     *
     * @param request               HttpServletRequest
     * @param response              HttpServletResponse
     * @param uploadedFile          File holding the uploaded file name
     * @param termInformationToList Data holding the Terms data to export
     */
    private void exportTMX(HttpServletRequest request,
                           HttpServletResponse response, File uploadedFile, List<TmProfileInfo> tmProfileInfoList) {
        try {

            File xmlFile = null;
            xmlFile = TeaminologyService.writeTMX(tmProfileInfoList, UPLOAD_ROOT_DIR + "/terminologyTemp.tmx");


            ServletOutputStream sos = null;
            String file = null;
            byte[] bytes = new byte[1];
            InputStream is = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int length = 0;

            sos = response.getOutputStream();
            response.setContentType("text/xml");

            file = UPLOAD_ROOT_DIR + "/terminologyTemp.tmx";
            System.out.println("file path" + file);
            response.setHeader("Content-Disposition", "attachment; filename=\"terminology.tmx \"");
            System.out.println("File:::::::::" + file);
            is = new DataInputStream(new FileInputStream(file));
            length = Integer.parseInt(String.valueOf(xmlFile.length()));

            response.setContentLength(length);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sos);

            bytes = new byte[length];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                bos.write(bytes, 0, bytesRead);
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            sos.flush();
            sos.close();

        }
        catch (Exception e) {
            logger.error("Failed to import TBX file");
            logger.error(e,e);
        }
    }

    /**
     * Used to download tmx file
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param fileId     String holding download tmx file id
     * @param exportType String holding the type of data to export
     */
    private void downloadTMXFile(HttpServletRequest request,
                                 HttpServletResponse response, String exportType, String fileId) {
        try {

            FileUploadStatus fileUpload = new FileUploadStatus();
            fileUpload = TeaminologyService.getFileUploadStatus(fileId);
            File xmlFile = null;
            xmlFile = new File(fileUpload.getFileUrl());


            ServletOutputStream sos = null;
            String file = null;
            byte[] bytes = new byte[1];
            InputStream is = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int length = 0;

            sos = response.getOutputStream();
            response.setContentType("text/xml");

            file = fileUpload.getFileUrl();
            String fileName = fileUpload.getFileUrl();
            if (fileName != null && fileName.lastIndexOf("/") > 0) {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            }
            System.out.println("file path  >>>" + fileName);
            System.out.println("file path" + file);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            System.out.println("File:::::::::" + file);
            is = new DataInputStream(new FileInputStream(file));
            length = Integer.parseInt(String.valueOf(xmlFile.length()));

            response.setContentLength(length);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sos);

            bytes = new byte[length];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                bos.write(bytes, 0, bytesRead);
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            sos.flush();
            sos.close();

        }
        catch (Exception e) {
            logger.error("Failed to import TBX file");
            logger.error(e,e);
        }

    }

    /**
     * Used to download log file
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param fileId     String holding download log file id
     * @param exportType String holding the type of data to export
     */

    private void downloadLogFile(HttpServletRequest request,
                                 HttpServletResponse response, String exportType, String fileId) {
        try {

            FileUploadStatus fileUpload = new FileUploadStatus();
            fileUpload = TeaminologyService.getFileUploadStatus(fileId);
            File xmlFile = null;
            xmlFile = new File(fileUpload.getFileLogUrl());


            ServletOutputStream sos = null;
            String file = null;
            byte[] bytes = new byte[1];
            InputStream is = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int length = 0;

            sos = response.getOutputStream();
            response.setContentType("text");

            file = fileUpload.getFileLogUrl();
            String fileName = fileUpload.getFileLogUrl();
            if (fileName != null && fileName.lastIndexOf("/") > 0) {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            }
            System.out.println("file path  >>>" + fileName);
            System.out.println("file path" + file);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            System.out.println("File:::::::::" + file);
            is = new DataInputStream(new FileInputStream(file));
            length = Integer.parseInt(String.valueOf(xmlFile.length()));

            response.setContentLength(length);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sos);

            bytes = new byte[length];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                bos.write(bytes, 0, bytesRead);
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            sos.flush();
            sos.close();

        }
        catch (Exception e) {
            logger.error("Failed to import TBX file");
            logger.error(e,e);
        }

    }

    /**
     * Used to download gs log file
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param exportType String holding the type of data to export
     */
    private void downloadGSLogFile(HttpServletRequest request,
                                   HttpServletResponse response, String exportType) {
        try {

            FileInfo fileInfoData = new FileInfo();
            String taskId = request.getParameter("taskId");
            String isImport = request.getParameter("isImport");

            fileInfoData = TeaminologyService.getFileInfoByTaskId(Integer.parseInt(taskId));
            File xmlFile = null;
            if (isImport != null && isImport.equalsIgnoreCase("Y")) {
                xmlFile = new File(fileInfoData.getLogFileUrl());
            } else {
                xmlFile = new File(fileInfoData.getExportLog());
            }


            ServletOutputStream sos = null;
            String file = null;
            byte[] bytes = new byte[1];
            InputStream is = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int length = 0;

            sos = response.getOutputStream();
            response.setContentType("text");
            if (isImport != null && isImport.equalsIgnoreCase("Y")) {
                file = fileInfoData.getLogFileUrl();
            } else {
                file = fileInfoData.getExportLog();
            }

            String fileName = null;
            if (isImport != null && isImport.equalsIgnoreCase("Y")) {
                fileName = fileInfoData.getLogFileUrl();
            } else {
                fileName = fileInfoData.getExportLog();
            }
            if (fileName != null && fileName.lastIndexOf("/") > 0) {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            is = new DataInputStream(new FileInputStream(file));
            length = Integer.parseInt(String.valueOf(xmlFile.length()));

            response.setContentLength(length);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sos);

            bytes = new byte[length];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                bos.write(bytes, 0, bytesRead);
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            sos.flush();
            sos.close();

        }
        catch (Exception e) {
            logger.error("Failed to import TBX file");
            logger.error(e,e);
        }

    }

    /**
     * Used to download template file
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    private void downloadTemplateFile(HttpServletRequest request,
                                      HttpServletResponse response) {
        try {


            String importCsvTemplate = TeaminologyProperty.CSV_SAMPLE_TEMPLATE.getValue();
            File templateFile = new File(importCsvTemplate);
            ServletOutputStream sos = null;
            String file = null;
            byte[] bytes = new byte[1];
            InputStream is = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int length = 0;

            sos = response.getOutputStream();
            response.setContentType("text/csv");

            String fileName = importCsvTemplate;
            if (fileName != null && fileName.lastIndexOf("/") > 0) {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            }
            
            logger.info("file path  >>>" + fileName);
            logger.info("file path" + importCsvTemplate);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            logger.info("File:::::::::" + importCsvTemplate);
            is = new DataInputStream(new FileInputStream(importCsvTemplate));
            length = Integer.parseInt(String.valueOf(templateFile.length()));

            response.setContentLength(length);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sos);

            bytes = new byte[length];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {

                bos.write(bytes, 0, bytesRead);
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            sos.flush();
            sos.close();

        }
        catch (Exception e) {
            logger.error("Failed to load Template file");
            logger.error(e,e);
        }

    }

    /**
     * Used to download Tbx sample template
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */

    private void downloadTBXTmpltFile(HttpServletRequest request,
                                      HttpServletResponse response) {

        try {
            String tbxSampleTemplate = TeaminologyProperty.TBX_SAMPLE_TEMPLATE.getValue();
            File templateFile = new File(tbxSampleTemplate);
            ServletOutputStream sos = null;
            String file = null;
            byte[] bytes = new byte[1];
            InputStream is = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int length = 0;

            sos = response.getOutputStream();
            response.setContentType("text/doc");

            String fileName = tbxSampleTemplate;
            if (fileName != null && fileName.lastIndexOf("/") > 0) {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            is = new DataInputStream(new FileInputStream(tbxSampleTemplate));
            length = Integer.parseInt(String.valueOf(templateFile.length()));

            response.setContentLength(length);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sos);

            bytes = new byte[length];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {
                bos.write(bytes, 0, bytesRead);
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            sos.flush();
            sos.close();
        }
        catch (Exception e) {
            logger.error("Failed to load Template file", e);
            logger.error(e,e);
        }
    }


/**
 * Used to download Tbx sample template
 *
 * @param request  HttpServletRequest
 * @param response HttpServletResponse
 */

private void downloadTABTmpltFile(HttpServletRequest request,
                                  HttpServletResponse response) {

    try {
        String tbxSampleTemplate = TeaminologyProperty.TAB_SAMPLE_TEMPLATE.getValue();
        File templateFile = new File(tbxSampleTemplate);
        ServletOutputStream sos = null;
        String file = null;
        byte[] bytes = new byte[1];
        InputStream is = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        int length = 0;

        sos = response.getOutputStream();
        response.setContentType("text/pain");

        String fileName = tbxSampleTemplate;
        if (fileName != null && fileName.lastIndexOf("/") > 0) {
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        is = new DataInputStream(new FileInputStream(tbxSampleTemplate));
        length = Integer.parseInt(String.valueOf(templateFile.length()));

        response.setContentLength(length);
        bis = new BufferedInputStream(is);
        bos = new BufferedOutputStream(sos);

        bytes = new byte[length];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {
            bos.write(bytes, 0, bytesRead);
        }
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        sos.flush();
        sos.close();
    }
    catch (Exception e) {
        logger.error("Failed to load Template file", e);
        logger.error(e,e);
    }
}
}

	 