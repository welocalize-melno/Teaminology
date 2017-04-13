package com.teaminology.hp.dao.hibernate;

import java.math.BigInteger;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.teaminology.hp.bo.Attributes;
import com.teaminology.hp.bo.CompanyTransMgmt;
import com.teaminology.hp.bo.DeprecatedTermInformation;
import com.teaminology.hp.bo.FileInfo;
import com.teaminology.hp.bo.FileUploadStatus;
import com.teaminology.hp.bo.GlobalsightTermInfo;
import com.teaminology.hp.bo.GlobalsightTerms;
import com.teaminology.hp.bo.TMProperties;
import com.teaminology.hp.bo.Tag;
import com.teaminology.hp.bo.TermInformation;
import com.teaminology.hp.bo.TermTranslation;
import com.teaminology.hp.bo.TermUpdateDetails;
import com.teaminology.hp.bo.TermVoteMaster;
import com.teaminology.hp.bo.TermVoteUserDetails;
import com.teaminology.hp.bo.TmProfileInfo;
import com.teaminology.hp.bo.User;
import com.teaminology.hp.bo.UserLanguages;
import com.teaminology.hp.bo.lookup.Language;
import com.teaminology.hp.dao.HibernateDAO;
import com.teaminology.hp.dao.TermDetailsDAO;
import com.teaminology.hp.data.GSJobObject;
import com.teaminology.hp.data.Invite;
import com.teaminology.hp.data.Member;
import com.teaminology.hp.data.PollTerms;
import com.teaminology.hp.data.QueryAppender;
import com.teaminology.hp.data.SuggestedTermDetails;
import com.teaminology.hp.data.TMProfileTerms;
import com.teaminology.hp.data.TermVoteDetails;
import com.teaminology.hp.data.TermVotingTo;
import com.teaminology.hp.data.Terms;


/**
 * Contains DAO methods to handle terms data.
 *
 * @author sarvaniC
 */

public class TermDetailsDAOImpl extends HibernateDAO implements  TermDetailsDAO
{
    private static Logger logger = Logger.getLogger(TermDetailsDAOImpl.class);
    protected static final String LS = System.getProperty("line.separator");
    private static final Integer APPROVED_STATUS_ID = 2;
    private static final String LIKE_PERCENT = "%";
    private static final String LIKE_UNDERSCORE = "_";

    
    private final static String GET_LEADER_BOARD_MEMBERS =
            "SELECT usr.user_id, usr.user_name, usr.first_name, usr.last_name, usr.photo_path, usr.term_request_count, COUNT(tdtls.term_vote_usr_dtls_id) AS votes"
                    + " FROM user usr"
                    + " LEFT JOIN term_vote_user_details tdtls ON (tdtls.vote_invite_status ='Y' AND tdtls.is_active = 'Y' AND usr.user_id = tdtls.user_id)"
                    + " WHERE usr.is_active ='Y' ";
    
    private final static String GET_LEADER_BOARD_MEMBERS_BY_LANGUAGE = " SELECT usr.user_id, usr.user_name, usr.first_name, usr.last_name, usr.photo_path, " 
    				+ " usr.term_request_count, COUNT(tdtls.term_vote_usr_dtls_id) AS votes FROM user usr " 
    		        + " INNER JOIN user_languages ul on (ul.user_id = usr.user_id) "
    				+ " INNER JOIN term_vote_user_details tdtls ON  (tdtls.vote_invite_status ='Y' AND tdtls.is_active = 'Y' " 
    				+ " AND usr.user_id = tdtls.user_id) " 
    				+ " INNER JOIN  term_information ti on  (tdtls.term_id = ti.term_id)   WHERE usr.is_active ='Y' ";
    
    
    private final static String GET_VOTE_RESULTS=
    		"select ti.term_id, ti.term_being_polled as source, ti.suggested_term  as  top_suggestion, " 
              +" tt.suggested_term as alternate_term,  ll.language_label as language , tt.vote as no_of_votes, "
              +" case "
              +" when ti.suggested_term is null then 'FALSE' "
              +" when ti.suggested_term = tt.suggested_term then 'TRUE' "
              +" else 'FALSE' "
              +" end as is_top "
              +" from term_information ti left outer join term_translation tt " 
              +" on ti.term_id = tt.term_id  left outer  join (term_vote_master tm) on "
              +" tm.term_id=tt.term_id " 
              +"left outer join language_lookup ll  on ll.language_id=tt.suggested_term_lang_id ";
    

    private final static String GET_TERMS_IN_GLOSSARY =
            "SELECT YEAR(create_date), COUNT(term_id)"
                    + " FROM term_information"
                    + " WHERE is_active ='Y' AND YEAR(create_date) >= YEAR(ADDDATE(CURDATE(), INTERVAL - 4 YEAR)) ";

    private final static String GET_TOTAL_TERMS_IN_GLOSSARY = "SELECT COUNT(*) FROM term_information ti WHERE ti.is_active = 'Y' ";

    private final static String GET_DEBATED_TERMS =
            "SELECT YEAR(invited_date), COUNT(tvm.term_id)"
                    + " FROM term_vote_master tvm"
                    + " LEFT JOIN term_information ti ON ti.is_active='Y' AND tvm.term_id = ti.term_id"
                    + " WHERE tvm.is_active ='Y' AND YEAR(invited_date) >= YEAR(ADDDATE(CURDATE(), INTERVAL - 4 YEAR)) ";


    private final static String GET_TOTAL_DEBATED_TERMS =
            "SELECT COUNT(*)"
                    + " FROM term_vote_master tvm"
                    + " LEFT JOIN term_information ti ON ti.is_active='Y' AND tvm.term_id = ti.term_id"
                    + " WHERE tvm.is_active = 'Y' AND tvm.voting_expired_date >= CURDATE() ";

    private final static String GET_TOP_TERMS =
            "SELECT suggested_term "
                    + "FROM term_information ti "
                    + "INNER JOIN term_vote_master tvm ON tvm.term_id = ti.term_id AND tvm.is_active='Y' AND tvm.voting_expired_date >= CURDATE() "
                    + "WHERE ti.is_active ='Y' AND ti.suggested_term IS NOT NULL ";

    private final static String GET_EXPIRED_POLL_TERMS =
            "select ti.term_id,ti.term_being_polled, ti.suggested_term, lang.language_label, "
                    + "date_format(tvm1.voting_expired_date, '%m/%d/%Y') as expired_date, "
                    + "sum(tt.vote) as all_votes, (select count(tvud.term_id) from term_vote_user_details tvud "
                    + "where (tvud.vote_invite_status ='Y'  or tvud.vote_invite_status is null) and (tvud.is_active = 'Y' or tvud.is_active is null) and tvud.term_id=ti.term_id) as invites, "
                    + "(select count(dd.term_id) from deprecated_term_info dd where dd.term_id=ti.term_id and dd.is_active='Y') as is_deprecate "
                    + "from term_information ti "
                    + "left join (language_lookup lang) on (ti.suggested_term_lang_id =lang.language_id) "
                    + "left join(term_vote_master tvm1) on ( ti.term_id = tvm1.term_id) "
                    + "left join (term_translation tt) on (ti.term_id = tt.term_id) "
                    + "where exists (select 1 from term_vote_master tvm "
                    + "where ti.term_id = tvm.term_id and tvm.voting_expired_date < curdate()) and ti.is_active='Y' and ti.term_status_id !=:param0 ";

    private final static String GET_SUGGESTED_TERMS =
            "select tt.suggested_term,tt.vote, usr.user_name, tt.term_translation_id, tt.is_updated "
                    + "from term_translation tt "
                    + "left join (term_vote_user_details tvud inner join user usr on usr.user_id = tvud.user_id and usr.is_active='Y') "
                    + "on (tvud.term_translation_id = tt.term_translation_id ) "
                    + "where (tt.is_active='Y' or tt.is_active is null) and tt.term_id ";

    private final static String GET_MONTHLY_TERM_DETAILS =
            "select date_format(create_date,'%b'), count(term_id) "
                    + "from term_information ti "
                    + "where  create_date >= (adddate(last_day(curdate()), interval -5 month)) and ti.is_active ='Y' ";

    private final static String GET_MONTHLY_DEBATED_DETAILS =
            "select date_format(invited_date,'%b'), count(tvm.term_id) "
                    + "from term_vote_master tvm left join (term_information ti) on (tvm.term_id = ti.term_id) "
                    + "where  invited_date >= (adddate(last_day(curdate()), interval -5 month)) and ti.is_active ='Y' and tvm.is_active ='Y' ";

    private final static String GET_QUARTERLY_TERM_DETAILS =
            "select concat('Q',quarter(create_date),'-', year(create_date)) quarter, count(term_id) cnt  "
                    + "from term_information ti "
                    + "where ti.is_active ='Y' and ti.create_date is not null and ti.create_date >= (adddate(curdate(), interval -12 month)) ";


    private final static String GET_QUARTERLY_DEBATED_TERM_DETAILS =
            "select concat('Q',quarter(invited_date),'-', year(invited_date)) quarter, count(tvm.term_id) cnt  "
                    + "from term_vote_master tvm left join (term_information ti) on (tvm.term_id = ti.term_id) "
                    + "where ti.is_active ='Y' and tvm.is_active ='Y'  and invited_date is not null and  invited_date >= (adddate(curdate(), interval -12 month)) ";

    private final static String GET_USER_POLL_TERMS =
            " select ti.term_id, ti.term_being_polled,  ti.term_status_id, date_format(tvm.voting_expired_date, '%m/%d/%Y') as expired_date "
                    + "from term_information ti "
                    + "left join (term_vote_master tvm) on ( ti.term_id = tvm.term_id) "
                    + " where exists (select * from term_vote_user_details tvud where ti.term_id = tvud.term_id and tvm.voting_expired_date > curdate() and "
                    + " tvud.vote_invite_status is null and tvud.user_id = ";

    private final static String GET_MANAGE_POLL_TERMS = LS
            + " select"
            + LS
            + " ti.term_id,ti.term_being_polled, ti.suggested_term,pos.part_of_speech,cat.category,lang.language_label,status.status,domain.domain,company.company_name,"
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
            + LS
            + " left join (domain_lookup domain) on (ti.domain_id=domain.domain_id)"
            + LS
            + " left join (company_lookup company) on (ti.company_id=company.company_id)"
            + LS + " where ti.is_active='Y'";

    private final static String GET_SEARCH_MANAGE_POLL_TERMS = LS
            + " select"
            + LS
            + " ti.term_id,ti.term_being_polled, ti.suggested_term,pos.part_of_speech,cat.category,lang.language_label,status.status,domain.domain,company.company_name, "
            + LS
            + " date_format(tvm.voting_expired_date, '%m/%d/%Y') as expired_date ,"
            + LS
            + " sum(tt.vote) as all_votes, (select count(tvud.term_id) from term_vote_user_details tvud "
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
            + LS
            + " left join (domain_lookup domain) on (ti.domain_id=domain.domain_id)"
            + LS
            + " left join (company_lookup company) on (ti.company_id=company.company_id)"
            + LS + " where ti.is_active ='Y'";

    private final static String GET_USER_VOTED_TERMS = LS
            + "select ti.term_id,ti.term_being_polled, ti.suggested_term, ti.term_status_id ,"
            + LS
            + " date_format(tvud.voting_date, '%m/%d/%Y') as voting_date , sum(tt.vote) as all_votes,"
            + LS
            + "(select count(tvud.term_id) from term_vote_user_details tvud  "
            + LS
            + " where (tvud.vote_invite_status ='Y'  or tvud.vote_invite_status is null) and (tvud.is_active = 'Y' or tvud.is_active is null) and tvud.term_id=ti.term_id) as invites from term_information ti  "
            + LS
            + "left join(term_vote_user_details tvud) on (tvud.term_id = ti.term_id)"
            + LS
            + " left join (term_translation tt) on (ti.term_id = tt.term_id) "
            + LS
            + "where tvud.vote_invite_status ='Y' and (ti.is_active ='Y' or ti.is_active is null) and tvud.user_id = ";

    private final static String GET_TOP_REG_LANGS = LS
            + " select lang.language_id,lang.language_label,lang.language_code "
            + LS
            + " from user_languages ul left join(language_lookup lang ) on (ul.language_id = lang.language_id ) "
            + LS
            + " left join(user usr) on (usr.user_id = ul.user_id) "
            + LS
            + " where (lang.is_active = 'Y' || lang.is_active is null) and usr.is_active = 'Y'  ";


    private final static String GET_MONTHLY_VOTES_PER_LANG = LS
            + "select count(term_vote_usr_dtls_id)"
            + LS
            + " from term_vote_user_details"
            + LS
            + " where month(voting_date) =  month(curdate())  and  year(voting_date) = year(curdate()) and  (is_active = 'Y' or is_active is null)"
            + LS
            + "  and term_id "
            + LS
            + " in (select term_id from term_information where suggested_term_lang_id = :languageId ";

    private final static String GET_TERM_VOTING_STATUS = LS
            + " select  "
            + LS
            + " tvd.term_id,usr.user_name,tvd.vote_invite_status,tvd.voting_date,tvd.comments,tt.suggested_term from "
            + LS
            + " term_vote_user_details tvd  left join (term_translation tt ) on (tvd.term_translation_id=tt.term_translation_id)  "
            + LS
            + " left join (user usr ) on (tvd.user_id=usr.user_id ) where tvd.term_id ";


    private final static String GET_IMPORT_FILE_INFO = LS + "select  distinct(file.file_info_id) as d_file_info_id ,file.file_id,file.file_name,slang.language_label as source_lang,tlang.language_label"
            + LS + "as target_lang,file.status,file.job_id,file.jobname,file.task_id,file.taskname,file.export_log_url from file_info file"
            + LS + "left join (language_lookup slang ) on (file.source_lang = slang.language_id)"
            + LS + "left join (globalsight_term_info gsinfo ) on (file.file_info_id=gsinfo.file_info_id )"
            + LS + "left join (language_lookup tlang ) on (file.target_lang = tlang.language_id)  where file.is_active='Y'  and gsinfo.is_active='Y'";


    private final static String GET_TM_PROFILE_TERMS = LS
            + " select"
            + LS
            + " tm.tm_profile_info_id,tm.source, tm.target,tm.industry_domain,tm.product_line,tm.target_lang,tm.content_type,tm.company "
            + LS
            + " from tm_profile_info tm ";

    private final static String GET_TOTAL_TERMS_IN_TMPROFILE = LS
            + "select count(*) from tm_profile_info tm where is_active = 'Y' ";

    private final static String GET_GLOBAL_SIGHT_TERM_INFO = LS
            + "select  gt.globalsight_term_info_id,gt.source_segment,gt.target_segment,slang.language_label as source_lang,"
            + LS
            + "tlang.language_label as target_lang ,gt.origin,fi.file_id,gt.transunit_id,ti.term_id,fi.job_id,fi.jobname,fi.task_id,fi.taskname,"
            + LS
            + " (select sum(tt1.vote) from term_translation tt1 where tt1.term_id=gt.term_id) as all_votes, (select count(tvud.term_id) from term_vote_user_details tvud "
            + LS
            + " where (tvud.vote_invite_status ='Y'  or tvud.vote_invite_status is null) and  (tvud.is_active = 'Y' or tvud.is_active is null)"
            + LS
            + " and tvud.term_id=gt.term_id) as invites "
            + LS
            + " from globalsight_term_info gt"
            + LS
            + " left join (file_info fi) on (fi.file_info_id= gt.file_info_id) "
            + LS
            + "left join (language_lookup slang ) on (fi.source_lang = slang.language_id)"
            + LS
            + "left join (term_information ti ) on (ti.term_id = gt.term_id)"
            + LS
            + "left join (language_lookup tlang ) on (fi.target_lang = tlang.language_id)  where gt.is_active!='N' and fi.is_active!='N'";

    private final static String GET_TOTAL_TERMS_IN_GS = LS
            + "select count(*) from globalsight_term_info  ti where ti.is_active!='n'";

    private final static String DELETE_TAG = LS
            + "delete from com.teaminology.hp.bo.Tag tag where tag.globalsightTermInfo.globalsightTermInfoId ";

    private final static String DELETE_ATTRIBUTES = LS
            + "delete from com.teaminology.hp.bo.Attributes att where att.tag.tagId ";
    private final static String GET_TMS_IN_GLOSSARY = LS
            + " select  "
            + LS
            + " year(create_date), count(tm_profile_info_id) "
            + LS
            + " from tm_profile_info "
            + LS
            + " where  year(create_date) >= year(adddate(curdate(), interval -4 year)) "
            + LS + " and is_active ='Y' ";

    private final static String GET_QUARTERLY_TM_DETAILS = LS
            + "select "
            + LS
            + "concat('Q',quarter(create_date),'-', year(create_date)) quarter, count(tm_profile_info_id) cnt"
            + LS
            + "from tm_profile_info tm"
            + LS
            + "where create_date >= (adddate(curdate(), interval -12 month)) and tm.is_active ='Y' ";

    private final static String GET_MONTHLY_TM_DETAILS = LS
            + "select "
            + LS
            + "date_format(create_date,'%b'), count(tm_profile_info_id)"
            + LS
            + "from tm_profile_info tm"
            + LS
            + "where  create_date >= (adddate(last_day(curdate()), interval -5 month)) and tm.is_active ='Y' ";


    private final static String GET_DISTINCT_JOBS = LS
            + LS + "select distinct(job_id),jobname  from file_info fInfo where fInfo.is_active ='Y' ";


    private final static String GET_TOTAL_TERM_IDS = LS +
            " select"
            + LS
            + " ti.term_id  from term_information ti "
            + LS
            + " left join (language_lookup lang ) on (ti.suggested_term_lang_id = lang.language_id)"
            + LS
            + " left join (deprecated_term_info dt) on (ti.term_id=dt.term_id)"
            + LS
            + " left join (company_lookup company) on (ti.company_id=company.company_id)"
            + LS + " where ti.is_active='Y'";

    private final static String GET_TOTAL_TM_TERMS = LS
            + " select"
            + LS
            + " tm.tm_profile_info_id "
            + LS
            + " from tm_profile_info tm ";


    private final static String GET_TOTAL_GS_TERMS = LS
            + "select  gt.term_id"
            + LS
            + " from globalsight_term_info gt"
            + LS
            + " left join (file_info fi) on (fi.file_info_id= gt.file_info_id) "
            + LS
            + "left join (language_lookup slang ) on (fi.source_lang = slang.language_id)"
            + LS
            + "left join (term_information ti ) on (ti.term_id = gt.term_id)"
            + LS
            + "left join (language_lookup tlang ) on (fi.target_lang = tlang.language_id)  where gt.is_active!='N' and fi.is_active!='N'";


    private final static String GET_VOTING_POLL_TERMS = LS
            + " select"
            + LS
            + " ti.term_id,"
            + LS
            + " (select sum(tt1.vote) from term_translation tt1 where tt1.term_id=ti.term_id) as all_votes, (select count(tvud.term_id) from term_vote_user_details tvud "
            + LS
            + " where (tvud.vote_invite_status ='Y'  or tvud.vote_invite_status is null) and  (tvud.is_active = 'Y' or tvud.is_active is null)"
            + LS
            + " and tvud.term_id=ti.term_id) as invites,(select count(dd.term_id) from deprecated_term_info dd where dd.term_id=ti.term_id and dd.is_active='Y') as is_deprecate "
            + LS
            + " from term_information ti "
            + LS + " where ti.term_id  in (";
    private final static String DELETE_IMPORTED_FILES = LS

            + "delete from com.teaminology.hp.bo.FileUploadStatus uploadStatus where uploadStatus.fileUploadStatusId in  (";

    private final static String DELETE_TM_PROFILE_INFO = LS
            + "delete from com.teaminology.hp.bo.TmProfileInfo tmprofile where tmprofile.tmProfileInfoId ";

    private final static String DELETE_TM_PROPERTIES = LS
            + "delete from com.teaminology.hp.bo.TMProperties tmprop where tmprop.tmProfileId ";
    
    //created final variable GET_USER_COMMENTS to get user's comment on term
    private final static String GET_USER_COMMENTS = LS
            + " select  "
            + LS
            + " usr.user_name,tvd.comments,tvd.voting_date from "
            + LS
            + " term_vote_user_details tvd  left join (term_translation tt ) on (tvd.term_translation_id=tt.term_translation_id)  "
            + LS
            + " left join (user usr ) on (tvd.user_id=usr.user_id ) where tvd.term_id ";

    private final static String  GET_USER_TOTAL_VOTES = LS + "select count(term_vote_usr_dtls_id) votes from term_vote_user_details tvd "
            + LS + "where  vote_invite_status ='Y' and  (tvd.is_active = 'Y' or tvd.is_active is null) and tvd.user_id = :param0 ";
    
    
	/*private static final String GET_TERM_HISTORY = LS 
			+ "select  "
			+ LS 
			+ " usr.user_name, tud.changed_source_term, tud.changed_target_term, tud.create_date from "
			+ LS 
			+ " term_update_details  tud  left join (user usr ) on (tud.user_id=usr.user_id and usr.is_active = 'Y') " 
			+ LS 
			+ " where tud.is_active ='Y' and tud.term_id ";*/
    
    private static final String GET_TERM_HISTORY = LS 
			+ "select  "
			+ LS 
			+ " usr.user_name, tud.changed_source_term, tud.changed_target_term, tud.part_of_speech, tud.concept_definition, tud.term_form, tud.term_status, "
			+ LS 
			+ " tud.term_category, tud.term_domain, tud.term_notes, tud.target_part_of_speech, tud.term_usage, tud.create_date from "
			+ LS 
			+ " term_update_details  tud  left join (user usr ) on (tud.user_id=usr.user_id and usr.is_active = 'Y') " 
			+ LS 
			+ " where tud.is_active ='Y' and tud.term_id ";
	
	private Object GET_STATUS_BY_STATUS_ID = LS
			+ "select  "
			+ LS 
			+ " status.status from status_lookup status "
			+ LS 
			+ LS 
			+ " where status.is_active ='Y' and status.status_id ";
    
