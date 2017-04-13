(function(window, $j) {
	var	 multiJobValues;
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
	var displayJobList= function(data,appenderClass) {
		var jobList = data;
		for ( var count = 0; count < jobList.length; count++) {
			var jobTmplClone = slctOptionsTmpl;
			jobTmplClone[1] = jobList[count].jobId;
			jobTmplClone[3] = jobList[count].jobName;
			$j(appenderClass).append(jobTmplClone.join(""));
		}
	};	
	var slctOptionsTmpl = ['<option value="',
		                    '',
		                    '" >',
		                    '',
		                    '</option>'
		                    ];
	
	var displayLanguageList = function(data, appenderClass) {
		var languageList = data;
		for ( var count = 0; count < languageList.length; count++) {
			var langSlctTmplClone = slctOptionsTmpl;
			langSlctTmplClone[1] = languageList[count].languageId;
			langSlctTmplClone[3] = languageList[count].languageLabel;
			$j(appenderClass).append(langSlctTmplClone.join(""));
		}
	};
	$j("#export").click(function(){

		$j("#exportForm").show();
	});
	
	
	$j().ready(function(){
		$j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.GLOBALSIGH);
		$j("#currentMenuPage").val(Constants.SUBMENU_ITEMS_GLOBALSIGHT.VIEW_TERMS);
		
		if($j("#adminHeaderFlag").val() == "true"){
			$j("#adminHeader").show();
		}
		if($j("#userHeaderFlag").val() == "true"){
			$j("#userHeader").show();

		}
		$j('#globalsight').addClass("on");

		$j(".subMenuLinks a").last().css("border-right", "none");

//		$t.getAllJobs({
//			success:function(data){
//				alert(data);
//			},
//			error: function(xhr, textStatus, errorThrown){
//				console.log(xhr.responseText);
//				if(Boolean(xhr.responseText.message)){
//					console.log(xhr.responseText.message);
//				}
//			}
//		});
		$t.getLanguageList({
			success:function(data){
				displayLanguageList(data, '#langsSlct');
				displayLanguageList(data, '#tagetLangsSlct ');

			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});

		$t.getUserDetails({
			success:function(data){
				displayUserDetails(data,'#userInfo');

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
					$j("#totalUsers").html(data);
				}

			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		
		$t.getAllJobs({
			success:function(data){
				displayJobList(data,"#jobSlct");
					
			    $j("#jobSlct").multiselect().multiselectfilter();
				
			    $j("#jobSlct").multiselect({
			      	   noneSelectedText: 'Select jobs',
			    	   selectedList: 2, // 0-based index
			    	   onClose:function(){
			    		    		   
			    		   multiJobValues = $j("#jobSlct").val();
			    	$t.getFileInfoByJobIds(multiJobValues,{
						success:function(data){
							
						},
						error: function(xhr, textStatus, errorThrown){
							console.log(xhr.responseText);
							if(Boolean(xhr.responseText.message)){
								console.log(xhr.responseText.message);
							}
						}
					});
			     
			    							    
		   	   }
//			    	
			    		   
			    });
			    
 	    
			
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
                    	dataXML.push("<chart showValues='0' adjustDiv='0' showBorder='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' bgColor='#F9F9F9,#F9F9F9' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#F9F9F9' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='2' lineColor='#0e94bc' canvasBorderThickness='0'>");
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

});     
	
})(window, jQuery);