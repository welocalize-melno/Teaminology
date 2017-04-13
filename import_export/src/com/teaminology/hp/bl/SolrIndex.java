package com.teaminology.hp.bl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;

import com.teaminology.hp.Enum.SuffixTerm;
import com.teaminology.hp.bo.Category;
import com.teaminology.hp.bo.Company;
import com.teaminology.hp.bo.ContentType;
import com.teaminology.hp.bo.Domain;
import com.teaminology.hp.bo.FileUploadStatusImp;
import com.teaminology.hp.bo.GlobalsightTermInfoTO;
import com.teaminology.hp.bo.Language;
import com.teaminology.hp.bo.PartsOfSpeech;
import com.teaminology.hp.bo.ProductGroup;
import com.teaminology.hp.bo.Status;
import com.teaminology.hp.bo.TermInformationBo;
import com.teaminology.hp.bo.TmProfileIndexBo;
import com.teaminology.hp.dao.ImpExp.hibernate.CompanyDAO;
import com.teaminology.hp.dao.ImpExp.hibernate.GSTermBaseDAO;
import com.teaminology.hp.dao.ImpExp.hibernate.Lookup;
import com.teaminology.hp.dao.ImpExp.hibernate.TermbaseDAO;
import com.teaminology.hp.dao.ImpExp.hibernate.TmsDAO;
import com.teaminology.hp.util.IndexTypeEnum;
import com.teaminology.hp.util.SolrUtil;

public class SolrIndex {

	private static Logger logger = Logger.getLogger(SolrIndex.class);
	private TermbaseDAO termBaseDao = TermbaseDAO.getInstance();
	private Lookup lookup = Lookup.getInstance();
	private CompanyDAO companyDAO = CompanyDAO.getInstance();
	private TmsDAO tmsDao = TmsDAO.getInstance();
    private GSTermBaseDAO gstermBaseDAO = GSTermBaseDAO.getInstance();


	
	

	/**
	 *  start indexing tmx
	 * @param companyIds
	 *
	 */
	public void processTMIndex(Set<Integer> companyIds,Integer startPK,Integer endPK){
		//If company ids are null process TermBase
		//for all the companyIds 
		if(companyIds == null || companyIds.isEmpty()){
			companyIds = companyDAO.getActiveCompanyIds();
		}

		// Process Termbase for each company
		if(companyIds != null && !companyIds.isEmpty()){

			for(Integer companyId : companyIds){
				try{
					logger.info("++++ Indexing TMS for companyId "+companyId);
					Company company = companyDAO.getCompany(companyId);
					
					if(company == null){
						logger.info("Got null company for "+companyId);
						continue;
					}
					// Index term base for this company
					indexTms(company,startPK,endPK);
					logger.info("++++ Completed Indexing TMS for companyId "+companyId);

				}catch(Exception e){
					logger.error("Got error in Indexing TMS for CompanyId "+companyId);
				}
			}
		}
	}

	/**
	 * Start indexing Termbase
	 * @param companyIds
	 */
	public void processTermBaseIndex(Set<Integer> companyIds,Integer startPK,Integer endPK){
		//If company ids are null process TermBase
		//for all the companyIds 
		if(companyIds == null || companyIds.isEmpty()){
			companyIds = companyDAO.getActiveCompanyIds();
		}

		// Process Termbase for each company
		if(companyIds != null && !companyIds.isEmpty()){
			for(Integer companyId : companyIds){
				try{
					logger.info("### Indexing Termbase for companyId "+companyId);
					Company company = companyDAO.getCompany(companyId);
					// Index term base for this company
					indexTermBase(company,startPK,endPK);
					logger.info("####Completed Indexing Termbase for companyId "+companyId);
				}catch(Exception e){
					logger.error("Got error in Indexing Termbase for CompanyId "+companyId);
				}
			}
		}

	}
	
	
	/**
	 * Process GS termbase index
	 * @param companyIds
	 */
	public void processGSTermBaseIndex(Set<Integer> companyIds,Integer startPK,Integer endPK){

		//If company ids are null process TermBase
		//for all the companyIds 
		if(companyIds == null || companyIds.isEmpty()){
			companyIds = companyDAO.getActiveCompanyIds();
		}

		// Process Termbase for each company
		if(companyIds != null && !companyIds.isEmpty()){
			for(Integer companyId : companyIds){
				try{
					logger.info("++++ Indexing Terms for companyId "+companyId);
					Company company = companyDAO.getCompany(companyId);
					// Index term base for this company
					indexGSTerms(company,startPK,endPK);
					logger.info("++++ Completed Indexing Terms for companyId "+companyId);
				}catch(Exception e){
					logger.error("Got error in Indexing Terms for CompanyId "+companyId);
				}
			}
		}

	
	}

	
	
