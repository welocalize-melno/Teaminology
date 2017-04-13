package com.teaminology.hp.dao.ImpExp.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.teaminology.hp.bo.GlobalsightTermInfoTO;
import com.teaminology.hp.bo.TermInformationBo;

public class GSTermBaseDAO {
	
	public static SessionFactory sessionFactoryObj = null;
	public static GSTermBaseDAO gsTermBaseDAO = null;
	protected static final String LS = System.getProperty("line.separator");
	
	String company = "companyId";
	String startPkObj = "startTmPK";
	String endPKObj = "endTmPK";

	HashMap<String/*parameterName*/, Integer/*value*/> paramMap =
			new HashMap<String, Integer>();
	
	/* protected static String GLOBALSIGHT_TERM_INFO = "select  gt.globalsight_term_info_id,gt.source_segment,gt.target_segment,fi.source_lang as source_lang,"
	            + LS
	            + " fi.target_lang as target_lang ,gt.origin,gt.company_id,fi.file_id,gt.transunit_id,gt.term_id,fi.job_id,fi.jobname,fi.task_id,fi.taskname "
	            + LS
	            + " from globalsight_term_info gt left join (file_info fi) on (fi.file_info_id= gt.file_info_id)  where gt.is_active!='N' and fi.is_active!='N' and gt.company_id =";
*/

	public GSTermBaseDAO(){
		sessionFactoryObj = HibernateSessionFactory.getSessionFactory();
	}

	public static Session getSession(){
		return sessionFactoryObj.openSession();	
	}
	
	
	 public static GSTermBaseDAO getInstance() {
	        if (gsTermBaseDAO == null) {
	        	gsTermBaseDAO = new GSTermBaseDAO();
	        }
	        return gsTermBaseDAO;
	    }
	 
	 
	 /**
	  * Get termbase data based on batchsize
	  * @param companyId
	  * @param indexFrom
	  * @param maxRecordPerBatch
	  * @return
	  */
	 public List<GlobalsightTermInfoTO> getGSTermBaseData(Integer companyId,Integer indexFrom,Integer maxRecordPerBatch,
			 Integer startPK,Integer endPK) {
		 
			List<GlobalsightTermInfoTO>  termInformation = new ArrayList<GlobalsightTermInfoTO>();
			
			StringBuilder selectClause = new StringBuilder();
			selectClause.append( "select  gt.globalsight_term_info_id,gt.source_segment,gt.target_segment,fi.source_lang as source_lang,"
		            + LS
		            + " fi.target_lang as target_lang ,gt.origin,gt.company_id,fi.file_id,gt.transunit_id,gt.term_id,fi.job_id,fi.jobname,fi.task_id,fi.taskname "
		            + LS
		            + " from globalsight_term_info gt left join (file_info fi) on (fi.file_info_id= gt.file_info_id)"
		            +LS
		            +" where gt.is_active!='N' and fi.is_active!='N' and gt.company_id =:"+company);

			paramMap.put(company,companyId);
			if(startPK!=null){
				selectClause.append(" and gt.globalsight_term_info_id >= :"+startPkObj);
				paramMap.put(startPkObj,startPK);
			}
			

			if(endPK!=null){
				selectClause.append(" and gt.globalsight_term_info_id <= :"+endPKObj);
				paramMap.put(endPKObj,endPK);
			}

			selectClause.append(" ORDER BY gt.globalsight_term_info_id ASC ");
			
			selectClause.append( " limit  " +indexFrom + " ,"+maxRecordPerBatch);
			
			Session session = getSession();
			SQLQuery hibQuery = session.createSQLQuery(selectClause.toString());
			
			if(paramMap != null && ! paramMap.isEmpty()){
				for(String key : paramMap.keySet()){
					Integer value = paramMap.get(key);
					hibQuery.setParameter(key, value);
				}
			}
			
			hibQuery.addScalar("globalsight_term_info_id", Hibernate.INTEGER);
			hibQuery.addScalar("source_segment", Hibernate.STRING);
			hibQuery.addScalar("target_segment", Hibernate.STRING);
			hibQuery.addScalar("source_lang", Hibernate.INTEGER);
			hibQuery.addScalar("target_lang", Hibernate.INTEGER);
			hibQuery.addScalar("origin", Hibernate.STRING);
			hibQuery.addScalar("company_id", Hibernate.INTEGER);
			hibQuery.addScalar("file_id", Hibernate.STRING);
			hibQuery.addScalar("transunit_id", Hibernate.INTEGER);
			hibQuery.addScalar("term_id", Hibernate.INTEGER);
			hibQuery.addScalar("job_id", Hibernate.STRING);
			hibQuery.addScalar("jobname", Hibernate.STRING);
			hibQuery.addScalar("task_id", Hibernate.STRING);
			hibQuery.addScalar("taskname", Hibernate.STRING);
			
			List<Object> hibernateResults =  (List<Object>) hibQuery.list();

			if(hibernateResults!=null && !hibernateResults.isEmpty()){

				for (Object obj : hibernateResults) {
					Object[] termInfoArrayObj = (Object[]) obj;
					int colNdx = 0;
					GlobalsightTermInfoTO termInfoObj = new GlobalsightTermInfoTO();
					if ((Integer) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setGlobalsightTermInfoId((Integer) termInfoArrayObj[colNdx]);
					}
					colNdx++;
					if ((String) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setSourceSegment((String) termInfoArrayObj[colNdx]);
					}
					colNdx++;

					if ((String) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setTargetSegment((String) termInfoArrayObj[colNdx]);
					}
					colNdx++;

					if ((Integer) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setSourceLang((Integer) termInfoArrayObj[colNdx]);
					}
					colNdx++;

					if ((Integer) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setTargetLang((Integer) termInfoArrayObj[colNdx]);
					}
					colNdx++;

					if ((String) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setOrigin((String) termInfoArrayObj[colNdx]);
					}
					colNdx++;

					if ((Integer) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setCompanyId((Integer) termInfoArrayObj[colNdx]);
					}
					colNdx++;
					if ((String) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setFileId((String) termInfoArrayObj[colNdx]);
					}
					colNdx++;
					if ((Integer) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setTransUnitId((Integer) termInfoArrayObj[colNdx]);
					}
					colNdx++;

					if ((Integer) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setTermId((Integer) termInfoArrayObj[colNdx]);
					}

					colNdx++;

					if ((String) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setJobId((String) termInfoArrayObj[colNdx]);
					}
					colNdx++;

					if ((String) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setJobName((String) termInfoArrayObj[colNdx]);
					}
					colNdx++;

					if ((String) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setTaskId((String) termInfoArrayObj[colNdx]);
					}
					colNdx++;

					if ((String) termInfoArrayObj[colNdx] != null) {
						termInfoObj.setTaskName((String) termInfoArrayObj[colNdx]);
					}
					
					termInformation.add(termInfoObj);

				}
			}
			session.close();
			return termInformation;
		}
	 
	 /**
	  * Check total terms in table
	  * and return terms count
	  * @param companyId
	  * 
	  */
	 public  Integer getTermBaseRecordCount(Integer companyId){
			String countQurey = "select count(globalsight_term_info_id) tot_rec from globalsight_term_info " +
					" where is_active='Y'  ";

			countQurey = countQurey + " and company_id = " +companyId; 
			Session session = getSession();
			SQLQuery sqlQueryObj = session.createSQLQuery(countQurey);
			sqlQueryObj.addScalar("tot_rec",Hibernate.INTEGER);
			Integer  listOfCount =(Integer)sqlQueryObj.uniqueResult();

			return listOfCount;
		}

}
