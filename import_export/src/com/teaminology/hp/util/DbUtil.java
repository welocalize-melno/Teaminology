package com.teaminology.hp.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;


/**
 * Generic database utility functions - Connect, Disconnect, etc.
 *
 * @author
 */
public class DbUtil
{

    private static Logger logger = Logger.getLogger(DbUtil.class);

    private static final String DB_DRIVER = ImportExportProperty.DB_DRIVER.getValue();
    private static final String DB_URL = ImportExportProperty.DB_URL.getValue();
    private static final String DB_USERID = ImportExportProperty.DB_USER.getValue();
    private static final String DB_PASSWORD = ImportExportProperty.DB_PWD.getValue();

    private static BasicDataSource bds = null;

    /**
     * lazily creates a connection to insight database and returns the connection
     * object. Also sets the auto-commit mode to false.
     *
     * @return _conn
     * @throws SQLException
     */
    public static Connection getDBConnection() {
        if (bds == null) {
            setupDataSource();
        }

        try {
            printDataSourceStats();
            return bds.getConnection();
        }
        catch (SQLException e) {
            logger.error(e, e);

            //wait for 10 seconds and retry
            try {
                Thread.sleep(10000);
            }
            catch (InterruptedException e1) {
                //none
            }

            bds = null;
            return getDBConnection();
        }
    }

    public static void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        }
        catch (SQLException e) {
            logger.error("SQL exception while closing statement.", e);
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        catch (SQLException e) {
            logger.error("SQL exception while closing result set.", e);
        }
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        }
        catch (SQLException e) {
            logger.error("SQL exception while closing connection.", e);
        }
    }

    public static void setupDataSource() {
        bds = new BasicDataSource();
        bds.setDriverClassName(DB_DRIVER);
        bds.setUsername(DB_USERID);
        bds.setPassword(DB_PASSWORD);
        bds.setUrl(DB_URL);
        printDataSourceStats();
    }

    private static void printDataSourceStats() {
        logger.info("NumActive connections in data source: " + bds.getNumActive());
        logger.info("NumIdle connections in data source: " + bds.getNumIdle());
    }

    @SuppressWarnings("unused")
    private static void shutdownDataSource() throws SQLException {
        bds.close();
    }

}
