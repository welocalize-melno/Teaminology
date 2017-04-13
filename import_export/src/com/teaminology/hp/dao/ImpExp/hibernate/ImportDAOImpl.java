package com.teaminology.hp.dao.ImpExp.hibernate;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.teaminology.hp.bo.Company;
import com.teaminology.hp.bo.ContentType;
import com.teaminology.hp.bo.Domain;
import com.teaminology.hp.bo.FileUploadStatusImp;
import com.teaminology.hp.bo.Language;
import com.teaminology.hp.bo.ProductGroup;
import com.teaminology.hp.bo.TMProperties;
import com.teaminology.hp.bo.TempTmProfileBo;
import com.teaminology.hp.bo.TmProfileBo;


public class ImportDAOImpl
{
    private static Logger logger = Logger.getLogger(ImportDAOImpl.class);

    SessionFactory sessFact = null;

    public static ImportDAOImpl importDAOImpl = null;

    private ImportDAOImpl() {
        sessFact = HibernateSessionFactory.getSessionFactory();
    }

    public static ImportDAOImpl getInstance() {
        if (importDAOImpl == null) {
            importDAOImpl = new ImportDAOImpl();
        }
        return importDAOImpl;
    }

    public FileUploadStatusImp getFileUrlByFileId(String fileIdStr, Session session)
            throws DataAccessException {
        FileUploadStatusImp fileData = (FileUploadStatusImp) session.get(FileUploadStatusImp.class, Integer.parseInt(fileIdStr));
        return fileData;

    }

    public Integer getCompanyId(String companyLabel, Session session) {
        if (companyLabel == null || companyLabel.trim().length() == 0) {
            return null;
        }

        Integer companyId;
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria
                .add(Restrictions.eq("companyName", companyLabel));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.setProjection(Projections.property("companyId"));

        companyId = (Integer) hibCriteria.uniqueResult();
        return companyId;
    }

    public Integer getProductId(String productLabel, Session session) {
        if (productLabel == null || productLabel.trim().length() == 0) {
            return null;
        }
        Integer productId;
        Criteria hibCriteria = session.createCriteria(ProductGroup.class);
        hibCriteria
                .add(Restrictions.eq("product", productLabel));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.setProjection(Projections.property("productId"));
        productId = (Integer) hibCriteria.uniqueResult();
        return productId;
    }

    public Integer getDomainId(String domainLabel, Session session) {
        if (domainLabel == null || domainLabel.trim().length() == 0) {
            return null;
        }

        Integer domainId;

        Criteria hibCriteria = session.createCriteria(Domain.class);
        hibCriteria
                .add(Restrictions.eq("domain", domainLabel));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.setProjection(Projections.property("domainId"));
        domainId = (Integer) hibCriteria.uniqueResult();
        return domainId;
    }

    public Integer getLanguageId(String LangLabel, Session session) {
        Language lang;
        Criteria hibCriteria = session.createCriteria(Language.class);
        hibCriteria
                .add(Restrictions.eq("languageCode", LangLabel));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        lang = (Language) hibCriteria.uniqueResult();
        return lang.getLanguageId();
    }

    public Integer getContentTypeId(String contentTypeLabel, Session session) {
        if (contentTypeLabel == null || contentTypeLabel.trim().length() == 0) {
            return null;
        }

        Integer contentTypeId;
        Criteria hibCriteria = session.createCriteria(ContentType.class);
        hibCriteria
                .add(Restrictions.eq("contentType", contentTypeLabel));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.setProjection(Projections.property("contentTypeId"));
        contentTypeId = (Integer) hibCriteria.uniqueResult();
        return contentTypeId;

    }

    public void saveTmproperties(TMProperties tmProperties, Session session)
            throws DataAccessException {
        if (tmProperties != null)
            session.save(tmProperties);
    }

    public void addCompany(Company company, Session session) {
        if (company != null) {
            session.save(company);
        }
    }

    public void addDomain(Domain domain, Session session) {
        if (domain != null) {
            session.save(domain);
        }
    }

    public void addProductGroup(ProductGroup product, Session session) {
        if (product != null) {
            session.save(product);
        }
    }

    public void addContentType(ContentType contentType, Session session) {
        if (contentType != null) {
            session.save(contentType);
        }
    }

