package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.teaminology.hp.data.TeaminologyObject;

@XmlRootElement
public class Task implements TeaminologyObject
{
    /**
     *
     */
    private static final long serialVersionUID = 535347652847664675L;
    private Integer id;
    private Integer workFlowId;
    private String name;
    private String state;
    private String type;
    private String estimatedAcceptanceDate;
    private String acceptedDate;
    private String estimatedCompletionDate;
    private String completedDate;
    private String availableDate;
    private String assignees;
    private Acceptor accepter;
    private String targetLanguage;
    private String sourceLanguage;
    private String fileInfoStatus;
    private String logUrl;


    public Integer getId() {
        return id;
    }

    @XmlElement(name = "id")
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkFlowId() {
        return workFlowId;
    }

    @XmlElement(name = "workflowId")
    public void setWorkFlowId(Integer workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getName() {
        return name;
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    @XmlElement(name = "state")
    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    @XmlElement(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public String getEstimatedAcceptanceDate() {
        return estimatedAcceptanceDate;
    }

    @XmlElement(name = "estimatedAcceptanceDate")
    public void setEstimatedAcceptanceDate(String estimatedAcceptanceDate) {
        this.estimatedAcceptanceDate = estimatedAcceptanceDate;
    }

    public String getAcceptedDate() {
        return acceptedDate;
    }

    @XmlElement(name = "acceptedDate")
    public void setAcceptedDate(String acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public String getEstimatedCompletionDate() {
        return estimatedCompletionDate;
    }

    @XmlElement(name = "estimatedCompletionDate")
    public void setEstimatedCompletionDate(String estimatedCompletionDate) {
        this.estimatedCompletionDate = estimatedCompletionDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    @XmlElement(name = "completedDate")
    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    @XmlElement(name = "availableDate")
    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public String getAssignees() {
        return assignees;
    }

    @XmlElement(name = "assignees")
    public void setAssignees(String assignees) {
        this.assignees = assignees;
    }

    public Acceptor getAccepter() {
        return accepter;
    }

    @XmlElement(name = "accepter")
    public void setAccepter(Acceptor accepter) {
        this.accepter = accepter;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getFileInfoStatus() {
        return fileInfoStatus;
    }

    public void setFileInfoStatus(String fileInfoStatus) {
        this.fileInfoStatus = fileInfoStatus;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }
}
