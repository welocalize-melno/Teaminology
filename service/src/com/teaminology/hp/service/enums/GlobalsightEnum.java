package com.teaminology.hp.service.enums;


/**
 * Contains values for all constants in Globalsight
 *
 * @author Sayeed
 */
public enum GlobalsightEnum
{
    XLIFF("xliff"),
    FILE("file"),
    ORIGINAL("original"),
    TARGET_LANGUAGE("target-language"),
    SOURCE_LANGUAGE("source-language"),
    NOTE("note"),
    SOURCE_LOCALE("Source Locale:"),
    TARGETLOCALE("Target Locale:"),
    PAGEID("Page ID:"),
    WORKFLOW_ID("Workflow ID:"),
    TASK_ID("Task ID:"),
    ACTIVITY_TYPE("Activity Type:"),
    USER_NAME("User name:"),
    ACCEPT_TIME("Accept time:"),
    ENCODING("Encoding:"),
    DOCUMENT_FORMATE("Document Format:"),
    PLACE_HOLDER_FORMATE("Placeholder Format:"),
    EXACT_MATCH("Exact Match word count:"),
    FUZZY_MATCH("Fuzzy Match word count:"),
    NO_MATCH("No Match word count:"),
    IN_CONTEXT_MATCH("In-Context Match word count:"),
    EDIT_ALL("Edit all:"),
    GS_TM_PROFILE_ID("GlobalSight TM Profile id:"),
    GS_TM_PROFILE_NAME("GlobalSight TM Profile name:"),
    GS_TERMBASE("GlobalSight Termbase:"),
    VERSION("version"),
    HEADER("header"),
    PHASE_GROUP("phase-group"),
    PHASE("phase"),
    PHASE_NAME("phase-name"),
    PROCESS_NAME("process-name"),
    TRANS_UNIT("trans-unit"),
    ID("id"),
    TRANSLATE("translate"),
    SOURCE("source"),
    TARGET("target"),
    STATE("state"),
    ALT_TRANS("alt-trans"),
    ORIGIN("origin"),
    MATCH_QUALITY("match-quality"),
    DATATYPE("datatype"),
    BODY("body"),
    TASK_NAME("Translation"),
    TASKNAME("translation"),

    GLOBALSIGHT("globalsight"),
    JOB_STATUS_EXPORT("EXPORTED");

    private final String value;

    private GlobalsightEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }


}