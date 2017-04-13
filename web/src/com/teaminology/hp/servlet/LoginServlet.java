package com.teaminology.hp.servlet;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.teaminology.hp.TeaminologyService;
import com.teaminology.hp.Utils;
import com.teaminology.hp.bo.Menu;
import com.teaminology.hp.bo.SubMenu;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserRole;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.dao.ILookUpDAO;
import com.teaminology.hp.dao.IUserDAO;
import com.teaminology.hp.dao.IUtilDAO;
import com.teaminology.hp.dao.TermDetailsDAO;
import com.teaminology.hp.service.ITermDetailsService;
import com.teaminology.hp.service.enums.RoleEnum;
import com.teaminology.hp.service.enums.SessionKey;
import com.teaminology.hp.service.enums.TeaminologyPage;


/**
 * Contains  methods to login into the application.
 *
 * @author Sarvanic
 */

public class LoginServlet extends HttpServlet
{
    private Logger logger = Logger.getLogger(LoginServlet.class);
   
    @Override
    public void init() throws ServletException {
        super.init();
    }
    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
			handleLogin(request, response);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * To check user login details.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws UnsupportedEncodingException 
     */
    private void handleLogin(HttpServletRequest request,
                             HttpServletResponse response) throws UnsupportedEncodingException {
    	request.setCharacterEncoding("UTF-8");
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        User user;
        Company company = null;
        boolean isSuperAdminUser = false;
        boolean isCompanyAdmin = false;
        boolean isCompanyTeamManager = false;
        boolean isCompanyTranslator = false;
        boolean isCommunityMember = false;
        boolean isSuperTranslator = false;

        boolean isHpCompany = false;
        logger.info("Entering login...username[" + userName + " (" + password + ")]");
        try {
            logger.debug("Encrypting password.....");
            password = Utils.encryptPassword(password);

            user = TeaminologyService.authentication(userName, password);

            HttpSession session = request.getSession();
            String pageUrl = request.getContextPath();
            String tmp = request.getRequestURI();
           if (tmp.toLowerCase().indexOf("newell") > 0)
                pageUrl += "/newell";

            if (user != null) {
                company = user.getCompany();
                if(company != null) {
                if (company != null && "newell".equalsIgnoreCase(company.getCompanyName()))
                    isHpCompany = true;

                //TODO:  This can be removed if we just use one set of web pages
                if (company != null && company.getContextRoot() != null)
                    pageUrl = request.getContextPath() + "/" + company.getContextRoot();
                else
                    pageUrl = request.getContextPath();
                }
                session.setAttribute(SessionKey.USER.getKey(), user);
                session.setAttribute(SessionKey.USER_ID.getKey(), user.getUserId());
                session.setAttribute(SessionKey.COMPANY.getKey(), company);
                session.setAttribute(SessionKey.HP_COMPANY.getKey(), isHpCompany);

                Set<UserRole> userRoles = TeaminologyService.getUserRole(user.getUserId());
                Integer roleIds[] = null;
                if (userRoles != null && userRoles.size() > 0) {
                    roleIds = new Integer[userRoles.size()];
                    int i = 0;
                    String roleName = "";
                    for (UserRole userRole : userRoles) {
                        roleIds[i] = userRole.getRole().getRoleId();
                        roleName = userRole.getRole().getRoleName();

                        if (RoleEnum.SUPER_ADMIN.getValue().equalsIgnoreCase(roleName))
                            isSuperAdminUser = true;
                        else if (RoleEnum.SUPER_TRANSLATOR.getValue().equalsIgnoreCase(roleName))
                            isSuperTranslator = true;
                        else if (RoleEnum.COMPANY_ADMIN.getValue().equalsIgnoreCase(roleName))
                            isCompanyAdmin = true;
                        else if (RoleEnum.COMPANY_TERM_MANAGER.getValue().equalsIgnoreCase(roleName))
                            isCompanyTeamManager = true;
                        else if (RoleEnum.COMPANY_TRANSLATOR.getValue().equalsIgnoreCase(roleName))
                            isCompanyTranslator = true;
                        else if (RoleEnum.COMMUNITY_MEMBER.getValue().equalsIgnoreCase(roleName))
                            isCommunityMember = true;

                        i++;
                    }
                }

                session.setAttribute(SessionKey.SUPER_ADMIN.getKey(), isSuperAdminUser);
                session.setAttribute(SessionKey.SUPER_TRANSLATOR.getKey(), isSuperTranslator);
                session.setAttribute(SessionKey.COMPANY_ADMIN_USER.getKey(), isCompanyAdmin);
                session.setAttribute(SessionKey.COMPANY_TEAM_MANAGER.getKey(), isCompanyTeamManager);
                session.setAttribute(SessionKey.COMPANY_TRANSLATOR.getKey(), isCompanyTranslator);
                session.setAttribute(SessionKey.COMMUNITY_MEMBER.getKey(), isCommunityMember);

                session.setAttribute(SessionKey.USER_ROLE.getKey(), userRoles);

                List<Menu> roleMenuList = TeaminologyService.getRoleMenuDetails(roleIds);
                session.setAttribute(SessionKey.MENU.getKey(), roleMenuList);

                List<SubMenu> subMenus = new ArrayList<SubMenu>();
                if (roleMenuList != null && !roleMenuList.isEmpty()) {
                    for (Menu menu : roleMenuList) {
                        List<SubMenu> sms = TeaminologyService.getRoleSubMenuDetails(roleIds, menu.getMenuId());
                        if (sms != null)
                            subMenus.addAll(sms);
                    }
                }
                session.setAttribute(SessionKey.SUB_MENU.getKey(), subMenus);

                //To set last login time
                user = TeaminologyService.getUserByUserId(user.getUserId());
                user.setLastLoginTime(Calendar.getInstance().getTime());
                TeaminologyService.updateUser(user);

                if ("on".equalsIgnoreCase(rememberMe)) {
                    String encryptedUserDtls = userName + "#^#$~" + password;

                    String encodedEncyptUserDtls = URLEncoder.encode(encryptedUserDtls, "UTF-8");

                    Cookie rememberMeCookie = new Cookie(SessionKey.REMEMBER_ME_COOKIE.name(),
                            encodedEncyptUserDtls);

                    //cookie life set to be 10 years
                    rememberMeCookie.setMaxAge(315660000);
                    response.addCookie(rememberMeCookie);
                }
            } else {
            	session.setAttribute("invalidLogin", "InvalidLogin");
                pageUrl = pageUrl + TeaminologyPage.INVALIDLOGIN.getPageUrl();
                response.sendRedirect(pageUrl);
                logger.info("Invalid Login, Redirecting to index page...." + pageUrl);
            }
            //For dashboard we are taking separate jsp not index.jsp
            response.sendRedirect(pageUrl + "/" + "dashboard.jsp");
        }
        catch (Exception e) {
            logger.error("Error in user authentication.", e);
        }
    }
}
		

	 