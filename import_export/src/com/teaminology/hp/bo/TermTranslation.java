package com.teaminology.hp.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * A Persistence class for TermTranslation.hbm, this class contains multiple suggested terms and their related votes
 * for a term that is being debated.
 *
 * @author sarvanic
 */
public class TermTranslation implements Serializable
{

    private Integer termTranslationId;
    private Integer termId;
    private String suggestedTerm;
    private Integer suggestedTermLangId;
    private Integer vote;
    private Integer userId;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String comment;
    private String isActive;

    public Integer getTermTranslationId() {
        return termTranslationId;
    }

    public void setTermTranslationId(Integer termTranslationId) {
        this.termTranslationId = termTranslationId;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getSuggestedTerm() {
        return suggestedTerm;
    }

    public void setSuggestedTerm(String suggestedTerm) {
        this.suggestedTerm = suggestedTerm;
    }

    public Integer getSuggestedTermLangId() {
        return suggestedTermLangId;
    }

    public void setSuggestedTermLangId(Integer suggestedTermLangId) {
        this.suggestedTermLangId = suggestedTermLangId;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}
