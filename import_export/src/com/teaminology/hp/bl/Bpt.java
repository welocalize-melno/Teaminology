package com.teaminology.hp.bl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "bpt")
public class Bpt
{
    String value;
    String x;
    String i;
    String type;

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

    public String getI() {
        return i;
    }

    @XmlAttribute(name = "i")
    public void setI(String i) {
        this.i = i;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setType(String type) {
        this.type = type;
    }

}
