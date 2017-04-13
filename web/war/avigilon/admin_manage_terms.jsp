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
<title> Terminology Community</title>
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
<input type="hidden" name="serverDate" id="serverDate" value= <%=new java.util.Date().getTime()%>>

<div class='siteContainer'>
    <%@include file="../common/header.jsp" %>
    <%
        List<SubMenu> subMenus = (List<SubMenu>) session.getAttribute(SessionKey.SUB_MENU.getKey());
        boolean pageFlag = false;
        if (subMenus != null) {
            for (SubMenu subMenu : subMenus) {
                if ("admin_manage_terms.jsp".equalsIgnoreCase(subMenu.getPageUrl())) {
                    pageFlag = true;
                    break;
                }
            }
        }
        if (!pageFlag)
            response.sendRedirect("./index.jsp");
    %>
    <div class='contentContainer'>
    <h1 class='bottommargin10'>Manage linguistic assets</h1>

    <div class='leftContainerManage'>

    <!-- Tabs -->
    <div id="tabs" class="bottommargin15">
    <ul>
        <li class="tab1"><a id="firstStep" href="#tabs-1">Highlights</a></li>
        <li class="tab2"><a class="importTermBase nodisplay" href="#tabs-2">Import Termbases </a></li>
        <li class="tab3"><a class="exportTermBase nodisplay" href="#tabs-3">Export Termbases </a></li>
        <li class="tab4"><a class="importTM nodisplay" href="#tabs-4">Import TMs</a></li>
        <li class="tab5"><a class="exportTM nodisplay" href="#tabs-5">Export TMs</a></li>
        <li class="tab6"><a href="#tabs-6">View TMs</a></li>
    </ul>
    <div id="tabs-1" class='reportContainerManage' style ="margin-left:-7px;border-width:1px;">
        <div class='reportHeader'>
            <div class='left'>
                <div class="headerTxt leftpadding25">
                    <div id="totalTerms"></div>
                    <div class='subCaption'>terms in glossary</div>
                    <input type="hidden" value="" id="termsInGlossary"/>
                </div>
            </div>
            <div class='right'>
                <div class="headerTxt">
                    <div id="totalDebatedTrms"></div>
                    <div class='subCaption'>terms being debated</div>
                </div>
                <div class='openCloseTerms'></div>
            </div>
        </div>
        <div class='reports'>
            <div class='rightmargin20 leftmargin40'>
                <div id="chartContainer">FusionCharts will load here!</div>
            </div>
            <div class='leftmargin20'>
                <div id="chartContainer1">FusionCharts will load here!</div>
            </div>
            <br class='clear'/>

        </div>
        <div class="smallFont padding5 alignRight" id="viewRprt"></div>
    </div>
    <div id="tabs-2"class="border-aln">
    <div class="topmargin10">
    <iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
    <form id="ImportForm" name="ImportForm" method="post" target="uploadFrame"
          action="<%=application.getContextPath()%>/impExp_serv" ENCTYPE="multipart/form-data">
        <input type="hidden" name="c" value="importCSV"/>
        <input type="hidden" name="type" value="termInfo"/>
        <input type="hidden" name="type" id="selectedMultiCompanyIds" value=""/>

        <p>Batch import in CSV or TAB or TBX format per template in appendix.</p>
        <!--
        <div class="topmargin10 companySlctDropdwn companyFilter  nodisplay bottommargin10 ">
            <select size="5" name="example-basic" multiple="multiple" title=" select company"
                    id="mutliCompanySlct"></select>
        </div>
        -->
        <div class="topmargin5">
             <input type="file" size="40" name="upload" id="importTerm" style="font-size:12px; width:150px;" />
            <img id="importFile" src="<%=application.getContextPath()%>/images/document_import.png" height="35px"
                 width="35px;" alt="Click to Import file" title="Click to Import file"
                 style="vertical-align: bottom; margin-left: 30px;cursor:pointer;"/>
        </div>
    </form>
    <div id="message"></div>
    <div id="addTerm" class="topmargin20 bottompadding10">
    <form id="addNewTerm">
    <h2>Add new term </h2>

    <h3 class="topmargin10">Entry (concept) information</h3>

    <div id="entryInfo" class="topmargin5 bottommargin10">
        <div class="left">
            <div>
                <div class="label">Concept definition:</div>
                <input type="text" value="" class="text250" name="cncptDef" id="cncptDef"/>
            </div>
            <!-- 	<div class="topmargin5">
                    <div class="label">Category:</div>
                    <select class="text250 conceptCategory"  name="category" id="category">
                        <option value="">--Select--</option>
                    </select>
                </div> -->
            <div class="topmargin5">
                <div class="label">Category:</div>
                <select class="text250 termCategory" name="termCategory" id="termCategory">
                    <option value="0">--Select--</option>
                </select>
            </div>
            <!--
            <div class="topmargin5 companySlctdropdown  companyFilter nodisplay">
                <div class="label">Company<span class="redTxt">*</span>:</div>
                <div class="floatright nodisplay companySlctErr" style="width:215px;margin-right:69px;"><span
                        class="error companySlctErr">Select at least one company</span></div>
                <select class="text250 companySlct" name="companySlct" id="companySlct" multiple="multiple">
                    <option value="">--Select--</option>

                </select>
            </div>
            -->
        </div>
    </div>
    <div class="clear"></div>
    <div id="sourceInfo" class="topmargin15 bottommargin10">
        <h3>English term information</h3>

        <div class="left topmargin5">











            <div>
                <div class="label">English<span class="redTxt">*</span>:</div>
                <label for="termInfo" class="error floatright nodisplay" generated="true"></label>

                <div class="clear"></div>
                <input type="text" value="" class="text250" name="termInfo" id="termInfo"/>

            </div>
            <div class="topmargin5">
                <div class="label">Form:</div>
                <select class="text250 termFormData" name="termForm" id="termFormData">
                    <option value="0">--Select--</option>
                </select>
            </div>
            <div class="topmargin5">
                <div class="label">Contextual example:</div>
                <input type="text" value="" class="text250" name="cncptExample" id="cncptExample"/>
            </div>
            <div class="topmargin5">
                <div class="label">Term notes:</div>
                <input type="text" value="" class="text250" name="termNotes" id="termNotes"/>
            </div>
            <div class="topmargin5">
                <div class="label">Term part of speech:</div>
                <label for="termPOS" class="error floatright nodisplay"
                       generated="true"></label>

                <div class="clear"></div>
                <select class="text250" id="termPOS" name="termPOS">
                    <option value="">--Select--</option>
                </select>
            </div>
            <div class="topmargin10">
                <div class="label">Domain:</div>
                <select class="text250 termDomain"  name="termDomain"  id="termDomain">
                    <option value="">--Select--</option>
                </select>
            </div>
        </div>
    </div>
    <div class="clear"></div>
    <div id="targetInfo" class="topmargin15 bottommargin10">
        <h3>Target language information</h3>

        <div class="left topmargin5">
            <div style="padding-bottom: 7px">
                <div class="label">Target language<span class="redTxt">*</span>:</div>
                <label for="trgtLang" class="error nodisplay" style="float:left;"
                        generated="true"></label>

                <div class="clear"></div>
                <select class="text250 language" name="trgtLang" id="trgtLang">
                    <option value="">--Select--</option>
                </select>
            </div>
            <div class="topmargin5">
			<div class="label">Target part of speech:</div>
					<label for="targetPOS" class="error floatright nodisplay" generated="true"></label><div class="clear"></div>
					<select class="text250"  id="targetPOS" name="termPOS">
					<option value="">--Select--</option>
				   </select>
		  </div>
            <div>
                <div class="label">Translation:</div>
                <input type="text" value="" class="text250" name="trnsltn" id="trnsltn"/>
                <!-- input type="text" value="" class="text250" name="cncptDef" style="direction:rtl; text-align: left;" / -->
            </div>

        </div>
    </div>
    <div class="clear"></div>
    <div id="deprecatedInfo" class="topmargin15 bottommargin10">
        <h3>Deprecated Term information</h3>

        <div class="left topmargin5">
            <div class="topmargin5">
                <div class="label">Deprecated Source Term 1:</div>
                <input type="text" value="" class="text250" name="depSource1" id="depSource1"/>
            </div>

            <div class="topmargin5">
                <div class="label">Deprecated Source Term 2:</div>
                <input type="text" value="" class="text250" name="depSource2" id="depSource2"/>
            </div>

            <div class="topmargin5">
                <div class="label">Deprecated Source Term 3:</div>
                <input type="text" value="" class="text250" name="depSource3" id="depSource3"/>
            </div>

        </div>
        <div class="right topmargin5">

            <div class="topmargin5">
                <div class="label">Deprecated Target Term 1:</div>
                <input type="text" value="" class="text250" name="depTarget1" id="depTarget1"/>
            </div>
            <div class="topmargin5">
                <div class="label">Deprecated Target Term 2:</div>
                <input type="text" value="" class="text250" name="depTarget2" id="depTarget2"/>
            </div>
            <div class="topmargin5">
                <div class="label">Deprecated Target Term 3:</div>
                <input type="text" value="" class="text250" name="depTarget3" id="depTarget3"/>
            </div>

        </div>
    </div>
    <div class="clear"></div>
    <!-- 	<div class="">
                <div class="label">Comments:</div>
                <textarea rows="2" cols="65"  name="comment" id="comment"></textarea>
        </div> -->

    <br/>

    <div class="saveBtn"><input type="submit" value="Save data" id="saveData" name="saveData"
                                class="commonBtn padding5"/><input type="reset" value="Reset" id="resetBtn"
                                                                   class="leftmargin10 commonBtn padding5"/></div>
    </form>
    </div>

    </div>

    </div>
    <div id="tabs-3" class="border-aln">
        <input type="hidden" name="exportType" value="termInfo"/>

        <div class="topmargin10"><p class="bold">Filter by: <select class="exprtCatSlct text178"
                                                                    name="exportBy"></select>
        </p></div>
        <div class="topmargin15 nodisplay" id="category_list">
            <p class="bold">Select from available options:<br/>
                <select class="exprtCatList text220 topmargin10" multiple size="5" name="selectedIds"></select>
            </p>
        </div>
        <div class="topmargin15"><input type="button" value="Get terms list" class="commonBtn disabledBtn"
                                        id="getFilteredList" disabled="disabled"/></div>
        <iframe id="downloadFrame" name="downloadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
        <form id="downloadForm" name="downloadForm" target="downloadFrame" method="post">
            <div id="waiting"></div>
            <div style="padding-top: 5px;">
                <p class="bold">Select file format to export:
                    <select class="widthForExport exportFormat" name="exportFormat">
                        <option>CSV</option>
                        <option>TAB </option>
                        <option>TBX</option>
                    </select>
                    <img src="<%=application.getContextPath()%>/images/document_export.png" height="35px" width="35px;"
                         class="export" alt="Click to Export" title="Click to Export"
                         style="vertical-align: bottom; margin-left: 30px;cursor:pointer;"/></p>
            </div>
        </form>
    </div>
    <div id="tabs-4" class="border-aln">
        <div id="uploadTMXFormId">
            <form>
                <input type="hidden" name="c" value="importTMX"/>
                <input type="hidden" name="type" value="termTMX"/>
                <input type="hidden" name="fileId" value="" id="fileId"/>

                <div id="inputSize"></div>
                <div class="topmargin5"><input type="file" size="40" name="upload" id="importTMXTerm"
                                               style="font-size:12px;"/> <input type="image"
                                                                                src="<%=application.getContextPath()%>/images/document_import.png"
                                                                                height="35px" width="35px;"
                                                                                alt="Click to Import file"
                                                                                title="Click to Import file"
                                                                                style="vertical-align: bottom; margin-left: 30px;cursor:pointer;"
                                                                                id="importTMXFile"></div>
            </form>
        </div>
        <div id="uploadTMxId" class="nodisplay" style="color: #00A4D6;margin-top: 5px;cursor:pointer;">Upload another
            file
        </div>

        <div class="topmargin20 nodisplay" id="impUrl"><a class="nextEnable topmargin15 font-12"
                                                          style="color: #00A4D6;margin-top: 5px;text-align: center;"
                                                          href="import_export_status.jsp">Click here to check Imported
            File
            status</a></div>

    </div>
    <div id="tabs-5" class="border-aln">
        <input type="hidden" name="exportTMXType" value="termInfo"/>

        <div class="topmargin10"><p class="bold">Filter by: <select class="exprtCatTMXSlct text178"
                                                                    name="exportTMXBy"></select></p></div>
        <div class="topmargin15 nodisplay" id="category_list_TMX">
            <p class="bold">Select from available options:<br/>
                <select class="exprtTMXCatList text220 topmargin10" multiple size="5" name="selectedTMXIds"></select>
            </p>
        </div>
        <div class="topmargin15"><input type="button" value="Get TM list" class="commonBtn disabledBtn"
                                        id="getFilteredTMXList" disabled="disabled"/></div>
        <iframe id="downloadFrame" name="downloadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
        <form id="downloadTMXForm" name="downloadFrame" method="post" target="downloadFrame"
              action="<%=application.getContextPath()%>/impExp_serv">
            <div style="text-align: left;" class="topmargin25">
                <input type="hidden" name="c" value="exportTMX"/>
                <input type="button" value="Export TM" class="commonBtn" name="exportTMX" id="exportTMX"
                       style="padding:3px 10px;"/>
                <input type="hidden" name="fileIdExport" value="" id="fileIdExport"/>

                <div class="topmargin15"><a target="_blank" class="nextEnable topmargin15 nodisplay font-12" id="expUrl"
                                            style="color: #00A4D6;margin-top: 5px;text-align: center;"
                                            href="import_export_status.jsp">Click here to check Exported File status</a>
                </div>
            </div>
        </form>


    </div>

    <div id="tabs-6" style="border-width:1px; margin-right:58px;margin-left:-7px;">

        <div class='reportHeader'>
            <div class='left'>
                <div class="headerTxt leftpadding25" style="width:250px;">
                    <div id="totalTMTerms"></div>
                    <div class='subCaption'>TM units</div>
                    <input type="hidden" value="" id="termsInTMGlossary"/>
                </div>
                <div class='openCloseTms '></div>
            </div>

        </div>
        <div class='reports'>
            <div class='rightmargin20 leftmargin40'>
                <div id="chartContainerTm" class="chartContainer">FusionCharts will load here!</div>
            </div>

            <br class='clear'/>

        </div>
        <div class="smallFont padding5" style="text-align:right;" id="viewRprt"></div>

    </div>


    </div>


    </div>

    <div class='rightContainer'>
        <div class='invitePpl bottommargin10'>
            <div class='bottommargin10'>Term search:</div>
            <div><input type='text' class="width233"  value='Enter term to search...' id='newTerm'/></div>
            <div class="smallFont topmargin10" style="margin-right: 5px;">
                <div class="floatleft" style="margin-top: 3px;"><input type="checkbox" id="chkCase" name="chkCase"/>
                </div>
                <div class="topmargin5 floatleft">Case sensitive</div>
            </div>
            <div style="text-align: right;margin-right: 5px;"><input type='button' id='searchTerm' value='Search'/>
            </div>
        </div>
        <div class='searchTm  nodisplay bottommargin10'>
            <div class='bottommargin10'>TM search:</div>
            <div><input type='text' size='35' value='Enter term to search...' id='newTmTerm'/></div>
            <div class="smallFont topmargin10" style="margin-right: 5px;">
                <div class="floatleft" style="margin-top: 3px;"><input type="checkbox" id="chktmCase" name="chktmCase"/>
                </div>
                <div class="topmargin5 floatleft">Case sensitive</div>
            </div>
            <div style="text-align: right;margin-right: 5px;"><input type='button' id='searchTm' value='Search'/></div>
        </div>
        <div id="termImage" class="nodisplay">
            <div style="width:100px; border:1px solid #cccccc;height: 100px;margin-top: 97px;margin-left: 1px;"><img
                    width="100px" height="100px" id="uploadedImage"
                    src="<%=application.getContextPath()%>/images/default.jpg"/></div>
            <div class="topmargin10 alignCenter"><a href="javascript:;" class="changePic"
                                                    style="font-size: 12px;padding-right: 161px;">Add term picture</a>
            </div>
        </div>
    </div>
    <div class='clear'></div>
	<%@include file="manage_linguistic_assets_highlights.jsp" %>
	<div class="topmargin15 nodisplay" id="tmModule">
        <div class="topmargin10 langSlctDropdwn bottommargin10 TmLangSlctDropdwn">
            <select size="5" name="example-basic" multiple="multiple" title=" select language"
                    id="tmLanguageSlct"></select>
        </div>
        <!--
        <div class="topmargin10 companyTmsSlctDropdwn  bottommargin10 ">
            <select size="5" name="example-basic" multiple="multiple" title="select company"
                    id="companyTmsSlct"></select>
        </div>
        -->
        <div class="bottommargin10 tmsBtn" style="text-align: right;width:950px;">
            <input type="button" value="Delete Selected" class="commonBtn" name="deleteMultipleTerms"
                   id="deleteMultipleTms"
                   style="padding:3px 10px;"/>&nbsp;&nbsp;
            <input type="button" value="Delete All" class="commonBtn" name="deleteAllTms" id="deleteAllTms"
                   style="padding:3px 10px;"/>

        </div>

        </p>
        <div id="manageTmTbl" class='bottommargin10 topmargin15'>
            <div id='mngTmDtlSectionHead'>
                <div id='column0' class='width30'><input type="checkbox" id="selectAllTms" title="Select all"
                                                         style="margin-top:1px;margin-left:-12px;"/></div>
                <div id='sourceTerm' class='width170' sortOrder="ASC">Source</div>
                <div id='targetTerm' class='width130' sortOrder="ASC">Target</div>
                <div id='tmLanguage' class='width90' sortOrder="ASC">Language</div>
                <div id='product' class='width110' sortOrder="ASC">Product line</div>
                <div id='domain' class='width110' sortOrder="ASC">Industry domain</div>
                <div id='tmCompany' class='width110 viewCompany nodisplay' sortOrder="ASC">Company</div>
                <div id='content' class='width110' sortOrder="ASC">Content type</div>
                <div id='termEdit' class='width30' sortOrder="ASC">Edit</div>
                <div id='termDelete' class='width30' sortOrder="ASC">Delete</div>
            </div>
            <div id='tmDtlRowsList'>

            </div>
            <div id="paginationTm" class="pageination">
                <div style="color: #999999; float:left">Viewing <span id="rangeOfTms">0 </span> of <span
                        id="totalPolledTms">0</span></div>
                <div class="floatright"><span class="rightmargin5 tmPrevious">Previous</span> | <span
                        class="leftmargin5 tmNext">Next</span></div>
            </div>
        </div>

    </div>
    </div>


    <div id="invitation" title="Invite to vote" class="nodisplay leftpadding15">
        <form id="termVoteForm">
            <div class="topmargin5">
                <label for="categorySlct" class="error floatleft nodisplay leftmargin40"
                       generated="true"></label>

                <div class="clear"></div>
                <p class="bold">Select: <select class="categorySlct text178" id="categorySlct" name="categorySlct">
                    <option value="">--Select--</option>
                    <option value="role">Role</option>
                    <option value="language">Language</option>
                    <option value="domain">Domain</option>
                </select></p>
                <div class="nodisplay redTxt leftmargin100 leftpadding40" id="usersReq">Select any one
                    category to invite users
                </div>
            </div>
            <div class="topmargin15 floatleft" id="category_list">
                <div class="nodisplay redTxt leftmargin100 userReqError" id="selectReq">
                    Select at least one from the list
                </div>
                <select class="categoryList text220" name="categoryList1" multiple size="5"></select>

            </div>
            <div class="width50 floatleft leftmargin15 topmargin20">
                <img src="<%=application.getContextPath()%>/images/right.png" alt="" height="30px" width="30px"
                     class="moveData"/><br class="clear"/>
                <img src="<%=application.getContextPath()%>/images/delete.png" alt="" height="30px" width="30px"
                     class="removeUser topmargin10"/>
            </div>

            <div class="floatleft" id="users_list">
                <p class="bold">Invited users</p>

                <div class="nodisplay redTxt leftmargin100 userReqError" id="noUsersReq">
                    No users for selected category
                </div>
                <div class="nodisplay redTxt leftmargin100 userReqError" id="UsersReqError">
                    Select users
                </div>
                <select class="usersList text220" name="usersList" multiple size="5">

                </select>


            </div>
            <div class="clear"></div>
            <div id="votingPeriod" class="topmargin5">
                <label for="votingPeriodNum" class="error floatleft nodisplay leftmargin15" generated="true"></label>
                <br class="clear"/>

                <p class="bold">Voting period (#days):
                    <input id="votingPeriodNum" name="votingPeriodNum" type="text" size="23" value=""/>
                </p>
            </div>
            <div class="clear"></div>
            <div class="topmargin10">
                <label for="termTemplate" class="error floatleft nodisplay leftmargin95" generated="true"></label>
                <br class="clear"/>

                <p class="bold">Email template<span class="redTxt">*</span>:
                    <select class="text190 inviteEmailTemplate" id="termTemplate" name="termTemplate">
                        <option value="">--Select template--</option>
                    </select>
                </p>

            </div>
            <div class="clear"></div>
            <div class="topmargin15 emailPrvw"
                 style="height:180px;width:500px; border:1px solid #888888;overflow: auto;">

            </div>
            <div class="clear"></div>
            <div class="sendMail topmargin15 alignCenter">
                <input type="button" value="Send mail" id="inviteVoteMail" name="inviteVoteMail"
                       class="rightmargin15 commonBtn padding5"/>
                <input type="button" value="Cancel" id="cancelInvitMail" name="cancelInvitMail"
                       class="commonBtn padding5"/>
            </div>
        </form>

    </div>

    <div id="loading">

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
       <div style="display: none;" id="historyDetailsDiv" title="History details">
        <table cellpadding="0" cellspacing="0" border="0" class="display" id="historyDetailsTab" width="100%">
            <thead>
            <th width="9%">User</th>
            <th width="9%">Changed Source Term</th>
            <th width="9%">Changed Target Term</th>
            <th width="5%">Term Part of Speech</th>
            <th width="10%">Concept Definition</th>
            <th width="5%">Form</th>
            <th width="5%">Status</th>
            <th width="9%">Term Category</th>
            <th width="9%">Domain</th>
            <th width="9%">Term Notes</th>
            <th width="5%">Target Part of Speech</th>
            <th width="9%">Term Usage</th>
            <th width="9%">Date</th>
           
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
    <div id="delete_cnfm" title="Delete" class="nodisplay">
        <p class="topmargin25 alignCenter"><span class="ui-icon ui-icon-alert termMsg" style="display: inline-block; margin: 2px -4px -4px; "></span>Are
            you sure you want to delete? Deletion will erase all records associated with this term from the database.
        </p>
    </div>
    <div id="mail_success" title="Success" class="nodisplay">
       	<span class="ui-icon ui-icon-circle-check termMsg" style="display: inline-block; margin: 26px -4px -4px 47px;"></span> &nbsp;&nbsp;
			<span id="logmsg" style="display:inline-block; margin-top: 26px;">Invitation is successfully sent.</span>
    </div>
    <div id="delete_success" title="Success" class="nodisplay">
        <p class="topmargin15 alignCenter"><span class="ui-icon ui-icon-circle-check termMsg"></span>
            <span id="deleteMsg"></span></p>
    </div>
    <div id="mail_failed" title="Failure" class="nodisplay">
        <p class="topmargin15 alignCenter"><span class="ui-icon ui-icon-circle-check termMsg" style="display: inline-block; margin: 18px -4px -17px;"></span> &nbsp;&nbsp;
            <span id="failmsg" style="display: inline-block;"></span> <span id="failedUserEmail"></span>.</p>
    </div>
    <div class='footerTopLine'></div>
    <div id="termExtendMsg" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            This term is not being debated.</p>
    </div>
    <div id="importMessage" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Please select the file to import.</p>
    </div>
    <div id="invalidFileFormat" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Invalid file.</p>
    </div>
    <div id="importCompanySlctMessage" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Please select at least one company to import the file.</p>
    </div>
    <div id="deleteTM" title="Success" class="nodisplay">
        <p class="topmargin20 alignCenter" style="display:inline-block;margin-left:66px"><span class="ui-icon ui-icon-circle-check termMsg"></span>
            Deleted successfully.</p>
    </div>
    <div id="deleteTerms" title="Success" class="nodisplay">
        <p class="topmargin20 alignCenter" style="display:inline-flex;margin-left:66px;margin-top:35px"><span class="ui-icon ui-icon-circle-check termMsg"></span>
            Deleted Successfully.</p>
    </div>
    <div id="selectTermDelete" title="Success" class="nodisplay" style="text-align:center;padding-top:35px">
        <p class="topmargin20"><span class="ui-icon ui-icon-circle-check termMsg"></span>
        </p>
    </div>

    <div id="termSelctMsg" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Please select at least one term to perform Action.</p>
    </div>
    <div id="termExpChkMsg" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Please select at least one option available.</p>
    </div>
    <div id="validationMsg" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Enter valid characters.</p>
    </div>
    <div id="dateAlet" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Enter valid date.</p>
    </div>
    <div id="termAddSuccess" title="Success" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Term successfully added.</p>
    </div>
    <div id="termAddFailed" title="Failed" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Failed to add new term.</p>
    </div>
    <div id="termUpdate" title="Success" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Term Updated Successfully.</p>
    </div>
    <div id="termAddFailedExists" title="Failed" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Term Already Exists</p>
    </div>
    <div id="termImportSuccess" title="Success" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Successfully imported CSV/TBX file.</p>
    </div>
    <div id="termImportFailed" title="Failed" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            Failed to import CSV/TBX file.</p>
    </div>


    <div id="uploadProgressMsg" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            <span id="importTmxmsg"></span> <br/><a target="_blank" class="nextEnable" id="progressLink"
                                                    style="color: #00A4D6;margin-top: 5px;text-align: center;">click
                here to
                check the status</a></p>
    </div>
    <div id="downloadProgressMsg" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            File download is in progress <br/><a target="_blank" class="nextEnable" id="progressLinkDownload"
                                                 style="color: #00A4D6;margin-top: 5px;text-align: center;">click here
                to
                check the status</a></p>
    </div>

    <div id="termImprtInfo" class="nodisplay" title="Import Termbases Status">
        <p class="topmargin15 greenTxt"><span id="successAddedTerms"></span></p>

        <p class="topmargin15 greenTxt"><span id="successUpdatedTerms"></span></p>

        <p class="topmargin15 redTxt"><span id="failedImportedTerms"></span></p>

        <div id="downloadTmpltFile" class="topmargin15 nodisplay">Please download the sample csv import template<span
                class="logFile nextEnable leftpadding15">Download</span></div>
        <div id="downloadTBXTmpltFile" class="topmargin15 nodisplay">Please download the sample TBX import template<span
                class="logFile nextEnable leftpadding15">Download</span></div>
         <div id="downloadTABTmpltFile" class="topmargin15 nodisplay">Please download the sample TAB import template<span
                class="logFile nextEnable leftpadding15">Download</span></div> 
        <p class="topmargin15 redTxt failedInfoTerms">Failed to import Terms : <span id="failedAddedTerms"></span></p>

        <div id="termImportTMXFailed" title="Failed" class="nodisplay">
            <p class="topmargin30 redTxt alignCenter"><span
                    class="termMsg"></span>
                Failed to import TMX file.</p>
        </div>
        <div id="termImportTMXInvalid" title="Failed" class="nodisplay">
            <p class="redTxt invalid alignCenter"><span class="termMsg"></span>
                Imported file is invalid.Please upload valid file.</p>
        </div>
        <div class="failedInfo">
            <div class="userImportTblHead topmargin15 boxWidth350">
                <div class="width220">Message</div>
                <div class="width130">Line # in import file</div>
            </div>
            <div class="clear"></div>
            <div class="termImportTblBody" style="width:365px;">
            </div>
        </div>
    </div>


    <div id="uploadPicBrwse" title="Upload picture" class="nodisplay">
        <iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
        <form id="uploadPicForm" name="uploadPicForm" target="uploadFrame" method="post"
              action="<%=application.getContextPath()%>/teaminology_ctrler/teaminology/uploadProfileImage?t=term"
              ENCTYPE="multipart/form-data">
            <p class="bold topmargin15"> Browse picture:</p>

            <p class="topmargin15"><input type="file" size="40" name="uploadPic" id="uploadPic"/></p>

            <p class="error nodisplay topmargin10" id="errorMsg"></p>

            <p class="topmargin15"><input type="button" value="Upload" id="uploadTermPic"
                                          class="commonBtn toppadding5"/>
            </p>

        </form>
    </div>

    <div id="showPicBrwse" title="show picture" class="nodisplay">
        <iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
        <form id="showPicForm" name="showPicForm" target="uploadFrame" method="post">
            <div id=showImage
                 style="width:550px; border:1px solid #cccccc;height: 350px;margin-top: 20px;margin-left: 30px;"><img
                    width="550px" height="350px" src=""/></div>

        </form>
    </div>


    <div id="InvaliduploadPic" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            <span id="logmsg">Invalid File.</span></p>
    </div>

    <div id="uploadTerm" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            <span id="logmsg">Please select the image to upload.</span></p>
    </div>

    <div id="InvaliduploadPic" title="Alert" class="nodisplay">
        <p class="topmargin30 alignCenter"><span class="termMsg"></span>
            <span id="logmsg">Invalid File.</span></p>
    </div>
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
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.validate.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/json2.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/common.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Constants.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/controller.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/charts.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/qtip.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/leaderBoard.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.validate.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.ns-autogrow.min.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/Util.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/userInfos.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
    <script type="text/javascript" src="<%=application.getContextPath()%>/js/admin_overview.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/manage_linguistic_hilight.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/admin_manage_terms.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/menu.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js/hpTeamList.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
	<script type="text/javascript" src="<%=application.getContextPath()%>/js-lib/jquery.tinymce.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>

<script type="text/javascript">
    jQuery(document).ready(function () {
        jQuery("#tabs").tabs();
    });
</script>
</body>
</html>