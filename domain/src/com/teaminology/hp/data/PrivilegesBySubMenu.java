package com.teaminology.hp.data;

import java.util.List;

public class PrivilegesBySubMenu implements TeaminologyObject
{

    String subMenu;
    List<PrivilegeTo> subMenuPrivileges;

    public String getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(String subMenu) {
        this.subMenu = subMenu;
    }

    public List<PrivilegeTo> getSubMenuPrivileges() {
        return subMenuPrivileges;
    }

    public void setSubMenuPrivileges(List<PrivilegeTo> subMenuPrivileges) {
        this.subMenuPrivileges = subMenuPrivileges;
    }


}
