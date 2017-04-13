package com.teaminology.hp.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teaminology.hp.bo.User;
import com.teaminology.hp.service.ILoginService;

/**
 * Contains methods to authenticate user login.
 *
 * @author sarvani
 */
@Service
public class LoginService
{
    private static ILoginService loginService;

    @Autowired
    public LoginService(ILoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * To authenticate user
     *
     * @param userName Logged user name
     * @param password Logged user password
     * @return User w.r.t the userName and password
     */
    public static User authentication(String userName, String password) {
        return loginService.authentication(userName, password);
    }
}
