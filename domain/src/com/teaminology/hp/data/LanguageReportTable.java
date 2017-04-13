package com.teaminology.hp.data;

import java.math.BigDecimal;


/**
 * POJO class containing the instance variables of  language report table.
 *
 * @author sarvanic
 */
public class LanguageReportTable implements TeaminologyObject
{

    private String languageLabel;
    private Integer members;
    private BigDecimal accuracy;
    private Integer totalTerms;
    private Integer debatedTerms;
    private Integer activePolls;
    private Integer monthlyAvg;
    private Integer totalVotes;

    public String getLanguageLabel() {
        return languageLabel;
    }

    public void setLanguageLabel(String languageLabel) {
        this.languageLabel = languageLabel;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public BigDecimal getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(BigDecimal accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getTotalTerms() {
        return totalTerms;
    }

    public void setTotalTerms(Integer totalTerms) {
        this.totalTerms = totalTerms;
    }

    public Integer getDebatedTerms() {
        return debatedTerms;
    }

    public void setDebatedTerms(Integer debatedTerms) {
        this.debatedTerms = debatedTerms;
    }

    public Integer getActivePolls() {
        return activePolls;
    }

    public void setActivePolls(Integer activePolls) {
        this.activePolls = activePolls;
    }

    public Integer getMonthlyAvg() {
        return monthlyAvg;
    }

    public void setMonthlyAvg(Integer monthlyAvg) {
        this.monthlyAvg = monthlyAvg;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

}
