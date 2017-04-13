package com.teaminology.hp.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.AuthorizationToken;
import com.teaminology.hp.bo.DeprecatedTermInformation;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermVoteMaster;
import com.teaminology.hp.bo.lookup.Domain;
import com.teaminology.hp.bo.lookup.Form;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.bo.lookup.PartsOfSpeech;
import com.teaminology.hp.bo.lookup.Status;
import com.teaminology.hp.dao.HibernateDAO;
import com.teaminology.hp.dao.ILookUpDAO;
import com.teaminology.hp.dao.ITermDao;
import com.teaminology.hp.dao.exception.ApplicationException;
import com.teaminology.hp.data.DepricatedTermDetails;
import com.teaminology.hp.data.TermInformationDetails;

public class TermsDao extends HibernateDAO implements ITermDao {

	private static final Logger logger = Logger.getLogger(TermsDao.class);

	@Autowired
	private ILookUpDAO lookUpDAO;

	private static final String NOT_APPLICABLE="N/A";
	
	
	@Transactional
	public TermInformationDetails searchTermDetails(Integer termId,
			String sourceLanguage, String targetLanguage)
					throws ApplicationException {
		logger.info("entered into searchTermDetails API");
		TermInformationDetails termInformationDetails = null;
		TermInformation termInformation = null;
		Integer srcLangId = 0;
		Integer tarLangId = 0;
		Language srcLanguage = null, tarLanguage = null;
		Session session = null;
		Criteria criteria = null;
		List<Language> srcLanguageList = null;
		Criteria targetcriteria = null;
		List<Language> targetLanguageList = null;
		List<TermInformation> termInformationsList = null;
		ApplicationException ae = null;
		try {
			session = getHibernateSession();
			if (sourceLanguage != null && !sourceLanguage.trim().isEmpty()) {
				criteria = session.createCriteria(Language.class).add(
						Restrictions.eq("languageCode",
								sourceLanguage.toUpperCase()));

				srcLanguageList = (List<Language>) criteria.list();
				logger.debug(srcLanguageList);
				if (srcLanguageList != null && srcLanguageList.size() > 0) {
					srcLanguage = srcLanguageList.get(0);
					srcLangId = srcLanguage.getLanguageId();
				}
			}

			if (targetLanguage != null && !targetLanguage.trim().isEmpty()) {
				targetcriteria = session.createCriteria(Language.class).add(
						Restrictions.eq("languageCode",
								targetLanguage.toUpperCase()));

				targetLanguageList = (List<Language>) targetcriteria.list();
				logger.debug(targetLanguageList);
				if (targetLanguageList != null && targetLanguageList.size() > 0) {
					tarLanguage = targetLanguageList.get(0);
					tarLangId = tarLanguage.getLanguageId();
				}
			}

			Criteria termInfoCriteria = session
					.createCriteria(TermInformation.class);
			termInfoCriteria = termInfoCriteria.add(Restrictions.eq("termId",
					termId));
			if (srcLangId != 0) {
				termInfoCriteria = termInfoCriteria.add(Restrictions.eq(
						"termLangId", srcLangId));
			}
			if (tarLangId != 0) {

				if (srcLangId == 0) {
					ae = new ApplicationException();
					ae.setErrorMessage("provide proper sourceLang");
					logger.error(ae);
					throw ae;
				}
				termInfoCriteria = termInfoCriteria.add(Restrictions.eq(
						"suggestedTermLangId", tarLangId));
			}

			termInfoCriteria.add(Restrictions.eq("isActive", "Y"));
			termInformationsList = (List<TermInformation>) termInfoCriteria
					.list();
			logger.debug(termInformationsList);
			if (termInformationsList != null && termInformationsList.size() > 0) {
				termInformation = termInformationsList.get(0);
				if(termInformation!=null)
				termInformationDetails=getTermInformationDetailsResponse(session, srcLanguage, tarLanguage, termInformation);
				}else {
				ae = new ApplicationException();
				ae.setErrorMessage("provided fields does not match the search criteria");
				logger.error(ae);
				throw ae;
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.info("exit from searchTermDetails API");
		return termInformationDetails;
	}

	
	
	@Transactional
	public List<TermInformationDetails> getAllTerms(Integer limit,
			String lastChangeDate, Integer offSet, String sourceLang,
			String targetLang) throws ApplicationException {
		logger.info("entered into getAllTerms API");
		List<TermInformationDetails> termInformationDetailsList = null;
		Session session = null;
		Integer srcLangId = 0;
		Integer tarLangId = 0;
		Language srcLanguage = null, tarLanguage = null;
		List<Language> srcLanguageList = null;
		List<Language> targetLanguageList = null;
		Criteria srcCriteria = null;
		Criteria targetCriteria = null;
		List<TermInformation> termInformationsList = null;
		ApplicationException ae = null;
		try {
			session = getHibernateSession();
			// Get Source language id by source language name
			if (sourceLang != null && !sourceLang.trim().isEmpty()) {
				srcCriteria = session.createCriteria(Language.class).add(
						Restrictions.eq("languageCode",
								sourceLang.toUpperCase()));

				srcLanguageList = (List<Language>) srcCriteria.list();
				logger.debug(srcLanguageList);
				if (srcLanguageList != null && srcLanguageList.size() > 0) {
					srcLanguage = srcLanguageList.get(0);
					srcLangId = srcLanguage.getLanguageId();
				}
			}
			// Get target language id by target language name
			if (targetLang != null && !targetLang.trim().isEmpty()) {
				targetCriteria = session.createCriteria(Language.class).add(
						Restrictions.eq("languageCode",
								targetLang.toUpperCase()));

				targetLanguageList = (List<Language>) targetCriteria.list();
				logger.debug(targetLanguageList);
				if (targetLanguageList != null && targetLanguageList.size() > 0) {
					tarLanguage = targetLanguageList.get(0);
					tarLangId = tarLanguage.getLanguageId();
				}
			}

			if (limit >= 0 /* && offSet>=0 */) {
				Criteria termInfoCriteria = session
						.createCriteria(TermInformation.class);

				if (tarLangId != 0 && srcLangId != 0) {
					termInfoCriteria = termInfoCriteria.add(Restrictions.eq(
							"termLangId", srcLangId));
					termInfoCriteria = termInfoCriteria.add(Restrictions.eq(
							"suggestedTermLangId", tarLangId));
				} else if (srcLangId != 0 && tarLangId == 0) {
					termInfoCriteria = termInfoCriteria.add(Restrictions.eq(
							"termLangId", srcLangId));
				} else if (srcLangId == 0 && tarLangId != 0) {
					ae = new ApplicationException();
					ae.setErrorMessage("sourceLang provided is invalid");
					logger.debug(ae);
					throw ae;
				}

				termInfoCriteria.setFirstResult(offSet);
				termInfoCriteria.setMaxResults(limit);
				termInfoCriteria.add(Restrictions.eq("isActive", "Y"));
				
				if (lastChangeDate != null && !lastChangeDate.equals("")) {
					SimpleDateFormat originalDateFormat = new SimpleDateFormat(
							"yyyyMMdd'T'HHmm");
					SimpleDateFormat targetDateFormat = new SimpleDateFormat(
							"YYYY-MM-dd");
					String resultDate = targetDateFormat.format(originalDateFormat.parse(lastChangeDate));
					
				termInfoCriteria.add(Restrictions.sqlRestriction("update_date between '" + resultDate + "' AND NOW() "));
					
				}
				termInformationsList = (List<TermInformation>) termInfoCriteria
						.list();
				logger.debug("Response from DAO :" + termInformationsList);
				if (termInformationsList == null
						|| termInformationsList.size() == 0) {
					ae = new ApplicationException();
					ae.setErrorMessage("provided fields does not match the search criteria");
					logger.error(ae);
					throw ae;
				} else {
					termInformationDetailsList = new ArrayList<TermInformationDetails>();

					for (TermInformation termInformation : termInformationsList) {

						TermInformationDetails termInformationDetails=getTermInformationDetailsResponse(session,srcLanguage, tarLanguage, termInformation);
						termInformationDetailsList.add(termInformationDetails);
						termInformationDetails=null;
					}
			     }
				}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.info("exit from getAllTerms API");
		return termInformationDetailsList;
	}

	/**
	 * @param termInformationDetailsList
	 * @param session
	 * @param srcLanguage
	 * @param tarLanguage
	 * @param termInformation
	 */
	private TermInformationDetails getTermInformationDetailsResponse(
			Session session, Language srcLanguage, Language tarLanguage,
			TermInformation termInformation) {
		TermInformationDetails termInformationDetails1 = new TermInformationDetails();
		Criteria criteria = null;
		Criteria targetcriteria = null;
		Language sourceLanguage, targetLanguage = null;
		List<Language> sourceLanguageList = null;
		List<Language> targLanguageList = null;
		if(termInformation.getTermId()!=null)
		termInformationDetails1.setTermId(termInformation.getTermId());
		if (termInformation.getConceptDefinition() != null && !termInformation.getConceptDefinition().isEmpty()){
			termInformationDetails1.setConceptDefinition(termInformation.getConceptDefinition().trim());
		}else{
			termInformationDetails1.setConceptDefinition(NOT_APPLICABLE);
		}	
		if (termInformation.getTermUsage() != null && !termInformation.getTermUsage().isEmpty()){
			termInformationDetails1.setTermUsage(termInformation.getTermUsage().trim());
			termInformationDetails1.setContextualExample(termInformation.getTermUsage().trim());
		}else{
			termInformationDetails1.setTermUsage(NOT_APPLICABLE);	
			termInformationDetails1.setContextualExample(NOT_APPLICABLE);
		}
		if (termInformation.getTermNotes() != null && !termInformation.getTermNotes().isEmpty()){
			termInformationDetails1.setTermNotes(termInformation.getTermNotes().trim());
		}else{
			termInformationDetails1.setTermNotes(NOT_APPLICABLE);
		}

		if(termInformation.getTermBeingPolled()!=null && !termInformation.getTermBeingPolled().isEmpty()){
			termInformationDetails1.setSourceTerm(termInformation.getTermBeingPolled().trim());
		}else{
			termInformationDetails1.setSourceTerm(NOT_APPLICABLE);	
		}
		if (srcLanguage != null && srcLanguage.getLanguageCode()!=null && !srcLanguage.getLanguageCode().isEmpty()) {
			termInformationDetails1.setIsoSourceLang(srcLanguage.getLanguageCode().trim());
		} else {
			criteria = session.createCriteria(Language.class).add(Restrictions.eq("languageId",termInformation.getTermLangId()));
			sourceLanguageList = criteria.list();
			logger.debug(sourceLanguageList);
			if (sourceLanguageList != null && sourceLanguageList.size() > 0) {
				sourceLanguage = sourceLanguageList.get(0);
				if(sourceLanguage.getLanguageCode()!=null && !sourceLanguage.getLanguageCode().isEmpty())
					termInformationDetails1.setIsoSourceLang(sourceLanguage.getLanguageCode().trim());
				else
					termInformationDetails1.setIsoSourceLang(NOT_APPLICABLE);	
			}else{
				termInformationDetails1.setIsoSourceLang(NOT_APPLICABLE);	
			}

		}
		if(termInformation.getSuggestedTerm()!=null && !termInformation.getSuggestedTerm().isEmpty()){
			termInformationDetails1.setTargetterm(termInformation.getSuggestedTerm().trim());
		}else{
			termInformationDetails1.setTargetterm(NOT_APPLICABLE);
		}
		if (tarLanguage != null && tarLanguage.getLanguageCode()!=null && !tarLanguage.getLanguageCode().isEmpty()) {
			termInformationDetails1.setIsotargetLang(tarLanguage.getLanguageCode().trim());
		} else {
			targetcriteria = session.createCriteria(Language.class)
					.add(Restrictions.eq("languageId",
							termInformation.getSuggestedTermLangId()));
			targLanguageList = targetcriteria.list();
			logger.debug(targLanguageList);
			if (targLanguageList != null && targLanguageList.size() > 0) {
				targetLanguage = targLanguageList.get(0);
				if(targetLanguage.getLanguageCode()!=null && targetLanguage.getLanguageCode()!=null && !targetLanguage.getLanguageCode().isEmpty())
					termInformationDetails1.setIsotargetLang(targetLanguage.getLanguageCode().trim());
				else
					termInformationDetails1.setIsotargetLang(NOT_APPLICABLE);	
			}else{
				termInformationDetails1.setIsotargetLang(NOT_APPLICABLE);	
			}
			
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd");
		if (termInformation.getCreateDate() != null)
			termInformationDetails1.setDateCreated(simpleDateFormat.format(termInformation.getCreateDate()).trim());
		else
			termInformationDetails1.setDateCreated(NOT_APPLICABLE);
		if (termInformation.getUpdateDate() != null)
			termInformationDetails1.setDateUpdated(simpleDateFormat.format(termInformation.getUpdateDate()).trim());
		else
			termInformationDetails1.setDateUpdated(NOT_APPLICABLE);
		if(termInformation.getTermPOS()!=null){
		PartsOfSpeech srcPartsOfSpeech = lookUpDAO.getPartsOfSpeech(termInformation.getTermPOS().getPartsOfSpeechId());
		if (srcPartsOfSpeech != null) {
			if (srcPartsOfSpeech.getPartOfSpeech() != null && !srcPartsOfSpeech.getPartOfSpeech().isEmpty())
				termInformationDetails1.setSourcePos(srcPartsOfSpeech.getPartOfSpeech().trim());
			else
				termInformationDetails1.setSourcePos(NOT_APPLICABLE);	
		}
		}
		if (termInformation.getSuggestedTermPosId() != null) {
			PartsOfSpeech targPartsOfSpeech = lookUpDAO.getPartsOfSpeech(termInformation.getSuggestedTermPosId());
			if (targPartsOfSpeech != null) {
				if (targPartsOfSpeech.getPartOfSpeech() != null && !targPartsOfSpeech.getPartOfSpeech().isEmpty())
					termInformationDetails1.setTargetPos(targPartsOfSpeech.getPartOfSpeech().trim());
				else
					termInformationDetails1.setTargetPos(NOT_APPLICABLE);	
			}
		}else{
			termInformationDetails1.setTargetPos(NOT_APPLICABLE);	
		}
		Form form = null;
		if (termInformation.getTermForm() != null && termInformation.getTermForm().getFormId() != null) {
			form = lookUpDAO.getForm(termInformation.getTermForm().getFormId());
			if (form!=null && form.getFormName() != null)
				termInformationDetails1.setForm(form.getFormName().trim());
			else
				termInformationDetails1.setForm(NOT_APPLICABLE);
		}else{
			termInformationDetails1.setForm(NOT_APPLICABLE);	
		}
		if (termInformation.getTermCategory() != null && termInformation.getTermCategory().getCategory() != null && !termInformation.getTermCategory().getCategory().isEmpty())
			termInformationDetails1.setTermCategory(termInformation.getTermCategory().getCategory().trim());
		else
			termInformationDetails1.setTermCategory(NOT_APPLICABLE);
		Domain domain = null;
		if (termInformation.getTermDomain() != null && termInformation.getTermDomain().getDomainId() != null) {
			domain = lookUpDAO.getDomain(termInformation.getTermDomain().getDomainId());
			if (domain != null && domain.getDomain() != null)
				termInformationDetails1.setDomain(domain.getDomain().trim());
			else
				termInformationDetails1.setDomain(NOT_APPLICABLE);
		}
		List<TermVoteMaster> termVoteCritList = null;
		if (termInformation.getTermId() != null) {
			Criteria termVoteCrit = session.createCriteria(TermVoteMaster.class);
			termVoteCrit.add(Restrictions.eq("termId",termInformation.getTermId()));
			termVoteCritList = termVoteCrit.list();
			TermVoteMaster termVoteMaster = null;
			if (termVoteCritList != null && termVoteCritList.size() > 0) {
				termVoteMaster = termVoteCritList.get(0);
				if (termVoteMaster != null && termVoteMaster.getVotingExpiredDate() != null)
					termInformationDetails1.setPollExpiration(simpleDateFormat.format(termVoteMaster.getVotingExpiredDate()).trim());
			}
		}else{
			termInformationDetails1.setPollExpiration(NOT_APPLICABLE);
		}

		if (termInformation.getTermStatusId() != null) {
			Status status = lookUpDAO.getStatus(termInformation.getTermStatusId());
			if (status != null) {
				if (status.getStatusId() == 2)
					termInformationDetails1.setState("Final".trim());
				else
					termInformationDetails1.setState(NOT_APPLICABLE);
				if(status.getStatus()!=null && !status.getStatus().isEmpty())
					termInformationDetails1.setStatus(status.getStatus().trim());
				else
					termInformationDetails1.setStatus(NOT_APPLICABLE);	
			}
		}else{
			termInformationDetails1.setState(NOT_APPLICABLE);
			termInformationDetails1.setStatus(NOT_APPLICABLE);
		}

		if (termInformation.getDeprecatedTermInfo() != null && termInformation.getDeprecatedTermInfo().size() > 0) {
			Set<DeprecatedTermInformation> deprecatedTermInformations = termInformation.getDeprecatedTermInfo();
			List<DepricatedTermDetails> depricatedTermDetailsList = new ArrayList<DepricatedTermDetails>();
			for (DeprecatedTermInformation deprecatedTermInformation : deprecatedTermInformations) {
				DepricatedTermDetails depricatedTermDetails = new DepricatedTermDetails();
				if (deprecatedTermInformation.getDeprecatedSource() != null && !deprecatedTermInformation.getDeprecatedSource().isEmpty())
					depricatedTermDetails.setDepricatedSourceTerm(deprecatedTermInformation.getDeprecatedSource().trim());
				else
					depricatedTermDetails.setDepricatedSourceTerm(NOT_APPLICABLE);	
				if (deprecatedTermInformation.getDeprecatedTarget() != null && !deprecatedTermInformation.getDeprecatedTarget().isEmpty())
					depricatedTermDetails.setDepricatedTargetTerm(deprecatedTermInformation.getDeprecatedTarget().trim());
				else
					depricatedTermDetails.setDepricatedTargetTerm(NOT_APPLICABLE);
				depricatedTermDetailsList.add(depricatedTermDetails);
			}
			
			termInformationDetails1.setDepricatedTermDetailsList(depricatedTermDetailsList);
		}
		return termInformationDetails1;
	}

	@Transactional
	public Boolean insertToken(String token) {
		logger.info("Method to insert auth token");
		Session session = null;
		Boolean status = false;
		try {
			session = getHibernateSession();
			/*
			 * SQLQuery insertQuery = session.createSQLQuery("" +
			 * "insert into authorization_token(token)VALUES(?)");
			 * insertQuery.setParameter(0, token); int
			 * rowsCopied=insertQuery.executeUpdate();
			 */
			Date dt = new Date();

			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(dt);

			dt = sdf.parse(date);
			AuthorizationToken authorizationToken = null;
			if (token != null && !token.trim().isEmpty()) {
				authorizationToken = new AuthorizationToken();
				authorizationToken.setAuthToken(token);
				authorizationToken.setCreatedDate(dt);
				Integer rowsCopied = (Integer) session.save(authorizationToken);
				logger.info("exit from getAllTerms API");
				status = (rowsCopied > 0) ? true : false;
			}
		} catch (Exception e) {
			logger.error("error : " + e);

		}
		return status;

	}

	@Transactional
	public boolean validateToken(String token) {
		logger.info("Method to validate token");
		Session session = null;
		Boolean status = false;
		try {
			session = getHibernateSession();
			Query query = session
					.createSQLQuery(
							"select token from authorization_token WHERE deleted_date IS NULL AND token = :token")
							.setParameter("token", token);
			status = (query.list().size() > 0) ? true : false;
			logger.info("exit from validateToken method");
		} catch (Exception e) {
			logger.error("error : " + e);
		}
		return status;
	}

	@Transactional
	public void deleteAllTokens() {
		logger.info("Method to validate token");
		Session session = null;
		Boolean status = false;
		try {
			session = getHibernateSession();
			Date dt = new Date();

			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(dt);

			dt = sdf.parse(date);
			/*
			 * Query query =session.createSQLQuery(
			 * "update authorization_token set deleted_date = :date where deleted_date IS NULL"
			 * ).setParameter("date",sdf.format(dt));
			 */

			Query query = session.createQuery(
					"update AuthorizationToken at set at.deletedDate=:date")
					.setParameter("date", dt);
			logger.info(" status for delete " + query.executeUpdate());
			logger.info("exit from validateToken method");
		} catch (Exception e) {
			logger.error("error : " + e);
		}

	}

}
