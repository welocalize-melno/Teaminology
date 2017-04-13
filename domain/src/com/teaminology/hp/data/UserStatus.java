package com.teaminology.hp.data;

/**
 * POJO class containing the instance variables for importing users.
 *
 * @author sarvanic
 */
public class UserStatus
{
    private Boolean isFinished;
    private Long bytesRead;
    private Long contentLength;
    private Long percent;
    private String filePath;
    private String fileName;
    private String errorMsg;

    /**
     * To get whether upload status is finished or not
     *
     * @return true if status is uploaded or false
     */
    public Boolean getIsFinished() {
        return isFinished;
    }

    /**
     * To set whether upload status is finished or not
     *
     * @param isFinished true if status is uploaded or false
     */
    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    /**
     * To get no.of bytes read while uploading
     *
     * @return no.of bytes read while uploading
     */
    public Long getBytesRead() {
        return bytesRead;
    }

    /**
     * To set no.of bytes read while uploading
     *
     * @param bytesRead no.of bytes read while uploading
     */
    public void setBytesRead(Long bytesRead) {
        this.bytesRead = bytesRead;
    }

    /**
     * To get size of the file that is uploading
     *
     * @return size of the file that is uploading
     */
    public Long getContentLength() {
        return contentLength;
    }

    /**
     * To set size of the file that is uploading
     *
     * @param contentLength size of the file that is uploading
     */
    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * To get percentage of uploaded file status
     *
     * @return percentage of uploaded file status
     */
    public Long getPercent() {
        return percent;
    }

    /**
     * To set percentage of uploaded file status
     *
     * @param percent percentage of uploaded file status
     */
    public void setPercent(Long percent) {
        this.percent = percent;
    }

    /**
     * To get uploaded file path
     *
     * @return uploaded file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * To set  uploaded file path
     *
     * @param filePath uploaded file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * To get uploaded file name
     *
     * @return uploaded file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * To set  uploaded file name
     *
     * @param fileName uploaded file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * To get error message
     *
     * @return error message
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * To set error message
     *
     * @param errorMsg error message
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


}