    @SuppressWarnings("unchecked")
    public TmProfileBo isTargetTMExists(TmProfileBo tmProfile, Session session) {
        Criteria criteria = session.createCriteria(TmProfileBo.class);
        criteria.add(Restrictions.eq("source", tmProfile.getSource()));
        criteria.add(Restrictions.eq("target", tmProfile.getTarget()));
        criteria.add(Restrictions.eq("targetLang", tmProfile.getTargetLang()));
        if (tmProfile.getCompanyId() != null) {
            criteria.add(Restrictions.eq("companyId", tmProfile.getCompanyId()));
        }
        if (tmProfile.getProductGroupId() != null) {
            criteria.add(Restrictions.eq("productGroupId", tmProfile.getProductGroupId()));
        }
        if (tmProfile.getDomainId() != null) {
            criteria.add(Restrictions.eq("domainId", tmProfile.getDomainId()));
        }
        if (tmProfile.getContentTypeId() != null) {
            criteria.add(Restrictions.eq("contentTypeId", tmProfile.getContentTypeId()));
        }
        criteria.add(Restrictions.eq("isActive", "Y"));
        TmProfileBo tmProfileBoList = (TmProfileBo) criteria.uniqueResult();
        return tmProfileBoList;
    }


    @SuppressWarnings("unchecked")
    public boolean isSourceTMExists(TmProfileBo tmProfile, Session session) {
        Criteria criteria = session.createCriteria(TmProfileBo.class);
        criteria.add(Restrictions.eq("source", tmProfile.getSource()));
        if (tmProfile.getCompanyId() != null) {
            criteria.add(Restrictions.eq("companyId", tmProfile.getCompanyId()));
        }
        criteria.add(Restrictions.eq("isActive", "Y"));
        criteria.setProjection(Projections.count("tmProfileInfoId"));
        Integer countOfTerms = (Integer) criteria.uniqueResult();

        if (countOfTerms > 0) {
            return true;
        }

        return false;
    }


    @SuppressWarnings("unchecked")
    public TmProfileBo isAttributesTMExists(TmProfileBo tmProfile, Session session) {
        Criteria criteria = session.createCriteria(TmProfileBo.class);
        criteria.add(Restrictions.eq("source", tmProfile.getSource()));
        criteria.add(Restrictions.eq("targetLang", tmProfile.getTargetLang()));
        if (tmProfile.getCompanyId() != null) {
            criteria.add(Restrictions.eq("companyId", tmProfile.getCompanyId()));
        }
        if (tmProfile.getProductGroupId() != null) {
            criteria.add(Restrictions.eq("productGroupId", tmProfile.getProductGroupId()));
        }
        if (tmProfile.getDomainId() != null) {
            criteria.add(Restrictions.eq("domainId", tmProfile.getDomainId()));
        }
        if (tmProfile.getContentTypeId() != null) {
            criteria.add(Restrictions.eq("contentTypeId", tmProfile.getContentTypeId()));
        }
        criteria.add(Restrictions.eq("isActive", "Y"));
        TmProfileBo tmProfileBo = (TmProfileBo) criteria.uniqueResult();
        return tmProfileBo;
    }


    public void deleteTmProperties(List<Integer> tmPropertiesDeleteList, Session session) {

        String tmpProfileIds = "";
        for (Integer tmProfileId : tmPropertiesDeleteList) {
            tmpProfileIds = tmpProfileIds + tmProfileId + ",";
        }
        if (tmpProfileIds.contains(",")) {
            tmpProfileIds = tmpProfileIds.substring(0, tmpProfileIds.lastIndexOf(","));
        }
        String queryString = "DELETE FROM com.teaminology.hp.bo.TMProperties tmp where tmp.tmProfileId ";
        queryString = queryString + " in (" + tmpProfileIds + ")";
        Query query = session.createQuery(queryString);
        int rows = query.executeUpdate();
        logger.debug("Successfully deleted " + rows + " rows");
    }


    public Integer saveTmProfileInfo(TempTmProfileBo tmInformation, Session session) {
        Integer tmProfileId = null;
        if (tmInformation != null) {
            tmProfileId = (Integer) session.save(tmInformation);
        }
        return tmProfileId;
    }

    public void updateTmProfileInfo(TmProfileBo tmProfileBo, Session session) {

        if (tmProfileBo != null) {
            session.update(tmProfileBo);

        }

    }

    public void updateFileUploadStatus(FileUploadStatusImp fileData, Session session) {
        if (fileData != null) {
            session.update(fileData);
        }

    }

    public Session getSession() {
        Session session = sessFact.openSession();
        return session;
    }

    public void deleteTempTmData(String fileId, Session session) {
        if (fileId != null) {
            java.sql.Connection con = session.connection();
            try {
                CallableStatement cstmt = con.prepareCall("{ call  deleteTempTmProfilesByFileId (?)  }");
                cstmt.setString(1, fileId);
                cstmt.execute();
            }
            catch (SQLException e) {

                e.printStackTrace();
            }
        }

    }


}
