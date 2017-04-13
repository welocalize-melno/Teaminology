package com.teaminology.hp.bl;

public enum StatusEnum
{
    EXPORTED("Exported"),
    IMPORTED("Imported"),
    IN_PROGRESS("In Progress"),
    IMPORT_FAILED("Import failed"),
    EXPORT_FAILED("Export failed"),;

    private final String value;

    private StatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }
}
