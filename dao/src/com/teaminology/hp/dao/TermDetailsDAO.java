package com.teaminology.hp.dao;

import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.teaminology.hp.bo.Attributes;
import com.teaminology.hp.bo.CompanyTransMgmt;
import com.teaminology.hp.bo.DeprecatedTermInformation;
import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.FileUploadStatus;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.GlobalsightTerms;
import com.teaminology.hp.bo.TMProperties;
import com.teaminology.hp.bo.Tag;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermTranslation;
import com.teaminology.hp.bo.TermUpdateDetails;
import com.teaminology.hp.bo.TermVoteMaster;
import com.teaminology.hp.bo.TermVoteUserDetails;
import com.teaminology.hp.bo.TmProfileInfo;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.data.GSJobObject;
import com.teaminology.hp.data.HistoryDetailsData;
import com.teaminology.hp.data.Invite;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.PollTerms;
import com.teaminology.hp.data.QueryAppender;
import com.teaminology.hp.data.SuggestedTermDetails;
import com.teaminology.hp.data.TermVotingTo;
import com.teaminology.hp.data.Terms;

/**
 * Conatins DAO Interface methods to handle terms.
 *
 * @author sarvaniC
 */

public interface TermDetailsDAO
{

    /**
     * To get Leader Board Members
     *
     * @param companyId Integer to be filtered
     * @return List of Board Members
     * @throws DataAccessException
     */
    public List<Member> getBoardMembers(Integer companyId) throws DataAccessException;
    
    
    
    public  List<TermVotingTo> getVoteResults(final String fromDate,final String toDate, final Integer companyId) throws DataAccessException;

    /**
     * To get Terms in Glossary per year
     *
     * @param companyId String to be filtered
     * @return List of data holding year and number of terms per year
     * @throws DataAccessException
     */

    public List<Terms> getTermsInGlossary(String companyIds) throws DataAccessException;

    /**
     * To Get Debated Terms per year
     *
     * @param companyId String to be filtered
     * @return List of data holding year and number of terms per year
     * @throws DataAccessException
     */

    public List<Terms> getDebatedTerms(String companyIds) throws DataAccessException;

    /**
     * To get Total terms in Glossary
     *
     * @param companyId String to be filtered
     * @return An int value holding the total number of terms in glossary
     * @throws DataAccessException
     */

    public int getTotalTermsInGlossary(String companyId) throws DataAccessException;

    /**
     * To get Total debated terms
     *
     * @param companyId String to be filtered
     * @return a int value holding the total number of debated terms
     * @throws DataAccessException
     */

    public int getTotalDebatedTerms(String companyId) throws DataAccessException;


    /**
     * To get current debated terms
     *
     * @param companyId Integer to be filtered
     * @return List of current debated terms
     * @throws DataAccessException
     */

    public List<String> getTopTerms(Integer companyId) throws DataAccessException;

    /**
     * To get term attributes for a termID
     *
     * @param termId Integer which contains termId to be filtered
     * @return TermInformation w.r.t the term id
     * @throws DataAccessException
     */

    public TermInformation getTermAttributes(Integer termId) throws DataAccessException;

    /**
     * To get Suggested terms for a termID
     *
     * @param termId Integer which contains termId to be filtered
     * @return List of suggested term details w.r.t the term id
     * @throws DataAccessException
     */

    public List<SuggestedTermDetails> getSuggestedTerms(Integer termId) throws DataAccessException;

    /**
     * To get monthly glossary terms
     *
     * @param companyId Integer to be filtered
     * @return List of data holding month and no of terms per month
     * @throws DataAccessException
     */

    public List<Terms> getMonthlyTermDetails(Integer companyId) throws DataAccessException;

    /**
     * To get monthly debated term details
     *
     * @param companyId Integer to be filtered
     * @return List of data holding month and no of terms per month
     * @throws DataAccessException
     */

    public List<Terms> getMonthlyDebatedTerms(Integer companyId) throws DataAccessException;


