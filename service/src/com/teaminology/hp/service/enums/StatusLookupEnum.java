package com.teaminology.hp.service.enums;

public enum StatusLookupEnum
{
    NEW("New"),
    APPROVED("Approved"),
    IN_REVIEW("In Review"),
    FOREBIDDEN("Forbidden"),
    OBSOLETE("Obsolete");
    private final String value;

    private StatusLookupEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }
}
