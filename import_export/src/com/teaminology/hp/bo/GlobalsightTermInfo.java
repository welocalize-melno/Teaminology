package com.teaminology.hp.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


public class GlobalsightTermInfo implements Serializable
{
    private Integer globalsightTermInfoId;
    private TermInformation termInformationId;
    private String segmentId;
    private FileInfo fileInfo;
    private String sourceSegment;
    private String targetSegment;
    private String origin;
    private String isActive;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private Integer transUnitId;
    private String translate;
    private String targetState;
    private String matchQuality;
    private Set<Tag> tag;
    private String targetContent;
    private String sourceContent;
    private Company company;


    public Integer getGlobalsightTermInfoId() {
        return globalsightTermInfoId;
    }

    public void setGlobalsightTermInfoId(Integer globalsightTermInfoId) {
        this.globalsightTermInfoId = globalsightTermInfoId;
    }


    public String getSegmentId() {
        return segmentId;
    }

    public TermInformation getTermInformationId() {
        return termInformationId;
    }

    public void setTermInformationId(TermInformation termInformationId) {
        this.termInformationId = termInformationId;
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

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public Set<Tag> getTag() {
        return tag;
    }

    public void setTag(Set<Tag> tag) {
        this.tag = tag;
    }

    public String getTargetContent() {
        return targetContent;
    }

    public void setTargetContent(String targetContent) {
        this.targetContent = targetContent;
    }

    public String getSourceContent() {
        return sourceContent;
    }

    public void setSourceContent(String sourceContent) {
        this.sourceContent = sourceContent;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

}
