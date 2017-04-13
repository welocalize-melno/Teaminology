package com.teaminology.hp.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserRole;
import com.teaminology.hp.dao.ILoginDAO;
import com.teaminology.hp.service.ILoginService;


/**
 * Used to check login authentication details.
 *
 * @author sarvanic
 */
@Service
public class LoginServiceImpl implements ILoginService
{
    private Logger logger = Logger.getLogger(LoginServiceImpl.class);
    private ILoginDAO loginDAO;


    @Autowired
    public LoginServiceImpl(ILoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }

    @Transactional(readOnly = true)
    @Qualifier("txManager")

    /**
     * To authenticate user
     * @param userName String holding the logged user name
     * @param password String holding the logged password
     * @return User w.r.t the userName and password
     */
    public User authentication(String userName, String password) {
        return loginDAO.authentication(userName, password);
    }

    /**
     * To get user role  for a userId
     *
     * @param userId Integer to get user role
     * @return Set of user role w.r.t the userId
     */
    public Set<UserRole> getUserRole(Integer userId) {
        if (userId == null || userId.intValue() < 1)
            return null;

        List<UserRole> userRoleList = null;
        Set<UserRole> userRoleSet = null;

        try {
            userRoleList = loginDAO.getUserRole(userId);
            if (userRoleList == null) {
                logger.debug("Cannot get roles for user : " + userId);
            }
            userRoleSet = new HashSet<UserRole>(userRoleList);
        }
        catch (Exception e) {
            logger.error("Error in getting user roles. ", e);
            return null;
        }
        return userRoleSet;
    }
}
