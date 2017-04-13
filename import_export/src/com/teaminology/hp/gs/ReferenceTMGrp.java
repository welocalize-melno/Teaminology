package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "referenceTMGrp")
public class ReferenceTMGrp
{
    private Integer id;
    private String referenceTM;

    public Integer getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(Integer id) {
        this.id = id;
    }

    public String getReferenceTM() {
        return referenceTM;
    }

    @XmlElement
    public void setReferenceTM(String referenceTM) {
        this.referenceTM = referenceTM;
    }

}
