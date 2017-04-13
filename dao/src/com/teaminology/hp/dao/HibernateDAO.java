/**
 * 
 */
package com.teaminology.hp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author jykim
 *
 */
public abstract class HibernateDAO {

	private SessionFactory sessionFactory;
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Session: Hibernate session
	 */
	private Session _session;
	
	protected HibernateDAO() {
		
	}
	
	protected Session getHibernateSession() {
		_session = getSessionFactory().getCurrentSession();
		return _session;
	}
	protected HibernateTemplate getHibernateTemplate(){
		return new HibernateTemplate(sessionFactory);
	}
}
