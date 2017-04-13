package com.teaminology.hp.service.enums;

/**
 * Contains field names to export TermInformation data.
 *
 * @author sarvani
 */
public enum TermInformationEnum
{

    SOURCE_TERM("Source Term", 0, 0, true, 8),
    TERM_STATUS("Term Status", 0, 1, false, 8),
    TERM_FORM("Term Form", 0, 2, false, 8),
    TERM_PART_OF_SPEECH("Term Part Of Speech", 0, 3, false, 8),
    TERM_USAGE("Term Usage", 0, 4, false, 8),
    TERM_CATEGORY("Term Category", 0, 5, false, 8),
    TERM_NOTES("Term Notes", 0, 6, false, 8),
    TERM_PROGRAM("Term Program", 0, 7, false, 8),
    CONCEPT_DEFINITION("Concept Definition", 0, 8, false, 8),
    CONCEPT_NOTES("Concept Notes", 0, 9, false, 8),
    CONCEPT_CATEGORY("Concept Category", 0, 10, false, 8),
    CONCEPT_PRODUCT_GROUP("Concept Product Group", 0, 11, false, 8),
    //TARGET_LANGUAGE("Target Language", 0, 12, false, 8),
    //TARGET_TERM("Target Term", 0, 13, true, 8),
    //TARGET_TERM_STATUS("Target Term Status",0, 12, false, 8),
    //TARGET_TERM_FORM("Target Term Form", 0, 13, false, 8),
    //TARGET_TERM_PART_OF_SPEECH("Target Term Part Of Speech", 0, 14,false, 8),
    TARGET_TERM_USAGE("Target Term Usage", 0, 12, false, 8),
    TARGET_TERM_NOTES("Target Term Notes", 0, 13, false, 8),
    TARGET_TERM_PROGRAM("Target Term Program", 0, 14, true, 8),
    COMMENTS("Comments", 0, 15, false, 8),
    COMPANY("Company", 0, 16, false, 8),
    TERM_DOMAIN("Domain", 0, 17, false, 8);

    private final String columnName;
    private final Integer columnPosition;
    private final Integer rowPosition;
    private final Boolean columnBreak;
    private final Integer columnWidth;

    private TermInformationEnum(String columnName, Integer rowPosition,
                                Integer columnPosition, Boolean columnBreak, Integer columnWidth) {
        this.columnName = columnName;
        this.rowPosition = rowPosition;
        this.columnPosition = columnPosition;
        this.columnBreak = columnBreak;
        this.columnWidth = columnWidth;
    }

    public String getColumnName() {
        return columnName;
    }

    public Integer getColumnPosition() {
        return columnPosition;
    }

    public Integer getRowPosition() {
        return rowPosition;
    }

    public Boolean getColumnBreak() {
        return columnBreak;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }
}
