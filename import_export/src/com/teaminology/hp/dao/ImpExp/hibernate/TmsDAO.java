package com.teaminology.hp.dao.ImpExp.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import com.teaminology.hp.bo.FileUploadStatusImp;
import com.teaminology.hp.bo.TmProfileIndexBo;
import com.teaminology.hp.util.Util;

public class TmsDAO {

	public SessionFactory sessionFactObj = null;
	public static TmsDAO tmsDao = null;
	protected static final String LS = System.getProperty("line.separator");

	
	String company = "companyId";
	String startPkObj = "startTmPK";
	String endPKObj = "endTmPK";

	HashMap<String/*parameterName*/, Integer/*value*/> paramMap =
			new HashMap<String, Integer>();
	/*private final  String GET_TM_PROFILE_TERMS = LS
			+ " select  "
			+ LS
			+ " tm_profile_info_id , source,target,industry_domain,product_line,content_type,company,source_lang,target_lang " 
			+LS
			+" from tm_profile_info   where is_active='Y' and company="+companyId;*/
	
	private final static String TM_PROFILE_TERMS = LS
			+ " select  "
			+ LS
			+ " tm_profile_info_id , source,target,industry_domain,product_line,content_type,company,source_lang,target_lang " 
			+LS
			+" from tm_profile_info  where  ";


	private TmsDAO(){
		sessionFactObj = HibernateSessionFactory.getSessionFactory();
	}

	public static TmsDAO getInstance(){
		if(tmsDao == null){
			tmsDao = new TmsDAO();
		}
		return tmsDao;
	}


	public Session getSession(){
		return sessionFactObj.openSession();
	}

