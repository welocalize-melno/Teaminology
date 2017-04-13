(function(window, $j) {
	var pieChartHeight = 40;
	var pieChartWidth = 40;
	var values = 0;
	var validateDetails;
	var validatePwd;
	var fileName = null;
	var termListLimit = 10;
	var selectedTermIds = "";
	var noOfPages =0;
	var totalTerms = 0;
	var totalResultedTerms = 0;
	var savedCriteria = null;
	var termListLimit = 10;
	var compSelectedVal = [];	
	var multiLValues=null;
	var multiTaskValues=null;
	var url = $j(location).attr('href');

	if(url.indexOf("/user_tms.jsp") != -1){
	    $j('#about').removeClass('aboutForDashboard');
	    $j('#about').addClass('aboutForDashboardMargin');    	    
	}
	
	$j.fn.tmDetails = function() {

		var ctx = $j(this);

		var tmDtlTmpl = ['<div class="rowBg" style="font-size:11px;"> <div  class="row twistie_close" termId="',
			   				'',
			   				'" id="rowid_',
			   				'',
			   				'"><div class="width170 bigFont  toppadding5 sourceTm">',
			   				'',
			   				'</div><div class="width170 bigFont toppadding5 targetTm">',
			   				'',
			   				'</div><div class="width110 toppadding5 tmLanguage smallFont">',
			   				'',
			   				'</div><div class="width110  toppadding5 productLine">',
			   				'',
			   				'</div><div class="width110  toppadding5 industryDomain">',
			   				'',
			   				'</div><div class="width110 viewCompany nodisplay toppadding5 company">',
			   				'',
			   				'</div><div class="width90  toppadding5 contentType">',
			   				'',
			   				'</div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay" style="min-height:170px;" id="overView" termId="',
			   				'',
			   				'"><div class="loadingDiv" style="text-align: center;"><img src='+$j("#contextRootPath").val()+'/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
			   				];

		var tmAttrTmpl = '<div class="termAttr"><div><div><div class="bold sourceTerm label" >Source: </div> <div class="sourceDesc viewDetailsFld" style=" word-wrap: break-word;"></div><div  name="editSourceTerm"  id=#{sourceId} class="sourceDescEdit editDetailsFld nodisplay "  style=" word-wrap: break-word;"></div><div class="clear"></div>'
			+ '</div><div class="topmargin5"><div class=" bold targetTerm label" >Target: </div> <div class="targetDesc viewDetailsFld" style=" word-wrap: break-word;"></div><textarea rows="3" cols="44"   name="editTargetTerm"  id=#{targetId} class="targetDescEdit editDetailsFld nodisplay editTargetTerm"  value="" style=" word-wrap: break-word;"></textarea><div class="clear"></div>'
			+ '</div></div><div class="left"><div class="topmargin5"><div class="bold label">Product Line: </div><span class="productDesc viewDetailsFld"></span><select name="editProductLine" id=#{productId} class="editDetailsFld  productDescEdit nodisplay   editProductLine"><option value="">---Select---</option></select><div class="clear"></div>'
			+ '</div><div class="topmargin5" ><div class="bold industry label" >Industry Domain: </div> <span class="domainDesc viewDetailsFld"></span><select name="editDomain" id=#{domainId} class="editDetailsFld nodisplay editDomain domainDescEdit"><option value="">---Select---</option></select><div class="clear"></div>'
			+ '</div><div class="topmargin5"><div class="bold label">Content Type: </div><span class="contentDesc viewDetailsFld"></span><select name="editContent" id=#{contentId} class="editDetailsFld  contentDescEdit nodisplay editContent"><option value="">---Select---</option></select><div class="clear"></div></div></div>';

		 var companyTmpl = ['<option value="',
		                    '',
		                    '" >',
		                    '',
		                    '</option>'
		                    ];

		var classNames = ['purpleBar','greenBar','yellowBar','pinkBar'];
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
		
		var displayTmDetails = function(data) {
			stopWaiting($j('#tmDtlRowsList'));
			
			var tmDetails = data;
			
			
				var listLength = (tmDetails.length >= termListLimit)?termListLimit:tmDetails.length;
		    	for ( var count = 0; count < listLength; count++) {
				var tmDtlTmplClone = tmDtlTmpl;
				var sourceTerm = (tmDetails[count].sourceTerm == null || tmDetails[count].sourceTerm == "")?"&nbsp;":tmDetails[count].sourceTerm;
				var targetTerm = (tmDetails[count].targetTerm == null || tmDetails[count].targetTerm == "")?"&nbsp;":tmDetails[count].targetTerm;
				var tmLanguage = (tmDetails[count].targetLang == null || tmDetails[count].targetLang == "")?"&nbsp;":tmDetails[count].targetLang;
				var company = (tmDetails[count].company == null || tmDetails[count].company == "")?"&nbsp;":tmDetails[count].company;
			    var IndustryDomain=(tmDetails[count].domain == null || tmDetails[count].domain == "")?"&nbsp;":tmDetails[count].domain;
				var productLine=(tmDetails[count].productLine == null || tmDetails[count].productLine == "")?"&nbsp;":tmDetails[count].productLine;
				var contentType=(tmDetails[count].contentType == null || tmDetails[count].contentType == "")?"&nbsp;":tmDetails[count].contentType;
				
				
				tmDtlTmplClone[1] = tmDetails[count].tmProfileInfoId;
				tmDtlTmplClone[3] = tmDetails[count].tmProfileInfoId;
				tmDtlTmplClone[5] = sourceTerm;
				tmDtlTmplClone[7] = targetTerm
				tmDtlTmplClone[9] = tmDetails[count].targetLang;
				tmDtlTmplClone[11] = company;
				tmDtlTmplClone[13] = productLine;
				tmDtlTmplClone[15] = IndustryDomain;
				tmDtlTmplClone[17] = contentType;
				tmDtlTmplClone[19] = tmDetails[count].tmProfileInfoId;
//				tmDtlTmplClone[19] = tmDetails[count].tmProfileInfoId;
//				tmDtlTmplClone[21] = tmDetails[count].tmProfileInfoId;
				$j('#tmDtlRowsList').append(tmDtlTmplClone.join(""));
			
				
			}
		    	showRolePrivileges();
		    	 $j("#tmDtlRowsList input:checkbox").each(function(i) {
		    		 if(selectedTermIds!=""){
		    		 var temptermIdsArray = selectedTermIds.split(",");
		    		 for(var j=0;j<temptermIdsArray.length;j++){
		    			 if(temptermIdsArray[j]==($j(this).parent().next().attr("termId"))){
		    				 $j(this).attr('checked', true);
		    			 }
		    		 }
		    		 }
					 });
		    	 $j('#tmDtlRowsList input:checkbox').click(function() {
		 			if($j(this).attr('checked')!="checked"){
		 				var tempIds="";
		 				var separator="";
		 				if(selectedTermIds!=""){
		 					var temptermIdsArray = selectedTermIds.split(",");
		 					for(var j=0;j<temptermIdsArray.length;j++){
		 						if(temptermIdsArray[j]!=($j(this).parent().next().attr("termId"))){
		 							tempIds=separator+temptermIdsArray[j];
		 							separator=",";
		 						}
		 					}
		 				}
		 				selectedTermIds=tempIds;
		 			}
		 		});
		};
		
		 var  isDate = function(txtDate) {
			  var currVal = txtDate;
			  if(currVal == '')
				  return false;

			  //Declare Regex 
			  var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/;
			  var dtArray = currVal.match(rxDatePattern); // is format OK?

			  if (dtArray == null)
				  return false;

			  //Checks for mm/dd/yyyy format.
			  dtMonth = dtArray[1];
			  dtDay= dtArray[3];
			  dtYear = dtArray[5];

			  if (dtMonth < 1 || dtMonth > 12)
				  return false;
			  else if (dtDay < 1 || dtDay> 31)
				  return false;
			  else if ((dtMonth==4 || dtMonth==6 || dtMonth==9 || dtMonth==11) && dtDay ==31)
				  return false;
			  else if (dtMonth == 2)
			  {
				  var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
				  if (dtDay> 29 || (dtDay ==29 && !isleap))
					  return false;
			  }
			  return true;
		  };
		  
		  
			var bindEvents = function(data){
				var classNamesClone = classNames.slice(0);
				$j('#tmDtlRowsList .row ').click(function(){
					var row = $j(this);
					if($j(this).hasClass('twistie_close')){
						$j(this).parent().next().next().show();
						$j(this).removeClass("twistie_close");
						$j(this).addClass("twistie_open");
						 var searchBy = $j("#newTmTerm").val();
						 searchBy=$j.trim(searchBy);
						   searchBy=searchBy.replace(/[&\/\\#,|+()$~%.'":*?<>{}]/g,'');
						  var  searchText=searchBy.replace(/\s\s+/g, ' ');
						  searchText=$j.trim(searchText);
						  var  caseFlag=false;
							 if($j("#chktmCase").attr('checked')){
								 caseFlag = true;
							 }
						var tmProfileInfoId=$j(this).attr("termId");
						$t.getTmAttributes(tmProfileInfoId,{
							success:function(data){
								if(Boolean(data)){
									var tmInfo = data;
									var defaultImg=$j("#contextRootPath").val()+"/images/default.jpg";
									var detailsSec = row.parent().siblings('.viewDtlsRow[termId='+row.attr('termId')+'] ');
									var tmAttrIds={
											sourceId:'sourceId_'+tmProfileInfoId,
											targetId:'targetId_'+tmProfileInfoId,
											productId:'productId_'+tmProfileInfoId,
											 domainId:'domainId_'+tmProfileInfoId,
											 contentId:'contentId_'+tmProfileInfoId
											  
									}
									
									var editTmTmpl=new Template(tmAttrTmpl).evaluate(tmAttrIds);
									detailsSec.html(editTmTmpl);
							
									
									if(tmInfo != null){
										var tmDomain =  (tmInfo.domain == null || tmInfo.domain.domain == null)?"":tmInfo.domain.domain;
									    var tmContent = (tmInfo.contentType == null || tmInfo.contentType.contentType == null)?"":tmInfo.contentType.contentType;
									    var tmProductLine = (tmInfo.productGroup == null|| tmInfo.productGroup.product == null)?"":tmInfo.productGroup.product;
									  var source=(tmInfo.source == null|| tmInfo.source == null)?"":tmInfo.source;
									  var target=(tmInfo.target == null|| tmInfo.target == null)?"":tmInfo.target;
									detailsSec.find('.sourceDesc').html(source);
									detailsSec.find('.sourceDesc').each(function(i){
										if(searchBy!=null && searchBy!= "Enter term to search..."){
											 var  sourceTermText=$j(this).html();
											 sourceTermText=Util.backgroundYellow(sourceTermText,searchText,null,caseFlag);
										       $j(this).html(sourceTermText);
										}
									});
									detailsSec.find('.sourceDescEdit').html(source);
//									detailsSec.find('.targetDesc').html(Util.wordWrap(100,"<br/>",false,target));
									detailsSec.find('.targetDesc').html(target);
									detailsSec.find('.targetDesc').each(function(i){
										if(searchBy!=null && searchBy!= "Enter term to search..."){
											  var  targetTermText=$j(this).html();
											  targetTermText=Util.backgroundYellow(targetTermText,searchText,null,caseFlag);
										          $j(this).html(targetTermText);	
										}
									});
									detailsSec.find('.targetDescEdit').html(target);
									detailsSec.find('.contentDesc').html(tmContent);
									detailsSec.find('.domainDesc').html(tmDomain);
									detailsSec.find('.productDesc').html(tmProductLine);
									
									}
//									
//									detailsSec.append(newTermSec.join(""));
//									
									
//									if(data.deprecatedTermInfo.length>0){
//								       var	finalSource="";
//								        var finalTarget="";
//								        for(var i=0;i<data.deprecatedTermInfo.length;i++){
//								        	if(data.deprecatedTermInfo[i].deprecatedSource!=null &&  $j.trim(data.deprecatedTermInfo[i].deprecatedSource)!="")
//								        		finalSource=finalSource+data.deprecatedTermInfo[i].deprecatedSource+"," ;
//								        	if(data.deprecatedTermInfo[i].deprecatedTarget!=null &&  $j.trim(data.deprecatedTermInfo[i].deprecatedTarget)!="")
//								        		finalTarget=finalTarget+data.deprecatedTermInfo[i].deprecatedTarget+",";
//								        }
//								      
//							        finalSource=  finalSource.substring(0,finalSource.lastIndexOf(","));
//							        finalTarget=  finalTarget.substring(0,finalTarget.lastIndexOf(","));
	//
//						     	deprecatedTermInfoSec[1]=finalSource;
//								deprecatedTermInfoSec[3]=finalTarget;
//								detailsSec.append(deprecatedTermInfoSecClone.join(""));
//									}
								
//								detailsSec.append(editDetailsSec);
									
									
									if($j("body").hasClass('adminOvr')){
										detailsSec.find('.votesBtn').hide();
										detailsSec.find('.commentsFld').hide();
										detailsSec.find('.newTermFld').hide();
										detailsSec.find('input:radio').hide();
											}
									
									/*Finding highest no. of votes*/
									var largest = {
										    val: null
										};
									for(var i in data.suggestedTermDetails){
										if( data.suggestedTermDetails[i].noOfVotes==0){
											
										}
									    if( data.suggestedTermDetails[i].noOfVotes>largest.val ){
									        largest.val = data.suggestedTermDetails[i].noOfVotes;
									        
									    }
									}
									
									
									//find highest
									var blockSize = 240/largest.val;
									
									detailsSec.find('.votesBar').each(function(i){
										$j(this).addClass(classNamesClone[classNamesClone.length-1]);
										classNamesClone.pop();
										if(classNamesClone.length == 0)
											classNamesClone = classNames.slice(0);
										if( data.suggestedTermDetails[i].noOfVotes==0){
											$j(this).width("0");
											$j(this).css("margin-right","10px");
										}
										else{
										  $j(this).width(blockSize * data.suggestedTermDetails[i].noOfVotes);
										  
										  /*Displaying qTip on mouseover*/
										  $j(this).hover(function(){
										  modalRender.bubble("#barId_"+row.attr('termId')+i,data.suggestedTermDetails[i].votersNames,"left center","right center");
											  });
										}
										  	 
									});
									
									 
									detailsSec.find('.suggTxt').focus(function(){
										$j(this).val('');
									});
									detailsSec.find('.suggTxt').blur(function(){
										if($j(this).val() == '')
										$j(this).val('Enter new suggestion');
									});
									
								
									
							
									row.find(".editDetailsFld select").click(function(event){
										$j(this).focus();
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
						
						
					}
					else{
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
		
	
		ctx.bind("showTmDetails", function(event,criteria) {
		 startWaiting("&nbsp;Loading ... please wait", $j('#tmDtlRowsList'), false);
			 $j("#pagination").hide();
			var pageNum = criteria.pageNum;
			 var  queryAppender= new Object();
			 queryAppender.colName = criteria.colName;
			 queryAppender.sortOrder = criteria.sortOrder;
			 queryAppender.pageNum = criteria.pageNum;
			 queryAppender.searchStr = criteria.searchStr;
			 queryAppender.caseFlag = criteria.caseSensitiveFlag;
			 queryAppender.filterBy = criteria.filterBy;
			 queryAppender.selectedIds = criteria.selectedIds;
			 queryAppender.filterByCompany = criteria.filterByCompany;
	     	 queryAppender.selectedCompanyIds = criteria.selectedCompanyIds;
	          	 
			 searchStringTM= criteria.searchStr;
			  var searchStr = criteria.searchStr;
			 var filterBy = criteria.filterBy;
			 var queryAppenderParameter =  Object.toJSON(queryAppender);
			$t.getTMProfileTerms(queryAppenderParameter,{
				success:function(data){
					
					if(data!=null){
						var termDetails = data.tmProfieTermsList;
						if(termDetails == null || termDetails.size()==0){
							var displayCriteria = $j("#newTmTerm").val();
							if(displayCriteria == "Enter term to search..."){
								displayCriteria = "No data to display";
							}else{
								displayCriteria ='No matches found for term : "<span class="bold">' +  $j("#newTmTerm").val()+ '</span>"';
							}

							$j("#tmDtlRowsList").html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">'+displayCriteria+'</span>' );
							$j("#paginationTm").hide();

						}else{

							var length = (termDetails.length >= termListLimit)?termListLimit:termDetails.length;
							var startLimit = 0;
							var endLimit = 0;
//							if(pageNum == 0 ){
//								totalTerms = termDetails.length;
//							}
						
							totalTerms=	parseInt(data.totalResults);
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
							displayTmDetails(termDetails);

							for(var i = 0; i<length;i++){
								var dataXML = new Array();
								var invites = (termDetails[i].invites == null)?0:termDetails[i].invites;
								var votesPerTerm = (termDetails[i].votesPerTerm == null)?0:termDetails[i].votesPerTerm;
								if(invites == 0 && votesPerTerm ==0 ){
									$j("#dataPie_"+termDetails[i].termId).css("visibility","hidden");
									$j("#dataPie_"+termDetails[i].termId).css("width","40px");
									$j("#dataPie_"+termDetails[i].termId).removeAttr("id");
									continue;
								}
								// Start of chart object
								dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='14' showShadow='0' slicingDistance='0' animation='0' paletteColors='#C9CCCB, #00A4D6' showBorder='0' bgAlpha='30' showToolTip='0'>");
								dataXML.push("<set label='Services' value='"+(invites-votesPerTerm)+"' />");
								dataXML.push("<set label='Hardware' value='"+votesPerTerm+"' />");
								dataXML.push("</chart>");
								ChartRender.twoDPieChart(termDetails[i].termId,pieChartHeight,pieChartWidth, "dataPie_"+termDetails[i].termId, dataXML );
								modalRender.bubble("#dataPie_"+termDetails[i].termId,votesPerTerm+"/"+invites+"<br>"+"voted / invited to vote","bottom center","top center");

							}
							$j("#paginationTm").show();
							$j("#rangeOfTms").html(startLimit + "-"+ endLimit);
							var totalTerms3 = insertCommmas(new String(totalTerms));
							$j("#totalPolledTms").html(totalTerms3);
							paginationTm(noOfPages, pageNum);
							bindEvents();
							 var searchBy = $j("#newTmTerm").val();
							 searchBy=$j.trim(searchBy);
							   searchBy=searchBy.replace(/[&\/\\#,|+()$~%.'":*?<>{}]/g,'');
							 var  searchText=searchBy.replace(/\s\s+/g, ' ');
							 searchText=$j.trim(searchText);
							 var  caseFlag=false;
							 if($j("#chktmCase").attr('checked')){
								 caseFlag = true;
							 }
								$j('div.sourceTm').each(function(i){
									var origText = $j(this).text();
									if(searchBy!=null && searchBy!= "Enter term to search..."){
										if(origText.length > 16){
										var finalText = origText.substring(0,14);
										finalText=Util.backgroundYellow(finalText,searchText,null,caseFlag);
										
										finalText=finalText+"...";
										
										$j(this).html(finalText);
										$j(this).attr("title",origText);
									}else{
										
										var origText = $j(this).text();
										origText=Util.backgroundYellow(origText,searchText,null,caseFlag);
										 $j(this).html(origText);
									}
									}else{
									
										if(origText.length > 16){
											var finalText = origText.substring(0,14);
											finalText=finalText+"...";
											$j(this).html(finalText);
											$j(this).attr("title",origText); 
									}
									}
								});
								
								$j('div.targetTm').each(function(i){
									var origText = $j(this).text();
									if(searchBy!=null && searchBy!="Enter term to search..."){
									if(origText.length > 14){
										var finalText = origText.substring(0,12);
										finalText=Util.backgroundYellow(finalText,searchText,null,caseFlag);
										finalText=finalText+"...";
										
										$j(this).html(finalText);
										$j(this).attr("title",origText);
									}else{
										
										var origText = $j(this).text();
										origText=Util.backgroundYellow(origText,searchText,null,caseFlag);
										 $j(this).html(origText);
									}
									}else{
										if(origText.length > 16){
											var finalText = origText.substring(0,14);
											finalText=finalText+"...";
											$j(this).html(finalText);
											$j(this).attr("title",origText); 
										}
									}
								});
								
							$j("#selectAllTms").click(function (event) {
								event.stopPropagation(); 
								$j('.case').attr('checked', this.checked);

							});



						}

					}},
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
		
		
		var displayCompanyList= function(data,appenderClass) {
			var companyList = data;
			for ( var count = 0; count < companyList.length; count++) {
				var companyTmplClone = companyTmpl;
				companyTmplClone[1] = companyList[count].companyId;
				companyTmplClone[3] = companyList[count].companyName;
				$j(appenderClass).append(companyTmplClone.join(""));
			}
		}
		var removeEmptyString = function(arrayValues){
			var newArrayValues = [];
			if(arrayValues !=null && arrayValues.length > 0){
				for(index=0; index<arrayValues.length; index++){
					var valAtIndex = arrayValues[index];
					if(valAtIndex !=null && $j.trim(valAtIndex).length > 0){
						newArrayValues.push(valAtIndex);
					}
				}
			}
			return newArrayValues;
		}
		 var isMultiSelectModified = function(newChkValues, oldChkValues){
				var isCriteriaModified = false;
					if(newChkValues != null && oldChkValues != null){
						if(newChkValues.length != oldChkValues.length){
						isCriteriaModified = true;
					}else{
						isCriteriaModified = Util.compareArrays(newChkValues, oldChkValues);
					 	if(isCriteriaModified){
							isCriteriaModified = false;
						}else{
							isCriteriaModified = true;
						}
					}
				}
					return isCriteriaModified;
			
			}
//			var triggerTmDetails = function(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds,filterByCompany,selectedCompanyIds){
//				if(colName == null){
//					$j("#mngTrmDtlSectionHead div").each(function(index) {
//						$j(this).removeClass("ascending descending");
//						$j(this).find('.sort').remove();
//						$j(this).attr('sortOrder','ASC');
//					});
//				}
//					$j('#termDtlRowsList').empty();
//					ChartRender.destroyTwoDPieChart();
//					$t.getTotalTermsInGlossary({
//						success:function(data){
//							var totalTerms1 = new String( data);
//							var totalTerms2 = insertCommmas(totalTerms1);
//							$j("#totalTerms").html(totalTerms2);
//							$j("#termsInGlossary").val(totalTerms1);
//							var criteria = {
//									'colName':colName,
//									'sortOrder':sortOrder,
//									'pageNum': pageNum,
//									'searchStr':searchStr,
//									'caseSensitiveFlag':caseSensitiveFlag,
//									'filterBy': filterBy,
//									'selectedIds' :selectedIds,
//									'filterByCompany':filterByCompany,
//									'selectedCompanyIds':selectedCompanyIds
//							};
//							savedCriteria = criteria;
//							$j("#manageTermTbl").trigger("showTmDetails", criteria);
//						},
//						error: function(xhr, textStatus, errorThrown){
//							console.log(xhr.responseText);
//							if(Boolean(xhr.responseText.message)){
//								console.log(xhr.responseText.message);
//							}
//						}
//					});
//					
//			};
		$t.getSuperTranslatorCompanyList({
			success:function(data){
				displayCompanyList(data,"#mutliCompanySlct");
				 $j("#mutliCompanySlct").multiselect().multiselectfilter();
				
			    $j("#mutliCompanySlct").multiselect({
			      	   noneSelectedText: 'Select company',
			    	   selectedList: 4, // 0-based index
			    	   onClose:function(){
			    		    		   
			    		 multiCValues = $j("#mutliCompanySlct").val();
			    		 if(multiCValues ==  null){
			    			 multiCValues = [];
							}
				    		compSelectedVal=removeEmptyString(compSelectedVal);
							var isModified = isMultiSelectModified(multiCValues,compSelectedVal);
							if(multiCValues==""){
								multiCValues=null;
							}
						
							if(isModified){
								compSelectedVal=multiCValues;
								if(multiCValues!=null){
									 triggerTmDetails(null, null, 0, null, false, null, null,'Company',multiCValues);
							  }else{
								  triggerTmDetails(null, null, 0, null, false, null, null,null,null);
							}
							}
			    							    
			    	   }
		    		   
			    });
			   
			}
		});

	};
	
	var langSlctTmpl = ['<option value="',
	                    '',
	                    '" >',
	                    '',
	                    '</option>'
	                    ];
	
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
	var displayUserDetails = function(data) {
		$j('#userInfo').html("");
		var userDetails = data;
			var userDetailTmplClone = userDetailTmpl;
			var count=userDetails.totalVotes;
			var totalTerms1 = new String(count);
			userDetailTmplClone[1] = userDetails.photoPath;
			userDetailTmplClone[3] = userDetails.userName;
			userDetailTmplClone[5] = insertCommmas(totalTerms1);
			userDetailTmplClone[7] = userDetails.createDate;
			$j('#userInfo').append(userDetailTmplClone.join(""));
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
  
	
  var chatData=function(){
  		
  		$t.getUserAccuracyRate({
  			success:function(data){
  				var finalisedTerms = data.finalizedTerm;
  				var votedTerms = data.votedTerms;
  				var dataXML = new Array();
  				if(votedTerms==0) {
  					$j("#accuracyRate").html("0%");
  					if ($j("#hpsite").val() == "true")
  					dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#0e94bc, #D1EAF1' bgColor='#F9F9F9' showBorder='0' bgAlpha='100'>");
  					else
  	  				dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");
  					
  					dataXML.push("<set label='Approved' value='"+finalisedTerms+"' />");
  					dataXML.push("<set label='Incorrect' value='"+(votedTerms-finalisedTerms)+"' />");
  					dataXML.push("</chart>");
  					ChartRender.twoDPieChart("0",130,70, "accuracyPie", dataXML );

  				}else{	
  					
  				var accuracyRate = Math.round((finalisedTerms/votedTerms) * 100);
  				if(!(isNaN(accuracyRate))) {
  					$j("#accuracyRate").html(accuracyRate+"%")
                } else {
                    	$j("#accuracyRate").html("0%")
                   }
  				if ($j("#hpsite").val() == "true")
  				dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#0e94bc, #D1EAF1' bgColor='#032D39' showBorder='0' bgAlpha='100'>");
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
  	var paginationTm = function(noOfPages, pageNum){
		if((noOfPages > 1) && (pageNum < noOfPages)){
			$j(".tmNext").addClass("nextEnable");
			$j(".tmNext").removeClass("nextDisable");
		}else{
			$j(".tmNext").removeClass("nextEnable");
			$j(".tmNext").addClass("nextDisable");
		}
		
		if((noOfPages > 1) && (pageNum > 1)){
			$j(".tmPrevious").removeClass("prevDisable");
			$j(".tmPrevious").addClass("prevEnable");
		}else{
			$j(".tmPrevious").removeClass("prevEnable");
			$j(".tmPrevious").addClass("prevDisable");
		}
	};
	var triggerTmDetails = function(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds,filterByCompany,selectedCompanyIds){
			if(colName == null){
			$j("#mngTmDtlSectionHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder','ASC');
			});
		}
			$j('#tmDtlRowsList').empty();
			ChartRender.destroyTwoDPieChart();
			$t.getTotalTermsInTM({
				success:function(data){
					var totalTerms1 = new String( data);
					var totalTerms2 = insertCommmas(totalTerms1);
					$j("#totalTerms").html(totalTerms2);
					$j("#termsInGlossary").val(totalTerms1);
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
			
	};
	$j(".tmNext").click(function(){
		if($j(".tmNext").hasClass("nextEnable")){
			 $j("#selectAllTms").attr('checked',false);
			  $j(".case").attr('checked',false);
			  var colName = savedCriteria.colName;
				var sortOrder = savedCriteria.sortOrder;
				var pageNum = (savedCriteria.pageNum == 0)? (savedCriteria.pageNum +2):(savedCriteria.pageNum+ 1);
				savedCriteria.pageNum = pageNum;
				var searchStr = savedCriteria.searchStr;
				var caseSensitiveFlag = savedCriteria.caseSensitiveFlag;
				var filterBy = savedCriteria.filterBy;
				var selectedIds = savedCriteria.selectedIds;
				var filterByCompany  = savedCriteria.filterByCompany ;
				var selectedCompanyIds = savedCriteria.selectedCompanyIds;
			var separator = "";
			 $j("#tmDtlRowsList input:checked").each(function(i) {
				 if(selectedTermIds==""){
					 separator = "";
				 }else{
					 separator = ",";
				 }
				 selectedTermIds += separator+$j(this).parent().next().attr("termId");
				 });
				triggerTmDetails(colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds,filterByCompany,selectedCompanyIds);
		};
});
	
$j(".tmPrevious").click(function(){
	if($j(".tmPrevious").hasClass("prevEnable")){
		 $j("#selectAllTms").attr('checked',false);
		  $j(".case").attr('checked',false);
			var colName = savedCriteria.colName;
			var sortOrder = savedCriteria.sortOrder;
			var langIds = savedCriteria.langIds;
			var pageNum = savedCriteria.pageNum -1;
			var searchStr = savedCriteria.searchStr;
			var caseSensitiveFlag = savedCriteria.caseSensitiveFlag;
			var filterBy = savedCriteria.filterBy;
			var selectedIds = savedCriteria.selectedIds;
			var filterByCompany  = savedCriteria.filterByCompany ;
			var selectedCompanyIds = savedCriteria.selectedCompanyIds;
			var isTM=savedCriteria.isTM;
			var separator = "";
			 $j("#tmDtlRowsList input:checked").each(function(i) {
				 if(selectedTermIds==""){
					 separator = "";
				 }else{
					 separator = ",";
				 }
				 selectedTermIds += separator+$j(this).parent().next().attr("termId");
				 });
		
			 triggerTmDetails( colName, sortOrder, pageNum, searchStr, caseSensitiveFlag, filterBy, selectedIds,filterByCompany,selectedCompanyIds);
	
	}
		
});

$j("#mngTmDtlSectionHead div").click(function(){
	var sortOrder = $j(this).attr('sortOrder');
	var colName =  $j(this).attr('id');
	if(colName == "column2"){
		return;
	}
	if(!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))){
		$j("#mngTmDtlSectionHead div").each(function(index) {
			$j(this).removeClass("ascending descending");
			$j(this).find('.sort').remove();
			$j(this).attr('sortOrder', 'ASC');
		});
		$j(this).attr('sortOrder', 'DESC');
		$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
		$j(this).addClass("ascending");
	}else if($j(this).hasClass("ascending")){
		  	$j("#mngTmDtlSectionHead div").each(function(index) {
		  		$j(this).removeClass("ascending descending");
		  		$j(this).find('.sort').remove();
		  		$j(this).attr('sortOrder', 'ASC');
		  	});
		  	$j(this).attr('sortOrder', 'ASC');
		  	$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
		  	$j(this).addClass("descending");
	}else if($j(this).hasClass("descending")){
		$j("#mngTmDtlSectionHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder', 'ASC');
			});
		$j(this).attr('sortOrder', 'DESC');
		$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
		$j(this).addClass("ascending");
		}
	
	triggerTmDetails(colName, sortOrder, 1, savedCriteria.searchStr, savedCriteria.caseSensitiveFlag,savedCriteria.filterBy,savedCriteria.selectedIds,savedCriteria.filterByCompany ,savedCriteria.selectedCompanyIds);
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

$j("#searchTm").click(function(){
	 triggerTMSearch();
	
});

$j('#newTmTerm').bind('keydown',function(e){
	  if(e.which==13)
		  triggerTMSearch();
		  
	});

var triggerTMSearch = function(){
	var caseSensitiveFlag = false;
	var searchBy = $j("#newTmTerm").val();
	    searchBy=$j.trim(searchBy);
	   searchBy=searchBy.replace(/[&\/\\#,|!^!@+()$~%.'":*?<>{}]/g,'');
	   searchBy=searchBy.replace(/\s\s+/g, ' ');
	if($j("#chktmCase").attr('checked')){
		caseSensitiveFlag = true;
	}
	
	if(searchBy == "Enter term to search..." || $j.trim(searchBy) == ""){
		searchBy = null;
		caseSensitiveFlag = false;
	}
	
	if(!(/[\^\<\>\%\$\#\(\)\{\}\?\|\~\"\'\@\&\!]+/g).test(searchBy) && searchBy != null){
	triggerTmDetails(null, null, 0, searchBy, caseSensitiveFlag, null, null,null,null);
	}
	else{
		alertMessage("#validationMsg");
	}


	return false;
};
$j().ready(function(){
	
	$j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.SEARCH);
	$j("#currentMenuPage").val(Constants.SUBMENU_ITEMS_SEARCH.TMS);
					
		   if($j("#adminHeaderFlag").val() == "true"){
			$j("#adminHeader").show();
			 $j('#adminProfile').css('color','#0DA7D5');
			   $j('#adminProfile').children("img").show();
				$j(".accuracyChrt").hide();
			
		}
            if($j("#userHeaderFlag").val() == "true"){
            	$j("#userHeader").show();
          	    $j('#userLinguisticAssets').children("img").show();
          	  $j('#userLinguisticAssets').css('color','#0DA7D5');
			$j(".accuracyChrt").show();
			ChartRender.destroyTwoDPieChart();
			chatData();
			}
		  if($j("#headerFlag").val() == "true"){
			  $j(".signUpButton").hide();
			  
		  } 
		  if($j.browser.version=="9.0"){
				$j(".paddingseven").css("padding-left","11px");
			}

		if($j.browser.version=="7.0"){
			$j(".menuArrowProf").css("top","26");
			$j("#manageTmTbl").removeClass("topmargin15");
			$j("#tmModule").removeClass("topmargin15");
		}
	
				
		$j(".subMenuLinks a").last().css("border-right", "none");
		
		$j("#changeForm").attr('autocomplete', 'off');

		$j("#manageTmTbl").tmDetails();
		triggerTmDetails(null, null, 0, null, false, null, null,null,null);
//		triggerTmDetails(null, null, 0, null, false, null, null,null,null);
		
		$j("#newTmTerm").focus(function(){
			$j(this).val('');
		});
		$j("#newTmTerm").blur(function(){
			if($j(this).val() == '')
			$j(this).val('Enter term to search...');
		});
		if($j.browser.version=="7.0"){
			$j(".menuArrowAdmn").css("top","26");
			$j(".termAttr").css("padding-bottom","10px");
		}
		
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
		
		//chatData();
	
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
                    	dataXML.push("<chart showValues='0' adjustDiv='0' showBorder='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' bgColor='#f9f9f9,#f9f9f9' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#f9f9f9' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='2' lineColor='#0e94bc' canvasBorderThickness='0'>");
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