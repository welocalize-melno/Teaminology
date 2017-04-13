<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import= "com.teaminology.hp.service.enums.TeaminologyPage,
                                  com.teaminology.hp.service.enums.SessionKey,
                                  com.teaminology.hp.service.enums.MenuEnum,
                                  com.teaminology.hp.bo.lookup.Company,
                                  com.teaminology.hp.bo.Menu,
                                  java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Welocalize Terminology Portal</title>
<link href="<%=application.getContextPath()%>/css/demo_table.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/global.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/language_m_select.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">

 
<script type="text/javascript">
function validate(){
    var userName = $.trim($("#username").val());
    var password = $("#password").val();
    var flag=true;
    if(userName == "" || userName == null){
   	 $("#userError").html("Username is required");
		 $("#userError").show();
	   	$("#username").addClass("error");
   		document.loginFrm.username.focus();
   		flag= false;
	}
    if(password == "" ||password == null){
		 $("#pwdError").html("Password is required");
		    $("#pwdError").show();
	   		$("#password").addClass("error");
	   document.loginFrm.password.focus();
	   flag=false;
	}
	return flag;
}
</script>
</head>

<body>
	<div class='siteContainer'>
	<%@include file="./common/header.jsp"%>
<%
if (session.getAttribute(SessionKey.USER.getKey()) != null) {
	 response.sendRedirect("./dashboard.jsp");
}
%>
	<% if (!isHpCompany) { %>
		<div class='bannerContainer'><img src="<%=application.getContextPath()%>/images/home_banner.png" alt="Home Banner" /></div> 
		<div class='signUpContainer' style="position:relative;">
			<h1 class='signUpText'>Join our community of translators and vote today</h1> 
			<%-- <div class='signUpButton showhide'>Sign In and Vote<img id="logon" alt="Down Arrow" src="<%=application.getContextPath()%>/images/down.gif"></div> --%>
			<!-- <div class='floatright nodisplay' id="signOutLink" style="margin-top: 16px;"><a href="<%=pageUrl%>/index.jsp?msg=logOut" style="border-right:none;color:#eeeeee;">Sign Out</a></div> -->
		</div>
		<% } %>
		<%-- <div class='contentContainer'>
		<div class='clear'></div>
		<div class="invalidDtls redTxt bottommargin10"><%=lostSession%>
      </div> --%>
      <div class='contentContainer'>
       <div class="invalidDtls redTxt" style="text-align: center;margin-top: 10px;"><%=lostSession%></div>
		<div class='clear'></div>
        <div style="text-align:center;">
			<img style="size: 73px;" src="/app/images/submit.jpg" alt="[get this text from configuration file]">
  			<img style="size: 73px; padding: 0px 60px;" src="/app/images/vote.jpg" alt="[get this text from configuration file]">
  			<img style="size: 73px;" src="/app/images/search.jpg" alt="[get this text from configuration file]">
		</div>
      <div>
      <div class='tagCloudContainer'>
            <span class='tagCloudHead' style="font-size:16px; color:#5a5a5a; font-weight: normal;">Words being voted on:</span>
            <div id='tagCloud'>
                <div id="noTerm" class="nodisplay" align="center">No active polls</div>
                <div id="tags">
                    <div id="listTrms" style="word-wrap: break-word;text-align:center; color: #5a5a5a;"></div>
                </div>
            </div>
        </div>
        <div class='rightContainer' style='padding-top: 30px; float:right;margin-right: 50px;'>
            <div class="newTrmRqst bottommargin15 nodisplay">
                <input type="submit" value="Request new term" class="termRqstBtn RequestnewtermNew"/>
            </div>
            <!-- leader board -->
            <div class='topmargin12' id='leaderBoard'>
                <h3 style="color: rgb(43, 166, 203); float: left; font-family: Hp Simplified;">Leader Board</h3>
                  <img src="/app/images/globe.png" style="margin-left: 18px; height: 14px;position: relative;top:3px;">
				<select class="langSelect sortByLanHide" size="5" name="example-basic" multiple="multiple"  class="text220 " title=" select language"
                    id="termlanguageSlctForRank"></select> </div>
                <div id='leadersList' style="position: relative;"></div>
                <div class='leaderBrdFoot' style="padding-top: 12px;">
                    <span class='smallFont seeAll' style="float: right; padding-right: 12px;">See All</span>
                </div>
           <div class="topmargin10 langSlctDropdwn bottommargin10 termLangSlctDropdwn sortByLanHide">
            <h3 style="padding-bottom: 7px; padding-top: 35px; padding-left: 1px; font-weight: bold;">Sort by language </h3>
            <select size="5" name="example-basic" multiple="multiple" title=" select language"
                    id="termlanguageSlctForRank"></select> </div>
            </div>
            <div class='clear'></div>
		</div>
		<div class="" style="font-size: 26px; color:#D7410B; padding-top: 20px; text-align: center;width:1100px; margin: 10px auto 0;font-family: HP Simplified;">Help shape the future of technology terminology.<a style="color: rgb(215, 65, 11); font-size: 28px; font-weight: bold; text-decoration: none; cursor: pointer;" href="registration.jsp"> Join now</a><span style="margin-left: 2px;">!</span></div>
		<div style="display: none;" id="allUserList" title="Leader board">
			<table cellpadding="0" cellspacing="0" border="0" class="display" id="leaderboard_seeAll" width="100%">
				<thead>
					<th width="15%">&nbsp;</th>
					<th width="25%">Username</th>
					<th width="20%">Total votes</th>
					<th width="20%">Badging</th>
					<th width="20%">Accuracy</th>
				</thead>
				<tbody>
					
				</tbody>
			</table>
		</div>
		<div style="display: none;" id="allUserListByLang" title="Leader board">
        <table cellpadding="0" cellspacing="0" border="0" class="display" id="leaderboardByLanguage" width="100%">
            <thead>
            <th width="15%">&nbsp;</th>
            <th width="25%">Username</th>
            <th width="20%">Total votes</th>
            <th width="20%">Badging</th>
            <th width="20%">Accuracy</th>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
	<div class="PopupDiv" style="display:none;">
	<div style="float:right;"><a href="javascript:;" class="showhide">Close <img src="<%=application.getContextPath()%>/images/close-btn.gif" alt="close button" border="0" class="leftmargin5" height="10px" width="10px;" /></a></div>
    <div style="font-size: 18px;font-weight:bold;color:#596269;margin-bottom:20px;">Member Sign In</div>
    <form id="loginForm" action="<%=application.getContextPath()%>/login" method="post"  name="loginFrm" onsubmit="return validate()">
    
    
     <label for="username" class="error floatright nodisplay" style="width:200px;margin-right:-3px;margin-top:-16px;" generated="true" id="userError"></label><div class="clear"></div> <div style="padding-bottom: 40px;"> <div class="label floatleft">Username<span class="redTxt">*</span>:</div><div class="floatleft"><input type="text" id="username"  name="username" class="text190" maxlength="30" /></div></div>
     <label for="password" class="error floatright nodisplay" style="width:200px;margin-right:-3px;margin-top:-16px;" generated="true" id="pwdError"></label><div class="clear"></div> <div style="padding-bottom: 20px;"><div class="label">Password<span class="redTxt">*</span>:</div><div class="floatleft"><input id="password" type="password" name="password" class="text190"  maxlength="100"></div></div><div class="clear"></div>
      
      <p class="smallFont floatright" style="margin-top:5px;"><a href="forgot_password.jsp">Forgot password</a></p>
      <div class="topmargin5" style="margin-top: 32px;margin-left: 76px;" >
      	  <div class="invalidDtls redTxt bottommargin10"><%=loginStatus%></div>
	      <div><input type="checkbox" name="rememberMe" style="border:none;font-size: 11px;" /> Remember me</div>
		  <div class="clear"></div>
		  <div><input type="submit" value="Log On" class="loginBtn topmargin10" name="loginBtn" /></div>
      </div>
      </form>
      <div style="float:left;" class="registerLnk">First time users, <a href="registration.jsp">Register now</a></div>

  </div>
    <!-- request new term part -->
    <div id="newTermDiv" class="nodisplay" title="Request new term">
    </div>

    <div id="loading"></div>

    <div id="mail_success" title="Success" class="nodisplay">
        <p class="topmargin15"><span class="ui-icon ui-icon-circle-check"
                                     style="float:left; margin:0 7px 50px 0;"></span>
            <span id="logmsg">Request successfully submitted for review. Mail will be sent upon validation.</span></p>
    </div>
		<div class='footerTopLine'></div>
	 <div style="width:1100px;margin:0 auto;">
    	<%@include file="../common/footer.jsp" %>
    </div>

    <script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/prototype.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.dataTables.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.qtip.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-ui-1.8.18.custom.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
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
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/index.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tagsphere.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>

    <script type="text/javascript">
    
    </script>
</body>
</html>