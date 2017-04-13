package com.teaminology.hp.bl;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Header
{

    List<Prop> prop;

    public List<Prop> getProp() {
        return prop;
    }

    @XmlElement(name = "prop")
    public void setProp(List<Prop> prop) {
        this.prop = prop;
    }

}
