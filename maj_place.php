<?php



function mise_a_jour(){
$con = mysqli_connect('assandi-baco.top','parkme','parkme','parkme');
$query = mysqli_query($con,"SELECT * FROM `places`");
#$query_modif = mysqli_query($con,"UPDATE `places` SET $row['fiabilite']=$row['fiabilite']-1");

/* Vérification de la connexion */
if (mysqli_connect_errno()) {
    echo 'Echec de la connexion:'.mysqli_connect_error();
    exit();
}


while ($row=mysqli_fetch_assoc($query)) {
#$user = $row['pseudo'];
$id = $row['idP'];
$fiabilite = $row['fiabilite'];

echo 'id: '.$id.' Message: '.$fiabilite;
echo "\n";
}

$query = mysqli_query($con,"SELECT * FROM `places`");

while ($row=mysqli_fetch_assoc($query)) {
#$user = $row['pseudo'];
$id = $row['idP'];
$fiabilite = $row['fiabilite'];

if($fiabilite != 1){
  mysqli_query($con,"UPDATE places SET fiabilite = $fiabilite - 1 WHERE idP = $id");
}
else{
  mysqli_query($con,"DELETE FROM places WHERE idP = $id");
}
}

$query = mysqli_query($con,"SELECT * FROM `places`");

while ($row=mysqli_fetch_assoc($query)) {
#$user = $row['pseudo'];
$id = $row['idP'];
$fiabilite = $row['fiabilite'];

echo '1id: '.$id.' Message: '.$fiabilite;
echo "\n";
}

mysqli_close($con);


}

while(1){
sleep(30); #30s
mise_a_jour();
}

?>
