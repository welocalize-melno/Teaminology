package com.teaminology.hp.ImpExp.dao;

import java.util.List;

import com.teaminology.hp.bo.lookup.ILookup;

public interface IUtilDAO
{
    public <C extends ILookup> List<C> getLookup(Class<C> lookupClass);

    public <C extends ILookup> List<C> updateLookup(Class<C> lookupClass);


}
