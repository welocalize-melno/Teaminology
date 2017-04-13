package com.teaminology.hp.service.enums;

public enum DBColumnsSizeEnum
{
    CONCEPT_DEFINATION_MAX_SIZE("5000"),
    SOURCE_TERM_MAX_SIZE("20000"),
    TERM_NOTES_MAX_SIZE("5000"),
    CONTEXTUAL_EXAMPLE_MAX_SIZE("5000"),
    TARGET_TERM_MAX_SIZE("20000"),
    DEPRECATED_TERM_MAX_SIZE("20000"),;

    private final String value;

    private DBColumnsSizeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }
}

