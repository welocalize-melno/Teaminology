package com.teaminology.hp.service.enums;

/**
 * Contains field names to import TermInformation data.
 *
 * @author sayeed
 */
public enum TeaminologyImportEnum
{
    FIRST_NAME("First Name"),
    LAST_NAME("Last Name"),
    USER_NAME("User Name"),
    ROLE("Role"),
    EMAIL("Email"),
    LANGUAGE("Language/Locale"),
    PASSWORD("Password"),
    COMPANY("Company"),
    SOURCE_TERM("Source Term"),
    TERM_PART_OF_SPEECH("Term Part Of Speech"),
    TERM_CATEGORY("Term Category"),
    TERM_FORM("Term Form"),
    TERM_NOTES("Term Notes"),
    CONTEXTUAL_EXAMPLE("Term Usage"),
    CONCEPT_DEFINITION("Concept Definition"),
    DOMAIN("Domain"),
    TARGET_LANGUAGE("Target Language"),
    TERM_STATUS("Term Status"),
    TERM_PROGRAM("Term Program"),
    CONCEPT_NOTES("Concept Notes"),
    CONCEPT_CATEGORY("Concept Category"),
    CONCEPT_PRODUCT_GROUP("Concept Product Group"),
    TARGET_TERM_USAGE("Target Term Usage"),
    TARGET_TERM_PROGRAM("Target Term Program"),
    TARGET_TERM_NOTES("Target Term Notes"),
    COMMENTS("Comments"),
    PART_OF_SPEECH("Other"),
    TARGET_TERM("Suggested Term");
  

    private TeaminologyImportEnum(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static TeaminologyImportEnum getEnumColumnById(int id) {
        for (TeaminologyImportEnum column : TeaminologyImportEnum.values()) {
            if (column.ordinal() == id) {
                return column;
            }
        }
        return null;
    }

    public static int getEnumColumnIndexByName(String columnName) {
        for (TeaminologyImportEnum column : TeaminologyImportEnum.values()) {
            String columnArray[] = column.getStringValue().split(",");

            for (String columnValue : columnArray) {
//				 if(columnValue.trim().equalsIgnoreCase(columnName.trim())) {
                if (columnName.trim().toLowerCase().contains(columnValue.trim().toLowerCase())) {
                    return column.ordinal();
                }
            }
        }
        return -1;
    }

    private final String stringValue;
}
