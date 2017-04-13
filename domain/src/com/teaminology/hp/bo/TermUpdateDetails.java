package com.teaminology.hp.bo;

import java.io.Serializable;
import java.util.Date;

public class TermUpdateDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private Integer termUpdateId;
	 private Integer termId;
	 private String changedSourceTerm;
	 private String changedTargetTerm;
	 private Integer userId;
	 private Date createDate;
	 private String isActive;
	 private Integer updatedBy;
	 
	 private String termsPOS;
	 private String conceptDefinition;
	 private String termForm;
	 private String termStatus;
	 private String termCategory;
	 private String termDomain;
	 private String termNotes;
	 private String targetTermPOS;
	 private String termUsage;
	 
	 
	public Integer getTermUpdateId() {
		return termUpdateId;
	}
	public void setTermUpdateId(Integer termUpdateId) {
		this.termUpdateId = termUpdateId;
	}
	public Integer getTermId() {
		return termId;
	}
	public void setTermId(Integer termId) {
		this.termId = termId;
	}
	public String getChangedSourceTerm() {
		return changedSourceTerm;
	}
	public void setChangedSourceTerm(String changedSourceTerm) {
		this.changedSourceTerm = changedSourceTerm;
	}
	public String getChangedTargetTerm() {
		return changedTargetTerm;
	}
	public void setChangedTargetTerm(String changedTargetTerm) {
		this.changedTargetTerm = changedTargetTerm;
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
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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
