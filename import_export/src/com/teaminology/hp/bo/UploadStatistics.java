package com.teaminology.hp.bo;

public class UploadStatistics
{
    private Integer insertCount;
    private Integer updateCount;
    private Integer skippedCount;
    private Integer totalcountForBatch;

    public Integer getInsertCount() {
        return insertCount;
    }

    public void setInsertCount(Integer insertCount) {
        this.insertCount = insertCount;
    }

    public Integer getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(Integer updateCount) {
        this.updateCount = updateCount;
    }

    public Integer getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(Integer skippedCount) {
        this.skippedCount = skippedCount;
    }

    public Integer getTotalcountForBatch() {
        return totalcountForBatch;
    }

    public void setTotalcountForBatch(Integer totalcountForBatch) {
        this.totalcountForBatch = totalcountForBatch;
    }


}
