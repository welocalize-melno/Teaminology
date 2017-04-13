package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.teaminology.hp.data.TeaminologyObject;

@XmlRootElement
public class Job implements TeaminologyObject
{
    /**
     *
     */
    private static final long serialVersionUID = 535347652847664675L;
    private Integer id;
    private String name;
    private String state;
    private String priority;
    private String displayState;
    private String createDate;
    private String startDate;
    private LocalizationProfile localizationProfile;
    private Project project;
    private Integer wordCount;
    private String sourceLang;
    private String dueDate;
    private SourcePages sourcePages;
    private Workflows workflows;

    public Integer getId() {
        return id;
    }

    @XmlElement
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    @XmlElement
    public void setState(String state) {
        this.state = state;
    }

    public String getPriority() {
        return priority;
    }

    @XmlElement
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDisplayState() {
        return displayState;
    }

    @XmlElement
    public void setDisplayState(String displayState) {
        this.displayState = displayState;
    }

    public String getCreateDate() {
        return createDate;
    }

    @XmlElement
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStartDate() {
        return startDate;
    }

    @XmlElement
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public LocalizationProfile getLocalizationProfile() {
        return localizationProfile;
    }

    @XmlElement
    public void setLocalizationProfile(LocalizationProfile localizationProfile) {
        this.localizationProfile = localizationProfile;
    }

    public Project getProject() {
        return project;
    }

    @XmlElement
    public void setProject(Project project) {
        this.project = project;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    @XmlElement
    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public String getSourceLang() {
        return sourceLang;
    }

    @XmlElement
    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    public String getDueDate() {
        return dueDate;
    }

    @XmlElement
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public SourcePages getSourcePages() {
        return sourcePages;
    }

    @XmlElement(name = "sourcePages")
    public void setSourcePages(SourcePages sourcePages) {
        this.sourcePages = sourcePages;
    }

    public Workflows getWorkflows() {
        return workflows;
    }

    @XmlElement(name = "workflows")
    public void setWorkflows(Workflows workflows) {
        this.workflows = workflows;
    }

}
