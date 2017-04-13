(function(window, $j) {
	var validateTermDetails;
	var values = 0;
	var texts = [];
	var oTable = null;
	LeaderBoard.showLeaderBoard();

	var url = $j(location).attr('href');
	if (url.indexOf("/dashboard.jsp") != -1) {
		$j('#about').removeClass('aboutForDashboard');
		$j('#about').addClass('aboutForDashboardMargin');

	}
	/* if(url.indexOf("?eraseCache=true") == -1){
		 window.location = window.location.href+'?eraseCache=true';
	 } else 
		 return;*/
	if (url.indexOf("hp/index.jsp") != -1) {
		$j('#about').css('margin-left', '-180px');

	}

	if ($('.header').find('.headerMenuAdmin').length >= 1) {
		$j('#about').css('margin-left', '60px');
	}/*else{
				$j('#about').css('margin-left','-180px');
			}*/

	/**
	 var showOrHide="show";
	 var popup_sh = function() {
			 
			 if (showOrHide=="show") {  
				 $('.PopupDiv').show();
				 var postion=$('.signUpButton').offset();
				
				 if($.browser.version=="6.0") 
				{
		$('.PopupDiv').css({'top':postion.top+44,'left':postion.left+83});}
		if($j.browser.version=="7.0") 
				{
			$('.PopupDiv').css({'top':postion.top+33,'left':postion.left-94});
			$('.PopupDiv').css('height','220px');
			 $(".registerLnk").css('margin-top','5px');
				
		}
		else
		$('.PopupDiv').css({'top':postion.top+33,'left':postion.left-102});
				$('img#logon').attr('src',$j("#contextRootPath").val()+'/images/up.gif');
				 
				 showOrHide = "hide";
				 }
			else if(showOrHide=="hide")  { 
				 $('.PopupDiv').hide();
				 $('img#logon').attr('src',$j("#contextRootPath").val()+'/images/down.gif');
				 showOrHide = "show";
				 }
		 };

	 $(".showhide").click(function(){
			 $j("#userError").hide();
			 $("#username").val("");
			 $("#password").val("");
			 $j("#username").removeClass("error");
			 $j("#pwdError").hide();
			 $j("#password").removeClass("error");
			 $j(".invalidDtls").hide();
			 
			 popup_sh();
		 });

	 /**
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
	var leaderBoardTmpl = [
			'<div class="leaderBrdItem"><div class="itemImg" style="width:64px; height: 68px;"><img src="',
			'',
			'" height="68px;" width="64px"/></div><div class="itemDesc"><div class="smallFont leaderBrdSmallFont bold" style="margin-top: -4px;">',
			'',
			'</div> <div class="badge" style=" margin-bottom: 6px;" ><img style="height: 39px;width:203px;" src="',
			'',
			'" /></div><div class="" style="color: #a9a9a9; float:left; font-size: 10px; margin-right: 10px;">',
			'', ' votes</div> <div class="floatleft starsDiv" ><img src="', '',
			'"  /></div></div></div>' ];

	var slctOptionTmpl = [ '<option value="', '', '" >', '', '</option>' ];
	
	var showTMUnitsRolePrivileges = function() {
		// To get UserPrevileges
		$t.getUserRolePrevileges({
			success : function(data) {
				if (data != null) {
					for (var i = 0; i < data.length; i++) {
						if (data[i].privileges.jsId != null) {
							if (data[i].privileges.jsId != "viewTMs") {
								$j('#tmUnitsTotal').css('display', 'none');
							} else {
								$j('#tmUnitsTotal').css('display', 'block');
								break;
							}
						}
					}
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				console.log(xhr.responseText);
				if (Boolean(xhr.responseText.message)) {
					console.log(xhr.responseText.message);
				}
			}
		});
	};
	showTMUnitsRolePrivileges();
	var displayPosList = function(data) {
		var posList = data;
		for (var count = 0; count < posList.length; count++) {
			var partOfSpeechSlctTmplClone = slctOptionTmpl;
			partOfSpeechSlctTmplClone[1] = posList[count].partsOfSpeechId;
			partOfSpeechSlctTmplClone[3] = posList[count].partOfSpeech;
			$j('.termPOS').append(partOfSpeechSlctTmplClone.join(""));
		}
	};

	var displayTermFormList = function(data) {
		var trmFormList = data;
		for (var count = 0; count < trmFormList.length; count++) {
			var termFormSlctTmplClone = slctOptionTmpl;
			termFormSlctTmplClone[1] = trmFormList[count].formId;
			termFormSlctTmplClone[3] = trmFormList[count].formName;
			$j('.termForm').append(termFormSlctTmplClone.join(""));
		}
	};

	var displayProgramList = function(data) {
		var programList = data;
		for (var count = 0; count < programList.length; count++) {
			var programSlctTmplClone = slctOptionTmpl;
			programSlctTmplClone[1] = programList[count].programId;
			programSlctTmplClone[3] = programList[count].programName;
			$j('.program').append(programSlctTmplClone.join(""));
		}
	};

	var displayProductGroupList = function(data) {
		var productList = data;
		for (var count = 0; count < productList.length; count++) {
			var productGroupSlctTmplClone = slctOptionTmpl;
			productGroupSlctTmplClone[1] = productList[count].productId;
			productGroupSlctTmplClone[3] = productList[count].product;
			$j('.productGroup').append(productGroupSlctTmplClone.join(""));
		}
	};

	var displayStatusList = function(data) {
		var statusList = data;
		for (var count = 0; count < statusList.length; count++) {
			var statusSlctTmplClone = slctOptionTmpl;
			statusSlctTmplClone[1] = statusList[count].statusId;
			statusSlctTmplClone[3] = statusList[count].status;
			$j('.status').append(statusSlctTmplClone.join(""));
		}
	};

	var displayTermCatList = function(data) {
		var termCatList = data;
		for (var count = 0; count < termCatList.length; count++) {
			var termCatSlctTmplClone = slctOptionTmpl;
			termCatSlctTmplClone[1] = termCatList[count].categoryId;
			termCatSlctTmplClone[3] = termCatList[count].category;
			$j('.termCategory').append(termCatSlctTmplClone.join(""));
		}
	};

	var displayConceptCatList = function(data) {
		var conceptCatList = data;
		for (var count = 0; count < conceptCatList.length; count++) {
			var conceptCatSlctTmplClone = slctOptionTmpl;
			conceptCatSlctTmplClone[1] = conceptCatList[count].conceptCatId;
			conceptCatSlctTmplClone[3] = conceptCatList[count].conceptCategory;
			$j('.conceptCategory').append(conceptCatSlctTmplClone.join(""));
		}
	};

	var displayLanguages = function(data, divClass) {
		var languageList = data;

		/*var AllLang =  {
				'languageId' : 'All',
				'languageLabel' : 'All',
				'languageCode'   : 'All'
				
		};
		languageList.unshift(AllLang);*/
		var langLength = languageList.length;

		for (var count = 0; count < langLength; count++) {
			var langSlctTmplClone = slctOptionTmpl;
			langSlctTmplClone[1] = languageList[count].languageId;
			langSlctTmplClone[3] = languageList[count].languageLabel;
			$j(divClass).append(langSlctTmplClone.join(""));
		}
	};

	var showNewTermForm = function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		$("#newTermDiv:ui-dialog").dialog("destroy");
		$j(".ui-dialog-content").dialog("close");
		console.log($("#newTermDiv").html());
		$("#newTermDiv").dialog({
			height : 400,
			width : 750,
			modal : true,
			close : function(event, ui) {
				//				$j('#trgtLang').css("border","1px solid  #BBBBBB");
				validateTermDetails.resetForm();
			}

		});
	};

	var showLoadingDialog = function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		$j("#loading:ui-dialog").dialog("destroy");

		$j("#loading").dialog({
			height : 120,
			width : 200,
			modal : true,
			resizable : false
		});
	};

	var closeLoadingDialog = function(dialogId, id) {
		$j(dialogId).html("");
		$j(dialogId).dialog('destroy');
		$j(id).dialog('destroy');
		mailSent();
	};

	var mailSent = function() {
		$j("#mail_success:ui-dialog").dialog("destroy");

		$j("#mail_success").dialog({
			modal : true,
			buttons : {
				OK : function() {
					$j(this).dialog("close");
				}
			}
		});
	}

	$j('#monthly')
			.click(
					function() {
						$t
								.getMonthlyTermsInGlossary({
									success : function(data) {
										var termsPerMonth = data;
										var dataXML = new Array();
										if ($j("#hpsite").val() == "true")
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' divLineColor='#d3d3d3' showAlternateHGridColor='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
										else
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
										if (termsPerMonth != null) {
											for (var count = 0; count < termsPerMonth.length; count++) {
												dataXML
														.push("<set label='"
																+ termsPerMonth[count].interval
																+ "' "
																+ "value='"
																+ termsPerMonth[count].termsPerInterval
																+ "'/>");
											}
										}
										dataXML.push("</chart>");
										var chartReference = FusionCharts("myChartId");
										chartReference.setXMLData(dataXML);
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});

						$t
								.getMonthlyTermsDebated({
									success : function(data) {
										var termsDebatedPerMonth = data;
										var dataXML = new Array();
										if ($j("#hpsite").val() == "true")
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' divLineColor='#d3d3d3' showAlternateHGridColor='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' canvasBorderThickness='0' baseFont='HP Simplified' lineThickness='2' lineColor='#0e94bc'>");
										else
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
										if (termsDebatedPerMonth != null) {
											for (var count = 0; count < termsDebatedPerMonth.length; count++) {
												dataXML
														.push("<set label='"
																+ termsDebatedPerMonth[count].interval
																+ "' "
																+ "value='"
																+ termsDebatedPerMonth[count].termsPerInterval
																+ "'/>");
											}
										}
										dataXML.push("</chart>");
										var chartReference = FusionCharts("myChartId1");
										chartReference.setXMLData(dataXML);
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});

						$t
								.getMonthlyTmDetails({
									success : function(data) {
										var tmsPerMonth = data;
										var dataXML = new Array();
										if ($j("#hpsite").val() == "true")
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' divLineColor='#d3d3d3' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' canvasBorderThickness='0' baseFont='HP Simplified' lineThickness='2' lineColor='#0e94bc'>");
										else
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
										if (tmsPerMonth != null) {
											for (var count = 0; count < tmsPerMonth.length; count++) {
												dataXML
														.push("<set label='"
																+ tmsPerMonth[count].interval
																+ "' "
																+ "value='"
																+ tmsPerMonth[count].termsPerInterval
																+ "'/>");
											}
										}
										dataXML.push("</chart>");
										var chartReference = FusionCharts("myChartIdTm");
										chartReference.setXMLData(dataXML);
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});

					});

	$j('#username').bind('keydown', function(e) {
		$j("#userError").hide();
		$j("#username").removeClass("error");
	});

	$j('#password').bind('keydown', function(e) {
		$j("#pwdError").hide();
		$j("#password").removeClass("error");
		if (e.which == 13) {
			$j("#loginForm").submit();
		}
	});

	$j('#quarterly')
			.click(
					function() {

						$t
								.getQrtrlyTermsInGlossary({
									success : function(data) {
										var termsPerQuarter = data;
										var dataXML = new Array();
										if ($j("#hpsite").val() == "true")
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' divLineColor='#d3d3d3' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' canvasBorderThickness='0' baseFont='HP Simplified' lineThickness='2' lineColor='#0e94bc'>");
										else
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
										if (termsPerQuarter != null) {
											for (var count = 0; count < termsPerQuarter.length; count++) {
												dataXML
														.push("<set label='"
																+ termsPerQuarter[count].interval
																+ "' "
																+ "value='"
																+ termsPerQuarter[count].termsPerInterval
																+ "'/>");
											}
										}
										dataXML.push("</chart>");
										var chartReference = FusionCharts("myChartId");
										chartReference.setXMLData(dataXML);
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});

						$t
								.getQrtrlyTermsDebated({
									success : function(data) {
										var termsDebatedPerQrtr = data;
										var dataXML = new Array();
										if ($j("#hpsite").val() == "true")
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' divLineColor='#d3d3d3' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
										else
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
										if (termsDebatedPerQrtr != null) {
											for (var count = 0; count < termsDebatedPerQrtr.length; count++) {
												dataXML
														.push("<set label='"
																+ termsDebatedPerQrtr[count].interval
																+ "' "
																+ "value='"
																+ termsDebatedPerQrtr[count].termsPerInterval
																+ "'/>");
											}
										}
										dataXML.push("</chart>");
										var chartReference = FusionCharts("myChartId1");
										chartReference.setXMLData(dataXML);
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});

						$t
								.getQuarterlyTmDetails({
									success : function(data) {
										var tmsPerQuarter = data;
										var dataXML = new Array();
										if ($j("#hpsite").val() == "true")
											//dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0'canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0'  plotbordercolor='0e94bc' plotborderthickness='3' divLineColor='#d3d3d3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0'   showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
										else
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
										if (tmsPerQuarter != null) {
											for (var count = 0; count < tmsPerQuarter.length; count++) {
												dataXML
														.push("<set label='"
																+ tmsPerQuarter[count].interval
																+ "' "
																+ "value='"
																+ tmsPerQuarter[count].termsPerInterval
																+ "'/>");
											}
										}
										dataXML.push("</chart>");
										var chartReference = FusionCharts("myChartIdTm");
										chartReference.setXMLData(dataXML);
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});
					});

	$j(".termRqstBtn").click(function() {
		/*if($j("#hpsite").val() == "true"){
		  $j('#newTermDiv').load('../hp/req_new_term.jsp');
		} else {
		  $j('#newTermDiv').load('../app/req_new_term.jsp');
		}*/
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
		/**
		 $j("#languageSlct").multiselect("uncheckAll");
		 $j("#rqstNewTerm .ui-state-default").css("border","1px solid #dddddd");
		 $j(".languageSlctErr").hide();
		 texts=[];
		 values=0;
		 */
	});

	$j("#cancelMail").click(function() {
		$j('#newTermDiv').dialog('destroy');
		$j(".ui-dialog-content").dialog("close");

	});
	validateTermDetails = $j("#rqstNewTrm").validate({
		debug : true,
		rules : {
			termInfo : "required",
			languageSlct : {
				required : {
					depends : function(element) {
						return (values == null || values == 0)
					}
				}
			}
		},
		messages : {
			termInfo : "English term is required",
			languageSlct : "Select at least one target language"
		}
	});

	$j("#newTermMail")
			.click(
					function() {
						if ($j("#rqstNewTrm").valid() && values != null
								&& values != 0) {
							$j('#loading')
									.append(
											'<div class="loading-msg alignCenter topmargin25 bold"><img src="'
													+ $j("#contextRootPath")
															.val()
													+ '/images/loading.gif"  align="top" class="rightmargin10" />Sending mail... Please wait</div>');
							showLoadingDialog();
							$j(".ui-dialog-titlebar").hide();
							setTimeout(function() {
								closeLoadingDialog('#loading', '#newTermDiv');

							}, 900);
							var date = new Date();
							var curr_date = date.getDate();
							var curr_month = date.getMonth();
							curr_month = curr_month + 1;
							var curr_year = date.getFullYear();
							date = curr_date + '/' + curr_month + '/'
									+ curr_year;
							var termInformation = new Object();
							var picklistName = $j("#termForm :selected").text();
							var pickListId = $j("#termForm :selected").val();
							if (pickListId == 0) {
								picklistName = "";
							}
							var form = new Object();

							$j("#languageSlct :selected").each(function() {
								texts.push($(this).text());
							});

							var form = {
								formId : pickListId,
								formName : picklistName,
								createdBy : null,
								createDate : null,
								updatedBy : null,
								updateDate : null,
								isActive : 'Y'
							}

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

							var picklistName = $j("#termCategory :selected")
									.text();
							var pickListId = $j("#termCategory :selected")
									.val();
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

							termInformation.termId = null;
							termInformation.termBeingPolled = $j("#termInfo")
									.val();
							termInformation.termStatusId = ($j(
									"#termStatus :selected").val() == 0) ? null
									: $j("#termStatus :selected").val();
							termInformation.termCategory = category;
							termInformation.termUsage = $j("#cncptExample")
									.val();
							termInformation.termForm = form;
							termInformation.termPOS = partsOfSpeech;
							termInformation.termProgram = program;
							termInformation.termNotes = $j("#termNotes").val();
							termInformation.termLangId = 17;
							termInformation.conceptDefinition = $j("#cncptDef")
									.val();
							termInformation.suggestedTerm = $j("#trnsltn")
									.val();
							//      termInformation.suggestedTermLangId=values;
							termInformation.suggestedTermLanguages = texts;
							termInformation.createdBy = null;
							termInformation.createDate = null;
							termInformation.updatedBy = null;
							termInformation.updateDate = null;
							termInformation.isActive = null;
							var termInformationObject = Object
									.toJSON(termInformation);
							$t.requestNewTerm(termInformationObject, {
								success : function(data) {
									texts = [];
									values = 0;
									validateTermDetails.resetForm();
								},
								error : function(xhr, textStatus, errorThrown) {
									console.log(xhr.responseText);
									if (Boolean(xhr.responseText.message)) {
										console.log(xhr.responseText.message);
									}
								}
							});
						}

						if (values == null || values == 0) {
							$j(".languageSlctErr").show();
							$j("#rqstNewTerm .ui-state-default").css("border",
									"1px solid red");
						} else {
							$j("#rqstNewTerm  .ui-state-default").css("border",
									"1px solid #DDDDDD");
							$j(".languageSlctErr").hide();
						}
					});

	var displayTopTerms = function(data) {
		var topTerms = data.topTerms;
		var topTermsTmpl = [ '<span class="', '', '">', '',
				'</span>&nbsp;&nbsp,&nbsp;&nbsp;' ];

		for (var count = 0; count < topTerms.length; count++) {
			var randomnumber = Math.floor(Math.random() * 11);
			var topTermsTmplClone = topTermsTmpl;
			topTermsTmplClone[1] = "tag" + randomnumber;
			topTermsTmplClone[3] = topTerms[count].termSrc;
			$j('#listTrms').append(topTermsTmplClone.join(""));
		}
	};

	var insertCommmas = function(value) {

		var length = value.length;
		var counter = 0;
		var returnValue = "";

		for (var i = length - 1; i >= 0; i--) {
			returnValue = value.charAt(i)
					+ ((counter > 0 && counter % 3 == 0) ? ',' : '')
					+ returnValue;
			counter++;
		}

		return returnValue;

	};

	var showLeaderBoardSeeAll = function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		$j("#allUserList:ui-dialog").dialog("destroy");
		$j("#allUserListByLang:ui-dialog").dialog("destroy");

		$j("#allUserList").dialog({
			height : 500,
			width : 500,
			modal : true
		});
		if ($j('#leaderboard_seeAll').length > 0) {
			console.log(oTable);
			if (!oTable) {
				oTable = $j('#leaderboard_seeAll')
						.dataTable(
								{
									"bProcessing" : true,
									"sServerMethod" : "GET",
									"bAutoWidth" : false,
									"sAjaxSource" : $j("#contextRootPath")
											.val()
											+ "/teaminology_ctrler/teaminology/getAllMembers",
									"aoColumns" : [ {
										"mDataProp" : "photoPath",
										"bSortable" : false,
										"sWidth" : "15%",
										"sClass" : "alignCenter"
									}, {
										"mDataProp" : "userName",
										"sWidth" : "25%"
									}, {
										"mDataProp" : "totalVotes",
										"sWidth" : "20%",
										"sClass" : "alignCenter"
									}, {
										"mDataProp" : "bagdingPhotopath",
										"sWidth" : "20%"
									}, {
										"mDataProp" : "accuracyPhotoPath",
										"sWidth" : "20%"
									} ],
									"iDisplayLength" : 8
								});
			}
			$j('.dataTables_length').hide();
		}
	};

	/**
	 var accuracyChartData=function(){
		
		$t.getUserAccuracyRate({
			success:function(data){
				var finalisedTerms = data.finalizedTerm;
				var votedTerms = data.votedTerms;
				var dataXML = new Array();
				if(votedTerms==0) {
					$j("#accuracyRate").html("0%");
					if ($j("#hpsite").val() == "true")
						dataXML.push("<chart showPercentValues='1'  bgColor='#e6eaeb,#e6eaeb' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568'  canvasbgColor='#e6eaeb' showBorder='0' bgAlpha='100'>");
					else
						dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");

					dataXML.push("<set label='Approved' value='"+finalisedTerms+"' />");
					dataXML.push("<set label='Incorrect' value='"+(votedTerms-finalisedTerms)+"' />");
					dataXML.push("</chart>");
					ChartRender.twoDPieChart("0",130,70, "accuracyPie", dataXML );

				}else{	
					
				var accuracyRate = Math.round((finalisedTerms/votedTerms) * 100);
				$j("#accuracyRate").html(accuracyRate+"%")
					if ($j("#hpsite").val() == "true")
						dataXML.push("<chart showPercentValues='1'  bgColor='#e6eaeb,#e6eaeb' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568'  canvasbgColor='#e6eaeb' showBorder='0' bgAlpha='100'>");
					else
						dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");
				dataXML.push("<set label='Approved' value='"+finalisedTerms+"' />");
				dataXML.push("<set label='Incorrect' value='"+(votedTerms-finalisedTerms)+"' />");
				dataXML.push("</chart>");
				ChartRender.twoDPieChart("0",130,70, "accuracyPie", dataXML );
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
	$j()
			.ready(
					function() {
						$j("#currentSuperMenuPage").val(
								Constants.MENU_ITEMS.DASHBOARD);
						// window.location = window.location.href+'?eraseCache=true';
						/**
						 if($j("#adminHeaderFlag").val() == "true"){
								$j("#adminHeader").show();
								$j('#adminDashboard').css('color','#0DA7D5');
								$j('#adminDashboard').children("img").show();
								$j(".accuracyChrt").hide();
							}

						 $j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.DASHBOARD);

						 if($j("#userHeaderFlag").val() == "true"){
							 $j("#userHeader").show();
							 $j('#userDashboard').children("img").show();
							 $j(".accuracyChrt").show();
							 $j(".newTrmRqst").show();
							 $j(".numUsersChrt").addClass("rightmargin15");
							 accuracyChartData();
							}

						 if($j.browser.version=="7.0"){
						//        		$j(".termRqstBtn").css("padding","1px 25px");
						    	$j(".termRqstBtn").css("padding","5px 0");
							}
						 if($j("#headerFlag").val() == "true"){
								  $j(".signUpButton").hide();
								  $j("#signOutLink").show();
								  $j(".welcomeMsg").show();
								$j(".userInfoContainer").show();
								$t.getUserDetails({
									success:function(data){
										displayUserDetails(data);
										 $j("#welcomeMsg").html("Welcome, "+data.userName);
									},
									error: function(xhr, textStatus, errorThrown){
										console.log(xhr.responseText);
										if(Boolean(xhr.responseText.message)){
											console.log(xhr.responseText.message);
										}
									}
								});
						  }

						 if(window.location.search == "?msg=InvalidLogin"){
							 popup_sh();
							 if($j.browser.version=="7.0") 
								{
								$j('.PopupDiv').css('height','225px');
								}
							}
						 */
						$j(".priodLinks span").first().addClass("on");
						$j(".priodLinks span").last().css("border-right",
								"none");

						$j(".priodLinks span.off").click(function() {
							if ($j(this).hasClass("off")) {

								$j(".priodLinks span").each(function(index) {
									$j(this).removeClass("on");
									$j(this).addClass("off");
								});

								$j(this).addClass("on");
							}
						});

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

						 $t.getTotalUsersInSystem({
								success:function(data){
									var totalUsersPerMonth = data;
									var dataXML = new Array();
									if(totalUsersPerMonth != null && totalUsersPerMonth!=""){
							if ($j("#hpsite").val() == "true")
								dataXML.push("<chart showValues='0' showBorder='0' bgColor='#e6eaeb,#e6eaeb' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' yAxisMinValue='0' yAxisMaxValue='30' showToolTip='1' chartTopMargin='5' canvasbgColor='#e6eaeb' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
							else
								dataXML.push("<chart showValues='0' showBorder='0' bgColor='#032D39,#032D39' bgAlpha='100,100' canvasPadding='0px' yAxisMinValue='0' yAxisMaxValue='30' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#ffffff' divLineThickness='2' labelPadding='0'  baseFontColor='#C0C0C0' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
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
						 */
						$t
								.getTermsInGlossary({
									success : function(data) {
										var d = new Date();
										var termsPerYear = data;
										var dataXML = new Array();
										if ($j("#hpsite").val() == "true")
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0'  divLineColor = '#d3d3d3' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
										else
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
										if (termsPerYear != null) {
											for (var count = 0; count < termsPerYear.length; count++) {
												dataXML
														.push("<set label='"
																+ termsPerYear[count].interval
																+ "' "
																+ "value='"
																+ termsPerYear[count].termsPerInterval
																+ "'/>");
											}
										}
										dataXML.push("</chart>");
										ChartRender.twoDLineChart("myChartId",
												"240", "150", "chartContainer",
												dataXML);
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});
						$j('#yearly')
								.click(
										function() {
											$t
													.getTermsInGlossary({
														success : function(data) {
															var termsPerYear = data;
															var dataXML = new Array();
															if ($j("#hpsite")
																	.val() == "true")
																dataXML
																		.push("<chart showValues='0' showBorder='0' bgAlpha='0' divLineColor='#d3d3d3'  plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
															else
																dataXML
																		.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
															if (termsPerYear != null) {
																for (var count = 0; count < termsPerYear.length; count++) {
																	dataXML
																			.push("<set label='"
																					+ termsPerYear[count].interval
																					+ "' "
																					+ "value='"
																					+ termsPerYear[count].termsPerInterval
																					+ "'/>");
																}
															}
															dataXML
																	.push("</chart>");
															var chartReference = FusionCharts("myChartId");
															chartReference
																	.setXMLData(dataXML);
														},
														error : function(xhr,
																textStatus,
																errorThrown) {
															console
																	.log(xhr.responseText);
															if (Boolean(xhr.responseText.message)) {
																console
																		.log(xhr.responseText.message);
															}
														}
													});

											$t
													.getTermsDebated({
														success : function(data) {

															var termsDebatedPerYear = data;
															var dataXML = new Array();
															if ($j("#hpsite")
																	.val() == "true")
																dataXML
																		.push("<chart showValues='0' showBorder='0' bgAlpha='0' divLineColor='#d3d3d3' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0'   showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
															else
																dataXML
																		.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
															if (termsDebatedPerYear != null) {
																for (var count = 0; count < termsDebatedPerYear.length; count++) {
																	dataXML
																			.push("<set label='"
																					+ termsDebatedPerYear[count].interval
																					+ "' "
																					+ "value='"
																					+ termsDebatedPerYear[count].termsPerInterval
																					+ "'/>");
																}
															}
															dataXML
																	.push("</chart>");
															var chartReference = FusionCharts("myChartId1");
															chartReference
																	.setXMLData(dataXML);
														},
														error : function(xhr,
																textStatus,
																errorThrown) {
															console
																	.log(xhr.responseText);
															if (Boolean(xhr.responseText.message)) {
																console
																		.log(xhr.responseText.message);
															}
														}
													});

											$t
													.getGlossaryTmTerms({
														success : function(data) {
															var tmsPerYear = data;
															var dataXML = new Array();
															if ($j("#hpsite")
																	.val() == "true")
																dataXML
																		.push("<chart showValues='0' showBorder='0' bgAlpha='0' divLineColor='#d3d3d3' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0'   showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
															else
																dataXML
																		.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
															if (tmsPerYear != null) {
																for (var count = 0; count < tmsPerYear.length; count++) {
																	dataXML
																			.push("<set label='"
																					+ tmsPerYear[count].interval
																					+ "' "
																					+ "value='"
																					+ tmsPerYear[count].termsPerInterval
																					+ "'/>");
																}
															}
															dataXML
																	.push("</chart>");
															var chartReference = FusionCharts("myChartIdTm");
															chartReference
																	.setXMLData(dataXML);
														},
														error : function(xhr,
																textStatus,
																errorThrown) {
															console
																	.log(xhr.responseText);
															if (Boolean(xhr.responseText.message)) {
																console
																		.log(xhr.responseText.message);
															}
														}
													});

										});

						$t
								.getGlossaryTmTerms({
									success : function(data) {
										var tmsPerYear = data;
										var dataXML = new Array();
										if ($j("#hpsite").val() == "true")
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0'   divLineColor = '#d3d3d3' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0'  showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
										else
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");

										if (tmsPerYear != null) {
											for (var count = 0; count < tmsPerYear.length; count++) {
												dataXML
														.push("<set label='"
																+ tmsPerYear[count].interval
																+ "' "
																+ "value='"
																+ tmsPerYear[count].termsPerInterval
																+ "'/>");
											}
										}
										dataXML.push("</chart>");
										ChartRender.twoDLineChart(
												"myChartIdTm", "240", "150",
												"chartContainerTm", dataXML);
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});

						$t
								.getTermsDebated({
									success : function(data) {
										var termsDebatedPerYear = data;
										var dataXML = new Array();
										if ($j("#hpsite").val() == "true")
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0'   divLineColor = '#d3d3d3' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0'  showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='2' baseFont='HP Simplified' lineColor='#0e94bc'>");
										else
											dataXML
													.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
										if (termsDebatedPerYear != null) {
											for (var count = 0; count < termsDebatedPerYear.length; count++) {
												dataXML
														.push("<set label='"
																+ termsDebatedPerYear[count].interval
																+ "' "
																+ "value='"
																+ termsDebatedPerYear[count].termsPerInterval
																+ "'/>");
											}
										}
										dataXML.push("</chart>");
										ChartRender.twoDLineChart("myChartId1",
												"240", "150",
												"chartContainer1", dataXML);
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});

						$t.getTotalTermsInGlossary({
							success : function(data) {
								var totalTerms = new String(data);
								totalTerms = insertCommmas(totalTerms);
								$j("#totalTerms").html(totalTerms);
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
									console.log(xhr.responseText.message);
								}
							}
						});

						$t.getTotalTermsInTM({
							success : function(data) {
								var totalTermsTm = new String(data);
								totalTermsTm = insertCommmas(totalTermsTm);
								$j("#totalTms").html(totalTermsTm);
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
									console.log(xhr.responseText.message);
								}
							}
						});

						$t.getTotalDebatedTerms({
							success : function(data) {
								var totalTerms1 = new String(data);
								var totalTerms2 = insertCommmas(totalTerms1);
								$j("#totalDebatedTrms").html(totalTerms2);
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
									console.log(xhr.responseText.message);
								}
							}
						});

						/*qtip modal dialog*/

						$t
								.getTopTermDetails({
									success : function(data) {

										var topTerms = data;
										if (topTerms != null) {
											var topTermsTmpl = [
													'<span  class="', '', '">',
													'',
													'</span>&nbsp;&nbsp;&nbsp;&nbsp;' ];

											for (var i = 0; i < topTerms.length; i++) {
												var randomnumber = Math
														.floor(Math.random() * 11);
												var topTermsTmplClone = topTermsTmpl;
												topTermsTmplClone[1] = "tag"
														+ randomnumber;
												topTermsTmplClone[3] = topTerms[i];
												$j('#listTrms').append(
														topTermsTmplClone
																.join(""));
											}
										} else {
											$j('#noTerm').show();
										}
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});
						var oTable;
						$j('.seeAll')
								.click(
										function() {
											var values = ($j(
													"#termlanguageSlctForRank")
													.val() == null) ? "0" : $j(
													"#termlanguageSlctForRank")
													.val();
											if (values != 0) {
												showLeaderBoardSeeAllByLanguage();
											} else {
												showLeaderBoardSeeAll();
											}
										});

						/*  $j('#about').click(function(){
						  	alert("The Terminology Community is a social media program aimed at managing and standardizing HP " +
						  			"terminology through crowdsourcing.  You are an expert in your domain, and know which words or phrases are important " +
						  			"in this domain and should be used consistently to ensure that we are all speaking HP Voice. ");
						  });*/

						// Parts of Speech Picklist
						$t.getPOSList({
							success : function(data) {
								displayPosList(data);
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
									console.log(xhr.responseText.message);
								}
							}
						});

						// Term Form Picklist
						$t.getFormList({
							success : function(data) {
								displayTermFormList(data);
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
									console.log(xhr.responseText.message);
								}
							}
						});

						// Program Project Picklist
						$t.getProgramList({
							success : function(data) {
								displayProgramList(data);
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
									console.log(xhr.responseText.message);
								}
							}
						});
						//product group list

						$t.getProductGroupList({
							success : function(data) {
								displayProductGroupList(data);
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
									console.log(xhr.responseText.message);
								}
							}
						});
						// Status Picklist
						$t.getStatusList({
							success : function(data) {
								displayStatusList(data);
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
									console.log(xhr.responseText.message);
								}
							}
						});

						// Language List

						$t
								.getLanguageList({
									success : function(data) {
										displayLanguages(data, '#languageSlct');

										$j("#languageSlct").multiselect()
												.multiselectfilter();
										$j("#languageSlct")
												.multiselect(
														{
															selectedList : 3,
															onClose : function() {
																values = ($j(
																		"#languageSlct")
																		.val() == null) ? "0"
																		: $j(
																				"#languageSlct")
																				.val();
																if (values != 0) {
																	$j(
																			".languageSlctErr")
																			.hide();
																	$j(
																			"#rqstNewTerm .ui-state-default")
																			.css(
																					"border",
																					"1px solid #dddddd");
																}
															},
															classes : "lang"
														});
									},
									error : function(xhr, textStatus,
											errorThrown) {
										console.log(xhr.responseText);
										if (Boolean(xhr.responseText.message)) {
											console
													.log(xhr.responseText.message);
										}
									}
								});
						/*  $t.getLanguageList({
						      success: function (data) {
						          displayLanguages(data, '#termlanguageSlctForRank');

						          $j("#termlanguageSlctForRank").multiselect().multiselectfilter();
						          $j("#termlanguageSlctForRank").multiselect({
						              noneSelectedText: ' ','Select language(s)',
						              selectedList: 3,
						              onClose: function () {
						                  values = ($j("#termlanguageSlctForRank").val() == null) ? "0" : $j("#termlanguageSlctForRank").val();
						                  if(values.indexOf('All') != -1) {
						                  	showLeaderBoardSeeAll();
						                  } else if (values != 0) {
						                     // $j(".languageSlctErr").hide();
						                      $j("#rqstNewTerm .ui-state-default").css("border", "1px solid #dddddd");
						                      showLeaderBoardSeeAllByLanguage();
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
						  });*/

						// Term Category Picklist
						$t.getCategoryList({
							success : function(data) {
								displayTermCatList(data);
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
									console.log(xhr.responseText.message);
								}
							}
						});

						// Concept Category Picklist
						$t.getConceptCategoryList({
							success : function(data) {
								displayConceptCatList(data);
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
									console.log(xhr.responseText.message);
								}
							}
						});

					});
	var oTableForLeaderBoardLang;
	var showLeaderBoardSeeAllByLanguage = function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		$j("#allUserList:ui-dialog").dialog("destroy");
		$j("#allUserListByLang:ui-dialog").dialog("destroy");

		$j("#allUserListByLang").dialog({
			height : 500,
			width : 500,
			modal : true
		});
		if ($j('#leaderboardByLanguage').length > 0) {
			oTableForLeaderBoardLang = $j('#leaderboardByLanguage')
					.dataTable(
							{
								"bProcessing" : true,
								"sServerMethod" : "GET",
								"bAutoWidth" : false,
								"bDestroy" : true,
								"bAutoWidth" : false,
								"sAjaxSource" : $j("#contextRootPath").val()
										+ "/teaminology_ctrler/teaminology/getAllMembersByLanguage/"
										+ values,
								"aoColumns" : [ {
									"mDataProp" : "photoPath",
									"bSortable" : false,
									"sWidth" : "15%",
									"sClass" : "alignCenter"
								}, {
									"mDataProp" : "userName",
									"sWidth" : "25%"
								}, {
									"mDataProp" : "totalVotes",
									"sWidth" : "20%",
									"sClass" : "alignCenter"
								}, {
									"mDataProp" : "bagdingPhotopath",
									"sWidth" : "20%"
								}, {
									"mDataProp" : "accuracyPhotoPath",
									"sWidth" : "20%"
								} ],
								"iDisplayLength" : 8
							});

			$j('.dataTables_length').hide();
		}
		$j("#termLangSlctDropdwn").val(
				$j("#termLangSlctDropdwn option:first").val());
	};

	$t.getLanguageList({
		success : function(data) {
			displayLanguages(data, '#termlanguageSlctForRank');

			$j("#termlanguageSlctForRank").multiselect().multiselectfilter();
			$j("#termlanguageSlctForRank")
					.multiselect(
							{
								noneSelectedText : 'Select language(s)',
								selectedList : 3,
								onClose : function() {
									values = ($j("#termlanguageSlctForRank")
											.val() == null) ? "0" : $j(
											"#termlanguageSlctForRank").val();
									if (values == 0) {
										LeaderBoard.showLeaderBoard();
									} else {
										$j("#rqstNewTerm .ui-state-default")
												.css("border",
														"1px solid #dddddd");
										showLeaderBoardForLanguage(values);
									}
								},
								classes : "lang"

							});
			$j(".langDropDownSpan").css('top', '-32px');
		},
		error : function(xhr, textStatus, errorThrown) {
			console.log(xhr.responseText);
			if (Boolean(xhr.responseText.message)) {
				console.log(xhr.responseText.message);
			}
		}
	});

	var showLeaderBoardForLanguage = function(values) {
		$j('#leadersList').empty();
		$t
				.getLeaderBoardMemberDetailsForLanguage(
						values,
						{
							success : function(data) {
								var leaders = data;
								if (leaders && leaders.length) {
									var membersList = (leaders.length > 2) ? 2
											: leaders.length;
									for (var count = 0; count < membersList; count++) {
										var accuracyImg = "";
										var badgingImg = "";
										var status = "";

										var accuracyRate = Math
												.round(leaders[count].accuracy);
										//var badgingRate = leaders[count].totalVotes + leaders[count].termRequestCount;
										var badgingRate = leaders[count].totalVotes;
										if (accuracyRate >= 0
												&& accuracyRate < 25) {
											accuracyImg = $j("#contextRootPath")
													.val()
													+ "/images/BeginnerAccuracy.jpg";
										} else if (accuracyRate >= 25
												&& accuracyRate < 50) {
											accuracyImg = $j("#contextRootPath")
													.val()
													+ "/images/NoviceAccuracy.jpg";
										} else if (accuracyRate >= 50
												&& accuracyRate < 100) {
											accuracyImg = $j("#contextRootPath")
													.val()
													+ "/images/RegularAccuracy.jpg";
										} else if (accuracyRate >= 100
												&& accuracyRate < 200) {
											accuracyImg = $j("#contextRootPath")
													.val()
													+ "/images/AdvancedAccuracy.jpg";
										} else if (accuracyRate >= 200) {
											accuracyImg = $j("#contextRootPath")
													.val()
													+ "/images/ExpertAccuracy.jpg";
										}

										if (badgingRate > 0 && badgingRate < 25) {
											badgingImg = $j("#contextRootPath")
													.val()
													+ "/images/biginner.jpg";
										} else if (badgingRate >= 25
												&& badgingRate < 50) {
											badgingImg = $j("#contextRootPath")
													.val()
													+ "/images/novice.jpg";
										} else if (badgingRate >= 50
												&& badgingRate < 100) {
											badgingImg = $j("#contextRootPath")
													.val()
													+ "/images/regular.jpg";
										} else if (badgingRate >= 100
												&& badgingRate < 200) {
											badgingImg = $j("#contextRootPath")
													.val()
													+ "/images/advanced.jpg";
										} else if (badgingRate >= 200) {
											badgingImg = $j("#contextRootPath")
													.val()
													+ "/images/expert.jpg";
										}

										var leaderBoardTmplClone = leaderBoardTmpl;
										var totalCount = leaders[count].totalVotes;
										var totalTerms1 = new String(totalCount);

										leaderBoardTmplClone[1] = leaders[count].photoPath;
										leaderBoardTmplClone[3] = leaders[count].userName;
										;
										leaderBoardTmplClone[5] = badgingImg;
										leaderBoardTmplClone[7] = insertCommmas(totalTerms1);
										leaderBoardTmplClone[9] = accuracyImg;
										$j('#leadersList').append(
												leaderBoardTmplClone.join(""));

										//bindEvents();
									}

									if (leaders.length <= 2) {
										$j(".seeAll").hide();
									} else {
										$j(".seeAll").show();
									}

								} else {
									$j(".seeAll").hide();
									$j('#leadersList')
											.html(
													'<span style="text-align: center; display: block;font-size:12px;padding-top: 15px;">No members in Leader Board</span>');
								}

							},
							error : function(xhr, textStatus, errorThrown) {
								if (Boolean(xhr.response))
									console.error(xhr.response.messsage);
							}
						});
	}
})(window, jQuery);
