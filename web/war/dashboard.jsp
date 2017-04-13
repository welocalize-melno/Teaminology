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
<link href="<%=application.getContextPath()%>/css/demo_table.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/global_hp.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<!-- <link rel="stylesheet" type="text/css" media="screen,projection" href="cssmap-europe/cssmap-europe.css" /> -->
<link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/language_m_select.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<script type="text/javascript">
    function validate() {
        var userName = $.trim($("#username").val());
        var password = $("#password").val();
        var flag = true;
        if (userName == "" || userName == null) {
            $("#userError").html("Username is required");
            $("#userError").show();
            $("#username").addClass("error");
            document.loginFrm.username.focus();
            flag = false;
        }
        if (password == "" || password == null) {
            $("#pwdError").html("Password is required");
            $("#pwdError").show();
            $("#password").addClass("error");
            document.loginFrm.password.focus();
            flag = false;
        }

        return flag;
    }
</script>
 <!--<script type="text/javascript">
  function some(){
    $('#map-europe').cssMap({'size' : 960});
  }
</script> -->
</head>
<body>
<div class='siteContainer'>
    <%@include file="./common/header.jsp" %>
    <%
	if (session.getAttribute(SessionKey.USER.getKey()) == null) {
		 response.sendRedirect("./index.jsp");
	}