    /**
     * To get Leader Board Members
     *
     * @param companyId to be filtered
     * @return List of leader board members
     */
    @Override
    public List<Member> getBoardMembers(final Integer companyId) {
        HibernateCallback<List<Member>> callback = new HibernateCallback<List<Member>>()
        {

            @Override
            public List<Member> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                query.append(GET_LEADER_BOARD_MEMBERS);
                if (companyId != null) {
                    query.append(" and usr.company_id=" + companyId);

                }
                query.append(" group by usr.user_id order by votes desc, usr.user_name asc");

                SQLQuery hibQuery = session.createSQLQuery(query.toString());

                List<Object> hibernateResults = hibQuery.list();
                List<Member> boardMembersList = new ArrayList<Member>();

                for (Object obj : hibernateResults) {
                    Object[] member = (Object[]) obj;
                    int colNdx = 0;
                    Member boardMember = new Member();

                    if ((Integer) member[colNdx] != null) {
                        boardMember.setUserId((Integer) member[colNdx]);
                    }
                    colNdx++;
                    if ((String) member[colNdx] != null) {
                        boardMember.setUserName((String) member[colNdx]);
                    }
                    colNdx++;

                    if ((String) member[colNdx] != null) {
                        boardMember.setFirstName((String) member[colNdx]);
                    }
                    colNdx++;

                    if ((String) member[colNdx] != null) {
                        boardMember.setLastName((String) member[colNdx]);
                    }
                    colNdx++;

                    if (((String) member[colNdx] != null)
                            && (((String) member[colNdx]).trim().length() != 0)) {
                        boardMember.setPhotoPath((String) member[colNdx]);
                    }
                    colNdx++;

                    if ((Integer) member[colNdx] != null) {
                        boardMember
                                .setTermRequestCount((Integer) member[colNdx]);
                    } else {
                        boardMember.setTermRequestCount(0);
                    }
                    colNdx++;

                    if ((BigInteger) member[colNdx] != null) {
                        boardMember.setTotalVotes((BigInteger) member[colNdx]);
                    }
                    colNdx++;
                    boardMembersList.add(boardMember);

                }

                logger.debug("Total Leader Board Members results :"
                        + boardMembersList.size());
                return boardMembersList;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get Terms in Glossary per year
     *
     * @param companyIds to be filtered
     * @return List of data holding year and no of terms per year
     */
    @Override
    public List<Terms> getTermsInGlossary(final String companyIds) {
        HibernateCallback<List<Terms>> callback = new HibernateCallback<List<Terms>>()
        {

            @Override
            public List<Terms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Terms> termsInGlossary = new ArrayList<Terms>();
                query.append(GET_TERMS_IN_GLOSSARY);
                if (companyIds != null) {
                    query.append(" and company_id in(" + companyIds + ")");

                }
                query.append(" group by(year(create_date)) ");
                termsInGlossary = getTermDetails(query, session);

                logger.debug("Total glossary terms per year "
                        + termsInGlossary.size());
                return termsInGlossary;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To Get Debated Terms per year
     *
     * @param companyIds to be filtered
     * @return List of data holding year and no of terms per year
     */
    @Override
    public List<Terms> getDebatedTerms(final String companyIds) {
        HibernateCallback<List<Terms>> callback = new HibernateCallback<List<Terms>>()
        {

            @Override
            public List<Terms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Terms> debatedTerms = new ArrayList<Terms>();
                query.append(GET_DEBATED_TERMS);
                if (companyIds != null) {
                    query.append(" and ti.company_id in(" + companyIds + ")");

                }
                query.append(" group by(year(invited_date)) ");

                debatedTerms = getTermDetails(query, session);
                logger.debug("Total debated terms per year "
                        + debatedTerms.size());
                return debatedTerms;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }
    
    
    
    @Override
    public List<TermVotingTo>  getVoteResults(final String  fromDate, final String  toDate, final Integer companyId){
    	
    	HibernateCallback<List<TermVotingTo>> callback = new HibernateCallback<List<TermVotingTo>>(){

    		@Override
    		public List<TermVotingTo> doInHibernate(Session session)
    				throws HibernateException, SQLException {
    			
    			if(fromDate == null || fromDate.isEmpty() || toDate == null || toDate.isEmpty()){
    				return null;
    			}
    			
    			List<TermVotingTo> termVote = null;
    			
    			StringBuffer query = new StringBuffer();
    			
    			query.append(GET_VOTE_RESULTS);

    			query.append(" where  date(curdate()) > date( tm.voting_expired_date)  and  date( tm.voting_expired_date)  between str_to_date(:fromDate,'%m/%d/%Y')  and  " +
    					"str_to_date(:toDate,'%m/%d/%Y') and  ti.company_id ="+companyId);
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
    		    hibQuery.setString("fromDate", fromDate);
                hibQuery.setString("toDate", toDate);
    			hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
                hibQuery.addScalar("SOURCE", Hibernate.STRING);
                hibQuery.addScalar("TOP_SUGGESTION", Hibernate.STRING);
                hibQuery.addScalar("ALTERNATE_TERM", Hibernate.STRING);
                hibQuery.addScalar("LANGUAGE", Hibernate.STRING);
                hibQuery.addScalar("NO_OF_VOTES", Hibernate.INTEGER);
                hibQuery.addScalar("IS_TOP", Hibernate.BOOLEAN);
                
                
    			Object[]  termVotes = hibQuery.list().toArray();
    			
    			if(termVotes !=null && termVotes.length>0){
    				
    				termVote = new ArrayList<TermVotingTo>();
    				Map<Long/*termId*/, TermVotingTo> termVoteMap = 
    						new HashMap<Long/*termId*/, TermVotingTo>();
    				
    				for (int i = 0; i < termVotes.length; i++) {

    					Object[] termVoteVal = (Object[]) termVotes[i];
    					
    					Long termId = termVoteVal[0] == null? null : Long.parseLong(termVoteVal[0].toString());
    					
    					if(termId == null){
    						continue;
    					}
    					
    					String source = termVoteVal[1] == null? null : termVoteVal[1].toString();
    					String topSuggestion = termVoteVal[2] == null? null : termVoteVal[2].toString();
    					String alternateTran = termVoteVal[3] == null? null :termVoteVal[3].toString();
    					String language =termVoteVal[4] == null? null :termVoteVal[4].toString();
    					String noOfVotes = termVoteVal[5] == null ? "0" : termVoteVal[5].toString();
    					Boolean isTopSugg = termVoteVal[6] == null? Boolean.FALSE : new Boolean(termVoteVal[6].toString());
    					if(termVoteMap.containsKey(termId)){
                            TermVotingTo tVoteObj = termVoteMap.get(termId);
        					if(isTopSugg){
        						tVoteObj.setTopSuggestion(new TermVoteDetails(termId, noOfVotes, topSuggestion));
        					}else{
        						tVoteObj.addAlternateSuggestion(new TermVoteDetails(termId, noOfVotes, alternateTran));
        					}
        					
    						
    					}else{
                            TermVotingTo tVoteObj = new TermVotingTo();
        					tVoteObj.setTermId(termId);
        					tVoteObj.setSource(source);
        					tVoteObj.setLanguage(language);
        					if(isTopSugg){
        						tVoteObj.setTopSuggestion(new TermVoteDetails(termId, noOfVotes, topSuggestion));
        					}else{
        						tVoteObj.addAlternateSuggestion(new TermVoteDetails(termId, noOfVotes, alternateTran));
        					}
        					termVoteMap.put(termId, tVoteObj);
    					}
    				}
    				
    				termVote.addAll(termVoteMap.values());
    			}

    			return termVote;
    		}

    	};
    			HibernateTemplate template = getHibernateTemplate();
    			return template.execute(callback);

    }
    /**
     * To get Total terms in Glossary
     *
     * @param companyIds An String to  filter the  terms
     * @return An int value holding the total no of terms in glossary
     */
    @Override
    public int getTotalTermsInGlossary(final String companyIds) {
        HibernateCallback<Integer> callback = new HibernateCallback<Integer>()
        {

            @Override
            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Integer totalTermsInGlossary;
                StringBuffer query = new StringBuffer();
                query.append(GET_TOTAL_TERMS_IN_GLOSSARY);
                if (companyIds != null) {
                    query.append(" and ti.company_id in(" + companyIds + ")");

                }
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                totalTermsInGlossary = new Integer(hibQuery.list().get(0)
                        .toString());
                logger.debug("No of terms in glossary :" + totalTermsInGlossary);
                return totalTermsInGlossary;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To get Total debated terms
     *
     * @param companyIds An String to  filter the  terms
     * @return An integer value holding the total no of debated terms
     */
    @Override
    @Transactional
    public int getTotalDebatedTerms(final String companyIds) {
        HibernateCallback<Integer> callback = new HibernateCallback<Integer>()
        {

            @Override
            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Integer totalDebatedTerms;
                StringBuffer query = new StringBuffer();
                query.append(GET_TOTAL_DEBATED_TERMS);
                if (companyIds != null) {
                    query.append(" and ti.company_id in(" + companyIds + ")");

                }
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                totalDebatedTerms = new Integer(hibQuery.list().get(0)
                        .toString());
                logger.debug("No of debated terms:" + totalDebatedTerms);
                return totalDebatedTerms;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To get current debated terms
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of current debated terms
     */
    @Override
    public List<String> getTopTerms(final Integer companyId) throws DataAccessException {
        HibernateCallback<List<String>> callback = new HibernateCallback<List<String>>()
        {

            @Override
            public List<String> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<String> topTerms = new ArrayList<String>();
                query.append(GET_TOP_TERMS);
                if (companyId != null) {
                    query.append(" and ti.company_id=" + companyId);

                }
                query.append(" order by tvm.invited_date desc limit 0, 20 ");

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("SUGGESTED_TERM", Hibernate.STRING);
                topTerms = hibQuery.list();
                logger.debug("Total current top terms :" + topTerms.size());
                return topTerms;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To get term attributes for a termID
     *
     * @param termId Integer which contains termId to be filtered
     * @return TermInformation w.r.t the term id
     */
    @Override
    public TermInformation getTermAttributes(final Integer termId)
            throws DataAccessException {
        if (termId == null) {
            return null;
        }
        HibernateCallback<TermInformation> callback = new HibernateCallback<TermInformation>()
        {

            @Override
            public TermInformation doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria hibCriteria = session
                        .createCriteria(TermInformation.class);
                hibCriteria.add(Restrictions.eq("termId", termId));
                logger.debug("Got attributes for termId :" + termId);
                return (TermInformation) hibCriteria.uniqueResult();
            }

        };
        return getHibernateTemplate().execute(callback);
    }


    /**
     * To get term attributes for a termID
     *
     * @param sourceTerm to be filtered
     * @param slangId   Id to be filtered
     * @return TermInformation w.r.t the source term and suggested term languageId
     */
    public TermInformation getTermInformationDetails(final String sourceTerm, final Integer slangId)
            throws DataAccessException {
        if (sourceTerm == null) {
            return null;
        }
        HibernateCallback<TermInformation> callback = new HibernateCallback<TermInformation>()
        {

            @Override
            public TermInformation doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria hibCriteria = session
                        .createCriteria(TermInformation.class);
                hibCriteria.add(Restrictions.eq("term_being_polled", sourceTerm));
                hibCriteria.add(Restrictions.eq("suggested_term_lang_id", slangId));
                logger.debug("Got attributes for sourceTerm :" + sourceTerm);
                return (TermInformation) hibCriteria.uniqueResult();
            }

        };
        return getHibernateTemplate().execute(callback);
    }


    /**
     * To Check whether sourceTerm exists or not in database
     *
     * @param sourceTerm to be filtered
     * @param slangId    to be filtered
     * @param companyId  to be filtered
     * @return Returns true if sourceTerm exists else it returns false
     */
    @Transactional
    public boolean ifSourceExists(String sourceTerm, Integer slangId, Integer companyId) {

         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(TermInformation.class);
        criteria.add(Restrictions.eq("termBeingPolled", sourceTerm));
        criteria.add(Restrictions.eq("suggestedTermLangId", slangId));
        if (companyId != null) {
            criteria.add(Restrictions.eq("termCompany.companyId", companyId));
        }
        criteria.add(Restrictions.eq("isActive", "Y"));
        criteria.setProjection(Projections.count("termId"));
        Integer countOfTerms = (Integer) criteria.uniqueResult();

        if (countOfTerms > 0) {
            return true;
        }

        return false;
    }

    /**
     * To check whether SourceTerm exists or not in database
     *
     * @param termInformation Object
     * @return Returns true if sourceTerm exists else it returns false
     */
    @Transactional
    public boolean ifSourceTermBaseExists(TermInformation termInformation) {
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(TermInformation.class);
        criteria.add(Restrictions.eq("termBeingPolled", termInformation.getTermBeingPolled()));
        if (termInformation.getTermCompany() != null) {
            criteria.add(Restrictions.eq("termCompany.companyId", termInformation.getTermCompany().getCompanyId()));
        }
        criteria.add(Restrictions.eq("isActive", "Y"));
        criteria.setProjection(Projections.count("termId"));
        Integer countOfTerms = (Integer) criteria.uniqueResult();

        if (countOfTerms > 0) {
            return true;
        }

        return false;
    }

    /**
     * To check whether TargetTerm exists or not  by passing attributes of TermInformation Object
     *
     * @param termInformation Object
     * @return Returns true if TargetTerm exists else it returns false
     */
    @Transactional
    public TermInformation isTargetTermBaseExists(TermInformation termInformation) {

         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(TermInformation.class);
  
        /**Target Term Attributes **/
        if (termInformation.getSuggestedTerm() != null && termInformation.getSuggestedTerm().trim().length() > 0) {
            criteria.add(Restrictions.eq("suggestedTerm", termInformation.getSuggestedTerm().trim()));
        }else if(termInformation.getSuggestedTerm() == null){
        	criteria.add(Restrictions.isNull("suggestedTerm"));
        }else{
        	criteria.add(Restrictions.eq("suggestedTerm",""));
        }
        if (termInformation.getSuggestedTermLangId() != null) {
            criteria.add(Restrictions.eq("suggestedTermLangId", termInformation.getSuggestedTermLangId()));
        } else {
        	criteria.add(Restrictions.isNull("suggestedTermLangId"));
        }
        if (termInformation.getSuggestedTermPosId() != null) {
            criteria.add(Restrictions.eq("suggestedTermPosId", termInformation.getSuggestedTermPosId()));
        }
        else{
        	criteria.add(Restrictions.isNull("suggestedTermPosId"));
        }
        
        if (termInformation.getSuggestedTermStatusId() != null) {
            criteria.add(Restrictions.eq("suggestedTermStatusId", termInformation.getSuggestedTermStatusId()));
        }
        else{
        	criteria.add(Restrictions.isNull("suggestedTermStatusId"));
        }
       
        if (termInformation.getTermCompany() != null) {
            if (termInformation.getTermCompany().getCompanyId() != null) {
                criteria.add(Restrictions.eq("termCompany.companyId", termInformation.getTermCompany().getCompanyId()));
            }
        }
        if (termInformation.getConceptDefinition() != null) {
            criteria.add(Restrictions.eq("conceptDefinition", termInformation.getConceptDefinition().trim()));
        }else if(termInformation.getConceptDefinition() == null) {
        	criteria.add(Restrictions.isNull("conceptDefinition"));
        }else{
        	criteria.add(Restrictions.eq("conceptDefinition",""));
        }
        if (termInformation.getTermUsage() != null) {
            criteria.add(Restrictions.eq("termUsage", termInformation.getTermUsage().trim()));
        }else if(termInformation.getTermUsage() == null) {
        	criteria.add(Restrictions.isNull("termUsage"));
        }else {
        	criteria.add(Restrictions.eq("termUsage",""));
        }
        if (termInformation.getTermNotes() != null) {
            criteria.add(Restrictions.eq("termNotes", termInformation.getTermNotes().trim()));
        }else if(termInformation.getTermNotes() == null){
        	criteria.add(Restrictions.isNull("termNotes"));
        }else{
        	criteria.add(Restrictions.eq("termNotes",""));
        }
        if (termInformation.getTermForm() != null) {
            criteria.add(Restrictions.eq("termForm.formId", termInformation.getTermForm().getFormId()));
        }else{
        	criteria.add(Restrictions.isNull("termForm.formId"));
        }
        if (termInformation.getTermStatusId() != null) {
            criteria.add(Restrictions.eq("termStatusId", termInformation.getTermStatusId()));
        }else{
        	criteria.add(Restrictions.isNull("termStatusId"));
        }
        if (termInformation.getComments() != null) {
            criteria.add(Restrictions.eq("comments", termInformation.getComments()));
        }else if(termInformation.getComments() == null){
        	criteria.add(Restrictions.isNull("comments"));
        }else{
        	criteria.add(Restrictions.eq("comments",""));
        }
        criteria.add(Restrictions.eq("isActive", "Y"));
        List<TermInformation> termInformationList = (List<TermInformation>) criteria.list();
        TermInformation termInformationTargetResult = null;
        if (termInformationList != null && !termInformationList.isEmpty()) {
            for (TermInformation termInfo : termInformationList) {
            	if(termInfo.getSuggestedTerm() != null && termInformation.getSuggestedTerm() != null) {
                    if (termInfo.getSuggestedTerm().equals(termInformation.getSuggestedTerm())) {
                        termInformationTargetResult = termInfo;
                        break;
                    }
            	} else {
            		 termInformationTargetResult = termInfo;
            	}
            }
        }
        return termInformationTargetResult;
    }


    /**
     * To check whether TargetTerm exists or not  by passing attributes of TermInformation Object
     *
     * @param termInformation Object
     * @return Returns true if TargetTerm exists else it returns false
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public TermInformation isTermBaseAttrbutesExists(TermInformation termInformation) {
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(TermInformation.class);
        /**Source Term Attributes **/
        if (termInformation.getTermBeingPolled() != null) {
            criteria.add(Restrictions.eq("termBeingPolled", termInformation.getTermBeingPolled().trim()));
        }
        if (termInformation.getTermPOS() != null) {
            if (termInformation.getTermPOS().getPartsOfSpeechId() != null) {
                criteria.add(Restrictions.eq("termPOS.partsOfSpeechId", termInformation.getTermPOS().getPartsOfSpeechId()));
            }
        }
        else{
        	criteria.add(Restrictions.isNull("termPOS.partsOfSpeechId"));
        }
        if (termInformation.getTermCategory() != null) {
            if (termInformation.getTermCategory().getCategoryId() != null) {
                criteria.add(Restrictions.eq("termCategory.categoryId", termInformation.getTermCategory().getCategoryId()));
            }
        }
        else{
        	criteria.add(Restrictions.isNull("termCategory.categoryId"));
        }
        if (termInformation.getTermDomain() != null) {
            if (termInformation.getTermDomain().getDomainId() != null) {
                criteria.add(Restrictions.eq("termDomain.domainId", termInformation.getTermDomain().getDomainId()));
            }
        }
        else{
        	criteria.add(Restrictions.isNull("termDomain.domainId"));
        }
        if (termInformation.getTermCompany() != null) {
            if (termInformation.getTermCompany().getCompanyId() != null) {
                criteria.add(Restrictions.eq("termCompany.companyId", termInformation.getTermCompany().getCompanyId()));
            }
        }
      
        if (termInformation.getSuggestedTermLangId() != null) {
            criteria.add(Restrictions.eq("suggestedTermLangId", termInformation.getSuggestedTermLangId()));
        }
        else{
        	criteria.add(Restrictions.isNull("suggestedTermLangId"));
        }
        criteria.add(Restrictions.eq("isActive", "Y"));
        List<TermInformation> termInformationList = (List<TermInformation>) criteria.list();
        TermInformation termInformationResult = null;
        if (termInformationList != null && !termInformationList.isEmpty()) {
            for (TermInformation termInfo : termInformationList) {
                if (termInfo.getTermBeingPolled() != null && termInformation.getTermBeingPolled() != null)
                    if (termInfo.getTermBeingPolled().equals(termInformation.getTermBeingPolled())) {
                        termInformationResult = termInfo;
                        break;
                    } 
            }
        }

        return termInformationResult;
    }


    /**
     * To get Suggested terms for a termID
     *
     * @param termId Integer which contains termId to be filtered
     * @return List of suggested term details w.r.t the term id
     */
    @Override
    public List<SuggestedTermDetails> getSuggestedTerms(final Integer termId)
            throws DataAccessException {
        if (termId == null) {
            return null;
        }
        HibernateCallback<List<SuggestedTermDetails>> callback = new HibernateCallback<List<SuggestedTermDetails>>()
        {

            @Override
            public List<SuggestedTermDetails> doInHibernate(Session session)
                    throws HibernateException, SQLException {

                StringBuffer query = new StringBuffer();
                List<SuggestedTermDetails> suggestedTermsList = new ArrayList<SuggestedTermDetails>();
                query.append(GET_SUGGESTED_TERMS);
                query.append(" = " + termId);
                String previousSuggestedTerm = null;
                String votersNames = null;

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("SUGGESTED_TERM", Hibernate.STRING);
                hibQuery.addScalar("VOTE", Hibernate.INTEGER);
                hibQuery.addScalar("USER_NAME", Hibernate.STRING);
                hibQuery.addScalar("TERM_TRANSLATION_ID", Hibernate.INTEGER);
                hibQuery.addScalar("IS_UPDATED", Hibernate.STRING);
                Object[] suggestedTermsData = hibQuery.list().toArray();
                if ((suggestedTermsData != null)
                        && suggestedTermsData.length > 0) {
                    for (int i = 0; i < suggestedTermsData.length; i++) {
                        Object[] suggestedTermsDataVal = (Object[]) suggestedTermsData[i];
                        String suggestedTerm = (suggestedTermsDataVal[0] == null) ? ""
                                : suggestedTermsDataVal[0].toString();
                        String votes = (suggestedTermsDataVal[1] == null) ? "0"
                                : suggestedTermsDataVal[1].toString();
                        String userName = (suggestedTermsDataVal[2] == null) ? ""
                                : suggestedTermsDataVal[2].toString();
                        String suggestedTermId = (suggestedTermsDataVal[3] == null) ? ""
                                : suggestedTermsDataVal[3].toString();
                        String isUpdated = (suggestedTermsDataVal[4] == null) ? "" : suggestedTermsDataVal[4].toString();


                        SuggestedTermDetails suggestedTermDtls = new SuggestedTermDetails();
                        if (suggestedTermsList != null
                                && suggestedTerm.equals(previousSuggestedTerm)) {
                            int previousElement = suggestedTermsList.size() - 1;
                            SuggestedTermDetails previousSuggestedTermDtls = suggestedTermsList
                                    .get(previousElement);
                            votersNames = previousSuggestedTermDtls
                                    .getVotersNames();
                            suggestedTermsList.remove(previousElement);
                            votersNames += ", " + userName;
                        } else {
                            votersNames = userName;
                        }
                        suggestedTermDtls.setSuggestedTerm(suggestedTerm);
                        suggestedTermDtls.setNoOfVotes(Integer.parseInt(votes));
                        suggestedTermDtls.setVotersNames(votersNames);
                        suggestedTermDtls.setSuggestedTermId(Integer
                                .parseInt(suggestedTermId));
                        suggestedTermDtls.setIsUpdated(isUpdated);

                        previousSuggestedTerm = suggestedTermDtls
                                .getSuggestedTerm();
                        suggestedTermsList.add(suggestedTermDtls);

                    }
                }
                logger.debug("Total suggested terms for termId " + termId
                        + " is :" + suggestedTermsList.size());
                return suggestedTermsList;

            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get monthly glossary terms
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of data holding month and no of terms per month
     */
    @Override
    public List<Terms> getMonthlyTermDetails(final Integer companyId) {
        HibernateCallback<List<Terms>> callback = new HibernateCallback<List<Terms>>()
        {

            @Override
            public List<Terms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Terms> monthlyTerms = new ArrayList<Terms>();
                query.append(GET_MONTHLY_TERM_DETAILS);
                if (companyId != null) {
                    query.append(" and ti.company_id=" + companyId);

                }
                query.append(" group by(month(create_date)) order by  create_date ");


                monthlyTerms = getTermDetails(query, session);
                logger.debug(" Total monthly term details :"
                        + monthlyTerms.size());
                return monthlyTerms;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get monthly debated term details
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of data holding month and no of terms per month
     */
    @Override
    public List<Terms> getMonthlyDebatedTerms(final Integer companyId) {
        HibernateCallback<List<Terms>> callback = new HibernateCallback<List<Terms>>()
        {

            @Override
            public List<Terms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Terms> monthlyDebatedTerms = new ArrayList<Terms>();
                query.append(GET_MONTHLY_DEBATED_DETAILS);
                if (companyId != null) {
                    query.append(" and ti.company_id=" + companyId);

                }
                query.append("  group by(month(invited_date)) order by  invited_date  ");

                monthlyDebatedTerms = getTermDetails(query, session);

                logger.debug(" Total monthly debated term details :"
                        + monthlyDebatedTerms.size());
                return monthlyDebatedTerms;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To get quarterly term details
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of data holding quarter-year and no of terms per quarter
     */
    @Override
    public List<Terms> getQuarterlyTermDetails(final Integer companyId) {
        HibernateCallback<List<Terms>> callback = new HibernateCallback<List<Terms>>()
        {

            @Override
            public List<Terms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Terms> quarterlyTerms = new ArrayList<Terms>();
                query.append(GET_QUARTERLY_TERM_DETAILS);
                if (companyId != null) {
                    query.append(" and ti.company_id=" + companyId);

                }
                query.append("  group by quarter(create_date),year(create_date) order by create_date  ");

                quarterlyTerms = getTermDetailsQuarter(query, session);
                logger.debug(" Total quarterly term details :"
                        + quarterlyTerms.size());
                return quarterlyTerms;

            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get quarterly debated term details
     *
     * @param companyId An Integer to  filter the  terms
     * @return List of data holding quarter-year and no of terms per quarter
     */
    @Override
    public List<Terms> getQuarterlyDebatedTerms(final Integer companyId) {
        HibernateCallback<List<Terms>> callback = new HibernateCallback<List<Terms>>()
        {

            @Override
            public List<Terms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Terms> quarterlyDebatedTerms = new ArrayList<Terms>();
                query.append(GET_QUARTERLY_DEBATED_TERM_DETAILS);
                if (companyId != null) {
                    query.append(" and ti.company_id=" + companyId);

                }
                query.append(" 	group by quarter(invited_date),year(invited_date) order by invited_date  ");

                quarterlyDebatedTerms = getTermDetailsQuarter(query, session);
                logger.debug(" Total quarterly debated term details :"
                        + quarterlyDebatedTerms.size());
                return quarterlyDebatedTerms;

            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * Method to get term details
     *
     * @param query   Query to be executed
     * @param session Hibernate session
     * @return List of terms
     */
    @Transactional
    private List<Terms> getTermDetails(StringBuffer query, Session session) {

        SQLQuery hibQuery = session.createSQLQuery(query.toString());
        List<Terms> termsPerIntervalList = new ArrayList<Terms>();
        Object[] Data = hibQuery.list().toArray();
        if ((Data != null) && Data.length > 0) {
            for (int i = 0; i < Data.length; i++) {
                Object[] termsDataVal = (Object[]) Data[i];
                Terms termsPerInterval = new Terms();
                termsPerInterval.setInterval(termsDataVal[0].toString());
                termsPerInterval.setTermsPerInterval(new Integer(termsDataVal[1].toString()));
                termsPerIntervalList.add(termsPerInterval);
            }

        }
        return termsPerIntervalList;
    }
    /**
     * Method to get term details for quarter
     *
     * @param query   Query to be executed
     * @param session Hibernate session
     * @return List of terms
     */
    @Transactional
    private List<Terms> getTermDetailsQuarter(StringBuffer query, Session session) {

        SQLQuery hibQuery = session.createSQLQuery(query.toString());
        List<Terms> termsPerIntervalList = new ArrayList<Terms>();
        hibQuery.addScalar("quarter", Hibernate.STRING);
        hibQuery.addScalar("cnt", Hibernate.INTEGER);
        Object[] Data = hibQuery.list().toArray();
        if ((Data != null) && Data.length > 0) {
            for (int i = 0; i < Data.length; i++) {
                Object[] termsDataVal = (Object[]) Data[i];
                Terms termsPerInterval = new Terms();
                termsPerInterval.setInterval(termsDataVal[0].toString());
                termsPerInterval.setTermsPerInterval(new Integer(termsDataVal[1].toString()));
                termsPerIntervalList.add(termsPerInterval);
            }

        }
        return termsPerIntervalList;
    }

    /**
     * Method to get term details
     *
     * @param query   Query to be executed
     * @param session Hibernate session
     * @return List of Poll Terms
     */
    @Transactional
    private List<PollTerms> getPollTermDetails(StringBuffer query,
                                               Session session, Integer statusId) {

        List<PollTerms> pollTerms = new ArrayList<PollTerms>();
        SQLQuery hibQuery = session.createSQLQuery(query.toString());
        hibQuery.setParameter("param0", statusId);
        hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
        hibQuery.addScalar("TERM_BEING_POLLED", Hibernate.STRING);
        hibQuery.addScalar("SUGGESTED_TERM", Hibernate.STRING);
        hibQuery.addScalar("LANGUAGE_LABEL", Hibernate.STRING);
        hibQuery.addScalar("EXPIRED_DATE", Hibernate.STRING);
        hibQuery.addScalar("ALL_VOTES", Hibernate.INTEGER);
        hibQuery.addScalar("INVITES", Hibernate.INTEGER);
        hibQuery.addScalar("IS_DEPRECATE", Hibernate.INTEGER);
        Object[] termsData = hibQuery.list().toArray();
        if ((termsData != null) && termsData.length > 0) {
            for (int i = 0; i < termsData.length; i++) {
                Object[] termsDataVal = (Object[]) termsData[i];
                PollTerms term = new PollTerms();
                term.setTermId((Integer) termsDataVal[0]);
                term.setSourceTerm((String) termsDataVal[1]);
                term.setSuggestedTerm((String) termsDataVal[2]);
                term.setLanguage((String) termsDataVal[3]);
                term.setPollExpirationDt((String) termsDataVal[4]);
                term.setVotesPerTerm((Integer) termsDataVal[5]);
                term.setInvites((Integer) termsDataVal[6]);
                term.setDeprecatedCount((Integer) termsDataVal[7]);
                pollTerms.add(term);
            }
        }
        return pollTerms;
    }

    /**
     * To update Term Details
     *
     * @param termInformation TermInformation that has to be updated
     * @return TermInformation object
     * @throws DataAccessException
     */
    @Override
    @Transactional
    public TermInformation updateTermDetails(TermInformation termInformation, String isReplace)
            throws DataAccessException {
        if (termInformation == null) {
            throw new IllegalArgumentException("Invalid termInformation");
        }
        TermInformation termInfoData = null;
        String status = null;
        if (termInformation.getTermId() != null) {
             Session session = getHibernateSession();
            termInfoData = (TermInformation) session.get(TermInformation.class, termInformation.getTermId());
            TermVoteMaster termVoteMaster = (TermVoteMaster) session.createCriteria(TermVoteMaster.class)
                    .add(Restrictions.eq("termId", termInfoData.getTermId())).uniqueResult();
            boolean isExists = false;
            Criteria crit = session.createCriteria(TermTranslation.class);
            crit.add(Restrictions.eq("termId", termInfoData.getTermId()));
            List<TermTranslation> termTranslationList = (List<TermTranslation>) crit.list();
            if (termTranslationList != null && termTranslationList.size() > 0) {
                for (TermTranslation termTranslation : termTranslationList) {
                    if (termInformation.getSuggestedTerm().equalsIgnoreCase(termTranslation.getSuggestedTerm())) {
                        isExists = true;
                    }
                }
                if (!isExists) {
                    for (TermTranslation termTranslation : termTranslationList) {
                        if (termTranslation.getSuggestedTerm() != null && termInfoData.getSuggestedTerm() != null && termTranslation.getSuggestedTerm().equalsIgnoreCase(termInfoData.getSuggestedTerm())) {
                            termTranslation.setTermId(termInformation.getTermId());
                            termTranslation.setSuggestedTerm(termInformation.getSuggestedTerm());
                            termTranslation.setUpdatedBy(termInformation.getUpdatedBy());
                            termTranslation.setUpdateDate(termInformation.getUpdateDate());
                            if (termVoteMaster != null && termTranslation.getVote() != null && termTranslation.getVote().intValue() > 0) {
                                termTranslation.setIsUpdated("Y");
                            }
                            getHibernateTemplate().update(termTranslation);
                        }
                    }
                }

            } else {
                if (termInformation.getSuggestedTerm() != null) {
                    TermTranslation termTranslation = new TermTranslation();
                    termTranslation.setSuggestedTerm(termInformation.getSuggestedTerm());
                    termTranslation.setSuggestedTermLangId(termInfoData.getSuggestedTermLangId());
                    termTranslation.setTermId(termInformation.getTermId());
                    termTranslation.setUserId(termInformation.getCreatedBy());
                    termTranslation.setIsActive("Y");
                    termTranslation.setUpdateDate(new Date());
                    getHibernateTemplate().save(termTranslation);
                }
            }

            if(isReplace.equalsIgnoreCase("Y") && termInfoData != null) {
            	saveHistoryPreviousData(termInfoData, termInformation);
            	
            	termInfoData.setTermBeingPolled(termInformation.getTermBeingPolled().trim());
            	termInfoData.setSuggestedTerm((termInformation.getSuggestedTerm() != null || !termInformation.getSuggestedTerm().isEmpty()) ? termInformation.getSuggestedTerm().trim() : "");
            	termInfoData.setUpdateDate(new Date());
            	getHibernateTemplate().update(termInfoData);
            	
            }
            
            if (termInfoData != null && !isReplace.equalsIgnoreCase("Y")) {

            	saveHistoryPreviousData(termInfoData, termInformation);
        		termInfoData.setSuggestedTermPosId(termInformation.getSuggestedTermPosId());

            	if (termInformation.getTermPOS() != null) {
            		termInfoData.setTermPOS(termInformation.getTermPOS());
            	}
            	termInfoData.setTermProgram(termInformation.getTermProgram());
            	termInfoData.setTermForm(termInformation.getTermForm());
            	if (termInformation.getTermForm() != null)
            		termInfoData.setSuggestedTermFormId(termInformation.getTermForm().getFormId());

            	termInfoData.setTermCategory(termInformation.getTermCategory());
            	termInfoData.setTermDomain(termInformation.getTermDomain());
            	termInfoData.setDeprecatedTermInfo(null);
            	if (termInformation.getTermNotes() != null) {
            		termInfoData.setTermNotes(termInformation.getTermNotes());
            	}

            	//set term status TNG-47
            	if(termInformation.getTermStatusId() != null) {
            		termInfoData.setTermStatusId(termInformation.getTermStatusId());
            	}

            	termInfoData.setConceptDefinition(termInformation.getConceptDefinition());
            	termInfoData.setTermUsage(termInformation.getTermUsage());
            	termInfoData.setUpdatedBy(termInformation.getUpdatedBy());
            	termInfoData.setSuggestedTerm(termInformation.getSuggestedTerm());
            	termInfoData.setTermBeingPolled(termInformation.getTermBeingPolled()); //lalitha
            	termInfoData.setIsActive("Y");
            	termInfoData.setUpdateDate(new Date());
            	getHibernateTemplate().update(termInfoData);
            	logger.debug(" Term Information is updated");


            } if(termInfoData == null) {
                getHibernateTemplate().save(termInformation);
                logger.debug(" Term Information is created");
            }
        }
        return termInfoData;
    }

    @Transactional
    private void saveHistoryPreviousData(TermInformation termInfoData, TermInformation termInformation) {
    	String status = null;
    	if(!(termInformation.getTermBeingPolled().equals(termInfoData.getTermBeingPolled())) || 
    			!(termInformation.getSuggestedTerm().equals(termInfoData.getSuggestedTerm())) ||
    			!((termInformation.getTermPOS() != null) && (termInfoData.getTermPOS() != null) && termInformation.getTermPOS().getPartOfSpeech().equals(termInfoData.getTermPOS().getPartOfSpeech())) || 
    			!((termInformation.getTermForm() != null) && (termInfoData.getTermForm() != null) && termInformation.getTermForm().getFormName().equals(termInfoData.getTermForm().getFormName())) || 
    			!((termInformation.getConceptDefinition() != null) && (termInfoData.getConceptDefinition() != null) &&  termInformation.getConceptDefinition().equals(termInfoData.getConceptDefinition())) || 
    			!((termInformation.getTargetTermPOS() != null) && (termInfoData.getTargetTermPOS() != null) &&  termInformation.getTargetTermPOS().getPartOfSpeech().equals(termInfoData.getTargetTermPOS().getPartOfSpeech())) || 
    			!((termInformation.getTermCategory() != null) && (termInfoData.getTermCategory() != null) && termInformation.getTermCategory().getCategory().equals(termInfoData.getTermCategory().getCategory())) || 
    					!((termInformation.getTermDomain() != null) && (termInfoData.getTermDomain() != null) && termInformation.getTermDomain().getDomain().equals(termInfoData.getTermDomain().getDomain())) || 
    							!((termInformation.getTermNotes() != null) && (termInfoData.getTermNotes() != null) && termInformation.getTermNotes().equals(termInfoData.getTermNotes())) ||
//    					termInformation.getTermId() == termInfoData.getTermId()) || 
    									!((termInformation.getTermUsage() != null) &&  (termInfoData.getTermUsage() != null) && termInformation.getTermUsage().equals(termInfoData.getTermUsage())))  {
    		// for Task TNG-89 save data into term_update_details table
    		TermUpdateDetails termUpdateDetails = new TermUpdateDetails();
    		termUpdateDetails.setTermId(termInformation.getTermId());
    		termUpdateDetails.setChangedSourceTerm(termInfoData.getTermBeingPolled().trim());
    		termUpdateDetails.setChangedTargetTerm((termInfoData.getSuggestedTerm() != null && !termInfoData.getSuggestedTerm().isEmpty()) ? termInfoData.getSuggestedTerm().trim() : "");
    		termUpdateDetails.setUserId(termInformation.getUpdatedBy());
    		termUpdateDetails.setCreateDate(new Date());
    		termUpdateDetails.setIsActive("Y");
    		termUpdateDetails.setConceptDefinition(termInfoData.getConceptDefinition());
    		termUpdateDetails.setTargetTermPOS((termInformation.getTargetTermPOS() != null) ? termInformation.getTargetTermPOS().getPartOfSpeech() : "");
    		termUpdateDetails.setTermCategory((termInfoData.getTermCategory() != null) ? termInfoData.getTermCategory().getCategory() : "");
    		termUpdateDetails.setTermDomain((termInfoData.getTermDomain() != null) ? termInfoData.getTermDomain().getDomain() : "");
    		termUpdateDetails.setTermForm((termInfoData.getTermForm() != null) ? termInfoData.getTermForm().getFormName() : "");
    		termUpdateDetails.setTermNotes(termInfoData.getTermNotes());
    		termUpdateDetails.setTermsPOS((termInfoData.getTermPOS() != null) ? termInfoData.getTermPOS().getPartOfSpeech() : "");
    		if(termInfoData.getTermStatusId() != null) {
    			status = getTermStatusByStatusId(termInfoData.getTermStatusId());
    			if(status != null) {
    				termUpdateDetails.setTermStatus(status);
    			}else {
    				termUpdateDetails.setTermStatus(status);
    			}
    		}else {
    			termUpdateDetails.setTermStatus(status);
    		}
    		termUpdateDetails.setTermUsage(termInfoData.getTermUsage());
    		termUpdateDetails.setUpdatedBy(termInformation.getUpdatedBy());
    		getHibernateTemplate().save(termUpdateDetails);
    	}

    }

	private String getTermStatusByStatusId(Integer statusId) {
    	SQLQuery hibQuery = null;
    	if(statusId != null) {
    	Session session = getHibernateSession();
        StringBuffer query = new StringBuffer();
        query.append(GET_STATUS_BY_STATUS_ID);
        query.append(" = " + statusId);
        hibQuery = session.createSQLQuery(query.toString());
        hibQuery.addScalar("status", Hibernate.STRING);
        logger.debug("Got status by status_id successfully");
    	}
        return  (String) hibQuery.uniqueResult();
	}

	/**
     * To get Translation id
     *
     * @param termInformation TermInformation that has to be filtered
     * @return List of TermTranslation obj's
     */
    @Transactional
    private List<TermTranslation> getTranslationId(TermInformation termInformation) {
         Session session = getHibernateSession();
        Integer translationId = null;
        Criteria hibCriteria = session.createCriteria(TermTranslation.class);
        hibCriteria.add(Restrictions.eq("termId", termInformation.getTermId()));
        hibCriteria.add(Restrictions.eq("suggestedTerm", termInformation.getSuggestedTerm()));
        return (List<TermTranslation>) hibCriteria.list();
    }

    /**
     * To extend poll of expired term id
     *
     * @param termVoteMaster TermVoteMaster that has to be updated
     */
    @Override
    public void extendPoll(TermVoteMaster termVoteMaster) {
        if (termVoteMaster == null) {
            throw new IllegalArgumentException("Invalid termVoteMaster");
        }

        termVoteMaster.setIsActive("Y");
        if (termVoteMaster.getTermVoteId() == null) {
            getHibernateTemplate().save(termVoteMaster);
            logger.debug(" Term Vote Master is created");
        } else if (termVoteMaster.getTermVoteId() != null) {
            getHibernateTemplate().update(termVoteMaster);
            logger.debug(" Term Vote Master is updated");
        }
    }

    /**
     * To get Term Vote Master details of a termId
     *
     * @param termId the Integer to get details
     * @return TermVoteMaster w.r.t the term id
     */
    @Override
    @Transactional
    public TermVoteMaster getTermVoteMaster(Integer termId) {
        if (termId == null) {
            return null;
        }

         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TermVoteMaster.class);
        hibCriteria.add(Restrictions.eq("termId", termId));
        logger.debug("Got Term Vote Master details for term Id :" + termId);
        return (TermVoteMaster) hibCriteria.uniqueResult();
    }

    /**
     * To approve a suggested term for a term Id
     *
     * @param suggestedTermId The Integer to get details
     * @return If approved it returns 1 else 0
     */
    @Override
    @Transactional
    public int approveSuggestedTerm(Integer suggestedTermId) {
        if (suggestedTermId == null) {
            return 0;
        }

        int updateStatus = 0;
         Session session = getHibernateSession();
        TermTranslation suggestedTermDetails = (TermTranslation) session.get(TermTranslation.class, suggestedTermId);
        if (suggestedTermDetails != null) {
            if (suggestedTermDetails.getTermId() != null) {
                TermInformation termInfo = (TermInformation) session.get(TermInformation.class, suggestedTermDetails.getTermId());
                if (termInfo != null) {
                    termInfo.setSuggestedTerm(suggestedTermDetails.getSuggestedTerm());
                    termInfo.setTermStatusId(2);
                    termInfo.setSuggestedTermStatusId(2);
                    termInfo.setUpdateDate(new Date());
                    termInfo.setIsActive("Y");
                    if (termInfo.getTermId() != null) {
                        getHibernateTemplate().update(termInfo);
                        logger.debug("Term Information is updated");
                    } else {
                        getHibernateTemplate().save(termInfo);
                        logger.debug("Term Information is created");
                    }
                    updateStatus = 1;
                }
            }


        }
        return updateStatus;
    }

    /**
     * To sort expired poll terms
     *
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param langIds   String containing language id's
     * @param pageNum   Integer to limit the data
     * @return List of terms
     */
    @Override
    @Transactional
    public List<PollTerms> sortOrFilterExpPollTerms(final String colName,
                                                    final String sortOrder, final String langIds, final Integer pageNum, final Integer companyId, final String expTermCompanyIds, final Integer statusId)
            throws DataAccessException {
        HibernateCallback<List<PollTerms>> callback = new HibernateCallback<List<PollTerms>>()
        {

            @SuppressWarnings("deprecation")
            @Override
            public List<PollTerms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                int limitFrom = 0;
                int limitTo = 0;
                StringBuffer query = new StringBuffer();
                List<PollTerms> expiredTerms = new ArrayList<PollTerms>();
                query.append(GET_EXPIRED_POLL_TERMS);

                if (langIds != null && !langIds.equalsIgnoreCase("null")) {
                    query.append(" and ti.suggested_term_lang_id in ("
                            + langIds + ")");
                }
                if (expTermCompanyIds != null && !expTermCompanyIds.equalsIgnoreCase("null")) {
                    query.append(" and ti.company_id in ("
                            + expTermCompanyIds + ")");
                } else if (companyId != null) {
                    query.append(" and ti.company_id= " + companyId);
                }
                query.append(" group by ti.term_id");

                if (colName.equalsIgnoreCase("language")) {
                    query.append(" order by lang.language_label " + sortOrder);
                } else if (colName.equalsIgnoreCase("pollExpirationDate")) {
                    query.append(" order by tvm1.voting_expired_date "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("sourceTerm")) {
                    query.append(" order by ti.term_being_polled " + sortOrder);
                } else if (colName.equalsIgnoreCase("suggestedTerm")) {
                    query.append(" order by ti.suggested_term " + sortOrder);
                }
                if (pageNum != 0) {
                    limitFrom = (pageNum - 1) * 10;
                    limitTo = (pageNum) * 10;
                    query.append(" limit " + limitFrom + " , " + limitTo);
                }

                expiredTerms = getPollTermDetails(query, session, statusId);

                logger.debug("Total Expired poll terms :" + expiredTerms.size());
                return expiredTerms;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To Get user poll terms for a language Id
     *
     * @param languageId String to filter terms respectively
     * @param colName    column name that has to be sorted
     * @param sortOrder  order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @param userId     Integer to filter terms respectively
     * @return List of terms
     */
    @Override
    @Transactional
    public List<PollTerms> getUserPollTerms(final String languageId,
                                            final String colName, final String sortOrder,
                                            final Integer pageNum, final Integer userId)
            throws DataAccessException {
        HibernateCallback<List<PollTerms>> callback = new HibernateCallback<List<PollTerms>>()
        {

            @SuppressWarnings("deprecation")
            @Override
            public List<PollTerms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                int limitFrom = 0;
                int limitTo = 0;
                StringBuffer query = new StringBuffer();
                List<PollTerms> userPollTerms = new ArrayList<PollTerms>();
                query.append(GET_USER_POLL_TERMS);
                query.append(userId
                        + " )  and (ti.is_active ='Y' or ti.is_active is null) and ti.suggested_term_lang_id = "
                        + languageId);
                query.append(" group by ti.term_id ");
                
                if(colName == null || colName.equalsIgnoreCase("null")) {
                    query.append(" order by ti.term_being_polled " + "ASC");
                }
                if (colName.equalsIgnoreCase("pollExpirationDate")) {
                    query.append(" order by tvm.voting_expired_date "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("sourceTerm")) {
                    query.append(" order by ti.term_being_polled " + sortOrder);
                } else if (colName.equalsIgnoreCase("suggestedTerm")) {
                    query.append(" order by ti.suggested_term " + sortOrder);
                }
               
                if (pageNum != 0) {
                    limitFrom = (pageNum - 1) * 10;
                    limitTo = (pageNum) * 10;
                    query.append(" limit " + limitFrom + " , " + limitTo);
                }
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
                hibQuery.addScalar("TERM_BEING_POLLED", Hibernate.STRING);
                hibQuery.addScalar("TERM_STATUS_ID", Hibernate.INTEGER);
                hibQuery.addScalar("EXPIRED_DATE", Hibernate.STRING);
                Object[] termsData = hibQuery.list().toArray();
                if ((termsData != null) && termsData.length > 0) {
                    for (int i = 0; i < termsData.length; i++) {
                        Object[] termsDataVal = (Object[]) termsData[i];
                        PollTerms term = new PollTerms();
                        term.setTermId((Integer) termsDataVal[0]);
                        term.setSourceTerm((String) termsDataVal[1]);
                        Integer statusId= (Integer) termsDataVal[2];
                        if(statusId == 2)
                         term.setStatus("Approved");
                        term.setPollExpirationDt((String) termsDataVal[3]);
                        userPollTerms.add(term);
                    }
                }

                logger.debug(" Total user poll terms :" + userPollTerms.size());
                return userPollTerms;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To vote a term, by a user
     *
     * @param termTranslation TermTranslation to count on vote
     * @param userId          Integer to set in updatedBy field
     * @return If term is voted it returns success else failed
     */
    @Override
    @Transactional
    public String voteTerm(TermTranslation termTranslation, Integer userId)
            throws DataAccessException {
        String status = "";
        if (termTranslation == null) {
            status = "failed";
            return null;
        }

        try {
            Integer termId = termTranslation.getTermId();
            Integer termTranslationId = 0;
            TermTranslation termTranslationDetails = null;
            TermVoteUserDetails termVoteDetails;
             Session session = getHibernateSession();
            if (termTranslation.getTermTranslationId() != null) {
                termTranslationId = termTranslation.getTermTranslationId();
                termTranslationDetails = (TermTranslation) session.get(TermTranslation.class, termTranslationId);
                if (termTranslationDetails != null) {
                    int vote = (termTranslationDetails.getVote() == null) ? 0 : termTranslationDetails.getVote();
                    termTranslationDetails.setVote(++vote);
                    termTranslationDetails.setUpdateDate(new Date());
                    termTranslationDetails.setUpdatedBy(userId);
                    getHibernateTemplate().update(termTranslationDetails);
                    logger.debug("Updated term translation data successfully for termId :: " + termId);
                } else {
                    logger.debug("Failed to update term translation data for termID :: " + termId);
                }

            } else {
                Criteria crit = session.createCriteria(TermTranslation.class);
                crit.add(Restrictions.eq("termId", termId));
                List<TermTranslation> termTranslationList = (List<TermTranslation>) crit.list();
                boolean isExists = false;
                for (TermTranslation termTrans : termTranslationList) {
                    if (termTranslation.getSuggestedTerm().equalsIgnoreCase(termTrans.getSuggestedTerm())) {
                        isExists = true;
                        termTranslationId = termTrans.getTermTranslationId();
                        int vote = (termTrans.getVote() == null) ? 0 : termTrans.getVote();
                        termTrans.setVote(++vote);
                        termTrans.setUpdateDate(new Date());
                        termTrans.setUpdatedBy(userId);
                        termTrans.setIsActive("Y");
                        getHibernateTemplate().update(termTrans);
                        break;
                    }
                }
                if (!isExists) {
                    termTranslation.setUserId(userId);
                    termTranslation.setVote(1);
                    termTranslation.setCreateDate(new Date());
                    termTranslation.setIsActive("Y");
                    termTranslationId = (Integer) getHibernateTemplate().save(termTranslation);
                }

                logger.debug("Added new suggested term details successfully");
            }

            termVoteDetails = getTermVoteUserDetails(termId, userId);
            if (termVoteDetails != null) {
                termVoteDetails.setTermTranslationId(termTranslationId);
                termVoteDetails.setVotingDate(new Date());
                termVoteDetails.setUpdatedBy(userId);
                termVoteDetails.setUpdateDate(new Date());
                if (termTranslation.getComment() != null) {
                	if(termTranslation.getComment().length() > 500)
                		termVoteDetails.setComments(termTranslation.getComment().substring(0, 500));
                	else
                		termVoteDetails.setComments(termTranslation.getComment());
                }
                termVoteDetails.setVoteInviteStatus("Y");
                termVoteDetails.setIsActive("Y");
                getHibernateTemplate().update(termVoteDetails);
                status = "correct";
                logger.debug(" Term Vote User Details is succesfully saved");
            } else {
                status = "failed";
                logger.debug("No Vote details found for user : " + userId);
            }

            //Commenting code because when user vote on traslation, top suggestion is changing wrongly.
           /* Criteria hibCriteria1 = session.createCriteria(TermTranslation.class);
            hibCriteria1.add(Restrictions.eq("termId", termId));
            hibCriteria1.add(Restrictions.isNotNull("vote"));
            ProjectionList proList = Projections.projectionList();
            proList.add(Projections.property("suggestedTerm"));
            proList.add(Projections.max("vote"));
            hibCriteria1.setProjection(proList);
            Object[] termsData = hibCriteria1.list().toArray();
            if ((termsData != null) && termsData.length > 0) {
                Object[] termsDataVal = (Object[]) termsData[0];

                TermInformation termInfo = (TermInformation) session.get(TermInformation.class, termId);
                termInfo.setSuggestedTerm((String) termsDataVal[0]);
                termInfo.setDeprecatedTermInfo(null);
                termInfo.setIsActive("Y");
                getHibernateTemplate().update(termInfo);
            }*/

        }
        catch (Exception e) {
            logger.debug("Failed to save term vote user details");
            status = "failed";
            logger.error(e);
        }

        return status;


    }


    /**
     * To reject a term, by a user
     *
     * @param termId termId to be rejected
     * @param userId userId that rejects the term
     */
    @Override
    @Transactional
    public void rejectTerm(Integer termId, Integer userId) {
        if (termId == null) {
            throw new IllegalArgumentException("Invalid termDetails");
        }

        try {
             Session session = getHibernateSession();
            Criteria hibCriteria = session
                    .createCriteria(TermVoteUserDetails.class);
            hibCriteria.add(Restrictions.eq("termId", termId));
            hibCriteria.add(Restrictions.eq("userId", userId));
            TermVoteUserDetails rejectedTermDetails = (TermVoteUserDetails) hibCriteria
                    .uniqueResult();
            if (rejectedTermDetails != null) {
                rejectedTermDetails.setUpdatedBy(userId);
                rejectedTermDetails.setUpdateDate(new Date());
                rejectedTermDetails.setVoteInviteStatus("N");
                rejectedTermDetails.setIsActive("Y");
                getHibernateTemplate().update(rejectedTermDetails);
                logger.debug("User rejected term: " + termId + " successfully.");
            } else {
                logger.debug("No Vote details found for user : " + userId);
            }

        }
        catch (Exception e) {
            logger.debug("Failed to rejected the term");
        }
    }

    /**
     * Add new term.
     *
     * @param termInformation TermInformation that needs to be added
     * @return If term is voted it returns success else failed
     */
    @Transactional
    @SuppressWarnings("unused")
    public String addNewTerm(TermInformation termInformation) {
        try {
            String status = "";
            Integer companyId = null;
            TermInformation updateTermInformation = new TermInformation();
            TermInformation updateTargetTermInformation = null;
            boolean sourceExits = false;
            boolean sourcePOSExists=false;
            boolean sourceDomainExists=false;
            boolean sourceCategoryExists=false;
            
            Integer countOfTerms = 0;
            if (termInformation == null) {
                status = "failed";
                return status;
            }
            String sourceTerm = termInformation == null ? null : termInformation.getTermBeingPolled();
            String targetTerm = termInformation == null ? null : termInformation.getSuggestedTerm();
            if (sourceTerm != null)
                termInformation.setTermBeingPolled(sourceTerm.trim());
            if (targetTerm != null)
                termInformation.setSuggestedTerm(targetTerm.trim());
            if (termInformation.getTermStatusId() == null) {
                termInformation.setTermStatusId(1);
            }
            termInformation.setIsActive("Y");
           if (sourceTerm != null
                    && termInformation.getSuggestedTermLangId() != null && termInformation.getIsTM() == null) {

                Integer domainId = null;
                Integer formId = null;
                Integer posId = null;
                Integer catId = null;
                if (termInformation.getTermCompany() != null) {
                    companyId = termInformation.getTermCompany().getCompanyId();
                }
                TermInformation termInfo =  isTermBaseAttrbutesExists(termInformation);
                
                if (termInfo == null) {
                	Integer   termId = (Integer) getHibernateTemplate().save(termInformation);
                    TermInformation termSaved = null;

                    if (termId != null && termId != 0
                            && termInformation.getSuggestedTerm() != null) {
                        TermTranslation newSuggestedTerm = new TermTranslation();
                        newSuggestedTerm.setTermId(termId);
                        newSuggestedTerm.setSuggestedTerm(termInformation
                                .getSuggestedTerm());
                        newSuggestedTerm.setCreateDate(termInformation.getCreateDate());
                        newSuggestedTerm.setUserId(termInformation.getCreatedBy());
                        newSuggestedTerm.setComment(termInformation.getComments());
                        newSuggestedTerm.setSuggestedTermLangId(termInformation
                                .getSuggestedTermLangId());
                        newSuggestedTerm.setIsActive("Y");
                        getHibernateTemplate().save(newSuggestedTerm);
                    }
                    status = "success";
                    logger.debug("Successfully added new term");
                } else {
                        updateTargetTermInformation = isTargetTermBaseExists(termInformation);
                        if (updateTargetTermInformation == null) {
                            Integer termId = termInfo.getTermId();
                            termInfo.setComments(termInformation.getComments());
                            termInfo.setConceptCatId(termInformation.getConceptCatId());
                            termInfo.setConceptDefinition(termInformation.getConceptDefinition());
                            termInfo.setConceptProdGroup(termInformation.getConceptProdGroup());
                            termInfo.setCreateDate(termInformation.getCreateDate());
                            termInfo.setCreatedBy(termInformation.getCreatedBy());
                            termInfo.setIsActive(termInformation.getIsActive());
                            termInfo.setPhotoPath(termInformation.getPhotoPath());
                            termInfo.setSuggestedTerm(termInformation.getSuggestedTerm());
                            termInfo.setSuggestedTermFormId(termInformation.getSuggestedTermFormId());
                            termInfo.setSuggestedTermLangId(termInformation.getSuggestedTermLangId());
                            termInfo.setSuggestedTermFormId(termInformation.getSuggestedTermFormId());
                            termInfo.setSuggestedTermNotes(termInformation.getSuggestedTermNotes());
                            termInfo.setSuggestedTermPgmId(termInformation.getSuggestedTermPgmId());
                            termInfo.setSuggestedTermPosId(termInformation.getSuggestedTermPosId());
                            termInfo.setSuggestedTermStatusId(termInformation.getSuggestedTermStatusId());
                            termInfo.setSuggestedTermUsage(termInformation.getSuggestedTermUsage());
                            termInfo.setTermBeingPolled(termInformation.getTermBeingPolled());
                            termInfo.setTermCategory(termInformation.getTermCategory());
                            termInfo.setTermCompany(termInformation.getTermCompany());
                            termInfo.setTermDomain(termInformation.getTermDomain());
                            termInfo.setTermForm(termInformation.getTermForm());
                            termInfo.setTermLangId(termInformation.getTermLangId());
                            termInfo.setTermNotes(termInformation.getTermNotes());
                            termInfo.setTermPOS(termInformation.getTermPOS());
                            termInfo.setTermProgram(termInformation.getTermProgram());
                            termInfo.setTermStatusId(termInformation.getTermStatusId());
                            termInfo.setTermUsage(termInformation.getTermUsage());
                            termInfo.setUpdateDate(new Date());
                            termInfo.setUpdatedBy(termInformation.getUpdatedBy());

                            Set<DeprecatedTermInformation> deprecatedTermInfoset = new HashSet<DeprecatedTermInformation>();
                            Set<DeprecatedTermInformation> deprecatedTermInfosetNew = new HashSet<DeprecatedTermInformation>();
                            if (termInformation.getDeprecatedTermInfo() != null) {
                                deprecatedTermInfosetNew = termInformation.getDeprecatedTermInfo();
                                for (DeprecatedTermInformation deprecatedTermInformation : deprecatedTermInfosetNew) {
                                    boolean isExists = false;
                                    deprecatedTermInfoset = termInfo.getDeprecatedTermInfo();
                                    if (deprecatedTermInfoset != null) {
                                        for (DeprecatedTermInformation deprecatedTermInfo : deprecatedTermInfoset) {
                                            if ((deprecatedTermInfo.getDeprecatedSource() != null && deprecatedTermInformation.getDeprecatedSource() != null) && (deprecatedTermInfo.getDeprecatedTarget() != null && deprecatedTermInformation.getDeprecatedTarget() != null)) {
                                                if (deprecatedTermInfo.getDeprecatedSource().equalsIgnoreCase(deprecatedTermInformation.getDeprecatedSource())
                                                        && (deprecatedTermInfo.getDeprecatedTarget().equalsIgnoreCase(deprecatedTermInformation.getDeprecatedTarget()))) {
                                                    isExists = true;
                                                    break;

                                                }
                                            }
                                        }
                                    }
                                    if (!isExists) {
                                        deprecatedTermInformation.setTermInfo(termInfo);
                                        deprecatedTermInformation.setIsActive("Y");
                                        deprecatedTermInformation.setCreateDate(new Date());
                                        getHibernateTemplate().save(deprecatedTermInformation);
                                    }
                                    termInformation.setDeprecatedTermInfo(null);
                                }

                            }

                            termInfo.setDeprecatedTermInfo(null);
                            getHibernateTemplate().update(termInfo);
                            TermInformation termSaved = null;
                            Integer  tranId= null;
                            TermTranslation tarnObj = null;
                            if(termId != null){
                            	 Session session = getHibernateSession();
                                Criteria crit = session.createCriteria(TermTranslation.class);
                                crit.add(Restrictions.eq("termId", termId));
                                crit.add(Restrictions.not(Restrictions.eq("isActive", "N")));
                               /* ProjectionList proList = Projections.projectionList();
                                proList.add(Projections.property("termTranslationId"));
                                crit.setProjection(proList);*/
                                List list = crit.list();
                                Iterator it = list.iterator();
                                while(it.hasNext()){
                                	 tarnObj=(TermTranslation)it.next();
                                	//tranIds.add(tranId);
                                }
                            }
                            if (termId != null && termId != 0
                                    && termInformation.getSuggestedTerm() != null) {
                                TermTranslation newSuggestedTerm = new TermTranslation();
                                newSuggestedTerm.setTermTranslationId(tarnObj.getTermTranslationId());
                                newSuggestedTerm.setTermId(termId);
                                newSuggestedTerm.setSuggestedTerm(termInformation
                                        .getSuggestedTerm());
                                newSuggestedTerm.setCreateDate(termInformation.getCreateDate());
                                newSuggestedTerm.setUserId(termInformation.getCreatedBy());
                                newSuggestedTerm.setComment(termInformation.getComments());
                                newSuggestedTerm.setSuggestedTermLangId(termInformation
                                        .getSuggestedTermLangId());
                                newSuggestedTerm.setIsActive("Y");
                                getHibernateTemplate().merge(newSuggestedTerm);
                            }
                            status = "update";
                            logger.debug("Successfully Updated term");
                        } else {
                            status = "failedterm";
                            logger.debug(" Term Already Exists");
                        }

                }
           }
            return status;
        }
        catch (Exception e) {
            logger.error("Error in addNewTerm().", e);
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * To delete deprecated term info by termId
     *
     * @param termId An Integer to  filter the  terms
     */
    @Transactional
    private void deleteDeprecatedTerminfo(Integer termId) {
        DeprecatedTermInformation deprecatedTermInfo = new DeprecatedTermInformation();

         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(DeprecatedTermInformation.class);

        hibCriteria.add(Restrictions.eq("termId.termId", termId));
        logger.debug("Got attributes for termId :" + termId);
        deprecatedTermInfo = (DeprecatedTermInformation) hibCriteria.uniqueResult();
        if (deprecatedTermInfo != null) {
            deprecatedTermInfo.setIsActive("N");
        }
        getHibernateTemplate().update(deprecatedTermInfo);
    }

    /**
     * To check whether SourceTerm exists or not in database
     *
     * @param sourceTerm An String  to  filter the  terms
     * @param companyId  An Integer to  filter the  terms
     * @return Returns true if sourceTerm exists else it returns false
     */
    @Transactional
    private boolean isSourceTermBaseExists(String sourceTerm, Integer companyId, Integer targetId) {
         Session session = getHibernateSession();
        Boolean isExists = false;
        Criteria criteria = session.createCriteria(TermInformation.class);
        criteria.add(Restrictions.eq("termBeingPolled", sourceTerm));
        criteria.add(Restrictions.eq("suggestedTermLangId", targetId));
        if (companyId != null) {
            criteria.add(Restrictions.eq("termCompany.companyId", companyId));
        }
        criteria.add(Restrictions.eq("isActive", "Y"));
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("termBeingPolled"));
        criteria.setProjection(proList);
        List<String> list = criteria.list();
        Iterator<String> it = list.iterator();
        List<String> sourceTermList = new ArrayList<String>();
        while (it.hasNext()) {
        	String term =(String) it.next();
            sourceTermList.add(term);
        }
        if (sourceTermList != null && sourceTermList.size() > 0) {
            for (String source : sourceTermList) {
                if (sourceTerm.equals(source)) {
                    isExists = true;
                }
            }
        }
        return isExists;
    }

    /**
     * Add new termXliff.
     *
     * @param termInformation TermInformation that needs to be added
     * @return If term is added it returns success else failed
     */
    public String addNewTermXliff(TermInformation termInformation) {
        String status = "";
        boolean sourceExits = false;
        if (termInformation == null) {
            status = "failed";
            return status;
        }
        String sourceTerm = termInformation.getTermBeingPolled();
        String targetTerm = termInformation.getSuggestedTerm();

        if (sourceTerm != null)
            termInformation.setTermBeingPolled(sourceTerm.trim());
        if (targetTerm != null)
            termInformation.setSuggestedTerm(targetTerm.trim());

        termInformation.setTermStatusId(1);
        termInformation.setIsActive("Y");
        if (sourceTerm != null
                && termInformation.getSuggestedTermLangId() != null) {
            Integer companyId = null;
            if (termInformation.getTermCompany() != null) {
                companyId = termInformation.getTermCompany().getCompanyId();
            }
            sourceExits = ifSourceExists(sourceTerm, termInformation.getSuggestedTermLangId(), companyId);
        }
        if (!sourceExits) {
            getHibernateTemplate().save(termInformation);
            status = "success";
            logger.debug("Successfully added new term");
        } else {
            status = "failedterm";
            logger.debug(" Term Already Exists");
        }

        return status;
    }

    /**
     * Invite to vote
     *
     * @param termVoteMaster Invitation that need to be saved
     * @param invite         Includes term id's that needs to be invited and the user id's
     */

    @Override
    @Transactional
    public void inviteToVote(TermVoteMaster termVoteMaster, Invite invite) {
    	logger.info("++++ inside inviteToVote method");
        Integer[] termIds = invite.getTermIds();
        Integer[] userIds = invite.getUserIds();
        List<TermInformation> termInformation = getTermInfoByTermIds(termIds);
        if (userIds != null) {
            List<User> usrList = getUsersByuserIdsList(userIds);
            logger.info("+++++++for loop to iterate user from userList");
            for (User user : usrList) {
                Integer userId = user.getUserId();
                List<Language> userLanguagesList = getUserRegLanguages(userId);
                if (userId != null) {
                	 logger.info("++++for loop to iterate term from termList");
                    for (TermInformation termInfo : termInformation) {
                        Integer termId = termInfo.getTermId();
                        if (termInfo.getTermCompany() != null) {
                            List<CompanyTransMgmt> companyTransMgmtList = getCompanyTransMgmtUsers(userId);
                            logger.info("+++getting list of companyTransMgmtList");
                            for (CompanyTransMgmt companyTransMgmt : companyTransMgmtList) {
                            	 logger.info("+++itrateting from companyTransMgmtList");
                                if (termInfo.getTermCompany().getCompanyId().intValue() == companyTransMgmt.getCompanyId().intValue()) {
                                    Integer langId = termInfo.getSuggestedTermLangId();
                                    if (userLanguagesList != null && userLanguagesList.size() > 0) {
                                        for (Language userLang : userLanguagesList) {
                                        	 logger.info("+++checking of userlanguage and termsuggestedlang");
                                            if (userLang.getLanguageId() != null && langId != null) {
                                                if (userLang.getLanguageId().intValue() == langId.intValue()) {
                                                	 logger.info("+++ userlanguage and termsuggestedlang is equal");
                                                    termVoteMaster.setTermId(termId);
                                                     Session session = getHibernateSession();
                                                    TermVoteMaster termSaved = new TermVoteMaster();

                                                    Criteria criteria = session.createCriteria(TermVoteMaster.class);
                                                    criteria.add(Restrictions.eq("termId", termId));
                                                    criteria.setProjection(Projections.count("termVoteId"));
                                                    Integer termCount = (Integer) criteria.uniqueResult();

                                                    if (termCount > 0) {
                                                    	logger.info("+++ updating temvotemaster table");
                                                        Criteria hibCriteria = session
                                                                .createCriteria(TermVoteMaster.class);
                                                        hibCriteria.add(Restrictions.eq("termId", termId));
                                                        termSaved = (TermVoteMaster) hibCriteria.uniqueResult();
                                                        termSaved.setVotingExpiredDate(termVoteMaster
                                                                .getVotingExpiredDate());
                                                        termSaved.setInvitedBy(termVoteMaster.getInvitedBy());
                                                        termSaved.setUpdateDate(termVoteMaster.getInvitedDate());
                                                        termSaved.setUpdatedBy(termVoteMaster.getUpdatedBy());
                                                        termSaved.setIsActive("Y");
                                                        getHibernateTemplate().update(termSaved);
                                                    } else {
                                                    	logger.info("+++ inserting in termvotemaster table");
                                                    	TermVoteMaster termVoteMasterObj = new TermVoteMaster();
                                                    	termVoteMasterObj.setInvitedBy(termVoteMaster.getInvitedBy());
                                                    	termVoteMasterObj.setVotingExpiredDate(termVoteMaster.getVotingExpiredDate());
                                                    	termVoteMasterObj.setUpdateDate(termVoteMaster.getUpdateDate());
                                                    	termVoteMasterObj.setIsActive("Y");
                                                    	termVoteMasterObj.setTermId(termId);
                                                    	termVoteMasterObj.setInvitedDate(termVoteMaster.getInvitedDate());
                                                    	
                                                    	
                                                        getHibernateTemplate().save(termVoteMasterObj);
                                                        
                                                        Criteria hibCriteria = session
                                                                .createCriteria(TermVoteMaster.class);
                                                        hibCriteria.add(Restrictions.eq("termId", termId));
                                                        termSaved = (TermVoteMaster) hibCriteria.uniqueResult();
                                                    }

                                                    Criteria criteria1 = session
                                                            .createCriteria(TermVoteUserDetails.class);
                                                    criteria1.add(Restrictions.eq("termId", termId));
                                                    criteria1.add(Restrictions.eq("userId", userId));
                                                    criteria1.setProjection(Projections.count("termVoteUsrDtlsId"));
                                                    Integer termUserCount = (Integer) criteria1.uniqueResult();

                                                    if (termUserCount == 0) {
                                                    	logger.info("+++ updating TermVoteUserDetails table");
                                                        TermVoteUserDetails termVoteUserDtls = new TermVoteUserDetails();
                                                        termVoteUserDtls.setTermId(termId);
                                                        termVoteUserDtls.setUserId(userId);
                                                        termVoteUserDtls
                                                                .setTermVoteId(termSaved.getTermVoteId());
                                                        termVoteUserDtls.setIsActive("Y");
                                                        getHibernateTemplate().save(termVoteUserDtls);
                                                    }

                                                    logger.debug("Successfully invited term :" + termId);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

    }

    /**
     * To manage poll terms for a language Id
     *
     * @param queryAppender Which is used to build a query
     * @return List of PollTerms obj's
     */
    @Override
   // @Transactional
    public List<PollTerms> getManagePollTerms(final QueryAppender queryAppender, final Integer companyId) {
        HibernateCallback<List<PollTerms>> callback = new HibernateCallback<List<PollTerms>>()
        {

            @SuppressWarnings("deprecation")
            @Override
            public List<PollTerms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                int limitFrom = 0;
                int limitTo = 0;
                String filtertBy = (queryAppender.getFilterBy() == null) ? ""
                        : queryAppender.getFilterBy();
                String filtertByCompany = (queryAppender.getFilterByCompany() == null) ? ""
                        : queryAppender.getFilterByCompany();
                Integer[] intSelectedIds = queryAppender.getSelectedIds();
                Integer[] intSelectedCompanyIds = queryAppender.getSelectedCompanyIds();
                String selectedIds = "";
                String selectedCompanyIds = "";
                if (intSelectedIds != null && intSelectedIds.length != 0) {
                    for (int i = 0; i < intSelectedIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedIds += separator + intSelectedIds[i];
                    }

                }

                if (intSelectedCompanyIds != null && intSelectedCompanyIds.length != 0) {
                    for (int i = 0; i < intSelectedCompanyIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedCompanyIds += separator + intSelectedCompanyIds[i];
                    }

                }
                selectedIds = selectedIds.trim();


                String likeValue = "";
                String termSourcelikeValue = "";
                String termTargetlikeValue = "";
                String depSourcelikeValue = "";
                String depTarglikeValue = "";
                String searchBy = queryAppender.getSearchStr();
                String colName = (queryAppender.getColName() == null) ? ""
                        : queryAppender.getColName();
                String sortOrder = (queryAppender.getSortOrder() == null) ? ""
                        : queryAppender.getSortOrder();
                Integer pageNum = queryAppender.getPageNum();
                String isTM = queryAppender.getIsTM();
                StringBuffer query = new StringBuffer();
                List<PollTerms> userPollTerms = new ArrayList<PollTerms>();
                query.append(GET_MANAGE_POLL_TERMS);
                if (companyId != null) {
                    query.append(" and ti.company_id = " + companyId + " ");
                }
                if (searchBy != null && !searchBy.equalsIgnoreCase("null")) {

                    if (searchBy.indexOf('*') == 0) {
                        searchBy = searchBy.replace("*", LIKE_PERCENT);
                    } else {
                        searchBy = searchBy.replace("*", LIKE_UNDERSCORE);
                    }

                    if (searchBy.contains(" ")) {
                        StringTokenizer st = new StringTokenizer(searchBy, " ");
                        if (queryAppender.isCaseFlag()) {

                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                termSourcelikeValue = termSourcelikeValue
                                        + " ti.term_being_polled like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";
                                termTargetlikeValue = termTargetlikeValue
                                        + " ti.suggested_term like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";
                                depSourcelikeValue = depSourcelikeValue
                                        + " dt.deprecated_source like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";
                                depTarglikeValue = depTarglikeValue
                                        + " dt.deprecated_target like _utf8 ' "
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";

                            }
                        } else {

                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                termSourcelikeValue = termSourcelikeValue
                                        + " ti.term_being_polled like _utf8 '"
                                        + searchValue + "%'" + " or ";
                                termTargetlikeValue = termTargetlikeValue
                                        + " ti.suggested_term like _utf8 '"
                                        + searchValue + "%'" + " or ";
                                depSourcelikeValue = depSourcelikeValue
                                        + " dt.deprecated_source like _utf8 '"
                                        + searchValue + "%'" + " or ";
                                depTarglikeValue = depTarglikeValue
                                        + " dt.deprecated_target like _utf8 ' "
                                        + searchValue + "%'" + " or ";

                            }
                        }
                        if (termSourcelikeValue.endsWith(" or ")) {
                            termSourcelikeValue = termSourcelikeValue
                                    .substring(0, termSourcelikeValue
                                            .lastIndexOf("or"));
                        }
                        if (termTargetlikeValue.endsWith(" or ")) {
                            termTargetlikeValue = termTargetlikeValue
                                    .substring(0, termTargetlikeValue
                                            .lastIndexOf("or"));
                        }
                        if (depSourcelikeValue.endsWith(" or ")) {
                            depSourcelikeValue = depSourcelikeValue.substring(
                                    0, depSourcelikeValue.lastIndexOf("or"));
                        }
                        if (depTarglikeValue.endsWith(" or ")) {
                            depTarglikeValue = depTarglikeValue.substring(0,
                                    depTarglikeValue.lastIndexOf("or"));
                        }

                        query.append("and (( " + termSourcelikeValue + "or"
                                + termTargetlikeValue + ") or ( "
                                + depSourcelikeValue + "or" + depTarglikeValue);
                        query.append("))");
                    } else {
                        likeValue = ((searchBy.indexOf(LIKE_UNDERSCORE) == -1) || (searchBy
                                .indexOf(LIKE_UNDERSCORE) == searchBy
                                .lastIndexOf('_'))) ? (searchBy + LIKE_PERCENT)
                                : (searchBy + " " + LIKE_PERCENT);

                        likeValue = "'" + likeValue + "'";

                        query.append(" and ((ti.term_being_polled like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(" or ti.suggested_term like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(")");

                        query.append(" or ( dt.deprecated_source like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }

                        query.append(" or dt.deprecated_target like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }

                        query.append("))");
                    }
                }

                if (filtertBy.equalsIgnoreCase("Locale")) {
                    if (selectedIds != "" && selectedIds != null) {
                        query.append(" and ti.suggested_term_lang_id in ( "
                                + selectedIds + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Part of Speech")) {
                    if (selectedIds != "" && selectedIds != null) {
                        query.append("  and  ti.term_pos_id in ( " + selectedIds
                                + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Category")) {
                    if (selectedIds != "" && selectedIds != null) {

                        query.append(" and  ti.term_category_id in ( "
                                + selectedIds + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Final")) {
                    query.append(" and  ti.term_status_id = "
                            + APPROVED_STATUS_ID + " ");
                } else if (filtertBy.equalsIgnoreCase("Poll Expiration")) {
                    query.append(" and  ti.term_id in  (select term_id from term_vote_master where voting_expired_date <= curdate() ) ");
                } else if (filtertBy.equalsIgnoreCase("Company")) {
                    if (selectedIds != "" && selectedIds != null) {

                        query.append(" and  ti.company_id in ( " + selectedIds
                                + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Domain")) {
                    if (selectedIds != "" && selectedIds != null) {
                        query.append(" and  ti.domain_id in ( " + selectedIds
                                + " ) ");
                    }
                }
                if (selectedCompanyIds != null && selectedCompanyIds != "") {
                    if (filtertByCompany.equalsIgnoreCase("Company")) {
                        query.append(" and  ti.company_id in ( " + selectedCompanyIds
                                + " ) ");
                    }
                }

                query.append(" group by ti.term_id ");

                if (colName.equalsIgnoreCase("domain")) {
                    query.append(" order by domain.domain " + sortOrder);
                } else if (colName.equalsIgnoreCase("company")) {
                    query.append(" order by company.company_name " + sortOrder);
                } else if (colName.equalsIgnoreCase("category")) {
                    query.append(" order by cat.category " + sortOrder);
                } else if (colName.equalsIgnoreCase("pollExpirationDate")) {
                    query.append(" order by tvm.voting_expired_date "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("targetTerm")) {
                    query.append(" order by ti.term_being_polled " + sortOrder);
                } else if (colName.equalsIgnoreCase("suggestedTerm")) {
                    query.append(" order by ti.suggested_term " + sortOrder);
                } else if (colName.equalsIgnoreCase("status")) {
                    query.append(" order by status.status " + sortOrder);
                } else if (colName.equalsIgnoreCase("language")) {
                    query.append(" order by lang.language_label " + sortOrder);
                } else if (colName.equalsIgnoreCase("POS")) {
                    query.append(" order by pos.part_of_speech " + sortOrder);
                } else {
                    query.append(" order by ti.create_date desc ");
                }

                if (pageNum != 0) {
                    limitFrom = (pageNum - 1) * 10;
                    limitTo = (pageNum) * 10;
                    query.append(" limit " + limitFrom + " , " + limitTo);
                }

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
                hibQuery.addScalar("TERM_BEING_POLLED", Hibernate.STRING);
                hibQuery.addScalar("SUGGESTED_TERM", Hibernate.STRING);
                hibQuery.addScalar("PART_OF_SPEECH", Hibernate.STRING);
                hibQuery.addScalar("CATEGORY", Hibernate.STRING);
                hibQuery.addScalar("DOMAIN", Hibernate.STRING);
                hibQuery.addScalar("COMPANY_NAME", Hibernate.STRING);
                hibQuery.addScalar("LANGUAGE_LABEL", Hibernate.STRING);
                hibQuery.addScalar("STATUS", Hibernate.STRING);
                hibQuery.addScalar("EXPIRED_DATE", Hibernate.STRING);
                hibQuery.addScalar("ALL_VOTES", Hibernate.INTEGER);
                hibQuery.addScalar("INVITES", Hibernate.INTEGER);
                hibQuery.addScalar("IS_DEPRECATE", Hibernate.INTEGER);
                Object[] termsData = hibQuery.list().toArray();
                if ((termsData != null) && termsData.length > 0) {
                    for (int i = 0; i < termsData.length; i++) {
                        Object[] termsDataVal = (Object[]) termsData[i];
                        PollTerms term = new PollTerms();
                        term.setTermId((Integer) termsDataVal[0]);

                        String sourceTerm = (String) termsDataVal[1];
                        if (searchBy != null && sourceTerm != null) {
                            String searchByValues[] = new String[1];
                            if (searchBy.contains(" ")) {
                                searchByValues = searchBy.split(" ");
                            } else {
                                searchByValues[0] = searchBy;
                            }
                            for (int j = 0; j < searchByValues.length; j++) {
                                Pattern pattern = Pattern.compile(
                                        searchByValues[j],
                                        Pattern.CASE_INSENSITIVE);
                                Matcher matcher = pattern.matcher(sourceTerm);
                                if (matcher.find()) {
                                    for (int k = 0; k <= matcher.groupCount(); k++) {

                                        sourceTerm = matcher
                                                .replaceFirst("<span class='selectedbg'>"
                                                        + matcher.group(k)
                                                        + "</span>");
                                    }
                                }
                            }

                        }
                        term.setSourceTerm(sourceTerm);

                        String targetTerm = (String) termsDataVal[2];
                        if (searchBy != null && targetTerm != null) {
                            String searchByValues[] = new String[1];
                            if (searchBy.contains(" ")) {
                                searchByValues = searchBy.split(" ");
                            } else {
                                searchByValues[0] = searchBy;
                            }
                            for (int j = 0; j < searchByValues.length; j++) {
                                Pattern pattern = Pattern.compile(
                                        searchByValues[j],
                                        Pattern.CASE_INSENSITIVE);
                                Matcher matcher = pattern.matcher(targetTerm);
                                if (matcher.find()) {
                                    for (int k = 0; k <= matcher.groupCount(); k++) {

                                        targetTerm = matcher
                                                .replaceFirst("<span class='selectedbg'>"
                                                        + matcher.group(k)
                                                        + "</span>");
                                    }
                                }
                            }
                        }
                        term.setSuggestedTerm(targetTerm);
                        term.setPartOfSpeech((String) termsDataVal[3]);
                        term.setCategory((String) termsDataVal[4]);
                        term.setDomain((String) termsDataVal[5]);
                        term.setCompany((String) termsDataVal[6]);
                        term.setLanguage((String) termsDataVal[7]);
                        term.setStatus((String) termsDataVal[8]);
                        term.setPollExpirationDt((String) termsDataVal[9]);
                        term.setVotesPerTerm((Integer) termsDataVal[10]);
                        term.setInvites((Integer) termsDataVal[11]);
                        term.setDeprecatedCount((Integer) termsDataVal[12]);
                        userPollTerms.add(term);
                    }
                }

                logger.debug(" Total manage poll terms :"
                        + userPollTerms.size());
                return userPollTerms;
            }

        };
        return getHibernateTemplate().execute(callback);
    }


    /**
     * To delete term ids
     *
     * @param termIds - Integer array that needs to be deleted
     */
    @Override
    @Transactional
    public void deleteTerms(final Integer[] termIds,User user) {
        if (termIds == null) {
            throw new IllegalArgumentException("Invalid termIds");
        }
        Integer companyId = null;
        if(user != null)
        	companyId  =user.getCompany().getCompanyId();
        TermInformation termInformation = new TermInformation();
        List<TermVoteUserDetails> termVoteUserDetailsList = new ArrayList<TermVoteUserDetails>();
        List<TermTranslation> termtranslationList = new ArrayList<TermTranslation>();
        Set<DeprecatedTermInformation> deprecatedTermInfoset = new HashSet<DeprecatedTermInformation>();

         Session session = getHibernateSession();
        for (int i = 0; i < termIds.length; i++) {
            Integer termId = termIds[i];
            Criteria hibCriteria = session
                    .createCriteria(TermInformation.class);
            hibCriteria.add(Restrictions.eq("termId", termId));
            if(companyId != null)
            hibCriteria.add(Restrictions.eq("termCompany.companyId", companyId));
            termInformation = (TermInformation) hibCriteria.uniqueResult();
            if (termInformation.getDeprecatedTermInfo() != null) {
                deprecatedTermInfoset = termInformation.getDeprecatedTermInfo();
                for (DeprecatedTermInformation deprecatedTermInformation : deprecatedTermInfoset) {
                    deprecatedTermInformation.setTermInfo(termInformation);
                    deprecatedTermInformation.setIsActive("N");
                    deprecatedTermInformation.setUpdateDate(new Date());
                    deprecatedTermInformation.setUpdatedBy(user.getUserId());
                    getHibernateTemplate().update(deprecatedTermInformation);
                }
                termInformation.setDeprecatedTermInfo(null);
            }
            termInformation.setIsActive("N");
            termInformation.setUpdateDate(new Date());
            termInformation.setUpdatedBy(user.getUserId());
            getHibernateTemplate().update(termInformation);
            logger.debug("Successfully deleted term id :" + termId);

            Criteria criteria = session
                    .createCriteria(TermVoteUserDetails.class);
            criteria.add(Restrictions.eq("termId", termId));
            logger.debug("Got term vote user details for termId :" + termId);
            termVoteUserDetailsList = (List<TermVoteUserDetails>) criteria
                    .list();
            if ((termVoteUserDetailsList != null)
                    && termVoteUserDetailsList.size() > 0) {
                for (int j = 0; j < termVoteUserDetailsList.size(); j++) {
                    TermVoteUserDetails termVoteUserDetails = termVoteUserDetailsList
                            .get(j);
                    termVoteUserDetails.setIsActive("N");
                    termVoteUserDetails.setUpdateDate(new Date());
                    termVoteUserDetails.setUpdatedBy(user.getUserId());
                    getHibernateTemplate().update(termVoteUserDetails);
                }
            }

            Criteria crit = session.createCriteria(TermTranslation.class);
            crit.add(Restrictions.eq("termId", termId));
            logger.debug("Got term translation details for termId :" + termId);
            termtranslationList = (List<TermTranslation>) crit.list();
            if ((termtranslationList != null) && termtranslationList.size() > 0) {
                for (int j = 0; j < termtranslationList.size(); j++) {
                    TermTranslation termTranslation = termtranslationList
                            .get(j);
                    termTranslation.setIsActive("N");
                    termTranslation.setUpdateDate(new Date());
                    termTranslation.setUpdatedBy(user.getUserId());
                    getHibernateTemplate().update(termTranslation);
                }
            }
        }

    }

    /**
     * To get termInformation
     *
     * @param exportBy    String to filter the terms
     * @param selectedIds Integer Array of filter values
     * @return List of terms
     */

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<TermInformation> getTermInformation(String exportBy, Integer[] selectedIds,Integer companyId) throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TermInformation.class);
        hibCriteria.add(Restrictions.not(Restrictions.eq("isActive", "N")));

        if (exportBy.equalsIgnoreCase("Locale")) {
            hibCriteria
                    .add(Restrictions.eq("termCompany.companyId", companyId));
            hibCriteria
            .add(Restrictions.in("suggestedTermLangId", selectedIds));
        } else if (exportBy.equalsIgnoreCase("Part of Speech")) {
        	hibCriteria
            .add(Restrictions.eq("termCompany.companyId", companyId));
            hibCriteria.add(Restrictions.in("termPOS.partsOfSpeechId",
                    selectedIds));
        } else if (exportBy.equalsIgnoreCase("Category")) {
        	hibCriteria
            .add(Restrictions.eq("termCompany.companyId", companyId));
            hibCriteria.add(Restrictions.in("termCategory.categoryId",
                    selectedIds));
        } else if (exportBy.equalsIgnoreCase("Final")) {
        	hibCriteria
            .add(Restrictions.eq("termCompany.companyId", companyId));
            hibCriteria
                    .add(Restrictions.eq("termStatusId", APPROVED_STATUS_ID));
        } else if (exportBy.equalsIgnoreCase("Poll Expiration")) {
        	hibCriteria
            .add(Restrictions.eq("termCompany.companyId", companyId));
            hibCriteria.add(Restrictions.in("termId", selectedIds));
        } else if (exportBy.equalsIgnoreCase("Company")) {
            hibCriteria.add(Restrictions.in("termCompany.companyId", selectedIds));
        } else if (exportBy.equalsIgnoreCase("Domain")) {
        	hibCriteria
            .add(Restrictions.eq("termCompany.companyId", companyId));
            hibCriteria.add(Restrictions.in("termDomain.domainId", selectedIds));
        }
        hibCriteria.addOrder(Order.asc("termBeingPolled"));
        return (List<TermInformation>) hibCriteria.list();
    }


    /**
     * To get termInformation
     *
     * @param exportBy    String to filter the terms
     * @param selectedIds Integer Array of filter values
     * @return List of terms
     */

    @Override
    @Transactional
    public List<TermInformation> getTermInformationTM(String exportBy,
                                                      Integer[] selectedIds) throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TermInformation.class);
        hibCriteria.add(Restrictions.not(Restrictions.eq("isActive", "N")));
        hibCriteria.add(Restrictions.eq("isTM", "Y"));

        if (exportBy.equalsIgnoreCase("Locale")) {
            hibCriteria
                    .add(Restrictions.in("suggestedTermLangId", selectedIds));
        } else if (exportBy.equalsIgnoreCase("Final")) {
            hibCriteria
                    .add(Restrictions.eq("termStatusId", APPROVED_STATUS_ID));
        } else if (exportBy.equalsIgnoreCase("Poll Expiration")) {
            hibCriteria.add(Restrictions.in("termId", selectedIds));
        }
        hibCriteria.addOrder(Order.asc("termBeingPolled"));
        return (List<TermInformation>) hibCriteria.list();
    }


    /**
     * To get top register languages
     *
     * @return List of top six registered languages
     */
    @Override
    //@Transactional
    public List<Language> getTopRegLangs(final Integer companyId) {
        HibernateCallback<List<Language>> callback = new HibernateCallback<List<Language>>()
        {

            @Override
            public List<Language> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Language> topRegLangs = new ArrayList<Language>();
                query.append(GET_TOP_REG_LANGS);
                if (companyId != null) {
                    query.append(" and usr.company_id = " + companyId);
                }
                query.append(" group by ul.language_id order by count(ul.language_id) desc, lang.language_label asc limit 0, 6 ");
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                Object[] regLangs = hibQuery.list().toArray();
                if ((regLangs != null) && regLangs.length > 0) {
                    for (int i = 0; i < regLangs.length; i++) {
                        Object[] regLangsVal = (Object[]) regLangs[i];
                        Language language = new Language();
                        language.setLanguageId((Integer) regLangsVal[0]);
                        language.setLanguageLabel((String) regLangsVal[1]);
                        language.setLanguageCode((String) regLangsVal[2]);
                        topRegLangs.add(language);

                    }

                }
                logger.debug("Got 6 top most registered languages");
                return topRegLangs;

            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To get list of termIds from term information by languageId
     *
     * @param languageId an Integer to be filtered
     * @return List of term id's
     */
    @Override
    @Transactional
    public List<Integer> getTermInformationByLanguage(Integer languageId, Integer companyId) {
        List<Integer> termInformationList = null;
         Session session = getHibernateSession();
        logger.info("in get termInformation by language " + languageId + ", " + companyId);
        if (languageId == null) {
            return termInformationList;
        }
        termInformationList = new ArrayList<Integer>();
        Criteria crit = session.createCriteria(TermInformation.class);
        if (companyId != null)
            crit.add(Restrictions.eq("termCompany.companyId", companyId));
        crit.add(Restrictions.and(
                Restrictions.eq("suggestedTermLangId", languageId),
                Restrictions.not(Restrictions.eq("isActive", "N"))));
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("termId"));
        crit.setProjection(proList);
        List list = crit.list();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object row = (Integer) it.next();
            Integer id = (Integer) row;
            termInformationList.add(id);
        }
        logger.info("Got the termInformationList :" + termInformationList);
        return termInformationList;

    }

    /**
     * To get list of termIds from term information by languageId and statusId
     *
     * @param languageId Integer to be filtered for terms
     * @param statusId   Integer to be filtered for terms
     * @param companyId  An Integer to be filtered
     * @return List of term id's
     */
    @Override
    @Transactional
    public List<Integer> getTermInformationByLanguageAndStatus(
            Integer languageId, Integer statusId, Integer companyId) {
        List<Integer> termInformationList = null;
         Session session = getHibernateSession();

        if (languageId == null) {
            return termInformationList;
        }
        termInformationList = new ArrayList<Integer>();
        Criteria crit = session
                .createCriteria(TermInformation.class);
        if (companyId != null) {
            crit.add(Restrictions.eq("termCompany.companyId", companyId));
        }
        crit.add(Restrictions.and(
                Restrictions.eq("suggestedTermLangId", languageId),
                Restrictions.not(Restrictions.eq("isActive", "N"))))
                .add(Restrictions.not(Restrictions.eq("termStatusId", statusId)));
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("termId"));
        crit.setProjection(proList);
        List list = crit.list();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object row = (Object) it.next();
            Integer id = (Integer) row;
            termInformationList.add(id);

        }
        logger.debug("Got the termInformationList :" + termInformationList);
        return termInformationList;

    }

    /**
     * To get list of term information by languageIds in a year
     *
     * @param languageIds Set of languageIds to be filtered
     * @return List of TermInformation in a year
     */
    @Override
    @Transactional
    public List<TermInformation> getTermInformationPerMonth(
            Set<Integer> languageIds, Integer companyId) {
        List<TermInformation> termInformationList = null;
         Session session = getHibernateSession();

        if (languageIds == null) {
            return termInformationList;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Calendar now = Calendar.getInstance();
        Date date = null;
        Date endDate = null;
        try {
            now.add(Calendar.MONTH, -11);
            date = formatter.parse((now.get(Calendar.MONTH) + 1) + "-" + 1
                    + "-" + now.get(Calendar.YEAR));
            now = Calendar.getInstance();
            endDate = now.getTime();
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        Criteria crit = session.createCriteria(TermInformation.class);
        if (companyId != null) {
            crit.add(Restrictions.eq("termCompany.companyId", companyId));
        }
        termInformationList = crit.add(Restrictions.in("suggestedTermLangId", languageIds))
                .add(Restrictions.not(Restrictions.eq("isActive", "N")))
                .add(Restrictions.gt("createDate", date))
                .add(Restrictions.lt("createDate", endDate))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .addOrder(Order.asc("createDate")).list();

        logger.debug("Got the termInformationList :" + termInformationList);
        return termInformationList;

    }

    /**
     * To get count of term information other than top 6 registered languages
     *
     * @param languageIds Set of languageIds to be filtered
     * @return An Integer value holding the count of term id's for languages
     *         other than top 6 registered
     */
    @Override
    @Transactional
    public Integer getTermInformationForOtherLanguage(Set<Integer> languageIds, Integer companyId) {
        Integer termCount = 0;
         Session session = getHibernateSession();
        try {
            if (languageIds == null) {
                return termCount;
            }
            Criteria hibCriteria = session
                    .createCriteria(TermInformation.class);
            if (companyId != null) {
                hibCriteria.add(Restrictions.eq("termCompany.companyId", companyId));
            }
            hibCriteria.add(Restrictions.not(Restrictions.in(
                    "suggestedTermLangId", languageIds)))
                    .add(Restrictions.not(Restrictions.eq("isActive", "N")))
                    .setProjection(Projections.count("termId"));
            termCount = (Integer) hibCriteria.uniqueResult();
            logger.debug("Count of Other languages : " + termCount);
            return termCount;
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }

        logger.debug("Count of Other languages : " + termCount);
        return termCount;
    }

    /**
     * To get list of debated terms in a year
     *
     * @param termIds Set of term id's that needs to be filtered
     * @return List of debated terms in a year
     */
    @Override
    @Transactional
    public List<TermVoteMaster> getTermVoteMasterByTermInfo(Set<Integer> termIds) {
        List<TermVoteMaster> termVoteMasterList = null;
         Session session = getHibernateSession();

        if (termIds == null) {
            return termVoteMasterList;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Calendar now = Calendar.getInstance();
        Date date = null;
        Date endDate = null;
        try {
            now.add(Calendar.MONTH, -11);
            date = formatter.parse((now.get(Calendar.MONTH) + 1) + "-" + 1
                    + "-" + now.get(Calendar.YEAR));
            endDate = formatter.parse(formatter.format(new Date()));
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        termVoteMasterList = session.createCriteria(TermVoteMaster.class)
                .add(Restrictions.in("termId", termIds))
                .add(Restrictions.gt("invitedDate", date))
                .add(Restrictions.lt("invitedDate", endDate))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .addOrder(Order.asc("invitedDate")).list();

        logger.debug("Got the termVoteMasterList :" + termVoteMasterList);
        return termVoteMasterList;

    }

    /**
     * To Get debated term ids
     *
     * @return Array of debated term id's
     */

    @Override
    @Transactional
    public Integer[] getDebatedTermIds() {

         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TermVoteMaster.class);
        hibCriteria.add(Restrictions.le("votingExpiredDate", new Date()));
        List<TermVoteMaster> termInfoList = (List<TermVoteMaster>) hibCriteria
                .list();
        Integer[] retrievedTermIds = new Integer[termInfoList.size()];
        if ((termInfoList != null) && termInfoList.size() > 0) {
            for (int i = 0; i < termInfoList.size(); i++) {
                TermVoteMaster termInfo = termInfoList.get(i);
                retrievedTermIds[i] = termInfo.getTermId();
            }
        }
        logger.debug("Got debated terms of : " + retrievedTermIds.length);
        return retrievedTermIds;
    }

    /**
     * To get total votes per language
     *
     * @param languageId An Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return An Integer which contains the count of total votes
     */
    @Override
    @Transactional
    public Integer getTotalVotesPerLang(Integer languageId, Integer companyId) {

         Session session = getHibernateSession();
        List<Integer> termIds = getTermInformationByLanguage(languageId, companyId);
        if (termIds == null || termIds.size() == 0)
            return 0;

        Criteria criteria = session.createCriteria(TermTranslation.class);
        criteria.add(Restrictions.in("termId", termIds));
        criteria.add(Restrictions.isNull("isActive"));
        criteria.setProjection(Projections.sum("vote"));
        Integer totalVotes = (Integer) criteria.uniqueResult();
        logger.debug("Got total votes per language : " + totalVotes);
        return totalVotes;

    }

    /**
     * To get monthly votes per language
     *
     * @param languageId An Integer to be filtered
     * @param companyId  An Integer to be filtered
     * @return An Integer which contains the count of votes in a month
     */
    @Override
    public Integer getMonthlyVotesPerLang(final String languageId, final Integer companyId) {
        HibernateCallback<Integer> callback = new HibernateCallback<Integer>()
        {

            @Override
            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                query.append(GET_MONTHLY_VOTES_PER_LANG);
                if (companyId != null) {
                    query.append(" and company_id=" + companyId);
                }
                query.append(")");
                SQLQuery hibQuery = session.createSQLQuery(query.toString());

                hibQuery.setParameter("languageId", languageId);
                Integer monthlyVotesPerLang = new Integer(hibQuery.list()
                        .get(0).toString());
                logger.debug("Got monthly votes per language : "
                        + monthlyVotesPerLang);
                return monthlyVotesPerLang;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To get Current Debated Terms
     *
     * @param termIds Set of term id's that needs to be filtered
     * @return List of debated terms from the given term id's
     */
    @Override
    @Transactional
    public List<TermVoteMaster> getCurrentDebatedTerms(Set<Integer> termIds) {
        List<TermVoteMaster> termVoteMasterList = null;
         Session session = getHibernateSession();

        if (termIds == null) {
            return termVoteMasterList;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date currDate = new Date();
        try {
            currDate = formatter.parse(formatter.format(currDate));
        }
        catch (Exception e) {
            logger.error("Error in formatting date in getting debated terms");
        }
        termVoteMasterList = session.createCriteria(TermVoteMaster.class)
                .add(Restrictions.in("termId", termIds))
                .add(Restrictions.ge("votingExpiredDate", currDate))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .addOrder(Order.asc("votingExpiredDate")).list();

        logger.debug("Got the termVoteMasterList :" + termVoteMasterList);
        return termVoteMasterList;

    }

    /**
     * To get Active Polls
     *
     * @param termIds Set of term id's that needs to be filtered
     * @return An Integer which contains the count of active invitations
     */

    @Override
    @Transactional
    public Integer getActivePolls(Set<Integer> termIds) {
         Session session = getHibernateSession();

        if (termIds == null) {
            return 0;
        }
        Criteria criteria = session
                .createCriteria(TermVoteUserDetails.class)
                .add(Restrictions.and(Restrictions.in("termId", termIds),
                        Restrictions.or(
                                Restrictions.eq("voteInviteStatus", "Y"),
                                Restrictions.isNull("voteInviteStatus"))))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setProjection(Projections.count("termVoteUsrDtlsId"));
        Integer countOfPolls = (Integer) criteria.uniqueResult();

        logger.debug("Got the Active Poll Terms :" + countOfPolls);
        return countOfPolls;

    }

    /**
     * To Get user voted terms for a language Id
     *
     * @param languageId String to filter terms respectively
     * @param colName    column name that has to be sorted
     * @param sortOrder  order in which it has to be sorted
     * @param pageNum    Integer to limit the data
     * @param userId     Integer to filter terms respectively
     * @return List of terms that are voted by the user
     */

    @Override
    //@Transactional
    public List<PollTerms> getUserVotedTerms(final String languageId,
                                             final String colName, final String sortOrder,
                                             final Integer pageNum, final Integer userId) {
        HibernateCallback<List<PollTerms>> callback = new HibernateCallback<List<PollTerms>>()
        {

            @Override
            public List<PollTerms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                int limitFrom = 0;
                int limitTo = 0;
                StringBuffer query = new StringBuffer();
                List<PollTerms> userVotedTerms = new ArrayList<PollTerms>();
                query.append(GET_USER_VOTED_TERMS);
                query.append(userId + "  and ti.suggested_term_lang_id = "
                        + languageId);
                query.append(" group by ti.term_id ");
                if(colName == null || colName.equalsIgnoreCase("null")) {
                    query.append(" order by ti.term_being_polled " + "ASC");
                }
                if (colName.equalsIgnoreCase("votingDate")) {
                    query.append(" order by tvud.voting_date " + sortOrder);
                } else if (colName.equalsIgnoreCase("sourceTerm")) {
                    query.append(" order by ti.term_being_polled " + sortOrder);
                } else if (colName.equalsIgnoreCase("suggestedTerm")) {
                    query.append(" order by ti.suggested_term " + sortOrder);
                }

                if (pageNum != 0) {
                    limitFrom = (pageNum - 1) * 10;
                    limitTo = (pageNum) * 10;
                    query.append(" limit " + limitFrom + " , " + limitTo);
                }
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
                hibQuery.addScalar("TERM_BEING_POLLED", Hibernate.STRING);
                hibQuery.addScalar("SUGGESTED_TERM", Hibernate.STRING); 
                //Adding status coloumn to know whether the term is final or not
                hibQuery.addScalar("TERM_STATUS_ID", Hibernate.INTEGER); //
                hibQuery.addScalar("VOTING_DATE", Hibernate.STRING);
                hibQuery.addScalar("ALL_VOTES", Hibernate.INTEGER);
                hibQuery.addScalar("INVITES", Hibernate.INTEGER);
                Object[] termsData = hibQuery.list().toArray();
                if ((termsData != null) && termsData.length > 0) {
                    for (int i = 0; i < termsData.length; i++) {
                        Object[] termsDataVal = (Object[]) termsData[i];
                        PollTerms term = new PollTerms();
                        term.setTermId((Integer) termsDataVal[0]);
                        term.setSourceTerm((String) termsDataVal[1]);
                        term.setSuggestedTerm((String) termsDataVal[2]); 
                        Integer statusId = (Integer) termsDataVal[3];
                        //Getting status to know whether the  term is final or not
                        if(statusId != null && statusId == 2) 
                        term.setStatus("Approved");
                        term.setVotingDate((String) termsDataVal[4]);
                        term.setVotesPerTerm((Integer) termsDataVal[5]);
                        term.setInvites((Integer) termsDataVal[6]);
                        userVotedTerms.add(term);
                    }
                }

                logger.debug(" Total user voted terms :"
                        + userVotedTerms.size());
                return userVotedTerms;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get term vote user details for a termId and userId
     *
     * @param termId Integer to be filtered for term vote details
     * @param userId Integer to be filtered for term vote details
     * @return TermVoteUserDetails w.r.t the term id and user id
     */
    @Override
    @Transactional
    public TermVoteUserDetails getTermVoteUserDetails(Integer termId,
                                                      Integer userId) throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session
                .createCriteria(TermVoteUserDetails.class);
        hibCriteria.add(Restrictions.eq("termId", termId));
        hibCriteria.add(Restrictions.eq("userId", userId));
        List<TermVoteUserDetails> termVoteUserDetailsList = hibCriteria.list();
        return (TermVoteUserDetails) ((termVoteUserDetailsList != null && termVoteUserDetailsList
                .size() > 0) ? termVoteUserDetailsList.get(0) : null);
    }

    /**
     * To get term translation for a termTranslationId
     *
     * @param termTranslationId Integer to be filtered
     * @return TermTranslation w.r.t the term translation id
     */
    @Override
    @Transactional
    public TermTranslation getTermTranslation(Integer termTranslationId)
            throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TermTranslation.class);
        hibCriteria
                .add(Restrictions.eq("termTranslationId", termTranslationId));
        return (TermTranslation) hibCriteria.uniqueResult();
    }

    /**
     * To delete vote details for users
     *
     * @param userIds Integer for which vote data has to be updated
     */
    @Override
    @Transactional
    public void deleteTermVoteDetailsForUser(Integer[] userIds , Integer loggedUserId) {
         Session session = getHibernateSession();
        List<TermVoteUserDetails> termVoteUserDetailsList = new ArrayList<TermVoteUserDetails>();
        TermTranslation termTranslation = new TermTranslation();
        for (int i = 0; i < userIds.length; i++) {
            Integer userId = userIds[i];
            Criteria criteria = session
                    .createCriteria(TermVoteUserDetails.class);
            criteria.add(Restrictions.eq("userId", userId));
            logger.debug("Got term vote user details for userId :" + userId);
            termVoteUserDetailsList = (List<TermVoteUserDetails>) criteria
                    .list();
            if ((termVoteUserDetailsList != null)
                    && termVoteUserDetailsList.size() > 0) {
                for (int j = 0; j < termVoteUserDetailsList.size(); j++) {
                    TermVoteUserDetails termVoteUserDetails = termVoteUserDetailsList
                            .get(j);
                    termVoteUserDetails.setIsActive("N");
                    termVoteUserDetails.setUpdateDate(new Date());
                    termVoteUserDetails.setUpdatedBy(loggedUserId);
                    getHibernateTemplate().update(termVoteUserDetails);

                    if (termVoteUserDetails.getTermTranslationId() != null) {
                        termTranslation = getHibernateTemplate().get(
                                TermTranslation.class,
                                termVoteUserDetails.getTermTranslationId());
                        Integer vote = termTranslation.getVote();
                        termTranslation.setVote(vote - 1);
                        termTranslation.setUpdateDate(new Date());
                        termTranslation.setUpdatedBy(userId);
                        getHibernateTemplate().update(termTranslation);
                    }
                }
            }
        }
    }

    /**
     * To get voting status of voted terms
     *
     * @param termId variable to get particular termId
     * @return VotingStatus object list w.r.t the term id
     */
    @Override
    @Transactional
    public List<Object> getvotingStatus(final Integer termId) {
         Session session = getHibernateSession();
        StringBuffer query = new StringBuffer();
        query.append(GET_TERM_VOTING_STATUS);
        query.append(" = " + termId);

        SQLQuery hibQuery = session.createSQLQuery(query.toString());
        hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
        hibQuery.addScalar("USER_NAME", Hibernate.STRING);
        hibQuery.addScalar("VOTE_INVITE_STATUS", Hibernate.STRING);
        hibQuery.addScalar("VOTING_DATE", Hibernate.STRING);
        hibQuery.addScalar("COMMENTS", Hibernate.STRING);
        hibQuery.addScalar("SUGGESTED_TERM", Hibernate.STRING);
        logger.debug("Got voted term status successfully");
        return hibQuery.list();

    }
    
    /**
     * To get history details
     * @param termId variable to get particular termId
     * 
     */
    
    @Override
    @Transactional
    public List<Object> getHitoryDeata(Integer termId) {
    	SQLQuery hibQuery = null;
    	List<Object>  historyObjList = null;
    	if(termId != null) {
    	Session session = getHibernateSession();
    	historyObjList = new ArrayList<Object>();
    	
        StringBuffer query = new StringBuffer();
        query.append(GET_TERM_HISTORY);
        query.append(" = " + termId);
        query.append(" order by create_date desc ");

        hibQuery = session.createSQLQuery(query.toString());
        hibQuery.addScalar("user_name", Hibernate.STRING);
        hibQuery.addScalar("changed_source_term", Hibernate.STRING);
        hibQuery.addScalar("changed_target_term", Hibernate.STRING);
        hibQuery.addScalar("part_of_speech", Hibernate.STRING);
        hibQuery.addScalar("concept_definition", Hibernate.STRING);
        hibQuery.addScalar("term_form", Hibernate.STRING);
        hibQuery.addScalar("term_status", Hibernate.STRING);
        hibQuery.addScalar("term_category", Hibernate.STRING);
        hibQuery.addScalar("term_domain", Hibernate.STRING);
        hibQuery.addScalar("term_notes", Hibernate.STRING);
        hibQuery.addScalar("target_part_of_speech", Hibernate.STRING);
        hibQuery.addScalar("term_usage", Hibernate.STRING);
        hibQuery.addScalar("create_date", Hibernate.STRING);
        historyObjList = hibQuery.list();
        logger.debug("Got History term status successfully");
    	}
        return historyObjList;
    }

    /**
     * TNG-83
     * Code Added to get user comments. 
     * @param termid
     * @return comments list
     */
    @SuppressWarnings("unchecked")
	@Override
    @Transactional
    public List<Object> getUserComments(final Integer termId){
    	
    	 Session session = getHibernateSession();
    	 
    	 //create query 
         StringBuffer query = new StringBuffer();
         
         query.append(GET_USER_COMMENTS);
         query.append(" = " + termId);
         query.append(" AND  tvd.comments !='' ");

         SQLQuery hibQuery = session.createSQLQuery(query.toString());
         
         hibQuery.addScalar("USER_NAME", Hibernate.STRING);
         hibQuery.addScalar("COMMENTS", Hibernate.STRING);
         hibQuery.addScalar("voting_date", Hibernate.STRING);
         
         logger.info("+++ User comment data fetched from database successfully ++++");
         
         return hibQuery.list();
    }
    
    /**
     * To get list of termIds from term information by categoryId
     *
     * @param categoryId an Integer to be filtered
     * @return List of term id's
     */
    @Override
    @Transactional
    public List<Integer> getTermInformationByCategory(Integer categoryId) {
        List<Integer> termInformationList = null;
         Session session = getHibernateSession();

        if (categoryId == null) {
            return termInformationList;
        }
        termInformationList = new ArrayList<Integer>();
        Criteria crit = session.createCriteria(TermInformation.class).add(
                Restrictions.and(
                        Restrictions.eq("termCategory.categoryId", categoryId),
                        Restrictions.not(Restrictions.eq("isActive", "N"))));
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("termId"));
        crit.setProjection(proList);
        List list = crit.list();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object row = (Integer) it.next();
            Integer id = (Integer) row;
            termInformationList.add(id);
        }
        logger.debug("Got the termInformationList :" + termInformationList);
        return termInformationList;

    }

    /**
     * To save term image
     *
     * @param termId    Path variable to get particular attributes
     * @param photoPath
     */

    @Override
    @Transactional
    public void saveTermImage(Integer termId, String photoPath) {
        if (termId == null) {
            throw new IllegalArgumentException("Invalid termDetails");
        }

         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TermInformation.class);
        hibCriteria.add(Restrictions.eq("termId", termId));
        TermInformation TermPictureDetails = (TermInformation) hibCriteria
                .uniqueResult();
        if (TermPictureDetails != null) {
            TermInformation termInfo = getHibernateTemplate().get(
                    TermInformation.class, termId);
            termInfo.setCreateDate(new Date());
            termInfo.setPhotoPath(photoPath);
            termInfo.setIsActive("Y");
            if (termInfo.getTermId() != null) {
                getHibernateTemplate().update(termInfo);
                logger.debug("Term Information is updated");
            } else {
                getHibernateTemplate().save(termInfo);
                logger.debug("Term Information is created");
            }
        }
    }

    /**
     * To get tm information by search
     *
     * @param queryAppender Which is used to build a query
     * @return List of terms
     */
    @Override
   // @Transactional
    public Object[] getSearchManagePollTermsTM(
            final QueryAppender queryAppender, final Integer companyId) {
        HibernateCallback<Object[]> callback = new HibernateCallback<Object[]>()
        {

            @SuppressWarnings("deprecation")
            @Override
            public Object[] doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String searchCriteria = queryAppender.getSearchStr();
                int limitFrom = 0;
                int limitTo = 0;
                String filtertBy = (queryAppender.getFilterBy() == null) ? ""
                        : queryAppender.getFilterBy();
                Integer[] intSelectedIds = queryAppender.getSelectedIds();
                Integer[] intSelectedLangIds = queryAppender.getLangValues();
                String selectedIds = "";
                if (intSelectedIds != null && intSelectedIds.length != 0) {
                    for (int i = 0; i < intSelectedIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedIds += separator + intSelectedIds[i];
                    }

                }

                String values = "";
                if (intSelectedLangIds != null
                        && intSelectedLangIds.length != 0) {
                    for (int i = 0; i < intSelectedLangIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        values += separator + intSelectedLangIds[i];
                    }

                }

                String searchType = queryAppender.getSearchType();
                String likeValue = "";
                String tmSourceLikeValue = "";
                String tmTargetLikeValue = "";
                String searchBy = queryAppender.getSearchTerm();
                String colName = (queryAppender.getColName() == null) ? ""
                        : queryAppender.getColName();
                String sortOrder = (queryAppender.getSortOrder() == null) ? ""
                        : queryAppender.getSortOrder();
                Integer pageNum = queryAppender.getPageNum();
                String isTM = (queryAppender.getIsTM() == null) ? ""
                        : queryAppender.getIsTM();

                StringBuffer query = new StringBuffer();
                List<GSJobObject> userPollTerms = new ArrayList<GSJobObject>();

                query.append(GET_TM_PROFILE_TERMS);

                if (colName.equalsIgnoreCase("LanguageTM")) {
                    query.append(" left join (language_lookup lang ) on (tm.target_lang = lang.language_id)");
                } else if (colName.equalsIgnoreCase("product")) {
                    query.append("  left join (product_group_lookup prod) on (tm.product_line = prod.product_id)");
                } else if (colName.equalsIgnoreCase("domain")) {
                    query.append("  left join ( domain_lookup d ) on (tm.industry_domain = d.domain_id)");
                } else if (colName.equalsIgnoreCase("content")) {
                    query.append("  left join ( content_type_lookup con ) on (tm.content_type = con.content_type_id)");
                } else if (colName.equalsIgnoreCase("company")) {
                    query.append("  left join ( company_lookup company ) on (tm.company = company.company_id)");
                } else {
                    query.append("");
                }
                query.append(" where tm.is_active = 'Y' ");
                if (companyId != null) {
                    query.append(" and  tm.company =" + companyId + " ");
                }
                if (searchBy != null && !searchBy.equalsIgnoreCase("null")) {

                    if (searchBy.indexOf('*') == 0) {
                        searchBy = searchBy.replace("*", LIKE_PERCENT);
                    } else {
                        searchBy = searchBy.replace("*", LIKE_UNDERSCORE);
                    }

                    if (searchBy.contains(" ")) {
                        StringTokenizer st = new StringTokenizer(searchBy, " ");
                        if (searchType.equalsIgnoreCase("Exact")) {
                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                if (queryAppender.isCaseFlag()) {
                                    tmSourceLikeValue = tmSourceLikeValue + "tm.source like _utf8 '" + searchValue + "' " + " collate utf8_bin or ";
                                    tmTargetLikeValue = tmTargetLikeValue + "tm.target like _utf8 '" + searchValue + "' " + " collate utf8_bin or ";
                                } else {
                                    tmSourceLikeValue = tmSourceLikeValue + "tm.source like _utf8 '" + searchValue + "'  or ";
                                    tmTargetLikeValue = tmTargetLikeValue + "tm.target like _utf8 '" + searchValue + "' or ";
                                }
                            }

                            if (queryAppender.isCaseFlag()) {
                                tmSourceLikeValue = tmSourceLikeValue
                                        + "tm.source like _utf8 '" + searchBy
                                        + "' " + " collate utf8_bin or ";
                                tmTargetLikeValue = tmTargetLikeValue
                                        + "tm.target like _utf8 '" + searchBy
                                        + "' " + " collate utf8_bin or ";
                            } else {
                                tmSourceLikeValue = tmSourceLikeValue
                                        + "tm.source like _utf8 '" + searchBy
                                        + "'  or ";
                                tmTargetLikeValue = tmTargetLikeValue
                                        + "tm.target like _utf8'" + searchBy
                                        + "'  or ";
                            }
                        }
                        if (searchType.equalsIgnoreCase("Fuzzy")) {

                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();

                                tmSourceLikeValue = tmSourceLikeValue
                                        + " strcmp(SOUNDEX(tm.source), soundex('"
                                        + searchValue + "') )=0 "
                                        + " or "
                                        + "tm.source like _utf8 '" + searchValue + "%'" + " or ";
                                tmTargetLikeValue = tmTargetLikeValue
                                        + " strcmp(SOUNDEX(tm.target), soundex('"
                                        + searchValue + "') )=0 "
                                        + " or "
                                        + "tm.target like _utf8 '" + searchValue + "%' " + " or ";
                            }
                            tmSourceLikeValue = tmSourceLikeValue
                                    + " strcmp(SOUNDEX(tm.source), soundex('"
                                    + searchBy + "') )=0 "
                                    + " or ";
                            tmTargetLikeValue = tmTargetLikeValue
                                    + " strcmp(SOUNDEX(tm.target), soundex('"
                                    + searchBy + "') )=0 "
                                    + " or ";


                        }
                        if (searchType.equalsIgnoreCase("--Select--")) {
                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                tmSourceLikeValue = tmSourceLikeValue
                                        + " tm.source like _utf8 '"
                                        + searchValue + "%'"
                                        + " or ";
                                tmTargetLikeValue = tmTargetLikeValue
                                        + " tm.target like _utf8 '"
                                        + searchValue + "%'"
                                        + " or ";
                            }
                        }

                        if (tmSourceLikeValue.endsWith(" or ")) {
                            tmSourceLikeValue = tmSourceLikeValue
                                    .substring(0, tmSourceLikeValue
                                            .lastIndexOf("or"));
                        }
                        if (tmTargetLikeValue.endsWith(" or ")) {
                            tmTargetLikeValue = tmTargetLikeValue
                                    .substring(0, tmTargetLikeValue
                                            .lastIndexOf("or"));
                        }

                        query.append("and (( " + tmSourceLikeValue + " ) or ("
                                + tmTargetLikeValue + "))");

                    } else {
                        if (searchType.equalsIgnoreCase("--Select--")) {
                            if (searchBy.indexOf('*') == 0) {
                                searchBy = searchBy.replace("*", LIKE_PERCENT);
                            } else {
                                searchBy = searchBy.replace("*",
                                        LIKE_UNDERSCORE);
                            }
                            likeValue = ((searchBy.indexOf(LIKE_UNDERSCORE) == -1) || (searchBy
                                    .indexOf(LIKE_UNDERSCORE) == searchBy
                                    .lastIndexOf('_'))) ? (searchBy + LIKE_PERCENT)
                                    : (searchBy + " " + LIKE_PERCENT);

                            query.append(" and ((tm.source like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }
                            query.append(" or tm.target like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }
                            query.append(")");

                            query.append(")");

                        }
                        if (searchType.equalsIgnoreCase("Exact")) {
                            likeValue = searchBy;

                            query.append(" and ((tm.source like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }
                            query.append(" or tm.target like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }
                            query.append(")");


                            query.append(")");
                        }
                        if (searchType.equalsIgnoreCase("Fuzzy")) {
                            if (searchBy.contains("*")) {
                                if (searchBy.indexOf('*') == 0) {
                                    searchBy = searchBy.replace("*", LIKE_PERCENT);
                                    searchBy = searchBy + LIKE_PERCENT;
                                } else {
                                    searchBy = searchBy.replace("*", LIKE_PERCENT);
                                }

                                query.append("and ((tm.source like _utf8 '"
                                        + searchBy + "'");
                                query.append("or tm.target like _utf8 ' "
                                        + searchBy + "' )");

                                query.append(")");
                            } else {
                                query.append(" and ((strcmp(SOUNDEX(tm.source), soundex(");
                                query.append("'");
                                query.append(searchBy);
                                query.append("') )=0");

                                query.append(" or strcmp(SOUNDEX(tm.target), soundex(");
                                query.append("'");
                                query.append(searchBy);
                                query.append("') )=0 )");

                                query.append(" or ((tm.source like _utf8 '"
                                        + searchBy + "%' ");
                                query.append("or tm.target like _utf8 ' "
                                        + searchBy + "%' )");

                                query.append("))");


                            }
                        }

                    }
                }
                if (values != null && values != "" && !(values.equals("0"))) {
                    query.append(" and tm.target_lang in ( "
                            + values + " ) ");
                }

                query.append(" group by tm.tm_profile_info_id ");

                if (colName.equalsIgnoreCase("sourceTerm")) {
                    query.append(" order by tm.source " + sortOrder);
                } else if (colName.equalsIgnoreCase("targetTerm")) {
                    query.append(" order by tm.target " + sortOrder);
                } else if (colName.equalsIgnoreCase("tmLanguage")) {
                    query.append("order by lang.language_label " + sortOrder);
                } else if (colName.equalsIgnoreCase("product")) {
                    query.append(" order by prod.product " + sortOrder);
                } else if (colName.equalsIgnoreCase("domain")) {
                    query.append("order by d.domain " + sortOrder);
                } else if (colName.equalsIgnoreCase("content")) {
                    query.append(" order by con.content_type " + sortOrder);
                }
                if (colName.equalsIgnoreCase("company")) {
                    query.append(" order by company.company_name " + sortOrder);
                } else {
                    query.append(" order by tm.tm_profile_info_id desc ");
                }

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("TM_PROFILE_INFO_ID", Hibernate.INTEGER);
                hibQuery.addScalar("SOURCE", Hibernate.STRING);
                hibQuery.addScalar("TARGET", Hibernate.STRING);
                hibQuery.addScalar("INDUSTRY_DOMAIN", Hibernate.INTEGER);
                hibQuery.addScalar("COMPANY_NAME", Hibernate.INTEGER);
                hibQuery.addScalar("PRODUCT_LINE", Hibernate.INTEGER);
                hibQuery.addScalar("TARGET_LANG", Hibernate.INTEGER);
                hibQuery.addScalar("CONTENT_TYPE", Hibernate.INTEGER);
                Object[] termsData = hibQuery.list().toArray();

                return termsData;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get Termbase Information by search
     *
     * @param queryAppender Which is used to build a query
     * @return List of terms
     */
    @Override
    //@Transactional
    public List<GSJobObject> getSearchManagePollTermsTermBase(
            final QueryAppender queryAppender, final Integer companyId) {
        HibernateCallback<List<GSJobObject>> callback = new HibernateCallback<List<GSJobObject>>()
        {

            @SuppressWarnings("deprecation")
            @Override
            public List<GSJobObject> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String searchCriteria = queryAppender.getSearchStr();

                String filtertBy = (queryAppender.getFilterBy() == null) ? ""
                        : queryAppender.getFilterBy();
                Integer[] intSelectedIds = queryAppender.getSelectedIds();
                Integer[] intSelectedLangIds = queryAppender.getLangValues();
                String selectedIds = "";
                if (intSelectedIds != null && intSelectedIds.length != 0) {
                    for (int i = 0; i < intSelectedIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedIds += separator + intSelectedIds[i];
                    }

                }

                String values = "";
                if (intSelectedLangIds != null
                        && intSelectedLangIds.length != 0) {
                    for (int i = 0; i < intSelectedLangIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        values += separator + intSelectedLangIds[i];
                    }

                }

                String searchType = queryAppender.getSearchType();
                String likeValue = "";
                String termBaseSourceLikeValue = "";
                String termBaseTargetLikeValue = "";
                String depSourcelikeValue = "";
                String depTarglikeValue = "";
                String searchBy = queryAppender.getSearchTerm();
                String colName = (queryAppender.getColName() == null) ? ""
                        : queryAppender.getColName();
                String sortOrder = (queryAppender.getSortOrder() == null) ? ""
                        : queryAppender.getSortOrder();
                Integer pageNum = queryAppender.getPageNum();
                String isTM = (queryAppender.getIsTM() == null) ? ""
                        : queryAppender.getIsTM();

                StringBuffer query = new StringBuffer();
                int limitFrom = 0;
                int limitTo = 0;
                List<GSJobObject> userPollTerms = new ArrayList<GSJobObject>();

                query.append(GET_SEARCH_MANAGE_POLL_TERMS);
                if (companyId != null) {
                    query.append(" and ti.company_id=" + companyId + " ");
                }

                if (searchBy != null && !searchBy.equalsIgnoreCase("null")) {


                    if (searchBy.indexOf('*') == 0) {
                        searchBy = searchBy.replace("*", LIKE_PERCENT);
                    } else {
                        searchBy = searchBy.replace("*", LIKE_UNDERSCORE);
                    }

                    if (searchBy.contains(" ")) {
                        StringTokenizer st = new StringTokenizer(searchBy, " ");
                        if (searchType.equalsIgnoreCase("Exact")) {
                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                if (queryAppender.isCaseFlag()) {
                                    termBaseSourceLikeValue = termBaseSourceLikeValue
                                            + "ti.term_being_polled like _utf8 '"
                                            + searchValue
                                            + "' "
                                            + " collate utf8_bin or ";
                                    termBaseTargetLikeValue = termBaseTargetLikeValue
                                            + "ti.suggested_term like _utf8 '"
                                            + searchValue
                                            + "' "
                                            + " collate utf8_bin or ";
                                    depSourcelikeValue = depSourcelikeValue
                                            + " dt.deprecated_source like _utf8 '"
                                            + searchValue + "'"
                                            + " collate utf8_bin  or ";
                                    depTarglikeValue = depTarglikeValue
                                            + " dt.deprecated_target like _utf8 ' "
                                            + searchValue + "'"
                                            + " collate utf8_bin  or ";
                                } else {
                                    termBaseSourceLikeValue = termBaseSourceLikeValue
                                            + " ti.term_being_polled like _utf8 '"
                                            + searchValue + "' or ";
                                    termBaseTargetLikeValue = termBaseTargetLikeValue
                                            + " ti.suggested_term like _utf8 '"
                                            + searchValue + "' or ";
                                    depSourcelikeValue = depSourcelikeValue
                                            + " dt.deprecated_source like _utf8 '"
                                            + searchValue + "' or ";
                                    depTarglikeValue = depTarglikeValue
                                            + " dt.deprecated_target like _utf8 '"
                                            + searchValue + "' or ";
                                }
                            }
                            if (queryAppender.isCaseFlag()) {
                                termBaseSourceLikeValue = termBaseSourceLikeValue
                                        + "ti.term_being_polled like _utf8 '"
                                        + searchBy
                                        + "' "
                                        + " collate utf8_bin or ";
                                termBaseTargetLikeValue = termBaseTargetLikeValue
                                        + "ti.suggested_term like _utf8 '"
                                        + searchBy
                                        + "' "
                                        + " collate utf8_bin or ";
                                depSourcelikeValue = depSourcelikeValue
                                        + "dt.deprecated_source like _utf8 '"
                                        + searchBy
                                        + "' "
                                        + " collate utf8_bin or ";
                                depTarglikeValue = depTarglikeValue
                                        + "dt.deprecated_target like _utf8 '"
                                        + searchBy
                                        + "' "
                                        + " collate utf8_bin or ";
                            } else {
                                termBaseSourceLikeValue = termBaseSourceLikeValue
                                        + "ti.term_being_polled like _utf8 '"
                                        + searchBy + "' or ";
                                termBaseTargetLikeValue = termBaseTargetLikeValue
                                        + "ti.suggested_term like _utf8 '"
                                        + searchBy + "' or ";
                                depSourcelikeValue = depSourcelikeValue
                                        + "dt.deprecated_source like _utf8 '"
                                        + searchBy + "' or ";
                                depTarglikeValue = depTarglikeValue
                                        + "dt.deprecated_target like _utf8 '"
                                        + searchBy + "' or ";
                            }
                        }
                        if (searchType.equalsIgnoreCase("Fuzzy")) {

                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();

                                termBaseSourceLikeValue = termBaseSourceLikeValue
                                        + " strcmp(SOUNDEX(ti.term_being_polled), soundex('"
                                        + searchValue + "') )=0 "
                                        + " or "
                                        + "ti.term_being_polled like _utf8 '"
                                        + searchBy + "%'" + " or ";
                                termBaseTargetLikeValue = termBaseTargetLikeValue
                                        + " strcmp(SOUNDEX(ti.suggested_term), soundex('"
                                        + searchValue + "') )=0 "
                                        + " or "
                                        + "ti.suggested_term like _utf8 '"
                                        + searchBy + "%'" + " or ";
                                depSourcelikeValue = depSourcelikeValue
                                        + " strcmp(SOUNDEX(dt.deprecated_source), soundex('"
                                        + searchValue + "') )=0 "
                                        + " or "
                                        + "dt.deprecated_source like _utf8 '"
                                        + searchBy + "%'" + " or ";
                                depTarglikeValue = depTarglikeValue
                                        + " strcmp(SOUNDEX(dt.deprecated_target), soundex('"
                                        + searchValue + "') )=0 "
                                        + " or "
                                        + "dt.deprecated_target like _utf8 '"
                                        + searchBy + "%'" + " or ";
                            }
                            termBaseSourceLikeValue = termBaseSourceLikeValue
                                    + " strcmp(SOUNDEX(ti.term_being_polled), soundex('"
                                    + searchBy + "') )=0 "
                                    + " or ";
                            termBaseTargetLikeValue = termBaseTargetLikeValue
                                    + " strcmp(SOUNDEX(ti.suggested_term), soundex('"
                                    + searchBy + "') )=0 "
                                    + " or ";
                            depSourcelikeValue = depSourcelikeValue
                                    + " strcmp(SOUNDEX(dt.deprecated_source), soundex('"
                                    + searchBy + "') )=0 "
                                    + " or ";
                            depTarglikeValue = depTarglikeValue
                                    + " strcmp(SOUNDEX(dt.deprecated_target), soundex('"
                                    + searchBy + "') )=0 "
                                    + " or ";
                        }
                        if (searchType.equalsIgnoreCase("--Select--")) {
                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                termBaseSourceLikeValue = termBaseSourceLikeValue
                                        + " ti.term_being_polled like _utf8 '"
                                        + searchValue + "%'"
                                        + " or ";
                                termBaseTargetLikeValue = termBaseTargetLikeValue
                                        + " ti.suggested_term like _utf8 '"
                                        + searchValue + "%'"
                                        + " or ";
                                depSourcelikeValue = depSourcelikeValue
                                        + " dt.deprecated_source like _utf8 '"
                                        + searchValue + "%'"
                                        + " or ";
                                depTarglikeValue = depTarglikeValue
                                        + " dt.deprecated_target like _utf8 '"
                                        + searchValue + "%'"
                                        + " or ";

                            }
                        }

                        if (termBaseSourceLikeValue.endsWith(" or ")) {
                            termBaseSourceLikeValue = termBaseSourceLikeValue
                                    .substring(0, termBaseSourceLikeValue
                                            .lastIndexOf("or"));
                        }
                        if (termBaseTargetLikeValue.endsWith(" or ")) {
                            termBaseTargetLikeValue = termBaseTargetLikeValue
                                    .substring(0, termBaseTargetLikeValue
                                            .lastIndexOf("or"));
                        }
                        if (depSourcelikeValue.endsWith(" or ")) {
                            depSourcelikeValue = depSourcelikeValue
                                    .substring(0, depSourcelikeValue
                                            .lastIndexOf("or"));
                        }
                        if (depTarglikeValue.endsWith(" or ")) {
                            depTarglikeValue = depTarglikeValue
                                    .substring(0, depTarglikeValue
                                            .lastIndexOf("or"));
                        }
                        query.append("and (( " + termBaseSourceLikeValue + "or"
                                + termBaseTargetLikeValue + " or "
                                + depSourcelikeValue + "or" + depTarglikeValue);
                        query.append("))");

                    } else {

                        if (searchType.equalsIgnoreCase("--Select--")) {
                            if (searchBy.indexOf('*') == 0) {
                                searchBy = searchBy.replace("*", LIKE_PERCENT);
                            } else {
                                searchBy = searchBy.replace("*", LIKE_UNDERSCORE);
                            }

                            likeValue = ((searchBy.indexOf(LIKE_UNDERSCORE) == -1) || (searchBy
                                    .indexOf(LIKE_UNDERSCORE) == searchBy
                                    .lastIndexOf('_'))) ? (searchBy + LIKE_PERCENT)
                                    : (searchBy + " " + LIKE_PERCENT);

                            query.append(" and ((ti.term_being_polled like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }
                            query.append(" or ti.suggested_term like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }
                            query.append(")");

                            query.append(" or ( dt.deprecated_source like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }

                            query.append(" or dt.deprecated_target like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }

                            query.append("))");
                        }
                        if (searchType.equalsIgnoreCase("Exact")) {
                            likeValue = searchBy;

                            query.append(" and ((ti.term_being_polled like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }
                            query.append(" or ti.suggested_term like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }
                            query.append(")");

                            query.append(" or ( dt.deprecated_source like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }

                            query.append(" or dt.deprecated_target like _utf8 '"
                                    + likeValue + "' ");

                            if (queryAppender.isCaseFlag()) {
                                query.append(" collate utf8_bin ");
                            }

                            query.append("))");
                        }
                        if (searchType.equalsIgnoreCase("Fuzzy")) {
                            if (searchBy.contains("*")) {
                                if (searchBy.indexOf('*') == 0) {
                                    searchBy = searchBy.replace("*", LIKE_PERCENT);
                                    searchBy = searchBy + LIKE_PERCENT;
                                } else {
                                    searchBy = searchBy.replace("*", LIKE_PERCENT);
                                }

                                query.append("and ((ti.term_being_polled like _utf8 '"
                                        + searchBy + "'");
                                query.append("or ti.suggested_term like _utf8 ' "
                                        + searchBy + "' )");
                                query.append(" or ( dt.deprecated_source like _utf8 '"
                                        + searchBy + "'");
                                query.append(" or  dt.deprecated_target like _utf8 '"
                                        + searchBy + "' ))");
                            } else {
                                query.append(" and (((strcmp(SOUNDEX(ti.term_being_polled), soundex(");
                                query.append("'");
                                query.append(searchBy);
                                query.append("') )=0");

                                query.append(" or strcmp(SOUNDEX(ti.suggested_term), soundex(");
                                query.append("'");
                                query.append(searchBy);
                                query.append("') )=0 )");

                                query.append(" or (strcmp(SOUNDEX(dt.deprecated_source ), soundex(");
                                query.append("'");
                                query.append(searchBy);
                                query.append("') )=0");

                                query.append(" or strcmp(SOUNDEX( dt.deprecated_target), soundex(");
                                query.append("'");
                                query.append(searchBy);
                                query.append("') )=0 ))");

                                query.append(" or ((ti.term_being_polled like _utf8 '"
                                        + searchBy + "%' ");
                                query.append("or ti.suggested_term like _utf8 ' "
                                        + searchBy + "%' )))");


                            }
                        }

                    }
                }
                if (values != null && values != "" && !(values.equals("0"))) {
                    query.append(" and ti.suggested_term_lang_id in ( "
                            + values + " ) ");
                }

                query.append(" and ti.is_tm is null ");
                query.append(" group by ti.term_id ");

                if (colName.equalsIgnoreCase("domain")) {
                    query.append(" order by domain.domain " + sortOrder);
                } else if (colName.equalsIgnoreCase("company")) {
                    query.append(" order by company.company_name " + sortOrder);
                } else if (colName.equalsIgnoreCase("category")) {
                    query.append(" order by cat.category " + sortOrder);
                } else if (colName.equalsIgnoreCase("pollExpirationDate")) {
                    query.append(" order by tvm.voting_expired_date "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("targetTerm")) {
                    query.append(" order by ti.term_being_polled " + sortOrder);
                } else if (colName.equalsIgnoreCase("suggestedTerm")) {
                    query.append(" order by ti.suggested_term " + sortOrder);
                } else if (colName.equalsIgnoreCase("status")) {
                    query.append(" order by status.status " + sortOrder);
                } else if (colName.equalsIgnoreCase("language")) {
                    query.append(" order by lang.language_label " + sortOrder);
                } else if (colName.equalsIgnoreCase("POS")) {
                    query.append(" order by pos.part_of_speech " + sortOrder);
                } else {
                    query.append(" order by ti.create_date desc ");
                }

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
                hibQuery.addScalar("TERM_BEING_POLLED", Hibernate.STRING);
                hibQuery.addScalar("SUGGESTED_TERM", Hibernate.STRING);
                hibQuery.addScalar("PART_OF_SPEECH", Hibernate.STRING);
                hibQuery.addScalar("CATEGORY", Hibernate.STRING);
                hibQuery.addScalar("DOMAIN", Hibernate.STRING);
                hibQuery.addScalar("COMPANY_NAME", Hibernate.STRING);
                hibQuery.addScalar("LANGUAGE_LABEL", Hibernate.STRING);
                hibQuery.addScalar("STATUS", Hibernate.STRING);
                hibQuery.addScalar("EXPIRED_DATE", Hibernate.STRING);
                hibQuery.addScalar("ALL_VOTES", Hibernate.INTEGER);
                hibQuery.addScalar("INVITES", Hibernate.INTEGER);
                hibQuery.addScalar("IS_DEPRECATE", Hibernate.INTEGER);
                Object[] termsData = hibQuery.list().toArray();
                if ((termsData != null) && termsData.length > 0) {
                    for (int i = 0; i < termsData.length; i++) {
                        Object[] termsDataVal = (Object[]) termsData[i];
                        GSJobObject term = new GSJobObject();

                        term.setTermBaseId((Integer) termsDataVal[0]);
                        String sourceTerm = (String) termsDataVal[1];
                        if (searchBy != null && sourceTerm != null) {
                            String searchByValues[] = new String[1];
                            if (searchBy.contains(" ")) {
                                searchByValues = searchBy.split(" ");
                            } else {
                                searchByValues[0] = searchBy;
                            }
                            for (int j = 0; j < searchByValues.length; j++) {
                                Pattern pattern = Pattern.compile(
                                        searchByValues[j],
                                        Pattern.CASE_INSENSITIVE);
                                Matcher matcher = pattern.matcher(sourceTerm);
                                if (matcher.find()) {
                                    for (int k = 0; k <= matcher.groupCount(); k++) {

                                        sourceTerm = matcher
                                                .replaceAll("<span class='selectedbg'>"
                                                        + matcher.group(k)
                                                        + "</span>");
                                    }
                                }
                            }

                        }
                        term.setTermsBeingPolled(sourceTerm);

                        String targetTerm = (String) termsDataVal[2];
                        if (searchBy != null && targetTerm != null) {
                            String searchByValues[] = new String[1];
                            if (searchBy.contains(" ")) {
                                searchByValues = searchBy.split(" ");
                            } else {
                                searchByValues[0] = searchBy;
                            }
                            for (int j = 0; j < searchByValues.length; j++) {
                                Pattern pattern = Pattern.compile(
                                        searchByValues[j],
                                        Pattern.CASE_INSENSITIVE);
                                Matcher matcher = pattern.matcher(targetTerm);
                                if (matcher.find()) {
                                    for (int k = 0; k <= matcher.groupCount(); k++) {

                                        targetTerm = matcher
                                                .replaceAll("<span class='selectedbg'>"
                                                        + matcher.group(k)
                                                        + "</span>");
                                    }
                                }
                            }
                        }
                        term.setSuggestedterms(targetTerm);
                        term.setPartOfSpeech((String) termsDataVal[3]);
                        term.setCategory((String) termsDataVal[4]);
                        term.setDomain((String) termsDataVal[5]);
                        term.setCompany((String) termsDataVal[6]);
                        term.setTargetTermLanguage((String) termsDataVal[7]);
                        term.setStatus((String) termsDataVal[8]);
                        term.setPollExpirationDt((String) termsDataVal[9]);
                        term.setVotesPerTerm((Integer) termsDataVal[10]);
                        term.setInvites((Integer) termsDataVal[11]);
                        term.setDeprecatedCount((Integer) termsDataVal[12]);
                        userPollTerms.add(term);
                    }
                }
                logger.debug(" Total manage poll terms :"
                        + userPollTerms.size());
                return userPollTerms;
            }

        };
        return getHibernateTemplate().execute(callback);
    }


    /**
     * Add new term in TermInformation,GlobalsightTermInfo
     *
     * @param termInformation,globalSightTermInfo
     *         TermInformation that needs to be added
     * @return If term added successfully termId of inserted termInformation else termId as 0.
     */
    //@Transactional
    public Integer saveGlobalSightTerm(GlobalsightTermInfo globalSightTermInfo) {
        Integer termId = null;
        if (globalSightTermInfo == null) {
            return null;
        }
        globalSightTermInfo.setIsActive("Y");
        termId = (Integer) getHibernateTemplate().save(globalSightTermInfo);

        return termId;
    }

    /**
     * Add new term in Term Translation and Global Sight.
     *
     * @param termTranslation,globalSightTermInfo
     *         Term Translation,Global Sight that needs to be added
     * @return If term is added successfully "Success", else "failed".
     */
    public String saveTranslation(TermTranslation termTranslation) {
        String status = "";
        if (termTranslation == null) {
            status = "failed";
            return status;
        }

        termTranslation.setIsActive("Y");

        getHibernateTemplate().save(termTranslation);
        status = "success";
        return status;
    }


    /**
     * To get globalSightTermInfo
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter the  terms
     * @return List of terms
     */
    //@Transactional
    public List<GlobalsightTerms> getGlobalSightTermInfo(final QueryAppender queryAppender, final Integer companyId) {
        HibernateCallback<List<GlobalsightTerms>> callback = new HibernateCallback<List<GlobalsightTerms>>()
        {

            @SuppressWarnings("deprecation")
            @Override
            public List<GlobalsightTerms> doInHibernate(Session session)
                    throws HibernateException, SQLException {

                StringBuffer query = new StringBuffer();
                int limitFrom = 0;
                int limitTo = 0;
                String filtertBy = (queryAppender.getFilterBy() == null) ? ""
                        : queryAppender.getFilterBy();
                Integer[] intSelectedIds = queryAppender.getSelectedIds();
                Integer[] intcompanyIds = queryAppender.getSelectedCompanyIds();
                Integer[] intlangIds = queryAppender.getSelectedLangIds();
                String selectedIds = "";
                String selectedlangIds = "";
                String selectedcompanyIds = "";
                String searchBy = queryAppender.getSearchStr();
                if (intSelectedIds != null && intSelectedIds.length != 0) {
                    for (int i = 0; i < intSelectedIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedIds += separator + intSelectedIds[i];
                    }

                }
                if (intcompanyIds != null && intcompanyIds.length != 0) {
                    for (int i = 0; i < intcompanyIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedcompanyIds += separator + intcompanyIds[i];
                    }

                }
                if (intlangIds != null && intlangIds.length != 0) {
                    for (int i = 0; i < intlangIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedlangIds += separator + intlangIds[i];
                    }

                }
                List<GlobalsightTerms> globalSightTermInfo = new ArrayList<GlobalsightTerms>();
                query.append(GET_GLOBAL_SIGHT_TERM_INFO);
                if (companyId != null) {
                    query.append(" and gt.company_id=" + companyId);
                }
                String likeValue = "";
                String termSourcelikeValue = "";
                String termTargetlikeValue = "";

                if (searchBy != null && !searchBy.equalsIgnoreCase("null")) {

                    if (searchBy.indexOf('*') == 0) {
                        searchBy = searchBy.replace("*", LIKE_PERCENT);
                    } else {
                        searchBy = searchBy.replace("*", LIKE_UNDERSCORE);
                    }

                    if (searchBy.contains(" ")) {
                        StringTokenizer st = new StringTokenizer(searchBy, " ");
                        if (queryAppender.isCaseFlag()) {
                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                termSourcelikeValue = termSourcelikeValue
                                        + " gt.source_segment like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";
                                termTargetlikeValue = termTargetlikeValue
                                        + " gt.target_segment like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";


                            }
                        } else {

                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                termSourcelikeValue = termSourcelikeValue
                                        + " gt.source_segment like _utf8 '"
                                        + searchValue + "%'" + " or ";
                                termTargetlikeValue = termTargetlikeValue
                                        + " gt.target_segment like _utf8 '"
                                        + searchValue + "%'" + " or ";

                            }
                        }
                        if (termSourcelikeValue.endsWith(" or ")) {
                            termSourcelikeValue = termSourcelikeValue
                                    .substring(0, termSourcelikeValue
                                            .lastIndexOf("or"));
                        }
                        if (termTargetlikeValue.endsWith(" or ")) {
                            termTargetlikeValue = termTargetlikeValue
                                    .substring(0, termTargetlikeValue
                                            .lastIndexOf("or"));
                        }


                        query.append("and ( " + termSourcelikeValue + "or"
                                + termTargetlikeValue + ")");
                    } else {
                        likeValue = ((searchBy.indexOf(LIKE_UNDERSCORE) == -1) || (searchBy
                                .indexOf(LIKE_UNDERSCORE) == searchBy
                                .lastIndexOf('_'))) ? (searchBy + LIKE_PERCENT)
                                : (searchBy + " " + LIKE_PERCENT);

                        likeValue = "'" + likeValue + "'";

                        query.append(" and (gt.source_segment like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(" or gt.target_segment like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(")");


                    }
                }
                String colName = (queryAppender.getColName() == null) ? ""
                        : queryAppender.getColName();
                String sortOrder = (queryAppender.getSortOrder() == null) ? ""
                        : queryAppender.getSortOrder();
                Integer pageNum = queryAppender.getPageNum();

                if (selectedcompanyIds != "" && selectedcompanyIds != null) {

                    query.append(" and  gt.company_id in ( " + selectedcompanyIds
                            + " ) ");
                }
                if (selectedIds != "" && selectedIds != null) {

                    query.append(" and  fi.task_id in ( " + selectedIds
                            + " ) ");
                }
                if (selectedlangIds != "" && selectedlangIds != null) {

                    query.append(" and  fi.target_lang in ( " + selectedlangIds
                            + " ) ");
                }
                if (colName.equalsIgnoreCase("targetTerm")) {
                    query.append(" order by gt.source_segment " + sortOrder);
                } else if (colName.equalsIgnoreCase("suggestedTerm")) {
                    query.append(" order by gt.target_segment "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("sourceLang")) {
                    query.append(" order by slang.language_label "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("targetLang")) {
                    query.append(" order by tlang.language_label "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("origin")) {
                    query.append(" order by gt.origin "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("pageId")) {
                    query.append(" order by fi.file_id "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("workFlowId")) {
                    query.append(" order by gt.transunit_id "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("taskId")) {
                    query.append(" order by fi.task_id "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("jobId")) {
                    query.append(" order by fi.job_id "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("jobName")) {
                    query.append(" order by fi.jobname "
                            + sortOrder);
                } else {
                    query.append(" order by gt.globalsight_term_info_id desc ");
                }


                if (pageNum != 0) {
                    limitFrom = (pageNum - 1) * 10;
                    limitTo = (pageNum) * 10;
                    query.append(" limit " + limitFrom + " , " + limitTo);
                }

                Language language = new Language();
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("GLOBALSIGHT_TERM_INFO_ID", Hibernate.INTEGER);
                hibQuery.addScalar("SOURCE_SEGMENT", Hibernate.STRING);
                hibQuery.addScalar("TARGET_SEGMENT", Hibernate.STRING);
                hibQuery.addScalar("SOURCE_LANG", Hibernate.STRING);
                hibQuery.addScalar("TARGET_LANG", Hibernate.STRING);
                hibQuery.addScalar("ORIGIN", Hibernate.STRING);
                hibQuery.addScalar("FILE_ID", Hibernate.INTEGER);
                hibQuery.addScalar("TRANSUNIT_ID", Hibernate.INTEGER);
                hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
                hibQuery.addScalar("JOB_ID", Hibernate.STRING);
                hibQuery.addScalar("JOBNAME", Hibernate.STRING);
                hibQuery.addScalar("TASK_ID", Hibernate.STRING);
                hibQuery.addScalar("TASKNAME", Hibernate.STRING);
                hibQuery.addScalar("ALL_VOTES", Hibernate.INTEGER);
                hibQuery.addScalar("INVITES", Hibernate.INTEGER);


                Object[] termsData = hibQuery.list().toArray();
                if ((termsData != null) && termsData.length > 0) {
                    for (int i = 0; i < termsData.length; i++) {
                        Object[] termsDataVal = (Object[]) termsData[i];
                        GlobalsightTerms term = new GlobalsightTerms();
                        term.setGlobalsightTermInfoId((Integer) termsDataVal[0]);
                        term.setSourceSegment((String) termsDataVal[1]);
                        term.setTargetSegment((String) termsDataVal[2]);
                        term.setSourceLang((String) termsDataVal[3]);
                        term.setTargetLang((String) termsDataVal[4]);
                        term.setOrigin((String) termsDataVal[5]);
                        term.setPageId((Integer) termsDataVal[6]);
                        term.setTransUnitId((Integer) termsDataVal[7]);
                        term.setGsTermId((Integer) termsDataVal[8]);
                        term.setJobId((String) termsDataVal[9]);
                        term.setJobName((String) termsDataVal[10]);
                        term.setTaskId((String) termsDataVal[11]);
                        term.setTaskName((String) termsDataVal[12]);
                        term.setTotalVotes((Integer) termsDataVal[13]);
                        term.setInvites((Integer) termsDataVal[14]);

                        globalSightTermInfo.add(term);
                    }
                }

                logger.debug(" Total manage poll terms :"
                        + globalSightTermInfo.size());

                return globalSightTermInfo;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get terms by using page id
     *
     * @param pageId An Integer to get the term details
     * @return List of GlobalsightTermInfo  w.r.t pageId
     */
    @Override
    @Transactional
    public List<GlobalsightTermInfo> getTermsByPageId(Integer pageId)
            throws DataAccessException {
        List<GlobalsightTermInfo> globalsightTermInfoList = null;
         Session session = getHibernateSession();

        if (pageId == null) {
            return globalsightTermInfoList;
        }
        globalsightTermInfoList = new ArrayList<GlobalsightTermInfo>();
        Criteria crit = session.createCriteria(GlobalsightTermInfo.class).add(
                Restrictions.and(
                        Restrictions.eq("fileInfo.fileInfoId", pageId),
                        Restrictions.not(Restrictions.eq("isActive", "N"))));
        globalsightTermInfoList = (List<GlobalsightTermInfo>) crit.list();

        logger.debug("Got the globalsightTermInfoList :" + globalsightTermInfoList);
        return globalsightTermInfoList;
    }

    /**
     * To save the file
     *
     * @param fileInfo FileInfo that has to be saved
     * @return An integer value holding the fileInfoId
     */
    @Override
    public Integer saveFile(FileInfo fileInfo) throws DataAccessException {
        Integer fileInfoId = null;
        if (fileInfo == null) {
            return null;
        }
        fileInfo.setIsActive("Y");
        fileInfoId = (Integer) getHibernateTemplate().save(fileInfo);

        return fileInfoId;
    }

    /**
     * To get file info list
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter the  terms
     * @return List of terms
     */
    //@Transactional
    public List<GlobalsightTerms> getFileInfoList(final QueryAppender queryAppender, final Integer companyId) {
        HibernateCallback<List<GlobalsightTerms>> callback = new HibernateCallback<List<GlobalsightTerms>>()
        {

            @SuppressWarnings("deprecation")
            @Override
            public List<GlobalsightTerms> doInHibernate(Session session) throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<GlobalsightTerms> globalSightTermInfo = new ArrayList<GlobalsightTerms>();
                query.append(GET_IMPORT_FILE_INFO);
                if (companyId != null) {
                    query.append(" and gsinfo.company_id=" + companyId);
                }
                String colName = (queryAppender.getColName() == null) ? "" : queryAppender.getColName();
                String sortOrder = (queryAppender.getSortOrder() == null) ? "" : queryAppender.getSortOrder();
                Integer pageNum = queryAppender.getPageNum();
                if (colName.equalsIgnoreCase("fileId")) {
                    query.append(" order by file.file_id " + sortOrder);
                } else if (colName.equalsIgnoreCase("fileName")) {
                    query.append(" order by file.file_name "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("sourceLang")) {
                    query.append(" order by slang.language_label "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("targetLang")) {
                    query.append(" order by tlang.language_label "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("status")) {
                    query.append(" order by file.status "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("jobId")) {
                    query.append(" order by file.job_id "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("jobName")) {
                    query.append(" order by file.jobname "
                            + sortOrder);
                } else if (colName.equalsIgnoreCase("taskId")) {
                    query.append(" order by file.task_id "
                            + sortOrder);
                } else {
                    query.append(" order by file.file_info_id desc ");
                }


                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("D_FILE_INFO_ID", Hibernate.INTEGER);
                hibQuery.addScalar("FILE_ID", Hibernate.INTEGER);
                hibQuery.addScalar("FILE_NAME", Hibernate.STRING);
                hibQuery.addScalar("SOURCE_LANG", Hibernate.STRING);
                hibQuery.addScalar("TARGET_LANG", Hibernate.STRING);
                hibQuery.addScalar("STATUS", Hibernate.STRING);
                hibQuery.addScalar("JOB_ID", Hibernate.STRING);
                hibQuery.addScalar("JOBNAME", Hibernate.STRING);
                hibQuery.addScalar("TASK_ID", Hibernate.STRING);
                hibQuery.addScalar("TASKNAME", Hibernate.STRING);
                hibQuery.addScalar("EXPORT_LOG_URL", Hibernate.STRING);
                Object[] termsData = hibQuery.list().toArray();
                if ((termsData != null) && termsData.length > 0) {
                    for (int i = 0; i < termsData.length; i++) {
                        Object[] termsDataVal = (Object[]) termsData[i];
                        GlobalsightTerms fileInfo = new GlobalsightTerms();
                        fileInfo.setFileInfoId((Integer) termsDataVal[0]);
                        fileInfo.setFileId((Integer) termsDataVal[1]);
                        fileInfo.setFileName((String) termsDataVal[2]);
                        fileInfo.setSourceLang((String) termsDataVal[3]);
                        fileInfo.setTargetLang((String) termsDataVal[4]);
                        fileInfo.setStatus((String) termsDataVal[5]);
                        fileInfo.setJobId((String) termsDataVal[6]);
                        fileInfo.setJobName((String) termsDataVal[7]);
                        fileInfo.setTaskId((String) termsDataVal[8]);
                        fileInfo.setTaskName((String) termsDataVal[9]);
                        fileInfo.setExportLog((String) termsDataVal[10]);
                        globalSightTermInfo.add(fileInfo);
                    }
                }

                logger.debug(" Total manage poll terms :"
                        + globalSightTermInfo.size());

                return globalSightTermInfo;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To update file status
     *
     * @param fileInfo FileInfo that has to be updated
     */
    @Override
    public void updateFileStatus(FileInfo fileInfo) {
        getHibernateTemplate().update(fileInfo);

    }

    /**
     * To get file info
     *
     * @param pageId An Integer to get the fileInfo details
     * @return FileInfo w.r.t the pageId
     */
    @Override
    @Transactional
    public FileInfo getFileInfo(Integer pageId) {
         Session session = getHibernateSession();
        return (FileInfo) session.get(FileInfo.class, pageId);

    }

    /**
     * To get term information
     *
     * @param termId Integer which contains termId to be filtered
     * @return TermInformation w.r.t the term id
     */
    @Override
    public TermInformation getTermInformation(Integer termId)
            throws DataAccessException {
        return (TermInformation) getHibernateTemplate().get(TermInformation.class, termId);
    }

    /**
     * To update  term information
     *
     * @param termInfo TermInformation that has to be updated
     */
    @Override
    public void updateTermInfo(TermInformation termInfo)
            throws DataAccessException {
        getHibernateTemplate().update(termInfo);

    }

    /**
     * To get Total terms in TM
     *
     * @param isTM String which contains isTM to be filtered
     * @return An integer value holding the total no of terms in TM
     */
    @Override
    //@Transactional
    public Integer getTotalTermsInTermBaseTM(final String isTM) {
        HibernateCallback<Integer> callback = new HibernateCallback<Integer>()
        {

            @Override
            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Integer totalTermsInGlossary;
                StringBuffer query = new StringBuffer();
                query.append(GET_TOTAL_TERMS_IN_GLOSSARY);
                if (isTM != null && isTM != "" && isTM.equalsIgnoreCase("Y")) {
                    query.append(" and ti.is_tm='Y' order by ti.create_date desc ");
                } else if (isTM != null && isTM.equalsIgnoreCase("N")) {
                    query.append(" and ti.is_tm is null order by ti.create_date desc ");
                }
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                totalTermsInGlossary = new Integer(hibQuery.list().get(0)
                        .toString());
                logger.debug("No of terms in glossary :" + totalTermsInGlossary);
                return totalTermsInGlossary;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To verify whether TM  exists or not in database
     *
     * @param tmProfileInfo TmProfileInfo that has to be verify
     * @return Returns true if tm exists else it returns false
     */
    @Override
    @Transactional
    public Boolean isTMExists(TmProfileInfo tmProfileInfo)
            throws DataAccessException {
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(TmProfileInfo.class);
        criteria.add(Restrictions.eq("source", tmProfileInfo.getSource()));
        criteria.add(Restrictions.eq("targetLang.languageId", tmProfileInfo.getTargetLang().getLanguageId()));
        criteria.add(Restrictions.eq("isActive", "Y"));
        criteria.setProjection(Projections.count("tmProfileInfoId"));
        Integer countOfTerms = (Integer) criteria.uniqueResult();

        if (countOfTerms > 0) {
            return true;
        }

        return false;
    }

    /**
     * To add new tm
     *
     * @param tmProfileInfo TmProfileInfo that has to be added
     * @return Returns true if newTm added else it returns false
     */
    @Override
    public Boolean addNewTM(TmProfileInfo tmProfileInfo)
            throws DataAccessException {
        if (tmProfileInfo == null)
            return false;
        tmProfileInfo.setIsActive("Y");
        getHibernateTemplate().save(tmProfileInfo);
        return true;
    }

    /**
     * To get TM profile terms
     *
     * @param queryAppender Which is used to build a query
     * @param companyId     An Integer to  filter the  terms
     * @return List of terms
     */
    @Override
   // @Transactional
    public Object[] getTMProfileTerms(final QueryAppender queryAppender, final Integer companyId) {

        HibernateCallback<Object[]> callback = new HibernateCallback<Object[]>()
        {

            @SuppressWarnings("deprecation")
            @Override
            public Object[] doInHibernate(Session session)
                    throws HibernateException, SQLException {
                int limitFrom = 0;
                int limitTo = 0;
                String filtertBy = (queryAppender.getFilterBy() == null) ? ""
                        : queryAppender.getFilterBy();
                String filterByCompany = (queryAppender.getFilterByCompany() == null) ? ""
                        : queryAppender.getFilterByCompany();
                Integer[] intSelectedIds = queryAppender.getSelectedIds();
                Integer[] intSelectedCompanyIds = queryAppender.getSelectedCompanyIds();
                String selectedIds = "";
                String selectedCompanyIds = "";
                if (intSelectedIds != null && intSelectedIds.length != 0) {
                    for (int i = 0; i < intSelectedIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedIds += separator + intSelectedIds[i];
                    }

                }
                if (intSelectedCompanyIds != null && intSelectedCompanyIds.length != 0) {
                    for (int i = 0; i < intSelectedCompanyIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedCompanyIds += separator + intSelectedCompanyIds[i];
                    }

                }
                String likeValue = "";
                String tmSourceLikeValue = "";
                String tmTargetLikeValue = "";
                String searchBy = queryAppender.getSearchStr();
                String colName = (queryAppender.getColName() == null) ? ""
                        : queryAppender.getColName();
                String sortOrder = (queryAppender.getSortOrder() == null) ? ""
                        : queryAppender.getSortOrder();
                Integer pageNum = queryAppender.getPageNum();
                String isTM = queryAppender.getIsTM();
                StringBuffer query = new StringBuffer();
                List<TMProfileTerms> tmProfileTermsList = new ArrayList<TMProfileTerms>();
                query.append(GET_TM_PROFILE_TERMS);
                if (colName.equalsIgnoreCase("tmLanguage")) {
                    query.append(" left join (language_lookup lang ) on (tm.target_lang = lang.language_id)");
                } else if (colName.equalsIgnoreCase("product")) {
                    query.append("  left join (product_group_lookup prod) on (tm.product_line = prod.product_id)");
                } else if (colName.equalsIgnoreCase("domain")) {
                    query.append("  left join ( domain_lookup d ) on (tm.industry_domain = d.domain_id)");
                } else if (colName.equalsIgnoreCase("content")) {
                    query.append("  left join ( content_type_lookup con ) on (tm.content_type = con.content_type_id)");
                } else {
                    query.append("");
                }
                query.append(" where tm.is_active = 'Y' ");
                if (companyId != null) {
                    query.append("and tm.company=" + companyId);
                }

                if (searchBy != null && !searchBy.equalsIgnoreCase("null")) {

                    if (searchBy.indexOf('*') == 0) {
                        searchBy = searchBy.replace("*", LIKE_PERCENT);
                    } else {
                        searchBy = searchBy.replace("*", LIKE_UNDERSCORE);
                    }

                    if (searchBy.contains(" ")) {
                        StringTokenizer st = new StringTokenizer(searchBy, " ");
                        if (queryAppender.isCaseFlag()) {

                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                tmSourceLikeValue = tmSourceLikeValue
                                        + " tm.source like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";
                                tmTargetLikeValue = tmTargetLikeValue
                                        + " tm.target like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";
                            }
                        } else {

                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                tmSourceLikeValue = tmSourceLikeValue
                                        + " tm.source like _utf8 '"
                                        + searchValue + "%'"
                                        + " or ";
                                tmTargetLikeValue = tmTargetLikeValue
                                        + " tm.target like _utf8 '"
                                        + searchValue + "%'"
                                        + " or ";
                            }
                        }

                        if (tmSourceLikeValue.endsWith(" or ")) {
                            tmSourceLikeValue = tmSourceLikeValue
                                    .substring(0, tmSourceLikeValue
                                            .lastIndexOf("or"));
                        }
                        if (tmTargetLikeValue.endsWith(" or ")) {
                            tmTargetLikeValue = tmTargetLikeValue
                                    .substring(0, tmTargetLikeValue
                                            .lastIndexOf("or"));
                        }

                        query.append("and (( " + tmSourceLikeValue + "or"
                                + tmTargetLikeValue + "))");

                    } else {

                        likeValue = ((searchBy.indexOf(LIKE_UNDERSCORE) == -1) || (searchBy
                                .indexOf(LIKE_UNDERSCORE) == searchBy
                                .lastIndexOf('_'))) ? (searchBy + LIKE_PERCENT)
                                : (searchBy + " " + LIKE_PERCENT);

                        query.append(" and ((tm.source like _utf8 '"
                                + likeValue + "' ");

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(" or tm.target like _utf8 '"
                                + likeValue + "' ");

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(")");
                        query.append(")");
                    }
                }
                if (filtertBy.equalsIgnoreCase("Locale")) {
                    if (selectedIds != "" && selectedIds != "") {
                        query.append(" and tm.target_lang in ( "
                                + selectedIds + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Industry Domain")) {
                    if (selectedIds != "" && selectedIds != "") {
                        query.append("  and  tm.industry_domain in ( " + selectedIds
                                + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Product Line")) {
                    if (selectedIds != "" && selectedIds != "") {
                        query.append(" and  tm.product_line in ( "
                                + selectedIds + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Content Type")) {
                    if (selectedIds != "" && selectedIds != "") {
                        query.append(" and  tm.content_type in ( "
                                + selectedIds + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Company")) {
                    if (selectedIds != "" && selectedIds != "") {
                        query.append(" and  tm.company in ( "
                                + selectedIds + " ) ");
                    }
                }
                if (filterByCompany.equalsIgnoreCase("Company")) {
                    if (selectedCompanyIds != "" && selectedCompanyIds != null) {
                        query.append(" and  tm.company in ( "
                                + selectedCompanyIds + " ) ");
                    }
                }

                query.append(" group by tm.tm_profile_info_id ");


                if (colName.equalsIgnoreCase("sourceTerm")) {
                    query.append(" order by tm.source " + sortOrder);
                } else if (colName.equalsIgnoreCase("targetTerm")) {
                    query.append(" order by tm.target " + sortOrder);
                } else if (colName.equalsIgnoreCase("tmLanguage")) {
                    query.append("order by lang.language_label " + sortOrder);
                } else if (colName.equalsIgnoreCase("product")) {
                    query.append(" order by prod.product " + sortOrder);
                } else if (colName.equalsIgnoreCase("domain")) {
                    query.append("order by d.domain " + sortOrder);
                } else if (colName.equalsIgnoreCase("company")) {
                    query.append("order by company.company_name " + sortOrder);
                } else if (colName.equalsIgnoreCase("content")) {
                    query.append(" order by con.content_type " + sortOrder);
                } else {
                    query.append(" order by tm.tm_profile_info_id desc ");
                }


                if (pageNum != 0) {
                    limitFrom = (pageNum - 1) * 10;
                    limitTo = (pageNum) * 10;
                    query.append(" limit " + limitFrom + " , " + limitTo);
                } else {
                    query.append(" limit " + 0 + " , " + 10);
                }
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("TM_PROFILE_INFO_ID", Hibernate.INTEGER);
                hibQuery.addScalar("SOURCE", Hibernate.STRING);
                hibQuery.addScalar("TARGET", Hibernate.STRING);
                hibQuery.addScalar("INDUSTRY_DOMAIN", Hibernate.INTEGER);
                hibQuery.addScalar("PRODUCT_LINE", Hibernate.INTEGER);
                hibQuery.addScalar("TARGET_LANG", Hibernate.INTEGER);
                hibQuery.addScalar("CONTENT_TYPE", Hibernate.INTEGER);
                hibQuery.addScalar("COMPANY", Hibernate.INTEGER);

                Object[] termsData = hibQuery.list().toArray();

                return termsData;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get Total tm terms in Glossary
     *
     * @param companyId An String  to  filter the  terms
     * @return An integer value holding the total no of terms in glossary
     */
    @Override
   // @Transactional
    public Integer getTotalTermsInTM(final String companyIds) {
        HibernateCallback<Integer> callback = new HibernateCallback<Integer>()
        {

            @Override
            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Integer totalTermsInTmProfile;
                StringBuffer query = new StringBuffer();
                query.append(GET_TOTAL_TERMS_IN_TMPROFILE);
                if (companyIds != null) {
                    query.append(" and company in(" + companyIds + ")");
                }
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                totalTermsInTmProfile = new Integer(hibQuery.list().get(0)
                        .toString());
                logger.debug("No of terms in glossary :" + totalTermsInTmProfile);
                return totalTermsInTmProfile;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To get total tm terms
     *
     * @param exportBy    String to filter the terms
     * @param selectedIds Integer Array of filter values
     * @return List of terms
     */
    @Override
    @Transactional
    public List<TmProfileInfo> getTotalTermsInTM(String exportBy,
                                                 Integer[] selectedIds) throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TmProfileInfo.class);
        hibCriteria.add(Restrictions.not(Restrictions.eq("isActive", "N")));

        if (exportBy.equalsIgnoreCase("Locale")) {
            hibCriteria
                    .add(Restrictions.in("targetLang.languageId", selectedIds));
        } else if (exportBy.equalsIgnoreCase("Industry Domain")) {
            hibCriteria.add(Restrictions.in("domain.domainId",
                    selectedIds));
        } else if (exportBy.equalsIgnoreCase("Product Line")) {
            hibCriteria.add(Restrictions.in("productGroup.productId",
                    selectedIds));
        } else if (exportBy.equalsIgnoreCase("Content Type")) {
            hibCriteria.add(Restrictions.in("contentType.contentTypeId",
                    selectedIds));
        } else if (exportBy.equalsIgnoreCase("Company")) {
            hibCriteria.add(Restrictions.in("company.companyId",
                    selectedIds));
        }
        return (List<TmProfileInfo>) hibCriteria.list();
    }

    /**
     * To get tm attributes
     *
     * @param tmProfileInfoId an Integer to get details
     * @return TmProfileInfo w.r.t tmProfileInfoId
     */
    @Override
    @Transactional
    public TmProfileInfo getTmAttributes(final Integer tmProfileInfoId)
            throws DataAccessException {
        if (tmProfileInfoId == null) {
            return null;
        }
         Session session = getHibernateSession();

        TmProfileInfo tmProfileInfo = (TmProfileInfo) session.get(TmProfileInfo.class, tmProfileInfoId);

        return tmProfileInfo;

    }

    /**
     * To update tm details
     *
     * @param tmProfileInfoId TmProfileInfo that has to be updated
     */
    @Override
    public void updateTmDetails(TmProfileInfo tmProfileInfo)
            throws DataAccessException {
        if (tmProfileInfo == null) {
            throw new IllegalArgumentException("Invalid tmInformation");
        }

        getHibernateTemplate().update(tmProfileInfo);


    }

    /**
     * To delete tms terms
     *
     * @param termIds An Integer array that needs to be deleted
     */
    @Override
    @Transactional
    public void deleteTms(final Integer[] termIds) {
        if (termIds == null) {
            throw new IllegalArgumentException("Invalid termIds");
        }

        TmProfileInfo tmprofile = new TmProfileInfo();
         Session session = getHibernateSession();
        for (int i = 0; i < termIds.length; i++) {
            Integer termId = termIds[i];
            Criteria hibCriteria = session
                    .createCriteria(TmProfileInfo.class);
            hibCriteria.add(Restrictions.eq("tmProfileInfoId", termId));
            logger.debug("Got attributes for termId :" + termId);
            tmprofile = (TmProfileInfo) hibCriteria.uniqueResult();
            tmprofile.setIsActive("N");
            tmprofile.setUpdateDate(new Date());
            getHibernateTemplate().update(tmprofile);
            logger.debug("Successfully deleted term id :" + termId);


        }

    }

    /**
     * To update gs term details
     *
     * @param gsTermInformation GlobalsightTermInfo that has to be updated
     */
    @Override
    @Transactional
    public void updateGSTermDetails(GlobalsightTermInfo gsTermInformation)
            throws DataAccessException {
        if (gsTermInformation == null) {
            throw new IllegalArgumentException("Invalid termInformation");
        }

        getHibernateTemplate().update(gsTermInformation);


    }

    /**
     * To get gs term information using term id
     *
     * @param termId An Integer to get details
     * @return GlobalsightTermInfo w.r.t the term id
     */
    @Override
    @Transactional
    public GlobalsightTermInfo getGSTermInfoUsingTermId(Integer termId)
            throws DataAccessException {
        if (termId == null) {
            throw new IllegalArgumentException("Invalid termInformation");
        }

         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(GlobalsightTermInfo.class);
        hibCriteria
                .add(Restrictions.eq("termInformationId.termId", termId));
        GlobalsightTermInfo globalsightTermInfo = (GlobalsightTermInfo) hibCriteria.uniqueResult();
        session.close();
        return globalsightTermInfo;


    }


    /**
     * To get total gs terms
     *
     * @return A integer  value holding the total no of gs terms in Glossary
     */
    @Override
    @Transactional
    public Integer getTotalGSTerms() {
        HibernateCallback<Integer> callback = new HibernateCallback<Integer>()
        {

            @Override
            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Integer totalTermsInGS;
                StringBuffer query = new StringBuffer();
                query.append(GET_TOTAL_TERMS_IN_GS);

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                totalTermsInGS = new Integer(hibQuery.list().get(0)
                        .toString());
                logger.debug("No of terms in glossary :" + totalTermsInGS);
                return totalTermsInGS;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To save tm properties
     *
     * @param tmProperties TMProperties that has to be saved
     */
    @Override
    public void saveTmproperties(TMProperties tmProperties)
            throws DataAccessException {
        if (tmProperties == null)
            return;

        getHibernateTemplate().save(tmProperties);
    }

    /**
     * To get tm properties by reference
     *
     * @param propRef String to filter the properties
     * @return List of TMProperties  w.r.t propRef
     */
    @Transactional
    public List<TMProperties> getTMPropertiesByRef(String propRef)
            throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TMProperties.class);
        hibCriteria.add(Restrictions.eq("propRef", propRef));
        hibCriteria.add(Restrictions.eq("isTu", "N"));
        return (List<TMProperties>) hibCriteria.list();
    }

    /**
     * To get tm properties using tm profile id
     *
     * @param tmProfileId integer to filter the properties
     * @return List of TMProperties  w.r.t tmProfileId
     */
    @Transactional
    public List<TMProperties> getTMpropertiesUsingTMProfileId(Integer tmProfileId)
            throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TMProperties.class);
        hibCriteria.add(Restrictions.eq("tmProfileId", tmProfileId));
        hibCriteria.add(Restrictions.eq("isTu", "Y"));

        return (List<TMProperties>) hibCriteria.list();
    }


    /**
     * To update global sight file data
     *
     * @param fileInformation fileInformation that has to be updated
     */
    @Override
    public void updateGSFileData(FileInfo fileInformation)
            throws DataAccessException {
        if (fileInformation == null) {
            throw new IllegalArgumentException("Invalid termInformation");
        }

        getHibernateTemplate().update(fileInformation);


    }

    /**
     * To get gs term information using file info id
     *
     * @param tmProfileId integer to filter the terms
     * @return List of GlobalsightTermInfo w.r.t fileInfoId
     */
    @Override
    @Transactional
    public List<GlobalsightTermInfo> getGSTermInfoUsingFileInfoId(Integer fileInfoId)
            throws DataAccessException {
        if (fileInfoId == null) {
            throw new IllegalArgumentException("Invalid FileInformation");
        }

         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(GlobalsightTermInfo.class);
        hibCriteria
                .add(Restrictions.eq("fileInfo.fileInfoId", fileInfoId));
        return (List<GlobalsightTermInfo>) hibCriteria.list();

    }

    /**
     * To get tags by gs id
     *
     * @param gsId An Integer to  filter the  tags
     * @return List of tag  w.r.t gsId
     */
    @Override
    @Transactional
    public List<Tag> getTagsByGSId(Integer gsId) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Tag.class);
        hibCriteria
                .add(Restrictions.eq("globalsightTermInfo.globalsightTermInfoId", gsId));
        hibCriteria.addOrder(Order.asc("sortOrder"));

        return (List<Tag>) hibCriteria.list();

    }

    /**
     * To get attributes by tag id
     *
     * @param tagId integer to filter the attributes
     * @return List of Attributes w.r.t tagId
     */
    @Override
    @Transactional
    public List<Attributes> getAttributesByTagId(Integer tagId)
            throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(Attributes.class);
        hibCriteria
                .add(Restrictions.eq("tag.tagId", tagId));
        return (List<Attributes>) hibCriteria.list();
    }

    /**
     * To delete attributes
     *
     * @param gsIds set that needs to be deleted
     */
    @Override
    @Transactional
    public void deleteTags(Set<Integer> gsIds) {
         Session session = getHibernateSession();
        List<List<Integer>> batchList = createBatches(gsIds, 500);

        for (List<Integer> batch : batchList) {
            String values = "";
            String queryString = DELETE_TAG;
            for (Integer gsId : batch) {
                values = values + gsId + " , ";
            }
            values = values.substring(0, values.lastIndexOf(",") - 1);
            queryString = queryString + " in (" + values + ")";
            Query query = session.createQuery(queryString);
            int rows = query.executeUpdate();
            logger.debug("Successfully deleted" + rows + " rows");


        }
    }

    /**
     * To get tags using gs id
     *
     * @param gsIds Set of gsIds from which it is to be filtered
     * @return List of tag ids
     */
    @Override
    @Transactional
    public List<Integer> getTagsUsinggsIds(Set<Integer> gsIds)
            throws DataAccessException {
        List<Integer> tagList = null;
         Session session = getHibernateSession();

        if (gsIds == null) {
            return tagList;
        }
        tagList = new ArrayList<Integer>();
        Criteria crit = session
                .createCriteria(Tag.class)
                .add(Restrictions.in("globalsightTermInfo.globalsightTermInfoId", gsIds));
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("tagId"));
        crit.setProjection(proList);
        List list = crit.list();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object row = (Object) it.next();
            Integer id = (Integer) row;
            tagList.add(id);

        }
        logger.debug("Got the termInformationList :" + tagList);
        return tagList;
    }

    /**
     * To delete attributes
     *
     * @param tagids list that needs to be deleted
     */
    @Override
    @Transactional
    public void deleteAttributes(List<Integer> tagids) {
         Session session = getHibernateSession();
        List<List<Integer>> batchList = createBatches(tagids, 500);

        for (List<Integer> batch : batchList) {
            String values = "";
            String queryString = DELETE_ATTRIBUTES;
            for (Integer tagId : batch) {
                values = values + tagId + " , ";
            }
            values = values.substring(0, values.lastIndexOf(",") - 1);
            queryString = queryString + " in (" + values + ")";
            Query query = session.createQuery(queryString);
            int rows = query.executeUpdate();
            logger.debug("Successfully deleted" + rows + " rows");

        }
    }


    /**
     * To get Total tms information by search
     *
     * @param @param queryAppender Which is used to build a query
     * @return An integer value holding the total no of terms in glossary
     */
    @Override
   // @Transactional
    public Integer getTotalTMTermsBySearch(final QueryAppender queryAppender) {
        HibernateCallback<Integer> callback = new HibernateCallback<Integer>()
        {

            @Override
            public Integer doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Integer totalTermsInTMGlossary;
                StringBuffer query = new StringBuffer();
                String filtertBy = (queryAppender.getFilterBy() == null) ? ""
                        : queryAppender.getFilterBy();
                String filterByCompany = (queryAppender.getFilterByCompany() == null) ? ""
                        : queryAppender.getFilterByCompany();
                Integer[] intSelectedIds = queryAppender.getSelectedIds();
                Integer[] intSelectedCompanyIds = queryAppender.getSelectedCompanyIds();
                String tmSourceLikeValue = "";
                String tmTargetLikeValue = "";
                String selectedIds = "";
                String selectedCompanyIds = "";
                if (intSelectedIds != null && intSelectedIds.length != 0) {
                    for (int i = 0; i < intSelectedIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedIds += separator + intSelectedIds[i];
                    }

                }
                if (intSelectedCompanyIds != null && intSelectedCompanyIds.length != 0) {
                    for (int i = 0; i < intSelectedCompanyIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedCompanyIds += separator + intSelectedCompanyIds[i];
                    }

                }

                String searchBy = queryAppender.getSearchStr();
                String likeValue = "";
                query.append(GET_TOTAL_TERMS_IN_TMPROFILE);
                if (searchBy != null && searchBy != "") {

                    if (searchBy.contains(" ")) {
                        StringTokenizer st = new StringTokenizer(searchBy, " ");
                        while (st.hasMoreTokens()) {
                            String searchValue = st.nextToken();
                            if (queryAppender.isCaseFlag()) {
                                tmSourceLikeValue = tmSourceLikeValue + " tm.source like _utf8 '" + searchValue + " %' " + " collate utf8_bin or ";
                                tmTargetLikeValue = tmTargetLikeValue + " tm.target like _utf8 '" + searchValue + " %' " + " collate utf8_bin or ";
                            } else {
                                tmSourceLikeValue = tmSourceLikeValue + " tm.source like _utf8 '" + searchValue + " %'  or ";
                                tmTargetLikeValue = tmTargetLikeValue + " tm.target like _utf8 '" + searchValue + " %' or ";
                            }
                        }


                        if (tmSourceLikeValue.endsWith(" or ")) {
                            tmSourceLikeValue = tmSourceLikeValue
                                    .substring(0, tmSourceLikeValue
                                            .lastIndexOf("or"));
                        }
                        if (tmTargetLikeValue.endsWith(" or ")) {
                            tmTargetLikeValue = tmTargetLikeValue
                                    .substring(0, tmTargetLikeValue
                                            .lastIndexOf("or"));
                        }

                        query.append("and (( " + tmSourceLikeValue + "or"
                                + tmTargetLikeValue + "))");

                    } else {


                        likeValue = ((searchBy.indexOf(LIKE_UNDERSCORE) == -1) || (searchBy
                                .indexOf(LIKE_UNDERSCORE) == searchBy
                                .lastIndexOf('_'))) ? (searchBy + LIKE_PERCENT)
                                : (searchBy + " " + LIKE_PERCENT);

                        query.append(" and ((tm.source like _utf8 '"
                                + likeValue + "' ");
                        query.append(" or tm.target like _utf8 '"
                                + likeValue + "' ");

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(")");
                        query.append(")");
                    }
                }
                if (filtertBy.equalsIgnoreCase("Locale")) {
                    if (selectedIds != "" && selectedIds != null) {
                        query.append(" and tm.target_lang in ( "
                                + selectedIds + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Industry Domain")) {
                    if (selectedIds != "" && selectedIds != null) {
                        query.append("  and  tm.industry_domain in ( " + selectedIds
                                + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Product Line")) {
                    if (selectedIds != "" && selectedIds != null) {
                        query.append(" and  tm.product_line in ( "
                                + selectedIds + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Content Type")) {
                    if (selectedIds != "" && selectedIds != null) {
                        query.append(" and  tm.content_type in ( "
                                + selectedIds + " ) ");
                    }
                } else if (filtertBy.equalsIgnoreCase("Company")) {
                    if (selectedIds != "" && selectedIds != null) {
                        query.append(" and  tm.company in ( "
                                + selectedIds + " ) ");
                    }
                }
                if (filterByCompany.equalsIgnoreCase("Company")) {
                    if (selectedCompanyIds != "" && selectedCompanyIds != null) {
                        query.append(" and  tm.company in ( "
                                + selectedCompanyIds + " ) ");
                    }
                }

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                totalTermsInTMGlossary = new Integer(hibQuery.list().get(0)
                        .toString());
                logger.debug("No of terms in TM glossary:" + totalTermsInTMGlossary);
                return totalTermsInTMGlossary;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To get Terms in Glossary per year
     *
     * @return List of data holding year and no of terms per year
     */
    @Override
    //@Transactional
    public List<Terms> getTmsInGlossary(final String companyIds) {
        HibernateCallback<List<Terms>> callback = new HibernateCallback<List<Terms>>()
        {

            @Override
            public List<Terms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Terms> termsInGlossary = new ArrayList<Terms>();
                query.append(GET_TMS_IN_GLOSSARY);
                if (companyIds != null) {
                    query.append(" and company in(" + companyIds + ")");
                }
                query.append(" group by(year(create_date)) ");

                termsInGlossary = getTermDetails(query, session);

                logger.debug("Total glossary terms per year "
                        + termsInGlossary.size());
                return termsInGlossary;
            }

        };
        HibernateTemplate template = getHibernateTemplate();
        return template.execute(callback);
    }

    /**
     * To get quarterly tm details
     *
     * @return List of data holding quarter-year and no of terms per quarter
     */
    @Override
   // @Transactional
    public List<Terms> getQuarterlyTmDetails(final Integer companyId) {
        HibernateCallback<List<Terms>> callback = new HibernateCallback<List<Terms>>()
        {

            @Override
            public List<Terms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Terms> quarterlyTms = new ArrayList<Terms>();
                query.append(GET_QUARTERLY_TM_DETAILS);
                if (companyId != null) {
                    query.append(" and company=" + companyId);
                }
                query.append(" group by quarter(create_date),year(create_date) order by create_date");

                quarterlyTms = getTermDetailsQuarter(query, session);
                logger.debug(" Total quarterly tm details :"
                        + quarterlyTms.size());
                return quarterlyTms;

            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get monthly glossary terms
     *
     * @return List of data holding month and no of terms per month
     */
    @Override
    //@Transactional
    public List<Terms> getMonthlyTmDetails(final Integer companyId) {
        HibernateCallback<List<Terms>> callback = new HibernateCallback<List<Terms>>()
        {

            @Override
            public List<Terms> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<Terms> monthlyTms = new ArrayList<Terms>();
                query.append(GET_MONTHLY_TM_DETAILS);
                if (companyId != null) {
                    query.append(" and company=" + companyId);
                }
                query.append(" group by(month(create_date)) order by  create_date");
                monthlyTms = getTermDetails(query, session);
                logger.debug(" Total monthly term details :"
                        + monthlyTms.size());
                return monthlyTms;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To save import tmx file
     *
     * @param fileUpload FileUploadStatus  to be saved
     */
    public void saveImportTMXFile(FileUploadStatus fileUpload) {
        if (fileUpload == null) {
            return;
        }
        getHibernateTemplate().save(fileUpload);
    }

    /**
     * To save import tmx file url
     *
     * @param fileId String to filter FileUploadStatus  details
     */
    @Transactional
    public void saveImportTMXFileUrl(String filePath, String fileId) {
        if (fileId == null) {
            return;
        }
         Session session = getHibernateSession();
        FileUploadStatus fileUpload = (FileUploadStatus) session.get(FileUploadStatus.class, Integer.parseInt(fileId));
        fileUpload.setFileUrl(filePath);

        getHibernateTemplate().update(fileUpload);

    }

    /**
     * To get file upload status
     *
     * @param fileId Integer to filter FileUploadStatus  details
     * @return FileUploadStatus w.r.t fileId
     */
    @Override
    @Transactional
    public FileUploadStatus getFileUploadStatus(Integer fileId)
            throws DataAccessException {
        if (fileId == null) {
            throw new IllegalArgumentException("Invalid file Upload");
        }

         Session session = getHibernateSession();
        FileUploadStatus fileData = (FileUploadStatus) session.get(FileUploadStatus.class, fileId);
        return fileData;

    }

    /**
     * To save export tmx file url
     *
     * @param fileUpload FileUploadStatus to be saved
     * @return FileUploadStatus w.r.t fileUpload obj
     */
    @Override
    @Transactional
    public FileUploadStatus saveExportTMXFileUrl(FileUploadStatus fileUpload)
            throws DataAccessException {
        if (fileUpload == null) {
            throw new IllegalArgumentException("Invalid file Upload");
        }

         Session session = getHibernateSession();
        FileUploadStatus fileData = (FileUploadStatus) session.save(fileUpload);
        return fileData;

    }

    /**
     * To get FileUploadstatus details
     *
     * @param request   HttpServletRequest
     * @param colName   column name that has to be sorted
     * @param sortOrder order in which it has to be sorted
     * @param pageNum   Integer to limit the data
     * @return List of FileUploadstatus obj's
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public List<FileUploadStatus> getImportExportData(final Integer userId, final String colName, final String sortOrder, final Integer pageNum)
            throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(FileUploadStatus.class);
        hibCriteria.add(Restrictions.eq("createdBy", userId));
        int limitFrom = 0;
        int dataLimit = 10;
        if (pageNum != 0) {
            limitFrom = (pageNum - 1) * 10;
            hibCriteria.setFirstResult(limitFrom);
            hibCriteria.setMaxResults(dataLimit);
        }
        hibCriteria.addOrder(Order.desc("fileUploadStatusId"));
        return (List<FileUploadStatus>) hibCriteria.list();

    }

    /**
     * To update file upload status
     *
     * @param fileUpload FileUploadStatus to be updated
     */
    public void updateFileUploadStatus(FileUploadStatus fileUpload) {
        if (fileUpload == null) {
            return;
        }
        getHibernateTemplate().update(fileUpload);
    }

    /**
     * To build index for tmprofile
     *
     * @return List of TmProfileInfo
     */
    @Override
    @Transactional
    public List<TmProfileInfo> buildIndexForTmprofile() {
         Session session = getHibernateSession();
        String hqlQuery = "from com.teaminology.hp.bo.TmProfileInfo";

        Query query = session.createQuery(hqlQuery);
        return (List<TmProfileInfo>) query.list();
    }

    /**
     * To get tmprofileInfo by propref(property Reference)
     *
     * @param propRef String to be filtered the tm profile information
     * @return List of TmProfileInfo w.r.t propRef
     */
    @Override
    @Transactional
    public List<TmProfileInfo> getTMProfileInfoByPropRef(String propRef)
            throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TmProfileInfo.class);
        hibCriteria.add(Restrictions.eq("propRef", propRef));

        return (List<TmProfileInfo>) hibCriteria.list();
    }

    /**
     * To get all jobs
     *
     * @return List of FileInfo obj's
     */

    @Override
    //@Transactional
    public List<FileInfo> getAllJobs() {
        HibernateCallback<List<FileInfo>> callback = new HibernateCallback<List<FileInfo>>()
        {

            @Override
            public List<FileInfo> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
                query.append(GET_DISTINCT_JOBS);
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("JOB_ID", Hibernate.STRING);
                hibQuery.addScalar("JOBNAME", Hibernate.STRING);
                Object[] fileInfoData = hibQuery.list().toArray();
                if ((fileInfoData != null)
                        && fileInfoData.length > 0) {
                    for (int i = 0; i < fileInfoData.length; i++) {
                        Object[] fileInfoDataVal = (Object[]) fileInfoData[i];
                        String jobId = (fileInfoDataVal[0] == null) ? ""
                                : fileInfoDataVal[0].toString();
                        String jobName = (fileInfoDataVal[1] == null) ? ""
                                : fileInfoDataVal[1].toString();
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setJobId(jobId);
                        fileInfo.setJobName(jobName);

                        fileInfoList.add(fileInfo);
                    }
                }
                return fileInfoList;
            }
        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get file information by jobIds
     *
     * @param jobIds String Array of filter jobIds
     * @return List of FileInfo w.r.t jobIds
     */
    @Override
    @Transactional
    public List<FileInfo> getFileInfoByJobIds(String[] jobIds) {
        if (jobIds == null) {

        }
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(FileInfo.class);
        hibCriteria.add(Restrictions.in("jobId", jobIds));
        return (List<FileInfo>) hibCriteria.list();

    }

    /**
     * To get file information by task id
     *
     * @param taskId Integer of filter task ids
     * @return FileInfo  w.r.t taskId
     */
    @Override
    @Transactional
    public FileInfo getFileInfoByTaskId(Integer taskId) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(FileInfo.class);
        hibCriteria.add(Restrictions.eq("taskId", taskId.toString()));
        List<FileInfo> fileInfoList = hibCriteria.list();
        return (FileInfo) ((fileInfoList != null && fileInfoList
                .size() > 0) ? fileInfoList.get(0) : null);

    }

    /**
     * To get termInformation by term ids
     * <p/>
     * termIds selectedIds Integer Array of filter termIds
     *
     * @return List of TermInformation w.r.t termIds
     */
    @Override
    @Transactional
    public List<TermInformation> getTermInfoByTermIds(Integer[] termIds) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TermInformation.class);
        hibCriteria.add(Restrictions.in("termId", termIds));
        return (List<TermInformation>) hibCriteria.list();

    }

    /**
     * To get user by user ids
     *
     * @param userIds Integer array to filter user ids
     * @return List of User w.r.t the userIds
     */
    @Override
    @Transactional
    public List<User> getUsersByuserIdsList(Integer[] userIds) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(User.class);
        hibCriteria.add(Restrictions.in("userId", userIds));
        return (List<User>) hibCriteria.list();
    }

    /**
     * To get all terms
     *
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @param companyId     An Integer to  filter the  terms
     * @return List of term ids that needs to manage
     */
    @Override
    //@Transactional
    public List<Integer> getAllTerms(final QueryAppender queryAppender, final Integer companyId) throws DataAccessException {
        HibernateCallback<List<Integer>> callback = new HibernateCallback<List<Integer>>()
        {
            @SuppressWarnings("deprecation")
            @Override
            public List<Integer> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                List<Integer> totalTerms = new ArrayList<Integer>();
                String filtertBy = (queryAppender.getFilterBy() == null) ? ""
                        : queryAppender.getFilterBy();
                String filtertByCompany = (queryAppender.getFilterByCompany() == null) ? ""
                        : queryAppender.getFilterByCompany();
                Integer[] intSelectedIds = queryAppender.getSelectedIds();
                Integer[] intSelectedCompanyIds = queryAppender.getSelectedCompanyIds();
                String selectedIds = "";
                String selectedCompanyIds = "";
                if (intSelectedIds != null && intSelectedIds.length != 0) {
                    for (int i = 0; i < intSelectedIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedIds += separator + intSelectedIds[i];
                    }

                }

                if (intSelectedCompanyIds != null && intSelectedCompanyIds.length != 0) {
                    for (int i = 0; i < intSelectedCompanyIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedCompanyIds += separator + intSelectedCompanyIds[i];
                    }

                }
                selectedIds = selectedIds.trim();


                String likeValue = "";
                String termSourcelikeValue = "";
                String termTargetlikeValue = "";
                String depSourcelikeValue = "";
                String depTarglikeValue = "";
                String searchBy = queryAppender.getSearchStr();
                String colName = (queryAppender.getColName() == null) ? ""
                        : queryAppender.getColName();
                String sortOrder = (queryAppender.getSortOrder() == null) ? ""
                        : queryAppender.getSortOrder();
                Integer pageNum = queryAppender.getPageNum();
                String isTM = queryAppender.getIsTM();
                StringBuffer query = new StringBuffer();
                List<Integer> userPollTerms = new ArrayList<Integer>();
                query.append(GET_TOTAL_TERM_IDS);
                if (companyId != null) {
                    query.append(" and ti.company_id = " + companyId + " ");
                }

                if (searchBy != null && !searchBy.equalsIgnoreCase("null")) {

                    if (searchBy.indexOf('*') == 0) {
                        searchBy = searchBy.replace("*", LIKE_PERCENT);
                    } else {
                        searchBy = searchBy.replace("*", LIKE_UNDERSCORE);
                    }

                    if (searchBy.contains(" ")) {
                        StringTokenizer st = new StringTokenizer(searchBy, " ");
                        if (queryAppender.isCaseFlag()) {

                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                termSourcelikeValue = termSourcelikeValue
                                        + " ti.term_being_polled like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";
                                termTargetlikeValue = termTargetlikeValue
                                        + " ti.suggested_term like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";
                                depSourcelikeValue = depSourcelikeValue
                                        + " dt.deprecated_source like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";
                                depTarglikeValue = depTarglikeValue
                                        + " dt.deprecated_target like _utf8 ' "
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";

                            }
                        } else {

                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                termSourcelikeValue = termSourcelikeValue
                                        + " ti.term_being_polled like _utf8 '"
                                        + searchValue + "%'" + " or ";
                                termTargetlikeValue = termTargetlikeValue
                                        + " ti.suggested_term like _utf8 '"
                                        + searchValue + "%'" + " or ";
                                depSourcelikeValue = depSourcelikeValue
                                        + " dt.deprecated_source like _utf8 '"
                                        + searchValue + "%'" + " or ";
                                depTarglikeValue = depTarglikeValue
                                        + " dt.deprecated_target like _utf8 ' "
                                        + searchValue + "%'" + " or ";

                            }
                        }
                        if (termSourcelikeValue.endsWith(" or ")) {
                            termSourcelikeValue = termSourcelikeValue
                                    .substring(0, termSourcelikeValue
                                            .lastIndexOf("or"));
                        }
                        if (termTargetlikeValue.endsWith(" or ")) {
                            termTargetlikeValue = termTargetlikeValue
                                    .substring(0, termTargetlikeValue
                                            .lastIndexOf("or"));
                        }
                        if (depSourcelikeValue.endsWith(" or ")) {
                            depSourcelikeValue = depSourcelikeValue.substring(
                                    0, depSourcelikeValue.lastIndexOf("or"));
                        }
                        if (depTarglikeValue.endsWith(" or ")) {
                            depTarglikeValue = depTarglikeValue.substring(0,
                                    depTarglikeValue.lastIndexOf("or"));
                        }

                        query.append("and (( " + termSourcelikeValue + "or"
                                + termTargetlikeValue + ") or ( "
                                + depSourcelikeValue + "or" + depTarglikeValue);
                        query.append("))");
                    } else {
                        likeValue = ((searchBy.indexOf(LIKE_UNDERSCORE) == -1) || (searchBy
                                .indexOf(LIKE_UNDERSCORE) == searchBy
                                .lastIndexOf('_'))) ? (searchBy + LIKE_PERCENT)
                                : (searchBy + " " + LIKE_PERCENT);

                        likeValue = "'" + likeValue + "'";

                        query.append(" and ((ti.term_being_polled like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(" or ti.suggested_term like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(")");

                        query.append(" or ( dt.deprecated_source like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }

                        query.append(" or dt.deprecated_target like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }

                        query.append("))");
                    }
                }
                if (filtertBy.equalsIgnoreCase("Locale")) {
                    if (selectedIds != "" && selectedIds != null) {
                        query.append(" and ti.suggested_term_lang_id in ( "
                                + selectedIds + " ) ");
                    }
                } else if (selectedCompanyIds != null && selectedCompanyIds != "") {
                    if (filtertByCompany.equalsIgnoreCase("Company")) {
                        query.append(" and  ti.company_id in ( " + selectedCompanyIds
                                + " ) ");
                    }
                }

                query.append(" group by ti.term_id ");
                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
                Object[] termsData = hibQuery.list().toArray();
                Integer[] intArray = new Integer[termsData.length];
                if ((termsData != null) && termsData.length > 0) {
                    for (int i = 0; i < termsData.length; i++) {
                        intArray[i] = (Integer) termsData[i];
                        totalTerms.add(intArray[i]);
                    }
                }

                return totalTerms;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get total global sight terms
     *
     * @param queryAppender QueryAppender holding column name to be sorted and the filter criteria
     * @param companyId     An Integer to  filter the  terms
     * @return List of global sight term ids that needs to manage
     */
    @Override
   // @Transactional
    public List<Integer> getTotalGSTerms(final QueryAppender queryAppender, final Integer companyId) throws DataAccessException {
        HibernateCallback<List<Integer>> callback = new HibernateCallback<List<Integer>>()
        {
            @SuppressWarnings("deprecation")
            @Override
            public List<Integer> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                int limitFrom = 0;
                int limitTo = 0;
                List<Integer> totalTerms = new ArrayList<Integer>();
                String filtertBy = (queryAppender.getFilterBy() == null) ? ""
                        : queryAppender.getFilterBy();
                Integer[] intSelectedIds = queryAppender.getSelectedIds();
                Integer[] intcompanyIds = queryAppender.getSelectedCompanyIds();
                Integer[] intlangIds = queryAppender.getSelectedLangIds();
                String selectedIds = "";
                String selectedlangIds = "";
                String selectedcompanyIds = "";
                String searchBy = queryAppender.getSearchStr();
                if (intSelectedIds != null && intSelectedIds.length != 0) {
                    for (int i = 0; i < intSelectedIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedIds += separator + intSelectedIds[i];
                    }

                }
                if (intcompanyIds != null && intcompanyIds.length != 0) {
                    for (int i = 0; i < intcompanyIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedcompanyIds += separator + intcompanyIds[i];
                    }

                }
                if (intlangIds != null && intlangIds.length != 0) {
                    for (int i = 0; i < intlangIds.length; i++) {
                        String separator = (i == 0) ? "" : ",";
                        selectedlangIds += separator + intlangIds[i];
                    }

                }

                query.append(GET_TOTAL_GS_TERMS);

                if (companyId != null) {
                    query.append(" and gt.company_id=" + companyId);
                }
                String likeValue = "";
                String termSourcelikeValue = "";
                String termTargetlikeValue = "";

                if (searchBy != null && !searchBy.equalsIgnoreCase("null")) {

                    if (searchBy.indexOf('*') == 0) {
                        searchBy = searchBy.replace("*", LIKE_PERCENT);
                    } else {
                        searchBy = searchBy.replace("*", LIKE_UNDERSCORE);
                    }

                    if (searchBy.contains(" ")) {
                        StringTokenizer st = new StringTokenizer(searchBy, " ");
                        if (queryAppender.isCaseFlag()) {
                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                termSourcelikeValue = termSourcelikeValue
                                        + " gt.source_segment like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";
                                termTargetlikeValue = termTargetlikeValue
                                        + " gt.target_segment like _utf8 '"
                                        + searchValue + "%'"
                                        + " collate utf8_bin  or ";


                            }
                        } else {

                            while (st.hasMoreTokens()) {
                                String searchValue = st.nextToken();
                                termSourcelikeValue = termSourcelikeValue
                                        + " gt.source_segment like _utf8 '"
                                        + searchValue + "%'" + " or ";
                                termTargetlikeValue = termTargetlikeValue
                                        + " gt.target_segment like _utf8 '"
                                        + searchValue + "%'" + " or ";

                            }
                        }
                        if (termSourcelikeValue.endsWith(" or ")) {
                            termSourcelikeValue = termSourcelikeValue
                                    .substring(0, termSourcelikeValue
                                            .lastIndexOf("or"));
                        }
                        if (termTargetlikeValue.endsWith(" or ")) {
                            termTargetlikeValue = termTargetlikeValue
                                    .substring(0, termTargetlikeValue
                                            .lastIndexOf("or"));
                        }


                        query.append("and ( " + termSourcelikeValue + "or"
                                + termTargetlikeValue + ")");
                    } else {
                        likeValue = ((searchBy.indexOf(LIKE_UNDERSCORE) == -1) || (searchBy
                                .indexOf(LIKE_UNDERSCORE) == searchBy
                                .lastIndexOf('_'))) ? (searchBy + LIKE_PERCENT)
                                : (searchBy + " " + LIKE_PERCENT);

                        likeValue = "'" + likeValue + "'";

                        query.append(" and (gt.source_segment like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(" or gt.target_segment like _utf8 "
                                + likeValue);

                        if (queryAppender.isCaseFlag()) {
                            query.append(" collate utf8_bin ");
                        }
                        query.append(")");


                    }
                }
                String colName = (queryAppender.getColName() == null) ? ""
                        : queryAppender.getColName();
                String sortOrder = (queryAppender.getSortOrder() == null) ? ""
                        : queryAppender.getSortOrder();
                Integer pageNum = queryAppender.getPageNum();

                if (selectedcompanyIds != "" && selectedcompanyIds != null) {

                    query.append(" and  gt.company_id in ( " + selectedcompanyIds
                            + " ) ");
                }
                if (selectedIds != "" && selectedIds != null) {

                    query.append(" and  fi.task_id in ( " + selectedIds
                            + " ) ");
                }
                if (selectedlangIds != "" && selectedlangIds != null) {

                    query.append(" and  fi.target_lang in ( " + selectedlangIds
                            + " ) ");
                }

                query.append(" order by gt.globalsight_term_info_id desc ");


                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
                Object[] termsData = hibQuery.list().toArray();
                Integer[] intArray = new Integer[termsData.length];
                if ((termsData != null) && termsData.length > 0) {
                    for (int i = 0; i < termsData.length; i++) {
                        intArray[i] = (Integer) termsData[i];
                        totalTerms.add(intArray[i]);
                    }
                }

                return totalTerms;
            }

        };
        return getHibernateTemplate().execute(callback);
    }

    /**
     * To get company id  by tmId
     *
     * @param tmId Integer to filter company  Details
     * @return An Integer value holding the count of company id's
     */
    @Override
    @Transactional
    public Integer getCompanyIdBytmId(Integer tmId) throws DataAccessException {

         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(TmProfileInfo.class);
        criteria.add(Restrictions.eq("tmProfileInfoId", tmId));
        criteria.setProjection(Projections.property("company.companyId"));
        return (Integer) criteria.uniqueResult();

    }

    /**
     * To get voting details  by termIds
     *
     * @param termIds String to filter voting details  Details
     * @return List of terms  w.r.t the termIds
     */
    @Override
    @Transactional
    public List<PollTerms> getVotingDetailsByTermIds(final String termIds) {

        HibernateCallback<List<PollTerms>> callback = new HibernateCallback<List<PollTerms>>()
        {

            @SuppressWarnings("deprecation")
            @Override
            public List<PollTerms> doInHibernate(Session session)
                    throws HibernateException, SQLException {

                StringBuffer query = new StringBuffer();
                List<PollTerms> userPollTerms = new ArrayList<PollTerms>();
                query.append(GET_VOTING_POLL_TERMS);
                query.append(termIds + ")");

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                hibQuery.addScalar("TERM_ID", Hibernate.INTEGER);
                hibQuery.addScalar("ALL_VOTES", Hibernate.INTEGER);
                hibQuery.addScalar("INVITES", Hibernate.INTEGER);
                hibQuery.addScalar("IS_DEPRECATE", Hibernate.INTEGER);
                Object[] termsData = hibQuery.list().toArray();
                if ((termsData != null) && termsData.length > 0) {
                    for (int i = 0; i < termsData.length; i++) {
                        Object[] termsDataVal = (Object[]) termsData[i];
                        PollTerms term = new PollTerms();
                        term.setTermId((Integer) termsDataVal[0]);
                        term.setVotesPerTerm((Integer) termsDataVal[1]);
                        term.setInvites((Integer) termsDataVal[2]);
                        term.setDeprecatedCount((Integer) termsDataVal[3]);
                        userPollTerms.add(term);
                    }
                }

                logger.debug(" Total manage poll terms :"
                        + userPollTerms.size());
                return userPollTerms;
            }

        };
        return getHibernateTemplate().execute(callback);


    }

    /**
     * To get deprecated term information  by termId
     *
     * @param termId Integer to filter deprecated term information  Details
     * @return List of DeprecatedTermInformation   w.r.t the termId
     */
    @Transactional
    public List<DeprecatedTermInformation> getDeprecatedTermInfoByTermId(Integer termId) {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(DeprecatedTermInformation.class);
        hibCriteria.add(Restrictions.eq("termInfo.termId", termId));
        return (List<DeprecatedTermInformation>) hibCriteria.list();
    }

    /**
     * To get List of TmProfileInfo Objects
     *
     * @param ids List of Integers to be filtered
     * @return List of TmProfileInfo  w.r.t the tmProfileInfoIds
     */
    @Override
    @Transactional
    public List<TmProfileInfo> getTmProfileInfoByIds(List<Integer> ids) {

         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(TmProfileInfo.class);
        criteria.add(Restrictions.in("tmProfileInfoId", ids));

        return (List<TmProfileInfo>) criteria.list();

    }

    /**
     * To get List of CompanyTransMgmt Objects
     *
     * @param userId to be filtered
     * @return List of CompanyTransMgmt  w.r.t the userId
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
     * To delete import files
     *
     * @param fileIds array of integer fileIds that needs to be deleted
     */
    @Override
    @Transactional
    public void deleteImportFiles(final Integer[] fileIds) throws DataAccessException {

        if (fileIds == null) {
            return;
        }
        StringBuffer query = new StringBuffer();
         Session session = getHibernateSession();
        query.append(DELETE_IMPORTED_FILES);
        String ids = null;

        for (int i = 0; i < fileIds.length; i++) {
            if (i == 0) {
                ids = fileIds[i].toString();
            } else {
                ids = ids + "," + fileIds[i].toString();
            }

        }
        query.append(ids + ")");
        Query hibQuery = session.createQuery(query.toString());
        hibQuery.executeUpdate();

        logger.debug("Deleted import files");

    }

    /**
     * To get import  status files by fileIds
     *
     * @param fileIds Integer array  to  filter the  import files status
     * @return List of import files obj w.r.t fileIds
     */

    @Override
    @Transactional
    public List<FileUploadStatus> getImportStatusFiles(Integer[] fileIds) {

         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(FileUploadStatus.class);
        criteria.add(Restrictions.in("fileUploadStatusId", fileIds));

        return (List<FileUploadStatus>) criteria.list();

    }

    /**
     * To delete tms terms
     *
     * @param termIds An arryList that needs to be deleted
     * @throws DataAccessException
     */
    @Override
    @Transactional
    public void deleteTms(final List<Integer> tmIds) {

         Session session = getHibernateSession();

        String values = "";
        String queryString = DELETE_TM_PROFILE_INFO;
        for (Integer tmId : tmIds) {
            values = values + tmId + " , ";
        }
        values = values.substring(0, values.lastIndexOf(",") - 1);
        queryString = queryString + " in (" + values + ")";
        Query query = session.createQuery(queryString);
        int rows = query.executeUpdate();
        logger.debug("Successfully deleted" + rows + " rows");


    }

    /**
     * To delete TmProperties
     *
     * @param termIds An arryList that needs to be deleted
     * @throws DataAccessException
     */
    @Override
    @Transactional
    public void deleteTmProperties(final List<Integer> tmIds) {


         Session session = getHibernateSession();

        String values = "";
        String queryString = DELETE_TM_PROPERTIES;
        for (Integer tmId : tmIds) {
            values = values + tmId + " , ";
        }
        values = values.substring(0, values.lastIndexOf(",") - 1);
        queryString = queryString + " in (" + values + ")";
        Query query = session.createQuery(queryString);
        int rows = query.executeUpdate();
        logger.debug("Successfully deleted" + rows + " rows");


    }

    /**
     * To create batches
     *
     * @param collection
     * @param batchSize  An Integer of  batch size
     * @throws DataAccessException
     */
    public static <C extends Object> List<List<C>> createBatches(
            Collection<C> collection, int batchSize) {
        if (batchSize < 1)
            throw new IllegalArgumentException("Batch size cannot be zero or less");

        List<C> objects = new ArrayList<C>(collection);
        int noOfBatches = (int) Math.ceil((double) objects.size() / batchSize);
        List<List<C>> batches = new ArrayList<List<C>>(noOfBatches);
        int index = 0;
        for (int i = 0; i < noOfBatches; i++) {
            List<C> list = new ArrayList<C>(batchSize);
            for (int j = 0; j < batchSize && index < objects.size(); j++, index++) {
                list.add(objects.get(index));
            }
            batches.add(list);
        }
        return batches;
    }

    /**
     * To get suggested term lang id
     *
     * @param termId Integer to be filtered
     * @return An Integer value holding the count of  user registered languages
     */
    @Override
    @Transactional
    public Integer getSuggestedTermLangId(Integer termId) {
         Session session = getHibernateSession();

        if (termId == null) {
            return 0;
        }
        Criteria criteria = session.createCriteria(TermInformation.class);
        criteria.add(Restrictions.eq("termId", termId));
        TermInformation termInfomation = (TermInformation) criteria.uniqueResult();
        Integer langId = termInfomation.getSuggestedTermLangId();
        logger.debug("Got the suggested term lang id :" + langId);
        return langId;
    }

    /**
     * To get user register languages
     *
     * @param userId Integer to be filtered
     * @return List of languages in which user is registered
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
     * To get ids of terms belongs to given companyid
     *
     * @param companyId of a company
     * @return termIds List of integer
     */
    @Override
    @Transactional
    public List<Integer> getTermIdsByCompanyId(Integer companyId) {
        if (companyId == null)
            return null;
         Session session = getHibernateSession();
        List<Integer> termInformationList = new ArrayList<Integer>();

        Criteria crit = session.createCriteria(TermInformation.class);
        if (companyId != null)
            crit.add(Restrictions.eq("termCompany.companyId", companyId));
        ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("termId"));
        crit.setProjection(proList);
        List list = crit.list();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object row = (Integer) it.next();
            Integer id = (Integer) row;
            termInformationList.add(id);
        }
        logger.debug("Got the termInformationList :" + termInformationList);
        return termInformationList;

    }

    /**
     * return GlobalsightTermInfo object using gsTermids.
     *
     * @param gsids lis of Gs term ids.
     * @return Globalsight termInformation list
     */
    @Override
    //@Transactional
    public List<GlobalsightTermInfo> getGSTermsByTermIdsList(List<Integer> gsids) {
        if (gsids == null)
            return null;
         Session session = getHibernateSession();
        Criteria criteria = session.createCriteria(GlobalsightTermInfo.class);
        criteria.add(Restrictions.in("globalsightTermInfoId", gsids));

        return (List<GlobalsightTermInfo>) criteria.list();


    }
    /**
     * #TNG-81 And #HPTC-40 
     * To get all alternative translations for given term id
     */
    @SuppressWarnings("unchecked")
	@Override
    @Transactional
    public List<TermTranslation> getAlternativeTranslations(Integer termId) throws DataAccessException {
         Session session = getHibernateSession();
        Criteria hibCriteria = session.createCriteria(TermTranslation.class);
        hibCriteria.add(Restrictions.eq("termId", termId));
        return (List<TermTranslation>) hibCriteria.list();
    }
    /**
     * Getting total votes of user
     * To send reward mail when user gets new rank
     * @param user_id
     * @return
     */
    @Override
    public Integer getTotalVotesOfUser(final Integer user_id) {
    	HibernateCallback<Integer> callback = new HibernateCallback<Integer>() {
    		@Override
    		public Integer doInHibernate(Session session)
    				throws HibernateException, SQLException {
    			StringBuffer query = new StringBuffer();
    			query.append(GET_USER_TOTAL_VOTES);
    		    SQLQuery sqlQuery = session.createSQLQuery(query.toString());
    		    sqlQuery.setParameter("param0", user_id);
    		    Integer userTotalVotes = new Integer(sqlQuery.list().get(0).toString());
    		    logger.info("Total votes of user For ranking reward "+userTotalVotes);
    			return userTotalVotes;
    		}
		};
		return getHibernateTemplate().execute(callback);
    }
  
    @Override
    public List<Member> getAllBoardMembersByLanguage(final Integer companyId,final String userLangId) {
        HibernateCallback<List<Member>> callback = new HibernateCallback<List<Member>>()
        {

            @Override
            public List<Member> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                StringBuffer query = new StringBuffer();
                query.append(GET_LEADER_BOARD_MEMBERS_BY_LANGUAGE);
                if (companyId != null) {
                    query.append(" and usr.company_id=" + companyId);
                }
                query.append( /* and ti.suggested_term_lang_id in(" + userLangId.toString() +") */ " and  ul.language_id in (" + userLangId.toString() + ") group by usr.user_id order by votes desc, usr.user_name asc");

                SQLQuery hibQuery = session.createSQLQuery(query.toString());
                List<Object> hibernateResults = hibQuery.list();
                List<Member> boardMembersList = new ArrayList<Member>();

                for (Object obj : hibernateResults) {
                    Object[] member = (Object[]) obj;
                    int colNdx = 0;
                    Member boardMember = new Member();

                    if ((Integer) member[colNdx] != null) {
                        boardMember.setUserId((Integer) member[colNdx]);
                    }
                    colNdx++;
                    if ((String) member[colNdx] != null) {
                        boardMember.setUserName((String) member[colNdx]);
                    }
                    colNdx++;

                    if ((String) member[colNdx] != null) {
                        boardMember.setFirstName((String) member[colNdx]);
                    }
                    colNdx++;

                    if ((String) member[colNdx] != null) {
                        boardMember.setLastName((String) member[colNdx]);
                    }
                    colNdx++;

                    if (((String) member[colNdx] != null)
                            && (((String) member[colNdx]).trim().length() != 0)) {
                        boardMember.setPhotoPath((String) member[colNdx]);
                    }
                    colNdx++;

                    if ((Integer) member[colNdx] != null) {
                        boardMember
                                .setTermRequestCount((Integer) member[colNdx]);
                    } else {
                        boardMember.setTermRequestCount(0);
                    }
                    colNdx++;

                    if ((BigInteger) member[colNdx] != null) {
                        boardMember.setTotalVotes((BigInteger) member[colNdx]);
                    }
                    colNdx++;
                    boardMembersList.add(boardMember);

                }

                logger.debug("Total Leader Board Members results :"
                        + boardMembersList.size());
                return boardMembersList;
            }

        };
        return getHibernateTemplate().execute(callback);
    }
    
    @Override
    @Transactional
    public List<TermUpdateDetails> getTermUpdateHistoryList(
    		List<Integer> termIdsList, String toDate, String fromDate) {
    	if (termIdsList == null) {
    		return null;
    	}
    	  java.util.Date toDate1 = null;
    	  java.util.Date fromDate1 = null;
    	  java.sql.Date toHistDate = null;
    	  java.sql.Date fromHitsDate = null;
    	  
    	  if(toDate != null && fromDate != null) {
    		  DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    		  try {
    			  toDate1 = format.parse(toDate);
    		  } catch (ParseException e) {
    			  // TODO Auto-generated catch block
    			  e.printStackTrace();
    		  }
    		  toHistDate = new java.sql.Date(toDate1.getTime());
    		  try {
    			  fromDate1 = format.parse(fromDate);
    		  } catch (ParseException e) {
    			  // TODO Auto-generated catch block
    			  e.printStackTrace();
    		  }
    		  fromHitsDate = new java.sql.Date(fromDate1.getTime());
    	  }
    	Session session = getHibernateSession();
    	Criteria criteria = session.createCriteria(TermUpdateDetails.class);
    	criteria.add(Restrictions.in("termId", termIdsList));
    	criteria.add(Restrictions.between("createDate", fromHitsDate, toHistDate));

    	return (List<TermUpdateDetails>) criteria.list();

    }
    
}
