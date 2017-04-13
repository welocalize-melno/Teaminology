package com.teaminology.hp;

import java.io.File;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.teaminology.hp.bo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.TermUpdateDetails;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.ContentType;
import com.teaminology.hp.bo.lookup.Domain;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.bo.lookup.ProductGroup;
import com.teaminology.hp.bo.lookup.Status;
import com.teaminology.hp.data.CSVImportStatus;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.TeaminologyObject;
import com.teaminology.hp.data.TermInformationTo;
import com.teaminology.hp.data.Terms;
import com.teaminology.hp.service.IGSService;
import com.teaminology.hp.service.IImportExportService;
import com.teaminology.hp.service.ILoginService;
import com.teaminology.hp.service.ILookUpService;
import com.teaminology.hp.service.ITermDetailsService;
import com.teaminology.hp.service.IUserService;
import com.teaminology.hp.data.TermVotingTo;
import com.teaminology.hp.service.enums.CompanyEnum;

/**
 * Contains methods to connect to service module from web module.
 *
 * @author sarvanic
 */
@Service
public class TeaminologyService
{
    private static ILoginService loginService;
    private static IImportExportService importExportService;
    private static ITermDetailsService termsService;
    private static IUserService userService;
    private static ILookUpService lookUpService;
    private static IGSService gsService;

    @Autowired
    public TeaminologyService(ILoginService loginService,
                              IImportExportService importExportService,
                              ITermDetailsService termsService,
                              IUserService userServive,
                              ILookUpService lookUpService,
                              IGSService gsService) {
        this.loginService = loginService;
        this.importExportService = importExportService;
        this.termsService = termsService;
        this.userService = userServive;
        this.lookUpService = lookUpService;
        this.gsService = gsService;

    }

    /**
     * To authenticate user
     *
     * @param userName String holding the logged user name
     * @param password String holding the logged password
     * @return User w.r.t the userName and password
     */
    public static User authentication(String userName, String password) {
        return loginService.authentication(userName, password);
    }

    /**
     * To  import csv
     *
     * @param uploadedFile  File to be read
     * @param lookupClass Object to be read
     * @param companyIds  String to be read
     * @return CSVImportStatus object
     */
    //@Transactional
    public static <C extends TeaminologyObject> CSVImportStatus importCSV(File uploadedFile, Class<C> lookupClass, User user, String companyIds) {
        return importExportService.readCSV(uploadedFile, lookupClass, user, companyIds);

    }

    /**
     * To  import TBX
     *
     * @param uploadedFile  -File to be read
     * @param lookupClass - Object to be read
     * @param companyIds  String to be read
     * @return CSVImportStatus obj
     */
    public static <C extends TeaminologyObject> CSVImportStatus importTBX(File uploadedFile, Class<C> lookupClass, User user, String companyIds) {
        return (CSVImportStatus) importExportService.readTBX(uploadedFile, lookupClass, user, companyIds);

    }

    /**
     * To  import TMX
     *
     * @param uploadedFile Imported file to be read
     * @param lookupClass  Instance of TermInformation persistence class to be imported
     * @return String holding status of the imported TMX
     */
    public static <C extends TeaminologyObject> CSVImportStatus importTMX(File uploadedFile, Class<C> lookupClass) {
        return (CSVImportStatus) importExportService.readTMX(uploadedFile, lookupClass);

    }

    /**
     * To  export csv
     *
     * @param exportList        Object list to be exported
     * @param absolutePath      Path of the export file template
     * @param uploadedfilesPath Path of the location where files are uploading
     * @return File in which the data is written
     */
    public static <C extends TeaminologyObject> File writeCSV(List<C> exportList, String absolutePath, String uploadedfilesPath, String ExportFormate) {
        return importExportService.writeCSV(exportList, absolutePath, uploadedfilesPath,ExportFormate);

    }
    
    public static <C extends TeaminologyObject> File writeVoteCSV(List<C> exportList, String absolutePath, String uploadedfilesPath , List params) {
        return importExportService.writeVoteCSV(exportList, absolutePath, uploadedfilesPath,  params);

    }

