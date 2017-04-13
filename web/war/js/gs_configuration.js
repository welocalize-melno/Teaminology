(function(window, $j) {
	var validateGsCreaditials;

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
	
	
	var gsCredintialsTmpl = '<form id="editDetails" name="editCredintials"><div id="urlRequired" class="error floatleft nodisplay" style="margin-top:-16px;padding-left:136px;"">Url required</div><div class="clear"></div><div  class="" style="padding-bottom: 20px;"><div class="label width130 floatleft" style="padding-top:3px;">Api Url<span class="required redTxt nodisplay">*</span>:</div><div class="floatleft" style="width:400px;"><div class="viewFld url" style="width:400px;" id="viewUrl"></div><div><input type="text"  value="" class="editFld url  nodisplay " style="width:400px;" id="urlFld" name="urlFld" /> </div></div></div><br class="clear" />'
		+' <div id="unameRequired" class="error floatleft nodisplay" style="margin-top:-14px;padding-left:136px;">UserName required</div><div class="clear"></div><div  class="" style="padding-bottom: 20px;"><div class="label width130 floatleft" style="padding-top:3px;">UserName<span class="required redTxt nodisplay">*</span>:</div><div class="floatleft" style="width:200px;"><div class="viewFld uname" style="width:200px;margin-top:5px;" id="viewUname"></div><div><input type="text"  value="" class="text350 editFld uname  nodisplay " id="unameFld" name="unameFld" /> </div></div></div><br class="clear" />'
		+' <div id="pwdRequired"  class="error floatleft nodisplay" style="margin-top:-14px;padding-left:136px;">Password required</div><div class="clear"></div><div  class="pwd" style="padding-bottom: 20px;"><div class="label width130 floatleft" style="padding-top:3px;">Password<span class="required redTxt nodisplay">*</span>:</div><div class="checkpwd floatleft" style="width:200px; margin-bottom: 10px;"><div class="viewFld password" style="width:200px;margin-top:5px;" id="viewPassword"></div><div><input type="password"  value="" class="text350 editFld password nodisplay" id="passwordFld" name="passwordFld"/></div><div class="pwdCheck" style="padding-left:270px;margin-top:-20px;"><input  class="checkbox" type="checkbox"  name="pwdCheckBox"  title="view password" id="pwdCheckBox"></div> </div></div><br class="clear" />'
		+'<div style="padding-bottom: 20px;"><div class="alignRight toppadding5"><a href="javascript:;" class="editDetails rightmargin15">Edit Details</a></div>'
		+'<div class="savePanel nodisplay" style="margin-left: 150px;margin-top: 10px;"><input type="button" value="Submit" id="saveCredintials" class="commonBtn padding5" /><input type="button" value="Cancel" id="cancelBtn" class="leftmargin20 commonBtn padding5" /></div>'
		+'<div class="btnPanel nodisplay" style="margin-left: 150px;margin-top: 10px;"><input type="button" value="Update" id="save" class="commonBtn padding5" /><input type="button" value="Cancel" id="cancelUpdate" class="leftmargin20 commonBtn padding5" /></div></form>';
	
	var displayGSCredintials = function(data){
		$j(".regFrm").html("");
		$j(".regFrm").append(gsCredintialsTmpl);
		var url= ((data.url) == null || (data.url)== "null")?" ":data.url;
		var userName = ((data.userName) == null || (data.userName)== "null")?" ":data.userName;
		var password = ((data.password) == null || (data.password)== "null")?" ":data.password;
		if( data.userName == null && data.url == null && data.password== null){
			$j("#editDetails .editFld").show();
			$j("#editDetails .viewFld").hide();
			$j("#pwdCheckBox").hide();
			$j(".btnPanel").hide();
			$j(".savePanel").show();
			$j(".editDetails").hide();
			$j(".required").show();
		}else{
	
		 $j("#viewUrl").html(url);
		 $j("#urlFld").val(url);
		$j("#viewUname").html(data.userName);
		$j("#unameFld").val(data.userName);
		$j("#viewPassword").html("***********");
		$j("#passwordFld").val(data.password);
		}
			
		$j("#unameFld").focus(function(){
			 $j(".proUserExists").hide();
			 $j("#unameReq").hide();
			
	});
	
	$j("#emailFld").focus(function(){
		 $j(".proEmailExists").hide();
		 $j("#emailFldReq").hide();
		 
	});
	};

	var saveGSCreditials = function(){
		// Create user Object
		
		var date = new Date();
        var curr_date = date.getDate();
        var curr_month = date.getMonth();
        curr_month = curr_month + 1;
        var curr_year = date.getFullYear();
        date= curr_date + '/'+ curr_month + '/'+ curr_year;
        var flag=true;

			 // new Object();
   	
          
	       var gsCredintials = new Object();
	       var url=$j("#urlFld").val();
	       var userName=$j("#unameFld").val();
	       var password=$j("#passwordFld").val();
	        if($j.trim(url)==""){
        	 $j("#urlRequired").show();
        	 $j('#urlFld').addClass("error");
        	 flag=false;
         }else{
        	  gsCredintials.url= url;
        	  $j('#urlFld').removeClass("error");
        	  $j("#urlRequired").hide();
         }
         if($j.trim(userName)==""){
        	 $j("#unameRequired").show();
        	 $j('#unameFld').addClass("error");
        	 flag=false;
         }else{
        	 gsCredintials.userName =userName;
        	 $j('#unameFld').removeClass("error");
        	 $j("#unameRequired").hide();
         }
    
        if($j.trim(password)==""){
        	 $j("#pwdRequired").show();
        	 $j('#passwordFld').addClass("error");
        	 flag=false;
        }else{
        	  gsCredintials.password =password;
        	  $j('#passwordFld').removeClass("error");
        	  $j("#pwdRequired").hide();
         }
         gsCredintials.isActive = 'Y';  
         gsCredintials.createTime =  null ;     
         gsCredintials.createdBy=  null ;         
         gsCredintials.updatedBy =  null;         
         gsCredintials.updateDate =    null ;     
		 var gsCredintialsDtlsParameter =  Object.toJSON(gsCredintials);
		 if(flag){
		 $t.saveGSCredintials(gsCredintialsDtlsParameter, {
				 success : function(data) {
					 credintialsList();
					  $j("#pwdCheckBox").show();
				
				 },
				 error : function(xhr, textStatus, errorThrown) {
					 console.log(xhr.responseText);
					 if(Boolean(xhr.responseText.message)){
							console.log(xhr.responseText.message);
						}
				 }
		 });
		 }
	  };
	
      var credintialsList=function(){
			$t.getGSCredintials({
				success:function(data){
					$j(".required").hide();
					var password=data.password;
//					var url= ((data.url) == null || (data.url)== "null")?" ":data.url;
//					var userName = ((data.userName) == null || (data.userName)== "null")?" ":data.userName;
//					var password = ((data.password) == null || (data.password)== "null")?" ":data.password;
					$j("#viewUrl").html(data.url);
					$j("#viewUname").html(data.userName);
					$j("#viewPassword").html(data.password);
									
					displayGSCredintials(data);
					
					$j(".editDetails").click(function(){
						$j("#pwdCheckBox").hide();
						$j(".savePanel").hide();
						$j("#passwordFld").val(password);
						$j("#unameRequired").hide();
				        $j("#pwdRequired").hide();
				        $j("#urlRequired").hide();
						$j(".required").show();
						$j(".viewFld").hide();
						$j(".editFld").show();
						$j('#unameFld').removeClass("error");
						$j('#urlFld').removeClass("error");
						$j('#passwordFld').removeClass("error");
					    $j(this).hide();
						$j(".btnPanel").show();
					});		
					$j("#save").click(function(){
						 saveGSCreditials();
					 });
					$j("#saveCredintials").click(function(){
						 saveGSCreditials();
					});
					$j('.checkbox').click(function(){
						if ($j('#pwdCheckBox').is(':checked')){
						      $j("#viewPassword").html(password);
						  }
						  else{
						   	$j("#viewPassword").html("***********");
						  }
						});
					$j("#cancelBtn").click(function(){
						$j("#pwdCheckBox").hide();
						$j(".savePanel").show();
						$j("#unameRequired").hide();
				         $j("#pwdRequired").hide();
				         $j("#urlRequired").hide();
						$j(".required").show();
						$j("#urlFld").val('');
						 $j("#unameFld").val('');
						 $j("#passwordFld").val('');
						$j(".viewFld").hide();
						$j(".editFld").show();
						 $j('#unameFld').removeClass("error");
						  $j('#urlFld').removeClass("error");
						  $j('#passwordFld').removeClass("error");
					  });
					
					$j("#cancelUpdate").click(function(){
					    $j("#unameRequired").hide();
				         $j("#pwdRequired").hide();
				         $j("#urlRequired").hide();
	                	 $j(".required").hide();
						 $j(".viewFld").show();
						 $j(".editFld").hide();
						 $j(".ui-multiselect").hide();
						 $j(".editDetails").show();
						 $j(".btnPanel").hide();
						  $j("#unameFld").val($j("#viewUname").html());
						  $j("#passwordFld").val(password);
						  $j("#urlFld").val($j("#viewUrl").html());
						  $j("#pwdCheckBox").show();
					  
					});
						
			},
				error: function(xhr, textStatus, errorThrown){
					console.log(xhr.responseText);
					if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
					}
				}
			});
			
		};
//		if($j('#editDetails .pwd  .pwdcheck . pwdCheck input  #pwdCheckBox').attr('checked')){
//               alert(1);
//            
//		}
	
    
	$j().ready(function(){
		$j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.GLOBALSIGH);
		$j("#currentMenuPage").val(Constants.SUBMENU_ITEMS_GLOBALSIGHT.CONFIGURATIONS);
		
		if($j("#adminHeaderFlag").val() == "true"){
			$j("#adminHeader").show();
		}
		if($j("#userHeaderFlag").val() == "true"){
			$j("#userHeader").show();

		}
		$j('#globalsight').addClass("on");

		$j(".subMenuLinks a").last().css("border-right", "none");
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
		
		credintialsList();

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
				var totalUsers = 0;
				var array = [];
				
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
					ChartRender.twoDLineChart("myChartId23", "240", "70", "userChart", dataXML);
				}else{
					$j("#myChartId23").hide();
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