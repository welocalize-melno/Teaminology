package com.teaminology.hp.data;

import java.io.Serializable;

/**
 * POJO class containing the instance variables of suggested terms, respective votes and the voted user names.
 *
 * @author sarvanic
 */
public class SuggestedTermDetails implements Serializable
{

    private Integer suggestedTermId;
    private String suggestedTerm;
    private Integer noOfVotes;
    private String votersNames;
    private String isUpdated;

    public String getSuggestedTerm() {
        return suggestedTerm;
    }

    public void setSuggestedTerm(String suggestedTerm) {
        this.suggestedTerm = suggestedTerm;
    }

    public Integer getNoOfVotes() {
        return noOfVotes;
    }

    public void setNoOfVotes(Integer noOfVotes) {
        this.noOfVotes = noOfVotes;
    }

    public String getVotersNames() {
        return votersNames;
    }

    public void setVotersNames(String votersNames) {
        this.votersNames = votersNames;
    }

    public Integer getSuggestedTermId() {
        return suggestedTermId;
    }

    public void setSuggestedTermId(Integer suggestedTermId) {
        this.suggestedTermId = suggestedTermId;
    }

    public String getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(String isUpdated) {
        this.isUpdated = isUpdated;
    }

}
