package com.teaminology.hp.bo;

import java.io.Serializable;
import java.util.List;

public class TMProfileList implements Serializable
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
