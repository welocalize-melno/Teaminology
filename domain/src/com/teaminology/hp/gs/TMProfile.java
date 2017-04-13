package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TMProfile")
public class TMProfile
{
    private Integer id;
    private String name;
    private String description;
    private String storageTMName;
    private ReferenceTMGrp referenceTMGrp;

    public Integer getId() {
        return id;
    }

    @XmlElement
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public String getStorageTMName() {
        return storageTMName;
    }

    @XmlElement
    public void setStorageTMName(String storageTMName) {
        this.storageTMName = storageTMName;
    }

    public ReferenceTMGrp getReferenceTMGrp() {
        return referenceTMGrp;
    }

    @XmlElement
    public void setReferenceTMGrp(ReferenceTMGrp referenceTMGrp) {
        this.referenceTMGrp = referenceTMGrp;
    }


}
