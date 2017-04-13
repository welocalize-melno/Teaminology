package com.teaminology.hp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.FileUploadStatus;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.GlobalsightTerms;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermTranslation;
import com.teaminology.hp.bo.TermUpdateDetails;
import com.teaminology.hp.bo.TermVoteMaster;
import com.teaminology.hp.bo.TmProfileInfo;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.data.GSJobObject;
import com.teaminology.hp.data.HistoryDetailsData;
import com.teaminology.hp.data.Invite;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.PollTerms;
import com.teaminology.hp.data.QueryAppender;
import com.teaminology.hp.data.TMProfileTermsResult;
import com.teaminology.hp.data.TermAttributes;
import com.teaminology.hp.data.TermVotingTo;
import com.teaminology.hp.data.Terms;
import com.teaminology.hp.data.UserComment;
import com.teaminology.hp.data.VotingStatus;


/**
 * Contains method prototype to deal with terms.
 *
 * @author sarvanic
 */
public interface ITermDetailsService
{

    /**
     * To get Leader Board Members
     *
     * @param companyId An Integer to be filtered
     * @return List of leader board members
     */

    public List<Member> getBoardMembers(Integer companyId);

    
    public    List<TermVotingTo> getVoteResults(String fromDate,String toDate,Integer companyId);
    
    /**
     * To get Terms in Glossary per year
     *
     * @param companyId An Integer set to be filtered
     * @return List of data holding year and no of terms per year
     */

    public List<Terms> getTermsInGlossary(Set<Integer> companyId);

    /**
     * To Get Debated Terms per year
     *
     * @param companyId An Integer set  to be filtered
     * @return List of data holding year and no of terms per year
     */

    public List<Terms> getDebatedTerms(Set<Integer> companyId);

    /**
     * To get Total terms in Glossary
     *
     * @param companyId An String  set to  filter the  terms
     * @return An int value holding the total no of terms in glossary
     */

    public int getTotalTermsInGlossary(Set<Integer> companyId);


    /**
     * To get Total debated terms
     *
     * @param companyId An Integer set to  filter the  terms
     * @return An integer value holding the total no of debated terms
     */
    public int getTotalDebatedTerms(Set<Integer> companyId);


    /**
     * To get current debated terms
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of current debated terms
     */

    public List<String> getTopTerms(Integer companyId);

//	public List<PollTerms> getExpiredPollTerms(int pageNum);

    /**
     * To get term attributes for a termID
     *
     * @param termId Integer which contains termId to be filtered
     * @param userId Integer which contains userId to be filtered
     * @return TermInformation w.r.t the term id
     */
    public TermAttributes getTermAttributes(Integer termId, Integer userId);

    /**
     * To get monthly glossary terms
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of data holding month and no of terms per month
     */

    public List<Terms> getMonthlyTermDetails(Integer companyId);


    /**
     * To get monthly debated term details
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of data holding month and no of terms per month
     */
    public List<Terms> getMonthlyDebatedTerms(Integer companyId);

    /**
     * To update Term Details
     *
     * @param termInformation TermInformation that has to be updated
     */
    public void updateTermDetails(TermInformation termInformation,  String isReplace);

    /**
     * To extend poll of expired term id
     *
     * @param termVoteMaster TermVoteMaster that has to be updated
     */

    public void extendPoll(TermVoteMaster termVoteMaster);

    /**
     * To get Term Vote Master details of a termId
     *
     * @param termId An Integer to get details
     * @return TermVoteMaster w.r.t the term id
     */
    public TermVoteMaster getTermVoteMaster(Integer termId);


    /**
     * To get quarterly term details
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of data holding quarter-year and no of terms per quarter
     */

    public List<Terms> getQuarterlyTermDetails(Integer companyId);

    /**
     * To get quarterly debated term details
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of data holding quarter-year and no of terms per quarter
     */
    public List<Terms> getQuarterlyDebatedTerms(Integer companyId);


