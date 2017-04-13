package com.teaminology.hp.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import com.teaminology.hp.Utils;
import com.teaminology.hp.service.enums.ExecuteStatusEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.EmailTemplates;
import com.teaminology.hp.bo.Role;
import com.teaminology.hp.bo.lookup.Category;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.ConceptCategory;
import com.teaminology.hp.bo.lookup.ContentType;
import com.teaminology.hp.bo.lookup.Domain;
import com.teaminology.hp.bo.lookup.Form;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.bo.lookup.PartsOfSpeech;
import com.teaminology.hp.bo.lookup.ProductGroup;
import com.teaminology.hp.bo.lookup.Program;
import com.teaminology.hp.bo.lookup.Status;
import com.teaminology.hp.bo.lookup.VoteConfig;
import com.teaminology.hp.dao.ILookUpDAO;
import com.teaminology.hp.dao.IUtilDAO;
import com.teaminology.hp.service.ILookUpService;
import com.teaminology.hp.service.enums.TeaminologyProperty;

/**
 * Used to get the data from the lookup tables.
 *
 * @author sarvanic
 */
@Service
public class LookUpServiceImpl implements ILookUpService
{
    private Logger logger = Logger.getLogger(LookUpServiceImpl.class);

    private ILookUpDAO lookUpDAO;
    private IUtilDAO utilDAO;

    @Autowired
    public LookUpServiceImpl(ILookUpDAO lookUpDAO, IUtilDAO utilDAO) {
        this.lookUpDAO = lookUpDAO;
        this.utilDAO = utilDAO;
    }

    @Transactional(readOnly = true)
    @Qualifier("txManager")
    /**
     * To get language lookup data
     * @return List of Language object's
     */
    public List<Language> getLanguages() {
        try {
            return lookUpDAO.getLanguages();
        }
        catch (Exception e) {
            logger.error("Error in getting Languages Data.", e);
        }
        return null;
    }

    /**
     * To get Parts of Speech lookup data
     *
     * @return List of PartsOfSpeech object's
     */
    @Override
    public List<PartsOfSpeech> getPOSLookUp() {
        try {
            return lookUpDAO.getPartsOfSpeechLookUp();
        }
        catch (Exception e) {
            logger.error("Error in getting PartsOfSpeech Data.", e);
        }
        return null;
    }

    /**
     * To get Form lookup data
     *
     * @return List of Form object's
     */
    @Override
    public List<Form> getFormLookUp() {
        try {
            return lookUpDAO.getFormLookUp();
        }
        catch (Exception e) {
            logger.error("Error in getting form data.", e);
        }
        return null;
    }

    /**
     * To get Program lookup data
     *
     * @return List of Program object's
     */
    @Override
    public List<Program> getProgramLookUp() {
        try {
            return lookUpDAO.getProgramLookUp();
        }
        catch (Exception e) {
            logger.error("Error in getting program data.", e);
        }
        return null;
    }

    /**
     * To get PartsOfSpeech for a part of speech id
     *
     * @param POSId Integer to get PartsOfSpeech
     * @return PartsOfSpeech w.r.t the part of speech id
     */
    @Override
    public PartsOfSpeech getPartsOfSpeech(Integer POSId) {
        if (POSId == null || POSId.intValue() == 0)
            return null;

        try {
            return lookUpDAO.getPartsOfSpeech(POSId);
        }
        catch (Exception e) {
            logger.error("Error in getting parts of speech data.", e);
        }
        return null;
    }

    /**
     * To get Program for a program id
     *
     * @param programId Integer to get Program
     * @return Program w.r.t the programId
     */
    @Override
    public Program getProgram(Integer programId) {
        if (programId == null || programId.intValue() == 0)
            return null;

        try {
            return lookUpDAO.getProgram(programId);
        }
        catch (Exception e) {
            logger.error("Error in getting Program data", e);
        }
        return null;
    }

    /**
     * To get Form for a form id
     *
     * @param formId Integer to get Form
     * @return Form w.r.t the formId
     */
    @Override
    public Form getForm(Integer formId) {
        if (formId == null || formId.intValue() == 0)
            return null;

        try {
            return lookUpDAO.getForm(formId);
        }
        catch (Exception e) {
            logger.error("Error in getting form data", e);
        }
        return null;
    }

    /**
     * To get language for a language id
     *
     * @param langId Integer to get language
     * @return Language w.r.t the langId
     */
    @Override
    public Language getLanguage(Integer langId) {
        if (langId == null || langId.intValue() == 0)
            return null;

        try {
            return lookUpDAO.getLanguage(langId);
        }
        catch (Exception e) {
            logger.error("Error in grtting the languages data", e);
        }
        return null;
    }

