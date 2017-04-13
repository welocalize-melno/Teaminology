package com.teaminology.hp.bo;

import java.util.Set;


/**
 * @author sayeedm
 */
public class Tag
{

    private Integer tagId;
    private String tagName;
    private Set<Attributes> attribute;
    private GlobalsightTermInfo globalsightTermInfo;
    private String tagValue;
    private String isSource;
    private Integer sortOrder;

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Set<Attributes> getAttribute() {
        return attribute;
    }

    public void setAttribute(Set<Attributes> attribute) {
        this.attribute = attribute;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getIsSource() {
        return isSource;
    }

    public void setIsSource(String isSource) {
        this.isSource = isSource;
    }

    public GlobalsightTermInfo getGlobalsightTermInfo() {
        return globalsightTermInfo;
    }

    public void setGlobalsightTermInfo(GlobalsightTermInfo globalsightTermInfo) {
        this.globalsightTermInfo = globalsightTermInfo;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }


}
