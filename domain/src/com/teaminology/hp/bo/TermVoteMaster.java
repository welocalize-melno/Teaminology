package com.teaminology.hp.bo;

import java.util.Date;

/**
 * A Persistence class for TermVoteMaster.hbm, this class contains the terms that are being debated, which will expire with in
 * the duration specified in the VoteCofig(can also be edited in front end), identified by unique id.
 *
 * @author sarvanic
 */
public class TermVoteMaster
{
    private Integer termVoteId;
    private Integer termId;
    private Integer invitedBy;
    private Date invitedDate;
    private Date votingExpiredDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;

    public Integer getTermVoteId() {
        return termVoteId;
    }

    public void setTermVoteId(Integer termVoteId) {
        this.termVoteId = termVoteId;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public Date getInvitedDate() {
        return invitedDate;
    }

    public void setInvitedDate(Date invitedDate) {
        this.invitedDate = invitedDate;
    }

    public Date getVotingExpiredDate() {
        return votingExpiredDate;
    }

    public void setVotingExpiredDate(Date votingExpiredDate) {
        this.votingExpiredDate = votingExpiredDate;
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

    public Integer getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(Integer invitedBy) {
        this.invitedBy = invitedBy;
    }


}
