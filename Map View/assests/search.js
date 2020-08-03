document.getElementById("src_txt_btn").addEventListener("click", function () {
    var search_query = document.getElementById("src_txt").value;
    console.log(search_query);
    $.ajax({
        type: "GET",
        url: "https://dps-sih.herokuapp.com/data.php?search_query=" + search_query,
        dataType: "json",
        success: function (data) {
            var loc = data.results[0].locations[0].latLng;
            console.log(data.results[0]);
            var results = data.results[0]
            LONGITUDE_ARRAY.push(loc.lat)
            LATITUDE_ARRAY.push(loc.lng)
            LAC_ARRAY.push("Search_result")
            MCC_ARRAY.push("Search_result")
            MNC_ARRAY.push("Search_result")
            BST_LAT_ARRAY.push("Search_result")
            BST_LON_ARRAY.push("Search_result")
            SIGNAL_STRENGTH_ARRAY.push("Search_result")
            OPERATOR_NAME_ARRAY.push("Search_result")
            NETWORK_SPEED_UP_ARRAY.push("Search_result")
            NETWORK_SPEED_DOWN_ARRAY.push("Search_result")
            COUNTRY_CODE_ARRAY.push("Search_result")
            OPERATOR_CODE_ARRAY.push("Search_result")
            // var value = element.SIGNAL_STRENGTH / 10
            MARKER_SIZE_ARRAY.push(20)

            MARKER_COLOUR_ARRAY.push("#000000");


            var text = `Search: ${search_query},</br>
            Country Name: ${results.adminArea1},</br>
            State: ${results.adminArea3},</br>
            LONGITUDE: ${loc.lng},</br>
            LATITUDE: ${loc.lat}`

            DESCRIPTION_TEXT_ARRAY.push(text)
            Plotly.redraw('myDiv');

        }
    });
});