    /**
     * To get user role  for a role id
     *
     * @param roleId Integer to get user role
     * @return role w.r.t the roleId
     */
    @Override
    public Role getUserRole(Integer roleId) {
        if (roleId == null || roleId.intValue() == 0)
            return null;

        try {
            return lookUpDAO.getUserRole(roleId);
        }
        catch (Exception e) {
            logger.error("Error in getting the user role data", e);
        }
        return null;
    }

    /**
     * To get concept category data
     *
     * @return List of ConceptCategory object's
     */

    @Override
    public List<ConceptCategory> getConceptCategoryLookUp() {
        try {
            return lookUpDAO.getConceptCategoryLookUp();
        }
        catch (Exception e) {
            logger.error("Error in getting concept category data", e);
        }
        return null;
    }

    /**
     * To get category data
     *
     * @return List of Category object's
     */

    @Override
    public List<Category> getCategoryLookUp() {
        try {
            return lookUpDAO.getCategoryLookUp();
        }
        catch (Exception e) {
            logger.error("Error in getting category data", e);
        }
        return null;
    }

    /**
     * To get product group data
     *
     * @return List of ProductGroup object's
     */

    @Override
    public List<ProductGroup> getProductGroupLookUp() {
        try {
            return lookUpDAO.getProductGroupLookUp();
        }
        catch (Exception e) {
            logger.error("Error in getting product group data", e);
        }
        return null;
    }

    /**
     * To get status data
     *
     * @return List of Status object's
     */

    @Override
    public List<Status> getStatusLookUp() {
        try {
            return lookUpDAO.getStatusLookUp();
        }
        catch (Exception e) {
            logger.error("Error in getting status information", e);
        }
        return null;
    }

    /**
     * To get Domain lookup data
     *
     * @return List of Domain object's
     */
    @Override
    public List<Domain> getDomainLookUp() {
        try {
            return lookUpDAO.getDomainLookUp();
        }
        catch (Exception e) {
            logger.error("Error in getting Domain data", e);
        }
        return null;
    }

    /**
     * To get Category from the categoryId
     *
     * @param categoryId Integer to get Category
     * @return Category w.r.t the categoryId
     */
    @Override
    public Category getCategory(Integer categoryId) {
        if (categoryId == null || categoryId.intValue() == 0)
            return null;

        try {
            return lookUpDAO.getCategory(categoryId);
        }
        catch (Exception e) {
            logger.error("Error in getting category data", e);
        }
        return null;
    }

    /**
     * To get email template for emailTemplateId
     *
     * @param emailTemplateId Integer to get EmailTemplates
     * @return EmailTemplates w.r.t the emailTemplateId
     */
    @Override
    public EmailTemplates getEmailTemplate(Integer emailTemplateId) {
        if (emailTemplateId == null || emailTemplateId.intValue() == 0)
            return null;

        try {
            return lookUpDAO.getEmailTemplate(emailTemplateId);
        }
        catch (Exception e) {
            logger.error("Error in getting email template data", e);
        }
        return null;
    }

    /**
     * Domain  create/update/delete(CUD) operations
     *
     * @param domain Domain object to be CUD
     * @param userId Integer for transactions
     * @return status  w.r.t to the operation
     */
    @Override
    public String setDomainCUD(Domain domain, Integer userId) {
        if (domain == null || userId == null || userId.intValue() == 0)
            return ExecuteStatusEnum.FAILED.getValue();

        try {
            return lookUpDAO.setDomainCUD(domain, userId);
        }
        catch (Exception e) {
            logger.error("Error in setting Domain data", e);
        }
        return ExecuteStatusEnum.FAILED.getValue();
    }

    /**
     * Language create/update/delete(CUD) operations
     *
     * @param language Language object to be CUD
     * @param userId   Integer for transactions
     * @return status w.r.t the operations
     */
    @Override
    public String setLanguageCUD(Language language, Integer userId) {
        if (language == null || userId == null || userId.intValue() == 0)
            return ExecuteStatusEnum.FAILED.getValue();

        try {
            return lookUpDAO.setLanguageCUD(language, userId);
        }
        catch (Exception e) {
            logger.error("Error in setting languages", e);
        }
        return ExecuteStatusEnum.FAILED.getValue();
    }

    /**
     * Email template create/update/delete(CUD) operations
     *
     * @param emailTemplate EmailTemplate object to get email template data
     * @param userId        Integer for transactions
     * @return status w.r.t the operations
     */
    @Override

