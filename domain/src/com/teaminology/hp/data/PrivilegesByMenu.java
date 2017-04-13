package com.teaminology.hp.data;

import java.util.List;

/**
 * @author sayeedm
 */
public class PrivilegesByMenu implements TeaminologyObject
{

    String menu;
    List<PrivilegesBySubMenu> privilegesBySubMenu;
    List<PrivilegeTo> menuPrivileges;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public List<PrivilegesBySubMenu> getPrivilegesBySubMenu() {
        return privilegesBySubMenu;
    }

    public void setPrivilegesBySubMenu(List<PrivilegesBySubMenu> privilegesBySubMenu) {
        this.privilegesBySubMenu = privilegesBySubMenu;
    }

    public List<PrivilegeTo> getMenuPrivileges() {
        return menuPrivileges;
    }

    public void setMenuPrivileges(List<PrivilegeTo> menuPrivileges) {
        this.menuPrivileges = menuPrivileges;
    }


}
