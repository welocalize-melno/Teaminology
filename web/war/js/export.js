(function(window, $j) {
	
	var termListLimit = 10;
	var noOfPages =0;
	var totalTerms = 0;
	var savedCriteria = null;
	var termBaseData=null;
	 $j('.signoutAdmin').css('padding-left','250px');
	var userDetailTmpl = ['<div class="userImg"><img src="',
	                      '',
	                      '" height="50px" width="50px" /></div><div class="userDetails"><h5>',
	                      '',
	                      '</h5>',
                          '<p class="smallFont">Member since: ',
	                      '',
	                      '</p></div>'
	                    ];
	
	var termDtlTmpl = ['<div class="rowBg"><div  class="chkBx" chkBxId="',
	                   '',
          		        '"><input type="checkbox" class="floatleft case" value="test" /></div> <div class="row" fileInfoId="',
		   				'',
		   				'"><div class="width100   fileId toppadding5">',
		   				'',
		   				'</div><div class="width100   fileName toppadding5">',
		   				'',
		   				'</div><div class="width90   jobId toppadding5">',
		   				'',
		   				'</div><div class="width100   jobName toppadding5">',
		   				'',
		   				'</div><div class="width90   taskId toppadding5">',
		   				'',
		   				'</div><div class="width100 toppadding5 sourceLang  floatleft">',
		   				'',
		   				'</div><div class="width100 toppadding5 targetLang  floatleft">',
		   				'',
		   				'</div><div class="width90 toppadding5 status  floatleft">',
		   				'',
		   				'</div><div class="width90 toppadding5 log  floatleft">',
		   				'',
		   				'</div><div class="width40 toppadding15 delete" ><img class="headerMenuLink deleteGSExpImg" height="20px" width="20px"  src="'+ $j("#contextRootPath").val()+'/images/DeleteIcon1.png" imgId="',
		   				'',
		   			    '"/></div></div></div><div class="clear"></div>'
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
	
	var downloadStatus= function(){
		$t.getDownloadStatus({
			success:function(data){
				  if(data != null){	
		                 if (data == 1) {
		                     triggerTermDetails(null, null, 0);
		                	 
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
	var startWaiting = function (msg, selector, floatRight){
		if(selector) {
			if (typeof msg == 'undefined' || msg == null) {
				msg = "  Loading... please wait.";
			}
			var temp = '<div class="loading-msg  topmargin15" style="padding-left:250px;"><img src="'+$j("#contextRootPath").val()+'/images/loading.gif" />'+msg+'</div>';
			selector.append(temp);

		}
	};
	var stopWaiting = function (selector){
		if(selector) {
			selector.find(".loading-msg").hide();
		}
	};
	
	
	var deleteMultipleFileIds= function(){
		var selectedTermIds="";
		var inProgressTermIds="";
		var deleteflag = false;
		$j( "#delete_cnfm:ui-dialog" ).dialog( "destroy" );
		$j( "#delete_cnfm" ).dialog({
			resizable: false,
			height:150,
			width:400,
			modal: true,
			buttons: {
				"Delete": function() {
					var separator = "";
					var separatorDel="";
					var selectedcount =0;
					var deletedcount = 0;
					
					$j("#exportDtlRowsList input:checked").each(function(i) {
						var status=$j(this).parent().parent().find('.status').text();
						selectedcount++;
						if(status!=null && status==Constants.GS_STATUS.EXPORTED){
							deleteflag = true;
							deletedcount++;
							if(deleteflag){
								selectedTermIds += separator+$j(this).parent().next().attr("fileinfoid");
								separator = ",";
								var viewNode = $j(this).parent().parent().next().next();
								viewNode.remove();
								$j(this).parent().parent().remove();
								$j(this).parent().parent().remove();
							}
						}else{
							 
							inProgressTermIds+= separatorDel+$j(this).parent().parent().find('.fileId').text();
							separatorDel = ",";
						}


					});
					if(selectedTermIds!="" && deleteflag){
						deleteGSFileDetails(selectedTermIds,deletedcount,selectedcount,inProgressTermIds);
					}else{
						alertMessage("#exportMultipleStatus");
					}


					$j( this ).dialog( "close" );

				},
				Cancel: function() {

					$j( this ).dialog( "close" );
				}
			},
			close: function(event, ui) { 

			}
		});
	};
	
	
	var deleteGSFileDetails = function(selectedTermIds,deletedcount,selectedcount,inProgressTermIds){
 		var termIdsArray = selectedTermIds.split(",");
 	 	$t.deleteGSFileData(termIdsArray, {
				success : function(data) {
					totalTerms= totalTerms-(termIdsArray.length);
				     	var pageNum = savedCriteria.pageNum;
					   var totalTermsTillBefore = (pageNum-1) * 10;
					   if(totalTermsTillBefore == totalTerms){
						  savedCriteria.pageNum = pageNum-1;
					}
					triggerTermDetails(savedCriteria.colName, savedCriteria.sortOrder, savedCriteria.pageNum);
					if(inProgressTermIds==null || inProgressTermIds==""){
						alertMessage("#deleteGS");
					}else{
						var remain=selectedcount-deletedcount;
						msg=deletedcount+" files deleted out of "+selectedcount+". <br>"+inProgressTermIds+" files are not deleted as the status is In Progress."
						$j('#deleteMultiple').html(msg);
						alertMessage("#deleteMultiple");
						
					}
				},
				error : function(xhr, textStatus, errorThrown) {
					console.log(xhr.responseText);
					if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
					}
				}
			});
	      };
	
	 var deleteGSFile = function(id,status){
		 var selectedTermIds="";
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
							  var deleteflag = true;
							  selectedTermIds= id;
							  $j("#"+id).parent().parent().remove();
					 
						 if(selectedTermIds!="" && status==Constants.GS_STATUS.EXPORTED){
							 deleteGSFileDetails(selectedTermIds,deletedcount,selectedcount,null);
						  }
						 if(selectedTermIds!="" && status==Constants.GS_STATUS.IMPORTED)
							{
							 alertMessage("#exportStatus");
							}
							
						$j( this ).dialog( "close" );
						 
					},
				Cancel: function() {
				$j( this ).dialog( "close" );
				}
			},
			close: function(event, ui) { 
				
			}
		});
	};
	
	
	$j.fn.termDetails = function() {
	var ctx = $j(this);

	var classNames = ['purpleBar','greenBar','yellowBar','pinkBar'];


	var displayFileList = function(data) {
	
		stopWaiting($j('#exportDtlRowsList'));
		$j('#exportDtlRowsList').empty();
		var fileList = data;

		var listLength = (fileList.length >= termListLimit)?termListLimit:fileList.length;
		
		for ( var count = 0; count < listLength; count++) {
			var filetemplate = termDtlTmpl;
			var targetLang = (fileList[count].targetLang== null || fileList[count].targetLang == "")?"&nbsp;":fileList[count].targetLang;
			var sourceLang = (fileList[count].sourceLang == null || fileList[count].sourceLang == "")?"&nbsp;":fileList[count].sourceLang;
			var fileName = (fileList[count].fileName == null || fileList[count].fileName == "")?"&nbsp;":fileList[count].fileName;
			var jobName = (fileList[count].jobName == null || fileList[count].jobName == "")?"&nbsp;":fileList[count].jobName;
			filetemplate[1] = fileList[count].fileInfoId;
			filetemplate[3] = fileList[count].fileInfoId;
			filetemplate[5] = fileList[count].fileId;
			filetemplate[7] = fileName;
			filetemplate[9]= fileList[count].jobId;
			filetemplate[11]=jobName;
			filetemplate[13]= fileList[count].taskId;
			filetemplate[15] =sourceLang;
			filetemplate[17] = targetLang;
			filetemplate[19] = fileList[count].status;
			if(fileList[count].exportLog!=null){
				filetemplate[21] ='<div id="downloadLogFile"><span class="logFile nextEnable">Download</span></div>';
				}else{
					filetemplate[21]="N/A";
				}
			filetemplate[23] = fileList[count].fileInfoId;
			
			$j('#exportDtlRowsList').append(filetemplate.join(""));
			
			}
		 for ( var count = 0; count < listLength; count++) {
			 var fileInfoId=fileList[count].fileInfoId;
				if(fileList[count].status == "Exported"){
				 $j('#exportDtlRowsList .rowBg').find('.chkBx[chkBxId='+fileInfoId+']').find("input").attr("disabled", true);
				}
		 }
		$j('div.fileName').each(function(i){
			var origText = $j(this).text();
			if(origText.length > 12){
				var finalText = origText.substring(0,10);
				finalText=finalText+"...";
				$j(this).text(finalText);
				$j(this).attr("title",origText); 
			}
		});
		$j('div.fileId').each(function(i){
			var origText = $j(this).text();
			if(origText.length > 12){
				var finalText = origText.substring(0,10);
				finalText=finalText+"...";
				$j(this).text(finalText);
				$j(this).attr("title",origText); 
			}


		});
		$j('div.taskId').each(function(i){
			var origText = $j(this).text();
			if(origText.length > 12){
				var finalText = origText.substring(0,10);
				finalText=finalText+"...";
				$j(this).text(finalText);
				$j(this).attr("title",origText); 
			}


		});
		$j('div.jobId').each(function(i){
			var origText = $j(this).text();
			if(origText.length > 12){
				var finalText = origText.substring(0,10);
				finalText=finalText+"...";
				$j(this).text(finalText);
				$j(this).attr("title",origText); 
			}


		});

	 
		$j('div.jobName').each(function(i){
			var origText = $j(this).text();
			if(origText.length > 12){
				var finalText = origText.substring(0,10);
				finalText=finalText+"...";
				$j(this).text(finalText);
				$j(this).attr("title",origText); 
			}


		});
		$j("#downloadLogFile .logFile").click(function(){
			var id=$j(this).parent().parent().parent().find(".taskId").html();
			location.href=$j("#contextRootPath").val()+"/impExp_serv?c=downloadGSLogFile&taskId="+id+"&isImport=N";
		});
	
	};
	

	

	var bindEvents = function(data){
		var classNamesClone = classNames.slice(0);
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


	ctx.bind("showTermDetails", function(event,criteria) {
	   startWaiting(" &nbsp; Loading ... please wait", $j('#exportDtlRowsList'), false);
		$j("#pagination").hide();
		var pageNum = criteria.pageNum;
		var  queryAppender= new Object();
		queryAppender.colName = criteria.colName;
		queryAppender.sortOrder = criteria.sortOrder;
		queryAppender.pageNum = criteria.pageNum;
		var queryAppenderParameter =  Object.toJSON(queryAppender);
		$t.getimportedFileList(queryAppenderParameter, {
			success:function(data){

				termBaseData=data;
				var termDetails = data;

				if(termDetails == null||termDetails.length==0){
					var displayCriteria = "No data to display";
					$j("#exportDtlRowsList").html('<span style="text-align: center; display: block;  font-size:12px;padding-top:10px; padding-bottom:10px;padding-right:95px;border-bottom:1px solid #DDDDDD;">'+displayCriteria+'</span>' );
					$j("#pagination").hide();

				}



				if (termDetails!=null && termDetails.length!=0){
					var length = (termDetails.length >= termListLimit)?termListLimit:termDetails.length;
					var startLimit = 0;
					var endLimit = 0;
					totalTerms = termDetails.length;

					noOfPages = Math.round(totalTerms/10) ;
					noOfPages = (totalTerms%10 < 5 && totalTerms%10 > 0)? noOfPages+1:noOfPages;
					if(pageNum == 0){
						startLimit = 1;
						endLimit = (termListLimit > totalTerms)? totalTerms : termListLimit;
					}else{
						startLimit = ((pageNum - 1)* termListLimit)+1;
						var tempLimit  = (pageNum) * termListLimit;
						endLimit =(parseInt(tempLimit) > parseInt(totalTerms))? totalTerms :tempLimit ;
					}


					displayFileList(data);


					$j("#pagination").show();
					$j("#rangeOfTerms").html(startLimit + "-"+ endLimit);
					var totalTerms3 = insertCommmas(new String(totalTerms));
					$j("#totalPolledTerms").html(totalTerms3);
					pagination(noOfPages, pageNum);
					bindEvents();
			
					 $j("#selectAllFileIds").click(function (event) {
						  event.stopPropagation(); 
					        $j('.case').attr('checked', this.checked);
					       
				    });
					
					$j(".rowBg .row .deleteGSExpImg").hover(function(){
				    	 modalRender.bubble(".rowBg .row .deleteGSExpImg","Delete","left center","right center");
				   	});
					 
					  $j(".rowBg .row .deleteGSExpImg").click(function(event){
						 	var userRole = "";
				        	 var selectedTermIds = "";
				        	 	if($j("#exportDtlRowsList input:checked").length == 0   || $j("#exportDtlRowsList input:checked").length ==1){
				        	 		 var id=$j(this).attr("imgId");
				        	 		
				        	 		var expStatus=$j(this).parent().parent().find('.status').text();
				        	 		deleteGSFile(id,expStatus);
				        	 		/*if(expStatus!=null && expStatus=="exported"){
				        	 		 deleteGSFile(id);
				        	 		}else{
				        	 			 alertMessage("#exportStatus");
				        	 		}*/
				        	}
								event.stopPropagation(); 

				      	});
					
				}



			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		$j(function() {
			$j( "#datepicker" ).datepicker({
				showOn: "button",
				buttonImage: "images/calendar.gif",
				buttonImageOnly: true
			});
		});



	});

};
	
	
	
	
	$j("#exprtFile").click(function(){
		var fileInfoId=null;
		 var separator = "";
			var selectedfileInfoIds = "";
		$j("#exportDtlRowsList input:checked").each(function(i) {
			 selectedfileInfoIds += separator+$j(this).parent().next().attr("fileInfoId");
			 separator = ",";
		});
		if(selectedfileInfoIds==""){
			 alertMessage("#exportRequired");
		}else{
			$j('#exportDtlRowsList').empty();
			startWaiting(" &nbsp; Downloading ... please wait", $j('#exportDtlRowsList'), false);
			downloadStatus();
			 $j('#downloadForm').attr('action',$j("#contextRootPath").val()+"/impExp_serv?c=exportXLF&pageId="+selectedfileInfoIds);
			 $j('#downloadForm').submit();
			//location.href= ;
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


	var pagination2 = function(noOfPages, pageNum){
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

	var displayFileList = function(data,start,end,pageNum) {
		$j('#exportDtlRowsList').empty();
		$j("#pagination").show();
		savedCriteria.pageNum=pageNum;
		pagination2(noOfPages, pageNum);
		var fileList = data;
		if(end>fileList.length){
			end=fileList.length;
		}
		if(pageNum == 0){
			startLimit = 1;
			endLimit = (termListLimit > totalTerms)? totalTerms : termListLimit;
		}else{
			startLimit = ((pageNum - 1)* termListLimit)+1;
			var tempLimit  = (pageNum) * termListLimit;
			endLimit =(parseInt(tempLimit) > parseInt(totalTerms))? totalTerms :tempLimit ;
		}

		$j("#rangeOfTerms").html(startLimit + "-"+ endLimit);


		for ( var count = start; count < end; count++) {
			var filetemplate = termDtlTmpl;
			var targetLang = (fileList[count].targetLang== null || fileList[count].targetLang == "")?"&nbsp;":fileList[count].targetLang;
			var sourceLang = (fileList[count].sourceLang == null || fileList[count].sourceLang == "")?"&nbsp;":fileList[count].sourceLang;
			var fileName = (fileList[count].fileName == null || fileList[count].fileName == "")?"&nbsp;":fileList[count].fileName;
			var jobName = (fileList[count].jobName == null || fileList[count].jobName == "")?"&nbsp;":fileList[count].jobName;
			filetemplate[1] = fileList[count].fileInfoId;
			filetemplate[3] = fileList[count].fileInfoId;
			filetemplate[5] = fileList[count].fileId;
			filetemplate[7] = fileName;
			filetemplate[9]= fileList[count].jobId;
			filetemplate[11]=jobName;
			filetemplate[13]= fileList[count].taskId;
			filetemplate[15] =sourceLang;
			filetemplate[17] = targetLang;
			filetemplate[19] = fileList[count].status;
			if(fileList[count].exportLog!=null){
				filetemplate[21] ='<div id="downloadLogFile"><span class="logFile nextEnable">Download</span></div>';
				}else{
					filetemplate[21]="N/A";
				}
			filetemplate[23] = fileList[count].fileInfoId;

			$j('#exportDtlRowsList').append(filetemplate.join(""));
			
			
			}
		$j('div.fileName').each(function(i){
			var origText = $j(this).text();
			if(origText.length > 12){
				var finalText = origText.substring(0,10);
				finalText=finalText+"...";
				$j(this).text(finalText);
				$j(this).attr("title",origText); 
			}


		});
		 for ( var count = start; count < end; count++) {
				 var fileInfoId=fileList[count].fileInfoId;
				if(fileList[count].status == "Exported"){
				 $j('#exportDtlRowsList .rowBg').find('.chkBx[chkBxId='+fileInfoId+']').find("input").attr("disabled", true);
				}
		 }
	
		$j('div.fileId').each(function(i){
			var origText = $j(this).text();
			if(origText.length > 12){
				var finalText = origText.substring(0,10);
				finalText=finalText+"...";
				$j(this).text(finalText);
				$j(this).attr("title",origText); 
			}


		});
		$j('div.taskId').each(function(i){
			var origText = $j(this).text();
			if(origText.length > 12){
				var finalText = origText.substring(0,10);
				finalText=finalText+"...";
				$j(this).text(finalText);
				$j(this).attr("title",origText); 
			}


		});
		$j('div.jobId').each(function(i){
			var origText = $j(this).text();
			if(origText.length > 12){
				var finalText = origText.substring(0,10);
				finalText=finalText+"...";
				$j(this).text(finalText);
				$j(this).attr("title",origText); 
			}


		});

	 
		$j('div.jobName').each(function(i){
			var origText = $j(this).text();
			if(origText.length > 12){
				var finalText = origText.substring(0,10);
				finalText=finalText+"...";
				$j(this).text(finalText);
				$j(this).attr("title",origText); 
			}


		});
		 $j("#selectAllFileIds").click(function (event) {
			  event.stopPropagation(); 
		        $j('.case').attr('checked', this.checked);
		       
	    });
		
		$j(".rowBg .row .deleteGSExpImg").hover(function(){
	    	 modalRender.bubble(".rowBg .row .deleteGSExpImg","Delete","left center","right center");
	   	});
		 
		$j(".rowBg .row .deleteGSExpImg").click(function(event){
		 	var userRole = "";
        	 selectedTermIds = "";
        	 	if($j("#exportDtlRowsList input:checked").length == 0   || $j("#exportDtlRowsList input:checked").length ==1){
        	 		 var id=$j(this).attr("imgId");
        	 		
        	 		var expStatus=$j(this).parent().parent().find('.status').text();
        	 		 deleteGSFile(id,expStatus);
        	 		/*if(expStatus!=null && expStatus=="exported"){
        	 		 deleteGSFile(id);
        	 		}else{
        	 			 alertMessage("#exportStatus");
        	 		}*/
        	}
				event.stopPropagation(); 

      	});
		
	};

	
	
	
	var triggerTermDetails = function(colName, sortOrder, pageNum){
		 $j("#selectAllFileIds").attr('checked',false);
		if(colName == null){
			$j("#exportSectionHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder','ASC');
			});
		}

		$j('#exportDtlRowsList').empty();
		var criteria = {
				'colName':colName,
				'sortOrder':sortOrder,
				'pageNum': pageNum

		};

		savedCriteria = criteria;
		$j("#exportTbl").trigger("showTermDetails", criteria);
	};


	$j(".next").click(function(){
		if($j(".next").hasClass("nextEnable")){
			 $j("#selectAllFileIds").attr('checked',false);
			 $j(".case").attr('checked',false);
			var colName = savedCriteria.colName;
			var sortOrder = savedCriteria.sortOrder;
			var pageNum = (savedCriteria.pageNum == 0)? (savedCriteria.pageNum +2):(savedCriteria.pageNum+ 1);
			savedCriteria.pageNum = pageNum;
			displayFileList(termBaseData,(savedCriteria.pageNum-1)*termListLimit,(savedCriteria.pageNum-1)*termListLimit+(termListLimit-1),savedCriteria.pageNum);
		};
	});

	$j(".previous").click(function(){
		if($j(".previous").hasClass("prevEnable")){
			 $j("#selectAllFileIds").attr('checked',false);
			 $j(".case").attr('checked',false);
			var colName = savedCriteria.colName;
			var sortOrder = savedCriteria.sortOrder;
			var pageNum = savedCriteria.pageNum -1;
			displayFileList(termBaseData,(pageNum-1)*termListLimit,(pageNum-1)*termListLimit+(termListLimit-1),pageNum);
		}

	});


	$j("#exportSectionHead div").click(function(){
		var sortOrder = $j(this).attr('sortOrder');
		var colName =  $j(this).attr('id');
		if(colName == "column2"){
			return;
		}
		if(!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))){
			$j("#exportSectionHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder', 'ASC');
			});
			$j(this).attr('sortOrder', 'DESC');
			$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
			$j(this).addClass("ascending");
		}else if($j(this).hasClass("ascending")){
			$j("#exportSectionHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder', 'ASC');
			});
			$j(this).attr('sortOrder', 'ASC');
			$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
			$j(this).addClass("descending");
		}else if($j(this).hasClass("descending")){
			$j("#exportSectionHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder', 'ASC');
			});
			$j(this).attr('sortOrder', 'DESC');
			$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
			$j(this).addClass("ascending");
		}
		triggerTermDetails(colName, sortOrder, 1);
	});
	
	
	$j("#deleteMultipleFileIds").click(function(){
		if($j("#exportTbl input:checked").length == 0){
			alertMessage("#fileSelectMsg");
			}else{
				deleteMultipleFileIds();
		}
	});
	
	$j().ready(function(){
		$j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.GLOBALSIGH);
		$j("#currentMenuPage").val(Constants.SUBMENU_ITEMS_GLOBALSIGHT.EXPORT);
		
		if($j("#adminHeaderFlag").val() == "true"){
			$j("#adminHeader").show();
			$j('#adminGlobalsight').children("img").show();
		}
	    if($j("#userHeaderFlag").val() == "true"){
	    	$j("#userHeader").show();
	    	$j('#termList').children("img").show();
			
		}
		$j('#adminGlobalsight').css('color','#0DA7D5');

		startWaiting(" &nbsp; Loading ... please wait", $j('#exportDtlRowsList'), false);
		
		 if(/chrom(e|ium)/.test(navigator.userAgent.toLowerCase())){
	           	//$j(".exportBtns").css("margin-left","388px");
	        	
	        }
		 
		 if($j.browser.version=="7.0"){
			 	//$j(".exportBtns").css("margin-left","375px");
				$j("#exprtFile").css("padding","2px 4px");
				$j("#deleteMultipleFileIds").css("padding","2px 4px");
			}
			if($j.browser.version=="9.0" ||  $j.browser.version=="8.0"){
				//$j(".exportBtns").css("margin-left","392px");
			}

        $j("#exportTbl").termDetails();
        triggerTermDetails(null, null, 0);
        if($j.browser.version=="7.0"  || $j.browser.version=="9.0" ||  $j.browser.version=="8.0"){
        	$j(".paddingseven").css("padding-left","11px");
        	$j("#selectAllFileIds").css("margin-top","-2px");
        	$j("#selectAllFileIds").css("margin-left","-16px");
        	       
		}
        
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
                    ChartRender.twoDLineChart("myChartId18", "240", "70", "userChart", dataXML);
                }else{
                    $j("#myChartId18").hide();
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
        
    	
     	/*$t.getimportedFileList({
			success:function(data){
				displayFileList(data,'#termDtlRowsList');
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		*/
});     
	
})(window, jQuery);