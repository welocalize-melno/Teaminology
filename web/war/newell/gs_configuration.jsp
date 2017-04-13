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
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/global_hp_new.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
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
<body class="searchTermsList">
	<div class='siteContainer'>
	<%@include file="../common/header.jsp"%>
        <%
            if (!adminUserFlag)
                response.sendRedirect("./index.jsp");
        %>

        <!--
		<div class='userInfoContainer'>
			<div id='userInfo'>
					
			</div>
			<div class="admnNumUsersChrt">
				<div class='paddingseven'>
					<span  id="totalUsers" class="bigNumber"></span>
					<div class="floatleft" id='userChart' style='float:right;'></div>
				</div>
				<br class='clear' />
				<span class='' style='position:absolute;top:60px;left:10px;font-size:12px;'>Total users</span>
			</div>
			
		</div> 
		<div class='subMenu'>
		<div id="menuItems" class='floatleft subMenuLinks'></div>
			<div  id='SignOut' class='floatright rightmargin25'><a href="index.jsp?msg=logOut" style="border-right:none;">Sign Out</a></div>
		</div>
		       -->

			<div class='contentContainer'> 
			<h1>Configuration</h1>
			<div class='regFrm topmargin20 floatleft' style="width:560px;">
									
								</div>
		<!--  <div id="addUser">
		 	<form id="creditialsForm">
				<div class="regFrm topmargin30">
					
					<label for="gsUrl" class="error floatright nodisplay" style="width:195px;margin-right:485px;margin-top:-31px;" generated="true"></label><div class="clear"></div>
						<div class="" style="padding-bottom: 20px;padding-left:140px;"><div class="label width110 floatleft">Url<span class="redTxt">*</span>:</div><div class="floatleft" style="width:200px;"><input type="text"  id="gsUrl" size="25" value="" class="text190" name="gsUrl" tabindex="3"/></div></div><br class="clear" />
						<label for="username" class="error floatright nodisplay" style="width:195px;margin-right:10px;margin-top:-31px;" generated="true"></label><div class="clear"></div>
						<div class="" style="padding-bottom: 20px;padding-left:140px;"><div class="label width110 floatleft">User name<span class="redTxt">*</span>:</div><div class="floatleft" style="width:200px;"><input type="text"  id="username" size="25" value="" class="text190" name="userName" tabindex="3"/></div></div><br class="clear" />
						<label for="password" class="error floatright nodisplay" style="width:195px;margin-right:10px;margin-top:-31px;" generated="true"></label><div class="clear"></div>
						<div class="" style="padding-bottom: 20px;padding-left:140px;"><div class="label width110 floatleft">Password<span class="redTxt">*</span>:</div><div class="floatleft" style="width:200px;"> <input type="password"  id="password" size="25" value="" class="text190" name="password"  tabindex="5"/></div> </div><br class="clear" />
				
					<div  id="gsCreditials" style="margin-left: 257px;margin-top:15px;" ><input type="button" value="Submit" class="loginBtn gsCreditials" name="submitBtn"/> <input id="cancelGsCreditials" type="button" name="resetBtn" value="Cancel" class="loginBtn leftmargin25" /></div>
				</div>
			</form>	
		</div> -->
   </div>
   <div class='footerTopLine' style="margin-top:250px;"></div>
	<%@include  file="../common/footer.jsp"%>
	</div>

	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/prototype.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tagsphere.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.qtip.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-ui-1.8.18.custom.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.multiselect.filter.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.multiselect.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.validate.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/json2.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/common.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Constants.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/charts.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/qtip.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/leaderBoard.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/gs_configuration.js"></script> 
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            jQuery("#tabs").tabs();
            $('.dataTables_length').hide();
        });
    </script>
</body>
</html>