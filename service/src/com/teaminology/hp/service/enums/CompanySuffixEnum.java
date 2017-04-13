package com.teaminology.hp.service.enums;

public enum CompanySuffixEnum
{
    LTD("ltd"),
    LIMITED("limited"),
    INC("inc"),
    COMMA(","),
    PERIOD("."),
    SPACE(" "),
    COM("com"),;

    private final String value;

    private CompanySuffixEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }
}