	/**
	 * GS data saved in solr database
	 * @param company
	 */
	public void indexGSTerms(Company company,Integer startPK,Integer endPK){
		
		SolrInputDocument doc = null;
		
		if(company == null){
			logger.info("Company doen't exist not indexing GSTermBase....");
		}else{
			String termBaseSuffix = SuffixTerm._GS.name();
			try{

				Integer recordIndex = 0;
				Integer maxRecordPerBatch = 1000;

				//total TermBase records count
				Integer totalRecords = gstermBaseDAO.getTermBaseRecordCount(company.getCompanyId()); 

				logger.info("Total Records found for :::"+totalRecords+" companyID"+company.getCompanyName());
				//creating batches
				Double totalBatches = Math.ceil(Double.valueOf(totalRecords.toString())/maxRecordPerBatch);

				logger.info("Getting no. of to TermBase to index for companyId "+company.getCompanyId()+" name: "+company.getCompanyName());

				List<GlobalsightTermInfoTO> termInformation = null;

				//get TermBase data for each batch
				logger.info("Total Batches "+totalBatches);

				for(int i = 0;i<totalBatches.intValue();i++){

					recordIndex = maxRecordPerBatch*i;
					termInformation = gstermBaseDAO.getGSTermBaseData(company.getCompanyId(),recordIndex,maxRecordPerBatch,
							startPK,endPK);
					logger.info("Procssed batches of "+i+"/"+totalBatches);


					//logger.info("Got "+number+" of TermBase to index for companyId "+company.getCompanyId()+" name: "+company.getCompanyName());


					logger.info("***TermInformation data***");

					if(termInformation!=null && !termInformation.isEmpty()){

						Collection<SolrInputDocument> collObj = new ArrayList<SolrInputDocument>();

						//process data in batches
						for(GlobalsightTermInfoTO term : termInformation){
							String gstermId = term.getGlobalsightTermInfoId() + termBaseSuffix;
							String source =	term.getSourceSegment();
							String target =	term.getTargetSegment();	
							Integer sourcelang = term.getSourceLang();
							Integer targetlang =term.getTargetLang();
							String origin =	term.getOrigin();
							Integer comapnyId = term.getCompanyId();
							String fileId =	term.getFileId();
							Integer transUnitId =	term.getTransUnitId();

							Integer termId = term.getTermId();
	                        String jobId = term.getJobId();
	                        String jobName = term.getJobName();
	                        String taskId = term.getTaskId();
	                        String taskName = term.getTaskName();


	                        doc = new SolrInputDocument();

							 if (gstermId != null)
		                            doc.addField("id", gstermId);
		                        if (source != null)
		                            doc.addField("source", source);
		                        if (target != null)
		                            doc.addField("target", target);
		                        if (fileId != null)
		                            doc.addField("fileId", fileId);
		                        if (transUnitId != null)
		                            doc.addField("transUnitId", transUnitId);
		                        if (termId != null)
		                            doc.addField("termId", termId);
		                        if (taskId != null)
		                            doc.addField("taskId", taskId);
		                        if (jobId != null)
		                            doc.addField("jobId", jobId);
		                        if (jobName != null)
		                            doc.addField("jobName", jobName);
		                        if (taskName != null)
		                            doc.addField("taskName", taskName);
		                        if (origin != null)
		                            doc.addField("origin", origin);
		                        if (comapnyId != null){
		                        	doc.addField("companyId", comapnyId);
		                        }
		                        
		                        doc.addField("company", company.getCompanyName());
		                        List<Language> languageList = lookup.getLanguages();
		                        if (sourcelang != null) {
		                            String sourceLangLable = null;
		                            for (Language language : languageList) {
		                                if (language.getLanguageId().intValue() == sourcelang) {
		                                    sourceLangLable = language.getLanguageLabel();
		                                    break;
		                                }
		                            }
		                            if (sourceLangLable != null)
		                                doc.addField("sourcelang", sourceLangLable);
		                        }

		                        if (targetlang != null) {
		                            String targetLangLable = null;
		                            for (Language language : languageList) {
		                                if (language.getLanguageId().intValue() == targetlang) {
		                                    targetLangLable = language.getLanguageLabel();
		                                    break;
		                                }
		                            }
		                            if (targetLangLable != null)
		                                doc.addField("targetlang", targetLangLable);
		                        }
		                        doc.addField("termtype", IndexTypeEnum.GS.name());
		                        doc.addField("primarykey",term.getGlobalsightTermInfoId());
							collObj.add(doc);
						}

						// Save the data in solr database
						logger.info("Saving GS terms ata to solr");
						SolrUtil.saveData(collObj);
						collObj.clear();
						collObj = null;
					}
					else{
						logger.info("No Terms exists for company "+company.getCompanyId() +": "+company.getCompanyName());
					}
				}

			}catch(Exception e){
				logger.error("****Error in  indexing GStermbase data****");
				logger.error(e,e);
			}
		}

	}
	