    public String setEmailTemplateCUD(EmailTemplates emailTemplate, Integer userId) {
        if (emailTemplate == null || userId == null || userId.intValue() == 0)
            return ExecuteStatusEnum.FAILED.getValue();

        try {
            return lookUpDAO.setEmailTemplateCUD(emailTemplate, userId);
        }
        catch (Exception e) {
            logger.error("Error in setting email template data", e);
        }
        return ExecuteStatusEnum.FAILED.getValue();
    }


    /**
     * To get vote config
     *
     * @return VoteConfig lookup
     */
    @Override
    public VoteConfig getVoteConfig() {
        try {
            return lookUpDAO.getVoteConfig();
        }
        catch (Exception e) {
            logger.error("Error in vote config data", e);
        }
        return null;
    }

    /**
     * VoteConfig update/delete(UD) operations
     *
     * @param voteConfig VoteConfig object to UD
     * @param userId     Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     */
    @Override
    public String setVoteConfigUD(VoteConfig voteConfig, Integer userId) {
        if (voteConfig == null || userId == null || userId.intValue() == 0)
            return ExecuteStatusEnum.FAILED.getValue();

        try {
            return lookUpDAO.setVoteConfigUD(voteConfig, userId);
        }
        catch (Exception e) {
            logger.error("Error in vote config data", e);
        }
        return ExecuteStatusEnum.FAILED.getValue();
    }

    /**
     * To get  sorted languages  data
     *
     * @param colName   Column name that has to be sorted
     * @param sortOrder Order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of Language object's
     */
    public List<Language> getSortedLanguages(String colName, String sortOrder, int pageNum) {
        try {
            return lookUpDAO.getSortedLanguages(colName, sortOrder, pageNum);
        }
        catch (Exception e) {
            logger.error("Error in getting languages data", e);
        }
        return null;
    }

    /**
     * To verify whether languageLabel exists or not in database
     *
     * @return Returns true if languagelabel exists else it returns false
     */
    @Override
    public boolean isLanguageLabelExists(String languageLabel) {
        if (Utils.isEmpty(languageLabel))
            return false;

        try {
            return lookUpDAO.isLanguageLabelExists(languageLabel);
        }
        catch (Exception e) {
            logger.error("Error found in isLanguageLabelExists().", e);
        }
        return false;
    }

    /**
     * To verify whether languageCode exists or not in database
     *
     * @return Returns true if languageCode exists else it returns false
     */
    @Override
    public boolean isLanguageCodeExists(String languageCode) {
        if (Utils.isEmpty(languageCode))
            return false;

        try {
            return lookUpDAO.isLanguageCodeExists(languageCode);
        }
        catch (Exception e) {
            logger.error("Error found in isLanguageCodeExists().", e);
        }
        return false;
    }

    /**
     * To get Language by label data for languageName
     *
     * @param languageName String to get language
     * @return Language w.r.t the language name
     */
    @Override
    public Language getLanguageByLabel(String languageName) {
        if (Utils.isEmpty(languageName))
            return null;

        try {
            return lookUpDAO.getLanguageByLabel(languageName);
        }
        catch (Exception e) {
            logger.error("Error in language data", e);
        }
        return null;
    }

    /**
     * Category  create/update/delete(CUD) operations
     *
     * @param category Category object to be CUD
     * @param userId   Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     */
    @Override
    public String setCategoryCUD(Category category, Integer userId) {
        if (category == null || userId == null || userId.intValue() == 0)
            return ExecuteStatusEnum.FAILED.getValue();

        try {
            return lookUpDAO.setCategoryCUD(category, userId);
        }
        catch (Exception e) {
            logger.error("Error in updating Category", e);
        }
        return null;
    }


    /**
     * To verify whether domain exists or not in database
     *
     * @return Returns true if domain exists else it returns false
     */
    @Override
    public boolean isDomainExists(String domain) {
        if (Utils.isEmpty(domain))
            return false;

        try {
            return lookUpDAO.isDomainExists(domain);
        }
        catch (Exception e) {
            logger.error("Error found in isDomainExists().", e);
        }
        return false;
    }


    /**
     * To verify whether category exists or not in database
     *
     * @return Returns true if category exists else it returns false
     */    
    @Override
    @Transactional
    public boolean isCategoryExists(String category) {
        if (Utils.isEmpty(category))
            return false;

        try {
            return lookUpDAO.isCategoryExists(category);
        }
        catch (Exception e) {
            logger.error("Error found in isCategoryExists().", e);
        }
        return false;
    }

