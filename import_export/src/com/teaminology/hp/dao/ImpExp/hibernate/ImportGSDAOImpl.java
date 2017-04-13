package com.teaminology.hp.dao.ImpExp.hibernate;


import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.teaminology.hp.bo.Attributes;
import com.teaminology.hp.bo.Company;
import com.teaminology.hp.bo.ContentType;
import com.teaminology.hp.bo.Domain;
import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.FileUploadStatusImp;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.GsCreditials;
import com.teaminology.hp.bo.Language;
import com.teaminology.hp.bo.ProductGroup;
import com.teaminology.hp.bo.Tag;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermTranslation;


public class ImportGSDAOImpl
{
    private static Logger logger = Logger.getLogger(ImportGSDAOImpl.class);

    SessionFactory sessFact = null;

    public static ImportGSDAOImpl importDAOImpl = null;

    private ImportGSDAOImpl() {
        sessFact = HibernateSessionFactory.getSessionFactory();
    }

    public static ImportGSDAOImpl getInstance() {
        if (importDAOImpl == null) {
            importDAOImpl = new ImportGSDAOImpl();
        }
        return importDAOImpl;
    }

    public FileUploadStatusImp getFileUrlByFileId(String fileIdStr)
            throws DataAccessException {
        Session session = getSession();
        FileUploadStatusImp fileData = (FileUploadStatusImp) session.get(FileUploadStatusImp.class, Integer.parseInt(fileIdStr));
        session.close();
        return fileData;
        //	return fileData.getFileUrl();

    }

    public Integer getCompanyId(String companyLabel) {
        Company companyData;
        Session session = getSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria
                .add(Restrictions.eq("companyName", companyLabel));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        companyData = (Company) hibCriteria.uniqueResult();
        session.close();
        if (companyData != null)
            return companyData.getCompanyId();
        else
            return null;
    }

    public Integer getProductId(String productLabel) {
        Session session = getSession();
        ProductGroup product;
        Criteria hibCriteria = session.createCriteria(ProductGroup.class);
        hibCriteria
                .add(Restrictions.eq("product", productLabel));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        product = (ProductGroup) hibCriteria.uniqueResult();
        session.close();
        if (product != null)
            return product.getProductId();
        else
            return null;
    }

    public Integer getDomainId(String domainLabel) {
        Session session = getSession();
        Domain domain = new Domain();
        Criteria hibCriteria = session.createCriteria(Domain.class);
        hibCriteria
                .add(Restrictions.eq("domain", domainLabel));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        domain = (Domain) hibCriteria.uniqueResult();
        session.close();
        if (domain != null)
            return domain.getDomainId();
        else
            return null;
    }

    public Integer getLanguageId(String LangLabel) {
        Session session = getSession();
        Language lang;
        Criteria hibCriteria = session.createCriteria(Language.class);
        hibCriteria
                .add(Restrictions.eq("languageCode", LangLabel));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        lang = (Language) hibCriteria.uniqueResult();
        session.close();
        return lang.getLanguageId();
    }

    public Integer getContentTypeId(String contentTypeLabel) {
        Session session = getSession();
        ContentType contentType;
        Criteria hibCriteria = session.createCriteria(ContentType.class);
        hibCriteria
                .add(Restrictions.eq("contentType", contentTypeLabel));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        contentType = (ContentType) hibCriteria.uniqueResult();
        session.close();
        if (contentType != null)
            return contentType.getContentTypeId();
        else
            return null;

    }


    public void addCompany(Company company) {
        Session session = getSession();
        if (company != null) {
            session.save(company);
            session.close();
        }
    }

    public void addDomain(Domain domain) {
        Session session = getSession();
        if (domain != null) {
            session.save(domain);
            session.close();
        }
    }

    public void addProductGroup(ProductGroup product) {
        Session session = getSession();
        if (product != null) {
            session.save(product);
            session.close();
        }
    }

    public void addContentType(ContentType contentType) {
        Session session = getSession();
        if (contentType != null) {
            session.save(contentType);
            session.close();
        }
    }


    public void updateFileUploadStatus(FileUploadStatusImp fileData) {
        Session session = getSession();
        if (fileData != null) {
            Transaction tr = session.beginTransaction();
            session.update(fileData);
            tr.commit();
            session.close();
        }

    }

    public Session getSession() {
        Session session = sessFact.openSession();
        return session;
    }

    public Integer saveFile(FileInfo fileInfo) {
        Session session = getSession();
        Integer fileInfoId = null;
        if (fileInfo == null) {
            return null;
        }
        fileInfoId = (Integer) session.save(fileInfo);

        return fileInfoId;
    }

    public FileInfo getFileInfo(Integer fileId) {
        Session session = getSession();
        FileInfo fileInfo = null;
        if (fileId == null) {
            return null;
        }
        fileInfo = (FileInfo) session.get(FileInfo.class, fileId);

        return fileInfo;
    }

