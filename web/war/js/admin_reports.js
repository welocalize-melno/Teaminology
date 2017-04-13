(function(window, $j) {
	
	var url = $j(location).attr('href');

	if(url.indexOf("/admin_reports.jsp") != -1){
	    $j('#signOut').removeClass('floatrightLogOut');
	    $j('#signOut').addClass('floatrightLogOutForOverview');
	    $j('.signoutAdmin').css('padding-left','250px');
	}
	
	$j.fn.reportDetails = function() {
		
		var ctx = $j(this);
		langTeamDetailsData = null;
		var reportListTmpl = [
		   				'<div class="rowBg" style="padding-left: 8px;"><div class="row"><div class="width252 bigFont" style="padding-top: 2px;">',
		   				'',
		   				'</div><div class="width90 toppadding5 totalMembers">',
		   				'',
		   				'</div><div class="width90 toppadding5 accuracyPrcnt">',
		   				'',
		   				'%</div><div class="width90 toppadding5 iniTrms">',
		   				'',
		   				'</div><div class="width100 toppadding5 debatedTrms">',
		   				'',
		   				'</div><div class="width90 toppadding5 actPolls">',
		   				'',
		   				'</div><div class="width90 toppadding5 monthAvg">',
		   				'',
		   				'</div><div class="width80 toppadding5">',
		   				'',
		   				'</div></div></div>'
		   				];
		
		displayteamReportDetails = function(data) {
			if(data != null){
			var langTeamDetails = data;
			for ( var count = 0; count < langTeamDetails.length; count++) {
				var reportListTmplClone = reportListTmpl;
				var members = new String(langTeamDetails[count].members);
				var totalTerms = new String(langTeamDetails[count].totalTerms);
				var debatedTerms = new String(langTeamDetails[count].debatedTerms);
				var activePolls = new String(langTeamDetails[count].activePolls);
				var monthlyAvg = new String(langTeamDetails[count].monthlyAvg);
				var totalVotes = new String(langTeamDetails[count].totalVotes);
				
				reportListTmplClone[1] = langTeamDetails[count].languageLabel;
				reportListTmplClone[3] = insertCommmas(members);
				reportListTmplClone[5] = langTeamDetails[count].accuracy;
				reportListTmplClone[7] = insertCommmas(totalTerms);
				reportListTmplClone[9] = insertCommmas(debatedTerms);
				reportListTmplClone[11] = insertCommmas(activePolls);
				reportListTmplClone[13] = insertCommmas(monthlyAvg);
				reportListTmplClone[15] = insertCommmas(totalVotes);
				$j('#teamRowsList').append(reportListTmplClone.join(""));
			}
			}
		};
		

		sortData = function(arrayData, prop, asc){
			if(Boolean(arrayData)){
				arrayData = arrayData.sort(function(a, b) {
					
					var aProp = a[prop];
					var bProp = b[prop];
		
					if(isNaN(aProp) && isNaN(bProp)){
							if (asc) {
								if (aProp > bProp){
									return 1
								}else if(aProp==bProp){
									return 0;
								}else{
									return -1;
								}
							}
					        else {
					        	if (bProp > aProp){
					        		return 1;
					        	}else if(aProp==bProp){
									return 0;
								}else{
									return -1;
								}
					        }
						}else{

					        if (asc) return aProp - bProp;
					        else return bProp - aProp;
				        }
			    });
				return arrayData;
			}
		};
		
		ctx.bind("showReportDetails", function() {
			
			$t.getLanguageReportTable({
				success:function(data){
					stopWaiting($j('#teamRowsList'))
					langTeamDetailsData = data;
					if(data != null && data != ''){
						displayteamReportDetails(data);
					}else{
						$j('#teamRowsList').html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">No data to display</span>' );;
					}
				  
				},
				error: function(xhr, textStatus, errorThrown){
					console.log(xhr.responseText);
					if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
					}
				}
			});
			
		});

	};
	
	var langSlctTmpl = ['<option value="',
	                    '',
	                    '" >',
	                    '',
	                    '</option>'
	                    ];
	
	var companyTmpl = ['<option value="',
	                   '',
	                   '" >',
	                   '',
	                   '</option>'
	               ];
	
	var displayLanguageList = function(data) {
		var languageList = data;
		for ( var count = 0; count < languageList.length; count++) {
			var langSlctTmplClone = langSlctTmpl;
			langSlctTmplClone[1] = languageList[count].languageId;
			langSlctTmplClone[3] = languageList[count].languageLabel;
			$j('#languageSlct').append(langSlctTmplClone.join(""));
		}
	};
	
	 var displayLanguageList = function (data, appenderClass) {
	        var languageList = data;
	        for (var count = 0; count < languageList.length; count++) {
	            var langSlctTmplClone = langSlctTmpl;
	            langSlctTmplClone[1] = languageList[count].languageId;
	            langSlctTmplClone[3] = languageList[count].languageLabel;
	            $j(appenderClass).append(langSlctTmplClone.join(""));
	        }
	    };
	 var displayPosList = function (data, appenderClass) {
	        var posList = data;
	        for (var count = 0; count < posList.length; count++) {
	            var partOfSpeechSlctTmplClone = langSlctTmpl;
	            partOfSpeechSlctTmplClone[1] = posList[count].partsOfSpeechId;
	            partOfSpeechSlctTmplClone[3] = posList[count].partOfSpeech;
	            $j(appenderClass).append(partOfSpeechSlctTmplClone.join(""));
	        }
	    };
	 var displayTermCatList = function (data, appenderClass) {
	        var termCatList = data;
	        for (var count = 0; count < termCatList.length; count++) {
	            var termCatSlctTmplClone = langSlctTmpl;
	            termCatSlctTmplClone[1] = termCatList[count].categoryId;
	            termCatSlctTmplClone[3] = termCatList[count].category;
	            $j(appenderClass).append(termCatSlctTmplClone.join(""));
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
	    var displayDomainList = function (data, appenderClass) {
	        var domainList = data;
	        for (var count = 0; count < domainList.length; count++) {
	            var domainSlctTmplClone = langSlctTmpl;
	            domainSlctTmplClone[1] = domainList[count].domainId;
	            domainSlctTmplClone[3] = domainList[count].domain;
	            $j(appenderClass).append(domainSlctTmplClone.join(""));
	        }
	    };
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
	                      '</h5>',
                          '<p class="smallFont">Member since: ',
	                      '',
	                      '</p></div>'
	                    ];
	var displayUserDetails = function(data) {
		if(data != null){
		var userDetails = data;
			var userDetailTmplClone = userDetailTmpl;
			var count=userDetails.totalVotes;
			var totalTerms1 = new String(count);
			userDetailTmplClone[1] = userDetails.photoPath;
			userDetailTmplClone[3] = userDetails.userName;
			userDetailTmplClone[6] = userDetails.createDate;
			$j('#userInfo').append(userDetailTmplClone.join(""));
		}
	};
	
	/*Email Field validation code ends */
	
	var showHpTeamSeeAll = function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		$( "#hpCommunityList:ui-dialog" ).dialog( "destroy" );
	
		$( "#hpCommunityList" ).dialog({
			height: 500,
			width:500,
			modal: true
		});
	};

	var insertCommas = function (value){
		var length = value.length;
		var counter = 0;
		var returnValue = "";
		for (var i = length-1 ; i >= 0; i--) {
			returnValue = value.charAt(i) + ((counter > 0 && counter % 3 == 0) ? ',': '') + returnValue;				
			counter++;
		} 
		return returnValue;
	};
	
	var showExportReports = function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		$j( "#exportRprtFilter:ui-dialog" ).dialog( "destroy" );
	
		$j( "#exportRprtFilter" ).dialog({
			height: 270,
			width: 500,
			modal: true,
			close: function(event, ui) { 
				$j("#reportSlct").val('');
				$j("#languageSlct").multiselect("uncheckAll");
			}
		});
	};
	
	var showExportVotingReports=function() {
			// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
			$j( "#exportVotingFilter:ui-dialog" ).dialog( "destroy" );

			$j( "#exportVotingFilter" ).dialog({
				height: 200,
				width: 500,
				modal: true,
			});
			
			
			
		};
	
	
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
	var startWaiting = function (msg, selector, floatRight){
		if(selector) {
			if (typeof msg == 'undefined' || msg == null) {
				msg = "Loading... please wait.";
			}
			var temp = '<div class="loading-msg alignCenter topmargin15"><img src="'+$j("#contextRootPath").val()+'/images/loading.gif" />'+msg+'</div>';
			selector.append(temp);
		
		}
	};
	var stopWaiting = function (selector){
		if(selector) {
			selector.find(".loading-msg").hide();
		}
	};
	
	var downloadStatus= function(){
		$t.getDownloadStatus({
			success:function(data){
				  if(data != null){	
		                 if (data == 1) {
		         			$j( "#exportRprtFilter:ui-dialog" ).dialog('destroy');
		         			 stopWaiting($j("#exportRprtFilter"));
		                 } else{
		                	 setTimeout(function(){
		                		 downloadStatus();
		 					},100);     
		                 }
		        }else{
		        	setTimeout(function(){
		        		downloadStatus();
					},100);
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
	
	
	var downloadStatusVote= function(){
		$t.getDownloadStatus({
			success:function(data){
				  if(data != null){	
		                 if (data == 1) {
		         			$j( "#exportVotingFilter:ui-dialog" ).dialog('destroy');
		         			 stopWaiting($j("#exportVotingFilter"));
		                 } else{
		                	 setTimeout(function(){
		                		 downloadStatusVote();
		 					},100);     
		                 }
		        }else{
		        	setTimeout(function(){
		        		downloadStatusVote();
					},100);
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
	
	
	$j().ready(function(){
		
	    $('#fromDate').attr('readonly', 'readonly');
		$j('#toDate').attr('readonly', 'readonly');
		$j("#currentMenuPage").val(Constants.SUBMENU_ITEMS.REPORTS);
		$j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.ADMIN);
		$j('#admin').css('color','#0DA7D5');
		$j('#admin').children("img").show();
		$j(".subMenuLinks a").last().css("border-right", "none");
	    $j("#reportsTbl").reportDetails();
		$j("#reportsTbl").trigger("showReportDetails");
		
		$j("#fromDate").datepicker();
		$j("#toDate").datepicker();
		$j("#fromDateHistory").datepicker();
	    $j("#toDateHistory").datepicker();
		
		if($j.browser.version=="9.0"){
			$j(".paddingseven").css("padding-left","11px");
		}
		
		if($j.browser.version=="7.0"){
			$j(".menuArrowAdmn").css("top","26");
			$j(".termAttr").css("padding-bottom","10px");
		}

        /**
		if($j("#adminHeaderFlag").val() == "true"){
			$j("#adminHeader").show();
			$j('#admin').children("img").show();
		}
        if($j("#userHeaderFlag").val() == "true"){
			$j("#userHeader").show();
			$j('#termList').children("img").show();
		}
         */
        if($j.browser.msie || $j.browser.webkit){

        	$j(".headerMenuLinks .headerMenuLink").css("padding-bottom","12px");

        	}

		//$j(ChartRender.twoDLineChart("myChartId", "240", "140", "chartContainer", "DataNoCaptn.xml"));
    	//$j(ChartRender.twoDLineChart("myChartId1", "240", "140", "chartContainer1", "Data2NoCaptn.xml"));
		
		$t.getUserDetails({
			success:function(data){
				displayUserDetails(data);
			  
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		startWaiting(" &nbsp; Loading ... please wait", $j('.momChart'), false);
		startWaiting(" &nbsp;Loading ... please wait", $j('#langTrms'), false);
		startWaiting(" &nbsp; Loading ... please wait", $j('#teamRowsList'), false);

		$t.getLanguageChartData({
			success:function(data){
				stopWaiting($j('.momChart'))
				var momReports = data;
				var dataXML = new Array();
				if(momReports != null && momReports != ""){
				  if ($j("#hpsite").val() == "true")
					dataXML.push("<chart caption='Monthly activity' showValues= '0' bgColor='FFFFFF' bgAlpha='70,80' baseFontSize='11' baseFont='HP Simplified' showBorder='0' canvasBorderThickness='1' canvasBorderColor='#eeeeee' showAlternateHGridColor='0' showYAxisValues='0' legendPosition='RIGHT' legendBgAlpha='0' legendBorderThickness='0' legendShadow='0' >");
				  else
				  	dataXML.push("<chart caption='Monthly activity' showValues= '0' bgColor='ECFFE1,FFFFFF' bgAlpha='70,80' baseFontSize='11'  showBorder='0' canvasBorderThickness='1' canvasBorderColor='#eeeeee' showAlternateHGridColor='0' showYAxisValues='0' legendPosition='RIGHT' legendBgAlpha='0' legendBorderThickness='0' legendShadow='0' >");
					dataXML.push("<categories>");
					for(var j=0;j<momReports[0].termsList.length;j++){
						dataXML.push("<category label='"+momReports[0].termsList[j].interval+"' />");
						
					}
					$(".languageDataExportTxt").show();
					dataXML.push("</categories>");
					for(var i = 0; i< momReports.length;i++){
						
						dataXML.push("<dataset seriesName='"+momReports[i].language.languageLabel+"'>");
						for(var j=0;j<momReports[i].termsList.length;j++){
							dataXML.push("<set value='"+momReports[i].termsList[j].termsPerInterval+"' />");
						}
						dataXML.push("</dataset>");
					}
					dataXML.push("</chart>");
					ChartRender.twoDMultiLine("myChartId4", "950", "300", "multiChartContainer", dataXML);
				}else{ 
					$j(".momChart").css("height","300px");
					$j(".momChart").css("background-color","#FFFFFF");
					$(".languageDataExportTxt").hide();
					$j(".momChart").html("<div style='text-align:center;color: #666666;font-family: Verdana;font-size: 10px; padding-top:100px;width:100%;'>No data to display</div>");

				}
			
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		
			
		
		});
		
		$t.getLanguagePiChart({

			success:function(data){
				stopWaiting($j('#langTrms'));
				var pieData = data;
				var dataXML = new Array();
				if(pieData != null && pieData.length>0){
				  if ($j("#hpsite").val() == "true")
					dataXML.push("<chart caption='Distribution' showPercentValues='0' showLabels='0' showLegend='1' showValues='1' baseFont='HP Simplified' showBorder='0' bgColor='#ffffff' pieRadius='50' chartRightMargin='0' showShadow='0'  animation='0' baseFontSize='11' paletteColors='#FF3E28, #FB8F4A, #8BBA00, #F6BD0F, #D64646, #008E8E, #AFD0EA' legendBorderThickness='0' legendShadow='0' legendPosition='RIGHT' >");
				  else
				  	dataXML.push("<chart caption='Distribution' showPercentValues='0' showLabels='0' showLegend='1' showValues='1'  showBorder='0' bgColor='#ffffff' pieRadius='50' chartRightMargin='0' showShadow='0'  animation='0' baseFontSize='11' paletteColors='#FF3E28, #FB8F4A, #8BBA00, #F6BD0F, #D64646, #008E8E, #AFD0EA' legendBorderThickness='0' legendShadow='0' legendPosition='RIGHT' >");
				for(var i = 0; i< pieData.length;i++){
					dataXML.push("<set label='"+pieData[i].language+"' value='"+pieData[i].noOfTerms+"' />");
				}
				dataXML.push("</chart>");
				ChartRender.twoDPieChart("langPieChartId", "380", "210", "pieChartContainer", dataXML);
				}else{
					$j("#langTrms").html("<div style='text-align:center;color: #666666;font-family: Verdana;font-size: 10px; padding-top:100px;width:100%;'>No data to display</div>");

				}
					
				
				
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		
			
		
		
		});
		
		$t.getTermsInGlossary({
			success:function(data){
				var termsPerYear = data;
				
				var dataXML = new Array();
				if ($j("#hpsite").val() == "true")
					dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showAlternateHGridColor='0' canvasBorderThickness='0' baseFont='HP Simplified' lineThickness='2' lineColor='#0e94bc'>");
				else
					dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
				if(termsPerYear != null){
					for(var count=0;count<termsPerYear.length;count++){
						dataXML.push("<set label='"+termsPerYear[count].interval + "' " + "value='" +termsPerYear[count].termsPerInterval +"'/>");
					}
				}
				dataXML.push("</chart>");
				ChartRender.twoDLineChart("myChartId", "235", "140", "chartContainer", dataXML);
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		
		//To render the data in charts for Tms
		$t.getGlossaryTmTerms({
			success:function(data){
				var tmsPerYear = data;
				var dataXML = new Array();
				dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='2' lineColor='#0e94bc'>");
						if(tmsPerYear != null){
					for(var count=0;count<tmsPerYear.length;count++){
						dataXML.push("<set label='"+tmsPerYear[count].interval + "' " + "value='" +tmsPerYear[count].termsPerInterval +"'/>");
					}
				}
				dataXML.push("</chart>");
				ChartRender.twoDLineChart("myChartIdTm", "235", "140", "chartContainerTm", dataXML);
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		
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
		
		$t.getTermsDebated({
			success:function(data){
				var termsDebatedPerYear = data;
				var dataXML = new Array();
				if ($j("#hpsite").val() == "true")
					dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' baseFont='HP Simplified' lineColor='#0e94bc'>");
				else
					dataXML.push("<chart showValues='0' showBorder='0' bgAlpha='0' showAlternateHGridColor='0' canvasBorderThickness='0' lineThickness='5px' lineColor='#6ab53a'>");
				if(termsDebatedPerYear != null){
					for(var count=0;count<termsDebatedPerYear.length;count++){
						dataXML.push("<set label='"+termsDebatedPerYear[count].interval + "' " + "value='" +termsDebatedPerYear[count].termsPerInterval +"'/>");
					}
				}
				dataXML.push("</chart>");
				ChartRender.twoDLineChart("myChartId1", "235", "140", "chartContainer1", dataXML);
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		$t.getTotalTermsInGlossary({
			success:function(data){
				var totalTerms = new String( data);
				totalTerms = insertCommmas(totalTerms);
				$j("#totalTerms").html(totalTerms);
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		
		$t.getTotalDebatedTerms({
			success:function(data){
				var totalTerms1 = new String( data);
				var totalTerms2 = insertCommmas(totalTerms1);
				$j("#totalDebatedTrms").html(totalTerms2);

			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		
		$t.getTotalTermsInTM({
			success:function(data){
				var totalTermsTm = new String( data);
				var totalTermsTms = insertCommmas(totalTermsTm);
				$j("#totalTmTerms").html(totalTermsTms);

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
                    ChartRender.twoDLineChart("myChartId10", "240", "70", "userChart", dataXML);
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
				
		
		$t.getLanguageList({
			success:function(data){
				displayLanguageList(data);
				$j("#languageSlct").multiselect().multiselectfilter();
			    $j("#languageSlct").multiselect({
			   	   noneSelectedText: 'Select language',
			    	   selectedList: 4, // 0-based index
			    	   onClose:function(){
			    		   values = ($j("#languageSlct").val() == null)?"0":$j("#languageSlct").val();
			    		   if(values != 0){
			    			   $j(".languageSlctErr").hide();
			    		   }
			    	   },
			    	   classes:"lang"
			    });
			  
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		
		
		var alertMessage = function(alertId){
			$j( alertId+":ui-dialog" ).dialog( "destroy" );
			$j( alertId ).dialog({
				height: 140,
				width:300,
				modal: true,
				buttons: {
					OK: function() {
						$j(this).dialog( "close" );
					}
				}
			});
		};
		$j("#exprtRprt").click(function(){
			showExportReports();
		});
		
		$j("#exprtVoting").click(function(){
			showExportVotingReports();
			
		});
		$j("#exportVotingResults").click(function () {
			  var condtn="";
			  var topSuggestionCheck="";
			  var altersuggestionCheck="";
			  var noOfVotesCheck="";
	    	  var startDate= $j('#fromDate').val();
	    	  var endDate= $j('#toDate').val();
	    	  if(startDate == '' || endDate == ''){
	    		  alert("please enter date, that must not empty");
	    		 }
	    	  if( Date.parse(startDate)>Date.parse(endDate)){
	    	    alert("Enter Valid date range");
	    	    return false;
	    	    }
	    	  if(!($("#topSugCandidate").is(':checked')) && !($("#alterCandidate").is(':checked')) && !($("#noofvotes").is(':checked')) ){
	    		alert("please check atleast one checkbox");
	    		}
	    	  
	    	  if( $("#topSugCandidate").is(':checked')){
	    		  topSuggestionCheck="true";
	    	  }
	    	  if($("#alterCandidate").is(':checked')){
	    		  altersuggestionCheck="true";
	    	  }
	    	  if($("#noofvotes").is(':checked')){
	    		  noOfVotesCheck="true";
	    	  }
	    	  startWaiting(" &nbsp; Downloading ... please wait", $j('#exportRprtFilter:ui-dialog'), false);
	    	  downloadStatusVote();
	    	  condtn+="&fromDate="+startDate+"&toDate="+endDate+"&topSug="+topSuggestionCheck+"&alterSug="+altersuggestionCheck+"&noofVotes="+noOfVotesCheck;
	    	    
				$j("#downloadAll").attr("action",  $j("#contextRootPath").val()+"/impExp_serv?c=exportCSV&exportType=exportVote&exportFormat=CSV"+condtn);
				
				$j("#downloadAll").submit();
	    	  
	    });
		
		
		$j("#getReports").click(function(){
			var query="";
			var userLanguages= $j("#languageSlct").val();;
			var reportType =$j("#reportSlct").val()
			if(reportType==null || reportType==0){
				alertMessage("#reportType");
			}
			else{
			startWaiting(" &nbsp; Downloading ... please wait", $j('#exportRprtFilter:ui-dialog'), false);
			if(userLanguages!=null)
			 query="&userLanguages="+userLanguages
			
			 if(reportType!=null){
				 for(i=0;i<reportType.length;i++){
						if(reportType[i]=="All Reports"){
							 reportType="Month to Month,Distribution,Terms Debated,Terms in Glossary,Overview,Tms in Glossary"
								 break;
						}
						
					}
				 query +="&reportType="+reportType;
			 }
			downloadStatus();
			$j("#downloadAll").attr("action",  $j("#contextRootPath").val()+"/impExp_serv?c=exportCSV&exportType=exportAll"+query);
			$j("#downloadAll").submit();
			}
			$j("#reportSlct").val('');
			$j("#languageSlct").multiselect("uncheckAll");
		
			
		});
		
			
		$j("#reportsHead div").click(function(){
			if($j(this).attr('sortOrder') == 'desc' || $j(this).attr('sortOrder') == null){
				$j("#reportsHead div").removeAttr("sortOrder");
				$j(this).attr('sortOrder','asc');
				langTeamDetailsData = sortData(langTeamDetailsData, $j(this).attr('prop'), true);
				$j("#reportsHead div").each(function(index) {
					$j(this).find('.sort').remove();
				});
				
				$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
			}else{
				$j("#reportsHead div").removeAttr("sortOrder");
				$j(this).attr('sortOrder','desc');
				langTeamDetailsData = sortData(langTeamDetailsData, $j(this).attr('prop'), false);
				$j("#reportsHead div").each(function(index) {
					$j(this).find('.sort').remove();
				});
				
				$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/descend.png" height="5px" width="10px" class="sort leftmargin5"/>');
			}
			$j('#teamRowsList').html('');
			displayteamReportDetails(langTeamDetailsData);
		});
		
		if ($j("#isSuperAdmin").val() == "true") {
		    $j(".exprtCatSlct").html('<option value="0">--Select--</option><option value="1">Locale</option><option  value="2">Part of speech</option><option value="3">Category</option><option value="4">Final</option><option value="4">Poll expiration</option><option value="5">Company</option><option value="6">Domain</option>');
		} else {
		    $j(".exprtCatSlct").html('<option value="0">--Select--</option><option value="1">Locale</option><option  value="2">Part of speech</option><option value="3">Category</option><option value="4">Final</option><option value="4">Poll expiration</option><option value="6">Domain</option>');
		}
		
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
		 $j("#termHistoryExport").click(function(){
		     if (($j(".exprtCatList").val() == null) && ($j(".exprtCatSlct :selected").val() != "4")) {
		         alertMessage("#termHistoryExpChkMsg");
		         return;
		     }else {
		         showExportHistoryReports();
		     }

		 });
		 
		 
		 $j("#termHistoryResults").click(function () {
		     var condtn="";
		     var startDate= $j('#fromDateHistory').val();
		     var endDate= $j('#toDateHistory').val();
		     if(startDate == '' || endDate == ''){
		         alert("please enter date, that must not empty");
		         return;
		     }
		     if( Date.parse(startDate)>Date.parse(endDate)){
		         alert("Enter Valid date range");
		         return false;
		     }
		     var exportFormat = "CSV";
		     var selectedIds = $j(".exprtCatList").val();
		     var exportBy = $j(".exprtCatSlct :selected").text();
		     startWaiting(" &nbsp; Downloading ... please wait", $j('#termHistoryFilter:ui-dialog'), false);
		     downloadStatusHistory();
		     condtn+="&fromDate="+startDate+"&toDate="+endDate;
		     $j('#downloadForm').attr('action', $j("#contextRootPath").val() + "/impExp_serv?c=exportCSV&exportType=termHistory&selectedIds=" + selectedIds + "&exportBy=" + exportBy + "&exportFormat=" + exportFormat + condtn);
		     $j('#downloadForm').submit();

		 });

		  var startLoading = function (id, msg) {
//	          var msg="Downloading ... please wait";
	        var temp = '<div class="loading-msg alignCenter topmargin15"><img src="' + $j("#contextRootPath").val() + '/images/loading.gif" />' + msg + '</div>';
	        $j("#" + id).append(temp);

	    };
	    var showExportHistoryReports=function() {
            // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
            $j( "#termHistoryFilter:ui-dialog" ).dialog( "destroy" );

            $j( "#termHistoryFilter" ).dialog({
                height: 150,
                width: 500,
                modal: true,
            });
        };
        var downloadStatusHistory= function(){
            $t.getDownloadStatus({
                success:function(data){
                      if(data != null){ 
                             if (data == 1) {
                                $j( "#termHistoryFilter:ui-dialog" ).dialog('destroy');
                                 stopWaiting($j("#termHistoryFilter"));
                             } else{
                                 setTimeout(function(){
                                     downloadStatusHistory();
                                },100);     
                             }
                    }else{
                        setTimeout(function(){
                            downloadStatusHistory();
                        },100);
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
		 
	});
	
	
	
	
	
	
})(window, jQuery);