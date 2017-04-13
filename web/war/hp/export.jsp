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
<title>HP Terminology Community</title>
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/global_hp_new.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
</head>
<body class="export">
	<div class='siteContainer'>
	<%@include file="../common/header.jsp"%>
        <%
            if (!adminUserFlag)
                response.sendRedirect("./index.jsp");
        %>
		<div class='contentContainer'>
		<h2 class=' bottommargin10 '>Export</h2>
		
		<div  class="bottommargin10 exportBtns exportalign">
		<input type="button" value="Export" class="commonBtn" name="exprtFile" id="exprtFile"  style="padding:3px 10px;"/> &nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" value="Delete" class="commonBtn" name="deleteMultipleFileIds" id="deleteMultipleFileIds" style="padding:3px 10px;" />
		</div>
			
		<div class="topmargin15" id="termsModule">
				<div id="exportTbl" class='bottommargin10 topmargin25'>
					<div id='exportSectionHead'>
							<div class='floatleft' sortOrder="ASC">&nbsp</div>
							<div id='fileId' class='width100' sortOrder="ASC">Page Id</div>
							<div id='fileName' class='width100' sortOrder="ASC">File Name</div>
							<div id='jobId' class='width90' sortOrder="ASC">JobId</div>
							<div id='jobName' class='width100' sortOrder="ASC">Job Name</div>
							<div id='taskId' class='width90' sortOrder="ASC">TaskId</div>
							<div id='sourceLang' class='width100' sortOrder="ASC">Source Language</div>
							<div id='targetLang' class='width100' sortOrder="ASC">Target Language</div>
							<div id='status' class='width90' sortOrder="ASC">Status</div>
							<div id='status' class='width90' sortOrder="ASC">Log</div>
							<div id='delete' class='width40' sortOrder="ASC">Delete</div>
						
						</div>
						
						<div id='exportDtlRowsList' >
							
						</div>
						<div id="pagination" style="height: 20px;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;">
							<div style="color: #999999; float:left">Viewing <span id="rangeOfTerms">0 </span> of <span id="totalPolledTerms">0</span></div>
							<div style="float: right;"><span class="rightmargin5 previous" >Previous</span> | <span class="leftmargin5 next">Next</span></div>
						</div>
					</div> 
		<div id="exportRequired" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Please select at least one file.</p>
		</div>
		<div id="deleteGS" title="Success" class="nodisplay">
			<p class="topmargin20"><span class="ui-icon ui-icon-circle-check" style="float:left;margin:0 7px 25px 0;"></span>
		Deleted successfully.</p>
		</div>
		<div id="fileSelectMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Please select at least one term to perform Action.</p>
		</div>
		<div id="exportStatus" title="Alert" class="nodisplay">
			<p class="topmargin20"><span class="ui-icon ui-icon-circle-check" style="float:left;margin:0 7px 25px 0;"></span>
		File cannot be deleted as the file status is in progress.</p>
		</div>
		<div id="exportMultipleStatus" title="Alert" class="nodisplay">
			<p class="topmargin20"><span class="ui-icon ui-icon-circle-check" style="float:left;margin:0 7px 25px 0;"></span>
		Selected files cannot be deleted as the file status of  files is  in progress.</p>
		</div>
		<div id="deleteMultiple" title="Alert" class="nodisplay">
			<p class="topmargin20"><span class="ui-icon ui-icon-circle-check" style="float:left;margin:0 7px 25px 0;"></span>
		Deleted successfully.</p>
		</div>
		 <div id="delete_cnfm" title="Delete" class="nodisplay">
			<p class="topmargin25"><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to delete? Deletion will erase all records associated with this term from the database.</p>
		</div>
			<iframe id="downloadFrame" name="downloadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>              
							<form id="downloadForm" name="downloadForm" target="downloadFrame" method="post">
					</form>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/export.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    
   <script type="text/javascript">

$(document).ready(function() {
	
	jQuery( "#tabs" ).tabs();
    $('.dataTables_length').hide();
} );

</script>
</body>
</html>