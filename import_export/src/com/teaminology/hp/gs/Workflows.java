package com.teaminology.hp.gs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "workflows")
public class Workflows
{
    /**
     *
     */
    private static final long serialVersionUID = -651210137296237747L;
    private List<Workflow> workflow;

    public List<Workflow> getWorkflow() {
        return workflow;
    }

    @XmlElement(name = "workflow")
    public void setWorkflow(List<Workflow> workflow) {
        this.workflow = workflow;
    }


}
