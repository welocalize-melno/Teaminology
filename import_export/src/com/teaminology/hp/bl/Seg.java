package com.teaminology.hp.bl;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sayeedm
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Seg
{

    private List<Object> compOrValue;

    public List<Object> getCompOrValue() {
        return compOrValue;
    }

    @XmlElementRefs({@XmlElementRef(name = "ut", type = Ut.class)
            , @XmlElementRef(name = "bpt", type = Bpt.class)
            , @XmlElementRef(name = "ept", type = Ept.class)
            , @XmlElementRef(name = "hi", type = Hi.class)
            , @XmlElementRef(name = "it", type = It.class)
            , @XmlElementRef(name = "ph", type = Ph.class)
            , @XmlElementRef(name = "sub", type = Sub.class)
    })
    @XmlMixed

    public void setCompOrValue(List<Object> compOrValue) {
        this.compOrValue = compOrValue;
    }


}