	public void indexTms(Company company,Integer startPK,Integer endPK){
		SolrInputDocument doc = null;
		logger.info("***inside tmIndex***");
		try {
			Integer recordIndex = 0;
			Integer maxRecordPerBatch = 1000;

			//total TM records count
			Integer totalRecords = tmsDao.getTMRecordCount(company.getCompanyId()); 
			logger.info("Total Records found for :::"+totalRecords+" companyID"+company.getCompanyName());
			//creating batches
			Double totalBatches = Math.ceil(Double.valueOf(totalRecords.toString())/maxRecordPerBatch);
			List<TmProfileIndexBo> tmProfileList = null;

			//get TM data for each batch
			logger.info("Total Batches "+totalBatches);
			for(int i = 0;i<totalBatches.intValue();i++){
				recordIndex = maxRecordPerBatch*i;
				tmProfileList = tmsDao.getTmsData(company.getCompanyId(),recordIndex,maxRecordPerBatch,startPK,endPK);

				logger.info("Procssed batches of "+i+" / "+totalBatches);

				if(tmProfileList!=null && !tmProfileList.isEmpty()){

					Collection<SolrInputDocument> collDocObj = new ArrayList<SolrInputDocument>();
					for(TmProfileIndexBo tmProfile : tmProfileList){
						String tmProfileId = tmProfile.getTmProfileId();
						String source = tmProfile.getSource();
						String target = tmProfile.getTarget();
						String domain = tmProfile.getDomain();
						String productLine = tmProfile.getProductLine();
						String contentType = tmProfile.getContentType();
						String comanyId = tmProfile.getComanyId();
						String sourceLang = tmProfile.getSourceLang();
						String targetLang = tmProfile.getTargetLang();

						doc = new SolrInputDocument();


						if (tmProfileId != null)
							doc.addField("id", tmProfileId+SuffixTerm._TM.name());
						if (source != null){
							doc.addField("source", source.trim());
						}
						if (target != null){
							doc.addField("target", target.trim());
						}
						if (domain != null) {
							String domainLable = null;
							List<Domain> domainList = lookup.getDomain();
							for (Domain domainObj : domainList) {
								if (domainObj.getDomainId().intValue() == Integer.parseInt(domain)) {
									domainLable = domainObj.getDomain();
								}
							}

							if (domainLable != null)
								doc.addField("domain", domainLable);
						}

						if (productLine != null) {
							String productLable = null;
							List<ProductGroup> productGroupList = lookup.getProductGroup();
							for (ProductGroup productGroup : productGroupList) {
								if (productGroup.getProductId().intValue() == Integer.parseInt(productLine)) {
									productLable = productGroup.getProduct();
									break;
								}

							}

							if (productLable != null)
								doc.addField("product", productLable);

						}

						if (contentType != null) {
							String contentTypeLable = null;
							List<ContentType> contentTypeList = lookup.getContentType();
							for (ContentType contentTypeObj : contentTypeList) {
								if (contentTypeObj.getContentTypeId().intValue() == Integer.parseInt(contentType)) {
									contentTypeLable = contentTypeObj.getContentType();
									break;
								}

							}

							if (contentTypeLable != null)
								doc.addField("contenttype", contentTypeLable);
						}

						if(comanyId!=null){
							String companyName = null;
							if(company.getCompanyId().intValue() == Integer.parseInt(comanyId)){
								companyName = company.getCompanyName();
							}

							if(companyName!=null){
								doc.addField("company", companyName);
							}
						}

						doc.addField("companyId", comanyId);

						List<Language> languageList = null;
						if (sourceLang != null) {
							String sourceLangLable = null;
							languageList  = lookup.getLanguages();
							for (Language language : languageList) {
								if (language.getLanguageId().intValue() == Integer.parseInt(sourceLang)) {
									sourceLangLable = language.getLanguageLabel();
									break;
								}
							}

							if (sourceLangLable != null)
								doc.addField("sourcelang", sourceLangLable);
						}

						if (targetLang != null) {
							String targetLangLable = null;
							for (Language language : languageList) {
								if (language.getLanguageId().intValue() == Integer.parseInt(targetLang)) {
									targetLangLable = language.getLanguageLabel();
									break;
								}
							}

							if (targetLangLable != null)
								doc.addField("targetlang", targetLangLable);
						}

						doc.addField("termtype",IndexTypeEnum.TM.name());
						doc.addField("primarykey",tmProfileId);

						collDocObj.add(doc);
					}
					SolrUtil.saveData(collDocObj);
					collDocObj.clear();
					collDocObj = null;
				}

				else{
					logger.info("No tms exists for company "+company.getCompanyId() +": "+company.getCompanyName());
				}
			}
		}
		catch (Exception e) {
			logger.error("***Failed to indexd tms data***");
			logger.error(e,e);
		}
	}