    public void updateFileInfo(FileInfo fileInfo) {
        Session session = getSession();
        Transaction tr = session.beginTransaction();

        if (fileInfo == null) {
            return;
        }
        session.saveOrUpdate(fileInfo);
        tr.commit();
    }

    public Company getCompanyById(Integer companyId) {
        if (companyId == null)
            return null;
        Session session = getSession();
        Company compData = (Company) session.get(Company.class, companyId);
        session.close();
        return compData;
    }

    public Company getCompanyByLable(String companyName) {
        if (companyName == null)
            return null;
        Session session = getSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria
                .add(Restrictions.eq("companyName", companyName));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        Company company = (Company) hibCriteria.uniqueResult();
        return company;
    }

    public String addNewTerm(TermInformation termInformation) {
        Session session = getSession();
        String status = "";
        boolean sourceExits = false;
        if (termInformation == null) {
            status = "failed";
            return status;
        }
        String sourceTerm = termInformation.getTermBeingPolled();
        String targetTerm = termInformation.getSuggestedTerm();

        if (sourceTerm != null)
            termInformation.setTermBeingPolled(sourceTerm.trim());
        if (targetTerm != null)
            termInformation.setSuggestedTerm(targetTerm.trim());

        termInformation.setTermStatusId(1);
        termInformation.setIsActive("Y");
        if (sourceTerm != null
                && termInformation.getSuggestedTermLangId() != null) {
            Integer companyId = null;
            if (termInformation.getTermCompany() != null) {
                companyId = termInformation.getTermCompany().getCompanyId();
            }
            //sourceExits=ifSourceExists(sourceTerm,termInformation.getSuggestedTermLangId(),companyId);
            sourceExits = false;
        }
        if (!sourceExits) {
            Integer termId = (Integer) session.save(termInformation);

            if (termId != null && termId != 0
                    && termInformation.getSuggestedTerm() != null) {
                TermTranslation newSuggestedTerm = new TermTranslation();
                newSuggestedTerm.setTermId(termId);
                newSuggestedTerm.setSuggestedTerm(termInformation
                        .getSuggestedTerm());
                newSuggestedTerm.setCreateDate(termInformation.getCreateDate());
                newSuggestedTerm.setUserId(termInformation.getCreatedBy());
                newSuggestedTerm.setComment(termInformation.getComments());
                newSuggestedTerm.setSuggestedTermLangId(termInformation
                        .getSuggestedTermLangId());
                session.save(newSuggestedTerm);
            }
            status = "success";
            logger.debug("Successfully added new term");
        } else {
            status = "failedterm";
            logger.debug(" Term Already Exists");
        }

        return status;
    }

    private boolean ifSourceExists(String sourceTerm,
                                   Integer suggestedTermLangId, Integer companyId) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(TermInformation.class);
        criteria.add(Restrictions.eq("termBeingPolled", sourceTerm));
        criteria.add(Restrictions.eq("suggestedTermLangId", suggestedTermLangId));
        criteria.add(Restrictions.eq("isTM", "Y"));
        if (companyId != null) {
            criteria.add(Restrictions.eq("termCompany.companyId", companyId));
        }
        criteria.add(Restrictions.eq("isActive", "Y"));
        criteria.setProjection(Projections.count("termId"));
        Integer countOfTerms = (Integer) criteria.uniqueResult();

        if (countOfTerms > 0) {
            return true;
        }

        return false;
    }

    public Integer saveGlobalSightTerm(GlobalsightTermInfo globalSightTermInfo) {
        Session session = getSession();
        Transaction tr = session.beginTransaction();
        Integer termId = null;
        if (globalSightTermInfo == null) {
            return null;
        }
        termId = (Integer) session.save(globalSightTermInfo);
        tr.commit();
        session.close();
        return termId;
    }

    public Integer saveTag(Tag tag) {
        Session session = getSession();
        Transaction tr = session.beginTransaction();
        Integer termId = null;
        if (tag == null) {
            return null;
        }
        termId = (Integer) session.save(tag);
        tr.commit();
        session.close();
        return termId;
    }

    public Integer saveAttribute(Attributes attributes) {
        Session session = getSession();
        Transaction tr = session.beginTransaction();
        Integer attrId = null;
        if (attributes == null) {
            return null;
        }
        attrId = (Integer) session.save(attributes);
        tr.commit();
        session.close();
        return attrId;
    }

    public GsCreditials getGSCredentialsByCompany(Integer comanyId) {
        Session session = getSession();
        GsCreditials gsCreditials = (GsCreditials) session.get(GsCreditials.class, comanyId);
        session.close();
        return gsCreditials;
    }

}
