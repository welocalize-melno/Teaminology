package com.teaminology.hp.service.impl;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.Utils;
import com.teaminology.hp.Utils.SortOrder;
import com.teaminology.hp.bo.CompanyTransMgmt;
import com.teaminology.hp.bo.Menu;
import com.teaminology.hp.bo.Privileges;
import com.teaminology.hp.bo.Role;
import com.teaminology.hp.bo.RoleMenuMgmt;
import com.teaminology.hp.bo.RolePrivileges;
import com.teaminology.hp.bo.RoleSubmenuMgmt;
import com.teaminology.hp.bo.SubMenu;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserConfigFields;
import com.teaminology.hp.bo.UserLanguages;
import com.teaminology.hp.bo.UserRole;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.dao.ILookUpDAO;
import com.teaminology.hp.dao.IUserDAO;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.PrivilegeTo;
import com.teaminology.hp.data.PrivilegesByMenu;
import com.teaminology.hp.data.PrivilegesBySubMenu;
import com.teaminology.hp.data.UsersChartData;
import com.teaminology.hp.service.IUserService;
import com.teaminology.hp.service.enums.CompanyUserEnum;
import com.teaminology.hp.service.enums.RoleEnum;
import com.teaminology.hp.service.enums.TeaminologyProperty;


/**
 * Conatins methods which deals with Users.
 *
 * @author sarvanic
 */

@Service
public class UserServiceImpl implements IUserService
{
    //	@SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(UserServiceImpl.class);
    private IUserDAO userDAO;
    private ILookUpDAO lookUpDAO;

    @Autowired
    public UserServiceImpl(IUserDAO userDAO, ILookUpDAO lookUpDAO) {
        this.userDAO = userDAO;
        this.lookUpDAO = lookUpDAO;
    }

    @Transactional(readOnly = true)
    @Qualifier("txManager")
    /**
     *
     *To register user
     *
     * @param user User to be registered
     * @return Returns "success" if it is registered else it returns "failure"
     */
    public String registerUser(User user) {
        String status = null;
        if (user == null) {
            status = "failure";
            throw new IllegalArgumentException("Invalid user");
        }
        user.setCreateTime(new Date());
        logger.info("Encrypting password");
        String password = Utils.encryptPassword(user.getPassword());
        user.setPassword(password);

        try {

            status = userDAO.registerUser(user);
            logger.info("Registered user");

        }
        catch (Exception e) {
            logger.error("Error in registring user" + e);
        }

        return status;

    }

    /**
     * To get user details
     *
     * @param userId An integer for which data is to be retrieved
     * @return Member which holds the user details
     */
    @Override
    public Member userDetails(Integer userId) {
        if (userId == 0) {
            throw new IllegalArgumentException("Invalid termInformation");
        }

        Member member = new Member();
        try {
            member = userDAO.userDetails(userId);
            if (member != null) {
                if (member.getPhotoPath() == null) {
                    member.setPhotoPath(TeaminologyProperty.PHOTO_NOT_AVAILABLE.getValue());
                }
                logger.debug("Got user details for user Id: " + userId);
            } else {
                logger.debug("Got empty user Details for user Id : " + userId);
            }
        }
        catch (Exception e) {
            logger.error("Error in  getting  userDetails");
            e.printStackTrace();
        }
        return member;

    }


    /**
     * To get users Per month
     *
     * @return List which holds the count of users per month
     */
    @Override
    public List<UsersChartData> usersPerMonth(Integer companyId) {
        List<UsersChartData> usersData = new ArrayList<UsersChartData>();
        try {
            usersData = userDAO.usersPerMonth(companyId);
            if ((usersData != null) && (usersData.size() > 0)) {
                logger.debug("Got users per month data ");
            } else {
                logger.debug("Got empty users per month data ");
            }
        }
        catch (Exception e) {
            logger.error("Error in getting users  per month ");
            return null;
        }
        return usersData;

    }

    /**
     * To get Total Active users count
     *
     * @return Integer which holds the count of active users
     */
    @Override
    public Integer getActiveUsersCount(Integer companyId) {
        int usersCount = 0;
        try {

            usersCount = userDAO.getActiveUsersCount(companyId);
            if (usersCount > 0) {
                logger.debug("Got  active users count: " + usersCount);
            } else {
                logger.debug("Got empty  active users count");
            }
        }
        catch (Exception e) {
            logger.error("Error in getting  active users count");
            return 0;
        }

        return usersCount;
    }

    /**
     * To get HP community members
     *
     * @return List of Users registered in Community and Business Member roles
     */
    @Override
    public List<Member> getHPMembers() {
        List<Member> hpMembers = new ArrayList<Member>();
        List<Member> hpMembersList = new ArrayList<Member>();
        try {

            hpMembers = userDAO.getHPMembers();

            if ((hpMembers != null) && (hpMembers.size() > 0)) {
                for (Member member : hpMembers) {
                    if (member.getPhotoPath() == null) {
                        member.setPhotoPath(TeaminologyProperty.PHOTO_NOT_AVAILABLE.getValue());
                    }
                    hpMembersList.add(member);
                }
                logger.debug("Got HP Members : " + hpMembers.size());
            } else {
                logger.debug("Got empty HP Members");
            }

        }
        catch (Exception e) {
            logger.error("Error in getting   HP Members ");
        }

        return hpMembersList;

    }

