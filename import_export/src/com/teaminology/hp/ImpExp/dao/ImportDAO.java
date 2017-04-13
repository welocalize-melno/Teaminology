package com.teaminology.hp.ImpExp.dao;

import org.springframework.dao.DataAccessException;


public interface ImportDAO
{

    //public File getFileNameByFileId(String fileIdStr);

    public String getFileUrlByFileId(String fileIdStr) throws DataAccessException;


}
