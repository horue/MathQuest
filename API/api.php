<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, GET");
header("Access-Control-Allow-Headers: Content-Type");



$file = '../placar_g.txt'; // Caminho para o placar global

if (file_exists($file)) {
    $placar = file($file, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
} else {
    $placar = [];
}

// Se o método for POST, adiciona nova pontuação
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['nome']) && isset($_POST['pontuacao'])) {
        $nome = trim($_POST['nome']);
        $pontuacao = intval($_POST['pontuacao']);

        // Valida a entrada
        if (!empty($nome) && $pontuacao >= 0) {
            $placar[] = "$nome - $pontuacao";
            file_put_contents($file, implode("\n", $placar));
            echo "Pontuação adicionada com sucesso!";
            exit();
        } else {
            http_response_code(400); 
            echo "Nome ou pontuação inválidos.";
            exit();
        }
    } else {
        http_response_code(400);
        echo "Parâmetros nome e pontuação são obrigatórios.";
        exit();
    }
}

// Se não for POST, exibe o placar
header('Content-Type: text/plain');
foreach ($placar as $entry) {
    echo $entry . "\n"; 
}
?>
