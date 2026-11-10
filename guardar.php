<?php
require 'conexion.php';
$sql="INSERT INTO productos (articulo, descripcion, precio_costo, precio_venta, itbis)
     VALUES (?,?,?,?,?)";
$pdo->prepare($sql)->execute([
    $_POST['articulo'],
    $_POST['descripcion'],
    $_POST['precio_costo'],
    $_POST['precio_venta'],
    $_POST['itbis'],    
    ]);

    $i='itbis' / 100;
    $preciofinal= 'precio_venta' - 'itbis';
header('Location: index.php?s=ver');
?>