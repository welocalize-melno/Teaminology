package com.teaminology.hp.web.spring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.teaminology.hp.dao.exception.ApplicationException;



@ControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler({Exception.class,ApplicationException.class})
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
		
		IllegalStateException illegalStateException=(IllegalStateException)ex;
		ApplicationException ae =(ApplicationException) illegalStateException.getCause();
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setStatusMsg(ae.getErrorMessage());
		
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}