    /**
     * To get Terms in Glossary per year
     *
     * @param companyIds An Integer set to filter terms
     * @return List of data holding year and no of terms per year
     */
    public static List<Terms> getTermsInGlossary(Set<Integer> companyIds) {
        return termsService.getTermsInGlossary(companyIds);

    }

    /**
     * To Get Debated Terms per year
     *
     * @param companyIds An Integer set  to be filtered
     * @return List of data holding year and no of terms per year
     */
    public static List<Terms> getDebatedTerms(Set<Integer> companyIds) {
        return termsService.getDebatedTerms(companyIds);

    }

    
    
    public  static  List<TermVotingTo> getVoteResults(final String fromDate,final String toDate, Integer companyId)
    {
    	return termsService.getVoteResults(fromDate,toDate,companyId);
    }

    /**
     * To get termInformation
     *
     * @param exportBy    String to filter the terms
     * @param selectedIds Integer Array of filter values
     * @return List of terms w.r.t to the filter criteria
     */
    public static List<TermInformation> getTermInformation(String exportBy, Integer[] selectedIds, Integer companyId) {
        return termsService.getTermInformation(exportBy, selectedIds,companyId);
    }


    /**
     * To get list of termIds from term information by languageId
     *
     * @param languageId An Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return List of term id's
     */
    public static List<Integer> getTermInformationByLanguage(Integer languageId, Integer companyId) {
        return termsService.getTermInformationByLanguage(languageId, companyId);
    }

    /**
     * To get count of term information other than top 6 registered languages
     *
     * @param languageIds Set of languageIds to be filtered
     * @param companyId   An Integer set to be filtered
     * @return an integer value holding the count of term id's for languages other than top 6 registered
     */

    public static Integer getTermInformationForOtherLanguage(Set<Integer> languageIds, Integer companyId) {
        return termsService.getTermInformationForOtherLanguage(languageIds, companyId);
    }

    /**
     * To get top register languages
     *
     * @param companyId An Integer to filter languages
     * @return List of top six registered languages
     */
    public static List<Language> getTopRegLangs(Integer companyId) {
        return termsService.getTopRegLangs(companyId);
    }

    /**
     * To get list of debated terms for a term
     *
     * @param termInformationList List of term objects from which it is to be filtered
     * @param chartData           String of type chartData
     * @return List of debated terms from a given list of term id's
     */
    public static List<TermVoteMaster> getTermVoteMasterByTermInfo(List<Integer> termInformationList, String chartData) {
        return termsService.getTermVoteMasterByTermInfo(termInformationList, chartData);
    }

    /**
     * To get TermList for reports
     *
     * @param termVoteMasterList  Holds the debated terms
     * @param termInformationList Holds the glossary terms
     * @return List of terms for report
     */
    public static List<Terms> getTermList(List<TermVoteMaster> termVoteMasterList, List<TermInformation> termInformationList) {
        return termsService.getTermList(termVoteMasterList, termInformationList);
    }

    /**
     * To  write tbx
     *
     * @param termInfoList list to be written in TBX
     * @param absolutePath temporary path to create a file
     * @return TBX file in which data is written
     */
    public static <C extends TeaminologyObject> File writeTBX(List<TermInformationTo> termInfoList, String absolutePath) {
        return importExportService.writeTBX(termInfoList, absolutePath);
    }


    /**
     * To get list of term information by languageIds in a year
     *
     * @param languageIds Set of languageIds to  filter terms
     * @param companyId   An Integer set to filter terms
     * @return List of TermInformation in a year
     */
    public static List<TermInformation> getTermInformationPerMonth(Set<Integer> languageIds, Integer companyId) {
        return termsService.getTermInformationPerMonth(languageIds, companyId);
    }

