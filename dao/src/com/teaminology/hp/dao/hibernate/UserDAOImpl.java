package com.teaminology.hp.dao.hibernate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.CompanyTransMgmt;
import com.teaminology.hp.bo.Menu;
import com.teaminology.hp.bo.Privileges;
import com.teaminology.hp.bo.Role;
import com.teaminology.hp.bo.RoleMenuMgmt;
import com.teaminology.hp.bo.RolePrivileges;
import com.teaminology.hp.bo.RoleSubmenuMgmt;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserLanguages;
import com.teaminology.hp.bo.UserRole;
import com.teaminology.hp.bo.lookup.Company;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.dao.HibernateDAO;
import com.teaminology.hp.dao.IUserDAO;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.UsersChartData;

/**
 * Contains DAO methods to handle User data.
 *
 * @author sarvaniC
 */
@SuppressWarnings("unused")
public class UserDAOImpl extends HibernateDAO implements IUserDAO
{
    private static Logger logger = Logger.getLogger(UserDAOImpl.class);

    protected static final String LS = System.getProperty("line.separator");
    private static final String LIKE_PERCENT = "%";
    private static final String LIKE_UNDERSCORE = "_";
    private static final Integer APPROVED_STATUS_ID = 2;

    private final static String GET_USER_DETAILS = LS + " select  "
            + LS + " usr.user_id,usr.user_name,usr.first_name, usr.last_name, usr.email_id,date_format(usr.create_time, '%m-%d-%y') as created_date ,rl.role_name,lang.language_label,usr.domain_id, usr.photo_path, usr_votes.votes,usr.term_request_count "
            + LS + " from (select count(term_vote_usr_dtls_id) votes from term_vote_user_details where vote_invite_status ='Y' and  (is_active = 'Y' or is_active is null) and user_id =:param0 ) usr_votes, user usr"
            + LS + " left join (user_languages usrlang inner join language_lookup lang  on (lang.language_id = usrlang.language_id && (lang.is_active = 'Y' or lang.is_active is null))) on (usrlang.user_id = usr.user_id )"
            + LS + "  left join(user_role url inner join role rl on(rl.role_id=url.role_id && (rl.is_active='Y'))) on (usr.user_id=url.user_id)"
            + LS + " where ";

    private final static String USERS_PER_MONTH = LS + " select "
            + LS + " date_format(create_time,'%b') as month, count(user_id) count"
            + LS + " from user"
            + LS + " where create_time  <= (adddate(curdate(), interval -1 month))"
            + LS + " and create_time  >= (adddate(curdate(), interval -5 month))"
            + LS + " group by month(create_time)"
            + LS + " order by create_time";

    private final static String GET_ACTIVE_USERS_COUNT = LS + " select"
            + LS + " count(user_id) as count "
            + LS + " from user "
            + LS + " where is_active='Y'";

    private final static String GET_HP_MEMBERS = LS + "select usr.user_id,usr.user_name,usr.first_name, usr.last_name,  "
            + LS + " lang.language_label, usr.photo_path,(select count(tdtls.term_vote_usr_dtls_id)  "
            + LS + " from term_vote_user_details tdtls where tdtls.vote_invite_status ='Y' &&  (tdtls.is_active = 'Y' or tdtls.is_active is null) && tdtls.user_id = usr.user_id) votes from"
            + LS + " user usr left join (user_languages usrlang inner join language_lookup lang  on lang.language_id = usrlang.language_id )"
            + LS + " on (usrlang.user_id = usr.user_id )"
            + LS + " where usr.is_active ='Y'  "//and usr.user_type_id in (2,3)
            + LS + "  order by votes desc, usr.user_name asc ";


    private final static String GET_TOTAL_USERS_LANG_TERMS = LS + " select "
            + LS + " count(tvud.term_id) from term_vote_user_details tvud "
            + LS + " inner join term_information ti on (tvud.term_id = ti.term_id && ti.suggested_term_lang_id = :param0)"
            + LS + " left join term_vote_master tm on (tm.term_vote_id=tvud.term_vote_id ) "
            + LS + " where tvud.user_id = :param1 and (vote_invite_status = 'Y' or vote_invite_status is null) and (ti.is_active ='Y' or ti.is_active is null) and (tvud.is_active ='Y' or tvud.is_active is null) "
            + LS + " and (tm.voting_expired_date>curdate() or  tvud.voting_date is not null) ";


    private final static String GET_USER_ACCURACY = LS + "select "
            + LS + "count(tvud.term_vote_usr_dtls_id) as finalised_terms, "
            + LS + "(select count(tvud1.term_id) from term_vote_user_details tvud1 inner join term_information termI on(termI.term_id=tvud1.term_id and termI.term_status_id = 2) "
            + LS + " where tvud1.user_id = :param0 and tvud1.vote_invite_status = 'Y' and  (tvud1.is_active = 'Y' or tvud1.is_active is null)) as voted_terms,"
            + LS + "tvud.term_id from term_vote_user_details tvud "
            + LS + "left join (term_information ti)"
            + LS + "on (ti.term_id = tvud.term_id and ti.term_status_id = :param1)"
            + LS + "where tvud.user_id= :param2 and tvud.term_translation_id in "
            + LS + "(select term_translation_id from term_translation tt where tt.suggested_term = ti.suggested_term)"
            + LS + "and tvud.vote_invite_status = 'Y' and ti.is_active='Y'";

    private final static String GET_TEAM_MEMBERS = LS + " select"
            + LS + "usr.user_id,usr.user_name, usr.first_name, usr.last_name, usr.email_id,date_format(usr.create_time, '%m-%d-%Y'),count(tdtls.term_vote_usr_dtls_id) as total_votes, "
            + LS + "round( (select count(tvud.term_vote_usr_dtls_id) from term_vote_user_details tvud left join (term_information ti) "
            + LS + "on (ti.term_id = tvud.term_id and ti.term_status_id = 2) where tvud.term_translation_id in (select term_translation_id from term_translation tt " +
            		"where tt.suggested_term = ti.suggested_term and tt.is_active='Y')"
            + LS + "and tvud.vote_invite_status = 'Y'  and tvud.is_active = 'Y' and ti.is_active='Y' "
            + LS + "and user_id =  usr.user_id ) * 100/ count(tdtls.term_vote_usr_dtls_id)) as accuracy ,(select count(user_id) as last_month "
            + LS + "from term_vote_user_details where  date(voting_date) >= adddate(date(curdate()), interval -1 month)  and"
            + LS + " date(voting_date) <= date(curdate()) and "
            + LS + "vote_invite_status ='Y' and user_id =  usr.user_id ) as last_month,company.company_name,rl.role_name, "
            + LS + "(count(tdtls.term_vote_usr_dtls_id)+ if(usr.term_request_count is null, 0,usr.term_request_count )) as badging"
            + LS + " from  user usr "
            + LS + "left join (term_vote_user_details tdtls) on (vote_invite_status ='Y' && usr.user_id = tdtls.user_id && tdtls.is_active = 'Y') "
            + LS + "left join(company_lookup company) on (company.company_id=usr.company_id && company.is_active='Y')"
            + LS + "left join(user_role url inner join role rl on(rl.role_id=url.role_id && (rl.is_active='Y'))) on (usr.user_id=url.user_id)"
            + LS + "where usr.is_active ='Y'";// and usr.user_type_id != 1


