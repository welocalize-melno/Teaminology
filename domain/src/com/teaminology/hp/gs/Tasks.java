package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.teaminology.hp.data.TeaminologyObject;

@XmlRootElement(name = "tasksInJob")
public class Tasks implements TeaminologyObject
{
    /**
     *
     */
    private static final long serialVersionUID = 6055264825451438475L;

    private String jobId;

    public String getJobId() {
        return jobId;
    }

    @XmlElement(name = "jobId")
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    private List<Task> task;

    public List<Task> getTask() {
        return task;
    }

    @XmlElement(name = "task")
    public void setTask(List<Task> task) {
        this.task = task;
    }


}
