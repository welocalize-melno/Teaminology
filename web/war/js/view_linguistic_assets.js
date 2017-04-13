(function (window, $j) {
    var pieChartHeight = 40;
    var pieChartWidth = 40;
    var values = 0;
    var validateDetails;
    var validatePwd;
    var fileName = null;
    var termListLimit = 10;
    var selectedTermIds = "";
    var noOfPages = 0;
    var totalTerms = 0;
    var totalResultedTerms = 0;
    var savedCriteria = null;
    var termListLimit = 10;


    $j.fn.termDetails = function () {

        var ctx = $j(this);

        var termDtlTmpl = ['<div class="rowBg"><div class="row twistie_close" termId="',
            '',
            '"><div class="width200 bigFont  pollTerm">',
            '',
            '</div><div class="smallPie" id=',
            '',
            ' class="width40"></div><div class="width200 bigFont  toppadding5 targetTrm">',
            '',
            '</div><div class="width110 toppadding5 trmLanguage">',
            '',
            '</div><div class="width110 toppadding5  viewDate">',
            '',
            '</div><div class="width110  toppadding5 tPartsOfSpeech">',
            '',
            '</div><div class="width130  toppadding5 tCategory">',
            '',
            '</div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay"  id="overView" termId="',
            '',
            '"><div class="loadingDiv" style="text-align: center;"><img src=' + $j("#contextRootPath").val() + '/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
        ];


        var termAttrTmpl = '<div class="termAttr"><div><div><div class="bold sourceTerm label" >Terms being polled: </div> <span class="sourceDesc viewDetailsFld"></span><span  name="editSourceTerm"  id=#{sourceId} class="sourceDescEdit editDetailsFld nodisplay " ></span><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class=" bold targetTerm label" >Top suggestion: </div> <span class="targetDesc viewDetailsFld"></span><textarea rows="3" cols="65"   name="editTargetTerm"  id=#{targetId} class="targetDescEdit editDetailsFld nodisplay editTargetTerm"  value=""></textarea><div class="clear"></div>'
            + '</div></div><div class="left"><div class="topmargin5"><div class="bold termType label" >Part of speech: </div> <span class="termTypeDesc viewDetailsFld"></span><select name="editTermPOS" id=#{posId} class="editDetailsFld nodisplay editTermPOS"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Form: </div><span class="formDesc viewDetailsFld"></span><select name="termForm"  id=#{formId} class="editDetailsFld nodisplay termForm"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Category: </div><span class="categoryDesc programDesc viewDetailsFld"></span><select name="category" id=#{categoryId} class="editDetailsFld nodisplay  category"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Term notes: </div><span class="domainDesc viewDetailsFld"></span><textarea id=#{notesId} rows="1" cols="12" class="domainDescEdit editDetailsFld nodisplay notes"></textarea>'
            + '</div></div><div class="right"><div class="topmargin5"><div class="bold label">Concept definition: </div><span class="definitionDesc viewDetailsFld"></span><textarea rows="2" cols="30" id=#{defId} class="definitionDescEdit editDetailsFld nodisplay definition"></textarea><div class="clear"></div>'
            + '</div><div class="topmargin10"><div class="bold label">Concept example: </div><span class="usageDesc viewDetailsFld"></span><textarea rows="2" cols="30"   id=#{usageId}  class="usageDesc editDetailsFld nodisplay"></textarea>'
            + '</div><div id="editTermImage" ><div id=#{uploadedImageId}  style="width:50px; border:1px solid #cccccc;height: 50px;margin-top: 26px;margin-left: 1px;"><img width="50px" height="50px" class="picture headerMenuLink " src=#{imagePath}  /></div><div class=" alignCenter" id=#{changePicId}><a href="javascript:;" class="changeTermPic"   style="font-size: 14px;padding-right: 294px;">Image</a></div></div></div></div>';


        var deprecatedTermInfoSec = ['<div class="clear"></div><div class=" bigFont" style="color:red;"> <span class="width220 label" style="color:red;padding-left:39px">Deprecated Source: ',
            '',
            '</span><div class="width50" >&nbsp;</div><span class=" width220 label">Deprecated Target: ',
            '',
            '</span></div>'
        ];
        var classNames = ['purpleBar', 'greenBar', 'yellowBar', 'pinkBar'];
        var startWaiting = function (msg, selector, floatRight) {
            if (selector) {
                if (typeof msg == 'undefined' || msg == null) {
                    msg = "&nbsp; Loading... please wait.";
                }
                var temp = '<div class="loading-msg alignCenter topmargin15"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif" />' + msg + '</div>';
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


            var listLength = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;
            for (var count = 0; count < listLength; count++) {
                var termDtlTmplClone = termDtlTmpl;
                var suggestedTerm = (termDetails[count].suggestedTerm == null || termDetails[count].suggestedTerm == "") ? "&nbsp;" : termDetails[count].suggestedTerm;
                var partOfSpeech = (termDetails[count].partOfSpeech == null || termDetails[count].partOfSpeech == "") ? "&nbsp;" : termDetails[count].partOfSpeech;
                var language = (termDetails[count].language == null || termDetails[count].language == "") ? "&nbsp;" : termDetails[count].language;
                var category = (termDetails[count].category == null || termDetails[count].category == "") ? "&nbsp;" : termDetails[count].category;
                termDtlTmplClone[1] = termDetails[count].termId;
                var sourceTerm = termDetails[count].sourceTerm;
                if (termDetails[count].deprecatedCount > 0) {
                    if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                        termDtlTmplClone[3] = "<span style='font-weight:bold;font-size:20px;color:red'><b>!</b></span>" + sourceTerm;
                    }
                    else {
                        termDtlTmplClone[3] = "<span style='font-weight:bold;font-size:17px;color:red'><b>!</b></span>" + sourceTerm;
                    }
                } else {
                    termDtlTmplClone[3] = sourceTerm;
                }
                termDtlTmplClone[5] = "dataPie_" + termDetails[count].termId;
                termDtlTmplClone[7] = suggestedTerm;
                termDtlTmplClone[9] = language;
                termDtlTmplClone[11] = termDetails[count].pollExpirationDt;
                termDtlTmplClone[13] = partOfSpeech;
                termDtlTmplClone[15] = category;
                termDtlTmplClone[17] = termDetails[count].termId;
                $j('#termDtlRowsList').append(termDtlTmplClone.join(""));
                if ((termDetails[count].status) == "Approved") {
                    $j('.tickImg_' + termDetails[count].termId).css("visibility", "visible");
                }

                var date = new Date();
                var currentMonth = date.getMonth();
                var currentDate = date.getDate();
                var currentYear = date.getFullYear();
                $j(function () {
                    $j("#" + termDtlTmplClone[19]).datepicker({
                        showOn: "button",
                        buttonImage: "images/calendar.gif",
                        buttonImageOnly: true,
                        minDate: new Date(currentYear, currentMonth, currentDate)  //Disables all the past dates and only current or future dats can be selected
                    });
                });

            }
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

        var isDate = function (txtDate) {
            var currVal = txtDate;
            if (currVal == '')
                return false;

            //Declare Regex
            var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/;
            var dtArray = currVal.match(rxDatePattern); // is format OK?

            if (dtArray == null)
                return false;

            //Checks for mm/dd/yyyy format.
            dtMonth = dtArray[1];
            dtDay = dtArray[3];
            dtYear = dtArray[5];

            if (dtMonth < 1 || dtMonth > 12)
                return false;
            else if (dtDay < 1 || dtDay > 31)
                return false;
            else if ((dtMonth == 4 || dtMonth == 6 || dtMonth == 9 || dtMonth == 11) && dtDay == 31)
                return false;
            else if (dtMonth == 2) {
                var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
                if (dtDay > 29 || (dtDay == 29 && !isleap))
                    return false;
            }
            return true;
        };


        var bindEvents = function (data) {
            var classNamesClone = classNames.slice(0);
            $j('#termDtlRowsList .row ').click(function () {
                var row = $j(this);
                if ($j(this).hasClass('twistie_close')) {
                    $j(this).parent().next().next().show();
                    $j(this).removeClass("twistie_close");
                    $j(this).addClass("twistie_open");
                    var termId = $j(this).attr("termId");

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
                                    var targetTerm = (termInfo.suggestedTerm == null || termInfo.suggestedTerm == null) ? "" : termInfo.suggestedTerm;
                                    var termBeingPolled = (termInfo.termBeingPolled == null || termInfo.termBeingPolled == null) ? "" : termInfo.termBeingPolled;

                                    detailsSec.find('.sourceDesc').html(termBeingPolled);
                                    detailsSec.find('.sourceDescEdit').html(termBeingPolled);
                                    detailsSec.find('.targetDesc').html(targetTerm);
                                    detailsSec.find('.targetDescEdit').html(targetTerm);
                                    detailsSec.find('.usageDesc').html(termUsage);
                                    detailsSec.find('.usageDescEdit').html(termUsage);
                                    detailsSec.find('.definitionDesc').html(conceptDefinition);
                                    detailsSec.find('.definitionDescEdit').html(conceptDefinition);
                                    detailsSec.find('.domainDesc').html(termNotes);
                                    detailsSec.find('.domainDescEdit').html(termNotes);
//									detailsSec.find('.programDesc').html(programName);\
                                    detailsSec.find('.categoryDesc').html(categoryName);
                                    detailsSec.find('.formDesc').html(formName);
                                    detailsSec.find('.termTypeDesc').html(partOfSpeech);


                                    var origDef = detailsSec.find('.domainDesc').html();
                                    if (origDef.length > 40) {
                                        var finalText = origDef.substring(0, 36);
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
                                            finalSource = finalSource + data.deprecatedTermInfo[i].deprecatedSource + ",";
                                        if (data.deprecatedTermInfo[i].deprecatedTarget != null && $j.trim(data.deprecatedTermInfo[i].deprecatedTarget) != "")
                                            finalTarget = finalTarget + data.deprecatedTermInfo[i].deprecatedTarget + ",";
                                    }

                                    finalSource = finalSource.substring(0, finalSource.lastIndexOf(","));
                                    finalTarget = finalTarget.substring(0, finalTarget.lastIndexOf(","));

                                    deprecatedTermInfoSec[1] = finalSource;
                                    deprecatedTermInfoSec[3] = finalTarget;
                                    detailsSec.append(deprecatedTermInfoSecClone.join(""));
                                }


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
                                detailsSec.find('.editDetails ').click(function () {
                                    row.find('.editDetailsFld').show();
                                    row.find('.viewDetailsFld').hide();
                                    detailsSec.find('.termAttr').css("padding-bottom", "68px");

//									$j(".termPOS").html("<option>---Select---</option>");
//									$j(".termCatLst").html("<option>---Select---</option>");
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

//									$t.getCategoryList({
//										success:function(data){
//											$j(".termCatLst").html("<option>---Select---</option>");
//										 displayTermCatList(data,".termCatLst");
//											var selectedCat = row.find('.category').html();
//											 $j("select[name='termCatLst'] option").each(function(){
//													if ($j(this).text() == selectedCat)
//														$j(this).attr("selected","selected");
//												});
//										},
//										error: function(xhr, textStatus, errorThrown){
//											console.log(xhr.responseText);
//											if(Boolean(xhr.responseText.message)){
//												console.log(xhr.responseText.message);
//											}
//										}
//									});

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
                                    var suggTerm = $j("#targetId_" + termId).val();
                                    var termPOS = $j("#posId_" + termId + " :selected").text();
                                    var termCat = $j("#categoryId_" + termId + " :selected").text();
                                    var term = (termPOS == "---Select---") ? "&nbsp;" : termPOS;
                                    var showCategory = (termCat == "---Select---") ? "&nbsp;" : termCat;
                                    var topSuggestedTerm = (suggTerm == null || suggTerm == "") ? "&nbsp;" : suggTerm;
                                    row.find('.tPartsOfSpeech').html(term).show();
                                    row.find('.tCategory').html(showCategory).show();
                                    row.find('.targetTrm').html(topSuggestedTerm).show();
                                    row.find('.targetTrm').each(function (i) {
                                        var origText = $j(this).text();
                                        if (origText.length > 8) {
                                            var finalText = origText.substring(0, 7);
                                            finalText = finalText + "...";
                                            $j(this).text(finalText);
                                            $j(this).attr("title", origText);
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
                                    row.find(".targetTrm").text($j(this).parent().find(".termSuggestion").html());
                                    row.find('.targetTrm').each(function (i) {
                                        var origText = $j(this).text();
                                        if (origText.length > 10) {
                                            var finalText = origText.substring(0, 8);
                                            finalText = finalText + "...";
                                            $j(this).text(finalText);
                                            $j(this).attr("title", origText);
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


        ctx.bind("showTermDetails", function (event, criteria) {
            startWaiting("&nbsp;Loading ... please wait", $j('#termDtlRowsList'), false);
            $j("#pagination").hide();
            var pageNum = criteria.pageNum;
            var queryAppender = new Object();
            queryAppender.colName = criteria.colName;
            queryAppender.sortOrder = criteria.sortOrder;
            queryAppender.pageNum = criteria.pageNum;
            queryAppender.searchStr = criteria.searchStr;
            queryAppender.caseFlag = criteria.caseSensitiveFlag;
            queryAppender.filterBy = criteria.filterBy;
            queryAppender.selectedIds = criteria.selectedIds;
            queryAppender.isTM = criteria.isTM;
            var searchStr = criteria.searchStr;
            var filterBy = criteria.filterBy;
            searchString = criteria.searchStr;
            var queryAppenderParameter = Object.toJSON(queryAppender);
            $t.getUserManagePollTerms(queryAppenderParameter, {
                success: function (data) {

                    var termDetails = data;
                    if (termDetails == null) {
                        var displayCriteria = $j("#newTerm").val();
                        if (displayCriteria == "Enter term to search...") {
                            displayCriteria = "No data to display";
                        } else {
                            displayCriteria = 'No matches found for term : "<span class="bold">' + $j("#newTerm").val() + '</span>"';
                        }

                        $j("#termDtlRowsList").html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">' + displayCriteria + '</span>');
                        $j("#pagination").hide();

                    } else {

                        var length = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;

                        var startLimit = 0;
                        var endLimit = 0;


                        if (pageNum == 0) {
                            totalTerms = termDetails.length;
                        }
                        /*else if(searchStr == null  && filterBy == null){
                         totalTerms = termDetails.length;
                         totalTerms = parseInt(totalTerms);
                         }*/

                        noOfPages = Math.round(totalTerms / 10);
                        noOfPages = (totalTerms % 10 < 5 && totalTerms % 10 > 0) ? noOfPages + 1 : noOfPages;

                        if (pageNum == 0) {
                            startLimit = 1;
                            endLimit = (termListLimit > totalTerms) ? totalTerms : termListLimit;
                        } else {
                            startLimit = ((pageNum - 1) * termListLimit) + 1;
                            var tempLimit = (pageNum) * termListLimit;
                            endLimit = (parseInt(tempLimit) > parseInt(totalTerms)) ? totalTerms : tempLimit;
                        }
                        displayTermDetails(data);

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
                        $j("#rangeOfTerms").html(startLimit + "-" + endLimit);
                        var totalTerms3 = insertCommmas(new String(totalTerms));
                        $j("#totalPolledTerms").html(totalTerms3);
                        pagination(noOfPages, pageNum);
                        bindEvents();
                        $j('div.pollTerm').each(function (i) {
                            var origText = $j(this).text();
                            var searchedtext = $j(this).find('.selectedbg').text();
                            if (origText.lastIndexOf("!") > 0) {
                                srcText = origText.substr(0, origText.lastIndexOf("!"));
                                var supscript = "";
                                if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                                    supscript = "<span style='font-weight:bold;font-size:20px; color:red'><b>!</b></span>";
                                }
                                else {
                                    supscript = "<span style='font-weight:bold;font-size:15px; color:red'><b>!</b></span>";
                                }
                                if (srcText.length > 20) {
                                    var finalText = srcText.substring(0, 18);
                                    if (searchedtext != "") {
                                        finalText = finalText.replace(searchedtext, "<span class='selectedbg'>" + searchedtext + "</span>");
                                    }

                                    finalText = finalText + "...";
                                    $j(this).html(finalText + supscript);
                                    $j(this).attr("title", srcText);

                                }
                            }
                            else {
                                $j(this).addClass("toppadding5");
                                if (origText.length > 20) {
                                    var finalText = origText.substring(0, 18);
                                    if (searchedtext != "") {
                                        finalText = finalText.replace(searchedtext, "<span class='selectedbg'>" + searchedtext + "</span>");
                                    }

                                    finalText = finalText + "...";
                                    $j(this).html(finalText);
                                    $j(this).attr("title", origText);

                                }
                            }

                        });

                        $j('div.targetTrm').each(function (i) {
                            var origText = $j(this).text();
                            var searchedtext = $j(this).find('.selectedbg').text();
                            if (origText.length > 8) {
                                var finalText = origText.substring(0, 7);
                                if (searchedtext != "") {
                                    finalText = finalText.replace(searchedtext, "<span class='selectedbg'>" + searchedtext + "</span>");
                                }
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

    $j("#mngTrmDtlSectionHead div").click(function () {
        var sortOrder = $j(this).attr('sortOrder');
        var colName = $j(this).attr('id');
        if (colName == "column2") {
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
        triggerTermDetails(colName, sortOrder, 1, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.isTM);

    });
    var triggerTermDetails = function (colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, isTM) {
        $j("#selectAll").attr('checked', false);
        if (colName == null) {
            $j("#mngTrmDtlSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
        }
        $j('#termDtlRowsList').empty();
        ChartRender.destroyTwoDPieChart();
        $t.getTotalTermsInGlossary({
            success: function (data) {
                var totalTerms1 = new String(data);
                var totalTerms2 = insertCommmas(totalTerms1);
                $j("#totalTerms").html(totalTerms2);
                $j("#termsInGlossary").val(totalTerms1);
                var criteria = {
                    'colName': colName,
                    'sortOrder': sortOrder,
                    'pageNum': pageNum,
                    'searchStr': searchStr,
                    'caseSensitiveFlag': caseSensitiveFlag,
                    'filterBy': filterBy,
                    'selectedIds': selectedIds,
                    'isTM': isTM
                };
                savedCriteria = criteria;
                $j("#manageTermTbl").trigger("showTermDetails", criteria);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

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

            var isTM = savedCriteria.isTM;
            var separator = "";
            $j("#termDtlRowsList input:checked").each(function (i) {
                if (selectedTermIds == "") {
                    separator = "";
                } else {
                    separator = ",";
                }
                selectedTermIds += separator + $j(this).parent().next().attr("termId");
            });
            triggerTermDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, isTM);

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
            var isTM = savedCriteria.isTM;
            var separator = "";
            $j("#termDtlRowsList input:checked").each(function (i) {
                if (selectedTermIds == "") {
                    separator = "";
                } else {
                    separator = ",";
                }
                selectedTermIds += separator + $j(this).parent().next().attr("termId");
            });
            triggerTermDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, isTM);


        }

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


    $j.fn.tmDetails = function () {

        var ctx = $j(this);

        var tmDtlTmpl = ['<div class="rowBg" style="font-size:11px;"><div  class="row twistie_close"  termId="',
            '',
            '" id="rowid_',
            '',
            '"><div class="width220 bigFont  toppadding5 sourceTm">',
            '',
            '</div><div class="width220 bigFont toppadding5 targetTm">',
            '',
            '</div><div class="width110 toppadding5 tmLanguage smallFont">',
            '',
            '</div><div class="width130  toppadding5 productLine">',
            '',
            '</div><div class="width130  toppadding5 industryDomain">',
            '',
            '</div><div class="width90  toppadding5 contentType">',
            '',
            '</div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay"  style="min-height:150px;"   id="overView" termId="',
            '',
            '"><div class="loadingDiv" style="text-align: center;"><img src=' + $j("#contextRootPath").val() + '/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
        ];

        var tmAttrTmpl = '<div class="termAttr"><div><div><div class="bold sourceTerm label" >Source: </div> <span class="sourceDesc viewDetailsFld"></span><span  name="editSourceTerm"  id=#{sourceId} class="sourceDescEdit editDetailsFld nodisplay " ></span><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class=" bold targetTerm label" >Target: </div> <span class="targetDesc viewDetailsFld"></span><textarea rows="3" cols="44"   name="editTargetTerm"  id=#{targetId} class="targetDescEdit editDetailsFld nodisplay editTargetTerm"  value=""></textarea><div class="clear"></div>'
            + '</div></div><div class="left"><div class="topmargin5"><div class="bold label">Product Line: </div><span class="productDesc viewDetailsFld"></span><select name="editProductLine" id=#{productId} class="editDetailsFld  productDescEdit nodisplay   editProductLine"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div><div class="topmargin5" ><div class="bold industry label" >Industry Domain: </div> <span class="domainDesc viewDetailsFld"></span><select name="editDomain" id=#{domainId} class="editDetailsFld nodisplay editDomain domainDescEdit"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Content Type: </div><span class="contentDesc viewDetailsFld"></span><select name="editContent" id=#{contentId} class="editDetailsFld  contentDescEdit nodisplay editContent"><option value="">---Select---</option></select><div class="clear"></div></div></div>';

        var classNames = ['purpleBar', 'greenBar', 'yellowBar', 'pinkBar'];
        var startWaiting = function (msg, selector, floatRight) {
            if (selector) {
                if (typeof msg == 'undefined' || msg == null) {
                    msg = "&nbsp; Loading... please wait.";
                }
                var temp = '<div class="loading-msg alignCenter topmargin15"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif" />' + msg + '</div>';
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

            var tmDetails = data;


            var listLength = (tmDetails.length >= termListLimit) ? termListLimit : tmDetails.length;
            for (var count = 0; count < listLength; count++) {
                var tmDtlTmplClone = tmDtlTmpl;
                var sourceTerm = (tmDetails[count].sourceTerm == null || tmDetails[count].sourceTerm == "") ? "&nbsp;" : tmDetails[count].sourceTerm;
                var targetTerm = (tmDetails[count].targetTerm == null || tmDetails[count].targetTerm == "") ? "&nbsp;" : tmDetails[count].targetTerm;
                var tmLanguage = (tmDetails[count].targetLang == null || tmDetails[count].targetLang == "") ? "&nbsp;" : tmDetails[count].targetLang;
                var IndustryDomain = (tmDetails[count].domain == null || tmDetails[count].domain == "") ? "&nbsp;" : tmDetails[count].domain;
                var productLine = (tmDetails[count].productLine == null || tmDetails[count].productLine == "") ? "&nbsp;" : tmDetails[count].productLine;
                var contentType = (tmDetails[count].contentType == null || tmDetails[count].contentType == "") ? "&nbsp;" : tmDetails[count].contentType;


                tmDtlTmplClone[1] = tmDetails[count].tmProfileInfoId;
                tmDtlTmplClone[3] = tmDetails[count].tmProfileInfoId;
                tmDtlTmplClone[5] = sourceTerm;
                tmDtlTmplClone[7] = targetTerm
                tmDtlTmplClone[9] = tmDetails[count].targetLang;
//				tmDtlTmplClone[13] = company;
                tmDtlTmplClone[11] = productLine;
                tmDtlTmplClone[13] = IndustryDomain;
                tmDtlTmplClone[15] = contentType;
                tmDtlTmplClone[17] = tmDetails[count].tmProfileInfoId;
//				tmDtlTmplClone[19] = tmDetails[count].tmProfileInfoId;
//				tmDtlTmplClone[21] = tmDetails[count].tmProfileInfoId;
                $j('#tmDtlRowsList').append(tmDtlTmplClone.join(""));


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

        var isDate = function (txtDate) {
            var currVal = txtDate;
            if (currVal == '')
                return false;

            //Declare Regex
            var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/;
            var dtArray = currVal.match(rxDatePattern); // is format OK?

            if (dtArray == null)
                return false;

            //Checks for mm/dd/yyyy format.
            dtMonth = dtArray[1];
            dtDay = dtArray[3];
            dtYear = dtArray[5];

            if (dtMonth < 1 || dtMonth > 12)
                return false;
            else if (dtDay < 1 || dtDay > 31)
                return false;
            else if ((dtMonth == 4 || dtMonth == 6 || dtMonth == 9 || dtMonth == 11) && dtDay == 31)
                return false;
            else if (dtMonth == 2) {
                var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
                if (dtDay > 29 || (dtDay == 29 && !isleap))
                    return false;
            }
            return true;
        };


        var bindEvents = function (data) {
            var classNamesClone = classNames.slice(0);
            $j('#tmDtlRowsList .row ').click(function () {
                var row = $j(this);
                if ($j(this).hasClass('twistie_close')) {
                    $j(this).parent().next().next().show();
                    $j(this).removeClass("twistie_close");
                    $j(this).addClass("twistie_open");
                    var tmProfileInfoId = $j(this).attr("termId");

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
//								var suggTrmListTmplClone = suggTrmListTmpl;
//								var deprecatedTermInfoSecClone= deprecatedTermInfoSec;

                                if (tmInfo != null) {
                                    var tmDomain = (tmInfo.domain == null || tmInfo.domain.domain == null) ? "" : tmInfo.domain.domain;
                                    var tmContent = (tmInfo.contentType == null || tmInfo.contentType.contentType == null) ? "" : tmInfo.contentType.contentType;
                                    var tmProductLine = (tmInfo.productGroup == null || tmInfo.productGroup.product == null) ? "" : tmInfo.productGroup.product;
                                    var source = (tmInfo.source == null || tmInfo.source == null) ? "" : tmInfo.source;
                                    var target = (tmInfo.target == null || tmInfo.target == null) ? "" : tmInfo.target;
                                    detailsSec.find('.sourceDesc').html(source);
                                    detailsSec.find('.sourceDescEdit').html(source);
                                    detailsSec.find('.targetDesc').html(target);
                                    detailsSec.find('.targetDescEdit').html(target);
                                    detailsSec.find('.contentDesc').html(tmContent);
                                    detailsSec.find('.domainDesc').html(tmDomain);
                                    detailsSec.find('.productDesc').html(tmProductLine);

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


        ctx.bind("showTmDetails", function (event, criteria) {
            startWaiting("&nbsp;Loading ... please wait", $j('#tmDtlRowsList'), false);
            $j("#pagination").hide();
            var pageNum = criteria.pageNum;
            var queryAppender = new Object();
            queryAppender.colName = criteria.colName;
            queryAppender.sortOrder = criteria.sortOrder;
            queryAppender.pageNum = criteria.pageNum;
            queryAppender.searchStr = criteria.searchStr;
            queryAppender.caseFlag = criteria.caseSensitiveFlag;
            queryAppender.filterBy = criteria.filterBy;
            queryAppender.selectedIds = criteria.selectedIds;
            searchStringTM = criteria.searchStr;
            var searchStr = criteria.searchStr;
            var filterBy = criteria.filterBy;
            var queryAppenderParameter = Object.toJSON(queryAppender);
            $t.getUserTMProfileTerms(queryAppenderParameter, {
                success: function (data) {

                    var termDetails = data;
                    if (termDetails == null || termDetails.size() == 0) {
                        var displayCriteria = $j("#newTmTerm").val();
                        if (displayCriteria == "Enter term to search...") {
                            displayCriteria = "No data to display";
                        } else {
                            displayCriteria = 'No matches found for term : "<span class="bold">' + $j("#newTmTerm").val() + '</span>"';
                        }

                        $j("#tmDtlRowsList").html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">' + displayCriteria + '</span>');
                        $j("#paginationTm").hide();

                    } else {

                        var length = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;
                        var startLimit = 0;
                        var endLimit = 0;
                        if (pageNum == 0) {
                            totalTerms = termDetails.length;
                        }
                        noOfPages = Math.round(totalTerms / 10);
                        noOfPages = (totalTerms % 10 < 5 && totalTerms % 10 > 0) ? noOfPages + 1 : noOfPages;

                        if (pageNum == 0) {
                            startLimit = 1;
                            endLimit = (termListLimit > totalTerms) ? totalTerms : termListLimit;
                        } else {
                            startLimit = ((pageNum - 1) * termListLimit) + 1;
                            var tempLimit = (pageNum) * termListLimit;
                            endLimit = (parseInt(tempLimit) > parseInt(totalTerms)) ? totalTerms : tempLimit;
                        }
                        displayTmDetails(data);

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
                        $j("#paginationTm").show();
                        $j("#rangeOfTms").html(startLimit + "-" + endLimit);
                        var totalTerms3 = insertCommmas(new String(totalTerms));
                        $j("#totalPolledTms").html(totalTerms3);
                        paginationTm(noOfPages, pageNum);
                        bindEvents();
                        $j('div.sourceTm').each(function (i) {
                            var origText = $j(this).text();
                            var searchedtext = $j(this).find('.selectedbg').text();
                            if (origText.lastIndexOf("!") > 0) {
                                srcText = origText.substr(0, origText.lastIndexOf("!"));
                                var supscript = "";
                                if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                                    supscript = "<span style='font-weight:bold;font-size:18px; color:red'><sup><b>!</b></sup></span>";
                                }
                                else {
                                    supscript = "<span style='font-weight:bold;font-size:16px; color:red'><sup><b>!</b></sup></span>";
                                }
                                if (srcText.length > 25) {
                                    var finalText = srcText.substring(0, 22);
                                    if (searchedtext != "") {
                                        finalText = finalText.replace(searchedtext, "<span class='selectedbg'>" + searchedtext + "</span>");
                                    }
                                }
                                finalText = finalText + "...";
                                $j(this).html(finalText + supscript);
                                $j(this).attr("title", srcText);
                            }
                            else {
                                if (origText.length > 25) {
                                    var finalText = origText.substring(0, 22);
                                    if (searchedtext != "") {
                                        finalText = finalText.replace(searchedtext, "<span class='selectedbg'>" + searchedtext + "</span>");
                                    }
                                    finalText = finalText + "...";
                                    $j(this).html(finalText);
                                    $j(this).attr("title", origText);
                                }
                            }
                        });

                        $j('div.targetTm').each(function (i) {
                            var origText = $j(this).text();
                            var searchedtext = $j(this).find('.selectedbg').text();
                            if (origText.length > 25) {
                                var finalText = origText.substring(0, 22);
                                if (searchedtext != "") {
                                    finalText = finalText.replace(searchedtext, "<span class='selectedbg'>" + searchedtext + "</span>");
                                }

                                finalText = finalText + "...";
                                $j(this).html(finalText);
                                $j(this).attr("title", origText);
                            }

                        });
                        $j("#selectAllTms").click(function (event) {
                            event.stopPropagation();
                            $j('.case').attr('checked', this.checked);

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

    var langSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    $t.getActiveUsersCount({
        success: function (data) {
            if (data != null) {
                var totalTerms1 = new String(data);
                var totalTerms2 = insertCommmas(totalTerms1);
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

    var userDetailTmpl = ['<div class="userImg"><img src="',
        '',
        '" height="50px" width="50px" /></div><div class="userDetails"><h5>',
        '',
        '</h5><p class="smallFont">',
        '',
        ' votes</p><p class="smallFont">Member since: ',
        '',
        '</p></div>'
    ];
    var displayUserDetails = function (data) {
        $j('#userInfo').html("");
        var userDetails = data;
        var userDetailTmplClone = userDetailTmpl;
        var count = userDetails.totalVotes;
        var totalTerms1 = new String(count);
        userDetailTmplClone[1] = userDetails.photoPath;
        userDetailTmplClone[3] = userDetails.userName;
        userDetailTmplClone[5] = insertCommmas(totalTerms1);
        userDetailTmplClone[7] = userDetails.createDate;
        $j('#userInfo').append(userDetailTmplClone.join(""));
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


    /*var chatData = function () {

        $t.getUserAccuracyRate({
            success: function (data) {
                var finalisedTerms = data.finalizedTerm;
                var votedTerms = data.votedTerms;
                var dataXML = new Array();
                if (votedTerms == 0) {
                    $j("#accuracyRate").html("0%");
                    if ($j("#hpsite").val() == "true")
                    dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#0e94bc, #D1EAF1' bgColor='#F9F9F9' showBorder='0' bgAlpha='100'>");
                    else
                     dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");

                    dataXML.push("<set label='Approved' value='" + finalisedTerms + "' />");
                    dataXML.push("<set label='Incorrect' value='" + (votedTerms - finalisedTerms) + "' />");
                    dataXML.push("</chart>");
                    ChartRender.twoDPieChart("0", 130, 70, "accuracyPie", dataXML);

                } else {

                    var accuracyRate = Math.round((finalisedTerms / votedTerms) * 100);
                    if(!(isNaN(accuracyRate))) {
                    	$j("#accuracyRate").html(accuracyRate + "%")
                    }else {
                    	$j("#accuracyRate").html("0%")
                    }
                    if ($j("#hpsite").val() == "true")
                    dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#0e94bc, #D1EAF1' bgColor='#032D39' showBorder='0' bgAlpha='100'>");
                   
                    else
                    dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");

                    
                    dataXML.push("<set label='Approved' value='" + finalisedTerms + "' />");
                    dataXML.push("<set label='Incorrect' value='" + (votedTerms - finalisedTerms) + "' />");
                    dataXML.push("</chart>");
                    ChartRender.twoDPieChart("0", 130, 70, "accuracyPie", dataXML);
                }

            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });


    };*/
    var chatData = function () {

        $t.getUserAccuracyRate({
            success: function (data) {
                var finalisedTerms = data.finalizedTerm;
                var votedTerms = data.votedTerms;
                var dataXML = new Array();
                if (votedTerms == 0) {
                    $j("#accuracyRate").html("0%");
                    if ($j("#hpsite").val() == "true")
                    dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#0e94bc, #D1EAF1' bgColor='#F9F9F9' showBorder='0' bgAlpha='100'>");
                    else
                     dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");

                    dataXML.push("<set label='Approved' value='" + finalisedTerms + "' />");
                    dataXML.push("<set label='Incorrect' value='" + (votedTerms - finalisedTerms) + "' />");
                    dataXML.push("</chart>");
                    ChartRender.twoDPieChart("0", 130, 70, "accuracyPie", dataXML);

                } else {

                    var accuracyRate = Math.round((finalisedTerms / votedTerms) * 100);
                    if(!(isNaN(accuracyRate))) {
                    	$j("#accuracyRate").html(accuracyRate + "%")
                    }else {
                    	$j("#accuracyRate").html("0%")
                    }
                    if ($j("#hpsite").val() == "true")
                    dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#0e94bc, #D1EAF1' bgColor='#032D39' showBorder='0' bgAlpha='100'>");
                   
                    else
                    dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");

                    
                    dataXML.push("<set label='Approved' value='" + finalisedTerms + "' />");
                    dataXML.push("<set label='Incorrect' value='" + (votedTerms - finalisedTerms) + "' />");
                    dataXML.push("</chart>");
                    ChartRender.twoDPieChart("0", 130, 70, "accuracyPie", dataXML);
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
    var paginationTm = function (noOfPages, pageNum) {
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
    var triggerTmDetails = function (colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds) {
        if (colName == null) {
            $j("#mngTmDtlSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
        }
        $j('#tmDtlRowsList').empty();
        ChartRender.destroyTwoDPieChart();
        $t.getTotalTermsInTM({
            success: function (data) {
                var totalTerms1 = new String(data);
                var totalTerms2 = insertCommmas(totalTerms1);
                $j("#totalTerms").html(totalTerms2);
                $j("#termsInGlossary").val(totalTerms1);
                var criteria = {
                    'colName': colName,
                    'sortOrder': sortOrder,
                    'pageNum': pageNum,
                    'searchStr': searchStr,
                    'caseSensitiveFlag': caseSensitiveFlag,
                    'filterBy': filterBy,
                    'selectedIds': selectedIds

                };

                savedCriteria = criteria;
                $j("#manageTmTbl").trigger("showTmDetails", criteria);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

    };
    $j(".tmNext").click(function () {
        if ($j(".tmNext").hasClass("nextEnable")) {
            $j("#selectAllTms").attr('checked', false);
            $j(".case").attr('checked', false);
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var pageNum = (savedCriteria.pageNum == 0) ? (savedCriteria.pageNum + 2) : (savedCriteria.pageNum + 1);
            savedCriteria.pageNum = pageNum;
            var searchStr = savedCriteria.searchStr;
            var caseSensitiveFlag = savedCriteria.caseSensitiveFlag;
            var filterBy = savedCriteria.filterBy;
            var selectedIds = savedCriteria.selectedIds;

            var isTM = savedCriteria.isTM;
            var separator = "";
            $j("#tmDtlRowsList input:checked").each(function (i) {
                if (selectedTermIds == "") {
                    separator = "";
                } else {
                    separator = ",";
                }
                selectedTermIds += separator + $j(this).parent().next().attr("termId");
            });
            triggerTmDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds);
        }
        ;
    });

    $j(".tmPrevious").click(function () {
        if ($j(".tmPrevious").hasClass("prevEnable")) {
            $j("#selectAllTms").attr('checked', false);
            $j(".case").attr('checked', false);
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var langIds = savedCriteria.langIds;
            var pageNum = savedCriteria.pageNum - 1;
            var searchStr = savedCriteria.searchStr;
            var caseSensitiveFlag = savedCriteria.caseSensitiveFlag;
            var filterBy = savedCriteria.filterBy;
            var selectedIds = savedCriteria.selectedIds;
            var isTM = savedCriteria.isTM;
            var separator = "";
            $j("#tmDtlRowsList input:checked").each(function (i) {
                if (selectedTermIds == "") {
                    separator = "";
                } else {
                    separator = ",";
                }
                selectedTermIds += separator + $j(this).parent().next().attr("termId");
            });

            triggerTmDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds);

        }

    });

    $j("#mngTmDtlSectionHead div").click(function () {
        var sortOrder = $j(this).attr('sortOrder');
        var colName = $j(this).attr('id');
        if (colName == "column2") {
            return;
        }
        if (!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))) {
            $j("#mngTmDtlSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
            $j(this).attr('sortOrder', 'DESC');
            $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
            $j(this).addClass("ascending");
        } else if ($j(this).hasClass("ascending")) {
            $j("#mngTmDtlSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
            $j(this).attr('sortOrder', 'ASC');
            $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
            $j(this).addClass("descending");
        } else if ($j(this).hasClass("descending")) {
            $j("#mngTmDtlSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
            $j(this).attr('sortOrder', 'DESC');
            $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
            $j(this).addClass("ascending");
        }

        triggerTmDetails(colName, sortOrder, 1, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds);
    });

    $j().ready(function () {

        if ($j("#adminHeaderFlag").val() == "true") {
            $j("#adminHeader").show();
            $j('#adminProfile').css('color', '#0DA7D5');
            $j('#adminProfile').children("img").show();
            $j(".accuracyChrt").hide();

        }
        if ($j("#userHeaderFlag").val() == "true") {
            $j("#userHeader").show();
            $j('#userLinguisticAssets').children("img").show();
            $j('#userLinguisticAssets').css('color', '#0DA7D5');
            $j(".accuracyChrt").show();
            ChartRender.destroyTwoDPieChart();
            chatData();
        }
        if ($j("#headerFlag").val() == "true") {
            $j(".signUpButton").hide();

        }
        if ($j.browser.version == "9.0") {
            $j(".paddingseven").css("padding-left", "11px");
        }

        if ($j.browser.version == "7.0") {
            $j(".menuArrowProf").css("top", "26");
        }


        $j(".subMenuLinks a").last().css("border-right", "none");

        $j("#changeForm").attr('autocomplete', 'off');
//
        $j("#manageTermTbl").termDetails();
        $j("#manageTmTbl").tmDetails();
//
        triggerTermDetails(null, null, 0, null, false, null, null, 'N');

        if ($j.browser.version == "7.0") {
            $j(".menuArrowAdmn").css("top", "26");
            $j(".termAttr").css("padding-bottom", "10px");
        }

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

        chatData();

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
                    	dataXML.push("<chart showValues='0' adjustDiv='0' showBorder='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0'  bgColor='#f9f9f9,#f9f9f9' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#f9f9f9' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='2' lineColor='#0e94bc' canvasBorderThickness='0'>");
                    else
                    	dataXML.push("<chart showValues='0' adjustDiv='0' showBorder='0' bgColor='#032D39,#032D39' bgAlpha='100,100' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#ffffff' divLineThickness='2' labelPadding='0'  baseFontColor='#C0C0C0' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
                    for(var count=0;count<totalUsersPerMonth.length;count++) {
                        dataXML.push("<set label='"+totalUsersPerMonth[count].month + "' " + "value='" +totalUsersPerMonth[count].count +"'/>");
                    }
                    dataXML.push("</chart>");
                    ChartRender.twoDLineChart("myChartId3", "240", "70", "userChart", dataXML);
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
        $j("#tabs").tabs({

            select: function (event, ui) {

                var $tabs = $j('#tabs').tabs();
                var selected = ui.index;
                if (selected != '0') {
                    $j('#termsModule').hide();
                    $j("#tmModule").hide();
                }
                else {
                    $j('#termsModule').show();
                    $j("#tmModule").hide();
                }
                if (selected == 1) {
                    triggerTmDetails(null, null, 0, null, false, null, null);
                    $j("#tmModule").show();
                    $j("#termsModule").hide();
                }

                $j("#message").html("");
            }
        });

    });

})(window, jQuery);