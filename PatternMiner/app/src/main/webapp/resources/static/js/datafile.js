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


    $('#fileUpload').on('change', function (event) {
        console.log(event);
        /*
        $.ajax({
            url: '/upload',
            data: $('#file').attr('files'),
            cache: false,
            contentType: 'multipart/form-data',
            processData: false,
            type: 'POST',
            success: function (data) {
                alert(data);
            }
        });
        */
    });


    $('#downloadExternal').on('click', function (event) {
        let params = {"externalURL": $("#externalURL").val()};
        $.post("/externalSource", $.param(params), function (response) {
            console.log(response);
        })
    });

    $('#inverseSearch').on('click', function (event) {
        $.post("/createLinks", {}, function () {
            console.log("Updated FullLink-Files.");
        });
    });

});