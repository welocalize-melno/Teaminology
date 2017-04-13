package com.teaminology.hp.bl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.teaminology.hp.Enum.ImportExportEnum;
import com.teaminology.hp.bo.Company;
import com.teaminology.hp.bo.ContentType;
import com.teaminology.hp.bo.Domain;
import com.teaminology.hp.bo.FileUploadStatusImp;
import com.teaminology.hp.bo.Language;
import com.teaminology.hp.bo.ProductGroup;
import com.teaminology.hp.bo.TMProperties;
import com.teaminology.hp.bo.TmProfileBo;
import com.teaminology.hp.dao.ImpExp.hibernate.ExportDAOImpl;
import com.teaminology.hp.util.Util;


public class ExportBL
{
	private static Logger logger = Logger.getLogger(ExportBL.class);

	public static void startExport(String fileId, String exportBy, String selectedIds, String companyId) throws FileNotFoundException, JAXBException {

		FileUploadStatusImp fileUpload = null;
		ExportDAOImpl exportDAOImpl = ExportDAOImpl.getInstance();
		Session session = exportDAOImpl.getSession();
		
		
		logger.info("Started Exporting....");
		
		logger.info("Started getting the lookup values.......");
		
		List<Company> companyList = exportDAOImpl.getCompanyLookUp(session);
		List<Domain> domainList = exportDAOImpl.getDomainLookUp(session);
		List<ProductGroup> productGroupList = exportDAOImpl.getProductGroupLookUp(session);
		List<ContentType> contentTypeList = exportDAOImpl.getContentType(session);
		List<Language> languageList = exportDAOImpl.getLanguages(session);
		
		logger.info("Completed getting the lookup values.......");
		
		List<TmProfileBo> tmProfileInfoList  = null;
		Integer maxRecordPerBatch = 5000;
		Integer totalActualRecord = 0;

		try {
			String selectedIdsArray[] = new String[1];
			
			if (selectedIds.contains(",")) {
				selectedIdsArray = selectedIds.split(",");
			} else {
				selectedIdsArray[0] = selectedIds;
			}

			List<String> lList = Arrays.asList(selectedIdsArray);
			if(lList!=null && !lList.isEmpty()){

				logger.info("+++++ For TM Export Processing languages  "+lList+" for export file ID "+fileId);

				Integer noDataRecords = 0;
				
				// Processing each language selected by user one after the other
				for(String languageID: lList ){

					Integer recordIndex = 0;

					logger.info("TM export, processing language "+languageID 
							+ " for export file ID "+fileId);					

					// For a single language get the total number of TU's
					// Instead of querying all the TU's for a language 
					// in one shot get them in batches say 5000
					Integer totalRecords = getRecordCount(session, exportBy, companyId, languageID);
					
					totalActualRecord = totalActualRecord + totalRecords;
					fileUpload = exportDAOImpl.getFileUrlByFileId(fileId, session);
					String absolutePath = fileUpload.getFileUrl();
					fileUpload.setFileStatus("Export Is In Progress");
					exportDAOImpl.updateFileUploadStatus(fileUpload, session);

					logger.info("Got total records "+totalRecords+" for language "+languageID 
							+ " for export file ID "+fileId);

					if(totalRecords==null || totalRecords==0){
						noDataRecords++;
						continue;
					}

					Double totalBatches = Math.ceil(Double.valueOf(totalRecords.toString())/maxRecordPerBatch);

					for(int i = 0; i<totalBatches.intValue(); i++){

						recordIndex = maxRecordPerBatch*i;

						tmProfileInfoList = exportDAOImpl.getTotalTermsInTM(exportBy,
								languageID, session, companyId, recordIndex, maxRecordPerBatch);

						logger.info("Got records For Pagination "+recordIndex+" "+maxRecordPerBatch);

						if (tmProfileInfoList != null && !tmProfileInfoList.isEmpty()) {
							logger.info("List size" + tmProfileInfoList.size());
							
							logger.info("Writing data in File......");
							writeTMX(tmProfileInfoList, fileId, session, absolutePath,
									fileUpload, companyList, domainList, productGroupList,
									contentTypeList, languageList);
							tmProfileInfoList.clear();
							tmProfileInfoList = null;
						}
					}

				}

				logger.info("Number of records having no data.... "+noDataRecords);
				
				if(noDataRecords == lList.size()){
					logger.info("Don't have any TM for the selected criteria....");
					if(fileUpload != null){
						String absolutePath = fileUpload.getFileUrl();
						String messageObj = "No Data Available ";
						File fileObj = new File(absolutePath);
						fileObj.createNewFile();

						FileWriter fw = new FileWriter(fileObj.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(messageObj);
						bw.close();
					}
				}
				
				Transaction tr = session.beginTransaction();
				fileUpload.setTotalRecords(totalActualRecord);
				fileUpload.setProcessedPercentage(100);
				fileUpload.setFileStatus(ImportExportEnum.TEAMINOLOGY_FILE_EXPORT_STATUS.getValue());
				fileUpload.setEndTime(new Date());
				exportDAOImpl.updateFileUploadStatus(fileUpload, session);
				tr.commit();

				logger.info("++++++ Completed  Processing TM Export for languages  "+lList+" for export file ID "+fileId);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in ImportBL");
			logger.error(e, e);

		}
		finally {
			session.close();


		}


	}

	private static File writeTMX(List<TmProfileBo> tmProfileInfoList,
			String fileId, Session session, String absolutePath, FileUploadStatusImp fileUpload,
			List<Company> companyList, List<Domain> domainList, 
			List<ProductGroup> productGroupList,  List<ContentType> contentTypeList,
			List<Language> languageList) throws SAXException, IOException {
		
		File x = null;
		Integer totalRecords = tmProfileInfoList.size();
		Integer percentageCount = 0;
		DocumentBuilderFactory f = null;
		DocumentBuilder b = null;
		Document d = null;
		Node bodyEntry = null;
		ExportDAOImpl exportDAOImpl = ExportDAOImpl.getInstance();
		try {
			logger.info("absolutePath" + absolutePath);
			x = new File(absolutePath);
			f = DocumentBuilderFactory.newInstance();
			b = f.newDocumentBuilder();
			d = b.newDocument();
			if(!x.exists()){
				Element tmxEntry = d.createElement("tmx");
				tmxEntry.setAttribute("version", "1.0 GS");
				d.appendChild(tmxEntry);
				Element header = d.createElement("header");
				header.setAttribute("srclang", "EN_US");

				tmxEntry.appendChild(header);
				bodyEntry = d.createElement("body");
				tmxEntry.appendChild(bodyEntry);
			}

			if(x.exists()){
				d =b.parse(x);
				bodyEntry = d.getElementsByTagName("body").item(0);
			}
			List<TMProperties> headerPropetiesList = new ArrayList<TMProperties>();
			List<TMProperties> tuPropertiesList = new ArrayList<TMProperties>();
			List<TMProperties> tuvSourcePropertiesList = new ArrayList<TMProperties>();
			List<TMProperties> tuvTargetPropertiesList = new ArrayList<TMProperties>();
			String propRef = null;
			List<Integer> tmProfileIds = new ArrayList<Integer>();

			for (TmProfileBo tpTerms : tmProfileInfoList) {
				tmProfileIds.add(tpTerms.getTmProfileInfoId());
			}

			Map<Integer, List<TMProperties>> tuPropertiesListMap = new HashMap<Integer, List<TMProperties>>();

			Map<Integer, List<TMProperties>> tuvSourcePropertiesListMap = new HashMap<Integer, List<TMProperties>>();

			Map<Integer, List<TMProperties>> tuvTargetPropertiesListMap = new HashMap<Integer, List<TMProperties>>();

			Map<String, List<TMProperties>> headerPropertiesListMap = new HashMap<String, List<TMProperties>>();

			exportDAOImpl.getTupropertiesUsingTMProfileId(tmProfileIds, session, tuPropertiesListMap, tuvSourcePropertiesListMap
					, tuvTargetPropertiesListMap, headerPropertiesListMap);
			Util.logMemoryUsage();
			for (int i = 0; i < tmProfileInfoList.size(); i++) {

				Element termEntry = d.createElement("tu");
				bodyEntry.appendChild(termEntry);
				if (tmProfileInfoList.get(i).getTuId() != null) {
					termEntry.setAttribute("tuid", tmProfileInfoList.get(i).getTuId().toString());
				}
				percentageCount++;
				String sourceLangName = null;
				Integer sourcelanguage = tmProfileInfoList.get(i).getSourceLang();
				for (Language language : languageList) {
					if (language.getLanguageId().equals(sourcelanguage)) {
						sourceLangName = language.getLanguageCode();
						break;
					}
				}
				String targetLangName = null;
				Integer targetLanguage = tmProfileInfoList.get(i).getTargetLang();
				for (Language language : languageList) {
					if (language.getLanguageId().equals(targetLanguage)) {
						targetLangName = language.getLanguageCode();
						break;
					}
				}
				String companyName = null;
				Integer companyId = tmProfileInfoList.get(i).getCompanyId();
				for (Company company : companyList) {
					if (company.getCompanyId().equals(companyId)) {
						companyName = company.getCompanyName();
						break;
					}
				}
				// Company company=tmProfileInfoList.get(i).getCompany();
				if (companyId != null) {
					Element prop = d.createElement("prop");
					prop.appendChild(d.createTextNode(companyName));
					prop.setAttribute("type", ImportExportEnum.TEAMINOLOGY_COMPANYTEAM.getValue());
					termEntry.appendChild(prop);
				}
				//Domain domain=tmProfileInfoList.get(i).getDomain();
				String domainName = null;
				Integer domainId = tmProfileInfoList.get(i).getDomainId();
				for (Domain domain : domainList) {
					if (domain.getDomainId().equals(domainId)) {
						domainName = domain.getDomain();
						break;
					}
				}
				if (domainId != null) {
					Element prop = d.createElement("prop");
					prop.appendChild(d.createTextNode(domainName));
					prop.setAttribute("type", ImportExportEnum.TEAMINOLOGY_DOMAIN.getValue());
					termEntry.appendChild(prop);
				}
				//   ProductGroup product=tmProfileInfoList.get(i).getProductGroup();
				String productGroupName = null;
				Integer productGroupId = tmProfileInfoList.get(i).getProductGroupId();
				for (ProductGroup product : productGroupList) {
					if (product.getProductId().equals(productGroupId)) {
						productGroupName = product.getProduct();
						break;
					}
				}
				if (productGroupId != null) {
					Element prop = d.createElement("prop");
					prop.appendChild(d.createTextNode(productGroupName));
					prop.setAttribute("type", ImportExportEnum.TEAMINOLOGY_PROUCTLINE.getValue());
					termEntry.appendChild(prop);
				}
				// ContentType contentType=tmProfileInfoList.get(i).getContentType();
				String contentTypeName = null;
				Integer contentTypeId = tmProfileInfoList.get(i).getContentTypeId();
				for (ContentType contentType : contentTypeList) {
					if (contentType.getContentTypeId().equals(contentTypeId)) {
						contentTypeName = contentType.getContentType();
						break;
					}
				}
				if (contentTypeId != null) {
					Element prop = d.createElement("prop");
					prop.appendChild(d.createTextNode(contentTypeName));
					prop.setAttribute("type", ImportExportEnum.TEAMINOLOGY_CONTENT.getValue());
					termEntry.appendChild(prop);
				}

				if (tmProfileInfoList.get(i).getPropRef() != null) {
					logger.info("counter" + i);
					String prop = tmProfileInfoList.get(i).getPropRef();
					if (propRef == null) {
						propRef = prop;
					}
					logger.info("prop" + prop);
					if (prop != null) {
						if (!propRef.equalsIgnoreCase(prop)) {
							headerPropetiesList = headerPropertiesListMap.get(prop);
							propRef = prop;
						}


					}

					if (headerPropetiesList != null) {

						for (int k = 0; k < headerPropetiesList.size(); k++) {
							if (headerPropetiesList.get(k).getDescription() != null) {
								Element propElement = d.createElement("prop");
								propElement.appendChild(d.createTextNode(headerPropetiesList.get(k).getDescription()));
								propElement.setAttribute("type", headerPropetiesList.get(k).getType());
								termEntry.appendChild(propElement);
							}
						}
					}
				}
				// logger.info("prop"+prop);
				if (tmProfileInfoList.get(i).getTmProfileInfoId() != null)
					tuPropertiesList = tuPropertiesListMap.get(tmProfileInfoList.get(i).getTmProfileInfoId());
				if (tuPropertiesList != null && tuPropertiesList.size() > 0) {

					for (int k = 0; k < tuPropertiesList.size(); k++) {
						if (tuPropertiesList.get(k).getDescription() != null) {
							Element tuPropElement = d.createElement("prop");
							tuPropElement.appendChild(d.createTextNode(tuPropertiesList.get(k).getDescription()));
							tuPropElement.setAttribute("type", tuPropertiesList.get(k).getType());
							termEntry.appendChild(tuPropElement);
						}
					}
				}
				if (tmProfileInfoList.get(i).getTmProfileInfoId() != null) {
					tuvSourcePropertiesList = tuvSourcePropertiesListMap.get(tmProfileInfoList.get(i).getTmProfileInfoId());
					tuvTargetPropertiesList = tuvTargetPropertiesListMap.get(tmProfileInfoList.get(i).getTmProfileInfoId());
				}
				String sourceTm = tmProfileInfoList.get(i).getSource();
				String targetTm = tmProfileInfoList.get(i).getTarget();
				if (sourceTm != null && targetTm != null) {
					Element tuvGrp = d.createElement("tuv");
					termEntry.appendChild(tuvGrp);

					if (tuvSourcePropertiesList != null && tuvSourcePropertiesList.size() > 0) {

						for (int k = 0; k < tuvSourcePropertiesList.size(); k++) {
							if (tuvSourcePropertiesList.get(k).getDescription() != null) {
								Element tuvSourcePropElement = d.createElement("prop");
								tuvSourcePropElement.appendChild(d.createTextNode(tuvSourcePropertiesList.get(k).getDescription()));
								tuvSourcePropElement.setAttribute("type", tuvSourcePropertiesList.get(k).getType());
								tuvGrp.appendChild(tuvSourcePropElement);
							}
						}
					}

					Element seg = d.createElement("seg");
					tuvGrp.appendChild(seg);
					seg.appendChild(d.createTextNode(sourceTm));
					tuvGrp.setAttribute("xml:lang", sourceLangName);
					if (tmProfileInfoList.get(i).getTuSourceCreationDate() != null)
						tuvGrp.setAttribute("creationdate", tmProfileInfoList.get(i).getTuSourceCreationDate());
					if (tmProfileInfoList.get(i).getTuSourceCreationId() != null)
						tuvGrp.setAttribute("creationid", tmProfileInfoList.get(i).getTuSourceCreationId());
					if (tmProfileInfoList.get(i).getTuSourceChangeDate() != null)
						tuvGrp.setAttribute("changedate", tmProfileInfoList.get(i).getTuSourceChangeDate());
					if (tmProfileInfoList.get(i).getTuSourceChangeId() != null)
						tuvGrp.setAttribute("changeid", tmProfileInfoList.get(i).getTuSourceChangeId());
					tuvGrp = d.createElement("tuv");
					termEntry.appendChild(tuvGrp);
					if (tuvTargetPropertiesList != null && tuvTargetPropertiesList.size() > 0) {
						for (int k = 0; k < tuvTargetPropertiesList.size(); k++) {
							if (tuvTargetPropertiesList.get(k).getDescription() != null) {
								Element tuvTargetPropElement = d.createElement("prop");
								tuvTargetPropElement.appendChild(d.createTextNode(tuvTargetPropertiesList.get(k).getDescription()));
								tuvTargetPropElement.setAttribute("type", tuvTargetPropertiesList.get(k).getType());
								tuvGrp.appendChild(tuvTargetPropElement);
							}
						}
					}
					seg = d.createElement("seg");
					tuvGrp.appendChild(seg);
					tuvGrp.setAttribute("xml:lang", targetLangName);
					if (tmProfileInfoList.get(i).getTuTargetCreationDate() != null)
						tuvGrp.setAttribute("creationdate", tmProfileInfoList.get(i).getTuTargetCreationDate());
					if (tmProfileInfoList.get(i).getTuTargetCreationId() != null)
						tuvGrp.setAttribute("creationid", tmProfileInfoList.get(i).getTuTargetCreationId());
					if (tmProfileInfoList.get(i).getTuTargetChangeDate() != null)
						tuvGrp.setAttribute("changedate", tmProfileInfoList.get(i).getTuTargetChangeDate());
					if (tmProfileInfoList.get(i).getTuTargetChangeId() != null)
						tuvGrp.setAttribute("changeid", tmProfileInfoList.get(i).getTuTargetChangeId());
					seg.appendChild(d.createTextNode(targetTm));
				}

				logger.info("Downloaded >>" + i + "/" +totalRecords);

				if (tuPropertiesList != null) {
					tuPropertiesList.clear();
					tuPropertiesList = null;
				}
				if (tuvSourcePropertiesList != null) {
					tuvSourcePropertiesList.clear();
					tuvSourcePropertiesList = null;
				}
				if (tuvTargetPropertiesList != null) {
					tuvTargetPropertiesList.clear();
					tuvTargetPropertiesList = null;
				}
			}


			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer m = tf.newTransformer();

			DOMSource source = new DOMSource(d);
			StreamResult result = new StreamResult(x);
			m.transform(source, result);
		}
		catch (ParserConfigurationException e) {
			fileUpload.setFileStatus("Export Failed");
			fileUpload.setEndTime(new Date());
			exportDAOImpl.updateFileUploadStatus(fileUpload, session);
			e.printStackTrace();
		}
		catch (TransformerConfigurationException e) {
			fileUpload.setFileStatus("Export Failed");
			fileUpload.setEndTime(new Date());
			exportDAOImpl.updateFileUploadStatus(fileUpload, session);
			e.printStackTrace();
		}
		catch (TransformerException e) {
			fileUpload.setFileStatus("Export Failed");
			fileUpload.setEndTime(new Date());
			exportDAOImpl.updateFileUploadStatus(fileUpload, session);
			e.printStackTrace();
		}
		return x;

	}

	public static Integer getRecordCount(Session session,String exportBy, String  companyId,String seletedID){
		String countQurey = "select count(tm_profile_info_id) tot_rec from tm_profile_info " +
				" where is_active='Y'  ";

		if (exportBy.equalsIgnoreCase("Locale")) {
			countQurey = countQurey + " and target_lang = ";
		} else if (exportBy.equalsIgnoreCase("IndustryDomain")) {
			countQurey = countQurey + " and industry_domain = " ;
		} else if (exportBy.equalsIgnoreCase("ProductLine")) {
			countQurey = countQurey + " and product_line = " ;
		} else if (exportBy.equalsIgnoreCase("ContentType")) {
			countQurey = countQurey + " and content_type = ";
		} else if (exportBy.equalsIgnoreCase("Company")) {
			countQurey = countQurey + "and company = " ;
		}
		countQurey = countQurey + seletedID;
		countQurey = countQurey + " and company = " +companyId; 
		SQLQuery sqlQueryObj = session.createSQLQuery(countQurey);
		sqlQueryObj.addScalar("tot_rec",Hibernate.INTEGER);
		Integer  listOfCount =(Integer)sqlQueryObj.uniqueResult();

		return listOfCount;
	}


}
