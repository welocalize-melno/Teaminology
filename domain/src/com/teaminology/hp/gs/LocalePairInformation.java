package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LocalePairInformation")
public class LocalePairInformation
{
    private List<LocalePair> localePair;

    public List<LocalePair> getLocalePair() {
        return localePair;
    }

    @XmlElement(name = "localePair")
    public void setLocalePair(List<LocalePair> localePair) {
        this.localePair = localePair;
    }
}
