TerminologyTeamList = function($j){
	var termListLimit = 10;
	var teamListTmpl = ['<div class="teamListItem"><div class="itemImg" style="width:38px;"><img src="',
                        '',
                        '" height="38px" width="38px"/></div><div class="itemDesc"><h6>',
                        '',
                        '',
                        ' ',
                        '<span class="smallFont">',
                        '',
                        ' votes</span></h6><p class="smallFont">',
                        '',
//                        ' | ',
                        '',
                        ' </p></div></div>'];
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
	
	return{
		
			showHpTerminologyTeamLst: function(){
				$t.getTerminologyTeamMemberList({
					success: function(data){
							var teamList = data;
							if(teamList!=null){
								var listLength = (teamList.length >= termListLimit)?termListLimit:teamList.length;
								for(var count = 0; count < listLength; count++){
									var languages = teamList[count].languages;
									var languageList = languages.split(',');
									var totalVotes=teamList[count].totalVotes;
									var totalTerms1 = new String(totalVotes);
									var length = languageList.length >= 2?2:languageList.length;
									languages = "";
									for(var i = 0; i<length; i++){
										if(i != 0){
											languages = languages + ' | '
										}
										languages = languages+languageList[i];
									}
									var teamListTmplClone = teamListTmpl;
									teamListTmplClone[1]=teamList[count].photoPath;
									teamListTmplClone[3]=teamList[count].userName;
									teamListTmplClone[7]=insertCommmas(totalTerms1);
									teamListTmplClone[9]=languages;
									$j('#hpTeamList').append(teamListTmplClone.join(""));
								}
								if(teamList.length<=termListLimit){
									$j(".seeAllHpTeam").hide();
								}else{
									$j(".seeAllHpTeam").show();
								}
							}
							else{
								$j(".seeAllHpTeam").hide();
								$j('#hpTeamList').html('<span class="teamListItem" style="text-align: center; display: block;font-size:12px;padding-top: 15px;color: #FFFFFF;">No members in the HP Community</span>');
							}
						},
						error: function(xhr, textStatus, errorThrown){
							if(Boolean(xhr.response))
							console.error(xhr.response.messsage);
						}
					});
			
			
		}
	};
}(jQuery);
