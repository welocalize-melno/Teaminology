package com.teaminology.hp.web.spring;



public class AuthCredentials {
	private String userName;
	private String companyName;
	private String authToken;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {	
		this.userName = userName;
	}


	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

}
