//A singleton utility class 

var Util = function ($) {
    return {
        //Compare two arrays and check whether they are equal or not
        compareArrays: function (arrayA, arrayB) {
            var a, b, i, a_type, b_type;
            // References to each other?
            if (arrayA === arrayB) {
                return true;
            }

            if (arrayA.length != arrayB.length) {
                return false;
            }
            // sort modifies original array
            // (which are passed by reference to our method!)
            // so clone the arrays before sorting
            a = jQuery.extend(true, [], arrayA);
            b = jQuery.extend(true, [], arrayB);
            a.sort();
            b.sort();
            for (i = 0, l = a.length; i < l; i += 1) {
                if (a[i] !== b[i]) {
                    return false;
                }
            }
            return true;
        },
        /*
         * @param - m, length of the text to warp.
         * @param - b, replace value after doing the warp.("\n","<br>").
         * @param - c, boolean value.
         */
        wordWrap: function (m, b, c, text) {
            if (!Boolean(text))
                return;
            var i, j, s, r = text.split("\n");
            if (m > 0) for (i in r) {
                for (s = r[i], r[i] = ""; s.length > m;
                     j = c ? m : (j = s.substr(0, m).match(/\S*$/)).input.length - j[0].length
                         || m,
                         r[i] += s.substr(0, j) + ((s = s.substr(j)).length ? b : "")
                    );
                r[i] += s;
            }
            return r.join("\n");
        },
        backgroundYellow: function (sourceTermText, searchText, searchType, caseflag) {
            if (!Boolean(sourceTermText))
                return;
            var matchedText = null;
            if (Boolean(caseflag)) {
                matchedText = sourceTermText.match(new RegExp(searchText, 'g'));
            } else {
                matchedText = sourceTermText.match(new RegExp(searchText, 'gi'));
            }

            if (matchedText != null) {
                for (var k = 0; k < matchedText.length; k++) {
                    var Re = null;
                    if (/^[a-zA-Z0-9]*$/.test(searchText)) {
                        if (searchType != null && (searchType == "Fuzzy" || searchType == "Exact")) {
                            Re = new RegExp('\\b' + matchedText[k] + '\\b', "g");
                        }
                        else {
                            Re = new RegExp('\\b' + matchedText[k], "g");
                        }
                    } else {
                        Re = new RegExp(matchedText[k], "g");
                    }
                    sourceTermText = sourceTermText.replace(Re, "<kbd>" + matchedText[k] + "</kbd>");
                }
            }
            return sourceTermText;
        },
        insertCommmas: function (value) {
            var length = value.length;
            var counter = 0;
            var returnValue = "";
            for (var i = length - 1; i >= 0; i--) {
                returnValue = value.charAt(i) + ((counter > 0 && counter % 3 == 0) ? ',' : '') + returnValue;
                counter++;
            }
            return returnValue;
        },
        isDate: function (txtDate) {
            var currVal = txtDate;
            if (currVal == '')
                return false;

            //Declare Regex
            var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/;
            var dtArray = currVal.match(rxDatePattern); // is format OK?

            if (dtArray == null)
                return false;

            //Checks for mm/dd/yyyy format.
            dtMonth = dtArray[1];
            dtDay = dtArray[3];
            dtYear = dtArray[5];

            if (dtMonth < 1 || dtMonth > 12)
                return false;
            else if (dtDay < 1 || dtDay > 31)
                return false;
            else if ((dtMonth == 4 || dtMonth == 6 || dtMonth == 9 || dtMonth == 11) && dtDay == 31)
                return false;
            else if (dtMonth == 2) {
                var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
                if (dtDay > 29 || (dtDay == 29 && !isleap))
                    return false;
            }
            return true;
        },
        startWaiting: function (msg, selector, floatRight) {
            if (selector) {
                if (typeof msg == 'undefined' || msg == null) {
                    msg = "&nbsp; Loading... please wait.";
                }
                var temp = '<div class="loading-msg alignCenter topmargin15"><img src="' + jQuery("#contextRootPath").val() + '/images/loading.gif" />' + msg + '</div>';
                selector.append(temp);

            }
        },
        stopWaiting: function (selector) {
            if (selector) {
                selector.find(".loading-msg").hide();
            }
        }
    }
}(jQuery);