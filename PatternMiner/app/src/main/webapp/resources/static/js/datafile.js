$(document).ready(function () {

    $('#optData').multiSelect({
        selectableOptgroup: true,
        afterSelect: function (values) {
            let params = {"selected": values};
            $.post("/data", $.param(params), function (response) {
            })
        },
        afterDeselect: function (values) {
            console.log("Deselect value: " + values);
            let params = {"deselected": values};
            $.post("/data", $.param(params), function (response) {
            })
        }
    });

});