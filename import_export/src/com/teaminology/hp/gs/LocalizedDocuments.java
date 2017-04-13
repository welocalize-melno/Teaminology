package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LocalizedDocuments
{
    private String urlPrefix;
    private String targetLocale;

    public String getUrlPrefix() {
        return urlPrefix;
    }

    @XmlElement
    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getTargetLocale() {
        return targetLocale;
    }

    @XmlElement
    public void setTargetLocale(String targetLocale) {
        this.targetLocale = targetLocale;
    }

}
