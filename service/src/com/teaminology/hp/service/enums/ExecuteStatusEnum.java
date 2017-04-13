package com.teaminology.hp.service.enums;

/**
 * Execute status enum
 * User: VincentYan
 * Date: 13-7-9
 * Time: 5:07PM
 */
public enum ExecuteStatusEnum
{
    SUCCESS("success"),
    FAILED("failed"),
    ERROR("error"),
    TERM_EXISTED("failedterm");

    private final String value;

    private ExecuteStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