    /**
     * To update Term Details
     *
     * @param termInformation TermInformation that has to be updated
     * @return TermInformation
     * @throws DataAccessException
     */
    public TermInformation updateTermDetails(TermInformation termInformation, String isReplace) throws DataAccessException;

    /**
     * To extend poll of expired term id
     *
     * @param termVoteMaster TermVoteMaster that has to be updated
     * @throws DataAccessException
     */

    public void extendPoll(TermVoteMaster termVoteMaster) throws DataAccessException;

    /**
     * To get Term Vote Master details of a termId
     *
     * @param termId the Integer which contains termId to be filtered
     * @return TermVoteMaster w.r.t the term id
     * @throws DataAccessException
     */

    public TermVoteMaster getTermVoteMaster(Integer termId) throws DataAccessException;

    /**
     * To get quarterly term details
     *
     * @param companyId Integer to be filtered
     * @return List of data holding quarter-year and no of terms per quarter
     * @throws DataAccessException
     */

    public List<Terms> getQuarterlyTermDetails(final Integer companyId) throws DataAccessException;

    /**
     * To get quarterly debated term details
     *
     * @param companyId Integer to be filtered
     * @return List of data holding quarter-year and no of terms per quarter
     * @throws DataAccessException
     */

    public List<Terms> getQuarterlyDebatedTerms(Integer companyId) throws DataAccessException;

    /**
     * To approve a suggested term for a term Id
     *
     * @param suggestedTermId Integer which contains suggestedTermId to be filtered
     * @return If approved it returns 1 else 0
     * @throws DataAccessException
     */

    public int approveSuggestedTerm(Integer suggestedTermId) throws DataAccessException;

    /**
     * To sort expired poll terms
     *
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param langIds   String containing language id's
     * @param pageNum   Integer to limit the data
     * @return List of terms
     * @throws DataAccessException
     */

    public List<PollTerms> sortOrFilterExpPollTerms(String colName, String sortOrder, String langIds, Integer pageNum, Integer companyId, String expTermCompanyIds, Integer statusId) throws DataAccessException;

    /**
     * To Get user poll terms for a language Id
     *
     * @param languageId String to filter terms respectively
     * @param colName    column name that has to be sorted
     * @param sortOrder  order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @param userId     Integer to filter terms respectively
     * @return List of Terms
     * @throws DataAccessException
     */

    public List<PollTerms> getUserPollTerms(String languageId, String colName, String sortOrder, Integer pageNum, Integer userId) throws DataAccessException;

    /**
     * To vote a term
     *
     * @param termTranslation TermTranslation to count on vote
     * @param userId          Integer to set in updatedBy field
     * @return If term is voted it returns success else failed
     * @throws DataAccessException
     */

    public String voteTerm(TermTranslation termTranslation, Integer userId) throws DataAccessException;


    /**
     * To reject a term by user
     *
     * @param termId To identify term
     * @param userId Integer that rejects the term
     * @throws DataAccessException
     */

    public void rejectTerm(Integer termId, Integer userId) throws DataAccessException;

    /**
     * To Get user voted terms for a language Id
     *
     * @param languageId String to filter terms respectively
     * @param colName    column name that has to be sorted
     * @param sortOrder  order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @param userId     Integer to filter terms respectively
     * @return List of terms that are voted by the user
     * @throws DataAccessException
     */

    public List<PollTerms> getUserVotedTerms(final String languageId, final String colName, final String sortOrder, final Integer pageNum, final Integer userId) throws DataAccessException;

    /**
     * Add new term.
     *
     * @param termInformation TermInformation that needs to be added
     * @return If term is voted it returns success else failed
     * @throws DataAccessException
     */

    public TermInformation isTargetTermBaseExists(TermInformation termInformation);
    
    public String addNewTerm(TermInformation termInformation) throws DataAccessException;

    /**
     * Add new addNewTermXlif.
     *
     * @param termInformation TermInformation that needs to be added
     * @return If term is added it returns success else failed
     * @throws DataAccessException
     */

