package com.teaminology.hp.service.enums;

/**
 * Contains values for all constants in teaminology
 *
 * @author Sayeed
 */
public enum TeaminologyEnum
{

	ENGLISH("English (United States)"),
    DEPRECATED_TERM("Deprecated"),
    DEPRECATED_TERM1("Deprecated1"),
    DEPRECATED_TERM2("Deprecated2"),
    DEPRECATED_TERM3("Deprecated3"),
    TYPE("type"),
    TERM("term"),
    TERM_NOTE("termNote"),
    TERM_PART_OF_SPEECH("Part Of Speech"),
    DEFINITION("definition"),
    TERM_ENRY("termEntry"),
    TERM_DESCRIPTION("descrip"),
    LANG_SET("langSet"),
    LANG("xml:lang"),
    DEPRECATED_SOURCE1("Deprecated Source1"),
    DEPRECATED_SOURCE2("Deprecated Source2"),
    DEPRECATED_SOURCE3("Deprecated Source3"),
    TERM_STATUS("Status"),
    TERM_FORM("Form"),
    DEPRECATED_TARGET1("Deprecated Target1"),
    DEPRECATED_TARGET2(" Deprecated Target2"),
    DEPRECATED_TARGET3("Deprecated Target3"),
    SELECT("--select--"),
    ENGLISH_SOURCE("en_US"),
    TM("TM"),
    TERM_BASE("TermBase"),
    TERM_COL_COUNT("6"),
    TERM_COL_TRAN_COUNT("7"),
    TEAMINOLOGY_DOMAIN("x-domainteam"),
    TEAMINOLOGY_PROUCTLINE("x-productlineteam"),
    TEAMINOLOGY_COMPANYTEAM("x-companyteam"),
    TEAMINOLOGY_CONTENT("x-contentteam"),
    TEAMINOLOGY_FILE_IMPORT("Import is in Progress"),
    TEAMINOLOGY_FILE_UPLOAD("Upload is in Progress"),
    TEAMINOLOGY_FILE_DOWNLOAD("Download is in Progress"),
    TEAMINOLOGY_FILE_IMPORT_UPLOAD("File Upload is in Progress"),
    TEAMINOLOGY_IMPORT("Import"),
    TEAMINOLOGY_EXPORT("Export"),
    TEAMINOLOGY_IMPORT_TM("Import TM"),
    TEAMINOLOGY_EXPORT_TM("Export TM"),
    CONTROLLER_URL("/teaminology_ctrler/teaminology"),
    TEAMINOLOGY_TMX_TYPE("TMX"),
    TEAMINOLOGY_GS_TYPE("GS"),
    TEAMINOLOGY_IMPORT_GS("Import GS"),
    IMAGES("/images"),
    TEAMINOLOGY_EXPORT_GS("Export GS"),
     FINAL("Final"),
    MAX_SEARCH_RESULT_LIMIT("1000000");
    

    private final String value;

    private TeaminologyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }


}
