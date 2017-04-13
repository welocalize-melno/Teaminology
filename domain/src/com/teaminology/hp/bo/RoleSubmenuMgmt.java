package com.teaminology.hp.bo;

import java.util.Date;

import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class used to describe submenus available for given role.
 *
 * @author sayeedm
 */
public class RoleSubmenuMgmt implements TeaminologyObject
{
    private Integer roleSubmenuMgmtId;
    private SubMenu subMenu;
    private Role role;
    private Date createDate;
    private Integer createdBy;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;

    public SubMenu getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(SubMenu subMenu) {
        this.subMenu = subMenu;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Integer getRoleSubmenuMgmtId() {
        return roleSubmenuMgmtId;
    }

    public void setRoleSubmenuMgmtId(Integer roleSubmenuMgmtId) {
        this.roleSubmenuMgmtId = roleSubmenuMgmtId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
