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
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/global_hp_new.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/language_m_select.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">


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
<body class="userTermsList">
	<div class='siteContainer'>
	<%@include file="../common/header.jsp"%>
 
 	 <%
 	 if (session.getAttribute(SessionKey.USER.getKey()) == null){
 		 session.setAttribute("lostSession","lostSession");
         response.sendRedirect("./index.jsp");
     }
  	  %>
 		<div class='contentContainer'>
		  	<div class='leftContainer'>
		  	<div style="width:100%;" class="nodisplay" id="noLanguage" > Please add one or more languages in Profile page to vote!</div>
		  	<div id="langDetails" style="border-right:1px solid #e8e8e8;">
                <div id="tabs" style="margin-left: -20px;">
                    <ul class="langMenu userLangMenu">
                    </ul>
                </div>
                   <!--  <br class='clear' /> -->
				<!-- <h1 class='bottommargin10 floatleft' id="languageLabel"></h1> -->
			<!-- <div class="rightpadding15"><span class="countTxt"><span id="totalLangTerms" class="floatright"></span><br /><span class="smallFont floatright">terms</span></span></div><br class="clear" /> -->
			
				<div class="bottommargin10 showTermsVoted" style="text-align: right;margin-right: 31px;font-size: 12px;top: 8px;right: 17px;" ><div class="votedTrms" style="position: relative; top: 22px; color: rgb(28, 148, 196); padding-right: 36px;  cursor: pointer;">Show Terms I've Voted On</div><div style="position: relative; top: -10px;  color: rgb(28, 148, 196);  cursor: pointer;" class="currentPolls nodisplay">Show current invitations</div></div>
				<div id="newInvitations" style=" margin-top: -30px;">
						<div id='termDetails' class='bottommargin10' style="border: 1px solid #0096D6;">
							<div id='trmDtlSectionHead' style="padding-left: 15px;">
								<div id='sourceTerm' class='width295 currentSourceTerm ascending' sortOrder="ASC" style="color: #a6a6a6;line-height: 30px;">Terms Being Polled <img src="/app/images/ascend.png" height="5px" width="10px" class="sort1 leftmargin5 nodisplay"/></div>
								<!-- <div id='column2' class='width170' style="padding-left: 5px;">Vote for a term</div> -->
								<div id='pollExpirationDate' class='floatleft currentExpirationDate' sortOrder="ASC" style="color: #a6a6a6;line-height: 30px;">Poll Expiration</div>
								
							</div>
							<div id='termDtlRowsList'></div>
							 <div id="pagination" style="height: 20px;text-align: right;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;">
								<div style="float:left;padding-left:10px;"><span >Viewing <span id="rangeOfList"> 0 </span> of <span id="totalTerms">0</span></span></div>
								<div style="float: right;padding-right:10px;"><span class="rightmargin5 previous"  >Previous</span> | <span class="next leftmargin5">Next</span></div>
							</div> 
					</div>
					
				</div> 	
			
			<div id="votedTerms" class="nodisplay">
						<div id='votedTermDetails' class='bottommargin10'>
							<div id='trmDtlSectionHead'>
								<div id='sourceTerm' class='width220 votedSourceTerm ascending' sortOrder="DESC" style="padding-top: 10px;">Terms Being Polled<img src="/app/images/ascend.png" height="5px" width="10px" class="sort2 leftmargin5"/></div>
								<div id='column2' class='width50' >&nbsp;</div>
								<div id='suggestedTerm' class='width209 votedSuggestedTerm'  sortOrder="ASC" style="padding-top: 10px;">Top suggestion</div>
								<div id='status' class='width40' sortOrder="ASC">&nbsp;</div>
								<div id='votingDate' class='floatleft termVotingDate '  sortOrder="ASC" style="padding-left:25px; padding-top: 10px;">Vote date</div>
							</div>
							<div id='votedRowsList'></div>
							<div id="pagination"  class="currentPagination"style="height: 20px;text-align: right;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;">
								<div style="float:left;"><span >Viewing <span id="vtdRangeOfList"> 0 </span> of <span id="vtdTotalTerms">0</span></span></div>
								<div style="float: right;"><span class="rightmargin5 previous"  >Previous</span> | <span class="next leftmargin5">Next</span></div>
							</div>
					</div>
					</div>
					
				</div>
			</div>			
			<div class='rightContainer'>
				<div class="newTrmRqst bottommargin15">
					<input type="submit" value="REQUEST NEW TERM" class="termRqstBtn" />
					
				</div>
				<div class='topmargin12' id='leaderBoard'>
                <h3 style="color: rgb(43, 166, 203);float:left;">Leader Board
                  </h3>
                  <img src="/app/images/globe.png" style="margin-left: 18px; height: 14px;position: relative;top: 4px;">
				<select class="langSelect sortByLanHide" size="5" name="example-basic" multiple="multiple"  class="text220 " title=" select language"
                    id="termlanguageSlctForRank"></select> </div>
                     <div id='leadersList' style="position: relative;"></div>
                <div class='leaderBrdFoot' style="padding-top: 12px;">
                    <span class='smallFont seeAll'>See All</span>
                </div>
          <div style="display: none;" id="allUserList" title="Leader board">
        <table cellpadding="0" cellspacing="0" border="0" class="display" id="leaderboard_seeAll" width="100%">
            <thead>
            <th width="15%">&nbsp;</th>
            <th width="25%">Username</th>
            <th width="20%">Total votes</th>
            <th width="20%">Badging</th>
            <th width="20%">Accuracy</th>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
     <div style="display: none;" id="allUserListByLang" title="Leader board">
        <table cellpadding="0" cellspacing="0" border="0" class="display" id="leaderboardByLanguage" width="100%">
            <thead>
            <th width="15%">&nbsp;</th>
            <th width="25%">Username</th>
            <th width="20%">Total votes</th>
            <th width="20%">Badging</th>
            <th width="20%">Accuracy</th>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
					<div id='leadersList'></div>
				</div>
				
				
			</div>
			<div class='clear'></div>
			
		</div>
		
		<div style="display: none;" id="allUserList" title="Leader board">
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
		<div style="display: none;" id="votiongDetailsDiv" title="User Comments">
			<table cellpadding="0" cellspacing="0" border="0" class="display" id="votiongDetailsTab" width="100%">
				<thead>
					<th class="sorting" width="20%">Username</th>
					<th width="25%">Comments</th>
					<th width="15%">Comment Date</th>
				</thead>
				<tbody>
					
				</tbody>
			</table>
		</div>
					 <div id="newTermDiv" class="nodisplay" title="Request new term">
			<form name="rqstNewTrm" id="rqstNewTrm">
					<div id="rqstNewTerm" class="topmargin20 bottompadding10">
					<div class='boxWidth320'>
						<label for="termInfo" class="error floatright nodisplay" style="width:212px;margin-right:-3px;margin-top:-16px;" generated="true"></label>
						<div class="" style="padding-bottom: 20px;"><div class="label width110 floatleft">English<span class="redTxt">*</span>:</div><div class="floatleft" style="width:200px;"><input type="text"  class=" text190"  name="termInfo" id="termInfo" tabindex="1"/></div></div><br class="clear" />
						<div class="" style="padding-bottom: 20px;"><div class="label width110 floatleft">Translation:</div><div class="floatleft" style="width:200px;"><input type="text"  class=" text190 trnsltn"  name="trnsltn" id="trnsltn" tabindex="2"/></div>  </div> <br class="clear" />
						<div class="" style="padding-bottom: 20px;"><div class="label width110 floatleft">Term Notes:</div><div class="floatleft" style="width:200px;"> <input type="text"  id="termNotes"  value="" class="text190" name="termNotes" tabindex="3" /></div> </div> <br class="clear" />
						<div class="" style="padding-bottom: 20px;"><div class="label width110 floatleft">Concept definition:</div><div class="floatleft" style="width:200px;"><input type="text" id="cncptDef"  value="" class="text190" name="cncptDef"  tabindex="4"/></div>  </div> <br class="clear" />
						<div class="" style="padding-bottom: 20px;"><div class="label width110 floatleft">Contextual example:</div><div class="floatleft" style="width:200px;"><input type="text" id="cncptExample"  value="" class="text190" name="cncptExample"  tabindex="5"/></div>  </div> <br class="clear" />
						
					</div>
					<div class='boxWidth320' style="width:335px;">
					
					<div class="floatright nodisplay languageSlctErr" style="width:215px;margin-right:10px;margin-top:-17px;"><span class="error languageSlctErr">Select at least one language</span></div>
						<div class="bottommargin20" style="padding-bottom: 16px;"><div class="label width110 floatleft">TargetLanguage<span class="redTxt">*</span>:</div>
						 <div class="floatleft">
							<select id="languageSlct" name="languageSlct" multiple="multiple"  class="text220" tabindex="6"></select> 
							
							</div>
						</div>

						<div class="" style="padding-bottom: 20px;"><div class="label width110 floatleft">Form:</div> 
						 <div class="floatleft">
							<select class="text220 termForm" name="termForm"  id="termForm" tabindex="7">
								<option value="0">--Select--</option>
							</select>
							</div>
						</div><br class="clear" />
					   		
						<div class="" style="padding-bottom: 20px;"><div class="label width110 floatleft">Category:</div> 
						 <div class="floatleft">
							<select class="text220 termCategory" name="termCategory"  id="termCategory" tabindex="8">
								<option value="0">--Select--</option>
							</select>
							</div>
						</div><br class="clear" />
						<div class="" style="padding-bottom: 20px;"><div class="label width110 floatleft">Part of speech:</div> 
						 <div class="floatleft">
							<select class="text220 termPOS" name="termPOS"  id="termPOS" tabindex="9" >
								<option value="0">--Select--</option>
							</select>
							</div>
						</div><br class="clear" />
						</div>
				
					
					<div class="clear"></div>
					
					<div style="margin-left: 235px;" class="topmargin15"><input type="button" value="Send mail" id="newTermMail" name="newTermMail"  class="rightmargin20 commonBtn padding5"  tabindex="10"/>
					<input type="button" value="Cancel" id="cancelMail"  name="cancelMail" class="cancelMailBtn  padding5" tabindex="11" style="border: 1px solid rgb(174, 174, 174); background-color: rgb(174, 174, 174); font-family: HP Simplified;"/></div>
			</div>
			</form>	
		</div>
			
			
			<div id="showTermPicBrwse" title="show picture" class="nodisplay">
		<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>              
		<form id="showPicForm" name="showPicForm"  target="uploadFrame" method="post"  >
				<div id=showTermImage style="width:550px; border:1px solid #cccccc;height: 350px;margin-top: 20px;margin-left: 30px;"><img width="550px" height="350px"  src=""  /></div>
				
				</form>
		</div>
		<div id="userOption" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg">Please select one option.</span></p>
		</div>
		 <div id="trslTerm" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg">Please enter your translation.</span></p>
		</div>
		<div id="userComments" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg">Only 500 characters are allowed in comments.</span></p>
		</div>
		<div id="pollExpireAlert" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg"> Congratulations You Got New Rank.</span></p>
		</div>
		<div id="mail_success" title="Success" class="nodisplay">
			<p class="topmargin15"><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
		<span id="logmsg">Mail will be send requesting for new term.</span></p>
		</div>
				
		<div class='footerTopLine'></div>
		<div id="loading">
		 	
		 </div>
	<div style="width:1100px;margin:0 auto;">
		<%@include  file="../common/footer.jsp"%>
	</div>
	</div>
	<script type="text/javascript">
//		var user = {
//						name: ,
//						role: 
//				}
	</script>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Constants.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/charts.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/qtip.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/leaderBoard.js"></script>
<!-- 	<script type="text/javascript" src="<%=application.getContextPath()%>/js/plugin_term_details.js"></script> -->
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/user_termlist.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/leaderBoard.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $('.dataTables_length').hide();
            //$("#tabs").tabs();
        });
    </script>
</body>
</html>