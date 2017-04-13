<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" import="com.teaminology.hp.service.enums.TeaminologyPage,
                                      com.teaminology.hp.service.enums.SessionKey,
                                      com.teaminology.hp.bo.UserRole,
                                      java.util.Set,
                                      com.teaminology.hp.service.enums.RoleEnum,
                                      com.teaminology.hp.service.enums.TeaminologyProperty,
                                      com.teaminology.hp.bo.Role" %>
<%@ page import="jodd.util.StringUtil" %>
<%@ page import="com.teaminology.hp.bo.User" %>
<%@ page import="com.teaminology.hp.bo.lookup.Company" %>
<%
    boolean headerFlag = false;
    boolean adminUserFlag = false;
    boolean userHeaderFlag = false;
    boolean isHpCompany = false;

    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

    String loginStatus = request.getParameter("msg");
    String lostSession = "";
    boolean flag = false;
    String pageUrl = application.getContextPath();

    if (StringUtil.isNotEmpty(loginStatus)) {
        if ("InvalidLogin".equalsIgnoreCase(loginStatus)) {
            loginStatus = "Enter valid username and password";
        } else if ("lostSession".equalsIgnoreCase(loginStatus)) {
            loginStatus = "";
            lostSession = "Please login";
        } else if ("logOut".equalsIgnoreCase(loginStatus)) {
            loginStatus = "";

            session.removeAttribute(SessionKey.USER.getKey());
            session.removeAttribute(SessionKey.ACCESS_TOKEN.getKey());
            session.removeAttribute(SessionKey.COMPANY.getKey());
            session.setAttribute(SessionKey.COOKIE_FLAG.getKey(), "true");
            session.removeAttribute(SessionKey.SUPER_ADMIN.getKey());

            lostSession = " You are signed out";
        }
    } else {
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        if (user != null) {
            headerFlag = true;
            Company company = (Company) session.getAttribute(SessionKey.COMPANY.getKey());
            if (company != null && company.getContextRoot() != null) {
                pageUrl = pageUrl + "/" + company.getContextRoot();
            }
            if (session.getAttribute(SessionKey.HP_COMPANY.getKey()) != null || request.getRequestURI().indexOf("hpIndex.jsp") > -1)
                isHpCompany = ((Boolean) session.getAttribute(SessionKey.HP_COMPANY.getKey())).booleanValue();

            if (session.getAttribute(SessionKey.SUPER_ADMIN.getKey()) != null)
                adminUserFlag = ((Boolean) session.getAttribute(SessionKey.SUPER_ADMIN.getKey())).booleanValue();

            if (!adminUserFlag && session.getAttribute(SessionKey.COMPANY_ADMIN_USER.getKey()) != null)
                adminUserFlag = ((Boolean) session.getAttribute(SessionKey.COMPANY_ADMIN_USER.getKey())).booleanValue();

            userHeaderFlag = !adminUserFlag;
        }
    }
%>
<div class='header'>
    <input type="hidden" id="headerFlag" value="<%=headerFlag %>"/>
    <input type="hidden" id="adminHeaderFlag" value="<%=adminUserFlag %>"/>
    <input type="hidden" id="userHeaderFlag" value="<%=userHeaderFlag %>"/>
    <input type="hidden" id="contextRootPath" value="<%=application.getContextPath()%>"/>
    <input type="hidden" id="currentMenuPage" value=""/>
    <input type="hidden" id="currentSuperMenuPage" value=""/>
    <input type="hidden" id="inputFileMaxSize" value="<%=TeaminologyProperty.IMPORT_MAXFILESIZE_IN_MB.getValue()%>"/>
    <input type="hidden" id="hpsite" value="<%=isHpCompany%>"/>

    <% if (!isHpCompany) { %>
		<div class='leftIcon'>
			<img alt="Logo" src="<%=application.getContextPath()%>/images/logo.png">
		</div>
		<div class='headerMenu'>
			<div class='menuHeading'>
				Teaminology Dashboard
			</div>
			<div class='headerMainDiv headerMenuLinks' id="header"></div>
		</div>
    <% } else { %>
		<div class='headerMenu' style="text-align:center;">
			<div class='menuHeading'>
				HP Terminology Community
			</div>
		</div>
		<div class='rightIcon'>
			<img alt="[get this text from configuration file]" src="<%=application.getContextPath()%>/images/logo_hpe.png">
		</div>
		<div class='clear'></div>

		<div class='headerMainDiv headerMenuLinks' id="header"></div>
    <% } %>

    <div id="helpDiv" class="nodisplay" title="Help">
        <form id="manual">
            <div class="topmargin15"><a class=" topmargin15 " id="adminDoc"
                                        style="color: #00A4D6;margin-top: 5px;text-align: center;"
                                        href="#">Admin_manual</a></div>
            <div class="topmargin15"><a class=" topmargin15 " id="userDoc"
                                        style="color: #00A4D6;margin-top: 5px;text-align: center;"
                                        href="#">User_manual</a></div>
        </form>
    </div>
</div>
<div class='userInfoContainer'>
    <div id='userInfo'></div>
    <div class="chartsContainer floatright" style="width:700px;">
        <div class="accuracyChrt floatright">
            <div class="accuracyPieDiv" >
                <span id="accuracyRate" class="bigNumber"></span><br class="clear" />
                <span class="accuracyPercent">Accuracy percent </span>
            </div>
            <div id='accuracyPie'></div><br class='clear' />
        </div>

        <div class="admnNumUsersChrt alignRight rightmargin15">
            <div class='paddingseven'>
                <span class="bigNumber" id="totalUsers"></span>
                <div class="floatleft" id='userChart'></div>
            </div>
            <br class='clear'/>
            <span class='' style='position:absolute;top:60px;left:10px;'>Total users</span>
        </div>
    </div>
</div>
<div class='subMenu'>
    <div id="menuItems" class='floatleft subMenuLinks'></div>
    <% if (request.getRequestURI().contains("index.jsp") || !request.getRequestURI().contains(".jsp")) { %>
        <h1 class='tagline floatleft'>Join our community and vote today on terminology that matters for you.</h1>
     <% } %>
    <%if (headerFlag) {%>
    <!-- <div class="floatright"><a href="index.jsp?msg=logOut" style="border-right:none;">Sign out</a></div> -->
    <input type="submit" name="logoutBtn" class="floatright"  value="Sign out" id="signOut"/>
    <%}%>
</div>

