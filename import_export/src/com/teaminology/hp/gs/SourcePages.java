package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "sourcePages")
public class SourcePages
{
    /**
     *
     */
    private static final long serialVersionUID = 9080671141236080972L;
    private List<SourcePage> sourcePage;

    public List<SourcePage> getSourcePage() {
        return sourcePage;
    }

    @XmlElement(name = "sourcePage")
    public void setSourcePage(List<SourcePage> sourcePage) {
        this.sourcePage = sourcePage;
    }


}
