var Constants = {};
Constants.MENU_ITEMS = {
		DASHBOARD: "DASHBOARD",
		TERM_LIST: "TERM LIST",
		PROFILE: "PROFILE",
		ADMIN: "ADMIN",
		SEARCH: "SEARCH",
		GLOBALSIGH: "GLOBALSIGHT"
};
Constants.SUBMENU_ITEMS = {
		OVERVIEW: "Overview",
		MANAGE_TERMS: "Manage linguistic assets ",
		MANAGE_TEAM: "Manage team",
		MANAGE_COMPANY: "Manage company",
		REPORTS: "Reports",
		CONFIGURATIONS: "Configurations",
		IMPORT: "Import",
		EXPORT: "Export"
		
};
Constants.SUBMENU_ITEMS_GLOBALSIGHT = {
		IMPORT: "Import",
		EXPORT: "Export",
		VIEW_SEGMENTS: "View Segments",
		CONFIGURATIONS: "Configurations"
};

Constants.SUBMENU_ITEMS_SEARCH = {
		SEARCH: "Search",
		TERMS: "Terms",
		TMS:"TMs"
};

Constants.PRIVILEGES={
		0:{},
		1:{0:"action",1:"pickFinalBtn",3:"extendPoll",4:"editDetails"},
		2:{0:"accuracyChrt"}
};
Constants.SEARCH={
		1: "TM",
		2: "Termbase",
		3: "Both"
};

Constants.CASE={
		1: "Sensitive",
		2: "Insensitive"
};
Constants.TYPE={
		1: "Fuzzy",
		2: "Exact"
};

Constants.ATTRIBUTES_TM={
		1: "Part of speech",
		2: "Category",
		3: "Final",
		4: "Poll expiration",
		5: "Domain"
};

Constants.ATTRIBUTES_TERM_BASE={
		1: "Part of speech",
		2: "Category",
		3: "Final",
		4: "Poll expiration",
		5: "Domain"
		
};

Constants.ROLES={
		SUPER_ADMIN: "1",
		COMPANY_ADMIN: "2",
		COMPANY_TERM_MGR: "3",
		SUPER_TRANSLATOR: "4",
		COMPANY_TRANSLATOR: "5",
		COMMUNITY_MEMBER: "6"
};
Constants.CONFIGURATION_OPTIONS={
	1: "Domain",
	2: "Voting threshold",
	3: "Language pairs",
	4: "Category",
	5: "Create/Update email template",
	6: "Delete email template",
	7: "Manage Roles",
	8: "Manage Privileges"
};

Constants.GS_STATUS = {
		NOT_IMPORTED: "Not Imported",
		IN_PROGRESS: "In Progress",
		EXPORT_PROGRESS: "Export is in progress",
		EXPORTED:"Exported",
		IMPORTED:"Imported"
		
};
Constants.LANGUAGE_MSG = "Please select at least one language";
Constants.ROLE_MSG = "Please select role. ";
Constants.DOMAIN_MSG = "Please select your domain of expertise or business unit.";
Constants.UPDATED_TERM_MSG="Term was changed"
