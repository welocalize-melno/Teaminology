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
<title>Welocalize Terminology Portal</title>
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/global.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">

</head>
<body class="adminOvr">
	<div class='siteContainer'>
	<%@include file="./common/header.jsp"%>
    <%
        List<SubMenu> subMenus = (List<SubMenu>) session.getAttribute(SessionKey.SUB_MENU.getKey());
        boolean pageFlag = false;
        if (subMenus != null) {
            for (SubMenu subMenu : subMenus) {
                if ("admin_overview.jsp".equalsIgnoreCase(subMenu.getPageUrl())) {
                    pageFlag = true;
                    break;
                }
            }
        }
        if (!pageFlag)
            response.sendRedirect("./index.jsp");
    %>
		<div class='contentContainer'>
			<h1 class='bottommargin10'>Glossary overview</h1>
			<div class='leftContainer'>
				<div class='reportContainer' style='margin-bottom:15px;'>
					<div class='reportHeader'>
						<div class='left'>
							<div class="headerTxt leftpadding25"><div id="totalTerms"></div>
								<div class='subCaption'>terms in glossary</div>
							</div>
						</div>
						<div class='right'>
							<div class="headerTxt"><div id="totalDebatedTrms"> </div>
								 <div class='subCaption'>terms being debated</div>
							</div>
							<div class='openClose'></div>
						</div>
					</div>
					<div id='reports'>
						<div class='rightmargin20 leftmargin40'>
							<div id="chartContainer" class="chartContainer">FusionCharts will load here!</div>          
						</div>
						<div class='leftmargin20'>
							<div id="chartContainer1" class="chartContainer">FusionCharts will load here!</div>          
						</div>
						<br class='clear' />
						
					</div>
					<div class="smallFont padding5" style="text-align:right;" id="viewRprt"></div>
				</div>
				
				<div>
					<h2 class='rightmargin20'>Expired polls</h2><span class='smallFont'>There are <span class="moreNum"> </span> expired polls to decide on. <span class='expiredPolls'></span></span>
					<div class="topmargin10 langSlctDropdwn bottommargin10">
						<select size="5" name="example-basic" multiple="multiple" title="select language" id="languageSlct"></select>
					</div>
						<div class="topmargin10 companyTermsSlctDropdwn companyFilter nodisplay bottommargin10 ">
									<select size="5" name="example-basic" multiple="multiple" title="select company" id="companyExpiredTermsSlct"></select>
								</div>
					
				
					<div id='termDetails' class='bottommargin10'>
						<div id='trmDtlSectionHead'>
							<div id='sourceTerm' class='width150' sortOrder="ASC">Terms being polled</div>
							<div id='column2' class='width40' >&nbsp;</div>
							<div id='suggestedTerm' class='width200' sortOrder="ASC">Top suggestion</div>
							<div id='language' class='width110' sortOrder="ASC">Language</div>
							<div id='pollExpirationDate' class="width105" sortOrder="ASC">Poll expiration</div>
						</div>
						<div id='termDtlRowsList'></div>
                    <div id="pagination" class="pageination">
                        <div class="floatleft"><span>Viewing <span id="rangeOfTerms_admin"> 0</span> of <span
                                id="totalTerms_admin">0</span></span></div>
                        <div class="floatright"><span class="rightmargin5 previous">Previous</span> | <span
                                class="next leftmargin5">Next</span></div>
                    </div>
					</div>
				</div>
			</div>
			<div class='rightContainer adminRtcontainer'>
				<div class='invitePpl bottommargin10' >
					<div class='bottommargin10'>Invite people to the community</div>
					<div><input type='text' class="width233"  value='Enter email addresses...' id='inviteEmail' /></div>
					<div class="topmargin10 alignCenter"> <input type='button' id='invitePplBtn' value='Invite' /></div>
				</div>
				<div id='leaderBoard'>	
					<h3>Leader board</h3>
					<div id='leadersList'></div>
					<div class='leaderBrdFoot'>
						<span class='smallFont seeAll'>See All</span>
					</div>
				</div>
				
				<div id='terminologyTeam' class="topmargin15">
					<h3>Community</h3>
					<div id='hpTeamList'></div>
					<div class='teamListFoot'>
						<span class='smallFont seeAllHpTeam seeAll'>See All</span>
					</div>
				</div>
				
				
			</div>
			<div class='clear'></div>
			
		</div>
		
		<div style="display: none;" id="allUserList" title="Leader Board">
			<table cellpadding="0" cellspacing="0" border="0" class="display" id="leaderboard_seeAll" width="100%">
				<thead>
					<th width="15%">&nbsp;</th>
					<th width="25%">User name</th>
					<th width="20%">Total votes</th>
					<th width="20%">Badging</th>
					<th width="20%">Accuracy</th>
				</thead>
				<tbody>
					
				</tbody>
			</table>
		</div>
		 <div id="hpCommunityList" class="nodisplay" title="Apple's Community">
			<table cellpadding="0" cellspacing="0" border="0" class="display" id="hpCommunity_seeAll" width="100%">
				<thead>
					<th>&nbsp;</th>
					<th>User name</th>
					<th>Total votes</th>
					<th>Languages</th>
				</thead>
				<tbody>
					
				</tbody>
			</table>
		</div>
		<div id="InvaliduploadPic" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg">Invalid File.</span></p>
		</div>
		<div id="mail_success" title="Success" class="nodisplay">
			<p class="topmargin15" style="margin-top:55px; margin-left:55px"><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
		Invitation will be sent to <span id="newUserEmail"></span>.</p>
		</div>
		<div id="mail_failed" title="Failure" class="nodisplay">
			<p class="topmargin15"><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
			Failed to send invitation to <span id="failedUserEmail"></span>.</p>
		</div>
		
		
		 <div id="dateAlet" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Enter valid date.</p>
		</div>
		 <div id="mailMessage" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Enter a  valid email address.</p>
		</div>
		<div class='footerTopLine'></div>
		<div id="loading">
		 	
		 </div>
		 		<div id="invitation"  title="Invite people to the community" class="nodisplay" style="padding-left:15px;">
		
			<div class="nodisplay redTxt leftmargin100" id="templateReq" style="padding-left:87px;margin-bottom: -15px;">Select at least one template </div>
			<div class="topmargin15">
				<p class="bold">Email template<span class="redTxt">*</span>  
				<select class="text190 emailTemplate">
					<option value="">--Select template--</option>
					
				</select>
				</p>
				
				
			</div>
			<div class="clear"></div>
			<div class="topmargin15 emailPrvw" style="height:180px;width:500px; border:1px solid #888888;overflow: auto;">
				
			</div>
			<div class="clear"></div>
			<div class="sendMail topmargin15" style="text-align: center;">
				<input type="button" value="Send mail" id="inviteMail" class="rightmargin15 commonBtn padding5" />
				<input type="button" value="Cancel" id="cancelInvitMail" class="commonBtn padding5" />
			</div>
			<div style="display: none;" id="votiongDetailsDiv" title="Voting details">
			<table cellpadding="0" cellspacing="0" border="0" class="display" id="votiongDetailsTab" width="100%">
				<thead>
					<th width="20%">Invited users</th>
					<th width="20%">Voting status</th>
					<th width="20%">Voted translation</th>
					<th width="25%">Comments</th>
					<th width="15%">Vote date</th>
				</thead>
				<tbody>
					
				</tbody>
			</table>
		</div>
			
		</div>
		
		
			<div id="uploadPicBrwse" title="Upload picture" class="nodisplay">
		<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>              
		<form id="uploadPicForm" name="uploadPicForm"  target="uploadFrame" method="post" action="<%=application.getContextPath()%>/teaminology_ctrler/teaminology/uploadProfileImage?t=term"  ENCTYPE="multipart/form-data" >
				<p class="bold topmargin15"> Browse picture:</p>
				<p class="topmargin15"><input type="file" size="40" name="uploadPic" id="uploadPic"/></p>
				<p class="error nodisplay topmargin10" id="errorMsg"></p>
				<p class="topmargin15"><input type="button" value="Upload" id="uploadTermPic" class="commonBtn toppadding5" /> </p>
				
				</form>
		</div>
			
		<div id="showTermPicBrwse" title="show picture" class="nodisplay">
		<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>              
		<form id="showPicForm" name="showPicForm"  target="uploadFrame" method="post"  >
				<div id=showTermImage style="width:550px; border:1px solid #cccccc;height: 350px;margin-top: 20px;margin-left: 30px;"><img width="550px" height="350px"  src=""  /></div>
				
				</form>
		</div>
		
		<div id="uploadTerm" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg">Please select the image to upload.</span></p>
		</div>
	<%@include  file="./common/footer.jsp"%>
	</div>


	<script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.dataTables.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tagsphere.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.qtip.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-ui-1.8.18.custom.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/prototype.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.multiselect.filter.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.multiselect.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/json2.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/common.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Constants.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/charts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/qtip.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/leaderBoard.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/plugin_term_details.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/admin_overview.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>

</body>
</html>