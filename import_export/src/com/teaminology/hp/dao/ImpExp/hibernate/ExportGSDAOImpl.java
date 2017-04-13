package com.teaminology.hp.dao.ImpExp.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.teaminology.hp.bo.Attributes;
import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.FileUploadStatusImp;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.Language;
import com.teaminology.hp.bo.Tag;


public class ExportGSDAOImpl
{
    private static Logger logger = Logger.getLogger(ExportGSDAOImpl.class);
    static SessionFactory sessFact = null;
    static Session session = null;
    Transaction tr = null;

    public static ExportGSDAOImpl exportDAOImpl = null;

    private ExportGSDAOImpl() {
        sessFact = HibernateSessionFactory.getSessionFactory();
        session = sessFact.openSession();
    }

    public static ExportGSDAOImpl getInstance() {
        if (exportDAOImpl == null) {
            exportDAOImpl = new ExportGSDAOImpl();
        }
        return exportDAOImpl;
    }

    public void updateFileUploadStatus(FileUploadStatusImp fileData) {
        if (fileData != null) {
            tr = session.beginTransaction();
            session.update(fileData);
            tr.commit();
        }

    }

    public FileUploadStatusImp saveFileUploadStatus(FileUploadStatusImp fileUpload) {
        if (fileUpload != null) {
            tr = session.beginTransaction();
            fileUpload = (FileUploadStatusImp) session.save(fileUpload);
            tr.commit();
        }
        return fileUpload;

    }

    public FileUploadStatusImp getFileUploadStatus(String fileIdStr)
            throws DataAccessException {

        FileUploadStatusImp fileData = (FileUploadStatusImp) session.get(FileUploadStatusImp.class, Integer.parseInt(fileIdStr));
        return fileData;

    }

    @SuppressWarnings("unchecked")
    public List<GlobalsightTermInfo> gettermsUsingPageId(Integer pageId) {
        List<GlobalsightTermInfo> globalsightTermInfoList = null;
        Session session = getSession();
        globalsightTermInfoList = new ArrayList<GlobalsightTermInfo>();
        Criteria crit = session.createCriteria(GlobalsightTermInfo.class).add(
                Restrictions.and(
                        Restrictions.eq("fileInfo.fileInfoId", pageId),
                        Restrictions.not(Restrictions.eq("isActive", "N"))));
        globalsightTermInfoList = (List<GlobalsightTermInfo>) crit.list();

        logger.debug("Got the globalsightTermInfoList :" + globalsightTermInfoList);
        return globalsightTermInfoList;
    }

    public FileInfo getFileInfo(Integer pageId) {
        Session session = getSession();
        return (FileInfo) session.get(FileInfo.class, pageId);
    }

    public void updateFileStatus(FileInfo fileInfo) {
        Session session = getSession();
        if (fileInfo != null) {
            tr = session.beginTransaction();
            session.update(fileInfo);
            tr.commit();
            session.close();
        }

    }

    public String getLanguageName(Integer langId) {
        Session session = getSession();
        Language lang;
        Criteria hibCriteria = session.createCriteria(Language.class);
        hibCriteria
                .add(Restrictions.eq("languageId", langId));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        lang = (Language) hibCriteria.uniqueResult();
        session.close();
        return lang.getLanguageCode();

    }

    private Session getSession() {
        Session session = sessFact.openSession();
        return session;
    }

    @SuppressWarnings("unchecked")
    public List<Tag> getTagsByGSId(Integer globalsightTermInfoId) {
        Session session = getSession();
        Criteria hibCriteria = session.createCriteria(Tag.class);
        hibCriteria
                .add(Restrictions.eq("globalsightTermInfo.globalsightTermInfoId", globalsightTermInfoId));
        hibCriteria.addOrder(Order.asc("sortOrder"));

        return (List<Tag>) hibCriteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Attributes> getAttributesByTagId(Integer tagId) {
        Session session = getSession();
        Criteria hibCriteria = session.createCriteria(Attributes.class);
        hibCriteria
                .add(Restrictions.eq("tag.tagId", tagId));
        return (List<Attributes>) hibCriteria.list();
    }

}
