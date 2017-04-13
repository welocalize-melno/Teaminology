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
<title>Newell Terminology Community</title>
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css" type="text/css?<%=TeaminologyProperty.RELEASE.getValue()%>" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/global_hp_new.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<style type="text/css">
input.disabled {
    user-select : none;
    -moz-user-select : none;
    -webkit-user-select : none;
    color: gray;
    cursor: default;
    background-color: #eeeeee;
}
.viewFld {
    user-select : none;
    -moz-user-select : none;
    -webkit-user-select : none;
    color: gray;
    cursor: default;
    background-color: #eeeeee;
    border: 1px solid #dddddd;
    min-height: 18px;
    padding: 3px;
}
</style>
</head>
<body>
	<div class='siteContainer'>
	<%@include file="../common/header.jsp"%>
	<%
	if (session.getAttribute(SessionKey.USER.getKey()) == null) {
		 response.sendRedirect("./index.jsp");
	}
%>
		<div id="pwdMsg"></div>
		<div class='contentContainer'>
		<div class='leftContainer' >
					
		<div id="tabs" class="bottommargin15">
						<ul>
							<li class="tab1"><a href="#tabs-1">Profile</a></li>
							<li class="tab2"><a href="#tabs-2">Change password</a></li>
						</ul>
						<div id="tabs-1" style="padding: 15px;">
								<div class='regFrm topmargin10 floatleft' style="width:360px;">
									
								</div>
								<div class="floatright rightmargin20">
									<div style="width:100px; border:1px solid #cccccc;height: 100px;margin-top: 8px;"><img width="100px" id="changeImgId" height="100px" src="" /></div>
									<div class="topmargin10 alignCenter"><a href="javascript:;" class="changePic" style="font-size: 12px;">Change Picture</a></div>
									<!-- <div class="topmargin10 alignCenter" id="bagingImgId"></div>
									<div class="topmargin10 alignCenter" id="accuracyImgid" ></div> -->
								</div>
							<div class="clear"></div>
							
						</div>
						<div id="tabs-2" style="padding: 15px;">
						<form id="changeForm">
							<div class="topmargin10">
								<div class="chgPwd bottompadding10">
								<span class="nodisplay redTxt floatleft error"  style="padding-left: 136px" id="emailReq">Enter current password</span>
									<label for="cnfmEmail" class="error floatleft nodisplay" style="padding-left:138px;margin-top:-30px;padding-top:14px;" generated="true"></label><div class="clear"></div><div style="padding-bottom: 20px;"><div class="label width130 floatleft">Current password:<span class="redTxt">*</span></div> <div class="floatleft" style="width:200px;"><input type="password" size="25" value="" class="text190" name="cnfmEmail" id="cnfmEmail" maxlength="100" /> </div></div> <br class="clear" />
									<label for="password" class="error floatleft nodisplay" style="padding-left:136px;margin-top:-15px;" generated="true"></label><div class="clear"></div><div style="padding-bottom: 20px;"><div class="label width130 floatleft">New password<span class="redTxt">*</span></div> <div class="floatleft" style="width:200px;"><input type="password" size="25" value="" class="text190" name="password" id="password"  maxlength="100" /> </div><span class="nodisplay redTxt floatleft" style="padding-left: 10px"  id="passwordReq">Enter new password</span><div><span class="nodisplay redTxt floatleft" id="matchReq"  style="padding-left: 10px; width: 233px;">Password should be alphanumeric with at least one special character</span></div></div> <br class="clear" />
									<label for="confirm_password" class="error floatleft nodisplay" style="padding-left:136px;margin-top:-15px;" generated="true"></label><div class="clear"></div><div style="padding-bottom: 20px;"><div class="label width130 floatleft">Confirm password<span class="redTxt">*</span></div> <div class="floatleft" style="width:200px;"><input type="password" size="25" value="" class="text190" name="confirm_password" id="confirm_password"  maxlength="100" /> </div><div class="nodisplay redTxt floatleft"  style="padding-left: 10px" id="confmReq">Re-enter new password</div> <div class="nodisplay redTxt floatleft"  style="padding-left: 10px" id="pwdMatchReq">Enter the same password as above</div></div><br class="clear" />
									<div class="saveBtn" style="margin-left: 135px;margin-top: 20px;"><input type="button" value="Save" id="updatePwd" class="commonBtn padding5" /><input type="reset" value="Cancel" class="leftmargin20  padding5" id="cancelBtn"/></div> 
								</div>
							
							</div>
							</form>
							
						</div>
					</div>
				
				
				
			</div>
			
			<div class='clear'></div>
			
		</div>
		<div id="delete_cnfm" title="Delete" class="nodisplay">
			<p class="topmargin15"><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to delete?</p>
		</div>
		
		<div id="uploadPicBrwse" title="Upload picture" class="nodisplay">
		<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>              
		<form id="uploadPicForm" name="uploadPicForm"  target="uploadFrame" method="post" action="<%=application.getContextPath()%>/teaminology_ctrler/teaminology/uploadProfileImage"  ENCTYPE="multipart/form-data" >
				<p class="bold topmargin15"> Browse picture:</p>
				<p class="topmargin15"><input type="file" size="40" name="uploadPic" id="uploadPic"/></p>
				<p class="error nodisplay topmargin10" id="errorMsg"></p>
				<p class="topmargin15"><input type="button" value="Upload" id="uploadUserPic" class="commonBtn toppadding5" /> </p>
				</form>
		</div>
		 <div id="upload" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg">Please select the image to upload .</span></p>
		</div>
		
		<div id="pwdMessage" title="Success" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Password successfully changed.</p>
		</div>
		
		<div class='footerTopLine'></div>
	<%@include  file="../common/footer.jsp"%>
	</div>
	
	<script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tagsphere.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.qtip.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.dataTables.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Constants.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/profile.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>

    <script type="text/javascript">
        var pageUrl = '<%=pageUrl %>';
        jQuery(document).ready(function () {
            jQuery("#tabs").tabs();
            jQuery("input").click(function () {
                jQuery(this).focus();
                jQuery(this).removeClass("disabled");
                jQuery(this).removeAttr("readonly");
            });
        });
    </script>
</body>
</html>