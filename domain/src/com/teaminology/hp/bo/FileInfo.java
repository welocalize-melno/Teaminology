package com.teaminology.hp.bo;

import java.util.Date;

import com.teaminology.hp.bo.lookup.Language;

/**
 * A Persistence class which is used to describe the information like jobid, taskid etc of a xliff file.
 *
 * @author sayeedm
 */
public class FileInfo
{
    private Integer fileInfoId;
    private Integer fileId;
    private String fileName;
    private String status;
    private String isActive;
    private Language sourceLang;
    private Language targetLang;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String activityType;
    private String userName;
    private String acceptTime;
    private String encodingType;
    private String documentForm;
    private String placeHolder;
    private Integer exactMatch;
    private Integer fuzzyMatch;
    private Integer noMatch;
    private Integer inContextMatch;
    private String editAll;
    private Integer gsTmProfileId;
    private String gsTmProfileName;
    private String gsTermbase;
    private String xmlVersion;
    private String xliffVersion;
    private String phanesName;
    private String processName;
    private String workFlowId;
    private String taskId;
    private String jobId;
    private String jobName;
    private String taksName;
    private String logFileUrl;
    private String exportLog;

    public Integer getFileInfoId() {
        return fileInfoId;
    }

    public void setFileInfoId(Integer fileInfoId) {
        this.fileInfoId = fileInfoId;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Language getSourceLang() {
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

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public String getDocumentForm() {
        return documentForm;
    }

    public void setDocumentForm(String documentForm) {
        this.documentForm = documentForm;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public Integer getExactMatch() {
        return exactMatch;
    }

    public void setExactMatch(Integer exactMatch) {
        this.exactMatch = exactMatch;
    }

    public Integer getFuzzyMatch() {
        return fuzzyMatch;
    }

    public void setFuzzyMatch(Integer fuzzyMatch) {
        this.fuzzyMatch = fuzzyMatch;
    }

    public Integer getNoMatch() {
        return noMatch;
    }

    public void setNoMatch(Integer noMatch) {
        this.noMatch = noMatch;
    }

    public Integer getInContextMatch() {
        return inContextMatch;
    }

    public void setInContextMatch(Integer inContextMatch) {
        this.inContextMatch = inContextMatch;
    }

    public String getEditAll() {
        return editAll;
    }

    public void setEditAll(String editAll) {
        this.editAll = editAll;
    }

    public Integer getGsTmProfileId() {
        return gsTmProfileId;
    }

    public void setGsTmProfileId(Integer gsTmProfileId) {
        this.gsTmProfileId = gsTmProfileId;
    }

    public String getGsTmProfileName() {
        return gsTmProfileName;
    }

    public void setGsTmProfileName(String gsTmProfileName) {
        this.gsTmProfileName = gsTmProfileName;
    }

    public String getGsTermbase() {
        return gsTermbase;
    }

    public void setGsTermbase(String gsTermbase) {
        this.gsTermbase = gsTermbase;
    }

    public String getXmlVersion() {
        return xmlVersion;
    }

    public void setXmlVersion(String xmlVersion) {
        this.xmlVersion = xmlVersion;
    }

    public String getXliffVersion() {
        return xliffVersion;
    }

    public void setXliffVersion(String xliffVersion) {
        this.xliffVersion = xliffVersion;
    }

    public String getPhanesName() {
        return phanesName;
    }

    public void setPhanesName(String phanesName) {
        this.phanesName = phanesName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(String workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTaksName() {
        return taksName;
    }

    public void setTaksName(String taksName) {
        this.taksName = taksName;
    }

    public String getLogFileUrl() {
        return logFileUrl;
    }

    public void setLogFileUrl(String logFileUrl) {
        this.logFileUrl = logFileUrl;
    }

    public String getExportLog() {
        return exportLog;
    }

    public void setExportLog(String exportLog) {
        this.exportLog = exportLog;
    }

}
