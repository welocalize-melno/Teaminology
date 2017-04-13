<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import= "com.teaminology.hp.service.enums.TeaminologyPage,
                                  com.teaminology.hp.service.enums.SessionKey,
                                  com.teaminology.hp.service.enums.MenuEnum,
                                  com.teaminology.hp.bo.lookup.Company,
                                  com.teaminology.hp.bo.Menu,
                                  com.teaminology.hp.bo.User,
                                  java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Newell Terminology Community</title>
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/global_hp_new.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    
</head>
<body class="searchTermsList">
	<div class='siteContainer'>
	<%@include file="../common/header.jsp"%>
	<%
	if (session.getAttribute(SessionKey.USER.getKey()) == null) {
		 response.sendRedirect("./index.jsp");
		 return;
	}
	User user = (User) session.getAttribute(SessionKey.USER.getKey());
    int user_id = user.getUserId(); 
%>
				<div class="contentContainer">
				<div>
				 <% if(user_id == 2 || user_id == 1)  {%>  
				<div class='replaceSearch' style="width: 300px; display: inline-block;">
				<% } else { %> 
				<div style="display: inline-block;">
				<%  } %>
				<h3 class=' bottommargin10 '>Search TM and Termbase</h3>
                 <input id="searchField" type="text" />       
         <% if(user_id == 2 || user_id == 1)  {%>  
              
                <div ><input type="button" value="Search" class="commonBtn searchBtn" name="submitBtn" style="margin-top: 10px; margin-bottom: -26px;"/></div>   
                </div>              
                <div style="width: 417px; display: inline-block;padding-left :11px">
                <h3 style="margin-bottom :12px">Replace</h3>    
                 <input id="replaceTermVal" type="text" style="width: 212px;padding-bottom :2px;height: 16px"/>        
                    <div class="floatright" style="width:200px; margin-right: -10px;">
                        <select id="replaceSearchOption"  class="text190" name="search"  tabindex="1" style="border: 1px solid #dddddd;height: 22px">
                                 <option value="1">Termbase</option>
                                 <option value="2">TM</option>
                                 <option value="3">Both</option>
                              </select>  </div> 
                                <span><input type="button" class="commonBtn" value="REPLACE" name="replaceSubmitBtn" id="replaceTerm" style="margin-top: 10px;"/></span>
               </div> <% } else { %> 
                   <span style="padding-left:316px;"><input type="button" value="Search" class="commonBtn searchBtn" name="submitBtn"/></span> 
              <%  } %>
				</div>
				
                  
         <div id="searchText"  >
		 	<form id="searchForm">
				<div class="regFrm topmargin30">
					<div class='width295'>
						
						<div class="" style="padding-bottom: 20px;"><div class=" width50 floatleft">Search:</div>
						<div class="floatleft" style="width:200px;">
						<select id="searchOption"  class="text190" name="search"  tabindex="1">
						         <option value="0">--select--</option>
						         <option value="1">TM</option>
						         <option value="2">Termbase</option>
						         <option value="3">Both</option>
						      </select>  </div></div><br class="clear" />
						      
						      	<div class="searchType" style="padding-bottom: 20px;"><div class=" width50 floatleft">Type:</div>
						<div class="floatleft " style="width:200px;">
						<select id="searchType" class="text190" name="type" tabindex="2" >
						      <option value="0">--select--</option>
						      <option value="2">Fuzzy</option>
						           <option value="1">Exact</option>
						       </select> </div></div><br class="clear" />
						
						
					</div>
					<div class='boxWidth320'>
					
						<div class="bottommargin20" style="padding-bottom: 20px;"><div class=" width90 floatleft">Language:</div>
						 <div class="floatleft">
							<select id="languageSlct" name="languageSlct" multiple="multiple"  class="text220" tabindex="9"></select> 
							
							</div>
						</div>
						<div class="companySlctDropdwn searchComp_filter bottommargin20 nodisplay">
							<div class="width90 floatleft" style="font-size:12px;">Company:</div>
				       		<select size="5" name="example-basic" multiple="multiple"
					             title="select company" id="mutliCompanySlct"></select>
			            </div>
						 <div class="bottommargin20 caseType nodisplay">
						 	<div class=" width90">Case:</div>
			    		 	<select id="searchCase"  class="text190" name="case" tabindex="4">
						           <option value="0">--select--</option>
						           <option value="1">Sensitive</option>
						           <option value="2">Insensitive</option>
						           </select>	
						</div>
					</div>
				
					
					<div class="clear"></div>
					

				</div>
			</form>	
		</div>
	<!-- 	 <span id="selectReplaceChkBox"><input id="selectAll" style="padding-left: 10px" type="checkbox"/><span
                    style="font-size: 13px; font-weight: bold; "> Selecte Terms to replace</span></span>  -->
              	<div id="rqstChangeDiv" class="nodisplay" title="Request change">
			<div id="requestChange" class="topmargin15 bottompadding10">
			<form name="rqstChange" id="rqstChange">
			<div >
			      <label for="source" class="error floatright nodisplay" style="width:195px;margin-right:60px;margin-top:-14px;" generated="true"></label><div class="clear"></div>
				   <div class="" style="padding-bottom: 20px;"><div class="label width140 floatleft">Source<span class="redTxt">*</span>:</div> <div class="floatleft" style="width:200px;"><input type="text" id="source" size="25" value="" class="text190" name="source"/> </div></div><br class="clear" />
				   <label for="target" class="error floatright nodisplay" style="width:195px;margin-right:60px;margin-top:-14px;" generated="true"></label><div class="clear"></div>
					<div class="" style="padding-bottom: 20px;"><div class="label width140 floatleft">Target<span class="redTxt">*</span>:</div> <div class="floatleft" style="width:200px;"><input type="text" id="target" size="25" value="" class="text190" name="target"/> </div></div><br class="clear" />
					<label for="suggTarget" class="error floatright nodisplay" style="width:195px;margin-right:60px;margin-top:-14px;" generated="true"></label><div class="clear"></div>
					<div class="" style="padding-bottom: 20px;"><div class="label width140 floatleft">Suggested New Target<span class="redTxt">*</span>:</div> <div class="floatleft" style="width:200px;"><input type="text" id="suggTarget" size="25" value="" class="text190" name="suggTarget"/> </div></div><br class="clear" />
					<div class="" style="padding-bottom: 20px;"><div class="label width140 floatleft">Notes:</div> <div class="floatleft" style="width:200px;"><input type="text" id="notes" size="25" value="" class="text190" name="notes"/> </div></div><br class="clear" />
								
				<div class="sendRequestMail topmargin10" style="padding-left:154px;">
					<input type="button" value="Send mail" id="requestChangeMail" name="requestChangeMail"  class="rightmargin20 commonBtn padding5" />
					<input type="button" value="Cancel" id="cancelRequestMail"  name="cancelRequestMail" class="cancelMailBtn  padding5"  style="border: 1px solid rgb(174, 174, 174); background-color: rgb(174, 174, 174); font-family: HP Simplified;"/>
				</div>
					</div>
				</form>
			</div>
		</div>
		       
                  <div class="topmargin10 nodisplay" id="termsModule" style="padding-right:10px;">
					<div class="newTrmRqst requestchange  nodisplay" style="padding-left:804px;">
					<input type="button" value="Request change " class="changeRqstBtn requestchange nodisplay" />
				</div>
				
					<div id="manageSearchTermTbl" class='bottommargin10 topmargin15 nodisplay ' >
				<div style="background-color: #F1F1F1; border-bottom: 1px solid #E1DCD6;border-top: 1px solid #E1DCD6;color: #404040; font-size: 16px; min-height: 20px; padding:0 0 0 4px; vertical-align: middle">Termbase Search</div>
				
					<div  id="range" style="border-bottom: 1px solid ;border-top: 1px solid ;color: #404040; font-size: 12px; min-height: 20px; padding: 0 0 0 330px; vertical-align: middle;background-color:#F1F1F1;"><b>Termbase matches(<span id="totalPolledTerms">0</span>)- page <span id="rangeOfTerms">0/0 </span></b></div>

						<div id='mngTrmDtlSectionHead'>
						<!--   <div id='column0' class='width20'>&nbsp;</div>-->	 
							<div id='targetTerm' style="margin-left : 50px" class='width110' sortOrder="ASC">Source</div>
							<div id='column2' class='width40' >&nbsp;</div>
							<div id='suggestedTerm' class='width110' sortOrder="ASC">Target</div>
							<div id='LanguageTrm' class='width90' sortOrder="ASC">Language</div>
							<div id='company' class='width90 viewComp_search nodisplay' sortOrder="ASC" >Company</div>
							<div id='status' class='width40' sortOrder="ASC">Final</div>
							<div id='pollExpirationDate' class='width90' sortOrder="ASC">Poll expiration</div>
							<div id='POS' class='width90' sortOrder="ASC">Part of speech</div>
							<div id='category' class='width110' sortOrder="ASC">Category</div>
							<div id='domain' class='width90' sortOrder="ASC">Domain</div>
     						<div id='termEdit' class='width40 editTermDetails nodisplay'>Edit</div>
							
						</div>
						<div id='termDtlRowsList' >
							
						</div>
						<div id="pagination" style="height: 20px;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;width:925px;">

							<div style="float: right;"><span class="rightmargin5 previous" >Previous</span> | <span class="leftmargin5 next">Next</span></div>
						</div>
					</div>
					
				</div>
				
				
				      <div class="topmargin20 nodisplay" id="tmModule" style="padding-right:10px;">
					
					<div id="manageTMTermTbl" class='bottommargin10 topmargin15 nodisplay' >
					<div style="background-color: #F1F1F1; border-bottom: 1px solid #E1DCD6;border-top: 1px solid #E1DCD6;color: #404040; font-size: 16px; min-height: 20px; padding:0 0 0 4px; vertical-align: middle"> Translation Search</div>
				
					<div  id="rangetm" style="border-bottom: 1px solid ;border-top: 1px solid ;color: #404040; font-size: 12px; min-height: 20px; padding:0 0 0 330px; vertical-align: middle;background-color:#F1F1F1;"><b>TM matches(<span id="totalTmPolledTerms">0</span>)- Page <span id="rangeOfTmTerms">0/0 </span></b></div>

						<div id='tmDtlSectionHead'>
							
							<div id='targetTmTerm' class='width170' sortOrder="ASC">Source</div>
							<div id='suggestedTmTerm' class='width150' sortOrder="ASC">Target</div>
							<div id='LanguageTM' class='width120' sortOrder="ASC">Language</div>
							<div id='tmCompany' class='width110 viewComp_search nodisplay' sortOrder="ASC">Company</div>
							<div id='product' class='width110' sortOrder="ASC">Product line</div>
							<div id='domain' class='width110' sortOrder="ASC">Industry domain</div>
     						<div id='content' class='width90' sortOrder="ASC">Content type</div>
							<div id='tmEdit' class='width40 editTermDetails nodisplay '>Edit</div>
						</div>
						<div id='tmDtlRowsList' >
							
						</div>
					 	<div id="tmPagination" style="height: 20px;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;width:925px;">

							<div style="float: right;"><span class="rightmargin5 tmPrevious" >Previous</span> | <span class="leftmargin5 tmNext">Next</span></div>
						</div>
					</div>
					
				</div>
           <div class="clear"></div>  
     
	
				
	<div id="termExtendMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		This term is not being debated.</p>
		</div>
		<div id="searchMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Enter search term.</p>
		
		</div>
		<div id="replaceSearchMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Enter replace term.</p>
		
		</div>
		<div id="noRplaceTrmMachMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		No matched <b>TermBase</b> terms to replace.</p>
		
		</div>
		<div id="noRplaceTmMachMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		No matched <b>TM</b> terms to replace.</p>
		
		</div>
			<div id="validSearchMsg" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		Enter valid character.</p>
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
				<div id="showPicBrwse" title="show picture" class="nodisplay">
		<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>              
		<form id="showPicForm" name="showPicForm"  target="uploadFrame" method="post"  >
				<div id=showImage style="width:550px; border:1px solid #cccccc;height: 350px;margin-top: 20px;margin-left: 30px;"><img width="550px" height="350px"  src=""  /></div>
				
				</form>
		</div>
		
		<div id="uploadTerm" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg">Please select the image to upload.</span></p>
		</div>
	 </div>	
	 <div id="InvaliduploadPic" title="Alert" class="nodisplay">
			<p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
		<span id="logmsg">Invalid File.</span></p>
		</div>
	 	<div id="mail_success" title="Success" class="nodisplay">
			<p class="topmargin15"><span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
		<span id="logmsg">Mail will be send requesting for change term.</span></p>
		</div>
	 
  <div class='footerTopLine' style="margin-top: 155px;"></div>
		<div id="loading">
		 	
		 </div>
	<%@include  file="../common/footer.jsp"%>
	</div>

	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/prototype.min.js"></script>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/search.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
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