<?php
$file = 'placar_g.txt';

if (file_exists($file)) {
    $placar = file($file, FILE_IGNORE_NEW_LINES);
} else {
    $placar = [];
}

// Atualiza o placar global se tiver coisa nova
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $nome = $_POST['nome'];
    $pontuacao = $_POST['pontuacao'];
    // Adiciona nova pontuação ao placar
    $placar[] = "$nome - $pontuacao";
    file_put_contents($file, implode("\n", $placar));
}

// JSON
header('Content-Type: application/json');
echo json_encode($placar);
