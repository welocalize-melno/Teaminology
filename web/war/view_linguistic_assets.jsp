<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import= "com.teaminology.hp.service.enums.TeaminologyPage,
                                  com.teaminology.hp.service.enums.SessionKey,
                                  com.teaminology.hp.bo.lookup.Company"%>

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
		<div class='contentContainer'>
		<h1 class='bottommargin10'>View linguistic assets</h1>
			<div class='leftContainer' >
					
		     <div id="tabs" class="bottommargin15">
						<ul>
							<li class="tab1"><a href="#tabs-1">Highlights</a></li>
							<li class="tab2"><a href="#tabs-2">View TMs</a></li>
						</ul>
					
					
						<div id="tabs-1" >
						
						</div>
						<div id="tabs-2" >
						
						</div>
					</div>
					
			</div>
			<div class='clear'></div>
			<div class="topmargin15" id="termsModule">
			<div id="manageTermTbl" class='bottommargin10 topmargin15'>
					<div id='mngTrmDtlSectionHead'>
					        <div id='targetTerm' class='width200' sortOrder="ASC">Terms being polled</div>
							<div id='column2' class='width40' >&nbsp;</div>
							<div id='suggestedTerm' class='width200' sortOrder="ASC">Top suggestion</div>
							<div id='language' class='width110' sortOrder="ASC">Language</div>
							<div id='pollExpirationDate' class='width110' sortOrder="ASC">Poll expiration</div>
							<div id='POS' class='width110' sortOrder="ASC">Part of speech</div>
							<div id='category' class='width130' sortOrder="ASC">Category</div>
						</div>
						<div id='termDtlRowsList' >
							
						</div>
						<div id="pagination" style="height: 20px;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;">
							<div style="color: #999999; float:left">Viewing <span id="rangeOfTerms">0 </span> of <span id="totalPolledTerms">0</span></div>
							<div style="float: right;"><span class="rightmargin5 previous" >Previous</span> | <span class="leftmargin5 next">Next</span></div>
						</div>
					</div>
					
				</div>
				
					<div class="topmargin15 nodisplay" id="tmModule">
				
					<div id="manageTmTbl" class='bottommargin10 topmargin15'>
						<div id='mngTmDtlSectionHead'>
							<div id='sourceTerm' class='width220' sortOrder="ASC">Source</div>
							<div id='targetTerm' class='width220' sortOrder="ASC">Target</div>
							<div id='tmLanguage' class='width110' sortOrder="ASC">Language</div>
							<div id='product' class='width130' sortOrder="ASC">Product line</div>
							<div id='domain' class='width130' sortOrder="ASC">Industry domain</div>
							<div id='content' class='width90' sortOrder="ASC">Content type</div>
							
						</div>
						<div id='tmDtlRowsList' >
							
						</div>
						<div id="paginationTm" style="height: 20px;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;">
							<div style="color: #999999; float:left">Viewing <span id="rangeOfTms">0 </span> of <span id="totalPolledTms">0</span></div>
							<div style="float: right;"><span class="rightmargin5 tmPrevious" >Previous</span> | <span class="leftmargin5 tmNext">Next</span></div>
						</div>
					</div>
					
				</div>
			
			</div>			
	
		
					
		<div class='footerTopLine'></div>
		<div id="loading">
		 	
		 </div>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/charts.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/qtip.js"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/leaderBoard.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/view_linguistic_assets.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $('.dataTables_length').hide();
        });
    </script>
</body>
</html>