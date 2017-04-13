<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" import="com.teaminology.hp.service.enums.TeaminologyPage,
                                      com.teaminology.hp.service.enums.SessionKey,
                                      com.teaminology.hp.bo.User,
                                      java.util.Set,
                                      com.teaminology.hp.service.enums.RoleEnum,
                                      com.teaminology.hp.service.enums.TeaminologyProperty" %>
<%@ page import="jodd.util.StringUtil" %>
<%@ page import="com.teaminology.hp.bo.lookup.Company" %>
<%


    boolean headerFlag = false;
    boolean adminUserFlag = false;
    boolean userHeaderFlag = false;
    boolean isHpCompany = false;
    boolean isSuperAdmin = false;
    String logoutStatus = "";
    String notValidLogin = "";
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
    String loginStatus = "";

   // String loginStatus = request.getParameter("msg");
    String lostSession = "";
    boolean flag = false;
    String pageUrl = application.getContextPath();
    String companyId = "";
    String statusOflogOut =(String)session.getAttribute("logOutMessage");
    String invalidLogin = (String)session.getAttribute("invalidLogin");
    String userLostSession =(String)session.getAttribute("lostSession");
    if(StringUtil.isNotEmpty(statusOflogOut)) {
    	loginStatus = statusOflogOut;
    } else if(StringUtil.isNotEmpty(invalidLogin)) {
    	loginStatus = invalidLogin;
    }else if(StringUtil.isNotEmpty(userLostSession)) {
    	loginStatus = userLostSession;
    }
    
    if (request.getRequestURI().indexOf("/hp/") > 0 || request.getRequestURI().endsWith("/hp")) {
        isHpCompany = true;
        companyId = "2";
        pageUrl += "/hp/";
        session.setAttribute(SessionKey.HP_COMPANY.getKey(), "true");
    } else
        session.setAttribute(SessionKey.HP_COMPANY.getKey(), "false");
        	
    if (StringUtil.isNotEmpty(loginStatus)) {
        if ("InvalidLogin".equalsIgnoreCase(loginStatus)) {
            loginStatus = "Enter valid username and password";
            notValidLogin = "Yes";
            session.removeAttribute("invalidLogin");
        } else if ("lostSession".equalsIgnoreCase(loginStatus)) {
            loginStatus = "";
            lostSession = "Please login";
            session.removeAttribute("lostSession");
        } else if ("logOut".equalsIgnoreCase(loginStatus)) {
            loginStatus = "";
            logoutStatus = "";
            session.removeAttribute(SessionKey.USER.getKey());
            session.removeAttribute(SessionKey.ACCESS_TOKEN.getKey());
            session.removeAttribute(SessionKey.COMPANY.getKey());
            session.setAttribute(SessionKey.COOKIE_FLAG.getKey(), "true");
            session.removeAttribute(SessionKey.SUPER_ADMIN.getKey());
            session.removeAttribute(SessionKey.SUB_MENU.getKey());
            session.removeAttribute("logOutMessage");
            lostSession = " You are signed out";
        }
    } else {
        User user = (User) session.getAttribute(SessionKey.USER.getKey());
        if (user != null) {
            headerFlag = true;
            Company company = user.getCompany();
            if (company != null && company.getContextRoot() != null) {
                pageUrl = pageUrl + "/" + company.getContextRoot();
                companyId = String.valueOf(company.getCompanyId());
            }

            if (session.getAttribute(SessionKey.SUPER_ADMIN.getKey()) != null) {
                adminUserFlag = ((Boolean) session.getAttribute(SessionKey.SUPER_ADMIN.getKey())).booleanValue();
                isSuperAdmin = adminUserFlag;
            }

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
    <input type="hidden" id="isSuperAdmin" value="<%=isSuperAdmin%>"/>
    <input type="hidden" id="companyId" value="<%=companyId%>"/>
    <input type="hidden" id="notValidLogin" value="<%=notValidLogin%>"/>

    <% if (!isHpCompany) { %>
     <div class='leftIcon'>
        <img alt="Logo" src="<%=application.getContextPath()%>/images/logo.png">
    </div>
    <div class='headerMenuAdmin'>
        <div class='menuHeadingAdmin'>
            Teaminology Dashboard
        </div>
         <!-- <div id="about" style="display: inline; color: #5a5a5a; cursor: pointer;margin-left: 60px;">ABOUT </div>  -->
<!-- <a href="http://www.hp.com" target="new window" id="about"  style="float: left; color: rgb(90, 90, 90); padding-top: 31px; cursor: pointer; margin-left: -180px; font-family: HP Simplified; font-size: 16px;">ABOUT </a> -->
        <div class="signUpButton showhide" style="color: #5A5A5A; float: none;display:inline; cursor: pointer; margin-left: 125px;color: #FFF;/* background-image: none; */"> LOG IN </div>
        <% if ((request.getRequestURI().contains("index.jsp") && request.getRequestURI().contains(".jsp")) ||  (!request.getRequestURI().contains(".jsp"))){ %>
              <a href="registration.jsp"  style="color: #d7410b; text-decoration: none; line-height: 20px;font-size: 16px; padding: 0 40px;"> SIGN UP AND VOTE</a>
  	   <% } %>         
 	  <% if (!request.getRequestURI().contains("index.jsp") && request.getRequestURI().contains(".jsp")){ %>
        <!-- <p style="margin-top:-43px;">
        	<input type="submit" id="signOut" value="LOG OUT" class="floatrightLogOut logoutStyle" name="logoutBtn">
       	</p> -->
       	<div class="signoutAdmin">
       		<input type="submit" id="signOut" value="LOG OUT" class="floatrightLogOut logoutStyle" name="logoutBtn">
       	</div>
        <% } %>
   		<!-- <div class="signUpButton showhide" style="color: #5A5A5A; float: left; margin-top: 30px; margin-left: -113px; cursor: pointer;"> LOG IN </div> -->
       <%--  <div class='signUpButton showhide'>Sign In and Vote<img id="logon" alt="Down Arrow" src="<%=application.getContextPath()%>/images/down.gif"></div> --%>
        <div class='headerMainDiv headerMenuLinks' id="header"></div>
    </div>
     
   
	<%-- <div style="padding-top: 77px;">
 		<% if ((request.getRequestURI().contains("index.jsp") && request.getRequestURI().contains(".jsp")) ||  (!request.getRequestURI().contains(".jsp"))){ %>
			<div style="padding: 5px; margin-top: -55px;" class="signup">
              <a href="registration.jsp"  style="color: #d7410b; text-decoration: none; line-height: 20px;font-size: 16px; padding: 0 10px;"> SIGN UP AND VOTE</a>
            </div>
  	   <% } %>         
 	  <% if (!request.getRequestURI().contains("index.jsp") && request.getRequestURI().contains(".jsp")){ %>
        <p style="margin-top:-43px;">
        	<input type="submit" id="signOut" value="LOG OUT" class="floatrightLogOut logoutStyle" name="logoutBtn">
       	</p>
        <% } %>          
</div> --%>
    <%-- <div class='rightIcon'>
        <img alt="[get this text from configuration file]" src="<%=application.getContextPath()%>/images/hp_logo.png">
    </div> --%>
    <div class='clear'></div>
    <% } else { %>
    <div class="leftIcon">
        <img alt="[get this text from configuration file]" src="/app/images/hp_logo.png">
    </div>
    <div class='headerMenu'>
        <div class='menuHeading'>
            HP Terminology Community
        </div>
    </div>
  <!--  <div id="about" class="aboutForDashboardMargin" style="float: left; color: #5a5a5a; padding-top: 31px; cursor: pointer;">ABOUT </div> -->
  <!-- <a href="http://www.hp.com" target="new window" id="about" class="aboutForDashboardMargin" style="float: left; color: rgb(90, 90, 90); padding-top: 31px; cursor: pointer; margin-left: -180px; font-family: HP Simplified; font-size: 16px;">ABOUT </a> -->
   <div class="signUpButton showhide" style="color: #5A5A5A; float: left; margin-top: 31px; margin-left: -113px; cursor: pointer;"> LOG IN </div>
	<div style="padding-top: 77px;">
 		<% if ((request.getRequestURI().contains("index.jsp") && request.getRequestURI().contains(".jsp")) ||  (!request.getRequestURI().contains(".jsp"))){ %>
			<div style="padding: 5px; margin-top: -55px;" class="signup">
              <a href="registration.jsp"  style="color: #d7410b; text-decoration: none; line-height: 20px;font-size: 16px; padding: 0 10px;"> SIGN UP AND VOTE</a>
            </div>
  	   <% } %>         
 	  <% if (!request.getRequestURI().contains("index.jsp") && request.getRequestURI().contains(".jsp")){ %>
        <p style="margin-top:-41px;">
        	<input type="submit" id="signOut" value="LOG OUT" class="floatrightLogOut logoutStyle" name="logoutBtn">
       	</p>
        <% } %>          
</div>
    <%-- <div class='rightIcon'>
        <img alt="[get this text from configuration file]" src="<%=application.getContextPath()%>/images/hp_logo.png">
    </div> --%>
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
<% if (!request.getRequestURI().contains("dashboard.jsp")){ %>
<div class='userInfoContainerHeader'>
</div>
  <%}%>

<% if (request.getRequestURI().contains("dashboard.jsp")){ %>
<div class='userInfoContainer'>
    <div id='userInfo'></div>
    <div class="chartsContainer floatright" style="width:700px;">
        <div class="accuracyChrt floatright">
            <div class="accuracyPieDiv" >
                <span id="accuracyRate" class="bigNumber"></span><br class="clear" />
                <span class="accuracyPercent" style="color:#5a5a5a;"> ACCURACY RATING </span>
            </div>
            <div id='accuracyPie'></div><br class='clear' />
            <div style="color:#5a5a5a;font-size:12px;padding:4px;position:relative;top:0;width:300px;">Accuracy percentage is defined as the number of your responses divided by the number of terms finalized by the administrator.For example, if you respond to 2 different polls and 1 of your responses is finalized by the administrator, your accuracy percentage is 50%,1/2.</div>
        </div>

        <div class="admnNumUsersChrt alignRight rightmargin47">
            <div class='paddingseven'>
                <span class="bigNumber" id="totalUsers"></span>

                <div class="floatleft" id='userChart'></div>
            </div>
            <br class='clear'/>
            <span class='chrtDesc' style="color:#5a5a5a;">TOTAL USERS</span>
        </div>
    </div>
</div>
     <%}%>
<div class="invalidDtls redTxt" style="text-align: right;position:relative;right:193px;bottom:70px;font-size:13px;"><%=lostSession%></div>
<div class='subMenu'>
<!-- <div style="color: rgb(227, 122, 83); padding-left: 401px; display:none" class="newRank"> Congratulations!! You got new rank </div> -->
    <div id="menuItems" class='floatleft subMenuLinks'></div>
    <% if (isHpCompany && !headerFlag && (request.getRequestURI().contains("index.jsp") || !request.getRequestURI().contains(".jsp"))) { %>
    <div style="text-align:center;">
	<h1 class="tagline">The HP Terminology Community is THE place to identify,</h1>
	<h1 class="tagLineForHome" style="">approve and standardize  HP terminology through crowdsourcing</h1>
  	<div class="taglineForSubject">As subject matter experts in your respective field, help us to reach all customers,<br>in any language, with a clear and consistent message</div>
  	<div>
  </div>
</div>
    <% } %>
    <%if (headerFlag) {%>
    <!-- <div class="floatright" id="signout"><a href="index.jsp?" style="border-right:none;" >Sign out</a></div> -->
<!--     <input type="submit" name="logoutBtn" class="floatright"  value="Sign out" id="signOut"/> -->
    <%}%>
</div>
<script type="text/javascript">
	var notValidLogin = <%="'"%><%=notValidLogin%><%="'"%>;
</script>