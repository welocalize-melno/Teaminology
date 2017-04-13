(function($j) {

$j.extend(teaminology, {


		getTermDetails : function(pageNum,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getExpiredPollTerms/"+pageNum+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},

		getTermsInGlossary : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getGlossaryTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},

		getTotalTermsInGlossary : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTotalGlossaryTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		
		getTotalDebatedTerms : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTotalDebatedTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getQrtrlyTermsDebated : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getQuarterlyDebatedTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		
		getTermsDebated : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getDebatedTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getTotalUsersInSystem : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/usersPerMonth?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getLeaderBoardMemberDetails : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getMembers?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		
		getLeaderBoardMemberDetailsForLanguage : function(values,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getBoardMembersByLanguage/"+values+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getTermAttributes : function(termId, callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTermAttributes/"+termId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getUserCommets : function(termId, callback) {
				var params = $j.extend({
					type: "GET", 
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserComment/"+termId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);

			$j.ajax(params);
		},
		getTerminologyTeamMemberList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getHPMembers?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getTopTermDetails : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTopTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		userRegistration : function(userObject, callback){
			var params = $j.extend({
				 type: "POST",
	  			 data: userObject,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/registerUser?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		getUserDetails : function(callback){
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/userDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		 getLanguageList : function(callback) {
            var params = $j.extend({
                    type: "GET",
                      url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getLanguages?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
                    dataType: "json"

            }, callback);
            
             $j.ajax(params);
    },
	getMonthlyTermsInGlossary : function(callback) {
		var params = $j.extend({
			type: "GET",
			url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getMonthlyTermDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
			dataType: "json"
		}, callback);
		
		$j.ajax(params);
	},
	getQrtrlyTermsInGlossary : function(callback) {
		var params = $j.extend({
			type: "GET",
			url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getQuarterlyTermDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
			dataType: "json"
		}, callback);
		
		$j.ajax(params);
	},
	
	 getActiveUsersCount : function(callback) {
        var params = $j.extend({
                type: "GET",
                  url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getActiveUsersCount?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
                dataType: "json"

        }, callback);
        
         $j.ajax(params);
	 },
		getPOSList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getPartsOfSpeechLookUp?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getFormList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getFormLookUp?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getProgramList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getProgramLookUp?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getMonthlyTermsDebated : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getMonthlyDebatedTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getUserTypeList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserTypeDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		updateTermDetails : function(termDetails, callback) {
			var params = $j.extend({
				type: "POST",
				data: termDetails,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/updateTermDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		
		updateExtendPoll : function(pollDate, termId, callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/extendPoll/"+pollDate+"/"+termId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		
		finalizeTerm :  function(suggestedTermId, callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/approveTerm/"+suggestedTermId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		
		invitePeople :  function(invite,callback) {
			var params = $j.extend({
				type: "POST",
				 data: invite,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/invitePeople?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		sortOrFilterExpPollTerms : function(colName, sortOrder, langIds,companyIds, pageNum, callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/sortOrFilterExpPollTerms/"+colName+"/"+sortOrder+"/"+langIds+"/"+companyIds+"/"+pageNum+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		voteTerm :  function(termVoteDetails,callback) {
			var params = $j.extend({
				type: "POST",
				 data: termVoteDetails,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/voteTerm?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		getUserPollTerms : function(languageId, colName, sortOrder, pageNum, callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserPollTerms/"+languageId+"/"+colName+"/"+sortOrder+"/"+pageNum+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
		getUserLanguages : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserRegLanguages?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
		
		getTotalUsersLangTerms : function(languageId, callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTotalUsersLangTerms/"+languageId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
		rejectTerm : function(termId,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/rejectTerm/"+termId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
		getUserAccuracyRate : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserAccuracyRate?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
//		getUserVotedTerms : function(languageId, callback) {
//			var params = $j.extend({
//				type: "GET",
//				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserVotedTerms/"+languageId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
//				dataType: "json"
//			}, callback);
//			$j.ajax(params);
//		}
		addNewTerm : function(termInformationObject, callback){
			var params = $j.extend({
				 type: "POST",
	  			 data: termInformationObject,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/addNewTerm?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
			    contentType: "application/json"
			
			  }, callback);
			
			$j.ajax(params);
		},
		getConceptCategoryList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getConceptCategoryLookUp?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		
		getCategoryList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getCategoryLookUp?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getProductGroupList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getProductGroupLookUp?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getStatusList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getStatusLookUp?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		requestNewTerm : function(termInfo, callback){
			var params = $j.extend({
				 type: "POST",
	  			 data: termInfo,	
	  			 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/newTermRequest?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
	  			 contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		getInviteUserList : function(filterBy,id,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/inviteUser/"+filterBy+"/"+id+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getEmailTemplates : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getEmailTemplates?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		inviteUsersToVote :  function(invite,callback) {
			var params = $j.extend({
				type: "POST",
				 data: invite,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/inviteUsersToVote?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		getDomainList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getDomainLookUp?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getManagePollTerms : function(queryAppender, callback) {
			var params = $j.extend({
				type: "POST",
				data: queryAppender,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getManagePollTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		getManagePollTermsForPagination :  function(queryAppender, callback) {
			var params = $j.extend({
				type: "POST",
				data: queryAppender,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getManagePollTermsForPagination?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		deleteTerms : function(termIds,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/deleteTerms/"+termIds+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
		exportCSV : function(termIds,callback) {
			var params = $j.extend({
				type: "POST",
				url: $j("#contextRootPath").val()+"/impExp_serv?c=exportCSV&random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				contentType: "application/vnd.ms-excel"
			}, callback);
			$j.ajax(params);
		},
		getTopRegLangList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTopRegLangs?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getEmailTemplate : function(emailTemplateId, callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getEmailTemplate/"+emailTemplateId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
		deleteUsers : function(userIds, callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/deleteUsers/"+userIds+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
		requestPassword : function(mailId, userName, isHp, callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/requestPassword/"+mailId+"/"+userName+"/" + isHp +"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text"
			}, callback);
			
			$j.ajax(params);
		},
		getTeamMemberDetails : function(languageId,companyIds,colName,sortOrder,pageNum,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTeamMemberDetails/"+languageId+"/"+companyIds+"/"+colName+"/"+sortOrder+"/"+pageNum+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		setDomainCUD : function(domainObject, callback){
			var params = $j.extend({
				 type: "POST",
	  			 data: domainObject,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/setDomainCUD?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		setLanguageCUD : function(languageObject, callback){
			var params = $j.extend({
				 type: "POST",
	  			 data: languageObject,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/setLanguageCUD?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		setEmailTemplateCUD : function(emailTemplateObject, callback){
			var params = $j.extend({
				 type: "POST",
	  			 data: emailTemplateObject,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/setEmailTemplateCUD?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		updateUser : function(userId,firstName,lastName,userName,emailId,password,userLanguages,userTypeId,userDomainId,companyId,userCompanyList,callback){
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/updateUser/"+userId+"/"+firstName+"/"+lastName+"/"+userName+"/"+emailId+"/"+password+"/"+userLanguages+"/"+userTypeId+"/"+userDomainId+"/"+companyId+"/"+userCompanyList+"?random="+ Math.floor(Math.random() * (new Date()).getTime() + 1),
				contentType: "charset=utf-8",
				dataType: "text"
			}, callback);
			
			$j.ajax(params);
		},
		updatePwd : function(pwdObj, callback){
			var params = $j.extend({
				 type: "POST",
				 data: pwdObj,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/updatePwd?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		getUser : function(userId,callback){
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUser/"+userId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getLangTeamDetails : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getLangTeamDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		 getVoteConfigList : function(callback) {
            var params = $j.extend({
                    type: "GET",
                      url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getVoteConfig?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
                    dataType: "json"

            }, callback);
            
             $j.ajax(params);
		 },
		 setVoteConfigUD : function(voteConfigObject, callback){
			 var params = $j.extend({
				 type: "POST",
				 data: voteConfigObject,	
				 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/setVoteConfigUD?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text",
				contentType: "application/json"
			 }, callback);
			 
			 $j.ajax(params);
		 },
		 getLanguageChartData : function(callback) {
			 var params = $j.extend({
				 type: "GET",
				 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getLanguageChartData?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				 contentType: "application/json"
			 }, callback);
		
			 $j.ajax(params);
		 },
		 getLanguageListBySortOrder : function(colName,sortOrder,pageNum,callback) {
			 var params = $j.extend({
                type: "GET",
                 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getSortedLanguages/"+colName+"/"+sortOrder+"/"+pageNum+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
                dataType: "json"

			 }, callback);
        
			 $j.ajax(params);
		 },
		 getLanguagePiChart : function(callback) {
			 var params = $j.extend({
				 type: "GET",
				 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getLanguagePiChart?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				contentType: "application/json"
			 }, callback);
		
			 $j.ajax(params);
		 },
		 uploadProfile : function(callback) {
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/uploadProfile?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getCropedImage : function(fileName,ext, callback){
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getCropedImage/"+fileName+"/"+ext+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"

			}, callback);
			
			$j.ajax(params);
		},
		getLanguageReportTable : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getLanguageReportTable?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getDownloadStatus : function(callback) {
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getDownloadStatus?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text"
			}, callback);
			
			$j.ajax(params);
		},
		getUploadStatus : function(callback) {
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUploadStatus?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		getUserVotedTermDetails: function(languageId,colName,sortOrder,pageNum,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserVotedTerms/"+languageId+"/"+colName+"/"+sortOrder+"/"+pageNum+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		addUser : function(userObject, callback){
			var params = $j.extend({
				 type: "POST",
	  			 data: userObject,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/addUser?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		getAllUserTypeList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getAllUserTypeDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		setCategoryCUD : function(categoryObject, callback){
			var params = $j.extend({
				 type: "POST",
	  			 data: categoryObject,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/setCategoryCUD?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		isUserExists : function(userName,callback) {
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/isUserExists/"+userName+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "text"

			}, callback);
			
			$j.ajax(params);
		},
		setTermPhotoPath : function(termId,photoPath,callback){
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/saveTermImage/"+termId+"/"+photoPath+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getAllJobs : function(callback) {
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getAllJobs?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"

			}, callback);
			
			$j.ajax(params);
		},
		getSearchManagePollTerms : function(queryAppender, callback) {
			var params = $j.extend({
				type: "POST",
				data: queryAppender,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getSearchManagePollTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		searchTermBaseEntries : function(queryAppender, callback) {
			var params = $j.extend({
				type: "POST",
				data: queryAppender,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/searchTermBaseEntries?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		searchTMEntries : function(queryAppender, callback) {
			var params = $j.extend({
				type: "POST",
				data: queryAppender,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/searchTMEntries?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		
		getAllLocalePairs : function(callback) {
			var params = $j.extend({
				type: "POST",
			    url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getAllLocalePairs?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		getAllTMProfiles : function(callback) {
			var params = $j.extend({
				type: "POST",
			    url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getAllTMProfiles?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		getAllTermbases : function(callback) {
			var params = $j.extend({
				type: "POST",
			    url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getAllTermbases?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		getXliffFileProfile : function(callback) {
			var params = $j.extend({
				type: "GET",
			    url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getXliffFileProfile?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		getGlobalSightTermInfo : function(queryAppender, callback) {
			var params = $j.extend({
				type: "POST",
				data: queryAppender,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getGlobalSightTermInfo?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		getimportedFileList : function(queryAppender,callback) {
			var params = $j.extend({
				type: "POST",
				data: queryAppender,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getimportedFileList?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
					contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		getTotalTermsInTermBaseTM : function(isTM,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTotalTermsInTermBaseTM/"+isTM+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},getTMProfileTerms : function(queryAppender, callback) {
			var params = $j.extend({
				type: "POST",
				data: queryAppender,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTMProfileTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			$j.ajax(params);
		},
		getTotalTermsInTM : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTotalTermsInTM?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getTmAttributes : function(tmProfileInfoId, callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTmAttributes/"+tmProfileInfoId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		updateTmDetails : function(tmDetails, callback) {
			var params = $j.extend({
				type: "POST",
				data: tmDetails,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/updateTmDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		deleteTms : function(termIds,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/deleteTms/"+termIds+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
		updateGSTermDetails : function(termDetails, callback) {
			var params = $j.extend({
				type: "POST",
				data: termDetails,
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/updateGSTermDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json",
				contentType: "application/json"
			}, callback);
			
			$j.ajax(params);
		},
		getTotalGSTerms : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTotalGSTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getContentTypeList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getContentTypeLookUp?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		deleteGSTerms : function(termIds,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/deleteGSTerms/"+termIds+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
		deleteGSFileData : function(fileIds,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/deleteGSFileData/"+fileIds+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			$j.ajax(params);
		},
		getUserTypeMenus : function(callback) {
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserTypeMenus?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"

			}, callback);
			
			$j.ajax(params);
		},
		getUserTypeSubMenus : function(menuId,callback) {
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserTypeSubMenus/"+menuId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"

			}, callback);
			
			$j.ajax(params);
		},
		getCompanyList : function(callback) {
			var params = $j.extend({
				 type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getCompanyLookUp?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"

			}, callback);
			
			$j.ajax(params);
		},
		getSuperTranslatorCompanyList : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getSuperTranslatorCompanyList?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
					
			}, callback);
			
			$j.ajax(params);
		},
		addNewCompany : function(companyObject, callback){
			var params = $j.extend({
				 type: "POST",
	  			 data: companyObject,	
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/addCompany?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
			    contentType: "application/json"
			
			  }, callback);
			
			$j.ajax(params);
		},
		 getCompanyDetailsList : function(colName,sortOrder,pageNum,callback) {
			 var params = $j.extend({
                type: "GET",
                 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getSortedCompanys/"+colName+"/"+sortOrder+"/"+pageNum+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
                dataType: "json"

			 }, callback);
        
			 $j.ajax(params);
		 },
		 getActiveCompanyCountDetails: function(callback) {
			 var params = $j.extend({
                type: "GET",
                 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getActiveCompaniesCount?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
                dataType: "json"

			 }, callback);
        
			 $j.ajax(params);
		 },
		 getCompanyUser : function(companyId,callback){
				var params = $j.extend({
					 type: "GET",
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getCompany/"+companyId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);
				
				$j.ajax(params);
			},
			updateCompany : function(companyObject, callback){
				var params = $j.extend({
					 type: "POST",
		  			 data: companyObject,	
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/updateCompany?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				    contentType: "application/json"
				
				  }, callback);
				
				$j.ajax(params);
			},
			deleteCompanies : function(companyIds, callback) {
				var params = $j.extend({
					type: "GET",
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/deleteCompanies/"+companyIds+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);
				$j.ajax(params);
			},getTotalTMTermsBySearch : function(queryAppender, callback) {
				var params = $j.extend({
					type: "POST",
					data: queryAppender,
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTotalTMTermsBySearch?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json",
					contentType: "application/json"
				}, callback);
				$j.ajax(params);
			},
			getGlossaryTmTerms : function(callback) {
				var params = $j.extend({
					type: "GET",
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getGlossaryTmTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);
				
				$j.ajax(params);
			},
			getQuarterlyTmDetails : function(callback) {
				var params = $j.extend({
					type: "GET",
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getQuarterlyTmDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);
				
				$j.ajax(params);
			},
			getMonthlyTmDetails : function(callback) {
				var params = $j.extend({
					type: "GET",
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getMonthlyTmDetails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);
				
				$j.ajax(params);
			},
			saveImportTMXFile : function(fileName,ext,type,callback){
				var params = $j.extend({
					 type: "GET",
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/saveImportTMXFile/"+fileName+"/"+ext+"/"+type+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);
				
				$j.ajax(params);
			},
			getPrevilegeList : function(callback) {
				var params = $j.extend({
					type: "GET",
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getPrevilegeList?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);
				
				$j.ajax(params);
			},
			updatePrevileges : function(fileIdArray,roleTypeId,callback) {
				 var params = $j.extend({
	                type: "GET",
	                 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/updatePrevileges/"+fileIdArray+"/"+roleTypeId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
	                dataType: "json"

				 }, callback);
	        
				 $j.ajax(params);
			 },
			 getPrevilegesByRole : function(roleId,callback) {
				 var params = $j.extend({
	                type: "GET",
	                 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getPrevilegesByRole/"+roleId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
	                dataType: "json"

				 }, callback);
	        
				 $j.ajax(params);
			 },getUserRolePrevileges : function(callback) {
					var params = $j.extend({
						type: "GET",
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserRolePrevileges?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "json"
					}, callback);
					
					$j.ajax(params);
				},
				setRoleCUD : function(roleDtlsParameter, callback){
					var params = $j.extend({
						 type: "POST",
			  			 data: roleDtlsParameter,	
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/setRoleCUD?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "text",
						contentType: "application/json"
					}, callback);
					
					$j.ajax(params);
				},
				getTMXUploadStatus : function(fileId,callback) {
					 var params = $j.extend({
		                type: "GET",
		                 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTMXUploadStatus/"+fileId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
		                dataType: "json"

					 }, callback);
		        
					 $j.ajax(params);
				 },
				 getImportExportData : function(userId,colName,sortOrder,pageNum,callback) {
					 var params = $j.extend({
		                type: "GET",
		                 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getImportExportData/"+userId+"/"+colName+"/"+sortOrder+"/"+pageNum+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
		                dataType: "json"

					 }, callback);
		        
					 $j.ajax(params);
				 },
				 requestChangeTerm : function(termRequestChange, callback){
						var params = $j.extend({
							 type: "POST",
				  			 data: termRequestChange,	
				  			 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/requestChangeTerm?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				  			 contentType: "application/json"
						}, callback);
						
						$j.ajax(params);
					},
				saveGSCredintials : function(gsCredintials, callback){
					var params = $j.extend({
					 type: "POST",
					 data: gsCredintials,	
					 url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/saveGSCredintials?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					 contentType: "application/json"
					}, callback);
					$j.ajax(params);
			},
			
		getGSCredintials : function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getGSCredintails?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getAllJobs: function(callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getAllJobs?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		getFileInfoByJobIds: function(multiJobValues,callback) {
			var params = $j.extend({
				type: "GET",
				url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getFileInfoByJobIds/"+multiJobValues+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
				dataType: "json"
			}, callback);
			
			$j.ajax(params);
		},
		 getGlobalSightJobList : function(pageNum,callback) {
				var params = $j.extend({
					type: "GET",
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getGlobalSightJobList/"+pageNum+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);
				
				$j.ajax(params);
		 },
		 getTasksInJob : function(jobId,callback) {
				var params = $j.extend({
					type: "GET",
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTasksInJob/"+jobId+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);
				
				$j.ajax(params);
		 },
		 getJobsCount : function(callback) {
				var params = $j.extend({
					type: "GET",
					url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getJobsCount?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					dataType: "json"
				}, callback);
				
				$j.ajax(params);
			},
			importGSTasks : function(taskId,taskName,jobId,jobName,callback) {
					var params = $j.extend({
						type: "GET",
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/importGSTasks/"+taskId+"/"+taskName+"/"+jobId+"/"+jobName+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "json"
					}, callback);
					
					$j.ajax(params);
			 },
			getUserRoleList : function(callback) {
					var params = $j.extend({
						type: "GET",
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getUserRoleListByMenu?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "json"
					}, callback);
					
					$j.ajax(params);
				},
				getAllTerms : function(queryAppender, callback) {
					var params = $j.extend({
						type: "POST",
						data: queryAppender,
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTotalTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "json",
						contentType: "application/json"
					}, callback);
					$j.ajax(params);
				},
				getAllTms : function(queryAppender, callback) {
					var params = $j.extend({
						type: "POST",
						data: queryAppender,
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getTotalTms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "json",
						contentType: "application/json"
					}, callback);
					$j.ajax(params);
				},
				getAllGSTerms : function(queryAppender, callback) {
					var params = $j.extend({
						type: "POST",
						data: queryAppender,
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getAllGSTerms?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "json",
						contentType: "application/json"
					}, callback);
					$j.ajax(params);
				},
				deleteImportFiles : function(fileIds,callback) {
					var params = $j.extend({
						type: "GET",
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/deleteImportFiles/"+fileIds+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "json"
					}, callback);
					$j.ajax(params);
				},
				getJobsByState : function(pageNum,callback) {
					var params = $j.extend({
						type: "GET",
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getJobsByState/"+pageNum+"?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "json"
					}, callback);
					
					$j.ajax(params);
				},
				getJobsCountByState : function(callback) {
					var params = $j.extend({
						type: "GET",
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getJobsCountByState?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "json"
					}, callback);
					
					$j.ajax(params);
				},
				getSelectedTermsOnly : function(queryAppender, callback) {
					var params = $j.extend({
						type: "POST",
						data: queryAppender,
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getSelectedTermsOnly?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
						dataType: "json",
						contentType: "application/json"
					}, callback);
					$j.ajax(params);
				},
				getSignoutFlag : function(callback) {
					var params = $j.extend({
						url: $j("#contextRootPath").val()+"/teaminology_ctrler/teaminology/getSignoutFlag?random=" + Math.floor(Math.random() * (new Date()).getTime() + 1),
					}, callback);
					$j.ajax(params);
				}
			 
});


})(jQuery);