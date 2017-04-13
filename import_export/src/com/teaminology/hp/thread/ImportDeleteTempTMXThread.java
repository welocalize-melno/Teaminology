package com.teaminology.hp.thread;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.teaminology.hp.dao.ImpExp.hibernate.ImportDAOImpl;

public class ImportDeleteTempTMXThread implements Runnable
{

    private String fileId;
    private Session session;

    private static Logger logger = Logger.getLogger(ImportDeleteTempTMXThread.class);

    // Passing fileId as String, since the jetty application can accept 'all' or comma separated userId
    public ImportDeleteTempTMXThread(String fileId, Session session) {
        this.fileId = fileId;
        this.session = session;

    }

    public void run() {
        deleteTempTMS(fileId, session);
    }

    private void deleteTempTMS(String fileId, Session session) {

        try {
            ImportDAOImpl importDAOImpl = ImportDAOImpl.getInstance();
            importDAOImpl.deleteTempTmData(fileId, session);

        }
        catch (Exception e) {
            logger.error("Error in Delete Temp Tm Data ");
            logger.error(e, e);
        }
    }
}
