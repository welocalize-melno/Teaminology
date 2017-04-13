<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import= "com.teaminology.hp.service.enums.TeaminologyPage,
                                  com.teaminology.hp.service.enums.SessionKey,
                                  com.teaminology.hp.service.enums.MenuEnum,
                                  com.teaminology.hp.bo.lookup.Company,
                                  com.teaminology.hp.bo.Menu,
                                  com.teaminology.hp.service.enums.SessionKey"%>
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
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/global_hp.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<style type="text/css">
#termDtlRowsList .1row{
	padding:11px 0 0 20px; 
	font-size:12px;
	border-bottom:1px solid #C0C0C1;
	min-height: 38px;
	width:592px;
	vertical-align: middle;
}

</style>
</head>
<body class="adminOvr">
	<div class='siteContainer'>
	<%@include file="../common/header.jsp"%>
        <%
            if (!adminUserFlag)
                response.sendRedirect("./index.jsp");
        %>

        <!--
		<div class='userInfoContainer'>
			<div id='userInfo'></div>
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
			<div class='floatright'><a href="index.jsp?msg=logOut" style="border-right:none;">Sign out</a></div>
		</div>
		       -->

		<div class='contentContainer'>
			<h1 class=' bottommargin10 floatleft'>Import/Export Status</h1>
			<div class='clear'></div>
				<div  class="bottommargin10 importStausBtn"  style="text-align: right;width:936px;">
						<input type="button" value="Delete Selected" class="commonBtn" name="deleteMultipleImports" id="deleteMultipleImports" style="padding:3px 10px;" />
						<!--  <input type="button" value="Delete All" class="commonBtn" name="deleteAllImports" id="deleteAllImports" style="padding:3px 10px;" />-->
			
			         </div>
			<div class="topmargin15" id="teamListModule">
					<div id="manageImportExportTbl" class='bottommargin10 topmargin15'>
						<div id='manageImportExportHead'>
						 <div id='column0' class='width25 checkboxCol' ><input type="checkbox" id="importSelectAll" title="Select all" style="margin-top:1px;margin-left:7px;"/></div>
						 	<div id="fileId" class="width30">S.No</div>
							<div id="fileName" class="width150">File Name</div>
							<div id="fileType" class="width90">File Type</div>
							<div id="startTime" class="width150" >Start Time</div>
							<div id="endTime" class="width130" >End Time</div>
							<div id="fileStatus" class="width150" >Status</div>
							<div id="fileDownload" class="width100" >Download File</div>
							<div id="fileLog" class="width100">Logs</div>
							</div>
						<div id='importExportRowsList'>
							
						</div>
						<div id="pagination" style="height: 20px;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;">
							<div style="float:left;"><span >Viewing <span id="rangeOfList"> 0</span> of <span id="totalRecords">0</span></span></div>
							<div style="float: right;"><span class="rightmargin5 previous"  >Previous</span> | <span class="next leftmargin5">Next</span></div>
						</div>
					</div>
					
				</div>
				
		</div>
		<div id="termSelctMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Please select at least one file to perform Action.</p>
		</div>
			<div id="mail_failed" title="Failure" class="nodisplay">
			<p class="topmargin15"><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
			<span id="failmsg"></span> <span id="failedUserEmail"></span>.</p>
		</div>
			 <div id="delete_cnfm" title="Delete" class="nodisplay">
			<p class="topmargin25"><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to delete? Deletion will erase all records associated with this file from the database.</p>
		</div>
		<div class='footerTopLine'></div>
			<%@include  file="../common/footer.jsp"%>
	</div>
	<script type="text/javascript">
	</script>
		
    <script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tagsphere.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.dataTables.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.qtip.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-ui-1.8.18.custom.min.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/prototype.min.js"></script>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.validate.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/import_export_status.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js"></script>
	<script src="<%=application.getContextPath()%>/js-lib/jquery-progressbar.js" type="text/javascript"></script>
    

</body>
</html>