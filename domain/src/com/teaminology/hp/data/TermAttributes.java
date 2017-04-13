package com.teaminology.hp.data;

import java.util.List;

import com.teaminology.hp.bo.DeprecatedTermInformation;
import com.teaminology.hp.bo.TermInformation;

/**
 * Contains the term information and the suggested term details of the respective term.
 *
 * @author sarvanic
 */
public class TermAttributes implements TeaminologyObject
{

    private TermInformation termInfo;
    private List<SuggestedTermDetails> suggestedTermDetails;
    private List<DeprecatedTermInformation> deprecatedTermInfo;
    private String isUserVoted;
    private String votedTerm;
 
	public TermInformation getTermInfo() {
        return termInfo;
    }

    public void setTermInfo(TermInformation termInfo) {
        this.termInfo = termInfo;
    }

    public List<SuggestedTermDetails> getSuggestedTermDetails() {
        return suggestedTermDetails;
    }

    public void setSuggestedTermDetails(
            List<SuggestedTermDetails> suggestedTermDetails) {
        this.suggestedTermDetails = suggestedTermDetails;
    }

    public String getIsUserVoted() {
        return isUserVoted;
    }

    public void setIsUserVoted(String isUserVoted) {
        this.isUserVoted = isUserVoted;
    }

    public String getVotedTerm() {
        return votedTerm;
    }

    public void setVotedTerm(String votedTerm) {
        this.votedTerm = votedTerm;
    }

    public List<DeprecatedTermInformation> getDeprecatedTermInfo() {
        return deprecatedTermInfo;
    }

    public void setDeprecatedTermInfo(
            List<DeprecatedTermInformation> deprecatedTermInfo) {
        this.deprecatedTermInfo = deprecatedTermInfo;
    }


}
