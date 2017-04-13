package com.teaminology.hp.bo.lookup;

import java.util.Date;

import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class for Form.hbm, this class refers to the form for a particular term.
 *
 * @author sarvanic
 */
public class Form extends AbstractLookup implements TeaminologyObject
{

    private Integer formId;
    private String formName;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createsBy) {
        this.createdBy = createsBy;
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
