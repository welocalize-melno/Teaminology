(function ($j) {
    var pieChartHeight = 40;
    var pieChartWidth = 40;
    var selectedTermIds = "";
    var selectedEmailIds = "";
    var noOfPages = 0;
    var totalTerms = 0;
    var totalTmTerms = 0;
    var savedCriteria = null;
    var termListLimit = 10;
    var validateTerm;
    var validateTermVote;
    var photoPath;
    var pictureId = null;
    var fileName = null;
    var searchString = null;
    var searchStringTM = null;
    var cvalues = 0;
    var multiCValues = 0;
    var multiCTermsValues = null;
    var multiCTmsValues = null;
    var values = null;
    var langSelectedVal = null;
    var compSelectedVal = null;
    var langSelectedVal = [];
    var compSelectedVal = [];
    var selected;
    var tempId = 1;
    var loadingFlag = false;
    var uniqueSelectedTermIds = [];
    var selectedSavedCriteria = null;
    var selectedLangIdArray = [];
    var langValues = null;
    var dataMap = new Hash();
    var termIdsArray = new Array();
    var oldtargetTerm = "";
    var isDivClicked = false;
    var companyValues = null;
    var selectedCompanyIdsArray = [];
    var txt=null;
    var popData=null;
    var pageLength=0;
    var termVoted= null;
    var searchLangValues = null;
    var searchCompanyValues = null;
    var searchValue = null;
    var totalTerms = "";
    var scrollLimitFrom = 0;
    var scrollLimitTo  = 0;
    var termClone = false;
    var termDtlTmplClone = null;
    var paginationVal = false;
    var langSelected = "";
    var selectValue = null;
    
    var url = $j(location).attr('href');

	if(url.indexOf("/admin_manage_terms.jsp") != -1){
	    $j('#signOut').removeClass('floatrightLogOut');
	    $j('#signOut').addClass('floatrightLogOutForOverview');
	    $j('.rowBg').addClass('rowBgForAdmin');
	}
    
    
    $j("#paginationId").val($j("#paginationId option:first").val()); 
    $j('#firstStep').click(function() {
    	   paginationVal = false;
    	   $j("#paginationId").val($j("#paginationId option:first").val()); 
    });
   /* $j(window).scroll(function () {
    	 var searchBy = $j('#newTermVoted').val();
    	 if(searchBy == 'Enter term ...') {
    		 searchBy = null;
    	 }
        if ($j(document).height() <= $j(window).scrollTop() + $j(window).height()) {
        	if(paginationVal && scrollLimitFrom != 0 && !($j('#showSelectedTerms').attr('checked'))) {
        		 if(langSelected == 'selected') {
        			 triggerTermBaseByPagination(null, null, 0, searchBy, null,'Locale', values,  null, null, null);
        		 } else {
        			 triggerTermBaseByPagination(null, null, 0, searchBy, null, null, null, null, null, null);
        		 }
        		 $j("#selectAll").attr('checked', false);
    	           //$j("#showSelectedTerms").attr('checked', false);
    	         //  scrollLimitFrom = scrollLimitFrom + paginationVal;
    	           console.log("scrollLimitFrom "+scrollLimitFrom);
        	}
        }
    });*/
    $j("#paginationId").change(function (e) {
    	  selectValue = $j("#paginationId").val(); 
    	  if(selectValue != null) 
    		  termListLimit = selectValue; 
    	//  triggerPaginationTermDetails(null, null, 0, null, false, null, null, null, null, 'N',selectValue);
    	  /*if(selectValue == "Select") {
   		  paginationVal = false;
   		  $j("#termDtlRowsList").html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">Please select any criteria</span>');
		  $j("#pagination").hide();
   		  return;*/
   	      /*else 
    	  var pageLength = parseInt($j("#paginationId").val());
    	  console.log("Language values are "+values);
    	  scrollLimitTo = pageLength;
    	  scrollLimitFrom = 0;
    	  paginationVal = true;*/
    	  $j('#termDtlRowsList').empty();
    	  var searchBy = $j('#newTermVoted').val();
    	  if(searchBy == 'Enter term ...') {
      		 searchBy = null;
      	 }
    	  console.log(searchBy);
    	  if(langSelected == 'selected') {
    		  triggerPaginationTermDetails(null, null, 0, searchBy, null, 'Locale', values, null, null, null,selectValue);
    	  } else {
    		  triggerPaginationTermDetails(null, null, 0, searchBy, null, null, null, null, null, null,selectValue);
    	  }
    	   $j("#selectAll").attr('checked', false);
           $j("#showSelectedTerms").attr('checked', false);
    });
    $j.fn.termDetails = function () {
    	//newTermVoted
    	if ($j.browser.msie){
    		$j('#newTermVoted').height(15);
    	}
    	$j('#newTermVoted').val('Enter term ...');
        var ctx = $j(this);
        var termDtlTmpl = [
            '<div class="rowBg rowBgForAdmin floatleft"><div class="chkBx"><input type="checkbox" class="floatleft case" value="test" /></div> <div class="row twistie_close" termId="',
            '',
            '" id="rowid_',
            '',
            '"><div class="width110 bigFont  pollTerm" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">',
            '',
            '</div><div class="smallPie" id=',
            '',
            ' class="width40"></div><div class="width90 bigFont  toppadding5 targetTrm" style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;" >',
            '',
            '</div><div class="width90 toppadding5 trmLanguage smallFont">',
            '',
            '</div><div class="width40 toppadding5"><img src="' + $j("#contextRootPath").val() + '/images/grey_checkmark.png" height="20px" width="20px" class="novisibility tickImg ',
            '',
            ' " /></div><div class="width90 toppadding5 viewDate smallFont" id="viewDate_',
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
            //'</div><div class="width80  toppadding5 tcompany viewCompany nodisplay"">',
            '</div><div class="width80  toppadding5 tstatus">',
            '',
            '</div><div class="width40 toppadding15 modify"><img class="headerMenuLink modifyImg "  height="20px" width="20px" src="' + $j("#contextRootPath").val() + '/images/Pencil1.png" editId="',
            '',
            '" /></div><div class="width30 toppadding15 delete" ><img class="headerMenuLink deleteImg" height="20px" width="20px"  src="' + $j("#contextRootPath").val() + '/images/DeleteIcon1.png" imgId="',
            '',
            '" /></div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay"  id="overView" termId="',
            '',
            '"><div class="loadingDiv" style="text-align: center;"><img src=' + $j("#contextRootPath").val() + '/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
        ];
        var termAttrTmpl = '<div class="termAttr"><div><div><div class="bold sourceTerm label" >Term being polled<span id=#{starId} class="editDetailsFld nodisplay redTxt">*</span>: </div> <div class="sourceDesc viewDetailsFld" style="word-wrap:break-word;"></div><textarea   rows="3" cols="61"   name="editSourceTerm"  id=#{sourceId} class="sourceDescEdit editDetailsFld nodisplay editSourceTerm"  value="" ></textarea><span style="font-size:12px;color-red;position:absolute;padding-left:10px;padding-top:17px;" id=#{errorId} class="error showError nodisplay">Term being polled cannot be empty</span><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class=" bold targetTerm label" >Top suggestion: </div> <div class="targetDesc viewDetailsFld" style="word-wrap:break-word;" ></div><textarea rows="3" cols="65"   name="editTargetTerm"  id=#{targetId} class="targetDescEdit editDetailsFld nodisplay editTargetTerm"  value="" ></textarea><div class="clear"></div>'
            + '</div></div><div class="left"><div class="topmargin5"><div class="bold termType label" >Term part of speech: </div> <span class="termTypeDesc viewDetailsFld"></span><select name="editTermPOS" id=#{posId} class="editDetailsFld nodisplay editTermPOS"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Form: </div><span class="formDesc viewDetailsFld"></span><select name="termForm"  id=#{formId} class="editDetailsFld nodisplay termForm"><option value="">---Select---</option></select><div class="clear"></div>'
            
            + '</div><div class="topmargin5"><div class="bold label">Status: </div><span class="termStatusDesc viewDetailsFld"></span><select name="status"  id=#{statusId} class="editDetailsFld nodisplay status"><option value="">---Select---</option></select><div class="clear"></div>'
            
            + '</div><div class="topmargin5"><div class="bold label">Term Category: </div><span class="categoryDesc programDesc viewDetailsFld"></span><select name="category" id=#{categoryId} class="editDetailsFld nodisplay  category"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Domain: </div><span class=" termDomainDesc viewDetailsFld"></span><select name="domain" id=#{domainId} class="editDetailsFld nodisplay  domain"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Term notes: </div><div class="domainDesc viewDetailsFld" style="word-wrap:break-word;"></div><textarea id=#{notesId} rows="1" cols="12" class="domainDescEdit editDetailsFld nodisplay notes" ></textarea><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold targetTermType label">Target part of speech: </div><span class="targetTermTypeDesc viewDetailsFld"></span><select name="editTargetPOS"  id=#{targetPosID} class="editDetailsFld nodisplay editTargetPOS"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div></div><div class="right"><div class="topmargin5"><div class="bold label">Concept definition: </div><div class="definitionDesc viewDetailsFld" style="word-wrap:break-word;" ></div><textarea rows="2" cols="30" id=#{defId} class="definitionDescEdit editDetailsFld nodisplay definition" ></textarea><div class="clear"></div>'
            + '</div><div class="topmargin10"><div class="bold label">Term Usage: </div><div class="usageDesc viewDetailsFld" style="word-wrap:break-word;"></div><textarea rows="2" cols="30"   id=#{usageId}  class="usageDesc editDetailsFld nodisplay" ></textarea>'
            + '</div><div id="editTermImage" ><div id=#{uploadedImageId}  style="width:50px; border:1px solid #cccccc;height: 50px;margin-top: 26px;margin-left: 1px;"><img width="50px" height="50px" class="picture headerMenuLink " src=#{imagePath}  /></div><div style="width:50px" class=" alignCenter" id=#{changePicId}><a href="javascript:;" class="changeTermPic"   style="font-size: 14px;padding-right: 0px;">Image</a></div></div></div></div>';

        var suggTrmListTmpl = [
            '<div class="termSlctFrm topmargin15"><span class="termSuggestion" style=" word-wrap: break-word;">',
            '',
            '</span><input type="radio" name="',
            '',
            '" /><div class="votesBar" id="',
            '',
            '">',
            '',
            '</div><input type="button" value="Pick as Final" class="pickFinalBtn nodisplay" id="',
            '',
            '"/><div style="font-size:11px;font-style: italic;padding-top:5px;  width: 590px;">',
            '',
            '</div></div>'
        ];
        
        var suggTrmListForFinalTermTmpl = [
                               '<div class="termSlctFrm topmargin15"><span class="termSuggestion finalColor" style=" word-wrap: break-word;">',
                               '',
                               '</span><input type="radio" name="',
                               '',
                               '" /><div class="votesBar" id="',
                               '',
                               '">',
                               '',
                               '</div><input type="button" value="Pick as Final" class="pickFinalBtn nodisplay" id="',
                               '',
                               '"/><div style="font-size:11px;font-style: italic;padding-top:5px;  width: 590px;">',
                               '',
                               '</div></div>'
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
        var editDetailsSec = '<div class="clear"></div><div class="editLinksSec smallFont"><a href="javascript:;" class="padding5 extendPoll">Extend poll</a><a href="javascript:;" class="padding5 updateDate nodisplay">Update date</a> | <a href="javascript:;" class="padding5 editDetails">Edit details</a><a href="javascript:;" class="padding5 updateDetails nodisplay">Update details</a> | <a href="javascript:;" class="padding5 votingDetails">Voting details</a></div>';
        var deprecatedTermInfoSec = ['<div class="clear"></div><div  class=" bigFont" style="color:red;"> <span class="width220 label source" style="color:red;padding-left:39px">Deprecated Source: ',
            '',
            '</span><div class="width50" >&nbsp;</div><span class=" width220 label target">Deprecated Target: ',
            '',
            '</span></div>'
        ];
        var classNames = ['purpleBar', 'greenBar', 'yellowBar', 'pinkBar'];
        var tmxIFrame = ['<iframe id="uploadTMXFrame_',
            '',
            '" name="uploadTMXFrame_',
            '',
            '" height="0" width="0" frameborder="0" scrolling="yes"></iframe>',
            '"<form id="ImportTMXForm',
            '',
            '" name="ImportTMXForm_',
            '',
            '" method="post" target="uploadTMXFrame_',
            '',
            '" action="' + $j("#contextRootPath").val() + '"/impExp_serv"  ENCTYPE="multipart/form-data">',
            '<input type="hidden" name="c" value="importTMX"/>',
            '<input type="hidden" name="type" value="termTMX"/>',
            '<input type="hidden" name="fileId" value="" id="fileId"/>',
            '<p>Import TMX file.</p>',
            '<div class="topmargin5"><input type="file" size="40" name="upload_"',
            '',
            '" id="importTMXTerm_"',
            '',
            'style="font-size:12px;"/>  <img id="importTMXFile_',
            '',
            '" src="' + $j("#contextRootPath").val() + '"/images/document_import.png" height="35px" width="35px;" alt="Click to Import file" title="Click to Import file" style="vertical-align: bottom; margin-left: 30px;cursor:pointer;" />  </div>',
            '<div class="topmargin15 nodisplay" id="impUrl_',
            '',
            '"><a  class="nextEnable topmargin15 "  style="color: #00A4D6;margin-top: 5px;text-align: center;" href="import_export_status.jsp">Click  here to check Imported File status</a></div>',
            '</form>'
        ];
        var displayTermDetailsForPagination = function(data) {
        	Util.stopWaiting($j('#termDtlRowsList'));
    		    var termDtlTmplClone  = termDtlTmpl;
        		var termDetails = data.pollTermsList;
        		var listLength = termDetails.length;
        		 for (var count = 0; count < listLength; count++) {
                     dataMap.set(termDetails[count].termId, termDetails[count]);
                     var suggestedTerm = (termDetails[count].suggestedTerm == null || termDetails[count].suggestedTerm == "") ? "&nbsp;" : termDetails[count].suggestedTerm;
                     var partOfSpeech = (termDetails[count].partOfSpeech == null || termDetails[count].partOfSpeech == "") ? "&nbsp;" : termDetails[count].partOfSpeech;
                     var language = (termDetails[count].language == null || termDetails[count].language == "") ? "&nbsp;" : termDetails[count].language;
                     var category = (termDetails[count].category == null || termDetails[count].category == "") ? "&nbsp;" : termDetails[count].category;
                     var domain = (termDetails[count].domain == null || termDetails[count].domain == "") ? "&nbsp;" : termDetails[count].domain;
                     //var company = (termDetails[count].company == null || termDetails[count].company == "") ? "&nbsp;" : termDetails[count].company;
                     var status = (termDetails[count].status == null || termDetails[count].status == "") ? "&nbsp;" : termDetails[count].status;
                     termDtlTmplClone[1] = termDetails[count].termId;
                     termDtlTmplClone[3] = termDetails[count].termId;
                     var sourceTerm = termDetails[count].sourceTerm;
                     if (termDetails[count].deprecatedCount > 0) {
                         if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                             termDtlTmplClone[5] = "<span style='font-weight:bold;font-size:20px;color:red'><b>!</b></span>" + sourceTerm;
                         } else {
                             termDtlTmplClone[5] = "<span style='font-weight:bold;font-size:17px;color:red'><b>!</b></span>" + sourceTerm;
                         }
                     } else {
                         termDtlTmplClone[5] = sourceTerm;
                     }
                     termDtlTmplClone[7] = "dataPie_" + termDetails[count].termId;
                     termDtlTmplClone[9] = suggestedTerm;
                     termDtlTmplClone[11] = language;
                     termDtlTmplClone[13] = "tickImg_" + termDetails[count].termId;
                     termDtlTmplClone[15] = termDetails[count].termId
                     termDtlTmplClone[17] = termDetails[count].pollExpirationDt;
                     termDtlTmplClone[19] = "datepicker" + termDetails[count].termId;
                     termDtlTmplClone[21] = partOfSpeech;
                     termDtlTmplClone[23] = category;
                     termDtlTmplClone[25] = domain;
                     termDtlTmplClone[27] = status;
                     //termDtlTmplClone[27] = company;
                     termDtlTmplClone[29] = termDetails[count].termId;
                     termDtlTmplClone[31] = termDetails[count].termId;
                     termDtlTmplClone[33] = termDetails[count].termId;
                     $j('#termDtlRowsList').append(termDtlTmplClone.join(""));
                     $j('#pagination').css('display', 'none');
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
                             buttonImage: $j("#contextRootPath").val() + "/images/calendar.gif",
                             buttonImageOnly: true,
                             minDate: new Date(currentYear, currentMonth, currentDate)  //Disables all the past dates and only current or future dats can be selected
                         });
                     });

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
        var displayTermDetails = function (data) {
            Util.stopWaiting($j('#termDtlRowsList'));
            var termDetails = data.pollTermsList;
            var listLength = (termDetails.length >= termListLimit) ? termListLimit : termDetails.length;
            for (var count = 0; count < listLength; count++) {
                dataMap.set(termDetails[count].termId, termDetails[count]);
                var termDtlTmplClone = termDtlTmpl;
                var suggestedTerm = (termDetails[count].suggestedTerm == null || termDetails[count].suggestedTerm == "") ? "&nbsp;" : termDetails[count].suggestedTerm;
                var partOfSpeech = (termDetails[count].partOfSpeech == null || termDetails[count].partOfSpeech == "") ? "&nbsp;" : termDetails[count].partOfSpeech;
                var language = (termDetails[count].language == null || termDetails[count].language == "") ? "&nbsp;" : termDetails[count].language;
                var category = (termDetails[count].category == null || termDetails[count].category == "") ? "&nbsp;" : termDetails[count].category;
                var domain = (termDetails[count].domain == null || termDetails[count].domain == "") ? "&nbsp;" : termDetails[count].domain;
                //var company = (termDetails[count].company == null || termDetails[count].company == "") ? "&nbsp;" : termDetails[count].company;
                var status = (termDetails[count].status == null || termDetails[count].status == "") ? "&nbsp;" : termDetails[count].status;
                termDtlTmplClone[1] = termDetails[count].termId;
                termDtlTmplClone[3] = termDetails[count].termId;
                var sourceTerm = termDetails[count].sourceTerm;
                if (termDetails[count].deprecatedCount > 0) {
                    if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                        termDtlTmplClone[5] = "<span style='font-weight:bold;font-size:20px;color:red'><b>!</b></span>" + sourceTerm;
                    } else {
                        termDtlTmplClone[5] = "<span style='font-weight:bold;font-size:17px;color:red'><b>!</b></span>" + sourceTerm;
                    }
                } else {
                    termDtlTmplClone[5] = sourceTerm;
                }
                termDtlTmplClone[7] = "dataPie_" + termDetails[count].termId;
                termDtlTmplClone[9] = suggestedTerm;
                termDtlTmplClone[11] = language;
                termDtlTmplClone[13] = "tickImg_" + termDetails[count].termId;
                termDtlTmplClone[15] = termDetails[count].termId
                termDtlTmplClone[17] = termDetails[count].pollExpirationDt;
                termDtlTmplClone[19] = "datepicker" + termDetails[count].termId;
                termDtlTmplClone[21] = partOfSpeech;
                termDtlTmplClone[23] = category;
                termDtlTmplClone[25] = domain;
                termDtlTmplClone[27] = status;
                //termDtlTmplClone[27] = company;
                termDtlTmplClone[29] = termDetails[count].termId;
                termDtlTmplClone[31] = termDetails[count].termId;
                termDtlTmplClone[33] = termDetails[count].termId;
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
                        buttonImage: $j("#contextRootPath").val() + "/images/calendar.gif",
                        buttonImageOnly: true,
                        minDate: new Date(currentYear, currentMonth, currentDate)  //Disables all the past dates and only current or future dats can be selected
                    });
                });

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

        var displaySelectedTermDetails = function (selecteddata) {
            $j('#termDtlRowsList').empty();
            $j('#pagination').hide();
            paginationVal =  false;
            var termDetails = selecteddata.pollTermsList;
            var length = termDetails.length;
            for (var count = 0; count < length; count++) {
                dataMap.set(termDetails[count].termId, termDetails[count]);
                var termDtlTmplClone = termDtlTmpl;
                var suggestedTerm = (termDetails[count].suggestedTerm == null || termDetails[count].suggestedTerm == "") ? "&nbsp;" : termDetails[count].suggestedTerm;
                var partOfSpeech = (termDetails[count].partOfSpeech == null || termDetails[count].partOfSpeech == "") ? "&nbsp;" : termDetails[count].partOfSpeech;
                var language = (termDetails[count].language == null || termDetails[count].language == "") ? "&nbsp;" : termDetails[count].language;
                var category = (termDetails[count].category == null || termDetails[count].category == "") ? "&nbsp;" : termDetails[count].category;
                var domain = (termDetails[count].domain == null || termDetails[count].domain == "") ? "&nbsp;" : termDetails[count].domain;
                //var company = (termDetails[count].company == null || termDetails[count].company == "") ? "&nbsp;" : termDetails[count].company;
                var status = (termDetails[count].status == null || termDetails[count].status == "") ? "&nbsp;" : termDetails[count].status;
                termDtlTmplClone[1] = termDetails[count].termId;
                termDtlTmplClone[3] = termDetails[count].termId;
                var sourceTerm = termDetails[count].sourceTerm;
                if (termDetails[count].deprecatedCount > 0) {
                    if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                        termDtlTmplClone[5] = "<span style='font-weight:bold;font-size:20px;color:red'><b>!</b></span>" + sourceTerm;
                    } else {
                        termDtlTmplClone[5] = "<span style='font-weight:bold;font-size:17px;color:red'><b>!</b></span>" + sourceTerm;
                    }
                } else {
                    termDtlTmplClone[5] = sourceTerm;
                }
                termDtlTmplClone[7] = "dataPie_" + termDetails[count].termId;
                termDtlTmplClone[9] = suggestedTerm;
                termDtlTmplClone[11] = language;
                termDtlTmplClone[13] = "tickImg_" + termDetails[count].termId;
                termDtlTmplClone[15] = termDetails[count].termId
                termDtlTmplClone[17] = termDetails[count].pollExpirationDt;
                termDtlTmplClone[19] = "datepicker" + termDetails[count].termId;
                termDtlTmplClone[21] = partOfSpeech;
                termDtlTmplClone[23] = category;
                termDtlTmplClone[25] = domain;
                termDtlTmplClone[27] = status;
                //termDtlTmplClone[27] = company;
                termDtlTmplClone[29] = termDetails[count].termId;
                termDtlTmplClone[31] = termDetails[count].termId;
                termDtlTmplClone[33] = termDetails[count].termId;
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
                        buttonImage: $j("#contextRootPath").val() + "/images/calendar.gif",
                        buttonImageOnly: true,
                        minDate: new Date(currentYear, currentMonth, currentDate)  //Disables all the past dates and only current or future dats can be selected
                    });
                });
            }

            showRolePrivileges();

            $j("#selectAll").attr("checked", true);
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
                var countcheck = $j('#termDtlRowsList input[type="checkbox"]:checked').length;
                if (countcheck != length) {
                    $j("#selectAll").attr("checked", false);
                } else {
                    $j("#selectAll").attr("checked", true);
                }
                if ($j(this).attr('checked') != "checked") {
                    var tempIds = "";
                    var separator = "";
                    if (selectedTermIds != "") {
                        var temptermIdsArray = selectedTermIds.split(",");
                        for (var j = 0; j < temptermIdsArray.length; j++) {
                            if (temptermIdsArray[j] != ($j(this).parent().next().attr("termId"))) {
                                tempIds = tempIds + separator + temptermIdsArray[j];
                                separator = ",";
                            }
                        }
                    }
                    selectedTermIds = tempIds;
                }
            });
        };

        var bindEvents = function (data) {
            var classNamesClone = classNames.slice(0);
            $j('#termDtlRowsList .row').click(function () {
                var row = $j(this);
                $j("#star_" + termId).hide();
                $j("#error_" + termId).hide();
                $j("#sourceId_" + termId).removeClass('errorBorder');
                if ($j(this).hasClass('twistie_close')) {
                    $j(this).parent().next().next().show();
                    $j(this).removeClass("twistie_close");
                    $j(this).addClass("twistie_open");
                    var termId = $j(this).attr("termId");
                    var searchBy = $j("#newTerm").val();
                    searchBy = $j.trim(searchBy);
                    searchBy = searchBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
                    var searchText = searchBy.replace(/\s\s+/g, ' ');
                    searchText = $j.trim(searchText);
                    var caseFlag = false;
                    if ($j("#chkCase").attr('checked')) {
                        caseFlag = true;
                    }
                    $g.termDtlOpenEle = $j(this).parent().next().next();
                    showRolePrivileges();
                    $t.getTermAttributes(termId, {
                        success: function (data) {
                            if (Boolean(data)) {
                                var termInfo = data.termInfo;
                                var defaultImg = $j("#contextRootPath").val() + "/images/default.jpg";
                                var detailsSec = row.parent().siblings('.viewDtlsRow[termId=' + row.attr('termId') + '] ');
                                var termImage = defaultImg;
                                if (termInfo != null && termInfo.photoPath != null)
                                    termImage = termInfo.photoPath;
                                var termAttrIds = {
                                    sourceId: 'sourceId_' + termId,
                                    targetId: 'targetId_' + termId,
                                    posId: 'posId_' + termId,
                                    targetPosID:'targetPosID_' + termId,
                                    statusId:'statusId_' + termId,
                                    formId: 'formId_' + termId,
                                    categoryId: 'categoryId_' + termId,
                                    domainId: 'domainId_' + termId,
                                    notesId: 'notesId_' + termId,
                                    defId: 'defId_' + termId,
                                    usageId: 'usageId_' + termId,
                                    changePicId: 'changePicId_' + termId,
                                    uploadedImageId: 'uploadedImageId_' + termId,
                                    errorId: 'errorId_' + termId,
                                    starId: 'starId_' + termId,
                                    imagePath: termImage
                                }

                                var editTermTmpl = new Template(termAttrTmpl).evaluate(termAttrIds);
                                detailsSec.html(editTermTmpl);
                                var suggTrmListTmplClone = suggTrmListTmpl;
                                //For showing final term in green color.
                                var suggTrmListForFinalTermTmplClone = suggTrmListForFinalTermTmpl;
                                var deprecatedTermInfoSecClone = deprecatedTermInfoSec;

                                if (termInfo != null) {
                                    var termUsage = (termInfo.termUsage == null) ? "" : termInfo.termUsage;
                                    var conceptDefinition = (termInfo.conceptDefinition == null) ? "" : termInfo.conceptDefinition;
                                    var termNotes = (termInfo.termNotes == null) ? "" : termInfo.termNotes;
                                    var categoryName = (termInfo.termCategory == null || termInfo.termCategory.category == null) ? "" : termInfo.termCategory.category;
                                    var formName = (termInfo.termForm == null || termInfo.termForm.formName == null) ? "" : termInfo.termForm.formName;
                                    var partOfSpeech = (termInfo.termPOS == null || termInfo.termPOS.partOfSpeech == null) ? "" : termInfo.termPOS.partOfSpeech;
                                    var targetPartOfSpeech = (termInfo.targetTermPOS == null || termInfo.targetTermPOS.partOfSpeech == null) ? "" : termInfo.targetTermPOS.partOfSpeech;
                                    var targetTerm = (termInfo.suggestedTerm == null || termInfo.suggestedTerm == null) ? "" : termInfo.suggestedTerm;
                                    var termBeingPolled = (termInfo.termBeingPolled == null || termInfo.termBeingPolled == null) ? "" : termInfo.termBeingPolled;
                                    var domainName = (termInfo.termDomain == null || termInfo.termDomain.domain == null) ? "" : termInfo.termDomain.domain;
                                    var statusName = (termInfo.termStatus == null || termInfo.termStatus.status == null) ? "" : termInfo.termStatus.status;
                                    
                                    detailsSec.find('.sourceDesc').html(termBeingPolled);
                                    detailsSec.find('.sourceDesc').each(function (i) {
                                        if (searchBy != null && searchBy != "Enter term to search...") {
                                            var sourceTermText = $j(this).html();

                                            sourceTermText = Util.backgroundYellow(sourceTermText, searchText, null, caseFlag);

                                            $j(this).html(sourceTermText);
                                        }
                                    });
                                    detailsSec.find('.sourceDescEdit').html(termBeingPolled);
                                    detailsSec.find('.targetDesc').html(targetTerm);
                                    detailsSec.find('.targetDesc').each(function (i) {
                                        if (searchBy != null && searchBy != "Enter term to search...") {
                                            var targetTermText = $j(this).html();
                                            targetTermText = Util.backgroundYellow(targetTermText, searchText, null, caseFlag);
                                            $j(this).html(targetTermText);
                                        }
                                    });
                                    detailsSec.find('.targetDescEdit').html(targetTerm);
                                    detailsSec.find('.termDomainDesc').html(domainName);
                                    detailsSec.find('.termStatusDesc').html(statusName);
                                    detailsSec.find('.usageDesc').html(termUsage);
                                    detailsSec.find('.usageDescEdit').html(termUsage);
                                    detailsSec.find('.definitionDesc').html(conceptDefinition);
                                    detailsSec.find('.definitionDescEdit').html(conceptDefinition);
                                    detailsSec.find('.domainDesc').html(termNotes);
                                    detailsSec.find('.domainDescEdit').html(termNotes);
                                    detailsSec.find('.categoryDesc').html(categoryName);
                                    detailsSec.find('.formDesc').html(formName);
                                    detailsSec.find('.termTypeDesc').html(partOfSpeech);
                                    detailsSec.find('.targetTermTypeDesc').html(targetPartOfSpeech);
                                   
                                    var origDef = detailsSec.find('.domainDesc').html();
                                    if (origDef.length > 40) {
                                        var finalText = origDef.substring(0, 36);
                                        finalText = finalText + "...";
                                        detailsSec.find('.domainDesc').text(finalText);
                                        detailsSec.find('.domainDesc').attr("title", origDef);
                                    }
                                }
                                if (data.suggestedTermDetails != null) {
                                    for (var i = 0; i < data.suggestedTermDetails.length; i++) {
                                        var numRand = Math.floor(Math.random() * 101);
                                        if (data.suggestedTermDetails[i].isUpdated == 'Y' && data.suggestedTermDetails[i].noOfVotes > 0) {
                                            suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
                                            suggTrmListTmplClone[11] = "<span>(" + Constants.UPDATED_TERM_MSG + ")</span>";
                                            suggTrmListForFinalTermTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
                                            suggTrmListForFinalTermTmplClone[11] = "<span>(" + Constants.UPDATED_TERM_MSG + ")</span>";
                                        } else {
                                            suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
                                            suggTrmListTmplClone[11] = "";
                                            suggTrmListForFinalTermTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
                                            suggTrmListForFinalTermTmplClone[11] = "";
                                        }
                                        suggTrmListTmplClone[3] = row.attr('termId');
                                        suggTrmListTmplClone[5] = "barId_" + row.attr('termId') + i;
                                        suggTrmListTmplClone[7] = data.suggestedTermDetails[i].noOfVotes;
                                        suggTrmListTmplClone[9] = data.suggestedTermDetails[i].suggestedTermId;
                                        suggTrmListForFinalTermTmplClone[3] = row.attr('termId');
                                        suggTrmListForFinalTermTmplClone[5] = "barId_" + row.attr('termId') + i;
                                        suggTrmListForFinalTermTmplClone[7] = data.suggestedTermDetails[i].noOfVotes;
                                        suggTrmListForFinalTermTmplClone[9] = data.suggestedTermDetails[i].suggestedTermId;
                                        if((data.suggestedTermDetails[i].suggestedTerm == termInfo.suggestedTerm) && termInfo.termStatusId == 2) {
                                        	detailsSec.append(suggTrmListForFinalTermTmplClone.join(""));
                                        } else {
                                        	detailsSec.append(suggTrmListTmplClone.join(""));
                                        }
                                    }
                                }

                                newTermSec[1] = suggTrmListTmplClone[3];
                                newTermSec[3] = "sugg_" + suggTrmListTmplClone[3];
                                newTermSec[5] = "submitVote" + suggTrmListTmplClone[3];
                                detailsSec.append(newTermSec.join(""));

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
                                    if (searchBy != null && searchBy != "Enter term to search...") {

                                        sourceTerm = Util.backgroundYellow(sourceTerm, searchText, null, caseFlag);
                                    }

                                    deprecatedTermInfoSecClone[1] = sourceTerm;
                                    var targetTerm = $j.trimText(finalTarget, 30);
                                    if (searchBy != null && searchBy != "Enter term to search...") {
                                        targetTerm = Util.backgroundYellow(targetTerm, searchText, null, caseFlag);
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
                                    oldtargetTerm = $j("#targetId_" + termId).val();
                                    row.find('.editDetailsFld').show();
                                    detailsSec.find('.sourceDescEdit').each(function (i) {
                                        if (searchBy != null && searchBy != "Enter term to search...") {
                                            var sourceTermText = $j(this).html();
                                            sourceTermText = Util.backgroundYellow(sourceTermText, searchText, null, caseFlag);
                                            $j(this).html(sourceTermText);
                                        }
                                    });
                                    row.find('.viewDetailsFld').hide();
                                    detailsSec.find('.termAttr').css("padding-bottom", "68px");

                                  //set term status
                                    $t.getStatusList({
                                        success: function (data) {
                                            displayStatusList(data, '.status');
                                            var selectedStatus = detailsSec.find('.termStatusDesc').html();
                                            $j("select[name='status'] option").each(function () {
                                                if ($j(this).text() == selectedStatus)
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
                                    
                                    $t.getPOSList({
                                        success: function (data) {
                                            displayPosList(data, '.editTermPOS');
                                            displayPosListForTarget(data, '.editTargetPOS');
                                            var selectedPos = detailsSec.find('.termTypeDesc').html();
                                            var selectedTargetPos=detailsSec.find('.targetTermTypeDesc').html();
                                            $j("select[name='editTermPOS'] option").each(function () {
                                                if ($j(this).text() == selectedPos)
                                                    $j(this).attr("selected", "selected");
                                            });
                                            $j("select[name='editTargetPOS'] option").each(function () {
                                                if ($j(this).text() == selectedTargetPos)
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
                                            var selectedDomain = detailsSec.find('.termDomainDesc').html().replace("&amp;","&");
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
                                    var termId = row.attr('termId');
                                    var sourceTermDiv = $j("#sourceId_" + termId).val();
                                    var targetTerm = $j("#targetId_" + termId).val();
                                    var showTargetTerm = "";
                                    /*=(targetTerm=="")?"&nbsp;":targetTerm;*/
                                    if ($j.trim(sourceTermDiv) != "") {
                                        if ($j.trim(targetTerm) != "") {
                                            showTargetTerm = targetTerm;
                                        }
                                        else {
                                            if ($j.trim(oldtargetTerm) == "") {
                                                showTargetTerm = "&nbsp";
                                            }
                                            else {
                                                showTargetTerm = oldtargetTerm;
                                            }
                                        }
                                        detailsSec.hide();
                                        row.find('.editDetailsFld').hide();
                                        row.find('.viewDetailsFld').show();
                                        row.removeClass("twistie_open");
                                        row.addClass("twistie_close");
                                        $j("#errorId_" + termId).hide();
                                        row.find('.editDate').hide();
                                        row.find('.viewDate').show();
                                        var termId = row.attr('termId');
                                        saveTermDetails(termId);
                                        var suggTerm = $j("#targetId_" + termId).val();
                                        var termPOS = $j("#posId_" + termId + " :selected").text();
                                        var targetPOS=$j("#targetPosID_" + termId + " :selected").text(); 
                                        var termCat = $j("#categoryId_" + termId + " :selected").text();
                                        var trmDomain = $j("#domainId_" + termId + " :selected").text();
                                        var trmStatus = $j("#statusId_" + termId + " :selected").text();
                                        var term = (termPOS == "---Select---") ? "&nbsp;" : termPOS;
                                        var showCategory = (termCat == "---Select---") ? "&nbsp;" : termCat;
                                        var showDomain = (trmDomain == "---Select---") ? "&nbsp;" : trmDomain;
                                        //var topSuggestedTerm=(suggTerm ==null || suggTerm =="")?"&nbsp;":suggTerm;
                                        var sourceTerm = $j("#sourceId_" + termId).val();// lalitha
                                        var showSourceTerm = (sourceTerm == "") ? "&nbsp;" : sourceTerm;
                                        var status = (trmStatus == "---Select---") ? "&nbsp;" : trmStatus;
                                        row.find('.tPartsOfSpeech').html(term).show();
                                        row.find('.tCategory').html(showCategory).show();
                                        row.find('.tDomain').html(showDomain).show();
                                        row.find('.targetTrm').html(showTargetTerm).show();
                                        row.find(".pollTerm").attr("title", sourceTerm);
                                        var setSourceTerm = "";
                                        if (data.deprecatedTermInfo.length > 0) {
                                            if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                                                setSourceTerm = "<span style='font-weight:bold;font-size:20px;color:red'><b>!</b></span>" + showSourceTerm;
                                            } else {
                                            	setSourceTerm = "<span style='font-weight:bold;font-size:17px;color:red'><b>!</b></span>" + showSourceTerm;
                                            }
                                        } else {
                                        	setSourceTerm = sourceTerm;
                                        }
                                        row.find('.pollTerm').html(setSourceTerm).show();
                                        row.find('.tstatus').html(status).show();
                                    } else {
                                        $j("#errorId_" + termId).show();
                                        $j("#sourceId_" + termId).addClass('errorBorder');

                                    }

                                    row.find('.editDate').hide();
                                    row.find('.viewDate').show();

                                    row.find('.targetTrm').each(function (i) {
                                        var origText = $j(this).text();
                                        if (searchBy != null && searchBy != "Enter term to search...") {

                                            if (origText.length > 8) {
                                                var finalText = origText.substring(0, 6);
                                                finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                                finalText = finalText + "...";

                                                $j(this).html(finalText);
                                                $j(this).attr("title", origText);
                                            } else {

                                                var origText = $j(this).text();
                                                origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                                $j(this).html(origText);
                                            }
                                        } else {
                                            if (origText.length > 10) {
                                                var finalText = origText.substring(0, 8);
                                                finalText = finalText + "...";
                                                $j(this).text(finalText);
                                                $j(this).attr("title", origText);
                                            }
                                        }
                                    });
                                    row.find('.tCategory').each(function (i) {
                                        var origText = $j(this).text();
                                        if (origText.length > 14) {
                                            var finalText = origText.substring(0, 12);
                                            finalText = finalText + "...";
                                            $j(this).text(finalText);
                                            $j(this).attr("title", origText);
                                        }
                                    });
                                    row.find('.tPartsOfSpeech').each(function (i) {
                                        var origText = $j(this).text();
                                        if (origText.length > 14) {
                                            var finalText = origText.substring(0, 12);
                                            finalText = finalText + "...";
                                            $j(this).text(finalText);
                                            $j(this).attr("title", origText);
                                        }
                                    });
                                    row.find('.tDomain').each(function (i) {
                                        var origText = $j(this).text();
                                        if (origText.length > 14) {
                                            var finalText = origText.substring(0, 12);
                                            finalText = finalText + "...";
                                            $j(this).text(finalText);
                                            $j(this).attr("title", origText);
                                        }
                                    });
                                });

                                detailsSec.find('.editSourceTerm').click(function () {
                                    //	$j('.editSourceTerm').removeClass('errorBorder');
                                    $j("#sourceId_" + termId).removeClass('errorBorder');
                                    $j("#errorId_" + termId).hide();
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
                                    if (Util.isDate(editedDate)) {
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
                                    var termPOS = $j("#posId_" + termId + " :selected").text();
                                    var targetPos = $j("#targetPosID_" + termId + " :selected").text();
                                    var termCat = $j("#categoryId_" + termId + " :selected").text();
                                    var term = (termPOS == "---Select---") ? "&nbsp;" : termPOS;
                                    var target=(targetPos == "---Select---") ? "&nbsp;" : targetPos;
                                    var showCategory = (termCat == "---Select---") ? "&nbsp;" : termCat;
                                    var targetTerm = $j("#targetId_" + termId).val();
                                    var sourceTerm = $j("#sourceId_" + termId).val();// lalitha
                                    var showSourceTerm = (sourceTerm == "") ? "&nbsp;" : sourceTerm;
                                    //var showTargetTerm=( $j.trim(targetTerm)=="")?"&nbsp;":targetTerm;
                                    finaliseTerm(suggestedTermId);
                                    detailsSec.hide();
                                    row.removeClass("twistie_open");
                                    row.addClass("twistie_close");
                                    row.find('.tickImg').css("visibility", "visible");
                                    row.find(".targetTrm").text($j(this).parent().find(".termSuggestion").html());
                                    row.find('.targetTrm').each(function (i) {
                                        var origText = $j(this).text();
                                        if (searchBy != null && searchBy != "Enter term to search...") {
                                            if (origText.length > 8) {
                                                var finalText = origText.substring(0, 6);
                                                finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                                finalText = finalText + "...";
                                                $j(this).html(finalText);
                                                $j(this).attr("title", origText);
                                            } else {
                                                var origText = $j(this).text();
                                                origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                                $j(this).html(origText);
                                            }
                                        } else {
                                            if (origText.length > 10) {
                                                var finalText = origText.substring(0, 8);
                                                finalText = finalText + "...";
                                                $j(this).text(finalText);
                                                $j(this).attr("title", origText);
                                            }
                                        }
                                    });
                                    row.find('.editDetailsFld').hide();
                                    row.find('.viewDetailsFld').show();
                                    row.find('.editDate').hide();
                                    row.find('.viewDate').show();
//                                    row.find('.pollTerm').html(showSourceTerm).show();
                                    row.find(".targetTrm").text($j(this).parent().find(".termSuggestion").html());
                                    row.find(".tstatus").text("Approved");
                                });
                                detailsSec.find('.votingDetails').click(function () {
                                    var termId = row.attr('termId');
                                    if ($j('#votiongDetailsTab').length > 0) {
                                    	var count = 0;
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
                                                { "mDataProp": "votedTerm", "sWidth": "20%"},
                                                { "mDataProp": "userComments", "sWidth": "25%",
                                                	"fnRender": function (oObj) {
                                                		 txt=oObj.aData[oObj.oSettings.aoColumns[oObj.iDataColumn].mDataProp];
                                                		if(txt != null){
                                                		if(txt.length>100){
                                                			popData=txt;
                                                			var tempHtml = "<div  class='subdata comment' id='subdata_"+count+"'>"+txt.substring(0,50)+" "+txt.substring(50,100) +"..."+"<span style='display: none;' class='tip-text'>"+txt+"</span></div>";
                                                			count++;
                                                			return tempHtml;
                                                		}
                                                		else {
                                                			return "<div>"+txt +"</div>";
                                                		}
                                                	}
                                                    }
                                             },
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
                
        
        var bindEvent = function (data) {
            var classNamesClone = classNames.slice(0);
            $j(".rowBg .row .modify .modifyImg").click(function (event) {
                $j("#errorMsg").hide();
                event.stopPropagation();
                var termId = $j(this).attr("editid");
                var row = $j("#rowid_" + termId);
                $j("#rowid_" + termId).parent().next().next().show();
                $j("#rowid_" + termId).removeClass("twistie_close");
                $j("#rowid_" + termId).addClass("twistie_open");
                showRolePrivileges();
                var searchBy = $j("#newTerm").val();
                searchBy = $j.trim(searchBy);
                searchBy = searchBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
                var searchText = searchBy.replace(/\s\s+/g, ' ');
                searchText = $j.trim(searchText);
                var caseFlag = false;
                if ($j("#chkCase").attr('checked')) {
                    caseFlag = true;
                }
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
                                statusId:'statusId_' + termId,
                                targetPosID: 'targetPosID_' + termId,
                                formId: 'formId_' + termId,
                                categoryId: 'categoryId_' + termId,
                                domainId: 'domainId_' + termId,
                                notesId: 'notesId_' + termId,
                                defId: 'defId_' + termId,
                                usageId: 'usageId_' + termId,
                                changePicId: 'changePicId_' + termId,
                                uploadedImageId: 'uploadedImageId_' + termId,
                                errorId: 'errorId_' + termId,
                                starId: 'starId_' + termId,
                                imagePath: termImage
                            }

                            var editTermTmpl = new Template(termAttrTmpl).evaluate(termAttrIds);
                            detailsSec.html(editTermTmpl);
                            var suggTrmListTmplClone = suggTrmListTmpl;
                            var deprecatedTermInfoSecClone = deprecatedTermInfoSec;

                            if (termInfo != null) {
                                var termUsage = (termInfo.termUsage == null) ? "" : termInfo.termUsage;
                                var conceptDefinition = (termInfo.conceptDefinition == null) ? "" : termInfo.conceptDefinition;
                                var termNotes = (termInfo.termNotes == null) ? "" : termInfo.termNotes;
                                var categoryName = (termInfo.termCategory == null || termInfo.termCategory.category == null) ? "" : termInfo.termCategory.category;
                                var formName = (termInfo.termForm == null || termInfo.termForm.formName == null) ? "" : termInfo.termForm.formName;
                                var partOfSpeech = (termInfo.termPOS == null || termInfo.termPOS.partOfSpeech == null) ? "" : termInfo.termPOS.partOfSpeech;
                                var targetPartOfSpeech=(termInfo.targetTermPOS == null || termInfo.targetTermPOS.partOfSpeech == null) ? "" : termInfo.targetTermPOS.partOfSpeech;
                                var suggestedTerm = (termInfo.suggestedTerm == null || termInfo.suggestedTerm == null) ? "" : termInfo.suggestedTerm;
                                var termBeingPolled = (termInfo.termBeingPolled == null || termInfo.termBeingPolled == null) ? "" : termInfo.termBeingPolled;
                                var domainName = (termInfo.termDomain == null || termInfo.termDomain.domain == null) ? "" : termInfo.termDomain.domain;
                                var statusName = (termInfo.termStatus == null || termInfo.termStatus.status == null) ? "" : termInfo.termStatus.status;
                                detailsSec.find('.sourceDesc').html(termBeingPolled);
                                detailsSec.find('.sourceDescEdit').html(termBeingPolled);
                                detailsSec.find('.sourceDescEdit').each(function (i) {
                                    if (searchBy != null && searchBy != "Enter term to search...") {
                                        var sourceTermText = $j(this).html();
                                        sourceTermText = Util.backgroundYellow(sourceTermText, searchText, null, caseFlag);

                                        $j(this).html(sourceTermText);
                                    }
                                });
                                detailsSec.find('.termStatusDesc').html(statusName);
                                detailsSec.find('.targetDesc').html(suggestedTerm);
                                detailsSec.find('.targetDescEdit').html(suggestedTerm);
                                detailsSec.find('.termDomainDesc').html(domainName);
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
                                detailsSec.find('.targetTermTypeDesc').html(targetPartOfSpeech);
                            }
                            for (var i = 0; i < data.suggestedTermDetails.length; i++) {
                                var numRand = Math.floor(Math.random() * 101);
                                suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
                                suggTrmListTmplClone[3] = row.attr('termId');
                                suggTrmListTmplClone[5] = "barId_" + row.attr('termId') + i;
                                suggTrmListTmplClone[7] = data.suggestedTermDetails[i].noOfVotes;
                                suggTrmListTmplClone[9] = data.suggestedTermDetails[i].suggestedTermId;
                                detailsSec.append(suggTrmListTmplClone.join(""));
                            }
                            newTermSec[1] = suggTrmListTmplClone[3];
                            newTermSec[3] = "sugg_" + suggTrmListTmplClone[3];
                            newTermSec[5] = "submitVote" + suggTrmListTmplClone[3];
                            detailsSec.append(newTermSec.join(""));

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
                                    sourceTerm = Util.backgroundYellow(sourceTerm, searchText, null, caseFlag);
                                }

                                deprecatedTermInfoSecClone[1] = sourceTerm;
                                var targetTerm = $j.trimText(finalTarget, 30);
                                if (searchBy != null) {
                                    targetTerm = Util.backgroundYellow(targetTerm, searchText, null, caseFlag);
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
                                oldtargetTerm = $j("#targetId_" + termId).val();
                                row.find('.editDetailsFld').show();
                                row.find('.viewDetailsFld').hide();
                                $j("#errorId_" + termId).hide();
                                $j("#sourceId_" + termId).removeClass('errorBorder');
                                $t.getPOSList({
                                    success: function (data) {
                                        displayPosList(data, '.editTermPOS');
                                        displayPosListForTarget(data, '.editTargetPOS');
                                        var selectedPos = detailsSec.find('.termTypeDesc').html();
                                        var selectedTargetPos=detailsSec.find('.targetTermTypeDesc').html();
                                        $j("select[name='editTermPOS'] option").each(function () {
                                            if ($j(this).text() == selectedPos)
                                                $j(this).attr("selected", "selected");
                                        });
                                        $j("select[name='editTargetPOS'] option").each(function () {
                                            if ($j(this).text() == selectedTargetPos)
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
                              //set term status
                                $t.getStatusList({
                                    success: function (data) {
                                        displayStatusList(data, '.status');
                                        var selectedStatus = detailsSec.find('.termStatusDesc').html();
                                        $j("select[name='status'] option").each(function () {
                                            if ($j(this).text() == selectedStatus)
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
                                        var selectedDomain = detailsSec.find('.termDomainDesc').html().replace("&amp;","&");;
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
                                var termId = row.attr('termId');
                                var sourceTermDiv = $j("#sourceId_" + termId).val();
                                var targetTerm = $j("#targetId_" + termId).val();
                                var showTargetTerm = "";
                                /*=(targetTerm=="")?"&nbsp;":targetTerm;*/
                                if ($j.trim(sourceTermDiv) != "") {
                                    if ($j.trim(targetTerm) != "") {
                                        showTargetTerm = targetTerm;
                                    }
                                    else {
                                        if ($j.trim(oldtargetTerm) == "") {
                                            showTargetTerm = "&nbsp";
                                        }
                                        else {
                                            showTargetTerm = oldtargetTerm;
                                        }
                                    }
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
                                    var targetPOS=$j("#targetPosID_" + termId + " :selected").text();
                                    var termCat = $j("#categoryId_" + termId + " :selected").text();
                                    var trmStatus = $j("#statusId_" + termId + " :selected").text();
                                    var term = (termPOS == "---Select---") ? "&nbsp;" : termPOS;
                                    var trmDomain = $j("#domainId_" + termId + " :selected").text();
                                    var showCategory = (termCat == "---Select---") ? "&nbsp;" : termCat;
                                    //var topSuggestedTerm=(suggTerm ==null || suggTerm =="")?"&nbsp;":suggTerm;
                                    var sourceTerm = $j("#sourceId_" + termId).val();// lalitha
                                    var showSourceTerm = (sourceTerm == "") ? "&nbsp;" : sourceTerm;
                                    var showDomain = (trmDomain == "---Select---") ? "&nbsp;" : trmDomain;
                                    var status = (trmStatus == "---Select---") ? "&nbsp;" : trmStatus;
                                    row.find('.tPartsOfSpeech').html(term).show();
                                    row.find('.tCategory').html(showCategory).show();
                                    row.find('.tDomain').html(showDomain).show();
                                    row.find('.targetTrm').html(showTargetTerm).show();
                                    row.find(".pollTerm").attr("title", sourceTerm);
                                    var setSourceTerm = "";
                                    if (data.deprecatedTermInfo.length > 0) {
                                        if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                                            setSourceTerm = "<span style='font-weight:bold;font-size:20px;color:red'><b>!</b></span>" + showSourceTerm;
                                        } else {
                                        	setSourceTerm = "<span style='font-weight:bold;font-size:17px;color:red'><b>!</b></span>" + showSourceTerm;
                                        }
                                    } else {
                                    	setSourceTerm = sourceTerm;
                                    }
                                    row.find('.pollTerm').html(setSourceTerm).show();
                                    row.find('.tstatus').html(status).show();
                                } else {
                                    $j("#errorId_" + termId).show();
                                    $j("#sourceId_" + termId).addClass('errorBorder');
                                }
                                row.find('.targetTrm').each(function (i) {
                                    var origText = $j(this).text();
                                    if (searchBy != null && searchBy != "Enter term to search...") {
                                        var searchText = searchBy.replace(/\s\s+/g, ' ')
                                        if (origText.length > 8) {
                                            var finalText = origText.substring(0, 6);
                                            finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                            finalText = finalText + "...";
                                            $j(this).html(finalText);
                                            $j(this).attr("title", origText);
                                        } else {
                                            var origText = $j(this).text();
                                            origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                            $j(this).html(origText);
                                        }
                                    } else {
                                        if (origText.length > 10) {
                                            var finalText = origText.substring(0, 8);
                                            finalText = finalText + "...";
                                            $j(this).text(finalText);
                                            $j(this).attr("title", origText);
                                        }
                                    }
                                });

                                row.find('.tCategory').each(function (i) {
                                    var origText = $j(this).text();
                                    if (origText.length > 14) {
                                        var finalText = origText.substring(0, 12);
                                        finalText = finalText + "...";
                                        $j(this).text(finalText);
                                        $j(this).attr("title", origText);
                                    }
                                });
                                row.find('.tPartsOfSpeech').each(function (i) {
                                    var origText = $j(this).text();
                                    if (origText.length > 14) {
                                        var finalText = origText.substring(0, 12);
                                        finalText = finalText + "...";
                                        $j(this).text(finalText);
                                        $j(this).attr("title", origText);
                                    }
                                });
                                row.find('.tDomain').each(function (i) {
                                    var origText = $j(this).text();
                                    if (origText.length > 14) {
                                        var finalText = origText.substring(0, 12);
                                        finalText = finalText + "...";
                                        $j(this).text(finalText);
                                        $j(this).attr("title", origText);
                                    }
                                });
                            });
                            detailsSec.find('.editSourceTerm').click(function () {
                                //	$j('.editSourceTerm').removeClass('errorBorder');
                                $j("#sourceId_" + termId).removeClass('errorBorder');
                                $j("#errorId_" + termId).hide();
                            });
                            //Extend Poll

                            detailsSec.find('.extendPoll').click(function () {
                                if ((row.find('.viewDate').html()) == "") {
                                    alertMessage("#termExtendMsg");
                                } else {
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
                                if (Util.isDate(editedDate)) {
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
                                    if (searchBy != null && searchBy != "Enter term to search...") {
                                        var searchText = searchBy.replace(/\s\s+/g, ' ')
                                        if (origText.length > 14) {
                                            var finalText = origText.substring(0, 12);
                                            finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                            finalText = finalText + "...";
                                            $j(this).html(finalText);
                                            $j(this).attr("title", origText);
                                        } else {
                                            var origText = $j(this).text();
                                            origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                            $j(this).html(origText);
                                        }
                                    } else {
                                        if (origText.length > 10) {
                                            var finalText = origText.substring(0, 8);
                                            finalText = finalText + "...";
                                            $j(this).text(finalText);
                                            $j(this).attr("title", origText);
                                        }
                                    }
                                });
                                row.find('.editDetailsFld').hide();
                                row.find('.viewDetailsFld').show();
                                row.find('.editDate').hide();
                                row.find('.viewDate').show();
                                row.find(".targetTrm").text($j(this).parent().find(".termSuggestion").html());
                                row.find(".tstatus").text("Approved");
                            });
                            detailsSec.find('.votingDetails').click(function () {
                                var termId = row.attr('termId');
                                if ($j('#votiongDetailsTab').length > 0) {
                                	var count = 0;
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
                                            { "mDataProp": "userComments", "sWidth": "25%",
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
            
           
          $j("#votiongDetailsDiv").click(function () {
        	   if($j('.subdata').hasClass('test'))
        	   {
        		 //   $(this).qtip('destroy');
        		   $(".qtip").remove();
        		    $j('.subdata').removeClass("test");
        	   }
           });
         
            $j("#showSelectedTerms").unbind('click').click(function (e) {
                e.stopPropagation();
                //$j("#paginationId").val($j("#paginationId option:first").val()); 
                if ($j('#showSelectedTerms').attr('checked')) {
                    $j("#selectAll").attr('checked', true);
                    $j("#termlanguageSlct").multiselect("uncheckAll");
                    $j("#companyTermsSlct").multiselect("uncheckAll");
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
                    triggerSelectedTermDetails(null, null, uniqueSelectedTermIds);
                } else {
                	/* if($j("#paginationId").val() != 'Select') {
                		 paginationVal = true;
                	 }*/
                    selectedLangIdArray = langValues;
                    selectedCompanyIdsArray = companyValues;
                    if (companyValues != null && selectedCompanyIdsArray.length > 0 && langValues == null /*&& selectedLangIdArray.length==0*/) {
                        for (var i = 0; i < selectedCompanyIdsArray.length; i++) {
                            $j("#companyTermsSlct").multiselect("widget").find(":checkbox[value='" + selectedCompanyIdsArray[i] + "']").attr("checked", "checked");
                            $j("#companyTermsSlct option[value='" + selectedCompanyIdsArray[i] + "']").attr("selected", 1);
                            $j("#companyTermsSlct").multiselect("refresh");
                        }
                        triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, null, null, 'Company', selectedCompanyIdsArray, savedCriteria.isTM);

                    } else if (langValues != null && selectedLangIdArray.length > 0 && companyValues == null /*&& selectedCompanyIdsArray.length==0*/) {
                        for (var i = 0; i < selectedLangIdArray.length; i++) {
                            $j("#termlanguageSlct").multiselect("widget").find(":checkbox[value='" + selectedLangIdArray[i] + "']").attr("checked", "checked");
                            $j("#termlanguageSlct option[value='" + selectedLangIdArray[i] + "']").attr("selected", 1);
                            $j("#termlanguageSlct").multiselect("refresh");
                        }
                        triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, 'Locale', selectedLangIdArray, savedCriteria.filterByCompany, savedCriteria.selectedCompanyIds, savedCriteria.isTM);

                    } else if (companyValues != null && selectedCompanyIdsArray.length > 0 && langValues != null && selectedLangIdArray.length > 0) {
                        for (var i = 0; i < selectedLangIdArray.length; i++) {
                            $j("#termlanguageSlct").multiselect("widget").find(":checkbox[value='" + selectedLangIdArray[i] + "']").attr("checked", "checked");
                            $j("#termlanguageSlct option[value='" + selectedLangIdArray[i] + "']").attr("selected", 1);
                            $j("#termlanguageSlct").multiselect("refresh");
                        }
                        for (var i = 0; i < selectedCompanyIdsArray.length; i++) {
                            $j("#companyTermsSlct").multiselect("widget").find(":checkbox[value='" + selectedCompanyIdsArray[i] + "']").attr("checked", "checked");
                            $j("#companyTermsSlct option[value='" + selectedCompanyIdsArray[i] + "']").attr("selected", 1);
                            $j("#companyTermsSlct").multiselect("refresh");
                        }
                        triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, 'Locale', selectedLangIdArray, 'Company', selectedCompanyIdsArray, savedCriteria.isTM);
                    } /*else if(paginationVal) {
                         $j('#termDtlRowsList').empty();
                    	scrollLimitFrom = 0;
                    	triggerTermBaseByPagination(null, null, 0, null, null, null, null, null, null, null);              
                    	}*/
                    else {
                        triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, null, null, savedCriteria.filterByCompany, savedCriteria.selectedCompanyIds, savedCriteria.isTM);
                    }
                    if (isDivClicked == true && savedCriteria.sortOrder == "DESC") {

                        $j('#mngTrmDtlSectionHead').find('.sort').remove();
                        $j("#" + savedCriteria.colName).append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
                    }
                    if (isDivClicked == true && savedCriteria.sortOrder == "ASC") {

                        $j('#mngTrmDtlSectionHead').find('.sort').remove();
                        $j("#" + savedCriteria.colName).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
                    }
                }
            });
        };

        ctx.bind("showTermBaseDetails", function (event, criteria) {
            Util.startWaiting("&nbsp;Loading ... please wait", $j('#termDtlRowsList'), false);
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
            queryAppender.filterByCompany = criteria.filterByCompany;
            queryAppender.selectedCompanyIds = criteria.selectedCompanyIds;
            queryAppender.isTM = criteria.isTM;
            var searchStr = criteria.searchStr;
            var filterBy = criteria.filterBy;
            var filterByCompany = criteria.filterByCompany;
            searchString = criteria.searchStr;
            var queryAppenderParameter = Object.toJSON(queryAppender);
            $t.getAllTerms(queryAppenderParameter, {
                success: function (data) {
                    $j('#termDtlRowsList').empty();
                    compSelectedVal = [];
                    langSelectedVal = [];
                    alertMessage("#deleteTerms");
                    $j("#companyTermsSlct").multiselect("refresh");
                    $j("#termlanguageSlct").multiselect("refresh");
                    $j("#companyTermsSlct").multiselect("uncheckAll");
                    $j("#termlanguageSlct").multiselect("uncheckAll");
                    $j("#newTerm").val("Enter term to search...");
                    triggerTermDetails(null, null, 0, null, false, null, null, null, null, 'N');
                    $t.getTermsInGlossary({
                        success: function (data) {
                            var d = new Date();
                            var termsPerYear = data;
                            var dataXML = new Array();
                            if ($j("#hpsite").val() == "true")
                                dataXML.push("<chart showValues='0' showBorder='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0'  bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
                            else
                                dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
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
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });
        });
        ctx.bind("showTermDetailsForPagination",function (event, criteria) {
        	Util.startWaiting("&nbsp;Loading ... please wait", $j('#termDtlRowsList'), false);
        	//$j("#pagination").hide();
        	var caseFlag = false;
        	if ($j("#chkCase").attr('checked')) {
        		caseFlag = true;
        	}
            var pageNum = criteria.pageNum;
        	var queryAppender = new Object();
        	queryAppender.colName = criteria.colName;
        	queryAppender.sortOrder = criteria.sortOrder;
        	queryAppender.pageNum = criteria.pageNum;
        	queryAppender.searchStr = criteria.searchStr;
        	queryAppender.caseFlag = criteria.caseSensitiveFlag;
        	queryAppender.filterBy = criteria.filterBy;
        	queryAppender.selectedIds = criteria.selectedIds;
        	queryAppender.filterByCompany = criteria.filterByCompany;
        	queryAppender.selectedCompanyIds = criteria.selectedCompanyIds;
        	queryAppender.isTM = criteria.isTM;
        	queryAppender.scrollLimitFrom = criteria.scrollLimitFrom;   
        	queryAppender.scrollLimitTo = criteria.scrollLimitTo;    
        	var searchStr = criteria.searchStr;
        	var filterBy = criteria.filterBy;
        	var filterByCompany = criteria.filterByCompany;
        	searchString = criteria.searchStr;
        	var queryAppenderParameter = Object.toJSON(queryAppender);
        	$t.getManagePollTermsForPagination(queryAppenderParameter, {
        		success: function (data) {
        			//$j('#termDtlRowsList').empty();
        			  scrollLimitFrom = scrollLimitFrom + scrollLimitTo;
        			var termDetails = data.pollTermsList;
        			if (termDetails == null) {
        				if( $j('#termDtlRowsList').html() != "" ) {
        					console.log("It is not empty");
        					$j("#termDtlRowsList").find('#noMoreData').hide();
        					Util.stopWaiting($j('#termDtlRowsList'));
        					displayCriteria = "No more data to display";
        					$j("#termDtlRowsList").append('<span id="noMoreData" style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">' + displayCriteria + '</span>');
        					return;
        				}
        				var displayCriteria = $j("#newTerm").val();
        				if (displayCriteria == "Enter term to search...") {
        					displayCriteria = "No data to display";
        				} else {
        					displayCriteria = 'No matches found for term : "<span class="bold">' + $j("#newTerm").val() + '</span>"';
        				}

        				$j("#termDtlRowsList").html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">' + displayCriteria + '</span>');
        				$j("#pagination").hide();
        			} else {
        				displayTermDetailsForPagination(data);
                        var length = data.pollTermsList.length;
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
        				
        				var totalTerms3 = Util.insertCommmas(new String(totalTerms));
        				$j("#totalPolledTerms").html(totalTerms3);
        				bindEvents();
        				bindEvent();
        				var searchBy = $j("#newTerm").val();
        				searchBy = $j.trim(searchBy);
        				searchBy = searchBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
        				searchText = searchBy.replace(/\s\s+/g, ' ');
        				searchText = $j.trim(searchText);
        				$j('div.pollTerm').each(function (i) {
        					var origText = $j(this).text();
        					if (searchBy != null && searchBy != "Enter term to search") {
        						if (origText.lastIndexOf("!") > -1) {
        							//srcText = origText.substr(0, origText.lastIndexOf("!"));
        							srcText = origText;
        							var supscript = "";
        							if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
        								supscript = "<span style='font-weight:bold;font-size:20px; color:red'><b>!</b></span>";
        							} else {
        								supscript = "<span style='font-weight:bold;font-size:15px; color:red'><b>!</b></span>";
        							}
        							if (srcText.length > 10) {
        								var finalText = srcText.substring(1, 8);
        								finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
        								finalText = finalText + "...";
        								$j(this).html(supscript+finalText);
        								$j(this).attr("title", srcText);
        							} else {
        								srcText = Util.backgroundYellow(srcText, searchText, null, caseFlag);
        								srcText=srcText.substring(1, srcText.length);
        								$j(this).html(supscript+srcText);
        							}
        						} else {
        							$j(this).addClass("toppadding5");
        							if (origText.length > 10) {
        								var finalText = origText.substring(0, 8);
        								finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
        								finalText = finalText + "...";
        								$j(this).html(finalText);
        								$j(this).attr("title", origText);
        							} else {
        								var origText = $j(this).text();
        								origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
        								$j(this).html(origText);
        							}
        						}
        					} else {
        						/*if (origText.length > 10) {
                            var finalText = origText.substring(0, 8);
                            finalText = finalText + "...";
                            $j(this).text(finalText);
                            $j(this).attr("title", origText);
                        }*/
        					}
        				});

        				$j('div.targetTrm').each(function (i) {
        					var origText = $j(this).text();
        					if (searchBy != null && searchBy != "Enter term to search...") {
        						if (origText.length > 10) {
        							var finalText = origText.substring(0, 8);
        							finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
        							finalText = finalText + "...";
        							$j(this).html(finalText);
        							$j(this).attr("title", origText);
        						} else {
        							var origText = $j(this).text();
        							origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
        							$j(this).html(origText);
        						}
        					} else {
        						if (origText.length > 10) {
        							var finalText = origText.substring(0, 8);
        							finalText = finalText + "...";
        							$j(this).text(finalText);
        							$j(this).attr("title", origText);
        						}
        					}
        				});
        				$j('div.tCategory').each(function (i) {
        					var origText = $j(this).text();
        					if (origText.length > 14) {
        						var finalText = origText.substring(0, 12);
        						finalText = finalText + "...";
        						$j(this).text(finalText);
        						$j(this).attr("title", origText);
        					}
        				});
        				$j('div.tPartsOfSpeech').each(function (i) {
        					var origText = $j(this).text();
        					if (origText.length > 14) {
        						var finalText = origText.substring(0, 12);
        						finalText = finalText + "...";
        						$j(this).text(finalText);
        						$j(this).attr("title", origText);
        					}
        				});
        				$j('div.tDomain').each(function (i) {
        					var origText = $j(this).text();
        					if (origText.length > 14) {
        						var finalText = origText.substring(0, 12);
        						finalText = finalText + "...";
        						$j(this).text(finalText);
        						$j(this).attr("title", origText);
        					}
        				});
        				$j("#selectAll").click(function (event) {
        					event.stopPropagation();
        					$j('.case').attr('checked', this.checked);
        					if (!$j('#showSelectedTerms').attr('checked')) {
        						$j("#showSelectedTerms").attr('checked', false);
        					}
        					$j("#termDtlRowsList input:checkbox").each(function (i) {
        						if ($j(this).attr('checked') != "checked") {
        							var tempIds = "";
        							var separator = "";
        							if (selectedTermIds != "") {
        								var temptermIdsArray = selectedTermIds.split(",");
        								for (var j = 0; j < temptermIdsArray.length; j++) {
        									if (temptermIdsArray[j] != ($j(this).parent().next().attr("termId"))) {
        										tempIds = tempIds + separator + temptermIdsArray[j];
        										separator = ",";
        									}
        								}
        							}
        							selectedTermIds = tempIds;
        						}
        					});

        					$j("#termDtlRowsList input:checked").each(function (i) {
        						if (selectedTermIds == "") {
        							separator = "";
        						} else {
        							separator = ",";
        						}
        						selectedTermIds += separator + $j(this).parent().next().attr("termId");
        					});

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
        				});
        				$j('#termDtlRowsList input[type="checkbox"]').click(function () {
        					var countcheck = $j('#termDtlRowsList input[type="checkbox"]:checked').length;
        					if (countcheck != length) {
        						$j("#selectAll").attr("checked", false);
        					} else {
        						$j("#selectAll").attr("checked", true);
        					}
        				});

        				$j(".rowBgForAdmin .row  .deleteImg").hover(function () {
        					modalRender.bubble(".rowBg .row .deleteImg", "Delete", "left center", "right center");
        				});

        				$j(".rowBgForAdmin .row .deleteImg").click(function (event) {
        					var userRole = "";
        					selectedTermIds = "";
        					if ($j("#termDtlRowsList input:checked").length == 0 || $j("#termDtlRowsList input:checked").length == 1) {
        						var id = $j(this).attr("imgId");
        						deleteVal(id);
        					}
        					event.stopPropagation();
        				});
        				$j(".rowBg .row .modifyImg").hover(function () {
        					modalRender.bubble(".rowBg .row .modifyImg", "Edit", "left center", "right center");
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
        			buttonImage: $j("#contextRootPath").val() + "/images/calendar.gif",
        			buttonImageOnly: true
        		});
        	});

});
        ctx.bind("showTermDetails", function (event, criteria) {
            Util.startWaiting("&nbsp;Loading ... please wait", $j('#termDtlRowsList'), false);
            $j("#pagination").hide();
            var caseFlag = false;
            if ($j("#chkCase").attr('checked')) {
                caseFlag = true;
            }
      /*      $t.getTermsInGlossary({
                success: function (data) {
                    var d = new Date();
                    var termsPerYear = data;
                    var dataXML = new Array();
                    if ($j("#hpsite").val() == "true")
                        dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' baseFont='HP Simplified' lineColor='#6ab53a'>");
                    else
                        dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
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
            });*/
            var pageNum = criteria.pageNum;
            var queryAppender = new Object();
            queryAppender.colName = criteria.colName;
            queryAppender.sortOrder = criteria.sortOrder;
            queryAppender.pageNum = criteria.pageNum;
            queryAppender.searchStr = criteria.searchStr;
            queryAppender.caseFlag = criteria.caseSensitiveFlag;
            queryAppender.filterBy = criteria.filterBy;
            queryAppender.selectedIds = criteria.selectedIds;
            queryAppender.filterByCompany = criteria.filterByCompany;
            queryAppender.selectedCompanyIds = criteria.selectedCompanyIds;
            queryAppender.isTM = criteria.isTM;
            queryAppender.paginationValue=criteria.paginationValue;
            var searchStr = criteria.searchStr;
            var filterBy = criteria.filterBy;
            var filterByCompany = criteria.filterByCompany;
            searchString = criteria.searchStr;
            var queryAppenderParameter = Object.toJSON(queryAppender);
            $t.getManagePollTerms(queryAppenderParameter, {
                success: function (data) {
                    $j('#termDtlRowsList').empty();
                    var termDetails = data.pollTermsList;
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
//						if((searchStr != null || filterBy != null || filterByCompany!=null ) && pageNum == 0 ){
//						totalTerms = termDetails.length;
//							}else if(searchStr == null  && filterBy == null && filterByCompany== null){
//							//totalTerms = $j("#termsInGlossary").val();
//							totalTerms = parseInt(totalTerms);
//							alert(totalTerms);
//							
//						}
////					
                        totalTerms = data.totalResults
                        totalTerms = parseInt(totalTerms);
                        if(selectValue != null) {
                        	if(selectValue == 25) {
                           	    noOfPages = Math.round(totalTerms / 25);
                                noOfPages = (totalTerms % 25 < 12 && totalTerms % 25 > 0) ? noOfPages + 1 : noOfPages;

                                if (pageNum == 0) {
                                    startLimit = 1;
                                    endLimit = (termListLimit > totalTerms) ? totalTerms : termListLimit;
                                } else {
                                    startLimit = ((pageNum - 1) * termListLimit) + 1;
                                    var tempLimit = (pageNum) * termListLimit;
                                    endLimit = (parseInt(tempLimit) > parseInt(totalTerms)) ? totalTerms : tempLimit;
                                }
                           
                        	}
                        	if(selectValue == 50) {
                           	    noOfPages = Math.round(totalTerms / 50);
                                noOfPages = (totalTerms % 50 < 25 && totalTerms % 50 > 0) ? noOfPages + 1 : noOfPages;

                                if (pageNum == 0) {
                                    startLimit = 1;
                                    endLimit = (termListLimit > totalTerms) ? totalTerms : termListLimit;
                                } else {
                                    startLimit = ((pageNum - 1) * termListLimit) + 1;
                                    var tempLimit = (pageNum) * termListLimit;
                                    endLimit = (parseInt(tempLimit) > parseInt(totalTerms)) ? totalTerms : tempLimit;
                                }
                           
                        	}
                        	if(selectValue == 100) {
                           	    noOfPages = Math.round(totalTerms / 100);
                                noOfPages = (totalTerms % 100 < 50 && totalTerms % 100 > 0) ? noOfPages + 1 : noOfPages;

                                if (pageNum == 0) {
                                    startLimit = 1;
                                    endLimit = (termListLimit > totalTerms) ? totalTerms : termListLimit;
                                } else {
                                    startLimit = ((pageNum - 1) * termListLimit) + 1;
                                    var tempLimit = (pageNum) * termListLimit;
                                    endLimit = (parseInt(tempLimit) > parseInt(totalTerms)) ? totalTerms : tempLimit;
                                }
                           
                        	}
                        	if(selectValue == 10) {
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
                        }
                        } else {
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
                        var totalTerms3 = Util.insertCommmas(new String(totalTerms));
                        $j("#totalPolledTerms").html(totalTerms3);
//							$j("#totalPolledTerms").html(totalResultedTerms);
                        pagination(noOfPages, pageNum);
                        bindEvents();
                        bindEvent();
                        var searchBy = $j("#newTerm").val();
                        searchBy = $j.trim(searchBy);
                        searchBy = searchBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
                        searchText = searchBy.replace(/\s\s+/g, ' ');
                        searchText = $j.trim(searchText);
                        $j('div.pollTerm').each(function (i) {
                            var origText = $j(this).text();
                            if (searchBy != null && searchBy != "Enter term to search") {
                                if (origText.lastIndexOf("!") > -1) {
                                    //srcText = origText.substr(0, origText.lastIndexOf("!"));
                                	srcText = origText;
                                    var supscript = "";
                                    if ($j.browser.version == "8.0" || $j.browser.version == "9.0") {
                                        supscript = "<span style='font-weight:bold;font-size:20px; color:red'><b>!</b></span>";
                                    } else {
                                        supscript = "<span style='font-weight:bold;font-size:15px; color:red'><b>!</b></span>";
                                    }
                                    if (srcText.length > 10) {
                                        var finalText = srcText.substring(1, 8);
                                        finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                        finalText = finalText + "...";
                                        $j(this).html(supscript+finalText);
                                        $j(this).attr("title", srcText);
                                    } else {
                                        srcText = Util.backgroundYellow(srcText, searchText, null, caseFlag);
                                        srcText=srcText.substring(1, srcText.length);
                                        $j(this).html(supscript+srcText);
                                    }
                                } else {
                                    $j(this).addClass("toppadding5");
                                    if (origText.length > 10) {
                                        var finalText = origText.substring(0, 8);
                                        finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                        finalText = finalText + "...";
                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    } else {
                                        var origText = $j(this).text();
                                        origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                        $j(this).html(origText);
                                    }
                                }
                            } else {
                                /*if (origText.length > 10) {
                                    var finalText = origText.substring(0, 8);
                                    finalText = finalText + "...";
                                    $j(this).text(finalText);
                                    $j(this).attr("title", origText);
                                }*/
                           	}
                        });

                        $j('div.targetTrm').each(function (i) {
                            var origText = $j(this).text();
                            if (searchBy != null && searchBy != "Enter term to search...") {
                                if (origText.length > 10) {
                                    var finalText = origText.substring(0, 8);
                                    finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                    finalText = finalText + "...";
                                    $j(this).html(finalText);
                                    $j(this).attr("title", origText);
                                } else {
                                    var origText = $j(this).text();
                                    origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                    $j(this).html(origText);
                                }
                            } else {
                                if (origText.length > 10) {
                                    var finalText = origText.substring(0, 8);
                                    finalText = finalText + "...";
                                    $j(this).text(finalText);
                                    $j(this).attr("title", origText);
                                }
                            }
                        });
                        $j('div.tCategory').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 14) {
                                var finalText = origText.substring(0, 12);
                                finalText = finalText + "...";
                                $j(this).text(finalText);
                                $j(this).attr("title", origText);
                            }
                        });
                        $j('div.tPartsOfSpeech').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 14) {
                                var finalText = origText.substring(0, 12);
                                finalText = finalText + "...";
                                $j(this).text(finalText);
                                $j(this).attr("title", origText);
                            }
                        });
                        $j('div.tDomain').each(function (i) {
                            var origText = $j(this).text();
                            if (origText.length > 14) {
                                var finalText = origText.substring(0, 12);
                                finalText = finalText + "...";
                                $j(this).text(finalText);
                                $j(this).attr("title", origText);
                            }
                        });
                        $j("#selectAll").click(function (event) {
                            event.stopPropagation();
                            $j('.case').attr('checked', this.checked);
                            if (!$j('#showSelectedTerms').attr('checked')) {
                                $j("#showSelectedTerms").attr('checked', false);
                            }
                            $j("#termDtlRowsList input:checkbox").each(function (i) {
                                if ($j(this).attr('checked') != "checked") {
                                    var tempIds = "";
                                    var separator = "";
                                    if (selectedTermIds != "") {
                                        var temptermIdsArray = selectedTermIds.split(",");
                                        for (var j = 0; j < temptermIdsArray.length; j++) {
                                            if (temptermIdsArray[j] != ($j(this).parent().next().attr("termId"))) {
                                                tempIds = tempIds + separator + temptermIdsArray[j];
                                                separator = ",";
                                            }
                                        }
                                    }
                                    selectedTermIds = tempIds;
                                }
                            });

                            $j("#termDtlRowsList input:checked").each(function (i) {
                                if (selectedTermIds == "") {
                                    separator = "";
                                } else {
                                    separator = ",";
                                }
                                selectedTermIds += separator + $j(this).parent().next().attr("termId");
                            });

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
                        });
                        $j('#termDtlRowsList input[type="checkbox"]').click(function () {
                            var countcheck = $j('#termDtlRowsList input[type="checkbox"]:checked').length;
                            if (countcheck != length) {
                                $j("#selectAll").attr("checked", false);
                            } else {
                                $j("#selectAll").attr("checked", true);
                            }
                        });

                        $j(".rowBg .row  .deleteImg").hover(function () {
                            modalRender.bubble(".rowBg .row .deleteImg", "Delete", "left center", "right center");
                        });

                        $j(".rowBg .row .deleteImg").click(function (event) {
                            var userRole = "";
                            selectedTermIds = "";
                            if ($j("#termDtlRowsList input:checked").length == 0 || $j("#termDtlRowsList input:checked").length == 1) {
                                var id = $j(this).attr("imgId");
                                deleteVal(id);
                            }
                            event.stopPropagation();
                        });
                        $j(".rowBg .row .modifyImg").hover(function () {
                            modalRender.bubble(".rowBg .row .modifyImg", "Edit", "left center", "right center");
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
                    buttonImage: $j("#contextRootPath").val() + "/images/calendar.gif",
                    buttonImageOnly: true
                });
            });
        });

        ctx.bind("showSelectedTermDetails", function (event, criteria) {
            Util.startWaiting("&nbsp; Loading ... please wait", $j('#termDtlRowsList'), false);
            $j("#pagination").hide();
            var queryAppender = new Object();
            queryAppender.colName = criteria.colName;
            queryAppender.sortOrder = criteria.sortOrder;
            queryAppender.termSelectedIds = criteria.selectedIds;
            var queryAppenderParameter = Object.toJSON(queryAppender);

            if (selectedTermIds == "") {
                paginationVal =  false;
                $j('#termDtlRowsList').empty();
                var displayCriteria = "No term Selected";
                $j("#termDtlRowsList").html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">' + displayCriteria + '</span>');
                $j("#pagination").hide();
            } else {
                $t.getSelectedTermsOnly(queryAppenderParameter, {
                    success: function (selecteddata) {
                        var termDetails = selecteddata.pollTermsList;
                        $j('#termDtlRowsList').empty();
                        displaySelectedTermDetails(selecteddata);
                        bindEvents();
                        bindEvent();
                        var length = termDetails.length;
                        for (var i = 0; i < length; i++) {
                            var dataXML = new Array();
                            var invites = (termDetails[i].invites == null) ? 0 : termDetails[i].invites;
                            var votesPerTerm = (termDetails[i].votesPerTerm == null) ? 0 : termDetails[i].votesPerTerm;
                            var randomNumber = Math.floor(Math.random() * (new Date()).getTime() + 1);

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
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });
            }

            $j(function () {
                $j("#datepicker").datepicker({
                    showOn: "button",
                    buttonImage: "images/calendar.gif",
                    buttonImageOnly: true
                });
            });
        });
    };

    $j("#uploadTMxId").click(function () {
        $j("#uploadTMXFormId").toggle();
        $j("#uploadTMxId").toggle();
        $j("#importTMXTerm").val("");
    });

    $j(window).bind('keydown', function (e) {
        if (e.which == 27) {
            if (loadingFlag) {
                showLoadingDialog();
                $j(".ui-dialog-titlebar").hide();
            }
        }
    });

    var fileUpload = function (form, action_url, div_id) {
        $j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif"  align="top" class="rightmargin10" />&nbsp;Uploading....  Please wait..</div>');
        showLoadingDialog();
        $j(".ui-dialog-titlebar").hide();
        loadingFlag = true;
        // Create the iframe...
        var iframe = document.createElement("iframe");
        iframe.setAttribute("id", "upload_iframe" + tempId);
        iframe.setAttribute("name", "upload_iframe" + tempId);
        iframe.setAttribute("width", "0");
        iframe.setAttribute("height", "0");
        iframe.setAttribute("border", "0");
        iframe.setAttribute("style", "width: 0; height: 0; border: none;");

        // Add to document...
        form.parentNode.appendChild(iframe);
        window.frames['upload_iframe' + tempId].name = "upload_iframe" + tempId;

        iframeId = document.getElementById("upload_iframe" + tempId);

        // Add event...
        var eventHandler = function () {
            if (iframeId.detachEvent) iframeId.detachEvent("onload", eventHandler);
            else iframeId.removeEventListener("load", eventHandler, false);

            // Message from server...
            if (iframeId.contentDocument) {
                var content = iframeId.contentDocument.body.innerHTML;
            } else if (iframeId.contentWindow) {
                var content = iframeId.contentWindow.document.body.innerHTML;
            } else if (iframeId.document) {
                var content = iframeId.document.body.innerHTML;
            }
        }

        if (iframeId.addEventListener) iframeId.addEventListener("load", eventHandler, true);
        if (iframeId.attachEvent) iframeId.attachEvent("onload", eventHandler);

        // Set properties of form...
        form.setAttribute("target", "upload_iframe" + tempId);
        form.setAttribute("action", action_url);
        form.setAttribute("method", "post");
        form.setAttribute("enctype", "multipart/form-data");
        form.setAttribute("encoding", "multipart/form-data");

        // Submit the form...
        form.submit();

        tempId++;
        // document.getElementById("uploadTMXFormId").style.display="none";
        document.getElementById("uploadTMxId").style.display = "none";
        // document.getElementById(div_id).innerHTML = "Uploading..."+tempId;
    };

    var tmxsubmit = function (form) {
        if ($j.trim($j("#importTMXTerm").val()) != "" && $j.trim($j("#importTMXTerm").val()).indexOf('.tmx') != -1) {
            //startLoading("message", "Uploading... please wait");
            var fileName = $j("#importTMXTerm").val();
            var ext = "xml";
            if (fileName != null && fileName.lastIndexOf(".") > 0)
                ext = fileName.substr(fileName.lastIndexOf(".") + 1);
            if (fileName != null && fileName.lastIndexOf('\\') > 0)
                fileName = fileName.substr(fileName.lastIndexOf('\\') + 1);
            var type = "Import";
            $t.saveImportTMXFile(fileName, ext, type, {
                success: function (data) {
                    statusId = data.fileUploadStatusId;
                    $j("#fileId").val(data.fileUploadStatusId);
                    uploadStatusTMX();
                    /*$j("#ImportTMXForm").attr("action",  $j("#contextRootPath").val()+"/impExp_serv?fileId="+data.fileUploadStatusId);
                     $j("#ImportTMXForm").attr("ENCTYPE", "multipart/form-data");

                     $j("#ImportTMXForm").submit();*/
                    fileUpload(form, $j("#contextRootPath").val() + '/impExp_serv', 'uploadTMxId');
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });
        } else if ($j.trim($j("#importTMXTerm").val()) != "" && $j.trim($j("#importTMXTerm").val()).indexOf('.tmx') == -1) {
            $j("#importTMXTerm").val('');
            alertMessage("#invalidFileFormat");
        } else {
            alertMessage("#importMessage");
        }

    };

    $j.fn.tmDetails = function () {
        var ctx = $j(this);
        var tmDtlTmpl = ['<div class="rowBg" style="font-size:11px;"><div class="chkBx"><input type="checkbox" class="floatleft case" value="test" /></div> <div  class="row twistie_close" termId="',
            '',
            '" id="rowid_',
            '',
            '"><div class="width170 bigFont  toppadding5 sourceTm">',
            '',
            '</div><div class="width130 bigFont toppadding5 targetTm">',
            '',
            '</div><div class="width90 toppadding5 tmLanguage smallFont">',
            '',
            '</div><div class="width110  toppadding5 productLine">',
            '',
            '</div><div class="width110  toppadding5 industryDomain">',
            '',
            '</div><div class="width110 viewCompany nodisplay toppadding5 company">',
            '',
            '</div><div class="width110  toppadding5 contentType">',
            '',
            '</div><div class="width40 toppadding15 modify"><img class="headerMenuLink modifyTmImg "  height="20px" width="20px" src="' + $j("#contextRootPath").val() + '/images/Pencil1.png" editId="',
            '',
            '" /></div><div class="width30 toppadding15 delete" ><img class="headerMenuLink deleteTmImg" height="20px" width="20px"  src="' + $j("#contextRootPath").val() + '/images/DeleteIcon1.png" imgId="',
            '',
            '" /></div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay" style="min-height:170px;" id="overView" termId="',
            '',
            '"><div class="loadingDiv" style="text-align: center;"><img src=' + $j("#contextRootPath").val() + '/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
        ];


        var tmAttrTmpl = '<div class="termAttr"><div><div><div class="bold sourceTerm label" >Source: </div> <div class="sourceDesc viewDetailsFld" style="word-wrap:break-word;"></div><div  name="editSourceTerm"  id=#{sourceId} class="sourceDescEdit editDetailsFld nodisplay " style="word-wrap:break-word;"></div><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class=" bold targetTerm label" >Target: </div> <div class="targetDesc viewDetailsFld" style=" word-wrap: break-word;"></div><textarea rows="3" cols="44"   name="editTargetTerm"  id=#{targetId} class="targetDescEdit editDetailsFld nodisplay editTargetTerm"  value="" style=" word-wrap: break-word;"></textarea><div class="clear"></div>'
            + '</div></div><div class="left"><div class="topmargin5"><div class="bold label">Product Line: </div><span class="productDesc viewDetailsFld"></span><select name="editProductLine" id=#{productId} class="editDetailsFld  productDescEdit nodisplay   editProductLine"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div><div class="topmargin5" ><div class="bold industry label" >Industry Domain: </div> <span class="domainDesc viewDetailsFld"></span><select name="editDomain" id=#{domainId} class="editDetailsFld nodisplay editDomain domainDescEdit"><option value="">---Select---</option></select><div class="clear"></div>'
            + '</div><div class="topmargin5"><div class="bold label">Content Type: </div><span class="contentDesc viewDetailsFld"></span><select name="editContent" id=#{contentId} class="editDetailsFld  contentDescEdit nodisplay editContent"><option value="">---Select---</option></select><div class="clear"></div></div></div>';


        var suggTrmListTmpl = [
            '<div class="termSlctFrm topmargin15"><span class="termSuggestion" style=" word-wrap: break-word;">',
            '',
            '</span><input type="radio" name="',
            '',
            '" /><div class="votesBar" id="',
            '',
            '">',
            '',
            '</div><input type="button" value="Pick as Final" class="pickFinalBtn nodisplay" id="',
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
        var editDetailsSec = '<div class="clear"></div><div class="editLinksSec smallFont"><a href="javascript:;" class="padding5 editDetails">Edit details</a><a href="javascript:;" class="padding5 updateDetails nodisplay">Update details</a> </div>';
        var deprecatedTermInfoSec = ['<div class="clear"></div><div class=" bigFont" style="color:red;"> <span class="width220 label deprecSource" style="color:red;padding-left:39px">Deprecated Source: ',
            '',
            '</span><div class="width50" >&nbsp;</div><span class=" width220 label depTarget">Deprecated Target: ',
            '',
            '</span></div>'
        ];
        var classNames = ['purpleBar', 'greenBar', 'yellowBar', 'pinkBar'];

        var displayTmDetails = function (data) {
            Util.stopWaiting($j('#tmDtlRowsList'));

            var tmDetails = data;

            var listLength = (tmDetails.length >= termListLimit) ? termListLimit : tmDetails.length;
            for (var count = 0; count < listLength; count++) {
                var tmDtlTmplClone = tmDtlTmpl;
                var sourceTerm = (tmDetails[count].sourceTerm == null || tmDetails[count].sourceTerm == "") ? "&nbsp;" : tmDetails[count].sourceTerm;
                var targetTerm = (tmDetails[count].targetTerm == null || tmDetails[count].targetTerm == "") ? "&nbsp;" : tmDetails[count].targetTerm;
                var tmLanguage = (tmDetails[count].targetLang == null || tmDetails[count].targetLang == "") ? "&nbsp;" : tmDetails[count].targetLang;
                var IndustryDomain = (tmDetails[count].domain == null || tmDetails[count].domain == "") ? "&nbsp;" : tmDetails[count].domain;
                var company = (tmDetails[count].company == null || tmDetails[count].company == "") ? "&nbsp;" : tmDetails[count].company;
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
                tmDtlTmplClone[15] = company;
                tmDtlTmplClone[17] = contentType;
                tmDtlTmplClone[19] = tmDetails[count].tmProfileInfoId;
                tmDtlTmplClone[21] = tmDetails[count].tmProfileInfoId;
                tmDtlTmplClone[23] = tmDetails[count].tmProfileInfoId;
                $j('#tmDtlRowsList').append(tmDtlTmplClone.join(""));
            }
            showRolePrivileges();
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

        var bindEvents = function (data) {
            var classNamesClone = classNames.slice(0);
            $j('#tmDtlRowsList .row ').click(function () {
                var row = $j(this);
                if ($j(this).hasClass('twistie_close')) {
                    $j(this).parent().next().next().show();
                    $j(this).removeClass("twistie_close");
                    $j(this).addClass("twistie_open");
                    var searchBy = $j("#newTmTerm").val();
                    searchBy = $j.trim(searchBy);
                    searchBy = searchBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
                    var searchText = searchBy.replace(/\s\s+/g, ' ');
                    searchText = $j.trim(searchText);
                    var caseFlag = false;
                    if ($j("#chktmCase").attr('checked')) {
                        caseFlag = true;
                    }
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
                                var suggTrmListTmplClone = suggTrmListTmpl;
                                var deprecatedTermInfoSecClone = deprecatedTermInfoSec;

                                if (tmInfo != null) {
                                    var tmDomain = (tmInfo.domain == null || tmInfo.domain.domain == null) ? "" : tmInfo.domain.domain;
                                    var tmContent = (tmInfo.contentType == null || tmInfo.contentType.contentType == null) ? "" : tmInfo.contentType.contentType;
                                    var tmProductLine = (tmInfo.productGroup == null || tmInfo.productGroup.product == null) ? "" : tmInfo.productGroup.product;
                                    var source = (tmInfo.source == null || tmInfo.source == null) ? "" : tmInfo.source;
                                    var target = (tmInfo.target == null || tmInfo.target == null) ? "" : tmInfo.target;
                                    detailsSec.find('.sourceDesc').html(source);
                                    detailsSec.find('.sourceDesc').each(function (i) {
                                        if (searchBy != null && searchBy != "Enter term to search...") {

                                            var sourceTermText = $j(this).html();
                                            sourceTermText = Util.backgroundYellow(sourceTermText, searchText, null, caseFlag);
                                            $j(this).html(sourceTermText);
                                        }
                                    });
                                    detailsSec.find('.sourceDescEdit').html(source);
//								detailsSec.find('.targetDesc').html(Util.wordWrap(100,"<br/>",false,target));
                                    detailsSec.find('.targetDesc').html(target);
                                    detailsSec.find('.targetDesc').each(function (i) {
                                        if (searchBy != null && searchBy != "Enter term to search...") {

                                            var targetTermText = $j(this).html();
                                            targetTermText = Util.backgroundYellow(targetTermText, searchText, null, caseFlag);
                                            $j(this).html(targetTermText);
                                        }
                                    });
                                    detailsSec.find('.targetDescEdit').html(target);
                                    detailsSec.find('.contentDesc').html(tmContent);
                                    detailsSec.find('.domainDesc').html(tmDomain);
                                    detailsSec.find('.productDesc').html(tmProductLine);

                                }
                                detailsSec.append(newTermSec.join(""));
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
                                    detailsSec.find('.sourceDescEdit').each(function (i) {
                                        if (searchBy != null && searchBy != "Enter term to search...") {
                                            var sourceTermText = $j(this).html();
                                            sourceTermText = Util.backgroundYellow(sourceTermText, searchText, null, caseFlag);
                                            $j(this).html(sourceTermText);
                                        }
                                    });
//									$j(".termPOS").html("<option>---Select---</option>");
//									$j(".termCatLst").html("<option>---Select---</option>");
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
                                    row.find('.contentType').each(function (i) {
                                        var origText = $j(this).text();
                                        if (origText.length > 10) {
                                            var finalText = origText.substring(0, 8);
                                            finalText = finalText + "...";
                                            $j(this).html(finalText);
                                            $j(this).attr("title", origText);
                                        }
                                    });

                                    row.find('.targetTm').each(function (i) {
                                        var origText = $j(this).text();
                                        if (searchBy != null && searchBy != "Enter term to search...") {

                                            if (origText.length > 16) {
                                                var finalText = origText.substring(0, 14);
                                                finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                                finalText = finalText + "...";
                                                $j(this).html(finalText);
                                                $j(this).attr("title", origText);
                                            } else {
                                                var origText = $j(this).text();
                                                origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                                $j(this).html(origText);
                                            }
                                        } else {
                                            if (origText.length > 16) {
                                                var finalText = origText.substring(0, 14);
                                                finalText = finalText + "...";
                                                $j(this).html(finalText);
                                                $j(this).attr("title", origText);
                                            }
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
                                        if (searchBy != null && searchBy != "Enter term to search...") {
                                            if (origText.length > 16) {
                                                var finalText = origText.substring(0, 14);
                                                finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                                finalText = finalText + "...";
                                                $j(this).html(finalText);
                                                $j(this).attr("title", origText);
                                            } else {
                                                var origText = $j(this).text();
                                                origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                                $j(this).html(origText);
                                            }
                                        } else {
                                            if (origText.length > 16) {
                                                var finalText = origText.substring(0, 14);
                                                finalText = finalText + "...";
                                                $j(this).html(finalText);
                                                $j(this).attr("title", origText);
                                            }
                                        }
                                    });
                                    row.find('.editDetailsFld').hide();
                                    row.find('.viewDetailsFld').show();
                                    row.find('.editDate').hide();
                                    row.find('.viewDate').show();
                                    row.find(".targetTrm").text($j(this).parent().find(".termSuggestion").html());
                                    row.find(".tstatus").text("Approved");
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

        var bindEvent = function (data) {
            var classNamesClone = classNames.slice(0);
            $j(".rowBg .row .modify .modifyTmImg").click(function (event) {
                event.stopPropagation();
                var tmProfileInfoId = $j(this).attr("editid");
                var row = $j("#rowid_" + tmProfileInfoId);
                $j("#rowid_" + tmProfileInfoId).parent().next().next().show();
                $j("#rowid_" + tmProfileInfoId).removeClass("twistie_close");
                $j("#rowid_" + tmProfileInfoId).addClass("twistie_open");
                var searchBy = $j("#newTmTerm").val();
                searchBy = $j.trim(searchBy);
                searchBy = searchBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
                var searchText = searchBy.replace(/\s\s+/g, ' ');
                searchText = $j.trim(searchText);
                var caseFlag = false;
                if ($j("#chktmCase").attr('checked')) {
                    caseFlag = true;
                }
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
                            var suggTrmListTmplClone = suggTrmListTmpl;
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
                                    if (searchBy != null && searchBy != "Enter term to search...") {
                                        var sourceTermText = $j(this).html();
                                        sourceTermText = Util.backgroundYellow(sourceTermText, searchText, null, caseFlag);
                                        $j(this).html(sourceTermText);
                                    }
                                });
                                detailsSec.find('.targetDesc').html(target);
                                detailsSec.find('.targetDescEdit').html(target);
                                detailsSec.find('.contentDesc').html(tmContent);
                                detailsSec.find('.domainDesc').html(tmDomain);
                                detailsSec.find('.productDesc').html(tmProductLine);
                            }
//								for(var i=0;i<data.suggestedTermDetails.length;i++){
//									var numRand = Math.floor(Math.random()*101);
//									suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
//									suggTrmListTmplClone[3] = row.attr('termId');
//									suggTrmListTmplClone[5] = "barId_"+row.attr('termId')+i;
//									suggTrmListTmplClone[7] = data.suggestedTermDetails[i].noOfVotes;
//									suggTrmListTmplClone[9] = data.suggestedTermDetails[i].suggestedTermId;
//									detailsSec.append(suggTrmListTmplClone.join(""));
//								}
//								newTermSec[1] = suggTrmListTmplClone[3];
//								newTermSec[3] = "sugg_"+suggTrmListTmplClone[3];
//								newTermSec[5] = "submitVote"+suggTrmListTmplClone[3];
                            detailsSec.append(newTermSec.join(""));
//								
//								if(data.deprecatedTermInfo.length>0){
//							       var	finalSource="";
//							        var finalTarget="";
//							        for(var i=0;i<data.deprecatedTermInfo.length;i++){
//							        	if(data.deprecatedTermInfo[i].deprecatedSource!=null &&  $j.trim(data.deprecatedTermInfo[i].deprecatedSource)!="")
//							        		finalSource=finalSource+data.deprecatedTermInfo[i].deprecatedSource+"," ;
//							        	if(data.deprecatedTermInfo[i].deprecatedTarget!=null &&  $j.trim(data.deprecatedTermInfo[i].deprecatedTarget)!="")
//							        		finalTarget=finalTarget+data.deprecatedTermInfo[i].deprecatedTarget+",";
//							        }
//							      
//						        finalSource=  finalSource.substring(0,finalSource.lastIndexOf(","));
//						        finalTarget=  finalTarget.substring(0,finalTarget.lastIndexOf(","));
//
//					     	deprecatedTermInfoSec[1]=finalSource;
//							deprecatedTermInfoSec[3]=finalTarget;
//							detailsSec.append(deprecatedTermInfoSecClone.join(""));
//								}

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

//									$j(".termPOS").html("<option>---Select---</option>");
//									$j(".termCatLst").html("<option>---Select---</option>");
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

                                row.find('.contentType').each(function (i) {
                                    var origText = $j(this).text();
                                    if (origText.length > 10) {
                                        var finalText = origText.substring(0, 8);
                                        finalText = finalText + "...";
                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    }
                                });
                                row.find('.targetTm').each(function (i) {
                                    var origText = $j(this).text();
                                    if (searchBy != null && searchBy != "Enter term to search...") {
                                        if (origText.length > 16) {
                                            var finalText = origText.substring(0, 14);
                                            finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                            finalText = finalText + "...";
                                            $j(this).html(finalText);
                                            $j(this).attr("title", origText);
                                        } else {
                                            var origText = $j(this).text();
                                            origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                            $j(this).html(origText);
                                        }
                                    } else {
                                        if (origText.length > 16) {
                                            var finalText = origText.substring(0, 14);
                                            finalText = finalText + "...";
                                            $j(this).html(finalText);
                                            $j(this).attr("title", origText);
                                        }
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
                                    if (searchBy != null && searchBy != "Enter term to search...") {
                                        if (origText.length > 16) {
                                            var finalText = origText.substring(0, 14);
                                            finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                            finalText = finalText + "...";
                                            $j(this).html(finalText);
                                            $j(this).attr("title", origText);
                                        } else {
                                            var origText = $j(this).text();
                                            origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                            $j(this).html(origText);
                                        }
                                    } else {
                                        if (origText.length > 16) {
                                            var finalText = origText.substring(0, 14);
                                            finalText = finalText + "...";
                                            $j(this).html(finalText);
                                            $j(this).attr("title", origText);
                                        }
                                    }
                                });
                                row.find('.contentType').each(function (i) {
                                    var origText = $j(this).text();
                                    if (origText.length > 10) {
                                        var finalText = origText.substring(0, 8);
                                        finalText = finalText + "...";
                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    }
                                });
                                row.find('.editDetailsFld').hide();
                                row.find('.viewDetailsFld').show();
                                row.find('.editDate').hide();
                                row.find('.viewDate').show();
                                row.find(".targetTrm").text($j(this).parent().find(".termSuggestion").html());
                                row.find(".tstatus").text("Approved");
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

        ctx.bind("showTmTermsDetails", function (event, criteria) {
            Util.startWaiting("&nbsp;Loading ... please wait", $j('#termDtlRowsList'), false);
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
            queryAppender.filterByCompany = criteria.filterByCompany;
            queryAppender.selectedCompanyIds = criteria.selectedCompanyIds;
            queryAppender.isTM = criteria.isTM;
            var searchStr = criteria.searchStr;
            var filterBy = criteria.filterBy;
            var filterByCompany = criteria.filterByCompany;
            searchString = criteria.searchStr;
            var queryAppenderParameter = Object.toJSON(queryAppender);
            $t.getAllTms(queryAppenderParameter, {
                success: function (data) {
                    $j('#tmDtlRowsList').empty();
                    compSelectedVal = [];
                    langSelectedVal = [];
                    $j("#newTmTerm").val("Enter term to search...");
                    $j("#tmLanguageSlct").multiselect("uncheckAll");
                    $j("#companyTmsSlct").multiselect("uncheckAll");
//							 $j("#jobSlct").multiselect("uncheckAll");
                    alertMessage("#deleteTM");
                    triggerTmDetails(null, null, 0, null, false, null, null, null, null);
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });
        });
        ctx.bind("showTmDetails", function (event, criteria) {
            Util.startWaiting("&nbsp;Loading ... please wait", $j('#tmDtlRowsList'), false);
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
            queryAppender.filterByCompany = criteria.filterByCompany;
            queryAppender.selectedCompanyIds = criteria.selectedCompanyIds;
            var caseFlag = false;
            if ($j("#chktmCase").attr('checked')) {
                caseFlag = true;
            }

            searchStringTM = criteria.searchStr;
            var searchStr = criteria.searchStr;
            var filterBy = criteria.filterBy;
            var queryAppenderParameter = Object.toJSON(queryAppender);
            $t.getTMProfileTerms(queryAppenderParameter, {
                success: function (data) {
                    if (data != null) {
                        $j('#tmDtlRowsList').empty();
                        var termDetails = data.tmProfieTermsList;
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
                            /*if((filterBy != null) ){
                             totalTerms = termDetails.length;
                             }else if((searchStr == null || searchStr != null)  && filterBy == null){
                             */	//totalTmTerms = $j("#termsInTMGlossary").val();
                            totalTmTerms = data.totalResults
                            totalTmTerms = parseInt(totalTmTerms);
                            //}
                            noOfPages = Math.round(totalTmTerms / 10);
                            noOfPages = (totalTmTerms % 10 < 5 && totalTmTerms % 10 > 0) ? noOfPages + 1 : noOfPages;

                            if (pageNum == 0) {
                                startLimit = 1;
                                endLimit = (termListLimit > totalTmTerms) ? totalTmTerms : termListLimit;
                            } else {
                                startLimit = ((pageNum - 1) * termListLimit) + 1;
                                var tempLimit = (pageNum) * termListLimit;
                                endLimit = (parseInt(tempLimit) > parseInt(totalTmTerms)) ? totalTmTerms : tempLimit;
                            }
                            displayTmDetails(termDetails);

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
                            var totalTerms3 = Util.insertCommmas(new String(totalTmTerms));
                            $j("#totalPolledTms").html(totalTerms3);
                            paginationTm(noOfPages, pageNum);
                            bindEvents();
                            var searchBy = $j("#newTmTerm").val();
                            searchBy = $j.trim(searchBy);
                            searchBy = searchBy.replace(/[&\/\\#,@|+()$~%.'":*?<>{}]/g, '');
                            searchText = searchBy.replace(/\s\s+/g, ' ');
                            searchText = $j.trim(searchText);

                            $j('div.sourceTm').each(function (i) {
                                var origText = $j(this).text();
                                if (searchBy != null && searchBy != "Enter term to search...") {
                                    if (origText.length > 16) {
                                        var finalText = origText.substring(0, 14);
                                        finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                        finalText = finalText + "...";
                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    } else {
                                        var origText = $j(this).text();
                                        origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                        $j(this).html(origText);
                                    }
                                } else {
                                    if (origText.length > 16) {
                                        var finalText = origText.substring(0, 14);
                                        finalText = finalText + "...";
                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    }
                                }
                            });

                            $j('div.targetTm').each(function (i) {
                                var origText = $j(this).text();
                                if (searchBy != null && searchBy != "Enter term to search...") {
                                    if (origText.length > 14) {
                                        var finalText = origText.substring(0, 12);
                                        finalText = Util.backgroundYellow(finalText, searchText, null, caseFlag);
                                        finalText = finalText + "...";
                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    } else {
                                        var origText = $j(this).text();
                                        origText = Util.backgroundYellow(origText, searchText, null, caseFlag);
                                        $j(this).html(origText);
                                    }
                                } else {
                                    if (origText.length > 16) {
                                        var finalText = origText.substring(0, 14);
                                        finalText = finalText + "...";
                                        $j(this).html(finalText);
                                        $j(this).attr("title", origText);
                                    }
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
                            $j("#selectAllTms").click(function (event) {
                                event.stopPropagation();
                                $j('.case').attr('checked', this.checked);
                            });

                            $j('#tmDtlRowsList input[type="checkbox"]').click(function () {
                                var countcheck = $j('#tmDtlRowsList input[type="checkbox"]:checked').length;
                                if (countcheck != length) {
                                    $j("#selectAllTms").attr("checked", false);
                                } else {
                                    $j("#selectAllTms").attr("checked", true);
                                }
                            });
//						   $j(".case").click(function(){
//							   
//						        if($j(".case").length == $j(".case:checked").length) {
//						            $j("#selectAllTms").attr("checked", "checked");
//						        } else {
//						            $j("#selectAllTms").removeAttr("checked");
//						        }
//						 
//						    });
////						

                            $j(".rowBg .row  .deleteTmImg").hover(function () {
                                modalRender.bubble(".rowBg .row .deleteTmImg", "Delete", "left center", "right center");
                            });

                            $j(".rowBg .row .deleteTmImg").click(function (event) {
                                var userRole = "";
                                selectedTermIds = "";
                                if ($j("#tmDtlRowsList input:checked").length == 0 || $j("#tmDtlRowsList input:checked").length == 1) {
                                    var id = $j(this).attr("imgId");
                                    deleteTmVal(id);
                                }
                                event.stopPropagation();

                            });
                            $j(".rowBg .row .modifyTmImg").hover(function () {
                                modalRender.bubble(".rowBg .row .modifyTmImg", "Edit", "left center", "right center");
                            });
                            $j(".selectedbg").hover(function () {
                                modalRender.bubble(".selectedbg", "Yellow highlight denotes search term(s).", "left center", "right center");
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
            $j(function () {
                $j("#datepicker").datepicker({
                    showOn: "button",
                    buttonImage: $j("#contextRootPath").val() + "/images/calendar.gif",
                    buttonImageOnly: true
                });
            });
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

    var tmxAlert = function (alertId) {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j(alertId + ":ui-dialog").dialog("destroy");

        $j(alertId).dialog({
            height: 120,
            width: 200,
            modal: true,
            header: false,
            resizable: false
        });
    };

    var finaliseTerm = function (suggestedTermId) {
        $t.finalizeTerm(suggestedTermId, {
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
                    console.log(xhr.responseText.message);
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


    var displayPosList = function (data, appenderClass) {

        var posList = data;
        for (var count = 0; count < posList.length; count++) {
            var partOfSpeechSlctTmplClone = slctOptionsTmpl;
            partOfSpeechSlctTmplClone[1] = posList[count].partsOfSpeechId;
            partOfSpeechSlctTmplClone[3] = posList[count].partOfSpeech;
            $j(appenderClass).append(partOfSpeechSlctTmplClone.join(""));
        }
    };
 var displayPosListForTarget=function (data, appenderClass) {

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

    //	var displayCompanyList= function(data,appenderClass) {
//		var companyList = data;
//		for ( var count = 0; count < companyList.length; count++) {
//			var companyTmplClone = slctOptionsTmpl;
//			companyTmplClone[1] = companyList[count].companyId;
//			companyTmplClone[3] = companyList[count].companyName;
//			$j(appenderClass).append(companyTmplClone.join(""));
//		}
//	};
    var displayContentList = function (data, appenderClass) {
        var contentList = data;
        for (var count = 0; count < contentList.length; count++) {
            var contentTmplClone = slctOptionsTmpl;
            contentTmplClone[1] = contentList[count].contentTypeId;
            contentTmplClone[3] = contentList[count].contentType;
            jQuery(appenderClass).append(contentTmplClone.join(""));
        }
    };
    var showVotingDetails = function () {
        $j("#votiongDetailsDiv:ui-dialog").dialog("destroy");

        $j("#votiongDetailsDiv").dialog({
            height: 700,
            width: 900,
            modal: true,
              });
        $j('#votiongDetailsTab').hover( function(){
        	$j($j('#votiongDetailsTab').find('.comment')).each(function(){
        		modalRender.bubble('#'+$j(this).attr('id'),  $j(this).find('.tip-text').text(), "left center", "right center");
        	});
      });
       
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
            userTypeTmplClone[1] = userTypeList[count].role.roleId;
            userTypeTmplClone[3] = userTypeList[count].role.roleName;
            $j('.categoryList').append(userTypeTmplClone.join(""));
        }
    };

    var usersTmpl = ['<option value="',
        '',
        '" emailid="',
        '',
        '">',
        '',
        '</option>'
    ];

    var companyTmpl = ['<option value="',
        '',
        '" >',
        '',
        '</option>'
    ];

    var displayUsersList = function (data) {
        var userList = data;
        var seen = {};
        var result = '';
        
        for (var count = 0; count < userList.length; count++) {
            var usersTmplClone = usersTmpl;
            var char =  userList[count].userId;
            
            if (char in seen) {
                continue;
            } else {
                seen[char] = true;
                result += char;
                usersTmplClone[1] = userList[count].userId;
                usersTmplClone[3] = userList[count].emailId;
                usersTmplClone[5] = userList[count].userName;
                $j('.usersList').append(usersTmplClone.join(""));
            }
        }
        
    };


    var displayProgramList = function (data) {
        var programList = data;
        for (var count = 0; count < programList.length; count++) {
            var programSlctTmplClone = slctOptionsTmpl;
            programSlctTmplClone[1] = programList[count].programId;
            programSlctTmplClone[3] = programList[count].programName;
            $j('.program').append(programSlctTmplClone.join(""));
        }
    };


    var displayProductGroupList = function (data, appendclass) {
        var productList = data;
        for (var count = 0; count < productList.length; count++) {
            var productGroupSlctTmplClone = slctOptionsTmpl;
            productGroupSlctTmplClone[1] = productList[count].productId;
            productGroupSlctTmplClone[3] = productList[count].product;
            $j(appendclass).append(productGroupSlctTmplClone.join(""));
        }
    };

    var displayStatusList = function (data) {
        var statusList = data;
        for (var count = 0; count < statusList.length; count++) {
            var statusSlctTmplClone = slctOptionsTmpl;
            statusSlctTmplClone[1] = statusList[count].statusId;
            statusSlctTmplClone[3] = statusList[count].status;
            $j('.status').append(statusSlctTmplClone.join(""));
        }
    };


    var displayLanguageList = function (data, appenderClass) {
        var languageList = data;
        for (var count = 0; count < languageList.length; count++) {
            var langSlctTmplClone = slctOptionsTmpl;
            langSlctTmplClone[1] = languageList[count].languageId;
            langSlctTmplClone[3] = languageList[count].languageLabel;
            $j(appenderClass).append(langSlctTmplClone.join(""));
        }
    };
    
    var displayDomainList = function (data, appenderClass) {
        var domainList = data;
        for (var count = 0; count < domainList.length; count++) {
            var domainSlctTmplClone = slctOptionsTmpl;
            domainSlctTmplClone[1] = domainList[count].domainId;
            domainSlctTmplClone[3] = domainList[count].domain;
            $j(appenderClass).append(domainSlctTmplClone.join(""));
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

    var displayConceptCatList = function (data, appenderClass) {
        var conceptCatList = data;
        for (var count = 0; count < conceptCatList.length; count++) {
            var conceptCatSlctTmplClone = slctOptionsTmpl;
            conceptCatSlctTmplClone[1] = conceptCatList[count].conceptCatId;
            conceptCatSlctTmplClone[3] = conceptCatList[count].conceptCategory;
            $j(appenderClass).append(conceptCatSlctTmplClone.join(""));
        }
    };


    var displayEmailTemplateList = function (data) {
        $j(".inviteEmailTemplate").html('<option value="">---Select template---</option>');
        var emailTemplateList = data;
        for (var count = 0; count < emailTemplateList.length; count++) {
            var emailTemplateSlctTmplClone = slctOptionsTmpl;
            emailTemplateSlctTmplClone[1] = emailTemplateList[count].emailTemplateId;
            emailTemplateSlctTmplClone[3] = emailTemplateList[count].emailSubject;
            $j('.inviteEmailTemplate').append(emailTemplateSlctTmplClone.join(""));
        }
    };

    var displayCompanyList = function (data, appenderClass) {
        var companyList = data;
        for (var count = 0; count < companyList.length; count++) {
            var companyTmplClone = companyTmpl;
            companyTmplClone[1] = companyList[count].companyId;
            companyTmplClone[3] = companyList[count].companyName;
            $j(appenderClass).append(companyTmplClone.join(""));
        }
    };

    var addNewTerm = function () {
        var date = new Date();
        var curr_date = date.getDate();
        var curr_month = date.getMonth();
        if (fileName != null)
            photoPath = $j("#contextRootPath").val() + "/images/term_images/" + fileName;
        else
            photopath = null;

        curr_month = curr_month + 1;
        var curr_year = date.getFullYear();
        date = curr_date + '/' + curr_month + '/' + curr_year;

        var termInformation = new Object();
        var form = new Object();

        form.formId = $j("#termFormData :selected").val();
        form.formName = null;
        form.createdBy = null;
        form.createDate = null;
        form.updatedBy = null;
        form.updateDate = null;
        form.isActive = 'Y';

        var partsOfSpeech = new Object();
        partsOfSpeech.partsOfSpeechId = $j("#termPOS :selected").val();
        partsOfSpeech.partOfSpeech = null;
        partsOfSpeech.createdBy = null;
        partsOfSpeech.createDate = null;
        partsOfSpeech.updatedBy = null;
        partsOfSpeech.updateDate = null;
        partsOfSpeech.isActive = 'Y';

        var targetPartsOfSpeech = new Object();
        targetPartsOfSpeech.partsOfSpeechId = $j("#targetPOS :selected").val();
        targetPartsOfSpeech.partOfSpeech = null;
        targetPartsOfSpeech.createdBy = null;
        targetPartsOfSpeech.createDate = null;
        targetPartsOfSpeech.updatedBy = null;
        targetPartsOfSpeech.updateDate = null;
        targetPartsOfSpeech.isActive = 'Y';
        
        var program = new Object();
        program.programId = $j("#pgrmPjct :selected").val();
        program.programName = null;
        program.createdBy = null;
        program.createDate = null;
        program.updatedBy = null;
        program.updateDate = null;
        program.isActive = 'Y';

        var category = new Object();
        category.categoryId = $j("#termCategory :selected").val();
        category.category = null;
        category.createdBy = null;
        category.createDate = null;
        category.updatedBy = null;
        category.updateDate = null;
        category.isActive = 'Y';

        var domain = new Object();
        domain.domainId = $j("#termDomain :selected").val();
        domain.domain = null;
        domain.createdBy = null;
        domain.createDate = null;
        domain.updatedBy = null;
        domain.updateDate = null;
        domain.isActive = 'Y';

        var deprecatedTermInformation = new Object();
        var deprecatedTermInformationList = new Array();
        var language = new Object();
        language.languageId = $j("#trgtLang :selected").val();
        language.languageLabel = null;
        language.languageCode = null;
        language.languageNotes = null;
        language.createdBy = null;
        language.createDate = null;
        language.updatedBy = null;
        language.updateDate = null;
        language.isActive = null;
        var languages = Object.toJSON(language);
        if ($j("#depSource1").val().length != 0 || $j("#depTarget1").val().length != 0) {
            deprecatedTermInformation = {
                termInfo: null,
                deprecatedSource: ($j("#depSource1").val() == "" ? null : $j("#depSource1").val()),
                deprecatedTarget: ($j("#depTarget1").val() == "" ? null : $j("#depTarget1").val()),
                deprecatedLangId: language,
                createDate: null,
                createdBy: null,
                updatedBy: null,
                updateDate: null,
                isActive: 'Y'
            }
            deprecatedTermInformationList[0] = deprecatedTermInformation;
        }

        if ($j("#depSource2").val().length != 0 || $j("#depTarget2").val().length != 0) {
            deprecatedTermInformation = {
                termInfo: null,
                deprecatedSource: ($j("#depSource2").val() == "" ? null : $j("#depSource2").val()),
                deprecatedTarget: ($j("#depTarget2").val() == "" ? null : $j("#depTarget2").val()),
                deprecatedLangId: language,
                createDate: null,
                createdBy: null,
                updatedBy: null,
                updateDate: null,
                isActive: 'Y'
            }
            deprecatedTermInformationList[1] = deprecatedTermInformation;
        }

        if ($j("#depSource3").val().length != 0 || $j("#depTarget3").val().length != 0) {
            deprecatedTermInformation = {
                termInfo: null,
                deprecatedSource: ($j("#depSource3").val() == "" ? null : $j("#depSource3").val()),
                deprecatedTarget: ($j("#depTarget3").val() == "" ? null : $j("#depTarget3").val()),
                deprecatedLangId: language,
                createDate: null,
                createdBy: null,
                updatedBy: null,
                updateDate: null,
                isActive: 'Y'
            }
            deprecatedTermInformationList[2] = deprecatedTermInformation;
        }

        var companyVisible = $j(".companySlctdropdown").is(":visible");

        if (cvalues != null && cvalues.length > 1 && companyVisible) {
            for (var i = 0; i < cvalues.length; i++) {
                var company = new Object();
                company.companyId = cvalues[i];
                termInformation.termId = null;
                termInformation.termBeingPolled = $j.trim($j("#termInfo").val());
                termInformation.termStatusId = $j("#termStatus :selected").val();
                termInformation.termCategory = category;
                termInformation.termUsage = $j("#cncptExample").val();
                termInformation.termForm = form;
                termInformation.termPOS = partsOfSpeech;
                termInformation.targetTermPOS = targetPartsOfSpeech;
                termInformation.termProgram = program;
                termInformation.termNotes = $j.trim($j("#termNotes").val());
                termInformation.termLangId = null;
                termInformation.conceptDefinition = $j.trim($j("#cncptDef").val());
                termInformation.conceptNotes = $j.trim($j("#notes").val());
                termInformation.conceptProdGroup = $j("#productGroup :selected").val();
                termInformation.conceptCatId = $j("#category :selected").val();
                var translation = $j("#trnsltn").val();
                translation = ( $j.trim(translation) == "") ? null : translation;
                termInformation.suggestedTerm = translation;
                termInformation.suggestedTermLangId = $j("#trgtLang :selected").val();
                termInformation.suggestedTermStatusId = $j("#status :selected").val();
                termInformation.suggestedTermFormId = $j("#termForm :selected").val();
                if ($j("#pos :selected").val() == "") {
                    termInformation.suggestedTermPosId = $j("#termPOS :selected").val();
                } else {
                    termInformation.suggestedTermPosId = $j("#pos :selected").val();
                }
                termInformation.suggestedTermUsage = $j.trim($j("#cncptlExmpl").val());
                termInformation.suggestedTermNotes = $j.trim($j("#cncptNotes").val());
                termInformation.suggestedTermPgmId = $j("#program :selected").val();
                termInformation.createdBy = null;
                termInformation.createDate = null;
                termInformation.updatedBy = null;
                termInformation.updateDate = null;
                termInformation.isActive = 'Y';
                termInformation.comments = $j.trim($j("#comment").val());
                termInformation.photoPath = photoPath;
                termInformation.deprecatedTermInfo = deprecatedTermInformationList;
                termInformation.termDomain = domain;
                termInformation.termCompany = company;
                var termInformationObject = Object.toJSON(termInformation);
                $t.addNewTerm(termInformationObject, {
                    success: function (data) {

                        if (data == "success") {
                            clearForm();
                            alertMessage("#termAddSuccess");
                        }
                        if (data == "failedterm") {
                            alertMessage("#termAddFailedExists");
                        }
                        if (data == "update") {
                            alertMessage("#termUpdate");
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
        } else if (cvalues != null) {

            var company = new Object();
			if (companyVisible)
				company.companyId = cvalues[0];
			else
				company.companyId = cvalues;
				
            termInformation.termId = null;
            termInformation.termBeingPolled = $j.trim($j("#termInfo").val());
            termInformation.termStatusId = $j("#termStatus :selected").val();
            termInformation.termCategory = category;
            termInformation.termUsage = $j("#cncptExample").val();
            termInformation.termForm = form;
            termInformation.termPOS = partsOfSpeech;
            termInformation.targetTermPOS = targetPartsOfSpeech;
            termInformation.termProgram = program;
            termInformation.termNotes = $j.trim($j("#termNotes").val());
            termInformation.termLangId = null;
            termInformation.conceptDefinition = $j.trim($j("#cncptDef").val());
            termInformation.conceptNotes = $j.trim($j("#notes").val());
            termInformation.conceptProdGroup = $j("#productGroup :selected").val();
            termInformation.conceptCatId = $j("#category :selected").val();
            var translation = $j("#trnsltn").val();
            translation = ( $j.trim(translation) == "") ? null : translation;
            termInformation.suggestedTerm = translation;
            termInformation.suggestedTermLangId = $j("#trgtLang :selected").val();
            termInformation.suggestedTermStatusId = $j("#status :selected").val();
            termInformation.suggestedTermFormId = $j("#termForm :selected").val();
            termInformation.suggestedTermPosId = $j("#pos :selected").val();
            termInformation.suggestedTermUsage = $j.trim($j("#cncptlExmpl").val());
            termInformation.suggestedTermNotes = $j.trim($j("#cncptNotes").val());
            termInformation.suggestedTermPgmId = $j("#program :selected").val();
            termInformation.createdBy = null;
            termInformation.createDate = null;
            termInformation.updatedBy = null;
            termInformation.updateDate = null;
            termInformation.isActive = 'Y';
            termInformation.comments = $j.trim($j("#comment").val());
            termInformation.photoPath = photoPath;
            termInformation.deprecatedTermInfo = deprecatedTermInformationList;
            termInformation.termDomain = domain;
            termInformation.termCompany = company;
            var termInformationObject = Object.toJSON(termInformation);
            $t.addNewTerm(termInformationObject, {
                success: function (data) {
                    if (data == "success") {
                        clearForm();
                        alertMessage("#termAddSuccess");
                    }
                    if (data == "failedterm") {
                        alertMessage("#termAddFailedExists");
                    }
                    if (data == "update") {
                        alertMessage("#termUpdate");
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
    };

    function clearForm() {
        $j("#termInfo").val('');
        $j("#cncptDef").val('');
        $j("#termStatus").val('');
        $j(".termCategory").val(0);
        $j("#cncptExample").val('');
        $j("#termFormData").val(0);
        $j("#termPOS").val('');
        $j("#pgrmPjct").val('');
        $j("#pgrmPjctd").val('');
        $j("#termNotes").val('');
        $j("#notes").val('');
        $j("#productGroup").val('');
        $j("#category").val('');
        $j("#trnsltn").val('');
        $j("#trgtLang ").val('');
        $j("#companySlct").multiselect("uncheckAll");
        $j("#termDomain").val('');
        $j("#status").val('');
        $j("#form").val('');
        $j("#pos").val('');
        $j("#cncptlExmpl").val('');
        $j("#cncptNotes").val('');
        $j("#program ").val('');
        $j("#comment").val('');
        $j("#depSource").val('');
        $j("#depTarget").val('');
        $j("#depTrgtLang").val('');
        $j("#depTarget1").val('');
        $j("#depTarget2").val('');
        $j("#depTarget3").val('');
        $j("#depSource1").val('');
        $j("#depSource2").val('');
        $j("#depSource3").val('');
        cvalues = 0;
        $j("#uploadedImage").attr('src', $j("#contextRootPath").val() + "/images/default.jpg");
        photoPath = null;
        fileName = null;

    }

    var pagination = function (noOfPages, pageNum) {
        if ((noOfPages > 1) && (pageNum < noOfPages)) {
            $j(".termNext").addClass("nextEnable");
            $j(".termNext").removeClass("nextDisable");
        } else {
            $j(".termNext").removeClass("nextEnable");
            $j(".termNext").addClass("nextDisable");
        }

        if ((noOfPages > 1) && (pageNum > 1)) {
            $j(".termprevious").removeClass("prevDisable");
            $j(".termprevious").addClass("prevEnable");
        } else {
            $j(".termprevious").removeClass("prevEnable");
            $j(".termprevious").addClass("prevDisable");
        }
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

    var showImprtUserDialog = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#termImprtInfo:ui-dialog").dialog("destroy");

        $j("#termImprtInfo").dialog({
            height: 500,
            width: 400,
            modal: true,
            close: function (event, ui) {
                $j("#termImprtInfo").val("");
            }
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

    var showDialog = function () {
        // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
        $j("#invitation:ui-dialog").dialog("destroy");

        $j("#invitation").dialog({
            height: 510,
            width: 550,
            modal: true,
            close: function (event, ui) {
                validateTermVote.resetForm();
                $j(".categoryList").html("");
                $j(".usersList").html("");
                $j(".categorySlct").val(0);
                $j(".inviteEmailTemplate").html("");
                $j('.inviteEmailTemplate').children().remove().end().append('<option value="">--Select template--</option>');
                $j("#selectReq").hide();
                $j(".emailPrvw").html("");
                $j(".mailTmpl").val(0);
                $j(".sampleVoteMail").hide();
                $j(".sampleWelcomeMail").hide();
                $j("#invitation").dialog("destroy");
//				selectedTermIds = ""; 
            }
        });
    };

    $j("#resetBtn").click(function () {
        $j("#uploadedImage").attr('src', $j("#contextRootPath").val() + "/images/default.jpg");
        validateTerm.resetForm();
        $j(".companySlctErr").hide();
        $j("#addNewTerm .companySlctdropdown .ui-state-default").css("border", "1px solid #BBBBBB");
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

    var deleteTermDetails = function (selectedTermIds, deletedcount, selectedcount) {
        var termIdsArray = selectedTermIds;
        $t.deleteTerms(termIdsArray, {
            success: function (data) {
                if (deletedcount > 0) {
                    var msg = "";
                    if (deletedcount == 1) {
                        msg = deletedcount + " term out of " + selectedcount + " selected terms is not deleted as poll date is not expired.";
                    } else {
                        msg = deletedcount + " terms out of " + selectedcount + " selected terms are not deleted as poll date is not expired.";
                    }
                    $j('#selectTermDelete').html(msg);

                } else if (deletedcount == 0) {
                    selectedTermIds = "";
                    $j('#selectTermDelete').html("Deleted successfully.");
                }

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
                totalTerms = totalTerms - (termIdsArray.length);
                var pageNum = savedCriteria.pageNum;
                var totalTermsTillBefore = (pageNum - 1) * 10;
                if (totalTermsTillBefore == totalTerms) {
                    savedCriteria.pageNum = pageNum - 1;
                }
                compSelectedVal = [];
                langSelectedVal = [];
                alertMessage("#selectTermDelete");
                $j("#companyTermsSlct").multiselect("refresh");
                $j("#termlanguageSlct").multiselect("refresh");
                $j("#companyTermsSlct").multiselect("uncheckAll");
                $j("#termlanguageSlct").multiselect("uncheckAll");
                $j("#newTerm").val("Enter term to search...");
                triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.filterByCompany, savedCriteria.selectedCompanyIds, savedCriteria.isTM);
                //	alertMessage("#deleteTerms");
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
    };

    var deleteTmDetails = function (selectedTermIds, deletedcount, selectedcount) {
        var termIdsArray = selectedTermIds.split(",");
        $t.deleteTms(termIdsArray, {
            success: function (data) {

                totalTerms = totalTerms - (termIdsArray.length);
                var pageNum = savedCriteria.pageNum;
                var totalTermsTillBefore = (pageNum - 1) * 10;
                if (totalTermsTillBefore == totalTerms) {
                    savedCriteria.pageNum = pageNum - 1;
                }
                compSelectedVal = [];
                langSelectedVal = [];
                $j("#newTmTerm").val("Enter term to search...");
                $j("#tmLanguageSlct").multiselect("uncheckAll");
                $j("#companyTmsSlct").multiselect("uncheckAll");
                triggerTmDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.filterByCompany, savedCriteria.selectedCompanyIds);
                alertMessage("#deleteTM");
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
    };


    var saveTermDetails = function (termId) {
        // Create TermInformation Object
        var termDtls = new Object();
        termDtls.termId = parseInt(termId);
        termDtls.termNotes = $j("#notesId_" + termId).val();
        termDtls.conceptDefinition = $j("#defId_" + termId).val();
        termDtls.termUsage = $j("#usageId_" + termId).val();
        var sourceTerm = $j("#sourceId_" + termId).val();
        termDtls.sourceTerm = $j("#sourceId_" + termId).val();

        var targetTerm = $j("#targetId_" + termId).val()
        if ($j.trim(targetTerm) != "") {
            termDtls.topSuggestion = targetTerm/*$j("#targetId_"+termId).val()*/;
        } else {
            termDtls.topSuggestion = oldtargetTerm;
        }
        if ($j("#posId_" + termId).val() == "") {
            termDtls.termPOSId = null;
        } else {
            termDtls.termPOSId = parseInt($j("#posId_" + termId).val());
        }
        if ($j("#targetPosID_" + termId).val() == "") {
            termDtls.targetPOSId = null;
        } else {
            termDtls.targetPOSId = parseInt($j("#targetPosID_" + termId).val());
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
        if ($j("#statusId_" + termId + " :selected").val() == "") {
            termDtls.termStatusId = null;
        } else {
            termDtls.termStatusId = parseInt($j("#statusId_" + termId + " :selected").val());
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

    var triggerSearch = function () {
        var caseSensitiveFlag = false;
        var searchBy = $j("#newTerm").val();
        searchBy = searchBy.replace(/[&\/\\#,|!^@+()$~%.'":*?<>{}]/g, '');
        searchBy = searchBy.replace(/\s\s+/g, ' ');
        searchBy = $j.trim(searchBy);
        if ($j("#chkCase").attr('checked')) {
            caseSensitiveFlag = true;
        }

        if (searchBy == "Enter term to search..." || $j.trim(searchBy) == "") {
            searchBy = null;
            caseSensitiveFlag = false;
        }

        var $tabs = $j('#tabs').tabs();
        $tabs.tabs('select', 0); // switch to highlights tab

        if (!(/[\^\<\>\%\$\#\(\)\{\}\?\|\~\"\'\@\&\!]+/g).test(searchBy) && searchBy != null) {
            triggerTermDetails(null, null, 0, searchBy, caseSensitiveFlag, null, null, null, null, null);
        } else {
            alertMessage("#validationMsg");
        }
        searchBy = null;
        return false;
    };
    
    var triggerVoteSearch = function () {
        var caseSensitiveFlag = false;
        var searchBy = $j('#newTermVoted').val();
        searchValue=searchBy;
        searchBy = searchBy.replace(/[&\/\\#,|!^@+()$~%.'":*?<>{}]/g, '');
        searchBy = searchBy.replace(/\s\s+/g, ' ');
        searchBy = $j.trim(searchBy);
        
        var filterBy = savedCriteria.filterBy;
        var selectedIds = savedCriteria.selectedIds;
        if (searchBy == "Enter term" || $j.trim(searchBy) == "") {
            searchBy = null;
            searchValue = null;
            caseSensitiveFlag = false;
        }
        var $tabs = $j('#tabs').tabs();
        $tabs.tabs('select', 0); // switch to highlights tab

        if (!(/[\^\<\>\%\$\#\(\)\{\}\?\|\~\"\'\@\&\!]+/g).test(searchBy) && searchBy != null) {
            triggerTermDetails(null, null, 0, searchBy, caseSensitiveFlag, 'Locale', searchLangValues, null, searchCompanyValues, null);
        } 
        if((searchLangValues != null && searchBy == null) || searchCompanyValues != null && searchBy == null){
        	triggerTermDetails(null, null, 0, searchBy, caseSensitiveFlag, 'Locale', searchLangValues, null, searchCompanyValues, null);
        }
        if(searchCompanyValues == null && searchBy == null && searchLangValues == null){
        	triggerTermDetails(null, null, 0, searchBy, caseSensitiveFlag, 'Locale', searchLangValues, null, searchCompanyValues, null);
        }
        searchBy = null;
        return false;
    };
    var triggerTMSearch = function () {
        var caseSensitiveFlag = false;
        var searchBy = $j("#newTmTerm").val();
        searchBy = $j.trim(searchBy);
        searchBy = searchBy.replace(/[&\/\\#,|^@!+()$~%.'":*?<>{}]/g, '');
        searchBy = searchBy.replace(/\s\s+/g, ' ');
        if ($j("#chktmCase").attr('checked')) {
            caseSensitiveFlag = true;
        }
        if (searchBy == "Enter term to search" || $j.trim(searchBy) == "") {
            searchBy = null;
            caseSensitiveFlag = false;
        }
        var $tabs = $j('#tabs').tabs();
        $tabs.tabs('select', 5); // switch to viewTMS tab

        if (searchBy != null) {
            triggerTmDetails(null, null, 0, searchBy, caseSensitiveFlag, null, null, null, null, null);
        } else {
            alertMessage("#validationMsg");
        }


        return false;
    };
    $j('#newTerm').bind('keydown', function (e) {
        if (e.which == 13)
            triggerSearch();
    });
    $j('#newTmTerm').bind('keydown', function (e) {
        if (e.which == 13)
            triggerTMSearch();
    });

    $j("#searchTerm").click(function () {
        $j("#termlanguageSlct").multiselect("uncheckAll");
        $j("#showSelectedTerms").attr('checked', false);
        triggerSearch();

    });

    $j("#searchTermVoted").click(function () {
    	$j("#paginationId").val($j("#paginationId option:first").val()); 
    	paginationVal = false;
    	termListLimit =10;
    	selectValue = null;
        $j("#showSelectedTerms").attr('checked', false);
        triggerVoteSearch();
    });

    $j("#searchTm").click(function () {
        $j("#tmLanguageSlct").multiselect("uncheckAll");
        triggerTMSearch();

    });

    $j("#saveData").click(function () {
        if (adminHeaderFlag == "true") {
            if (cvalues == null || cvalues == 0)
                cvalues = $j("#companyId").val();
            if ($j("#addNewTerm").valid() && cvalues != null && cvalues != 0) {
                addNewTerm();
            } else if (cvalues == null || cvalues == 0) {
                $j(".companySlctErr").show();
                $j("#addNewTerm .ui-state-default").css("border", "1px solid red");
            } else {
                $j("#addNewTerm .ui-state-default").css("border", "1px solid #BBBBBB");
                $j(".companySlctErr").hide();
            }
        } else {
            if ($j("#addNewTerm").valid()) {
                addNewTerm();
            }
        }
    });

    $j("#resetBtn").click(function () {
        cvalues = 0;
    });
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
                                fileName = null;
                                photoPath = null;
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

    $j(".moveData").click(function () {
        $j('.usersList').html("");
        $j("#noUsersReq").hide();
        $j("#UsersReqError").hide();
        $j('.usersList').removeClass("error");
        if ($j('.categoryList').val() == null) {
            $j('.categoryList').addClass("error");
            $j("#selectReq").show();

        } else {
            $j("#selectReq").hide();
//			$j('.categoryList').removeClass("error");
            $j('.categoryList').css("border", "1px solid  #BBBBBB");

            var filterBy = $j(".categorySlct").val();
            var ids = $j('.categoryList').val();

            $t.getInviteUserList(filterBy, ids, {
                success: function (data) {
                    displayUsersList(data);
                    if ($j('.usersList').html() == "") {
                        $j('.usersList').addClass("error");
                        $j("#noUsersReq").show();
                        $j("#UsersReqError").hide();

                        flag = false;
                    } else {
                        $j("#noUsersReq").hide();
                        $j('.usersList').removeClass("error");
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

    });

    var termListTmpl = ['<div class="termImportTblrows" style="text-align:center"><div style="word-wrap:break-word;" class="sourceName width220">',
        '',
        '</div><div class="lineNumber width110" style="text-align:center; padding-top:8px">',
        '',
        '</div></div>'
    ];

    var displayFailedImportList = function (data) {

        var userNames = data.userNames;
        var lineNumbers = data.lineNumbers;
        $j('.termImportTblBody').html("");
        $j('.termImprtInfo').html("");
        var termListTmplClone = termListTmpl;
        if (userNames != null)
            for (var i = 0; i < userNames.length; i++) {
                if (lineNumbers != null) {
                    termListTmplClone[1] = userNames[i];
                    if (i == 0 && (lineNumbers[i] == -1)) {
                        termListTmplClone[3] = "";
                        $j('.termImprtInfo').append($j("#termImportTMXInvalid").show());
                    } else {
                        termListTmplClone[3] = lineNumbers[i];
                    }

                }

                $j('.termImportTblBody').append(termListTmplClone.join(""));
            }

        $j('div.sourceName').each(function (i) {
            var origText = $j(this).text();
            if (origText.length > 75) {
                var finalText = origText.substring(0, 70);
                finalText = finalText + "...";
                $j(this).text(finalText);
                $j(this).attr("title", origText);
            }
        });
    };
    $j("#downloadTmpltFile").click(function () {

        location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadTemplateFile";
    });
    $j("#downloadTBXTmpltFile").click(function () {

        location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadTBXTmpltFile";
    });
    $j("#downloadTABTmpltFile").click(function () {

        location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadTABTmpltFile";
    });
    var uploadStatus = function () {
        $t.getUploadStatus({
            success: function (data) {

                if (data != null) {
                    loadingFlag = false;
                    $j("#loading:ui-dialog").dialog("destroy");
                    $j('#loading').html('');
//					  setTimeout(function(){
                    if (data.termInformationStatus == "success") {

                        $j("#termImprtInfo").show();
                        $j("#message").find(".loading-msg").hide();
                        $j("#importTerm").val("");
                        $j("#importTMXTerm").val("");
                        if (data.insertedCount == 0 && data.updatedCount == 0 && data.rejectedCount == 0) {
                            $j("#failedImportedTerms").html("Invalid File. Target Language does not exist");
                            $j("#downloadTmpltFile").show();
                            $j("#downloadTBXTmpltFile").show();
                            $j("#downloadTABTmpltFile").show();
                            $j(".failedInfoTerms").hide();
                            $j(".failedInfo").hide();
                            $j("#downloadTmpltFile").click(function () {

                                location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadTemplateFile";
                            });
                            $j("#downloadTBXTmpltFile").click(function () {

                                location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadTBXTmpltFile";
                            });
                            $j("#downloadTABTmpltFile").click(function () {

                                location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadTABTmpltFile";
                            });

                        }
                    }

                    $j("#termImprtInfo").show();
                    $j("#successAddedTerms").html("");
                    $j("#successUpdatedTerms").html("");
                    $j("#failedAddedTerms").html("");
                    if (data.insertedCount > 0) {
                        $j("#termImportTMXFailed").hide();
                        $j("#termImportTMXInvalid").hide();
                        $j(".failedInfo").hide();
                        $j("#downloadTmpltFile").hide();
                        $j("#downloadTBXTmpltFile").hide();
                        $j("#downloadTABTmpltFile").hide();
                        $j("#failedImportedTerms").html("");
                        $j("#successAddedTerms").html("Successfully imported terms : " + data.insertedCount);
                    }
                    if (data.updatedCount > 0) {
                        $j("#termImportTMXFailed").hide();
                        $j("#termImportTMXInvalid").hide();
                        $j(".failedInfo").hide();
                        $j("#downloadTmpltFile").hide();
                        $j("#downloadTBXTmpltFile").hide();
                        $j("#downloadTABTmpltFile").hide();
                        $j("#failedImportedTerms").html("");
                        $j("#successUpdatedTerms").html("Successfully updated terms : " + data.updatedCount);
                    }
                    showImprtUserDialog();
                    if (data.termInformationStatus == "error") {
                        if (data.rejectedCount == -2) {
                            //  $j("#termImprtInfo").hide();

                            //$j("#termImportFailed").show();
                            displayFailedImportList(data);

                        }
                    }
                    if (data.termInformationStatus == "invalid") {
                        if (data.rejectedCount == -1) {
                            //  $j(".failedInfo").hide();
                            $j("#termImportTMXInvalid").show();
                        }
                    }
                    if (data.rejectedCount == 0 || data.rejectedCount == -1 || data.rejectedCount == -2) {
                        $j(".failedInfoTerms").hide();
                    }
                    if (data.termInformationStatus == "failed" && data.insertedCount == null && data.updatedCount == null && data.rejectedCount == null) {
                        $j("#message").find(".loading-msg").hide();
                        if (data.missedColumns != null) {
                            $j("#failedImportedTerms").html("Invalid File. Missing <b>" + data.missedColumns + "</b> column(s)");
                        } else {
                            $j("#failedImportedTerms").html("Invalid File");
                            $j("#downloadTBXTmpltFile").show();
                            $j("#downloadTBXTmpltFile").click(function () {

                                location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadTBXTmpltFile";
                            });
                        }
                        $j("#downloadTmpltFile").show();
                        $j("#downloadTABTmpltFile").show();
                        $j(".failedInfoTerms").hide();
                        $j(".failedInfo").hide();
                        $j("#downloadTmpltFile").click(function () {

                            location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadTemplateFile";
                        });
                        $j("#downloadTABTmpltFile").click(function () {

                            location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadTABTmpltFile";
                        });
                    }
                    else {

                        if (data.insertedCount > 0) {
                            $j("#termImportTMXFailed").hide();
                            $j("#termImportTMXInvalid").hide();
                            $j(".failedInfo").hide();
                            $j("#downloadTmpltFile").hide();
                            $j("#downloadTBXTmpltFile").hide();
                            $j("#downloadTABTmpltFile").hide();
                            $j("#failedImportedTerms").html("");
                            $j("#successAddedTerms").html("Successfully imported terms : " + data.insertedCount);
                        }
                        if (data.updatedCount > 0) {
                            $j("#termImportTMXFailed").hide();
                            $j("#termImportTMXInvalid").hide();
                            $j(".failedInfo").hide();
                            $j("#downloadTmpltFile").hide();
                            $j("#downloadTBXTmpltFile").hide();
                            $j("#downloadTABTmpltFile").hide();
                            $j("#failedImportedTerms").html("");
                            $j("#successUpdatedTerms").html("Successfully updated terms : " + data.updatedCount);
                        }
                        //  $j("#termImportTMXFailed").hide();
                        if (data.rejectedCount > 0) {
                            $j(".failedInfoTerms").show();
                            $j(".failedInfo").show();
                            $j("#downloadTmpltFile").hide();
                            $j("#downloadTBXTmpltFile").hide();
                            $j("#downloadTABTmpltFile").hide();
                            $j("#failedImportedTerms").html("");
                            $j("#failedAddedTerms").html(data.rejectedCount);
                            displayFailedImportList(data);
                        }

                    }


//					  },10);

                }
                else {
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

    var uploadStatusTMX = function () {
        $t.getUploadStatus({
            success: function (data) {
                if (data != null && data.termInformationStatus != null) {
                    loadingFlag = false;
                    $j("#loading:ui-dialog").dialog("destroy");
                    alertMessage("#uploadProgressMsg");
                    $j('#loading').html('');
                    $j("#importTmxmsg").html(data.termInformationStatus);
                } else {
                    setTimeout(function () {
                        uploadStatusTMX();

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

    var downloadStatusTMX = function () {
        $t.getUploadStatus({
            success: function (data) {
                alertMessage("#downloadProgressMsg");
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
    };


    var downloadStatus = function () {
        $t.getDownloadStatus({
            success: function (data) {
                if (data != null) {
                    if (data == 1) {
                        $j("#waiting").find(".loading-msg").hide();
                        $j("#waitingTMX").find(".loading-msg").hide();

                    } else {
                        setTimeout(function () {
                            downloadStatus();
                        }, 100);
                    }
                } else {
                    setTimeout(function () {
                        categorySlct
                        downloadStatus();
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
    var startLoading = function (id, msg) {
//			var msg="Downloading ... please wait";
        var temp = '<div class="loading-msg alignCenter topmargin15"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif" />' + msg + '</div>';
        $j("#" + id).append(temp);

    };
    $j("#importFile").click(function () {
        if ($j.trim($j("#importTerm").val()) != "") {
        	var uploadFileName = $j("#importTerm").val();
        	 if(!((uploadFileName.indexOf('txt') != -1) || (uploadFileName.indexOf('csv') != -1) || (uploadFileName.indexOf('tbx') != -1))) {
        		 $j("#importTerm").val('');
                 $j("#failedImportedTerms").html("Allowed file types are CSV or TXT or TBX");
                 $j("#downloadTmpltFile").show();
                 $j("#downloadTBXTmpltFile").show();
                 $j("#downloadTABTmpltFile").show();
                 $j(".failedInfoTerms").hide();
                 $j(".failedInfo").hide();
        	 }
            if (adminHeaderFlag == "true") {
                //if ($j("#mutliCompanySlct").val() != null) {
                $j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif"  align="top" class="rightmargin10" />&nbsp;Uploading....  Please wait..</div>');
                showLoadingDialog();
                $j(".ui-dialog-titlebar").hide();
                loadingFlag = true;
                uploadStatus();
                MultiCValues = $j("#companyId").val();

                if ($j("#isSuperAdmin").val() == "true")
                    MultiCValues = $j("#mutliCompanySlct").val();

                $j("#ImportForm").attr("action", $j("#contextRootPath").val() + "/impExp_serv?selectedMultiCompanyIds=" + MultiCValues);
                $j("#ImportForm").submit();
                //} else {
                //    alertMessage("#importCompanySlctMessage");
                //}
            } else {
                $j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif"  align="top" class="rightmargin10" />&nbsp;Uploading....  Please wait..</div>');
                // showLoadingDialog();
                $j(".ui-dialog-titlebar").hide();
                //uploadStatus();
                $j("#selectedMultiCompanyIds").val(MultiCValues);
                $j("#ImportForm").attr("action", $j("#contextRootPath").val() + "/impExp_serv?selectedMultiCompanyIds=" + null);
                // $j("#ImportForm").submit();
            }
        }
        else {
            alertMessage("#importMessage");
        }
    });


    $j("#progressLink").click(function () {
        var fileIdval = $j("#fileId").val();
        if ($j("#hpsite").val() == "true")
            location.href = $j("#contextRootPath").val() + "/hp/import_export_status.jsp";
        else
            location.href = $j("#contextRootPath").val() + "/import_export_status.jsp";
        $j('#uploadProgressMsg').dialog("destroy");
        $j("#importTMXTerm").val("");
    });

    $j("#progressLinkDownload").click(function () {
        var fileIdval = $j("#fileIdExport").val();
        if ($j("#hpsite").val() == "true")
            location.href = $j("#contextRootPath").val() + "/hp/import_export_status.jsp";
        else
            location.href = $j("#contextRootPath").val() + "/import_export_status.jsp";
        $j('#downloadProgressMsg').dialog("destroy");

    });

    $j("#importTMXFile").click(function () {
        tmxsubmit(this.form);
        return false;


    });


    var closeLoadingDialog = function (dialogId, id) {
        $j(dialogId).html("");
        $j(dialogId).dialog('destroy');
        $j(id).dialog('destroy');
        mailSent();
    };

    $j("#inviteVoteMail").click(function () {
        var invite = new Object();
        var mailIds = new Array();
        var userIds = new Array();
        var termIds = uniqueSelectedTermIds;
        var expdate = "";
        var count = 0;
        var flag = true;
        var votingDays = 0;
        $j(".usersList option").each(function () {
            count++;
            mailIds[count] = $j(this).attr("emailid");
            userIds[count] = $j(this).val();
        });
        invite.mailIds = mailIds;
        invite.emailTemplateId = $j('.inviteEmailTemplate :selected').val();
        invite.userIds = userIds;
        invite.termIds = termIds;

        if ($j('#votingPeriodNum').val() == "") {
            $j("#voteReq").show();
            flag = false;
        } else {
            var str = $j('#votingPeriodNum').val();
            var patt1 = /^[0-9]+$/g;
            flag = patt1.test(str);
            if (flag == false) {
                $j("#votePReq").show();
            }
        }

        if ($j('.categorySlct  :selected').val() == 0) {
            $j('.categoryList').addClass("error");
            $j("#usersReq").show();

        } else {
            $j('.categoryList').removeClass("error");
            $j("#usersReq").hide();
        }

        if ($j('.categoryList').val() == null) {
            $j('.categoryList').addClass("error");
            $j("#selectReq").show();
            $j("#usersReq").hide();

            flag = false;
        } else {
            $j('.categoryList').removeClass("error");
            $j("#selectReq").hide();


        }

        if ($j('.usersList').html() == null || $j.trim($j('.usersList').html()) == "") {
            $j('.usersList').addClass("error");
            $j("#UsersReqError").show();
            flag = false;
            if ($j('.categoryList').html() != null && $j.trim($j('.categoryList').val()) != "") {
                $j('.usersList').addClass("error");
                $j("#noUsersReq").hide();
                $j("#UsersReqError").show();
            }
        } else {
            $j("#noUsersReq").hide();
            $j("#UsersReqError").hide();
            $j('.usersList').removeClass("error");
        }

        if ($j("#termVoteForm").valid() && flag) {
            $j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif"  align="top" class="rightmargin10" />&nbsp; Sending mail... Please wait</div>');
            showLoadingDialog();
            $j(".ui-dialog-titlebar").hide();
//				setTimeout(function(){
//					closeLoadingDialog('#loading','#invitation');
//					
//				},900);
            votingDays = parseInt($j('#votingPeriodNum').val());
            invite.votingPeriod = $j('#votingPeriodNum').val();
            var inviteParameter = Object.toJSON(invite);

            $t.inviteUsersToVote(inviteParameter, {
                success: function (data) {
                    $j('#loading').html("");
                    $j('#loading').dialog('destroy');
                    $j('#invitation').dialog('destroy');
                    $j(".categoryList").html("");
                    $j(".usersList").html("");
                    $j(".categorySlct").val(0);
                    $j(".mailTmpl").val(0);
                    $j(".sampleVoteMail").hide();
                    $j(".sampleWelcomeMail").hide();
                    $j(".inviteEmailTemplate").val("");
                    $j(".emailPrvw").html("");
                    $j("#templateReq").html('');
                    $j("#templateReq").val('');
                    $j("#usersReq").hide();
                    $j("#noUsersReq").hide();
                    $j("#selectReq").hide();
                    $t.getTotalDebatedTerms({
                        success: function (data) {
                            var totalDebatedTrms = data;
                            $j("#totalDebatedTrms").html(totalDebatedTrms);
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
                                dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' baseFont='HP Simplified' lineColor='#0e94bc'>");
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

                    
                    var serverDate = $j('#serverDate').val();
                    var currDate = new Date(new Number(serverDate));
                    var expdate = new Date(currDate.setDate(currDate.getDate() + votingDays));
                    for (var i = 0; i < uniqueSelectedTermIds.length; i++) {
                        var id = uniqueSelectedTermIds[i];
                        var dataObj = dataMap.get(id);
                        delete dataMap[id];
                        var updatedExpDate = (dataObj.pollExpirationDt = ((expdate.getMonth() + 1) + "/" + expdate.getDate() + "/" + expdate.getFullYear()));
                        dataMap.set(id, dataObj);
                    }
                    selectedLangIdArray = langValues;
                    selectedCompanyIdsArray = companyValues;
                    /*	if(langValues!=null&&selectedLangIdArray.length>0){
                     for(var i = 0; i < selectedLangIdArray.length; i++){
                     $j("#termlanguageSlct").multiselect("widget").find(":checkbox[value='"+selectedLangIdArray[i]+"']").attr("checked","checked");
                     $j("#termlanguageSlct option[value='" + selectedLangIdArray[i] + "']").attr("selected", 1);
                     $j("#termlanguageSlct").multiselect("refresh");
                     }
                     triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, 'Locale', selectedLangIdArray,savedCriteria.filterByCompany ,savedCriteria.selectedCompanyIds,savedCriteria.isTM);

                     }*/
                    if (companyValues != null && selectedCompanyIdsArray.length > 0 && langValues == null /*&& selectedLangIdArray.length==0*/) {
                        for (var i = 0; i < selectedCompanyIdsArray.length; i++) {
                            $j("#companyTermsSlct").multiselect("widget").find(":checkbox[value='" + selectedCompanyIdsArray[i] + "']").attr("checked", "checked");
                            $j("#companyTermsSlct option[value='" + selectedCompanyIdsArray[i] + "']").attr("selected", 1);
                            $j("#companyTermsSlct").multiselect("refresh");
                        }
                        triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, null, null, 'Company', selectedCompanyIdsArray, savedCriteria.isTM);

                    } else if (langValues != null && selectedLangIdArray.length > 0 && companyValues == null /*&& selectedCompanyIdsArray.length==0*/) {
                        for (var i = 0; i < selectedLangIdArray.length; i++) {
                            $j("#termlanguageSlct").multiselect("widget").find(":checkbox[value='" + selectedLangIdArray[i] + "']").attr("checked", "checked");
                            $j("#termlanguageSlct option[value='" + selectedLangIdArray[i] + "']").attr("selected", 1);
                            $j("#termlanguageSlct").multiselect("refresh");
                        }
                        triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, 'Locale', selectedLangIdArray, savedCriteria.filterByCompany, savedCriteria.selectedCompanyIds, savedCriteria.isTM);

                    } else if (companyValues != null && selectedCompanyIdsArray.length > 0 && langValues != null && selectedLangIdArray.length > 0) {
                        for (var i = 0; i < selectedLangIdArray.length; i++) {
                            $j("#termlanguageSlct").multiselect("widget").find(":checkbox[value='" + selectedLangIdArray[i] + "']").attr("checked", "checked");
                            $j("#termlanguageSlct option[value='" + selectedLangIdArray[i] + "']").attr("selected", 1);
                            $j("#termlanguageSlct").multiselect("refresh");
                        }
                        for (var i = 0; i < selectedCompanyIdsArray.length; i++) {
                            $j("#companyTermsSlct").multiselect("widget").find(":checkbox[value='" + selectedCompanyIdsArray[i] + "']").attr("checked", "checked");
                            $j("#companyTermsSlct option[value='" + selectedCompanyIdsArray[i] + "']").attr("selected", 1);
                            $j("#companyTermsSlct").multiselect("refresh");
                        }
                        triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, 'Locale', selectedLangIdArray, 'Company', selectedCompanyIdsArray, savedCriteria.isTM);
                    }
                    else {
                        triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, null, uniqueSelectedTermIds, savedCriteria.filterByCompany, savedCriteria.selectedCompanyIds, savedCriteria.isTM);
                    }
                    closeLoadingDialog('#loading', '#invitation');
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                    if (Boolean(xhr.responseText.message)) {
                        console.log(xhr.responseText.message);
                    }
                }
            });
            $j("#showSelectedTerms").attr('checked', false);

        }


    });
    $j('#votingPeriodNum').change(function () {
        $j('#voteReq').hide();
        $j('#votePReq').hide();
    });
    $j('#termAction').change(function () {

        $j("#termDtlRowsList input:checked").each(function (i) {

            if (selectedTermIds == "") {
                separator = "";
            } else {
                separator = ",";
            }
            selectedTermIds += separator + $j(this).parent().next().attr("termId");

        });

        if (selectedTermIds.length == 0) {
            alertMessage("#termSelctMsg");
            $j('#termAction').val(0);
        }
        else {
//			  if($j(this).val()==1){
//				  deleteVal();
//				  
//				  
//			  }
            if ($j(this).val() == 2) {
                $j(".inviteEmailTemplate ").val("");
                validateTermVote.resetForm();
                $j('#categorySlct').removeClass("error");
                $j('.categoryList').removeClass("error");
                $j('#termTemplate').removeClass("error");
                $j('#votingPeriodNum').removeClass("error");
                $j("#noUsersReq").hide();
                $j("#UsersReqError").hide();
                $j('.usersList').removeClass("error");


                $j("#templateReq").hide();
                $j("#voteReq").hide();
                $j("#votePReq").hide();
                $j("#usersReq").hide();
                $j("#noUsersReq").hide();
                $j(".inviteEmailTemplate").html("");
                updateSelectedTerms();
                showDialog();
                $t.getVoteConfigList({
                    success: function (data) {

                        if (data != null) {
                            $j("#votingPeriodNum").val(data.votingPeriod);


                        }
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });

                // Email Templates Picklist
                $t.getEmailTemplates({
                    success: function (data) {
                        displayEmailTemplateList(data);
                        var emailTmpl = data;
                        var emailTemplateId = "";
                        $j(".inviteEmailTemplate").change(function () {
                            for (var i = 0; i < emailTmpl.length; i++) {
                                if ($j(".inviteEmailTemplate").val() == 0) {
                                    $j(".emailPrvw").html("");
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
                $j('#termAction').val(0);
            }
        }

    });

    $j("#getFilteredList").click(function () {
    	paginationVal = false;    	
        $j("#newTerm").val("Enter term to search...");
        $j("#chkCase").attr("checked", false);
//			$j("#termCompanySlct").hide();
        $j('#termsModule').show();
        var exportBy = $j(".exprtCatSlct :selected").text();
        var selectedIds = $j(".exprtCatList").val();

        triggerTermDetails(null, null, 0, null, false, exportBy, selectedIds, null, null, 'N');

    });

    $j("#getFilteredTMXList").click(function () {
    	paginationVal = false; 
        $j("#newTmTerm").val("Enter term to search...");
        $j("#chktmCase").attr("checked", false);

        $j('#tmModule').show();
        var exportBy = $j(".exprtCatTMXSlct :selected").text();
        var selectedIds = $j(".exprtTMXCatList").val();

        triggerTmDetails(null, null, 0, null, false, exportBy, selectedIds, null, null, 'Y');

    });

    var triggerTermDetails = function (colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds, isTM) {
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
                var totalTerms2 = Util.insertCommmas(totalTerms1);
                //totalResultedTerms = totalTerms2;
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
                    'filterByCompany': filterByCompany,
                    'selectedCompanyIds': selectedCompanyIds,
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
    var triggerPaginationTermDetails = function (colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds, isTM, paginationValue) {
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
                var totalTerms2 = Util.insertCommmas(totalTerms1);
                //totalResultedTerms = totalTerms2;
                $j("#totalTerms").html(totalTerms2);
                $j("#termsInGlossary").val(totalTerms1);
                var criteria = {
                    'colName': colName,
                    'sortOrder': sortOrder,
                    'pageNum': pageNum,
                    'paginationValue': $j("#paginationId").val(),
                    'searchStr': searchStr,
                    'caseSensitiveFlag': caseSensitiveFlag,
                    'filterBy': filterBy,
                    'selectedIds': selectedIds,
                    'filterByCompany': filterByCompany,
                    'selectedCompanyIds': selectedCompanyIds,
                    'isTM': isTM,
                    'paginationValue' : paginationValue
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
var triggerTermBaseByPagination = function(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds, isTM){
    $j("#selectAll").attr('checked', false);
    if (colName == null) {
        $j("#mngTrmDtlSectionHead div").each(function (index) {
            $j(this).removeClass("ascending descending");
            $j(this).find('.sort').remove();
            $j(this).attr('sortOrder', 'ASC');
        });
    }
  // $j('#termDtlRowsList').empty();
  //  ChartRender.destroyTwoDPieChart();
    $t.getTotalTermsInGlossary({
        success: function (data) {
            var totalTerms1 = new String(data);
            var totalTerms2 = Util.insertCommmas(totalTerms1);
            //totalResultedTerms = totalTerms2;
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
                'filterByCompany': filterByCompany,
                'selectedCompanyIds': selectedCompanyIds,
                'isTM': isTM,
                'scrollLimitFrom': scrollLimitFrom,
                'scrollLimitTo': scrollLimitTo
            };
            savedCriteria = criteria;
            $j("#manageTermTbl").trigger("showTermDetailsForPagination", criteria);
        },
        error: function (xhr, textStatus, errorThrown) {
            console.log(xhr.responseText);
            if (Boolean(xhr.responseText.message)) {
                console.log(xhr.responseText.message);
            }
        }
    });
};
    var triggerTermBaseDetails = function (colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds, isTM) {
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

                var totalTerms2 = Util.insertCommmas(totalTerms1);
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
                    'filterByCompany': filterByCompany,
                    'selectedCompanyIds': selectedCompanyIds,
                    'isTM': isTM
                };
                savedCriteria = criteria;
                $j("#manageTermTbl").trigger("showTermBaseDetails", criteria);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

    };


    var triggerTmTermDetails = function (colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds) {
        $j("#selectAllTms").attr('checked', false);
        if (colName == null) {
            $j("#mngTmDtlSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
        }
        $j('#tmDtlRowsList').empty();
        ChartRender.destroyTwoDPieChart();
        /*if(searchStr!=null){
         alert("in 1");
         var  queryAppender= new Object();
         queryAppender.colName = colName;
         queryAppender.sortOrder = sortOrder;
         queryAppender.pageNum = pageNum;
         queryAppender.searchStr = searchStr;
         queryAppender.caseFlag = caseSensitiveFlag;
         queryAppender.filterBy = filterBy;
         queryAppender.selectedIds = selectedIds;
         queryAppender.filterByCompany=filterByCompany;
         queryAppender.selectedCompanyIds=selectedCompanyIds;

         var searchStr = searchStr;
         var filterBy = filterBy;
         var queryAppenderParameter =  Object.toJSON(queryAppender);
         $t.getTotalTMTermsBySearch(queryAppenderParameter,{
         success:function(data){
         var totalTerms1 = new String( data);
         var totalTerms2 = insertCommmas(totalTerms1);
         $j("#totalTMTerms").html(totalTerms2);
         $j("#termsInTMGlossary").val(totalTerms1);
         var criteria = {
         'colName':colName,
         'sortOrder':sortOrder,
         'pageNum': pageNum,
         'searchStr':searchStr,
         'caseSensitiveFlag':caseSensitiveFlag,
         'filterBy': filterBy,
         'selectedIds' :selectedIds,
         'filterByCompany':filterByCompany,
         'selectedCompanyIds':selectedCompanyIds

         };

         savedCriteria = criteria;
         $j("#manageTmTbl").trigger("showTmDetails", criteria);
         },
         error: function(xhr, textStatus, errorThrown){
         console.log(xhr.responseText);
         if(Boolean(xhr.responseText.message)){
         console.log(xhr.responseText.message);
         }
         }
         });
         }


         if(filterBy!=null){
         alert("in 2");
         var  queryAppender= new Object();
         queryAppender.colName = colName;
         queryAppender.sortOrder = sortOrder;
         queryAppender.pageNum = pageNum;
         queryAppender.searchStr = searchStr;
         queryAppender.caseFlag = caseSensitiveFlag;
         queryAppender.filterBy = filterBy;
         queryAppender.selectedIds = selectedIds;
         queryAppender.filterByCompany=filterByCompany;
         queryAppender.selectedCompanyIds= selectedCompanyIds;
         var searchStr = searchStr;
         var filterBy = filterBy;
         var queryAppenderParameter =  Object.toJSON(queryAppender);
         $t.getTotalTMTermsBySearch(queryAppenderParameter,{
         success:function(data){
         var totalTerms1 = new String( data);
         var totalTerms2 = insertCommmas(totalTerms1);
         $j("#totalTMTerms").html(totalTerms2);
         $j("#termsInTMGlossary").val(totalTerms1);
         var criteria = {
         'colName':colName,
         'sortOrder':sortOrder,
         'pageNum': pageNum,
         'searchStr':searchStr,
         'caseSensitiveFlag':caseSensitiveFlag,
         'filterBy': filterBy,
         'selectedIds' :selectedIds,
         'filterByCompany ':filterByCompany,
         'selectedCompanyIds':selectedCompanyIds

         };

         savedCriteria = criteria;
         $j("#manageTmTbl").trigger("showTmDetails", criteria);
         },
         error: function(xhr, textStatus, errorThrown){
         console.log(xhr.responseText);
         if(Boolean(xhr.responseText.message)){
         console.log(xhr.responseText.message);
         }
         }
         });
         }

         if(filterByCompany!=null){
         alert("in 3");
         var  queryAppender= new Object();
         queryAppender.colName = colName;
         queryAppender.sortOrder = sortOrder;
         queryAppender.pageNum = pageNum;
         queryAppender.searchStr = searchStr;
         queryAppender.caseFlag = caseSensitiveFlag;
         queryAppender.filterBy = filterBy;
         queryAppender.selectedIds = selectedIds;
         queryAppender.filterByCompany=filterByCompany;
         queryAppender.selectedCompanyIds= selectedCompanyIds;
         var searchStr = searchStr;
         var filterBy = filterBy;
         var queryAppenderParameter =  Object.toJSON(queryAppender);
         $t.getTotalTMTermsBySearch(queryAppenderParameter,{
         success:function(data){
         var totalTerms1 = new String( data);
         var totalTerms2 = insertCommmas(totalTerms1);
         $j("#totalTMTerms").html(totalTerms2);
         $j("#termsInTMGlossary").val(totalTerms1);
         var criteria = {
         'colName':colName,
         'sortOrder':sortOrder,
         'pageNum': pageNum,
         'searchStr':searchStr,
         'caseSensitiveFlag':caseSensitiveFlag,
         'filterBy': filterBy,
         'selectedIds' :selectedIds,
         'filterByCompany ':filterByCompany,
         'selectedCompanyIds':selectedCompanyIds

         };

         savedCriteria = criteria;
         $j("#manageTmTbl").trigger("showTmDetails", criteria);
         },
         error: function(xhr, textStatus, errorThrown){
         console.log(xhr.responseText);
         if(Boolean(xhr.responseText.message)){
         console.log(xhr.responseText.message);
         }
         }
         });
         }*/


        //if(searchStr==null && filterBy==null && filterByCompany==null){
        $t.getTotalTermsInTM({
            success: function (data) {
                var totalTerms1 = new String(data);
                var totalTerms2 = Util.insertCommmas(totalTerms1);
                $j("#totalTMTerms").html(totalTerms2);
                $j("#termsInTMGlossary").val(totalTerms1);
                var criteria = {
                    'colName': colName,
                    'sortOrder': sortOrder,
                    'pageNum': pageNum,
                    'searchStr': searchStr,
                    'caseSensitiveFlag': caseSensitiveFlag,
                    'filterBy': filterBy,
                    'selectedIds': selectedIds,
                    'filterByCompany': filterByCompany,
                    'selectedCompanyIds': selectedCompanyIds

                };

                savedCriteria = criteria;

                $j("#manageTmTbl").trigger("showTmTermsDetails", criteria);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
        /*	}
         else
         {
         var  queryAppender= new Object();
         queryAppender.colName = colName;
         queryAppender.sortOrder = sortOrder;
         queryAppender.pageNum = pageNum;
         queryAppender.searchStr = searchStr;
         queryAppender.caseFlag = caseSensitiveFlag;
         queryAppender.filterBy = filterBy;
         queryAppender.selectedIds = selectedIds;
         queryAppender.filterByCompany=filterByCompany;
         queryAppender.selectedCompanyIds= selectedCompanyIds;
         var searchStr = searchStr;
         var filterBy = filterBy;
         var queryAppenderParameter =  Object.toJSON(queryAppender);
         $t.getTotalTMTermsBySearch(queryAppenderParameter,{
         success:function(data){
         var totalTerms1 = new String( data);
         var totalTerms2 = insertCommmas(totalTerms1);
         $j("#totalTMTerms").html(totalTerms2);
         $j("#termsInTMGlossary").val(totalTerms1);
         var criteria = {
         'colName':colName,
         'sortOrder':sortOrder,
         'pageNum': pageNum,
         'searchStr':searchStr,
         'caseSensitiveFlag':caseSensitiveFlag,
         'filterBy': filterBy,
         'selectedIds' :selectedIds,
         'filterByCompany':filterByCompany,
         'selectedCompanyIds':selectedCompanyIds

         };

         savedCriteria = criteria;

         $j("#manageTmTbl").trigger("showTmTermsDetails", criteria);
         },
         error: function(xhr, textStatus, errorThrown){
         console.log(xhr.responseText);
         if(Boolean(xhr.responseText.message)){
         console.log(xhr.responseText.message);
         }
         }
         });
         }*/


    };

    var triggerTmDetails = function (colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds) {
        $j("#selectAllTms").attr('checked', false);
        if (colName == null) {
            $j("#mngTmDtlSectionHead div").each(function (index) {
                $j(this).removeClass("ascending descending");
                $j(this).find('.sort').remove();
                $j(this).attr('sortOrder', 'ASC');
            });
        }
        $j('#tmDtlRowsList').empty();
        ChartRender.destroyTwoDPieChart();
        /*if(searchStr!=null){
         alert("in 1");
         var  queryAppender= new Object();
         queryAppender.colName = colName;
         queryAppender.sortOrder = sortOrder;
         queryAppender.pageNum = pageNum;
         queryAppender.searchStr = searchStr;
         queryAppender.caseFlag = caseSensitiveFlag;
         queryAppender.filterBy = filterBy;
         queryAppender.selectedIds = selectedIds;
         queryAppender.filterByCompany=filterByCompany;
         queryAppender.selectedCompanyIds=selectedCompanyIds;

         var searchStr = searchStr;
         var filterBy = filterBy;
         var queryAppenderParameter =  Object.toJSON(queryAppender);
         $t.getTotalTMTermsBySearch(queryAppenderParameter,{
         success:function(data){
         var totalTerms1 = new String( data);
         var totalTerms2 = insertCommmas(totalTerms1);
         $j("#totalTMTerms").html(totalTerms2);
         $j("#termsInTMGlossary").val(totalTerms1);
         var criteria = {
         'colName':colName,
         'sortOrder':sortOrder,
         'pageNum': pageNum,
         'searchStr':searchStr,
         'caseSensitiveFlag':caseSensitiveFlag,
         'filterBy': filterBy,
         'selectedIds' :selectedIds,
         'filterByCompany':filterByCompany,
         'selectedCompanyIds':selectedCompanyIds

         };

         savedCriteria = criteria;
         $j("#manageTmTbl").trigger("showTmDetails", criteria);
         },
         error: function(xhr, textStatus, errorThrown){
         console.log(xhr.responseText);
         if(Boolean(xhr.responseText.message)){
         console.log(xhr.responseText.message);
         }
         }
         });
         }


         if(filterBy!=null){
         alert("in 2");
         var  queryAppender= new Object();
         queryAppender.colName = colName;
         queryAppender.sortOrder = sortOrder;
         queryAppender.pageNum = pageNum;
         queryAppender.searchStr = searchStr;
         queryAppender.caseFlag = caseSensitiveFlag;
         queryAppender.filterBy = filterBy;
         queryAppender.selectedIds = selectedIds;
         queryAppender.filterByCompany=filterByCompany;
         queryAppender.selectedCompanyIds= selectedCompanyIds;
         var searchStr = searchStr;
         var filterBy = filterBy;
         var queryAppenderParameter =  Object.toJSON(queryAppender);
         $t.getTotalTMTermsBySearch(queryAppenderParameter,{
         success:function(data){
         var totalTerms1 = new String( data);
         var totalTerms2 = insertCommmas(totalTerms1);
         $j("#totalTMTerms").html(totalTerms2);
         $j("#termsInTMGlossary").val(totalTerms1);
         var criteria = {
         'colName':colName,
         'sortOrder':sortOrder,
         'pageNum': pageNum,
         'searchStr':searchStr,
         'caseSensitiveFlag':caseSensitiveFlag,
         'filterBy': filterBy,
         'selectedIds' :selectedIds,
         'filterByCompany ':filterByCompany,
         'selectedCompanyIds':selectedCompanyIds

         };

         savedCriteria = criteria;
         $j("#manageTmTbl").trigger("showTmDetails", criteria);
         },
         error: function(xhr, textStatus, errorThrown){
         console.log(xhr.responseText);
         if(Boolean(xhr.responseText.message)){
         console.log(xhr.responseText.message);
         }
         }
         });
         }

         if(filterByCompany!=null){
         alert("in 3");
         var  queryAppender= new Object();
         queryAppender.colName = colName;
         queryAppender.sortOrder = sortOrder;
         queryAppender.pageNum = pageNum;
         queryAppender.searchStr = searchStr;
         queryAppender.caseFlag = caseSensitiveFlag;
         queryAppender.filterBy = filterBy;
         queryAppender.selectedIds = selectedIds;
         queryAppender.filterByCompany=filterByCompany;
         queryAppender.selectedCompanyIds= selectedCompanyIds;
         var searchStr = searchStr;
         var filterBy = filterBy;
         var queryAppenderParameter =  Object.toJSON(queryAppender);
         $t.getTotalTMTermsBySearch(queryAppenderParameter,{
         success:function(data){
         var totalTerms1 = new String( data);
         var totalTerms2 = insertCommmas(totalTerms1);
         $j("#totalTMTerms").html(totalTerms2);
         $j("#termsInTMGlossary").val(totalTerms1);
         var criteria = {
         'colName':colName,
         'sortOrder':sortOrder,
         'pageNum': pageNum,
         'searchStr':searchStr,
         'caseSensitiveFlag':caseSensitiveFlag,
         'filterBy': filterBy,
         'selectedIds' :selectedIds,
         'filterByCompany ':filterByCompany,
         'selectedCompanyIds':selectedCompanyIds

         };

         savedCriteria = criteria;
         $j("#manageTmTbl").trigger("showTmDetails", criteria);
         },
         error: function(xhr, textStatus, errorThrown){
         console.log(xhr.responseText);
         if(Boolean(xhr.responseText.message)){
         console.log(xhr.responseText.message);
         }
         }
         });
         }*/

        //	if(searchStr==null && filterBy==null && filterByCompany==null){
        $t.getTotalTermsInTM({
            success: function (data) {
                var totalTerms1 = new String(data);
                var totalTerms2 = Util.insertCommmas(totalTerms1);
                $j("#totalTMTerms").html(totalTerms2);
                $j("#termsInTMGlossary").val(totalTerms1);
                var criteria = {
                    'colName': colName,
                    'sortOrder': sortOrder,
                    'pageNum': pageNum,
                    'searchStr': searchStr,
                    'caseSensitiveFlag': caseSensitiveFlag,
                    'filterBy': filterBy,
                    'selectedIds': selectedIds,
                    'filterByCompany': filterByCompany,
                    'selectedCompanyIds': selectedCompanyIds
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
        /*}
         else
         {
         var  queryAppender= new Object();
         queryAppender.colName = colName;
         queryAppender.sortOrder = sortOrder;
         queryAppender.pageNum = pageNum;
         queryAppender.searchStr = searchStr;
         queryAppender.caseFlag = caseSensitiveFlag;
         queryAppender.filterBy = filterBy;
         queryAppender.selectedIds = selectedIds;
         queryAppender.filterByCompany=filterByCompany;
         queryAppender.selectedCompanyIds= selectedCompanyIds;
         var searchStr = searchStr;
         var filterBy = filterBy;
         var queryAppenderParameter =  Object.toJSON(queryAppender);
         $t.getTotalTMTermsBySearch(queryAppenderParameter,{
         success:function(data){
         var totalTerms1 = new String( data);
         var totalTerms2 = insertCommmas(totalTerms1);
         $j("#totalTMTerms").html(totalTerms2);
         $j("#termsInTMGlossary").val(totalTerms1);
         var criteria = {
         'colName':colName,
         'sortOrder':sortOrder,
         'pageNum': pageNum,
         'searchStr':searchStr,
         'caseSensitiveFlag':caseSensitiveFlag,
         'filterBy': filterBy,
         'selectedIds' :selectedIds,
         'filterByCompany':filterByCompany,
         'selectedCompanyIds':selectedCompanyIds

         };

         savedCriteria = criteria;

         $j("#manageTmTbl").trigger("showTmDetails", criteria);
         },
         error: function(xhr, textStatus, errorThrown){
         console.log(xhr.responseText);
         if(Boolean(xhr.responseText.message)){
         console.log(xhr.responseText.message);
         }
         }
         });
         }*/
    };
    var triggerSelectedTermDetails = function (colName, sortOrder, selectedIds) {
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

        var criteria = {
            'colName': colName,
            'sortOrder': sortOrder,
            'selectedIds': selectedIds,
        };
        selectedSavedCriteria = criteria;
        $j("#manageTermTbl").trigger("showSelectedTermDetails", selectedSavedCriteria);
    };
    $j(".termNext").click(function () {
        if ($j(".termNext").hasClass("nextEnable")) {
            $j("#selectAll").attr('checked', false);
            //$j(".case").attr('checked',false);
            $j("#showSelectedTerms").attr('checked', false);

            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var pageNum = (savedCriteria.pageNum == 0) ? (savedCriteria.pageNum + 2) : (savedCriteria.pageNum + 1);
            savedCriteria.pageNum = pageNum;
            var searchStr = savedCriteria.searchStr;
            var caseSensitiveFlag = savedCriteria.caseSensitiveFlag;
            var filterBy = savedCriteria.filterBy;
            var selectedIds = savedCriteria.selectedIds;
            var filterByCompany = savedCriteria.filterByCompany;
            var selectedCompanyIds = savedCriteria.selectedCompanyIds;

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
           
            if(selectValue != null) {
            	 var filterBy = null;
            	 var searchBy = $j('#newTermVoted').val();
           	     if(searchBy == 'Enter term ...') {
             		 searchBy = null;
             	 }
           	    console.log(searchBy);
            	if(langSelected == 'selected') {
            		filterBy = "Locale";
            	}
            	triggerPaginationTermDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds, isTM, selectValue);
            } else {
            	triggerTermDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds, isTM);
            }
           
        }
        ;
    });

    $j(".termprevious").click(function () {
        if ($j(".termprevious").hasClass("prevEnable")) {
            $j("#showSelectedTerms").attr('checked', false);
            $j("#selectAll").attr('checked', false);
            //$j(".case").attr('checked',false);
            var colName = savedCriteria.colName;
            var sortOrder = savedCriteria.sortOrder;
            var langIds = savedCriteria.langIds;
            var pageNum = savedCriteria.pageNum - 1;
            var searchStr = savedCriteria.searchStr;
            var caseSensitiveFlag = savedCriteria.caseSensitiveFlag;
            var filterBy = savedCriteria.filterBy;
            var selectedIds = savedCriteria.selectedIds;
            var filterByCompany = savedCriteria.filterByCompany;
            var selectedCompanyIds = savedCriteria.selectedCompanyIds;
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
            if(selectValue != null) {
           	 var filterBy = null;
        	 var searchBy = $j('#newTermVoted').val();
       	     if(searchBy == 'Enter term ...') {
         		 searchBy = null;
         	 }
       	    console.log(searchBy);
        	if(langSelected == 'selected') {
        		filterBy = "Locale";
        	}
        	triggerPaginationTermDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds, isTM, selectValue);
        } else {
            	triggerTermDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds, isTM);
            }
        }
    });

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
            var filterByCompany = savedCriteria.filterByCompany;
            var selectedCompanyIds = savedCriteria.selectedCompanyIds;

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
            triggerTmDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds);
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
            var filterByCompany = savedCriteria.filterByCompany;
            var selectedCompanyIds = savedCriteria.selectedCompanyIds;
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

            triggerTmDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds, filterByCompany, selectedCompanyIds);
        }
    });

    $j("#mngTrmDtlSectionHead div").click(function () {
        var sortOrder = $j(this).attr('sortOrder');
        var colName = $j(this).attr('id');
        if (colName == "column2" || colName == "column0" || colName == "termEdit" || colName == "termDelete") {
            return;
        }
        isDivClicked = true;

        var separator = "";

        $j("#termDtlRowsList input:checked").each(function (i) {
            if (selectedTermIds == "") {
                separator = "";
            } else {
                separator = ",";
            }
            selectedTermIds += separator + $j(this).parent().next().attr("termId");

        });
        if (colName == savedCriteria.colName && savedCriteria.sortOrder == 'ASC') {
            sortOrder = 'DESC';
            $j(this).find('.sort').remove();
            $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
        }
        if (colName == savedCriteria.colName && savedCriteria.sortOrder == 'DESC') {
            sortOrder = 'ASC';
            $j(this).find('.sort').remove();
            $j(this).append('<img src="' + $j("#contextRootPath").val() + '/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
        } else if (colName != savedCriteria.colName || $j('#showSelectedTerms').attr('checked')) {
            sortOrder = $j(this).attr('sortOrder');
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
        }
        if ($j('#showSelectedTerms').attr('checked')) {
        	paginationVal = false;
            triggerSelectedTermDetails(colName, sortOrder, selectedSavedCriteria.selectedIds);
        }
        else {
            triggerTermDetails(colName, sortOrder, 1, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.filterByCompany, savedCriteria.selectedCompanyIds, savedCriteria.isTM);
        }
//		triggerTermDetails(colName, sortOrder, 1, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag,savedCriteria.filterBy, savedCriteria.selectedIds,savedCriteria.isTM);
    });

    $j("#mngTmDtlSectionHead div").click(function () {
        var sortOrder = $j(this).attr('sortOrder');
        var colName = $j(this).attr('id');
        if (colName == "column0" || colName == "termEdit" || colName == "termDelete") {
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

        triggerTmDetails(colName, sortOrder, 1, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.filterByCompany, savedCriteria.selectedCompanyIds);
    });

    var deleteVal = function (id) {
        selectedTermIds = "";
        $j("#delete_cnfm:ui-dialog").dialog("destroy");
        $j("#delete_cnfm").dialog({
            resizable: false,
            height: 150,
            width: 400,
            modal: true,
            buttons: {
                "Delete": function () {
                    var separator = "";
                    var selectedcount = 0;
                    var deletedcount = 0;

                    var deleteflag = true;
                    var d = new Date();
                    var month = d.getMonth() + 1;
                    var day = d.getDate();
                    var year = d.getFullYear()
                    var pollexpDate = $j("#viewDate_" + id).html().split('/');
                    if (pollexpDate.length == 3) {
                        if (pollexpDate[2] >= year) {
                            if (pollexpDate[2] == year) {
                                if (pollexpDate[0] >= month) {
                                    if (pollexpDate[0] == month) {
                                        if (pollexpDate[1] >= day) {
                                            deletedcount++;
                                            deleteflag = false;
                                        }
                                    } else {
                                        deletedcount++;
                                        deleteflag = false;
                                    }
                                }
                            } else {
                                deletedcount++;
                                deleteflag = false;
                            }
                        }
                    }
                    if (deleteflag) {
                        selectedTermIds = id;
                        $j("#" + id).parent().parent().remove();
                    }

                    if (selectedTermIds != "") {
                        deleteTermDetails(selectedTermIds, deletedcount, selectedcount);
                    } else {
                        $j('#failmsg').html("The selected Terms are not deleted as poll date is not expired.");
                        $j("#mail_failed").dialog({
                            modal: true,
                            buttons: {
                                OK: function () {
                                    $j(this).dialog("close");
                                }
                            }
                        });
                    }
                    $j('#termAction').val(0);

                    $j(this).dialog("close");
                },
                Cancel: function () {
                    $j(this).dialog("close");
                }
            },
            close: function (event, ui) {
                $j('#termAction').val(0);
            }
        });
    };

    var updateSelectedTerms = function () {
        var selectedTermIdsInArray = new Array();

        selectedTermIdsInArray = selectedTermIds.split(",");
        for (var element = 0; element < selectedTermIdsInArray.length; element++) {
            selectedTermIdsInArray[element] = parseInt(selectedTermIdsInArray[element]);
        }
        uniqueSelectedTermIds = $j.unique(selectedTermIdsInArray);
        uniqueSelectedTermIds = $j.unique(uniqueSelectedTermIds);
    };
    var deleteTmVal = function (id) {
        selectedTermIds = "";
        $j("#delete_cnfm:ui-dialog").dialog("destroy");
        $j("#delete_cnfm").dialog({
            resizable: false,
            height: 150,
            width: 400,
            modal: true,
            buttons: {
                "Delete": function () {
                    var separator = "";
                    var selectedcount = 0;
                    var deletedcount = 0;
                    var deleteflag = true;
                    selectedTermIds = id;
                    $j("#" + id).parent().parent().remove();

                    if (selectedTermIds != "") {
                        deleteTmDetails(selectedTermIds, deletedcount, selectedcount);
                    }

                    $j(this).dialog("close");
                },
                Cancel: function () {
                    $j(this).dialog("close");
                }
            },
            close: function (event, ui) {
                $j('#termAction').val(0);
            }
        });
    };
    var deleteTermsVal = function () {
        $j("#delete_cnfm:ui-dialog").dialog("destroy");

        $j("#delete_cnfm").dialog({
            resizable: false,
            height: 150,
            width: 400,
            modal: true,
            buttons: {
                "Delete": function () {
                    var separator = "";
                    var selectedcount = 0;
                    var deletedcount = 0;
                    var delselected = "";

                    $j("#termDtlRowsList input:checkbox").each(function (i) {
                        $j("#showSelectedTerms").attr('checked', false);
                        if ($j(this).attr('checked') != "checked") {
                            var tempIds = "";
                            var separator = "";
                            if (selectedTermIds != "") {
                                var temptermIdsArray = selectedTermIds.split(",");
                                for (var j = 0; j < temptermIdsArray.length; j++) {
                                    if (temptermIdsArray[j] != ($j(this).parent().next().attr("termId"))) {
                                        tempIds = tempIds + separator + temptermIdsArray[j];
                                        separator = ",";
                                    }
                                }
                            }
                            selectedTermIds = tempIds;
                        }
                    });
                    var selectedTermIdsInArray = new Array();

                    selectedTermIdsInArray = selectedTermIds.split(",");
                    for (var element = 0; element < selectedTermIdsInArray.length; element++) {
                        selectedTermIdsInArray[element] = parseInt(selectedTermIdsInArray[element]);
                    }
                    uniqueSelectedTermIds = $j.unique(selectedTermIdsInArray);
                    uniqueSelectedTermIds = $j.unique(uniqueSelectedTermIds);
                    for (var i = 0; i < uniqueSelectedTermIds.length; i++) {
                        var deleteflag = true;
                        selectedcount++;
                        var d = new Date();
                        var month = d.getMonth() + 1;
                        var day = d.getDate();
                        var year = d.getFullYear()
                        var id = uniqueSelectedTermIds[i];
                        var dataObj = dataMap.get(id);
                        var pollexpDate = dataObj.pollExpirationDt;
                        if (pollexpDate != null) {
                            pollexpDate = pollexpDate.split('/');
                            if (pollexpDate.length == 3) {
                                if (pollexpDate[2] >= year) {
                                    if (pollexpDate[2] == year) {
                                        if (pollexpDate[0] >= month) {
                                            if (pollexpDate[0] == month) {
                                                if (pollexpDate[1] >= day) {
                                                    deletedcount++;
                                                    deleteflag = false;
                                                }
                                            } else {
                                                deletedcount++;
                                                deleteflag = false;
                                            }
                                        }
                                    } else {
                                        deletedcount++;
                                        deleteflag = false;
                                    }


                                }
                            }
                        }
                        if (deleteflag) {
                            if (delselected == "") {
                                separator = "";
                            } else {
                                separator = ",";
                            }
                            delselected += separator + id;

                            /*  var viewNode = $j(this).parent().parent().next().next();
                             viewNode.remove();
                             $j(this).parent().parent().remove();
                             $j(this).parent().parent().remove();*/
                        }
                    }

                    if (delselected != "") {
                        var deletedIdsArray = delselected.split(",");
                        deleteTermDetails(delselected.split(","), deletedcount, selectedcount);
                        //					 uniqueSelectedTermIds = [];
                        var tempIds = "";
                        var separator = "";
                        if (selectedTermIds != "") {
                            var temptermIdsArray = selectedTermIds.split(",");
                            for (var k = 0; k < temptermIdsArray.length; k++) {
                                var isExists = false;
                                for (var j = 0; j < deletedIdsArray.length; j++) {
                                    if (deletedIdsArray[j] == temptermIdsArray[k]) {
                                        isExists = true;
                                    }
                                }
                                if (!isExists) {
                                    tempIds = tempIds + separator + temptermIdsArray[k];
                                    separator = ",";
                                }
                            }
                        }
                        selectedTermIds = tempIds;
                    } else {
                        $j('#failmsg').html("The selected Terms are not deleted as poll date is not expired.");
                        $j("#mail_failed").dialog({
                            modal: true,
                            buttons: {
                                OK: function () {
                                    $j(this).dialog("close");
                                }
                            }
                        });
                    }
                    $j('#termAction').val(0);

                    $j(this).dialog("close");
                },
                Cancel: function () {
                    $j('#termAction').val(0);
                    $j(this).dialog("close");
                }
            },
            close: function (event, ui) {
                $j('#termAction').val(0);
            }
        });
    };


    var deleteMultiTmsVal = function () {
        selectedTermIds = "";
        $j("#delete_cnfm:ui-dialog").dialog("destroy");
        $j("#delete_cnfm").dialog({
            resizable: false,
            height: 150,
            width: 400,
            modal: true,
            buttons: {
                "Delete": function () {
                    var separator = "";
                    var selectedcount = 0;
                    var deletedcount = 0;
                    $j("#tmDtlRowsList input:checked").each(function (i) {
                        var deleteflag = true;
                        selectedcount++;
                        deletedcount++;
                        var d = new Date();
                        var month = d.getMonth() + 1;
                        var day = d.getDate();
                        var year = d.getFullYear()

                        if (deleteflag) {
                            selectedTermIds += separator + $j(this).parent().next().attr("termId");
                            separator = ",";
                            var viewNode = $j(this).parent().parent().next().next();
                            viewNode.remove();
                            $j(this).parent().parent().remove();
                            $j(this).parent().parent().remove();
                        }
                    });
                    if (selectedTermIds != "") {
                        deleteTmDetails(selectedTermIds, deletedcount, selectedcount);
                    }

                    $j(this).dialog("close");
                },
                Cancel: function () {
                    $j('#termAction').val(0);
                    $j(this).dialog("close");
                }
            },
            close: function (event, ui) {
                $j('#termAction').val(0);
            }
        });
    };


    var deleteAllTerms = function () {
    	selectedTermIds = "";
    	var filterBy="";
    	 var searchBy = $j('#newTermVoted').val();
    	if (searchBy == "Enter term ..." || $j.trim(searchBy) == "") {
            searchValue = null;
        } 
    	if(searchLangValues != null){
    		filterBy = "Locale";
    	}
    	//var str ="";
    	/*$j(".termLangSlctDropdwn option:selected" ).each(function() {
            str += $j( this ).text() + ",";
          });
    	//str = str.remove(str.lastIndexOf(','));
    	str=str.substring(0, str.length-1);
    	alert(str);
        $j("#delete_cnfm:ui-dialog").dialog("destroy");
        if((str != "")  && (searchValue != null)) {
           $j('#delete_cnfm').find('p').html('You are deleting the terms with below criteria'+ "<br/>" + 'language(s) :'+ str +  "<br/>" + '          Keyword : ' +searchValue+"<br/>"+' Are you sure to delete ?');
        }
        else if((str == "")  && (searchValue != null)){
           $j('#delete_cnfm').find('p').html('You are deleting the terms with below criteria'+ "<br/>" + 'language(s) : All'+  "<br/>" + '          Keyword : ' +searchValue+"<br/>"+' Are you sure to delete ?');
        } else if(str != "" && searchValue == null){
        	 $j('#delete_cnfm').find('p').html('You are deleting the terms with below criteria'+ "<br/>" + 'language(s) :'+ str +  "<br/>" + '          Keyword : All terms '+"<br/>"+' Are you sure to delete ?');
        }
        else {
           $j('#delete_cnfm').find('p').html('You are deleting the terms with below criteria'+ "<br/>" + 'language(s) : All' +"<br/>" + '          Keyword : All terms '+"<br/>"+' Are you sure to delete ?');
        }*/
        $j("#delete_cnfm").dialog({
            resizable: false,
            height: 150,
            width: 400,
            modal: true,
            buttons: {
                "Delete": function () {
                    var separator = "";
                    var selectedcount = 0;
                    var deletedcount = 0;
                    triggerTermBaseDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, searchValue, savedCriteria.caseSensitiveFlag, filterBy, searchLangValues, savedCriteria.filterByCompany, savedCriteria.selectedCompanyIds, savedCriteria.isTM);
                    $j(this).dialog("close");
                },
                Cancel: function () {
                    $j('#termAction').val(0);
                    $j(this).dialog("close");
                }
            },
            close: function (event, ui) {
                $j('#termAction').val(0);
            }
        });
    };


    var deleteAllTms = function () {
        selectedTermIds = "";
        $j("#delete_cnfm:ui-dialog").dialog("destroy");
        $j("#delete_cnfm").dialog({
            resizable: false,
            height: 150,
            width: 400,
            modal: true,
            buttons: {
                "Delete": function () {
                    var separator = "";
                    var selectedcount = 0;
                    var deletedcount = 0;
                    triggerTmTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag, savedCriteria.filterBy, savedCriteria.selectedIds, savedCriteria.filterByCompany, savedCriteria.selectedCompanyIds, savedCriteria.isTM);
                    $j(this).dialog("close");

                },
                Cancel: function () {
                    $j('#termAction').val(0);
                    $j(this).dialog("close");
                }
            },
            close: function (event, ui) {
                $j('#termAction').val(0);
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
                    //					$j("#termDtlRowsList input:checked").attr('checked', false);
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
    $j("#deleteMultipleTerms").click(function () {
        $j("#termDtlRowsList input:checked").each(function (i) {
            if (selectedTermIds == "") {
                separator = "";
            } else {
                separator = ",";
            }
            selectedTermIds += separator + $j(this).parent().next().attr("termId");
        });
        if (selectedTermIds.length == 0) {
            alertMessage("#termSelctMsg");
        } else {
            deleteTermsVal();
        }
    });

    $j("#deleteAllTerms").click(function () {
    	$j("#paginationId").val($j("#paginationId option:first").val()); 
        //	if($j("#manageTermTbl input:checked").length == 0){
//		updateSelectedTerms();
//		if(selectedTermIds==""){
//				deleteAllTerms();
//			}else{
//				  $j("#termDtlRowsList input:checked").each(function(i){
//						
//						if(selectedTermIds==""){
//							separator = "";
//						}else{
//							separator = ",";
//						}
//						selectedTermIds += separator+$j(this).parent().next().attr("termId");
//						
//					});
//		}
        deleteAllTerms();
    });
    $j("#deleteAllTms").click(function () {
        deleteAllTms();
//		if($j("#manageTmTbl input:checked").length == 0){
//			}else{
//				
//			 deleteMultiTmsVal();
//		}
    });

    $j("#deleteMultipleTms").click(function () {
        if ($j("#manageTmTbl input:checked").length == 0) {
            alertMessage("#termSelctMsg");
        } else {
            deleteMultiTmsVal();
        }
    });

    $j().ready(function () {
        //	$j('.redTxt').hide();
        showRolePrivileges();

        $j("#currentMenuPage").val(Constants.SUBMENU_ITEMS.MANAGE_TERMS);
        $j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.ADMIN);

        /**
         if (adminHeaderFlag == "true") {
            $j(".companyTermsSlctDropdwn").show();
            $j(".companyTmsSlctDropdwn").show();
        } else {
            $j(".companyTermsSlctDropdwn").hide();
            $j(".companyTmsSlctDropdwn").hide();
        }
         */
        if ($j("#adminHeaderFlag").val() == "true") {
            $j("#adminHeader").show();
            $j('#admin').children("img").show();
        }
        if ($j("#userHeaderFlag").val() == "true") {
            $j("#userHeader").show();
            $j('#termList').children("img").show();
        }
        if ($j("#isSuperAdmin").val() == "true") {
            $j(".companyFilter").show();
            $j("#termCompanySlct").show();
        } else {
            $j(".companyFilter").hide();
            $j("#termCompanySlct").hide();
        }

        if ($j.browser.version == "7.0") {
            $j(".menuArrowAdmn").css("top", "26");
            $j(".termAttr").css("padding-bottom", "10px");
            $j("#selectAll").css("margin-top", "-2px");
            $j("#selectAllTms").css("margin-top", "-2px");
            $j("#selectAll").css("margin-left", "-16px");
            $j("#selectAllTms").css("margin-left", "-16px");
            $j("#deleteMultipleTms").css("padding", "3px 0px");
            $j("#deleteMultipleTerms").css("padding", "3px 0px");
            $j(".termsBtn").css("width", "980px");
            $j(".tmsBtn").css("width", "980px");
        }
        if ($j.browser.version == "9.0") {
            $j(".paddingseven").css("padding-left", "11px");
            $j("#selectAll").css("margin-top", "-2px");
            $j("#selectAllTms").css("margin-top", "-2px");
            $j("#selectAll").css("margin-left", "-16px");
            $j("#selectAllTms").css("margin-left", "-16px");
            $j("#deleteMultipleTms").css("margin-left", "723px");
            $j("#deleteMultipleTerms").css("margin-left", "723px");
            $j("#deleteMultipleTerms").css("padding", "3px 10px");
            $j("#deleteMultipleTms").css("padding", "3px 10px");
        }

        if ($j.browser.version == "8.0") {
            $j(".paddingseven").css("padding-left", "11px");
            $j("#selectAll").css("margin-top", "-2px");
            $j("#selectAllTms").css("margin-top", "-2px");
            $j("#selectAll").css("margin-left", "-16px");
            $j("#selectAllTms").css("margin-left", "-16px");
            $j("#deleteMultipleTms").css("margin-left", "721px");
            $j("#deleteMultipleTerms").css("margin-left", "721px");
            $j("#deleteMultipleTerms").css("padding", "3px 10px");
            $j("#deleteMultipleTms").css("padding", "3px 10px");
        }
        if ($j.browser.msie || $j.browser.webkit) {
            $j(".headerMenuLinks .headerMenuLink").css("padding-bottom", "12px");
            $j(".termAttr .sourceDescEdit").addClass('width455');
        }
        if (/chrom(e|ium)/.test(navigator.userAgent.toLowerCase())) {
            $j("#deleteMultipleTms").css("margin-left", "715px");
           // $j("#deleteMultipleTerms").css("margin-left", "715px");
        }
        $j('#admin').css('color', '#0DA7D5');

        $j(".subMenuLinks a").last().css("border-right", "none");

        $j("#manageTermTbl").termDetails();
        $j("#manageTmTbl").tmDetails();

        triggerTermDetails(null, null, 0, null, false, null, null, null, null, 'N');


        $j("#newTerm").focus(function () {
              $j(this).val('');
        });
        $j("#newTerm").blur(function () {
            if ($j(this).val() == '')
                $j(this).val('Enter term to search...');
        });
        $j("#newTermVoted").focus(function () {
        	if($j(this).val() == 'Enter term ...')
            $j(this).val('');
        });
        $j("#newTermVoted").blur(function () {
            if ($j(this).val() == '')
                $j(this).val('Enter term ...');
        });
        $j("#newTmTerm").focus(function () {
            $j(this).val('');
        });
        $j("#newTmTerm").blur(function () {
            if ($j(this).val() == '')
                $j(this).val('Enter term to search...');
        });

        $j('.openCloseTerms').click(function () {
            $j('#tabs-1 .reports').slideToggle();
            $j(this).toggleClass('moduleClose');
            $j('#myChartId').css('position', 'relative');

        });
        $j('.openCloseTms').click(function () {
            $j('#tabs-6 .reports').slideToggle();
            $j(this).toggleClass('moduleCloseTms');
            $j('#myChartId1').css('position', 'relative');
        });

        $j('.categorySlct').change(function () {
            $j('.categoryList').html("");
            $j('.usersList').removeClass("error");
            $j('.usersList').html("");
            $j('#selectReq').hide();
            $j("#noUsersReq").hide();
            if ($j(this).val() == "role") {
                $t.getUserRoleList({
                    success: function (data) {
                        displayUserTypeList(data);

                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });

            }

            if ($j(this).val() == "language") {
                $t.getLanguageList({
                    success: function (data) {
                        displayLanguageList(data, '.categoryList');
                    }
                });


            }
            
            if ($j(this).val() == "domain") {
                $t.getDomainList({
                    success: function (data) {
                        displayDomainList(data, '.categoryList');
                    }
                });


            }

        });

        $j('.exprtCatSlct').change(function () {

            $j('.exprtCatList').html("");

            if ($j(this).val() == 0) {
                $j("#category_list").hide();
                $j("#getFilteredList").attr("disabled", "disabled");
                $j("#getFilteredList").addClass("disabledBtn");
                $j("#termsModule").hide();
                $j(".exprtCatList").change(function () {
                    $j("#category_list").show();
                    $j("#getFilteredList").removeAttr("disabled");
                    $j("#getFilteredList").removeClass("disabledBtn");
                });
            }
            if ($j(this).val() == 1) {
                $j("#getFilteredList").attr("disabled", "disabled");
                $j("#getFilteredList").addClass("disabledBtn");

                $j("#category_list").show();
                $j(".exprtCatList").change(function () {
                    $j("#getFilteredList").removeAttr("disabled");
                    $j("#getFilteredList").removeClass("disabledBtn");
                });
                $t.getLanguageList({
                    success: function (data) {
                        displayLanguageList(data, ".exprtCatList");
                    }
                });
            }
            if ($j(this).val() == 2) {
                $j("#category_list").show();
                $j("#getFilteredList").attr("disabled", "disabled");
                $j("#getFilteredList").addClass("disabledBtn");
                $j(".exprtCatList").change(function () {
                    $j("#getFilteredList").removeAttr("disabled");
                    $j("#getFilteredList").removeClass("disabledBtn");
                });
                $t.getPOSList({
                    success: function (data) {
                        displayPosList(data, ".exprtCatList");
                    }
                });
            }
            if ($j(this).val() == 3) {
                $j("#category_list").show();
                $j("#getFilteredList").attr("disabled", "disabled");
                $j("#getFilteredList").addClass("disabledBtn");
                $j(".exprtCatList").change(function () {
                    $j("#getFilteredList").removeAttr("disabled");
                    $j("#getFilteredList").removeClass("disabledBtn");
                });
                $t.getCategoryList({
                    success: function (data) {
                        displayTermCatList(data, ".exprtCatList");
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });

            }
            if ($j(this).val() == 4) {
                $j("#category_list").hide();
                $j("#getFilteredList").removeAttr("disabled");
                $j("#getFilteredList").removeClass("disabledBtn");
            }

            if ($j(this).val() == 5) {

                $j("#category_list").show();
                $j("#getFilteredList").attr("disabled", "disabled");
                $j("#getFilteredList").addClass("disabledBtn");
                $j(".exprtCatList").change(function () {
                    $j("#getFilteredList").removeAttr("disabled");
                    $j("#getFilteredList").removeClass("disabledBtn");
                });

                $t.getCompanyList({
                    success: function (data) {
                        displayCompanyList(data, ".exprtCatList");

                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });

            }
            if ($j(this).val() == 6) {
                $j("#category_list").show();
                $j("#getFilteredList").attr("disabled", "disabled");
                $j("#getFilteredList").addClass("disabledBtn");
                $j(".exprtCatList").change(function () {
                    $j("#getFilteredList").removeAttr("disabled");
                    $j("#getFilteredList").removeClass("disabledBtn");
                });

                $t.getDomainList({
                    success: function (data) {
                        displayDomainList(data, ".exprtCatList");

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


        $j('.exprtCatTMXSlct').change(function () {

            $j('.exprtTMXCatList').html("");
            if ($j(this).val() == 0) {
                $j("#getFilteredTMXList").attr("disabled", "disabled");
                $j("#getFilteredTMXList").addClass("disabledBtn");
                $j("#category_list_TMX").hide();
                $j("#tmModule").hide();
                $j(".exprtTMXCatList").change(function () {
                    $j("#getFilteredTMXList").removeAttr("disabled");
                    $j("#getFilteredTMXList").removeClass("disabledBtn");
                    $j("#category_list_TMX").show();
                });
            }
            if ($j(this).val() == 1) {
                $j("#getFilteredTMXList").attr("disabled", "disabled");
                $j("#getFilteredTMXList").addClass("disabledBtn");

                $j("#category_list_TMX").show();
                $j(".exprtTMXCatList").change(function () {
                    $j("#getFilteredTMXList").removeAttr("disabled");
                    $j("#getFilteredTMXList").removeClass("disabledBtn");
                });
                $t.getLanguageList({
                    success: function (data) {
                        displayLanguageList(data, ".exprtTMXCatList");
                    }
                });
            }

            if ($j(this).val() == 2) {
                $j("#category_list_TMX").show();
                $j(".exprtTMXCatList").change(function () {
                    $j("#getFilteredTMXList").removeAttr("disabled");
                    $j("#getFilteredTMXList").removeClass("disabledBtn");
                });
                $t.getCompanyList({
                    success: function (data) {
                        displayCompanyList(data, ".exprtTMXCatList");

                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });
            }
            if ($j(this).val() == 3) {
                $j("#category_list_TMX").show();
                $j(".exprtTMXCatList").change(function () {
                    $j("#getFilteredTMXList").removeAttr("disabled");
                    $j("#getFilteredTMXList").removeClass("disabledBtn");
                });
                $t.getDomainList({
                    success: function (data) {
                        displayDomainList(data, ".exprtTMXCatList");

                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });
            }
            if ($j(this).val() == 4) {
                $j("#getFilteredTMXList").attr("disabled", "disabled");
                $j("#getFilteredTMXList").addClass("disabledBtn");

                $j("#category_list_TMX").show();
                $j(".exprtTMXCatList").change(function () {
                    $j("#getFilteredTMXList").removeAttr("disabled");
                    $j("#getFilteredTMXList").removeClass("disabledBtn");
                });
                $t.getProductGroupList({
                    success: function (data) {
                        displayProductGroupList(data, ".exprtTMXCatList");
                    }
                });
            }
            if ($j(this).val() == 5) {
                $j("#getFilteredTMXList").attr("disabled", "disabled");
                $j("#getFilteredTMXList").addClass("disabledBtn");

                $j("#category_list_TMX").show();
                $j(".exprtTMXCatList").change(function () {
                    $j("#getFilteredTMXList").removeAttr("disabled");
                    $j("#getFilteredTMXList").removeClass("disabledBtn");
                });
                $t.getContentTypeList({
                    success: function (data) {
                        displayContentList(data, ".exprtTMXCatList");
                    }
                });
            }

        });


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

        $j(".removeUser").click(function () {

            $j(".usersList :selected").remove();
            if ($j('.usersList').html() == "") {
                $j('.usersList').addClass("error");
                $j("#noUsersReq").show();
                $j("UsersReqError").hide();
                flag = false;
            } else {
                $j("#noUsersReq").hide();
                $j("UsersReqError").show();
                $j('.usersList').removeClass("error");
            }
        });
        $t.getLanguageList({
            success: function (data) {
                displayLanguageList(data, "#termlanguageSlct");
                displayLanguageList(data, "#tmLanguageSlct");

                $j("#termlanguageSlct").multiselect().multiselectfilter();
                $j("#termlanguageSlct").multiselect({
                    noneSelectedText: 'Select language',
                    selectedList: 4, // 0-based index
                    onClose: function () {
                        $j("#showSelectedTerms").attr('checked', false);

                        values = $j("#termlanguageSlct").val();
                        console.log(values);
                        langValues = values
                        if (values == null) {
                            values = [];
                            langSelected = "No Languages";
                        }else {
                        	 langSelected = "selected";
                        }
                        langSelectedVal = removeEmptyString(langSelectedVal);
                        var isLangModified = isMultiSelectModified(values, langSelectedVal);
                        if (values == "") {
                            values = null;
                        }
                        searchLangValues = values;
                        if (isLangModified) {/*
                            langSelectedVal = values;
                            if (values != null && values.length > 0 && multiCTermsValues != null) {
                                $j("#dataPie_1005").empty();
                                triggerTermDetails(null, null, 0, null, false, 'Locale', values, 'Company', multiCTermsValues, null);
                            } else if (values == null && multiCTermsValues != null) {
                                triggerTermDetails(null, null, 0, null, false, null, null, 'Company', multiCTermsValues, null);
                            } else if (values != null && multiCTermsValues == null) {
                                triggerTermDetails(null, null, 0, null, false, 'Locale', values, null, null, null);
                            } else {
                                triggerTermDetails(null, null, 0, null, false, null, null, null, null, null);
                            }
                        */}
                    },
                    classes: "lang"
                });

                $j("#tmLanguageSlct").multiselect().multiselectfilter();
                $j("#tmLanguageSlct").multiselect({
                    noneSelectedText: 'Select language',
                    selectedList: 4, // 0-based index
                    onClose: function () {
                        values = $j("#tmLanguageSlct").val();
                        console.log(values);
                        if (values == null) {
                            values = [];
                            langSelected = "No Languages";
                        } else {
                        	 langSelected = "selected";
                        }
                        langSelectedVal = removeEmptyString(langSelectedVal);
                        var isLangModified = isMultiSelectModified(values, langSelectedVal);
                        if (values == "") {
                            values = null;
                        }
                        if (isLangModified) {
                            langSelectedVal = values;
                            if (values != null && values.length > 0 && multiCTmsValues != null) {
                                $j("#dataPie_1005").empty();
                                triggerTmDetails(null, null, 0, null, false, 'Locale', values, 'Company', multiCTmsValues);
                            } else if (values == null && multiCTmsValues != null) {
                                triggerTmDetails(null, null, 0, null, false, null, null, 'Company', multiCTmsValues);
                            } else if (values != null && multiCTmsValues == null) {
                                triggerTmDetails(null, null, 0, null, false, 'Locale', values, null, null);
                            }
                            else {
                                triggerTmDetails(null, null, 0, null, false, null, null, null, null);
                            }
                        }
                    },
                    classes: "lang"
                });
            }
        });

        $t.getCompanyList({
            success: function (data) {
                displayCompanyList(data, "#companySlct");
                displayCompanyList(data, "#mutliCompanySlct");
                displayCompanyList(data, "#companyTermsSlct");
                displayCompanyList(data, "#companyTmsSlct");

                $j("#companySlct").multiselect().multiselectfilter();

                $j("#companySlct").multiselect({
                    noneSelectedText: 'Select company',
                    selectedList: 4,// 0-based index
                    onClose: function () {
                        $j("#showSelectedTerms").attr('checked', false);
                        cvalues = $j("#companySlct").val();
                        if (cvalues != 0) {
                            $j(".languageSlctErr").hide();
                            $j("#addNewTerm .ui-state-default").css("border", "1px solid #dddddd");
                        }
                    }
//			    	   classes:"lang"
                });
                $j("#mutliCompanySlct").multiselect().multiselectfilter();

                $j("#mutliCompanySlct").multiselect({
                    noneSelectedText: 'Select company',
                    selectedList: 4, // 0-based index
                    onClose: function () {
                        MultiCValues = $j("#mutliCompanySlct").val();
                    }
//			    	   classes:"lang"
                });

                $j("#companyTermsSlct").multiselect().multiselectfilter();

                $j("#companyTermsSlct").multiselect({
                    noneSelectedText: 'Select company',
                    selectedList: 4, // 0-based index
                    onClose: function () {
                        $j("#showSelectedTerms").attr('checked', false);
                        multiCTermsValues = $j("#companyTermsSlct").val();
                        searchCompanyValues = multiCTermsValues;
                        companyValues = multiCTermsValues;
                        if (multiCTermsValues == null) {
                            multiCTermsValues = [];
                        }
                        compSelectedVal = removeEmptyString(compSelectedVal);
                        var isModified = isMultiSelectModified(multiCTermsValues, compSelectedVal);
                        if (multiCTermsValues == "") {
                            multiCTermsValues = null;
                        }
                        if (isModified) {/*
                            compSelectedVal = multiCTermsValues;
                            if (multiCTermsValues != null && multiCTermsValues.length > 0 && values != null) {
                                $j("#dataPie_1005").empty();
                                triggerTermDetails(null, null, 0, null, false, 'Locale', values, 'Company', multiCTermsValues, null);
                            } else if (multiCTermsValues == null && values != null) {
                                triggerTermDetails(null, null, 0, null, false, 'Locale', values, null, null, null);
                            } else if (multiCTermsValues != null && values == null) {
                                triggerTermDetails(null, null, 0, null, false, null, null, 'Company', multiCTermsValues, null);
                            } else {
                                triggerTermDetails(null, null, 0, null, false, null, null, null, null, null);
                            }
                        */}
                    }
//			    	   classes:"lang"
                });

                $j("#companyTmsSlct").multiselect().multiselectfilter();
                $j("#companyTmsSlct").multiselect({
                    noneSelectedText: 'Select company',
                    selectedList: 4, // 0-based index
                    onClose: function () {
                        multiCTmsValues = $j("#companyTmsSlct").val();
                        if (multiCTmsValues == null) {
                            multiCTmsValues = [];
                        }

                        compSelectedVal = removeEmptyString(compSelectedVal);
                        var isModified = isMultiSelectModified(multiCTmsValues, compSelectedVal);
                        if (multiCTmsValues == "") {
                            multiCTmsValues = null;
                        }
                        if (isModified) {
                            compSelectedVal = multiCTmsValues;

                            if (multiCTmsValues != null && multiCTmsValues.length > 0 && values != null) {
                                $j("#dataPie_1005").empty();
                                triggerTmDetails(null, null, 0, null, false, 'Locale', values, 'Company', multiCTmsValues);
                            } else if (multiCTmsValues == null && values != null) {
                                triggerTmDetails(null, null, 0, null, false, 'Locale', values, null, null);
                            } else if (multiCTmsValues != null && values == null) {
                                triggerTmDetails(null, null, 0, null, false, null, null, 'Company', multiCTmsValues);

                            } else {
                                triggerTmDetails(null, null, 0, null, false, null, null, null, null);
                            }
                        }
                    }
//			    	   classes:"lang"
                });
            }
        });
        $j('#termlanguageSlct').change(function () {
            $j("#termDtlRowsList input:checked").each(function (i) {
                if (selectedTermIds == "") {
                    separator = "";
                } else {
                    separator = ",";
                }
                selectedTermIds += separator + $j(this).parent().next().attr("termId");
            });

            updateSelectedTerms();
        });

        $j('#companyTermsSlct').change(function () {
            $j("#termDtlRowsList input:checked").each(function (i) {

                if (selectedTermIds == "") {
                    separator = "";
                } else {
                    separator = ",";
                }
                selectedTermIds += separator + $j(this).parent().next().attr("termId");

            });

            updateSelectedTerms();
        });
        $j("#cancelInvitMail").click(function () {
            validateTermVote.resetForm();
            $j("#noUsersReq").hide();
            $j('.usersList').removeClass("error");
            $j('#categorySlct').removeClass("error");
            $j('.categoryList').removeClass("error");
            $j('#termTemplate').removeClass("error");
            $j('#votingPeriodNum').removeClass("error");
            $j('#invitation').dialog('destroy');
            $j(".categoryList").html("");
            $j(".usersList").html("");
            $j(".categorySlct").val("");
            $j("#selectReq").hide();
            $j(".inviteEmailTemplate").val("");
            $j('.inviteEmailTemplate').children().remove().end().append('<option value="">--Select template--</option>');
            $j(".emailPrvw").html("");
            $j(".mailTmpl").val(0);
            $j(".sampleVoteMail").hide();
            $j(".sampleWelcomeMail").hide();
            selectedValues = "";
//			selectedTermIds = "";
        });

        $j(".export").click(function () {
            if (($j(".exprtCatList").val() == null) && ($j(".exprtCatSlct :selected").val() != "4")) {
                alertMessage("#termExpChkMsg");
            } else {
                var exportFormat = $j(".exportFormat :selected").text();
                var selectedIds = $j(".exprtCatList").val();
                var exportBy = $j(".exprtCatSlct :selected").text();
                startLoading("waiting", " &nbsp; Downloading ... please wait");
                downloadStatus();
                $j('#downloadForm').attr('action', $j("#contextRootPath").val() + "/impExp_serv?c=exportCSV&exportType=termInfo&selectedIds=" + selectedIds + "&exportBy=" + exportBy + "&exportFormat=" + exportFormat);
                $j('#downloadForm').submit();
                //location.href= $j("#contextRootPath").val()+"/impExp_serv?c=exportCSV&exportType=termInfo&selectedIds="+selectedIds+"&exportBy="+exportBy+"&exportFormat="+exportFormat;
            }
        });


        $j("#exportTMX").click(function () {
            if (($j(".exprtTMXCatList").val() == null)) {
                alertMessage("#termExpChkMsg");
            } else {
                var selectedIds = $j(".exprtTMXCatList").val();

                var exportByTMX = $j(".exprtCatTMXSlct :selected").text();
                startLoading("waitingTMX", "Downloading ... please wait");

                var exportBy = $j(".exprtCatTMXSlct :selected").text();
                startLoading("waiting", " &nbsp; Downloading ... please wait");
                var fileName = "ExportTMX";
                var ext = "tmx";
                var type = "Export";
                $t.saveImportTMXFile(fileName, ext, type, {
                    success: function (data) {
                        $j("#fileIdExport").val(data.fileUploadStatusId);
                        $j('#downloadTMXForm').attr('action', $j("#contextRootPath").val() + "/impExp_serv?c=exportTMX&exportType=termInfo&selectedIds=" + selectedIds + "&exportBy=" + exportByTMX + "&fileIdExport=" + data.fileUploadStatusId);
                        downloadStatusTMX();
                        $j('#downloadTMXForm').submit();
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });
                /**downloadStatus();
                 $j('#downloadTMXForm').attr('action',$j("#contextRootPath").val()+"/impExp_serv?c=exportTMX&exportType=termInfo&selectedIds="+selectedIds+"&exportBy="+exportByTMX);
                 $j('#downloadTMXForm').submit(); **/
            }
        });


        $j("#tabs").tabs({

        	
            select: function (event, ui) {
            	$t.getTermsInGlossary({
                    success: function (data) {
                        var d = new Date();
                        var termsPerYear = data;
                        var dataXML = new Array();
                        if ($j("#hpsite").val() == "true")
                            dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' baseFont='HP Simplified' lineColor='#0e94bc'>");
                        else
                            dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
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
                             dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' baseFont='HP Simplified' lineColor='#0e94bc'>");
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
                $j("#termlanguageSlct").multiselect("uncheckAll");
                $j(".TmLangSlctDropdwn").multiselect("uncheckAll");
                $j("#companyTmsSlct").multiselect("uncheckAll");
                $j("#mutliCompanySlct").multiselect("uncheckAll");

                validateTerm.resetForm();
                var $tabs = $j('#tabs').tabs();
                selected = ui.index;
                if (selected != '0') {
                    $j('#showSelectedTerms').attr('checked', false);

                    $j("#termFormData").val(0);
                    $j("#termImage").show();
                    $j('#termsModule').hide();
                    $j("#tmModule").hide();
                    $j("#chkCase").attr("checked", false);
                    $j(".exprtCatTMXSlct").val(0);
                    $j("#getFilteredTMXList").attr("disabled", "disabled");
                    $j("#getFilteredTMXList").addClass("disabledBtn");
                    $j("#category_list_TMX").hide();
                    $j(".exprtTMXCatList").val(1);
                    $j("#newTerm").val("Enter term to search...");
                    $j(".TmLangSlctDropdwn").hide();
                    $j(".companyTmsSlctDropdwn").hide();
                    $j(".searchTm").hide();
                    $j(".invitePpl").show();
                    $j("#action").hide();
                    $j("#termAction").hide();
                } else {
                    $j("#action").show();
                    $j("#termAction").show();
                    var searchBy = $j("#newTerm").val();
                    $j("#termImage").hide();
                    $j(".invitePpl").show();
                    $j(".searchTm").hide();
                    $j("#uploadedImage").attr('src', $j("#contextRootPath").val() + "/images/default.jpg");
                    $j('#termsModule').show();
                    $j("#tmModule").hide();
                    $j(".termLangSlctDropdwn").show();
                    if ($j("#isSuperAdmin").val() == "true") {
                        $j(".companyTermsSlctDropdwn").show();
                    } else {
                        $j(".companyTermsSlctDropdwn").hide();
                    }
                    if (searchBy == "Enter term to search...") {
                        triggerTermDetails(null, null, 0, null, false, null, null, null, null, null);
                    }

                    $j(".exprtCatTMXSlct").val(0);
                    $j("#getFilteredTMXList").attr("disabled", "disabled");
                    $j("#getFilteredTMXList").addClass("disabledBtn");
                    $j("#category_list_TMX").hide();
                    $j(".exprtTMXCatList").val(1);
                    $j(".TmLangSlctDropdwn").hide();
                    $j(".companyTmsSlctDropdwn").hide();
                    //$j("#companyTermsSlct").multiselect("uncheckAll");
                    $j(".companySlctErr").hide();
                    $j("#addNewTerm .companySlctdropdown .ui-state-default").css("border", "1px solid #BBBBBB");
                }

                if (selected == 2) {
                    $j('#showSelectedTerms').attr('checked', false);

                    $j("#uploadedImage").attr('src', $j("#contextRootPath").val() + "/images/default.jpg");
                    $j("#termImage").hide();
                    $j(".invitePpl").show();
                    $j(".searchTm").hide();
                    $j("#action").hide();
                    $j("#termAction").hide();
                    $j(".exprtCatSlct").val(0);
                    $j(".termLangSlctDropdwn").hide();
                    $j(".companyTermsSlctDropdwn").hide();
                    $j("#companyTmsSlct").multiselect("uncheckAll");
                    $j("#category_list").hide();
                    $j("#tmModule").hide();
                    if ($j("#isSuperAdmin").val() == "true") {
                        $j(".exprtCatSlct").html('<option value="0">--Select--</option><option value="1">Locale</option><option  value="2">Part of speech</option><option value="3">Category</option><option value="4">Final</option><option value="4">Poll expiration</option><option value="5">Company</option><option value="6">Domain</option>');
                    } else {
                        $j(".exprtCatSlct").html('<option value="0">--Select--</option><option value="1">Locale</option><option  value="2">Part of speech</option><option value="3">Category</option><option value="4">Final</option><option value="4">Poll expiration</option><option value="6">Domain</option>');
                    }
                    $j("#getFilteredList").attr("disabled", "disabled");
                    $j("#getFilteredList").addClass("disabledBtn");
                    $j(".exprtCatTMXSlct").val(0);
                    $j("#getFilteredTMXList").attr("disabled", "disabled");
                    $j("#getFilteredTMXList").addClass("disabledBtn");
                    $j("#category_list_TMX").hide();
                    $j(".exprtTMXCatList").val(1);
                    $j(".TmLangSlctDropdwn").hide();
                    $j(".companyTmsSlctDropdwn").hide();
                    $j("#mutliCompanySlct").multiselect("uncheckAll");
                    $j("#companyTermsSlct").multiselect("uncheckAll");
                    $j(".companySlctErr").hide();
                    $j("#addNewTerm .companySlctdropdown .ui-state-default").css("border", "1px solid #BBBBBB");
                }
                if (selected == 3) {
                    $j(".searchTm").show();
                    //$j("#uploadTMXFormId").show();
                    $j("#inputSize").html("<span>Import TMX file.  (Max</span>&nbsp;" + $j("#inputFileMaxSize").val() + '<span> MB)');
                    $j("#uploadedImage").attr('src', $j("#contextRootPath").val() + "/images/default.jpg");
                    $j("#termImage").hide();
                    $j(".invitePpl").hide();
                    $j("#tmModule").hide();
                    $j(".exprtCatSlct").val(0);
                    $j("#action").hide();
                    $j(".termLangSlctDropdwn").hide();
                    $j(".companyTermsSlctDropdwn").hide();
                    $j("#companyTmsSlct").multiselect("uncheckAll");
                    $j("#newTmTerm").val("Enter term to search...");
                    $j("#category_list").hide();
                    $j("#getFilteredList").attr("disabled", "disabled");
                    $j("#getFilteredList").addClass("disabledBtn");
                    $j(".exprtCatTMXSlct").val(0);
                    $j("#getFilteredTMXList").attr("disabled", "disabled");
                    $j("#getFilteredTMXList").addClass("disabledBtn");
                    $j("#category_list_TMX").hide();
                    $j(".exprtTMXCatList").val(1);
                    $j("#termAction").hide();
                    $j(".TmLangSlctDropdwn").hide();
                    $j("#mutliCompanySlct").multiselect("uncheckAll");
                    $j("#companyTermsSlct").multiselect("uncheckAll");
                    $j(".companySlctErr").hide();
                    $j("#addNewTerm .companySlctdropdown .ui-state-default").css("border", "1px solid #BBBBBB");
                    $t.getImportExportData(0, null, null, 0, {
                        success: function (data) {
                            if (data.length > 0) {
                                $j("#impUrl").show();
                            }
                            else {
                                $j("#impUrl").hide();
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
                if (selected == 4) {
                    $j("#uploadedImage").attr('src', $j("#contextRootPath").val() + "/images/default.jpg");
                    $j("#termImage").hide();
                    $j(".searchTm").show();
                    $j("#tmModule").hide();
                    $j("#action").hide();
                    $j(".invitePpl").hide();
                    $j(".exprtCatSlct").val(0);
                    $j(".termLangSlctDropdwn").hide();
                    $j(".companyTermsSlctDropdwn").hide();
                    $j(".TmLangSlctDropdwn").hide();
                    $j("#category_list").hide();
                    $j("#newTmTerm").val("Enter term to search...");
                    $j("#termAction").hide();
                    if ($j("#isSuperAdmin").val() == "true") {
                        $j(".exprtCatTMXSlct").html('<option value="0">--Select--</option><option value="1">Locale</option><option value="2">Company</option><option value="3">Industry Domain</option><option value="4">Product Line</option><option value="5">Content Type</option>');
                    } else {
                        $j(".exprtCatTMXSlct").html('<option value="0">--Select--</option><option value="1">Locale</option><option value="3">Industry Domain</option><option value="4">Product Line</option><option value="5">Content Type</option>');
                    }
                    $j("#getFilteredList").attr("disabled", "disabled");
                    $j("#getFilteredList").addClass("disabledBtn");
                    $j("#mutliCompanySlct").multiselect("uncheckAll");
                    $j("#companyTmsSlct").multiselect("uncheckAll");
                    $j("#companyTermsSlct").multiselect("uncheckAll");
                    $j(".companySlctErr").hide();
                    $j("#addNewTerm .companySlctdropdown .ui-state-default").css("border", "1px solid #BBBBBB");
                    $t.getImportExportData(0, null, null, 0, {
                        success: function (data) {
                            if (data.length > 0) {
                                $j("#expUrl").show();
                            }
                            else {
                                $j("#expUrl").hide();
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
                if (selected == 5) {
                    var searchBy = $j("#newTmTerm").val();
                    $j("#termImage").hide();
                    $j("#uploadedImage").attr('src', $j("#contextRootPath").val() + "/images/default.jpg");
                    $j('#termsModule').hide();
                    $j("#tmModule").show();
                    $j(".invitePpl").hide();
                    $j(".searchTm").show();
                    $j("#termAction").hide();
                    $j(".termLangSlctDropdwn").hide();
                    $j(".companyTermsSlctDropdwn").hide();
                    $j(".TmLangSlctDropdwn").show();
                    if ($j("#isSuperAdmin").val() == "true") {
                        $j(".companyTmsSlctDropdwn").show();
                    } else {
                        $j(".companyTmsSlctDropdwn").hide();
                    }
                    $j(".companySlctErr").hide();
                    $j("#addNewTerm .companySlctdropdown .ui-state-default").css("border", "1px solid #BBBBBB");
                    $j("#action").hide();
                    if (searchBy == "Enter term to search...") {
                        triggerTmDetails(null, null, 0, null, false, null, null, null, null);
                    }
                    $j(".exprtCatTMXSlct").val(0);
                    $j("#getFilteredTMXList").attr("disabled", "disabled");
                    $j("#getFilteredTMXList").addClass("disabledBtn");
                    $j("#category_list_TMX").hide();
                    $j(".exprtTMXCatList").val(1);
                    $j("#mutliCompanySlct").multiselect("uncheckAll");
                    $j("#companyTermsSlct").multiselect("uncheckAll");
                }

                $j("#message").html("");
            }
        });

        // Parts of Speech Picklist
        $t.getPOSList({
            success: function (data) {
                displayPosList(data, '#termPOS');
                displayPosList(data, '#pos');
                displayPosList(data,'#targetPOS');
                displayPosList(data,'#targetPosID');
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

        $t.getDomainList({
            success: function (data) {
                displayDomainList(data, '#termDomain');

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
                displayTermFormList(data, '.termFormData');
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
                displayProductGroupList(data, '.productGroup');
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
                displayLanguageList(data, '.language');
                $j("#trgtLang").change(function () {

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
                displayTermCatList(data, '.termCategory');
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        //To render the data in charts for Tms
        $t.getGlossaryTmTerms({
            success: function (data) {
                var tmsPerYear = data;
                var dataXML = new Array();
                dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showAlternateHGridColor='0' canvasBorderThickness='0' baseFont='HP Simplified' lineThickness='2' lineColor='#0e94bc'>");
                if (tmsPerYear != null) {
                    for (var count = 0; count < tmsPerYear.length; count++) {
                        dataXML.push("<set label='" + tmsPerYear[count].interval + "' " + "value='" + tmsPerYear[count].termsPerInterval + "'/>");
                    }
                }
                dataXML.push("</chart>");
                ChartRender.twoDLineChart("myChartIdTm", "240", "150", "chartContainerTm", dataXML);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
        // Concept Category Picklist
        $t.getConceptCategoryList({
            success: function (data) {
                displayConceptCatList(data, '.conceptCategory');
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });

        $j(".changePic").click(function () {
            showBrowsePic();
        });

        validateTermVote = $j("#termVoteForm").validate({
            debug: true,
            rules: {
                categorySlct: {
                    required: {
                        depends: function (element) {
                            return $j("#categorySlct").val() == '';
                        }
                    }
                },

                votingPeriodNum: {
                    required: true,
                    votingPeriod: true
                },
                termTemplate: {
                    required: {
                        depends: function (element) {
                            return $j("#termTemplate").val() == '';
                        }
                    }
                }
            },

            messages: {
                categorySlct: "Select category to invite users",
                votingPeriodNum: "Voting period is required",
                termTemplate: "Select email template "
            }
        });
        $j.validator.addMethod('votingPeriod', function (value, element) {
                return this.optional(element) || (value.match(/^[0-9]+$/));
            },
            'Voting period must be numeric value');

        validateTerm = $j("#addNewTerm").validate({
            debug: true,
            rules: {
                termInfo: "required",
                trgtLang: {
                    required: {
                        depends: function (element) {
                            return $j("#trgtLang").val() == '';
                        }
                    }
                }
            },
            messages: {
                termInfo: "<br/>English term is required",
                trgtLang: "Select at least one target language"
            }
        });
    });
})(jQuery);