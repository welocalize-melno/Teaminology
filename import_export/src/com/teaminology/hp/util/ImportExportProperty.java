package com.teaminology.hp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author
 */
public enum ImportExportProperty
{

    // Database Connection
    DB_DRIVER,
    DB_URL,
    DB_USER,
    DB_PWD,

    // Port number of server
    SERVER_PORT,
    UPLOAD_PATH,
    LOGS_PATH,
    GS_DOWNLOAD_PATH,
    ADMIN_COMPANY,
    NO_OF_RECORDS_PER_BATCH,
    NO_OF_EXPORT_RECORDS,
    SOLR_URL;

    private String value;

    private ImportExportProperty() {
        this.value = ResourceBundle.getBundle("import_export").getString(this.name());
    }

    public String getValue() {
        return value;
    }

    public boolean getBoolean() {
        return Boolean.parseBoolean(value);
    }

    public int getIntValue() {
        return Integer.parseInt(value);
    }

    public Date getDateValue() {

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = df.parse(value);
        }
        catch (ParseException e) {
            //ignore
        }

        return date;
    }

    @Override
    public String toString() {
        return getValue();
    }

}
