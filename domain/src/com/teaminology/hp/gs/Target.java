package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "target")
public class Target
{
    private String locale;
    private String segment;

    @XmlElement
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getSegment() {
        return segment;
    }

    @XmlElement
    public void setSegment(String segment) {
        this.segment = segment;
    }
}
