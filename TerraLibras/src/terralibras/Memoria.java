package terralibras;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 *
 * @author Hozana Raquel
 */
public class Memoria extends javax.swing.JFrame {
    private Socket s;
    private ObjectInputStream sIn;
    private DataInputStream in;
    private DataOutputStream out;
    //atrasar a interface para que as mudancas acontecam enquanto espera uma
    //mensagem do servidor
    Timer atraso1, atraso2, atraso3;
    boolean atrasar;
    int linhaAtualizar1, linhaAtualizar2, colunaAtualizar1, colunaAtualizar2;
    
    private JButton botoes[][];
    private char valores[][];
    private boolean estado[][];
    //quantidade de pares de cartas acertados
    private int acertos;
    private boolean estadoPrimeiraCarta;
    //posicao da primeira carta selecionada no desvirar carta
    private int posicaoCarta;
    private boolean interfaceAtiva;
    
    
    public Memoria() {
        initComponents();
        try {
            this.s = new Socket("localhost", 3333);
        } catch (IOException ex) {
            System.err.println("Erro na abertura de socket do cliente.");
        }
        jogarNovamenteButton.setVisible(false);
        botoes = new JButton[5][10];
        atraso1 = new Timer(2000, atualizarInterface1);
        atraso2 = new Timer(500, atualizarInterface2);
        atraso3 = new Timer(500, atualizarInterface3);
        atrasar = false;
        acertos = 0;
        estadoPrimeiraCarta = false;
        posicaoCarta = 100;
        interfaceAtiva = false;
        associarBotoes();
        receberTabuleiro();
        ativarStreams();
        //processa a primeira mensagem do servidor e inicia o jogo
        processarMensagem(); 
    }
    
    //recebendo os vetores com as informacoes do tabuleiro gerado no servidor
    private void receberTabuleiro(){
        try {
            sIn = new ObjectInputStream(this.s.getInputStream());
            this.valores = (char[][]) sIn.readObject();
            this.estado = (boolean[][]) sIn.readObject();
        } catch (IOException ex) {
            System.err.println("Erro ao receber tabuleiro do servidor.");
        } catch (ClassNotFoundException ex) {
            System.err.println("Erro ao receber tabuleiro do servidor.");
        }
        
    }
    