    /**
     * To get Domain for a program id
     *
     * @param domainId Integer to get Domain
     * @return Domain w.r.t the domainId
     */
    @Override
    public Domain getDomainById(Integer domainId) {
        if (domainId == null || domainId.intValue() < 1)
            return null;

        try {
            return lookUpDAO.getDomain(domainId);
        }
        catch (Exception e) {
            logger.error("Error in getting domain data", e);
        }
        return null;
    }

    /**
     * To get product group  for a product id
     *
     * @param productId Integer to get ProductGroup
     * @return ProductGroup w.r.t the productId
     */
    @Override
    public ProductGroup getProductGroupById(Integer productId) {
        if (productId == null || productId.intValue() < 1)
            return null;

        try {
            return lookUpDAO.getProductGroup(productId);
        }
        catch (Exception e) {
            logger.error("Error in getting product data", e);
        }
        return null;
    }

    /**
     * To get content type for a contentTypeID
     *
     * @param contentTypeID Integer to get ContentType
     * @return ContentType w.r.t the contentTypeID
     */
    @Override
    public ContentType getContentTypeById(Integer contentTypeID) {
        if (contentTypeID == null || contentTypeID.intValue() < 1)
            return null;

        try {
            return lookUpDAO.getContentType(contentTypeID);
        }
        catch (Exception e) {
            logger.error("Error in getting content type data", e);
        }
        return null;
    }

    /**
     * To get content type lookup data
     *
     * @return List of ContentType object's
     */
    @Override
    public List<ContentType> getContentType() {
        try {
            return lookUpDAO.getContentType();
        }
        catch (Exception e) {
            logger.error("Error in getting content type list.", e);
        }
        return null;
    }

    /**
     * To get Company lookup data
     *
     * @return List of Company object's
     */
    @Override
    public List<Company> getCompanyLookUp() {
        try {
            return lookUpDAO.getCompanyLookUp();
        }
        catch (Exception e) {
            logger.error("Error in getting Company  Data", e);
        }
        return null;
    }


    /**
     * Add new company.
     *
     * @param company RequestBody Company that needs to be added
     * @return If company  is added it returns success else failed
     */

    public String addNewCompany(final Company company) {
        if (company == null)
            return ExecuteStatusEnum.FAILED.getValue();

        String status = ExecuteStatusEnum.FAILED.getValue();

        try {
            status = lookUpDAO.addCompany(company);
            if (ExecuteStatusEnum.SUCCESS.getValue().equalsIgnoreCase(status))
                utilDAO.updateLookup(Company.class);
        }
        catch (Exception e) {
            logger.error("Error in  adding  new company ", e);
        }
        return status;
    }


    /**
     * To get company details
     *
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of companies
     */
    @Override

    public List<Company> getCompanyDetails(String colName, String sortOrder, Integer pageNum) {
        try {
            return lookUpDAO.getSortedCompanys(colName, sortOrder, pageNum);
        }
        catch (Exception e) {
            logger.error("Error in getting Company  Data", e);
        }
        return null;
    }

    /**
     * To get Total Active companies count
     *
     * @return Integer which holds the count of active companiess
     */
    @Override
    public int getActiveCompaniesCount() {
        try {
            return lookUpDAO.getActiveCompaniesCount();
        }
        catch (Exception e) {
            logger.error("Error in getting  active companies count", e);
        }

        return 0;
    }

    /**
     * To update company.
     *
     * @param company RequestBody Company that needs to be updated
     * @return If company  is updated it returns success else failed
     */

    public String updateCompany(Company company) {
        if (company == null)
            return ExecuteStatusEnum.FAILED.getValue();

        String status = ExecuteStatusEnum.FAILED.getValue();
        try {
            status = lookUpDAO.updateCompany(company);
            if (ExecuteStatusEnum.SUCCESS.getValue().equalsIgnoreCase(status))
                utilDAO.updateLookup(Company.class);
        }
        catch (Exception e) {
            logger.error("Error in updating company ", e);
        }
        return status;
    }

    /**
     * To delete companies
     *
     * @param companyIds array of integer companyIds that needs to be deleted
     */
    @Override
    public void deleteCompanies(final Integer[] companyIds, final Integer userId) {
        if (companyIds == null || companyIds.length == 0)
            return;

        try {
            lookUpDAO.deleteCompanies(companyIds, userId);
            utilDAO.updateLookup(Company.class);
        }
        catch (Exception e) {
            logger.error("Error in deleting company", e);
        }
    }

