(function (window, $j) {
	$j("#signOut").hide();
	$j(".signUpButton").hide(); 
	$j("#about").hide(); 
    var values = 0;
    var fileName = null;
    var userTypeTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];
    var url = $j(location).attr('href');
	 if(url.indexOf("/registration.jsp") != -1){
	 	    $j('#footer').css('margin-top' ,'180px');
	 }
	 if(url.indexOf("/newell/registration.jsp") != -1){
	     $j(".header").css("background-image" ,"none");
	 }

    var displayUserTypeList = function (data) {
        var userTypeList = data;
        for (var count = 0; count < userTypeList.length; count++) {
            var userTypeTmplClone = userTypeTmpl;
            userTypeTmplClone[1] = userTypeList[count].roleId;
            userTypeTmplClone[3] = userTypeList[count].roleName;
            jQuery('#role').append(userTypeTmplClone.join(""));
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

    userRegistration = function () {
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
        var userRoleList = new Array();
        for (var i = 0; i < values.length; i++) {
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
        //var roleTypeId = parseInt($j("#role :selected").val());
        var roleTypeId = Constants.ROLES.COMMUNITY_MEMBER;

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
        user.userName = $j("#userName").val();
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
        user.userDomainId = $j("#domain").val();
        if (fileName == null) {
            user.photoPath = null;
        } else {
            user.photoPath = $j("#contextRootPath").val() + "/images/person/" + fileName;

        }
        user.userLanguages = userLanguagesList;

        var userDtlsParameter = Object.toJSON(user);
        $t.userRegistration(userDtlsParameter, {
            success: function (data) {

                if (data.indexOf("User exists") != -1) {
                    $j("#userName").css("border", "1px solid red");
                    $j(".userExistsErr").show();
                    $j(".userExistsErr").html("Username already exists, please choose other username");
                } else {
                    $j(".userExistsErr").hide();
                    $j("#userName").removeClass("errorBorder");
                }
                if (data.indexOf("Email exists") != -1) {
                    $j("#email").css("border", "1px solid red");
                    $j(".emailExistsErr").show();
                    $j(".emailExistsErr").html("Email id already exists, Please choose other email id");
                } else {
                    $j(".userExistsErr").show();
                    $j(".emailExistsErr").hide();
                    $j("#email").removeClass("errorBorder");
                }

                if (data.indexOf("User exists") == -1 && data.indexOf("Email exists") == -1) {
                    location.href = data;

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

    $j("#cancelBtn").click(function () {
        if ($j("#hpsite").val() == "true")
            window.location = "index.jsp";
        else
            window.location = "/app/index.jsp";
        $j(".userExistsErr").hide();
        $j(".emailExistsErr").hide();
        $j("#userName").css("border", "1px solid #DDDDDD");
        $j("#email").css("border", "1px solid #DDDDDD");
    });

    $j(".loginBtn").click(function () {
        if ($j("#registerForm").valid() && values != null && values != 0) {
            userRegistration();
        }
        if (values == null || values == 0) {
            $j(".languageSlctErr").show();
            $j(".regFrm .ui-state-default").css("border", "1px solid red");
        } else {
            $j(".regFrm .ui-state-default").css("border", "1px solid #DDDDDD");
            $j(".languageSlctErr").hide();
        }

    });
    $j("#userName").focus(function () {
        $j('.userExistsErr').hide();
    });

    $j("#email").focus(function () {
        $j('.emailExistsErr').hide();
    });

    var showBrowsePic = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#uploadPicBrwse:ui-dialog").dialog("destroy");

        $j("#uploadPicBrwse").dialog({
            height: 200,
            width: 400,
            modal: true,
            close: function (event, ui) {
                $j("#uploadRegPic").val('');
            }
        });
        $j(".picError").hide();
    };

    jQuery().ready(function () {
        $j(".userInfoContainer").hide();

        $t.getUserTypeList({
            success: function (data) {
                displayUserTypeList(data);
                $j("select[name='role'] option").each(function () {
                    if ($j(this).text() == "Community Member")
                        $j(this).attr("selected", "selected");
                });

            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

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
        $j("#role").change(function () {
//			if($j(this).val()==2){
//				$j("#domain").removeAttr('disabled');
//				$j(".domainReq").show();
//			}
//			else{
//				$j("#domain").attr('disabled','disabled');
//				$j(".domainReq").hide();
//				}
//			
        });


        $t.getLanguageList({
            success: function (data) {
                displayLanguageList(data);
                $j("#languageSlct").multiselect().multiselectfilter();
                modalRender.bubble(".regFrm .ui-state-default", Constants.LANGUAGE_MSG, "left center", "right center");
                $j("#languageSlct").multiselect({
                    noneSelectedText: 'Select language',
                    selectedList: 4, // 0-based index
                    onClose: function () {
                        values = ($j("#languageSlct").val() == null) ? "0" : $j("#languageSlct").val();
                        if (values != 0) {
                            $j(".languageSlctErr").hide();
                            $j(".regFrm .ui-state-default").css("border", "1px solid #dddddd");
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
        $j("#registerForm").attr('autocomplete', 'off');
        $j("#cnfmEmail").val("");
        $j("#password").val("");

        $j("#registerForm").validate({
            rules: {
                userName: {
                    required: true,
                    minlength: 2
                },
                password: {
                    required: true,
                    mypassword: true,
                    minlength: 6
                },
                cnfmPassword: {
                    required: true,
                    minlength: 6,
                    equalTo: "#password"
                },
                email: {
                    required: true,
                    email: true
                },
                cnfmEmail: {
                    required: true,
                    email: true,
                    equalTo: "#email"
                },
                languageSlct: {
                    required: {
                        depends: function (element) {
                            return(values == null || values == 0)
                        }
                    }
                }

            },


            messages: {
                userName: {
                    required: "Username is required",
                    minlength: "Username must consist of at least 2 characters"
                },
                password: {
                    required: "Password is required",
                    minlength: "Password must be at least 6 characters long"
                },
                cnfmPassword: {
                    required: "Password is required",
                    minlength: "Password must be at least 6 characters long",
                    equalTo: "Enter the same password as above"
                },
                email: "Enter a valid email address",
                cnfmEmail: {
                    required: "Enter a valid email address",
                    equalTo: "Enter the same email address as above"
                },
                languageSlct: ""
            }
        });

        $.validator.addMethod('mypassword', function (value, element) {
                return this.optional(element) || (value.match(/[a-zA-Z]/) && value.match(/[0-9]/) && value.match(/[`~|:;|"',.<>{}+=?!@#$%^&*()_-]/));
            },
            'Password should be alphanumeric with at least one special character');


        $j("#uploadPicId").click(function () {

            showBrowsePic();
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
        $j("#uploadUserRegPic").click(function (e) {
            if ($j('#uploadRegPic').val() == "") {
                alertMessage("#upload");
            } else {
                e.preventDefault();
                fileName = $j("#uploadRegPic").val();
                ajaxFunction();
                $j("#uploadRegPicForm").submit();
            }
        });

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
                                    $j("#uploadedImage").attr('src', $j("#contextRootPath").val() + "/images/person/" + fileName);
                                }
                                else {
                                    $j(".picError").html(data.errorMsg);
                                    $j(".picError").show();
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


    });

})(window, jQuery);

