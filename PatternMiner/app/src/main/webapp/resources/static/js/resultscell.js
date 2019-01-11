var time_id;
const LOADING_CONTENT = 'Loading...';

$(document).ready(function () {

    var timeoutId;

    $('.cell').hover(
        function () {
            let cell = $(this);
            let id = cell.attr('id');
            let tooltip = cell.find('.tooltip');

            console.log("Hovered cell: " + id);

            let params = {'seqKey': id.substr(0, id.length - 3), 'groupKey': id.slice(-2)};

            cell.css({'border-style': 'solid', 'border-color': '#3273dc'});
            tooltip.html(LOADING_CONTENT);

            if (!timeoutId) {
                timeoutId = window.setTimeout(function () {
                    timeoutId = null;

                    $.ajax({
                        url: "resultscell",
                        data: params,
                        dataType: 'json',
                        success: function (response) {
                            tooltip.html(response);
                        },
                        error: function (jqXHR, status, err) {
                            if (jqXHR.responseText) {
                                tooltip.html(jqXHR.responseText);
                            } else {
                                tooltip.html("<p>Fail to fetch cell data!</p>");
                            }
                        }
                    });
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