	public List<TmProfileIndexBo> getTmsData(Integer companyId ,Integer recordIndex,Integer maxRecordPerBatch,
			Integer startPk,Integer endPK) {

		StringBuilder selectClause = new StringBuilder();
		selectClause.append(LS
				+ " select  "
				+ LS
				+ " tm_profile_info_id , source,target,industry_domain,product_line,content_type,company,source_lang,target_lang " 
				+LS
				+" from tm_profile_info   where is_active='Y' and company=:"+company);

		paramMap.put(company,companyId);
		if(startPk!=null){
			selectClause.append(" and tm_profile_info_id >= :"+startPkObj);
			paramMap.put(startPkObj,startPk);
		}
		

		if(endPK!=null){
			selectClause.append(" and tm_profile_info_id <= :"+endPKObj);
			paramMap.put(endPKObj,endPK);
		}

		selectClause.append(" ORDER BY tm_profile_info_id ASC ");
		
		selectClause.append( " limit  " +recordIndex + " ,"+maxRecordPerBatch);
		
		List<TmProfileIndexBo> tmsProfileList = new ArrayList<TmProfileIndexBo>();
		Session session = getSession();
		
		SQLQuery hibQuery = session.createSQLQuery(selectClause.toString());
		
		if(paramMap != null && ! paramMap.isEmpty()){
			for(String key : paramMap.keySet()){
				Integer value = paramMap.get(key);
				hibQuery.setParameter(key, value);
			}
		}
		
		
		hibQuery.addScalar("TM_PROFILE_INFO_ID", Hibernate.INTEGER);
		hibQuery.addScalar("SOURCE", Hibernate.STRING);
		hibQuery.addScalar("TARGET", Hibernate.STRING);
		hibQuery.addScalar("INDUSTRY_DOMAIN", Hibernate.INTEGER);
		hibQuery.addScalar("PRODUCT_LINE", Hibernate.INTEGER);
		hibQuery.addScalar("CONTENT_TYPE", Hibernate.INTEGER);
		hibQuery.addScalar("COMPANY", Hibernate.INTEGER);
		hibQuery.addScalar("SOURCE_LANG", Hibernate.INTEGER);
		hibQuery.addScalar("TARGET_LANG", Hibernate.INTEGER);
		List<Object> tmsObjlist = (List<Object>)hibQuery.list();

		if(tmsObjlist!=null && !tmsObjlist.isEmpty()){
			for (Object obj : tmsObjlist) {
				Object[] tmProfileBoObj = (Object[]) obj;
				int colNdx = 0;
				TmProfileIndexBo tmProfileBo = new TmProfileIndexBo();
				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setTmProfileId(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;
				if ((String) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setSource((String) tmProfileBoObj[colNdx]);
				}
				colNdx++;

				if ((String) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setTarget((String) tmProfileBoObj[colNdx]);
				}
				colNdx++;

				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setDomain(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;

				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setProductLine(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;

				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setContentType(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;

				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setComanyId(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;

				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setSourceLang(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;
				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setTargetLang(tmProfileBoObj[colNdx].toString());
				}
				tmsProfileList.add(tmProfileBo);

			}
		}
		return tmsProfileList;
	}
/*
	public List<TmProfileBo> getTmProfileInfoByIds(Set<Integer> ids) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(TmProfileBo.class);
		criteria.add(Restrictions.in("tmProfileInfoId", ids));
		return (List<TmProfileBo>) criteria.list();
	}
	*/
	
	public List<TmProfileIndexBo> getTmProfileInfoByIds(Set<Integer> ids) {
		
		List<TmProfileIndexBo> tmsProfileList = new ArrayList<TmProfileIndexBo>();
		Session session = getSession();

		String inQuery = Util.generateInQuery(ids, "tm_profile_info_id");
		String query = TM_PROFILE_TERMS+ inQuery;
		SQLQuery hibQuery = session.createSQLQuery(query);
		hibQuery.addScalar("TM_PROFILE_INFO_ID", Hibernate.INTEGER);
		hibQuery.addScalar("SOURCE", Hibernate.STRING);
		hibQuery.addScalar("TARGET", Hibernate.STRING);
		hibQuery.addScalar("INDUSTRY_DOMAIN", Hibernate.INTEGER);
		hibQuery.addScalar("PRODUCT_LINE", Hibernate.INTEGER);
		hibQuery.addScalar("CONTENT_TYPE", Hibernate.INTEGER);
		hibQuery.addScalar("COMPANY", Hibernate.INTEGER);
		hibQuery.addScalar("SOURCE_LANG", Hibernate.INTEGER);
		hibQuery.addScalar("TARGET_LANG", Hibernate.INTEGER);
		List<Object> tmsObjlist = (List<Object>)hibQuery.list();
		
		if(tmsObjlist!=null && !tmsObjlist.isEmpty()){
			for (Object obj : tmsObjlist) {
				Object[] tmProfileBoObj = (Object[]) obj;
				int colNdx = 0;
				TmProfileIndexBo tmProfileBo = new TmProfileIndexBo();
				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setTmProfileId(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;
				if ((String) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setSource((String) tmProfileBoObj[colNdx]);
				}
				colNdx++;

				if ((String) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setTarget((String) tmProfileBoObj[colNdx]);
				}
				colNdx++;

				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setDomain(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;

				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setProductLine(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;

				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setContentType(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;

				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setComanyId(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;

				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setSourceLang(tmProfileBoObj[colNdx].toString());
				}
				colNdx++;
				if ((Integer) tmProfileBoObj[colNdx] != null) {
					tmProfileBo.setTargetLang(tmProfileBoObj[colNdx].toString());
				}
				tmsProfileList.add(tmProfileBo);

			}
		}
		return tmsProfileList;
	}

	public FileUploadStatusImp getFileUploadStatus(Integer fileId)  throws DataAccessException {
		if (fileId == null) {
			throw new IllegalArgumentException("Invalid file Upload");
		}
		Session session = getSession();
		FileUploadStatusImp fileData = (FileUploadStatusImp) session.get(FileUploadStatusImp.class, fileId);
		return fileData;
	}

	public void updateFileUploadStatus(FileUploadStatusImp fileUpload) {
		if (fileUpload == null) {
			return;
		}
		Session session = getSession();
		session.update(fileUpload);
	}

	public  Integer getTMRecordCount(Integer companyId){
		String countQurey = "select count(tm_profile_info_id) tot_rec from tm_profile_info " +
				" where is_active='Y'  ";

		countQurey = countQurey + " and company = " +companyId; 
		Session session = getSession();
		SQLQuery sqlQueryObj = session.createSQLQuery(countQurey);
		sqlQueryObj.addScalar("tot_rec",Hibernate.INTEGER);
		Integer  listOfCount =(Integer)sqlQueryObj.uniqueResult();

		return listOfCount;
	}

}
