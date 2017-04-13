package com.teaminology.hp.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.teaminology.hp.TeaminologyService;
import com.teaminology.hp.bo.Role;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserRole;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.service.enums.RoleEnum;
import com.teaminology.hp.service.enums.SessionKey;
import com.teaminology.hp.service.enums.TeaminologyEnum;
import com.teaminology.hp.service.enums.TeaminologyProperty;

/**
 * Filter which is used to track Remember Me cookie.
 *
 * @author sarvani
 */
public class LoginFilter implements Filter
{
    private Logger logger = Logger.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    /**
     * To check user login details.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain fc) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        //resp.addHeader("X-FRAME-OPTIONS", "DENY");
        HttpSession session = req.getSession();
        String url = req.getRequestURL().toString();
        String contextRoot = null;
        if (!url.contains(TeaminologyEnum.CONTROLLER_URL.getValue()) && !url.endsWith(".js") && !url.endsWith(".css") && !url.contains(TeaminologyEnum.IMAGES.getValue())) {
            if (url.contains(TeaminologyProperty.DOMAIN.getValue())) {
                contextRoot = url.substring(url.lastIndexOf(TeaminologyProperty.DOMAIN.getValue()) + TeaminologyProperty.DOMAIN.getValue().length() - 1, url.lastIndexOf("/"));
                contextRoot = contextRoot.substring(contextRoot.indexOf("/") + 1, contextRoot.length());
            }
            Company company = TeaminologyService.getCompanyByContextRoot("avigilon");
            if (null != company) {
                session.setAttribute(SessionKey.COMPANY.getKey(), company);
            } else {
                session.removeAttribute(SessionKey.COMPANY.getKey());
            }
        }

        if (session.getAttribute(SessionKey.USER.getKey()) != null) {
            fc.doFilter(request, response);
        } else {
            //logger.info("Entering login...");

            User user;
            if (session.getAttribute(SessionKey.COOKIE_FLAG.getKey()) == null) {
                // Login if the Remember Cookie already exists for the user
                Cookie rememberMeCookie = null;
                try {
                    rememberMeCookie = getRememberMeCookie(req);
                    if (rememberMeCookie != null) {
                        String[] cookieData = URLDecoder.decode(rememberMeCookie.getValue(), "UTF-8").split("\\#\\^\\#\\$\\~");
                        String userName = cookieData[0];
                        String password = cookieData[1];
                        logger.info("Cookie found for user: " + userName + ". Authenticating using cookie ...");
                        user = TeaminologyService.authentication(userName, password);

                        if (user != null) {
                            session.setAttribute(SessionKey.USER_ID.getKey(), user.getUserId());
                            session.setAttribute(SessionKey.USER_MAIL_ID.getKey(), user.getEmailId());
                            session.setAttribute(SessionKey.USER.getKey(), user);

                            Set<UserRole> userRole = TeaminologyService.getUserRole(user.getUserId());
                            session.setAttribute(SessionKey.USER_ROLE.getKey(), userRole);
                            Role roleObj = TeaminologyService.getRoleIdByLabel(RoleEnum.SUPER_ADMIN.getValue());

                            session.setAttribute(SessionKey.SUPER_ADMIN.getKey(), Boolean.valueOf(false));
                            logger.info("Set up SUPER_ADMIN to false");
                            if (null != userRole && userRole.size() > 0) {
                                for (UserRole userroleObj : userRole) {
                                    if (userroleObj.getRole().getRoleId().intValue() == roleObj.getRoleId().intValue()) {
                                        session.setAttribute(SessionKey.SUPER_ADMIN.getKey(), Boolean.valueOf(true));
                                    }
                                }
                            }
                        } else {
                            rememberMeCookie.setMaxAge(0);
                        }
                    }
                }
                catch (Exception e) {
                    logger.error("Error in cookie authentication, redirecting to login page.", e);
                    if (rememberMeCookie != null) {
                        rememberMeCookie.setMaxAge(0);
                        resp.addCookie(rememberMeCookie);
                    }
                    fc.doFilter(request, response);
                }
            }
            fc.doFilter(request, response);
        }
    }

    /**
     * To get the saved cookie from the list of browser cookies
     *
     * @param req HttpServletRequest
     * @return The Remember Me cookie from the browser
     */
    public Cookie getRememberMeCookie(HttpServletRequest req) {
        Cookie rememberMeCookie = null;
        try {
            Cookie[] cookies = req.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i] != null) {
                        if (cookies[i].getName().equals(
                                SessionKey.REMEMBER_ME_COOKIE.name())) {
                            if (cookies[i].getValue() != null
                                    && cookies[i].getValue().length() > 0) {
                                return cookies[i];
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return rememberMeCookie;
    }

    @Override
    public void destroy() {
    }
}
