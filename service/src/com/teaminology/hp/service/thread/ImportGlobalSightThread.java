package com.teaminology.hp.service.thread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import com.google.gson.Gson;
import com.teaminology.hp.Utils;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.data.GSTermInfoTo;
import com.teaminology.hp.data.GlobalsightTermInfoTO;
import com.teaminology.hp.data.GsListObject;
import com.teaminology.hp.service.IGSService;
import com.teaminology.hp.service.ILookUpService;
import com.teaminology.hp.service.enums.HttpStatusCodeEnum;
import com.teaminology.hp.service.enums.IndexTypeEnum;
import com.teaminology.hp.service.enums.SuffixEnum;
import com.teaminology.hp.service.enums.TeaminologyProperty;
import com.teaminology.solrInstance.HttpSolrServerInstance;

/**
 * A class is used to import global sight thread using jetty server
 */
public class ImportGlobalSightThread implements Runnable
{

    private Integer fileId;
    private Integer companyId;
    private ILookUpService iLookupService;
    private IGSService gsService;
    private static Logger logger = Logger.getLogger(ImportGlobalSightThread.class);

    // Passing fileId as String, since the jetty application can accept 'all' or comma separated userId
    public ImportGlobalSightThread(Integer fileId, Integer companyId, ILookUpService iLookupService, IGSService gsService) {
        this.fileId = fileId;
        this.companyId = companyId;
        this.iLookupService = iLookupService;
        this.gsService = gsService;
    }

    public void run() {
        runGSImport(fileId, companyId, iLookupService, gsService);
    }

    private void runGSImport(Integer fileId, Integer companyId, ILookUpService iLookupService, IGSService gsService) {
        if (Utils.isNull(fileId)) {
            return;
        }
        logger.info("Calling Import GS events  in jetty server with fileId" + fileId);
        try {
            //Form the URL Object
            String apiURL = TeaminologyProperty.IMPORT_GS_JETTY_EVENTS_URL.getValue();
            apiURL = apiURL + "fileid=" + fileId;
            if (companyId == null) {
                Company companyObj = iLookupService.getCompanyIdByLabel(TeaminologyProperty.INTERNAL_COMPANY.getValue());
                if (companyObj != null)
                    companyId = companyObj.getCompanyId();

            }


            if (companyId != null) {
                apiURL = apiURL + "&companyId=" + companyId;
            }

            URL url = new URL(apiURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (!Utils.isNull(httpURLConnection)) {
                logger.info("Got status code " + httpURLConnection.getResponseCode() + " for api " + apiURL);
      
            } else {
                logger.error("Got empty HTTPURLConnection object for api.... " + apiURL);
            }
        }
        catch (Exception e) {
            logger.error("Error in connecting jetty server ");
            logger.error(e, e);
        }
    }
}
