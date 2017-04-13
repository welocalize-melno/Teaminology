package com.teaminology.hp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


/**
 * Contains the String validations and encrypt password functionalities.
 *
 * @author sarvanic
 */
public class Utils
{
	private static Logger logger = Logger.getLogger(Utils.class); 

    /**
     * Contains sorting order values required for objects to sort
     *
     * @author sarvanic
     */
    public enum SortOrder
    {
        NONE,
        ASC,
        DESC
    }

    private static final NumberFormat NUMBER = NumberFormat.getIntegerInstance();
    public static final int DB_NA = -999999999;
    public static final int DB_NM = -888888888;
    private static final SimpleDateFormat DATEMMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Returns true if the string is of zero length, null or
     * only contains white spaces, false otherwise
     *
     * @param string String value that has to be verified
     * @return boolean true if the string is empty, otherwise false
     */
    public static boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }

    /**
     * Returns true if the Object is null, otherwise false
     *
     * @param object Whose value has to be verified
     * @return boolean true if the Object is null, otherwise false
     */
    public static boolean isNull(Object object) {
        if (object == null)
            return true;

        if (object instanceof List) {
            List listObject = (List) object;
            if (listObject.size() == 0)
                return true;
        }

        if (object instanceof Set) {
            Set setObject = (Set) object;
            if (setObject.size() == 0)
                return true;
        }

        return false;
    }

    /**
     * Returns encrypted password
     *
     * @param plainPassword string that has to be encrypted
     * @return string password that is encrypted
     */
    public static String encryptPassword(String plainPassword) {
        try {
            return byteArrayToHexString(computeHash(plainPassword));
        }
        catch (Exception e) {
            logger.error("Error found in encryptPassword().", e);
        }
        return null;
    }


    /**
     * Returns Array of bytes for a given string, method used for password encryption
     *
     * @param x String that has to be converted into bytes
     * @return byte[] Array of bytes
     */
    private static byte[] computeHash(String x) throws Exception {
        java.security.MessageDigest d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return d.digest();
    }


    /**
     * Returns a hexadecimal String for a given byte array, method used for encryption
     *
     * @param b bytes[] which has to be converted into hexadecimal String
     * @return String Hexadecimal string
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * To compare two Integers
     *
     * @param i1        Integer to compare
     * @param i2        Integer to compare
     * @param sortOrder Order in which they need to be sorted
     * @return Integer holding -1 if i1 < i2 else +1
     */
    public static int compareIntegers(Integer i1, Integer i2, SortOrder sortOrder) {
        return compareIntegers(i1, i2, sortOrder, false);
    }

    /**
     * To compare two Floats
     *
     * @param f1
     * @param f2
     * @param sortOrder Order in which they need to be sorted
     * @return Integer holding -1 if i1 < i2 else +1
     */
    public static int compareFloats(Float f1, Float f2, SortOrder sortOrder) {
        return compareFloats(f1, f2, sortOrder, false);
    }

    /**
     * To compare two Integers
     *
     * @param i1           Integer to compare
     * @param i2           Integer to compare
     * @param sortOrder    Order in which they need to be sorted
     * @param nonNullFirst Boolean holding false if the null values are not considered else true
     * @return Integer holding -1 if i1 < i2 else +1
     */
    public static int compareIntegers(Integer i1, Integer i2, SortOrder sortOrder, boolean nonNullFirst) {
        if (sortOrder == SortOrder.NONE) return 0;

        if (i1 == null && i2 == null) return 0;

        if (i1 == null || i2 == null) {
            return compareNulls(i1, i2, sortOrder, nonNullFirst);
        }

        return sortOrder == SortOrder.ASC ? i1.compareTo(i2) : i2.compareTo(i1);
    }

    /**
     * To compare two string objects
     *
     * @param o1           String to compare
     * @param o2           String to compare
     * @param sortOrder    Order in which they need to be sorted
     * @param nonNullFirst Boolean holding false if the null values are not considered else true
     * @return Integer holding -1 if i1 < i2 else +1
     */
    private static int compareNulls(Object o1, Object o2, SortOrder sortOrder, boolean nonNullFirst) {
        if (o1 == null || o2 == null) {
            if (sortOrder == SortOrder.ASC) {
                if (o1 == null) return nonNullFirst ? 1 : -1;
                return nonNullFirst ? -1 : 1;
            } else {
                if (o1 == null) return 1;
                return -1;
            }
        } else {
            throw new IllegalArgumentException("Both arguments are not null.");
        }
    }

    /**
     * To compare two Float objects
     *
     * @param f1           Float to compare
     * @param f2           Float to compare
     * @param sortOrder    Order in which they need to be sorted
     * @param nonNullFirst Boolean holding false if the null values are not considered else true
     * @return Integer holding -1 if i1 < i2 else +1
     */
    public static int compareFloats(Float f1, Float f2, SortOrder sortOrder, boolean nonNullFirst) {
        if (sortOrder == SortOrder.NONE) return 0;

        if (f1 == null && f2 == null) return 0;

        if (f1 == null || f2 == null) {
            return compareNulls(f1, f2, sortOrder, nonNullFirst);
        }

        return sortOrder == SortOrder.ASC ? f1.compareTo(f2) : f2.compareTo(f1);
    }

    /**
     * Formats the specified number with commas. Returns "N/A" if either N/A
     * or N/M is passed in.
     */
    public static String format(int amt) {
        return isNA_NM(amt) ? "N/A" : NUMBER.format(amt);
    }

    public static boolean isNA_NM(double val) {
        return val == DB_NA || val == DB_NM;
    }

    public static String createUUID() {
        return UUID.randomUUID().toString();
    }

    public static int compareStrings(String s1, String s2, SortOrder sortOrder) {
        if (sortOrder == SortOrder.NONE) return 0;

        return compareStrings(s1, s2, sortOrder, false);
    }

    /**
     * To compare two dates
     *
     * @param s1        String to compare
     * @param s2        String to compare
     * @param sortOrder Order in which they need to be sorted
     * @return Integer holding -1 if s1 < s2 else +1
     */
    public static int compareDates(String s1, String s2, SortOrder sortOrder) {
        if (sortOrder == SortOrder.NONE) return 0;

        try {
            if (s1 == null && s2 == null) return 0;
            if (s1 == null || s2 == null) {
                return compareNulls(s1, s2, sortOrder, false);
            }
            Date d1 = DATEMMDDYYYY.parse(s1);
            Date d2 = DATEMMDDYYYY.parse(s2);

            return sortOrder == SortOrder.ASC ? d1.compareTo(d2) : d2.compareTo(d1);
        }
        catch (ParseException e) {
            logger.error("Error found in compareDates().", e);
        }
        return 0;
    }

    /**
     * To compare two Strings
     *
     * @param s1           String to compare
     * @param s2           String to compare
     * @param sortOrder    Order in which they need to be sorted
     * @param nonNullFirst Boolean holding false if the null values are not considered else true
     * @return Integer holding -1 if s1 < s2 else +1
     */
    public static int compareStrings(String s1, String s2, SortOrder sortOrder, boolean nonNullFirst) {
        if (sortOrder == SortOrder.NONE) return 0;
        if (s1 == null && s2 == null) return 0;

        if (s1 == null || s2 == null) {
            return compareNulls(s1, s2, sortOrder, nonNullFirst);
        }

        return sortOrder == SortOrder.ASC ? s1.compareTo(s2) : s2.compareTo(s1);
    }

    /**
     * To compare two case insensitive String
     *
     * @param s1        String to compare
     * @param s2        String to compare
     * @param sortOrder Order in which they need to be sorted
     * @return Integer holding -1 if s1 < s2 else +1
     */
    public static int compareCaseInsensitiveStrings(String s1, String s2, SortOrder sortOrder) {
        return compareStrings(s1.toLowerCase(), s2.toLowerCase(), sortOrder, false);
    }

    /**
     * To compare two case insensitive Strings
     *
     * @param s1           String to compare
     * @param s2           String to compare
     * @param sortOrder    Order in which they need to be sorted
     * @param nonNullFirst Boolean holding false if the null values are not considered else true
     * @return Integer holding -1 if s1 < s2 else +1
     */
    public static int compareCaseInsensitiveStrings(String s1, String s2, SortOrder sortOrder, boolean nonNullFirst) {
        if (sortOrder == SortOrder.NONE) return 0;

        if (s1 == null && s2 == null) return 0;

        if (s1 == null || s2 == null) {
            return compareNulls(s1, s2, sortOrder, nonNullFirst);
        }

        return sortOrder == SortOrder.ASC ? s1.compareTo(s2) : s2.compareTo(s1);
    }

    /**
     * To get comma seperated values
     *
     * @param companyId An Integer set to be seperated
     * @return String contains comma seperated values
     */
    public static String getCommaSeparatedValues(Set<Integer> companyIds) {
        String result = null;
        if (companyIds != null) {
            List<Integer> companyIdsList = new ArrayList<Integer>(companyIds);
            for (Integer i = 0; i < companyIdsList.size(); i++) {
                if (i == 0) {
                    result = companyIdsList.get(i).toString();
                } else {
                    result = result + ", " + companyIdsList.get(i);
                }
            }
        }
        return result;
    }

    /**
     * To log memory usage
     */
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
    
    // new code added 
    
    public static PropertyResourceBundle getPropertyResourceBundle(String filePath){
        PropertyResourceBundle propertyResourceBundle = null;
        InputStream inputStreamObj = null;
        if(Utils.isEmpty(filePath)){
        	return null;
        }
        
        try{         
        	inputStreamObj = getInputStream(filePath);
        	propertyResourceBundle	=new PropertyResourceBundle(inputStreamObj);
        }
        catch(Exception e){
        	 logger.error("Error in resourcePropertyResourceBundle() method to get the PropertyResourceBundle for inputfile: "+filePath,e);
	    }
        finally{
        	try{
        		inputStreamObj.close();
        	}catch(Exception e){
        		 logger.error("Error in closing inputstream in resourcePropertyResourceBundle() method to get the PropertyResourceBundle for inputfile: "+filePath,e);
        	}
        }
        return propertyResourceBundle;
    }
    
    
    public static InputStream getInputStream(String filePath) throws Exception{
    	if(Utils.isEmpty(filePath)){
    		return null;
    	}

    	InputStream inputStream = new FileInputStream(filePath);
    	return inputStream;    		
    }

	public static String getIpAddress(HttpServletRequest request){
		
	      // get client IP address
		   String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		   if (ipAddress == null) {  
			   ipAddress = request.getRemoteAddr();  
		   }
		return ipAddress;
	}
	
}