	public void indexTermBase(Company company,Integer startPk,Integer endPK){
		if(company == null){
			logger.info("Company doen't exist not indexing TermBase....");
		}

		String termBaseSuffix = SuffixTerm._TB.name();


		try{

			SolrInputDocument doc = null;
			Integer recordIndex = 0;
			Integer maxRecordPerBatch = 1000;

			//total TermBase records count
			Integer totalRecords = termBaseDao.getTermBaseRecordCount(company.getCompanyId()); 
			logger.info("Total Records found for :::"+totalRecords+" company::"+company.getCompanyName());

			//creating batches
			Double totalBatches = Math.ceil(Double.valueOf(totalRecords.toString())/maxRecordPerBatch);


			List<TermInformationBo> termInformation = null;

			//get TermBase data for each batch
			logger.info("Total Batches "+totalBatches);

			for(int i = 0;i<totalBatches.intValue();i++){

				recordIndex = maxRecordPerBatch*i;
				termInformation = termBaseDao.getTermBaseData(company.getCompanyId(),recordIndex,maxRecordPerBatch,
						startPk,endPK);
				logger.info("Procssed batches of "+i+"/"+totalBatches);


				//logger.info("Got "+number+" of TermBase to index for companyId "+company.getCompanyId()+" name: "+company.getCompanyName());


				logger.info("***TermInformation data***");

				if(termInformation!=null && !termInformation.isEmpty()){

					Collection<SolrInputDocument> collObj = new ArrayList<SolrInputDocument>();

					//process data in batches
					for(TermInformationBo term : termInformation){
						String termId = term.getTermId() + termBaseSuffix;
						String source =	term.getTermBeingPolled();
						String target =	term.getSuggestedTerm();	
						Integer pos = term.getTermPosId();
						Integer targetPos = term.getSuggestedTermPosId();
						Integer category =term.getTermCategoryID();
						Integer targetLang =	term.getSugestedTermLangId();
						Integer statusId =	term.getTermStatusId();
						Integer domainId =	term.getDomainId();
						Integer compId =term.getCompanyId();	
						String expireDate =	term.getExpiredDate();
						Integer deprecatedCount = term.getIsDeprecated()!= null ? Integer.parseInt(term.getIsDeprecated()) : null;

						Date expireDateObj = null;

						if(expireDate!=null){
							SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
							expireDateObj = formatter.parse(expireDate);
						}
						//Adding data to solr document object

						doc = new SolrInputDocument();

						if (term.getTermId() != null)
							doc.addField("id", termId);
						if (source != null){

							doc.addField("source", source.trim());
						}
						if (target != null){
							doc.addField("target", target.trim());
						}
						if (domainId != null) {
							String domainLable = null;
							List<Domain> domainList = lookup.getDomain();
							for (Domain domainObj : domainList) {
								if (domainObj.getDomainId().intValue() == domainId.intValue()) {
									domainLable = domainObj.getDomain();
									break;
								}
							}

							if (domainLable != null)
								doc.addField("domain", domainLable);
						}


						if (pos != null) {
							String posLable = null;
							List<PartsOfSpeech> partOfSpeechList = lookup.getPartsOfSpeech();
							for (PartsOfSpeech partOfSpeech : partOfSpeechList) {
								if (partOfSpeech.getPartsOfSpeechId().intValue() == pos) {
									posLable = partOfSpeech.getPartOfSpeech();
									break;
								}

							}

							if (posLable != null)
								doc.addField("partofspeech", posLable);
						}
						
						if (targetPos != null) {
							String posLable = null;
							List<PartsOfSpeech> partOfSpeechList = lookup.getPartsOfSpeech();
							for (PartsOfSpeech partOfSpeech : partOfSpeechList) {
								if (partOfSpeech.getPartsOfSpeechId().intValue() == targetPos) {
									posLable = partOfSpeech.getPartOfSpeech();
									break;
								}

							}

							if (posLable != null)
								doc.addField("targetpartofspeech", posLable);
						}

						if (category != null) {
							String categoryLable = null;
							List<Category> categoryList = lookup.getCategory();
							for (Category categoryObj : categoryList) {
								if (categoryObj.getCategoryId().intValue() == category) {
									categoryLable = categoryObj.getCategory();
									break;
								}

							}
							if (categoryLable != null)
								doc.addField("category", categoryLable);
						}

						if(compId!=null){
							String companyName = null;

							if(company.getCompanyId().intValue() == compId){
								companyName = company.getCompanyName();
							}
							if(companyName!=null){
								doc.addField("company", companyName);
							}
						}
						doc.addField("companyId", compId);

						if (statusId != null) {

							String statusLable = null;
							List<Status> statusList = lookup.getStatus();

							if(statusList!=null && !statusList.isEmpty()){

								for (Status status : statusList) {
									if (status.getStatusId().intValue() == statusId) {
										statusLable = status.getStatus();
										break;
									}
								}

								if (statusLable != null)
									doc.addField("status", statusLable);
							}
						}

						if (targetLang != null) {

							String targetLangLable = null;
							List<Language> languageList =  lookup.getLanguages();

							if(languageList!=null && !languageList.isEmpty()){

								for (Language language : languageList) {
									if (language.getLanguageId().intValue() ==targetLang) {
										targetLangLable = language.getLanguageLabel();
										break;
									}
								}

								if (targetLangLable != null)
									doc.addField("targetlang", targetLangLable);
							}
						}

						if (expireDateObj != null) {
							doc.addField("expireDate", expireDateObj);
						}

						doc.addField("termtype", IndexTypeEnum.TB.name());
						doc.addField("primarykey",term.getTermId());
						if(deprecatedCount!=null && deprecatedCount>0){

							List<Object> listObject = lookup.getDepcrecatedTerm(term.getTermId());
							String deprecatedTerm = "";
							if(!listObject.isEmpty()){

								int count = 1;
								for(Object deprecatedObj : listObject){

									
									if(count==4){
										break;
									}
									
									String deprecatedSource = null;
									String deprecatedTarget = null;
									Object[] deprecatedArrayObj = (Object[]) deprecatedObj;
									int colNdx = 0;

									if ((String) deprecatedArrayObj[colNdx] != null) {
										deprecatedSource = (String)deprecatedArrayObj[colNdx];
										if(deprecatedSource!=null){
											deprecatedTerm = deprecatedTerm + deprecatedSource + " ";
										}
									}
									colNdx++;
									if ((String) deprecatedArrayObj[colNdx] != null) {
										deprecatedTarget = (String)deprecatedArrayObj[colNdx];
										if(deprecatedTarget!=null){
											deprecatedTerm = deprecatedTerm + deprecatedTarget + " ";
										}
									}

									doc.addField("deprecatedsource"+count, deprecatedSource);
									doc.addField("deprecatedtarget"+count, deprecatedTarget);
									count++;
									
									
								}

								if(!deprecatedTerm.isEmpty()){
									doc.addField("deprecated",deprecatedTerm);
								}

							}
						}
						collObj.add(doc);
					}

					// Save the data in solr database
					logger.info("Saving TermBase data to solr");
					SolrUtil.saveData(collObj);
					collObj.clear();
					collObj = null;
				}
				else{
					logger.info("No TermBase exists for company "+company.getCompanyId() +": "+company.getCompanyName());
				}
			}

		}catch(Exception e){
			logger.error("****Error in  indexing termbase data****");
			logger.error(e,e);
		}
	}


