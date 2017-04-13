package com.teaminology.hp.bo.lookup;

import java.util.Date;

import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class for PartsOfSpeech.hbm, this class refers to the part of speech for a particular term.
 *
 * @author sarvanic
 */
public class PartsOfSpeech extends AbstractLookup implements TeaminologyObject
{

    private Integer partsOfSpeechId;
    private String partOfSpeech;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;

    public Integer getPartsOfSpeechId() {
        return partsOfSpeechId;
    }

    public void setPartsOfSpeechId(Integer partsOfSpeechId) {
        this.partsOfSpeechId = partsOfSpeechId;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
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


}