    /**
     * To approve a suggested term for a term Id
     *
     * @param suggestedTermId An Integer to get details
     * @return If approved it returns 1 else 0
     */
    public int approveSuggestedTerm(Integer suggestedTermId);

    /**
     * To sort expired poll terms
     *
     * @param colName   Column name that has to be sorted
     * @param sortOrder Order in which it has to be sorted
     * @param langIds   String containing language id's
     * @param companyId An Integer to filter terms
     * @param pageNum   Integer to limit the data
     * @return List of terms
     */

    public List<PollTerms> sortOrFilterExpPollTerms(String colName, String sortOrder, String langIds, Integer pageNum, Integer companyId, String expTermCompanyIds);

    /**
     * To vote a term
     *
     * @param termTranslation TermTranslation to count on vote
     * @param userId          Integer to set in updatedBy field
     * @return If term is voted it returns success else failed
     */

    public String voteTerm(TermTranslation termTranslation, Integer userId);

    /**
     * To Get user poll terms for a language Id
     *
     * @param languageId String to filter terms respectively
     * @param colName    Column name that has to be sorted
     * @param sortOrder  Order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @param userId     Integer to filter terms respectively
     * @return List of terms
     */

    public List<PollTerms> getUserPollTerms(String languageId, String colName, String sortOrder, Integer pageNum, Integer userId);


    /**
     * To reject a term by user
     *
     * @param termId TermId to be rejected
     * @param userId UserId that rejects the term
     */

    public void rejectTerm(Integer termId, Integer userId);

//	public List<PollTerms> getUserVotedTerms(String languageId,Integer userId);

    /**
     * Add new term.
     *
     * @param termInformation TermInformation that needs to be added
     * @return If term is voted it returns success else failed
     */

    public String addNewTerm(TermInformation termInformation);


    /**
     * Invite to vote
     *
     * @param termVoteMaster Invitation that need to be saved
     * @param invite         Includes term id's that needs to be invited and the user id's
     */
    public void inviteToVote(TermVoteMaster termVoteMaster, Invite invite);


    /**
     * To manage poll terms for a language Id
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter terms
     * @param user          User to filter terms
     * @return List of terms
     */

    public TMProfileTermsResult getManagePollTerms(QueryAppender queryAppender, Integer companyId, User user);


    /**
     * To Search Manage poll terms for a language Id
     *
     * @param queryAppender Which is used to build a query
     * @return List of terms
     *
     */

    // public List<PollTerms> getSearchManagePollTerms(QueryAppender queryAppender);

    /**
     * To get Search manage poll terms for tms
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter the tm terms
     * @param user          to filter the tms terms
     * @return List of terms
     */
    public List<GSJobObject> getSearchManagePollTermsTM(QueryAppender queryAppender, Integer companyId, User user);

    /**
     * To get Search manage term base poll terms for a language Id
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter the  terms
     * @param user          to filter the terms
     * @return terms
     */
    public TMProfileTermsResult getSearchManagePollTermsTermBase(QueryAppender queryAppender, Integer companyId, User user);

    /**
     * To delete term ids
     *
     * @param termIds   An Integer array that needs to be deleted
     * @param companyId An Integer to be filtered
     */
    public void deleteTerms(final Integer[] termIds, User user);

    /**
     * To get termInformation
     *
     * @param exportBy    String to filter the terms
     * @param selectedIds Integer Array of filter values
     * @return List of terms
     */

    public List<TermInformation> getTermInformation(String exportBy, Integer[] selectedIds, Integer companyId);

    /**
     * To get termInformation
     *
     * @param exportBy    String to filter the terms
     * @param selectedIds Integer Array of filter values
     * @return List of terms
     */

    public List<TermInformation> getTermInformationTM(String exportBy, Integer[] selectedIds);

    /**
     * To Get user voted terms for a language Id
     *
     * @param languageId String to filter terms respectively
     * @param colName    Column name that has to be sorted
     * @param sortOrder  Order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @param userId     Integer to filter terms respectively
     * @return List of terms that are voted by the user
     */

    public List<PollTerms> getUserVotedTerms(String languageId, String colName, String sortOrder, Integer pageNum, Integer userId);

