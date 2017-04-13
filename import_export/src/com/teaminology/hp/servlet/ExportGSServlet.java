package com.teaminology.hp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.teaminology.hp.bl.ExportGSBL;
import com.teaminology.hp.util.Util;

/**
 * @author Sushma
 */

public class ExportGSServlet extends HttpServlet
{

    private static final long serialVersionUID = -890866203732389853L;

    private static Logger logger = Logger.getLogger(ExportGSServlet.class);

    private static Set<Long> fileIdsSet = null;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {

        logger.info(" export Process start ");
        PrintWriter pw = resp.getWriter();
        String fileId = req.getParameter("fileId");
        if (!Util.isEmpty(fileId)) {
            Integer companyId = null;
            if (req.getParameter("companyid") != null) {
                companyId = new Integer(req.getParameter("companyid"));
            }

            logger.info(" export Process started for uploadFileId " + fileId);
            // Add the fileId in the set
            // Start the Import Process for the FileId
            try {
                ExportGSBL.startExport(fileId, companyId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            logger.info(" Export Servlet..........");
            logger.info(" Export completed " + fileId);
        } else {

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
