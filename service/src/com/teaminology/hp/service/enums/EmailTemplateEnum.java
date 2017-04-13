package com.teaminology.hp.service.enums;

public enum EmailTemplateEnum
{
    NEW_REQUEST_CHANGE("New Request Change"),
    YOU_GOT_NEW_RANK("You Got New Rank"),
    ALERT_POLL_EXPIRE("Your voting is going to expire in 2 days"),;
    private final String value;

    private EmailTemplateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }
}
