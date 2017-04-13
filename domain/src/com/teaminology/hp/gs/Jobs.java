package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.teaminology.hp.data.TeaminologyObject;

@XmlRootElement(name = "Jobs")
public class Jobs implements TeaminologyObject
{
    /**
     *
     */
    private static final long serialVersionUID = 6055264825451438475L;
    private List<Job> job;

    public List<Job> getJob() {
        return job;
    }

    @XmlElement(name = "Job")
    public void setJob(List<Job> job) {
        this.job = job;
    }

}
