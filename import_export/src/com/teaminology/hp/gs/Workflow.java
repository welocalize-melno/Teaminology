package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "workflow")
public class Workflow
{
    /**
     *
     */
    private static final long serialVersionUID = -651210137296237747L;
    private Integer wfId;
    private String targetLang;

    public Integer getWfId() {
        return wfId;
    }

    @XmlElement
    public void setWfId(Integer wfId) {
        this.wfId = wfId;
    }

    public String getTargetLang() {
        return targetLang;
    }

    @XmlElement
    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }

}
