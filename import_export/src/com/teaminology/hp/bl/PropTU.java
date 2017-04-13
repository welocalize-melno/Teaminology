package com.teaminology.hp.bl;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "prop")
public class PropTU
{
    private String prop;
    private String propType;

    public String getProp() {
        return prop;
    }

    @XmlValue
    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getPropType() {
        return propType;
    }

    @XmlAttribute(name = "type")
    public void setPropType(String propType) {
        this.propType = propType;
    }

}
