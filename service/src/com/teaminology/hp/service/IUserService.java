package com.teaminology.hp.service;


import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.teaminology.hp.bo.CompanyTransMgmt;
import com.teaminology.hp.bo.Menu;
import com.teaminology.hp.bo.Role;
import com.teaminology.hp.bo.RoleMenuMgmt;
import com.teaminology.hp.bo.RolePrivileges;
import com.teaminology.hp.bo.SubMenu;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserLanguages;
import com.teaminology.hp.bo.UserRole;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.PrivilegesByMenu;
import com.teaminology.hp.data.UsersChartData;


/**
 * Contains method prototypes for User.
 *
 * @author sarvanic
 */
public interface IUserService
{

    /**
     * To register user
     *
     * @param user - User to be registered
     * @return Returns "success" if it is registered else it returns "failure"
     */
    public String registerUser(User user);

    /**
     * To get user details
     *
     * @param userId - an integer for which data is to be retrieved
     * @return Member which holds the user details
     */
    public Member userDetails(Integer userId);


    /**
     * To get users Per month
     *
     * @return List which holds the count of users per month
     */

    public List<UsersChartData> usersPerMonth(Integer companyId);

    /**
     * To get Total Active users count
     *
     * @return integer which holds the count of active users
     */

    public Integer getActiveUsersCount(Integer companyId);


    /**
     * To get HP community members
     *
     * @return List of Users registered in Community and Business Member roles
     */
    public List<Member> getHPMembers();

    /**
     * To get list of user type details
     *
     * @return List of users role look up
     */
    public List<Role> getUserTypeDetails();

    /**
     * To get user register languages
     *
     * @param userId - Integer to be filtered
     * @return List of languages in which user is registered
     */

    public List<Language> getUserRegLanguages(Integer userId);

    /**
     * To get total users language terms
     *
     * @param languageId - Integer to be filtered
     * @param userId     - Integer to be filtered
     * @return an integer which holds the count of terms w.r.t the language
     */
    public Integer getTotalUsersLangTerms(final String languageId, final Integer userId);

    /**
     * To get user accuracy rated values
     *
     * @return A collection which contains the finalized voted terms count and total voted terms count
     */
    public Map<String, BigInteger> getUserAccuracyRate(Integer userId, Integer statusId, Integer companyId);

    /**
     * To verify whether user exists or not in database
     *
     * @return Returns true if user exists else it returns false
     */
    public boolean ifUserExists(String userName);

    /**
     * To get invite user details
     *
     * @param intIds   - array of integer role type/ language id's
     * @param filterBy - String to be filtered by role/language
     * @return List of Users to be invited
     */
    public List<User> inviteUser(String filterBy, Integer intIds[], Integer companyId, Set<Integer> roleIds);

    /**
     * To delete user
     *
     * @param userIds - array of integer userIds that needs to be deleted
     */

    public void deleteUsers(final Integer[] userIds , Integer userId);

    /**
     * To chnage user password
     *
     * @param user - User for which password to be changed
     * @return A string which holds "success" if successfully changed password else "failure"
     */

    public String changePassword(User user);

    /**
     * To Get users
     *
     * @param languageId - String to filter terms respectively
     * @param colName    - column name that has to be sorted
     * @param sortOrder  - order in which it has to be sorted
     * @param pageNum    - Integer to limit the data
     * @param userId     - Integer to filter terms respectively
     * @return List of users other than administrator
     */

    public List<Member> getTeamMemberDetails(String languageId, String companyIds, String colName,
                                             String sortOrder, Integer pageNum, Integer userId, Integer companyId);

    /**
     * To update the user.
     *
     * @param user User that is to be updated
     * @return If user updated returns success else failure
     */

    public String updateUser(User user);

    /**
     * To get the user
     *
     * @param userId - Integer for which User is to be filtered
     * @return User w.r.t the userId
     */

    public User getUser(Integer userId);

    /**
     * To save Or Update UserLanguage
     *
     * @param userLanguages - that needs to be saved/updated
     * @return UserLanguages that is saved/updated
     */

    public UserLanguages saveOrUpdateUserLanguage(UserLanguages userLanguages);

    /**
     * To delete UserLanguages
     *
     * @param userId - Integer to be filtered
     * @return If successfully deleted it returns true else false
     */

    public Boolean deleteUserLanguages(Integer userId);


    /**
     * get User To Export
     *
     * @param roleIds     - Set collection which includes role id's to be filtered
     * @param languageIds - Set collection which includes language id's to be filtered
     * @return List of users to be exported w.r.t the role id's and language id's
     */
    public List<User> getUserToExport(Set<Integer> roleIds, Set<Integer> languageIds, Set<Company> companies);

    /**
     * To verify whether email exists or not in database
     *
     * @return Returns true if email exists else it returns false
     */
    public boolean ifEmailExists(String emailId);

    /**
     * To get user details
     *
     * @param domainId Integer to filter userDetails
     * @return List of user ids  w.r.t the domainId
     */
    public List<Integer> getUserByDomain(Integer domainId);

    /**
     * To get list of user type details
     *
     * @return List of users role look up
     */

    public List<Role> getAllUserTypeDetails(Integer roleId[]);

    /**
     * To get list of user type menu details
     *
     * @param roleId Array of Integer to filter user role Details
     * @return List of menu obj's
     */
    public List<Menu> getRoleMenuDetails(Integer roleId[]);

