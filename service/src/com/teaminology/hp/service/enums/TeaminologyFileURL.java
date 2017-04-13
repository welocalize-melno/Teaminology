/**
 * 
 */
package com.teaminology.hp.service.enums;

/**
 * @author Joydev
 *
 */

/*
 * This file will read the WealthAtlas static file location (URL)
 * The file and their location will be stored as key-value pair in standalone/configuration/standalone.xml 
 *  
 */

public enum TeaminologyFileURL {
	
	TEAMINOLOGY_PROPERTY_FILE_LOCATION,
	TEAMINOLOGY_IMPORT_EXPORT_FILE_LOCATION
	;
	
	private final String value;
	
	private TeaminologyFileURL() {
		this.value = System.getProperty(this.name());
	}
	
	public String getValue() {
		return value;
	}
	
	public int getIntValue() {
		return Integer.parseInt(value);
	}
	
	public boolean getBooleanValue() {
		return Boolean.parseBoolean(value);
	}
	
	
}