    public String addNewTermXliff(TermInformation termInformation) throws DataAccessException;

    /**
     * Invite to vote
     *
     * @param termVoteMaster Invitation that need to be saved
     * @param invite         Includes term id's that needs to be invited and the user id's
     * @throws DataAccessException
     */

    public void inviteToVote(TermVoteMaster termVoteMaster, Invite invite) throws DataAccessException;

    /**
     * To get List of PollTerms
     *
     * @param queryAppender Which is used to build a query
     * @param CompanyId     Integer which is used to get term details
     * @return List of terms
     * @throws DataAccessException
     */
    public List<PollTerms> getManagePollTerms(QueryAppender queryAppender, Integer companyId) throws DataAccessException;


    /**
     * To delete term ids
     *
     * @param termIds Integer array that needs to be deleted
     * @throws DataAccessException
     */

    public void deleteTerms(final Integer[] termIds , User user) throws DataAccessException;


    /**
     * To get termInformation
     *
     * @param exportBy    String to filter the terms
     * @param selectedIds Integer Array of filter values
     * @return List of terms
     * @throws DataAccessException
     */
    public List<TermInformation> getTermInformation(String exportBy, Integer[] selectedIds,Integer companyId) throws DataAccessException;


    /**
     * To get termInformation
     *
     * @param exportBy    String to filter the terms
     * @param selectedIds Integer Array of filter values
     * @return List of terms
     * @throws DataAccessException
     */
    public List<TermInformation> getTermInformationTM(String exportBy, Integer[] selectedIds) throws DataAccessException;

    /**
     * To get top register languages
     *
     * @param companyId An Integer to be filtered
     * @return List of top six registered languages
     * @throws DataAccessException
     */

    public List<Language> getTopRegLangs(Integer companyId) throws DataAccessException;

    /**
     * To get list of termIds from term information by languageId
     *
     * @param languageId An Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return List of term id's
     * @throws DataAccessException
     */

    public List<Integer> getTermInformationByLanguage(Integer languageId, Integer companyId) throws DataAccessException;


    /**
     * To get list of termIds from term information by languageId and statusId
     *
     * @param languageId Integer to be filtered for terms
     * @param statusId   Integer to be filtered for terms
     * @param companyId  An Integer to be filtered
     * @return List of term id's
     * @throws DataAccessException
     */

    public List<Integer> getTermInformationByLanguageAndStatus(Integer languageId, Integer statusId, Integer companyId) throws DataAccessException;


    /**
     * To get list of debated terms in a year
     *
     * @param termIds Set of term id's that needs to be filtered
     * @return List of debated terms in a year
     * @throws DataAccessException
     */

    public List<TermVoteMaster> getTermVoteMasterByTermInfo(Set<Integer> termIds) throws DataAccessException;


    /**
     * To Get debated term ids
     *
     * @return Integer array of debated term id's
     * @throws DataAccessException
     */
    public Integer[] getDebatedTermIds() throws DataAccessException;

    /**
     * To get count of term information other than top 6 registered languages
     *
     * @param languageIds Set of languageIds to be filtered
     * @return An integer value holding the count of term id's for languages other than top 6 registered
     * @throws DataAccessException
     */

    public Integer getTermInformationForOtherLanguage(Set<Integer> languageIds, Integer companyId) throws DataAccessException;

    /**
     * To get list of term information by languageIds in a year
     *
     * @param languageIds Set of languageIds to be filtered
     * @return List of TermInformation in a year
     * @throws DataAccessException
     */

    public List<TermInformation> getTermInformationPerMonth(Set<Integer> languageIds, Integer companyId) throws DataAccessException;


    /**
     * To get total votes per language
     *
     * @param languageId an Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return an integer which contains the count of total votes
     * @throws DataAccessException
     */

    public Integer getTotalVotesPerLang(Integer languageId, Integer companyId) throws DataAccessException;

    /**
     * To get monthly votes per language
     *
     * @param languageId an Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return an integer which contains the count of votes in a month
     * @throws DataAccessException
     */