    /**
     * To get list of user type Sub menu details
     *
     * @param roleId Array of Integer to filter user role Details
     * @param menuId Integer to filter user menu Details
     * @return List of Sub menu belongs to given userType
     */

    public List<SubMenu> getRoleSubMenuDetails(Integer roleId[], Integer menuId);

    /**
     * To create company users
     *
     * @param company Company to  be created
     * @param userId  Integer to filter user  Details
     * @return true if  company user created  else it returns false
     */

    public Boolean createCompanyUsers(Company company, Integer userId);

    /**
     * To get user details by company id
     *
     * @param companyIds Integer to filter userDetails
     * @return List of user ids w.r.t the companyId
     */

    public List<Integer> getUserByCompanyId(Integer companyId);

    /**
     * To  save register user in CompanyTransMgmt
     *
     * @param companyTransMgmt companyTransMgmt that has to be saved
     * @return Returns "success" if it is registered else it returns "failure"
     */

    public String SaveCompanyTransMgmt(CompanyTransMgmt companyTransMgmt);

    /**
     * To  save or update user roles
     *
     * @param userRole UserRole that has to be saved/updated
     * @return Returns UserRole obj's
     */
    public UserRole saveOrUpdateUserRole(UserRole userRole);

    /**
     * To get previleges List
     *
     * @return Returns List of PrivilegesByMenu
     */

    public List<PrivilegesByMenu> getPrevilegeList();

    /**
     * To update Previleges
     *
     * @param privelegIds Array of integer holding privelegIds
     * @param roleTypeId  Integer to identify user role
     * @return List of  user rolePrivileges
     */

    public List<RolePrivileges> updatePrevileges(Integer[] fileIdArray, Integer roleTypeId, Integer userId);

    /**
     * To get  previleges by role
     *
     * @param roleId Integer to identify user role
     * @return List of rolePrivileges
     */

    public List<RolePrivileges> getPrevilegesByRole(Integer roleId);

    /**
     * To verify whether user role  exists or not in database
     *
     * @return Returns true if user role exists else it returns false
     */

    public boolean ifRoleExists(String roleName);

    /**
     * Used for Role Create/Update/Delete operations
     *
     * @param role   role obj to me modified
     * @param userId Integer to filter userDetails
     * @return String of the path to be redirected
     */

    public String setRoleCUD(Role role, Integer userId);

    /**
     * To get user details by role
     *
     * @param roleId Integer to filter user role Details
     * @return List of user ids w.r.t the roleId
     */

    public List<Integer> getUserByRole(Integer roleId);

    /**
     * To save user in CompanyTransMgmt
     *
     * @param user User that has to be saved
     * @return Returns "success" if it is registered else it returns "failure"
     */

    public String SaveCompanyTransMgmt(User user);

    /**
     * To get company tranMgmt users
     *
     * @param userId Integer to filter user  Details
     * @return set of companyTransMgmt obj's  w.r.t the userId
     */

    public Set<CompanyTransMgmt> getCompanyTransMgmtUsers(Integer userId);

    /**
     * To save Or Update CompanyTransMgmt
     *
     * @param userCompanyTransMgmt CompanyTransMgmt that has to be saved/updated
     * @return CompanyTransMgmt that is saved/updated
     */

    public CompanyTransMgmt saveOrUpdateCompanyTransMgmt(CompanyTransMgmt userCompanyTransMgmt);

    /**
     * To delete companyTransMgmt users
     *
     * @param userIds array of integer userIds that needs to be deleted
     * @return Returns true if user exists else it returns false
     */

    public Boolean deleteCompanyTransUsers(Integer userId);

    /**
     * To get user details list by companyId
     *
     * @param companyId Integer to filter userDetails
     * @return List of user obj's w.r.t the companyId
     */


    public List<User> getUserListByCompanyId(Integer companyId);

    /**
     * To get user details list by company and role
     *
     * @param companyId Integer to filter userDetails
     * @param roleIds   Set collection which includes role id's to be filtered
     * @return List of user obj's w.r.t the companyId
     */

    public List<User> getUserListByCompanyAndRole(Integer companyId, Set<Integer> roleIds);

    /**
     * To get user details list by role
     *
     * @param roleId Integer to filter userDetails
     * @return List of user obj's w.r.t the roleId
     */

    public List<User> getUserListByRole(Integer roleId);

    /**
     * To get user role list by menu
     *
     * @param roleId Integer to filter user roles
     * @return List of RoleMenuMgmt obj's w.r.t the menuId
     */
    public List<RoleMenuMgmt> getUserRoleListByMenu(Integer menuId);

    /**
     * To get menuId by menuName
     *
     * @param menuName string to get Menu
     * @return Menu w.r.t the menuName
     */
    public Menu getMenuIdByLabel(String menuName);

    /**
     * To get Role Id by roleName
     *
     * @param roleName string to get Role
     * @return Role w.r.t the roleName
     */
    public Role getRoleIdByLabel(String roleName);

    /**
     * To get role id  list by label
     *
     * @param roleNames String  to filter user role ids
     * @return set of role ids  w.r.t the roleNames
     */

    public Set<Integer> getRoleIdListByLabel(Set<String> roleNames);


}
