(function (window, $j) {

    var selectedTermIds = "";
    var selectedEmailIds = "";
    var pieChartHeight = 40;
    var pieChartWidth = 40;
    var noOfPages = 0;
    var langValues = "";
    var totalTerms = 0;
    var tmTotalTerms = 0;
    var termBasetotalTerms = 0;
    var totalResultedTerms = 0;
    var savedCriteria = null;
    var termListLimit = 10;
    var searchStr = "";
    var exportBy = "";
    var searchBy = null;
    var selectedIds = "";
    var searchStr = null;
    var serachType = null;
    var loadFlag = null;
    var termBaseData = null;
    var tmData = null;
    var termbaseInfo = null;
    var tmInfo = null;
    var searchString = null;
    var searchText = null;
    var replaceText = null;
    var caseFlag = false;
    var compSelectedVal = [];
    var validateRequestChangeDetails;
    var replaceAlertCount = 0;
    var replaceAlertBothCount = 0;
    var uniqueSelectedTermIds = [];
    var url = $j(location).attr('href');

	if(url.indexOf("/search.jsp") != -1){
	    $j('#about').removeClass('aboutForDashboard');
	    $j('#about').addClass('aboutForDashboardMargin');
	    $j('.signoutAdmin').css('padding-left','250px');
	}
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
     */

    var termDtlTmpl = [
        '<div class="rowBg"><div class="chkBx"><input type="checkbox" value="test" class="floatleft case checkbox" checked></div><div class="row twistie_close" termId="',
        '',
        '" id="rowid_',
        '',
        '"><div class="width110 bigFont  pollTerm">',
        '',
        '</div><div class="smallPie" id=',
        '',
        ' class="width40"></div><div class="width110 bigFont  toppadding5 targetTrm">',
        '',
        '</div><div class="width90 toppadding5 trmLanguage smallFont">',
        '',
        '</div><div class="width90  toppadding5 viewComp_search nodisplay tComapny">',
        '',
        '</div><div class="width40 toppadding5"><img src="' + $j("#contextRootPath").val() + '/images/grey_checkmark.png" height="20px" width="20px" class="novisibility tickImg ',
        '',
        ' " /></div><div class="width90 toppadding5 viewDate smallFont" viewId="viewDate_',
        '',
        '">',
        '',
        '</div><div class="editDate nodisplay width90 toppadding5"><input type="text" size="8" class="rightmargin5" id="',
        '',
        '" /></div><div class="width90  toppadding5 tPartsOfSpeech">',
        '',
        '</div><div class="width110  toppadding5 tCategory">',
        '',
        '</div><div class="width90  toppadding5 tDomain">',
        '',
        '</div><div class="width30 toppadding15 modify editTermDetails nodisplay"><img class="headerMenuLink modifyImg "  height="20px" width="20px" src="' + $j("#contextRootPath").val() + '/images/Pencil1.png" editId="',
        '',
        '" /></div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay"  id="overView" termId="',
        '',
        '"><div class="loadingDiv" style="text-align: center;"><img src=' + $j("#contextRootPath").val() + '/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
    ];
    var tmDtlTmpl = ['<div class="rowBg" style="font-size:11px;"><div class="chkBx"><input type="checkbox" class="floatleft case checkbox" value="test" checked/></div><div  class="row twistie_close" termId="',
        '',
        '" id="rowid_',
        '',
        '"><div class="width170 bigFont  toppadding5 sourceTm">',
        '',
        '</div><div class="width150 bigFont toppadding5 targetTm">',
        '',
        '</div><div class="width120 toppadding5 tmLanguage smallFont">',
        '',
        '</div><div class="width110  toppadding5 viewComp_search  nodisplay tmCompany">',
        '',
        '</div><div class="width110  toppadding5 productLine">',
        '',
        '</div><div class="width110  toppadding5 industryDomain">',
        '',
        '</div><div class="width90  toppadding5 contentType">',
        '',
        '</div><div class="width40 toppadding15 modify editTermDetails nodisplay"><img class="headerMenuLink modifyTmImg "  height="20px" width="20px" src="' + $j("#contextRootPath").val() + '/images/Pencil1.png" editId="',
        '',
        '" /></div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay" style="min-height:170px;" id="overView" termId="',
        '',
        '"><div class="loadingDiv" style="text-align: center;"><img src=' + $j("#contextRootPath").val() + '/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
    ];

    var companyTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];
    /**
    var displayUserDetails = function (data, id) {
        var userDetails = data;
        var userDetailTmplClone = userDetailTmpl;
        if (userDetails != null && userDetails != undefined) {
            var count = userDetails.totalVotes;
            var totalTerms1 = new String(count);
            userDetailTmplClone[1] = userDetails.photoPath;
            userDetailTmplClone[3] = userDetails.userName;
            userDetailTmplClone[6] = userDetails.createDate;
            $j(id).append(userDetailTmplClone.join(""));
        }

    };
         */
    var slctOptionsTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];


    var displayPosList = function (data, appenderClass) {

        var posList = data;
        for (var count = 0; count < posList.length; count++) {
            var partOfSpeechSlctTmplClone = slctOptionsTmpl;
            partOfSpeechSlctTmplClone[1] = posList[count].partsOfSpeechId;
            partOfSpeechSlctTmplClone[3] = posList[count].partOfSpeech;
            $j(appenderClass).append(partOfSpeechSlctTmplClone.join(""));
        }

    };

    var displayProductGroupList = function (data, appenderClass) {

        var posList = data;
        for (var count = 0; count < posList.length; count++) {
            var partOfSpeechSlctTmplClone = slctOptionsTmpl;
            partOfSpeechSlctTmplClone[1] = posList[count].productId;
            partOfSpeechSlctTmplClone[3] = posList[count].product;
            $j(appenderClass).append(partOfSpeechSlctTmplClone.join(""));
        }

    };


    var displayContentList = function (data, appenderClass) {
        var contentList = data;
        for (var count = 0; count < contentList.length; count++) {
            var contentTmplClone = slctOptionsTmpl;
            contentTmplClone[1] = contentList[count].contentTypeId;
            contentTmplClone[3] = contentList[count].contentType;
            jQuery(appenderClass).append(contentTmplClone.join(""));
        }
    };

    var displayTermFormList = function (data, appenderClass) {
        var trmFormList = data;
        for (var count = 0; count < trmFormList.length; count++) {
            var termFormSlctTmplClone = slctOptionsTmpl;
            termFormSlctTmplClone[1] = trmFormList[count].formId;
            termFormSlctTmplClone[3] = trmFormList[count].formName;
            $j(appenderClass).append(termFormSlctTmplClone.join(""));
        }
    };

    var displayDomainList = function (data, appenderClass) {
        var trmFormList = data;
        for (var count = 0; count < trmFormList.length; count++) {
            var termFormSlctTmplClone = slctOptionsTmpl;
            termFormSlctTmplClone[1] = trmFormList[count].domainId;
            termFormSlctTmplClone[3] = trmFormList[count].domain;
            $j(appenderClass).append(termFormSlctTmplClone.join(""));
        }
    };

    var displayUserTypeList = function (data) {
        var userTypeList = data;
        for (var count = 0; count < userTypeList.length; count++) {
            var userTypeTmplClone = slctOptionsTmpl;
            userTypeTmplClone[1] = userTypeList[count].roleId;
            userTypeTmplClone[3] = userTypeList[count].roleName;
            $j('.categoryList').append(userTypeTmplClone.join(""));
        }
    };


    var termAttrTmpl = '<div class="termAttr"><div><div><div class="bold sourceTerm label" >Source: </div> <div class="sourceDesc viewDetailsFld" style="word-wrap:break-word;"></div><textarea rows="3" cols="65"   name="editSourceTerm"  id=#{sourceId} class="sourceDescEdit editDetailsFld nodisplay editSourceTerm"  value="" style=" overflow-wrap: hyphenate;"></textarea>'
        + '<div class="clear"></div>'
        + '</div><div class="topmargin5"><div class=" bold targetTerm label" >Target: </div> <div class="targetDesc viewDetailsFld" style=" word-wrap: break-word;" ></div><textarea rows="3" cols="65"   name="editTargetTerm"  id=#{targetId} class="targetDescEdit editDetailsFld nodisplay editTargetTerm"  value="" style=" overflow-wrap: hyphenate;"></textarea><div class="clear"></div>'
        + '</div></div><div class="left"><div class="topmargin5"><div class="bold termType label" >Part of speech: </div> <span class="termTypeDesc viewDetailsFld"></span><select name="editTermPOS" id=#{posId} class="editDetailsFld nodisplay editTermPOS"><option value="">---Select---</option></select><div class="clear"></div>'
        + '</div><div class="topmargin5"><div class="bold label">Form: </div><span class="formDesc viewDetailsFld"></span><select name="termForm"  id=#{formId} class="editDetailsFld nodisplay termForm"><option value="">---Select---</option></select><div class="clear"></div>'
        + '</div><div class="topmargin5"><div class="bold label">Term Category: </div><span class="categoryDesc programDesc viewDetailsFld"></span><select name="category" id=#{categoryId} class="editDetailsFld nodisplay  category"><option value="">---Select---</option></select><div class="clear"></div>'
        + '</div><div class="topmargin5"><div class="bold label">Domain: </div><span class=" termDomainDesc viewDetailsFld"></span><select name="domain" id=#{domainId} class="editDetailsFld nodisplay  domain"><option value="">---Select---</option></select><div class="clear"></div>'
        + '</div><div class="topmargin5"><div class="bold label">Term notes: </div><span class="domainDesc viewDetailsFld" style=" word-wrap: break-word;"></span><textarea id=#{notesId} rows="1" cols="12" class="domainDescEdit editDetailsFld nodisplay notes" style=" word-wrap: break-word;"></textarea>'
        + '</div></div><div class="right"><div class="topmargin5"><div class="bold label">Concept definition: </div><span class="definitionDesc viewDetailsFld" style=" word-wrap: break-word;"></span><textarea rows="2" cols="30" id=#{defId} class="definitionDescEdit editDetailsFld nodisplay definition" style=" word-wrap: break-word;"></textarea><div class="clear"></div>'
        + '</div><div class="topmargin10"><div class="bold label">Term Usage: </div><span class="usageDesc viewDetailsFld" style=" word-wrap: break-word;"></span><textarea rows="2" cols="30"   id=#{usageId}  class="usageDesc editDetailsFld nodisplay" style=" word-wrap: break-word;"></textarea>'
        + '</div><div id="editTermImage" ><div id=#{uploadedImageId}  style="width:50px; border:1px solid #cccccc;height: 50px;margin-top: 26px;margin-left: 1px;"><img width="50px" height="50px" class="picture headerMenuLink " src=#{imagePath}  /></div><div style="width:50px" class=" alignCenter" id=#{changePicId}><a href="javascript:;" class="changeTermPic  editTermDetails nodisplay"   style="font-size: 14px;padding-right: 0px;">Image</a></div></div></div></div>';


    var tmAttrTmpl = '<div class="termAttr"><div><div><div class="bold sourceTerm label" >Source: </div> <div class="sourceDesc viewDetailsFld" style="word-wrap:break-word;"></div><div  name="editSourceTerm"  id=#{sourceId} class="sourceDescEdit editDetailsFld nodisplay " ></div><div class="clear"></div>'
        + '</div><div class="topmargin5"><div class=" bold targetTerm label" >Target: </div> <div class="targetDesc viewDetailsFld" style=" word-wrap: break-word;"></div><textarea rows="3" cols="44"   name="editTargetTerm"  id=#{targetId} class="targetDescEdit editDetailsFld nodisplay editTargetTerm"  value="" style=" word-wrap: break-word;"></textarea><div class="clear"></div>'
        + '</div></div><div class="left"><div class="topmargin5"><div class="bold label">Product Line: </div><span class="productDesc viewDetailsFld"></span><select name="editProductLine" id=#{productId} class="editDetailsFld  productDescEdit nodisplay   editProductLine"><option value="">---Select---</option></select><div class="clear"></div>'
        + '</div><div class="topmargin5" ><div class="bold industry label" >Industry Domain: </div> <span class="domainDesc viewDetailsFld"></span><select name="editDomain" id=#{domainId} class="editDetailsFld nodisplay editDomain domainDescEdit"><option value="">---Select---</option></select><div class="clear"></div>'
        + '</div><div class="topmargin5"><div class="bold label">Content Type: </div><span class="contentDesc viewDetailsFld"></span><select name="editContent" id=#{contentId} class="editDetailsFld  contentDescEdit nodisplay editContent"><option value="">---Select---</option></select><div class="clear"></div></div></div>';


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
    var deprecatedTermInfoSec = ['<div class="clear"></div><div class=" bigFont" style="color:red;"> <span class="width295 label source" style="color:red;padding-left:39px">Deprecated Source: ',
        '',
        '</span><div class="width30" >&nbsp;</div><span class=" width220 label target">Deprecated Target: ',
        '',
        '</span></div>'
    ];
    var editDetailsSec = '<div class="clear"></div><div class="editLinksSec smallFont editTermDetails nodisplay"><a href="javascript:;" class="padding5 editDetails">Edit details</a><a href="javascript:;" class="padding5 updateDetails nodisplay">Update details</a> </div>';

    $j("#selectReplaceChkBox").hide();
    
    var classNames = ['purpleBar', 'greenBar', 'yellowBar', 'pinkBar'];
    var bindEvents = function (data) {
        var classNamesClone = classNames.slice(0);
        $j('#termDtlRowsList .row ').click(function () {
            var row = $j(this);
            if ($j(this).hasClass('twistie_close')) {
                $j(this).parent().next().next().show();
                $j(this).removeClass("twistie_close");
                $j(this).addClass("twistie_open");
                var termId = $j(this).attr("termId");
                showRolePrivileges();
                $t.getTermAttributes(termId, {
                    success: function (data) {
                        if (Boolean(data)) {
                            var termInfo = data.termInfo;
                            var defaultImg = $j("#contextRootPath").val() + "/images/default.jpg";
                            var detailsSec = row.parent().siblings('.viewDtlsRow[termId=' + row.attr('termId') + '] ');
                            var termImage = (termInfo.photoPath == null) ? defaultImg : termInfo.photoPath;
                            var termAttrIds = {
                                sourceId: 'sourceId_' + termId,
                                targetId: 'targetId_' + termId,
                                posId: 'posId_' + termId,
                                formId: 'formId_' + termId,
                                categoryId: 'categoryId_' + termId,
                                domainId: 'domainId_' + termId,
                                notesId: 'notesId_' + termId,
                                defId: 'defId_' + termId,
                                usageId: 'usageId_' + termId,
                                changePicId: 'changePicId_' + termId,
                                uploadedImageId: 'uploadedImageId_' + termId,
                                imagePath: termImage

                            }
                            var editTermTmpl = new Template(termAttrTmpl).evaluate(termAttrIds);
                            detailsSec.html(editTermTmpl);
//							var suggTrmListTmplClone = suggTrmListTmpl;
                            var deprecatedTermInfoSecClone = deprecatedTermInfoSec;


                            if (termInfo != null) {
                                var termUsage = (termInfo.termUsage == null) ? "" : termInfo.termUsage;
                                var conceptDefinition = (termInfo.conceptDefinition == null) ? "" : termInfo.conceptDefinition;
                                var termNotes = (termInfo.termNotes == null) ? "" : termInfo.termNotes;
                                var categoryName = (termInfo.termCategory == null || termInfo.termCategory.category == null) ? "" : termInfo.termCategory.category;
                                var formName = (termInfo.termForm == null || termInfo.termForm.formName == null) ? "" : termInfo.termForm.formName;
                                var partOfSpeech = (termInfo.termPOS == null || termInfo.termPOS.partOfSpeech == null) ? "" : termInfo.termPOS.partOfSpeech;
                                var targetTerm = (termInfo.suggestedTerm == null || termInfo.suggestedTerm == null) ? "" : termInfo.suggestedTerm;
                                var termBeingPolled = (termInfo.termBeingPolled == null || termInfo.termBeingPolled == null) ? "" : termInfo.termBeingPolled;
                                var domainName = (termInfo.termDomain == null || termInfo.termDomain.domain == null) ? "" : termInfo.termDomain.domain;

                                detailsSec.find('.sourceDesc').html(termBeingPolled);
                                detailsSec.find(".sourceDescEdit").html(termBeingPolled);
                                detailsSec.find('.sourceDesc').each(function (i) {
                                    var sourceTermText = $j(this).html();
                                    sourceTermText = Util.backgroundYellow(sourceTermText, searchText, searchType, caseFlag);
                                    $j(this).html(sourceTermText);
                                });


//                                if (searchBy != null) {
//                                    var sourceTermText = termBeingPolled;
//                                    sourceTermText = Util.backgroundYellow(sourceTermText, searchText, searchType, caseFlag);
//                                    detailsSec.find('.sourceDescEdit').html(sourceTermText);
//                                }
//								detailsSec.find('.sourceDescEdit').html(termBeingPolled);

//								detailsSec.find('.targetDesc').html(Util.wordWrap(100,"<br/>",false,targetTerm));
                                detailsSec.find('.targetDesc').html(targetTerm);


                                detailsSec.find('.targetDesc').each(function (i) {
                                    var targetTermText = $j(this).html();
                                    targetTermText = Util.backgroundYellow(targetTermText, searchText, searchType, caseFlag);
                                    $j(this).html(targetTermText);
                                });

                                detailsSec.find('.targetDescEdit').html(targetTerm);
                                detailsSec.find('.termDomainDesc').html(domainName);
                                detailsSec.find('.usageDesc').html(termUsage);
                                detailsSec.find('.usageDescEdit').html(termUsage);
                                detailsSec.find('.definitionDesc').html(conceptDefinition);
                                detailsSec.find('.definitionDescEdit').html(conceptDefinition);
                                detailsSec.find('.domainDesc').html(termNotes);
                                detailsSec.find('.domainDescEdit').html(termNotes);
//								detailsSec.find('.programDesc').html(programName);\
                                detailsSec.find('.categoryDesc').html(categoryName);
                                detailsSec.find('.formDesc').html(formName);
                                detailsSec.find('.termTypeDesc').html(partOfSpeech);

                                var origDef = detailsSec.find('.domainDesc').html();
                                if (origDef.length > 20) {
                                    var finalText = origDef.substring(0, 16);
                                    finalText = finalText + "...";
                                    detailsSec.find('.domainDesc').text(finalText);
                                    detailsSec.find('.domainDesc').attr("title", origDef);
                                }
                            }

                            if (data.deprecatedTermInfo.length > 0) {
                                var finalSource = "";
                                var finalTarget = "";
                                for (var i = 0; i < data.deprecatedTermInfo.length; i++) {
                                    if (data.deprecatedTermInfo[i].deprecatedSource != null && $j.trim(data.deprecatedTermInfo[i].deprecatedSource) != "")
                                        finalSource = finalSource + data.deprecatedTermInfo[i].deprecatedSource + "\n";
                                    if (data.deprecatedTermInfo[i].deprecatedTarget != null && $j.trim(data.deprecatedTermInfo[i].deprecatedTarget) != "")
                                        finalTarget = finalTarget + data.deprecatedTermInfo[i].deprecatedTarget + "\n";
                                }

                                finalSource = finalSource.substring(0, finalSource.lastIndexOf("\n"));
                                finalTarget = finalTarget.substring(0, finalTarget.lastIndexOf("\n"));
                                var sourceTerm = $j.trimText(finalSource, 30);
                                if (searchBy != null) {
                                    sourceTerm = Util.backgroundYellow(sourceTerm, searchText, searchType, caseFlag);
                                }

                                deprecatedTermInfoSecClone[1] = sourceTerm;
                                var targetTerm = $j.trimText(finalTarget, 30);
                                if (searchBy != null) {
                                    targetTerm = Util.backgroundYellow(targetTerm, searchText, searchType, caseFlag);
                                }


                                deprecatedTermInfoSecClone[3] = targetTerm;
                                detailsSec.append(deprecatedTermInfoSecClone.join(""));
                                $j(".source").hover(function () {
                                    if ($j(this).attr("title") == undefined) {
                                        $j(this).attr("title", finalSource);
                                    }

                                });
                                $j(".target").hover(function () {
                                    if ($j(this).attr("title") == undefined) {
                                        $j(this).attr("title", finalTarget);
                                    }

                                });
                            }
                            detailsSec.append(editDetailsSec);

                            if ($j("body").hasClass('adminOvr')) {
                                detailsSec.find('.votesBtn').hide();
                                detailsSec.find('.commentsFld').hide();
                                detailsSec.find('.newTermFld').hide();
                                detailsSec.find('input:radio').hide();
                            }
                            detailsSec.find(".picture").hover(function () {
                                modalRender.bubble(".picture", "click for full image", "left center", "right center");
                            });
                            detailsSec.find(".picture").click(function () {
                                $t.getTermAttributes(termId, {
                                    success: function (imgData) {
                                        if (data != null && data.termInfo != null) {
                                            var hoverImagepath = (imgData.termInfo.photoPath == null) ? defaultImg : imgData.termInfo.photoPath;
                                            showPicture();
                                            $j("#showImage").find("img").attr('src', hoverImagepath);
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

                            $j(".changeTermPic").click(function () {
                                var id = $j(this).parent().attr("id");
                                pictureId = id.replace("changePicId_", "");
                                showBrowsePic();
                            });

                            //Edit button
                            detailsSec.find('.editDetails').click(function () {
                                row.find('.editDetailsFld').show();
                                row.find('.viewDetailsFld').hide();
//								if(searchBy!=null && searchBy!= "Enter term to search..."){
//									var  sourceTermText=detailsSec.find('.sourceDescEdit').html();
//									 sourceTermText=Util.backgroundYellow(targetTerm,searchText,searchType);
//									detailsSec.find('.sourceDescEdit').html(sourceTermText);
//								}
                                $t.getPOSList({
                                    success: function (data) {
                                        displayPosList(data, '.editTermPOS');
                                        var selectedPos = row.find('.tPartsOfSpeech').html();
                                        $j("select[name='editTermPOS'] option").each(function () {
                                            if ($j(this).text() == selectedPos)
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
                                        displayDomainList(data, '.domain');
                                        var selectedDomain = detailsSec.find('.termDomainDesc').html();
                                        $j("select[name='domain'] option").each(function () {
                                            if ($j(this).text() == selectedDomain)
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


                                // Term Form Picklist
                                $t.getFormList({
                                    success: function (data) {
                                        displayTermFormList(data, '.termForm');
                                        var selectedForm = detailsSec.find('.formDesc').html();
                                        $j("select[name='termForm'] option").each(function () {
                                            if ($j(this).text() == selectedForm)
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
                                $t.getCategoryList({
                                    success: function (data) {
                                        displayTermCatList(data, ".category");
                                        var selectedCat = detailsSec.find('.categoryDesc').html();
                                        $j("select[name='category'] option").each(function () {
                                            if ($j(this).text() == selectedCat)
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

                                detailsSec.find('.editDetailsFld').show();

                                detailsSec.find('.viewDetailsFld').hide();
                                $(this).hide();
                                detailsSec.find('.updateDetails').show();
                                detailsSec.find('.termAttr').css("padding-bottom", "15px");

                            });
                            row.find(".editDetailsFld select").click(function (event) {
                                $j(this).focus();
                                event.stopPropagation();


                            });
                            detailsSec.find('.updateDetails').click(function () {
                                detailsSec.hide();
                                row.find('.editDetailsFld').hide();
                                row.find('.viewDetailsFld').show();
                                row.removeClass("twistie_open");
                                row.addClass("twistie_close");
                                row.find('.editDate').hide();
                                row.find('.viewDate').show();
                                var termId = row.attr('termId');
                                saveTermDetails(termId);
                                var sourceTerm = $j("#sourceId_" + termId).val();
                                var suggTerm = $j("#targetId_" + termId).val();

                                var termPOS = $j("#posId_" + termId + " :selected").text();
                                var termCat = $j("#categoryId_" + termId + " :selected").text();
                                var term = (termPOS == "---Select---") ? "&nbsp;" : termPOS;
                                var trmDomain = $j("#domainId_" + termId + " :selected").text();
                                var showCategory = (termCat == "---Select---") ? "&nbsp;" : termCat;
                                var topSuggestedTerm = (suggTerm == null || suggTerm == "") ? "&nbsp;" : suggTerm;
                                var showDomain = (trmDomain == "---Select---") ? "&nbsp;" : trmDomain;
                                row.find('.tPartsOfSpeech').html(term).show();
                                row.find('.tCategory').html(showCategory).show();
                                row.find('.tDomain').html(showDomain).show();
                                row.find('.targetTrm').html(topSuggestedTerm).show();
                                row.find('.targetTrm').each(function (i) {
                                    var origText = $j(this).text();

                                    if (origText.length > 14) {
                                        var finalText = origText.substring(0, 12);
                                        finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                        finalText = finalText + "...";

                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    } else {

                                        var origText = $j(this).text();
                                        origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);

                                        $j(this).html(origText);
                                    }
                                });
                                row.find(".pollTerm").html(sourceTerm).show();
                                row.find('.pollTerm').each(function (i) {
                                    var origText = $j(this).text();

                                    if (origText.length > 14) {
                                        var finalText = origText.substring(0, 12);
                                        finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                        finalText = finalText + "...";

                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    } else {

                                        var origText = $j(this).text();
                                        origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);

                                        $j(this).html(origText);
                                    }
                                });
                            });
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
            else {
                $j(this).parent().next().next().hide();
                $j(this).removeClass("twistie_open");
                $j(this).addClass("twistie_close");
                $j(this).find('.editDate').hide();
                $j(this).find('.viewDate').show();
                $j(this).find('.editDetailsFld').hide();
                $j(this).find('.viewDetailsFld').show();
            }
        });


    };
    var bindEvnts = function (data) {
        var classNamesClone = classNames.slice(0);
        $j(".rowBg .row .modify .modifyImg").click(function (event) {

            event.stopPropagation();
            var termId = $j(this).attr("editid");
            var row = $j("#rowid_" + termId);
            $j("#rowid_" + termId).parent().next().next().show();
            $j("#rowid_" + termId).removeClass("twistie_close");
            $j("#rowid_" + termId).addClass("twistie_open");
            showRolePrivileges();

            $t.getTermAttributes(termId, {
                success: function (data) {
                    if (Boolean(data)) {
                        var termInfo = data.termInfo;
                        var defaultImg = $j("#contextRootPath").val() + "/images/default.jpg";
                        var detailsSec = row.parent().siblings('.viewDtlsRow[termId=' + row.attr('termId') + '] ');
                        var termImage = (termInfo.photoPath == null) ? defaultImg : termInfo.photoPath;
                        var termAttrIds = {
                            sourceId: 'sourceId_' + termId,
                            targetId: 'targetId_' + termId,
                            posId: 'posId_' + termId,
                            formId: 'formId_' + termId,
                            categoryId: 'categoryId_' + termId,
                            domainId: 'domainId_' + termId,
                            notesId: 'notesId_' + termId,
                            defId: 'defId_' + termId,
                            usageId: 'usageId_' + termId,
                            changePicId: 'changePicId_' + termId,
                            uploadedImageId: 'uploadedImageId_' + termId,
                            imagePath: termImage
                        }
                        var editTermTmpl = new Template(termAttrTmpl).evaluate(termAttrIds);
                        detailsSec.html(editTermTmpl);
                        var deprecatedTermInfoSecClone = deprecatedTermInfoSec;

                        if (termInfo != null) {
                            var termUsage = (termInfo.termUsage == null) ? "" : termInfo.termUsage;
                            var conceptDefinition = (termInfo.conceptDefinition == null) ? "" : termInfo.conceptDefinition;
                            var termNotes = (termInfo.termNotes == null) ? "" : termInfo.termNotes;
                            var categoryName = (termInfo.termCategory == null || termInfo.termCategory.category == null) ? "" : termInfo.termCategory.category;
                            var formName = (termInfo.termForm == null || termInfo.termForm.formName == null) ? "" : termInfo.termForm.formName;
                            var partOfSpeech = (termInfo.termPOS == null || termInfo.termPOS.partOfSpeech == null) ? "" : termInfo.termPOS.partOfSpeech;
                            var suggestedTerm = (termInfo.suggestedTerm == null || termInfo.suggestedTerm == null) ? "" : termInfo.suggestedTerm;
                            var termBeingPolled = (termInfo.termBeingPolled == null || termInfo.termBeingPolled == null) ? "" : termInfo.termBeingPolled;
                            var domainName = (termInfo.termDomain == null || termInfo.termDomain.domain == null) ? "" : termInfo.termDomain.domain;
                            detailsSec.find('.sourceDesc').html(termBeingPolled);
                            detailsSec.find('.sourceDescEdit').html(termBeingPolled);
//                            if (searchBy != null) {
//                                var sourceTermText = termBeingPolled;
//                                sourceTermText = Util.backgroundYellow(sourceTermText, searchText, searchType, caseFlag);
//                                detailsSec.find('.sourceDescEdit').html(sourceTermText);
//                            }
                            detailsSec.find('.targetDesc').html(suggestedTerm);
                            detailsSec.find('.targetDescEdit').html(suggestedTerm);
                            detailsSec.find('.termDomainDesc').html(domainName);
                            detailsSec.find('.usageDesc').html(termUsage);
                            detailsSec.find('.usageDescEdit').html(termUsage);
                            detailsSec.find('.definitionDesc').html(conceptDefinition);
                            detailsSec.find('.definitionDescEdit').html(conceptDefinition);
                            detailsSec.find('.domainDesc').html(termNotes);
                            detailsSec.find('.domainDescEdit').html(termNotes);
//								detailsSec.find('.programDesc').html(programName);\
                            detailsSec.find('.categoryDesc').html(categoryName);
                            detailsSec.find('.formDesc').html(formName);
                            detailsSec.find('.termTypeDesc').html(partOfSpeech);
                        }
//							for(var i=0;i<data.suggestedTermDetails.length;i++){
//								var numRand = Math.floor(Math.random()*101);
//								suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
//								suggTrmListTmplClone[3] = row.attr('termId');
//								suggTrmListTmplClone[5] = "barId_"+row.attr('termId')+i;
//								suggTrmListTmplClone[7] = data.suggestedTermDetails[i].noOfVotes;
//								suggTrmListTmplClone[9] = data.suggestedTermDetails[i].suggestedTermId;
//								detailsSec.append(suggTrmListTmplClone.join(""));
//							}
//							newTermSec[1] = suggTrmListTmplClone[3];
//							newTermSec[3] = "sugg_"+suggTrmListTmplClone[3];
//							newTermSec[5] = "submitVote"+suggTrmListTmplClone[3];
//							detailsSec.append(newTermSec.join(""));


                        if (data.deprecatedTermInfo.length > 0) {
                            var finalSource = "";
                            var finalTarget = "";
                            for (var i = 0; i < data.deprecatedTermInfo.length; i++) {
                                if (data.deprecatedTermInfo[i].deprecatedSource != null && $j.trim(data.deprecatedTermInfo[i].deprecatedSource) != "")
                                    finalSource = finalSource + data.deprecatedTermInfo[i].deprecatedSource + "\n";
                                if (data.deprecatedTermInfo[i].deprecatedTarget != null && $j.trim(data.deprecatedTermInfo[i].deprecatedTarget) != "")
                                    finalTarget = finalTarget + data.deprecatedTermInfo[i].deprecatedTarget + "\n";
                            }

                            finalSource = finalSource.substring(0, finalSource.lastIndexOf("\n"));
                            finalTarget = finalTarget.substring(0, finalTarget.lastIndexOf("\n"));

                            var sourceTerm = $j.trimText(finalSource, 30);
                            if (searchBy != null) {
                                sourceTerm = Util.backgroundYellow(sourceTerm, searchText, searchType, caseFlag);
                            }

                            deprecatedTermInfoSecClone[1] = sourceTerm;
                            var targetTerm = $j.trimText(finalTarget, 30);
                            if (searchBy != null) {
                                targetTerm = Util.backgroundYellow(targetTerm, searchText, searchType, caseFlag);
                            }


                            deprecatedTermInfoSecClone[3] = targetTerm;
                            detailsSec.append(deprecatedTermInfoSecClone.join(""));
                            $j(".source").hover(function () {
                                if ($j(this).attr("title") == undefined) {
                                    $j(this).attr("title", finalSource);
                                }

                            });
                            $j(".target").hover(function () {
                                if ($j(this).attr("title") == undefined) {
                                    $j(this).attr("title", finalTarget);
                                }

                            });
                        }

                        detailsSec.append(editDetailsSec);


                        if ($j("body").hasClass('adminOvr')) {
                            detailsSec.find('.votesBtn').hide();
                            detailsSec.find('.commentsFld').hide();
                            detailsSec.find('.newTermFld').hide();
                            detailsSec.find('input:radio').hide();
                        }

                        /*Finding highest no. of votes*/
                        var largest = {
                            val: null
                        };
                        for (var i in data.suggestedTermDetails) {
                            if (data.suggestedTermDetails[i].noOfVotes == 0) {

                            }
                            if (data.suggestedTermDetails[i].noOfVotes > largest.val) {
                                largest.val = data.suggestedTermDetails[i].noOfVotes;

                            }
                        }
                        $j(".picture").hover(function () {
                            modalRender.bubble(".picture", "click for full image", "left center", "right center");
                        });
                        $j(".picture").click(function () {
                            $t.getTermAttributes(termId, {
                                success: function (imgData) {
                                    if (data != null && data.termInfo != null) {
                                        var hoverImagepath = (imgData.termInfo.photoPath == null) ? defaultImg : imgData.termInfo.photoPath;
                                        showPicture();
                                        $j("#showImage").find("img").attr('src', hoverImagepath);
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
                        //find highest
                        var blockSize = 240 / largest.val;

                        detailsSec.find('.votesBar').each(function (i) {
                            $j(this).addClass(classNamesClone[classNamesClone.length - 1]);
                            classNamesClone.pop();
                            if (classNamesClone.length == 0)
                                classNamesClone = classNames.slice(0);
                            if (data.suggestedTermDetails[i].noOfVotes == 0) {
                                $j(this).width("0");
                                $j(this).css("margin-right", "10px");
                            }
                            else {
                                $j(this).width(blockSize * data.suggestedTermDetails[i].noOfVotes);

                                /*Displaying qTip on mouseover*/
                                $j(this).hover(function () {
                                    modalRender.bubble("#barId_" + row.attr('termId') + i, data.suggestedTermDetails[i].votersNames, "left center", "right center");
                                });
                            }

                        });


                        detailsSec.find('.suggTxt').focus(function () {
                            $j(this).val('');
                        });
                        detailsSec.find('.suggTxt').blur(function () {
                            if ($j(this).val() == '')
                                $j(this).val('Enter new suggestion');
                        });

                        $j(".changeTermPic").click(function () {
                            var id = $j(this).parent().attr("id");
                            pictureId = id.replace("changePicId_", "");
                            showBrowsePic();
                        });

                        //Edit Details
                        detailsSec.find('.editDetails').click(function () {
                            row.find('.editDetailsFld').show();
                            row.find('.viewDetailsFld').hide();
//																	detailsSec.find('.termAttr').css("padding-bottom","68px");

//								$j(".termPOS").html("<option>---Select---</option>");
//								$j(".termCatLst").html("<option>---Select---</option>");
                            $t.getPOSList({
                                success: function (data) {
                                    displayPosList(data, '.editTermPOS');
                                    var selectedPos = detailsSec.find('.termTypeDesc').html();
                                    $j("select[name='editTermPOS'] option").each(function () {
                                        if ($j(this).text() == selectedPos)
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
                                    displayDomainList(data, '.domain');
                                    var selectedDomain = detailsSec.find('.termDomainDesc').html();
                                    $j("select[name='domain'] option").each(function () {
                                        if ($j(this).text() == selectedDomain)
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


                            // Term Form Picklist
                            $t.getFormList({
                                success: function (data) {
                                    displayTermFormList(data, '.termForm');
                                    var selectedForm = detailsSec.find('.formDesc').html();
                                    $j("select[name='termForm'] option").each(function () {
                                        if ($j(this).text() == selectedForm)
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
                            $t.getCategoryList({
                                success: function (data) {
                                    displayTermCatList(data, ".category");
                                    var selectedCat = detailsSec.find('.categoryDesc').html();
                                    $j("select[name='category'] option").each(function () {
                                        if ($j(this).text() == selectedCat)
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

                            detailsSec.find('.editDetailsFld').show();
                            detailsSec.find('.viewDetailsFld').hide();
                            $(this).hide();
                            detailsSec.find('.updateDetails').show();
                            detailsSec.find('.termAttr').css("padding-bottom", "15px");

                        });
                        detailsSec.find('.editDetails').trigger('click');
                        row.find(".editDetailsFld select").click(function (event) {
                            $j(this).focus();
                            event.stopPropagation();


                        });
                        detailsSec.find('.updateDetails').click(function () {
                            detailsSec.hide();
                            row.find('.editDetailsFld').hide();
                            row.find('.viewDetailsFld').show();
                            row.removeClass("twistie_open");
                            row.addClass("twistie_close");
                            row.find('.editDate').hide();
                            row.find('.viewDate').show();
                            var termId = row.attr('termId');
                            saveTermDetails(termId);
                            var suggTerm = $j("#targetId_" + termId).val();

                            var termPOS = $j("#posId_" + termId + " :selected").text();
                            var termCat = $j("#categoryId_" + termId + " :selected").text();
                            var term = (termPOS == "---Select---") ? "&nbsp;" : termPOS;
                            var trmDomain = $j("#domainId_" + termId + " :selected").text();
                            var showCategory = (termCat == "---Select---") ? "&nbsp;" : termCat;
                            var topSuggestedTerm = (suggTerm == null || suggTerm == "") ? "&nbsp;" : suggTerm;
                            var showDomain = (trmDomain == "---Select---") ? "&nbsp;" : trmDomain;
                            row.find('.tPartsOfSpeech').html(term).show();
                            row.find('.tCategory').html(showCategory).show();
                            row.find('.tDomain').html(showDomain).show();
                            row.find('.targetTrm').html(topSuggestedTerm).show();
                            row.find('.targetTrm').each(function (i) {
                                var origText = $j(this).text();

                                if (origText.length > 14) {
                                    var finalText = origText.substring(0, 12);
                                    finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);

                                    finalText = finalText + "...";

                                    $j(this).html(finalText);
                                    $j(this).attr("title", origText);
                                } else {

                                    var origText = $j(this).text();
                                    origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                                    $j(this).html(origText);

                                }

                            });


                        });

                        //Extend Poll

                        detailsSec.find('.extendPoll').click(function () {

                            if ((row.find('.viewDate').html()) == "") {

                                alertMessage("#termExtendMsg");
                            }
                            else {
                                row.find('.editDate').show();
                                row.find('.viewDate').hide();
                                var curDt = row.find('.viewDate').text();
                                row.find('.editDate input').val(curDt);
                                detailsSec.find('.updateDate').show();
                                $(this).hide();
                                row.find(".hasDatepicker").click(function (event) {
                                    $j(this).focus();
                                    event.stopPropagation();
                                });
                            }
                        });

                        detailsSec.find('.updateDate').click(function () {
                            var editedDate = row.find('.hasDatepicker').val();
                            if (isDate(editedDate)) {
                                row.find('.editDate').hide();
                                row.find('.viewDate').show();
                                row.find('.editDetailsFld').hide();
                                row.find('.viewDetailsFld').show();
                                detailsSec.hide();
                                row.removeClass("twistie_open");
                                row.addClass("twistie_close");
                                var termId = row.attr('termId');
                                var pollDate = $j("#datepicker" + termId).val();
                                extendPoll(pollDate, termId);
                                row.find('.viewDate').html(pollDate).show();
                                row.parent().show();
                            } else {
                                alertMessage("#dateAlet");
                                row.find('.hasDatepicker').val("");

                            }
                        });

                        //Pick as Final button functionality
                        $j('.pickFinalBtn').click(function () {

                            var suggestedTermId = $j(this).attr("id");
                            var termId = row.attr('termId');
                            finaliseTerm(suggestedTermId);
                            detailsSec.hide();
                            row.removeClass("twistie_open");
                            row.addClass("twistie_close");
                            row.find('.tickImg').css("visibility", "visible");
                            row.find('.targetTrm').each(function (i) {
                                var origText = $j(this).text();

                                if (origText.length > 14) {
                                    var finalText = origText.substring(0, 12);
                                    finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                    finalText = finalText + "...";

                                    $j(this).html(finalText);
                                    $j(this).attr("title", origText);
                                } else {

                                    var origText = $j(this).text();
                                    origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                                    $j(this).html(origText);

                                }


                            });
                            row.find('.editDetailsFld').hide();
                            row.find('.viewDetailsFld').show();
                            row.find('.editDate').hide();
                            row.find('.viewDate').show();

                        });
                        detailsSec.find('.votingDetails').click(function () {
                            var termId = row.attr('termId');
                            if ($j('#votiongDetailsTab').length > 0) {
                                var oTable = $j('#votiongDetailsTab').dataTable({
                                    "bProcessing": true,
                                    "sServerMethod": "GET",
                                    "bAutoWidth": false,
                                    "bDestroy": true,
                                    "sAjaxDataProp": "termData",
                                    "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getvotingStatus/" + termId,
                                    "aoColumns": [
                                        { "mDataProp": "userName", "sWidth": "20%"},
                                        { "mDataProp": "votingStatus", "sWidth": "20%" },
                                        { "mDataProp": "votedTerm", "sWidth": "20%", "sClass": "alignCenter"},
                                        { "mDataProp": "userComments", "sWidth": "25%"},
                                        { "mDataProp": "votedDate", "sWidth": "15%"}
                                    ],
                                    "iDisplayLength": 10
                                });

                            }
                            $j('.dataTables_length').hide();
                            showVotingDetails(data);

                        });
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


    };
    var bindEvent = function (data) {
        var classNamesClone = classNames.slice(0);
        $j('#tmDtlRowsList .row ').click(function () {
            var row = $j(this);
            if ($j(this).hasClass('twistie_close')) {
                $j(this).parent().next().next().show();
                $j(this).removeClass("twistie_close");
                $j(this).addClass("twistie_open");
                var tmProfileInfoId = $j(this).attr("termId");
                showRolePrivileges();
                $t.getTmAttributes(tmProfileInfoId, {
                    success: function (data) {
                        if (Boolean(data)) {
                            var tmInfo = data;
                            var defaultImg = $j("#contextRootPath").val() + "/images/default.jpg";
                            var detailsSec = row.parent().siblings('.viewDtlsRow[termId=' + row.attr('termId') + '] ');
                            var tmAttrIds = {
                                sourceId: 'sourceId_' + tmProfileInfoId,
                                targetId: 'targetId_' + tmProfileInfoId,
                                productId: 'productId_' + tmProfileInfoId,
                                domainId: 'domainId_' + tmProfileInfoId,
                                contentId: 'contentId_' + tmProfileInfoId

                            }

                            var editTmTmpl = new Template(tmAttrTmpl).evaluate(tmAttrIds);
                            detailsSec.html(editTmTmpl);
//						var suggTrmListTmplClone = suggTrmListTmpl;
                            var deprecatedTermInfoSecClone = deprecatedTermInfoSec;

                            if (tmInfo != null) {
                                var tmDomain = (tmInfo.domain == null || tmInfo.domain.domain == null) ? "" : tmInfo.domain.domain;
                                var tmContent = (tmInfo.contentType == null || tmInfo.contentType.contentType == null) ? "" : tmInfo.contentType.contentType;
                                var tmProductLine = (tmInfo.productGroup == null || tmInfo.productGroup.product == null) ? "" : tmInfo.productGroup.product;
                                var source = (tmInfo.source == null || tmInfo.source == null) ? "" : tmInfo.source;
                                var target = (tmInfo.target == null || tmInfo.target == null) ? "" : tmInfo.target;
                                detailsSec.find('.sourceDesc').html(source);
                                detailsSec.find('.sourceDesc').each(function (i) {

                                    var sourceTermText = $j(this).html();
                                    sourceTermText = Util.backgroundYellow(sourceTermText, searchText, searchType, caseFlag);


                                    $j(this).html(sourceTermText);
                                });

                                detailsSec.find('.sourceDescEdit').html(source);

//						detailsSec.find('.targetDesc').html(Util.wordWrap(102,"<br/>",false,target));
                                detailsSec.find('.targetDesc').html(target);
                                detailsSec.find('.targetDesc').each(function (i) {

                                    var targetTermText = $j(this).html();
                                    targetTermText = Util.backgroundYellow(targetTermText, searchText, searchType, caseFlag);

                                    $j(this).html(targetTermText);
                                });
                                detailsSec.find('.targetDescEdit').html(target);
                                detailsSec.find('.contentDesc').html(tmContent);
                                detailsSec.find('.domainDesc').html(tmDomain);
                                detailsSec.find('.productDesc').html(tmProductLine);

                            }


                            detailsSec.append(editDetailsSec);


                            if ($j("body").hasClass('adminOvr')) {
                                detailsSec.find('.votesBtn').hide();
                                detailsSec.find('.commentsFld').hide();
                                detailsSec.find('.newTermFld').hide();
                                detailsSec.find('input:radio').hide();
                            }

                            /*Finding highest no. of votes*/
                            var largest = {
                                val: null
                            };
                            for (var i in data.suggestedTermDetails) {
                                if (data.suggestedTermDetails[i].noOfVotes == 0) {

                                }
                                if (data.suggestedTermDetails[i].noOfVotes > largest.val) {
                                    largest.val = data.suggestedTermDetails[i].noOfVotes;

                                }
                            }


                            //find highest
                            var blockSize = 240 / largest.val;

                            detailsSec.find('.votesBar').each(function (i) {
                                $j(this).addClass(classNamesClone[classNamesClone.length - 1]);
                                classNamesClone.pop();
                                if (classNamesClone.length == 0)
                                    classNamesClone = classNames.slice(0);
                                if (data.suggestedTermDetails[i].noOfVotes == 0) {
                                    $j(this).width("0");
                                    $j(this).css("margin-right", "10px");
                                }
                                else {
                                    $j(this).width(blockSize * data.suggestedTermDetails[i].noOfVotes);

                                    /*Displaying qTip on mouseover*/
                                    $j(this).hover(function () {
                                        modalRender.bubble("#barId_" + row.attr('termId') + i, data.suggestedTermDetails[i].votersNames, "left center", "right center");
                                    });
                                }

                            });


                            detailsSec.find('.suggTxt').focus(function () {
                                $j(this).val('');
                            });
                            detailsSec.find('.suggTxt').blur(function () {
                                if ($j(this).val() == '')
                                    $j(this).val('Enter new suggestion');
                            });


                            //Edit Details
                            detailsSec.find('.editDetails ').click(function () {
                                row.find('.editDetailsFld').show();
                                row.find('.viewDetailsFld').hide();
                                detailsSec.find('.termAttr').css("padding-bottom", "68px");

//							$j(".termPOS").html("<option>---Select---</option>");
//							$j(".termCatLst").html("<option>---Select---</option>");
                                $t.getProductGroupList({
                                    success: function (data) {
                                        displayProductGroupList(data, '.editProductLine');
                                        var selectedPos = detailsSec.find('.productDesc').html();
                                        $j("select[name='editProductLine'] option").each(function () {
                                            if ($j(this).text() == selectedPos)
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

                                $t.getContentTypeList({
                                    success: function (data) {
                                        displayContentList(data, '.editContent');
                                        var selectedContent = detailsSec.find('.contentDesc').html();
                                        $j("select[name='editContent'] option").each(function () {
                                            if ($j(this).text() == selectedContent)
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


                                // Term Form Picklist
                                $t.getDomainList({
                                    success: function (data) {
                                        displayDomainList(data, '.editDomain');
                                        var selectedDomain = detailsSec.find('.domainDesc').html();
                                        $j("select[name='editDomain'] option").each(function () {
                                            if ($j(this).text() == selectedDomain)
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

                                detailsSec.find('.editDetailsFld').show();
                                detailsSec.find('.viewDetailsFld').hide();
                                $(this).hide();
                                detailsSec.find('.sourceDescEdit').each(function (i) {
                                    if (searchBy != null && searchBy != "Enter term to search...") {
                                        var sourceTermText = $j(this).html();

                                        sourceTermText = Util.backgroundYellow(sourceTermText, searchText, searchType, caseFlag);


                                        $j(this).html(sourceTermText);
                                    }

                                });
                                detailsSec.find('.updateDetails').show();
                                detailsSec.find('.termAttr').css("padding-bottom", "15px");

                            });
                            row.find(".editDetailsFld select").click(function (event) {
                                $j(this).focus();
                                event.stopPropagation();


                            });
                            detailsSec.find('.updateDetails').click(function () {

                                detailsSec.hide();
                                row.find('.editDetailsFld').hide();
                                row.find('.viewDetailsFld').show();
                                row.removeClass("twistie_open");
                                row.addClass("twistie_close");
                                row.find('.editDate').hide();
                                row.find('.viewDate').show();
                                var tmProfileInfoId = row.attr('termId');
                                saveTmDetails(tmProfileInfoId);

                                var suggTerm = $j("#targetId_" + tmProfileInfoId).val();
                                var product = $j("#productId_" + tmProfileInfoId + " :selected").text();
                                var inDomain = $j("#domainId_" + tmProfileInfoId + " :selected").text();
                                var content = $j("#contentId_" + tmProfileInfoId + " :selected").text();
                                var pLine = (product == "---Select---") ? "&nbsp;" : product;
                                var contentTm = (content == "---Select---") ? "&nbsp;" : content;
                                var domainTm = (inDomain == "---Select---") ? "&nbsp;" : inDomain;
                                var topSuggestedTerm = (suggTerm == null || suggTerm == "") ? "&nbsp;" : suggTerm;
                                row.find('.productLine').html(pLine).show();
                                row.find('.contentType').html(contentTm).show();
                                row.find('.industryDomain').html(domainTm).show();
                                row.find('.targetTm').html(topSuggestedTerm).show();


                                row.find('.targetTm').each(function (i) {

                                    var origText = $j(this).text();
                                    if (origText.length > 14) {
                                        var finalText = origText.substring(0, 12);
                                        finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                        finalText = finalText + "...";

                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    } else {
                                        var origText = $j(this).text();
                                        origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                                        $j(this).html(origText);

                                    }

                                });


                                row.find('.industryDomain').each(function (i) {
                                    var origText = $j(this).text();
                                    if (origText.length > 10) {
                                        var finalText = origText.substring(0, 8);
                                        finalText = finalText + "...";
                                        $j(this).text(finalText);
                                        $j(this).attr("title", origText);
                                    }
                                });

                                row.find('.contentType').each(function (i) {
                                    var origText = $j(this).text();
                                    if (origText.length > 10) {
                                        var finalText = origText.substring(0, 8);
                                        finalText = finalText + "...";
                                        $j(this).text(finalText);
                                        $j(this).attr("title", origText);
                                    }
                                });


                                row.find('.productLine').each(function (i) {
                                    var origText = $j(this).text();
                                    if (origText.length > 10) {
                                        var finalText = origText.substring(0, 8);
                                        finalText = finalText + "...";
                                        $j(this).text(finalText);
                                        $j(this).attr("title", origText);
                                    }
                                });

                            });


                            //Pick as Final button functionality
                            $j('.pickFinalBtn').click(function () {

                                var suggestedTermId = $j(this).attr("id");
                                var termId = row.attr('termId');
                                finaliseTerm(suggestedTermId);
                                detailsSec.hide();
                                row.removeClass("twistie_open");
                                row.addClass("twistie_close");
                                row.find('.tickImg').css("visibility", "visible");
                                row.find(".targetTrm").text($j(this).parent().find(".termSuggestion").html());
                                row.find('.targetTm').each(function (i) {
                                    var origText = $j(this).text();

                                    if (origText.length > 14) {
                                        var finalText = origText.substring(0, 12);
                                        finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                        finalText = finalText + "...";

                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    } else {

                                        var origText = $j(this).text();
                                        origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                                        $j(this).html(origText);

                                    }


                                });
                                row.find('.editDetailsFld').hide();
                                row.find('.viewDetailsFld').show();
                                row.find('.editDate').hide();
                                row.find('.viewDate').show();

                            });

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
            else {
                $j(this).parent().next().next().hide();
                $j(this).removeClass("twistie_open");
                $j(this).addClass("twistie_close");
                $j(this).find('.editDate').hide();
                $j(this).find('.viewDate').show();
                $j(this).find('.editDetailsFld').hide();
                $j(this).find('.viewDetailsFld').show();
            }
        });

    };
    var bindEvnt = function (data) {
        var classNamesClone = classNames.slice(0);
        $j(".rowBg .row .modify .modifyTmImg").click(function (event) {

            event.stopPropagation();
            var tmProfileInfoId = $j(this).attr("editid");
            var row = $j("#rowid_" + tmProfileInfoId);
            $j("#rowid_" + tmProfileInfoId).parent().next().next().show();
            $j("#rowid_" + tmProfileInfoId).removeClass("twistie_close");
            $j("#rowid_" + tmProfileInfoId).addClass("twistie_open");
            showRolePrivileges();
            $t.getTmAttributes(tmProfileInfoId, {
                success: function (data) {
                    if (Boolean(data)) {
                        var tmInfo = data;
                        var defaultImg = $j("#contextRootPath").val() + "/images/default.jpg";
                        var detailsSec = row.parent().siblings('.viewDtlsRow[termId=' + row.attr('termId') + '] ');
                        var tmAttrIds = {
                            sourceId: 'sourceId_' + tmProfileInfoId,
                            targetId: 'targetId_' + tmProfileInfoId,
                            productId: 'productId_' + tmProfileInfoId,
                            domainId: 'domainId_' + tmProfileInfoId,
                            contentId: 'contentId_' + tmProfileInfoId

                        }
                        var editTmTmpl = new Template(tmAttrTmpl).evaluate(tmAttrIds);
                        detailsSec.html(editTmTmpl);
//							var suggTrmListTmplClone = suggTrmListTmpl;
                        var deprecatedTermInfoSecClone = deprecatedTermInfoSec;

                        if (tmInfo != null) {
                            var tmDomain = (tmInfo.domain == null || tmInfo.domain.domain == null) ? "" : tmInfo.domain.domain;
                            var tmContent = (tmInfo.contentType == null || tmInfo.contentType.contentType == null) ? "" : tmInfo.contentType.contentType;
                            var tmProductLine = (tmInfo.productGroup == null || tmInfo.productGroup.product == null) ? "" : tmInfo.productGroup.product;
                            var source = (tmInfo.source == null || tmInfo.source == null) ? "" : tmInfo.source;
                            var target = (tmInfo.target == null || tmInfo.target == null) ? "" : tmInfo.target;


                            detailsSec.find('.sourceDesc').html(source);
                            detailsSec.find('.sourceDescEdit').html(source);
                            detailsSec.find('.sourceDescEdit').each(function (i) {

                                var sourceTermText = $j(this).html();
                                sourceTermText = Util.backgroundYellow(sourceTermText, searchText, searchType, caseFlag);
                                $j(this).html(sourceTermText);
                            });
                            detailsSec.find('.targetDesc').html(target);
                            detailsSec.find('.targetDescEdit').html(target);
                            detailsSec.find('.contentDesc').html(tmContent);
                            detailsSec.find('.domainDesc').html(tmDomain);
                            detailsSec.find('.productDesc').html(tmProductLine);

                        }
//							
                        detailsSec.append(editDetailsSec);


                        if ($j("body").hasClass('adminOvr')) {
                            detailsSec.find('.votesBtn').hide();
                            detailsSec.find('.commentsFld').hide();
                            detailsSec.find('.newTermFld').hide();
                            detailsSec.find('input:radio').hide();
                        }

                        /*Finding highest no. of votes*/
                        var largest = {
                            val: null
                        };
                        for (var i in data.suggestedTermDetails) {
                            if (data.suggestedTermDetails[i].noOfVotes == 0) {

                            }
                            if (data.suggestedTermDetails[i].noOfVotes > largest.val) {
                                largest.val = data.suggestedTermDetails[i].noOfVotes;

                            }
                        }


                        //find highest
                        var blockSize = 240 / largest.val;

                        detailsSec.find('.votesBar').each(function (i) {
                            $j(this).addClass(classNamesClone[classNamesClone.length - 1]);
                            classNamesClone.pop();
                            if (classNamesClone.length == 0)
                                classNamesClone = classNames.slice(0);
                            if (data.suggestedTermDetails[i].noOfVotes == 0) {
                                $j(this).width("0");
                                $j(this).css("margin-right", "10px");
                            }
                            else {
                                $j(this).width(blockSize * data.suggestedTermDetails[i].noOfVotes);

                                /*Displaying qTip on mouseover*/
                                $j(this).hover(function () {
                                    modalRender.bubble("#barId_" + row.attr('termId') + i, data.suggestedTermDetails[i].votersNames, "left center", "right center");
                                });
                            }

                        });


                        detailsSec.find('.suggTxt').focus(function () {
                            $j(this).val('');
                        });
                        detailsSec.find('.suggTxt').blur(function () {
                            if ($j(this).val() == '')
                                $j(this).val('Enter new suggestion');
                        });


                        //Edit Details
                        detailsSec.find('.editDetails ').click(function () {
                            row.find('.editDetailsFld').show();
                            row.find('.viewDetailsFld').hide();
                            detailsSec.find('.termAttr').css("padding-bottom", "68px");

//								$j(".termPOS").html("<option>---Select---</option>");
//								$j(".termCatLst").html("<option>---Select---</option>");
                            $t.getProductGroupList({
                                success: function (data) {
                                    displayProductGroupList(data, '.editProductLine');
                                    var selectedPos = detailsSec.find('.productDesc').html();
                                    $j("select[name='editProductLine'] option").each(function () {
                                        if ($j(this).text() == selectedPos)
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
                            $t.getContentTypeList({
                                success: function (data) {
                                    displayContentList(data, '.editContent');
                                    var selectedContent = detailsSec.find('.contentDesc').html();
                                    $j("select[name='editContent'] option").each(function () {
                                        if ($j(this).text() == selectedContent)
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


                            // Term Form Picklist
                            $t.getDomainList({
                                success: function (data) {
                                    displayDomainList(data, '.editDomain');
                                    var selectedDomain = detailsSec.find('.domainDesc').html();
                                    $j("select[name='editDomain'] option").each(function () {
                                        if ($j(this).text() == selectedDomain)
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

                            detailsSec.find('.editDetailsFld').show();
                            detailsSec.find('.viewDetailsFld').hide();
                            $(this).hide();
                            detailsSec.find('.updateDetails').show();
                            detailsSec.find('.termAttr').css("padding-bottom", "15px");

                        });
                        detailsSec.find('.editDetails').trigger('click')
                        row.find(".editDetailsFld select").click(function (event) {
                            $j(this).focus();
                            event.stopPropagation();


                        });
                        ;

                        detailsSec.find('.updateDetails').click(function () {

                            detailsSec.hide();
                            row.find('.editDetailsFld').hide();
                            row.find('.viewDetailsFld').show();
                            row.removeClass("twistie_open");
                            row.addClass("twistie_close");
                            row.find('.editDate').hide();
                            row.find('.viewDate').show();
                            var tmProfileInfoId = row.attr('termId');
                            saveTmDetails(tmProfileInfoId);
                            var suggTerm = $j("#targetId_" + tmProfileInfoId).val();
                            var product = $j("#productId_" + tmProfileInfoId + " :selected").text();
                            var inDomain = $j("#domainId_" + tmProfileInfoId + " :selected").text();
                            var content = $j("#contentId_" + tmProfileInfoId + " :selected").text();
                            var topSuggestedTerm = (suggTerm == null || suggTerm == "") ? "&nbsp;" : suggTerm;
                            var pLine = (product == "---Select---") ? "&nbsp;" : product;
                            var contentTm = (content == "---Select---") ? "&nbsp;" : content;
                            var domainTm = (inDomain == "---Select---") ? "&nbsp;" : inDomain;
                            row.find('.productLine').html(pLine).show();
                            row.find('.contentType').html(contentTm).show();
                            row.find('.industryDomain').html(domainTm).show();
                            row.find('.targetTm').html(topSuggestedTerm).show();

                            row.find('.targetTm').each(function (i) {

                                var origText = $j(this).text();

                                if (origText.length > 14) {
                                    var finalText = origText.substring(0, 12);
                                    finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                    finalText = finalText + "...";

                                    $j(this).html(finalText);
                                    $j(this).attr("title", origText);
                                } else {
                                    var origText = $j(this).text();
                                    origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                                    $j(this).html(origText);

                                }


                            });


                            ////
                            row.find('.contentType').each(function (i) {
                                var origText = $j(this).text();
                                if (origText.length > 10) {
                                    var finalText = origText.substring(0, 8);
                                    finalText = finalText + "...";
                                    $j(this).text(finalText);
                                    $j(this).attr("title", origText);
                                }


                            });

                            row.find('.productLine').each(function (i) {
                                var origText = $j(this).text();
                                if (origText.length > 10) {
                                    var finalText = origText.substring(0, 8);
                                    finalText = finalText + "...";
                                    $j(this).text(finalText);
                                    $j(this).attr("title", origText);
                                }


                            });

                            row.find('.industryDomain').each(function (i) {
                                var origText = $j(this).text();
                                if (origText.length > 10) {
                                    var finalText = origText.substring(0, 8);
                                    finalText = finalText + "...";
                                    $j(this).text(finalText);
                                    $j(this).attr("title", origText);
                                }


                            });


                            ////


                        });


                        //Pick as Final button functionality
                        $j('.pickFinalBtn').click(function () {

                            var suggestedTermId = $j(this).attr("id");
                            var termId = row.attr('termId');
                            finaliseTerm(suggestedTermId);
                            detailsSec.hide();
                            row.removeClass("twistie_open");
                            row.addClass("twistie_close");
                            row.find('.tickImg').css("visibility", "visible");
                            row.find(".targetTrm").text($j(this).parent().find(".termSuggestion").html());
                            row.find('.targetTm').each(function (i) {
                                var origText = $j(this).text();

                                if (origText.length > 14) {
                                    var finalText = origText.substring(0, 12);
                                    finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);

                                    finalText = finalText + "...";

                                    $j(this).html(finalText);
                                    $j(this).attr("title", origText);
                                } else {

                                    var origText = $j(this).text();
                                    origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                                    $j(this).html(origText);

                                }


                            });
                            row.find('.editDetailsFld').hide();
                            row.find('.viewDetailsFld').show();
                            row.find('.editDate').hide();
                            row.find('.viewDate').show();

                        });

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


    };


    /// ***tm start **///


    $j.fn.tmDetails = function () {

        var ctx = $j(this);

        var startWaiting = function (msg, selector, floatRight) {
            if (selector) {
                if (typeof msg == 'undefined' || msg == null) {
                    msg = " Loading... please wait.";
                }
                var temp = '<div class="loading-msg  topmargin15" style="text-align :center;"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif" />' + '&nbsp;' + msg + '</div>';
                selector.append(temp);

            }
        };
        var stopWaiting = function (selector) {
            if (selector) {
                selector.find(".loading-msg").hide();
            }
        };


        var displayTmDetails = function (data) {
            stopWaiting($j('#tmDtlRowsList'));
            var tmDetails = data.tmProfileList;
            if (data != null && data.tmProfileList != null) {
                var listLength = (tmDetails.length >= termListLimit) ? termListLimit : tmDetails.length;
                for (var count = 0; count < listLength; count++) {
                    var tmDtlTmplClone = tmDtlTmpl;
                    var IndustryDomain = (data.tmProfileList[count].domain == null || data.tmProfileList[count].domain == "") ? "&nbsp;" : data.tmProfileList[count].domain;
                    var productLine = (data.tmProfileList[count].productLine == null || data.tmProfileList[count].productLine == "") ? "&nbsp;" : data.tmProfileList[count].productLine;
                    var contentType = (data.tmProfileList[count].contentType == null || data.tmProfileList[count].contentType == "") ? "&nbsp;" : data.tmProfileList[count].contentType;
                    var company = (data.tmProfileList[count].company == null || data.tmProfileList[count].company == "") ? "&nbsp;" : data.tmProfileList[count].company;
                    tmDtlTmplClone[1] = data.tmProfileList[count].tmProfileId;
                    tmDtlTmplClone[3] = data.tmProfileList[count].tmProfileId;
                    tmDtlTmplClone[5] = (data.tmProfileList[count].termsBeingPolled == null || data.tmProfileList[count].termsBeingPolled == " " ) ? "&nbsp" : data.tmProfileList[count].termsBeingPolled;
                    tmDtlTmplClone[7] = (data.tmProfileList[count].suggestedterms == null || data.tmProfileList[count].suggestedterms == " ") ? "&nbsp" : data.tmProfileList[count].suggestedterms;
                    tmDtlTmplClone[9] = (data.tmProfileList[count].targetTermLanguage == null || data.tmProfileList[count].targetTermLanguage == " ") ? "&nbsp" : data.tmProfileList[count].targetTermLanguage;
                    tmDtlTmplClone[11] = company;
                    tmDtlTmplClone[13] = productLine;
                    tmDtlTmplClone[15] = IndustryDomain;
                    tmDtlTmplClone[17] = contentType;
                    tmDtlTmplClone[19] = data.tmProfileList[count].tmProfileId;
                    tmDtlTmplClone[21] = data.tmProfileList[count].tmProfileId;
                    $j('#tmDtlRowsList').append(tmDtlTmplClone.join(""));

                }
                showRolePrivileges();

            }
            
            $j("#tmDtlRowsList input:checkbox").each(function (i) {
                if (selectedTermIds != "") {
                    var temptermIdsArray = selectedTermIds.split(",");
                    for (var j = 0; j < temptermIdsArray.length; j++) {
                        if (temptermIdsArray[j] == ($j(this).parent().next().attr("termId"))) {
                            $j(this).attr('checked', true);
                        }
                    }
                }
            });
            $j('#tmDtlRowsList input:checkbox').click(function () {
                if ($j(this).attr('checked') != "checked") {
                    var tempIds = "";
                    var separator = "";
                    if (selectedTermIds != "") {
                        var temptermIdsArray = selectedTermIds.split(",");
                        for (var j = 0; j < temptermIdsArray.length; j++) {
                            if (temptermIdsArray[j] != ($j(this).parent().next().attr("termId"))) {
                                tempIds = separator + temptermIdsArray[j];
                                separator = ",";
                            }
                        }
                    }
                    selectedTermIds = tempIds;
                }
            });


        };
        showRolePrivileges();
//		$j('div.contentType').each(function(i){
//			var origText = $j(this).text();
//			if(origText.length > 10){
//				var finalText = origText.substring(0,8);
//				finalText=finalText+"...";
//				$j(this).html(finalText);
//				$j(this).attr("title",origText); 
//			}
//			
//		});

//				$j('div.sourceTm').each(function(i){
//			var origText = $j(this).text();
//			var searchedtext= $j(this).find('.selectedbg').text();
//			if(origText.length > 20){
//				var finalText = origText.substring(0,18);
//					searchedtext=$j(this).text();
//					if(searchedtext!=""){
//					finalText=finalText.replace(searchedtext,"<span class='selectedbg'>"+searchedtext+"</span>");
//					}
//					finalText=finalText+"...";
//				$j(this).html(finalText);
//				$j(this).attr("title",origText); 
//			}
//		});
//		
//		
//		$j('div.targetTm').each(function(i){
//			var origText = $j(this).text();
//			var searchedtext= $j(this).find('.selectedbg').text();
//			if(origText.length > 10){
//				var finalText = origText.substring(0,8);
//					searchedtext=$j(this).text();
//					if(searchedtext!=""){
//					finalText=finalText.replace(searchedtext,"<span class='selectedbg'>"+searchedtext+"</span>");
//					}				
//					finalText=finalText+"...";
//				$j(this).html(finalText);
//				$j(this).attr("title",origText); 
//			
//			}
//			
//			
//		});
//		$j('div.contentType').each(function(i){
//			var origText = $j(this).text();
//			if(origText.length > 10){
//				var finalText = origText.substring(0,8);
//				finalText=finalText+"...";
//				$j(this).html(finalText);
//				$j(this).attr("title",origText); 
//			}
//			
//		});
//		$j('div.company').each(function(i){
//			var origText = $j(this).text();
//			if(origText.length > 10){
//				var finalText = origText.substring(0,8);
//				finalText=finalText+"...";
//				$j(this).html(finalText);
//				$j(this).attr("title",origText); 
//			}
//			
//		});
//		$j('div.industryDomain').each(function(i){
//			var origText = $j(this).text();
//			if(origText.length > 10){
//				var finalText = origText.substring(0,8);
//				finalText=finalText+"...";
//				$j(this).html(finalText);
//				$j(this).attr("title",origText); 
//			}
//			
//		});
//		$j('div.productLine').each(function(i){
//			var origText = $j(this).text();
//			if(origText.length > 10){
//				var finalText = origText.substring(0,8);
//				finalText=finalText+"...";
//				$j(this).html(finalText);
//				$j(this).attr("title",origText); 
//			}
//			
//		});
//		

        ctx.bind("showTMDetails", function (event, criteria) {
            startWaiting("Loading ... please wait", $j('#tmDtlRowsList'), false);

            $j("#tmPagination").hide();
            var pageNum = criteria.pageNum;
            var pageNumTm = criteria.pageNumTm;
            var queryAppender = new Object();
            queryAppender.colName = criteria.colName;
            queryAppender.sortOrder = criteria.sortOrder;
            queryAppender.pageNum = criteria.pageNum;
            queryAppender.pageNumTm = criteria.pageNumTm;
            queryAppender.searchTerm = criteria.searchBy;
            queryAppender.searchStr = criteria.searchStr;
            queryAppender.caseFlag = criteria.caseSensitiveFlag;
            queryAppender.filterBy = criteria.filterBy;
            queryAppender.selectedIds = criteria.selectedIds;
            queryAppender.langValues = criteria.langValues;
            queryAppender.searchType = criteria.searchType;
            queryAppender.replaceStr=criteria.replaceStr;
            queryAppender.isReplaceTerm=criteria.isReplaceTerm;
            queryAppender.selectedCompanyIds = criteria.companyValues;
            searchString = criteria.searchBy;
            searchStr = criteria.searchBy;
            var filterBy = criteria.filterBy;
            var queryAppenderParameter = Object.toJSON(queryAppender);

            $t.searchTMEntries(queryAppenderParameter, {
                success: function (data) {
                    $j('#tmDtlRowsList').empty();
                    tmData = data;
                    var tmDetails = data.tmProfileList;

                    if (tmDetails == null || tmDetails.length == 0) {
                        var displayCriteriaTm = "No data to display";
                        $j("#rangetm").html('<div> <b>TM Matches(<span id="totalTmPolledTerms">0</span>) - Page <span id="rangeOfTmTerms">0/0 </span></b></div>');

                        $j("#tmDtlRowsList").html('<span style="text-align: center; display: block;  font-size:12px;padding-top:10px; padding-bottom:10px;">' + displayCriteriaTm + '</span>');
                        $j("#tmPagination").hide();
                        if(criteria.isReplaceTerm == 'Y') {
                            criteria.selectedIds = '';
                        }
                        if( criteria.isReplaceTerm == 'Y' && criteria.selectedIds.length == 0 && !(replaceAlertCount > 0) && replaceAlertBothCount == 0) {
                            alertMessage("#noRplaceTmMachMsg");
                            replaceAlertCount++;
                        }

                    }


                    if (tmDetails != null && tmDetails != "") {
                        if(criteria.isReplaceTerm == 'Y' && queryAppender.selectedIds.length != 0 && tmDetails[0].termReplaced != 'Y' && !(replaceAlertBothCount >0) && replaceAlertCount == 0) {
                            alertMessage("#noRplaceTmMachMsg");
                            replaceAlertBothCount++;
                        }

                  /*      if(tmDetails != null && tmDetails.length != 0 && criteria.isReplaceTerm == 'Y' && tmDetails[0].termReplaced != 'Y' && replaceAlertCount == 0 ) {
                            alertMessage("#noRplaceTrmMachMsg");
                        }else if(tmDetails != null && tmDetails.length != 0 && criteria.isReplaceTerm == 'Y' && tmDetails[0].termReplaced != 'Y' && replaceAlertCount != 0 ){
                            replaceAlertCount++;
                        }*/
                        var length = (tmDetails.length >= termListLimit) ? termListLimit : tmDetails.length;
                        var startLimit = 0;
                        var endLimit = 0;
                       /* if (pageNumTm == 0) {
                            tmTotalTerms = tmDetails.length;
                        }*/
                        tmTotalTerms = data.totalRecords;
                        tmTotalTerms = parseInt(tmTotalTerms);
                        noOfPagesTm = Math.round(tmTotalTerms / 10);
                        noOfPagesTm = (tmTotalTerms % 10 < 5 && tmTotalTerms % 10 > 0) ? noOfPagesTm + 1 : noOfPagesTm;
                        if (pageNumTm == 0) {
                            startLimit = 1;
                            endLimit = (termListLimit > tmTotalTerms) ? tmTotalTerms : termListLimit;
                        } else {
                            startLimit = ((pageNumTm - 1) * termListLimit) + 1;
                            var tempLimit = (pageNumTm) * termListLimit;
                            endLimit = (parseInt(tempLimit) > parseInt(tmTotalTerms)) ? tmTotalTerms : tempLimit;
                        }
                        
                        
                    /*    var length = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;
                        var startLimit = 0;
                        var endLimit = 0;

                        termBasetotalTerms = data.totalResults;
                        termBasetotalTerms = parseInt(termBasetotalTerms);
                        noOfPages = Math.round(termBasetotalTerms / 10);
                        noOfPages = (termBasetotalTerms % 10 < 5 && termBasetotalTerms % 10 > 0) ? noOfPages + 1 : noOfPages;
                        if (pageNum == 0) {
                            startLimit = 1;
                            endLimit = (termListLimit > termBasetotalTerms) ? termBasetotalTerms : termListLimit;
                        } else {
                            startLimit = ((pageNum - 1) * termListLimit) + 1;
                            var tempLimit = (pageNum) * termListLimit;
                            endLimit = (parseInt(tempLimit) > parseInt(termBasetotalTerms)) ? termBasetotalTerms : tempLimit;
                        }*/
                        displayTmDetails(data);
                        showRolePrivileges();


                        $j("#tmPagination").show();
                        $j("#rangeOfTmTerms").html(((pageNumTm == 0) ? 1 : pageNumTm) + "/" + noOfPagesTm);
                        var totalTerms3 = insertCommmas(new String(tmTotalTerms));
                        $j("#totalTmPolledTerms").html(totalTerms3);
                        tmPagination(noOfPagesTm, pageNumTm);
                        bindEvent();
                        bindEvnt();
                        $j('div.sourceTm').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 14) {
                                var finalText = origText.substring(0, 12);
                                finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                finalText = finalText + "...";

                                $j(this).html(finalText);
                                $j(this).attr("title", origText);
                            } else {

                                var origText = $j(this).text();
                                origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                                $j(this).html(origText);
                            }
                        });

                        $j('div.targetTm').each(function (i) {
                            var origText = $j(this).text();

                            if (origText.length > 14) {
                                var finalText = origText.substring(0, 12);
                                finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                finalText = finalText + "...";

                                $j(this).html(finalText);
                                $j(this).attr("title", origText);
                            } else {

                                var origText = $j(this).text();
                                origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                                $j(this).html(origText);
                            }
                        });
                        $j('div.contentType').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 10) {
                                var finalText = origText.substring(0, 8);
                                finalText = finalText + "...";
                                $j(this).html(finalText);
                                $j(this).attr("title", origText);
                            }

                        });
                        $j('div.company').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 10) {
                                var finalText = origText.substring(0, 8);
                                finalText = finalText + "...";
                                $j(this).html(finalText);
                                $j(this).attr("title", origText);
                            }

                        });
                        $j('div.industryDomain').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 10) {
                                var finalText = origText.substring(0, 8);
                                finalText = finalText + "...";
                                $j(this).html(finalText);
                                $j(this).attr("title", origText);
                            }

                        });
                        $j('div.productLine').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 10) {
                                var finalText = origText.substring(0, 8);
                                finalText = finalText + "...";
                                $j(this).html(finalText);
                                $j(this).attr("title", origText);
                            }

                        });

                        $j(".selectedbg").hover(function () {
                            modalRender.bubble(".selectedbg", "Yellow highlight denotes search term(s).", "left center", "right center");
                        });
                    }

                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });
            $j(function () {
                $j("#datepicker").datepicker({
                    showOn: "button",
                    buttonImage: "images/calendar.gif",
                    buttonImageOnly: true
                });
            });


        });


    };


    var displayCompanyList = function (data, appenderClass) {
        var companyList = data;
        for (var count = 0; count < companyList.length; count++) {
            var companyTmplClone = companyTmpl;
            companyTmplClone[1] = companyList[count].companyId;
            companyTmplClone[3] = companyList[count].companyName;
            $j(appenderClass).append(companyTmplClone.join(""));
        }
    }

    /// ***tm end**///

    $j.fn.termDetails = function () {

        var ctx = $j(this);

        var startWaiting = function (msg, selector, floatRight) {
            if (selector) {
                if (typeof msg == 'undefined' || msg == null) {
                    msg = " Loading... please wait.";
                }
                var temp = '<div class="loading-msg  topmargin15" style="text-align :center;"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif" />' + '&nbsp;' + msg + '</div>';
                selector.append(temp);

            }
        };
        var stopWaiting = function (selector) {
            if (selector) {
                selector.find(".loading-msg").hide();
            }
        };

        var displayTermDetails = function (data) {
            stopWaiting($j('#termDtlRowsList'));

            var termDetails = data;
            if (termDetails != null) {
                var listLength = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;
                for (var count = 0; count < listLength; count++) {
                    var termDtlTmplClone = termDtlTmpl;

                    termDtlTmplClone[1] = termDetails[count].termId;
                    termDtlTmplClone[3] = termDetails[count].termId;
                    var sourceTerm = termDetails[count].sourceTerm;
                    var partOfSpeech = (termDetails[count].partOfSpeech == null || termDetails[count].partOfSpeech == " ") ? "&nbsp;" : termDetails[count].partOfSpeech;
                    var category = (termDetails[count].category == null || termDetails[count].category == " ") ? "&nbsp;" : termDetails[count].category;
                    var domain = (termDetails[count].domain == null || termDetails[count].domain == " ") ? "&nbsp;" : termDetails[count].domain;
                    var company = (termDetails[count].company == null || termDetails[count].company == "") ? "&nbsp;" : termDetails[count].company;
                    if (termDetails[count].deprecatedCount > 0) {

                        if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                            termDtlTmplClone[5] = "<span style='font-weight:bold;font-size:20px;color:red'><b>!</b></span>" + sourceTerm;
                        } else {

                            termDtlTmplClone[5] = "<span style='font-weight:bold;font-size:17px;color:red'><b>!</b></span>" + sourceTerm;
                        }
                    } else {
                        termDtlTmplClone[5] = sourceTerm;

                    }
//				termDtlTmplClone[3] = (data.termBaseList[count].termsBeingPolled==null || data.termBaseList[count].termsBeingPolled == "" )? "&nbsp;": data.termBaseList[count].termsBeingPolled;
                    termDtlTmplClone[7] = "dataPie_" + termDetails[count].termId;
                    termDtlTmplClone[9] = (termDetails[count].suggestedTerm == null || termDetails[count].suggestedTerm == " ") ? "&nbsp;" : termDetails[count].suggestedTerm;
                    termDtlTmplClone[11] = (termDetails[count].language == null || termDetails[count].language == " ") ? "&nbsp;" : termDetails[count].language;
                    termDtlTmplClone[13] = (termDetails[count].company == null || termDetails[count].company == " ") ? "&nbsp;" : termDetails[count].company;
                    termDtlTmplClone[15] = "tickImg_" + termDetails[count].termId;
                    termDtlTmplClone[17] = termDetails[count].termId;
                    termDtlTmplClone[19] = termDetails[count].pollExpirationDt;
                    termDtlTmplClone[21] = "datepicker" + termDetails[count].termId;
                    termDtlTmplClone[23] = (termDetails[count].partOfSpeech == null || termDetails[count].partOfSpeech == " ") ? "&nbsp;" : termDetails[count].partOfSpeech;
                    termDtlTmplClone[25] = (termDetails[count].category == null || termDetails[count].category == " ") ? "&nbsp;" : termDetails[count].category;
                    termDtlTmplClone[27] = (termDetails[count].domain == null || termDetails[count].domain == " ") ? "&nbsp;" : termDetails[count].domain;
                    termDtlTmplClone[29] = termDetails[count].termId;
                    termDtlTmplClone[31] = termDetails[count].termId;
                    $j('#termDtlRowsList').append(termDtlTmplClone.join(""));
                    if ((termDetails[count].status) == "Approved") {
                        $j('.tickImg_' + termDetails[count].termId).css("visibility", "visible");
                    }
                }
            }

            showRolePrivileges();
            
            $j("#termDtlRowsList input:checkbox").each(function (i) {
                if (selectedTermIds != "") {
                    var temptermIdsArray = selectedTermIds.split(",");
                    for (var j = 0; j < temptermIdsArray.length; j++) {
                        if (temptermIdsArray[j] == ($j(this).parent().next().attr("termId"))) {
                            $j(this).attr('checked', true);
                        }
                    }
                }
            });
            $j('#termDtlRowsList input:checkbox').click(function () {
                // $j("#showSelectedTerms").attr('checked',false);
                //$j("#paginationId").val($j("#paginationId option:first").val());              
                if ($j(this).attr('checked') != "checked") {
                    var tempIds = "";
                    var separator = "";
                    if (selectedTermIds != "") {
                        var temptermIdsArray = selectedTermIds.split(",");
                        for (var j = 0; j < temptermIdsArray.length; j++) {
                            if (temptermIdsArray[j] != ($j(this).parent().next().attr("termId"))) {
                                //tempIds=separator+temptermIdsArray[j];
                                tempIds = tempIds + separator + temptermIdsArray[j];

                                separator = ",";
                            }
                        }
                    }

                    selectedTermIds = tempIds;
                }
            });
        };

        showRolePrivileges();

        ctx.bind("showTermBaseDetails", function (event, criteria) {

            startWaiting("Loading ... please wait", $j('#termDtlRowsList'), false);

            $j("#pagination").hide();
            var pageNum = criteria.pageNum;
            var pageNumTm = criteria.pageNumTm;
            var queryAppender = new Object();
            queryAppender.colName = criteria.colName;
            queryAppender.sortOrder = criteria.sortOrder;
            queryAppender.pageNum = criteria.pageNum;
            queryAppender.pageNumTm = criteria.pageNumTm;
            queryAppender.searchTerm = criteria.searchBy;
            queryAppender.searchStr = criteria.searchStr;
            queryAppender.caseFlag = criteria.caseSensitiveFlag;
            queryAppender.filterBy = criteria.filterBy;
            queryAppender.selectedIds = criteria.selectedIds;
            queryAppender.langValues = criteria.langValues;
            queryAppender.searchType = criteria.searchType;
            queryAppender.replaceStr=criteria.replaceStr;
            queryAppender.isReplaceTerm=criteria.isReplaceTerm;
            queryAppender.selectedCompanyIds = criteria.companyValues;
            searchString = criteria.searchBy;
            searchStr = criteria.searchBy;
            var filterBy = criteria.filterBy;
            var queryAppenderParameter = Object.toJSON(queryAppender);
            $t.searchTermBaseEntries(queryAppenderParameter, {
                success: function (data) {
                    $j('#termDtlRowsList').empty();
                    termBaseData = data;
                    var termDetails = data.pollTermsList;
                    //var tmDetails = data.tmProfileList;
                    if (termDetails == null || termDetails.length == 0) {
                        var displayCriteria = "No data to display";
                        $j("#range").html('<div> <b>Termbase Matches(<span id="totalPolledTerms">0</span>) - Page <span id="rangeOfTerms">0/0 </span></b></div>');
                        $j("#termDtlRowsList").html('<span style="text-align: center; display: block;  font-size:12px;padding-top:10px; padding-bottom:10px;">' + displayCriteria + '</span>');
                        $j("#pagination").hide();
                        if(criteria.isReplaceTerm == 'Y') {
                            criteria.selectedIds = '';
                        }
                        if(criteria.isReplaceTerm == 'Y' && criteria.selectedIds.length == 0 && !(replaceAlertCount > 0) && replaceAlertBothCount == 0) {
                            alertMessage("#noRplaceTrmMachMsg");
                            replaceAlertCount++;
                        }
                    }


                    if (termDetails != null && termDetails != "") {
//							 $j(".termBaseDetails").show();
                        if(criteria.isReplaceTerm == 'Y' && queryAppender.selectedIds.length != 0 && termDetails[0].termReplaced != 'Y' && !(replaceAlertBothCount >0) && replaceAlertCount == 0) {
                            alertMessage("#noRplaceTrmMachMsg");
                            replaceAlertBothCount++;
                        }
                       /* if(termDetails != null && termDetails.length != 0 && criteria.isReplaceTerm == 'Y' && termDetails[0].termReplaced != 'Y' && replaceAlertCount == 0) {
                            alertMessage("#noRplaceTrmMachMsg");
                        }else if(termDetails != null && termDetails.length != 0 && criteria.isReplaceTerm == 'Y' && termDetails[0].termReplaced != 'Y' && replaceAlertCount != 0 ){
                            replaceAlertCount++;
                        }*/
                        var length = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;
                        var startLimit = 0;
                        var endLimit = 0;

                        termBasetotalTerms = data.totalResults;
                        termBasetotalTerms = parseInt(termBasetotalTerms);
                        noOfPages = Math.round(termBasetotalTerms / 10);
                        noOfPages = (termBasetotalTerms % 10 < 5 && termBasetotalTerms % 10 > 0) ? noOfPages + 1 : noOfPages;
                        if (pageNum == 0) {
                            startLimit = 1;
                            endLimit = (termListLimit > termBasetotalTerms) ? termBasetotalTerms : termListLimit;
                        } else {
                            startLimit = ((pageNum - 1) * termListLimit) + 1;
                            var tempLimit = (pageNum) * termListLimit;
                            endLimit = (parseInt(tempLimit) > parseInt(termBasetotalTerms)) ? termBasetotalTerms : tempLimit;
                        }

                        displayTermDetails(termDetails);
                        showRolePrivileges();
                        for (var i = 0; i < length; i++) {
                            var dataXML = new Array();
                            var invites = (termDetails[i].invites == null) ? 0 : termDetails[i].invites;
                            var votesPerTerm = (termDetails[i].votesPerTerm == null) ? 0 : termDetails[i].votesPerTerm;
                            if (invites == 0 && votesPerTerm == 0) {
                                $j("#dataPie_" + termDetails[i].termId).css("visibility", "hidden");
                                $j("#dataPie_" + termDetails[i].termId).css("width", "40px");
                                $j("#dataPie_" + termDetails[i].termId).removeAttr("id");
                                continue;
                            }
                            // Start of chart object
                            dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='14' showShadow='0' slicingDistance='0' animation='0' paletteColors='#C9CCCB, #00A4D6' showBorder='0' bgAlpha='30' showToolTip='0'>");
                            dataXML.push("<set label='Services' value='" + (invites - votesPerTerm) + "' />");
                            dataXML.push("<set label='Hardware' value='" + votesPerTerm + "' />");
                            dataXML.push("</chart>");
                            ChartRender.twoDPieChart(termDetails[i].termId, pieChartHeight, pieChartWidth, "dataPie_" + termDetails[i].termId, dataXML);
                            modalRender.bubble("#dataPie_" + termDetails[i].termId, votesPerTerm + "/" + invites + "<br>" + "voted / invited to vote", "bottom center", "top center");

                        }


                        $j("#pagination").show();
                        $j("#rangeOfTerms").html(((pageNum == 0) ? 1 : pageNum) + "/" + noOfPages);
                        var totalTerms3 = insertCommmas(new String(termBasetotalTerms));
                        $j("#totalPolledTerms").html(totalTerms3);
                        pagination(noOfPages, pageNum);
                        bindEvents();
                        bindEvnts();


                        $j('div.pollTerm').each(function (i) {
                            var origText = $j(this).text();


                            if (origText.lastIndexOf("!") > 0) {
                                srcText = origText.substr(0, origText.lastIndexOf("!"));
                                var supscript = "";
                                if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                                    supscript = "<span style='font-weight:bold;font-size:20px; color:red'><b>!</b></span>";

                                }
                                else {
                                    supscript = "<span style='font-weight:bold;font-size:15px; color:red'><b>!</b></span>";
                                }
                                if (srcText.length > 10) {
                                    var finalText = srcText.substring(0, 8);
                                    finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                    finalText = finalText + "...";
                                    $j(this).html(finalText + supscript);
                                    $j(this).attr("title", srcText);

                                } else {
                                    srcText = Util.backgroundYellow(srcText, searchText, searchType, caseFlag);
                                    $j(this).html(srcText + supscript);
                                }
                            }

                            else {
                                $j(this).addClass("toppadding5");
                                if (origText.length > 10) {
                                    var finalText = origText.substring(0, 8);
                                    finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                    finalText = finalText + "...";
                                    $j(this).html(finalText);
                                    $j(this).attr("title", origText);
                                } else {
                                    var origText = $j(this).text();
                                    origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                                    $j(this).html(origText);
                                }
                            }


                        });

                        $j('div.targetTrm').each(function (i) {
                            var origText = $j(this).text();

                            if (origText.length > 14) {
                                var finalText = origText.substring(0, 12);
                                finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                                finalText = finalText + "...";

                                $j(this).html(finalText);
                                $j(this).attr("title", origText);
                            } else {

                                var origText = $j(this).text();
                                origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                                $j(this).html(origText);
                            }

                        });

                        $j(".selectedbg").hover(function () {
                            modalRender.bubble(".selectedbg", "Yellow highlight denotes search term(s).", "left center", "right center");
                        });
                    }


                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });
            $j(function () {
                $j("#datepicker").datepicker({
                    showOn: "button",
                    buttonImage: "images/calendar.gif",
                    buttonImageOnly: true
                });
            });


        });


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
        var preg_quote = function (str) {
            return (str + '').replace(/([\\\.\+\*\?\[\^\]\$\(\)\{\}\=\!\<\>\|\:])/g, "\\$1");
        }
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


    var slctOptionsTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];


    var displayLanguageList = function (data) {
        $j('#languageSlct').html("");
        //$j("#languageSlct").html('<div><select><option value="0">--select--</option></select></div>');
        var languageList = data;
        for (var count = 0; count < languageList.length; count++) {
            var langSlctTmplClone = slctOptionsTmpl;
            langSlctTmplClone[1] = languageList[count].languageId;
            langSlctTmplClone[3] = languageList[count].languageLabel;
            $j('#languageSlct').append(langSlctTmplClone.join(""));
        }


    };

    var displayPosList = function (data, appenderClass) {

        var posList = data;
        for (var count = 0; count < posList.length; count++) {
            var partOfSpeechSlctTmplClone = slctOptionsTmpl;
            partOfSpeechSlctTmplClone[1] = posList[count].partsOfSpeechId;
            partOfSpeechSlctTmplClone[3] = posList[count].partOfSpeech;
            $j(appenderClass).append(partOfSpeechSlctTmplClone.join(""));
        }

    };

    var displayDomainList = function (data, appenderClass) {
        var domainList = data;
        for (var count = 0; count < domainList.length; count++) {
            var domainTmplClone = slctOptionsTmpl;
            domainTmplClone[1] = domainList[count].domainId;
            domainTmplClone[3] = domainList[count].domain;
            jQuery(appenderClass).append(domainTmplClone.join(""));
        }
    };

    var displayTermCatList = function (data, appenderClass) {
        var termCatList = data;
        for (var count = 0; count < termCatList.length; count++) {
            var termCatSlctTmplClone = slctOptionsTmpl;
            termCatSlctTmplClone[1] = termCatList[count].categoryId;
            termCatSlctTmplClone[3] = termCatList[count].category;
            $j(appenderClass).append(termCatSlctTmplClone.join(""));
        }
    };

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


    var tmPagination = function (noOfPages, pageNum) {
        if ((noOfPages > 1) && (pageNum < noOfPages)) {
            $j(".tmNext").addClass("nextEnable");
            $j(".tmNext").removeClass("nextDisable");
        } else {
            $j(".tmNext").removeClass("nextEnable");
            $j(".tmNext").addClass("nextDisable");
        }

        if ((noOfPages > 1) && (pageNum > 1)) {
            $j(".tmPrevious").removeClass("prevDisable");
            $j(".tmPrevious").addClass("prevEnable");
        } else {
            $j(".tmPrevious").removeClass("prevEnable");
            $j(".tmPrevious").addClass("prevDisable");
        }
    };


    $j(".searchBtn").click(function () {
        $j("#searchText:ui-dialog").dialog("destroy");
        $j("#selectAll").attr('checked', true);
        selectedTermIds = '';
       // $j("#selectAll").attr('checked', true);
        searchBy = $j("#searchField").val();
        searchBy = $j.trim(searchBy);
        searchBy = searchBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
        searchText = searchBy.replace(/\s\s+/g, ' ');
        searchText = $j.trim(searchText);

        if ($j.trim($j("#searchField").val()) == "") {
            alertMessage("#searchMsg");
        }
        else {
            $j("#chkCase").attr("checked", false);
            $j('#termsModule').show();
            $j('#tmModule').show();
//		 		 $j(".changeRqstBtn").show();
            showRolePrivileges();
            var searchCriteria = $j("#searchOption :selected").text();
            langValues = $j("#languageSlct").val();
            companyValues = $j("#mutliCompanySlct").val();
            searchStr = $j("#searchCase :selected").text();
            exportBy = $j("#attributeTM :selected").text();
            selectedIds = $j(".serachList ").val();
            searchType = $j("#searchType :selected").text();
            if (searchCriteria == "Termbase") {
                $j("#manageSearchTermTbl").show();
                $j("#manageTMTermTbl").hide();
                if (searchStr == "Sensitive") {
                    if (searchType == "Exact")
                        caseFlag = true;
                    triggerTermBaseDetails(null, null, 0, 0, searchText, searchCriteria, true, exportBy, selectedIds, langValues, searchType, companyValues);
                } else {
                    caseFlag = false;
                    triggerTermBaseDetails(null, null, 0, 0, searchText, searchCriteria, null, exportBy, selectedIds, langValues, searchType, companyValues);
                }
            } else if (searchCriteria == "TM") {
                $j("#manageTMTermTbl").show();
                $j("#manageSearchTermTbl").hide();
                if (searchStr == "Sensitive") {
                    if (searchType == "Exact")
                        caseFlag = true;
                    triggerTMDetails(null, null, 0, 0, searchText, searchCriteria, true, exportBy, selectedIds, langValues, searchType, companyValues);
                } else {
                    caseFlag = false;
                    triggerTMDetails(null, null, 0, 0, searchText, searchCriteria, null, exportBy, selectedIds, langValues, searchType, companyValues);
                }
            } else {
                $j("#manageTMTermTbl").show();
                $j("#manageSearchTermTbl").show();
                if (searchStr == "Sensitive") {
                    if (searchType == "Exact")
                        caseFlag = true;
                    triggerTermBaseDetails(null, null, 0, 0, searchText, searchCriteria, true, exportBy, selectedIds, langValues, searchType, companyValues);

                    triggerTMDetails(null, null, 0, 0, searchText, searchCriteria, true, exportBy, selectedIds, langValues, searchType, companyValues);
                } else {
                    caseFlag = false;
                    triggerTMDetails(null, null, 0, 0, searchText, searchCriteria, null, exportBy, selectedIds, langValues, searchType, companyValues);
                    triggerTermBaseDetails(null, null, 0, 0, searchText, searchCriteria, null, exportBy, selectedIds, langValues, searchType, companyValues);
                }
            }
            if(selectedIds != "") {
                $j("#selectReplaceChkBox").show();
            }
        }
    });

    
    $j("#selectAll").click(function () {
        if(this.checked) { 
            $j('.checkbox').each(function() { 
                this.checked = true; 
            });
        }else{
            $j('.checkbox').each(function() { 
                this.checked = false; 
            });
        }
    });
    
    $j(document).on('click', '.checkbox', function () {
        if($j("#selectAll").attr('checked') == 'checked' || $j("#selectAll").attr('checked') == undefined){
            $j("#selectAll").attr('checked', false);
        }else{
            $j("#selectAll").attr('checked', true);
        }
    });


    
 $j("#replaceTerm").click(function () {
       
        //if(selectedTermIds == ""){
     replaceAlertCount = 0;
     replaceAlertBothCount = 0;
        $j("#termDtlRowsList input:checked").each(function (i) {
            if (selectedTermIds == "") {
                separator = "";
            } else {
                separator = ",";
            }
            
            selectedTermIds += separator + $j(this).parent().next().attr("termId");
            //Remove duplicates if we have in selected term IDs.
           /*var array = selectedTermIds.split(",");
            var selectedTermIds = [array[0]];
            for (var i = 1; i < array.length; i++) {
                if (array[i - 1] !== array[i])
                    selectedTermIds.push(array[i]);
            }*/
            
            var selectedTermIdsInArray = new Array();

            selectedTermIdsInArray = selectedTermIds.split(",");
            for (var element = 0; element < selectedTermIdsInArray.length; element++) {
                selectedTermIdsInArray[element] = parseInt(selectedTermIdsInArray[element]);
            }
            uniqueSelectedTermIds = $j.unique(selectedTermIdsInArray);
            uniqueSelectedTermIds = $j.unique(uniqueSelectedTermIds);
            
        });
        $j("#tmDtlRowsList input:checked").each(function (i) {
            if (selectedTermIds == "") {
                separator = "";
            } else {
                separator = ",";
            }
            selectedTermIds += separator + $j(this).parent().next().attr("termId");
            var selectedTermIdsInArray = new Array();

            selectedTermIdsInArray = selectedTermIds.split(",");
            for (var element = 0; element < selectedTermIdsInArray.length; element++) {
                selectedTermIdsInArray[element] = parseInt(selectedTermIdsInArray[element]);
            }
            uniqueSelectedTermIds = $j.unique(selectedTermIdsInArray);
            uniqueSelectedTermIds = $j.unique(uniqueSelectedTermIds);
            
        });
        //}
       
        $j("#searchText:ui-dialog").dialog("destroy");
        searchBy = $j("#searchField").val();
        searchBy = $j.trim(searchBy);
        searchBy = searchBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
        searchText = searchBy.replace(/\s\s+/g, ' ');
        searchText = $j.trim(searchText);
        
        var replaceBy = $j('#replaceTermVal').val();
        replaceBy = $j.trim(replaceBy);
        replaceBy = replaceBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
        replaceText = replaceBy.replace(/\s\s+/g, ' ');
        replaceText = $j.trim(replaceText);

        
        var replaceFlag = 'Y';
        if (($j.trim($j("#searchField").val()) == "") && ($j.trim($j("#replaceTermVal").val()) == "")) {
            alertMessage("#searchMsg");
        }else if($j.trim($j("#searchField").val()) == "") {
            alertMessage("#searchMsg");
        } else if($j.trim($j("#replaceTermVal").val()) == "") {
            alertMessage("#replaceSearchMsg");
        }
     
        if(($j.trim($j("#replaceTermVal").val()) != "") && ($j.trim($j("#searchField").val()) != "")) {
            $j("#chkCase").attr("checked", false);
            $j('#termsModule').show();
            $j('#tmModule').show();
//               $j(".changeRqstBtn").show();
            showRolePrivileges();
            var searchCriteria = $j("#replaceSearchOption :selected").text();
            langValues = $j("#languageSlct").val();
            companyValues = $j("#mutliCompanySlct").val();
            searchStr = $j("#searchCase :selected").text();
            exportBy = $j("#attributeTM :selected").text();
            selectedIds = $j(".serachList ").val();
            searchType = $j("#searchType :selected").text();
            if (searchCriteria == "Termbase") {
                $j("#manageSearchTermTbl").show();
                $j("#manageTMTermTbl").hide();
                if (searchStr == "Sensitive") {
                    if (searchType == "Exact")
                        caseFlag = true;
                    triggerTermBaseDetailsReplace(null, null, 0, 0, searchText, searchCriteria, true, exportBy, uniqueSelectedTermIds, langValues, searchType, companyValues, replaceText, replaceFlag);
                } else {
                    caseFlag = false;
                    triggerTermBaseDetailsReplace(null, null, 0, 0, searchText, searchCriteria, null, exportBy, uniqueSelectedTermIds, langValues, searchType, companyValues, replaceText, replaceFlag);
                }
            } else if (searchCriteria == "TM") {
                $j("#manageTMTermTbl").show();
                $j("#manageSearchTermTbl").hide();
                if (searchStr == "Sensitive") {
                    if (searchType == "Exact")
                        caseFlag = true;
                    triggerTMDetailsReplace(null, null, 0, 0, searchText, searchCriteria, true, exportBy, uniqueSelectedTermIds, langValues, searchType, companyValues, replaceText, replaceFlag);
                } else {
                    caseFlag = false;
                    triggerTMDetailsReplace(null, null, 0, 0, searchText, searchCriteria, null, exportBy, uniqueSelectedTermIds, langValues, searchType, companyValues, replaceText, replaceFlag);
                }
            } else {
                $j("#manageTMTermTbl").show();
                $j("#manageSearchTermTbl").show();
                if (searchStr == "Sensitive") {
                    if (searchType == "Exact")
                        caseFlag = true;
                    triggerTermBaseDetailsReplace(null, null, 0, 0, searchText, searchCriteria, true, exportBy, uniqueSelectedTermIds, langValues, searchType, companyValues, replaceText, replaceFlag);

                    triggerTMDetailsReplace(null, null, 0, 0, searchText, searchCriteria, true, exportBy, uniqueSelectedTermIds, langValues, searchType, companyValues, replaceText, replaceFlag);
                } else {
                    caseFlag = false;
                    triggerTMDetailsReplace(null, null, 0, 0, searchText, searchCriteria, null, exportBy, uniqueSelectedTermIds, langValues, searchType, companyValues, replaceText, replaceFlag);
                    triggerTermBaseDetailsReplace(null, null, 0, 0, searchText, searchCriteria, null, exportBy, uniqueSelectedTermIds, langValues, searchType, companyValues, replaceText, replaceFlag);
                    
                }
            }

        }
       /* //set timeout function for replace term not found in termbase and tmbase showing allert
        setTimeout(function () {
            if(replaceAlertCount == 3) {
                alertMessage("#noRplaceTrmMachMsg");
            }
            replaceAlertCount = 0;
        }, 100);*/
    });
    
    var pagination2 = function (noOfPages, pageNum) {
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

    var paginationTm2 = function (noOfPagesTm, pageNumTm) {
        if ((noOfPagesTm > 1) && (pageNumTm < noOfPagesTm)) {
            $j(".tmNext").addClass("nextEnable");
            $j(".tmNext").removeClass("nextDisable");
        } else {
            $j(".tmNext").removeClass("nextEnable");
            $j(".tmNext").addClass("nextDisable");
        }

        if ((noOfPagesTm > 1) && (pageNumTm > 1)) {
            $j(".tmPrevious").removeClass("prevDisable");
            $j(".tmPrevious").addClass("prevEnable");
        } else {
            $j(".tmPrevious").removeClass("prevEnable");
            $j(".tmPrevious").addClass("prevDisable");
        }
    };
    
    
    var displayTmDetails = function (data, start, end, pageNumTm) {


        $j("#tmDtlRowsList").empty();
        $j("#tmPagination").show();
        $j("#rangeOfTmTerms").html(pageNumTm + "/" + noOfPagesTm);

        savedCriteria.pageNumTm = pageNumTm;
        paginationTm2(noOfPagesTm, pageNumTm);
        var termDetails = data.tmProfileList;

        if (end > termDetails.length) {
            end = termDetails.length;
        }


        for (var count = start; count < end; count++) {
            var tmDtlTmplClone = tmDtlTmpl;
            var IndustryDomain = (data.tmProfileList[count].domain == null || data.tmProfileList[count].domain == "") ? "&nbsp;" : data.tmProfileList[count].domain;
            var productLine = (data.tmProfileList[count].productLine == null || data.tmProfileList[count].productLine == "") ? "&nbsp;" : data.tmProfileList[count].productLine;
            var contentType = (data.tmProfileList[count].contentType == null || data.tmProfileList[count].contentType == "") ? "&nbsp;" : data.tmProfileList[count].contentType;
            var company = (data.tmProfileList[count].company == null || data.tmProfileList[count].company == "") ? "&nbsp;" : data.tmProfileList[count].company;
            tmDtlTmplClone[1] = data.tmProfileList[count].tmProfileId;
            tmDtlTmplClone[3] = data.tmProfileList[count].tmProfileId;
            tmDtlTmplClone[5] = (data.tmProfileList[count].termsBeingPolled == null || data.tmProfileList[count].termsBeingPolled == " " ) ? "&nbsp" : data.tmProfileList[count].termsBeingPolled;
            tmDtlTmplClone[7] = (data.tmProfileList[count].suggestedterms == null || data.tmProfileList[count].suggestedterms == " ") ? "&nbsp" : data.tmProfileList[count].suggestedterms;
            tmDtlTmplClone[9] = (data.tmProfileList[count].targetTermLanguage == null || data.tmProfileList[count].targetTermLanguage == " ") ? "&nbsp" : data.tmProfileList[count].targetTermLanguage;
            tmDtlTmplClone[11] = company;
            tmDtlTmplClone[13] = productLine;
            tmDtlTmplClone[15] = IndustryDomain;
            tmDtlTmplClone[17] = contentType;
            tmDtlTmplClone[19] = data.tmProfileList[count].tmProfileId;
            tmDtlTmplClone[21] = data.tmProfileList[count].tmProfileId;
            $j('#tmDtlRowsList').append(tmDtlTmplClone.join(""));

        }

        showRolePrivileges();


        bindEvent();
        bindEvnt();
        $j('div.sourceTm').each(function (i) {
            var origText = $j(this).text();

            if (origText.length > 14) {
                var finalText = origText.substring(0, 12);
                finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                finalText = finalText + "...";

                $j(this).html(finalText);
                $j(this).attr("title", origText);
            } else {

                var origText = $j(this).text();
                origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                $j(this).html(origText);
            }
        });
        $j('div.targetTm').each(function (i) {
            var origText = $j(this).text();

            if (origText.length > 14) {
                var finalText = origText.substring(0, 12);
                finalText = Util.backgroundYellow(finalText, searchText, searchType, caseFlag);
                finalText = finalText + "...";

                $j(this).html(finalText);
                $j(this).attr("title", origText);
            } else {
                var origText = $j(this).text();
                origText = Util.backgroundYellow(origText, searchText, searchType, caseFlag);
                $j(this).html(origText);
            }
        });

        $j('div.contentType').each(function (i) {
            var origText = $j(this).text();
            if (origText.length > 10) {
                var finalText = origText.substring(0, 8);
                finalText = finalText + "...";
                $j(this).html(finalText);
                $j(this).attr("title", origText);
            }

        });
        $j('div.company').each(function (i) {
            var origText = $j(this).text();
            if (origText.length > 10) {
                var finalText = origText.substring(0, 8);
                finalText = finalText + "...";
                $j(this).html(finalText);
                $j(this).attr("title", origText);
            }

        });
        $j('div.industryDomain').each(function (i) {
            var origText = $j(this).text();
            if (origText.length > 10) {
                var finalText = origText.substring(0, 8);
                finalText = finalText + "...";
                $j(this).html(finalText);
                $j(this).attr("title", origText);
            }

        });
        $j('div.productLine').each(function (i) {
            var origText = $j(this).text();
            if (origText.length > 10) {
                var finalText = origText.substring(0, 8);
                finalText = finalText + "...";
                $j(this).html(finalText);
                $j(this).attr("title", origText);
            }

        });

        $j(".selectedbg").hover(function () {
            modalRender.bubble(".selectedbg", "Yellow highlight denotes search term(s).", "left center", "right center");
        });

    };


    var triggerTermBaseDetails = function (colName, sortOrder, pageNum, pageNumTm, searchBy, searchCriteria, caseSensitiveFlag, filterBy, selectedIds, langValues, searchType, companyValues) {
        if (colName == null) {
            $j("#mngTrmDtlSectionHead div").each(function (index) {
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
            'pageNum': pageNum,
            'pageNumTm': pageNumTm,
            'searchBy': searchBy,
            'searchStr': searchCriteria,
            'caseSensitiveFlag': caseSensitiveFlag,
            'filterBy': filterBy,
            'selectedIds': selectedIds,
            'langValues': langValues,
            'searchType': searchType,
            'companyValues': companyValues
        };

        savedCriteria = criteria;
        $j("#manageSearchTermTbl").trigger("showTermBaseDetails", criteria);
    };
    var triggerTermBaseDetailsReplace = function (colName, sortOrder, pageNum, pageNumTm, searchBy, searchCriteria, caseSensitiveFlag, filterBy, selectedIds, langValues, searchType, companyValues, replaceStr, replaceFlag) {
        if (colName == null) {
            $j("#mngTrmDtlSectionHead div").each(function (index) {
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
                'pageNum': pageNum,
                'pageNumTm': pageNumTm,
                'searchBy': searchBy,
                'searchStr': searchCriteria,
                'caseSensitiveFlag': caseSensitiveFlag,
                'filterBy': filterBy,
                'selectedIds': selectedIds,
                'langValues': langValues,
                'searchType': searchType,
                'companyValues': companyValues,
                'replaceStr': replaceStr,
                'isReplaceTerm': replaceFlag
        };
        
        savedCriteria = criteria;
        $j("#manageSearchTermTbl").trigger("showTermBaseDetails", criteria);
    };


    var triggerTMDetails = function (colName, sortOrder, pageNum, pageNumTm, searchBy, searchCriteria, caseSensitiveFlag, filterBy, selectedIds, langValues, searchType, companyValues) {

        if (colName == null) {
            $j("#tmDtlSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
        }

        $j('#tmDtlRowsList').empty();
        var criteria = {
            'colName': colName,
            'sortOrder': sortOrder,
            'pageNum': pageNum,
            'pageNumTm': pageNumTm,
            'searchBy': searchBy,
            'searchStr': searchCriteria,
            'caseSensitiveFlag': caseSensitiveFlag,
            'filterBy': filterBy,
            'selectedIds': selectedIds,
            'langValues': langValues,
            'searchType': searchType,
            'companyValues': companyValues

        };

        savedCriteria = criteria;
        $j("#manageTMTermTbl").trigger("showTMDetails", criteria);
    };
    var triggerTMDetailsReplace = function (colName, sortOrder, pageNum, pageNumTm, searchBy, searchCriteria, caseSensitiveFlag, filterBy, selectedIds, langValues, searchType, companyValues, replaceStr, replaceFlag) {
        
        if (colName == null) {
            $j("#tmDtlSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
        }
        
        $j('#tmDtlRowsList').empty();
        var criteria = {
                'colName': colName,
                'sortOrder': sortOrder,
                'pageNum': pageNum,
                'pageNumTm': pageNumTm,
                'searchBy': searchBy,
                'searchStr': searchCriteria,
                'caseSensitiveFlag': caseSensitiveFlag,
                'filterBy': filterBy,
                'selectedIds': selectedIds,
                'langValues': langValues,
                'searchType': searchType,
                'companyValues': companyValues,
                'replaceStr': replaceStr,
                'isReplaceTerm': replaceFlag
                
        };
        
        savedCriteria = criteria;
        $j("#manageTMTermTbl").trigger("showTMDetails", criteria);
    };


    $j(".next").click(function () {
        if ($j(".next").hasClass("nextEnable")) {
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var pageNum = (savedCriteria.pageNum == 0) ? (savedCriteria.pageNum + 2) : (savedCriteria.pageNum + 1);
            savedCriteria.pageNum = pageNum;
            var searchStr = savedCriteria.searchStr;
            var caseSensitiveFlag = savedCriteria.caseSensitiveFlag;
            var filterBy = savedCriteria.filterBy;
            var selectedIds = savedCriteria.selectedIds;
            var separator = "";
            $j("#termDtlRowsList input:checked").each(function (i) {
                if (selectedTermIds == "") {
                    separator = "";
                } else {
                    separator = ",";
                }
                selectedTermIds += separator + $j(this).parent().next().attr("termId");
            });
            var selectedTermIdsInArray = new Array();

            selectedTermIdsInArray = selectedTermIds.split(",");
            for (var element = 0; element < selectedTermIdsInArray.length; element++) {
                selectedTermIdsInArray[element] = parseInt(selectedTermIdsInArray[element]);
            }
            uniqueSelectedTermIds = $j.unique(selectedTermIdsInArray);
            uniqueSelectedTermIds = $j.unique(uniqueSelectedTermIds);
            
            if(savedCriteria != null && savedCriteria.isReplaceTerm == 'Y'){
                triggerTermBaseDetailsReplace(colName, sortOrder, pageNum, savedCriteria.pageNumTm, savedCriteria.replaceStr, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, uniqueSelectedTermIds, savedCriteria.langValues, savedCriteria.searchType, savedCriteria.companyValues, savedCriteria.replaceStr, savedCriteria.isReplaceTerm);
            }else {
                triggerTermBaseDetails(colName, sortOrder, pageNum, savedCriteria.pageNumTm, savedCriteria.searchBy, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.langValues, savedCriteria.searchType, savedCriteria.companyValues);
            }
            }
        ;
    });

    $j(".previous").click(function () {
        if ($j(".previous").hasClass("prevEnable")) {
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var langIds = savedCriteria.langIds;
            var pageNum = savedCriteria.pageNum - 1;
            var searchStr = savedCriteria.searchStr;
            var caseSensitiveFlag = savedCriteria.caseSensitiveFlag;
            var filterBy = savedCriteria.filterBy;
            var selectedIds = savedCriteria.selectedIds;
            var separator = "";
            $j("#termDtlRowsList input:checked").each(function (i) {
                if (selectedTermIds == "") {
                    separator = "";
                } else {
                    separator = ",";
                }
                selectedTermIds += separator + $j(this).parent().next().attr("termId");
            });
            triggerTermBaseDetails(colName, sortOrder, pageNum, savedCriteria.pageNumTm, savedCriteria.searchBy, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.langValues, savedCriteria.searchType, savedCriteria.companyValues);
        }

    });

    $j(".tmNext").click(function () {
        if ($j(".tmNext").hasClass("nextEnable")) {
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var pageNumTm = (savedCriteria.pageNumTm == 0) ? (savedCriteria.pageNumTm + 2) : (savedCriteria.pageNumTm + 1);
            savedCriteria.pageNumTm = pageNumTm;
            var searchStr = savedCriteria.searchStr;
            var caseSensitiveFlag = savedCriteria.caseSensitiveFlag;
            var filterBy = savedCriteria.filterBy;
            var selectedIds = savedCriteria.selectedIds;
            var separator = "";
            $j("#tmDtlRowsList input:checked").each(function (i) {
                if (selectedTermIds == "") {
                    separator = "";
                } else {
                    separator = ",";
                }
                selectedTermIds += separator + $j(this).parent().next().attr("termId");
            });
            var selectedTermIdsInArray = new Array();

            selectedTermIdsInArray = selectedTermIds.split(",");
            for (var element = 0; element < selectedTermIdsInArray.length; element++) {
                selectedTermIdsInArray[element] = parseInt(selectedTermIdsInArray[element]);
            }
            uniqueSelectedTermIds = $j.unique(selectedTermIdsInArray);
            uniqueSelectedTermIds = $j.unique(uniqueSelectedTermIds);
//            displayTmDetails(tmData, (savedCriteria.pageNumTm - 1) * termListLimit, (savedCriteria.pageNumTm - 1) * termListLimit + (termListLimit - 1), savedCriteria.pageNumTm);
            if(savedCriteria != null && savedCriteria.isReplaceTerm == 'Y'){
                triggerTMDetailsReplace(colName, sortOrder, savedCriteria.pageNum, pageNumTm, savedCriteria.replaceStr, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, uniqueSelectedTermIds, savedCriteria.langValues, savedCriteria.searchType, savedCriteria.companyValues, savedCriteria.replaceStr, savedCriteria.isReplaceTerm);
            }else {
                triggerTMDetails(colName, sortOrder, savedCriteria.pageNum,pageNumTm, savedCriteria.searchBy, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.langValues, savedCriteria.searchType, savedCriteria.companyValues);
            }
        }
        ;
    });

    $j(".tmPrevious").click(function () {
        if ($j(".tmPrevious").hasClass("prevEnable")) {
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var langIds = savedCriteria.langIds;
            var pageNumTm = savedCriteria.pageNumTm - 1;
            var searchStr = savedCriteria.searchStr;
            var caseSensitiveFlag = savedCriteria.caseSensitiveFlag;
            var filterBy = savedCriteria.filterBy;
            var selectedIds = savedCriteria.selectedIds;
            var separator = "";
            $j("#tmDtlRowsList input:checked").each(function (i) {
                if (selectedTermIds == "") {
                    separator = "";
                } else {
                    separator = ",";
                }
                selectedTermIds += separator + $j(this).parent().next().attr("termId");
            });
//            displayTmDetails(tmData, (pageNumTm - 1) * termListLimit, (pageNumTm - 1) * termListLimit + (termListLimit - 1), pageNumTm);
            triggerTMDetails(colName, sortOrder, savedCriteria.pageNum,pageNumTm, savedCriteria.searchBy, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.langValues, savedCriteria.searchType, savedCriteria.companyValues);
        }

    });

    $j("#mngTrmDtlSectionHead div").click(function () {
        if (termBaseData.pollTermsList.length > 0) {
            var sortOrder = $j(this).attr('sortOrder');
            var colName = $j(this).attr('id');

            if (colName == "column2" || colName == "termEdit") {
                return;
            }
            if (!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))) {
                $j("#mngTrmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).attr('sortOrder', 'ASC');
                });
                $j(this).attr('sortOrder', 'DESC');
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
                $j(this).addClass("ascending");
            } else if ($j(this).hasClass("ascending")) {
                $j("#mngTrmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).attr('sortOrder', 'ASC');
                });
                $j(this).attr('sortOrder', 'ASC');
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
                $j(this).addClass("descending");
            } else if ($j(this).hasClass("descending")) {
                $j("#mngTrmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).attr('sortOrder', 'ASC');
                });
                $j(this).attr('sortOrder', 'DESC');
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
                $j(this).addClass("ascending");
            }


            triggerTermBaseDetails(colName, sortOrder, 1, savedCriteria.pageNumTm, savedCriteria.searchBy, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.langValues, savedCriteria.searchType, savedCriteria.companyValues);
        }
    });


    $j("#tmDtlSectionHead div").click(function () {
        if (tmData.tmProfileList.length > 0) {
            var sortOrder = $j(this).attr('sortOrder');
            var colName = $j(this).attr('id');
            if (colName == "column2" || colName == "termEdit") {
                return;
            }
            if (!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))) {
                $j("#tmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).attr('sortOrder', 'ASC');
                });
                $j(this).attr('sortOrder', 'DESC');
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
                $j(this).addClass("ascending");
            } else if ($j(this).hasClass("ascending")) {
                $j("#tmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).attr('sortOrder', 'ASC');
                });
                $j(this).attr('sortOrder', 'ASC');
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
                $j(this).addClass("descending");
            } else if ($j(this).hasClass("descending")) {
                $j("#tmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).attr('sortOrder', 'ASC');
                });
                $j(this).attr('sortOrder', 'DESC');
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
                $j(this).addClass("ascending");
            }

            triggerTMDetails(colName, sortOrder, savedCriteria.pageNum, 1, savedCriteria.searchBy, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.langValues, savedCriteria.searchType, savedCriteria.companyValues);
        }
    });

    var extendPoll = function (pollDate, termId) {
        var date = new Date(pollDate);
        var day = date.getDate();
        var month = date.getMonth() + 1;
        var year = date.getFullYear();
        var finalDate = month + "-" + day + "-" + year;
        $t.updateExtendPoll(finalDate, termId, {
            success: function (data) {

            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
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


    $j(".changeRqstBtn").click(function () {
        showRequestChangeForm();
        validateRequestChangeDetails.resetForm();

    });
    var showRequestChangeForm = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $("#rqstChangeDiv:ui-dialog").dialog("destroy");

        $("#rqstChangeDiv").dialog({
            height: 255,
            width: 420,
            modal: true,
            close: function (event, ui) {
//					

            }

        });
    };
    var closeLoadingDialog = function (dialogId, id) {
        $j(dialogId).html("");
        $j(dialogId).dialog('destroy');
        $j(id).dialog('destroy');
        mailSent();
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
    $j("#cancelRequestMail").click(function () {
        $j('#rqstChangeDiv').dialog('destroy');

    });


    validateRequestChangeDetails = $j("#rqstChange").validate({
        debug: true,
        rules: {
            source: "required",
            target: "required",
            suggTarget: "required"
        },
        messages: {
            source: "Source is required",
            target: "	Target is required",
            suggTarget: " Suggested new target is required"

        }
    });
    $j("#requestChangeMail").click(function () {
        if ($j("#rqstChange").valid()) {
            $j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif"  align="top" class="rightmargin10" />Sending mail... Please wait</div>');
            showLoadingDialog();
            $j(".ui-dialog-titlebar").hide();
            setTimeout(function () {
                closeLoadingDialog('#loading', '#rqstChangeDiv');

            }, 900);

            var termRequestChange = new Object();
            termRequestChange.sourceTerm = $j("#source").val();
            termRequestChange.targetTerm = $j("#target").val();
            termRequestChange.newSuggestedTerm = $j("#suggTarget").val();
            termRequestChange.notes = $j("#notes").val();
            var termRequestChangeObject = Object.toJSON(termRequestChange);
            $t.requestChangeTerm(termRequestChangeObject, {
                success: function (data) {
                    validateRequestChangeDetails.resetForm();
                    $j("#source").val('');
                    $j("#target").val('');
                    $j("#suggTarget").val('');
                    $j("#notes").val('');

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

    var mailSent = function () {
        $j("#mail_success:ui-dialog").dialog("destroy");

        $j("#mail_success").dialog({
            modal: true,
            buttons: {
                OK: function () {
                    $j(this).dialog("close");
                }
            }
        });
    }

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


    $j("#uploadTermPic").click(function (e) {
        if ($j('#uploadPic').val() == "") {
            alertMessage("#uploadTerm");
        } else {
            e.preventDefault();
            fileName = $j("#uploadPic").val();
            var regexp = /\.(jpg|JPG|jpeg|JPEG|png|PNG|bmp|BMP|gif|GIF)$/i;
            if (fileName.length > 0) {
                if (regexp.test(fileName)) {
                    ajaxFunction();
                    $j("#uploadPicForm").submit();
                }
                else {
                    alertMessage("#InvaliduploadPic");
                }
            }
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
                    photoPath = $j("#contextRootPath").val() + "/images/term_images/" + fileName;
                    if (pictureId != null) {
                        $j("#uploadedImageId_" + pictureId).find("img").attr('src', photoPath);
                        var imageName = fileName + '.' + ext;
                        $t.setTermPhotoPath(pictureId, imageName, {
                            success: function (data) {
                            },
                            error: function (xhr, textStatus, errorThrown) {
                                console.log(xhr.responseText);
                                if (Boolean(xhr.responseText.message)) {
                                    console.log(xhr.responseText.message);
                                }
                            }
                        });
                        pictureId = null;
                    } else {
                        $j("#uploadedImage").attr('src', photoPath);

                    }
                    photoPath = null;
                    $j('#uploadPicBrwse').dialog('destroy');
                }
            }
        }
    };

    $j('#searchField').bind('keydown', function (e) {
        if (e.which == 13) {


            $j("#searchText:ui-dialog").dialog("destroy");
            searchBy = $j("#searchField").val();
            searchBy = $j.trim(searchBy);
            searchBy = searchBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
            searchText = searchBy.replace(/\s\s+/g, ' ');
            searchText = $j.trim(searchText);


            if ($j.trim($j("#searchField").val()) == "") {
                alertMessage("#searchMsg");
            }
            else {
                $j("#chkCase").attr("checked", false);
                $j('#termsModule').show();
                $j('#tmModule').show();
//								 $j(".changeRqstBtn").show();
                showRolePrivileges();

                var searchCriteria = $j("#searchOption :selected").text();
                langValues = $j("#languageSlct").val();
                companyValues = $j("#mutliCompanySlct").val();
                searchStr = $j("#searchCase :selected").text();
                exportBy = $j("#attributeTM :selected").text();
                selectedIds = $j(".serachList ").val();
                searchType = $j("#searchType :selected").text();
                if (searchCriteria == "Termbase") {
                    $j("#manageSearchTermTbl").show();
                    $j("manageTMTermTbl").hide();
                    if (searchStr == 1) {
                        triggerTermBaseDetails(null, null, 0, 0, searchText, searchCriteria, true, exportBy, selectedIds, langValues, searchType, companyValues);
                    } else {
                        triggerTermBaseDetails(null, null, 0, 0, searchText, searchCriteria, false, exportBy, selectedIds, langValues, searchType, companyValues);
                    }
                } else if (searchCriteria == "TM") {
                    $j("#manageTMTermTbl").show();
                    $j("#manageSearchTermTbl").hide();
                    if (searchStr == 1) {
                        triggerTMDetails(null, null, 0, 0, searchText, searchCriteria, true, exportBy, selectedIds, langValues, searchType, companyValues);
                    } else {
                        triggerTMDetails(null, null, 0, 0, searchText, searchCriteria, false, exportBy, selectedIds, langValues, searchType, companyValues);
                    }
                } else {
                    $j("#manageTMTermTbl").show();
                    $j("#manageSearchTermTbl").show();
                    if (searchStr == 1) {
                        triggerTermBaseDetails(null, null, 0, 0, searchText, searchCriteria, true, exportBy, selectedIds, langValues, searchType, companyValues);
                        triggerTMDetails(null, null, 0, 0, searchText, searchCriteria, true, exportBy, selectedIds, langValues, searchType, companyValues);
                    } else {
                        triggerTermBaseDetails(null, null, 0, 0, searchText, searchCriteria, false, exportBy, selectedIds, langValues, searchType, companyValues);
                        triggerTMDetails(null, null, 0, 0, searchText, searchCriteria, false, exportBy, selectedIds, langValues, searchType, companyValues);
                    }
                }


            }

        }
    });

    var showPicture = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#showPicBrwse:ui-dialog").dialog("destroy");

        $j("#showPicBrwse").dialog({
            height: 420,
            width: 630,
            modal: true,
            close: function (event, ui) {
                $j("#showImage").find("img").attr('src', "");
            }

        });

    };

    var saveTermDetails = function (termId) {
        // Create TermInformation Object
        var termDtls = new Object();
        termDtls.termId = parseInt(termId);
        termDtls.sourceTerm = $j("#sourceId_" + termId).val();
        termDtls.termNotes = $j("#notesId_" + termId).val();
        termDtls.conceptDefinition = $j("#defId_" + termId).val();
        termDtls.termUsage = $j("#usageId_" + termId).val();
        if ($j("#targetId_" + termId).val() == "") {
            termDtls.topSuggestion = null;
        } else {
            termDtls.topSuggestion = $j("#targetId_" + termId).val();
        }
        if ($j("#posId_" + termId).val() == "") {
            termDtls.termPOSId = null;
        } else {
            termDtls.termPOSId = parseInt($j("#posId_" + termId).val());
        }
        if ($j("#categoryId_" + termId + " :selected").val() == "") {
            termDtls.termCatagoryId = null;
        } else {
            termDtls.termCatagoryId = parseInt($j("#categoryId_" + termId + " :selected").val());
        }
        if ($j("#domainId_" + termId + " :selected").val() == "") {
            termDtls.termDomainId = null;
        } else {
            termDtls.termDomainId = parseInt($j("#domainId_" + termId + " :selected").val());
        }
        if ($j("#formId_" + termId + " :selected").val() == "") {
            termDtls.termFormId = null;
        } else {
            termDtls.termFormId = parseInt($j("#formId_" + termId + " :selected").val());
        }
        termDtls.termProgramId = null;
        var termDtlsParameter = Object.toJSON(termDtls);
        $t.updateTermDetails(termDtlsParameter, {
            success: function (data, textStatus, xhr) {
                if (xhr.readyState == 4) {
                    if ((xhr.status >= 200 && xhr.status < 300)
                        || xhr.status == 304) {

                    }
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                }
            }
        });

    };

    var saveTmDetails = function (tmProfileInfoId) {
        // Create TermInformation Object
        var tmDtls = new Object();
        tmDtls.tmProfileInfoId = parseInt(tmProfileInfoId);

        if ($j("#targetId_" + tmProfileInfoId).val() == "") {
            tmDtls.topSuggestion = null;
        } else {
            tmDtls.topSuggestion = $j("#targetId_" + tmProfileInfoId).val();
        }
        if ($j("#productId_" + tmProfileInfoId + " :selected").val() == "") {
            tmDtls.productId = null;
        } else {
            tmDtls.productId = parseInt($j("#productId_" + tmProfileInfoId + " :selected").val());
        }
        if ($j("#contentId_" + tmProfileInfoId + " :selected").val() == "") {
            tmDtls.contentTypeId = null;
        } else {
            tmDtls.contentTypeId = parseInt($j("#contentId_" + tmProfileInfoId + " :selected").val());
            ;
        }
        if ($j("#domainId_" + tmProfileInfoId + " :selected").val() == "") {
            tmDtls.domainId = null;
        } else {
            tmDtls.domainId = parseInt($j("#domainId_" + tmProfileInfoId + " :selected").val());
        }

        var tmDtlsParameter = Object.toJSON(tmDtls);
        $t.updateTmDetails(tmDtlsParameter, {
            success: function (data, textStatus, xhr) {
                if (xhr.readyState == 4) {
                    if ((xhr.status >= 200 && xhr.status < 300)
                        || xhr.status == 304) {

                    }
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                }
            }
        });

    };


    $j().ready(function () {
        $j(".changeRqstBtn").hide();
        $j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.SEARCH);
        $j("#currentMenuPage").val(Constants.SUBMENU_ITEMS_SEARCH.SEARCH);

        if ($j("#adminHeaderFlag").val() == "true") {
            $j("#adminHeader").show();
            $j('#adminSearch').css('color', '#0DA7D5');
            $j('#adminSearch').children("img").show();
            $j('#searchId').show();
            $j('#userOptions').hide();
//		 $j('#userTerms').hide();
//		 $j('#userTm').hide();

        }
        if ($j("#userHeaderFlag").val() == "true") {
            $j("#userHeader").show();
            $j('#searchId').hide();
            $j('#userOptions').show();
            $j("#userSearch").children("img").show();
//    	$j('#termList').children("img").show();
            $j('#userSearch').css('color', '#0DA7D5');


        }
        if ($j("#headerFlag").val() == "true") {
            $j(".signUpButton").hide();

        }
        if ($j.browser.version == "9.0") {
            $j(".paddingseven").css("padding-left", "11px");
        }

        if ($j.browser.version == "7.0") {
            $j(".menuArrowSearch").css("top", "26");
            $j(".changeRqstBtn ").css("padding", "1px 0px");
            $j(".newTrmRqst").css("padding-left", "770px");
        }

        $j(".subMenuLinks a").last().css("border-right", "none");

        $j("#changeForm").attr('autocomplete', 'off');


        if ($j.browser.version == "7.0") {
            $j(".menuArrowAdmn").css("top", "26");
            $j(".termAttr").css("padding-bottom", "10px");
        }

        if ($j.browser.version == "9.0") {
            $j("#searchSubmit").css("margin-bottom", "-6px");
        }


        $j('#adminSearch').addClass("on");
        $j('#userSearch').addClass("on");

        $j("#manageSearchTermTbl").termDetails();
        $j("#manageTMTermTbl").tmDetails();

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
        $t.getSuperTranslatorCompanyList({
            success: function (data) {
                displayCompanyList(data, "#mutliCompanySlct");
                $j("#mutliCompanySlct").multiselect().multiselectfilter();

                $j("#mutliCompanySlct").multiselect({
                    noneSelectedText: 'Select company',
                    selectedList: 4, // 0-based index
                    onClose: function () {

                        /* multiCValues = $j("#mutliCompanySlct").val();
                         if(multiCValues ==  null){
                         multiCValues = [];
                         }
                         compSelectedVal=removeEmptyString(compSelectedVal);
                         var isModified = isMultiSelectModified(multiCValues,compSelectedVal);
                         if(multiCValues==""){
                         multiCValues=null;
                         }

                         if(isModified){
                         compSelectedVal=multiCValues;
                         if (multiCValues!=null){
                         $j("#dataPie_1005").empty();
                         triggerTermDetails(null, null, 0, null, false,null,null,'Company',multiCValues,null);

                         }else{
                         triggerTermDetails(null, null, 0, null, false, null, null,null,null,null); 
                         }
                         }*/

                    }

                });

            }
        });


        $t.getLanguageList({
            success: function (data) {
                //displayLanguageList(data,"#languageSlct");
                displayLanguageList(data, "#languageSlct");
                $j("#languageSlct").multiselect().multiselectfilter();
                $j("#languageSlct").multiselect({
                    noneSelectedText: 'Select language',
                    selectedList: 4, // 0-based index
                    onClose: function () {
                    },
                    classes: "lang"

                });
            }
        });
        $j('#searchType').change(function () {
            if ($j(this).val() == 1) {
                if (searchStr == "Sensitive") {
                    caseFlag = true;
                } else {
                    caseFlag = false;
                }
                $j(".caseType").show();
            } else {
                caseFlag = false;
                $j(".caseType").hide();
            }
        });

    });

})(window, jQuery);