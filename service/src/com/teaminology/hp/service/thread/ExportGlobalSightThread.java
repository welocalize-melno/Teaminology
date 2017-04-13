package com.teaminology.hp.service.thread;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.teaminology.hp.Utils;
import com.teaminology.hp.service.enums.HttpStatusCodeEnum;
import com.teaminology.hp.service.enums.TeaminologyProperty;

/**
 * A class is used to export global sight thread using jetty server
 */
public class ExportGlobalSightThread implements Runnable
{


    private String fileId;
    private Integer companyId;
    private static Logger logger = Logger.getLogger(ExportGlobalSightThread.class);

    public ExportGlobalSightThread(String fileId, Integer companyId) {
        this.fileId = fileId;
        this.companyId = companyId;

    }

    public void run() {
        runNetworkStats(fileId, companyId);
    }

    private void runNetworkStats(String fileId, Integer companyId) {
        if (Utils.isNull(fileId)) {
            return;
        }
        String EXPORT_NETWORK_GS_STATS_URL = TeaminologyProperty.EXPORT_GS_JETTY_EVENTS_URL.getValue();

        try {
            //Form the URL Object
            String apiURL = EXPORT_NETWORK_GS_STATS_URL + fileId;
            if (companyId != null) {
                apiURL = apiURL + "&companyid=" + companyId;
            }

            URL url = new URL(apiURL);
            // Open the Connection and call the API
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (!Utils.isNull(httpURLConnection)) {
                logger.info("Got status code " + httpURLConnection.getResponseCode() + " for api " + apiURL);
                // Check if the status code is 200 then get the inputstream response
                if (httpURLConnection.getResponseCode() == HttpStatusCodeEnum.HTML_STATUS_200.getIntegerValue().intValue()) {
                    logger.debug("Got InputStream for api " + apiURL);
                    httpURLConnection.getInputStream();
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
