<?php

include("../includes/conexion.php");
include("../includes/header.php");
include("../includes/auth.php");

$id=$_GET["id"];

$sql="SELECT * FROM productos WHERE id=$id";
$result=mysqli_query($conn,$sql);
$producto=mysqli_fetch_assoc($result);

?>

<h2>Editar producto</h2>

<form method="POST">

<div class="mb-3">

<label>Nombre</label>

<input type="text" name="nombre" class="form-control"
value="<?php echo $producto["nombre"]; ?>">

</div>

<div class="mb-3">

<label>Descripción</label>

<textarea name="descripcion" class="form-control">

<?php echo $producto["descripcion"]; ?>

</textarea>

</div>

<div class="mb-3">

<label>Precio</label>

<input type="number" step="0.01" name="precio"
value="<?php echo $producto["precio"]; ?>" class="form-control">

</div>

<div class="mb-3">

<label>Categoría</label>

<select name="categoria" class="form-control">

<option value="ropa">Ropa</option>
<option value="juguetes">Juguetes</option>
<option value="deporte">Deporte</option>

</select>

</div>

<div class="mb-3">

<label>Stock</label>

<input type="number" name="stock"
value="<?php echo $producto["stock"]; ?>" class="form-control">

</div>

<div class="mb-3">

<label>Estado</label><br>

<input type="radio" name="estado" value="disponible"> Disponible
<input type="radio" name="estado" value="agotado"> Agotado

</div>

<button class="btn btn-warning">Actualizar</button>

</form>

<?php

if($_SERVER["REQUEST_METHOD"]=="POST"){

$nombre=$_POST["nombre"];
$descripcion=$_POST["descripcion"];
$precio=$_POST["precio"];
$categoria=$_POST["categoria"];
$stock=$_POST["stock"];
$estado=$_POST["estado"];

$sql="UPDATE productos SET

nombre='$nombre',
descripcion='$descripcion',
precio='$precio',
categoria='$categoria',
stock='$stock',
estado='$estado'

WHERE id=$id";

if(mysqli_query($conn,$sql)){

echo "<div class='alert alert-success mt-3'>Producto actualizado</div>";

}

}

?>

<?php include("../includes/footer.php"); ?>