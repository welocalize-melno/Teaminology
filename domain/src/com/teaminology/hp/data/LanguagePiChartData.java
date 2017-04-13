package com.teaminology.hp.data;


/**
 * Contains the instance variables of Pie chart for reports.
 *
 * @author sarvanic
 */

public class LanguagePiChartData implements TeaminologyObject
{

    private String language;
    private Integer noOfTerms;

    /**
     * To get language name
     *
     * @return language name
     */
    public String getLanguage() {
        return language;
    }

    /**
     * To set the language name
     *
     * @param language language name
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * To get  no.of terms of given language
     *
     * @return No.of  terms of given language
     */
    public Integer getNoOfTerms() {
        return noOfTerms;
    }

    /**
     * To set the no.of terms of given language
     *
     * @param noOfTerms No.of terms of given language
     */
    public void setNoOfTerms(Integer noOfTerms) {
        this.noOfTerms = noOfTerms;
    }


}
