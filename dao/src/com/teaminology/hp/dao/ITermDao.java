package com.teaminology.hp.dao;

import java.util.List;

import com.teaminology.hp.dao.exception.ApplicationException;
import com.teaminology.hp.data.TermInformationDetails;

public interface ITermDao {
	/**
	 * This API will fetch Term Information by taking termId,sourceLanguage &
	 * targetLanguage as input parameters
	 * 
	 * @param termId
	 * @param sourceLanguage
	 * @param targetLanguage
	 * @return
	 */
	TermInformationDetails searchTermDetails(Integer termId, String sourceLanguage,
			String targetLanguage) throws ApplicationException;

	List<TermInformationDetails> getAllTerms(Integer limit, String lastChangeDate,
			Integer offSet, String sourceLang, String targetLang)
			throws ApplicationException;
	
	Boolean insertToken(String token);

	boolean validateToken(String token);

	void deleteAllTokens();

}
