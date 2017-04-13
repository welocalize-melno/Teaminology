package com.teaminology.hp.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.teaminology.hp.bl.ImportBL;
import com.teaminology.hp.bo.TMProfileList;
import com.teaminology.hp.util.Util;

/**
 * @author Sushma
 */

public class ImportServlet extends HttpServlet
{

    private static final long serialVersionUID = -890866203732389853L;

    private static Logger logger = Logger.getLogger(ImportServlet.class);

    private static Set<Long> fileIdsSet = null;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        // Get the contacts uploaded fileid
        String fileIdStr = req.getParameter("fileId");


        TMProfileList tmProfileList = new TMProfileList();

        if (!Util.isEmpty(fileIdStr)) {
            Long fileId = Long.parseLong(fileIdStr);
            if (!Util.isNull(fileIdStr)) {
                logger.info(" Import Process started for uploadFileId " + fileIdStr);
                // Add the fileId in the set
                fileIdsSet.add(fileId);
                // Start the Import Process for the FileId
                try {
                    tmProfileList = ImportBL.startImport(fileIdStr, req);
                    Gson gson = new Gson();
                    Map<String, TMProfileList> tmProfileMap = new HashMap<String, TMProfileList>();
                    tmProfileMap.put("tmprofile", tmProfileList);
                    String gsonObject = gson.toJson(tmProfileMap);
                    pw.print(gsonObject);
                }
                catch (JAXBException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                logger.info("Import Export Servlet..........");
                logger.info(" Import completed for uploadFileId " + fileIdStr);
            } else {
                logger.info("Duplicate FileId " + fileId + ".Request will not be processed");
            }
        } else {
            logger.info(" Got empty request parameter uploadFileId");
        }
    }

    // Initialise a singleton instance of set object
    // This will be used to keep track of contact match is process of already completed
    // If anything exists in this set do not process
    static {
        if (Util.isNull(fileIdsSet)) {
            fileIdsSet = new HashSet<Long>();
        }
    }
}
