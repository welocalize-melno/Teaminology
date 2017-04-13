package com.teaminology.hp.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.SMTPAuthenticator;
import com.teaminology.hp.bo.DeprecatedTermInformation;
import com.teaminology.hp.bo.EmailTemplates;
import com.teaminology.hp.bo.Role;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermTranslation;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserRole;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.dao.ILookUpDAO;
import com.teaminology.hp.dao.ITeaminologyDAO;
import com.teaminology.hp.dao.IUserDAO;
import com.teaminology.hp.dao.TermDetailsDAO;
import com.teaminology.hp.data.Invite;
import com.teaminology.hp.service.IMailService;
import com.teaminology.hp.service.enums.RoleEnum;
import com.teaminology.hp.service.enums.TeaminologyProperty;

/**
 * Contains the methods to send the mails.
 *
 * @author Sarvani
 */
@Service
public class MailServiceImpl implements IMailService
{

    private Logger logger = Logger.getLogger(MailServiceImpl.class);

    private ITeaminologyDAO teaminologyDAO;
    private ILookUpDAO lookUpDAO;
    private IUserDAO userDAO;
    private TermDetailsDAO termDetailsDAO;

    private String host = TeaminologyProperty.MAIL_HOST.getValue();
    private String authEnable = TeaminologyProperty.MAIL_AUTH_ENABLE.getValue();
    private String userName = TeaminologyProperty.MAIL_AUTH_USER.getValue();
    private String password = TeaminologyProperty.MAIL_AUTH_PWD.getValue();
    private String port = TeaminologyProperty.MAIL_SMTP_PORT.getValue();
    private String companyHost=TeaminologyProperty.HOST.getValue();


    private String mailSender = TeaminologyProperty.FORGOT_PASSWORD_MAIL_SENDER.getValue();
    private String emailTemplateId = TeaminologyProperty.INVITE_USER_EMAIL_TEMPLATE.getValue();

    @Autowired
    public MailServiceImpl(ITeaminologyDAO teaminologyDAO, ILookUpDAO lookUpDAO, IUserDAO userDAO, TermDetailsDAO termDetailsDAO) {
        this.teaminologyDAO = teaminologyDAO;
        this.lookUpDAO = lookUpDAO;
        this.userDAO = userDAO;
        this.termDetailsDAO = termDetailsDAO;
    }

    /**
     * To Invite a user for joining the community
     *
     * @param invite     Holding the mail id's and email template
     * @param userMailId String the mail id of the user inviting
     * @return The status of the invited mail
     */

