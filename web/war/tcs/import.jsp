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
		
		<h2 class=' bottommargin10 '>View Segments</h2>
		
		
		 	<div class="importGSTerm nodisplay">
							<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
								<form id="termImportForm" name="termImportForm" method="post" target="uploadFrame" action="<%=application.getContextPath()%>/teaminology_ctrler/teaminology/getImportXliffData"  ENCTYPE="multipart/form-data">
								<input type="hidden" name="type" id="selectedCompanyIds" value=""/>
								<div class='gsSerchContainer'>
					<div class="searchTm   bottommargin10" >
					<div class="bottommargin10">Term Search:</div>
					<div><input type="text" id="newTerm" value="Enter term to search..." size="35"></div>
					<div style="margin-right: 5px;" class="smallFont topmargin10"><div style="margin-top: 3px;" class="floatleft"><input type="checkbox" name="chktmCase" id="chktmCase"></div> <div class="topmargin5 floatleft">Case sensitive</div></div>
					<div style="text-align: right;margin-right: 5px;"><input type="button" value="Search" id="searchTerm"></div>
				</div>
				</div>
								<div class="topmargin10 companySlctDropdwn  slctCompany  nodisplay bottommargin10 ">
									<select size="5" name="example-basic" multiple="multiple" title="select company" id="mutliCompanySlct"></select>
								</div>
								<div class="topmargin10 langSlctDropdwn    bottommargin10 ">
									<select size="5" name="example-basic" multiple="multiple" title="select language" id="mutlilangSlct"></select>
								</div>
									<div class="topmargin10 jobSlctDropdwn bottommargin10"><select id="jobSlct" name="jobSlct" multiple="multiple"  class="text220" ></select></div>
								<div class="topmargin10 tskDropdown bottommargin10 nodisplay"><select id="taskSelectId" name="taskSelectId" multiple="multiple"  class="text220" ></select></div>
									
													
								
									<!--  <div style="font-size:13px;"><span>Upload File :&nbsp; </span> <input type="file" size="40" name="upload" id="importTerm" style="font-size:12px;"/>  <img id="importFile" src="<%=application.getContextPath()%>/images/document_import.png" height="35px" width="35px;" alt="Click to Import file" title="Click to Import file" style="vertical-align: bottom; margin-left: 30px;cursor:pointer;" />  </div>-->
								</form>
									
								<div id="message"></div>
					</div> 
				
		<div class="topmargin15" id="termsModule">
				<div id="action" class="gsInviteToVote nodisplay">	<p class="bold">Action:  
					<select id="termAction" name="termAction" class="invite_vote">	
						<option value="0">Select action</option>
						<!--  <option value="1">Delete</option>-->
						<option value="2">Invite to vote</option>
					</select>
			 </div>
			 <div  class="bottommargin10">
						<input type="button" value="Delete Selected" class="commonBtn" name="deleteMultipleGSTerms" id="deleteMultipleGSTerms" style="padding:3px 10px;margin-left:690px;" />&nbsp;&nbsp;
						<input type="button" value="Delete All" class="commonBtn" name="deleteAllGSTerms" id="deleteAllGSTerms" style="padding:3px 10px;" />
			
			         </div>
				<div id="importTbl" class='bottommargin10 topmargin25'>
				<!--  	<div id='importSectionHead' >
							
							<div id='targetTerm' class='width130' sortOrder="ASC">Source Segment</div>
							<div id='column2' class='width40' >&nbsp;</div>
							<div id='suggestedTerm' class='width150' sortOrder="ASC">Target Segment</div>
							<div id='sourceLang' class='width110' sortOrder="ASC">Source Language</div>
							<div id='targetLang' class='width110' sortOrder="ASC">Target Language</div>
							<div id='origin' class='width110' sortOrder="ASC">Origin of match</div>
							<div id='pageId' class='width90' sortOrder="ASC">Page ID</div>
							<div id='workFlowId' class='width90' sortOrder="ASC">Trans-unit  ID</div>
							<div id='taskId' class='width80' sortOrder="ASC">TaskID</div>
							
						</div>-->
								<div id='importSectionHead'  >
						  <div id='column0' class='width20'><input type="checkbox" id="selectAll" title="Select all" style="margin-top:1px;margin-left:-12px;"/></div>
							<div id='targetTerm' class='width90' sortOrder="ASC">Source </div>
							<div id='column2' class='width40' >&nbsp;</div>
							<div id='suggestedTerm' class='width110' sortOrder="ASC">Target </div>
							<div id='sourceLang' class='width110' sortOrder="ASC">Source Language</div>
							<div id='targetLang' class='width110' sortOrder="ASC">Target Language</div>
							<div id='origin' class='width110' sortOrder="ASC">Origin of match</div>
							<div id='pageId' class='width60' sortOrder="ASC">Page ID</div>
							<div id='workFlowId' class='width80' sortOrder="ASC">Trans-unit ID</div>
							<div id='taskId' class='width50' sortOrder="ASC">Job ID</div>
							<div id='taskId' class='width50' sortOrder="ASC">Task ID</div>
							<div id='termEdit' class='width40' sortOrder="ASC">Edit</div>
							<div id='termDelete' class='width40' sortOrder="ASC">Delete</div>
							
						</div>
						<div id='termDtlRowsList' >
							
						</div>
						<div id="pagination" style="height: 20px;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;">
							<div style="color: #999999; float:left">Viewing <span id="rangeOfTerms">0 </span> of <span id="totalPolledTerms">0</span></div>
							<div style="float: right;"><span class="rightmargin5 previous" >Previous</span> | <span class="leftmargin5 next">Next</span></div>
						</div>
					</div> 
		</div>	
		
		
				
		<div id="invitation"  title="Invite to vote" class="nodisplay" style="padding-left:15px;">
		<form id="termVoteForm">
			<div class="topmargin5">
			<label for="categorySlct" class="error floatleft nodisplay" style=" margin-left: 48px;" generated="true"></label><div class="clear"></div>
			<p class="bold">Select<span class="redTxt">*</span>: <select class="categorySlct text178" id="categorySlct" name="categorySlct"><option value="">--Select--</option><option value="role">Role</option><option value="language">Language</option></select></p><div class="nodisplay redTxt leftmargin100 " id="usersReq" style="padding-left:40px ">Select any one category to invite users</div></div>
			<div class="topmargin15 floatleft" id="category_list">
				<div class="nodisplay redTxt leftmargin100" id="selectReq" style="font-size: 11px;font-style: italic;">Select at least one from the list</div>
				<select class="categoryList text220" name="categoryList1"  multiple size="5"></select>
				
			</div>
			<div class="width50 floatleft leftmargin15 topmargin20" >
				<img src="<%=application.getContextPath()%>/images/right.png" alt="" height="30px" width="30px" class="moveData" /><br class="clear" />
				<img src="<%=application.getContextPath()%>/images/delete.png" alt="" height="30px" width="30px" class="removeUser topmargin10" />
			</div>
			
			<div class="floatleft" id="users_list">
				<p class="bold">Invited users</p>			
			<div class="nodisplay redTxt leftmargin100" id="noUsersReq" style="font-size: 11px;font-style: italic;">No users for selected category</div>
				<select class="usersList text220" name="usersList"  multiple size="5">
								
				</select>
				
							
			</div>
			<div class="clear"></div>
			<div id="votingPeriod" class="topmargin5">
			<label for="votingPeriodNum" class="error floatleft nodisplay" style="margin-left:115px;" generated="true"></label><br class="clear"/>
				<p class="bold">Voting period (#days): <input id="votingPeriodNum"  name="votingPeriodNum"  type="text" size="23" value="" /></p>
			</div>
			<div class="clear"></div>
			<div class="topmargin10">
			 <label for="termTemplate" class="error floatleft nodisplay" style="margin-left:96px;" generated="true"></label><br class="clear"/>
				<p class="bold">Email template<span class="redTxt">*</span>:  
					<select class="text190 inviteEmailTemplate" id="termTemplate" name="termTemplate">
						<option value="">--Select template--</option>
					</select>
				</p>
			
			</div>
			<div class="clear"></div>
			<div class="topmargin15 emailPrvw" style="height:180px;width:500px; border:1px solid #888888;overflow: auto;">
				
			</div>
			<div class="clear"></div>
			<div class="sendMail topmargin15" style="text-align: center;">
				<input type="button" value="Send mail" id="inviteVoteMail"  name="inviteVoteMail" class="rightmargin15 commonBtn padding5" />
				<input type="button" value="Cancel" id="cancelInvitMail" name="cancelInvitMail" class="commonBtn padding5" />
			</div>
			</form>
			
		</div>			
						<div id="termImportSuccess" title="Success" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Successfully imported XLF file.</p>
		</div>
		<div id="termImportFailed" title="Failed" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Failed to import XLF file.</p>
		</div>
		<div id="mail_success" title="Success" class="nodisplay">
			<p class="topmargin15"><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
		<span id="logmsg">Invitation is successfully sent.</span></p>
		</div>
		
		<div id="deleteGS" title="Success" class="nodisplay">
			<p class="topmargin20"><span class="ui-icon ui-icon-circle-check" style="float:left;margin:0 7px 25px 0;"></span>
		Deleted successfully.</p>
		</div>
		<div id="importMessage" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Please select a file.</p>
		</div>
		<div id="termSelctMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Please select at least one term to perform Action.</p>
		</div>
		<div id="termSelctMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Please select at least one term to perform Action.</p>
		</div>
			 <div id="delete_cnfm" title="Delete" class="nodisplay">
			<p class="topmargin25"><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to delete? Deletion will erase all records associated with this term from the database.</p>
		</div>
		
		<div id="termImprtInfo" class="nodisplay" title="Import terms">
				<p class="topmargin15 greenTxt"> <span id="successAddedTerms"></span></p>
				<p class="topmargin15 redTxt failedInfo">Failed to import as 'Source term/target language is not available' : <span id="failedAddedTerms"></span></p>
			   <p class="topmargin15 redTxt invalidfile">Imported file is invalid.Please upload valid file.</p>
				
				<div class="failedInfo"> 
					<div class="userImportTblHead topmargin15" style="width:350px;"> 
						<div class="floatleft" style="width:220px;">Source term</div>
						<div class="floatleft" style="width:130px;">Line # in import file</div>
					</div>
					<div class="clear"></div>
					<div class="termImportTblBody" style="width:365px;">
					</div>
				</div>
			</div>
					<div id="validationMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Enter valid characters.</p>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/import.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script> 
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            jQuery("#tabs").tabs();
            $('.dataTables_length').hide();
        });
    </script>
</body>
</html>