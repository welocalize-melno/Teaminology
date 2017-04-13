<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import= "com.teaminology.hp.service.enums.TeaminologyProperty"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="<%=application.getContextPath()%>/css/demo_table.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.qtip.min.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery-ui-1.8.18.custom.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/jquery.multiselect.filter.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
<link href="<%=application.getContextPath()%>/css/global.css?<%=TeaminologyProperty.RELEASE.getValue()%>" type="text/css" rel="stylesheet">
</head>
<body>
 <form name="rqstNewTrm" id="rqstNewTrm">
            <div id="rqstNewTerm" class="topmargin20 bottompadding10">
                <div class='boxWidth320'>
                    <label for="termInfo" class="error floatright nodisplay"
                           style="width:212px;margin-right:-3px;margin-top:-16px;" generated="true"></label>

                    <div class="" style="padding-bottom: 20px;">
                        <div class="label width110 floatleft">English<span class="redTxt">*</span>:</div>
                        <div class="floatleft" style="width:200px;"><input type="text" class=" text190" name="termInfo"
                                                                           id="termInfo" tabindex="1"/></div>
                    </div>
                    <br class="clear"/>

                    <div class="" style="padding-bottom: 20px;">
                        <div class="label width110 floatleft">Translation:</div>
                        <div class="floatleft" style="width:200px;"><input type="text" class=" text190 trnsltn"
                                                                           name="trnsltn" id="trnsltn" tabindex="2"/>
                        </div>
                    </div>
                    <br class="clear"/>

                    <div class="" style="padding-bottom: 20px;">
                        <div class="label width110 floatleft">Term notes:</div>
                        <div class="floatleft" style="width:200px;"><input type="text" id="termNotes" value=""
                                                                           class="text190" name="termNotes"
                                                                           tabindex="3"/></div>
                    </div>
                    <br class="clear"/>

                    <div class="" style="padding-bottom: 20px;">
                        <div class="label width110 floatleft">Concept definition:</div>
                        <div class="floatleft" style="width:200px;"><input type="text" id="cncptDef" value=""
                                                                           class="text190" name="cncptDef"
                                                                           tabindex="4"/></div>
                    </div>
                    <br class="clear"/>

                    <div class="" style="padding-bottom: 20px;">
                        <div class="label width110 floatleft">Contextual example:</div>
                        <div class="floatleft" style="width:200px;"><input type="text" id="cncptExample" value=""
                                                                           class="text190" name="cncptExample"
                                                                           tabindex="5"/></div>
                    </div>
                    <br class="clear"/>

                </div>
                <div class='boxWidth320' style="width:335px;">

                    <div class="floatright nodisplay languageSlctErr"
                         style="width:215px;margin-right:10px;margin-top:-17px;"><span class="error languageSlctErr">Select at least one language</span>
                    </div>
                    <div class="bottommargin20" style="padding-bottom: 16px;">
                        <div class="label width110 floatleft">TargetLanguage<span class="redTxt">*</span>:</div>
                        <div class="floatleft">
                            <select id="languageSlct" name="languageSlct" multiple="multiple" class="text220"
                                    tabindex="6"></select>

                        </div>
                    </div>

                    <div class="" style="padding-bottom: 20px;">
                        <div class="label width110 floatleft">Form:</div>
                        <div class="floatleft">
                            <select class="text220 termForm" name="termForm" id="termForm" tabindex="7">
                                <option value="0">--Select--</option>
                            </select>
                        </div>
                    </div>
                    <br class="clear"/>

                    <div class="" style="padding-bottom: 20px;">
                        <div class="label width110 floatleft">Category:</div>
                        <div class="floatleft">
                            <select class="text220 termCategory" name="termCategory" id="termCategory" tabindex="8">
                                <option value="0">--Select--</option>
                            </select>
                        </div>
                    </div>
                    <br class="clear"/>

                    <div class="" style="padding-bottom: 20px;">
                        <div class="label width110 floatleft">Part of speech:</div>
                        <div class="floatleft">
                            <select class="text220 termPOS" name="termPOS" id="termPOS" tabindex="9">
                                <option value="0">--Select--</option>
                            </select>
                        </div>
                    </div>
                    <br class="clear"/>
                </div>

                <div class="clear"></div>

                <div style="margin-left: 235px;" class="topmargin15"><input type="button" value="Send mail"
                                                                            id="newTermMail" name="newTermMail"
                                                                            class="rightmargin20 commonBtn padding5"
                                                                            tabindex="10"/>
                    <input type="button" value="Cancel" id="cancelMail" name="cancelMail"
                           class="cancelMailBtn commonBtn padding5" tabindex="11" style="border: 1px solid rgb(174, 174, 174); background-color: rgb(174, 174, 174); font-family: HP Simplified;"/></div>
            </div>
        </form>
        	<script type="text/javascript" src="<%=application.getContextPath()%>/js/index.js?<%=TeaminologyProperty.RELEASE.getValue()%>"></script>
        
</body>
</html>