package com.teaminology.hp.util;

import org.apache.solr.client.solrj.impl.HttpSolrServer;


public enum HttpSolrServerInstance {
	INSTANCE;

	HttpSolrServerInstance(){
		server = new HttpSolrServer(ImportExportProperty.SOLR_URL.getValue());
	}

	private HttpSolrServer server = null;

	public HttpSolrServer getServer(){
		return server;
	}

}
