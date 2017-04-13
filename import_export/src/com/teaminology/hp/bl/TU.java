package com.teaminology.hp.bl;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Allows to Read the TU List in TMX File
 *
 * @author Sushma
 */
@XmlRootElement
public class TU
{

    List<TUV> tuv;
    List<Prop> prop;
    String tuId;
    String creationDate;
    String creationId;
    String changeId;
    String changeDate;


    public List<TUV> getTuv() {
        return tuv;
    }

    @XmlElement
    public void setTuv(List<TUV> tuv) {
        this.tuv = tuv;
    }

    public List<Prop> getProp() {
        return prop;
    }

    @XmlElement(name = "prop")
    public void setProp(List<Prop> prop) {
        this.prop = prop;
    }

    public String getTuId() {
        return tuId;
    }

    @XmlAttribute(name = "tuid")
    public void setTuId(String tuId) {
        this.tuId = tuId;
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

}
