package com.teaminology.hp.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * A Persistence class for EmailTemplate.hbm, this class contains the email related data which includes
 * mail subject and the respective mail message identified by the unique id.
 *
 * @author sarvanic
 */
public class EmailTemplates implements Serializable
{

    private Integer emailTemplateId;
    private String emailSubject;
    private String emailMessageContent;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;
    private String transactionType;

    public Integer getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(Integer emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailMessageContent() {
        return emailMessageContent;
    }

    public void setEmailMessageContent(String emailMessageContent) {
        this.emailMessageContent = emailMessageContent;
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }


}
