<?php
$pdo = new PDO('mysql:host=localhost;dbname=Inventario;charset=utf8mb4', 'root', '');

function h($t) { return htmlspecialchars($t); }