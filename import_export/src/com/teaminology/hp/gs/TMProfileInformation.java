package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TMProfileInformation")
public class TMProfileInformation
{
    private List<TMProfile> tMProfile;

    public List<TMProfile> gettMProfile() {
        return tMProfile;
    }

    @XmlElement(name = "TMProfile")
    public void settMProfile(List<TMProfile> tMProfile) {
        this.tMProfile = tMProfile;
    }


}
