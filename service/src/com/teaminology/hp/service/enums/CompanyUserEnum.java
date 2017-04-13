package com.teaminology.hp.service.enums;

public enum CompanyUserEnum
{
    ADMIN("admin"),
    TERMLEAD("termlead"),
    TRANSLATOR("translator"),
    PASSWORD("password"),;

    private final String value;

    private CompanyUserEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }

}
