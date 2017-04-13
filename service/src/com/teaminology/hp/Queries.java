package com.teaminology.hp;

public class Queries
{
    protected static final String LS = System.getProperty("line.separator");

    protected static String companyQuery = "SELECT * FROM company_lookup where is_active='Y'";
    protected static String languageQuery = "SELECT * FROM language_lookup where is_active='Y'";
    protected static String domainQuery = "SELECT * FROM domain_lookup where is_active='Y'";
    protected static String contentTypeQuery = "SELECT * FROM content_type_lookup where is_active='Y'";
    protected static String productGroupQuery = "SELECT * FROM product_group_lookup where is_active='Y'";
    protected static String partsOfSpeechQuery = "SELECT * FROM parts_of_speech_lookup where is_active='Y'";
    protected static String categoryQuery = "SELECT * FROM category_lookup where is_active='Y'";
    protected static String statusQuery = "SELECT * FROM status_lookup where is_active='Y'";
    protected static String tmProfileQuery = "SELECT * FROM tm_profile_info where is_active='Y' and company=";
    protected static String termInfoQuery = "select  ti.term_id,ti.term_being_polled, ti.suggested_term,ti.term_pos_id," +
            LS + "ti.term_category_id,ti.suggested_term_lang_id,ti.term_status_id,ti.domain_id,ti.company_id," +
            LS + " date_format(tvm.voting_expired_date, '%m/%d/%Y') as expired_date,(select count(dd.term_id) from deprecated_term_info dd" +
            LS + " where dd.term_id=ti.term_id and dd.is_active='Y') as is_deprecated from term_information ti" +
            LS + " left join (term_vote_master tvm) on (ti.term_id = tvm.term_id) where  ti.is_active='Y' and ti.is_tm is null and ti.company_id=";

    protected static String DEPRECATED_TERMS = "select deprecated_source,deprecated_target from deprecated_term_info where term_id =";

    protected static String GLOBALSIGHT_TERM_INFO = "select  gt.globalsight_term_info_id,gt.source_segment,gt.target_segment,fi.source_lang as source_lang,"
            + LS
            + " fi.target_lang as target_lang ,gt.origin,fi.file_id,gt.transunit_id,gt.term_id,fi.job_id,fi.jobname,fi.task_id,fi.taskname "
            + LS
            + " from globalsight_term_info gt left join (file_info fi) on (fi.file_info_id= gt.file_info_id)  where gt.is_active!='N' and fi.is_active!='N' and gt.company_id =";


}