    /**
     * To verify whether company exists or not in database
     *
     * @param companyName String to get company
     * @return Returns true if company exists else it returns false
     */
    @Override
    public boolean isCompanyExists(String companyName) {
        if (Utils.isEmpty(companyName))
            return false;

        try {
            return lookUpDAO.isCompanyExists(companyName);
        }
        catch (Exception e) {
            logger.error("Error found in isCompanyExists().", e);
        }
        return false;
    }

    /**
     * To get company for a company id
     *
     * @param companyId Integer to get company
     * @return Company w.r.t the companyId
     */
    @Override
    public Company getCompanyById(Integer companyId) {
        if (companyId == null || companyId.intValue() < 1)
            return null;

        try {
            return lookUpDAO.getCompany(companyId);
        }
        catch (Exception e) {
            logger.error("Error in getting company data", e);
        }
        return null;
    }

    /**
     * To get company by context root
     *
     * @param contextRoot String  to get company details
     * @return Company w.r.t the contextRoot
     */
    @Override
    public Company getCompanyByContextRoot(String contextRoot) {
        if (Utils.isEmpty(contextRoot))
            return null;

        return lookUpDAO.getCompanyByContextRoot(contextRoot);
    }

    /**
     * To create company context
     *
     * @param company Company  that has to be created
     * @param An      integer for which data is to be retrieved
     */
    @Override
    public void createCompanyContext(Company company, Integer userId) {
        if (company == null || userId == null || userId.intValue() < 1)
            return;

        try {
            File source = new File(TeaminologyProperty.JSP_FILE_LOCATION.getValue());
            File destination = new File(TeaminologyProperty.COMPANY_DIR_LOCATION.getValue() + company.getContextRoot());
            if (!destination.exists()) {
                destination.mkdirs();
                destination.setReadable(true);
                destination.setWritable(true);
            }

            copyFile(source, destination, TeaminologyProperty.COMPANY_DIR_LOCATION.getValue() + company.getContextRoot());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To copy the file
     *
     * @param source,dest,destnationDir
     */
    public void copyFile(File source, File dest, String destnationDir) throws IOException {
        if (source == null || dest == null || Utils.isEmpty(destnationDir))
            return;

        File destination = new File(destnationDir);
        if (source.isDirectory()) {
            String[] children = source.list();
            for (int i = 0; i < children.length; i++) {
                copyFile(new File(source, children[i]), new File(destination, children[i]), destnationDir);
            }
        } else {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(dest);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    /**
     * To get email template by subject
     *
     * @param subject string to get EmailTemplates
     * @return EmailTemplates w.r.t the subject
     */
    @Override
    public EmailTemplates getEmailTemplateBySubject(String subject) {
        if (Utils.isEmpty(subject))
            return null;

        try {
            return lookUpDAO.getEmailTemplateBySubject(subject);
        }
        catch (Exception e) {
            logger.error("Error in getting email template data", e);
        }
        return null;
    }

    /**
     * To get status  Id by statusName
     *
     * @param statusName string to get Status
     * @return Status w.r.t the statusName
     */
    @Override
    @Transactional(readOnly = true)
    @Qualifier("txManager")
    public Status getStatusIdByLabel(String statusName) {
        if (Utils.isEmpty(statusName))
            return null;

        try {
            return lookUpDAO.getStatusIdByLabel(statusName);
        }
        catch (Exception e) {
            logger.error("Error in getting email template data", e);
        }
        return null;
    }

    /**
     * To get company Id by companyName
     *
     * @param companyName string to get EmailTemplates
     * @return Company w.r.t the companyName
     */
    @Override
    public Company getCompanyIdByLabel(String companyName) {
        if (Utils.isEmpty(companyName))
            return null;

        try {
            return lookUpDAO.getCompanyIdByName(companyName);
        }
        catch (Exception e) {
            logger.error("Error in getting company data", e);
        }
        return null;
    }

    /**
     * To get company LookUp By Cache
     *
     * @return List of company obj's
     */
    public List<Company> getCompanyLookUpByCache() {
        return utilDAO.updateLookup(Company.class);
    }

    /**
     * To get Company List
     *
     * @param companyIds set of Ids to be filtered
     * @return List of Company's w.r.t companyIds
     */
    public List<Company> getCompanyListObj(Set<Integer> companyIds) {
        if (companyIds == null || companyIds.size() == 0)
            return null;

        try {
            return lookUpDAO.getCompanyListObj(companyIds);
        }
        catch (Exception e) {
            logger.error("Error in getting company list data", e);
        }
        return null;
    }
}


