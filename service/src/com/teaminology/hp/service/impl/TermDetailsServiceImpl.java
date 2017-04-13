package com.teaminology.hp.service.impl;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.Utils;
import com.teaminology.hp.Utils.SortOrder;
import com.teaminology.hp.bo.CompanyTransMgmt;
import com.teaminology.hp.bo.DeprecatedTermInformation;
import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.FileUploadStatus;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.GlobalsightTerms;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermTranslation;
import com.teaminology.hp.bo.TermUpdateDetails;
import com.teaminology.hp.bo.TermVoteMaster;
import com.teaminology.hp.bo.TermVoteUserDetails;
import com.teaminology.hp.bo.TmProfileInfo;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.lookup.Category;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.ContentType;
import com.teaminology.hp.bo.lookup.Domain;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.bo.lookup.PartsOfSpeech;
import com.teaminology.hp.bo.lookup.ProductGroup;
import com.teaminology.hp.bo.lookup.Status;
import com.teaminology.hp.dao.ILookUpDAO;
import com.teaminology.hp.dao.IUserDAO;
import com.teaminology.hp.dao.IUtilDAO;
import com.teaminology.hp.dao.TermDetailsDAO;
import com.teaminology.hp.data.GSJobObject;
import com.teaminology.hp.data.HistoryDetailsData;
import com.teaminology.hp.data.Invite;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.PollTerms;
import com.teaminology.hp.data.QueryAppender;
import com.teaminology.hp.data.SuggestedTermDetails;
import com.teaminology.hp.data.TMProfileTerms;
import com.teaminology.hp.data.TMProfileTermsResult;
import com.teaminology.hp.data.TermAttributes;
import com.teaminology.hp.data.TermVotingTo;
import com.teaminology.hp.data.Terms;
import com.teaminology.hp.data.UserComment;
import com.teaminology.hp.data.VotingStatus;
import com.teaminology.hp.service.ITermDetailsService;
import com.teaminology.hp.service.enums.IndexTypeEnum;
import com.teaminology.hp.service.enums.SolrDocumentFieldEnum;
import com.teaminology.hp.service.enums.StatusLookupEnum;
import com.teaminology.hp.service.enums.SuffixEnum;
import com.teaminology.hp.service.enums.TeaminologyProperty;
import com.teaminology.solrInstance.HttpSolrServerInstance;

/**
 * Contains methods which deals with terms.
 *
 * @author sarvanic
 */
@Service
@Transactional
public class TermDetailsServiceImpl implements ITermDetailsService
{


	private Logger logger = Logger.getLogger(TermDetailsServiceImpl.class);
	private TermDetailsDAO termDetailsDAO;
	private IUtilDAO utilDAO;
	private ILookUpDAO iLookUpDAO;
	private IUserDAO iUserDAO;
	public static List<TMProfileTerms> alltmProfileTermsList = null;

	HttpSolrServer server =HttpSolrServerInstance.INSTANCE.getServer();

	@Autowired
	public TermDetailsServiceImpl(TermDetailsDAO termDetailsDAO, IUtilDAO utilDAO, ILookUpDAO iLookUpDAO, IUserDAO iUserDAO) {
		this.termDetailsDAO = termDetailsDAO;
		this.utilDAO = utilDAO;
		this.iLookUpDAO = iLookUpDAO;
		this.iUserDAO = iUserDAO;
		server.setParser(new XMLResponseParser());
	}

	@Transactional(readOnly = true)
	@Qualifier("txManager")

