	package com.teaminology.hp.service.impl;
	
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
	import java.io.OutputStream;
	import java.io.OutputStreamWriter;
	import java.io.Reader;
	import java.io.StringReader;
	import java.io.StringWriter;
	import java.text.DecimalFormat;
	import java.text.SimpleDateFormat;
	import java.util.ArrayList;
	import java.util.Collection;
	import java.util.Collections;
	import java.util.Comparator;
	import java.util.Date;
	import java.util.HashMap;
	import java.util.HashSet;
	import java.util.List;
	import java.util.Map;
	import java.util.Set;
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
	
	import com.teaminology.hp.service.enums.*;
	import jxl.Cell;
	import jxl.Sheet;
	import jxl.Workbook;
	import jxl.WorkbookSettings;
	import jxl.format.PageOrientation;
	import jxl.write.Label;
	import jxl.write.NumberFormat;
	import jxl.write.WritableCellFormat;
	import jxl.write.WritableFont;
	import jxl.write.WritableFont.FontName;
	import jxl.write.WritableSheet;
	import jxl.write.WritableWorkbook;
	import jxl.write.WriteException;
	import jxl.write.biff.RowsExceededException;
	import org.apache.log4j.Logger;
	import org.apache.solr.client.solrj.impl.HttpSolrServer;
	import org.apache.solr.common.SolrInputDocument;
	import org.codehaus.plexus.util.xml.XmlUtil;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;
	import org.w3c.dom.Attr;
	import org.w3c.dom.Document;
	import org.w3c.dom.Element;
	import org.w3c.dom.NamedNodeMap;
	import org.w3c.dom.Node;
	import org.w3c.dom.NodeList;
	import org.xml.sax.InputSource;
	import com.Ostermiller.util.ExcelCSVParser;
	import com.sun.syndication.io.XmlReader;
	import com.teaminology.hp.Utils;
	import com.teaminology.hp.bo.Attributes;
	import com.teaminology.hp.bo.CompanyTransMgmt;
	import com.teaminology.hp.bo.DeprecatedTermInformation;
	import com.teaminology.hp.bo.GlobalsightTermInfo;
	import com.teaminology.hp.bo.Role;
	import com.teaminology.hp.bo.TMProperties;
	import com.teaminology.hp.bo.Tag;
	import com.teaminology.hp.bo.TermInformation;
	import com.teaminology.hp.bo.TermVoteMaster;
	import com.teaminology.hp.bo.TmProfileInfo;
	import com.teaminology.hp.bo.User;
	import com.teaminology.hp.bo.UserLanguages;
	import com.teaminology.hp.bo.UserRole;
	import com.teaminology.hp.bo.lookup.Category;
	import com.teaminology.hp.bo.lookup.Company;
	import com.teaminology.hp.bo.lookup.ConceptCategory;
	import com.teaminology.hp.bo.lookup.ContentType;
	import com.teaminology.hp.bo.lookup.Domain;
	import com.teaminology.hp.bo.lookup.Form;
	import com.teaminology.hp.bo.lookup.Language;
	import com.teaminology.hp.bo.lookup.PartsOfSpeech;
	import com.teaminology.hp.bo.lookup.ProductGroup;
	import com.teaminology.hp.bo.lookup.Program;
	import com.teaminology.hp.bo.lookup.Status;
	import com.teaminology.hp.dao.ILookUpDAO;
	import com.teaminology.hp.dao.IUserDAO;
	import com.teaminology.hp.dao.TermDetailsDAO;
	import com.teaminology.hp.data.CSVImportStatus;
	import com.teaminology.hp.data.DepractedObject;
	import com.teaminology.hp.data.LanguagePiChartData;
	import com.teaminology.hp.data.LanguageReportData;
	import com.teaminology.hp.data.LanguageReportTable;
	import com.teaminology.hp.data.Member;
	import com.teaminology.hp.data.TeaminologyObject;
	import com.teaminology.hp.data.TermInformationTo;
	import com.teaminology.hp.data.TermVoteDetails;
	import com.teaminology.hp.data.TermVotingTo;
	import com.teaminology.hp.data.Terms;
	import com.teaminology.hp.service.IImportExportService;
	import com.teaminology.hp.service.IUserService;
	import com.teaminology.hp.service.enums.DBColumnsSizeEnum;
	import com.teaminology.hp.service.enums.ExecuteStatusEnum;
	import com.teaminology.hp.service.enums.GlobalsightEnum;
	import com.teaminology.hp.service.enums.LanguageDataReportEnum;
	import com.teaminology.hp.service.enums.LanguagePiChartDataEnum;
	import com.teaminology.hp.service.enums.LanguageReportDataEnum;
	import com.teaminology.hp.service.enums.SuffixEnum;
	import com.teaminology.hp.service.enums.TeaminologyEnum;
	import com.teaminology.hp.service.enums.TeaminologyImportEnum;
	import com.teaminology.hp.service.enums.TeaminologyProperty;
	import com.teaminology.hp.service.enums.TermEnum;
	import com.teaminology.hp.service.enums.TermInformationEnum;
	import com.teaminology.hp.service.enums.UserDataEnum;
