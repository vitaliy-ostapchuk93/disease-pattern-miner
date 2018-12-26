document.body.style.cursor = 'wait';

$(document).ready(function () {
    document.body.style.cursor = 'default';

    $('form').on("change", function () {
        document.body.style.cursor = 'wait';
    });
});