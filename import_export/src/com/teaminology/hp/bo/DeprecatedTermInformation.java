package com.teaminology.hp.bo;

import java.util.Date;

/**
 * A Persistence class for DeperecatedTermInformation.hbm, this class contains deprecated Source ,deprecated target and deprecated Lang Id.
 * for a term that is being debated.
 *
 * @author sushma
 */
public class DeprecatedTermInformation 
{

    /**
     *
     */
    private static final long serialVersionUID = 6648056001778287018L;
    private Integer deprecatedTermInfoId;
    private TermInformation termInfo;

    private String deprecatedSource;
    private String deprecatedTarget;
    private Language deprecatedLangId;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;

    public Integer getDeprecatedTermInfoId() {
        return deprecatedTermInfoId;
    }

    public void setDeprecatedTermInfoId(Integer deprecatedTermInfoId) {
        this.deprecatedTermInfoId = deprecatedTermInfoId;
    }

    public String getDeprecatedSource() {
        return deprecatedSource;
    }

    public void setDeprecatedSource(String deprecatedSource) {
        this.deprecatedSource = deprecatedSource;
    }

    public String getDeprecatedTarget() {
        return deprecatedTarget;
    }

    public void setDeprecatedTarget(String deprecatedTarget) {
        this.deprecatedTarget = deprecatedTarget;
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

    public Language getDeprecatedLangId() {
        return deprecatedLangId;
    }

    public void setDeprecatedLangId(Language deprecatedLangId) {
        this.deprecatedLangId = deprecatedLangId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public TermInformation getTermInfo() {
        return termInfo;
    }

    public void setTermInfo(TermInformation termInfo) {
        this.termInfo = termInfo;
    }

}
