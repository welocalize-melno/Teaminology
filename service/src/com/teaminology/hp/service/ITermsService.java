package com.teaminology.hp.service;

import java.util.List;

import com.teaminology.hp.dao.exception.ApplicationException;
import com.teaminology.hp.data.TermInformationDetails;

public interface ITermsService {

	/**
	 * This API will fetch Term Information by taking termId,sourceLanguage & targetLanguage as input parameters
	 * @param termId
	 * @param sourceLanguage
	 * @param targetLanguage
	 * @return
	 */
	TermInformationDetails searchTermDetails(Integer termId, String sourceLanguage, String targetLanguage) throws ApplicationException;

	List<TermInformationDetails> getAllTerms(Integer limit,String lastChangeDate, Integer offSet,String sourceLang, String targetLang) throws ApplicationException;

	Boolean insertAuthToken(String token);

	boolean validateToken(String token);


}
