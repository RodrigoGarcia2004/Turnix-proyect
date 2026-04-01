<?php
include("includes/header.php");

if(!isset($_SESSION["usuario"])){
    header("Location: usuarios/login.php");
    exit();
}
?>

<div class="container mt-4">
    <h1>Bienvenido, <?= htmlspecialchars($_SESSION["usuario"] ?? "Invitado") ?></h1>
    <p>Esta es tu página de inicio después del inicio de sesión.</p>

    <form method="GET" action="productos/api.php" class="mb-3">
        <div class="input-group">
            <input type="text" name="q" placeholder="Buscar producto..." class="form-control">
            <button type="submit" class="btn btn-success">Buscar</button>
        </div>
    </form>

    <div class="mb-3">
        <a href="productos/api.php" class="btn btn-info">Ver todos los productos</a>
        <a href="productos/listar.php" class="btn btn-primary">Gestionar Productos</a>

        <a href="clima.php" class="btn btn-warning">Ver el clima</a>
    </div>
</div>

<?php include("includes/footer.php"); ?>


