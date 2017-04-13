package com.teaminology.hp.data;

/**
 * A POJO class containing the instance variables of Term Request change data.
 */
public class TermRequestChange
{
    private String sourceTerm;
    private String targetTerm;
    private String newSuggestedTerm;
    private String notes;

    /**
     * To get sourceTerm
     *
     * @return sourceTerm
     */
    public String getSourceTerm() {
        return sourceTerm;
    }

    /**
     * To set sourceTerm
     *
     * @param sourceTerm
     */
    public void setSourceTerm(String sourceTerm) {
        this.sourceTerm = sourceTerm;
    }

    /**
     * To get targetTerm
     *
     * @return targetTerm
     */
    public String getTargetTerm() {
        return targetTerm;
    }

    /**
     * To set targetTerm
     *
     * @param targetTerm
     */
    public void setTargetTerm(String targetTerm) {
        this.targetTerm = targetTerm;
    }

    /**
     * To get new suggested term
     *
     * @return new suggested term
     */
    public String getNewSuggestedTerm() {
        return newSuggestedTerm;
    }

    /**
     * To set new Suggested Term
     *
     * @param new Suggested Term
     */
    public void setNewSuggestedTerm(String newSuggestedTerm) {
        this.newSuggestedTerm = newSuggestedTerm;
    }

    /**
     * To get notes
     *
     * @return notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * To set notes
     *
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

}