    public Integer getMonthlyVotesPerLang(String languageId, Integer companyId) throws DataAccessException;

    /**
     * To get Current Debated Terms
     *
     * @param termIds Set of term id's that needs to be filtered
     * @return List of debated terms from the given term id's
     * @throws DataAccessException
     */

    public List<TermVoteMaster> getCurrentDebatedTerms(Set<Integer> termIds) throws DataAccessException;

    /**
     * To get Active Polls
     *
     * @param termIds Set of term id's that needs to be filtered
     * @return an integer which contains the count of active invitations
     * @throws DataAccessException
     */

    public Integer getActivePolls(Set<Integer> termIds) throws DataAccessException;

    /**
     * To get term vote user details for a termId and userId
     *
     * @param termId Integer to be filtered for term vote details
     * @param userId Integer to be filtered for term vote details
     * @return TermVoteUserDetails w.r.t the term id and user id
     * @throws DataAccessException
     */

    public TermVoteUserDetails getTermVoteUserDetails(Integer termId, Integer userId) throws DataAccessException;

    /**
     * To get term translation for a termTranslationId
     *
     * @param termTranslationId Integer to be filtered
     * @return TermTranslation w.r.t the term translation id
     * @throws DataAccessException
     */

    public TermTranslation getTermTranslation(Integer termTranslationId) throws DataAccessException;

    /**
     * To delete vote details for users
     *
     * @param userIds Integer array for which vote data has to be updated
     */
    public void deleteTermVoteDetailsForUser(Integer[] userIds , Integer userId);

    /**
     * To get voting status of voted terms
     *
     * @param termId variable to get particular termId
     * @return VotingStatus object list w.r.t the term id
     */
    public List<Object> getvotingStatus(Integer termId);


    /**
     * To get list of termIds from term information by categoryId
     *
     * @param categoryId An Integer to be filtered
     * @return List of term id's
     * @throws DataAccessException
     */

    public List<Integer> getTermInformationByCategory(Integer categoryId) throws DataAccessException;


    /**
     * To save term image
     *
     * @param termId    Path variable  to get particular attributes
     * @param photoPath
     * @throws DataAccessException
     */
    public void saveTermImage(Integer termId, String photoPath) throws DataAccessException;

    /**
     * To getTM information by search
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to be filtered
     * @return List of terms
     * @throws DataAccessException
     */
    public Object[] getSearchManagePollTermsTM(QueryAppender queryAppender, Integer comapnyId) throws DataAccessException;

    /**
     * To get Termbase Information by search
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to be filtered
     * @return List of terms
     * @throws DataAccessException
     */
    public List<GSJobObject> getSearchManagePollTermsTermBase(QueryAppender queryAppender, Integer comapnyId) throws DataAccessException;

    /**
     * Add new term in TermInformation,GlobalsightTermInfo
     *
     * @param termInformation,globalSightTermInfo
     *         TermInformation that needs to be added
     * @return If term added successfully termId of inserted termInformation else termId as 0.
     * @throws DataAccessException
     */

    public Integer saveGlobalSightTerm(GlobalsightTermInfo globalSightTermInfo) throws DataAccessException;

    /**
     * Add new term in Term Translation and Global Sight.
     *
     * @param termInformation,globalSightTermInfo
     *         Term Translation,Global Sight that needs to be added
     * @return If term is added successfully "Success", else "failed".
     * @throws DataAccessException
     */
    public String saveTranslation(TermTranslation termTranslation) throws DataAccessException;

    /**
     * To get terms by using page id
     *
     * @param pageId An Integer to get the details
     * @return List of GlobalsightTermInfo obj's
     * @throws DataAccessException
     */
    public List<GlobalsightTermInfo> getTermsByPageId(Integer pageId) throws DataAccessException;

    /**
     * To get globalSightTermInfo
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter the  terms
     * @return List of terms
     * @throws DataAccessException
     */
    public List<GlobalsightTerms> getGlobalSightTermInfo(QueryAppender queryAppender, Integer companyId) throws DataAccessException;

