package com.teaminology.hp.bo;

import java.util.Date;


/**
 * A Persistence class for TermInformation.hbm, this class contains the term related data which includes
 * the term to be polled - its status,parts of speech, category, example, program, language and also its
 * related suggested term information identified by the unique id.
 */
public class TermInformation
{

    private Integer termId;
    private String termBeingPolled;
    private Integer termStatusId;

    private String termUsage;
    private String termNotes;

    private Integer termLangId;
    private String conceptDefinition;
    private String conceptNotes;
    private Integer conceptProdGroup;
    private Integer conceptCatId;
    private String suggestedTerm;
    private Integer suggestedTermLangId;
    private Integer suggestedTermStatusId;
    private Integer suggestedTermFormId;
    private Integer suggestedTermPosId;
    private String suggestedTermUsage;
    private String suggestedTermNotes;
    private Integer suggestedTermPgmId;
    private Integer createdBy;
    private Date createDate;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;
    private String comments;
    private String photoPath;

    //private Set<GlobalsightTermInfo> globalsightTermInfo;
    private String isTM;
    private Company termCompany;
    private Domain termDomain;


    public Company getTermCompany() {
        return termCompany;
    }

    public void setTermCompany(Company termCompany) {
        this.termCompany = termCompany;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getTermBeingPolled() {
        return termBeingPolled;
    }

    public void setTermBeingPolled(String termBeingPolled) {
        this.termBeingPolled = termBeingPolled;
    }

    public Integer getTermStatusId() {
        return termStatusId;
    }

    public void setTermStatusId(Integer termStatusId) {
        this.termStatusId = termStatusId;
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

    public Integer getTermLangId() {
        return termLangId;
    }

    public void setTermLangId(Integer termLangId) {
        this.termLangId = termLangId;
    }

    public String getConceptDefinition() {
        return conceptDefinition;
    }

    public void setConceptDefinition(String conceptDefinition) {
        this.conceptDefinition = conceptDefinition;
    }

    public String getConceptNotes() {
        return conceptNotes;
    }

    public void setConceptNotes(String conceptNotes) {
        this.conceptNotes = conceptNotes;
    }

    public void setConceptProdGroup(Integer conceptProdGroup) {
        this.conceptProdGroup = conceptProdGroup;
    }

    public Integer getConceptProdGroup() {
        return conceptProdGroup;
    }

    public Integer getConceptCatId() {
        return conceptCatId;
    }

    public void setConceptCatId(Integer conceptCatId) {
        this.conceptCatId = conceptCatId;
    }

    public String getSuggestedTerm() {
        return suggestedTerm;
    }

    public void setSuggestedTerm(String suggestedTerm) {
        this.suggestedTerm = suggestedTerm;
    }

    public Integer getSuggestedTermLangId() {
        return suggestedTermLangId;
    }

    public void setSuggestedTermLangId(Integer suggestedTermLangId) {
        this.suggestedTermLangId = suggestedTermLangId;
    }

    public Integer getSuggestedTermStatusId() {
        return suggestedTermStatusId;
    }

    public void setSuggestedTermStatusId(Integer suggestedTermStatusId) {
        this.suggestedTermStatusId = suggestedTermStatusId;
    }

    public Integer getSuggestedTermFormId() {
        return suggestedTermFormId;
    }

    public void setSuggestedTermFormId(Integer suggestedTermFormId) {
        this.suggestedTermFormId = suggestedTermFormId;
    }

    public Integer getSuggestedTermPosId() {
        return suggestedTermPosId;
    }

    public void setSuggestedTermPosId(Integer suggestedTermPosId) {
        this.suggestedTermPosId = suggestedTermPosId;
    }

    public String getSuggestedTermUsage() {
        return suggestedTermUsage;
    }

    public void setSuggestedTermUsage(String suggestedTermUsage) {
        this.suggestedTermUsage = suggestedTermUsage;
    }

    public String getSuggestedTermNotes() {
        return suggestedTermNotes;
    }

    public void setSuggestedTermNotes(String suggestedTermNotes) {
        this.suggestedTermNotes = suggestedTermNotes;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatesBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getSuggestedTermPgmId() {
        return suggestedTermPgmId;
    }

    public void setSuggestedTermPgmId(Integer suggestedTermPgmId) {
        this.suggestedTermPgmId = suggestedTermPgmId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }


    public String getIsTM() {
        return isTM;
    }

    public void setIsTM(String isTM) {
        this.isTM = isTM;
    }

    public Domain getTermDomain() {
        return termDomain;
    }

    public void setTermDomain(Domain termDomain) {
        this.termDomain = termDomain;
    }


}
