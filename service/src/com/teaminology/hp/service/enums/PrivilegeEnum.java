package com.teaminology.hp.service.enums;

public enum PrivilegeEnum {

	PICK_FINAL_TERM("Pick final term");
	
	private final String value;

    private PrivilegeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