    /**
     * To save the file
     *
     * @param fileInfo FileInfo that has to be saved
     * @return An integer value holding the fileInfoId
     * @throws DataAccessException
     */
    public Integer saveFile(FileInfo fileInfo) throws DataAccessException;

    /**
     * To get file info list
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter the  terms
     * @return List of terms
     * @throws DataAccessException
     */
    public List<GlobalsightTerms> getFileInfoList(QueryAppender queryAppender, Integer companyId) throws DataAccessException;

    /**
     * To update file status
     *
     * @param fileInfo FileInfo that has to be updated
     * @throws DataAccessException
     */
    public void updateFileStatus(FileInfo fileInfo) throws DataAccessException;

    /**
     * To get file info
     *
     * @param pageId An Integer to get the fileInfo details
     * @return FileInfo w.r.t the pageId
     * @throws DataAccessException
     */
    public FileInfo getFileInfo(Integer pageId) throws DataAccessException;

    /**
     * To get term information
     *
     * @param termId Integer which contains termId to be filtered
     * @return TermInformation w.r.t the term id
     * @throws DataAccessException
     */

    public TermInformation getTermInformation(Integer termId) throws DataAccessException;

    /**
     * To update  term information
     *
     * @param termInfo TermInformation that has to be updated
     * @throws DataAccessException
     */
    public void updateTermInfo(TermInformation termInfo) throws DataAccessException;

    /**
     * To get Total terms in TM
     *
     * @return An integer value holding the total no of terms in TM
     * @throws DataAccessException
     */
    public Integer getTotalTermsInTermBaseTM(String isTM) throws DataAccessException;

    /**
     * To verify whether TM  exists or not in database
     *
     * @return Returns true if tm exists else it returns false
     * @throws DataAccessException
     */
    public Boolean isTMExists(TmProfileInfo tmProfileInfo) throws DataAccessException;

    /**
     * To add new tm
     *
     * @param tmProfileInfo TmProfileInfo that has to be added
     * @return Returns true if newTm added else it returns false
     * @throws DataAccessException
     */
    public Boolean addNewTM(TmProfileInfo tmProfileInfo) throws DataAccessException;

    /**
     * To get TM profile terms
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter the  terms
     * @return List of terms
     * @throws DataAccessException
     */
    public Object[] getTMProfileTerms(QueryAppender queryAppender, Integer companyId) throws DataAccessException;

    /**
     * To get Total tms information by search
     *
     * @param @param queryAppender Which is used to build a query
     * @return An integer value holding the total no of terms in glossary
     * @throws DataAccessException
     */
    public Integer getTotalTMTermsBySearch(QueryAppender queryAppender) throws DataAccessException;

    /**
     * To get Total tm terms in Glossary
     *
     * @param companyId companyId An Integer to  filter the  terms
     * @return An integer value holding the total no of terms in glossary
     * @throws DataAccessException
     */
    public Integer getTotalTermsInTM(String companyIds) throws DataAccessException;

    /**
     * To get tm attributes
     *
     * @param tmProfileInfoId an Integer to get details
     * @return TmProfileInfo w.r.t tmProfileInfoId
     * @throws DataAccessException
     */
    public TmProfileInfo getTmAttributes(Integer tmProfileInfoId) throws DataAccessException;

    /**
     * To get total tm terms
     *
     * @param exportBy    String to filter the terms
     * @param selectedIds Integer Array of filter values
     * @return List of terms
     * @throws DataAccessException
     */
    public List<TmProfileInfo> getTotalTermsInTM(String exportBy, Integer[] selectedIds) throws DataAccessException;

    /**
     * To update tm details
     *
     * @param tmProfileInfoId TmProfileInfo that has to be updated
     * @throws DataAccessException
     */
    public void updateTmDetails(TmProfileInfo tmProfileInfo) throws DataAccessException;

    /**
     * To delete tms terms
     *
     * @param termIds An Integer array that needs to be deleted
     * @throws DataAccessException
     */

