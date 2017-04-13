package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "localePair")
public class LocalePair
{
    private String id;
    private SourceLocale sourceLocale;
    private TargetLocale targetLocale;

    public String getId() {
        return id;
    }

    @XmlElement
    public void setId(String id) {
        this.id = id;
    }

    public SourceLocale getSourceLocale() {
        return sourceLocale;
    }

    @XmlElement
    public void setSourceLocale(SourceLocale sourceLocale) {
        this.sourceLocale = sourceLocale;
    }

    public TargetLocale getTargetLocale() {
        return targetLocale;
    }

    @XmlElement
    public void setTargetLocale(TargetLocale targetLocale) {
        this.targetLocale = targetLocale;
    }

}
