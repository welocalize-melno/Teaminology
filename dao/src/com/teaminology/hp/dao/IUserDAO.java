package com.teaminology.hp.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.teaminology.hp.bo.CompanyTransMgmt;
import com.teaminology.hp.bo.Menu;
import com.teaminology.hp.bo.Privileges;
import com.teaminology.hp.bo.Role;
import com.teaminology.hp.bo.RoleMenuMgmt;
import com.teaminology.hp.bo.RolePrivileges;
import com.teaminology.hp.bo.RoleSubmenuMgmt;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserConfigFields;
import com.teaminology.hp.bo.UserLanguages;
import com.teaminology.hp.bo.UserRole;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.UsersChartData;

/**
 * Contains DAO methods to handle users.
 *
 * @author sarvanic
 */
public interface IUserDAO
{

    /**
     * To register user
     *
     * @param user User to be registered
     * @return Returns "success" if it is registered else it returns "failure"
     * @throws DataAccessException
     */

    public String registerUser(User user) throws DataAccessException;


    /**
     * To get user details
     *
     * @param userId An integer for which data is to be retrieved
     * @return Member which holds the user details
     * @throws DataAccessException
     */
    public Member userDetails(final Integer userId) throws DataAccessException;

    /**
     * To get users Per month
     *
     * @param companyId to get userPerMonth
     * @return List which holds the count of users per month
     * @throws DataAccessException
     */

    public List<UsersChartData> usersPerMonth(Integer companyId) throws DataAccessException;


    /**
     * To get Total Active users count
     *
     * @param companyId to get ActiveUsercCount
     * @return integer which holds the count of active users
     * @throws DataAccessException
     */
    public Integer getActiveUsersCount(Integer companyId) throws DataAccessException;

    /**
     * To get HP community members
     *
     * @return List of Users registered in Community and Business Member roles
     * @throws DataAccessException
     */

    public List<Member> getHPMembers() throws DataAccessException;

    /**
     * To get list of user type details
     *
     * @return List of users role look up
     * @throws DataAccessException
     */

    public List<Role> getUserTypeDetails() throws DataAccessException;

    /**
     * To get user register languages
     *
     * @param userId Integer to be filtered
     * @return List of languages in which user is registered
     * @throws DataAccessException
     */

    public List<Language> getUserRegLanguages(final Integer userId) throws DataAccessException;


    /**
     * To get total users language terms
     *
     * @param languageId Integer to be filtered
     * @param userId     Integer to be filtered
     * @return An integer which holds the count of terms w.r.t the language
     * @throws DataAccessException
     */
    public Integer getTotalUsersLangTerms(final String languageId, final Integer userId) throws DataAccessException;

    /**
     * To get user accuracy rated values
     *
     * @param userId    Integer to be filtered
     * @param statusId  Integer to be filtered
     * @param companyId An Integer to  filter the users
     * @return A collection which contains the finalised voted terms count and total voted terms count
     * @throws DataAccessException
     */

    public Map<String, BigInteger> getUserAccuracyRate(final Integer userId, final Integer statusId, final Integer companyId) throws DataAccessException;


    /**
     * To verify whether user exists or not in database
     *
     * @param userName to be filtered
     * @return Returns true if user exists else it returns false
     * @throws DataAccessException
     */

    public boolean ifUserExists(String userName) throws DataAccessException;

    /**
     * To get admin users
     *
     * @return List of users registered in Admin user type
     * @throws DataAccessException
     */

    public List<UserRole> getAdminUsers() throws DataAccessException;

    /**
     * To get invite user details
     *
     * @param intIds    Array of integer role type/ language id's
     * @param filterBy  String to be filtered by role/language
     * @param companyId An Integer to  filter the users
     * @return List of Users to be invited
     * @throws DataAccessException
     */

    public List<User> inviteUser(String filterBy, Integer[] intIds, Integer companyId, Set<Integer> roleIds) throws DataAccessException;

