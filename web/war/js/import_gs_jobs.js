(function(window, $j) {
	var  companyValidate;
	var impExpListLimit = 10;
	var type=null;
	var fileIdVal=0;
	var totalCount=0;
	 $j('.signoutAdmin').css('padding-left','250px');
	$j.fn.importExportDetails = function() {

		var ctx = $j(this);

		var gsJobListTmpl = ['<div class="rowBg" id="rowBg_',
		                     '',
		                     '"><div class="row twistie_close rowJobImpExp" jobId="',
					   				'',
					   				'" id="rowid_',
					   				'',
					   				'"><div class="width200  toppadding5 jobId">',
					   				'',
					   				'</div><div class="width200  toppadding5 jobName">',
					   				'',
					   				'</div><div class="width150 toppadding5 projectName">',
					   				'',
					   				'</div><div class="width200 toppadding5 jobStatus">',
					   				'',
					   				'</div><div class="width150 toppadding5 jobStartDate">',
					   				'',
					   				'</div></div></div><div class="clear"></div><div class="gsViewDtlsRow nodisplay"  id="overView" termId="',
					   				'',
					   				'"><div class="loadingDiv" style="text-align: center;"><img src='+$j("#contextRootPath").val()+'/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
					   				];

		var termAttrHeaderTmpl='<div id="jobId" class="width200">Task Id</div>'
							+'<div id="jobName" class="width200">Task Name</div>'
							+'<div id="projectName" class="width100">Target Language</div>'
							+'<div id="jobStatus" class="width100" >Status</div>'
							+'<div id="jobStatus" class="width100" >Action</div>';
							
		var termAttrTmpl = '<div class="taskrowHeader"> <div id="column0" class="width25 ">&nbsp;</div>'
			+'<div id="jobId" class="width150">Task Id</div>'
			+'<div id="jobName" class="width170">Task Name</div>'
			+'<div id="sourceLanguage" class="width150">Source Language</div>'
			+'<div id="targetLanguage" class="width150">Target Language</div>'
			+'<div id="jobStatus" class="width150" >Status</div>'
			+'<div id="Logs" class="width100" >Logs</div>'
			+'</div><hr/>'
			;
	
		var taskAttrTmpl=['<div class="taskrow"><div style="margin-top: 2px;" class="chkBx " chkBxId="',
		                  '',
		                  '"><input type="checkbox" class="floatleft case " value="test"  disabled="true" /></div> <div  taskId="',
			   				'',
			   				'" id="taskid_',
			   				'',
			   				'"><div class="width150  toppadding5 taskId">',
			   				'',
			   				'</div><div class="width170  toppadding5 taskName">',
			   				'',
			   				'</div><div class="width150 toppadding5 taskSourceLang">',
			   				'',
			   				'</div><div class="width150 toppadding5 taskTargetLang">',
			   				'',
			   				'</div><div class="width150 toppadding5 taskStatus ">',
			   				'',
			   				'</div><div class="width100 toppadding5 tasklogUrl">',
			   				'',
			   				'</div></div><div class="clear"></div></div>'
			   				];

				
		var displayGSJobDetails = function(data,pageNum) {
			Util.stopWaiting($j('#importExportRowsList'));
			var fileId=null;
			var gsJobDetailsLst = data[0].job;
			for ( var count = 0; count < data[0].job.length; count++) {
				var gsJobTmplClone = gsJobListTmpl;
				gsJobTmplClone[1] = gsJobDetailsLst[count].id;
				gsJobTmplClone[3] = gsJobDetailsLst[count].id;
				gsJobTmplClone[5] = gsJobDetailsLst[count].id;
				gsJobTmplClone[7] = gsJobDetailsLst[count].id;
				gsJobTmplClone[9] = gsJobDetailsLst[count].name;
				gsJobTmplClone[11] = gsJobDetailsLst[count].project.projectName;
				gsJobTmplClone[13] = gsJobDetailsLst[count].createDate;
				gsJobTmplClone[15] = gsJobDetailsLst[count].startDate;
				gsJobTmplClone[17] = gsJobDetailsLst[count].id;
				$j('#importExportRowsList').append(gsJobTmplClone.join(""));
			}

		};
		
		$j('#importGSTerms ').click(function(){
			var isChecked=false;
			 $j("#importExportRowsList .gsViewDtlsRow  .taskrow .chkBx  input:checked").each(function(i) {
				 $(this).attr("checked", false);
				 $(this).attr("disabled", true);
				 isChecked=true;
				var taskId =$j(this).parent().attr("chkbxid");
				var taskName=$j(this).parent().next().find(".taskName").html();
				var jobId=$j(this).parent().parent().parent().attr("termid");
				var jobName=$j(this).parent().parent().parent().siblings('.rowBg[id=rowBg_'+jobId+'] ').find('.rowJobImpExp ').find('.jobName').attr("title");
				$j(this).parent().next().find(".taskStatus").html(Constants.GS_STATUS.IN_PROGRESS);
				$t.importGSTasks(taskId,taskName,jobId,jobName,{
					success:function(data){
					/*$j("#importExportRowsList .row").addClass("twistie_close");
					$j("#importExportRowsList .row").removeClass("twistie_open");*/
					},
					error: function(xhr, textStatus, errorThrown){
						console.log(xhr.responseText);
						if(Boolean(xhr.responseText.message)){
							console.log(xhr.responseText.message);
						}
					}
					
				});
				 });
			 if(!isChecked){
				 alertMessage("#taskerrror")
			 }else{
				 alertMessage("#importedtask")
			 }
		});
		var bindEvents = function(data){
			
			$j('#importExportRowsList .row ').click(function(){
				var row = $j(this);
				var detailsSec = row.parent().siblings('.gsViewDtlsRow[termId='+row.attr('jobId')+'] ');
				detailsSec.html("");
				Util.startWaiting("&nbsp;Loading ... please wait",detailsSec, false);
				if($j(this).hasClass('twistie_close')){
					$j(this).parent().next().next().show();
					$j(this).removeClass("twistie_close");
					$j(this).addClass("twistie_open");
					var jobId=$j(this).attr("jobid");
					showRolePrivileges();
					$t.getTasksInJob(jobId,{
						success:function(data){
							if(Boolean(data)){
								if(data!=null && data.size()>0){
								var termInfo = data[0].task;
								var defaultImg=$j("#contextRootPath").val()+"/images/default.jpg";
									var termAttrIds={
										taskId:'taskId_'+jobId,
										taskName:'taskName_'+jobId,
										taskState:'taskState_'+jobId,
										taskTargetLangId:'taskTargetLangId_'+jobId,
										taskStatusId:'taskStatusId_'+jobId
										   
								}
								var editTermTmpl=new Template(termAttrTmpl).evaluate(termAttrIds);
								var termHeaderTmpl=new Template(termAttrHeaderTmpl);
								detailsSec.html(editTermTmpl);
								var taskAttrTmplClone = taskAttrTmpl;
						    	for ( var count = 0; count < termInfo.length; count++){
									
									Util.stopWaiting( row.parent().siblings('.gsViewDtlsRow[termId='+row.attr('jobId')+'] '));
									var taskId= termInfo[count].id;
									taskAttrTmplClone[1] = termInfo[count].id;
									taskAttrTmplClone[3] = termInfo[count].id;
									taskAttrTmplClone[5] = termInfo[count].id;
									taskAttrTmplClone[7] = termInfo[count].id;
									taskAttrTmplClone[9] = termInfo[count].name;
									taskAttrTmplClone[11] = termInfo[count].sourceLanguage;
									taskAttrTmplClone[13] = termInfo[count].targetLanguage;
									var status=termInfo[count].fileInfoStatus;
									taskAttrTmplClone[15] = termInfo[count].fileInfoStatus==null ? Constants.GS_STATUS.NOT_IMPORTED:termInfo[count].fileInfoStatus;
									if(termInfo[count].logUrl!=null){
									taskAttrTmplClone[17] ='<div id="downloadLogFile"><span class="logFile nextEnable">Download</span></div>';
									}else{
										taskAttrTmplClone[17]="N/A";
									}
										
									detailsSec.append(taskAttrTmplClone.join(""));
									if(status==Constants.GS_STATUS.IMPORTED || status=="In progress" ||status==Constants.GS_STATUS.IN_PROGRESS || status==Constants.GS_STATUS.EXPORTED){
										detailsSec.find('.taskrow').find('.chkBx[chkBxId='+taskId+']').children("input").attr("disabled", true);
									}else{
										detailsSec.find('.taskrow').find('.chkBx[chkBxId='+taskId+']').children("input").removeAttr("disabled");
									}
								
								}
								
								$j("#downloadLogFile .logFile").click(function(){
									var id=$j(this).parent().parent().parent().attr("taskId");
									location.href=$j("#contextRootPath").val()+"/impExp_serv?c=downloadGSLogFile&taskId="+id+"&isImport=Y";;
								});
								
								
								detailsSec.find('.editDetailsFld').show();	
								
							}else{
								detailsSec.html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">No Data to display</span>');
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
					
					
				}
				else{
					$j(this).parent().next().next().hide();
					$j(this).removeClass("twistie_open");
					$j(this).addClass("twistie_close");
										$j(this).find('.editDetailsFld').hide();
					$j(this).find('.viewDetailsFld').show();
				}
			});
			
			
			
		};
		
		
		
		
	   ctx.bind("showGSJobDetails", function(event, criteria) {
			$t.getJobsByState(criteria.pageNum,{
				success:function(data){
					if(data!=null){
					var pageNum = criteria.pageNum ;
					var gsJobDetails = data[0].job;
					var startLimit = 0;
					var endLimit = 0;
					if(gsJobDetails == null || gsJobDetails ==""){
						totalCount = 0;
						noOfPages = 0;
						$j("#pagination").hide();
						$j('#importExportRowsList').html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">No Data to display</span>' );
					}else{
						
						var length = (gsJobDetails.length >= impExpListLimit)?impExpListLimit:gsJobDetails.length;
						totalCount=$j("#totalJobs").val();
						totalCount=parseInt(totalCount);
						noOfPages = Math.round(totalCount/10) ;
						noOfPages = (totalCount%10 < 5 && totalCount%10 > 0)? noOfPages+1:noOfPages;
						if(pageNum == 0){
							startLimit = 1;
							endLimit = (impExpListLimit > totalCount)? totalCount : impExpListLimit;
						}else{
							startLimit = ((pageNum - 1)* impExpListLimit)+1;
							var tempLimit  = (pageNum) * impExpListLimit;
							endLimit =(parseInt(tempLimit) > parseInt(totalCount))? totalCount :tempLimit ;
						}
						
						displayGSJobDetails(data,pageNum);
						
						  showRolePrivileges();
						$j("#pagination").show();
				
					$j("#rangeOfList").html(startLimit + "-"+ endLimit);
					var totalCount1 = Util.insertCommmas(new String(totalCount));
					 $j("#totalRecords").html(totalCount1);		
					pagination(noOfPages, pageNum);
					bindEvents();
					$j('div.jobName').each(function(i){
						var origText = $j(this).text();
						$j(this).attr("title",origText); 
						if(origText.length > 20){
						var finalText = origText.substring(0,18);
						finalText=finalText+"...";
						$j(this).text(finalText);
						}
						
					});
				}	
				       
				}else{
					totalCount = 0;
					noOfPages = 0;
					$j("#pagination").hide();
					$j('#importExportRowsList').html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">No Data to display</span>' );

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
	
	$j(".next").click(function(){
		if($j(".next").hasClass("nextEnable")){
			var colName = savedCriteria.colName;
			var sortOrder = savedCriteria.sortOrder;
			var pageNum = (savedCriteria.pageNum == 0)? (savedCriteria.pageNum +2):(savedCriteria.pageNum+ 1);
			savedCriteria.pageNum = pageNum;
			triggerGSJobDetails(colName, sortOrder, pageNum);
		};
});
	
	$j(".previous").click(function(){
	if($j(".previous").hasClass("prevEnable")){
			var colName = savedCriteria.colName;
			var sortOrder = savedCriteria.sortOrder;
			var pageNum = savedCriteria.pageNum -1;
			triggerGSJobDetails(colName, sortOrder, pageNum);
	}
		
	});

		
		
	var triggerGSJobDetails = function(colName,sortOrder,pageNum){
		if(colName == null){
			$j("#manageImportExportHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder','ASC');
			});
		}
			$j('#importExportRowsList').empty();
			ChartRender.destroyTwoDPieChart();
			 
			 Util.startWaiting("&nbsp;Loading ... please wait", $j('#importExportRowsList'), false);
//			$t.getJobsCount({
//				success:function(data){
//					
//					var totalTerms1 = new String( data);
//					var totalTerms2 = insertCommmas(totalTerms1);
//					$j("#totalRecords").html(totalTerms2);
//					$j("#totalJobs").val(totalTerms2)
//					var criteria = {
//							'colName':colName,
//							'sortOrder':sortOrder,
//							'pageNum': pageNum
//					};
//					savedCriteria = criteria;
//					$j("#importTbl").trigger("showGSJobDetails", criteria);
//				},
//				error: function(xhr, textStatus, errorThrown){
//					console.log(xhr.responseText);
//					if(Boolean(xhr.responseText.message)){
//						console.log(xhr.responseText.message);
//					}
//				}
//			});
			  
				$t.getJobsCountByState({
					success:function(data){
						
						var totalTerms1 = new String( data);
						var totalTerms2 = Util.insertCommmas(totalTerms1);
						$j("#totalRecords").html(totalTerms2);
						$j("#totalJobs").val(totalTerms2)
						var criteria = {
								'colName':colName,
								'sortOrder':sortOrder,
								'pageNum': pageNum
						};
						savedCriteria = criteria;
						$j("#importTbl").trigger("showGSJobDetails", criteria);
					},
					error: function(xhr, textStatus, errorThrown){
						console.log(xhr.responseText);
						if(Boolean(xhr.responseText.message)){
							console.log(xhr.responseText.message);
						}
					}
				});
			
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
	var displayUserDetails = function(data,id) {
		var userDetails = data;
			var userDetailTmplClone = userDetailTmpl;
			var count=userDetails.totalVotes;
			var totalCompanies1 = new String(count);
		   userDetailTmplClone[1] = userDetails.photoPath;
			userDetailTmplClone[3] = userDetails.userName;
			userDetailTmplClone[6] = userDetails.createDate;
			$j(id).append(userDetailTmplClone.join(""));
	};
	
	  
		
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
		
		var pagination = function(noOfPages, pageNum){
			if((noOfPages > 1) && (pageNum < noOfPages)){
				$j(".next").addClass("nextEnable");
				$j(".next").removeClass("nextDisable");
			}else{
				$j(".next").removeClass("nextEnable");
				$j(".next").addClass("nextDisable");
			}
			
			if((noOfPages > 1) && (pageNum > 1)){
				$j(".previous").removeClass("prevDisable");
				$j(".previous").addClass("prevEnable");
			}else{
				$j(".previous").removeClass("prevEnable");
				$j(".previous").addClass("prevDisable");
			}
		};
		
		
		
		$j().ready(function(){
			
			showRolePrivileges();

			$j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.GLOBALSIGH);
			$j("#currentMenuPage").val(Constants.SUBMENU_ITEMS_GLOBALSIGHT.IMPORT);
			if( $j.browser.version=="7.0" || $j.browser.version=="9.0" ||  $j.browser.version=="8.0"){
				$j("#selectAll").css("margin-top","-2px");
				$j("#selectAll").css("margin-left","-4px");
				
			}
			if( $j.browser.version=="7.0"){
				$j(".loginBtn").css("padding-right","3px");
				$j(".editBtn").css("margin-left","110px");
				$j(".editBtn").css("margin-top","-12px");
				$j("#importGSTerms").css("margin-left","850px");
			}	
		
		$j(".subMenuLinks a").last().css("border-right", "none");
		
		$j("#importTbl").importExportDetails();
		triggerGSJobDetails(null,null,0);
        
        
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
                    ChartRender.twoDLineChart("myChartId39", "240", "70", "userChart", dataXML);
                }else{
                    $j("#myChartId39").hide();
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
		
        showRolePrivileges();

        
        
});     
	
})(window, jQuery);
