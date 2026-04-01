<?php

include("../includes/conexion.php");
include("../includes/header.php");
include("../includes/auth.php");

?>

<h2>Crear producto</h2>

<form method="POST" id="formProducto">

<div class="mb-3">
<label>Nombre</label>
<input type="text" name="nombre" id="nombre" class="form-control">
</div>

<div class="mb-3">
<label>Descripción</label>
<textarea name="descripcion" id="descripcion" class="form-control"></textarea>
</div>

<div class="mb-3">
<label>Precio</label>
<input type="number" step="0.01" name="precio" id="precio" class="form-control">
</div>

<div class="mb-3">
<label>Categoría</label>

<select name="categoria" id="categoria" class="form-control">
<option value="">Seleccionar</option>
<option value="ropa">Ropa</option>
<option value="juguetes">Juguetes</option>
<option value="deporte">Deporte</option>
</select>

</div>

<div class="mb-3">
<label>Stock</label>
<input type="number" name="stock" id="stock" class="form-control">
</div>

<div class="mb-3">
<label>Estado</label><br>

<input type="radio" name="estado" value="disponible"> Disponible
<input type="radio" name="estado" value="agotado"> Agotado

</div>

<button class="btn btn-success">Guardar</button>

</form>

<?php

if($_SERVER["REQUEST_METHOD"]=="POST"){

$nombre=$_POST["nombre"];
$descripcion=$_POST["descripcion"];
$precio=$_POST["precio"];
$categoria=$_POST["categoria"];
$stock=$_POST["stock"];
$estado=$_POST["estado"];

$sql="INSERT INTO productos
(nombre,descripcion,precio,categoria,stock,estado,fecha_creacion)
VALUES
('$nombre','$descripcion','$precio','$categoria','$stock','$estado',NOW())";

if(mysqli_query($conn,$sql)){

echo "<div class='alert alert-success mt-3'>Producto creado correctamente</div>";

}else{

echo "<div class='alert alert-danger'>Error al crear producto</div>";

}

}

?>

<script>

document.getElementById("formProducto").addEventListener("submit",function(e){

let nombre=document.getElementById("nombre").value.trim();
let descripcion=document.getElementById("descripcion").value.trim();
let precio=document.getElementById("precio").value.trim();
let categoria=document.getElementById("categoria").value;
let stock=document.getElementById("stock").value.trim();
let estado=document.querySelector('input[name="estado"]:checked');

if(nombre==""||descripcion==""||precio==""||categoria==""||stock==""||!estado){

alert("Todos los campos deben completarse");
e.preventDefault();

}

});

</script>

<?php include("../includes/footer.php"); ?>