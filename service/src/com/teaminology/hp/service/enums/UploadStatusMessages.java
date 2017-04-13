package com.teaminology.hp.service.enums;

public enum UploadStatusMessages
{

    TOO_LARGE("This file exceeds the @@FILESIZE@@ MB attachment limit."),
    SUCCESS("Upload Completed. Import is in progress."),
    INVALID("Upload failed "),
    INVALID_FILE_TYPE("Invalid file type");

    private final String value;

    private UploadStatusMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue() {
        return new Integer(value);
    }


}