    /**
     * To get list of user type details
     *
     * @return List of users role look up
     */
    @Override
    public List<Role> getUserTypeDetails() {
        List<Role> userTypeDetails = new ArrayList<Role>();
        try {

            userTypeDetails = userDAO.getUserTypeDetails();
            if ((userTypeDetails != null) && (userTypeDetails.size() > 0)) {
                logger.debug("Got userType details : "
                        + userTypeDetails.size());
            } else {
                logger.debug("Got empty  userType details");
            }

        }
        catch (Exception e) {
            logger.error("Error in geeting  userType details");
        }

        return userTypeDetails;

    }

    /**
     * To get user register languages
     *
     * @param userId Integer of the user to get the registered languages
     * @return List of languages in which user is registered
     */
    @Override
    public List<Language> getUserRegLanguages(Integer userId) {
        List<Language> userLanguageTerms = new ArrayList<Language>();
        try {

            userLanguageTerms = userDAO.getUserRegLanguages(userId);
            if ((userLanguageTerms != null) && (userLanguageTerms.size() > 0)) {
                logger.debug("Got users register languages : "
                        + userLanguageTerms.size());
            } else {
                logger.debug("Got empty users register languages");
            }

        }
        catch (Exception e) {
            logger.error("Error in getting  user register languages ");
        }

        return userLanguageTerms;

    }

    /**
     * To get total users language terms
     *
     * @param languageId Integer to filter users
     * @param userId     Integer of the user to get the language terms
     * @return An integer which holds the count of terms w.r.t the language
     */
    @Override
    public Integer getTotalUsersLangTerms(final String languageId,
                                          final Integer userId) {
        int totalUsersLangTerms = 0;
        try {

            totalUsersLangTerms = userDAO.getTotalUsersLangTerms(languageId,
                    userId);
            if (totalUsersLangTerms > 0) {
                logger.debug("Got Total users  language terms : "
                        + totalUsersLangTerms);
            } else {
                logger.debug("Got empty language terms ");
            }
        }
        catch (Exception e) {
            logger.error("Error in getting  total users language terms");
            return 0;
        }

        return totalUsersLangTerms;
    }

    /**
     * To get user accuracy rated values
     *
     * @param userId    An Integer to filter  users
     * @param statusId  An Integer to filter users
     * @param companyId An Integer to filter users
     * @return A collection which contains the finalised voted terms count and total voted terms count
     */
    @Override
    public Map<String, BigInteger> getUserAccuracyRate(Integer userId, Integer statusId, Integer companyId) {
        Map<String, BigInteger> userAccuracyRate = new HashMap<String, BigInteger>();
        try {

            userAccuracyRate = userDAO.getUserAccuracyRate(userId, statusId, companyId);
            if ((userAccuracyRate != null) && (userAccuracyRate.size() > 0)) {
                logger.debug("Got user accuracy rate : "
                        + userAccuracyRate.size());
            } else {
                logger.debug("Got empty user accuracy rate");
            }

        }
        catch (Exception e) {
            logger.error("Error in getting user accuracy rate");
            e.printStackTrace();
        }

        return userAccuracyRate;

    }

    /**
     * To verify whether user exists or not in database
     *
     * @return Returns true if user exists else it returns false
     */
    @Override
    public boolean ifUserExists(String userName) {
        boolean userExists = false;
        try {
            userExists = userDAO.ifUserExists(userName);
            logger.debug("User is not exits");

        }
        catch (Exception e) {
            logger.error("Error in verifying the user is exists or not");
            e.printStackTrace();
            userExists = false;
        }
        return userExists;
    }

