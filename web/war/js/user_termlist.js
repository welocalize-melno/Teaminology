(function (window, $j) {
    var langId = 0;
    var savedCriteria = null;
    var noOfPages_user = 0;
    var totalTerms_user = 0;
    var termListLimit = 10;
    var extraPolls = 0;
    var pieChartHeight = 40;
    var pieChartWidth = 40;
    var currPage = "";
    var validateTermDetails;
    var validateRequestChangeDetails;
    var values = 0;
    var texts = [];
    var popData = null;
    var totalVotes = 0;
    var pagenationHideCount = 0;
    LeaderBoard.showLeaderBoard();
	// $j("#signOut").addClass("errorBorder").addClass('logOutClass');
    var leaderBoardTmpl = ['<div class="leaderBrdItem"><div class="itemImg" style="width:64px; height: 68px;border: 1px solid #aeaeae;"><img src="',
                           '',
                           '" height="68px" width="64px"/></div><div class="itemDesc"><div class="smallFont leaderBrdSmallFont bold" style="margin-top: -4px;">',
                           '',
                           '</div> <div class="badge" style=" margin-bottom: 6px;" ><img style="height: 39px;width:203px;" src="',
                           '',
                           '" /></div><div class="" style="color: #a9a9a9; float:left; font-size: 10px; margin-right: 10px;">',
                           '',
                           ' votes</div> <div class="floatleft starsDiv" ><img src="',
                           '',
                           '"  /></div></div></div>'
                           ];
	
    var url = $j(location).attr('href');

	if(url.indexOf("/user_termlist.jsp") != -1){
	    $j('#about').removeClass('aboutForDashboard');
	    $j('#about').addClass('aboutForDashboardMargin');
	}
    $j.fn.termDetails = function () {

        var ctx = $j(this);

        var termDtlTmpl = [
            '<div class="rowBg"><div class="row" style="padding-left: 15px; width:680px;" termId="',
            '',
            '"><div class="width295 grayBigFont toppadding5  pollTerm fontStyle" style="color:#5a5a5a;font-size:16px;">',
            '',
            '</div><div class="widthCls toppadding8 grayTxt expiryDate" style="text-align:left;padding-top:5px;color:#5a5a5a; font-size:16px;">',
            '',
            '</div><div class="width20 toppadding5"><img src="' + $j("#contextRootPath").val() + '/images/grey_checkmark.png" height="20px" width="20px" class="novisibility tickImg ',
            '',
            '" /></div><div class="twistie_close_user redBigFont voteStat toppadding5" style="float:right;margin-right:20px;width:34px;cursor: pointer;font-size:16px;">Vote',
            '',
            '<i class="fa fa-caret-down " style="position: relative; left: 38px; top: -20px;display:none; height: 0;"></i></div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay" style="background-color:#fafafa;" termId="',
            '',
            '"><div class="loadingDiv" style="text-align: center;"><img src="' + $j("#contextRootPath").val() + '/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
        ];


        var termDtlVotedTmpl = [
            '<div class="rowBg"><div class="row twistie_close" termId="',
            '',
            '"><div class="width221 bigFont  toppadding5 pollTerm">',
            '',
            '</div><div class="smallPie" id=',
            '',
            ' class="width50"></div><div class="width221 bigFont  toppadding5 targetTrm"  style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap; padding-left: 11px;">',
            '',
            '</div><div class="width20 toppadding5"><img src="' + $j("#contextRootPath").val() + '/images/grey_checkmark.png" height="20px" width="20px" class="novisibility tickImg ',
            '',
            '" /></div><div class="width81 toppadding5 votedDate">',
            '',
            '</div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay" termId="',
            '',
            '"><div class="loadingDiv" style="text-align: center;"><img src="' + $j("#contextRootPath").val() + '/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
        ];
        
        var votedTermAttrTmpl = '<div class="termAttr"><div class="left"><div><div class="bold termType label" style="" >Part of speech: </div> <span class="termTypeDesc viewDetailsFld" style=""></span><select name="termPOS" id=#{posId} class="editDetailsFld nodisplay termPOS"></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Form: </div><span class="formDesc viewDetailsFld"></span><select name="termForm"  id=#{formId} class="editDetailsFld nodisplay termForm"></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Category: </div><span class="categoryDesc programDesc viewDetailsFld"></span><select name="category" id=#{categoryId} class="editDetailsFld nodisplay program category"></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Term Notes: </div><span class="domainDesc viewDetailsFld"></span><textarea id=#{notesId} rows="1" cols="12" class="domainDescEdit editDetailsFld nodisplay notes"></textarea>'
            + '</div></div><div class="right"><div><div class="bold label">Concept definition: </div><span class="definitionDesc viewDetailsFld" style="word-wrap:break-word;"></span><textarea rows="2" cols="30" id=#{defId} class="definitionDescEdit editDetailsFld nodisplay definition"></textarea><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Contextual example: </div><span class="usageDesc viewDetailsFld" style="word-wrap:break-word;"></span><textarea rows="2" cols="30" id=#{usageId} class="usageDescEdit editDetailsFld nodisplay usage"></textarea>'
            + '</div><div id="editTermImage" ><div id=uploadedImageId  style="width:50px; border:1px solid #cccccc;height: 50px;margin-top: 25px;margin-left: 1px;"><img width="50px" height="50px" id=termImgId class="headerMenuLink" src=""  /></div></div></div></div><div class="clear"></div><div class="topmargin5 voteHeader"><p id="viewComment" class="bold">Vote on the best translation:</p>'
            + '<a href="javascript:;" class="userComments nodisplay">View User Comments</a></div>';

        var termAttrTmpl = '<div class="viewDtlsRowMain"><div class="termAttr"><div class="left"><div><div id="editTermImage" style="width:52px;float: left;margin-right:12px;" ><div id=uploadedImageId  style="width:50px; border:1px solid #cccccc;height: 50px;margin-left: 1px;"><img width="50px" height="50px" id=termImgId class="headerMenuLink" src=""  /></div></div><div style="width: 274px;"><div class="bold termType label" style="color:#5a5a5a;font-size:12px;">Part of Speech: </div> <span class="termTypeDesc viewDetailsFld" style="color:#aeaeae;position:relative;top:1px;font-size:12px;"></span><select style="color:#aeaeae;" name="termPOS" id=#{posId} class="editDetailsFld nodisplay termPOS"></select>'
            + '</div><div class="topmargin5"><div class="bold label" style="color:#5a5a5a;font-size:12px;">Form: </div><span class="formDesc viewDetailsFld" style="color:#aeaeae;position:relative;top:1px;font-size:12px;"></span><select style="color:#aeaeae;" name="termForm"  id=#{formId} class="editDetailsFld nodisplay termForm"></select>'
            + '</div><div class="topmargin5"><div class="bold label" style="color:#5a5a5a;font-size:12px;">Category: </div><span class="categoryDesc programDesc viewDetailsFld" style="color:#aeaeae;position:relative;top:1px;font-size:12px;"></span><select style="color:#aeaeae;" name="category" id=#{categoryId} class="editDetailsFld nodisplay program category"></select>'
            + '</div><div class="clear"></div><div class="topmargin5"><div class="bold label" style="color:#5a5a5a;font-size:12px;">Term Notes: </div><span class="domainDesc viewDetailsFld" style="color:#aeaeae;position:relative;top:1px;font-size:12px;"></span><textarea style="color:#aeaeae;" id=#{notesId} rows="1" cols="12" class="domainDescEdit editDetailsFld nodisplay notes"></textarea>'
            + '</div></div><div class="clear"><div style="width:348px;"><div class="bold label" style="margin-top:5px;color:#5a5a5a;font-size:12px;">Concept Definition: </div><span class="definitionDesc viewDetailsFld" style="word-wrap:break-word;color:#aeaeae;position:relative;top:6px;font-size:12px;"></span><textarea style="color:#aeaeae;" rows="2" cols="30" id=#{defId} class="definitionDescEdit editDetailsFld nodisplay definition"></textarea></div><div class="clear"></div>'
            + '</div><div class="clear"></div><div style="width:348px;"><div class="bold label" style="color:#5a5a5a;font-size:12px;margin-top:5px;">Contextual Example: </div><span class="usageDesc viewDetailsFld" style="word-wrap:break-word;color:#aeaeae;position:relative;top:6px;font-size:12px;"></span><textarea style="color:#aeaeae;" rows="2" cols="30" id=#{usageId} class="usageDescEdit editDetailsFld nodisplay usage"></textarea>'
            + '</div></div></div><div class="right termAttrRight" style="float:right;width:346px;"><div><div class="topmargin5 voteHeader"><p id="viewComment" class="bold" style="color:#5a5a5a;">Vote on the best translation:</p>'
            + '<a href="javascript:;" class="userComments nodisplay">View User Comments</a></div><div class="rightClass"></div></div></div></div></div>';


        var suggTrmListTmpl = [
            '<div class="termSlctFrm topmargin15"><input style="position:relative;top:-2px;" type="radio" class="radioClass nodisplay" id="',
            '',
            '" name="',
            '',
            '" value="',
            '',
            '" /><span class="termSuggestion" style=" word-wrap: break-word;margin-left: 5px;color:#5a5a5a;font-size:12px;">',
            '',
            '<div style="font-size:11px;font-style: italic;">',
            '',
            '</div></span><div class="votesBar" id="',
            '',
            '">',
            '',
            '</div></div><br/>',
        ];
        
        var votedSuggTrmListTmpl = [
                               '<div class="termSlctFrm topmargin15"><span class="termSuggestion" style=" word-wrap: break-word;">',
                               '',
                               '<div style="font-size:11px;font-style: italic;">',
                               '',
                               '</div></span><input type="radio" class="radioClass nodisplay" id="',
                               '',
                               '" name="',
                               '',
                               '" value="',
                               '',
                               '" /><div class="votesBar" id="',
                               '',
                               '">',
                               '',
                               '</div></div><div class="clear"></div>',
                           ];
                           
        
       /* var suggTrmListForFinalTmpl = [
                               '<div class="termSlctFrm topmargin15"><input type="radio" class="radioClass nodisplay" id="',
                               '',
                               '" name="',
                               '',
                               '" value="',
                               '',
                               '" />"',
                               '<span class="termSuggestion finalColor" style=" word-wrap: break-word;">',
                               '',
                               '<div style="font-size:11px;font-style: italic;">',
                               '',
                               '</div></span><div class="votesBar" id="',
                               '',
                               '">',
                               '',
                               '</div></div><br/>',
                           ];*/
        
        var suggTrmListForFinalTmpl = [
                                       '<div class="termSlctFrm topmargin15"><input type="radio" class="radioClass nodisplay" id="',
                                       '',
                                       '" name="',
                                       '',
                                       '" value="',
                                       '',
                                       '" /><span class="termSuggestion finalColor" style=" word-wrap: break-word;">',
                                       '',
                                       '<div style="font-size:11px;font-style: italic;">',
                                       '',
                                       '</div></span><div class="votesBar" id="',
                                       '',
                                       '">',
                                       '',
                                       '</div></div><div class="clear"></div>',
                                ];
        
        var votedSuggFinalTrmListTmpl = [
                                    '<div class="termSlctFrm topmargin15"><span class="termSuggestion finalColor" style=" word-wrap: break-word;">',
                                    '',
                                    '<div style="font-size:11px;font-style: italic;">',
                                    '',
                                    '</div></span><input type="radio" class="radioClass nodisplay" id="',
                                    '',
                                    '" name="',
                                    '',
                                    '" value="',
                                    '',
                                    '" /><div class="votesBar" id="',
                                    '',
                                    '">',
                                    '',
                                    '</div></div><div class="clear"></div>',
                                ];

        var newTermSec = ['<div class="termSlctFrm topmargin15 nodisplay newSuggestion" title="Your term" style=" word-wrap: break-word;"><div class="termSuggestion bold" style=" word-wrap: break-word; width:290px;"></div></div><div class="newTermFld" style="clear: both;"><input id="new" type="radio" style=" word-wrap: break-word;position:relative;top:3px;" value="new" name="',
            '',
            '" /><input type="text" size="40" style=" word-wrap: break-word;background-color: #fafafa;border: 1px solid #e1e1e1; font-family: Hp Simplified;" placeholder="Enter New Term" class="suggTxt leftmargin5" id="',
            '',
            '" /></div><div class="commentsFld"><textarea placeholder="Enter Comments" class="commentTxt leftmargin5" style="word-wrap: break-word;color:#5a5a5a;height:100px;width:257px;background-color: #fafafa;border: 1px solid #e1e1e1;"></textarea></div><div style="float: right; margin-right:47px;padding-bottom: 10px;"><input style="width:128px;background-color: #aeaeae;border: 1px solid #aeaeae;color: #fff; height: 30px; font-family: HP Simplified;" type="submit" value="Decline to Vote" class="rejectTermBtn" id="',
            '',
            '" /><input type="button" value="Submit" class="sbtVoteBtn" style="height: 30px; font-family: HP Simplified;font-weight:bold; width:128px;margin-left:10px;background-color: #2ba6cb;border: 1px solid #2ba6cb;color: #fff;" id="',
            '',
            '" /></div> <div class="votesBtnForVoting nodisplay" style="margin-top:50px;"><div class="noOfTerms"></div></div><div class="nodisplay" style="padding-left: 205px; padding-top:20px;"><input type="button" class="commonBtn gotToNxt" value="Vote on next term" /> </div>'
        ];

        var classNames = ['purpleBar', 'greenBar', 'yellowBar', 'pinkBar'];

        var displayTermDetails = function (data) {
            var termDetails = data;
            var listLength = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;
            for (var count = 0; count < listLength; count++) {
                var termDtlTmplClone = termDtlTmpl;
                termDtlTmplClone[1] = termDetails[count].termId;
                termDtlTmplClone[3] = termDetails[count].sourceTerm;
                termDtlTmplClone[5] = termDetails[count].pollExpirationDt;
                termDtlTmplClone[7] = "tickImg_" + termDetails[count].termId;
                termDtlTmplClone[11] = termDetails[count].termId;
                $j('#termDtlRowsList').append(termDtlTmplClone.join(""));
                if ((termDetails[count].status) == "Approved") {
                    $j('.tickImg_' + termDetails[count].termId).css("visibility", "visible");
                }
            }

        };
        var displayVotedTermDetails = function (data) {
            var termDetails = data;
            var length = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;
            for (var count = 0; count < length; count++) {
                var termDtlTmplClone = termDtlVotedTmpl;
                var suggestedTerm = (termDetails[count].suggestedTerm == null || termDetails[count].suggestedTerm == "") ? "&nbsp;" : termDetails[count].suggestedTerm;
                termDtlTmplClone[1] = termDetails[count].termId;
                termDtlTmplClone[3] = termDetails[count].sourceTerm;
                termDtlTmplClone[5] = "dataPie_" + termDetails[count].termId;
                termDtlTmplClone[7] = suggestedTerm;
                termDtlTmplClone[9] = "tickImg_" + termDetails[count].termId;
                termDtlTmplClone[11] = termDetails[count].votingDate;
                termDtlTmplClone[13] = termDetails[count].termId;
                $j('#votedRowsList').append(termDtlTmplClone.join(""));
                if ((termDetails[count].status) == "Approved") {
                    $j('.tickImg_' + termDetails[count].termId).css("visibility", "visible");
                }
            }

        };

        var partOfSpeechSlctTmpl = ['<option value="',
            '',
            '" >',
            '',
            '</option>'
        ];


        var termFormSlctTmpl = ['<option value="',
            '',
            '" >',
            '',
            '</option>'
        ];

        var displayTermFormList = function (data) {
            var trmFormList = data.trmFormList;
            for (var count = 0; count < trmFormList.length; count++) {
                var termFormSlctTmplClone = termFormSlctTmpl;
                termFormSlctTmplClone[1] = trmFormList[count].formLabel;
                termFormSlctTmplClone[3] = trmFormList[count].formLabel;
                $j('.termForm').append(termFormSlctTmplClone.join(""));
            }
        };

        var programSlctTmpl = ['<option value="',
            '',
            '" >',
            '',
            '</option>'
        ];

        var displayProgramList = function (data) {
            var programList = data.programList;
            for (var count = 0; count < programList.length; count++) {
                var programSlctTmplClone = programSlctTmpl;
                programSlctTmplClone[1] = programList[count].programId;
                programSlctTmplClone[3] = programList[count].programLabel;
                $j('.program').append(programSlctTmplClone.join(""));
            }
        };

        var showDialog = function () {
            // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
            $("#fullTrmInfo:ui-dialog").dialog("destroy");

            $("#fullTrmInfo").dialog({
                height: 510,
                width: 590,
                modal: true
            });
        };
        
        var showVotingDetails = function () {
            $j("#votiongDetailsDiv:ui-dialog").dialog("destroy");

            $j("#votiongDetailsDiv").dialog({
                height: 600,
                width: 800,
                modal: true,
            });
            
            $j('#votiongDetailsTab').hover( function(){
            	
            	$j('#votiongDetailsTab').find('.subdata').each(function(){
            		modalRender.bubble('#'+$(this).attr('id'),  $(this).find('.tip-text').text(), "left center", "right center");
            	});
          });
            
        };
        
        var bindEvents = function (data) {
            var classNamesClone = classNames.slice(0);
            $j('#termDtlRowsList .twistie_close_user ').click(function () {
            	$t.getUserVotedTermDetails(langId, null, 'ASC', 0, {
            		 success: function (data) {
            			totalVotes = data.length;
            		 },
            		 error: function (xhr, textStatus, errorThrown) {
                         console.log(xhr.responseText);
                         if (Boolean(xhr.responseText.message)) {
                             console.log(xhr.responseText.message);
                         }
                     }
            	});
                var row = $j(this).parent();
                if ($j(this).hasClass('twistie_close_user')) {
                	$j(this).css('margin-top','-3px');
                    $j(this).parent().parent().next().next().show();
                    $(this).prev().prev().prev().addClass('blueClrFont').removeClass('fontStyle');
                    $j(this).removeClass("twistie_close_user");
                    $j(this).addClass("twistie_open_user");
                    $j(this).find('.fa-caret-down').css("display","block");
                    var termId = $j(this).parent().attr("termId");
                    if ($j(this).html() == "Voted") {

                    }
                    else {
                        $t.getTermAttributes(termId, {
                            success: function (data) {
                                if (Boolean(data)) {
                                    var termInfo = data.termInfo;
                                    var defaultImg = $j("#contextRootPath").val() + "/images/default.jpg";
                                    var detailsSec = row.parent().siblings('.viewDtlsRow[termId=' + row.attr('termId') + '] ');


                                    detailsSec.html(termAttrTmpl);
                                    if ($j.browser.version == "7.0") {
                                        $j(".termAttr").css("min-height", "135px");
                                    }

                                    var termUsage = (termInfo.termUsage == null) ? "&nbsp;" : termInfo.termUsage;
                                    var conceptDefinition = (termInfo.conceptDefinition == null) ? "&nbsp;" : termInfo.conceptDefinition;
                                    var termNotes = (termInfo.termNotes == null) ? "&nbsp;" : termInfo.termNotes;
                                    var categoryName = (termInfo.termCategory == null || termInfo.termCategory.category == null) ? "&nbsp;" : termInfo.termCategory.category;
                                    var formName = (termInfo.termForm == null || termInfo.termForm.formName == null) ? "&nbsp;" : termInfo.termForm.formName;
                                    var partOfSpeech = (termInfo.termPOS == null || termInfo.termPOS.partOfSpeech == null) ? "&nbsp;" : termInfo.termPOS.partOfSpeech;
                                    var termImage = (termInfo.photoPath == null) ? defaultImg : termInfo.photoPath;
                                    if (termInfo.termUsage == null || termInfo.termUsage == "") {
                                        detailsSec.find("#editTermImage").css("margin-top", "0px");
                                    } else {
                                        detailsSec.find("#editTermImage").css("margin-top", "0px");
                                    }

                                    detailsSec.find('.usageDesc').html(termUsage);
                                    detailsSec.find('.definitionDesc').html(conceptDefinition);
                                    detailsSec.find('.domainDesc').html(termNotes);
                                    detailsSec.find('.categoryDesc').html(categoryName);
                                    detailsSec.find('.formDesc').html(formName);
                                    detailsSec.find('.termTypeDesc').html(partOfSpeech);
                                    detailsSec.find('.termTypeDesc').attr("id", data.TypeId);
                                    detailsSec.find('.moreAttr').attr("termId", row.attr('termId'));
                                    detailsSec.find("#termImgId").attr('src', termImage);

                                    var origNotes = detailsSec.find('.domainDesc').html();
                                    if (origNotes.length > 40) {
                                        var finalText = origNotes.substring(0, 36);
                                        finalText = finalText + "...";
                                        detailsSec.find('.domainDesc').text(finalText);
                                        detailsSec.find('.domainDesc').attr("title", origNotes);
                                    }
                                   // var origDef = detailsSec.find('.definitionDesc').html();
                                    /*if (origDef.length > 50) {
                                        var finalText = origDef.substring(0, 48);
                                        finalText = finalText + "...";
                                        detailsSec.find('.definitionDesc').text(finalText);
                                        detailsSec.find('.definitionDesc').attr("title", origDef);
                                    }*/
                                    /*var origExmpl = detailsSec.find('.usageDesc').html();
                                    if (origExmpl.length > 50) {
                                        var finalText = origExmpl.substring(0, 48);
                                        finalText = finalText + "...";
                                        detailsSec.find('.usageDesc').text(finalText);
                                        detailsSec.find('.usageDesc').attr("title", origExmpl);
                                    }*/
                                    
                                    if (data.suggestedTermDetails != null && data.suggestedTermDetails.length > 0) {

                                        for (var i = 0; i < data.suggestedTermDetails.length; i++) {

                                            var numRand = Math.floor(Math.random() * 101);
                                            var suggTrmListTmplClone = suggTrmListTmpl;
                                            var suggTrmListForFinalTermTmplClone = suggTrmListForFinalTmpl;
                                            suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTermId;
                                            suggTrmListTmplClone[3] = row.attr('termId');
                                            suggTrmListTmplClone[5] = data.suggestedTermDetails[i].suggestedTerm;
                                            suggTrmListForFinalTermTmplClone[1] = data.suggestedTermDetails[i].suggestedTermId;
                                            suggTrmListForFinalTermTmplClone[3] = row.attr('termId');
                                            suggTrmListForFinalTermTmplClone[5] = data.suggestedTermDetails[i].suggestedTerm;
                                            
                                            if (data.suggestedTermDetails[i].isUpdated == 'Y' && data.suggestedTermDetails[i].noOfVotes > 0) {
                                                suggTrmListTmplClone[7] = data.suggestedTermDetails[i].suggestedTerm;
                                                suggTrmListTmplClone[9] = "(" + Constants.UPDATED_TERM_MSG + ")";
                                                suggTrmListForFinalTermTmplClone[7] = data.suggestedTermDetails[i].suggestedTerm;
                                                suggTrmListForFinalTermTmplClone[9] = "(" + Constants.UPDATED_TERM_MSG + ")";
                                            } else {
                                            	suggTrmListTmplClone[7] = data.suggestedTermDetails[i].suggestedTerm;
                                            	suggTrmListTmplClone[9] = "";
                                            	suggTrmListForFinalTermTmplClone[7] = data.suggestedTermDetails[i].suggestedTerm;
                                            	suggTrmListForFinalTermTmplClone[9] = "";
                                            }
                                            suggTrmListTmplClone[11] = "barId_" + row.attr('termId') + i;
                                            suggTrmListTmplClone[13] = data.suggestedTermDetails[i].noOfVotes
                                            suggTrmListForFinalTermTmplClone[11] = "barId_" + row.attr('termId') + i;
                                            suggTrmListForFinalTermTmplClone[13] = data.suggestedTermDetails[i].noOfVotes
                                           /* suggTrmListForFinalTermTmplClone[5] = data.suggestedTermDetails[i].suggestedTermId;
                                            suggTrmListForFinalTermTmplClone[7] = row.attr('termId');
                                            suggTrmListForFinalTermTmplClone[9] = data.suggestedTermDetails[i].suggestedTerm;
                                            suggTrmListForFinalTermTmplClone[11] = "barId_" + row.attr('termId') + i;
                                            suggTrmListForFinalTermTmplClone[13] = data.suggestedTermDetails[i].noOfVotes;;*/

                                            if(data.suggestedTermDetails[i].suggestedTerm != null && $j.trim(data.suggestedTermDetails[i].suggestedTerm) != "")
                                            	 if((data.suggestedTermDetails[i].suggestedTerm == termInfo.suggestedTerm) && termInfo.termStatusId == 2) {
                                            		 detailsSec.find('.rightClass').append(suggTrmListForFinalTermTmplClone.join(""));
                                                 } else {
                                                	 detailsSec.find('.rightClass').append(suggTrmListTmplClone.join(""));
                                                 }{
                                            	$j('.radioClass').show();
                                            }
                                        }
                                    }
                                }
                                newTermSec[1] = row.attr('termId');
                                newTermSec[3] = "sugg_" + row.attr('termId');
                                newTermSec[5] = "rejectVote" + row.attr('termId');
                                newTermSec[7] = "submitVote" + row.attr('termId');
                                detailsSec.find('.rightClass').append(newTermSec.join(""));
                              //  detailsSec.append();
                              //  detailsSec.append('<div style="font-size:12px;font-weight:bold;color:#AEAEAE;" class="noOfTerms">No.of Submitted Terms : '+totalVotes+'</div>');
                               // $j('#trmDtlSectionHead').find('.votedTrms').html(" ( "+totalVotes + ")") ;
                                var termId = row.attr('termId');
                                 
                                  $t.getUserCommets(termId, {
                                        success: function (data) {
                                            if (data != null && data.commentData != null && data.commentData != '') {
                                            	detailsSec.append('<div  id="votiongDetailsDiv" title="User Comments">'
                                             		    + '<table cellpadding="0" cellspacing="0" border="0" class="display" id="votiongDetailsTab" width="100%" style="table-layout:fixed;">'
                                             			+ '<thead> <th width="20%" style="height:40px;color: #5a5a5a;font-size: 14px;">UserName</th> <th width="25%" style="height:40px;color: #5a5a5a;font-size: 14px;">Comments</th> <th width="15%" style="height:40px;color: #5a5a5a;font-size: 14px;">Comment Date</th> </thead> <tbody style="font-size:12px;">'	
                                             			+ '</tbody></table> </div>');
                                            	var count = 0;
                                           	 detailsSec.find('#votiongDetailsTab').dataTable({
                                                   "bProcessing": true,
                                                   "sServerMethod": "GET",
                                                   "bAutoWidth": false,
                                                   "bPaginate": true,
                                                   "bDestroy": true,
                                                   "sAjaxDataProp": "commentData",
                                                   "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getUserComment/" + termId,
                                                   "aoColumns": [
                                                       { "mDataProp": "userName", "sWidth": "20%"},
                                                       { "mDataProp": "userComment", "sWidth": "25%",
                                                       "fnRender": function (oObj) {
                                                    	   txt=oObj.aData[oObj.oSettings.aoColumns[oObj.iDataColumn].mDataProp];
                                                    	   if(txt != null){
                                                      			var tempHtml = "<span style='word-wrap:break-word;'>"+txt+"</span>";
                                                      			return tempHtml;
                                                      		} 
                                                       }
                                               		
                                                       /*	"fnRender": function (oObj) {
                                                       		 txt=oObj.aData[oObj.oSettings.aoColumns[oObj.iDataColumn].mDataProp];
                                                       		if(txt != null){
                                                       		if(txt.length>100){
                                                       			popData=txt;
                                                       			var tempHtml = "<div  class='subdata' id='subdata_"+count+"'>"+txt.substring(0,50)+" "+txt.substring(50,100) +"..."+"<span style='display: none;' class='tip-text'>"+txt+"</span></div>";
                                                       			count++;
                                                       			return tempHtml;
                                                       		}
                                                       		else {
                                                       			return "<div>"+txt +"</div>";
                                                       		}
                                                       	}
                                                           }*/
                                                    },
                                                       { "mDataProp": "commentDate", "sWidth": "15%"}
                                                   ],
                                                   "iDisplayLength": 10
                                               });
                                             $j("#votiongDetailsTab_info").hide();
                                             detailsSec.find('#votiongDetailsTab').find('sorting_asc').add('sorting');
                                             if(data.commentData.length < 10) {
                                            	 detailsSec.find("#votiongDetailsTab_info").hide();
                                            	 detailsSec.find("#votiongDetailsTab_paginate").hide();
                                             }else {
                                             	detailsSec.find("#votiongDetailsTab_info").show();
                                             }
                                             detailsSec.find("#votiongDetailsTab_filter").hide();
                                             detailsSec.find("#votiongDetailsTab_length").hide();
                                             //detailsSec.find(".sorting_asc").hide();
                                             //detailsSec.find("#sorting").hide();
                                             
                                            } else {
                                            	detailsSec.append('<div class="noComments">No comments have been submitted for this term.</div>');
                                            }
                                        },
                                        error: function (xhr, textStatus, errorThrown) {
                                            console.log(xhr.responseText);
                                            if (Boolean(xhr.responseText.message)) {
                                                console.log(xhr.responseText.message);
                                            }
                                        }
                                    });
                              //  if ($j('#votiongDetailsTab').length > 0) {
                                
                                    
                             //   }
                                $j('.dataTables_length').hide();
                                $j('#votiongDetailsTab_filter').hide();
                                //showVotingDetails(data);
                                
                                var largest = {
                                    val: null
                                };
                                for (var i in data.suggestedTermDetails) {
                                    if (data.suggestedTermDetails[i].noOfVotes > largest.val) {
                                        largest.val = data.suggestedTermDetails[i].noOfVotes;

                                    }
                                }
                                $j("#termImgId").hover(function () {
                                    modalRender.bubble("#termImgId", "click for full image", "left center", "right center");
                                });

                                $j("#termImgId").click(function () {
                                    $t.getTermAttributes(termId, {
                                        success: function (imgData) {
                                            if (data != null && data.termInfo != null) {
                                                var hoverImagepath = (imgData.termInfo.photoPath == null) ? defaultImg : imgData.termInfo.photoPath;
                                                showTermPicture();
                                                $j("#showTermImage").find("img").attr('src', hoverImagepath);
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

                                /*Hiding Votes Bar and other modules from User Screen before he votes*/
                                if ($j("body").hasClass('userTermsList')) {
                                    detailsSec.find('.votesBar').hide();

                                }

                                detailsSec.find('.sbtVoteBtn').click(function () {
                                    var termId = $j(this).attr("id");
                                    termId = termId.replace("submitVote", "");
                                    var suggestedTermId = null;
                                    var suggestedTxt = detailsSec.find("#sugg_" + termId).val();
                                    
                                  	  if($(".commentTxt").val().length > 500) {
	                                  		  alertMessage("#userComments");
	                                		  return;
                                  		  
                                  	  }
                                    if ($j("body").hasClass('userTermsList')) {
                                        if (detailsSec.find('input:radio[name=row.attr("termId")]:checked').val() == '' || detailsSec.find('input:radio[name=row.attr("termId")]:checked').val() == null) {
                                            alertMessage("#userOption");
                                        }
                                        else {
                                            if (detailsSec.find('input:radio[name=row.attr("termId")]:checked').val() == "new") {
                                                if (suggestedTxt == "Enter your own translation" || $j.trim(suggestedTxt) == "") {
                                                    alertMessage("#trslTerm");
                                                    return;
                                                }
                                                suggestedTermId = detailsSec.find('input:radio[name=row.attr("termId")]:checked').attr("id");

                                                detailsSec.find(".newSuggestion span").html(suggestedTxt);
                                                detailsSec.find(".newSuggestion").show();

                                            }
                                            if (detailsSec.find('input:radio[name=row.attr("termId")]:checked').val() != "new") {
                                                suggestedTxt = "";
                                                suggestedTermId = detailsSec.find('input:radio[name=row.attr("termId")]:checked').attr("id");
                                                detailsSec.find('input:radio[name=row.attr("termId")]:checked').prev().css("font-weight", "bold");
                                            }
                                            detailsSec.find('.voteHeader').hide();
                                            row.find(".voteStat").html("Voted");
                                            $t.getUserPollTerms(langId,null, null, 0, {
                                             	 success: function (data) {
                                             		 if(data != null){
                                             			 $j('#totalCount_'+langId).html(data.length);
                                             		 }else{
                                             			 $j('#totalCount_'+langId).html("0"); 
                                             		 }
                                             	 }
                                              });
                                            var voted = row.find(".voteStat");
                                            if(voted != null) {
                                	        totalVotes = totalVotes+1;
                                	        console.log("Total votes after submit" +totalVotes);
                                	        $j('.showTermsVoted').find('.totalVotes').html(" (" + totalVotes + ")"); 
                                            }
                                            voteTerm(suggestedTermId, termId, suggestedTxt);
                                            detailsSec.find('.votesBtn').hide();
                                            detailsSec.find('.commentsFld').hide();
                                            detailsSec.find('.newTermFld').hide();
                                            detailsSec.find('input').hide();
                                           // detailsSec.find(".noOfTerms").html("No.of Submitted Terms: "+totalVotes);
                                            detailsSec.find('.votesBar').each(function (i) {
                                                $j(this).show();
                                                $j(this).attr("id", " ");
                                            });
                                        }

                                    }

                                });

                                detailsSec.find('.rejectTermBtn').click(function () {

                                    row.parent().remove();
                                    detailsSec.remove();
                                    var termId = $j(this).attr("id");

                                    termId = termId.replace("rejectVote", "");

                                    rejectTerm(termId);

                                });

                                detailsSec.find('.suggTxt').focus(function () {
                                    $j(this).val('');
                                    detailsSec.find("#new").attr("checked", "checked");
                                });
                                detailsSec.find('.suggTxt').blur(function () {
                                    if ($j(this).val() == '')
                                        $j(this).val('Enter your own translation');
                                });
                                detailsSec.find('.gotToNxt').click(function () {
                                });
                                
                                detailsSec.find('.userComments').click(function () {
                                    var termId = row.attr('termId');
                                    if ($j('#votiongDetailsTab').length > 0) {
                                    	var count = 0;
                                        var oTable = $j('#votiongDetailsTab').dataTable({
                                            "bProcessing": true,
                                            "sServerMethod": "GET",
                                            "bAutoWidth": false,
                                            "bPaginate": true,
                                            "bDestroy": true,
                                            "sAjaxDataProp": "commentData",
                                            "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getUserComment/" + termId,
                                            "aoColumns": [
                                                { "mDataProp": "userName", "sWidth": "20%"},
                                                { "mDataProp": "userComment", "sWidth": "25%",
                                                	"fnRender": function (oObj) {
                                                		 txt=oObj.aData[oObj.oSettings.aoColumns[oObj.iDataColumn].mDataProp];
                                                		if(txt != null){
                                                		if(txt.length>100){
                                                			popData=txt;
                                                			var tempHtml = "<div  class='subdata' id='subdata_"+count+"'>"+txt.substring(0,50)+" "+txt.substring(50,100) +"..."+"<span style='display: none;' class='tip-text'>"+txt+"</span></div>";
                                                			count++;
                                                			return tempHtml;
                                                		}
                                                		else {
                                                			return "<div>"+txt +"</div>";
                                                		}
                                                	}
                                                    }
                                             },
                                                { "mDataProp": "commentDate", "sWidth": "15%"}
                                            ],
                                            "iDisplayLength": 10
                                        });
                                        
                                    }
                                    $j('.dataTables_length').hide();
                                    $j('#votiongDetailsTab_filter').hide();
                                    showVotingDetails(data);
                                   
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
                }
                else {
                	$j(this).css('margin-top','0px');
                    $(this).parent().parent().next().next().hide();
                    $(this).prev().prev().prev().removeClass('blueClrFont').addClass('fontStyle');
                    $(this).removeClass("twistie_open_user");
                    $(this).addClass("twistie_close_user");
                    $j(this).find('.fa-caret-down').css("display","none");
                    $(this).find('.editDate').hide();
                    $(this).find('.expiryDate').show();
                }


            });


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

        ctx.bind("showUserLanguageTerms", function (event, criteria) {
            var pageNum = criteria.pageNum;
            $j('.showTermsVoted').find('span').remove();
            $t.getUserVotedTermDetails(langId, null, 'ASC', 0, {
          		 success: function (data) {
          			 if(data == null){
        				 totalVotes = 0;
        			 }else{
        				 totalVotes = data.length;
        			 }
          			$j('.showTermsVoted').find('span').remove();
          		 $j('.showTermsVoted').append("<span class='totalVotes'> ( " + totalVotes  + " ) </span> ") ;
          		 },
          		 error: function (xhr, textStatus, errorThrown) {
                       console.log(xhr.responseText);
                       if (Boolean(xhr.responseText.message)) {
                           console.log(xhr.responseText.message);
                       }
                   }
          	});
            $t.getUserPollTerms(criteria.langId, criteria.colName, criteria.sortOrder, criteria.pageNum, {
                success: function (data) {
                    if (data == null) {
                        $j("#termDtlRowsList").html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">No invitations for this language  </span>');
                        $j("#pagination").hide();
                        totalTerms_user = 0;
                        $j("#totalLangTerms").html(totalTerms_user);
                    } else {
                        var termDetails = data;
                        var startLimit = 0;
                        var endLimit = 0;

                        if (pageNum == 0) {
                            totalTerms_user = termDetails.length;
                            noOfPages_user = Math.round(termDetails.length / 10);
                            noOfPages_user = (termDetails.length % 10 < 5 && termDetails.length % 10 > 0) ? noOfPages_user + 1 : noOfPages_user;
                            startLimit = 1;
                            endLimit = (termListLimit > totalTerms_user) ? totalTerms_user : termListLimit;
                        } else {
                            startLimit = ((pageNum - 1) * termListLimit) + 1;
                            var tempLimit = (pageNum) * termListLimit;
                            endLimit = (parseInt(tempLimit) > parseInt(totalTerms_user)) ? totalTerms_user : tempLimit;
                        }
                        var length = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;
                        displayTermDetails(data);
                        $j("#pagination").show();

                        $j("#rangeOfList").html(startLimit + "-" + endLimit);
                        var totalTerms = insertCommmas(new String(totalTerms_user));
                        $j('#totalCount_'+langId).html(totalTerms);
                        $j("#totalTerms").html(totalTerms);
                        $j("#totalLangTerms").html(totalTerms);
                        
                        if(data.length <= 10 && pagenationHideCount == 0) {
                        	$j("#pagination").hide();
                        } else {
                        	$j("#pagination").show();
                        	pagination(noOfPages_user, pageNum);
                        }
                       // pagination(noOfPages_user, pageNum);

                        $j('div.pollTerm').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 27) {
                                var finalText = origText.substring(0, 25);
                                finalText = finalText + "...";
                                $j(this).text(finalText);
                                $j(this).attr("title", origText);
                            }
                        });


                        bindEvents();
                        if ($j("body").hasClass('userTermsList')) {
                            $j(".trmLanguage").each(function () {
                                $j(this).hide();
                            });
                        }
                        if ($j("body").hasClass('adminOvr')) {
                            $j(".trmCategory").each(function () {
                                $j(this).hide();
                            });
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


        var bindVotedEvents = function (data) {
            var classNamesClone = classNames.slice(0);
            $j('#votedRowsList .row ').click(function () {
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
                                var termAttrIds = {

                                    posId: 'posId_' + termId,
                                    formId: 'formId_' + termId,
                                    categoryId: 'categoryId_' + termId,
                                    notesId: 'notesId_' + termId,
                                    defId: 'defId_' + termId,
                                    usageId: 'usageId_' + termId

                                }

                                var editTermTmpl = new Template(votedTermAttrTmpl).evaluate(termAttrIds);

                                detailsSec.html(editTermTmpl);


                                if (termInfo != null) {
                                    var termUsage = (termInfo.termUsage == null) ? "" : termInfo.termUsage;
                                    var conceptDefinition = (termInfo.conceptDefinition == null) ? "" : termInfo.conceptDefinition;
                                    var termNotes = (termInfo.termNotes == null) ? "" : termInfo.termNotes;
                                    var categoryName = (termInfo.termCategory == null || termInfo.termCategory.category == null) ? "" : termInfo.termCategory.category;
                                    var formName = (termInfo.termForm == null || termInfo.termForm.formName == null) ? "" : termInfo.termForm.formName;
                                    var partOfSpeech = (termInfo.termPOS == null || termInfo.termPOS.partOfSpeech == null) ? "" : termInfo.termPOS.partOfSpeech;
                                    var termImage = (termInfo.photoPath == null) ? defaultImg : termInfo.photoPath;
                                    if (termInfo.termUsage == null || termInfo.termUsage == "") {
                                        detailsSec.find("#editTermImage").css("margin-top", "0px");
                                    } else {
                                        detailsSec.find("#editTermImage").css("margin-top", "0px");
                                    }
                                    detailsSec.html(votedTermAttrTmpl);

                                    if ($j.browser.version == "7.0") {
                                        $j(".termAttr").css("min-height", "135px");
                                    }
                                    detailsSec.find('.usageDesc').html(termUsage);
                                    detailsSec.find('.definitionDesc').html(conceptDefinition);
                                    detailsSec.find('.domainDesc').html(termNotes);
                                    detailsSec.find('.programDesc').html(categoryName);
                                    detailsSec.find('.formDesc').html(formName);
                                    detailsSec.find('.termTypeDesc').html(partOfSpeech);
                                    detailsSec.find('.termTypeDesc').attr("id", data.TypeId);
                                    detailsSec.find('.moreAttr').attr("termId", row.attr('termId'));
                                    detailsSec.find("#termImgId").attr('src', termImage);

                                    var origNotes = detailsSec.find('.domainDesc').html();
                                    if (origNotes.length > 40) {
                                        var finalText = origNotes.substring(0, 36);
                                        finalText = finalText + "...";
                                        detailsSec.find('.domainDesc').text(finalText);
                                        detailsSec.find('.domainDesc').attr("title", origNotes);
                                    }
                                    /*var origDef = detailsSec.find('.definitionDesc').html();
                                    if (origDef.length > 50) {
                                        var finalText = origDef.substring(0, 48);
                                        finalText = finalText + "...";
                                        detailsSec.find('.definitionDesc').text(finalText);
                                        detailsSec.find('.definitionDesc').attr("title", origDef);
                                    }*/
                                   /* detailsSec.find('.definitionDesc').text(finalText);
                                    var origExmpl = detailsSec.find('.usageDesc').html();
                                    if (origExmpl.length > 50) {
                                        var finalText = origExmpl.substring(0, 48);
                                        finalText = finalText + "...";
                                        detailsSec.find('.usageDesc').text(finalText);
                                        detailsSec.find('.usageDesc').attr("title", origExmpl);
                                    }*/
                                }

                                if (data.suggestedTermDetails != null && data.suggestedTermDetails.length > 0) {
                                	
                                	//detailsSec.append('<a id="userCmnt" href="javascript:;" class="userComments bold">View User Comments</a>');
                                    for (var i = 0; i < data.suggestedTermDetails.length; i++) {
                                        var suggTrmListTmplClone = votedSuggTrmListTmpl;
                                        //For showing Final term in green color
                                        var suggTrmListForFinalTmplClone = votedSuggFinalTrmListTmpl;
                                       
                                        if (data.suggestedTermDetails[i].isUpdated == 'Y' && data.suggestedTermDetails[i].noOfVotes > 0) {
//											suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm+"("+Constants.UPDATED_TERM_MSG+")";
                                            suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
                                            suggTrmListTmplClone[3] = "(" + Constants.UPDATED_TERM_MSG + ")";
                                            suggTrmListForFinalTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
                                            suggTrmListForFinalTmplClone[3] = "(" + Constants.UPDATED_TERM_MSG + ")";

                                        } else {
                                            suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
                                            suggTrmListTmplClone[3] = "";
                                            suggTrmListForFinalTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
                                            suggTrmListForFinalTmplClone[3] = "";
                                        }
                                        suggTrmListTmplClone[5] = data.suggestedTermDetails[i].suggestedTermId;
                                        suggTrmListTmplClone[7] = row.attr('termId');
                                        suggTrmListTmplClone[9] = data.suggestedTermDetails[i].suggestedTerm;
                                        suggTrmListTmplClone[13] = data.suggestedTermDetails[i].noOfVotes;
                                        //suggTrmListTmplClone[15] = totalVotes;
                                        console.log("bindevents "+totalVotes);
                                        suggTrmListForFinalTmplClone[5] = data.suggestedTermDetails[i].suggestedTermId;
                                        suggTrmListForFinalTmplClone[7] = row.attr('termId');
                                        suggTrmListForFinalTmplClone[9] = data.suggestedTermDetails[i].suggestedTerm;
                                        suggTrmListForFinalTmplClone[13] = data.suggestedTermDetails[i].noOfVotes;
                                        
                                        if((data.suggestedTermDetails[i].suggestedTerm == termInfo.suggestedTerm) && termInfo.termStatusId == 2) {
                                        	detailsSec.append(suggTrmListForFinalTmplClone.join(""));
                                        } else {
                                        	detailsSec.append(suggTrmListTmplClone.join(""));
                                        }
                                    }
                                }
                                $j(".voteHeader").hide();
                                detailsSec.find('input:radio').hide();
                                
                              //  detailsSec.append('<div style="color: #aeaeae; font-size:12px;font-weight:bold;" class="noOfTerms">No.of Submitted Terms : '+totalVotes+'</div>');
                                var termId = row.attr('termId');
                                
                                $t.getUserCommets(termId, {
                                    success: function (data) {
                                        if (data != null && data.commentData != null && data.commentData != '') {
                                        	  detailsSec.append('<div  id="votiongDetailsDiv" title="User Comments">'
                                          		    + '<table cellpadding="0" cellspacing="0" border="0" class="display" id="votiongDetailsTab" width="100%" style="table-layout:fixed;">'
                                          			+ '<thead> <th width="20%" style="height:40px;color: #5a5a5a;font-size: 14px;">Username</th> <th width="25%" style="height:40px;color: #5a5a5a;font-size: 14px;">Comments</th> <th width="15%" style="height:40px;color: #5a5a5a;font-size: 14px;">Comment Date</th> </thead> <tbody style="font-size:12px;">'	
                                          			+ '</tbody></table> </div>');
                                        	var count = 0;
                                       	 detailsSec.find('#votiongDetailsTab').dataTable({
                                               "bProcessing": true,
                                               "sServerMethod": "GET",
                                               "bAutoWidth": false,
                                               "bPaginate": true,
                                               "bDestroy": true,
                                               "sAjaxDataProp": "commentData",
                                               "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getUserComment/" + termId,
                                               "aoColumns": [
                                                   { "mDataProp": "userName", "sWidth": "20%"},
                                                   { "mDataProp": "userComment", "sWidth": "25%",
                                                	   "fnRender": function (oObj) {
                                                		   txt=oObj.aData[oObj.oSettings.aoColumns[oObj.iDataColumn].mDataProp];
                                                    	   if(txt != null){
                                                      			var tempHtml = "<span style='word-wrap:break-word;'>"+txt+"</span>";
                                                      			return tempHtml;
                                                      		} 
                                                       }
                                                },
                                                   { "mDataProp": "commentDate", "sWidth": "15%"}
                                               ],
                                               "iDisplayLength": 10
                                           });
                                       	 detailsSec.find('#votiongDetailsTab').find('sorting_asc').add('sorting');
                                       	if(data.commentData.length < 10) {
                                       	 detailsSec.find("#votiongDetailsTab_info").hide();
                                       	 detailsSec.find("#votiongDetailsTab_paginate").hide();
                                        } else {
                                        	detailsSec.find("#votiongDetailsTab_info").show();
                                        }
                                        detailsSec.find("#votiongDetailsTab_filter").hide();
                                        detailsSec.find("#votiongDetailsTab_length").hide();
                                        /*detailsSec.find(".sorting").hide();
                                        detailsSec.find(".sorting_asc").hide();*/
                                        }
                                        else {
                                        	detailsSec.append('<div class="noComments">No comments have been submitted for this term.</div>');
                                        }
                                    },
                                    error: function (xhr, textStatus, errorThrown) {
                                        console.log(xhr.responseText);
                                        if (Boolean(xhr.responseText.message)) {
                                            console.log(xhr.responseText.message);
                                        }
                                    }
                                });
                                /*if ($j('#votiongDetailsTab').length > 0) {
                                	var count = 0;
                                    var oTable = $j('#votiongDetailsTab').dataTable({
                                        "bProcessing": true,
                                        "sServerMethod": "GET",
                                        "bAutoWidth": false,
                                        "bPaginate": true,
                                        "bDestroy": true,
                                        "sAjaxDataProp": "commentData",
                                        "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getUserComment/" + termId,
                                        "aoColumns": [
                                            { "mDataProp": "userName", "sWidth": "20%"},
                                            { "mDataProp": "userComment", "sWidth": "25%",
                                            	"fnRender": function (oObj) {
                                            		 txt=oObj.aData[oObj.oSettings.aoColumns[oObj.iDataColumn].mDataProp];
                                            		if(txt != null){
                                            		if(txt.length>100){
                                            			popData=txt;
                                            			var tempHtml = "<div  class='subdata' id='subdata_"+count+"'>"+txt.substring(0,50)+" "+txt.substring(50,100) +"..."+"<span style='display: none;' class='tip-text'>"+txt+"</span></div>";
                                            			count++;
                                            			return tempHtml;
                                            		}
                                            		else {
                                            			return "<div>"+txt +"</div>";
                                            		}
                                            	}
                                                }
                                         },
                                            { "mDataProp": "commentDate", "sWidth": "15%"}
                                        ],
                                        "iDisplayLength": 10
                                    });
                                    
                                }*/
                                $j('.dataTables_length').hide();
                                $j('#votiongDetailsTab_filter').hide();
                                //showVotingDetails(data);
                                
                                /*Finding highest no. of votes*/
                                var largest = {
                                    val: null
                                };
                                for (var i in data.suggestedTermDetails) {
                                    if (data.suggestedTermDetails[i].noOfVotes > largest.val) {
                                        largest.val = data.suggestedTermDetails[i].noOfVotes;

                                    }
                                }
                                $j("#termImgId").hover(function () {
                                    modalRender.bubble("#termImgId", "click for full image", "left center", "right center");
                                });

                                $j("#termImgId").click(function () {
                                    $t.getTermAttributes(termId, {
                                        success: function (imgData) {
                                            if (data != null && data.termInfo != null) {
                                                var hoverImagepath = (imgData.termInfo.photoPath == null) ? defaultImg : imgData.termInfo.photoPath;
                                                showTermPicture();
                                                $j("#showTermImage").find("img").attr('src', hoverImagepath);
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

                                    $j(this).width(blockSize * data.suggestedTermDetails[i].noOfVotes);

                                    /*Displaying qTip on mouseover*/
                                    $j(this).hover(function () {
                                        modalRender.bubble("#barId_" + row.attr('termId') + i, data.suggestedTermDetails[i].votersNames, "left center", "right center");
                                    });

                                });

                                var votedTerm = data.votedTerm;

                                detailsSec.find('.termSuggestion').each(function (i) {
                                    if ($j(this).html() == votedTerm) {
                                        $j(this).css("font-weight", "bold");
                                    }
                                });

                                detailsSec.find('.userComments').click(function () {
                                    var termId = row.attr('termId');
                                    if ($j('#votiongDetailsTab').length > 0) {
                                    	var count = 0;
                                        var oTable = $j('#votiongDetailsTab').dataTable({
                                            "bProcessing": true,
                                            "sServerMethod": "GET",
                                            "bAutoWidth": false,
                                            "bPaginate": true,
                                            "bDestroy": true,
                                            "sAjaxDataProp": "commentData",
                                            "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getUserComment/" + termId,
                                            "aoColumns": [
                                                { "mDataProp": "userName", "sWidth": "20%"},
                                                { "mDataProp": "userComment", "sWidth": "25%",
                                                	"fnRender": function (oObj) {
                                                		 txt=oObj.aData[oObj.oSettings.aoColumns[oObj.iDataColumn].mDataProp];
                                                		if(txt != null){
                                                		if(txt.length>100){
                                                			popData=txt;
                                                			var tempHtml = "<div  class='subdata' id='subdata_"+count+"'>"+txt.substring(0,50)+" "+txt.substring(50,100) +"..."+"<span style='display: none;' class='tip-text'>"+txt+"</span></div>";
                                                			count++;
                                                			return tempHtml;
                                                		}
                                                		else {
                                                			return "<div>"+txt +"</div>";
                                                		}
                                                	}
                                                    }
                                             },
                                                { "mDataProp": "commentDate", "sWidth": "15%"}
                                            ],
                                            "iDisplayLength": 10
                                        });
                                        
                                    }
                                    $j('.dataTables_length').hide();
                                    $j('#votiongDetailsTab_filter').hide();
                                    showVotingDetails(data);
                                   
                                });
                                
                                $j(".moreAttr").click(function () {

                                    var divTrmId = $j(this).attr("termId");
                                    $j(".row").each(function (index) {
                                        if ($j(this).attr("termId") == divTrmId) {
                                            $j(".srcTrm").html($j(this).find(".pollTerm").text());
                                            $j(".translateTrm").html($j(this).find(".targetTrm").text());
                                            $j(".srcTrmFrm").html($j(this).parent().parent().find(".formDesc").text());

                                        }
                                    });

                                    showDialog();

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
                    $(this).parent().next().next().hide();
                    $(this).removeClass("twistie_open");
                    $(this).addClass("twistie_close");
                    $(this).find('.editDate').hide();
                    $(this).find('.expiryDate').show();
                }
            });


        };
        ctx.bind("showVotedTermDetails", function (event, criteria) {
            var pageNum = criteria.pageNum;
            $t.getUserVotedTermDetails(criteria.langId, criteria.colName, criteria.sortOrder, criteria.pageNum, {
                success: function (data) {
                    if (data == null) {
                        $j("#votedRowsList").html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">No voted terms for this language  </span>');
                        $j(".currentPagination").hide();
                        totalTerms_user = 0;
                        $j("#totalLangTerms").html(totalTerms_user);
                    } else {

                        var termDetails = data;
                        var startLimit = 0;
                        var endLimit = 0;
                        totalVotes = data.length;
                        if (pageNum == 0) {
                            totalTerms_user = termDetails.length;
                            noOfPages_user = Math.round(termDetails.length / 10);
                            noOfPages_user = (termDetails.length % 10 < 5 && termDetails.length % 10 > 0) ? noOfPages_user + 1 : noOfPages_user;
                            startLimit = 1;
                            endLimit = (termListLimit > totalTerms_user) ? totalTerms_user : termListLimit;
                        } else {
                            startLimit = ((pageNum - 1) * termListLimit) + 1;
                            var tempLimit = (pageNum) * termListLimit;
                            endLimit = (parseInt(tempLimit) > parseInt(totalTerms_user)) ? totalTerms_user : tempLimit;
                        }

                        displayVotedTermDetails(data);
                        /* if(data.length > 10) {
                        	$j(".currentPagination").show();
                            $j("#vtdRangeOfList").html(startLimit + "-" + endLimit);
                            var votedTotalTerms = insertCommmas(new String(totalTerms_user));
                            pagination(noOfPages_user, pageNum);
                        } else {
                        	$j("#vtdRangeOfList").hide();
                        	$j(".previous").hide();
                        	$j(".next").hide();
                        }
                        
                        $j("#vtdTotalTerms").html(votedTotalTerms);
                        $j("#totalLangTerms").html(votedTotalTerms); */
                        $j(".currentPagination").show();
                        $j("#vtdRangeOfList").html(startLimit + "-" + endLimit);
                        var votedTotalTerms = insertCommmas(new String(totalTerms_user));
                        $j("#vtdTotalTerms").html(votedTotalTerms);
                        $j("#totalLangTerms").html(votedTotalTerms);
                        if(data.length <= 10 && pagenationHideCount == 0) {
                        	$j("#pagination").hide();
                        } else {
                        	$j("#pagination").show();
                        	pagination(noOfPages_user, pageNum);
                        }
                        //pagination(noOfPages_user, pageNum);
                        var length = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;
                        for (var i = 0; i < length; i++) {
                            var dataXML = new Array();
                            var invites = (termDetails[i].invites == null) ? 0 : termDetails[i].invites;
                            var votesPerTerm = (termDetails[i].votesPerTerm == null) ? 0 : termDetails[i].votesPerTerm;
                            // Start of chart object
                            dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='14' showShadow='0' slicingDistance='0' animation='0' paletteColors='#C9CCCB, #00A4D6' showBorder='0' bgAlpha='30' showToolTip='0'>");
                            dataXML.push("<set label='Services' value='" + (invites - votesPerTerm) + "' />");
                            dataXML.push("<set label='Hardware' value='" + votesPerTerm + "' />");
                            dataXML.push("</chart>");
                            ChartRender.twoDPieChart(termDetails[i].termId, pieChartHeight, pieChartWidth, "dataPie_" + termDetails[i].termId, dataXML);
                            modalRender.bubble("#dataPie_" + termDetails[i].termId, votesPerTerm + "/" + invites + "<br>" + "voted / invited to vote", "bottom center", "top center");
                        }
                        console.log("termattr "+totalVotes);
                        bindVotedEvents();
                        $j('div.pollTerm').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 14) {
                                var finalText = origText.substring(0, 12);
                                finalText = finalText + "...";
                                $j(this).text(finalText);
                                $j(this).attr("title", origText);
                            }


                        });
                        $j('div.targetTrm').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 18) {
                                var finalText = origText.substring(0, 16);
                                finalText = finalText + "...";
                                $j(this).text(finalText);
                                $j(this).attr("title", origText);
                            }
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
            $(function () {
                $("#datepicker").datepicker({
                    showOn: "button",
                    buttonImage: "images/calendar.gif",
                    buttonImageOnly: true
                });
            });
        });

    };

    var voteTerm = function (suggestedTermId, termId, suggestedTxt) {
        var termTranslation = new Object();
        var suggested_term_id = null;
        var suggestedTerm = null;
        var suggestedLangId = null;
        var comment = null;
        if (suggestedTermId == "new") {
            suggested_term_id = null;
            suggestedTerm = suggestedTxt;
            suggestedLangId = $j("#langDetails .on").attr("id").replace("langId_", "");
        } else {
            suggested_term_id = suggestedTermId;
            suggestedTerm = null;
        }

        termTranslation.termTranslationId = suggested_term_id;
        termTranslation.termId = termId;
        termTranslation.suggestedTerm = suggestedTerm;
        termTranslation.suggestedTermLangId = suggestedLangId;
        termTranslation.vote = null;
        termTranslation.userId = null;
        termTranslation.createDate = null;
        termTranslation.updatedBy = null;
        termTranslation.updateDate = null;
        termTranslation.comment = ($j.trim($j(".commentTxt").val()) == "")? null : $j(".commentTxt").val();
        termTranslation.isActive = null;

        
        
        
        
        
        var termTranslationParameter = Object.toJSON(termTranslation);
        $t.voteTerm(termTranslationParameter, {
            success: function (data) {
              	 if(data == 'New Rank') {
              		 //$j('.newRank').show();
              		alertMessageForPoll("#pollExpireAlert");
              	 }
            	displayDetails();
            	$t.getUserPollTerms(langId,null, null, 0, {
                	 success: function (data) {
                		 if(data != null){
                			 $j('#totalCount_'+langId).html(data.length);
                		 }else{
                			 $j('#totalCount_'+langId).html("0"); 
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
    var alertMessageForPoll = function (alertId) {
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

	var displayDetails = function() {
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
	}
    var rejectTerm = function (termId) {
        if (termId != null) {

            $t.rejectTerm(termId, {
                success: function (data) {

                    triggerTermDetails(langId, null, null, 0);

                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });
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

    /**
    var userLangTmpl = ['<span class="languages" id="langId_',
        '',
        '"  >',
        '',
        '</span>'
    ];
     */
    var userLangTmpl = ['<li class="ui-state-default ui-corner-top ',
        '',
        '" id="langId_',
        '',
        '" langLabel="',
        '',
        '"><a>',
        '',
        '&nbsp;(<span style=" border-right: medium none;padding:0px;" id="totalCount_',
        '',
        '"></span>)</a></li>'
    ];

    var displayUserLanguages = function (data) {
        var userLangList = data;
        $j("#languageLabel").text(userLangList[0].languageLabel);
        langId = userLangList[0].languageId;
        for (var count = 0; count < userLangList.length; count++) {
            var userLangTmplClone = userLangTmpl;
            userLangTmplClone[1] = "tab" + (count + 1);
            userLangTmplClone[3] = userLangList[count].languageId;
            userLangTmplClone[5] = userLangList[count].languageLabel;
            userLangTmplClone[7] = userLangList[count].languageLabel;
            userLangTmplClone[9] = userLangList[count].languageId;
            $j('#tabs ul').append(userLangTmplClone.join(""));
        }  
        $j(".langMenu li").each(function (index) {
        	var langId = $j(this).attr("id");
             langId = langId.replace("langId_", "");
             $t.getUserPollTerms(langId,null, null, 0, {
            	 success: function (data) {
            		 if(data != null){
            			 $j('#totalCount_'+langId).html(data.length);
            		 }else{
            			 $j('#totalCount_'+langId).html("0"); 
            		 }
            	 }
             });
            
        });
        
        currPage = "currentPolls";
        triggerTermDetails(langId, null, null, 0);
    };

    var partOfSpeechSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayPosList = function (data) {
        var posList = data;
        for (var count = 0; count < posList.length; count++) {
            var partOfSpeechSlctTmplClone = partOfSpeechSlctTmpl;
            partOfSpeechSlctTmplClone[1] = posList[count].partsOfSpeechId;
            partOfSpeechSlctTmplClone[3] = posList[count].partOfSpeech;
            $j('.termPOS').append(partOfSpeechSlctTmplClone.join(""));
        }
    };

    var termFormSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayTermFormList = function (data) {
        var trmFormList = data;
        for (var count = 0; count < trmFormList.length; count++) {
            var termFormSlctTmplClone = termFormSlctTmpl;
            termFormSlctTmplClone[1] = trmFormList[count].formId;
            termFormSlctTmplClone[3] = trmFormList[count].formName;
            $j('.termForm').append(termFormSlctTmplClone.join(""));
        }
    };

    var programSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayProgramList = function (data) {
        var programList = data;
        for (var count = 0; count < programList.length; count++) {
            var programSlctTmplClone = programSlctTmpl;
            programSlctTmplClone[1] = programList[count].programId;
            programSlctTmplClone[3] = programList[count].programName;
            $j('.program').append(programSlctTmplClone.join(""));
        }
    };

    var productGroupSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayProductGroupList = function (data) {
        var productList = data;
        for (var count = 0; count < productList.length; count++) {
            var productGroupSlctTmplClone = productGroupSlctTmpl;
            productGroupSlctTmplClone[1] = productList[count].productId;
            productGroupSlctTmplClone[3] = productList[count].product;
            $j('.productGroup').append(productGroupSlctTmplClone.join(""));
        }
    };

    var statusSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayStatusList = function (data) {
        var statusList = data;
        for (var count = 0; count < statusList.length; count++) {
            var statusSlctTmplClone = statusSlctTmpl;
            statusSlctTmplClone[1] = statusList[count].statusId;
            statusSlctTmplClone[3] = statusList[count].status;
            $j('.status').append(statusSlctTmplClone.join(""));
        }
    };

    var termCatSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayTermCatList = function (data) {
        var termCatList = data;
        for (var count = 0; count < termCatList.length; count++) {
            var termCatSlctTmplClone = termCatSlctTmpl;
            termCatSlctTmplClone[1] = termCatList[count].categoryId;
            termCatSlctTmplClone[3] = termCatList[count].category;
            $j('.termCategory').append(termCatSlctTmplClone.join(""));
        }
    };

    var conceptCatSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayConceptCatList = function (data) {
        var conceptCatList = data;
        for (var count = 0; count < conceptCatList.length; count++) {
            var conceptCatSlctTmplClone = conceptCatSlctTmpl;
            conceptCatSlctTmplClone[1] = conceptCatList[count].conceptCatId;
            conceptCatSlctTmplClone[3] = conceptCatList[count].conceptCategory;
            $j('.conceptCategory').append(conceptCatSlctTmplClone.join(""));
        }
    };

    var langSlctTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayLanguages = function (data, divClass) {
        var languageList = data;
       /* var AllLang =  {
        		'languageId' : 'All',
        		'languageLabel' : 'All',
        		'languageCode'   : 'All'
        		
        };
        languageList.unshift(AllLang);*/
        var langLength = languageList.length;
        
        for (var count = 0; count < langLength; count++) {
            var langSlctTmplClone = langSlctTmpl;
            langSlctTmplClone[1] = languageList[count].languageId;
            langSlctTmplClone[3] = languageList[count].languageLabel;
            $j(divClass).append(langSlctTmplClone.join(""));
        }
        var $tabs = $j("#tabs").tabs();
        $tabs.tabs('select', 0);
    };

    var showDialog = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $("#allUserList:ui-dialog").dialog("destroy");

        $("#allUserList").dialog({
            height: 500,
            width: 500,
            modal: true
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

    var getSlctdLang = function () {
        $j(".langMenu li").first().addClass("ui-tabs-selected ui-state-active on");
        /*var defaultLangText = $j(".langMenu li").first().attr('langLabel');
        $j("#languageText").text(defaultLangText);*/
        $j(".langMenu li").last().css("border-right", "none");
        $j("#langs span").css("padding-left", "8px");
        $j("#langs span").first().addClass("on");
        $j("#langs span").first().removeClass("link");
        $j(".langMenu li").click(function () {
        	$j(".sort1").css('display','none');
        	$('div #sourceTerm > img').remove();
        	totalVotes = 0;
        	langId = $j(this).attr("id");
            langId = langId.replace("langId_", "");
            $t.getUserPollTerms(langId,null, null, 0, {
            	 success: function (data) {
            		 if(data != null){
            			 $j('#totalCount_'+langId).html(data.length);
            		 }else{
            			 $j('#totalCount_'+langId).html("0"); 
            		 }
            	 }
             });
        /*defaultLangText = $j(this).attr('langLabel');
        $j("#languageText").text(defaultLangText);*/
            $j('.votedSourceTerm').attr('sortOrder', 'ASC');
            $j('.votedSuggestedTerm').attr('sortOrder', 'ASC');
            $j('.termVotingDate').attr('sortOrder', 'ASC');
            $j('.currentSourceTerm').attr('sortOrder', 'ASC');
            $j('.currentExpirationDate').attr('sortOrder', 'ASC');
            $j(".votedTrms").show();
            $j(".currentPolls").hide();
            $j('#votedTermDetails').hide();
            $j("#newInvitations").show();
            $j("#trmDtlSectionHead").find('.sort1').show();
            $j("#trmDtlSectionHead").find('.sort2').show();
            
            $j("#trmDtlSectionHead div").each(function (index) {
               // $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
            });
            if (!$j(this).hasClass("on")) {
                $j(".langMenu li").each(function (index) {
                    $j(this).removeClass("ui-tabs-selected ui-state-active on");
                });

                $j(this).addClass("ui-tabs-selected ui-state-active on");
				
                langId = $j(this).attr("id");
                langId = langId.replace("langId_", "");
                $j("#languageLabel").text($j(this).text());
                currPage = "currentPolls";
                triggerTermDetails(langId, null, null, 0);
            }
        });
    };
    $j(".langMenu li").first().addClass("on");
    $j(".langMenu li").last().remove();


    var triggerTermDetails = function (langId, colName, sortOrder, pageNum) {
        $j('#termDtlRowsList').empty();
        var criteria = {
            'langId': langId,
            'colName': colName,
            'sortOrder': sortOrder,
            'pageNum': pageNum
        };
        savedCriteria = criteria;
        var noOfPages = $j("#termDetails").trigger("showUserLanguageTerms", criteria);

    };
    var triggerVotedTermDetails = function (langId, colName, sortOrder, pageNum) {
        $j("#votedRowsList").html(" ");
        $j("#votedRowsList").empty();

        ChartRender.destroyTwoDPieChart();
        var criteria = {
            'langId': langId,
            'colName': colName,
            'sortOrder': sortOrder,
            'pageNum': pageNum
        };
        savedCriteria = criteria;
        $j("#votedTermDetails").trigger("showVotedTermDetails", criteria);

        chatData();

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


    var showNewTermForm = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $("#newTermDiv:ui-dialog").dialog("destroy");
        $("#newTermDiv").dialog({
            height: 400,
            width: 750,
            modal: true,
            close: function (event, ui) {
//				$j('#trgtLang').css("border","1px solid  #BBBBBB");
                validateTermDetails.resetForm();

            }

        });
    };

    var closeLoadingDialog = function (dialogId, id) {
        $j(dialogId).html("");
        $j(dialogId).dialog('destroy');
        $j(id).dialog('destroy');
        mailSent();
    };
    $j(".next").click(function () {
    	pagenationHideCount = pagenationHideCount  + 1;
        if ($j(".next").hasClass("nextEnable")) {
            var langId = savedCriteria.langId;
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var pageNum = (savedCriteria.pageNum > 0) ? (savedCriteria.pageNum + 1) : (savedCriteria.pageNum + 2);
            savedCriteria.pageNum = pageNum;
            if (currPage == "currentPolls") {
                triggerTermDetails(langId, colName, sortOrder, pageNum);
            } else if (currPage == "votedTrms") {
                triggerVotedTermDetails(langId, colName, sortOrder, pageNum);
            }
        }
    });

    $j(".previous").click(function () {
    	pagenationHideCount = pagenationHideCount  + 1;
    	$j("#pagination").show();
        if ($j(".previous").hasClass("prevEnable")) {
            var langId = savedCriteria.langId;
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var pageNum = savedCriteria.pageNum - 1;
            savedCriteria.pageNum = pageNum;
            if (currPage == "currentPolls") {
                triggerTermDetails(langId, colName, sortOrder, pageNum);
            } else if (currPage == "votedTrms") {
                triggerVotedTermDetails(langId, colName, sortOrder, pageNum);

            }
        }

    });
    $j(".currentPolls").click(function () {
        $j(".votedTrms").show();
        $j(".currentPolls").hide();
        $j("#termDtlRowsList").html("");
        $j("#votedRowsList").html(" ");
        $j("#votedTerms").hide();
        $j("#newInvitations").show();
        $j("#trmDtlSectionHead").find('.sort').hide();
        $j("#trmDtlSectionHead").find('.sort1').show();
        var languageId;
        languageId = $j("#langDetails .on").attr("id");
        languageId = languageId.replace("langId_", "");
        currPage = "currentPolls";
        triggerTermDetails(languageId, null, null, 0);
    });

    $j(".votedTrms").click(function () {
    	$j('.showTermsVoted').find('span').remove();
        $j(".votedTrms").hide();
        $j(".currentPolls").show();
        $j("#votedTerms").show();
        $j("#votedRowsList").html(" ");
        $j("#votedRowsList").empty();
        $j('#votedTermDetails').show();
        $j("#termDtlRowsList").html("");
        $j("#newInvitations").hide();
        $j("#votedTermDetails").find('.votedSourceTerm').find('.sort').hide();
        $j("#votedTermDetails").find('.votedSourceTerm').find('.sort2').show();
        var languageId;
        languageId = $j("#langDetails .on").attr("id");
        if (languageId != null && languageId != undefined)
            languageId = languageId.replace("langId_", "");
        currPage = "votedTrms";
        triggerVotedTermDetails(languageId, null, null, 0);

    });

    $j("#cancelMail").click(function () {
        $j('#newTermDiv').dialog('destroy');
    });

    validateTermDetails = $j("#rqstNewTrm").validate({
        debug: true,
        rules: {
            termInfo: "required"
        },
        messages: {
            termInfo: "English term is required"
        }
    });


    $j("#newTermMail").click(function () {
        if ($j("#rqstNewTrm").valid()) {
            if (values == null || values == 0) {
                $j(".languageSlctErr").show();
                $j("#rqstNewTerm .ui-state-default").css("border", "1px solid red");
                return;
            } else {
                $j("#rqstNewTerm  .ui-state-default").css("border", "1px solid #DDDDDD");
                $j(".languageSlctErr").hide();
            }
            $j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif"  align="top" class="rightmargin10" />Sending mail... Please wait</div>');
            showLoadingDialog();
            $j(".ui-dialog-titlebar").hide();
            setTimeout(function () {
                closeLoadingDialog('#loading', '#newTermDiv');
            }, 900);
            var date = new Date();
            var curr_date = date.getDate();
            var curr_month = date.getMonth();
            curr_month = curr_month + 1;
            var curr_year = date.getFullYear();
            date = curr_date + '/' + curr_month + '/' + curr_year;
            var termInformation = new Object();
            var picklistName = $j("#termForm :selected").text();
            var pickListId = $j("#termForm :selected").val();
            if (pickListId == 0) {
                picklistName = "";
            }
            var form = new Object();
            var form = {
                formId: pickListId,
                formName: picklistName,
                createdBy: null,
                createDate: null,
                updatedBy: null,
                updateDate: null,
                isActive: 'Y'
            }
            $j("#languageSlct :selected").each(function () {
                texts.push($(this).text());
            });


            var picklistName = $j("#termPOS :selected").text();
            var pickListId = $j("#termPOS :selected").val();
            if (pickListId == 0) {
                picklistName = "";
            }
            var partsOfSpeech = new Object();
            partsOfSpeech.partsOfSpeechId = pickListId;
            partsOfSpeech.partOfSpeech = picklistName;
            partsOfSpeech.createdBy = null;
            partsOfSpeech.createDate = null;
            partsOfSpeech.updatedBy = null;
            partsOfSpeech.updateDate = null;
            partsOfSpeech.isActive = 'Y';

            var picklistName = $j("#pgrmPjct :selected").text();
            var pickListId = $j("#pgrmPjct :selected").val();
            if (pickListId == 0) {
                picklistName = "";
            }
            var program = new Object();
            program.programId = pickListId;
            program.programName = picklistName;
            program.createdBy = null;
            program.createDate = null;
            program.updatedBy = null;
            program.updateDate = null;
            program.isActive = 'Y';

            var picklistName = $j("#termCategory :selected").text();
            var pickListId = $j("#termCategory :selected").val();
            if (pickListId == 0) {
                picklistName = "";
            }
            var category = new Object();
            category.categoryId = pickListId;
            category.category = picklistName;
            category.createdBy = null;
            category.createDate = null;
            category.updatedBy = null;
            category.updateDate = null;
            category.isActive = 'Y';

            var deprecatedTermInformation = new Object();
            var deprecatedTermInformationList = new Array();
//    	    var language = new Object();
//     	   language.languageId = $j("#trgtLang :selected").val();
//     	   language.languageLabel=null;
//    		   language.languageCode = null;
//    		   language.languageNotes = null;
//    		   language.createdBy = null;
//    		   language.createDate = null;
//    		   language.updatedBy = null;
//    		   language.updateDate = null;
//    		   language.isActive = null;
//    		   var languages =  Object.toJSON(language);
            /**
             if($j("#depSource1").val().length!=0 || $j("#depTarget1").val().length!=0)
             {

                    deprecatedTermInformation ={
                            termInfo: null,
                            deprecatedSource :($j("#depSource1").val()==""?null:$j("#depSource1").val()),
                            deprecatedTarget : ($j("#depTarget1").val()==""?null:$j("#depTarget1").val()),
                            deprecatedLangId : null,
                             createDate : null,
                             createdBy : null,
                             updatedBy : null,
                             updateDate : null,
                             isActive : 'Y'
                      }
                    deprecatedTermInformationList[0] = deprecatedTermInformation;
             }

             if($j("#depSource2").val().length!=0 || $j("#depTarget2").val().length!=0)
             {

                    deprecatedTermInformation ={
                            termInfo: null,
                            deprecatedSource : ($j("#depSource2").val()==""?null:$j("#depSource2").val()),
                            deprecatedTarget : ($j("#depTarget2").val()==""?null:$j("#depTarget2").val()),
                            deprecatedLangId :null,
                             createDate : null,
                             createdBy : null,
                             updatedBy : null,
                             updateDate : null,
                             isActive : 'Y'
                      }
                    deprecatedTermInformationList[1] = deprecatedTermInformation;
             }

             if($j("#depSource3").val().length!=0 || $j("#depTarget3").val().length!=0)
             {

                    deprecatedTermInformation ={
                            termInfo: null,
                            deprecatedSource : ($j("#depSource3").val()==""?null:$j("#depSource3").val()),
                            deprecatedTarget :($j("#depTarget3").val()==""?null:$j("#depTarget3").val()),
                            deprecatedLangId : null,
                             createDate : null,
                             createdBy : null,
                             updatedBy : null,
                             updateDate : null,
                             isActive : 'Y'
                      }
                    deprecatedTermInformationList[2] = deprecatedTermInformation;
             }
             */

            termInformation.termId = null;
            termInformation.termBeingPolled = $j("#termInfo").val();
            termInformation.termStatusId = ($j("#termStatus :selected").val() == 0) ? null : $j("#termStatus :selected").val();
            termInformation.termCategory = category;
            termInformation.termUsage = $j("#cncptExample").val();
            termInformation.termForm = form;
            termInformation.termPOS = partsOfSpeech;
            termInformation.termProgram = program;
            termInformation.termNotes = $j("#termNotes").val()
            termInformation.termLangId = 17;
            termInformation.conceptDefinition = $j("#cncptDef").val();
            termInformation.conceptNotes = $j("#cncptNotes").val();
            termInformation.conceptProdGroup = ($j("#productGroup :selected").val() == 0) ? null : $j("#productGroup :selected").val();
            termInformation.conceptCatId = ($j("#category :selected").val() == 0) ? null : $j("#category :selected").val();
            termInformation.suggestedTerm = $j("#trnsltn").val();
            termInformation.suggestedTermLangId = ($j("#trgtLang :selected").val() == 0) ? null : $j("#trgtLang :selected").val();
            termInformation.suggestedTermStatusId = ($j("#trgtStatus :selected").val() == 0) ? null : $j("#trgtStatus :selected").val();
            termInformation.suggestedTermFormId = ($j("#trgtForm :selected").val() == 0) ? null : $j("#trgtForm :selected").val();
            termInformation.suggestedTermPosId = ($j("#trgtPOS :selected").val() == 0) ? null : $j("#trgtPOS :selected").val();
            termInformation.suggestedTermUsage = $j("#trgtExmpl").val();
            termInformation.suggestedTermNotes = $j("#trgtNotes").val();
            termInformation.suggestedTermPgmId = ($j("#trgtPgrm :selected").val() == 0) ? null : $j("#trgtPgrm :selected").val();
            termInformation.createdBy = null;
            termInformation.createDate = null;
            termInformation.updatedBy = null;
            termInformation.updateDate = null;
            termInformation.isActive = null;
            // termInformation.comments=$j("#comment").val();
            termInformation.deprecatedTermInfo = deprecatedTermInformationList;
            termInformation.suggestedTermLanguages = texts;
            var termInformationObject = Object.toJSON(termInformation);
            $t.requestNewTerm(termInformationObject, {
                success: function (data) {

                    validateTermDetails.resetForm();
                    $j("#depTarget1").val('');
                    $j("#depTarget2").val('');
                    $j("#depTarget3").val('');
                    $j("#depSource1").val('');
                    $j("#depSource2").val('');
                    $j("#depSource3").val('');
                    texts = [];
                    values = 0;
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


    var chatData = function () {

        $t.getUserAccuracyRate({
            success: function (data) {
                var finalisedTerms = data.finalizedTerm;
                var votedTerms = data.votedTerms;
                var dataXML = new Array();
                if (votedTerms == 0) {
                    $j("#accuracyRate").html("0%");
                    if ($j("#hpsite").val() == "true")
                        dataXML.push("<chart showPercentValues='1'  bgColor='#F9F9F9,#F9F9F9' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#0e94bc, #D1EAF1'  canvasbgColor='#F9F9F9' showBorder='0' bgAlpha='100'>");
                    else
                        dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");
                    dataXML.push("<set label='Approved' value='" + finalisedTerms + "' />");
                    dataXML.push("<set label='Incorrect' value='" + (votedTerms - finalisedTerms) + "' />");
                    dataXML.push("</chart>");
                    ChartRender.twoDPieChart("mychartId5", 130, 70, "accuracyPie", dataXML);

                } else {
                    var accuracyRate = Math.round((finalisedTerms / votedTerms) * 100);
                    if(!(isNaN(accuracyRate))) {
                    	$j("#accuracyRate").html(accuracyRate + "%")
                    } else {
                    	$j("#accuracyRate").html("0%")
                    }
                    if ($j("#hpsite").val() == "true")
                        dataXML.push("<chart showPercentValues='1'  bgColor='#F9F9F9,#F9F9F9' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#0e94bc, #D1EAF1'  canvasbgColor='#F9F9F9' showBorder='0' bgAlpha='100'>");
                    else
                    dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");
                    dataXML.push("<set label='Approved' value='" + finalisedTerms + "' />");
                    dataXML.push("<set label='Incorrect' value='" + (votedTerms - finalisedTerms) + "' />");
                    dataXML.push("</chart>");
                    ChartRender.twoDPieChart("mychartId5", 130, 70, "accuracyPie", dataXML);
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

    var showTermPicture = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#showTermPicBrwse:ui-dialog").dialog("destroy");

        $j("#showTermPicBrwse").dialog({
            height: 420,
            width: 630,
            modal: true,
            close: function (event, ui) {
                $j("#showTermImage").val("");
            }

        });

    };


    $j().ready(function () {

        $j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.TERM_LIST);

        /**
        if ($j("#adminHeaderFlag").val() == "true") {
            $j("#adminHeader").show();
            $j('#admin').children("img").show();

        }
        if ($j("#userHeaderFlag").val() == "true") {
            $j("#userHeader").show();
            $j('#termList').children("img").show();
        }

        if ($j.browser.version == "7.0") {
            $j(".menuArrowAdmn").css("top", "26");
            $j(".termAttr").css("padding-bottom", "10px");
            $j(".termAttr").css("min-height", "135px");
            $j(".termRqstBtn").css("padding", "1px 0px");
        }
        if ($j.browser.version == "9.0") {
            $j(".paddingseven").css("padding-left", "8px");
        }

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
        */

        $t.getUserLanguages({
            success: function (data) {
                if (data == null || data == "" || data.size == 0) {
                    $j('#noLanguage').show();
                    $j('#langDetails').hide();

                } else {
                    displayUserLanguages(data);
                    getSlctdLang();
                    $j('#noLanguage').hide();
                    $j('#langDetails').show();
                }

            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }

        });


        $j("#termDetails").termDetails();
        $j("#votedTermDetails").termDetails();

/*        $j('.seeAll').click(function () {
            showLeaderBoardSeeAll();
        });*/
       /* $j('.seeAll').click(function () {
      	  var values = ($j("#termlanguageSlctForRank").val() == null) ? "0" : $j("#termlanguageSlctForRank").val();
      	  if(values != 0) {
      		  showLeaderBoardSeeAllByLanguage();
      	  } else {
      		  showLeaderBoardSeeAll();
      	  }
      });*/
        
        var oTable ;
        $j('.seeAll').click(function () {
        	  var values = ($j("#termlanguageSlctForRank").val() == null) ? "0" : $j("#termlanguageSlctForRank").val();
        	  if(values != 0) {
        		  showLeaderBoardSeeAllByLanguage();
        	  } else {
        		  showLeaderBoardSeeAll();
        	  }
        });
        


        $j('#termList').css('color', '#0DA7D5');
        $j('#termList').children("img").show();
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

        $j(".termRqstBtn").click(function () {
            showNewTermForm();
            validateTermDetails.resetForm();

            $j('#termReq').hide();
            $j('#targetReq').hide();
            $j("#termInfo").val('');
            $j("#cncptDef").val('');
            $j("#termStatus").val(0);
            $j("#termCategory").val(0);
            $j("#cncptExample").val('');
            $j("#cncptNotes").val('');
            $j("#termForm").val(0);
            $j("#termPOS").val(0);
            $j("#pgrmPjct").val(0);
            $j("#pgrmPjctd").val('');
            $j("#termNotes").val('');
            $j("#notes").val('');
            $j("#productGroup").val(0);
            $j("#category").val(0);
            $j("#trnsltn").val('');
            $j("#trgtLang ").val('');
            $j("#trgtStatus").val(0);
            $j("#trgtForm").val(0);
            $j("#trgtPOS ").val(0);
            $j("#trgtExmpl").val('');
            $j("#trgtNotes").val('');
            $j("#trgtPgrm ").val(0);
            $j("#comment").val('');

        });


        var showLeaderBoardSeeAll = function () {
            // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
            $j("#allUserList:ui-dialog").dialog("destroy");
            $j("#allUserListByLang:ui-dialog").dialog("destroy");

            $j("#allUserList").dialog({
                height: 500,
                width: 500,
                modal: true
            });
            if ($j('#leaderboard_seeAll').length > 0) {
            	console.log(oTable);
            	if(!oTable) {
                 oTable = $j('#leaderboard_seeAll').dataTable({
                    "bProcessing": true,
                    "sServerMethod": "GET",
                    "bAutoWidth": false,
                    "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getAllMembers",
                    "aoColumns": [
                        { "mDataProp": "photoPath", "bSortable": false, "sWidth": "15%", "sClass": "alignCenter"},
                        { "mDataProp": "userName", "sWidth": "25%" },
                        { "mDataProp": "totalVotes", "sWidth": "20%", "sClass": "alignCenter"},
                        { "mDataProp": "bagdingPhotopath", "sWidth": "20%"},
                        { "mDataProp": "accuracyPhotoPath", "sWidth": "20%"}
                    ],
                    "iDisplayLength": 8
                });
            }
                $j('.dataTables_length').hide();
            }
        };

        /**
        $t.getTotalUsersInSystem({
            success: function (data) {
                var totalUsersPerMonth = data;
                var dataXML = new Array();
                if (totalUsersPerMonth != null && totalUsersPerMonth != "") {
                    if ($j("#hpsite").val() == "true")
                        dataXML.push("<chart showValues='0' showBorder='0' bgColor='#e6eaeb,#e6eaeb' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' yAxisMinValue='0' yAxisMaxValue='30' showToolTip='1' chartTopMargin='5' canvasbgColor='#e6eaeb' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
                    else
                        dataXML.push("<chart showValues='0' showBorder='0' bgColor='#032D39,#032D39' bgAlpha='100,100' canvasPadding='0px' yAxisMinValue='0' yAxisMaxValue='30' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#ffffff' divLineThickness='2' labelPadding='0'  baseFontColor='#C0C0C0' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
                    for (var count = 0; count < totalUsersPerMonth.length; count++) {
                        dataXML.push("<set label='" + totalUsersPerMonth[count].month + "' " + "value='" + totalUsersPerMonth[count].count + "'/>");
                    }
                    dataXML.push("</chart>");
                    ChartRender.twoDLineChart("", "210", "70", "userChart", dataXML);
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

        $j("#trmDtlSectionHead div").click(function () {
            var sortOrder = $j(this).attr('sortOrder');
            var colName = $j(this).attr('id');
            if (colName == "column2") {
                return;
            }
            // bold text if user has sorted 
            if($j(this).text() == 'Terms Being Polled ' ) {
            	$j("#sourceTerm").addClass('sortBold');
            	$j("#pollExpirationDate").removeClass('sortBold');
            } else {
            	$j("#pollExpirationDate").addClass('sortBold');
            	$j("#sourceTerm").removeClass('sortBold');
            }
           
            if( (colName != savedCriteria.colName) && (colName == "sourceTerm") ) {
            	$j(this).addClass("ascending");
            	sortOrder = 'DESC';
            }
            if (colName == savedCriteria.colName && savedCriteria.sortOrder == 'ASC') {
                sortOrder = 'DESC';
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).find('.sort1').hide();
                $j(this).find('.sort2').hide();
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
            }
            else  if (colName == savedCriteria.colName && savedCriteria.sortOrder == 'DESC') {
                sortOrder = 'ASC';
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).find('.sort1').hide();
                $j(this).find('.sort2').hide();
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
            } else if (!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))) {
                $j(this).attr('sortOrder', 'DESC');
                $j("#trmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).find('.sort1').hide();
                    $j(this).find('.sort2').hide();
                });
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
                $j(this).addClass("ascending");
              } else if ($j(this).hasClass("ascending")) {
                $j(this).attr('sortOrder', 'ASC');

                $j("#trmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).find('.sort1').hide();
                    $j(this).find('.sort2').hide();
                });
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
                $j(this).addClass("descending");
             } else if ($j(this).hasClass("descending")) {
                $j(this).attr('sortOrder', 'DESC');


                $j("#trmDtlSectionHead div").each(function (index) {
                    $j(this).removeClass("ascending descending");
                    $j(this).find('.sort').remove();
                    $j(this).find('.sort1').hide();
                    $j(this).find('.sort2').hide();
                });
                $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
                $j(this).addClass("ascending");
            }
            if (currPage == "currentPolls") {
                triggerTermDetails(langId, colName, sortOrder, 1);

            } else if (currPage == "votedTrms") {
                triggerVotedTermDetails(langId, colName, sortOrder, 1);
            }
        });

        var oTable = $('#LBTable').dataTable({
            "bProcessing": true,
            "sServerMethod": "GET",
            "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getAllMembers",
            "aoColumns": [
                { "mDataProp": "photoPath", "bSortable": false, "sWidth": "20%", "sClass": "alignCenter" },
                { "mDataProp": "userName", "sWidth": "40%", "sTitle": "User Name" },
                { "mDataProp": "totalVotes", "sWidth": "40%", "sTitle": "Total Votes"  }
            ],
            "iDisplayLength": 8
        });
        $('.dataTables_length').hide();


        // Parts of Speech Picklist
        $t.getPOSList({
            success: function (data) {
                displayPosList(data);
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
                displayTermFormList(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        // Program Project Picklist
        $t.getProgramList({
            success: function (data) {
                displayProgramList(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
        //product group list

        $t.getProductGroupList({
            success: function (data) {
                displayProductGroupList(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
        // Status Picklist
        $t.getStatusList({
            success: function (data) {
                displayStatusList(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        // Language List

        $t.getLanguageList({
            success: function (data) {
                //displayLanguages(data,'.trgtLang');
                displayLanguages(data, '#languageSlct');

                $j("#languageSlct").multiselect().multiselectfilter();
                $j("#languageSlct").multiselect({
                    noneSelectedText: 'Select language(s)',
                    selectedList: 2,
                    onClose: function () {
                        values = ($j("#languageSlct").val() == null) ? "0" : $j("#languageSlct").val();
                        if (values != 0) {
                            $j(".languageSlctErr").hide();
                            $j("#rqstNewTerm .ui-state-default").css("border", "1px solid #dddddd");
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

        // Term Category Picklist
        $t.getCategoryList({
            success: function (data) {
                displayTermCatList(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
        
        
        var oTableForLeaderBoardLang ;
        var showLeaderBoardSeeAllByLanguage = function () {
            // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
            $j("#allUserList:ui-dialog").dialog("destroy");
            $j("#allUserListByLang:ui-dialog").dialog("destroy");
            
            $j("#allUserListByLang").dialog({
                height: 500,
                width: 500,
                modal: true
            });
            if ($j('#leaderboardByLanguage').length > 0) {
            	oTableForLeaderBoardLang = $j('#leaderboardByLanguage').dataTable({
                    "bProcessing": true,
                    "sServerMethod": "GET",
                    "bAutoWidth": false,
                    "bDestroy": true,
                    "bAutoWidth": false,
                    "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getAllMembersByLanguage/" +values,
                    "aoColumns": [
                        { "mDataProp": "photoPath", "bSortable": false, "sWidth": "15%", "sClass": "alignCenter"},
                        { "mDataProp": "userName", "sWidth": "25%" },
                        { "mDataProp": "totalVotes", "sWidth": "20%", "sClass": "alignCenter"},
                        { "mDataProp": "bagdingPhotopath", "sWidth": "20%"},
                        { "mDataProp": "accuracyPhotoPath", "sWidth": "20%"}
                    ],
                    "iDisplayLength": 8
                });
            
                $j('.dataTables_length').hide();
            }
            $j("#termLangSlctDropdwn").val($j("#termLangSlctDropdwn option:first").val()); 
        };
        
        	   $t.getLanguageList({
                   success: function (data) {
                       displayLanguages(data, '#termlanguageSlctForRank');

                       $j("#termlanguageSlctForRank").multiselect().multiselectfilter();
                       $j("#termlanguageSlctForRank").multiselect({
                           noneSelectedText: 'Select language(s)',
                           selectedList: 3,
                           onClose: function () {
                        	   values = ($j("#termlanguageSlctForRank").val() == null) ? "0" : $j("#termlanguageSlctForRank").val();
                               if(values == 0) {
                            	   LeaderBoard.showLeaderBoard();
                               } else if (values != 0) {
                                   $j("#rqstNewTerm .ui-state-default").css("border", "1px solid #dddddd");
                                   //showLeaderBoardSeeAllByLanguage();
                                   showLeaderBoardForLanguage(values);
                               }
                           },
                           classes: "lang"
                        	   
                       });
                       $j(".langDropDownSpan").css('top','-32px');   	
                   },
                   error: function (xhr, textStatus, errorThrown) {
                       console.log(xhr.responseText);
                       if (Boolean(xhr.responseText.message)) {
                           console.log(xhr.responseText.message);
                       }
                   }
               });
        	   var  showLeaderBoardForLanguage =  function(values){
     			  $j('#leadersList').empty();
     			  $t.getLeaderBoardMemberDetailsForLanguage(values,{
     				success: function(data){
     					var leaders = data;
     					if(leaders && leaders.length){
     						var membersList = (leaders.length > 2) ? 2: leaders.length;
     						for(var count = 0; count < membersList; count++){
     							var accuracyImg = "";
     							var badgingImg = "";
     							var status="";
     							
     							var accuracyRate =  Math.round(leaders[count].accuracy);
     							//var badgingRate = leaders[count].totalVotes + leaders[count].termRequestCount;
     	                        var badgingRate = leaders[count].totalVotes;
     							if(accuracyRate>=0 && accuracyRate<25){
     								accuracyImg=$j("#contextRootPath").val()+"/images/BeginnerAccuracy.jpg";
     							}else if(accuracyRate>=25 && accuracyRate<50){
     								accuracyImg=$j("#contextRootPath").val()+"/images/NoviceAccuracy.jpg";
     							}else if(accuracyRate>=50 && accuracyRate<100){
     								accuracyImg=$j("#contextRootPath").val()+"/images/RegularAccuracy.jpg";
     							}else if(accuracyRate>=100 && accuracyRate<200){
     								accuracyImg=$j("#contextRootPath").val()+"/images/AdvancedAccuracy.jpg";
     							}else if(accuracyRate >= 200){
     								accuracyImg=$j("#contextRootPath").val()+"/images/ExpertAccuracy.jpg";
     							}
     							
     							if(badgingRate > 0 && badgingRate < 25){
     								badgingImg=$j("#contextRootPath").val()+"/images/biginner.jpg";
     							}else if(badgingRate >= 25 && badgingRate <  50){
     								badgingImg=$j("#contextRootPath").val()+"/images/novice.jpg";
     							}else if(badgingRate >= 50 && badgingRate < 100 ){
     								badgingImg=$j("#contextRootPath").val()+"/images/regular.jpg";
     							}else if(badgingRate >= 100 && badgingRate < 200 ){
     								badgingImg=$j("#contextRootPath").val()+"/images/advanced.jpg";
     							}else if(badgingRate >= 200){
     								badgingImg=$j("#contextRootPath").val()+"/images/expert.jpg";
     							}
     							
     							var leaderBoardTmplClone = leaderBoardTmpl;
     							var totalCount=leaders[count].totalVotes;
     							var totalTerms1 = new String(totalCount);
     							
     							
     							leaderBoardTmplClone[1]=leaders[count].photoPath; 
     							leaderBoardTmplClone[3]=leaders[count].userName;;
     							leaderBoardTmplClone[5]=badgingImg;
     							leaderBoardTmplClone[7]=insertCommmas(totalTerms1);
     							leaderBoardTmplClone[9]=accuracyImg;
     							$j('#leadersList').append(leaderBoardTmplClone.join(""));

     							//bindEvents();
     						}
     																		
     						if(leaders.length <= 2){
     							$j(".seeAll").hide();
     						}else{
     							$j(".seeAll").show();
     						}
     						
     					}else{
     						$j(".seeAll").hide();
     						$j('#leadersList').html('<span style="text-align: center; display: block;font-size:12px;padding-top: 15px;">No members in Leader Board</span>');
     					}
     					
     				},
     				error: function(xhr, textStatus, errorThrown){
     					if(Boolean(xhr.response))
     					console.error(xhr.response.messsage);
     				}
     			});
     		}

        // Concept Category Picklist
        $t.getConceptCategoryList({
            success: function (data) {
                displayConceptCatList(data);
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