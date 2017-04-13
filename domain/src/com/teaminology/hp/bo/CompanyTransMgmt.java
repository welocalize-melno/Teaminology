package com.teaminology.hp.bo;

/**
 * A Persistence class used to describe the companies of a user to be which he can vote.
 *
 * @author sayeedm
 */
public class CompanyTransMgmt
{
    private Integer companyTransMgmtId;
    private Integer userId;
    private Integer companyId;
    private String isExternal;
    private String isActive;

    public Integer getCompanyTransMgmtId() {
        return companyTransMgmtId;
    }

    public void setCompanyTransMgmtId(Integer companyTransMgmtId) {
        this.companyTransMgmtId = companyTransMgmtId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(String isExternal) {
        this.isExternal = isExternal;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}
