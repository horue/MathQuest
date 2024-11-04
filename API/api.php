<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST");
header("Content-Type: text/plain");

$file = '../placar_g.txt'; // Caminho para o placar global

// Inicializa o array de placar
$placar = [];

// Verifica se o arquivo de placar existe e o lê
if (file_exists($file)) {
    $placar = file($file, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
}

// Se o método for POST, adiciona uma nova pontuação
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['nome']) && isset($_POST['pontuacao'])) {
        $nome = trim($_POST['nome']);
        $pontuacao = intval($_POST['pontuacao']);

        // Valida a entrada
        if (!empty($nome) && $pontuacao >= 0) {
            $placar[] = "$nome - $pontuacao"; // Adiciona nova pontuação ao array
            file_put_contents($file, implode("\n", $placar)); // Salva o novo placar no arquivo
            echo "Pontuação adicionada com sucesso!";
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

// Retorna o placar atual
if (!empty($placar)) {
    foreach ($placar as $entry) {
        echo $entry . "\n"; 
    }
} else {
    echo "Nenhum placar encontrado.\n"; // Mensagem se o placar estiver vazio
}
?>
