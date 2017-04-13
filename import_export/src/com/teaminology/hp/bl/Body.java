package com.teaminology.hp.bl;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Allows to Read the Body Content in TMX File
 *
 * @author Sushma
 */
@XmlRootElement
public class Body
{
    List<TU> tu;

    public List<TU> getTu() {
        return tu;
    }

    @XmlElement
    public void setTu(List<TU> tu) {
        this.tu = tu;
    }

}
