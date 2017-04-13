package com.teaminology.hp.data;

import java.io.Serializable;
import java.util.List;

/**
 * Contains the List of all Leader Board members and HP Members
 * as an instance variable required for a data table and also total records and count of total records.
 *
 * @author sarvanic
 */
public class DataTable implements Serializable
{

    private List<Member> aaData;
    private int iTotalRecords;
    private int iTotalDisplayRecords;
    private List<VotingStatus> termData;
    private List<UserComment> commentData;

    /**
     * To get list of all leader board members and HP members
     *
     * @return List of all leader board members and HP members
     */
    public List<Member> getAaData() {
        return aaData;
    }

    /**
     * To get  total no.of records fetched
     *
     * @return total no.of records fetched
     */
    public int getiTotalRecords() {
        return iTotalRecords;
    }

    /**
     * To set total no.of records fetched
     *
     * @param iTotalRecords total no.of records fetched
     */
    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    /**
     * To get total no.of records displayed
     *
     * @return total no.of records displayed
     */
    public int getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    /**
     * To set total no.of records displayed
     *
     * @param iTotalDisplayRecords total no.of records displayed
     */
    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    /**
     * To set list of all leader board members and HP members
     *
     * @param aaData list of all leader board members and HP members
     */
    public void setAaData(List<Member> aaData) {
        this.aaData = aaData;
    }

    /**
     * To get list of voting status
     *
     * @return List of voting status
     */
    public List<VotingStatus> getTermData() {
        return termData;
    }

    /**
     * To set list of voting status
     *
     * @param termData list of voting status
     */
    public void setTermData(List<VotingStatus> termData) {
        this.termData = termData;
    }

	public List<UserComment> getCommentData() {
		return commentData;
	}

	public void setCommentData(List<UserComment> commentData) {
		this.commentData = commentData;
	}

}
