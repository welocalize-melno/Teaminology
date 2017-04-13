package com.teaminology.hp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.teaminology.hp.bl.ImportBL;
import com.teaminology.hp.bl.SolrIndex;
import com.teaminology.hp.util.IndexTypeEnum;

public class BatchIndexServlet extends HttpServlet{

	private static Logger logger = Logger.getLogger(ImportBL.class);
	private static final long serialVersionUID = -9124229276967803910L;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	public void service(HttpServletRequest reqObj , HttpServletResponse resObj) 
			throws IOException, ServletException{

		// What should be indexed either Termbase, TM or Globalsight
		String toIndex = reqObj.getParameter("toIndex");

		// What company data to index.
		// if null based on toIndex (index type) index  all companies data
		String companyIds = reqObj.getParameter("companyIds");

		//get tm_profile_info_id to start indexing
		//this value will set starting point
		String startFrom = reqObj.getParameter("start");
		
		//upto this tm_profile_info_id index will be process.
		String endPointObj = reqObj.getParameter("end");
		
		PrintWriter pw = resObj.getWriter();

		// toIndex is mandatory. If not read from request object
		// don't process
		if(toIndex == null || toIndex.isEmpty()){
			logger.info("toIndex paremeter cannot be empty....");

			pw.println("toIndex paremeter cannot be empty. " +
					"Please enter either toIndex=TB or toIndex=TM");
			return;
		}

		// If toIndex is not TB, TM or GS don't process
		if(!toIndex.equalsIgnoreCase(IndexTypeEnum.TB.name()) &&
				!toIndex.equalsIgnoreCase(IndexTypeEnum.TM.name()) &&
				!toIndex.equalsIgnoreCase(IndexTypeEnum.GS.name())){
			logger.info("toIndex paremeter value is not valid.... "+toIndex);

			pw.println("toIndex parameter value " +toIndex+ " is not valid" +
					" Please enter either " +IndexTypeEnum.TB.name()
					+" OR " +
					IndexTypeEnum.TM.name()
					+" OR " +
					IndexTypeEnum.GS.name());
			return;
		}

		Integer startPK = null;
		Integer endPK = null;
		if(startFrom!=null){
			startPK = Integer.parseInt(startFrom);
		}
		
		if(endPointObj!=null){
			endPK = Integer.parseInt(endPointObj);
		}
		Set<Integer> compIds = null;
		List<String> ids = null;

		SolrIndex indexDataSolr = new SolrIndex();

		// Get the companyId from the request
		if(companyIds!=null && !companyIds.isEmpty()){
			try{
			String [] companyIdsArray = companyIds.split(",");
			ids = Arrays.asList(companyIdsArray);
			compIds = new HashSet<Integer>();

			for(String id: ids){
				compIds.add(Integer.parseInt(id));
			}
			}catch(NumberFormatException nfe){
				logger.error(nfe, nfe);
				pw.println("CompanyId is not valid. Please enter only numbers. "
						+"If multiple companyId please separate by comma. Got companyIds = "+companyIds);
			}catch(Exception e){
				pw.println("CompanyId is not valid. Please enter only numbers. "
						+"If multiple companyId please separate by comma. Got companyIds = "+companyIds);
			}			
		}
		// Start indexing based on index type
		if(toIndex.equalsIgnoreCase("TB")){
			indexDataSolr.processTermBaseIndex(compIds,startPK,endPK);
		}else if(toIndex.equalsIgnoreCase("TM")){
			indexDataSolr.processTMIndex(compIds,startPK,endPK);
		}else if(toIndex.equalsIgnoreCase("GS")){
			indexDataSolr.processGSTermBaseIndex(compIds,startPK,endPK);
		}

		pw.println("Completed indexing. Got request parameters "+
				" toIndex = "+toIndex+" companyIds = "+companyIds);

	}	
}