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
<link href="<%=application.getContextPath()%>/css/demo_table.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/global.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
</head>
<body>
	<div class='siteContainer'>
	<%@include file="./common/header.jsp"%>
        <%
            List<SubMenu> subMenus = (List<SubMenu>) session.getAttribute(SessionKey.SUB_MENU.getKey());
            boolean pageFlag = false;
            if (subMenus != null) {
                for (SubMenu subMenu : subMenus) {
                    if ("admin_reports.jsp".equalsIgnoreCase(subMenu.getPageUrl())) {
                        pageFlag = true;
                        break;
                    }
                }
            }
            if (!pageFlag)
                response.sendRedirect("./index.jsp");
        %>
		<div class='contentContainer'  style="padding-left: 35px;">
			<h1>Reports</h1>
			<div style="text-align: right; padding-right: 112px;" class="bottommargin10">
			<input type="submit" value="Export voting results" class="commonBtn" name="exprtRprt" id="exprtVoting" style="padding:3px 10px;" />
			<input type="submit" value="Export list" class="commonBtn" name="exprtRprt" id="exprtRprt" style="padding:3px 10px;" /> </div>
			<div class="momChart" style="border:1px solid #dddddd;height:315px; width:950px;margin-bottom: 15px;padding-left: 1px; background-color: #F1FFEA;">
				<div id="multiChartContainer"></div>
				<div class="smallFont languageDataExportTxt" style="text-align:right;margin-right: 5px;margin-bottom: 5px;"><a href="<%=application.getContextPath()%>/impExp_serv?c=exportCSV&exportType=languageReport" id="languageDataExport"class="multiRprt">Export report</a></div>
			</div>  
			
				<div class='boxWidth235' id="trmsInGlossary" style='border:1px solid #dddddd;padding-left: 5px;'>
					<div class="headerTxt" id="totalTerms"></div><div class='clear'></div>
					<div class="chrtSubHdr">terms in glossary</div>
					<!-- >div class="loadingDiv" style="text-align: center;"><img src="<%=application.getContextPath()%>/images/loading.gif" alt=" "  /></div -->
					<div id="chartContainer">FusionCharts will load here!</div>       
					<div class="smallFont" style="text-align:right;margin-right: 5px;margin-bottom: 5px;"><a href="<%=application.getContextPath()%>/impExp_serv?c=exportCSV&exportType=termGlosary" id="termGlosaryExport" class="multiRprt">Export report</a></div>   
				</div>
				<div class='boxWidth235 leftmargin10' id="debatedTrms" style='border:1px solid #dddddd;padding-left: 5px;'>
					<div class="headerTxt" id="totalDebatedTrms"></div><div class='clear'></div>
					<div class="chrtSubHdr" >terms being debated</div>
					<!-- >div class="loadingDiv" style="text-align: center;"><img src="<%=application.getContextPath()%>/images/loading.gif" alt=" " /></div-->
					<div id="chartContainer1">FusionCharts will load here!</div>      
					<div class="smallFont" style="text-align:right;margin-right: 5px;margin-bottom: 5px;"><a href="<%=application.getContextPath()%>/impExp_serv?c=exportCSV&exportType=term" id="termExport" class="multiRprt">Export report</a></div>    
				</div>
				<div class='boxWidth385 leftmargin10' id="langTrms" style='border:1px solid #dddddd;padding-left: 5px;'>
					<div id="pieChartContainer"></div>       
					<div class="smallFont" style="text-align:right;margin-right: 5px;margin-bottom: 5px;"><a href="<%=application.getContextPath()%>/impExp_serv?c=exportCSV&exportType=languageDistribution" class="multiRprt">Export report</a></div> 
				</div>
            <div class='clear'></div>

				<div class='boxWidth235 topmargin20' id="tmsInGlossary" style='border:1px solid #dddddd;padding-left: 5px;'>
					<div class="headerTxt" id="totalTmTerms"></div><div class='clear'></div>
					<div class="chrtSubHdr">TM units</div>
					<!-- >div class="loadingDiv" style="text-align: center;"><img src="<%=application.getContextPath()%>/images/loading.gif" alt=" "  /></div -->
					<div id="chartContainerTm">FusionCharts will load here!</div>       
					<div class="smallFont" style="text-align:right;margin-right: 5px;margin-bottom: 5px;"><a href="<%=application.getContextPath()%>/impExp_serv?c=exportCSV&exportType=tms" id="tmGlosaryExport" class="multiRprt">Export report</a></div>   
				</div>
				
	<!--  Export the Term History excel code	
	 <div class='topmargin20' style="padding-left: 24%; width: 30%">
                <div>Term History</div>
                 <div class="topmargin10"><p class="bold">Filter by: <select class="exprtCatSlct text178" name="exportBy"></select></p>
                  <div class="smallFont" style="float: right;padding-right: 20px;margin-top: -21px;">
                    <iframe id="downloadFrame" name="downloadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe> 
                    <form id="downloadForm" name="downloadForm"  target="downloadFrame" method="post">
                    <div id="termHistoryExport" class="multiRprt" style="cursor: pointer; color: #2ba6cb;" >Export report</div>
                    <div id="waiting"></div> 
                   </form>
                 </div>
                 </div>
                 <div class="topmargin15 nodisplay" id="category_list">
                   <p class="bold">Select from available options:<br />
                      <select class="exprtCatList text220 topmargin10" multiple size="5" name="selectedIds"></select>
                   </p>
                 </div>
                 <div id="termHistoryExpChkMsg" title="Alert" class="nodisplay">
                  <p class="topmargin30 alignCenter"><span class="termMsg"></span>
                   Please select at least one option available.</p>
                 </div>
                </div>
                
                --> 
                
				<div class='clear'></div>
				<div class="topmargin20">
					<div id="reportsTbl" class='bottommargin10 topmargin15' style="padding-right: 163px;">
						<div id='reportsHead' class="tblHead">
							<div id='column1' class='width252' prop="languageLabel">Language team</div>
							<div id='column2' class='width90' prop="members">Members</div>
							<div id='column3' class='width90' prop="accuracy">Accuracy</div>
							<div id='column4' class='width90' prop="totalTerms">Terms</div>
							<div id='column5' class='width100' prop="debatedTerms">Debated terms</div>
							<div id='column5' class='width90' prop="activePolls">Active polls</div>
							<div id='column5' class='width90' prop="monthlyAvg">Monthly avg</div>
							<div id='column5' class='width80' prop="totalVotes">Total votes</div>
					    </div>
						<div id='teamRowsList' class="tblData">
						</div>
						
					</div>
				
					
				</div>
			
			<div class='clear'></div>
			
		</div>
		<div id="exportRprtFilter" title="Export reports" class="nodisplay">
			<p class="bold topmargin15">Select Report:</p>
			<div class="topmargin15 floatleft">
				<select id="reportSlct" name="reportSlct" multiple="multiple"  class="text220" size="6">
					<option value="Month to Month">Monthly activity</option>
					<option value="Distribution">Distribution</option>
					<option value="Terms Debated" >Terms debated</option>
					<option value="Terms in Glossary">Terms in glossary</option>	
					<option value="Overview">Overview</option>
					<option value="Tms in Glossary">Tms in glossary</option>
					<option value="All Reports">All reports</option>
				</select>
			</div>
			<div class="topmargin15 floatleft leftmargin15"><select id="languageSlct" name="languageSlct" multiple="multiple"  class="text220"></select></div>
			<!-- <div class="topmargin15 floatleft leftmargin15"><select id="companySlct" name="companySlct" multiple="multiple"  class="text220"></select></div> -->
			
			<div class="clear"></div>
			<div class="topmargin20"><input type="button" value="Get export" id="getReports" class="commonBtn" /> </div>
		</div>
		<iframe id="uploadFrameID" name="downloadAllframe" height="0" width="0" frameborder="0" scrolling="yes"></iframe>              
		<form id="downloadAll" name="downloadAll"  target="downloadAllframe" method="post"  >
		</form>	
		<div id="reportType" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg">Please select at least one reportType.</span></p>
		</div>
		<div class='footerTopLine'></div>
	<%@include  file="./common/footer.jsp"%>
	</div>
	
	<div id="exportVotingFilter" title="Export Voting Results" class="nodisplay"> </br>
	From Date: <input type="text" id="fromDate"/>   &nbsp To Date: <input type="text" id="toDate"/></br></br>
	<input type="checkbox" id="topSugCandidate" /> Top Suggestion candidate</br>
	<input type="checkbox" id="alterCandidate"/> Alternate candidate</br>
	<input type="checkbox" id="noofvotes"/> Number of votes per candidate</br></br>
	<input type="submit" Value="Export" id="exportVotingResults" />
	</div>
	
    <div id="termHistoryFilter" title="Term History Results" class="nodisplay"> </br>
    From Date: <input type="text" id="fromDateHistory"/>   &nbsp To Date: <input type="text" id="toDateHistory"/></br></br>
    <div style="text-align: center;">
    <input type="submit" class="commonBtn" Value="Export History" id="termHistoryResults" />
    </div>
    </div>
    
	<script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tagsphere.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.qtip.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.dataTables.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-ui-1.8.18.custom.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.multiselect.filter.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.multiselect.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/json2.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/common.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Constants.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/charts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/qtip.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/admin_reports.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	
	
	
       

</body>
</html>