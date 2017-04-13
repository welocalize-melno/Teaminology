package com.teaminology.hp.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.EmailTemplates;
import com.teaminology.hp.dao.HibernateDAO;
import com.teaminology.hp.dao.PollExpireDAO;

/**
 * To send mail notification when  poll is going to be end.
 * Based on cron expression.
 * @author ratnakumarik
 *
 */
public class PollExpireDAOImpl extends HibernateDAO implements PollExpireDAO {
    private static Logger logger = Logger.getLogger(PollExpireDAOImpl.class);

	private final static String GET_EMAIL_FOR_POLL_EXPIRE = " select  u.email_id , count(tvud.term_id) AS terms  from  user u "
			+  " inner join term_vote_user_details tvud on (u.user_id = tvud.user_id  and "
            +  " tvud.voting_date is null and tvud.updated_by is null) "
			+  " inner join term_vote_master tvm on(tvm.term_id = tvud.term_id ) "
		    +  " where date(tvm.voting_expired_date) = CURDATE() + INTERVAL 2 DAY "
			+  " and (tvud.is_active ='Y' or tvud.is_active is null)" 
			+  " group by u.email_id order by terms desc;";
	  @Override
	    public List<Object>  getEmailForPollExpireAlert() {
	    	HibernateCallback<List<Object>> callback = new HibernateCallback<List<Object>>() {
	    		@Override
	    		public List<Object>  doInHibernate(Session session)
	    				throws HibernateException, SQLException {
	    			StringBuffer query = new StringBuffer();
	    			query.append(GET_EMAIL_FOR_POLL_EXPIRE);
	    		    SQLQuery sqlQuery = session.createSQLQuery(query.toString());
	    		    @SuppressWarnings("unchecked")
					List<Object>  obj = sqlQuery.list();
	    		    logger.info("Emails ids for poll expire "+obj);
	    			return obj;
	    		}
			};
			return getHibernateTemplate().execute(callback);
	    }
	  /**
	     * To get Email Template
	     *
	     * @param emailTemplateId Integer to get Email Template
	     * @return List of EmailTemplates obj's
	     */
	    @Override
	    @Transactional
	    public EmailTemplates getEmailTemplateBySubject(String subject)
	            throws DataAccessException {
	        if (subject == null)
	            return null;

	        Session session = getHibernateSession();
	        Criteria hibCriteria = session.createCriteria(EmailTemplates.class);
	        hibCriteria.add(Restrictions.eq("emailSubject", subject));
	        hibCriteria.add(Restrictions.eq("isActive", "Y"));
	        return (EmailTemplates) hibCriteria.uniqueResult();
	    }
  }
