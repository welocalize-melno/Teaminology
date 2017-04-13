package com.teaminology.hp.bl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "ept")
public class Ept
{
    String value;
    String i;

    public String getValue() {
        return value;
    }

    @XmlValue
    public void setValue(String value) {
        this.value = value;
    }

    public String getI() {
        return i;
    }

    @XmlAttribute(name = "i")
    public void setI(String i) {
        this.i = i;
    }


}