import com.teaminology.solrInstance.HttpSolrServerInstance;
	
	/**
	 * Allows you to import and export csv  and tbx files.
	 *
	 * @author sarvani
	 */
	@Service
	public class ImportExportService implements IImportExportService
	{
		private static Logger logger = Logger.getLogger(ImportExportService.class);
	
		HttpSolrServer server =HttpSolrServerInstance.INSTANCE.getServer();
	
		private static final PageOrientation LANDSCAPE_ORIENTATION = PageOrientation.LANDSCAPE;
		private static final String DEFAULT_FONT_NAME = "Calibri";
		private static final Integer DEFAULT_FONT_SIZE = 10;
		private static Set<String> sourceTermLangSet = null;
		private int startRow = 1;
		private int startCol = 0;
		private static final String DOLLAR_EXCEL_FORMAT = "_(\"$\"* #,##0_);_(\"$\"* (#,##0);_(\"$\"* \"-\"??_);_(@_)";
		private static final java.text.NumberFormat NUMBER_DEC_ONE = new DecimalFormat("#,##0");
		private static String dcdSource1 = null;
		private static String dcdSource2 = null;
		private static String dcdSource3 = null;
		private static String dcdTarget1 = null;
		private static String dcdTarget2 = null;
		private static String dcdTarget3 = null;
	
	
		private IUserDAO userDAO;
		private ILookUpDAO lookUpDAO;
		private TermDetailsDAO termDAO;
		private IUserService userService;
	
		@Autowired
		public ImportExportService(IUserDAO userDAO, ILookUpDAO lookUpDAO, TermDetailsDAO termDAO, IUserService userService) {
			this.userDAO = userDAO;
			this.lookUpDAO = lookUpDAO;
			this.termDAO = termDAO;
			this.userService = userService;
		}
	
		/**
		 * To  import csv
		 *
		 * @param uploadFile  File to be read
		 * @param lookupClass Object to be read
		 * @param companyIds  String to be read
		 * @return CSVImportStatus object
		 */
	
		@Override
		@Transactional
		public <C extends TeaminologyObject> CSVImportStatus readCSV(File uploadFile, Class<C> lookupClass, User userObj, String companyIds) {
	        CSVImportStatus importStatus = new CSVImportStatus();
	        importStatus.setTermInformationStatus(ExecuteStatusEnum.FAILED.getValue());

	        if (uploadFile == null || userObj == null)
	            return importStatus;

	        List<String[]> rows = parseExcelCSV(uploadFile.getAbsolutePath());
	        if (rows == null || rows.size() == 0)
	            return importStatus;

	        List<String> userExistsList = new ArrayList<String>();
	        List<Integer> userExistsLineNumber = new ArrayList<Integer>();
	        List<Company> companyList = new ArrayList<Company>();
	        List<TermInformation> insertedTermInformationList = new ArrayList<TermInformation>();
	        List<TermInformation> updatedTermInformationList = new ArrayList<TermInformation>();
	        List<String> rejectedTermList = new ArrayList<String>();
	        List<Integer> termErrorLineNumber = new ArrayList<Integer>();

	        List<String> termInfoLableList = getTermInfoLablesList();

	        Integer insertedCount = 0;
	        Integer rejectedCount = 0;
	        Integer updatedCount = 0;

	        String sourceTerm = null;
	        String domainValue=null;
	        String catValue=null;
	        String posValue=null;
	        String sourceVal=null;
	        Domain domain=null;
	        Category category=null;
	        PartsOfSpeech partsOfSpeech=null;
	        String termSavedInformation = null;
	        String selectedIdsArray[] = new String[1];
	        Company companyObj=null;
	        Company internalCompanyObj = lookUpDAO.getCompanyByLable(TeaminologyProperty.INTERNAL_COMPANY.getValue());
	        Role superAdminRol = userDAO.getUserTypeByRole("Super Translator");
	        if (companyIds != null && companyIds.length() > 0) {
	            if (companyIds.contains(",")) {
	                selectedIdsArray = companyIds.split(",");
	            } else {
	                selectedIdsArray[0] = companyIds;
	            }
	        }

	        List<Company> allCompanies = lookUpDAO.getCompanyLookUp();
	        Map<Object, Company> allCompaniesMap = lookUpDAO.getCompanyLookUpMap();
	        Map<Object, Role> allRoles = lookUpDAO.getRolesLookUpMap();
	        List<Language> allLanguages = lookUpDAO.getLanguages();
	        Map<Object, PartsOfSpeech> allPartsOfSpeech = lookUpDAO.getPartsOfSpeechLookUpMap();
	        Map<Object, Domain> allDomains = lookUpDAO.getDomainLookUpMap();
	        Map<Object, Category> allCategories = lookUpDAO.getCategoryLookUpMap();
	        Map<Object, Form> allForms = lookUpDAO.getFormLookUpMap();
	        Map<Object, Program> programs=lookUpDAO.getProramLookupMap();
	        Map<Object,Status> statuses=lookUpDAO.getStatusLookUpMap();
	        
	        //Map<Object, Status> allStatuses = lookUpDAO.getStatusLookupMap();
	        try {
	            for (int i = 0; i < selectedIdsArray.length; i++) {
	                if (selectedIdsArray[i] != null && !selectedIdsArray[i].isEmpty()) {
	                    for (Company company : allCompanies) {
	                        if (company.getCompanyId().intValue() == Integer.parseInt(selectedIdsArray[i])) {
	                            companyList.add(company);
	                        }
	                    }
	                }
	            }
	            if (rows != null && !rows.isEmpty()) {
	                int totalRows = rows.size();
	                logger.info("tota records" + totalRows);
	                if (totalRows <= 1) {
	                    importStatus.setTermInformationStatus("failed");
	                    return importStatus;
	                } else {
	                    int rowCount = 0;
	                    boolean flag = true;
	                    int newIndex[] = null;
	                    int noOfLangs = 0;
	                    int index = 0;
	                    String[] langNames = null;
	                    String[] targetTerms = null;
	                    boolean sugExists =true;
	                    int sugg=0;
	                    String targets=null;
	                    for (String[] row : rows) {
	                        try {
	                            Boolean isValidData = true;
	                           
	                            User user = new User();
	                            TermInformation termInformation = new TermInformation();
	                            int columnNdx = 0;
	                            //use reflection
	                            int regularcolumCount = TeaminologyEnum.TERM_COL_COUNT.getIntValue().intValue();
	                            rowCount++;
	                            if (flag) {
	                                newIndex = new int[row.length];
	                                if ((lookupClass.newInstance() instanceof TermInformation) && row.length > regularcolumCount) {
	                                    langNames = new String[row.length];
	                                    targetTerms = new String[row.length];
	                                }
	                                for (columnNdx = 0; columnNdx < row.length; columnNdx++) {
	                                	String headerValue=row[columnNdx];
	                                	 if( headerValue.contains("Concept Definition")){
	                                     	sugg=columnNdx;
	                                     }
	                                    if(headerValue.contains("Source Term") || headerValue.contains("Concept Definition") || headerValue.contains("Term Category")
	                                    		|| headerValue.contains("Term Part Of Speech") || headerValue.contains("Term Form") || headerValue.contains("Term Status")
	                                    		||  headerValue.contains("Comments") || headerValue.contains("Company") || headerValue.contains("Domain") || headerValue.contains("Term Usage")
	                                    		|| headerValue.contains("Term Notes") ) {
	                                        newIndex[columnNdx] = TeaminologyImportEnum.getEnumColumnIndexByName(row[columnNdx]);
	                                        if (lookupClass.newInstance() instanceof TermInformation) {
	                                            if (newIndex[columnNdx] > 0) {
	                                                TeaminologyImportEnum enumValue = TeaminologyImportEnum.getEnumColumnById(newIndex[columnNdx]);
	                                                if (enumValue != null) {
	                                                    String indexColumn = enumValue.getStringValue();
	                                                    if (indexColumn != null) {
	                                                        indexColumn = indexColumn.toLowerCase().trim();
	                                                        termInfoLableList.remove(indexColumn);
	                                                    }
	                                                }
	                                            }
	                                        }
	                                    } 
	                                    
	                                    else{
	                                         if(columnNdx == 1){
	                                    	 newIndex[columnNdx] = TeaminologyImportEnum.getEnumColumnIndexByName("Suggested Term");
	                                    	 if (lookupClass.newInstance() instanceof TermInformation) {
	                                             if (newIndex[columnNdx] > 0) {
	                                                 TeaminologyImportEnum enumValue = TeaminologyImportEnum.getEnumColumnById(newIndex[columnNdx]);
	                                                 if (enumValue != null) {
	                                                     String indexColumn = enumValue.getStringValue();
	                                                     if (indexColumn != null) {
	                                                         indexColumn = indexColumn.toLowerCase().trim();
	                                                         termInfoLableList.remove(indexColumn);
	                                                     }
	                                                 }
	                                             }

	                                         }
	                                        }
	                                                                              
	                                        newIndex[columnNdx] = TeaminologyImportEnum.getEnumColumnIndexByName(row[columnNdx]);
	                                        if(langNames != null){
	                                        langNames[noOfLangs] = row[columnNdx]; 
	                                        noOfLangs++;
	                                        }
	                                }
	                                
	                                }
	                                
	                                flag = false;
	                            } else if (!flag) {
	                                if (lookupClass.newInstance() instanceof TermInformation) {
	                                    if (termInfoLableList != null && termInfoLableList.size() > 0) {
	                                        String msg = "";
	                                        for (String lable : termInfoLableList) {
	                                            msg = msg + lable + ",";
	                                        }
	                                        if (msg.contains(",")) {
	                                            msg = msg.substring(0, msg.length() - 1);
	                                        }
	                                        importStatus.setMissedColumns(msg);
	                                        importStatus.setTermInformationStatus("failed");
	                                        break;
	                                    } else {
	                                        if (noOfLangs == 0) {
	                                            importStatus.setMissedColumns("Target Language");
	                                            importStatus.setTermInformationStatus("failed");
	                                            break;
	                                        }
	                                    }

	                                }
	                                for(int k=0; k < newIndex.length;k++){
	                                	if(newIndex[k] == -1){
	                                		newIndex[k]=0;
	                                	}
	                                }
	                                if (lookupClass.newInstance() instanceof TermInformation) {
	                                if(sugExists)
	                                	newIndex[1]=27;
	                                }
	                                for (int i : newIndex) {
	                                    if (row.length > columnNdx) {
	                                        String columnText = row[columnNdx];
	                                        if (columnText != null) {
	                                            columnText = removeNewLineCharacter(columnText);
	                                            columnText = columnText.trim();
	                                        }
	                                        
	                                        TeaminologyImportEnum column = TeaminologyImportEnum.getEnumColumnById(i);
	                                        if (lookupClass.newInstance() instanceof TermInformation) {
	                                            if (column.getStringValue().equalsIgnoreCase("Domain")) {
	                                            	if(!(columnText .isEmpty()))
	                                               	    domainValue=columnText;
	                                            }
	                                            if (column.getStringValue().equalsIgnoreCase("Term Part Of Speech")) {
	                                            	if(!(columnText .isEmpty()))
	                                               	    posValue=columnText;
	                                            }
	                                            if (column.getStringValue().equalsIgnoreCase("Term Category")) {
	                                            	if(!(columnText .isEmpty()))
	                                            		catValue=columnText;
	                                            }
	                                            if (column != null && columnText != null) {
	                                                if (column.getStringValue().equalsIgnoreCase(TeaminologyImportEnum.CONCEPT_DEFINITION.getStringValue())) {
	                                                    if (columnText.length() > DBColumnsSizeEnum.CONCEPT_DEFINATION_MAX_SIZE.getIntValue()) {
	                                                        isValidData = false;

	                                                        rejectedTermList.add("Data is too long for the column  ' " + column.getStringValue() + "'. Maximum limit is " + DBColumnsSizeEnum.CONCEPT_DEFINATION_MAX_SIZE.getIntValue());
	                                                        termErrorLineNumber.add(rowCount);
	                                                    }
	                                                }
	                                                if (column.getStringValue().equalsIgnoreCase(TeaminologyImportEnum.TERM_NOTES.getStringValue())) {
	                                                    if (columnText.length() > DBColumnsSizeEnum.TERM_NOTES_MAX_SIZE.getIntValue()) {
	                                                        isValidData = false;

	                                                        rejectedTermList.add("Data is too long for the column  ' " + column.getStringValue() + "'. Maximum limit is " + DBColumnsSizeEnum.TERM_NOTES_MAX_SIZE.getIntValue());
	                                                        termErrorLineNumber.add(rowCount);


	                                                    }
	                                                }
	                                                if (column.getStringValue().equalsIgnoreCase(TeaminologyImportEnum.CONTEXTUAL_EXAMPLE.getStringValue())) {
	                                                    if (columnText.length() > DBColumnsSizeEnum.CONTEXTUAL_EXAMPLE_MAX_SIZE.getIntValue()) {
	                                                        isValidData = false;

	                                                        rejectedTermList.add("Data is too long for the column  ' " + column.getStringValue() + "'. Maximum limit is " + DBColumnsSizeEnum.CONTEXTUAL_EXAMPLE_MAX_SIZE.getIntValue());
	                                                        termErrorLineNumber.add(rowCount);


	                                                    }
	                                                }
	                                                if (column.getStringValue().equalsIgnoreCase(TeaminologyImportEnum.SOURCE_TERM.getStringValue())) {
	                                                    if (columnText.length() > DBColumnsSizeEnum.SOURCE_TERM_MAX_SIZE.getIntValue()) {
	                                                        isValidData = false;

	                                                        rejectedTermList.add("Data is too long for the column  ' " + column.getStringValue() + "'. Maximum limit is " + DBColumnsSizeEnum.SOURCE_TERM_MAX_SIZE.getIntValue());
	                                                        termErrorLineNumber.add(rowCount);
	                                                    }
	                                                }
	                                            }
	                                        }
	                                        if(lookupClass.newInstance() instanceof TermInformation){
	                                        if (columnText != null) {
	                                          //  if (columnNdx >= regularcolumCount && lookupClass.newInstance() instanceof TermInformation) {
	                                        	if(i==27){
	                                        		targetTerms[index] = columnText;
	                                        		index++;
	                                        	}
	                                        	
	                                        	if(i == 0){
	                                                targetTerms[index] = columnText;
	                                                index++;
	                                            }
	                                        } else {
	                                            continue;
	                                        }
	                                        }
	                                        switch (column) {
	                                            case FIRST_NAME:
	                                                user.setFirstName(columnText);
	                                                break;
	                                            case LAST_NAME:
	                                                user.setLastName(columnText);
	                                                break;
	                                            case USER_NAME:
	                                                user.setUserName(columnText);
	                                                break;
	                                            case ROLE:
	                                                String[] userRoles = columnText.split(",");
	                                                Set<UserRole> userRoleSet = new HashSet<UserRole>();
	                                                for (int k = 0; k < userRoles.length; k++) {
	                                                    //Role role = userDAO.getUserTypeByRole(userRoles[k].trim());
	                                                    Role role = allRoles.get(userRoles[k].trim());
	                                                    if (role == null) {
	                                                        //role =  userDAO.getUserTypeByRole("Community Member");
	                                                        role = allRoles.get("Community Member");
	                                                    }
	                                                    UserRole urole = new UserRole();
	                                                    urole.setRole(role);
	                                                    userRoleSet.add(urole);
	                                                }
	                                                user.setUserRole(userRoleSet);
	                                                break;
	                                            case EMAIL:
	                                                user.setEmailId(columnText);
	                                                break;
	                                            case LANGUAGE:
	                                                columnText = columnText.replaceAll("-", "_");
	                                                String[] languageCodes = columnText.split(",");
	                                                Set<UserLanguages> userLanguagesSet = new HashSet<UserLanguages>();
	                                                for (int k = 0; k < languageCodes.length; k++) {
	                                                    Language languages = null;
	                                                    if (allLanguages != null && !allLanguages.isEmpty()) {
	                                                        for (Language lang : allLanguages) {
	                                                            if (languageCodes[k] != null && lang != null)
	                                                                if ((lang.getLanguageCode() != null && lang.getLanguageCode().equalsIgnoreCase(languageCodes[k].trim()))
	                                                                        || (lang.getLanguageLabel() != null && lang.getLanguageLabel().equalsIgnoreCase(languageCodes[k].trim()))) {
	                                                                    languages = lang;
	                                                                }
	                                                        }
	                                                    }
	                                                    if (languages == null) {
	                                                        if (allLanguages != null && !allLanguages.isEmpty())
	                                                            languages = allLanguages.get(0);
	                                                    }
	                                                    UserLanguages userlanguages = new UserLanguages();
	                                                    userlanguages.setLanguages(languages);
	                                                    userLanguagesSet.add(userlanguages);
	                                                }
	                                                user.setUserLanguages(userLanguagesSet);
	                                                break;
	                                            case PASSWORD:
	                                                String password = Utils.encryptPassword(columnText);
	                                                user.setPassword(password);
	                                                break;
	                                            case COMPANY:
	                                                //Company companyObj=lookUpDAO.getCompanyByLable(columnText);
	                                                companyObj = allCompaniesMap.get(columnText);
	                                                if (companyObj == null) {
	                                                    //	companyObj=lookUpDAO.getCompanyByLable(TeaminologyProperty.INTERNAL_COMPANY.getValue());
	                                                    //companyObj = allCompaniesMap.get(TeaminologyProperty.INTERNAL_COMPANY.getValue());
	                                                	companyObj=userObj.getCompany();
	                                                }
	                                                //companyList.add(companyObj);
	                                                user.setCompany(companyObj);
	                                                break;

	                                            case SOURCE_TERM:
	                                                sourceTerm = columnText;
	                                                Language languages = lookUpDAO.getLanguageByCode(TeaminologyEnum.ENGLISH_SOURCE.getValue(), true);
	                                                termInformation.setTermLangId(languages.getLanguageId());
	                                                if (columnText != null && columnText.trim().length() > 0)
	                                                    termInformation.setTermBeingPolled(columnText);
	                                                break;
	                                            case TERM_PART_OF_SPEECH:
	                                                try {
	                                                    partsOfSpeech = allPartsOfSpeech.get(columnText);
	                                                    if (partsOfSpeech == null) {
	                                                    	partsOfSpeech = allPartsOfSpeech.get(TeaminologyImportEnum.PART_OF_SPEECH.getStringValue());
	                                                    }
	                                                    termInformation.setTermPOS(partsOfSpeech);
	                                                }
	                                                catch (Exception ce) {
	                                                    logger.info(ce.getMessage());
	                                                    isValidData = false;

	                                                    if (ce.getMessage().equalsIgnoreCase("query did not return a unique result: 2")) {
	                                                        rejectedTermList.add("There are more than one parts of speech with the name  ' " + columnText + " please  active only one parts of speech.'");
	                                                    } else {
	                                                        rejectedTermList.add(ce.getMessage());
	                                                    }
	                                                    termErrorLineNumber.add(rowCount);

	                                                    break;

	                                                }
	                                                break;
	                                            case DOMAIN:
	                                                try {
	                                                    //Domain domain=lookUpDAO.getDomainByLable(columnText);
	                                                     domain = allDomains.get(columnText);
	                                                    if (domain != null) {
	                                                        termInformation.setTermDomain(domain);
	                                                    }
	                                                }
	                                                catch (Exception ce) {
	                                                    logger.info(ce.getMessage());
	                                                    isValidData = false;

	                                                    if (ce.getMessage().equalsIgnoreCase("query did not return a unique result: 2")) {
	                                                        rejectedTermList.add("There are more than one domains with the name  ' " + columnText + " please  active only one domain.'");
	                                                    } else {
	                                                        rejectedTermList.add(ce.getMessage());
	                                                    }
	                                                    termErrorLineNumber.add(rowCount);

	                                                    break;

	                                                }
	                                                break;
	                                            case TERM_CATEGORY:
	                                                try {
	                                                    //Category category=lookUpDAO.getCategoryByName(columnText);
	                                                     category = allCategories.get(columnText);
	                                                     if(category != null)
	                                                     termInformation.setTermCategory(category);
	                                                }
	                                                catch (Exception ce) {
	                                                    logger.info(ce.getMessage());
	                                                    isValidData = false;

	                                                    if (ce.getMessage().equalsIgnoreCase("query did not return a unique result: 2")) {
	                                                        rejectedTermList.add("There are more than one categories with the name  ' " + columnText + " please  active only one category.'");
	                                                    } else {
	                                                        rejectedTermList.add(ce.getMessage());
	                                                    }
	                                                    termErrorLineNumber.add(rowCount);

	                                                    break;

	                                                }
	                                                break;
	                                            case TERM_FORM:
	                                                try {
	                                                    //Form form = lookUpDAO.getFormByName(columnText);
	                                                    Form form = allForms.get(columnText);
	                                                    if (form == null) {
	                                                        form = null;
	                                                    }
	                                                    termInformation.setTermForm(form);
	                                                }
	                                                catch (Exception ce) {
	                                                    logger.info(ce.getMessage());
	                                                    isValidData = false;

	                                                    if (ce.getMessage().equalsIgnoreCase("query did not return a unique result: 2")) {
	                                                        rejectedTermList.add("There are more than one Forms with the name  ' " + columnText + " please  active only one form.'");
	                                                    } else {
	                                                        rejectedTermList.add(ce.getMessage());
	                                                    }
	                                                    termErrorLineNumber.add(rowCount);
	                                                    break;
	                                                }
	                                                break;
	                                            case TARGET_LANGUAGE:
	                                                break;
	                                            case TARGET_TERM:
	                                            	//Integer targetValue=3;
	                                                Language suggestedTermLang = lookUpDAO.getLanguageByLabel(langNames[0]); 
	                                                if (suggestedTermLang == null) {
	                                                    suggestedTermLang = lookUpDAO.getLanguage(1);
	                                                }
	                                                termInformation.setSuggestedTermLangId(suggestedTermLang.getLanguageId());
	                                                if (columnText != null && columnText.trim().length() > 0)
	                                                    termInformation.setSuggestedTerm(columnText);
	                                                break;
	                                            case CONTEXTUAL_EXAMPLE:
	                                                termInformation.setTermUsage(columnText);
	                                                break;
	                                            case CONCEPT_DEFINITION:
	                                                termInformation.setConceptDefinition(columnText);
	                                                break;
	                                            case TERM_NOTES:
	                                                termInformation.setTermNotes(columnText);
	                                                break;
	                                            case TERM_PROGRAM:
	                                            	try{
	                                            	Program program=programs.get(columnText);
	                                            	if(program != null)
	                                            	termInformation.setTermProgram(program);
	                                            	}
	                                            	catch(Exception ce){
	                                                    logger.info(ce.getMessage());
	                                                    isValidData = false;
	                                                    if (ce.getMessage().equalsIgnoreCase("query did not return a unique result: 2")) {
	                                                        rejectedTermList.add("There are more than one Forms with the name  ' " + columnText + " please  active only one form.'");
	                                                    } else {
	                                                        rejectedTermList.add(ce.getMessage());
	                                                    }
	                                                    termErrorLineNumber.add(rowCount);
	                                                    break;
	                                            	}
	                                            	break;
	                                            case  TERM_STATUS:
	                                            	Status status = statuses.get(columnText);
	                                            	if(status != null){
	                                            		termInformation.setTermStatusId(status.getStatusId() == null ? null : status.getStatusId());
	                                            	}
	                                            	else
	                                            	termInformation.setTermStatusId(1);
	                                            	break;
	                                            case CONCEPT_NOTES:
	                                            	termInformation.setConceptNotes(columnText);
	                                            	break;
	                                            case CONCEPT_CATEGORY:
	                                            	ConceptCategory conceptCategory = lookUpDAO.getConceptCategoryByLabel(columnText);
	                                            	if(conceptCategory != null)
	                                            		termInformation.setConceptCatId(conceptCategory.getConceptCatId());
	                                            	break;
	                                            case CONCEPT_PRODUCT_GROUP:
	                                            	ProductGroup productGroup=lookUpDAO.getProductGroupByLabel(columnText);
	                                            	if(productGroup != null)
	                                            		termInformation.setConceptProdGroup(productGroup.getProductId());
	                                            	break;
	                                            case COMMENTS:
	                                            	termInformation.setComments(columnText);
	                                            break;
	                                            case TARGET_TERM_USAGE:
	                                            	termInformation.setSuggestedTermUsage(columnText);
	                                            	break;
	                                            case TARGET_TERM_PROGRAM:
	                                            	Program program=programs.get(columnText);
	                                            	if(program != null)
	                                            	termInformation.setSuggestedTermPgmId(program.getProgramId());
	                                            	break;
	                                            case TARGET_TERM_NOTES:
	                                            	termInformation.setSuggestedTermNotes(columnText);
	                                            	break;
	                                            default: {
	                                                break;
	                                            }
	                                        }
	                                        columnNdx++;
	                                    }
	                                }

	                            }
	                            if (rowCount > 1) {
	                                if ((lookupClass.newInstance() instanceof User) && (user != null && user.getUserName() != null)) {
	                                    if ((user.getUserName().length() > 0) && (user.getEmailId() != null && user.getEmailId().length() > 0)) {
	                                        boolean userExistsFlag = false;
	                                        boolean emailIdExists = false;
	                                        boolean isSuperTranslator = false;
	                                        userExistsFlag = userDAO.ifUserExists(user.getUserName());
	                                        emailIdExists = userDAO.ifEmailExists(user.getEmailId());
	                                        Set<UserRole> userRoleSet = user.getUserRole();
	                                        for (UserRole userRoleObj : userRoleSet) {
	                                            if (userRoleObj.getRole() != null && superAdminRol != null && userRoleObj.getRole().getRoleId().intValue() == superAdminRol.getRoleId().intValue()) {
	                                                user.setCompany(internalCompanyObj);
	                                            }
	                                        }
	                                        if (userObj != null) {
	                                            if (userObj.getCompany() != null) {
	                                                user.setCompany(userObj.getCompany());
	                                            }
	                                            if (userObj.getCompany() != null) {
	                                                if (userRoleSet != null && userRoleSet.size() > 0) {
	                                                    for (UserRole uRole : userRoleSet) {
	                                                        if (uRole.getRole() != null && superAdminRol != null && uRole.getRole().getRoleId().intValue() == superAdminRol.getRoleId().intValue()) {
	                                                            isSuperTranslator = true;
	                                                        }
	                                                    }
	                                                }
	                                            }
	                                        }

	                                        if (!userExistsFlag && !emailIdExists && !isSuperTranslator) {
	                                            user.setCreatedBy(userObj.getUserId());
	                                            user.setCreateTime(new Date());
	                                            user.setIsActive("Y");
	                                            if (user.getCompany() == null) {
	                                                user.setCompany(internalCompanyObj);
	                                            }
	                                            userDAO.registerUser(user);
	                                            CompanyTransMgmt companyTransMgmt = new CompanyTransMgmt();
	                                            companyTransMgmt.setIsActive("Y");
	                                            companyTransMgmt.setIsExternal("Y");
	                                            companyTransMgmt.setUserId(user.getUserId());
	                                            if (userObj != null && userObj.getCompany() != null) {
	                                                companyTransMgmt.setCompanyId(userObj.getCompany().getCompanyId());
	                                            } else {
	                                                if (user.getCompany() != null) {
	                                                    companyTransMgmt.setCompanyId(user.getCompany().getCompanyId());
	                                                } else {
	                                                    companyTransMgmt.setCompanyId(internalCompanyObj.getCompanyId());
	                                                }
	                                            }

	                                            userService.SaveCompanyTransMgmt(companyTransMgmt);
	                                            insertedCount++;
	                                        } else {
	                                            String failedStr = "";
	                                            if (emailIdExists && userExistsFlag) {
	                                                failedStr = user.getUserName() + "/" + user.getEmailId() + " already exists";
	                                            } else if (isSuperTranslator) {
	                                                failedStr = "Company Admin do not have privilege to create super translator";
	                                            } else {
	                                                if (userExistsFlag) {
	                                                    failedStr = "Username '" + user.getUserName() + "' already exists";
	                                                }
	                                                if (emailIdExists) {
	                                                    failedStr = "email-id '" + user.getEmailId() + "' already exists";
	                                                }
	                                            }
	                                            userExistsList.add(failedStr);
	                                            rejectedCount++;
	                                            userExistsLineNumber.add(rowCount);

	                                        }
	                                    }


	                                    if (user.getUserName() == null || user.getUserName() == "") {
	                                        rejectedCount++;
	                                        userExistsList.add("User name is empty");
	                                        userExistsLineNumber.add(rowCount);
	                                    }

	                                    if (user.getEmailId() == null || user.getEmailId() == "") {
	                                        rejectedCount++;
	                                        userExistsList.add("Email id empty");
	                                        userExistsLineNumber.add(rowCount);
	                                    }

	                                }
	                                if (lookupClass.newInstance() instanceof TermInformation) {

	                                    index = 0;
	                                    //logic to save TermInformation
	                                    termInformation.setCreatedBy(userObj.getUserId());
	                                    termInformation.setCreateDate(new Date());
	                                    String errorDisp = "";
	                                    //	String deprecatedSource1=null;
	                                    //String deprecatedSource2=null;
	                                    //String deprecatedSource3=null;
	                                    String language = null;
	                                    Set<DeprecatedTermInformation> deprecatedTermInformationSet = null;
	                                    
	                                    Map<String, TermInformation> termMap = new HashMap<String, TermInformation>();
	                                    dcdSource1 = null;
	                                    dcdSource2 = null;
	                                    dcdSource3 = null;
	                                    dcdTarget1 = null;
	                                    dcdTarget2 = null;
	                                    dcdTarget3 = null;
	                                    if (termInformation.getTermBeingPolled() == null || termInformation.getTermBeingPolled().trim().length() <= 0) {
	                                        errorDisp = "Source Term is not available";
	                                        rejectedTermList.add(errorDisp);
	                                        termErrorLineNumber.add(rowCount);
	                                        isValidData = false;
	                                    }

	                                    for (int i = 0; i < noOfLangs; i++) {
	                                        if (isValidData && termInformation != null && termInformation.getTermBeingPolled() != null && termInformation.getTermBeingPolled().trim() != "" && termInformation.getSuggestedTermLangId() != null) {

	                                            DeprecatedTermInformation deprecatedTermInfo = new DeprecatedTermInformation();
	                                            deprecatedTermInfo.setCreateDate(new Date());
	                                            deprecatedTermInfo.setCreatedBy(userObj.getUserId());
	                                            deprecatedTermInfo.setIsActive("Y");
	                                            TermInformation newTerm = new TermInformation();
	                                            newTerm.setCreatedBy(userObj.getUserId());
	                                            newTerm.setCreateDate(new Date());
	                                            newTerm.setTermLangId(termInformation.getTermLangId());
	                                            newTerm.setTermBeingPolled(termInformation.getTermBeingPolled());
	                                            newTerm.setTermPOS(termInformation.getTermPOS());
	                                            newTerm.setTermForm(termInformation.getTermForm());
	                                            newTerm.setTermStatusId(termInformation.getTermStatusId());
	                                            if (termInformation.getTermCategory() != null) {
	                                                newTerm.setTermCategory(termInformation.getTermCategory());
	                                            }
	                                            if (termInformation.getTermDomain() != null) {
	                                                newTerm.setTermDomain(termInformation.getTermDomain());
	                                            }
	                                            newTerm.setTermUsage(termInformation.getTermUsage());
	                                            newTerm.setConceptDefinition(termInformation.getConceptDefinition());
	                                            newTerm.setTermNotes(termInformation.getTermNotes());
	                                            newTerm.setComments(termInformation.getComments());
	                                            //newTerm.setTermCompany(userObj.getCompany());
	                                        
	                                            newTerm.setTermCompany(companyObj);
	                                            if (langNames[i] != null  && langNames[i].contains(TeaminologyEnum.DEPRECATED_TERM.getValue())) {
	                                                if (langNames[i].contains(TeaminologyEnum.DEPRECATED_SOURCE1.getValue())) {
	                                                    language = langNames[i].substring(0, langNames[i].indexOf(TeaminologyEnum.DEPRECATED_SOURCE1.getValue())).trim();
	                                                    //if (language.equalsIgnoreCase(TeaminologyEnum.ENGLISH.getValue())) {
	                                                    if(TeaminologyEnum.ENGLISH.getValue().contains(language)){
	                                                        if (targetTerms[i] != null && targetTerms[i].trim().length() > 0) {
	                                                            if (targetTerms[i].trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
	                                                                dcdSource1 = targetTerms[i].trim();
	                                                            } else {
	                                                                isValidData = false;
	                                                                errorDisp = "English Deprecated1  is too long . Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
	                                                                rejectedTermList.add(errorDisp);
	                                                                termErrorLineNumber.add(rowCount);
	                                                            }
	                                                        }

	                                                    } 
	                                                        if (dcdSource1 != null && dcdSource1.trim().length() > 0 &&  dcdSource1.trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue())
	                                                            deprecatedTermInfo.setDeprecatedSource(dcdSource1);
	                                                }
	                                                if (langNames[i].contains(TeaminologyEnum.DEPRECATED_TARGET1.getValue())) {
	                                                    language = langNames[i].substring(0, langNames[i].indexOf(TeaminologyEnum.DEPRECATED_TARGET1.getValue())).trim();
	                                                    if(language != null){
	                                                    	if(targetTerms[i] == null || targetTerms[i].isEmpty()){
	                                                    		dcdTarget1 = null;
	                                                    	}
	                                                        if (targetTerms[i] != null && targetTerms[i].trim().length() > 0) {
	                                                            if (targetTerms[i].trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
	                                                            	dcdTarget1 = targetTerms[i].trim();
	                                                            } else {
	                                                                isValidData = false;
	                                                                errorDisp = "Target Deprecated1  is too long . Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
	                                                                rejectedTermList.add(errorDisp);
	                                                                termErrorLineNumber.add(rowCount);
	                                                            }
	                                                        }
	                                                    }
	                                                    
	                                                        if (dcdTarget1 != null && dcdTarget1.trim().length() > 0 && dcdTarget1.trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue())
	                                                            deprecatedTermInfo.setDeprecatedTarget(dcdTarget1);
	                                                }
	                                                if (langNames[i].contains(TeaminologyEnum.DEPRECATED_SOURCE2.getValue())) {
	                                                    language = langNames[i].substring(0, langNames[i].indexOf(TeaminologyEnum.DEPRECATED_SOURCE2.getValue())).trim();
	                                                   // if (language.equalsIgnoreCase(TeaminologyEnum.ENGLISH.getValue())) {
	                                                    	if(TeaminologyEnum.ENGLISH.getValue().contains(language)){
	                                                        if (targetTerms[i] != null && targetTerms[i].trim().length() > 0) {

	                                                            if (targetTerms[i].trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
	                                                                dcdSource2 = targetTerms[i].trim();
	                                                            } else {
	                                                                isValidData = false;
	                                                                errorDisp = "English Deprecated2  is too long . Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
	                                                                rejectedTermList.add(errorDisp);
	                                                                termErrorLineNumber.add(rowCount);
	                                                            }
	                                                        }

	                                                    }  
	                                                        if (dcdSource2 != null && dcdSource2.trim().length() > 0 && dcdSource2.trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue())
	                                                            deprecatedTermInfo.setDeprecatedSource(dcdSource2);
	                                                   
	                                                }
	                                                if (langNames[i].contains(TeaminologyEnum.DEPRECATED_TARGET2.getValue())) {
	                                                    language = langNames[i].substring(0, langNames[i].indexOf(TeaminologyEnum.DEPRECATED_TARGET2.getValue())).trim();
	                                                    if(language != null){
	                                                    	if(targetTerms[i] == null || targetTerms[i].isEmpty()) {
	                                                    		dcdTarget2 = null;
	                                                    	}
	                                                        if (targetTerms[i] != null && targetTerms[i].trim().length() > 0) {
	                                                            if (targetTerms[i].trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
	                                                            	dcdTarget2 = targetTerms[i].trim();
	                                                            } else {
	                                                                isValidData = false;
	                                                                errorDisp = "Target Deprecated2  is too long . Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
	                                                                rejectedTermList.add(errorDisp);
	                                                                termErrorLineNumber.add(rowCount);
	                                                            }
	                                                        }
	                                                    }
	                                                     
	                                                }
	                                                if (dcdTarget2 != null && dcdTarget2.trim().length() > 0 &&  dcdTarget2.trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue())
	                                                	deprecatedTermInfo.setDeprecatedTarget(dcdTarget2);
	                                                
	                                                if (langNames[i].contains(TeaminologyEnum.DEPRECATED_SOURCE3.getValue())) {
	                                                    language = langNames[i].substring(0, langNames[i].indexOf(TeaminologyEnum.DEPRECATED_SOURCE3.getValue())).trim();
	                                                   // if (language.equalsIgnoreCase(TeaminologyEnum.ENGLISH.getValue())) {
	                                                    if(TeaminologyEnum.ENGLISH.getValue().contains(language)){
	                                                        if (targetTerms[i] != null && targetTerms[i].trim().length() > 0) {

	                                                            if (targetTerms[i].trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
	                                                                dcdSource3 = targetTerms[i].trim();
	                                                            } else {
	                                                                isValidData = false;
	                                                                errorDisp = "English Deprecated3  is too long . Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
	                                                                rejectedTermList.add(errorDisp);
	                                                                termErrorLineNumber.add(rowCount);
	                                                            }
	                                                        }
	                                                    } 
	                                                       
	                                                }
	                                                if (dcdSource3 != null && dcdSource3.trim().length() > 0 && dcdSource3.trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue())
	                                                    deprecatedTermInfo.setDeprecatedSource(dcdSource3);
	                                                
	                                                if (langNames[i].contains(TeaminologyEnum.DEPRECATED_TARGET3.getValue())) {
	                                                    language = langNames[i].substring(0, langNames[i].indexOf(TeaminologyEnum.DEPRECATED_TARGET3.getValue())).trim();
	                                                    if(language != null){
	                                                    	if(targetTerms[i] == null || targetTerms[i].isEmpty()){
	                                                    		dcdTarget3 = null;
	                                                    	}
	                                                        if (targetTerms[i] != null && targetTerms[i].trim().length() > 0) {
	                                                            if (targetTerms[i].trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
	                                                            	dcdTarget3 = targetTerms[i].trim();
	                                                            } else {
	                                                                isValidData = false;
	                                                                errorDisp = "Target Deprecated3  is too long . Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
	                                                                rejectedTermList.add(errorDisp);
	                                                                termErrorLineNumber.add(rowCount);
	                                                            }
	                                                        }
	                                                    } 
	                                                }
	                                                if (dcdTarget3 != null && dcdTarget3.trim().length() > 0 && dcdTarget3.trim().length() < DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue())
	                                                    deprecatedTermInfo.setDeprecatedTarget(dcdTarget3);
	                                                
	                                                if (termMap.containsKey(language.trim())) {

	                                                    deprecatedTermInformationSet = new HashSet<DeprecatedTermInformation>();

	                                                    Language deprecatedTermLang = lookUpDAO.getLanguageByLabel(language.trim());
	                                                    TermInformation termInfo = termMap.get(language.trim());
	                                                   
	                                                    if ((dcdSource1 != null && dcdSource1.trim().length() > 0) || (dcdTarget1 != null && dcdTarget1.trim().length() > 0) ) {
	                                                        DeprecatedTermInformation deprecatedTermInfo1 = new DeprecatedTermInformation();
	                                                        deprecatedTermInfo1.setCreateDate(new Date());
	                                                        deprecatedTermInfo1.setCreatedBy(userObj.getUserId());
	                                                        deprecatedTermInfo1.setIsActive("Y");
	                                                        deprecatedTermInfo1.setDeprecatedSource(dcdSource1);
	                                                        deprecatedTermInfo1.setDeprecatedTarget(dcdTarget1);
	                                                        deprecatedTermInformationSet.add(deprecatedTermInfo1);
	                                                    }

	                                                    if ((dcdSource2 != null && dcdSource2.trim().length() > 0) || (dcdTarget2 != null && dcdTarget2.trim().length() > 0 )) {
	                                                    	DeprecatedTermInformation deprecatedTermInfo2 = new DeprecatedTermInformation();
	                                                        deprecatedTermInfo2.setCreateDate(new Date());
	                                                        deprecatedTermInfo2.setCreatedBy(userObj.getUserId());
	                                                        deprecatedTermInfo2.setIsActive("Y");
	                                                        deprecatedTermInfo2.setDeprecatedSource(dcdSource2);
	                                                        deprecatedTermInfo2.setDeprecatedTarget(dcdTarget2);
	                                                        deprecatedTermInformationSet.add(deprecatedTermInfo2);
	                                                    }

	                                                    if (dcdSource3 != null && dcdSource3.trim().length() > 0 || (dcdTarget3 != null && dcdTarget3.trim().length() > 0 )) {
	                                                        DeprecatedTermInformation deprecatedTermInfo3 = new DeprecatedTermInformation();
	                                                        deprecatedTermInfo3.setCreateDate(new Date());
	                                                        deprecatedTermInfo3.setCreatedBy(userObj.getUserId());
	                                                        deprecatedTermInfo3.setIsActive("Y");
	                                                        deprecatedTermInfo3.setDeprecatedSource(dcdSource3);
	                                                        deprecatedTermInfo3.setDeprecatedTarget(dcdTarget3);
	                                                        deprecatedTermInformationSet.add(deprecatedTermInfo3);
	                                                    }
	                                                    termInfo.setDeprecatedTermInfo(deprecatedTermInformationSet);
	                                                    termMap.put(language.trim(), termInfo);
	                                                }
	                                            }
	                                            Language suggestedTermLang = lookUpDAO.getLanguageByLabel(langNames[i]);
	                                            if (suggestedTermLang != null && suggestedTermLang.getLanguageId() != null) {
	                                                if (targetTerms[i].trim().length() < DBColumnsSizeEnum.SOURCE_TERM_MAX_SIZE.getIntValue()) {
	                                                    newTerm.setSuggestedTermLangId(suggestedTermLang.getLanguageId());
	                                                    newTerm.setSuggestedTerm(targetTerms[i]);
	                                                    String lang= suggestedTermLang.getLanguageLabel();
	                                                    String targetPos=lang+" "+"Part Of Speech";
	                                                    String targetStatus=lang+"Status";
	                                                    int j=sugg-1+i;
	                                                    if(langNames[j] != null && langNames[j].contains(targetPos)){
	                                                  	   PartsOfSpeech partOfSpeech = allPartsOfSpeech.get(targetTerms[j]);
	                                                  	   if(partOfSpeech != null){
	                                                  		   newTerm.setSuggestedTermPosId(partOfSpeech.getPartsOfSpeechId());
	                                                  	   }
	                                                  	   else
	                                                  		 newTerm.setSuggestedTermPosId(5);
	                                                    }
	                                                  int k=j+(sugg-1);
	                                                    if(langNames[k] != null && langNames[k].contains(targetStatus)){
	                                                 	   Status status=statuses.get(targetTerms[k]);
	                                                 	   if(status !=null)
	                                                 		   newTerm.setSuggestedTermStatusId(status.getStatusId());
	                                                    }
	                                                    termMap.put(langNames[i].trim(), newTerm);

	                                                } else {
	                                                    isValidData = false;
	                                                    errorDisp = "Target term is  too long for   " + langNames[i].toUpperCase() + " language term ";
	                                                    rejectedTermList.add(errorDisp);
	                                                    termErrorLineNumber.add(rowCount);
	                                                }
	                                               
	                                            }
	                                          
	                                            if (suggestedTermLang == null && (!(langNames[i].contains("Deprecated"))) && (!(langNames[i].contains("Part Of Speech"))) && (!(langNames[i].contains("Status"))) && (!(langNames[i].contains("Final"))) ) {
	                                                errorDisp = "Target Language is not available for:  " + langNames[i].toUpperCase();
	                                                rejectedTermList.add(errorDisp);
	                                                termErrorLineNumber.add(rowCount);
	                                                isValidData = false;
	                                            }
	                                        }
	                                        if (!isValidData) {
	                                            isValidData = true;
	                                            rejectedCount++;
	                                        }
	                                    }
	                                    for (String label : termMap.keySet()) {
	                                        TermInformation termInfoObj = termMap.get(label);
	                                        if (termInfoObj != null && (termInfoObj.getDeprecatedTermInfo() == null || termInfoObj.getDeprecatedTermInfo().size() == 0)) {}

	                                       
	                                       // for (Company company : companyList) {
	                                        	 int count=0;
	                                            TermInformation newTermInfo = new TermInformation();
	                                            newTermInfo.setTermBeingPolled(termInfoObj.getTermBeingPolled());
	                                            newTermInfo.setTermStatusId(termInfoObj.getTermStatusId());
	                                           // newTermInfo.setTermPOS(termInfoObj.getTermPOS());
	                                            newTermInfo.setTermUsage(termInfoObj.getTermUsage());
	                                            if (termInfoObj.getTermPOS() != null) {
	                                                newTermInfo.setTermPOS(termInfoObj.getTermPOS());
	                                            }                                    
	                                            if (termInfoObj.getTermForm() != null) {
	                                                newTermInfo.setTermForm(termInfoObj.getTermForm());
	                                            }
	                                            if (termInfoObj.getTermCategory() != null) {
	                                                newTermInfo.setTermCategory(termInfoObj.getTermCategory());
	                                            }
	                                            if (termInfoObj.getTermDomain() != null) {
	                                                newTermInfo.setTermDomain(termInfoObj.getTermDomain());
	                                            }
	                                            newTermInfo.setSuggestedTermUsage(termInfoObj.getTermUsage());
	                                            newTermInfo.setTermNotes(termInfoObj.getTermNotes());
	                                            newTermInfo.setTermProgram(termInfoObj.getTermProgram());
	                                            newTermInfo.setTermLangId(termInfoObj.getTermLangId());
	                                            newTermInfo.setConceptDefinition(termInfoObj.getConceptDefinition());
	                                            newTermInfo.setConceptNotes(termInfoObj.getConceptNotes());
	                                            newTermInfo.setConceptProdGroup(termInfoObj.getConceptProdGroup());
	                                            newTermInfo.setConceptCatId(termInfoObj.getConceptCatId());
	                                            newTermInfo.setSuggestedTerm(termInfoObj.getSuggestedTerm());
	                                            newTermInfo.setSuggestedTermLangId(termInfoObj.getSuggestedTermLangId());
	                                            newTermInfo.setSuggestedTermStatusId(termInfoObj.getSuggestedTermStatusId());
	                                            newTermInfo.setSuggestedTermFormId(termInfoObj.getSuggestedTermFormId());
	                                            newTermInfo.setSuggestedTermPosId(termInfoObj.getSuggestedTermPosId());
	                                            newTermInfo.setSuggestedTermUsage(termInfoObj.getSuggestedTermUsage());
	                                            newTermInfo.setSuggestedTermNotes(termInfoObj.getSuggestedTermNotes());
	                                            newTermInfo.setSuggestedTermPgmId(termInfoObj.getSuggestedTermPgmId());
	                                            newTermInfo.setCreatedBy(termInfoObj.getCreatedBy());
	                                            newTermInfo.setCreateDate(termInfoObj.getCreateDate());
	                                            newTermInfo.setIsActive(termInfoObj.getIsActive());
	                                            newTermInfo.setComments(termInfoObj.getComments());
	                                            newTermInfo.setTermCompany(companyObj);
	                                            newTermInfo.setTermDomain(termInfoObj.getTermDomain());
	                                            Set<DeprecatedTermInformation> newDeprecatedTermInformationSet = new HashSet<DeprecatedTermInformation>();
	                                            if (termInfoObj.getDeprecatedTermInfo() != null) {
	                                                for (DeprecatedTermInformation deprecatedTermInformation : termInfoObj.getDeprecatedTermInfo()) {
	                                                    DeprecatedTermInformation newDeprTermInfo = new DeprecatedTermInformation();
	                                                    newDeprTermInfo.setTermInfo(newTermInfo);
	                                                    newDeprTermInfo.setDeprecatedSource(deprecatedTermInformation.getDeprecatedSource());
	                                                    newDeprTermInfo.setDeprecatedTarget(deprecatedTermInformation.getDeprecatedTarget());
	                                                    newDeprTermInfo.setIsActive(deprecatedTermInformation.getIsActive());
	                                                    newDeprecatedTermInformationSet.add(newDeprTermInfo);
	                                                }
	                                            }
	                                            if (newTermInfo.getTermBeingPolled() == null || newTermInfo.getTermBeingPolled().trim().length() <= 0) {
	                                            	sourceVal="NoSource";
													rejectedCount++;
	                                            	break;
	                                            }
	                                            for(int i=0; i<sugg-1;i++){
	                                          	  if( targetTerms[i] .isEmpty()){
	                                                 count++;
	                                          	  }
	                                              }
	                                             if(count == sugg-1){
	                                      		   targets="Novalue";
												   rejectedCount++;
	                                      		   break;
	                                      	  }
	                                            if(newTermInfo.getTermDomain() == null || newTermInfo.getTermCategory() == null || newTermInfo.getTermPOS() == null ){
	                                            	termSavedInformation="DontProcess";
													rejectedCount++;
	                                            	break;
	                                            }
	                                            
	                                            newTermInfo.setDeprecatedTermInfo(newDeprecatedTermInformationSet);
	                                            termSavedInformation = termDAO.addNewTerm(newTermInfo);
	                                            logger.info("termSavedInfomation == " + termSavedInformation);
	                                            if ("success".equalsIgnoreCase(termSavedInformation)) {
	                                                insertedTermInformationList.add(newTermInfo);

	                                            } else if ("update".equalsIgnoreCase(termSavedInformation)) {
	                                                updatedTermInformationList.add(newTermInfo);
	                                            }
	                                       // }
	                                        if ("success".equalsIgnoreCase(termSavedInformation)) {
	                                            insertedCount++;
	                                        } else if ("failedterm".equalsIgnoreCase(termSavedInformation)) {
	                                            rejectedCount++;
	                                                                                       
	                                            String langLabel = "";
	                                            for(Language lang : allLanguages) {
	                                            	if(termInfoObj.getSuggestedTermLangId() != null && termInfoObj.getSuggestedTermLangId().intValue() == lang.getLanguageId().intValue()) {
	                                            		langLabel = lang.getLanguageLabel();
	                                            		break;
	                                            	}
	                                            }
	                                            errorDisp = "Source Term  '" + termInfoObj.getTermBeingPolled() + "' with '" + langLabel + "' language already exists";

	                                            rejectedTermList.add(errorDisp);
	                                            termErrorLineNumber.add(rowCount);
	                                        } else if ("update".equalsIgnoreCase(termSavedInformation)) {
	                                            updatedCount++;

	                                        } else if ("failed".equalsIgnoreCase(termSavedInformation)) {
	                                            rejectedCount++;
	                                            errorDisp = "Failed To Insert Term for " + termInfoObj.getTermBeingPolled();
	                                            rejectedTermList.add(errorDisp);
	                                            termErrorLineNumber.add(rowCount);
	                                        } else{
	                                        	 rejectedCount++;
	                                        }
	                                    }
	                                    targetTerms =  new String[row.length];
	                                    importStatus.setTermInformationStatus("success");
	                                }
	                            }
	                          
	                            logger.info("current row" + rowCount + "/" + totalRows);
	                        }
	                        catch (Exception e) {
	                            rejectedCount++;
	                            rejectedTermList.add(e.getMessage() + " for " + sourceTerm);
	                            termErrorLineNumber.add(rowCount);
	                            logger.error("Error in readCSV().", e);
	                        }
	                       if(("DontProcess".equalsIgnoreCase(termSavedInformation)) && (domainValue == null || catValue == null || posValue ==null)) {
	                       	     rejectedTermList.add("Domain/Category/Parts of Speech  should not be empty");
	                       	     termErrorLineNumber.add(rowCount);
	                        	 termSavedInformation=null;
	                       }else if((domainValue != null || catValue != null || posValue != null) && (domain == null || partsOfSpeech == null || category == null))  {
	                    	     rejectedTermList.add("Domain/Category/Parts of Speech "+ "\""+domainValue+ "\"" + " or  "+"\""+catValue+ "\"" + " or "+ "\""+posValue+ "\""+
	                           "doesn't exist in DB ");
	                    	   termErrorLineNumber.add(rowCount);
	                       }
	                        if(targets != null){
	                        if(targets.equalsIgnoreCase("Novalue")){
	                        	rejectedTermList.add("No targets are there, please give target values ");
	                        	termErrorLineNumber.add(rowCount);
	                        	targets=null;
	                        }
	                        }
	                        domainValue=null;
	                        partsOfSpeech=null;
	                        category=null;
	                    }
	                    //end of for loop
	                    updateTermBaseIndex(updatedTermInformationList);
	                    addNewTermBaseIndex(insertedTermInformationList);
	                    if (importStatus.getMissedColumns() == null) {
	                        importStatus.setInsertedCount(insertedCount);
	                        importStatus.setRejectedCount(rejectedCount);
	                        importStatus.setUpdatedCount(updatedCount);
	                        importStatus.setUserNames(rejectedTermList);
	                        importStatus.setLineNumbers(termErrorLineNumber);
	                    }
	                    
	                    if (lookupClass.newInstance() instanceof User) {
	                        if (rejectedCount > 0) {
	                            importStatus.setRejectedCount(rejectedCount);
	                            importStatus.setUserNames(userExistsList);
	                            importStatus.setLineNumbers(userExistsLineNumber);
	                        } else if(insertedCount > 0 ){
	                        	  importStatus.setInsertedCount(insertedCount);
	                        	  importStatus.setTermInformationStatus("Success");
	                        }
	                        else {
	                            importStatus.setTermInformationStatus("failed");
	                        }

	                    }

	                    if (lookupClass.newInstance() instanceof TermInformation) {
	                        if (importStatus.getMissedColumns() == null) {
	                            importStatus.setInsertedCount(insertedCount);
	                            importStatus.setRejectedCount(rejectedCount);
	                            importStatus.setUserNames(rejectedTermList);
	                            importStatus.setLineNumbers(termErrorLineNumber);
	                        }
	                        rejectedTermList = new ArrayList<String>();
	                        termErrorLineNumber = new ArrayList<Integer>();
	                    }
	                }
	            }

	        }
	        catch (Exception e) {
	            importStatus.setTermInformationStatus("failed");
	            logger.error("Error in readCSV().", e);
	        }
	        finally {
	            allCompanies.clear();
	            allCompaniesMap.clear();
	            allRoles.clear();
	            allLanguages.clear();
	            allPartsOfSpeech.clear();
	            allDomains.clear();
	            allCategories.clear();
	            allForms.clear();
	        }

	        return importStatus;
	    }
	
		public void indexMethod(int newIndex[], int columnNdx , List<String> termInfoLableList)
		{
	
			if (newIndex[columnNdx] > 0) {
				TeaminologyImportEnum enumValue = TeaminologyImportEnum.getEnumColumnById(newIndex[columnNdx]);
				if (enumValue != null) {
					String indexColumn = enumValue.getStringValue();
					if (indexColumn != null) {
						indexColumn = indexColumn.toLowerCase().trim();
						termInfoLableList.remove(indexColumn);
					}
				}
			}
	
	
		}
		/**
		 * To  import TBX
		 *
		 * @param uploadFile  -File to be read
		 * @param lookupClass - Object to be read
		 * @param companyIds  String to be read
		 * @return CSVImportStatus obj
		 */
		@Override
		@Transactional
		public <C extends TeaminologyObject> CSVImportStatus readTBX(File uploadFile, Class<C> lookupClass, User user, String companyIds) {
			return TbxConvert(uploadFile, lookupClass, user, companyIds);
	
		}
	
		/**
		 * To  export csv
		 *
		 * @param exportObjList     - Object list to be export
		 * @param absolutePath      -  Path of the export file template
		 * @param uploadedfilesPath - Path of the location where files are uploading
		 * @return File
		 */
		@Override
		@Transactional
		public <C extends TeaminologyObject> File writeCSV(List<C> exportObjList,
				String absolutePath, String uploadedfilesPath, String exportFormate) {
			File csvFile = null;
			File tabFile;
			try {
				Collections.sort(exportObjList, new Comparator<C>()
						{
					@Override
					public int compare(C o1, C o2) {
						if (o1 instanceof LanguagePiChartData) {
							LanguagePiChartData obj1 = (LanguagePiChartData) o1;
							LanguagePiChartData obj2 = (LanguagePiChartData) o2;
							return obj1.getLanguage().compareTo(obj2.getLanguage());
						} else if (o1 instanceof LanguageReportData) {
							LanguageReportData obj1 = (LanguageReportData) o1;
							LanguageReportData obj2 = (LanguageReportData) o2;
							return obj1.getLanguage().getLanguageLabel().compareTo(obj2.getLanguage().getLanguageLabel());
						} 
						return 0;  //To change body of implemented methods use File | Settings | File Templates.
					}
						});
	
				if(exportFormate!=null){
					if(exportFormate.equalsIgnoreCase("TAB")){
						tabFile=convertFromExcelToTab(exportObjList, absolutePath, uploadedfilesPath,csvFile);
						return tabFile;
					}
				}
				csvFile = handleDownlodCSV(exportObjList, absolutePath, uploadedfilesPath);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
	
			return csvFile;
		}
	
	
		public <C extends TeaminologyObject> File convertFromExcelToTab(List<C> exportObjList,  String absolutePath, String uploadedfilesPath, File  inputFile) throws IOException {  
			//BufferedReader br=null;
			//BufferedWriter bw=null;
			String path=uploadedfilesPath+"/termTabData.txt";
			File csvFile = null;
			String data=null;
			ByteArrayOutputStream byteArrayOutputStream = null;
			WritableWorkbook outputWorkBook = null;
			File file = new File(path);
			try {
				WorkbookSettings wbSettings = new WorkbookSettings();
	
				wbSettings.setEncoding("Cp1252");
	
				InputStream is = getInputStreamFromFile(absolutePath +".xls");
				Workbook inputWorkBook = Workbook.getWorkbook(is, wbSettings);
				byteArrayOutputStream = new ByteArrayOutputStream();
	
				outputWorkBook = Workbook.createWorkbook(byteArrayOutputStream, inputWorkBook, wbSettings);
				WritableSheet sheet1 = outputWorkBook.getSheet("Sheet1");
				//Set the column names, column headers, column page breaks and other formatting
				sheetFormatSettings(sheet1);
				populateSheetData(sheet1, exportObjList);
	
				OutputStream os = (OutputStream) new FileOutputStream(file);
				String encoding = "UTF-8";
				os.write(239);
				os.write(187);
				os.write(191);
				OutputStreamWriter osw = new OutputStreamWriter(os, encoding);
				BufferedWriter bw = new BufferedWriter(osw);
	
				for (int sheet = 0; sheet < outputWorkBook.getNumberOfSheets(); sheet++) {
					Sheet s = outputWorkBook.getSheet(sheet);
	
					Cell[] row = null;
	
					for (int i = 0; i < s.getRows(); i++) {
						row = s.getRow(i);
						if (row.length > 0) {
							data=row[0].getContents().replace("\n", new String("\u0002"));
							bw.write(data);
							for (int j = 1; j < row.length; j++) {
								bw.write('\t');
								data=row[j].getContents().replace("\n", new String("\u0002"));
								bw.write(data);
							}
						}
						bw.newLine();
					}
				}
				bw.flush();
				bw.close();
			}   
			catch (FileNotFoundException a) {  
				System.out.println("Could not open file");  
				a.printStackTrace();  
			}  
			catch(IOException b){  
				System.out.println("IOException occured");  
				b.printStackTrace();  
			}  
			catch(Exception c){  
				c.printStackTrace();  
			}  
			finally {  
	
				byteArrayOutputStream.close();  
			}    
	
			return file;  
		}  
	
		@Override
		@Transactional
		public <C extends TeaminologyObject> File writeVoteCSV(List<C> exportObjList, String absolutePath, String uploadedfilesPath ,List<C> params) {
			File csvFile = null;
			try {
	
				if(exportObjList != null){
					Collections.sort(exportObjList, new Comparator<C>()
							{
						@Override
						public int compare(C o1, C o2) {
	
							TermVotingTo obj1 = (TermVotingTo) o1;
							TermVotingTo obj2 = (TermVotingTo) o2;
							return obj1.getSource().compareToIgnoreCase(obj2.getSource());
						}
							});
				}
				csvFile = handleDownlodVoteCSV(exportObjList, absolutePath, uploadedfilesPath,  params);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return csvFile;
	
		}
	
		/**
		 * To parse excel csv
		 *
		 * @param filePath
		 * @return List<String []>
		 */
		@Transactional
		private static List<String[]> parseExcelCSV(String filePath) {
			List<String[]> out = new ArrayList<String[]>();
			try {
				File uploadFile = new File(filePath);
				InputStream inputStream = new FileInputStream(uploadFile);
				Reader reader = null;
	
				//Check if uploaded file is in UTF8 file format
				if (isUTF8FileFormat(uploadFile))
					reader = new InputStreamReader(inputStream, "UTF-8");
				else
					reader = new InputStreamReader(inputStream);
	
				String[][] values;
				try {
					//Check if file is tab delimited file format
					if (isTabDelimited(uploadFile))
						values = ExcelCSVParser.parse(reader, '\t');
					else
						values = ExcelCSVParser.parse(reader);
	
					if (values != null) {
						for (int rowNdx = 0; rowNdx < values.length; rowNdx++) {
							String[] strings = values[rowNdx];
							if (strings == null || strings.length < 1) {
								break;
							}
							out.add(strings);
						}
					}
				}
				catch (IOException e) {
					logger.error("Error found in parse CSV file.", e);
				}
			}
			catch (FileNotFoundException e) {
				logger.error("Cannot find file [" + filePath + "]", e);
			}
			catch (Exception e) {
				logger.error("Error found in parseExcelCSV().", e);
			}
			return out;
		}
	
		private static boolean isTabDelimited(File uploadedFile) {
			if (!uploadedFile.exists() || !uploadedFile.isFile())
				return false;
	
			BufferedReader fin = null;
			try {
				fin = new BufferedReader(new FileReader(uploadedFile));
				String line = null;
				while ((line = fin.readLine()) != null) {
					if (line.contains("\t") && !line.contains(","))
						return true;
					break;
				}
			} catch (Exception e) {
				logger.error("Found error in isTabDelimited().", e);
			} finally {
				try {
					if (fin != null)
						fin.close();
				} catch (IOException ioe) {
					logger.error("Found error in closing file.", ioe);
				}
			}
			return false;
		}
	
		private static boolean isUTF8FileFormat(File file) {
			if (file == null || !file.exists() || !file.isFile())
				return false;
	
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				byte[] b = new byte[3];
				fis.read(b, 0, 3);
				if (b[0] == (byte) 0xef && b[1] == (byte) 0xbb && b[2] == (byte) 0xbf)
					return true;
			} catch (Exception e) {
				logger.error("Error found in isUTF8FileFormat().", e);
			} finally {
				try {
					if (fis != null)
						fis.close();
				} catch (IOException ioe) {
					logger.error("Error found in closing file.", ioe);
				}
			}
			return false;
		}
		/**
		 * To remove new line character
		 *
		 * @param inputStr
		 * @return String
		 */
		public static String removeNewLineCharacter(String inputStr) {
			String pattern = "[\\t\\n\\x0B\\f\\r]";
			String resultStr = inputStr.replaceAll(pattern, " ");
			return resultStr;
		}
	
		/**
		 * To handle download csv
		 *
		 * @param exportObjList     - Object list to be export
		 * @param absolutePath      -  Path of the export file template
		 * @param uploadedfilesPath - Path of the location where files are uploading
		 * @return File
		 */
		@Transactional
		private <C extends TeaminologyObject> File handleDownlodCSV(
				List<C> exportObjList, String absolutePath, String uploadedfilesPath)
						throws Exception {
			if(exportObjList != null){
	
				Collections.sort(exportObjList, new Comparator<C>()
						{
					TermInformationTo obj1 =null;
					TermInformationTo obj2=null;
					int sortValue=0;
					@Override
					public int compare(C o1, C o2) {
						if(o1 instanceof TermInformationTo){
							obj1 = (TermInformationTo) o1;
							obj2 = (TermInformationTo) o2;
							if(obj1.getTermBeingPolled() != null && obj2.getTermBeingPolled() != null)
							sortValue = obj1.getTermBeingPolled().compareToIgnoreCase(obj2.getTermBeingPolled());
						}
						return  sortValue;
					}
	
						});
			}
			File csvFile = null;
			ByteArrayOutputStream byteArrayOutputStream = null;
			WritableWorkbook outputWorkBook = null;
			try {
				WorkbookSettings wbSettings = new WorkbookSettings();
	
				//wbSettings.setLocale(new Locale("en", "EN"));
				wbSettings.setEncoding("Cp1252");
	
				/* String templateFilePath = TeaminologyFileURL.TEAMINOLOGY_IMPORT_EXPORT_FILE_LOCATION.getValue()
	            			+absolutePath + ".xls";*/
	
	            			InputStream is = getInputStreamFromFile(absolutePath +".xls");
	            			Workbook inputWorkBook = Workbook.getWorkbook(is, wbSettings);
	            			byteArrayOutputStream = new ByteArrayOutputStream();
	
	            			outputWorkBook = Workbook.createWorkbook(byteArrayOutputStream,
	            					inputWorkBook, wbSettings);
	            			WritableSheet sheet1 = outputWorkBook.getSheet("Sheet1");
	            			// Set the column names, column headers, column page breaks and other formatting
	            			sheetFormatSettings(sheet1);
	            			populateSheetData(sheet1, exportObjList);
	            			csvFile = convertExcelToCsv(outputWorkBook, uploadedfilesPath + absolutePath + ".csv");
	
	            			outputWorkBook.write();
	            			outputWorkBook.close();
	
			}
			finally {
				if (byteArrayOutputStream != null) byteArrayOutputStream.close();
			}
			return csvFile;
		}
	
		@Transactional
		private <C extends TeaminologyObject> File handleDownlodVoteCSV(
				List<C> exportObjList, String absolutePath, String uploadedfilesPath, List params)
						throws Exception {
	
			File csvFile = null;
			ByteArrayOutputStream byteArrayOutputStream = null;
			WritableWorkbook outputWorkBook = null;
			try {
				WorkbookSettings wbSettings = new WorkbookSettings();
	
				//wbSettings.setLocale(new Locale("en", "EN"));
				wbSettings.setEncoding("Cp1252");
	
				/* String templateFilePath = TeaminologyFileURL.TEAMINOLOGY_IMPORT_EXPORT_FILE_LOCATION.getValue()
	            			+absolutePath + ".xls";*/
	
				InputStream is = getInputStreamFromFile(absolutePath +".xls");
				Workbook inputWorkBook = Workbook.getWorkbook(is, wbSettings);
				byteArrayOutputStream = new ByteArrayOutputStream();
	
				outputWorkBook = Workbook.createWorkbook(byteArrayOutputStream,
						inputWorkBook, wbSettings);
				WritableSheet sheet1 = outputWorkBook.getSheet("Sheet1");
				// Set the column names, column headers, column page breaks and other formatting
				sheetFormatSettings(sheet1);
				populateVoteSheetData(sheet1, exportObjList,params);
				csvFile = convertExcelToCsv(outputWorkBook, uploadedfilesPath + absolutePath + ".csv");
				outputWorkBook.write();
				outputWorkBook.close();
	
			}
			finally {
				if (byteArrayOutputStream != null) byteArrayOutputStream.close();
			}
			return csvFile;
		}
	
	
		/**
		 * To get input stream from file
		 *
		 * @param filename - Name of the file to be read
		 * @return InputStream
		 */
		public InputStream getInputStreamFromFile(String filename) throws Exception {
			/*InputStream is = Utils.getInputStream(filename);		
			return is;*/
	
			InputStream is = getClass().getResourceAsStream(filename);
			return is;
		}
	
		/**
		 * To sheet format settings
		 *
		 * @param sheet
		 */
		private void sheetFormatSettings(WritableSheet sheet) throws Exception {
			if (sheet != null) {
	
				// Set the Page Orientation as Landscape
				sheet.setPageSetup(LANDSCAPE_ORIENTATION);
				sheet.getSettings().setPrintTitlesRow(0, 0);
			}
		}
	
		/**
		 * To write columns names
		 *
		 * @param sheet-        Excel sheet Object
		 * @param exportObjList - Object list to be export
		 */
		@Transactional
		private <C extends TeaminologyObject> void writeColumnNames(WritableSheet sheet, List<C> exportObjList) throws Exception {
			WritableCellFormat cellFormat = getFontCellFormat(Boolean.FALSE);
			if (exportObjList != null && exportObjList.size() > 0) {
				if (exportObjList.get(0) instanceof Terms) {
					for (TermEnum column : TermEnum.values()) {
						String columnName = column.getColumnName();
						Integer rowAt = column.getRowPosition();
						Integer columnAt = column.getColumnPosition();
						Boolean columnBreak = column.getColumnBreak();
						Integer columnWidth = column.getColumnWidth();
	
						sheet.setColumnView(columnAt, columnWidth);
						// Column Breaks
						if (columnBreak) {
							int breakAtColumn = columnAt;
							addColumnBreaks(sheet, (++breakAtColumn));
						}
						addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
					}
				}
				if (exportObjList.get(0) instanceof TermInformationTo) {
					String columnName = null;
					Integer rowAt = null;
					Integer columnAt = null;
					Boolean columnBreak = null;
					Integer columnWidth = null;
					sourceTermLangSet = new HashSet<String>();
					for (Object expObj : exportObjList) {
						TermInformationTo termInfoTo = (TermInformationTo) expObj;
						List<Integer> suggestedLangIds = termInfoTo.getSuggestedTermLangIds();
						for (Integer langId : suggestedLangIds) {
							Language langObj = lookUpDAO.getLanguage(langId);
							String langLabel = langObj == null ? null : langObj.getLanguageLabel();
							if (langLabel != null)
								sourceTermLangSet.add(langLabel);
						}
					}
					TermInformationEnum columnsource = TermInformationEnum.SOURCE_TERM;
					columnName = columnsource.getColumnName();
					rowAt = columnsource.getRowPosition();
					columnAt = columnsource.getColumnPosition();
					columnBreak = columnsource.getColumnBreak();
					columnWidth = columnsource.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					for (String lang : sourceTermLangSet) {
						columnAt++;
						columnName = lang;
						sheet.setColumnView(columnAt, columnWidth);
						addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
					}
					TermInformationEnum columncondef = TermInformationEnum.CONCEPT_DEFINITION;
					columnName = columncondef.getColumnName();
					rowAt = columncondef.getRowPosition();
					//columnAt = columncondef.getColumnPosition();
					columnAt++;
					columnBreak = columncondef.getColumnBreak();
					columnWidth = columncondef.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					TermInformationEnum columntermUsage = TermInformationEnum.TERM_USAGE;
					columnName = columntermUsage.getColumnName();
					rowAt = columntermUsage.getRowPosition();
					//columnAt = columntermUsage.getColumnPosition();
					columnAt++;
					columnBreak = columntermUsage.getColumnBreak();
					columnWidth = columntermUsage.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					TermInformationEnum columntermNotes = TermInformationEnum.TERM_NOTES;
					columnName = columntermNotes.getColumnName();
					rowAt = columntermNotes.getRowPosition();
					//columnAt = columntermNotes.getColumnPosition();
					columnAt++;
					columnBreak = columntermNotes.getColumnBreak();
					columnWidth = columntermNotes.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					TermInformationEnum columntercategory = TermInformationEnum.TERM_CATEGORY;
					columnName = columntercategory.getColumnName();
					rowAt = columntercategory.getRowPosition();
					//columnAt = columntercategory.getColumnPosition();
					columnAt++;
					columnBreak = columntercategory.getColumnBreak();
					columnWidth = columntercategory.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					TermInformationEnum columnTermPos = TermInformationEnum.TERM_PART_OF_SPEECH;
					columnName = columnTermPos.getColumnName();
					rowAt = columnTermPos.getRowPosition();
					// columnAt = columnTermPos.getColumnPosition();
					columnAt++;
					columnBreak = columnTermPos.getColumnBreak();
					columnWidth = columnTermPos.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					TermInformationEnum columnTermForm = TermInformationEnum.TERM_FORM;
					columnName = columnTermForm.getColumnName();
					rowAt = columnTermForm.getRowPosition();
					//columnAt = columnTermForm.getColumnPosition();
					columnAt++;
					columnBreak = columnTermForm.getColumnBreak();
					columnWidth = columnTermForm.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					TermInformationEnum columnTermStatus= TermInformationEnum.TERM_STATUS;
					columnName = columnTermStatus.getColumnName();
					rowAt = columnTermStatus.getRowPosition();
					//columnAt = columnTermStatus.getColumnPosition();
					columnAt++;
					columnBreak = columnTermStatus.getColumnBreak();
					columnWidth = columnTermStatus.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					for (String lang : sourceTermLangSet) {
						columnAt++;
						columnName = lang;
						columnWidth = 16;
						columnName = lang + " "+TeaminologyEnum.TERM_PART_OF_SPEECH.getValue();
						sheet.setColumnView(columnAt, columnWidth);
						addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
					}
	
					for (String lang : sourceTermLangSet) {
						columnAt++;
						columnWidth = 16;
						columnName = lang + TeaminologyEnum.TERM_STATUS.getValue();
						sheet.setColumnView(columnAt, columnWidth);
						addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
					}
	
					TermInformationEnum columnComments= TermInformationEnum.COMMENTS;
					columnName = columnComments.getColumnName();
					rowAt = columnComments.getRowPosition();
					//columnAt = columnComments.getColumnPosition();
					columnAt++;
					columnBreak = columnComments.getColumnBreak();
					columnWidth = columnComments.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					TermInformationEnum columnCompany= TermInformationEnum.COMPANY;
					columnName = columnCompany.getColumnName();
					rowAt = columnCompany.getRowPosition();
					//columnAt = columnCompany.getColumnPosition();
					columnAt++;
					columnBreak = columnCompany.getColumnBreak();
					columnWidth = columnCompany.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					TermInformationEnum columnDomain= TermInformationEnum.TERM_DOMAIN;
					columnName = columnDomain.getColumnName();
					rowAt = columnDomain.getRowPosition();
					//columnAt = columnDomain.getColumnPosition();
					columnAt++;
					columnBreak = columnDomain.getColumnBreak();
					columnWidth = columnDomain.getColumnWidth();
					sheet.setColumnView(columnAt, columnWidth);
					// Column Breaks
					if (columnBreak) {
						int breakAtColumn = columnAt;
						addColumnBreaks(sheet, (++breakAtColumn));
					}
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					columnAt++;
					columnName = TeaminologyEnum.DEPRECATED_SOURCE1.getValue();
					sheet.setColumnView(columnAt, columnWidth);
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
					columnAt++;
					columnName = TeaminologyEnum.DEPRECATED_SOURCE2.getValue();
					sheet.setColumnView(columnAt, columnWidth);
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
					columnAt++;
					columnName = TeaminologyEnum.DEPRECATED_SOURCE3.getValue();
					sheet.setColumnView(columnAt, columnWidth);
					addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
	
					//if (sourceTermLangSet != null)
						for (String lang : sourceTermLangSet) {
							columnAt++;
							columnName = lang;
							columnWidth = 16;
							columnName = lang + TeaminologyEnum.FINAL.getValue();
							sheet.setColumnView(columnAt, columnWidth);
							addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
							columnAt++;
							columnWidth = 16;
							columnName = lang + TeaminologyEnum.DEPRECATED_TARGET1.getValue();
							sheet.setColumnView(columnAt, columnWidth);
							addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
							columnAt++;
							columnWidth = 16;
							columnName = lang + TeaminologyEnum.DEPRECATED_TARGET2.getValue();
							sheet.setColumnView(columnAt, columnWidth);
							addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
							columnAt++;
							columnWidth = 16;
							columnName = lang + TeaminologyEnum.DEPRECATED_TARGET3.getValue();
							sheet.setColumnView(columnAt, columnWidth);
							addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
						}
	
				}
				if (exportObjList.get(0) instanceof LanguageReportData) {
					for (LanguageReportDataEnum column : LanguageReportDataEnum.values()) {
						String columnName = column.getColumnName();
						Integer rowAt = column.getRowPosition();
						Integer columnAt = column.getColumnPosition();
						Boolean columnBreak = column.getColumnBreak();
						Integer columnWidth = column.getColumnWidth();
	
						sheet.setColumnView(columnAt, columnWidth);
						// Column Breaks
						if (columnBreak) {
							int breakAtColumn = columnAt;
							addColumnBreaks(sheet, (++breakAtColumn));
						}
						addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
					}
				}
	
				if (exportObjList.get(0) instanceof LanguagePiChartData) {
					for (LanguagePiChartDataEnum column : LanguagePiChartDataEnum.values()) {
						String columnName = column.getColumnName();
						Integer rowAt = column.getRowPosition();
						Integer columnAt = column.getColumnPosition();
						Boolean columnBreak = column.getColumnBreak();
						Integer columnWidth = column.getColumnWidth();
	
						sheet.setColumnView(columnAt, columnWidth);
						// Column Breaks
						if (columnBreak) {
							int breakAtColumn = columnAt;
							addColumnBreaks(sheet, (++breakAtColumn));
						}
						addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
					}
				}
				if (exportObjList.get(0) instanceof User) {
					for (UserDataEnum column : UserDataEnum.values()) {
						String columnName = column.getColumnName();
						Integer rowAt = column.getRowPosition();
						Integer columnAt = column.getColumnPosition();
						Boolean columnBreak = column.getColumnBreak();
						Integer columnWidth = column.getColumnWidth();
	
						sheet.setColumnView(columnAt, columnWidth);
						// Column Breaks
						if (columnBreak) {
							int breakAtColumn = columnAt;
							addColumnBreaks(sheet, (++breakAtColumn));
						}
						addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
					}
				}
				if (exportObjList.get(0) instanceof LanguageReportTable) {
					for (LanguageDataReportEnum column : LanguageDataReportEnum.values()) {
						String columnName = column.getColumnName();
						Integer rowAt = column.getRowPosition();
						Integer columnAt = column.getColumnPosition();
						Boolean columnBreak = column.getColumnBreak();
						Integer columnWidth = column.getColumnWidth();
	
						sheet.setColumnView(columnAt, columnWidth);
						// Column Breaks
						if (columnBreak) {
							int breakAtColumn = columnAt;
							addColumnBreaks(sheet, (++breakAtColumn));
						}
						addLabel(sheet, columnAt, rowAt, columnName, cellFormat);
					}
				}
	
			}
		}
	
	
		/**
		 * To get font settings
		 *
		 * @param isBold
		 * @return WritableFont
		 */
		private WritableFont getFontSettings(Boolean isBold) {
			WritableFont font = null;
			FontName fontName = WritableFont.createFont(DEFAULT_FONT_NAME);
			if (isBold) {
				font = new WritableFont(fontName, DEFAULT_FONT_SIZE, WritableFont.BOLD);
			} else {
				font = new WritableFont(fontName, DEFAULT_FONT_SIZE, WritableFont.NO_BOLD);
			}
			return font;
		}
	
		/**
		 * To get font cell format
		 *
		 * @param isBold
		 * @return WritableCellFormat
		 */
	
		private WritableCellFormat getFontCellFormat(Boolean isBold)
				throws Exception {
			WritableFont font = getFontSettings(isBold);
			WritableCellFormat cellFormat = new WritableCellFormat(font);
			return cellFormat;
		}
	
	
		/**
		 * To  add column breaks
		 *
		 * @param sheet-                Excel sheet
		 * @param breakAtColumn-Integer where excel colum to be break to print in next page
		 */
		private static void addColumnBreaks(WritableSheet sheet, Integer breakAtColumn)
				throws Exception {
			if (sheet != null && breakAtColumn != null) {
				sheet.addColumnPageBreak(breakAtColumn);
			}
		}
	
	
		/**
		 * To add label
		 *
		 * @param sheet-      Excel sheet
		 * @param column-     Column index
		 * @param row         - Row inded
		 * @param cellFormat- Cell format
		 */
		private void addLabel(WritableSheet sheet, int column, int row, String s,
				WritableCellFormat cellFormat) throws WriteException, RowsExceededException {
			Label label;
			if (cellFormat != null) {
				label = new Label(column, row, s, cellFormat);
			} else {
				label = new Label(column, row, s);
			}
			sheet.addCell(label);
		}
	
		 @Transactional
		    private <C extends TeaminologyObject> void writeColumnNames(WritableSheet sheet, List<C> exportObjList, String  topSugCheck, String alterSugCheck, String votesCheck ) throws Exception {
		    	 WritableCellFormat cellFormat = getFontCellFormat(Boolean.FALSE);
		    	       
		    	       
		    		   addLabel(sheet, 0, 1, "Source", cellFormat); 
		    		   sheet.setColumnView(0, 8);
		    		   addLabel(sheet,1,1,"Target Language",cellFormat);
		    		   sheet.setColumnView(1, 8);
		               Boolean columnBreak =true;
		               if(!topSugCheck.isEmpty() && !alterSugCheck.isEmpty() &&  !votesCheck.isEmpty()){
		               if(!topSugCheck.isEmpty() ){
		            	   addLabel(sheet,2,1,"Top Suggestion",cellFormat);
		            	   sheet.setColumnView(0, 8);
		               }
		               if(!alterSugCheck.isEmpty()){
		            	   addLabel(sheet,3,1,"Alternate Suggestion",cellFormat);
		            	   sheet.setColumnView(0, 8);
		               }
		               if(!votesCheck.isEmpty()){
		            	   addLabel(sheet,4,1,"No.Of Votes",cellFormat);
		            	   sheet.setColumnView(0, 8);
		               }
		               } 
		               if(!topSugCheck.isEmpty() && (alterSugCheck.isEmpty() && votesCheck.isEmpty())){
		            	   addLabel(sheet,2,1,"Top Suggestion",cellFormat);
		            	   sheet.setColumnView(0, 8);
		               }
		               if(!alterSugCheck.isEmpty() && (topSugCheck.isEmpty() && votesCheck.isEmpty())){
		            	   addLabel(sheet,2,1,"Alternate Suggestion",cellFormat);
		            	   sheet.setColumnView(0, 8);
		               }
		               
		               if(!votesCheck.isEmpty() && (alterSugCheck.isEmpty() && topSugCheck.isEmpty())){
		            	   addLabel(sheet,2,1,"No.Of Votes",cellFormat);
		            	   sheet.setColumnView(0, 8);
		               }
		               
		               if((!topSugCheck.isEmpty() && !alterSugCheck.isEmpty()) && votesCheck.isEmpty()){
		            	   addLabel(sheet,2,1,"Top Suggestion",cellFormat);
		            	   addLabel(sheet,3,1,"Alternate Suggestion",cellFormat);
		            	   sheet.setColumnView(0, 8);
		               }
		               
		               if((!topSugCheck.isEmpty() && !votesCheck.isEmpty()) && alterSugCheck.isEmpty()){
		            	   addLabel(sheet,2,1,"Top Suggestion",cellFormat);
		            	   addLabel(sheet,3,1,"No.Of Votes",cellFormat);
		            	   sheet.setColumnView(0, 8);
		               }
		               
		               if((!alterSugCheck.isEmpty() && !votesCheck.isEmpty()) && topSugCheck.isEmpty()){
		            	   addLabel(sheet,2,1,"Alternate Suggestion",cellFormat);
		            	   addLabel(sheet,3,1,"No.Of Votes",cellFormat);
		            	   sheet.setColumnView(0, 8);
		               }
		                    /*// Column Breaks
		                 if (columnBreak) {
		                     int breakAtColumn = 0;
		                     addColumnBreaks(sheet, (++breakAtColumn));
		                 }*/
		             }
		    
		    @Transactional	
		    private  <C extends TeaminologyObject> void writeColumnNames(WritableSheet sheet) throws Exception{
		    	 WritableCellFormat cellFormat = getFontCellFormat(Boolean.FALSE);
		    	 addLabel(sheet, 0, 1, "No Data Available", cellFormat); 
		    	 addLabel(sheet, 1, 1, "", cellFormat); 
		    	 addLabel(sheet, 2, 1, "", cellFormat); 
		    	 addLabel(sheet, 3, 1, "", cellFormat); 
		    	 sheet.setColumnView(0, 8);
		    }
		
	
		/**
		 * To populate sheet data
		 *
		 * @param sheet          - Excel sheet
		 * @param exportObjList- Object list to be export
		 */
		@Transactional
		private <C extends TeaminologyObject> void populateSheetData(WritableSheet sheet, List<C> exportObjList) {
			try {
				if(exportObjList instanceof TermInformationTo ){
					Collections.sort(exportObjList, new Comparator<C>()
							{
						@Override
						public int compare(C o1, C o2) {
	
							TermInformationTo obj1 = (TermInformationTo) o1;
							TermInformationTo obj2 = (TermInformationTo) o2;
							return obj1.getTermBeingPolled().compareToIgnoreCase(obj2.getTermBeingPolled());
						}
	
							});
				}
				int row = startRow;
				int col = startCol;
				int tempCol = 0;
	
				//Read the format from excelSheet
				WritableCellFormat excelCellNumberFormat = getExcelCustomDollarFormat();
				WritableCellFormat cellFormat = getFontCellFormat(Boolean.FALSE);
				writeColumnNames(sheet, exportObjList);
				for (Object expObj : exportObjList) {
					if (expObj instanceof Terms) {
	
						Terms terms = (Terms) expObj;
	
						String interval = terms.getInterval();
						String termPerInterval = terms.getTermsPerInterval() != null ? "\"" + NUMBER_DEC_ONE.format(terms.getTermsPerInterval()) + "\"" : null;
	
						col = 0; // Reset the column count
	
	
						addLabel(sheet, col, row, termPerInterval, cellFormat);
						col++;
						addLabel(sheet, col, row, interval, cellFormat);
						col++;
						row++;
					}
					if (expObj instanceof LanguageReportData) {
	
						LanguageReportData languageReportData = (LanguageReportData) expObj;
	
						String language = languageReportData.getLanguage().getLanguageLabel();
						List<Terms> termsList = languageReportData.getTermsList();
						for (Terms terms : termsList) {
							col = 0; // Reset the column count
							String month = terms.getInterval() + " " + terms.getYear();
							String number = "";
							if (terms.getTermsPerInterval() != null) {
								number = terms.getTermsPerInterval().toString();
							}
							addLabel(sheet, col, row, month, cellFormat);
							col++;
							addLabel(sheet, col, row, number, cellFormat);
							col++;
							addLabel(sheet, col, row, language, cellFormat);
							col++;
							row++;
						}
	
					}
					if (expObj instanceof LanguagePiChartData) {
	
						LanguagePiChartData languagePiChartData = (LanguagePiChartData) expObj;
	
						String language = languagePiChartData.getLanguage();
						String noOfTerms = "\"" + NUMBER_DEC_ONE.format(languagePiChartData.getNoOfTerms()) + "\"";
						col = 0; // Reset the column count
						addLabel(sheet, col, row, language, cellFormat);
						col++;
						addLabel(sheet, col, row, noOfTerms, cellFormat);
						col++;
						row++;
	
					}
					if (expObj instanceof User) {
	
						User user = (User) expObj;
						String firstName = user.getFirstName();
						String lastName = user.getLastName();
						String userName = user.getUserName();
						String email = user.getEmailId();
						String password = null;
						String userCompany = user.getCompany() == null ? null : user.getCompany().getCompanyName();
						Set<UserRole> userRoleList = user.getUserRole();
						Set<UserLanguages> userLanguagesList = user.getUserLanguages();
						String createdDate = user.getCreateTime() == null ? null : new SimpleDateFormat("MM -dd-yyyy").format(user.getCreateTime());
	
						String languages = "";
						if (userLanguagesList != null) {
							for (UserLanguages userLanguages : userLanguagesList) {
								languages += userLanguages.getLanguages().getLanguageLabel() + ", ";
							}
						}
						String userType = "";
						if (userRoleList != null) {
							for (UserRole userRole : userRoleList) {
								userType = userType + userRole.getRole().getRoleName() + ",";
							}
							userType = userType.substring(0, userType.lastIndexOf(","));
						}
						if (languages.contains(","))
							languages = languages.substring(0, languages.lastIndexOf(","));
						languages = "\"" + languages + "\"";
						col = 0; // Reset the column count
						addLabel(sheet, col, row, firstName, cellFormat);
						col++;
						addLabel(sheet, col, row, lastName, cellFormat);
						col++;
						addLabel(sheet, col, row, userName, cellFormat);
						col++;
						addLabel(sheet, col, row, email, cellFormat);
						col++;
						addLabel(sheet, col, row, userType, cellFormat);
						col++;
						addLabel(sheet, col, row, languages, cellFormat);
						col++;
						addLabel(sheet, col, row, userCompany, cellFormat);
						col++;
						addLabel(sheet, col, row, password, cellFormat);
						col++;
						addLabel(sheet, col, row, createdDate, cellFormat);
						col++;
						row++;
	
					}
					if (expObj instanceof LanguageReportTable) {
	
						LanguageReportTable languageReportTable = (LanguageReportTable) expObj;
	
						String languageTeam = languageReportTable.getLanguageLabel();
						String members = languageReportTable.getMembers() == null ? "0" : languageReportTable.getMembers().toString();
						String accuracy = languageReportTable.getAccuracy() == null ? "0" : languageReportTable.getAccuracy().toString() + " %";
						String terms = languageReportTable.getTotalTerms() == null ? "0" : "\"" + NUMBER_DEC_ONE.format(languageReportTable.getTotalTerms()) + "\"";
						String debatedTerms = languageReportTable.getDebatedTerms() == null ? "0" : languageReportTable.getDebatedTerms().toString();
						String activePolls = languageReportTable.getActivePolls() == null ? "0" : languageReportTable.getActivePolls().toString();
						String mothlyAvg = languageReportTable.getMonthlyAvg() == null ? "0" : languageReportTable.getMonthlyAvg().toString();
						String totalVotes = languageReportTable.getTotalVotes() == null ? "0" : languageReportTable.getTotalVotes().toString();
						col = 0; // Reset the column count
						addLabel(sheet, col, row, languageTeam, cellFormat);
						col++;
						addLabel(sheet, col, row, members, cellFormat);
						col++;
						addLabel(sheet, col, row, accuracy, cellFormat);
						col++;
						addLabel(sheet, col, row, terms, cellFormat);
						col++;
						addLabel(sheet, col, row, debatedTerms, cellFormat);
						col++;
						addLabel(sheet, col, row, activePolls, cellFormat);
						col++;
						addLabel(sheet, col, row, mothlyAvg, cellFormat);
						col++;
						addLabel(sheet, col, row, totalVotes, cellFormat);
						col++;
						row++;
	
					}
					if (expObj instanceof TermInformationTo) {
						TermInformationTo termInformation = (TermInformationTo) expObj;
						Status status = new Status();
						ConceptCategory conceptCat = new ConceptCategory();
						ProductGroup conceptProdGrp = new ProductGroup();
						Program program = new Program();
						Language language = new Language();
						List<String> lanaguageLableList = new ArrayList<String>();
						List<String> tempList=new ArrayList<String>();
						if (sourceTermLangSet != null) {
							for (String lang : sourceTermLangSet) {
								lanaguageLableList.add(lang);
							}
						}
	
						if (termInformation.getTermStatusId() != null && termInformation.getTermStatusId().toString().trim().length() != 0)
							status = lookUpDAO.getStatus(termInformation.getTermStatusId());
						if (termInformation.getConceptCatId() != null && termInformation.getConceptCatId().toString().trim().length() != 0)
							conceptCat = lookUpDAO.getConceptCategory(termInformation.getConceptCatId());
						if (termInformation.getConceptProdGroup() != null && termInformation.getConceptProdGroup().toString().trim().length() != 0)
							conceptProdGrp = lookUpDAO.getProductGroup(termInformation.getConceptProdGroup());
						if (termInformation.getSuggestedTermPgmId() != null && termInformation.getSuggestedTermPgmId().toString().trim().length() != 0)
							program = lookUpDAO.getProgram(termInformation.getSuggestedTermPgmId());
	
						List<Integer> suggestedTermPosIds = termInformation.getSuggestedTermPosIds();
						List<Integer> suggestedTermLangIds = termInformation.getSuggestedTermLangIds();
						List<String> suggestedtargetTerms = termInformation.getSuggestedTerms();
						List<Integer> targetTermStatusIds = termInformation.getSuggestedTermStatusIds();
						List<Integer> suggestedTargetFormIds = termInformation.getSuggestedTermFormId();
						List<Set<DeprecatedTermInformation>> targetdeptermlist = termInformation.getTargetDepterInfoList();
						Set<DeprecatedTermInformation> deprecatedTermInfoset = new HashSet<DeprecatedTermInformation>();
						String  conceptDefinition =( termInformation.getConceptDefinition() == null)? null : termInformation.getConceptDefinition().replace("\"", "\"\""); 
						conceptDefinition = conceptDefinition == null ? null : "\"" + conceptDefinition + "\""; 
						String conceptNotes = termInformation.getConceptNotes() == null ? null :  termInformation.getConceptNotes().replace("\"", "\"\"");
						conceptNotes = conceptNotes == null ? null : "\"" + conceptNotes + "\""; 
						String conceptCategory = (conceptCat != null) ? ((conceptCat.getConceptCategory())==null?null:conceptCat.getConceptCategory().replace("\"", "\"\"")) : "";
						conceptCategory = conceptCategory == null ? null : "\"" + conceptCategory + "\""; 
						String conceptPrdGrp = (conceptProdGrp != null) ? ((conceptProdGrp.getProduct())==null?null:conceptProdGrp.getProduct().replace("\"", "\"\"")) : "";
						conceptPrdGrp = conceptPrdGrp == null ? null : "\"" + conceptPrdGrp + "\""; 
						String sourceTerm = termInformation.getTermBeingPolled() == null ? null : termInformation.getTermBeingPolled().replace("\"", "\"\"");
						sourceTerm = sourceTerm == null ? null : "\"" + sourceTerm + "\""; 
						String termStatus = (status != null) ? status.getStatus() : "";
						String termForm = (termInformation.getTermForm() != null) ? termInformation.getTermForm().getFormName() : "";
						String termPOS = (termInformation.getTermPOS() != null) ? termInformation.getTermPOS().getPartOfSpeech() : "";                                  
						String termUsage = termInformation.getTermUsage() == null ? null : termInformation.getTermUsage().replace("\"", "\"\""); 
						termUsage = termUsage == null ? null : "\"" + termUsage + "\""; 
						String  termCategory =( termInformation.getTermCategory() == null)? null : termInformation.getTermCategory().getCategory().replace("\"", "\"\""); 
						termCategory = termCategory == null ? null : "\"" + termCategory + "\"";                 
						String termNotes = termInformation.getTermNotes() == null ? null :  termInformation.getTermNotes().replace("\"", "\"\"");
						termNotes=termNotes==null?null:"\""+termNotes+"\"";
						String termPrg = (termInformation.getTermProgram() != null) ? termInformation.getTermProgram().getProgramName().replace("\"", "\"\"") : "";
						termPrg = termPrg == null ? null : "\"" + termPrg + "\""; 
						String termCompany = (termInformation.getCompany() != null) ? termInformation.getCompany().getCompanyName().replace("\"", "\"\"") : "";
						termCompany = termCompany == null ? null : "\"" + termCompany + "\""; 
						String termDomain = (termInformation.getTermDomain() != null) ? termInformation.getTermDomain().getDomain().replace("\"", "\"\"") : "";
						termDomain = termDomain == null ? null : "\"" + termDomain + "\""; 
						String targetTermUsage = termInformation.getSuggestedTermUsage()==null?null: termInformation.getSuggestedTermUsage().replace("\"", "\"\"");
						targetTermUsage = targetTermUsage == null ? null : "\"" + targetTermUsage + "\""; 
						String targetTermNotes = termInformation.getSuggestedTermNotes()==null?null: termInformation.getSuggestedTermNotes().replace("\"", "\"\"");
						targetTermNotes = targetTermNotes == null ? null : "\"" + targetTermNotes + "\""; 
						String targetTermPrg = (program != null) ? ((program.getProgramName())==null?null:program.getProgramName().replace("\"", "\"\"")) : "";
						targetTermPrg = targetTermPrg == null ? null : "\"" + targetTermPrg + "\""; 
						String finalValue=null;
						String comments = termInformation.getComments();
						int targets=lanaguageLableList.size();
						//Writing column data to excel
						col = 0; // Reset the column count
						addLabel(sheet, col, row, sourceTerm, cellFormat);
						col++;
						if (sourceTermLangSet != null) {
							for (String lang : sourceTermLangSet) {
								tempList.add(lang);
							}
						}
						for (int i = 0; i < termInformation.getSuggestedTermLangIds().size(); i++) {
							col = 1;
							String suggestedTargetTerm = null;
							String lanaguageLabel = null;
							String languageStr = null;
							if (suggestedTermLangIds != null && !suggestedTermLangIds.isEmpty()) {
								Integer id = suggestedTermLangIds.get(i);
								language = lookUpDAO.getLanguage(id);
								lanaguageLabel = language == null ? null : language.getLanguageLabel();
							}
							// Collections.sort( suggestedTermLangIds );
							if (suggestedtargetTerms != null && !suggestedtargetTerms.isEmpty()) {
								suggestedTargetTerm =suggestedtargetTerms.get(i)==null?null:suggestedtargetTerms.get(i).replace("\"", "\"\"");
								suggestedTargetTerm = suggestedTargetTerm == null ? null : "\"" + suggestedTargetTerm + "\""; 
							}
							for (int j=0; j < lanaguageLableList.size();j++) {
								if (j < (lanaguageLableList.size())) {
									languageStr = lanaguageLableList.get(j);
								}
								if (lanaguageLabel != null && lanaguageLabel.equalsIgnoreCase(languageStr)) {
									addLabel(sheet, col, row, suggestedTargetTerm, cellFormat);
									col++;
									//j++;
									break;
								} else{
									col++;
								}
							}
						}
						col=targets+1;
						addLabel(sheet, col, row, conceptDefinition, cellFormat);
						col++;
						addLabel(sheet, col, row, termUsage, cellFormat);
						col++;
						addLabel(sheet, col, row, termNotes, cellFormat);
						col++;
						addLabel(sheet, col, row, termCategory, cellFormat);
						col++;
						addLabel(sheet, col, row, termPOS, cellFormat);
						col++;
						addLabel(sheet, col, row, termForm, cellFormat);
						col++;
						addLabel(sheet, col, row, termStatus, cellFormat);
	
						tempCol=col;
						for (int i = 0; i < termInformation.getSuggestedTermLangIds().size(); i++) {
							col=tempCol;
							String suggestedTargetTerm = null;
							String targetTermPOS = null;
							String lanaguageLabel = null;
							String languageStr = null;
							if (suggestedTermLangIds != null && !suggestedTermLangIds.isEmpty()) {
								Integer id = suggestedTermLangIds.get(i);
								language = lookUpDAO.getLanguage(id);
								lanaguageLabel = language == null ? null : language.getLanguageLabel();
							}
							if (suggestedTermPosIds != null && !suggestedTermPosIds.isEmpty()) {
								Integer id = suggestedTermPosIds.get(i);
								PartsOfSpeech partOfSpeechObj = lookUpDAO.getPartsOfSpeech(id);
								targetTermPOS = (partOfSpeechObj != null) ? partOfSpeechObj.getPartOfSpeech() : "";
							}
							for (int j=0; j < lanaguageLableList.size();j++) {
								if (j < (lanaguageLableList.size())) {
									languageStr = lanaguageLableList.get(j);
								}
								if (lanaguageLabel != null && lanaguageLabel.equalsIgnoreCase(languageStr)) {
									col++;
									addLabel(sheet, col, row, targetTermPOS, cellFormat);
									//j++;
									break;
								} else {
									col++;
								}
							}
						}
						col=tempCol+targets;
						tempCol=col;
						for (int i = 0; i < termInformation.getSuggestedTermLangIds().size(); i++) {
							col=tempCol;
							String suggestedTargetTerm = null;
							String targetTermStatus = null;
							String lanaguageLabel = null;
							String languageStr = null;
							if (suggestedTermLangIds != null && !suggestedTermLangIds.isEmpty()) {
								Integer id = suggestedTermLangIds.get(i);
								language = lookUpDAO.getLanguage(id);
								lanaguageLabel = language == null ? null : language.getLanguageLabel();
							}
							if (targetTermStatusIds != null && !targetTermStatusIds.isEmpty()) {
								Integer id = targetTermStatusIds.get(i);
								Status statusObj = lookUpDAO.getStatus(id);
								targetTermStatus = (statusObj != null) ? statusObj.getStatus() : "";
							}
							for (int j=0; j < lanaguageLableList.size();j++) {
								if (j < (lanaguageLableList.size())) {
									languageStr = lanaguageLableList.get(j);
								}
								if (lanaguageLabel != null && lanaguageLabel.equalsIgnoreCase(languageStr)) {
									col++;
									addLabel(sheet, col, row, targetTermStatus, cellFormat);
									break;
	
								}  else {
									col++;
								}
							}
						}
						col=tempCol+targets;
						col++;
						addLabel(sheet, col, row, comments, cellFormat);
						col++;
						addLabel(sheet, col, row, termCompany, cellFormat);
						col++;
						addLabel(sheet, col, row, termDomain, cellFormat);
						col++;
						if (termInformation.getDeprecatedsouceSet() != null) {
							for (int l = 0; l < 3; l++) {
								if (l < (termInformation.getDeprecatedsouceSet().size())) {
									String source = termInformation.getDeprecatedsouceSet().get(l);
									addLabel(sheet, col, row, (source != null) ? source : "", cellFormat);
								}
								col++;
							} 
						} else {
							col = col + 3;
						}
						tempCol=col;
						for (int i = 0; i < termInformation.getSuggestedTermLangIds().size(); i++) {
							col=tempCol;
							String lanaguageLabel = null;
							String languageStr = null;
							String deprecatedSource = null;
							String suggestedTargetTerm = null;
							String targetTermStatus = null;
							List<DeprecatedTermInformation> deprecatedTermInfoList = new ArrayList<DeprecatedTermInformation>();
	
							if (suggestedTermLangIds != null && !suggestedTermLangIds.isEmpty()) {
								Integer id = suggestedTermLangIds.get(i);
								language = lookUpDAO.getLanguage(id);
								lanaguageLabel = language == null ? null : language.getLanguageLabel();
							}
							if (targetTermStatusIds != null && !targetTermStatusIds.isEmpty()) {
								Integer id = targetTermStatusIds.get(i);
								if(id != null){
									if(id == 2)
										finalValue="Yes";
									else
                                	   finalValue="No";
								}
								else{
									finalValue="No";
								}
							}else{
								finalValue="No";
							}
							if (targetdeptermlist != null) {
								deprecatedTermInfoset = targetdeptermlist.get(i);
								for (DeprecatedTermInformation deprecatedTermInformation : deprecatedTermInfoset) {
									deprecatedSource = deprecatedTermInformation.getDeprecatedSource();
									deprecatedTermInfoList.add(deprecatedTermInformation);
								}
							}
	
							for (int k=0; k < lanaguageLableList.size(); k++) {
								if (k < (lanaguageLableList.size())) {
									languageStr = lanaguageLableList.get(k);
								}
								if (lanaguageLabel != null && lanaguageLabel.equalsIgnoreCase(languageStr)) {
									addLabel(sheet, col, row,finalValue, cellFormat);
									col++;
									for (int l = 0; l < 3; l++) {
										if (l < (deprecatedTermInfoList.size())) {
											String target = deprecatedTermInfoList.get(l).getDeprecatedTarget();
											addLabel(sheet, col, row, (target != null) ? target : "", cellFormat);
										}
										col++;
									}
									break;
								} else {
									col = col+4;
								}
							}
						}
						row++;
					}
				}
	
			}
			catch (Exception e) {
				e.printStackTrace();
	
			}
		}
	
	
		@Transactional
		private <C extends TeaminologyObject> void populateVoteSheetData(WritableSheet sheet, List<C> exportObjList, List params) {
			try {
				String topSugCheck =(String)params.get(0);
				String alterSugCheck  =(String)params.get(1);
				String votesCheck  =(String)params.get(2);
				String fromDate  =(String)params.get(3);
				String toDate =(String)params.get(4);
				String flag =(String)params.get(5);
				int row = startRow;
				int col = startCol;
				int tempCol = 0;
				String alterSug="";
				String altervotes="";
				Date firstDate=null;
				String formatedFromDate=null;
				Date secondDate=null;
				String formatedToDate=null;
				String date=null;
				String check=null;
				String checkNotSelected="No checkboxes  or proper date range  selected";
	
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				if(!fromDate.isEmpty()){
					firstDate =(Date)formatter.parse(fromDate);
					formatedFromDate=firstDate.toString();
				}
				if(!toDate.isEmpty()){
					secondDate=(Date)formatter.parse(toDate);
					formatedToDate=secondDate.toString();
				}
				if(formatedFromDate !=null  && formatedToDate !=null)
					date="For voting period ("+formatedFromDate.substring(4,10)+" - "+formatedToDate.substring(4,10) +")";
				WritableCellFormat excelCellNumberFormat = getExcelCustomDollarFormat();
				WritableCellFormat cellFormat = getFontCellFormat(Boolean.FALSE);
				row=0;
				if((!topSugCheck.isEmpty() || !alterSugCheck.isEmpty()  || !votesCheck.isEmpty()) &&  (!fromDate.isEmpty() && !toDate.isEmpty())) {
					addLabel(sheet, col, row, date, cellFormat);
				}
				else
				{
					addLabel(sheet, col, row, checkNotSelected, cellFormat);	
				}
				if(topSugCheck.isEmpty() && alterSugCheck.isEmpty()  && votesCheck.isEmpty()){
					check="empty";
				}
				if(flag=="Y" && check != "empty" ){
					writeColumnNames(sheet, exportObjList,  topSugCheck, alterSugCheck,  votesCheck);
					row=2;
					for(int i=0; i<exportObjList.size();i++)  {
						col=0;
						TermVoteDetails alterSugObj=null;
						String  alterSugVotes="0";
						Integer totalAlterVotes=0;
						Integer count=0;
						Integer totlaTerms=exportObjList.size();
						for(int l=0; l<exportObjList.size(); l++){
							TermVotingTo terms1 = (TermVotingTo) exportObjList.get(i);
							List<TermVoteDetails> checkAlterSugg=terms1.getAlternativeSuggestion();
							if(checkAlterSugg == null){
								count++;
							}	
						}
						TermVotingTo terms = (TermVotingTo) exportObjList.get(i);
	
						String source= terms == null ? null : terms.getSource()==null ? null : terms.getSource().replace("\"", "\"\"");
						source=source == null ? null : "\"" + source +"\""; 
						TermVoteDetails   topSuggestionObj = terms == null ?  null : terms.getTopSuggestion()== null ? null : terms.getTopSuggestion();
						String topSug = topSuggestionObj == null ? null : topSuggestionObj.getTransalation() == null ? null : topSuggestionObj.getTransalation().replace("\"", "\"\""); 
						topSug = topSug == null ? null : "\"" + topSug + "\""; 
						String topSugVotes = topSuggestionObj == null ? "0" : topSuggestionObj.getNoOfVotes()== null ? "0" : topSuggestionObj.getNoOfVotes().toString();
						String language = terms == null ? null : terms.getLanguage()== null ? null : terms.getLanguage().replace("\"", "\"\"");
						language = language == null ? null : "\"" + language + "\""; 
						List<TermVoteDetails> alterTranObj =  terms == null ?  null : terms.getAlternativeSuggestion() == null ? null :  terms.getAlternativeSuggestion();
	
						if(!topSugCheck.isEmpty()){
							addLabel(sheet, col, row, source, cellFormat);
							col++;
							addLabel(sheet, col, row, language, cellFormat);
							col++;
							addLabel(sheet, col, row, topSug, cellFormat);
							col++;
							if(!votesCheck.isEmpty() && !alterSugCheck.isEmpty() ){
								col++;
								addLabel(sheet, col, row, topSugVotes, cellFormat);
							}
							if(!votesCheck.isEmpty()){
								addLabel(sheet, col, row, topSugVotes, cellFormat);
							}
							row++;
						}	
	
						if(!alterSugCheck.isEmpty()){
							List<TermVoteDetails> alternateSuggestion= terms.getAlternativeSuggestion()== null ? null : terms.getAlternativeSuggestion();
							if(topSugCheck.isEmpty() && votesCheck.isEmpty())
							{
								if(totlaTerms == count){
									row=1;
									writeColumnNames(sheet);
								}
							}
							if(topSugCheck.isEmpty() && !votesCheck.isEmpty())
							{
								if(totlaTerms == count){
									row=1;
									writeColumnNames(sheet);
								}
							}
							if(alternateSuggestion != null){
								for(int j=0;j<alternateSuggestion.size();j++) {
									TermVoteDetails alter = alternateSuggestion == null ? null : alternateSuggestion.get(j);
									alterSug= alter == null ? null : alter.getTransalation() == null ? null : alter.getTransalation().replace("\"", "\"\"");
									alterSug= alterSug == null ? null : "\"" + alterSug +"\"";
									altervotes = alter == null ? "0" : alter.getNoOfVotes()== null ? "0" : alter.getNoOfVotes().toString();
	
									if(!topSugCheck.isEmpty() && !votesCheck.isEmpty() && !alterSugCheck.isEmpty()){
										col=0; // Reset the colum n count	
										addLabel(sheet, col, row, source, cellFormat);
										col++;
										addLabel(sheet, col, row, language, cellFormat);
										col++;
										col++;
										addLabel(sheet, col, row, alterSug, cellFormat);
										col++;
										if(!votesCheck.isEmpty()){
											addLabel(sheet, col, row, altervotes, cellFormat);
										}
										row++;
									}
	
									if((topSugCheck.isEmpty() && !votesCheck.isEmpty()) ){
										col=0; 	
										addLabel(sheet, col, row, source, cellFormat);
										col++;
										addLabel(sheet, col, row, language, cellFormat);
										col++;
										addLabel(sheet, col, row, alterSug, cellFormat);
										col++;
										if(!votesCheck.isEmpty()){
											addLabel(sheet, col, row, altervotes, cellFormat);
										}
										row++;
									}
	
									if((!topSugCheck.isEmpty() && votesCheck.isEmpty())){
	
										col=0; 
										addLabel(sheet, col, row, source, cellFormat);
										col++;
										addLabel(sheet, col, row, language, cellFormat);
										col++;
										col++;
										addLabel(sheet, col, row, alterSug, cellFormat);
										row++;
									}
	
									if(topSugCheck.isEmpty() && votesCheck.isEmpty() ){
										col=0; 	
										addLabel(sheet, col, row, source, cellFormat);
										col++;
										addLabel(sheet, col, row, language, cellFormat);
										col++;
										addLabel(sheet, col, row, alterSug, cellFormat);
										col++;
										if(!votesCheck.isEmpty()){
											addLabel(sheet, col, row, altervotes, cellFormat);
										}
										row++;
									}
								}
							}
						}
						if(!votesCheck.isEmpty()){
							if(alterSugCheck.isEmpty()){
								if(topSugCheck.isEmpty()){
									col=0;
									addLabel(sheet, col, row, source, cellFormat);
									col++;
									addLabel(sheet, col, row, language, cellFormat);
									col++;
									if(altervotes.isEmpty()){
										altervotes="0";
									}
									if(topSugVotes.isEmpty()){
										topSugVotes="0";
									}
									if(alterTranObj!=null){
										for(int k=0;k<alterTranObj.size();k++) {
											alterSugObj=alterTranObj.get(k);
											alterSugVotes = alterSugObj.getNoOfVotes();
											totalAlterVotes=totalAlterVotes+Integer.parseInt(alterSugVotes);
										}
									}
									String totalVotes=String.valueOf(totalAlterVotes+Integer.parseInt(topSugVotes));
									addLabel(sheet, col, row, totalVotes, cellFormat);
									row++;
								}
							}
						}
					}
				} 
				else{
					row=1;
					writeColumnNames(sheet);
				}
			}             
			catch (Exception e) {
				e.printStackTrace();
			}
		}  
	
		/**
		 * To get excel custom dollar format
		 *
		 * @return WritableCellFormat
		 */
		private WritableCellFormat getExcelCustomDollarFormat() {
			String format = DOLLAR_EXCEL_FORMAT;
			NumberFormat numberFormat = new NumberFormat(format, NumberFormat.COMPLEX_FORMAT);
			WritableFont wFont = getFontSettings(Boolean.FALSE);
			WritableCellFormat writableNumberFormat = new WritableCellFormat(wFont, numberFormat);
			return writableNumberFormat;
		}
	
		/**
		 * To Tbx convert
		 *
		 * @param uploadFile  -File to be read
		 * @param lookupClass - Object to be read
		 * @return String
		 */
		@Transactional
		private <C extends TeaminologyObject> CSVImportStatus TbxConvert(File uploadFile, Class<C> lookupClass, User user, String companyIds) {
			CSVImportStatus csvImportStatus = new CSVImportStatus();
			List<TermInformation> insertedTermInformationList = new ArrayList<TermInformation>();
			List<TermInformation> updatedTermInformationList = new ArrayList<TermInformation>();
			try {
				String termEntryId = "";
				String errorDisp = "";
				Integer insertedCount = 0;
				Integer rejectedCount = 0;
				Integer updatedCount = 0;
				String languageCode = "";
				String termSaveInformation = null;
				List<String> rejectedTermList = new ArrayList<String>();
				List<Integer> termErrorLineNumber = new ArrayList<Integer>();
				List<Company> companyList = new ArrayList<Company>();
	
				String selectedIdsArray[] = new String[1];
				if (companyIds != null && companyIds.contains(",")) {
					selectedIdsArray = companyIds.split(",");
				} else {
					selectedIdsArray[0] = companyIds;
				}
	
				for (int i = 0; i < selectedIdsArray.length; i++) {
					if (selectedIdsArray[i] != null && !selectedIdsArray[i].isEmpty()) {
						Company company = lookUpDAO.getCompany(Integer.parseInt(selectedIdsArray[i]));
						companyList.add(company);
					}
				}
	
				InputStream inputStream = new FileInputStream(uploadFile);
				Reader reader = new InputStreamReader(inputStream, "UTF-8");
	
				InputSource is = new InputSource(reader);
				is.setEncoding("UTF-8");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				dbFactory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				dbFactory.setValidating(false);
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(is);
				doc.getDocumentElement().normalize();
	
				NodeList termEntryNodeList = doc.getElementsByTagName(TeaminologyEnum.TERM_ENRY.getValue());
	
				for (int termEntryIndex = 0; termEntryIndex < termEntryNodeList.getLength(); termEntryIndex++) {
					Boolean isValidData = true;
					Node termEntryNode = termEntryNodeList.item(termEntryIndex);
					if (termEntryNodeList.item(termEntryIndex).getAttributes().getNamedItem("id") != null) {
						termEntryNodeList.item(termEntryIndex).getAttributes().getNamedItem("id");
					}
					if (termEntryNode.getNodeType() == Node.ELEMENT_NODE) {
						Element termEntryElement = (Element) termEntryNode;
						TermInformation termInformation = new TermInformation();
						NodeList termDescNodeList = termEntryElement.getElementsByTagName(TeaminologyEnum.TERM_DESCRIPTION.getValue());
						if (termDescNodeList != null) {
							for (int termDescNodeListIndex = 0; termDescNodeListIndex < termDescNodeList.getLength(); termDescNodeListIndex++) {
	
								Node descNode = termDescNodeList.item(termDescNodeListIndex);
								if (descNode != null && descNode.getNodeType() == Node.ELEMENT_NODE) {
	
									Element descElement = (Element) descNode;
									for (int descIndex = 0; descIndex < descNode.getChildNodes().getLength(); descIndex++) {
										Node descValueNode = descNode.getChildNodes().item(descIndex);
										if (descElement.getAttribute(TeaminologyEnum.TYPE.getValue()) != null && descElement.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEFINITION.getValue())) {
											if (descValueNode != null && descValueNode.getNodeValue() != null && (descValueNode.getNodeValue().length()) > DBColumnsSizeEnum.CONCEPT_DEFINATION_MAX_SIZE.getIntValue()) {
												isValidData = false;
												rejectedCount++;
												errorDisp = " Concept Defination is too long for term entry '" + termEntryNodeList.item(termEntryIndex).getAttributes().getNamedItem("id").getNodeValue() + "'. Maximum limit is " + DBColumnsSizeEnum.CONCEPT_DEFINATION_MAX_SIZE.getIntValue();
														rejectedTermList.add(errorDisp);
	
											} else {
												if (descValueNode != null && descValueNode.getNodeValue() != null && descValueNode.getNodeValue().length() > 0) {
													termInformation.setConceptDefinition(descValueNode.getNodeValue());
												}
											}
										}
									}
	
	
								}
							}
						}
						NodeList languageNodeList = termEntryElement.getElementsByTagName(TeaminologyEnum.LANG_SET.getValue());
						dcdSource1 = null;
						dcdSource2 = null;
						dcdSource3 = null;
						if (languageNodeList != null) {
							Integer termLength = languageNodeList.getLength();
	
							for (int langIndex = 0; langIndex < languageNodeList.getLength(); langIndex++) {
								Node nNode = languageNodeList.item(langIndex);
								if (nNode.getNodeType() == Node.ELEMENT_NODE) {
									Element eElement = (Element) nNode;
	
									Node n = eElement.getElementsByTagName("term").item(0);
									if (n != null && n.toString().trim().length() > 0) {
										Element e1 = (Element) n;
										termEntryId = e1.getAttribute("id");
									}
									// verifying max size of each column	
									String langCode = eElement.getAttribute(TeaminologyEnum.LANG.getValue());
									if (langCode != null && (langCode.equalsIgnoreCase("en") || langCode.equalsIgnoreCase("en-US") || langCode.equalsIgnoreCase("en_US"))) {
										langCode = TeaminologyEnum.ENGLISH_SOURCE.getValue();
									}
	
									Language language = lookUpDAO.getLanguageByCode(langCode, true);
	
									if (eElement.getElementsByTagName(TeaminologyEnum.TERM.getValue()) != null && eElement.getElementsByTagName(TeaminologyEnum.TERM.getValue()).item(0) != null) {
										NodeList nlList = eElement.getElementsByTagName(TeaminologyEnum.TERM.getValue()).item(0).getChildNodes();
										Node nValue = (Node) nlList.item(0);
										NodeList descList = eElement.getElementsByTagName(TeaminologyEnum.TERM.getValue());
										int descListLength = descList.getLength();
										for (int j = 0; j < descListLength; j++) {
											if ((langCode.equalsIgnoreCase("en") || langCode.equalsIgnoreCase("en-US") || langCode.equalsIgnoreCase(TeaminologyEnum.ENGLISH_SOURCE.getValue())) && language != null) {
												if (nValue != null && nValue.toString().trim().length() > DBColumnsSizeEnum.SOURCE_TERM_MAX_SIZE.getIntValue()) {
													isValidData = false;
	
													errorDisp = "English term  is too long for term entry '" + termEntryId + "'. Maximum limit is " + DBColumnsSizeEnum.SOURCE_TERM_MAX_SIZE.getIntValue();
													rejectedTermList.add(errorDisp);
												}
											} else {
												if (nValue != null && nValue.toString().trim().length() > DBColumnsSizeEnum.TARGET_TERM_MAX_SIZE.getIntValue()) {
													isValidData = false;
													errorDisp = "Target term is too long for term entry '" + termEntryId + "'. Maximum limit is " + DBColumnsSizeEnum.TARGET_TERM_MAX_SIZE.getIntValue();
													rejectedTermList.add(errorDisp);
												}
											}
										}
	
									}
	
									if (eElement.getElementsByTagName(TeaminologyEnum.TERM_NOTE.getValue()) != null && eElement.getElementsByTagName(TeaminologyEnum.TERM_NOTE.getValue()).item(0) != null) {
										NodeList nlList = eElement.getElementsByTagName(TeaminologyEnum.TERM_NOTE.getValue()).item(0).getChildNodes();
										Node nValue = (Node) nlList.item(0);
										NodeList descList = eElement.getElementsByTagName(TeaminologyEnum.TERM_NOTE.getValue());
										int descListLength = descList.getLength();
										for (int j = 0; j < descListLength; j++) {
											Element descEle = (Element) descList.item(j);
											if (langCode.equalsIgnoreCase(TeaminologyEnum.ENGLISH_SOURCE.getValue())) {
												if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM1.getValue())) {
													if (descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null && descEle.getFirstChild().getNodeValue().length() > DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
														errorDisp = "English Deprecated1  is too long for term entry '" + termEntryId + "'. Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
																rejectedTermList.add(errorDisp);
																isValidData = false;
													}
	
												}
												if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM2.getValue())) {
													if (descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null && descEle.getFirstChild().getNodeValue().length() > DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
														errorDisp = "English Deprecated2  is too long for term entry '" + termEntryId + "'. Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
														rejectedTermList.add(errorDisp);
														isValidData = false;
													}
	
												}
												if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM3.getValue())) {
													if (descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null && descEle.getFirstChild().getNodeValue().length() > DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
														errorDisp = "English Deprecated3  is too long for term entry '" + termEntryId + "'. Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
														rejectedTermList.add(errorDisp);
														isValidData = false;
													}
	
												}
											} else {
												if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM1.getValue())) {
													if (descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null && descEle.getFirstChild().getNodeValue().length() > DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
														errorDisp = "Target Deprecated1  is too long for term entry '" + termEntryId + "'. Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
														rejectedTermList.add(errorDisp);
														isValidData = false;
													}
	
												}
												if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM2.getValue())) {
													if (descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null && descEle.getFirstChild().getNodeValue().length() > DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
														errorDisp = "Target Deprecated2  is too long for term entry '" + termEntryId + "'. Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
														rejectedTermList.add(errorDisp);
														isValidData = false;
													}
	
												}
												if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM3.getValue())) {
													if (descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null && descEle.getFirstChild().getNodeValue().length() > DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue()) {
														errorDisp = "Target Deprecated3 is too long for term entry '" + termEntryId + "'. Maximum limit is " + DBColumnsSizeEnum.DEPRECATED_TERM_MAX_SIZE.getIntValue();
														rejectedTermList.add(errorDisp);
														isValidData = false;
													}
	
												}
											}
										}
									}
									if (isValidData) {
										TermInformation newTermInfo = new TermInformation();
										newTermInfo.setConceptDefinition(termInformation.getConceptDefinition());
										DepractedObject depractedObject = new DepractedObject();
										languageCode = eElement.getAttribute(TeaminologyEnum.LANG.getValue());
										getTagValue(TeaminologyEnum.TERM.getValue(), eElement, newTermInfo, depractedObject);
										getTagValue(TeaminologyEnum.TERM_NOTE.getValue(), eElement, newTermInfo, depractedObject);
										if (newTermInfo.getTermBeingPolled() != null) {
											if (depractedObject != null) {
												if (depractedObject.getDeprecated1() == null) dcdSource1 = null;
												if (depractedObject.getDeprecated2() == null) dcdSource2 = null;
												if (depractedObject.getDeprecated3() == null) dcdSource3 = null;
	
											}
											termInformation.setTermBeingPolled(newTermInfo.getTermBeingPolled());
											termInformation.setTermLangId(newTermInfo.getTermLangId());
											termInformation.setTermPOS(newTermInfo.getTermPOS());
											termInformation.setDeprecatedTermInfo(newTermInfo.getDeprecatedTermInfo());
											//							    								    	  
										}
	
	
										if (termInformation.getTermBeingPolled() != null && newTermInfo.getSuggestedTermLangId() != null && termInformation.getTermBeingPolled().trim() != "") {
											newTermInfo.setTermBeingPolled(termInformation.getTermBeingPolled());
											newTermInfo.setTermLangId(termInformation.getTermLangId());
											newTermInfo.setTermPOS(termInformation.getTermPOS());
											newTermInfo.setCreateDate(new Date());
											//							    							    	
											if (newTermInfo.getDeprecatedTermInfo() == null || newTermInfo.getDeprecatedTermInfo().size() == 0) {
												if (dcdSource1 != null || dcdSource2 != null || dcdSource3 != null) {
													Set<DeprecatedTermInformation> deprecatedTermInfoset = new HashSet<DeprecatedTermInformation>();
													if (dcdSource1 != null) {
														DeprecatedTermInformation deprecatedTermInformation = new DeprecatedTermInformation();
														deprecatedTermInformation.setTermInfo(newTermInfo);
														deprecatedTermInformation.setDeprecatedSource(dcdSource1);
														deprecatedTermInformation.setIsActive("Y");
														deprecatedTermInfoset.add(deprecatedTermInformation);
	
													}
													if (dcdSource2 != null) {
														DeprecatedTermInformation deprecatedTermInformation = new DeprecatedTermInformation();
														deprecatedTermInformation.setTermInfo(newTermInfo);
														deprecatedTermInformation.setDeprecatedSource(dcdSource2);
														deprecatedTermInformation.setIsActive("Y");
														deprecatedTermInfoset.add(deprecatedTermInformation);
	
													}
													if (dcdSource3 != null) {
														DeprecatedTermInformation deprecatedTermInformation = new DeprecatedTermInformation();
														deprecatedTermInformation.setTermInfo(newTermInfo);
														deprecatedTermInformation.setDeprecatedSource(dcdSource3);
														deprecatedTermInformation.setIsActive("Y");
														deprecatedTermInfoset.add(deprecatedTermInformation);
													}
	
													if (deprecatedTermInfoset.size() > 0)
														newTermInfo.setDeprecatedTermInfo(deprecatedTermInfoset);
	
												}
											}
											for (Company company : companyList) {
												Set<DeprecatedTermInformation> newDeprecatedTermInformationSet = new HashSet<DeprecatedTermInformation>();
												Set<DeprecatedTermInformation> newDeprecatedTermInfoSet = newTermInfo.getDeprecatedTermInfo();
												TermInformation newTermInformation = new TermInformation();
												newTermInformation.setTermBeingPolled(newTermInfo.getTermBeingPolled());
												newTermInformation.setTermLangId(newTermInfo.getTermLangId());
												newTermInformation.setTermPOS(newTermInfo.getTermPOS());
												newTermInformation.setTermCategory(newTermInfo.getTermCategory());
												newTermInformation.setTermDomain(newTermInfo.getTermDomain());
												newTermInformation.setTermForm(newTermInfo.getTermForm());
												newTermInformation.setTermNotes(newTermInfo.getTermNotes());
												newTermInformation.setTermProgram(newTermInfo.getTermProgram());
												newTermInformation.setTermStatusId(newTermInfo.getTermStatusId());
												newTermInformation.setTermUsage(newTermInfo.getTermUsage());
												newTermInformation.setSuggestedTerm(newTermInfo.getSuggestedTerm());
												newTermInformation.setSuggestedTermLangId(newTermInfo.getSuggestedTermLangId());
												newTermInformation.setSuggestedTermFormId(newTermInfo.getSuggestedTermFormId());
												newTermInformation.setSuggestedTermNotes(newTermInfo.getSuggestedTermNotes());
												newTermInformation.setSuggestedTermPgmId(newTermInfo.getSuggestedTermPgmId());
												newTermInformation.setSuggestedTermPosId(newTermInfo.getSuggestedTermPosId());
												newTermInformation.setSuggestedTermStatusId(newTermInfo.getSuggestedTermStatusId());
												newTermInformation.setSuggestedTermUsage(newTermInfo.getSuggestedTermUsage());
												newTermInformation.setSuggestedTermNotes(newTermInfo.getSuggestedTermNotes());
												newTermInformation.setConceptCatId(newTermInfo.getConceptCatId());
												newTermInformation.setConceptDefinition(newTermInfo.getConceptDefinition());
												newTermInformation.setConceptNotes(newTermInfo.getConceptNotes());
												newTermInformation.setConceptProdGroup(newTermInfo.getConceptProdGroup());
												newTermInformation.setIsActive(newTermInfo.getIsActive());
												newTermInformation.setComments(newTermInfo.getComments());
												newTermInformation.setCreateDate(newTermInfo.getCreateDate());
												newTermInformation.setCreatesBy(newTermInfo.getCreatedBy());
												newTermInformation.setTermCompany(company);
												if (newDeprecatedTermInfoSet != null && newDeprecatedTermInfoSet.size() > 0) {
													for (DeprecatedTermInformation deprecatedTermInformation : newDeprecatedTermInfoSet) {
														DeprecatedTermInformation newDeprTermInfo = new DeprecatedTermInformation();
														newDeprTermInfo.setTermInfo(newTermInformation);
														newDeprTermInfo.setDeprecatedSource(deprecatedTermInformation.getDeprecatedSource());
														newDeprTermInfo.setDeprecatedTarget(deprecatedTermInformation.getDeprecatedTarget());
														newDeprTermInfo.setIsActive(deprecatedTermInformation.getIsActive());
														newDeprecatedTermInformationSet.add(newDeprTermInfo);
													}
												}
												newTermInformation.setDeprecatedTermInfo(newDeprecatedTermInformationSet);
												termSaveInformation = termDAO.addNewTerm(newTermInformation);
												if (termSaveInformation.equals("success")) {
													insertedTermInformationList.add(newTermInformation);
												} else if (termSaveInformation.equals("update")) {
													updatedTermInformationList.add(newTermInformation);
												}
											}
	
											if (termSaveInformation.equals("success")) {
												insertedCount++;
											}
											if (termSaveInformation.equals("update")) {
												updatedCount++;
											}
											if (termSaveInformation.equals("failedterm")) {
												rejectedCount++;
												errorDisp = "Term already exists for : '" + termEntryId + "' for Language Code " + eElement.getAttribute("xml:lang");
												rejectedTermList.add(errorDisp);
											}
										}
	
	
										if ((languageCode.equalsIgnoreCase("en") || languageCode.equalsIgnoreCase("en-US") || languageCode.equalsIgnoreCase(TeaminologyEnum.ENGLISH_SOURCE.getValue())) && (newTermInfo.getTermBeingPolled() == null || newTermInfo.getTermBeingPolled().trim() == "")) {
											rejectedCount++;
											errorDisp = "Source not available for termEntry:'" + termEntryId + "'";
											rejectedTermList.add(errorDisp);
										}
										if (!(languageCode.equalsIgnoreCase("en") || languageCode.equalsIgnoreCase("en-US") || languageCode.equalsIgnoreCase(TeaminologyEnum.ENGLISH_SOURCE.getValue())) && languageCode != null && newTermInfo.getSuggestedTermLangId() == null) {
											rejectedCount++;
											errorDisp = "Language Code: '" + eElement.getAttribute("xml:lang") + "' not available for termEntry:'" + termEntryId + "'";
											rejectedTermList.add(errorDisp);
										}
									} else {
										rejectedCount++;
										isValidData = true;
									}
								}
							}
						}
					}
				}
				csvImportStatus.setInsertedCount(insertedCount);
				csvImportStatus.setRejectedCount(rejectedCount);
				csvImportStatus.setUpdatedCount(updatedCount);
				csvImportStatus.setUserNames(rejectedTermList);
				csvImportStatus.setLineNumbers(termErrorLineNumber);
				csvImportStatus.setTermInformationStatus("success");
			}
			catch (Exception e) {
				e.printStackTrace();
				csvImportStatus.setTermInformationStatus("failed");
			}
			updateTermBaseIndex(updatedTermInformationList);
			addNewTermBaseIndex(insertedTermInformationList);
	
			return csvImportStatus;
	
		}
	
	
		/**
		 * To get tag value
		 *
		 * @param sTag
		 * @param eElement
		 * @param termInformation
		 * @return TermInformation
		 */
		@Transactional
		private TermInformation getTagValue(String sTag, Element eElement, TermInformation termInformation, DepractedObject depractedObject) {
			Set<DeprecatedTermInformation> deprecatedTermInfoset = new HashSet<DeprecatedTermInformation>();
			DeprecatedTermInformation deprecatedTermInformation = null;
			String langCode = eElement.getAttribute(TeaminologyEnum.LANG.getValue());
			if (langCode != null && (langCode.equalsIgnoreCase("en") || langCode.equalsIgnoreCase("en-US") || langCode.equalsIgnoreCase("en_US"))) {
				langCode = TeaminologyEnum.ENGLISH_SOURCE.getValue();
			}
			Language language = lookUpDAO.getLanguageByCode(langCode, true);
			if (eElement.getElementsByTagName(sTag) != null && eElement.getElementsByTagName(sTag).item(0) != null) {
				NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
				Node nValue = (Node) nlList.item(0);
	
				NodeList descList = eElement.getElementsByTagName(sTag);
				int descListLength = descList.getLength();
				for (int j = 0; j < descListLength; j++) {
					Element descEle = (Element) descList.item(j);
					if (sTag.equalsIgnoreCase(TeaminologyEnum.TERM.getValue())) {
						if ((langCode.equalsIgnoreCase("en") || langCode.equalsIgnoreCase("en-US") || langCode.equalsIgnoreCase(TeaminologyEnum.ENGLISH_SOURCE.getValue())) && language != null) {
							termInformation.setTermLangId(language.getLanguageId());
							if (nValue != null && nValue.getNodeValue() != null && nValue.getNodeValue().toString().trim().length() > 0) {
								termInformation.setTermBeingPolled(nValue.getNodeValue());
							} else {
								termInformation.setTermBeingPolled("");
							}
						} else if (langCode != null && language != null) {
							termInformation.setSuggestedTermLangId(language.getLanguageId());
							if (nValue != null && nValue.toString().trim().length() > 0) {
								termInformation.setSuggestedTerm(nValue.getNodeValue());
							} else {
								termInformation.setSuggestedTerm(null);
							}
						}
					}
	
					if (sTag.equalsIgnoreCase(TeaminologyEnum.TERM_NOTE.getValue())) {
						if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()) != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.TERM_PART_OF_SPEECH.getValue())) {
							if (nValue != null && nValue.getNodeValue() != null) {
								PartsOfSpeech partOfSpeech = lookUpDAO.getPartsOfSpeechByName(nValue.getNodeValue());
								termInformation.setTermPOS(partOfSpeech);
							}
						}
						if (langCode.equalsIgnoreCase(TeaminologyEnum.ENGLISH_SOURCE.getValue())) {
							if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()) != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM1.getValue())) {
								if (descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null) {
									dcdSource1 = descEle.getFirstChild().getNodeValue();
									depractedObject.setDeprecated1(dcdSource1);
								}
	
							}
							if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM2.getValue())) {
								if (descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null) {
	
									dcdSource2 = descEle.getFirstChild().getNodeValue();
									depractedObject.setDeprecated2(dcdSource2);
								}
	
							}
							if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM3.getValue())) {
								if (descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null) {
	
									dcdSource3 = descEle.getFirstChild().getNodeValue();
									depractedObject.setDeprecated3(dcdSource3);
								}
	
							}
	
						} else {
							if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()) != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM1.getValue()) && descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null) {
								deprecatedTermInformation = new DeprecatedTermInformation();
								deprecatedTermInformation.setTermInfo(termInformation);
								deprecatedTermInformation.setDeprecatedTarget(descEle.getFirstChild().getNodeValue());
								deprecatedTermInformation.setDeprecatedSource(dcdSource1);
								deprecatedTermInformation.setDeprecatedLangId(language);
								deprecatedTermInformation.setIsActive("Y");
								deprecatedTermInfoset.add(deprecatedTermInformation);
							} else if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()) != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM2.getValue()) && descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null) {
								deprecatedTermInformation = new DeprecatedTermInformation();
								deprecatedTermInformation.setTermInfo(termInformation);
								deprecatedTermInformation.setDeprecatedTarget(descEle.getFirstChild().getNodeValue());
								deprecatedTermInformation.setDeprecatedSource(dcdSource2);
								deprecatedTermInformation.setIsActive("Y");
								deprecatedTermInformation.setDeprecatedLangId(language);
								deprecatedTermInfoset.add(deprecatedTermInformation);
							} else if (descEle != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()) != null && descEle.getAttribute(TeaminologyEnum.TYPE.getValue()).equalsIgnoreCase(TeaminologyEnum.DEPRECATED_TERM3.getValue()) && descEle.getFirstChild() != null && descEle.getFirstChild().getNodeValue() != null) {
								deprecatedTermInformation = new DeprecatedTermInformation();
								deprecatedTermInformation.setTermInfo(termInformation);
								deprecatedTermInformation.setIsActive("Y");
								deprecatedTermInformation.setDeprecatedTarget(descEle.getFirstChild().getNodeValue());
								deprecatedTermInformation.setDeprecatedSource(dcdSource3);
								deprecatedTermInformation.setDeprecatedLangId(language);
								deprecatedTermInfoset.add(deprecatedTermInformation);
							}
	
						}
	
	
					}
	
				}
	
				termInformation.setDeprecatedTermInfo(deprecatedTermInfoset);
			}
			return termInformation;
		}
	
	
		/**
		 * To  write tbx
		 *
		 * @param termInfoList - list to be written in TBX
		 * @param absolutePath - temporary path to create a file
		 * @return File in which data is written
		 */
		@Override
		@Transactional
		public <C extends TeaminologyObject> File writeTBX(
				List<TermInformationTo> termInfoList, String absolutePath) {
			File x = null;
			try {
				x = new File(absolutePath);
				DocumentBuilderFactory f
				= DocumentBuilderFactory.newInstance();
				DocumentBuilder b = f.newDocumentBuilder();
				Document d = b.newDocument();
	
				Element matifEntry = d.createElement("martif");
				matifEntry.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
				matifEntry.setAttribute("xsi:noNamespaceSchemaLocation", "TBXcsV02.xsd");
				matifEntry.setAttribute("type", "TBX");
				matifEntry.setAttribute("xml:lang", "en");
				d.appendChild(matifEntry);
				Element martifHeader = d.createElement("martifHeader");
				matifEntry.appendChild(martifHeader);
				Element fileDesc = d.createElement("fileDesc");
				martifHeader.appendChild(fileDesc);
				Element sourceDesc = d.createElement("sourceDesc");
				fileDesc.appendChild(sourceDesc);
				Element p = d.createElement("p");
				sourceDesc.appendChild(p);
				p.appendChild(d.createTextNode("TBX file, created via MultiTerm Export"));
				Element encodingDesc = d.createElement("encodingDesc");
				martifHeader.appendChild(encodingDesc);
				Element p2 = d.createElement("p");
				encodingDesc.appendChild(p2);
				p2.setAttribute("type", "XCSURI");
				Element textEntry = d.createElement("text");
				matifEntry.appendChild(textEntry);
				Element bodyEntry = d.createElement("body");
				textEntry.appendChild(bodyEntry);
				for (int i = 0; i < termInfoList.size(); i++) {
					TermInformationTo termInfo = termInfoList.get(i);
					Language language = lookUpDAO.getLanguage(termInfo.getTermLangId());
	
					String suggestedTermLang = "";
					String date = "";
					String trasactedBy = "";
					Member memberDetails = null;
					Status status = null;
					ConceptCategory conceptCategory=null;
					ProductGroup productGroup=null;
					Program program=null;
					String tempStr = "";
	
					Element termEntry = d.createElement("termEntry");
					bodyEntry.appendChild(termEntry);
	
	
					Element transacGrp = d.createElement("transacGrp");
					termEntry.appendChild(transacGrp);
					Element transac = d.createElement("transac");
					transacGrp.appendChild(transac);
					transac.setAttribute("type", "terminologyManagementTransactions");
					transac.appendChild(d.createTextNode("origination"));
					Element originDate = d.createElement("date");
					transacGrp.appendChild(originDate);
					if (termInfo.getCreateDate() != null)
						date = termInfo.getCreateDate().get(0) != null ? termInfo.getCreateDate().get(0).toString() : "";
						originDate.appendChild(d.createTextNode(date));
						Element originatedBy = d.createElement("transacNote");
						transacGrp.appendChild(originatedBy);
						if (termInfo.getCreatedBy() != null && termInfo.getCreatedBy().get(0) != null) {
							memberDetails = userDAO.userDetails(termInfo.getCreatedBy().get(0));
							trasactedBy = memberDetails.getUserName();
						}
						originatedBy.setAttribute("type", "responsibility");
						originatedBy.appendChild(d.createTextNode(trasactedBy));
						trasactedBy = "";
	
						Element transacGrp2 = d.createElement("transacGrp");
						termEntry.appendChild(transacGrp2);
						Element transac2 = d.createElement("transac");
						transacGrp2.appendChild(transac2);
						transac2.setAttribute("type", "terminologyManagementTransactions");
						transac2.appendChild(d.createTextNode("modification"));
						Element modifyDate = d.createElement("date");
						transacGrp2.appendChild(modifyDate);
						if (termInfo.getUpdateDate() != null)
							date = termInfo.getUpdateDate().get(0) != null ? termInfo.getUpdateDate().get(0).toString() : "";
							modifyDate.appendChild(d.createTextNode(date));
							Element modifiedBy = d.createElement("transacNote");
							transacGrp2.appendChild(modifiedBy);
							modifiedBy.setAttribute("type", "responsibility");
							if (termInfo.getUpdatedBy() != null && termInfo.getUpdatedBy().get(0) != null) {
								memberDetails = userDAO.userDetails(termInfo.getUpdatedBy().get(0));
								trasactedBy = memberDetails.getUserName();
							}
							modifiedBy.appendChild(d.createTextNode(trasactedBy));
							trasactedBy = "";
							Element langSet = d.createElement("langSet");
							termEntry.appendChild(langSet);
							if (language != null) {
								langSet.setAttribute("xml:lang", language.getLanguageCode().toLowerCase());
							}
							Element tag = d.createElement("tig");
							langSet.appendChild(tag);
							Element term = d.createElement("term");
							tag.appendChild(term);
							if (termInfo.getTermBeingPolled() != null) {
								term.appendChild(d.createTextNode(termInfo.getTermBeingPolled()));
							}
							Element srcTransacGrp = d.createElement("transacGrp");
							tag.appendChild(srcTransacGrp);
							Element srcTransac = d.createElement("transac");
							srcTransacGrp.appendChild(srcTransac);
							srcTransac.setAttribute("type", "terminologyManagementTransactions");
							srcTransac.appendChild(d.createTextNode("origination"));
							Element srcOrgnDate = d.createElement("date");
							srcTransacGrp.appendChild(srcOrgnDate);
							if (termInfo.getCreateDate() != null)
								date = termInfo.getCreateDate().get(0) != null ? termInfo.getCreateDate().get(0).toString() : "";
								srcOrgnDate.appendChild(d.createTextNode(date));
								Element srcOriginatedBy = d.createElement("transacNote");
								srcTransacGrp.appendChild(srcOriginatedBy);
								if (termInfo.getCreatedBy() != null && termInfo.getCreatedBy().get(0) != null) {
									memberDetails = userDAO.userDetails(termInfo.getCreatedBy().get(0));
									trasactedBy = memberDetails.getUserName();
								}
								srcOriginatedBy.setAttribute("type", "responsibility");
								srcOriginatedBy.appendChild(d.createTextNode(trasactedBy));
								trasactedBy = "";
	
								srcTransacGrp = d.createElement("transacGrp");
								tag.appendChild(srcTransacGrp);
								srcTransac = d.createElement("transac");
								srcTransacGrp.appendChild(srcTransac);
								srcTransac.setAttribute("type", "terminologyManagementTransactions");
								srcTransac.appendChild(d.createTextNode("modification"));
								Element srcModifyDate = d.createElement("date");
								srcTransacGrp.appendChild(srcModifyDate);
								if (termInfo.getUpdateDate() != null)
									date = termInfo.getUpdateDate().get(0) != null ? termInfo.getUpdateDate().get(0).toString() : "";
									srcModifyDate.appendChild(d.createTextNode(date));
									Element srcModifiedBy = d.createElement("transacNote");
									srcTransacGrp.appendChild(srcModifiedBy);
									srcModifiedBy.setAttribute("type", "responsibility");
									if (termInfo.getUpdatedBy() != null && termInfo.getUpdatedBy().get(0) != null) {
										memberDetails = userDAO.userDetails(termInfo.getUpdatedBy().get(0));
										trasactedBy = memberDetails.getUserName();
									}
									srcModifiedBy.appendChild(d.createTextNode(trasactedBy));
									trasactedBy = "";
	
	
									Element descripGrp = d.createElement("descripGrp");
									tag.appendChild(descripGrp);
									Element descrip = d.createElement("descrip");
									descripGrp.appendChild(descrip);
									descrip.setAttribute("type", "Status");
									if (termInfo.getTermStatusId() != null) {
										status = lookUpDAO.getStatus(termInfo.getTermStatusId());
										descrip.appendChild(d.createTextNode(status.getStatus()));
	
									}
									descripGrp = d.createElement("descripGrp");
									tag.appendChild(descripGrp);
									descrip = d.createElement("descrip");
									descripGrp.appendChild(descrip);
									descrip.setAttribute("type", "Part of Speech");
									if (termInfo.getTermPOS() != null) {
										descrip.appendChild(d.createTextNode(termInfo.getTermPOS().getPartOfSpeech()));
	
									}
									descripGrp = d.createElement("descripGrp");
									tag.appendChild(descripGrp);
									descrip = d.createElement("descrip");
									descripGrp.appendChild(descrip);
									descrip.setAttribute("type", "Company");
									if (termInfo.getCompany() != null) {
										descrip.appendChild(d.createTextNode(termInfo.getCompany().getCompanyName()));
	
									}
									descripGrp = d.createElement("descripGrp");
									tag.appendChild(descripGrp);
									descrip = d.createElement("descrip");
									descripGrp.appendChild(descrip);
									descrip.setAttribute("type", "Domain");
									if (termInfo.getTermDomain() != null) {
										descrip.appendChild(d.createTextNode(termInfo.getTermDomain().getDomain()));
	
									}
	
									if (termInfo.getDeprecatedsouceSet() != null) {
										int sourceCount = 1;
										for (int l = 0; l < 3; l++) {
											if (l < (termInfo.getDeprecatedsouceSet().size())) {
												String source = termInfo.getDeprecatedsouceSet().get(l) != null ? termInfo.getDeprecatedsouceSet().get(l) : "";
												descripGrp = d.createElement("descripGrp");
												tag.appendChild(descripGrp);
												descrip = d.createElement("descrip");
												descripGrp.appendChild(descrip);
												descrip.setAttribute("type", "Deprecated Source" + sourceCount++);
												descrip.appendChild(d.createTextNode(source));
											}
	
										}
									}
	
									descripGrp = d.createElement("descripGrp");
									tag.appendChild(descripGrp);
									descrip = d.createElement("descrip");
									descripGrp.appendChild(descrip);
									descrip.setAttribute("type", "Form");
									if (termInfo.getTermForm() != null) {
										descrip.appendChild(d.createTextNode(termInfo.getTermForm().getFormName()));
	
									}
									descripGrp = d.createElement("descripGrp");
									tag.appendChild(descripGrp);
									descrip = d.createElement("descrip");
									descripGrp.appendChild(descrip);
									descrip.setAttribute("type", "Contextual Example");
									if (termInfo.getTermUsage() != null) {
										tempStr = termInfo.getTermUsage();
									}
									descrip.appendChild(d.createTextNode(tempStr));
									//		        	 descrip.appendChild(d.createTextNode(termInfo.getTermUsage()));
	
									descripGrp = d.createElement("descripGrp");
									tag.appendChild(descripGrp);
									descrip = d.createElement("descrip");
									descripGrp.appendChild(descrip);
									descrip.setAttribute("type", "Category");
									if (termInfo.getTermCategory() != null) {
										descrip.appendChild(d.createTextNode(termInfo.getTermCategory().getCategory()));
									}
	
									//for different Languages
									for (int j = 0; j < termInfo.getSuggestedTermLangIds().size(); j++) {
										List<Set<DeprecatedTermInformation>> targetdeptermlist = termInfo.getTargetDepterInfoList();
										Set<DeprecatedTermInformation> deprecatedTermInfoset = new HashSet<DeprecatedTermInformation>();
										List<DeprecatedTermInformation> deprecatedTermInfoList = new ArrayList<DeprecatedTermInformation>();
	
										Integer langId = termInfo.getSuggestedTermLangIds().get(j);
										String suggestedTerm = termInfo.getSuggestedTerms().get(j);
										Integer suggestedPOSId = termInfo.getSuggestedTermPosIds().get(j) == null ? null : termInfo.getSuggestedTermPosIds().get(j);
										Integer statusId = termInfo.getTermStatusId() == null ? null : termInfo.getTermStatusId();
										Date createDate = termInfo.getCreateDate() == null ? null : termInfo.getCreateDate().get(j);
										Date updateDate = termInfo.getUpdateDate() == null ? null : termInfo.getUpdateDate().get(j);
										Integer createdBy = termInfo.getCreatedBy() == null ? null : (termInfo.getCreatedBy().get(j) == null ? null : termInfo.getCreatedBy().get(j));
										Integer updatedBy = termInfo.getUpdatedBy() == null ? null : (termInfo.getUpdatedBy().get(j) == null ? null : termInfo.getUpdatedBy().get(j));
										PartsOfSpeech suggestedTermPOS = null;
										Status suggestedTermStatus = null;
	
										langSet = d.createElement("langSet");
										termEntry.appendChild(langSet);
	
										language = lookUpDAO.getLanguage(langId);
										suggestedTermLang = language == null ? "" : language.getLanguageCode().toLowerCase();
										langSet.setAttribute("xml:lang", suggestedTermLang);
										tag = d.createElement("tig");
										langSet.appendChild(tag);
										term = d.createElement("term");
										tag.appendChild(term);
										if (suggestedTerm != null) {
											term.appendChild(d.createTextNode(suggestedTerm));
										}
	
										Element tgtTransacGrp = d.createElement("transacGrp");
										tag.appendChild(tgtTransacGrp);
										Element tgtTransac = d.createElement("transac");
										tgtTransacGrp.appendChild(tgtTransac);
										tgtTransac.setAttribute("type", "terminologyManagementTransactions");
										tgtTransac.appendChild(d.createTextNode("origination"));
										Element tgtOrgnDate = d.createElement("date");
										tgtTransacGrp.appendChild(tgtOrgnDate);
										date = createDate != null ? createDate.toString() : "";
										tgtOrgnDate.appendChild(d.createTextNode(date));
										Element tgtOriginatedBy = d.createElement("transacNote");
										tgtTransacGrp.appendChild(tgtOriginatedBy);
										if (createdBy != null) {
											memberDetails = userDAO.userDetails(createdBy);
											trasactedBy = memberDetails.getUserName();
										}
										tgtOriginatedBy.setAttribute("type", "responsibility");
										tgtOriginatedBy.appendChild(d.createTextNode(trasactedBy));
										trasactedBy = "";
	
										tgtTransacGrp = d.createElement("transacGrp");
										tag.appendChild(tgtTransacGrp);
										tgtTransac = d.createElement("transac");
										tgtTransacGrp.appendChild(tgtTransac);
										tgtTransac.setAttribute("type", "terminologyManagementTransactions");
										tgtTransac.appendChild(d.createTextNode("modification"));
										Element tgtModifyDate = d.createElement("date");
										tgtTransacGrp.appendChild(tgtModifyDate);
										date = updateDate != null ? updateDate.toString() : "";
										tgtModifyDate.appendChild(d.createTextNode(date));
										Element tgtModifiedBy = d.createElement("transacNote");
										tgtTransacGrp.appendChild(tgtModifiedBy);
										tgtModifiedBy.setAttribute("type", "responsibility");
										if (updatedBy != null) {
											memberDetails = userDAO.userDetails(updatedBy);
											trasactedBy = memberDetails.getUserName();
										}
										tgtModifiedBy.appendChild(d.createTextNode(trasactedBy));
										trasactedBy = "";
										descripGrp = d.createElement("descripGrp");
										tag.appendChild(descripGrp);
										descrip = d.createElement("descrip");
										descripGrp.appendChild(descrip);
										descrip.setAttribute("type", "Status");
										if (statusId != null) {
											suggestedTermStatus = lookUpDAO.getStatus(statusId);
											descrip.appendChild(d.createTextNode(suggestedTermStatus.getStatus()));
	
										}
	
										descripGrp = d.createElement("descripGrp");
										tag.appendChild(descripGrp);
										descrip = d.createElement("descrip");
										descripGrp.appendChild(descrip);
										descrip.setAttribute("type", "Part of Speech");
										if (suggestedPOSId != null) {
											suggestedTermPOS = lookUpDAO.getPartsOfSpeech(suggestedPOSId);
											descrip.appendChild(d.createTextNode(suggestedTermPOS.getPartOfSpeech()));
	
										}
	
	
										if (targetdeptermlist != null) {
											deprecatedTermInfoset = targetdeptermlist.get(j);
											for (DeprecatedTermInformation deprecatedTermInformation : deprecatedTermInfoset) {
												deprecatedTermInfoList.add(deprecatedTermInformation);
	
											}
										}
	
										if (deprecatedTermInfoList.size() > 0 && deprecatedTermInfoList != null) {
											int targetCount = 1;
											for (int l = 0; l < 3; l++) {
												if (l < (deprecatedTermInfoList.size())) {
													String target = deprecatedTermInfoList.get(l).getDeprecatedTarget() != null ? deprecatedTermInfoList.get(l).getDeprecatedTarget() : "";
													descripGrp = d.createElement("descripGrp");
													tag.appendChild(descripGrp);
													descrip = d.createElement("descrip");
													descripGrp.appendChild(descrip);
													descrip.setAttribute("type", "Deprecated Target" + targetCount++);
													descrip.appendChild(d.createTextNode(target));
												}
	
											}
										}
	
									}
	
				}
	
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer m = tf.newTransformer();
	
				DOMSource source = new DOMSource(d);
				StreamResult result = new StreamResult(x);
				m.transform(source, result);
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
			return x;
		}
	
	
		/**
		 * To  write TMX
		 *
		 * @param tmProfileInfoList - list to be written in TMX
		 * @param absolutePath      - temporary path to create a file
		 * @return File in which data is written
		 */
		@Override
		@Transactional
		public <C extends TeaminologyObject> File writeTMX(
				List<TmProfileInfo> tmProfileInfoList, String absolutePath) {
			File x = null;
			try {
				x = new File(absolutePath);
				DocumentBuilderFactory f
				= DocumentBuilderFactory.newInstance();
				DocumentBuilder b = f.newDocumentBuilder();
				Document d = b.newDocument();
	
	
				Element tmxEntry = d.createElement("tmx");
				tmxEntry.setAttribute("version", "1.0 GS");
				d.appendChild(tmxEntry);
				Element header = d.createElement("header");
				header.setAttribute("srclang", "EN_US");
				/*Domain domain=tmProfileInfoList.get(i).getDomain();
	                      if(domain!=null){
				        	 Element prop=d.createElement("prop");
				        	 String domainValue=domain.getDomain();
				        	prop.appendChild(d.createTextNode(domainValue));
				        	prop.setAttribute("type", TeaminologyEnum.TEAMINOLOGY_DOMAIN.getValue());
				        	header.appendChild(prop);
				         }
				         ProductGroup product=tmProfileInfoList.get(i).getProductGroup();
				         if(product!=null){
				        	 Element prop=d.createElement("prop");
				        	 String productValue=product.getProduct();
				        	prop.appendChild(d.createTextNode(productValue));
				        	prop.setAttribute("type", TeaminologyEnum.TEAMINOLOGY_PROUCTLINE.getValue());
				        	header.appendChild(prop);
				         }
				         ContentType contentType=tmProfileInfoList.get(i).getContentType();
				         if(contentType!=null){
				        	 Element prop=d.createElement("prop");
				        	 String contentTypeValue=contentType.getContentType();
				        	prop.appendChild(d.createTextNode(contentTypeValue));
				        	prop.setAttribute("type", TeaminologyEnum.TEAMINOLOGY_CONTENT.getValue());
				        	header.appendChild(prop);
				         }
				 */
				tmxEntry.appendChild(header);
	
	
				Element bodyEntry = d.createElement("body");
				tmxEntry.appendChild(bodyEntry);
				for (int i = 0; i < tmProfileInfoList.size(); i++) {
					/*Language sourcelanguage = lookUpDAO.getLanguage( termInfoList.get(i).getTermLangId());
				        	Language targetLanguage= lookUpDAO.getLanguage( termInfoList.get(i).getSuggestedTermLangId());*/
					// TMProperties tmProperties= new TMProperties();
					List<TMProperties> headerPropetiesList = new ArrayList<TMProperties>();
					List<TMProperties> tuPropertiesList = new ArrayList<TMProperties>();
					Language sourcelanguage = tmProfileInfoList.get(i).getSourceLang();
					Language targetLanguage = tmProfileInfoList.get(i).getTargetLang();
					Element termEntry = d.createElement("tu");
					bodyEntry.appendChild(termEntry);
	
	
					Company company = tmProfileInfoList.get(i).getCompany();
					if (company != null) {
						Element prop = d.createElement("prop");
						String companyValue = company.getCompanyName();
						prop.appendChild(d.createTextNode(companyValue));
						prop.setAttribute("type", TeaminologyEnum.TEAMINOLOGY_COMPANYTEAM.getValue());
						termEntry.appendChild(prop);
					}
					Domain domain = tmProfileInfoList.get(i).getDomain();
					if (domain != null) {
						Element prop = d.createElement("prop");
						String domainValue = domain.getDomain();
						prop.appendChild(d.createTextNode(domainValue));
						prop.setAttribute("type", TeaminologyEnum.TEAMINOLOGY_DOMAIN.getValue());
						termEntry.appendChild(prop);
					}
					ProductGroup product = tmProfileInfoList.get(i).getProductGroup();
					if (product != null) {
						Element prop = d.createElement("prop");
						String productValue = product.getProduct();
						prop.appendChild(d.createTextNode(productValue));
						prop.setAttribute("type", TeaminologyEnum.TEAMINOLOGY_PROUCTLINE.getValue());
						termEntry.appendChild(prop);
					}
					ContentType contentType = tmProfileInfoList.get(i).getContentType();
					if (contentType != null) {
						Element prop = d.createElement("prop");
						String contentTypeValue = contentType.getContentType();
						prop.appendChild(d.createTextNode(contentTypeValue));
						prop.setAttribute("type", TeaminologyEnum.TEAMINOLOGY_CONTENT.getValue());
						termEntry.appendChild(prop);
					}
	
					if (tmProfileInfoList.get(i).getPropRef() != null) {
	
						String prop = tmProfileInfoList.get(i).getPropRef();
						headerPropetiesList = termDAO.getTMPropertiesByRef(prop);
						if (headerPropetiesList != null) {
	
							for (int k = 0; k < headerPropetiesList.size(); k++) {
								Element propElement = d.createElement("prop");
								propElement.appendChild(d.createTextNode(headerPropetiesList.get(k).getDescription()));
								propElement.setAttribute("type", headerPropetiesList.get(k).getType());
								termEntry.appendChild(propElement);
							}
						}
					}
					tuPropertiesList = termDAO.getTMpropertiesUsingTMProfileId(tmProfileInfoList.get(i).getTmProfileInfoId());
					if (tuPropertiesList != null) {
	
						for (int k = 0; k < tuPropertiesList.size(); k++) {
							Element tuPropElement = d.createElement("prop");
							tuPropElement.appendChild(d.createTextNode(tuPropertiesList.get(k).getDescription()));
							tuPropElement.setAttribute("type", tuPropertiesList.get(k).getType());
							termEntry.appendChild(tuPropElement);
						}
					}
					Element tuvGrp = d.createElement("tuv");
					termEntry.appendChild(tuvGrp);
					Element seg = d.createElement("seg");
					tuvGrp.appendChild(seg);
					seg.appendChild(d.createTextNode(tmProfileInfoList.get(i).getSource()));
					tuvGrp.setAttribute("xml:lang", sourcelanguage.getLanguageCode().toUpperCase());
					tuvGrp = d.createElement("tuv");
					termEntry.appendChild(tuvGrp);
					seg = d.createElement("seg");
					tuvGrp.appendChild(seg);
					tuvGrp.setAttribute("xml:lang", targetLanguage.getLanguageCode().toUpperCase());
					seg.appendChild(d.createTextNode(tmProfileInfoList.get(i).getTarget()));
	
				}
	
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer m = tf.newTransformer();
	
				DOMSource source = new DOMSource(d);
				StreamResult result = new StreamResult(x);
				m.transform(source, result);
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
			return x;
		}
	
	
		/**
		 * To convert excel to csv
		 *
		 * @param outputWorkBook
		 * @param absolutePath
		 * @return File
		 */
		@Transactional
		private File convertExcelToCsv(WritableWorkbook outputWorkBook, String absolutePath) {
	
			File f = new File(absolutePath);
			try {
				OutputStream os = (OutputStream) new FileOutputStream(f);
				String encoding = "UTF-8";
				os.write(239);
				os.write(187);
				os.write(191);
				OutputStreamWriter osw = new OutputStreamWriter(os, encoding);
				BufferedWriter bw = new BufferedWriter(osw);
	
				for (int sheet = 0; sheet < outputWorkBook.getNumberOfSheets(); sheet++) {
					Sheet s = outputWorkBook.getSheet(sheet);
	
					Cell[] row = null;
	
					for (int i = 0; i < s.getRows(); i++) {
						row = s.getRow(i);
	
						if (row.length > 0) {
							bw.write(row[0].getContents());
							for (int j = 1; j < row.length; j++) {
								bw.write(',');
								bw.write(row[j].getContents());
							}
						}
						bw.newLine();
					}
				}
				bw.flush();
				bw.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return f;
		}
	
		/**
		 * To remove Extra Line
		 *
		 * @param absolutePath temporary path to create a file
		 * @param filePath     - temporary path of the file
		 * @param encodingType String holding the type of file to be remove
		 * @return File
		 */
		private File removeExtraLine(String filePath, String absolutePath, String encodingType) {
	
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
	
		/**
		 * To  read text file
		 *
		 * @param fileName     - Name of the file to be read
		 * @param encodingType String holding the type of file to be read
		 * @return file
		 */
		public String readTextFile(String fileName, String encodingType) {
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
					System.out.println(returnValue);
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
	
		/**
		 * To  write text file
		 *
		 * @param fileName     - Name of the file to be write
		 * @param encodingType String holding the type of file to be write
		 */
		public void writeTextFile(String fileName, String s, String encodingType) {
			FileWriter output;
			try {
				output = new FileWriter(fileName);
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(fileName), encodingType));
				writer.write(s);
				writer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	
		}
	
		public static String cleanUp(String xml) {
			final StringReader reader = new StringReader(xml.trim());
			final StringWriter writer = new StringWriter();
			try {
				XmlUtil.prettyFormat(reader, writer);
				return writer.toString();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return xml.trim();
		}
	
		/**
		 * To  export xliff
		 *
		 * @param globalSightTermInfoList list to be written in Xliff
		 * @param absolutePath            temporary path to create a file
		 * @return Xliff file in which data is written
		 */
		@Override
		@Transactional
		public <C extends TeaminologyObject> File exportXliff(
				List<GlobalsightTermInfo> globalSightTermInfoList, String absolutePath) {
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
				filePath = filePath + "globalsight.xlf";
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
					if (gsInfo.getFileInfo().getSourceLang().getLanguageCode() != null) {
						fileElement.setAttribute(GlobalsightEnum.SOURCE_LANGUAGE.getValue(), gsInfo.getFileInfo().getSourceLang().getLanguageCode());
					}
					if (gsInfo.getFileInfo().getTargetLang().getLanguageCode() != null) {
						fileElement.setAttribute(GlobalsightEnum.TARGET_LANGUAGE.getValue(), gsInfo.getFileInfo().getTargetLang().getLanguageCode());
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
					String sourceLocale = (gsInfo.getFileInfo().getSourceLang().getLanguageCode() != null) ? "# " + GlobalsightEnum.SOURCE_LOCALE.getValue() + gsInfo.getFileInfo().getSourceLang().getLanguageCode() + "\n" : null;
					String targetLocale = (gsInfo.getFileInfo().getTargetLang().getLanguageCode() != null) ? "# " + GlobalsightEnum.TARGETLOCALE.getValue() + gsInfo.getFileInfo().getTargetLang().getLanguageCode() + "\n" : null;
					String pageId = (gsInfo.getFileInfo().getFileId() != null) ? "# " + GlobalsightEnum.PAGEID.getValue() + gsInfo.getFileInfo().getFileId() + "\n" : null;
					//String workFlowId=(gsInfo.getWorkFlowId()!=null) ? "# "+GlobalsightEnum.WORKFLOW_ID.getValue()+gsInfo.getWorkFlowId()+"\n" : null;
					//String taskId=(gsInfo.getTaskId()!=null) ? "# "+GlobalsightEnum.TASK_ID.getValue()+gsInfo.getTaskId()+"\n" : null;
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
						List<Tag> tagList = termDAO.getTagsByGSId(globalsightTermInfoObj.getGlobalsightTermInfoId());
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
									List<Attributes> attributesList = termDAO.getAttributesByTagId(tagObj.getTagId());
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
	
	
		/**
		 * To  import TMX
		 *
		 * @param uploadFile  -File to be read
		 * @param lookupClass - Object to be read
		 * @return CSVImportStatus obj
		 */
		@Override
		public <C extends TeaminologyObject> CSVImportStatus readTMX(File uploadFile, Class<C> lookupClass) {
			return TMxConvert(uploadFile, lookupClass);
	
			/**
			 * To  import TMX
			 *
			 * @param uploadFile -File to be convert
			 * @param lookupClass - Object to be convert
			 * @return CSVImportStatus obj
			 */}
		@Transactional
		private <C extends TeaminologyObject> CSVImportStatus TMxConvert(File uploadFile, Class<C> lookupClass) {
			CSVImportStatus csvImportStatus = new CSVImportStatus();
			List<String> rejectedTermList = new ArrayList<String>();
			List<Integer> termErrorLineNumber = new ArrayList<Integer>();
			List<String> userExistsList = new ArrayList<String>();
			List<Integer> userExistsLineNumber = new ArrayList<Integer>();
	
			try {
				String termEntryId = "";
				String errorDisp = "";
				Integer insertedCount = 0;
				Integer rejectedCount = 0;
				String languageCode = "";
	
	
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				dbFactory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				dbFactory.setValidating(false);
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = null;
				XmlReader encodingType = new XmlReader(uploadFile);
				String encodingVal = (encodingType.getEncoding() != null || encodingType.getEncoding() != "") ? encodingType.getEncoding() : "UTF-8";
				if (encodingVal.contains("UTF-16LE")) {
					doc = dBuilder.parse(uploadFile);
				} else {
					InputStream inputStream = new FileInputStream(uploadFile);
					Reader reader = new InputStreamReader(inputStream, encodingVal);
	
					InputSource is = new InputSource(reader);
					is.setEncoding(encodingVal);
					doc = dBuilder.parse(is);
				}
				Domain domain = null;
				ProductGroup productGroup = null;
				ContentType contentType = null;
				Company company = null;
				String uniqueId = Utils.createUUID();
	
				NodeList headerList = doc.getElementsByTagName("header");
				for (int h = 0; h < headerList.getLength(); h++) {
					Node headerNode = headerList.item(h);
					if (headerNode.getNodeType() == Node.ELEMENT_NODE) {
						Element headerElement = (Element) headerNode;
						NodeList propList = headerElement.getElementsByTagName("prop");
						for (int t = 0; t < propList.getLength(); t++) {
							TMProperties tmProperties = new TMProperties();
							Element headerPropElement = (Element) propList.item(t);
							String propDesc = headerPropElement.getFirstChild().getNodeValue();
							String propType = propList.item(t).getAttributes().getNamedItem("type").getNodeValue();
							if (propType != null) {
								if (!propType.equalsIgnoreCase(TeaminologyEnum.TEAMINOLOGY_COMPANYTEAM.getValue())
										&& !propType.equalsIgnoreCase(TeaminologyEnum.TEAMINOLOGY_DOMAIN.getValue())
										&& !propType.equalsIgnoreCase(TeaminologyEnum.TEAMINOLOGY_PROUCTLINE.getValue())
										&& !propType.equalsIgnoreCase(TeaminologyEnum.TEAMINOLOGY_CONTENT.getValue())) {
									tmProperties.setType(propType);
									tmProperties.setDescription(propDesc);
									tmProperties.setCreateDate(new Date());
									tmProperties.setPropRef(uniqueId);
									tmProperties.setIsTu("N");
									termDAO.saveTmproperties(tmProperties);
								} else {
	
									if (propType.equalsIgnoreCase(TeaminologyEnum.TEAMINOLOGY_COMPANYTEAM.getValue())) {
										if (propDesc != null && propDesc.trim().length() > 0) {
											company = lookUpDAO.getCompanyByLable(propDesc);
											if (company == null) {
												company = new Company();
												company.setCompanyName(propDesc);
												company.setCreateDate(new Date());
												company.setIsActive("Y");
												lookUpDAO.addCompany(company);
											}
										}
									} else if (propType.equalsIgnoreCase(TeaminologyEnum.TEAMINOLOGY_DOMAIN.getValue())) {
										if (propDesc != null && propDesc.trim().length() > 0) {
											domain = lookUpDAO.getDomainByLable(propDesc);
											if (domain == null) {
												domain = new Domain();
												domain.setDomain(propDesc);
												domain.setCreateDate(new Date());
												domain.setIsActive("Y");
												lookUpDAO.addDomain(domain);
											}
										}
									} else if (propType.equalsIgnoreCase(TeaminologyEnum.TEAMINOLOGY_PROUCTLINE.getValue())) {
										if (propDesc != null && propDesc.trim().length() > 0) {
											productGroup = lookUpDAO.getProductGroupByLabel(propDesc);
											if (productGroup == null) {
												productGroup = new ProductGroup();
												productGroup.setProduct(propDesc);
												productGroup.setCreateDate(new Date());
												productGroup.setIsActive("Y");
												lookUpDAO.addProduct(productGroup);
	
											}
										}
	
									} else if (propType.equalsIgnoreCase(TeaminologyEnum.TEAMINOLOGY_CONTENT.getValue())) {
										if (propDesc != null && propDesc.trim().length() > 0) {
											contentType = lookUpDAO.getContentByLabel(propDesc);
											if (contentType == null) {
												contentType = new ContentType();
												contentType.setContentType(propDesc);
												contentType.setCreateDate(new Date());
												contentType.setIsActive("Y");
												lookUpDAO.addContent(contentType);
	
											}
										}
	
									}
								}
	
							}
	
						}
					}
				}
				doc.getDocumentElement().normalize();
				String xmlLang;
				String sourceTerm = null;
				String suggestedTerm = null;
				String termSaveInformation = null;
	
	
				NodeList tuList = doc.getElementsByTagName("tu");
				if (tuList != null && tuList.getLength() > 0) {
					for (int s = 0; s < tuList.getLength(); s++) {
	
						Node tuNode = tuList.item(s);
						if (tuNode.getNodeType() == Node.ELEMENT_NODE) {
	
							Element element = (Element) tuNode;
							xmlLang = null;
							TmProfileInfo tmProfileInfo = new TmProfileInfo();
	
	
							NodeList tuvList = element.getElementsByTagName("tuv");
	
							for (int t = 0; t < tuvList.getLength(); t++) {
	
								Node targetNode = tuvList.item(t);
	
								if (tuvList.item(t).getAttributes().getNamedItem("xml:lang") != null) {
									xmlLang = tuvList.item(t).getAttributes().getNamedItem("xml:lang").getNodeValue();
								} else {
									xmlLang = tuvList.item(t).getAttributes().getNamedItem("lang").getNodeValue();
								}
	
								if (xmlLang != null && xmlLang.equalsIgnoreCase("EN_US")) {
									Language sourceLanguage = lookUpDAO.getLanguageByCode(xmlLang, true);
									tmProfileInfo.setSourceLang(sourceLanguage);
									if (targetNode.getNodeType() == Node.ELEMENT_NODE) {
										Element altTransUnitElement = (Element) tuvList.item(t);
										NodeList segNodeList = altTransUnitElement.getElementsByTagName("seg");
										sourceTerm = getNodeData(segNodeList);
									}
								}
								if (xmlLang != null && !(xmlLang.equalsIgnoreCase("EN_US"))) {
									Language targetLanguage = lookUpDAO.getLanguageByCode(xmlLang, true);
									if (targetLanguage != null) {
										tmProfileInfo.setTargetLang(targetLanguage);
										if (targetNode.getNodeType() == Node.ELEMENT_NODE) {
											Element altTransUnitElement = (Element) tuvList.item(t);
											NodeList segNodeList = altTransUnitElement.getElementsByTagName("seg");
											suggestedTerm = getNodeData(segNodeList);
										}
									} else {
										termSaveInformation = "Not exits";
									}
								}
							}
							if (termSaveInformation == null) {
								tmProfileInfo.setSource(sourceTerm);
								tmProfileInfo.setTarget(suggestedTerm);
								tmProfileInfo.setCreateDate(new Date());
								tmProfileInfo.setIsActive("Y");
								if (company != null)
									tmProfileInfo.setCompany(company);
								if (domain != null)
									tmProfileInfo.setDomain(domain);
								if (contentType != null)
									tmProfileInfo.setContentType(contentType);
								if (productGroup != null)
									tmProfileInfo.setProductGroup(productGroup);
								tmProfileInfo.setPropRef(uniqueId);
								Boolean isTermExists = termDAO.isTMExists(tmProfileInfo);
								if (isTermExists) {
									termSaveInformation = "failedterm";
								} else {
									Boolean statusmsg = termDAO.addNewTM(tmProfileInfo);
									if (statusmsg) {
										termSaveInformation = "success";
									} else {
										termSaveInformation = "failed";
									}
								}
	
								if (termSaveInformation != null && termSaveInformation.equals("success")) {
									NodeList tuPropList = element.getElementsByTagName("prop");
									for (int tp = 0; tp < tuPropList.getLength(); tp++) {
	
										TMProperties tmProperties = new TMProperties();
										Element tuPropElement = (Element) tuPropList.item(tp);
										String tuPropDesc = tuPropElement.getFirstChild().getNodeValue();
										String tuPropType = tuPropList.item(tp).getAttributes().getNamedItem("type").getNodeValue();
										if (tuPropType != null) {
											tmProperties.setType(tuPropType);
											tmProperties.setDescription(tuPropDesc);
											tmProperties.setIsTu("Y");
											tmProperties.setTmProfileId(tmProfileInfo.getTmProfileInfoId());
											tmProperties.setCreateDate(new Date());
											tmProperties.setPropRef(uniqueId);
											termDAO.saveTmproperties(tmProperties);
										}
	
									}
									insertedCount++;
								}
								if (termSaveInformation != null && termSaveInformation.equals("failedterm")) {
									rejectedCount++;
									errorDisp = "Source Term already exists for term : '" + sourceTerm;
									rejectedTermList.add(errorDisp);
								}
								if (termSaveInformation != null && termSaveInformation.equals("failed")) {
									rejectedCount++;
									errorDisp = "Failed To insert Term for : '" + sourceTerm + "' for Language Code " + xmlLang;
									rejectedTermList.add(errorDisp);
								}
								termSaveInformation = null;
							} else {
								rejectedCount++;
								errorDisp = "Target language not exists for term  : '" + suggestedTerm;
								rejectedTermList.add(errorDisp);
								termSaveInformation = null;
	
							}
						}
					}
					csvImportStatus.setInsertedCount(insertedCount);
					csvImportStatus.setRejectedCount(rejectedCount);
					csvImportStatus.setUserNames(rejectedTermList);
					csvImportStatus.setLineNumbers(termErrorLineNumber);
					csvImportStatus.setTermInformationStatus("success");
				} else {
					csvImportStatus.setRejectedCount(-1);
					csvImportStatus.setTermInformationStatus("invalid");
				}
	
			}
			catch (Exception e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
				csvImportStatus.setRejectedCount(-2);
				csvImportStatus.setTermInformationStatus("error");
				if (e.getMessage().equalsIgnoreCase("Content is not allowed in prolog.")) {
					rejectedTermList.add("Invalid TMX file");
				} else {
					rejectedTermList.add("Failed To Import TMX file." + e.getMessage());
				}
				csvImportStatus.setUserNames(rejectedTermList);
				termErrorLineNumber.add(-1);
				csvImportStatus.setLineNumbers(termErrorLineNumber);
				rejectedTermList = new ArrayList<String>();
				termErrorLineNumber = new ArrayList<Integer>();
			}
			return csvImportStatus;
	
		}
	
		/**
		 * To  get node data
		 *
		 * @param segNodeList NodeList
		 * @return node data
		 */
	
		private static String getNodeData(NodeList segNodeList) {
	
			StringBuffer strbuf = null;
			for (int i = 0; i < segNodeList.getLength(); i++) {
				String segval = "";
				Node segNode = segNodeList.item(i);
				NodeList segChildNodeList = segNode.getChildNodes();
	
				for (int j = 0; j < segChildNodeList.getLength(); j++) {
					Node segChildNode = segChildNodeList.item(j);
	
					if (segChildNode.getNodeType() == Node.ELEMENT_NODE) {
	
						String segChildNodeName = segChildNode.getNodeName();
						Node transTextNode = segChildNode.getFirstChild();
						Attr segNodeAttribute = null;
						String segNodeAttrArray = new String();
	
						if (segChildNode.hasAttributes()) {
							NamedNodeMap attrs = segChildNode.getAttributes();
							for (int k = 0; k < attrs.getLength(); k++) {
								segNodeAttribute = (Attr) attrs.item(k);
								segNodeAttrArray = segNodeAttrArray
										+ segNodeAttribute.getName().toString() + "="
												+ '"' + segNodeAttribute.getValue().toString()
												+ '"' + " ";
							}
						}
						int no_of_childs = segChildNode.getChildNodes().getLength();
	
						if (no_of_childs == 1) {
							segval = segval
									+ "<"
									+ segChildNodeName.toString()
									+ " "
									+ segNodeAttrArray.trim()
									+ ">"
									+ transTextNode.getNodeValue().trim()
									.toString() + "</"
									+ segChildNodeName.toString() + ">";
						} else {
							NodeList ChildNodeList = segChildNode.getChildNodes();
	
							Attr segAttribute = null;
							String segAttrArray = new String();
	
							if (segChildNode.hasAttributes()) {
								NamedNodeMap attrs = segChildNode.getAttributes();
								for (int l = 0; l < attrs.getLength(); l++) {
									segAttribute = (Attr) attrs.item(l);
									segAttrArray = segAttrArray
											+ segAttribute.getName().toString() + "="
													+ '"' + segAttribute.getValue().toString()
													+ '"' + " ";
								}
							}
							segval = segval + "<" + segChildNode.getNodeName() + " "
									+ segAttrArray.trim() + ">";
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
							segval = segval + "</" + segChildNode.getNodeName()
									+ ">";
						}
	
					} else {
						segval = segval + segChildNode.getNodeValue();
					}
	
				}
	
				strbuf = new StringBuffer(segval);
	
			}
	
			return strbuf.toString();
	
		}
	
		/**
		 * To update term base index
		 *
		 * @param tmInfoList TermInformation that has to be updated
		 */
		@Transactional
		public void updateTermBaseIndex(List<TermInformation> tmInfoList) {
			try {
				if (tmInfoList != null && tmInfoList.size() > 0) {
					for (TermInformation tmInfo : tmInfoList) {
						try{
							if (tmInfo.getIsTM() == null) {
								tmInfo = termDAO.isTermBaseAttrbutesExists(tmInfo);
								Integer termCompanyId = tmInfo.getTermCompany() != null ? tmInfo.getTermCompany().getCompanyId() : null;
								String termId = tmInfo.getTermId().toString()+SuffixEnum._TB.name();
								String source = tmInfo.getTermBeingPolled();
								String target = tmInfo.getSuggestedTerm();
								String domainLable = (tmInfo.getTermDomain() == null) ? null : tmInfo.getTermDomain().getDomain();
								PartsOfSpeech partOfSpeech = lookUpDAO.getPartsOfSpeech(tmInfo.getSuggestedTermPosId());
								String posLable = (partOfSpeech == null) ? null : partOfSpeech.getPartOfSpeech();
								PartsOfSpeech  targetPosObj=lookUpDAO.getPartsOfSpeech(tmInfo.getSuggestedTermPosId());
								String targetPos=targetPosObj ==null ? null : targetPosObj.getPartOfSpeech();
								String category = (tmInfo.getTermCategory()) == null ? null : tmInfo.getTermCategory().getCategory();
								String company = (tmInfo.getTermCompany() == null) ? null : tmInfo.getTermCompany().getCompanyName();
								Status statusId = lookUpDAO.getStatus(tmInfo.getTermStatusId());
								String statusLabel = (statusId == null) ? null : statusId.getStatus();
								Language language = lookUpDAO.getLanguage(tmInfo.getSuggestedTermLangId());
								String targetLangLable = (language == null) ? null : language.getLanguageLabel();
								TermVoteMaster termVoteMaster = termDAO.getTermVoteMaster(tmInfo.getTermId());
								Date expDate = termVoteMaster != null ? termVoteMaster.getVotingExpiredDate() : null;
								String expireDate = expDate == null ? null : new SimpleDateFormat("MM/dd/yyyy").format(expDate) == null ? null : new SimpleDateFormat("MM/dd/yyyy").format(expDate);
								List<DeprecatedTermInformation> deprecatedTermInfoList = termDAO.getDeprecatedTermInfoByTermId(tmInfo.getTermId());
								String deprecatedTerm = "";
								if (deprecatedTermInfoList != null && deprecatedTermInfoList.size() > 0) {
									for (DeprecatedTermInformation deprectedTermInfo : deprecatedTermInfoList) {
										deprecatedTerm = deprecatedTerm + deprectedTermInfo.getDeprecatedSource() + " " + deprectedTermInfo.getDeprecatedTarget() + " ";
	
									}
	
								}
	
								server.deleteById(termId);
								server.commit();
	
								SolrInputDocument doc = new SolrInputDocument();
								Collection<SolrInputDocument> collObj = new ArrayList<SolrInputDocument>();
								if (termId != null)
									doc.addField("id", termId);
								if (source != null)
									doc.addField("source", source);
								if (target != null)
									doc.addField("target", target);
								if (domainLable != null)
									doc.addField("domain", domainLable);
								if (posLable != null)
									doc.addField("partofspeech", posLable);
								if (category != null)
									doc.addField("category", category);
								if (company != null)
									doc.addField("company", company);
								if (statusLabel != null)
									doc.addField("status", statusLabel);
								if (targetLangLable != null)
									doc.addField("targetlang", targetLangLable);
								if (expDate != null) {
									doc.addField("expireDate", new SimpleDateFormat("MM/dd/yyyy").parse(expireDate));
								}
								if (!deprecatedTerm.isEmpty()) {
									doc.addField("deprecated", deprecatedTerm);
								}
								if (targetPos!=null) {
									doc.addField("targetpartofspeech", targetPos);
								}
								if (termCompanyId!=null) {
									doc.addField("companyId", termCompanyId);
								}
								doc.addField("termtype","TB");
								doc.addField("primarykey", tmInfo.getTermId());
								collObj.add(doc);
								server.add(collObj);
								server.commit();
								collObj.clear();
								collObj = null;
							}
						}catch(Exception e){
	
							logger.error(e,e);
						}
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		/**
		 * Add new term base index.
		 *
		 * @param tmInfoList TermInformation list that needs to be added
		 */
		@Transactional
		public void addNewTermBaseIndex(List<TermInformation> tmInfoList) {
			SolrInputDocument doc = null;
			try {
				if (tmInfoList != null && tmInfoList.size() > 0) {
					Collection<SolrInputDocument> collObj = new ArrayList<SolrInputDocument>();
					for (TermInformation tmInfo : tmInfoList) {
						try{
							if (tmInfo.getIsTM() == null) {
								String termId = tmInfo.getTermId().toString()+SuffixEnum._TB.name();
								String source = tmInfo.getTermBeingPolled();
								String target = tmInfo.getSuggestedTerm();
								String domainLable = (tmInfo.getTermDomain() == null) ? null : tmInfo.getTermDomain().getDomain();
								PartsOfSpeech partOfSpeech = tmInfo.getTermPOS();
								String posLable = (partOfSpeech == null) ? null : partOfSpeech.getPartOfSpeech();
								String category = (tmInfo.getTermCategory()) == null ? null : tmInfo.getTermCategory().getCategory();
								String company = (tmInfo.getTermCompany() == null) ? null : tmInfo.getTermCompany().getCompanyName();
								Status statusId = lookUpDAO.getStatus(tmInfo.getTermStatusId());
								String statusLabel = (statusId == null) ? null : statusId.getStatus();
								PartsOfSpeech  targetPosObj=lookUpDAO.getPartsOfSpeech(tmInfo.getSuggestedTermPosId());
								String targetPos=targetPosObj ==null ? null : targetPosObj.getPartOfSpeech();
								Language language = lookUpDAO.getLanguage(tmInfo.getSuggestedTermLangId());
								String targetLangLable = (language == null) ? null : language.getLanguageLabel();
								TermVoteMaster termVoteMaster = termDAO.getTermVoteMaster(tmInfo.getTermId());
								Date expDate = termVoteMaster != null ? termVoteMaster.getVotingExpiredDate() : null;
								String expireDate = expDate == null ? null : new SimpleDateFormat("MM/dd/yyyy").format(expDate) == null ? null : new SimpleDateFormat("MM/dd/yyyy").format(expDate);
								List<DeprecatedTermInformation> deprecatedTermInfoList = termDAO.getDeprecatedTermInfoByTermId(tmInfo.getTermId());
								String deprecatedTerm = "";
								if (deprecatedTermInfoList != null && deprecatedTermInfoList.size() > 0) {
									for (DeprecatedTermInformation deprectedTermInfo : deprecatedTermInfoList) {
										deprecatedTerm = deprecatedTerm + deprectedTermInfo.getDeprecatedSource() + " " + deprectedTermInfo.getDeprecatedTarget() + " ";
									}
	
								}
								doc  = new SolrInputDocument();
								if (termId != null)
									doc.addField("id", termId);
								if (source != null)
									doc.addField("source", source);
								if (target != null)
									doc.addField("target", target);
								if (domainLable != null)
									doc.addField("domain", domainLable);
								if (posLable != null)
									doc.addField("partofspeech", posLable);
								if (category != null)
									doc.addField("category", category);
								if (company != null)
									doc.addField("company", company);
								if (tmInfo.getTermCompany()!= null)
									doc.addField("companyId", tmInfo.getTermCompany().getCompanyId());
								if (statusLabel != null)
									doc.addField("status", statusLabel);
								if (targetLangLable != null)
									doc.addField("targetlang", targetLangLable);
								if (expireDate != null) {
									doc.addField("expireDate", new SimpleDateFormat("MM/dd/yyyy").parse(expireDate));
								}
								if (!deprecatedTerm.isEmpty()) {
									doc.addField("deprecated", deprecatedTerm);
								}
								if (targetPos!=null) {
									doc.addField("targetpartofspeech", targetPos);
								}
								doc.addField("termtype","TB");
								doc.addField("primarykey", tmInfo.getTermId());
	
								collObj.add(doc);
							}
							
							server.add(collObj);
							server.commit();
						}catch(Exception e){
							logger.error(e, e);
						}
						
					}
				}
			}
				catch (Exception e) {
					logger.error("Added new term to solr database");
					logger.error(e, e);
				}
				
			}
	
			public List<String> getTermInfoLablesList() {
				List<String> termInfoLableList = new ArrayList<String>();
				termInfoLableList.add(TeaminologyImportEnum.SOURCE_TERM.getStringValue().toLowerCase().trim());//source term
				termInfoLableList.add(TeaminologyImportEnum.TERM_PART_OF_SPEECH.getStringValue().toLowerCase().trim());
				termInfoLableList.add(TeaminologyImportEnum.TERM_CATEGORY.getStringValue().toLowerCase().trim());
				termInfoLableList.add(TeaminologyImportEnum.TARGET_TERM.getStringValue().toLowerCase().trim());
				// termInfoLableList.add(TeaminologyImportEnum.TERM_FORM.getStringValue().toLowerCase().trim());
				// termInfoLableList.add(TeaminologyImportEnum.TERM_NOTES.getStringValue().toLowerCase().trim());
				// termInfoLableList.add(TeaminologyImportEnum.CONTEXTUAL_EXAMPLE.getStringValue().toLowerCase().trim());
				//termInfoLableList.add(TeaminologyImportEnum.CONCEPT_DEFINITION.getStringValue().toLowerCase().trim());
				termInfoLableList.add(TeaminologyImportEnum.DOMAIN.getStringValue().toLowerCase().trim());
				return termInfoLableList;
			}
		}
