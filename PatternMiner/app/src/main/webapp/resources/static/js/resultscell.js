const LOADING_CONTENT = 'Loading...';

$(document).ready(function () {

    var timeoutId;

    $('.cell').hover(
        function () {
            let cell = $(this);

            let id = cell.attr('id');
            let seqKey = id.substr(0, id.length - 3);
            let groupKey = id.slice(-2);

            let tooltip = cell.find('.tooltip');

            console.log("Hovered cell: " + id);

            let params = {'seqKey': seqKey, 'groupKey': groupKey};

            cell.css({'border-style': 'solid', 'border-color': '#3273dc'});
            tooltip.html(LOADING_CONTENT);

            if (!timeoutId) {
                timeoutId = window.setTimeout(function () {
                    timeoutId = null;

                    var cellStats = (function () {
                        let stats = null;

                        $.ajax({
                            'async': false,
                            'data': params,
                            'url': "/resultscell",
                            'dataType': "json",
                            'success': function (data) {
                                stats = data;
                            }
                        });
                        return stats;
                    })();

                    console.log(cellStats);

                    let commonCodes = '<b>Pattern Code-Search [ICD-9 Code | SUP] :</b> <br>';
                    cellStats['commonCodes'].forEach(function (codeGroup) {
                        commonCodes += '<div class="column">';
                        commonCodes += '<div class="content">';
                        commonCodes += '<p>Codes for <b>' + codeGroup['groupOrdinal'] + '</b> [' + codeGroup['groupName'] + ']';
                        commonCodes += '</div>';
                        commonCodes += '<div class="field is-grouped is-grouped-multiline">';

                        codeGroup['topCodes'].forEach(function (topCode) {
                            commonCodes += '<div class="control">';
                            commonCodes += '<div class="tags has-addons">';
                            commonCodes += '<span class="tag">' + topCode['code'] + '</span>';
                            commonCodes += '<span class="tag is-info">' + topCode['count'] + ' %</span>';
                            commonCodes += '</div>';
                            commonCodes += '</div>';
                        });

                        commonCodes += '</div>';
                        commonCodes += '</div>';
                    });

                    /*
                    for (Map.Entry<ICDCode, Integer> code : topTen.entrySet()) {
                        float p = code.getValue() * 100.0f / matchingSequences.size();
                        if (p >= 5) {
                            patterns.append("<div class=\"control\">");
                            patterns.append("<div class=\"tags has-addons\">");
                            patterns.append("<span class=\"tag\">").append(code.getKey().getSmallCode()).append("</span>");
                            patterns.append("<span class=\"tag is-info\">");

                            DecimalFormat df = new DecimalFormat();
                            df.setMaximumFractionDigits(2);
                            patterns.append(df.format(p) + " %");

                            patterns.append("</span>");
                            patterns.append("</div>");
                            patterns.append("</div>");
                        }
                    }
                    */

                    tooltip.html(
                        "<b>Pattern found in:</b><br>" + cellStats['stats']['fileOfResult'] + "<br>" +
                        " [ absolute support = " + cellStats['stats']['absSup'] +
                        " , sequence-count (patients) = " + cellStats['stats']['seqCount'] +
                        " , relative support = " + cellStats['stats']['relSup'] + " ]" + "<br><br>" +
                        "<b>tTest for difference in gender:</b> " + cellStats['tTest'] + "<br><br><br>" +
                        commonCodes
                    );

                }, 500);
            }

        }, function () {
            if (timeoutId) {
                window.clearTimeout(timeoutId);
                timeoutId = null;
            }
            $(this).css({'border-style': 'none'});
        }
    );
});

