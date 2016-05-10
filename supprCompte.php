<?php
error_reporting(0);
require "init.php";

$name = $_POST["name"];

$sql = "DELETE FROM  `user_info` WHERE `name` = '".$name."';";
if(!mysqli_query($con, $sql)){
	echo 'Erreur : Impossible de supprimer votre compte.';

}
?>
