package com.teaminology.hp.service.enums;

/**
 * Contains values for Roles
 *
 * @author Sayeed
 */
public enum RoleEnum
{
    //Super user
    SUPER_ADMIN("Super Admin"),
    SUPER_TRANSLATOR("Super Translator"),

    //Company user
    COMPANY_ADMIN("Company Admin"),
    COMPANY_TERM_MANAGER("Company Term Manager"),
    COMPANY_TRANSLATOR("Company Translator"),
    COMMUNITY_MEMBER("Community Member"),;

    private final String value;

    private RoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