    private final static String DELETE_USER_LANGUAGE = LS + " delete com.teaminology.hp.bo.UserLanguages userlanguage "
            + LS + "where userlanguage.userId = ";

    private final static String GET_USER_MANAGE_POLL_TERMS = LS
            + " select"
            + LS
            + " ti.term_id,ti.term_being_polled, ti.suggested_term,pos.part_of_speech,cat.category,lang.language_label,status.status, "
            + LS
            + " date_format(tvm.voting_expired_date, '%m/%d/%Y') as expired_date ,"
            + LS
            + " (select sum(tt1.vote) from term_translation tt1 where tt1.term_id=ti.term_id) as all_votes, (select count(tvud.term_id) from term_vote_user_details tvud "
            + LS
            + " where (tvud.vote_invite_status ='Y'  or tvud.vote_invite_status is null) and  (tvud.is_active = 'Y' or tvud.is_active is null)"
            + LS
            + " and tvud.term_id=ti.term_id) as invites,(select count(dd.term_id) from deprecated_term_info dd where dd.term_id=ti.term_id and dd.is_active='Y') as is_deprecate "
            + LS
            + " from term_information ti "
            + LS
            + " left join (language_lookup lang ) on (ti.suggested_term_lang_id = lang.language_id)"
            + LS
            + " left join (status_lookup status ) on (ti.term_status_id = status.status_id)"
            + LS
            + " left join (term_vote_master tvm) on (ti.term_id = tvm.term_id)"
            + LS
            + " left join(parts_of_speech_lookup pos) on (ti.term_pos_id = pos.parts_of_speech_id)"
            + LS
            + " left join(category_lookup cat) on (ti.term_category_id = cat.category_id)"
            + LS
            + " left join (term_translation tt) on (ti.term_id = tt.term_id) "
            + LS
            + " left join (deprecated_term_info dt) on (ti.term_id=dt.term_id)"
            + LS + " where ti.is_active!='n'";

    private final static String GET_USER_TM_PROFILE_TERMS = LS
            + " select"
            + LS
            + " tm.tm_profile_info_id,tm.source, tm.target,d.domain,prod.product,lang.language_label,con.content_type "
            + LS
            + " from tm_profile_info tm "
            + LS
            + " left join (language_lookup lang ) on (tm.target_lang = lang.language_id)"
            + LS
            + " left join ( domain_lookup d ) on (tm.industry_domain = d.domain_id)"
            + LS
            + " left join ( content_type_lookup con ) on (tm.content_type = con.content_type_id)"
            + LS
            + " left join (product_group_lookup prod) on (tm.product_line = prod.product_id)"
            + LS + " where tm.is_active='Y'";

    private final static String DELETE_USER_ROLE_PRIVILEGES = LS + " delete com.teaminology.hp.bo.RolePrivileges rolePrivileges "
            + LS + "where rolePrivileges.role.roleId = ";

    private final static String DELETE_USER_ROLE_MENUS = LS + " delete com.teaminology.hp.bo.RoleMenuMgmt roleMenuMgmt "
            + LS + "where roleMenuMgmt.role.roleId = ";

    private final static String DELETE_USER_ROLE_SUBMENUS = LS + " delete com.teaminology.hp.bo.RoleSubmenuMgmt roleSubmenuMgmt "
            + LS + "where roleSubmenuMgmt.role.roleId = ";

    private final static String DELETE_COMPANY_TRANSMGMT_USERS = LS + " delete com.teaminology.hp.bo.CompanyTransMgmt companyTransMgmt "
            + LS + "where  companyTransMgmt.userId = ";

    /**
     * To register user
     *
     * @param user User to be registered
     * @return Returns "success" if it is registered else it returns "failure"
     */
    public String registerUser(User user) {
        String status;
        if (user == null) {
            status = "failure";
            return status;
        }
        user.setIsActive("Y");
        getHibernateTemplate().save(user);
        if (user.getUserLanguages() != null) {
            Iterator it = user.getUserLanguages().iterator();
            while (it.hasNext()) {
                // Get element
                UserLanguages userlangs = (UserLanguages) it.next();
                userlangs.setUserId(user.getUserId());
                userlangs.setIsActive("Y");
                userlangs.setCreateDate(new Date());
                getHibernateTemplate().save(userlangs);
            }
        }
        if (user.getUserRole() != null) {
            Iterator roleIterator = user.getUserRole().iterator();
            while (roleIterator.hasNext()) {
                // Get element
                UserRole userRole = (UserRole) roleIterator.next();
                userRole.setUserId(user.getUserId());
                userRole.setIsActive("Y");
                userRole.setCreateDate(new Date());
                getHibernateTemplate().save(userRole);
            }
        }

        logger.debug("User registered successfully :" + user.getUserId());
        status = "success";
        return status;

    }

