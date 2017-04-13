(function(window, $j) {
	var fileIdVal=0;
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
		var userDetails = data;
			var userDetailTmplClone = userDetailTmpl;
			var count=userDetails.totalVotes;
			var totalTerms1 = new String(count);
			userDetailTmplClone[1] = userDetails.photoPath;
			userDetailTmplClone[3] = userDetails.userName;
			userDetailTmplClone[6] = userDetails.createDate;
			$j('#userInfo').append(userDetailTmplClone.join(""));
	};
						  
  var downloadTMXStatus= function(fileIdVal){
		$t.getTMXUploadStatus(fileIdVal,{
		success:function(data){
			  if(data != null&& data.proccesedPercentage==100 && data.fileUrl!=null){	
				  $j("#progressbarImp").progressbar(100);
				  setTimeout(function(){
					  $j("#progressbarImp").reportprogress(100);
				  },10);
				 /* 
				  $j("#progressbarImp").progressbar(100);
				  $j("#progressMsg").hide();
				  $j("#statusMsg").show();
				  $j("#urlLocation").html(data.fileUrl);
				  $j("#fileMsg").show();
				 setTimeout(function(){
						  $j("#progressbarImp").reportprogress(100);
				  },10);*/
				location.href=$j("#contextRootPath").val()+"/impExp_serv?c=exportTMXFile&fileId="+data.fileUploadStatusId;
	        }
			  else if(data != null && data.fileUrl==null){
				  $j("#progressbarImp").reportprogress(0); 
				  $j("#progressMsg").show();
				  	setTimeout(function(){
				  		downloadTMXStatus(fileIdVal);
					},10);
			  } 
			  else{
	        		$j("#progressbarImp").reportprogress(data.proccesedPercentage);
	        		 $j("#progressMsg").show();
		        	setTimeout(function(){
		        		downloadTMXStatus(fileIdVal);
					},10);
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
			  
							  
jQuery().ready(function(){	

	fileIdVal=$j("#fileId").val();	
	downloadTMXStatus(fileIdVal);
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
