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
    
   // if (request.getRequestURI().indexOf("/hp/") > 0 || request.getRequestURI().endsWith("/hp")) {
        isHpCompany = true;
        String homeUrl  = request.getRequestURI();
    	int appPosition = homeUrl.indexOf("/app") ;
    	pageUrl += homeUrl.substring(appPosition+4,homeUrl.length()-1);
        companyId = "2";
        session.setAttribute(SessionKey.HP_COMPANY.getKey(), "true");
  /*   } else */
       // session.setAttribute(SessionKey.HP_COMPANY.getKey(), "false");
        	
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
    <div class='headerMenu'>
        <div class='menuHeading'>
            Teaminology Dashboard
        </div>
        <div class='headerMainDiv headerMenuLinks' id="header"></div>
    </div>
    <% } else { %>
    <div class="leftIcon">
        <img alt="[get this text from configuration file]" src="/app/images/apple_logo.jpg">
    </div>
    <div class='headerMenu'>
        <div class='menuHeading'>
            Terminology Community
        </div>
        <p style="margin-top: -53px;">
			<input type="submit" id="signOut" value="Log Out" class="floatright" name="logoutBtn">
		</p>
    </div>
<!-- <div style="color: rgb(0, 0, 0); font-size: 15px; float: left; padding: 32px 0px 15px;"> ABOUT </div>
<div style="color: rgb(0, 0, 0); font-size: 15px; float: left; padding: 32px 50px    15px;"> LOG IN </div> -->
<div style="padding-top: 60px;">
<div style="padding: 1px; margin-top: -36px;" class="signUpButton showhide">
              SIGN UP AND VOTE
            </div>
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

<div class='userInfoContainerForDashboard'>
    <div id='userInfo'></div>
    <div class="chartsContainer floatright" style="width:700px;">
        <div class="accuracyChrt floatright">
            <div class="accuracyPieDiv" >
                <span id="accuracyRate" class="bigNumber"></span><br class="clear" />
                <span class="accuracyPercent"> ACCURACY RATING </span>
            </div>
            <div id='accuracyPie'></div><br class='clear' />
        </div>

        <div class="admnNumUsersChrt alignRight rightmargin15">
            <div class='paddingseven'>
                <span class="bigNumber" id="totalUsers"></span>

                <div class="floatleft" id='userChart'></div>
            </div>
            <br class='clear'/>
            <span class='chrtDesc'>Total users</span>
        </div>
    </div>
</div>

<div class='subMenu'>
    <div id="menuItems" class='floatleft subMenuLinks'></div>
    <% if (isHpCompany && !headerFlag && (request.getRequestURI().contains("index.jsp") || !request.getRequestURI().contains(".jsp"))) { %>
    <div style="padding-left: 200px;">
	<h1 class="tagline floatleft">The Terminology Community is THE place to identify, approve and </h1>
	<h1 class="tagline floatleft" style="padding-left: 75px;">standardize  HP terminology Through crowdsourcing</h1>
  	<div style="font-size: 15px; padding-left: 101px;" class="taglineForSubject floatleft">As subject matter experts in your respective field, help us to reach all<div>
  <div style="font-size: 15px; padding-left: 12px;" class="taglineForSubject floatleft">customers, in any language, with a clear and consistent message.<div>
  </div></div></div></div>
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