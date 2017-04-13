package com.teaminology.hp;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.teaminology.hp.bl.ImportBL;
import com.teaminology.hp.servlet.ExportGSServlet;
import com.teaminology.hp.servlet.ExportServlet;
import com.teaminology.hp.servlet.ImportGSServlet;
import com.teaminology.hp.servlet.ImportServlet;
import com.teaminology.hp.servlet.BatchIndexServlet;
import com.teaminology.hp.util.ImportExportProperty;


/**
 * @author Sushma
 */
public class ImportExportRunner
{

    private static Logger logger = Logger.getLogger(ImportExportRunner.class);

    public static final String LS = System.getProperty("line.separator");

    private static final String SERVER_PORT = ImportExportProperty.SERVER_PORT.getValue();

    public ImportExportRunner() {
    }

    public static void main(String[] args) {

        try {
            logger.info("Setting up hibernate....");
            ImportBL.loadHibernate();

            System.out.println("Import Export Runner..........");
            logger.info("Starting Jetty Server...");
            logger.info("Server will be binded to port " + SERVER_PORT);
            //Bind the server to a given port
            Server jettyServer = new Server(Integer.parseInt(SERVER_PORT));

            ServletContextHandler contextHandler = new ServletContextHandler(
                    ServletContextHandler.SESSIONS);
            contextHandler.setContextPath("/");
            jettyServer.setHandler(contextHandler);
            System.out.println("Import Export Runner after Handling jetty server..........");
            //Add the ImportServlet with path /importFile

            contextHandler.addServlet(new ServletHolder(
                    new ImportServlet()), "/importFile");

            contextHandler.addServlet(new ServletHolder(
                    new ExportServlet()), "/exportFile");

            contextHandler.addServlet(new ServletHolder(
                    new ImportGSServlet()), "/importGsFile");

            contextHandler.addServlet(new ServletHolder(
                    new ExportGSServlet()), "/exportGsFile");
            
            contextHandler.addServlet(new ServletHolder(
                    new BatchIndexServlet()), "/indexData");
            // Start the Jetty server
            jettyServer.start();
            logger.info("Jetty Server started successfully");
            jettyServer.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}