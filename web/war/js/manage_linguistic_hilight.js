var commonObj={};
(function ($j) {
	
    var selectedFieldsArray = [];//[19,20,21,22];    
    var staticTermFieldLength = 8;
    var staticEditTermsLength = 3;
    
    $j("#moreData").click(function(){
        $j(".fieldsConfig").toggleClass("nodisplay");
    }); 
    
    
	$j("#selectAllBtn").click(function(){
        $j('.fieldsConfig input:checkbox').attr('checked','checked');
    }); 
    
    $j("#none").click(function(){
        $j('.fieldsConfig input:checkbox').removeAttr('checked');
    }); 
 
    commonObj.termConfigFields = function() {
			 
	   	$j("#termDtlRowsList div.rowBg div.tPartsOfSpeech,div.tDomain,div.tCategory,div.viewDate,div.editDate").hide();      
	   	
        $j("#mngTrmDtlSectionHead div#POS,div#domain,div#category,div#pollExpirationDate").addClass("nodisplay");
        
        $j("#pollSpan").addClass("nodisplay");
        
        // Hide/Show selected fields
        if($j.inArray("19",selectedFieldsArray) != -1){
        	$j("#termDtlRowsList div.rowBg div.viewDate").show();
        	$j("#mngTrmDtlSectionHead div#pollExpirationDate").removeClass("nodisplay");        	
        	$j("#pollSpan").removeClass("nodisplay");    
        	
        	 
        	
        }
        if($j.inArray("20",selectedFieldsArray) != -1){
        	$j("#termDtlRowsList div.rowBg div.tPartsOfSpeech").show();
        	$j("#mngTrmDtlSectionHead div#POS").removeClass("nodisplay");
        }              
        if($j.inArray("21",selectedFieldsArray) != -1){
        	$j("#termDtlRowsList div.rowBg div.tCategory").show();
        	$j("#mngTrmDtlSectionHead div#category").removeClass("nodisplay");
        }
        if($j.inArray("22",selectedFieldsArray) != -1){
        	$j("#termDtlRowsList div.rowBg div.tDomain").show();
        	$j("#mngTrmDtlSectionHead div#domain").removeClass("nodisplay");
        }
        
       	commonObj.ConfigureFieldsWidth(selectedFieldsArray);
      
    }; 
    
    $j("#apply").click(function(){
     $j(".fieldsConfig").toggleClass("nodisplay");
     
   	 selectedFieldsArray = $j("input:checked").map(function(){
			 return $j(this).attr("value");
   	}).get();  
    	$t.saveConfiguredFields(selectedFieldsArray,{
           success: function (data) {   
            commonObj.termConfigFields();
            $j(".rowBg .twistie_open").each(function(){
              	$(this).click();
             });           
           },
           error: function (xhr, textStatus, errorThrown) {
                if (Boolean(xhr.responseText.message)) {
                   console.log(xhr.responseText.message);
               }
           }
       }); 
   	
   }); 
    
    //Dynamic width assignment to manage linguistic assets fields
    commonObj.ConfigureFieldsWidth = function(selectedFields) {
    	var fullWidth = $j('#manageTermTbl').width() - 175;
     	var finalFieldLength = staticTermFieldLength + selectedFields.length - staticEditTermsLength;
    	var fieldWidth = fullWidth/finalFieldLength;
    	$j('#manageTermTbl #mngTrmDtlSectionHead div').each(function(index, ele){
    		if(!$j(ele).hasClass('noWidth')) {
    			$j(ele).width(Math.round(fieldWidth) - 5);
    		}
    	})
    	$j('.rowBg .row div').each(function(index, ele){
    		if(!$j(ele).hasClass('noWidth')) {
    			$j(ele).width(Math.round(fieldWidth) - 5);
    		}
    	})
    }
    
    commonObj.reConfigCssForSmallPie = function() {
   	 	commonObj.ConfigureFieldsWidth(selectedFieldsArray);
    }
    
    
        // To get User configured fields	   
        $t.getUserConfiguredFields({
            success: function (data) {
            	
            	selectedFieldsArray = data.map(String);
                  $j.each(data, function( index, value ) { 
                	  $j(":checkbox[value="+value+"]").prop("checked","true");
                	});
                 commonObj.termConfigFields();
            },
            error: function (xhr, textStatus, errorThrown) {
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
     
		commonObj.textAreaAutoGrow = function(){
			$j('.definitionDescEdit,.usageDesc ').autogrow({flickering: false});
		}
        
		commonObj.trimMoreThanOnek = function(value){
			return $j.trim(value).substring(0, 1000);
		}
        
})(jQuery);