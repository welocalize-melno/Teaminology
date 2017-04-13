package com.teaminology.hp.service.enums;

import java.io.File;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.teaminology.hp.Utils;

/**
 * Contains keys to get the values from properties files.
 *
 * @author sarvanic
 */
public enum TeaminologyProperty
{

    MAIL_HOST,
    MAIL_SMTP_PORT,
    MAIL_AUTH_ENABLE,
    MAIL_AUTH_USER,
    MAIL_AUTH_PWD,
    UPLOAD_ROOT_DIR,
    FORGOT_PASSWORD_MAIL_SENDER,
    INVITE_USER_EMAIL_TEMPLATE,
    TERM_CHART_DATA_EXCEL,
    TERMS_EXCEL,
    LANGUAGE_EXCEL,
    LANGUAGE_DISTRIBUTION,
    CROPPED_IMAGE_PATH,
    LICENCED_KEY,
    LIB_FILE,
    FACECROP_LIB_PATH,
    EXPORT_USER,
    LANGUAGE_REPORT_TABLE,
    TERM_CHART_GLOSSARY,
    PHOTO_NOT_AVAILABLE,
    CONTEXT_PATH,
    TERM_IMAGE_PATH,
    GS_URL,
    GS_UNAME,
    GS_PWD,
    GS_DOMAIN,
    TM_CHART_GLOSSARY,
    IMPORT_EXPORT_JETTY_THREADS,
    IMPORT_JETTY_EVENTS_URL,
    EXPORT_JETTY_EVENTS_URL,
    DOMAIN,
    JSP_FILE_LOCATION,
    COMPANY_DIR_LOCATION,
    DB_HOST_NAME,
    DB_USER_NAME,
    DB_PASSWORD,
    DBNAME,
    IMPORT_EXPORT_GS_JETTY_THREADS,
    IMPORT_GS_JETTY_EVENTS_URL,
    EXPORT_GS_JETTY_EVENTS_URL,
    RELEASE,
    CSV_SAMPLE_TEMPLATE,
    ADMIN_MANUAL,
    USER_MANUAL,
    INTERNAL_COMPANY,
    IMPORT_MAXFILESIZE_IN_MB,
    TBX_SAMPLE_TEMPLATE,
    FUZZY_DISTANCE,
	VERSION,
    NEW_USER_REGISTRATION_EMAIL_HP,
    EXPORT_VOTE_RESULTS,
    HOST,
    NEW_USER_REGISTRATION_EMAIL,
    SOLR_PARSER_TYPE,
    SOLR_URL,
    TAB_SAMPLE_TEMPLATE,
    EXPORT_HISTORY_RESULTS;

    //private static PropertyResourceBundle propResourceBundle;
    private final String value;
    private InputStream inputStreamObj = null;
    
    private TeaminologyProperty() {
        this.value = ResourceBundle.getBundle("teaminology").getString(this.name());
    	//init();
    }

   /* public String getValue() {
        return value;
    }*/

    public String getValue() {
		/*if (isFileChanged()) {
			logger.info("Property file has changed");
			init();
		}*/
		//return (String)propResourceBundle.handleGetObject(this.name());
    	return value;
	}
    
    @Override
    public String toString() {
        return getValue();
    }
    
    /*private void init(){
		String teaminologyPropertyFileURL = TeaminologyFileURL.TEAMINOLOGY_PROPERTY_FILE_LOCATION.getValue();
		propResourceBundle = Utils.getPropertyResourceBundle(teaminologyPropertyFileURL);
	}
	*/


}
