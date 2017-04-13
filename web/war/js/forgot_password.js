(function(window, $j) {
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
	
	 var url = $j(location).attr('href');
	 if(url.indexOf("/forgot_password.jsp") != -1){
	 	    $j('.signUpButton,.aboutForDashboard,#about,#signOut').css('display','none');
	 	    $j('#footer').css('margin-top' ,'180px');
	 	    
	 }
	var mailSentSuccess = function(){
		$j( "#mail_success:ui-dialog" ).dialog( "destroy" );
		$j( "#mail_success" ).dialog({
			height: 200,
			width:350,
			modal: true,
			buttons: {
				OK: function() {
					$j(this).dialog( "close" );
					location.href=$j("#contextRootPath").val()+'/demo/index.jsp';
				}
			}
		});
	};
	
	var closeLoadingDialog = function(dialogId){
		$j(dialogId).html("");
		$j(dialogId).dialog('destroy');
	};
	
	$j("#submitBtn").click(function(){
		if($j("#forgotPwdForm").valid()){
			 $j('#loading').append('<div class="loading-msg alignCenter topmargin25 bold"><img src="'+$j("#contextRootPath").val()+'/images/loading.gif"  align="top" class="rightmargin10" />Sending mail... please wait</div>');
				showLoadingDialog();
				$j(".ui-dialog-titlebar").hide(); 
		var userName = $j("#username").val();
		var emailId = $j("#email").val();
        var isHp = $j("#hpsite").val();
		$t.requestPassword(emailId, userName, isHp, {
			success:function(data){
				 location.href=data;
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});
		$t.isUserExists(userName, {
			success:function(data){
				 if(data=='success'){
							mailSentSuccess();
							closeLoadingDialog('#loading');
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
	 });
	
	jQuery().ready(function(){
        $j(".userInfoContainer").hide();

        // validate form on keyup and submit
		$j("#forgotPwdForm").validate({
			debug: true,
			rules: {
			username: {
			required: true,
			minlength: 2
			},
			email: {
			required: true,
			email: true
			}
	        },

			messages: {
			username: {
			required: "<br />Enter your username",
			minlength: "<br />Username must consist of at least 2 characters"
			},
			email: "<br />Enter your email address"
			}
	  
		});
				
	});
	
})(window, jQuery);
