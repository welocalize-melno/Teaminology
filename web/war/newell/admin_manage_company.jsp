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
<title> Newell Terminology Portal</title>
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/demo_table.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/global_hp_new.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
    <link href="<%=application.getContextPath()%>/css/font.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
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
<body class="adminOvr">
	<div class='siteContainer'>
	<%@include file="../common/header.jsp"%>
        <%
            List<SubMenu> subMenus = (List<SubMenu>) session.getAttribute(SessionKey.SUB_MENU.getKey());
            boolean pageFlag = false;
            if (subMenus != null) {
                for (SubMenu subMenu : subMenus) {
                    if ("admin_manage_company.jsp".equalsIgnoreCase(subMenu.getPageUrl())) {
                        pageFlag = true;
                        break;
                    }
                }
            }
            if (!pageFlag)
                response.sendRedirect("./index.jsp");
        %>

        <div class='contentContainer'>
            <h1 class=' bottommargin10 floatleft'>Manage company</h1>
            <div class='clear'></div>
            <div id="user" class="greenTxt font-12" style="font-size:12px;"></div>
            <div style="text-align: right;margin-right:351px;" class="bottommargin10">
                <input type="button" value="Add" class="commonBtn addCompany nodisplay" name="addCompanyBtn"
                       id="addCompanyBtn" style="padding:3px 10px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="Delete" class="commonBtn deleteCompany nodisplay"
                       name="deleteMultipleCompanies" id="deleteMultipleCompanies" style="padding:3px 10px;"/>
            </div>

            <div class="topmargin15" id="teamListModule">
                <div id="manageCompanyTbl" class='bottommargin10 topmargin15'>
                    <div id='companyListSectionHead'>
                        <div id='column0' class='width20'><input type="checkbox" id="selectAll" title="Select all"
                                                                 style="margin-top:1px;margin-left:-1px;"/></div>
                        <div id="companyName" class="width200" sortOrder="ASC">Name</div>
                        <div id="emailId" class="width210" sortOrder="ASC">Email id</div>
                        <div id="poc" class="width110" sortOrder="ASC">Poc</div>
                        <div id="modify" class="width90 modifyCompany nodisplay" sortOrder="ASC">Modify</div>
                        <div id="delete" class="width90 deleteCompany nodisplay" sortOrder="ASC">Delete</div>
                    </div>
                    <div id='companyRowsList'>
                        <div class="rowBg">
                            <div class="chkBx"><input type="checkbox" value="1" class="floatleft"></div>
                        </div>
                    </div>
                    <div id="pagination"
                         style="height: 20px;font-size: 11px;border-bottom: 1px solid #dddddd;marign-top:0;padding-top: 5px;font-weight: normal;">
                        <div style="float:left;"><span>Viewing <span id="rangeOfList"> 0</span> of <span
                                id="totalCompanies">0</span></span></div>
                        <div style="float: right;"><span class="rightmargin5 previous">Previous</span> | <span
                                class="next leftmargin5">Next</span></div>
                    </div>
                </div>

            </div>

            <div id="addCompany" class="nodisplay" title="Add company">
                <form id="companyForm">
                    <div class="regFrm topmargin30">
                        <div class='boxWidth320'>
                            <label for="companyNameId" class="error floatright nodisplay"
                                   style="width:195px;margin-right:10px;margin-top:-17px;" generated="true"></label>

                            <div class="clear"></div>
                            <div class="error floatright nodisplay companyExistsFlag"
                                 style="width:195px;margin-right:10px;margin-top:-31px;"></div>
                            <div class="clear"></div>
                            <div class="" style="padding-bottom: 20px;">
                                <div class="label width110 floatleft">Name<span class="redTxt">*</span>:</div>
                                <div class="floatleft" style="width:200px;"><input type="text" id="companyNameId"
                                                                                   size="25" value="" class="text190"
                                                                                   name="companyNameId" tabindex="1"/>
                                </div>
                            </div>
                            <br class="clear"/>

                            <div class="" style="padding-bottom: 20px;">
                                <div class="label width110 floatleft">POC:</div>
                                <div class="floatleft" style="width:200px;"><input type="text" id="contact" size="25"
                                                                                   value="" class="text190"
                                                                                   name="contact" tabindex="2"/></div>
                            </div>
                            <br class="clear"/>

                            <div class="" style="padding-bottom: 20px;">
                                <div class="label width110 floatleft">Email:</div>
                                <div class="floatleft" style="width:200px;"><input type="text" id="email" size="25"
                                                                                   value="" class="text190" name="email"
                                                                                   tabindex="3"/></div>
                            </div>
                            <br class="clear"/>

                            <div style="margin-left: 110px;" class="topmargin15 addBtn"><input type="button"
                                                                                               value="Submit"
                                                                                               class="loginBtn addNewCompany"
                                                                                               tabindex="4"
                                                                                               name="submitBtn"/> <input
                                    id="cancelAddCompany" type="button" name="resetBtn" tabindex="5" value="Cancel"
                                    class="leftmargin25"/></div>
                        </div>
                    </div>
                </form>
            </div>


            <div id="modifyCompany" class="nodisplay" title="Modify company">
                <form id="modifyForm">
                    <div class="regFrm topmargin30">
                        <div class="editDetails"></div>


                        <div class="clear"></div>
                        <div style="margin-left: 124px;" class="topmargin15 editBtn"><input type="button"
                                                                                            id="submitModCompany"
                                                                                            value="Submit"
                                                                                            class="loginBtn"
                                                                                            name="submitBtn"/> <input
                                id="cancelModCompany" type="button" name="resetBtn" value="Cancel"
                                class="loginBtn leftmargin25"/></div>
                    </div>

                    <div class='clear'></div>
                </form>
            </div>
        </div>
        <div id="companyAddSuccess" title="Success" class="nodisplay">
            <p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
                Company added successfully .</p>
        </div>
        <div id="companyAddFailed" title="Failed" class="nodisplay">
            <p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
                Failed to add a Company.</p>
        </div>
        <div id="companySlctMessage" title="Alert" class="nodisplay">
            <p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
                Select at least one company to perform Action.</p>
        </div>

        <div id="companyUpdateSuccess" title="Success" class="nodisplay">
            <p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
                Company updated successfully .</p>
        </div>
        <div id="companyUpdateFailed" title="Failed" class="nodisplay">
            <p class="topmargin30" style="text-align:center;"><span style="float:left; margin:0 7px 25px 0;"></span>
                Failed to update a Company.</p>
        </div>
        <div id="delete_cnfm" title="Delete" class="nodisplay">
            <p class="topmargin25"><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Are
                you sure you want to delete? Deletion will erase all records associated with this company(s) from the
                database. </p>
        </div>
        <div id="deleteCompanies" title="Success" class="nodisplay">
            <p class="topmargin20"><span class="ui-icon ui-icon-circle-check"
                                         style="float:left;margin:0 7px 25px 0;"></span>
                Deleted successfully.</p>
        </div>

        <div class='footerTopLine'></div>
        <%@include file="../common/footer.jsp" %>
    </div>

    <script type="text/javascript" src="<%=application.getContextPath()%>/FusionCharts/FusionCharts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery-1.7.1.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tagsphere.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.dataTables.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.validate.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
   <script type="text/javascript" src="<%=application.getContextPath()%>/js/admin_manage_company.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/admin.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
   	<script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>

    <script type="text/javascript">
        jQuery(document).ready(function () {
            jQuery("#tabs").tabs();
        });
    </script>

</body>
</html>