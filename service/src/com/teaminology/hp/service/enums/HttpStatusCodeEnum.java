package com.teaminology.hp.service.enums;

/**
 * @author Sushma
 */

public enum HttpStatusCodeEnum
{
    HTML_STATUS_200(200);

    private HttpStatusCodeEnum(Integer integerValue) {
        this.integerValue = integerValue;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    private final Integer integerValue;
}