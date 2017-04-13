package com.teaminology.hp.service;

import java.util.List;
import java.util.Set;

import com.teaminology.hp.bo.EmailTemplates;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.data.Invite;

/**
 * Contains method prototypes to send mails.
 *
 * @author sarvanic
 */
public interface IMailService
{

    /**
     * To Invite a user for joining the community
     *
     * @param mailIds    Holding the mail id's and email template
     * @param userMailId Mail id of the user inviting
     * @return The status of the invited mail
     */
    public String inviteUser(Invite mailIds, String userMailId);


    /**
     * To send a mail for a new term request
     *
     * @param termInfo TermInformation of the new term
     * @param user     User requesting for a new term
     */
    public void newTermRequest(TermInformation termInfo, User user);


    /**
     * To get email template data
     *
     * @return List of EmailTemplates objects
     */
    public List<EmailTemplates> getEmailTemplates();


    /**
     * Mail for requesting new password
     *
     * @param user User that raises the request
     */
    public void requestPassword(User user);

    /**
     * To send a mail using template  for a new term request change
     *
     * @param requestTermContent content to be requested
     * @param subject
     * @param emailIds           list-list  the mail id of the user
     */
    public void sendMailUsingTemplate(Set<String> emailIds, String requestTermContent, String emailSubject);

    /**
     * To send a mail for a new register user
     *
     * @param user User requesting for a new term
     */
    public void newUserRegistered(User user);

    /**
     * To send a mail for adding new user
     *
     * @param user User requesting for a new term
     */
    public void addNewUser(User user);
}