    @Override
    @Transactional
    public String inviteUser(Invite invite, String userMailId) {
        String status = null;
        //Company company=new Company();
        String companyContext=invite.getCompanyContext();
       // Integer companyId=company.getCompanyId();
        if (invite == null || invite.getMailIds() == null) {
            status = "failed";
            return status;
        }
        try {
            String subject = "", body = "";
            String from = StringUtil.isEmpty(userMailId) ? "admin@teaminology.com" : userMailId;
            int length = invite.getUserIds().length;
            String[] toArray = new String[length];
            Integer[] userIds = invite.getUserIds();

            if (userIds != null && length != 0) {
                for (int i = 0; i < userIds.length; i++) {
                    if (userIds[i] != null && userIds[i] != 0) {
                        User user = userDAO.getUser(userIds[i]);
                        toArray[i] = user.getEmailId();
                    }
                }
            } else {
                toArray = invite.getMailIds();
            }

            Properties properties = new Properties();
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.host", host);
            properties.put("mail.host", host);
            properties.put("mail.smtp.auth", authEnable);
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", port);


            Session session = Session.getInstance(properties, new SMTPAuthenticator(userName, password));

            EmailTemplates emailTemplate = teaminologyDAO.getEmailTemplate(invite.getEmailTemplateId());
            subject = emailTemplate.getEmailSubject();
            body = emailTemplate.getEmailMessageContent();
            Integer companyId=invite.getCompanyId();
            String root=invite.getCompanyRoot();
               if(companyId !=null  && companyId ==1){
                if (invite.getTermIds().length != 0) {
                	String link=companyHost+companyContext+"/"+"index.jsp";
                    body += "<a href='" + link + "'>Vote</a>";
                }
                     }
               else
               {
            	 String  userLinkCompany=companyHost+companyContext+"/" + root+"/"+"index.jsp";
            	   if (invite.getTermIds().length != 0) {
                       body += "<a href='" + userLinkCompany + "'>Vote</a>";
                   }
               }
            if (emailTemplate != null) {
            	String body1=null;
                for (String to : toArray) {
                    if (StringUtil.isEmpty(to))
                        continue;
                    if(emailTemplate.getEmailTemplateId().intValue()==19){
                    	subject = emailTemplate.getEmailSubject();
                    	body1 = emailTemplate.getEmailMessageContent();
                        Integer companyId1=invite.getCompanyId();
                        String root1=invite.getCompanyRoot();
                           if(companyId1 !=null  && companyId1 ==1){
                            if (invite.getTermIds().length != 0) {
                            	String link=companyHost+companyContext+"/"+"index.jsp";
                            	body1 += "<a href='" + link + "'>Vote</a>";
                            }
                                 }
                           else
                           {
                        	 String  userLinkCompany=companyHost+companyContext+"/" + root1+"/"+"index.jsp";
                        	   if (invite.getTermIds().length != 0) {
                        		   body1 += "<a href='" + userLinkCompany + "'>Vote</a>";
                               }
                           }
                    	Map<String, List<TermInformation>> userTermList = invite.getUserPollTerms();

                    	if(userTermList!=null){

                    		List<TermInformation> termObjList = userTermList.get(to);

                    		if(termObjList!=null && termObjList.size()>0){
                    			body1 = addPollTermsToEmail(body1,termObjList);
                    		}
                    	}

                    }
                    logger.info("Sending invite user email to user -- " + to);
                    try {

                        MimeMessage message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(from));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                     // Setting the Subject and Content Type
                        message.setSubject(subject,"UTF-8");
                     // Create a message part to represent the body text
                        BodyPart messageBodyPart = new MimeBodyPart();
                        if(emailTemplate.getEmailTemplateId().intValue()==19){
                        	messageBodyPart.setContent( body1, "text/html; charset=utf-8" );
                        } else {
                        	messageBodyPart.setContent( body, "text/html; charset=utf-8" );
                        }
                     // use a MimeMultipart
                        Multipart multipart = new MimeMultipart();
                     // add the message body to the mime message
                        multipart.addBodyPart(messageBodyPart );
                     // Put all message parts in the message
                        message.setContent( multipart );
                     //message.setContent(body, "text/html");
                        Transport trans = session.getTransport("smtp");

                        trans.connect(host, userName, password);

                        trans.sendMessage(message, message.getAllRecipients());
                        body1 = null;
                        logger.info("Sending invite user email to user " + to + " successfully.");
                    }
                    catch (Exception e) {
                        logger.error("Failed to send invite user email to user " + to, e);
                    }
                }
                status = "success";
            } else {
                logger.info("Failed to load email template for inviting user");
                status = "failed";
            }
        }
        catch (Exception e) {
            logger.error("Failed to send inviting user email.", e);
            status = "failed";
        }
        return status;
    }

    /**
     * To send a mail for a new term request
     *
     * @param termInfo TermInformation of the new term
     * @param user     User requesting for a new term
     */
    @Override
    public void newTermRequest(TermInformation termInfo, User user) {
        if (termInfo == null || user == null)
            return;

        StringBuilder sbNewTermData = new StringBuilder();
        Set<DeprecatedTermInformation> deprecatedTermInfoset = new HashSet<DeprecatedTermInformation>();
        String subject = "Requesting for new term";
        List<UserRole> adminUsers = userDAO.getAdminUsers();
        String to = "";
        String from = user.getEmailId();
        sbNewTermData.append("<fieldset>");
        sbNewTermData.append("<legend> User Information </legend>");
        sbNewTermData.append("<table>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td width='20%' style='color:Gray;text-align:right;'>");
        sbNewTermData.append("Requestor:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td>");
        sbNewTermData.append(user.getUserName());
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td width='20%' style='color:Gray;text-align:right;'>");
        sbNewTermData.append("Email Address:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td>");
        sbNewTermData.append(user.getEmailId());
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("</table>");
        sbNewTermData.append("</fieldset>");

        sbNewTermData.append("<br/>");

        sbNewTermData.append("<div style='width: 100%'>");
        sbNewTermData.append("<fieldset>");
        sbNewTermData.append("<legend> Term information details </legend>");

        sbNewTermData.append("<table>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td style='color:Gray;text-align:right;' valign='top'>");
        sbNewTermData.append("Source:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td valign='top'>");
        sbNewTermData.append(termInfo.getTermBeingPolled());
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td style='color:Gray;text-align:right;' valign='top'>");
        sbNewTermData.append("Translation:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td valign='top'>");
        sbNewTermData.append(termInfo.getSuggestedTerm());
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td style='color:Gray;text-align:right;' valign='top' >");
        sbNewTermData.append("Target Language:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td valign='top'>");

        if (termInfo.getSuggestedTermLanguages() != null && termInfo.getSuggestedTermLanguages().length > 0) {
            String targetLangs = "";
            String[] suggestedTermLanguages = termInfo.getSuggestedTermLanguages();
            for (int i = 0; i < suggestedTermLanguages.length; i++) {
                if (i == 0) {
                    targetLangs = suggestedTermLanguages[i];
                } else {
                    targetLangs = targetLangs + "," + suggestedTermLanguages[i];
                }
            }
            sbNewTermData.append(targetLangs);
        }
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td style='color:Gray;text-align:right;' valign='top'>");
        sbNewTermData.append("Form:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td valign='top'>");
        if (termInfo.getTermForm() != null && termInfo.getTermForm().getFormId() != 0) {
            sbNewTermData.append(termInfo.getTermForm().getFormName());
        }
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td style='color:Gray;text-align:right;' valign='top'>");
        sbNewTermData.append("Part of Speech:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td valign='top'>");
        if (termInfo.getTermPOS() != null && termInfo.getTermPOS().getPartsOfSpeechId() != 0) {
            sbNewTermData.append(termInfo.getTermPOS().getPartOfSpeech());
        }
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td style='color:Gray;text-align:right;' valign='top'>");
        sbNewTermData.append("Term Category:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td valign='top'>");
        if (termInfo.getTermCategory() != null && termInfo.getTermCategory().getCategoryId() != 0) {
            sbNewTermData.append(termInfo.getTermCategory().getCategory());
        }
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td width='20%' style='color:Gray;text-align:right;' valign='top'>");
        sbNewTermData.append("Term Notes:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td valign='top'>");
        sbNewTermData.append(termInfo.getTermNotes());
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td width='20%'  style='color:Gray;text-align:right;' valign='top'>");
        sbNewTermData.append("Concept definition:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td valign='top'>");
        sbNewTermData.append(termInfo.getConceptDefinition());
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("<tr>");
        sbNewTermData.append("<td  width='20%' style='color:Gray;text-align:right;' valign='top'>");
        sbNewTermData.append("Contextual Example:");
        sbNewTermData.append("</td>");
        sbNewTermData.append("<td valign='top'>");
        sbNewTermData.append(termInfo.getTermUsage());
        sbNewTermData.append("</td>");
        sbNewTermData.append("</tr>");
        sbNewTermData.append("</table>");
        sbNewTermData.append("</fieldset>");
        sbNewTermData.append("</div>");
        //Deprecated  FieldSet
        sbNewTermData.append("<fieldset>");

        Integer sourceCount = 1;
        Integer targetCount = 1;
        if (termInfo.getDeprecatedTermInfo() != null && termInfo.getDeprecatedTermInfo().size() > 0) {
            sbNewTermData.append("<legend> Deprecated Term  Information </legend>");
            sbNewTermData.append("<table>");
            deprecatedTermInfoset = termInfo.getDeprecatedTermInfo();
            for (DeprecatedTermInformation deprecatedTermInformation : deprecatedTermInfoset) {
                sbNewTermData.append("<tr>");
                sbNewTermData.append("<td style='color:Gray;text-align:right;'>");
                sbNewTermData.append("Deprecated Source").append(sourceCount++).append(":");
                sbNewTermData.append("</td>");
                sbNewTermData.append("<td>");
                if (deprecatedTermInformation.getDeprecatedSource() != null) {
                    sbNewTermData.append(deprecatedTermInformation.getDeprecatedSource());
                } else {
                    sbNewTermData.append("");
                }
                sbNewTermData.append("</td>");
                sbNewTermData.append("<td style='color:Gray;text-align:right;'>");
                sbNewTermData.append("Deprecated Target").append(targetCount++).append(":");
                sbNewTermData.append("</td>");
                sbNewTermData.append("<td>");
                if (deprecatedTermInformation.getDeprecatedTarget() != null) {
                    sbNewTermData.append(deprecatedTermInformation.getDeprecatedTarget());
                } else {
                    sbNewTermData.append("");
                }
                sbNewTermData.append("</td>");
                sbNewTermData.append("</tr>");
            }
            sbNewTermData.append("</table>");
        }

        sbNewTermData.append("</fieldset>");
        sbNewTermData.append("</div>");

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", host);
        properties.put("mail.host", host);
        properties.put("mail.smtp.auth", authEnable);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", port);

        Session session = Session.getInstance(properties, new SMTPAuthenticator(userName, password));

        if (StringUtil.isEmpty(from))
            from = "admin@teaminology.com";

        try {
            for (int i = 0; i < adminUsers.size(); i++) {
                User admin = userDAO.getUser(adminUsers.get(i).getUserId());
                to = admin.getEmailId();
                if (StringUtil.isEmpty(to))
                    continue;

                logger.info("Sending new term request email to user " + to);
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(from));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    message.setSubject(subject, "UTF-8");
                    message.setText(sbNewTermData.toString(), "UTF-8");
                    message.setHeader("Content-Type", "text/html; charset=UTF-8");

                    Transport trans = session.getTransport("smtp");

                    trans.connect(host, userName, password);

                    trans.sendMessage(message, message.getAllRecipients());
                    logger.info("Sending new term request email to user " + to + " successfully.");
                } catch (Exception e) {
                    logger.error("Failed to send new term request email to user " + to, e);
                }
            }
        }
        catch (Exception e) {
            logger.error("Failed to send new term request email.", e);
        }
    }

    /**
     * To get email template data
     *
     * @return List of EmailTemplates objects
     */
    @Override
    public List<EmailTemplates> getEmailTemplates() {
        List<EmailTemplates> email_template_list = new ArrayList<EmailTemplates>();
        try {
            email_template_list = teaminologyDAO.getEmailTemplates();
        }
        catch (Exception e) {
            logger.error("Error in getting email template data", e);
        }
        return email_template_list;
    }


    /**
     * Mail for requesting new password
     *
     * @param user User that raises the request
     */
    @Override
    public void requestPassword(User user) {
        StringBuilder sbNewRequestData = new StringBuilder();
        String subject = "Requested new password";
        String to = "";
        String from = StringUtil.isEmpty(mailSender) ? "admin@teaminology.com" : mailSender;

        sbNewRequestData.append("Your Teaminology login password is reset to : " + user.getPassword());

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", host);
        properties.put("mail.host", host);
        properties.put("mail.smtp.auth", authEnable);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", port);

        Session session = Session.getInstance(properties, new SMTPAuthenticator(userName, password));

        try {
            to = user.getEmailId();
            if (StringUtil.isEmpty(to)) {
                logger.info("User " + user.getUserName() + " does not have any email address.");
                return;
            }

            logger.info("Sending request new password email to user " + user.getUserName());
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(sbNewRequestData.toString(), "text/html");
            Transport trans = session.getTransport("smtp");

            trans.connect(host, userName, password);

            trans.sendMessage(message, message.getAllRecipients());
            logger.info("Sending request new password email to user " + user.getUserName() + " successfully.");
        }
        catch (Exception e) {
            logger.error("Failed to send request new password email to user " + user.getUserName(), e);
        }
    }

    /**
     * To send a mail using template  for a new term request change
     *
     * @param requestTermContent content to be requested
     * @param emailSubject
     * @param emailIds           list-list  the mail id of the user
     */
    @Override
    public void sendMailUsingTemplate(Set<String> emailIds, String requestTermContent, String emailSubject) {
        String to = "";
        try {
            String from = StringUtil.isEmpty(mailSender) ? "admin@teaminology.com" : mailSender;

            Properties properties = new Properties();
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.host", host);
            properties.put("mail.host", host);
            properties.put("mail.smtp.auth", authEnable);
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", port);

            Session session = Session.getInstance(properties, new SMTPAuthenticator(userName, password));
            for (String usrEmailIds : emailIds) {
                if (StringUtil.isEmpty(usrEmailIds))
                    continue;

                logger.info("Send " +emailSubject + " email to user  " + usrEmailIds);
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                to = usrEmailIds;
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                message.setSubject(emailSubject, "UTF-8");
                message.setText(requestTermContent.toString(), "UTF-8");
                message.setHeader("Content-Type", "text/html; charset=UTF-8");

                Transport trans = session.getTransport("smtp");

                trans.connect(host, userName, password);

                trans.sendMessage(message, message.getAllRecipients());
                logger.info("Send " +emailSubject + " email to user " + usrEmailIds + " successfully.");
            }
        }
        catch (Exception e) {
            logger.error("Failed to send request change email to user " + to, e);
        }
    }


    /**
     * To send a mail to the Admin regarding new UserRegistration
     */
    @Override
    public void newUserRegistered(User user) {
        String subject = "Confirmation  for new User Registration";
        //	List<UserRole> adminUsers = userDAO.getAdminUsers();
        Integer companyId = null;
        if (user != null && user.getCompany() != null) {
            companyId = user.getCompany().getCompanyId();
        }
        List<String> emailIds = new ArrayList<String>();
        //added Method to get company admins
        Role role = userDAO.getRoleIdByLabel(RoleEnum.COMPANY_ADMIN.getValue());
        List<User> companyAdminUsers = userDAO.getCompanyUsersByRole(companyId, role.getRoleId());
        if (companyAdminUsers != null && !companyAdminUsers.isEmpty()) {
            for (User userObj : companyAdminUsers) {
                emailIds.add(userObj.getEmailId());
            }
        }
        String from = user.getEmailId();
        try {
            String registrationEmail = TeaminologyProperty.NEW_USER_REGISTRATION_EMAIL.getValue();
            //For HP company
            if (companyId.intValue() == 2)
                registrationEmail = TeaminologyProperty.NEW_USER_REGISTRATION_EMAIL_HP.getValue();

            sendMail(emailIds, registrationEmail, from, subject);
        }
        catch (Exception e) {
            logger.error("Failed to send new user register email to user.", e);
        }
    }

    /**
     * To send a mail to the Admin regarding new UserRegistration
     */
    @Override
    public void addNewUser(User user) {
        if (user == null)
            return;

        String subject = "A New User Is Added";
        Integer companyId = null;
        if (user != null && user.getCompany() != null) {
            companyId = user.getCompany().getCompanyId();
        }
        List<String> emailIds = new ArrayList<String>();
        //added Method to get company admins
        Role role = userDAO.getRoleIdByLabel(RoleEnum.COMPANY_ADMIN.getValue());
        List<User> companyAdminUsers = userDAO.getCompanyUsersByRole(companyId, role.getRoleId());
        if (companyAdminUsers != null && !companyAdminUsers.isEmpty()) {
            for (User userObj : companyAdminUsers) {
                emailIds.add(userObj.getEmailId());
            }
        }
        String from = user.getEmailId();
        try {
            StringBuilder content = new StringBuilder();
            content.append("A new user is created by admin user.<br>");
            content.append("****************************************<br>");
            content.append("Username: ").append(user.getUserName()).append("<br>");
            content.append("Email: " + user.getEmailId()).append("<br>");

            String mailContent = content.toString();
            sendMail(emailIds, mailContent, from, subject);

            subject = "Welcome to Teaminology Community";
            if (emailIds != null && !emailIds.isEmpty()) {
                from = (String) emailIds.get(0);
                emailIds = new ArrayList<String>();
                emailIds.add(user.getEmailId());

                content = new StringBuilder();
                content.append("Welcome to Teaminology Community!<br>");
                content.append("You account to access Teaminology website is <br>");
                content.append("Username : " + user.getUserName() + "<br>");
                content.append(" Password : " + user.getPassword() + "<br><br>");
                content.append("**************************************<br>");
                content.append("If you forget your password, you can get it by using 'Forget password' or by contacting the administrator.<br>");
                content.append("<br>");
                content.append("Best regards,<br><br>");
                content.append("Teaminology");

                sendMail(emailIds, content.toString(), from, subject);
            }
        }
        catch (Exception e) {
            logger.error("Failed to send adding new user email.", e);
        }
    }

    private void sendMail(List<String> toMails, String mailText, String from, String subject) {
        if (toMails == null || toMails.size() == 0 || StringUtil.isEmpty(subject) || StringUtil.isEmpty(mailText))
            return;

        from = StringUtil.isEmpty(from) ? mailSender : from;

        try {
            Properties properties = new Properties();
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.host", host);
            properties.put("mail.host", host);
            properties.put("mail.smtp.auth", authEnable);
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", port);

            Session session = Session.getInstance(properties, new SMTPAuthenticator(userName, password));

            Set<String> toSendMails = new HashSet<String>(toMails);


            for (String to : toSendMails) {
                if (StringUtil.isEmpty(to))
                    continue;

                logger.info("Sending email to user " + to + " with subject [" + subject + "]");
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(from));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    message.setSubject(subject, "UTF-8");
                    message.setText(mailText, "UTF-8");
                    message.setHeader("Content-Type", "text/html; charset=UTF-8");

                    Transport trans = session.getTransport("smtp");
                    trans.connect(host, userName, password);

                    trans.sendMessage(message, message.getAllRecipients());
                    logger.info("Sending email to user " + to + " with subject [" + subject + "] successfully.");
                } catch (Exception e) {
                    logger.error("Failed to send email to user " + to + " with subject [" + subject + "]", e);
                }
            }
        }
        catch (Exception e) {
            logger.error("Failed to send email to user.", e);
        }
    }

    /**
     * TNG-81 or HPTC-40
     * This method used to add polling terms in email template
     * @param emailTemplate
     * @param termList
     */
    public String addPollTermsToEmail(String emailTemplate, List<TermInformation> termsList) {

       //Table for showing term details in the email.
    	String  htmlTmplt = " <table width=\"100%\" style=\"border: 1px solid black;border-collapse: collapse;\"> "  +
    			" <thead> <tr id=\"newtable\"> " +
    			" <th width=\"20%\" style=\"border: 1px solid black;background-color:#007dba; color:white;\"> "+
    			" <div style=\"padding-left:10px;text-align:left;\"> English Term </div></th>" +
    			" <th width=\"20%\" style=\"border: 1px solid black;background-color:#007dba; color:white;\">" +
    			" <div style=\"padding-left:10px;text-align:left;\"> Language </div></th>" +
    			" <th width=\"20%\" style=\"border: 1px solid black;background-color:#007dba; color:white;\">" +
    			" <div style=\"padding-left:10px;text-align:left;\"> Proposed Translation </div> </th><th width=\"20%\" style=\"background-color:#007dba; color:white;\">" +
    			" <div style=\"padding-left:10px;text-align:left;\"> Alternative Translation </div></th></tr> </thead> "+
    			"<tbody>";

    	String pollTerms = "";

		   if(termsList != null) {
		    	for(TermInformation term : termsList) {
		    		 String alterSug="";
			    		if(term != null) {
			    			//Getting all alternative translations of a specific termId.
			    			List<TermTranslation> suggestions =(List<TermTranslation>) termDetailsDAO.getAlternativeTranslations(term.getTermId());
			    			if(!(suggestions.isEmpty()) && suggestions != null) {
				                for(TermTranslation termTranslation : suggestions) {
				                	if(termTranslation != null) {
				                		//Storing all alternative suggestions in a string called  alterSug with comma separated.
				                		if((!termTranslation.getSuggestedTerm().equalsIgnoreCase(term.getSuggestedTerm()))) {
				                			alterSug = alterSug + termTranslation.getSuggestedTerm() + ", ";
				                		}
				                	}
				                }
				            }
			    		}
			    		//Deleting last comma in a string.
			    		if(!alterSug.isEmpty() && alterSug != null) {
			    			alterSug= alterSug.substring(0, alterSug.lastIndexOf(","));
			    		}
			    		//Appending all term details.
			    		String suggestedTerm = term.getSuggestedTerm()== null ? "" : term.getSuggestedTerm();
		    		pollTerms = pollTerms + "<tr>" +
		    				" <td style=\"border: 1px solid black;font-size: medium;vertical-align:top;\">" +
		    				" <div style=\"padding-left:10px;text-align:left;\">" + term.getTermBeingPolled() +"</div></td>" +
		    				" <td style=\"border: 1px solid black; font-size: medium;vertical-align:top;\">" +
		    				" <div style=\"padding-left:10px;text-align:left;\">" + getLanguageById(term.getSuggestedTermLangId()) +"</div></td> " +
		    				" <td style=\"border: 1px solid black; font-size: medium ;vertical-align:top;\">" +
		    				" <div style=\"padding-left:10px;text-align:left;\">" + suggestedTerm + "</div></td> " +
		    				" <td style=\"border: 1px solid black; font-size: medium ;vertical-align:top;\">" +
		    				" <div style=\"padding-left:10px;text-align:left;\"> " + alterSug + "</div></td></tr>";
		    	}
		    }

    	pollTerms = pollTerms + "</tbody></table>";

    	htmlTmplt = htmlTmplt + pollTerms;

    	Pattern patternObj = Pattern.compile("<div style=\"font-weight: bold;\"><span>Terms to poll will be shown here</span></div>");
    	Matcher matcherObj = patternObj.matcher(emailTemplate);

    	StringBuffer pollhtmlTmplt = new StringBuffer();

    	while(matcherObj.find()) {
    		matcherObj.appendReplacement(pollhtmlTmplt, htmlTmplt);
    	}

    	matcherObj.appendTail(pollhtmlTmplt);

    	return pollhtmlTmplt.toString();
    }

    public String getLanguageById(Integer langId){

    	String langLabel = null;

    	List<Language> allLanguages = lookUpDAO.getLanguages();

    	for(Language lang : allLanguages) {

        	if(langId.intValue() == lang.getLanguageId().intValue()) {
        		langLabel = lang.getLanguageLabel();
        		break;
        	}
        }
    	return langLabel;
    }
}
