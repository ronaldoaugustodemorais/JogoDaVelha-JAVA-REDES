package jogovelha;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;



public class Tabuleiro extends javax.swing.JFrame {
    private Socket s;
    
    
    private JButton botoes[][];
    private int valores[][];
    private boolean estado[][];
    private boolean interfaceAtiva;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread timeOut;
    
    
    public Tabuleiro() {
        initComponents();
        try {
            this.s = new Socket("localhost", 2222);
        } catch (IOException ex) {
            System.err.println("Erro na abertura de socket do cliente.");
        }
        jogarNovamente.setVisible(false);
        botoes = new JButton[3][3];
        interfaceAtiva = false;
        associarBotoes();
        associarEstado();
        ativarStreams();
        //processa a primeira mensagem do servidor e inicia o jogo
        new Thread(new ProcessaMensagem(in, out)).start();
    }
   
    
    
    public void associarBotoes(){
        
        botoes[0][0] = this.carta00;
        botoes[0][1] = this.carta01;
        botoes[0][2] = this.carta02;
        botoes[1][0] = this.carta03;
        botoes[1][1] = this.carta04;
        botoes[1][2] = this.carta05;
        botoes[2][0] = this.carta06;
        botoes[2][1] = this.carta07;
        botoes[2][2] = this.carta08;
        
    }
    
    //acao quando um botao é clicado
    public void desvirarBotao(int linha, int coluna){
        ImageIcon img;
        //se o botao ainda nao foi clicado antes
        if(estado[linha][coluna]==false){
            //interrompe o timeout pois o jogador fez uma acao
            timeOut.interrupt();
            img = new ImageIcon("src/img/X.png");
            botoes[linha][coluna].setIcon(img);
            //1 representa X na matriz de valores
            valores[linha][coluna] = 1;
            //o botao nao pode mais ser usado
            estado[linha][coluna] = true;
            
            //se nessa ultima jogada o cliente conseguiu ganhar
            if(verificarTabuleiro()){
                pintarX();
                quemJoga.setText("VOCE GANHOU!");
                enviarMensagem("GANHEI " + linha + " " + coluna);
                interfaceAtiva = false;
                jogarNovamente.setVisible(true);
            //caso ele nao tenha ganhado o jogo continua ou deu velha
            }else{
                if (!deuVelha()){
                    quemJoga.setText("VEZ DO OPONENTE");
                    enviarMensagem(linha + " " + coluna);
                    interfaceAtiva = false;
                    new Thread(new ProcessaMensagem(in, out)).start();
                }else{
                    quemJoga.setText("DEU VELHA!");
                    enviarMensagem("VELHA " + linha + " " + coluna);
                    interfaceAtiva = false;
                    jogarNovamente.setVisible(true);
                }
            }
        }
    }
    
    //verifica se existe alguma das 8 sequencias possiveis para o jogo ser ganho
    //na jogada
    private boolean verificarTabuleiro(){
        if ((valores[0][0] == 1) && (valores[0][1]) == 1 && (valores[0][2] == 1) ||
                (valores[1][0] == 1) && (valores[1][1]) == 1 && (valores[1][2] == 1) ||
                (valores[2][0] == 1) && (valores[2][1]) == 1 && (valores[2][2] == 1) ||
                (valores[0][0] == 1) && (valores[1][0]) == 1 && (valores[2][0] == 1) ||
                (valores[0][1] == 1) && (valores[1][1]) == 1 && (valores[2][1] == 1) ||
                (valores[0][2] == 1) && (valores[1][2]) == 1 && (valores[2][2] == 1) ||
                (valores[0][0] == 1) && (valores[1][1]) == 1 && (valores[2][2] == 1) ||
                (valores[0][2] == 1) && (valores[1][1]) == 1 && (valores[2][0] == 1)){
            return true;
        }
        return false;
    }
    
