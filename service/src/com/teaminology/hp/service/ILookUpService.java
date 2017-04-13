package com.teaminology.hp.service;

import java.util.List;
import java.util.Set;

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

/**
 * Contains method prototypes to retrieve lookup data.
 *
 * @author sarvanic
 */
public interface ILookUpService
{

    /**
     * To get language lookup data
     *
     * @return List of Language object's
     */

    public List<Language> getLanguages();

    /**
     * To get Parts of Speech lookup data
     *
     * @return List of PartsOfSpeech object's
     */

    public List<PartsOfSpeech> getPOSLookUp();

    /**
     * To get Form lookup data
     *
     * @return List of Form object's
     */

    public List<Form> getFormLookUp();

    /**
     * To get Program lookup data
     *
     * @return List of Program object's
     */
    public List<Program> getProgramLookUp();


    /**
     * To get PartsOfSpeech for a part of speech id
     *
     * @param POSId Integer to get PartsOfSpeech
     * @return PartsOfSpeech w.r.t the part of speech id
     */
    public PartsOfSpeech getPartsOfSpeech(Integer POSId);

    /**
     * To get Program for a program id
     *
     * @param programId Integer to get Program
     * @return Program w.r.t the programId
     */

    public Program getProgram(Integer programId);

    /**
     * To get Form for a form id
     *
     * @param formId Integer to get Form
     * @return Form w.r.t the formId
     */

    public Form getForm(Integer formId);

    /**
     * To get language for a language id
     *
     * @param langId Integer to get language
     * @return Language w.r.t the langId
     */

    public Language getLanguage(Integer langId);

    /**
     * To get concept category lookup data
     *
     * @return List of ConceptCategory object's
     */


    public List<ConceptCategory> getConceptCategoryLookUp();


    /**
     * To get category lookup data
     *
     * @return List of Category object's
     */
    public List<Category> getCategoryLookUp();


    /**
     * To get product group lookup data
     *
     * @return List of ProductGroup object's
     */

    public List<ProductGroup> getProductGroupLookUp();

    /**
     * To get status lookup data
     *
     * @return List of Status object's
     */

    public List<Status> getStatusLookUp();

    /**
     * To get Domain lookup data
     *
     * @return List of Domain object's
     */

    public List<Domain> getDomainLookUp();

    /**
     * To get Category for a category id
     *
     * @param categoryId Integer to get Category
     * @return Category w.r.t the categoryId
     */

    public Category getCategory(Integer categoryId);

    /**
     * To get email template for emailTemplateId
     *
     * @param emailTemplateId Integer to get EmailTemplates
     * @return EmailTemplates w.r.t the emailTemplateId
     */

    public EmailTemplates getEmailTemplate(Integer emailTemplateId);

    /**
     * Domain  create/update/delete(CUD) operations
     *
     * @param domain Domain object to be CUD
     * @param userId Integer for transactions
     * @return status  w.r.t to the operation
     */

    public String setDomainCUD(Domain domain, Integer userId);


    /**
     * Language create/update/delete(CUD) operations
     *
     * @param language Language object to be CUD
     * @param userId   Integer for transactions
     * @return status w.r.t the operations
     */

    public String setLanguageCUD(Language language, Integer userId);

    /**
     * Email template create/update/delete(CUD) operations
     *
     * @param emailTemplate EmailTemplate object to get email template data
     * @param userId        Integer for transactions
     * @return status w.r.t the operations
     */


    public String setEmailTemplateCUD(EmailTemplates emailTemplate, Integer userId);

    /**
     * To get vote config
     *
     * @return VoteConfig lookup
     */

    public VoteConfig getVoteConfig();

    /**
     * VoteConfig update/delete(UD) operations
     *
     * @param voteConfig VoteConfig object to UD
     * @param userId     Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     */
    public String setVoteConfigUD(VoteConfig voteConfig, Integer userId);

    /**
     * To get  sorted languages  data
     *
     * @param colName   Column name that has to be sorted
     * @param sortOrder Order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of Language object's
     */

    public List<Language> getSortedLanguages(String colName, String sortOrder, int pageNum);

    /**
     * To verify whether languageLabel exists or not in database
     *
     * @return Returns true if languageLabel exists else it returns false
     */
    public boolean isLanguageLabelExists(String languageLabel);

