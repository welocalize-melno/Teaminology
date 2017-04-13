package com.teaminology.hp.web.spring;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.teaminology.hp.TeaminologyService;
import com.teaminology.hp.Utils;
import com.teaminology.hp.Utils.SortOrder;
import com.teaminology.hp.bo.CompanyTransMgmt;
import com.teaminology.hp.bo.DeprecatedTermInformation;
import com.teaminology.hp.bo.EmailTemplates;
import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.FileUploadStatus;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.GlobalsightTerms;
import com.teaminology.hp.bo.Menu;
import com.teaminology.hp.bo.Role;
import com.teaminology.hp.bo.RoleMenuMgmt;
import com.teaminology.hp.bo.RolePrivileges;
import com.teaminology.hp.bo.SubMenu;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermTranslation;
import com.teaminology.hp.bo.TermVoteMaster;
import com.teaminology.hp.bo.TmProfileInfo;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserLanguages;
import com.teaminology.hp.bo.UserRole;
import com.teaminology.hp.bo.lookup.Category;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.ConceptCategory;
import com.teaminology.hp.bo.lookup.ContentType;
import com.teaminology.hp.bo.lookup.Domain;
import com.teaminology.hp.bo.lookup.Form;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.bo.lookup.PartsOfSpeech;
import com.teaminology.hp.bo.lookup.ProductGroup;
import com.teaminology.hp.bo.lookup.Program;
import com.teaminology.hp.bo.lookup.Status;
import com.teaminology.hp.bo.lookup.VoteConfig;
import com.teaminology.hp.data.CSVImportStatus;
import com.teaminology.hp.data.DataTable;
import com.teaminology.hp.data.GSCredintials;
import com.teaminology.hp.data.GSJobObject;
import com.teaminology.hp.data.GSSearchObject;
import com.teaminology.hp.data.HistoryDetailsData;
import com.teaminology.hp.data.Invite;
import com.teaminology.hp.data.LanguagePiChartData;
import com.teaminology.hp.data.LanguageReportData;
import com.teaminology.hp.data.LanguageReportTable;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.PollTerms;
import com.teaminology.hp.data.PrivilegesByMenu;
import com.teaminology.hp.data.QueryAppender;
import com.teaminology.hp.data.TMProfileTermsResult;
import com.teaminology.hp.data.TermAttributes;
import com.teaminology.hp.data.TermDetails;
import com.teaminology.hp.data.TermRequestChange;
import com.teaminology.hp.data.Terms;
import com.teaminology.hp.data.TmDetails;
import com.teaminology.hp.data.UserComment;
import com.teaminology.hp.data.UserStatus;
import com.teaminology.hp.data.UsersChartData;
import com.teaminology.hp.data.VotingStatus;
import com.teaminology.hp.gs.Jobs;
import com.teaminology.hp.gs.LocalePairInformation;
import com.teaminology.hp.gs.TMProfileInformation;
import com.teaminology.hp.gs.Tasks;
import com.teaminology.hp.gs.TermbaseInformation;
import com.teaminology.hp.service.IGSService;
import com.teaminology.hp.service.IImportExportService;
import com.teaminology.hp.service.ILoginService;
import com.teaminology.hp.service.ILookUpService;
import com.teaminology.hp.service.IMailService;
import com.teaminology.hp.service.ITermDetailsService;
import com.teaminology.hp.service.IUserService;
import com.teaminology.hp.service.enums.CompanySuffixEnum;
import com.teaminology.hp.service.enums.EmailTemplateEnum;
import com.teaminology.hp.service.enums.ImportGlobalSightExecutor;
import com.teaminology.hp.service.enums.IndexTypeEnum;
import com.teaminology.hp.service.enums.MenuEnum;
import com.teaminology.hp.service.enums.PrivilegeEnum;
import com.teaminology.hp.service.enums.RoleEnum;
import com.teaminology.hp.service.enums.SessionKey;
import com.teaminology.hp.service.enums.StatusLookupEnum;
import com.teaminology.hp.service.enums.TeaminologyEnum;
import com.teaminology.hp.service.enums.TeaminologyPage;
import com.teaminology.hp.service.enums.TeaminologyProperty;
import com.teaminology.hp.service.thread.ImportGlobalSightThread;
import com.teaminology.hp.web.utils.Pair;
import com.teaminology.hp.web.utils.ParamKeyEnum;

/**
 * Main Controller of the application, which controls the Model and View.
 */
@Controller
@RequestMapping(value = "/teaminology")
public class TeaminologyController
{
    private Logger logger = Logger.getLogger(TeaminologyController.class);

    private ITermDetailsService termsService;
    private IUserService userService;
    private ILookUpService lookUpService;
    private IMailService mailService;
    private IImportExportService impExpService;
    private IGSService gsService;
    private ILoginService loginService;

