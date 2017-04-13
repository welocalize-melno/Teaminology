package com.teaminology.hp.data;

import java.io.Serializable;
import java.util.List;

/**
 * Contains the count of inserted and rejected rows, status,
 * rejected user names and their line numbers while importing terms and user.
 *
 * @author sarvanic
 */
public class CSVImportStatus implements Serializable
{

    private Integer insertedCount;
    private Integer rejectedCount;
    private String missedColumns;
    private List<String> userNames;
    private List<Integer> lineNumbers;
    private String termInformationStatus;
    private Integer updatedCount;

    /**
     * To get the No.of inserted records
     *
     * @return count of inserted records
     */
    public Integer getInsertedCount() {
        return insertedCount;
    }

    /**
     * To set the No.of inserted records
     *
     * @param insertedCount count of number of inserted records
     */
    public void setInsertedCount(Integer insertedCount) {
        this.insertedCount = insertedCount;
    }

    /**
     * To give the No.of rejected records
     *
     * @return count of rejected records
     */
    public Integer getRejectedCount() {
        return rejectedCount;
    }

    /**
     * To set the No.of rejected records
     *
     * @param rejectedCount count of number of rejected records
     */
    public void setRejectedCount(Integer rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    /**
     * To get rejected user names
     *
     * @return list of rejected user names
     */
    public List<String> getUserNames() {
        return userNames;
    }

    /**
     * To set the list of rejected user names
     *
     * @param userNames list of rejected user names
     */
    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    /**
     * To get rejected line numbers in given csv file
     *
     * @return list of rejected line numbers in given csv file
     */
    public List<Integer> getLineNumbers() {
        return lineNumbers;
    }

    /**
     * To set the list of rejected line numbers in given csv file
     *
     * @param lineNumbers rejected line numbers in given csv file
     */
    public void setLineNumbers(List<Integer> lineNumbers) {
        this.lineNumbers = lineNumbers;
    }

    /**
     * To get term information status
     *
     * @return success or failure
     */
    public String getTermInformationStatus() {
        return termInformationStatus;
    }

    /**
     * To set status of term information  csv file that is uploaded
     *
     * @param termInformationStatus status of term information  csv file that is uploaded possible values are "success" or "failure"
     */
    public void setTermInformationStatus(String termInformationStatus) {
        this.termInformationStatus = termInformationStatus;
    }

    public Integer getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(Integer updatedCount) {
        this.updatedCount = updatedCount;
    }

    public String getMissedColumns() {
        return missedColumns;
    }

    public void setMissedColumns(String missedColumns) {
        this.missedColumns = missedColumns;
    }

}