    /**
     * To get top register languages
     *
     * @param companyId An Integer to filter languages
     * @return List of top six registered languages
     */
    public List<Language> getTopRegLangs(Integer companyId);

    /**
     * To get list of termIds from term information by languageId
     *
     * @param languageId An Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return List of term id's
     */

    public List<Integer> getTermInformationByLanguage(Integer languageId, Integer companyId);

    /**
     * To get list of termIds from term information by languageId and statusId
     *
     * @param languageId Integer to filter terms
     * @param statusId   Integer to filter terms
     * @return List of term id's
     */

    public List<Integer> getTermInformationByLanguageAndStatus(Integer languageId, Integer statusId, Integer companyId);


    /**
     * To get count of term information other than top 6 registered languages
     *
     * @param languageIds Set of languageIds to be filtered
     * @param companyId   An Integer set to be filtered
     * @return an integer value holding the count of term id's for languages other than top 6 registered
     */

    public Integer getTermInformationForOtherLanguage(Set<Integer> languageIds, Integer companyId);

    /**
     * To get list of debated terms for a term
     *
     * @param termInformationList List of term objects from which it is to be filtered
     * @param type                String for the type to be filtered
     * @return List of debated terms from a given list of term id's
     */

    public List<TermVoteMaster> getTermVoteMasterByTermInfo(List<Integer> termInformationList, String type);


    /**
     * To get TermList for reports
     *
     * @param termVoteMasterList  Holds the debated terms
     * @param termInformationList Holds the glossary terms
     * @return List of terms for report
     */
    public List<Terms> getTermList(List<TermVoteMaster> termVoteMasterList, List<TermInformation> termInformationList);

    /**
     * To get list of term information by languageIds in a year
     *
     * @param languageIds Set of languageIds to  filter terms
     * @param companyId   An Integer set to filter terms
     * @return List of TermInformation in a year
     */

    public List<TermInformation> getTermInformationPerMonth(Set<Integer> languageIds, Integer companyId);


    /**
     * To get total votes per language
     *
     * @param languageId An Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return an integer which contains the count of total votes
     */
    public Integer getTotalVotesPerLang(Integer languageId, Integer companyId);


    /**
     * To get monthly votes per language
     *
     * @param languageId An Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return an integer which contains the count of votes in a month
     */
    public Integer getMonthlyVotesPerLang(String languageId, Integer companyId);

    /**
     * To get Active Polls
     *
     * @param termIds Set of term id's that needs to be filtered
     * @return An integer which contains the count of active invitations
     */

    public Integer getActivePolls(List<TermVoteMaster> termIds);

    /**
     * To get inivted user details
     *
     * @param invites Contains a list of userIds and termIds
     * @return An Integer[] array object which contains the list of users that are not invited for the terms
     */

    public Integer[] getInvitedUsers(Invite invites);

    /**
     * To get final terms by sorting the terms
     *
     * @param terms holds the termsList
     * @param type  holds type possible values are "Yearly","Quarterly","Monthly"
     * @return List of terms sorting accordingly
     */
    public List<Terms> getFinalisedTerms(List<Terms> terms, String type);

    /**
     * To delete vote details for users
     *
     * @param userIds Integer for which vote data has to be updated
     * @return Status if updated vote details successfully it returns "success" else "failure"
     */
    public String deleteTermVoteDetailsForUser(Integer[] userIds,Integer companyId);

    /**
     * To get voting status of voted terms
     *
     * @param termId variable to get particular termId
     * @return VotingStatusList w.r.t the term id
     */

    public List<VotingStatus> getvotingStatus(Integer termId);

    /**
     * To get list of termIds from term information by categoryId
     *
     * @param categoryId An Integer to be filtered
     * @return List of term id's
     */
    public List<Integer> getTermInformationByCategory(Integer categoryId);

    /**
     * To save term image
     *
     * @param termId    Path variable  to get particular attributes
     * @param photoPath
     * @param request   HttpServletRequest
     */

    public void saveTermImage(Integer termId, String photoPath);


