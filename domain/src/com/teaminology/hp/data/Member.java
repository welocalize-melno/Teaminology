package com.teaminology.hp.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * A customised POJO class of User.
 *
 * @author sarvanic
 */
public class Member implements Serializable
{

    private Integer userId;
    //	private String userFullName;
    private String userName;
    private String firstName;
    private String lastName;
    private Date createTime;
    private String createDate;
    private String photoPath;
    private BigInteger totalVotes;
    private String languages;
    private String emailId;
    private BigInteger lastMonthVotes;
    private BigDecimal accuracy;
    private String userTypeName;
    private Integer domainId;
    private Integer termRequestCount;
    private BigInteger badgingRate;
    private String bagdingPhotopath;
    private String accuracyPhotoPath;
    private String votes;
    private String companyName;
    private String userRole;

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    //	public void setUserFullName(String userFullName) {
//		this.userFullName = userFullName;
//	}
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigInteger getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(BigInteger totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    //	public String getUserFullName() {
//		return userFullName;
//	}
//	public void setUserFullName(String firstName,  String lastName) {
//		this.userFullName = firstName + " "+ lastName;
//	}
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {

//		this.firstName = firstName.substring(0,1).toUpperCase()  + firstName.substring(1, firstName.length()).toLowerCase();
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
//		this.lastName = lastName.substring(0,1).toUpperCase()  + lastName.substring(1, lastName.length()).toLowerCase();
        this.lastName = lastName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigInteger getLastMonthVotes() {
        return lastMonthVotes;
    }

    public void setLastMonthVotes(BigInteger lastMonthVotes) {
        this.lastMonthVotes = lastMonthVotes;
    }

    public BigDecimal getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(BigDecimal accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public Integer getTermRequestCount() {
        return termRequestCount;
    }

    public void setTermRequestCount(Integer termRequestCount) {
        this.termRequestCount = termRequestCount;
    }

    public BigInteger getBadgingRate() {
        return badgingRate;
    }

    public void setBadgingRate(BigInteger badgingRate) {
        this.badgingRate = badgingRate;
    }

    public String getBagdingPhotopath() {
        return bagdingPhotopath;
    }

    public void setBagdingPhotopath(String bagdingPhotopath) {
        this.bagdingPhotopath = bagdingPhotopath;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getAccuracyPhotoPath() {
        return accuracyPhotoPath;
    }

    public void setAccuracyPhotoPath(String accuracyPhotoPath) {
        this.accuracyPhotoPath = accuracyPhotoPath;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }


}
