/**
 *
 */
package com.teaminology.hp.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.lookup.ILookup;
import com.teaminology.hp.dao.IUtilDAO;


/**
 * @author sayeed
 */
public class UtilDAOImpl extends HibernateDaoSupport implements IUtilDAO
{

    private static Logger logger = Logger.getLogger(UtilDAOImpl.class);

    /**
     * To get Lookup of a class
     *
     * @param lookupClass class Object to be passed as argument to get lookup Object
     * @return List of LookUpClass Object.
     */
    @Transactional
    public <C extends ILookup> List<C> getLookup(Class<C> lookupClass) {
        Session session = getSession(true);
        return LookupCache.getInstance().getLookup(session, lookupClass);
    }

    /**
     * To Update LookUp class Object
     *
     * @param lookupClass class Object to be passed as argument to update lookup Object
     * @return List of LookUpClass Object.
     */
    @Transactional
    public <C extends ILookup> List<C> updateLookup(Class<C> lookupClass) {
        Session session = getSession(true);
        return LookupCache.getInstance().updateLookup(session, lookupClass);
    }
}
