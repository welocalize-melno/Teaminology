package com.teaminology.hp.data;

import java.io.Serializable;

public class HistoryDetailsData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 	private Integer termId;
	    private String userName;
	    private String historySourceTerm;
	    private String historyDate;
	    private String changedTargetTerm;
	    
	     private String termsPOS;
		 private String conceptDefinition;
		 private String termForm;
		 private String termStatus;
		 private String termCategory;
		 private String termDomain;
		 private String termNotes;
		 private String targetTermPOS;
		 private String termUsage;
	    
	    
		public Integer getTermId() {
			return termId;
		}
		public void setTermId(Integer termId) {
			this.termId = termId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getHistorySourceTerm() {
			return historySourceTerm;
		}
		public void setHistorySourceTerm(String historySourceTerm) {
			this.historySourceTerm = historySourceTerm;
		}
		public String getHistoryDate() {
			return historyDate;
		}
		public void setHistoryDate(String historyDate) {
			this.historyDate = historyDate;
		}
		public String getChangedTargetTerm() {
			return changedTargetTerm;
		}
		public void setChangedTargetTerm(String changedTargetTerm) {
			this.changedTargetTerm = changedTargetTerm;
		}
		public String getTermsPOS() {
			return termsPOS;
		}
		public void setTermsPOS(String termsPOS) {
			this.termsPOS = termsPOS;
		}
		public String getConceptDefinition() {
			return conceptDefinition;
		}
		public void setConceptDefinition(String conceptDefinition) {
			this.conceptDefinition = conceptDefinition;
		}
		public String getTermForm() {
			return termForm;
		}
		public void setTermForm(String termForm) {
			this.termForm = termForm;
		}
		public String getTermStatus() {
			return termStatus;
		}
		public void setTermStatus(String termStatus) {
			this.termStatus = termStatus;
		}
		public String getTermCategory() {
			return termCategory;
		}
		public void setTermCategory(String termCategory) {
			this.termCategory = termCategory;
		}
		public String getTermDomain() {
			return termDomain;
		}
		public void setTermDomain(String termDomain) {
			this.termDomain = termDomain;
		}
		public String getTermNotes() {
			return termNotes;
		}
		public void setTermNotes(String termNotes) {
			this.termNotes = termNotes;
		}
		public String getTargetTermPOS() {
			return targetTermPOS;
		}
		public void setTargetTermPOS(String targetTermPOS) {
			this.targetTermPOS = targetTermPOS;
		}
		public String getTermUsage() {
			return termUsage;
		}
		public void setTermUsage(String termUsage) {
			this.termUsage = termUsage;
		}
	    
}
