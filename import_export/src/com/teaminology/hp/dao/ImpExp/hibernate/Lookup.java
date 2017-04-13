package com.teaminology.hp.dao.ImpExp.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.teaminology.hp.bo.Category;
import com.teaminology.hp.bo.Company;
import com.teaminology.hp.bo.ContentType;
import com.teaminology.hp.bo.DeprecatedTermInformation;
import com.teaminology.hp.bo.Domain;
import com.teaminology.hp.bo.Language;
import com.teaminology.hp.bo.PartsOfSpeech;
import com.teaminology.hp.bo.ProductGroup;
import com.teaminology.hp.bo.Status;

public class Lookup {

	public static SessionFactory sessionFact = HibernateSessionFactory.getSessionFactory();
	public static Lookup lookup = null;
	private Lookup() {
		sessionFact = HibernateSessionFactory.getSessionFactory();
	}


	public  Session getSession(){
		return sessionFact.openSession();
	}


	public static Lookup getInstance() {
		if (lookup == null) {
			lookup = new Lookup();
		}
		return lookup;
	}

	/**
	 * list of language
	 * @return
	 */
	public  List<Language> getLanguages(){
		List<Language> languageList = null;
		Session sessionObj = getSession();
		Criteria hibCriteria = sessionObj.createCriteria(Language.class);
		languageList = (List<Language>)hibCriteria.list();
		return languageList;
	} 



	public  List<ProductGroup> getProductGroup(){
		List<ProductGroup>  productGrouplist= null;
		Session sessionObj = getSession();
		Criteria hibCriteria = sessionObj.createCriteria(ProductGroup.class);
		productGrouplist = (List<ProductGroup>)hibCriteria.list();
		return productGrouplist;

	} 

	public  List<Domain> getDomain(){
		List<Domain> domainList = null;
		Session sessionObj = getSession();
		Criteria hibCriteria = sessionObj.createCriteria(Domain.class);
		domainList = (List<Domain>)hibCriteria.list();
		return domainList;
	} 

	public  List<ContentType> getContentType(){
		List<ContentType> contentTypeList = null;
		Session sessionObj = getSession();
		Criteria hibCriteria = sessionObj.createCriteria(ContentType.class);
		contentTypeList =  (List<ContentType>)hibCriteria.list();
		return contentTypeList;
	} 

	public  List<PartsOfSpeech> getPartsOfSpeech(){
		List<PartsOfSpeech> partsOfSpeechList  = null; 
		Session sessionObj = getSession();
		Criteria hibCriteria = sessionObj.createCriteria(PartsOfSpeech.class);
		partsOfSpeechList =  (List<PartsOfSpeech>)hibCriteria.list();
		return partsOfSpeechList;
	} 

	public  List<Company> getCompany(){
		List<Company> companyList = null;
		Session sessionObj = getSession();
		Criteria hibCriteria = sessionObj.createCriteria(Company.class);
		hibCriteria.add(Restrictions.eq("isActive","Y"));
		companyList =  hibCriteria.list();
		return companyList;
	} 

	public  List<Status> getStatus(){
		List<Status> status = null;
		Session sessionObj = getSession();
		Criteria hibCriteria = sessionObj.createCriteria(Status.class);
		status  =  (List<Status>)hibCriteria.list();
		return status;
	} 

	public  List<Category> getCategory(){
		List<Category> category = null;
		Session sessionObj = getSession();
		Criteria hibCriteria = sessionObj.createCriteria(Category.class);
		category =  (List<Category>)hibCriteria.list();
		return category;
	} 


	public  List<Object> getDepcrecatedTerm(Integer termId){
		Session sessionObj = getSession();
		Criteria criteriaObj = sessionObj.createCriteria(DeprecatedTermInformation.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("deprecatedSource"));
		projList.add(Projections.property("deprecatedTarget"));
		criteriaObj.setProjection(projList);
		criteriaObj.add(Restrictions.eq("termInfo.termId",termId));
		List<Object> obj = (List<Object>)criteriaObj.list();
		return obj;	
	}
}