%>
    <!-- head content part -->
    <div class='contentContainer'>
        <div class="invalidDtls redTxt bottommargin10"><%=lostSession%></div>
        <div class=''>
            <div class="floatleft priodLinks" style="color: #5a5a5a ! important;">
                Select Interval: <span id="yearly" class="off priodSpan">Yearly</span> |
                <span id="quarterly" class="off priodSpan">Quarterly</span> |
                <span id="monthly" class="off priodSpan" style="border-right: medium none;">Monthly</span>
            </div>
            <div class='clear'></div>
            <div class="leftContain">
	            <div class='boxWidth270 leftpadding8' style="font-family: hp simplified ! important;"
	                <%--  style='border:none;background-image:url("<%=application.getContextPath()%>/images/ovrvwChrtBg.jpg");background-repeat:repeat-x;' --%>>
	                <div class="chrtSubHdr">TERMS IN GLOSSARY</div>
	                <div class="chrtHdrTxt" id="totalTerms"></div>
	                <!-- <div class="chrtSubHdr">terms in glossary</div> -->
	                <div id="chartContainer">FusionCharts will load here!</div>
	            </div>
	            <div class='boxWidth270 leftmargin16 leftpadding8'
	                <%--  style='border:none;background-image:url("<%=application.getContextPath()%>/images/ovrvwChrtBg.jpg");background-repeat:repeat-x;' --%>>
	                <div class="chrtSubHdr">TERMS BEING DEBATED</div>
	                <div class="chrtHdrTxt" id="totalDebatedTrms"></div>
	                <!-- <div class="chrtSubHdr">terms being debated</div> -->
	                <div id="chartContainer1">FusionCharts will load here!</div>
	            </div>
	            <div class='boxWidth270 leftmargin16 leftpadding8'
	                 <%-- style='border:none;background-image:url("<%=application.getContextPath()%>/images/ovrvwChrtBg.jpg");background-repeat:repeat-x;' --%>>
	                <div class="chrtSubHdr">TM UNITS</div>
	                <div class="chrtHdrTxt" id="totalTms"></div>
	                <!-- <div class="chrtSubHdr">TM units</div> -->
	                <div id="chartContainerTm">FusionCharts will load here!</div>
	            </div>
	          </div>
           <!--  <div  class='boxWidth270 leftpadding25' id="map-europe">
 <ul class="europe">
  <li class="eu1"><a href="#albania">Albania</a></li>
  <li class="eu2"><a href="#andorra">Andorra</a></li>
  <li class="eu3"><a href="#austria">Austria</a></li>
  <li class="eu4"><a href="#belarus">Belarus</a></li>
  <li class="eu5"><a href="#belgium">Belgium</a></li>
  <li class="eu6"><a href="#bosnia-and-herzegovina">Bosnia and Herzegovina</a></li>
  <li class="eu7"><a href="#bulgaria">Bulgaria</a></li>
  <li class="eu8"><a href="#croatia">Croatia</a></li>
  <li class="eu9"><a href="#cyprus">Cyprus</a></li>
  <li class="eu10"><a href="#czech-republic">Czech Republic</a></li>
  <li class="eu11"><a href="#denmark">Denmark</a></li>
  <li class="eu12"><a href="#estonia">Estonia</a></li>
  <li class="eu13"><a href="#france">France</a></li>
  <li class="eu14"><a href="#finland">Finland</a></li>
  <li class="eu15"><a href="#georgia">Georgia</a></li>
  <li class="eu16"><a href="#germany">Germany</a></li>
  <li class="eu17"><a href="#greece">Greece</a></li>
  <li class="eu18"><a href="#hungary">Hungary</a></li>
  <li class="eu19"><a href="#iceland">Iceland</a></li>
  <li class="eu20"><a href="#ireland">Ireland</a></li>
  <li class="eu21"><a href="#san-marino">San Marino</a></li>
  <li class="eu22"><a href="#italy">Italy</a></li>
  <li class="eu23"><a href="#kosovo">Kosovo</a></li>
  <li class="eu24"><a href="#latvia">Latvia</a></li>
  <li class="eu25"><a href="#liechtenstein">Liechtenstein</a></li>
  <li class="eu26"><a href="#lithuania">Lithuania</a></li>
  <li class="eu27"><a href="#luxembourg">Luxembourg</a></li>
  <li class="eu28"><a href="#macedonia">Macedonia <abbr title="The Former Yugoslav Republic of Macedonia">(F.Y.R.O.M.)</abbr></a></li>
  <li class="eu29"><a href="#malta">Malta</a></li>
  <li class="eu30"><a href="#moldova">Moldova</a></li>
  <li class="eu31"><a href="#monaco">Monaco</a></li>
  <li class="eu32"><a href="#montenegro">Montenegro</a></li>
  <li class="eu33"><a href="#netherlands">Netherlands</a></li>
  <li class="eu34"><a href="#norway">Norway</a></li>
  <li class="eu35"><a href="#poland">Poland</a></li>
  <li class="eu36"><a href="#portugal">Portugal</a></li>
  <li class="eu37"><a href="#romania">Romania</a></li>
  <li class="eu38"><a href="#russian-federation">Russian Federation</a></li>
  <li class="eu39"><a href="#serbia">Serbia</a></li>
  <li class="eu40"><a href="#slovakia">Slovakia</a></li>
  <li class="eu41"><a href="#slovenia">Slovenia</a></li>
  <li class="eu42"><a href="#spain">Spain</a></li>
  <li class="eu43"><a href="#sweden">Sweden</a></li>
  <li class="eu44"><a href="#switzerland">Switzerland</a></li>
  <li class="eu45"><a href="#turkey">Turkey</a></li>
  <li class="eu46"><a href="#ukraine">Ukraine</a></li>
  <li class="eu47"><a href="#united-kingdom">United Kingdom</a></li>
remove this comment and UK list item (above) to activate the United Kingdom countries
  <li class="eu48"><a href="#england">England</a></li>
  <li class="eu49"><a href="#isle-of-man">Isle of Man</a></li>
  <li class="eu50"><a href="#northern-ireland">Northern Ireland</a></li>
  <li class="eu51"><a href="#scotland">Scotland</a></li>
  <li class="eu52"><a href="#wales">Wales</a></li>

 </ul>
