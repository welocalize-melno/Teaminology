package com.teaminology.hp.data;

import java.util.List;

public class GSSearchObject
{

    private List<GSJobObject> termBaseList;
    private List<GSJobObject> tmProfileList;
    private Integer totalRecords;
    
    public List<GSJobObject> getTermBaseList() {
        return termBaseList;
    }

    public void setTermBaseList(List<GSJobObject> termBaseList) {
        this.termBaseList = termBaseList;
    }

    public List<GSJobObject> getTmProfileList() {
        return tmProfileList;
    }

    public void setTmProfileList(List<GSJobObject> tmProfileList) {
        this.tmProfileList = tmProfileList;
    }

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}


}
