package com.teaminology.hp.dao;

import java.util.List;

import com.teaminology.hp.bo.lookup.ILookup;

/**
 * Contains DAO Interface methods to handle Look up class object
 *
 * @author sushmab
 */
public interface IUtilDAO
{

    /**
     * To get Lookup of a class
     *
     * @param lookupClass class Object to be passed as argument to get lookup Object
     * @return List of LookUpClass Object.
     */
    public <C extends ILookup> List<C> getLookup(Class<C> lookupClass);

    /**
     * To Update LookUp class Object
     *
     * @param lookupClass class Object to be passed as argument to update lookup Object
     * @return List of LookUpClass Object.
     */
    public <C extends ILookup> List<C> updateLookup(Class<C> lookupClass);


}
