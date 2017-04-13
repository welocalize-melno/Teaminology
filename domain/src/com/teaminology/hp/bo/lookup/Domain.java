package com.teaminology.hp.bo.lookup;

import java.util.Date;

import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class for Domain.hbm, this class refers to the domain for a particular term.
 *
 * @author sarvanic
 */
public class Domain extends AbstractLookup implements TeaminologyObject
{
    private Integer domainId;
    private String domain;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;
    private String transactionType;

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
