package com.teaminology.hp.bo;

import java.util.Date;


public class TempTMProperties
{
    private Integer tmPropertiesId;
    private String type;
    private String description;
    //private Integer tmProfileId;
    private Integer createdBy;
    private Date createDate;
    private String isTu;
    private String propRef;
    private TempTmProfileBo tmProfileId;
    private String isTuv;
    private String isTuvSource;
    private String isTuvTarget;


    public Integer getTmPropertiesId() {
        return tmPropertiesId;
    }

    public void setTmPropertiesId(Integer tmPropertiesId) {
        this.tmPropertiesId = tmPropertiesId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public Integer getTmProfileId() {
        return tmProfileId;
    }
    public void setTmProfileId(Integer tmProfileId) {
        this.tmProfileId = tmProfileId;
    }*/
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

    public String getIsTu() {
        return isTu;
    }

    public void setIsTu(String isTu) {
        this.isTu = isTu;
    }

    public String getPropRef() {
        return propRef;
    }

    public void setPropRef(String propRef) {
        this.propRef = propRef;
    }

    /*public TmProfileBo getTmProfileId() {
        return tmProfileId;
    }
    public void setTmProfileId(TmProfileBo tmInformation) {
        this.tmProfileId = tmInformation;
    }*/
    public String getIsTuv() {
        return isTuv;
    }

    public void setIsTuv(String isTuv) {
        this.isTuv = isTuv;
    }

    public String getIsTuvSource() {
        return isTuvSource;
    }

    public void setIsTuvSource(String isTuvSource) {
        this.isTuvSource = isTuvSource;
    }

    public String getIsTuvTarget() {
        return isTuvTarget;
    }

    public void setIsTuvTarget(String isTuvTarget) {
        this.isTuvTarget = isTuvTarget;
    }

    public TempTmProfileBo getTmProfileId() {
        return tmProfileId;
    }

    public void setTmProfileId(TempTmProfileBo tmProfileId) {
        this.tmProfileId = tmProfileId;
    }


}
