package com.teaminology.hp.web.spring;

import org.apache.commons.fileupload.ProgressListener;

/**
 * A listener class, to know the status of a file while uploading.
 *
 * @author Sarvani
 */
public class FileUploadListener implements ProgressListener
{
    private volatile long bytesRead = 0L, contentLength = 0L, item = 0L;

    public FileUploadListener() {
        super();
    }

    /**
     * To update the instance variables in the process of reading the file
     *
     * @param aBytesRead     No of bytes read from the uploaded file
     * @param aContentLength Actual size of the uploaded content
     * @param anItem         No of items to upload
     */
    public void update(long aBytesRead, long aContentLength, int anItem) {
        bytesRead = aBytesRead;
        contentLength = aContentLength;
        item = anItem;
    }

    /**
     * To get the bytes read from the uploaded file
     *
     * @return Long holding the bytes length
     */
    public long getBytesRead() {
        return bytesRead;
    }

    /**
     * To get the actual size of the uploaded content
     *
     * @return Long holding the content length
     */
    public long getContentLength() {
        return contentLength;
    }

    /**
     * To get the no of items uploaded
     *
     * @return Long holding the count of items
     */
    public long getItem() {
        return item;
    }
}