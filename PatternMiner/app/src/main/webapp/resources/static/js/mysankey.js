// Load the Visualization API
google.charts.load("current", {packages: ["sankey"]});

function drawChart(rows, colors, icdNames) {
    var data = new google.visualization.DataTable();

    data.addColumn('string', 'Source');
    data.addColumn('string', 'Target');
    data.addColumn('number', 'Support');

    if (rows.length === 0) {
        document.getElementById('sankey').innerHTML = '<div class="column"><h2 class="subtitle has-text-danger">No data available to create Sankey-Chart!</h2></div>';
        return;
    }

    data.addRows(rows);

    // Set chart options
    var options = {
        sankey: {
            node: {
                interactivity: true,
                colors: colors,
                width: 20,
                label: {
                    fontName: 'Times-Roman',
                    fontSize: 14,
                }
            }
        }
    };

    // Instantiate and draw our chart, passing in some options.
    let chart = new google.visualization.Sankey(document.getElementById('sankey'));

    var btnSave = document.getElementById('save-png');

    google.visualization.events.addListener(chart, 'ready', function () {
        btnSave.disabled = false;
    });

    btnSave.addEventListener('click', function () {
        let imgData = getImgData(document.getElementById('sankey'));
        let download = document.getElementById("download");

        let image = imgData.replace("image/png", "image/octet-stream");
        download.setAttribute("href", image);

    }, false);


    chart.draw(data, options);

    google.visualization.events.addListener(chart, 'onmouseover', function (event) {
        setTooltipContent(event, rows);
    });
}

function getTotalIn(data, name) {
    let inSup = 0;

    data.forEach(link => {
        if (link[1] === name) {
            inSup += link[2];
        }
    });

    return inSup;
}

function getTotalOut(data, name) {
    let outSup = 0;

    data.forEach(link => {
        if (link[0] === name) {
            outSup += link[2];
        }
    });

    return outSup;
}

function setTooltipContent(event, data) {
    if (event.hasOwnProperty('name')) {
        let rects = $("svg > g > rect");
        let rect = rects[event.row];

        let inSup = getTotalIn(data, event.name);
        let outSup = getTotalOut(data, event.name);

        var tooltip = '<p>' + '<b>' + event.name + '</b>';

        if (inSup !== 0) {
            tooltip += '<br>Total In-Support: ' + inSup;
        }
        if (outSup !== 0) {
            tooltip += '<br>Total Out-Support: ' + outSup + '</p>';
        }

        tippy(rect, {content: tooltip});
    }
}

function valueUpdate() {
    document.body.style.cursor = 'wait';

    let age = $('#ageValue').val();
    let patternKey = $('#patternKey').val();
    //let patternGender = $('#patternGender').val();
    var gender = $("#gender option:selected").val();

    $('#agelabel').text('Age: ' + (Math.round(age / 10) * 10) + ' to ' + (10 + Math.round(age / 10) * 10));

    let groupKey = Math.round(age / 10) + '' + gender.charAt(0);

    let params = {'patternKey': patternKey, 'patternGroupKey': groupKey, 'ageValue': age};

    $.get("/linkValues", $.param(params), function (response) {
        let rows = [];

        response.icdLinks.links.forEach(link => {
            let s = link.source + " : " + response.icdNamesMap[link.source];
            let t = link.target + " : " + response.icdNamesMap[link.target];
            rows.push([s, t, link.value]);
        });

        drawChart(rows, response.icdLinks.colors, response.icdNamesMap);
        document.body.style.cursor = 'default';
    });

    let download = document.getElementById("download");
    download.setAttribute("download", "sankey_" + patternKey + "_" + groupKey + ".png");
}


function getImgData(chartContainer) {
    var chartArea = chartContainer.getElementsByTagName('svg')[0].parentNode;
    var svg = chartArea.innerHTML;
    var doc = chartContainer.ownerDocument;
    var canvas = doc.createElement('canvas');
    canvas.setAttribute('width', chartArea.elemWidth);
    canvas.setAttribute('height', chartArea.elemHeight);


    canvas.setAttribute(
        'style',
        'position: absolute; ' +
        'top: ' + (-chartArea.elemHeight * 2) + 'px;' +
        'left: ' + (-chartArea.elemWidth * 2) + 'px;');
    doc.body.appendChild(canvas);
    canvg(canvas, svg);

    var imgData = canvas.toDataURL("image/png");
    canvas.parentNode.removeChild(canvas);
    return imgData;
}

function saveAsImg(chartContainer) {
    var imgData = getImgData(chartContainer);

    // Replacing the mime-type will force the browser to trigger a download
    // rather than displaying the image in the browser window.
    window.location = imgData.replace("image/png", "image/octet-stream");
}



$('#ageValue').on('input', function () {
    valueUpdate();
});

$('#gender').on('change', function () {
    valueUpdate();
});


$(document).ready(function () {
    valueUpdate();
});