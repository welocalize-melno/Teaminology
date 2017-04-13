package com.teaminology.hp.bo;

import java.util.Date;

import com.teaminology.hp.data.TeaminologyObject;

/**
 * A Persistence class used to describe the menus available for a given role.
 *
 * @author sayeedm
 */
public class RoleMenuMgmt implements TeaminologyObject
{
    private Integer roleMenuMgmtId;
    private Role role;
    private Menu userMenu;
    private Date createDate;
    private Integer createdBy;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;


    public Integer getRoleMenuMgmtId() {
        return roleMenuMgmtId;
    }

    public void setRoleMenuMgmtId(Integer roleMenuMgmtId) {
        this.roleMenuMgmtId = roleMenuMgmtId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Menu getUserMenu() {
        return userMenu;
    }

    public void setUserMenu(Menu userMenu) {
        this.userMenu = userMenu;
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

}
