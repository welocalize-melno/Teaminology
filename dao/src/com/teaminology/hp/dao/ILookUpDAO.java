package com.teaminology.hp.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;

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
 * Contains DAO Interface methods to handle the lookup tables data.
 *
 * @author sarvanic
 */
public interface ILookUpDAO
{

    /**
     * To get language lookup data
     *
     * @return List of Language object's
     * @throws DataAccessException
     */
    public List<Language> getLanguages() throws DataAccessException;


    /**
     * To get Languages lookup data Map
     *
     * @return List of Languages object's
     * @throws DataAccessException
     */

    public Map<Object, Language> getLanguagesLookUpMap() throws DataAccessException;


    /**
     * To get Parts of Speech lookup data
     *
     * @return List of PartsOfSpeech object's
     * @throws DataAccessException
     */

    public List<PartsOfSpeech> getPartsOfSpeechLookUp() throws DataAccessException;


    /**
     * To get PartsOfSpeech lookup data Map
     *
     * @return List of PartsOfSpeech object's
     * @throws DataAccessException
     */

    public Map<Object, PartsOfSpeech> getPartsOfSpeechLookUpMap() throws DataAccessException;


    public Map<Object, Program> getProramLookupMap() throws DataAccessException;
    /**
     * To get Form lookup data
     *
     * @return List of Form object's
     * @throws DataAccessException
     */
    public List<Form> getFormLookUp() throws DataAccessException;

    /**
     * To get Form lookup data Map
     *
     * @return List of Form object's
     * @throws DataAccessException
     */

    public Map<Object, Form> getFormLookUpMap() throws DataAccessException;


    /**
     * To get Program lookup data
     *
     * @return List of Program object's
     * @throws DataAccessException
     */

    public List<Program> getProgramLookUp() throws DataAccessException;

    /**
     * To get ConceptCategory lookup data
     *
     * @return List of ConceptCategory object's
     * @throws DataAccessException
     */

    public List<ConceptCategory> getConceptCategoryLookUp() throws DataAccessException;

    /**
     * To get Category lookup data
     *
     * @return List of Category object's
     * @throws DataAccessException
     */

    public List<Category> getCategoryLookUp() throws DataAccessException;

    /**
     * To get Category lookup data Map
     *
     * @return List of Category object's
     * @throws DataAccessException
     */

    public Map<Object, Category> getCategoryLookUpMap() throws DataAccessException;


    /**
     * To get ProductGroup lookup data
     *
     * @return List of ProductGroup object's
     * @throws DataAccessException
     */

    public List<ProductGroup> getProductGroupLookUp() throws DataAccessException;

    /**
     * To get Status lookup data
     *
     * @return List of Status object's
     * @throws DataAccessException
     */

    public List<Status> getStatusLookUp() throws DataAccessException;
    public Map<Object,Status>  getStatusLookUpMap() throws DataAccessException; 

    /**
     * To get Roles lookup data
     *
     * @return List of Roles object's
     * @throws DataAccessException
     */
    public List<Role> getRolesLookUp();


    /**
     * To get Roles lookup data Map
     *
     * @return List of Roles object's
     * @throws DataAccessException
     */

    public Map<Object, Role> getRolesLookUpMap() throws DataAccessException;


    /**
     * To get PartsOfSpeech for a part of speech id
     *
     * @param POSId Integer to get PartsOfSpeech
     * @return PartsOfSpeech w.r.t the part of speech id
     * @throws DataAccessException
     */

    public PartsOfSpeech getPartsOfSpeech(Integer POSId) throws DataAccessException;

    /**
     * To get Program for a program id
     *
     * @param programId Integer to get Program
     * @return Program w.r.t the programId
     * @throws DataAccessException
     */

    public Program getProgram(Integer programId) throws DataAccessException;

    /**
     * To get Form for a form id
     *
     * @param formId Integer to get Form
     * @return Form w.r.t the formId
     * @throws DataAccessException
     */
    public Form getForm(Integer formId) throws DataAccessException;

    /**
     * To get Language lookup data for languageId
     *
     * @param languageId Integer to get Language
     * @return Language w.r.t the languageId
     * @throws DataAccessException
     */

    public Language getLanguage(Integer languageId) throws DataAccessException;

    /**
     * To get Status from the status Id
     *
     * @param statusId Integer to get Status
     * @return Status w.r.t the statusId
     * @throws DataAccessException
     */

    public Status getStatus(Integer statusId) throws DataAccessException;

