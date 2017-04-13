package com.teaminology.hp.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.EmailTemplates;
import com.teaminology.hp.dao.HibernateDAO;
import com.teaminology.hp.dao.ITeaminologyDAO;

/**
 * Contains DAO methods to handle email templates.
 *
 * @author sarvaniC
 */
public class TeaminologyDAOImpl extends HibernateDAO implements
        ITeaminologyDAO
{
    /**
     * To get Email Template
     *
     * @param emailTemplateId Integer to get Email Template
     * @return List of EmailTemplates obj's
     */
    @Override
    @Transactional
    public EmailTemplates getEmailTemplate(int emailTemplateId)
            throws DataAccessException {
        if (emailTemplateId < 1)
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(EmailTemplates.class);
        hibCriteria.add(Restrictions.eq("emailTemplateId", emailTemplateId));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        return (EmailTemplates) hibCriteria.uniqueResult();
    }
    
    
    /**
     * To get Email Templates
     *
     * @return List of EmailTemplates obj's
     */
    @Override
    @Transactional
    public List<EmailTemplates> getEmailTemplates() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(EmailTemplates.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        return (List<EmailTemplates>) hibCriteria.list();
    }
}
