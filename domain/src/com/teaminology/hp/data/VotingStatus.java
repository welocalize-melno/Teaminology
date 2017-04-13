package com.teaminology.hp.data;

/**
 * POJO class containing voting statistics
 *
 * @author sayeedm
 */
public class VotingStatus
{

    private Integer termId;
    private String userName;
    private String votingStatus;
    private String votedTerm;
    private String votedDate;
    private String userComments;

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVotingStatus() {
        return votingStatus;
    }

    public void setVotingStatus(String votingStatus) {
        this.votingStatus = votingStatus;
    }

    public String getVotedTerm() {
        return votedTerm;
    }

    public void setVotedTerm(String votedTerm) {
        this.votedTerm = votedTerm;
    }

    public String getVotedDate() {
        return votedDate;
    }

    public void setVotedDate(String votedDate) {
        this.votedDate = votedDate;
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }

}
