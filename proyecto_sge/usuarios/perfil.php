<?php

include("../includes/conexion.php");
include("../includes/header.php");
include("../includes/auth.php");

$username = $_SESSION["usuario"];

$sql = "SELECT * FROM usuarios WHERE username='$username'";
$result = mysqli_query($conn,$sql);
$usuario = mysqli_fetch_assoc($result);

?>

<h2>Mi perfil</h2>

<form method="POST">    

<div class="mb-3">

<label>Usuario</label>

<input type="text" name="username" class="form-control"
value="<?php echo $usuario["username"]; ?>">

</div>

<div class="mb-3">

<label>Email</label>

<input type="email" name="email" class="form-control"
value="<?php echo $usuario["email"]; ?>">

</div>

<div class="mb-3">

<label>Teléfono</label>

<input type="text" name="telefono" class="form-control"
value="<?php echo $usuario["telefono"]; ?>">

</div>

<button class="btn btn-success">Actualizar perfil</button>

</form>

<hr>

<h3>Cambiar contraseña</h3>

<form method="POST">

<div class="mb-3">

<label>Contraseña actual</label>

<input type="password" name="actual" class="form-control">

</div>

<div class="mb-3">

<label>Nueva contraseña</label>

<input type="password" name="nueva" class="form-control">

</div>

<button class="btn btn-danger">Cambiar contraseña</button>

</form>

<?php

if(isset($_POST["email"])){

$email=$_POST["email"];
$telefono=$_POST["telefono"];

$sql="UPDATE usuarios SET

email='$email',
telefono='$telefono'

WHERE username='$username'";

mysqli_query($conn,$sql);

echo "<div class='alert alert-success mt-3'>Perfil actualizado</div>";

}

if(isset($_POST["actual"])){

$actual=$_POST["actual"];
$nueva=$_POST["nueva"];

$sql="SELECT * FROM usuarios WHERE username='$username'";
$result=mysqli_query($conn,$sql);
$user=mysqli_fetch_assoc($result);

if(password_verify($actual,$user["password"])){

$nuevaHash=password_hash($nueva,PASSWORD_DEFAULT);

$sql="UPDATE usuarios SET password='$nuevaHash' WHERE username='$username'";

mysqli_query($conn,$sql);

echo "<div class='alert alert-success'>Contraseña actualizada</div>";

}else{

echo "<div class='alert alert-danger'>Contraseña actual incorrecta</div>";

}

}

?>

<?php include("../includes/footer.php"); ?>