</div>
        </div> -->

        <div class='' style='float:right;'>
            <div class='signUpButton showhide' style="float: none;width: 150px;text-align: center;">
                Sign up and vote
            </div>
            
            <div class="rightContainer" style="margin-top: 30px;">
            <div class="newTrmRqst bottommargin15 nodisplay" style=" margin-top: -30px;">
                <input type="submit" value="REQUEST NEW TERM" class="termRqstBtn"/>
            </div>
             <div class='topmargin12' id='leaderBoard'>
                <h3 style="color: rgb(43, 166, 203);float:left;">Leader Board
                  </h3>
                  <img src="/app/images/globe.png" style="margin-left: 18px; height: 14px;position: relative;top:3px;">
				<select class="langSelect sortByLanHide" size="5" name="example-basic" multiple="multiple"  class="text220 " title=" select language"
                    id="termlanguageSlctForRank"></select> </div>
                    
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
            <!-- end leader board -->
            
            <div id='leadersList' style="position: relative; padding-top: 12px;"></div>
                <div class='leaderBrdFoot' style="padding-top: 12px; float: right;padding-right: 12px;">
                    <span class='smallFont seeAll'>See All</span>
                </div>
            </div>
            
            
            

            <!-- leader board -->
           <!--  <div class='topmargin12' id='leaderBoard'>
                <h3>Leader board</h3>
                <div id='leadersList'></div>
                <div class='leaderBrdFoot'>
                    <span class='smallFont seeAll seeAllhide'>See All</span>
                </div>
            </div> -->
            
        </div>
        <div class='clear'></div>
        <div class=''>
            <span class='tagCloudHead' style="font-size:16px; color:#5a5a5a; font-weight: normal;">Words being voted on:</span>

            <div id='tagCloud'>
                <div id="noTerm" class="nodisplay" align="center">No active polls</div>
                <div id="tags">
                    <div id="listTrms" style="word-wrap: break-word;width:920px;text-align:center; color: #5a5a5a;"></div>
                </div>
            </div>
        </div>
    </div>

    <!-- user login dialog -->
    <div class="PopupDiv" style="display:none;">
        <div style="float:right;"><a href="javascript:;" class="showhide">Close <img
                src="<%=application.getContextPath()%>/images/close-btn.gif" alt="close button" border="0"
                class="leftmargin5" height="10px" width="10px;"/></a></div>
        <div style="font-size: 18px;font-weight:bold;color:#596269;margin-bottom:20px;">Member Sign In</div>
        <form id="loginForm" action="<%=application.getContextPath()%>/hp/login" method="post" name="loginFrm"
              onsubmit="return validate()">

            <label for="username" class="error floatright nodisplay"
                   style="width:200px;margin-right:-3px;margin-top:-16px;" generated="true" id="userError"></label>

            <div class="clear"></div>
            <div style="padding-bottom: 40px;">
                <div class="label floatleft">Username<span class="redTxt">*</span>:</div>
                <div class="floatleft"><input type="text" id="username" name="username" class="text190" maxlength="100"/>
                </div>
            </div>
            <label for="password" class="error floatright nodisplay"
                   style="width:200px;margin-right:-3px;margin-top:-16px;" generated="true" id="pwdError"></label>

            <div class="clear"></div>
            <div style="padding-bottom: 20px;">
                <div class="label">Password<span class="redTxt">*</span>:</div>
                <div class="floatleft"><input id="password" type="password" name="password" class="text190"
                                              maxlength="100"></div>
            </div>
            <div class="clear"></div>

            <p class="smallFont floatright" style="margin-top:5px;"><a href="forgot_password.jsp">Forgot password</a>
            </p>

            <div class="topmargin5" style="margin-top: 32px;margin-left: 76px;">
                <div class="invalidDtls redTxt bottommargin10"><%=loginStatus%>
                </div>
                <div><input type="checkbox" name="rememberMe" style="border:none;font-size: 11px;"/> Remember me</div>
                <div class="clear"></div>
                <div><input type="submit" value="Log On" class="loginBtn topmargin10" name="loginBtn"/></div>
            </div>
        </form>
        <div style="float:left;" class="registerLnk">First time users, <a href="registration.jsp">Register now</a></div>
    </div>

    <!-- request new term part -->
     <div id="newTermDiv"  class="nodisplay"  title="Request new term">
    </div>

    <div id="loading"></div>

    <div id="mail_success" title="Success" class="nodisplay">
        <p class="topmargin15"><span class="ui-icon ui-icon-circle-check"
                                     style="float:left; margin:0 7px 50px 0;"></span>
            <span id="logmsg">Request successfully submitted for review. Mail will be sent upon validation.</span></p>
    </div>

    <div class='footerTopLine'></div>
    <%@include file="../common/footer.jsp" %>
</div>


	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/prototype.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.dataTables.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.qtip.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/index.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>	
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tagsphere.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<!-- <script  type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
    <script type="text/javascript" src="http://cssmapsplugin.com/4/jquery.cssmap.js"></script>  -->
    <script type="text/javascript">
    
    </script>

</body>
</html>