    /**
     * To  write tmx
     *
     * @param tmProfileInfoList list to be written in TMX
     * @param absolutePath temporary path to create a file
     * @return TBX file in which data is written
     */
    public static <C extends TeaminologyObject> File writeTMX(List<TmProfileInfo> tmProfileInfoList, String absolutePath) {
        return importExportService.writeTMX(tmProfileInfoList, absolutePath);
    }

    /**
     * get User To Export
     *
     * @param roleIds     Set collection which includes role id's to be filtered
     * @param languageIds Set collection which includes language id's to be filtered
     * @return List of users to be exported w.r.t the role id's and language id's
     * @parm company Company to filter users respectively
     */

    public static List<User> getUserToExport(Set<Integer> roleIds, Set<Integer> languageIds, Company company,  Set<Integer> slctCompanyIds) {
    	
    	Set<Company> companies = new HashSet<Company>();
    	    	
    	if(slctCompanyIds != null && slctCompanyIds.size() > 0) {
    		List<Company> slctCompaniesList = lookUpService.getCompanyListObj(slctCompanyIds);
    		if(slctCompaniesList != null && slctCompaniesList.size() > 0) {
    			companies.addAll(slctCompaniesList);
    		}
    	} else if(!company.getCompanyName().equalsIgnoreCase(CompanyEnum.COMPANY_NAME.getValue())) {
    		companies.add(company);
    	}
    	
        return userService.getUserToExport(roleIds, languageIds, companies);
        
    }


    /**
     * To Get users
     *
     * @param languageId String to filter terms respectively
     * @param colName    Column name that has to be sorted
     * @param sortOrder  Order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @param userId     Integer to filter terms respectively
     * @param companyId  Integer to filter terms respectively
     * @return List of users other than administrator
     */

    public static List<Member> getTeamMemberDetails(String languageId, String companyIds, String colName,
                                                    String sortOrder, Integer pageNum, Integer userId, Integer companyId) {
        return userService.getTeamMemberDetails(languageId, companyIds, colName,
                sortOrder, pageNum, userId, companyId);
    }

    /**
     * To get total votes per language
     *
     * @param languageId An Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return an integer which contains the count of total votes
     */
    public static Integer getTotalVotesPerLang(Integer languageId, Integer companyId) {
        return termsService.getTotalVotesPerLang(languageId, companyId);
    }


    /**
     * To get monthly votes per language
     *
     * @param languageId An Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return an integer which contains the count of votes in a month
     */
    public static Integer getMonthlyVotesPerLang(String languageId, Integer companyId) {
        return termsService.getMonthlyVotesPerLang(languageId, companyId);
    }

    /**
     * To get user accuracy rated values
     *
     * @param userId    An Integer to filter  users
     * @param statusId  An Integer to filter users
     * @param companyId An Integer to filter users
     * @return A collection which contains the finalised voted terms count and total voted terms count
     */

    public static Map<String, BigInteger> getUserAccuracyRate(Integer userId, Integer statusId, Integer companyId) {
        return userService.getUserAccuracyRate(userId, statusId, companyId);
    }

    /**
     * To get Active Polls
     *
     * @param teamVoterList Collection holding list of objects
     * @return An integer which contains the count of active invitations
     */
    public static Integer getActivePolls(List<TermVoteMaster> teamVoterList) {
        return termsService.getActivePolls(teamVoterList);
    }

    /**
     * To get language for a language ID
     *
     * @param languageId Integer to get language
     * @return Language w.r.t the languageId
     */
    public static Language getLanguage(Integer languageId) {
        return lookUpService.getLanguage(languageId);
    }

    /**
     * To get status data
     *
     * @return List of Status object's
     */
    public static List<Status> getStatusLookUp() {
        return lookUpService.getStatusLookUp();
    }

    /**
     * To get language lookup data
     *
     * @return List of Language object's
     */
    public static List<Language> getLanguages() {
        return lookUpService.getLanguages();
    }

