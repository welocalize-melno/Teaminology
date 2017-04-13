package com.teaminology.hp.data;

/**
 * POJO class containing UserComment statistics
 *
 * @author 
 */
public class UserComment {

	private String userName;
	private String userComment;
	private String commentDate;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserComment() {
		return userComment;
	}
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	public String getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}
	
}
