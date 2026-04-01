<?php
include("../includes/conexion.php");
include("../includes/header.php"); 
?>

<div class="container mt-4">
<h2>Login</h2>

<?php if(isset($_SESSION["usuario"])): ?>
    <div class="alert alert-warning text-center">
        ⚠️ ¡Ya has iniciado sesión, <?= htmlspecialchars($_SESSION["usuario"]) ?>!
    </div>
    <div class="text-center">
        <a href="../dashboard.php" class="btn btn-primary">Ir al dashboard</a>
    </div>

<?php else: ?>

    <form method="POST">
      <div class="mb-3">
        <label>Usuario</label>
        <input type="text" name="username" class="form-control" required>
      </div>

      <div class="mb-3">
        <label>Contraseña</label>
        <input type="password" name="password" class="form-control" required>
      </div>

      <button class="btn btn-primary">Entrar</button>
    </form>

    <?php
    if($_SERVER["REQUEST_METHOD"]=="POST"){

      $username = trim($_POST["username"]);
      $password = trim($_POST["password"]);

      $sql="SELECT * FROM usuarios WHERE username='$username'";
      $result=mysqli_query($conn,$sql);

      if(mysqli_num_rows($result)==1){
        $user=mysqli_fetch_assoc($result);

        if(password_verify($password,$user["password"])){
          $_SESSION["usuario"]=$user["username"];
          header("Location: ../dashboard.php");
          exit();
        }else{
          echo "<div class='alert alert-danger mt-3'>Contraseña incorrecta</div>";
        }

      }else{
        echo "<div class='alert alert-danger mt-3'>Usuario no encontrado</div>";
      }
    }
    ?>
<?php endif; ?>
</div>

<?php include("../includes/footer.php"); ?>
