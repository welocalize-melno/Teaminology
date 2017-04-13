package com.teaminology.hp.ImpExp.dao;

public class ImportQueries
{
    static final String LS = System.getProperty("line.separator");
    public static String GET_URL_BY_FILE_ID = "select distinct file_url from file_upload_status"
            + LS + "where file_id = ? ";
}
