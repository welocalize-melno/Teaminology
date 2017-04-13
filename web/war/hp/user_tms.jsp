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
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/global_hp_new.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    <%-- <link href="<%=application.getContextPath()%>/css/global_hp.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet"> --%>

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
		<div class='contentContainer'>
			<div class="companySlctDropdwn viewComp_tm companyFilter floatleft nodisplay">
				<select size="5" name="example-basic" multiple="multiple"
					title="select company" id=mutliCompanySlct></select>
			</div>
		<div class='termAndTMSearch'>
				<div class='searchTm  bottommargin10'>
					<div class='bottommargin10'>TM search:</div>
					<div><input type='text' size='30' value='Enter term to search...' id='newTmTerm' /></div>
					<div class="smallFont topmargin10" style="margin-right: 5px;"><div class="floatleft"style="margin-top: 3px;"><input type="checkbox" id="chktmCase" name="chktmCase" /></div> <div class="topmargin5 floatleft" >Case sensitive</div></div>
					<div style="text-align: right;margin-right: 5px;"><input type='button' id='searchTm' value='Search'  /></div>
				</div>
				
			</div>
				<div class='clear'></div>
			
		  	<div class='leftContainer'>
		 
		<div class="topmargin15 " id="tmModule">
				
					<div id="manageTmTbl" class='bottommargin10 topmargin15'>
						<div id='mngTmDtlSectionHead'>
							<div id='sourceTerm' class='width170' sortOrder="ASC">Source</div>
							<div id='targetTerm' class='width170' sortOrder="ASC">Target</div>
							<div id='tmLanguage' class='width110' sortOrder="ASC">Language</div>
							<div id='tmCompany' class='width110 viewComp_tm  nodisplay' sortOrder="ASC">Company</div>
							<div id='product' class='width110' sortOrder="ASC">Product line</div>
							<div id='domain' class='width110' sortOrder="ASC">Industry domain</div>
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
	
			<div class='clear'></div>
			
		</div>
		
		

		<div id="validationMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Enter valid characters.</p>
		</div>			
	<div class='footerTopLine'></div>	
	<%@include  file="../common/footer.jsp"%>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/leaderBoard.js"></script>
<!-- 	<script type="text/javascript" src="<%=application.getContextPath()%>/js/plugin_term_details.js"></script> -->
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/user_tms.js"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js"></script>
        <script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js"></script>


    <script type="text/javascript">
        $(document).ready(function () {
            $('.dataTables_length').hide();
        });
    </script>
</body>
</html>