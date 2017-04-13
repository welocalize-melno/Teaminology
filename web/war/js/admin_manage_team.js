(function ($j) {
    var values = 0;
    var cvalues = 0;
    var selectedLangIds = 0;
    var defaultSelectedLang = 0;
    var selecteduserIds = "";
    var savedCriteria = "";
    var teamListLimit = 10;
    var userId = "";
    var onLangId = "";
    var fileName = "";
    var validateUser;
    var validateModifyUser;
    var MultiCValues = 0;
    var flag = true;
    var showAllTeamMem = false;
    var allClicked = false;
    var newSellang = 0;
    var langsClicked = false;
    
    var url = $j(location).attr('href');

	if(url.indexOf("/admin_manage_team.jsp") != -1){
	    $j('#signOut').removeClass('floatrightLogOut');
	    $j('#signOut').addClass('floatrightLogOutForOverview');
	}
    
    validateForm = function (emailIds) {
        var validationFlag = true;
        var emailRegex = /^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9]{2,4}$/;
        var email = emailIds;
        var emailAddress = new Array();
        if (email.indexOf(";") >= 0 && email.indexOf(",") >= 0) {
            emailArray = new Array();
            temp = email.split(";");
            for (var i = 0; i < temp.length; i++) {
                var showDialog = function () {
                    // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
                    $j("#invitation:ui-dialog").dialog("destroy");

                    $j("#invitation").dialog({
                        height: 510,
                        width: 550,
                        modal: true,
                        close: function (event, ui) {
                            $j(".categoryList").html("");
                            $j(".usersList").html("");
                            $j(".categorySlct").val(0);
                            $j(".emailTemplate").val("");
                            $j("#selectReq").hide();
                            $j(".emailPrvw").html("");
                            $j(".mailTmpl").val(0);
                            $j(".sampleVoteMail").hide();
                            $j(".sampleWelcomeMail").hide();
                            $j("#invitation").dialog("destroy");
                            selectedTermIds = "";
                        }
                    });
                };
                if (temp[i].indexOf(",")) {
                    commaArray = temp[i].split(",");
                    emailArray = emailArray.concat(commaArray);
                } else {
                    emailArray.push(temp[i]);
                }
            }
            email = emailArray;
        }
        else if (email.indexOf(",") >= 0) {
            email = email.split(",");
        } else if (email.indexOf(";") >= 0) {
            email = email.split(";");
        } else {
            email = new Array(emailIds);
        }
        for (var i = 0; i < email.length; i++) {
            email[i] = $j.trim(email[i]);
            if (email[i].indexOf(">") > 0 && email[i].indexOf("<") >= 0) {
                suffix = email[i].substring(0, email[i].indexOf("<"));
                prefix = email[i].substring(email[i].indexOf(">") + 1, email[i].length);
                namedEmail = $j.trim(email[i].substring((email[i].indexOf("<")) + 1, email[i].indexOf(">")));
                if (namedEmail != "" && (!Boolean(namedEmail) || !emailRegex.test(namedEmail))) {
                    validationFlag = false;
                    alertMessage("#mailMessage");
                }
                if (suffix.indexOf("@") >= 0 || suffix.indexOf(">") >= 0 || suffix.indexOf("<") >= 0 || prefix.indexOf("@") >= 0 || prefix.indexOf(">") >= 0 || prefix.indexOf("<") >= 0) {
                    validationFlag = false;
                    alertMessage("#mailMessage");
                }
            } else if (email[i] != "" && (!Boolean(email[i]) || !emailRegex.test(email[i]))) {
                validationFlag = false;
                alertMessage("#mailMessage");
            }
            if (email[i] != "") {
                emailAddress.push(email[i]);
            }
        }
        emailIds = emailAddress;
        return validationFlag;
    };

    $j.fn.teamDetails = function () {

        var ctx = $j(this);

        var teamListTmpl = ['<div class="rowBg floatleft" id="team"><div class="chkBx"><input type="checkbox" class="floatleft case" value="test" /></div><div class="row" userId="',
            '',
            '"><div class="width120 bigFont toppadding5 userFullName">',
            '',
            '</div><div class="width140 toppadding5 emailId">',
            '',
            '</div><div class="width100 toppadding5 dateOfCreation">',
            '',
            '</div><div class="width60 toppadding5 accuracyPrcnt">',
            '',
            '</div><div class="width70 toppadding5 lastMonthVotes">',
            '',
            '</div><div class="width60 toppadding5 totalVotes">',
            '',
            '</div><div class="width80 toppadding5 usrCompany">',
            '',
            '</div><div class="width100 toppadding5 usrRole">',
            '',
            '</div><div class="width90 toppadding5 badging floatleft"><img src="',
            '',
            '" /></div><div class="width40 toppadding15 modify modifyAllUser modifyCUser nodisplay"><img class="headerMenuLink modifyImg "  height="20px" width="20px" src="' + $j("#contextRootPath").val() + '/images/Pencil.png" id="',
            '',
            '" /></div><div class="width40 toppadding15 delete delCUser delAllUser nodisplay"><img class="headerMenuLink deleteImg"  height="20px" width="20px" src="' + $j("#contextRootPath").val() + '/images/DeleteIcon.png" id="',
            '',
            '" /></div></div></div><div class="clear"></div>'
        ];

        var termAttrTmpl = '<div class="termAttr"><div class="left"><div><div class="bold termType label" >Part of Speech: </div> <span class="termTypeDesc viewDetailsFld"></span><select class="editDetailsFld nodisplay termPOS"></select>'
            + '</div><div class="topmargin5"><div class="bold label">Form: </div><span class="formDesc viewDetailsFld"></span><select class="editDetailsFld nodisplay termForm"></select>'
            + '</div><div class="topmargin5"><div class="bold label">Program Project: </div><span class="programDesc viewDetailsFld"></span><select class="editDetailsFld nodisplay program"></select>'
            + '</div><div class="topmargin5"><div class="bold label">Term Notes: </div><span class="domainDesc viewDetailsFld"></span><textarea rows="1" cols="12" class="definitionDesc editDetailsFld nodisplay"></textarea>'
            + '</div></div><div ><div><div class="bold label">Concept Definition: </div><span class="definitionDesc viewDetailsFld"></span><textarea rows="2" cols="35" class="definitionDesc editDetailsFld nodisplay"></textarea><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Concept Example: </div><span class="usageDesc viewDetailsFld"></span><textarea rows="2" cols="35" class="usageDesc editDetailsFld nodisplay"></textarea>'
            + '</div></div></div>';


        var suggTrmListTmpl = [
            '<div class="termSlctFrm topmargin15"><span class="termSuggestion">',
            '',
            '</span><input type="radio" name="',
            '',
            '" /><div class="votesBar" id="',
            '',
            '">',
            '',
            '</div><input type="button" value="Pick as Final" class="pickFinalBtn" id="',
            '',
            '"/></div>'
        ];

        var newTermSec = ['<div class="clear"></div><div class="newTermFld"><span class="label">New Term: </span><input type="radio" name="',
            '',
            '" /><input type="text" size="20" value="Enter your suggestion" class="suggTxt leftmargin5" id=" ',
            '',
            '" /></div><div class="clear"></div><div class="commentsFld"><span class="label">Comments: </span><input type="text" size="30" value="" class="commentTxt leftmargin5" /></div> <div class="votesBtn"><input type="submit" value="Submit" class="sbtVoteBtn leftmargin10" id="',
            '',
            '" /><input type="submit" value="Decline to vote" class="rejectTermBtn leftmargin20" id="',
            '',
            '" /></div>'
        ];
        var editDetailsSec = '<div class="clear"></div><div class="editLinksSec smallFont"><a href="javascript:;" class="padding5 extendPoll">Extend poll</a><a href="javascript:;" class="padding5 updateDate nodisplay">Update date</a> | <a href="javascript:;" class="padding5 editDetails">Edit details</a><a href="javascript:;" class="padding5 updateDetails nodisplay">Update details</a></div>';

        var classNames = ['purpleBar', 'greenBar', 'yellowBar', 'pinkBar'];


        var insertCommmas = function (value) {
            var length = value.length;
            var counter = 0;
            var returnValue = "";
            for (var i = length - 1; i >= 0; i--) {
                returnValue = value.charAt(i) + ((counter > 0 && counter % 3 == 0) ? ',' : '') + returnValue;
                counter++;
            }
            return returnValue;
        };


        var displayTeamDetails = function (data) {
            var teamDetailsLst = data;
            var length = null;

            if (showAllTeamMem) {
                length = teamDetailsLst.length;
            } else {
                length = (teamDetailsLst.length >= teamListLimit) ? teamListLimit : teamDetailsLst.length;
            }

            for (var count = 0; count < length; count++) {
                var badgingImg = "";
                var badgingRate = teamDetailsLst[count].badgingRate;

                if (badgingRate >= 0 && badgingRate < 50) {
                    badgingImg = $j("#contextRootPath").val() + "/images/BeginnerBadge.gif";
                } else if (badgingRate >= 50 && badgingRate < 150) {
                    badgingImg = $j("#contextRootPath").val() + "/images/AdvancedBadge.gif";
                } else if (badgingRate >= 150) {
                    badgingImg = $j("#contextRootPath").val() + "/images/ExpertBadge.gif";
                }
                var editImage = $j("#contextRootPath").val() + "/images/Pencil.png";
                var teamListTmplClone = teamListTmpl;
                var totalCount = teamDetailsLst[count].totalVotes;
                var totalTerms1 = new String(totalCount);
                teamListTmplClone[1] = teamDetailsLst[count].userId;
//				teamListTmplClone[3] = teamDetailsLst[count].userId;
                teamListTmplClone[3] = teamDetailsLst[count].userName;
                teamListTmplClone[5] = teamDetailsLst[count].emailId;
                teamListTmplClone[7] = teamDetailsLst[count].createDate;
                teamListTmplClone[9] = (teamDetailsLst[count].accuracy == null) ? "0%" : teamDetailsLst[count].accuracy + "%";
                teamListTmplClone[11] = teamDetailsLst[count].lastMonthVotes;
                teamListTmplClone[13] = insertCommmas(totalTerms1);
                teamListTmplClone[15] = teamDetailsLst[count].companyName;
                teamListTmplClone[17] = teamDetailsLst[count].userRole;
                teamListTmplClone[19] = badgingImg;
                teamListTmplClone[21] = teamDetailsLst[count].userId;
                teamListTmplClone[23] = teamDetailsLst[count].userId;

                $j('#teamRowsList').append(teamListTmplClone.join(""));
            }
            $j('div.userFullName').each(function (i) {
                var origText = $j(this).text();
                var userRegex = /^[a-zA-Z0-9]/;
                if(!userRegex.test(origText) && (origText.length > 10)){
                        var finalText = origText.substring(0, 6);
                        finalText = finalText + "...";
                        $j(this).text(finalText);
                        $j(this).attr("title", origText);
                } else  if (origText.length > 14) {
                    var finalText = origText.substring(0, 12);
                    finalText = finalText + "...";
                    $j(this).text(finalText);
                    $j(this).attr("title", origText);
                }

            });
            $j('div.usrCompany').each(function (i) {
                var origText = $j(this).text();
                if (origText.length > 14) {
                    var finalText = origText.substring(0, 12);
                    finalText = finalText + "...";
                    $j(this).text(finalText);
                    $j(this).attr("title", origText);
                }

            });
            $j('div.emailId').each(function (i) {
                var origText = $j(this).text();
                if (origText.length > 20) {
                    var finalText = origText.substring(0, 18);
                    finalText = finalText + "...";
                    $j(this).text(finalText);
                    $j(this).attr("title", origText);
                }

            });

        };


        ctx.bind("showTeamDetails", function (event, criteria) {
            var pageNum = criteria.pageNum;
            $t.getTeamMemberDetails(criteria.langIds, criteria.companyIds, criteria.colName, criteria.sortOrder, criteria.pageNum, {
                success: function (data) {
                    $j('#teamRowsList').empty();
                    var teamDetails = data;
                    var startLimit = 0;
                    var endLimit = 0;
                    if (teamDetails != null) {
                        var length = (teamDetails.length >= teamListLimit) ? teamListLimit : teamDetails.length;

                        if (pageNum == 0) {
                            totalTerms = teamDetails.length;
                        }
                        noOfPages = Math.round(totalTerms / 10);
                        noOfPages = (totalTerms % 10 < 5 && totalTerms % 10 > 0) ? noOfPages + 1 : noOfPages;

                        if (pageNum == 0) {
                            startLimit = 1;
                            endLimit = (teamListLimit > totalTerms) ? totalTerms : teamListLimit;

                        } else {
                            startLimit = ((pageNum - 1) * teamListLimit) + 1;
                            var tempLimit = (pageNum) * teamListLimit;
                            endLimit = (parseInt(tempLimit) > parseInt(totalTerms)) ? totalTerms : tempLimit;
                        }

                        displayTeamDetails(data);
                        showRolePrivileges();
                        $j("#pagination").show();
                        // To stop rendering pagination prev/next for All Link
                        if (showAllTeamMem) {
                            $j("#pagination").hide();
                        }
                    } else {
                        totalTerms = 0;
                        noOfPages = 0;
                        $j("#pagination").hide();
                        $j('#teamRowsList').html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">No members in the selected language(s)</span>');
                    }


                    $j("#rangeOfList").html(startLimit + "-" + endLimit);
                    var totalTerms3 = insertCommmas(new String(totalTerms));
                    $j("#totalMembers").html(totalTerms3);
                    pagination(noOfPages, pageNum);


                    $j(".rowBg .row .addImg").hover(function () {
                        modalRender.bubble(".rowBg .row .addImg", "Add", "left center", "right center");
                    });
                    $j("#selectAll").click(function (event) {
                        event.stopPropagation();
                        $j('.case').attr('checked', this.checked);

                    });
                    $j('#teamRowsList input[type="checkbox"]').click(function () {
                        var countcheck = $j('#teamRowsList input[type="checkbox"]:checked').length;
                        if (countcheck != length) {
                            $j("#selectAll").attr("checked", false);
                        } else {
                            $j("#selectAll").attr("checked", true);
                        }
                    });
                    $j(".rowBg .row .modifyImg").hover(function () {
                        modalRender.bubble(".rowBg .row .modifyImg", "Edit", "left center", "right center");
                    });
                    $j(".rowBg .row .modifyImg").click(function () {
                        var userRole = "";
                        $j("#user").html("");
                        $j("#user").hide();
                        userId = $j(this).attr("id");
                        $j(".editDetails").html("");
                        var langIds = new Array();
                        var companyIds = new Array();
                        var userLanguageData = null;
                        var userCompanyData = null;
                        $t.getUser(userId, {
                            success: function (data) {
                                if (data.userRole != "") {
                                    var roleId = data.userRole[0].role.roleId;
                                    $j("#mCompanySlct").multiselect("uncheckAll");
                                    $j(".mCompanySlct").hide();
                                    $t.getPrevilegesByRole(roleId, {
                                        success: function (data) {
                                            if (data != null) {
                                                for (var i = 0; i < data.length; i++) {
                                                    $j('.' + data[i].privileges.jsId).show();
                                                }
                                                if (roleId == Constants.ROLES.SUPER_ADMIN) {
                                                    $j(".hideModifyUser").hide();
                                                    $j(".hideMLanguagesSlct").hide();
                                                } else {
                                                    if ($j("#isSuperAdmin").val() == "true") {
                                                        $j(".hideModifyUser").show();
                                                    }
                                                    if (adminHeaderFlag == "false") {
                                                        $j(".hideModifyUser ").hide();
                                                    }
                                                }
                                            }
                                        },
                                        error: function (xhr, textStatus, errorThrown) {
                                            console.log(xhr.responseText);
                                            if (Boolean(xhr.responseText.message)) {
                                                console.log(xhr.responseText.message);
                                            }
                                        }
                                    });
                                }
                                var userTypeId = data.userRole;
                                var userDomainId = data.userDomainId;
                                if (data.company != null) {
                                    var userCompanyId = data.company.companyId;
                                }
                                langIds = data;
                                userLanguageData = data.userLanguages;
                                $t.getLanguageList({
                                    success: function (data) {
                                        displayLanguages(data, "#mlanguagesSlctd");
                                        $j("#mlanguagesSlctd option").each(function (i) {
                                            for (var j = 0; j < userLanguageData.length; j++) {
                                                if ($j(this).val() == userLanguageData[j].languages.languageId) {

                                                    $j(this).attr("selected", "selected");
                                                }
                                            }
                                        });

                                        $j("#mlanguagesSlctd").multiselect().multiselectfilter();
                                        $j("#mlanguagesSlctd").multiselect({
                                            noneSelectedText: 'Select language',
                                            selectedList: 4 // 0-based index
                                        });

                                    }
                                });
                                companyIds = data;
                                userCompanyData = data.companyTransMgmt;
                                $t.getCompanyList({
                                    success: function (data) {
                                        displayCompanyList(data, '#mCompanySlct');
                                        $j("#mCompanySlct option").each(function (i) {
                                            for (var j = 0; j < userCompanyData.length; j++) {
                                                if ($j(this).val() == userCompanyData[j].companyId) {

                                                    $j(this).attr("selected", "selected");
                                                }
                                            }
                                        });

                                        $j("#mCompanySlct").multiselect().multiselectfilter();
                                        $j("#mCompanySlct").multiselect({
                                            noneSelectedText: 'Select company',
                                            selectedList: 3 // 0-based index
                                        });

                                    }
                                });

                                $t.getAllUserTypeList({
                                    success: function (data) {
                                        displayUserTypeList(data, "#mrole");
                                        $j("#mrole option").each(function (i) {
                                            for (var j = 0; j < userTypeId.length; j++) {
                                                if ($j(this).val() == userTypeId[j].role.roleId) {

                                                    $j(this).attr("selected", "selected");
                                                }
                                            }
                                        });

                                    }
                                });


                                $t.getCompanyList({
                                    success: function (data) {
                                        var selectedCompany = userCompanyId;
                                        var companyName = null;
                                        for (var i = 0; i < data.length; i++) {
                                            if (data[i] != null) {
                                                if (selectedCompany == data[i].companyId) {
                                                    companyName = data[i].companyName;
                                                }
                                            }
                                        }
                                        displayCompanyList(data, '#mcompany');
                                        if (roleId == Constants.ROLES.SUPER_TRANSLATOR) {
                                            $j("#mcompany").attr('disabled', 'disabled');
                                        } else {
                                            $j("#mcompany").removeAttr('disabled');
                                        }
                                        $j("select[name='mcompany'] option").each(function () {
                                            if ($j(this).text() == companyName) {
                                                $j(this).attr("selected", "selected");
                                            }
                                        });
                                    },
                                    error: function (xhr, textStatus, errorThrown) {
                                        console.log(xhr.responseText);
                                        if (Boolean(xhr.responseText.message)) {
                                            console.log(xhr.responseText.message);
                                        }
                                    }
                                });

                                displayModifyUserScreen(data);
                                $t.getDomainList({
                                    success: function (data) {
                                        var selectedDomain = userDomainId;
                                        var domainName = null;
                                        for (var i = 0; i < data.length; i++) {
                                            if (data[i] != null) {

                                                if (selectedDomain == data[i].domainId) {
                                                    domainName = data[i].domain;


                                                }
                                            }
                                        }
                                        displayDomain(data);
//							if(domainName==null && userRole != "Business Member"){
//	$j("#mdomain").attr('disabled','disabled');
//}else{
//	$j("#mdomain").removeAttr('disabled');
//							}
                                        $j("select[name='domain'] option").each(function () {
                                            if ($j(this).text() == domainName) {
                                                $j(this).attr("selected", "selected");
                                            }
                                        });

                                    },
                                    error: function (xhr, textStatus, errorThrown) {
                                        console.log(xhr.responseText);
                                        if (Boolean(xhr.responseText.message)) {
                                            console.log(xhr.responseText.message);
                                        }
                                    }
                                });

                            },
                            error: function (xhr, textStatus, errorThrown) {
                                console.log(xhr.responseText);
                                if (Boolean(xhr.responseText.message)) {
                                    console.log(xhr.responseText.message);
                                }
                            }
                        });
                        showModifyUser();
//					}


                    });

                    $j(".rowBg .row .deleteImg").hover(function () {
                        modalRender.bubble(".rowBg .row .deleteImg", "Delete", "left center", "right center");
                    });
                    $j(".rowBg .row .deleteImg").click(function () {
                        var userRole = "";
                        var id = $j(this).attr("id");
                        $j("#user").html("");
                        if ($j("#teamRowsList input:checked").length == 0) {
                            deleteVal(id);
                        }
                    });

                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });

        });

    };


    var insertCommmas = function (value) {
        var length = value.length;
        var counter = 0;
        var returnValue = "";
        for (var i = length - 1; i >= 0; i--) {
            returnValue = value.charAt(i) + ((counter > 0 && counter % 3 == 0) ? ',' : '') + returnValue;
            counter++;
        }
        return returnValue;
    };

    var slctOptionsTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayLanguages = function (data, divClass) {
        var languageList = data;
        for (var count = 0; count < languageList.length; count++) {
            var langSlctTmplClone = slctOptionsTmpl;
            langSlctTmplClone[1] = languageList[count].languageId;
            langSlctTmplClone[3] = languageList[count].languageLabel;
            $j(divClass).append(langSlctTmplClone.join(""));
        }
    };

    var displayEmailTemplateList = function (data) {
        var emailTemplateList = data;
        for (var count = 0; count < emailTemplateList.length; count++) {
            var emailTemplateSlctTmplClone = slctOptionsTmpl;
            emailTemplateSlctTmplClone[1] = emailTemplateList[count].emailTemplateId;
            emailTemplateSlctTmplClone[3] = emailTemplateList[count].emailSubject;
            $j('.emailTemplate').append(emailTemplateSlctTmplClone.join(""));
        }
    };

    var domainTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];


    var displayDomainList = function (data) {
        var domainList = data;
        for (var count = 0; count < domainList.length; count++) {
            var domainTmplClone = domainTmpl;
            domainTmplClone[1] = domainList[count].domainId;
            domainTmplClone[3] = domainList[count].domain;
            jQuery('#domain').append(domainTmplClone.join(""));
        }
    };


    var userTypeTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];


    var displayUserTypeList = function (data, id) {
        var userTypeList = data;
        for (var count = 0; count < userTypeList.length; count++) {
            //if (userTypeList[count].roleId != Constants.ROLES.SUPER_ADMIN) {
                var userTypeTmplClone = userTypeTmpl;
                userTypeTmplClone[1] = userTypeList[count].roleId;
                userTypeTmplClone[3] = userTypeList[count].roleName;
                jQuery(id).append(userTypeTmplClone.join(""));
            //}
        }
    };
    var userRegistration = function () {
        // Create user Object

        var date = new Date();
        var curr_date = date.getDate();
        var curr_month = date.getMonth();
        curr_month = curr_month + 1;
        var curr_year = date.getFullYear();
        date = curr_date + '/' + curr_month + '/' + curr_year;

        // new Object();
        var user = new Object();
        var userLanguages = new Object();
        var userLanguagesList = new Array();
        var companyTransMgmtList = new Array();
        var userRoleList = new Array();
        var companyTransMgmt = new Object();
        var company = new Object();
        if ($j("#company :selected").val() != "" && $j("#company :selected").val() != '0') {
            company.companyId = $j("#company :selected").val();
            companyTransMgmt.companyId = $j("#company :selected").val();
            companyTransMgmt.isExternal = 'N';
            companyTransMgmtList[0] = companyTransMgmt;
        } else {
            company.companyId = 0;
        }
        company.companyName = null;
        company.poc = null;
        company.emailId = null;
        company.createdBy = null;
        company.createDate = null;
        company.updatedBy = null;
        company.updateDate = null;
        company.isActive = 'Y';
        if (values != null && values.length > 0) {
            for (var i = 0; i < values.length; i++) {
//				var roleTypeId = parseInt($j("#role :selected").val());
                var language = new Object();
                language.languageId = values[i];
                language.languageLabel = null;
                language.languageCode = null;
                language.languageNotes = null;
                language.createdBy = null;
                language.createDate = null;
                language.updatedBy = null;
                language.updateDate = null;
                language.isActive = null;
                var languages = Object.toJSON(language);

                userLanguages = {
                    userLangId: null,
                    userId: null,
                    languages: language,
                    createDate: null,
                    createdBy: null,
                    updatedBy: null,
                    updateDate: null,
                    isActive: 'Y'
                }

                userLanguagesList[i] = userLanguages;
            }
        }
        if (cvalues != null && (cvalues.length > 0 || cvalues != '0')) {
            for (var i = 0; i < cvalues.length; i++) {
                companyTransMgmt = new Object();
                companyTransMgmt.companyId = cvalues[i];
                companyTransMgmt.isExternal = 'N';
                var index = i + 1;
                companyTransMgmtList[index] = companyTransMgmt;

            }
        }
        var roleTypeId = parseInt($j("#role :selected").val());

        var roles = new Object();
        roles.roleId = roleTypeId;
        roles.roleName = null;
        roles.createdBy = null;
        roles.createDate = null;
        roles.updatedBy = null;
        roles.updateDate = null;
        roles.isActive = null;
        var roleObj = Object.toJSON(roles);
        var userRole = new Object();
        userRole = {
            userRoleId: null,
            userId: null,
            role: roles,
            createDate: null,
            createdBy: null,
            updatedBy: null,
            updateDate: null,
            isActive: 'Y'
        }

        userRoleList[0] = userRole;
        var firstName = null;
        var lastName = null;
        if ($j("#fName").val() != "") {
            firstName = $j("#fName").val();
        }
        if ($j("#lName").val() != "") {
            lastName = $j("#lName").val();
        }
        user.userId = null;
        user.userName = $j("#username").val();
        user.password = $j("#password").val();
        user.firstName = firstName;
        user.lastName = lastName;
        user.emailId = $j("#email").val();
        user.userRole = userRoleList;
        user.isActive = 'Y';
        user.createTime = null;
        user.createdBy = null;
        user.updatedBy = null;
        user.updateDate = null;
        user.lastLoginTime = null;
        user.wrongPwdEntries = 0;
        user.isTermRequest = false;
        
        user.photoPath = $j("#upldPicture").val();
        if ($j("#domain").val() != "") {
            user.userDomainId = $j("#domain").val();
        } else {
            user.userDomainId = "";
        }

        user.userLanguages = userLanguagesList;
        user.company = company;
        user.companyTransMgmt = companyTransMgmtList;

        var userDtlsParameter = Object.toJSON(user);
        $t.addUser(userDtlsParameter, {
            success: function (data) {
                  var users = null;
                if (data.indexOf("User exists") != -1) {
                    $j("#username").addClass("error");
                    $j(".userExists").show();
                    $j("#userReq").hide();
                    $j(".userExists").html("Username already exists, please choose other username");
                } else {
                    $j(".userExists").hide();
                }
                if (data.indexOf("Email exists") != -1) {
                    $j("#email").addClass("error");
                    $j(".emailExists").show();
                    $j("#emailReq").hide();
                    $j(".emailExists").html("Email id already exists, please choose other email id");
                } else {
                    $j(".emailExists").hide();
                }

                if (data.indexOf("User exists") == -1 && data.indexOf("Email exists") == -1) {
                    alertMessage("#userMessage");
                    $j('#addUser').dialog('close');
                    
                    
                    triggerTeamDetails(savedCriteria.langIds, savedCriteria.companyIds, savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum);

                    $t.getActiveUsersCount({
                        success: function (data) {
                            if (data != null) {
                                var totalTerms1 = new String(data);
                                var totalTerms2 = insertCommmas(totalTerms1);
                                 users=totalTerms2;
                                $j("#totalUsers").html(totalTerms2);
                                
                            }
                            
                        },
                        error: function (xhr, textStatus, errorThrown) {
                            console.log(xhr.responseText);
                            if (Boolean(xhr.responseText.message)) {
                                console.log(xhr.responseText.message);
                            }
                        }
                    });
                    $t.getTotalUsersInSystem({
                        success:function(data){
                            var totalUsersPerMonth = data;
                            var dataXML = new Array();
                            var totaluser = 0;
                            var array = [];
                            
                            if(totalUsersPerMonth != null && totalUsersPerMonth!=""){
                            	
                            	for(var count=0;count<totalUsersPerMonth.length;count++) {
                                    totaluser =  totalUsersPerMonth[4].count;
                                    array.push(totalUsersPerMonth[count].count);
                                }
                            	
                            	if(array[0]==totaluser){
                            		 array.unshift(0);
                            	 }
                            	
                                if ($j("#hpsite").val() == "true")
                                	dataXML.push("<chart showValues='0' adjustDiv='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showBorder='0' bgColor='#f9f9f9,#f9f9f9' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#f9f9f9' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='2' lineColor='#0e94bc' canvasBorderThickness='0'>");
                                else
                                	dataXML.push("<chart showValues='0' adjustDiv='0' showBorder='0' bgColor='#032D39,#032D39' bgAlpha='100,100' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#ffffff' divLineThickness='2' labelPadding='0'  baseFontColor='#C0C0C0' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
                                for(var count=0;count<totalUsersPerMonth.length;count++) {
                                    dataXML.push("<set label='"+totalUsersPerMonth[count].month + "' " + "value='" +totalUsersPerMonth[count].count +"'/>");
                                }
                                dataXML.push("</chart>");
                                ChartRender.twoDLineChart("myChartId14", "240", "70", "userChart", dataXML);
                            }else{
                                $j("#myChartId3").hide();
                                $j("#userChart").html("<div style='text-align:center; padding-top:30px;font-size:11px;color:#fff;width:270px;'>No data to display</div>");
                            }
                        },
                        error: function(xhr, textStatus, errorThrown){
                            console.log(xhr.responseText);
                            if(Boolean(xhr.responseText.message)){
                                console.log(xhr.responseText.message);
                            }
                        }
                    });
            		
                    $j("#username").val('');
                    $j("#password").val('');
                    $j("#fName").val('');
                    $j("#lName").val('');
                    $j("#email").val('');
                    $j("#company").val(0);
                    $j("#languageSlct").multiselect("uncheckAll");
                    $j("#domain").val('');
                    cvalues = null;
                    companyTransMgmtList = null;

//				$j("#role :selected").val('');
                    $j("select[name='role'] option").each(function () {
                        if ($j(this).text() == "Community Member")
                            $j(this).attr("selected", "selected");

                    });
                    $j("#company").removeAttr('disabled');

                    $j("#cnfmPassword").val('');
                    $j(".hideLanguageSlct").show();
                    $j(".addCompanyUser").show();
//				$j("#languageSlct :selected").html('');
                    $j("#companySlct").multiselect("uncheckAll");
                    values = null;
                    

                }
                
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
       

    };


    var clearAddForm = function () {
        $j("#registerForm input.error").removeClass("error");
        $j("#registerForm :text").val('');
        validateUser.resetForm();
        $j("#registerForm .ui-state-default").css("border", "1px solid #dddddd");
        $j('#Action').val(0);
        $j(".languageSlctErr").hide();
        $j("#addUser:ui-dialog").dialog("destroy");

    }

    var showDialog = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#addUser:ui-dialog").dialog("destroy");

        $j("#addUser").dialog({
            height: 430,
            width: 700,
            modal: true,
            close: function (event, ui) {
                clearAddForm();
                $j(".userExists").hide();
                $j(".emailExists").hide();
                $j("#languageSlct").multiselect("uncheckAll");
                $j("#password").val("");
                $j("#cnfmPassword").val("");
                $j(".companySlct").hide();
                $j("#role").val(Constants.ROLES.COMMUNITY_MEMBER);
                showRolePrivileges();
                $j(".hideLanguageSlct").show();
                $j("#company").removeAttr('disabled');
                $j("#company").val(0);
            }
        });
    };


    var showModifyUser = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#ModifyUser:ui-dialog").dialog("destroy");

        $j("#ModifyUser").dialog({
            height: 330,
            width: 700,
            modal: true,
            close: function (event, ui) {
                $j("#ModifyUser").dialog("destroy");
            }

        });
    };


    var insertCommmas = function (value) {
        var length = value.length;
        var counter = 0;
        var returnValue = "";
        for (var i = length - 1; i >= 0; i--) {
            returnValue = value.charAt(i) + ((counter > 0 && counter % 3 == 0) ? ',' : '') + returnValue;
            counter++;
        }
        return returnValue;
    };
    /**
     var userDetailTmpl = ['<div class="userImg"><img src="',
     '',
     '" height="50px" width="50px" /></div><div class="userDetails"><h5>',
     '',
     '</h5>',
     '<p class="smallFont">Member since: ',
     '',
     '</p></div>'
     ];
     var displayUserDetails = function(data,id) {
		var userDetails = data;
		var userDetailTmplClone = userDetailTmpl;
		var count=userDetails.totalVotes;
		var totalTerms1 = new String(count);
		userDetailTmplClone[1] = userDetails.photoPath;
		userDetailTmplClone[3] = userDetails.userName;
		userDetailTmplClone[6] = userDetails.createDate;
		$j(id).append(userDetailTmplClone.join(""));
	};
     */
    var domainTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];
    var displayDomain = function (data) {
        var domainList = data;
        for (var count = 0; count < domainList.length; count++) {
            var domainTmplClone = domainTmpl;
            domainTmplClone[1] = domainList[count].domainId;
            domainTmplClone[3] = domainList[count].domain;
            $j('#mdomain').append(domainTmplClone.join(""));
        }
    };

    var displayCompanyList = function (data, appenderClass) {
        var companyList = data;
        for (var count = 0; count < companyList.length; count++) {
            var companyTmplClone = domainTmpl;
            companyTmplClone[1] = companyList[count].companyId;
            companyTmplClone[3] = companyList[count].companyName;
            $j(appenderClass).append(companyTmplClone.join(""));
        }
    };


    var modifyUserTmpl = ['<form id="modifyUser"><div class="boxWidth320"><div style="padding-bottom: 20px;"><div class="label width110 floatleft">First name:</div> <div class="floatleft" style="width:200px;"><input type="text" id="mfName" size="25" class="text190" name="fName"  tabindex="1" value="',
        '',
        '" /> </div></div><br class="clear" /><label for="muserName" class="error floatright nodisplay" style="width:212px;margin-right:-9px;margin-top:-31px;" generated="true"></label><div class="clear"></div><div class="error modUserExists nodisplay" style="width:195px;margin-right:10px;padding-left:115px;margin-top: -15px;"></div> <div style="padding-bottom: 20px;"><div class="label width110 floatleft">Username<span class="redTxt">*</span> </div><div class="floatleft" style="width:200px;"><input type="text"  id="muserName" size="25" class="text190" name="muserName" tabindex="3"  value="',
        '',
        '"/></div> </div><br class="clear" /><label for="memail" class="error floatright nodisplay" style="width:212px;margin-right:-7px;margin-top:-30px;" generated="true"></label><div class="clear"></div><div class="error modEmailExists nodisplay" style="width:195px;margin-right:10px;padding-left:115px;margin-top: -15px;"></div><div style="padding-bottom: 20px;"><div class="label width110 floatleft">Email<span class="redTxt">*</span> </div><div class="floatleft" style="width:200px;"> <input type="text" id="memail"  size="25" class="text190" name="memail" tabindex="4" value="',
        '',
        '" /></div> </div> <br class="clear" /><div class="error passwordErr floatright nodisplay" style="width:212px;margin-right:-7px;margin-top:-16px;" >Password should be alphanumeric with at least one special character</div><div class="clear"></div><div style="padding-bottom: 20px;"><div class="label width110 floatleft">Password:</div><div class="floatleft" style="width:200px;"> <input type="password"  id="mpwd"   class="text190" name="mpwd" tabindex="5" value="',
        '',
        '" /></div> </div> <br class="clear" /><div class="error cpasswordReq floatright nodisplay" style="width:212px;margin-right:-7px;margin-top:-15px;" >Password is required</div><div class="error cpasswordErr floatright nodisplay" style="width:212px;margin-right:-7px;margin-top:-15px;" >Enter the same password as above</div><div class="clear"></div><div style="padding-bottom: 20px;"><div class="label width110 floatleft">Confirm password:</div><div class="floatleft" style="width:200px;"> <input type="password"  id="mcpwd"   class="text190" name="mcpwd" tabindex="5" value="',
        '',
        '" /></div> </div> <br class="clear" /></div><div class="boxWidth320"><div style="padding-bottom: 20px;"><div class="label width90 floatleft">Last name:</div> <div class="floatleft" style="width:200px;"><input type="text" id="mlName" size="25" class="text190" name="lName"  tabindex="2" value="',
        '',
        '" /> </div></div><br class="clear" /><div style="padding-bottom: 20px;"><div class="label width90 floatleft">Role:</div><div class="floatleft" style="width:200px;"><select id="mrole" class="text190" name="role"  class="role" tabindex="5"> <option value="">---Select role---</option></select></div> </div><br class="clear" /><div style="padding-bottom: 25px;" class="hideMLanguagesSlct"><div class="label width90 floatleft">Languages:</div><div class="floatleft"><select id="mlanguagesSlctd" name="languageSlct" multiple="multiple"  class="text220" tabindex="9"></select></div></div><br class="clear" /><div class=" mainDomainDiv"><div class="label width90">Domain<span class=" domainReq"></span>:</div><select  class="text220" id="mdomain" name="domain"> <option value="">---Select domain---</option></select> </div><br class="clear" /><div style="padding-bottom: 30px;" class="modifyAllUser hideModifyUser nodisplay"><div class="label width90 floatleft ">Company:</div><div class="floatleft" style="width:200px;"><select id="mcompany"  disabled class="text190" name="mcompany"  class="company" tabindex="5"> <option value="">---Select company---</option></select></div></div><div style="padding-bottom: 25px;" class="bottommargin15 nodisplay voteForMultipleCompanies mCompanySlct"><div class="label width90 floatleft">Companies voted for:</div><div class="floatleft"><select id="mCompanySlct" name="mCompanySlct" multiple="multiple"  class="text220" ></select></div></div><div class="clear"></div></form> '
    ];
    var displayModifyUserScreen = function (data) {
        var userDetails = data;
        var firstName = ((userDetails.firstName) == null || (userDetails.firstName) == "null") ? " " : userDetails.firstName;
        var lastName = ((userDetails.lastName) == null || (userDetails.lastName) == "null") ? " " : userDetails.lastName;
        var modifyUserTmplClone = modifyUserTmpl;
        modifyUserTmplClone[1] = firstName;
        modifyUserTmplClone[3] = userDetails.userName;
        modifyUserTmplClone[5] = userDetails.emailId;
        modifyUserTmplClone[7] = "";
        modifyUserTmplClone[9] = "";
        modifyUserTmplClone[11] = lastName;
        $j(".editDetails").append(modifyUserTmplClone.join(""));

        showRolePrivileges();
        $j("#mrole").change(function () {
            var roleId = $j(this).val();
            if (roleId != Constants.ROLES.SUPER_ADMIN) {
                $j(".companySlctErr").hide();
                $j("#company").removeClass("error");
                $t.getCompanyList({
                    success: function (data) {
                        displayCompanyList(data, "#mcompany");
                        $j("select[name='mcompany'] option").each(function () {
                            if ($j(this).text() == "Welocalize")
                                $j(this).attr("selected", "selected");
                            $j(".companySlctErr").hide();
                            $j("#company").removeClass("error");

                        });

                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });

                $j("#mcompany").attr('disabled', 'disabled');

            }
            else {
                $j("#mcompany").html("");
                $j("#mcompany").html('<option>---select---</option>')
                $j("#mcompany").removeAttr('disabled');

            }
            if (roleId != Constants.ROLES.SUPER_TRANSLATOR) {
                $t.getUser(userId, {
                    success: function (data) {
                        if (data.company != null) {
                            var userCompanyId = data.company.companyId;
                        }
                        $t.getCompanyList({
                            success: function (data) {
                                var selectedCompany = userCompanyId;
                                var companyName = null;
                                for (var i = 0; i < data.length; i++) {
                                    if (data[i] != null) {

                                        if (selectedCompany == data[i].companyId) {
                                            companyName = data[i].companyName;


                                        }
                                    }
                                }
                                displayCompanyList(data, '#mcompany');
                                if (roleId == Constants.ROLES.SUPER_TRANSLATOR) {
                                    $j("#mcompany").attr('disabled', 'disabled');
                                } else {
                                    $j("#mcompany").removeAttr('disabled');
                                }
                                $j("select[name='mcompany'] option").each(function () {
                                    if ($j(this).text() == companyName) {
                                        $j(this).attr("selected", "selected");
                                    }
                                });

                            },
                            error: function (xhr, textStatus, errorThrown) {
                                console.log(xhr.responseText);
                                if (Boolean(xhr.responseText.message)) {
                                    console.log(xhr.responseText.message);
                                }
                            }
                        });


                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });
            }
            $j("#mCompanySlct").multiselect("uncheckAll");
            $j(".mCompanySlct").hide();
            $t.getPrevilegesByRole(roleId, {
                success: function (data) {
                    if (data != null) {
                        for (var i = 0; i < data.length; i++) {
                            $j('.' + data[i].privileges.jsId).show();
                        }
                        if (roleId == Constants.ROLES.SUPER_ADMIN) {
                            $j(".hideModifyUser").hide();
                            $j(".hideMLanguagesSlct").hide();
                        } else {
                            if ($j("#isSuperAdmin").val() == "true") {
                                $j(".hideModifyUser").show();
                            } else
                                $j(".hideModifyUser ").hide();
                            $j(".hideMLanguagesSlct").show();
                        }
                    }
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });

        });
        $j("#memail").focus(function () {
            $j(".modEmailExists").hide();
            $j("#emailReq").hide();
        });
        $j("#muserName").focus(function () {
            $j(".modUserExists").hide();
            $j("#userReq").hide();

        });
    };

    var userLangTmpl = ['<span id="',
        '',
        '" class=" hand link">',
        '',
        '</span>'
    ];

    var displayUserLanguages = function (data) {
        var userLangList = data;
        if (userLangList != null) {
            defaultSelectedLang = userLangList[0].languageId;
//
//			if(defaultSelectedLang != 0){
//			$j("#manageTeamTbl").teamDetails();
//			triggerTeamDetails(defaultSelectedLang,0, null, null, 0);
//			}
            $j("#manageTeamTbl").teamDetails();
            triggerTeamDetails(defaultSelectedLang, 0, "dateOfCreation", "DESC", 0);
            $j("#dateOfCreation").append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');


            for (var count = 0; count < userLangList.length; count++) {
                var userLangTmplClone = userLangTmpl;
                if (count == 0) {
                    userLangTmplClone[1] = 'defaultLang" value="' + userLangList[count].languageId;

                } else {
                    userLangTmplClone[1] = userLangList[count].languageId;
                }
                userLangTmplClone[3] = userLangList[count].languageLabel;
                $j('.langMenu').append(userLangTmplClone.join(""));
            }
            $j('.langMenu').append('<span id="all" class=" hand link" style="border-right: medium none;">All</span>');

        } else {
            $j('.langMenu').html("<span></span>");
            $j("#manageTeamTbl").teamDetails();
            triggerTeamDetails(0, 0, null, null, 0);
        }
    };

    var userListTmpl = ['<div class="userImportTblRow"><div class="sourceName width189">',
        '',
        '</div><div class="width110">',
        '',
        '</div></div>'
    ];

    var displayFailedImportList = function (data) {
        userNames = data.userNames;
        lineNumbers = data.lineNumbers;
        $j('.userImportTblBody').html("");
        var userListTmplClone = userListTmpl;
        for (var i = 0; i < userNames.length; i++) {
            userListTmplClone[1] = userNames[i];
            userListTmplClone[3] = lineNumbers[i];
            $j('.userImportTblBody').append(userListTmplClone.join(""));
        }

        $j('div.sourceName').each(function (i) {
            var origText = $j(this).text();
            if (origText.length > 30) {
                var finalText = origText.substring(0, 20);
                finalText = finalText + "...";
                $j(this).text(finalText);
                $j(this).attr("title", origText);
            }
        });
    }

    var getSlctdLang = function () {
        $j("#langs span").first().addClass("on");
        $j("#langs span").first().removeClass("link");
        $j("#langs span").last().css("border-right", "none");

        $j("#langs span").click(function () {
            if ($j(this).hasClass("on"))
                return;
            selectedLangIds = $j(this).attr("id");
            langsClicked = true;
            $j("#mainLanguageSlct").multiselect("uncheckAll");
            if (selectedLangIds == "defaultLang") {
                selectedLangIds = $j('#defaultLang').attr('value');
            }

            //to stop sorting for all the divs inside the teamListSectionHead div

            $j('#teamListSectionHead div').find('.sort').remove();
            $j("#dateOfCreation").append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');


            if (selectedLangIds.indexOf('all') > -1) {
                allClicked = true;
                showAllTeamMem = true;

            } else {
                showAllTeamMem = false;
            }

            if ($j("#hpsite").val() == "true")
                MultiCValues = 2;

            if (MultiCValues != 0 && MultiCValues != null && selectedLangIds != 0 && selectedLangIds != null) {
                triggerTeamDetails(selectedLangIds, MultiCValues, "dateOfCreation", "DESC", 0);
            } else if (selectedLangIds != null && selectedLangIds != 0) {
                triggerTeamDetails(selectedLangIds, 0, "dateOfCreation", "DESC", 0);
            }
            $j("#langs span").each(function (index) {
                $j(this).removeClass("on");
                $j(this).addClass("link");
            });

            $j(this).addClass("on");
        });
    };


    var showLoadingDialog = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#loading:ui-dialog").dialog("destroy");

        $j("#loading").dialog({
            height: 120,
            width: 200,
            modal: true,
            resizable: false
        });
    };
    var showImprtUserDialog = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#importUserBrwse:ui-dialog").dialog("destroy");

        $j("#importUserBrwse").dialog({
            height: 500,
            width: 400,
            modal: true,
            close: function (event, ui) {
                $j("#userImport").val("");
                $j("#userImprtInfo").hide();
                $t.getActiveUsersCount({
                    success: function (data) {
                        if (data != null) {
                            $j("#totalUsers").html(data);
                        }

                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });
                triggerTeamDetails(0, 0, null, null, 0);


            }
        });
    };

    var uploadStatus = function () {
        $t.getUploadStatus({
            success: function (data) {
                if (data != null) {
                    $j("#successAddedUsers").html("");
                    $j("#failedAddedUsers").html("");
                    $j("#userImprtInfo").show();
                    if (data.insertedCount > 0) {
                        $j(".failedInvalidInfo").hide();
                        $j("#successAddedUsers").html("Successfully imported users:" + data.insertedCount);
                    }
                    if (data.rejectedCount == 0) {
                        if (data.termInformationStatus == "failed") {
//						$j(".failedInvalidInfo").hide();
//						$j(".failedInfo").hide();
                        } else {
                            $j(".failedInvalidInfo").hide();
                            $j(".failedInfo").hide();
                        }
                    }
                    else if ((data.rejectedCount == null && data.insertedCount == null && data.updatedCount == null)) {
                        $j(".failedInvalidInfo").show();
                        $j(".failedInfo").hide();
                    }
                    else {
                        $j(".failedInvalidInfo").hide();
                        $j(".failedInfo").show();
                        $j("#failedAddedUsers").html(data.rejectedCount);
                        displayFailedImportList(data);
                    }
                    if(data.termInformationStatus == "Success"){
                    	 $j(".failedInvalidInfo").hide();
                    	 $j(".failedInfo").hide();
                    	 $j("#failedAddedUsers").html("");
                    }
                } else {
                    setTimeout(function () {
                        uploadStatus();
                    }, 100);
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
    };

    var showInviteDialog = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#invitation:ui-dialog").dialog("destroy");

        $j("#invitation").dialog({
            height: 330,
            width: 550,
            modal: true,
            close: function (event, ui) {
                $j(".emailTemplate").val("");
                $j(".emailPrvw").html("");
                $j('.emailTemplate').removeClass("errorBorder");
                $j("#invitation").dialog("destroy");
            }
        });
    };

    $j("#cancelInvitMail").click(function () {
        $j('#invitation').dialog('destroy');
        $j(".emailTemplate").val("");
        $j(".emailPrvw").html("");
        $j('.emailTemplate').removeClass("errorBorder");
    });


    $j(".importUsers").click(function () {
        showImprtUserDialog();

    });

    $j("#importUserFile").click(function () {
        if ($j.trim($j("#userImport").val()) != "") {
            uploadStatus();
            $j("#ImportUserForm").submit();
        }
        else {
            alertMessage("#importMessage");
        }

    });

    var closeLoadingDialog = function (dialogId) {
        $j(dialogId).html("");
        $j(dialogId).dialog('destroy');
    };

    var alertMessage = function (alertId) {
        $j(alertId + ":ui-dialog").dialog("destroy");
        $j(alertId).dialog({
            height: 140,
            width: 300,
            modal: true,
            buttons: {
                OK: function () {
                    $j(this).dialog("close");
                }
            }
        });
    };

    $j('#sendMail').click(function () {

        var emailIdsTxt = $j('#emailIds').val();
        $j("#templateReq").hide();
        $j('.emailTemplate').removeClass("errorBorder");
        if (validateForm(emailIdsTxt)) {
            if ($j.trim(emailIdsTxt) == "") {
                alertMessage("#mailMessage");
            } else {
                showInviteDialog();
                var emailTemplateId = $j('.emailTemplate :selected').val();
            }
        }
    });

    $j("#inviteMail").click(function () {
        var emailIdsTxt = $j('#emailIds').val();
        var emailArray = new Array();
        var emailTemplateId = $j('.emailTemplate :selected').val();
        var flag = true;
        if (emailIdsTxt.indexOf(",") >= 0) {
            emailArray = emailIdsTxt.split(",");
        } else if (emailIdsTxt.indexOf(";") >= 0) {
            emailArray = emailIdsTxt.split(";");
        } else if (emailIdsTxt.indexOf(",") == -1 || emailIdsTxt.indexOf(";") == -1) {
            emailArray[0] = emailIdsTxt;
        }

        if ($j('.emailTemplate :selected').val() == 0) {
            $j("#templateReq").show();
            $j('.emailTemplate').addClass("errorBorder");
            flag = false;
        }

        if (flag) {
            $j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif"  align="top" class="rightmargin10" />&nbsp;Sending mail... please wait</div>');
            showLoadingDialog();
            $j(".ui-dialog-titlebar").hide();
            setTimeout(function () {
                mailSentSuccess(emailIdsTxt);
                closeLoadingDialog('#loading');
                $j('#emailIds').val('Enter email addresses...');
            }, 200);
            var invite = new Object();
            var inviteIds = emailArray;
            invite.mailIds = inviteIds;
            invite.emailTemplateId = emailTemplateId;
            invite.termIds = new Array();
            invite.userIds = new Array();
            invite.votingPeriod = 0;
            var inviteParameter = Object.toJSON(invite);
            $t.invitePeople(inviteParameter, {
                success: function (data) {
                    $j(".emailTemplate").val("");
                    $j(".emailPrvw").html("");
                    $j("#templateReq").hide();
                    $j('.emailTemplate').removeAttr("border");
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });
        }

    });

    $j(".addUser").click(function () {
        if (flag == true) {
            if (adminHeaderFlag == "true") {
                if ($j("#company :selected").val() == 0) {
                    $j(".companySlctErr").show();
                    $j("#company").addClass("error");
                } else {
                    $j(".companySlctErr").hide();
                    $j("#company").removeClass("error");
                }
            }
            if ($j("#registerForm").valid()) {
                if (values == null || values == 0) {
                    $j(".teamselecterror").show();
                    //$j(".regFrm .ui-state-default").css("border", "1px solid red");
                    //$j(".regFrm .companySlct .ui-state-default").css("border", "1px solid #BBBBBB");
                    return;
                } else {
//                    $j(".regFrm .ui-state-default").css("border", "1px solid blue");
                    $j(".teamselecterror").hide();
                }
                if (adminHeaderFlag == "true") {
                    //if($j("#company :selected").val()!=0){
                    userRegistration();
//					}else{
//						$j(".companySlctErr").show();
//						$j("#company").addClass("error");
//					}
                } else {
                    userRegistration();
                }
            }
            if (adminHeaderFlag == "true") {
//				if(cvalues == null || cvalues ==0 ){
//				$j(".companySlctErr").show();
//				$j("#company").addClass("error");
////				$j(".regFrm #company .ui-state-default").css("border","1px solid red");
//				$j(".regFrm .companySlct .ui-state-default").css("border","1px solid #BBBBBB");
//				}else{
//				$j("#company").css("border","1px solid blue");
//				$j(".regFrm .ui-state-default").css("border","1px solid blue");
//				$j(".companySlctErr").hide();
//				}
            }
        }
        if (flag == false) {

            if ($j("#registerForm").valid()) {

                userRegistration();
            }
        }


    });
    $j("#submitModUser").click(function () {
        var flag = true;
        validateModifyUser = $j("#modifyUser").validate({
            rules: {
                muserName: {
                    required: true,
                    minlength: 2
                },
                memail: {
                    required: true,
                    email: true
                }
            },
            messages: {
                muserName: {
                    required: "<br/>Username is required",
                    minlength: "<br/>Username must consist of at least 2 characters"
                },
                memail: "<br/>Enter a valid email address"
            }
        });

        var firstName = $j("#mfName").val();
        var lastName = $j("#mlName").val();
        var password = $j("#mpwd").val();
        var confirmPwd = $j("#mcpwd").val();
        var flag = true;
        if ($j.trim(firstName) == "") {
            firstName = null;
        }
        if ($j.trim(lastName) == "") {
            lastName = null;
        }
        if ($j.trim(password) == "") {
            password = null;
            $j(".cpasswordReq").hide();
            $j(".passwordErr").hide();
            $j(".cpasswordErr").hide();
            $j("#mpwd").css("border", "1px solid #DDDDDD");
            $j("#mcpwd").css("border", "1px solid #DDDDDD");
            $j("#mcpwd").val("");
            flag = true;
        } else {
            if (!(password.match(/[a-zA-Z]/) && password.match(/[0-9]/) && password.match(/[!@#$%^&*()_-]/))) {
                $j(".passwordErr").show();
                $j(".cpasswordErr").hide();
                $j("#mpwd").css("border", "1px solid red");
                flag = false;
            } else if ($j.trim(confirmPwd) == "") {
                password = null;
                $j(".cpasswordReq").show();
                $j(".passwordErr").hide();
                $j(".cpasswordErr").hide();
                $j("#mpwd").css("border", "1px solid #DDDDDD");
                $j("#mcpwd").css("border", "1px solid red");
                flag = false;

            } else if (password != confirmPwd) {
                $j(".passwordErr").hide();
                $j("#mpwd").css("border", "1px solid #DDDDDD");
                $j(".cpasswordErr").show();
                $j(".cpasswordReq").hide();
                $j("#mcpwd").css("border", "1px solid red");
                flag = false;
            } else {
                $j(".cpasswordErr").hide();
                $j(".cpasswordReq").hide();
                $j("#mcpwd").css("border", "1px solid #DDDDDD");
                flag = true;
            }

        }

        var userName = $j("#muserName").val();
        var emailId = $j("#memail").val();
        var userLanguages = new Array();
        var userTypeId = $j("#mrole").val();
        if (userTypeId == Constants.ROLES.SUPER_ADMIN) {
            userLanguages = null;
        } else {
            userLanguages = $j("#mlanguagesSlctd").val();
        }
        if ($j("#mdomain").val() == "") {
            var userDomainId = 0;
        } else {
            var userDomainId = $j("#mdomain").val();
        }

        if ($j("#mcompany").val() == "") {
            var companyId = 0;
        } else {
            var companyId = $j("#mcompany").val();
        }

        var userCompanyList = new Array();
        userCompanyList = $j("#mCompanySlct").val();
        if ($j("#modifyUser").valid()) {
            if (flag == true) {
                $t.updateUser(userId, firstName, lastName, userName, emailId, password, userLanguages, userTypeId, userDomainId, companyId, userCompanyList, {
                    success: function (data) {
                        if (data.indexOf("User exists") != -1) {
                            $j("#userReq").hide();
                            $j(".modUserExists").show();
                            $j('#muserName').addClass("error");
                            $j(".modUserExists").html("Username already exists, please choose other username");

                        } else {
                            $j(".modUserExists").hide();
                        }
                        if (data.indexOf("Email exists") != -1) {
                            $j(".modEmailExists").show();
                            $j('#memail').addClass("error");
                            $j("#emailReq").hide();
                            $j(".modEmailExists").html("Email id already exists, please choose other email id");
                        } else {
                            $j(".modEmailExists").hide();
                        }

                        if (data.indexOf("User exists") == -1 && data.indexOf("Email exists") == -1) {
                            $j('#ModifyUser').dialog('destroy');
                            triggerTeamDetails(savedCriteria.langIds, savedCriteria.companyIds, savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum);
                        }


                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }

                });
            }
        }

    });


    var triggerTeamDetails = function (langIds, companyIds, colName, sortOrder, pageNum) {
    	
    	$t.getTotalUsersInSystem({
            success:function(data){
                var totalUsersPerMonth = data;
                var dataXML = new Array();
                var totaluser = 0;
                var array = [];
                
                if(totalUsersPerMonth != null && totalUsersPerMonth!=""){
                	
                	for(var count=0;count<totalUsersPerMonth.length;count++) {
                        totaluser =  totalUsersPerMonth[4].count;
                        array.push(totalUsersPerMonth[count].count);
                    }
                	
                	if(array[0]==totaluser){
                		 array.unshift(0);
                	 }
                	
                    if ($j("#hpsite").val() == "true")
                    	dataXML.push("<chart showValues='0' adjustDiv='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showBorder='0' bgColor='#f9f9f9,#f9f9f9' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#f9f9f9' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='2' lineColor='#0e94bc' canvasBorderThickness='0'>");
                    else
                    	dataXML.push("<chart showValues='0' adjustDiv='0' showBorder='0' bgColor='#032D39,#032D39' bgAlpha='100,100' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#ffffff' divLineThickness='2' labelPadding='0'  baseFontColor='#C0C0C0' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
                    for(var count=0;count<totalUsersPerMonth.length;count++) {
                        dataXML.push("<set label='"+totalUsersPerMonth[count].month + "' " + "value='" +totalUsersPerMonth[count].count +"'/>");
                    }
                    dataXML.push("</chart>");
                    ChartRender.twoDLineChart("myChartId15", "240", "70", "userChart", dataXML);
                }else{
                    $j("#myChartId3").hide();
                    $j("#userChart").html("<div style='text-align:center; padding-top:30px;font-size:11px;color:#fff;width:270px;'>No data to display</div>");
                }
            },
            error: function(xhr, textStatus, errorThrown){
                console.log(xhr.responseText);
                if(Boolean(xhr.responseText.message)){
                    console.log(xhr.responseText.message);
                }
            }
        });
		
        if (colName == null) {
            $j("#teamListSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
        }
        $j('#teamRowsList').empty();
        ChartRender.destroyTwoDPieChart();
        var criteria = {
            'colName': colName,
            'sortOrder': sortOrder,
            'langIds': langIds,
            'companyIds': companyIds,
            'pageNum': pageNum
        };
        savedCriteria = criteria;
        $j("#manageTeamTbl").trigger("showTeamDetails", criteria);
    };

    $j(".next").click(function () {
        $j('#selectAll').attr('checked', false);
        if ($j(".next").hasClass("nextEnable")) {
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var langIds = savedCriteria.langIds;
            var companyIds = savedCriteria.companyIds;
            var pageNum = (savedCriteria.pageNum == 0) ? (savedCriteria.pageNum + 2) : (savedCriteria.pageNum + 1);
            savedCriteria.pageNum = pageNum;
            triggerTeamDetails(langIds, companyIds, colName, sortOrder, pageNum);
        }
        ;
    });

    $j(".previous").click(function () {
        $j('#selectAll').attr('checked', false);
        if ($j(".previous").hasClass("prevEnable")) {
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var langIds = savedCriteria.langIds;
            var companyIds = savedCriteria.companyIds;
            var pageNum = savedCriteria.pageNum - 1;
            triggerTeamDetails(langIds, companyIds, colName, sortOrder, pageNum);
        }

    });

    $j("#teamListSectionHead div").click(function () {
        var sortOrder = $j(this).attr('sortOrder');
        var colName = $j(this).attr('id');
        if ( (colName == "column0") ||  (colName == "badging") ||
            (colName == "modify") || (colName == "delete")) {
            return;
        }
        if (!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))) {
            $j("#teamListSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
            $j(this).attr('sortOrder', 'DESC');
            $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
            $j(this).addClass("ascending");
        } else if ($j(this).hasClass("ascending")) {
            $j("#teamListSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
            $j(this).attr('sortOrder', 'ASC');
            $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
            $j(this).addClass("descending");
        } else if ($j(this).hasClass("descending")) {
            $j("#teamListSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
            $j(this).attr('sortOrder', 'DESC');
            $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
            $j(this).addClass("ascending");
        }
        if (selectedLangIds == 0) {
            defaultSelectedLang = $j('#defaultLang').attr('value');
        } else {
            defaultSelectedLang = selectedLangIds;
        }
        triggerTeamDetails(savedCriteria.langIds, savedCriteria.companyIds, colName, sortOrder, 1);

    });

    var pagination = function (noOfPages, pageNum) {
        if ((noOfPages > 1) && (pageNum < noOfPages)) {
            $j(".next").addClass("nextEnable");
            $j(".next").removeClass("nextDisable");
        } else {
            $j(".next").removeClass("nextEnable");
            $j(".next").addClass("nextDisable");
        }

        if ((noOfPages > 1) && (pageNum > 1)) {
            $j(".previous").removeClass("prevDisable");
            $j(".previous").addClass("prevEnable");
        } else {
            $j(".previous").removeClass("prevEnable");
            $j(".previous").addClass("prevDisable");
        }
    };


    var showExportUsers = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#exportUserFilter:ui-dialog").dialog("destroy");

        $j("#exportUserFilter").dialog({
            height: 300,
            width: 500,
            modal: true,
            close: function (event, ui) {
                $j("#roleSlct").val('');
                $j("#exprtlangSlct").multiselect("uncheckAll");
                $j('#exprtcompanySlct').multiselect("uncheckAll");
            }


        });
    };

    var deleteUserDetails = function (selectedUserIds) {
        var userIdsArray = selectedUserIds.split(',');
        $t.deleteUsers(userIdsArray, {
            success: function (data) {
                $t.getActiveUsersCount({
                    success: function (data) {
                        if (data != null) {
                            var totalTerms1 = new String(data);
                            var totalTerms2 = insertCommmas(totalTerms1);
                            $j("#totalUsers").html(totalTerms2);
                            totalTerms = totalTerms - userIdsArray.length;
                            var pageNum1 = savedCriteria.pageNum;
                            var totalTermsTillBefore = (pageNum1 - 1) * teamListLimit;
                            if (totalTermsTillBefore == totalTerms) {
                                savedCriteria.pageNum = pageNum1 - 1;
                            }
                            $j('#selectAll').attr('checked', false);
                            triggerTeamDetails(savedCriteria.langIds, savedCriteria.companyIds, savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum);
                            alertMessage("#deleteUsers");
                        }

                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });

            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
    };
    var deleteVal = function (id) {
        $j("#user").html("");
        $j("#delete_cnfm:ui-dialog").dialog("destroy");
        $j("#delete_cnfm").dialog({
            resizable: false,
            height: 150,
            width: 400,
            modal: true,
            buttons: {
                "Delete": function () {
                    $j("#user").hide();

                    selectedUserIds = id;
                    deleteUserDetails(selectedUserIds);
                    $j(this).dialog("close");

                },
                Cancel: function () {
                    $j('#Action').val(0);
                    $j(this).dialog("close");
                }
            }
        });
    };


    var deleteUserVal = function () {
        $j("#user").html("");
        $j("#delete_cnfm:ui-dialog").dialog("destroy");
        $j("#delete_cnfm").dialog({
            resizable: false,
            height: 150,
            width: 400,
            modal: true,
            buttons: {
                "Delete": function () {
                    $j("#user").hide();
                    var separator = ",";
                    selectedUserIds = "";
                    $j("#teamRowsList input:checked").each(function (i) {
                        selectedUserIds += $j(this).parent().next().attr("userId") + separator;
                        $j(this).parent().parent().remove();
                        var viewNode = $j(this).parent().parent().next().next();
                        viewNode.remove();
                        $j(this).parent().parent().remove();
                        $j(this).parent().parent().remove();
                    });


                    selectedUserIds = selectedUserIds.substr(0, selectedUserIds.lastIndexOf(separator));
                    deleteUserDetails(selectedUserIds);


                    $j(this).dialog("close");
                    $j('#Action').val(0);

                },
                Cancel: function () {
                    $j('#Action').val(0);
                    $j(this).dialog("close");
                }
            }
        });
    };

    var mailSentSuccess = function (emailTxt) {
        $j("#mail_success:ui-dialog").dialog("destroy");
        $j("#newUserEmail").html(emailTxt);
        $j("#mail_success").dialog({
            height: 200,
            width: 350,
            modal: true,
            buttons: {
                OK: function () {
                    $j(this).dialog("close");
                    $j("#invitation:ui-dialog").dialog("destroy");
                }
            }
        });
    }

    var mailSentFailed = function (emailTxt) {
        $j("#mail_failed:ui-dialog").dialog("destroy");
        $j("#failedUserEmail").html(emailTxt);
        $j("#mail_failed").dialog({
            height: 200,
            width: 350,
            modal: true,
            buttons: {
                OK: function () {
                    $j(this).dialog("close");
                }
            }
        });
    }


    $j("#deleteMultipleUsers").click(function () {
        if ($j("#teamRowsList input:checked").length == 0) {
            alertMessage("#userSlctMessage");
        } else {
            deleteUserVal();
        }
    });
    $j("#addUsers").click(function () {
        var userRole = "";
        values = 0;
        cvalues = 0;
        $j(".companySlct").hide();
        $j("#domain").val('');
        $j("#company").val(0);
        $j(".companySlctErr").hide();
        $j("#company").removeClass("error");
        showDialog();
        $j("#hideCompanySlct").hide();
        
        var selRoleTypeId = parseInt($j("#role :selected").val());
       /* $t.getPrevilegesByRole(selRoleTypeId, {
        	success: function (data) {
                if (data != null) {
                    for (var i = 0; i < data.length; i++) {
                    	if(data[i].privileges.jsId == "voteForMultipleCompanies") {
                    		console.log(data[i].privileges.jsId);
                    		alert(data[i].privileges.jsId);
                    		$j('.' + data[i].privileges.jsId).show();
                    		break;
                    	}
                    }
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });*/
    });


    $j().ready(function () {
        showRolePrivileges();
        $j("#currentMenuPage").val(Constants.SUBMENU_ITEMS.MANAGE_TEAM);
        $j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.ADMIN);

//		if($j("#adminHeaderFlag").val() == "true"){
//		$j("#adminHeader").show();
//		}

        if ($j("#userHeaderFlag").val() == "true") {
            $j("#userHeader").show();
        }
        if ($j.browser.version == "7.0") {
            $j(".termAttr").css("padding-bottom", "10px");
        }

        if ($j.browser.version == "9.0") {
            $j(".paddingseven").css("padding-left", "11px");
        }
        if ($j.browser.msie || $j.browser.webkit) {
            $j(".headerMenuLinks .headerMenuLink").css("padding-bottom", "12px");
        }
        if ($j.browser.version == "9.0" || $j.browser.version == "8.0") {
            $j("#selectAll").css("margin-top", "-2px");
            $j("#selectAll").css("margin-left", "-4px");
            $j("#hideLanguageSlct").css("margin-bottom", "50px");
        }
        if ($j.browser.version == "7.0") {
            $j("#hideLanguageSlct").css("margin-bottom", "25px");
            $j(".companySlctErr").css("margin-top", "-14px");
            $j("#selectAll").css("margin-top", "-2px");
            $j("#selectAll").css("margin-left", "-4px");

        }
        $t.getAllUserTypeList({
            success: function (data) {
                displayUserTypeList(data, '#roleSlct');
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
        $t.getAllUserTypeList({
            success: function (data) {
                displayUserTypeList(data, '#role');
                $j("select[name='role'] option").each(function () {
                    if ($j(this).text() == "Community Member")
                        $j(this).attr("selected", "selected");
                });
                $j("#company").removeAttr('disabled');

            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        // To show header when user is in session
        if ($j("#headerFlag").val() == "true") {
            $j(".headerMainDiv").css("visibility", "visible");
//			if($j("#adminHeaderFlag").val() != "true"){
//			$j(".adminHeader").css("visibility","hidden");
//			}
        }
        $j('#admin').css('color', '#0DA7D5');
        $j('#admin').children("img").show();

        $j(".subMenu a").last().css("border-right", "none");
        $j(".subMenuLinks a").last().css("border-right", "none");

        $j("#emailIds").focus(function () {
            $j(this).val('');
        });
        $j("#email").focus(function () {
            $j(".emailExists").hide();
        });
        $j("#username").focus(function () {
            $j(".userExists").hide();
        });

        $j("#emailIds").blur(function () {
            if ($j(this).val() == '')
                $j(this).val('Enter email addresses...');
        });

        $j("#registerForm").attr('autocomplete', 'off');
        $j("#email").val("");
        $j("#password").val("");
        $t.getDomainList({
            success: function (data) {
                displayDomainList(data);

            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        $t.getCompanyList({
            success: function (data) {
                displayCompanyList(data, "#company");


            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        $j("#role").change(function () {
            var roleId = $j(this).val();
            if (roleId == Constants.ROLES.SUPER_TRANSLATOR) {
                $j("#company").html("");
                $j(".companySlctErr").hide();
                $j("#company").removeClass("error");
                $j("#company").html('<option>---select---</option>')
                $t.getCompanyList({
                    success: function (data) {
                        displayCompanyList(data, "#company");
                        $j("select[name='company'] option").each(function () {
                            if ($j(this).text() == "Welocalize")
                                $j(this).attr("selected", "selected");
                            $j(".companySlctErr").hide();
                            $j("#company").removeClass("error");
                        });
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });
                $j("#company").attr('disabled', 'disabled');
            }
            else {
                $j("#company").val(0);
                $j("#company").removeAttr('disabled');
            }

            $j("#companySlct").multiselect("uncheckAll");
            $j(".companySlct").hide();
            $t.getPrevilegesByRole(roleId, {
                success: function (data) {
                    if (data != null) {
                        for (var i = 0; i < data.length; i++) {
                            $j('.' + data[i].privileges.jsId).show();
                        }
                        if (roleId == Constants.ROLES.SUPER_ADMIN) {
                            $j(".addCompanyUser ").hide();
                            $j(".hideLanguageSlct").hide();
                            $j(".languageSlctErr").hide();
                            $j(".companySlctErr").hide();
                            $j("#company").removeClass("error");
                            $j(".regFrm .ui-state-default").css("border", "1px solid #BBBBBB");
                            flag = false;
                        } else {
                            /*if (adminHeaderFlag == "true") {
                                $j(".addCompanyUser").hide();
                            }
                            if (adminHeaderFlag == "false") {
                                $j(".addCompanyUser").hide();
                            }*/
                        	if($j(isSuperAdmin).val() == "true") {
                        		$j(".addCompanyUser").show();
                        	}
                        	if($j(isSuperAdmin).val() == "false") {
                        		$j(".addCompanyUser").hide();
                        	}
                            $j(".hideLanguageSlct").show();
                            flag = true;
                        }
                    }
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });
        });

        $t.getTopRegLangList({
            success: function (data) {
                displayUserLanguages(data, '.langMenu');
                getSlctdLang();
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }

        });

//		$j(".langMenu a").first().addClass("on");
        $j(".langMenu span").last().remove();


        $j("#cancelAddUser").click(function () {
            $j("#registerForm :text").val('');
            $j('#addUser').dialog('close');
            $j('#Action').val(0);
            showRolePrivileges();
//			$j(".addCompanyUser ").show(); 
//			$j(".hideLanguageSlct").show();

        });

        $j("#cancelModUser").click(function () {
            $j("#ModifyForm :text").val('');
            $j('#ModifyUser').dialog('close');
        });

        $j("#userName").focus(function () {
            $j('.userExistsErr').hide();
        });

        $j("#email").focus(function () {
            $j('.emailExistsErr').hide();
        });


        // Language List

        $t.getLanguageList({
            success: function (data) {

                displayLanguages(data, "#languageSlct");

                $j("#languageSlct").multiselect().multiselectfilter();
                $j("#languageSlct").multiselect({
                    noneSelectedText: 'Select language',
                    selectedList: 3,
                    onClose: function () {
                        values = ($j("#languageSlct").val() == null) ? "0" : $j("#languageSlct").val();
                        if (values != 0) {
                            $j(".languageSlctErr").hide();
                            $j("#registerForm .ui-state-default").css("border", "1px solid #dddddd");
                        }
                    },
                    classes: "lang"
                });

                displayLanguages(data, "#mainLanguageSlct");

                $j("#mainLanguageSlct").multiselect().multiselectfilter();
                $j("#mainLanguageSlct").multiselect({
                    noneSelectedText: 'Select language',
                    selectedList: 4,
                    onClose: function () {
                        $j('#teamListSectionHead div').find('.sort').remove();
                        $j("#dateOfCreation").append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
                        selectedLangIds = ($j("#mainLanguageSlct").val() == null) ? 0 : $j("#mainLanguageSlct").val();
                        if ($j.trim(newSellang) == $j.trim(selectedLangIds))
                            return;
                        newSellang = selectedLangIds;

                        if (selectedLangIds != null && selectedLangIds.length > 0 && selectedLangIds != 0 && MultiCValues != 0) {
                            allClicked = false;
                            showAllTeamMem = false;
                            triggerTeamDetails(selectedLangIds, MultiCValues, "dateOfCreation", "DESC", 0);
                            $j("#langs span").each(function (index) {
                                $j(this).removeClass("on");
                                $j(this).addClass("link");
                            });

                        } else if (MultiCValues != null && MultiCValues != 0 && selectedLangIds == 0 && !allClicked) {


                            triggerTeamDetails(0, MultiCValues, "dateOfCreation", "DESC", 0);
                            $j("#langs span").each(function (index) {
                                $j(this).removeClass("on");
                                $j(this).addClass("link");
                            });

                        } else if (MultiCValues == 0 && selectedLangIds != null && selectedLangIds != 0) {
                            allClicked = false;
                            showAllTeamMem = false;
                            triggerTeamDetails(selectedLangIds, 0, "dateOfCreation", "DESC", 0);
                            $j("#langs span").each(function (index) {
                                $j(this).removeClass("on");
                                $j(this).addClass("link");
                            });

                        } else if (MultiCValues == 0 && selectedLangIds == 0) {
                            showAllTeamMem = false;
                            triggerTeamDetails(defaultSelectedLang, 0, "dateOfCreation", "DESC", 0);
                            $j("#langs span").each(function (index) {
                                $j(this).removeClass("on");
                                $j(this).addClass("link");
                            });

                            $j("#defaultLang").addClass("on");
                        }
                    },
                    classes: "lang"
                });

                displayLanguages(data, "#exprtlangSlct");
                $j("#exprtlangSlct").multiselect().multiselectfilter();
                $j("#exprtlangSlct").multiselect({
                    noneSelectedText: 'Select language',
                    selectedList: 4,
                    onClose: function () {
                        selectedLangIds = ($j("#mainLanguageSlct").val() == null) ? "0" : $j("#exprtlangSlct").val();
                    },
                    classes: "lang"
                });
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });


        $t.getCompanyList({
            success: function (data) {
                displayCompanyList(data, "#companySlct");
                displayCompanyList(data, "#mutliCompanySlct");
                $j("#companySlct").multiselect().multiselectfilter();
                $j("#companySlct").multiselect({
                    noneSelectedText: 'Select company',
                    selectedList: 3, // 0-based index
                    onClose: function () {

                        cvalues = $j("#companySlct").val();

                    },
                    classes: "lang"

                });
                $j("#mutliCompanySlct").multiselect().multiselectfilter();

                $j("#mutliCompanySlct").multiselect({
                    noneSelectedText: 'Select company',
                    selectedList: 4, // 0-based index
                    onClose: function () {

                        $j('#teamListSectionHead div').find('.sort').remove();
                        $j("#dateOfCreation").append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');

                        MultiCValues = ($j("#mutliCompanySlct").val() == null) ? 0 : $j("#mutliCompanySlct").val();
                        if ($j.trim(MultiCValues) == $j.trim(savedCriteria.companyIds))
                            return;
                        if (MultiCValues != null && MultiCValues.length > 0 && newSellang != null && newSellang != 0) {
                            if (langsClicked) {
                                langsClicked = false;
                                newSellang = 0
                            }
                            $j("#langs span").each(function (index) {
                                $j(this).removeClass("on");
                                $j(this).addClass("link");
                            });
                            allClicked = false;
                            showAllTeamMem = false;
                            triggerTeamDetails(newSellang, MultiCValues, "dateOfCreation", "DESC", 0);

                        } else if (MultiCValues != null && MultiCValues != 0 && newSellang == 0) {
                            allClicked = false;
                            showAllTeamMem = false;
                            triggerTeamDetails(0, MultiCValues, "dateOfCreation", "DESC", 0);
                            $j("#langs span").each(function (index) {
                                $j(this).removeClass("on");
                                $j(this).addClass("link");
                            });

                        } else if (MultiCValues == 0 && newSellang != null && newSellang != 0) {
                            allClicked = false;
                            showAllTeamMem = false;
                            $j("#langs span").each(function (index) {
                                $j(this).removeClass("on");
                                $j(this).addClass("link");
                            });
                            if (langsClicked) {
                                langsClicked = false;
                                newSellang = defaultSelectedLang;
                                /*$j("#langs span").each(function(index) {
                                 $j(this).removeClass("on");
                                 $j(this).addClass("link");
                                 });*/
                                $j("#defaultLang").addClass("on");
                            }
                            triggerTeamDetails(newSellang, 0, "dateOfCreation", "DESC", 0);
                        } else if (MultiCValues == 0 && newSellang == 0) {

                            $j("#langs span").each(function (index) {
                                $j(this).removeClass("on");
                                $j(this).addClass("link");
                            });
                            showAllTeamMem = false;
                            triggerTeamDetails(defaultSelectedLang, 0, "dateOfCreation", "DESC", 0);
                            $j("#defaultLang").addClass("on");

                        }

                    }
//			classes:"lang"

                });
                
                displayCompanyList(data, "#exprtcompanySlct");
                $j("#exprtcompanySlct").multiselect().multiselectfilter();
                $j("#exprtcompanySlct").multiselect({
                    noneSelectedText: 'Select company',
                    selectedList: 4,
                    onClose: function () {
                        selectedLangIds = ($j("#exprtcompanySlct").val() == null) ? "0" : $j("#exprtcompanySlct").val();
                    },
                    classes: "lang"
                });

            }
        });

/**
        $t.getUserDetails({
            success: function (data) {
                displayUserDetails(data, '#userInfo');

            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
     */
        $t.getActiveUsersCount({
            success: function (data) {
                if (data != null) {
                    $j("#totalUsers").html(data);
                }

            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        $t.getTotalUsersInSystem({
            success:function(data){
                var totalUsersPerMonth = data;
                var dataXML = new Array();
                var totaluser = 0;
                var array = [];
                
                if(totalUsersPerMonth != null && totalUsersPerMonth!=""){
                	
                	for(var count=0;count<totalUsersPerMonth.length;count++) {
                        totaluser =  totalUsersPerMonth[4].count;
                        array.push(totalUsersPerMonth[count].count);
                    }
                	
                	if(array[0]==totaluser){
                		 array.unshift(0);
                	 }
                	
                    if ($j("#hpsite").val() == "true")
                    	dataXML.push("<chart showValues='0' adjustDiv='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showBorder='0' bgColor='#f9f9f9,#f9f9f9' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#f9f9f9' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='2' lineColor='#0e94bc' canvasBorderThickness='0'>");
                    else
                    	dataXML.push("<chart showValues='0' adjustDiv='0' showBorder='0' bgColor='#032D39,#032D39' bgAlpha='100,100' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#ffffff' divLineThickness='2' labelPadding='0'  baseFontColor='#C0C0C0' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
                    for(var count=0;count<totalUsersPerMonth.length;count++) {
                        dataXML.push("<set label='"+totalUsersPerMonth[count].month + "' " + "value='" +totalUsersPerMonth[count].count +"'/>");
                    }
                    dataXML.push("</chart>");
                    ChartRender.twoDLineChart("myChartId13", "240", "70", "userChart", dataXML);
                }else{
                    $j("#myChartId3").hide();
                    $j("#userChart").html("<div style='text-align:center; padding-top:30px;font-size:11px;color:#fff;width:270px;'>No data to display</div>");
                }
            },
            error: function(xhr, textStatus, errorThrown){
                console.log(xhr.responseText);
                if(Boolean(xhr.responseText.message)){
                    console.log(xhr.responseText.message);
                }
            }
        });
		


        validateUser = $j("#registerForm").validate({
            rules: {
                auserName: {
                    required: true,
                    minlength: 2
                },
                apassword: {
                    required: true,
                    mypassword: true,
                    minlength: 5
                },
                acnfmPassword: {
                    required: true,
                    minlength: 5,
                    equalTo: "#password"
                },
                aemail: {
                    required: true,
                    email: true
                },
                languageSlct: {
                    required: {
                        depends: function (element) {
                            return $j("#languageSlct").val() == '';
                        }
                    }
                }
            },


            messages: {
                auserName: {
                    required: "<br/>Username is required",
                    minlength: "<br/>Username must consist of at least 2 characters"
                },
                apassword: {
                    required: "<br/>Password is required",
                    minlength: "<br/>Password must be at least 5 characters long"
                },
                acnfmPassword: {
                    required: "<br/>Password is required",
                    minlength: "<br/>Password must be at least 5 characters long",
                    equalTo: "<br/>Enter the same password as above"
                },
                aemail: "<br/>Enter a valid email address",
                languageSlct: "<br/>Select at least one language"
            }
        });


        $j.validator.addMethod('mypassword', function (value, element) {
                return this.optional(element) || (value.match(/[a-zA-Z]/) && value.match(/[0-9]/) && value.match(/[!@#$%^&*()_-]/));
            },
            '<br/>Password should be alphanumeric with at least one special character');


        $j("#exprtUsers").click(function () {
            showExportUsers();
        });

        $j("#getUsers").click(function () {
            var query = "";
            var userLanguages = $j("#exprtlangSlct").val();
            var slctCompanies = $j("#exprtcompanySlct").val();
            var role = $j("#roleSlct").val();
            if (role == null || role == 0) {

                alertMessage("#roleSlctMessage");
            }
            else {
                if (userLanguages != null)
                    query = "&userLanguages=" + userLanguages
                if(slctCompanies != null)
                	query += "&slctCompanies=" + slctCompanies
                if (role != null)
                    query += "&role=" + role
                $j('#exportUserFilter').dialog('destroy');
                location.href = $j("#contextRootPath").val() + "/impExp_serv?c=exportCSV&exportType=user" + query;
            }
            $j("#roleSlct").val('');
            $j("#exprtlangSlct").multiselect("uncheckAll");
            $j("#exprtcompanySlct").multiselect("uncheckAll");

        });

        // Email Templates Picklist
        $t.getEmailTemplates({
            success: function (data) {
                displayEmailTemplateList(data);
                var emailTmpl = data;
                var emailTemplateId = "";
                $j(".emailTemplate").change(function () {
                    for (var i = 0; i < emailTmpl.length; i++) {
                        if ($j(".emailTemplate").val() == 0) {
                            $j(".emailPrvw").html("");
                            $j("#templateReq").show();
                            $j('.emailTemplate').addClass("errorBorder");
                        } else {

                            if ($j(this).val() == emailTmpl[i].emailTemplateId) {
                                emailTemplateId = $j(this).val();
                                $j(".emailPrvw").html(emailTmpl[i].emailMessageContent);
                            }
                        }
                    }
                });
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

    });


})(jQuery);