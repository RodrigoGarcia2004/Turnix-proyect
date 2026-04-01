<?php
session_start();

$ciudad = "Madrid";
$apiKey = "2af877bb5c05e1dd96e4ec69d4efe44f";

$url = "https://api.openweathermap.org/data/2.5/weather?q=$ciudad&appid=$apiKey&units=metric&lang=es";

$response = @file_get_contents($url);
$data = $response ? json_decode($response, true) : null;

$temperatura = "--";
$icon = "🌤";

if(isset($data["main"]["temp"])){
    $temperatura = round($data["main"]["temp"]) . "°C";

    $weatherMain = strtolower($data["weather"][0]["main"]);
    switch($weatherMain){
        case "clear":
            $icon = "☀️";
            break;
        case "clouds":
            $icon = "☁️";
            break;
        case "rain":
        case "drizzle":
            $icon = "🌧️";
            break;
        case "thunderstorm":
            $icon = "⛈️";
            break;
        case "snow":
            $icon = "❄️";
            break;
        default:
            $icon = "🌤";
    }
}
?>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Proyecto SGE</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
<div class="container">

<span class="btn btn-warning me-3">
<?= $icon ?> <?= $temperatura ?>
</span>

<a class="navbar-brand" href="/proyecto_sge/index.php">SGE</a>

<div class="d-flex ms-auto">
<?php if(isset($_SESSION["usuario"])): ?>
<span class="text-white me-3">Usuario: <?= htmlspecialchars($_SESSION["usuario"]) ?></span>
<a href="/proyecto_sge/usuarios/perfil.php" class="btn btn-warning btn-sm me-2">Perfil</a>
<a href="/proyecto_sge/usuarios/logout.php" class="btn btn-danger btn-sm">Salir</a>
<?php endif; ?>
</div>

</div>
</nav>

<div class="container mt-4">
