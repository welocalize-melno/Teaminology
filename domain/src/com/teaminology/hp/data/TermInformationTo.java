package com.teaminology.hp.data;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.teaminology.hp.bo.DeprecatedTermInformation;
import com.teaminology.hp.bo.TermUpdateDetails;
import com.teaminology.hp.bo.lookup.Category;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.Domain;
import com.teaminology.hp.bo.lookup.Form;
import com.teaminology.hp.bo.lookup.PartsOfSpeech;
import com.teaminology.hp.bo.lookup.Program;

/**
 * A customised TermInformation POJO class for generating reports.
 *
 * @author sarvanic
 */
public class TermInformationTo implements TeaminologyObject
{

    private Integer termId;
    private String termBeingPolled;
    private Integer termStatusId;
    private PartsOfSpeech termPOS;
    private Form termForm;
    private Category termCategory;
    private String termUsage;
    private String termNotes;
    private Program termProgram;
    private Integer termLangId;
    private String conceptDefinition;
    private String conceptNotes;
    private Integer conceptProdGroup;
    private Integer conceptCatId;
    private List<String> suggestedTerms;
    private List<Integer> suggestedTermLangIds;
    private List<Integer> suggestedTermStatusIds;
    private List<Integer> suggestedTermFormId;
    private List<Integer> suggestedTermPosIds;
    private String suggestedTermUsage;
    private String suggestedTermNotes;
    private Integer suggestedTermPgmId;
    private List<Integer> createdBy;
    private List<Date> createDate;
    private List<Integer> updatedBy;
    private List<Date> updateDate;
    private String isActive;
    private String comments;
    private Set<DeprecatedTermInformation> deprecatedTermInfo;
    private List<Set<DeprecatedTermInformation>> targetDepterInfoList;
    private List<String> deprecatedsouceSet;
    private Company company;
    private Domain termDomain;
    List<TermUpdateDetails> termUpdateDetails;


//	private TermVoteMaster termVoteMaster;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

    public Category getTermCategory() {
        return termCategory;
    }

    public void setTermCategory(Category termCategory) {
        this.termCategory = termCategory;
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

    public String getIsActive() {
        return isActive;
    }

    public Domain getTermDomain() {
        return termDomain;
    }

    public void setTermDomain(Domain termDomain) {
        this.termDomain = termDomain;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Program getTermProgram() {
        return termProgram;
    }

    public void setTermProgram(Program termProgram) {
        this.termProgram = termProgram;
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

    public List<String> getSuggestedTerms() {
        return suggestedTerms;
    }

    public void setSuggestedTerms(List<String> suggestedTerms) {
        this.suggestedTerms = suggestedTerms;
    }

    public List<Integer> getSuggestedTermLangIds() {
        return suggestedTermLangIds;
    }

    public void setSuggestedTermLangIds(List<Integer> suggestedTermLangIds) {
        this.suggestedTermLangIds = suggestedTermLangIds;
    }

    public List<Integer> getSuggestedTermStatusIds() {
        return suggestedTermStatusIds;
    }

    public void setSuggestedTermStatusIds(List<Integer> suggestedTermStatusIds) {
        this.suggestedTermStatusIds = suggestedTermStatusIds;
    }

    public List<Integer> getSuggestedTermPosIds() {
        return suggestedTermPosIds;
    }

    public void setSuggestedTermPosIds(List<Integer> suggestedTermPosIds) {
        this.suggestedTermPosIds = suggestedTermPosIds;
    }

    public List<Integer> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(List<Integer> createdBy) {
        this.createdBy = createdBy;
    }

    public List<Date> getCreateDate() {
        return createDate;
    }

    public void setCreateDate(List<Date> createDate) {
        this.createDate = createDate;
    }

    public List<Integer> getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(List<Integer> updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<Date> getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(List<Date> updateDate) {
        this.updateDate = updateDate;
    }

    public PartsOfSpeech getTermPOS() {
        return termPOS;
    }

    public void setTermPOS(PartsOfSpeech termPOS) {
        this.termPOS = termPOS;
    }

    public List<Integer> getSuggestedTermFormId() {
        return suggestedTermFormId;
    }

    public void setSuggestedTermFormId(List<Integer> suggestedTermFormId) {
        this.suggestedTermFormId = suggestedTermFormId;
    }

    public Form getTermForm() {
        return termForm;
    }

    public void setTermForm(Form termForm) {
        this.termForm = termForm;
    }

    public Set<DeprecatedTermInformation> getDeprecatedTermInfo() {
        return deprecatedTermInfo;
    }

    public void setDeprecatedTermInfo(
            Set<DeprecatedTermInformation> deprecatedTermInfo) {
        this.deprecatedTermInfo = deprecatedTermInfo;
    }

    public List<Set<DeprecatedTermInformation>> getTargetDepterInfoList() {
        return targetDepterInfoList;
    }

    public void setTargetDepterInfoList(
            List<Set<DeprecatedTermInformation>> targetDepterInfoList) {
        this.targetDepterInfoList = targetDepterInfoList;
    }

    public List<String> getDeprecatedsouceSet() {
        return deprecatedsouceSet;
    }

    public void setDeprecatedsouceSet(List<String> deprecatedsouceSet) {
        this.deprecatedsouceSet = deprecatedsouceSet;
    }

	public List<TermUpdateDetails> getTermUpdateDetails() {
		return termUpdateDetails;
	}

	public void setTermUpdateDetails(List<TermUpdateDetails> termUpdateDetails) {
		this.termUpdateDetails = termUpdateDetails;
	}


}
