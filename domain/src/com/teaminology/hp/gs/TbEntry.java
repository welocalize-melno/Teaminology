package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TbEntry
{
    private String tbName;
    private List<Term> term;

    //private Term term;
    public String getTbName() {
        return tbName;
    }

    @XmlElement
    public void setTbName(String tbName) {
        this.tbName = tbName;
    }

    public List<Term> getTerm() {
        return term;
    }

    @XmlElement
    public void setTerm(List<Term> term) {
        this.term = term;
    }


//public Term getTerm() {
//	return term;
//}
//@XmlElement
//public void setTerm(Term term) {
//	this.term = term;
//}


}
