package com.teaminology.hp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.dao.ITermDao;
import com.teaminology.hp.dao.exception.ApplicationException;
import com.teaminology.hp.data.TermInformationDetails;
import com.teaminology.hp.service.ITermsService;

@Service
@Transactional
public class TermsService implements ITermsService{

	@Autowired
	private ITermDao termDao;

	public TermInformationDetails searchTermDetails(Integer termId,String sourceLanguage,String targetLanguage) throws ApplicationException{
		TermInformationDetails termInformationDetails= termDao.searchTermDetails(termId,sourceLanguage,targetLanguage);

		return termInformationDetails;
	}

	@Override
	public List<TermInformationDetails> getAllTerms(Integer limit,String lastChangeDate, Integer offSet,
			String sourceLang, String targetLang) throws ApplicationException{
		List<TermInformationDetails> termInformationDetailsList=termDao.getAllTerms(limit,lastChangeDate,offSet,sourceLang,targetLang) ;
		return termInformationDetailsList;
	}
	
	@Override
	public Boolean insertAuthToken(String token) {
		return termDao.insertToken(token) ;
	}

	@Override
	public boolean validateToken(String token) {
		return termDao.validateToken(token) ;
	}



}
