<?php
$servername = "assandi-baco.top";
$username = "parkme";
$password = "parkme";
$dbname = "parkme";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


$lat=$_GET['lat'];
$lon=$_GET['lon'];
$id=$_GET['pseudo'];
$date=$_GET['date'];

$sql = "INSERT INTO places (lat, lon, pseudo,date,fiabilite)
VALUES ($lat, $lon, $id,$date,3)";

$conn->close();
?>
