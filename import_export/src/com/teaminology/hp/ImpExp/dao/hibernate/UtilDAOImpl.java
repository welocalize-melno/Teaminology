/**
 *
 */
package com.teaminology.hp.ImpExp.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.teaminology.hp.ImpExp.dao.IUtilDAO;
import com.teaminology.hp.bo.lookup.ILookup;
import com.teaminology.hp.dao.ImpExp.hibernate.HibernateSessionFactory;


/**
 * @author sushma
 */
public class UtilDAOImpl extends HibernateDaoSupport implements IUtilDAO
{

    private static Logger logger = Logger.getLogger(UtilDAOImpl.class);
    static SessionFactory sessFact = null;
    static Session session = null;
    Transaction tr = null;
    public static UtilDAOImpl utilDAOImpl = null;

    private UtilDAOImpl() {
        sessFact = HibernateSessionFactory.getSessionFactory();
        session = sessFact.openSession();
        tr = session.beginTransaction();
    }

    public static UtilDAOImpl getInstance() {
        if (utilDAOImpl == null) {
            utilDAOImpl = new UtilDAOImpl();
        }
        return utilDAOImpl;
    }

    public <C extends ILookup> List<C> getLookup(Class<C> lookupClass) {
        return LookupCache.getInstance().getLookup(session, lookupClass);
    }

    public <C extends ILookup> List<C> updateLookup(Class<C> lookupClass) {
        return LookupCache.getInstance().updateLookup(session, lookupClass);
    }
}
