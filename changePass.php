<?php
error_reporting(0);
require "init.php";

$name = $_POST["name"];
$password = $_POST["password"];

$sql = "UPDATE `user_info` SET `password` =  '".$password."' WHERE `name` = '".$name."';";
if(!mysqli_query($con, $sql)){
	echo 'Erreur modification de votre mot de passe';

}
?>
