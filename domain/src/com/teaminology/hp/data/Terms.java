package com.teaminology.hp.data;


/**
 * A POJO class containing the instance variables of Term Glossary/Debated Terms Chart data.
 *
 * @author sarvanic
 */
public class Terms implements TeaminologyObject
{

    private String year;
    private String Interval;
    private Integer termsPerInterval;

    /**
     * To get month name
     *
     * @return month name
     */
    public String getInterval() {
        return Interval;
    }

    /**
     * To set month name
     *
     * @param interval month name
     */
    public void setInterval(String interval) {
        Interval = interval;
    }

    /**
     * To get no.of terms per month
     *
     * @return no.of terms per month
     */
    public Integer getTermsPerInterval() {
        return termsPerInterval;
    }

    /**
     * To set no.of terms per month
     *
     * @param termsPerInterval no.of terms per month
     */
    public void setTermsPerInterval(Integer termsPerInterval) {
        this.termsPerInterval = termsPerInterval;
    }

    /**
     * To get year
     *
     * @return year
     */
    public String getYear() {
        return year;
    }

    /**
     * To set year
     *
     * @param year year
     */
    public void setYear(String year) {
        this.year = year;
    }

}
