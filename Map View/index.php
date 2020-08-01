<?php
$type= $_GET['T'];
$htmlDOC='<!DOCTYPE html>
<html lang="en">

<head>
    <title>SIH Demo</title>
    <script src="assests/plotly.js"></script>
    <script src="assests/jquery.min.js"></script>
</head>

<body>
    <div id="myDiv">
        <!-- Plotly chart will be drawn inside this DIV -->
    </div>
    <script src="scattermapbox-Green-Variable-Size.js"></script>';
if($type=="VS"){
$htmlDOC=$htmlDOC.'<script src="scattermapbox-Green-Variable-Size.js"></script>';
}elseif ($type=="VC") {
    $htmlDOC=$htmlDOC.'<script src="scattermapbox-RYG-constant-size.js"></script>';
}
$htmlDOC=$htmlDOC.'
</body>
</html>';
echo $htmlDOC;
?>