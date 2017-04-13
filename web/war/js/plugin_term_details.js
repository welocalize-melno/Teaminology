(function($j) {

	var pieChartHeight = 40;
	var pieChartWidth = 40;
	var termListLimit = 10;
	var extraPolls = 0;
	var suggestedTermId = "";
	var noOfPages_user = 0;
	var totalTerms_user = 0;
	var noOfPages_admin = 0;
	var totalTerms_admin = 0;
	var photoPath;
	var pictureId=null;
	var oldtargetTerm = "";
	
	$j.fn.termDetails = function() {

		var ctx = $j(this);

		var termDtlTmpl = [
		   				'<div class="rowBg"><div class="row twistie_close" termId="',
		   				'',
		   				'"><div class="width150 bigFont pollTerm"  style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">',
		   				'',
		   				'</div><div class="smallPie" id=',
		   				'',
		   				' class="width50"></div><div class="width200 bigFont  toppadding5 targetTerm"  style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">',
		   				'',
		   				'</div><div class="width110 toppadding5 trmLanguage">',
		   				'',
		   				'</div><div class="width130 toppadding5 trmCategory">',
		   				'',
		   				'</div><div class="width99 toppadding5 redTxt viewDate">',
		   				'',
		   				'</div><div class="editDate nodisplay"><input type="text" size="8" class="rightmargin5" style="font-size:9px;" id="',
		   				'',
		   				'" /></div></div></div><div class="clear"></div><div class="viewDtlsRow nodisplay"  id="overView" termId="',
		   				'',
		   				'"><div class="loadingDiv" style="text-align: center;"><img src='+$j("#contextRootPath").val()+'/images/ajax-loader.gif" alt=" " height="35px" width="35px" /></div></div>'
		   				];
		
		var termAttrTmpl = '<div class="termAttr" id="termAttr"><div><div class="bold sourceTerm label">Terms being polled<span id=#{starId} class="editDetailsFld nodisplay redTxt">*</span>:</div><div class="sourceDesc viewDetailsFld" style="word-wrap:break-word;" ></div><div style="padding-left: 23px;"><textarea rows="2" cols="75"   name="editSourceTerm"  id=#{sourceId} class="sourceDescEdit width465 editDetailsFld nodisplay editSourceTerm"  value="" ></textarea></div><div class="clear"></div>'
			+ '</div><div id=#{errorId} style="font-size:11px;color-red;position:absolute;padding-left:21px;" class="error showError nodisplay">Term being polled cannot be empty</div><div class="topmargin10 toppadding10 bottompadding10"><div class=" bold targetTerm label" >Top suggestion: </div><div class="targetDesc viewDetailsFld" style="word-wrap:break-word;" ></div><span id="topSuggestionDiv" style="padding-left:22px;"><textarea rows="2" cols="75"   name="editTargetTerm"  id=#{targetId} class="targetDescEdit editDetailsFld width465 nodisplay editTargetTerm"  value="" ></textarea></span><div class="clear"></div>'
			+ '</div><div class="left"><div><div class="bold termType label" >Part of speech: </div> <span class="termTypeDesc viewDetailsFld"></span><select name="termPOS" id=#{posId} class="editDetailsFld nodisplay termPOS"><option value="">---Select ----</option></select><div class="clear"></div>'
			+ '</div><div class="topmargin5"><div class="bold label">Form: </div><span class="formDesc viewDetailsFld"></span><select name="termForm"  id=#{formId} class="editDetailsFld nodisplay termForm"><option value="">---Select----</option></select><div class="clear"></div>'
			+ '</div><div class="topmargin5"><div class="bold label">Category: </div><span class="categoryDesc programDesc viewDetailsFld"></span><select name="category" id=#{categoryId} class="editDetailsFld nodisplay  category"><option value="">---Select----</option></select><div class="clear"></div>'
			+ '</div><div class="topmargin5"><div class="bold label">Term notes: </div><div class="domainDesc viewDetailsFld" style="word-wrap:break-word;"></div><textarea id=#{notesId} rows="1" cols="12" class="domainDescEdit editDetailsFld nodisplay notes"></textarea>'
			+ '</div></div><div class="right"><div><div class="bold label">Concept definition: </div><span class="definitionDesc viewDetailsFld"></span><textarea rows="2" cols="30" id=#{defId} class="definitionDescEdit editDetailsFld nodisplay definition"></textarea><div class="clear"></div>'
			+ '</div><div class="topmargin10"><div class="bold label">Concept example: </div><span class="usageDesc viewDetailsFld"></span><textarea   rows="2" cols="30" id=#{usageId} class="usageDescEdit editDetailsFld nodisplay usage" "></textarea>'
			+ '</div><div id="termImage" ><div id=#{uploadedImageId}  style="width:50px; border:1px solid #cccccc;height:50px;margin-top: 26px;margin-left: 1px;"><img width="50px" height="50px" class="termPicture headerMenuLink" src=#{imagePath}  /></div><div  alignCenter" id=#{changePicId}><a href="javascript:;" class="changeTermPic"  style="font-size: 14px;padding-left:10px;">Image</a></div></div></div>';

		var suggTrmListTmpl = [
		                     '<div class="termSlctFrm topmargin15"><span class="termSuggestion" style=" word-wrap: break-word;">',
		                     '',
		                     '</span><input type="radio" id="',
		                     '',
		                     '" name="',
		                     '',
		                     '" value="',
		                     '',
		                     '" /><div class="votesBar" id="',
		                     '',
		                     '">',
		                     '',
		                     '</div><input type="button" value="Pick as final" class="pickFinalBtn" id="',
		                     '',
		                     '"/></div><div class="clear"></div>'
		                   ];
		
		var newTermSec = ['<div class="termSlctFrm topmargin15 nodisplay newSuggestion" title="Your term"><span class="termSuggestion bold"></span></div><div class="clear"></div><div class="newTermFld"><span class="label">New Term: </span><input id="new" type="radio" value="new" name="',
						'',
						'" /><input type="text" size="20" value="enter your suggestion" class="suggTxt leftmargin5" id=" ',
						'',
						'" /></div><div class="clear"></div><div class="commentsFld"><span class="label">Comments: </span><input type="text" size="30" value="" class="commentTxt leftmargin5" /></div> <div class="votesBtn"><input type="submit" value="Submit" class="sbtVoteBtn leftmargin10" id="',
						'',
						'" /><input type="submit" value="Reject term" class="rejectTermBtn leftmargin20" id="',
						'',
						'" /></div>'
						];
		var editDetailsSec = '<div class="clear"></div><div class="editLinksSec smallFont"><a href="javascript:;" class="padding5 extendPoll">Extend poll</a><a href="javascript:;" class="padding5 updateDate nodisplay">Update date</a> | <a href="javascript:;" class="padding5 editDetails">Edit details</a><a href="javascript:;" class="padding5 updateDetails nodisplay">Update details</a>| <a href="javascript:;" class="padding5 votingDetails">Voting details</a> | <a href="javascript:;" class="padding5 historyDetails">History details</a></div>';
		var deprecatedTermInfoSec = ['<div class="clear"></div><div class=" bigFont" style="color:red" margin-top:-2px;> <span class="width295 label source" style="color:red;padding-left:39px">Deprecated Source: ',
		                             '',
		                             '</span><span class=" width220 label target" >Deprecated Target: ',
		                             '',
		                             '</span></div>'
		                            ];
		
		var classNames = ['purpleBar','greenBar','yellowBar','pinkBar'];
		
		
		var displayTermDetails = function(data) {
			var termDetails = data;
					
			var listLength = (termDetails.length >= termListLimit)?termListLimit:termDetails.length;
		   		for ( var count = 0; count < listLength; count++) {
				var termDtlTmplClone = termDtlTmpl;
				var suggestedTerm = (termDetails[count].suggestedTerm == null || termDetails[count].suggestedTerm == "")?"&nbsp;":termDetails[count].suggestedTerm;
				termDtlTmplClone[1] = termDetails[count].termId;
				if(termDetails[count].deprecatedCount>0){
//					if($j.browser.version=="8.0" || $j.browser.version=="9.0"){
//				termDtlTmplClone[3] = termDetails[count].sourceTerm+"<span style='font-weight:bold;font-size:19px;color:red'><sup>!</sup></span>";
//					}else{
						termDtlTmplClone[3] = termDetails[count].sourceTerm+"<span style='font-weight:bold;font-size:16px;color:red'><sup>!</sup></span>";

//					}
				}else{
					termDtlTmplClone[3] = termDetails[count].sourceTerm;
				}
				termDtlTmplClone[5] = "dataPie_" + termDetails[count].termId;
				termDtlTmplClone[7] = suggestedTerm;
				termDtlTmplClone[9] = termDetails[count].language;
				termDtlTmplClone[11] = termDetails[count].category;
				termDtlTmplClone[13] = termDetails[count].pollExpirationDt;
				termDtlTmplClone[15] = "datepicker"+termDetails[count].termId;
				termDtlTmplClone[17] = termDetails[count].termId;
				$j('#termDtlRowsList').append(termDtlTmplClone.join(""));
				 
				var date = new Date();  
				var currentMonth = date.getMonth();  
				var currentDate = date.getDate();  
				var currentYear = date.getFullYear();
				 
				$j(function() {
					$j("#"+termDtlTmplClone[15] ).datepicker({
						showOn: "button",
						buttonImage: $j("#contextRootPath").val()+"/images/calendar.gif",
						buttonImageOnly: true,
						minDate: new Date(currentYear, currentMonth, currentDate)  //Disables all the past dates and only current or future dats can be selected
					});
				});
			}
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
		
		var partOfSpeechSlctTmpl = ['<option value="',
		                    '',
		                    '" >',
		                    '',
		                    '</option>'
		                    ];
		
		var displayPosList = function(data) {
			var posList = data;
			for ( var count = 0; count < posList.length; count++) {
				var partOfSpeechSlctTmplClone = partOfSpeechSlctTmpl;
				partOfSpeechSlctTmplClone[1] = posList[count].partsOfSpeechId;
				partOfSpeechSlctTmplClone[3] = posList[count].partOfSpeech;
				$j('.termPOS').append(partOfSpeechSlctTmplClone.join(""));
			}
		};
		
		
		var termFormSlctTmpl = ['<option value="',
				                    '',
				                    '" >',
				                    '',
				                    '</option>'
				                    ];
				
		var displayTermFormList = function(data) {
			var trmFormList = data;
			for ( var count = 0; count < trmFormList.length; count++) {
				var termFormSlctTmplClone = termFormSlctTmpl;
				termFormSlctTmplClone[1] = trmFormList[count].formId;
				termFormSlctTmplClone[3] = trmFormList[count].formName;
				$j('.termForm').append(termFormSlctTmplClone.join(""));

			}
		};
		
		var slctOptionsTmpl = ['<option value="',
			                    '',
			                    '" >',
			                    '',
			                    '</option>'
			                    ];
			
		var displayCategoryList = function(data) {
			var termCatList = data;
			for ( var count = 0; count < termCatList.length; count++) {
				var termCatSlctTmplClone = slctOptionsTmpl;
				termCatSlctTmplClone[1] = termCatList[count].categoryId;
				termCatSlctTmplClone[3] = termCatList[count].category;
				$j('.category').append(termCatSlctTmplClone.join(""));
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
			$j('#termDtlRowsList .row ').click(function(){
				var row = $j(this);
				$j("#star_"+termId).hide(); //l
				$j("#error_"+termId).hide(); //l
				$j("#sourceId_"+termId).removeClass('errorBorder'); //l
				if($j(this).hasClass('twistie_close')){
					$j(this).parent().next().next().show();
					$j(this).removeClass("twistie_close");
					$j(this).addClass("twistie_open");
					var termId=$j(this).attr("termId");

					$t.getTermAttributes(termId,{
						success:function(data){
							if(Boolean(data)){
								var termInfo = data.termInfo;
								var defaultImg=$j("#contextRootPath").val()+"/images/default.jpg";
								var detailsSec = row.parent().siblings('.viewDtlsRow[termId='+row.attr('termId')+'] ');
								var termImage=(termInfo.photoPath == null)?defaultImg:termInfo.photoPath;
							var termAttrIds={
										sourceId:'sourceId_'+termId, //l
										targetId:'targetId_'+termId, //l
										   posId:'posId_'+termId,
										   formId:'formId_'+termId,
										   categoryId:'categoryId_'+termId,
										   notesId:'notesId_'+termId,
										   defId:'defId_'+termId,
										   usageId:'usageId_'+termId,
										   changePicId:'changePicId_'+termId,
										   uploadedImageId:'uploadedImageId_'+termId,
										   errorId:'errorId_'+termId, //l
										   starId:'starId_'+termId, //l
										   imagePath:termImage
							
							}
							
								var editTermTmpl=new Template(termAttrTmpl).evaluate(termAttrIds);
								detailsSec.html(editTermTmpl);
								var deprecatedTermInfoSecClone= deprecatedTermInfoSec;
									if(termInfo != null){
									var termUsage = (termInfo.termUsage == null)?"":termInfo.termUsage;
									var conceptDefinition = (termInfo.conceptDefinition == null)?"":termInfo.conceptDefinition;
									var termNotes = (termInfo.termNotes == null)?"":termInfo.termNotes;
									var categoryName = (termInfo.termCategory == null || termInfo.termCategory.category== null)?"":termInfo.termCategory.category;
									var formName =  (termInfo.termForm == null || termInfo.termForm.formName == null)?"":termInfo.termForm.formName;
									var partOfSpeech = (termInfo.termPOS == null || termInfo.termPOS.partOfSpeech == null)?"":termInfo.termPOS.partOfSpeech;
									var targetTerm = (termInfo.suggestedTerm == null || termInfo.suggestedTerm == null)?"":termInfo.suggestedTerm;
									var termBeingPolled = (termInfo.termBeingPolled == null || termInfo.termBeingPolled == null)?"":termInfo.termBeingPolled;
									detailsSec.find('.sourceDesc').html(termBeingPolled); //l 
									detailsSec.find('.sourceDescEdit').html(termBeingPolled); //l
									if(termInfo.termUsage ==null || termInfo.termUsage ==""){
											detailsSec.find("#termImage").css("margin-top","0px");
									}else{
										detailsSec.find("#termImage").css("margin-top","-23px");
									}
									detailsSec.find('.targetDesc').html(targetTerm); //l
									detailsSec.find('.targetDescEdit').html(targetTerm); //l
									detailsSec.find('.usageDesc').html(termUsage);
									detailsSec.find('.usageDescEdit').html(termUsage);
									detailsSec.find('.definitionDesc').html(conceptDefinition);
									detailsSec.find('.definitionDescEdit').html(conceptDefinition);
									detailsSec.find('.domainDesc').html(termNotes);
									detailsSec.find('.domainDescEdit').html(termNotes);
									detailsSec.find('.categoryDesc').html(categoryName);
									detailsSec.find('.formDesc').html(formName);
									detailsSec.find('.termTypeDesc').html(partOfSpeech);
									
									var origNotes = detailsSec.find('.domainDesc').html();
									if(origNotes.length > 90){
									var finalText = origNotes.substring(0,87);
									finalText=finalText+"...";
									detailsSec.find('.domainDesc').text(finalText); 
									detailsSec.find('.domainDesc').attr("title",origNotes);
									}
									var origDef = detailsSec.find('.definitionDesc').html();
									if(origDef.length > 50){
									var finalText = origDef.substring(0,49);
									finalText=finalText+"...";
									detailsSec.find('.definitionDesc').text(finalText); 
									detailsSec.find('.definitionDesc').attr("title",origDef);
									}
									var origExmpl = detailsSec.find('.usageDesc').html();
									if(origExmpl.length > 50){
									var finalText = origExmpl.substring(0,49);
									finalText=finalText+"...";
									detailsSec.find('.usageDesc').text(finalText); 
									detailsSec.find('.usageDesc').attr("title",origExmpl);
									}

								}
								if(data.suggestedTermDetails != null && data.suggestedTermDetails.length > 0){
									for(var i=0;i<data.suggestedTermDetails.length;i++){
										var numRand = Math.floor(Math.random()*101);
										var suggTrmListTmplClone = suggTrmListTmpl;
										suggTrmListTmplClone[1] = data.suggestedTermDetails[i].suggestedTerm;
										suggTrmListTmplClone[3] = data.suggestedTermDetails[i].suggestedTermId;
										suggTrmListTmplClone[5] = row.attr('termId');
										suggTrmListTmplClone[7] = data.suggestedTermDetails[i].suggestedTerm;
										suggTrmListTmplClone[9] = "barId_"+row.attr('termId')+i;
										suggTrmListTmplClone[11] = data.suggestedTermDetails[i].noOfVotes;
										suggTrmListTmplClone[13] = data.suggestedTermDetails[i].suggestedTermId;
										detailsSec.append(suggTrmListTmplClone.join(""));

									}

								}
								newTermSec[1] = row.attr('termId');
								newTermSec[3] = "sugg_"+row.attr('termId');
								newTermSec[5] = "submitVote"+row.attr('termId');
								newTermSec[7] = "rejectVote"+row.attr('termId');
								detailsSec.append(newTermSec.join(""));
								

								if(data.deprecatedTermInfo.length>0){
							       var	finalSource="";
							        var finalTarget="";
							        for(var i=0;i<data.deprecatedTermInfo.length;i++){
							        	if(data.deprecatedTermInfo[i].deprecatedSource!=null &&  $j.trim(data.deprecatedTermInfo[i].deprecatedSource)!="")
							        		finalSource=finalSource+data.deprecatedTermInfo[i].deprecatedSource+"\n" ;
							        	if(data.deprecatedTermInfo[i].deprecatedTarget!=null &&  $j.trim(data.deprecatedTermInfo[i].deprecatedTarget)!="")
							        		finalTarget=finalTarget+data.deprecatedTermInfo[i].deprecatedTarget+"\n";
							        }

						        finalSource=  finalSource.substring(0,finalSource.lastIndexOf("\n"));
						        finalTarget=  finalTarget.substring(0,finalTarget.lastIndexOf("\n"));

						        deprecatedTermInfoSecClone[1]=biography = $j.trimText(finalSource, 30);
						        deprecatedTermInfoSecClone[3]=$j.trimText(finalTarget, 30);
							   detailsSec.append(deprecatedTermInfoSecClone.join(""));
							$j(".source").hover(function(){
								if($j(this).attr("title") == undefined){
									$j(this).attr("title",finalSource);
								}
								
						   	});
							$j(".target").hover(function(){
								if($j(this).attr("title") == undefined){
									$j(this).attr("title",finalTarget);
								}
								
						   	});
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
								if(data.suggestedTermDetails != null && data.suggestedTermDetails.length > 0){
									for(var i in data.suggestedTermDetails){
										if( data.suggestedTermDetails[i].noOfVotes>largest.val ){
											largest.val = data.suggestedTermDetails[i].noOfVotes;

										}
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

								/*Hiding Votes Bar and other modules from User Screen before he votes*/
								if($j("body").hasClass('userTermsList')){
									
									
									detailsSec.find('.votesBar').hide();
									detailsSec.find('.pickFinalBtn').hide(); 
									detailsSec.find('.editLinksSec').hide();

								}
								
								$j(".ui-datepicker-trigger").attr("alt","Calendar");

								detailsSec.find('.sbtVoteBtn').click(function(){
									//detailsSec.find('.votesBar').show();
									if($j("body").hasClass('userTermsList')){
										var termId = $j(this).attr("id");
										termId = termId.replace("submitVote","");
										var suggestedTermId = null;
										if(detailsSec.find('input:radio[name=row.attr("termId")]:checked').val()=='' || detailsSec.find('input:radio[name=row.attr("termId")]:checked').val()==null){alert("Please select one option");}
										else{
											if(detailsSec.find('input:radio[name=row.attr("termId")]:checked').val()=="new"){
												suggestedTermId = detailsSec.find('input:radio[name=row.attr("termId")]:checked').attr("id");
												detailsSec.find(".newSuggestion span").html(detailsSec.find('.suggTxt').val());
												detailsSec.find(".newSuggestion").show();

											}
											if(detailsSec.find('input:radio[name=row.attr("termId")]:checked').val()!="new"){
												suggestedTermId = detailsSec.find('input:radio[name=row.attr("termId")]:checked').attr("id");
												detailsSec.find('input:radio[name=row.attr("termId")]:checked').prev().css("font-weight","bold");
											}

											//alert(detailsSec.find('input:radio[name=row.attr("termId")]').length);

											voteTerm(suggestedTermId, termId);
											detailsSec.find('.votesBar').each(function(i){
												$j(this).show();
												$j(this).attr("id"," ");
												detailsSec.find('.votesBtn').hide();
												detailsSec.find('.commentsFld').hide();
												detailsSec.find('.newTermFld').hide();
												detailsSec.find('input').hide();
											});
										}
									}
								});

								detailsSec.find('.rejectTermBtn').click(function(){
									row.parent().remove();
									detailsSec.remove();
								});

								detailsSec.find('.suggTxt').focus(function(){
									$j(this).val('');
								});
								detailsSec.find('.suggTxt').blur(function(){
									if($j(this).val() == '')
										$j(this).val('enter new suggestion');
								});
								
								$j(".changeTermPic").click(function(){
									var id=$j(this).parent().attr("id");
									 pictureId= id.replace("changePicId_","");
									 	showBrowsePic();
								});
								$j(".termPicture").hover(function(){
									 modalRender.bubble(".termPicture","click for full image","left center","right center");
							   	});
								
								$j(".termPicture").click(function(){
									$j('.termPicture').trigger('mouseleave');

										$t.getTermAttributes(termId,{
										success:function(imgData){
											if(data!=null  && data.termInfo!=null){
												var hoverImagepath=(imgData.termInfo.photoPath == null)?defaultImg:imgData.termInfo.photoPath;
												showTermPicture();
												$j("#showTermImage").find("img").attr('src', hoverImagepath);
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

								//Edit Details
								detailsSec.find('.editDetails').click(function(){
									$j(".termAttr").addClass("editHeight");
									oldtargetTerm = $j("#targetId_" + termId).val(); //l
									detailsSec.find('.editDetailsFld').show();
									detailsSec.find('.viewDetailsFld').hide();
									$j("#errorId_"+termId).hide();  //l
									$j("#sourceId_"+termId).removeClass('errorBorder'); //l
									$(this).hide();
									detailsSec.find('.updateDetails').show();
									//detailsSec.find('.termAttr').css("padding-bottom","195px");
									detailsSec.find("#termImage").css("margin-top","-28px");

									// Parts of Speech Picklist
									$t.getPOSList({
										success:function(data){
											displayPosList(data);
											var selectedPos = detailsSec.find('.termTypeDesc').html();
											$j("select[name='termPOS'] option").each(function(){
												if ($j(this).text() == selectedPos)
													$j(this).attr("selected","selected");
											});
										},
										error: function(xhr, textStatus, errorThrown){
											console.log(xhr.responseText);
											if(Boolean(xhr.responseText.message)){
												console.log(xhr.responseText.message);
											}
										}
									});

									// Term Form Picklist
									$t.getFormList({
										success:function(data){
											displayTermFormList(data);
											var selectedForm = detailsSec.find('.formDesc').html();
											$j("select[name='termForm'] option").each(function(){
												if ($j(this).text() == selectedForm)
													$j(this).attr("selected","selected");
											});

										},
										error: function(xhr, textStatus, errorThrown){
											console.log(xhr.responseText);
											if(Boolean(xhr.responseText.message)){
												console.log(xhr.responseText.message);
											}
										}
									});

									// Program Project Picklist
									$t.getCategoryList({
										success:function(data){
											displayCategoryList(data);
											var selectedCat = detailsSec.find('.categoryDesc').html();
											$j("select[name='category'] option").each(function(){
												if ($j(this).text() == selectedCat)
													$j(this).attr("selected","selected");
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
								detailsSec.find('.extendPoll').click(function(){
									row.find('.editDate').show();
									row.find('.viewDate').hide();
									var curDt = row.find('.viewDate').text();
									row.find('.editDate input').val(curDt);
									detailsSec.find('.updateDate').show();
									$(this).hide();
									
									row.find(".hasDatepicker").click(function(event){
										$j(this).focus();
										event.stopPropagation(); 
									});
									
								});
								detailsSec.find('.updateDate').click(function(){
									var editedDate =row.find('.hasDatepicker').val();
									if(isDate(editedDate)){
										row.find('.editDate').hide();
										row.find('.viewDate').show();
//										detailsSec.hide();
										row.removeClass("twistie_open");
										row.addClass("twistie_close");
										var termId = row.attr('termId');
										var pollDate = $j("#datepicker"+termId).val();
										extendPoll(pollDate,termId);
										row.parent().hide();
									}else{
										alertMessage("#dateAlet");
										row.find('.hasDatepicker').val("");

									}
									
									
								});
								detailsSec.find('.updateDetails').click(function(){
									var termId = row.attr('termId');
									var sourceTermDiv=$j("#sourceId_" + termId).val();
									var targetTerm = $j("#targetId_" + termId).val();
									var showTargetTerm = "";  /*=(targetTerm=="")?"&nbsp;":targetTerm;*/
									if($j.trim(sourceTermDiv) !=""){
										if($j.trim(targetTerm)!=""){
											showTargetTerm=targetTerm;
										}
										else{
											if($j.trim(oldtargetTerm)==""){
											showTargetTerm="&nbsp";
											}
											else{
											showTargetTerm=oldtargetTerm;	
											}
										}
									detailsSec.hide();
									row.removeClass("twistie_open");
									row.addClass("twistie_close");
									row.find('.editDate').hide();
									row.find('.viewDate').show();
									
									$j("#errorId_"+termId).hide();
									var sourceTerm = $j("#sourceId_" + termId).val();// lalitha
									var showSourceTerm=(sourceTerm == "")?"&nbsp;":sourceTerm;
									row.find('.targetTerm').html(showTargetTerm).show();
									row.find('.pollTerm').html(showSourceTerm).show();
									saveTermDetails(row.attr('termId'));
									}else{
										$j("#errorId_"+termId).show();
									$j("#sourceId_"+termId).addClass('errorBorder');
								
									}
									row.find('.editDate').hide();
									row.find('.viewDate').show();
									
								});
								detailsSec.find('.editSourceTerm').click(function(){
									$j("#errorId_"+termId).hide();
									$j("#sourceId_"+termId).removeClass('errorBorder');
								});
								
								detailsSec.find('.votingDetails').click(function(){
									 var termId = row.attr('termId');
										if($j('#votiongDetailsTab').length >0){
											var count = 0;
											var oTable = $j('#votiongDetailsTab').dataTable({
												"bProcessing": true,
												"sServerMethod": "GET",
												"bAutoWidth": false,
												"bDestroy": true,
												"sAjaxDataProp":"termData",
												"sAjaxSource":  $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getvotingStatus/"+termId,
												"aoColumns": [
												              { "mDataProp": "userName","sWidth": "20%"},
												              { "mDataProp": "votingStatus","sWidth": "20%" },
												              { "mDataProp": "votedTerm","sWidth": "20%","sClass": "alignLeft"},
												              { "mDataProp": "userComments","sWidth": "25%",
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
												              { "mDataProp": "votedDate","sWidth":"15%"}
												              ],
												              "iDisplayLength": 10
											});
											
										}
										$j('.dataTables_length').hide();
										showVotingDetails(data);

								});
								detailsSec.find('.historyDetails').click(function () {
                                    var termId = row.attr('termId');
                                    if ($j('#historyDetailsTab').length > 0) {
                                        var count = 0;
                                        var oTable = $j('#historyDetailsTab').dataTable({
                                            "bProcessing": true,
                                            "sServerMethod": "GET",
                                            "bAutoWidth": false,
                                            "bDestroy": true,
                                            "sAjaxDataProp": "historyData",
                                            "sAjaxSource": $j("#contextRootPath").val() + "/teaminology_ctrler/teaminology/getHistoryDetails/" + termId,
                                            "aoColumns": [
                                                          { "mDataProp": "userName", "sWidth": "9%"},
                                                          { "mDataProp": "historySourceTerm", "sWidth": "9%" },
                                                          { "mDataProp": "changedTargetTerm", "sWidth": "9%" },
                                                          { "mDataProp": "termsPOS", "sWidth": "5%"},
                                                          { "mDataProp": "conceptDefinition", "sWidth": "10%" },
                                                          { "mDataProp": "termForm", "sWidth": "5%" },
                                                          { "mDataProp": "termStatus", "sWidth": "5%" },
                                                          { "mDataProp": "termCategory", "sWidth": "9%" },
                                                          { "mDataProp": "termDomain", "sWidth": "9%" },
                                                          { "mDataProp": "termNotes", "sWidth": "9%" },
                                                          { "mDataProp": "targetTermPOS", "sWidth": "5%" },
                                                          { "mDataProp": "termUsage", "sWidth": "10%" },
                                                          { "mDataProp": "historyDate", "sWidth": "9%"}
                                                          ],
                                                          "iDisplayLength": 10
                                        });
                                    }
                                    $j('.dataTables_length').hide();
                                    showHistoryDetails(data);

                                });
								
								$j(".pickFinalBtn").click(function(){
									var suggestedTermId = $j(this).attr("id");
									var termId = row.attr('termId');
									finaliseTerm(suggestedTermId);
								});

								$j(".rejectTermBtn").click(function(){
									var termId = $j(this).attr("id");
									termId = termId.replace("rejectVote","");
									rejectTerm(termId);
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
				}
			});



		};
		
		
		
		ctx.bind("showTermDetails",function(event,criteria) {
//			if(Boolean(criteria) && Boolean(criteria.companyIds) && Boolean(criteria.langIds)){
//				return;
//			}
			
			var pageNum  = criteria.pageNum;

			$t.sortOrFilterExpPollTerms(criteria.colName, criteria.sortOrder,criteria.langIds,criteria.companyIds,criteria.pageNum,{
				success:function(data){
					$j('#termDtlRowsList').empty();
					var termDetails = data;
					if(termDetails != null){
						displayTermDetails(data);
					}
					if(termDetails != null){
						var startLimit = 0;
						var endLimit = 0;
						
						if(pageNum == 0){
							totalTerms_admin = termDetails.length;
							
							noOfPages_admin = Math.round(termDetails.length/10) ;
							noOfPages_admin = (termDetails.length%10 < 5 && termDetails.length%10 > 0)? noOfPages_admin+1:noOfPages_admin;
							startLimit = 1;
							endLimit = (termListLimit > totalTerms_admin)? totalTerms_admin : termListLimit;
						}else{
							startLimit = ((pageNum - 1)* termListLimit)+1;
							var tempLimit  = (pageNum) * termListLimit;
							endLimit =(parseInt(tempLimit) > parseInt(totalTerms_admin))? totalTerms_admin :tempLimit ;
						}
						var length = (data.length >= termListLimit)?termListLimit:data.length;
						for(var i = 0; i< length;i++){
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
							
				
							
							dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='14' showShadow='0' animation='0' slicingDistance='0' paletteColors='#C9CCCB, #00A4D6' showBorder='0' bgAlpha='30' showToolTip='0'>");
							dataXML.push("<set label='Services' value='"+(invites-votesPerTerm)+"' />");
							dataXML.push("<set label='Hardware' value='"+votesPerTerm+"' />");
							dataXML.push("</chart>");
							//$j(ChartRender.twoDPieChart(termDetails[i].termId,pieChartHeight,pieChartWidth, "dataPie_"+termDetails[i].termId, "pieData"+i+".xml" ));
							ChartRender.twoDPieChart(termDetails[i].termId,pieChartHeight,pieChartWidth, "dataPie_"+termDetails[i].termId, dataXML );
							modalRender.bubble("#dataPie_"+termDetails[i].termId,votesPerTerm+"/"+invites+"<br>"+"voted / invited to vote","bottom center","top center");

						}
						$j("#pagination").show();
						$j("#rangeOfTerms_admin").html(startLimit + "-"+ endLimit);
						$j("#totalTerms_admin").html(totalTerms_admin);
						var totalTerms1 = new String(totalTerms_admin);
						var totalTerms2 = insertCommmas(totalTerms1);
						
						$j(".moreNum").html(' '+totalTerms2+' ');
						pagination(noOfPages_admin, pageNum);
						
						$j('div.pollTerm').each(function(i){
							var origText = $j(this).text();
							if(origText.lastIndexOf("!")>0){
								srcText=origText.substr(0,origText.lastIndexOf("!"));
								var supscript="";
								if($j.browser.version=="8.0" || $j.browser.version=="9.0"){
									 supscript="<span style='font-weight:bold;font-size:19px; color:red'><b>!</b></span>";

								}
								else{
									supscript="<span style='font-weight:bold;font-size:16px; color:red'><b>!</b></span>";
								}
							   if(srcText.length > 18){
								var finalText = srcText.substring(0,16);
								finalText=finalText+"...";
								$j(this).html(finalText+supscript);
								$j(this).attr("title",srcText); 
							}
							}
							else
								{
								 $j(this).addClass("toppadding5");
								if(origText.length > 18){
									var finalText = origText.substring(0,16);
									finalText=finalText+"...";
									$j(this).text(finalText);
									$j(this).attr("title",origText); 
								}
								}		
							
						});
						$j('div.targetTerm').each(function(i){
							var origText = $j(this).text();
							if(origText.length > 16){
								var finalText = origText.substring(0,14);
								finalText=finalText+"...";
								$j(this).text(finalText); 
								$j(this).attr("title",origText);
							}
						});
					}	else{
						$j("#rangeOfTerms_admin").html(0 + "-"+ 0);
						$j("#totalTerms_admin").html(0);
						$j(".moreNum").html(' '+0+' ');
						$j('#termDtlRowsList').html('<span style="text-align: center; display: block; border-bottom: 1px solid #DDDDDD; font-size:12px;padding-top:10px; padding-bottom:10px;">No expired polls to display</span>' );
						pagination(0, 0);
						$j("#pagination").hide();
					}
					

					bindEvents();
					if($j("body").hasClass('userTermsList')){
						$j(".trmLanguage").each(function(){
						$j(this).hide();
						});
						}
						if($j("body").hasClass('adminOvr')){
						$j(".trmCategory").each(function(){
						$j(this).hide();
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
			$(function() {
				$( "#datepicker" ).datepicker({
					showOn: "button",
					buttonImage: "images/calendar.gif",
					buttonImageOnly: true
				});
			});
		});

	};
	
	
	
	var generatePieChart = function(data, listLength, listLimit){
		var termDetails = data;
		if(termDetails != null){
			var length = (termDetails.length >= termListLimit)?termListLimit:termDetails.length;
			for(var i = 0; i< length;i++){
				if(i < listLimit)
					continue;
				var dataXML = new Array();
				var invites = (termDetails[i].invites == null)?0:termDetails[i].invites;
				var votesPerTerm = (termDetails[i].votesPerTerm == null)?0:termDetails[i].votesPerTerm;
				
				// Start of chart object
				dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='14' showShadow='0' animation='0' paletteColors='#C9CCCB, #00A4D6' showBorder='0' bgAlpha='30' showToolTip='0'>");
				dataXML.push("<set label='Invited' value='"+invites+"' />");
				dataXML.push("<set label='Voted' value='"+votesPerTerm+"' />");
				dataXML.push("</chart>");
				ChartRender.twoDPieChart(termDetails[i].termId,pieChartHeight,pieChartWidth, "dataPie_"+termDetails[i].termId, dataXML );
				
				modalRender.bubble("#"+termDetails[i].termId,votesPerTerm+"/"+invites+"<br>"+"voted / invited to vote","bottom center","top center");
				
			}
		}	
		
	};
	
	var triggerPlugnTrmDtls = function(){
		$j('#termDtlRowsList').empty();
		ChartRender.destroyTwoDPieChart();
		 var criteria = {
					'name':null,
					'sortOrder':null,
					'langIds' :null,
					'companyIds':null,
					'pageNum': 0
			};
//		 $j("#trmDtlSectionHead div").each(function(index) {
//				$j(this).removeClass("ascending descending");
//				$j(this).find('.sort').remove();
//				$j(this).attr('sortOrder','ASC');
//			});
		$j("#termDetails").trigger("showTermDetails", criteria);
	};
	
	var finaliseTerm = function(suggestedTermId){
	    	$t.finalizeTerm(suggestedTermId, {
				success : function(data) {
					triggerPlugnTrmDtls();
					
				},
				error : function(xhr, textStatus, errorThrown) {
					console.log(xhr.responseText);
					if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
					}
				}
			});
    };
    
//    var voteTerm =  function(suggestedTermId, termId){
//    	var termTranslation = new Object();
//    	var suggested_term_id = null;
//    	var suggestedTerm = null;
//    	var comment = null;
//    	if(suggestedTermId == "new"){
//    		suggested_term_id = null;
//    		suggestedTerm = $j(".suggTxt").val();
//    	}else{
//    		suggested_term_id = suggestedTermId;
//    		suggestedTerm = null;
//    	}
//    	termTranslation.termTranslationId = suggested_term_id;
//    	termTranslation.termId = termId;
//    	termTranslation.suggestedTerm = suggestedTerm;
//    	termTranslation.suggestedTermLangId = null;
//    	termTranslation.vote = null;
//    	termTranslation.userId = null;
//    	termTranslation.createDate =null;
//    	termTranslation.updatedBy = null;
//    	termTranslation.updateDate = null;
//    	termTranslation.comment = ($j(".commentTxt").val().trim() == "")?null:$j(".commentTxt").val();
//    	termTranslation.isActive = null;
//    	
//    	var termTranslationParameter =  Object.toJSON(termTranslation);
//		$t.voteTerm(termTranslationParameter, {
//			success : function(data) {
//
//			},
//			error : function(xhr, textStatus, errorThrown) {
//				console.log(xhr.responseText);
//				if(Boolean(xhr.responseText.message)){
//					console.log(xhr.responseText.message);
//				}
//			}
//		});
//    	
//    };
    
//    var rejectTerm = function(termId){
//    	if(termId != null){
//    		
//    		$t.rejectTerm(termId, {
//    			success : function(data) {
//    		
//    			},
//    			error : function(xhr, textStatus, errorThrown) {
//    				console.log(xhr.responseText);
//    				if(Boolean(xhr.responseText.message)){
//    					console.log(xhr.responseText.message);
//    				}
//    			}
//    		});
//    	}
//    	
//    };
    
    var extendPoll = function(pollDate,termId){
    	var date =new Date(pollDate);
    	var day=date.getDate();
    	var month=date.getMonth()+1;
    	var year =date.getFullYear();
    	var finalDate=month+"-"+day+"-"+year;
		$t.updateExtendPoll(finalDate,termId, {
			success : function(data) {
				triggerPlugnTrmDtls();
			},
			error : function(xhr, textStatus, errorThrown) {
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
      };
      
      var showBrowsePic = function() {
			// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
			$j( "#uploadPicBrwse:ui-dialog" ).dialog( "destroy" );
		
			$j( "#uploadPicBrwse" ).dialog({
				height: 200,
				width: 400,
				modal: true,
				close: function(event, ui) { 
					$j("#uploadPic").val('');
				}
			
			});
			$j("#errorMsg").hide();
		};
		
		
		var showTermPicture = function() {
			// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
			$j( "#showTermPicBrwse:ui-dialog" ).dialog( "destroy" );
			$j( "#showTermPicBrwse" ).dialog({
				height: 420,
				width:630,
				modal: true,
				close: function(event, ui) {
					
				}
			
			});
			
		};
		
			
  	var showVotingDetails = function() {
		$j( "#votiongDetailsDiv:ui-dialog" ).dialog( "destroy" );
	
		$j( "#votiongDetailsDiv" ).dialog({
			height: 500,
			width:700,
			modal: true
		});
		  $j('#votiongDetailsTab').hover( function(){
          	
          	$j('#votiongDetailsTab').find('.subdata').each(function(){
          		modalRender.bubble('#'+$j(this).attr('id'),  $j(this).find('.tip-text').text(), "left center", "right center");
          	});
        });
	};
	var showHistoryDetails = function () {
	    $j("#historyDetailsDiv:ui-dialog").dialog("destroy");

	    $j("#historyDetailsDiv").dialog({
	        height: 600,
	        width: 1230,
	        modal: true,
	    });
	    $j('#historyDetailsTab').hover( function(){
	        $j($j('#historyDetailsTab').find('.comment')).each(function(){
	            modalRender.bubble('#'+$j(this).attr('id'),  $j(this).find('.tip-text').text(), "left center", "right center");
	        });
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
	$j("#uploadTermPic").click(function(e){
		if($j('#uploadPic').val()==""){
		alertMessage("#uploadTerm");
   }else{
	e.preventDefault();
	fileName =$j("#uploadPic").val();
	var regexp=/\.(jpg|JPG|jpeg|JPEG|png|PNG|bmp|BMP|gif|GIF)$/i;
	if (fileName.length > 0)
	 {
	 if (regexp.test(fileName))
	 {
		 ajaxFunction();	
		 $j("#uploadPicForm").submit();
	 }
	 else
	 {
		 alertMessage("#InvaliduploadPic");
	 }
	 }
	}
	});

	var ajaxFunction = function(){
		$t.uploadProfile({
			success:function(data){
				processStateChange(data);
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		
	};
	
	var processStateChange = function(uploadData){
		
		if(uploadData == null){
			setTimeout(function(){
				ajaxFunction();
			},100);
		}else{
				var isNotFinished = uploadData.isFinished
				var myBytesRead = uploadData.bytesRead
				var myContentLength = uploadData.contentLength
				var myPercent = uploadData.percent;
				fileName=uploadData.fileName;
				if ((isNotFinished == null) && (myPercent == null)){
					setTimeout(function(){
						ajaxFunction();
					},100); 
				} else {

					if (myPercent != null) {
						setTimeout(function(){
							ajaxFunction();
						},100); 
					} else {
							var ext="JPG";
							if(fileName!=null && fileName.lastIndexOf(".") > 0)
								ext=fileName.substr(fileName.lastIndexOf(".")+1);
							if(fileName!=null && fileName.lastIndexOf('\\') > 0){
								fileName=fileName.substr(fileName.lastIndexOf('\\')+1);
							}
							photoPath=$j("#contextRootPath").val()+"/images/term_images/"+fileName;
							 if(pictureId!=null){
								$j("#uploadedImageId_"+pictureId).find("img").attr('src', photoPath);
						    	var imageName=fileName+'.'+ext;
						    	$t.setTermPhotoPath(pictureId,imageName,{
									 success : function(data) {
						      },
								error: function(xhr, textStatus, errorThrown){
									console.log(xhr.responseText);
									if(Boolean(xhr.responseText.message)){
										console.log(xhr.responseText.message);
									}
								}
							});
						         pictureId=null;
						 }else{
							         	$j("#uploadedImage").attr('src', photoPath);
						    	  	
						    }
						    photoPath=null;
						    $j('#uploadPicBrwse').dialog('destroy');
					}
				}
		}
	}; 
      var saveTermDetails = function(termId){
			// Create TermInformation Object
			var termDtls = new Object();
			termDtls.termId = parseInt(termId);
			termDtls.termNotes = $j("#notesId_"+termId).val();
			termDtls.conceptDefinition = $j("#defId_"+termId).val();
			termDtls.termUsage = $j("#usageId_"+termId).val();
			var sourceTerm = $j("#sourceId_" + termId).val();
			termDtls.sourceTerm=$j("#sourceId_" + termId).val();
			var targetTerm  = $j("#targetId_"+termId).val()
			if($j.trim(targetTerm)!=""){
				termDtls.topSuggestion = targetTerm/*$j("#targetId_"+termId).val()*/;
			}else{
				termDtls.topSuggestion=oldtargetTerm;
			}
			if($j("#categoryId_"+termId+" :selected").val()==""){
				termDtls.termCatagoryId =null;
			}else{
			termDtls.termCatagoryId = parseInt($j("#categoryId_"+termId+" :selected").val());
			}
			if($j("#posId_"+termId+" :selected").val()==""){
				termDtls.termPOSId=null;
			}else{
			termDtls.termPOSId = parseInt($j("#posId_"+termId+" :selected").val());
			}
			if($j("#formId_"+termId+" :selected").val()==""){
				termDtls.termFormId =null;
			}else{
			termDtls.termFormId = parseInt($j("#formId_"+termId+" :selected").val());
			}
			termDtls.termProgramId = null;
			

			var termDtlsParameter =  Object.toJSON(termDtls);
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
					if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
					}
				}
			});
			
	      };
	      
})(jQuery);