    /**
     * To get list of termIds from term information by languageId and statusId
     *
     * @param languageId An Integer to filter terms
     * @param statusId   An Integer to filter terms
     * @param companyId  An Integer to filter the terms
     * @return List of term id's
     */
    public static List<Integer> getTermInformationByLanguageAndStatus(Integer languageId, Integer statusId, Integer companyId) {
        return termsService.getTermInformationByLanguageAndStatus(languageId, statusId, companyId);
    }

    /**
     * To Update user
     *
     * @param user     User to update
     * @return Status "success" if successfully updated else "failed"
     */
    public static String updateUser(User user) {
        try {
            String status = userService.updateUser(user);
            return status;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";

    }


    /**
     * To Update user
     *
     * @param userId     User to update
     * @return Status "success" if successfully updated else "failed"
     */
    public static User getUserByUserId(Integer userId) {
        try {
            User user = userService.getUser(userId);
            return user;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * To get final terms by sorting the terms
     *
     * @param terms holds the termsList
     * @param type  holds type possible values are "Yearly","Quarterly","Monthly"
     * @return List of terms sorting accordingly
     */
    public static List<Terms> getFinalisedTerms(List<Terms> terms, String type) {
        return termsService.getFinalisedTerms(terms, type);
    }

    /**
     * @param globalSightTermInfoList GlobalsightTermInformation Object list
     * @param absolutePath            absolute path of file
     * @return XLIFF file in which data is written
     */
    public static File exportXliff(List<GlobalsightTermInfo> globalSightTermInfoList, String absolutePath) {
        return importExportService.exportXliff(globalSightTermInfoList, absolutePath);
    }

    /**
     * to get GlobalsighttermInformation list Object using pageId
     *
     * @param pageId pageId of xliff file
     * @return GlobalsighttermInformation list
     */
    public static List<GlobalsightTermInfo> gettermsUsingPageId(Integer pageId) {
        return gsService.getTermsByPageId(pageId);
    }

    /**
     * To get file info
     *
     * @param pageId An Integer to get the fileInfo details
     * @return FileInfo w.r.t pageId
     */
    public static FileInfo getFileInfo(Integer pageId) {
        return gsService.getFileInfo(pageId);
    }

    /**
     * To update file status
     *
     * @param fileInfo FileInfo that has to be updated
     */
    public static void updateFileStatus(FileInfo fileInfo) {
        gsService.updateFileStatus(fileInfo);
    }

    /**
     * To get termInformationTM
     *
     * @param exportBy    String to filter the terms
     * @param selectedIds Integer Array of filter values
     * @return List of terms w.r.t to the filter criteria
     */
    public static List<TermInformation> getTermInformationTM(String exportBy, Integer[] selectedIds) {
        return termsService.getTermInformationTM(exportBy, selectedIds);
    }

    /**
     * To get total terms in tms
     *
     * @param exportBy    String to filter the  tm terms
     * @param selectedIds Integer Array of filter values
     * @return List of TmProfileInfo obj's w.r.t selected ids
     */
    public static List<TmProfileInfo> getTotalTermsInTM(String exportBy, Integer[] selectedIds) {
        return termsService.getTotalTermsInTM(exportBy, selectedIds);
    }

    /**
     * To get Tms in Glossary per year
     *
     * @return List of data holding year and no of terms per year
     */
    public static List<Terms> getTmsInGlossary(Set<Integer> companyIds) {
        return termsService.getTmsInGlossary(companyIds);

    }

    /**
     * To get final terms by sorting the tms
     *
     * @param terms holds the termsList
     * @param type  holds type possible values are "Yearly","Quarterly","Monthly"
     * @return List of terms sorting accordingly
     */
    public static List<Terms> getFinalisedTms(List<Terms> terms, String type) {
        return termsService.getFinalisedTms(terms, type);
    }

    /**
     * To get user role  for a userId
     *
     * @param userId Integer to get user role
     * @return Set of user role w.r.t the userId
     */
    public static Set<UserRole> getUserRole(Integer userId) {
        Set<UserRole> userRoles;
        try {
            userRoles = loginService.getUserRole(userId);
            return userRoles;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * To save import tmx file url
     *
     * @param fileId String to filter FileUploadStatus  details
     */
    public static void saveImportTMXFileUrl(String filePath, String fileId) {

        termsService.saveImportTMXFileUrl(filePath, fileId);
    }

    /**
     * To get file upload status
     *
     * @param fileId Integer to filter FileUploadStatus  details
     * @return FileUploadStatus w.r.t fileId
     */
    public static FileUploadStatus getFileUploadStatus(String fileId) {

        return termsService.getFileUploadStatus(new Integer(fileId));
    }

    /**
     * To update FileUploadStatus
     *
     * @param fileUpload FileUploadStatus to be updated
     */
    public static void updateFileUploadStatus(FileUploadStatus fileUpload) {
        termsService.updateFileUploadStatus(fileUpload);

    }

    /**
     * To get company by context root
     *
     * @param contextRoot String  to get company details
     * @return Company w.r.t the contextRoot
     */
    public static Company getCompanyByContextRoot(String contextRoot) {

        return lookUpService.getCompanyByContextRoot(contextRoot);
    }

    /**
     * To get company LookUp By Cache
     *
     * @return List of company obj's
     */
    public static List<Company> getCompanyLookUp() {
        return lookUpService.getCompanyLookUpByCache();
    }

    /**
     * To get list of user type menu details
     *
     * @param roleId Array of Integer to filter user role Details
     * @return List of menu obj's
     */
    public static List<Menu> getRoleMenuDetails(Integer[] roleId) {
        return userService.getRoleMenuDetails(roleId);
    }

    public static List<SubMenu> getRoleSubMenuDetails(Integer[] roleId, Integer menuId) {
        return userService.getRoleSubMenuDetails(roleId, menuId);
    }

    /**
     * To get product group data
     *
     * @return List of ProductGroup object's
     */
    public static List<ProductGroup> getproductGroupLookup() {
        return lookUpService.getProductGroupLookUp();
    }

    /**
     * To get content type lookup data
     *
     * @return List of ContentType object's
     */
    public static List<ContentType> getContentTypeLookup() {
        return lookUpService.getContentType();
    }

    /**
     * To get Domain lookup data
     *
     * @return List of Domain object's
     */
    public static List<Domain> getDomainLookup() {
        return lookUpService.getDomainLookUp();
    }

    /**
     * To get file information by task id
     *
     * @param taskId Integer of filter task ids
     * @return FileInfo w.r.t taskId
     */
    public static FileInfo getFileInfoByTaskId(Integer taskId) {
        return gsService.getFileInfoByTaskId(taskId);
    }

    /**
     * To get Role Id by roleName
     *
     * @param roleName string to get Role
     * @return Role w.r.t the roleName
     */
    public static Role getRoleIdByLabel(String roleName) {
        return userService.getRoleIdByLabel(roleName);
    }

    /**
     * To get tm profile info by ids
     *
     * @param ids List of Integer to  filter the  terms
     * @return List of terms
     */
    public static List<TmProfileInfo> getTmProfileInfoByIds(List<Integer> ids) {
        return termsService.getTmProfileInfoByIds(ids);

    }

    /**
     * To get company tranMgmt users
     *
     * @param userId Integer to filter user  Details
     * @return set of companyTransMgmt obj's  w.r.t the userId
     */
    public static Set<CompanyTransMgmt> getCompanyTransMgmtUsers(Integer userId) {
        return userService.getCompanyTransMgmtUsers(userId);
    }

	public static List<TermUpdateDetails> getHistoryUpdateDetails(List<Integer> termIdsList, String toDate, String fromDate) {
		// TODO Auto-generated method stub
		return termsService.getHistoryDetails(termIdsList, toDate, fromDate);
	}

	public static <C extends TeaminologyObject> File writeHistoryCSV(List<C> exportList, String absolutePath, String uploadedFilesPath, String exportFormate) {
        return importExportService.writeHistoryCSV(exportList, absolutePath, uploadedFilesPath, exportFormate);
		
	}
}
