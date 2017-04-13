package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LocalizationProfile
{
    private Integer localizationProfileId;
    private String localizationProfileName;

    public Integer getLocalizationProfileId() {
        return localizationProfileId;
    }

    @XmlElement
    public void setLocalizationProfileId(Integer localizationProfileId) {
        this.localizationProfileId = localizationProfileId;
    }

    public String getLocalizationProfileName() {
        return localizationProfileName;
    }

    @XmlElement
    public void setLocalizationProfileName(String localizationProfileName) {
        this.localizationProfileName = localizationProfileName;
    }

}
