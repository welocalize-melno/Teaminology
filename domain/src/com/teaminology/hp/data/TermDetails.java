package com.teaminology.hp.data;

import java.io.Serializable;

/**
 * Contains the instance variables of limited term information.
 *
 * @author sarvanic
 */
public class TermDetails implements Serializable
{

    private Integer termId;
    private Integer termPOSId;
    private Integer termFormId;
    private Integer termProgramId;
    private String termNotes;
    private String conceptDefinition;
    private String termUsage;
    private Integer termCatagoryId;
    private String topSuggestion;
    private Integer termDomainId;
    private String sourceTerm;
    private Integer targetPOSId;
    private Integer termStatusId;

	public Integer getTermStatusId() {
		return termStatusId;
	}

	public void setTermStatusId(Integer termStatusId) {
		this.termStatusId = termStatusId;
	}

    public Integer getTargetPOSId() {
		return targetPOSId;
	}

	public void setTargetPOSId(Integer targetPOSId) {
		this.targetPOSId = targetPOSId;
	}
	
	public String getSourceTerm() {
        return sourceTerm;
    }

    public void setSourceTerm(String sourceTerm) {
        this.sourceTerm = sourceTerm;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public Integer getTermPOSId() {
        return termPOSId;
    }

    public void setTermPOSId(Integer termPOSId) {
        this.termPOSId = termPOSId;
    }

    public Integer getTermFormId() {
        return termFormId;
    }

    public void setTermFormId(Integer termFormId) {
        this.termFormId = termFormId;
    }

    public Integer getTermProgramId() {
        return termProgramId;
    }

    public void setTermProgramId(Integer termProgramId) {
        this.termProgramId = termProgramId;
    }

    public String getTermNotes() {
        return termNotes;
    }

    public void setTermNotes(String termNotes) {
        this.termNotes = termNotes;
    }

    public String getTermUsage() {
        return termUsage;
    }

    public void setTermUsage(String termUsage) {
        this.termUsage = termUsage;
    }

    public String getConceptDefinition() {
        return conceptDefinition;
    }

    public void setConceptDefinition(String conceptDefinition) {
        this.conceptDefinition = conceptDefinition;
    }

    public Integer getTermCatagoryId() {
        return termCatagoryId;
    }

    public void setTermCatagoryId(Integer termCatagoryId) {
        this.termCatagoryId = termCatagoryId;
    }

    public String getTopSuggestion() {
        return topSuggestion;
    }

    public void setTopSuggestion(String topSuggestion) {
        this.topSuggestion = topSuggestion;
    }

    public Integer getTermDomainId() {
        return termDomainId;
    }

    public void setTermDomainId(Integer termDomainId) {
        this.termDomainId = termDomainId;
    }


}
