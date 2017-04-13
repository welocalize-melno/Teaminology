package com.teaminology.hp;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Contains the method(getPasswordAuthentication()) of org.enhydra.shark.toolagent.SmtpAuthenticator required for mail authentication.
 *
 * @author sarvanic
 */
public class SMTPAuthenticator extends Authenticator
{

    String username;
    String password;

    /**
     * Method to use Email authentication.
     *
     * @param userName String holding the default mail id for mailing
     * @param passWord String holding the password w.r.t the userName
     */
    public SMTPAuthenticator(String userName, String passWord) {
        username = userName;
        password = passWord;
    }

    /**
     * Method to use get password authentication.
     *
     * @return PasswordAuthentication
     */
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}
