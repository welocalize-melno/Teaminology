package com.teaminology.hp.dao.ImpExp.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.teaminology.hp.bo.Company;
import com.teaminology.hp.bo.ContentType;
import com.teaminology.hp.bo.Domain;
import com.teaminology.hp.bo.FileUploadStatusImp;
import com.teaminology.hp.bo.Language;
import com.teaminology.hp.bo.ProductGroup;
import com.teaminology.hp.bo.TMProperties;
import com.teaminology.hp.bo.TmProfileBo;
import com.teaminology.hp.util.ImportExportProperty;


public class ExportDAOImpl
{
    private static Logger logger = Logger.getLogger(ExportDAOImpl.class);
    static SessionFactory sessFact = null;
    protected static final String LS = System.getProperty("line.separator");


    private final static String GET_TM_PROFILE_TERMS = LS
            + " select  "
            + LS
            + " tm_profile_info_id , source,target,industry_domain,product_line,content_type,company,source_lang,target_lang ,prop_ref,  "
            + LS
            + "  tu_id, tu_creation_id, tu_creation_date, tu_change_id, tu_change_date, tuv_source_creation_id,"
            + LS
            + " tuv_source_creation_date, tuv_source_change_id, tuv_source_change_date, tuv_target_creation_id, tuv_target_creation_date, tuv_target_change_id ,"
            + LS
            + " tuv_target_change_date from tm_profile_info   where is_active='Y'  ";

    private final static String GET_TM_TU_PROPERTIES = LS
            + " select  "
            + LS
            + " description , type , tm_profile_info_id, is_tu,is_tuv,is_tuv_target,is_tuv_source, prop_ref "
            + LS
            + " from tm_properties where  ";

    public static ExportDAOImpl exportDAOImpl = null;

    private ExportDAOImpl() {
        sessFact = HibernateSessionFactory.getSessionFactory();
    }

    public static ExportDAOImpl getInstance() {
        if (exportDAOImpl == null) {
            exportDAOImpl = new ExportDAOImpl();
        }
        return exportDAOImpl;
    }

    @SuppressWarnings("unchecked")
    public List<TmProfileBo> ifSourceExists(String source,
                                            Integer targetLang, Session session) {

        Criteria criteria = session.createCriteria(TmProfileBo.class);
        criteria.add(Restrictions.eq("source", source));
        criteria.add(Restrictions.eq("targetLang", targetLang));
        criteria.add(Restrictions.eq("isActive", "Y"));
        List<TmProfileBo> tmprofileBoList = (List<TmProfileBo>) criteria.list();
        return tmprofileBoList;
    }

