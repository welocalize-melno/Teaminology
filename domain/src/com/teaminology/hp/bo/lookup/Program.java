package com.teaminology.hp.bo.lookup;

import java.util.Date;

import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class for Program.hbm, this class refers to the program for a particular term.
 *
 * @author sarvanic
 */
public class Program extends AbstractLookup implements TeaminologyObject
{
    private Integer programId;
    private String programName;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
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
