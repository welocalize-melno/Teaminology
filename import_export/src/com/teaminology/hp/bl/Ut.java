package com.teaminology.hp.bl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "ut")
public class Ut
{
    String value;
    String x;

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

}
