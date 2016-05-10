<?php
error_reporting(0);
require "init.php";

$com = $_POST["com"];
$pseudo = $_POST["pseudo"];


$sql = "INSERT INTO `commentaire` (`com`, `pseudo`) VALUES ('".$com."', '".$pseudo."');";
if(!mysqli_query($con, $sql)){
	echo '{"message":"Impossible de sauvegarder votre commentaire dans la base de données."}';
}

?>
