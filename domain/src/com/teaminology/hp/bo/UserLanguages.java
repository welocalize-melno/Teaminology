package com.teaminology.hp.bo;

import java.util.Date;

import com.teaminology.hp.bo.lookup.Language;

/**
 * A Persistence class for UserLanguages.hbm, this class contains the languages of the user in which they are registered.
 *
 * @author sarvanic
 */
public class UserLanguages
{
    private Integer userLangId;
    private Integer userId;
    private String langId;
    private Date createDate;
    private Integer createdBy;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;
    private Language languages;

    public Integer getUserLangId() {
        return userLangId;
    }

    public void setUserLangId(Integer userLangId) {
        this.userLangId = userLangId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public Language getLanguages() {
        return languages;
    }

    public void setLanguages(Language languages) {
        this.languages = languages;
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


}
