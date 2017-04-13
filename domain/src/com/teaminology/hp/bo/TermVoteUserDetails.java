package com.teaminology.hp.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * A Persistence class for TermVoteUserDetails.hbm, this class contains the vote of the user on a particular suggested term,
 * for which the user has been invited to vote.
 *
 * @author sarvanic
 */
public class TermVoteUserDetails implements Serializable
{

  
	private Integer termVoteUsrDtlsId;
    private Integer termId;
    private Integer userId;
    private Integer termTranslationId;
    private Integer termVoteId;
    private Date votingDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;
    private String voteInviteStatus;
    private String comments;

    public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getTermVoteUsrDtlsId() {
        return termVoteUsrDtlsId;
    }

    public void setTermVoteUsrDtlsId(Integer termVoteUsrDtlsId) {
        this.termVoteUsrDtlsId = termVoteUsrDtlsId;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTermTranslationId() {
        return termTranslationId;
    }

    public void setTermTranslationId(Integer termTranslationId) {
        this.termTranslationId = termTranslationId;
    }

    public Integer getTermVoteId() {
        return termVoteId;
    }

    public void setTermVoteId(Integer termVoteId) {
        this.termVoteId = termVoteId;
    }

    public Date getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(Date votingDate) {
        this.votingDate = votingDate;
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

    public String getVoteInviteStatus() {
        return voteInviteStatus;
    }

    public void setVoteInviteStatus(String voteInviteStatus) {
        this.voteInviteStatus = voteInviteStatus;
    }


}
