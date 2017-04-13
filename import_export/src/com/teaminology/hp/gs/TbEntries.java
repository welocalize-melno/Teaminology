package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TbEntries
{
    private List<TbEntry> tbEntry;

    public List<TbEntry> getTbEntry() {
        return tbEntry;
    }

    @XmlElement
    public void setTbEntry(List<TbEntry> tbEntry) {
        this.tbEntry = tbEntry;
    }

}
