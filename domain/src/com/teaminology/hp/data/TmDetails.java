package com.teaminology.hp.data;

/**
 * POJO class containing the customised instance variables of TM Information.
 */
public class TmDetails
{
    private String topSuggestion;
    private Integer tmProfileInfoId;
    private Integer productId;
    private Integer domainId;
    private Integer contentTypeId;

    /**
     * To get tmProfileInfoId
     *
     * @return tmProfileInfoId
     */
    public Integer getTmProfileInfoId() {
        return tmProfileInfoId;
    }

    /**
     * To set tmProfileInfoId
     *
     * @param tmProfileInfoId
     */
    public void setTmProfileInfoId(Integer tmProfileInfoId) {
        this.tmProfileInfoId = tmProfileInfoId;
    }

    /**
     * To get productId
     *
     * @return productId
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * To set productId
     *
     * @param productId
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * To get domainId
     *
     * @return domainId
     */
    public Integer getDomainId() {
        return domainId;
    }

    /**
     * To set domainId
     *
     * @param domainId
     */
    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    /**
     * To get contentTypeId
     *
     * @return contentTypeId
     */
    public Integer getContentTypeId() {
        return contentTypeId;
    }

    /**
     * To set contentTypeId
     *
     * @param contentTypeId
     */
    public void setContentTypeId(Integer contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    /**
     * To get topSuggestion
     *
     * @return topSuggestion
     */
    public String getTopSuggestion() {
        return topSuggestion;
    }

    /**
     * To set topSuggestion
     *
     * @param topSuggestion
     */
    public void setTopSuggestion(String topSuggestion) {
        this.topSuggestion = topSuggestion;
    }

}
