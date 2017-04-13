package com.teaminology.hp.ImpExp.dao.hibernate;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.teaminology.hp.bo.lookup.ILookup;


/**
 * @author Sayeed
 */
public class LookupCache extends HibernateDaoSupport
{

    private static Logger logger = Logger.getLogger(LookupCache.class);

    private static LookupCache INSTANCE = new LookupCache();

    @SuppressWarnings("unchecked")
    private Hashtable<Class, List> cache;

    public static LookupCache getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    private LookupCache() {
        cache = new Hashtable<Class, List>();
    }

    @SuppressWarnings("unchecked")
    public <C extends ILookup> List<C> getLookup(Session session, Class<C> lookupClass) {
        List<C> lookup = cache.get(lookupClass);
        if (lookup == null) {
            lookup = updateLookup(session, lookupClass);
        }
        return lookup;
    }


    @SuppressWarnings("unchecked")
    public <C extends ILookup> List<C> updateLookup(Session session, Class<C> lookupClass) {
        List<C> lookup = (List<C>) getAll(session, lookupClass);
        cache.put(lookupClass, lookup);
        return lookup;
    }

    public <C> Collection<C> getAll(Session session, Class<C> persistentClass) {

        String hqlQuery = "from " + persistentClass.getName() + " where  isActive = 'Y'";

        Query query = session.createQuery(hqlQuery);
        return query.list();
    }

	/*@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Session session = HibernateSessionFactory.getThreadCurrentSession();
		Set<Class> keys = INSTANCE.cache.keySet();
		for (Class lookupClass : keys) {
			updateLookup(session, lookupClass);
		}
		logger.info("Synchronized lookup data for " + keys.size() + " lookup types.");
		HibernateSessionFactory.closeThreadCurrentSession();
	}*/


}
