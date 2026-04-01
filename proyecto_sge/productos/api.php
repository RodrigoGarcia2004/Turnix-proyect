<?php
include("../includes/header.php"); 

if(!isset($_SESSION["usuario"])){
    header("Location: ../usuarios/login.php");
    exit();
}

$productos = [
    ["id"=>1, "title"=>"Camiseta Roja", "brand"=>"Nike", "price"=>25, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>2, "title"=>"Pantalón Azul", "brand"=>"Adidas", "price"=>40, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>3, "title"=>"Zapatillas Negras", "brand"=>"Puma", "price"=>60, "category"=>"Calzado", "thumbnail"=>""],
    ["id"=>4, "title"=>"Gorra Verde", "brand"=>"Nike", "price"=>15, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>5, "title"=>"Chaqueta Gris", "brand"=>"Reebok", "price"=>80, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>6, "title"=>"Sudadera Negra", "brand"=>"Nike", "price"=>50, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>7, "title"=>"Pantalón Corto", "brand"=>"Adidas", "price"=>30, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>8, "title"=>"Zapatillas Blancas", "brand"=>"Puma", "price"=>65, "category"=>"Calzado", "thumbnail"=>""],
    ["id"=>9, "title"=>"Calcetines Deportivos", "brand"=>"Reebok", "price"=>10, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>10, "title"=>"Cinturón Negro", "brand"=>"Nike", "price"=>20, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>11, "title"=>"Camisa Azul", "brand"=>"Adidas", "price"=>35, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>12, "title"=>"Pantalón de Chándal", "brand"=>"Puma", "price"=>45, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>13, "title"=>"Botas Marrones", "brand"=>"Timberland", "price"=>90, "category"=>"Calzado", "thumbnail"=>""],
    ["id"=>14, "title"=>"Gafas de Sol", "brand"=>"Ray-Ban", "price"=>120, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>15, "title"=>"Chaqueta Roja", "brand"=>"Nike", "price"=>75, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>16, "title"=>"Sudadera Gris", "brand"=>"Adidas", "price"=>55, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>17, "title"=>"Zapatillas de Running", "brand"=>"Puma", "price"=>70, "category"=>"Calzado", "thumbnail"=>""],
    ["id"=>18, "title"=>"Gorra Negra", "brand"=>"Reebok", "price"=>18, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>19, "title"=>"Camiseta Blanca", "brand"=>"Nike", "price"=>22, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>20, "title"=>"Chándal Azul", "brand"=>"Adidas", "price"=>60, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>21, "title"=>"Sudadera Roja", "brand"=>"Nike", "price"=>48, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>22, "title"=>"Pantalón Verde", "brand"=>"Adidas", "price"=>38, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>23, "title"=>"Zapatillas Verdes", "brand"=>"Puma", "price"=>62, "category"=>"Calzado", "thumbnail"=>""],
    ["id"=>24, "title"=>"Gorra Azul", "brand"=>"Nike", "price"=>17, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>25, "title"=>"Chaqueta Negra", "brand"=>"Reebok", "price"=>85, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>26, "title"=>"Sudadera Azul", "brand"=>"Nike", "price"=>52, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>27, "title"=>"Pantalón Marrón", "brand"=>"Adidas", "price"=>42, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>28, "title"=>"Zapatillas Rojas", "brand"=>"Puma", "price"=>68, "category"=>"Calzado", "thumbnail"=>""],
    ["id"=>29, "title"=>"Calcetines Blancos", "brand"=>"Reebok", "price"=>12, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>30, "title"=>"Cinturón Marrón", "brand"=>"Nike", "price"=>22, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>31, "title"=>"Camisa Blanca", "brand"=>"Adidas", "price"=>37, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>32, "title"=>"Pantalón Negro", "brand"=>"Puma", "price"=>48, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>33, "title"=>"Botas Negras", "brand"=>"Timberland", "price"=>95, "category"=>"Calzado", "thumbnail"=>""],
    ["id"=>34, "title"=>"Gafas Deportivas", "brand"=>"Ray-Ban", "price"=>125, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>35, "title"=>"Chaqueta Azul", "brand"=>"Nike", "price"=>78, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>36, "title"=>"Sudadera Verde", "brand"=>"Adidas", "price"=>58, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>37, "title"=>"Zapatillas Negras de Running", "brand"=>"Puma", "price"=>72, "category"=>"Calzado", "thumbnail"=>""],
    ["id"=>38, "title"=>"Gorra Roja", "brand"=>"Reebok", "price"=>20, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>39, "title"=>"Camiseta Gris", "brand"=>"Nike", "price"=>23, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>40, "title"=>"Chándal Gris", "brand"=>"Adidas", "price"=>65, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>41, "title"=>"Sudadera Negra con Capucha", "brand"=>"Nike", "price"=>55, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>42, "title"=>"Pantalón de Jogging Azul", "brand"=>"Adidas", "price"=>45, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>43, "title"=>"Zapatillas Casual Negras", "brand"=>"Puma", "price"=>75, "category"=>"Calzado", "thumbnail"=>""],
    ["id"=>44, "title"=>"Gorra Amarilla", "brand"=>"Nike", "price"=>18, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>45, "title"=>"Chaqueta Deportiva Roja", "brand"=>"Reebok", "price"=>88, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>46, "title"=>"Camiseta Verde", "brand"=>"Nike", "price"=>27, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>47, "title"=>"Pantalón Azul Claro", "brand"=>"Adidas", "price"=>39, "category"=>"Ropa", "thumbnail"=>""],
    ["id"=>48, "title"=>"Zapatillas Running Blancas", "brand"=>"Puma", "price"=>69, "category"=>"Calzado", "thumbnail"=>""],
    ["id"=>49, "title"=>"Calcetines Negros", "brand"=>"Reebok", "price"=>11, "category"=>"Accesorios", "thumbnail"=>""],
    ["id"=>50, "title"=>"Cinturón Gris", "brand"=>"Nike", "price"=>24, "category"=>"Accesorios", "thumbnail"=>""],
];



$search = $_GET['q'] ?? "";
$search = trim(strtolower($search));

$productos_filtrados = [];

if($search !== ""){
    foreach($productos as $prod){
        if(str_contains(strtolower($prod['title']), $search)){
            $productos_filtrados[] = $prod;
        }
    }
} else {
    $productos_filtrados = $productos; 
}
?>

<div class="container mt-4">
    <h2>Resultados de búsqueda: <?= htmlspecialchars($search ?: "Todos los productos") ?></h2>

    <div class="row">
        <?php if(empty($productos_filtrados)): ?>
            <div class="col-12">
                <p>No se encontraron productos.</p>
            </div>
        <?php endif; ?>

        <?php foreach($productos_filtrados as $producto): ?>
            <div class="col-md-4 mb-4">
                <div class="card p-3">
                  
                    <!-- Información del producto -->
                    <h5 class="card-title"><?= htmlspecialchars($producto['title']) ?></h5>
                    <p><strong>Marca:</strong> <?= htmlspecialchars($producto['brand']) ?></p>
                    <p><strong>Precio:</strong> $<?= htmlspecialchars($producto['price']) ?></p>
                    <p><strong>Categoría:</strong> <?= htmlspecialchars($producto['category']) ?></p>
                </div>
            </div>
        <?php endforeach; ?>
    </div>
</div>

<?php include("../includes/footer.php"); ?>
