package com.teaminology.hp.service.enums;

/**
 * Contains field names to export User data.
 *
 * @author sarvani
 */
public enum UserDataEnum
{
    FIRST_NAME("First Name", 0, 0, false, 8),
    LAST_NAME("Last Name", 0, 1, false, 8),
    USER_NAME("User Name", 0, 2, false, 8),
    EMAIL("Email", 0, 3, false, 8),
    ROLE("Role", 0, 4, false, 16),
    LANGUAGE("Language/Locale", 0, 5, false, 16),
    COMPANY("Company", 0, 6, false, 16),
    PASSWORD("Password", 0, 7, false, 16),
    MEMBER_SINCE("Member Since", 0, 8, false, 16),;


    private final String columnName;
    private final Integer columnPosition;
    private final Integer rowPosition;
    private final Boolean columnBreak;
    private final Integer columnWidth;

    private UserDataEnum(String columnName, Integer rowPosition,
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
