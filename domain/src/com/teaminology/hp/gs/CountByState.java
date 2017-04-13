package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CountByState
{
    private String state;
    private Integer count;

    public String getState() {
        return state;
    }

    @XmlElement(name = "state")
    public void setState(String state) {
        this.state = state;
    }

    public Integer getCount() {
        return count;
    }

    @XmlElement(name = "count")
    public void setCount(Integer count) {
        this.count = count;
    }
}
