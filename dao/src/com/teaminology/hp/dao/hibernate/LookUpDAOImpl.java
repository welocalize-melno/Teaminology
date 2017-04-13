package com.teaminology.hp.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jodd.util.StringUtil;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.EmailTemplates;
import com.teaminology.hp.bo.Role;
import com.teaminology.hp.bo.User;
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
import com.teaminology.hp.dao.DaoConstants;
import com.teaminology.hp.dao.HibernateDAO;
import com.teaminology.hp.dao.ILookUpDAO;

/**
 * Contains DAO methods to handle the lookup tables data.
 *
 * @author sarvanic
 */
public class LookUpDAOImpl extends HibernateDAO implements ILookUpDAO
{

    /**
     * To get language lookup data
     *
     * @return List of Language object's
     */
	@Transactional
    public List<Language> getLanguages() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Language.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.addOrder(Order.asc("languageLabel"));

        return (List<Language>) hibCriteria.list();
    }


    /**
     * To get Language lookup  as a Map data
     *
     * @return List of Language object's
     */
    public Map<Object, Language> getLanguagesLookUpMap() throws DataAccessException {
        List<Language> languages = getLanguages();
        Map<Object, Language> languageMap = null;
        if (languages != null && !languages.isEmpty()) {
            languageMap = new HashMap<Object, Language>();
            for (Language language : languages) {
                languageMap.put(language.getLanguageLabel(), language);
            }
        }

        return languageMap;
    }


    /**
     * To get Parts of Speech lookup data
     *
     * @return List of PartsOfSpeech object's
     */
    @Override
    @Transactional
    public List<PartsOfSpeech> getPartsOfSpeechLookUp() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(PartsOfSpeech.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (List<PartsOfSpeech>) hibCriteria.list();
    }

    /**
     * To get POSLookup  as a Map data
     *
     * @return List of PartofSpeech object's
     */
    @Transactional
    public Map<Object, PartsOfSpeech> getPartsOfSpeechLookUpMap() throws DataAccessException {
        List<PartsOfSpeech> partsOfSpeeches = getPartsOfSpeechLookUp();
        Map<Object, PartsOfSpeech> partsOfSpeechMap = null;
        if (partsOfSpeeches != null && !partsOfSpeeches.isEmpty()) {
            partsOfSpeechMap = new HashMap<Object, PartsOfSpeech>();
            for (PartsOfSpeech pos : partsOfSpeeches) {
                partsOfSpeechMap.put(pos.getPartOfSpeech(), pos);
            }
        }

        return partsOfSpeechMap;
    }
    /**
     * To get ProgramLookUp  as a Map data
     *
     * @return List of Status object's
     */
    @Transactional
    public Map<Object,Program> getProramLookupMap() throws DataAccessException{
    	List<Program> programs= getProgramLookUp();
    	Map<Object,Program> programMap=null;
    	if(programs != null && !programs.isEmpty()){
    		programMap=new HashMap<Object,Program>();
    		for(Program program:programs){
    			programMap.put(program.getProgramName(), program);
    		}
    	}
    	return programMap;
    }
    
    /**
     * To get StatusLookUp  as a Map data
     *
     * @return List of Status object's
     */
    @Transactional
    public Map<Object,Status> getStatusLookUpMap() throws DataAccessException{
    	List<Status> statuses= getStatusLookUp();
    	Map<Object,Status> statusMap=null;
    	if(statuses != null && !statuses.isEmpty()){
    		statusMap=new HashMap<Object,Status>();
    		for(Status status:statuses){
    			statusMap.put(status.getStatus(), status);
    		}
    	}
    	return statusMap;
    }
    
    /**
     * To get Form lookup data
     *
     * @return List of Form object's
     */
    @Override
    @Transactional
    public List<Form> getFormLookUp() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Form.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (List<Form>) hibCriteria.list();
    }

    /**
     * To get Form lookup  as a Map data
     *
     * @return List of Form object's
     */
    @Transactional
    public Map<Object, Form> getFormLookUpMap() throws DataAccessException {
        List<Form> forms = getFormLookUp();
        Map<Object, Form> formMap = null;
        if (forms != null && !forms.isEmpty()) {
            formMap = new HashMap<Object, Form>();
            for (Form form : forms) {
                formMap.put(form.getFormName(), form);
            }
        }

        return formMap;
    }

    /**
     * To get Program lookup data
     *
     * @return List of Program object's
     */
    @Override
    @Transactional
    public List<Program> getProgramLookUp() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Program.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (List<Program>) hibCriteria.list();
    }

    /**
     * To get PartsOfSpeech for a part of speech id
     *
     * @param posId Integer to get PartsOfSpeech
     * @return PartsOfSpeech w.r.t the POSId
     */
    @Override
    public PartsOfSpeech getPartsOfSpeech(Integer posId) throws DataAccessException {
        if (posId == null || posId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(PartsOfSpeech.class, posId);
    }

    /**
     * To get Program for a program id
     *
     * @param programId Integer to get Program
     * @return Program w.r.t the programId
     */
    @Override
    public Program getProgram(Integer programId) throws DataAccessException {
        if (programId == null || programId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(Program.class, programId);
    }

    /**
     * To get domain for a domain id
     *
     * @param domainId Integer to get domain
     * @return Domain w.r.t the domainId
     */
    @Override
    public Domain getDomain(Integer domainId) throws DataAccessException {
        if (domainId == null || domainId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(Domain.class, domainId);
    }

    /**
     * To get Form for a form id
     *
     * @param formId Integer to get Form
     * @return Form w.r.t the formId
     */
    @Override
    public Form getForm(Integer formId) throws DataAccessException {
        if (formId == null || formId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(Form.class, formId);
    }

    /**
     * To get Language lookup data for languageId
     *
     * @param languageId Integer to get Language
     * @return Language w.r.t the languageId
     */
    @Override
    public Language getLanguage(Integer languageId) throws DataAccessException {
        if (languageId == null || languageId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(Language.class, languageId);
    }

    /**
     * To get ConceptCategory lookup data
     *
     * @return List of ConceptCategory object's
     */

    @Override
    @Transactional
    public List<ConceptCategory> getConceptCategoryLookUp() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(ConceptCategory.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (List<ConceptCategory>) hibCriteria.list();
    }

    /**
     * To get Category lookup data
     *
     * @return List of Category object's
     */

    @Override
    @Transactional
    public List<Category> getCategoryLookUp() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Category.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.addOrder(Order.asc("category"));
        return (List<Category>) hibCriteria.list();
    }

    /**
     * To get Category lookup  as a Map data
     *
     * @return List of Category object's
     */
    @Transactional
    public Map<Object, Category> getCategoryLookUpMap() throws DataAccessException {
        List<Category> categories = getCategoryLookUp();
        Map<Object, Category> categoryMap = null;
        if (categories != null && !categories.isEmpty()) {
            categoryMap = new HashMap<Object, Category>();
            for (Category category : categories) {
                categoryMap.put(category.getCategory(), category);
            }
        }

        return categoryMap;
    }

    /**
     * To get ProductGroup lookup data
     *
     * @return List of ProductGroup object's
     */
    @Override
    @Transactional
    public List<ProductGroup> getProductGroupLookUp() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(ProductGroup.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (List<ProductGroup>) hibCriteria.list();
    }

    /**
     * To get Status lookup data
     *
     * @return List of Status object's
     */
    @Override
    @Transactional
    public List<Status> getStatusLookUp() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Status.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (List<Status>) hibCriteria.list();
    }

    /**
     * To get Status from the status Id
     *
     * @param statusId Integer to get Status
     * @return Status w.r.t the statusId
     */
    @Override
    public Status getStatus(Integer statusId) throws DataAccessException {
        if (statusId == null || statusId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(Status.class, statusId);
    }

    /**
     * To get Category from the categoryId
     *
     * @param categoryId Integer to get Category
     * @return Category w.r.t the categoryId
     */
    @Override
    public Category getCategory(Integer categoryId) throws DataAccessException {
        if (categoryId == null || categoryId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(Category.class, categoryId);
    }

    /**
     * To get ConceptCategory from the conceptCatId
     *
     * @param conceptCatId Integer to get ConceptCategory
     * @return ConceptCategory w.r.t the conceptCatId
     */
    @Override
    public ConceptCategory getConceptCategory(Integer conceptCatId) throws DataAccessException {
        if (conceptCatId == null || conceptCatId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(ConceptCategory.class, conceptCatId);
    }

    /**
     * To get ProductGroup from the productGroupId
     *
     * @param productGroupId Integer to get product group
     * @return ProductGroup w.r.t the productGroupId
     */

    @Override
    public ProductGroup getProductGroup(Integer productGroupId) throws DataAccessException {
        if (productGroupId == null || productGroupId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(ProductGroup.class, productGroupId);
    }

    /**
     * To get Domain lookup data
     *
     * @return List of domain object's
     */
    @Override
    @Transactional
    public List<Domain> getDomainLookUp() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Domain.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.addOrder(Order.asc("domain"));

        return (List<Domain>) hibCriteria.list();
    }


    /**
     * To get Domain lookup  as a Map data
     *
     * @return List of Domain object's
     */
    @Transactional
    public Map<Object, Domain> getDomainLookUpMap() throws DataAccessException {
        List<Domain> domains = getDomainLookUp();
        Map<Object, Domain> domainMap = null;
        if (domains != null && !domains.isEmpty()) {
            domainMap = new HashMap<Object, Domain>();
            for (Domain domain : domains) {
                domainMap.put(domain.getDomain(), domain);
            }
        }

        return domainMap;
    }

    /**
     * To get ContentType from the contentTypeId
     *
     * @param contentTypeId Integer to get ContentType
     * @return ContentType w.r.t the contentTypeId
     */
    @Override
    public ContentType getContentType(Integer contentTypeId) throws DataAccessException {
        if (contentTypeId == null || contentTypeId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(ContentType.class, contentTypeId);
    }

    /**
     * To get Language object for language code
     *
     * @param languageCode String to get language
     * @return Language w.r.t the language code
     */
    @Override
    @Transactional
    public Language getLanguageByCode(String languageCode) throws DataAccessException {
        if (StringUtil.isEmpty(languageCode))
            return null;

        String langCode = languageCode.trim().toLowerCase();

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Language.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.sqlRestriction(
                "lower(trim(Language_Code)) = ?", langCode, new StringType()));

        return (Language) hibCriteria.uniqueResult();
    }

    /**
     * To get Language object for language code
     *
     * @param languageCode String to get language
     * @return Language w.r.t the language code
     */
    @Override
    @Transactional
    public Language getLanguageByCode(String languageCode, boolean allType) throws DataAccessException {
        if (StringUtil.isEmpty(languageCode))
            return null;

        String langCode = languageCode.trim().toLowerCase();
        Language language = getLanguageByCode(langCode);
        if (language == null && allType) {
            if (langCode.indexOf('-') > 0)
                langCode = langCode.replaceAll("-", "_");
            else
                langCode = langCode.replaceAll("_", "-");
            language = getLanguageByCode(langCode);
        }

        return language;
    }

    /**
     * To get PartsOfSpeech lookup data for a POSName
     *
     * @param POSName String to get partsOfSpeech
     * @return PartsOfSpeech w.r.t the pos name
     */
    @Override
    @Transactional
    public PartsOfSpeech getPartsOfSpeechByName(String POSName) throws DataAccessException {
        if (StringUtil.isEmpty(POSName))
            return null;

        POSName = POSName.trim().toLowerCase();

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(PartsOfSpeech.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.sqlRestriction(
                "lower(trim(part_of_speech)) = ?", POSName,
                new StringType()));

        return (PartsOfSpeech) hibCriteria.uniqueResult();
    }

    /**
     * To get Program lookup data for programName
     *
     * @param programName String to get program
     * @return Program w.r.t the program name
     */
    @Override
    @Transactional
    public Program getProgramByName(String programName) throws DataAccessException {
        if (StringUtil.isEmpty(programName))
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Program.class);
        hibCriteria.add(Restrictions.eq("programName", programName));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (Program) hibCriteria.uniqueResult();
    }

    /**
     * To get Language by label data for languageName
     *
     * @param languageName String to get language
     * @return Language w.r.t the language name
     */
    @Override
    @Transactional
    public Language getLanguageByLabel(String languageName) throws DataAccessException {
        if (StringUtil.isEmpty(languageName))
            return null;

        languageName = languageName.trim().toLowerCase();

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Language.class);
        //hibCriteria.add(Restrictions.eq("languageLabel", languageName));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.sqlRestriction(
                "lower(trim(language_label)) = ?", languageName,
                new StringType()));

        return (Language) hibCriteria.uniqueResult();
    }

    
    @Override
    @Transactional
    public ConceptCategory getConceptCategoryByLabel(String concatName) throws DataAccessException {
        if (StringUtil.isEmpty(concatName))
            return null;

        concatName = concatName.trim().toLowerCase();

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(ConceptCategory.class);
        //hibCriteria.add(Restrictions.eq("languageLabel", languageName));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.sqlRestriction(
                "lower(trim(concept_category)) = ?", concatName,
                new StringType()));

        return (ConceptCategory) hibCriteria.uniqueResult();
    }
    /**
     * To get email template for emailTemplateId
     *
     * @param emailTemplateId Integer to get EmailTemplates
     * @return EmailTemplates w.r.t the emailTemplateId
     */
    @Override
    public EmailTemplates getEmailTemplate(Integer emailTemplateId)
            throws DataAccessException {
        if (emailTemplateId == null || emailTemplateId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(EmailTemplates.class, emailTemplateId);
    }

    /**
     * To get vote config
     *
     * @return VoteConfig lookup
     */
    @Override
    @Transactional
    public VoteConfig getVoteConfig() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(VoteConfig.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (VoteConfig) hibCriteria.uniqueResult();
    }

    /**
     * Domain create/update/delete(CUD) operations
     *
     * @param domain Domain object to be CUD
     * @param userId Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     */
    @Override
    @Transactional
    public String setDomainCUD(final Domain domain, final Integer userId) {
        if (domain == null || userId == null || userId.intValue() < 1)
            return DaoConstants.FAILED;

        return getHibernateTemplate().execute(new HibernateCallback<String>()
        {
            @Override
            public String doInHibernate(Session session)
                    throws HibernateException, SQLException {
                if ("Add Domain".equalsIgnoreCase(domain.getTransactionType())) {
                    domain.setCreateDate(new Date());
                    domain.setCreatedBy(userId);
                    domain.setIsActive("Y");
                    session.save(domain);

                    return DaoConstants.SUCCESS;
                }

                Domain savedDomain = (Domain) session
                        .createCriteria(Domain.class)
                        .add(Restrictions.eq("domainId", domain.getDomainId()))
                        .add(Restrictions.eq("isActive", "Y"))
                        .uniqueResult();

                if (savedDomain != null) {
                    if ("Edit Domain".equalsIgnoreCase(domain.getTransactionType())) {
                        savedDomain.setDomain(domain.getDomain());
                        savedDomain.setUpdatedBy(userId);
                        savedDomain.setUpdateDate(new Date());
                        savedDomain.setIsActive("Y");
                        session.update(savedDomain);

                        return DaoConstants.SUCCESS;
                    }

                    if ("Delete Domain".equalsIgnoreCase(domain.getTransactionType())) {
                        savedDomain.setUpdatedBy(userId);
                        savedDomain.setUpdateDate(new Date());
                        savedDomain.setIsActive("N");
                        session.update(savedDomain);

                        return DaoConstants.SUCCESS;
                    }
                }

                return DaoConstants.FAILED;
            }
        });
    }

    /**
     * Language create/update/delete(CUD) operations
     *
     * @param language Language object to be CUD
     * @param userId   Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     */
    @Override
    public String setLanguageCUD(final Language language, final Integer userId) {
        if (language == null || userId == null || userId.intValue() < 1)
            return DaoConstants.FAILED;

        return getHibernateTemplate().execute(new HibernateCallback<String>()
        {
            @Override
            public String doInHibernate(Session session)
                    throws HibernateException, SQLException {
                if ("Add Language".equalsIgnoreCase(language.getTransactionType())) {
                    language.setCreateDate(new Date());
                    language.setCreatedBy(userId);
                    language.setIsActive("Y");
                    session.save(language);

                    return DaoConstants.SUCCESS;
                }

                Language savedLanguage = (Language) session
                        .createCriteria(Language.class)
                        .add(Restrictions.eq("languageId",
                                language.getLanguageId())).uniqueResult();

                if (savedLanguage != null) {
                    if ("Edit Language".equalsIgnoreCase(language.getTransactionType())) {
                        savedLanguage.setLanguageLabel(language.getLanguageLabel());
                        savedLanguage.setLanguageCode(language.getLanguageCode());
                        savedLanguage.setUpdatedBy(userId);
                        savedLanguage.setUpdateDate(new Date());
                        savedLanguage.setIsActive("Y");
                        session.update(savedLanguage);

                        return DaoConstants.SUCCESS;
                    }

                    if ("Delete Language".equalsIgnoreCase(language.getTransactionType())) {
                        savedLanguage.setUpdatedBy(userId);
                        savedLanguage.setUpdateDate(new Date());
                        savedLanguage.setIsActive("N");
                        session.update(savedLanguage);

                        return DaoConstants.SUCCESS;
                    }
                }
                return DaoConstants.FAILED;
            }
        });
    }

    /**
     * Email template create/update/delete(CUD) operations
     *
     * @param emailTemplate EmailTemplate object to get email template data
     * @param userId        Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     */
    @Override
    public String setEmailTemplateCUD(final EmailTemplates emailTemplate,
                                      final Integer userId) {
        if (emailTemplate == null || userId == null || userId.intValue() < 1)
            return DaoConstants.FAILED;

        return getHibernateTemplate().execute(new HibernateCallback<String>()
        {
            @Override
            public String doInHibernate(Session session)
                    throws HibernateException, SQLException {
                if ("Add Template".equalsIgnoreCase(emailTemplate.getTransactionType())) {
                    emailTemplate.setCreateDate(new Date());
                    emailTemplate.setCreatedBy(userId);
                    emailTemplate.setIsActive("Y");
                    session.save(emailTemplate);

                    return DaoConstants.SUCCESS;
                }

                EmailTemplates savedemailTemplate = (EmailTemplates) session
                        .createCriteria(EmailTemplates.class)
                        .add(Restrictions.eq("emailTemplateId",
                                emailTemplate.getEmailTemplateId()))
                        .uniqueResult();

                if (savedemailTemplate != null) {
                    if ("Edit Template".equalsIgnoreCase(emailTemplate.getTransactionType())) {
                        savedemailTemplate.setEmailMessageContent(emailTemplate.getEmailMessageContent());
                        savedemailTemplate.setEmailSubject(emailTemplate.getEmailSubject());
                        savedemailTemplate.setUpdatedBy(userId);
                        savedemailTemplate.setUpdateDate(new Date());
                        savedemailTemplate.setIsActive("Y");
                        session.update(savedemailTemplate);

                        return DaoConstants.SUCCESS;
                    }

                    if ("Delete Template".equalsIgnoreCase(emailTemplate.getTransactionType())) {
                        savedemailTemplate.setUpdatedBy(userId);
                        savedemailTemplate.setUpdateDate(new Date());
                        savedemailTemplate.setIsActive("N");
                        session.update(savedemailTemplate);

                        return DaoConstants.SUCCESS;
                    }
                }
                return DaoConstants.FAILED;
            }
        });
    }

    /**
     * VoteConfig update/delete(UD) operations
     *
     * @param voteConfig VoteConfig object to UD
     * @param userId     Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     */
    @Override
    public String setVoteConfigUD(final VoteConfig voteConfig,
                                  final Integer userId) throws DataAccessException {
        if (voteConfig == null || userId == null || userId.intValue() < 1)
            return DaoConstants.FAILED;

        return getHibernateTemplate().execute(new HibernateCallback<String>()
        {
            @Override
            public String doInHibernate(Session session)
                    throws HibernateException, SQLException {
                if ("Add Vote".equalsIgnoreCase(voteConfig.getTransactionType())) {
                    voteConfig.setCreateDate(new Date());
                    voteConfig.setCreatedBy(userId);
                    voteConfig.setIsActive("Y");
                    session.save(voteConfig);

                    return DaoConstants.SUCCESS;
                }

                VoteConfig savedVote = (VoteConfig) session
                        .createCriteria(VoteConfig.class)
                        .add(Restrictions.eq("voteConfigId",
                                voteConfig.getVoteConfigId())).uniqueResult();

                if (savedVote != null) {
                    if ("Edit Vote".equalsIgnoreCase(voteConfig.getTransactionType())) {
                        savedVote.setVoteConfigId(voteConfig.getVoteConfigId());
                        savedVote.setVotingPeriod(voteConfig.getVotingPeriod());
                        savedVote.setVotesPerUser(voteConfig.getVotesPerUser());
                        savedVote.setUpdatedBy(userId);
                        savedVote.setUpdateDate(new Date());
                        savedVote.setIsActive("Y");
                        session.update(savedVote);

                        return DaoConstants.SUCCESS;
                    }

                    if ("Delete Vote".equalsIgnoreCase(voteConfig.getTransactionType())) {
                        savedVote.setUpdatedBy(userId);
                        savedVote.setUpdateDate(new Date());
                        savedVote.setIsActive("N");
                        session.update(savedVote);
                        return DaoConstants.SUCCESS;
                    }
                }
                return DaoConstants.FAILED;
            }
        });
    }

    /**
     * To get sorted languages data
     *
     * @param colName   Column name that has to be sorted
     * @param sortOrder Order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of Language object's
     */
    @Transactional
    public List<Language> getSortedLanguages(String colName, String sortOrder,
                                             int pageNum) throws DataAccessException {
        int limitFrom = 0;
        int dataLimit = 5;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Language.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        if (pageNum > 0) {
            limitFrom = (pageNum - 1) * 5;
            hibCriteria.setFirstResult(limitFrom);
            hibCriteria.setMaxResults(dataLimit);
        }

        //By default, records are sorted by language label
        if (StringUtil.isEmpty(colName) || colName.trim().equalsIgnoreCase("null"))
            colName = "languageLabel";

        if ("DESC".equalsIgnoreCase(sortOrder))
            hibCriteria.addOrder(Order.desc(colName));
        else
            hibCriteria.addOrder(Order.asc(colName));

        return (List<Language>) hibCriteria.list();

    }

    /**
     * To get Form lookup data for formName
     *
     * @param formName String to get form
     * @return Form w.r.t the formName
     */
    @Override
    @Transactional
    public Form getFormByName(String formName) throws DataAccessException {
        if (StringUtil.isEmpty(formName))
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Form.class);
        hibCriteria.add(Restrictions.eq("formName", formName));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (Form) hibCriteria.uniqueResult();
    }

    /**
     * To get UserType for userTypeId
     *
     * @param roleId Integer to get UserType
     * @return UserType w.r.t the userTypeId
     */

    public Role getRole(Integer roleId) throws DataAccessException {
        if (roleId == null || roleId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(Role.class, roleId);
    }

    /**
     * To verify whether languageCode exists or not in database
     *
     * @return Returns true if languageCode exists else it returns false
     */
    public boolean isLanguageCodeExists(String languageCode) throws DataAccessException {
        if (StringUtil.isEmpty(languageCode))
            return false;

        return getLanguageByCode(languageCode, true) != null;
    }

    /**
     * To verify whether languageLabel exists or not in database
     *
     * @return Returns true if languageLabel exists else it returns false
     */
    public boolean isLanguageLabelExists(String languageLabel) throws DataAccessException {
        if (StringUtil.isEmpty(languageLabel))
            return false;

        return getLanguageByLabel(languageLabel) != null;
    }

    /**
     * To verify whether domain exists or not in database
     *
     * @return Returns true if domain exists else it returns false
     */
    @Transactional
    public boolean isDomainExists(String domain) throws DataAccessException {
        if (StringUtil.isEmpty(domain))
            return false;

        return getDomainByLable(domain) != null;
    }

    /**
     * To verify whether category exists or not in database
     *
     * @return Returns true if category exists else it returns false
     */
    @Transactional
    public boolean isCategoryExists(String category) throws DataAccessException {
        if (StringUtil.isEmpty(category))
            return false;

        return getCategoryByName(category) != null;
    }

    /**
     * Category create/update/delete(CUD) operations
     *
     * @param category Category object to be CUD
     * @param userId   Integer for transactions
     * @return Status is "success" if successfully modified else "failure"
     */
    @Override
    public String setCategoryCUD(final Category category, final Integer userId) throws DataAccessException {
        if (category == null || userId == null || userId.intValue() < 1)
            return DaoConstants.FAILED;

        return getHibernateTemplate().execute(new HibernateCallback<String>()
        {
            @Override
            public String doInHibernate(Session session)
                    throws HibernateException, SQLException {
                if ("Add Category".equalsIgnoreCase(category.getTransactionType())) {
                    category.setCreateDate(new Date());
                    category.setCreatedBy(userId);
                    category.setIsActive("Y");
                    session.save(category);

                    return DaoConstants.SUCCESS;
                }

                Category savedCategory = (Category) session.get(Category.class, category.getCategoryId());

                if (savedCategory != null) {
                    if ("Edit Category".equalsIgnoreCase(category.getTransactionType())) {
                        savedCategory.setCategory(category.getCategory());
                        savedCategory.setUpdatedBy(userId);
                        savedCategory.setUpdateDate(new Date());
                        savedCategory.setIsActive("Y");
                        session.update(savedCategory);

                        return DaoConstants.SUCCESS;
                    }

                    if ("Delete Category".equalsIgnoreCase(category.getTransactionType())) {
                        savedCategory.setUpdatedBy(userId);
                        savedCategory.setUpdateDate(new Date());
                        savedCategory.setIsActive("N");
                        session.update(savedCategory);
                        return DaoConstants.SUCCESS;
                    }
                }
                return DaoConstants.FAILED;
            }
        });
    }

    /**
     * To get Category lookup data for a CategoryName
     *
     * @param categoryName String to get Category
     * @return Category w.r.t the Category name
     */
    @Override
    @Transactional
    public Category getCategoryByName(String categoryName) throws DataAccessException {
        if (StringUtil.isEmpty(categoryName))
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Category.class);
        hibCriteria.add(Restrictions.eq("category", categoryName));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (Category) hibCriteria.uniqueResult();
    }

    /**
     * To get domain lookup data for a label
     *
     * @param lable String to get Domain
     * @return Domain w.r.t the lable
     */
    @Override
    @Transactional
    public Domain getDomainByLable(String lable) throws DataAccessException {
        if (StringUtil.isEmpty(lable))
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Domain.class);
        hibCriteria.add(Restrictions.eq("domain", lable));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (Domain) hibCriteria.uniqueResult();
    }

    /**
     * To add domain
     *
     * @param domain Domain that has to be added
     */
    @Override
    @Transactional
    public void addDomain(Domain domain) throws DataAccessException {
        if (domain == null)
            return;

        Session session = getHibernateSession();
        domain.setIsActive("Y");
        session.save(domain);
    }

    /**
     * To get ProductGroup lookup data for a label
     *
     * @param label String to get ProductGroup
     * @return ProductGroup w.r.t the lable
     */
    @Override
    @Transactional
    public ProductGroup getProductGroupByLabel(String label) throws DataAccessException {
        if (StringUtil.isEmpty(label))
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(ProductGroup.class);
        hibCriteria.add(Restrictions.eq("product", label));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (ProductGroup) hibCriteria.uniqueResult();
    }

    /**
     * To add product group
     *
     * @param product ProductGroup that has to be added
     */
    @Override
    @Transactional
    public void addProduct(ProductGroup product) throws DataAccessException {
        if (product == null)
            return;

        Session session = getHibernateSession();
        product.setIsActive("Y");
        session.save(product);
    }

    /**
     * To get ContentType lookup data for a label
     *
     * @param label String to get ContentType
     * @return ContentType w.r.t the lable
     */
    @Override
    @Transactional
    public ContentType getContentByLabel(String label) throws DataAccessException {
        if (StringUtil.isEmpty(label))
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(ContentType.class);
        hibCriteria.add(Restrictions.eq("contentType", label));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (ContentType) hibCriteria.uniqueResult();
    }

    /**
     * To add contentType
     *
     * @param contentType ContentType that has to be added
     */
    @Override
    @Transactional
    public void addContent(ContentType contentType) throws DataAccessException {
        if (contentType == null)
            return;

        Session session = getHibernateSession();
        contentType.setIsActive("Y");
        session.save(contentType);
    }

    /**
     * To get ContentType lookup data
     *
     * @return list ContentType obj's
     */
    @Override
    @Transactional
    public List<ContentType> getContentType() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(ContentType.class);

        return (List<ContentType>) hibCriteria.list();
    }

    /**
     * To get Company lookup data for a label
     *
     * @param label String to get Company
     * @return Company w.r.t the lable
     */
    @Override
    @Transactional
    public Company getCompanyByLable(String label) throws DataAccessException {
        if (StringUtil.isEmpty(label))
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria.add(Restrictions.eq("companyName", label));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        return (Company) hibCriteria.uniqueResult();
    }

    /**
     * To get Company lookup data
     *
     * @return List of Company object's
     */
    @Transactional
    public List<Company> getCompanyLookUp() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.addOrder(Order.asc("companyName"));

        return (List<Company>) hibCriteria.list();
    }

    /**
     * To get Company lookup  as a Map data
     *
     * @return List of Company object's
     */
    @Transactional
    public Map<Object, Company> getCompanyLookUpMap() throws DataAccessException {
        List<Company> companies = getCompanyLookUp();
        Map<Object, Company> companyMap = null;
        if (companies != null && !companies.isEmpty()) {
            companyMap = new HashMap<Object, Company>();
            for (Company company : companies) {
                companyMap.put(company.getCompanyName(), company);
            }
        }

        return companyMap;
    }

    /**
     * To get company details
     *
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of companies
     */
    @Transactional
    public List<Company> getSortedCompanys(String colName, String sortOrder,
                                           Integer pageNum) throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));

        int limitFrom = 0;
        int dataLimit = 10;
        if (pageNum != 0) {
            limitFrom = (pageNum - 1) * 10;
            hibCriteria.setFirstResult(limitFrom);
            hibCriteria.setMaxResults(dataLimit);
        }

        if (StringUtil.isEmpty(colName) || colName.trim().equalsIgnoreCase("null"))
            colName = "companyName";

        if ("DESC".equalsIgnoreCase(sortOrder))
            hibCriteria.addOrder(Order.desc(colName));
        else
            hibCriteria.addOrder(Order.asc(colName));

        return (List<Company>) hibCriteria.list();
    }

    /**
     * Add new company.
     *
     * @param company RequestBody Company that needs to be added
     * @return If company is added it returns success else failed
     */

    public String addCompany(Company company) {
        if (company == null)
            return DaoConstants.FAILED;

        company.setIsActive("Y");
        getHibernateTemplate().save(company);
        return DaoConstants.SUCCESS;
    }

    /**
     * To get Total Active companies count
     *
     * @return Integer which holds the count of active companies
     */
    @Override
    @Transactional
    public Integer getActiveCompaniesCount() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.setProjection(Projections.count("companyId"));
        Integer countOfCompanies = (Integer) hibCriteria.uniqueResult();

        return countOfCompanies;
    }

    /**
     * To update company.
     *
     * @param company RequestBody Company that needs to be updated
     * @return If company is updated it returns success else failed
     */

    public String updateCompany(Company company) throws DataAccessException {
        if (company == null)
            return DaoConstants.FAILED;

        getHibernateTemplate().update(company);
        return DaoConstants.SUCCESS;
    }

    /**
     * To delete companies
     *
     * @param companyIds array of integer companyIds that needs to be deleted
     */

    @Override
    public void deleteCompanies(final Integer[] companyIds, final Integer userId)
            throws DataAccessException {
        if (companyIds == null || companyIds.length == 0)
            return;

        getHibernateTemplate().execute(new HibernateCallback<Void>()
        {
            @Override
            public Void doInHibernate(Session session)
                    throws HibernateException, SQLException {
                for (int i = 0; i < companyIds.length; i++) {
                    Integer companyId = companyIds[i];
                    Company company = (Company) session
                            .createCriteria(Company.class)
                            .add(Restrictions.eq("companyId", companyId))
                            .uniqueResult();

                    if (company != null) {
                        company.setIsActive("N");
                        company.setUpdateDate(new Date());
                        company.setUpdatedBy(userId);
                        session.update(company);
                    }
                }
                return null;
            }
        });
    }

    /**
     * To verify whether company exists or not in database
     *
     * @return Returns true if company exists else it returns false
     */
    @Override
    @Transactional
    public boolean isCompanyExists(String companyName) throws DataAccessException {
        if (StringUtil.isEmpty(companyName))
            return false;

        return getCompanyByLable(companyName) != null;
    }

    /**
     * To get company for a company id
     *
     * @param companyId Integer to get company
     * @return Company w.r.t the companyId
     * @throws DataAccessException
     */
    @Override
    public Company getCompany(Integer companyId) throws DataAccessException {
        if (companyId == null || companyId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(Company.class, companyId);
    }

    /**
     * To get user role for a role id
     *
     * @param roleId Integer to get user role
     * @return role w.r.t the roleId
     */
    @Override
    public Role getUserRole(Integer roleId) throws DataAccessException {
        if (roleId == null || roleId.intValue() < 1)
            return null;

        return getHibernateTemplate().get(Role.class, roleId);
    }

    /**
     * To get company by context root
     *
     * @param contextRoot String to get company details
     * @return Company w.r.t the contextRoot
     */
    @Override
    @Transactional
    public Company getCompanyByContextRoot(String contextRoot)
            throws DataAccessException {
        if (StringUtil.isEmpty(contextRoot))
            return null;
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria.add(Restrictions.eq("contextRoot", contextRoot));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        return (Company) hibCriteria.uniqueResult();
    }

    /**
     * To get email template by subject
     *
     * @param subject string to get EmailTemplates
     * @return EmailTemplates w.r.t the subject
     */
    @Override
    @Transactional
    public EmailTemplates getEmailTemplateBySubject(String subject)
            throws DataAccessException {
        if (StringUtil.isEmpty(subject))
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(EmailTemplates.class);
        hibCriteria.add(Restrictions.eq("emailSubject", subject));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        return (EmailTemplates) hibCriteria.uniqueResult();
    }

    /**
     * To get languages by langId list
     *
     * @param ids Array of Integer to filter languages
     * @return List of Language object's
     */
    @Override
    @Transactional
    public List<Language> getLanguagesByIds(Integer[] ids)
            throws DataAccessException {
        if (ids == null || ids.length == 0)
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Language.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.in("languageId", ids));
        return (List<Language>) hibCriteria.list();
    }

    /**
     * To get company Id by companyName
     *
     * @param companyName string to get Company
     * @return Company w.r.t the companyName
     */
    @Override
    @Transactional
    public Company getCompanyIdByName(String companyName) {
        if (StringUtil.isEmpty(companyName))
            return null;

        companyName = companyName.trim().toLowerCase();

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.sqlRestriction(
                "lower(trim(company_name)) = ?", companyName,
                new StringType()));

        return (Company) hibCriteria.uniqueResult();
    }

    /**
     * To get status Id by statusName
     *
     * @param statusName string to get Status
     * @return Status w.r.t the statusName
     */
    @Override
    @Transactional
    public Status getStatusIdByLabel(String statusName) throws DataAccessException {
        if (StringUtil.isEmpty(statusName))
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Status.class);
        hibCriteria.add(Restrictions.eq("status", statusName));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        return (Status) hibCriteria.uniqueResult();
    }

    /**
     * To get domains by domainId list
     *
     * @param ids Array of Integer to filter domains
     * @return List of Domain object's
     */
    @Override
    @Transactional
    public List<Domain> getDomainsByIds(Integer[] ids)
            throws DataAccessException {
        if (ids == null || ids.length == 0)
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Domain.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.in("domainId", ids));
        return (List<Domain>) hibCriteria.list();
    }

    /**
     * To get companies by companyId list
     *
     * @param ids Array of Integer to filter companies
     * @return List of Company object's
     */
    @Override
    @Transactional
    public List<Company> getCompaniesByIds(Integer[] ids)
            throws DataAccessException {
        if (ids == null || ids.length == 0)
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.in("companyId", ids));
        return (List<Company>) hibCriteria.list();
    }

    /**
     * To get part of speech by posId list
     *
     * @param ids Array of Integer to filter PartsOfSpeech
     * @return List of PartsOfSpeech object's
     */
    @Override
    @Transactional
    public List<PartsOfSpeech> getPartOfSpeechesByIds(Integer[] ids)
            throws DataAccessException {
        if (ids == null || ids.length == 0)
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(PartsOfSpeech.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.in("partsOfSpeechId", ids));
        return (List<PartsOfSpeech>) hibCriteria.list();
    }

    /**
     * To get category by categoryId list
     *
     * @param ids Array of Integer to filter category
     * @return List of Category object's
     */
    @Override
    @Transactional
    public List<Category> getCategoriesByIds(Integer[] ids)
            throws DataAccessException {
        if (ids == null || ids.length == 0)
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Category.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.in("categoryId", ids));
        return (List<Category>) hibCriteria.list();
    }

    /**
     * To get status list by statusId list
     *
     * @param ids Array of Integer to filter status
     * @return List of Status object's
     */
    @Override
    @Transactional
    public List<Status> getStatusesByIds(Integer[] ids)
            throws DataAccessException {
        if (ids == null || ids.length == 0)
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Status.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.in("statusId", ids));
        return (List<Status>) hibCriteria.list();
    }

    /**
     * To get product group by posId list
     *
     * @param ids Array of Integer to filter product group
     * @return List of ProductGroup object's
     */

    @Override
    @Transactional
    public List<ProductGroup> getproducGroupsByIds(Integer[] ids)
            throws DataAccessException {
        if (ids == null || ids.length == 0)
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(ProductGroup.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.in("productId", ids));
        return (List<ProductGroup>) hibCriteria.list();
    }

    /**
     * To get content type by contentId list
     *
     * @param ids Array of Integer to filter content type
     * @return List of ContentType w.r.t ids
     */
    @Override
    @Transactional
    public List<ContentType> getContentTypesByIds(Integer[] ids)
            throws DataAccessException {
        if (ids == null || ids.length == 0)
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(ContentType.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        hibCriteria.add(Restrictions.in("contentTypeId", ids));
        return (List<ContentType>) hibCriteria.list();
    }

    /**
     * To get Company List
     *
     * @param companyIds set of Ids to be filtered
     * @return List of Company's w.r.t companyIds
     */
    @Override
    @Transactional
    public List<Company> getCompanyListObj(Set<Integer> companyIds)
            throws DataAccessException {
        if (companyIds == null || companyIds.size() == 0)
            return null;

        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y")).add(
                Restrictions.in("companyId", companyIds));
        return (List<Company>) hibCriteria.list();
    }

    /**
     * To get Roles lookup data
     *
     * @return List of Roles object's
     */

    @Override
    @Transactional
    public List<Role> getRolesLookUp() throws DataAccessException {
        Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Role.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        return (List<Role>) hibCriteria.list();
    }

    /**
     * To get Role lookup  as a Map data
     *
     * @return List of Company object's
     */
    @Transactional
    public Map<Object, Role> getRolesLookUpMap() throws DataAccessException {
        List<Role> roles = getRolesLookUp();
        Map<Object, Role> roleMap = null;
        if (roles != null && !roles.isEmpty()) {
            roleMap = new HashMap<Object, Role>();
            for (Role role : roles) {
                roleMap.put(role.getRoleName(), role);
            }
        }

        return roleMap;
    }


	@Override
	  /**
     * To get the user
     *
     * @param userId Integer 
     * @return User w.r.t the userId
     */
    public String getUserNameByUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        String userName = null;
        User user = null;
        user = getHibernateTemplate().get(User.class, userId);
        if(user != null && user.getUserName() != null) {
        	userName = user.getUserName();
        }
        return userName;

    }
}