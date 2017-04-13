package com.teaminology.hp.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

import com.globalsight.www.webservices.AmbassadorProxy;
import com.teaminology.hp.bo.GsCreditials;


public class Util
{

    private static Logger logger = Logger.getLogger(Util.class);

    private static final String LS = System.getProperty("line.separator");

    public static final int DB_NA = -999999999;
    public static final int DB_NM = -888888888;
    public static final int DB_NAM = -1000000000;

    public static final String DB_DATE_NA_STR = "01/01/0001";
    public static final String DB_DATE_NA_QRY_STR = "TO_DATE('01/01/0001', 'MM/DD/YYYY')";
    public static Date DB_DATE_NA;
    private static long DB_DATE_TIME_NA;

    // formatter for printing fy ends
    private static final SimpleDateFormat FY_PRINT = new SimpleDateFormat("MM/yy");
    // formatter for printing regular dates
    private static final SimpleDateFormat DATE = new SimpleDateFormat("MM/dd/yy");
    private static final SimpleDateFormat DATEMMDDYY = new SimpleDateFormat("MMddyy");
    private static final SimpleDateFormat DATEMMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat DATEMMMMyyyy = new SimpleDateFormat("MMMM yyyy");
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance();
    private static final NumberFormat MILLS = NumberFormat.getCurrencyInstance();

    public static final String NA = "N/A";

    public static final String specialCharStr = ",;&*~-=|!$\\?.()";


    static {
        try {
            DB_DATE_NA = new SimpleDateFormat("MM/dd/yyyy").parse(DB_DATE_NA_STR);
            DB_DATE_TIME_NA = DB_DATE_NA.getTime();
        }
        catch (ParseException pe) {
            pe.printStackTrace();
        }
        CURRENCY.setMaximumFractionDigits(0);
        MILLS.setMaximumFractionDigits(1);
    }