    /**
     * To get Role details  by role
     *
     * @param userRole String which describes the user role
     * @return User Type w.r.t the user role
     * @throws DataAccessException
     */

    public Role getUserTypeByRole(String userRole) throws DataAccessException;


    /**
     * To chnage user password
     *
     * @param user User for which password to be changed
     * @return A string which holds "success" if successfully changed password else "failure"
     * @throws DataAccessException
     */
    public String changePassword(User user) throws DataAccessException;

    /**
     * To delete user
     *
     * @param userIds array of integer userIds that needs to be deleted
     * @throws DataAccessException
     */

    public void deleteUsers(final Integer[] userIds, final Integer userId) throws DataAccessException;

    /**
     * To Get users
     *
     * @param languageId String to filter terms respectively
     * @param colName    Column name that has to be sorted
     * @param sortOrder  Order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @param companyId  An Integer to  filter the users
     * @param userId     Integer to filter terms respectively
     * @return List of users other than administrator
     * @throws DataAccessException
     */

    public List<Member> getManageTeamMembers(String languageId, String companyIds, String colName,
                                             String sortOrder, Integer pageNum, Integer userId, Integer companyId) throws DataAccessException;


    /**
     * To update the user.
     *
     * @param user User that is to be updated
     * @return If user updated returns success else failure
     * @throws DataAccessException
     */

    public String updateUser(User user) throws DataAccessException;

    /**
     * To get the user
     *
     * @param userId Integer for which User is to be filtered
     * @return User w.r.t the userId
     * @throws DataAccessException
     */

    public User getUser(Integer userId) throws DataAccessException;

    /**
     * To save Or Update UserLanguage
     *
     * @param userLanguages that needs to be saved/updated
     * @return UserLanguages that is saved/updated
     *         * @throws DataAccessException
     */

    public UserLanguages saveOrUpdateUserLanguage(UserLanguages userLanguages) throws DataAccessException;

    /**
     * To delete UserLanguages
     *
     * @param userId Integer to be filtered
     * @return If successfully deleted it returns true else false
     * @throws DataAccessException
     */
    public Boolean deleteUserLanguages(Integer userId) throws DataAccessException;


    /**
     * get User To Export
     *
     * @param roleIds     Set collection which includes role id's to be filtered
     * @param languageIds Set collection which includes language id's to be filtered
     * @return List of users to be exported w.r.t the role id's and language id's
     * @throws DataAccessException
     */
    public List<User> getUserToExport(Set<Integer> roleIds, Set<Integer> languageIds, Set<Company> companies) throws DataAccessException;

    /**
     * To verify whether email exists or not in database
     *
     * @param emailId String to be filtered
     * @return Returns true if email exists else it returns false
     * @throws DataAccessException
     */

    public boolean ifEmailExists(String emailId) throws DataAccessException;

    /**
     * To get the user details
     *
     * @param domainId Integer for which User is to be filtered
     * @return List of User ids w.r.t the domainId
     * @throws DataAccessException
     */
    public List<Integer> getUserByDomain(Integer domainId) throws DataAccessException;

    /**
     * To get list of user type details
     *
     * @param roleId Integer array to get role details
     * @return List of Roles w.r.t roleId
     * @throws DataAccessException
     */

    public List<Role> getAllUserTypeDetails(Integer roleId[]) throws DataAccessException;

    /**
     * To get list of user  type sub menu details
     *
     * @param roleId Integer array to get RoleSubMenuManagement
     * @return List of Role Sub Menu Management w.r.t roleId
     * @throws DataAccessException
     */


    public List<RoleSubmenuMgmt> getRoleSubMenuDetails(Integer roleId[]) throws DataAccessException;

    /**
     * To get list of user  type menu details
     *
     * @param roleIds Integer array to get Role MenuMgmt
     * @return List of Role Menu Management w.r.t roleId
     * @throws DataAccessException
     */