	/**
	 * 
	 * @param update tms when ui changes occur.
	 * @param fileId
	 */
	public void updateOrIndexTms(Set<Integer> tmIds , String fileId){

		List<TmProfileIndexBo> updatedOrInsertedList = null;

		//Get total TMS based on tm ids
		updatedOrInsertedList = tmsDao.getTmProfileInfoByIds(tmIds);


		Collection<SolrInputDocument> collDocObj = new ArrayList<SolrInputDocument>();

		if(updatedOrInsertedList!=null && !updatedOrInsertedList.isEmpty()){
			try {
				for (TmProfileIndexBo tmProfile : updatedOrInsertedList) {

					String tmProfileId = tmProfile.getTmProfileId().toString();
					String source = tmProfile.getSource();
					String target = tmProfile.getTarget();
					Integer domain = tmProfile.getDomain()!=null ? Integer.parseInt(tmProfile.getDomain()) : null;
					Integer  productLine = tmProfile.getProductLine()!=null ? Integer.parseInt(tmProfile.getProductLine()) : null;
					Integer contentType = tmProfile.getContentType()!=null ? Integer.parseInt(tmProfile.getContentType()) : null;
					Integer companyId = tmProfile.getComanyId()!=null ? Integer.parseInt(tmProfile.getComanyId()) : null;
					Integer sourceLang = tmProfile.getSourceLang()!=null ? Integer.parseInt(tmProfile.getSourceLang()) : null;
					Integer targetLang = tmProfile.getTargetLang()!=null ? Integer.parseInt(tmProfile.getTargetLang()) : null;

					//adding tms data to solr document
					SolrInputDocument doc = new SolrInputDocument();

					if (tmProfileId != null)
						doc.addField("id", tmProfileId+SuffixTerm._TM.name());
					if (source != null){
						doc.addField("source", source);
					}
					if (target != null){
						doc.addField("target", target);
					}
					if (domain != null) {
						String domainLable = null;
						List<Domain> domainList = lookup.getDomain();
						for (Domain domainObj : domainList) {
							if (domainObj.getDomainId().intValue() == domain.intValue()) {
								domainLable = domainObj.getDomain();
							}
						}

						if (domainLable != null)
							doc.addField("domain", domainLable);
					}

					if (productLine != null) {
						String productLable = null;
						List<ProductGroup> productGroupList = lookup.getProductGroup();
						for (ProductGroup productGroup : productGroupList) {
							if (productGroup.getProductId().intValue() ==productLine.intValue()) {
								productLable = productGroup.getProduct();
								break;
							}

						}

						if (productLable != null)
							doc.addField("product", productLable);

					}

					if (contentType != null) {
						String contentTypeLable = null;
						List<ContentType> contentTypeList = lookup.getContentType();
						for (ContentType contentTypeObj : contentTypeList) {
							if (contentTypeObj.getContentTypeId().intValue() == contentType.intValue()) {
								contentTypeLable = contentTypeObj.getContentType();
								break;
							}

						}

						if (contentTypeLable != null)
							doc.addField("contenttype", contentTypeLable);
					}

					if(companyId!=null){
						String companyName = null;
						Company company = companyDAO.getCompany(companyId);
						if(company.getCompanyId().intValue() == companyId.intValue()){
							companyName = company.getCompanyName();
						}

						if(companyName!=null){
							doc.addField("company", companyName);
						}
					}

					doc.addField("companyId", companyId);

					List<Language> languageList = null;
					if (sourceLang != null) {
						String sourceLangLable = null;
						languageList  = lookup.getLanguages();
						for (Language language : languageList) {
							if (language.getLanguageId().intValue() == sourceLang) {
								sourceLangLable = language.getLanguageLabel();
								break;
							}
						}

						if (sourceLangLable != null)
							doc.addField("sourcelang", sourceLangLable);
					}

					if (targetLang != null) {
						String targetLangLable = null;
						for (Language language : languageList) {
							if (language.getLanguageId().intValue() == targetLang) {
								targetLangLable = language.getLanguageLabel();
								break;
							}
						}

						if (targetLangLable != null)
							doc.addField("targetlang", targetLangLable);
					}
					doc.addField("termtype", IndexTypeEnum.TM.name());
					doc.addField("primarykey",tmProfileId);
					collDocObj.add(doc);

				}

				logger.info("Saving TM data to solr");
				SolrUtil.saveData(collDocObj);
				collDocObj.clear();
				collDocObj = null;

				//Update file upload status 
				FileUploadStatusImp fileUploadStatus = tmsDao.getFileUploadStatus(Integer.parseInt(fileId));

				if (fileUploadStatus != null) {
					if (fileUploadStatus.getProcessedPercentage() != null && fileUploadStatus.getProcessedPercentage().intValue() == 100) {
						fileUploadStatus.setFileStatus("Import Completed");
						tmsDao.updateFileUploadStatus(fileUploadStatus);
					}
				}
			}catch(Exception e){
				logger.info("Falied to update or insert tms");
				logger.error(e,e);
			}
		}

	}


}
