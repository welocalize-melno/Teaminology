package com.teaminology.hp.dao;

import java.util.List;

import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserRole;

/**
 * Contains interface methods to authenticate user.
 *
 * @author sarvanic
 */
public interface ILoginDAO
{
  
    /**
     * To authenticate user
     *
     * @param userName Logged user name
     * @param password Logged user password
     * @return User
     */

    public User authentication(String userName, String password);

    /**
     * To get user role  for a userId
     *
     * @param userId Integer to get user role
     * @return List of user role w.r.t the userId
     */
    public List<UserRole> getUserRole(Integer userId);
}