    /**
     * To get global sight term information
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to filter the terms
     * @return terms
     */
    /*

	 public Integer addNewGlobalSightTerm(TermInformation termInformation,GlobalsightTermInfo globalSightTermInfo);*/
    public TMProfileTermsResult getGlobalSightTermInfo(QueryAppender queryAppender, Integer companyId);

    /**
     * To get file info list
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter the  terms
     * @return List of terms
     */
    public List<GlobalsightTerms> getFileInfoList(QueryAppender queryAppender, Integer companyId);

    /**
     * To get Total terms in Glossary
     *
     * @param isTm An String  to  filter the  terms
     * @return A int value holding the total no of terms in Glossary
     */

    public Integer getTotalTermsInTermBaseTM(String isTM);

    /**
     * To get TM profile terms
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter the  terms
     * @return terms
     */

    public TMProfileTermsResult getTMProfileTerms(QueryAppender queryAppender, Integer companyId, User user);

    /**
     * To get Total terms in TM
     *
     * @param companyIds an Integer  set to  filter the  terms
     * @return A int value holding the total no of terms in TM
     */
    public Integer getTotalTermsInTM(Set<Integer> companyId);

    /**
     * To get term attributes for a termID
     *
     * @param tmProfileInfoId An Integer to get details
     * @return TermInformation w.r.t the term id
     */
    public TmProfileInfo getTmAttributes(Integer tmProfileInfoId);

    /**
     * To get total terms in tms
     *
     * @param exportBy    String to filter the  tm terms
     * @param selectedIds Integer Array of filter values
     * @return List of TmProfileInfo obj's
     */
    public List<TmProfileInfo> getTotalTermsInTM(String exportBy, Integer[] selectedIds);

    /**
     * To update Term Details
     *
     * @param termInformation TermInformation that has to be updated
     * @param companyId       An Integer to be filtered
     */
    public void updateTmDetails(TmProfileInfo tmProfileInfo, Integer companyId);

    /**
     * To delete tms terms
     *
     * @param termIds   An Integer array that needs to be deleted
     * @param companyId An Integer to  filter the  terms
     */
    public void deleteTms(final Integer[] termIds, User user);

    /**
     * To update gs term details
     *
     * @param gsTermInformation GlobalsightTermInfo that has to be updated
     */
    public void updateGSTermDetails(GlobalsightTermInfo gsTermInformation);

    /**
     * To get gs term information using term id
     *
     * @param termId An Integer to get details
     * @return GlobalsightTermInfo w.r.t the term id
     */
    public GlobalsightTermInfo getGSTermInfoUsingTermId(Integer termId);

    /**
     * To get total gs terms
     *
     * @return A integer  value holding the total no of gs terms in Glossary
     */
    public Integer getTotalGSTerms();

    /**
     * To delete gs file data
     *
     * @param fileIds An Integer array that needs to be deleted
     */
    public void deleteGSFileData(Integer[] fileIds , User user);

    /**
     * To delete gs terms
     *
     * @param termIds   An Integer array that needs to be deleted
     * @param companyId An Integer to filter the  terms
     */
    public void deleteGSTerms(Integer[] termIds, Integer companyId);

    /**
     * To get total tm terms by search
     *
     * @param queryAppender Which is used to build a query
     * @return A integer  value holding the total no of  tm terms in Glossary
     */

    public Integer getTotalTMTermsBySearch(QueryAppender queryAppender);

    /**
     * To get Tms in Glossary per year
     *
     * @param companyId An Integer set  to  filter the  terms
     * @return List of data holding year and no of terms per year
     */

    public List<Terms> getTmsInGlossary(Set<Integer> companyId);

    /**
     * To get tms by sorting the terms
     *
     * @param tms  holds the termsList
     * @param type holds type possible values are "Yearly","Quarterly","Monthly"
     * @return List of terms sorting accordingly
     */
    public List<Terms> getFinalisedTms(List<Terms> terms, String type);

    /**
     * To get quarterly tm details
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of data holding quarter-year and no of terms per quarter
     */

