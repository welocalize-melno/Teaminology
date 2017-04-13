<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="com.teaminology.hp.Utils,
    							 com.teaminology.hp.bo.User,
    							  com.teaminology.hp.bo.lookup.Company,
    							 com.teaminology.hp.service.enums.SessionKey"%>
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
<link href="<%=application.getContextPath()%>/css/global_hp.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
</head>
<%
%>
<body>
	<div class='siteContainer'>
	<%@include file="../common/header.jsp"%>
		<div class='contentContainer'>
<!--		<h1 class='tagline'>Register Now!</h1>  -->
				<form id = "registerForm">
 				<div class="regFrm" >
					<!-- <div class="blueTxt subCaption bottommargin15">*Note: Your username will be used throughout Welocalize Terminology Portal and may appear in Leader board. Your remaining profile information will only be visible to administrators. </div> -->
					<h1 class="tagline" style="margin-bottom: 30px;margin-left: 52px;margin-top: 30px;"> Sign Up and Vote </h1>
					<div class='boxWidth330 topmargin20'>
						<label for="email" class="error floatright nodisplay" style="width:200px;margin-right:14px;margin-top:-16px;" generated="true"></label><div class="clear"></div>
						<div class="error floatright nodisplay userExistsErr" style="width:200px;margin-right:14px;margin-top:-30px;"></div><div class="clear"></div>
						<div class="error floatright nodisplay emailExistsErr" style="width:200px;margin-right:14px;margin-top:-30px;"></div><div class="clear"></div>
						<div class="" style="padding-bottom: 20px;" ><div class="label width110 floatleft">Email<span class="redTxt">*</span>:</div><div class="floatleft" style="width:200px;"> <input type="text"  id="email" size="25" value="" class="text220" name="email" tabindex="4" maxlength="100" /></div> </div> <br class="clear" />
						<label for="password" class="error floatright nodisplay" style="width:200px;margin-right:14px;margin-top:-16px;" generated="true"></label><div class="clear"></div>
						<div class="" style="padding-bottom: 20px;"><div class="label width110 floatleft">Password<span class="redTxt">*</span>:</div><div class="floatleft" style="width:200px;"> <input type="password"  id="password" size="25" value="" class="text220" name="password"  tabindex="5"  maxlength="100"/></div> </div><br class="clear" />
						<label for="cnfmPassword" class="error floatright nodisplay" style="width:200px;margin-right:14px;margin-top:-16px;" generated="true"></label><div class="clear"></div>
						<div class="" style="padding-bottom: 20px;"><div class="label width110 floatleft">Confirm password<span class="redTxt">*</span>:</div><div class="floatleft" style="width:200px;"><input type="password" id="cnfmPassword" size="25" value="" class="text220" name="cnfmPassword"  tabindex="6" maxlength="100"/></div>  </div> <br class="clear" />
						<div class="floatright nodisplay languageSlctErr" style="width:242px;margin-right:-26px;margin-top:-16px;"><span class="error languageSlctErr">Select at least one language</span></div>
						<div  class="boxWidth350"><div class="label width110 floatleft">Language(s)<span class="redTxt">*</span>:</div>
						 <div class="floatleft"  id="langToolTip" style="margin-bottom:30px;">
							<select id="languageSlct" name="languageSlct" multiple="multiple"  class="text220" tabindex="7">
							
							</select> 
							</div>
							<div style="margin-left: 116px;"><input type="button" value="Submit" class="loginBtn" name="submitBtn" tabindex="10"/> <input id="cancelBtn" type="reset" name="resetBtn" value="Cancel" class="cancelBtn leftmargin25"  tabindex="11" style="border: 1px solid rgb(174, 174, 174); background-color: rgb(174, 174, 174); font-family: HP Simplified;" onclick="javascript:window.location = 'index.jsp'"/></div>
						</div>
			    		
					</div>
						<div class="width170 picPrvw leftmargin15">
						<div style="width:100px; border:1px solid #cccccc;height: 100px;margin-top: 8px;margin-left: 33px;"><img width="100px" height="100px" id="uploadedImage" src="<%=application.getContextPath()%>/images/profile-photo-placeholder.png" /></div>
						<div class="topmargin10 alignCenter"><a style="cursor: pointer" id ="uploadPicId" class="uploadPic">Upload picture</a></div>
					</div>
					<div class="clear"></div>
					
				</div>
 
			<div class='clear'></div>
			</form>
		</div>
		
		<div id="uploadPicBrwse" title="Upload picture" class="nodisplay">
		<iframe id="uploadRegFrameID" name="uploadRegFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>              
		<form id="uploadRegPicForm" name="uploadRegPicForm"  target="uploadRegFrame" method="post" action="<%=application.getContextPath()%>/teaminology_ctrler/teaminology/uploadProfileImage"  ENCTYPE="multipart/form-data" >
				<p class="bold topmargin15"> Browse picture:</p>
				<p class="topmargin15"><input type="file" size="40" name="uploadRegPic" id="uploadRegPic"/></p>
				<p class="picError nodisplay topmargin10 redTxt" >Failed to Detect Face</p>
				<p class="topmargin15"><input type="button" value="Upload" id="uploadUserRegPic" class="commonBtn toppadding5" /> </p>
				</form>
		</div>
		<div id="upload" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg">Please select the image to upload.</span></p>
		</div>
		
		<div class='footerTopLine'></div>
	<%@include  file="../common/footer.jsp"%>
	</div>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/prototype.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/json2.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/common.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.qtip.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-ui-1.8.18.custom.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.multiselect.filter.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.multiselect.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.validate.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/qtip.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Constants.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/registration.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>

</body>
</html>