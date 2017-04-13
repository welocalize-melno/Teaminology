var modalRender = function($j) {
	
	return{
		
		overlay : function(linkClass, modalDivId, titleTxt){

			$j(linkClass).qtip(
	    			{
	    				id: 'modal', // Since we're only creating one modal, give it an ID so we can style it
	    				content: {
	    					text: $j(modalDivId),
	    					title: {
	    						text: titleTxt,
	    						button: true
	    					}
	    				},
	    				position: {
	    					my: 'center', // ...at the center of the viewport
	    					at: 'center',
	    					target: $j(window)
	    				},
	    				show: {
	    					event: 'click', // Show it on click...
	    					solo: true, // ...and hide all other tooltips...
	    					modal: true // ...and make it modal
	    				},
	    				hide: false,
	    				style: 'ui-tooltip-light'
	    			});
		},
	
		bubble : function(id,content,myPos,atPos){//alert(content);
			$j(id).qtip({
	            content: {
	               text: content 
	            },
	            position: {
	               my: myPos, // Use the corner...
	               at: atPos // ...and opposite corner
	            },
	            show: {
	                event: 'mouseenter'
	             },
	             hide: {
	                event: 'mouseleave'
	             }, // Don't specify a hide event either!
	            // hide: false,  Don't specify a hide event either!
	            style: {
	               //classes: 'ui-tooltip-shadow ui-tooltip-' + styles[i]
	            }
	         });
			
		}
		
	};
	
	
	
}(jQuery);