package com.teaminology.hp.bl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "ph")
public class Ph
{
    String value;
    String x;
    String type;
    String assoc;

    public String getValue() {
        return value;
    }

    @XmlValue
    public void setValue(String value) {
        this.value = value;
    }

    public String getX() {
        return x;
    }

    @XmlAttribute(name = "x")
    public void setX(String x) {
        this.x = x;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public String getAssoc() {
        return assoc;
    }

    @XmlAttribute(name = "assoc")
    public void setAssoc(String assoc) {
        this.assoc = assoc;
    }

}
