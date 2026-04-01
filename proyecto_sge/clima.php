<?php
include("includes/header.php");
include("includes/auth.php");

$ciudad = "Madrid";

// Tu API KEY
$apiKey = "2af877bb5c05e1dd96e4ec69d4efe44f";

$url = "https://api.openweathermap.org/data/2.5/weather?q=$ciudad&appid=$apiKey&units=metric&lang=es";

$response = file_get_contents($url);
$data = json_decode($response, true);
?>

<div class="container mt-4">

<h2>Clima actual</h2>

<?php if(isset($data["main"])): ?>

<div class="card" style="width: 20rem;">
<div class="card-body">

<h5 class="card-title"><?php echo $data["name"]; ?></h5>

<p class="card-text">

🌡 Temperatura: <?php echo $data["main"]["temp"]; ?> °C
<br>

🌡 Sensación térmica: <?php echo $data["main"]["feels_like"]; ?> °C
<br>

☁ Clima: <?php echo $data["weather"][0]["description"]; ?>
<br>

💧 Humedad: <?php echo $data["main"]["humidity"]; ?> %

</p>

<a href="dashboard.php" class="btn btn-secondary">Volver</a>

</div>
</div>

<?php else: ?>

<div class="alert alert-danger">
No se pudo obtener el clima. Comprueba la API o la ciudad.
</div>

<?php endif; ?>

</div>

<?php include("includes/footer.php"); ?>
