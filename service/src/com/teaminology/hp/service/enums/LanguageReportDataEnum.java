package com.teaminology.hp.service.enums;

/**
 * Contains field names to export Month over Month localisation chart.
 *
 * @author Sarvani
 */
public enum LanguageReportDataEnum
{
    MONTH("Month", 0, 0, false, 8),
    NUMBER("Number", 0, 1, false, 8),
    LANGUAGE("Language", 0, 2, false, 8),;

    private final String columnName;
    private final Integer columnPosition;
    private final Integer rowPosition;
    private final Boolean columnBreak;
    private final Integer columnWidth;

    private LanguageReportDataEnum(String columnName, Integer rowPosition,
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