    /**
     * To get Category from the categoryId
     *
     * @param categoryId Integer to get Category
     * @return Category w.r.t the categoryId
     * @throws DataAccessException
     */

    public Category getCategory(Integer categoryId) throws DataAccessException;


    /**
     * To get ConceptCategory from the conceptCatId
     *
     * @param conceptCatId Integer to get ConceptCategory
     * @return ConceptCategory w.r.t the conceptCatId
     * @throws DataAccessException
     */

    public ConceptCategory getConceptCategory(Integer conceptCatId) throws DataAccessException;


    /**
     * To get Domain lookup data
     *
     * @return List of domain object's
     * @throws DataAccessException
     */
    public List<Domain> getDomainLookUp() throws DataAccessException;


    /**
     * To get Domain lookup data Map
     *
     * @return List of Domain object's
     * @throws DataAccessException
     */

    public Map<Object, Domain> getDomainLookUpMap() throws DataAccessException;


    /**
     * To get Language object for language code
     *
     * @param languageCode String to get language
     * @return Language  w.r.t  the language code
     * @throws DataAccessException
     */

    public Language getLanguageByCode(String languageCode) throws DataAccessException;

    /**
     * To get Language object for language code
     *
     * @param languageCode String to get language
     * @return Language  w.r.t  the language code
     * @throws DataAccessException
     */

    public Language getLanguageByCode(String languageCode, boolean allType) throws DataAccessException;

    /**
     * To get PartsOfSpeech lookup data for a POSName
     *
     * @param POSName String to get partsOfSpeech
     * @return PartsOfSpeech w.r.t the pos name
     * @throws DataAccessException
     */

    public PartsOfSpeech getPartsOfSpeechByName(String POSName) throws DataAccessException;

    /**
     * To get Program lookup data for programName
     *
     * @param programName String to get program
     * @return Program w.r.t the program name
     * @throws DataAccessException
     */

    public Program getProgramByName(String programName) throws DataAccessException;

    /**
     * To get Language by label data for languageName
     *
     * @param languageName String to get language
     * @return Language w.r.t the language name
     * @throws DataAccessException
     */

    public Language getLanguageByLabel(String languageName) throws DataAccessException;
    
    public  ConceptCategory getConceptCategoryByLabel(String conCapName) throws DataAccessException;

    /**
     * To get email template for emailTemplateId
     *
     * @param emailTemplateId Integer to get EmailTemplates
     * @return EmailTemplates w.r.t the emailTemplateId
     * @throws DataAccessException
     */
    public EmailTemplates getEmailTemplate(Integer emailTemplateId) throws DataAccessException;


    /**
     * Domain  create/update/delete(CUD) operations
     *
     * @param domain Domain object to be CUD
     * @param userId Integer for transactions
     * @return status  w.r.t to the operation
     * @throws DataAccessException
     */
    public String setDomainCUD(final Domain domain, final Integer userId) throws DataAccessException;

    /**
     * Language create/update/delete(CUD) operations
     *
     * @param language Language object to be CUD
     * @param userId   Integer for transactions
     * @return status w.r.t the operations
     * @throws DataAccessException
     */

    public String setLanguageCUD(final Language language, final Integer userId) throws DataAccessException;

    /**
     * Email template create/update/delete(CUD) operations
     *
     * @param emailTemplate EmailTemplate object to get email template data
     * @param userId        Integer for transactions
     * @return status w.r.t the operations
     * @throws DataAccessException
     */

    public String setEmailTemplateCUD(final EmailTemplates emailTemplate, final Integer userId) throws DataAccessException;

    /**
     * To get vote config
     *
     * @return VoteConfig lookup
     * @throws DataAccessException
     */

    public VoteConfig getVoteConfig() throws DataAccessException;

    /**
     * VoteConfig update/delete(UD) operations
     *
     * @param voteConfig VoteConfig object to UD
     * @param userId     Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     * @throws DataAccessException
     */

    public String setVoteConfigUD(VoteConfig voteConfig, Integer userId) throws DataAccessException;

    /**
     * To get  sorted languages  data
     *
     * @param colName   Column name that has to be sorted
     * @param sortOrder Order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of Language object's
     * @throws DataAccessException
     */
    public List<Language> getSortedLanguages(String colName, String sortOrder, int pageNum) throws DataAccessException;


    /**
     * To get Form lookup data for formName
     *
     * @param formName String to get form
     * @return Form w.r.t the formName
     * @throws DataAccessException
     */
    public Form getFormByName(String formName) throws DataAccessException;


