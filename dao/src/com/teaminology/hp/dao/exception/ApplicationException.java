package com.teaminology.hp.dao.exception;

public class ApplicationException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1821222375455882893L;

	private String errorMessage;

	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public String toString() {
		return "ApplicationException [errorMessage=" + errorMessage + "]";
	}

	


}
