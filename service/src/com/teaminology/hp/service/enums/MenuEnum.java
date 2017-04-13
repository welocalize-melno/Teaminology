package com.teaminology.hp.service.enums;

public enum MenuEnum
{
    DASHBOARD("DASHBOARD"),
    TERM_LIST("TERM LIST"),
    PROFILE("PROFILE"),
    ADMIN("ADMIN"),
    SEARCH("SEARCH"),
    GLOBALSIGH("GLOBALSIGHT");
    private final String value;

    private MenuEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }
}