    @SuppressWarnings("unchecked")
    public List<TmProfileBo> getTotalTermsInTM(String exportBy, String selectedId, Session session, 
    		                String companyId, Integer recordIndex, Integer limit) {

    	       List<TmProfileBo> tmProfileBoList = new ArrayList<TmProfileBo>();
    		   StringBuffer query = new StringBuffer();
    			
    			query.append(GET_TM_PROFILE_TERMS);
    			if (exportBy.equalsIgnoreCase("Locale")) {
    				query.append(" and target_lang ");
    			} else if (exportBy.equalsIgnoreCase("IndustryDomain")) {
    				query.append(" and industry_domain ");
    			} else if (exportBy.equalsIgnoreCase("ProductLine")) {
    				query.append(" and product_line ");
    			} else if (exportBy.equalsIgnoreCase("ContentType")) {
    				query.append(" and content_type ");
    			} else if (exportBy.equalsIgnoreCase("Company")) {
    				query.append(" and company ");
    			}
    			query.append(" = "+selectedId);

    			if (companyId != null) {
    				query.append(" and company =" + companyId);
    			}

    			query.append(" limit  " +recordIndex.toString());
    			query.append(" ,"+limit);


    			SQLQuery hibQuery = session.createSQLQuery(query.toString());
    			hibQuery.addScalar("TM_PROFILE_INFO_ID", Hibernate.INTEGER);
    			hibQuery.addScalar("SOURCE", Hibernate.STRING);
    			hibQuery.addScalar("TARGET", Hibernate.STRING);
    			hibQuery.addScalar("INDUSTRY_DOMAIN", Hibernate.INTEGER);
    			hibQuery.addScalar("PRODUCT_LINE", Hibernate.INTEGER);
    			hibQuery.addScalar("CONTENT_TYPE", Hibernate.INTEGER);
    			hibQuery.addScalar("COMPANY", Hibernate.INTEGER);
    			hibQuery.addScalar("SOURCE_LANG", Hibernate.INTEGER);
    			hibQuery.addScalar("TARGET_LANG", Hibernate.INTEGER);
    			hibQuery.addScalar("PROP_REF", Hibernate.STRING);
    			hibQuery.addScalar("TU_ID", Hibernate.STRING);
    			hibQuery.addScalar("TU_CREATION_ID", Hibernate.STRING);
    			hibQuery.addScalar("TU_CREATION_DATE", Hibernate.STRING);
    			hibQuery.addScalar("TU_CHANGE_ID", Hibernate.STRING);
    			hibQuery.addScalar("TU_CHANGE_DATE", Hibernate.STRING);
    			hibQuery.addScalar("TUV_SOURCE_CREATION_ID", Hibernate.STRING);
    			hibQuery.addScalar("TUV_SOURCE_CREATION_DATE", Hibernate.STRING);
    			hibQuery.addScalar("TUV_SOURCE_CHANGE_ID", Hibernate.STRING);
    			hibQuery.addScalar("TUV_SOURCE_CHANGE_DATE", Hibernate.STRING);
    			hibQuery.addScalar("TUV_TARGET_CREATION_ID", Hibernate.STRING);
    			hibQuery.addScalar("TUV_TARGET_CREATION_DATE", Hibernate.STRING);
    			hibQuery.addScalar("TUV_TARGET_CHANGE_ID", Hibernate.STRING);
    			hibQuery.addScalar("TUV_TARGET_CHANGE_DATE", Hibernate.STRING);

    			List<Object> hibernateResults = hibQuery.list();

    			for (Object obj : hibernateResults) {
    				Object[] tmProfileBoObj = (Object[]) obj;
    				int colNdx = 0;
    				TmProfileBo tmProfileBo = new TmProfileBo();
    				if ((Integer) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTmProfileInfoId((Integer) tmProfileBoObj[colNdx]);
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
    					tmProfileBo.setDomainId((Integer) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;

    				if ((Integer) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setProductGroupId((Integer) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;

    				if ((Integer) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setContentTypeId((Integer) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;

    				if ((Integer) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setCompanyId((Integer) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;

    				if ((Integer) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setSourceLang((Integer) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((Integer) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTargetLang((Integer) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setPropRef((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;

    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuId((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuCreationId((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuCreationDate((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuChangeId((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuChangeDate((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuSourceCreationId((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuSourceCreationDate((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuSourceChangeId((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuSourceChangeDate((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuTargetCreationId((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuTargetCreationDate((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuTargetChangeId((String) tmProfileBoObj[colNdx]);
    				}
    				colNdx++;
    				if ((String) tmProfileBoObj[colNdx] != null) {
    					tmProfileBo.setTuTargetChangeDate((String) tmProfileBoObj[colNdx]);
    				}

    				tmProfileBoList.add(tmProfileBo);

    			}
    			query.delete(0, query.length());

    	return tmProfileBoList;
}

    @SuppressWarnings("unchecked")
    public List<TMProperties> getTMPropertiesByRef(String propRef, Session session) {
        // Session session=getSession();
        Criteria hibCriteria = session.createCriteria(TMProperties.class);
        hibCriteria.add(Restrictions.eq("propRef", propRef));
        hibCriteria.add(Restrictions.eq("isTu", "N"));
        //hibCriteria.add(Expression.isNull("tmProfileId"));

        List<TMProperties> tmPropertiesList = (List<TMProperties>) hibCriteria.list();
        //session.close();
        return tmPropertiesList;
    }

    /*public void getTupropertiesUsingTMProfileId(List<Integer> tmProfileIds,Session session
            ,Map<Integer,List<TMProperties>> tuPropertiesListMap , Map<Integer,List<TMProperties>> tuvSourcePropertiesListMap
            ,Map<Integer,List<TMProperties>> tuvTargetPropertiesListMap , Map<String,List<TMProperties>> headerPropertiesListMap ){
        StringBuffer query = new StringBuffer();

        List<List<Integer>> batchList=createBatches(tmProfileIds,1000);
        for(List<Integer> batch : batchList){
            query.append(GET_TM_TU_PROPERTIES);
            String values="";
            for(Integer id : batch){
                values=values+id+" , ";
            }
            values=values.substring(0,values.lastIndexOf(",")-1);
            query.append(" tm_profile_info_id in ("+values+")");

            if(isTu){
                query.append(" and is_tu ='Y' ");
            }else if(isTuvSource){
                query.append(" and is_tuv ='Y' and is_tuv_source ='Y' ");
            }else if(isTuvTarget){
                query.append(" and is_tuv ='Y' and is_tuv_target ='Y' ");
            }
            SQLQuery hibQuery = session.createSQLQuery(query.toString());
            hibQuery.addScalar("DESCRIPTION", Hibernate.STRING);
            hibQuery.addScalar("TYPE", Hibernate.STRING);
            hibQuery.addScalar("TM_PROFILE_INFO_ID", Hibernate.INTEGER);
            hibQuery.addScalar("IS_TU", Hibernate.STRING);
            hibQuery.addScalar("IS_TUV", Hibernate.STRING);
            hibQuery.addScalar("IS_TUV_TARGET", Hibernate.STRING);
            hibQuery.addScalar("IS_TUV_SOURCE", Hibernate.STRING);
            hibQuery.addScalar("PROP_REF", Hibernate.STRING);
            List<Object> hibernateResults = hibQuery.list();

            for (Object obj : hibernateResults) {
                Object[] propertyObj = (Object[]) obj;
                int colNdx = 0;
                TMProperties tmProperty = new TMProperties();
                if ((String) propertyObj[colNdx] != null) {
                    tmProperty.setDescription((String) propertyObj[colNdx]);
                }
                colNdx++;
                if ((String) propertyObj[colNdx] != null) {
                    tmProperty.setType((String) propertyObj[colNdx]);
                }
                colNdx++;
                if ((Integer) propertyObj[colNdx] != null) {
                    tmProperty.setTempTmProfileId((Integer) propertyObj[colNdx]);
                }
                colNdx++;
                if ((String) propertyObj[colNdx] != null) {
                    tmProperty.setIsTu((String) propertyObj[colNdx]);
                }
                colNdx++;
                if ((String) propertyObj[colNdx] != null) {
                    tmProperty.setIsTuv((String) propertyObj[colNdx]);
                }
                colNdx++;
                if ((String) propertyObj[colNdx] != null) {
                    tmProperty.setIsTuvTarget((String) propertyObj[colNdx]);
                }
                colNdx++;
                if ((String) propertyObj[colNdx] != null) {
                    tmProperty.setIsTuvSource((String) propertyObj[colNdx]);
                }
                colNdx++;
                if ((String) propertyObj[colNdx] != null) {
                    tmProperty.setPropRef((String) propertyObj[colNdx]);
                }
                if(tmProperty.getIsTu() != null && tmProperty.getIsTu().equalsIgnoreCase("Y")){

                    List<TMProperties> tmProperties =  new ArrayList<TMProperties> ();
                    if(!tuPropertiesListMap.containsKey(tmProperty.getTmProfileId())){
                        tmProperties.add(tmProperty);
                        tuPropertiesListMap.put(tmProperty.getTempTmProfileId(), tmProperties);
                    }else{
                        List <TMProperties> tempList = tuPropertiesListMap.get(tmProperty.getTmProfileId());
                        tempList.add(tmProperty);
                        tuPropertiesListMap.put(tmProperty.getTempTmProfileId(), tempList);
                    }

                }
                if(tmProperty.getIsTuv() != null && tmProperty.getIsTuv().equalsIgnoreCase("Y")){
                    if(tmProperty.getIsTuvSource() !=null && tmProperty.getIsTuvSource().equalsIgnoreCase("Y")){


                        List<TMProperties> tmProperties =  new ArrayList<TMProperties> ();
                        if(!tuvSourcePropertiesListMap.containsKey(tmProperty.getTmProfileId())){
                            tmProperties.add(tmProperty);
                            tuvSourcePropertiesListMap.put(tmProperty.getTempTmProfileId(), tmProperties);
                        }else{
                            List <TMProperties> tempList = tuvSourcePropertiesListMap.get(tmProperty.getTmProfileId());
                            tempList.add(tmProperty);
                            tuvSourcePropertiesListMap.put(tmProperty.getTempTmProfileId(), tempList);
                        }


                    }
                    if(tmProperty.getIsTuvTarget() !=null && tmProperty.getIsTuvTarget().equalsIgnoreCase("Y")){
                        List<TMProperties> tmProperties =  new ArrayList<TMProperties> ();
                        if(!tuvTargetPropertiesListMap.containsKey(tmProperty.getTmProfileId())){
                            tmProperties.add(tmProperty);
                            tuvTargetPropertiesListMap.put(tmProperty.getTempTmProfileId(), tmProperties);
                        }else{
                            List <TMProperties> tempList = tuvTargetPropertiesListMap.get(tmProperty.getTmProfileId());
                            tempList.add(tmProperty);
                            tuvTargetPropertiesListMap.put(tmProperty.getTempTmProfileId(), tempList);
                        }
                    }

                }
                if(tmProperty.getIsTu() != null && tmProperty.getIsTu().equalsIgnoreCase("N")){
                    List<TMProperties> tmProperties =  new ArrayList<TMProperties> ();
                    if(!headerPropertiesListMap.containsKey(tmProperty.getPropRef())){
                        tmProperties.add(tmProperty);
                        headerPropertiesListMap.put(tmProperty.getPropRef(), tmProperties);
                    }else{
                        List <TMProperties> tempList = headerPropertiesListMap.get(tmProperty.getPropRef());
                        tempList.add(tmProperty);
                        headerPropertiesListMap.put(tmProperty.getPropRef(), tempList);
                    }
                }
            }
            query.delete(0, query.length());

        }


    }*/
    public void getTupropertiesUsingTMProfileId(List<Integer> tmProfileIds, Session session
            , Map<Integer, List<TMProperties>> tuPropertiesListMap, Map<Integer, List<TMProperties>> tuvSourcePropertiesListMap
            , Map<Integer, List<TMProperties>> tuvTargetPropertiesListMap, Map<String, List<TMProperties>> headerPropertiesListMap) {
        if (tmProfileIds == null || tmProfileIds.size() < 0)
            return;
        List<List<Integer>> tmProfileIdsList = createBatches(tmProfileIds, ImportExportProperty.NO_OF_EXPORT_RECORDS.getIntValue());
        Integer count = tmProfileIdsList.size();
        for (List<Integer> tmProfiles : tmProfileIdsList) {
            Criteria crit = session.createCriteria(TMProperties.class);
            crit.add(Restrictions.in("tmProfileId.tmProfileInfoId", tmProfiles));

            ProjectionList proList = Projections.projectionList();
            proList.add(Projections.property("description"));
            proList.add(Projections.property("type"));
            proList.add(Projections.property("tmProfileId.tmProfileInfoId"));
            proList.add(Projections.property("isTu"));
            proList.add(Projections.property("isTuv"));
            proList.add(Projections.property("isTuvTarget"));
            proList.add(Projections.property("isTuvSource"));
            proList.add(Projections.property("propRef"));
            crit.setProjection(proList);
            List list = crit.list();
            Iterator it = list.iterator();
            logger.info("current batch" + count-- + "/" + tmProfileIdsList.size());
            if (!it.hasNext()) {
                logger.info("No any data!");
            } else {
                while (it.hasNext()) {
                    Object[] propertyObj = (Object[]) it.next();
                    // for(int i = 0; i < row.length;i++){

                    int colNdx = 0;
                    TMProperties tmProperty = new TMProperties();
                    if ((String) propertyObj[colNdx] != null) {
                        tmProperty.setDescription((String) propertyObj[colNdx]);
                    }
                    colNdx++;
                    if ((String) propertyObj[colNdx] != null) {
                        tmProperty.setType((String) propertyObj[colNdx]);
                    }
                    colNdx++;
                    if ((Integer) propertyObj[colNdx] != null) {
                        tmProperty.setTempTmProfileId((Integer) propertyObj[colNdx]);
                    }
                    colNdx++;
                    if ((String) propertyObj[colNdx] != null) {
                        tmProperty.setIsTu((String) propertyObj[colNdx]);
                    }
                    colNdx++;
                    if ((String) propertyObj[colNdx] != null) {
                        tmProperty.setIsTuv((String) propertyObj[colNdx]);
                    }
                    colNdx++;
                    if ((String) propertyObj[colNdx] != null) {
                        tmProperty.setIsTuvTarget((String) propertyObj[colNdx]);
                    }
                    colNdx++;
                    if ((String) propertyObj[colNdx] != null) {
                        tmProperty.setIsTuvSource((String) propertyObj[colNdx]);
                    }
                    colNdx++;
                    if ((String) propertyObj[colNdx] != null) {
                        tmProperty.setPropRef((String) propertyObj[colNdx]);
                    }
                    if (tmProperty.getIsTu() != null && tmProperty.getIsTu().equalsIgnoreCase("Y")) {

                        List<TMProperties> tmProperties = new ArrayList<TMProperties>();
                        if (!tuPropertiesListMap.containsKey(tmProperty.getTmProfileId())) {
                            tmProperties.add(tmProperty);
                            tuPropertiesListMap.put(tmProperty.getTempTmProfileId(), tmProperties);
                        } else {
                            List<TMProperties> tempList = tuPropertiesListMap.get(tmProperty.getTmProfileId());
                            tempList.add(tmProperty);
                            tuPropertiesListMap.put(tmProperty.getTempTmProfileId(), tempList);
                        }

                    }
                    if (tmProperty.getIsTuv() != null && tmProperty.getIsTuv().equalsIgnoreCase("Y")) {
                        if (tmProperty.getIsTuvSource() != null && tmProperty.getIsTuvSource().equalsIgnoreCase("Y")) {


                            List<TMProperties> tmProperties = new ArrayList<TMProperties>();
                            if (!tuvSourcePropertiesListMap.containsKey(tmProperty.getTmProfileId())) {
                                tmProperties.add(tmProperty);
                                tuvSourcePropertiesListMap.put(tmProperty.getTempTmProfileId(), tmProperties);
                            } else {
                                List<TMProperties> tempList = tuvSourcePropertiesListMap.get(tmProperty.getTmProfileId());
                                tempList.add(tmProperty);
                                tuvSourcePropertiesListMap.put(tmProperty.getTempTmProfileId(), tempList);
                            }


                        }
                        if (tmProperty.getIsTuvTarget() != null && tmProperty.getIsTuvTarget().equalsIgnoreCase("Y")) {
                            List<TMProperties> tmProperties = new ArrayList<TMProperties>();
                            if (!tuvTargetPropertiesListMap.containsKey(tmProperty.getTmProfileId())) {
                                tmProperties.add(tmProperty);
                                tuvTargetPropertiesListMap.put(tmProperty.getTempTmProfileId(), tmProperties);
                            } else {
                                List<TMProperties> tempList = tuvTargetPropertiesListMap.get(tmProperty.getTmProfileId());
                                tempList.add(tmProperty);
                                tuvTargetPropertiesListMap.put(tmProperty.getTempTmProfileId(), tempList);
                            }
                        }

                    }
                    if (tmProperty.getIsTu() != null && tmProperty.getIsTu().equalsIgnoreCase("N")) {
                        List<TMProperties> tmProperties = new ArrayList<TMProperties>();
                        if (!headerPropertiesListMap.containsKey(tmProperty.getPropRef())) {
                            tmProperties.add(tmProperty);
                            headerPropertiesListMap.put(tmProperty.getPropRef(), tmProperties);
                        } else {
                            List<TMProperties> tempList = headerPropertiesListMap.get(tmProperty.getPropRef());
                            tempList.add(tmProperty);
                            headerPropertiesListMap.put(tmProperty.getPropRef(), tempList);
                        }
                    }

                }
            }
        }

    }

    /*@SuppressWarnings("unchecked")
    public  List<TMProperties> getTupropertiesUsingTMProfileId(
            Integer tmProfileId,Session session) {
        // Session session=getSession();
        Criteria hibCriteria = session.createCriteria(TMProperties.class);
        hibCriteria.add(Restrictions.eq("tmProfileId.tmProfileInfoId", tmProfileId));
        hibCriteria.add(Restrictions.eq("isTu", "Y"));

        List<TMProperties> tmPropertiesList= (List<TMProperties>) hibCriteria.list();
    //	session.close();
        return tmPropertiesList;
    }
    @SuppressWarnings("unchecked")
    public  List<TMProperties> getTuvSourcepropertiesUsingTMProfileId(
            Integer tmProfileId,Session session) {
        // Session session=getSession();
        Criteria hibCriteria = session.createCriteria(TMProperties.class);
        hibCriteria.add(Restrictions.eq("tmProfileId.tmProfileInfoId", tmProfileId));
        hibCriteria.add(Restrictions.eq("isTuv", "Y"));
        hibCriteria.add(Restrictions.eq("isTuvSource", "Y"));

        List<TMProperties> tmPropertiesList= (List<TMProperties>) hibCriteria.list();
        //session.close();
        return tmPropertiesList;
    }
    @SuppressWarnings("unchecked")
    public  List<TMProperties> getTuvTargetpropertiesUsingTMProfileId(
            Integer tmProfileId,Session session) {
        // Session session=getSession();
        Criteria hibCriteria = session.createCriteria(TMProperties.class);
        hibCriteria.add(Restrictions.eq("tmProfileId.tmProfileInfoId", tmProfileId));
        hibCriteria.add(Restrictions.eq("isTuv", "Y"));
        hibCriteria.add(Restrictions.eq("isTuvTarget", "Y"));

        List<TMProperties> tmPropertiesList= (List<TMProperties>) hibCriteria.list();
    //	session.close();
        return tmPropertiesList;
    }*/
    public void updateFileUploadStatus(FileUploadStatusImp fileData, Session session) {
        if (fileData != null) {
            // Session session=getSession();
            //Transaction tr = session.beginTransaction();
            session.update(fileData);

            //tr.commit();
            //session.close();
        }

    }

    public FileUploadStatusImp saveFileUploadStatus(FileUploadStatusImp fileUpload, Session session) {
        if (fileUpload != null) {
            // Session session=getSession();
            Transaction tr = session.beginTransaction();
            fileUpload = (FileUploadStatusImp) session.save(fileUpload);

            //	tr.commit();
            // session.close();
        }
        return fileUpload;

    }

    public FileUploadStatusImp getFileUrlByFileId(String fileIdStr, Session session)
            throws DataAccessException {

        FileUploadStatusImp fileData = (FileUploadStatusImp) session.get(FileUploadStatusImp.class, Integer.parseInt(fileIdStr));

        return fileData;
        //	return fileData.getFileUrl();

    }

    public Session getSession() {
        Session session = sessFact.openSession();
        return session;
    }

    public List<ProductGroup> getProductGroupLookUp(Session session) {
        //	Session session = getSession();
        Criteria hibCriteria = session.createCriteria(ProductGroup.class);
        return (List<ProductGroup>) hibCriteria.list();
    }

    public List<Domain> getDomainLookUp(Session session) {
        //	Session session = getSession();
        Criteria hibCriteria = session.createCriteria(Domain.class);
        hibCriteria.add(Restrictions.not(Restrictions.eq("isActive", "N")));
        hibCriteria.addOrder(Order.asc("domain"));
        return (List<Domain>) hibCriteria.list();
    }

    public List<Company> getCompanyLookUp(Session session) {

        //	Session session = getSession();
        Criteria hibCriteria = session.createCriteria(Company.class);
        hibCriteria.add(Restrictions.not(Restrictions.eq("isActive", "N")));
//		hibCriteria.addOrder(Order.asc("languageLabel"));
        return (List<Company>) hibCriteria.list();

    }

    public List<ContentType> getContentType(Session session) {
        //Session session = getSession();
        Criteria hibCriteria = session.createCriteria(ContentType.class);
        return (List<ContentType>) hibCriteria.list();
    }

    public List<Language> getLanguages(Session session) {

        //Session session = getSession();
        Criteria hibCriteria = session.createCriteria(Language.class);
        hibCriteria.add(Restrictions.not(Restrictions.eq("isActive", "N")));
        hibCriteria.addOrder(Order.asc("languageLabel"));
        return (List<Language>) hibCriteria.list();

    }

    public static <C extends Object> List<List<C>> createBatches(
            Collection<C> collection, int batchSize) {
        if (batchSize < 1)
            throw new IllegalArgumentException("Batch size cannot be zero or less");

        List<C> objects = new ArrayList<C>(collection);
        int noOfBatches = (int) Math.ceil((double) objects.size() / batchSize);
        List<List<C>> batches = new ArrayList<List<C>>(noOfBatches);
        int index = 0;
        for (int i = 0; i < noOfBatches; i++) {
            List<C> list = new ArrayList<C>(batchSize);
            for (int j = 0; j < batchSize && index < objects.size(); j++, index++) {
                list.add(objects.get(index));
            }
            batches.add(list);
        }
        return batches;
    }
    
}
