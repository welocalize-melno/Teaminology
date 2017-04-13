package com.teaminology.hp.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.teaminology.hp.bo.TermInformation;

/**
 * A POJO class related to the invitations for voting, users, and mailing for new term request.
 *
 * @author sarvanic
 */
public class Invite implements Serializable
{
    private String[] mailIds;
    private Integer emailTemplateId;
    private Integer[] termIds;
    private Integer[] userIds;
    private Integer votingPeriod;
    private  String  companyContext;
    private Integer companyId;
    private String companyRoot;
    private Map<String,List<TermInformation>> userPollTerms;
    
	public Map<String, List<TermInformation>> getUserPollTerms() {
		return userPollTerms;
	}

	public void setUserPollTerms(Map<String, List<TermInformation>> userPollTerms) {
		this.userPollTerms = userPollTerms;
	}

	public String getCompanyRoot() {
		return companyRoot;
	}

	public void setCompanyRoot(String companyRoot) {
		this.companyRoot = companyRoot;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	
    
    
    public String getCompanyContext() {
		return companyContext;
	}

	public void setCompanyContext(String companyContext) {
		this.companyContext = companyContext;
	}

	public String[] getMailIds() {
        return mailIds;
    }

    public void setMailIds(String[] mailIds) {
        this.mailIds = mailIds;
    }

    public Integer getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(Integer emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

    public Integer[] getTermIds() {
        return termIds;
    }

    public void setTermIds(Integer[] termIds) {
        this.termIds = termIds;
    }

    public Integer[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Integer[] userIds) {
        this.userIds = userIds;
    }

    public Integer getVotingPeriod() {
        return votingPeriod;
    }

    public void setVotingPeriod(Integer votingPeriod) {
        this.votingPeriod = votingPeriod;
    }


}
