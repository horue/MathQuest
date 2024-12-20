import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.Normalizer;

public class Jogo extends JFrame {
    private Jogador jogador;
    private TelaJogo telaJogo;
    private Conta contaAtual;
    private Timer jogoTimer;
    private int tempoRestante;
    private Font customFont;
    private Font numberFont;
    private Clip acertoClip;
    private Clip erroClip;
    private Clip clip;
    private Clip fimJogoClip;
    private boolean musicaTocando = false;
    private Clip jogoClip;
    private static final String DATABASE_URL = "https://mathquest-21f30-default-rtdb.firebaseio.com/";






    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Jogo().iniciar());
    }

    public void pararJogo() {
        tempoRestante = 0;
        telaJogo.atualizarTempo(tempoRestante);
        terminarJogo();
    }

    public List<String[]> obterPlacarComp() {
        List<String[]> placar = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("placar.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                placar.add(linha.split(" - "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ordena o placar em ordem decrescente
        Collections.sort(placar, (p1, p2) -> Integer.compare(Integer.parseInt(p2[1]), Integer.parseInt(p1[1])));
        return placar.stream().collect(Collectors.toList());
    }

    public void exibirPlacarCompleto() {
        List<String[]> placarCompleto = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("placar.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                placarCompleto.add(linha.split(" - "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] columnNames = {"Jogador", "Pontuação"};
        JTable tabelaCompleta = new JTable(placarCompleto.toArray(new String[0][0]), columnNames);
        tabelaCompleta.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tabelaCompleta);
        
        JOptionPane.showMessageDialog(this, scrollPane, "Placar Completo", JOptionPane.INFORMATION_MESSAGE);
    }

    public List<String[]> obterPlacarCompFB() {
        List<String[]> placar = new ArrayList<>();
        
        try {
            String path = "global.json";
            URI uri = new URI(DATABASE_URL + path);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Ler a resposta JSON do Firebase
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                     BufferedReader bufferedReader = new BufferedReader(reader)) {
                    
                    StringBuilder jsonResponse = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        jsonResponse.append(line);
                    }

                    String jsonData = jsonResponse.toString();
                    if (jsonData.equals("null")) {
                        return placar; 
                    }


                    jsonData = jsonData.substring(1, jsonData.length() - 1); 
                    String[] entries = jsonData.split("},");  // Separa os objetos dos jogadores
                    
                    for (String entry : entries) {
                        // Garantir que as chaves e valores estão formatados corretamente
                        entry = entry.replace("{", "").replace("}", "").trim();

                        // Dividir cada entrada por vírgula
                        String[] parts = entry.split(",");

                        String nome = "";
                        int pontuacao = 0;
                        
                        // Extrair corretamente os valores de "nome" e "pontuacao"
                        for (String part : parts) {
                            if (part.contains("\"nome\"")) {
                                nome = part.split(":")[2].replace("\"", "").trim(); 
                            }
                            if (part.contains("\"pontuacao\"")) {
                                pontuacao = Integer.parseInt(part.split(":")[1].trim());
                            }
                        }

                        // Adicionar o jogador ao placar
                        placar.add(new String[]{nome, String.valueOf(pontuacao)});
                    }
                }
            } else {
                System.out.println("Erro ao obter placar: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //faz a ordem decrescente
        placar.sort((p1, p2) -> Integer.compare(Integer.parseInt(p2[1]), Integer.parseInt(p1[1])));
        return placar.stream().collect(Collectors.toList());
    }


    private void carregarSons() {
        try {
            AudioInputStream acertoAudio = AudioSystem.getAudioInputStream(new File("Assets\\Powerup.wav"));
            acertoClip = AudioSystem.getClip();
            acertoClip.open(acertoAudio);
    
            AudioInputStream erroAudio = AudioSystem.getAudioInputStream(new File("Assets\\Hurt.wav"));
            erroClip = AudioSystem.getClip();
            erroClip.open(erroAudio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tocarSom(Clip clip) {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        }
    }
    
    public void tocarSomAcerto() {
        tocarSom(acertoClip);
    }
    
    public void tocarSomErro() {
        tocarSom(erroClip);
    }

    public void tocarMusicaFundo() {
        try {
            if (musicaTocando) {
                // Para a música se já estiver tocando
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.close(); // Fecha o clip antes de reabrir
            }
    
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Assets\\m1.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Repetir a música continuamente
            clip.start();
            musicaTocando = true; // Define que a música está tocando
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pararMusicaFundo() {
        if (musicaTocando) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
            musicaTocando = false; // Define que a música não está mais tocando
        }
    }


    private void carregarMusicaFimJogo() {
        try {
            AudioInputStream fimJogoAudio = AudioSystem.getAudioInputStream(new File("Assets\\GhostAlley.wav"));
            fimJogoClip = AudioSystem.getClip();
            fimJogoClip.open(fimJogoAudio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tocarSomFimJogo() {
        if (fimJogoClip != null) {
            fimJogoClip.setFramePosition(0); // Volta para o início
            fimJogoClip.start(); // Inicia a reprodução
        }
    }


    public void pararSomFimJogo() {
        if (fimJogoClip != null) {
            fimJogoClip.setFramePosition(0); // Volta para o início
            fimJogoClip.stop(); // Inicia a reprodução
        }
    }


    private void carregarMusicaJogo() {
        try {
            AudioInputStream jogoAudio = AudioSystem.getAudioInputStream(new File("Assets\\Ivegotyou.wav"));
            jogoClip = AudioSystem.getClip();
            jogoClip.open(jogoAudio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tocarMusicaJogo() {
        carregarMusicaJogo(); // Parar qualquer outra música antes
        if (jogoClip != null) {
            jogoClip.setFramePosition(0);
            jogoClip.loop(Clip.LOOP_CONTINUOUSLY);
            jogoClip.start();
        }
    }

    public void pararMusicaJogo() {
        if (jogoClip != null) {
            jogoClip.setFramePosition(0); // Volta para o início
            jogoClip.stop(); // Inicia a reprodução
        }
    }

    


    public void iniciar() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font\\SuperFunky-lgmWw.ttf")).deriveFont(14f);
            numberFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font\\MouldyCheeseRegular-WyMWG.ttf")).deriveFont(14f); // Fonte para números
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        pararSomFimJogo();
        carregarSons(); // Carregar os sons
        jogador = new Jogador();
        telaJogo = new TelaJogo(this);
        setContentPane(telaJogo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        tocarMusicaFundo();
        carregarMusicaFimJogo();
    }


    public void iniciarJogo() {
        gerarNovaConta(); // Gera a primeira conta imediatamente
        telaJogo.atualizarTela(); // Atualiza a tela para mostrar a conta
        tempoRestante = 35;
        telaJogo.atualizarTempo(tempoRestante);
    
        jogoTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempoRestante--;
                telaJogo.atualizarTempo(tempoRestante);
                if (tempoRestante <= 0) {
                    terminarJogo();
                }
            }
        });
        jogoTimer.start();
    }

    public void gerarNovaConta() {
        contaAtual = new Conta();
        contaAtual.gerarConta();
    }

    public void terminarJogo() {
        jogoTimer.stop();
        pararMusicaFundo();
        pararMusicaJogo();
        tocarSomFimJogo();
        telaJogo.exibirTelaFimDeJogo();
    }

    public void registrarPontuacao() {
        if (jogador.getNome().equalsIgnoreCase("nullo")) {
            return; // Não registra a pontuação se o nome for "nullo"
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("placar.txt", true))) {
            writer.write(jogador.getNome() + " - " + jogador.getPontuacao());
            writer.newLine();
            try {
                String nomeLimpo = Normalizer.normalize(jogador.getNome(), Normalizer.Form.NFD);
                nomeLimpo = nomeLimpo.replaceAll("[^\\p{ASCII}]", "");
                enviarParaFirebase(nomeLimpo, jogador.getPontuacao());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                System.out.println("Erro de URI: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erro de IO: " + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }

   private void enviarParaFirebase(String nome, int pontuacao) throws IOException, URISyntaxException {
        String path = "global.json"; //nome da parte que vai ser usada na firebase
        URI uri = new URI(DATABASE_URL + path);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);

        String jsonInputString = "{\"nome\": \"" + nome + "\", \"pontuacao\": " + pontuacao + "}";



        try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            dos.writeBytes(jsonInputString);
            dos.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Pontuação registrada com sucesso!");
        } else {
            System.out.println("Erro ao registrar pontuação: " + responseCode);
        }

        connection.disconnect();
    }

    public void exibirPlacar() {
        List<String> placar = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("placar.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                placar.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ordena o placar em ordem decrescente
        Collections.sort(placar, new Comparator<String>() {
            @Override
            public int compare(String p1, String p2) {
                int score1 = Integer.parseInt(p1.split(" - ")[1]);
                int score2 = Integer.parseInt(p2.split(" - ")[1]);
                return Integer.compare(score2, score1);
            }
        });

        // Exibe o placar em um JOptionPane (AINDA VOU MUDAR)
        StringBuilder placarTexto = new StringBuilder("Placar:\n");
        for (String p : placar) {
            placarTexto.append(p).append("\n");
        }
        JOptionPane.showMessageDialog(this, placarTexto.toString(), "Placar", JOptionPane.INFORMATION_MESSAGE);
    }

    class Jogador {
        private String nome;
        private int pontuacao;

        public Jogador() {
            this.pontuacao = 0;
        }

        public void responder(int resposta, Conta conta) {
            if (conta.verificarResposta(resposta)) {
                incrementarPontuacao(conta);
                tempoRestante += 4; // Adiciona tempo se acertar (PODE SER ALTERADO)
                tocarSomAcerto(); // Toca som de acerto
            } else {
                tempoRestante -= 2; // Reduz o tempo se errar (PODE SER ALTERADO)
                tocarSomErro(); // Toca som de erro
            }
        }

        public void incrementarPontuacao(Conta conta) {
            if (conta.getOperador() == '+' || conta.getOperador() == '-') {
                pontuacao += 1;
            } else {
                pontuacao += 3;
            }
        }

        public int getPontuacao() {
            return pontuacao;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }
    }

    class TelaJogo extends JPanel {
        private Jogo jogo;
        private JLabel lblConta;
        private JTextField txtResposta;
        private JLabel lblPontuacao;
        private JLabel lblTempo;
        private JButton btnJogar; //MESMO SE ESTIVER DANDO ERRO NÃO TIRA PORQUE SE NÃO PARA DE FUNCIONAR
        private Color corBtn = new Color(248, 241, 196);
        private Color corTrac = new Color(122, 0, 14);

        public void exibirTabelaPlacar() {
            List<String[]> placarTop10 = jogo.obterPlacarComp(); //
            String[] columnNames = {"Colocação", "Jogador", "Pontuação"};
    
            // Criar uma lista temporária para incluir a colocação
            List<String[]> placarComColocacao = new ArrayList<>();
            for (int i = 0; i < placarTop10.size(); i++) {
                String[] jogador = placarTop10.get(i);
                placarComColocacao.add(new String[]{(i + 1) + "°", jogador[0], jogador[1]}); // Adiciona a colocação
            }
    
            JTable tabelaTop10 = new JTable(placarComColocacao.toArray(new String[0][0]), columnNames);
            tabelaTop10.setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(tabelaTop10);
            
            JOptionPane.showOptionDialog(
                this, 
                scrollPane, 
                "Classificação", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                null, 
                null
            );
        }


        public void exibirTabelaPlacarFB() {
            List<String[]> placarCompleto = obterPlacarCompFB();

            String[] columnNames = {"Colocação", "Jogador", "Pontuação"};
    
            List<String[]> placarComColocacao = new ArrayList<>();
            for (int i = 0; i < placarCompleto.size(); i++) {
                String[] jogador = placarCompleto.get(i);
                placarComColocacao.add(new String[]{(i + 1) + "°", jogador[0], jogador[1]});
            }
    
            JTable tabelaCompleta = new JTable(placarComColocacao.toArray(new String[0][0]), columnNames);
            tabelaCompleta.setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(tabelaCompleta);
    
            JOptionPane.showOptionDialog(
                    null, 
                    scrollPane,
                    "Classificação Completa",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null
            );

        }

        public void exibirPlacarComAbas() {
            JTabbedPane tabbedPane = new JTabbedPane();
    
            List<String[]> placarLocal = obterPlacarComp(); // Do arquivo .txt
            List<String[]> placarGlobal = obterPlacarCompFB(); // Da fd
    
            String[] columnNames = {"Colocação", "Jogador", "Pontuação"};
    
            // Criando a tabela para o placar local
            List<String[]> placarLocalComColocacao = new ArrayList<>();
            for (int i = 0; i < placarLocal.size(); i++) {
                String[] jogador = placarLocal.get(i);
                placarLocalComColocacao.add(new String[]{(i + 1) + "°", jogador[0].toUpperCase(), jogador[1]});
            }
            JTable tabelaLocal = new JTable(placarLocalComColocacao.toArray(new String[0][0]), columnNames);
            tabelaLocal.setFillsViewportHeight(true);
            JScrollPane scrollLocal = new JScrollPane(tabelaLocal);
    
            // Lista FB
            List<String[]> placarGlobalComColocacao = new ArrayList<>();
            for (int i = 0; i < placarGlobal.size(); i++) {
                String[] jogador = placarGlobal.get(i);
                placarGlobalComColocacao.add(new String[]{(i + 1) + "°", jogador[0].toUpperCase(), jogador[1]});
            }
            JTable tabelaGlobal = new JTable(placarGlobalComColocacao.toArray(new String[0][0]), columnNames);
            tabelaGlobal.setFillsViewportHeight(true);
            JScrollPane scrollGlobal = new JScrollPane(tabelaGlobal);
    
            tabbedPane.addTab("Placar Local", scrollLocal);
            tabbedPane.addTab("Placar Global", scrollGlobal);
    
            JOptionPane.showOptionDialog(
                    null,
                    tabbedPane,
                    "Classificação",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null
            );
        }
    
        public TelaJogo(Jogo jogo) {
            setTitle("Math Quest");
            ImageIcon img = new ImageIcon("Assets\\Images\\iconbig.png");
            setIconImage(img.getImage());
            this.jogo = jogo;
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os componentes (NÃO ALTERAR)


            ImageIcon originalIcon = new ImageIcon("Assets\\Images\\mathquestlogo2.png");

            // Redimensiona a imagem (NÃO MUDAR)
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(originalIcon.getIconWidth() / 3, originalIcon.getIconHeight() / 3, Image.SCALE_SMOOTH);
            

            JLabel logo = new JLabel(new ImageIcon(scaledImage));
            
            GridBagConstraints gbcLogo = new GridBagConstraints();
            gbcLogo.insets = new Insets(10, 10, 10, 10); 
            gbcLogo.gridx = 0;
            gbcLogo.gridy = 0; // Ajuste conforme necessário
            gbcLogo.gridwidth = 2;
            gbcLogo.anchor = GridBagConstraints.CENTER;
            
            add(logo, gbcLogo);
        
            JLabel finalLogo = logo;

            lblConta = new JLabel();
            lblConta.setFont(jogo.numberFont.deriveFont(40f));
            lblConta.setForeground(Color.black);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            add(lblConta, gbc);

            txtResposta = new JTextField(10);
            txtResposta.setVisible(false); // Inicialmente oculto
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(txtResposta, gbc);

            lblPontuacao = new JLabel();
            lblPontuacao.setFont(jogo.customFont.deriveFont(14f));
            lblPontuacao.setForeground(Color.black);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.EAST;
            add(lblPontuacao, gbc);

            lblTempo = new JLabel();
            lblTempo.setFont(jogo.customFont.deriveFont(14f));
            lblTempo.setForeground(Color.black);
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.WEST;
            add(lblTempo, gbc);

            // Botão para iniciar o jogo
            btnJogar = new JButton("Jogar");
            JButton btnJogar = new JButton("Jogar");
            btnJogar.setFont(jogo.customFont);
            btnJogar.setFocusPainted(false);
            btnJogar.setBackground(corBtn);
            btnJogar.setBorder(new LineBorder(corTrac, 2));
            btnJogar.setForeground(Color.black);
            btnJogar.setPreferredSize(new Dimension(100, 35));
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(btnJogar, gbc);

            // Ação do botão para iniciar o jogo
            btnJogar.addActionListener(new ActionListener() {
                private boolean jogando = false; // Variável para rastrear o estado do jogo
            
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!jogando) {
                        jogo.iniciarJogo(); // Inicia o jogo
                        btnJogar.setText("Parar"); // Muda o texto do botão para "Parar"
                        jogando = true;
                        pararMusicaFundo();
                        tocarMusicaJogo();
                        txtResposta.setVisible(true); // Mostrar a barra de input
                        lblPontuacao.setVisible(true); // Mostrar a pontuação
                        lblTempo.setVisible(true); // Mostrar o tempo
                        finalLogo.setVisible(false); // Ocultar a logo
                    } else {
                        jogo.pararJogo(); // Para o jogo
                        btnJogar.setText("Jogar"); // Muda o texto do botão de volta para "Jogar"
                        jogando = false;
                        txtResposta.setVisible(false); // Ocultar a barra de input
                        lblPontuacao.setVisible(false); // Ocultar a pontuação
                        lblTempo.setVisible(false); // Ocultar o tempo
                        finalLogo.setVisible(true); // Mostrar a logo
                    }
                    btnJogar.setEnabled(true); // Reativa o botão
                }
            });

            // Botão para mostrar o placar
            JButton btnPlacar = new JButton("Ver Placar");
            btnPlacar.setFont(jogo.customFont);
            btnPlacar.setFocusPainted(false);
            btnPlacar.setFocusPainted(false);
            btnPlacar.setBackground(corBtn);
            btnPlacar.setBorder(new LineBorder(corTrac, 2));
            btnPlacar.setForeground(Color.black);
            btnPlacar.setPreferredSize(new Dimension(100, 35));
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(btnPlacar, gbc);

            // Ação do botão para exibir o placar
            btnPlacar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    exibirPlacarComAbas(); // Mude aqui para exibirTabelaPlacar
                }
            });


            // Muda a fonte (NÃO ALTERAR)
            setFontForAllComponents(this, jogo.customFont);

            txtResposta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int resposta = txtResposta.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtResposta.getText().trim());
                    jogo.jogador.responder(resposta, jogo.contaAtual);
                    jogo.gerarNovaConta();
                    atualizarTela();
                    txtResposta.setText(""); // Limpa a caixa de resposta após enviar
                }
            });
        }

        private void setFontForAllComponents(Container container, Font font) {
            for (Component component : container.getComponents()) {
                component.setFont(font); // Use a fonte customizada
                if (component instanceof Container) {
                    setFontForAllComponents((Container) component, font);
                }
            }
        }

        public void atualizarTela() {
            lblConta.setText(jogo.contaAtual.toString());
            lblConta.setFont(numberFont.deriveFont(30f));
            lblPontuacao.setText("Pontuação: " + jogo.jogador.getPontuacao());
            lblPontuacao.setPreferredSize(new Dimension(300,20));

        }

        public void atualizarTempo(int tempo) {
            // Define a cor do número com base no valor de 'tempo'
            String color = (tempo <= 10) ? "red" : "black"; 
        
            // Atualiza o texto do JLabel usando HTML
            lblTempo.setText("<html>Tempo Restante: <span style='color:" + color + "'>" + tempo + "s</html>");
        
            // Mantém a fonte atualizada
            lblTempo.setFont(jogo.customFont.deriveFont(Font.PLAIN, 14f));
            lblTempo.setPreferredSize(new Dimension(300,20));
        }

        public void exibirTelaFimDeJogo() {
            String nome = JOptionPane.showInputDialog(this, "Fim de Jogo! Digite seu nome:", "Fim de Jogo", 0);
            
            // Verifica se o nome é nulo ou vazio
            while (nome == null || nome.trim().isEmpty() || !nome.matches("[a-zA-ZÀ-ÖØ-ÿ\\s]+")) {
                nome = JOptionPane.showInputDialog(this, "Seu nome não pode ser vazio e nem conter números! Por favor, digite seu nome novamente:");
            }
            

            jogo.jogador.setNome(nome);
            System.out.println("Jogador: " + jogo.jogador.getNome() + " - Pontuação: " + jogo.jogador.getPontuacao());

            if (!nome.equalsIgnoreCase("nullo")) {
                jogo.registrarPontuacao(); // Salva a pontuação do jogador no txt se o nome for difernte de nullo (Só para teste e não ficar aumentando o txt)
            }        
                
            int option = JOptionPane.showConfirmDialog(this, "Deseja jogar novamente?", "Fim de Jogo", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                jogo.iniciar();
            } else {
                System.exit(0);
            }
        }
    }

    class Conta {
        private int numero1;
        private int numero2;
        private char operador;
        private int resultado;

        public Conta() {
        }

        public void gerarConta() {
            Random random = new Random();
            numero1 = random.nextInt(9) + 1; 
            numero2 = random.nextInt(9) + 1; // Gera núemro entre 1 e 19
        
            char[] operadores = {'+', '-', 'x', '÷'};
            operador = operadores[random.nextInt(4)];
        
            // Ajustes para subtração e divisão
            if (operador == '-') {
                if (numero1 < numero2) { // Garantir que o primeiro número seja maior
                    int temp = numero1;
                    numero1 = numero2;
                    numero2 = temp;
                }
            } else if (operador == '÷') {
                // Garantir que a divisão seja exata
                numero1 = random.nextInt(9) + 1; // Gera um número entre 1 e 9
                numero2 = random.nextInt(9) + 1; // Gera um número entre 1 e 9
                numero1 *= numero2; // Faz o número1 ser um múltiplo de número2
            }
        
            calcularResultado();
        }

        public void calcularResultado() {
            switch (operador) {
                case '+':
                    resultado = numero1 + numero2;
                    break;
                case '-':
                    resultado = numero1 - numero2;
                    break;
                case 'x':
                    resultado = numero1 * numero2;
                    break;
                case '÷':
                    resultado = numero1 / numero2;
                    break;
            }
        }

        public boolean verificarResposta(int resposta) {
            return resposta == resultado;
        }

        public char getOperador() {
            return operador;
        }

        @Override
        public String toString() {
            return numero1 + " " + operador + " " + numero2 + " = ?";
        }
    }
}
