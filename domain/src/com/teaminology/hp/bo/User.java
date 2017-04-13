package com.teaminology.hp.bo;

import java.util.Date;
import java.util.Set;

import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.Domain;
import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class for User.hbm, this class contains the details of the user which includes their registered userName, password,
 * firstName, lastName, role and the photo path identified by unique id.
 *
 * @author sarvanic
 */
public class User implements TeaminologyObject
{

    private Integer userId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String emailId;
    private String isActive;
    private Date createTime;
    private Integer createdBy;
    private Integer updatedBy;
    private Date updateDate;
    private Date lastLoginTime;
    private Integer wrongPwdEntries;
    private Boolean isTermRequest;
    private String photoPath;
    private Set<UserLanguages> userLanguages;
    private Integer userDomainId;
    private Integer termRequestCount;
    private Company company;
    private Set<UserRole> userRole;
    private Set<CompanyTransMgmt> companyTransMgmt;
    private Domain domain;

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<UserLanguages> getUserLanguages() {
        return userLanguages;
    }

    public void setUserLanguages(Set<UserLanguages> userLanguages) {
        this.userLanguages = userLanguages;
    }


    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getWrongPwdEntries() {
        return wrongPwdEntries;
    }

    public void setWrongPwdEntries(Integer wrongPwdEntries) {
        this.wrongPwdEntries = wrongPwdEntries;
    }

    public Boolean getIsTermRequest() {
        return isTermRequest;
    }

    public void setIsTermRequest(Boolean isTermRequest) {
        this.isTermRequest = isTermRequest;
    }

    public Integer getUserDomainId() {
        return userDomainId;
    }

    public void setUserDomainId(Integer userDomainId) {
        this.userDomainId = userDomainId;
    }

    public Integer getTermRequestCount() {
        return termRequestCount;
    }

    public void setTermRequestCount(Integer termRequestCount) {
        this.termRequestCount = termRequestCount;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<UserRole> getUserRole() {
        return userRole;
    }

    public void setUserRole(Set<UserRole> userRole) {
        this.userRole = userRole;
    }

    public Set<CompanyTransMgmt> getCompanyTransMgmt() {
        return companyTransMgmt;
    }

    public void setCompanyTransMgmt(Set<CompanyTransMgmt> companyTransMgmt) {
        this.companyTransMgmt = companyTransMgmt;
    }


}
