package com.teaminology.hp.service;

import java.io.File;
import java.util.List;

import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.TmProfileInfo;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.data.CSVImportStatus;
import com.teaminology.hp.data.TeaminologyObject;
import com.teaminology.hp.data.TermInformationTo;

/**
 * Contains method prototypes for importing and exporting files.
 *
 * @author sarvanic
 */


public interface IImportExportService
{

    /**
     * To  import csv
     *
     * @param uploadFile  Imported file to be read
     * @param lookupClass Instance of User/TermInformation persistence class to be imported
     * @param companyIds  String to be read
     * @return CSVImportStatus object
     */
    public <C extends TeaminologyObject> CSVImportStatus readCSV(File uploadFile, Class<C> lookupClass, User user, String companyIds);

    /**
     * To  import TBX
     *
     * @param uploadFile  Imported file to be read
     * @param lookupClass Instance of User/TermInformation persistence class to be imported
     * @param companyIds  String to be read
     * @return String holding status of the imported TBX
     */
    public <C extends TeaminologyObject> CSVImportStatus readTBX(File uploadFile, Class<C> lookupClass, User user, String companyIds);

    /**
     * To  export csv
     *
     * @param lookupClass       Object list to be exported
     * @param absolutePath      Path of the export file template
     * @param uploadedfilesPath Path of the location where files are uploading
     * @return CSV file in which the data is written
     */

    public <C extends TeaminologyObject> File writeCSV(List<C> lookupClass, String absolutePath, String uploadedfilesPath,String ExportedFormate);
 public <C extends TeaminologyObject> File writeVoteCSV(List<C> lookupClass, String absolutePath, String uploadedfilesPath, List<C> params);

    /**
     * To  write tbx
     *
     * @param termInfoList list to be written in TBX
     * @param absolutePath temporary path to create a file
     * @return TBX file in which data is written
     */

    public <C extends TeaminologyObject> File writeTBX(List<TermInformationTo> termInfoList, String absolutePath);

    /**
     * To  write tmx
     *
     * @param termInfoList list to be written in TMX
     * @param absolutePath temporary path to create a file
     * @return TBX file in which data is written
     */

    public <C extends TeaminologyObject> File writeTMX(List<TmProfileInfo> tmProfileInfoList, String absolutePath);

    /**
     * To  export xliff
     *
     * @param globalSightTermInfoList list to be written in Xliff
     * @param absolutePath            temporary path to create a file
     * @return Xliff file in which data is written
     */

    public <C extends TeaminologyObject> File exportXliff(List<GlobalsightTermInfo> globalSightTermInfoList, String absolutePath);

    /**
     * To  import TMX
     *
     * @param uploadFile  Imported file to be read
     * @param lookupClass Instance of User/TermInformation persistence class to be imported
     * @return String holding status of the imported TMX
     */
    public <C extends TeaminologyObject> CSVImportStatus readTMX(File uploadFile, Class<C> lookupClass);


}
