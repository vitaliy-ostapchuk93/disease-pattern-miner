$(document).ready(function () {


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