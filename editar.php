<?php
require 'conexion.php';

if(isset($_POST['actualizar'])) {
    $sql="UPDATE productos
         SET articulo=?,descripcion=?,precio_costo=?,precio_venta=?,itbis=?
         WHERE id=?";

$pdo->prepare($sql)->execute([
    $_POST['articulo'],
    $_POST['descripcion'],
    $_POST['precio_costo'],
    $_POST['precio_venta'],
    $_POST['itbis'],
    $_POST['id'],
    ]);

    header('Location: index.php?s=modificar');
    exit;
}


$st = $pdo->prepare("SELECT * FROM productos WHERE id = ?");
$st->execute([$_GET['id'] ?? 0]);
$p = $st->fetch(PDO::FETCH_ASSOC);

if (!$p) {
    header('Location: index.php?s=modificar');
    exit;
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Editar producto</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>

<nav>
  <a href="index.php?s=inicio">Inicio</a>
  <a href="index.php?s=registrar">Registrar Inventario</a>
  <a href="index.php?s=ver">Ver Inventario</a>
  <a href="index.php?s=modificar" class="activo">Modificar Inventario</a>
</nav>

<main>
  <h1>Editar Producto</h1>
  <form method="post">
    <input type="hidden" name="id" value="<?= $p['id'] ?>">

    <label>Artículo</label>
    <input type="text" name="articulo" value="<?= h($p['articulo']) ?>" required>

    <label>Descripción</label>
    <textarea name="descripcion" required><?= h($p['descripcion']) ?></textarea>

    <label>Precio costo</label>
    <input type="number" step="0.01" min="0" name="precio_costo" value="<?= $p['precio_costo'] ?>" required>

    <label>Precio de venta</label>
    <input type="number" step="0.01" min="0" name="precio_venta" value="<?= $p['precio_venta'] ?>" required>

    <label>ITBIS</label>
    <input type="number" step="0.01" min="0" name="itbis" value="<?= $p['itbis'] ?>" required>

    <button type="submit" name="actualizar">Guardar cambios</button>
    <a class="cancelar" href="index.php?s=modificar">Cancelar</a>
  </form>
</main>

<footer>&copy; <?= date('Y') ?> val</footer>

</body>
</html>
