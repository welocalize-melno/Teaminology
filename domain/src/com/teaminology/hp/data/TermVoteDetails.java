package com.teaminology.hp.data;

public class TermVoteDetails implements TeaminologyObject {

	private Long termId;
	private String noOfVotes;
	private String transalation;	
	
	public TermVoteDetails(){
		
	}
	
	public TermVoteDetails(Long termId, String noOfVotes, String translation){
		this.termId = termId;
		this.noOfVotes = noOfVotes;
		this.transalation = translation;
	}
	
	public Long getTermId() {
		return termId;
	}

	public void setTermId(Long termId) {
		this.termId = termId;
	}

	
	public String getNoOfVotes() {
		return noOfVotes;
	}

	public void setNoOfVotes(String noOfVotes) {
		this.noOfVotes = noOfVotes;
	}

	public String getTransalation() {
		return transalation;
	}

	public void setTransalation(String transalation) {
		this.transalation = transalation;
	}
}