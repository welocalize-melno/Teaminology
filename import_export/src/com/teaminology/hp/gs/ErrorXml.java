package com.teaminology.hp.gs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorXml
{
    String method;
    String error;

    public String getMethod() {
        return method;
    }

    @XmlElement
    public void setMethod(String method) {
        this.method = method;
    }

    public String getError() {
        return error;
    }

    @XmlElement
    public void setError(String error) {
        this.error = error;
    }

}
