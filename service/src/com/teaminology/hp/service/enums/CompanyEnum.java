package com.teaminology.hp.service.enums;

public enum CompanyEnum
{
    COMPANY_NAME("Welocalize"),;

    private final String value;

    private CompanyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }
}
