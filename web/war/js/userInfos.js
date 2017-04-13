/**
 * Created with IntelliJ IDEA.
 * User: VincentYan
 * Date: 13-7-2
 * Time: ä¸‹å�ˆ2:52
 * To change this template use File | Settings | File Templates.
 */
(function(window, $j) {

    var showOrHide="show";
    var popup_sh = function() {

        if (showOrHide=="show") {
            $('.PopupDiv').show();
            var postion=$('.signUpButton').offset();

            if($.browser.version=="6.0")
            {
                $('.PopupDiv').css({'top':postion.top+44,'left':postion.left+83});}
            if($j.browser.version=="7.0")
            {
                $('.PopupDiv').css({'top':postion.top+33,'left':postion.left-94});
                $('.PopupDiv').css('height','220px');
                $(".registerLnk").css('margin-top','5px');

            }
            else
                $('.PopupDiv').css({'top':postion.top+33,'left':postion.left-102});
            $('img#logon').attr('src',$j("#contextRootPath").val()+'/images/up.gif');

            showOrHide = "hide";
        }
        else if(showOrHide=="hide")  {
            $('.PopupDiv').hide();
            $('img#logon').attr('src',$j("#contextRootPath").val()+'/images/down.gif');
            showOrHide = "show";
        }
    };

    if ($(".showhide")) {
        $(".showhide").click(function(){
            $j("#userError").hide();
            $("#username").val("");
            $("#password").val("");
            $j("#username").removeClass("error");
            $j("#pwdError").hide();
            $j("#password").removeClass("error");
            $j(".invalidDtls").hide();

            popup_sh();
        });
    };

    var userDetailTmpl = ['<div class="userImg" style="margin-top:9px;"><img id="userChangeImg"  src="',
        '',
        '" height="50px" width="50px" /></div><div class="userDetails"><h5>',
        '',
        '</h5><p class="smallFont userVotes ">',
        '',
        ' votes</p><p class="smallFont">Member since: ',
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
        userDetailTmplClone[5] = Util.insertCommmas(totalTerms1);
        userDetailTmplClone[7] = userDetails.createDate;
        $j("#changeImgId").attr('src', userDetails.photoPath);
        $j('#userInfo').append(userDetailTmplClone.join(""));
        if($j("#adminHeaderFlag").val() != "true"){
            var badgingRate = userDetails.totalVotes + userDetails.termRequestCount;
            if(badgingRate>=0 && badgingRate<50){
                $j("#bagingImgId").html('<img src="'+$j("#contextRootPath").val()+'/images/BeginnerBadge.gif"/> &nbsp; <img src="'+$j("#contextRootPath").val()+'/images/hp-logo-blue.jpg" height="20px" width="20px" />');
            }else if(badgingRate>=50 && badgingRate<150){
                $j("#bagingImgId").html('<img src="'+$j("#contextRootPath").val()+'/images/AdvancedBadge.gif"/> &nbsp; <img src="'+$j("#contextRootPath").val()+'/images/hp-logo-blue.jpg" height="20px" width="20px" />');
            }else if(badgingRate>=150){
                $j("#bagingImgId").html('<img src="'+$j("#contextRootPath").val()+'/images/ExpertBadge.gif"/> &nbsp; <img src="'+$j("#contextRootPath").val()+'/images/hp-logo-blue.jpg" height="20px" width="20px" />');
            }
        }else{
            $j(".userVotes").hide();
        }
    };

    var accuracyChartData=function(){
        $t.getUserAccuracyRate({
            success:function(data){
                var finalisedTerms = data.finalizedTerm;
                var votedTerms = data.votedTerms;
                var dataXML = new Array();
                if(votedTerms==0) {
                    $j("#accuracyRate").html("0%");
                    if ($j("#hpsite").val() == "true")
                        dataXML.push("<chart showPercentValues='1'  bgColor='#F9F9F9,#F9F9F9' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#0e94bc, #D1EAF1'  showplotborder='0' legendshadow='0' legendborderalpha='0' use3dlighting='0' legendbgcolor='CCCCCC' canvasbordercolor='CCCCCC' canvasborderthickness='0' canvasbgColor='#F9F9F9' showBorder='0' bgAlpha='100'>");
                    else
                        dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");

                    dataXML.push("<set label='Approved' value='"+finalisedTerms+"' />");
                    dataXML.push("<set label='Incorrect' value='"+(votedTerms-finalisedTerms)+"' />");
                    dataXML.push("</chart>");
                    ChartRender.twoDPieChart("0",130,70, "accuracyPie", dataXML );

                }else{

                    var accuracyRate = Math.round((finalisedTerms/votedTerms) * 100);
                    if(!(isNaN(accuracyRate))) {
                    	$j("#accuracyRate").html(accuracyRate+"%")
                    } else {
                    	$j("#accuracyRate").html("0%")
                    }
                    if ($j("#hpsite").val() == "true")
                        dataXML.push("<chart showPercentValues='1'  bgColor='#F9F9F9,#F9F9F9' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#0e94bc, #D1EAF1'  canvasbgColor='#F9F9F9' showplotborder='0' legendshadow='0' legendborderalpha='0' use3dlighting='0' legendbgcolor='CCCCCC' canvasbordercolor='CCCCCC' canvasborderthickness='0'  showBorder='0' bgAlpha='100'>");
                    else
                        dataXML.push("<chart showPercentValues='1' showLabels='0' showValues='0' pieRadius='28' slicingDistance='0' showShadow='0' animation='0' paletteColors='#7CC242, #FFC568' bgColor='#032D39' showBorder='0' bgAlpha='100'>");
                    dataXML.push("<set label='Approved' value='"+finalisedTerms+"' />");
                    dataXML.push("<set label='Incorrect' value='"+(votedTerms-finalisedTerms)+"' />");
                    dataXML.push("</chart>");
                    ChartRender.twoDPieChart("0",130,70, "accuracyPie", dataXML );
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

    $j().ready(function(){
        // Load CSS file
        if ($j("#headerFlag").val() == "true") {
            $j(".userInfoContainer").show();
            $j("#header").show();
        } else {
            $j(".userInfoContainer").hide();
            $j("#header").hide();
        }

        // Admin user
        if($j("#adminHeaderFlag").val() == "true"){
//            $j("#adminHeader").show();
//            $j('#adminDashboard').css('color','#0DA7D5');
//            $j('#adminDashboard').children("img").show();
            $j(".accuracyChrt").hide();
            $j(".admnNumUsersChrt").css("top", "9px");
        }

        if($j("#userHeaderFlag").val() == "true"){
//            $j("#userHeader").show();
//            $j('#userDashboard').children("img").show();
            $j(".accuracyChrt").show();
            $j(".newTrmRqst").show();
            $j(".numUsersChrt").addClass("rightmargin15");
            accuracyChartData();
        }

        if ($j.browser.msie || $j.browser.webkit) {
            $j(".headerMenuLinks .headerMenuLink").css("padding-bottom", "12px");
        }
        if ($j.browser.version == "9.0") {
            $j(".paddingseven").css("padding-left", "11px");
        }
        if ($j.browser.version == "7.0") {
            $j(".menuArrowAdmn").css("top", "26");
            $j(".termAttr").css("padding-bottom", "10px");
            $j(".termRqstBtn").css("padding","5px 0");
        }

        if($j("#headerFlag").val() == "true"){
            $j(".signUpButton").hide();
            $j("#signOutLink").show();
            $j(".welcomeMsg").show();
            $j(".userInfoContainer").show();

            $t.getUserDetails({
                success:function(data){
                    displayUserDetails(data);
                    $j("#welcomeMsg").html("Welcome, "+data.userName);
                },
                error: function(xhr, textStatus, errorThrown){
                    console.log(xhr.responseText);
                    if(Boolean(xhr.responseText.message)){
                        console.log(xhr.responseText.message);
                    }
                }
            });
        }
        if(notValidLogin != ""){
            popup_sh();
            if($j.browser.version=="7.0")
            {
                $j('.PopupDiv').css('height','225px');
            }
        }

        $t.getActiveUsersCount({
            success:function(data){
                if(data != null){
                    var totalTerms1 = new String( data);
                    var totalTerms2 = Util.insertCommmas(totalTerms1);
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
                    	dataXML.push("<chart showValues='0' adjustDiv='0' plotbordercolor='0e94bc' plotborderthickness='3' anchorbgcolor='0e94bc' anchorborderthickness= '0' showshadow='0' showBorder='0' bgColor='#f9f9f9,#f9f9f9' chartData='032D39' bgAlpha='100,100' baseFont='HP Simplified' canvasPadding='0px' numDivLines='3'  yaxisvaluespadding='20'  showcanvasborder='0'  anchorradius='4' divLineDashGap='2' yAxisValuesStep='2' chartrightmargin='25'  canvasborderalpha='0' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#f9f9f9' divLineColor='#d3d3d3' divLineThickness='2' labelPadding='0'  baseFontColor='#595a5c' chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='2' lineColor='#0e94bc' canvasBorderThickness='0'>");
                       // dataXML.push("<chart caption='Total Revenues from 2008-2013' numberprefix='$' bgcolor='FFFFFF' showalternatehgridcolor='0' plotbordercolor='008ee4' plotborderthickness='3' showvalues='0' divlinecolor='CCCCCC' showcanvasborder='0' tooltipbgcolor='00396d' tooltipcolor='FFFFFF' tooltipbordercolor='00396d' numdivlines='2' yaxisvaluespadding='20' anchorbgcolor='008ee4' anchorborderthickness='0' showshadow='0' anchorradius='4' chartrightmargin='25' canvasborderalpha='0' showborder='0'>");
                    else
                    	dataXML.push("<chart showValues='0' adjustDiv='0' showBorder='0' bgColor='#032D39,#032D39' bgAlpha='100,100' canvasPadding='0px' numDivLines='3' yAxisValuesStep='2' yAxisMinValue='"+array[0]+"' yAxisMaxValue='" +totaluser+"' showToolTip='1' chartTopMargin='5' canvasbgColor='#032D39' divLineColor='#d3d3d3' yaxisvaluespadding='20'  divLineThickness='2' divLineDashGap='2' labelPadding='0'  baseFontColor='#C0C0C0' anchorradius='4' chartrightmargin='25' canvasborderalpha='0'  chartLeftMargin='20' baseFontSize='9' showAlternateHGridColor='0' lineThickness='5' lineColor='#6ab53a' canvasBorderThickness='0'>");
                    for(var count=0;count<totalUsersPerMonth.length;count++) {
                        dataXML.push("<set label='"+totalUsersPerMonth[count].month + "' " + "value='" +totalUsersPerMonth[count].count +"'/>");
                    }
                    dataXML.push("</chart>");
                    ChartRender.twoDLineChart("myChartId3", "300", "100", "userChart", dataXML);
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