    /**
     * Returns true if the string is of zero length, null or
     * only contains white spaces, false otherwise
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        if (string == null || string.trim().length() == 0)
            return true;
        else
            return false;
    }

    /**
     * Returns a typasfe collection, created from a generic collection.
     *
     * @param <C>
     * @param genericCollection
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <C extends Object> List<C> typesafeList(Collection collection, Class<C> clazz) {
        List<C> typesafeList = Collections.checkedList(new ArrayList<C>(), clazz);
        for (Object obj : collection) {
            typesafeList.add((C) obj);
        }
        return typesafeList;
    }

    /**
     * Returns a type safe value for the type specified by dataType and using any
     * numeric value supplied.
     *
     * @param <C>
     * @param dataType
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <C extends Number> C typesafeValue(Class<C> dataType, Number value) {
        if (value == null)
            return null;
        if (dataType == Long.class) {
            return (C) new Long(value.longValue());
        } else if (dataType == Double.class) {
            return (C) new Double(value.doubleValue());
        } else if (dataType == Integer.class) {
            return (C) new Integer(value.intValue());
        } else if (dataType == Float.class) {
            return (C) new Float(value.floatValue());
        } else if (dataType == Short.class) {
            return (C) new Short(value.shortValue());
        } else {
            throw new IllegalArgumentException("Type " + dataType.getName() + " cannot be handled.");
        }
    }

    public static List<String> convertToListOfString(Set<Long> objList) {
        List<String> stringList = null;
        if (isNull(objList) || objList.size() == 0) {
            return stringList;
        }

        stringList = new ArrayList<String>();

        for (Long val : objList) {
            if (!isNull(val)) {
                stringList.add(String.valueOf(val));
            }
        }
        return stringList;
    }

    public static boolean isZero(Long val) {
        if (isNull(val)) return true;
        if (val.longValue() == 0) return true;
        return false;
    }

    public static boolean isZero(Double val) {
        if (isNull(val)) return true;
        if (val.doubleValue() == 0.0 || val.doubleValue() == 0 || val.doubleValue() < 0) return true;
        return false;
    }


    public static boolean isNumber(String val) {
        if (isEmpty(val)) return true;
        Pattern pattern = Pattern.compile("^[+]?\\d*$");
        Matcher matcher = pattern.matcher(val);
        if (!matcher.matches()) return true;
        return false;
    }

    public static boolean isDate(String val) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            if (val.trim().length() != df.toPattern().length()) return true;
            Date tDate = df.parse(val);
            if (!df.format(tDate).equals(val)) {
                return true;
            }
            return false;
        }
        catch (ParseException e) {
            return true;
        }


    }

    /**
     * For checking param is numeric or not.
     *
     * @param val
     * @return
     */
    public static boolean isNumeric(String val) {
        String expression = "[-+]?[0-9]*\\.?[0-9]+$";
        CharSequence inputStr = val;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isNA_NM(double val) {
        return val == DB_NA || val == DB_NM;
    }

    public static boolean isNAM(double val) {
        return val == DB_NAM;
    }

    public static boolean isNA_NM(Double val) {
        if (val == null) return true;
        return isNA_NM(val.doubleValue());
    }

    public static boolean isNA_NM(Long val) {
        if (val == null) return true;
        return isNA_NM(val.doubleValue());
    }

    public static boolean isNA_NM(Integer val) {
        if (val == null) return true;
        return isNA_NM(val.doubleValue());
    }

    public static boolean isNA(Double val) {
        return isNA_NM(val);
    }

    public static boolean isNA(Long val) {
        if (val == null) return true;
        return isNA_NM(val);
    }

    public static Double convertNaNToNA(Double d) {
        if (d == null || d.isNaN()) {
            return new Double(DB_NA);
        } else {
            return d;
        }
    }

    public static boolean isNA(Date date) {
        if (date == null) return true;

        long time = date.getTime();
        return (time == DB_DATE_TIME_NA);
    }

    /**
     * Formats the date as a fiscal year end (MM/yy).
     */
    public static String formatFY(Date fyEnd) {
        if (isNA(fyEnd)) {
            return "N/A";
        }
        return FY_PRINT.format(fyEnd);
    }

    /**
     * Formats the date as MM/dd/yy.
     */
    public static String format(Date date) {
        if (isNA(date)) {
            return "N/A";
        }
        return DATE.format(date);
    }

    /**
     * Formats the date as MM/dd/yyyy.
     */
    public static String formatMMDDYYYY(Date date) {
        if (isNA(date)) {
            return "N/A";
        }
        return DATEMMDDYYYY.format(date);
    }

    /**
     * Formats the date as MMMM yyyy. (for ex: September 2006)
     */
    public static String formatMMMMyyyy(Date date) {
        if (isNA(date)) {
            return "N/A";
        }
        return DATEMMMMyyyy.format(date);
    }

    /**
     * Formats the date as MMDDYY.
     */
    public static String formatMMDDYY(Date date) {
        if (isNA(date)) {
            return "N/A";
        }
        return DATEMMDDYY.format(date);
    }

    public static boolean isNA_NM(String val) {
        if (val == null) {
            return false;
        }
        return val.equals("N/A");
    }

    public static boolean isNAORNULL(String val) {
        if (val == null) {
            return true;
        }
        return val.equals("N/A");
    }

    public static int dateToYear(Date date) {
        int dateYear = 0;

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        dateYear = calendar.get(Calendar.YEAR);

        if (dateYear == 1)
            dateYear = DB_NA;

        return dateYear;
    }

    /**
     * Returns true if the Object is null, otherwise false
     *
     * @param object - whose value has to be verified
     * @return boolean - true if the Object is null, otherwise false
     */
    public static boolean isNull(Object object) {
        return (null == object);
    }

    public static AmbassadorProxy getProxyObject(GsCreditials gsCreditials) {

        AmbassadorProxy proxy = new AmbassadorProxy(gsCreditials.getUrl(), gsCreditials.getUserName(), gsCreditials.getPassword());
        return proxy;
    }

    public static String getAccessToke(AmbassadorProxy proxy, GsCreditials gsCreditials) {
        String accesstoken = null;
        try {
            accesstoken = proxy.login(gsCreditials.getUserName(), gsCreditials.getPassword());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return accesstoken;
    }

    public static void saveFileFromUrlWithJavaIO(String fileName, String fileUrl) throws IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            if (fileUrl != null && fileUrl.contains(" ")) {
                fileUrl = fileUrl.replaceAll(" ", "%20");
            }
            in = new BufferedInputStream(new URL(fileUrl).openStream());
            fout = new FileOutputStream(fileName);

            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }
    }

