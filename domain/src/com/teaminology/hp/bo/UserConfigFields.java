package com.teaminology.hp.bo;

import java.io.Serializable;
import java.util.Date;

public class UserConfigFields implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private Integer id;
private User user;
private Integer fieldId;
private Character isActive;
private Date createdDate;
private Date updatedDate;
private Date deletedDate;
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public Character getIsActive() {
	return isActive;
}
public void setIsActive(Character isActive) {
	this.isActive = isActive;
}
public Date getCreatedDate() {
	return createdDate;
}
public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}
/**
 * @return the updatedDate
 */
public Date getUpdatedDate() {
	return updatedDate;
}
/**
 * @param updatedDate the updatedDate to set
 */
public void setUpdatedDate(Date updatedDate) {
	this.updatedDate = updatedDate;
}
public Date getDeletedDate() {
	return deletedDate;
}
public void setDeletedDate(Date deletedDate) {
	this.deletedDate = deletedDate;
}
public Integer getFieldId() {
	return fieldId;
}
public void setFieldId(Integer fieldId) {
	this.fieldId = fieldId;
}
/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((fieldId == null) ? 0 : fieldId.hashCode());
	result = prime * result + ((user == null) ? 0 : user.hashCode());
	return result;
}
/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	UserConfigFields other = (UserConfigFields) obj;
	if (fieldId == null) {
		if (other.fieldId != null)
			return false;
	} else if (!fieldId.equals(other.fieldId))
		return false;
	if (user == null) {
		if (other.user != null)
			return false;
	} else if (!user.equals(other.user))
		return false;
	return true;
}


}