    public void deleteTms(final Integer[] termIds) throws DataAccessException;

    /**
     * To update gs term details
     *
     * @param gsTermInformation GlobalsightTermInfo that has to be updated
     * @throws DataAccessException
     */
    public void updateGSTermDetails(GlobalsightTermInfo gsTermInformation) throws DataAccessException;

    /**
     * To get gs term information using term id
     *
     * @param termId An Integer to get details
     * @return GlobalsightTermInfo w.r.t the term id
     * @throws DataAccessException
     */
    public GlobalsightTermInfo getGSTermInfoUsingTermId(Integer termId) throws DataAccessException;

    /**
     * To get total gs terms
     *
     * @return A integer  value holding the total no of gs terms in Glossary
     * @throws DataAccessException
     */
    public Integer getTotalGSTerms() throws DataAccessException;

    /**
     * To save tm properties
     *
     * @param tmProperties TMProperties that has to be saved
     * @throws DataAccessException
     */
    public void saveTmproperties(TMProperties tmProperties) throws DataAccessException;

    /**
     * To get tm properties by PropReference
     *
     * @param propRef String to filter the properties
     * @return List of TMProperties  w.r.t propRef
     * @throws DataAccessException
     */
    public List<TMProperties> getTMPropertiesByRef(String propRef) throws DataAccessException;

    /**
     * To get tm properties using tm profile id
     *
     * @param tmProfileId integer to filter the properties
     * @return List of TMProperties  w.r.t tmProfileId
     * @throws DataAccessException
     */
    public List<TMProperties> getTMpropertiesUsingTMProfileId(Integer termProfileId) throws DataAccessException;

    /**
     * To update gs file data
     *
     * @param fileInformation fileInformation that has to be updated
     * @throws DataAccessException
     */
    public void updateGSFileData(FileInfo fileInformation) throws DataAccessException;

    /**
     * To get gs term information using file info id
     *
     * @param tmProfileId integer to filter the terms
     * @return List of GlobalsightTermInfo obj's
     * @throws DataAccessException
     */
    public List<GlobalsightTermInfo> getGSTermInfoUsingFileInfoId(Integer FileInfoId) throws DataAccessException;

    /**
     * To get tags by gs id
     *
     * @return List of tag obj's
     * @throws DataAccessException
     */
    public List<Tag> getTagsByGSId(Integer gsId) throws DataAccessException;

    /**
     * To get attributes by tag id
     *
     * @param tagId integer to filter the attributes
     * @return List of Attributes w.r.t tagId
     * @throws DataAccessException
     */
    public List<Attributes> getAttributesByTagId(Integer tagId) throws DataAccessException;

    /**
     * To delete attributes
     *
     * @param gsIds set that needs to be deleted
     * @throws DataAccessException
     */
    public void deleteTags(Set<Integer> gsIds) throws DataAccessException;

    /**
     * To get tags using gs id
     *
     * @param gsIds Set of gsIds from which it is to be filtered
     * @return List of tag ids
     * @throws DataAccessException
     */
    public List<Integer> getTagsUsinggsIds(Set<Integer> gsIds) throws DataAccessException;

    /**
     * To delete attributes
     *
     * @param tagids list that needs to be deleted
     * @throws DataAccessException
     */
    public void deleteAttributes(List<Integer> tagIds) throws DataAccessException;

    /**
     * To get Tms in Glossary per year
     *
     * @param companyId String to be filtered
     * @return List of data holding year and number of terms per year
     * @throws DataAccessException
     */

    public List<Terms> getTmsInGlossary(String companyIds) throws DataAccessException;

    /**
     * To get quarterly tm details
     *
     * @param companyId An Integer to be filtered
     * @return List of data holding quarter-year and no of terms per quarter
     * @throws DataAccessException
     */

    public List<Terms> getQuarterlyTmDetails(Integer companyId) throws DataAccessException;

    /**
     * To get monthly glossary terms
     *
     * @param companyId An Integer to be filtered
     * @return List of data holding month and no of terms per month
     * @throws DataAccessException
     */

