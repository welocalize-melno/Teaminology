package com.teaminology.hp.bl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Allows to Read the TMX File
 *
 * @author Sushma
 */
@XmlRootElement(name = "tmx")
public class TMX
{

    Body body;
    Header header;

    public Body getBody() {
        return body;
    }

    @XmlElement(name = "body")
    public void setBody(Body body) {
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    @XmlElement(name = "header")
    public void setHeader(Header header) {
        this.header = header;
    }

}
