package com.teaminology.hp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.teaminology.hp.bl.ExportBL;
import com.teaminology.hp.util.Util;

/**
 * @author Sushma
 */

public class ExportServlet extends HttpServlet
{

    private static final long serialVersionUID = -890866203732389853L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

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
        // Get the contacts uploaded fileid
        String fileId = req.getParameter("fileId");
        String exportBy = req.getParameter("exportBy");
        String selectedIds = req.getParameter("selectedIds");
        String companyId = req.getParameter("companyId");
        pw.println("Response from Export servlet");
        pw.println("Export By " + exportBy);
        pw.println("selectedIds" + selectedIds);

        if (!Util.isEmpty(selectedIds)) {

            if (!Util.isNull(selectedIds)) {
                logger.info(" export Process started for uploadFileId " + fileId+
                		" with selected optionIds "+selectedIds+" for companyIds "+companyId);
                // Add the fileId in the set
                // Start the Import Process for the FileId
                try {
                    ExportBL.startExport(fileId, exportBy, selectedIds, companyId);
                }
                catch (JAXBException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                logger.info(" Export Servlet..........");
                logger.info(" Export completed " + fileId);
            } else {

            }
        } else {
            logger.info(" Got empty request parameter selectedIds");
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
