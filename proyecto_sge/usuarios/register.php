<?php

include("../includes/conexion.php");
include("../includes/header.php");

?>

<h2>Registro de usuario</h2>

<form method="POST" id="formRegistro">

<div class="mb-3">
<label>Usuario</label>
<input type="text" name="username" id="username" class="form-control">
</div>

<div class="mb-3">
<label>Email</label>
<input type="email" name="email" id="email" class="form-control">
</div>

<div class="mb-3">
<label>Contraseña</label>
<input type="password" name="password" id="password" class="form-control">
</div>

<div class="mb-3">
<label>Teléfono</label>
<input type="text" name="telefono" id="telefono" class="form-control">
</div>

<div class="mb-3">
<label>Género</label><br>

<input type="radio" name="genero" value="hombre"> Hombre
<input type="radio" name="genero" value="mujer"> Mujer
<input type="radio" name="genero" value="otro"> Otro

</div>

<div class="mb-3">

<label>Rol</label>

<select name="rol" id="rol" class="form-control">

<option value="">Seleccionar</option>
<option value="usuario">Usuario</option>
<option value="admin">Admin</option>

</select>

</div>

<button class="btn btn-success">Registrarse</button>

</form>

<?php

if($_SERVER["REQUEST_METHOD"]=="POST"){

$username=$_POST["username"];
$email=$_POST["email"];
$password=password_hash($_POST["password"],PASSWORD_DEFAULT);
$telefono=$_POST["telefono"];
$genero=$_POST["genero"];
$rol=$_POST["rol"];

$sql="INSERT INTO usuarios(username,email,password,telefono,genero,rol,fecha_registro)
VALUES('$username','$email','$password','$telefono','$genero','$rol',NOW())";

if(mysqli_query($conn,$sql)){

echo "<div class='alert alert-success mt-3'>Usuario registrado</div>";

}else{

echo "<div class='alert alert-danger'>Error</div>";

}

}

?>

<script>

document.getElementById("formRegistro").addEventListener("submit",function(e){

let username=document.getElementById("username").value.trim();
let email=document.getElementById("email").value.trim();
let password=document.getElementById("password").value.trim();
let telefono=document.getElementById("telefono").value.trim();
let rol=document.getElementById("rol").value;
let genero=document.querySelector('input[name="genero"]:checked');

if(username==""||email==""||password==""||telefono==""||rol==""||!genero){

alert("Todos los campos son obligatorios");
e.preventDefault();

}

});

</script>

<?php include("../includes/footer.php"); ?>