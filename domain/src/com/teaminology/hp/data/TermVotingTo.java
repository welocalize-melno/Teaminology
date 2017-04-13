package com.teaminology.hp.data;

import java.util.ArrayList;
import java.util.List;

public class TermVotingTo implements TeaminologyObject {

	private Long termId;
	private TermVoteDetails topSuggestion;
	private List<TermVoteDetails> alternativeSuggestion;
	private String source;
	private String noOfVotes;
	private String Language;
	
	public String getLanguage() {
		return Language;
	}
	public void setLanguage(String language) {
		Language = language;
	}
	public String getNoOfVotes() {
		return noOfVotes;
	}
	public void setNoOfVotes(String noOfVotes) {
		this.noOfVotes = noOfVotes;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Long getTermId() {
		return termId;
	}
	public void setTermId(Long termId) {
		this.termId = termId;
	}
	public TermVoteDetails getTopSuggestion() {
		return topSuggestion;
	}
	public void setTopSuggestion(TermVoteDetails topSuggestion) {
		this.topSuggestion = topSuggestion;
	}
	public List<TermVoteDetails> getAlternativeSuggestion() {
		return alternativeSuggestion;
	}
	public void setAlternativeSuggestion(List<TermVoteDetails> alternativeSuggestion) {
		this.alternativeSuggestion = alternativeSuggestion;
	}
	
	public void addAlternateSuggestion(TermVoteDetails termVoteDtl){
		if(termVoteDtl == null){
			return;
		}
		
		if(this.alternativeSuggestion == null){
			this.alternativeSuggestion = new ArrayList<TermVoteDetails>();
		}
		
		this.alternativeSuggestion.add(termVoteDtl);
	}
	
}
