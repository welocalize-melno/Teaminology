package com.teaminology.hp.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.teaminology.hp.bo.EmailTemplates;

public interface PollExpireDAO {
	/**
	 * 
	 * @return
	 */
	public List<Object>  getEmailForPollExpireAlert();
	 /**
     * To send email when poll is going to expire in 2 days
     * @param subject
     * @return
     * @throws DataAccessException
     */

    public EmailTemplates getEmailTemplateBySubject(String subject) throws DataAccessException;
}