    public List<RoleMenuMgmt> getRoleMenuDetails(Integer roleId[]) throws DataAccessException;

    /**
     * To get user details by company id
     *
     * @param companyIds Integer to filter userDetails
     * @return List of user ids w.r.t the companyIds
     * @throws DataAccessException
     */

    public List<Integer> getUserByCompanyId(Integer companyId) throws DataAccessException;

    /**
     * To save companyTransMgmt
     *
     * @param companyTransMgmt CompanyTransMgmt to be saved
     * @return Returns "success" if it is registered else it returns "failure"
     * @throws DataAccessException
     */


    public String companyTransMgmt(CompanyTransMgmt companyTransMgmt) throws DataAccessException;

    /**
     * To  save or update user roles
     *
     * @param userRole UserRole that has to be saved/updated
     * @return Returns UserRole
     * @throws DataAccessException
     */

    public UserRole saveOrUpdateUserRole(UserRole userRole) throws DataAccessException;

    /**
     * To get previleges List
     *
     * @return Returns List of PrivilegesByMenu
     * @throws DataAccessException
     */
    public List<Privileges> getPrevilegeList() throws DataAccessException;

    /**
     * To get role
     *
     * @param roleTypeId Integer to filter user roles
     * @return role  w.r.t roleTypeId
     * @throws DataAccessException
     */

    public Role getRole(Integer roleTypeId) throws DataAccessException;

    /**
     * To get previleges
     *
     * @param fileIdArray Integer to filter user previleges
     * @return Privileges w.r.t fileIdArray
     * @throws DataAccessException
     */
    public Privileges getPrevileges(Integer fileIdArray) throws DataAccessException;

    /**
     * To save Role Previleges
     *
     * @param RolePrivileges RolePrivileges that has to be saved
     * @throws DataAccessException
     */

    public void saveRolePrevileges(RolePrivileges rolePrivilege) throws DataAccessException;

    /**
     * To delete Role Previleges
     *
     * @param roleId Integer to filter user role previleges
     * @throws DataAccessException
     */

    public void deleteRolePrivileges(Integer roleId) throws DataAccessException;

    /**
     * To delete role menus by roleId
     *
     * @param roleId Integer to identify user role
     * @throws DataAccessException
     */
    public void deleteRoleMenus(Integer roleId) throws DataAccessException;

    /**
     * To delete role sub menus by roleId
     *
     * @param roleId Integer to identify user role
     * @throws DataAccessException
     */
    public void deleteRoleSubmenus(Integer roleId) throws DataAccessException;

    /**
     * To get  previleges by roleId
     *
     * @param roleId Integer to identify user role
     * @return List of rolePrivileges
     * @throws DataAccessException
     */
    public List<RolePrivileges> getPrevilegesByRole(Integer roleId) throws DataAccessException;

    /**
     * To verify whether user role  exists or not in database
     *
     * @param roleName to check particular role exists or not
     * @return Returns true if user role exists else returns false
     * @throws DataAccessException
     */
    public boolean ifRoleExists(String roleName) throws DataAccessException;

    /**
     * To save Or Update Role
     *
     * @param role Role that has to be saved/updated
     * @throws DataAccessException
     */
    public void saveOrUpdateRole(Role role) throws DataAccessException;

    /**
     * To get user details by role
     *
     * @param roleId Integer to filter user role Details
     * @return List of user ids w.r.t the roleId
     * @throws DataAccessException
     */

    public List<Integer> getUserByRole(Integer roleId) throws DataAccessException;

    /**
     * To save role menu mgmt
     *
     * @param roleMenuMgmt RoleMenuMgmt that has to be saved
     * @throws DataAccessException
     */

    public void saveRoleMenuMgmt(RoleMenuMgmt roleMenuMgmt) throws DataAccessException;