    /**
     * To get user details
     *
     * @param userId An Integer for which data is to be retrieved
     * @return Member which holds the user details
     */
   // @Transactional
    public Member userDetails(final Integer userId) {

        HibernateCallback<Member> callback = new HibernateCallback<Member>()
        {

            @Override
            public Member doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Member> membersList = new ArrayList<Member>();
                Member userMember = null;
                query.append(GET_USER_DETAILS);
                query.append("usr.user_id=" + userId);
                String previousUserId = null;
                String languageNames = null;
                int noOfLangs = 0;
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.setParameter("param0", userId);
                List<Object> hibernateResults = hibQuery.list();
                if ((hibernateResults != null) && hibernateResults.size() > 0) {
                    for (Object obj : hibernateResults) {
                        Object[] member = (Object[]) obj;
                        String userId = (member[0] == null) ? "" : member[0].toString();
                        String userName = (member[1] == null) ? "" : member[1].toString();
                        String firstName = (member[2] == null) ? "" : member[2].toString();
                        String lastName = (member[3] == null) ? "" : member[3].toString();
                        String email = (member[4] == null) ? "" : member[4].toString();
                        String createDate = (member[5] == null) ? "" : member[5].toString();
                        String userTypeNmae = (member[6] == null) ? "" : member[6].toString();
                        String language = (member[7] == null) ? "" : member[7].toString();
                        Integer domainId = (Integer) ((member[8] == null) ? 0 : member[8]);
                        String photoPath = (((String) member[9] != null) && (((String) member[9])
                                .trim().length() != 0)) ? member[9].toString()
                                : null;
                        BigInteger votes = (BigInteger) ((member[10] == null) ? 0 : member[10]);
                        Integer termRequestCount = (Integer) ((member[11] == null) ? 0 : member[11]);
                        Member hpMember = new Member();
                        if (membersList != null
                                && userId.equals(previousUserId)) {
                            noOfLangs++;

                            int previousElement = membersList.size() - 1;
                            Member previoushpMember = membersList.get(previousElement);
                            languageNames = previoushpMember.getLanguages();
                            membersList.remove(previousElement);
                            languageNames += ", " + language;


                        } else {
                            languageNames = language;
                            noOfLangs = 0;
                        }
                        hpMember.setUserId(Integer.parseInt(userId));
                        hpMember.setUserName(userName);
                        hpMember.setFirstName(firstName);
                        hpMember.setLastName(lastName);
                        hpMember.setEmailId(email);
                        hpMember.setCreateDate(createDate);
                        hpMember.setUserTypeName(userTypeNmae);
                        hpMember.setDomainId(domainId);
                        hpMember.setPhotoPath(photoPath);
                        hpMember.setTotalVotes(votes);
                        hpMember.setLanguages(languageNames);
                        hpMember.setTermRequestCount(termRequestCount);
                        previousUserId = hpMember.getUserId().toString();
                        membersList.add(hpMember);
                        userMember = hpMember;

                    }
                }
                logger.debug("Total users :" + membersList.size());
                return userMember;

            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get users Per month
     *
     * @return List which holds the count of users per month
     */
   // @Transactional
    public List<UsersChartData> usersPerMonth(final Integer companyId) {

        HibernateCallback<List<UsersChartData>> callback = new HibernateCallback<List<UsersChartData>>()
        {

            @Override
            public List<UsersChartData> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Date> createdDateList = new ArrayList<Date>();
                Integer previousCount = 0;
                List<UsersChartData> chartDataList = new ArrayList<UsersChartData>();

                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                Calendar cal = Calendar.getInstance();
                Date date = null;
                Date endDate = null;
                try {
                    cal.add(Calendar.MONTH, -4);
                    date = formatter.parse((cal.get(Calendar.MONTH) + 1) + "-" + 1 + "-" + cal.get(Calendar.YEAR));
                    Calendar now = Calendar.getInstance();
                    endDate = now.getTime();
                    Criteria criteria = session.createCriteria(User.class);
                    criteria.add(Restrictions.not(Restrictions.eq("isActive", "N")));
                    //criteria.add(Restrictions.not(Restrictions.eq("userTypeId", 1)));
                    criteria.add(Restrictions.lt("createTime", date));
                    if (companyId != null) {
                        criteria.add(Restrictions.eq("company.companyId", companyId));
                    }
                    criteria.setProjection(Projections.count("userId"));
                    previousCount = (Integer) criteria.uniqueResult();
                    Criteria criteria1 = session.createCriteria(User.class);

                    //createdDateList = session.createCriteria(User.class)
                    criteria1.setProjection(Projections.property("createTime"));
                    criteria1.add(Restrictions.not(Restrictions.eq("isActive", "N")));
                    // .add(Restrictions.not(Restrictions.eq("userTypeId", 1)))
                    criteria1.add(Restrictions.ge("createTime", date));
                    criteria1.add(Restrictions.lt("createTime", endDate));
                    if (companyId != null) {
                        criteria1.add(Restrictions.eq("company.companyId", companyId));
                    }
                    createdDateList = criteria1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(Order.asc("createTime")).list();
                    logger.debug("Total users per month :" + createdDateList.size());
                    if (createdDateList != null && !createdDateList.isEmpty()) {
                        Map<String, List<Date>> createdDateMap = new HashMap<String, List<Date>>();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                        for (Date dateObj : createdDateList) {
                            String strDate = sdf.format(dateObj);
                            List<Date> dateList = new ArrayList<Date>();
                            if (!createdDateMap.containsKey(strDate)) {
                                dateList.add(dateObj);
                                createdDateMap.put(strDate, dateList);
                            } else {
                                List<Date> tempList = createdDateMap.get(strDate);
                                tempList.add(dateObj);
                                createdDateMap.put(strDate, tempList);
                            }
                        }
                        for (int i = 0; i <= 4; i++) {
                            List<Date> dateList = createdDateMap.get(sdf.format(date));
                            UsersChartData chartData = new UsersChartData();
                            if (dateList != null && !dateList.isEmpty()) {
                                SimpleDateFormat monthFormate = new SimpleDateFormat("MMM");
                                chartData.setMonth(monthFormate.format(date));
                                previousCount = previousCount + dateList.size();
                                chartData.setCount(previousCount);
                            } else {
                                SimpleDateFormat monthFormate = new SimpleDateFormat("MMM");
                                chartData.setMonth(monthFormate.format(date));
                                chartData.setCount(previousCount);
                            }
                            cal.add(Calendar.MONTH, 1);
                            date = cal.getTime();
                            chartDataList.add(chartData);
                        }

                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e);
                }
                return chartDataList;
            }
        };
        HibernateTemplate template = getHibernateTemplate();
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get Total Active users count
     *
     * @return Integer which holds the count of active users
     */
    //@Transactional
    public Integer getActiveUsersCount(final Integer companyId) {
        HibernateCallback<Integer> callback = new HibernateCallback<Integer>()
        {

            @Override
            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Integer usersCount;
                StringBuffer query = new StringBuffer();
                query.append(GET_ACTIVE_USERS_COUNT);
                if (companyId != null) {
                    query.append(" and company_id=" + companyId);

                }
                SQLQuery hibQuery = session.createSQLQuery(query.toString());

                usersCount = new Integer(hibQuery.list().get(0).toString());
                logger.debug("Total active users :" + usersCount);
                return usersCount;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get HP community members
     *
     * @return List of Users registered in Community and Business Member roles
     */
    @Override
    //@Transactional
    public List<Member> getHPMembers() {
        HibernateCallback<List<Member>> callback = new HibernateCallback<List<Member>>()
        {

            @Override
            public List<Member> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Member> membersList = new ArrayList<Member>();
                query.append(GET_HP_MEMBERS);
                String previousUserId = null;
                String languageNames = null;
                int noOfLangs = 0;

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                List<Object> hibernateResults = hibQuery.list();
                if ((hibernateResults != null) && hibernateResults.size() > 0) {
                    for (Object obj : hibernateResults) {
                        Object[] member = (Object[]) obj;
                        String userId = (member[0] == null) ? "" : member[0].toString();
                        String userName = (member[1] == null) ? "" : member[1].toString();
                        String firstName = (member[2] == null) ? "" : member[2].toString();
                        String lastName = (member[3] == null) ? "" : member[3].toString();
                        String language = (member[4] == null) ? "" : member[4].toString();
                        String photoPath = (((String) member[5] != null) && (((String) member[5])
                                .trim().length() != 0)) ? member[5].toString()
                                : null;
                        BigInteger votes = (BigInteger) ((member[6] == null) ? 0 : member[6]);
                        Member hpMember = new Member();
                        if (membersList != null
                                && userId.equals(previousUserId)) {
                            noOfLangs++;
//							if (noOfLangs < 2) {
                            int previousElement = membersList.size() - 1;
                            Member previoushpMember = membersList.get(previousElement);
                            languageNames = previoushpMember.getLanguages();
                            membersList.remove(previousElement);
                            languageNames += ", " + language;
//							} else
//								continue;

                        } else {
                            languageNames = language;
                            noOfLangs = 0;
                        }
                        hpMember.setUserId(Integer.parseInt(userId));
                        hpMember.setUserName(userName);
                        hpMember.setFirstName(firstName);
                        hpMember.setLastName(lastName);
                        hpMember.setPhotoPath(photoPath);
                        hpMember.setTotalVotes(votes);
                        hpMember.setLanguages(languageNames);
                        previousUserId = hpMember.getUserId().toString();
                        membersList.add(hpMember);

                    }
                }
                logger.debug("Total HP Members :" + membersList.size());
                return membersList;

            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get list of user type details
     *
     * @return List of users role look up
     */
    @Override
    @Transactional
    public List<Role> getUserTypeDetails() {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Role.class);
        hibCriteria.add(Restrictions.not(Restrictions.eq("roleId", 1)));
        hibCriteria.add(Restrictions.not(Restrictions.eq("roleId", 4)));
        hibCriteria.add(Restrictions.not(Restrictions.eq("isActive", "N")));
        return (List<Role>) hibCriteria.list();
    }

    /**
     * To get suggested term lang id
     *
     * @param termId Integer to be filtered
     * @return An Integer value holding the count of  user registered languages
     */
    @Override
    @Transactional
    public List<Language> getUserRegLanguages(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Invalid userId");
        }
        List<Language> languageList = new ArrayList<Language>();
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(UserLanguages.class);
        hibCriteria.add(Restrictions.eq("userId", userId));
        hibCriteria.addOrder(Order.asc("languages.languageId"));

        List<UserLanguages> userLanguagesList = (List<UserLanguages>) hibCriteria.list();
        if ((userLanguagesList != null) && userLanguagesList.size() > 0) {
            for (int i = 0; i < userLanguagesList.size(); i++) {
                UserLanguages userLanguages = userLanguagesList.get(i);
                Language languages = userLanguages.getLanguages();
                if (languages.getIsActive().equalsIgnoreCase("Y")) {
                    languageList.add(languages);
                }

            }

        }
        return languageList;

    }

    /**
     * To get total users language terms
     *
     * @param languageId Integer to be filtered
     * @param userId     Integer to be filtered
     * @return An Integer which holds the count of terms w.r.t the language
     */
    @Override
   // @Transactional
    public Integer getTotalUsersLangTerms(final String languageId,
                                          final Integer userId) {
        HibernateCallback<Integer> callback = new HibernateCallback<Integer>()
        {

            @Override
            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Integer totalUsersLangTerms;
                StringBuffer query = new StringBuffer();
                query.append(GET_TOTAL_USERS_LANG_TERMS);

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.setParameter("param0", languageId);
                hibQuery.setParameter("param1", userId);

                totalUsersLangTerms = new Integer(hibQuery.list().get(0)
                        .toString());
                logger.debug("Total users language terms :"
                        + totalUsersLangTerms);
                return totalUsersLangTerms;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return getHibernateTemplate().execute(callback);

    }

    /**
     * To get user accuracy rated values
     *
     * @param userId    Integer to be filtered
     * @param statusId  Integer to be filtered
     * @param companyId An Integer to  filter the users
     * @return A collection which contains the finalised voted terms count and total voted terms count
     */
    //@Transactional
    public Map<String, BigInteger> getUserAccuracyRate(final Integer userId, final Integer statusId, final Integer companyId) {

        HibernateCallback<Map<String, BigInteger>> callback = new HibernateCallback<Map<String, BigInteger>>()
        {

            @Override
            public Map<String, BigInteger> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                Map<String, BigInteger> accuracy = new HashMap<String, BigInteger>();

                query.append(GET_USER_ACCURACY);
            /*	if(companyId!=null){
                    query.append(" and ti.company_id="+companyId);
				}*/
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.setParameter("param0", userId);
                hibQuery.setParameter("param1", statusId);
                hibQuery.setParameter("param2", userId);

                Object[] accuracyDtls = hibQuery.list().toArray();
                if ((accuracyDtls != null) && accuracyDtls.length > 0) {
                    for (int i = 0; i < accuracyDtls.length; i++) {
                        Object[] accuracyDtlsVal = (Object[]) accuracyDtls[i];
                        accuracy.put("finalizedTerm", (BigInteger) accuracyDtlsVal[0]);
                        accuracy.put("votedTerms", (BigInteger) accuracyDtlsVal[1]);
                    }

                }
                logger.debug("users  accuracy rate :" + accuracy.size());
                return accuracy;
            }
        };
        HibernateTemplate template = getHibernateTemplate();
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To verify whether user exists or not in database
     *
     * @param userName to be filtered
     * @return Returns true if user exists else it returns false
     */
    @Transactional
    public boolean ifUserExists(String userName) {

         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.sqlRestriction("lower(trim(user_name)) = ?",
                userName.toLowerCase(), new StringType()));
        criteria.add(Restrictions.eq("isActive", "Y"));
        criteria.setProjection(Projections.count("userId"));
        Integer countOfUser = (Integer) criteria.uniqueResult();

        if (countOfUser > 0) {
            return true;
        }

        return false;
    }

    /**
     * To get admin users
     *
     * @return List of users registered in Admin user type
     */
    @Override
    @Transactional
    public List<UserRole> getAdminUsers() {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(UserRole.class);
        hibCriteria.add(Restrictions.eq("role.roleId", 2));
        return (List<UserRole>) hibCriteria.list();
    }

    /**
     * To get invite user details
     *
     * @param intIds    Array of integer role type/ language id's
     * @param filterBy  String to be filtered by role/language
     * @param companyId An Integer to  filter the users
     * @return List of Users to be invited
     */
    @Override
    @Transactional
    public List<User> inviteUser(String filterBy, Integer[] intIds, Integer companyId, Set<Integer> roleIds) {
         Session session = getHibernateSession();
//		User user;
        Criteria hibCriteria = null;
        hibCriteria = session.createCriteria(User.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        if (companyId != null) {

            Criteria childCriteria = hibCriteria.createCriteria("companyTransMgmt", Criteria.LEFT_JOIN)
                    .add(Restrictions.eq("companyId", companyId)).add(Restrictions.eq("isActive", "Y"));
        }
        if (filterBy.equalsIgnoreCase("role")) {
            Criteria childCriteria = hibCriteria.createCriteria("userRole", Criteria.LEFT_JOIN);
            Criteria childCriteria1 = childCriteria.createCriteria("role", Criteria.LEFT_JOIN);
            childCriteria1.add(Restrictions.in("roleId", intIds));
        }
        if (filterBy.equalsIgnoreCase("language")) {
            Criteria childCriteria = hibCriteria.createCriteria("userLanguages", Criteria.LEFT_JOIN);
            Criteria childCriteria1 = childCriteria.createCriteria("languages", Criteria.LEFT_JOIN);
            Criteria childCriteria2 = hibCriteria.createCriteria("userRole", Criteria.LEFT_JOIN);
            Criteria childCriteria3 = childCriteria2.createCriteria("role", Criteria.LEFT_JOIN)
                    .add(Restrictions.not(Restrictions.in("roleId", roleIds)));
            childCriteria1.add(Restrictions.in("languageId", intIds));

        }
        if (filterBy.equalsIgnoreCase("domain")) {
        	Criteria childCriteria = hibCriteria.createCriteria("domain", Criteria.LEFT_JOIN);
        	childCriteria.add(Restrictions.in("domainId", intIds));
        	childCriteria.add(Restrictions.eq("isActive", "Y"));
        }
        return (List<User>) hibCriteria.list();
    }

    /**
     * To get user type details  by role
     *
     * @param userRole String which describes the user role
     * @return User Type w.r.t the user role
     */
    @Override
    @Transactional
    public Role getUserTypeByRole(String userRole) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Role.class);
        hibCriteria.add(Restrictions.eq("roleName", userRole));
        return (Role) hibCriteria.uniqueResult();
    }

    /**
     * To delete user
     *
     * @param userIds array of integer userIds that needs to be deleted
     */
    @Override
    @Transactional
    public void deleteUsers(final Integer[] userIds ,Integer userId) throws DataAccessException {
        getHibernateTemplate().execute(new HibernateCallback<Void>()
        {
            @Override
            public Void doInHibernate(Session session)
                    throws HibernateException, SQLException {
                for (int i = 0; i < userIds.length; i++) {
                    Integer userId = userIds[i];
                    User user = (User) session.createCriteria(User.class).add(
                            Restrictions.eq("userId", userId)).add(Restrictions.eq("isActive", "Y")).uniqueResult();

                    if (user != null) {
                        user.setIsActive("N");
                        user.setUpdateDate(new Date());
                        user.setUpdatedBy(userId);
                        session.update(user);
                    }
                    @SuppressWarnings("unchecked")
                    List<CompanyTransMgmt> companyTransMgmt = (List<CompanyTransMgmt>) session.createCriteria(CompanyTransMgmt.class).add(
                            Restrictions.eq("userId", userId)).add(Restrictions.eq("isActive", "Y")).list();
                    if (companyTransMgmt != null && companyTransMgmt.size() > 0) {


                        for (int j = 0; j < companyTransMgmt.size(); j++) {
                            CompanyTransMgmt companyTransMgmtObj = companyTransMgmt
                                    .get(j);
                            companyTransMgmtObj.setIsActive("N");
                            session.update(companyTransMgmtObj);
                        }
                        //companyTransMgmt.setIsActive("N");
                        //session.update(companyTransMgmt);
                    }

                    UserRole userRoles = (UserRole) session.createCriteria(UserRole.class).add(Restrictions.eq("userId", userId)).add(Restrictions.eq("isActive", "Y")).uniqueResult();
                    if (userRoles != null) {
                        userRoles.setIsActive("N");
                        userRoles.setUpdateDate(new Date());
                        userRoles.setUpdatedBy(userId);
                        session.update(userRoles);
                    }

                }
                return null;
            }
        });

    }

    /**
     * To chnage user password
     *
     * @param user User for which password to be changed
     * @return A string which holds "success" if successfully changed password else "failure"
     */
    @Override
    //@Transactional
    public String changePassword(final User user) {
        return getHibernateTemplate().execute(new HibernateCallback<String>()
        {
            @Override
            public String doInHibernate(Session session)
                    throws HibernateException, SQLException {
                User retrievedUser = (User) session.createCriteria(User.class).add(Restrictions.eq("isActive", "Y"))
                        .add(Restrictions.sqlRestriction("lower(trim(user_name)) = ?",
                                user.getUserName().toLowerCase(),
                                new StringType())).uniqueResult();

                if (retrievedUser != null) {
                    retrievedUser.setPassword(user.getPassword());
                    retrievedUser.setUpdateDate(new Date());
                    session.update(retrievedUser);
                    return "success";
                } else {
                    return "failed";
                }
            }
        });
    }

    /**
     * To Get users
     *
     * @param languageId String to filter terms respectively
     * @param colName    column name that has to be sorted
     * @param sortOrder  order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @param companyId  An Integer to  filter the users
     * @param userId     Integer to filter terms respectively
     * @return List of users other than administrator
     */
    @Override
   // @Transactional
    public List<Member> getManageTeamMembers(final String languageId, final String companyIds,
                                             final String colName, final String sortOrder,
                                             final Integer pageNum, final Integer userId, final Integer companyId)
            throws DataAccessException {
        HibernateCallback<List<Member>> callback = new HibernateCallback<List<Member>>()
        {

            @SuppressWarnings("deprecation")
            @Override
            public List<Member> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                int limitFrom = 0;
                int limitTo = 0;
                StringBuffer query = new StringBuffer();
                List<Member> teamMembersList = new ArrayList<Member>();
                query.append(GET_TEAM_MEMBERS);
                if (languageId != null && !languageId.equals("0") && (companyIds == null || companyIds.equals("0"))) {
                    if (!languageId.equalsIgnoreCase("all")) {
                        query.append(" and exists(select 1 from user_languages user_lang ");
                        query.append(" where user_lang.language_id in (" + languageId + ") and user_lang.user_id = usr.user_id) ");
                    }
                }
                if (companyIds != null && !companyIds.equals("0") && languageId.equals("0")) {
                    query.append(" and  usr.company_id in (" + companyIds + ")");
                }
                if (companyIds != null && !companyIds.equals("0") && languageId != null && !languageId.equals("0")) {
                    if (!languageId.equalsIgnoreCase("all")) {
                        query.append(" and  exists(select 1 from user_languages user_lang ");
                        query.append(" where user_lang.language_id in (" + languageId + ") and user_lang.user_id = usr.user_id) ");
                    }
                    query.append(" and  usr.company_id in (" + companyIds + ")");
                } else if (companyId != null) {
                    query.append(" and usr.company_id=" + companyId);
                }

                query.append(" GROUP BY usr.user_id ");

                if (colName != null && colName.equalsIgnoreCase("userName")) {
                    query.append(" order by usr.user_name " + sortOrder);
                } else if (colName != null && colName.equalsIgnoreCase("emailId")) {
                    query.append(" order by usr.email_id " + sortOrder);
                } else if (colName != null && colName.equalsIgnoreCase("totalVotes")) {
                    query.append(" order by total_votes " + sortOrder);
                } else if (colName != null && colName.equalsIgnoreCase("accuracy")) {
                    query.append(" order by accuracy " + sortOrder);
                } else if (colName != null && colName.equalsIgnoreCase("lastMonth")) {
                    query.append(" order by last_month " + sortOrder);
                } else if (colName != null && colName.equalsIgnoreCase("usrCompany")) {
                    query.append(" order by company.company_name " + sortOrder);
                } else if (colName != null && colName.equalsIgnoreCase("usrRole")) {
                    query.append(" order by rl.role_name " + sortOrder);
                } else if (colName != null && colName.equalsIgnoreCase("badging")) {
                    query.append(" order by badging " + sortOrder);
                } else if (colName != null && colName.equalsIgnoreCase("dateOfCreation")) {
                    query.append(" order by usr.create_time " + sortOrder);
                }

                if (pageNum != 0) {// && !languageId.equalsIgnoreCase("all")) {
                    limitFrom = (pageNum - 1) * 10;
                    limitTo = (pageNum) * 10;
                    query.append(" limit " + limitFrom + " , " + limitTo);
                }
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                Object[] termsData = hibQuery.list().toArray();
                if ((termsData != null) && termsData.length > 0) {
                    for (int i = 0; i < termsData.length; i++) {
                        Object[] membersDataVal = (Object[]) termsData[i];
                        Member teamMember = new Member();
                        teamMember.setUserId((Integer) membersDataVal[0]);
                        teamMember.setUserName((String) membersDataVal[1]);
                        teamMember.setFirstName((String) membersDataVal[2]);
                        teamMember.setLastName((String) membersDataVal[3]);
                        teamMember.setEmailId((String) membersDataVal[4]);
                        teamMember.setCreateDate((String) membersDataVal[5]);
                        teamMember.setTotalVotes((BigInteger) membersDataVal[6]);
                        teamMember.setAccuracy((BigDecimal) membersDataVal[7]);
                        teamMember.setLastMonthVotes((BigInteger) membersDataVal[8]);
                        teamMember.setCompanyName((String) membersDataVal[9]);
                        teamMember.setUserRole((String) membersDataVal[10]);
                        teamMember.setBadgingRate((BigInteger) membersDataVal[11]);
                        teamMembersList.add(teamMember);
                    }
                }

                logger.debug(" Total manage Team members :"
                        + teamMembersList.size());
                return teamMembersList;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To update the user.
     *
     * @param user User that is to be updated
     * @return If user updated returns success else failure
     */
    public String updateUser(User user) {
        String status = null;
        if (user == null) {
            return status;
        }
        getHibernateTemplate().update(user);
        logger.debug("Updated  user successfully :" + user.getUserId());

        return status;

    }

    /**
     * To get the user
     *
     * @param userId Integer for which User is to be filtered
     * @return User w.r.t the userId
     */
    public User getUser(Integer userId) {
        if (userId == null) {
            return null;
        }
        User user = getHibernateTemplate().get(User.class, userId);

        logger.debug("got the user :" + user);
        return user;

    }

    /**
     * To save Or Update UserLanguage
     *
     * @param userLanguages that needs to be saved/updated
     * @return UserLanguages that is saved/updated
     */
    public UserLanguages saveOrUpdateUserLanguage(UserLanguages userLanguages) {
        if (userLanguages == null) {
            return userLanguages;
        }
        getHibernateTemplate().saveOrUpdate(userLanguages);
        return userLanguages;
    }

    /**
     * To delete UserLanguages
     *
     * @param userId Integer to be filtered
     * @return If successfully deleted it returns true else false
     */
    @Transactional
    public Boolean deleteUserLanguages(Integer userId) {
        Boolean status = false;
        if (userId == null) {
            return status;
        }
         Session session = getHibernateSession();
        String delQuery = DELETE_USER_LANGUAGE + userId;
        Query query = session.createQuery(delQuery);
        int count = query.executeUpdate();
        if (count > 0)
            status = true;

        logger.debug("Deleted  User Languages");
        return status;
    }

    /**
     * get User To Export
     *
     * @param roleIds     Set collection which includes role id's to be filtered
     * @param languageIds Set collection which includes language id's to be filtered
     * @return List of users to be exported w.r.t the role id's and language id's
     */
    @Transactional
    public List<User> getUserToExport(Set<Integer> roleIds, Set<Integer> languageIds, Set<Company> companies) {
        if (roleIds.isEmpty()) {
            return null;
        }
         Session session = getHibernateSession();
        Criteria hibCriteria = null;
        hibCriteria = session.createCriteria(User.class).add(Restrictions.eq("isActive", "Y"));
        if (!languageIds.isEmpty()) {
            Criteria childCriteria = hibCriteria.createCriteria("userLanguages", Criteria.LEFT_JOIN);
            Criteria childCriteria1 = childCriteria.createCriteria("languages", Criteria.LEFT_JOIN);
            childCriteria1.add(Restrictions.in("languageId", languageIds));
        }
        if (!roleIds.isEmpty()) {
            Criteria childCriteria = hibCriteria.createCriteria("userRole", Criteria.LEFT_JOIN);
            Criteria childCriteria1 = childCriteria.createCriteria("role", Criteria.LEFT_JOIN);
            childCriteria1.add(Restrictions.in("roleId", roleIds));
        }
        //if (companies != null) {
        if(!companies.isEmpty()) {
            //hibCriteria.add(Restrictions.eq("company", company));
        	hibCriteria.add(Restrictions.in("company", companies));
        }
        return (List<User>) hibCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    /**
     * To verify whether email exists or not in database
     *
     * @param emailId String to be filtered
     * @return Returns true if email exists else it returns false
     */
    public boolean ifEmailExists(String emailId) {

        /**
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("emailId", emailId));
        criteria.add(Restrictions.eq("isActive", "Y"));
        criteria.setProjection(Projections.count("userId"));
        Integer countOfUser = (Integer) criteria.uniqueResult();

        if (countOfUser > 0) {
            return true;
        }
        */
        return false;
    }


    /**
     * To get the user details
     *
     * @param domainId Integer for which User is to be filtered
     * @return List of User ids w.r.t the domainId
     */
    @Transactional
    public List<Integer> getUserByDomain(Integer domainId) {
        List<Integer> userList = new ArrayList<Integer>();
        if (domainId == null) {
            return null;
        }
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(User.class);
        //criteria.add(Restrictions.eq("userTypeId",2));
        criteria.add(Restrictions.eq("userDomainId", domainId));
        criteria.add(Restrictions.eq("isActive", "Y"));
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("userId"));
        criteria.setProjection(proList);
        List list = criteria.list();
        if (list != null) {

            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object row = (Object) it.next();
                Integer id = (Integer) row;
                userList.add(id);

            }
        }
        logger.debug("Got the Users List :" + userList);
        return userList;


    }

    /**
     * To get list of user type details
     *
     * @param roleId Integer array to get role details
     * @return List of Roles w.r.t roleId
     */
    @Override
    @Transactional
    public List<Role> getAllUserTypeDetails(Integer roleId[]) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Role.class);
        for (int i = 0; i < roleId.length; i++) {
            if (roleId[i] == 2) {
                hibCriteria.add(Restrictions.not(Restrictions.eq("roleId", 1)));
                hibCriteria.add(Restrictions.not(Restrictions.eq("roleId", 4)));
                //hibCriteria.add(Restrictions.not(Restrictions.eq("roleId", 2)));

            }
        }
        hibCriteria.add(Restrictions.not(Restrictions.eq("isActive", "N")));
        return (List<Role>) hibCriteria.list();
    }

    /**
     * To get list of user  type menu details
     *
     * @param roleId Integer array to get Role MenuMgmt
     * @return List of Role Menu Management w.r.t roleId
     */
    @Override
    @Transactional
    public List<RoleMenuMgmt> getRoleMenuDetails(Integer roleId[]) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(RoleMenuMgmt.class);
        hibCriteria.add(Restrictions.in("role.roleId", roleId));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        return (List<RoleMenuMgmt>) hibCriteria.list();
    }

    /**
     * To get list of user  type sub menu details
     *
     * @param roleId Integer array to get RoleSubMenuManagement
     * @return List of Role Sub Menu Management w.r.t roleId
     */
    @Override
    @Transactional
    public List<RoleSubmenuMgmt> getRoleSubMenuDetails(Integer roleId[]) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(RoleSubmenuMgmt.class);
        hibCriteria.add(Restrictions.in("role.roleId", roleId));
        return (List<RoleSubmenuMgmt>) hibCriteria.list();


    }


