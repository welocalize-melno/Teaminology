package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "offlineFiles")
public class OfflineFiles
{
    private String offlineFiles;

    public String getOfflineFiles() {
        return offlineFiles;
    }

    @XmlValue
    public void setOfflineFiles(String offlineFiles) {
        this.offlineFiles = offlineFiles;
    }


}
