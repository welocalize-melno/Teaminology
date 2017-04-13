package com.teaminology.hp.util;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

public class SolrUtil {
	private static final Integer batchSize = 100;
	private static Logger logger = Logger.getLogger(SolrUtil.class);

	public static void saveData(Collection<SolrInputDocument> documents) throws Exception{
		if(documents == null || documents.isEmpty()){
			return;
		}
		Integer counter = 0;
		
		List<List<SolrInputDocument>> docBatch = 		
				Util.createBatches(documents, batchSize);
		
		logger.info("Data will be saved in "+docBatch.size()+" batches....");
		
		for(List<SolrInputDocument> data : docBatch){
			logger.info("Saving in solr batch no  "+(++counter)+"/"+docBatch.size());
			
			// Get the connection to solr database
			HttpSolrServer httpserver = HttpSolrServerInstance.INSTANCE.getServer();
			httpserver.setParser(new XMLResponseParser());
			
			//Add and commit to solr
			httpserver.add(data);
			httpserver.commit();
			logger.info("Completed Saving in solr batch no "+(counter)+"/"+docBatch.size());
		}
	}
}
