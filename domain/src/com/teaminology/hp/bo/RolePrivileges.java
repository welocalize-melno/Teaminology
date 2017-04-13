package com.teaminology.hp.bo;

import java.util.Date;

/**
 * A Persistence class which is used to describe privileges available for given role.
 *
 * @author sayeedm
 */
public class RolePrivileges
{
    private Integer rolePrivilegesId;
    private Role role;
    private Privileges privileges;
    private Date createDate;
    private Integer createdBy;
    private Integer updatedBy;
    private Date updateDate;
    private String isActive;


    public Integer getRolePrivilegesId() {
        return rolePrivilegesId;
    }

    public void setRolePrivilegesId(Integer rolePrivilegesId) {
        this.rolePrivilegesId = rolePrivilegesId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Privileges getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Privileges privileges) {
        this.privileges = privileges;
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