    public List<Terms> getQuarterlyTmDetails(Integer companyId);

    /**
     * To get monthly glossary tms
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of data holding month and no of terms per month
     */

    public List<Terms> getMonthlyTmDetails(Integer companyId);

    /**
     * To save import tmx file
     *
     * @param fileUpload FileUploadStatus  to be saved
     */
    public void saveImportTMXFile(FileUploadStatus fileUpload);

    /**
     * To get file upload status
     *
     * @param fileId Integer to filter FileUploadStatus  details
     * @return FileUploadStatus obj's
     */
    public FileUploadStatus getFileUploadStatus(Integer fileId);

    /**
     * To save import tmx file url
     *
     * @param fileId String to filter FileUploadStatus  details
     */
    public void saveImportTMXFileUrl(String filePath, String fileId);

    /**
     * To get FileUploadstatus details
     *
     * @param request   HttpServletRequest
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of FileUploadstatus obj's
     */

    public List<FileUploadStatus> getImportExportData(Integer userId, String colName, String sortOrder, Integer pageNum);

    /**
     * To update FileUploadStatus
     *
     * @param fileUpload FileUploadStatus to be updated
     */
    public void updateFileUploadStatus(FileUploadStatus fileUpload);

    /**
     * To get all jobs
     *
     * @param fileUpload FileUploadStatus to be updated
     * @return List of FileInfo obj's
     */
    public List<FileInfo> getAllJobs();

    /**
     * To get file information by jobIds
     *
     * @param jobIds String  Array of filter jobIds
     * @return List of FileInfo w.r.t job id's
     */
    public List<FileInfo> getFileInfoByJobIds(String[] jobIds);

    /**
     * To save file information
     *
     * @param fileInfo FileInfo to be saved
     */

    public void saveFileInfo(FileInfo fileInfo);

    /**
     * To get all terms
     *
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @param companyId     An Integer to  filter the  terms
     * @return List of term ids that needs to manage
     */

    public List<Integer> getAllTerms(QueryAppender queryAppender,
                                     Integer companyId);

    /**
     * To get total tm terms
     *
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @param companyId     An Integer to  filter the  terms
     * @return List of tm profile  ids that needs to manage
     */
    public List<Integer> getTotalTms(QueryAppender queryAppender, Integer companyId);

    /**
     * To get total gs terms
     *
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @param companyId     An Integer to  filter the  terms
     * @return List of gs term ids that needs to manage
     */
    public List<Integer> getTotalGSTerms(QueryAppender queryAppender, Integer companyId);

    /**
     * To get tm profile info by ids
     *
     * @param ids List of Integer to  filter the  terms
     * @return List of terms
     */
    public List<TmProfileInfo> getTmProfileInfoByIds(List<Integer> ids);

    /**
     * To delete import files
     *
     * @param fileIds array of integer fileIds that needs to be deleted
     */

    public void deleteImportFiles(Integer[] fileIds);

    /**
     * To get import  status files by fileIds
     *
     * @param fileIds Integer array  to  filter the  import files status
     * @return List of import files obj w.r.t fileIds
     */
    public List<FileUploadStatus> getImportStatusFiles(Integer[] fileIds);

    /**
     * To get ids of terms belongs to given companyid
     *
     * @param companyId of a company
     * @return termIds array of integer
     */
    public Integer[] getTermIdsByCompanyId(Integer companyId);

    public TMProfileTermsResult getSelectedTermsOnly(QueryAppender queryAppender,Company company);


	public List<UserComment> getUserComment(Integer termId);


	public TMProfileTermsResult getManagePollTermsForPagination(QueryAppender queryAppender, Integer companyId, User user);


	public Integer getTotalVotesOfUser(Integer user_id);


	public List<Member> getAllBoardMembersByLanguage(Integer companyId, String user_lang_id);
	
	
    public List<HistoryDetailsData> getHistoryDatails(Integer termId);


	public List<TermUpdateDetails> getHistoryDetails(List<Integer> termIdsList, String toDate, String fromDate);


}