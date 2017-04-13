package com.teaminology.hp.data;

import java.io.Serializable;

public class DepricatedTermDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String depricatedSourceTerm;
	private String depricatedTargetTerm;

	public String getDepricatedSourceTerm() {
		return depricatedSourceTerm;
	}

	public void setDepricatedSourceTerm(String depricatedSourceTerm) {
		this.depricatedSourceTerm = depricatedSourceTerm;
	}

	public String getDepricatedTargetTerm() {
		return depricatedTargetTerm;
	}

	public void setDepricatedTargetTerm(String depricatedTargetTerm) {
		this.depricatedTargetTerm = depricatedTargetTerm;
	}

	@Override
	public String toString() {
		return "DepricatedTermDetails [depricatedSourceTerm="
				+ depricatedSourceTerm + ", depricatedTargetTerm="
				+ depricatedTargetTerm + "]";
	}

}
