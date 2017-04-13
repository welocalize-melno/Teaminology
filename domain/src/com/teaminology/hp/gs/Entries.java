package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Entries
{
    private List<Entry> entry;

    public List<Entry> getEntry() {
        return entry;
    }

    @XmlElement
    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

}
