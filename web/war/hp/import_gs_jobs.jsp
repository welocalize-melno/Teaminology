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
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/global_hp_new.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
</head>
<body class="export">
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
			<h1 class=' bottommargin10 floatleft'>Import</h1>
			<div class='clear'></div>
			<input type="hidden" value="" id="totalJobs"/>
			<div class="bottommargin10">
<input type="button" style="padding:3px 10px;margin-left:866px;" id="importGSTerms" name="importGSTerms" class="commonBtn" value="Import">			
 </div>
			<div class="topmargin15" id="teamListModule">
					<div id="importTbl" class='bottommargin10 topmargin15'>
						<div id='importSectionHead'>
						    <div id='column0' class='width5' >&nbsp;</div>
						 	<div id="jobId" class="width200">Job Id</div>
							<div id="jobName" class="width200">Job Name</div>
							<div id="projectName" class="width150">Project Type</div>
							<div id="jobStatus" class="width200" >Job Create Date</div>
							<div id="jobstartDate" class="width150" >Job Start Date</div>
							
							</div>
						<div id='importExportRowsList'>
							
						</div>
						<div id="pagination" style="height: 20px;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;">
							<div style="float:left;"><span >Viewing <span id="rangeOfList"> 0</span> of <span id="totalRecords">0</span></span></div>
							<div style="float: right;"><span class="rightmargin5 previous"  >Previous</span> | <span class="next leftmargin5">Next</span></div>
						</div>
					</div>
					
				</div>
			 
			<div id="taskerrror" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Please select at least one task. </p>
		</div>
				<div id="importedtask" title="Success" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Selected task will be imported. Please check the status. </p>
		</div>
		
		</div>
		 <div class='footerTopLine'></div>
		<div id="loading">
		 	
		 </div>
		 
	<%@include  file="../common/footer.jsp"%>
	</div>

	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/prototype.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tagsphere.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.qtip.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.dataTables.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/import_gs_jobs.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script  type="text/javascript"src="<%=application.getContextPath()%>/js-lib/jquery-progressbar.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            jQuery("#tabs").tabs();
            $('.dataTables_length').hide();
        });
    </script>
</body>
</html>