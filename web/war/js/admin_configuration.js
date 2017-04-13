(function(window, $j) {

	var displayListLimit = 5;
	var savedCriteria = "";
	var domainData = null;
	var categoryData = null;
	var roleData=null;
	 var domainValidate;
	 var langsValidate;
	 var templateValidate;
	 var voteValidate;
	 var categoryValidate;
	 var roleValidate;
	
	 var url = $j(location).attr('href');

		if(url.indexOf("/admin_configuration.jsp") != -1){
		    $j('#about').removeClass('floatrightLogOut');
		    $j('#about').addClass('floatrightLogOutForAdminConf');
		}
		
	var domainListTmpl = ['<div class="configLstItem"><div class="width170 viewDtl" id="',
	                      '',
	                      '">',
	                      '',
		                  '</div><div class="width130 editDtl nodisplay"><input class="domainTxtFld" maxlength="45" type="text" size="15" value="',
		                  '',
		                  '" id="',
		                  '',
		                  '"/></div><div class="width100 alignRight"><a href="javascript:;" class="edit" id="',
		                  '',
		                  '">Edit</a><a href="javascript:;" class="save nodisplay domainSave" id="',
		                  '',
		                  '">Save</a> | <a href="javascript:;"  class="delete" id="',
		                  '',
		                  '">Delete</a></div></div>'
		                 ];
	
		var tempHtml = 	 '<head><style>' +
			             '@font-face {' +
			             'font-family: "HP Simplified";' +
	 	                 'font-style: normal;' +
	                     'font-weight: normal;' +
	                     'src: url("http://www8.hp.com/h10180/hp-fonts/v2-0/hps-euro-w01-regular-eot.eot?#iefix") format("embedded-opentype"),' +
	                     'url("http://www8.hp.com/h10180/hp-fonts/v2-0/hps-euro-w01-regular-woff.woff") format("woff"),' +
	                     'url("http://www8.hp.com/h10180/hp-fonts/v2-0/hps-euro-w01-regular-ttf.ttf") format("truetype");' +
		                 '} </style> </head>' +
			             '<table style="width:100%; height: 20px;">'+
						 '<tbody>'+
						 '<tr>'+
						 '<td style="padding-top: 30px;" width="80%"><span style="font-family: HP Simplified, Arial !important;font-size: 36px;"><strong>HP Terminology Community</strong></span></td>'+
						 '<td style="padding-top: 30px;" width="20%" align="right" width="20%"><img src="http://185.5.242.50:8080/app/images/logo_hpe.png" alt="" /></td>'+
						 '</tr>'+
						 '</tbody>'+
						 '</table>'+
						 '<p>&nbsp;</p>';
	
	var displayDomainList = function(data) {
		var domainList = data;
		domainData = data;
		for ( var count = 0; count < domainList.length; count++) {
			var domainListTmplClone = domainListTmpl;
			domainListTmplClone[1] = domainList[count].domainId;
			domainListTmplClone[3] = domainList[count].domain;
			domainListTmplClone[5] = domainList[count].domain;
			domainListTmplClone[7] = "input_"+domainList[count].domainId;
			domainListTmplClone[9] = "domainedit_"+domainList[count].domainId;
			domainListTmplClone[11] = "domainsave_"+domainList[count].domainId;
			domainListTmplClone[13] = "domaindel_"+domainList[count].domainId;
			$j('#domainList').append(domainListTmplClone.join(""));
		}
	};
	var roleListDetailsTmpl =   ['<div class="configLstItem"><div class="width170 viewDtl" id="',
	                      '',
	                      '">',
	                      '',
		                  '</div><div class="width130 editDtl nodisplay"><input class="domainTxtFld" maxlength="45" type="text" size="15" value="',
		                  '',
		                  '" id="',
		                  '',
		                  '"/></div><div class="width100 alignRight"><a href="javascript:;" class="edit" id="',
		                  '',
		                  '">Edit</a><a href="javascript:;" class="save nodisplay roleSave" id="',
		                  '',
		                  '">Save</a> | <a href="javascript:;"  class="delete" id="',
		                  '',
		                  '">Delete</a></div></div>'
		                 ];
	
	var tm_fonts = 		"Andale Mono=andale mono,times;"+
					    "Arial=arial;"+
					    "Arial Black=arial black,avant garde;"+
					    "Book Antiqua=book_antiquaregular,palatino;"+
					    "Corda Light=CordaLight,sans-serif;"+
					    "Courier New=courier_newregular,courier;"+
					    "Flexo Caps=FlexoCapsDEMORegular;"+                 
					    "Lucida Console=lucida_consoleregular,courier;"+
					    "Georgia=georgia,palatino;"+
					    "Helvetica=helvetica;"+
					    "HP Simplified=HP Simplified;"+
					    "Impact=impactregular,chicago;"+
					    "Museo Slab=MuseoSlab500Regular,sans-serif;"+                   
					    "Museo Sans=MuseoSans500Regular,sans-serif;"+
					    "Oblik Bold=OblikBoldRegular;"+
					    "Sofia Pro Light=SofiaProLightRegular;"+                    
					    "Symbol=webfontregular;"+
					    "Tahoma=tahoma,arial,helvetica,sans-serif;"+
					    "Terminal=terminal,monaco;"+
					    "Tikal Sans Medium=TikalSansMediumMedium;"+
					    "Times New Roman=times new roman,times;"+
					    "Trebuchet MS=trebuchet ms,geneva;"+
					    "Verdana=verdana,geneva;"+
					    "Webdings=webdings;"+
					    "Wingdings=wingdings,zapf dingbats"+
					    "Aclonica=Aclonica, sans-serif;"+
					    "Michroma=Michroma;"+
					    "Paytone One=Paytone One, sans-serif;"+
					    "Andalus=andalusregular, sans-serif;"+
					    "Arabic Style=b_arabic_styleregular, sans-serif;"+
					    "Andalus=andalusregular, sans-serif;"+
					    "KACST_1=kacstoneregular, sans-serif;"+
					    "Mothanna=mothannaregular, sans-serif;"+
					    "Nastaliq=irannastaliqregular, sans-serif,"
	
	var displayRoleList = function(data) {
		var roleDetails = data;
		roleData = data;
		for ( var count = 0; count < roleDetails.length; count++) {
			var roleListTmplClone = roleListDetailsTmpl;
			roleListTmplClone[1] = roleDetails[count].roleId;
			roleListTmplClone[3] = roleDetails[count].roleName;
			roleListTmplClone[5] = roleDetails[count].roleName;
			roleListTmplClone[7] = "inputRole_"+roleDetails[count].roleId;
			roleListTmplClone[9] = "roleedit_"+roleDetails[count].roleId;
			roleListTmplClone[11] = "rolesave_"+roleDetails[count].roleId;
			roleListTmplClone[13] = "roledel_"+roleDetails[count].roleId;
			$j('#roleList').append(roleListTmplClone.join(""));
		}
	};
	var langCodeTmpl = ['<div class="configLstItem"><div class="width200 viewDtl viewlangLabel" id="',
	                    '',
	                    '">',
	                    '',
	                    '</div><div class="width200 editDtl nodisplay editlangLabel"><input type="text" size="15" maxlength="50" name="lLabel" value="',
	                    '',
	                    '" id="',
	                    '',
	                    '" /></div><div class="width90 viewDtl viewCode">',
	                    '',
	                    '</div><div class="width90 editDtl nodisplay editCode"><input type="text" size="15" maxlength="45" name="lCode" value="',
	                    '',
	                    '" id="',
	                    '',
	                    '" /></div><div class="width100 alignRight"><a href="javascript:;" class="edit" id="',
	                    '',
	                    '">Edit</a><a href="javascript:;" class="save nodisplay langSave" id="',
	                    '',
	                    '">Save</a> | <a href="javascript:;" class="delete" id="',
	                    '',
	                    '">Delete</a></div></div>']
	
	
	var displayLanguageList = function(data) {
		var languageList = data;
		var length = (data.length >= displayListLimit)?displayListLimit:data.length;
		for ( var count = 0; count < length; count++) {
			var langSlctTmplClone = langCodeTmpl;
			langSlctTmplClone[1] = languageList[count].languageId;
			langSlctTmplClone[3] = languageList[count].languageLabel;
			langSlctTmplClone[5] = languageList[count].languageLabel;
			langSlctTmplClone[7] = "inputLabel_"+languageList[count].languageId;
			langSlctTmplClone[9] = languageList[count].languageCode;
			langSlctTmplClone[11] = languageList[count].languageCode;
			langSlctTmplClone[13] = "inputCode_"+languageList[count].languageId;
			langSlctTmplClone[15] = "langEdit_"+languageList[count].languageId;
			langSlctTmplClone[17] = "langSave_"+languageList[count].languageId;
			langSlctTmplClone[19] = "langDel_"+languageList[count].languageId;
			$j('#langsList').append(langSlctTmplClone.join(""));
		}
	};
	var slctOptionsTmpl = ['<option value="',
		                    '',
		                    '" >',
		                    '',
		                    '</option>'
		                    ]; 
	
	var displayEmailList = function(data) {
		var emailList = data;
	
		for ( var count = 0; count < emailList.length; count++) {
			var domainEmailTmplClone = slctOptionsTmpl;
			domainEmailTmplClone[1] = emailList[count].emailTemplateId;
			domainEmailTmplClone[3] = emailList[count].emailSubject;
			$j('#emailtmpl').append(domainEmailTmplClone.join(""));
		}
		var domainEmailTmplClone = slctOptionsTmpl;
		domainEmailTmplClone[1] = 0;
		domainEmailTmplClone[3] = "Add New Template";
		$j('#emailtmpl').append(domainEmailTmplClone.join(""));
		$j('#emailtmpl').prepend('<option value="slct">--Select--</option>');
		$j('#emailtmpl').val("slct");
	};
	
		
		var displayUserTypeList = function(data,id) {
			var userTypeList = data;
			for ( var count = 0; count < userTypeList.length; count++) {
				var userTypeTmplClone = slctOptionsTmpl;
				userTypeTmplClone[1] = userTypeList[count].roleId;
				userTypeTmplClone[3] = userTypeList[count].roleName;
				$j(id).append(userTypeTmplClone.join(""));
			}
		};
	
	var domainEmailSubTmpl = ['<div class="configLstItem"><div class="boxWidth320" id="',
	                      '',
	                      '">',
	                      '',
		                  '</div><div class="width90 alignRight"><a href="javascript:;" class="delete" id="',
		                  '',
		                  '">Delete</a></div></div>'
		                 ];
	
	var displayDelEmailList = function(data) {
		var emailList = data;
		for ( var count = 0; count < emailList.length; count++) {
			var domainEmailTmplClone = domainEmailSubTmpl;
			domainEmailTmplClone[1] = emailList[count].emailTemplateId;
			domainEmailTmplClone[3] = emailList[count].emailSubject;
			domainEmailTmplClone[5] = "delTmpl_"+emailList[count].emailTemplateId;
			
			$j('#emailSubList').append(domainEmailTmplClone.join(""));
		}
	};
	
	var categoryListTmpl = ['<div class="configLstItem"><div class="width170 viewDtl" id="',
	                      '',
	                      '">',
	                      '',
		                  '</div><div class="width130 editDtl nodisplay"><input type="text" maxlength="45" size="15" value="',
		                  '',
		                  '" id="',
		                  '',
		                  '"/></div><div class="width100 alignRight"><a href="javascript:;" class="edit" id="',
		                  '',
		                  '">Edit</a><a href="javascript:;" class="save nodisplay categorySave" id="',
		                  '',
		                  '">Save</a> | <a href="javascript:;"  class="delete" id="',
		                  '',
		                  '">Delete</a></div></div>'
		                 ];
	
	var displayCategoryList = function(data) {
		var categoryList = data;
		categoryData = data;
		for ( var count = 0; count < categoryList.length; count++) {
			var categoryListTmplClone = categoryListTmpl;
			categoryListTmplClone[1] = categoryList[count].categoryId;
			categoryListTmplClone[3] = categoryList[count].category;
			categoryListTmplClone[5] = categoryList[count].category;
			categoryListTmplClone[7] = "input_"+categoryList[count].categoryId;
			categoryListTmplClone[9] = "categoryedit_"+categoryList[count].categoryId;
			categoryListTmplClone[11] = "categorysave_"+categoryList[count].categoryId;
			categoryListTmplClone[13] = "categorydel_"+categoryList[count].categoryId;
			$j('#categoryList').append(categoryListTmplClone.join(""));
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
    /**
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
	 */
	var deleteRow = function(id){
		if(id.indexOf("deleteVt") != -1)
		{	
			$j("#"+id).parent().parent().hide();
			$j("#noDataVT").show();
			$j(".addVT").show();
		}else{
			$j("#"+id).parent().parent().remove();
		}
		
		
		if(!($j("#domainList").hasClass(".configLstItem")))
		{
			$j("#noDataDomain").show();
		}
		if($j("#emailSubList .configLstItem").length ==0)
		{
			$j("#noDataET").show();
		}
		
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
			
					deleteRow(id);
						if(id.indexOf("domaindel")!=-1){
						  domainId= id.replace("domaindel_","");
						  saveDomain("Delete Domain",domainId);
						  domainLookUp();
					 }
						 if(id.indexOf("langDel")!=-1){
							  languageId= id.replace("langDel_","");
							  saveLanguage("Delete Language", languageId); 
						 
					 }
						 
						 if(id.indexOf("categorydel")!=-1){
							 categoryId= id.replace("categorydel_","");
							  saveCategory("Delete Category",categoryId);
						 }
						 if(id.indexOf("roledel")!=-1){
							roleId= id.replace("roledel_","");
							  saveRole("Delete Role",roleId);
						
						 }
						
						
					$j( this ).dialog( "close" );
					 
				},
				Cancel: function() {
					$j( this ).dialog( "close" );
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
	
	var saveRole=function(transactType,roleId){
		 var role=new Object();
		 if(transactType=="Add Role"){
			 role.roleName=$j.trim($j("#role").val());
			 role.roleId=null;	
		 }else if(transactType=="Edit Role"){
			 role.roleName=$j.trim($j("#inputRole_"+ roleId).val());
			  role.roleId=roleId;
		 }else{
			 role.roleId=roleId;
			 role.roleName=null;
		 }
			
		 role.createdBy=null;
		 role.createDate=null;
		 role.updatedBy=null;
		 role.updateDate=null;
		 role.transactionType= transactType;
		 var roleDtlsParameter =  Object.toJSON(role);

		 $t.setRoleCUD(roleDtlsParameter, {
			 success : function(data) {
					 if(transactType=="Add Role"){
					 if(data.indexOf("Role exists") != -1){
						 $j("#role").addClass("errorBorder");
						$j("#rLabelExists").show();
						 $j("#rLabelExists").html("Role already exists, please choose other Role");
							
					 }else{
						 $j("#rLabelExists").hide();
						 $j("#role").removeClass("errorBorder");
						 rolesLookUp();
					 }	  
				 }
				 if(transactType=="Edit Role"){
					 if(data.indexOf("Role exists") != -1){
						 var  id="input_"+roleId;
						 $j("#"+id).css("border","1px solid red");
						$j("#roleIdExists").show();
						 $j("#roleIdExists").html("<span class='redTxt'>*</span>Role already exists");
							
					 }else{
						 $j("#roleIdExists").hide();
						 $j("#"+id).removeClass("errorBorder");
						 var  id="roleSave_"+roleId;
						  $j("#"+id).hide();
						  $j("#"+id).parent().parent().find(".edit").show();
						  var newTxt = $j("#"+id).parent().parent().find(".editDtl input").val();
						  $j("#"+id).parent().parent().find(".viewDtl").html(newTxt);
						  $j("#"+id).parent().parent().find(".editDtl").hide();
						  $j("#"+id).parent().parent().find(".viewDtl").show();
						 roleValidate.resetForm();	 
						  rolesLookUp();
					 }
				 } 

				 $j('#role').val('');
			 },
			 error : function(xhr, textStatus, errorThrown) {
				 console.log(xhr.responseText);
				 if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
				}
			 }
	 });
	
  };
	var saveDomain=function(transactType,domainId){
		 var domain=new Object();
		 if(transactType=="Add Domain"){
			 domain.domain=$j.trim($j("#domain").val());
			 domain.domainId=null;	
		 }else if(transactType=="Edit Domain"){
			 domain.domain=$j.trim($j("#input_"+ domainId).val());
			 domain.domainId=domainId;
		 }else{
			 domain.domainId=domainId;
			 domain.domain=null;
		 }
			
		 domain.createdBy=null;
		 domain.createDate=null;
    	 domain.updatedBy=null;
		 domain.updateDate=null;
		 domain.transactionType= transactType;
		 var domainDtlsParameter =  Object.toJSON(domain);

		 $t.setDomainCUD(domainDtlsParameter, {
			 success : function(data) {
					 if(transactType=="Add Domain"){
					 if(data.indexOf("Domain exists") != -1){
						 $j("#domain").addClass("errorBorder");
						$j("#dLabelExists").show();
						 $j("#dLabelExists").html("Domain already exists, please choose other Domain");
							
					 }else{
						 $j("#dLabelExists").hide();
						 $j("#domain").removeClass("errorBorder");
						 domainLookUp();
					 }	  
				 }
				 if(transactType=="Edit Domain"){
					 if(data.indexOf("Domain exists") != -1){
						 var  id="input_"+domainId;
						 $j("#"+id).css("border","1px solid red");
						$j("#idExists").show();
						 $j("#idExists").html("<span class='redTxt'>*</span>Domain already exists");
							
					 }else{
						 $j("#idExists").hide();
						 $j("#"+id).removeClass("errorBorder");
						 var  id="domainSave_"+domainId;
						  $j("#"+id).hide();
						  $j("#"+id).parent().parent().find(".edit").show();
						  var newTxt = $j("#"+id).parent().parent().find(".editDtl input").val();
						  $j("#"+id).parent().parent().find(".viewDtl").html(newTxt);
						  $j("#"+id).parent().parent().find(".editDtl").hide();
						  $j("#"+id).parent().parent().find(".viewDtl").show();
						  domainValidate.resetForm();	 
						  domainLookUp();
					 }
				 } 

				 $j('#domain').val('');
			 },
			 error : function(xhr, textStatus, errorThrown) {
				 console.log(xhr.responseText);
				 if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
				}
			 }
	 });
	
 };
 
  var saveVoteConfig=function(transactType,voteConfigId){
		 var voteConfig=new Object();
		 if(transactType=="Add Vote"){
			 voteConfig.votingPeriod=$j.trim($j("#period").val());
			 voteConfig.votesPerUser=$j.trim($j("#user").val());
			 voteConfig.voteConfigId=null;
		 }else if(transactType=="Edit Vote"){
			 voteConfig.votingPeriod=$j.trim($j("#newVotePeriod").val());
			 voteConfig.votesPerUser=$j.trim($j("#newVotePerTrm").val());
			 voteConfig.voteConfigId=voteConfigId;
		 }else{
			 voteConfig.voteConfigId=voteConfigId;
		 }
		 voteConfig.createdBy=null;
		 voteConfig.createDate=null;
		 voteConfig.updatedBy=null;
		 voteConfig.updateDate=null;
		 voteConfig.transactionType= transactType;
		 var voteConfigDtlsParameter =  Object.toJSON(voteConfig);
		 $t.setVoteConfigUD(voteConfigDtlsParameter, {
			 success : function(data) {
				 if(transactType=="Edit Vote"){
				 var  id="saveVt_"+voteConfigId;
				 var newPeriod = $j.trim($j("#newVotePeriod").val());
					$j("#votePeriod").html(newPeriod);
					var newNumVotes = $j.trim($j("#newVotePerTrm").val());
					 $j("#votesPerTerm").html(newNumVotes);
					 $j("#"+id).hide();
					 $j(".editVt").show();
					 $j("#"+id).parent().parent().find(".editDtl").hide();
					 $j("#votesPerTerm").show();
					 $j("#votePeriod").show();
					 
				 }
//				 $j(".configLstItem").html("");
				 voteConfiguration();
					
			 },
			 error : function(xhr, textStatus, errorThrown) {
				 console.log(xhr.responseText);
				 if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
				}
			 }
	 });
	
};
  
  
  var emailTemplate=function(transactType,emailTemplateId){
	   var  emailTemplates=new Object();
	   if(transactType== "Add Template"){
		   emailTemplates.emailSubject= $j.trim($j("#newSub").val());
		   emailTemplates.emailMessageContent= $j("#content").val();
		  
	   } else if(transactType== "Edit Template"){
		   emailTemplates.emailTemplateId=emailTemplateId;
		   emailTemplates.emailMessageContent=$j("#content").html();
		  
	   }else{  
		   
			 emailTemplates.emailTemplateId=emailTemplateId;
			 emailTemplates.emailSubject=null;
		
		 }
			
		 emailTemplates.createdBy=null;
		 emailTemplates.createDate=null;
		 emailTemplates.updatedBy=null;
		 emailTemplates.updateDate=null;
		 emailTemplates.transactionType= transactType;
		 var emailTemplatesDtlsParameter =  Object.toJSON(emailTemplates);
		 $t.setEmailTemplateCUD(emailTemplatesDtlsParameter, {
			 success : function(data) {
					templateValidate.resetForm();	
				 if(transactType== "Add Template"){
					 alertMessage('#addedTemplateMessage');
				 }else if(transactType== "Edit Template"){
					 alertMessage('#editTemplateMessage');
				 }else{
					 alertMessage('#deleteTemplateMessage');
				 }
			 mailTemplate();
			
			 },
			 error : function(xhr, textStatus, errorThrown) {
				 console.log(xhr.responseText);
				 if(Boolean(xhr.responseText.message)){
						console.log(xhr.responseText.message);
				}
			 }
	 });

	};
  var saveLanguage=function(transactType,languageId){
		 var language=new Object();
		 if(transactType=="Add Language"){
			 language.languageLabel=$j.trim($j("#language").val());
			 language.languageCode=$j.trim($j("#code").val());	
		 }else if(transactType=="Edit Language"){
			 language.languageLabel=$j.trim($j("#inputLabel_"+ languageId).val());
			 language.languageCode=$j.trim($j("#inputCode_" + languageId).val());
			 language.languageId=languageId;
		 }else{
			 language.languageId=languageId;
			 language.languageLabel=null;
			 language.languageCode=null;
		 }
			
		 language.createdBy=null;
		 language.createDate=null;
		 language.updatedBy=null;
		 language.updateDate=null;
		 language.transactionType= transactType;
		 var languageDtlsParameter =  Object.toJSON(language);
		 $t.setLanguageCUD(languageDtlsParameter, {
			 success : function(data) {
				 langsValidate.resetForm();	
				 if(transactType=="Add Language"){
					 if(data.indexOf("Label exists") != -1){
					 $j("#language").addClass("error");
					$j(".labelExists").show();
					 $j("#userReq").hide();
					 $j(".labelExists").html("Language label already exists, please choose other language label");
						
				 }else{
					 $j(".labelExists").hide(); 
				 }	 
				 if(data.indexOf("Code exists") != -1){
					 $j("#code").addClass("error");
					 $j(".codeExists").show();
					 $j(".codeExists").html("Language code already exists, please choose other language code");
				 }else{
					 $j(".codeExists").hide();
				 }
				 }
				 if(transactType=="Edit Language"){
					 if(data.indexOf("Label exists") != -1){
						 var  id="inputLabel_"+languageId;
						 $j("#"+id).css("border","1px solid red");
						$j(".langLabelExists").show();
						 $j("#userReq").hide();
						 $j(".langLabelExists").html("<span class='redTxt'>*</span>Language label already exists");
							
					 }else{
						 $j(".langLabelExists").hide(); 
					 }	 
					 if(data.indexOf("Code exists") != -1){
						 var  id="inputCode_"+languageId;
						 $j("#"+id).css("border","1px solid red");
						 $j(".langCodeExists").show();
						 $j(".langCodeExists").html("<span class='redTxt'>*</span>Language code already exists");
					 }else{
						 $j(".langCodeExists").hide();
//						 $j("#"+id).css("border","1px solid #BBBBBB");
					 } 
					 
				 }
				
				 if(data.indexOf("Label exists") == -1   && data.indexOf("Code exists") == -1 )	{
					 if(transactType=="Edit Language"){
						 var  labelId="inputLabel_"+languageId;
						 $j("#"+labelId).css("border","1px solid #BBBBBB");
						 var  codeId="inputCode_"+languageId;
						 $j("#"+codeId).css("border","1px solid #BBBBBB");
							var  id="langSave_"+languageId;
						   $j("#"+id).hide();
							$j("#"+id).parent().parent().find(".edit").show();
							var newLangLabel = $j("#"+id).parent().parent().find(".editlangLabel input").val();
							$j("#"+id).parent().parent().find(".viewlangLabel").html(newLangLabel);
							var newLangCode = $j("#"+id).parent().parent().find(".editCode input").val();
							$j("#"+id).parent().parent().find(".viewCode").html(newLangCode);
							$j("#"+id).parent().parent().find(".editDtl").hide();
							$j("#"+id).parent().parent().find(".viewDtl").show();
					 }
					 
					 languageLookUp(null,null,0);
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

var saveCategory=function(transactType,categoryId){
	 var category=new Object();
	 if(transactType=="Add Category"){
		 category.category=$j.trim($j("#category").val());
		 category.categoryId=null;	
	 }else if(transactType=="Edit Category"){
		 category.category=$j.trim($j("#input_"+ categoryId).val());
		 category.categoryId=categoryId;
	 }else{
		 category.categoryId=categoryId;
		 category.category=null;
	 }
		
	 category.createdBy=null;
	 category.createDate=null;
	 category.updatedBy=null;
	 category.updateDate=null;
	 category.transactionType= transactType;
	 var categoryDtlsParameter =  Object.toJSON(category);
	 $t.setCategoryCUD(categoryDtlsParameter, {
		 success : function(data) {
			 	 if(transactType=="Add Category"){
				 if(data.indexOf("Category exists") != -1){
					 $j("#category").addClass("errorBorder");
					$j("#cLabelExists").show();
					 $j("#cLabelExists").html("Category already exists, please choose other category");
						
				 }else{
					 categoryLookUp();
					 $j("#cLabelExists").hide();
					 $j("#category").removeClass("errorBorder");
				 }	  
			 }
			 	 
			 	if(transactType=="Edit Category"){
			 		 if(data.indexOf("Category exists") != -1){
						 var  id="input_"+categoryId;
						 $j("#"+id).css("border","1px solid red");
						 $j("#cIdExists").show();
						 $j("#cIdExists").html("<span class='redTxt'>*</span>Category  already exists");
							
					 }else{
							categoryLookUp();
						 $j("#cIdExists").hide();
						 $j("#"+id).removeClass("errorBorder");
						 var  id="categorysave"+categoryId;
						    $j("#"+id).hide();
							$j("#"+id).parent().parent().find(".edit").show();
							var newTxt = $j("#"+id).parent().parent().find(".editDtl input").val();
							$j("#"+id).parent().parent().find(".viewDtl").html(newTxt);
							$j("#"+id).parent().parent().find(".editDtl").hide();
							$j("#"+id).parent().parent().find(".viewDtl").show();
//							categoryValidate.resetForm();	 
						
					 }
				 } 
				if(transactType=="Delete Category"){
					categoryLookUp();
				}
			 
		$j('#category').val('');
		 },
		 error : function(xhr, textStatus, errorThrown) {
			 console.log(xhr.responseText);
			 if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
			}
		 }
});

};

var sortData = function(arrayData, prop, asc){
	if(Boolean(arrayData)){
		arrayData = arrayData.sort(function(a, b) {
		var aProp = a[prop];
		var bProp = b[prop];
			
			if(isNaN(aProp) && isNaN(bProp)){
				 aProp = aProp.toUpperCase();
				 bProp = bProp.toUpperCase();
					if (asc) {
						if (aProp > bProp){
							return 1
						}else if(aProp==bProp){
							return 0;
						}else{
							return -1;
						}
					}
			        else {
			        	if (bProp > aProp){
			        		return 1;
			        	}else if(aProp==bProp){
							return 0;
						}else{
							return -1;
						}
			        }
				}else{

			        if (asc) return aProp - bProp;
			        else return bProp - aProp;
		        }
	    });
		return arrayData;
	}
};
 
      $j(".domainBtn").click(function(){
    	  var flag=true;
    	  $j("#dLabelExists").hide();
    	  
    	  if($j("#domainForm").valid()){
    		  saveDomain("Add Domain",0);
    	  }
      });
      
      $j(".categoryBtn").click(function(){
    	  var flag=true;
    	  $j("#cLabelExists").hide();
    	  if($j("#categoryForm").valid()){
    		  saveCategory("Add Category",0);
    	  }
      });
//      $j(".domainTxtFld").focus(function(){
//    	  var txt =  $j(".domainTxtFld")
//    	  
//      });
      
      $j(".roleBtn").click(function(){
    	  var flag=true;
    	  $j("#rLabelExists").hide();
    	  
    	  if($j("#roleForm").valid()){
    		  saveRole("Add Role",0);
    	  }
      });
      $j(".commonCancelBtn ").click(function(){
    	  window.location=window.location.href;
      });
      $j(".addLangBtn").click(function(){
    	  $j(".labelExists").hide();
    	  $j(".codeExists").hide();
    	  if($j("#langForm").valid()){
    		  saveLanguage("Add Language",0);	
    		  $j("#language").val('');
    	 	  $j("#code").val('');
    	  }
	  		
});
      $j("#addNewVT").click(function(){
    	  if($j("#vtForm").valid()){
    		  saveVoteConfig("Add Vote",0);
    	  }  		
});
     
      
      
    $j("#language").focus(function(){
			 $j(".labelExists").hide();
			 $j("#emailReq").hide();
		});
	$j("#code").focus(function(){
				 $j(".codeExists").hide();
				 $j("#userReq").hide();
				
		});
		
	$j("#category").focus(function(){
			   $j("#cLabelExists").hide();
	   });
	   
	 $j("#domain").focus(function(){
		   $j("#dLabelExists").hide();
	   });
	
      $j(".updateEmailBtn").click(function(){
    	  	  var flag=true;
    	  if($j("#emailtmpl").val()=="slct"){
//    		  $j(".slctTmplate").show(); 
    		  alertMessage("#templateAlert");
    	  }
		 if($j("#emailtmpl").val()==0){
//			   	  if($j("#newSub").val() == ""){
//			         $j("#subjectReq").show();  
//			         flag=false;
//		        }else{
    	        if($j("#templateForm").valid()){
		    	    	$j('#mailTemplate').hide();
		               emailTemplate("Add Template",0);
		               $j('#newTmpl').hide();
		               $j('#newSub').val('');
		               $j('#content').val('');
		               $j("#subjectReq").hide(); 
		    	
		      }
	        }
		 
		 
		 if($j("#emailtmpl").val()!="slct" && $j("#emailtmpl").val()!=0){
			 emailTemplateId = $j("#emailtmpl").val();
			 emailTemplate("Edit Template",emailTemplateId);
			 $j('#content').val('');
		 }
		 
		});
     
	$j(".next").click(function(){
			if($j(".next").hasClass("nextEnable")){
				var colName = savedCriteria.colName;
				var sortOrder = savedCriteria.sortOrder;
				var pageNum = (savedCriteria.pageNum == 0)? (savedCriteria.pageNum +2):(savedCriteria.pageNum+ 1);
				savedCriteria.pageNum = pageNum;
				languageLookUp(colName, sortOrder, pageNum)
			};
	});
		
	$j(".previous").click(function(){
		if($j(".previous").hasClass("prevEnable")){
				var colName = savedCriteria.colName;
				var sortOrder = savedCriteria.sortOrder;
				var pageNum = savedCriteria.pageNum -1;
				languageLookUp(colName, sortOrder, pageNum)
		}
			
	});
	
	$j(".configtblHead div").click(function(){
		var colName =  $j(this).attr('id');
		if( $j("#configSlct option:selected").val()=="lp"){
			var sortOrder = $j(this).attr('sortOrder');
			if((colName == "column5")||(colName == "column0")){
				return;
			}
			if(!($j(this).hasClass("ascending")) && !($j(this).hasClass("descending"))){
				$j(this).attr('sortOrder', 'DESC');
				$j(".configtblHead div").each(function(index) {
					$j(this).removeClass("ascending descending");
					$j(this).find('.sort').remove();
				});
				$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
				$j(this).addClass("ascending");
			}else if($j(this).hasClass("ascending")){
					$j(this).attr('sortOrder', 'ASC');
					$j(".configtblHead div").each(function(index) {
						$j(this).removeClass("ascending descending");
						$j(this).find('.sort').remove();
					});
					$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/descend.png" height="5px" width="10px" class="sort leftmargin5" />');
					$j(this).addClass("descending");
			}else if($j(this).hasClass("descending")){
					$j(this).attr('sortOrder', 'DESC');
					$j(".configtblHead div").each(function(index) {
						$j(this).removeClass("ascending descending");
						$j(this).find('.sort').remove();
					});
					$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
					$j(this).addClass("ascending");
				}
		
			languageLookUp(colName, sortOrder, 1);
		}
		if(($j("#configSlct option:selected").val()== "domain") || ($j("#configSlct option:selected").val()== "category") || ($j("#configSlct option:selected").val()== "roleName")){
			if(colName == "column2"){
				return;
			}
			if($j(this).attr('sortOrder') == 'desc' || $j(this).attr('sortOrder') == null){
				$j("#configtblHead div").removeAttr("sortOrder");
				$j(this).attr('sortOrder','asc');
				if($j("#configSlct option:selected").val()== "domain"){
				
					domainData = sortData(domainData,"domain", true);
				}else if($j("#configSlct option:selected").val()== "category"){
					categoryData = sortData(categoryData,"category", true);
				}else if($j("#configSlct option:selected").val()== "roleName"){
					roleData = sortData(roleData,"roleName", true);
				}
				$j(".configtblHead div").each(function(index) {
					$j(this).find('.sort').remove();
				});
				
				$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/ascend.png" height="5px" width="10px" class="sort leftmargin5"/>');
			}else{
				$j("#configtblHead div").removeAttr("sortOrder");
				$j(this).attr('sortOrder','desc');
				if($j("#configSlct option:selected").val()== "domain"){
					domainData = sortData(domainData,"domain", false);
				}else if($j("#configSlct option:selected").val()== "category"){
					categoryData = sortData(categoryData,"category", false);
				}else if($j("#configSlct option:selected").val()== "roleName"){
					roleData = sortData(roleData,"roleName", false);
				}
				$j(".configtblHead div").each(function(index) {
					$j(this).find('.sort').remove();
				});
				
				$j(this).append('<img src="'+$j("#contextRootPath").val()+'/images/descend.png" height="5px" width="10px" class="sort leftmargin5"/>');
			}
			if($j("#configSlct option:selected").val()== "domain"){
				$j('#domainList').html('');
				displayDomainList(domainData);
				$j("#noDataDomain").hide();
				$j("a").click(function(){
					if($j(this).hasClass("edit")){
						
						$j(this).parent().parent().find(".editDtl").show();
						$j(this).parent().parent().find(".viewDtl").hide();
						$j(this).hide();
						$j(this).parent().parent().find(".save").show();
					}
					
					if($j(this).hasClass("save")){
						var domainId=$j(this).attr("id");
						
						if(domainId.indexOf("domainsave")!=-1){
							domainId= domainId.replace("domainsave_","");
							saveDomain("Edit Domain",domainId);
							
						}		  
						
						$j(this).hide();
						$j(this).parent().parent().find(".edit").show();
						var newTxt = $j(this).parent().parent().find(".editDtl input").val();
						$j(this).parent().parent().find(".viewDtl").html(newTxt);
						$j(this).parent().parent().find(".editDtl").hide();
						$j(this).parent().parent().find(".viewDtl").show();
					}
					if($j(this).hasClass("delete")){
						id=$j(this).attr("id")
						deleteVal(id);
						
					}
					
				});

			}
			if($j("#configSlct option:selected").val()== "roleName"){
			$j('#roleList').html('');
			displayRoleList(roleData);
			$j("#noRoleData").hide();
			$j("a").click(function(){
				if($j(this).hasClass("edit")){
							
					$j(this).parent().parent().find(".editDtl").show();
					$j(this).parent().parent().find(".viewDtl").hide();
					$j(this).hide();
					$j(this).parent().parent().find(".save").show();
				}
				
				if($j(this).hasClass("save")){
					  var roleId=$j(this).attr("id");
				
					  $j("#roleIdExists").hide();
					  
					  if(roleId.indexOf("rolesave")!=-1){
						  roleId= roleId.replace("rolesave_","");
						 var roleName=$j("#input_"+ roleId).val();
						 var saveFlag = true;
						 var  id="input_"+roleId;
								 if($j.trim(roleName) == ""){
									 $j("#"+id).addClass("errorBorder");
									$j("#roleExists").show();
	     							 $j("#roleExists").html("<span class='redTxt'>*</span>Role is required"); 
									 saveFlag = false;
							  }else{
								  $j("#roleExists").hide();
								  $j("#"+id).removeClass("errorBorder");
							  }
								 if(saveFlag){
									saveRole("Edit Role",roleId);
							 }
											 
					  }		  
  

				}
				if($j(this).hasClass("delete")){
					 id=$j(this).attr("id");
					deleteVal(id);
					
					
				}
				
			});

		}
			
			if($j("#configSlct option:selected").val()== "category"){
				$j('#categoryList').html('');
				categoryEvents(categoryData);
			}
		}
		});
	
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
	
	var languageLookUp = function(colName, sortOrder, pageNum){
		$j("#langsList").html('');
		var criteria = {
				'colName':colName,
				'sortOrder':sortOrder,
				'pageNum': pageNum
		};
		savedCriteria = criteria;
		
		$t.getLanguageListBySortOrder(colName, sortOrder, pageNum,{
			success:function(data){
				$j(".langLabelExists").hide();
				$j(".langCodeExists").hide();
				if($j.browser.version=="9.0"){
					$j(".siteContainer").css("width","960px");
				}
				var langData = data;
				if(langData != null){
					var length = (langData.length >= displayListLimit)?displayListLimit:langData.length;
					var startLimit = 0;
					var endLimit = 0;
					
					if(pageNum == 0){
						totalRows = langData.length;
						noOfPages = Math.round(langData.length/5) ;
						noOfPages = (langData.length%5 < 3 && langData.length%5 > 0)? noOfPages+1:noOfPages;
						startLimit = 1;
						endLimit = (displayListLimit > totalRows)? totalRows : displayListLimit;
					}else{
						startLimit = ((pageNum - 1)* displayListLimit)+1;
						var tempLimit  = (pageNum) * displayListLimit;
						endLimit =(parseInt(tempLimit) > parseInt(totalRows))? totalRows :tempLimit ;
					}
					displayLanguageList(data);
					$j("#rangeOfList").html(startLimit + "-"+ endLimit);
					$j("#totalData").html(totalRows);
					pagination(noOfPages, pageNum);
				}else{
					$j("#noDataLangs").show();
				}
				
				$j("a").click(function(){
					if($j(this).hasClass("edit")){
				
						$j(this).parent().parent().find(".editDtl").show();
						$j(this).parent().parent().find(".viewDtl").hide();
						$j(this).hide();
						$j(this).parent().parent().find(".save").show();
					}
					
					if($j(this).hasClass("save")){
						  var languageId=$j(this).attr("id");
						  
						  if(languageId.indexOf("langSave")!=-1){
							  languageId= languageId.replace("langSave_","");
							  var languageLabel=$j("#inputLabel_"+ languageId).val();
							  var languageCode=$j("#inputCode_" + languageId).val();
							  var saveFlag = true;
							  var  id="inputLabel_"+languageId;
							  var  id2="inputCode_"+languageId;
							  if($j.trim(languageLabel) == ""){
									 $j("#"+id).addClass("errorBorder");
									$j(".langLabelExists").show();
									 $j(".langLabelExists").html("<span class='redTxt'>*</span>Language label is required"); 
									 saveFlag = false;
							  }else{
								  $j(".langLabelExists").hide();
								  $j("#"+id).removeClass("errorBorder");
							  }
							  if($j.trim(languageCode) == ""){
									 $j("#"+id2).addClass("errorBorder");
									 $j(".langCodeExists").show();
									 $j(".langCodeExists").html("<span class='redTxt'>*</span>Language code is required");
									 saveFlag = false;
							  }else{
								  $j(".langCodeExists").hide();
								  $j("#"+id2).removeClass("errorBorder");
							  }
							  if(saveFlag){
								  saveLanguage("Edit Language",languageId);
							  }
							 
						  }		  

					}
					if($j(this).hasClass("delete")){
						id=$j(this).attr("id");
						deleteVal(id);
												
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
		
	};
	 var domainLookUp = function(){
			$j("#domainList").html(''); 
			$t.getDomainList({
				success:function(data){
					if(data.length != 0){
						if($j.browser.version=="9.0"){
							$j(".siteContainer").css("width","960px");
						}
					$j("#idExists").html("");
					 $j("#idExists").hide();
					 $j("#domainExists").html("");
					 $j("#domainExists").hide();
					displayDomainList(data);
					$j("#noDataDomain").hide();
					$j("a").click(function(){
						if($j(this).hasClass("edit")){
					
							$j(this).parent().parent().find(".editDtl").show();
							$j(this).parent().parent().find(".viewDtl").hide();
							$j(this).hide();
							$j(this).parent().parent().find(".save").show();
						}
						
						if($j(this).hasClass("save")){
							  var domainId=$j(this).attr("id");
							  $j("#idExists").hide();
							  
							  if(domainId.indexOf("domainsave")!=-1){
								  domainId= domainId.replace("domainsave_","");
     							 var domainlabel=$j("#input_"+ domainId).val();
								 var saveFlag = true;
								 var  id="input_"+domainId;
										 if($j.trim(domainlabel) == ""){
											 $j("#"+id).addClass("errorBorder");
											$j("#domainExists").show();
			     							 $j("#domainExists").html("<span class='redTxt'>*</span>Domain is required"); 
											 saveFlag = false;
									  }else{
										  $j("#domainExists").hide();
										  $j("#"+id).removeClass("errorBorder");
									  }
										 if(saveFlag){
											saveDomain("Edit Domain",domainId);
									 }
													 
							  }		  
	      

						}
						if($j(this).hasClass("delete")){
							 id=$j(this).attr("id")
							deleteVal(id);
							
												
						}
						
					});
					}
					else{
						$j("#noDataDomain").show();
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
	 
	 
	 var rolesLookUp = function(){
			$j("#roleList").html(''); 
			$t.getAllUserTypeList({
				success:function(data){
					if(data!=null){
							if($j.browser.version=="9.0"){
							$j(".siteContainer").css("width","960px");
						}
					$j("#roleIdExists").html("");
					 $j("#roleIdExists").hide();
					 $j("#roleExists").html("");
					 $j("#roleExists").hide();
					 
					 displayRoleList(data);
					$j("#noRoleData").hide();
					$j("a").click(function(){
						if($j(this).hasClass("edit")){
									
							$j(this).parent().parent().find(".editDtl").show();
							$j(this).parent().parent().find(".viewDtl").hide();
							$j(this).hide();
							$j(this).parent().parent().find(".save").show();
						}
						
						if($j(this).hasClass("save")){
							  var roleId=$j(this).attr("id");
						
							  $j("#roleIdExists").hide();
							  
							  if(roleId.indexOf("rolesave")!=-1){
								  roleId= roleId.replace("rolesave_","");
								 var roleName=$j("#inputRole_"+ roleId).val();
								 var saveFlag = true;
								 var  id="inputRole_"+roleId;
										 if($j.trim(roleName) == ""){
											 $j("#"+id).addClass("errorBorder");
											$j("#roleExists").show();
			     							 $j("#roleExists").html("<span class='redTxt'>*</span>Role is required"); 
											 saveFlag = false;
									  }else{
										  $j("#roleExists").hide();
										  $j("#"+id).removeClass("errorBorder");
									  }
										 if(saveFlag){
											saveRole("Edit Role",roleId);
									 }
													 
							  }		  
	      

						}
						if($j(this).hasClass("delete")){
							 id=$j(this).attr("id");
							deleteVal(id);
							
												
						}
						
					});
					}
					else{
						$j("#noRoleData").show();
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
	 
	 var mailTemplate=function(){
			$j("#emailtmpl").html('');
			$t.getEmailTemplates({
				success:function(data){
					if($j.browser.version=="9.0"){
						$j(".siteContainer").css("width","960px");
					}
				displayEmailList(data);
				
				var url = $j(location).attr('href');
				
				
				if($j("#emailtmpl").val()=="slct"){
					if(url.indexOf("/hp/") != -1){
					    $j('#content').val(tempHtml+'<div>Please Add email content here</div>');
					}
					else{
						$j('#content').val('<div>Please Add email content here</div>');
					}
				}
				var emailTmpl = data;
				$j("#emailtmpl").change(function(){
					$j("#mailTemplate").hide();
					$j("#subjectReq").hide();
					 templateValidate.resetForm();
					$j(".slctTmplate").hide();
					if($j("#emailtmpl").val()=="slct"){
						if(url.indexOf("/hp/") != -1){
							$j('#content').val(tempHtml+'<div>Please Add email content here</div>');
						}else{
							$j('#content').val('<div>Please Add email content here</div>');
						}
					}
				  for(var i=0;i<emailTmpl.length;i++){
					  if($j("#emailtmpl").val()==0){
						  $j("#newTmpl").show();
							tinyMCE.execCommand( 'mceRemoveControl', false, 'textarea1' );
							tinyMCE.execCommand( 'mceAddControl', false, 'textarea1' );
							
							if(url.indexOf("/hp/") != -1){
								tinymce.activeEditor.setContent(tempHtml+'<div>Please Add email content here</div>');
							}else{
								tinymce.activeEditor.setContent('<div>Please Add email content here</div>');
							}
					  }	else{
					 
						  $j("#newTmpl").hide();
					     if($j(this).val()==emailTmpl[i].emailTemplateId){
						  emailTemplateId=$j(this).val();
						
						tinyMCE.execCommand( 'mceRemoveControl', false, 'textarea1' );
						tinyMCE.execCommand( 'mceAddControl', false, 'textarea1' );
						tinymce.activeEditor.setContent(emailTmpl[i].emailMessageContent);
						
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
		 
		 
	 };
 var  voteConfiguration=function(){
		$t.getVoteConfigList({
			 success:function(data){
			if(data != null){
//				 $j(".configLstItem ").html("");
				if($j.browser.version=="9.0"){
					$j(".siteContainer").css("width","959px");
				}
				$j("#dataVT .configLstItem").show();
				$j(".deleteVt").attr("id","deleteVt_"+ data.voteConfigId);
				$j(".saveVt").attr("id","saveVt_"+ data.voteConfigId);
				$j("#votePeriod").html(data.votingPeriod);
				$j("#votesPerTerm").html(data.votesPerUser);
				$j("#noDataVT").hide();
				$j(".addVT").hide();
				
				//to reset the config when we add vote cofig
				$j("#votePeriod").show();
				$j("#votesPerTerm").show();
				$j("#newVotePeriod").hide();
				$j("#newVotePerTrm").hide();
				 $j(".editVt").show();
				 $j(".saveVt ").hide();
			}
			else{
				$j("#noDataVT").show();
				$j(".addVT").show();
				$j("#dataVT .configLstItem").hide();
				
			}
				
			$j(".editVt").click(function(){
				var editDays = $j("#votePeriod").html();
				$j("#newVotePeriod").val(editDays);
				var editTxt = $j("#votesPerTerm").html();
				$j("#newVotePerTrm").val(editTxt);
				$j(this).parent().parent().find(".editDtl").show();
				$j("#newVotePeriod").show();
				$j("#newVotePerTrm").show();
				$j("#votesPerTerm").hide();
				$j("#votePeriod").hide();
				$j(this).hide();
				$j(".saveVt").show();
				
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
 
 var categoryLookUp = function(){
		$j("#categoryList").html(''); 
		$t.getCategoryList({
			success:function(data){
				if(data.length != 0){
					$j("#categoryExists").hide();
					$j("#cIdExists").hide();
					if($j.browser.version=="9.0"){
						$j(".siteContainer").css("width","960px");
					}
					$j("#configtblHead div").each(function(index) {
						$j(this).find('.sort').remove();
					});
					categoryEvents(data);
				}
				else{
					$j("#noDataCategory").show();
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
var roleLookup = function(){
	$j("#roleId").html(''); 
	$t.getAllUserTypeList({
		success:function(data){
			if(data!=null && data.length != 0){
				
				displayUserTypeList(data,"#roleId");
			}
		},
		error: function(xhr, textStatus, errorThrown){
			console.log(xhr.responseText);
			if(Boolean(xhr.responseText.message)){
				console.log(xhr.responseText.message);
			}
		}
	});
	$t.getPrevilegeList({
		success:function(data){
			if(data!=null && data.length != 0){
				DisplayprevilegesData(data);
                var roleId=$j("#roleId").val();
				//DisplayRoleprevileges(Constants.ROLES.SUPER_ADMIN);
                DisplayRoleprevileges(roleId);
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
var roleListTmpl = ['<div id="',
                      '',
                      '"  ><input type="checkbox" privId="',
                      '',
                      '"  />',
	                  '',
	                  '</div>'
	                 ];

var DisplayprevilegesData = function(data) {
	var previlegeData = data;
	var prvTmplClone = 	'<table width="100%">';
	for ( var count = 0; count < previlegeData.length; count++) {
		prvTmplClone=prvTmplClone+'<tr><td ><table width="100%"><tr><th align="left"><span defalutValue="menuClass'+count+'" class="privilegemenu plusImage">'+previlegeData[count].menu+'</span></th></tr><tr class=" nodisplay menuClass'+count+'"><td><table width="100%"><tr>';
		var privilegesBySubMenuList = previlegeData[count].privilegesBySubMenu;
		if(privilegesBySubMenuList!=null){
			for(var i=0;i<privilegesBySubMenuList.length;i++){
				var index=i+1;
				if(index%4==0){
					prvTmplClone=prvTmplClone+'</tr><tr>';
				}
					
				prvTmplClone=prvTmplClone+'<td align="left" width="33%" class="privilegesubmenuHeader"><span class="privilegesubmenu">'+privilegesBySubMenuList[i].subMenu+'</span>';
				var subMenuPrivilegesList=privilegesBySubMenuList[i].subMenuPrivileges;
				if(subMenuPrivilegesList!=null){
				
					for(var j=0;j<subMenuPrivilegesList.length;j++){
						//prvTmplClone=prvTmplClone+'<li><a >'+subMenuPrivilegesList[j].privilegeDesc+'</a><span id="'+subMenuPrivilegesList[j].privilegeId+'" ><input type="checkbox" privId="'+subMenuPrivilegesList[j].privilegeId+'"/><span></li>';
						prvTmplClone=prvTmplClone+'<table  width="100%"><tr><td align="left" ><span id="'+subMenuPrivilegesList[j].privilegeId+'" ><input type="checkbox" privId="'+subMenuPrivilegesList[j].privilegeId+'"/></span>'+subMenuPrivilegesList[j].privilegeDesc+'</td></tr></table>';
					}
				}
							
				prvTmplClone=prvTmplClone+'</td>';
			}

		}
		
		var menuPrivilegesList = previlegeData[count].menuPrivileges;

		if(menuPrivilegesList!=null){
			for(var j=0;j<menuPrivilegesList.length;j++){
				prvTmplClone=prvTmplClone+'<td><table><tr><td align="left" ><span id="'+menuPrivilegesList[j].privilegeId+'" ><input type="checkbox" privId="'+menuPrivilegesList[j].privilegeId+'"/></span>'+menuPrivilegesList[j].privilegeDesc+'</td></tr></table></td>';

			}
		}
		prvTmplClone=prvTmplClone+'</tr></table></td></tr></table></td></tr>'

	}
	prvTmplClone=prvTmplClone+'</table>';

	$j('#previlegesList').html("");
	$j('#previlegesList').append(prvTmplClone);
	$j('#previlegesList .privilegemenu').click(function() {
		 var divId =   $j(this).attr('defalutValue');
		 $j('.'+divId).toggle();
		 $j(this).toggleClass('plusImage');
		 $j(this).toggleClass('minusImage');
	});
	
};

var categoryEvents = function(data){
	displayCategoryList(data);
	$j("#noDataCategory").hide();
	$j("a").click(function(){
		if($j(this).hasClass("edit")){
	
			$j(this).parent().parent().find(".editDtl").show();
			$j(this).parent().parent().find(".viewDtl").hide();
			$j(this).hide();
			$j(this).parent().parent().find(".save").show();
		}
		
		if($j(this).hasClass("save")){
			  var categoryId=$j(this).attr("id");
			  
			  if(categoryId.indexOf("categorysave")!=-1){
				  categoryId= categoryId.replace("categorysave_","");
				  $j("#cIdExists").hide();
				  
				  var categorylabel=$j("#input_"+ categoryId).val();
					 var saveFlag = true;
						 var  id="input_"+categoryId;
						 if($j.trim(categorylabel) == ""){
							 $j("#"+id).addClass("errorBorder");
							$j("#categoryExists").show();
							 $j("#categoryExists").html("<span class='redTxt'>*</span>Category is required"); 
							 saveFlag = false;
					  }else{
						  $j("#categoryExists").hide();
						  $j("#"+id).removeClass("errorBorder");
					  }
						 if(saveFlag){
							 saveCategory("Edit Category",categoryId);
					 }
			 
			  }		  

		}
		if($j(this).hasClass("delete")){
			 id=$j(this).attr("id")
			deleteVal(id);
								
		}
		
	});
};
	$j(".saveVt").click(function(){
		var id=$j(this).attr("id");
		 
			  if(id.indexOf("saveVt")!=-1){
				  flag=true;
				 voteConfigId= id.replace("saveVt_","");
				 var newPeriod = $j.trim($j("#newVotePeriod").val());
				 var newNumVotes = $j.trim($j("#newVotePerTrm").val());
				 var patt1=/^[0-9]+$/g;
				 var period =  patt1.test(newPeriod);
				 if($j.trim($j("#newVotePeriod").val())==""){
					 $j("#newVotePeriod").addClass("errorBorder");
						$j("#periodExists").show();
						 $j("#daysExists").hide();
						 $j("#periodExists").html("<span class='redTxt'>*</span>Voting period is required");
						 flag=false;
				 }else if(!period){
					 $j("#periodExists").hide();
						 $j("#newVotePeriod").addClass("errorBorder");
						 $j("#daysExists").show();
						 $j("#daysExists").html("<span class='redTxt'>*</span>Voting period must be numeric value");
						 flag=false;
				} else{
						 $j("#daysExists").hide();
						 $j("#newVotePeriod").removeClass("errorBorder");
						 $j("#periodExists").hide();
						 $j("#periodExists").removeClass("errorBorder"); 
						 
					 }
				 var patt2=/^[0-9]+$/g;
				 var votes=patt2.test(newNumVotes);
			
				 if($j.trim($j("#newVotePerTrm").val())==""){
					 $j("#newVotePerTrm").addClass("errorBorder");
					 $j("#numVotesExists").show();
					 $j("#votesExists").hide();
					 $j("#numVotesExists").html("<span class='redTxt'>*</span>Votes for user is required");
						 flag=false;
				 }else  if(!votes){
					 $j("#numVotesExists").hide();
						 $j("#newVotePerTrm").addClass("errorBorder");
						 $j("#votesExists").show();
						 $j("#votesExists").html("<span class='redTxt'>*</span>Votes for user must be numeric value");  
						 flag=false;
				} else{
						 $j("#numVotesExists").hide();
						 $j("#newVotePerTrm").removeClass("errorBorder");
						 $j("#votesExists").hide();
						 $j("#newVotePerTrm").removeClass("errorBorder"); 
						 
					 }
			 if(flag){
			 	saveVoteConfig("Edit Vote",voteConfigId );
				 }
			  }	
	 

		
	});

	

	$j('#roleId').change(function(){
		var roleId=$j(this).val();
		DisplayRoleprevileges(roleId);
	});
	var DisplayRoleprevileges = function(roleId) { 
		$j('#previlegesList input:checkbox').each(function() {
			$j(this).attr('checked', false);
		});
		$t.getPrevilegesByRole(roleId,{
			success:function(data){
				if(data!=null && data.length > 0){
					for(i=0;i<data.length;i++){
						$j('#previlegesList input:checkbox').each(function() {
							var privId=$j(this).attr('privId');
							if(privId==data[i].privileges.privilegeId){
								$j(this).attr('checked', true);

							}
						});
					}

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
	$j('#updateRoles').click(function() {
		selectedTermIds="";
		separator="";
		if($j("#previlegesList input:checked").length == 0){
			alertMessage("#roleSelctMsg");
		}else{
		$j("#previlegesList input:checked").each(function(i) {
			var fileId = $j(this).parent().attr("id");
			selectedTermIds += separator+fileId;
			separator = ",";
		});
		if(selectedTermIds!=""){
 		 	var fileIdArray = selectedTermIds.split(",");
		 	var roleTypeId = parseInt($j("#roleId :selected").val());
			$t.updatePrevileges(fileIdArray,roleTypeId, {
				success : function(data) {
					 alertMessage('#updateUserRoles');
				},
				error : function(xhr, textStatus, errorThrown) {
					console.log(xhr.responseText);
					if (Boolean(xhr.responseText.message)) {
					}
				}
			});

		}
		}
	});
	$j(".deleteVt").click(function(){
		id=$j(this).attr("id");
           $j( "#delete_vtcnfm:ui-dialog" ).dialog( "destroy" );
		    	$j( "#delete_vtcnfm" ).dialog({
			resizable: false,
			height:150,
			width:300,
			modal: true,
			buttons: {
				"Delete": function() {
					deleteRow(id);
		       if(id.indexOf("deleteVt")!=-1){
			  voteConfigId= id.replace("deleteVt_","");
			 saveVoteConfig("Delete Vote", voteConfigId); 
			 $j('#daysExists').hide();
			 $j('#votesExists').hide();
			 $j('#period').val("");
			 $j('#user').val("");
			 $j('#periodExists').hide();
			 $j('#numVotesExists').hide();
//			 $j(".configLstItem").html("");
//			
	       }
		   	    $j( this ).dialog( "close" );
			 
		},
				Cancel: function() {
					$j( this ).dialog( "close" );
				}
			}
		});

	});
	
    jQuery().ready(function(){
		showRolePrivileges();
    	$j("#currentMenuPage").val(Constants.SUBMENU_ITEMS.CONFIGURATIONS);
		$j("#currentSuperMenuPage").val(Constants.MENU_ITEMS.ADMIN);	

		/**
		var configurationData=Constants.CONFIGURATION_OPTIONS;
		for ( prop in Constants.CONFIGURATION_OPTIONS) {
			var configurationOptions = slctOptionsTmpl;
			configurationOptions[1] = prop;
			configurationOptions[3] = configurationData[prop];
			$j("#configSlct").append(configurationOptions.join(""));
		}
		*/
    	if($j("#adminHeaderFlag").val() == "true"){

    		$j("#adminHeader").show();
    		$j('#admin').children("img").show();
		}
        if($j("#userHeaderFlag").val() == "true"){
        	$j("#userHeader").show();
        	$j('#termList').children("img").show();
		}
		
		$j('#admin').css('color','#0DA7D5');
		
		$j(".subMenuLinks a").last().css("border-right", "none");
		
		if($j.browser.version=="7.0"){
			$j(".menuArrowAdmn").css("top","26");
			$j(".termAttr").css("padding-bottom","10px");
		}
		if($j.browser.version=="9.0"){
			$j(".paddingseven").css("padding-left","11px");
			$j(".siteContainer").css("width","959px");
		}
		
		if($j.browser.msie || $j.browser.webkit){

			$j(".headerMenuLinks .headerMenuLink").css("padding-bottom","12px");

			}
        /**
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
				if(totalUsersPerMonth != null && totalUsersPerMonth!=""){
					if ($j("#hpsite").val() == "true") {
						dataXML.push("<chart showValues='0' showBorder='0' bgColor='#e6eaeb,#e6eaeb' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' yAxisMinValue='0' yAxisMaxValue='30' showToolTip='1' chartTopMargin='5' canvasbgColor='#e6eaeb' divLineColor='#595a5c' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
					} else {
						dataXML.push("<chart showValues='0' showBorder='0' bgColor='#032D39,#032D39' bgAlpha='100,100' canvasPadding='0px' yAxisMinValue='0' yAxisMaxValue='80' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#ffffff' divLineThickness='2' labelPadding='0'  baseFontColor='#907460' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
					}
				
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
		 */
		$j(".leftContainer").css("height","200px");	
		$j("#configSlct").val(0);
		
		$j('textarea.tinymce1').tinymce({
		      script_url : $j("#contextRootPath").val()+'/js-lib/jscripts/tiny_mce/tiny_mce.js',

	        // General options
	        mode : "textareas",
	        theme : "advanced",
	        height : 400,
	        plugins : "autolink,lists,spellchecker,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
	        content_css : $j("#contextRootPath").val()+'/css/font.css',
	        // Theme options
	        theme_advanced_buttons1 : "bold,italic,underline,|,justifyleft,justifycenter,justifyright,justifyfull,|,formatselect,fontselect,fontsizeselect,|,cut,copy,paste,pasteword,|,bullist,numlist",
	        theme_advanced_buttons2 : "outdent,indent,blockquote,|,undo,redo,|,link,unlink,image,code,|,insertdate,inserttime,preview,|,forecolor,backcolor,|,hr,|,sub,sup,|,charmap,iespell,|,ltr,rtl,|,spellchecker,|,template",
	        theme_advanced_buttons3 : "tablecontrols",
	        //theme_advanced_buttons4 : "search,replace,|,spellchecker,|,visualchars,nonbreaking,template,blockquote",
	        theme_advanced_toolbar_location : "top",
	        theme_advanced_toolbar_align : "left",
	        theme_advanced_statusbar_location : "bottom",
	        theme_advanced_resizing : true,
	        theme_advanced_fonts : tm_fonts,

	        // Skin options
	        skin : "o2k7",
	        skin_variant : "silver",

	        // Replace values for the template plugin
	        template_replace_values : {
	                username : "Some User",
	                staffid : "991234"
	        }
	});
	
		 voteValidate=$j("#vtForm").validate({
			rules: {
				period: {
				required: true,
				numericOnly:true
				},
				user: {
				required: true,
				numericOnly:true
				}
	        },
	        
			messages: {
				period: {
				required: "Voting period is required"
				}, 
				user: {
				required: "Votes per user is required"
				}
			}
		});	
		
	});
    $j.validator.addMethod('numericOnly', function (value) { 
    	   return /^[0-9]+$/.test(value); 
    	}, 'Please only enter numeric values (0-9)');
	
   domainValidate= $j("#domainForm").validate({
		rules: {
			domain: {
			required: true
			}
        },
        
		messages: {
			domain: {
			required: "Domain is required"
			}
		}
	});	
    
  roleValidate= $j("#roleForm").validate({
		rules: {
			role: {
			required: true
			}
       },
       
		messages: {
			role: {
			required: "Role is required"
			}
		}
	});	
    templateValidate=$j("#templateForm").validate({
		rules: {
			newSub: {
			required: true
			}
        },
        
		messages: {
			newSub: {
			required: "Subject is required"
			}
		}
	});	
    langsValidate=  $j("#langForm").validate({
		rules: {
			language: {
				required: true
			},
			code: {
				required: true
				}
        },
        
		messages: {
			language: {
				required: "Language label is required"
			},
			code: {
				required: "Language code is required"
			}
		}
	});	
   
    categoryValidate= $j("#categoryForm").validate({
		rules: {
			category: {
			required: true
			}
        },
        
		messages: {
			category: {
			required: "Category is required"
			}
		}
	});	
    
    $j("#configSlct").change(function(){
		tinyMCE.execCommand( 'mceRemoveControl', false, 'textarea1' );
		tinyMCE.execCommand( 'mceAddControl', false, 'textarea1' );
//		tinymce.activeEditor.setContent("");
		domainValidate.resetForm();
		roleValidate.resetForm();
		categoryValidate.resetForm();
		 langsValidate.resetForm();
		 templateValidate.resetForm();
		 voteValidate.resetForm();
		$j("#domain").removeClass("errorBorder");
		$j("#category").removeClass("errorBorder");
		$j("#newVotePeriod").removeClass("errorBorder");
		$j("#newVotePerTrm").removeClass("errorBorder");
		$j(".labelExists").hide();
		$j(".codeExists").hide();
		$j(".leftContainer").css("height","");
		$j('#subjectReq').hide();
		$j('#languageReq').hide();
		$j(".langLabelExists").hide();
		$j(".langCodeExists").hide();
		$j("#domainExists").hide();
		$j("#newTmpl").hide();
		$j("#categoryExists").hide();
		$j('#codeReq').hide();
		$j('#userReq').hide();
		$j('#periodReq').hide();
		$j('#subjectReq').hide();
		$j('#mailTemplate').hide();
		$j('.error').hide();
		$j(".configtblHead div").each(function(index) {
			$j(this).find('.sort').remove();
			$j(this).attr("sortOrder","ASC");
			$j(this).removeClass("ascending descending");
		});
		if($j(this).val() == "domain"){
			$j("#domainList").html("");
			domainLookUp();
			$j("#manageDomain").show();
			$j("#manageVT").hide();
			$j("#manageLangs").hide();
			$j("#manageEmailTmpl").hide();
			$j("#deleteEmailTmpl").hide();
			$j("#manageCategory").hide();
			$j("#managePrivileges").hide();
			$j("#manageRoles").hide();

			
		}
		if($j(this).val() == "lp"){
			$j("#langsList").html("");
			languageLookUp(null, null, 0);

			$j("#manageDomain").show();
			$j("#manageVT").hide();
			$j("#manageLangs").hide();
			$j("#manageEmailTmpl").hide();
			$j("#deleteEmailTmpl").hide();
			$j("#manageCategory").hide();
			$j("#managePrivileges").hide();
			$j("#manageRoles").hide();

		}


		if($j(this).val() == "vt"){
	
			voteConfiguration();	
			$j("#manageVT").show();
			$j("#votePeriod").show();
			$j("#votesPerTerm").show();
			$j("#manageLangs").hide();
			$j("#manageDomain").hide();
			$j("#manageEmailTmpl").hide();
			 $j(".editVt").show();
			 $j(".saveVt ").hide();
			 
			$j(".editDtl").hide();
			$j("#deleteEmailTmpl").hide();
			$j("#manageCategory").hide();
			$j("#managePrivileges").hide();
			$j("#manageRoles").hide();

			
		}
	
		if($j(this).val() == "lp"){
			$j("#manageLangs").show();
			$j("#manageDomain").hide();
			$j("#manageVT").hide();
			$j("#manageEmailTmpl").hide();	
			$j("#deleteEmailTmpl").hide();
			$j("#manageCategory").hide();
			$j("#managePrivileges").hide();
			$j("#manageRoles").hide();

		}
	
		if($j(this).val() == "et"){
			mailTemplate();
			$j("#manageEmailTmpl").show();
			$j("#manageDomain").hide();
			$j("#manageVT").hide();
			$j("#manageLangs").hide();
			$j("#deleteEmailTmpl").hide();
			$j("#manageCategory").hide();
			$j("#managePrivileges").hide();
			$j("#manageRoles").hide();

		}
		if($j(this).val() == "etDel"){
			
			$j("#emailSubList").html("");
			$t.getEmailTemplates({
				success:function(data){
					if($j.browser.version=="9.0"){
						$j(".siteContainer").css("width","959px");
					}
					if(data.length!=0){
					displayDelEmailList(data);
					$j("#noDataET").hide();
					$j("a").click(function(){
					
						if($j(this).hasClass("delete")){
							id=$j(this).attr("id");
							   $j( "#delete_mailcnfm:ui-dialog" ).dialog( "destroy" );
								$j( "#delete_mailcnfm" ).dialog({
							resizable: false,
							height:150,
							width:400,
							modal: true,
							buttons: {
								"Delete": function() {
							
									deleteRow(id);
											 if(id.indexOf("delTmpl")!=-1){
												 emailTemplateId= id.replace("delTmpl_","");
												 emailTemplate("Delete Template", emailTemplateId); 
										 
									 }
										 
															
									$j( this ).dialog( "close" );
									 
								},
								Cancel: function() {
									$j( this ).dialog( "close" );
								}
							}
						});

					}
						
					});
					}
					else{
						$j("#noDataET").show();
					}
							  
			},
			error: function(xhr, textStatus, errorThrown){
				console.log(xhr.responseText);
				if(Boolean(xhr.responseText.message)){
					console.log(xhr.responseText.message);
				}
			}
		});

	
			$j("#manageEmailTmpl").hide();		
			$j("#manageDomain").hide();
			$j("#manageCategory").hide();
			$j("#manageVT").hide();
			$j("#manageLangs").hide();
			$j("#managePrivileges").hide();
			$j("#deleteEmailTmpl").show();
			$j("#manageRoles").hide();
		}
		
		if($j(this).val() == "category"){
			$j("#categoryList").html("");
			categoryLookUp();
			$j("#manageCategory").show();
			$j("#manageDomain").hide();
			$j("#manageVT").hide();
			$j("#manageLangs").hide();
			$j("#manageEmailTmpl").hide();
			$j("#deleteEmailTmpl").hide();
			$j("#managePrivileges").hide();
			$j("#manageRoles").hide();
		}
		if($j(this).val() == "roleName"){
			$j("#roleList").html("");
			rolesLookUp();
			$j("#manageCategory").hide();
			$j("#manageDomain").hide();
			$j("#manageVT").hide();
			$j("#manageLangs").hide();
			$j("#manageEmailTmpl").hide();
			$j("#deleteEmailTmpl").hide();
			$j("#manageRoles").show();
			$j("#managePrivileges").hide();
		}
		if($j(this).val() == "mgRoles"){
			$j("#roleId").html("");
			roleLookup();
			$j("#manageCategory").hide();
			$j("#manageDomain").hide();
			$j("#manageVT").hide();
			$j("#manageLangs").hide();
			$j("#manageEmailTmpl").hide();
			$j("#deleteEmailTmpl").hide();
			$j("#managePrivileges").show();
			$j("#manageRoles").hide();
		}
		

	});
	
	$j(".deleteEmail").click(function(){
		 deleteVal($j(this).attr("id"));
      
	});


	
})(window, jQuery);