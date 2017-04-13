(function (window, $j) {

    var values = null;
    var savedCriteria = null;
    var dataObj = null;
    var cExpiredTermvalues = null;
    var langSelectedVal = [];
    var compSelectedVal = [];
    
    var url = $j(location).attr('href');

	if(url.indexOf("/admin_overview.jsp") != -1){
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

    var langSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];


    var displayLanguageList = function (data) {
        var languageList = data;
        for (var count = 0; count < languageList.length; count++) {
            var langSlctTmplClone = langSlctTmpl;
            langSlctTmplClone[1] = languageList[count].languageId;
            langSlctTmplClone[3] = languageList[count].languageLabel;
            $j('#languageSlct').append(langSlctTmplClone.join(""));
        }
    };

    var slctOptionsTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayEmailTemplateList = function (data) {
        var emailTemplateList = data;
        for (var count = 0; count < emailTemplateList.length; count++) {
            var emailTemplateSlctTmplClone = slctOptionsTmpl;
            emailTemplateSlctTmplClone[1] = emailTemplateList[count].emailTemplateId;
            emailTemplateSlctTmplClone[3] = emailTemplateList[count].emailSubject;
            $j('.emailTemplate').append(emailTemplateSlctTmplClone.join(""));
        }
    };
    var companyTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];
    var displayCompanyList = function (data, appenderClass) {
        if (Boolean(data)) {
            var companyList = data;
            for (var count = 0; count < companyList.length; count++) {
                var companyTmplClone = companyTmpl;
                companyTmplClone[1] = companyList[count].companyId;
                companyTmplClone[3] = companyList[count].companyName;
                $j(appenderClass).append(companyTmplClone.join(""));
            }
        }
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
    var displayUserDetails = function (data) {
        var userDetails = data;
        var userDetailTmplClone = userDetailTmpl;
        userDetailTmplClone[1] = userDetails.photoPath;
        userDetailTmplClone[3] = userDetails.userName;
        userDetailTmplClone[6] = userDetails.createDate;
        $j('#userInfo').append(userDetailTmplClone.join(""));
    };
    */

    var triggerTermDetails = function (colName, sortOrder, langIds, companyIds, pageNum) {
        if (colName == null) {
            $j("#trmDtlSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
        }
        $j('#termDtlRowsList').empty();
        ChartRender.destroyTwoDPieChart();
        var criteria = {
            'colName': colName,
            'sortOrder': sortOrder,
            'langIds': langIds,
            'companyIds': companyIds,
            'pageNum': pageNum
        };
        savedCriteria = criteria;
        $j("#termDetails").trigger("showTermDetails", criteria);
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

    $j(".next").click(function () {
        if ($j(".next").hasClass("nextEnable")) {
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var langIds = savedCriteria.langIds;
            var companyIds = savedCriteria.companyIds;
            var pageNum = (savedCriteria.pageNum == 0) ? (savedCriteria.pageNum + 2) : (savedCriteria.pageNum + 1);
            savedCriteria.pageNum = pageNum;
            triggerTermDetails(colName, sortOrder, langIds, companyIds, pageNum);
        }
        ;
    });

    $j(".previous").click(function () {
        if ($j(".previous").hasClass("prevEnable")) {
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var langIds = savedCriteria.langIds;
            var companyIds = savedCriteria.companyIds;
            var pageNum = savedCriteria.pageNum - 1;
            triggerTermDetails(colName, sortOrder, langIds, companyIds, pageNum);
        }
    });

    var showHpTeamSeeAll = function () {
        $j(".ui-dialog").hide();
        $j(".ui-widget-overlay").hide();
        $j("#hpCommunityList:ui-dialog").dialog("destroy");
        $j("#hpCommunityList").dialog({
            height: 500,
            width: 600,
            modal: true
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

    var showLeaderBoardSeeAll = function () {
        $j("#allUserList:ui-dialog").dialog("destroy");
       // $j("#allUserList").show();
        

        $j("#allUserList").dialog({
            height: 500,
            width: 500,
            modal: true
        });
        $j(".ui-widget-overlay").show();
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

    $j().ready(function () {
        $j("#currentMenuPage").val(Constants.SUBMENU_ITEMS.OVERVIEW);
        $j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.ADMIN);

        showRolePrivileges();
        /**
        if ($j("#adminHeaderFlag").val() == "true") {
            $j("#adminHeader").show();
            $j('#admin').children("img").show();
        }
        if ($j("#userHeaderFlag").val() == "true") {
            $j("#userHeader").show();
            $j('#termList').children("img").show();

        }
        if ($j("#headerFlag").val() == "true") {
            $j(".signUpButton").hide();

        }
        $j('#admin').css('color', '#0DA7D5');
//		$j('#admin').children("img").show();

        if ($j.browser.msie || $j.browser.webkit) {
            $j(".headerMenuLinks .headerMenuLink").css("padding-bottom", "12px");
        }
        if ($j.browser.version == "9.0") {
            $j(".paddingseven").css("padding-left", "11px");
        }
        if ($j.browser.version == "7.0") {
            $j(".menuArrowAdmn").css("top", "26");
            $j(".termAttr").css("padding-bottom", "10px");
        }

        //$j(".subMenuLinks a").last().css("border-right", "none");
         */

        termListLimit = 10;

        $j("#termDetails").termDetails();
        triggerTermDetails(null, null, null, null, 0);

        $j('.openClose').click(function () {
            $j('#reports').slideToggle();
            $j(this).toggleClass('moduleClose');
        });

        var closeLoadingDialog = function (dialogId) {
            $j(dialogId).html("");
            $j(dialogId).dialog('destroy');
        };

        $j('#invitePplBtn').click(function () {

            var emailIdsTxt = $j('#inviteEmail').val();
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
//			var emailTxt = $j('#inviteEmail').val();
//				
//			if(validateForm(emailTxt)){
//				if($j.trim(emailTxt)==""){
//					alertMessage("#mailMessage");
//				}else{

//					$j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="'+$j("#contextRootPath").val()+'/images/loading.gif"  align="top" class="rightmargin10" />Sending mail... please wait</div>');
//					showLoadingDialog();
//					$j(".ui-dialog-titlebar").hide(); 
//					setTimeout(function(){
//						mailSentSuccess(emailTxt);
//						closeLoadingDialog('#loading');
//						$j('#inviteEmail').val('Enter email addresses...');
//					},200);
//				 var invite = new Object();
//				 var inviteIds = $j('#inviteEmail').val().split(',');
//				 invite.mailIds = inviteIds;
//				 invite.emailTemplateId = 0;
//				 invite.termIds = new Array();
//				 invite.userIds = new Array();
//				 invite.votingPeriod = 0;
//				 var inviteParameter =  Object.toJSON(invite);
//				 $t.invitePeople(inviteParameter,{
//						success:function(data){
//
//						},
//						error: function(xhr, textStatus, errorThrown){
//							console.log(xhr.responseText);
//							if(Boolean(xhr.responseText.message)){
//								console.log(xhr.responseText.message);
//							}
//						}
//					});

//			}
//			}
        });

        $j("#inviteMail").click(function () {
            var emailIdsTxt = $j('#inviteEmail').val();
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
                $j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif"  align="top" class="rightmargin10" />&nbsp; Sending mail... please wait</div>');
                showLoadingDialog();
                $j(".ui-dialog-titlebar").hide();
                setTimeout(function () {
                    mailSentSuccess(emailIdsTxt);
                    closeLoadingDialog('#loading');
                    $j('#inviteEmail').val('Enter email addresses...');
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
                        $j('.emailTemplate').removeClass("errorBorder");
                        $j("#invitation:ui-dialog").dialog("destroy");
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
        var isMultiSelectLangModified = function (newChkValues, oldChkValues) {
            var isCriteriaModified = false;
            if (newChkValues != null && oldChkValues != null) {
                if (newChkValues.length != oldChkValues.length) {
                    isCriteriaModified = true;
                } else {
                    isCriteriaModified = Util.compareArrays(newChkValues, oldChkValues);
                    if (isCriteriaModified) {
                        isCriteriaModified = false;
                    } else {
                        isCriteriaModified = true;
                    }
                }
            }
            return isCriteriaModified;

        }

        LeaderBoard.showLeaderBoard();
        TerminologyTeamList.showHpTerminologyTeamLst();

        /**
        $t.getActiveUsersCount({
            success: function (data) {
                if (data != null) {
                    var totalTerms1 = new String(data);
                    var totalTerms2 = Util.insertCommmas(totalTerms1);
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

        $t.getUserDetails({
            success: function (data) {

                displayUserDetails(data);


            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
        */

        var isMultiSelectModified = function (newChkValues, oldChkValues) {
            var isCriteriaModified = false;
            if (newChkValues != null && oldChkValues != null) {
                if (newChkValues.length != oldChkValues.length) {
                    isCriteriaModified = true;
                } else {
                    isCriteriaModified = Util.compareArrays(newChkValues, oldChkValues);
                    if (isCriteriaModified) {
                        isCriteriaModified = false;
                    } else {
                        isCriteriaModified = true;
                    }
                }
            }
            return isCriteriaModified;

        }

        var removeEmptyString = function (arrayValues) {
            var newArrayValues = [];
            if (arrayValues != null && arrayValues.length > 0) {
                for (index = 0; index < arrayValues.length; index++) {
                    var valAtIndex = arrayValues[index];
                    if (valAtIndex != null && $j.trim(valAtIndex).length > 0) {
                        newArrayValues.push(valAtIndex);
                    }
                }
            }
            return newArrayValues;
        }


        var validateDropDown = function (prev, changed) {
            var changedFlag = false;
            if (Boolean(changed)) {
                if (prev.length != changed.length) {
                    changedFlag = true;
                } else {
                    var count = 0;
                    for (var i = 0; i < prev.length; i++) {
                        for (var j = 0; j < changed.length; j++) {
                            if (changed[j] == prev[i]) {
                                count++;
                                break;
                            }
                        }
                    }
                    if (prev.length != count) {
                        changedFlag = true;
                    }
                }
            }
            return changedFlag;
        };

        $t.getLanguageList({
            success: function (data) {
                displayLanguageList(data);
                $j("#languageSlct").multiselect().multiselectfilter();
                $j("#languageSlct").multiselect({
                    noneSelectedText: function () {
                        return 'Select language';
                    },
                    selectedList: 4, // 0-based index
                    onClose: function () {


                        values = $j("#languageSlct").val();
                        if (values == null) {
                            values = [];
                        }
                        langSelectedVal = removeEmptyString(langSelectedVal);
//			    		   if(langSelectedVal == null || validateDropDown(langSelectedVal,values)){
                        var isLangModified = isMultiSelectLangModified(values, langSelectedVal);
                        if (values == "") {
                            values = null;
                        }

                        if (isLangModified) {
                            langSelectedVal = values;
                            if (values != null && values.length > 0 && cExpiredTermvalues != null) {
                                $j("#dataPie_1005").empty();
                                triggerTermDetails(null, null, values, cExpiredTermvalues, 0);
                            } else if (values == null && cExpiredTermvalues != null) {
                                triggerTermDetails(null, null, null, cExpiredTermvalues, 0);
                            } else if (values != null && cExpiredTermvalues == null) {
                                triggerTermDetails(null, null, values, null, 0);
                            } else {
                                triggerTermDetails(null, null, null, null, 0);
                            }

                        }


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
                if (Boolean(data)) {
                    displayCompanyList(data, "#companyExpiredTermsSlct");

                    $j("#companyExpiredTermsSlct").multiselect().multiselectfilter();

                    $j("#companyExpiredTermsSlct").multiselect({
                        noneSelectedText: 'Select company',
                        selectedList: 4,// 0-based index
                        onClose: function () {


                            cExpiredTermvalues = $j("#companyExpiredTermsSlct").val();
                            if (cExpiredTermvalues == null) {
                                cExpiredTermvalues = [];
                            }
                            compSelectedVal = removeEmptyString(compSelectedVal);
                            var isModified = isMultiSelectModified(cExpiredTermvalues, compSelectedVal);
                            if (cExpiredTermvalues == "") {
                                cExpiredTermvalues = null;
                            }
                            if (isModified) {
                                compSelectedVal = cExpiredTermvalues;
                                if (cExpiredTermvalues != null && cExpiredTermvalues.length > 0 && values != null) {
                                    $j("#dataPie_1005").empty();
                                    triggerTermDetails(null, null, values, cExpiredTermvalues, 0);
                                } else if (values == null && cExpiredTermvalues != null) {
                                    triggerTermDetails(null, null, null, cExpiredTermvalues, 0);
                                } else if (values != null && cExpiredTermvalues == null) {
                                    triggerTermDetails(null, null, values, null, 0);
                                } else {
                                    triggerTermDetails(null, null, null, null, 0);
                                }
//									
                            }
//							}

                        }
//			    	   classes:"lang"

                    });


                }
            }
        });
        $t.getTermsInGlossary({
            success: function (data) {
                var termsPerYear = data;
                var dataXML = new Array();
                dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0'   showAlternateHGridColor='0' canvasBorderThickness='0' baseFont='HP Simplified' lineThickness='2' lineColor='#0e94bc'>");
                if (termsPerYear != null) {
                    for (var count = 0; count < termsPerYear.length; count++) {
                        dataXML.push("<set label='" + termsPerYear[count].interval + "' " + "value='" + termsPerYear[count].termsPerInterval + "'/>");
                    }
                }
                dataXML.push("</chart>");
                ChartRender.twoDLineChart("myChartId", "240", "150", "chartContainer", dataXML);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });


        $t.getTermsDebated({
            success: function (data) {
                var termsDebatedPerYear = data;
                var dataXML = new Array();
                if ($j("#hpsite").val() == "true")
                    dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showAlternateHGridColor='0' canvasBorderThickness='0' baseFont='HP Simplified' lineThickness='2' lineColor='#0e94bc'>");
                else
                    dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
                if (termsDebatedPerYear != null) {
                    for (var count = 0; count < termsDebatedPerYear.length; count++) {
                        dataXML.push("<set label='" + termsDebatedPerYear[count].interval + "' " + "value='" + termsDebatedPerYear[count].termsPerInterval + "'/>");
                    }
                }
                dataXML.push("</chart>");
                ChartRender.twoDLineChart("myChartId1", "240", "150", "chartContainer1", dataXML);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        /**
        $t.getTotalUsersInSystem({
            success: function (data) {
                var totalUsersPerMonth = data;
                var dataXML = new Array();
                if (totalUsersPerMonth != null && totalUsersPerMonth != "") {
                    if ($j("#hpsite").val() == "true")
                        dataXML.push("<chart showValues='0' showBorder='0' bgColor='#e6eaeb,#e6eaeb' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' yAxisMinValue='0' yAxisMaxValue='30' showToolTip='1' chartTopMargin='5' canvasbgColor='#e6eaeb' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
                    else
                        dataXML.push("<chart showValues='0' showBorder='0' bgColor='#032D39,#032D39' chartData='032D39' bgAlpha='100,100' canvasPadding='0px' yAxisMinValue='0' yAxisMaxValue='30' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#ffffff' divLineThickness='2' labelPadding='0'  baseFontColor='#C0C0C0' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
                    for (var count = 0; count < totalUsersPerMonth.length; count++) {
                        dataXML.push("<set label='" + totalUsersPerMonth[count].month + "' " + "value='" + totalUsersPerMonth[count].count + "'/>");
                    }
                    dataXML.push("</chart>");
                    ChartRender.twoDLineChart("myChartId2", "240", "70", "userChart", dataXML);
                } else {
                    $j("#myChartId2").hide();
                    $j("#userChart").html("<div style='text-align:center; padding-top:30px;font-size:11px;color:#fff;width:270px;'>No data to display</div>");
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
         */

        $t.getTotalTermsInGlossary({
            success: function (data) {
                var totalTerms = new String(data);
                totalTerms = Util.insertCommmas(totalTerms);
                $j("#totalTerms").html(totalTerms);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        $t.getTotalDebatedTerms({
            success: function (data) {
                var totalTerms1 = new String(data);
                var totalTerms2 = Util.insertCommmas(totalTerms1);
                $j("#totalDebatedTrms").html(totalTerms2);

            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });


        $j("#trmDtlSectionHead div").click(function () {
            var sortOrder = $j(this).attr('sortOrder');
            var colName = $j(this).attr('id');
            if (colName == "column2") {
                return;
            }
            if (!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))) {
                $j("#trmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).attr('sortOrder', 'ASC');
                });
                $j(this).attr('sortOrder', 'DESC');
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
                $j(this).addClass("ascending");
            } else if ($j(this).hasClass("ascending")) {
                $j("#trmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).attr('sortOrder', 'ASC');
                });
                $j(this).attr('sortOrder', 'ASC');
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
                $j(this).addClass("descending");
            } else if ($j(this).hasClass("descending")) {
                $j("#trmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).attr('sortOrder', 'ASC');
                });
                $j(this).attr('sortOrder', 'DESC');
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
                $j(this).addClass("ascending");
            }
            triggerTermDetails(colName, sortOrder, values, cExpiredTermvalues, 1);
        });

        $j('.seeAll').click(function () {
            showLeaderBoardSeeAll();

        });

        $j('.seeAllHpTeam').click(function () {
            showHpTeamSeeAll();
        });

        $j('#inviteEmail').focus(function () {
            if ($j(this).val() == 'Enter email addresses...')
                $j(this).val('');
        });
        $j('#inviteEmail').blur(function () {
            if ($j(this).val() == '')
                $j(this).val('Enter email addresses...');
        });

        if ($j('#leaderboard_seeAll').length > 0) {
            var oTable = $j('#leaderboard_seeAll').dataTable({
                "bProcessing": true,
                "sServerMethod": "GET",
                "bAutoWidth": false,
                "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getAllMembers",
                "aoColumns": [
                    { "mDataProp": "photoPath", "bSortable": false, "sWidth": "15%", "sClass": "alignCenter"},
                    { "mDataProp": "userName", "sWidth": "25%" },
                    { "mDataProp": "votes", "sWidth": "20%", "sClass": "alignCenter"},
                    { "mDataProp": "bagdingPhotopath", "sWidth": "20%"},
                    { "mDataProp": "accuracyPhotoPath", "sWidth": "20%"}
                ],
                "iDisplayLength": 8
            });

        }

        if ($j('#hpCommunity_seeAll').length > 0) {
            var hpTable = $j('#hpCommunity_seeAll').dataTable({
                "bProcessing": true,
                "sServerMethod": "GET",
                "bAutoWidth": false,
                "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getAllHPMembers",
                "aoColumns": [
                    { "mDataProp": "photoPath", "bSortable": false, "sWidth": "5%", "sClass": "alignCenter padding5"},
                    { "mDataProp": "userName", "sWidth": "30%" },
                    { "mDataProp": "totalVotes", "sWidth": "20%", "sClass": "alignCenter"},
                    { "mDataProp": "languages", "sWidth": "45%"}
                ],
                "iDisplayLength": 8
            });

        }

        $j('.dataTables_length').hide();


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
                            $j("#templateReq").hide();
                            $j('.emailTemplate').removeClass("errorBorder");
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


})(window, jQuery);