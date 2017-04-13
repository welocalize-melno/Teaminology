package com.teaminology.hp.data;

import java.util.List;

import com.teaminology.hp.bo.lookup.Language;

/**
 * POJO class containing the instance variables of language report chart.
 *
 * @author sarvanic
 */
public class LanguageReportData implements TeaminologyObject
{

    private Language language;
    private List<Terms> termsList;

    /**
     * To get language object
     *
     * @return language object
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * To set language object
     *
     * @param language language object
     */

    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * To get list of terms
     *
     * @return list of terms
     */
    public List<Terms> getTermsList() {
        return termsList;
    }

    /**
     * To set list of terms
     *
     * @param termsList list of terms
     */
    public void setTermsList(List<Terms> termsList) {
        this.termsList = termsList;
    }


}
