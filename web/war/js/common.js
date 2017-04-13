(function (window, $j) { 
    var teaminology = function () {}, globalVar = {};

    window.teaminology = window.$T = window.$t = teaminology;
    window.$g = globalVar;
    var _HPEPOpenWindow = function(url,windowName){
        window.open(url);
    };
    window._HPEPOpenWindow=_HPEPOpenWindow;
    var url = $j(location).attr('href');
    var hrefParts = url.split('/');
    if(hrefParts[hrefParts.length - 1] == ""){
    	$j('#about').css('margin-left','-180px');
    }
   
    var showRolePrivileges = function () {
        // To get UserPrevileges
        $t.getUserRolePrevileges({
            success: function (data) {
                if (data != null) {
                    for (var i = 0; i < data.length; i++) {
                    	if (data[i].privileges.jsId != null) {
                    		if (data[i].privileges.jsId == "pickFinalBtn") {
                    			if ($g.termDtlOpenEle != null) {
                    				$g.termDtlOpenEle.find('.' + data[i].privileges.jsId).show();
                    			}
                    		} else {
                    			$j('.' + data[i].privileges.jsId).show();
                    		}
                    	}
                    }
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
    };

    window.showRolePrivileges = showRolePrivileges;
    var adminHeaderFlag = $j("#adminHeaderFlag").val();
    window.adminHeaderFlag = adminHeaderFlag;

    //page specific JS
    $j.extend({
        trimText: function (txt, len, ellipsisAtEnd) {
            if (txt == null) return '';
            if (txt.length <= len) {
                return txt;
            }
            var last3 = txt.substring(txt.length - 3, txt.length);
            if (ellipsisAtEnd) {
                return txt.substring(0, len - 3) + '...';
            }
            return txt.substring(0, len - 3) + '...';
            //return txt.substring(0, len - 5) + '..' + last3;
        }
    });
    // For logout from the application
    $j("#signOut").click(function() {
    	$t.getSignoutFlag({
    		success:function(data){
    			logOutData = data;
    			var isHpUrl = $j(location).attr('href');
    			var url = "";
    			//if(isHpUrl.indexOf("/apple") != -1){
					url = $j("#contextRootPath").val() + "/avigilon/index.jsp";  
    				//url = $j("#contextRootPath").val() + "/govtcanada/index.jsp";  
    			/*} else {
    				url = $j("#contextRootPath").val() + "/apple/index.jsp";
    			}*/
    			location.href = url;		
    		}
    	});
    });
})(window, jQuery);