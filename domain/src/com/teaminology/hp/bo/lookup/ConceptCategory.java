package com.teaminology.hp.bo.lookup;

import java.util.Date;

import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class for ConceptCategory.hbm, this class refers to the concept category for a particular term.
 *
 * @author sarvanic
 */
public class ConceptCategory extends AbstractLookup implements TeaminologyObject
{
    private Integer conceptCatId;
    private String conceptCategory;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;

    public Integer getConceptCatId() {
        return conceptCatId;
    }

    public void setConceptCatId(Integer conceptCatId) {
        this.conceptCatId = conceptCatId;
    }

    public String getConceptCategory() {
        return conceptCategory;
    }

    public void setConceptCategory(String conceptCategory) {
        this.conceptCategory = conceptCategory;
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
