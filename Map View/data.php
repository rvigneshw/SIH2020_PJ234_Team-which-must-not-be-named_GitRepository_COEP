<?php
$json = file_get_contents('http://open.mapquestapi.com/geocoding/v1/address?key=AQWuFdYM1V8cEik4FIbxOPF4IJIFmJfu&location='.$_GET['search_query']);
echo $json;
?>