    /**
     * To get Role for userTypeId
     *
     * @param roleId Integer to get UserType
     * @return UserType w.r.t the userTypeId
     * @throws DataAccessException
     */

    public Role getRole(Integer roleId) throws DataAccessException;


    /**
     * To verify whether languageLabel exists or not in database
     *
     * @return Returns true if languageLabel exists else it returns false
     * @throws DataAccessException
     */
    public boolean isLanguageLabelExists(String languageLabel) throws DataAccessException;


    /**
     * To verify whether languageCode exists or not in database
     *
     * @return Returns true if languageCode exists else it returns false
     * @throws DataAccessException
     */
    public boolean isLanguageCodeExists(String languageCode) throws DataAccessException;

    /**
     * Category  create/update/delete(CUD) operations
     *
     * @param domain Category object to be CUD
     * @param userId Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     * @throws DataAccessException
     */
    public String setCategoryCUD(final Category domain, final Integer userId) throws DataAccessException;

    /**
     * To verify whether domain exists or not in database
     *
     * @return Returns true if domain exists else it returns false
     * @throws DataAccessException
     */
    public boolean isDomainExists(String domain) throws DataAccessException;

    /**
     * To verify whether category exists or not in database
     *
     * @return Returns true if category exists else it returns false
     * @throws DataAccessException
     */
    public boolean isCategoryExists(String category) throws DataAccessException;

    /**
     * To get domain for a domain id
     *
     * @param domainId Integer to get domain
     * @return Domain w.r.t the domainId
     * @throws DataAccessException
     */

    public Domain getDomain(Integer domainId) throws DataAccessException;

    /**
     * To get Category lookup data for a categoryName
     *
     * @param categoryName String to get Category
     * @return Category  w.r.t the category name
     * @throws DataAccessException
     */

    public Category getCategoryByName(String categoryName) throws DataAccessException;

    /**
     * To get ProductGroup from the productGroupId
     *
     * @param productId Integer to get product group
     * @return ProductGroup w.r.t the productGroupId
     * @throws DataAccessException
     */
    public ProductGroup getProductGroup(Integer productId) throws DataAccessException;

    /**
     * To get ContentType from the contentTypeId
     *
     * @param contentTypeId Integer to get ContentType
     * @return ContentType w.r.t the contentTypeId
     * @throws DataAccessException
     */
    public ContentType getContentType(Integer contentTypeId) throws DataAccessException;

    /**
     * To get domain lookup data for a label
     *
     * @param lable String to get Domain
     * @return Domain w.r.t the lable
     * @throws DataAccessException
     */
    public Domain getDomainByLable(String lable) throws DataAccessException;

    /**
     * To get ContentType lookup data
     *
     * @return list ContentType obj's
     * @throws DataAccessException
     */
    public List<ContentType> getContentType() throws DataAccessException;

    /**
     * To  add domain
     *
     * @param domain Domain that has to be added
     * @throws DataAccessException
     */

    public void addDomain(Domain domain) throws DataAccessException;

    /**
     * To get ProductGroup lookup data for a label
     *
     * @param label String to get ProductGroup
     * @return ProductGroup w.r.t the lable
     * @throws DataAccessException
     */
    public ProductGroup getProductGroupByLabel(String label) throws DataAccessException;

    /**
     * To  add product group
     *
     * @param product ProductGroup that has to be added
     * @throws DataAccessException
     */
    public void addProduct(ProductGroup product) throws DataAccessException;

    /**
     * To get ContentType lookup data for a label
     *
     * @param label String to get ContentType
     * @return ContentType w.r.t the lable
     * @throws DataAccessException
     */
    public ContentType getContentByLabel(String label) throws DataAccessException;

    /**
     * To  add contentType
     *
     * @param contentType ContentType that has to be added
     * @throws DataAccessException
     */
    public void addContent(ContentType contentType) throws DataAccessException;

    /**
     * To get Company lookup data
     *
     * @return List of company object's
     * @throws DataAccessException
     */
    public List<Company> getCompanyLookUp() throws DataAccessException;


    /**
     * To get Company lookup data Map
     *
     * @return List of company object's
     * @throws DataAccessException
     */

    public Map<Object, Company> getCompanyLookUpMap() throws DataAccessException;

    /**
     * Add new company.
     *
     * @param company RequestBody Company that needs to be added
     * @return If company  is added it returns success else failed
     * @throws DataAccessException
     */

    public String addCompany(final Company company) throws DataAccessException;

    /**
     * To get company details
     *
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of companies
     * @throws DataAccessException
     */

    public List<Company> getSortedCompanys(String colName, String sortOrder, Integer pageNum) throws DataAccessException;

