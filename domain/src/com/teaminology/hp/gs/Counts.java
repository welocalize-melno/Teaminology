package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Counts
{
    private List<CountByState> countByState;

    public List<CountByState> getCountByState() {
        return countByState;
    }

    public void setCountByState(List<CountByState> countByState) {
        this.countByState = countByState;
    }

}