    /**
     * To get invite user details
     *
     * @param filterBy String to be filtered by role/language
     * @param intIds   Array of integer role type/ language id's
     * @return List of Users to be invited
     */
    @Override
    public List<User> inviteUser(String filterBy, Integer intIds[], Integer companyId, Set<Integer> roleIds) {
        List<User> inviteUser = new ArrayList<User>();
        try {
            inviteUser = userDAO.inviteUser(filterBy, intIds, companyId, roleIds);
            
            
            Collections.sort(inviteUser,
                    new Comparator<User>()
                    {
                        public int compare(User arg0,
                                           User arg1) {
                            return Utils.compareCaseInsensitiveStrings(arg0.getUserName(), arg1.getUserName(), SortOrder.ASC);
                        }
                    });
            if (inviteUser != null) {
                logger.debug("Got user Data for :" + intIds);
            } else {
                logger.debug("Got empty  user Data for :" + intIds);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in inviting user");
            return null;
        }
        return inviteUser;
    }

    /**
     * To delete user
     *
     * @param userIds array of integer userIds that needs to be deleted
     */
    @Override
    public void deleteUsers(final Integer[] userIds,Integer userId) {
        if (userIds == null) {
            throw new IllegalArgumentException("Invalid userIds");
        }
        try {
            userDAO.deleteUsers(userIds ,userId);
            logger.info("Delete user details");
        }
        catch (Exception e) {
            logger.error("Error in deleting  user");
            logger.error(e,e);
        }

    }

    /**
     * To chnage user password
     *
     * @param user User for which password has to be changed
     * @return A string which holds "success" if password is successfully changed else "failure"
     */
    @Override
    public String changePassword(User user) {
        String status = null;
        if (user == null) {
            status = "failure";
            throw new IllegalArgumentException("Invalid user");
        }
        user.setCreateTime(new Date());
        logger.info("Encrypting password");
        String password = Utils.encryptPassword(user.getPassword());
        user.setPassword(password);
        try {

            status = userDAO.changePassword(user);
            logger.debug("User password is resetted");

        }
        catch (Exception e) {
            logger.error("Error in password resetting" + e);
        }

        return status;
    }

    /**
     * To Get users
     *
     * @param languageId String to filter terms respectively
     * @param colName    Column name that has to be sorted
     * @param sortOrder  Order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @param userId     Integer to filter terms respectively
     * @param companyId  Integer to filter terms respectively
     * @return List of users other than administrator
     */
    @Override
    public List<Member> getTeamMemberDetails(String languageId, String companyIds, String colName,
                                             String sortOrder, Integer pageNum, Integer userId, Integer companyId) {

        List<Member> teamMemberDetails = new ArrayList<Member>();
        try {

            teamMemberDetails = userDAO.getManageTeamMembers(languageId, companyIds,
                    colName, sortOrder, pageNum, userId, companyId);
            if ((teamMemberDetails != null) && (teamMemberDetails.size() > 0)) {
                logger.debug("Got Total team members : "
                        + teamMemberDetails.size());
            } else {
                logger.debug("Got empty team members list");
            }

        }
        catch (Exception e) {
            logger.error("Error in getting team member details");
            e.printStackTrace();
        }

        return teamMemberDetails;

    }

    /**
     * To update the user.
     *
     * @param user User that is to be updated
     * @return If user updated returns success else failure
     */
    @Override
    public String updateUser(User user) {
        String status = null;
        if (user == null) {

            throw new IllegalArgumentException("Invalid user");
        }

        try {

            status = userDAO.updateUser(user);
            logger.debug("update user");

        }
        catch (Exception e) {
            logger.error("Error in updating user");
            e.printStackTrace();
        }

        return status;

    }

    /**
     * To get the user
     *
     * @param userId Integer to filter users
     * @return User w.r.t the userId
     */
    @Override
    public User getUser(Integer userId) {
        if (userId == null) {
            return null;
        }
        User user = null;
        try {

            user = userDAO.getUser(userId);
            logger.debug("get user");

        }
        catch (Exception e) {
            logger.error("Error in getting user");
            e.printStackTrace();
        }

        return user;

    }

    /**
     * To save Or Update UserLanguage
     *
     * @param userLanguages UserLanguages to be saved/updated
     * @return UserLanguages that is saved/updated
     */
    @Override
    public UserLanguages saveOrUpdateUserLanguage(UserLanguages userLanguages) {
        if (userLanguages == null) {
            return null;
        }
        try {
            logger.debug("update userLanguages");
            return userDAO.saveOrUpdateUserLanguage(userLanguages);


        }
        catch (Exception e) {
            logger.error("Error in updating   user Languages");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * To delete UserLanguages
     *
     * @param userId Integer to filter UserLanguages
     * @return If successfully deleted it returns true else false
     */
    @Override
    public Boolean deleteUserLanguages(Integer userId) {
        if (userId == null) {
            return null;
        }
        Boolean status = false;
        try {
            logger.debug("delete userLanguages");

            status = userDAO.deleteUserLanguages(userId);

        }
        catch (Exception e) {
            logger.error("Error in deleting userLanguages");
            e.printStackTrace();
        }

        return status;
    }


    /**
     * get User To Export
     *
     * @param roleIds     Set collection which includes role id's to be filtered
     * @param languageIds Set collection which includes language id's to be filtered
     * @return List of users to be exported w.r.t the role id's and language id's
     * @parm company Company to filter users respectively
     */
    public List<User> getUserToExport(Set<Integer> roleIds, Set<Integer> languageIds, Set<Company> companies) {
        List<User> userList = null;
        try {
            logger.debug("getUsers based on roleId and language");

            userList = userDAO.getUserToExport(roleIds, languageIds, companies);


        }
        catch (Exception e) {
            logger.error("Error in getting users based on roleId and language" + e);
        }
        return userList;
    }

    /**
     * To verify whether email exists or not in database
     *
     * @return Returns true if email exists else it returns false
     */
    @Override
    public boolean ifEmailExists(String emailId) {
        boolean emailExists = false;
        try {
            emailExists = userDAO.ifEmailExists(emailId);
            logger.debug("email is not exists");

        }
        catch (Exception e) {
            logger.error("Error in verifying the email is exists or not");
            e.printStackTrace();
            emailExists = false;
        }
        return emailExists;
    }

    /**
     * To get user details
     *
     * @param domainId Integer to filter userDetails
     * @return List of user ids w.r.t the domainId
     */

    @Override
    public List<Integer> getUserByDomain(Integer domainId) {
        List<Integer> userList = new ArrayList<Integer>();
        try {
            userList = userDAO.getUserByDomain(domainId);
            if ((userList != null) && (userList.size() > 0)) {
                logger.debug(" users data :"
                        + userList.size());
            } else {
                logger.debug("Got users Data");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in getting user details");
            return null;
        }
        return userList;
    }

    /**
     * To get list of user type details
     *
     * @param roleId Array of Integer to filter user role Details
     * @return List of users role look up
     */
    @Override
    public List<Role> getAllUserTypeDetails(Integer roleId[]) {
        List<Role> userTypeDetails = new ArrayList<Role>();
        try {

            userTypeDetails = userDAO.getAllUserTypeDetails(roleId);
            if ((userTypeDetails != null) && (userTypeDetails.size() > 0)) {
                logger.debug("Got userType details : "
                        + userTypeDetails.size());
            } else {
                logger.debug("Got empty  userType details");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in geeting  userType details");
        }

        return userTypeDetails;

    }


    /**
     * To get list of user type menu details
     *
     * @param roleId Array of Integer to filter user role Details
     * @return List of menu obj's
     */
    @Override
    public List<Menu> getRoleMenuDetails(Integer roleId[]) {
        List<RoleMenuMgmt> roleMenuMgmtDetails = new ArrayList<RoleMenuMgmt>();
        List<Menu> menuDetails = new ArrayList<Menu>();
        try {

            roleMenuMgmtDetails = userDAO.getRoleMenuDetails(roleId);
            if ((roleMenuMgmtDetails != null) && (roleMenuMgmtDetails.size() > 0)) {
                logger.debug("Got role menu details : "
                        + roleMenuMgmtDetails.size());
                for (RoleMenuMgmt roleMenuMgmt : roleMenuMgmtDetails) {
                	Menu menu = roleMenuMgmt.getUserMenu();
                	menu.setMenuName(menu.getMenuName().toUpperCase());
                    menuDetails.add(menu);
                }
                Collections.sort(menuDetails,
                        new Comparator<Menu>()
                        {
                            public int compare(Menu arg0,
                                               Menu arg1) {
                                return Utils.compareIntegers(arg0.getDisplayOrder(), arg1.getDisplayOrder(), SortOrder.ASC);
                            }
                        });

            } else {
                logger.debug("Got empty  userType menu details");
            }

        }
        catch (Exception e) {
            logger.error("Error in geeting  userType details");
        }

        return menuDetails;

    }

    /**
     * To get list of user type Sub menu details
     *
     * @param roleId Array of Integer to filter user role Details
     * @param menuId Integer to filter user menu Details
     * @return List of Sub menu belongs to given userType
     */
    @Override
    public List<SubMenu> getRoleSubMenuDetails(Integer roleId[], Integer menuId) {
        List<RoleSubmenuMgmt> roleeSubMenuDetails = new ArrayList<RoleSubmenuMgmt>();
        List<SubMenu> subMenuDetails = new ArrayList<SubMenu>();
        try {

            roleeSubMenuDetails = userDAO.getRoleSubMenuDetails(roleId);
            if ((roleeSubMenuDetails != null) && (roleeSubMenuDetails.size() > 0)) {
                logger.debug("Got role sub menu details : "
                        + roleeSubMenuDetails.size());
                for (RoleSubmenuMgmt roleSubmenuMgmt : roleeSubMenuDetails) {
                    if (menuId.intValue() == roleSubmenuMgmt.getSubMenu().getMenu().getMenuId().intValue()) {
                        subMenuDetails.add(roleSubmenuMgmt.getSubMenu());
                    }
                }
                Collections.sort(subMenuDetails,
                        new Comparator<SubMenu>()
                        {
                            public int compare(SubMenu arg0,
                                               SubMenu arg1) {
                                return Utils.compareIntegers(arg0.getDisplayOrder(), arg1.getDisplayOrder(), SortOrder.ASC);
                            }
                        });

            } else {
                logger.debug("Got empty  userType menu details");
            }

        }
        catch (Exception e) {
            logger.error("Error in geeting  userType details");
            e.printStackTrace();
        }

        return subMenuDetails;

    }

    /**
     * To create company users
     *
     * @param company Company that has to be created
     * @param userId  Integer to filter user  Details
     * @return true if  company user created  else it returns false
     */
    @Override
    public Boolean createCompanyUsers(Company company, Integer userId) {
        if (company == null || userId == null)
            return false;
        User currentUser = userDAO.getUser(userId);

        String password = CompanyUserEnum.PASSWORD.getValue();
        User user = new User();
        CompanyTransMgmt companyTransMgmt = new CompanyTransMgmt();

        user.setUserName(company.getCompanyName() + CompanyUserEnum.ADMIN.getValue());
        user.setPassword(password);
        user.setEmailId(company.getEmailId());
        //user.setUserTypeId(6);
        user.setCreatedBy(userId);
        user.setUserLanguages(null);
        user.setCompany(company);
        user.setCreateTime(new Date());
        user.setIsActive("Y");
        Set<UserRole> userRoleSet = new HashSet<UserRole>();
        String roleName = RoleEnum.COMPANY_ADMIN.getValue();
        Role roleObj = userDAO.getRoleIdByLabel(roleName);
        Role role = lookUpDAO.getRole(roleObj.getRoleId());
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRoleSet.add(userRole);
        user.setUserRole(userRoleSet);
        registerUser(user);


        companyTransMgmt.setCompanyId(company.getCompanyId());
        companyTransMgmt.setUserId(user.getUserId());
        companyTransMgmt.setIsExternal("Y");
        companyTransMgmt.setIsActive("Y");
        userDAO.companyTransMgmt(companyTransMgmt);


        user = new User();
        companyTransMgmt = new CompanyTransMgmt();
        user.setUserName(company.getCompanyName() + CompanyUserEnum.TERMLEAD.getValue());
        user.setPassword(password);
        user.setEmailId(company.getEmailId());
        userRoleSet = new HashSet<UserRole>();
        String companyRoleName = RoleEnum.COMPANY_TERM_MANAGER.getValue();
        Role companyRoleObj = userDAO.getRoleIdByLabel(companyRoleName);
        role = lookUpDAO.getRole(companyRoleObj.getRoleId());
        userRole = new UserRole();
        userRole.setRole(role);
        userRoleSet.add(userRole);
        user.setUserRole(userRoleSet);
        user.setCreatedBy(userId);
        user.setCompany(company);
        user.setCreateTime(new Date());
        user.setIsActive("Y");
        registerUser(user);

        companyTransMgmt.setCompanyId(company.getCompanyId());
        companyTransMgmt.setUserId(user.getUserId());
        companyTransMgmt.setIsExternal("Y");
        companyTransMgmt.setIsActive("Y");
        userDAO.companyTransMgmt(companyTransMgmt);


        user = new User();
        companyTransMgmt = new CompanyTransMgmt();
        user.setUserName(company.getCompanyName() + CompanyUserEnum.TRANSLATOR.getValue());
        user.setPassword(password);
        user.setEmailId(company.getEmailId());
        user.setCreatedBy(userId);
        user.setCompany(company);
        userRoleSet = new HashSet<UserRole>();
        String translatorName = RoleEnum.COMPANY_TRANSLATOR.getValue();
        Role translatorRoleObj = userDAO.getRoleIdByLabel(translatorName);
        role = lookUpDAO.getRole(translatorRoleObj.getRoleId());
        userRole = new UserRole();
        userRole.setRole(role);
        userRoleSet.add(userRole);
        user.setUserRole(userRoleSet);
        user.setCreateTime(new Date());
        user.setIsActive("Y");
        registerUser(user);

        companyTransMgmt.setCompanyId(company.getCompanyId());
        companyTransMgmt.setUserId(user.getUserId());
        companyTransMgmt.setIsExternal("Y");
        companyTransMgmt.setIsActive("Y");
        userDAO.companyTransMgmt(companyTransMgmt);
        logger.debug("created company users");

        Set<UserRole> userRoles = currentUser.getUserRole();
        for (UserRole userRole1 : userRoles) {
            if (userRole1.getRole().getRoleId() == 1) {
                //Current user is super admin user,
                //Then add management to the new company
                companyTransMgmt = new CompanyTransMgmt();
                companyTransMgmt.setCompanyId(company.getCompanyId());
                companyTransMgmt.setUserId(currentUser.getUserId());
                companyTransMgmt.setIsExternal("Y");
                companyTransMgmt.setIsActive("Y");
                userDAO.companyTransMgmt(companyTransMgmt);
            }
        }

        return true;
    }


    /**
     * To get user details by company id
     *
     * @param companyId Integer to filter userDetails
     * @return List of user ids w.r.t the companyId
     */

    @Override
    public List<Integer> getUserByCompanyId(Integer companyId) {
        List<Integer> userList = new ArrayList<Integer>();
        try {
            userList = userDAO.getUserByCompanyId(companyId);
            if ((userList != null) && (userList.size() > 0)) {
                logger.debug(" users data :"
                        + userList.size());
            } else {
                logger.debug("Got users Data");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in getting user details");
            return null;
        }
        return userList;
    }


    /**
     * To  save register user in CompanyTransMgmt
     *
     * @param companyTransMgmt companyTransMgmt that has to be saved
     * @return Returns "success" if it is registered else it returns "failure"
     */
    @Override
    public String SaveCompanyTransMgmt(CompanyTransMgmt companyTransMgmt) {
        String status = null;
        if (companyTransMgmt == null) {
            status = "failure";
            throw new IllegalArgumentException("Invalid user");
        }
        userDAO.companyTransMgmt(companyTransMgmt);
        logger.debug("saved companyTransMgmt");
        return status;

    }

    /**
     * To  save or update user roles
     *
     * @param userRole UserRole that has to be saved/updated
     * @return Returns UserRole obj's
     */
    @Override
    public UserRole saveOrUpdateUserRole(UserRole userRole) {
        if (userRole == null) {
            return null;
        }
        try {
            logger.debug("update user role");
            return userDAO.saveOrUpdateUserRole(userRole);


        }
        catch (Exception e) {
            logger.error("Error in updating usre role");
            e.printStackTrace();
        }

        return null;
    }


    /**
     * To get previleges List
     *
     * @return Returns List of PrivilegesByMenu
     */
    @Override
    public List<PrivilegesByMenu> getPrevilegeList() {
        List<Privileges> allPrivilegesList = null;
        Map<String, List<Privileges>> privilegesMap = new HashMap<String, List<Privileges>>();
        List<PrivilegesByMenu> privilegesByMenuList = new ArrayList<PrivilegesByMenu>();
        try {
            allPrivilegesList = userDAO.getPrevilegeList();
            Collections.sort(allPrivilegesList,
                    new Comparator<Privileges>()
                    {
                        public int compare(Privileges arg0,
                                           Privileges arg1) {
                            return Utils.compareIntegers(arg0.getDisplayOrder(), arg1.getDisplayOrder(), SortOrder.ASC);
                        }
                    });
            if (allPrivilegesList != null && allPrivilegesList.size() > 0) {
                for (Privileges privileges : allPrivilegesList) {
                    List<Privileges> tempprivilegeList = privilegesMap.get(privileges.getMenu().getMenuName());
                    if (tempprivilegeList != null) {
                        tempprivilegeList.add(privileges);

                        privilegesMap.put(privileges.getMenu().getMenuName(), tempprivilegeList);
                    } else {
                        List<Privileges> privilegeList = new ArrayList<Privileges>();
                        privilegeList.add(privileges);
                        privilegesMap.put(privileges.getMenu().getMenuName(), privilegeList);
                    }
                }
                for (String menuName : privilegesMap.keySet()) {
                    List<Privileges> menuPrivilegesList = privilegesMap.get(menuName);
                    PrivilegesByMenu privilegesByMenu = new PrivilegesByMenu();
                    privilegesByMenu.setMenu(menuName);
                    List<PrivilegeTo> menuStrList = new ArrayList<PrivilegeTo>();
                    List<PrivilegesBySubMenu> privilegesBySubMenuList = new ArrayList<PrivilegesBySubMenu>();
                    Map<String, List<Privileges>> privilegessubMenuMap = new HashMap<String, List<Privileges>>();

                    for (Privileges privileges : menuPrivilegesList) {
                        if (privileges.getSubMenu() != null) {
                            List<Privileges> tempprivilegeList = privilegessubMenuMap.get(privileges.getSubMenu().getSubMenuName());
                            if (tempprivilegeList != null) {
                                tempprivilegeList.add(privileges);
                                privilegessubMenuMap.put(privileges.getSubMenu().getSubMenuName(), tempprivilegeList);
                            } else {
                                List<Privileges> privilegeList = new ArrayList<Privileges>();
                                privilegeList.add(privileges);
                                privilegessubMenuMap.put(privileges.getSubMenu().getSubMenuName(), privilegeList);
                            }
                        } else {
                            PrivilegeTo privilegeTo = new PrivilegeTo();
                            privilegeTo.setPrivilegeId(privileges.getPrivilegeId());
                            privilegeTo.setPrivilegeDesc(privileges.getPrivilegeDesc());
                            menuStrList.add(privilegeTo);
                        }
                    }
                    for (String subMenuName : privilegessubMenuMap.keySet()) {
                        List<Privileges> subMenuPrivilegesList = privilegessubMenuMap.get(subMenuName);
                        PrivilegesBySubMenu privilegesBySubMenu = new PrivilegesBySubMenu();
                        List<PrivilegeTo> subMenuStrList = new ArrayList<PrivilegeTo>();
                        privilegesBySubMenu.setSubMenu(subMenuName);
                        for (Privileges privileges : subMenuPrivilegesList) {
                            PrivilegeTo privilegeTo = new PrivilegeTo();
                            privilegeTo.setPrivilegeId(privileges.getPrivilegeId());
                            privilegeTo.setPrivilegeDesc(privileges.getPrivilegeDesc());
                            subMenuStrList.add(privilegeTo);
                        }
                        privilegesBySubMenu.setSubMenuPrivileges(subMenuStrList);
                        privilegesBySubMenuList.add(privilegesBySubMenu);
                    }
                    if (menuStrList != null && menuStrList.size() > 0)
                        privilegesByMenu.setMenuPrivileges(menuStrList);
                    if (privilegesBySubMenuList != null && privilegesBySubMenuList.size() > 0)
                        privilegesByMenu.setPrivilegesBySubMenu(privilegesBySubMenuList);
                    privilegesByMenuList.add(privilegesByMenu);
                }

            }
            if ((privilegesByMenuList != null) && (privilegesByMenuList.size() > 0)) {
                logger.debug("privilegesList :" + privilegesByMenuList.size());
            } else {
                logger.debug("Got empty privilegesList ");
            }
        }
        catch (Exception e) {
            logger.error("Error in getting  privilegesList");
        }
        return privilegesByMenuList;
    }

    /**
     * To update Previleges
     *
     * @param privelegIds Array of integer holding privelegIds
     * @param roleTypeId  Integer to identify user role
     * @return List of  user rolePrivileges
     */
    @Override
    public List<RolePrivileges> updatePrevileges(Integer[] privelegIds,
                                                 Integer roleTypeId, Integer userId) {
        List<RolePrivileges> rolePrivilegesList = null;
        Map<Integer, Menu> menuMap = new HashMap<Integer, Menu>();
        Map<Integer, SubMenu> subMenuMap = new HashMap<Integer, SubMenu>();

        if (roleTypeId != null && privelegIds != null && privelegIds.length > 0) {
            rolePrivilegesList = new ArrayList<RolePrivileges>();
            userDAO.deleteRolePrivileges(roleTypeId);
            userDAO.deleteRoleMenus(roleTypeId);
            userDAO.deleteRoleSubmenus(roleTypeId);

            Role role = userDAO.getRole(roleTypeId);
            for (int i = 0; i < privelegIds.length; i++) {
                Privileges privileges = userDAO.getPrevileges(privelegIds[i]);
                RolePrivileges rolePrivilege = new RolePrivileges();
                rolePrivilege.setRole(role);
                rolePrivilege.setCreateDate(new Date());
                rolePrivilege.setIsActive("Y");
                rolePrivilege.setCreatedBy(userId);
                rolePrivilege.setPrivileges(privileges);
                userDAO.saveRolePrevileges(rolePrivilege);
                rolePrivilegesList.add(rolePrivilege);
                if (privileges.getMenu() != null)
                    menuMap.put(privileges.getMenu().getMenuId(), privileges.getMenu());
                if (privileges.getSubMenu() != null)
                    subMenuMap.put(privileges.getSubMenu().getSubMenuId(), privileges.getSubMenu());
            }
            for (Integer menuId : menuMap.keySet()) {
                Menu menu = menuMap.get(menuId);
                RoleMenuMgmt roleMenuMgmt = new RoleMenuMgmt();
                roleMenuMgmt.setRole(role);
                roleMenuMgmt.setCreateDate(new Date());
                roleMenuMgmt.setIsActive("Y");
                roleMenuMgmt.setCreatedBy(userId);
                roleMenuMgmt.setUserMenu(menu);
                userDAO.saveRoleMenuMgmt(roleMenuMgmt);
            }
            for (Integer subMenuId : subMenuMap.keySet()) {
                SubMenu subMenu = subMenuMap.get(subMenuId);
                RoleSubmenuMgmt roleSubmenuMgmt = new RoleSubmenuMgmt();
                roleSubmenuMgmt.setRole(role);
                roleSubmenuMgmt.setCreateDate(new Date());
                roleSubmenuMgmt.setIsActive("Y");
                roleSubmenuMgmt.setCreatedBy(userId);
                roleSubmenuMgmt.setSubMenu(subMenu);
                userDAO.saveRoleSubMenuMgmt(roleSubmenuMgmt);
            }
            if ((rolePrivilegesList != null) && (rolePrivilegesList.size() > 0)) {
                logger.debug(" role privilegesList :" + rolePrivilegesList.size());
            } else {
                logger.debug("Got empty role privilegesList ");
            }
            return rolePrivilegesList;
        }
        return null;
    }

    /**
     * To get  previleges by role
     *
     * @param roleId Integer to identify user role
     * @return List of rolePrivileges
     */
    @Override
    public List<RolePrivileges> getPrevilegesByRole(Integer roleId) {
        List<RolePrivileges> privilegesList = null;
        try {
            privilegesList = userDAO.getPrevilegesByRole(roleId);
            if ((privilegesList != null) && (privilegesList.size() > 0)) {
                logger.debug("privilegesList :" + privilegesList.size());
            } else {
                logger.debug("Got empty privilegesList ");
            }
        }
        catch (Exception e) {
            logger.error("Error in getting  privilegesList");
        }
        return privilegesList;
    }

    /**
     * To verify whether user role  exists or not in database
     *
     * @return Returns true if user role exists else it returns false
     */
    @Override
    public boolean ifRoleExists(String roleName) {
        boolean roleExists = false;
        try {
            roleExists = userDAO.ifRoleExists(roleName);
        }
        catch (Exception e) {
            logger.error("Error in verifying the user is exists or not");
            e.printStackTrace();
            roleExists = false;
        }
        return roleExists;
    }

    /**
     * Used for Role Create/Update/Delete operations
     *
     * @param role   role obj to me modified
     * @param userId Integer to filter userDetails
     * @return String of the path to be redirected
     */
    @Override
    public String setRoleCUD(Role role, Integer userId) {
        String status = "failed";
        try {
            if (role.getTransactionType().equalsIgnoreCase("Add Role")) {

                role.setCreateDate(new Date());
                role.setCreatedBy(userId);
                role.setIsActive("Y");
                userDAO.saveOrUpdateRole(role);
                status = "success";
            }


            if (role.getTransactionType().equalsIgnoreCase("Edit Role")) {
                Role updateRole = userDAO.getRole(role.getRoleId());
                updateRole.setRoleName(role.getRoleName());
                updateRole.setUpdateDate(new Date());
                updateRole.setIsActive("Y");
                updateRole.setUpdatedBy(userId);
                userDAO.saveOrUpdateRole(updateRole);
                status = "success";
            }

            if (role.getTransactionType().equalsIgnoreCase("Delete Role")) {
                Role updateRole = userDAO.getRole(role.getRoleId());
                updateRole.setIsActive("N");
                updateRole.setUpdateDate(new Date());
                updateRole.setUpdatedBy(userId);
                userDAO.saveOrUpdateRole(updateRole);
                status = "success";
            }
            if (status != null) {
                logger.debug("Got role data ");
            } else {
                logger.debug("Got empty role data ");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in setting role data");
            return status;
        }
        return status;
    }

    /**
     * To get user details by role
     *
     * @param roleId Integer to filter user role Details
     * @return List of user ids w.r.t the roleId
     */
    @Override
    public List<Integer> getUserByRole(Integer roleId) {
        List<Integer> userList = new ArrayList<Integer>();
        try {
            userList = userDAO.getUserByRole(roleId);
            if ((userList != null) && (userList.size() > 0)) {
                logger.debug(" users data :"
                        + userList.size());
            } else {
                logger.debug("Got users Data");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in getting user details");
            return null;
        }
        return userList;
    }

    /**
     * To save user in CompanyTransMgmt
     *
     * @param user User that has to be saved
     * @return Returns "success" if it is registered else it returns "failure"
     */
    @Override
    public String SaveCompanyTransMgmt(User user) {
        String status = null;
        if (user == null) {
            status = "failure";
            throw new IllegalArgumentException("Invalid user");
        }
        CompanyTransMgmt companyTransMgmt = new CompanyTransMgmt();
        companyTransMgmt.setCompanyId(user.getCompany().getCompanyId());
        companyTransMgmt.setUserId(user.getUserId());
        companyTransMgmt.setIsExternal("Y");
        companyTransMgmt.setIsActive("Y");
        userDAO.companyTransMgmt(companyTransMgmt);
        logger.debug("save company tranMgmt user");
        return status;

    }

    /**
     * To get company tranMgmt users
     *
     * @param userId Integer to filter user  Details
     * @return set of companyTransMgmt obj's  w.r.t the userId
     */
    @Override
    public Set<CompanyTransMgmt> getCompanyTransMgmtUsers(Integer userId) {
        if (userId == null) {
            return null;
        }
        List<CompanyTransMgmt> companyTransMgmtUsers = null;
        Set<CompanyTransMgmt> companyTransMgmtUsersSet = null;
        ;
        try {

            companyTransMgmtUsers = userDAO.getCompanyTransMgmtUsers(userId);
            companyTransMgmtUsersSet = new HashSet<CompanyTransMgmt>(companyTransMgmtUsers);
            logger.debug("got company transMgmt users");

        }
        catch (Exception e) {
            logger.error("Error in getting user");
            e.printStackTrace();
        }

        return companyTransMgmtUsersSet;

    }

    /**
     * To save Or Update CompanyTransMgmt
     *
     * @param userCompanyTransMgmt CompanyTransMgmt that has to be saved/updated
     * @return CompanyTransMgmt that is saved/updated
     */
    @Override
    public CompanyTransMgmt saveOrUpdateCompanyTransMgmt(CompanyTransMgmt userCompanyTransMgmt) {
        if (userCompanyTransMgmt == null) {
            return null;
        }
        try {
            logger.debug("update company tranMgmt");
            return userDAO.saveOrUpdateCompanyTransMgmt(userCompanyTransMgmt);


        }
        catch (Exception e) {
            logger.error("Error in updating  company tranMgmt");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * To delete companyTransMgmt users
     *
     * @param userId array of integer userIds that needs to be deleted
     * @return Returns true if user exists else it returns false
     */
    @Override
    public Boolean deleteCompanyTransUsers(Integer userId) {
        if (userId == null) {
            return null;
        }
        Boolean status = false;
        try {


            status = userDAO.deleteCompanyTransUsers(userId);
            logger.debug("delete company transMgmt user");

        }
        catch (Exception e) {
            logger.error("Error in deleting company transMgmt user");
            e.printStackTrace();
        }

        return status;
    }

    /**
     * To get user details list by companyId
     *
     * @param companyId Integer to filter userDetails
     * @return List of user obj's w.r.t the companyId
     */

    @Override
    public List<User> getUserListByCompanyId(Integer companyId) {
        List<User> userList = new ArrayList<User>();
        try {
            userList = userDAO.getUserListByCompanyId(companyId);
            if ((userList != null) && (userList.size() > 0)) {
                logger.debug(" users data :"
                        + userList.size());
            } else {
                logger.debug("Got users Data");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in getting user details");
            return null;
        }
        return userList;
    }

    /**
     * To get user details list by company and role
     *
     * @param companyId Integer to filter userDetails
     * @param roleIds   Set collection which includes role id's to be filtered
     * @return List of user obj's w.r.t the companyId
     */

    @Override
    public List<User> getUserListByCompanyAndRole(Integer companyId, Set<Integer> roleIds) {
        List<User> userList = null;
        try {
            userList = userDAO.getUserListByCompanyAndRole(companyId, roleIds);
            if ((userList != null) && (userList.size() > 0)) {
                logger.debug(" users data :"
                        + userList.size());
            } else {
                logger.debug("Got users Data");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in getting user details");
            return null;
        }
        return userList;
    }

    /**
     * To get user details list by role
     *
     * @param roleId Integer to filter userDetails
     * @return List of user obj's w.r.t the roleId
     */
    @Override
    public List<User> getUserListByRole(Integer roleId) {
        List<User> userList = null;
        try {
            userList = userDAO.getUserListByRole(roleId);
            if ((userList != null) && (userList.size() > 0)) {
                logger.debug(" users data :"
                        + userList.size());
            } else {
                logger.debug("Got users Data");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in getting user details");
            return null;
        }
        return userList;
    }

    /**
     * To get user role list by menu
     *
     * @param menuId Integer to filter user roles
     * @return List of RoleMenuMgmt obj's w.r.t the menuId
     */
    public List<RoleMenuMgmt> getUserRoleListByMenu(Integer menuId) {
        List<RoleMenuMgmt> roleMenuMgmtDetails = new ArrayList<RoleMenuMgmt>();
        try {

            roleMenuMgmtDetails = userDAO.getUserRoleListByMenu(menuId);
            if ((roleMenuMgmtDetails != null) && (roleMenuMgmtDetails.size() > 0)) {
                logger.debug(" users role list:"
                        + roleMenuMgmtDetails.size());
            } else {
                logger.debug("Got users role list");
            }
        }
        catch (Exception e) {
            logger.error("Error in getting  user role List");
        }

        return roleMenuMgmtDetails;

    }

    /**
     * To get menuId by menuName
     *
     * @param menuName string to get Menu
     * @return Menu w.r.t the menuName
     */
    public Menu getMenuIdByLabel(String menuName) {
        Menu menu = null;
        try {
            menu = userDAO.getMenuIdByLabel(menuName);
            if (menu != null) {
                logger.debug("Got menuId for :" + menuName);
            } else {
                logger.debug("Got empty menuId for :" + menuName);
            }
        }
        catch (Exception e) {
            logger.error("Error in getting menuId");
            e.printStackTrace();
            return null;
        }
        return menu;
    }

    /**
     * To get Role Id by roleName
     *
     * @param roleName string to get Role
     * @return Role w.r.t the roleName
     */
    @Override
    public Role getRoleIdByLabel(String roleName) {
        Role role = null;
        try {
            role = userDAO.getRoleIdByLabel(roleName);
            if (role != null) {
                logger.debug("Got roleId for :" + roleName);
            } else {
                logger.debug("Got roleId for :" + roleName);
            }
        }
        catch (Exception e) {
            logger.error("Error in getting roleId");
            e.printStackTrace();
            return null;
        }
        return role;
    }

    /**
     * To get role id  list by label
     *
     * @param roleNames String  to filter user role ids
     * @return set of role ids  w.r.t the roleNames
     */
    public Set<Integer> getRoleIdListByLabel(Set<String> roleNames) {
        Set<Integer> userRoleIds = new HashSet<Integer>();

        try {
            userRoleIds = userDAO.getRoleIdListByLabel(roleNames);
            if (userRoleIds != null) {
                logger.debug("Got role ids for :" + roleNames);
            } else {
                logger.debug("Got empty roleIds for :" + roleNames);
            }
        }
        catch (Exception e) {
            logger.error("Error in getting roleIds");
            e.printStackTrace();
            return null;
        }
        return userRoleIds;
    }

	@Override
	public List<UserConfigFields> getPrevilegesByUser(Integer userId) {
		
		List<UserConfigFields> userScreenPrivilegesList=userDAO.getPrevilegesByUser(userId);
		
		
		return userScreenPrivilegesList;
	}

	/**
     * save configured fields
     *
     * @param Array String  selected field id's
     * @param Integer userid
     * @return String
     */
	
	@Override
	public String savefields(String[] data,Integer userId) {
		return userDAO.savefields(data, userId);
	}

}
