var LONGITUDE_ARRAY = null;
var LATITUDE_ARRAY = null;
var LAC_ARRAY = null;
var MCC_ARRAY = null;
var MNC_ARRAY = null;
var BST_LAT_ARRAY = null;
var BST_LON_ARRAY = null;
var SIGNAL_STRENGTH_ARRAY = null;
var OPERATOR_NAME_ARRAY = null;
var NETWORK_SPEED_UP_ARRAY = null;
var NETWORK_SPEED_DOWN_ARRAY = null;
var COUNTRY_CODE_ARRAY = null;
var OPERATOR_CODE_ARRAY = null;
var MARKER_COLOUR_ARRAY = null;
var MARKER_SHAPE_ARRAY = null;
var DESCRIPTION_TEXT_ARRAY = null;
var MARKER_SIZE_ARRAY = null;

$(document).ready(function () {
    initialize();
    $.ajax({
        type: "GET",
        url: "assests/geo.lg.json",
        dataType: "text",
        success: function (data) {
            processData(data);
        }
    });
});

function initialize() {
    LONGITUDE_ARRAY = []
    LATITUDE_ARRAY = []
    LAC_ARRAY = []
    MCC_ARRAY = []
    MNC_ARRAY = []
    BST_LAT_ARRAY = []
    BST_LON_ARRAY = []
    SIGNAL_STRENGTH_ARRAY = []
    OPERATOR_NAME_ARRAY = []
    NETWORK_SPEED_UP_ARRAY = []
    NETWORK_SPEED_DOWN_ARRAY = []
    COUNTRY_CODE_ARRAY = []
    OPERATOR_CODE_ARRAY = []
    MARKER_COLOUR_ARRAY = []
    MARKER_SIZE_ARRAY = []
    DESCRIPTION_TEXT_ARRAY = []
}
function processData(data) {
    var json_data = JSON.parse(data);
    // console.log(json_data);
    for (let i = 0; i < json_data.length; i++) {
        const element = json_data[i];
        // console.log(element)
        LONGITUDE_ARRAY.push(element.LONGITUDE)
        LATITUDE_ARRAY.push(element.LATITUDE)
        LAC_ARRAY.push(element.LAC)
        MCC_ARRAY.push(element.MCC)
        MNC_ARRAY.push(element.MNC)
        BST_LAT_ARRAY.push(element.BST_LAT)
        BST_LON_ARRAY.push(element.BST_LON)
        SIGNAL_STRENGTH_ARRAY.push(element.SIGNAL_STRENGTH)
        OPERATOR_NAME_ARRAY.push(element.OPERATOR_NAME)
        NETWORK_SPEED_UP_ARRAY.push(element.NETWORK_SPEED_UP)
        NETWORK_SPEED_DOWN_ARRAY.push(element.NETWORK_SPEED_DOWN)
        COUNTRY_CODE_ARRAY.push(element.COUNTRY_CODE)
        OPERATOR_CODE_ARRAY.push(element.OPERATOR_CODE)
        var value = element.SIGNAL_STRENGTH / 10
        MARKER_SIZE_ARRAY.push(value)


        MARKER_COLOUR_ARRAY.push("#00FF00");

        var text = `Operator: ${element.OPERATOR_NAME},</br>
        Country Name: ${element.COUNTRY_CODE},</br>
        Signal Strength: ${element.SIGNAL_STRENGTH},</br>
        LONGITUDE: ${element.LONGITUDE},</br>
        LATITUDE: ${element.LATITUDE},</br>
        NETWORK_SPEED_UP: ${element.NETWORK_SPEED_UP},</br>
        NETWORK_SPEED_DOWN: ${element.NETWORK_SPEED_DOWN}`


        DESCRIPTION_TEXT_ARRAY.push(text)
    }
    // console.log(LONGITUDE_ARRAY);
    // console.log(LATITUDE_ARRAY);
    // console.log(LAC_ARRAY);
    // console.log(MCC_ARRAY);
    // console.log(MNC_ARRAY);
    // console.log(BST_LAT_ARRAY);
    // console.log(BST_LON_ARRAY);
    // console.log(SIGNAL_STRENGTH_ARRAY);
    // console.log(OPERATOR_NAME_ARRAY);
    // console.log(NETWORK_SPEED_UP_ARRAY);
    // console.log(NETWORK_SPEED_DOWN_ARRAY);
    // console.log(COUNTRY_CODE_ARRAY);
    // console.log(OPERATOR_CODE_ARRAY);
    // console.log(MARKER_COLOUR_ARRAY);
    // console.log(DESCRIPTION_TEXT_ARRAY);

    plot();
}





function plot() {

    var data = [{
        type: 'scattermapbox',
        mode: 'markers',
        text: DESCRIPTION_TEXT_ARRAY,
        lon: LATITUDE_ARRAY,
        lat: LONGITUDE_ARRAY,
        marker: {
            color: MARKER_COLOUR_ARRAY,
            // colorscale: scl, Determines Variable color scale
            cmin: 0,
            cmax: 1.4,
            reversescale: true,
            opacity: 0.5,
            size: MARKER_SIZE_ARRAY,//Determines variable size

        },
        name: 'Signal Strength Chart'
    }];

    var config = { responsive: true }

    var layout = {
        mapbox: {
            // style: 'streets'
            style: 'open-street-map'
            // style: 'white-bg'
            // style: 'carto-positron'
            // style: 'carto-darkmatter'
            // style: 'stamen-terrain'
            // style: 'stamen-toner'
            // style: 'stamen-watercolor '
        },
        title: {
            text: "Detect Poor Signal Strength-SIH Demo",
            side: "top left"
        },
        geo: {
            scope: "asia"
        }
    };

    Plotly.newPlot('myDiv', data, layout, config);

    // handler()
}
