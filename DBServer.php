<?php

error_reporting(0);

$db_name = "parkme";
$mysql_user = "parkme";
$mysql_pass = "parkme";
$server_name = "assandi-baco.top";

$con = mysqli_connect($server_name, $mysql_user, $mysql_pass, $db_name);

if(!$con){
	echo '{"message":"Impossible de se connecter à la base de données."}';
}

?>