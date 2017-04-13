package com.teaminology.hp.bo;

import java.util.Date;

public class FileUploadStatusImp
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer fileUploadStatusId;
    private String fileName;
    private String fileUrl;
    private String fileStatus;
    private Date startTime;
    private Date endTime;
    private Integer totalRecords;
    private Integer processedPercentage;
    private String fileType;
    private String fileLogUrl;
    private Integer createdBy;
    private String propref;


    public Integer getFileUploadStatusId() {
        return fileUploadStatusId;
    }

    public void setFileUploadStatusId(Integer fileUploadStatusId) {
        this.fileUploadStatusId = fileUploadStatusId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getProcessedPercentage() {
        return processedPercentage;
    }

    public void setProcessedPercentage(Integer processedPercentage) {
        this.processedPercentage = processedPercentage;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileLogUrl() {
        return fileLogUrl;
    }

    public void setFileLogUrl(String fileLogUrl) {
        this.fileLogUrl = fileLogUrl;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getPropref() {
        return propref;
    }

    public void setPropref(String propref) {
        this.propref = propref;
    }


}
