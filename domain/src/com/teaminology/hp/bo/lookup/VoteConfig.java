package com.teaminology.hp.bo.lookup;

import java.util.Date;

import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class for VoteConfig.hbm, this class refers to the vote configuration for a particular invited term, which
 * includes the voting period for an invited term.
 *
 * @author sarvanic
 */
public class VoteConfig extends AbstractLookup implements TeaminologyObject
{
    private Integer voteConfigId;
    private Integer votingPeriod;
    private Integer votesPerUser;
    private Integer votingThreshold;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;
    private String transactionType;

    public Integer getVoteConfigId() {
        return voteConfigId;
    }

    public void setVoteConfigId(Integer voteConfigId) {
        this.voteConfigId = voteConfigId;
    }

    public Integer getVotingPeriod() {
        return votingPeriod;
    }

    public void setVotingPeriod(Integer votingPeriod) {
        this.votingPeriod = votingPeriod;
    }

    public Integer getVotesPerUser() {
        return votesPerUser;
    }

    public void setVotesPerUser(Integer votesPerUser) {
        this.votesPerUser = votesPerUser;
    }

    public Integer getVotingThreshold() {
        return votingThreshold;
    }

    public void setVotingThreshold(Integer votingThreshold) {
        this.votingThreshold = votingThreshold;
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
