package com.teaminology.hp.data;

import java.util.Date;

/**
 * POJO class containing the customised instance variables of GS Credintials data.
 */
public class GSCredintials
{
    private Integer gsCredintialsId;
    private Integer companyId;
    private String url;
    private String userName;
    private String password;
    private Date createTime;
    private Integer createdBy;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;

    public Integer getGsCredintialsId() {
        return gsCredintialsId;
    }

    public void setGsCredintialsId(Integer gsCredintialsId) {
        this.gsCredintialsId = gsCredintialsId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
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