    /**
     * To get user details by company id
     *
     * @param companyId Integer to filter userDetails
     * @return List of user ids w.r.t the companyIds
     */
    @Override
    @Transactional
    public List<Integer> getUserByCompanyId(Integer companyId) {
        List<Integer> userList = new ArrayList<Integer>();
        if (companyId == null) {
            return null;
        }
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("company.companyId", companyId));
        criteria.add(Restrictions.eq("isActive", "Y"));
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("userId"));
        criteria.setProjection(proList);
        List list = criteria.list();
        if (list != null) {

            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object row = (Object) it.next();
                Integer id = (Integer) row;
                userList.add(id);

            }
        }
        logger.debug("Got the Users List :" + userList);
        return userList;


    }

    /**
     * To save companyTransMgmt
     *
     * @param companyTransMgmt CompanyTransMgmt that has to be saved
     * @return Returns "success" if it is registered else it returns "failure"
     */
    @Override
    public String companyTransMgmt(CompanyTransMgmt companyTransMgmt) {
        String status;
        if (companyTransMgmt == null) {
            status = "failure";
            return status;
        }
        companyTransMgmt.setIsActive("Y");
        getHibernateTemplate().save(companyTransMgmt);
        logger.debug("User registered successfully :" + companyTransMgmt.getUserId());
        status = "success";
        return status;

    }

    /**
     * To  save or update user roles
     *
     * @param role UserRole that has to be saved/updated
     * @return Returns UserRole obj's
     */
    @Override
    public UserRole saveOrUpdateUserRole(UserRole role) {
        if (role == null) {
            return role;
        }
        getHibernateTemplate().saveOrUpdate(role);
        return role;
    }

    /**
     * To get previleges List
     *
     * @return Returns List of PrivilegesByMenu
     */
    @Override
    @Transactional
    public List<Privileges> getPrevilegeList() throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Privileges.class);
        hibCriteria.add(Restrictions.not(Restrictions.eq("isActive", "N")));
        return (List<Privileges>) hibCriteria.list();
    }

    /**
     * To get role
     *
     * @param roleTypeId Integer to filter user roles
     * @return role  w.r.t roleTypeId
     */
    @Override
    public Role getRole(Integer roleTypeId) {
        if (roleTypeId == null) {
            return null;
        }
        return (Role) getHibernateTemplate().get(Role.class, roleTypeId);

    }

    /**
     * To get previleges
     *
     * @param fileIdArray Integer to filter user previleges
     * @return Privileges  obj's w.r.t fileIdArray
     */
    @Override
    public Privileges getPrevileges(Integer fileIdArray) throws DataAccessException {
        if (fileIdArray == null) {
            return null;
        }
        return (Privileges) getHibernateTemplate().get(Privileges.class, fileIdArray);

    }

    /**
     * To save Role Previleges
     *
     * @param rolePrivilege RolePrivileges that has to be saved
     */
    @Override
    public void saveRolePrevileges(RolePrivileges rolePrivilege) {
        if (rolePrivilege == null) {
            return;
        }

        rolePrivilege.setIsActive("Y");
        getHibernateTemplate().save(rolePrivilege);

    }

    /**
     * To delete Role Previleges
     *
     * @param roleId Integer to filter user role previleges
     */
    @Override
    @Transactional
    public void deleteRolePrivileges(Integer roleId) throws DataAccessException {
        if (roleId == null) {
            return;
        }
         Session session = getHibernateSession();
        StringBuffer query = new StringBuffer();
        query.append(DELETE_USER_ROLE_PRIVILEGES);
        query.append(roleId);
        Query hibQuery = session.createQuery(query.toString());
        hibQuery.executeUpdate();

        logger.debug("Deleted  User Previleges");

    }

    /**
     * To get  previleges by roleId
     *
     * @param roleId Integer to identify user role
     * @return List of rolePrivileges
     */
    @Override
    @Transactional
    public List<RolePrivileges> getPrevilegesByRole(Integer roleId) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(RolePrivileges.class);
        hibCriteria.add(Restrictions.eq("role.roleId", roleId));
        return (List<RolePrivileges>) hibCriteria.list();


    }

    /**
     * To delete role menus by roleId
     *
     * @param roleId Integer to identify user role
     */
    @Override
    @Transactional
    public void deleteRoleMenus(Integer roleId) throws DataAccessException {
        if (roleId == null) {
            return;
        }
         Session session = getHibernateSession();
        StringBuffer query = new StringBuffer();
        query.append(DELETE_USER_ROLE_MENUS);
        query.append(roleId);
        Query hibQuery = session.createQuery(query.toString());
        hibQuery.executeUpdate();

        logger.debug("Deleted  User Previleges");


    }

    /**
     * To delete role sub menus by roleId
     *
     * @param roleId Integer to identify user role
     */
    @Override
    @Transactional
    public void deleteRoleSubmenus(Integer roleId) throws DataAccessException {
        if (roleId == null) {
            return;
        }
         Session session = getHibernateSession();
        StringBuffer query = new StringBuffer();
        query.append(DELETE_USER_ROLE_SUBMENUS);
        query.append(roleId);
        Query hibQuery = session.createQuery(query.toString());
        hibQuery.executeUpdate();

        logger.debug("Deleted  User Previleges");

    }

    /**
     * To verify whether user role  exists or not in database
     *
     * @return Returns true if user role exists else it returns false
     */
    @Override
    @Transactional
    public boolean ifRoleExists(String roleName) throws DataAccessException {
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(Role.class);
        criteria.add(Restrictions.eq("roleName", roleName));
        criteria.add(Restrictions.or(Restrictions.eq("isActive", "Y"), Restrictions.isNull("isActive")));
        criteria.setProjection(Projections.count("roleId"));
        Integer countOfRoles = (Integer) criteria.uniqueResult();
        if (countOfRoles > 0) {
            return true;
        }

        return false;

    }

    /**
     * To save role menu mgmt
     *
     * @param roleMenuMgmt RoleMenuMgmt that has to be saved
     */
    @Override
    public void saveRoleMenuMgmt(RoleMenuMgmt roleMenuMgmt) {
        if (roleMenuMgmt == null) {
            return;
        }

        roleMenuMgmt.setIsActive("Y");
        getHibernateTemplate().save(roleMenuMgmt);
    }

    /**
     * To save role sub menu mgmt
     *
     * @param roleSubmenuMgmt RoleSubmenuMgmt that has to be saved
     */
    @Override
    public void saveRoleSubMenuMgmt(RoleSubmenuMgmt roleSubmenuMgmt) {
        if (roleSubmenuMgmt == null) {
            return;
        }
        roleSubmenuMgmt.setIsActive("Y");
        getHibernateTemplate().save(roleSubmenuMgmt);
    }

    /**
     * To save Or Update Role
     *
     * @param role Role that has to be saved/updated
     */
    @Override
    public void saveOrUpdateRole(Role role) throws DataAccessException {
        if (role == null) {
            return;
        }
        getHibernateTemplate().saveOrUpdate(role);
    }

    /**
     * To get user details by role
     *
     * @param roleId Integer to filter user role Details
     * @return List of user ids w.r.t the roleId
     */
    @Override
    @Transactional
    public List<Integer> getUserByRole(Integer roleId) throws DataAccessException {
        List<Integer> userList = new ArrayList<Integer>();
        if (roleId == null) {
            return null;
        }
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(UserRole.class);
        //criteria.add(Restrictions.eq("userTypeId",2));
        criteria.add(Restrictions.eq("role.roleId", roleId));
        criteria.add(Restrictions.eq("isActive", "Y"));
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("userId"));
        criteria.setProjection(proList);
        List list = criteria.list();
        if (list != null) {

            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object row = (Object) it.next();
                Integer id = (Integer) row;
                userList.add(id);

            }
        }
        logger.debug("Got the Users List :" + userList);
        return userList;


    }

    /**
     * To get company tranMgmt users
     *
     * @param userId Integer to filter user  Details
     * @return set of companyTransMgmt  w.r.t the userId
     */

    @Override
    @Transactional
    public List<CompanyTransMgmt> getCompanyTransMgmtUsers(Integer userId) {
        if (userId == null) {
            return null;
        }
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(CompanyTransMgmt.class);
        hibCriteria.add(Restrictions.eq("userId", userId));
        return (List<CompanyTransMgmt>) hibCriteria.list();

    }

    /**
     * To save Or Update CompanyTransMgmt
     *
     * @param userCompanyTransMgmt CompanyTransMgmt that has to be saved/updated
     * @return CompanyTransMgmt that is saved/updated
     */
    @Override
    public CompanyTransMgmt saveOrUpdateCompanyTransMgmt(CompanyTransMgmt userCompanyTransMgmt) {
        if (userCompanyTransMgmt == null) {
            return userCompanyTransMgmt;
        }
        getHibernateTemplate().saveOrUpdate(userCompanyTransMgmt);
        return userCompanyTransMgmt;
    }

    /**
     * To delete companyTransMgmt users
     *
     * @param userId array of integer userIds that needs to be deleted
     * @return Returns true if user exists else it returns false
     */
    @Override
    @Transactional
    public Boolean deleteCompanyTransUsers(Integer userId) {
        Boolean status = false;
        if (userId == null) {
            return status;
        }
         Session session = getHibernateSession();
        String delQuery = DELETE_COMPANY_TRANSMGMT_USERS + userId;
        Query query = session.createQuery(delQuery);
        int count = query.executeUpdate();
        if (count > 0)
            status = true;

        logger.debug("Deleted  User Languages");
        return status;
    }

    /**
     * To get user details list by companyId
     *
     * @param companyId Integer to filter userDetails
     * @return List of user  w.r.t the companyId
     */


    @Override
    @Transactional
    public List<User> getUserListByCompanyId(Integer companyId) {
        if (companyId == null) {
            return null;
        }
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("company.companyId", companyId));
        criteria.add(Restrictions.eq("isActive", "Y"));
        return (List<User>) criteria.list();


    }

    /**
     * To get user details list by company and role
     *
     * @param companyId Integer to filter userDetails
     * @param roleIds   Set collection which includes role id's to be filtered
     * @return List of user obj's w.r.t the companyId
     */

    @Override
    @Transactional
    public List<User> getUserListByCompanyAndRole(Integer companyId, Set<Integer> roleIds) {
        if (companyId == null) {
            return null;
        }
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(User.class);
        hibCriteria.add(Restrictions.eq("company.companyId", companyId));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        Criteria childCriteria = hibCriteria.createCriteria("userRole", Criteria.LEFT_JOIN);
        Criteria childCriteria1 = childCriteria.createCriteria("role", Criteria.LEFT_JOIN);
        childCriteria1.add(Restrictions.in("roleId", roleIds));
        return (List<User>) hibCriteria.list();


    }

    /**
     * To get user details list by role
     *
     * @param roleId Integer to filter userDetails
     * @return List of user  w.r.t the roleId
     */
    @Override
    @Transactional
    public List<User> getUserListByRole(Integer roleId) {
        if (roleId == null) {
            return null;
        }
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(User.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        Criteria childCriteria = hibCriteria.createCriteria("userRole", Criteria.LEFT_JOIN);
        Criteria childCriteria1 = childCriteria.createCriteria("role", Criteria.LEFT_JOIN);
        childCriteria1.add(Restrictions.eq("roleId", roleId));
        return (List<User>) hibCriteria.list();


    }

    /**
     * To save or update the gs credintials
     *
     * @param company Company to be saved
     * @return Returns "success" if it is registered else it returns "failure"
     */
    @Override
    public String saveOrUpdateGSCredintials(Company company) {
        String status = null;
        if (company == null) {
            return status;
        }
        getHibernateTemplate().saveOrUpdate(company);
        logger.debug("Updated  user successfully :");

        return status;

    }

    /**
     * To get GS credintials
     *
     * @param companyId Integer to get the credintials details
     * @return company w.r.t companyId
     */
    @Override
    public Company getGSCredintails(Integer companyId) {
        if (companyId == null) {
            return null;
        }
        return (Company) getHibernateTemplate().get(Company.class, companyId);

    }

    /**
     * To get user role list by menu
     *
     * @param roleId Integer to filter user roles
     * @return List of RoleMenuMgmt w.r.t the menuId
     */
    @Override
    @Transactional
    public List<RoleMenuMgmt> getUserRoleListByMenu(Integer menuId) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(RoleMenuMgmt.class);
        hibCriteria.add(Restrictions.eq("userMenu.menuId", menuId));
        return (List<RoleMenuMgmt>) hibCriteria.list();
    }

    /**
     * To get menu Id by menuName
     *
     * @param menuName string to get Menu
     * @return Menu w.r.t the menuName
     */
    @Override
    @Transactional
    public Menu getMenuIdByLabel(String menuName) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Menu.class);
        hibCriteria.add(Restrictions.eq("menuName", menuName));
        return (Menu) hibCriteria.uniqueResult();

    }

    /**
     * To get Role Id by roleName
     *
     * @param roleName string to get Role
     * @return Role w.r.t the roleName
     */
    @Override
    @Transactional
    public Role getRoleIdByLabel(String roleName) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Role.class);
        hibCriteria.add(Restrictions.eq("roleName", roleName));
        return (Role) hibCriteria.uniqueResult();
    }

    /**
     * To get role id  list by label
     *
     * @param roleNames String  to filter user role ids
     * @return set of role ids  w.r.t the roleNames
     */
    @Override
    @Transactional
    public Set<Integer> getRoleIdListByLabel(Set<String> roleNames) {
        Set<Integer> roleIdList = new HashSet<Integer>();
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Role.class);
        hibCriteria.add(Restrictions.in("roleName", roleNames));
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("roleId"));
        hibCriteria.setProjection(proList);
        List list = hibCriteria.list();
        Set set = new HashSet(list);
        if (set != null) {

            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object row = (Object) it.next();
                Integer id = (Integer) row;
                roleIdList.add(id);

            }
        }
        logger.debug("Got the Users List :" + roleIdList);
        return roleIdList;


    }

    @Override
    @Transactional
    public List<User> getSuperAdmins() {
        List<User> usersList = null;
         Session session = getHibernateSession();
        Criteria hibCriteria = null;

        hibCriteria = session.createCriteria(User.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        Criteria childCriteria = hibCriteria.createCriteria("userRole", Criteria.LEFT_JOIN);
        Criteria childCriteria1 = childCriteria.createCriteria("role", Criteria.LEFT_JOIN);
        childCriteria1.add(Restrictions.eq("roleId", 1));
        usersList = (List<User>) hibCriteria.list();


        return usersList;
    }

    @Override
    @Transactional
    public List<User> getCompanyUsersByRole(Integer companyId, Integer roleId) {
        List<User> usersList = null;
         Session session = getHibernateSession();
        Criteria hibCriteria = null;

        hibCriteria = session.createCriteria(User.class);
        hibCriteria.add(Restrictions.eq("isActive", "Y"));
        Criteria childCriteria1 = hibCriteria.createCriteria("companyTransMgmt", Criteria.LEFT_JOIN)
                .add(Restrictions.eq("companyId", companyId)).add(Restrictions.eq("isActive", "Y"));
        Criteria childCriteria2 = hibCriteria.createCriteria("userRole", Criteria.LEFT_JOIN);
        Criteria childCriteria3 = childCriteria2.createCriteria("role", Criteria.LEFT_JOIN);
        childCriteria3.add(Restrictions.eq("roleId", roleId));
        usersList = (List<User>) hibCriteria.list();
        return usersList;
    }
}

  

		
	


