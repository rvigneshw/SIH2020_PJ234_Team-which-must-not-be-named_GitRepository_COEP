var LONGITUDE_ARRAY = null;
var LATITUDE_ARRAY = null;
var LAC_ARRAY = null;
var MCC_ARRAY = null;
var MNC_ARRAY = null;
var BST_LAT_ARRAY = null;
var BST_LON_ARRAY = null;
var CALL_DROP_ARRAY = null;
var OPERATOR_NAME_ARRAY = null;
var RATING_ARRAY = null;
var NETWORK_SPEED_DOWN_ARRAY = null;
var COUNTRY_CODE_ARRAY = null;
var OPERATOR_CODE_ARRAY = null;
var MARKER_COLOUR_ARRAY = null;
var MARKER_SHAPE_ARRAY = null;
var DESCRIPTION_TEXT_ARRAY = null;
var MARKER_SIZE_ARRAY = null;

// var OPERATOR_ARRAY
// var IN_OUT_TRAVEL_ARRAY
// var NETWORK_TYPE_ARRAY
// var RATING_ARRAY
// var CALL_ARRAY
// var LATITUDE_ARRAY
// var LONGITUDE_ARRAY
// var STATE_ARRAY


// "fields": [{ "id": "a", "label": "Operator", "type": "string" }, { "id": "b", "label": "In Out Travelling", "type": "string" }, { "id": "c", "label": "Network Type", "type": "string" }, { "id": "d", "label": "Rating", "type": "string" }, { "id": "e", "label": "Call Drop Category", "type": "string" }, { "id": "f", "label": "Latitude", "type": "string" }, { "id": "g", "label": "Longitude", "type": "string" }, { "id": "h", "label": "State Name", "type": "string" }]
$(document).ready(function () {
    initialize();
    $.ajax({
        type: "GET",
        url: "assests/data.gov.in.call-drop.json",
        dataType: "text",
        success: function (data) {
            var json_data = JSON.parse(data);
            var fields = json_data.fields;
            var cd_data = json_data.data;
            console.log(fields)
            // console.log(cd_data[0][0])
            for (let i = 0; i < cd_data.length; i++) {
                const element = cd_data[i];
                // console.log(element[3])

            }
            // console.log(data.data);
            processData(cd_data);
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
    CALL_DROP_ARRAY = []
    OPERATOR_NAME_ARRAY = []
    RATING_ARRAY = []
    NETWORK_SPEED_DOWN_ARRAY = []
    COUNTRY_CODE_ARRAY = []
    OPERATOR_CODE_ARRAY = []
    MARKER_COLOUR_ARRAY = []
    MARKER_SIZE_ARRAY = []
    DESCRIPTION_TEXT_ARRAY = []
}
function processData(json_data) {
    // var json_data = JSON.parse(data);
    // console.log(json_data);
    for (let i = 0; i < json_data.length; i++) {
        const element = json_data[i];
        // console.log(element)
        LONGITUDE_ARRAY.push(element[5])
        LATITUDE_ARRAY.push(element[6])
        // LAC_ARRAY.push("Call drop data")
        // MCC_ARRAY.push("Call drop data")
        // MNC_ARRAY.push("Call drop data")
        // BST_LAT_ARRAY.push("Call drop data")
        // BST_LON_ARRAY.push("Call drop data")
        // CALL_DROP_ARRAY.push(element[4])
        // OPERATOR_NAME_ARRAY.push(element.OPERATOR_NAME)
        // RATING_ARRAY.push(element.NETWORK_SPEED_UP)
        // NETWORK_SPEED_DOWN_ARRAY.push(element.NETWORK_SPEED_DOWN)
        // COUNTRY_CODE_ARRAY.push(element.COUNTRY_CODE)
        // OPERATOR_CODE_ARRAY.push(element.OPERATOR_CODE)
        // var value = element.SIGNAL_STRENGTH / 10
        MARKER_SIZE_ARRAY.push(5)

        if (element[3] == "1") {
            MARKER_COLOUR_ARRAY.push("#FF2000");
        } else if (element[3] == "2") {
            MARKER_COLOUR_ARRAY.push("#FFA000");
        } else if (element[3] == "3") {
            MARKER_COLOUR_ARRAY.push("#FFD000");
        } else if (element[3] == "4") {
            MARKER_COLOUR_ARRAY.push("#E0FF00");
        } else if (element[3] == "5") {
            MARKER_COLOUR_ARRAY.push("#10FF00");
        } else {
            MARKER_COLOUR_ARRAY.push("#000000");
        }

        var text = `<b>OPERATOR</b>:${element[0]},</br><b>IN_OUT_TRAVEL</b>:${element[1]},</br><b>NETWORK_TYPE</b>:${element[2]},</br><b>RATING</b>:${element[3]},</br><b>CALL</b>:${element[4]},</br><b>LATITUDE</b>:${element[5]},</br><b>LONGITUDE</b>:${element[6]},</br><b>STATE</b>:${element[7]}`

        DESCRIPTION_TEXT_ARRAY.push(text)
    }

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
            opacity: 0.8,
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
            center: {
                lon: 78,
                lat: 23
            },
            scope: "asia"
        }
    };

    Plotly.newPlot('myDiv', data, layout, config);

    // handler()
}
