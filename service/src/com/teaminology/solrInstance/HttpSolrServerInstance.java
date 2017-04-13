package com.teaminology.solrInstance;
import com.teaminology.hp.service.enums.TeaminologyProperty;

import org.apache.solr.client.solrj.impl.HttpSolrServer;


public enum HttpSolrServerInstance {
	INSTANCE;

	HttpSolrServerInstance(){
		server = new HttpSolrServer(TeaminologyProperty.SOLR_URL.getValue());
	}

	private HttpSolrServer server = null;

	public HttpSolrServer getServer(){
		return server;
	}

}
