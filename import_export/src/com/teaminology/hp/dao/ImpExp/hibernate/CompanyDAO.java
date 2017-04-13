package com.teaminology.hp.dao.ImpExp.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.teaminology.hp.bo.Company;

public class CompanyDAO {

	public static SessionFactory  sessionFact = null;
	public static CompanyDAO  companyDAO = null;

	private CompanyDAO(){
		sessionFact = HibernateSessionFactory.getSessionFactory();
	}
	
	
	public static CompanyDAO getInstance() {
		if (companyDAO == null) {
			companyDAO = new CompanyDAO();
		}
		return companyDAO;
	}
	
	
	/**
	 * Get session 
	 * @return
	 */
	public  Session getSession(){
		return sessionFact.openSession();
	}

	/**
	 * 
	 * @return active company id
	 */
	public  Set<Integer> getActiveCompanyIds(){
		Set<Integer> companyIds = new HashSet<Integer>();
		Session sessionObj = getSession();
		Criteria criteriaObj = sessionObj.createCriteria(Company.class);
		criteriaObj.setProjection(Projections.property("companyId"));
		criteriaObj.add(Restrictions.eq("isActive","Y"));
		List<Object> list = criteriaObj.list();
		for(Object obj : list){
			companyIds.add((Integer)obj);
		}
		return companyIds;
	}

	public  Company  getCompany(Integer companyId){
		Session sessionObj = getSession();
		Criteria criteriaObj = sessionObj.createCriteria(Company.class);
		criteriaObj.add(Restrictions.eq("isActive","Y"));
		criteriaObj.add(Restrictions.eq("companyId",companyId.intValue()));
		Company  compName = (Company)criteriaObj.uniqueResult();
		return compName;
	}
}
