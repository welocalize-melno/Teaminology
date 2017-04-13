package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Term
{
    private String langName;
    private String termContent;

    private String isSrc;

    public String getLangName() {
        return langName;
    }

    @XmlElement(name = "lang_name")
    public void setLangName(String langName) {
        this.langName = langName;
    }

    public String getTermContent() {
        return termContent;
    }

    @XmlElement
    public void setTermContent(String termContent) {
        this.termContent = termContent;
    }

    public String getIsSrc() {
        return isSrc;
    }

    @XmlAttribute(name = "isSrc")
    public void setIsSrc(String isSrc) {
        this.isSrc = isSrc;
    }

}
