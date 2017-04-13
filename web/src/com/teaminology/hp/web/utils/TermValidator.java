package com.teaminology.hp.web.utils;

public class TermValidator {

	

public static String isAlphaCharacters(String fieldName,String fieldValue){
	
	String isAlphaCharExp="^[a-zA-Z_]+$";
	String errorMessage="";
	if(!fieldValue.matches(isAlphaCharExp)){
		errorMessage=fieldName+" should contain Alphabet Characters with underScore are allowed";
	}
	return errorMessage;
}

public static String isFieldNullOrEmpty(String fieldName,Object fieldValue){
	String errorMessage="";
	if(fieldValue==null || fieldValue.toString().isEmpty())
	errorMessage= fieldName+" value cannot be Empty or null";
	return errorMessage;
}
	
	
	
}
