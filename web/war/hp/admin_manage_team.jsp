<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import= "com.teaminology.hp.service.enums.TeaminologyPage,
                                  com.teaminology.hp.service.enums.SessionKey,
                                  com.teaminology.hp.service.enums.MenuEnum,
                                  com.teaminology.hp.bo.lookup.Company,
                                  com.teaminology.hp.bo.Menu,
                                  java.util.List"%>
<%@ page import="com.teaminology.hp.bo.SubMenu" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>HP Terminology Community</title>
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/global_hp_new.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
</head>

<body class="adminOvr">
<div class='siteContainer'>
    <%@include file="../common/header.jsp" %>
    <%
        List<SubMenu> subMenus = (List<SubMenu>) session.getAttribute(SessionKey.SUB_MENU.getKey());
        boolean pageFlag = false;
        if (subMenus != null) {
            for (SubMenu subMenu : subMenus) {
                if ("admin_manage_team.jsp".equalsIgnoreCase(subMenu.getPageUrl())) {
                    pageFlag = true;
                    break;
                }
            }
        }
        if (!pageFlag)
            response.sendRedirect("./index.jsp");
    %>
    <div class='contentContainer'>
        <h1 class='bottommargin10 floatleft'>Manage team</h1>

        <div class='rightContainer floatright'>
            <div class='invitePpl bottommargin10'>
                <div class='bottommargin10'>Invite people to the community</div>
                <div>
                    <div class="floatleft"><input type='text' class="width170"  value='Enter email address...' id='emailIds'/></div>
                    <div class="floatright"><input type='button' id='sendMail' value='Invite'/></div>
                </div>
                <div class="floatright topmargin10 fullWidth alignCenter"><span
                        class="importUsers hand link">Import users</span></div>
            </div>
        </div>
        <div class='leftContainer topmargin10'>
            <div>
                <!--	<div class="floatleft rightmargin10" style="font-size: 12px"><span class="bold">Action:</span>
                        <select id="Action" name="Action" class="invite_vote">
                            <option value="0">Select action</option>
                            <option value="1">Delete</option>
                            <option value="2">Modify</option>
                            <option value="3">Add</option>
                        </select>
                    </div>  -->
                <div class='langMenu' id="langs"></div>
            </div>

            <div class="topmargin20 langSlctDropdwn">
                <select size="5" name="example-basic" multiple="multiple" title="select language"
                        id="mainLanguageSlct"></select>
            </div>
            <div class="topmargin20 companySlctDropdwn  companyFilter nodisplay">
                <select size="5" name="example-basic" multiple="multiple" title="select company"
                        id=mutliCompanySlct></select>
            </div>
        </div>

        <div class='clear'></div>
        <br class='clear'/>

        <div id="user" class="greenTxt font-12"></div>

        <div class="alignRight bottommargin10">
            <input type="button" value="Add" class="commonBtn addallUser addCUser nodisplay" name="addUsers" id="addUsers" />&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="Delete" class="commonBtn delAllUser delCUser nodisplay" name="deleteMultipleUsers" id="deleteMultipleUsers" />&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="Export users" class="commonBtn" name="exprtUsers" id="exprtUsers" />
        </div>

        <div class="topmargin15" id="teamListModule">
            <div id="manageTeamTbl" class='bottommargin10 topmargin15'>
                <div id='teamListSectionHead'>
                    <div id='column0' class='width20'>
                        <input type="checkbox" id="selectAll" title="Select all" class="topmargin1 leftmargin1"/></div>
                    <div id="userName" class="width140" sortOrder="ASC"><span class="headerMenuLink">Name</span></div>
                    <div id="emailId" class="width130" sortOrder="ASC"><span class="headerMenuLink">Email id</span></div>
                    <div id="dateOfCreation" class="width80" sortOrder="ASC"><span
                            class="headerMenuLink">Member since</span></div>
                    <div id="accuracy" class="width50" sortOrder="ASC"><span class="headerMenuLink">Accuracy</span></div>
                    <div id="lastMonth" class="width70" sortOrder="ASC"><span class="headerMenuLink">Last month</span></div>
                    <div id="totalVotes" class="width70" sortOrder="ASC"><span class="headerMenuLink">Total votes</span></div>
                    <div id="usrCompany" class="width90" sortOrder="ASC"><span class="headerMenuLink">Company</span></div>
                    <div id="usrRole" class="width100" sortOrder="ASC"><span class="headerMenuLink">Role</span></div>
                    <div id="badging" class="width90" sortOrder="ASC">Badging</div>
                    <div id="modify" class="width40 nodisplay modifyAllUser modifyCUser" sortOrder="ASC">Edit</div>
                    <div id="delete" class="width40 nodisplay delAllUser  delCUser" sortOrder="ASC">Delete</div>
                </div>

                <div id='teamRowsList'>
                    <div class="rowBg">
                        <div class="chkBx"><input type="checkbox" value="1" class="floatleft"></div>
                    </div>
                </div>

                <div id="pagination" class="pageination">
                    <div class="floatleft"><span>Viewing <span id="rangeOfList"> 0</span> of <span
                            id="totalMembers">0</span></span></div>
                    <div class="floatright"><span class="rightmargin5 previous">Previous</span> | <span
                            class="next leftmargin5">Next</span></div>
                </div>
            </div>
        </div>
    </div>

    <div id="addUser" class="nodisplay" title="Add new user">
        <form id="registerForm">
            <div class="regFrm topmargin30">
                <div class='boxWidth320'>
                    <div class="bottompadding20">
                        <div class="label width110 floatleft">First name:</div>
                        <div class="floatleft width200">
                            <input type="text" id="fName" size="25" value="" class="text190" name="fName" tabindex="1" style="position: absolute;"/></div>
                    </div>
                    <br class="clear"/>

                    <label for="username" class="error floatright nodisplay teamLabel" generated="true"></label>
                    <div class="clear"></div>
                    <div class="error floatright nodisplay userExists teamError"></div>
                    <div class="clear"></div>
                    <div class="bottompadding20">
                        <div class="label width110 floatleft">User name<span class="redTxt">*</span>:</div>
                        <div class="floatleft width200">
                            <input type="text" id="username" size="25" value="" class="text190" name="auserName" tabindex="3" style="position: absolute;"/>
                        </div>
                    </div>
                    <br class="clear"/>

                    <label for="email" class="error nodisplay teamLabel" style="float:right;" generated="true"></label>
                    <div class="clear"></div>
                    <div class="error floatright nodisplay emailExists teamError"></div>
                    <div class="clear"></div>
                    <div class="bottompadding20">
                        <div class="label width110 floatleft">Email<span class="redTxt">*</span>:</div>
                        <div class="floatleft width200">
                            <input type="text" id="email" size="25" value="" class="text190" name="aemail" tabindex="5" style="position: absolute;"/>
                        </div>
                    </div>
                    <br class="clear"/>

                    <label for="password" class="error floatright nodisplay teamLabel" generated="true"></label>
                    <div class="clear"></div>
                    <div class="bottompadding20">
                        <div class="label width110 floatleft">Password<span class="redTxt">*</span>:</div>
                        <div class="floatleft width200">
                            <input type="password" id="password" size="25" value="" class="text190" name="apassword" tabindex="7" style="position: absolute;"/>
                        </div>
                    </div>
                    <br class="clear"/>

                    <label for="cnfmPassword" class="error floatright nodisplay teamLabel" generated="true"></label>
                    <div class="clear"></div>
                    <div class="bottompadding20">
                        <div class="label width110 floatleft">Confirm password<span class="redTxt">*</span>:</div>
                        <div class="floatleft width200">
                            <input type="password" id="cnfmPassword" size="25" value="" class="text190" name="acnfmPassword" tabindex="9" style="position: absolute;"/>
                        </div>
                    </div>
                    <br class="clear"/>

                </div>

                <div class='boxWidth320'>
                    <div class="bottompadding20">
                        <div class="label width90 floatleft">Last name:</div>
                        <div class="floatleft">
                            <input type="text" id="lName" size="25" value="" class="text220" name="lName" tabindex="2"/>
                        </div>
                    </div>
                    <br class="clear"/>

                    <div class="bottommargin15 hideLanguageSlct bottompadding20">
                        <div class="label width90 floatleft">Language<span class="redTxt">*</span>:</div>
                        <div class="floatleft nodisplay teamselecterror">
                            <span class="error languageSlctErr floatleft">Select at least one language</span>
                        </div>
                        <div id="border-rad" class="floatleft">
                            <select id="languageSlct" name="languageSlct" multiple="multiple" tabindex="6" style="width:220px;border-radius:0px;"></select>
                        </div>
                    </div>
					
					<div class="bottompadding20">
                        <div class="label width90 floatleft">Role:</div>
                        <div class="floatleft bottompadding20">
                            <select id="role" name="role" tabindex="4" style="width:225px">
                                <option value="">---Select role---</option>
                            </select>
                        </div>
                    </div>
					
					 <div class="floatright nodisplay teamSelectError">
                        <span class="error companySlctErr">Select at least one company</span>
                    </div>
                    <div class="bottommargin15 addCompanyUser addallUser  nodisplay" id="hideCompanySlct">
                        <div class="label width90 floatleft">Company<span class="redTxt">*</span>:</div>
                        <select disabled id="company" name="company" tabindex="8" style="width:225px">
                            <option value="0">---Select company---</option>
                        </select>
                    </div>

                    <div class="bottommargin20 ">
                        <div class="label width90">Domain:</div>
                        <select id="domain" tabindex="10" style="width:225px">
                            <option value="">---Select domain---</option>
                        </select>
                    </div>

                    <div class="bottommargin15 nodisplay voteForMultipleCompanies companySlct">
                        <div class="label width90 floatleft">Companies to be voted:</div>
                         <select id="companySlct" name="companySlct" multiple="multiple" class="text220 " tabindex="11"></select>
                    </div>
                </div>

                <div class="clear"></div>

                <div id="editUserBtns" class="teamButton">
                    <input type="button" value="Submit" tabindex="12" class="loginBtn addUser" name="submitBtn"/>
                    <input id="cancelAddUser" type="button" name="resetBtn" tabindex="13" value="Cancel" class="leftmargin25"/>
                </div>
            </div>
        </form>
    </div>

    <!-- Modify user form -->
    <div class='clear'></div>
    <div id="ModifyUser" class="nodisplay" title="Modify user">
        <form id="ModifyForm">
            <div class="regFrm topmargin30">
                <div class="editDetails"></div>
                <div class="clear"></div>
                <div id="editUserBtns" class="teamButton">
                    <input type="button" id="submitModUser" value="Submit" class="loginBtn" name="submitBtn"/>
                    <input id="cancelModUser" type="button" name="resetBtn" value="Cancel" class="loginBtn leftmargin25"/>
                </div>
            </div>

            <div class='clear'></div>
        </form>
    </div>

    <!-- Import user form -->
    <div id="importUserBrwse" title="Import users" class="nodisplay">
        <iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
        <form id="ImportUserForm" name="ImportUserForm" method="post" target="uploadFrame" action="<%=application.getContextPath()%>/impExp_serv" ENCTYPE="multipart/form-data">
            <input type="hidden" name="c" value="importCSV"/>
            <input type="hidden" name="type" value="user"/>

            <p class="bold topmargin15"> Browse CSV file format to batch import the users:</p>
            <p class="topmargin15"><input type="file" size="40" name="uploadUser" id="userImport"/></p>
            <p class="topmargin15"><input type="button" value="Import users" id="importUserFile" class="commonBtn"/></p>
        </form>

        <div id="userImprtInfo" class="nodisplay">
            <p class="topmargin15 greenTxt"><span id="successAddedUsers"></span></p>
            <p class="topmargin15 redTxt failedInfo">Failed to import users: <span id="failedAddedUsers"></span></p>
            <p class="topmargin15 redTxt failedInvalidInfo">Invalid File</p>
            <div class="failedInfo">
                <div class="userImportTblHead topmargin15 boxWidth350">
                    <div class="floatleft width220">Username/email-id</div>
                    <div class="floatleft width130">Line # in import file</div>
                </div>
                <div class="clear"></div>
                <div class="userImportTblBody width365">
                </div>
            </div>
        </div>
    </div>

    <div id="exportUserFilter" title="Export users" class="nodisplay">
        <div class="topmargin15 floatleft">
            <p class="bold bottommargin10">Select role<span class="redTxt">*</span>:</p>
            <select id="roleSlct" name="roleSlct" multiple="multiple" class="text220" size="4"></select>
        </div>
		<div class="topmargin40 floatleft leftmargin15">
	    	<div><select id="exprtlangSlct" name="languageSlct" multiple="multiple" class="text220"></select></div>
	    	<% if (isSuperAdmin) { %>
	    		<div>&nbsp;</div>
	    		<div><select id="exprtcompanySlct" name="companySlct" multiple="multiple" class="text220"></select></div>
	    	<% } %>
	    </div>
        <div class="clear"></div>
        <div class="topmargin20">
            <input type="button" value="Export" id="getUsers" class="commonBtn" style="padding: 3px 5px;"/>
        </div>
    </div>

    <div id="loading"></div>

    <div id="mail_success" title="Success" class="nodisplay">
        <p class="topmargin15" style="margin-top:55px; margin-left:55px"><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
            Invitation will be sent to <span id="newUserEmail"></span>.</p>
    </div>

    <div id="mail_failed" title="Failure" class="nodisplay">
        <p class="topmargin15"><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
            Failed to send invitation to <span id="failedUserEmail"></span>.</p>
    </div>

    <div id="delete_cnfm" title="Delete" class="nodisplay">
        <p class="topmargin25"><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you
            sure you want to delete? Deletion will erase all records associated with this users from the database.</p>
    </div>

    <div id="mailMessage" title="Alert" class="nodisplay">
        <p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
            Enter a valid email address.</p>
    </div>

    <div id="importMessage" title="Alert" class="nodisplay">
        <p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
            Please select the file to import.</p>
    </div>

    <div id="userSlctMessage" title="Alert" class="nodisplay">
        <p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
            Select at least one user to perform Action.</p>
    </div>

    <div id="roleSlctMessage" title="Alert" class="nodisplay">
        <p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
            Please select at least one role.</p>
    </div>

    <div id="userMessage" title="Success" class="nodisplay">
        <p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
            User successfully added.</p>
    </div>

    <div id="invitation" title="Invite people to the community" class="nodisplay" style="padding-left:15px;">
        <div class="nodisplay redTxt leftmargin100" id="templateReq" style="padding-left:87px;margin-bottom: -15px;">Select
            at least one template
        </div>
        <div class="topmargin15">
            <p class="bold">Email template<span class="redTxt">*</span>
                <select class="text190 emailTemplate">
                    <option value="">--Select template--</option>
                </select>
            </p>
        </div>

        <div class="clear"></div>
        <div class="topmargin15 emailPrvw" style="height:180px;width:500px; border:1px solid #888888;overflow:auto;"></div>

        <div class="clear"></div>
        <div class="sendMail topmargin15" style="text-align: center;">
            <input type="button" value="Send mail" id="inviteMail" class="rightmargin15 commonBtn padding5"/>
            <input type="button" value="Cancel" id="cancelInvitMail" class="commonBtn padding5"/>
        </div>

        <div id="deleteUsers" title="Success" class="nodisplay">
            <p class="topmargin20 alignCenter">
                <span class="ui-icon ui-icon-circle-check" style="float:left;margin:0 7px 25px 0;"></span>
                Deleted successfully.
            </p>
        </div>

    </div>
    <div class='footerTopLine'></div>
    <%@include file="../common/footer.jsp" %>
</div>

	<script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tagsphere.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.qtip.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-ui-1.8.18.custom.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/prototype.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.multiselect.filter.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.multiselect.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.validate.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/json2.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/common.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Constants.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/charts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/qtip.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/leaderBoard.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
<script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
<script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/admin_manage_team.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>

   <script type="text/javascript">
	</script>

</body>
</html>