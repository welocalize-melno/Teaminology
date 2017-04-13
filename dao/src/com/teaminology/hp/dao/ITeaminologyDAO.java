package com.teaminology.hp.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.teaminology.hp.bo.EmailTemplates;


/**
 * The high-level business interface.
 * <p/>
 * <p>This is basically a data access object.
 */
public interface ITeaminologyDAO
{

    /**
     * To get Email Template
     *
     * @param emailTemplateId Integer to get Email Template
     * @return List of EmailTemplates obj's
     * @throws DataAccessException
     */
    public EmailTemplates getEmailTemplate(int emailTemplateId) throws DataAccessException;

    /**
     * To get Email Templates
     *
     * @return List of EmailTemplates obj's
     * @throws DataAccessException
     */

    public List<EmailTemplates> getEmailTemplates() throws DataAccessException;
}

