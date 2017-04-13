 (function(window, $j) {
	 
	 
	var termListLimit = 10;
	var noOfPages =0;
	var totalTerms = 0;
	var savedCriteria = null;
	var termBaseData=null;
	var pieChartHeight = 40;
	var pieChartWidth = 40;
	var multiCValues=null;
	var multiTaskValues=null;
	 var langSelectedVal =[];
	 var compSelectedVal = [];
	 var taskSelectedVal=[];
	 var multiLValues=null;
	 var multiJobValues=null;
	 var sourceSegment =null;
	 var targetSegment =null;
	 var sourceTerm=null;
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
	/**	
	
	/*var termDtlTmpl = ['<div class="rowBg"> <div class="rowGS twistie_close" termId="',
		   				'',
		   				'"><div class="width130 bigFont pollTerm toppadding5">',
		   				'',
		   				'</div><div class="smallPie width40" id=',
		   				'',
		   				'></div><div class="width150 bigFont targetTrm toppadding5">',
		   				'',
		   				'</div><div class="width110 toppadding5 trmLanguage smallFont floatleft">',
		   				'',
		   				'</div><div class="width110 toppadding5 floatleft">',
		   				'',
		   				'</div><div class="width110 toppadding5 viewDate smallFont floatleft">',
		   				'',
		   				'</div><div class="editDate nodisplay width90 toppadding5"><input type="text" size="8" class="rightmargin5" id="',
		   				'',
		   				'" /></div><div class="width90  toppadding5 source">',
		   				'',
		   				'</div><div class="width90  toppadding5 jobId">',
		   				'',
		   				'</div><div class="width80  toppadding5 jobName">',
		   				'',
		   				'</div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay"  id="overView" termId="',
		   				'',
		   				'"><div class="loadingDiv" style="text-align: center;"><img src='+$j("#contextRootPath").val()+'/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
		   				];*/
	var termDtlTmpl = ['<div class="rowBg"><div class="width20 chkBx"><input type="checkbox" class="floatleft case" value="test" /></div><div class="rowGS twistie_close" termId="', 
	                   '',
		   				'" id="gsrowid_',
		   				'',
		   				'"><div class="width90 bigFont pollTerm toppadding5">',
		   				'',
		   				'</div><div class="smallPie width40" id=',
		   				'',
		   				'></div><div class="width110 bigFont targetTrm toppadding5">',
		   				'',
		   				'</div><div class="width110 toppadding5 trmLanguage smallFont floatleft">',
		   				'',
		   				'</div><div class="width110 toppadding5 floatleft targetLang">',
		   				'',
		   				'</div><div class="width110  toppadding5 origin">',
		   				'',
		   				'</div><div class="width60  toppadding5 pageId">',
		   				'',
		   				'</div><div class="width80  toppadding5 transId">',
		   				'',
		   				'</div><div class="width50  toppadding5 jobid">',
		   				'',
		   				'</div><div class="width50  toppadding5 taskid">',
		   				'',
		   				'</div><div class="width40 toppadding15 modify"><img class="headerMenuLink modifyGSImg "  height="20px" width="20px" src="'+$j("#contextRootPath").val()+'/images/Pencil1.png" editId="',
		   				'',
		   			    '" /></div><div class="width40 toppadding15 delete" ><img class="headerMenuLink deleteGSImg" height="20px" width="20px"  src="'+ $j("#contextRootPath").val()+'/images/DeleteIcon1.png" imgId="',
		   				'',
		   			    '" /></div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay"  id="overView" termId="',
		   				'',
		   				'"><div class="loadingDiv" style="text-align: center;"><img src='+$j("#contextRootPath").val()+'/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
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
	
		
			$j.fn.termDetails = function() {
				var ctx = $j(this);
				
				
//				var termAttrTmpl = '<div class="termAttr"><div class="bold label" style="font-size:11px;">Source Segment:</div> <span class="sourceDesc viewDetailsFld "></span><span class="sourceDescEdit editDetailsFld nodisplay"></span><div class="clear"></div></div><div class="floatleft topmargin5" style="font-size:11px;"><div><div class="bold label"> </div> <div class="targetDesc viewDetailsFld" style=" word-wrap: break-word;"></div><textarea  cols="30"  rows="6"  name="editTarget"  id=#{targetId} class="targetDescEdit editDetailsFld nodisplay editTarget"  value=""></textarea><div class="clear"></div></div></div></div>';
				var termAttrTmpl = '<div class="termAttr"><div style="font-size:11px;"><div class="bold sourceTerm label" >Source Segment:</div> <span class="sourceDesc viewDetailsFld"></span><span   name="editSource"   class="sourceDescEdit editDetailsFld nodisplay editSourceTerm"  value="" ></span><div class="clear"></div>'
				                  + '</div><div class="topmargin5" style="font-size:11px;"><div class=" bold targetTerm label" >Target Segment: </div> <div class="targetDesc viewDetailsFld" style="word-wrap:break-word;" ></div><textarea rows="3" cols="65"   name="editTarget"  id=#{targetId} class="targetDescEdit editDetailsFld nodisplay editTargetTerm"  value="" ></textarea><div class="clear"></div></div></div>'
				
				
				var suggTrmListTmpl = [
					                     '<div class="termSlctFrm topmargin15"><span class="termSuggestion" style=" word-wrap: break-word;">',
					                     '',
					                     '</span><div class="votesBar" id="',
					                     '',
					                     '">',
					                     '',
					                     '</div><input type="button" value="Pick as Final" class="pickFinalBtn pcfinalGSterm nodisplay" id="',
					                     '',
					                     '"/></div><div class="clear"></div>',
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
				
				
				var classNames = ['purpleBar','greenBar','yellowBar','pinkBar'];
				var startWaiting = function (msg, selector, floatRight){
					if(selector) {
						if (typeof msg == 'undefined' || msg == null) {
							msg = " &nbsp; Loading... please wait.";
						}
						var temp = '<div class="loading-msg  topmargin15" style="padding-left:320px;"><img src="'+$j("#contextRootPath").val()+'/images/loading.gif" />'+msg+'</div>';
						selector.append(temp);

					}
				};
				var stopWaiting = function (selector){
					if(selector) {
						selector.find(".loading-msg").hide();
					}
				};

				var displayTermDetails = function(data) {

					stopWaiting($j('#termDtlRowsList'));
					$j('#termDtlRowsList').empty();
					var termDetails = data;

					var listLength = (termDetails.length >= termListLimit)?termListLimit:termDetails.length;
					for ( var count = 0; count < listLength; count++) {
						var termDtlTmplClone = termDtlTmpl;
						 sourceSegment = (termDetails[count].sourceSegment == null || termDetails[count].sourceSegment == "")?"&nbsp;":termDetails[count].sourceSegment;
						 targetSegment = (termDetails[count].targetSegment == null || termDetails[count].targetSegment == "")?"&nbsp;":termDetails[count].targetSegment;
						termDtlTmplClone[1] = termDetails[count].gsTermId;
						termDtlTmplClone[3] = termDetails[count].gsTermId;
						termDtlTmplClone[5] = termDetails[count].sourceSegment;
						termDtlTmplClone[7] = "dataPie_" + termDetails[count].gsTermId;
						termDtlTmplClone[9] = targetSegment;
						if(termDetails[count].sourceLang!=null){
							termDtlTmplClone[11] = termDetails[count].sourceLang;
						}
						if(termDetails[count].targetLang!=null){
							termDtlTmplClone[13] = termDetails[count].targetLang;
						}
						termDtlTmplClone[15] = termDetails[count].origin;
						termDtlTmplClone[17] = termDetails[count].pageId;
						termDtlTmplClone[19] =  termDetails[count].transUnitId;
						termDtlTmplClone[21] =   termDetails[count].jobId;
						termDtlTmplClone[23] =   termDetails[count].taskId;
						termDtlTmplClone[25] =  termDetails[count].gsTermId;
						termDtlTmplClone[27] = termDetails[count].gsTermId;
						termDtlTmplClone[29] = termDetails[count].gsTermId;
						$j('#termDtlRowsList').append(termDtlTmplClone.join(""));
						if((termDetails[count].status) == "Approved"){
							$j('.tickImg_'+ termDetails[count].termId).css("visibility","visible");
						}else{

						}
						var date = new Date();  
						var currentMonth = date.getMonth();  
						var currentDate = date.getDate();  
						var currentYear = date.getFullYear();  
						$j(function() {
							$j("#"+termDtlTmplClone[13] ).datepicker({
								showOn: "button",
								buttonImage: "images/calendar.gif",
								buttonImageOnly: true,
								minDate: new Date(currentYear, currentMonth, currentDate)  //Disables all the past dates and only current or future dats can be selected
							});
						});

					}


				};

				

				$j('div.pollTerm').each(function(i){
					var origText = $j(this).text();
					if(origText.length > 15){
						var finalText = origText.substring(0,12);
						finalText=finalText+"...";
						$j(this).text(finalText);
						$j(this).attr("title",origText); 
					}


				});


				$j('div.targetTrm').each(function(i){
					var origText = $j(this).text().trim();
					if(origText.length > 12){
						var finalText = origText.substring(0,10);
						finalText=finalText+"...";
						$j(this).text(finalText);
						$j(this).attr("title",origText); 
					}


				});



				var bindEvents = function(data){
					var classNamesClone = classNames.slice(0);
					$j('#termDtlRowsList .rowGS ').click(function(){
						var row = $j(this);
						if($j(this).hasClass('twistie_close')){
							$j(this).parent().next().next().show();
							$j(this).removeClass("twistie_close");
							$j(this).addClass("twistie_open");
							var termId=$j(this).attr("termId");
							var searchBy = $j("#newTerm").val();
							searchBy=$j.trim(searchBy);
							   searchBy=searchBy.replace(/[&\/\\#,|+()$~%.'":*?<>{}]/g,'');
							  var  searchText=searchBy.replace(/\s\s+/g, ' ');
							  searchText=$j.trim(searchText);
							  var  caseFlag=false;
								 if($j("#chktmCase").attr('checked')){
									 caseFlag = true;
								 }
							$t.getTermAttributes(termId,{
								success:function(data){
									if(Boolean(data)){
										var termInfo = data.termInfo;
										var detailsSec = row.parent().siblings('.viewDtlsRow[termId='+row.attr('termId')+'] ');
										var termAttrIds={
												targetId:'targetId_'+termId
												
										}
										var editTermTmpl=new Template(termAttrTmpl).evaluate(termAttrIds);
										detailsSec.html(editTermTmpl);
										var suggTrmListTmplClone = suggTrmListTmpl;
//										var deprecatedTermInfoSecClone= deprecatedTermInfoSec;
										
										if(termInfo != null){
											var suggestedTerm = (termInfo.suggestedTerm == null)?"":termInfo.suggestedTerm;
//											detailsSec.find('.targetDesc').html(Util.wordWrap(100,"<br/>",false,suggestedTerm));
										    sourceTerm=(termInfo.termBeingPolled==null)?"":termInfo.termBeingPolled;
											detailsSec.find('.sourceDesc').html(sourceTerm);
											detailsSec.find('.sourceDesc').each(function(i){
												if(searchBy!=null && searchBy!= "Enter term to search..."){
													var  sourceTermText=$j(this).html();
													
													          sourceTermText=Util.backgroundYellow(sourceTermText,searchText,null,caseFlag);
												
													          $j(this).html(sourceTermText);
													}
													
											});
											detailsSec.find('.sourceDescEdit').html(sourceTerm);
											detailsSec.find('.targetDesc').html(suggestedTerm);
											detailsSec.find('.targetDesc').each(function(i){
												if(searchBy!=null && searchBy!= "Enter term to search..."){
													  var  targetTermText=$j(this).html();
														
													  targetTermText=Util.backgroundYellow(targetTermText,searchText,null,caseFlag);
													          $j(this).html(targetTermText);
													}
													});
											detailsSec.find('.targetDescEdit').html(suggestedTerm);
											
										}
										for(var i=0;i<data.suggestedTermDetails.length;i++){
											var numRand = Math.floor(Math.random()*101);
											suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
//											suggTrmListTmplClone[3] = row.attr('termId');
											suggTrmListTmplClone[3] = "barId_"+row.attr('termId')+i;
											suggTrmListTmplClone[5] = data.suggestedTermDetails[i].noOfVotes;
											suggTrmListTmplClone[7] = data.suggestedTermDetails[i].suggestedTermId;
											detailsSec.append(suggTrmListTmplClone.join(""));
										}
										newTermSec[1] = suggTrmListTmplClone[3];
										newTermSec[3] = "sugg_"+suggTrmListTmplClone[3];
										newTermSec[5] = "submitVote"+suggTrmListTmplClone[3];
//										detailsSec.append(newTermSec.join(""));
										
										
										if(data.deprecatedTermInfo.length>0){
									       var	finalSource="";
									        var finalTarget="";
									        for(var i=0;i<data.deprecatedTermInfo.length;i++){
									        	if(data.deprecatedTermInfo[i].deprecatedSource!=null &&  $j.trim(data.deprecatedTermInfo[i].deprecatedSource)!="")
									        		finalSource=finalSource+data.deprecatedTermInfo[i].deprecatedSource+"," ;
									        	if(data.deprecatedTermInfo[i].deprecatedTarget!=null &&  $j.trim(data.deprecatedTermInfo[i].deprecatedTarget)!="")
									        		finalTarget=finalTarget+data.deprecatedTermInfo[i].deprecatedTarget+",";
									        }
									      
								        finalSource=  finalSource.substring(0,finalSource.lastIndexOf(","));
								        finalTarget=  finalTarget.substring(0,finalTarget.lastIndexOf(","));

							     	deprecatedTermInfoSec[1]=finalSource;
									deprecatedTermInfoSec[3]=finalTarget;
									detailsSec.append(deprecatedTermInfoSecClone.join(""));
										}
									
									detailsSec.append(editDetailsSec);
										
										
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
											 if(data.suggestedTermDetails[i].noOfVotes==0){
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
										
																
										//Edit Details
										detailsSec.find('.editDetails ').click(function(){
											row.find('.editDetailsFld').show();
											row.find('.viewDetailsFld').hide();
//											detailsSec.find('.sourceDesc').html(sourceTerm);
											detailsSec.find('.termAttr').css("padding-bottom","68px");
											detailsSec.find('.editDetailsFld').show();
											detailsSec.find('.viewDetailsFld').hide();
											$(this).hide();
											detailsSec.find('.sourceDescEdit').each(function(i){
												if(searchBy!=null && searchBy!= "Enter term to search..."){
													var  sourceTermText=$j(this).html();
													sourceTermText=Util.backgroundYellow(sourceTermText,searchText,null,caseFlag);
												
													          $j(this).html(sourceTermText);
													}
													
											});
											detailsSec.find('.updateDetails').show();
											detailsSec.find('.termAttr').css("padding-bottom","15px");
											
										});

										row.find(".editDetailsFld select").click(function(event){
											$j(this).focus();
											event.stopPropagation();


										});
										detailsSec.find('.updateDetails').click(function(){
											detailsSec.hide();
											row.find('.editDetailsFld').hide();
											row.find('.viewDetailsFld').show();
											row.removeClass("twistie_open");
											row.addClass("twistie_close");
											row.find('.editDate').hide();
											row.find('.viewDate').show();
											var termId = row.attr('termId');
											saveTermDetails(termId);
											var suggTerm=$j("#targetId_"+termId).val();
											var topSuggestedTerm=(suggTerm ==null || suggTerm =="")?"&nbsp;":suggTerm;
											row.find('.targetTrm').html(topSuggestedTerm).show();
											row.find('.targetTrm').each(function(i){
												var origText = $j(this).text();
												if(searchBy!=null && searchBy!= "Enter term to search..."){
													if(origText.length > 8){
													var finalText = origText.substring(0,6);
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
												
													if(origText.length > 10){
														var finalText = origText.substring(0,8);
														finalText=finalText+"...";
														$j(this).text(finalText);
														$j(this).attr("title",origText); 
													}
												}
											});
										

										});
										
										//Extend Poll
								
										
									
										//Pick as Final button functionality
										$j('.pickFinalBtn').click(function(){
											
											var suggestedTermId = $j(this).attr("id");
										   var termId = row.attr('gsTermId');
										    finaliseTerm(suggestedTermId);
											detailsSec.hide();
											row.removeClass("twistie_open");
											row.addClass("twistie_close");
											row.find('.tickImg').css("visibility","visible");
											row.find(".targetTrm").text($j(this).parent().find(".termSuggestion").html());
											row.find('.targetTrm').each(function(i){
												var origText = $j(this).text();
												if(searchBy!=null && searchBy!= "Enter term to search..."){
													if(origText.length > 8){
													var finalText = origText.substring(0,6);
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
												
													if(origText.length > 10){
														var finalText = origText.substring(0,8);
														finalText=finalText+"...";
														$j(this).text(finalText);
														$j(this).attr("title",origText); 
													}
												}
											});
											row.find('.editDetailsFld').hide();
											row.find('.viewDetailsFld').show();
											row.find('.editDate').hide();
											row.find('.viewDate').show();
											
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
				var bindEvent = function(data){
					var classNamesClone = classNames.slice(0);
					$j('.rowBg .rowGS  .modify .modifyGSImg ').click(function(event){
				    	  event.stopPropagation();
						var row = $j(this);
			/*				$j(this).parent().next().next().show();
							$j(this).removeClass("twistie_close");
							$j(this).addClass("twistie_open");
							var termId=$j(this).attr("termId");*/
						var termId=$j(this).attr("editid");
			    		var row = $j("#gsrowid_"+termId);
							$j("#gsrowid_"+termId).parent().next().next().show();
							$j("#gsrowid_"+termId).removeClass("twistie_close");
							$j("#gsrowid_"+termId).addClass("twistie_open");
							var searchBy = $j("#newTerm").val();
							searchBy=$j.trim(searchBy);
							   searchBy=searchBy.replace(/[&\/\\#,|+()$~%.'":*?<>{}]/g,'');
							  var  searchText=searchBy.replace(/\s\s+/g, ' ');
							  searchText=$j.trim(searchText);
							  var  caseFlag=false;
								 if($j("#chktmCase").attr('checked')){
									 caseFlag = true;
								 }
							$t.getTermAttributes(termId,{
								success:function(data){
									if(Boolean(data)){
										var termInfo = data.termInfo;
										var detailsSec = row.parent().siblings('.viewDtlsRow[termId='+row.attr('termId')+'] ');
										var termAttrIds={
												targetId:'targetId_'+termId
												
										}
										var editTermTmpl=new Template(termAttrTmpl).evaluate(termAttrIds);
										detailsSec.html(editTermTmpl);
										var suggTrmListTmplClone = suggTrmListTmpl;
//										var deprecatedTermInfoSecClone= deprecatedTermInfoSec;
										
										if(termInfo != null){
											var suggestedTerm = (termInfo.suggestedTerm == null)?"":termInfo.suggestedTerm;
											detailsSec.find('.targetDesc').html(suggestedTerm);
											detailsSec.find('.targetDescEdit').html(suggestedTerm);
											var sourceTerm=(termInfo.termBeingPolled==null)?"":termInfo.termBeingPolled;
											detailsSec.find('.sourceDesc').html(sourceTerm);
											detailsSec.find('.sourceDescEdit').html(sourceTerm);
											detailsSec.find('.sourceDescEdit').each(function(i){
												if(searchBy!=null && searchBy!= "Enter term to search..."){
													var  targetTermText=$j(this).html();
													targetTermText=Util.backgroundYellow(targetTermText,searchText,null,caseFlag);			
												
													          $j(this).html(targetTermText);
													}
													
											});

										}
										for(var i=0;i<data.suggestedTermDetails.length;i++){
											var numRand = Math.floor(Math.random()*101);
											suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
//											suggTrmListTmplClone[3] = row.attr('termId');
											suggTrmListTmplClone[3] = "barId_"+row.attr('termId')+i;
											suggTrmListTmplClone[5] = data.suggestedTermDetails[i].noOfVotes;
											suggTrmListTmplClone[7] = data.suggestedTermDetails[i].suggestedTermId;
											detailsSec.append(suggTrmListTmplClone.join(""));
										}
										newTermSec[1] = suggTrmListTmplClone[3];
										newTermSec[3] = "sugg_"+suggTrmListTmplClone[3];
										newTermSec[5] = "submitVote"+suggTrmListTmplClone[3];
//										detailsSec.append(newTermSec.join(""));
										
										
										if(data.deprecatedTermInfo.length>0){
									       var	finalSource="";
									        var finalTarget="";
									        for(var i=0;i<data.deprecatedTermInfo.length;i++){
									        	if(data.deprecatedTermInfo[i].deprecatedSource!=null &&  $j.trim(data.deprecatedTermInfo[i].deprecatedSource)!="")
									        		finalSource=finalSource+data.deprecatedTermInfo[i].deprecatedSource+"," ;
									        	if(data.deprecatedTermInfo[i].deprecatedTarget!=null &&  $j.trim(data.deprecatedTermInfo[i].deprecatedTarget)!="")
									        		finalTarget=finalTarget+data.deprecatedTermInfo[i].deprecatedTarget+",";
									        }
									      
								        finalSource=  finalSource.substring(0,finalSource.lastIndexOf(","));
								        finalTarget=  finalTarget.substring(0,finalTarget.lastIndexOf(","));

							     	deprecatedTermInfoSec[1]=finalSource;
									deprecatedTermInfoSec[3]=finalTarget;
									detailsSec.append(deprecatedTermInfoSecClone.join(""));
										}
									
									detailsSec.append(editDetailsSec);
										
										
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
										
																
										//Edit Details
										detailsSec.find('.editDetails ').click(function(){
											row.find('.editDetailsFld').show();
											row.find('.viewDetailsFld').hide();
											detailsSec.find('.termAttr').css("padding-bottom","68px");
											detailsSec.find('.editDetailsFld').show();
											detailsSec.find('.viewDetailsFld').hide();
											$(this).hide();
											detailsSec.find('.updateDetails').show();
											detailsSec.find('.termAttr').css("padding-bottom","15px");
											
										});
										detailsSec.find('.editDetails').trigger('click');

										row.find(".editDetailsFld select").click(function(event){
											$j(this).focus();
											event.stopPropagation();
										});
										detailsSec.find('.updateDetails').click(function(){
											detailsSec.hide();
											row.find('.editDetailsFld').hide();
											row.find('.viewDetailsFld').show();
											row.removeClass("twistie_open");
											row.addClass("twistie_close");
											row.find('.editDate').hide();
											row.find('.viewDate').show();
											var termId = row.attr('termId');
											saveTermDetails(termId);
											var suggTerm=$j("#targetId_"+termId).val();
											var topSuggestedTerm=(suggTerm ==null || suggTerm =="")?"&nbsp;":suggTerm;
											row.find('.targetTrm').html(topSuggestedTerm).show();
											row.find('.targetTrm').each(function(i){
												var origText = $j(this).text();
												if(searchBy!=null && searchBy!= "Enter term to search..."){
													if(origText.length > 8){
													var finalText = origText.substring(0,6);
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
												
													if(origText.length > 10){
														var finalText = origText.substring(0,8);
														finalText=finalText+"...";
														$j(this).text(finalText);
														$j(this).attr("title",origText); 
													}
												}
											});
										

										});
										
										//Extend Poll
								
										
									
										//Pick as Final button functionality
										$j('.pickFinalBtn').click(function(){
											
											var suggestedTermId = $j(this).attr("id");
										   var termId = row.attr('gsTermId');
										    finaliseTerm(suggestedTermId);
											detailsSec.hide();
											row.removeClass("twistie_open");
											row.addClass("twistie_close");
											row.find('.tickImg').css("visibility","visible");
											row.find(".targetTrm").text($j(this).parent().find(".termSuggestion").html());
											row.find('.targetTrm').each(function(i){
												var origText = $j(this).text();
												if(searchBy!=null && searchBy!= "Enter term to search..."){
													if(origText.length > 8){
													var finalText = origText.substring(0,6);
													
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
												
													if(origText.length > 10){
														var finalText = origText.substring(0,8);
														finalText=finalText+"...";
														$j(this).text(finalText);
														$j(this).attr("title",origText); 
													}
												}
											});
											row.find('.editDetailsFld').hide();
											row.find('.viewDetailsFld').show();
											row.find('.editDate').hide();
											row.find('.viewDate').show();
											
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
				ctx.bind("showGSTermDetails", function(event,criteria) {
					startWaiting(" &nbsp; Loading ... please wait", $j('#termDtlRowsList'), false);
					$j("#pagination").hide();
			    	var pageNum = criteria.pageNum;
					var  queryAppender= new Object();
					queryAppender.colName = criteria.colName;
					queryAppender.sortOrder = criteria.sortOrder;
					queryAppender.pageNum = criteria.pageNum;
					 queryAppender.searchStr = criteria.searchBy;
					 queryAppender.caseFlag = criteria.caseSensitive;
					 queryAppender.filterBy = criteria.filterBy;
					 queryAppender.selectedIds = criteria.selectedIds;
					 queryAppender.filterByCompany  = criteria.filterByCompany ;
					 queryAppender.selectedCompanyIds = criteria.selectedCompanyIds;
					 queryAppender.filterByTask  = criteria.filterByTask;
					 queryAppender.selectedTaskIds = criteria.selectedTaskIds;
					 queryAppender.isTM="Y";
					var queryAppenderParameter =  Object.toJSON(queryAppender);
					$t.getAllGSTerms(queryAppenderParameter, {
								success:function(data){
									$j('#termDtlRowsList').empty();
//									var termIds=data;
//									var gsTerms=data.length;
//								 		totalTerms= totalTerms-(gsTerms);
//										     	var pageNum = savedCriteria.pageNum;
//											   var totalTermsTillBefore = (pageNum-1) * 10;
//											   if(totalTermsTillBefore == totalTerms){
//												  savedCriteria.pageNum = pageNum-1;
//											  }
									 langSelectedVal =[];
								     compSelectedVal = [];
									 taskSelectedVal=[];
											   alertMessage("#deleteGS");
												$j("#newTerm").val("Enter term to search...");
												 $j("#mutliCompanySlct").multiselect("uncheckAll");
												 $j("#mutlilangSlct").multiselect("uncheckAll");
												 $j("#jobSlct").multiselect("uncheckAll");
											 triggerTermDetails(null, null, 0,null,null,null,null,null,null,null,null);
											
										},
										error : function(xhr, textStatus, errorThrown) {
											console.log(xhr.responseText);
											if(Boolean(xhr.responseText.message)){
												console.log(xhr.responseText.message);
											}
										}
									});
			
			
				});
				ctx.bind("showTermDetails", function(event,criteria) {
					startWaiting(" &nbsp; Loading ... please wait", $j('#termDtlRowsList'), false);
					$j("#pagination").hide();
					var pageNum = criteria.pageNum;
					var  queryAppender= new Object();
					queryAppender.colName = criteria.colName;
					queryAppender.sortOrder = criteria.sortOrder;
					queryAppender.pageNum = criteria.pageNum;
					 queryAppender.searchStr = criteria.searchBy;
					 queryAppender.caseFlag = criteria.caseSensitive;
					 queryAppender.filterBy = criteria.filterBy;
					 queryAppender.selectedIds = criteria.selectedIds;
					 queryAppender.filterByCompany  = criteria.filterByCompany ;
					 queryAppender.selectedCompanyIds = criteria.selectedCompanyIds;
					 queryAppender.filterByTask  = criteria.filterByTask;
					 queryAppender.selectedTaskIds = criteria.selectedTaskIds;
					 queryAppender.isTM="Y";
					var queryAppenderParameter =  Object.toJSON(queryAppender);
							$t.getGlobalSightTermInfo(queryAppenderParameter, {
								success:function(data){
									if(data!=null){
										var termDetails = data.globalsightTermsList;
									if(termDetails == null||termDetails.length==0){
										var displayCriteria = "No data to display";
										$j("#termDtlRowsList").html('<span style="text-align: center; display: block;  font-size:12px;padding-top:10px; padding-bottom:10px;padding-right:150px;border-bottom:1px solid #DDDDDD;">'+displayCriteria+'</span>' );
										$j("#pagination").hide();
									}else{



									if (termDetails!=null && termDetails.length!=0){
										var length = (termDetails.length >= termListLimit)?termListLimit:termDetails.length;
										var startLimit = 0;
										var endLimit = 0;
										//if(pageNum == 0){
									totalTerms = data.totalResults;
								}
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


										displayTermDetails(termDetails);
										showRolePrivileges();

										for(var i = 0; i<length;i++){
											var dataXML = new Array();
											var invites = (termDetails[i].invites == null)?0:termDetails[i].invites;
											var votesPerTerm = (termDetails[i].totalVotes == null)?0:termDetails[i].totalVotes;
											if(invites == 0 && votesPerTerm ==0 ){
												$j("#dataPie_"+termDetails[i].gsTermId).css("visibility","hidden");
												$j("#dataPie_"+termDetails[i].gsTermId).css("width","40px");
												$j("#dataPie_"+termDetails[i].gsTermId).removeAttr("id");
												continue;
											}
											// Start of chart object
											dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='14' showShadow='0' slicingDistance='0' animation='0' paletteColors='#C9CCCB, #00A4D6' showBorder='0' bgAlpha='30' showToolTip='0'>");
											dataXML.push("<set label='Services' value='"+(invites-votesPerTerm)+"' />");
											dataXML.push("<set label='Hardware' value='"+votesPerTerm+"' />");
											dataXML.push("</chart>");
											ChartRender.twoDPieChart(termDetails[i].gsTermId,pieChartHeight,pieChartWidth, "dataPie_"+termDetails[i].gsTermId, dataXML );
											modalRender.bubble("#dataPie_"+termDetails[i].gsTermId,votesPerTerm+"/"+invites+"<br>"+"voted / invited to vote","bottom center","top center");

										}
										$j("#pagination").show();
										$j("#rangeOfTerms").html(startLimit + "-"+ endLimit);
										var totalTerms3 = insertCommmas(new String(totalTerms));
										$j("#totalPolledTerms").html(totalTerms3);
										pagination(noOfPages, pageNum);
										bindEvents();
										bindEvent();
										var searchBy = $j("#newTerm").val();
										searchBy=$j.trim(searchBy);
										 searchBy=searchBy.replace(/[&\/\\#,|+()$~%.'":*?<>{}]/g,'');
										  var  searchText=searchBy.replace(/\s\s+/g, ' ');
										  searchText=$j.trim(searchText);
										  var  caseFlag=false;
											 if($j("#chktmCase").attr('checked')){
												 caseFlag = true;
											 }
										$j('div.pollTerm').each(function(i){
											var origText = $j(this).text();
											if(searchBy!=null &&  searchBy!= "Enter term to search..."){
												if(origText.length > 10){
												var finalText = origText.substring(0,8);
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
													if(origText.length > 10){
													var finalText = origText.substring(0,8);
													finalText=finalText+"...";
													$j(this).text(finalText);
													$j(this).attr("title",origText); 
												}
											}


										});


										$j('div.targetTrm').each(function(i){
											var origText = $j(this).text();
											if(searchBy!=null &&  searchBy!= "Enter term to search..."){
												if(origText.length > 10){
												var finalText = origText.substring(0,8);
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
													if(origText.length > 10){
													var finalText = origText.substring(0,8);
													finalText=finalText+"...";
													$j(this).text(finalText);
													$j(this).attr("title",origText); 
												}
											}


										});

										$j("#selectAll").click(function (event) {
											event.stopPropagation(); 
											$j('.case').attr('checked', this.checked);

										});
										
										$j('#termDtlRowsList input[type="checkbox"]').click(function(){
										     var countcheck = $j('#termDtlRowsList input[type="checkbox"]:checked').length;
										     if(countcheck != length) {
										    	 $j("#selectAll").attr("checked",false);
										     }else{
										    	 $j("#selectAll").attr("checked",true);
										     }
										 });
									

										$j(".rowBg .rowGS   .deleteGSImg").hover(function(){
											modalRender.bubble(".rowBg .rowGS  .deleteGSImg","Delete","left center","right center");
										});

										$j(".rowBg .rowGS  .deleteGSImg").click(function(event){
											var userRole = "";
											selectedTermIds = "";
											if($j("#termDtlRowsList input:checked").length == 0 ||  $j("#termDtlRowsList input:checked").length ==1){
												var id=$j(this).attr("imgId");
												deleteVal(id);
											}
											event.stopPropagation(); 

										});
										$j(".rowBg .rowGS  .modifyGSImg").hover(function(){
											modalRender.bubble(".rowBg .rowGS  .modifyGSImg","Edit","left center","right center");
										});     
									}
								}else{

									var displayCriteria = "No data to display";
									$j("#termDtlRowsList").html('<span style="text-align: center; display: block;  font-size:12px;padding-top:10px; padding-bottom:10px;padding-right:150px;border-bottom:1px solid #DDDDDD;">'+displayCriteria+'</span>' );
									$j("#pagination").hide();
								
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

			var termListTmpl = ['<div class="termImportTblrows"><div class="width220">',
			                    '',
			                    '</div><div class="width110" style="text-align:center">',
			                    '',
			                    '</div></div>'	                    
			                    ];

			var displayFailedImportList = function(data){

				var	userNames =data.userNames;
				var	lineNumbers =data.lineNumbers;
				$j('.termImportTblBody').html("");
				var termListTmplClone = termListTmpl;
				for (var i=0;i<userNames.length;i++)
				{
					termListTmplClone[1] = userNames[i];
					termListTmplClone[3] = lineNumbers[i];
					$j('.termImportTblBody').append(termListTmplClone.join(""));
				}

			};
			
			var slctOptionsTmpl = ['<option value="',
				                    '',
				                    '" >',
				                    '',
				                    '</option>'
				                    ];
			var displayEmailTemplateList = function(data) {
				var emailTemplateList = data;
				for ( var count = 0; count < emailTemplateList.length; count++) {
					var emailTemplateSlctTmplClone = slctOptionsTmpl;
					emailTemplateSlctTmplClone[1] = emailTemplateList[count].emailTemplateId;
					emailTemplateSlctTmplClone[3] = emailTemplateList[count].emailSubject;
					$j('.inviteEmailTemplate').append(emailTemplateSlctTmplClone.join(""));
				}
			};
			var displayUserTypeList = function(data) {
				var userTypeList = data;
				for ( var count = 0; count < userTypeList.length; count++) {
					var userTypeTmplClone = slctOptionsTmpl;
					userTypeTmplClone[1] = userTypeList[count].roleId;
					userTypeTmplClone[3] = userTypeList[count].roleName;
					$j('.categoryList').append(userTypeTmplClone.join(""));
				}
			};
			var displayLanguageList = function(data, appenderClass) {
				var languageList = data;
				for ( var count = 0; count < languageList.length; count++) {
					var langSlctTmplClone = slctOptionsTmpl;
					langSlctTmplClone[1] = languageList[count].languageId;
					langSlctTmplClone[3] = languageList[count].languageLabel;
					$j(appenderClass).append(langSlctTmplClone.join(""));
				}
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
			var displayTaskList= function(data,appenderClass) {
				var tskList = data;
				for ( var count = 0; count < tskList.length; count++) {
					var jobTmplClone = slctOptionsTmpl;
					jobTmplClone[1] = tskList[count].taskId;
					jobTmplClone[3] = tskList[count].taksName;
					$j(appenderClass).append(jobTmplClone.join(""));
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
			var displayUsersList = function(data) {
				var userList = data;
				for ( var count = 0; count < userList.length; count++) {
					var usersTmplClone = usersTmpl;
					usersTmplClone[1] = userList[count].userId;
					usersTmplClone[3] = userList[count].emailId;
					usersTmplClone[5] = userList[count].userName;
					$j('.usersList').append(usersTmplClone.join(""));
				}
			};
			
			var displayCompanyList= function(data,appenderClass) {
				var companyList = data;
				for ( var count = 0; count < companyList.length; count++) {
					var companyTmplClone = companyTmpl;
					companyTmplClone[1] = companyList[count].companyId;
					companyTmplClone[3] = companyList[count].companyName;
					$j(appenderClass).append(companyTmplClone.join(""));
				}
			};	
			var displayLangList= function(data,appenderClass) {
				var companyList = data;
				for ( var count = 0; count < companyList.length; count++) {
					var companyTmplClone = companyTmpl;
					companyTmplClone[1] = companyList[count].languageId;
					companyTmplClone[3] = companyList[count].languageLabel;
					$j(appenderClass).append(companyTmplClone.join(""));
				}
			};	
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
				
			 var isMultiSelectLangModified = function(newChkValues, oldChkValues){
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
		
			$t.getLanguageList({
				success:function(data){
					displayLangList(data,"#mutlilangSlct");
					 $j("#mutlilangSlct").multiselect().multiselectfilter();
					
				    $j("#mutlilangSlct").multiselect({
				      	   noneSelectedText: 'Select language',
				    	   selectedList: 4, // 0-based index
				    	   onClose:function(){
				    		    		   
				    		 multiLValues = $j("#mutlilangSlct").val();
				    		 if(multiLValues == null){
				    			 multiLValues = [];
								}
				    		  langSelectedVal=removeEmptyString(langSelectedVal);
				    		  var isLangModified=isMultiSelectLangModified(multiLValues,langSelectedVal);
				    		 if(multiLValues==""){
				    			 multiLValues=null;
				    		 }
				    	
								if(isLangModified){
				    			   langSelectedVal = multiLValues;
				    			   if (multiLValues != null && multiLValues.length >0 && multiCValues!=null && multiTaskValues!=null){
					    			   $j("#dataPie_1005").empty();
					    			    triggerTermDetails(null,null, 0, null, false, 'Locale',multiLValues,'Company',multiCValues,'Task',multiTaskValues);
							       }else if(multiLValues != null &&  multiCValues!=null && multiTaskValues ==null){
							     	    triggerTermDetails(null, null, 0, null, false,'Locale',multiLValues,'Company',multiCValues,null,null);
							       	}else if(multiLValues != null &&  multiCValues ==null && multiTaskValues !=null){
							       		 triggerTermDetails(null, null, 0, null, false,'Locale',multiLValues,null,null,'Task',multiTaskValues);
							    	}else if(multiLValues == null &&  multiCValues !=null && multiTaskValues !=null){
							    		  triggerTermDetails(null, null, 0, null, false,null,null,'company',multiCValues,'Task',multiTaskValues);
							    	}else if(multiLValues != null &&  multiCValues ==null && multiTaskValues ==null){
							    		 triggerTermDetails(null, null, 0, null, false,'Locale',multiLValues,null,null,null,null);
							    	}else if(multiLValues == null &&  multiCValues !=null && multiTaskValues ==null){
							    	     triggerTermDetails(null, null, 0, null, false,null,null,'Company',multiCValues,null,null);
							    	} else if(multiLValues == null &&  multiCValues ==null && multiTaskValues !=null){
							    		 triggerTermDetails(null, null, 0, null, false,null,null,null,null,'Task',multiTaskValues);
								   	}else{
								   		 triggerTermDetails(null, null, 0, null, false,null,null,null,null,null,null);
							    	}
								}
				    							    
				    	   }
			    		   
				    });
				   
				}
			});
			$t.getCompanyList({
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
									 if (multiLValues != null && multiLValues.length >0 && multiCValues!=null && multiTaskValues!=null){
										    $j("#dataPie_1005").empty();
										  triggerTermDetails(null,null, 0, null, false, 'Locale',multiLValues,'Company',multiCValues,'Task',multiTaskValues);
								       }else if(multiLValues != null &&  multiCValues!=null && multiTaskValues ==null){
								    	     triggerTermDetails(null, null, 0, null, false,'Locale',multiLValues,'Company',multiCValues,null,null);
								       	}else if(multiLValues != null &&  multiCValues ==null && multiTaskValues !=null){
								       	   	 triggerTermDetails(null, null, 0, null, false,'Locale',multiLValues,null,null,'Task',multiTaskValues);
								    	}else if(multiLValues == null &&  multiCValues !=null && multiTaskValues !=null){
								    	   	  triggerTermDetails(null, null, 0, null, false,null,null,'Company',multiCValues,'Task',multiTaskValues);
								    	}else if(multiLValues != null &&  multiCValues ==null && multiTaskValues ==null){
								    	   	 triggerTermDetails(null, null, 0, null, false,'Locale',multiLValues,null,null,null,null);
								    	}else if(multiLValues == null &&  multiCValues !=null && multiTaskValues ==null){
								    	  	 triggerTermDetails(null, null, 0, null, false,null,null,'Company',multiCValues,null,null);
								    	} else if(multiLValues == null &&  multiCValues ==null && multiTaskValues !=null){
								    	    	 triggerTermDetails(null, null, 0, null, false,null,null,null,null,'Task',multiTaskValues);
									  	}else{
									  	        triggerTermDetails(null, null, 0, null, false,null,null,null,null,null,null);
								    	}
								}
				    							    
				    	   }
			    		   
				    });
				   
				}
			});
			var finaliseTerm = function(suggestedTermId){
		    	$t.finalizeTerm(suggestedTermId, {
					success : function(data, textStatus, xhr) {
						if (xhr.readyState == 4) {
							if ((xhr.status >= 200 && xhr.status < 300)
									|| xhr.status == 304) {
								
							}
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
   $j(".removeUser").click(function(){
			
			$j(".usersList :selected").remove();
			if ($j('.usersList').html() == "") {
				$j('.usersList').addClass("error");
				$j("#noUsersReq").show();
				flag = false;
			} else {
				$j("#noUsersReq").hide();
				$j('.usersList').removeClass("error");
			}
		});
			var showImprtUserDialog = function() {
				// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
				$j( "#termImprtInfo:ui-dialog" ).dialog( "destroy" );

				$j( "#termImprtInfo" ).dialog({
					height: 500,
					width:400,
					modal: true,
					close: function(event, ui) { 
						$j("#termImprtInfo").val("");
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

			var startLoading = function (id, msg){
				var temp = '<div class="loading-msg alignCenter topmargin15" style="padding-left:150px;"><img src="'+$j("#contextRootPath").val()+'/images/loading.gif" />'+msg+'</div>';
				$j("#"+id).append(temp);

			};
			$j("#importFile").click(function(){
				if($j.trim($j("#importTerm").val()) != ""){
					startLoading("message", " &nbsp; Uploading... please wait");
					uploadStatus();
					$j("#selectedCompanyIds").val(multiCValues);
					$j("#termImportForm").attr("action",  $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getImportXliffData?selectedCompanyIds="+ multiCValues);
					$j("#termImportForm").submit();


				}
				else{
					alertMessage("#importMessage");
				}


			});

			
			 var deleteVal = function(id){
				 selectedTermIds="";
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
							 
								 if(selectedTermIds!=""){
									 deleteGSDetails(selectedTermIds,deletedcount,selectedcount);
								  }
									
									
								$j( this ).dialog( "close" );
								 
							},
						Cancel: function() {
						$j( this ).dialog( "close" );
						}
					},
					close: function(event, ui) { 
						$j('#termAction').val(0);
					}
				});
			};
			
			var deleteMultiGSVal = function(){
				   selectedTermIds="";
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
								  $j("#termDtlRowsList input:checked").each(function(i) {
									  var deleteflag = true;
									  selectedcount++;
									  deletedcount++;
									 var d = new Date();
									  var month = d.getMonth()+1;
									  var day = d.getDate();
									  var year=d.getFullYear()
									  
									  if(deleteflag){
									  selectedTermIds += separator+$j(this).parent().next().attr("termId");
									  separator = ",";
									  var viewNode = $j(this).parent().parent().next().next();
									  viewNode.remove();
									  $j(this).parent().parent().remove();
										 $j(this).parent().parent().remove();
								  }
									     });
								 if(selectedTermIds!=""){
									 deleteGSDetails(selectedTermIds,deletedcount,selectedcount);
								  }
									
									
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
				
				
				var deleteAllGSTerms = function(){
					
					   selectedTermIds="";
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
									triggerGSTermDetails(savedCriteria.colName
											, savedCriteria.sortOrder
											, savedCriteria.pageNum
											,savedCriteria.searchBy
											,savedCriteria.caseSensitive
											,savedCriteria.filterBy
											,savedCriteria.selectedIds
											,savedCriteria.filterByCompany
											,savedCriteria.selectedCompanyIds
											,savedCriteria.filterByTask
											,savedCriteria.selectedTaskIds);
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
					
		      var deleteGSDetails = function(selectedTermIds,deletedcount,selectedcount){
		 		var termIdsArray = selectedTermIds.split(",");
		 	 	$t.deleteGSTerms(termIdsArray, {
						success : function(data) {
							$t.deleteTerms(termIdsArray, {
								success : function(data) {
								},
								error: function(xhr, textStatus, errorThrown){
									console.log(xhr.responseText);
									if(Boolean(xhr.responseText.message)){
										console.log(xhr.responseText.message);
									}
								}
							});
							
								totalTerms= totalTerms-(termIdsArray.length);
						     	var pageNum = savedCriteria.pageNum;
							   var totalTermsTillBefore = (pageNum-1) * 10;
							   if(totalTermsTillBefore == totalTerms){
								  savedCriteria.pageNum = pageNum-1;
							}
							    langSelectedVal =[];
							     compSelectedVal = [];
								 taskSelectedVal=[];
										  	$j("#newTerm").val("Enter term to search...");
											 $j("#mutliCompanySlct").multiselect("uncheckAll");
											 $j("#mutlilangSlct").multiselect("uncheckAll");
											 $j("#jobSlct").multiselect("uncheckAll");
							triggerTermDetails(savedCriteria.colName
									, savedCriteria.sortOrder
									, savedCriteria.pageNum
									,savedCriteria.searchBy
									,savedCriteria.caseSensitive
									,savedCriteria.filterBy
									,savedCriteria.selectedIds
									,savedCriteria.filterByCompany
									,savedCriteria.selectedCompanyIds
									,savedCriteria.filterByTask
									,savedCriteria.selectedTaskIds);
							alertMessage("#deleteGS");
						},
						error : function(xhr, textStatus, errorThrown) {
							console.log(xhr.responseText);
							if(Boolean(xhr.responseText.message)){
								console.log(xhr.responseText.message);
							}
						}
					});
			      };
			var uploadStatus= function(){
				$t.getUploadStatus({
					success:function(data){
						if(data != null){
							$j("#message").find(".loading-msg").hide();
							if(data.termInformationStatus == "success"){
								$j("#importTerm").val("");
								$j('#termDtlRowsList').empty();
								$j("#importTbl").termDetails();
								$j(".invalidfile").hide();
								triggerTermDetails(null, null, 0,null,null,null,null,null,null,null,null);
							}
							$j("#termImprtInfo").show();
							$j("#successAddedTerms").html("");
							$j("#failedAddedTerms").html("");
							if(data.insertedCount>0)
							$j("#successAddedTerms").html("Successfully imported terms : "+data.insertedCount);
							showImprtUserDialog();
							 if(data.termInformationStatus == "error"){
								  if(data.rejectedCount == -1){
									  $j(".failedInfo").hide();
									  $j(".invalidfile").show();
								  } 
							  }
							if(data.rejectedCount == 0 || data.rejectedCount == -1){
								$j(".failedInfo").hide();
							}
							else{
								$j(".failedInfo").show();
								$j("#failedAddedTerms").html(data.rejectedCount);
								displayFailedImportList(data);

							}
							/*if(data.termInformationStatus == "invalid"){
								$j(".invalidfile").show();
								$j(".failedInfo").hide();
							}*/
						}
						else{
							setTimeout(function(){
								uploadStatus();

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

			$j("#cancelInvitMail").click(function(){
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
				$j('.inviteEmailTemplate').children().remove().end().append('<option value="">--Select template--</option>') ;
				$j(".emailPrvw").html("");
				$j(".mailTmpl").val(0);
				$j(".sampleVoteMail").hide();
				$j(".sampleWelcomeMail").hide();
				selectedValues = "";
				selectedTermIds = "";
			});
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

			 
			var triggerTermDetails = function(colName, sortOrder, pageNum,searchBy,caseSensitive,filterBy,selectedIds,filterByCompany,selectedCompanyIds,filterByTask,selectedTaskIds){
				 $j("#selectAll").attr('checked',false);
				if(colName == null){
					$j("#importSectionHead div").each(function(index) {
						$j(this).removeClass("ascending descending");
						$j(this).find('.sort').remove();
						$j(this).attr('sortOrder','ASC');
					});
				}

				$j('#termDtlRowsList').empty();
				$t.getTotalGSTerms({
					success:function(data){
						var totalTerms1 = new String( data);
						var totalTerms2 = insertCommmas(totalTerms1);
						$j("#totalTerms").html(totalTerms2);
						$j("#termsInGlossary").val(totalTerms1);
				   var criteria = {
						'colName':colName,
						'sortOrder':sortOrder,
						'pageNum': pageNum,
						'searchBy': searchBy,
						'caseSensitive': caseSensitive,
						'filterBy': filterBy,
						'selectedIds' :selectedIds,
						'filterByCompany' :filterByCompany ,
						'selectedCompanyIds':selectedCompanyIds,
						'filterByTask':filterByTask,
						'selectedTaskIds':selectedTaskIds

				};
				savedCriteria = criteria;
				$j("#importTbl").trigger("showTermDetails", criteria);
			},
				error: function(xhr, textStatus, errorThrown){
					console.log(xhr.responseText);
					if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
					}
				}
			});
			
	};

	var triggerGSTermDetails = function(colName, sortOrder, pageNum,searchBy,caseSensitive,filterBy,selectedIds,filterByCompany,selectedCompanyIds,filterByTask,selectedTaskIds){
		 $j("#selectAll").attr('checked',false);
		if(colName == null){
			$j("#importSectionHead div").each(function(index) {
				$j(this).removeClass("ascending descending");
				$j(this).find('.sort').remove();
				$j(this).attr('sortOrder','ASC');
			});
		}

		$j('#termDtlRowsList').empty();
		$t.getTotalGSTerms({
			success:function(data){
				var totalTerms1 = new String( data);
				var totalTerms2 = insertCommmas(totalTerms1);
				$j("#totalTerms").html(totalTerms2);
				$j("#termsInGlossary").val(totalTerms1);
		   var criteria = {
					'colName':colName,
					'sortOrder':sortOrder,
					'pageNum': pageNum,
					'searchBy': searchBy,
					'caseSensitive': caseSensitive,
					'filterBy': filterBy,
					'selectedIds' :selectedIds,
					'filterByCompany' :filterByCompany ,
					'selectedCompanyIds':selectedCompanyIds,
					'filterByTask':filterByTask,
					'selectedTaskIds':selectedTaskIds

		};
		savedCriteria = criteria;
		$j("#importTbl").trigger("showGSTermDetails", criteria);
	},
		error: function(xhr, textStatus, errorThrown){
			console.log(xhr.responseText);
			if(Boolean(xhr.responseText.message)){
				console.log(xhr.responseText.message);
			}
		}
	});
	
};
 	$j(".next").click(function(){
		if($j(".next").hasClass("nextEnable")){
			 $j("#selectAll").attr('checked',false);
			  $j(".case").attr('checked',false);
			var colName = savedCriteria.colName;
			var sortOrder = savedCriteria.sortOrder;
			var pageNum = (savedCriteria.pageNum == 0)? (savedCriteria.pageNum +2):(savedCriteria.pageNum+ 1);
			var searchBy=savedCriteria.searchBy;
			var casesensitive=savedCriteria.caseSensitive;
			var filterBy = savedCriteria.filterBy;
			var selectedIds = savedCriteria.selectedIds;
			var filterByCompany = savedCriteria.filterByCompany;
			var selectedCompanyIds = savedCriteria.selectedCompanyIds;
			var filterByTask = savedCriteria.filterByTask;
			var selectedTaskIds = savedCriteria.selectedTaskIds;
			savedCriteria.pageNum = pageNum;
				var separator = "";
			 $j("#termDtlRowsList input:checked").each(function(i) {
				 if(selectedTermIds==""){
					 separator = "";
				 }else{
					 separator = ",";
				 }
				 selectedTermIds += separator+$j(this).parent().next().attr("termId");
				 });
			triggerTermDetails( colName, sortOrder, pageNum,searchBy,casesensitive,filterBy,selectedIds,filterByCompany,selectedCompanyIds,filterByTask,selectedTaskIds);
			
		};
});

 	$j(".previous").click(function(){
 		 $j("#selectAll").attr('checked',false);
		  $j(".case").attr('checked',false);
		if($j(".previous").hasClass("prevEnable")){
				var colName = savedCriteria.colName;
				var sortOrder = savedCriteria.sortOrder;
				var langIds = savedCriteria.langIds;
				var pageNum = savedCriteria.pageNum -1;
				var searchBy=savedCriteria.searchBy;
				var casesensitive=savedCriteria.caseSensitive;
				var filterBy = savedCriteria.filterBy;
				var selectedIds = savedCriteria.selectedIds;
				var filterByCompany = savedCriteria.filterByCompany;
				var selectedCompanyIds = savedCriteria.selectedCompanyIds;
				var filterByTask = savedCriteria.filterByTask;
				var selectedTaskIds = savedCriteria.selectedTaskIds;
				var separator = "";
				 $j("#termDtlRowsList input:checked").each(function(i) {
					 if(selectedTermIds==""){
						 separator = "";
					 }else{
						 separator = ",";
					 }
					 selectedTermIds += separator+$j(this).parent().next().attr("termId");
					 });
				triggerTermDetails( colName, sortOrder, pageNum,searchBy,casesensitive,filterBy,selectedIds,filterByCompany,selectedCompanyIds,filterByTask,selectedTaskIds);
				
		
		}
			
	});
	
		 var saveTermDetails = function(termId) {
				// Create TermInformation Object
				var termDtls = new Object();
				termDtls.termId = parseInt(termId);
				if($j("#targetId_"+termId).val()==""){
					termDtls.topSuggestion=null;
					
				}else{
					termDtls.sourceTerm=sourceTerm;
					termDtls.topSuggestion = $j("#targetId_"+termId).val();
					
				}
				var termDtlsParameter = Object.toJSON(termDtls);
				$t.updateGSTermDetails(termDtlsParameter, {
					success : function(data) {
						$t.updateTermDetails(termDtlsParameter, {
							success : function(data, textStatus, xhr) {
								if (xhr.readyState == 4) {
									if ((xhr.status >= 200 && xhr.status < 300)
											|| xhr.status == 304) {

									}
								}
							},
							error : function(xhr, textStatus, errorThrown) {
								console.log(xhr.responseText);
								if (Boolean(xhr.responseText.message)) {
								}
							}
						});
						
					},
					error : function(xhr, textStatus, errorThrown) {
						console.log(xhr.responseText);
						if (Boolean(xhr.responseText.message)) {
						}
					}
				});

			};

			$j("#importSectionHead div").click(function(){
				var sortOrder = $j(this).attr('sortOrder');
				var colName =  $j(this).attr('id');
				if(colName == "column2" || colName == "column0"){
					return;
				}
				searchBy=$j("#newTerm").val();
				if(searchBy == "Enter term to search..." || $j.trim(searchBy) == ""){
					searchBy = null;
				}
				var caseSensitiveFlag=null;
				if($j("#chktmCase").attr('checked')){
					caseSensitiveFlag = true;
				}
				if(!(/[\^\<\>\%\$\#\(\)\{\}\?\|\~\"\'\@\&\!]+/g).test(searchBy) && searchBy != null){
					savedCriteria.searchBy=searchBy;
					savedCriteria.caseSensitive=caseSensitiveFlag;
				}
				if(!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))){
					$j("#importSectionHead div").each(function(index) {
						$j(this).removeClass("ascending descending");
						$j(this).find('.sort').remove();
						$j(this).attr('sortOrder', 'ASC');
					});
					$j(this).attr('sortOrder', 'DESC');
					$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
					$j(this).addClass("ascending");
				}else if($j(this).hasClass("ascending")){
					$j("#importSectionHead div").each(function(index) {
						$j(this).removeClass("ascending descending");
						$j(this).find('.sort').remove();
						$j(this).attr('sortOrder', 'ASC');
					});
					$j(this).attr('sortOrder', 'ASC');
					$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
					$j(this).addClass("descending");
				}else if($j(this).hasClass("descending")){
					$j("#importSectionHead div").each(function(index) {
						$j(this).removeClass("ascending descending");
						$j(this).find('.sort').remove();
						$j(this).attr('sortOrder', 'ASC');
					});
					$j(this).attr('sortOrder', 'DESC');
					$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
					$j(this).addClass("ascending");
				}
				triggerTermDetails(colName
						, sortOrder
						, 1
						,searchBy
						,caseSensitiveFlag
						,savedCriteria.filterBy
						,savedCriteria.selectedIds
						,savedCriteria.filterByCompany
						,savedCriteria.selectedCompanyIds
						,savedCriteria.filterByTask
						,savedCriteria.selectedTaskIds);
			});
			
			var showLoadingDialog = function() {
				// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
				$j( "#loading:ui-dialog" ).dialog( "destroy" );
			
				$j( "#loading" ).dialog({
					height: 120,
					width:200,
					modal: true,
					resizable: false
				});
			};
			var closeLoadingDialog = function(dialogId,id){
				$j(dialogId).html("");
				$j(dialogId).dialog('destroy');
				$j(id).dialog('destroy');
				mailSent();
			};
			
			var mailSent = function(){
				$j( "#mail_success:ui-dialog" ).dialog( "destroy" );
				
				$j( "#mail_success" ).dialog({
					modal: true,
					buttons: {
						OK: function() {
							$j(this).dialog( "close" );
						}
					}
				});
			}
			
			$j("#inviteVoteMail").click(function(){
				 var invite = new Object();
				 var mailIds = new Array();
				 var userIds = new Array();
				 var termIds = selectedTermIds.split(',');
				 var count = 0;
				 var flag=true;
				 
				 $j(".usersList option").each(function () {
					 	count ++;
					 	mailIds[count] = $j(this).attr("emailid");
					 	userIds[count] = $j(this).val();
		            });
				 invite.mailIds = mailIds;
				 invite.emailTemplateId = $j('.inviteEmailTemplate :selected').val();
				 invite.userIds = userIds;
				 invite.termIds = termIds;
			
				if($j('#votingPeriodNum').val()== ""){
					  $j("#voteReq").show();
					   flag=false;
				} else{
					  var str=$j('#votingPeriodNum').val();
					  var patt1=/^[0-9]+$/g;
					 flag =  patt1.test(str);
					 if(flag==false){
						 $j("#votePReq").show();
					 }
				}

				if($j('.categorySlct  :selected').val()==0){
					$j('.categoryList').addClass("error");
					  $j("#usersReq").show();
						
				}						 
				  
				if($j('.categoryList').val()==null){
					$j('.categoryList').addClass("error");
						  	  $j("#selectReq").show();
						  	   $j("#usersReq").hide();
						  	
						  	flag=false;
					  } else{
						  $j('.categoryList').removeClass("error");
						  $j("#selectReq").hide(); 
						  
						  
					  }	
				 if( $j('.usersList').html()==""){
				  	  flag=false;
				 }

				 if($j("#termVoteForm").valid()){
						$j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="'+$j("#contextRootPath").val()+'/images/loading.gif"  align="top" class="rightmargin10" />&nbsp; Sending mail... Please wait</div>');
					showLoadingDialog();
					$j(".ui-dialog-titlebar").hide();
					setTimeout(function(){
						closeLoadingDialog('#loading','#invitation');
						
					},900);
				invite.votingPeriod = $j('#votingPeriodNum').val();
				 var inviteParameter =  Object.toJSON(invite);
				
				 $t.inviteUsersToVote (inviteParameter,{
						success:function(data){
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
							selectedTermIds="";
						$t.getTotalDebatedTerms({
							success:function(data){
								var totalDebatedTrms = data;
								$j("#totalDebatedTrms").html(totalDebatedTrms);
							},
							error: function(xhr, textStatus, errorThrown){
								console.log(xhr.responseText);
								if(Boolean(xhr.responseText.message)){
									console.log(xhr.responseText.message);
								}
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
				 
				}
				
				 
			});
			$j('#votingPeriodNum').change(function() {
				$j('#voteReq').hide();
				$j('#votePReq').hide();
			});
			
			
			$j('.categorySlct').change(function(){
				$j('.categoryList').html("");
				$j('.usersList').html("");
				$j('#selectReq').hide();
				 $j("#noUsersReq").hide(); 
				if($j(this).val()=="role"){
					$t.getUserTypeList({
						success:function(data){
							displayUserTypeList(data);
						  
						},
						error: function(xhr, textStatus, errorThrown){
							console.log(xhr.responseText);
							if(Boolean(xhr.responseText.message)){
								console.log(xhr.responseText.message);
							}
						}
					});
						
				}
				
				if($j(this).val()=="language"){
					$t.getLanguageList({
						success:function(data){
							displayLanguageList(data, '.categoryList');
						}
					});


				}
			
			});
			
			
			$j(".moveData").click(function() {
				$j('.usersList').html("");
				$j("#noUsersReq").hide();
				$j('.usersList').removeClass("error");
				if ($j('.categoryList').val() == null) {
					$j('.categoryList').addClass("error");
					$j("#selectReq").show();

				} else {
					$j("#selectReq").hide();
//					$j('.categoryList').removeClass("error");
					$j('.categoryList').css("border","1px solid  #BBBBBB");

					var filterBy = $j(".categorySlct").val();
					var ids = $j('.categoryList').val();
					
					$t.getInviteUserList(filterBy, ids, {
						success : function(data) {
							displayUsersList(data);
							if ($j('.usersList').html() == "") {
								$j('.usersList').addClass("error");
								$j("#noUsersReq").show();
								flag = false;
							} else {
								$j("#noUsersReq").hide();
								$j('.usersList').removeClass("error");
							}

						},
						error : function(xhr, textStatus, errorThrown) {
							console.log(xhr.responseText);
							if (Boolean(xhr.responseText.message)) {
								console.log(xhr.responseText.message);
							}
						}
					});
				}

			});
			$j('#termAction').change(function() {
				selectedTermIds = "";
				if($j("#termDtlRowsList input:checked").length == 0){
					alertMessage("#termSelctMsg");
					$j('#termAction').val(0);
				}
				else{
//				  if($j(this).val()==1){
//					  deleteVal();
//					  
//					  
//				  }
				  if($j(this).val()==2){
					  validateTermVote.resetForm();
					  	$j('#categorySlct').removeClass("error");
						$j('.categoryList').removeClass("error"); 
						$j('#termTemplate').removeClass("error");
						$j('#votingPeriodNum').removeClass("error");
						$j("#noUsersReq").hide();
						$j('.usersList').removeClass("error");
						

						
					  $j("#templateReq").hide();
					    $j("#voteReq").hide();
					  $j("#votePReq").hide();
					  $j("#usersReq").hide();
					  $j("#noUsersReq").hide();
					  $j(".inviteEmailTemplate").val('');
					  var separator = "";
						 $j("#termDtlRowsList input:checked").each(function(i) {
							 selectedTermIds += separator+$j(this).parent().next().attr("termId");
							 separator = ",";
							 });
						 
						
					  showDialog();
					  $t.getVoteConfigList({
						   success:function(data){
						
						   if(data != null){
							   	$j("#votingPeriodNum").val(data.votingPeriod);	
							   	
							   	
						 }
						   },
						error: function(xhr, textStatus, errorThrown){
							console.log(xhr.responseText);
							if(Boolean(xhr.responseText.message)){
								console.log(xhr.responseText.message);
							}
						}
					});
					  
						// Email Templates Picklist
						$t.getEmailTemplates({
							success:function(data){
								displayEmailTemplateList(data);
								var emailTmpl = data;
								var emailTemplateId = "";
								$j(".inviteEmailTemplate").change(function(){
									  for(var i=0;i<emailTmpl.length;i++){
										  if($j(".inviteEmailTemplate").val()==0){
											  $j(".emailPrvw").html("");
										  }	else{
										 
										     if($j(this).val()==emailTmpl[i].emailTemplateId){
											  emailTemplateId=$j(this).val();
											 $j(".emailPrvw").html(emailTmpl[i].emailMessageContent);
											}
									  }
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
					  $j('#termAction').val(0);
				  }
				}
				
			});
			
			var showDialog = function() {
				// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
				$j( "#invitation:ui-dialog" ).dialog( "destroy" );
			
				$j( "#invitation" ).dialog({
					height: 510,
					width:550,
					modal: true,
					close: function(event, ui) {
						validateTermVote.resetForm();
						$j(".categoryList").html("");
						$j(".usersList").html("");
						$j(".categorySlct").val(0);
						$j(".inviteEmailTemplate").val("");
						$j('.inviteEmailTemplate').children().remove().end().append('<option value="">--Select template--</option>') ;
						$j("#selectReq").hide();
						$j(".emailPrvw").html("");
						$j(".mailTmpl").val(0);
						$j(".sampleVoteMail").hide();
						$j(".sampleWelcomeMail").hide();
						$j("#invitation").dialog( "destroy" );
						selectedTermIds = ""; }
				});
			};
			$j("#deleteMultipleGSTerms").click(function(){
				if($j("#importTbl input:checked").length == 0){
					alertMessage("#termSelctMsg");
					}else{
						deleteMultiGSVal();
				}
			});
			
			$j("#deleteAllGSTerms").click(function(){
						deleteAllGSTerms();
					
			});
			var triggerSearch = function(){
				var caseSensitiveFlag = false;
			    searchBy = $j("#newTerm").val();
				  searchBy=$j.trim(searchBy);
				   searchBy=searchBy.replace(/[&\/\\#,@!^|+()$~%.'":*?<>{}]/g,'');
				   searchBy=searchBy.replace(/\s\s+/g, ' ');
				
				if($j("#chktmCase").attr('checked')){
					caseSensitiveFlag = true;
				}
				
				if(searchBy == "Enter term to search..." || $j.trim(searchBy) == ""){
					searchBy = null;
					caseSensitiveFlag = false;
				}
				
				if(!(/[\^\<\>\%\$\#\(\)\{\}\?\|\~\"\'\@\&\!]+/g).test(searchBy) && searchBy != null){
						savedCriteria.searchBy=searchBy;
					savedCriteria.caseSensitive=caseSensitiveFlag;
					triggerTermDetails(null, null, 0, searchBy, caseSensitiveFlag,null,null,null,null,null,null);
				}else{
					alertMessage("#validationMsg");
				}


				return false;
			};
			$j('#newTerm').bind('keydown',function(e){
				  if(e.which==13)
					  triggerSearch();
					  
				});
	$j().ready(function(){
		  showRolePrivileges();
		$j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.GLOBALSIGH);
		$j("#currentMenuPage").val(Constants.SUBMENU_ITEMS_GLOBALSIGHT.VIEW_SEGMENTS);
		
		if($j("#adminHeaderFlag").val() == "true"){
			$j("#adminHeader").show();
			$j('#adminGlobalsight').children("img").show();
		}
	    if($j("#userHeaderFlag").val() == "true"){
	    	$j("#userHeader").show();
	    	$j('#termList').children("img").show();
			
		}
		/*if(/chrom(e|ium)/.test(navigator.userAgent.toLowerCase())){
           	$j("#deleteMultipleGSTerms").css("margin-left","715px");
         }*/
		
		if($j.browser.version=="7.0"){
		 	$j("#deleteMultipleGSTerms").css("margin-left","658px");
			$j("#deleteMultipleGSTerms").css("padding","3px 0px");
			
		}
		if($j.browser.version=="9.0"){
			$j("#deleteMultipleGSTerms").css("margin-left","723px");
		}
		if($j.browser.version=="8.0"){
			$j("#deleteMultipleGSTerms").css("margin-left","721px");	
		}
		$j('#adminGlobalsight').css('color','#0DA7D5');

        $j("#importTbl").termDetails();
        triggerTermDetails(null, null, 0,null,null,null,null,null,null,null,null);
        if($j.browser.version=="7.0"  || $j.browser.version=="9.0" ||  $j.browser.version=="8.0"){
        	$j(".paddingseven").css("padding-left","11px");
        	$j("#selectAll").css("margin-top","-2px");
        	$j("#selectAll").css("margin-left","-16px");
        	       
		}
        
    	$j("#newTerm").focus(function(){
			$j(this).val('');
		});
		$j("#newTerm").blur(function(){
			if($j(this).val() == '')
			$j(this).val('Enter term to search...');
		});
		$j("#searchTerm").click(function(){
			 triggerSearch();
			
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
        
        validateTermVote=$j("#termVoteForm").validate({
			debug: true,
			rules: {
				categorySlct: {
                required: {
                depends: function(element) {
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
                      depends: function(element) {
                          return $j("#termTemplate").val() == '';
                           }
                      }
    		}
			},
			
			messages: {
				categorySlct: "Select category to invite users",
				votingPeriodNum: "Voting period is required",
				termTemplate:  "Select email template "
			}
		});
		$j.validator.addMethod('votingPeriod', function(value, element) {
			   return this.optional(element) || (value.match(/^[0-9]+$/));
	    },
	    'Voting period must be numeric value');
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
                    ChartRender.twoDLineChart("myChartId33", "240", "70", "userChart", dataXML);
                }else{
                    $j("myChartId33").hide();
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
			$t.getAllJobs({
			success:function(data){
				displayJobList(data,"#jobSlct");
					
			    $j("#jobSlct").multiselect().multiselectfilter();
				
			    $j("#jobSlct").multiselect({
			      	   noneSelectedText: 'Select jobs',
			    	   selectedList: 2, // 0-based index
			    	   onClose:function(){
			    		   
			    		  
			    		   multiJobValues = $j("#jobSlct").val();
							$j("#taskSelectId").html("");
							$j("#taskSelectId").multiselect("destroy");
							if(multiJobValues!=null){
			    	    	$t.getFileInfoByJobIds(multiJobValues,{
			    				success:function(data1){
			    					if(data1.size()>0){
			    						 $j(".tskDropdown").show();
			    						displayTaskList(data1,"#taskSelectId");
			    						 $j("#taskSelectId").multiselect().multiselectfilter();
			    							
			    						    $j("#taskSelectId").multiselect({
			    						      	   noneSelectedText: 'Select task',
			    						    	   selectedList: 4, // 0-based index
			    						    	   onClose:function(){
			    						    		    		   
			    						    		 multiTaskValues = $j("#taskSelectId").val();
			    						    		 if(multiTaskValues ==  null){
			    						    			 multiTaskValues = [];
			    										}
			    							    		taskSelectedVal=removeEmptyString(taskSelectedVal);
			    										var isModified = isMultiSelectModified(multiTaskValues,taskSelectedVal);
			    										if(multiTaskValues==""){
			    											multiTaskValues=null;
			    										}
			    									
			    										if(isModified){
			    											taskSelectedVal=multiTaskValues;
			    						    		 if (multiLValues != null && multiLValues.length >0 && multiCValues!=null && multiTaskValues!=null){
			    						    			   $j("#dataPie_1005").empty();
			    						    			   triggerTermDetails(null,null, 0, null, false, 'Locale',multiLValues,'Company',multiCValues,'Task',multiTaskValues);
			    								       }else if(multiLValues != null &&  multiCValues!=null && multiTaskValues ==null){
			    								    	  	   triggerTermDetails(null, null, 0, null, false,'Locale',multiLValues,'Company',multiCValues,null,null);
			    								       	}else if(multiLValues != null &&  multiCValues ==null && multiTaskValues !=null){
			    								       		  triggerTermDetails(null, null, 0, null, false,'Locale',multiLValues,null,null,'Task',multiTaskValues);
			    								    	}else if(multiLValues == null &&  multiCValues !=null && multiTaskValues !=null){
			    								    		  triggerTermDetails(null, null, 0, null, false,null,null,'Company',multiCValues,'Task',multiTaskValues);
			    								    	}else if(multiLValues != null &&  multiCValues ==null && multiTaskValues ==null){
			    								    		 triggerTermDetails(null, null, 0, null, false,'Locale',multiLValues,null,null,null,null);
			    								    	}else if(multiLValues == null &&  multiCValues !=null && multiTaskValues ==null){
			    								    	   	 triggerTermDetails(null, null, 0, null, false,null,null,'Company',multiCValues,null,null);
			    								    	} else if(multiLValues == null &&  multiCValues ==null && multiTaskValues !=null){
			    								    	   	triggerTermDetails(null, null, 0, null, false,null,null,null,null,'Task',multiTaskValues);
			    								    	}else{
			    								    		triggerTermDetails(null, null, 0, null, false,null,null,null,null,null,null);
			    								    	}
			    						    							    
			    						    	   }
			    						    	   }   
			    						    });
			    						   
			    					}else{
			    						 $j(".tskDropdown").hide();
			    					}
			    					
			    				},
			    				error: function(xhr, textStatus, errorThrown){
			    					console.log(xhr.responseText);
			    					if(Boolean(xhr.responseText.message)){
			    						console.log(xhr.responseText.message);
			    					}
			    				}
			    			});
			    	   }else{
			    		   $j(".tskDropdown").hide();
			    	   }
		
			     
			    							    
		   	   }
//			    	
			    		   
			    });
			    
 	    
			
			}
		});
        

        });     
	
})(window, jQuery);
        