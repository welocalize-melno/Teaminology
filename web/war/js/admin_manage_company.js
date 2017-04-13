(function(window, $j) {
	var  companyValidate;
	var companyListLimit = 10;
	var modifyCompanyValidate;
	var totalCompanies=0;
	var companyId=0;
	$j.fn.companyDetails = function() {
	    
	    $j(".floatrightLogOut, .floatrightLogOutForOverview").css("left", "330px");

		var ctx = $j(this);

			var companyListTmpl = ['<div class="rowBg"><div class="chkBx"><input type="checkbox" class="floatleft case" value="test" /></div> <div class="row" companyId="',
					   				'',
					   				'" id="rowid_',
					   				'',
					   				'"><div class="width200 bigFont toppadding5 companyName">',
					   				'',
					   				'</div><div class="width210 toppadding5 emailId">',
					   				'',
					   				'</div><div class="width110 toppadding5 poc">',
					   				'',
					   				'</div><div class="width90 toppadding5 modify modifyCompany nodisplay"><img class="headerMenuLink modifyImg "  height="20px" width="20px" src="'+$j("#contextRootPath").val()+'/images/Pencil.png" id="',
					   				'',
					   			    '" /></div><div class="width90 toppadding5 delete deleteCompany nodisplay"><img class="headerMenuLink deleteImg"  height="20px" width="20px" src="'+ $j("#contextRootPath").val()+'/images/DeleteIcon.png" id="',
					   				'',
					   			    '" /></div></div></div><div class="clear"></div>'
					   				];
		
		
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
			
		
			
		
		var displayCompanyDetails = function(data) {
			var companyDetailsLst = data;
			var length = (companyDetailsLst.length >= companyListLimit)?companyListLimit:companyDetailsLst.length;
			for ( var count = 0; count < length; count++) {
				var companyListTmplClone = companyListTmpl;
				companyListTmplClone[1] = companyDetailsLst[count].companyId;
				companyListTmplClone[3] = companyDetailsLst[count].companyId;
				companyListTmplClone[5] = companyDetailsLst[count].companyName;
				companyListTmplClone[7] = companyDetailsLst[count].emailId;
				companyListTmplClone[9] = companyDetailsLst[count].poc;
				companyListTmplClone[11] = companyDetailsLst[count].companyId;
				companyListTmplClone[13] = companyDetailsLst[count].companyId;
				
				$j('#companyRowsList').append(companyListTmplClone.join(""));
			}
		};
		ctx.bind("showCompanyDetails", function(event, criteria) {
			var pageNum = criteria.pageNum ;
			$t.getCompanyDetailsList( criteria.colName, criteria.sortOrder, criteria.pageNum ,{
				success:function(data){
					$j('#companyRowsList').empty();
					var companyDetails = data[0];
					var startLimit = 0;
					var endLimit = 0;
					if(companyDetails == null || companyDetails ==""){
						totalCompanies = 0;
						noOfPages = 0;
						$j("#pagination").hide();
						$j('#companyRowsList').html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">No company(s) to display</span>' );
					}else{
						var length = (data[1] >= companyListLimit) ? companyListLimit : data[1];
						
						if(pageNum == 0){
							totalCompanies = data[1];
						}
						
						noOfPages = Math.round(totalCompanies/10) ;
						noOfPages = (totalCompanies % 10 < 5 && totalCompanies%10 > 0)? noOfPages+1 : noOfPages;
						
						if(pageNum == 0){
							startLimit = 1;
							endLimit = (companyListLimit > totalCompanies)? totalCompanies : companyListLimit;
						}else{
							startLimit = ((pageNum - 1)* companyListLimit)+1;
							var tempLimit  = (pageNum) * companyListLimit;
							endLimit =(parseInt(tempLimit) > parseInt(totalCompanies))? totalCompanies :tempLimit ;
						}
						
						displayCompanyDetails(companyDetails);
						  showRolePrivileges();
						$j("#pagination").show();
					}
					$j("#rangeOfList").html(startLimit + "-"+ endLimit);
					var totalCompanies3 = insertCommmas(new String(totalCompanies));
					 $j("#totalCompanies").html(totalCompanies3);		
					pagination(noOfPages, pageNum);
					
					$j('div.pollTerm').each(function(i){
						var origText = $j(this).text();
						if(origText.length > 11){
						var finalText = origText.substring(0,9);
						finalText=finalText+"...";
						$j(this).text(finalText);
						$j(this).attr("title",origText); 
						}
						
					});
					 $j("#selectAll").click(function (event) {
						  event.stopPropagation(); 
					        $j('.case').attr('checked', this.checked);
					       
				    });
					 

						

					 $j('#companyRowsList input[type="checkbox"]').click(function(){
					     var countcheck = $j('#companyRowsList input[type="checkbox"]:checked').length;
					     if(countcheck != length) {
					    	 $j("#selectAll").attr("checked",false);
					     }else{
					    	 $j("#selectAll").attr("checked",true);
					     }
					 });
					 $j(".rowBg .row .deleteImg").hover(function(){
				    	 modalRender.bubble(".rowBg .row .deleteImg","Delete","left center","right center");
				   	});
				       
				        
				        $j(".rowBg .row .modifyImg").hover(function(){
					    	 modalRender.bubble(".rowBg .row .modifyImg","Edit","left center","right center");
					   	});
				        $j(".rowBg .row .modifyImg").click(function(){
				        	
				  		  $j(".editDetails").html("");
						   var userCompany="";
						  companyId = $j(this).attr("id");
						    $t.getCompanyList({
										success:function(data){
											var companyData = {};
											for(var i=0;i<data.length;i++){
									   		  if(data[i]!=null){
									   			  if(companyId == data[i].companyId){
									   				companyData.companyName = data[i].companyName;
									   				companyData.poc = data[i].poc;
									   				companyData.emailId = data[i].emailId;
								   		    	
									   			  }
									   		}
									   	}
											 displayModifyCompanyScreen(companyData);
											  showModifyCompany();
											  $j('#Action').val(0);
										},
										error: function(xhr, textStatus, errorThrown){
											console.log(xhr.responseText);
											if(Boolean(xhr.responseText.message)){
												console.log(xhr.responseText.message);
											}
										}
									});
				    	});
				        
				        $j(".rowBg .row .deleteImg").click(function(){
				        	 var  companyId = $j(this).attr("id");
				        	 if($j("#companyRowsList input:checked").length == 0 || $j("#companyRowsList input:checked").length == 1){
				        		  	     deleteVal(companyId);
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
		   $j('#selectAll').attr('checked', false);
		if($j(".next").hasClass("nextEnable")){
			var colName = savedCriteria.colName;
			var sortOrder = savedCriteria.sortOrder;
			var pageNum = (savedCriteria.pageNum == 0)? (savedCriteria.pageNum +2):(savedCriteria.pageNum+ 1);
			savedCriteria.pageNum = pageNum;
			triggerCompanyDetails(colName, sortOrder, pageNum);
		};
});
	
	$j(".previous").click(function(){
		   $j('#selectAll').attr('checked', false);
	if($j(".previous").hasClass("prevEnable")){
			var colName = savedCriteria.colName;
			var sortOrder = savedCriteria.sortOrder;
			var pageNum = savedCriteria.pageNum -1;
			triggerCompanyDetails(colName, sortOrder, pageNum);
	}
		
	});

		$j("#companyListSectionHead div").click(function(){
		var sortOrder = $j(this).attr('sortOrder');
		var colName =  $j(this).attr('id');
		if((colName == "column5")||(colName == "column0")){
			return;
		}
		if(!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))){
			$j("#companyListSectionHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder','ASC');
			});
				$j(this).attr('sortOrder', 'DESC');
				$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
				$j(this).addClass("ascending");
			}else if($j(this).hasClass("ascending")){
			  	$j("#companyListSectionHead div").each(function(index) {
			  		$j(this).removeClass("ascending descending");
			  		$j(this).find('.sort').remove();
			  		$j(this).attr('sortOrder','ASC');
			  	});
			  	$j(this).attr('sortOrder', 'ASC');
			  	$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
			  	$j(this).addClass("descending");
			}else if($j(this).hasClass("descending")){
					$j("#companyListSectionHead div").each(function(index) {
					$j(this).removeClass("ascending descending");
					$j(this).find('.sort').remove();
					$j(this).attr('sortOrder','ASC');
				});
			$j(this).attr('sortOrder', 'DESC');
			$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
			$j(this).addClass("ascending");
			}
		triggerCompanyDetails(colName, sortOrder, 1);
		
		});
	
		
	var triggerCompanyDetails = function( colName, sortOrder, pageNum){
		if(colName == null){
			$j("#companyListSectionHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder','ASC');
			});
		}
			$j('#companyRowsList').empty();
			ChartRender.destroyTwoDPieChart();
			var criteria = {
					'colName':colName,
					'sortOrder':sortOrder,
					'pageNum': pageNum
			};
			savedCriteria = criteria;
			$j("#manageCompanyTbl").trigger("showCompanyDetails", criteria);
	};
	
	var modifyCompanyTmpl = ['<form id="modifyComForm"><div class="boxWidth320"><div style="padding-bottom: 20px;"><label for="mName" class="error floatright nodisplay" style="width:212px;margin-right:-7px;margin-top:-16px;" generated="true"></label><div class="error floatright nodisplay UpdateCompanyExistsFlag" style="width:195px;margin-right:10px;margin-top:-31px;"></div><div class="clear"></div><div class="label width110 floatleft">Name<span class="redTxt">*</span>:</div> <div class="floatleft" style="width:200px;"><input type="text" id="mName" size="25" class="text190" name="mName"  tabindex="1" value="',
	                      '',
	                      '" /> </div></div><br class="clear" /><div style="padding-bottom: 20px;"><div class="label width110 floatleft">Poc:</div><div class="floatleft" style="width:200px;"><input type="text"  id="mPoc" size="25" class="text190" name="mPoc" tabindex="3"  value="',
	                      '',
	                      '"/></div> </div><br class="clear" /><div style="padding-bottom: 20px;"><div class="label width110 floatleft">Email: </div><div class="floatleft" style="width:200px;"> <input type="text" id="mEmail"  size="25" class="text190" name="memail" tabindex="4" value="',
	                      '',
	                      '" /></div></div></div><div class="clear"></div></form> '
	                      ];
	
	var displayModifyCompanyScreen = function(data){
		var compamyDetailsList = data;
		var modifyCompanyTmplClone = modifyCompanyTmpl;
		modifyCompanyTmplClone[1] = compamyDetailsList.companyName;
		modifyCompanyTmplClone[3] = compamyDetailsList.poc;
		modifyCompanyTmplClone[5] = compamyDetailsList.emailId;
		$j(".editDetails").append(modifyCompanyTmplClone.join(""));
		
		$j("#mName").focus(function(){
			 $j(".UpdateCompanyExistsFlag").hide();
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
	
	$j("#companyNameId").focus(function(){
		 $j(".companyExistsFlag").hide();
	});
	
	  var clearAddForm = function(){
		  $j("#companyForm input.error").removeClass("error");
		  $j("#companyForm :text").val('');
			 companyValidate.resetForm();
			 $j("#companyForm .ui-state-default").css("border","1px solid #dddddd");
			 $j('#Action').val(0);
			 $j( "#addCompany:ui-dialog" ).dialog( "destroy" );
	  }
	  
	  var clearModifyForm = function(){
		  $j("#modifyForm input.error").removeClass("error");
		  $j("#modifyForm :text").val('');
		      $j("#modifyForm .ui-state-default").css("border","1px solid #dddddd");
			 $j('#Action').val(0);
			 $j( "#modifyForm:ui-dialog" ).dialog( "destroy" );
	  }
	  
	var showDialog = function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		$j( "#addCompany:ui-dialog" ).dialog( "destroy" );
		
	
		$j( "#addCompany" ).dialog({
			height: 255,
			width:420,
			modal: true,
			close: function(event, ui) { 
				
				clearAddForm();
			  
			}
			
		});
	};
	$j('#email').blur(function(evt) {
		$j(".addNewCompany").focus();	
	});
	$j('#addCompany').keyup(function(evt) {
		if(evt.keyCode === 13 /* ENTER Key */) {
			if($j("#companyForm").valid()){
				addNewCompany();
			}
		} 				
	});
	$j(".addNewCompany").click(function(){
		 if($j("#companyForm").valid()){
        	     	  addNewCompany();
         }
	 });
	 $j("#submitModCompany").click(function(){
		 var companymodifyValidate= $j("#modifyComForm").validate({
	    		rules: {
	    			mName: {
	    			required: true
	    			}
	            },
	            
	    		messages: {
	    			mName: {
	    			required: "Company name is required"
	    			}
	    		}
	    	});
	 	  if($j("#modifyComForm").valid()){
			 updateCompany();
		 }
        
	 });
	 
		$j("#cancelAddCompany").click(function(){
			$j("#companyForm :text").val('');
			$j('#addCompany').dialog('close');
			 $j('#Action').val(0);
		});
		
		$j("#cancelModCompany").click(function(){
			$j("#modifyForm :text").val('');
			$j('#modifyCompany').dialog('close');
			 $j('#Action').val(0);
		});
		
		
		
		var showModifyCompany = function() {
			// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
			$j( "#modifyCompany:ui-dialog" ).dialog( "destroy" );
		
			$j( "#modifyCompany" ).dialog({
				height: 255,
				width:420,
				modal: true,
				close: function(event, ui) {
					clearModifyForm();
				}
				
			});
		};
		var addNewCompany = function(){
			var date = new Date();
	        var curr_date = date.getDate();
	        var curr_month = date.getMonth();
	        curr_month = curr_month + 1;
	        var curr_year = date.getFullYear();
	        date= curr_date + '/'+ curr_month + '/'+ curr_year;
	        var company = new Object();
		    company.companyId=null;
		    company.companyName=$j("#companyNameId").val();
		    company.poc=$j("#contact").val();
		    company.emailId=$j("#email").val();
	           var companyObject=Object.toJSON(company);
	    	    	 $t.addNewCompany(companyObject, {
					 success : function(data) {
							 if(data.indexOf("company exists") != -1){
								 $j("#companyNameId").addClass("error");
								$j(".companyExistsFlag").show();
								  $j(".companyExistsFlag").html("company already exists, please choose other company");
									
							 }else{
								 $j(".companyExistsFlag").hide(); 
							 }
							 
						 if(data.indexOf("company exists") == -1){
							 alertMessage("#companyAddSuccess");
							 $j('#addCompany').dialog('close');
	    		 			$j("#companyNameId").val('');
	    	    			$j("#contact").val('');
	    	    			$j("#email").val('');
	    	    			triggerCompanyDetails(savedCriteria.colName,savedCriteria.sortOrder,savedCriteria.pageNum);
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
		
		var updateCompany = function(){
			var date = new Date();
	        var curr_date = date.getDate();
	        var curr_month = date.getMonth();
	        curr_month = curr_month + 1;
	        var curr_year = date.getFullYear();
	        date= curr_date + '/'+ curr_month + '/'+ curr_year;
            var company = new Object();
		    company.companyId=companyId;
		     company.companyName=$j("#mName").val();
		    company.poc=$j("#mPoc").val();
		    company.emailId=$j("#mEmail").val();

	           var companyObject=Object.toJSON(company);
	    	    	 $t.updateCompany(companyObject, {
					 success : function(data) {
						 	 if(data.indexOf("updateCompany exists") != -1){
								 $j("#mName").addClass("error");
								$j(".UpdateCompanyExistsFlag").show();
								  $j(".UpdateCompanyExistsFlag").html("company already exists, please choose other company");
									
							 }						 
						 if(data.indexOf("updateCompany exists") == -1){
	    		 			 triggerCompanyDetails(savedCriteria.colName,savedCriteria.sortOrder,savedCriteria.pageNum);
	    	    			alertMessage("#companyUpdateSuccess");
	    	    			 $j('#modifyCompany').dialog('close');
	 	    			
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
		
		var deleteCompanyDetails = function(selectedCompanyIds){
		 	var companyIdsArray = selectedCompanyIds.split(',');
		$t.deleteCompanies(companyIdsArray, {
			success : function(data) {
			$t.getActiveCompanyCountDetails({
					success:function(data){
						if(data != null){
							var totalCompanies1 = new String( data);
							var totalCompanies2 = insertCommmas(totalCompanies1);
//							$j("#totalUsers").html(totalCompanies2);
							totalCompanies = totalCompanies -  companyIdsArray.length;
							var pageNum1 = savedCriteria.pageNum;
							  var totalCompaniesTillBefore = (pageNum1-1) * companyListLimit;
							  if(totalCompaniesTillBefore == totalCompanies){
								  savedCriteria.pageNum = pageNum1-1;
							  }
							   $j('#selectAll').attr('checked', false);
							  triggerCompanyDetails(savedCriteria.colName,savedCriteria.sortOrder,savedCriteria.pageNum);
							  alertMessage("#deleteCompanies");
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
						}
					  
					},
					error: function(xhr, textStatus, errorThrown){
						console.log(xhr.responseText);
						if(Boolean(xhr.responseText.message)){
							console.log(xhr.responseText.message);
						}
					}
				});
                    
			},
			error : function(xhr, textStatus, errorThrown) {
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
      };
		
		var deleteVal = function(id){
			
			$j( "#delete_cnfm:ui-dialog" ).dialog( "destroy" );
			$j( "#delete_cnfm" ).dialog({
				resizable: false,
				height:150,
				width:400,
				modal: true,
				buttons: {
					"Delete": function() {
						 $j("#user").hide();
						  var separator = ",";
//						  selectedCompanyIds="";
//						  $j("#manageCompanyTbl input:checked").each(function(i) {
						  selectedCompanyIds=id;	  
//	    				  selectedCompanyIds=selectedCompanyIds.substr(0,selectedCompanyIds.lastIndexOf(separator));
						  deleteCompanyDetails(selectedCompanyIds);
		  
						 					
						$j(this).dialog( "close" );
						 $j('#Action').val(0);
						 
					},
					Cancel: function() {
						 $j('#Action').val(0);
						$j( this ).dialog( "close" );
					}
				}
			});
		};
		var deleteMultiCompanies = function(){
		$j( "#delete_cnfm:ui-dialog" ).dialog( "destroy" );
		$j( "#delete_cnfm" ).dialog({
			resizable: false,
			height:150,
			width:400,
			modal: true,
			buttons: {
				"Delete": function() {
					 $j("#user").hide();
					  var separator = ",";
					  selectedCompanyIds="";
					  $j("#companyRowsList input:checked").each(function(i) {
						  selectedCompanyIds +=$j(this).parent().next().attr("companyid")+separator;
										 
							 $j(this).parent().parent().remove();
							 var viewNode = $j(this).parent().parent().next().next();
							  viewNode.remove();
							  $j(this).parent().parent().remove();
							  $j(this).parent().parent().remove();
						     });
					  
					  selectedCompanyIds=selectedCompanyIds.substr(0,selectedCompanyIds.lastIndexOf(separator));
					  deleteCompanyDetails(selectedCompanyIds);
	  
					 					
					$j(this).dialog( "close" );
					 $j('#Action').val(0);
					 
				},
				Cancel: function() {
					 $j('#Action').val(0);
					$j( this ).dialog( "close" );
				}
			}
		});
		};

		$j("#addCompanyBtn").click(function(){
//			var userRole = "";
			showDialog();
		});
		$j("#deleteMultipleCompanies").click(function(){
			if($j("#companyRowsList input:checked").length == 0){
				alertMessage("#companySlctMessage");
			}else{
				deleteMultiCompanies();
			}
		
		});
		
		$j().ready(function(){
	        showRolePrivileges();
			$j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.ADMIN);
			$j("#currentMenuPage").val(Constants.SUBMENU_ITEMS.MANAGE_COMPANY);
		
/*			if($j("#adminHeaderFlag").val() == "true"){
			$j("#adminHeader").show();
			}
			if($j("#userHeaderFlag").val() == "true"){
        	$j("#userHeader").show();
			
			}
			
			
			
		
       $j('#admin').addClass("on");*/
			if( $j.browser.version=="7.0" || $j.browser.version=="9.0" ||  $j.browser.version=="8.0"){
				$j("#selectAll").css("margin-top","-2px");
				$j("#selectAll").css("margin-left","-4px");
				
			}
			if( $j.browser.version=="7.0"){
				$j(".loginBtn").css("padding-right","3px");
				$j(".editBtn").css("margin-left","110px");
				$j(".editBtn").css("margin-top","-12px");
			}	
		
		$j(".subMenuLinks a").last().css("border-right", "none");
		
		$j("#manageCompanyTbl").companyDetails();
		triggerCompanyDetails(null,null,0);
        
        
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
				var array = [];
				var totalUsers = 0;
				
				if(totalUsersPerMonth != null && totalUsersPerMonth!=""){
					
					for(var count=0;count<totalUsersPerMonth.length;count++){
						totalUsers = totalUsersPerMonth[4].count;
						array.push(totalUsersPerMonth[count].count);
					}

					if(array[0]==totalUsers){
						array.unshift(0);
					}
					
				if ($j("#hpsite").val() == "true")
					dataXML.push("<chart showValues='0' adjustDiv='0' showBorder='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showBorder='0' bgColor='#f9f9f9,#f9f9f9' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totalUsers+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#f9f9f9' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='2' lineColor='#0e94bc' canvasBorderThickness='0'>");
				else
					dataXML.push("<chart showValues='0' adjustDiv='0' showBorder='0' bgColor='#032D39,#032D39' bgAlpha='100,100' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totalUsers+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#ffffff' divLineThickness='2' labelPadding='0'  baseFontColor='#C0C0C0' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
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
        companyValidate= $j("#companyForm").validate({
    		rules: {
    			companyNameId: {
    			required: true
    			}
            },
            
    		messages: {
    			companyNameId: {
    			required: "Company name is required"
    			}
    		}
    	});
        
        
});     
	
})(window, jQuery);
