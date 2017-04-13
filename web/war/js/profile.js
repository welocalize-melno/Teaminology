(function (window, $j) {

    var values = 0;
    var validateDetails;
    var validatePwd;
    var fileName = null;
    var lastEditedDomainId = 0;
    
    var url = $j(location).attr('href');

	if(url.indexOf("/profile.jsp") != -1){
	    $j('#about').removeClass('aboutForDashboard');
	    $j('#about').addClass('aboutForDashboardMargin');
	    $j('.signoutAdmin').css('padding-left','250px');
	}
    var langSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];
    /**
     $t.getActiveUsersCount({
		success:function(data){
			if(data != null){
				var totalTerms1 = new String( data);
				var totalTerms2 = insertCommmas(totalTerms1);
				$j("#totalUsers").html(totalTerms2);
				
			}
		  
		},
		error: function(xhr, textStatus, errorThrown){
			console.log(xhr.responseText);
			if(Boolean(xhr.responseText.message)){
				console.log(xhr.responseText.message);
			}
		}
	});
     */
    var domainTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];
    var displayDomain = function (data) {
        $j('#mdomain').html("");
        $j('#mdomain').html('<option value="0">---Select---</option>');
        var domainList = data;
        for (var count = 0; count < domainList.length; count++) {
            var domainTmplClone = domainTmpl;
            domainTmplClone[1] = domainList[count].domainId;
            domainTmplClone[3] = domainList[count].domain;
            $j('#mdomain').append(domainTmplClone.join(""));
        }
    };

    var displayLanguageList = function (data) {
        $j('#languageSlct').html("");
        var languageList = data;
        for (var count = 0; count < languageList.length; count++) {
            var langSlctTmplClone = langSlctTmpl;
            langSlctTmplClone[1] = languageList[count].languageId;
            langSlctTmplClone[3] = languageList[count].languageLabel;
            $j('#languageSlct').append(langSlctTmplClone.join(""));
        }


    };

    var editProfile = function () {

        var userName = $j("#unameFld").val();
        var emailId = $j("#emailFld").val();
        var firstName = $j("#fnameFld").val();
        if ($j.trim(firstName) == "") {
            $j("#fnameFld").val('');
            firstName = null;
        }
        var lastName = $j("#lnameFld").val();
        if ($j.trim(lastName) == "") {
            $j("#lnameFld").val('');
            lastName = null;
        }
        var userLanguages = new Array();
        var userLanguages = $j("#languageSlct").val();
        var userTypeId = 0;
        var userDomainId = $j("#mdomain").val();

        if (userDomainId != 0) {
            userDomainId = $j("#mdomain").val();
        } else {
            userDomainId = 0;
        }
        $t.updateUser(0, firstName, lastName, userName, emailId, null, userLanguages, userTypeId, userDomainId, 0, null, {
            success: function (data) {
                var flag = true;
                if (data.indexOf("User exists") != -1) {

                    $j(".proUserExists").show();
                    $j("#unameFld").addClass("error");
                    $j(".proUserExists").html("Username already exists, please choose other user name");
                    flag = false;
                } else {
                    $j(".proUserExists").hide();
                }
                if (data.indexOf("Email exists") != -1) {
                    $j(".proEmailExists").show();
                    $j("#emailFld").addClass("error");
                    $j(".proEmailExists").html("Email id already exists, please choose other email id");
                    flag = false;
                } else {
                    $j(".proEmailExists").hide();
                }
                if (flag && data.indexOf("User exists") == -1 && data.indexOf("Email exists") == -1) {
                    $j("#unameReq").hide();
                    $j("#emailFldReq").hide();
                    $j(".viewFld").show();
                    $j(".editFld").hide();
                    $j(".ui-multiselect").hide();
                    $j(".editProf").show();
                    $j(".btnPanel").hide();
                    $j(".proUserExists").hide();
                    $j(".proEmailExists").hide();
                    userList();
                }
                lastEditedDomainId = userDomainId;
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
                lastEditedDomainId = 0;
            }
        });

        userLanguages = 0;
    };

    var userList = function () {
        $t.getUserDetails({
            success: function (data) {
                $j(".required").hide();
                var firstName = ((data.firstName) == null || (data.firstName) == "null") ? " " : data.firstName;
                var lastName = ((data.lastName) == null || (data.lastName) == "null") ? " " : data.lastName;

                $j("#viewFname").html(firstName);
                $j("#viewLname").html(lastName);
                $j("#viewUname").html(data.userName);
                $j("#viewEmail").html(data.emailId);
                $j("#viewRole").html(data.userTypeName);
//				$j("#viewDomain").html(data.domainName);
                var userDomainId = data.domainId;
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
                        displayDomain(data, '#mdomain');
                        $j("#viewDomain").html(domainName);
                        var domainFlag = false;

                        $j("select[name='mdomain'] option").each(function () {

                            if ($j(this).val() == selectedDomain) {
                                domainFlag = true;
                                $j(this).attr("selected", "selected");
                            }
                        });
                        if (domainFlag) {
                            $j("#mdomain").html(domainName);
                        }
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });
                $j(".langs").html(data.languages);
                //$j('#userInfo').html("");
                modalRender.bubble("#editProfile #viewRole", Constants.ROLE_MSG, "left center", "right center");
                modalRender.bubble("#editProfile #viewDomain", Constants.DOMAIN_MSG, "left center", "right center");
                modalRender.bubble("#editProfile #selLang", Constants.LANGUAGE_MSG, "left center", "right center");
                //displayUserDetails(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

    };

    var ajaxFunction = function () {
        $t.uploadProfile({
            success: function (data) {
                processStateChange(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

    };

    var processStateChange = function (uploadData) {
        if (uploadData == null) {
            setTimeout(function () {
                ajaxFunction();
            }, 100);
        } else {
            var isNotFinished = uploadData.isFinished
            var myBytesRead = uploadData.bytesRead
            var myContentLength = uploadData.contentLength
            var myPercent = uploadData.percent;
            fileName = uploadData.fileName;
            if ((isNotFinished == null) && (myPercent == null)) {
                setTimeout(function () {
                    ajaxFunction();
                }, 100);
            } else {

                if (myPercent != null) {
                    setTimeout(function () {
                        ajaxFunction();
                    }, 100);
                } else {
                    var ext = "JPG";
                    if (fileName != null && fileName.lastIndexOf(".") > 0)
                        ext = fileName.substr(fileName.lastIndexOf(".") + 1);
                    if (fileName != null && fileName.lastIndexOf('\\') > 0) {
                        fileName = fileName.substr(fileName.lastIndexOf('\\') + 1);
                    }

                    $t.getCropedImage(fileName, ext, {
                        success: function (data) {
                            if (data.errorMsg == null) {
                                $j('#uploadPicBrwse').dialog('destroy');
                                $j("#changeImgId").attr('src', $j("#contextRootPath").val() + "/images/person/" + fileName);
                                $j("#userChangeImg").attr('src', $j("#contextRootPath").val() + "/images/person/" + fileName);
                                $t.getUserDetails({
                                    success: function (data) {
                                        //$j('#userInfo').html("");
                                        // displayUserDetails(data);
                                    },
                                    error: function (xhr, textStatus, errorThrown) {
                                        console.log(xhr.responseText);
                                        if (Boolean(xhr.responseText.message)) {
                                            console.log(xhr.responseText.message);
                                        }
                                    }
                                });
                            } else {
                                $j("#errorMsg").html(data.errorMsg);
                                $j("#errorMsg").show();
                                $j("#uploadPic").val("");
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
        }
    };
    $j("#cnfmEmail").focus(function () {
        $j("#emailReq").hide();

    });
    var updatePwd = function () {

        var oldPwd = $j("#cnfmEmail").val();
        var newPwd = $j("#password").val();
        var passwordObjList = new Array();
        passwordObjList[0] = oldPwd;
        passwordObjList[1] = newPwd;

        var passwordObj = Object.toJSON(passwordObjList);
        $t.updatePwd(passwordObj, {
            success: function (data) {
                if (data.indexOf("failure") != -1) {
//				if(data=="failure"){
                    $j("#emailReq").show();
                    $j("#emailReq").html("Enter current password");
                    $j('#cnfmEmail').addClass("error");
                } else {
                    alertMessage("#pwdMessage");
                    $j("#emailReq").hide();
//					 $j("#passwordReq").hide();
//					 $j("#confmReq").hide();
//					 $j("#matchReq").hide();
//					 $j("#pwdMatchReq").hide();
                    $j("#confirm_password").val('');
                    $j("#password").val('');
                    $j("#cnfmEmail").val('');

                    var $tabs = $j('#tabs').tabs();

                    $tabs.tabs('select', 0); // switch to profile tab
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

    /**
     var insertCommmas = function (value){
		  		var length = value.length;
		  		var counter = 0;
		  		var returnValue = "";
		  		for (var i = length-1 ; i >= 0; i--) {
		  			returnValue = value.charAt(i) + ((counter > 0 && counter % 3 == 0) ? ',': '') + returnValue;				
		  			counter++;
		  		} 
		  		return returnValue;
		  };

     var userDetailTmpl = ['<div class="userImg"><img src="',
     '',
     '" height="50px" width="50px" /></div><div class="userDetails"><h5>',
     '',
     '</h5><p class="smallFont userVotes ">',
     '',
     ' votes</p><p class="smallFont">Member since: ',
     '',
     '</p></div>'
     ];
     var displayUserDetails = function(data) {
		var userDetails = data;
			var userDetailTmplClone = userDetailTmpl;
			var count=userDetails.totalVotes;
			var totalTerms1 = new String(count);
			userDetailTmplClone[1] = userDetails.photoPath;
			userDetailTmplClone[3] = userDetails.userName;
			userDetailTmplClone[5] = insertCommmas(totalTerms1);
			userDetailTmplClone[7] = userDetails.createDate;
			$j("#changeImgId").attr('src', userDetails.photoPath);
			$j('#userInfo').append(userDetailTmplClone.join(""));
			if($j("#adminHeaderFlag").val() != "true"){
				var badgingRate = userDetails.totalVotes + userDetails.termRequestCount;
				if(badgingRate>=0 && badgingRate<50){
					$j("#bagingImgId").html('<img src="'+$j("#contextRootPath").val()+'/images/BeginnerBadge.gif"/> &nbsp; <img src="'+$j("#contextRootPath").val()+'/images/hp-logo-blue.jpg" height="20px" width="20px" />');
				}else if(badgingRate>=50 && badgingRate<150){
					$j("#bagingImgId").html('<img src="'+$j("#contextRootPath").val()+'/images/AdvancedBadge.gif"/> &nbsp; <img src="'+$j("#contextRootPath").val()+'/images/hp-logo-blue.jpg" height="20px" width="20px" />');
				}else if(badgingRate>=150){
					$j("#bagingImgId").html('<img src="'+$j("#contextRootPath").val()+'/images/ExpertBadge.gif"/> &nbsp; <img src="'+$j("#contextRootPath").val()+'/images/hp-logo-blue.jpg" height="20px" width="20px" />');
				}
			}else{
				$j(".userVotes").hide();
			}
	};
     */
    var optnSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];


    var displayUserTypeList = function (data) {
        $j('.userTypeSlct').html("");
        var userTypeList = data;
        for (var count = 0; count < userTypeList.length; count++) {
            var userTypeTmplClone = optnSlctTmpl;
            userTypeTmplClone[1] = userTypeList[count].roleId;
            userTypeTmplClone[3] = userTypeList[count].roleName;
            $j('.userTypeSlct').append(userTypeTmplClone.join(""));
            if ($j(".role").html() == "Admin") {
                $j('.userTypeSlct').append('<option selected="selected">Admin</option>');
            }
        }
    };

    var userProfileTmpl = '<form id="editProfile" name="editProfile"><div style="padding-bottom: 20px;"><div class="label width130 floatleft" style="padding-top:3px;">First name:</div> <div class="floatleft" ><div class="viewFld fname" style="width:200px;" id="viewFname"></div><div><input type="text" size="25" value="" class="text220 editFld fname nodisplay" id="fnameFld" name="fnameFld" maxlength="30" /></div></div></div><br class="clear" />'
        + '<div style="padding-bottom: 20px;"><div class="label width130 floatleft" style="padding-top:3px;">Last name:</div><div class="floatleft" style="width:200px;"><div class="viewFld lname" style="width:200px;" id="viewLname"></div><div><input type="text" size="25" value="" class="text220 editFld lname nodisplay" id="lnameFld" name="lnameFld"  maxlength="30"/></div> <span class="nodisplay redTxt leftmargin100" id="unameReq">Username is required</span><span class="error proUserExists floatleft" style="width:270px;font-size:11px;margin-top:-1px;"></span></div></div><br class="clear" />'
        + ' <label for="unameFld" class="error floatleft nodisplay" style="margin-top:-16px;padding-left:136px;" generated="true"></label><div class="clear"></div><div  class="" style="padding-bottom: 20px;"><div class="label width130 floatleft" style="padding-top:3px;">Username<span class="required redTxt nodisplay">*</span>:</div><div class="floatleft" style="width:200px;"><div class="viewFld uname" style="width:200px;" id="viewUname"></div><div><input type="text" size="25" value="" class="text220 editFld uname  nodisplay " id="unameFld" name="unameFld" maxlength="30"/> </div> <span class="nodisplay redTxt leftmargin100" id="emailFldReq">Enter a valid email address</span><span class="error proEmailExists floatleft " style="width:270px;font-size:11px;margin-top:-1px;"></span></div></div><br class="clear" />'
        + ' <label for="emailFld" class="error floatleft nodisplay" style="margin-top:-15px;padding-left:136px;" generated="true"></label><div class="clear"></div><div  class="" style="padding-bottom: 20px;"><div class="label width130 floatleft" style="padding-top:3px;">Email<span  class="required redTxt nodisplay">*</span>:</div><div class="floatleft" style="width:200px; margin-bottom: 10px;"><div class="viewFld email" style="width:200px;" id="viewEmail"></div><div><input type="text" size="25" value="" class="text220 editFld email nodisplay" id="emailFld" name="emailFld" maxlength="50"/></div></div> </div><br class="clear" />'
        + '<div  class="" style="padding-bottom: 20px;"><div class="label width130 floatleft" style="padding-top:3px;">Role:</div><div class="floatleft" style="width:200px;"><div class="viewFld role" style="width:200px;" id="viewRole"></div><div><div style="min-height: 10px;width: 214px;" class="text220 viewFld userTypeSlct nodisplay editFld" disabled name="role" id="role"></div></div></div> </div><br class="clear" />'
        + '<div  style="padding-bottom: 20px;" id="member"><div class="label width130 floatleft domain" style="padding-top:3px;">Domain:</div><div class="floatleft" style="width:200px;"><div class="viewFld domain" style="width:200px;" id="viewDomain"></div><div><select id="mdomain"  class="text220 nodisplay editFld" name="mdomain"><option value="0">--Select--</option></select></div></div></div></div><br class="clear" />'
        + '<div style="padding-bottom: 20px;" class="languagedropDownSlct nodisplay"><div class="label width130 floatleft" style="padding-top:3px;">Languages:</div><div class="floatleft" style="width:200px;"><div class="viewFld langs" id="selLang" style="width:200px;"></div><div><select id="languageSlct" multiple class="text220 nodisplay editFld"></select></div></div></div><br class="clear" />'
        + '<div style="padding-bottom: 20px;"><div class="alignRight toppadding5"><a href="javascript:;" class="editProf rightmargin15">Edit profile</a></div>'
        + '<div class="btnPanel nodisplay" style="margin-left: 135px;margin-top: 20px;"><input type="button" value="Update" id="save" class="commonBtn padding5" /><input type="button" value="Cancel" id="cancelUpdate" class="leftmargin20  padding5" /></div></div></form>';

    var displayUserProfile = function (data) {
        $j(".regFrm").html("");
        $j(".regFrm").html(userProfileTmpl);
        var firstName = ((data.firstName) == null || (data.firstName) == "null") ? " " : data.firstName;
        var lastName = ((data.lastName) == null || (data.lastName) == "null") ? " " : data.lastName;
        $j("#viewFname").html(firstName);
        $j("#fnameFld").val(firstName);
        $j("#viewLname").html(lastName);
        $j("#lnameFld").val(lastName);

        $j("#viewUname").html(data.userName);
        $j("#unameFld").val(data.userName);
        $j("#viewEmail").html(data.emailId);
        $j("#emailFld").val(data.emailId);
        $j("#viewRole").html(data.userTypeName);
        if (data.userTypeName == "Super Admin") {
            $j(".languagedropDownSlct").hide();
        } else if (data.userTypeName == "Company Admin") {
            $j(".languagedropDownSlct").hide();
        } else {
            $j(".languagedropDownSlct").show();
        }
        var userDomainId = data.domainId;
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
                displayDomain(data, '#mdomain');
                $j("#viewDomain").html(domainName);
                var domainFlag = false;

                $j("select[name='mdomain'] option").each(function () {
                    if ($j(this).val() == selectedDomain) {
                        domainFlag = true;
                        $j(this).attr("selected", "selected");
                    }
                });
                if (domainFlag) {
                    $j("#mdomain").html(domainName);
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        modalRender.bubble("#editProfile #viewRole", Constants.ROLE_MSG, "left center", "right center");
        modalRender.bubble("#editProfile #viewDomain", Constants.DOMAIN_MSG, "left center", "right center");
        modalRender.bubble("#editProfile #selLang", Constants.LANGUAGE_MSG, "left center", "right center");


        $j(".regFrm").find(".langs").html(data.languages);

        $j("#mrole").change(function () {

        });

        $j("#unameFld").focus(function () {
            $j(".proUserExists").hide();
            $j("#unameReq").hide();

        });

        $j("#emailFld").focus(function () {
            $j(".proEmailExists").hide();
            $j("#emailFldReq").hide();

        });
    };

    var showBrowsePic = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#uploadPicBrwse:ui-dialog").dialog("destroy");

        $j("#uploadPicBrwse").dialog({
            height: 200,
            width: 400,
            modal: true,
            close: function (event, ui) {
                $j("#uploadPic").val('');
            }

        });
        $j("#errorMsg").hide();
    };

    $j("#tabs").tabs({
        select: function (event, ui) {
            var selected = ui.index;
            if (selected == 1) {
                validatePwd.resetForm();
                $j('#cnfmEmail').val('');
                $j('#password').val('');
                $j('#confirm_password').val('');
                $j('#cnfmEmail').removeClass("error");
                $j('#password').removeClass("error");
                $j('#confirm_password').removeClass("error");


            }
            if (selected == 0) {
                $j("#unameFld").val($j("#viewUname").html());
                $j("#fnameFld").val($j("#viewFname").html());
                $j("#lnameFld").val($j("#viewLname").html());
                $j("#emailFld").val($j("#viewEmail").html());
                validateDetails.resetForm();
                $j('#unameFld').removeClass("error");
                $j('#emailFld').removeClass("error");
                $j('.proUserExists').hide();
                $j('.proEmailExists').hide();
                $j("#languageSlct").hide();


            }


        }
    });


    var alertMessage = function (alertId) {
        $j(alertId + ":ui-dialog").dialog("destroy");
        $j(alertId).dialog({
            height: 140,
            width: 240,
            modal: true,
            buttons: {
                OK: function () {
                    $j(this).dialog("close");
                }
            }
        });
    };

    /**
     var chatData=function(){
			
			$t.getUserAccuracyRate({
				success:function(data){
					var finalisedTerms = data.finalizedTerm;
					var votedTerms = data.votedTerms;
					var dataXML = new Array();
					if(votedTerms==0) {
						$j("#accuracyRate").html("0%");
						if ($j("#hpsite").val() == "true")
							dataXML.push("<chart showPercentValues='1'   bgColor='#e6eaeb,#e6eaeb' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568'  canvasbgColor='#e6eaeb' showBorder='0' bgAlpha='100'>");
						else
							dataXML.push("<chart showPercentValues='1'  bgColor='#032D39,#032D39' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568'  canvasbgColor='#032D39' showBorder='0' bgAlpha='100'>");
						dataXML.push("<set label='Approved' value='"+finalisedTerms+"' />");
						dataXML.push("<set label='Incorrect' value='"+(votedTerms-finalisedTerms)+"' />");
						dataXML.push("</chart>");
						ChartRender.twoDPieChart("mychartId5",130,70, "accuracyPie", dataXML );

					}else{	
						
						var accuracyRate = Math.round((finalisedTerms/votedTerms) * 100);
						$j("#accuracyRate").html(accuracyRate+"%")
					
					
						if ($j("#hpsite").val() == "true")
							dataXML.push("<chart showPercentValues='1'   bgColor='#e6eaeb,#e6eaeb' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568'  canvasbgColor='#e6eaeb' showBorder='0' bgAlpha='100'>");
						else
							dataXML.push("<chart showPercentValues='1'  bgColor='#032D39,#032D39' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568'  canvasbgColor='#032D39' showBorder='0' bgAlpha='100'>");
						dataXML.push("<set label='Approved' value='"+finalisedTerms+"' />");
						dataXML.push("<set label='Incorrect' value='"+(votedTerms-finalisedTerms)+"' />");
						dataXML.push("</chart>");
						ChartRender.twoDPieChart("mychartId5",130,70, "accuracyPie", dataXML );
					}
				 
				},
				error: function(xhr, textStatus, errorThrown){
					console.log(xhr.responseText);
					if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
					}
				}
			});
			
			
		};
     */
    $j().ready(function () {
        $j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.PROFILE);
        /**

         if($j("#adminHeaderFlag").val() == "true"){
			$j("#adminHeader").show();
			 $j('#adminProfile').css('color','#0DA7D5');
			   $j('#adminProfile').children("img").show();
				$j(".accuracyChrt").hide();
			
		}
         if($j("#userHeaderFlag").val() == "true"){
            	$j("#userHeader").show();
          	    $j('#userProfile').children("img").show();
			//$j(".accuracyChrt").show();
			$j(".numUsersChrt").addClass("rightmargin15");
			ChartRender.destroyTwoDPieChart();
			chatData();
          	  $j('#userProfile').css('color','#0DA7D5');
			//$j(".accuracyChrt").show();
 			$j(".numUsersChrt").addClass("rightmargin15");
			ChartRender.destroyTwoDPieChart();
			chatData();
			}
         if($j("#headerFlag").val() == "true"){
			  $j(".signUpButton").hide();
			  
		  }
         if($j.browser.version=="9.0"){
				$j(".paddingseven").css("padding-left","11px");
			}

         if($j.browser.version=="7.0"){
			$j(".menuArrowProf").css("top","26");
		}

         $j(".subMenuLinks a").last().css("border-right", "none");
         */
        $j("#changeForm").attr('autocomplete', 'off');


        if ($j.browser.version == "7.0") {
            $j(".menuArrowAdmn").css("top", "26");
            $j(".termAttr").css("padding-bottom", "10px");
        }

        $t.getUserDetails({
            success: function (data) {
                //displayUserDetails(data);
                var typeName = data.userTypeName;
                var userDomainId = data.domainId;
                if (data.userTypeName == "Super Admin") {
                    $j(".languagedropDownSlct").hide();
                } else if (data.userTypeName == "Company Admin") {
                    $j(".languagedropDownSlct").hide();
                } else {
                    $j(".languagedropDownSlct").show();
                }

                displayUserProfile(data);

                $j(".editProf").click(function () {
                    $j(".required").show();
                    $j(".viewFld").hide();
                    $j(".editFld").show();
                    $j('#unameFld').removeClass("error");
                    $j('#emailFld').removeClass("error");
                    $j(".ui-multiselect").show();
                    $j("#languageSlct").hide();
                    $j(this).hide();
                    $j(".btnPanel").show();
                    var userTypeName = $j(".regFrm").find("#viewRole").html();
                    $j("#role").html(userTypeName);

                    $t.getDomainList({
                        success: function (data) {
                            var selectedDomain = 0;
                            if (lastEditedDomainId > 0)
                                selectedDomain = lastEditedDomainId;
                            else
                                selectedDomain = userDomainId;
                            var domainName = $j(".regFrm").find("#viewDomain").html();
                            displayDomain(data);
                            $j("select[name='mdomain'] option").each(function () {
                                if ($j(this).val() == selectedDomain) {
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
                    $t.getLanguageList({
                        success: function (data) {
                            displayLanguageList(data);

                            var theString = $j(".regFrm").find(".langs").html();
                            var langArray = theString.split(",");
                            for (var i = 0; i < langArray.length; i++) {
                                // check if any array element is empty, if so, take it out of the array
                                if ($j.trim(langArray[i]) == "")
                                    langArray.splice(i, 1); // remove element since it's blank
                            }
                            $j("#languageSlct option").each(function (i) {
                                for (var j = 0; j < langArray.length; j++) {

                                    if ($j.trim($j(this).text()) == $j.trim(langArray[j])) {
                                        $j(this).attr("selected", "selected");
                                    }
                                }
                            });

                            $j("#languageSlct").multiselect("destroy");
                            $j("#languageSlct").multiselect().multiselectfilter();
                            $j("#languageSlct").multiselect({
                                noneSelectedText: 'Select language',
                                selectedList: 4 // 0-based index

                            });
                            modalRender.bubble(".regFrm .ui-state-default", Constants.LANGUAGE_MSG, "left center", "right center");

                        },
                        error: function (xhr, textStatus, errorThrown) {
                            console.log(xhr.responseText);
                            if (Boolean(xhr.responseText.message)) {
                                console.log(xhr.responseText.message);
                            }
                        }
                    });
                    modalRender.bubble("#editProfile #mdomain", Constants.DOMAIN_MSG, "left center", "right center");
                    modalRender.bubble("#editProfile #role", Constants.ROLE_MSG, "left center", "right center");
                });

                $j("#cancelUpdate").click(function () {
                    $j("#unameReq").hide();
                    $j(".required").hide();
                    $j("#emailFldReq").hide();
                    $j(".viewFld").show();
                    $j(".editFld").hide();
                    $j(".ui-multiselect").hide();
                    $j(".editProf").show();
                    $j(".btnPanel").hide();
                    $j(".proEmailExists").hide();
                    $j(".proUserExists").hide();
                    $j("#unameFld").val($j("#viewUname").html());
                    $j("#fnameFld").val($j("#viewFname").html());
                    $j("#lnameFld").val($j("#viewLname").html());
                    $j("#emailFld").val($j("#viewEmail").html());
                    $j("#languageSlct").val($j(".viewFld").html());
                    validateDetails.resetForm();

                });

                validateDetails = $j("#editProfile").validate({
                    rules: {
                        unameFld: {
                            required: true,
                            minlength: 2
                        },
                        emailFld: {
                            required: true,
                            email: true
                        }
                    },
                    messages: {
                        unameFld: {
                            required: "Username is required",
                            minlength: "Username must consist of at least 2 characters"
                        },
                        emailFld: "Enter a valid email address"

                    }
                });
                $j("#save").click(function () {
                    if ($j("#editProfile").valid()) {
                        editProfile();
                    }
                });

                $j("#cancelBtn").click(function () {
                    if ($j("#hpsite").val() == "true")
                        window.location = "profile.jsp";
                    else
                        window.location = pageUrl + "/profile.jsp";
                });
                $j("#updatePwd").click(function () {
                    if ($j("#changeForm").valid()) {

                        updatePwd();
                    }
                });


                $j("#uploadUserPic").click(function (e) {
                    if ($j('#uploadPic').val() == "") {
                        alertMessage("#upload");
                    } else {
                        e.preventDefault();
                        fileName = $j("#uploadPic").val();
                        ajaxFunction();
                        $j("#uploadPicForm").submit();
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

        var values;


        /**
         $t.getTotalUsersInSystem({
			success:function(data){
				var totalUsersPerMonth = data;
				var dataXML = new Array();
				if(totalUsersPerMonth != null && totalUsersPerMonth!=""){
				if ($j("#hpsite").val() == "true")
					dataXML.push("<chart showValues='0' showBorder='0' bgColor='#e6eaeb,#e6eaeb' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' yAxisMinValue='0' yAxisMaxValue='30' showToolTip='1' chartTopMargin='5' canvasbgColor='#e6eaeb' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
				else
					dataXML.push("<chart showValues='0' showBorder='0' bgColor='#032D39,#032D39' bgAlpha='100,100' canvasPadding='0px' yAxisMinValue='0' yAxisMaxValue='80' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#ffffff' divLineThickness='2' labelPadding='0'  baseFontColor='#907460' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
					for(var count=0;count<totalUsersPerMonth.length;count++){
						dataXML.push("<set label='"+totalUsersPerMonth[count].month + "' " + "value='" +totalUsersPerMonth[count].count +"'/>");
					}
					dataXML.push("</chart>");
					ChartRender.twoDLineChart("myChartId2", "240", "70", "userChart", dataXML);
				}else{
					$j("#myChartId2").hide();
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
         $t.getUserAccuracyRate({
			success:function(data){
				var finalisedTerms = data.finalizedTerm;
				var votedTerms = data.votedTerms;
				if(votedTerms==0)
					votedTerms=1;
				var accuracyRate = Math.round((finalisedTerms/votedTerms) * 100);
				if($j("#adminHeaderFlag").val() != "true"){
					if(accuracyRate>=0 && accuracyRate<25){
					$j("#accuracyImgid").html('<img src="'+$j("#contextRootPath").val()+'/images/zero_stars.gif"/>');
				}else if(accuracyRate>=25 && accuracyRate<50){
					$j("#accuracyImgid").html('<img src="'+$j("#contextRootPath").val()+'/images/one_star.gif"/>');
				}else if(accuracyRate>=50 && accuracyRate<85){
					$j("#accuracyImgid").html('<img src="'+$j("#contextRootPath").val()+'/images/two_stars.gif"/>');
				}else if(accuracyRate>=85 && accuracyRate<=100){
					$j("#accuracyImgid").html('<img src="'+$j("#contextRootPath").val()+'/images/three_stars.gif"/>');
				}
				}
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
         */

        $j(".changePic").click(function () {

            showBrowsePic();
        });

        validatePwd = $j("#changeForm").validate({
            rules: {
                cnfmEmail: {
                    required: true

                },
                password: {
                    required: true,
                    mypassword: true,
                    minlength: 6
                },
                confirm_password: {
                    required: true,
                    minlength: 6,
                    equalTo: "#password"
                }
            },
            messages: {
                cnfmEmail: {
                    required: "Enter current password"
                },
                password: {
                    required: "Password is required",
                    minlength: "Password must be at least 6 characters long"
                },
                confirm_password: {
                    required: "Password is required",
                    minlength: "Password must be at least 6 characters long",
                    equalTo: "Enter the same password as above"
                }

            }
        });
        $j.validator.addMethod('mypassword', function (value, element) {
                return this.optional(element) || (value.match(/[a-zA-Z]/) && value.match(/[0-9]/) && value.match(/[`~|:;|"',.<>{}+=?!@#$%^&*()_-]/));
            },
            'Password should be alphanumeric with at least one special character');

    });


})(window, jQuery);