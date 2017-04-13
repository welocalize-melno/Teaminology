package com.teaminology.hp.service.thread;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.teaminology.hp.Utils;
import com.teaminology.hp.service.enums.HttpStatusCodeEnum;
import com.teaminology.hp.service.enums.TeaminologyProperty;

/**
 * A class is used to export TMX thread using jetty server
 */
public class ExportTMXThread implements Runnable
{

    private String exportBy;
    private Integer[] intSelectedIds;
    private Integer fileId;
    private Integer companyId;
    private static Logger logger = Logger.getLogger(ExportTMXThread.class);

    public ExportTMXThread(Integer fileId, String exportBy, Integer[] intSelectedIds, Integer companyId) {
        this.fileId = fileId;
        this.exportBy = exportBy;
        this.intSelectedIds = intSelectedIds;
        this.companyId = companyId;
    }

    public void run() {
        runNetworkStats(fileId, exportBy, intSelectedIds, companyId);
    }

    private void runNetworkStats(Integer fileId, String exportBy, Integer[] intSelectedIds, Integer companyId) {
        if (Utils.isNull(exportBy)) {
            return;
        }
        String EXPORT_NETWORK_STATS_URL = TeaminologyProperty.EXPORT_JETTY_EVENTS_URL.getValue();
        logger.info("Calling Import events  in jetty server with exportBy" + exportBy);
        try {
            //Form the URL Object
            String apiURL = EXPORT_NETWORK_STATS_URL + fileId;
            exportBy = exportBy.replaceAll("\\s", "");
            apiURL += "&exportBy=" + exportBy;
            if (companyId != null) {
                apiURL += "&companyId=" + companyId;

            }
            if (intSelectedIds != null && intSelectedIds.length > 0) {
                String selectedIds = "";
                if (intSelectedIds != null && intSelectedIds.length != 0) {
                    for (int i = 0; i < intSelectedIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedIds += separator + intSelectedIds[i];
                    }

                }
                apiURL += "&selectedIds=" + selectedIds;

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
