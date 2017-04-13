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
<title>Terminology Community</title>
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/global_hp.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<%
//String status = request.getParameter("msg");
String status  = (String)session.getAttribute("invalidUserNameForForgot");
String userExistsErr = "";
if(status != null && status.equalsIgnoreCase("invalidUserName")){
	userExistsErr = "User does not exist, please provide valid username ";
	session.removeAttribute("invalidUserNameForForgot");
}
%>
</head>
<body>
	<div class='siteContainer'>
	<%@include file="../common/header.jsp"%>
		<div class='contentContainer'>
            <h1 class='tagline' style="margin-bottom: 30px;margin-left: 52px;margin-top: 30px;">Forgot your password?</h1>
			<form id=forgotPwdForm action="" method="get">
				<div class="regFrm">
					
					<div class='boxWidth330'>
						<label for="username" class="error floatright nodisplay" style="width:200px;margin-right:10px;margin-top:-30px;" generated="true"></label><div class="clear"></div>
						<label class="error floatright" style="width:212px;" ><%=userExistsErr %></label><div class="clear"></div>
						<div style="padding-bottom: 35px;"><div class="label width110 floatleft">User name<span class="redTxt">*</span></div> <div class="floatleft" style="width:200px;"><input type="text" size="25" value="" class="text190" id="username" name="username" maxlength="30"  style="<%=userExistsErr==""?"":"border:1px solid red;" %>"/> </div></div><div class="clear"></div>
						<label for="email" class="error floatright nodisplay" style="width:200px;margin-right:12px;margin-top:-28px;" generated="true"></label><div class="clear"></div><div style="padding-bottom: 20px;"><div class="label width110 floatleft">Email<span class="redTxt">*</span></div> <div class="floatleft" style="width:200px;"><input type="text" size="25" value="" class="text190" name="email" id="email" maxlength="50"/> </div></div> <br class="clear" />
					</div>
					<div class="clear"></div>
					<div style="margin-left: 115px;" class="topmargin15"><input id="submitBtn" type="submit" value="Submit" class="loginBtn" name="signIn" /> <input type="reset" value="Cancel" name="resetBtn" class="loginBtn leftmargin25" onclick="javascript:window.location = 'index.jsp'" style="border: 1px solid rgb(174, 174, 174); background-color: rgb(174, 174, 174); font-family: HP Simplified;"/></div>
				</div>
			</form>
			<div class='clear'></div>
			
		</div>
		
		<div class='footerTopLine'></div>
			<div id="loading">
		 	
		 </div>
		  <div id="mail_success" title="Success" class="nodisplay">
			<p class="topmargin15"><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
		New password has been sent to provided email address.</p>
		</div>
	<%@include  file="../common/footer.jsp"%>
	</div>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/common.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-ui-1.8.18.custom.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.validate.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/forgot_password.js"></script>
	<script type="text/javascript">

	
    
    </script>
   
</body>
</html>