    @Autowired
    public TeaminologyController(
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

    /**
     * To get Leader Board Members
     *
     * @param request HttpServletRequest
     * @return List of Leader Board Members
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getMembers")
    public
    @ResponseBody
    List<Member> getBoardMembers(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            String statusName = StatusLookupEnum.APPROVED.getValue();
            Status status = lookUpService.getStatusIdByLabel(statusName);
            Integer statusId = status.getStatusId();
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            List<Member> membersList = termsService.getBoardMembers(companyId);
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
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getBoardMembersByLanguage/{userLangId}")
    public
    @ResponseBody
    List<Member> getBoardMembersByLanguage(@PathVariable String userLangId,HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            String statusName = StatusLookupEnum.APPROVED.getValue();
            Status status = lookUpService.getStatusIdByLabel(statusName);
            Integer statusId = status.getStatusId();
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            List<Member> membersList = termsService.getAllBoardMembersByLanguage(companyId,userLangId);
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
        }
        return null;
    }
    
    
    /**
     * To get HP Teaminology members
     *
     * @param request HttpServletRequest
     * @return List of HP Teaminology members
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getHPMembers")
    public
    @ResponseBody
    List<Member> getHPMembers(HttpServletRequest request) {
        try {
            List<Member> hp_members_list = userService.getHPMembers();
            if (hp_members_list != null && !hp_members_list.isEmpty()
                    && hp_members_list.size() > 0) {
                logger.info(" +++  Got " + hp_members_list.size()
                        + " Leader Board Members");
                return hp_members_list;
            } else {
                logger.info(" +++  NULL Leader Board Members");
            }
        }
        catch (Exception e) {
            logger.error("Failed to Leader Board Members");
            logger.error(e,e);
        }
        return null;
    }

    /**
     * To get total terms in glossary
     *
     * @param request HttpServletRequest
     * @return Integer holding the total count of terms in glossary
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTotalGlossaryTerms")
    public
    @ResponseBody
    Integer getTotalTermsInGlossary(HttpServletRequest request) {
        int totalTermsInGlossary = 0;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
        Integer companyId = null;
        Set<Integer> companyIds = null;
        Set<CompanyTransMgmt> companyTransMgmtUsers = new HashSet<CompanyTransMgmt>();
        if (user != null) {
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
            companyTransMgmtUsers = userService.getCompanyTransMgmtUsers(user.getUserId());
            if (companyTransMgmtUsers != null && !companyTransMgmtUsers.isEmpty()) {
                companyIds = new HashSet<Integer>();
                for (CompanyTransMgmt companyTransMgmtObj : companyTransMgmtUsers) {
                    companyIds.add(companyTransMgmtObj.getCompanyId());
                }
            }
        }
        if (companyId == null && company != null) {
            companyId = company.getCompanyId();
        }
        try {

            totalTermsInGlossary = termsService.getTotalTermsInGlossary(companyIds);
            logger.info(" +++  Got " + totalTermsInGlossary
                    + " Total Glossary Terms");

        }
        catch (Exception e) {
            logger.error("Failed to get Total Terms Value in Glossary");
            logger.error(e,e);
        }
        return totalTermsInGlossary;
    }

    /**
     * To get total debated terms
     *
     * @param request HttpServletRequest
     * @return Integer holding the count of total debated terms
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTotalDebatedTerms")
    public
    @ResponseBody
    Integer getTotalDebatedTerms(HttpServletRequest request) {
        int totalDebatedTerms = 0;
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            Set<Integer> companyIds = null;
            Set<CompanyTransMgmt> companyTransMgmtUsers = new HashSet<CompanyTransMgmt>();
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
                companyTransMgmtUsers = userService.getCompanyTransMgmtUsers(user.getUserId());
                if (companyTransMgmtUsers != null && !companyTransMgmtUsers.isEmpty()) {
                    companyIds = new HashSet<Integer>();
                    for (CompanyTransMgmt companyTransMgmtObj : companyTransMgmtUsers) {
                        companyIds.add(companyTransMgmtObj.getCompanyId());
                    }
                }
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            totalDebatedTerms = termsService.getTotalDebatedTerms(companyIds);
            logger.info(" +++  Got " + totalDebatedTerms + " Total Debated Terms");

        }
        catch (Exception e) {
            logger.error("Failed to get Total Debated Terms Value");
        }
        return totalDebatedTerms;
    }

    /**
     * To get list of glossary terms count per year
     *
     * @param request HttpServletRequest
     * @return List of data holding count of terms per year
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getGlossaryTerms")
    public
    @ResponseBody
    List<Terms> getGlossaryTerms(HttpServletRequest request) {
        long startTimeInMS = System.currentTimeMillis();
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            Set<Integer> companyIds = null;
            Set<CompanyTransMgmt> companyTransMgmtUsers = new HashSet<CompanyTransMgmt>();
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
                companyTransMgmtUsers = userService.getCompanyTransMgmtUsers(user.getUserId());
                if (companyTransMgmtUsers != null && !companyTransMgmtUsers.isEmpty()) {
                    companyIds = new HashSet<Integer>();
                    for (CompanyTransMgmt companyTransMgmtObj : companyTransMgmtUsers) {
                        companyIds.add(companyTransMgmtObj.getCompanyId());
                    }
                }
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            List<Terms> glossaryTermsList = termsService.getTermsInGlossary(companyIds);
            glossaryTermsList = termsService.getFinalisedTerms(glossaryTermsList, "Yearly");
            session.setAttribute(SessionKey.GLOSSARY_TERMS.getKey(), glossaryTermsList);
            if (glossaryTermsList != null && !glossaryTermsList.isEmpty()
                    && glossaryTermsList.size() > 0) {
                logger.info(" +++  Got " + glossaryTermsList.size()
                        + " Glossary Terms");
                return glossaryTermsList;
            } else {
                logger.info(" +++  NULL Terms in Glossary");
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Terms in Glossary");
        }
        finally {
            long endTimeInMS = System.currentTimeMillis();
            long totalTimeInMs = endTimeInMS - startTimeInMS;
            double totalTimeInSeconds = totalTimeInMs / 1000D;
            double totalTimeInMinutes = totalTimeInSeconds / 60D;
            String timeTook;
            if (totalTimeInMinutes >= 1D) {
                timeTook = totalTimeInMinutes + " minutes" + " or "
                        + totalTimeInSeconds + " seconds";
            } else {
                timeTook = totalTimeInSeconds + " seconds";
            }
            logger.info(" +++  Took total for getGlossaryTerms" + timeTook);
            logger.info(" +++  startTimeInMS:" + startTimeInMS / 1000
                    + " ending time:" + endTimeInMS / 1000);
        }
        return null;
    }

    /**
     * To get list of debated terms count per year
     *
     * @param request HttpServletRequest
     * @return List of data holding count of debated terms per year
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getDebatedTerms")
    public
    @ResponseBody
    List<Terms> getDebatedTerms(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            Set<Integer> companyIds = null;
            Set<CompanyTransMgmt> companyTransMgmtUsers = new HashSet<CompanyTransMgmt>();
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
                companyTransMgmtUsers = userService.getCompanyTransMgmtUsers(user.getUserId());
                if (companyTransMgmtUsers != null && !companyTransMgmtUsers.isEmpty()) {
                    companyIds = new HashSet<Integer>();
                    for (CompanyTransMgmt companyTransMgmtObj : companyTransMgmtUsers) {
                        companyIds.add(companyTransMgmtObj.getCompanyId());
                    }
                }
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            List<Terms> debatedTermsList = termsService.getDebatedTerms(companyIds);
            debatedTermsList = termsService.getFinalisedTerms(debatedTermsList, "Yearly");
            session.setAttribute(SessionKey.DEBATED_TERMS.getKey(), debatedTermsList);
            if (debatedTermsList != null && !debatedTermsList.isEmpty()
                    && debatedTermsList.size() > 0) {
                logger.info(" +++  Got " + debatedTermsList.size()
                        + " Debated Terms");
                return debatedTermsList;
            } else {
                logger.info(" +++  NULL Debated Terms");
            }

        }
        catch (Exception e) {
            logger.error("Failed to get  Debated Terms");
        }
        return null;
    }

    /**
     * To get total terms in glossary per month
     *
     * @param request HttpServletRequest
     * @return List of data holding count of terms per month
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getMonthlyTermDetails")
    public
    @ResponseBody
    List<Terms> getMonthlyTermDetails(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            List<Terms> monthlyTermsList = termsService
                    .getMonthlyTermDetails(companyId);
            monthlyTermsList = termsService.getFinalisedTerms(monthlyTermsList, "Monthly");

            if (monthlyTermsList != null) {
                logger.info(" +++  Got " + monthlyTermsList.size()
                        + " Glossary Terms per month");
                return monthlyTermsList;
            } else {
                logger.info(" +++  Null terms in glossary per month");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Term Monthly details");
        }
        return null;
    }

    /**
     * To get total debated terms per month
     *
     * @param request HttpServletRequest
     * @return List of data holding count of debated terms per month
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getMonthlyDebatedTerms")
    public
    @ResponseBody
    List<Terms> getMonthlyDebatedTerms(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            List<Terms> monthlyDebatedTerms = termsService
                    .getMonthlyDebatedTerms(companyId);
            monthlyDebatedTerms = termsService.getFinalisedTerms(monthlyDebatedTerms, "Monthly");

            if (monthlyDebatedTerms != null) {
                logger.info(" +++  Got " + monthlyDebatedTerms.size()
                        + " Debated Terms per month");
                return monthlyDebatedTerms;
            } else {
                logger.info(" +++  NULL Debated terms per month");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Debated Term Monthly details ");
        }
        return null;
    }

    /**
     * To get total terms in glossary per quarter
     *
     * @param request HttpServletRequest
     * @return List of data holding count of terms per quarter
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getQuarterlyTermDetails")
    public
    @ResponseBody
    List<Terms> getQuarterlyTermDetails(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            List<Terms> quarterlyTermsList = termsService
                    .getQuarterlyTermDetails(companyId);
            quarterlyTermsList = termsService.getFinalisedTerms(quarterlyTermsList, "Quarterly");
            if (quarterlyTermsList != null) {
                logger.info(" +++  Got " + quarterlyTermsList.size()
                        + " Glossary Terms per Quarter");
                return quarterlyTermsList;
            } else {
                logger.info(" +++  NULL Debated terms per Quarter");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Quarterly Term details");
        }
        return null;
    }

    /**
     * To get total debated terms per quarter
     *
     * @param request HttpServletRequest
     * @return List of data holding count of debated terms per quarter
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getQuarterlyDebatedTerms")
    public
    @ResponseBody
    List<Terms> getQuarterlyDebatedTerms(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            List<Terms> quarterlyDebatedTerms = termsService
                    .getQuarterlyDebatedTerms(companyId);
            quarterlyDebatedTerms = termsService.getFinalisedTerms(quarterlyDebatedTerms, "Quarterly");

            if (quarterlyDebatedTerms != null) {
                logger.info(" +++  Got " + quarterlyDebatedTerms.size()
                        + " Debated Terms per Quarter");
                return quarterlyDebatedTerms;
            } else {
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Debated Quarterly Term details");
        }
        return null;
    }

    /**
     * To get current top 20 terms for polling
     *
     * @param request HttpServletRequest
     * @return List of top 20 poll terms
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTopTerms")
    public
    @ResponseBody
    List<String> getTopTerms(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            List<String> topTermsList = termsService.getTopTerms(companyId);
            if (topTermsList != null && !topTermsList.isEmpty()
                    && topTermsList.size() > 0) {
                logger
                        .info("Got " + topTermsList.size()
                                + " Top most Terms");
                return topTermsList;
            } else {
                logger.info(" +++  NULL Top most Terms");
            }

        }
        catch (Exception e) {
            logger.error("Failed to get Top Terms");
        }
        return null;
    }


    /**
     * To get term attributes of a termId
     *
     * @param request HttpServletRequest
     * @param termId  Path variable to get particular attributes
     * @return TermAttributes w.r.t the term id
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTermAttributes/{termId}")
    public
    @ResponseBody
    TermAttributes getTermAttributes(@PathVariable Integer termId,
                                     HttpServletRequest request) {
        if (termId == null) {
            return null;
        }
        try {
            HttpSession session = request.getSession();
            User user = Utils.isNull(session.getAttribute(SessionKey.USER.getKey())) ? null : (User) session.getAttribute(SessionKey.USER.getKey());
            TermAttributes termAttributes = null;
            if (user != null)
                termAttributes = termsService.getTermAttributes(termId, user.getUserId());
            if (termAttributes != null) {
            	
            	//set the suggested term language id in session
                int suggestedTermLangId = termAttributes.getTermInfo().getSuggestedTermLangId();
                session.setAttribute(SessionKey.SELECTED_SUGGESTED_TERM_LANG_ID.getKey(), suggestedTermLangId);
                
                logger.info(" +++  Got Term Attributes for term Id" + termId);
                return termAttributes;
            } else {
                logger.info(" +++  NULL Term Attributes");
            }

        }
        catch (Exception e) {
            logger.error("Failed to get Term Attributes");
        }
        return null;
    }

    /**
     * To register a user
     *
     * @param request HttpServletRequest
     * @param user    Request Body of the user to register
     * @return String holding the status of registration
     */
    @RequestMapping(method = RequestMethod.POST, value = "/registerUser")
    public
    @ResponseBody
    String registerUser(@RequestBody User user, HttpServletRequest request,
                        HttpServletResponse response) {

        final User immutableUser;
        String registrationStatus = null;
        String redirect = "";
        String pageUrl = request.getContextPath();
        HttpSession session = request.getSession();
        Company company = Utils.isNull(session.getAttribute(SessionKey.COMPANY
                .getKey())) ? null : (Company) session
                .getAttribute(SessionKey.COMPANY.getKey());
        if (company == null){
        	
        	String hpCompany = (String)session.getAttribute(SessionKey.HP_COMPANY.getKey());
        	
        	if(hpCompany.equalsIgnoreCase("true")){
        		company = lookUpService.getCompanyIdByLabel("hp");
        	}else{
        		company = lookUpService.getCompanyById(1);
        	}
        }

        if (!Utils.isNull(user) && !Utils.isEmpty(user.getEmailId())) {
            user.setUserName(user.getEmailId());

            try {
                Set<UserLanguages> userLanguagesSet = new HashSet<UserLanguages>();
                Set<UserRole> roleSet = new HashSet<UserRole>();
                @SuppressWarnings("rawtypes")
				Iterator it = user.getUserLanguages().iterator();
                @SuppressWarnings("rawtypes")
				Iterator roleIterator = user.getUserRole().iterator();


                boolean userExistsFlag = userService.ifUserExists(user.getUserName());
                boolean emailExistsFlag = userService.ifEmailExists(user.getEmailId());
                
                if (!userExistsFlag && !emailExistsFlag) {
                    while (it.hasNext()) {
                        UserLanguages userLanguage = (UserLanguages) it.next();
                        Language language = lookUpService
                                .getLanguage(userLanguage.getLanguages()
                                        .getLanguageId());
                        userLanguage.setLanguages(language);
                        userLanguagesSet.add(userLanguage);
                    }
                    user.setUserLanguages(userLanguagesSet);

                    while (roleIterator.hasNext()) {
                        UserRole userRoles = (UserRole) roleIterator.next();
                        Role role = lookUpService.getUserRole(userRoles.getRole().getRoleId());
                        userRoles.setRole(role);
                        roleSet.add(userRoles);

                    }
                    user.setUserLanguages(userLanguagesSet);
                    user.setUserRole(roleSet);
                    if (company != null) {
                        user.setCompany(company);
                    }
                    registrationStatus = userService.registerUser(user);
                    logger.info(" +++  registrationStatus == " + registrationStatus);
                    // sending mail to admin regarding new user Registration.
                    immutableUser = user;
                    new Thread()
                    {
                        public void run() {
                            mailService.newUserRegistered(immutableUser);
                        }
                    }.start();

                    if ("success".equalsIgnoreCase(registrationStatus) && company != null) {
                        CompanyTransMgmt companyTransMgmt = new CompanyTransMgmt();
                        companyTransMgmt.setIsActive("Y");
                        companyTransMgmt.setIsExternal("Y");
                        companyTransMgmt.setUserId(user.getUserId());
                        companyTransMgmt.setCompanyId(company.getCompanyId());
                        userService.SaveCompanyTransMgmt(companyTransMgmt);
                    }

                } else {
                    registrationStatus = "failure";
                    if (userExistsFlag) {
                        session.setAttribute(SessionKey.IS_USER_EXISTS.getKey(), Boolean.TRUE);
                        session.setAttribute(SessionKey.USER.getKey(), user);
                        redirect = "User exists";
                    }
                    if (emailExistsFlag) {
                        session.setAttribute(SessionKey.IS_EMAIL_EXISTS.getKey(), Boolean.TRUE);
                        session.setAttribute(SessionKey.USER.getKey(), user);
                        if (redirect != "") {
                            redirect = redirect + ',' + " Email exists";
                        } else {
                            redirect = "Email exists";
                        }
                    }

                }


                if (registrationStatus.equalsIgnoreCase("success")) {
                    session.setAttribute(SessionKey.USER_ID.getKey(), user.getUserId());
                    session.setAttribute(SessionKey.USER.getKey(), user);
                    session.setAttribute(SessionKey.USER_ROLE.getKey(), user.getUserRole());
                    String roleName = RoleEnum.SUPER_ADMIN.getValue();
                    Role roleObj = userService.getRoleIdByLabel(roleName);
                    session.setAttribute(SessionKey.ROLE.getKey(), roleObj);
                    if (null != user.getUserRole() && user.getUserRole().size() > 0) {
                        for (UserRole userroleObj : user.getUserRole()) {
                            if (userroleObj.getRole().getRoleId().intValue() == roleObj.getRoleId().intValue()) {
                                session.setAttribute(SessionKey.USER_TYPE.getKey(), "ADMIN");
                                logger.info(" +++  Redirecting to admin page....");
                                if (company != null && company.getContextRoot() != null) {
                                    pageUrl = request.getContextPath() + "/" + company.getContextRoot();
                                } else
                                    pageUrl = request.getContextPath() + "/hp";
                                redirect = pageUrl + TeaminologyPage.ADMIN.getPageUrl();
                            } else {
                              /*  if (company != null && company.getContextRoot() != null) {
                                    pageUrl = request.getContextPath() + "/" + company.getContextRoot();
                                } else
                                    pageUrl = request.getContextPath();*/
                                redirect = pageUrl + "/hp" + TeaminologyPage.DASHBOARD.getPageUrl();
                            }
                            break;
                        }
                    }
                }

            }
            catch (Exception e) {
                logger.error("Error in registering  the user ");
                logger.error(e,e);
                registrationStatus = "failure";
            }
        }
        logger.info(" +++  redirect == " + redirect);
        return redirect;
    }

    /**
     * To get User Details of the user in session
     *
     * @param request HttpServletRequest
     * @return User details w.r.t the user id in the session
     */
    @RequestMapping(method = RequestMethod.GET, value = "/userDetails")
    public
    @ResponseBody
    Member userDetails(HttpServletRequest request) {
        Member usermember = null;
        HttpSession session = request.getSession();
        User user = Utils.isNull(session.getAttribute(SessionKey.USER.getKey())) ? null : (User) session.getAttribute(SessionKey.USER.getKey());


        if (!Utils.isNull(user)) {
            try {
                usermember = userService.userDetails(user.getUserId());
                if (usermember != null) {
                    logger.info(" +++  Got User Details for user Id" + user.getUserId());
                    return usermember;
                } else {
                    logger.info(" +++  No Details for user Id" + user.getUserId());
                    return null;
                }
            }
            catch (Exception e) {
                logger.error("Failed to get User Details for user Id" + user.getUserId());
                return null;
            }
        }
        return null;

    }

    /**
     * To get list of users per month
     *
     * @param request HttpServletRequest
     * @return List of data holding count of users per month
     */
    @RequestMapping(method = RequestMethod.GET, value = "/usersPerMonth")
    public
    @ResponseBody
    List<UsersChartData> usersPerMonth(HttpServletRequest request) {
        long startTimeInMS = System.currentTimeMillis();
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;

            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            List<UsersChartData> usersData = userService.usersPerMonth(companyId);
            if (usersData != null) {
                logger.info(" +++  Got usersPerMonth Data");
                return usersData;
            } else {
                logger.info(" +++  NULL usersPerMonth Data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get no of UsersPerMonth");
        }
        finally {
            long totalTimeInMs = System.currentTimeMillis() - startTimeInMS;
            double totalTimeInSeconds = totalTimeInMs / 1000D;
            double totalTimeInMinutes = totalTimeInSeconds / 60D;
            String timeTook;
            if (totalTimeInMinutes >= 1D) {
                timeTook = totalTimeInMinutes + " minutes" + " or "
                        + totalTimeInSeconds + " seconds";
            } else {
                timeTook = totalTimeInSeconds + " seconds";
            }
            logger.info(" +++  Took total " + timeTook);
        }

        return null;
    }

    /**
     * To get total active users
     *
     * @param request HttpServletRequest
     * @return Integer holding the count of total active users
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getActiveUsersCount")
    public
    @ResponseBody
    Integer getActiveUsersCount(HttpServletRequest request) {
        Integer usersCount = 0;
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            usersCount = userService.getActiveUsersCount(companyId);
            if (usersCount != null) {
                logger.info(" +++  Got Active users count");
                return usersCount;
            } else {
                logger.info(" +++  NULL Active users count");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Active users count");
        }
        return null;
    }

    /**
     * To get language lookup data
     *
     * @param request HttpServletRequest
     * @return List of language obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getLanguages")
    public
    @ResponseBody
    List<Language> getLanguages(HttpServletRequest request) {
        try {
            List<Language> languages = lookUpService.getLanguages();
            
            if (languages != null) {
                logger.info(" +++  Got languages data");
                return languages;
            } else {
                logger.info(" +++  NULL languages data");
                return null;
            }

        }
        catch (Exception e) {
            logger.error("Failed to get Languages");
            logger.error(e,e);
        }
        return null;
    }

    /**
     * To get Parts of speech lookup data
     *
     * @param request HttpServletRequest
     * @return List of Parts of speech obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getPartsOfSpeechLookUp")
    public
    @ResponseBody
    List<PartsOfSpeech> getPOSLookUp(HttpServletRequest request) {
        try {
            List<PartsOfSpeech> posList = lookUpService.getPOSLookUp();
            if (posList != null) {
                logger.info(" +++  Got PartsOfSpeech data");
                return posList;
            } else {
                logger.info(" +++  NULL PartsOfSpeech data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Parts Of Speech");
            logger.error(e,e);
        }
        return null;
    }

    /**
     * To get Form lookup data
     *
     * @param request HttpServletRequest
     * @return List of form obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getFormLookUp")
    public
    @ResponseBody
    List<Form> getFormLookUp(HttpServletRequest request) {
        try {
            List<Form> forms_list = lookUpService.getFormLookUp();
            if (forms_list != null) {
                logger.info(" +++  Got Form data");
                return forms_list;
            } else {
                logger.info(" +++  NULL Form data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Forms");
            logger.error(e,e);
        }
        return null;
    }

    /**
     * To get Program lookup data
     *
     * @param request HttpServletRequest
     * @return List of program obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getProgramLookUp")
    public
    @ResponseBody
    List<Program> getProgramLookUp(HttpServletRequest request) {
        try {
            List<Program> program_list = lookUpService.getProgramLookUp();
            if (program_list != null) {
                logger.info(" +++  Got Program data");
                return program_list;
            } else {
                logger.info(" +++  NULL Program data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Program data");
        }
        return null;
    }


    /**
     * To update term attributes
     *
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @param termDetails RequestBody term attributes to be updated
     * @return Integer holding the status of the update
     */
    @RequestMapping(method = RequestMethod.POST, value = "/updateTermDetails")
    public
    @ResponseBody
    int updateTermDetails(@RequestBody TermDetails termDetails,
                          HttpServletRequest request, HttpServletResponse response) {

        int updateStatusCode = 1;
        int termId = 0;
        PartsOfSpeech termPOS = null;
        PartsOfSpeech targetPOS=null;
        Program termProgram = null;
        Form termForm = null;
        Category termCategory = null;
        Domain termDomain = null;
        String isReplace = "N";
        User user = (User)request.getSession().getAttribute(SessionKey.USER.getKey());
        logger.info("Called the controller /updateTermDetails/ to update  a term.....");
        
        if(!Utils.isNull(user)){
        	
        	@SuppressWarnings("unchecked")
        	List<Pair<String, String>> loggerMessages = Arrays.asList(
        			new Pair<String,String>(ParamKeyEnum.TERM_ID.toString(),termDetails.getTermId().toString()),
        			new Pair<String,String>(ParamKeyEnum.UPDATED_BY.toString(),user.getUserName()),
        			new Pair<String,String>(ParamKeyEnum.UPDATED_DATE.toString(),new Date().toString()),
        			new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
        			new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
        			new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
        			new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
        			);
        	
        	logger.info("Updated Term Details ++++ "+loggerMessages);
        }
        
        if (!Utils.isNull(termDetails)) {

            try {
                HttpSession session = request.getSession();
                Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID.getKey())) ? null : (Integer) session.getAttribute(SessionKey.USER_ID.getKey());
                termId = termDetails.getTermId();
                TermInformation termInformation = new TermInformation();
                termInformation.setTermId(termId);

                if (termDetails.getTermPOSId() != null) {
                    termPOS = lookUpService.getPartsOfSpeech(termDetails.getTermPOSId());
                }

                if (termDetails.getTargetPOSId() != null) {
                	targetPOS = lookUpService.getPartsOfSpeech(termDetails
                            .getTargetPOSId());
                }

                //set term status TNG-47
                if(termDetails.getTermStatusId() != null) {
                	termInformation.setTermStatusId(termDetails.getTermStatusId());
                }
                
                if (termDetails.getTermProgramId() != null) {
                    termProgram = lookUpService.getProgram(termDetails
                            .getTermProgramId());
                }

                if (termDetails.getTermFormId() != null) {
                    termForm = lookUpService.getForm(termDetails
                            .getTermFormId());
                }
                if (termDetails.getTermCatagoryId() != null) {
                    termCategory = lookUpService.getCategory(termDetails
                            .getTermCatagoryId());
                }
                if (termDetails.getTermDomainId() != null) {
                    termDomain = lookUpService.getDomainById(termDetails.getTermDomainId());
                }

                termInformation.setTermBeingPolled(termDetails.getSourceTerm());
                termInformation.setTermPOS(termPOS);
                termInformation.setTargetTermPOS(targetPOS);
                termInformation.setTermProgram(termProgram);
                termInformation.setTermForm(termForm);
                termInformation.setSuggestedTermPosId(termDetails.getTargetPOSId());
                if (termDetails.getTopSuggestion() != null || termDetails.getTopSuggestion() != "") {
                    termInformation.setSuggestedTerm(termDetails.getTopSuggestion());
                }
                termInformation.setTermNotes(termDetails.getTermNotes());
                termInformation.setConceptDefinition(termDetails.getConceptDefinition());
                termInformation.setTermCategory(termCategory);
                termInformation.setTermUsage(termDetails.getTermUsage());
                termInformation.setTermDomain(termDomain);
                termInformation.setUpdatedBy(userId);
                termInformation.setUpdateDate(new Date());
                termsService.updateTermDetails(termInformation, isReplace);
                logger.info(" +++  Updated term attributes for termId :" + termId);
                updateStatusCode = 1;

            }
            catch (Exception e) {
                updateStatusCode = -1;
                logger.error("Failed to update term attributes for term :"
                        + termId);
                return updateStatusCode;
            }
        } else {
            updateStatusCode = 0;
        }
        return updateStatusCode;
    }

    /**
     * To extend Poll of an expired term
     *
     * @param request  HttpServletRequest
     * @param pollDate PathVariable Date to extend the poll term
     * @param termId   PathVariable Integer to identify the term
     */
    @RequestMapping(method = RequestMethod.GET, value = "/extendPoll/{pollDate}/{termId}")
    public
    @ResponseBody
    void extendPoll(@PathVariable String pollDate, @PathVariable Integer termId,
                    HttpServletRequest request) {
        if (termId == null) {
            return;
        }

        try {
            HttpSession session = request.getSession();
            Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                    .getKey())) ? null : (Integer) session
                    .getAttribute(SessionKey.USER_ID.getKey());
            TermVoteMaster termVoteMaster = null;
            termVoteMaster = termsService.getTermVoteMaster(termId);
            logger.info(" +++  #############:pollDate" + pollDate);
            System.out.println("#############:pollDate" + pollDate);
            if (pollDate != null && !pollDate.isEmpty()) {
                Date date = new SimpleDateFormat("MM-dd-yyyy").parse(pollDate);
                if (termVoteMaster != null) {
                    termVoteMaster.setVotingExpiredDate(date);
                    termVoteMaster.setUpdatedBy(userId);
                    termVoteMaster.setUpdateDate(new Date());
                    termsService.extendPoll(termVoteMaster);
                }
            }
            logger.info(" +++  Extended Poll for termId :" + termId);

        }
        catch (Exception e) {
            logger.error("Failed to extend poll");
        }
    }

    /**
     * To approve a term
     *
     * @param suggestedTermId PathVariable Integer to identify the suggested term
     */
    @RequestMapping(method = RequestMethod.GET, value = "/approveTerm/{suggestedTermId}")
    public
    @ResponseBody
    int approveTerm(@PathVariable Integer suggestedTermId,
                     HttpServletRequest request) {
    	int updateStatus = 0;
        if (suggestedTermId == null) {
            return updateStatus;
        }
        try {
            updateStatus = termsService.approveSuggestedTerm(suggestedTermId);
        }
        catch (Exception e) {
            logger.error("Failed to extend poll");
        }
        
        return updateStatus;
    }

    /**
     * To get user type details
     *
     * @param request HttpServletRequest
     * @return List of User type obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUserTypeDetails")
    public
    @ResponseBody
    List<Role> getUserTypeDetails(HttpServletRequest request) {
        try {
            List<Role> userTypeList = userService.getUserTypeDetails();
            if (userTypeList != null) {
                logger.info(" +++  Got " + userTypeList.size()
                        + " Glossary Terms per Quarter");
                return userTypeList;
            } else {
                logger.info(" +++  NULL Debated terms per Quarter");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Quarterly Term details");
        }
        return null;
    }

    /**
     * To invite users to join in HP Teaminology
     *
     * @param request   HttpServletRequest
     * @param inviteIds RequestBody Invite containing the mail id's
     * @return String holding the status of the invite
     */
    @RequestMapping(method = RequestMethod.POST, value = "/invitePeople")
    public
    @ResponseBody
    String invitePeople(@RequestBody Invite inviteIds,
                        HttpServletRequest request) {
        String status = "";
        try {
        	
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer userId=user.getUserId();
            String userEmailId = user.getEmailId();
            String contextPath=request.getContextPath();
            inviteIds.setCompanyContext(contextPath);
            if(userId != 1){
            	Company company=user.getCompany();
                inviteIds.setCompanyId(company.getCompanyId());
                inviteIds.setCompanyRoot(company.getContextRoot());
                }
            status = mailService.inviteUser(inviteIds, userEmailId);
            if (status == null) {
                status = "failed";
                logger.info(" +++  Got null status while inviting people");
            } else {
                logger.info(" +++  Successfully invited people");
            }
        }
        catch (Exception e) {
            logger.error("Failed to invite people to community");
        }
        return status;
    }

    /**
     * To sort expired poll terms
     *
     * @param request   HttpServletRequest
     * @param colName   PathVariable column name that has to be sorted
     * @param sortOrder PathVariable order in which it has to be sorted
     * @param langIds   PathVariable String containing language id's
     * @param pageNum   PathVariable Integer to limit the data
     * @return List of terms
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sortOrFilterExpPollTerms/{colName}/{sortOrder}/{langIds}/{companyIds}/{pageNum}")
    public
    @ResponseBody
    List<PollTerms> sortOrFilterExpPollTerms(@PathVariable String colName,
                                             @PathVariable String sortOrder, @PathVariable String langIds, @PathVariable String companyIds,
                                             @PathVariable int pageNum, HttpServletRequest request) {
        List<PollTerms> filteredPollTermsList = null;
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            String expTermCompanyIds = companyIds.toString();

            filteredPollTermsList = termsService.sortOrFilterExpPollTerms(
                    colName, sortOrder, langIds, pageNum, companyId, expTermCompanyIds);
            if (filteredPollTermsList != null
                    && !filteredPollTermsList.isEmpty()
                    && filteredPollTermsList.size() > 0) {
                logger.info(" +++  Got " + filteredPollTermsList.size()
                        + " Expired Poll Terms while sorting by language ");
                return filteredPollTermsList;
            } else {
                logger
                        .info("NULL Expired Poll Terms while sorting by Language");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to sort Poll terms");
        }
        return null;

    }

    /**
     * To get user register languages
     *
     * @param request HttpServletRequest
     * @return List to get user registered languages
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUserRegLanguages")
    public
    @ResponseBody
    List<Language> getUserRegLanguages(HttpServletRequest request) {
        List<Language> languageList = null;
        HttpSession session = request.getSession();
        Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                .getKey())) ? null : (Integer) session
                .getAttribute(SessionKey.USER_ID.getKey());
        if (!Utils.isNull(userId)) {
            try {
                languageList = userService.getUserRegLanguages(userId);
                if (languageList != null) {
                    logger.info(" +++  Got User Details for user Id" + userId);
                    return languageList;
                } else {
                    logger.info(" +++  No Details for user Id" + userId);
                    return null;
                }
            }
            catch (Exception e) {
                logger.error("Failed to get User Details for user Id" + userId);
                return null;
            }
        }
        return null;

    }

    /**
     * To get user poll terms
     *
     * @param request    HttpServletRequest
     * @param languageId String to filter terms respectively
     * @param colName    column name that has to be sorted
     * @param sortOrder  order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @return List of terms which user invited to poll
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUserPollTerms/{languageId}/{colName}/{sortOrder}/{pageNum}")
    public
    @ResponseBody
    List<PollTerms> getUserPollTerms(@PathVariable String languageId,
                                     @PathVariable String colName, @PathVariable String sortOrder,
                                     @PathVariable Integer pageNum, HttpServletRequest request) {
        if (languageId == null) {
            return null;
        }
        List<PollTerms> user_poll_terms_list = null;
        try {
            HttpSession session = request.getSession();
            Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                    .getKey())) ? null : (Integer) session
                    .getAttribute(SessionKey.USER_ID.getKey());
            user_poll_terms_list = termsService.getUserPollTerms(languageId,
                    colName, sortOrder, pageNum, userId);
            if (user_poll_terms_list != null && !user_poll_terms_list.isEmpty()
                    && user_poll_terms_list.size() > 0) {
                logger.info(" +++  Got " + user_poll_terms_list.size()
                        + " Expired Poll Terms for language ids " + languageId);
                return user_poll_terms_list;
            } else {
                logger.info(" +++  NULL user Poll Terms for Language Id "
                        + languageId);
            }

        }
        catch (Exception e) {
            logger.error("Failed to user poll terms  by language");
        }
        return null;

    }

    /**
     * To get total user language terms
     *
     * @param request    HttpServletRequest
     * @param languageId Integer to be filtered
     * @return An integer which holds the count of terms w.r.t the language
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTotalUsersLangTerms/{languageId}")
    public
    @ResponseBody
    Integer getTotalUsersLangTerms(@PathVariable String languageId,
                                   HttpServletRequest request) {
        int totalUsersLangTerms = 0;
        try {
            HttpSession session = request.getSession();
            Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                    .getKey())) ? null : (Integer) session
                    .getAttribute(SessionKey.USER_ID.getKey());
            totalUsersLangTerms = userService.getTotalUsersLangTerms(
                    languageId, userId);
            if (totalUsersLangTerms != 0) {
                logger.info(" +++  Got total users language terms");
                return totalUsersLangTerms;
            } else {
                logger.info(" +++  NULL total users language terms");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get  total users language terms");
        }
        return null;
    }

    /**
     * To vote a particular term, by a user
     *
     * @param request         HttpServletRequest
     * @param termTranslation TermTranslation to count on vote
     * @return String holding the status of vote
     */
    @RequestMapping(method = RequestMethod.POST, value = "/voteTerm")
    public
    @ResponseBody
    String voteTerm(@RequestBody TermTranslation termTranslation,
                    HttpServletRequest request) {
        String status = "";
        if (termTranslation == null) {
            status = "failed";
            return status;
        }
        try {
            HttpSession session = request.getSession();
            Integer user_id = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                    .getKey())) ? null : (Integer) session
                    .getAttribute(SessionKey.USER_ID.getKey());
            status = termsService.voteTerm(termTranslation, user_id);
           Integer totalVotesOfUserForRank =  termsService.getTotalVotesOfUser(user_id);
           if(totalVotesOfUserForRank == 25 || totalVotesOfUserForRank == 50 || totalVotesOfUserForRank == 100 || totalVotesOfUserForRank == 200 ) {
        	   status = "New Rank";
               User user = Utils.isNull(session.getAttribute(SessionKey.USER
                       .getKey())) ? null : (User) session
                       .getAttribute(SessionKey.USER.getKey());
               String emailSubject = EmailTemplateEnum.YOU_GOT_NEW_RANK.getValue();
               String requestTermContent = "";
              /* if (user != null) {
               	requestTermContent ="<html><table style=\"width: 100%; height: 20px;\"> " +
               	" <tbody><tr> <td style=\"padding-top: 30px;\" width=\"80%\"><span style=\"font-size: 36px; font-family: HP Simplified ,Arial;\"> " +
               	" <strong>HP Terminology Community</strong></span></td><td style=\"padding-top: 30px;\" align=\"right\" width=\"20%\"><span> " +
                " <img src=\"http://www.hptermcommunity.com/app/hp/images/hp_logo.png\" /></span></td></tr></tbody></table> " +
                " <body><p>&nbsp;</p><p><span style=\"font-size: small; font-family: 'HP Simplified';\">Hello,</span></p> " +
                " <p><span style=\"font-size: small; font-family: 'HP Simplified';\">Congratulations!! You got new rank in teaminology.</span></p> " +
                " <p><span style=\"font-size: small; font-family: 'HP Simplified';\">Thanks a lot for your great help and involvement in managing HP terminology.</span></p> " +
                " <p><span style=\"font-size: small; font-family: 'HP Simplified';\">&nbsp;</span></p> " +
                " <p><span style=\"font-size: small; font-family: 'HP Simplified';\">Best Regards,</span><br/><span style=\"font-size: small; font-family: 'HP Simplified';\"> " +
                " <strong>HP Terminology<br/> </strong>HP Global Brand Terminology Management team</span><br/> " +
                " <span style=\"font-size: small; font-family: 'HP Simplified';\"> Digital Publishing &amp; Operations (Globalization &amp; Translation)</span><br/> " +
                " <br/><span style=\"font-size: small; font-family: 'HP Simplified';\"> " +
                " <a href=\"https://external1.collaboration.hp.com/external/TL_Vendor_Access/new_look/terminology/terminology.aspx\">Terminology Program SharePoint</a></span></p></body></html>";
                   Set<String> emailIds = new HashSet<String>();
                       emailIds.add(user.getEmailId());
                   if (emailIds != null && emailIds.size() > 0) {
                       mailService.sendMailUsingTemplate(emailIds, requestTermContent, emailSubject);
                   }
               }*/

               logger.info(" +++  Sent reward mail");
           }
            if (status == null) {
                status = "failed";
                logger.info(" +++  Got null status while voting a term");
            }
        }
        catch (Exception e) {
            logger.error("Failed to vote a term by userId : ");
        }
        return status;

    }

    /**
     * To reject a term, by a user
     *
     * @param request HttpServletRequest
     * @param termId  PathVariable Integer to identify the term
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rejectTerm/{termId}")
    public
    @ResponseBody
    void rejectTerm(@PathVariable Integer termId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID.getKey())) ? null : (Integer) session.getAttribute(SessionKey.USER_ID.getKey());

        if (termId == null) {
            return;
        }

        try {

            termsService.rejectTerm(termId, userId);
            logger.info(" +++  User: " + userId + " rejected term : " + termId);

        }
        catch (Exception e) {
            logger.error("Failed to reject a term by userId : " + userId);
        }

    }

    /**
     * To get user accuracy rated values for a user in session
     *
     * @return A collection which contains the finalised voted terms count and total voted terms count
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUserAccuracyRate")
    public
    @ResponseBody
    Map<String, BigInteger> getUserAccuracyRate(HttpServletRequest request) {
        Map<String, BigInteger> userAccuracyRate = null;
        HttpSession session = request.getSession();
        User user = Utils.isNull(session.getAttribute(SessionKey.USER.getKey())) ? null : (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        String statusName = StatusLookupEnum.APPROVED.getValue();
        Status status = lookUpService.getStatusIdByLabel(statusName);
        Integer statusId = status.getStatusId();
        if (user != null) {
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
        }
        if (!Utils.isNull(user)) {
            try {
                userAccuracyRate = userService.getUserAccuracyRate(user.getUserId(), statusId, companyId);
                if (userAccuracyRate != null) {
                    logger.info(" +++  Got accuracy rate for user Id :" + user.getUserId());
                    return userAccuracyRate;
                } else {
                    logger.info(" +++  No accuracy rate for user Id :" + user.getUserId());
                    return null;
                }
            }
            catch (Exception e) {
                logger.error("Failed to get accuracy rate for user Id :"
                        + user.getUserId());
                return null;
            }
        }
        return null;

    }

    /**
     * Add new term.
     *
     * @param termInformation RequestBody TermInformation that needs to be added
     * @return If term is voted it returns success else failed
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addNewTerm")
    public
    @ResponseBody
    String addNewTerm(@RequestBody TermInformation termInformation,
                      HttpServletRequest request) {
        String status = "";
        HttpSession session = request.getSession();
        Integer user_id = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                .getKey())) ? null : (Integer) session
                .getAttribute(SessionKey.USER_ID.getKey());
        User user = Utils.isNull(session.getAttribute(SessionKey.USER
                .getKey())) ? null : (User) session
                .getAttribute(SessionKey.USER.getKey());
        PartsOfSpeech termPOS = null;
        Program termProgram = null;
        Form termForm = null;
        Category termCategory = null;
        Domain termDomain = null;
        Company termCompany = null;

        logger.info("Called the controller /addNewTerm/ to add  a new term.....");
        
        @SuppressWarnings("unchecked")
		List<Pair<String, String>> loggerMessages = Arrays.asList(
        		new Pair<String,String>(ParamKeyEnum.ADDED_BY.toString(),user.getUserName()),
        		new Pair<String,String>(ParamKeyEnum.ADDED_DATE.toString(),new Date().toString()),
        		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
        		new Pair<String,String>(ParamKeyEnum.USER_NAME.toString(),user.getUserName()),
        		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
        		new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
        		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
        		);
        
        logger.info("Added term Info ++++ "+loggerMessages);
        
        Set<DeprecatedTermInformation> deprecatedTermInfoset = new HashSet<DeprecatedTermInformation>();
        if (!Utils.isNull(termInformation)) {
            try {
                if (termInformation.getTermPOS() != null
                        && termInformation.getTermPOS().getPartsOfSpeechId() != null
                        && termInformation.getTermPOS().getPartsOfSpeechId() != 0)
                    termPOS = lookUpService.getPartsOfSpeech(termInformation.getTermPOS().getPartsOfSpeechId());

                if (termInformation.getTermProgram() != null
                        && termInformation.getTermProgram().getProgramId() != null
                        && termInformation.getTermProgram().getProgramId() != 0)
                    termProgram = lookUpService.getProgram(termInformation.getTermProgram().getProgramId());

                if (termInformation.getTermForm() != null
                        && termInformation.getTermForm().getFormId() != null
                        && termInformation.getTermForm().getFormId() != 0)
                    termForm = lookUpService.getForm(termInformation.getTermForm().getFormId());

                if (termInformation.getTermCategory() != null
                        && termInformation.getTermCategory().getCategoryId() != null
                        && termInformation.getTermCategory().getCategoryId() != 0)
                    termCategory = lookUpService.getCategory(termInformation.getTermCategory().getCategoryId());

                if (termInformation.getTermDomain() != null
                        && termInformation.getTermDomain().getDomainId() != null
                        && termInformation.getTermDomain().getDomainId() != 0)
                    termDomain = lookUpService.getDomainById(termInformation.getTermDomain().getDomainId());

                if (termInformation.getTermCompany() != null
                        && termInformation.getTermCompany().getCompanyId() != null
                        && termInformation.getTermCompany().getCompanyId() != 0) {
                    termCompany = lookUpService.getCompanyById(termInformation.getTermCompany().getCompanyId());
                } else {
                    termCompany = lookUpService.getCompanyById(user.getCompany().getCompanyId());
                }

                Language language = lookUpService.getLanguageByLabel(TeaminologyEnum.ENGLISH.getValue());
                termInformation.setTermLangId(language.getLanguageId());
                termInformation.setTermPOS(termPOS);
                termInformation.setSuggestedTermPosId(termInformation.getTargetTermPOS().getPartsOfSpeechId());
                termInformation.setTermProgram(termProgram);
                termInformation.setTermForm(termForm);
                termInformation.setTermCategory(termCategory);
                termInformation.setCreatedBy(user_id);
                termInformation.setCreateDate(new Date());
                termInformation.setPhotoPath(termInformation.getPhotoPath());
                termInformation.setTermDomain(termDomain);
                termInformation.setTermCompany(termCompany);
                if (termInformation.getDeprecatedTermInfo() != null) {
                    deprecatedTermInfoset = termInformation.getDeprecatedTermInfo();
                    for (DeprecatedTermInformation deprecatedTermInformation : deprecatedTermInfoset) {
                        deprecatedTermInformation.setTermInfo(termInformation);
                        deprecatedTermInformation.setCreatedBy(user_id);
                        deprecatedTermInformation.setCreateDate(new Date());
                    }
                    termInformation.setDeprecatedTermInfo(deprecatedTermInfoset);
                }
                status = termsService.addNewTerm(termInformation);
                logger.info(" +++  Successfully added new term");

            }
            catch (Exception e) {
                logger.error(e,e);
                logger.error("Failed to add new term");	
                return status = "failed";
            }

        }
        return status;
    }

    /**
     * To get concept category lookup data
     *
     * @param request HttpServletRequest
     * @return List of ConceptCategory obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getConceptCategoryLookUp")
    public
    @ResponseBody
    List<ConceptCategory> getConceptCategoryLookUp(HttpServletRequest request) {
        try {
            List<ConceptCategory> conceptCategoryList = lookUpService
                    .getConceptCategoryLookUp();
            if (conceptCategoryList != null) {
                logger.info(" +++  Got Concept Category data");
                return conceptCategoryList;
            } else {
                logger.info(" +++  NULL Concept Category data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Concept Category data");
            logger.error(e,e);
        }
        return null;
    }

    /**
     * To get category lookup data
     *
     * @param request HttpServletRequest
     * @return List of Category obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getCategoryLookUp")
    public
    @ResponseBody
    List<Category> getCategoryLookUp(HttpServletRequest request) {
        try {
            List<Category> categoryList = lookUpService.getCategoryLookUp();
            if (categoryList != null) {
                logger.info(" +++  Got  Category data");
                return categoryList;
            } else {
                logger.info(" +++  NULL  Category data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get  Category data");
            logger.error(e,e);
        }
        return null;
    }

    /**
     * To get product group lookup data
     *
     * @param request HttpServletRequest
     * @return List of ProductGroup obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getProductGroupLookUp")
    public
    @ResponseBody
    List<ProductGroup> getProductGroups(HttpServletRequest request) {
        try {
            List<ProductGroup> product_group_list = lookUpService
                    .getProductGroupLookUp();
            if (product_group_list != null) {
                logger.info(" +++  Got  ProductGroup data");
                return product_group_list;
            } else {
                logger.info(" +++  NULL  ProductGroup data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get  ProductGroup data");
            logger.error(e,e);
        }
        return null;
    }

    /**
     * To get status lookup data
     *
     * @param request HttpServletRequest
     * @return List of Status obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getStatusLookUp")
    public
    @ResponseBody
    List<Status> getStatusLookUp(HttpServletRequest request) {
        try {
            List<Status> statusList = lookUpService.getStatusLookUp();
            if (statusList != null) {
                logger.info(" +++  Got  status  data");
                return statusList;
            } else {
                logger.info(" +++  NULL  status data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get status data");
            logger.error(e,e);
        }
        return null;
    }

    /**
     * To get customised Leader Board Members
     *
     * @param request HttpServletRequest
     * @return DataTable holding the customised data with users
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getAllMembers")
    public
    @ResponseBody
    DataTable getAllBoardMembers(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        String statusName = StatusLookupEnum.APPROVED.getValue();
        Status status = lookUpService.getStatusIdByLabel(statusName);
        Integer statusId = status.getStatusId();
        if (user != null) {
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
        }
        Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
        if (companyId == null && company != null) {
            companyId = company.getCompanyId();
        }
        try {
            List<Member> memberList = termsService.getBoardMembers(companyId);
            List<Member> modifiedMemberList = new ArrayList<Member>();
            String photoPath = null;
            String badgingPhotopath = null;
            String accuracyImg = null;
            DataTable dataTable = new DataTable();

            if (memberList != null && !memberList.isEmpty()
                    && memberList.size() > 0) {
                logger.info(" +++  Got " + memberList.size()
                        + " Leader Board Members");
                for (int i = 0; i < memberList.size(); i++) {
                    float accuracyRate = 0;
                    int badgingNum = 0;
                    Member modifiedMember = memberList.get(i);
                    photoPath = modifiedMember.getPhotoPath();
                    Integer totalvotes = (Integer) ((modifiedMember.getTotalVotes() == null) ? 0 : modifiedMember.getTotalVotes().intValue());
                    //Integer termRequestCount = (modifiedMember.getTermRequestCount() == null) ? 0 : modifiedMember.getTermRequestCount();
                    modifiedMember
                            .setPhotoPath("<img src='"
                                    + photoPath
                                    + "' height='38px' width='38px' class='alignCenter' />");

                    //Integer badgingRate = totalvotes.intValue() + termRequestCount.intValue();
                    Integer badgingRate = totalvotes.intValue();
                    if (badgingRate >= 0 && badgingRate < 25) {
                        badgingPhotopath = request.getContextPath() + "/images/biginner.jpg";
                        badgingNum = 0;
                    } else if (badgingRate >= 25 && badgingRate < 50) {
                        badgingPhotopath = request.getContextPath() + "/images/novice.jpg";
                        badgingNum = 1;
                    } else if (badgingRate >= 50 && badgingRate < 100) {
                        badgingPhotopath = request.getContextPath() + "/images/regular.jpg";
                        badgingNum = 2;
                    }else if (badgingRate >= 100  && badgingRate < 200) {
                        badgingPhotopath = request.getContextPath() + "/images/advanced.jpg";
                        badgingNum = 3;
                    }else if (badgingRate >= 200) {
                        badgingPhotopath = request.getContextPath() + "/images/expert.jpg";
                        badgingNum = 4;
                    }

                    modifiedMember.setBagdingPhotopath("<span class='nodisplay'>" + badgingNum + "</span><img src='" + badgingPhotopath + "'  class='alignCenter imageWidth150' />");
                    String formattedVotes = Utils.format(modifiedMember.getTotalVotes().intValue());
                    Map<String, BigInteger> userAccuracyRate = userService.getUserAccuracyRate(modifiedMember.getUserId(), statusId, companyId);
                    float finalizedTerm = userAccuracyRate.get("finalizedTerm").floatValue();
                    float votedTerms = userAccuracyRate.get("votedTerms").floatValue();
                    int intAccuracy = 0;
                    if (finalizedTerm != 0 && votedTerms != 0) {
                        accuracyRate = finalizedTerm / votedTerms;
                        accuracyRate = accuracyRate * 100;
                    }
                    if (accuracyRate >= 0 && accuracyRate < 25) {
                        accuracyImg = request.getContextPath() + "/images/BeginnerAccuracy.jpg";
                        intAccuracy = 0;
                    } else if (accuracyRate >= 25 && accuracyRate < 50) {
                        accuracyImg = request.getContextPath() + "/images/NoviceAccuracy.jpg";
                        intAccuracy = 1;
                    } else if (accuracyRate >= 50 && accuracyRate < 100) {
                        accuracyImg = request.getContextPath() + "/images/RegularAccuracy.jpg";
                        intAccuracy = 2;
                    } else if (accuracyRate >= 100 && accuracyRate < 200) {
                        accuracyImg = request.getContextPath() + "/images/AdvancedAccuracy.jpg";
                        intAccuracy = 3;
                    }else if(accuracyRate >= 200){
                    	accuracyImg = request.getContextPath() + "/images/ExpertAccuracy.jpg";
                    	intAccuracy = 4;
  					}
                    modifiedMember.setAccuracyPhotoPath("<span class='nodisplay'>" + intAccuracy + "</span><img src='" + accuracyImg + "'  class='alignCenter' />");
                    modifiedMember.setVotes(formattedVotes);
                    modifiedMemberList.add(modifiedMember);
                }
                dataTable.setAaData(modifiedMemberList);
                return dataTable;
            } else {
                logger.info(" +++  NULL Leader Board Members");
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Leader Board Members");
            logger.error(e,e);
        }
        return null;
    }

    /**
     * To get customised HP members
     *
     * @param request HttpServletRequest
     * @return DataTable holding the customised data with users
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getAllHPMembers")
    public
    @ResponseBody
    DataTable getAllHPMembers(HttpServletRequest request) {
        try {
            List<Member> membersList = userService.getHPMembers();
            List<Member> modifiedMemberList = new ArrayList<Member>();
            String photoPath = null;
            String languages = null;
            DataTable dataTable = new DataTable();

            if (membersList != null && !membersList.isEmpty()
                    && membersList.size() > 0) {
                logger.info(" +++  Got " + membersList.size() + " HP Members");
                for (int i = 0; i < membersList.size(); i++) {
                    Member modifiedMember = membersList.get(i);
                    photoPath = modifiedMember.getPhotoPath();
                    languages = modifiedMember.getLanguages();
                    modifiedMember.setPhotoPath("<img src='" + photoPath + "' height='38px' width='38px' class='alignCenter' />");
                    if (languages == null || languages == "") {
                        modifiedMember.setLanguages("&nbsp;");
                    } else {
                        modifiedMember.setLanguages(languages.replace('|', ','));
                    }
                    String formattedVotes = Utils.format(modifiedMember.getTotalVotes().intValue());
                    modifiedMember.setVotes(formattedVotes);

                    modifiedMemberList.add(modifiedMember);
                }
                dataTable.setAaData(modifiedMemberList);
                return dataTable;
            } else {
                logger.info(" +++  NULL HP Members");
            }
        }
        catch (Exception e) {
            logger.error("Failed to get HP Members");
        }
        return null;
    }

    /**
     * To request a new term, by a user
     *
     * @param request  HttpServletRequest
     * @param termInfo TermInformation details of s term for a request
     */
    @RequestMapping(method = RequestMethod.POST, value = "/newTermRequest")
    public
    @ResponseBody
    void newTermRequest(@RequestBody TermInformation termInfo,
                        HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = Utils.isNull(session.getAttribute(SessionKey.USER
                    .getKey())) ? null : (User) session
                    .getAttribute(SessionKey.USER.getKey());
            if (user != null) {
                Integer usrRequestCnt = 0;
                mailService.newTermRequest(termInfo, user);
                user = userService.getUser(user.getUserId());
                if (user.getTermRequestCount() != null) {
                    usrRequestCnt = user.getTermRequestCount() + 1;
                } else {
                    usrRequestCnt = 1;
                }
                user.setTermRequestCount(usrRequestCnt);
                userService.updateUser(user);
            }
            logger.info(" +++  Requested for new term");

        }
        catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            logger.error("Failed to request new Term");
        }
    }

    /**
     * To get EmailTemplate look up data
     *
     * @param request HttpServletRequest
     * @return List of EmailTemplates obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getEmailTemplates")
    public
    @ResponseBody
    List<EmailTemplates> getEmailTemplates(HttpServletRequest request) {
        try {
            List<EmailTemplates> emailTemplateList = mailService
                    .getEmailTemplates();
            if (emailTemplateList != null) {
                logger.info(" +++  Got  EmailTemplate  data");
                return emailTemplateList;
            } else {
                logger.info(" +++  NULL  EmailTemplate data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get EmailTemplate data");
        }
        return null;
    }

    /**
     * To get invite user details
     *
     * @param request  HttpServletRequest
     * @param id       String role-type/language id's
     * @param filterBy String to be filtered by role/language
     * @return List of Users to be invited
     */
    @RequestMapping(method = RequestMethod.GET, value = "/inviteUser/{filterBy}/{id}")
    public
    @ResponseBody
    List<User> inviteUsers(@PathVariable String filterBy,
                           @PathVariable String id, HttpServletRequest request) {
        if (id == null) {
            return null;
        }

        Integer[] intIds;
        String[] ids = null;
        ids = id.split(",");
        intIds = new Integer[ids.length];
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        if (user != null) {
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
        }
        Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
        if (companyId == null && company != null) {
            companyId = company.getCompanyId();
        }
        try {
            for (int i = 0; i < ids.length; i++) {
                intIds[i] = Integer.parseInt(ids[i]);
            }
            Integer arry[] = null;
            if (companyId != null) {
                arry = new Integer[1];
                arry[0] = companyId;
            } else {
                arry = new Integer[1];
                arry[0] = null;
            }

            Set<String> roleNames = new HashSet<String>();
            roleNames.add(RoleEnum.SUPER_ADMIN.getValue());
            roleNames.add(RoleEnum.COMPANY_ADMIN.getValue());
            roleNames.add(RoleEnum.COMPANY_TERM_MANAGER.getValue());
            Set<Integer> roleIds = userService.getRoleIdListByLabel(roleNames);
            List<User> inviteUser = userService.inviteUser(filterBy, intIds, arry[0], roleIds);
            if (inviteUser != null) {
                logger.info(" +++  Got invite user details" + intIds);
                return inviteUser;
            } else {
                logger.info(" +++  NULL invite users");
            }

        }
        catch (Exception e) {
            logger.error(e);
            logger.error("Failed to invite users");
        }
        return null;
    }

    /**
     * To invite users for polling
     *
     * @param request HttpServletRequest
     * @param invite  Invite holding user id's, mail id's and the email template
     */
    @RequestMapping(method = RequestMethod.POST, value = "/inviteUsersToVote")
    public
    @ResponseBody
    String inviteUsersToVote(@RequestBody Invite invite,
                             HttpServletRequest request) {
        String status = "";
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            
            String userMailId = (String) session
                    .getAttribute(SessionKey.USER_MAIL_ID.getKey());
            Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                    .getKey())) ? null : (Integer) session
                    .getAttribute(SessionKey.USER_ID.getKey());


            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, invite.getVotingPeriod());
            date = calendar.getTime();
            try {

                TermVoteMaster termVoteMaster = new TermVoteMaster();
                termVoteMaster.setInvitedDate(new Date());
                termVoteMaster.setVotingExpiredDate(date);
                termVoteMaster.setInvitedBy(userId);
                termVoteMaster.setIsActive("Y");

                Integer[] userIds = termsService.getInvitedUsers(invite);
                invite.setUserIds(userIds);
                termsService.inviteToVote(termVoteMaster, invite);
                logger.info(" +++  +++Step:8  send mail to users ");
                
                String contextPath=request.getContextPath();
                invite.setCompanyContext(contextPath);
                if(user!=null && userId != 1){
                		Company company=user.getCompany();
                		invite.setCompanyId(company.getCompanyId());
                		invite.setCompanyRoot(company.getContextRoot());
                		
                } else {
                    invite.setCompanyId(1);	
                }
                status = mailService.inviteUser(invite, userMailId);
            }
            catch (Exception e) {
                logger.error(e,e);
            }

            if (status == null || status.equalsIgnoreCase("failed")) {
                logger.info(" +++  Got null status while inviting users to vote");
            } else {
                logger.info(" +++  Successfully invited users to vote");
            }

        }
        catch (Exception e) {
            logger.error(e,e);
            logger.error("Failed to invite users to vote");
        }
        return status;

    }

    /**
     * To get Domian lookup data
     *
     * @param request HttpServletRequest
     * @return List of Domain obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getDomainLookUp")
    public
    @ResponseBody
    List<Domain> getDomianLookUp(HttpServletRequest request) {
        try {
            List<Domain> domain_list = lookUpService.getDomainLookUp();
            if (domain_list != null) {
                logger.info(" +++  Got Domain data");
                return domain_list;
            } else {
                logger.info(" +++  NULL Domain data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Domains");
        }
        return null;
    }

    /**
     * To get manage poll terms
     *
     * @param request       HttpServletRequest
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of debated terms that needs to manage
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getManagePollTerms")
    public
    @ResponseBody
    TMProfileTermsResult getManagePollTerms(@RequestBody QueryAppender queryAppender,
                                            HttpServletRequest request) {

        TMProfileTermsResult tmProfileTermsResult = null;
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null) {
                    companyId = user.getCompany().getCompanyId();

                }
            }
            queryAppender.setSearchIndexType(IndexTypeEnum.TB.name());
            tmProfileTermsResult = termsService.getManagePollTerms(queryAppender, companyId, user);
            if (tmProfileTermsResult != null
                    ) {
                logger.info(" +++  Got " + tmProfileTermsResult.getTotalResults());
                return tmProfileTermsResult;
            } else {
                logger.info(" +++  NULL manage Poll Terms ");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to manage poll terms");
        }
        return null;

    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getManagePollTermsForPagination")
    public
    @ResponseBody
    TMProfileTermsResult getManagePollTermsForPagination(@RequestBody QueryAppender queryAppender,
            HttpServletRequest request) {
        TMProfileTermsResult tmProfileTermsResult = null;
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null) {
                    companyId = user.getCompany().getCompanyId();

                }
            }
            queryAppender.setSearchIndexType(IndexTypeEnum.TB.name());
            tmProfileTermsResult = termsService.getManagePollTermsForPagination(queryAppender, companyId, user);
            if (tmProfileTermsResult != null
                    ) {
                logger.info(" +++  Got " + tmProfileTermsResult.getTotalResults());
                return tmProfileTermsResult;
            } else {
                logger.info(" +++  NULL manage Poll Terms ");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to manage poll terms");
        }
        return null;
    }

    /**
     * To delete terms
     *
     * @param request HttpServletRequest
     * @param termIds Array of integer holding termIds to be deleted
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteTerms/{termIds}")
    public
    @ResponseBody
    void deleteTerms(@PathVariable Integer[] termIds,
                     HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            
            logger.info(" +++  Called the controller /deleteTerms/ to delete selected Terms.....");
            
            @SuppressWarnings("unchecked")
			List<Pair<String, String>> loggerMessages = Arrays.asList(
            		new Pair<String,String>(ParamKeyEnum.DELETED_TERM_ID.toString(),Arrays.asList(termIds).toString()),
            		new Pair<String,String>(ParamKeyEnum.DELETED_BY.toString(),user.getUserName()),
            		new Pair<String,String>(ParamKeyEnum.DELETED_DATE.toString(),new Date().toString()),
            		new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
            		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
            		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
            		new Pair<String,String>(ParamKeyEnum.USER_NAME.toString(),user.getUserName()),
            		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
            		);
            
            logger.info(" +++  +++ Deleted Term details +++ "+ loggerMessages);
            
            if (termIds.length != 0) {
                termsService.deleteTerms(termIds,user);
                logger.info(" +++  Successfully deleted the terms");
            }
        }
        catch (Exception e) {
            logger.error("Failed to delete the term");
        }

    }

    /**
     * To get user voted termsupdateTmDetails
     *
     * @param request    HttpServletRequest
     * @param languageId String to filter terms respectively
     * @param colName    column name that has to be sorted
     * @param sortOrder  order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @return List of terms that are voted by the user
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUserVotedTerms/{languageId}/{colName}/{sortOrder}/{pageNum}")
    public
    @ResponseBody
    List<PollTerms> getUserVotedTerms(@PathVariable String languageId, @PathVariable String colName, @PathVariable String sortOrder,
                                      @PathVariable Integer pageNum, HttpServletRequest request) {
        if (languageId == null) {
            return null;
        }
        List<PollTerms> user_voted_terms_list = null;
        try {
            HttpSession session = request.getSession();
            Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID.getKey())) ? null : (Integer) session
                    .getAttribute(SessionKey.USER_ID.getKey());

            user_voted_terms_list = termsService.getUserVotedTerms(languageId, colName, sortOrder, pageNum, userId);
            if (user_voted_terms_list != null && !user_voted_terms_list.isEmpty() && user_voted_terms_list.size() > 0) {
                logger.info(" +++  Got " + user_voted_terms_list.size() + " Expired Poll Terms for language ids " + languageId);
                return user_voted_terms_list;
            } else {
                logger.info(" +++  NULL user voted Terms for Language Id " + languageId);
            }

        }
        catch (Exception e) {
            logger.error("Failed to user voted terms  by language");
        }
        return null;

    }

    /**
     * To get top register languages
     *
     * @param request HttpServletRequest
     * @return List of top six registered languages
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTopRegLangs")
    public
    @ResponseBody
    List<Language> getTopRegLangs(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            List<Language> topRegLangs = termsService.getTopRegLangs(companyId);
            if (topRegLangs != null && !topRegLangs.isEmpty()
                    && topRegLangs.size() > 0) {
                logger.info(" +++  Got " + topRegLangs.size()
                        + " Top register languages");
                return topRegLangs;
            } else {
                logger.info(" +++  NULL Top register languages");
            }

        }
        catch (Exception e) {
            logger.error("Failed to get Top Terms");
        }
        return null;
    }

    /**
     * To delete users
     *
     * @param request HttpServletRequest
     * @param userIds array of integer userIds that needs to be deleted
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteUsers/{userIds}")
    public
    @ResponseBody
    void deleteUsers(@PathVariable Integer[] userIds, HttpServletRequest request) {
        try {
        	User user = (User)request.getSession().getAttribute(SessionKey.USER.getKey());
        	
        	Integer userId = user.getUserId();
        	
        	logger.info("Logged in user deleting selected users ");

        	logger.info("Called the  /deleteUsers/ to delete  selected user.....");

        	@SuppressWarnings("unchecked")
        	List<Pair<String, String>> loggerMessages = Arrays.asList(
        			new Pair<String,String>(ParamKeyEnum.DELETED_USER_IDS.toString(),Arrays.asList(userIds).toString()),
        			new Pair<String,String>(ParamKeyEnum.DELETED_BY.toString(),user.getUserName()),
        			new Pair<String,String>(ParamKeyEnum.DELETED_DATE.toString(),new Date().toString()),
        			new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
        			new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
        			new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
        			new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
        			);

        	logger.info(loggerMessages);
        	
            if (userIds.length != 0) {
                userService.deleteUsers(userIds,userId);
                termsService.deleteTermVoteDetailsForUser(userIds,userId);
            }
        }
        catch (Exception e) {
            logger.error("Failed to delete the user");
        }

    }

    /**
     * To get EmailTemplate data
     *
     * @param request         HttpServletRequest
     * @param emailTemplateId Integer to identify the email template id
     * @return EmailTemplates w.r.t the emailTemplateId
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getEmailTemplates/{emailTemplateId}")
    public
    @ResponseBody
    EmailTemplates getEmailTemplate(@PathVariable Integer emailTemplateId,
                                    HttpServletRequest request) {
        try {
            EmailTemplates email_template_list = lookUpService
                    .getEmailTemplate(emailTemplateId);
            if (email_template_list != null) {
                logger.info(" +++  Got  EmailTemplate  data");
                return email_template_list;
            } else {
                logger.info(" +++  NULL  EmailTemplate data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get EmailTemplate data");
        }
        return null;
    }

    /**
     * Request for password
     *
     * @param request HttpServletRequest
     * @param mailId  String to which  the mail is sent with new password
     * @return String of the path to be redirected
     */
    @RequestMapping(method = RequestMethod.GET, value = "/requestPassword/{mailId}/{userName}/{isHp}")
    public
    @ResponseBody
    String requestPassword(@PathVariable String mailId,
                           @PathVariable String userName, @PathVariable String isHp, HttpServletRequest request) {
        String redirect = "";
        String pageUrl = request.getContextPath();
        HttpSession session = request.getSession();
        try {
            String newPassword = userName.toLowerCase() + "4Te@m";
            User user = new User();
            user.setUserName(userName);
            user.setPassword(newPassword);
            user.setEmailId(mailId);
            /*Company company = Utils.isNull(session.getAttribute(SessionKey.COMPANY
                    .getKey())) ? null : (Company) session
                    .getAttribute(SessionKey.COMPANY.getKey());*/
            boolean userExistsFlag = userService.ifUserExists(user
                    .getUserName());

            if (userExistsFlag) {
                String status = userService.changePassword(user);
                if (status.equalsIgnoreCase("success")) {
                    user.setPassword(newPassword);
                    mailService.requestPassword(user);
                    //if (company != null && company.getContextRoot() != null) {
                    if ("true".equalsIgnoreCase(isHp)) {
                        pageUrl = request.getContextPath() + "/hp";
                    }
                    redirect = pageUrl + TeaminologyPage.INDEX.getPageUrl();
                }
            } else {
                //if (company != null && company.getContextRoot() != null) {
                if ("true".equalsIgnoreCase(isHp)) {
                    pageUrl = request.getContextPath() + "/hp";
                }
                session.setAttribute("invalidUserNameForForgot", "invalidUserName");
                redirect = pageUrl + TeaminologyPage.FORGOT_PASSWORD_DENIED.getPageUrl();
            }
        }
        catch (Exception e) {
            logger.error("Failed to reset new password");
        }
        return redirect;
    }

    /**
     * Used for domain Create/Update/Delete operations
     *
     * @param request HttpServletRequest
     * @param domain  domain obj to me modified
     * @return String of the path to be redirected
     */
    @RequestMapping(method = RequestMethod.POST, value = "/setDomainCUD")
    public
    @ResponseBody
    String setDomainCUD(@RequestBody Domain domain, HttpServletRequest request,
                        HttpServletResponse response) {
        if (domain == null) {
            return null;
        }
        String status = "";
        String domainLabel = domain.getDomain();
        boolean domainExistsFlag = false;
        HttpSession session = request.getSession();
        
        User loggedUserObj = (User)session.getAttribute(SessionKey.USER.getKey());
        
        Integer user_id = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                .getKey())) ? null : (Integer) session
                .getAttribute(SessionKey.USER_ID.getKey());

        try {
            if (domain.getTransactionType().equalsIgnoreCase("Edit Domain")) {
                Domain domain1 = lookUpService.getDomainById(domain.getDomainId());
                if (!domain1.getDomain().equalsIgnoreCase(domain.getDomain())) {
                    domainExistsFlag = lookUpService.isDomainExists(domain.getDomain());
                }
            }
            if (domain.getTransactionType().equalsIgnoreCase("Add Domain")) {
                domainExistsFlag = lookUpService.isDomainExists(domainLabel);
            }

            if (!domainExistsFlag) {
                status = lookUpService.setDomainCUD(domain, user_id);
                if (status.equalsIgnoreCase("success")) {
                    if (domain.getTransactionType().equalsIgnoreCase("Delete Domain")) {
                        List<Integer> userIds = userService.getUserByDomain(domain.getDomainId());
                        if (userIds != null && userIds.size() != 0) {
                            Integer[] intUserIds = new Integer[userIds.size()];
                            for (int i = 0; i < userIds.size(); i++) {
                                intUserIds[i] = userIds.get(i);
                            }
                            
                        	logger.info("Called the  /setDomainCUD/ to "+domain.getTransactionType());

                        	@SuppressWarnings("unchecked")
                        	List<Pair<String, String>> loggerMessages = Arrays.asList(
                        			new Pair<String,String>(ParamKeyEnum.DELETED_USER_IDS.toString(),Arrays.asList(userIds).toString()),
                        			new Pair<String,String>(ParamKeyEnum.DELETED_BY.toString(),loggedUserObj.getUserName()),
                        			new Pair<String,String>(ParamKeyEnum.DELETED_DATE.toString(),new Date().toString()),
                        			new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),loggedUserObj.getUserId().toString()),
                        			new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),loggedUserObj.getCompany().getCompanyId().toString()),
                        			new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),loggedUserObj.getCompany().getCompanyName()),
                        			new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
                        			);

                        	logger.info(loggerMessages);
                            
                            
                            userService.deleteUsers(intUserIds , loggedUserObj.getUserId());
                            termsService.deleteTermVoteDetailsForUser(intUserIds , loggedUserObj.getCompany().getCompanyId());
                        }
                    }/* else {
                        TeaminologyBL.buildIndexForTmprofile();
                    }*/
                    status = "success";
                }
            } else {
                if (domainExistsFlag) {
                    status = "Domain exists";
                } else {
                    status = "failed";
                }
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Domains");
        }
        return status;
    }

    /**
     * To get team member details
     *
     * @param request    HttpServletRequest
     * @param languageId String to filter terms respectively
     * @param colName    column name that has to be sorted
     * @param sortOrder  order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @return List of users other than administrator
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTeamMemberDetails/{languageId}/{companyIds}/{colName}/{sortOrder}/{pageNum}")
    public
    @ResponseBody
    List<Member> getTeamMemberDetails(@PathVariable String languageId, @PathVariable String companyIds,
                                      @PathVariable String colName, @PathVariable String sortOrder,
                                      @PathVariable Integer pageNum, HttpServletRequest request) {
        List<Member> teamMembersList = null;
        try {
        	
        	if(languageId == null || languageId.equals("0")) {
        		languageId = "all";
        	}
        	
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
                teamMembersList = userService.getTeamMemberDetails(languageId, companyIds,
                        colName, sortOrder, pageNum, user.getUserId(), companyId);
            }


            if (teamMembersList != null && !teamMembersList.isEmpty()
                    && teamMembersList.size() > 0) {
                logger.info(" +++  Got " + teamMembersList.size()
                        + " Expired Poll Terms for language ids " + languageId);
                return teamMembersList;
            } else {
                logger.info(" +++  NULL user voted Terms for Language Id "
                        + languageId);
            }

        }
        catch (Exception e) {
            logger.error("Failed to user voted terms  by language");
        }
        return null;
    }

    /**
     * Used for Language Create/Update/Delete operations
     *
     * @param request  HttpServletRequest
     * @param language Language object need to be modified
     * @return String of the path to be redirected
     */
    @RequestMapping(method = RequestMethod.POST, value = "/setLanguageCUD")
    public
    @ResponseBody
    String setLanguageCUD(@RequestBody Language language,
                          HttpServletRequest request, HttpServletResponse response) {
        if (language == null) {
            return null;
        }
        String status = "";
        String languageLabel = language.getLanguageLabel();
        String languageCode = language.getLanguageCode();
        boolean languageExistsFlag = false;
        boolean codeExistsFlag = false;
        Integer userId = null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        if (user != null) {
            userId = user.getUserId();
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
        }
        
        logger.info("Called the controller /setLanguageCUD/ to delete/edit/add By language.....");
        
        @SuppressWarnings("unchecked")
		List<Pair<String, String>> loggerMessages = Arrays.asList(
        		new Pair<String,String>(ParamKeyEnum.LANGUAGE_LABEL.toString(),languageLabel),
        		new Pair<String,String>(ParamKeyEnum.LANGUAGE_CODE.toString(),languageCode),
        		new Pair<String,String>(ParamKeyEnum.ADDED_BY.toString(),user.getUserName()),
        		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
        		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
        		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
        		);
        
        logger.info("Edit lanuage::"+loggerMessages);
        
        try {

            if (language.getTransactionType().equalsIgnoreCase("Edit Language")) {
                Language lang = lookUpService.getLanguage(language.getLanguageId());
                if (!lang.getLanguageLabel().equalsIgnoreCase(language.getLanguageLabel())) {
                    languageExistsFlag = lookUpService.isLanguageLabelExists(languageLabel);
                }
                if (!lang.getLanguageCode().equalsIgnoreCase(language.getLanguageCode())) {
                    codeExistsFlag = lookUpService.isLanguageCodeExists(languageCode);
                }
                logger.info("Edit language ++++ ");
            }

            if (language.getTransactionType().equalsIgnoreCase("Add Language")) {
                languageExistsFlag = lookUpService.isLanguageLabelExists(languageLabel);
                codeExistsFlag = lookUpService.isLanguageCodeExists(languageCode);
            }

            if (!languageExistsFlag && !codeExistsFlag) {
                status = lookUpService.setLanguageCUD(language, userId);
                if (status.equalsIgnoreCase("success")) {
                    if (language.getTransactionType().equalsIgnoreCase("Delete Language")) {
                        List<Integer> termIds = termsService.getTermInformationByLanguage(language.getLanguageId(), companyId);
                        if (termIds != null && termIds.size() != 0) {
                            Integer[] intTermIds = new Integer[termIds.size()];
                            for (int i = 0; i < termIds.size(); i++) {
                                intTermIds[i] = termIds.get(i);
                            }
                            
                            @SuppressWarnings("unchecked")
							List<Pair<String,String>> loggerMsg = Arrays.<Pair<String,String>>asList(
                            		new Pair<String,String>(ParamKeyEnum.DELETED_TERM_ID.toString(),Arrays.asList(intTermIds).toString()));
                            
                            logger.info("Deleted terms+++++"+loggerMsg);
                            
                            termsService.deleteTerms(intTermIds, user);
                        }
                    }
                    TeaminologyPage.ADMIN_CONFIGURATION_SUCCESS.getPageUrl();
                }
            } else {
                if (languageExistsFlag) {
                    status = "Label exists";
                }
                if (codeExistsFlag) {
                    if (status != "") {
                        status = status + ',' + " Code exists";
                    } else {
                        status = "Code exists";
                    }
                }
                if (!languageExistsFlag && !codeExistsFlag) {
                    status = "failed";
                }
            }
        }
        catch (Exception e) {
            status = "failed";
            logger.error("Failed to get languages");
            logger.error(e,e);
        }
        return status;
    }

    /**
     * Used for EmailTemplate Create/Update/Delete operations
     *
     * @param request       HttpServletRequest
     * @param emailTemplate Email Template need to be modified
     * @return String of the path to be redirected
     */
    @RequestMapping(method = RequestMethod.POST, value = "/setEmailTemplateCUD")
    public
    @ResponseBody
    String setEmailTemplateCUD(@RequestBody EmailTemplates emailTemplate,
                               HttpServletRequest request, HttpServletResponse response) {
        if (emailTemplate == null) {
            return null;
        }
        String status;
        HttpSession session = request.getSession();
        Integer user_id = Utils.isNull(session.getAttribute(SessionKey.USER_ID.getKey())) ? null : (Integer) session.getAttribute(SessionKey.USER_ID.getKey());

        try {
            if (emailTemplate.getTransactionType().equalsIgnoreCase("Edit Template")) {
                EmailTemplates template = lookUpService.getEmailTemplate(emailTemplate.getEmailTemplateId());
                template.setEmailMessageContent(emailTemplate.getEmailMessageContent());
                template.setTransactionType("Edit Template");
                status = lookUpService.setEmailTemplateCUD(template, user_id);
            } else {
                status = lookUpService.setEmailTemplateCUD(emailTemplate, user_id);
            }
            if (status.equalsIgnoreCase("success")) {
                TeaminologyPage.ADMIN_CONFIGURATION_SUCCESS.getPageUrl();
            }
        }
        catch (Exception e) {
            logger.error("Failed to get email templates");
        }
        return null;
    }

    /**
     * To update the user
     *
     * @param request HttpServletRequest
     * @param userId  Integer to identify User
     */
    @RequestMapping(method = RequestMethod.GET, value = "/updateUser/{userId}/{firstName}/{lastName}/{userName}/{emailId}/{password}/{userLanguages}/{userTypeId}/{userDomainId}/{companyId}/{userCompanyList}")
    public
    @ResponseBody
    String updateUser(@PathVariable Integer userId, @PathVariable String firstName, @PathVariable String lastName, @PathVariable String userName,
                      @PathVariable String emailId, @PathVariable String userLanguages, @PathVariable Integer userTypeId, @PathVariable Integer userDomainId, @PathVariable Integer companyId, @PathVariable String userCompanyList, @PathVariable String password,
                      HttpServletRequest request) {
        HttpSession session = request.getSession();
        User loggedInUser = (User)session.getAttribute(SessionKey.USER.getKey());
        User user = null;
        Company companyUser = null;
        String userLangs = "";
        String status = null;
        String usersRole = "";
        String fName = null;
        String lName = null;
        

        logger.info("Called the controller /updateUser / to modified user details.....");

        @SuppressWarnings("unchecked")
        List<Pair<String, String>> loggerMessages = Arrays.asList(
        		new Pair<String,String>(ParamKeyEnum.UPDATED_USER_NAME.toString(),userName),
        		new Pair<String,String>(ParamKeyEnum.UPDATED_USER_ID.toString(),userId.toString()),
        		new Pair<String,String>(ParamKeyEnum.UPDATED_BY.toString(),loggedInUser.getUserName()),
        		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),loggedInUser.getUserId().toString()),
        		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),loggedInUser.getCompany().getCompanyId().toString()),
        		new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),loggedInUser.getCompany().getCompanyName()),
        		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request)),
        		new Pair<String,String>(ParamKeyEnum.UPDATED_DATE.toString(),new Date().toString())
        		);


        logger.info(loggerMessages);
        
        if (firstName != null && !firstName.equalsIgnoreCase("null") && firstName.trim().length() != 0) {
            fName = firstName;
        }
        if (lastName != null && !lastName.equalsIgnoreCase("null") && lastName.trim().length() != 0) {
            lName = lastName;
        }
        boolean userExistsFlag = false;
        boolean emailExistsFlag = false;
        Integer updateUserId = Utils.isNull(session.getAttribute(SessionKey.USER_ID.getKey())) ? null : (Integer) session.getAttribute(SessionKey.USER_ID.getKey());
        if (userId == 0) {
            user = Utils.isNull(session.getAttribute(SessionKey.USER.getKey())) ? null :
                    (User) session.getAttribute(SessionKey.USER.getKey());
        } else {
            user = userService.getUser(userId);
        }
        if (!userName.equalsIgnoreCase(user.getUserName())) {
            userExistsFlag = userService.ifUserExists(userName);
        }
        if (!emailId.equalsIgnoreCase(user.getEmailId())) {
            emailExistsFlag = userService.ifEmailExists(emailId);
        }
        if (!Utils.isNull(user)) {
            try {
                Set<UserLanguages> userLanguagesSet = new HashSet<UserLanguages>();
                Set<UserRole> userRolesSet = new HashSet<UserRole>();
                Set<CompanyTransMgmt> userCompanyTransMgmt = new HashSet<CompanyTransMgmt>();
                user = userService.getUser(user.getUserId());
                Role superTranslatorRole = userService.getRoleIdByLabel(RoleEnum.SUPER_TRANSLATOR.getValue());

                if (user != null && !userExistsFlag && !emailExistsFlag) {
                    userService.deleteUserLanguages(user.getUserId());
                    if (userId != 0) {
                        userService.deleteCompanyTransUsers(user.getUserId());
                    }
                    if (userLanguages != null && !userLanguages.equalsIgnoreCase("null")) {
                        String lanuguages[] = userLanguages.split(",");
                        for (String id : lanuguages) {
                            Language language = lookUpService.getLanguage(new Integer(id));

                            UserLanguages userLang = new UserLanguages();
                            userLang.setLangId(id);
                            userLang.setLanguages(language);
                            userLang.setIsActive("Y");
                            userLang.setUserId(user.getUserId());
                            userLang.setCreateDate(user.getCreateTime());
                            userLang.setUpdateDate(new Date());
                            userLang.setUpdatedBy(updateUserId);
                            userLang.setCreatedBy(user.getUserId());
                            userLanguagesSet.add(userLang);
                            userLangs += language.getLanguageLabel() + ", ";
                            userService.saveOrUpdateUserLanguage(userLang);

                        }

                    }
                    if (userLangs.contains(","))
                        userLangs = userLangs.substring(0, userLangs.lastIndexOf(","));
                    if (userCompanyList != null && !userCompanyList.equalsIgnoreCase("null")) {
                        Integer roleId = superTranslatorRole.getRoleId();
                        if (userTypeId.intValue() == roleId.intValue()) {
                            String userCompanies[] = userCompanyList.split(",");
                            for (String id : userCompanies) {
                                CompanyTransMgmt companyTransMgmt = new CompanyTransMgmt();
                                companyTransMgmt.setCompanyId(new Integer(id));
                                companyTransMgmt.setUserId(user.getUserId());
                                companyTransMgmt.setIsActive("Y");
                                companyTransMgmt.setIsExternal("N");
                                userCompanyTransMgmt.add(companyTransMgmt);
                                userService.saveOrUpdateCompanyTransMgmt(companyTransMgmt);
                            }
                        }
                    }

                    user.setFirstName(fName);
                    user.setLastName(lName);
                    user.setUserName(userName);
                    user.setEmailId(emailId);
                    user.setUserLanguages(userLanguagesSet);
                    if (userDomainId != 0) {
                        user.setUserDomainId(userDomainId);
                    } else {
                        user.setUserDomainId(null);
                    }
                    if (companyId != 0) {
                        CompanyTransMgmt companyTransMgmt = new CompanyTransMgmt();
                        companyTransMgmt.setCompanyId(companyId);
                        companyTransMgmt.setUserId(user.getUserId());
                        companyTransMgmt.setIsActive("Y");
                        companyTransMgmt.setIsExternal("Y");
                        userCompanyTransMgmt.add(companyTransMgmt);
                        userService.saveOrUpdateCompanyTransMgmt(companyTransMgmt);
                        userCompanyTransMgmt.add(companyTransMgmt);
                        companyUser = lookUpService.getCompanyById(companyId);
                        user.setCompany(companyUser);
                    }
                    user.setCompanyTransMgmt(userCompanyTransMgmt);

                    if (password != null && !password.equalsIgnoreCase("null")) {
                        String usrPassword = Utils.encryptPassword(password);
                        user.setPassword(usrPassword);
                    }

                    if (userId != 0) {
                        if (userTypeId != null) {
                            String roleType[] = userTypeId.toString().split(",");
                            Set<UserRole> userRoleList = loginService.getUserRole(user.getUserId());
                            //Need to change this logic
                            for (String id : roleType) {
                                Role role = lookUpService.getUserRole(new Integer(id));
                                for (UserRole userRole : userRoleList) {
                                    userRole.setRole(role);
                                    userRole.setUserId(user.getUserId());
                                    userRole.setUpdateDate(new Date());
                                    userRole.setUpdatedBy(updateUserId);
                                    userRolesSet.add(userRole);
                                    usersRole += role.getRoleName() + ", ";
                                    userService.saveOrUpdateUserRole(userRole);
                                }
                            }

                        }
                    }
                    if (usersRole.contains(","))
                        usersRole = usersRole.substring(0, usersRole.lastIndexOf(","));

                    user.setUpdateDate(new Date());
                    user.setUpdatedBy(updateUserId);

                    userService.updateUser(user);
                    if (userId == 0) {
                        session.setAttribute(SessionKey.USER.getKey(), user);
                    }
                    status = "success";

                } else {
                    if (userExistsFlag) {
                        status = "User exists";
                    }
                    if (emailExistsFlag) {
                        if (status != "") {
                            status = status + ',' + " Email exists";
                        } else {
                            status = "Email exists";
                        }
                    }
                }

            }
            catch (Exception e) {
            	logger.info("Failed to Update User");
            	logger.error(e,e);
            }
        }
        return status;
    }


    /**
     * To update password of the user
     *
     * @param request HttpServletRequest
     * @return String holding the status of the update
     */
    @RequestMapping(method = RequestMethod.POST, value = "/updatePwd")
    public
    @ResponseBody
    String updatePwd(@RequestBody String[] passwordObject, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = Utils.isNull(session.getAttribute(SessionKey.USER.getKey())) ? null :
                (User) session.getAttribute(SessionKey.USER.getKey());
        String oldPassword = Utils.encryptPassword(passwordObject[0]);
        String newPassword = Utils.encryptPassword(passwordObject[1]);
        String status = null;
        if (!Utils.isNull(user)) {
            try {
                user = userService.getUser(user.getUserId());
                if (user != null) {
                    if (user.getPassword().equalsIgnoreCase(oldPassword)) {
                        user.setPassword(newPassword);
                        user.setUpdateDate(new Date());
                        userService.updateUser(user);
                        logger.info(" +++  Updated successfully");
                        status = "success";
                    } else {
                        logger.info(" +++  Old Password doesnt Match");
                        status = "failure";
                    }

                }

            }
            catch (Exception e) {
                status = "failure";
                e.printStackTrace();

            }
        }
        return status;
    }

    /**
     * To get User details
     *
     * @param request HttpServletRequest
     * @param userId  Integer to identify the user
     * @return User w.r.t the userId
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUser/{userId}")
    public
    @ResponseBody
    User getUserDetails(@PathVariable Integer userId, HttpServletRequest request) {
        User user = null;
        Set<CompanyTransMgmt> companyTransMgmtUsers = new HashSet<CompanyTransMgmt>();
        if (!Utils.isNull(userId)) {
            try {
                user = userService.getUser(userId);
                companyTransMgmtUsers = userService.getCompanyTransMgmtUsers(userId);
                user.setCompanyTransMgmt(companyTransMgmtUsers);

            }
            catch (Exception e) {
                logger.error("Error in getting user details using userId");

            }
        }
        return user;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getSuperTranslatorCompanyList")
    public
    @ResponseBody
    List<Company> getSuperTranslatorCompanyList(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());

        Set<CompanyTransMgmt> companyTransMgmtUsers = null;
        Set<Integer> companyIds = new HashSet<Integer>();
        List<Company> companyList = new ArrayList<Company>();
        if (!Utils.isNull(user) && !Utils.isNull(user.getUserId())) {
            try {
                companyTransMgmtUsers = userService.getCompanyTransMgmtUsers(user.getUserId());
                if (!Utils.isNull(companyTransMgmtUsers) && !companyTransMgmtUsers.isEmpty()) {
                    companyIds = new HashSet<Integer>();
                    for (CompanyTransMgmt companyTransObj : companyTransMgmtUsers) {
                        companyIds.add(companyTransObj.getCompanyId());
                    }
                    if (!Utils.isNull(companyIds) && !companyIds.isEmpty()) {
                        companyList = lookUpService.getCompanyListObj(companyIds);

                    }
                }


            }
            catch (Exception e) {
                logger.error("Error in getting superTranslator company details");

            }
        }
        return companyList;
    }

    /**
     * To get current top 20 terms report
     *
     * @param request HttpServletRequest
     * @return List of data holding the terms per month within a duration of one year
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getLanguageChartData")
    public
    @ResponseBody
    List<LanguageReportData> getLanguageChartData(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        if (user != null) {
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
        }
        List<LanguageReportData> languageReportDataList = null;
        languageReportDataList = new ArrayList<LanguageReportData>();
        try {
            List<Language> topRegLangs = (List<Language>) termsService.getTopRegLangs(companyId);

            List<Terms> termsList = new ArrayList<Terms>();
            for (Language language : topRegLangs) {
                Set<Integer> languageIds = new HashSet<Integer>();
                languageIds.add(language.getLanguageId());
                List<Integer> termInformationList = termsService.getTermInformationByLanguage(language.getLanguageId(), companyId);
                List<TermVoteMaster> termVoteMasterList = termsService.getTermVoteMasterByTermInfo(termInformationList, "ChartData");
                List<TermInformation> termInformationListPerMonth = termsService.getTermInformationPerMonth(languageIds, companyId);
                termsList = termsService.getTermList(termVoteMasterList, termInformationListPerMonth);
                LanguageReportData languageReportData = new LanguageReportData();
                languageReportData.setLanguage(language);
                languageReportData.setTermsList(termsList);
                languageReportDataList.add(languageReportData);
            }

        }
        catch (Exception e) {
            logger.error("Failed to get Top Terms");
        }
        if (languageReportDataList != null && !languageReportDataList.isEmpty())
            session.setAttribute(SessionKey.MONTH_TO_MONTH.getKey(), languageReportDataList);
        return languageReportDataList;
    }

    /**
     * To get vote configuration data
     *
     * @param request HttpServletRequest
     * @return VoteConfig object
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getVoteConfig")
    public
    @ResponseBody
    VoteConfig getVoteConfig(HttpServletRequest request) {
        VoteConfig voteConfig = null;
        try {
            voteConfig = lookUpService.getVoteConfig();
        }
        catch (Exception e) {
            logger.error("Failed to get vote config details");
        }
        return voteConfig;
    }

    /**
     * To set vote configuration Update/delete Operations
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param voteConfig VoteConfig that needs to be updated
     * @return of the path to be redirected
     */
    @RequestMapping(method = RequestMethod.POST, value = "/setVoteConfigUD")
    public
    @ResponseBody
    String setVoteConfigUD(@RequestBody VoteConfig voteConfig,
                           HttpServletRequest request, HttpServletResponse response) {
        if (voteConfig == null) {
            return null;
        }
        HttpSession session = request.getSession();
        Integer userid = Utils.isNull(session.getAttribute(SessionKey.USER_ID.getKey())) ? null : (Integer) session
                .getAttribute(SessionKey.USER_ID.getKey());

        try {
             lookUpService.setVoteConfigUD(voteConfig, userid);
        }
        catch (Exception e) {
            logger.error("Failed to get vote config details");
        }
        return null;
    }

    /**
     * To get language lookup data
     *
     * @param request   HttpServletRequest
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of sorted languages
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getSortedLanguages/{colName}/{sortOrder}/{pageNum}")
    public
    @ResponseBody
    List<Language> getLanguages(@PathVariable String colName, @PathVariable String sortOrder, @PathVariable Integer pageNum, HttpServletRequest request) {
        try {
            List<Language> languages = lookUpService.getSortedLanguages(colName, sortOrder, pageNum);
            if (languages != null) {
                logger.info(" +++  Got languages data");
                return languages;
            } else {
                logger.info(" +++  NULL languages data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to getlLanguages");
        }
        return null;
    }

    /**
     * To get language lookup data
     *
     * @param request   HttpServletRequest
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of sorted languages
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getSortedCompanys/{colName}/{sortOrder}/{pageNum}")
    public
    @ResponseBody
    List<Object> getSortedCompanys(@PathVariable String colName, @PathVariable String sortOrder, @PathVariable Integer pageNum, HttpServletRequest request) {
        try {
            List<Company> companies = lookUpService.getCompanyLookUp();
            List<Object> companyList =new ArrayList<Object>();
            Integer   totalResults = companies.size();
          
            int limitFrom = 0;
            int limitTo = 10;
            if (pageNum != 0) {
                limitFrom = (pageNum - 1) * 10;
                limitTo = (pageNum) * 10;
            }
            if (limitTo > companies.size()) {
                limitTo = companies.size();
            }
            companies = companies.subList(limitFrom, limitTo);
            companyList.add(companies);
            companyList.add(totalResults);
            if (companies != null) {
                logger.debug("Got languages data");
                return companyList;
            } else {
                logger.info(" +++  NULL languages data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to getlLanguages");
        }
        return null;
    }


    /**
     * To get getLanguagePiChart
     *
     * @param request HttpServletRequest
     * @return List of data holding the Pi chart details
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getLanguagePiChart")
    public
    @ResponseBody
    List<LanguagePiChartData> getLanguagePiChart(HttpServletRequest request) {
        List<LanguagePiChartData> languagePiChartDataList = null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        if (user != null) {
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
        }
        languagePiChartDataList = new ArrayList<LanguagePiChartData>();
        try {
            List<Language> topRegLangs = (List<Language>) termsService.getTopRegLangs(companyId);
            Set<Integer> topRegLangIds = new HashSet<Integer>();
            for (Language language : topRegLangs) {
                topRegLangIds.add(language.getLanguageId());
                List<Integer> termInformationList = termsService.getTermInformationByLanguage(language.getLanguageId(), companyId);
                LanguagePiChartData languagePiChartData = new LanguagePiChartData();
                languagePiChartData.setLanguage(language.getLanguageLabel());
                if (termInformationList != null) {
                    languagePiChartData.setNoOfTerms(termInformationList.size());
                } else {
                    languagePiChartData.setNoOfTerms(0);
                }

                languagePiChartDataList.add(languagePiChartData);
            }
            if (topRegLangIds != null && topRegLangIds.size() > 0) {
                Integer count = termsService.getTermInformationForOtherLanguage(topRegLangIds, companyId);
                LanguagePiChartData languagePiChartData = new LanguagePiChartData();
                languagePiChartData.setLanguage("Other");
                languagePiChartData.setNoOfTerms(count);
                languagePiChartDataList.add(languagePiChartData);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to get language chartData list");
        }
        if (languagePiChartDataList != null && !languagePiChartDataList.isEmpty()) {
            Collections.sort(languagePiChartDataList, new Comparator<LanguagePiChartData>()
            {
                @Override
                public int compare(LanguagePiChartData o1, LanguagePiChartData o2) {
                    return o1.getLanguage().compareTo(o2.getLanguage());
                }
            });
            session.setAttribute(SessionKey.DISTRIBUTION.getKey(), languagePiChartDataList);
        }
        return languagePiChartDataList;
    }

    /**
     * To upload profile image
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value = "/uploadProfileImage")
    public
    @ResponseBody
    void uploadProfileImage(
            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {
            File uploadedFile = null;
            String ext = "";
            String hiddenVal = request.getParameter("t");
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                FileUploadListener listener = new FileUploadListener();
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = new ArrayList<FileItem>();
                String UPLOAD_PATH = null;
                session.setAttribute("LISTENER", listener);
                upload.setProgressListener(listener);
                items = upload.parseRequest(request);
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        if (fileName.contains(" ")) {
                            fileName = fileName.replace(' ', '_');
                        }
                        if (fileName != null && fileName.lastIndexOf('\\') > 0) {
                            fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
                        }
                        if (fileName != null && fileName.lastIndexOf('.') > 0) {
                            ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                        }
                        if (hiddenVal != null && hiddenVal.equalsIgnoreCase("term")) {
                            UPLOAD_PATH = TeaminologyProperty.TERM_IMAGE_PATH.getValue();
                        } else {
                        	//Changing path here for facecrop removal regarding TNG-84
                            UPLOAD_PATH = TeaminologyProperty.CROPPED_IMAGE_PATH.getValue();
                        }
                        fileName = fileName + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "." + ext;
                        session.setAttribute(SessionKey.IMAGE_NAME.getKey(), fileName);
                        logger.info(" +++  Final upload fileName -- " + fileName);
                        new File(UPLOAD_PATH).mkdirs();
                        uploadedFile = new File(UPLOAD_PATH, fileName);
                        item.write(uploadedFile);
                        logger.info(" +++  Finished upload contact uploadedFile -- " + uploadedFile.getAbsolutePath());
                    }
                } 
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("failed to crop image");
        }

    }

    /**
     * To uploadProfile
     *
     * @param request HttpServletRequest
     * @return UserStatus holding the details of the uploaded file
     */
    @RequestMapping(method = RequestMethod.GET, value = "/uploadProfile")
    public
    @ResponseBody
    UserStatus uploadProfile(HttpServletRequest request) {
        UserStatus userStatus = new UserStatus();
        try {
            HttpSession session = request.getSession();
            FileUploadListener listener = null;
            long bytesRead = 0, contentLength = 0;
            if (session == null) {
                return null;
            } else if (session != null) {
                listener = (FileUploadListener) session.getAttribute("LISTENER");

                if (listener == null) {
                    return null;
                } else {
                    bytesRead = listener.getBytesRead();
                    contentLength = listener.getContentLength();
                }
                if (session.getAttribute(SessionKey.IMAGE_NAME.getKey()) != null) {
                    userStatus.setFileName(session.getAttribute(SessionKey.IMAGE_NAME.getKey()).toString());
                }
            }
            userStatus.setBytesRead(bytesRead);
            userStatus.setContentLength(contentLength);
            if (bytesRead == contentLength) {
                userStatus.setIsFinished(true);
                session.setAttribute("LISTENER", null);
            } else {
                long percentComplete = ((100 * bytesRead) / contentLength);
                userStatus.setPercent(percentComplete);
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        return userStatus;
    }

    /**
     * To update the user
     *
     * @param request  HttpServletRequest
     * @param fileName String holding the file path
     * @param ext      String extension of the image uploaded
     * @return UserStatus holding the details of the uploaded image
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getCropedImage/{fileName}/{ext}")
    public
    @ResponseBody
    UserStatus getCropedImage(@PathVariable String fileName, @PathVariable String ext, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = Utils.isNull(session.getAttribute(SessionKey.USER.getKey())) ? null :
                (User) session.getAttribute(SessionKey.USER.getKey());
        //String filePath = TeaminologyProperty.UPLOAD_ROOT_DIR.getValue() + "/" + fileName;
        UserStatus userstatus = new UserStatus();
        try {
            String cropedImgPath = null;
            cropedImgPath = request.getContextPath() + "/images/person/" + fileName;
                if (user != null) {
                    user = userService.getUser(user.getUserId());
                    user.setPhotoPath(cropedImgPath);
                    user.setUpdateDate(new Date());
                    user.setUpdatedBy(user.getUserId());
                    userService.updateUser(user);
                }
                userstatus.setFileName(fileName);
                userstatus.setFilePath(cropedImgPath);
                return userstatus;
                
                //commenting code for removing FaceCrop regarding TNG-84.
                
           /*Runtime.getRuntime().load(TeaminologyProperty.FACECROP_LIB_PATH.getValue() + "/" + TeaminologyProperty.LIB_FILE.getValue());
          	if (FSDK.ActivateLibrary(licenseKey) == FSDK.FSDKE_OK) {
                if (FSDK.Initialize() == FSDK.FSDKE_OK) {
                    HImage hInputImage = new FSDK.HImage();
                    if (FSDK.LoadImageFromFileW(hInputImage, filePath) == FSDK.FSDKE_OK) {
                        TFacePosition.ByReference inputFacePositionRef = new TFacePosition.ByReference();
                        FSDK_Features.ByReference inputFaceFeaturesRef = new FSDK_Features.ByReference();
                       
                         /* if (FSDK.DetectFacialFeatures(hInputImage, inputFaceFeaturesRef) == FSDK.FSDKE_OK) {
                                HImage hOutImage = new FSDK.HImage();
                                FSDK_Features.ByReference outFaceFeaturesRef = new FSDK_Features.ByReference();
                                if (FSDK.ExtractFaceImage(hInputImage, inputFaceFeaturesRef, 200, 400, hOutImage, outFaceFeaturesRef) == FSDK.FSDKE_OK) {
                                    cropedImgPath = TeaminologyProperty.CROPPED_IMAGE_PATH.getValue() + fileName;
                                    if (FSDK.SaveImageToFile(hOutImage, cropedImgPath) == FSDK.FSDKE_OK) {
                                        if (fileName == null) {
                                            cropedImgPath = null;
                                        } else {

                                            cropedImgPath = request.getContextPath() + "/images/person/" + fileName;
                                        }
                                        if (user != null) {
                                            user = userService.getUser(user.getUserId());
                                            user.setPhotoPath(cropedImgPath);
                                            user.setUpdateDate(new Date());
                                            user.setUpdatedBy(user.getUserId());
                                            userService.updateUser(user);
                                        }
                                        userstatus.setFileName(fileName);
                                        userstatus.setFilePath(cropedImgPath);
                                        return userstatus;

                                    } else {
                                        userstatus.setErrorMsg("Failed to save image file");
                                        logger.error("Failed to save image file ");

                                    }
                                } 
                                    HImage hOutImage = new FSDK.HImage();
                                    FSDK_Features.ByReference outFaceFeaturesRef = new FSDK_Features.ByReference();
                                        cropedImgPath = TeaminologyProperty.CROPPED_IMAGE_PATH.getValue() + fileName;
                                        
                                            if (fileName == null) {
                                                cropedImgPath = null;
                                            } else {

                                                cropedImgPath = request.getContextPath() + "/images/person/" + fileName;
                                            }
                                            if (user != null) {
                                                user = userService.getUser(user.getUserId());
                                                user.setPhotoPath(cropedImgPath);
                                                user.setUpdateDate(new Date());
                                                user.setUpdatedBy(user.getUserId());
                                                userService.updateUser(user);
                                            }
                                            userstatus.setFileName(fileName);
                                            userstatus.setFilePath(cropedImgPath);
                                            return userstatus;

                    } else {
                        userstatus.setErrorMsg("Failed to load image");
                        logger.error("Failed to load image");

                    }
                } else {
                    userstatus.setErrorMsg("Failed to Initialize FSDK");
                    logger.error("Failed to Initialize FSDK ");

                }

            } else {
                userstatus.setErrorMsg("Failed to  activate libraryKey");
                logger.error("Failed to  activate libraryKey ");

            */ 

        } 
        catch (Exception e) {
            userstatus.setErrorMsg("Failed to Detect Face");
            return userstatus;
        }
    }

    /**
     * To get Language report table data
     *
     * @param request HttpServletRequest
     * @return List of report data w.r.t the top 6 registered languages
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/getLanguageReportTable")
    public
    @ResponseBody
    List<LanguageReportTable> getLanguageReportTable(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        if (user != null) {
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
        }
        List<LanguageReportTable> languageReportDataList = null;
        try {
            List<Language> topRegLangs = (List<Language>) termsService.getTopRegLangs(companyId);


            languageReportDataList = new ArrayList<LanguageReportTable>();
            float totalAccuracyRate = 0;
            int listSize = 0;
            int tempCount = 0;
            List<Status> statusList = null;
            if (session.getAttribute(SessionKey.STATUS_LOOKUP.getKey()) == null) {
                statusList = TeaminologyService.getStatusLookUp();
                session.setAttribute(SessionKey.STATUS_LOOKUP.getKey(), statusList);
            } else {
                statusList = (List<Status>) session.getAttribute(SessionKey.STATUS_LOOKUP.getKey());
            }
            Integer approvedStatus = 0;
            for (Status status : statusList) {
                if (status.getStatus().equalsIgnoreCase("Approved")) {
                    approvedStatus = status.getStatusId();
                }
            }

            for (Language language : topRegLangs) {
                totalAccuracyRate = 0;
                listSize = 0;
                LanguageReportTable langReportTableData = new LanguageReportTable();
                List<Integer> termInformationList = termsService.getTermInformationByLanguage(language.getLanguageId(), companyId);
                List<Member> teamMembersList = userService.getTeamMemberDetails(language.getLanguageId().toString(), null, null, null, 0, 0, companyId);
                Integer totalVotesPerLanguage = termsService.getTotalVotesPerLang(language.getLanguageId(), companyId);
                Integer monthlyVotesPerLanguage = termsService.getMonthlyVotesPerLang(language.getLanguageId().toString(), companyId);
                for (Member teamMember : teamMembersList) {

                    float accuracy = (teamMember.getAccuracy() == null) ? 0 : teamMember.getAccuracy().floatValue();
                    totalAccuracyRate += accuracy;
                }
                List<Integer> finalTermList = termsService.getTermInformationByLanguageAndStatus(language.getLanguageId(), approvedStatus, companyId);

                List<TermVoteMaster> currentDebatedTermsList = termsService.getTermVoteMasterByTermInfo(finalTermList, "TableData");
                Integer activePolls = termsService.getActivePolls(currentDebatedTermsList);
                //set language label
                langReportTableData.setLanguageLabel(language.getLanguageLabel());

                //set total terms per language
                listSize = termInformationList == null ? 0 : termInformationList.size();
                langReportTableData.setTotalTerms(listSize);

                //set members registered per language
                listSize = teamMembersList == null ? 0 : teamMembersList.size();
                langReportTableData.setMembers(listSize);

                //set Accuracy average of all repondents
                float accuracyRate = listSize == 0 ? 0 : totalAccuracyRate / listSize;
                DecimalFormat df = new DecimalFormat("#.##");
                langReportTableData.setAccuracy(new BigDecimal(df.format(accuracyRate)));

                //set debated terms count
                listSize = currentDebatedTermsList == null ? 0 : currentDebatedTermsList.size();
                langReportTableData.setDebatedTerms(listSize);

                //set Monthly average votes
                tempCount = monthlyVotesPerLanguage == null ? 0 : monthlyVotesPerLanguage;
                langReportTableData.setMonthlyAvg(tempCount);

                //set total votes per language
                tempCount = totalVotesPerLanguage == null ? 0 : totalVotesPerLanguage;
                langReportTableData.setTotalVotes(tempCount);

                //set Active Polls
                tempCount = activePolls == null ? 0 : activePolls;
                langReportTableData.setActivePolls(tempCount);
                languageReportDataList.add(langReportTableData);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to get language report table data");
        }
        if (languageReportDataList != null && !languageReportDataList.isEmpty())  {
            Collections.sort(languageReportDataList, new Comparator<LanguageReportTable>()
            {
                @Override
                public int compare(LanguageReportTable o1, LanguageReportTable o2) {
                    return o1.getLanguageLabel().compareTo(o2.getLanguageLabel());
                }
            });
            session.setAttribute(SessionKey.OVERVIEW.getKey(), languageReportDataList);
        }
        return languageReportDataList;
    }

    /**
     * To get download status of the file
     *
     * @param request HttpServletRequest
     * @return String status of the download file
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getDownloadStatus")
    public
    @ResponseBody
    String getDownloadStatus(HttpServletRequest request) {
        Integer downloadStatus = -1;
        try {
            HttpSession session = request.getSession();
            downloadStatus = session.getAttribute(SessionKey.DOWNLOAD_STATUS.getKey()) == null ? -1 : (Integer) session.getAttribute(SessionKey.DOWNLOAD_STATUS.getKey());
            if (downloadStatus == 1)
                session.removeAttribute(SessionKey.DOWNLOAD_STATUS.getKey());
        }
        catch (Exception e) {
            logger.info(" +++  Error in getting download status");
        }

        return downloadStatus.toString();

    }

    /**
     * To get upload status
     *
     * @param request HttpServletRequest
     * @return CSVImportStatus holding the list of users added and rejected
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUploadStatus")
    public
    @ResponseBody
    CSVImportStatus getUploadStatus(HttpServletRequest request) {
        CSVImportStatus importStatus = null;
        try {
            HttpSession session = request.getSession();
            importStatus = (CSVImportStatus) session.getAttribute(SessionKey.UPLOAD_STATUS.getKey());
            if (importStatus != null)
                session.removeAttribute(SessionKey.UPLOAD_STATUS.getKey());
            logger.info(" +++  Got upload status");
        }
        catch (Exception e) {
            logger.info(" +++  Error in getting upload status");
        }

        return importStatus;

    }

    /**
     * To add new user
     *
     * @param request HttpServletRequest
     * @param user    Request Body of the user to be added
     * @return String holding the status of adding user
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addUser")
    public
    @ResponseBody
    String addUser(@RequestBody User user, HttpServletRequest request,
                   HttpServletResponse response) {

        final User immutableUser;
        String status = null;
        Company companyUser = null;

        logger.info("Called the controller /addUser / to add user  .....");
        
        if (!Utils.isNull(user) && !Utils.isEmpty(user.getUserName())) {

            try {
                Set<UserLanguages> userLanguagesSet = new HashSet<UserLanguages>();
                Set<UserRole> roleSet = new HashSet<UserRole>();

                HttpSession session = request.getSession();
                User loggedInUser =(User)session.getAttribute(SessionKey.USER.getKey());
                
                Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID.getKey())) ? null : (Integer) session.getAttribute(SessionKey.USER_ID.getKey());
                boolean userExistsFlag = userService.ifUserExists(user.getUserName());
                boolean emailExistsFlag = userService.ifEmailExists(user.getEmailId());
                if (!userExistsFlag && !emailExistsFlag) {
                    if (user.getUserLanguages() != null && user.getUserLanguages().size() > 0) {
                        for (UserLanguages userLanguage : user.getUserLanguages()) {
                            Language language = lookUpService.getLanguage(userLanguage.getLanguages().getLanguageId());
                            userLanguage.setLanguages(language);
                            userLanguagesSet.add(userLanguage);
                        }
                    }
                    if (user.getUserRole() != null && user.getUserRole().size() > 0) {
                        for (UserRole userRoles : user.getUserRole()) {
                            Role role = lookUpService.getUserRole(userRoles.getRole().getRoleId());
                            userRoles.setRole(role);
                            roleSet.add(userRoles);

                        }
                    }

                    if (user.getCompany() != null && user.getCompany().getCompanyId() != 0) {
                        companyUser = lookUpService.getCompanyById(user.getCompany().getCompanyId());
                        user.setUserLanguages(userLanguagesSet);
                        user.setUserRole(roleSet);
                        user.setCreatedBy(userId);
                        user.setCompany(companyUser);
                        user.setIsActive("Y");
                    } else {
                        User userObj = (User) session.getAttribute(SessionKey.USER.getKey());
                        if (userObj.getCompany().getCompanyId() != null) {
                            Integer companyId = userObj.getCompany().getCompanyId();
                            companyUser = lookUpService.getCompanyById(companyId);
                        }
                        user.setUserLanguages(userLanguagesSet);
                        user.setUserRole(roleSet);
                        user.setCreatedBy(userId);
                        if (companyUser != null) {
                            user.setCompany(companyUser);
                        }
                        user.setIsActive("Y");
                    }

                    String password = user.getPassword();
                    status = userService.registerUser(user);
                    if (user.getCompanyTransMgmt() != null && user.getCompanyTransMgmt().size() > 0) {
                        for (CompanyTransMgmt companyTransMgmt : user.getCompanyTransMgmt()) {
                            companyTransMgmt.setIsActive("Y");
                            companyTransMgmt.setIsExternal(companyTransMgmt.getIsExternal());
                            companyTransMgmt.setUserId(user.getUserId());
                            userService.SaveCompanyTransMgmt(companyTransMgmt);
                        }
                    } else {
                        User userObj = (User) session.getAttribute(SessionKey.USER.getKey());
                        CompanyTransMgmt companyTransMgmt = new CompanyTransMgmt();
                        companyTransMgmt.setIsActive("Y");
                        companyTransMgmt.setIsExternal("N");
                        companyTransMgmt.setUserId(user.getUserId());
                        companyTransMgmt.setCompanyId(userObj.getCompany().getCompanyId());
                        userService.SaveCompanyTransMgmt(companyTransMgmt);
                    }
                    
                    @SuppressWarnings("unchecked")
                    List<Pair<String, String>> loggerMessages = Arrays.asList(
                    		new Pair<String,String>(ParamKeyEnum.NEW_USER_ADDED_BY.toString(),loggedInUser.getUserName()),
                    		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
                    		new Pair<String,String>(ParamKeyEnum.ADDED_USER.toString(),user.getUserName()),
                    		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),loggedInUser.getCompany().getCompanyId().toString()),
                    		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request)),
                    		new Pair<String,String>(ParamKeyEnum.USER_EMAIL_ID.toString(),user.getEmailId()),
                    		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_DATE.toString(),new Date().toString())
                    		);
                    
                    logger.info(loggerMessages);

                    immutableUser = user;
                    immutableUser.setPassword(password);
                    new Thread()
                    {
                        public void run() {
                            mailService.addNewUser(immutableUser);
                        }
                    }.start();

                    
                    
                    logger.info(" +++  User successfully added");
                } else {

                    if (userExistsFlag) {
                        status = "User exists";
                    }
                    if (emailExistsFlag) {
                        if (status != "") {
                            status = status + ',' + " Email exists";
                        } else {
                            status = "Email exists";
                        }
                    }
                }
            }
            catch (Exception e) {
            	status = "failure";
                logger.error("Error in adding  the user ");
                logger.error(e,e);
            }
        }
        return status;
    }

    /**
     * To get all user type details
     *
     * @param request HttpServletRequest
     * @return List of User type obj's
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/getAllUserTypeDetails")
    public
    @ResponseBody
    List<Role> getAllUserTypeDetails(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            Set<UserRole> userRoleList = Utils.isNull(session.getAttribute(SessionKey.USER_ROLE.getKey())) ? null : (Set<UserRole>) session.getAttribute(SessionKey.USER_ROLE.getKey());
            Integer roleId[] = null;
            if (userRoleList != null && userRoleList.size() > 0) {
                roleId = new Integer[userRoleList.size()];
                int i = 0;
                for (UserRole usrRole : userRoleList) {
                    roleId[i] = usrRole.getRole().getRoleId();
                    i++;
                }
                List<Role> userTypeList = userService.getAllUserTypeDetails(roleId);
                if (userTypeList != null) {
                    logger.info(" +++  Got " + userTypeList.size()
                            + "all user role details ");
                    return userTypeList;
                } else {
                    logger.info(" +++   Got null user role details");
                    return null;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to get Quarterly Term details");
        }
        return null;
    }


    /**
     * Used for category Create/Update/Delete operations
     *
     * @param request  HttpServletRequest
     * @param category category object to me modified
     * @return String of the path to be redirected
     */
    @RequestMapping(method = RequestMethod.POST, value = "/setCategoryCUD")
    public
    @ResponseBody
    String setCategoryCUD(@RequestBody Category category, HttpServletRequest request,
                          HttpServletResponse response) {
        String returnStatus = "";
        if (category == null) {
            return null;
        }
        String status = "";
        String categoryLabel = category.getCategory();
        boolean categoryExistsFlag = false;
        HttpSession session = request.getSession();
        Integer user_id = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                .getKey())) ? null : (Integer) session
                .getAttribute(SessionKey.USER_ID.getKey());
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        
        logger.info("Called the controller /setCategoryCUD / to get category object to me modified.....");
        
        @SuppressWarnings("unchecked")
		List<Pair<String, String>> loggerMessages = Arrays.asList(
				new Pair<String,String>(ParamKeyEnum.CATEGORY_LABEL.toString(),categoryLabel),
/*				new Pair<String,String>(ParamKeyEnum.CATEGORY_CODE.toString(),category.getCategoryId().toString()),
*/        	new Pair<String,String>(ParamKeyEnum.CATEGORY_CODE.toString(), category.getCategoryId() != null ? category.getCategoryId().toString() : category.getCategoryId()+""),
				new Pair<String,String>(ParamKeyEnum.USER_NAME.toString(),user.getUserName()),
        		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
        		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
        		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request)),
        		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_DATE.toString(),new Date().toString())
        		);
        
        
        logger.info(" Category CUD  +++ "+loggerMessages);
        
        try {
            if (category.getTransactionType().equalsIgnoreCase("Edit Category")) {
                Category catgry = lookUpService.getCategory(category.getCategoryId());
                if (!catgry.getCategory().equalsIgnoreCase(category.getCategory())) {
                    categoryExistsFlag = lookUpService.isCategoryExists(category.getCategory());
                    
                    logger.info("Edit Category +++ ");
                }
            }
            if (category.getTransactionType().equalsIgnoreCase("Add Category")) {
                categoryExistsFlag = lookUpService.isCategoryExists(categoryLabel);
                logger.info(" Added  Category +++ ");
            }
            if (!categoryExistsFlag) {

                status = lookUpService.setCategoryCUD(category, user_id);
                if (status.equalsIgnoreCase("success")) {
                    if (category.getTransactionType().equalsIgnoreCase("Delete Category")) {
                        List<Integer> termIds = termsService.getTermInformationByCategory(category.getCategoryId());
                        if (termIds != null && termIds.size() != 0) {
                            Integer[] intTermIds = new Integer[termIds.size()];
                            for (int i = 0; i < termIds.size(); i++) {
                                intTermIds[i] = termIds.get(i);
                            }
                            
                            @SuppressWarnings("unchecked")
							List<Pair<String,String>> loggerMsg = Arrays.<Pair<String,String>>asList(
                            		new Pair<String,String>(ParamKeyEnum.DELETED_TERM_ID.toString(),Arrays.asList(intTermIds).toString()));
                            
                            logger.info("Deleted terms+++++"+loggerMsg);
                            
                            termsService.deleteTerms(intTermIds, user);
                        }
                    }
                    returnStatus = "success";

                }
            } else {
                if (categoryExistsFlag) {
                    returnStatus = "Category exists";
                } else {
                    returnStatus = "failed";
                }
            }

        }
        catch (Exception e) {
            logger.error("Failed to update categories");
            logger.error(e,e);
        }
        return returnStatus;
    }

    /**
     * To get voting status of voted terms
     *
     * @param termId  Path variable to get particular termId
     * @param request HttpServletRequest
     * @return DataTable holding voting status of term
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getvotingStatus/{termId}")
    public
    @ResponseBody
    DataTable getvotingStatus(@PathVariable Integer termId,
                              HttpServletRequest request) {
        DataTable dataTable = new DataTable();
        if (termId == null) {
            return null;
        }
        try {
            List<VotingStatus> votingStatusList = termsService.getvotingStatus(termId);
            if (votingStatusList != null) {
                logger.info(" +++  Got votingStatus for term Id" + termId);
                dataTable.setTermData(votingStatusList);
                return dataTable;
            } else {
                logger.info(" +++  NULL votingStatus");
            }

        }
        catch (Exception e) {
            logger.error("Failed to get votingStatus");
        }
        return null;
    }

    /**
     * To get history status of voted terms
     *
     * @param termId  Path variable to get particular termId
     * @param request HttpServletRequest
     * @return DataTable holding History data of term
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getHistoryDetails/{termId}")
    public
    @ResponseBody
    DataTable getHistoryDetails(@PathVariable Integer termId,
    		HttpServletRequest request) {
    	DataTable dataTable = new DataTable();
    	if (termId == null) {
    		return null;
    	}
    	try {
    		List<HistoryDetailsData> historyDataList = termsService.getHistoryDatails(termId);
    		if (historyDataList != null) {
    			logger.info(" +++  Got History data for term Id" + termId);
    			dataTable.setHistoryData(historyDataList);
    			return dataTable;
    		} else {
    			logger.info(" +++  NULL History data");
    		}

    	}
    	catch (Exception e) {
    		logger.error("Failed to get History Data");
    	}
    	return null;
    }
    
    /**
     * To check whether user exists or not
     *
     * @param userName userName
     * @param request  HttpServletRequest
     * @return String holding status
     */
    @RequestMapping(method = RequestMethod.GET, value = "/isUserExists/{userName}")
    public
    @ResponseBody
    String isUserExists(@PathVariable String userName,
                        HttpServletRequest request) {
        String status = "failure";
        if (userName == null) {
            return null;
        }
        try {
            boolean userExistsFlag = userService.ifUserExists(userName);
            if (userExistsFlag) {
                status = "success";
            }

        }
        catch (Exception e) {
            logger.error("Failed to verify user exists");
        }
        return status;
    }

    /**
     * To save term image
     *
     * @param termId    Path variable  to get particular attributes
     * @param photoPath
     * @param request   HttpServletRequest
     */
    @RequestMapping(method = RequestMethod.GET, value = "/saveTermImage/{termId}/{photoPath}")
    public
    @ResponseBody
    void saveTermImage(@PathVariable Integer termId, @PathVariable String photoPath, HttpServletRequest request) {
        if (termId == null) {
            return;
        }
        try {
            photoPath = request.getContextPath() + "/images/term_images/" + photoPath;
            termsService.saveTermImage(termId, photoPath);
            logger.info(" +++  Successfully Saved term image");

        }
        catch (Exception e) {
            logger.error("Failed to save the photopath");
        }
    }

    /**
     * To search term entries
     *
     * @param request       HttpServletRequest
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return GSSerachObject that needs to manage
     */
    @RequestMapping(method = RequestMethod.POST, value = "/searchTermBaseEntries")
    public
    @ResponseBody
    TMProfileTermsResult searchTermBaseEntries(
            @RequestBody QueryAppender queryAppender, HttpServletRequest request) {
        HttpSession session = request.getSession();
        TMProfileTermsResult tmProfileTermsResult = null;
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        queryAppender.setSearchIndexType(IndexTypeEnum.TB.name());
        Integer companyId = null;
        if (user != null) {
            if (user.getCompany() != null) {
                companyId = user.getCompany().getCompanyId();
            }
        }
        try {

            tmProfileTermsResult = termsService.getSearchManagePollTermsTermBase(queryAppender,
                    companyId, user);
            if (tmProfileTermsResult != null
                    ) {
                logger.info(" +++  Got " + tmProfileTermsResult.getTotalResults());
                return tmProfileTermsResult;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get TermBase Information");
        }

        return null;
    }

    /**
     * To search tm entries
     *
     * @param request       HttpServletRequest
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return GSSerachObject that needs to manage
     */
    @RequestMapping(method = RequestMethod.POST, value = "/searchTMEntries")
    public
    @ResponseBody
    GSSearchObject searchTMEntries(@RequestBody QueryAppender queryAppender,
                                   HttpServletRequest request) {
        GSSearchObject searchObject = new GSSearchObject();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        queryAppender.setSearchIndexType(IndexTypeEnum.TM.name());
        Integer companyId = null;
        if (user != null) {
            if (user.getCompany() != null) {
                companyId = user.getCompany().getCompanyId();
            }
        }
        try {

            List<GSJobObject> managePollTermsListTm = null;

            managePollTermsListTm = termsService
                    .getSearchManagePollTermsTM(queryAppender, companyId,
                            user);

            searchObject.setTmProfileList(managePollTermsListTm);
            searchObject.setTotalRecords(queryAppender.getTotalRecords());
            logger.debug("Got search TmProfile Information ");
        }
        catch (Exception e) {
            logger.error("Failed to search TmProfile Information");
        }
        return searchObject;

    }

    /**
     * To get all local pairs
     *
     * @param request HttpServletRequest
     * @return LocalePairInformation obj  that needs to manage
     */

    @RequestMapping(method = RequestMethod.POST, value = "/getAllLocalePairs")
    public
    @ResponseBody
    LocalePairInformation getAllLocalePairs(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Company company = null;
        if (user != null) {
            company = user.getCompany();
        }
        if (company == null) {
            company = lookUpService.getCompanyIdByLabel(TeaminologyProperty.INTERNAL_COMPANY.getValue());
        }
        LocalePairInformation localePairInformation = (LocalePairInformation) session.getAttribute(SessionKey.LOCALE_PAIR_INFORMARION.getKey());
        String accessToken = null;
        try {
            if (accessToken == null) {
                accessToken = gsService.getAccessToken(company);
                //session.setAttribute(SessionKey.ACCESS_TOKEN.getKey(), accessToken);
            }

            localePairInformation = gsService.getAllLocalePairs(accessToken);
            logger.info(" +++  Got locale pair Information");

        }
        catch (Exception e) {
            logger.error("Failed to get locale pair information");
        }
        return localePairInformation;
    }

    /**
     * To get all tm profiles information
     *
     * @param request HttpServletRequest
     * @return TMProfileInformation obj that needs to manage
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getAllTMProfiles")
    public
    @ResponseBody
    TMProfileInformation getAllTMProfiles(HttpServletRequest request) {
        HttpSession session = request.getSession();
        TMProfileInformation tMProfileInformation = (TMProfileInformation) session.getAttribute(SessionKey.TM_PROFILE.getKey());
        String accessToken = null;
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Company company = null;
        if (user != null) {
            company = user.getCompany();
        }
        if (company == null) {
            company = lookUpService.getCompanyIdByLabel(TeaminologyProperty.INTERNAL_COMPANY.getValue());
        }
        try {
            if (accessToken == null) {
                accessToken = gsService.getAccessToken(company);
                //session.setAttribute(SessionKey.ACCESS_TOKEN.getKey(), accessToken);
            }
            if (tMProfileInformation == null) {
                tMProfileInformation = gsService.getAllTMProfiles(accessToken);
                session.setAttribute(SessionKey.TM_PROFILE.getKey(), tMProfileInformation);

            }
            logger.info(" +++  Got all tm profile terms");

        }
        catch (Exception e) {
            logger.error("Failed to get all tm profile terms");
        }
        return tMProfileInformation;
    }

    /**
     * To get all term base information
     *
     * @param request HttpServletRequest
     * @return TermbaseInformation obj that needs to manage
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getAllTermbases")
    public
    @ResponseBody
    TermbaseInformation getAllTermbases(HttpServletRequest request) {
        HttpSession session = request.getSession();
        TermbaseInformation termbaseInformation = (TermbaseInformation) session.getAttribute(SessionKey.TERMBASE.getKey());
        String accessToken = null;
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        Company company = null;
        if (user != null) {
            company = user.getCompany();
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
            company = lookUpService.getCompanyById(companyId);
        }
        if (company == null) {
            company = lookUpService.getCompanyIdByLabel(TeaminologyProperty.INTERNAL_COMPANY.getValue());
        }
        try {
            if (accessToken == null) {
                accessToken = gsService.getAccessToken(company);
                //session.setAttribute(SessionKey.ACCESS_TOKEN.getKey(), accessToken);
            }
            if (termbaseInformation == null) {
                termbaseInformation = gsService.getAllTermbases(accessToken);
                session.setAttribute(SessionKey.TERMBASE.getKey(), termbaseInformation);

            }


        }
        catch (Exception e) {
            logger.error("Failed to get accessToken");
        }
        return termbaseInformation;
    }

    /**
     * To get import xliff data
     *
     * @param request HttpServletRequest
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST, value = "/getImportXliffData")
    public
    @ResponseBody
    void getImportXliffData(HttpServletRequest request) {
        HttpSession session = request.getSession();

        try {
            String uploadedfilesPath = TeaminologyProperty.UPLOAD_ROOT_DIR.getValue();
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            CSVImportStatus importStatus = null;
            File uploadedFile = null;
            String cmd = null;
            String companyIds = request.getParameter("selectedCompanyIds");
            if (isMultipart) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = new ArrayList<FileItem>();
                items = upload.parseRequest(request);
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        logger.info(" +++  Final upload fileName -- " + fileName);
                        if (fileName != null && fileName.lastIndexOf("\\") > 0) {
                            fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
                        }

                        String UPLOAD_PATH = uploadedfilesPath;
                        new File(UPLOAD_PATH).mkdirs();
                        uploadedFile = new File(UPLOAD_PATH, fileName);
                        item.write(uploadedFile);
                        logger.info(" +++  Finished upload contact uploadedFile -- " + uploadedFile.getAbsolutePath());
                        if (fileName != null) {
                            int dotPos = fileName.lastIndexOf(".");
                            String extension = fileName.substring(dotPos);
                            if (extension.equalsIgnoreCase(".xlf")) {
                                cmd = "importXLF";
                            }
                        }

                    }
                }
            }
            if (cmd != null && cmd.equalsIgnoreCase("importXLF")) {
                importStatus = gsService.getImportXliffData(uploadedFile, companyIds);
            } else {
                importStatus = new CSVImportStatus();
                importStatus.setTermInformationStatus("invalid");
                importStatus.setRejectedCount(0);
            }
            session.setAttribute(SessionKey.UPLOAD_STATUS.getKey(), importStatus);

        }
        catch (Exception e) {
            logger.error("Failed to import Xlf");
        }
    }

    /**
     * To get global sight term information
     *
     * @param request       HttpServletRequest
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of  GlobalsightTerms that needs to manage
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getGlobalSightTermInfo")
    public
    @ResponseBody
    TMProfileTermsResult getGlobalSightTermInfo(@RequestBody QueryAppender queryAppender, HttpServletRequest request) {
        TMProfileTermsResult tmProfileTermsResult = null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        if (user != null) {
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
        }
        try {
        	queryAppender.setSearchIndexType(IndexTypeEnum.GS.name());
            tmProfileTermsResult = termsService.getGlobalSightTermInfo(queryAppender, companyId);
            logger.info(" +++   Got GlobalSightTermInfo  details ");
        }
        catch (Exception e) {
            logger.error("Failed to get GlobalSightTermInfo  details");
        }
        return tmProfileTermsResult;
    }

    /**
     * To get imported file list
     *
     * @param request       HttpServletRequest
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of  GlobalsightTerms that needs to manage
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getimportedFileList")
    public
    @ResponseBody
    List<GlobalsightTerms> getimportedFileList(@RequestBody QueryAppender queryAppender, HttpServletRequest request) {
        List<GlobalsightTerms> fileInfoList = null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        if (user != null) {
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
        }
        try {
            fileInfoList = termsService.getFileInfoList(queryAppender, companyId);
            logger.info(" +++  Got imported file list  details");
        }
        catch (Exception e) {
            logger.error("Failed to get imported file list  details");
        }
        return fileInfoList;
    }

    /**
     * To get total terms in glossary
     *
     * @param request HttpServletRequest
     * @return Integer holding the total count of terms in glossary
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTotalTermsInTermBaseTM/{isTM}")
    public
    @ResponseBody
    Integer getTotalTermsInTermBaseTM(@PathVariable String isTM, HttpServletRequest request) {
        int totalTermsInGlossary = 0;
        try {

            totalTermsInGlossary = termsService.getTotalTermsInTermBaseTM(isTM);
            logger.info(" +++  Got " + totalTermsInGlossary
                    + " Total Glossary Terms");

        }
        catch (Exception e) {
            logger.error("Failed to get Total Terms Value in Glossary");
        }
        return totalTermsInGlossary;
    }

    /**
     * To get TM terms
     *
     * @param request       HttpServletRequest
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of debated terms that needs to manage
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getTMProfileTerms")
    public
    @ResponseBody
    TMProfileTermsResult getTMProfileTerms(@RequestBody QueryAppender queryAppender,
                                           HttpServletRequest request) {

        TMProfileTermsResult tmProfileTermsResult = null;
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            queryAppender.setSearchIndexType(IndexTypeEnum.TM.name());
            if (user != null) {
                if (user.getCompany() != null) {
                    companyId = user.getCompany().getCompanyId();
                }
            }

            tmProfileTermsResult = termsService.getTMProfileTerms(queryAppender, companyId, user);
            logger.info(" +++  Got  manage tm terms");

        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to manage tm terms");
        }
        return tmProfileTermsResult;

    }

    /**
     * To get total terms in glossary
     *
     * @param request HttpServletRequest
     * @return Integer holding the total count of terms in glossary
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTotalTermsInTM")
    public
    @ResponseBody
    Integer getTotalTermsInTM(HttpServletRequest request) {
        int totalTermsInGlossary = 0;
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            Set<Integer> companyIds = null;
            Set<CompanyTransMgmt> companyTransMgmtUsers = new HashSet<CompanyTransMgmt>();

            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
                companyTransMgmtUsers = userService.getCompanyTransMgmtUsers(user.getUserId());
                if (companyTransMgmtUsers != null && !companyTransMgmtUsers.isEmpty()) {
                    companyIds = new HashSet<Integer>();
                    for (CompanyTransMgmt companyTransMgmtObj : companyTransMgmtUsers) {
                        companyIds.add(companyTransMgmtObj.getCompanyId());
                    }
                }
            }

            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
            }
            totalTermsInGlossary = termsService.getTotalTermsInTM(companyIds);
            logger.info(" +++  Got " + totalTermsInGlossary
                    + " Total Terms In  Tms");

        }
        catch (Exception e) {
            logger.error("Failed to get Total Terms im Tms");
        }
        return totalTermsInGlossary;
    }

    /**
     * To get tm attributes of a tmProfileInfoId
     *
     * @param request         HttpServletRequest
     * @param tmProfileInfoId Path variable to get particular attributes
     * @return TermAttributes w.r.t the term id
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTmAttributes/{tmProfileInfoId}")
    public
    @ResponseBody
    TmProfileInfo getTmAttributes(@PathVariable Integer tmProfileInfoId,
                                  HttpServletRequest request) {
        if (tmProfileInfoId == null) {
            return null;
        }
        try {
            TmProfileInfo term_attributes = termsService.getTmAttributes(tmProfileInfoId);
            if (term_attributes != null) {
                logger.info(" +++  Got TM Attributes for term Id" + tmProfileInfoId);
                return term_attributes;
            } else {
                logger.info(" +++  NULL TM Attributes");
            }

        }
        catch (Exception e) {
            logger.error("Failed to get TM Attributes");
        }
        return null;
    }

    /**
     * To update term attributes
     *
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @return Integer holding the status of the update
     */
    @RequestMapping(method = RequestMethod.POST, value = "/updateTmDetails")
    public
    @ResponseBody
    Integer updateTmDetails(@RequestBody TmDetails tmDetails,
                            HttpServletRequest request, HttpServletResponse response) {

        Integer updateStatusCode = 1;
        Integer tmProfileInfoId = 0;
        ProductGroup tmProduct = null;
        Domain tmDomain = null;
        ContentType contentType = null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        
        
        logger.info("Called the controller /updateTmDetails/ to update a tm.....");
        
        @SuppressWarnings("unchecked")
		List<Pair<String, String>> loggerMessages = Arrays.asList(
				new Pair<String,String>(ParamKeyEnum.TM_ID.toString(),tmDetails.getTmProfileInfoId().toString()),
        		new Pair<String,String>(ParamKeyEnum.UPDATED_BY.toString(),user.getUserName()),
        		new Pair<String,String>(ParamKeyEnum.UPDATED_DATE.toString(),new Date().toString()),
        		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
        		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
        		new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
        		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
        		);
        
        logger.info("get all Gsterms ++++ "+loggerMessages);

        if (!Utils.isNull(tmDetails)) {

            try {
                Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID.getKey())) ? null : (Integer) session.getAttribute(SessionKey.USER_ID.getKey());
                Integer companyId = null;
                if (user != null) {
                    if (user.getCompany() != null)
                        companyId = user.getCompany().getCompanyId();
                }
                tmProfileInfoId = tmDetails.getTmProfileInfoId();
                TmProfileInfo tmProfileInfo = termsService.getTmAttributes(tmProfileInfoId);

                if (tmDetails.getDomainId() != null) {
                    tmDomain = lookUpService.getDomainById(tmDetails.getDomainId());

                }

                if (tmDetails.getProductId() != null) {
                    tmProduct = lookUpService.getProductGroupById(tmDetails.getProductId());

                }
                if (tmDetails.getContentTypeId() != null) {
                    contentType = lookUpService.getContentTypeById(tmDetails.getContentTypeId());
                }
                tmProfileInfo.setTarget(tmDetails.getTopSuggestion());
                tmProfileInfo.setDomain(tmDomain);
                tmProfileInfo.setProductGroup(tmProduct);
                tmProfileInfo.setContentType(contentType);
                tmProfileInfo.setUpdateDate(new Date());
                tmProfileInfo.setUpdatedBy(userId);
                termsService.updateTmDetails(tmProfileInfo, companyId);
                logger.info(" +++  Updated term attributes for termId :" + tmProfileInfoId);
                updateStatusCode = 1;

            }
            catch (Exception e) {
                updateStatusCode = -1;
                logger.error("Failed to update tm attributes for term :"
                        + tmProfileInfoId);
                return updateStatusCode;
            }
        } else {
            updateStatusCode = 0;
        }
        return updateStatusCode;
    }

    /**
     * To delete tms
     *
     * @param termIds  An Integer array that needs to be deleted
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteTms/{termIds}")
    public
    @ResponseBody
    void deleteTms(@PathVariable Integer[] termIds,
                   HttpServletRequest request) {
        try {
            if (termIds.length != 0) {
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute(SessionKey.USER.getKey());
                
                logger.info("Called the controller /deleteTms/ to delete  selected tms.....");
                
                @SuppressWarnings("unchecked")
    			List<Pair<String, String>> loggerMessages = Arrays.asList(
    					new Pair<String,String>(ParamKeyEnum.DELETED_TMS_ID.toString(),Arrays.asList(termIds).toString()),
                		new Pair<String,String>(ParamKeyEnum.DELETED_BY.toString(),user.getUserName()),
                		new Pair<String,String>(ParamKeyEnum.DELETED_DATE.toString(),new Date().toString()),
                		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
                		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
                		new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
                		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
                		);
                
                logger.info("get all Gsterms ++++ "+loggerMessages);
                
                if (user != null) {
                    termsService.deleteTms(termIds, user);
                }
                logger.info(" +++  Successfully deleted the  TM terms");
            }
        }
        catch (Exception e) {
            logger.error("Failed to delete the TM term");
        }

    }

    /**
     * To update  gs term details
     *
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @param termDetails RequestBody term attributes to be updated
     * @return Integer holding the status of the update
     */
    @RequestMapping(method = RequestMethod.POST, value = "/updateGSTermDetails")
    public
    @ResponseBody
    Integer updateGSTermDetails(@RequestBody TermDetails termDetails,
                                HttpServletRequest request, HttpServletResponse response) {

        Integer updateStatusCode = 1;
        Integer termId = 0;

        if (!Utils.isNull(termDetails)) {

            try {
                HttpSession session = request.getSession();
                Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID.getKey())) ? null : (Integer) session.getAttribute(SessionKey.USER_ID.getKey());
                termId = termDetails.getTermId();
                GlobalsightTermInfo gsTermInformation = termsService.getGSTermInfoUsingTermId(termId);
                if (termDetails.getTopSuggestion() != null || termDetails.getTopSuggestion() != "") {
                    gsTermInformation.setTargetSegment(termDetails.getTopSuggestion());
                }
                gsTermInformation.setUpdatedBy(userId);
                gsTermInformation.setTag(null);
                termsService.updateGSTermDetails(gsTermInformation);
                logger.info(" +++  Updated term attributes for termId :" + termId);
                updateStatusCode = 1;

            }
            catch (Exception e) {
                updateStatusCode = -1;
                logger.error("Failed to update term attributes for term :"
                        + termId);
                return updateStatusCode;
            }
        } else {
            updateStatusCode = 0;
        }
        return updateStatusCode;
    }

    /**
     * To get total  gs terms
     *
     * @param request HttpServletRequest
     * @return Integer holding the count of total gs terms
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTotalGSTerms")
    public
    @ResponseBody
    Integer getTotalGSTerms(HttpServletRequest request) {
        int totalTermsInGlossary = 0;
        try {

            totalTermsInGlossary = termsService.getTotalGSTerms();
            logger.info(" +++  Got " + totalTermsInGlossary
                    + " Total GS Terms");

        }
        catch (Exception e) {
            logger.error("Failed to get Total GS Terms");
        }
        return totalTermsInGlossary;
    }

    /**
     * To get content type look uop information
     *
     * @param request HttpServletRequest
     * @return List of  ContentType that needs to manage
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getContentTypeLookUp")
    public
    @ResponseBody
    List<ContentType> getContentType(HttpServletRequest request) {
        try {
            List<ContentType> content_list = lookUpService.getContentType();
            if (content_list != null) {
                logger.info(" +++  Got content type data");
                return content_list;
            } else {
                logger.info(" +++  NULL content type data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get content type");
        }
        return null;
    }

    /**
     * To delete gs terms
     *
     * @param termIds  An Integer array that needs to be deleted
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteGSTerms/{termIds}")
    public
    @ResponseBody
    void deleteGSTerms(@PathVariable Integer[] termIds,
                       HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            if (termIds.length != 0) {
                termsService.deleteGSTerms(termIds, companyId);
                logger.info(" +++  Successfully deleted the terms");
            }
        }
        catch (Exception e) {
            logger.error("Failed to delete the term");
        }

    }

    /**
     * To delete gs file data
     *
     * @param fileIds  An Integer array that needs to be deleted
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteGSFileData/{fileIds}")
    public
    @ResponseBody
    void deleteGSFileData(@PathVariable Integer[] fileIds,
                          HttpServletRequest request) {
        try {
        	
        	HttpSession session = request.getSession();
        	User user = (User)session.getAttribute(SessionKey.USER.getKey());
        	
            if (fileIds.length != 0) {
                termsService.deleteGSFileData(fileIds,user);
                logger.info(" +++  Successfully deleted the  gs file data");
            }
        }
        catch (Exception e) {
            logger.error("Failed to delete the  gs file data");
        }

    }

    /**
     * To get total terms in glossary
     *
     * @param request HttpServletRequest
     * @return Integer holding the total count of terms in glossary
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getTotalTMTermsBySearch")
    public
    @ResponseBody
    Integer getTotalTMTermsBySearch(@RequestBody QueryAppender queryAppender, HttpServletRequest request) {
        int totalTermsInTMGlossary = 0;
        long startTimeInMS = System.currentTimeMillis();
        try {

            totalTermsInTMGlossary = termsService.getTotalTMTermsBySearch(queryAppender);
            logger.info(" +++  Got " + totalTermsInTMGlossary
                    + " Total TM Glossary Terms By search");

        }
        catch (Exception e) {
            logger.error("Failed to get Total Terms Value in Glossary");
        }
        finally {

            long endTimeInMS = System.currentTimeMillis();
            long totalTimeInMs = endTimeInMS - startTimeInMS;
            double totalTimeInSeconds = totalTimeInMs / 1000D;
            double totalTimeInMinutes = totalTimeInSeconds / 60D;
            String timeTook;
            if (totalTimeInMinutes >= 1D) {
                timeTook = totalTimeInMinutes + " minutes" + " or "
                        + totalTimeInSeconds + " seconds";
            } else {
                timeTook = totalTimeInSeconds + " seconds";
            }
            logger.info(" +++  Took total for TMProfile search terms" + timeTook);
            logger.info(" +++  TM search startTimeInMinutes:" + startTimeInMS / 1000
                    + "TM search ending time in Minutes:" + endTimeInMS / 1000);

        }
        return totalTermsInTMGlossary;
    }

    /**
     * To get all user type menu details
     *
     * @param request HttpServletRequest
     * @return List of  menu  obj's
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/getUserTypeMenus")
    public
    @ResponseBody
    List<Menu> getRoleMenuDetails(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();

            User user = Utils.isNull(session.getAttribute(SessionKey.USER
                    .getKey())) ? null : (User) session
                    .getAttribute(SessionKey.USER.getKey());
            if (user != null) {

                Set<UserRole> userRoleList = Utils.isNull(session.getAttribute(SessionKey.USER_ROLE
                        .getKey())) ? null : (Set<UserRole>) session
                        .getAttribute(SessionKey.USER_ROLE.getKey());
                Integer roleId[] = null;
                if (userRoleList != null && userRoleList.size() > 0) {
                    roleId = new Integer[userRoleList.size()];
                    int i = 0;
                    for (UserRole usrRole : userRoleList) {
                        roleId[i] = usrRole.getRole().getRoleId();
                        i++;
                    }
                    List<Menu> roleMenuList = userService.getRoleMenuDetails(roleId);

                    if (roleMenuList != null) {
                        logger.info(" +++  Got " + roleMenuList.size()
                                + " user type menu details");

                        return roleMenuList;
                    }
                }

            }

        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to get user  type menu details");
        }
        return null;

    }

    /**
     * To get all user type sub menu details
     *
     * @param request HttpServletRequest
     * @return List of  SubMenu  obj's
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/getUserTypeSubMenus/{menuId}")
    public
    @ResponseBody
    List<SubMenu> getUserTypeSubMenuDetails(@PathVariable Integer menuId, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();

            User user = Utils.isNull(session.getAttribute(SessionKey.USER
                    .getKey())) ? null : (User) session
                    .getAttribute(SessionKey.USER.getKey());
            List<SubMenu> userTypeSubMenuList = null;
            Integer roleId[] = null;

            if (user != null) {
                Set<UserRole> userRoleList = Utils.isNull(session.getAttribute(SessionKey.USER_ROLE
                        .getKey())) ? null : (Set<UserRole>) session
                        .getAttribute(SessionKey.USER_ROLE.getKey());
                if (userRoleList != null && userRoleList.size() > 0) {
                    roleId = new Integer[userRoleList.size()];
                    int i = 0;
                    for (UserRole usrRole : userRoleList) {
                        roleId[i] = usrRole.getRole().getRoleId();
                        i++;
                    }
                    userTypeSubMenuList = userService.getRoleSubMenuDetails(roleId, menuId);
                }
            }


            if (userTypeSubMenuList != null) {
                logger.info(" +++  Got " + userTypeSubMenuList.size()
                        + " user type sub menu details");

                return userTypeSubMenuList;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get user  type sub menu details");
        }
        return null;

    }

    /**
     * To get Parts of speech lookup data
     *
     * @param request HttpServletRequest
     * @return List of Parts of speech obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getCompanyLookUp")
    public
    @ResponseBody
    List<Company> getCompanyLookUp(HttpServletRequest request) {
        try {
            List<Company> company_list = lookUpService.getCompanyLookUp();
            if (company_list != null) {
                logger.info(" +++  Got Company data");
                return company_list;
            } else {
                logger.info(" +++  NULL Company data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Company");
        }
        return null;
    }


    /**
     * To get company details
     *
     * @param request   HttpServletRequest
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of companies
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getCompanyDetails/{colName}/{sortOrder}/{pageNum}")
    public
    @ResponseBody
    List<Company> getCompanyDetails(@PathVariable String colName, @PathVariable String sortOrder,
                                    @PathVariable Integer pageNum, HttpServletRequest request) {
        try {

            List<Company> company_list = lookUpService.getCompanyDetails(colName, sortOrder, pageNum);
            if (company_list != null) {
                logger.info(" +++  Got Company data");
                return company_list;
            } else {
                logger.info(" +++  NULL Company data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Company");
        }
        return null;
    }


    /**
     * Add new company.
     *
     * @param company RequestBody Company that needs to be added
     * @return If company  is added it returns success else failed
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addCompany")
    public
    @ResponseBody
    String addNewCompany(@RequestBody Company company,
                         HttpServletRequest request) {
        String status = null;
        boolean companyExistsFlag = false;
        HttpSession session = request.getSession();
        
        User  user = (User)session.getAttribute(SessionKey.USER.getKey());
    
        logger.info("Called the controller /addCompany/ to add new company.....");
 
        @SuppressWarnings("unchecked")
		List<Pair<String, String>> loggerMessages = Arrays.asList(
        				new Pair<String,String>(ParamKeyEnum.CREATED_COMPANY_NAME.toString(),company.getCompanyName()),
                   		new Pair<String,String>(ParamKeyEnum.CREATED_COMPANY_EMAIL_ID.toString(),company.getEmailId()),
                   		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
                   		new Pair<String,String>(ParamKeyEnum.USER_NAME.toString(),user.getUserName()),
                   		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request)),
                   		new Pair<String,String>(ParamKeyEnum.CREATED_DATE.toString(),new Date().toString())
                   		);
           
        logger.info("Created Company +++ "+loggerMessages);
        
        Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                .getKey())) ? null : (Integer) session
                .getAttribute(SessionKey.USER_ID.getKey());
        String companyName = company.getCompanyName().trim();
        try {
            companyExistsFlag = lookUpService.isCompanyExists(companyName);
            if (!Utils.isNull(company) && !companyExistsFlag) {
                String contextRoot = null;
                company.setCreatedBy(userId);
                company.setIsActive("Y");
                company.setCreateDate(new Date());
                if (company.getCompanyName() != null) {
                    contextRoot = company.getCompanyName().toLowerCase();
                    for (CompanySuffixEnum companySuffixEnum : CompanySuffixEnum.values()) {
                        if (contextRoot.contains(companySuffixEnum.getValue())) {
                            if (!CompanySuffixEnum.SPACE.getValue().equalsIgnoreCase(companySuffixEnum.getValue()))
                                contextRoot = contextRoot.replace(companySuffixEnum.getValue(), "");
                            contextRoot = contextRoot.trim();
                        }
                    }

                    if (contextRoot.contains(CompanySuffixEnum.SPACE.getValue())) {
                        contextRoot = contextRoot.replaceAll(CompanySuffixEnum.SPACE.getValue(), "_");
                    }
                }
                company.setContextRoot(contextRoot);
                status = lookUpService.addNewCompany(company);
                logger.info(" +++  Successfully added new company");
                userService.createCompanyUsers(company, userId);
                lookUpService.createCompanyContext(company, userId);
                status = "success";
            } else {
                if (companyExistsFlag) {
                    status = "company exists";
                }

            }

        }
        catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            logger.error("Failed to add new company");
            return status = "failed";
        }

        return status;
    }

    /**
     * To get total active companies
     *
     * @param request HttpServletRequest
     * @return Integer holding the count of total active companies
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getActiveCompaniesCount")
    public
    @ResponseBody
    int getActiveCompaniesCount(HttpServletRequest request) {
        try {
            return lookUpService.getActiveCompaniesCount();
        }
        catch (Exception e) {
            logger.error("Failed to get Active users count");
        }
        return 0;
    }


    /**
     * To update company.
     *
     * @param company RequestBody Company that needs to be updated
     * @return If company  is updated it returns success else failed
     */
    @RequestMapping(method = RequestMethod.POST, value = "/updateCompany")
    public
    @ResponseBody
    String updateCompany(@RequestBody Company company,
                         HttpServletRequest request) {
        String status = "";
        HttpSession session = request.getSession();
        
        User  user = (User)session.getAttribute(SessionKey.USER.getKey());
        
        logger.info("Called the controller /updateCompany/ to update company.....");
 
        @SuppressWarnings("unchecked")
		List<Pair<String, String>> loggerMessages = Arrays.asList(
        				new Pair<String,String>(ParamKeyEnum.UPDATED_COMPANY_ID.toString(),company.getCompanyId().toString()),
                   		new Pair<String,String>(ParamKeyEnum.UPDATED_COMPANY_NAME.toString(),company.getCompanyName()),
                   		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
                   		new Pair<String,String>(ParamKeyEnum.USER_NAME.toString(),user.getUserName()),
                   		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request)),
                   		new Pair<String,String>(ParamKeyEnum.UPDATED_DATE.toString(),new Date().toString())
                   		);
           
        logger.info("Updated Company +++ "+loggerMessages);
        
        Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                .getKey())) ? null : (Integer) session
                .getAttribute(SessionKey.USER_ID.getKey());
        Integer companyId = company.getCompanyId();
        Company updateCompany = null;
        boolean companyUpdateFlag = false;
        try {
            if (companyId != null) {
                updateCompany = lookUpService.getCompanyById(companyId);
                if (!company.getCompanyName().equalsIgnoreCase(updateCompany.getCompanyName())) {
                    companyUpdateFlag = lookUpService.isCompanyExists(company.getCompanyName());
                }

                if (!Utils.isNull(company) && !companyUpdateFlag) {
                    updateCompany.setUpdatedBy(userId);
                    updateCompany.setUpdateDate(new Date());
                    updateCompany.setCompanyName(company.getCompanyName());
                    updateCompany.setPoc(company.getPoc());
                    updateCompany.setEmailId(company.getEmailId());
                    status = lookUpService.updateCompany(updateCompany);
                    logger.info(" +++  Successfully updated company");
                    status = "success";
                } else {
                    if (companyUpdateFlag) {
                        status = "updateCompany exists";
                    }

                }
            }
        }
        catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            logger.error("Failed to update company");
            return status = "failed";
        }

        return status;
    }

    /**
     * To delete companies
     *
     * @param request    HttpServletRequest
     * @param companyIds array of integer companyIds that needs to be deleted
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteCompanies/{companyIds}")
    public
    @ResponseBody
    void deleteCompanies(@PathVariable Integer[] companyIds, HttpServletRequest request) {
        try {

        	HttpSession session = request.getSession();
        	User  user = (User)session.getAttribute(SessionKey.USER.getKey());
            Integer companyId;
            
            logger.info("Called the controller /deleteCompanies/ to delete company.....");
            

            @SuppressWarnings("unchecked")
			List<Pair<String, String>> loggerMessages = Arrays.asList(
            				new Pair<String,String>(ParamKeyEnum.DELETED_COMPANY_IDS.toString(),Arrays.asList(companyIds).toString()),
                       		new Pair<String,String>(ParamKeyEnum.DELETED_BY.toString(),user.getUserName()),
                       		new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
                       		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
                       		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
                       		new Pair<String,String>(ParamKeyEnum.USER_NAME.toString(),user.getUserName()),
                       		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request)),
                       		new Pair<String,String>(ParamKeyEnum.DELETED_DATE.toString(),new Date().toString())
                       		);
               
            logger.info("Deleted Company +++ "+loggerMessages);
            
            if (companyIds.length != 0) {
                lookUpService.deleteCompanies(companyIds, user.getUserId());
                for (int i = 0; i < companyIds.length; i++) {
                    companyId = companyIds[i];
                    List<Integer> userIds = userService.getUserByCompanyId(companyId);

                    if (userIds != null && userIds.size() != 0) {
                        Integer[] intUserIds = new Integer[userIds.size()];
                        for (int j = 0; j < userIds.size(); j++) {
                            intUserIds[j] = userIds.get(j);
                        }
                        
                        
                        logger.info("Called the  /deleteCompanies/ to delete company and their users....");

                    	@SuppressWarnings("unchecked")
                    	List<Pair<String, String>> loggerMessagesObj = Arrays.asList(
                    			new Pair<String,String>(ParamKeyEnum.DELETED_USER_IDS.toString(),Arrays.asList(intUserIds).toString())
                    			);

                    	logger.info("Deleted Users when deleting company:::::::"+loggerMessagesObj);
                        
                        userService.deleteUsers(intUserIds , user.getUserId());
                        Integer[] termIds = termsService.getTermIdsByCompanyId(companyId);
                        
                        @SuppressWarnings("unchecked")
						List<Pair<String,String>> loggerMsg = Arrays.<Pair<String,String>>asList(
                        		new Pair<String,String>(ParamKeyEnum.DELETED_TERM_ID.toString(),Arrays.asList(termIds).toString()));
                        
                        logger.info("Deleted terms+++++"+loggerMsg);
                        
                        Company compObj = user.getCompany();
                        compObj.setCompanyId(companyId);
                        user.setCompany(compObj);
                        
                        if (termIds != null)
                            termsService.deleteTerms(termIds,user);

                        logger.info(" +++  Successfully delete the companies");
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("Failed to delete the companies");
        }

    }

    /**
     * To get list of glossary tm terms count per year
     *
     * @param request HttpServletRequest
     * @return List of data holding count of terms per year
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getGlossaryTmTerms")
    public
    @ResponseBody
    List<Terms> getGlossaryTmTerms(HttpServletRequest request) {
        long startTimeInMS = System.currentTimeMillis();
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            Set<CompanyTransMgmt> companyTransMgmtUsers = null;
            Set<Integer> companyIds = new HashSet<Integer>();
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
                companyTransMgmtUsers = userService.getCompanyTransMgmtUsers(user.getUserId());
            }
            if (companyTransMgmtUsers != null && !companyTransMgmtUsers.isEmpty()) {
                for (CompanyTransMgmt companyTransMgmtObj : companyTransMgmtUsers) {
                    companyIds.add(companyTransMgmtObj.getCompanyId());
                }
            }
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (companyId == null && company != null) {
                companyId = company.getCompanyId();
                companyIds.add(companyId);
            }
            List<Terms> glossaryTermsList = termsService.getTmsInGlossary(companyIds);
            glossaryTermsList = termsService.getFinalisedTms(glossaryTermsList, "Yearly");
            session.setAttribute(SessionKey.GLOSSARY_TERMS.getKey(), glossaryTermsList);
            if (glossaryTermsList != null && !glossaryTermsList.isEmpty()
                    && glossaryTermsList.size() > 0) {
                logger.info(" +++  Got " + glossaryTermsList.size()
                        + " Glossary Terms");
                return glossaryTermsList;
            } else {
                logger.info(" +++  NULL Terms in Glossary");
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Terms in Glossary");
        }
        finally {
            long endTimeInMS = System.currentTimeMillis();
            long totalTimeInMs = endTimeInMS - startTimeInMS;
            double totalTimeInSeconds = totalTimeInMs / 1000D;
            double totalTimeInMinutes = totalTimeInSeconds / 60D;
            String timeTook;
            if (totalTimeInMinutes >= 1D) {
                timeTook = totalTimeInMinutes + " minutes" + " or "
                        + totalTimeInSeconds + " seconds";
            } else {
                timeTook = totalTimeInSeconds + " seconds";
            }
            logger.info(" +++  Took total for getGlossaryTmchartTerms" + timeTook);
            logger.info(" +++  startTimeInMS:" + startTimeInMS / 1000
                    + " ending time:" + endTimeInMS / 1000);
        }
        return null;
    }

    /**
     * To get total Tm terms in  per quarter
     *
     * @param request HttpServletRequest
     * @return List of data holding count of terms per quarter
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getQuarterlyTmDetails")
    public
    @ResponseBody
    List<Terms> getQuarterlyTmDetails(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }

            List<Terms> quarterlyTermsList = termsService
                    .getQuarterlyTmDetails(companyId);
            quarterlyTermsList = termsService.getFinalisedTms(quarterlyTermsList, "Quarterly");
            if (quarterlyTermsList != null) {
                logger.info(" +++  Got " + quarterlyTermsList.size()
                        + " Glossary Tms per Quarter");
                return quarterlyTermsList;
            } else {
                logger.info(" +++  NULL tms per Quarter");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Quarterly Tms details");
        }
        return null;
    }

    /**
     * To get total terms in TMs per month
     *
     * @param request HttpServletRequest
     * @return List of data holding count of  terms per month
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getMonthlyTmDetails")
    public
    @ResponseBody
    List<Terms> getMonthlyTmDetails(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            List<Terms> monthlyTermsList = termsService
                    .getMonthlyTmDetails(companyId);
            monthlyTermsList = termsService.getFinalisedTms(monthlyTermsList, "Monthly");

            if (monthlyTermsList != null) {
                logger.info(" +++  Got " + monthlyTermsList.size()
                        + " Glossary Tms per month");
                return monthlyTermsList;
            } else {
                logger.info(" +++  Null tm in glossary per month");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get Tm Monthly details");
        }
        return null;
    }


    /**
     * To save import tmx file
     *
     * @param request  HttpServletRequest
     * @param fileName - Name of the file to be save
     * @param type     String holding the type of file to be save
     * @return FileUploadStatus
     */

    @RequestMapping(method = RequestMethod.GET, value = "/saveImportTMXFile/{fileName}/{ext}/{type}")
    public
    @ResponseBody
    FileUploadStatus saveImportTMXFile(@PathVariable String fileName, @PathVariable String ext, @PathVariable String type,
                                       HttpServletRequest request, HttpServletResponse response) {
        String uploadedfilesPath = TeaminologyProperty.UPLOAD_ROOT_DIR.getValue();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        if (fileName == null) {
            return null;
        }
        try {
            FileUploadStatus fileData = new FileUploadStatus();
            fileData.setFileName(fileName);
            fileData.setStartTime(new Date());
            fileData.setCreatedBy(user.getUserId());
            if (type.equalsIgnoreCase(TeaminologyEnum.TEAMINOLOGY_IMPORT.getValue())) {
                fileData.setFileType(TeaminologyEnum.TEAMINOLOGY_IMPORT_TM.getValue());
                fileData.setFileStatus(TeaminologyEnum.TEAMINOLOGY_FILE_IMPORT_UPLOAD.getValue());
            }
            if (type.equalsIgnoreCase(TeaminologyEnum.TEAMINOLOGY_EXPORT.getValue())) {
                fileData.setFileType(TeaminologyEnum.TEAMINOLOGY_EXPORT_TM.getValue());
                fileData.setFileStatus(TeaminologyEnum.TEAMINOLOGY_FILE_DOWNLOAD.getValue());
                uploadedfilesPath = TeaminologyProperty.UPLOAD_ROOT_DIR.getValue();
                File userIdDir = new File(uploadedfilesPath);
                if (!userIdDir.exists()) {
                    userIdDir.mkdir();
                }
                fileName = "teaminology" + new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date()) + "." + ext;
                fileData.setFileUrl(uploadedfilesPath + "/" + fileName);

            }
            termsService.saveImportTMXFile(fileData);
            CSVImportStatus failedStatus = new CSVImportStatus();
            failedStatus.setInsertedCount(0);
            session.setAttribute(SessionKey.UPLOAD_STATUS.getKey(), failedStatus);
            logger.info(" +++  Got file upload status");
            return fileData;

        }
        catch (Exception e) {
            logger.error("Failed to fileData");
        }
        return null;
    }

    /**
     * To get previlegeList
     *
     * @param request HttpServletRequest
     * @return List of PrivilegesByMenu obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getPrevilegeList")
    public
    @ResponseBody
    List<PrivilegesByMenu> getPrevilegeList(HttpServletRequest request) {
        try {
            List<PrivilegesByMenu> privilegesList = userService.getPrevilegeList();

            if (privilegesList != null) {
                logger.info(" +++  Got " + privilegesList.size()
                        + " previliges list");
                return privilegesList;
            } else {
                logger.info(" +++  Null previliges list ");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get previliges list");
        }
        return null;
    }

    /**
     * To update Previleges
     *
     * @param request    HttpServletRequest
     * @param roleTypeId Integer to identify user role
     */
    @RequestMapping(method = RequestMethod.GET, value = "/updatePrevileges/{fileIdArray}/{roleTypeId}")
    public
    @ResponseBody
    void updatePrevileges(@PathVariable Integer[] fileIdArray, @PathVariable Integer roleTypeId,
                          HttpServletRequest request) {
        try {
            if (fileIdArray != null && fileIdArray.length != 0 && roleTypeId != null) {
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute(SessionKey.USER.getKey());
                userService.updatePrevileges(fileIdArray, roleTypeId, user.getUserId());
                logger.info(" +++  Successfully update the previliges");
            }
        }
        catch (Exception e) {
            logger.error("Failed to update the previliges");
        }

    }

    /**
     * To get  previleges by role
     *
     * @param request HttpServletRequest
     * @param roleId  Integer to identify user role
     * @return List of rolePrivileges
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getPrevilegesByRole/{roleId}")
    public
    @ResponseBody
    List<RolePrivileges> getPrevilegesByRole(@PathVariable Integer roleId, HttpServletRequest request) {
        try {
            List<RolePrivileges> privilegesList = userService.getPrevilegesByRole(roleId);

            if (privilegesList != null) {
                logger.info(" +++  Got " + privilegesList.size()
                        + " previleges");
                return privilegesList;
            } else {
                logger.info(" +++  empty privelges for roleId " + roleId);
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to getPrevilegesByRole");
        }
        return null;
    }

    /**
     * To get all user role previleges
     *
     * @param request HttpServletRequest
     * @return List of rolePrivileges
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/getUserRolePrevileges")
    public
    @ResponseBody
    List<RolePrivileges> getUserRolePrevileges(HttpServletRequest request) {
        List<RolePrivileges> privilegesList = new ArrayList<RolePrivileges>();
        try {
            HttpSession session = request.getSession();

            User user = Utils.isNull(session.getAttribute(SessionKey.USER
                    .getKey())) ? null : (User) session
                    .getAttribute(SessionKey.USER.getKey());

            if (user != null) {
                Set<UserRole> userRoleList = Utils.isNull(session.getAttribute(SessionKey.USER_ROLE
                        .getKey())) ? null : (Set<UserRole>) session
                        .getAttribute(SessionKey.USER_ROLE.getKey());
                if (userRoleList != null && userRoleList.size() > 0) {
                    for (UserRole usrRole : userRoleList) {
                        List<RolePrivileges> privileges = userService.getPrevilegesByRole(usrRole.getRole().getRoleId());
                        if (privileges != null && privileges.size() > 0) {
                            privilegesList.addAll(privileges);
                        }
                    }
                }
            }
   
            //check the suggested term language and logged in user language
            //for company term manager only
            int suggestedTermLangId = 0;
            if(session.getAttribute(SessionKey.SELECTED_SUGGESTED_TERM_LANG_ID.getKey()) != null) {
            	suggestedTermLangId = (Integer) session.getAttribute(SessionKey.SELECTED_SUGGESTED_TERM_LANG_ID.getKey());
            	session.removeAttribute(SessionKey.SELECTED_SUGGESTED_TERM_LANG_ID.getKey());
            }
            Boolean isCompanyTeamManager = (Boolean) session.getAttribute(SessionKey.COMPANY_TEAM_MANAGER.getKey());
            if(isCompanyTeamManager != null && isCompanyTeamManager && suggestedTermLangId != 0) {
           	   	Set<UserLanguages> userLangSet = user.getUserLanguages();
           	   	if(userLangSet != null) {
           	   		boolean userHasLang = false;
           	   		for(UserLanguages userLanguage : userLangSet) {
           	   			if(suggestedTermLangId == userLanguage.getLanguages().getLanguageId().intValue()) {
           	   				userHasLang = true;
           	   				break;
           	   			}
           	   		}
           	   		if(!userHasLang) {
           	   			for(RolePrivileges rolePrev : privilegesList) {
           	   				if(rolePrev.getPrivileges() != null
           	   						&& rolePrev.getPrivileges().getPrivilegeDesc() != null
           	   						&& rolePrev.getPrivileges().getPrivilegeDesc().trim().equalsIgnoreCase(PrivilegeEnum.PICK_FINAL_TERM.getValue())) {
           	   					privilegesList.remove(rolePrev);
           	   					break;
           	   				}
           	   			}
           	   		}
           	   	}
            }
            
            if (privilegesList != null) {
                logger.info(" +++  Got " + privilegesList.size()
                        + " Glossary Tms per month");
                return privilegesList;
            } 
        }
        catch (Exception e) {
            logger.error("Failed to get Tm Monthly details");
        }
        return null;
    }

    /**
     * Used for Role Create/Update/Delete operations
     *
     * @param request HttpServletRequest
     * @param role    role obj to me modified
     * @return String of the path to be redirected
     */
    @RequestMapping(method = RequestMethod.POST, value = "/setRoleCUD")
    public
    @ResponseBody
    String setRoleCUD(@RequestBody Role role, HttpServletRequest request,
                      HttpServletResponse response) {
        if (role == null) {
            return null;
        }
        String status = "";
        String roleName = role.getRoleName();
        boolean roleExistsFlag = false;
        HttpSession session = request.getSession();
        
        User loggedUser = (User)session.getAttribute(SessionKey.USER.getKey());
        
        Integer userId = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                .getKey())) ? null : (Integer) session
                .getAttribute(SessionKey.USER_ID.getKey());

        try {
            if (role.getTransactionType().equalsIgnoreCase("Edit Role")) {
                Role roles = lookUpService.getUserRole(role.getRoleId());
                if (!roles.getRoleName().equalsIgnoreCase(role.getRoleName())) {
                    roleExistsFlag = userService.ifRoleExists(role.getRoleName());
                }
            }
            if (role.getTransactionType().equalsIgnoreCase("Add Role")) {
                roleExistsFlag = userService.ifRoleExists(roleName);
            }

            if (!roleExistsFlag) {
                status = userService.setRoleCUD(role, userId);
                if (status.equalsIgnoreCase("success")) {
                    if (role.getTransactionType().equalsIgnoreCase("Delete Role")) {
                        List<Integer> userIds = userService.getUserByRole(role.getRoleId());
                        if (userIds != null && userIds.size() != 0) {
                            Integer[] intUserIds = new Integer[userIds.size()];
                            for (int i = 0; i < userIds.size(); i++) {
                                intUserIds[i] = userIds.get(i);
                            }
                            
                            logger.info("Called the controller /setRoleCUD/ to delete users.....");
                            

                            @SuppressWarnings("unchecked")
                			List<Pair<String, String>> loggerMessages = Arrays.asList(
                            				new Pair<String,String>(ParamKeyEnum.DELETED_USER_IDS.toString(),Arrays.asList(userIds).toString()),
                                       		new Pair<String,String>(ParamKeyEnum.DELETED_BY.toString(),loggedUser.getUserName()),
                                       		new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),loggedUser.getCompany().getCompanyName()),
                                       		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),loggedUser.getCompany().getCompanyId().toString()),
                                       		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),loggedUser.getUserId().toString()),
                                       		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request)),
                                       		new Pair<String,String>(ParamKeyEnum.DELETED_DATE.toString(),new Date().toString())
                                       		);
                               
                            logger.info("Deleted Company +++ "+loggerMessages);
                            
                            userService.deleteUsers(intUserIds,loggedUser.getUserId());
                        }
                    }
                    status = "success";
                }
            } else {
                if (roleExistsFlag) {
                    status = "Role exists";
                } else {
                    status = "failed";
                }
            }
        }
        catch (Exception e) {
            logger.error("Failed to get roles");
        }
        return status;
    }

    /**
     * To get download status of the file
     *
     * @param request HttpServletRequest
     * @return String status of the download file
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getTMXUploadStatus/{fileId}")
    public
    @ResponseBody
    FileUploadStatus getTMXUploadStatus(@PathVariable Integer fileId, HttpServletRequest request, HttpServletResponse response) {
        try {
            FileUploadStatus fileData = new FileUploadStatus();
            fileData = termsService.getFileUploadStatus(fileId);
            logger.info(" +++  Got file upload status");
            return fileData;

        }
        catch (Exception e) {
            logger.error("Failed tmx upload status");
        }
        return null;

    }

    /**
     * To get FileUploadstatus details
     *
     * @param request   HttpServletRequest
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of FileUploadstatus
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getImportExportData/{userId}/{colName}/{sortOrder}/{pageNum}")
    public
    @ResponseBody
    List<FileUploadStatus> getImportExportData(@PathVariable Integer userId, @PathVariable String colName, @PathVariable String sortOrder,
                                               @PathVariable Integer pageNum, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            Integer userIdVal = Utils.isNull(session.getAttribute(SessionKey.USER_ID
                    .getKey())) ? null : (Integer) session
                    .getAttribute(SessionKey.USER_ID.getKey());
            List<FileUploadStatus> import_export_list = termsService.getImportExportData(userIdVal, colName, sortOrder, pageNum);
            if (import_export_list != null) {
                logger.info(" +++  Got FileUploadStatus data");
                return import_export_list;
            } else {
                logger.info(" +++  NULL FileUploadStatus data");
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to get FileUploadStatus");
        }
        return null;
    }

    /**
     * To request change  term, by a user
     *
     * @param request           HttpServletRequest
     * @param termRequestChange TermRequestChange details of  term for a request
     */
    @RequestMapping(method = RequestMethod.POST, value = "/requestChangeTerm")
    public
    @ResponseBody
    void requestChangeTerm(@RequestBody TermRequestChange termRequestChange,
                           HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = Utils.isNull(session.getAttribute(SessionKey.USER
                    .getKey())) ? null : (User) session
                    .getAttribute(SessionKey.USER.getKey());
            String emailSubject = EmailTemplateEnum.NEW_REQUEST_CHANGE.getValue();
            EmailTemplates requestChangeTemplate = lookUpService.getEmailTemplateBySubject(emailSubject);
            
            String requestTermContent = requestChangeTemplate.getEmailMessageContent();
            requestTermContent = requestTermContent.replace("Thank you.", " ");
            if (termRequestChange != null) {
            	String source = termRequestChange.getSourceTerm() != null ? termRequestChange.getSourceTerm() : "";
            	String target = termRequestChange.getTargetTerm() != null ? termRequestChange.getTargetTerm() : "";
            	String newTarget = termRequestChange.getNewSuggestedTerm() != null ? termRequestChange.getNewSuggestedTerm().trim() : "";
            	String notes = termRequestChange.getNotes() != null ? termRequestChange.getNotes() : "";
            	
            	requestTermContent = requestTermContent + " <table width=\"100%\" style=\"border: 1px solid black;border-collapse: collapse;\"> "  +
            			" <thead> <tr id=\"newtable\"> " +
            			" <th width=\"16%\" style=\"border: 1px solid black;background-color:#007dba; color:white;\"> "+
            			" <div style=\"padding-left:10px;text-align:left;\"> Source </div></th>" +
            			" <th width=\"16%\" style=\"border: 1px solid black;background-color:#007dba; color:white;\">" +
            			" <div style=\"padding-left:10px;text-align:left;\"> Target </div></th>" +
            			" <th width=\"16%\" style=\"border: 1px solid black;background-color:#007dba; color:white;\">" +
            			" <div style=\"padding-left:10px;text-align:left;\"> Suggested New Target </div> </th>" +
            			" <th width=\"16%\" style=\"border: 1px solid black;background-color:#007dba; color:white;\">" +
            			" <div style=\"padding-left:10px;text-align:left;\"> Notes </div></th>" + 
            			" <th width=\"16%\" style=\"border: 1px solid black;background-color:#007dba; color:white;\">" +
                    	" <div style=\"padding-left:10px;text-align:left;\"> User Name </div></th>" + 
            			"<th width=\"16%\" style=\"background-color:#007dba; color:white;\">" +
                    	" <div style=\"padding-left:10px;text-align:left;\"> Email Id </div></th>"	+ "</tr> </thead> " +
            			"<tbody>" + "<tr>" + " <td style=\"border: 1px solid black;font-size: medium;vertical-align:top;\">" +
        				" <div style=\"padding-left:10px;text-align:left;\">" + source +"</div></td>" +
        				" <td style=\"border: 1px solid black; font-size: medium;vertical-align:top;\">" +
        				" <div style=\"padding-left:10px;text-align:left;\">" + target +"</div></td> " +
        				" <td style=\"border: 1px solid black; font-size: medium ;vertical-align:top;\">" +
        				" <div style=\"padding-left:10px;text-align:left;\">" + newTarget + "</div></td> " +
        				" <td style=\"border: 1px solid black; font-size: medium ;vertical-align:top;\">" +
        				" <div style=\"padding-left:10px;text-align:left;\"> " + notes + "</div></td>";
                /*requestTermContent = requestTermContent.replace("$source", termRequestChange.getSourceTerm());
                requestTermContent = requestTermContent.replace("$target", termRequestChange.getTargetTerm());
                requestTermContent = requestTermContent.replace("$newtarget", termRequestChange.getNewSuggestedTerm().trim());
                if (termRequestChange.getNewSuggestedTerm() != "") {
                    requestTermContent = requestTermContent.replace("$notes", termRequestChange.getNotes());
                } else {
                    requestTermContent = requestTermContent.replace("$notes", "");
                }*/
            }
            Integer companyId = null;
            if (user != null) {
            	requestTermContent = requestTermContent + "<td style=\"border: 1px solid black; font-size: medium ;vertical-align:top;\">" +
        				" <div style=\"padding-left:10px;text-align:left;\"> " + user.getUserName() + "</div></td>" +
            			" <td style=\"border: 1px solid black; font-size: medium ;vertical-align:top;\">" +
                		" <div style=\"padding-left:10px;text-align:left;\"> " + user.getEmailId() + "</div></td></tr></tbody></table>" +
            			" </br><p>Thank you.&nbsp;</p>";
                /*requestTermContent = requestTermContent.replace("$requestor", user.getUserName());
                requestTermContent = requestTermContent.replace("$email", user.getEmailId());*/
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();


                Set<String> roleNames = new HashSet<String>();
                roleNames.add(RoleEnum.SUPER_ADMIN.getValue());
                roleNames.add(RoleEnum.COMPANY_ADMIN.getValue());
                roleNames.add(RoleEnum.COMPANY_TERM_MANAGER.getValue());
                roleNames.add(RoleEnum.SUPER_TRANSLATOR.getValue());
                Set<Integer> roleIds = userService.getRoleIdListByLabel(roleNames);

                List<User> userList = userService.getUserListByCompanyAndRole(companyId, roleIds);
                String roleName = RoleEnum.SUPER_ADMIN.getValue();
                Role role = userService.getRoleIdByLabel(roleName);
                List<User> superAdminUserList = userService.getUserListByRole(role.getRoleId());
                if (userList != null) {
                    userList.addAll(superAdminUserList);
                }
                Set<String> emailIds = new HashSet<String>();
                for (User userObj : userList) {
                    emailIds.add(userObj.getEmailId());
                }
                if (emailIds != null && emailIds.size() > 0) {
                    mailService.sendMailUsingTemplate(emailIds, requestTermContent, emailSubject);
                }
            }

            logger.info(" +++  Requested for new term");

        }
        catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            logger.error("Failed to request new Term");
        }
    }

    /**
     * To save gs credintials details
     *
     * @param request       HttpServletRequest
     * @param gsCredintials gsCredintials obj to be saved
     * @return String of the path to be redirected
     */
    @RequestMapping(method = RequestMethod.POST, value = "/saveGSCredintials")
    public
    @ResponseBody
    String saveGSCredintials(@RequestBody GSCredintials gsCredintials, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            String status = null;
            Company company = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
                company = gsService.getGSCredintails(companyId);
            }


            if (company != null) {
                if (gsCredintials.getUrl() != null) {
                    company.setUrl(gsCredintials.getUrl());
                }
                if (gsCredintials.getUserName() != null) {
                    company.setUserName(gsCredintials.getUserName());
                }
                if (gsCredintials.getPassword() != null) {
                    company.setPassword(gsCredintials.getPassword());
                }
                company.setCreateDate(new Date());
                company.setCreatedBy(user.getUserId());
                company.setIsActive(gsCredintials.getIsActive());
                status = gsService.saveOrUpdateGSCredintials(company);
                logger.info(" +++  Saved gs credintials for company");
                return status;
            }

        }
        catch (Exception e) {
            logger.error("Error in saving gs credintials for company");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * To get global sight job list
     *
     * @param request HttpServletRequest
     * @param pageNum PathVariable Integer to limit the data
     * @return List of jobs
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getGlobalSightJobList/{pageNum}")
    public
    @ResponseBody
    List<Jobs> getGlobalSightJobList(@PathVariable Integer pageNum, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Jobs> jobList = null;
        String accessToken = null;
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        Company company = null;
        if (user != null) {
            company = user.getCompany();
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
            company = lookUpService.getCompanyById(companyId);
        }
        if (company == null) {
            company = lookUpService.getCompanyIdByLabel(TeaminologyProperty.INTERNAL_COMPANY.getValue());
        }

        try {
            if (accessToken == null) {
                accessToken = gsService.getAccessToken(company);
                //session.setAttribute(SessionKey.ACCESS_TOKEN.getKey(), accessToken);
            }
            jobList = gsService.getGlobalSightJobList(accessToken, pageNum);
            logger.info(" +++  Got job list details");
        }
        catch (Exception e) {
            logger.error("Failed to get jobList  details");
        }
        return jobList;

    }

    /**
     * To get tasks in jobs
     *
     * @param request HttpServletRequest
     * @param jobId   String to identify jobs
     * @return List of Tasks
     */

    @RequestMapping(method = RequestMethod.GET, value = "/getTasksInJob/{jobId}")
    public
    @ResponseBody
    List<Tasks> getTasksInJob(@PathVariable String jobId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Tasks> taskList = null;
        String accessToken = null;
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        Company company = null;
        if (user != null) {
            company = user.getCompany();
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
            company = lookUpService.getCompanyById(companyId);
        }
        if (company == null) {
            company = lookUpService.getCompanyIdByLabel(TeaminologyProperty.INTERNAL_COMPANY.getValue());
        }

        try {
            if (accessToken == null) {
                accessToken = gsService.getAccessToken(company);
                //session.setAttribute(SessionKey.ACCESS_TOKEN.getKey(), accessToken);
            }
            taskList = gsService.getTasksInJob(accessToken, jobId);
            logger.info(" +++  Got task list details");
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to get taskList  details");
        }
        return taskList;

    }

    /**
     * To get job count
     *
     * @param request HttpServletRequest
     * @return Integer holding  the total job count
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getJobsCount")
    public
    @ResponseBody
    Integer getJobsCount(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer count = null;
        String accessToken = null;
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        Company company = null;
        if (user != null) {
            company = user.getCompany();
            if (user.getCompany() != null) {
                companyId = user.getCompany().getCompanyId();
                company = lookUpService.getCompanyById(companyId);
            }

        }
        if (company == null) {
            company = lookUpService.getCompanyIdByLabel(TeaminologyProperty.INTERNAL_COMPANY.getValue());
        }
        try {
            if (accessToken == null) {
                accessToken = gsService.getAccessToken(company);
                //session.setAttribute(SessionKey.ACCESS_TOKEN.getKey(), accessToken);
            }
            count = gsService.getJobsCount(accessToken);
            logger.info(" +++  got job list details");
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to get jobList  details");
        }
        return count;

    }

    /**
     * To get GS credintials
     *
     * @param request HttpServletRequest
     * @return company obj
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getGSCredintails")
    public
    @ResponseBody
    Company getGSCredintails(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();

            }
            Company credintialsList = gsService.getGSCredintails(companyId);

            if (credintialsList != null) {
                logger.info(" +++  Got " + credintialsList
                        + " previleges");
                return credintialsList;
            } else {
                logger.info(" +++  empty credintialsList for companyId " + companyId);
                return null;
            }
        }
        catch (Exception e) {
            logger.error("Failed to  get credintialsList");
        }
        return null;
    }

    /**
     * To get all jobs
     *
     * @param request HttpServletRequest
     * @return List jobs
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getAllJobs")
    public
    @ResponseBody
    List<FileInfo> getAllJobs(HttpServletRequest request) {
        try {
            List<FileInfo> fileInfoList = termsService.getAllJobs();
            if (fileInfoList != null) {
                logger.info(" +++  Got jobs data");
                return fileInfoList;
            } else {
                logger.info(" +++  NULL jobs data");
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to get jobs");
        }
        return null;
    }

    /**
     * To get file information by jobIds
     *
     * @param request        HttpServletRequest
     * @param multiJobValues Array of integer holding jobIds
     * @return List of FileInfo obj's
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getFileInfoByJobIds/{multiJobValues}")
    public
    @ResponseBody
    List<FileInfo> getFileInfoByJobIds(@PathVariable String multiJobValues, HttpServletRequest request) {
        List<FileInfo> fileInfoList = null;
        try {
            if (multiJobValues == null) {
                return null;
            }
            String[] jobIds = null;
            if (multiJobValues != null) {
                if (multiJobValues.contains(",")) {
                    jobIds = multiJobValues.split(",");
                } else {
                    jobIds = new String[1];
                    jobIds[0] = multiJobValues;
                }
            }
            fileInfoList = termsService.getFileInfoByJobIds(jobIds);
            logger.info(" +++   got the file information");
            return fileInfoList;

        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to get file info");
        }
        return null;
    }

    /**
     * To import gs tasks
     *
     * @param request        HttpServletRequest
     * @param taskId         -String need to be import
     * @param taskName       - String need to be import
     * @param jobId          -String need to be import
     * @param jobName-       String need to be import
     */
    @RequestMapping(method = RequestMethod.GET, value = "/importGSTasks/{taskId}/{taskName}/{jobId}/{jobName}")
    public
    @ResponseBody
    void importGSTasks(@PathVariable String taskId, @PathVariable String taskName, @PathVariable String jobId, @PathVariable String jobName, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        if (user != null) {
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
        }
        if (taskId != null) {
            FileInfo fileInfo = null;
            fileInfo = gsService.getFileInfoByTaskId(new Integer(taskId));
            if (fileInfo == null) {
                fileInfo = new FileInfo();
                fileInfo.setJobId(jobId);
                fileInfo.setJobName(jobName);
                fileInfo.setTaksName(taskName);
                fileInfo.setTaskId(taskId);
                fileInfo.setCreateDate(new Date());
                fileInfo.setCreatedBy(user.getUserId());
                fileInfo.setIsActive("Y");
                fileInfo.setStatus("In progress");
                termsService.saveFileInfo(fileInfo);
            } else {
                fileInfo.setJobId(jobId);
                fileInfo.setJobName(jobName);
                fileInfo.setTaksName(taskName);
                fileInfo.setTaskId(taskId);
                fileInfo.setCreateDate(new Date());
                fileInfo.setCreatedBy(user.getUserId());
                fileInfo.setIsActive("Y");
                fileInfo.setStatus("In progress");
                gsService.updateFileStatus(fileInfo);
            }

            ExecutorService executorObj = ImportGlobalSightExecutor.INSTANCE.get();
            Runnable thread = new ImportGlobalSightThread(fileInfo.getFileInfoId(), companyId, lookUpService, gsService);
            executorObj.execute(thread);
        }
    }

    /**
     * To get user role list by menu
     *
     * @param request HttpServletRequest
     * @return List user roles
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUserRoleListByMenu")
    public
    @ResponseBody
    List<RoleMenuMgmt> getUserRoleListByMenu(HttpServletRequest request) {
        try {
            String menuName = MenuEnum.TERM_LIST.getValue();
            Menu menu = userService.getMenuIdByLabel(menuName);
            Integer menuId = menu.getMenuId();
            List<RoleMenuMgmt> roleList = userService.getUserRoleListByMenu(menuId);
            return roleList;
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to get user role list");
        }
        return null;
    }

    /**
     * To get all terms
     *
     * @param request       HttpServletRequest
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getTotalTerms")
    public
    @ResponseBody
    void getAllTerms(@RequestBody QueryAppender queryAppender,
                     HttpServletRequest request) {

        List<Integer> managePolltermsList = null;
        try {
        	
        	
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            
            logger.info("Called the controller /getTotalTerms/ to delete total Terms.....");
            
            @SuppressWarnings("unchecked")
			List<Pair<String, String>> loggerMessages = Arrays.asList(
            		new Pair<String,String>(ParamKeyEnum.DELETED_BY.toString(),user.getCompany().getCompanyName()),
            		new Pair<String,String>(ParamKeyEnum.DELETED_DATE.toString(),new Date().toString()),
            		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
            		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
            		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
            		);
            
            logger.info("Get total Terms +++" +loggerMessages);
            
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            managePolltermsList = termsService.getAllTerms(queryAppender, companyId);
            if (managePolltermsList != null && managePolltermsList.size() != 0) {
                Integer[] termIds = new Integer[managePolltermsList.size()];
                for (int j = 0; j < managePolltermsList.size(); j++) {
                	
                	
                    termIds[j] = managePolltermsList.get(j);
                }

                @SuppressWarnings("unchecked")
				List<Pair<String,String>> loggerMsg = Arrays.<Pair<String,String>>asList(
                		new Pair<String,String>(ParamKeyEnum.DELETED_TERM_ID.toString(),Arrays.asList(termIds).toString()));
                
                logger.info("Deleted terms+++++"+loggerMsg);

                termsService.deleteTerms(termIds, user);
            }
            if (managePolltermsList != null
                    && !managePolltermsList.isEmpty()
                    && managePolltermsList.size() > 0) {
                logger.info(" +++  Got " + managePolltermsList.size());

            } else {
                logger.info(" +++  NULL manage Poll Terms ");
            }

        }
        catch (Exception e) {
            logger.error("Failed to manage poll terms");
        }

    }

    /**
     * To get total tm terms
     *
     * @param request       HttpServletRequest
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getTotalTms")
    public
    @ResponseBody
    void getTotalTms(@RequestBody QueryAppender queryAppender,
                     HttpServletRequest request) {

        List<Integer> managePolltermsList = null;
        try {
        	HttpSession session = request.getSession();
        	User user = (User) session.getAttribute(SessionKey.USER.getKey());

        	logger.info("Called the controller /getTotalTms/ to delete  all tm's.....");

        	@SuppressWarnings("unchecked")
        	List<Pair<String, String>> loggerMessages = Arrays.asList(
        			new Pair<String,String>(ParamKeyEnum.DELETED_BY.toString(),user.getUserName()),
        			new Pair<String,String>(ParamKeyEnum.DELETED_DATE.toString(),new Date().toString()),
        			new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
        			new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
        			new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
        			new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
        			);

            logger.info("get all Gsterms ++++ "+loggerMessages);
            
            Integer companyId = null;
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            managePolltermsList = termsService.getTotalTms(queryAppender, companyId);
            if (managePolltermsList != null && managePolltermsList.size() != 0) {
                Integer[] termIds = new Integer[managePolltermsList.size()];
                for (int j = 0; j < managePolltermsList.size(); j++) {
                    termIds[j] = managePolltermsList.get(j);
                }

                @SuppressWarnings("unchecked")
				List<Pair<String,String>> loggerMsg = Arrays.<Pair<String,String>>asList(
                		new Pair<String,String>(ParamKeyEnum.DELETED_TMS_ID.toString(),Arrays.asList(termIds).toString()));
                
                logger.info("Deleted tms +++++ "+loggerMsg);

                
                termsService.deleteTms(termIds, user);
            }
            if (managePolltermsList != null
                    && !managePolltermsList.isEmpty()
                    && managePolltermsList.size() > 0) {
                logger.info(" +++  Got " + managePolltermsList.size());

            } else {
                logger.info(" +++  NULL manage Poll Terms ");
            }

        }
        catch (Exception e) {
            logger.error("Failed to manage poll terms");
        }


    }

    /**
     * To get all gs terms
     *
     * @param request       HttpServletRequest
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getAllGSTerms")
    public
    @ResponseBody
    void getAllGSTerms(@RequestBody QueryAppender queryAppender,
                       HttpServletRequest request) {

        List<Integer> managePolltermsList = null;
        try {
        	
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(SessionKey.USER.getKey());
            Integer companyId = null;
            
            logger.info("Called the controller /getAllGSTerms/ to delete  all gs terms.....");
            
            @SuppressWarnings("unchecked")
			List<Pair<String, String>> loggerMessages = Arrays.asList(
            		new Pair<String,String>(ParamKeyEnum.DELETED_BY.toString(),user.getUserName()),
            		new Pair<String,String>(ParamKeyEnum.DELETED_DATE.toString(),new Date().toString()),
            		new Pair<String,String>(ParamKeyEnum.LOGGED_IN_USER_ID.toString(),user.getUserId().toString()),
            		new Pair<String,String>(ParamKeyEnum.COMPANY_ID.toString(),user.getCompany().getCompanyId().toString()),
            		new Pair<String,String>(ParamKeyEnum.COMPANY_NAME.toString(),user.getCompany().getCompanyName()),
            		new Pair<String,String>(ParamKeyEnum.IP_ADDRESS.toString(),Utils.getIpAddress(request))
            		);
            
            logger.info("get all Gsterms ++++ "+loggerMessages);
            
            if (user != null) {
                if (user.getCompany() != null)
                    companyId = user.getCompany().getCompanyId();
            }
            queryAppender.setSearchIndexType(IndexTypeEnum.GS.name());
            managePolltermsList = termsService.getTotalGSTerms(queryAppender, companyId);
            if (managePolltermsList != null && managePolltermsList.size() != 0) {
                Integer[] termId = new Integer[managePolltermsList.size()];
                for (int j = 0; j < managePolltermsList.size(); j++) {
                    termId[j] = managePolltermsList.get(j);
                }
                
                @SuppressWarnings("unchecked")
				List<Pair<String,String>> loggerMsg = Arrays.<Pair<String,String>>asList(
                		new Pair<String,String>(ParamKeyEnum.DELETED_TERM_ID.toString(),Arrays.asList(termId).toString()));
                
                logger.info("Deleted terms+++++"+loggerMsg);

                termsService.deleteGSTerms(termId, companyId);
                termsService.deleteTerms(termId, user);
            }

            if (managePolltermsList != null
                    && !managePolltermsList.isEmpty()
                    && managePolltermsList.size() > 0) {
                logger.info(" +++  Got " + managePolltermsList.size());

            } else {
                logger.info(" +++  NULL gs Terms ");
            }

        }
        catch (Exception e) {
            logger.error("Failed to getting the gs terms");
        }


    }

    /**
     * To delete import files
     *
     * @param request HttpServletResponse
     * @param fileIds  An Integer array that needs to be deleted
     */
    @RequestMapping(method = RequestMethod.GET, value = "/deleteImportFiles/{fileIds}")
    public
    @ResponseBody
    void deleteImportFiles(@PathVariable Integer[] fileIds,
                           HttpServletRequest request) {
        try {
            String importStatus = null;
            List<FileUploadStatus> importFileList = null;

            if (fileIds.length != 0) {
                importFileList = termsService.getImportStatusFiles(fileIds);
                List<Integer> fileIdsList = new ArrayList<Integer>();
                if (importFileList != null && importFileList.size() > 0) {
                    for (FileUploadStatus importedFileList : importFileList) {
                        importStatus = importedFileList.getFileStatus();
                        Integer id = importedFileList.getFileUploadStatusId();
                        if (importStatus.equalsIgnoreCase("Import Completed") || importStatus.equalsIgnoreCase("Import failed") || importStatus.equalsIgnoreCase("File upload failed")) {
                            fileIdsList.add(id);
                        }


                    }
                }
                if (fileIdsList != null && fileIdsList.size() != 0) {
                    Integer[] fileId = new Integer[fileIdsList.size()];
                    for (int j = 0; j < fileIdsList.size(); j++) {
                        fileId[j] = fileIdsList.get(j);
                    }

                    termsService.deleteImportFiles(fileId);
                }
                logger.info(" +++  Successfully deleted the files");
            }
        }
        catch (Exception e) {
            logger.error("Failed to delete the files");
        }

    }

    /**
     * To get jobs by state
     *
     * @param pageNum     PathVariable Integer to limit the data
     * @return List of Jobs
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getJobsByState/{pageNum}")
    public
    @ResponseBody
    List<Jobs> getJobsByState(@PathVariable Integer pageNum, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Jobs> jobList = null;
        String accessToken = null;
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        Company company = null;
        if (user != null) {
            company = user.getCompany();
            if (user.getCompany() != null)
                companyId = user.getCompany().getCompanyId();
            company = lookUpService.getCompanyById(companyId);
        }
        if (company == null) {
            company = lookUpService.getCompanyIdByLabel(TeaminologyProperty.INTERNAL_COMPANY.getValue());
        }

        try {
            if (accessToken == null) {
                accessToken = gsService.getAccessToken(company);
                //session.setAttribute(SessionKey.ACCESS_TOKEN.getKey(), accessToken);
            }
            jobList = gsService.getJobsByState(accessToken, pageNum);
            logger.info(" +++  Got job list details");
        }
        catch (Exception e) {
            logger.error("Failed to get jobList  details");
        }
        return jobList;

    }

    /**
     * To get job count by state
     *
     * @param request HttpServletRequest
     * @return Integer holding  the total job count
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getJobsCountByState")
    public
    @ResponseBody
    Integer getJobsCountByState(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer count = null;
        String accessToken = null;
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        Company company = null;
        if (user != null) {
            company = user.getCompany();
            if (user.getCompany() != null) {
                companyId = user.getCompany().getCompanyId();
                company = lookUpService.getCompanyById(companyId);
            }

        }
        if (company == null) {
            company = lookUpService.getCompanyIdByLabel(TeaminologyProperty.INTERNAL_COMPANY.getValue());
        }
        try {
            if (accessToken == null) {
                accessToken = gsService.getAccessToken(company);
                //session.setAttribute(SessionKey.ACCESS_TOKEN.getKey(), accessToken);
            }
            count = gsService.getJobsCountByState(accessToken);
            logger.info(" +++  got job list details");
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to get jobList  details");
        }
        return count;

    }

    /**
     * To get manage poll terms
     *
     * @param request       HttpServletRequest
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @return List of debated terms that needs to manage
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getSelectedTermsOnly")
    public
    @ResponseBody
    TMProfileTermsResult getSelectedTermsOnly(@RequestBody QueryAppender queryAppender,
                                              HttpServletRequest request) {
        TMProfileTermsResult tmProfileTermsResult = null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        Integer companyId = null;
        Company company = null;
        if (user != null) {
            company = user.getCompany();
            if (user.getCompany() != null) {
                companyId = user.getCompany().getCompanyId();
                company = lookUpService.getCompanyById(companyId);
            }

        }
        if (company == null) {
            company = lookUpService.getCompanyIdByLabel(TeaminologyProperty.INTERNAL_COMPANY.getValue());
        }
        try {
            tmProfileTermsResult = termsService.getSelectedTermsOnly(queryAppender,company);

            if (tmProfileTermsResult != null) {
                logger.info(" +++  Got " + tmProfileTermsResult.getPollTermsList());
                return tmProfileTermsResult;
            } else {
                logger.info(" +++  NULL selected Terms ");
            }

        }
        catch (Exception e) {
            logger.error("Failed to selected terms");
        }
        return null;

    }
    
    /**
     * To get user comment from  voted term
     *
     * @param termId  Path variable to get particular termId
     * @param request HttpServletRequest
     * @return DataTable holding user comments of term
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUserComment/{termId}")
    public
    @ResponseBody
    DataTable getUserComment(@PathVariable Integer termId, HttpServletRequest request) {
    	
        DataTable dataTable = new DataTable();
        
        if (termId == null) {
            return null;
        }
        
        try {
            List<UserComment> userCommentList = termsService.getUserComment(termId);
            
            if (userCommentList != null) {
                logger.info(" +++  Got user comment for term Id +++ " + termId);
                dataTable.setCommentData(userCommentList);
                return dataTable;
            } else {
                logger.info(" +++ user comment data is not available +++ ");
            }

        }
        catch (Exception e) {
            logger.error(" ++++ Errror occured in getting user comments +++ ");
        }
        return null;
    }
 /**
  * 
  * @param request
  * @return
  */
  @RequestMapping(method = RequestMethod.GET, value = "/getSignoutFlag")
  public
  @ResponseBody
  String getSignoutFlag(HttpServletRequest request) {
	//  List l =new ArrayList();
	  String logoutStatus = "logOut";
	  HttpSession session = request.getSession();
	  session.setAttribute("logOutMessage", "logOut");
	 // l.add(logoutStatus);
	  return logoutStatus;
  }
  /**
   * 
   * @param user_lang_id
   * @param request
   * @return
   */
  @RequestMapping(method = RequestMethod.GET, value = "/getAllMembersByLanguage/{userLangId}")
  public
  @ResponseBody
  DataTable getAllMembersByLanguage(@PathVariable String userLangId ,HttpServletRequest request) {
      HttpSession session = request.getSession();
      User user = (User) session.getAttribute(SessionKey.USER.getKey());
      Integer companyId = null;
      String langIds[] = userLangId.split(",");
     // Integer[] userLangIds = new Integer[langIds.length];
      int userLangIds[] = new int[langIds.length];
      for(int i=0;i<langIds.length;i++) {
    	  int val = Integer.parseInt(langIds[i]);
    	  userLangIds[i] = val;
      }
      String statusName = StatusLookupEnum.APPROVED.getValue();
      Status status = lookUpService.getStatusIdByLabel(statusName);
      Integer statusId = status.getStatusId();
      if (user != null) {
          if (user.getCompany() != null)
              companyId = user.getCompany().getCompanyId();
      }
      Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
      if (companyId == null && company != null) {
          companyId = company.getCompanyId();
      }
      try {
          List<Member> memberList = termsService.getAllBoardMembersByLanguage(companyId,userLangId);
          List<Member> modifiedMemberList = new ArrayList<Member>();
          String photoPath = null;
          String badgingPhotopath = null;
          String accuracyImg = null;
          DataTable dataTable = new DataTable();

          if (memberList != null && !memberList.isEmpty()
                  && memberList.size() > 0) {
              logger.info(" +++  Got " + memberList.size()
                      + " Leader Board Members");
              for (int i = 0; i < memberList.size(); i++) {
                  float accuracyRate = 0;
                  int badgingNum = 0;
                  String statuslevel = null;
                  String leftMargin = null;
                  Member modifiedMember = memberList.get(i);
                  photoPath = modifiedMember.getPhotoPath();
                  Integer totalvotes = (Integer) ((modifiedMember.getTotalVotes() == null) ? 0 : modifiedMember.getTotalVotes().intValue());
                  //Integer termRequestCount = (modifiedMember.getTermRequestCount() == null) ? 0 : modifiedMember.getTermRequestCount();
                  modifiedMember
                          .setPhotoPath("<img src='"
                                  + photoPath
                                  + "' height='38px' width='38px' class='alignCenter' />");

                  //Integer badgingRate = totalvotes.intValue() + termRequestCount.intValue();
                  Integer badgingRate = totalvotes.intValue();
                  if (badgingRate >= 0 && badgingRate < 25) {
                      badgingPhotopath = request.getContextPath() + "/images/biginner.jpg";
                      badgingNum = 0;
                  } else if (badgingRate >= 25 && badgingRate < 50) {
                      badgingPhotopath = request.getContextPath() + "/images/novice.jpg";
                      badgingNum = 1;
                  } else if (badgingRate >= 50 && badgingRate < 100) {
                      badgingPhotopath = request.getContextPath() + "/images/regular.jpg";
                      badgingNum = 2;
                  }else if (badgingRate >= 100  && badgingRate < 200) {
                      badgingPhotopath = request.getContextPath() + "/images/advanced.jpg";
                      badgingNum = 3;
                  }else if (badgingRate >= 200) {
                      badgingPhotopath = request.getContextPath() + "/images/expert.jpg";
                      badgingNum = 4;
                  }
               
                  modifiedMember.setBagdingPhotopath("<span class='nodisplay'>" + badgingNum + "</span><img src='" + badgingPhotopath + "'  class='alignCenter imageWidth150' />");
                  String formattedVotes = Utils.format(modifiedMember.getTotalVotes().intValue());
                  Map<String, BigInteger> userAccuracyRate = userService.getUserAccuracyRate(modifiedMember.getUserId(), statusId, companyId);
                  float finalizedTerm = userAccuracyRate.get("finalizedTerm").floatValue();
                  float votedTerms = userAccuracyRate.get("votedTerms").floatValue();
                  int intAccuracy = 0;
                  if (finalizedTerm != 0 && votedTerms != 0) {
                      accuracyRate = finalizedTerm / votedTerms;
                      accuracyRate = accuracyRate * 100;
                  }
                  if (accuracyRate >= 0 && accuracyRate < 25) {
                      accuracyImg = request.getContextPath() + "/images/BeginnerAccuracy.jpg";
                      intAccuracy = 0;
                  } else if (accuracyRate >= 25 && accuracyRate < 50) {
                      accuracyImg = request.getContextPath() + "/images/NoviceAccuracy.jpg";
                      intAccuracy = 1;
                  } else if (accuracyRate >= 50 && accuracyRate < 100) {
                      accuracyImg = request.getContextPath() + "/images/RegularAccuracy.jpg";
                      intAccuracy = 2;
                  } else if (accuracyRate >= 100 && accuracyRate < 200) {
                      accuracyImg = request.getContextPath() + "/images/AdvancedAccuracy.jpg";
                      intAccuracy = 3;
                  }else if(accuracyRate >= 200){
                  	accuracyImg = request.getContextPath() + "/images/ExpertAccuracy.jpg";
                  	intAccuracy = 4;
					}
                  modifiedMember.setAccuracyPhotoPath("<span class='nodisplay'>" + intAccuracy + "</span><img src='" + accuracyImg + "'  class='alignCenter' />");
                  modifiedMember.setVotes(formattedVotes);
                  modifiedMemberList.add(modifiedMember);
              }
              dataTable.setAaData(modifiedMemberList);
              return dataTable;
          } else {
              logger.info(" +++  NULL Leader Board Members");
          }
      }
      catch (Exception e) {
          logger.error("Failed to get Leader Board Members");
          logger.error(e,e);
      }
      return null;
  }
}