    public List<Terms> getMonthlyTmDetails(Integer companyId) throws DataAccessException;

    /**
     * To save import tmx file
     *
     * @param fileUpload FileUploadStatus  to be saved
     * @throws DataAccessException
     */
    public void saveImportTMXFile(FileUploadStatus fileUpload) throws DataAccessException;

    /**
     * To save import tmx file url
     *
     * @param fileId String to filter FileUploadStatus  details
     * @throws DataAccessException
     */
    public void saveImportTMXFileUrl(String filePath, String fileId);

    /**
     * To get file upload status
     *
     * @param fileId Integer to filter FileUploadStatus  details
     * @return FileUploadStatus w.r.t fileId
     * @throws DataAccessException
     */
    public FileUploadStatus getFileUploadStatus(Integer fileId);

    /**
     * To save export tmx file url
     *
     * @param fileUpload FileUploadStatus to be saved
     * @return FileUploadStatus
     * @throws DataAccessException
     */
    public FileUploadStatus saveExportTMXFileUrl(FileUploadStatus fileUpload);

    /**
     * To get FileUploadstatus details
     *
     * @param request   HttpServletRequest
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of FileUploadstatus w.r.t userid
     * @throws DataAccessException
     */
    public List<FileUploadStatus> getImportExportData(Integer userId, String colName,
                                                      String sortOrder, Integer pageNum);

    /**
     * To update file upload status
     *
     * @param fileUpload FileUploadStatus to be updated
     * @throws DataAccessException
     */
    public void updateFileUploadStatus(FileUploadStatus fileUpload);

    /**
     * To build index for tmprofile
     *
     * @return List of TmProfileInfo
     * @throws DataAccessException
     */
    public List<TmProfileInfo> buildIndexForTmprofile();

    /**
     * To get tmprofileInfo by propref
     *
     * @return List of TmProfileInfo w.r.t propReference
     * @throws DataAccessException
     */
    public List<TmProfileInfo> getTMProfileInfoByPropRef(String propRef) throws DataAccessException;

    /**
     * To get all jobs
     *
     * @return List of FileInfo obj's
     * @throws DataAccessException
     */
    public List<FileInfo> getAllJobs() throws DataAccessException;

    /**
     * To get file information by jobIds
     *
     * @param jobIds Integer Array of filter jobIds
     * @return List of FileInfo w.r.t jobIds
     */
    public List<FileInfo> getFileInfoByJobIds(String[] jobIds) throws DataAccessException;

    /**
     * To get file information by task id
     *
     * @param taskId Integer of filter task ids
     * @return FileInfo w.r.t taskId
     */
    public FileInfo getFileInfoByTaskId(Integer taskId) throws DataAccessException;

    /**
     * To get termInformation by term ids
     *
     * @param termIds Integer Array of filter termIds
     * @return List of TermInformation w.r.t termIds
     * @throws DataAccessException
     */
    public List<TermInformation> getTermInfoByTermIds(Integer[] termIds) throws DataAccessException;

    /**
     * To get user by user ids
     *
     * @param userId Integer to filter user ids
     * @return List of User w.r.t the userIds
     * @throws DataAccessException
     */
    public List<User> getUsersByuserIdsList(Integer[] userIds) throws DataAccessException;

    /**
     * To get all terms
     *
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of term ids that needs to manage
     * @throws DataAccessException
     */
    public List<Integer> getAllTerms(QueryAppender queryAppender,
                                     Integer companyId) throws DataAccessException;

    /**
     * To get all gs terms
     *
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of gs term information ids that needs to manage
     * @throws DataAccessException
     */
    public List<Integer> getTotalGSTerms(QueryAppender queryAppender,
                                         Integer companyId) throws DataAccessException;

    /**
     * To get company id  by tmId
     *
     * @param tmId Integer to filter company  Details
     * @return An Integer value holding the count of company id's
     * @throws DataAccessException
     */
    public Integer getCompanyIdBytmId(Integer tmId) throws DataAccessException;