    //ativa os fluxos de comunicação entre o cliente e o servidor
    private void ativarStreams(){
        try {
            this.in = new DataInputStream(this.s.getInputStream());
            this.out = new DataOutputStream(this.s.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Erro de conexão com o servidor.");
        }
    }
    
    public void associarBotoes(){
        
        botoes[0][0] = this.carta00;
        botoes[0][1] = this.carta01;
        botoes[0][2] = this.carta02;
        botoes[0][3] = this.carta03;
        botoes[0][4] = this.carta04;
        botoes[0][5] = this.carta05;
        botoes[0][6] = this.carta06;
        botoes[0][7] = this.carta07;
        botoes[0][8] = this.carta08;
        botoes[0][9] = this.carta09;
    
        botoes[1][0] = this.carta10;
        botoes[1][1] = this.carta11;
        botoes[1][2] = this.carta12;
        botoes[1][3] = this.carta13;
        botoes[1][4] = this.carta14;
        botoes[1][5] = this.carta15;
        botoes[1][6] = this.carta16;
        botoes[1][7] = this.carta17;
        botoes[1][8] = this.carta18;
        botoes[1][9] = this.carta19;

        botoes[2][0] = this.carta20;
        botoes[2][1] = this.carta21;
        botoes[2][2] = this.carta22;
        botoes[2][3] = this.carta23;
        botoes[2][4] = this.carta24;
        botoes[2][5] = this.carta25;
        botoes[2][6] = this.carta26;
        botoes[2][7] = this.carta27;
        botoes[2][8] = this.carta28;
        botoes[2][9] = this.carta29;
    
        botoes[3][0] = this.carta30;
        botoes[3][1] = this.carta31;
        botoes[3][2] = this.carta32;
        botoes[3][3] = this.carta33;
        botoes[3][4] = this.carta34;
        botoes[3][5] = this.carta35;
        botoes[3][6] = this.carta36;
        botoes[3][7] = this.carta37;
        botoes[3][8] = this.carta38;
        botoes[3][9] = this.carta39;

        botoes[4][0] = this.carta40;
        botoes[4][1] = this.carta41;
        botoes[4][2] = this.carta42;
        botoes[4][3] = this.carta43;
        botoes[4][4] = this.carta44;
        botoes[4][5] = this.carta45;
        botoes[4][6] = this.carta46;
        botoes[4][7] = this.carta47;
        botoes[4][8] = this.carta48;
        botoes[4][9] = this.carta49;
        
    }
    
    public void desvirarCarta(int linha, int coluna){
        int linha2;
        int coluna2;
        ImageIcon img;
        //se a carta não estiver virada
        if(estado[linha][coluna]==false){
            //se for a primeira carta
            if(estadoPrimeiraCarta == false){

                if(valores[linha][coluna] > 95){
                    img = new ImageIcon("src/img/m_"+valores[linha][coluna]+".png");
                }else{
                    img = new ImageIcon("src/img/"+valores[linha][coluna]+".png");
                }
                botoes[linha][coluna].setIcon(img);
                estado[linha][coluna] = true;
                
                estadoPrimeiraCarta = true;
                posicaoCarta = (linha*10)+coluna;
            
            //se for a segunda carta
            }else{

                if(valores[linha][coluna] > 95){
                    img = new ImageIcon("src/img/m_"+valores[linha][coluna]+".png");
                }else{
                    img = new ImageIcon("src/img/"+valores[linha][coluna]+".png");
                }
                
                botoes[linha][coluna].setIcon(img);
                
                //linha e coluna da primeira carta
                linha2 = posicaoCarta/10;
                coluna2 = posicaoCarta%10;
                
                //calculo da jogada
                if(Math.abs((int)valores[linha][coluna]-(int)valores[linha2][coluna2])==32){
                    pontuacao1.setText(""+ (10+Integer.parseInt(pontuacao1.getText())));
                    estado[linha][coluna] = true;
                    estado[linha2][coluna2] = true;
                    ++acertos;
                    //congela a interface
                    interfaceAtiva = false;
                    System.out.println(acertos);
                    //envia mensagem ao servidor indicando as cartas do acerto
                    if (acertos < 25){//o jogo continua
                        this.quemJoga.setText("VEZ DO OPONENTE");
                        enviarMensagem("ACERTEI " + linha2 + " " + coluna2 + " " +
                        linha + " " + coluna);
                        estadoPrimeiraCarta = false;
                        posicaoCarta = 100;
                        atraso1.start();
                        
                    }else{//o jogo terminou
                        if (Integer.parseInt(pontuacao1.getText()) > 125){
                            this.quemJoga.setText("VOCÊ VENCEU!");
                            enviarMensagem("GANHEI " + linha2 + " " + coluna2 + " " +
                                linha + " " + coluna);
                        }else{
                            this.quemJoga.setText("VOCE PERDEU!");
                            enviarMensagem("PERDI " + linha2 + " " + coluna2 + " " +
                                linha + " " + coluna);
                        }
                        
                        //fechando as streams e socket
                        try {
                            sIn.close();
                            in.close();
                            out.close();
                            s.close();
                        } catch (IOException ex) {
                            System.err.println("Erro ao fechar as streams.");
                        }
                        jogarNovamenteButton.setVisible(true);
                    }
                    
                }else{
                    //em caso de erro as cartas vao voltar ao estado de viradas,
                    //e para isso e necessario um atraso antes de receber uma mensagem
                    //do servidor
                    atrasar = true;
                    linhaAtualizar1 = linha;
                    colunaAtualizar1 = coluna;
                    linhaAtualizar2 = linha2;
                    colunaAtualizar2 = coluna2;
                    
                    estado[linha][coluna] = false;
                    estado[linha2][coluna2] = false;
                    this.quemJoga.setText("VEZ DO OPONENTE");
                    interfaceAtiva = false;
                    //envia mensagem ao servidor indicando que errou
                    enviarMensagem("ERREI");
                    estadoPrimeiraCarta = false;
                    posicaoCarta = 100;
                    atraso1.start();
                }
            }
        }
    }
     
    //processa as mensagens enviadas pelo servidor (protocolo)
    private void processarMensagem(){
        int linha, coluna, linha2, coluna2;
        linha = 0; 
        coluna = 0; 
        linha2 = 0; 
        coluna2 = 0;
        String[] mensagemServidor = null;
        
        try {
            mensagemServidor = (in.readUTF().split(" "));
        } catch (IOException ex) {
            System.err.println("Erro de leitura de mensagem do servidor");
        }
        
        //para facilitar o entendimento da mensagem
        if (mensagemServidor.length > 1){
            linha = Integer.parseInt(mensagemServidor[1]);
            coluna = Integer.parseInt(mensagemServidor[2]);
            linha2 = Integer.parseInt(mensagemServidor[3]);
            coluna2 = Integer.parseInt(mensagemServidor[4]);
            //imprime a mensagem que recebe pelo socket no console
            System.out.println(mensagemServidor[0] + " " + mensagemServidor[1] + " " 
                + mensagemServidor[2] + " " + mensagemServidor[3] + " " 
                + mensagemServidor[4]);
        }else{
            //imprime a mensagem que recebe pelo socket no console
            System.out.println(mensagemServidor[0]);
        }
        
        //protocolo
        switch(mensagemServidor[0]){
            case "SUAVEZ":
                this.quemJoga.setText("SUA VEZ");
                interfaceAtiva = true;
                break;
            case "OPVEZ":
                interfaceAtiva = false;
                atraso2.start();
                break;
            case "OPACERTOU":
                ++acertos;
                this.pontuacao2.setText(""+ (10+Integer.parseInt(this.pontuacao2.getText())));
                desvirarCarta(linha, coluna);
                estadoPrimeiraCarta = false;
                desvirarCarta(linha2, coluna2);
                estadoPrimeiraCarta = false;
                this.quemJoga.setText("SUA VEZ");
                interfaceAtiva = true;              
                break;
            case "OPGANHOU":
                this.pontuacao2.setText(""+ (10+Integer.parseInt(this.pontuacao2.getText())));
                this.quemJoga.setText("VOCE PERDEU!");
                //desvirando as ultimas cartas
                desvirarCarta(linha, coluna);
                estadoPrimeiraCarta = false;
                desvirarCarta(linha2, coluna2);
                estadoPrimeiraCarta = false;
                //fechando as streams
                try {
                    sIn.close();
                    in.close();
                    out.close();
                    s.close();
                } catch (IOException ex) {
                    System.err.println("Erro ao fechar as streams.");
                }
                jogarNovamenteButton.setVisible(true);
                break;
            case "OPPERDEU":
                this.quemJoga.setText("VOCE GANHOU!");
                //desvirando as ultimas cartas
                desvirarCarta(linha, coluna);
                estadoPrimeiraCarta = false;
                desvirarCarta(linha2, coluna2);
                estadoPrimeiraCarta = false;
                //fechando as streams
                try {
                    sIn.close();
                    in.close();
                    out.close();
                    s.close();
                } catch (IOException ex) {
                    System.err.println("Erro ao fechar as streams.");
                }
                jogarNovamenteButton.setVisible(true);
                break;
            default:    
        }
    }
    
    //envia mensagem ao servidor
    private void enviarMensagem(String s){
        try {
            out.writeUTF(s);
        } catch (IOException ex) {
            System.err.println("Erro ao enviar mensagem para o servidor.");
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////
    // funcoes que servem para fazer um delay na interface antes da mesma travar //
    // ao ficar esperando por uma mensagem do servidor                           //
    ActionListener atualizarInterface1 = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (atrasar){
                    botoes[linhaAtualizar1][colunaAtualizar1].setIcon(new ImageIcon("src/img/simbolo.png"));
                    botoes[linhaAtualizar2][colunaAtualizar2].setIcon(new ImageIcon("src/img/simbolo.png"));
                    atrasar = false;
                }
                atraso3.start();
                atraso1.stop();
            }
    };
    
    ActionListener atualizarInterface2 = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                processarMensagem();
                atraso2.stop();
            }
    };
    
    ActionListener atualizarInterface3 = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                processarMensagem();
                atraso3.stop();
            }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        carta00 = new javax.swing.JButton();
        carta01 = new javax.swing.JButton();
        carta02 = new javax.swing.JButton();
        carta03 = new javax.swing.JButton();
        carta04 = new javax.swing.JButton();
        carta05 = new javax.swing.JButton();
        carta11 = new javax.swing.JButton();
        carta12 = new javax.swing.JButton();
        carta13 = new javax.swing.JButton();
        carta14 = new javax.swing.JButton();
        carta15 = new javax.swing.JButton();
        carta10 = new javax.swing.JButton();
        carta06 = new javax.swing.JButton();
        carta07 = new javax.swing.JButton();
        carta08 = new javax.swing.JButton();
        carta16 = new javax.swing.JButton();
        carta17 = new javax.swing.JButton();
        carta18 = new javax.swing.JButton();
        carta21 = new javax.swing.JButton();
        carta26 = new javax.swing.JButton();
        carta22 = new javax.swing.JButton();
        carta27 = new javax.swing.JButton();
        carta23 = new javax.swing.JButton();
        carta28 = new javax.swing.JButton();
        carta24 = new javax.swing.JButton();
        carta25 = new javax.swing.JButton();
        carta36 = new javax.swing.JButton();
        carta31 = new javax.swing.JButton();
        carta37 = new javax.swing.JButton();
        carta32 = new javax.swing.JButton();
        carta38 = new javax.swing.JButton();
        carta33 = new javax.swing.JButton();
        carta34 = new javax.swing.JButton();
        carta35 = new javax.swing.JButton();
        carta20 = new javax.swing.JButton();
        carta30 = new javax.swing.JButton();
        carta40 = new javax.swing.JButton();
        carta41 = new javax.swing.JButton();
        carta46 = new javax.swing.JButton();
        carta42 = new javax.swing.JButton();
        carta47 = new javax.swing.JButton();
        carta43 = new javax.swing.JButton();
        carta48 = new javax.swing.JButton();
        carta44 = new javax.swing.JButton();
        carta45 = new javax.swing.JButton();
        carta49 = new javax.swing.JButton();
        carta09 = new javax.swing.JButton();
        carta19 = new javax.swing.JButton();
        carta29 = new javax.swing.JButton();
        carta39 = new javax.swing.JButton();
        pontuacao1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pontuacao2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        quemJoga = new javax.swing.JLabel();
        jogarNovamenteButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(245, 195, 195));

        jPanel1.setBackground(new java.awt.Color(255, 222, 209));

        carta00.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta00.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta00ActionPerformed(evt);
            }
        });

        carta01.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta01ActionPerformed(evt);
            }
        });

        carta02.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta02ActionPerformed(evt);
            }
        });

        carta03.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta03ActionPerformed(evt);
            }
        });

        carta04.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta04ActionPerformed(evt);
            }
        });

        carta05.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta05ActionPerformed(evt);
            }
        });

        carta11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta11ActionPerformed(evt);
            }
        });

        carta12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta12ActionPerformed(evt);
            }
        });

        carta13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta13ActionPerformed(evt);
            }
        });

        carta14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta14ActionPerformed(evt);
            }
        });

        carta15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta15ActionPerformed(evt);
            }
        });

        carta10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta10ActionPerformed(evt);
            }
        });

        carta06.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta06ActionPerformed(evt);
            }
        });

        carta07.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta07ActionPerformed(evt);
            }
        });

        carta08.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta08.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta08ActionPerformed(evt);
            }
        });

        carta16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta16ActionPerformed(evt);
            }
        });

        carta17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta17ActionPerformed(evt);
            }
        });

        carta18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta18ActionPerformed(evt);
            }
        });

        carta21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta21ActionPerformed(evt);
            }
        });

        carta26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta26ActionPerformed(evt);
            }
        });

        carta22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta22ActionPerformed(evt);
            }
        });

        carta27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta27ActionPerformed(evt);
            }
        });

        carta23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta23ActionPerformed(evt);
            }
        });

        carta28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta28ActionPerformed(evt);
            }
        });

        carta24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta24ActionPerformed(evt);
            }
        });

        carta25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta25ActionPerformed(evt);
            }
        });

        carta36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta36ActionPerformed(evt);
            }
        });

        carta31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta31ActionPerformed(evt);
            }
        });

        carta37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta37ActionPerformed(evt);
            }
        });

        carta32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta32ActionPerformed(evt);
            }
        });

        carta38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta38ActionPerformed(evt);
            }
        });

        carta33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta33ActionPerformed(evt);
            }
        });

        carta34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta34ActionPerformed(evt);
            }
        });

        carta35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta35ActionPerformed(evt);
            }
        });

        carta20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta20ActionPerformed(evt);
            }
        });

        carta30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta30ActionPerformed(evt);
            }
        });

        carta40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta40ActionPerformed(evt);
            }
        });

        carta41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta41ActionPerformed(evt);
            }
        });

        carta46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta46ActionPerformed(evt);
            }
        });

        carta42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta42ActionPerformed(evt);
            }
        });

        carta47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta47ActionPerformed(evt);
            }
        });

        carta43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta43ActionPerformed(evt);
            }
        });

        carta48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta48ActionPerformed(evt);
            }
        });

        carta44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta44ActionPerformed(evt);
            }
        });

        carta45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta45ActionPerformed(evt);
            }
        });

        carta49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta49ActionPerformed(evt);
            }
        });

        carta09.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta09.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta09ActionPerformed(evt);
            }
        });

        carta19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta19ActionPerformed(evt);
            }
        });

        carta29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta29ActionPerformed(evt);
            }
        });

        carta39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/simbolo.png"))); // NOI18N
        carta39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta39ActionPerformed(evt);
            }
        });

        pontuacao1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        pontuacao1.setText("0");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("VOCÊ:");

        pontuacao2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        pontuacao2.setText("0");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("OPONENTE:");

        quemJoga.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        quemJoga.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jogarNovamenteButton.setText("Jogar Novamente");
        jogarNovamenteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jogarNovamenteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(carta40, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(carta41, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(carta42, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(carta43, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(carta44, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(carta45, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(carta46, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(carta47, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(carta48, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(carta10, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta11, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta12, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta13, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta14, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta15, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(carta00, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta01, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta02, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta03, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta04, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta05, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(carta16, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta17, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta18, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(carta06, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta07, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta08, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(carta30, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta31, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta32, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta33, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta34, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta35, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(carta20, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta21, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta22, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta23, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta24, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta25, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(carta36, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta37, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta38, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(carta26, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta27, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(carta28, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pontuacao1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(137, 137, 137)
                                .addComponent(quemJoga, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pontuacao2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(carta49, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(carta19, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(carta09, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(carta39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(381, 381, 381)
                        .addComponent(jogarNovamenteButton)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pontuacao1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pontuacao2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(quemJoga, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                        .addGap(1, 1, 1)))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(carta09, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(carta19, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(carta29, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(carta39, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(carta49, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(carta08, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta07, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta06, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(carta18, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta17, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta16, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(carta05, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta04, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta03, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta02, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta01, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta00, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(carta15, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta14, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta13, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta12, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta11, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta10, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(carta28, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta27, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta26, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(carta38, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta37, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta36, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(carta25, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta24, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta23, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta22, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta21, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta20, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(carta35, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta34, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta33, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta32, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta31, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(carta30, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(carta48, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carta47, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carta46, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carta45, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carta44, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carta43, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carta42, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carta41, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carta40, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jogarNovamenteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void carta00ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta00ActionPerformed
        if (interfaceAtiva) desvirarCarta(0, 0);
    }//GEN-LAST:event_carta00ActionPerformed

    private void carta01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta01ActionPerformed
       if (interfaceAtiva) desvirarCarta(0, 1);
    }//GEN-LAST:event_carta01ActionPerformed

    private void carta02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta02ActionPerformed
       if (interfaceAtiva) desvirarCarta(0, 2);
    }//GEN-LAST:event_carta02ActionPerformed

    private void carta03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta03ActionPerformed
       if (interfaceAtiva) desvirarCarta(0, 3);
    }//GEN-LAST:event_carta03ActionPerformed

    private void carta04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta04ActionPerformed
       if (interfaceAtiva) desvirarCarta(0, 4);
    }//GEN-LAST:event_carta04ActionPerformed

    private void carta05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta05ActionPerformed
       if (interfaceAtiva) desvirarCarta(0, 5);
    }//GEN-LAST:event_carta05ActionPerformed

    private void carta06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta06ActionPerformed
       if (interfaceAtiva) desvirarCarta(0, 6);
    }//GEN-LAST:event_carta06ActionPerformed

    private void carta07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta07ActionPerformed
       if (interfaceAtiva) desvirarCarta(0, 7);
    }//GEN-LAST:event_carta07ActionPerformed

    private void carta08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta08ActionPerformed
       if (interfaceAtiva) desvirarCarta(0, 8);
    }//GEN-LAST:event_carta08ActionPerformed

    private void carta09ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta09ActionPerformed
       if (interfaceAtiva) desvirarCarta(0, 9);
    }//GEN-LAST:event_carta09ActionPerformed

    private void carta10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta10ActionPerformed
       if (interfaceAtiva) desvirarCarta(1, 0);
    }//GEN-LAST:event_carta10ActionPerformed

    private void carta11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta11ActionPerformed
       if (interfaceAtiva) desvirarCarta(1, 1);
    }//GEN-LAST:event_carta11ActionPerformed

    private void carta12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta12ActionPerformed
       if (interfaceAtiva) desvirarCarta(1, 2);
    }//GEN-LAST:event_carta12ActionPerformed

    private void carta13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta13ActionPerformed
       if (interfaceAtiva) desvirarCarta(1, 3);
    }//GEN-LAST:event_carta13ActionPerformed

    private void carta14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta14ActionPerformed
       if (interfaceAtiva) desvirarCarta(1, 4);
    }//GEN-LAST:event_carta14ActionPerformed

    private void carta15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta15ActionPerformed
       if (interfaceAtiva) desvirarCarta(1, 5);
    }//GEN-LAST:event_carta15ActionPerformed

    private void carta16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta16ActionPerformed
       if (interfaceAtiva) desvirarCarta(1, 6);
    }//GEN-LAST:event_carta16ActionPerformed

    private void carta17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta17ActionPerformed
       if (interfaceAtiva) desvirarCarta(1, 7);
    }//GEN-LAST:event_carta17ActionPerformed

    private void carta18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta18ActionPerformed
       if (interfaceAtiva) desvirarCarta(1, 8);
    }//GEN-LAST:event_carta18ActionPerformed

    private void carta19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta19ActionPerformed
       if (interfaceAtiva) desvirarCarta(1, 9);
    }//GEN-LAST:event_carta19ActionPerformed

    private void carta20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta20ActionPerformed
       if (interfaceAtiva) desvirarCarta(2, 0);
    }//GEN-LAST:event_carta20ActionPerformed

    private void carta21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta21ActionPerformed
       if (interfaceAtiva) desvirarCarta(2, 1);
    }//GEN-LAST:event_carta21ActionPerformed

    private void carta22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta22ActionPerformed
       if (interfaceAtiva) desvirarCarta(2, 2);
    }//GEN-LAST:event_carta22ActionPerformed

    private void carta23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta23ActionPerformed
       if (interfaceAtiva) desvirarCarta(2, 3);
    }//GEN-LAST:event_carta23ActionPerformed

    private void carta24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta24ActionPerformed
       if (interfaceAtiva) desvirarCarta(2, 4);
    }//GEN-LAST:event_carta24ActionPerformed

    private void carta25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta25ActionPerformed
       if (interfaceAtiva) desvirarCarta(2, 5);
    }//GEN-LAST:event_carta25ActionPerformed

    private void carta26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta26ActionPerformed
       if (interfaceAtiva) desvirarCarta(2, 6);
    }//GEN-LAST:event_carta26ActionPerformed

    private void carta27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta27ActionPerformed
       if (interfaceAtiva) desvirarCarta(2, 7);
    }//GEN-LAST:event_carta27ActionPerformed

    private void carta28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta28ActionPerformed
       if (interfaceAtiva) desvirarCarta(2, 8);
    }//GEN-LAST:event_carta28ActionPerformed

    private void carta29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta29ActionPerformed
       if (interfaceAtiva) desvirarCarta(2, 9);
    }//GEN-LAST:event_carta29ActionPerformed

    private void carta30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta30ActionPerformed
       if (interfaceAtiva) desvirarCarta(3, 0);
    }//GEN-LAST:event_carta30ActionPerformed

    private void carta31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta31ActionPerformed
       if (interfaceAtiva) desvirarCarta(3, 1);
    }//GEN-LAST:event_carta31ActionPerformed

    private void carta32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta32ActionPerformed
       if (interfaceAtiva) desvirarCarta(3, 2);
    }//GEN-LAST:event_carta32ActionPerformed

    private void carta33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta33ActionPerformed
       if (interfaceAtiva) desvirarCarta(3, 3);
    }//GEN-LAST:event_carta33ActionPerformed

    private void carta34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta34ActionPerformed
       if (interfaceAtiva) desvirarCarta(3, 4);
    }//GEN-LAST:event_carta34ActionPerformed

    private void carta35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta35ActionPerformed
       if (interfaceAtiva) desvirarCarta(3, 5);
    }//GEN-LAST:event_carta35ActionPerformed

    private void carta36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta36ActionPerformed
       if (interfaceAtiva) desvirarCarta(3, 6);
    }//GEN-LAST:event_carta36ActionPerformed

    private void carta37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta37ActionPerformed
       if (interfaceAtiva) desvirarCarta(3, 7);
    }//GEN-LAST:event_carta37ActionPerformed

    private void carta38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta38ActionPerformed
       if (interfaceAtiva) desvirarCarta(3, 8);
    }//GEN-LAST:event_carta38ActionPerformed

    private void carta39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta39ActionPerformed
       if (interfaceAtiva) desvirarCarta(3, 9);
    }//GEN-LAST:event_carta39ActionPerformed

    private void carta40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta40ActionPerformed
       if (interfaceAtiva) desvirarCarta(4, 0);
    }//GEN-LAST:event_carta40ActionPerformed

    private void carta41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta41ActionPerformed
       if (interfaceAtiva) desvirarCarta(4, 1);
    }//GEN-LAST:event_carta41ActionPerformed

    private void carta42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta42ActionPerformed
       if (interfaceAtiva) desvirarCarta(4, 2);
    }//GEN-LAST:event_carta42ActionPerformed

    private void carta43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta43ActionPerformed
       if (interfaceAtiva) desvirarCarta(4, 3);
    }//GEN-LAST:event_carta43ActionPerformed

    private void carta44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta44ActionPerformed
       if (interfaceAtiva) desvirarCarta(4, 4);
    }//GEN-LAST:event_carta44ActionPerformed

    private void carta45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta45ActionPerformed
       if (interfaceAtiva) desvirarCarta(4, 5);
    }//GEN-LAST:event_carta45ActionPerformed

    private void carta46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta46ActionPerformed
       if (interfaceAtiva) desvirarCarta(4, 6);
    }//GEN-LAST:event_carta46ActionPerformed

    private void carta47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta47ActionPerformed
       if (interfaceAtiva) desvirarCarta(4, 7);
    }//GEN-LAST:event_carta47ActionPerformed

    private void carta48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta48ActionPerformed
       if (interfaceAtiva) desvirarCarta(4, 8);
    }//GEN-LAST:event_carta48ActionPerformed

    private void carta49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta49ActionPerformed
       if (interfaceAtiva) desvirarCarta(4, 9);
    }//GEN-LAST:event_carta49ActionPerformed

    private void jogarNovamenteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jogarNovamenteButtonActionPerformed
        Inicio i = new Inicio();
        i.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jogarNovamenteButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Memoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Memoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Memoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Memoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new Memoria().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton carta00;
    private javax.swing.JButton carta01;
    private javax.swing.JButton carta02;
    private javax.swing.JButton carta03;
    private javax.swing.JButton carta04;
    private javax.swing.JButton carta05;
    private javax.swing.JButton carta06;
    private javax.swing.JButton carta07;
    private javax.swing.JButton carta08;
    private javax.swing.JButton carta09;
    private javax.swing.JButton carta10;
    private javax.swing.JButton carta11;
    private javax.swing.JButton carta12;
    private javax.swing.JButton carta13;
    private javax.swing.JButton carta14;
    private javax.swing.JButton carta15;
    private javax.swing.JButton carta16;
    private javax.swing.JButton carta17;
    private javax.swing.JButton carta18;
    private javax.swing.JButton carta19;
    private javax.swing.JButton carta20;
    private javax.swing.JButton carta21;
    private javax.swing.JButton carta22;
    private javax.swing.JButton carta23;
    private javax.swing.JButton carta24;
    private javax.swing.JButton carta25;
    private javax.swing.JButton carta26;
    private javax.swing.JButton carta27;
    private javax.swing.JButton carta28;
    private javax.swing.JButton carta29;
    private javax.swing.JButton carta30;
    private javax.swing.JButton carta31;
    private javax.swing.JButton carta32;
    private javax.swing.JButton carta33;
    private javax.swing.JButton carta34;
    private javax.swing.JButton carta35;
    private javax.swing.JButton carta36;
    private javax.swing.JButton carta37;
    private javax.swing.JButton carta38;
    private javax.swing.JButton carta39;
    private javax.swing.JButton carta40;
    private javax.swing.JButton carta41;
    private javax.swing.JButton carta42;
    private javax.swing.JButton carta43;
    private javax.swing.JButton carta44;
    private javax.swing.JButton carta45;
    private javax.swing.JButton carta46;
    private javax.swing.JButton carta47;
    private javax.swing.JButton carta48;
    private javax.swing.JButton carta49;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jogarNovamenteButton;
    private javax.swing.JLabel pontuacao1;
    private javax.swing.JLabel pontuacao2;
    private javax.swing.JLabel quemJoga;
    // End of variables declaration//GEN-END:variables
}
