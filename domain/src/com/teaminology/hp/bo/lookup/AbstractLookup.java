/**
 *
 */
package com.teaminology.hp.bo.lookup;

/**
 * @author sayeed
 */
public abstract class AbstractLookup implements ILookup
{

    private static final long serialVersionUID = 1L;
    private Integer lookupId;
    private String description;
    private Integer sortOrder;
    private Boolean isActive;

    public AbstractLookup() {
    }

    public AbstractLookup(Integer lookupId) {
        setLookupId(lookupId);
    }

    public Integer getLookupId() {
        return lookupId;
    }

    public void setLookupId(Integer lookupId) {
        this.lookupId = lookupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    private Boolean getIsActive() {
        return isActive;
    }

    public boolean isActive() {
        return getIsActive() == null ? false : getIsActive().booleanValue();
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((lookupId == null) ? 0 : lookupId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        AbstractLookup that = null;
        try {
            that = (AbstractLookup) obj;
        }
        catch (Exception e) {
            return false;
        }

        if (this.getLookupId() == null || that.getLookupId() == null)
            return false;
        return (this.getLookupId().equals(that.getLookupId()));
    }

}
