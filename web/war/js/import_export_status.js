(function(window, $j) {
	var  companyValidate;
	var impExpListLimit = 10;
	var type=null;
	var fileIdVal=0;
	var htmlVar='';
	
	  var url = $j(location).attr('href');

		if(url.indexOf("/import_export_status.jsp") != -1) {
		    $j('#header').removeClass('subMenu');
		    $j('#header').addClass('subMenuForImport');
		    $j('#about').hide();
		}
	$j.fn.importExportDetails = function() {

		var ctx = $j(this);

		var importExportListTmpl =  new Template(['<div class="rowBg"><div  class="chkBx" chkBxId="',
		                                          '#{fileUploadStatusId}',
		                		     '"><input type="checkbox"  disabled="true" class="floatleft case" value="test" /></div><div class="rowImpExp" fileId="',
					   				'#{fileUploadStatusId}',
					   				'" id="rowid_',
					   				'#{fileUploadStatusId}',
					   				'"><div class="width30 bigFont toppadding5 serialNo">',
					   				'#{numCount}',
					   				'</div><div class="width150 bigFont toppadding5 fileName">',
					   				'#{fileName}',
					   				'</div><div class="width90 bigFont toppadding5 fileType">',
					   				'#{fileType}',
					   				'</div><div class="width150 toppadding5 startTime">',
					   				'#{startDateVal}',
					   				'</div><div class="width130 toppadding5 endTime">',
					   				'#{endDateVal}',
					   				'</div><div class="width150 toppadding5 fileStatus">',
					   				'#{fileStatus}',
					   				'</div><div class="width100 toppadding5 fileDownload " >',
					   				'#{downloadStr}',
					   				'</div><div class="width100 toppadding5 fileLog" >',
					   				'#{fileUploadStr}',
					   				'</div></div></div><div class="clear">'
					   				].join(""));  
		
		var startWaiting = function (msg, selector, floatRight){
			if(selector) {
				if (typeof msg == 'undefined' || msg == null) {
					msg = "&nbsp; Loading... please wait.";
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
			
			  var uploadTMXStatus= function(fileIdVal,id,fileId){
					$t.getTMXUploadStatus(fileId,{
					success:function(data){
						  if(data != null&& data.proccesedPercentage==100 && data.fileUrl!=null){	
							  var importExportDetailsLst = ''; 
								 importExportDetailsLst =data;

								 var length = (importExportDetailsLst.length >= impExpListLimit)?impExpListLimit:importExportDetailsLst.length;
								if(data.fileType=="Import TM"){
									var	 endDateTM=null;
									var	endDateVal=null;
									var endHours = null;
								    var endMinutes = null;
									if(data.endTime!=null){
										endDateTM= new Date(data.endTime);
										endDateVal=endDateTM.getMonth()+1+"/"+endDateTM.getDate()+"/"+endDateTM.getFullYear()+" ";
										 if(endDateTM.getHours()<10){
											 endHours = "0"+endDateTM.getHours();
											 endDateVal = endDateVal+endHours;
										 }else{
											 endDateVal=endDateVal+endDateTM.getHours();
										 }
										 if(endDateTM.getMinutes()<10){
											 endMinutes = "0"+endDateTM.getMinutes();
											 endDateVal = endDateVal+":"+endMinutes;
										 }else{
											 endDateVal = endDateVal+":"+endDateTM.getMinutes();
										 }
									}

									$j("#"+id).find(".endTime").html(endDateVal!=null?endDateVal:"");
									if(data.fileStatus=="Import Completed" || data.fileStatus=="Import failed"){
										$j("#"+id).find(".fileStatus").html(data.fileStatus);
										$j("#"+id).find(".fileLog").html('<div id="downloadLogFile"><span class="logFile nextEnable">Download</span></div>');
									}else{
										$j("#"+id).find(".fileLog").html('<div id="downloadLogFile"><span class="logFile nextEnable">N/A</span></div>');
									}
							  }
								if(data.fileType=="Export TM"){
									var	 endDateTM=null;
									var	endDateVal=null;
									var endHours = null;
									var endMinutes = null;
									if(data.endTime!=null){
										endDateTM= new Date(data.endTime);
										endDateVal=endDateTM.getMonth()+1+"/"+endDateTM.getDate()+"/"+endDateTM.getFullYear()+" ";
										 if(endDateTM.getHours()<10){
											 endHours = "0"+endDateTM.getHours();
											 endDateVal = endDateVal+endHours;
										 }else{
											 endDateVal=endDateVal+endDateTM.getHours();
										 }
										 if(endDateTM.getMinutes()<10){
											 endMinutes = "0"+endDateTM.getMinutes();
											 endDateVal = endDateVal+":"+endMinutes;
										 }else{
											 endDateVal = endDateVal+":"+endDateTM.getMinutes();
										 }
										 
									}
									$j("#"+id).find(".endTime").html(endDateVal!=null?endDateVal:"");
									$j("#"+id).find(".fileStatus").html("Export Completed");
									if(data.fileStatus=="Export Completed"){
										$j("#"+id).find(".fileDownload ").html('<div id="download"><span class="downloadFile nextEnable">Download</span></div>');
									}else{
										$j("#"+id).find(".fileDownload ").html('<div id="download"><span class="downloadFile nextEnable">N/A</span></div>');  
									}
								}
								
								 for ( var count = 0; count < length; count++) {

									 var status=importExportDetailsLst[count].fileStatus;
									 var importFileId=importExportDetailsLst[count].fileUploadStatusId;
									 if(status=="Import Is In Progress" || status=="Import Is in Progress" || status=="File Upload is in Progress"){

									 }else{
										 $j('#importExportRowsList .rowBg').find('.chkBx[chkBxId='+importFileId+']').find("input").attr("disabled", false);
									 }
								 }
								$j("#download .downloadFile").click(function(){
									var id=$j(this).parent().parent().parent().attr("fileId");
									location.href=$j("#contextRootPath").val()+"/impExp_serv?c=exportTMXFile&fileId="+id;
								});
								
								$j("#downloadLogFile .logFile").click(function(){
									var id=$j(this).parent().parent().parent().attr("fileId");
									location.href=$j("#contextRootPath").val()+"/impExp_serv?c=downloadLogFile&fileId="+id;
								});
								  
				        }
						  else if(data != null && data.fileUrl==null){
							
							  	setTimeout(function(){
							  	  uploadTMXStatus(fileIdVal,id,fileId);
								},2000);
							  	
							  	
						  } else if(data!=null && data.proccesedPercentage==0 && data.fileStatus=="Invalid File"){
							
							  $j("#downloadLogFile .logFile").click(function(){
									var id=$j(this).parent().parent().parent().attr("fileId");
									location.href=$j("#contextRootPath").val()+"/impExp_serv?c=downloadLogFile&fileId="+id;
								}); 	
						  }
						  else{
							
					        	setTimeout(function(){
					        		  uploadTMXStatus(fileIdVal,id,fileId);
								},2000);
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
		
			 var displayImportExportDetails = function(data,pageNum) {

				 var fileId=null;
				 var importExportDetailsLst = ''; 
				 importExportDetailsLst =data;

				 var length = (importExportDetailsLst.length >= impExpListLimit)?impExpListLimit:importExportDetailsLst.length;
				 var downloadTMXStatus= function(fileIdVal,id,fileId){
					 uploadTMXStatus(fileIdVal,id,fileId);
				 };
				 htmlVar = '';
				 for ( var count = 0; count < length; count++) {
					 var counter=0;
					 var startDate = null;
					 var endDate = null;
					 var startDateVal=null;
					 var endDateVal=null;
					 var endHours = null;
					 var endMinutes = null;
					 if(importExportDetailsLst[count].startTime!=null || importExportDetailsLst[count].startTime!=undefined){
						 startDate=new Date(importExportDetailsLst[count].startTime);
						 startDateVal=startDate.getMonth()+1+"/"+startDate.getDate()+"/"+startDate.getFullYear()+" ";
						 if(startDate.getHours()<10){
							 hours = "0"+startDate.getHours();
							 startDateVal = startDateVal+hours;
						 }else{
							 startDateVal=startDateVal+ startDate.getHours();
						 }
						 if(startDate.getMinutes()<10){
							 minute = "0"+startDate.getMinutes();
							 startDateVal = startDateVal+":"+minute;
						 }else{
							 startDateVal = startDateVal+":"+startDate.getMinutes();
						 }
					 }


					 if(importExportDetailsLst[count].endTime!=null || importExportDetailsLst[count].endTime!=undefined){
						 endDate=new Date(importExportDetailsLst[count].endTime);
						 endDateVal=endDate.getMonth()+1+"/"+endDate.getDate()+"/"+endDate.getFullYear()+" ";
						 if(endDate.getHours()<10){
							 endHours = "0"+endDate.getHours();
							 endDateVal = endDateVal+endHours;
						 }else{
							 endDateVal=endDateVal+endDate.getHours();
						 }
						 if(endDate.getMinutes()<10){
							 endMinutes = "0"+endDate.getMinutes();
							 endDateVal = endDateVal+":"+endMinutes;
						 }else{
							 endDateVal = endDateVal+":"+endDate.getMinutes();
						 }
					 }

					 if(pageNum==0){
						 importExportDetailsLst[count].numCount = count+1;

					 }else{
						 console.log((pageNum-1)*impExpListLimit+(count+1));
						 importExportDetailsLst[count].numCount =(pageNum-1)*impExpListLimit+(count+1);
					 }


					 importExportDetailsLst[count].startDateVal =startDateVal==null ?"":startDateVal;
					 importExportDetailsLst[count].endDateVal = endDateVal==null ?"":endDateVal;
					 var status=importExportDetailsLst[count].fileStatus;
					 var importFileId=importExportDetailsLst[count].fileUploadStatusId;

					 importExportDetailsLst[count].fileStatus=status;
					 var percentage=importExportDetailsLst[count].proccesedPercentage;
					 var type=importExportDetailsLst[count].fileType;
					var checkBox="chkBxId_"+importExportDetailsLst[count].fileUploadStatusId;
					 var id="rowid_"+importExportDetailsLst[count].fileUploadStatusId;

					 downloadTMXStatus(importExportDetailsLst[count].proccesedPercentage,id,importExportDetailsLst[count].fileUploadStatusId);

					 if(percentage==100 && type=="Export TM"){
						 if(status=="Export Completed"){
							 importExportDetailsLst[count].downloadStr='<div id="download"><span class="downloadFile nextEnable">Download</span></div>';
						 }else{
							 importExportDetailsLst[count].downloadStr="N/A";
						 }
					 }else if( type=="Import TM"){
						 importExportDetailsLst[count].downloadStr="N/A";
					 }else
					 {
						 importExportDetailsLst[count].downloadStr=null;
					 }

					 if(type=="Import TM"){
						 if(status=="Import Completed" || status=="Import failed"){
							 if(importExportDetailsLst[count].fileLogUrl!=null){
								 importExportDetailsLst[count].fileUploadStr = '<div id="downloadLogFile"><span class="logFile nextEnable">Download</span></div>';
							 } 
						 }else if((percentage==100) || (percentage==0 && status=="Invalid File")){
							 if(importExportDetailsLst[count].fileLogUrl!=null){
								 importExportDetailsLst[count].fileUploadStr = '<div id="downloadLogFile"><span class="logFile nextEnable">Download</span></div>';
							 }
						 }else{
							 importExportDetailsLst[count].fileUploadStr = "N/A"; 
						 }
					 }
					 else if( type=="Export TM"){
						 importExportDetailsLst[count].fileUploadStr = "N/A";
					 }else
					 {
						 importExportDetailsLst[count].fileUploadStr = null;
					 }
					 htmlVar = htmlVar+importExportListTmpl.evaluate(importExportDetailsLst[count]);

				 }
				 $j('#importExportRowsList').html(htmlVar);

				 $j("#download .downloadFile").click(function(){
					 var id=$j(this).parent().parent().parent().attr("fileId");
					 location.href=$j("#contextRootPath").val()+"/impExp_serv?c=exportTMXFile&fileId="+id;
				 });
				 if( $j.browser.version=="7.0"){
					 $j('#importExportRowsList .rowBg').css("width","980px");
					}

				 $j("#downloadLogFile .logFile").click(function(){
					 var id=$j(this).parent().parent().parent().attr("fileId");
					 location.href=$j("#contextRootPath").val()+"/impExp_serv?c=downloadLogFile&fileId="+id;
				 });
				 for ( var count = 0; count < length; count++) {

					 var status=importExportDetailsLst[count].fileStatus;
					 var importFileId=importExportDetailsLst[count].fileUploadStatusId;
					 if(status=="Import is in Progress" || status=="Import Is In Progress" ||  status=="File Upload is in Progress"){

					 }else{
						 $j('#importExportRowsList .rowBg').find('.chkBx[chkBxId='+importFileId+']').find("input").attr("disabled", false);
					 }
				 }

			 };
//				ctx.bind("showImportExportStatusDetails", function(event, criteria) {
//					var pageNum = criteria.pageNum ;
//				var type="TMX";
//				
//				$t.deleteAllImportFiles(0, criteria.colName, criteria.sortOrder, criteria.pageNum,{
//					success:function(data){
//	                 		triggerImportExportDetails(null,null,0);
//					},
//					error : function(xhr, textStatus, errorThrown) {
//						console.log(xhr.responseText);
//						if(Boolean(xhr.responseText.message)){
//							console.log(xhr.responseText.message);
//						}
//					}
//				});
//
//
//	});
		
		
			ctx.bind("showImportExportDetails", function(event, criteria) {
				startWaiting("&nbsp;Loading ... please wait", $j('#importExportRowsList'), false);
				var pageNum = criteria.pageNum ;
			var type="TMX";
			
			$t.getImportExportData(0, criteria.colName, criteria.sortOrder, criteria.pageNum,{
				success:function(data){
					var impExpDetails = data;
					var startLimit = 0;
					var endLimit = 0;
					if(impExpDetails == null || impExpDetails ==""){
						totalCount = 0;
						noOfPages = 0;
						$j("#pagination").hide();
						$j('#importExportRowsList').html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">No Data to display</span>' );
					}else{
						
						var length = (impExpDetails.length >= impExpListLimit)?impExpListLimit:impExpDetails.length;
						
						if(pageNum == 0){
							totalCount = impExpDetails.length;
						}
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
						
						displayImportExportDetails(data,pageNum);
						
						  showRolePrivileges();
						$j("#pagination").show();
					}
					$j("#rangeOfList").html(startLimit + "-"+ endLimit);
					var totalCount1 = insertCommmas(new String(totalCount));
					 $j("#totalRecords").html(totalCount1);		
					pagination(noOfPages, pageNum);
					
					
					  $j("#importSelectAll").click(function (event) {
						  event.stopPropagation(); 
						  if($j(this).is(':checked')){
							  $j('.case').each(function(i){
								  if(!$j(this).is(':disabled')){
									  $j(this).attr('checked', true);
								  }
							  }); 
						  }else{
							  $j('.case').attr('checked', false);
						  }
				    });
					  
					
					  $j('#importExportRowsList input[type="checkbox"]').click(function(){
						     var countcheck = $j('#importExportRowsList input[type="checkbox"]:checked').length;
						      if(countcheck != length) {
						    	 $j("#importSelectAll").attr("checked",false);
						     }else{
						    	 $j("#importSelectAll").attr("checked",true);
						     }
						 });
					$j('div.fileName').each(function(i){
						var origText = $j(this).text();
						if(origText.length > 15){
						var finalText = origText.substring(0,13);
						finalText=finalText+"...";
						$j(this).text(finalText);
						$j(this).attr("title",origText); 
						}
						
					});
					
					
				       
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
	
	$j(".next").click(function(){
		if($j(".next").hasClass("nextEnable")){
			var colName = savedCriteria.colName;
			var sortOrder = savedCriteria.sortOrder;
			var pageNum = (savedCriteria.pageNum == 0)? (savedCriteria.pageNum +2):(savedCriteria.pageNum+ 1);
			savedCriteria.pageNum = pageNum;
			triggerImportExportDetails(colName, sortOrder, pageNum);
		};
});
	
	$j(".previous").click(function(){
	if($j(".previous").hasClass("prevEnable")){
			var colName = savedCriteria.colName;
			var sortOrder = savedCriteria.sortOrder;
			var pageNum = savedCriteria.pageNum -1;
			triggerImportExportDetails(colName, sortOrder, pageNum);
	}
		
	});

		/*$j("#manageImportExportHead div").click(function(){
		var sortOrder = $j(this).attr('sortOrder');
		var colName =  $j(this).attr('id');
		if((colName == "column5")||(colName == "column0")){
			return;
		}
		if(!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))){
			$j("#manageImportExportHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder','ASC');
			});
				$j(this).attr('sortOrder', 'DESC');
				$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
				$j(this).addClass("ascending");
			}else if($j(this).hasClass("ascending")){
			  	$j("#manageImportExportHead div").each(function(index) {
			  		$j(this).removeClass("ascending descending");
			  		$j(this).find('.sort').remove();
			  		$j(this).attr('sortOrder','ASC');
			  	});
			  	$j(this).attr('sortOrder', 'ASC');
			  	$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
			  	$j(this).addClass("descending");
			}else if($j(this).hasClass("descending")){
					$j("#manageImportExportHead div").each(function(index) {
					$j(this).removeClass("ascending descending");
					$j(this).find('.sort').remove();
					$j(this).attr('sortOrder','ASC');
				});
			$j(this).attr('sortOrder', 'DESC');
			$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
			$j(this).addClass("ascending");
			}
		triggerImportExportDetails(type,colName, sortOrder, 1);
		
		});*/
	
		
	var triggerImportExportDetails = function(colName,sortOrder,pageNum){
		 $j("#importSelectAll").attr('checked',false);
		if(colName == null){
			$j("#manageImportExportHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder','ASC');
			});
		}
			$j('#importExportRowsList').empty();
			ChartRender.destroyTwoDPieChart();
			var criteria = {
					'colName':colName,
					'sortOrder':sortOrder,
					'pageNum': pageNum
			};
			savedCriteria = criteria;
			$j("#manageImportExportTbl").trigger("showImportExportDetails", criteria);
	};
	
	
//	var triggerImportExportStatusDetails = function(colName,sortOrder,pageNum){
//		 $j("#importSelectAll").attr('checked',false);
//		if(colName == null){
//			$j("#manageImportExportHead div").each(function(index) {
//				$j(this).removeClass("ascending descending");
//				$j(this).find('.sort').remove();
//				$j(this).attr('sortOrder','ASC');
//			});
//		}
//			$j('#importExportRowsList').empty();
//			ChartRender.destroyTwoDPieChart();
//			var criteria = {
//					'colName':colName,
//					'sortOrder':sortOrder,
//					'pageNum': pageNum
//			};
//			savedCriteria = criteria;
//			$j("#manageImportExportTbl").trigger("showImportExportStatusDetails", criteria);
//	};

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
		
//		$j("#deleteAllImports").click(function(){
//			deleteAllImports();
//			
//    });
		
//		var deleteAllImports = function(){
//			
//			   selectedFileIds="";
//				$j( "#delete_cnfm:ui-dialog" ).dialog( "destroy" );
//				$j( "#delete_cnfm" ).dialog({
//					resizable: false,
//					height:150,
//					width:400,
//					modal: true,
//					buttons: {
//						"Delete": function() {
//							var separator = "";
//							var selectedcount =0;
//							var deletedcount = 0;
//							triggerImportExportStatusDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum);
//							$j( this ).dialog( "close" );
//							 
//						},
//						Cancel: function() {
//							 $j('#termAction').val(0);
//							$j( this ).dialog( "close" );
//						}
//					},
//					close: function(event, ui) { 
//						$j('#termAction').val(0);
//					}
//				});
//			};
		
		$j("#deleteMultipleImports").click(function(){
			if($j("#importExportRowsList input:checked").length == 0){
				alertMessage("#termSelctMsg");
				}else{
					 deleteImportFiles();
			}
		});
		var deleteImportFiles = function(){
			   selectedFileIds="";
				$j( "#delete_cnfm:ui-dialog" ).dialog( "destroy" );
				
				$j( "#delete_cnfm" ).dialog({
					resizable: false,
					height:150,
					width:400,
					modal: true,
					buttons: {
						"Delete": function() {
							var separator = "";
							var selectedcount =0;
							var deletedcount = 0;
							  $j("#importExportRowsList input:checked:not(:disabled)").each(function(i) {
								  var deleteflag = true;
								  selectedcount++;
								  deletedcount++;
//								 var status = $j(this).parent().next().find(".fileStatus").html();
//								if(status=="Import Completed" || status=="Import failed" || status=="File upload failed"){
									    selectedFileIds += separator+$j(this).parent().next().attr("fileId");
										  separator = ",";
										  var viewNode = $j(this).parent().parent().next().next();
										  viewNode.remove();
										  $j(this).parent().parent().remove();
											 $j(this).parent().parent().remove();
						
								     });
						
							 if(selectedFileIds!=""){
								 deleteImportDetails(selectedFileIds,deletedcount,selectedcount);
							  }
//							 else{
//								  $j('#failmsg').html("The selected file status is in proress.");
//								  $j( "#mail_failed" ).dialog({
//										modal: true,
//										buttons: {
//											OK: function() {
//												$j(this).dialog( "close" );
//											}
//										}
//									});
//							  }
							  $j('#termAction').val(0);
								
								
							$j( this ).dialog( "close" );
							 
						},
						Cancel: function() {
							 $j('#termAction').val(0);
							$j( this ).dialog( "close" );
						}
					},
					close: function(event, ui) { 
						$j('#termAction').val(0);
					}
				});
			};
			
		
			   var deleteImportDetails = function(selectedFileIds,deletedcount,selectedcount){
				 	var fileIdsArray = selectedFileIds.split(",");
				 	 	$t.deleteImportFiles(fileIdsArray, {
						success : function(data) {
							totalCount= totalCount-(fileIdsArray.length);
							var pageNum = savedCriteria.pageNum;
							var totalTermsTillBefore = (pageNum-1) * 10;
							if(totalTermsTillBefore == totalCount){
								  savedCriteria.pageNum = pageNum-1;
							}
////							 compSelectedVal=[];
////							 langSelectedVal=[];
//							 	$j("#newTmTerm").val("Enter term to search...");
//								 $j("#tmLanguageSlct").multiselect("uncheckAll");
//								 $j("#companyTmsSlct").multiselect("uncheckAll");
								 triggerImportExportDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum);
//							 alertMessage("#deleteTM");
						},
						error : function(xhr, textStatus, errorThrown) {
							console.log(xhr.responseText);
							if(Boolean(xhr.responseText.message)){
								console.log(xhr.responseText.message);
							}
						}
					});
			      };
		$j().ready(function(){
			var users = null;
			showRolePrivileges();

			$j("#currentMenuPage").val(Constants.SUBMENU_ITEMS.MANAGE_TERMS);
			$j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.ADMIN);

			if( $j.browser.version=="7.0" || $j.browser.version=="9.0" ||  $j.browser.version=="8.0"){
				$j("#importSelectAll").css("margin-top","-2px");
				$j("#importSelectAll").css("margin-left","3px");
				
			}
			if( $j.browser.version=="7.0"){
				
				$j("#deleteMultipleImports").css("padding","3px 0px");
				$j(".siteContainer").css("width","1020px");
				$j(".header").css("width","1020px");
				$j("#manageImportExportTbl").css("width","980px");
				$j(".loginBtn").css("padding-right","3px");
				$j(".editBtn").css("margin-left","110px");
				$j(".editBtn").css("margin-top","-12px");
				$j(".importStausBtn").css("width","1012px");
				$j(".checkboxCol").css("width","50px");
				
			}	
		
		$j(".subMenuLinks a").last().css("border-right", "none");
		
		$j("#manageImportExportTbl").importExportDetails();
		triggerImportExportDetails(null,null,0);
        
        
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
					 var totalTerms1 = new String(data);
                     var totalTerms2 = insertCommmas(totalTerms1);
                      users=totalTerms2;
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
                    ChartRender.twoDLineChart("myChartId35", "240", "70", "userChart", dataXML);
                }else{
                    $j("#myChartId35").hide();
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