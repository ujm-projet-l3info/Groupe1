<?php
error_reporting(0);
require "init.php";

$name = $_POST["name"];
$password = $_POST["password"];
$email = $_POST["email"];

$sql = "INSERT INTO `user_info` (`id`,`name`, `password`, `email`) VALUES (NULL, '".$name."', '".$password."', '".$email."');";
if(!mysqli_query($con, $sql)){
	echo 'Erreur inscription : Les identifiants sont pris.';

}
?>
