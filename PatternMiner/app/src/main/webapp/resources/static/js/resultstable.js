$(document).ready(function () {

    var groupBy = function (xs, key) {
        return xs.reduce(function (rv, x) {
            (rv[x[key]] = rv[x[key]] || []).push(x);
            return rv;
        }, {});
    };

    var reduce = function (xs, key) {
        return xs.reduce(function (accumulator, currentValue) {
            return accumulator.concat(currentValue[key]);
        }, []);
    };

    var icdGroups = (function () {
        let groups = null;
        $.ajax({
            'async': false,
            'url': "/icdgroups",
            'dataType': "json",
            'success': function (data) {
                groups = data;
            }
        });

        return groups;
    })();


    $("#toggleICD").click(function () {
        $(".targetICD").toggle('slow');
    });

    $("#toggleFilter").click(function () {
        $(".targetFilter").toggle('slow');
    });

    $('#save-results').on("click", function () {
        $.ajax({
            url: "resultsdownload",
            success: function (response) {
                $("<a/>", {
                    "download": "resultTable.json",
                    "href": "data:application/json," + encodeURIComponent(response)
                }).appendTo("body")
                    .click(function () {
                        $(this).remove()
                    })[0].click()
            },
            error: function (jqXHR, status, err) {
                console.log("Failed to load exported JSON.")
            }
        });
    });
});