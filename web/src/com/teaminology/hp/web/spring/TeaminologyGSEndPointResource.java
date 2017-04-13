package com.teaminology.hp.web.spring;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.teaminology.hp.Utils;
import com.teaminology.hp.Utils.SortOrder;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.Status;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.service.IGSService;
import com.teaminology.hp.service.IImportExportService;
import com.teaminology.hp.service.ILoginService;
import com.teaminology.hp.service.ILookUpService;
import com.teaminology.hp.service.IMailService;
import com.teaminology.hp.service.ITermDetailsService;
import com.teaminology.hp.service.IUserService;
import com.teaminology.hp.service.enums.SessionKey;
import com.teaminology.hp.service.enums.StatusLookupEnum;


@Controller
@RequestMapping(value = "/teaminology/gs")
public class TeaminologyGSEndPointResource {
	
    private Logger logger = Logger.getLogger(TeaminologyController.class);

    private ITermDetailsService termsService;
    private IUserService userService;
    private ILookUpService lookUpService;
    private IMailService mailService;
    private IImportExportService impExpService;
    private IGSService gsService;
    private ILoginService loginService;

    @Autowired
    public TeaminologyGSEndPointResource(
            ITermDetailsService termsService, IUserService userService,
            ILookUpService lookUpService, IMailService mailService,
            IImportExportService impExpService, IGSService gsService,
            ILoginService loginService) {
        this.termsService = termsService;
        this.userService = userService;
        this.lookUpService = lookUpService;
        this.mailService = mailService;
        this.impExpService = impExpService;
        this.gsService = gsService;
        this.loginService = loginService;
    }
	
    
    @RequestMapping(method = RequestMethod.PUT, value = "/{versionID}/companies/{companyID}/termbases/{termbaseID}/terms/{termID}")
    @ResponseBody
    public List<Member> putDataToGS(@PathVariable Integer versionId, @PathVariable Integer companyId,  @PathVariable Integer termbaseId,  
    		@PathVariable Integer termId, HttpServletRequest request) {

//    	try { 
    		
    		Company company = null;
    		String accessToken = null;
    		if (companyId == null && company != null) {
    			company  = lookUpService.getCompanyById(companyId);
    		}
    		if(company != null) {
    			accessToken = gsService.getAccessToken(company);
    		}
    		
    		
    		
    		/*List<Member> membersList = termsService.getAllBoardMembersByLanguage(companyId,userLangId);
    		List<Member> finalMembersList = new ArrayList<Member>();

    		if (membersList != null && !membersList.isEmpty() && membersList.size() > 0) {

    			for (Member teamMember : membersList) {

    				float accuracy = 0;
    				DecimalFormat df = new DecimalFormat("#.##");
    				Map<String, BigInteger> userAccuracyRate = userService.getUserAccuracyRate(teamMember.getUserId(), statusId, companyId);
    				float finalizedTerm = userAccuracyRate.get("finalizedTerm").floatValue();
    				float votedTerms = userAccuracyRate.get("votedTerms").floatValue();
    				if (finalizedTerm != 0 && votedTerms != 0) {
    					accuracy = finalizedTerm / votedTerms;
    					accuracy = accuracy * 100;
    				}
    				teamMember.setAccuracy(new BigDecimal(df.format(accuracy)));
    				finalMembersList.add(teamMember);
    			}
    			Collections.sort(finalMembersList,
    					new Comparator<Member>()
    					{
    				public int compare(Member arg0,
    						Member arg1) {
    					return Utils.compareFloats(new Float(arg0.getAccuracy().toString()), new Float(arg1.getAccuracy().toString()), SortOrder.DESC);
    				}
    					});

    			Collections.sort(finalMembersList,
    					new Comparator<Member>()
    					{
    				public int compare(Member arg0,
    						Member arg1) {
    					return Utils.compareIntegers(arg0.getTotalVotes() == null ? null : new Integer(arg0.getTotalVotes().toString()), arg1.getTotalVotes() == null ? null : new Integer(arg1.getTotalVotes().toString()), SortOrder.DESC);
    				}
    					});
    			logger.info(" +++  Got " + membersList.size() + " Leader Board Members");
    			return finalMembersList;
    		} else {
    			logger.info(" +++  NULL Leader Board Members");
    		}
    	}
    	catch (Exception e) {
    		logger.error("Failed to get Leader Board Members");
    		logger.error(e,e);
    	} */
    	return null;
   }
    

}
