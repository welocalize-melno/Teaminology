package com.teaminology.hp.bo;

import java.util.Date;

import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class used to describe properties of TM terms.
 *
 * @author sayeedm
 */
public class TMProperties implements TeaminologyObject
{
    private Integer tmPropertiesId;
    private String type;
    private String description;
    private Integer tmProfileId;
    private Integer createdBy;
    private Date createDate;
    private String isTu;
    private String propRef;

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

    public Integer getTmProfileId() {
        return tmProfileId;
    }

    public void setTmProfileId(Integer tmProfileId) {
        this.tmProfileId = tmProfileId;
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


}
