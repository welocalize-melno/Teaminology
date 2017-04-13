package com.teaminology.hp.bl;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Allows to Read the TUV Element in TMX File
 *
 * @author Sushma
 */
@XmlRootElement(name = "tuv")
public class TUV
{

    String lang;
    String language;
    String creationDate;
    String creationId;
    String changeId;
    String changeDate;
    List<Prop> prop;
    Seg seg;

    @XmlElement
    public Seg getSeg() {
        return seg;
    }

    public void setSeg(Seg seg) {
        this.seg = seg;
    }

    public String getLang() {
        return lang;
    }

    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace", required = true)
    public void setLang(String lang) {
        this.lang = lang;
    }


    public String getLanguage() {
        return language;
    }

    @XmlAttribute(name = "lang")
    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCreationDate() {
        return creationDate;
    }

    @XmlAttribute(name = "creationdate")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationId() {
        return creationId;
    }

    @XmlAttribute(name = "creationid")
    public void setCreationId(String creationId) {
        this.creationId = creationId;
    }

    public String getChangeId() {
        return changeId;
    }

    @XmlAttribute(name = "changeid")
    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getChangeDate() {
        return changeDate;
    }

    @XmlAttribute(name = "changedate")
    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public List<Prop> getProp() {
        return prop;
    }

    @XmlElement(name = "prop")
    public void setProp(List<Prop> prop) {
        this.prop = prop;
    }

}
