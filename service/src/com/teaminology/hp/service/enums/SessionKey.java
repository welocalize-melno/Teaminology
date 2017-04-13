package com.teaminology.hp.service.enums;

/**
 * Contains key names to maintain session data.
 *
 * @author sarvani
 */
public enum SessionKey
{
    COMPANY,
    HP_COMPANY,

    SUPER_ADMIN,
    SUPER_TRANSLATOR,
    COMPANY_ADMIN_USER,
    COMPANY_TEAM_MANAGER,
    COMPANY_TRANSLATOR,
    COMMUNITY_MEMBER,

    USER,
    USER_ID,
    USER_NAME,
    USER_MAIL_ID,
    USER_TYPE,
    USER_ROLE,
    IS_USER_EXISTS,

    ROLE,

    MENU,
    SUB_MENU,

    LANGUAGES,
    IS_LANGUAGE_LABEL_EXISTS,
    IS_LANGUAGE_CODE_EXISTS,

    IS_EMAIL_EXISTS,

    OVERVIEW,
    DISTRIBUTION,
    MONTH_TO_MONTH,
    TOP_LANGUAGES,
    STATUS_LOOKUP,
    DEBATED_TERMS,
    GLOSSARY_TERMS,
    DOWNLOAD_STATUS,
    UPLOAD_STATUS,
    IMAGE_NAME,

    ACCESS_TOKEN,
    JOBS,
    LOCALE_PAIR_INFORMARION,
    TM_PROFILE,
    TERMBASE,

    COOKIE_FLAG,
    REMEMBER_ME_COOKIE,
    
    SELECTED_SUGGESTED_TERM_LANG_ID;

    public String getKey() {
        return "teaminology." + name();
    }
}
