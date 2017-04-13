package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Entry
{
    private String percentage;
    private String sid;
    private Source source;
    private List<Target> target;

    public String getPercentage() {
        return percentage;
    }

    @XmlElement
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getSid() {
        return sid;
    }

    @XmlElement
    public void setSid(String sid) {
        this.sid = sid;
    }

    public Source getSource() {
        return source;
    }

    @XmlElement
    public void setSource(Source source) {
        this.source = source;
    }

    public List<Target> getTarget() {
        return target;
    }

    @XmlElement
    public void setTarget(List<Target> target) {
        this.target = target;
    }


}
