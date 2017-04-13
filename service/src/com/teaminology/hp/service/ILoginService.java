package com.teaminology.hp.service;

import java.util.Set;

import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserRole;

/**
 * Contains User login method prototypes.
 *
 * @author sarvanic
 */
public interface ILoginService
{

    /**
     * To authenticate user
     *
     * @param userName Logged user name
     * @param password Logged user password
     * @return User w.r.t the userName and password
     */
    public User authentication(String userName, String password);

    /**
     * To get user role
     *
     * @param userId Integer to filter the user roles
     * @return set of user roles  w.r.t userId
     */
    public Set<UserRole> getUserRole(Integer userId);

}
