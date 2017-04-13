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
<title>Welocalize Terminology Portal</title>
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/global.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">

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
	<%@include file="./common/header.jsp"%>
	<%
	if (session.getAttribute(SessionKey.USER.getKey()) == null) {
		 response.sendRedirect("./index.jsp");
	}
%>
		<div class='contentContainer'>

			<div class="companySlctDropdwn viewComp_term companyFilter floatleft nodisplay">
				<select size="5" name="example-basic" multiple="multiple"
					title="select company" id=mutliCompanySlct></select>
			</div>
			<div class='termAndTMSearch'>
				<div class='invitePpl bottommargin10'>
					<div class='bottommargin10'>Term search:</div>
					<div>
						<input type='text' size='30' value='Enter term to search...'
							id='newTerm' />
					</div>
					<div class="smallFont topmargin10" style="margin-right: 5px;">
						<div class="floatleft" style="margin-top: 3px;">
							<input type="checkbox" id="chkCase" name="chkCase" />
						</div>
						<div class="topmargin5 floatleft">Case sensitive</div>
					</div>
					<div style="text-align: right; margin-right: 5px;">
						<input type='button' id='searchTerm' value='Search' />
					</div>
				</div>

			</div>

			<div class='clear'></div>

			<div class='leftContainer'>

				<div class="topmargin15" id="termsModule">
					<div id="manageTermTbl" class='bottommargin10 topmargin15'>
						<div id='mngTrmDtlSectionHead'>
							<div id='targetTerm' class='width150' sortOrder="ASC">Terms
								being polled</div>
							<div id='column2' class='width40'>&nbsp;</div>
							<div id='suggestedTerm' class='width150' sortOrder="ASC">Top
								suggestion</div>
							<div id='language' class='width110' sortOrder="ASC">Language</div>
							<div id='company ' class='width110 viewComp_term nodisplay'
								sortOrder="ASC">Company</div>
							<div id='pollExpirationDate' class='width110' sortOrder="ASC">Poll
								expiration</div>
							<div id='POS' class='width110' sortOrder="ASC">Part of
								speech</div>
							<div id='category' class='width110' sortOrder="ASC">Category</div>
						</div>
						<div id='termDtlRowsList'></div>
						<div id="pagination"
							style="height: 20px; font-size: 11px; border-bottom: 1px solid #dddddd; marign-top: 0; padding-top: 5px; font-weight: normal;">
							<div style="color: #999999; float: left">
								Viewing <span id="rangeOfTerms">0 </span> of <span
									id="totalPolledTerms">0</span>
							</div>
							<div style="float: right;">
								<span class="rightmargin5 previous">Previous</span> | <span
									class="leftmargin5 next">Next</span>
							</div>
						</div>
					</div>

				</div>

			</div>


			<div class='clear'></div>

		</div>



		<div id="validationMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Enter valid characters.</p>
		</div>			
		<div class='footerTopLine'></div>
	<%@include  file="./common/footer.jsp"%>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Constants.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/charts.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/qtip.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/leaderBoard.js"></script>
<!-- 	<script type="text/javascript" src="<%=application.getContextPath()%>/js/plugin_term_details.js"></script> -->
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/user_terms.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js"></script>
        <script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js"></script>
    
    
   <script type="text/javascript">

$(document).ready(function() {
	

    $('.dataTables_length').hide();
} );

</script>
</body>
</html>