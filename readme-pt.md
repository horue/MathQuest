
<img src="Assets\Images\mathquestlogo2.png" alt="logo do projeto" width="300">

[Leia em Inglês](readme.md)


## Visão Geral

**Math Quest** é um jogo interativo de matemática desenvolvido em Java usando a biblioteca Swing. O objetivo é resolver problemas matemáticos (adição, subtração, multiplicação e divisão) que aparecem na tela dentro de um limite de tempo. O jogo atualiza dinamicamente a pontuação com base nas respostas do jogador, e sons são reproduzidos para respostas corretas e incorretas. Os jogadores podem visualizar suas pontuações em um placar e jogar novamente após o término do jogo.

## Funcionalidades

1.  **Jogabilidade de Problemas Matemáticos:**
    
    -   Os jogadores devem resolver problemas matemáticos gerados aleatoriamente.
    -   Respostas corretas de adição e subtração valem 1 ponto, enquanto multiplicação e divisão valem 3 pontos.
    -   A pontuação e o tempo restante do jogador são exibidos e atualizados em tempo real.
2.  **Placar:**
    
    -   O jogo rastreia as pontuações dos jogadores e mantém um placar armazenado em um arquivo (`placar.txt`).
    -   Os jogadores podem visualizar as melhores pontuações através do botão "Ver Pontuação".
3.  **Sons e Música:**
    
    -   Música de fundo é reproduzida durante o jogo, com faixas diferentes para a tela de jogo, de fim de jogo e para o plano de fundo.
    -   Efeitos sonoros para respostas corretas, respostas incorretas e fim de jogo.
4.  **Fontes Personalizadas:**
    
    -   O jogo utiliza fontes personalizadas tanto para a interface do jogo quanto para os números.
5.  **Interface Gráfica (GUI):**
    
    -   O jogo utiliza uma interface baseada no Java Swing.
    -   O problema matemático, a pontuação e o tempo são apresentados de forma clara e intuitiva.
    -   Uma logo é exibida no início do jogo.
6.  **Entrada do Usuário:**
    
    -   O jogador insere a resposta do problema matemático em um campo de texto.
    -   O campo de entrada é automaticamente limpo após o envio.
7.  **Opções de Reiniciar e Sair:**
    
    -   Quando o jogo termina, o jogador pode escolher jogar novamente ou sair.
    -   O jogador deve inserir seu nome para salvar sua pontuação no placar.
8.  **Mecânica de Temporizador:**
    
    -   Os jogadores começam com 35 segundos. O tempo aumenta quando respostas corretas são dadas e diminui para respostas erradas.

## Instruções de Configuração

### Pré-requisitos

-   JDK (Java Development Kit) 8 ou superior.
-   Um editor de texto ou um Ambiente de Desenvolvimento Integrado (IDE), como IntelliJ IDEA, Eclipse ou NetBeans.
-   Bibliotecas e assets necessários: as fontes personalizadas, efeitos sonoros e arquivos de música devem ser colocados nos diretórios apropriados.

### Passos para Rodar o Jogo

1.  **Compilar e Rodar:**
    
    -   Clone o repositório do GitHub usando `git clone https://github.com/horue/MathQuest.git`
    -   Compile os arquivos Java usando uma IDE ou linha de comando.
    -   Execute o método `main` da classe `Jogo` para iniciar o jogo.
2.  **Fontes e Assets:**
    
    -   Certifique-se de que as fontes e os arquivos de som estejam corretamente colocados em seus respectivos diretórios (`Font` para fontes e `Assets` para imagens e áudio).
    -   Música composta por [Bert Cole](https://bitbybitsound.com/).
3.  **Jogabilidade:**
    
    -   Pressione "Jogar" para iniciar o jogo.
    -   Insira o resultado do problema matemático no campo de texto e pressione "Enter" para enviar.
    -   O jogo termina quando o tempo acaba, e o jogador é solicitado a salvar sua pontuação.

### Controles

-   **Botão Jogar:** Inicia o jogo.
-   **Botão Ver Pontuação:** Exibe o placar atual.
-   **Campo de Texto:** Digite sua resposta para o problema matemático e pressione "Enter."

### Salvando Pontuações

As pontuações são salvas automaticamente no arquivo `placar.txt` após cada jogo. Os jogadores podem visualizar sua classificação com base nas pontuações.

## Personalização

1.  **Alterar Fontes:**
    
    -   As fontes personalizadas podem ser alteradas substituindo os arquivos `.ttf` na pasta `Font`.
2.  **Alterar Efeitos Sonoros:**
    
    -   Os efeitos sonoros e a música de fundo estão localizados na pasta `Assets` e podem ser substituídos por outros arquivos `.wav`.
3.  **Modificações no Tempo:**
    
    -   Ajuste o tempo inicial ou o tempo ganho/perdido para respostas corretas/incorretas no método `responder()` da classe `Jogador`.

## Problemas Conhecidos

-   O jogo atualmente só suporta modo single-player.
-   O cursor não seleciona automaticamente o campo de entrada da resposta.
-   Certifique-se de que os arquivos de música e som estejam no formato `.wav` para compatibilidade com o `AudioSystem`.

## Melhorias Futuras

-   Adicionar problemas matemáticos mais complexos (potências, raízes quadradas, etc.).
-   Introduzir múltiplos níveis de dificuldade.
-   Adicionar funcionalidade multiplayer ou modo de desafio.
-   Adicionar um modo história.

## Licença

Este projeto é open-source e gratuito para uso sob a licença MIT.