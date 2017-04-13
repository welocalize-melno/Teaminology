package com.teaminology.hp.service.enums;

/**
 * Contains field names to export Overview chart.
 *
 * @author sarvani
 */
public enum LanguageDataReportEnum
{
    LANGUAGE_TEAM("Language Team", 0, 0, false, 8),
    MEMBERS("Members", 0, 1, false, 8),
    ACCURACY("Accuracy", 0, 2, false, 8),
    TERMS("Terms", 0, 3, false, 8),
    DEBATED_TERMS("Debated Terms", 0, 4, false, 8),
    ACTIVE_POLLS("Active Polls", 0, 5, false, 8),
    MONTHLY_AVG("Monthly Avg", 0, 6, false, 8),
    TOTAL_VOTES("Total Votes", 0, 7, false, 8),;


    private final String columnName;
    private final Integer columnPosition;
    private final Integer rowPosition;
    private final Boolean columnBreak;
    private final Integer columnWidth;

    private LanguageDataReportEnum(String columnName, Integer rowPosition,
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
