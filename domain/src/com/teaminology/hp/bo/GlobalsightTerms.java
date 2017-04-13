package com.teaminology.hp.bo;

import java.util.Date;

import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class which is used to represent GlobalsighttermInfo Object
 *
 * @author sayeedm
 */
public class GlobalsightTerms implements TeaminologyObject
{
    private Integer globalsightTermInfoId;
    private TermInformation termId;
    private Integer pageId;
    private Integer workFlowId;
    private String taskId;
    private String taskName;
    private String jobId;
    private String jobName;
    private String segmentId;
    private String sourceLang;
    private String targetLang;
    private String sourceSegment;
    private String targetSegment;
    private String origin;
    private String isActive;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String fileName;
    private Integer transUnitId;
    private String translate;
    private String targetState;
    private String matchQuality;
    private Integer fileId;
    private String status;
    private Integer fileInfoId;
    private Integer gsTermId;
    private Integer invites;
    private Integer totalVotes;
    private String exportLog;


    public Integer getGlobalsightTermInfoId() {
        return globalsightTermInfoId;
    }

    public void setGlobalsightTermInfoId(Integer globalsightTermInfoId) {
        this.globalsightTermInfoId = globalsightTermInfoId;
    }

    public TermInformation getTermId() {
        return termId;
    }

    public void setTermId(TermInformation termId) {
        this.termId = termId;
    }

    /*	public Integer getPageId() {
            return pageId;
        }
        public void setPageId(Integer pageId) {
            this.pageId = pageId;
        }*/
    public Integer getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(Integer workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getSourceSegment() {
        return sourceSegment;
    }


    public void setSourceSegment(String sourceSegment) {
        this.sourceSegment = sourceSegment;
    }

    public String getTargetSegment() {
        return targetSegment;
    }

    public void setTargetSegment(String targetSegment) {
        this.targetSegment = targetSegment;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /*public Language getSourceLang() {
        return sourceLang;
    }
    public void setSourceLang(Language sourceLang) {
        this.sourceLang = sourceLang;
    }
    public Language getTargetLang() {
        return targetLang;
    }
    public void setTargetLang(Language targetLang) {
        this.targetLang = targetLang;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }*/
    public Integer getTransUnitId() {
        return transUnitId;
    }

    public void setTransUnitId(Integer transUnitId) {
        this.transUnitId = transUnitId;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getTargetState() {
        return targetState;
    }

    public void setTargetState(String targetState) {
        this.targetState = targetState;
    }

    public String getMatchQuality() {
        return matchQuality;
    }

    public void setMatchQuality(String matchQuality) {
        this.matchQuality = matchQuality;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getSourceLang() {
        return sourceLang;
    }

    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    public String getTargetLang() {
        return targetLang;
    }

    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFileInfoId() {
        return fileInfoId;
    }

    public void setFileInfoId(Integer fileInfoId) {
        this.fileInfoId = fileInfoId;
    }

    public Integer getGsTermId() {
        return gsTermId;
    }

    public void setGsTermId(Integer gsTermId) {
        this.gsTermId = gsTermId;
    }

    public Integer getInvites() {
        return invites;
    }

    public void setInvites(Integer invites) {
        this.invites = invites;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getExportLog() {
        return exportLog;
    }

    public void setExportLog(String exportLog) {
        this.exportLog = exportLog;
    }


}
