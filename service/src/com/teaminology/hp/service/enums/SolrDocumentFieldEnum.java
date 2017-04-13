package com.teaminology.hp.service.enums;

public enum SolrDocumentFieldEnum {

	ID("id", Boolean.TRUE),
	CONTENTTYPE("contenttype", Boolean.TRUE),
	SOURCELANG("sourcelang", Boolean.TRUE),
	PRODUCT("product", Boolean.TRUE),
	SOURCE("source", Boolean.TRUE),	
	TARGET("target", Boolean.TRUE),	
	POS("partofspeech", Boolean.TRUE),
	TARGETPOS("targetpartofspeech",Boolean.TRUE),
	CATEGORY("category", Boolean.TRUE),      
	TARGETLANG("targetlang", Boolean.TRUE),    
	STATUS("status", Boolean.TRUE),  
	DOMAIN("domain", Boolean.TRUE),     
	COMPANYID("companyid", Boolean.TRUE),   
	COMPANY("company", Boolean.TRUE),  
	EXPIREDATE("expireDate", Boolean.TRUE),     
	DEPRECATEDSOURCE1("deprecatedsource1", Boolean.TRUE),
	DEPRECATEDTARGET1("deprecatedtarget1", Boolean.TRUE),
	DEPRECATEDSOURCE2("deprecatedsource2", Boolean.TRUE),
	DEPRECATEDTARGET2("deprecatedtarget2", Boolean.TRUE),
	DEPRECATEDSOURCE3("deprecatedsource3", Boolean.TRUE),
	DEPRECATEDTARGET3("deprecatedtarget3", Boolean.TRUE),
	DEPRECATED("deprecated", Boolean.TRUE),
	TYPE ("type", Boolean.FALSE),
	ORIGIN("origin", Boolean.TRUE),
	FILEID("fileId", Boolean.TRUE),
	TRANSUNITID("transUnitId", Boolean.TRUE),
	TERMID("termId", Boolean.TRUE),
	TASKID("taskId", Boolean.TRUE),
	JOBID("jobId", Boolean.TRUE),
	JOBNAME("jobName", Boolean.TRUE),
	TASKNAME("taskName", Boolean.TRUE),
	PK("primarykey", Boolean.TRUE);
	
	String fieldName;
	Boolean selectable;

	private SolrDocumentFieldEnum(String fieldName, Boolean selectable) {
		this.fieldName = fieldName;
		this.selectable = selectable;
	}
	                  
	public String fieldName(){
		return fieldName;
	}
	
	public Boolean isSelectable(){
		return selectable;
	}
	
	public static String[] getSelectableDocumentField(){
		String selectableFields = "";
		String[] solrDocFields = null;
		for(SolrDocumentFieldEnum value : SolrDocumentFieldEnum.values()){
			if(value.isSelectable()){
				selectableFields = selectableFields+value.fieldName()+",";
			}
		}
		solrDocFields = selectableFields.split(",");
		return solrDocFields;
	}
}
