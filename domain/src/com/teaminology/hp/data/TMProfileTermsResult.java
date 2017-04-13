package com.teaminology.hp.data;

import java.util.List;

import com.teaminology.hp.bo.GlobalsightTerms;

/**
 * Java class to show the term base ,tm and GS term results.
 *
 * @author sayeedm
 */
public class TMProfileTermsResult
{

    private List<TMProfileTerms> tmProfieTermsList;
    private List<PollTerms> pollTermsList;
    private List<GlobalsightTerms> globalsightTermsList;
    private Integer totalResults;

    public List<TMProfileTerms> getTmProfieTermsList() {
        return tmProfieTermsList;
    }

    public void setTmProfieTermsList(List<TMProfileTerms> tmProfieTermsList) {
        this.tmProfieTermsList = tmProfieTermsList;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public List<PollTerms> getPollTermsList() {
        return pollTermsList;
    }

    public void setPollTermsList(List<PollTerms> pollTermsList) {
        this.pollTermsList = pollTermsList;
    }

    public List<GlobalsightTerms> getGlobalsightTermsList() {
        return globalsightTermsList;
    }

    public void setGlobalsightTermsList(List<GlobalsightTerms> globalsightTermsList) {
        this.globalsightTermsList = globalsightTermsList;
    }


}