    public static File unzip(File inFile, File outFolder) {
        final int BUFFER = 2048;
        try {
            BufferedOutputStream out = null;
            ZipInputStream in = new ZipInputStream(
                    new BufferedInputStream(
                            new FileInputStream(inFile)));
            ZipEntry entry;

            File entrySupposedPath = null;
            while ((entry = in.getNextEntry()) != null) {
                System.out.println("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];

                //We will try to reconstruct the entry directories
                entrySupposedPath = new File(outFolder.getAbsolutePath() + File.separator + entry.getName());
                String name = entry.getName();

                name.substring(0, name.indexOf("/"));
                //   System.out.println("name"+File.separator+entry.getName().indexOf("\\"));
                //Does the parent folder exist?
                if (!entrySupposedPath.getParentFile().exists()) {
                    entrySupposedPath.getParentFile().mkdirs();
                }


                // write the files to the disk
                out = new BufferedOutputStream(
                        new FileOutputStream(outFolder.getPath() + "/" + entry.getName()), BUFFER);
                while ((count = in.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                out.flush();
                out.close();
            }
            in.close();
            return entrySupposedPath;
        }
        catch (Exception e) {
            e.printStackTrace();
            return inFile;
        }
    }

    /**
     * Used to Delete a file
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (file.delete()) {
                logger.info(file.getName() + " is deleted!");
            } else {
                logger.info("Delete operation is failed for file" + file.getName());
            }
        }

    }

    public static void logMemoryUsage() {

        Runtime runtime = Runtime.getRuntime();
        System.gc();

        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        long usedMemory = totalMemory - freeMemory;

        double ratio = ((double) usedMemory / (double) maxMemory) * 100;

        logger.info("Jboss-Mem: " + ratio + "% used");

    }
    
    public static <C extends Object> List<List<C>> createBatches(
            Collection<C> collection, int batchSize) {
        if (batchSize < 1)
            throw new IllegalArgumentException("Batch size cannot be zero or less");

        List<C> objects = new ArrayList<C>(collection);
        int noOfBatches = (int) Math.ceil((double) objects.size() / batchSize);
        List<List<C>> batches = new ArrayList<List<C>>(noOfBatches);
        int index = 0;
        for (int i = 0; i < noOfBatches; i++) {
            List<C> list = new ArrayList<C>(batchSize);
            for (int j = 0; j < batchSize && index < objects.size(); j++, index++) {
                list.add(objects.get(index));
            }
            batches.add(list);
        }
        return batches;
    }
    
    
    public static <C extends Object> String generateInQuery(
			Collection<C> values, 
			String fieldName) {
		return generateInQuery(values, fieldName, false);
	}
	
	public static <C extends Object> String generateInQuery(
			Collection<C> values, 
			String fieldName,
			boolean withQuotes) {
		if (values == null || values.size() == 0) return "";
		
		String query = "";
		List<List<C>> batches = createBatches(values, 800);
		for (int i = 0; i < batches.size(); i++) {
			List<C> batch = batches.get(i);
			StringBuilder inQuery = new StringBuilder(fieldName + " IN (");
			for (int j = 0; j < batch.size(); j++) {
				C value = batch.get(j);
				
				if (value.toString().trim().length() == 0) continue;
				
				inQuery.append(withQuotes 
						? "'" + value.toString().trim() + "'" 
						: value.toString());
				if (j + 1 == batch.size())
					inQuery.append(")");
				else
					inQuery.append(", ");
			}
			query += "(" + inQuery.toString() + ")";
			if (i + 1 != batches.size())
				query += " OR ";
		}
		return "(" + query + ")";
	}
    
}
