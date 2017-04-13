package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TermbaseInformation")
public class TermbaseInformation
{
    private List<Termbase> termbase;

    public List<Termbase> getTermbase() {
        return termbase;
    }

    @XmlElement(name = "Termbase")
    public void setTermbase(List<Termbase> termbase) {
        this.termbase = termbase;
    }

}
