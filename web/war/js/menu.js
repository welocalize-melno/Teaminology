(function (window, $j) {


    var menuLinkTmpl = ['<a href="',
        '',
        '">',
        '',
        '</a>'
    ];
    var superMenuLinkTmpl = ['<a href="',
        '',
        '">',
        '<span class="headerMenuLink" id="adminHeader">',
        '',
        '<img  id="image" style="border:0px;" class="nodisplay menuArrowHeader"/></span></a>',
        '<span style="padding-left:5px;padding-right:10px;">|</span>',

    ];

    if ($j("#hpsite").val() == "true") {
        superMenuLinkTmpl = ['<a href="',
            '',
            '">',
            '<span class="headerMenuLink" id="adminHeader">',
            '',
            '</span></a>',
            '',

        ];
    }

    var displayMenu = function (data) {
        var menuData = data;
        var defaultDisplayPage = "";
        var onCount = 0;
        var clickedMenu = $j("#currentSuperMenuPage").val();
        for (var count = 0; count < menuData.length; count++) {
            var menuLinkTmplClone = superMenuLinkTmpl;
            if (count == 0) {
                menuLinkTmplClone[1] = menuData[count].pageUrl + '" class ="first';
                if ($j("#hpsite").val() == "true")
                    menuLinkTmplClone[1] = menuData[count].pageUrl;
                defaultDisplayPage = menuData[count].pageUrl;
                menuLinkTmplClone[4] = menuData[count].menuName;
            } else if (count == (menuData.length - 1)) {
                menuLinkTmplClone[1] = menuData[count].pageUrl + '" style="border-right:none;';
                if ($j("#hpsite").val() == "true")
                    menuLinkTmplClone[1] = menuData[count].pageUrl;
                menuLinkTmplClone[4] = menuData[count].menuName;
                menuLinkTmplClone[6] = "";
            } else {
                menuLinkTmplClone[1] = menuData[count].pageUrl;
                menuLinkTmplClone[4] = menuData[count].menuName;
            }

            if (clickedMenu == menuData[count].menuName.toUpperCase()) {
                var menuId = menuData[count].menuId;
                $t.getUserTypeSubMenus(menuId, {
                    success: function (data) {
                        if (data != null)
                            displaySubMenu(data);
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        console.log(xhr.responseText);
                        if (Boolean(xhr.responseText.message)) {
                            console.log(xhr.responseText.message);
                        }
                    }
                });
            }
            if ($j("#hpsite").val() != "true")
                menuLinkTmplClone[4] = menuLinkTmplClone[4].toUpperCase();

            $j('#header').append(menuLinkTmplClone.join(""));
        }

        $j('#header a span').each(function (index) {
            if (clickedMenu != $j(this).text().toUpperCase()) {
                $j(this).removeClass("on");
            } else {
                $j(this).addClass("on");
                if (clickedMenu != "DASHBOARD") {
                    $j(this).parent().find("#image").attr('src', $j("#contextRootPath").val() + "/images/arrow_top.gif").show();
                }
            }
        });


        var menuLinkTmplClone = superMenuLinkTmpl;
        menuLinkTmplClone[1] = "#";
        menuLinkTmplClone[4] = '<span style="padding-left:5px;padding-right:10px;">|</span><span style="margin:0px !important;" id="help">HELP</span>';

        if ($j("#hpsite").val() == "true")
            menuLinkTmplClone[4] = '<span id="help">HELP</span>';
        $j('#header').append(menuLinkTmplClone.join(""));

        $j('#header #help').click(function () {
            showHelpMenu();
            $j(this).addClass("on");
        });

        var showHelpMenu = function () {
            // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
            $j("#helpDiv:ui-dialog").dialog("destroy");

            $j("#helpDiv").dialog({
                height: 200,
                width: 350,
                modal: true,
                close: function (event, ui) {
                    $j('#header #help').removeClass("on");
                }
            });
        };

        $j("#adminDoc").click(function () {
            location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadAdminManual";
        });
        $j("#userDoc").click(function () {
            location.href = $j("#contextRootPath").val() + "/impExp_serv?c=downloadUserManual";
        });
    };


    var displaySubMenu = function (data) {
        var menuData = data;
        var defaultDisplayPage = "";
        var onCount = 0;
        for (var count = 0; count < menuData.length; count++) {
            var menuLinkTmplClone = menuLinkTmpl;
            if (count == 0) {
                menuLinkTmplClone[1] = menuData[count].pageUrl + '" class ="first';
                defaultDisplayPage = menuData[count].pageUrl;
            } else {
                menuLinkTmplClone[1] = menuData[count].pageUrl;
            }
            if (count == (menuData.length - 1)) {
                menuLinkTmplClone[1] = menuData[count].pageUrl + '" style="border-right:none;';
            }

            menuLinkTmplClone[3] = menuData[count].subMenuName;
            $j('#menuItems').append(menuLinkTmplClone.join(""));
        }

        var clickedMenu = $j("#currentMenuPage").val();
        $j('#menuItems a').each(function (index) {
            if (clickedMenu != $j(this).html()) {
                $j(this).removeClass("on");
            } else {
                $j(this).addClass("on");
            }
        });
    };

    $j().ready(function () {
        $t.getUserTypeMenus({
            success: function (data) {
                if (data != null)
                    displayMenu(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log(xhr.responseText);
                if (Boolean(xhr.responseText.message)) {
                    console.log(xhr.responseText.message);
                }
            }
        });
    });
})(window, jQuery);