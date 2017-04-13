package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Project
{
    private Integer projectId;
    private String projectName;

    public Integer getProjectId() {
        return projectId;
    }

    @XmlElement
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    @XmlElement
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


}
