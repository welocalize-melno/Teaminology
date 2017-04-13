package com.teaminology.hp.Enum;

/**
 * Contains values for all constants in teaminology
 *
 * @author Sayeed
 */
public enum ImportExportEnum
{

    TEAMINOLOGY_DOMAIN("x-domainteam"),
    TEAMINOLOGY_PROUCTLINE("x-productlineteam"),
    TEAMINOLOGY_COMPANYTEAM("x-companyteam"),
    TEAMINOLOGY_CONTENT("x-contentteam"),
    TEAMINOLOGY_FILE_UPLOAD("Upload Is In Progress"),
    TEAMINOLOGY_IMPORT_PROGRESS("Import Is In Progress"),
    TEAMINOLOGY_FILE_IMPORT_STATUS("Import Completed"),
    TEAMINOLOGY_INVALID_FILE("Invalid File"),
    TEAMINOLOGY_FILE_EXPORT_STATUS("Export Completed"),
    TEAMINOLOGY_BATCH_LIST_SIZE("500"),
    API_URL_SUBSTR("/globalsight/services/AmbassadorWebService?wsdl"),
    RESPONSE_URL_SUBSTR("/globalsight/cxedocs/");
    ;

    private final String value;

    private ImportExportEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }


}