    /**
     * To get voting details
     *
     * @param termIds Integer to be filtered
     * @return List of terms w.r.t the termIds
     * @throws DataAccessException
     */
    public List<PollTerms> getVotingDetailsByTermIds(String termIds) throws DataAccessException;

    /**
     * To get DeprecatedTermInformation
     *
     * @param termIds Integer to be filtered
     * @return List of DeprecatedTermInformation  w.r.t the termIds
     * @throws DataAccessException
     */
    public List<DeprecatedTermInformation> getDeprecatedTermInfoByTermId(Integer termId) throws DataAccessException;

    /**
     * To check whether TargetTerm exists or not  by passing attributes of TermInformation Object
     *
     * @param terminformation Object
     * @return Returns true if TargetTerm exists else it returns false
     * @throws DataAccessException
     */
    public TermInformation isTermBaseAttrbutesExists(TermInformation termInfo);

    /**
     * To get List of TmProfileInfo Objects
     *
     * @param ids List of Integers to be filtered
     * @return List of TmProfileInfo  w.r.t the tmProfileInfoIds
     * @throws DataAccessException
     */
    public List<TmProfileInfo> getTmProfileInfoByIds(List<Integer> ids);

    /**
     * To get List of CompanyTransMgmt Objects
     *
     * @param userId to be filtered
     * @return List of CompanyTransMgmt  w.r.t the userId
     * @throws DataAccessException
     */
    public List<CompanyTransMgmt> getCompanyTransMgmtUsers(Integer userId);

    /**
     * To delete import files
     *
     * @param fileIds array of integer fileIds that needs to be deleted
     */
    public void deleteImportFiles(Integer[] fileIds);

    /**
     * To delete tms terms
     *
     * @param termIds An arryList that needs to be deleted
     * @throws DataAccessException
     */

    public void deleteTms(final List<Integer> tmIds) throws DataAccessException;

    /**
     * To delete TmProperties
     *
     * @param termIds An arryList that needs to be deleted
     * @throws DataAccessException
     */

    public void deleteTmProperties(final List<Integer> tmIds) throws DataAccessException;

    /**
     * To get import  status files by fileIds
     *
     * @param fileIds Integer array  to  filter the  import files status
     * @return List of import files obj w.r.t fileIds
     */
    public List<FileUploadStatus> getImportStatusFiles(Integer[] fileIds);

    /**
     * To get suggested term lang id
     *
     * @param termId Integer to be filtered
     * @return List of languages in which user is registered
     */
    public Integer getSuggestedTermLangId(Integer termId);

    /**
     * To get user register languages
     *
     * @param userId Integer to be filtered
     * @return List of languages in which user is registered
     */
    public List<Language> getUserRegLanguages(Integer userId);


    /**
     * To get ids of terms belongs to given companyid
     *
     * @param companyId of a company
     * @return termIds List of integer
     */
    public List<Integer> getTermIdsByCompanyId(Integer companyId);

    /**
     * return GlobalsightTermInfo object using gsTermids.
     *
     * @param gsids lis of Gs term ids.
     * @return Globalsight termInformation list
     */
    public List<GlobalsightTermInfo> getGSTermsByTermIdsList(List<Integer> gsids);


    /**
     * this method returns comments given by user
     * for a term
     * @param termId
     * @return list of comments
     */
	public List<Object> getUserComments(Integer termId);
	
	/**
	 * #TNG-81 And #HPTC-40 
	 * To get all alternative translations for given term id
	 * @param termId
	 * @return
	 */
	
	public List<TermTranslation> getAlternativeTranslations(Integer termId);
	
	public Integer getTotalVotesOfUser(final Integer user_id);


	public List<Member> getAllBoardMembersByLanguage(Integer companyId, String user_lang_id);



	public List<Object> getHitoryDeata(Integer termId);


	public List<TermUpdateDetails> getTermUpdateHistoryList(List<Integer> termIdsList, String toDate, String fromDate);

}

