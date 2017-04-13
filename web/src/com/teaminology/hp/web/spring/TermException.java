package com.teaminology.hp.web.spring;

public class TermException extends Exception {

	
	private static final long serialVersionUID = 1L;
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public TermException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}
		
	public TermException() {
		super();
	}
}
