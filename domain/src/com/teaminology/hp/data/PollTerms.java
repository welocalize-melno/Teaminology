package com.teaminology.hp.data;


/**
 * POJO class containing the customised instance variables of Term Information.
 *
 * @author sarvanic
 */
public class PollTerms
{

    private Integer termId;
    private String sourceTerm;
    private String suggestedTerm;
    private String language;
    private String pollExpirationDt;
    //	private Integer votesPerCandidate;
    private Integer votesPerTerm;
    private String category;
    private String partOfSpeech;
    private String status;
    private Integer invites;
    private String votingDate;
    private Integer deprecatedCount;
    private String domain;
    private String company;
    private String termReplaced;
    


    public Integer getDeprecatedCount() {
        return deprecatedCount;
    }

    public void setDeprecatedCount(Integer deprecatedCount) {
        this.deprecatedCount = deprecatedCount;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getSourceTerm() {
        return sourceTerm;
    }

    public void setSourceTerm(String sourceTerm) {
        this.sourceTerm = sourceTerm;
    }

    public String getSuggestedTerm() {
        return suggestedTerm;
    }

    public void setSuggestedTerm(String suggestedTerm) {
        this.suggestedTerm = suggestedTerm;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPollExpirationDt() {
        return pollExpirationDt;
    }

    public void setPollExpirationDt(String pollExpirationDt) {
        this.pollExpirationDt = pollExpirationDt;
    }

    //	public Integer getVotesPerCandidate() {
//		return votesPerCandidate;
//	}
//	public void setVotesPerCandidate(Integer votesPerCandidate) {
//		this.votesPerCandidate = votesPerCandidate;
//	}
    public Integer getVotesPerTerm() {
        return votesPerTerm;
    }

    public void setVotesPerTerm(Integer votesPerTerm) {
        this.votesPerTerm = votesPerTerm;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getInvites() {
        return invites;
    }

    public void setInvites(Integer invites) {
        this.invites = invites;
    }

    public String getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(String votingDate) {
        this.votingDate = votingDate;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTermReplaced() {
		return termReplaced;
	}

	public void setTermReplaced(String termReplaced) {
		this.termReplaced = termReplaced;
	}

	public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}
