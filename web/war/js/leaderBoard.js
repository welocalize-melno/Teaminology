LeaderBoard = function($j){
	
	var leaderBoardTmpl = ['<div class="leaderBrdItem"><div class="itemImg" style="width:64px; height: 68px;border: 1px solid #aeaeae;"><img src="',
                           '',
                           '" height="68px" width="64px"/></div><div class="itemDesc"><div class="smallFont leaderBrdSmallFont bold" style="margin-top: -4px;">',
                           '',
                           '</div> <div class="badge" style=" margin-bottom: 6px;" ><img style="height: 39px;width:203px;" src="',
                           '',
                           '" /></div><div class="" style="color: #a9a9a9; float:left; font-size: 10px; margin-right: 10px;">',
                           '',
                           ' votes</div> <div class="floatleft starsDiv" ><img src="',
                           '',
                           '"  /></div></div></div>'
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
	var bindEvents = function(){
	}

	return{
		showLeaderBoard: function(){
		    $j('#leadersList').empty();
			$t.getLeaderBoardMemberDetails({
				success: function(data){
					var leaders = data;
					if(leaders && leaders.length){
						var membersList = (leaders.length > 2)? 2: leaders.length;
						for(var count = 0; count < membersList; count++){
							var accuracyImg = "";
							var badgingImg = "";
							var status="";
							
							var accuracyRate =  Math.round(leaders[count].accuracy);
							//var badgingRate = leaders[count].totalVotes + leaders[count].termRequestCount;
                            var badgingRate = leaders[count].totalVotes;
							if(accuracyRate>=0 && accuracyRate<25){
								accuracyImg=$j("#contextRootPath").val()+"/images/BeginnerAccuracy.jpg";
							}else if(accuracyRate>=25 && accuracyRate<50){
								accuracyImg=$j("#contextRootPath").val()+"/images/NoviceAccuracy.jpg";
							}else if(accuracyRate>=50 && accuracyRate<100){
								accuracyImg=$j("#contextRootPath").val()+"/images/RegularAccuracy.jpg";
							}else if(accuracyRate>=100 && accuracyRate<200){
								accuracyImg=$j("#contextRootPath").val()+"/images/AdvancedAccuracy.jpg";
							}else if(accuracyRate >= 200){
								accuracyImg=$j("#contextRootPath").val()+"/images/ExpertAccuracy.jpg";
							}
							
							if(badgingRate >= 0 && badgingRate < 25){
								badgingImg=$j("#contextRootPath").val()+"/images/biginner.jpg";
							}else if(badgingRate >= 25 && badgingRate <  50){
								badgingImg=$j("#contextRootPath").val()+"/images/novice.jpg";
							}else if(badgingRate >= 50 && badgingRate < 100 ){
								badgingImg=$j("#contextRootPath").val()+"/images/regular.jpg";
							}else if(badgingRate >= 100 && badgingRate < 200 ){
								badgingImg=$j("#contextRootPath").val()+"/images/advanced.jpg";
							}else if(badgingRate >= 200){
								badgingImg=$j("#contextRootPath").val()+"/images/expert.jpg";
							}
							
							var leaderBoardTmplClone = leaderBoardTmpl;
							var totalCount=leaders[count].totalVotes;
							var totalTerms1 = new String(totalCount);
							
							
							leaderBoardTmplClone[1]=leaders[count].photoPath; 
							leaderBoardTmplClone[3]=leaders[count].userName;;
							leaderBoardTmplClone[5]=badgingImg;
							leaderBoardTmplClone[7]=insertCommmas(totalTerms1);
							leaderBoardTmplClone[9]=accuracyImg;
							$j('#leadersList').append(leaderBoardTmplClone.join(""));

							bindEvents();
						}
																		
						if(leaders.length <= 5){
							$j(".seeAll").hide();
						}else{
							$j(".seeAll").show();
						}
						
					}else{
						$j(".seeAll").hide();
						$j('#leadersList').html('<span style="text-align: center; display: block;font-size:12px;padding-top: 15px;">No members in Leader Board</span>');
					}
					
				},
				error: function(xhr, textStatus, errorThrown){
					if(Boolean(xhr.response))
					console.error(xhr.response.messsage);
				}
			});
			
			
		}
		
		
	}
	
}(jQuery);

