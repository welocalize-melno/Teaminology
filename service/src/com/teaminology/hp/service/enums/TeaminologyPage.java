package com.teaminology.hp.service.enums;

/**
 * Contains key values to maintain page urls.
 *
 * @author Sarvani
 */
public enum TeaminologyPage
{

    ADMIN("/admin_overview.jsp"),
    USER("/user_termlist.jsp"),
    INVALIDLOGIN("/index.jsp"),
    INDEX("/index.jsp"),
    DASHBOARD("/dashboard.jsp"),
    ACCESS_DENIED("/error/403.jsp"),
    LOST_SESSION("/index.jsp"),
    ADMIN_MANAGE_TERMS_SUCCESS("/admin_manage_terms.jsp?status=success"),
    ADMIN_MANAGE_TERMS_FAILED("/admin_manage_terms.jsp?status=failed"),
    ADMIN_MANAGE_TEAM_SUCCESS("/admin_manage_team.jsp?status=success"),
    ADMIN_MANAGE_TEAM_FAILED("/admin_manage_team.jsp?status=failed"),
    REGISTRATION("/registration.jsp"),
    FORGOT_PASSWORD_DENIED("/forgot_password.jsp"),
    ADMIN_CONFIGURATION_SUCCESS("/admin_configuration.js");

    private final String pageUrl;

    private TeaminologyPage(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }
}
