package com.teaminology.hp.dao.ImpExp.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

import com.teaminology.hp.ImpExp.dao.ISorlDataDAO;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermInformationBo;

public class TermbaseDAO implements ISorlDataDAO{

	public static SessionFactory sessionFactoryObj = null;
	public static TermbaseDAO indxtermsDao = null;
	
	HashMap<String/*parameterName*/, Integer/*value*/> paramMap =
			new HashMap<String, Integer>();
	
	String company = "companyParam";
	String startPkObj  = "startProfileId";
	String endPkObj = "endProfileId";
	
	protected static final String LS = System.getProperty("line.separator");
	
	public TermbaseDAO(){
		sessionFactoryObj = HibernateSessionFactory.getSessionFactory();
	}

	public static Session getSession(){
		return sessionFactoryObj.openSession();	
	}
	
	
	 public static TermbaseDAO getInstance() {
	        if (indxtermsDao == null) {
	        	indxtermsDao = new TermbaseDAO();
	        }
	        return indxtermsDao;
	    }

	@Override
	public List<TermInformationBo> getTermBaseData(Integer companyId,Integer recordIndex,Integer maxRecordPerBatch,
			Integer startPK,Integer endPK) {
		List<TermInformationBo>  termInformation = new ArrayList<TermInformationBo>();
		Session session = getSession();
		
		StringBuilder selectClause 	= new StringBuilder();
		
		selectClause.append(" select  ti.term_id,ti.term_being_polled, ti.suggested_term,ti.term_pos_id, ti.suggested_term_pos_id," +
				LS + "ti.term_category_id,ti.suggested_term_lang_id,ti.term_status_id,ti.domain_id,ti.company_id," +
				LS + " date_format(tvm.voting_expired_date, '%m/%d/%Y') as expired_date,(select count(dd.term_id) from deprecated_term_info dd" +
				LS + " where dd.term_id=ti.term_id and dd.is_active='Y') as is_deprecated from term_information ti" +
				LS + " left join (term_vote_master tvm) on (ti.term_id = tvm.term_id) where  ti.is_active='Y' and ti.is_tm is null and ti.company_id=:"+company);
		
		
		paramMap.put(company,companyId);
		if(startPK!=null){
			selectClause.append(" and ti.term_id >= :"+startPkObj);
			paramMap.put(startPkObj,startPK);
		}
		

		if(endPK!=null){
			selectClause.append(" and ti.term_id <= :"+endPkObj);
			paramMap.put(endPkObj,endPK);
		}

		selectClause.append(" ORDER BY ti.term_id ASC ");
		
		selectClause.append( " limit  " +recordIndex + " ,"+maxRecordPerBatch);
		SQLQuery hibQuery = session.createSQLQuery(selectClause.toString());
		
		if(paramMap != null && ! paramMap.isEmpty()){
			for(String key : paramMap.keySet()){
				Integer value = paramMap.get(key);
				hibQuery.setParameter(key, value);
			}
		}
		
		hibQuery.addScalar("term_id", Hibernate.INTEGER);
		hibQuery.addScalar("term_being_polled", Hibernate.STRING);
		hibQuery.addScalar("suggested_term", Hibernate.STRING);
		hibQuery.addScalar("term_pos_id", Hibernate.INTEGER);
		hibQuery.addScalar("suggested_term_pos_id", Hibernate.INTEGER);
		hibQuery.addScalar("term_category_id", Hibernate.INTEGER);
		hibQuery.addScalar("suggested_term_lang_id", Hibernate.INTEGER);
		hibQuery.addScalar("term_status_id", Hibernate.INTEGER);
		hibQuery.addScalar("domain_id", Hibernate.INTEGER);
		hibQuery.addScalar("company_id", Hibernate.INTEGER);
		hibQuery.addScalar("expired_date", Hibernate.STRING);
		hibQuery.addScalar("is_deprecated", Hibernate.STRING);
		List<Object> hibernateResults =  (List<Object>) hibQuery.list();

		if(hibernateResults!=null && !hibernateResults.isEmpty()){

			for (Object obj : hibernateResults) {
				Object[] termInfoArrayObj = (Object[]) obj;
				int colNdx = 0;
				TermInformationBo termInfoObj = new TermInformationBo();
				if ((Integer) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setTermId((Integer) termInfoArrayObj[colNdx]);
				}
				colNdx++;
				if ((String) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setTermBeingPolled((String) termInfoArrayObj[colNdx]);
				}
				colNdx++;

				if ((String) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setSuggestedTerm((String) termInfoArrayObj[colNdx]);
				}
				colNdx++;

				if ((Integer) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setTermPosId((Integer) termInfoArrayObj[colNdx]);
				}
				colNdx++;
				
				if ((Integer) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setSuggestedTermPosId((Integer) termInfoArrayObj[colNdx]);
				}
				colNdx++;

				if ((Integer) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setTermCategoryID((Integer) termInfoArrayObj[colNdx]);
				}
				colNdx++;

				if ((Integer) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setSugestedTermLangId((Integer) termInfoArrayObj[colNdx]);
				}
				colNdx++;

				if ((Integer) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setTermStatusId((Integer) termInfoArrayObj[colNdx]);
				}
				colNdx++;
				if ((Integer) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setDomainId((Integer) termInfoArrayObj[colNdx]);
				}
				colNdx++;
				if ((Integer) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setCompanyId((Integer) termInfoArrayObj[colNdx]);
				}
				colNdx++;

				if ((String) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setExpiredDate((String) termInfoArrayObj[colNdx]);
				}

				colNdx++;

				if ((String) termInfoArrayObj[colNdx] != null) {
					termInfoObj.setIsDeprecated((String) termInfoArrayObj[colNdx]);
				}
				termInformation.add(termInfoObj);

			}
		}
		session.close();
		return termInformation;
	}

	@Override
	public List<String> getTermBaseIds() {
		Session session= getSession();
		Criteria criteriaObj = session.createCriteria(TermInformation.class);
		criteriaObj.setProjection(Projections.property("termId"));
		List<Object> object = criteriaObj.list();
		
		System.out.println(object);
		return null;
	}

	public  Integer getTermBaseRecordCount(Integer companyId){
		String countQurey = "select count(term_id) tot_rec from term_information " +
				" where is_active='Y'  ";

		countQurey = countQurey + " and company_id = " +companyId; 
		Session session = getSession();
		SQLQuery sqlQueryObj = session.createSQLQuery(countQurey);
		sqlQueryObj.addScalar("tot_rec",Hibernate.INTEGER);
		Integer  listOfCount =(Integer)sqlQueryObj.uniqueResult();

		return listOfCount;
	}

}