	/**
	 * To get Leader Board Members
	 * @param companyId An Integer to be filtered
	 * @return List of leader board members
	 */
	public List<Member> getBoardMembers(Integer companyId) {

		List<Member> members = new ArrayList<Member>();
	        List<Member> boardMembers = new ArrayList<Member>();
	        try {
	            members = termDetailsDAO.getBoardMembers(companyId);
	            if ((members != null) && (members.size() > 0)) {
	                for (Member member : members) {
	                    if (member.getPhotoPath() == null) {
	                        member.setPhotoPath(TeaminologyProperty.PHOTO_NOT_AVAILABLE.getValue());
	                    }
	                    boardMembers.add(member);
	                }
	                logger.debug("Total Leader Board Members :" + members.size());
	            } else {
	                logger.debug("Got empty Leader Board Members");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  leader board members");
	            logger.error(e,e);
	        }

	        return boardMembers;

	}

	/**
	 * To get Terms in Glossary per year
	 *
	 * @param companyId An Integer set to filter terms
	 * @return List of data holding year and no of terms per year
	 */
	@Transactional
	public List<Terms> getTermsInGlossary(Set<Integer> companyIds) {
		
		 List<Terms> termsInGlossary = new ArrayList<Terms>();
	        try {
	            String companysCommaSeparated = Utils.getCommaSeparatedValues(companyIds);
	            termsInGlossary = termDetailsDAO.getTermsInGlossary(companysCommaSeparated);
	            if ((termsInGlossary != null) && (termsInGlossary.size() > 0)) {
	                logger
	                        .debug("Total termsInGlossary :"
	                                + termsInGlossary.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty termsInGlossary ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  terms in Glossary ");
	            logger.error(e,e);
	        }

	        return termsInGlossary;
	}

	/**
	 * To Get Debated Terms per year
	 *
	 * @param companyId An Integer set  to be filtered
	 * @return List of data holding year and no of terms per year
	 */
	@Transactional
	public List<Terms> getDebatedTerms(Set<Integer> companyIds) {
		  List<Terms> debatedTerms = new ArrayList<Terms>();
	        try {
	            String companysCommaSeparated = Utils.getCommaSeparatedValues(companyIds);
	            debatedTerms = termDetailsDAO.getDebatedTerms(companysCommaSeparated);
	            if ((debatedTerms != null) && (debatedTerms.size() > 0)) {
	                logger.info(" ++++  ++++ Total Debated Terms :" + debatedTerms.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty Debated Terms ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  Debated Terms");
	            logger.error(e,e);
	        }
	        return debatedTerms;
	}



	public List<TermVotingTo> getVoteResults(final String   fromDate, final String  toDate, Integer companyId){

		return termDetailsDAO.getVoteResults(fromDate,toDate,companyId);

	}




	/**
	 * To get Total terms in Glossary
	 *
	 * @param companyId An String  set to  filter the  terms
	 * @return An int value holding the total no of terms in glossary
	 */
	@Transactional
	public int getTotalTermsInGlossary(Set<Integer> companyIds) {
		
		int totalTermsInGlossary = 0;
		
		try {
			
			//Created SolrQuery object to send  query to solr
			SolrQuery query = new SolrQuery();

			String queryObj = "";
			Object[] companyIdArrObj = null;

			if(companyIds!=null){

				companyIdArrObj = companyIds.toArray();	

				for(int i=0;i<companyIdArrObj.length;i++){

					if(i == 0){
						
						queryObj = queryObj +"companyId:"+companyIdArrObj[0];
						
					}else{
						
						queryObj = queryObj + " OR " + "companyId:"+companyIdArrObj[i];
						
					}

				}
			}

			//Finds All Documents Available In Solr
			query.setQuery( "*:*" );
			
			//Added filter query If company Id exist
			if(companyIdArrObj!=null){
				query.addFilterQuery(queryObj);
			}
			
			query.addFilterQuery("termtype:"+IndexTypeEnum.TB.toString());

			QueryResponse rsp = server.query(query);

			SolrDocumentList solDocList = rsp.getResults();

			//Total Records From Solr
			Long recordsFound = solDocList.getNumFound();
			
			totalTermsInGlossary = recordsFound.intValue();
			
			if(totalTermsInGlossary>0){
				logger.info("+++ Total Records Available In solr +++ "+totalTermsInGlossary);
			}else{
				logger.info("+++ Got empty Total Terms In Glossary +++ "+totalTermsInGlossary);
			}
			
			
		}
		catch (Exception e) {
			logger.error("+++Error in getting  Total Terms In Glossary+++");
			logger.error(e, e);
		}

		return totalTermsInGlossary;
	}

	/**
	 * To get Total debated terms
	 *
	 * @param companyId An Integer set to  filter the  terms
	 * @return An integer value holding the total no of debated terms
	 */
	@Transactional
	public int getTotalDebatedTerms(Set<Integer> companyIds) {
		
		int totalDebatedTerms = 0;
		
	        try {
	            String companysCommaSeparated = Utils.getCommaSeparatedValues(companyIds);
	            totalDebatedTerms = termDetailsDAO.getTotalDebatedTerms(companysCommaSeparated);
	            if (totalDebatedTerms > 0) {
	                logger.info(" ++++  ++++ Total Terms Debated In Glossary :"
	                        + totalDebatedTerms);
	            } else {
	                logger.info(" ++++  ++++ Got empty Total Debated Terms In Glossary");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  Total Debated Terms In Glossary");
	            logger.error(e,e);
	        }

	        return totalDebatedTerms;
	}

	/**
	 * To get current debated terms
	 *
	 * @param companyId An Integer to  filter the  terms
	 * @return List of current debated terms
	 */
	@Transactional
	@Override
	public List<String> getTopTerms(Integer companyId) {
		  List<String> topTerms = new ArrayList<String>();
	        try {

	            topTerms = termDetailsDAO.getTopTerms(companyId);
	            if ((topTerms != null) && (topTerms.size() > 0)) {
	                logger.info(" ++++  ++++ Total Top Debated Terms :" + topTerms.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty Top Debated Terms ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  top debated terms");
	            logger.error(e,e);
	        }
	        return topTerms;
	}

	/**
	 * To get term attributes for a termID
	 *
	 * @param termId Integer which contains termId to be filtered
	 * @param userId Integer which contains userId to be filtered
	 * @return TermInformation w.r.t the term id
	 */
	@Override
	@Transactional
	public TermAttributes getTermAttributes(Integer termId, Integer userId) {
		
		   if (termId == null) {
	            throw new IllegalArgumentException("Invalid termId");
	        }
	        TermAttributes termAttributes = new TermAttributes();
	        TermInformation termInfo = new TermInformation();
	        List<DeprecatedTermInformation> deprecatedTermInfoList = new ArrayList<DeprecatedTermInformation>();
	        DeprecatedTermInformation deprecatedTermInfo = new DeprecatedTermInformation();
	        Set<DeprecatedTermInformation> deprecatedTermInfoset = new HashSet<DeprecatedTermInformation>();

	        try {
	            termInfo = (TermInformation) termDetailsDAO.getTermAttributes(termId);
	            PartsOfSpeech targetpos = iLookUpDAO.getPartsOfSpeech(termInfo.getSuggestedTermPosId());
	            termInfo.setTargetTermPOS(targetpos);
	            if (termInfo != null && termInfo.getDeprecatedTermInfo() != null && termInfo.getDeprecatedTermInfo().size() > 0) {
	                for (DeprecatedTermInformation deprecatedTermInformation : termInfo.getDeprecatedTermInfo()) {
	                    if (deprecatedTermInformation.getIsActive().equalsIgnoreCase("Y")) {
	                        deprecatedTermInfoList.add(deprecatedTermInformation);
	                    }
	                }
	                termInfo.setDeprecatedTermInfo(null);
	            }

	            //set term status
	            if(termInfo != null) {
	            	Status termStatus = iLookUpDAO.getStatus(termInfo.getTermStatusId());
	            	if(termStatus != null) {
	            		termInfo.setTermStatus(termStatus);
	            	}
	            }
	            
	            List<SuggestedTermDetails> suggestedTermDetails = termDetailsDAO.getSuggestedTerms(termId);
	            TermVoteUserDetails termVoteUserDetails = termDetailsDAO.getTermVoteUserDetails(termId, userId);
	            if (termVoteUserDetails != null && termVoteUserDetails.getVoteInviteStatus() != null && termVoteUserDetails.getVoteInviteStatus().equalsIgnoreCase("Y")) {
	                Integer termTranslationId = termVoteUserDetails.getTermTranslationId();
	                TermTranslation termTranslation = termDetailsDAO.getTermTranslation(termTranslationId);
	                termAttributes.setIsUserVoted("Y");
	                termAttributes.setVotedTerm(termTranslation.getSuggestedTerm());
	            } else {
	                termAttributes.setIsUserVoted("N");
	            }
	            termAttributes.setTermInfo(termInfo);
	            termAttributes.setDeprecatedTermInfo(deprecatedTermInfoList);
	            termAttributes.setSuggestedTermDetails(suggestedTermDetails);
	            if (termInfo != null) {
	                logger.info(" ++++  ++++ Got Term Attributes for term ID :" + termId);
	            } else {
	                logger.info(" ++++  ++++ Got empty Term Attributes for term ID ");
	            }

	        }
	        catch (Exception e) {
	        	logger.error(e,e);
	            logger.error("Error in getting  term attributes for term ID");
	        }
	        return termAttributes;
	}

	/**
	 * To get monthly glossary terms
	 *
	 * @param companyId An Integer to  filter the  terms
	 * @return List of data holding month and no of terms per month
	 */
	@Override
	@Transactional
	public List<Terms> getMonthlyTermDetails(Integer companyId) {
		 List<Terms> monthlyTerms = new ArrayList<Terms>();
	        try {
	            monthlyTerms = termDetailsDAO.getMonthlyTermDetails(companyId);
	            if ((monthlyTerms != null) && (monthlyTerms.size() > 0)) {
	                logger.info(" ++++  ++++ Total Monthly Terms :" + monthlyTerms.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty Monthly Terms ");
	            }
	        }
	        catch (Exception e) {
	        	logger.error(e,e);
	            logger.error("Error in getting  monthly term details");
	        }
	        return monthlyTerms;
	}

	/**
	 * To get monthly debated term details
	 *
	 * @param companyId An Integer to  filter the  terms
	 * @return List of data holding month and no of terms per month
	 */
	@Override
	@Transactional
	public List<Terms> getMonthlyDebatedTerms(Integer companyId) {
		
		  List<Terms> monthlyDebatedTerms = new ArrayList<Terms>();
	        try {
	            monthlyDebatedTerms = termDetailsDAO.getMonthlyDebatedTerms(companyId);
	            if ((monthlyDebatedTerms != null)
	                    && (monthlyDebatedTerms.size() > 0)) {
	                logger.info(" ++++  ++++ Total Monthly Debated Terms :"
	                        + monthlyDebatedTerms.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty Monthly Debated Terms ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  monthly debated terms");
	            logger.error(e,e);
	        }
	        return monthlyDebatedTerms;

	}


	/**
	 * To get quarterly term details
	 *
	 * @param companyId An Integer to  filter the  terms
	 * @return List of data holding quarter-year and no of terms per quarter
	 */
	@Override
	@Transactional
	public List<Terms> getQuarterlyTermDetails(Integer companyId) {
		
		  List<Terms> quarterlyTerms = new ArrayList<Terms>();
	        try {
	            quarterlyTerms = termDetailsDAO.getQuarterlyTermDetails(companyId);
	            if ((quarterlyTerms != null) && (quarterlyTerms.size() > 0)) {
	                logger.info("  ++++ Total Quarterly Terms :" + quarterlyTerms.size());
	            } else {
	                logger.info(" ++++  Got empty Quarterly Terms ");
	            }
	        }
	        catch (Exception e) {
	        	logger.error(e,e);
	            return null;
	        }
	        return quarterlyTerms;


	}

	/**
	 * To get quarterly debated term details
	 *
	 * @param companyId An Integer to  filter the  terms
	 * @return List of data holding quarter-year and no of terms per quarter
	 */
	@Override
	@Transactional
	public List<Terms> getQuarterlyDebatedTerms(Integer companyId) {
		
		 List<Terms> quarterlyDebatedTerms = new ArrayList<Terms>();
	        try {
	            quarterlyDebatedTerms = termDetailsDAO.getQuarterlyDebatedTerms(companyId);
	            if ((quarterlyDebatedTerms != null)
	                    && (quarterlyDebatedTerms.size() > 0)) {
	                logger.info(" ++++  ++++ Total Quarterly Debated Terms :"
	                        + quarterlyDebatedTerms.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty Quarterly Debated Terms ");
	            }

	        }
	        catch (Exception e) {

	        	logger.error(e,e);
	            return null;
	        }
	        return quarterlyDebatedTerms;
	}

	/**
	 * To update Term Details
	 *
	 * @param termInformation TermInformation that has to be updated
	 */
	@Override
	@Transactional
	public void updateTermDetails(TermInformation termInformation,  String isReplace) {
		
		 if (termInformation == null) {
	            throw new IllegalArgumentException("Invalid termInformation");
	        }
	        try {

	            TermInformation tmInfo = termDetailsDAO.updateTermDetails(termInformation, isReplace);

	            updateTermBaseIndex(tmInfo);
	            logger.info(" ++++  ++++ updated Term Details");
	        }
	        catch (Exception e) {
	            logger.error("Error in updating  term details");
	            logger.error(e,e);
	        }

	}

	/**
	 * To extend poll of expired term id
	 *
	 * @param termVoteMaster TermVoteMaster that has to be updated
	 */
	@Override
	@Transactional
	public void extendPoll(TermVoteMaster termVoteMaster) {
		  if (termVoteMaster == null) {
	            throw new IllegalArgumentException("Invalid termVoteMaster");
	        }
	        try {

	            termDetailsDAO.extendPoll(termVoteMaster);
	            TermInformation tmInfo = termDetailsDAO.getTermInformation(termVoteMaster.getTermId());

	            updateTermBaseIndex(tmInfo);
	            logger.info(" ++++  ++++ Updated Term Vote Master");

	        }
	        catch (Exception e) {
	            logger.error("Error in updating  term vote master");
	        }
	}

	/**
	 * To get Term Vote Master details of a termId
	 *
	 * @param termId An Integer to get details
	 * @return TermVoteMaster w.r.t the term id
	 */
	@Override
	@Transactional
	public TermVoteMaster getTermVoteMaster(Integer termId) {
		
		 if (termId == null) {
	            throw new IllegalArgumentException("Invalid termId");
	        }
	        TermVoteMaster termVoteMaster = null;
	        try {
	            termVoteMaster = termDetailsDAO.getTermVoteMaster(termId);

	            if (termVoteMaster != null) {
	                logger.info(" ++++  ++++ Got term Vote Master for term Id :" + termId);
	            } else {
	                termVoteMaster = new TermVoteMaster();
	                termVoteMaster.setTermId(termId);
	                logger.info(" ++++  ++++ Cannot get term Vote Master for term Id :"
	                        + termId);
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  term vote master details for term Id");
	        }
	        return termVoteMaster;

	}

	/**
	 * To approve a suggested term for a term Id
	 *
	 * @param suggestedTermId An Integer to get details
	 * @return If approved it returns 1 else 0
	 */
	@Override
	@Transactional
	public int approveSuggestedTerm(Integer suggestedTermId) {
		 int updateStatus = 0;
	        if (suggestedTermId == null) {
	            updateStatus = -1;
	            return updateStatus;
	        }
	        TermTranslation termTranslation = null;
	        TermInformation termInformation = null;
	        try {
	            termTranslation = termDetailsDAO.getTermTranslation(suggestedTermId);
	            String statusName = StatusLookupEnum.APPROVED.getValue();
	            Status status = iLookUpDAO.getStatusIdByLabel(statusName);
	            if (termTranslation != null) {
	                termInformation = termDetailsDAO.getTermInformation(termTranslation.getTermId());
	                
	                termInformation.setSuggestedTerm(termTranslation.getSuggestedTerm());
		            termInformation.setTermStatusId(status.getStatusId());
		            termInformation.setSuggestedTermStatusId(status.getStatusId());
		            termInformation.setUpdateDate(new Date());
		            termDetailsDAO.updateTermInfo(termInformation);
		            updateStatus = 1;
		            if (updateStatus == 1) {
		            	logger.info(" ++++  ++++ Approved term for suggested term Id :"
		            			+ suggestedTermId);
		            } else {
		            	logger.info(" ++++  ++++ Cannot approve term for suggested term Id :"
		                	+ suggestedTermId);
		            }
		            updateTermBaseIndex(termInformation);
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            logger.error("Error in approving  term for suggested term id");
	            updateStatus = -1;
	        }
	        return updateStatus;
	}

	/**
	 * To sort expired poll terms
	 *
	 * @param colName   Column name that has to be sorted
	 * @param sortOrder Order in which it has to be sorted
	 * @param langIds   String containing language id's
	 * @param companyId An Integer to filter terms
	 * @param pageNum   Integer to limit the data
	 * @return List of terms
	 */
	@Override
	@Transactional
	public List<PollTerms> sortOrFilterExpPollTerms(String colName,
			String sortOrder, String langIds, Integer pageNum, Integer companyId, String expTermCompanyIds) {
		 List<PollTerms> orderedPollTerms = new ArrayList<PollTerms>();
	        try {
	            String statusName = StatusLookupEnum.APPROVED.getValue();
	            Status status = iLookUpDAO.getStatusIdByLabel(statusName);
	            orderedPollTerms = termDetailsDAO.sortOrFilterExpPollTerms(colName,
	                    sortOrder, langIds, pageNum, companyId, expTermCompanyIds, status.getStatusId());
	            if ((orderedPollTerms != null) && (orderedPollTerms.size() > 0)) {
	                logger.info(" ++++  ++++ Total Expired Poll Terms :"
	                        + orderedPollTerms.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty Expired Poll Terms ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  expired poll terms");
	        }
	        return orderedPollTerms;
	}

	/**
	 * To vote a term
	 *
	 * @param termTranslation TermTranslation to count on vote
	 * @param userId          An Integer to set in updatedBy field
	 * @return If term is voted it returns success else failed
	 */
	@Override
	@Transactional
	public String voteTerm(TermTranslation termTranslation, Integer userId) {
		  String status = "";
	        if (termTranslation == null) {
	            status = "failed";
	            return status;
	        }
	        try {
	            status = termDetailsDAO.voteTerm(termTranslation, userId);
	            if (termTranslation != null && termTranslation.getTermId() != null) {
	                TermInformation termInfo = termDetailsDAO.getTermInformation(termTranslation.getTermId());
	                updateTermBaseIndex(termInfo);

	            }


	        }
	        catch (Exception e) {
	            logger.error("Error in voting the term");
	            e.printStackTrace();
	            status = "failed";
	        }

	        return status;
	}


	/**
	 * To Get user poll terms for a language Id
	 *
	 * @param languageId String to filter terms respectively
	 * @param colName    Column name that has to be sorted
	 * @param sortOrder  Order in which it has to be sorted
	 * @param pageNum    An Integer to limit the data
	 * @param userId     An Integer to filter terms respectively
	 * @return List of terms
	 */
	@Override
	@Transactional
	public List<PollTerms> getUserPollTerms(String languageId, String colName,
			String sortOrder, Integer pageNum, Integer userId) {
		  List<PollTerms> userPollTerms = new ArrayList<PollTerms>();
	        try {

	            userPollTerms = termDetailsDAO.getUserPollTerms(languageId,
	                    colName, sortOrder, pageNum, userId);
	            if ((userPollTerms != null) && (userPollTerms.size() > 0)) {
	                logger.info(" ++++  ++++ Total user Poll Terms :" + userPollTerms.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty user Poll Terms ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  user poll terms");
	        }
	        return userPollTerms;
	}

	/**
	 * To reject a term by user
	 *
	 * @param termId A termId to reject the invitation
	 * @param userId Id of the user who rejects the term
	 */
	@Override
	@Transactional
	public void rejectTerm(Integer termId, Integer userId) {
		if (termId == null) {
		}
		try {
			termDetailsDAO.rejectTerm(termId, userId);

		}
		catch (Exception e) {
			logger.error("Error in rejecting the term");
		}
	}

	/**
	 * Add new term.
	 *
	 * @param termInformation TermInformation that needs to be added
	 * @return If term is voted it returns success else failed
	 */
	@Transactional
	public String addNewTerm(final TermInformation termInformation) {
		 String status = "";
	        List<TermInformation> insertedTermInformationList = new ArrayList<TermInformation>();
	        List<TermInformation> updatedTermInformationList = new ArrayList<TermInformation>();
	        if (termInformation == null) {
	            status = "failed";
	            throw new IllegalArgumentException("Invalid term");

	        }

	        try {

	            status = termDetailsDAO.addNewTerm(termInformation);
	            if (status.equals("success")) {
	                insertedTermInformationList.add(termInformation);
	            } else if (status.equals("update")) {
	                updatedTermInformationList.add(termInformation);
	            }
	            updateTermBaseIndex(updatedTermInformationList);
	            addNewTermBaseIndex(insertedTermInformationList);
	            logger.info(" ++++  ++++ new term is added");

	        }
	        catch (Exception e) {
	            logger.error("Error in  adding  new term " + e);
	            e.printStackTrace();
	        }
	        return status;
	}

	/**
	 * Invite to vote
	 *
	 * @param termVoteMaster Invitation that need to be saved
	 * @param invite         Includes term id's that needs to be invited and the user id's
	 */
	@Override
	@Transactional
	public void inviteToVote(TermVoteMaster termVoteMaster, Invite invite) {
		 if (termVoteMaster == null) {
	            throw new IllegalArgumentException("Invalid termVoteMaster");
	        }

	        try {

	            termDetailsDAO.inviteToVote(termVoteMaster, invite);
	            if (invite != null && invite.getTermIds() != null && invite.getTermIds().length > 0) {
	                List<TermInformation> updatedTermInformationList = termDetailsDAO.getTermInfoByTermIds(invite.getTermIds());
	                updateTermBaseIndex(updatedTermInformationList);
	            }


	            logger.info(" ++++  ++++ new term is added");

	        }
	        catch (Exception e) {
	            logger.error("Error in inviting vote   " + e);
	        }

	}


	/**
	 * To manage poll terms for a language Id
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to  filter terms
	 * @param user          User to filter terms
	 * @return List of terms
	 */
	@Override
	@Transactional
	public TMProfileTermsResult getManagePollTerms(QueryAppender queryAppender, Integer companyId, User user) {
		List<PollTerms> finalPollTerms = new ArrayList<PollTerms>();
		TMProfileTermsResult tmProfileTermsResult = new TMProfileTermsResult();

		List<CompanyTransMgmt> companyTransManageMentSet = null;
		if (companyId != null) {
			if (user != null) {
				if (user.getCompany() != null) {
					companyId = user.getCompany().getCompanyId();
					if (user.getCompany().getCompanyName() != null && user.getCompany().getCompanyName().equalsIgnoreCase(TeaminologyProperty.INTERNAL_COMPANY.getValue())) {
						companyTransManageMentSet = iUserDAO.getCompanyTransMgmtUsers(user.getUserId());
					}
				}
			}
		}
		List<PollTerms> poleTermsList = new ArrayList<PollTerms>();
		Long startTimeInMS = System.currentTimeMillis();
		List<Company> companyList = utilDAO.getLookup(Company.class);
		Integer totalRecords =0;

		if (companyId != null) {
			if (companyTransManageMentSet == null) {
				poleTermsList = getAllTermBaseProfileTerms(queryAppender, companyId);
				totalRecords = queryAppender.getTotalRecords();
			} else {
				List<PollTerms> companyPolltermsList = null;
				for (CompanyTransMgmt companyTrans : companyTransManageMentSet) {
					companyPolltermsList = getAllTermBaseProfileTerms(queryAppender, companyTrans.getCompanyId());
					poleTermsList.addAll(companyPolltermsList);
					totalRecords = totalRecords+queryAppender.getTotalRecords();
				}
				companyPolltermsList.clear();
			}

		} else {
			List<PollTerms> companyPolltermsList = null;
			for (Company company : companyList) {
				companyPolltermsList = getAllTermBaseProfileTerms(queryAppender, company.getCompanyId());
				poleTermsList.addAll(companyPolltermsList);
				totalRecords = totalRecords+queryAppender.getTotalRecords();
			}
		}
		try {

			String termIds = "";
			if (poleTermsList != null && poleTermsList.size() > 0) {
				for (PollTerms pollTerms : poleTermsList) {
					termIds = termIds + pollTerms.getTermId() + ",";
				}
				termIds = termIds.substring(0, termIds.lastIndexOf(","));


				List<PollTerms> managePollTems = termDetailsDAO.getVotingDetailsByTermIds(termIds);
				for (PollTerms pollTerms : poleTermsList) {
					for (PollTerms poll : managePollTems) {
						if (pollTerms.getTermId().intValue() == poll.getTermId().intValue()) {
							PollTerms finalPollTerm = new PollTerms();
							finalPollTerm.setTermId(pollTerms.getTermId());
							finalPollTerm.setSourceTerm(pollTerms.getSourceTerm());
							finalPollTerm.setSuggestedTerm(pollTerms.getSuggestedTerm());
							finalPollTerm.setLanguage(pollTerms.getLanguage());
							finalPollTerm.setPartOfSpeech(pollTerms.getPartOfSpeech());
							finalPollTerm.setCategory(pollTerms.getCategory());
							finalPollTerm.setDomain(pollTerms.getDomain());
							finalPollTerm.setCompany(pollTerms.getCompany());
							finalPollTerm.setStatus(pollTerms.getStatus());
							finalPollTerm.setPollExpirationDt(pollTerms.getPollExpirationDt());
							finalPollTerm.setVotesPerTerm(poll.getVotesPerTerm());
							finalPollTerm.setInvites(poll.getInvites());
							finalPollTerm.setDeprecatedCount(poll.getDeprecatedCount());
							finalPollTerms.add(finalPollTerm);
						}
					}
				}
				managePollTems.clear();
				tmProfileTermsResult.setPollTermsList(finalPollTerms);
				tmProfileTermsResult.setTotalResults(totalRecords);
				managePollTems = null;
			}
		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms:::::::::::");
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}

		return tmProfileTermsResult;
	}
	
	@Override
	@Transactional
	public TMProfileTermsResult getManagePollTermsForPagination(QueryAppender queryAppender, Integer companyId, User user) {
		Long startTimeInMS = System.currentTimeMillis();
		List<PollTerms> finalPollTerms = new ArrayList<PollTerms>();
		TMProfileTermsResult tmProfileTermsResult = new TMProfileTermsResult();
		try {
			Integer totalRecords =0;
			List<PollTerms> poleTermsList = new ArrayList<PollTerms>();
			if (companyId != null) {
					poleTermsList = getAllTermBaseProfileTermsForPagination(queryAppender, companyId);
					totalRecords = queryAppender.getTotalRecords();
		  }
			String termIds = "";
			if (poleTermsList != null && poleTermsList.size() > 0) {
				for (PollTerms pollTerms : poleTermsList) {
					termIds = termIds + pollTerms.getTermId() + ",";
				}
				termIds = termIds.substring(0, termIds.lastIndexOf(","));

				List<PollTerms> managePollTems = termDetailsDAO.getVotingDetailsByTermIds(termIds);
				for (PollTerms pollTerms : poleTermsList) {
					for (PollTerms poll : managePollTems) {
						if (pollTerms.getTermId().intValue() == poll.getTermId().intValue()) {
							PollTerms finalPollTerm = new PollTerms();
							finalPollTerm.setTermId(pollTerms.getTermId());
							finalPollTerm.setSourceTerm(pollTerms.getSourceTerm());
							finalPollTerm.setSuggestedTerm(pollTerms.getSuggestedTerm());
							finalPollTerm.setLanguage(pollTerms.getLanguage());
							finalPollTerm.setPartOfSpeech(pollTerms.getPartOfSpeech());
							finalPollTerm.setCategory(pollTerms.getCategory());
							finalPollTerm.setDomain(pollTerms.getDomain());
							finalPollTerm.setCompany(pollTerms.getCompany());
							finalPollTerm.setStatus(pollTerms.getStatus());
							finalPollTerm.setPollExpirationDt(pollTerms.getPollExpirationDt());
							finalPollTerm.setVotesPerTerm(poll.getVotesPerTerm());
							finalPollTerm.setInvites(poll.getInvites());
							finalPollTerm.setDeprecatedCount(poll.getDeprecatedCount());
							finalPollTerms.add(finalPollTerm);
						}
					}
				}
				managePollTems.clear();
				tmProfileTermsResult.setPollTermsList(finalPollTerms);
				tmProfileTermsResult.setTotalResults(totalRecords);
				managePollTems = null;
			}
		}
		catch(Exception e) {
			logger.error("Error in getting  manage poll terms for pagination:::::::::::");
		}
		finally {
			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}
		
		return tmProfileTermsResult;
	}
	
	/**
	 * To delete term ids
	 *
	 * @param termIds   An Integer array that needs to be deleted
	 * @param companyId An Integer to be filtered
	 */
	@Override
	@Transactional
	public void deleteTerms(final Integer[] termIds, User user) {
		  if (termIds == null) {
	            throw new IllegalArgumentException("Invalid termIds");
	        }
	        try {
	            termDetailsDAO.deleteTerms(termIds,user);
	            deleteTermsInIndex(termIds, user.getCompany().getCompanyId());
	            logger.debug("delete Term Details");
	        }
	        catch (Exception e) {
	            logger.error("Error in deleting term");
	        }
	}


	/**
	 * To get termInformation
	 *
	 * @param exportBy    String to filter the terms
	 * @param selectedIds Integer Array of filter values
	 * @return List of terms
	 */
	@Override
	@Transactional
	public List<TermInformation> getTermInformation(String exportBy,
			Integer[] selectedIds, Integer companyIds) {
		if (exportBy.equalsIgnoreCase("Poll Expiration")) {
			selectedIds = termDetailsDAO.getDebatedTermIds();
		}
		return termDetailsDAO.getTermInformation(exportBy, selectedIds,companyIds);
	}


	/**
	 * To get termInformationTM
	 *
	 * @param exportBy    String to filter the terms
	 * @param selectedIds Integer Array of filter values
	 * @return List of terms
	 */
	@Override
	@Transactional
	public List<TermInformation> getTermInformationTM(String exportBy,
			Integer[] selectedIds) {
		if (exportBy.equalsIgnoreCase("Poll Expiration")) {
			selectedIds = termDetailsDAO.getDebatedTermIds();
		}
		return termDetailsDAO.getTermInformationTM(exportBy, selectedIds);
	}

	/**
	 * To Get user voted terms for a language Id
	 *
	 * @param languageId String to filter terms respectively
	 * @param colName    Column name that has to be sorted
	 * @param sortOrder  Order in which it has to be sorted
	 * @param pageNum    An Integer to limit the data
	 * @param userId     Integer to filter terms respectively
	 * @return List of terms that are voted by the user
	 */
	@Override
	@Transactional
	public List<PollTerms> getUserVotedTerms(String languageId, String colName, String sortOrder, Integer pageNum, Integer userId) {
		  List<PollTerms> userVotedTerms = new ArrayList<PollTerms>();
	        try {

	            userVotedTerms = termDetailsDAO.getUserVotedTerms(languageId, colName, sortOrder, pageNum, userId);
	            if ((userVotedTerms != null) && (userVotedTerms.size() > 0)) {
	                logger
	                        .debug("Total user voted Terms :"
	                                + userVotedTerms.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty user voted Terms ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  user voted terms");
	        }
	        return userVotedTerms;
	}

	/**
	 * To get top register languages
	 *
	 * @param companyId An Integer to filter languages
	 * @return List of top six registered languages
	 */
	@Override
	@Transactional
	public List<Language> getTopRegLangs(Integer companyId) {
		 List<Language> regLanguages = new ArrayList<Language>();
	        try {

	            regLanguages = termDetailsDAO.getTopRegLangs(companyId);
	            if ((regLanguages != null) && (regLanguages.size() > 0)) {
	                logger.info(" ++++  ++++ Total Top register languages :"
	                        + regLanguages.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty Top  register languages ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  top register languages");
	        }
	        return regLanguages;
	}

	/**
	 * To get list of termIds from term information by languageId
	 *
	 * @param languageId An Integer to be filtered
	 * @param companyId  An Integer to be filtered
	 * @return List of term id's
	 */
	@Override
	@Transactional
	public List<Integer> getTermInformationByLanguage(Integer languageId, Integer companyId) {
		 List<Integer> termInformationList = null;
	        if (languageId == null)
	            return termInformationList;
	        try {

	            termInformationList = termDetailsDAO.getTermInformationByLanguage(languageId, companyId);
	            if ((termInformationList != null) && (termInformationList.size() > 0)) {
	                logger.info(" ++++  ++++ TermInformation :"
	                        + termInformationList.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty TermInformation ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  term information using languages");
	        }
	        return termInformationList;
	}

	/**
	 * To get list of termIds from term information by languageId and statusId
	 *
	 * @param languageId An Integer to filter terms
	 * @param statusId   An Integer to filter terms
	 * @param companyId  An Integer to filter the terms
	 * @return List of term id's
	 */

	@Override
	@Transactional
	public List<Integer> getTermInformationByLanguageAndStatus(Integer languageId, Integer statusId, Integer companyId) {
		  List<Integer> termInformationList = null;
	        if (languageId == null)
	            return termInformationList;
	        try {

	            termInformationList = termDetailsDAO.getTermInformationByLanguageAndStatus(languageId, statusId, companyId);
	            if ((termInformationList != null) && (termInformationList.size() > 0)) {
	                logger.info(" ++++  ++++ TermInformation :"
	                        + termInformationList.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty TermInformation ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting term information");
	        }
	        return termInformationList;
	}


	/**
	 * To get list of term information by languageIds in a year
	 *
	 * @param languageIds Set of languageIds to  filter terms
	 * @param companyId   An Integer set to filter terms
	 * @return List of TermInformation in a year
	 */
	@Override
	public List<TermInformation> getTermInformationPerMonth(Set<Integer> languageIds, Integer companyId) {
		List<TermInformation> termInformationList = null;
		if (languageIds == null || languageIds.isEmpty())
			return termInformationList;
		try {

			termInformationList = termDetailsDAO.getTermInformationPerMonth(languageIds, companyId);
			if ((termInformationList != null) && (termInformationList.size() > 0)) {
				logger.error("TermInformation :"
						+ termInformationList.size());
			} else {
				logger.error("Got empty TermInformation ");
			}

		}
		catch (Exception e) {
			logger.error("Error in getting term information using languages for last one year ");
		}
		return termInformationList;
	}

	/**
	 * To get count of term information other than top 6 registered languages
	 *
	 * @param languageIds Set of languageIds to be filtered
	 * @param companyId   An Integer set to be filtered
	 * @return an integer value holding the count of term id's for languages other than top 6 registered
	 */
	@Override
	@Transactional
	public Integer getTermInformationForOtherLanguage(Set<Integer> languageIds, Integer companyId) {

		Integer count = 0;
		if (languageIds == null || languageIds.isEmpty())
			return count;
		try {

			count = termDetailsDAO.getTermInformationForOtherLanguage(languageIds, companyId);

		}
		catch (Exception e) {
			logger.error("Error in getting  term information using other languages");
		}
		return count;


	}

	/**
	 * To get list of debated terms for a term
	 *
	 * @param termInformationList List of term objects from which it is to be filtered
	 * @param type                String for the type to be filtered
	 * @return List of debated terms from a given list of term id's
	 */
	@Override
	@Transactional
	public List<TermVoteMaster> getTermVoteMasterByTermInfo(List<Integer> termInformationList, String type) {
		  List<TermVoteMaster> termVoteMasterList = null;
	        if (termInformationList == null)
	            return termVoteMasterList;
	        Set<Integer> termIds = new HashSet<Integer>();
	        for (Integer termInformation : termInformationList) {
	            termIds.add(termInformation);
	        }
	        if (termIds.isEmpty())
	            return termVoteMasterList;

	        try {
	            if (type.equalsIgnoreCase("ChartData")) {
	                termVoteMasterList = termDetailsDAO.getTermVoteMasterByTermInfo(termIds);
	            } else if (type.equalsIgnoreCase("TableData")) {
	                termVoteMasterList = termDetailsDAO.getCurrentDebatedTerms(termIds);
	            }
	            if ((termVoteMasterList != null) && (termVoteMasterList.size() > 0)) {
	                logger.info(" ++++  ++++ TermInformation :"
	                        + termVoteMasterList.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty TermInformation ");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  term vote master details using term information");
	        }
	        return termVoteMasterList;


	}

	/**
	 * To get TermList for reports
	 *
	 * @param termVoteMasterList  Holds the debated terms
	 * @param termInformationList Holds the glossary terms
	 * @return List of terms for report
	 */
	@Override
	@Transactional
	public List<Terms> getTermList(List<TermVoteMaster> termVoteMasterList, List<TermInformation> termInformationList) {
		 List<Terms> termsList = new ArrayList<Terms>();
	        List<Terms> termsvoteMasterList = new ArrayList<Terms>();
	        List<Terms> termsInformationList = new ArrayList<Terms>();
	        List<Terms> finaltermsvoteMasterList = new ArrayList<Terms>();
	        List<Terms> finaltermsInformationList = new ArrayList<Terms>();


	        List<Terms> finalTermList = new ArrayList<Terms>();
	        try {
	            Map<String, List<TermVoteMaster>> termVoterMap = new HashMap<String, List<TermVoteMaster>>();
	            Map<String, List<TermInformation>> termInformationMap = new HashMap<String, List<TermInformation>>();

	            Integer[] months = new Integer[12];
	            Integer[] year = new Integer[12];

	            DateFormatSymbols dfs = new DateFormatSymbols();
	            Calendar beginCalendar = Calendar.getInstance();
	            beginCalendar.add(Calendar.MONTH, -12);
	            int i = 0;
	            while (i < 12) {
	                // ... calculations
	                beginCalendar.add(Calendar.MONTH, 1);
	                months[i] = beginCalendar.get(Calendar.MONTH);
	                year[i] = beginCalendar.get(Calendar.YEAR);
	                i++;
	            }

	            SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy");
	            if (termVoteMasterList != null) {
	                for (TermVoteMaster termVoteMaster : termVoteMasterList) {
	                    String date = sdf.format(termVoteMaster.getInvitedDate());
	                    List<TermVoteMaster> termVoteList = new ArrayList<TermVoteMaster>();
	                    if (!termVoterMap.containsKey(date)) {
	                        termVoteList.add(termVoteMaster);
	                        termVoterMap.put(date, termVoteList);
	                    } else {
	                        List<TermVoteMaster> tempList = termVoterMap.get(date);
	                        tempList.add(termVoteMaster);
	                        termVoterMap.put(date, tempList);
	                    }
	                }
	            }

	            if (termInformationList != null) {
	                for (TermInformation termInformation : termInformationList) {
	                    String date = sdf.format(termInformation.getCreateDate());
	                    List<TermInformation> termInfoList = new ArrayList<TermInformation>();
	                    if (!termInformationMap.containsKey(date)) {
	                        termInfoList.add(termInformation);
	                        termInformationMap.put(date, termInfoList);
	                    } else {
	                        List<TermInformation> tempList = termInformationMap.get(date);
	                        tempList.add(termInformation);
	                        termInformationMap.put(date, tempList);
	                    }
	                }
	            }

	            for (String dateStr : termVoterMap.keySet()) {
	                List<TermVoteMaster> termVoteList = termVoterMap.get(dateStr);
	                Terms terms = new Terms();
	                Date date = (Date) sdf.parse(dateStr);
	                if (!termVoteList.isEmpty()) {
	                    SimpleDateFormat monthFormate = new SimpleDateFormat("MM");
	                    SimpleDateFormat yearFormate = new SimpleDateFormat("yyyy");

	                    terms.setInterval(monthFormate.format(date));
	                    terms.setTermsPerInterval(termVoteList.size());
	                    terms.setYear(yearFormate.format(date));
	                }

	                termsvoteMasterList.add(terms);
	            }
	            for (String dateStr : termInformationMap.keySet()) {
	                List<TermInformation> termInfoList = termInformationMap.get(dateStr);
	                Terms terms = new Terms();
	                Date date = (Date) sdf.parse(dateStr);
	                if (!termInfoList.isEmpty()) {
	                    SimpleDateFormat monthFormate = new SimpleDateFormat("MM");
	                    SimpleDateFormat yearFormate = new SimpleDateFormat("yyyy");

	                    terms.setInterval(monthFormate.format(date));
	                    terms.setTermsPerInterval(termInfoList.size());
	                    terms.setYear(yearFormate.format(date));
	                }

	                termsInformationList.add(terms);
	            }
	            finaltermsvoteMasterList = getFinalTermsList(termsvoteMasterList, months, year);
	            finaltermsInformationList = getFinalTermsList(termsInformationList, months, year);
	            for (Terms termsInfo : finaltermsInformationList) {
	                for (Terms termsMaster : finaltermsvoteMasterList) {
	                    if (termsInfo.getInterval().equalsIgnoreCase(termsMaster.getInterval())) {
	                        Terms terms = new Terms();
	                        terms.setInterval(termsInfo.getInterval());
	                        terms.setTermsPerInterval(termsInfo.getTermsPerInterval().intValue() + termsMaster.getTermsPerInterval().intValue());
	                        terms.setYear(termsInfo.getYear());
	                        finalTermList.add(terms);
	                    }
	                }
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        return finalTermList;

	}

	/**
	 * To get Final term list for a given term ids
	 *
	 * @param termsList List of Terms that need to be finalised
	 * @param months    For ordering the terms based on months
	 * @param year      For ordering the terms based on year
	 * @return List of final terms that are used for reports
	 */
	@Transactional
	public List<Terms> getFinalTermsList(List<Terms> termsList, Integer[] months, Integer[] year) {
		  int k = 0;
	        List<Terms> finalTermList = new ArrayList<Terms>();
	        if (termsList != null) {
	            for (int j = 0; j < 12; j++) {
	                Terms term = null;
	                for (Terms terms : termsList) {
	                    if (terms.getInterval() != null)
	                        if ((new Integer(terms.getInterval()).intValue() - 1) == months[j].intValue()) {
	                            term = new Terms();
	                            term.setInterval(getMonthForInt(new Integer(terms.getInterval()) - 1));
	                            term.setTermsPerInterval(terms.getTermsPerInterval());
	                            term.setYear(terms.getYear());
	                            k++;
	                            break;
	                        }
	                }
	                if (term == null) {
	                    term = new Terms();
	                    term.setInterval(getMonthForInt(months[j]));
	                    term.setTermsPerInterval(0);
	                    term.setYear(year[k].toString());
	                    k++;
	                }
	                finalTermList.add(term);
	            }
	        } else {
	            for (int j = 0; j < 12; j++) {
	                Terms term = new Terms();
	                term.setInterval(getMonthForInt(months[j]));
	                term.setTermsPerInterval(0);
	                term.setYear(year[k].toString());
	                k++;
	                finalTermList.add(term);
	            }
	        }
	        return finalTermList;


	}

	String getMonthForInt(int m) {
		String month = "invalid";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getShortMonths();
		if (m >= 0 && m <= 11) {
			month = months[m];
		}
		return month;
	}


	/**
	 * To get total votes per language
	 *
	 * @param languageId An Integer to be filtered
	 * @param companyId  An Integer to be filtered
	 * @return an integer which contains the count of total votes
	 */
	@Override
	@Transactional
	public Integer getTotalVotesPerLang(Integer languageId, Integer companyId) {

		Integer count = 0;
		if (languageId == null)
			return count;
		try {

			count = termDetailsDAO.getTotalVotesPerLang(languageId, companyId);

		}
		catch (Exception e) {
			logger.error("Error in getting total votes per language");
		}
		return count;

	}


	/**
	 * To get monthly votes per language
	 *
	 * @param languageId An Integer to be filtered
	 * @param companyId  An Integer to be filtered
	 * @return an integer which contains the count of votes in a month
	 */
	@Override
	@Transactional
	public Integer getMonthlyVotesPerLang(String languageId, Integer companyId) {

		Integer count = 0;
		if (languageId == null)
			return count;
		try {

			count = termDetailsDAO.getMonthlyVotesPerLang(languageId, companyId);

		}
		catch (Exception e) {
			logger.error("Error in getting monthly votes per language");
		}
		return count;

	}

	/**
	 * To get Active Polls
	 *
	 * @param termVoteMasterList Collection holding list of objects
	 * @return an integer which contains the count of active invitations
	 */
	@Override
	@Transactional
	public Integer getActivePolls(List<TermVoteMaster> termVoteMasterList) {
		//		 List<TermVoteMaster> termVoteMasterList = null;
		  Integer count = 0;
	        try {

	            if (termVoteMasterList == null)
	                return 0;

	            Set<Integer> termIds = new HashSet<Integer>();
	            for (TermVoteMaster termVoteMaster : termVoteMasterList) {
	                termIds.add(termVoteMaster.getTermId());
	            }
	            if (termIds.isEmpty())
	                return 0;


	            count = termDetailsDAO.getActivePolls(termIds);

	        }
	        catch (Exception e) {
	            logger.error("Error in getting active polls using term vote master");
	        }
	        return count;
	}

	/**
	 * To get the users and termIds for which they are not invited
	 *
	 * @param invites Invite object that contains the list of users and the termIds
	 * @return Integer[] which contains the filtered users
	 */

	@Override
	@Transactional
	public Integer[] getInvitedUsers(Invite invites) {
		  Integer[] termIds = invites.getTermIds();
	        Integer[] userIds = invites.getUserIds();
	        int length = (userIds == null) ? 0 : userIds.length;
	        Integer[] finalUserIds = null;
	        List<Integer> userIdsList = new ArrayList<Integer>();
	        List<String> mailIdsList = new ArrayList<String>();

	        List<TermInformation> termInformation = termDetailsDAO.getTermInfoByTermIds(termIds);
	        List<User> usrList = termDetailsDAO.getUsersByuserIdsList(userIds);
	        int index = 0;
	        int mailIndex = 0;
	        
	        //Added  for TNG-81
	        List<TermInformation> termsList = null;
	        Map<String,List<TermInformation>> userIdWithTermIds = new HashMap<String, List<TermInformation>>();
	        
	        for (User user : usrList) {
	            Integer userId = user.getUserId();
	            if (userId != null) {
	            	
	                List<Language> userLanguagesList = iUserDAO.getUserRegLanguages(userId);
	                int inviteCount = 0;
	                termsList = new ArrayList<TermInformation>();
	                
	                for (TermInformation termInfo : termInformation) {
	                    Integer termId = termInfo.getTermId();
	                    Boolean isLanguageExists = false;
	                    Boolean companyFlag = false;
	                    
	                    if (termId != null) {
	                        TermVoteUserDetails termVoteUserDetails = termDetailsDAO.getTermVoteUserDetails(termId, userId);
	                        if (termVoteUserDetails == null) {
	                            if (termInfo.getTermCompany() != null) {
	                                List<CompanyTransMgmt> companyTransMgmtList = iUserDAO.getCompanyTransMgmtUsers(userId);
	                                for (CompanyTransMgmt companyTransMgmt : companyTransMgmtList) {
	                                    if (termInfo.getTermCompany().getCompanyId().intValue() == companyTransMgmt.getCompanyId().intValue()) {
	                                        companyFlag = true;
	                                        break;
	                                    }
	                                }
	                                if (companyFlag) {
	                                    Integer langId = termInfo.getSuggestedTermLangId();
	                                    if (userLanguagesList != null && userLanguagesList.size() > 0) {
	                                        for (Language userLang : userLanguagesList) {
	                                            if (userLang.getLanguageId() != null && langId != null) {
	                                                if (userLang.getLanguageId().intValue() == langId.intValue()) {
	                                                    isLanguageExists = true;
	                                                    break;
	                                                }

	                                            }
	                                        }
	                                    }
	                                    if (isLanguageExists) {
	                                    	
	                                    	termsList.add(termInfo);
	                                        inviteCount++;
	                                        
	                                    }
	                                }

	                            }

	                        }

	                    }
	                }
	                if (inviteCount != 0) {
	                    userIdsList.add(userId);
	                    mailIdsList.add(user.getEmailId());
	                    userIdWithTermIds.put(user.getEmailId(), termsList);
	                }
	            }
	        }
	        if (userIdsList != null && userIdsList.size() > 0) {
	            finalUserIds = new Integer[userIdsList.size()];
	            for (Integer userId : userIdsList) {
	                finalUserIds[index] = userId;
	                index++;
	            }

	        }
	        if (mailIdsList != null && mailIdsList.size() > 0) {
	            String finalmailIds[] = new String[mailIdsList.size()];
	            for (String mailId : mailIdsList) {
	                finalmailIds[mailIndex] = mailId;
	                mailIndex++;
	            }
	            invites.setMailIds(finalmailIds);
	        } else {
	            invites.setMailIds(null);
	        }
	        
	        invites.setUserPollTerms(userIdWithTermIds);
	        
	        return finalUserIds;
	}

	/**
	 * To get final terms by sorting the terms
	 *
	 * @param terms holds the termsList
	 * @param type  holds type possible values are "Yearly","Quarterly","Monthly"
	 * @return List of terms sorting accordingly
	 */
	@Transactional
	public List<Terms> getFinalisedTerms(List<Terms> terms, String type) {
        if (terms == null || terms.isEmpty()) {
            return terms;
        }
        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH);
        Integer quarter = (cal.get(Calendar.MONTH) / 3) + 1;
        List<Terms> finalTermsList = new ArrayList<Terms>();
        Map<String, Terms> termMap = new HashMap<String, Terms>();

/*		if(terms==null || terms.isEmpty()){
            if(type.equalsIgnoreCase("Yearly")){
				for(int i=0;i<=4;i++){
					Terms term = new Terms();
					term.setInterval(year.toString());
					term.setTermsPerInterval(0);
					year--;
					finalTermsList.add(term);
				}
			}
			if(type.equalsIgnoreCase("Quarterly")){
				for(int i=0;i<=4;i++){
					Terms term = new Terms();
					term.setInterval("Q"+quarter+"-"+year);
					term.setTermsPerInterval(0);
					if(quarter==1){
						quarter=5;
						year--;
					}
					quarter--;
					
					finalTermsList.add(term);
				}
			}
			if(type.equalsIgnoreCase("Monthly")){
				for(int i=0;i<=4;i++){
					Terms term = new Terms();
					term.setInterval(getMonthForInt(month));
					term.setTermsPerInterval(0);
					if(month==0){
						month=12;
					}
					
					month--;
					
					finalTermsList.add(term);
				}
			}
			
		}else{*/
        for (Terms termObj : terms) {
            termMap.put(termObj.getInterval(), termObj);
        }
        if (type.equalsIgnoreCase("Yearly")) {
            for (int i = 0; i <= 4; i++) {
                Terms term = termMap.get(year.toString());
                if (term == null) {
                    term = new Terms();
                    term.setInterval(year.toString());
                    term.setTermsPerInterval(0);
                }
                year--;
                finalTermsList.add(term);
            }
        }
        if (type.equalsIgnoreCase("Quarterly")) {
            for (int i = 0; i <= 4; i++) {
                Terms term = termMap.get("Q" + quarter + "-" + year);

                if (term == null) {
                    term = new Terms();
                    term.setInterval("Q" + quarter + "-" + year);
                    term.setTermsPerInterval(0);
                }

                if (quarter == 1) {
                    quarter = 5;
                    year--;
                }
                quarter--;

                finalTermsList.add(term);
            }
        }
        if (type.equalsIgnoreCase("Monthly")) {
            for (int i = 0; i <= 4; i++) {
                Terms term = termMap.get(getMonthForInt(month));
                if (term == null) {
                    term = new Terms();
                    term.setInterval(getMonthForInt(month));
                    term.setTermsPerInterval(0);
                }
                if (month == 0) {
                    month = 12;
                }

                month--;

                finalTermsList.add(term);
            }
        }
        //}
        Collections.reverse(finalTermsList);
        return finalTermsList;
   }

	/**
	 * To delete vote details for users
	 *
	 * @param userIds Integer for which vote data has to be updated
	 * @return Status if updated vote details successfully it returns "success" else "failure"
	 */
	@Override
	@Transactional
	 public String deleteTermVoteDetailsForUser(Integer[] userIds ,Integer userId) {
        String status = "";
        try {
            termDetailsDAO.deleteTermVoteDetailsForUser(userIds , userId);
            status = "success";
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in deleting vote details of user");
            status = "failure";
        }
        return status;
    }

	/**
	 * To get voting status of voted terms
	 *
	 * @param termId variable to get particular termId
	 * @return VotingStatusList w.r.t the term id
	 */

	@Override
	@Transactional
	public List<VotingStatus> getvotingStatus(Integer termId) {

        List<VotingStatus> votingStatusList = new ArrayList<VotingStatus>();
        try {
            List<Object> votingStatusObjList = termDetailsDAO.getvotingStatus(termId);
            for (Object obj : votingStatusObjList) {
                Object[] votingStatusObj = (Object[]) obj;
                int colNdx = 0;
                VotingStatus votingStatus = new VotingStatus();

                if ((Integer) votingStatusObj[colNdx] != null) {
                    votingStatus.setTermId((Integer) votingStatusObj[colNdx]);
                }
                colNdx++;
                if ((String) votingStatusObj[colNdx] != null) {
                    votingStatus.setUserName((String) votingStatusObj[colNdx]);
                }
                colNdx++;

                if ((String) votingStatusObj[colNdx] != null) {
                    String status = (String) votingStatusObj[colNdx];
                    if (status.equalsIgnoreCase("Y")) {
                        votingStatus.setVotingStatus("Voted");
                    } else {
                        votingStatus.setVotingStatus("Rejected");
                    }
                } else {
                    votingStatus.setVotingStatus("Pending");
                }
                colNdx++;

                if ((String) votingStatusObj[colNdx] != null) {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    votingStatus.setVotedDate(new SimpleDateFormat("MM/dd/yyyy").format(formatter.parse(votingStatusObj[colNdx].toString())));
                } else {
                    votingStatus.setVotedDate("&nbsp;");
                }
                colNdx++;
                if ((String) votingStatusObj[colNdx] != null) {
                    votingStatus.setUserComments((String) votingStatusObj[colNdx]);
                } else {
                    votingStatus.setUserComments("&nbsp;");
                }

                colNdx++;

                if ((String) votingStatusObj[colNdx] != null) {
                    votingStatus.setVotedTerm((String) votingStatusObj[colNdx]);
                } else {
                    votingStatus.setVotedTerm("&nbsp;");
                }
                votingStatusList.add(votingStatus);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in geting voting status");
        }
        return votingStatusList;
        }

	@Override
	@Transactional
	public List<HistoryDetailsData> getHistoryDatails(Integer termId) {

		List<HistoryDetailsData> historyDetailsList = null;
		if(termId != null) {
			try {
				historyDetailsList = new ArrayList<HistoryDetailsData>();
				List<Object>  historyObjList = termDetailsDAO.getHitoryDeata(termId);
				for (Object object : historyObjList) {
					Object[] historyDataDetails = (Object[]) object;
					int colNdx = 0;
					HistoryDetailsData historyData = new HistoryDetailsData();

					if ((String)historyDataDetails[colNdx] != null) {
						historyData.setUserName((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if ((String)historyDataDetails[colNdx] != null) {
						historyData.setHistorySourceTerm((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if ((String)historyDataDetails[colNdx] != null) {
						historyData.setChangedTargetTerm((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if((String)historyDataDetails[colNdx] != null) {
						historyData.setTermsPOS((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if((String)historyDataDetails[colNdx] != null) {
						historyData.setConceptDefinition((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if((String)historyDataDetails[colNdx] != null) {
						historyData.setTermForm((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if((String)historyDataDetails[colNdx] != null) {
						historyData.setTermStatus((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if((String)historyDataDetails[colNdx] != null) {
						historyData.setTermCategory((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if((String)historyDataDetails[colNdx] != null) {
						historyData.setTermDomain((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if((String)historyDataDetails[colNdx] != null) {
						historyData.setTermNotes((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if((String)historyDataDetails[colNdx] != null) {
						historyData.setTargetTermPOS((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					if((String)historyDataDetails[colNdx] != null) {
						historyData.setTermUsage((String)historyDataDetails[colNdx]);
					}
					colNdx++;
					/*if ((String)historyDataDetails[colNdx] != null) {
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						historyData.setHistoryDate(new SimpleDateFormat("MM/dd/yyyy").format(formatter.parse(historyDataDetails[colNdx].toString())));
					} else {
						historyData.setHistoryDate("&nbsp;");
					}*/
					if ((String)historyDataDetails[colNdx] != null) {
						String date = (historyDataDetails[colNdx].toString());
						String historyDate = date.substring(0, date.indexOf('.'));
						historyData.setHistoryDate(historyDate);
					}
					historyDetailsList.add(historyData);
				}


			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error in geting History Details");
			}
		}

		return historyDetailsList;
	}
	
	
	/**
	 * To get list of termIds from term information by categoryId
	 *
	 * @param categoryId An Integer to be filtered
	 * @return List of term id's
	 */
	@Override
	@Transactional
	public List<Integer> getTermInformationByCategory(Integer categoryId) {

        List<Integer> termInformationList = null;
        if (categoryId == null)
            return termInformationList;
        try {

            termInformationList = termDetailsDAO.getTermInformationByCategory(categoryId);
            if ((termInformationList != null) && (termInformationList.size() > 0)) {
                logger.info(" ++++  ++++ TermInformation :"
                        + termInformationList.size());
            } else {
                logger.info(" ++++  ++++ Got empty TermInformation ");
            }

        }
        catch (Exception e) {
            logger.error("Error in getting  term information using category");
        }
        return termInformationList;
   
	}

	/**
	 * To save term image
	 *
	 * @param termId    Path variable  to get particular attributes
	 * @param photoPath
	 */
	@Override
	@Transactional
	public void saveTermImage(Integer termId, String photoPath) {
		  if (termId == null) {
	            throw new IllegalArgumentException("Invalid termVoteMaster");
	        }
	        try {

	            termDetailsDAO.saveTermImage(termId, photoPath);
	            logger.info(" ++++  ++++ Updated Term Vote Master");

	        }
	        catch (Exception e) {
	            logger.error("Error in saving  term picture");
	        }

	}

	/**
	 * To get global sight term information
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to filter the terms
	 * @return terms
	 */
	@Override
	@Transactional
	public TMProfileTermsResult getGlobalSightTermInfo(QueryAppender queryAppender, Integer companyId) {
		List<GlobalsightTerms> globalsightTermInfoList = null;
		List<GlobalsightTerms> finalGlobalSightTermInfoList = null;
		TMProfileTermsResult tmProfileTermsResult = new TMProfileTermsResult();
		try {
			//globalsightTermInfoList=termDetailsDAO.getGlobalSightTermInfo(queryAppender,companyId);

			globalsightTermInfoList = new ArrayList<GlobalsightTerms>();
			finalGlobalSightTermInfoList = new ArrayList<GlobalsightTerms>();
			Long startTimeInMS = System.currentTimeMillis();
			Integer totalResults = null;
			List<Company> companyList = utilDAO.getLookup(Company.class);
			if (companyId != null) {
				globalsightTermInfoList = getGSTermsUsingSearchIndex(queryAppender, companyId);
				totalResults = queryAppender.getTotalRecords();
			} else {
				List<GlobalsightTerms> companyGStermsList = null;
				for (Company company : companyList) {
					companyGStermsList = getGSTermsUsingSearchIndex(queryAppender, company.getCompanyId());
					globalsightTermInfoList.addAll(companyGStermsList);
					totalResults = totalResults + queryAppender.getTotalRecords();
				}
			}
			try {
				String termIds = "";
				if (globalsightTermInfoList != null && globalsightTermInfoList.size() > 0) {
					for (GlobalsightTerms pollTerms : globalsightTermInfoList) {
						termIds = termIds + pollTerms.getGsTermId() + ",";
					}
					termIds = termIds.substring(0, termIds.lastIndexOf(","));


					List<PollTerms> managePollTems = termDetailsDAO.getVotingDetailsByTermIds(termIds);
					for (GlobalsightTerms pollTerms : globalsightTermInfoList) {
						for (PollTerms poll : managePollTems) {
							if (pollTerms.getGsTermId().intValue() == poll.getTermId().intValue()) {
								GlobalsightTerms gsTerms = new GlobalsightTerms();
								gsTerms.setGlobalsightTermInfoId(pollTerms.getGlobalsightTermInfoId());
								gsTerms.setSourceSegment(pollTerms.getSourceSegment());
								gsTerms.setTargetSegment(pollTerms.getTargetSegment());
								gsTerms.setSourceLang(pollTerms.getSourceLang());
								gsTerms.setTargetLang(pollTerms.getTargetLang());
								gsTerms.setOrigin(pollTerms.getOrigin());
								gsTerms.setPageId(pollTerms.getPageId());
								gsTerms.setTransUnitId(pollTerms.getTransUnitId());
								gsTerms.setGsTermId(pollTerms.getGsTermId());
								gsTerms.setJobId(pollTerms.getJobId());
								gsTerms.setJobName(pollTerms.getJobName());
								gsTerms.setTaskId(pollTerms.getTaskId());
								gsTerms.setTaskName(pollTerms.getTaskName());
								gsTerms.setTotalVotes(poll.getVotesPerTerm());
								gsTerms.setInvites(poll.getInvites());
								finalGlobalSightTermInfoList.add(gsTerms);

							}
						}
					}
					tmProfileTermsResult.setGlobalsightTermsList(finalGlobalSightTermInfoList);
					tmProfileTermsResult.setTotalResults(totalResults);
				}
			}
			catch (Exception e) {
				logger.error("Error in getting  manage poll terms");
			}
			finally {

				long endTimeInMS = System.currentTimeMillis();
				long totalTimeInMs = endTimeInMS - startTimeInMS;
				double totalTimeInSeconds = totalTimeInMs / 1000D;
				double totalTimeInMinutes = totalTimeInSeconds / 60D;
				String timeTook;
				if (totalTimeInMinutes >= 1D) {
					timeTook = totalTimeInMinutes + " minutes" + " or "
							+ totalTimeInSeconds + " seconds";
				} else {
					timeTook = totalTimeInSeconds + " seconds";
				}
				logger.info("Took total for TM terms" + timeTook);
				logger.info("startTimeInMinutes:" + startTimeInMS / 1000
						+ " ending time in Minutes:" + endTimeInMS / 1000);

			}
		}
		catch (Exception e) {
			logger.error("Failed to get GlobalSightTermInfo  details");
		}
		return tmProfileTermsResult;

	}

	/**
	 * To get file info list
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to  filter the  terms
	 * @return List of terms
	 */
	@Override
	@Transactional
	public List<GlobalsightTerms> getFileInfoList(QueryAppender queryAppender, Integer companyId) {
		 List<GlobalsightTerms> fileInfoList = null;
	        try {
	            fileInfoList = termDetailsDAO.getFileInfoList(queryAppender, companyId);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            logger.warn("Failed to get fileInfoList  details");
	        }
	        return fileInfoList;
	}

	/**
	 * To get Search manage poll terms for tms
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to  filter the tm terms
	 * @param user          to filter the tms terms
	 * @return List of terms
	 */
	@Override
	@Transactional
	public List<GSJobObject> getSearchManagePollTermsTM(QueryAppender queryAppender, Integer companyId, User user) {

		List<TMProfileTerms> tmProfileTermsList = new ArrayList<TMProfileTerms>();
		List<GSJobObject> managePollTerms = null;
		List<CompanyTransMgmt> companyTransManageMentSet = null;
		Long startTimeInMS = null;
		String status = null;

		try {
			if (companyId != null) {
				if (user != null) {
					if (user.getCompany() != null) {
						companyId = user.getCompany().getCompanyId();
						if (user.getCompany().getCompanyName() != null && user.getCompany().getCompanyName().equalsIgnoreCase(TeaminologyProperty.INTERNAL_COMPANY.getValue())) {
							companyTransManageMentSet = iUserDAO.getCompanyTransMgmtUsers(user.getUserId());
						}
					}
				}
			}

			startTimeInMS = System.currentTimeMillis();

			List<Company> companyList = utilDAO.getLookup(Company.class);

			if (companyId != null) {
				if (companyTransManageMentSet == null) {
					tmProfileTermsList = getTmSearchUsingSearchIndex(queryAppender, companyId);
				} else {
					List<TMProfileTerms> companyPolltermsList = null;
					for (CompanyTransMgmt companyTrans : companyTransManageMentSet) {
						companyPolltermsList = getTmSearchUsingSearchIndex(queryAppender, companyTrans.getCompanyId());
						if(companyPolltermsList!=null){
							tmProfileTermsList.addAll(companyPolltermsList);
						}
					}
					companyPolltermsList.clear();
				}

			} else {
				List<TMProfileTerms> companyPolltermsList = null;
				for (Company company : companyList) {
					companyPolltermsList = getTmSearchUsingSearchIndex(queryAppender, company.getCompanyId());
					if(companyPolltermsList!=null){
						tmProfileTermsList.addAll(companyPolltermsList);
					}
				}
				companyPolltermsList.clear();
			}
		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
			e.printStackTrace();
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}
		if (tmProfileTermsList != null && tmProfileTermsList.size() > 0) {
			managePollTerms = pollTMBaseList(tmProfileTermsList, queryAppender, managePollTerms);
		}
		if(queryAppender != null && "Y".equalsIgnoreCase( queryAppender.getIsReplaceTerm())) {
			if(queryAppender.getReplaceStr() != null && !queryAppender.getReplaceStr().equals("") 
					&& queryAppender.getSearchStr() != null && managePollTerms != null && !managePollTerms.isEmpty() && queryAppender.getSelectedIds() != null) {
				status = replaceTMBaseText(queryAppender, managePollTerms, queryAppender.getSearchTerm(), companyId, user);
				if("update".equalsIgnoreCase(status)) {
					queryAppender.setSearchTerm(queryAppender.getReplaceStr());
					queryAppender.setSearchReplacedTerm("replaced");
				}
			}
		}
		if("update".equals(status)) {
			if (companyTransManageMentSet == null) {
				tmProfileTermsList = getTmSearchUsingSearchIndex(queryAppender, companyId);
				if (tmProfileTermsList != null && tmProfileTermsList.size() > 0) {
					managePollTerms = pollTMBaseList(tmProfileTermsList, queryAppender, managePollTerms);
					managePollTerms.get(0).setTermReplaced("Y");
				}
			}else {
				for (CompanyTransMgmt companyTrans : companyTransManageMentSet) {
					tmProfileTermsList = getTmSearchUsingSearchIndex(queryAppender, companyTrans.getCompanyId());
					if (tmProfileTermsList != null && tmProfileTermsList.size() > 0) {
						managePollTerms = pollTMBaseList(tmProfileTermsList, queryAppender, managePollTerms);
					}
				}
				if (tmProfileTermsList != null && tmProfileTermsList.size() > 0) {
					managePollTerms.get(0).setTermReplaced("Y");
				}
			}
		}
		tmProfileTermsList.clear();
		tmProfileTermsList  = null;
		return managePollTerms;

	}

	private String replaceTMBaseText(QueryAppender queryAppender,
			List<GSJobObject> managePollTerms, String searchTerm, Integer companyId, User user) {
		String status = null;
        Integer tmProfileInfoId = 0;
        
		if(queryAppender != null && queryAppender.getReplaceStr() != null 
				&& managePollTerms != null && !managePollTerms.isEmpty() && companyId != null && user!= null  && queryAppender.getSelectedIds() != null) {
			TmProfileInfo tmProfileInfo = null;
			for (GSJobObject pollTerms : managePollTerms) {
				for (Integer selectTermIds : queryAppender.getSelectedIds()) {
					if(pollTerms != null && (pollTerms.getTermsBeingPolled().equalsIgnoreCase(searchTerm) || searchTerm.equalsIgnoreCase(pollTerms.getSuggestedterms()))  && pollTerms.getTmProfileId().equals(selectTermIds)) {
						tmProfileInfo = new TmProfileInfo();
						tmProfileInfoId = pollTerms.getTmProfileId();
						tmProfileInfo = termDetailsDAO.getTmAttributes(tmProfileInfoId);
						tmProfileInfo.setSource((pollTerms.getTermsBeingPolled().equalsIgnoreCase(searchTerm)) ? queryAppender.getReplaceStr() : pollTerms.getTermsBeingPolled());
						tmProfileInfo.setUpdateDate(new Date());
						tmProfileInfo.setUpdatedBy(user.getUserId());
						tmProfileInfo.setTarget((pollTerms.getSuggestedterms() != null && searchTerm.equalsIgnoreCase(pollTerms.getSuggestedterms())) ? queryAppender.getReplaceStr() : (pollTerms.getSuggestedterms() != null) ? pollTerms.getSuggestedterms() : "");
						updateTmDetails(tmProfileInfo, companyId);
						status = "update";
					}
				}
			}
		}
		return status;
	}

	private List<GSJobObject> pollTMBaseList(List<TMProfileTerms> tmProfileTermsList, QueryAppender queryAppender, List<GSJobObject> managePollTerms) {
		if(tmProfileTermsList != null && tmProfileTermsList.size() > 0 && queryAppender != null) {
			managePollTerms =	new ArrayList<GSJobObject>();
			for (TMProfileTerms tmProfileTerms : tmProfileTermsList) {
				GSJobObject gsJobObject = new GSJobObject();
				gsJobObject.setTmProfileId(tmProfileTerms.getTmProfileInfoId());
				gsJobObject.setTermsBeingPolled(tmProfileTerms.getSourceTerm());
				gsJobObject.setSuggestedterms(tmProfileTerms.getTargetTerm());
				gsJobObject.setTargetTermLanguage(tmProfileTerms.getTargetLang());
				gsJobObject.setDomain(tmProfileTerms.getDomain());
				gsJobObject.setContentType(tmProfileTerms.getContentType());
				gsJobObject.setProductLine(tmProfileTerms.getProductLine());
				gsJobObject.setCompany(tmProfileTerms.getCompany());
				managePollTerms.add(gsJobObject);
			}
		}

		return managePollTerms;

	}

	/**
	 * To get Search manage term base poll terms for a language Id
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to  filter the  terms
	 * @param user          to filter the terms
	 * @return terms
	 */
	@Override
	@Transactional
	public TMProfileTermsResult getSearchManagePollTermsTermBase(QueryAppender queryAppender, Integer companyId, User user) {
		List<PollTerms> managePollTerms = new ArrayList<PollTerms>();
		List<PollTerms> poleTermsList = new ArrayList<PollTerms>();
		List<PollTerms> finalPollTerms = new ArrayList<PollTerms>();
		TMProfileTermsResult tmProfileTermsResult = new TMProfileTermsResult();
		List<CompanyTransMgmt> companyTransManageMentSet = null;
		if (companyId != null) {
			if (user != null) {
				if (user.getCompany() != null) {
					companyId = user.getCompany().getCompanyId();
					if (user.getCompany().getCompanyName() != null && user.getCompany().getCompanyName().equalsIgnoreCase(TeaminologyProperty.INTERNAL_COMPANY.getValue())) {
						companyTransManageMentSet = iUserDAO.getCompanyTransMgmtUsers(user.getUserId());
					}
				}
			}
		}
		Integer totalResults = 0;

		List<Company> companyList = utilDAO.getLookup(Company.class);
		if (companyId != null) {
			if (companyTransManageMentSet == null) {
				poleTermsList = getTermBaseSearchUsingSearchIndex(queryAppender, companyId);
				totalResults = queryAppender.getTotalRecords();
			} else {
				for (CompanyTransMgmt companyTrans : companyTransManageMentSet) {
					List<PollTerms> companyPolltermsList = getTermBaseSearchUsingSearchIndex(queryAppender, companyTrans.getCompanyId());
					poleTermsList.addAll(companyPolltermsList);
					totalResults = totalResults + queryAppender.getTotalRecords();
					
				}
			}

		} else {
			for (Company company : companyList) {
				List<PollTerms> companyPolltermsList = getTermBaseSearchUsingSearchIndex(queryAppender, company.getCompanyId());
				poleTermsList.addAll(companyPolltermsList);
				totalResults = totalResults + queryAppender.getTotalRecords();
			}
		}


		try {

			String termIds = "";
			if (poleTermsList != null && poleTermsList.size() > 0) {
				for (PollTerms pollTerms : poleTermsList) {
					termIds = termIds + pollTerms.getTermId() + ",";
				}
				termIds = termIds.substring(0, termIds.lastIndexOf(","));


				List<PollTerms> managePollTems = termDetailsDAO.getVotingDetailsByTermIds(termIds);
				for (PollTerms pollTerms : poleTermsList) {
					for (PollTerms poll : managePollTems) {
						if (pollTerms.getTermId().intValue() == poll.getTermId().intValue()) {
							PollTerms finalPollTerm = new PollTerms();
							finalPollTerm.setTermId(pollTerms.getTermId());
							finalPollTerm.setSourceTerm(pollTerms.getSourceTerm());
							finalPollTerm.setSuggestedTerm(pollTerms.getSuggestedTerm());
							finalPollTerm.setLanguage(pollTerms.getLanguage());
							finalPollTerm.setPartOfSpeech(pollTerms.getPartOfSpeech());
							finalPollTerm.setCategory(pollTerms.getCategory());
							finalPollTerm.setDomain(pollTerms.getDomain());
							finalPollTerm.setCompany(pollTerms.getCompany());
							finalPollTerm.setStatus(pollTerms.getStatus());
							finalPollTerm.setPollExpirationDt(pollTerms.getPollExpirationDt());
							finalPollTerm.setVotesPerTerm(poll.getVotesPerTerm());
							finalPollTerm.setInvites(poll.getInvites());
							finalPollTerm.setDeprecatedCount(poll.getDeprecatedCount());
							finalPollTerms.add(finalPollTerm);
						}
					}
				}

			}
			tmProfileTermsResult.setPollTermsList(finalPollTerms);
			tmProfileTermsResult.setTotalResults(totalResults);

			poleTermsList.clear();
			poleTermsList = null;
			if ((managePollTerms != null) && (managePollTerms.size() > 0)) {
				logger
				.debug("Total user Poll Terms :"
						+ managePollTerms.size());
				managePollTerms.clear();
				managePollTerms = null;
			} else {
				logger.debug("Got empty manage Poll Terms ");
			}

		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
		}
		return tmProfileTermsResult;
	}

	private List<PollTerms> pollTermBaseList(List<PollTerms> poleTermsList, QueryAppender queryAppender, List<PollTerms> finalPollTerms) {

		if(poleTermsList != null && poleTermsList.size() > 0) {
			finalPollTerms = new ArrayList<PollTerms>();
			String termIds = "";
			for (PollTerms pollTerms : poleTermsList) {
				termIds = termIds + pollTerms.getTermId() + ",";
			}
			termIds = termIds.substring(0, termIds.lastIndexOf(","));


			List<PollTerms> managePollTems = termDetailsDAO.getVotingDetailsByTermIds(termIds);
			for (PollTerms pollTerms : poleTermsList) {
				for (PollTerms poll : managePollTems) {
					if (pollTerms.getTermId().intValue() == poll.getTermId().intValue()) {
						PollTerms finalPollTerm = new PollTerms();
						finalPollTerm.setTermId(pollTerms.getTermId());
						finalPollTerm.setSourceTerm(pollTerms.getSourceTerm());
						finalPollTerm.setSuggestedTerm(pollTerms.getSuggestedTerm());
						finalPollTerm.setLanguage(pollTerms.getLanguage());
						finalPollTerm.setPartOfSpeech(pollTerms.getPartOfSpeech());
						finalPollTerm.setCategory(pollTerms.getCategory());
						finalPollTerm.setDomain(pollTerms.getDomain());
						finalPollTerm.setCompany(pollTerms.getCompany());
						finalPollTerm.setStatus(pollTerms.getStatus());
						finalPollTerm.setPollExpirationDt(pollTerms.getPollExpirationDt());
						finalPollTerm.setVotesPerTerm(poll.getVotesPerTerm());
						finalPollTerm.setInvites(poll.getInvites());
						finalPollTerm.setDeprecatedCount(poll.getDeprecatedCount());
						finalPollTerms.add(finalPollTerm);
					}
				}
			}

		}
		return finalPollTerms;
	}

	/**
	 *
	 * Add new term.
	 *
	 * @param termInformation TermInformation that needs to be added
	 * @return If term is voted it returns success else failed
	 */

	/*	public Integer addNewGlobalSightTerm(final TermInformation termInformation,GlobalsightTermInfo globalSightTermInfo) {
        Integer termId=0;
		if (termInformation == null) {

		}

		try {

			termId=termDetailsDAO.addNewGlobalSightTerm(termInformation,globalSightTermInfo);
			logger.debug("new term is added");

		} catch (Exception e) {
			logger.error("Error in  adding  new term " + e);
		}
    return termId;
	}*/

	/**
	 * To get Total terms in Glossary
	 *
	 * @param isTm An String  to  filter the  terms
	 * @return A int value holding the total no of terms in Glossary
	 */
	@Override
	@Transactional
	public Integer getTotalTermsInTermBaseTM(String isTM) {
		 int totalTermsInTermBaseTM = 0;
	        try {

	            totalTermsInTermBaseTM = termDetailsDAO.getTotalTermsInTermBaseTM(isTM);
	            if (totalTermsInTermBaseTM > 0) {
	                logger
	                        .debug("totalTermsInTermBaseTM :"
	                                + totalTermsInTermBaseTM);
	            } else {
	                logger.info(" ++++  ++++ Got empty Total Terms In Glossary");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  Total Terms In Glossary");
	        }

	        return totalTermsInTermBaseTM;
	}


	/**
	 * To get TM profile terms
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to  filter the  terms
	 * @param user          to
	 * @return List of terms
	 */
	@Override
	@Transactional
	public TMProfileTermsResult getTMProfileTerms(QueryAppender queryAppender, Integer companyId, User user) {

		TMProfileTermsResult tmProfileTermsResult = new TMProfileTermsResult();
		List<TMProfileTerms> tmProfileTermsList = new ArrayList<TMProfileTerms>();
		List<CompanyTransMgmt> companyTransManageMentSet = null;
		Integer totalRecords = 0;
		if (companyId != null) {
			if (user != null) {
				if (user.getCompany() != null) {
					companyId = user.getCompany().getCompanyId();
					if (user.getCompany().getCompanyName() != null && user.getCompany().getCompanyName().equalsIgnoreCase(TeaminologyProperty.INTERNAL_COMPANY.getValue())) {
						companyTransManageMentSet = iUserDAO.getCompanyTransMgmtUsers(user.getUserId());
					}
				}
			}
		}
		Long startTimeInMS = System.currentTimeMillis();
		List<Company> companyList = utilDAO.getLookup(Company.class);
		if (companyId != null) {
			if (companyTransManageMentSet == null) {
				tmProfileTermsList = getAllTMProfileTerms(queryAppender, companyId);
				totalRecords = queryAppender.getTotalRecords();
			} else {
				List<TMProfileTerms> companyPolltermsList = null;
				for (CompanyTransMgmt companyTrans : companyTransManageMentSet) {
					companyPolltermsList = getAllTMProfileTerms(queryAppender, companyTrans.getCompanyId());
					if(companyPolltermsList!=null){
						tmProfileTermsList.addAll(companyPolltermsList);
					}
					totalRecords = totalRecords+queryAppender.getTotalRecords();
				}
			}

		} else {
			List<TMProfileTerms> companyPolltermsList = null;
			for (Company company : companyList) {
				companyPolltermsList = getAllTMProfileTerms(queryAppender, company.getCompanyId());
				if(companyPolltermsList!=null){
					tmProfileTermsList.addAll(companyPolltermsList);
				}
				totalRecords = totalRecords+queryAppender.getTotalRecords();
			}
		}

		try {
			tmProfileTermsResult.setTmProfieTermsList(tmProfileTermsList);
			tmProfileTermsResult.setTotalResults(totalRecords);


		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
			e.printStackTrace();
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}
		return tmProfileTermsResult;
	}

	/**
	 * To get Total terms in TM
	 *
	 * @param companyIds an Integer  set to  filter the  terms
	 * @return A int value holding the total no of terms in TM
	 */
	@Override
	@Transactional
	public Integer getTotalTermsInTM(Set<Integer> companyIds) {
		
		int totalTermsInTermBaseTM = 0;
		
		try {
			
			//Created SolrQuery object to send query to Solr
			SolrQuery query = new SolrQuery();

			String queryObj = "";
			Object[] companyIdArrObj = null;

			if(companyIds!=null){
				
				companyIdArrObj = companyIds.toArray();
				
				//Added Filter to query If company Ids exist
				for(int i=0;i<companyIdArrObj.length;i++){
					
					if(i == 0){
						
						queryObj = queryObj +"companyId:"+companyIdArrObj[0];
						
					}else{
						
						queryObj = queryObj + " OR " + "companyId:"+companyIdArrObj[i];
						
					}
				}
			}

			//Fetch all document available in solr
			query.setQuery( "*:*" );

			if(companyIdArrObj!=null){
				
				query.addFilterQuery(queryObj);
				
			}
			
			query.addFilterQuery("termtype:"+IndexTypeEnum.TM.toString());

			QueryResponse rsp = server.query(query);

			SolrDocumentList solDocList = rsp.getResults();

			//Total TM's in solr
			Long resultFound = solDocList.getNumFound();

			totalTermsInTermBaseTM = resultFound.intValue();
			
			if(totalTermsInTermBaseTM>0){
				logger.info("+++ Total Records Available In solr +++ "+totalTermsInTermBaseTM);
			}else{
				logger.info("+++ Got empty Total Terms In Glossary +++ "+totalTermsInTermBaseTM);
			}
			

			}
		catch (Exception e) {
			
			logger.error("++++Error in getting  Total Terms In Glossary++++");
			logger.error(e,e);
		}

		return totalTermsInTermBaseTM;
	}

	/**
	 * To get total terms in tms
	 *
	 * @param exportBy    String to filter the  tm terms
	 * @param selectedIds Integer Array of filter values
	 * @return List of TmProfileInfo obj's w.r.t selected ids
	 */
	@Override
	@Transactional
	public List<TmProfileInfo> getTotalTermsInTM(String exportBy,
			Integer[] selectedIds) {

		return termDetailsDAO.getTotalTermsInTM(exportBy, selectedIds);
	}

	/**
	 * To get term attributes for a termID
	 *
	 * @param tmProfileInfoId An Integer to get details
	 * @return TermInformation w.r.t the term id
	 */
	@Override
	@Transactional
	public TmProfileInfo getTmAttributes(Integer tmProfileInfoId) {
		if (tmProfileInfoId == null) {
			throw new IllegalArgumentException("Invalid termId");
		}
		TmProfileInfo tmInfo = new TmProfileInfo();

		try {

			tmInfo = (TmProfileInfo) termDetailsDAO.getTmAttributes(tmProfileInfoId);


		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in getting  term attributes for term ID");
		}
		return tmInfo;
	}

	/**
	 * To update Term Details
	 *
	 * @param termInformation TermInformation that has to be updated
	 * @param companyId       An Integer to be filtered
	 */
	@Override
	@Transactional
	public void updateTmDetails(TmProfileInfo tmProfileInfo, Integer companyId) {
		SolrInputDocument doc = null;
		if (tmProfileInfo == null) {
			throw new IllegalArgumentException("Invalid termInformation");
		}
		try {

			Collection<SolrInputDocument> collDocObj = new ArrayList<SolrInputDocument>();

			termDetailsDAO.updateTmDetails(tmProfileInfo);
			String ltmProfileId = tmProfileInfo.getTmProfileInfoId().toString()+SuffixEnum._TM.name();
			String source = tmProfileInfo.getSource();
			String target = tmProfileInfo.getTarget();
			String domain = (tmProfileInfo.getDomain() == null) ? null : tmProfileInfo.getDomain().getDomain();
			String productLine = (tmProfileInfo.getProductGroup() == null) ? null : tmProfileInfo.getProductGroup().getProduct();
			String contentType = (tmProfileInfo.getContentType()) == null ? null : tmProfileInfo.getContentType().getContentType();
			String company = (tmProfileInfo.getCompany() == null) ? null : tmProfileInfo.getCompany().getCompanyName();
			String sourceLang = (tmProfileInfo.getSourceLang() == null) ? null : tmProfileInfo.getSourceLang().getLanguageLabel();
			String targetLang = (tmProfileInfo.getTargetLang() == null) ? null : tmProfileInfo.getTargetLang().getLanguageLabel();


			server.deleteById(ltmProfileId);
			server.commit();
			
			doc  = new SolrInputDocument();
			if (ltmProfileId != null)
				doc.addField("id", ltmProfileId);
			if (source != null)
				doc.addField("source", source);
			if (target != null)
				doc.addField("target", target);
			if (domain != null) {
				doc.addField("domain", domain);
			}

			if (productLine != null) {
				doc.addField("product", productLine);
			}

			if (contentType != null) {
				doc.addField("contenttype", contentType);
			}

			if (company != null) {
				doc.addField("company", company);
			}

			if(tmProfileInfo.getCompany().getCompanyId()!=null){
				doc.addField("companyId", tmProfileInfo.getCompany().getCompanyId());
			}

			if (sourceLang != null) {
				doc.addField("sourcelang", sourceLang);
			}

			if (targetLang != null) {
				doc.addField("targetlang", targetLang);
			}
			doc.addField("termtype", IndexTypeEnum.TM.name());
			doc.addField("primarykey", tmProfileInfo.getTmProfileInfoId());
			collDocObj.add(doc);

			if(collDocObj!=null && !collDocObj.isEmpty()){
				server.add(collDocObj);
				server.commit();
				logger.debug("updated Term Details");
			}
			collDocObj.clear();
			collDocObj = null;
			
		}
		catch (Exception e) {
			logger.error("Error in updating  term details");
			e.printStackTrace();
		}

	}

	/**
	 * To delete tms terms
	 *
	 * @param termIds   An Integer array that needs to be deleted
	 * @param companyId An Integer to  filter the  terms
	 */
	@Override
	@Transactional
	public void deleteTms(final Integer[] termIds, User user) {

		if (termIds == null) {
			throw new IllegalArgumentException("Invalid termIds");
		}
		try {
			List<Integer> tmIdsList = null;

			if (termIds != null && termIds.length > 0) {
				tmIdsList = new ArrayList<Integer>();
				for (int i = 0; i < termIds.length; i++) {
					tmIdsList.add(termIds[i]);
				}
				if (tmIdsList != null && tmIdsList.size() > 0) {
					List<List<Integer>> batchList = Utils.createBatches(tmIdsList, 500);
					HttpSolrServer httpSolrServerObj = HttpSolrServerInstance.INSTANCE.getServer();
					List<String> tmProfileIdList = new ArrayList<String>();;
					for (List<Integer> batch : batchList) {
						for (Integer tmId : batch) {
							String tmSuffix = SuffixEnum._TM.toString();
							String tmProfileId = tmId.toString()+tmSuffix;
							tmProfileIdList.add(tmProfileId);
						}
						httpSolrServerObj.deleteById(tmProfileIdList);
						httpSolrServerObj.commit();
						termDetailsDAO.deleteTms(batch);
						termDetailsDAO.deleteTmProperties(batch);
					}
				}


			}

			logger.error("delete Term Details");
		}
		catch (Exception e) {
			logger.error("Error in deleting term");
			logger.error(e,e);
		}

	}

	/**
	 * To update gs term details
	 *
	 * @param gsTermInformation GlobalsightTermInfo that has to be updated
	 */
	@Override
	@Transactional
	public void updateGSTermDetails(GlobalsightTermInfo gsTermInformation) { if (gsTermInformation == null) {
        throw new IllegalArgumentException("Invalid termInformation");
    }
    try {

        termDetailsDAO.updateGSTermDetails(gsTermInformation);
        if (gsTermInformation != null) {
            List<GlobalsightTermInfo> gsTermInfoList = new ArrayList<GlobalsightTermInfo>();
            gsTermInfoList.add(gsTermInformation);
            updateGsIndex(gsTermInfoList);
        }

        logger.info(" ++++  ++++ updated Term Details");
    }
    catch (Exception e) {
        logger.error("Error in updating  term details");
        e.printStackTrace();
    }}

	/**
	 * To get gs term information using term id
	 *
	 * @param termId An Integer to get details
	 * @return GlobalsightTermInfo w.r.t the term id
	 */
	@Override
	@Transactional
	public GlobalsightTermInfo getGSTermInfoUsingTermId(Integer termId) {
		if (termId == null) {
			throw new IllegalArgumentException("Invalid termInformation");
		}
		GlobalsightTermInfo globalsightTermInfo = null;
		try {

			globalsightTermInfo = termDetailsDAO.getGSTermInfoUsingTermId(termId);
			logger.debug("updated Term Details");
		}
		catch (Exception e) {
			logger.error("Error in updating  term details");
			e.printStackTrace();
		}

		return globalsightTermInfo;
	}

	/**
	 * To get total gs terms
	 *
	 * @return A integer  value holding the total no of gs terms in Glossary
	 */

	@Override
	@Transactional
	public Integer getTotalGSTerms() {
		int totalTermsInGs = 0;
		try {

			totalTermsInGs = termDetailsDAO.getTotalGSTerms();
			if (totalTermsInGs > 0) {
				logger
				.debug("totalTermsInTermBaseTM :"
						+ totalTermsInGs);
			} else {
				logger.debug("Got empty Total Terms In Glossary");
			}

		}
		catch (Exception e) {
			logger.error("Error in getting  Total Terms In Glossary");
		}

		return totalTermsInGs;
	}

	/**
	 * To delete gs terms
	 *
	 * @param termIds   An Integer array that needs to be deleted
	 * @param companyId An Integer to filter the  terms
	 */
	@Override
	@Transactional
	public void deleteGSTerms(Integer[] termIds, Integer companyId) {
		if (termIds == null) {
			throw new IllegalArgumentException("Invalid termIds");
		}
		try {
			for (int i = 0; i < termIds.length; i++) {
				Integer termId = termIds[i];
				GlobalsightTermInfo gsTermInfo = termDetailsDAO.getGSTermInfoUsingTermId(termId);
				gsTermInfo.setIsActive("N");
				gsTermInfo.setUpdateDate(new Date());
				termDetailsDAO.updateGSTermDetails(gsTermInfo);
			}
			deleteGSTermsInIndex(termIds, companyId);
			logger.debug("delete Term Details");
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in deleting term");
		}

	}


	/**
	 * To delete gs file data
	 *
	 * @param fileIds An Integer array that needs to be deleted
	 */
	@Override
	@Transactional
	public void deleteGSFileData(Integer[] fileIds , User user) {
		List<GlobalsightTermInfo> gsTermsInfoList = new ArrayList<GlobalsightTermInfo>();
		List<String> rejectedTermList = new ArrayList<String>();
		Set<Integer> termIds = new HashSet<Integer>();
		Set<Integer> gsIds = new HashSet<Integer>();

		if (fileIds == null) {
			throw new IllegalArgumentException("Invalid fileIds");
		}
		try {
			for (int i = 0; i < fileIds.length; i++) {
				Integer fileId = fileIds[i];
				FileInfo fileInfo = termDetailsDAO.getFileInfo(fileId);
				if (fileInfo != null && fileInfo.getStatus().equalsIgnoreCase("exported")) {
					fileInfo.setIsActive("N");
					fileInfo.setUpdateDate(new Date());
					gsTermsInfoList = termDetailsDAO.getGSTermInfoUsingFileInfoId(fileInfo.getFileInfoId());
					if (gsTermsInfoList != null && gsTermsInfoList.size() > 0) {
						for (GlobalsightTermInfo gsTermInfo : gsTermsInfoList) {
							gsTermInfo.setIsActive("N");
							gsTermInfo.setUpdateDate(new Date());
							gsTermInfo.setTag(null);
							termDetailsDAO.updateGSTermDetails(gsTermInfo);
							if (gsTermInfo.getTermInformationId() != null)
								termIds.add(gsTermInfo.getTermInformationId().getTermId());
							gsIds.add(gsTermInfo.getGlobalsightTermInfoId());
						}
					}
					termDetailsDAO.updateGSFileData(fileInfo);
				}
			}
			Integer termIdsArray[] = null;
			if (termIds != null && termIds.size() > 0) {
				termIdsArray = new Integer[termIds.size()];
				termIdsArray = termIds.toArray(termIdsArray);
				termDetailsDAO.deleteTerms(termIdsArray , user);
			}
			List<Integer> tagIds = termDetailsDAO.getTagsUsinggsIds(gsIds);
			termDetailsDAO.deleteTags(gsIds);
			termDetailsDAO.deleteAttributes(tagIds);
			deleteGSTermsInIndex(termIdsArray, null);
			logger.debug("delete Term Details");
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in deleting term");
		}

	}

	/**
	 * To get total tm terms by search
	 *
	 * @param queryAppender Which is used to build a query
	 * @return A integer  value holding the total no of  tm terms in Glossary
	 */
	@Override
	@Transactional
	public Integer getTotalTMTermsBySearch(QueryAppender queryAppender) {
		 int totalTermsInTM = 0;
	        try {

	            totalTermsInTM = termDetailsDAO.getTotalTMTermsBySearch(queryAppender);
	            if (totalTermsInTM > 0) {
	                logger
	                        .debug("totalTermsInTM :"
	                                + totalTermsInTM);
	            } else {
	                logger.info(" ++++  ++++ Got empty Total Terms In Glossary");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  Total Terms In Glossary");
	        }

	        return totalTermsInTM;
	}

	/**
	 * To get Tms in Glossary per year
	 *
	 * @param companyId An Integer set  to  filter the  terms
	 * @return List of data holding year and no of terms per year
	 */
	@Transactional
	public List<Terms> getTmsInGlossary(Set<Integer> companyIds) {
		List<Terms> tmsInGlossary = new ArrayList<Terms>();
		try {
			String idsList = null;

			tmsInGlossary = termDetailsDAO.getTmsInGlossary(idsList);
			if ((tmsInGlossary != null) && (tmsInGlossary.size() > 0)) {
				logger
				.debug("Total termsInGlossary :"
						+ tmsInGlossary.size());
			} else {
				logger.debug("Got empty termsInGlossary ");
			}

		}
		catch (Exception e) {
			logger.error("Error in getting  terms in Glossary ");
		}

		return tmsInGlossary;
	}

	/**
	 * To get final terms by sorting the tms
	 *
	 * @param tms  holds the termsList
	 * @param type holds type possible values are "Yearly","Quarterly","Monthly"
	 * @return List of terms sorting accordingly
	 */
	@Transactional
	public List<Terms> getFinalisedTms(List<Terms> terms, String type) {
        if (terms == null || terms.isEmpty()) {
            return terms;
        }
        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH);
        Integer quarter = (cal.get(Calendar.MONTH) / 3) + 1;
        List<Terms> finalTmsList = new ArrayList<Terms>();
        Map<String, Terms> termMap = new HashMap<String, Terms>();


        for (Terms termObj : terms) {
            termMap.put(termObj.getInterval(), termObj);
        }
        if (type.equalsIgnoreCase("Yearly")) {
            for (int i = 0; i <= 4; i++) {
                Terms term = termMap.get(year.toString());
                if (term == null) {
                    term = new Terms();
                    term.setInterval(year.toString());
                    term.setTermsPerInterval(0);
                }
                year--;
                finalTmsList.add(term);
            }
        }
        if (type.equalsIgnoreCase("Quarterly")) {
            for (int i = 0; i <= 4; i++) {
                Terms term = termMap.get("Q" + quarter + "-" + year);

                if (term == null) {
                    term = new Terms();
                    term.setInterval("Q" + quarter + "-" + year);
                    term.setTermsPerInterval(0);
                }

                if (quarter == 1) {
                    quarter = 5;
                    year--;
                }
                quarter--;

                finalTmsList.add(term);
            }
        }
        if (type.equalsIgnoreCase("Monthly")) {
            for (int i = 0; i <= 4; i++) {
                Terms term = termMap.get(getMonthForInt(month));
                if (term == null) {
                    term = new Terms();
                    term.setInterval(getMonthForInt(month));
                    term.setTermsPerInterval(0);
                }
                if (month == 0) {
                    month = 12;
                }

                month--;

                finalTmsList.add(term);
            }
        }

        Collections.reverse(finalTmsList);
        return finalTmsList;
    }

	/**
	 * To get quarterly tm details
	 *
	 * @param companyId An Integer to  filter the  terms
	 * @return List of data holding quarter-year and no of terms per quarter
	 */
	@Override
	@Transactional
	public List<Terms> getQuarterlyTmDetails(Integer companyId) {
		  List<Terms> quarterlyTms = new ArrayList<Terms>();
	        try {
	            quarterlyTms = termDetailsDAO.getQuarterlyTmDetails(companyId);
	            if ((quarterlyTms != null) && (quarterlyTms.size() > 0)) {
	                logger.info(" ++++  ++++ Total Quarterly Tms :" + quarterlyTms.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty Quarterly Tms ");
	            }
	        }
	        catch (Exception e) {
	            return null;
	        }
	        return quarterlyTms;

	}

	/**
	 * To get monthly glossary tms
	 *
	 * @param companyId An Integer to  filter the  terms
	 * @return List of data holding month and no of terms per month
	 */
	@Override
	@Transactional
	public List<Terms> getMonthlyTmDetails(Integer companyId) {
		 List<Terms> monthlyTerms = new ArrayList<Terms>();
	        try {
	            monthlyTerms = termDetailsDAO.getMonthlyTmDetails(companyId);
	            if ((monthlyTerms != null) && (monthlyTerms.size() > 0)) {
	                logger.info(" ++++  ++++ Total Monthly Tms :" + monthlyTerms.size());
	            } else {
	                logger.info(" ++++  ++++ Got empty Monthly Tms ");
	            }
	        }
	        catch (Exception e) {
	            logger.error("Error in getting  monthly tm details");
	        }
	        return monthlyTerms;
	}

	/**
	 * To save import tmx file
	 *
	 * @param fileUpload FileUploadStatus  to be saved
	 */
	@Transactional
	public void saveImportTMXFile(FileUploadStatus fileUpload) {

		try {
			termDetailsDAO.saveImportTMXFile(fileUpload);
			if ((fileUpload != null)) {
				logger.debug("Got File Name :" + fileUpload.getFileName());
			} else {
				logger.debug("Got empty  File Name  ");
			}
		}
		catch (Exception e) {
			logger.error("Error in getting   File Name ");
		}
	}

	/**
	 * To save import tmx file url
	 *
	 * @param fileId String to filter FileUploadStatus  details
	 */
	@Transactional
	public void saveImportTMXFileUrl(String filePath, String fileId) {
		try {

			termDetailsDAO.saveImportTMXFileUrl(filePath, fileId);
		}
		catch (Exception e) {
			logger.error("Error in uploading file url ");
		}
	}

	/**
	 * To get file upload status
	 *
	 * @param fileId Integer to filter FileUploadStatus  details
	 * @return FileUploadStatus w.r.t fileId
	 */
	@Override
	@Transactional
	public FileUploadStatus getFileUploadStatus(Integer fileId) {
		if (fileId == null) {
			throw new IllegalArgumentException("Invalid FileUpload");
		}
		FileUploadStatus fileUploadStatus = null;
		try {

			fileUploadStatus = termDetailsDAO.getFileUploadStatus(fileId);
			logger.debug("Got File upload details");
		}
		catch (Exception e) {
			logger.error("Error getting file upload status");
			e.printStackTrace();
		}

		return fileUploadStatus;
	}

	/**
	 * To get FileUploadstatus details
	 *
	 * @param request   HttpServletRequest
	 * @param colName   column name that has to be sorted
	 * @param sortOrder order in which it has to be sorted
	 * @param pageNum   Integer to limit the data
	 * @return List of FileUploadstatus obj's
	 */

	@Override
	@Transactional
	public List<FileUploadStatus> getImportExportData(Integer userId, String colName, String sortOrder, Integer pageNum) {
		try {
			List<FileUploadStatus> fileList = termDetailsDAO.getImportExportData(userId, colName, sortOrder, pageNum);
			if ((fileList != null) && (fileList.size() > 0)) {
				logger.debug("Import Export Data :" + fileList.size());
			} else {
				logger.debug("Got empty  Data");
			}
			return fileList;
		}
		catch (Exception e) {

			e.printStackTrace();
			logger.error("Error in getting Import Export  Data");
			return null;
		}
	}

	/**
	 * To update FileUploadStatus
	 *
	 * @param fileUpload FileUploadStatus to be updated
	 */

	@Override
	@Transactional
	public void updateFileUploadStatus(FileUploadStatus fileUpload) {
		FileUploadStatus fileData = new FileUploadStatus();

		try {
			termDetailsDAO.updateFileUploadStatus(fileUpload);
			if ((fileData != null)) {
				logger.debug("Got File Name :" + fileData.getFileName());
			} else {
				logger.debug("Got empty  File Name  ");
			}
		}
		catch (Exception e) {
			logger.error("Error in getting   File Name ");
		}
	}

	/**
	 * To get all jobs
	 *
	 * @param fileUpload FileUploadStatus to be updated
	 * @return List of FileInfo obj's
	 */
	@Override
	@Transactional
	public List<FileInfo> getAllJobs() {
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		try {
			fileInfoList = termDetailsDAO.getAllJobs();
			if ((fileInfoList != null) && (fileInfoList.size() > 0)) {
				logger.debug("Total job Data :" + fileInfoList.size());
			} else {
				logger.debug("Got jobs  Data");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in gettingjobs data");
			return null;
		}
		return fileInfoList;
	}

	/**
	 * To get file information by jobIds
	 *
	 * @param jobIds String  Array of filter jobIds
	 * @return List of FileInfo w.r.t job id's
	 */
	@Transactional
	public List<FileInfo> getFileInfoByJobIds(String[] jobIds) {
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		if (jobIds == null) {
			return null;
		}
		try {
			fileInfoList = termDetailsDAO.getFileInfoByJobIds(jobIds);
			if ((fileInfoList != null) && (fileInfoList.size() > 0)) {
				logger.debug("Got file information");
			} else {
				logger.debug("Got  empty file information");
			}
		}
		catch (Exception e) {
			logger.error("Error getting file upload status");
			e.printStackTrace();
		}

		return fileInfoList;
	}

	/**
	 * To save file information
	 *
	 * @param fileInfo FileInfo to be saved
	 */
	@Override
	@Transactional
	public void saveFileInfo(FileInfo fileInfo) {
		if (fileInfo == null) {
			return;
		}
		try {
			termDetailsDAO.saveFile(fileInfo);
			logger.debug("Got File upload details");
		}
		catch (Exception e) {
			logger.error("Error getting file upload status");
			e.printStackTrace();
		}
	}

	/**
	 * To get all terms
	 *
	 * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
	 * @param companyId     An Integer to  filter the  terms
	 * @return List of term ids that needs to manage
	 */

	@Override
	@Transactional
	public List<Integer> getAllTerms(QueryAppender queryAppender, Integer companyId) {
		List<Integer> totalTerms = new ArrayList<Integer>();
		Long startTimeInMS = null;
		startTimeInMS = System.currentTimeMillis();

		List<Company> companyLookup = utilDAO.getLookup(Company.class);

		try {
			if (companyId != null) {
				totalTerms = getSelectedTermIdsFormSearchIndex(queryAppender, companyId);
			} else {
				for (Company company : companyLookup) {
					List<Integer> companyTotalterm = getSelectedTermIdsFormSearchIndex(queryAppender, company.getCompanyId());
					totalTerms.addAll(companyTotalterm);
				}
			}

		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
			e.printStackTrace();
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}
		return totalTerms;


	}

	/**
	 * To get total tm terms
	 *
	 * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
	 * @param companyId     An Integer to  filter the  terms
	 * @return List of tm profile  ids that needs to manage
	 */
	@Override
	@Transactional
	public List<Integer> getTotalTms(QueryAppender queryAppender, Integer companyId) {
		List<Integer> totalTerms = new ArrayList<Integer>();
		Long startTimeInMS = null;
		startTimeInMS = System.currentTimeMillis();

		List<Company> companyLookup = utilDAO.getLookup(Company.class);

		try {
			if (companyId != null) {
				totalTerms = getSelectedTmIdsFormSearchIndex(queryAppender, companyId);
			} else {
				for (Company company : companyLookup) {
					List<Integer> companyTotalterm = getSelectedTmIdsFormSearchIndex(queryAppender, company.getCompanyId());
					totalTerms.addAll(companyTotalterm);
				}
			}

		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
			e.printStackTrace();
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}
		return totalTerms;


	}

	/**
	 * To get total gs terms
	 *
	 * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
	 * @param companyId     An Integer to  filter the  terms
	 * @return List of gs term ids that needs to manage
	 */

	@Override
	@Transactional
	public List<Integer> getTotalGSTerms(QueryAppender queryAppender, Integer companyId) {
		List<Integer> totalTerms = new ArrayList<Integer>();
		Long startTimeInMS = null;
		startTimeInMS = System.currentTimeMillis();
		List<Company> companyLookup = utilDAO.getLookup(Company.class);

		try {
			if (companyId != null) {
				totalTerms = getSelectedGSTermIdsFormSearchIndex(queryAppender, companyId);
			} else {
				for (Company company : companyLookup) {
					List<Integer> companyTotalterm = getSelectedGSTermIdsFormSearchIndex(queryAppender, company.getCompanyId());
					totalTerms.addAll(companyTotalterm);
				}
			}

		}
		catch (Exception e) {
			logger.error("Error in getting  gs terms");
			e.printStackTrace();
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}
		return totalTerms;


	}

	/**
	 * To get all tm profile terms
	 *
	 * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
	 * @param companyId     An Integer to  filter the  terms
	 * @return List of terms that needs to manage
	 */
	@Transactional
	public List<TMProfileTerms> getAllTMProfileTerms(QueryAppender queryAppender, Integer companyId) {

		List<TMProfileTerms> tmProfileTermsList = new ArrayList<TMProfileTerms>();
		Long startTimeInMS = null;
		startTimeInMS = System.currentTimeMillis();


		if(queryAppender.getColName()!=null){

			if(queryAppender.getColName().equalsIgnoreCase("targetTerm")){
				queryAppender.setColName("suggestedTerm");
			}
		}

		try {

			//Get solr documnent list from Enum map
			EnumMap<IndexTypeEnum,SolrDocumentList> solrDocsEnum = search(queryAppender, companyId);
			SolrDocumentList soldocList1 = solrDocsEnum.get(IndexTypeEnum.TM);
			Long totalRecords = soldocList1.getNumFound();
			queryAppender.setTotalRecords(totalRecords.intValue());

			for (int i = 0; i < soldocList1.size(); ++i) {
				SolrDocument doc = soldocList1.get(i);
				String sourceTerm = (String)doc.getFieldValue("source");
				String targetTerm = (String)doc.getFieldValue("target");

				TMProfileTerms tMProfileTerms = new TMProfileTerms();
				String tmProfileID[]=((String)doc.getFieldValue("id")).split("_");

				Integer termId=Integer.parseInt(tmProfileID[0]);
				tMProfileTerms.setTmProfileInfoId(termId);
				tMProfileTerms.setSourceTerm(sourceTerm);
				tMProfileTerms.setTargetTerm(targetTerm);
				tMProfileTerms.setTargetLang((String)doc.getFieldValue(("targetlang")));
				tMProfileTerms.setSourceLang((String)doc.getFieldValue(("sourcelang")));
				tMProfileTerms.setProductLine((String)doc.getFieldValue(("product")));
				tMProfileTerms.setDomain((String)doc.getFieldValue(("domain")));
				tMProfileTerms.setContentType((String)doc.getFieldValue(("contenttype")));
				tMProfileTerms.setCompany((String)doc.getFieldValue(("company")));
				tmProfileTermsList.add(tMProfileTerms);
			}

		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
			e.printStackTrace();
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}
		return tmProfileTermsList;

	}

	/**
	 * To get all term base profile terms
	 *
	 * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
	 * @param companyId     An Integer to  filter the  terms
	 * @return List of terms that needs to manage
	 */
	@Transactional
	public List<PollTerms> getAllTermBaseProfileTerms(QueryAppender queryAppender, Integer companyId) {

		List<PollTerms> pollTermsList = new ArrayList<PollTerms>();
		Long startTimeInMS = null;
		startTimeInMS = System.currentTimeMillis();
		EnumMap<IndexTypeEnum,SolrDocumentList> solrDocMapEnumObj  = null;
		SolrDocumentList solrResults  = null;
		SolrDocument doc = null;
		
		try {
			String filterBy = queryAppender.getFilterBy();
			Boolean pollExpiration = false;

			if(filterBy!=null){

				if (filterBy.equalsIgnoreCase("Poll Expiration")) {
					pollExpiration = true;
				}
			}

			solrDocMapEnumObj = search(queryAppender,companyId);
			solrResults = solrDocMapEnumObj.get(IndexTypeEnum.TB);
			if(solrResults!=null){
				
				Long totalFound = solrResults.getNumFound();
				queryAppender.setTotalRecords(totalFound.intValue());
				
				for(int i=0; i<solrResults.size();i++){
					if(solrResults != null){ 
						doc=solrResults.get(i);
						String sourceTerm =(String) doc.getFieldValue("source");
						String targetTerm = (String)doc.getFieldValue("target");
						
						PollTerms term = new PollTerms();
						String termTdGet[]=((String)doc.getFieldValue("id")).split("_");
						Integer   termId=Integer.parseInt(termTdGet[0]);
						term.setTermId(termId);
						term.setSourceTerm(sourceTerm);
						term.setSuggestedTerm(targetTerm);
						term.setLanguage((String)doc.getFieldValue("targetlang"));
						term.setPartOfSpeech((String)doc.getFieldValue("partofspeech"));
						term.setCategory((String)doc.getFieldValue("category"));
						term.setDomain((String)doc.getFieldValue("domain"));
						term.setCompany((String)doc.getFieldValue("company"));
						term.setStatus((String)doc.getFieldValue("status"));
						if(doc.getFieldValue("expireDate")!=null){
							term.setPollExpirationDt(new SimpleDateFormat("MM/dd/yyyy").format((Date)doc.getFieldValue("expireDate")));
						}
						if (pollExpiration) {
							if (term.getPollExpirationDt() != null) {
								if (new SimpleDateFormat("MM/dd/yyyy").parse(term.getPollExpirationDt()).getTime() < new Date().getTime()) {
									pollTermsList.add(term);
								}
							}
						} else {
							pollTermsList.add(term);
							logger.info("total polls:::::"+pollTermsList.size());
						}
					}   
				}
			}else{
				logger.info("No data found For companyID::::"+companyId);
			}
		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
			e.printStackTrace();
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}
		return pollTermsList;

	}
private String replaceTermBaseText(QueryAppender queryAppender, List<PollTerms> pollTermsList, String searchStr, User user) {
		String status = null;
		if(queryAppender != null && queryAppender.getReplaceStr() != null 
				&& pollTermsList != null && !pollTermsList.isEmpty() && queryAppender.getSelectedIds() != null) {
			 String isReplace = null;
			TermInformation termInformation = null;
			for (PollTerms pollTerms : pollTermsList) {
				for (Integer selectTermIds : queryAppender.getSelectedIds()) {
					if(pollTerms != null  && ( pollTerms.getSourceTerm().equalsIgnoreCase(searchStr) || searchStr.equalsIgnoreCase(pollTerms.getSuggestedTerm())) && pollTerms.getTermId().equals(selectTermIds)) {
						termInformation = new TermInformation();
						isReplace = "Y";
						termInformation.setTermId(pollTerms.getTermId());
						termInformation.setTermBeingPolled((pollTerms.getSourceTerm().equalsIgnoreCase(searchStr)) ? queryAppender.getReplaceStr() : pollTerms.getSourceTerm());
						termInformation.setUpdateDate(new Date());
						termInformation.setUpdatedBy((user != null && user.getUserId() != null) ? user.getUserId() : null);
						termInformation.setSuggestedTerm((pollTerms.getSuggestedTerm() != null && searchStr.equalsIgnoreCase(pollTerms.getSuggestedTerm()) ? queryAppender.getReplaceStr() : (pollTerms.getSuggestedTerm() != null) ? pollTerms.getSuggestedTerm() : ""));
						updateTermDetails(termInformation, isReplace);
						status = "update";
					}
				}
			}
		}
		return status;

	}
	/**
	 * For pagination of dropdown
	 * @param queryAppender
	 * @param companyId
	 * @return
	 */
	@Transactional
	public List<PollTerms> getAllTermBaseProfileTermsForPagination(QueryAppender queryAppender, Integer companyId) {
		List<PollTerms> pollTermsList = new ArrayList<PollTerms>();
		Long startTimeInMS = null;
		startTimeInMS = System.currentTimeMillis();
		SolrDocument doc = null;
		String filterBy = queryAppender.getFilterBy();
		Boolean pollExpiration = false;
		Integer selectedIds[] = queryAppender.getSelectedIds();
		String querystr = queryAppender.getSearchStr();
		String languageFilter="";
		
		if(filterBy!=null){
			if (filterBy.equalsIgnoreCase("Poll Expiration")) {
				pollExpiration = true;
			}
		}
		try {
			SolrDocumentList solDocList = null;
			logger.info("Forming solr Query ----------");
			String searchStr = "";
			String searchType= IndexTypeEnum.TB.name();
			if (companyId != null && querystr == null) {
				if (searchStr.isEmpty()) {
					 if(companyId == 2) {
						 if (companyId != null) {
								Company company = iLookUpDAO.getCompany(companyId);
								String	companyName = company.getCompanyName();
						        searchStr = "company:\"" + companyName+"\"";
						 }
					 } else {
						 searchStr = "company:*";
					 }
				}
			} else {
				searchStr = "search_data_like:" + querystr;
			}
			 List<String> languageNamesList = null;
				if (selectedIds != null) {
					if (filterBy.equalsIgnoreCase("Locale")) {
						languageNamesList = new ArrayList<String>();
						for (int i = 0; i < selectedIds.length; i++) {
							Language language = iLookUpDAO.getLanguage(selectedIds[i]);
							languageNamesList.add(language.getLanguageLabel());

						}
					}
				}
			 if (languageNamesList != null && languageNamesList.size() > 0) {
					String appender = "";
					languageFilter = "";
					for (String langName : languageNamesList) {
						languageFilter = languageFilter + appender + " targetlang :\"" + langName + "\"";
						appender = " OR ";
					}
			 }
					if (languageFilter != null && !languageFilter.isEmpty()) {
					/*if (searchStr.isEmpty()) {
						searchStr = languageFilter;
					} else {*/
						searchStr = searchStr + " AND ( " + languageFilter + " ) ";
					//}
				}
			 SolrQuery solrQuery=new SolrQuery();
			 solrQuery.setQuery(searchStr);
			//perform select seach based on the specified 
				// parser type(edismax)
				if(queryAppender.getSearchStr()!=null || queryAppender.getSearchTerm()!=null){

					logger.info("Forming edismax query");
					if(queryAppender.getSearchType()==null || queryAppender.getSearchType().equalsIgnoreCase("--Select--")){
						solrQuery.setParam("defType","edismax");
						if(queryAppender.isCaseFlag()){
							solrQuery.setParam("qf","search_data_like_casesensitive");

						}else{
							solrQuery.setParam("qf","search_data_like");
						}
						solrQuery.setParam("mm","100%");
					}else{
						solrQuery.setParam("defType",TeaminologyProperty.SOLR_PARSER_TYPE.getValue());
					}
				}
			 solrQuery.setStart(queryAppender.getScrollLimitFrom());
			 solrQuery.setRows(queryAppender.getScrollLimitTo());
			 solrQuery.addFilterQuery("termtype:"+searchType);
			 solrQuery.addFilterQuery("companyId:"+companyId);
			 solrQuery.setParam("fl",SolrDocumentFieldEnum.getSelectableDocumentField());
			 solrQuery.setSort("primarykey",ORDER.desc);
			 QueryResponse rsp = server.query(solrQuery);
			 solDocList = rsp.getResults();
			 Long totalFound = solDocList.getNumFound();
			 queryAppender.setTotalRecords(totalFound.intValue());
			 for(int i=0; i<solDocList.size();i++){
					if(solDocList != null){ 
						doc=solDocList.get(i);
						String sourceTerm =(String) doc.getFieldValue("source");
						String targetTerm = (String)doc.getFieldValue("target");
						
						PollTerms term = new PollTerms();
						String termTdGet[]=((String)doc.getFieldValue("id")).split("_");
						Integer   termId=Integer.parseInt(termTdGet[0]);
						term.setTermId(termId);
						term.setSourceTerm(sourceTerm);
						term.setSuggestedTerm(targetTerm);
						term.setLanguage((String)doc.getFieldValue("targetlang"));
						term.setPartOfSpeech((String)doc.getFieldValue("partofspeech"));
						term.setCategory((String)doc.getFieldValue("category"));
						term.setDomain((String)doc.getFieldValue("domain"));
						term.setCompany((String)doc.getFieldValue("company"));
						term.setStatus((String)doc.getFieldValue("status"));
						if(doc.getFieldValue("expireDate")!=null){
							term.setPollExpirationDt(new SimpleDateFormat("MM/dd/yyyy").format((Date)doc.getFieldValue("expireDate")));
						}
						if (pollExpiration) {
							if (term.getPollExpirationDt() != null) {
								if (new SimpleDateFormat("MM/dd/yyyy").parse(term.getPollExpirationDt()).getTime() < new Date().getTime()) {
									pollTermsList.add(term);
								}
							}
						} else {
							pollTermsList.add(term);
							logger.info("total polls:::::"+pollTermsList.size());
						}
					}   
				}
			 logger.info(" solr doc List size is  :::"+solDocList.getNumFound());
		}catch(Exception e) {
			logger.error("Error in getting  manage poll terms for pagination");
			e.printStackTrace();
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);
		}
		return pollTermsList;
	}
	
	/**
	 * To update term base index
	 *
	 * @param tmInfo TermInformation that has to be updated
	 */
	@Transactional
	private void updateTermBaseIndex(TermInformation tmInfo) {
		try {
			if (tmInfo.getIsTM() == null) {
				Integer termCompanyId = tmInfo.getTermCompany() != null ? tmInfo.getTermCompany().getCompanyId() : null;
				
				//String termId = tmInfo.getTermId().toString()+SuffixEnum._TB.name();
				String termId = tmInfo.getTermId().toString() == null ? null : tmInfo.getTermId().toString()+SuffixEnum._TB.name();
				String source = tmInfo.getTermBeingPolled() == null ? null : tmInfo.getTermBeingPolled() ;
				String target = tmInfo.getSuggestedTerm() == null ? null : tmInfo.getSuggestedTerm() ;
				String domainLable = (tmInfo.getTermDomain() == null) ? null : tmInfo.getTermDomain().getDomain() == null ? null : tmInfo.getTermDomain().getDomain();

				//to handle null pointer exception
				PartsOfSpeech partOfSpeech = null;
				if(tmInfo.getTermPOS() != null)
					partOfSpeech = iLookUpDAO.getPartsOfSpeech(tmInfo.getTermPOS().getPartsOfSpeechId() == null ? null : tmInfo.getTermPOS().getPartsOfSpeechId());
				String posLable = (partOfSpeech == null) ? null : partOfSpeech.getPartOfSpeech() == null ? null : partOfSpeech.getPartOfSpeech() ;
				PartsOfSpeech  targetPosObj=iLookUpDAO.getPartsOfSpeech(tmInfo.getSuggestedTermPosId() == null ? null : tmInfo.getSuggestedTermPosId() );
				String targetPos=targetPosObj == null ? null : targetPosObj.getPartOfSpeech() == null ? null : targetPosObj.getPartOfSpeech() ;
				String category = (tmInfo.getTermCategory()) == null ? null : tmInfo.getTermCategory().getCategory() == null ? null : tmInfo.getTermCategory().getCategory() ;
				String company = (tmInfo.getTermCompany() == null) ? null : tmInfo.getTermCompany().getCompanyName() == null ? null : tmInfo.getTermCompany().getCompanyName() ;
				Status status = iLookUpDAO.getStatus(tmInfo.getTermStatusId() == null ? null : tmInfo.getTermStatusId() );
				String statusLabel = (status == null) ? null : status.getStatus() == null ? null : status.getStatus() ;
				Language language = iLookUpDAO.getLanguage(tmInfo.getSuggestedTermLangId() == null ? null : tmInfo.getSuggestedTermLangId() );
				String targetLangLable = (language == null) ? null : language.getLanguageLabel() == null ? null : language.getLanguageLabel();
				TermVoteMaster termVoteMaster = termDetailsDAO.getTermVoteMaster(tmInfo.getTermId() == null ? null : tmInfo.getTermId());
				Date expDate = termVoteMaster == null ?  null : termVoteMaster.getVotingExpiredDate() == null ? null :  termVoteMaster.getVotingExpiredDate();
				String expireDate = expDate == null ? null : new SimpleDateFormat("MM/dd/yyyy").format(expDate) == null ? null : new SimpleDateFormat("MM/dd/yyyy").format(expDate);
				List<DeprecatedTermInformation> deprecatedTermInfoList = termDetailsDAO.getDeprecatedTermInfoByTermId(tmInfo.getTermId());
				String deprecatedTerm = "";
				if (deprecatedTermInfoList != null && deprecatedTermInfoList.size() > 0) {
					for (DeprecatedTermInformation deprectedTermInfo : deprecatedTermInfoList) {
						deprecatedTerm = deprecatedTerm + deprectedTermInfo.getDeprecatedSource() + " " + deprectedTermInfo.getDeprecatedTarget() + " ";
					}
				}

				server.deleteById(termId);
				server.commit();
				//updating term details in the solr in the form of documents
				SolrInputDocument doc = new SolrInputDocument();
				Collection<SolrInputDocument> collObj = new ArrayList<SolrInputDocument>();
				if (termId != null)
					doc.addField("id", termId);
				if (termCompanyId != null)
					doc.addField("companyId", termCompanyId);
				if (source != null){
					doc.addField("source", source);
				}
				if (target != null){
					doc.addField("target", target);
				}
				if (domainLable != null)
					doc.addField("domain", domainLable);
				if (posLable != null)
					doc.addField("partofspeech", posLable);
				if (targetPos != null)
                    doc.addField("targetpartofspeech", targetPos);
				if (category != null)
					doc.addField("category", category);
				if (company != null)
					doc.addField("company", company);
				if (statusLabel != null)
					doc.addField("status", statusLabel);
				if (targetLangLable != null)
					doc.addField("targetlang", targetLangLable);
				if (expireDate != null) {
					doc.addField("expireDate", new SimpleDateFormat("MM/dd/yyyy").parse(expireDate));
				}
				if (!deprecatedTerm.isEmpty()) {
					doc.addField("deprecated", deprecatedTerm);
				}

				//Adding all  documents to the Solr
				doc.addField("termtype", IndexTypeEnum.TB.name());
				doc.addField("primarykey", tmInfo.getTermId());
				collObj.add(doc);
				server.add(collObj);
				server.commit();

			}
		}
		catch (Exception e) {
			logger.error("Error in updating the term base index");
			logger.error(e,e);
		}
	}


	/**
	 * To delete terms in index
	 *
	 * @param termIds   An Integer array that needs to be deleted
	 * @param companyId An Integer to  filter the  terms
	 */
	@Transactional
	private void deleteTermsInIndex(Integer[] termIds, Integer companyId) {
		try {
			List<Integer> tmIdsList = null;
			if (companyId != null) {
				if (termIds != null && termIds.length > 0) {
					tmIdsList = new ArrayList<Integer>();
					for (int i = 0; i < termIds.length; i++) {
						tmIdsList.add(termIds[i]);
					}
					if (tmIdsList != null && tmIdsList.size() > 0) {
						List<List<Integer>> batchList = Utils.createBatches(tmIdsList, 500);
						for (List<Integer> batch : batchList) {
							List<String> deleteIdsList = new ArrayList<String>();
							for (Integer getTermId : batch) {

								String termId=String.valueOf(getTermId)+"_TB";
								deleteIdsList.add(termId);
							}
							server.deleteById(deleteIdsList);
							server.commit();
						}			
					}
				}
			} 
		}
		catch (Exception e) {
			logger.error("Error in deleting the terms in index");
			logger.error(e,e);
		}
	}

	/**
	 * To get gs term using search index
	 *
	 * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
	 * @param companyId     An Integer to  filter the  terms
	 * @return List of terms that needs to manage
	 */
	@Transactional
	public List<GlobalsightTerms> getGSTermsUsingSearchIndex(QueryAppender queryAppender, Integer companyId) {

		List<GlobalsightTerms> pollTermsList = new ArrayList<GlobalsightTerms>();
		Long startTimeInMS = null;
		startTimeInMS = System.currentTimeMillis();
		queryAppender.setSearchIndexType(IndexTypeEnum.GS.name());
		try {
		
			logger.info(":::Inside gs term search::::");
			EnumMap<IndexTypeEnum,SolrDocumentList> solrDocEnum = search(queryAppender, companyId);
			SolrDocumentList soldocList1 =solrDocEnum.get(IndexTypeEnum.GS);
			if(soldocList1!=null){
				Long totalDocs = soldocList1.getNumFound();
				queryAppender.setTotalRecords(totalDocs.intValue());
				for (int i = 0; i < soldocList1.size(); ++i) {
					SolrDocument doc = soldocList1.get(i);
					GlobalsightTerms gsTerms = new GlobalsightTerms();
					Long key = (Long)doc.getFieldValue("primarykey");
					gsTerms.setGlobalsightTermInfoId(key.intValue());
					gsTerms.setSourceSegment((String)doc.getFieldValue("source"));
					gsTerms.setTargetSegment((String)doc.getFieldValue("target"));
					gsTerms.setSourceLang((String)doc.getFieldValue("sourcelang"));
					gsTerms.setTargetLang((String)doc.getFieldValue("targetlang"));
					gsTerms.setOrigin((String)doc.getFieldValue("origin"));
					gsTerms.setPageId(new Integer((String)doc.getFieldValue("fileId")));
					gsTerms.setTransUnitId(new Integer((String)doc.getFieldValue("transUnitId")));
					gsTerms.setGsTermId(new Integer((String)doc.getFieldValue("termId")));
					gsTerms.setJobId((String)doc.getFieldValue("jobId"));
					gsTerms.setJobName((String)doc.getFieldValue("jobName"));
					gsTerms.setTaskId((String)doc.getFieldValue("taskId"));
					gsTerms.setTaskName((String)doc.getFieldValue("taskName"));
					pollTermsList.add(gsTerms);
					
				}
			}

		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
			e.printStackTrace();
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}
		return pollTermsList;

	}

	/**
	 * To update term base index
	 *
	 * @param tmInfoList TermInformation that has to be updated
	 */
	@Transactional
	public void updateTermBaseIndex(List<TermInformation> tmInfoList) {

		try {
			if (tmInfoList != null && tmInfoList.size() > 0) {

				logger.info("TermBase updating in solr:::");
				for (TermInformation tmInfo : tmInfoList) {
					
					try{
						
					if (tmInfo.getIsTM() == null) {
						tmInfo = termDetailsDAO.isTermBaseAttrbutesExists(tmInfo);
						if(tmInfo!=null){
							Integer termCompanyId = tmInfo.getTermCompany() != null ? tmInfo.getTermCompany().getCompanyId() : null;
							
//							String termId = tmInfo.getTermId().toString()+SuffixEnum._TB.name();
							String termId = tmInfo.getTermId().toString() == null ? null : tmInfo.getTermId().toString()+SuffixEnum._TB.name();
							 String source = tmInfo.getTermBeingPolled() == null ? null : tmInfo.getTermBeingPolled() ;
	                            String target = tmInfo.getSuggestedTerm() == null ? null : tmInfo.getSuggestedTerm() ;
	                            String domainLable = (tmInfo.getTermDomain() == null) ? null : tmInfo.getTermDomain().getDomain() == null ? null : tmInfo.getTermDomain().getDomain();
	                            PartsOfSpeech partOfSpeech = tmInfo.getTermPOS() == null ? null : iLookUpDAO.getPartsOfSpeech(tmInfo.getTermPOS().getPartsOfSpeechId() == null ? null : tmInfo.getTermPOS().getPartsOfSpeechId());
	                            String posLable = (partOfSpeech == null) ? null : partOfSpeech.getPartOfSpeech() == null ? null : partOfSpeech.getPartOfSpeech();
	                            PartsOfSpeech  targetPosObj=iLookUpDAO.getPartsOfSpeech(tmInfo.getSuggestedTermPosId() == null ? null : tmInfo.getSuggestedTermPosId());
	                            String targetPos = targetPosObj == null ? null : targetPosObj.getPartOfSpeech() == null ? null : targetPosObj.getPartOfSpeech() ;
	                            String category = (tmInfo.getTermCategory()) == null ? null : tmInfo.getTermCategory().getCategory() == null ? null : tmInfo.getTermCategory().getCategory();
	                            String company = (tmInfo.getTermCompany() == null) ? null : tmInfo.getTermCompany().getCompanyName() == null ? null : tmInfo.getTermCompany().getCompanyName();
	                            Status status = iLookUpDAO.getStatus(tmInfo.getTermStatusId() == null ? null : tmInfo.getTermStatusId());
	                            String statusLabel = (status == null) ? null : status.getStatus() == null ? null : status.getStatus();
	                            Language language = iLookUpDAO.getLanguage(tmInfo.getSuggestedTermLangId() == null ? null : tmInfo.getSuggestedTermLangId());
	                            String targetLangLable = (language == null) ? null : language.getLanguageLabel() == null ? null : language.getLanguageLabel();
	                            TermVoteMaster termVoteMaster = getTermVoteMaster(tmInfo.getTermId() == null ? null : tmInfo.getTermId());
	                            Date expDate = termVoteMaster == null ?  null : termVoteMaster.getVotingExpiredDate() == null ? null  : termVoteMaster.getVotingExpiredDate();
	                            String expireDate = expDate == null ? null : new SimpleDateFormat("MM/dd/yyyy").format(expDate) == null ? null : new SimpleDateFormat("MM/dd/yyyy").format(expDate);

							List<DeprecatedTermInformation> deprecatedTermInfoList = termDetailsDAO.getDeprecatedTermInfoByTermId(tmInfo.getTermId());
							String deprecatedTerm = "";
							if (deprecatedTermInfoList != null && deprecatedTermInfoList.size() > 0) {
								for (DeprecatedTermInformation deprectedTermInfo : deprecatedTermInfoList) {
									deprecatedTerm = deprecatedTerm + deprectedTermInfo.getDeprecatedSource() + " " + deprectedTermInfo.getDeprecatedTarget() + " ";
								}

							}
							
							server.deleteById(termId);
							server.commit();
							
							//Updating term details in solr in the form of documents 
							SolrInputDocument doc = new SolrInputDocument();
							Collection<SolrInputDocument> collObj = new ArrayList<SolrInputDocument>();
							if (termId != null)
								doc.addField("id", termId);
							if (termCompanyId != null)
								doc.addField("companyId", termCompanyId);
							if (source != null){
								doc.addField("source", source);
							}
							if (target != null){
								doc.addField("target", target);
							}
							if (domainLable != null)
								doc.addField("domain", domainLable);
							if (posLable != null)
								doc.addField("partofspeech", posLable);
							if (targetPos != null)
                                doc.addField("targetpartofspeech", targetPos);
							if (category != null)
								doc.addField("category", category);
							if (company != null)
								doc.addField("company", company);
							if (statusLabel != null)
								doc.addField("status", statusLabel);
							if (targetLangLable != null)
								doc.addField("targetlang", targetLangLable);
							if (expireDate != null) {
								doc.addField("expireDate", new SimpleDateFormat("MM/dd/yyyy").parse(expireDate));
							}
							if (!deprecatedTerm.isEmpty()) {
								doc.addField("deprecated", deprecatedTerm);
							}

							doc.addField("termtype",IndexTypeEnum.TB.name());
							doc.addField("primarykey",tmInfo.getTermId());
							//Adding all documents to solr
							collObj.add(doc);
							server.add(collObj);
							server.commit();
							logger.info(":::TermBase updated In Solr:::");
						}
					}
				}catch(Exception e){
					
					logger.error("++++++++++Unable to Update data In solr+++++++++++");
					logger.error(e,e);
					
				}
				}
			}
		}
		catch (Exception e) {
			logger.error("++++++++++ Error in updating the term base index +++++++++++");
		}

	}

	/**
	 * Add new term base index.
	 *
	 * @param tmInfoList list of TermInformation that needs to be added
	 */
	@Transactional
	public void addNewTermBaseIndex(List<TermInformation> tmInfoList) {
		try {
			logger.info("Adding new term In solr:::");
			if (tmInfoList != null && tmInfoList.size() > 0) {
				for (TermInformation tmInfo : tmInfoList) {
					
					try{
					
					if (tmInfo.getIsTM() == null) {
						Integer termCompanyId = tmInfo.getTermCompany() != null ? tmInfo.getTermCompany().getCompanyId() : null;
						if (termCompanyId != null) {
//							String termId = tmInfo.getTermId().toString()+"_TB";
							 	String termId = tmInfo.getTermId().toString() == null ? null : tmInfo.getTermId().toString()+SuffixEnum._TB.name();
	                            String source = tmInfo.getTermBeingPolled() == null ? null : tmInfo.getTermBeingPolled() ;
	                            String target = tmInfo.getSuggestedTerm() == null ? null : tmInfo.getSuggestedTerm();
	                            String domainLable = (tmInfo.getTermDomain() == null) ? null : tmInfo.getTermDomain().getDomain() == null ? null : tmInfo.getTermDomain().getDomain();
	                            PartsOfSpeech partOfSpeech = tmInfo.getTermPOS() == null ? null : tmInfo.getTermPOS() ;
	                            String posLable = (partOfSpeech == null) ? null : partOfSpeech.getPartOfSpeech() == null ? null : partOfSpeech.getPartOfSpeech();
	                            String category = (tmInfo.getTermCategory()) == null ? null : tmInfo.getTermCategory().getCategory() == null ? null : tmInfo.getTermCategory().getCategory();
	                            PartsOfSpeech  targetPosObj=iLookUpDAO.getPartsOfSpeech(tmInfo == null ? null : tmInfo.getTermPOS() == null ? null : tmInfo.getTermPOS().getPartsOfSpeechId() == null ? null : tmInfo.getTermPOS().getPartsOfSpeechId());
	                            String targetPos= targetPosObj ==null ? null : targetPosObj.getPartOfSpeech() == null ? null : targetPosObj.getPartOfSpeech();
	                            String company = (tmInfo.getTermCompany() == null) ? null : tmInfo.getTermCompany().getCompanyName() == null ? null : tmInfo.getTermCompany().getCompanyName();
	                            Status status = iLookUpDAO.getStatus(tmInfo.getTermStatusId() == null ? null : tmInfo.getTermStatusId() );
	                            String statusLabel = (status == null) ? null : status.getStatus() == null ? null : status.getStatus();
	                            Language language = iLookUpDAO.getLanguage(tmInfo.getSuggestedTermLangId() == null ? null : tmInfo.getSuggestedTermLangId() );
	                            String targetLangLable = (language == null) ? null : language.getLanguageLabel() == null ? null : language.getLanguageLabel() ;
	                            TermVoteMaster termVoteMaster = getTermVoteMaster(tmInfo.getTermId() == null ? null : tmInfo.getTermId());
	                            Date expDate = termVoteMaster == null ? null : termVoteMaster.getVotingExpiredDate() == null ? null  : termVoteMaster.getVotingExpiredDate();
	                            String expireDate = expDate == null ? null : new SimpleDateFormat("MM/dd/yyyy").format(expDate) == null ? null : new SimpleDateFormat("MM/dd/yyyy").format(expDate);
							List<DeprecatedTermInformation> deprecatedTermInfoList = termDetailsDAO.getDeprecatedTermInfoByTermId(tmInfo.getTermId());
							String deprecatedTerm = "";
							if (deprecatedTermInfoList != null && deprecatedTermInfoList.size() > 0) {
								for (DeprecatedTermInformation deprectedTermInfo : deprecatedTermInfoList) {
									deprecatedTerm = deprecatedTerm + deprectedTermInfo.getDeprecatedSource() + " " + deprectedTermInfo.getDeprecatedTarget() + " ";

								}

							}
							//Updating term details in solr in the form of documents
							SolrInputDocument doc = new SolrInputDocument();
							Collection<SolrInputDocument> collObj = new ArrayList<SolrInputDocument>();
							if (termId != null)
								doc.addField("id", termId);
							if (termCompanyId != null)
								doc.addField("companyId", termCompanyId);
							if (source != null){
								doc.addField("source", source);
							}
							if (target != null){
								doc.addField("target", target);
							}

							if (domainLable != null)
								doc.addField("domain", domainLable);
							if (posLable != null)
								doc.addField("partofspeech", posLable);
							if (targetPos != null)
                                doc.addField("targetpartofspeech", targetPos);
							if (category != null)
								doc.addField("category", category);
							if (company != null)
								doc.addField("company", company);
							if (statusLabel != null)
								doc.addField("status", statusLabel);
							if (targetLangLable != null)
								doc.addField("targetlang", targetLangLable);
							if (expireDate != null) {
								doc.addField("expireDate", expireDate);
							}
							if (!deprecatedTerm.isEmpty()) {
								doc.addField("deprecated", deprecatedTerm);
							}
							doc.addField("termtype", IndexTypeEnum.TB.name());
							doc.addField("primarykey",tmInfo.getTermId());
							
							//Adding documents to the solr
							collObj.add(doc);
							server.add(collObj);
							server.commit();

						}
					}
				}catch(Exception e){
					logger.error("++++Unable to add new TB in Solr++++");
					logger.error(e, e);
				}
				logger.info("++++++TerrmBase added in solr:::+++++");
				}
			}
		}
		catch (Exception e) {
			logger.error("+++++++++Error in adding the new termBase index++++++++++");
			logger.error(e, e);
		}
	}

	/**
	 * To get tm search using search index
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to filter the terms
	 * @return List of terms
	 */
	@Transactional
	public List<TMProfileTerms> getTmSearchUsingSearchIndex(QueryAppender queryAppender, Integer companyId) {

		List<TMProfileTerms> tmProfileTermsList = new ArrayList<TMProfileTerms>();

		EnumMap<IndexTypeEnum,SolrDocumentList> solrDocEnum = null;
		SolrDocumentList soldocList1 = null;
		Long startTimeInMS = null;
		SolrDocument doc = null;
		startTimeInMS = System.currentTimeMillis();

		try {

			solrDocEnum = search(queryAppender, companyId);
			
			soldocList1 =solrDocEnum.get(IndexTypeEnum.TM);
			if(soldocList1!=null){
				
				Long totalDocs = soldocList1.getNumFound();
				queryAppender.setTotalRecords(totalDocs.intValue());
				
				for (int i = 0; i < soldocList1.size(); ++i) {
					
					doc = soldocList1.get(i);
					String sourceTerm =(String) doc.getFieldValue("source");
					String targetTerm =(String) doc.getFieldValue("target");
					TMProfileTerms tMProfileTerms = new TMProfileTerms();
					String tmTdGet[]=((String)doc.getFieldValue("id")).split("_");
					Integer   tmId=Integer.parseInt(tmTdGet[0]);
					tMProfileTerms.setTmProfileInfoId(tmId);
					tMProfileTerms.setSourceTerm(sourceTerm);
					tMProfileTerms.setTargetTerm(targetTerm);
					tMProfileTerms.setTargetLang((String)doc.getFieldValue("targetlang"));
					tMProfileTerms.setSourceLang((String)doc.getFieldValue("sourcelang"));
					tMProfileTerms.setProductLine((String)doc.getFieldValue("product"));
					tMProfileTerms.setDomain((String)doc.getFieldValue("domain"));
					tMProfileTerms.setContentType((String)doc.getFieldValue("contenttype"));
					tMProfileTerms.setCompany((String)doc.getFieldValue("company"));
					tmProfileTermsList.add(tMProfileTerms);
				}
			}

		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}

		return tmProfileTermsList;

	}

	/**
	 * To get term  base search using search index
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to filter the terms
	 * @return List of terms
	 */
	@Transactional
	public List<PollTerms> getTermBaseSearchUsingSearchIndex(QueryAppender queryAppender, Integer companyId) {


		List<PollTerms> pollTermsList = new ArrayList<PollTerms>();
		Long startTimeInMS = null;
		startTimeInMS = System.currentTimeMillis();


		try {


			EnumMap<IndexTypeEnum,SolrDocumentList> solrDocListEnum = search(queryAppender,companyId);
			SolrDocumentList solrResults = solrDocListEnum.get(IndexTypeEnum.TB);
			
			if(solrResults!=null){
				
				Long totalDocs = solrResults.getNumFound();
				queryAppender.setTotalRecords(totalDocs.intValue());
				
				
				for (int i = 0; i < solrResults.size(); ++i) {
					SolrDocument doc = solrResults.get(i);
					String sourceTerm = (String)doc.getFieldValue("source");
					String targetTerm = (String)doc.getFieldValue("target");
					
					PollTerms term = new PollTerms();
					String termTdGet[]=((String)doc.getFieldValue("id")).split("_");
					Integer   termId=Integer.parseInt(termTdGet[0]);
					term.setTermId(termId);
					term.setSourceTerm(sourceTerm);
					term.setSuggestedTerm(targetTerm);
					term.setLanguage((String)doc.getFieldValue("targetlang"));
					term.setPartOfSpeech((String)doc.getFieldValue("partofspeech"));
					term.setCategory((String)doc.getFieldValue("category"));
					term.setDomain((String)doc.getFieldValue("domain"));
					term.setCompany((String)doc.getFieldValue("company"));
					term.setStatus((String)doc.getFieldValue("status"));
					if(doc.getFieldValue("expireDate")!=null){
						term.setPollExpirationDt(new SimpleDateFormat("MM/dd/yyyy").format((Date)doc.getFieldValue("expireDate")));
					}
					pollTermsList.add(term);
					logger.debug("got  manage poll terms");
				}
			}
		}          
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
			logger.error(e, e);
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}

		return pollTermsList;


	}

	/**
	 * To update gs index
	 *
	 * @param gsTermInfoList TermInformation list that has to be updated
	 */
	@Transactional
	public void updateGsIndex(List<GlobalsightTermInfo> gsTermInfoList) {
		try {
			if (gsTermInfoList != null && gsTermInfoList.size() > 0) {

				Collection<SolrInputDocument> solDocList = new ArrayList<SolrInputDocument>();
				for (GlobalsightTermInfo gsTermInfo : gsTermInfoList) {

					Integer termCompanyId = gsTermInfo.getCompany() != null ? gsTermInfo.getCompany().getCompanyId() : null;
					server.deleteById(gsTermInfo.getGlobalsightTermInfoId().toString()+SuffixEnum._GS.name());
					server.commit();
					Integer gsTermInfoId = gsTermInfo.getGlobalsightTermInfoId();
					String source = gsTermInfo.getSourceSegment();
					String target = gsTermInfo.getTargetSegment();
					String sourceLang = gsTermInfo.getFileInfo() == null ? null : (gsTermInfo.getFileInfo().getSourceLang() == null ? null : gsTermInfo.getFileInfo().getSourceLang().getLanguageLabel());
					String targetLang = gsTermInfo.getFileInfo() == null ? null : (gsTermInfo.getFileInfo().getTargetLang() == null ? null : gsTermInfo.getFileInfo().getTargetLang().getLanguageLabel());
					String origin = gsTermInfo.getOrigin();
					Integer fileId = gsTermInfo.getFileInfo() == null ? null : gsTermInfo.getFileInfo().getFileId();
					Integer transUnitId = gsTermInfo.getTransUnitId();
					Integer termId = gsTermInfo.getTermInformationId() == null ? null : gsTermInfo.getTermInformationId().getTermId();
					String jobId = gsTermInfo.getFileInfo() == null ? null : gsTermInfo.getFileInfo().getJobId();
					String jobName = gsTermInfo.getFileInfo() == null ? null : gsTermInfo.getFileInfo().getJobName();
					String taskId = gsTermInfo.getFileInfo() == null ? null : gsTermInfo.getFileInfo().getTaskId();
					String taskName = gsTermInfo.getFileInfo() == null ? null : gsTermInfo.getFileInfo().getTaksName();
					String company = gsTermInfo.getCompany() == null ? null : gsTermInfo.getCompany().getCompanyName();

					SolrInputDocument doc = new SolrInputDocument();
					if (gsTermInfoId != null)
						doc.addField("id",gsTermInfoId.toString()+SuffixEnum._GS.name());
					if (source != null)
						doc.addField("source", source);
					if (target != null)
						doc.addField("target", target);
					if (fileId != null)
						doc.addField("fileId", fileId.toString());
					if (transUnitId != null)
						doc.addField("transUnitId", transUnitId.toString());
					if (termId != null)
						doc.addField("termId", termId.toString());
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
					if (company != null)
						doc.addField("company", company);
					if (sourceLang != null)
						doc.addField("sourcelang", sourceLang);
					if (targetLang != null)
						doc.addField("targetlang", targetLang);
					if (termCompanyId != null)
						doc.addField("companyId", termCompanyId);

					doc.addField("termtype", IndexTypeEnum.GS.name());
					doc.addField("primarykey",gsTermInfo.getGlobalsightTermInfoId());
					solDocList.add(doc);

				}
				server.add(solDocList);
				server.commit();
				logger.debug(" update gs index Details");
				solDocList.clear();
				solDocList = null;
			}
		}
		catch (Exception e) {
			logger.error(" Error in updating gs index Details");
			logger.error(e, e);
		}
	}

	/**
	 * To delete gs terms  in index
	 *
	 * @param termIds   An Integer array that needs to be deleted
	 * @param companyId An Integer to be filtered
	 */
	@Transactional
	private void deleteGSTermsInIndex(Integer[] termIds, Integer companyId) {
		try {

			List<Integer> termIdList = new ArrayList<Integer>(); 
			
			//converting Integer into array list
				for(Integer id:termIds){
					termIdList.add(id);
				}
				
			   List<List<Integer>> idsBatch = Utils.createBatches(termIdList, 10);

				for(List<Integer> batchid:idsBatch){
					for(Integer id:batchid)
					server.deleteByQuery("termId:"+id);
					server.commit();
				}
								
				logger.info("delted ids :::"+termIds.toString());
		}
		catch (Exception e) {
			logger.error("error in deleting  gs Term Details");
			logger.error(e, e);
		}
	}

	/**
	 * To get selected tm ids search using search index
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to filter the terms
	 * @return List of tm id's
	 */
	@Transactional
	public List<Integer> getSelectedTmIdsFormSearchIndex(QueryAppender queryAppender, Integer companyId) {
		List<Integer> totalTerms = new ArrayList<Integer>();
		String companyName = null;
		List<String> companyNamesList = null;
		List<String> languageNamesList = null;
		SolrDocumentList  docListObj = null;
		SolrDocument doc = null;
		String querystr = queryAppender.getSearchStr() == null ? null : queryAppender.getSearchStr().trim();
		//	String filteredCompany=queryAppender.getFilterByCompany();
		String filterBy = queryAppender.getFilterBy();
		Integer selectedIds[] = queryAppender.getSelectedIds();
		Integer companyIds[] = queryAppender.getSelectedCompanyIds();
		if (companyId != null) {
			Company company = iLookUpDAO.getCompany(companyId);
			companyName = company.getCompanyName();
		}
		if (companyIds != null) {
			companyNamesList = new ArrayList<String>();
			for (int i = 0; i < companyIds.length; i++) {
				Company company = iLookUpDAO.getCompany(companyIds[i]);
				companyNamesList.add(company.getCompanyName());

			}
		}
		if (selectedIds != null) {
			if (filterBy.equalsIgnoreCase("Locale")) {
				languageNamesList = new ArrayList<String>();
				for (int i = 0; i < selectedIds.length; i++) {
					Language language = iLookUpDAO.getLanguage(selectedIds[i]);
					languageNamesList.add(language.getLanguageLabel());

				}
			}
			if (filterBy.equalsIgnoreCase("Company")) {
				companyNamesList = new ArrayList<String>();
				for (int i = 0; i < selectedIds.length; i++) {
					Company company = iLookUpDAO.getCompany(selectedIds[i]);
					companyNamesList.add(company.getCompanyName());

				}
			}
		}
		try {
			String searchStr = "";
			if (querystr != null)
				searchStr = "search_data_like:" + querystr;
			if (companyName != null) {
				if (searchStr.isEmpty()) {
					searchStr = " company:\"" + companyName + "\"";
				}
			}
			String companyFilter = null;
			String languageFilter = null;

			if (companyNamesList != null && companyNamesList.size() > 0) {
				String appender = "";
				companyFilter = "";
				for (String companyNameObj : companyNamesList) {
					companyFilter = companyFilter + appender + " company :\"" + companyNameObj + "\"";
					appender = " OR ";
				}
			}
			if (languageNamesList != null && languageNamesList.size() > 0) {
				String appender = "";
				languageFilter = "";
				for (String langName : languageNamesList) {
					languageFilter = languageFilter + appender + " targetlang :\"" + langName + "\"";
					appender = " OR ";
				}
			}
			if (companyFilter != null) {
				if (searchStr.isEmpty()) {
					searchStr = companyFilter;
				} else {
					searchStr = searchStr + " AND ( " + companyFilter + " )";
				}

			}
			if (languageFilter != null) {
				if (searchStr.isEmpty()) {
					searchStr = languageFilter;
				} else {
					searchStr = searchStr + " AND ( " + languageFilter + " ) ";
				}
			}
			docListObj = performSearch(searchStr, companyId,IndexTypeEnum.TM.name(), queryAppender);
			if(docListObj!=null){
				Long docs = docListObj.getNumFound();
				Integer[] intArray = new Integer[docs.intValue()];
				for (int i = 0; i < docListObj.size(); ++i) {
					doc = docListObj.get(i);
					Long primaryKey = (Long)doc.getFieldValue("primarykey");
					intArray[i] = new Integer(primaryKey.intValue());
					totalTerms.add(intArray[i]);

				}
			}
			if (totalTerms != null && totalTerms.size() > 0) {
				logger.debug("Total  tmIds :"
						+ totalTerms);
			} else {
				logger.debug("Got empty  tmIds");
			}
		}
		catch (Exception e) {
			logger.error("Error in getting tmIds");
		}
		return totalTerms;
	}

	/**
	 * To get selected term ids form search index
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to filter the terms
	 * @return List of term id's
	 */
	@Transactional
	public List<Integer> getSelectedTermIdsFormSearchIndex(QueryAppender queryAppender, Integer companyId) {
		List<Integer> totalTerms = new ArrayList<Integer>();
		String companyName = null;
		List<String> companyNamesList = null;
		List<String> languageNamesList = null;
		SolrDocumentList doclistObj = null;
		SolrDocument doc = null;
		
		if (companyId != null) {
			Company company = iLookUpDAO.getCompany(companyId);
			companyName = company.getCompanyName();
		}

		String querystr = queryAppender.getSearchStr() == null ? null : queryAppender.getSearchStr().trim();
		String filterBy = queryAppender.getFilterBy();
		Integer selectedIds[] = queryAppender.getSelectedIds();
		Integer companyIds[] = queryAppender.getSelectedCompanyIds();
		if (companyIds != null) {
			companyNamesList = new ArrayList<String>();
			for (int i = 0; i < companyIds.length; i++) {
				Company company = iLookUpDAO.getCompany(companyIds[i]);
				companyNamesList.add(company.getCompanyName());

			}
		}
		if (selectedIds != null) {
			if (filterBy.equalsIgnoreCase("Locale")) {
				languageNamesList = new ArrayList<String>();
				for (int i = 0; i < selectedIds.length; i++) {
					Language language = iLookUpDAO.getLanguage(selectedIds[i]);
					languageNamesList.add(language.getLanguageLabel());

				}
			}
			if (filterBy.equalsIgnoreCase("Company")) {
				companyNamesList = new ArrayList<String>();
				for (int i = 0; i < selectedIds.length; i++) {
					Company company = iLookUpDAO.getCompany(selectedIds[i]);
					companyNamesList.add(company.getCompanyName());

				}
			}
		}
		try {

			String searchStr = "";
			if (querystr != null){
				if(queryAppender.isCaseFlag()){
					searchStr = "search_data_like_casesensitive:" + querystr;
				}else{
					searchStr = "search_data_like:" + querystr;
				}
			}

			if (companyName != null) {
				if (searchStr.isEmpty()) {
					searchStr = " company:\"" + companyName + "\"";
				} else {
					searchStr = searchStr + " AND company:\"" + companyName + "\"";
				}
			}
			String companyFilter = null;
			String languageFilter = null;

			if (companyNamesList != null && companyNamesList.size() > 0) {
				String appender = "";
				companyFilter = "";
				for (String companyNameObj : companyNamesList) {
					companyFilter = companyFilter + appender + " company :\"" + companyNameObj + "\"";
					appender = " OR ";
				}
			}
			if (languageNamesList != null && languageNamesList.size() > 0) {
				String appender = "";
				languageFilter = "";
				for (String langName : languageNamesList) {
					languageFilter = languageFilter + appender + " targetlang :\"" + langName + "\"";
					appender = " OR ";
				}
			}
			if (companyFilter != null) {
				if (searchStr.isEmpty()) {
					searchStr = companyFilter;
				} else {
					searchStr = searchStr + " AND ( " + companyFilter + " )";
				}

			}
			if (languageFilter != null) {
				if (searchStr.isEmpty()) {
					searchStr = languageFilter;
				} else {
					searchStr = searchStr + " AND ( " + languageFilter + " ) ";
				}
			}
			
			doclistObj = performSearch(searchStr, companyId, IndexTypeEnum.TB.name(), queryAppender);
			Long totalDoc = doclistObj.getNumFound();
			
			// set totalDoc  Found record for deletion
			SolrQuery queryObj = new SolrQuery();
			queryObj.setQuery(searchStr);
			queryObj.setRows(totalDoc.intValue());
			queryObj.addFilterQuery("termtype:"+IndexTypeEnum.TB.name());
			
			QueryResponse rsp = server.query(queryObj);
			SolrDocumentList solrDocListObj  = rsp.getResults();
			
			Integer[] intArray = new Integer[solrDocListObj.size()];
			for(int i=0; i<solrDocListObj.size();i++){
				if(solrDocListObj != null){ 
					doc = solrDocListObj.get(i);

					String termTdGet[]=((String)doc.getFieldValue("id")).split("_");
					Integer   termId=Integer.parseInt(termTdGet[0]);
					
					Date  pollExpireDate = (Date)doc.getFieldValue("expireDate");

                    	Date todayDate = new Date();


                    	if(pollExpireDate!=null){

//                    		Date pollExpireDate = currDate.parse(dateObj);
                    		
                    		if(todayDate.before(pollExpireDate) || todayDate.toString().equalsIgnoreCase(pollExpireDate.toString()) ){
                    				logger.info("Term cannot be deleted  poll date is not expired ++++++");
                    		}
                    	}
                    	
                    	else{
                    		intArray[i] = new Integer(termId);
                    		totalTerms.add(intArray[i]);
                    	}
				}

			}
			if (totalTerms != null && totalTerms.size() > 0) {
				logger.debug("Total  termIds :"
						+ totalTerms);
			} else {
				logger.debug("Got empty  termIds");
			}
		}
		catch (Exception e) {
			logger.error(":::Error in getting  termIds:::::");
			logger.error(e,e);
		}
		return totalTerms;
	}

	/**
	 * To get selected gs term ids form search index
	 *
	 * @param queryAppender Which is used to build a query
	 * @param companyId     An Integer to filter the terms
	 * @return List of gs term id's
	 */
	@Transactional
	public List<Integer> getSelectedGSTermIdsFormSearchIndex(QueryAppender queryAppender, Integer companyId) {
		List<Integer> totalTerms = new ArrayList<Integer>();
		logger.info("Inside GS term serach For companyID:::"+companyId);
		EnumMap<IndexTypeEnum,SolrDocumentList> solDocEnum = null;
		SolrDocumentList docList = null;
		SolrDocument doc = null;
		try {
			    solDocEnum = search(queryAppender, companyId);
			    docList = solDocEnum.get(IndexTypeEnum.GS);
				if(docList!=null){
					
					Long totalRecords = docList.getNumFound();
					queryAppender.setTotalRecords(totalRecords.intValue());
					logger.info("Get totalrecords from solr"+totalRecords);
					Integer[] intArray = new Integer[totalRecords.intValue()];
					for (int i = 0; i < docList.size(); ++i) {
						
						doc = docList.get(i);
						intArray[i] =Integer.parseInt((String)doc.getFieldValue("termId"));
						totalTerms.add(intArray[i]);
					}
					
					
					if (totalTerms != null && totalTerms.size() > 0) {
						logger.debug("Total gs termIds :"
								+ totalTerms);
					} else {
						logger.debug("Got empty gs termIds");
					}
				}

			}catch (Exception e) {
				logger.error("Error in getting gs termIds");
			}
		return totalTerms;
		}
		

	/**
	 * To get tm profile info by ids
	 *
	 * @param ids List of Integer to  filter the  terms
	 * @return List of terms
	 */
	@Override
	@Transactional
	public List<TmProfileInfo> getTmProfileInfoByIds(List<Integer> ids) {
		return termDetailsDAO.getTmProfileInfoByIds(ids);

	}

	/**
	 * To delete import files
	 *
	 * @param fileIds array of integer fileIds that needs to be deleted
	 */
	@Transactional
	public void deleteImportFiles(final Integer[] fileIds) {
		if (fileIds == null) {
			throw new IllegalArgumentException("Invalid fileIds");
		}
		try {
			termDetailsDAO.deleteImportFiles(fileIds);
			logger.debug("Delete import status details");
		}
		catch (Exception e) {
			logger.error("Error in deleting  import files");
			e.printStackTrace();
		}

	}

	/**
	 * To get import  status files by fileIds
	 *
	 * @param fileIds Integer array  to  filter the  import files status
	 * @return List of import files obj w.r.t fileIds
	 */
	@Override
	@Transactional
	public List<FileUploadStatus> getImportStatusFiles(Integer[] fileIds) {
		List<FileUploadStatus> importedFileList = new ArrayList<FileUploadStatus>();
		try {
			importedFileList = termDetailsDAO.getImportStatusFiles(fileIds);
			if ((importedFileList != null)
					&& (importedFileList.size() > 0)) {
				logger.debug("Total import status files :"
						+ importedFileList.size());
			} else {
				logger.debug("Got empty import status files ");
			}

		}
		catch (Exception e) {
			logger.error("Error in getting import files");
			return null;
		}
		return importedFileList;

	}

	/**
	 * To get ids of terms belongs to given companyid
	 *
	 * @param companyId of a company
	 * @return termIds array of integer
	 */
	@Override
	@Transactional
	public Integer[] getTermIdsByCompanyId(Integer companyId) {
		if (companyId == null)
			return null;
		Integer[] termIds = null;
		List<Integer> termIdsList = termDetailsDAO.getTermIdsByCompanyId(companyId);
		if (termIdsList != null && termIdsList.size() > 0) {
			termIds = new Integer[termIdsList.size()];
			for (int i = 0; i < termIdsList.size(); i++) {
				if (termIdsList.get(i) != null)
					termIds[i] = (Integer) termIdsList.get(i);
			}
		}
		return termIds;
	}

	@Override
	@Transactional
	public TMProfileTermsResult getSelectedTermsOnly(QueryAppender queryAppender,Company company) {
		List<PollTerms> selectedTerms = new ArrayList<PollTerms>();
		TMProfileTermsResult tmProfileTermsResult = null;
		try {

			selectedTerms = getTermBaseSelectedTermsOnly(queryAppender,company);
			if ((selectedTerms != null) && (selectedTerms.size() > 0)) {
				tmProfileTermsResult = new TMProfileTermsResult();
				tmProfileTermsResult.setPollTermsList(selectedTerms);
				logger.debug("Total Terms Selected :" + selectedTerms.size());
			} else {
				logger.debug("Got empty selected  Terms ");
			}

		}
		catch (Exception e) {
			logger.error("Error in getting  selected terms");
		}
		return tmProfileTermsResult;
	}

	@Transactional
	public List<PollTerms> getTermBaseSelectedTermsOnly(QueryAppender queryAppender,Company company) {


		List<PollTerms> pollTermsList = new ArrayList<PollTerms>();
		SolrDocumentList solrDocListObj = null;
		Long startTimeInMS = null;
		SolrDocument doc = null;
		startTimeInMS = System.currentTimeMillis();
		Integer termIds[] = queryAppender.getSelectedIds();
		Integer seletedTermIds[] = queryAppender.getTermSelectedIds();
		List<PollTerms> finalPollTerms = new ArrayList<PollTerms>();

		try {
			String querystr = "";
			if(termIds != null) {
				for (Integer termId : termIds) {
					querystr = querystr + " " + termId+SuffixEnum._TB.name();
				}
			}
			if(seletedTermIds != null) {
				for (Integer termId : seletedTermIds) {
					querystr = querystr + " " + termId+SuffixEnum._TB.name();
				}
			}
			String searchStr = null;
			searchStr = "(id:" + querystr + ")";

			solrDocListObj = performSearch(searchStr, company.getCompanyId(), IndexTypeEnum.TB.name(), queryAppender);
			for (int i = 0; i < solrDocListObj.size(); ++i) {
				doc = solrDocListObj.get(i);
				String sourceTerm = (String)doc.getFieldValue("source");
				String targetTerm = (String)doc.getFieldValue("target");

				PollTerms term = new PollTerms();
				Long pk = (Long)doc.getFieldValue("primarykey");
				term.setTermId(pk.intValue());
				term.setSourceTerm(sourceTerm);
				term.setSuggestedTerm(targetTerm);
				term.setLanguage((String)doc.getFieldValue("targetlang"));
				term.setPartOfSpeech((String)doc.getFieldValue("partofspeech"));
				term.setCategory((String)doc.getFieldValue("category"));
				term.setDomain((String)doc.getFieldValue("domain"));
				term.setCompany((String)doc.getFieldValue("company"));
				term.setStatus((String)doc.getFieldValue("status"));
				if(doc.getFieldValue("expireDate")!=null){
					term.setPollExpirationDt(new SimpleDateFormat("MM/dd/yyyy").format((Date)doc.getFieldValue("expireDate")));
				}
				pollTermsList.add(term);

			}

			logger.debug("got  manage poll terms");
			String strTermIds = "";
			if(termIds != null) {
				for (int i = 0; i < termIds.length; i++) {
					if (i == 0) {
						strTermIds = termIds[i] + "";
					} else {
						strTermIds += "," + termIds[i];
					}
				}
			}
			if(seletedTermIds != null) {
				for (int i = 0; i < seletedTermIds.length; i++) {
					if (i == 0) {
						strTermIds = seletedTermIds[i] + "";
					} else {
						strTermIds += "," + seletedTermIds[i];
					}
				}
			}

			List<PollTerms> managePollTems = termDetailsDAO.getVotingDetailsByTermIds(strTermIds);
			for (PollTerms pollTerms : pollTermsList) {
				for (PollTerms poll : managePollTems) {
					if (pollTerms.getTermId().intValue() == poll.getTermId().intValue()) {
						PollTerms finalPollTerm = new PollTerms();
						finalPollTerm.setTermId(pollTerms.getTermId());
						finalPollTerm.setSourceTerm(pollTerms.getSourceTerm());
						finalPollTerm.setSuggestedTerm(pollTerms.getSuggestedTerm());
						finalPollTerm.setLanguage(pollTerms.getLanguage());
						finalPollTerm.setPartOfSpeech(pollTerms.getPartOfSpeech());
						finalPollTerm.setCategory(pollTerms.getCategory());
						finalPollTerm.setDomain(pollTerms.getDomain());
						finalPollTerm.setCompany(pollTerms.getCompany());
						finalPollTerm.setStatus(pollTerms.getStatus());
						finalPollTerm.setPollExpirationDt(pollTerms.getPollExpirationDt());
						finalPollTerm.setVotesPerTerm(poll.getVotesPerTerm());
						finalPollTerm.setInvites(poll.getInvites());
						finalPollTerm.setDeprecatedCount(poll.getDeprecatedCount());
						finalPollTerms.add(finalPollTerm);
					}
				}
			}

			pollTermsList.clear();
			pollTermsList = null;
			managePollTems.clear();
			managePollTems = null;
		}
		catch (Exception e) {
			logger.error("Error in getting  manage poll terms");
			e.printStackTrace();
		}
		finally {

			long endTimeInMS = System.currentTimeMillis();
			long totalTimeInMs = endTimeInMS - startTimeInMS;
			double totalTimeInSeconds = totalTimeInMs / 1000D;
			double totalTimeInMinutes = totalTimeInSeconds / 60D;
			String timeTook;
			if (totalTimeInMinutes >= 1D) {
				timeTook = totalTimeInMinutes + " minutes" + " or "
						+ totalTimeInSeconds + " seconds";
			} else {
				timeTook = totalTimeInSeconds + " seconds";
			}
			logger.info("Took total for TM terms" + timeTook);
			logger.info("startTimeInMinutes:" + startTimeInMS / 1000
					+ " ending time in Minutes:" + endTimeInMS / 1000);

		}

		return finalPollTerms;


	}


	//perform saerch operation

	@Transactional
	public  EnumMap<IndexTypeEnum, SolrDocumentList> search(QueryAppender queryAppender,Integer companyId){

		logger.info("search method called::");
		EnumMap<IndexTypeEnum, SolrDocumentList> solrDocMapEnumObj = new EnumMap<IndexTypeEnum, SolrDocumentList>(IndexTypeEnum.class);

		String searchType = "";
		SolrDocumentList solrDocListObj = null;
		if(queryAppender.isCaseFlag()){
			String query = caseSensitiveQuery(queryAppender,companyId);

			if(queryAppender.getSearchIndexType().equalsIgnoreCase(IndexTypeEnum.TB.name())){
				
				searchType=IndexTypeEnum.TB.name();
				logger.info("search type"+searchType);
				solrDocListObj = performSearch(query,companyId,searchType,queryAppender);
				logger.info("Sol Document List From solr"+solrDocListObj.getNumFound());
				solrDocMapEnumObj.put(IndexTypeEnum.TB, solrDocListObj);
			}
			if(queryAppender.getSearchIndexType().equalsIgnoreCase(IndexTypeEnum.TM.name())){
				
				searchType=IndexTypeEnum.TM.name();
				logger.info("search type"+searchType);
				solrDocListObj = performSearch(query,companyId,searchType,queryAppender);
				logger.info("Sol Document List From solr"+solrDocListObj.getNumFound());
				solrDocMapEnumObj.put(IndexTypeEnum.TM, solrDocListObj);
			}
			if(queryAppender.getSearchIndexType().equalsIgnoreCase(IndexTypeEnum.GS.name())){
				
				searchType=IndexTypeEnum.GS.name();
				logger.info("search type"+searchType);
				solrDocListObj = performSearch(query,companyId,searchType,queryAppender);
				solrDocMapEnumObj.put(IndexTypeEnum.GS, solrDocListObj);
			}

		}else{

			String query = caseInSensitiveQuery(queryAppender,companyId);

			if(queryAppender.getSearchIndexType().equalsIgnoreCase(IndexTypeEnum.TB.name())){
				
				searchType=IndexTypeEnum.TB.name();
				logger.info("search type"+searchType);
				solrDocListObj = performSearch(query,companyId,searchType,queryAppender);
				logger.info("Sol Document List From solr"+solrDocListObj.getNumFound());
				solrDocMapEnumObj.put(IndexTypeEnum.TB, solrDocListObj);
			}

			if(queryAppender.getSearchIndexType().equalsIgnoreCase(IndexTypeEnum.TM.name())){
				
				searchType=IndexTypeEnum.TM.name();
				
				logger.info("search type"+searchType);
				
				solrDocListObj = performSearch(query,companyId,searchType,queryAppender);
				
				logger.info("Sol Document List From solr"+solrDocListObj.getNumFound());
				
				solrDocMapEnumObj.put(IndexTypeEnum.TM, solrDocListObj);
			}
			if(queryAppender.getSearchIndexType().equalsIgnoreCase(IndexTypeEnum.GS.name())){
				
				searchType=IndexTypeEnum.GS.name();
				
				logger.info("search type"+searchType);
				
				solrDocListObj = performSearch(query,companyId,searchType,queryAppender);
				
				logger.info("Sol Document List From solr"+solrDocListObj.getNumFound());
				
				solrDocMapEnumObj.put(IndexTypeEnum.GS, solrDocListObj);
			}
		}
		return solrDocMapEnumObj;
	}

	// Forming case sensitive query
	@Transactional
	public  String caseSensitiveQuery(QueryAppender queryAppender, Integer companyId){

		logger.info("inside casesensitive search  ::"+queryAppender.isCaseFlag());
		
		String companyName = null;
		List<String> companyNamesList = null;
		List<String> languageNamesList = null;
		List<String> industrynamesList = null;
		List<String> partsOfSpeechNamesList = null;
		List<String> categoryNamesList = null;
		List<String> filterBylanguageList = null;
		String finalStatus = null;
		String searchStr = "";
		String searchType = queryAppender.getSearchType();

		try {
			//Getting all lookup data here to make filter work
			if (companyId != null) {
				Company company = iLookUpDAO.getCompany(companyId);
				companyName = company.getCompanyName();
				String querystr = queryAppender.getSearchTerm()== null ? null : queryAppender.getSearchTerm().trim();

				if(querystr==null){
					querystr = queryAppender.getSearchStr() == null ? null : queryAppender.getSearchStr().trim();
				}
				
				String filterBy = queryAppender.getFilterBy();
				Integer selectedIds[] = queryAppender.getSelectedIds();
				Integer companyIds[] = queryAppender.getSelectedCompanyIds();
				Integer languageIds[] = queryAppender.getLangValues();

				if (companyIds != null) {
					companyNamesList = new ArrayList<String>();
					for (int i = 0; i < companyIds.length; i++) {
						company = iLookUpDAO.getCompany(companyIds[i]);
						companyNamesList.add(company.getCompanyName());
					}
				}

				if(languageIds !=null){
					filterBylanguageList = new ArrayList<String>();
					List<Language> languageList = iLookUpDAO.getLanguagesByIds(languageIds);
					for (Language language : languageList) {
						filterBylanguageList.add(language.getLanguageLabel());
					}
				}
				
				if (selectedIds != null && filterBy != null) {
					if (filterBy.equalsIgnoreCase("Locale")) {
						languageNamesList = new ArrayList<String>();
						List<Language> languageList = iLookUpDAO.getLanguagesByIds(selectedIds);
						for (Language language : languageList) {
							languageNamesList.add(language.getLanguageLabel());
						}
					}

					if (filterBy.equalsIgnoreCase("Domain")) {
						industrynamesList = new ArrayList<String>();
						List<Domain> domainList = iLookUpDAO.getDomainsByIds(selectedIds);
						for (Domain domain : domainList) {

							industrynamesList.add(domain.getDomain());

						}
					}

					if (filterBy.equalsIgnoreCase("Company")) {
						companyNamesList = new ArrayList<String>();
						List<Company> companyList = iLookUpDAO.getCompaniesByIds(selectedIds);
						for (Company company1 : companyList) {
							companyNamesList.add(company1.getCompanyName());
						}
					}

					if (filterBy.equalsIgnoreCase("Part of Speech")) {

						List<PartsOfSpeech> partsOfSpeechList = iLookUpDAO.getPartOfSpeechesByIds(selectedIds);
						partsOfSpeechNamesList = new ArrayList<String>();

						for (PartsOfSpeech partsOfSpeech : partsOfSpeechList) {
							partsOfSpeechNamesList.add(partsOfSpeech.getPartOfSpeech());
						}
					}

					if (filterBy.equalsIgnoreCase("Category")) {
						List<Category> categoryList = iLookUpDAO.getCategoriesByIds(selectedIds);
						categoryNamesList = new ArrayList<String>();
						for (Category category : categoryList) {
							categoryNamesList.add(category.getCategory());
						}
					}
				}

				if (filterBy != null) {
					if (filterBy.equalsIgnoreCase("Final")) {
						Status status = iLookUpDAO.getStatusIdByLabel(StatusLookupEnum.APPROVED.getValue());
						finalStatus = status.getStatus();
					}
				}




				// Forming query to perform search operation
				if (querystr != null){

					if(searchType!=null){
						if (searchType.equalsIgnoreCase("Fuzzy")) {
							searchStr = "search_data:" + querystr + "~" + TeaminologyProperty.FUZZY_DISTANCE.getValue();
						} if (searchType.equalsIgnoreCase("Exact")) {
							searchStr = "search_data_casesensitive:\"" + querystr + "\"";
						} else if (searchType.equalsIgnoreCase("--Select--")) {
							searchStr ="search_data_like_casesensitive:\"" + querystr +"\"";
						}
					}else{
						searchStr = "search_data_like_casesensitive:\"" + querystr +"\"";
					}
				}
				if ((companyNamesList == null || companyNamesList.size() == 0) && companyName != null) {
					if (searchStr.isEmpty()) {
						searchStr = " company:\"" + companyName+"\"";
					}
				}

				String companyFilter = null;
				String languageFilter = null;
				String domainFilter = null;
				String posFilter = null;
				String categoryFilter = null;
				String filterByLanguage = null;
				
				if (companyNamesList != null && companyNamesList.size() > 0) {
					String appender = "";
					companyFilter = "";
					for (String companyNameObj : companyNamesList) {
						companyFilter = companyFilter + appender + " company :\"" + companyNameObj+"\"";
						appender = " OR ";
					}
				}
				if (languageNamesList != null && languageNamesList.size() > 0) {
					String appender = "";
					languageFilter = "";
					for (String langName : languageNamesList) {
						languageFilter = languageFilter + appender + " targetlang :\""+ langName+"\"";
						appender = " OR ";
					}
				}
				if (industrynamesList != null && industrynamesList.size() > 0) {
					String appender = "";
					domainFilter = "";
					for (String indName : industrynamesList) {
						domainFilter = domainFilter + appender + " domain :\"" + indName+"\"";
						appender = " OR ";
					}
				}
				if (partsOfSpeechNamesList != null && partsOfSpeechNamesList.size() > 0) {
					String appender = "";
					posFilter = "";
					for (String pos : partsOfSpeechNamesList) {
						posFilter = posFilter + appender + " partofspeech :\"" + pos+"\"";
						appender = " OR ";
					}
				}
				if (categoryNamesList != null && categoryNamesList.size() > 0) {
					String appender = "";
					categoryFilter = "";
					for (String cat : categoryNamesList) {
						categoryFilter = categoryFilter + appender + " category :\""+ cat+"\"" ;
						appender = " OR ";
					}
				}
				
				if (filterBylanguageList != null && filterBylanguageList.size() > 0) {
					String appender = "";
					filterByLanguage = "";
					for (String lang : filterBylanguageList) {
						filterByLanguage = filterByLanguage + appender + " targetlang :\""+lang+"\"" ;
						appender = " OR ";
					}
				}
				
				if (finalStatus != null) {

					if (searchStr.isEmpty()) {
						searchStr = companyFilter;
					} else {
						searchStr = searchStr + " AND ( status : " + finalStatus + " )";
					}


				}
				if (companyFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = companyFilter;
					} else {
						searchStr = searchStr + " AND ( " + companyFilter + " )";
					}

				}
				if (languageFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = languageFilter;
					} else {
						searchStr = searchStr + " AND ( " + languageFilter + " ) ";
					}
				}
				if (domainFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = domainFilter;
					} else {
						searchStr = searchStr + " AND ( " + domainFilter + " ) ";
					}
				}
				if (posFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = posFilter;
					} else {
						searchStr = searchStr + " AND ( " + posFilter + " ) ";
					}
				}
				
				if (categoryFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = categoryFilter;
					} else {
						searchStr = searchStr + " AND ( " + categoryFilter + " ) ";
					}
				}
				
				if (filterByLanguage != null) {
					if (searchStr.isEmpty()) {
						searchStr = filterByLanguage;
					} else {
						searchStr = searchStr + " AND ( " + filterByLanguage + " ) ";
					}
				}
			}

			logger.info("Query Fromation Completed:::"+searchStr);
		}catch(Exception e){
			logger.info("Error in forming case sensitive search query");
		}
		return searchStr;	
	}
	// froming case insensitive query
	@Transactional
	public  String caseInSensitiveQuery(QueryAppender queryAppender, Integer companyId){ 

		logger.info("case insensitive search :::");
		
		String companyName = null;
		List<String> companyNamesList = null;
		List<String> languageNamesList = null;
		List<String> industrynamesList = null;
		List<String> partsOfSpeechNamesList = null;
		List<String> categoryNamesList = null;
		List<String> contentTypeList = null;
		List<String> productLineList = null;
		List<String> taskIdsList = null;
		List<String> filterByLanguageList = null;
		String finalStatus = null;
		String searchStr = "";
		String searchType = queryAppender.getSearchType();

		try {
			//Calling lookup to filter the result
			if (companyId != null) {
				Company company = iLookUpDAO.getCompany(companyId);
				companyName = company.getCompanyName();
				String querystr = queryAppender.getSearchTerm() == null ? null : queryAppender.getSearchTerm().trim();

				if(querystr==null){
					querystr = queryAppender.getSearchStr();
				}
				
				
				String filterBy = queryAppender.getFilterBy();
				Integer selectedIds[] = queryAppender.getSelectedIds();
				Integer companyIds[] = queryAppender.getSelectedCompanyIds();
				String filterByTask = queryAppender.getFilterByTask();
				String filterByCompany = queryAppender.getFilterByCompany();
				Integer taskIds[] = queryAppender.getSelectedTaskIds();
				Integer languageIds[] = queryAppender.getLangValues();

				if (companyIds != null) {
					companyNamesList = new ArrayList<String>();
					for (int i = 0; i < companyIds.length; i++) {
						company = iLookUpDAO.getCompany(companyIds[i]);
						companyNamesList.add(company.getCompanyName());

					}
				}
				
				  if (languageIds != null) {
		                List<Language> langList = iLookUpDAO.getLanguagesByIds(languageIds);
		                if (langList != null && langList.size() > 0) {
		                    filterByLanguageList = new ArrayList<String>();
		                    for (Language lang : langList) {
		                    	filterByLanguageList.add(lang.getLanguageLabel());
		                    }
		                }

		            }

				
				if (selectedIds != null && filterBy != null) {
					if (filterBy.equalsIgnoreCase("Locale")) {
						languageNamesList = new ArrayList<String>();
						List<Language> languageList = iLookUpDAO.getLanguagesByIds(selectedIds);
						for (Language language : languageList) {
							languageNamesList.add(language.getLanguageLabel());

						}
					}
					
					if (filterBy.equalsIgnoreCase("Domain") || filterBy.equalsIgnoreCase("Industry Domain")) {
						industrynamesList = new ArrayList<String>();
						List<Domain> domainList = iLookUpDAO.getDomainsByIds(selectedIds);
						for (Domain domain : domainList) {

							industrynamesList.add(domain.getDomain());

						}
					}
					if (filterBy.equalsIgnoreCase("Company")) {
						companyNamesList = new ArrayList<String>();
						List<Company> companyList = iLookUpDAO.getCompaniesByIds(selectedIds);
						for (Company company1 : companyList) {
							companyNamesList.add(company1.getCompanyName());

						}
					}
					if (filterBy.equalsIgnoreCase("Part of Speech")) {
						List<PartsOfSpeech> partsOfSpeechList = iLookUpDAO.getPartOfSpeechesByIds(selectedIds);
						partsOfSpeechNamesList = new ArrayList<String>();
						for (PartsOfSpeech partsOfSpeech : partsOfSpeechList) {
							partsOfSpeechNamesList.add(partsOfSpeech.getPartOfSpeech());

						}
					}
					if (filterBy.equalsIgnoreCase("Category")) {
						List<Category> categoryList = iLookUpDAO.getCategoriesByIds(selectedIds);
						categoryNamesList = new ArrayList<String>();
						for (Category category : categoryList) {
							categoryNamesList.add(category.getCategory());

						}
					}

					if (filterBy.equalsIgnoreCase("Product Line")) {
						productLineList = new ArrayList<String>();
						List<ProductGroup> productGroupList = iLookUpDAO.getproducGroupsByIds(selectedIds);
						for (ProductGroup productGroup : productGroupList) {
							productLineList.add(productGroup.getProduct());

						}
					}

					if (filterBy.equalsIgnoreCase("Content Type")) {
						contentTypeList = new ArrayList<String>();
						List<ContentType> contentTypeObjList = iLookUpDAO.getContentTypesByIds(selectedIds);
						for (ContentType contentType : contentTypeObjList) {
							contentTypeList.add(contentType.getContentType());
						}
					}

				}
				
				if (taskIds != null) {
					if (filterByTask.equalsIgnoreCase("Task")) {
						taskIdsList = new ArrayList<String>();
						for (int i = 0; i < taskIds.length; i++) {
							taskIdsList.add(taskIds[i].toString());
						}
					}

				}

				if (filterBy != null) {
					if (filterBy.equalsIgnoreCase("Final")) {
						Status status = iLookUpDAO.getStatusIdByLabel(StatusLookupEnum.APPROVED.getValue());
						finalStatus = status.getStatus();
					}
				}



				//forming query
				if (querystr != null){

					if(searchType!=null){

						if (searchType.equalsIgnoreCase("Fuzzy")) {
							searchStr = "search_data:" + querystr + "~" + TeaminologyProperty.FUZZY_DISTANCE.getValue();
						} else if (searchType.equalsIgnoreCase("Exact")) {
							searchStr = "search_data:\"" + querystr + "\"";
						} else if (searchType.equalsIgnoreCase("--Select--")) {
							searchStr = "search_data_like:\"" + querystr +"\"";
						}
					}else{

						searchStr = "search_data_like:\"" + querystr +"\"";

					}
				}
				if ((companyNamesList == null || companyNamesList.size() == 0) && companyName != null) {
					if (searchStr.isEmpty()) {
						searchStr = "company:\"" + companyName+"\"";
					}
				}

				String companyFilter = null;
				String languageFilter = null;
				String domainFilter = null;
				String posFilter = null;
				String categoryFilter = null;
				String productFilter= null;
				String contentFilter = null;
				String taskListFilter = null;
				String filterByLanguage = null;
				
				//Filter Query
				if (companyNamesList != null && companyNamesList.size() > 0) {
					String appender = "";
					companyFilter = "";
					for (String companyNameObj : companyNamesList) {
						companyFilter = companyFilter + appender + " company :\"" + companyNameObj+"\"";
						appender = " OR ";
					}
				}
				if (languageNamesList != null && languageNamesList.size() > 0) {
					String appender = "";
					languageFilter = "";
					for (String langName : languageNamesList) {
						languageFilter = languageFilter + appender + " targetlang :\""+ langName+"\"";
						appender = " OR ";
					}
				}
				if (industrynamesList != null && industrynamesList.size() > 0) {
					String appender = "";
					domainFilter = "";
					for (String indName : industrynamesList) {
						domainFilter = domainFilter + appender + " domain :\"" + indName+"\"";
						appender = " OR ";
					}
				}
				if (partsOfSpeechNamesList != null && partsOfSpeechNamesList.size() > 0) {
					String appender = "";
					posFilter = "";
					for (String pos : partsOfSpeechNamesList) {
						posFilter = posFilter + appender + " partofspeech :\"" + pos+"\"";
						appender = " OR ";
					}
				}
				if (categoryNamesList != null && categoryNamesList.size() > 0) {
					String appender = "";
					categoryFilter = "";
					for (String cat : categoryNamesList) {
						categoryFilter = categoryFilter + appender + " category :\""+ cat+"\"" ;
						appender = " OR ";
					}
				}
				if (productLineList != null && productLineList.size() > 0) {
					String appender = "";
					productFilter = "";
					for (String prod : productLineList) {
						productFilter = productFilter + appender + " product :\"" + prod + "\"";
						appender = " OR ";
					}
				}
				if (contentTypeList != null && contentTypeList.size() > 0) {
					String appender = "";
					contentFilter = "";
					for (String contType : contentTypeList) {
						contentFilter = contentFilter + appender + "contenttype :\"" + contType + "\"";
						appender = " OR ";
					}
				}
				
				// Filter By language selected from search page
				if (filterByLanguageList != null && filterByLanguageList.size() > 0) {
					String appender = "";
					filterByLanguage = "";
					for (String lang : filterByLanguageList) {
						filterByLanguage = filterByLanguage + appender + "targetlang :\"" + lang + "\"";
						appender = " OR ";
					}
				}
				
				
				if (finalStatus != null) {

					if (searchStr.isEmpty()) {
						searchStr = companyFilter;
					} else {
						searchStr = searchStr + " AND ( status : " + finalStatus + " )";
					}


				}
				if (companyFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = companyFilter;
					} else {
						searchStr = searchStr + " AND ( " + companyFilter + " )";
					}

				}
				if (languageFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = languageFilter;
					} else {
						searchStr = searchStr + " AND ( " + languageFilter + " ) ";
					}
				}
				if (domainFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = domainFilter;
					} else {
						searchStr = searchStr + " AND ( " + domainFilter + " ) ";
					}
				}
				if (posFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = posFilter;
					} else {
						searchStr = searchStr + " AND ( " + posFilter + " ) ";
					}
				}
				if (categoryFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = categoryFilter;
					} else {
						searchStr = searchStr + " AND ( " + categoryFilter + " ) ";
					}
				}

				if (productFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = productFilter;
					} else {
						searchStr = searchStr + " AND ( " + productFilter + " ) ";
					}
				}

				if (contentFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = contentFilter;
					} else {
						searchStr = searchStr + " AND ( " + contentFilter + " ) ";
					}
				}
				if (taskIdsList != null && taskIdsList.size() > 0) {
					String appender = "";
					taskListFilter = "";
					for (String taskId : taskIdsList) {
						taskListFilter = taskListFilter + appender + " taskId :\"" + taskId + "\"";
						appender = " OR ";
					}
				}
				if (taskListFilter != null) {
					if (searchStr.isEmpty()) {
						searchStr = taskListFilter;
					} else {
						searchStr = searchStr + " AND ( " + taskListFilter + " ) ";
					}
				}
				
				if (filterByLanguage != null) {
					if (searchStr.isEmpty()) {
						searchStr = filterByLanguage;
					} else {
						searchStr = searchStr + " AND ( " + filterByLanguage + " ) ";
					}
				}
			}

			logger.info("Query Formation completed"+searchStr);
		}catch(Exception e){
			logger.info("Error in forming case insensitive search query");
		}
		return searchStr;	
	}
	/**
	 * 
	 * @param query
	 * @param companyId
	 * @param type
	 * @param queryAppender
	 * @return
	 */
	public SolrDocumentList performSearch(String query,Integer companyId,String type,QueryAppender queryAppender){

		logger.info("Inside performsearch:::::");
		Integer limitFrom = 0;
		SolrDocumentList solDocList = null;
		String colName = (queryAppender.getColName() == null) ? ""
				: queryAppender.getColName().trim();
		final String sortOrder = (queryAppender.getSortOrder() == null) ? ""
				: queryAppender.getSortOrder();

		try {

			Integer pageNum = queryAppender.getPageNum();
			Integer pageNumTm = queryAppender.getPageNumTm();
			String paginationVal = queryAppender.getPaginationValue();
			if(pageNum!=null){
				if (pageNum!= 0) {
					limitFrom = (pageNum - 1) * 10;
				}
			}

			if(pageNumTm!=null){
				if (pageNumTm!= 0) {
					limitFrom = (pageNumTm - 1) * 10;
				}
			}
			if(pageNum != null && pageNum!= 0 && paginationVal != null){
				limitFrom = (pageNum - 1) * Integer.parseInt(paginationVal);
			}
			logger.info("Forming solr Query ----------");
			SolrQuery solrQuery=new SolrQuery();
			solrQuery.setQuery(query);
			solrQuery.setStart(limitFrom);
			//Setting row size when user chooses "selected terms" only, because solr will give 10 by default.
			if(queryAppender.getTermSelectedIds() != null ) {
					solrQuery.setRows(queryAppender.getTermSelectedIds().length);
			}
			if(paginationVal != null) {
				solrQuery.setRows(Integer.parseInt(paginationVal));
			}
			solrQuery.addFilterQuery("termtype:"+type);
			solrQuery.addFilterQuery("companyId:"+companyId);

			solrQuery.setParam("fl",SolrDocumentFieldEnum.getSelectableDocumentField());

			logger.info("Sorting the results");
			// sort results
			if(colName.equalsIgnoreCase("domain")){
				solrQuery.setSort("domain",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("company") || colName.equalsIgnoreCase("tmcompany")){

				solrQuery.setSort("company",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("category")){

				solrQuery.setSort("category_sort",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("pollExpirationDate")){

				solrQuery.setSort("expiredate_sort",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("targetTerm") || colName.equalsIgnoreCase("sourceTerm") || colName.equalsIgnoreCase("targetTmTerm")){

				solrQuery.setSort("source_sort",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("suggestedTerm") || colName.equalsIgnoreCase("suggestedTmTerm")){

				solrQuery.setSort("target_sort",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("status")){

				solrQuery.setSort("status",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("language") || colName.equalsIgnoreCase("tmLanguage") || colName.equalsIgnoreCase("LanguageTM")){

				solrQuery.setSort("targetlang",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("POS")){
				solrQuery.setSort("partofspeech",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("LanguageTrm")){
				solrQuery.setSort("targetlang",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("product")){
				solrQuery.setSort("product",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("content")){
				solrQuery.setSort("contenttype",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);

			}else if(colName.equalsIgnoreCase("sourceLang")){
				
				solrQuery.setSort("sourcelang",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);
			}else if(colName.equalsIgnoreCase("origin")){
				
				solrQuery.setSort("origin",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);
			}else if(colName.equalsIgnoreCase("pageId")){
				
				solrQuery.setSort("fileId",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);
			}else if(colName.equalsIgnoreCase("workFlowId")){
				
				solrQuery.setSort("transUnitId",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);
			}else if(colName.equalsIgnoreCase("taskId")){
				
				solrQuery.setSort("taskId",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);
			}else if(colName.equalsIgnoreCase("jobId")){
				
				solrQuery.setSort("jobId",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);
			}else if(colName.equalsIgnoreCase("jobName")){
				
				solrQuery.setSort("jobName",sortOrder.equalsIgnoreCase(SortOrder.DESC.name()) ? ORDER.desc : ORDER.asc);
			}
			
			else{
				solrQuery.setSort("primarykey",ORDER.desc);
			}

			//perform select seach based on the specified 
			// parser type(edismax)
			if(queryAppender.getSearchStr()!=null || queryAppender.getSearchTerm()!=null){

				logger.info("Forming edismax query");
				if(queryAppender.getSearchType()==null || queryAppender.getSearchType().equalsIgnoreCase("--Select--")){
					solrQuery.setParam("defType","edismax");
					if(queryAppender.isCaseFlag()){
						solrQuery.setParam("qf","search_data_like_casesensitive");

					}else{
						solrQuery.setParam("qf","search_data_like");
					}
					solrQuery.setParam("mm","100%");
				}else{
					solrQuery.setParam("defType",TeaminologyProperty.SOLR_PARSER_TYPE.getValue());
				}
			}
	
			QueryResponse rsp = server.query(solrQuery);
			solDocList = rsp.getResults();

			if(queryAppender.getIsReplaceTerm() != null && "Y".equalsIgnoreCase(queryAppender.getIsReplaceTerm()) && !"replaced".equals(queryAppender.getSearchReplacedTerm())) {
				solrQuery.setRows((int)solDocList.getNumFound());
				QueryResponse responce = server.query(solrQuery);
				solDocList = responce.getResults();
			} 
			logger.info(" solr doc List size is  :::"+solDocList.getNumFound());
		} catch (Exception e) {
			logger.info("Error in getting solr Document list");
		}

		return solDocList;
	}

	/**
	 * Code Added for task TNG-83
	 * @param termId
	 * @return list of comments given by users
	 */
	@Override
	@Transactional
	public List<UserComment> getUserComment(Integer termId){
		
		List<UserComment> userCommentListObj = new ArrayList<UserComment>();
		
		try {
			
		//List holds all the user's comment for given term id
		List<Object> commentObjList = termDetailsDAO.getUserComments(termId);
		
		for(Object commentObj : commentObjList){

            Object[] comntObj = (Object[]) commentObj;
            
            UserComment userComment = new UserComment();
            
            int colNdx = 0;

            if ((String) comntObj[colNdx] != null) {
            	userComment.setUserName((String) comntObj[colNdx]);
            }
           
            colNdx++;
            
            if ((String) comntObj[colNdx] != null) {
            	userComment.setUserComment((String) comntObj[colNdx]);
            } else {
            	userComment.setUserComment("&nbsp;");
            }
            
            colNdx++;
            
            if ((String) comntObj[colNdx] != null) {
            	
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                userComment.setCommentDate(new SimpleDateFormat("MM/dd/yyyy").format(formatter.parse(comntObj[colNdx].toString())));
                
            } else {
            	userComment.setCommentDate("&nbsp;");
            }
            
            userCommentListObj.add(userComment);

		}
	} catch(Exception e) {
		logger.error(e, e);
		logger.info(" ++++++ Error in getting user comments ++++++ ");
	}
		return userCommentListObj;
	}
	@Override
	public Integer getTotalVotesOfUser(Integer user_id) {
		Integer totalVotes = null;
		if(user_id != null) {
			totalVotes = termDetailsDAO.getTotalVotesOfUser(user_id);
		}
		return totalVotes;
	}
	
	public List<Member> getAllBoardMembersByLanguage(Integer companyId, String userLangId) {

		List<Member> members = new ArrayList<Member>();
	        List<Member> boardMembers = new ArrayList<Member>();
	        try {
	            members = termDetailsDAO.getAllBoardMembersByLanguage(companyId,userLangId);
	            if ((members != null) && (members.size() > 0)) {
	                for (Member member : members) {
	                    if (member.getPhotoPath() == null) {
	                        member.setPhotoPath(TeaminologyProperty.PHOTO_NOT_AVAILABLE.getValue());
	                    }
	                    boardMembers.add(member);
	                }
	                logger.debug("Total Leader Board Members :" + members.size());
	            } else {
	                logger.debug("Got empty Leader Board Members");
	            }

	        }
	        catch (Exception e) {
	            logger.error("Error in getting  leader board members");
	            logger.error(e,e);
	        }

	        return boardMembers;
	  }
	
	@Override
	@Transactional
	public List<TermUpdateDetails> getHistoryDetails(List<Integer> termIdsList, String toDate, String fromDate) {
		return termDetailsDAO.getTermUpdateHistoryList(termIdsList, toDate, fromDate);
	}
	
	}