    //se nao tiver nenhum 0 na matriz de valores e ninguem ganhou, o jogo deu velha
    private boolean deuVelha(){
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 3; ++j){
                if (valores[i][j] == 0) return false;
            }
        }
        return true;
    }
    
    private void pintarX(){
        if ((valores[0][0] == 1) && (valores[0][1]) == 1 && (valores[0][2] == 1)){
            botoes[0][0].setIcon(new ImageIcon("src/img/Xc.png"));
            botoes[0][1].setIcon(new ImageIcon("src/img/Xc.png"));
            botoes[0][2].setIcon(new ImageIcon("src/img/Xc.png"));
        }else{
            if ((valores[1][0] == 1) && (valores[1][1]) == 1 && (valores[1][2] == 1)){
                botoes[1][0].setIcon(new ImageIcon("src/img/Xc.png"));
                botoes[1][1].setIcon(new ImageIcon("src/img/Xc.png"));
                botoes[1][2].setIcon(new ImageIcon("src/img/Xc.png"));
            }else{
                if ((valores[2][0] == 1) && (valores[2][1]) == 1 && (valores[2][2] == 1)){
                    botoes[2][0].setIcon(new ImageIcon("src/img/Xc.png"));
                    botoes[2][1].setIcon(new ImageIcon("src/img/Xc.png"));
                    botoes[2][2].setIcon(new ImageIcon("src/img/Xc.png"));
                }else{
                    if ((valores[0][0] == 1) && (valores[1][0]) == 1 && (valores[2][0] == 1)){
                        botoes[0][0].setIcon(new ImageIcon("src/img/Xc.png"));
                        botoes[1][0].setIcon(new ImageIcon("src/img/Xc.png"));
                        botoes[2][0].setIcon(new ImageIcon("src/img/Xc.png"));
                    }else{
                        if ((valores[0][1] == 1) && (valores[1][1]) == 1 && (valores[2][1] == 1)){
                            botoes[0][1].setIcon(new ImageIcon("src/img/Xc.png"));
                            botoes[1][1].setIcon(new ImageIcon("src/img/Xc.png"));
                            botoes[2][1].setIcon(new ImageIcon("src/img/Xc.png"));
                        }else{
                            if ((valores[0][2] == 1) && (valores[1][2]) == 1 && (valores[2][2] == 1)){
                                botoes[0][2].setIcon(new ImageIcon("src/img/Xc.png"));
                                botoes[1][2].setIcon(new ImageIcon("src/img/Xc.png"));
                                botoes[2][2].setIcon(new ImageIcon("src/img/Xc.png"));
                            }else{
                                if ((valores[0][0] == 1) && (valores[1][1]) == 1 && (valores[2][2] == 1)){
                                    botoes[0][0].setIcon(new ImageIcon("src/img/Xc.png"));
                                    botoes[1][1].setIcon(new ImageIcon("src/img/Xc.png"));
                                    botoes[2][2].setIcon(new ImageIcon("src/img/Xc.png"));
                                }else{
                                    if ((valores[0][2] == 1) && (valores[1][1]) == 1 && (valores[2][0] == 1)){
                                        botoes[0][2].setIcon(new ImageIcon("src/img/Xc.png"));
                                        botoes[1][1].setIcon(new ImageIcon("src/img/Xc.png"));
                                        botoes[2][0].setIcon(new ImageIcon("src/img/Xc.png"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
    }
    
    private void pintarO(){
        if ((valores[0][0] == 2) && (valores[0][1]) == 2 && (valores[0][2] == 2)){
            botoes[0][0].setIcon(new ImageIcon("src/img/Oc.png"));
            botoes[0][1].setIcon(new ImageIcon("src/img/Oc.png"));
            botoes[0][2].setIcon(new ImageIcon("src/img/Oc.png"));
        }else{
            if ((valores[1][0] == 2) && (valores[1][1]) == 2 && (valores[1][2] == 2)){
                botoes[1][0].setIcon(new ImageIcon("src/img/Oc.png"));
                botoes[1][1].setIcon(new ImageIcon("src/img/Oc.png"));
                botoes[1][2].setIcon(new ImageIcon("src/img/Oc.png"));
            }else{
                if ((valores[2][0] == 2) && (valores[2][1]) == 2 && (valores[2][2] == 2)){
                    botoes[2][0].setIcon(new ImageIcon("src/img/Oc.png"));
                    botoes[2][1].setIcon(new ImageIcon("src/img/Oc.png"));
                    botoes[2][2].setIcon(new ImageIcon("src/img/Oc.png"));
                }else{
                    if ((valores[0][0] == 2) && (valores[1][0]) == 2 && (valores[2][0] == 2)){
                        botoes[0][0].setIcon(new ImageIcon("src/img/Oc.png"));
                        botoes[1][0].setIcon(new ImageIcon("src/img/Oc.png"));
                        botoes[2][0].setIcon(new ImageIcon("src/img/Oc.png"));
                    }else{
                        if ((valores[0][1] == 2) && (valores[1][1]) == 2 && (valores[2][1] == 2)){
                            botoes[0][1].setIcon(new ImageIcon("src/img/Oc.png"));
                            botoes[1][1].setIcon(new ImageIcon("src/img/Oc.png"));
                            botoes[2][1].setIcon(new ImageIcon("src/img/Oc.png"));
                        }else{
                            if ((valores[0][2] == 2) && (valores[1][2]) == 2 && (valores[2][2] == 2)){
                                botoes[0][2].setIcon(new ImageIcon("src/img/Oc.png"));
                                botoes[1][2].setIcon(new ImageIcon("src/img/Oc.png"));
                                botoes[2][2].setIcon(new ImageIcon("src/img/Oc.png"));
                            }else{
                                if ((valores[0][0] == 2) && (valores[1][1]) == 2 && (valores[2][2] == 2)){
                                    botoes[0][0].setIcon(new ImageIcon("src/img/Oc.png"));
                                    botoes[1][1].setIcon(new ImageIcon("src/img/Oc.png"));
                                    botoes[2][2].setIcon(new ImageIcon("src/img/Oc.png"));
                                }else{
                                    if ((valores[0][2] == 2) && (valores[1][1]) == 2 && (valores[2][0] == 2)){
                                        botoes[0][2].setIcon(new ImageIcon("src/img/Oc.png"));
                                        botoes[1][1].setIcon(new ImageIcon("src/img/Oc.png"));
                                        botoes[2][0].setIcon(new ImageIcon("src/img/Oc.png"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    //envia mensagem ao servidor pelo stream
    private void enviarMensagem(String s){
        try {
            out.writeUTF(s);
        } catch (IOException ex) {
            System.err.println("Erro ao enviar mensagem ao servidor.");
            ex.printStackTrace();
        }
    }
    
    //ativa os fluxos de comunicacao com o servidor
    private void ativarStreams(){
        try {
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Erro na ativacao das streams.");
            ex.printStackTrace();
        }        
    }
    
    //no inicio todos os botoes estao desvirados
    private void associarEstado(){
        valores = new int[3][3];
        estado = new boolean[3][3];
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 3; ++j){
                estado[i][j] = false;
            }
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////
    // Threads de timeout e de processar as mensagens do servidor                //
    ///////////////////////////////////////////////////////////////////////////////
    
    //A thread espera 20 segundos e entao nao faz jogada alguma e passa a vez
    //para o oponente
    public class TimeOut implements Runnable{
        private DataOutputStream out;
        
        public TimeOut(DataOutputStream out){
            this.out = out;
        }
        
        @Override
        public void run() {
            try {
                //cada jogador tem 20 segundos para jogar
                Thread.sleep(20000);
                quemJoga.setText("VEZ DO OPONENTE");
                out.writeUTF("TIMEOUT");
                interfaceAtiva = false;
                new Thread(new ProcessaMensagem(in, out)).start(); 
            } catch (IOException ex) {
                System.err.println("Erro de comunicacao com o servidor.");
            } catch (InterruptedException ex) {
                
            }
        }
        
    }
    
    //A Thread processa uma mensagem do servidor
    public class ProcessaMensagem implements Runnable{
        private DataInputStream in;
        private DataOutputStream out;
        
        public ProcessaMensagem(DataInputStream in, DataOutputStream out){
            this.in = in;
            this.out = out;
        }
        
        
        @Override
        public void run() {
            String mensagem = "";
            String[] mensagem2;
            ImageIcon img;
            try {
                mensagem = in.readUTF();
            } catch (IOException ex) {
                System.err.println("Erro ao receber mensagem do servidor");
            }

            mensagem2 = mensagem.split(" ");

            switch (mensagem2[0]){
                case "SUAVEZ":
                    quemJoga.setText("SUAVEZ");
                    interfaceAtiva = true;
                    timeOut = new Thread(new TimeOut(out));
                    timeOut.start();
                    break;
                case "OPVEZ":
                    interfaceAtiva = false;
                    quemJoga.setText("VEZ DO OPONENTE");
                    new Thread(new ProcessaMensagem(in, out)).start();
                    break;
                case "PERDEU":
                    valores[Integer.parseInt(mensagem2[1])][Integer.parseInt(mensagem2[2])] = 2;
                    estado[Integer.parseInt(mensagem2[1])][Integer.parseInt(mensagem2[2])] = true;
                    pintarO();
                    quemJoga.setText("VOCE PERDEU!");
                    interfaceAtiva = false;
                    jogarNovamente.setVisible(true);
                    break;
                case "VELHA":
                    img = new ImageIcon("src/img/O.png");
                    valores[Integer.parseInt(mensagem2[1])][Integer.parseInt(mensagem2[2])] = 2;
                    botoes[Integer.parseInt(mensagem2[1])][Integer.parseInt(mensagem2[2])].setIcon(img);
                    estado[Integer.parseInt(mensagem2[1])][Integer.parseInt(mensagem2[2])] = true;
                    quemJoga.setText("DEU VELHA!");
                    interfaceAtiva = false;
                    jogarNovamente.setVisible(true);
                    break;
                default:
                    img = new ImageIcon("src/img/O.png");
                    valores[Integer.parseInt(mensagem2[0])][Integer.parseInt(mensagem2[1])] = 2;
                    botoes[Integer.parseInt(mensagem2[0])][Integer.parseInt(mensagem2[1])].setIcon(img);
                    estado[Integer.parseInt(mensagem2[0])][Integer.parseInt(mensagem2[1])] = true;
                    quemJoga.setText("SUA VEZ");
                    interfaceAtiva = true;
                    timeOut = new Thread(new TimeOut(out));
                    timeOut.start();
                    break;
            }
        }
        
    }

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
        carta06 = new javax.swing.JButton();
        carta07 = new javax.swing.JButton();
        carta08 = new javax.swing.JButton();
        quemJoga = new javax.swing.JLabel();
        jogarNovamente = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(245, 195, 195));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 222, 209));

        carta00.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta00ActionPerformed(evt);
            }
        });

        carta01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta01ActionPerformed(evt);
            }
        });

        carta02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta02ActionPerformed(evt);
            }
        });

        carta03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta03ActionPerformed(evt);
            }
        });

        carta04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta04ActionPerformed(evt);
            }
        });

        carta05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta05ActionPerformed(evt);
            }
        });

        carta06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta06ActionPerformed(evt);
            }
        });

        carta07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta07ActionPerformed(evt);
            }
        });

        carta08.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carta08ActionPerformed(evt);
            }
        });

        quemJoga.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        quemJoga.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        quemJoga.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jogarNovamente.setText("Jogar Novamente");
        jogarNovamente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jogarNovamenteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(121, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(carta00, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(carta01, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(carta02, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(carta03, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(carta04, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(carta05, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(quemJoga, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(carta06, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(carta07, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(carta08, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(122, 122, 122))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jogarNovamente, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(156, 156, 156))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(quemJoga, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(carta02, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(carta01, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(carta00, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(carta05, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(carta04, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(carta03, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(carta08, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(carta07, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(carta06, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(jogarNovamente, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jogarNovamenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jogarNovamenteActionPerformed
        Inicio i = new Inicio();
        i.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jogarNovamenteActionPerformed

    private void carta08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta08ActionPerformed
        if (interfaceAtiva) desvirarBotao(2, 2);
    }//GEN-LAST:event_carta08ActionPerformed

    private void carta07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta07ActionPerformed
        if (interfaceAtiva) desvirarBotao(2, 1);
    }//GEN-LAST:event_carta07ActionPerformed

    private void carta06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta06ActionPerformed
        if (interfaceAtiva) desvirarBotao(2, 0);
    }//GEN-LAST:event_carta06ActionPerformed

    private void carta05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta05ActionPerformed
        if (interfaceAtiva) desvirarBotao(1, 2);
    }//GEN-LAST:event_carta05ActionPerformed

    private void carta04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta04ActionPerformed
        if (interfaceAtiva) desvirarBotao(1, 1);
    }//GEN-LAST:event_carta04ActionPerformed

    private void carta03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta03ActionPerformed
        if (interfaceAtiva) desvirarBotao(1, 0);
    }//GEN-LAST:event_carta03ActionPerformed

    private void carta02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta02ActionPerformed
        if (interfaceAtiva) desvirarBotao(0, 2);
    }//GEN-LAST:event_carta02ActionPerformed

    private void carta01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta01ActionPerformed
        if (interfaceAtiva) desvirarBotao(0, 1);
    }//GEN-LAST:event_carta01ActionPerformed

    private void carta00ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carta00ActionPerformed
        if (interfaceAtiva) desvirarBotao(0, 0);
    }//GEN-LAST:event_carta00ActionPerformed

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
            java.util.logging.Logger.getLogger(Tabuleiro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tabuleiro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tabuleiro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tabuleiro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jogarNovamente;
    private javax.swing.JLabel quemJoga;
    // End of variables declaration//GEN-END:variables
}
