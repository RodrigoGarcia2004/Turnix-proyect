<?php

if(!isset($_SESSION["usuario"])){

header("Location: /proyecto_sge/usuarios/login.php");
exit();

}

?>