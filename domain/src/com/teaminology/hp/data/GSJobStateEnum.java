package com.teaminology.hp.data;

public enum GSJobStateEnum
{
    ADDING_FILES("ADDING_FILES"),
    CANCELLED("CANCELLED"),
    DTPINPROGRESS("DTPINPROGRESS"),
    LOCALIZED("LOCALIZED"),
    ARCHIVED("ARCHIVED"),
    PENDING("PENDING"),
    IMPORT_FAILED("IMPORT_FAILED"),
    DISPATCHED("DISPATCHED"),
    BATCH_RESERVED("BATCH_RESERVED"),
    EXPORTED("EXPORTED"),
    READY_TO_BE_DISPATCHED("READY_TO_BE_DISPATCHED"),
    EXPORT_FAILED("EXPORT_FAILED"),;

    private final String value;

    private GSJobStateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }

}

