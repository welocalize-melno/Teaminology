package com.teaminology.hp.service.thread;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import com.teaminology.hp.Utils;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.ContentType;
import com.teaminology.hp.bo.lookup.Domain;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.bo.lookup.ProductGroup;
import com.teaminology.hp.service.enums.HttpStatusCodeEnum;
import com.teaminology.hp.service.enums.TeaminologyProperty;

/**
 * A class is used to import  TMX thread using jetty server
 */
public class ImportTMXThread implements Runnable
{

    private Integer fileId;
    List<Language> languageList;
    List<ProductGroup> productGroupList;
    List<Domain> domainList;
    List<ContentType> contentTypeList;
    List<Company> companyList;
    private static Logger logger = Logger.getLogger(ImportTMXThread.class);

    // Passing fileId as String, since the jetty application can accept 'all' or comma separated userId
    public ImportTMXThread(Integer fileId, List<Language> languageList, List<ProductGroup> productGroupList, List<Domain> domainList, List<ContentType> contentTypeList, List<Company> companyList) {
        this.fileId = fileId;
        this.languageList = languageList;
        this.productGroupList = productGroupList;
        this.domainList = domainList;
        this.contentTypeList = contentTypeList;
        this.companyList = companyList;

    }

    public void run() {
        runNetworkStats(fileId);
    }

    private void runNetworkStats(Integer fileId) {
        if (Utils.isNull(fileId)) {
            return;
        }
        String IMPORT_NETWORK_STATS_URL = TeaminologyProperty.IMPORT_JETTY_EVENTS_URL.getValue();
        logger.info("Calling Import events  in jetty server with fileId" + fileId);
        try {
            //Form the URL Object
            String apiURL = IMPORT_NETWORK_STATS_URL + fileId;

            URL url = new URL(apiURL);
            // Open the Connection and call the API
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (!Utils.isNull(httpURLConnection)) {
                logger.info("Got status code " + httpURLConnection.getResponseCode() + " for api " + apiURL);
                // Check if the status code is 200 then get the inputstream response
                if (httpURLConnection.getResponseCode() == HttpStatusCodeEnum.HTML_STATUS_200.getIntegerValue().intValue()) {
           
                } else {
                    logger.error("Error in getting InputStream for api " + apiURL);
                }
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
