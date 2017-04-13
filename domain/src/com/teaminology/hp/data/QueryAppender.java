package com.teaminology.hp.data;



/**
 * POJO class containing the instance variables required for appending an SQL query
 * for transactions like sorting, filter, pagination and search for managing the terms.
 *
 * @author sarvanic
 */
public class QueryAppender implements TeaminologyObject
{
    private String colName;
    private String sortOrder;
    private Integer pageNum;
    private String searchStr;
    private boolean caseFlag;
    private String filterBy;
    private Integer[] selectedIds;
    private String values;
    private String caseSensitive;
    private String searchTerm;
    private String searchType;
    private String termbaseName;
    private String tmProfileName;
    private Integer pageNumTm;
    private String isTM;
    private Integer[] langValues;
    private String filterByCompany;
    private Integer[] selectedCompanyIds;
    private Integer[] selectedLangIds;
    private String filterByTask;
    private Integer[] selectedTaskIds;
    private Integer totalRecords;
    private String searchIndexType;
    private String termVoted;
	private Integer[] termSelectedIds;
	private Integer scrollLimitFrom;
	private Integer scrollLimitTo;
	private String paginationValue;
	
	
	public String getPaginationValue() {
		return paginationValue;
	}

	public void setPaginationValue(String paginationValue) {
		this.paginationValue = paginationValue;
	}
	
	public Integer getScrollLimitFrom() {
		return scrollLimitFrom;
	}

	public void setScrollLimitFrom(Integer scrollLimitFrom) {
		this.scrollLimitFrom = scrollLimitFrom;
	}

	public Integer getScrollLimitTo() {
		return scrollLimitTo;
	}

	public void setScrollLimitTo(Integer scrollLimitTo) {
		this.scrollLimitTo = scrollLimitTo;
	}

    public Integer[] getTermSelectedIds() {
		return termSelectedIds;
	}

	public void setTermSelectedIds(Integer[] termSelectedIds) {
		this.termSelectedIds = termSelectedIds;
	}
	
//To search terms being voted
    public String getTermVoted() {
		return termVoted;
	}

	public void setTermVoted(String termVoted) {
		this.termVoted = termVoted;
	}
    /**
     * To get  column name  of the table
     *
     * @return column name  of the table
     */
    public String getColName() {
        return colName;
    }

    /**
     * To set column name  of the table
     *
     * @param colName column name  of the table
     */
    public void setColName(String colName) {
        this.colName = colName;
    }

    /**
     * To get the sort order of the table
     *
     * @return sort order of the table
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * To set sort order of the table
     *
     * @param sortOrder possible values asc or desc
     */
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * To get the page number for pagination
     *
     * @return page number for pagination
     */
    public Integer getPageNum() {
        return pageNum;
    }

    /**
     * To set page number for pagination
     *
     * @param pageNum page number for pagination
     */
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * To get the search string
     *
     * @return search string
     */
    public String getSearchStr() {
        return searchStr;
    }

    /**
     * To set the search string
     *
     * @param searchStr search string
     */
    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    /**
     * To check whether case sensitive or not
     *
     * @return true or false
     */
    public boolean isCaseFlag() {
        return caseFlag;
    }

    /**
     * To set whether case sensitive or not
     *
     * @param caseFlag possible values true or false
     */
    public void setCaseFlag(boolean caseFlag) {
        this.caseFlag = caseFlag;
    }

    /**
     * To get the filtered field
     *
     * @return filtered field
     */
    public String getFilterBy() {
        return filterBy;
    }

    /**
     * To set the filtered field
     *
     * @param filterBy the filtered field
     */
    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    public Integer[] getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Integer[] selectedIds) {
        this.selectedIds = selectedIds;
    }

    //	public Integer[] getValues() {
//		return values;
//	}
//	public void setValues(Integer[] values) {
//		this.values = values;
//	}
    public String getCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(String caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getValues() {
        return values;
    }

    public String getTermbaseName() {
        return termbaseName;
    }

    public void setTermbaseName(String termbaseName) {
        this.termbaseName = termbaseName;
    }

    public String getTmProfileName() {
        return tmProfileName;
    }

    public void setTmProfileName(String tmProfileName) {
        this.tmProfileName = tmProfileName;
    }

    public Integer getPageNumTm() {
        return pageNumTm;
    }

    public void setPageNumTm(Integer pageNumTm) {
        this.pageNumTm = pageNumTm;
    }

    public String getIsTM() {
        return isTM;
    }

    public void setIsTM(String isTM) {
        this.isTM = isTM;
    }

    public Integer[] getLangValues() {
        return langValues;
    }

    public void setLangValues(Integer[] langValues) {
        this.langValues = langValues;
    }

    public String getFilterByCompany() {
        return filterByCompany;
    }

    public void setFilterByCompany(String filterByCompany) {
        this.filterByCompany = filterByCompany;
    }

    public Integer[] getSelectedCompanyIds() {
        return selectedCompanyIds;
    }

    public void setSelectedCompanyIds(Integer[] selectedCompanyIds) {
        this.selectedCompanyIds = selectedCompanyIds;
    }

    public Integer[] getSelectedLangIds() {
        return selectedLangIds;
    }

    public void setSelectedLangIds(Integer[] selectedLangIds) {
        this.selectedLangIds = selectedLangIds;
    }

    public String getFilterByTask() {
        return filterByTask;
    }

    public void setFilterByTask(String filterByTask) {
        this.filterByTask = filterByTask;
    }

    public Integer[] getSelectedTaskIds() {
        return selectedTaskIds;
    }

    public void setSelectedTaskIds(Integer[] selectedTaskIds) {
        this.selectedTaskIds = selectedTaskIds;
    }

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getSearchIndexType() {
		return searchIndexType;
	}

	public void setSearchIndexType(String searchIndexType) {
		this.searchIndexType = searchIndexType;
	}


}
