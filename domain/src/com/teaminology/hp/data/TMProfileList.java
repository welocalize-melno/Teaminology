package com.teaminology.hp.data;

import java.util.List;

/**
 * Used to get TMProfile List from jetty server using GSon
 *
 * @author sayeedm
 */
public class TMProfileList
{
    private List<TmProfileBo> insertedList;

    private List<TmProfileBo> updatedList;

    public List<TmProfileBo> getInsertedList() {
        return insertedList;
    }

    public void setInsertedList(List<TmProfileBo> insertedList) {
        this.insertedList = insertedList;
    }

    public List<TmProfileBo> getUpdatedList() {
        return updatedList;
    }

    public void setUpdatedList(List<TmProfileBo> updatedList) {
        this.updatedList = updatedList;
    }


}
