package com.teaminology.hp.data;

import java.io.Serializable;
import java.util.List;

public class TermInformationDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer termId;
	private String sourceTerm;
	private String targetterm;
	private String isoSourceLang;
	private String isotargetLang;
	private String dateUpdated;
	private String dateCreated;
	private String sourcePos;
	private String targetPos;
	private String form;
	private String termCategory;
	private String domain;
	private String conceptDefinition;
	private String pollExpiration;
	private String state;
	private String status;
	private String termUsage;
	private String termNotes;
	private String contextualExample;
	private List<DepricatedTermDetails> depricatedTermDetailsList;

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

	public String getTargetterm() {
		return targetterm;
	}

	public void setTargetterm(String targetterm) {
		this.targetterm = targetterm;
	}

	public String getIsoSourceLang() {
		return isoSourceLang;
	}

	public void setIsoSourceLang(String isoSourceLang) {
		this.isoSourceLang = isoSourceLang;
	}

	public String getIsotargetLang() {
		return isotargetLang;
	}

	public void setIsotargetLang(String isotargetLang) {
		this.isotargetLang = isotargetLang;
	}

	public String getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getTermCategory() {
		return termCategory;
	}

	public void setTermCategory(String termCategory) {
		this.termCategory = termCategory;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getConceptDefinition() {
		return conceptDefinition;
	}

	public void setConceptDefinition(String conceptDefinition) {
		this.conceptDefinition = conceptDefinition;
	}

	public String getPollExpiration() {
		return pollExpiration;
	}

	public void setPollExpiration(String pollExpiration) {
		this.pollExpiration = pollExpiration;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTermUsage() {
		return termUsage;
	}

	public void setTermUsage(String termUsage) {
		this.termUsage = termUsage;
	}

	public String getTermNotes() {
		return termNotes;
	}

	public void setTermNotes(String termNotes) {
		this.termNotes = termNotes;
	}

	public String getSourcePos() {
		return sourcePos;
	}

	public void setSourcePos(String sourcePos) {
		this.sourcePos = sourcePos;
	}

	public String getTargetPos() {
		return targetPos;
	}

	public void setTargetPos(String targetPos) {
		this.targetPos = targetPos;
	}

	public List<DepricatedTermDetails> getDepricatedTermDetailsList() {
		return depricatedTermDetailsList;
	}

	public void setDepricatedTermDetailsList(
			List<DepricatedTermDetails> depricatedTermDetailsList) {
		this.depricatedTermDetailsList = depricatedTermDetailsList;
	}

	public String getContextualExample() {
		return contextualExample;
	}

	public void setContextualExample(String contextualExample) {
		this.contextualExample = contextualExample;
	}
	
   
	
}