    /**
     * To verify whether languageCode exists or not in database
     *
     * @return Returns true if languageCode exists else it returns false
     */
    public boolean isLanguageCodeExists(String languageCode);

    /**
     * To get Language by label data for languageName
     *
     * @param languageName String to get language
     * @return Language w.r.t the language name
     */
    public Language getLanguageByLabel(String languageName);


    /**
     * Category  create/update/delete(CUD) operations
     *
     * @param category Category object to be CUD
     * @param userId   Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     */
    public String setCategoryCUD(final Category category, final Integer userId);


    /**
     * To verify whether domain exists or not in database
     *
     * @return Returns true if domain exists else it returns false
     */
    public boolean isDomainExists(String domain);

    /**
     * To verify whether category exists or not in database
     *
     * @return Returns true if category exists else it returns false
     */

    public boolean isCategoryExists(String category);

    /**
     * To get Domain for a domian id
     *
     * @param domainId Integer to get Domain
     * @return Domain w.r.t the domainId
     */

    public Domain getDomainById(Integer domainId);

    /**
     * To get product group  for a product id
     *
     * @param productId Integer to get ProductGroup
     * @return ProductGroup w.r.t the productId
     */
    public ProductGroup getProductGroupById(Integer productId);

    /**
     * To get content type for a contentTypeID
     *
     * @param contentTypeID Integer to get ContentType
     * @return ContentType w.r.t the contentTypeID
     */
    public ContentType getContentTypeById(Integer contentTypeID);

    /**
     * To get content type lookup data
     *
     * @return List of ContentType object's
     */
    public List<ContentType> getContentType();

    /**
     * To get Company lookup data
     *
     * @return List of Company object's
     */

    public List<Company> getCompanyLookUp();

    /**
     * Add new company.
     *
     * @param company Company that needs to be added
     * @return If company  is added it returns success else failed
     */

    public String addNewCompany(final Company company);

    /**
     * To get company details
     *
     * @param request   HttpServletRequest
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of companies
     */

    public List<Company> getCompanyDetails(String colName, String sortOrder, Integer pageNum);

    /**
     * To get total active companies
     *
     * @return Integer holding the count of total active companies
     */
    public int getActiveCompaniesCount();

    /**
     * To update company.
     *
     * @param company Company that needs to be updated
     * @return If company  is updated it returns success else failed
     */
    public String updateCompany(Company company);

    /**
     * To delete companies
     *
     * @param request    HttpServletRequest
     * @param companyIds array of integer companyIds that needs to be deleted
     */

    public void deleteCompanies(final Integer[] companyIds, final Integer userId);

    /**
     * To verify whether company exists or not in database
     *
     * @param companyName String to get company
     * @return Returns true if company exists else it returns false
     */

    public boolean isCompanyExists(String companyName);

    /**
     * To get company for a company id
     *
     * @param companyId Integer to get company
     * @return Company w.r.t the companyId
     */

    public Company getCompanyById(Integer companyId);

    /**
     * To get user role  for a role id
     *
     * @param roleId Integer to get user role
     * @return role w.r.t the roleId
     */
    public Role getUserRole(Integer roleId);

    /**
     * To get company by context root
     *
     * @param contextRoot String  to get company details
     * @return Company w.r.t the contextRoot
     */
    public Company getCompanyByContextRoot(String contextRoot);

    /**
     * To create company context
     *
     * @param company Company  that has to be created
     * @param An      integer for which data is to be retrieved
     */
    public void createCompanyContext(Company company, Integer userId);

    /**
     * To get email template by subject
     *
     * @param subject string to get EmailTemplates
     * @return EmailTemplates w.r.t the subject
     */
    public EmailTemplates getEmailTemplateBySubject(String subject);

    /**
     * To get company Id by companyName
     *
     * @param companyName string to get EmailTemplates
     * @return Company w.r.t the companyName
     */
    public Company getCompanyIdByLabel(String companyName);

    /**
     * To get status  Id by statusName
     *
     * @param statusName string to get Status
     * @return Status w.r.t the statusName
     */
    public Status getStatusIdByLabel(String statusName);

    /**
     * To get company LookUp By Cache
     *
     * @return List of company obj's
     */
    public List<Company> getCompanyLookUpByCache();

    /**
     * To get Company List
     *
     * @param companyIds set of Ids to be filtered
     * @return List of Company's w.r.t companyIds
     */
    public List<Company> getCompanyListObj(Set<Integer> companyIds);

}
