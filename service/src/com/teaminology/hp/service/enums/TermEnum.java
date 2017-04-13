package com.teaminology.hp.service.enums;

/**
 * Contains field names to export TermsDebated and TermsInGlossary chart.
 *
 * @author sarvani
 */
public enum TermEnum
{
    NO_OF_TERMS("Number of Terms", 0, 0, false, 8),
    QTR_YEAR("Qrtr/Year", 0, 1, false, 8),;

    private final String columnName;
    private final Integer columnPosition;
    private final Integer rowPosition;
    private final Boolean columnBreak;
    private final Integer columnWidth;

    private TermEnum(String columnName, Integer rowPosition,
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
