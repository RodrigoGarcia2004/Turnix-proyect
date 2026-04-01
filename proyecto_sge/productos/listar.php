<?php

include("../includes/conexion.php");
include("../includes/header.php");
include("../includes/auth.php");

$sql = "SELECT * FROM productos";
$result = mysqli_query($conn,$sql);

?>

<h2>Lista de productos</h2>

<a href="crear.php" class="btn btn-success mb-3">Nuevo producto</a>

<table class="table table-bordered">

<tr>
<th>ID</th>
<th>Nombre</th>
<th>Precio</th>
<th>Categoría</th>
<th>Stock</th>
<th>Estado</th>
<th>Acciones</th>
</tr>

<?php while($row = mysqli_fetch_assoc($result)): ?>

<tr>

<td><?php echo $row["id"]; ?></td>
<td><?php echo $row["nombre"]; ?></td>
<td><?php echo $row["precio"]; ?></td>
<td><?php echo $row["categoria"]; ?></td>
<td><?php echo $row["stock"]; ?></td>
<td><?php echo $row["estado"]; ?></td>

<td>

<a href="editar.php?id=<?php echo $row["id"]; ?>" class="btn btn-warning btn-sm">Editar</a>

<a href="eliminar.php?id=<?php echo $row["id"]; ?>" class="btn btn-danger btn-sm"
onclick="return confirm('¿Seguro que deseas eliminar el producto?')">
Eliminar
</a>

</td>

</tr>

<?php endwhile; ?>

</table>

<?php include("../includes/footer.php"); ?> 