    /**
     * To save role sub menu mgmt
     *
     * @param roleSubmenuMgmt RoleSubmenuMgmt that has to be saved
     * @throws DataAccessException
     */
    public void saveRoleSubMenuMgmt(RoleSubmenuMgmt roleSubmenuMgmt) throws DataAccessException;

    /**
     * To get company tranMgmt users
     *
     * @param userId Integer to filter user  Details
     * @return List of companyTransMgmt   w.r.t the userId
     * @throws DataAccessException
     */
    public List<CompanyTransMgmt> getCompanyTransMgmtUsers(Integer userId) throws DataAccessException;

    /**
     * To save Or Update CompanyTransMgmt
     *
     * @param userCompanyTransMgmt CompanyTransMgmt that has to be saved/updated
     * @return CompanyTransMgmt that is saved/updated
     * @throws DataAccessException
     */

    public CompanyTransMgmt saveOrUpdateCompanyTransMgmt(
            CompanyTransMgmt userCompanyTransMgmt) throws DataAccessException;

    /**
     * To delete companyTransMgmt users
     *
     * @param userIds array of integer userIds that needs to be deleted
     * @return Returns true if user exists else returns false
     * @throws DataAccessException
     */

    public Boolean deleteCompanyTransUsers(Integer userId) throws DataAccessException;

    /**
     * To get user details list by companyId
     *
     * @param companyId Integer to filter userDetails
     * @return List of user  w.r.t the companyId
     * @throws DataAccessException
     */

    public List<User> getUserListByCompanyId(Integer companyId) throws DataAccessException;

    /**
     * To get user details list by company and role
     *
     * @param companyId Integer to filter userDetails
     * @param roleIds   Set collection which includes role id's to be filtered
     * @return List of user  w.r.t the companyId
     * @throws DataAccessException
     */

    public List<User> getUserListByCompanyAndRole(Integer companyId, Set<Integer> roleIds) throws DataAccessException;

    /**
     * To get user details list by role
     *
     * @param roleId Integer to filter userDetails
     * @return List of user  w.r.t the roleId
     * @throws DataAccessException
     */

    public List<User> getUserListByRole(Integer roleId) throws DataAccessException;

    /**
     * To save or update the gs credintials
     *
     * @param company Company to be saved
     * @return Returns "success" if it is registered else it returns "failure"
     * @throws DataAccessException
     */

    public String saveOrUpdateGSCredintials(Company company) throws DataAccessException;

    /**
     * To get GS credintials
     *
     * @param companyId Integer to get the credintials details
     * @return company w.r.t the companyId
     * @throws DataAccessException
     */
    public Company getGSCredintails(Integer companyId) throws DataAccessException;

    /**
     * To get user role list by menu
     *
     * @param roleId Integer to filter user roles
     * @return List of RoleMenuMgmt  w.r.t the menuId
     * @throws DataAccessException
     */
    public List<RoleMenuMgmt> getUserRoleListByMenu(Integer menuId) throws DataAccessException;

    /**
     * To get menu Id by menuName
     *
     * @param menuName string to get Menu
     * @return Menu w.r.t the menuName
     * @throws DataAccessException
     */
    public Menu getMenuIdByLabel(String menuName);


    /**
     * To get Role Id by roleName
     *
     * @param roleName string to get Role
     * @return Role w.r.t the roleName
     * @throws DataAccessException
     */

    public Role getRoleIdByLabel(String roleName);

    /**
     * To get Role Id by roleName
     *
     * @param roleName string to get roleIds
     * @return set of roleIds w.r.t the roleName
     * @throws DataAccessException
     */
    public Set<Integer> getRoleIdListByLabel(Set<String> roleIds);

    public List<User> getSuperAdmins();

    public List<User> getCompanyUsersByRole(Integer companyId, Integer roleId);
    
    
    public List<UserConfigFields> getPrevilegesByUser(Integer userId);

	public String savefields(String[] data, Integer userId);


}
