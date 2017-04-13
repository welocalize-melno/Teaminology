package com.teaminology.hp.service;

import java.io.File;
import java.util.List;

import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.data.CSVImportStatus;
import com.teaminology.hp.data.GSJobObject;
import com.teaminology.hp.data.QueryAppender;
import com.teaminology.hp.data.TeaminologyObject;
import com.teaminology.hp.gs.Job;
import com.teaminology.hp.gs.Jobs;
import com.teaminology.hp.gs.LocalePairInformation;
import com.teaminology.hp.gs.TMProfileInformation;
import com.teaminology.hp.gs.Tasks;
import com.teaminology.hp.gs.TermbaseInformation;

/**
 * Contains method prototypes for global sight import and export
 *
 * @author sayeedm
 */
public interface IGSService
{
    /**
     * get accesstoken if login details are valid
     *
     * @return accesstoken
     */

    public String getAccessToken(Company company);

    /**
     * to get file profile information
     *
     * @param accessToken accessToken
     * @return file profile Information
     */
    public String getFileProfileInfo(String accessToken);

    /**
     * To get all jobs
     *
     * @param accessToken accessToken
     * @return all jobids
     */
    public String[] fetchAllJobIds(String accessToken);

    /**
     * To get all job attributes
     *
     * @param accessToken accessToken
     * @param jobIds      Integer List
     * @return all job attributes
     */
    public String getJobAttributes(String accessToken, List<Integer> jobIds);

    /**
     * To get all jobs
     *
     * @param accessToken accessToken
     * @return job obj
     */
    public Jobs getJobs(String accessToken);

    /**
     * To get  gs terms
     *
     * @param accessToken accessToken
     * @param jobs        Jobs
     * @return List of terms
     */
    public List<GSJobObject> getGSTerms(String accessToken, Jobs jobs);

    /**
     * To get  gs terms
     *
     * @param accessToken accessToken
     * @param jobs        List of Job
     * @return List of GSJobObject obj's
     */
    public List<GSJobObject> getGSTerms(String accessToken, List<Job> jobs);

    /**
     * To get  gs In progress terms
     *
     * @param accessToken accessToken
     * @param jobs        list of jobs
     * @return List of terms
     */
    public List<GSJobObject> getGSInProgressTerms(String accessToken, List<Job> jobs);

    /**
     * To filter the job ids by state
     *
     * @param accessToken accessToken
     * @param jobIds      String Array of filter the job ids
     * @return List of jobs
     */
    public String[] filterJobIdsByState(String accessToken, String[] jobIds);

    /**
     * To get jobs by state
     *
     * @param jobs        Jobs
     * @return List of jobs
     */
    public List<Job> getJobsByState(Jobs jobs);

    /**
     * To search term entries
     *
     * @param accessToken   accessToken
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     */
    public void searchTermEntries(String accessToken, QueryAppender queryAppender);

    /**
     * To get all tm profiles
     *
     * @param accessToken accessToken
     * @return TMProfileInformation obj
     */
    public TMProfileInformation getAllTMProfiles(String accessToken);

    /**
     * To get all term bases
     *
     * @param accessToken accessToken
     * @return TermbaseInformation obj
     */
    public TermbaseInformation getAllTermbases(String accessToken);

    /**
     * To search term base
     *
     * @param accessToken   accessToken
     *                      param  termbaseInformation TermbaseInformation
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of terms
     */
    public List<GSJobObject> searchTermBase(String accessToken, TermbaseInformation termbaseInformation, QueryAppender queryAppender);

    /**
     * To search term information
     *
     * @param accessToken   accessToken
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of terms
     * @parm company Company to filter the terms
     */

    public List<GSJobObject> searchTermBaseInfo(String accessToken, QueryAppender queryAppender);

    /**
     * To getall locale pairs
     *
     * @param accessToken
     * @return LocalePairInformation  w.r.t company
     */
    public LocalePairInformation getAllLocalePairs(String accessToken);

    /**
     * To search tm
     *
     * @param accessToken          accessToken
     * @param queryAppender        QueryAppender holding column name to be sorted and the filter criteria
     * @return List terms
     */
    public List<GSJobObject> searchTM(String accessToken, TMProfileInformation tMProfileInformation, QueryAppender queryAppender);

    /**
     * To  import Xliff data
     *
     * @param uploadedFile File to be read
     * @param companyIds
     * @return CSVImportStatus object
     */
    public <C extends TeaminologyObject> CSVImportStatus getImportXliffData(File uploadedFile, String companyIds);

    /**
     * To get terms by using page id
     *
     * @param pageId An Integer to get the details
     * @return List of terms w.r.t pageId
     */
    public List<GlobalsightTermInfo> getTermsByPageId(Integer pageId);

    /**
     * To get file info
     *
     * @param pageId An Integer to get the fileInfo details
     * @return FileInfo w.r.t pageId
     */

    public FileInfo getFileInfo(Integer pageId);

    /**
     * To update file status
     *
     * @param fileInfo FileInfo that has to be updated
     */
    public void updateFileStatus(FileInfo fileInfo);

    /**
     * Used to get the Job Details
     *
     * @param accessToken
     * @param pageNum     PathVariable Integer to limit the data
     * @return List of Jobs
     *         *
     */
    public List<Jobs> getGlobalSightJobList(String accessToken, int pageNum);

    /**
     * used to get Taks Details for a particular Job
     *
     * @param accessToken
     * @param jobId       Sting to filter the tasks
     * @return List of Taks
     *         *
     */
    public List<Tasks> getTasksInJob(String accessToken, String jobId);

    /**
     * To get job count
     *
     * @param accessToken accessToken
     * @return an integer value holding the count jobs
     */
    public Integer getJobsCount(String accessToken);

    /**
     * To save or update the gs credintials
     *
     * @param company Company to be saved
     * @return Returns "success" if it is registered else it returns "failure"
     */
    public String saveOrUpdateGSCredintials(Company company);

    /**
     * To get GS credintials
     *
     * @param companyId Integer of filter companyIds
     * @return company obj
     */
    public Company getGSCredintails(Integer companyId);

    /**
     * To get file information by task id
     *
     * @param taskId Integer of filter task ids
     * @return FileInfo w.r.t taskId
     */
    public FileInfo getFileInfoByTaskId(Integer taskId);

    /**
     * return GlobalsightTermInfo object using gsTermids.
     *
     * @param gsids lis of Gs term ids.
     * @return Globalsight termInformation list
     */
    public List<GlobalsightTermInfo> getGSTermsByTermIdsList(List<Integer> gsids);

    /**
     * To get jobs by state
     *
     * @param accessToken
     * @param pageNum     PathVariable Integer to limit the data
     * @return List of Jobs
     */
    public List<Jobs> getJobsByState(String accessToken, Integer pageNum);

    /**
     * To get job count by state
     *
     * @return Integer holding  the total job count
     */
    public Integer getJobsCountByState(String accessToken);

}
