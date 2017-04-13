package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.teaminology.hp.data.TeaminologyObject;

@XmlRootElement(name = "sourcePage")
public class SourcePage implements TeaminologyObject
{
    /**
     *
     */
    private static final long serialVersionUID = 9080671141236080972L;
    private Integer sourcePageId;
    private String externalPageId;

    public Integer getSourcePageId() {
        return sourcePageId;
    }

    @XmlElement
    public void setSourcePageId(Integer sourcePageId) {
        this.sourcePageId = sourcePageId;
    }

    public String getExternalPageId() {
        return externalPageId;
    }

    @XmlElement
    public void setExternalPageId(String externalPageId) {
        this.externalPageId = externalPageId;
    }

}
