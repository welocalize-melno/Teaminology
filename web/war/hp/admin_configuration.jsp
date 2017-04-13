<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import= "com.teaminology.hp.service.enums.TeaminologyPage,
                                  com.teaminology.hp.service.enums.SessionKey,
                                  com.teaminology.hp.service.enums.MenuEnum,
                                  com.teaminology.hp.bo.lookup.Company,
                                  com.teaminology.hp.bo.Menu,
                                  java.util.List"%>
<%@ page import="com.teaminology.hp.bo.SubMenu" %>
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
<link href="<%=application.getContextPath()%>/css/jquery.treeview.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet" />
<link href="<%=application.getContextPath()%>/css/screen.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet"  />
<link href="<%=application.getContextPath()%>/css/global_hp_new.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">

</head>
<body class="adminOvr">
	<div class='siteContainer'>
	<%@include file="../common/header.jsp"%>
	<%
	if (session.getAttribute(SessionKey.USER.getKey()) == null) {
		 response.sendRedirect("./index.jsp");
	}
%>
    <%
        List<SubMenu> subMenus = (List<SubMenu>) session.getAttribute(SessionKey.SUB_MENU.getKey());
        boolean pageFlag = false;
        if (subMenus != null) {
            for (SubMenu subMenu : subMenus) {
                if ("admin_configuration.jsp".equalsIgnoreCase(subMenu.getPageUrl())) {
                    pageFlag = true;
                    break;
                }
            }
        }
        if (!pageFlag)
            response.sendRedirect("./index.jsp");
    %>
		<div class='contentContainer'>
			<div class='leftContainer' id="manageConfig">
				<div>
					<p class="bold">Select module to manage/configure 
						<select id="configSlct" style="width:200px;">
							<option value="0">--Select--</option>
						    <option value="domain" class="confDomain nodisplay">Domain</option>
							<option value="vt" class="confVt nodisplay">Voting threshold</option>
							<option value="lp" class="confLang nodisplay">Language pairs</option>
							<option value="category" class="confCat nodisplay">Category</option>
							<option value="et" class="confEmailTemplate nodisplay">Create/Update email template</option>
							<option value="etDel" class="confEmailTemplate nodisplay">Delete email template</option>
							<option value="roleName" class="mgRole nodisplay">Manage Roles</option>
							<option value="mgRoles" class="mgPgs nodisplay">Manage Privileges</option>
						</select>
					</p>
				</div>
				
				<div id="manageDomain" class="topmargin30 configTbls nodisplay" style="width:360px;">
					<h3>Manage domain</h3>
					<div id="addBu" class="bottommargin20" >
					   <form id="domainForm">
					    <div class="topmargin20">
					    <div class="error nodisplay floatLeft dLabelExists"  id="dLabelExists" style="margin-bottom: -10px; margin-top: -7px;padding-left: 115px;"></div><div class="clear"></div>
						 <label for="domain" class="error floatright nodisplay" style="width:212px;margin-right:30px;margin-top:-2px;" generated="true"></label><br class="clear"/>
							<div class="width110 floatleft">Add domain<span class="redTxt">*</span>:</div>
							<div class="floatleft leftmargin5"><input  id="domain" type="text" size="20" value="" maxlength="45" name="domain" style="margin-right: 5px;" /></div>
						</div>
						<div class="clear"></div>
						</form>	
						<br/>
						<div class=""><input type="button" value="Save" class="commonBtn domainBtn"  /><input type="button" value="Cancel" class="commonCancelBtn leftmargin15"  /></div>
					</div>
						<div class="clear"></div>				
					<div class="error nodisplay idExists"  id="idExists" style="margin-top: -6px;"></div><div class="clear"></div>
					<div class="error nodisplay domainExists"  id="domainExists" style="margin-top: -6px;"></div><div class="clear"></div>
					
						<div id='configtblHead' class="configtblHead" style="width:330px;">
							<div id='column1' class='width130'>Domain</div>
							<div id='column2' class='width170'>&nbsp;</div>
						</div>
						<div id="domainList" class="configLst" style="width:330px;">
							
						</div>
						<div class="configLstItem nodisplay" id="noDataDomain" style="text-align:center;width:330px;">
								<div style="width:100%;"> No data to display</div>
							</div>
				</div>
				
				<div id="manageVT" class="topmargin30 configTbls nodisplay" style="width:500px;">
					<h3>Manage voting threshold</h3>
					<div class="bottommargin20">
					<div class="error nodisplay periodExists"  id="periodExists" style="margin-bottom: -32px;margin-top: 11px; padding-bottom: 19px;"></div><div class="clear"></div>
					<div class="error nodisplay numVotesExists"  id="numVotesExists" style="margin-bottom: -31px;margin-top: 11px; padding-bottom: 19px;" ></div><div class="clear"></div>
					<div class="error nodisplay daysExists"  id="daysExists" style="margin-bottom: -32px;margin-top: 11px; padding-bottom: 19px;"></div><div class="clear"></div>
					<div class="error nodisplay votesExists"  id="votesExists" style="margin-bottom: -32px;margin-top: 11px; padding-bottom: 19px;"></div><div class="clear"></div>
					</div>
					<div class="addVT bottommargin20 nodisplay">
						<form id="vtForm">
								<div>
								 <label for="period" class="error floatright nodisplay"  style="width:212px;margin-right:148px;margin-top:-2px;" generated="true"></label><br class="clear"/>
									<div class="width130 bottommargin10 floatleft">Voting period (#days)<span class="redTxt">*</span>:</div>
									<div class="floatleft leftmargin5"><input type="text" size="20" value=""  id="period" name="period" maxlength="11"  style="margin-right: 5px;" /></div>
								</div>	
								<div class="clear"></div>
								<div>
								 <label for="user" class="error floatright nodisplay"  style="width:212px;margin-right:149px;margin-top:-2px;" generated="true"></label><br/>
									<div class="width130 floatleft">Votes per User<span class="redTxt">*</span>:</div>
									<div class="floatleft leftmargin5"><input type="text" size="20" value="" id="user" name="user" maxlength="11" style="margin-right: 5px;" /></div>
								</div>
								<div class="clear"></div>
								<div class="topmargin20"><input type="button" value="Add" id="addNewVT" class="commonBtn"  /><input type="submit" value="Cancel" class="commonCancelBtn leftmargin15"  /></div>
							</form>
						</div>
						<div class="clear"></div>
						<div id='configtblHead' class="topmargin15 configtblHead" style="height:30px;width:400px;">
							<div id='column1' class='width110'>Voting period<br />(#days)</div>
							<div id='column1' class='width110'>Votes per user <br />(# of votes per term)</div>
							<div id='column2' class='width130'>&nbsp;</div>
						</div>
						<div class="clear"></div>
						<div class="configLst" id="dataVT" style="width:400px;">
							
							<div class="configLstItem">
								<div class='width110 viewDtl' id="votePeriod"></div><div class="width110 editDtl nodisplay"><input type="text" size="10" maxlength="11" value="" id="newVotePeriod" /></div>
								<div class='width110 viewDtl' id="votesPerTerm"></div><div class="width110 editDtl nodisplay"><input type="text" size="10" maxlength="11" value="" id="newVotePerTrm" /></div>
								<div class='width130 alignRight'><a href="javascript:;" class="editVt">Edit</a><a  class="saveVt nodisplay" href="javascript:;">Save</a> | <a href="javascript:;" class="deleteVt">Delete</a></div>
							</div>
						</div>
						<div class="configLstItem nodisplay" id="noDataVT" style="text-align:center;width:400px;">
								<div style="width:100%;"> No data to display</div>
							</div>
				</div>
				
				<div id="manageLangs" class="topmargin30 configTbls nodisplay" style="width:420px;">
					<h3>Manage language pairs</h3>
					<div id="addlang" class="topmargin20">
					   <form id="langForm">
					    <label for="language" class="error floatleft nodisplay" style="margin-top:-16px;padding-left:98px;" generated="true"></label><div class="clear"></div>
						<div>
						<div class="error floatright nodisplay labelExists" style="margin-right:-5px;margin-top:-15px;" ></div><div class="clear"></div>
							<div class="floatleft width90" >Language<span class="redTxt">*</span>:</div>
							<div class="floatleft leftmargin5"><input id="language" maxlength="50" type="text" size="20" value="" name="language" style="margin-right: 5px;" /></div>
						</div>		
						<div class="clear"></div>
						<div class="topmargin15">
						 <label for="code" class="error floatleft nodisplay"style="margin-top:-16px;padding-left:98px;" generated="true"></label><div class="clear"></div>
						 
						 <div class="error floatright nodisplay codeExists" style="margin-right:-5px;margin-top:-15px;"></div><div class="clear"></div>
						 	<div class="floatleft width90">Code<span class="redTxt">*</span>:</div>
							<div class="floatleft leftmargin5"><input  id="code" maxlength="45" type="text" size="20" value="" name="code" style="margin-right: 5px;" /></div>
						</div>				
						<div class="clear"></div>
						<div class="topmargin20"><input type="button" value="Save" class="commonBtn addLangBtn"  /> <input type="button" class="commonCancelBtn leftmargin20" value="Cancel" /></div>
					  </form>				
					</div>
					 <label for="lLabel" class="error floatright nodisplay" style="width:212px;margin-right:290px;margin-top:0px;margin-bottom:4px" generated="true"></label><div class="clear"></div>
					<div class="error nodisplay langLabelExists" style="margin-right:-91px;margin-top:10px;"></div><div class="clear"></div>
					 <label for="lCode" class="error floatright nodisplay" style="width:212px;margin-right:290px;margin-top:0px;margin-bottom:4px" generated="true"></label><div class="clear"></div>
					<div class="error nodisplay langCodeExists" style="margin-right:-91px;margin-top:5px"></div><div class="clear"></div>
					<div>
						<div id='configtblHead' class="configtblHead topmargin15">
							<div id='languageLabel' class='width200' sortOrder="ASC">Language label</div>
							<div id='languageCode' class='width90' sortOrder="ASC">Code</div>
							<div id='column2' class='width110'>&nbsp;</div>
						</div>
						<div class="clear"></div>
						<div id="langsList" class="configLst"></div>
						<div class="configLstItem nodisplay" id="noDataLangs" style="text-align:center;">
							<div style="width:100%;"> No data to display</div>
						</div>
						
						<div id="pagination" style="height: 20px;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;">
							<div style="color: #999999; float:left">Viewing <span id="rangeOfList">0</span> of <span id="totalData">0</span></div>
							<div style="float: right;"><span class="rightmargin5 previous" >Previous</span> | <span class="leftmargin5 next">Next</span></div>
						</div>
					</div>
				</div>
				
				<div id="manageEmailTmpl" class="topmargin30 configTbls nodisplay" style="width:550px;">
					<h3>Create/Update email templates</h3>
						<div id="emailTmplList" class="topmargin15 bottommargin15">
							<span class="bold">Select email template<span class="redTxt">*</span>:</span> 
							<select id="emailtmpl">
								
								<option value="0">Add new template</option>
							</select>
						</div>
					<!--  	<div class="slctTmplate nodisplay error">Select at least one email template</div>-->
						
						<div id="newTmpl" class="nodisplay">
						<form id="templateForm">
						 <label for="newSub" class="error floatright nodisplay" style="padding-right: 400px;margin-top:-5px;" generated="true"></label><br/>				
							<span class="bold">Subject<span class="redTxt">*</span></span>: <input type="text" value="" size="25" id="newSub" name="newSub"/>	
							</form>
						</div>
						<form class="topmargin15">
					        <p>     
				                <textarea name="content" cols="50" rows="18" id="content" class="tinymce1">This is some content that will be editable with TinyMCE.</textarea> </p>
				                
				                 <!--  <div  id="mailTemplate" class="greenTxt topmargin15" style="font-size:11px;">EmaiLtemplate successfully added/updated</div>-->
				              <div class="topmargin15">
				                <input type="button" class="commonBtn updateEmailBtn" value="Save"  />
				                <input type="button" class="commonCancelBtn leftmargin20" value="Cancel"  />
					       	 </div>
						</form>
				</div>
				<div id="deleteEmailTmpl" class="topmargin30 configTbls nodisplay" style="width:450px;">
					<h3>Delete email templates</h3>
					<div class="topmargin20 configtblHead" id="configtblHead">
						<div class="width170" id="column1">Email template</div>
						<div class="width130" id="column2">&nbsp;</div>
					</div>
					<div class="configLst" id="emailSubList">
						
					</div>
					<div class="configLstItem nodisplay" id="noDataET" style="text-align:center;">
							<div style="width:100%;"> No data to display</div>
						</div>
				</div>
				
				<div id="manageCategory" class="topmargin20 configTbls nodisplay" style="width:360px;">
					<h3>Manage category</h3>
					<div id="addBu" class="bottommargin20">
					   <form id="categoryForm">
						<div>
						<div class="error nodisplay cLabelExists"  id=cLabelExists style="margin-bottom: -10px; margin-top: 0px;padding-left: 112px;"></div><div class="clear"></div>
						 <label for="category" class="error floatright nodisplay" style="width:212px;margin-right:30px;margin-top:-2px;" generated="true"></label><br/> 
							<div class="width110 floatleft">Add category<span class="redTxt">*</span>:</div>
							<div class="floatleft leftmargin5"><input  id="category" maxlength="45" type="text" size="20" value="" name="category" style="margin-right: 5px;" /></div>
						</div>
						<div class="clear"></div>
						</form>	
						<br/>
						<div><input type="button" value="Save" class="commonBtn categoryBtn"  /><input type="button" value="Cancel" class="commonCancelBtn leftmargin15"  /></div>
					</div>
					
					<div class="error nodisplay cIdExists"  id="cIdExists" style="margin-top:-6px;margin-bottom: -18px;"></div><div class="clear"></div>
					<div class="error nodisplay categotyExists"  id="categoryExists" style="margin-top:-6px;margin-bottom: -18px;"></div><div class="clear"></div>
						<div id='configtblHead' class="configtblHead topmargin20" style="width:330px;">
							<div id='column1' class='width130'>Category</div>
							<div id='column2' class='width170'>&nbsp;</div>
						</div>
						<div id="categoryList" class="configLst" style="width:330px;">
							
						</div>
						<div class="configLstItem nodisplay" id="noDataCategory" style="text-align:center;width:330px;">
								<div style="width:100%;"> No data to display</div>
							</div>
				</div>
						<div id="manageRoles" class="topmargin30 configTbls nodisplay" style="width:360px;">
					<h3>Manage Roles</h3>
					<div id="addBu" class="bottommargin20" >
					   <form id="roleForm">
					    <div class="topmargin20">
					    <div class="error nodisplay floatLeft rLabelExists"  id="rLabelExists" style="margin-bottom: -10px; margin-top: -7px;padding-left: 115px;"></div><div class="clear"></div>
						 <label for="role" class="error floatright nodisplay" style="width:212px;margin-right:30px;margin-top:-2px;" generated="true"></label><br class="clear"/>
							<div class="width110 floatleft">Add role<span class="redTxt">*</span>:</div>
							<div class="floatleft leftmargin5"><input  id="role" type="text" size="20" value="" maxlength="45" name="role" style="margin-right: 5px;" /></div>
						</div>
						<div class="clear"></div>
						</form>	
						<br/>
						<div class=""><input type="button" value="Save" class="commonBtn roleBtn"  /><input type="button" value="Cancel" class="commonCancelBtn leftmargin15"  /></div>
					</div>
						<div class="clear"></div>				
					<div class="error nodisplay roleIdExists"  id="roleIdExists" style="margin-top: -6px;"></div><div class="clear"></div>
					<div class="error nodisplay roleExists"  id="roleExists" style="margin-top: -6px;"></div><div class="clear"></div>
					
						<div id='configtblHead' class="configtblHead" style="width:330px;">
							<div id='column1' class='width130'>Role</div>
							<div id='column2' class='width170'>&nbsp;</div>
						</div>
						<div id="roleList" class="configLst" style="width:330px;">
							
						</div>
						<div class="configLstItem nodisplay" id="noRoleData" style="text-align:center;width:330px;">
								<div style="width:100%;"> No data to display</div>
							</div>
				</div>
				
				<div id="managePrivileges" class="topmargin20 configTbls nodisplay" style="width:360px;">
					<h3>Manage Privileges</h3>
						<div  style="margin-top:15px;"></div><div class="clear"></div>
					<div id="mgRoles" class="bottommargin20">
					   <form id="rolesForm">
						<div>
							<div class="floatleft bold">Select Role: <select class="text250 conceptCategory"  name="roleId" id="roleId">
													<option value="">--Select--</option>
												</select></div>
					
						</div>
						<div class="clear"></div>
						</form>	
					</div>
					<div class="clear"></div>
						<div id='configtblHead' class="configtblHead topmargin20" >
							<div id='column1'>Previleges</div>
						</div>
						<div id="previlegesList" class="configLst" style="width:960px;">
		
						</div>
												<div  style="margin-top:15px;"></div><div class="clear"></div>
						<div><input type="button" style="padding:3px 10px;" id="updateRoles" name="updateRoles" class="commonBtn" value="Update Roles"></div>
				</div>
				
				
			</div>
			
			<div class='clear'></div>
			
		</div>
		<div id="delete_cnfm" title="Delete" class="nodisplay">
			<p class="topmargin25"><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to delete? Deletion will erase all records associated with this language (or) domain (or) category (or) role from the database.</p>
		</div>
		<div id="delete_vtcnfm" title="Delete" class="nodisplay">
			<p class="topmargin25"><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to delete? </p>
		</div>
		<div id="delete_mailcnfm" title="Delete" class="nodisplay">
			<p class="topmargin25"><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are you sure you want to delete? </p>
		</div>
		<div id="addedTemplateMessage" title="Success" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Email template successfully added.</p>
		</div>
		<div id="editTemplateMessage" title="Success" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Email template successfully updated.</p>
		</div>
		<div id="deleteTemplateMessage" title="Success" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Email template successfully deleted.</p>
		</div>
		<div id="templateAlert" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Select at least one email template.</p>
		</div>
		<div id="updateUserRoles" title="Success" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		User role(s) successfully updated.</p>
		</div>
		<div id="roleSelctMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Please select at least one role to perform Action.</p>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/json2.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/common.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Constants.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/charts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.validate.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/admin_configuration.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tinymce.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
   	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.cookie.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.treeview.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>

</body>
</html>