package com.teaminology.hp.servlet;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import com.teaminology.hp.Enum.SuffixTerm;
import com.teaminology.hp.bl.ImportGSBL;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.util.HttpSolrServerInstance;
import com.teaminology.hp.util.IndexTypeEnum;
import com.teaminology.hp.util.Util;

/**
 * @author Sushma
 */

public class ImportGSServlet extends HttpServlet
{

	private static final long serialVersionUID = -890866203732389853L;

	private static Logger logger = Logger.getLogger(ImportGSServlet.class);

	//	private static Set<Long> fileIdsSet = null;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
	ServletException, IOException {
		// Get the contacts uploaded fileid
		String fileId = req.getParameter("fileid");
		String companyId = req.getParameter("companyId");
		Collection<SolrInputDocument> collObj = new ArrayList<SolrInputDocument>(); 

		if (!Util.isEmpty(fileId)) {

			Integer company = null;
			if (companyId != null) {
				company = new Integer(companyId);
			}
			Integer file = null;
			if (fileId != null) {
				file = new Integer(fileId);
			}
			logger.info(" Import GS Process started for taskId " + fileId);

			try {

				List<GlobalsightTermInfo> insertedGSTermInfoList = ImportGSBL.startImport(file, company);

				logger.info("Total GS terms to Index :::"+insertedGSTermInfoList.size());
				HttpSolrServer solrServer = HttpSolrServerInstance.INSTANCE.getServer();
				if(insertedGSTermInfoList!=null && !insertedGSTermInfoList.isEmpty()){
				for(GlobalsightTermInfo term:insertedGSTermInfoList){

					String gstermId = term.getGlobalsightTermInfoId()+SuffixTerm._GS.name();
					String source =	term.getSourceSegment();
					String target =	term.getTargetSegment();	
					Integer sourcelang = term.getFileInfo().getSourceLang();
					Integer targetlang =term.getFileInfo().getTargetLang();
					String origin =	term.getOrigin();
					Integer comapnyId = term.getCompany().getCompanyId();
					String fileIds =	term.getFileInfo().getFileId().toString();
					Integer transUnitId =	term.getTransUnitId();

					Integer termId = term.getTermInformationId().getTermId();
					String jobId = term.getFileInfo().getJobId();
					String jobName = term.getFileInfo().getJobName();
					String taskId = term.getFileInfo().getTaskId();
					String taskName = term.getFileInfo().getTaskName();


					SolrInputDocument doc = new SolrInputDocument();

					if (gstermId != null)
						doc.addField("id", gstermId);
					if (source != null)
						doc.addField("source", source);
					if (target != null)
						doc.addField("target", target);
					if (fileId != null)
						doc.addField("fileId", fileIds);
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

					doc.addField("company", term.getCompany().getCompanyName());
					if (sourcelang != null)
						doc.addField("sourcelang", sourcelang);

					if (targetlang != null)
						doc.addField("targetlang", targetlang);
					doc.addField("termtype", IndexTypeEnum.GS.name());
					doc.addField("primarykey",term.getGlobalsightTermInfoId());
					collObj.add(doc);

				}
				
				logger.info("Commiting document in solr");
				solrServer.add(collObj);
				solrServer.commit();
				logger.info(":::Data Indexed completed:::");
				}else{
					logger.info("::Data Indexed failed::");
				}
				
			} 
			catch (Exception e) {
				logger.info("Failed  to index GS data ");
				logger.error(e, e);
			}
			logger.info("Import Export Servlet..........");
			//logger.info(" Import completed for uploadFileId "+fileIdStr);

		} else {
			logger.info(" Got empty request parameter uploadFileId");
		}
	}

}
