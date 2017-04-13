package com.teaminology.hp.servlet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import com.teaminology.hp.bo.EmailTemplates;
import com.teaminology.hp.dao.PollExpireDAO;
import com.teaminology.hp.service.IMailService;
import com.teaminology.hp.service.enums.EmailTemplateEnum;
import com.teaminology.hp.service.impl.MailServiceImpl;

/**
 * sending alert mail when user poll is going to expire Based on cron.expression
 */

 public class PollExpireCronJob {
	 
	 PollExpireDAO pollExpireDAO;
	
	public PollExpireCronJob(PollExpireDAO pollExpireDAO) {
		System.out.println("Inside PollExpireCronJob constructor." );
		this.pollExpireDAO = pollExpireDAO;
	}
	private static Logger logger = Logger.getLogger(PollExpireCronJob.class);
	/**
	 * Sending alert mail when user poll is going to expire Based on cron.expression
	 */
	@Scheduled(cron = "${cron.expression}")
	public void sendPollExpireNotificationMail() {
		IMailService mailService  = new MailServiceImpl(null,null,null,null);
		List<Object>  mailObject = pollExpireDAO.getEmailForPollExpireAlert();
		logger.info("#### Emails are to send poll alert #### "+mailObject);
		String emailSubject = EmailTemplateEnum.ALERT_POLL_EXPIRE.getValue();
		String requestTermContent = "";
		Set<String> emailIds = new HashSet<String>();
		//To store each user emailId with total # of votes pending
		Map<String, Long> emailAndTotalTerms = new HashMap<String, Long>();
		for(Object obj : mailObject) {
			Object[] emailandTotalPendingPolls = (Object[])obj;
			emailAndTotalTerms.put(emailandTotalPendingPolls[0].toString(), Long.valueOf(emailandTotalPendingPolls[1].toString()));
		}
	if(emailAndTotalTerms != null) {
		for(Map.Entry<String/* Email ids*/, Long/* total pending terms*/> entry : emailAndTotalTerms.entrySet()) {
			String email = entry.getKey();
			emailIds.add(email);
			Long totalPendingTerms = entry.getValue();
			//Body of Email
			/*requestTermContent ="<html><table style=\"width: 100%; height: 20px;\"> " +
					" <tbody><tr> <td style=\"padding-top: 30px;\" width=\"80%\"><span style=\"font-size: 36px; font-family: HP Simplified ,Arial;\"> " +
					" <strong>HP Terminology Community</strong></span></td><td style=\"padding-top: 30px;\" align=\"right\" width=\"20%\"><span> " +
					" <img src=\"http://www.hptermcommunity.com/app/hp/images/hp_logo.png\" /></span></td></tr></tbody></table> " +
					" <body><p>&nbsp;</p><p><span style=\"font-size: small; font-family: 'HP Simplified';\">Hello,</span></p> " +
					" <p><span style=\"font-size: small; font-family: 'HP Simplified';\">Your poll is going to expire in 2days, Please vote !!!</span></p> " +
					" <p><span style=\"font-size: small; font-family: 'HP Simplified';\">Total Pending Terms are : </span>" +totalPendingTerms + " </p> " +
					" <p><span style=\"font-size: small; font-family: 'HP Simplified';\">Thanks a lot for your great help and involvement in managing HP terminology.</span></p> " +
					" <p><span style=\"font-size: small; font-family: 'HP Simplified';\">&nbsp;</span></p> " +
					" <p><span style=\"font-size: small; font-family: 'HP Simplified';\">Best Regards,</span><br/><span style=\"font-size: small; font-family: 'HP Simplified';\"> " +
					" <strong>HP Terminology<br/> </strong>HP Global Brand Terminology Management team</span><br/> " +
					" <span style=\"font-size: small; font-family: 'HP Simplified';\"> Digital Publishing &amp; Operations (Globalization &amp; Translation)</span><br/> " +
					" <br/><span style=\"font-size: small; font-family: 'HP Simplified';\"> " +
					" <a href=\"https://external1.collaboration.hp.com/external/TL_Vendor_Access/new_look/terminology/terminology.aspx\">Terminology Program SharePoint</a></span></p></body></html>";*/
			if (emailIds != null && emailIds.size() > 0) {
				EmailTemplates emailTemplate = pollExpireDAO.getEmailTemplateBySubject(emailSubject);
				requestTermContent = emailTemplate.getEmailMessageContent();
				Pattern patternObj = Pattern.compile("<div style=\"font-size: small; font-family: 'HP Simplified';\"><span>Total Pending terms are:</span></div>");
				String content = emailTemplate.getEmailMessageContent();
				String htmlTmplt = "<div style=\"font-size: small; font-family: 'HP Simplified'; padding-right:3px;\"><span>Total Pending terms are: </span>"+totalPendingTerms +"</div>";
		    	Matcher matcherObj = patternObj.matcher(content);
		    	StringBuffer pollhtmlTmplt = new StringBuffer();

		    	while(matcherObj.find()) {
		    		matcherObj.appendReplacement(pollhtmlTmplt, htmlTmplt);
		    	}
		    	 matcherObj.appendTail(pollhtmlTmplt);
				 mailService.sendMailUsingTemplate(emailIds, pollhtmlTmplt.toString(), emailSubject);
			}
			emailIds.clear();
		}
	  }
	}
}