    /**
     * To get Total Active companies count
     *
     * @return Integer which holds the count of active companies
     * @throws DataAccessException
     */
    public Integer getActiveCompaniesCount() throws DataAccessException;

    /**
     * To update company.
     *
     * @param company RequestBody Company that needs to be updated
     * @return If company  is updated it returns success else failed
     * @throws DataAccessException
     */

    public String updateCompany(Company company) throws DataAccessException;

    /**
     * To delete companies
     *
     * @param companyIds array of integer companyIds that needs to be deleted
     * @throws DataAccessException
     */

    public void deleteCompanies(final Integer[] companyIds, final Integer userId) throws DataAccessException;

    /**
     * To verify whether company exists or not in database
     *
     * @return Returns true if company exists else it returns false
     */

    public boolean isCompanyExists(String companyName);


    /**
     * To get company for a company id
     *
     * @param companyId Integer to get company
     * @return Company w.r.t the companyId
     * @throws DataAccessException
     */

    public Company getCompany(Integer companyId) throws DataAccessException;

    /**
     * To get Company lookup data for a label
     *
     * @param label String to get Company
     * @return Company w.r.t the lable
     * @throws DataAccessException
     */
    public Company getCompanyByLable(String label) throws DataAccessException;

    /**
     * To get user role  for a role id
     *
     * @param roleId Integer to get user role
     * @return role w.r.t the roleId
     * @throws DataAccessException
     */
    public Role getUserRole(Integer roleId) throws DataAccessException;

    /**
     * To get company by context root
     *
     * @param contextRoot String  to get company details
     * @return Company w.r.t the contextRoot
     * @throws DataAccessException
     */
    public Company getCompanyByContextRoot(String contextRoot) throws DataAccessException;

    /**
     * To get email template by subject
     *
     * @param subject string to get EmailTemplates
     * @return EmailTemplates w.r.t the subject
     * @throws DataAccessException
     */
    public EmailTemplates getEmailTemplateBySubject(String subject) throws DataAccessException;

    /**
     * To get languages by langId list
     *
     * @param ids Array of Integer to filter languages
     * @return List of Language object's
     * @throws DataAccessException
     */
    public List<Language> getLanguagesByIds(Integer[] ids) throws DataAccessException;

    /**
     * To get company Id by companyName
     *
     * @param companyName string to get Company
     * @return Company w.r.t the companyName
     */
    public Company getCompanyIdByName(String companyName);

    /**
     * To get status  Id by statusName
     *
     * @param statusName string to get Status
     * @return Status w.r.t the statusName
     */

    public Status getStatusIdByLabel(String statusName);

    /**
     * To get domains by domainId list
     *
     * @param ids Array of Integer to filter domains
     * @return List of Domain object's
     */
    public List<Domain> getDomainsByIds(Integer[] ids) throws DataAccessException;

    /**
     * To get companies by companyId list
     *
     * @param ids Array of Integer to filter companies
     * @return List of Company object's
     */
    public List<Company> getCompaniesByIds(Integer[] ids) throws DataAccessException;

    /**
     * To get part of speech by posId list
     *
     * @param ids Array of Integer to filter PartsOfSpeech
     * @return List of PartsOfSpeech object's
     */
    public List<PartsOfSpeech> getPartOfSpeechesByIds(Integer[] ids) throws DataAccessException;

    /**
     * To get category by categoryId list
     *
     * @param ids Array of Integer to filter category
     * @return List of Category object's
     */
    public List<Category> getCategoriesByIds(Integer[] ids) throws DataAccessException;

    /**
     * To get status list by statusId list
     *
     * @param ids Array of Integer to filter status
     * @return List of Status object's
     */
    public List<Status> getStatusesByIds(Integer[] ids) throws DataAccessException;

    /**
     * To get product group by posId list
     *
     * @param ids Array of Integer to filter product group
     * @return List of ProductGroup object's
     */
    public List<ProductGroup> getproducGroupsByIds(Integer[] ids) throws DataAccessException;

    /**
     * To get content type by contentId list
     *
     * @param ids Array of Integer to filter content type
     * @return List of ContentType object's
     */
    public List<ContentType> getContentTypesByIds(Integer[] ids) throws DataAccessException;

    /**
     * To get companyList
     *
     * @param companyIds to get Company
     * @return List of Company object w.r.t companyIds
     */
    public List<Company> getCompanyListObj(Set<Integer> companyIds) throws DataAccessException;


	public String getUserNameByUserId(Integer userId);

}
