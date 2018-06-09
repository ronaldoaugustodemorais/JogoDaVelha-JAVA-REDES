//Classe que vai gerenciar cada partida
package servidorterralibras;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class Partida implements Runnable{
    private Socket j1, j2;
    private Tabuleiro tabuleiro;
    private ObjectOutputStream outTabuleiroJ1, outTabuleiroJ2;
    private DataOutputStream j1Out, j2Out;
    private DataInputStream j1In, j2In;
    private boolean fimDeJogo;
    
    public Partida(Socket j1, Socket j2){
        this.j1 = j1;
        this.j2 = j2;
        this.tabuleiro = new Tabuleiro();
        this.fimDeJogo = false;
    }

    @Override
    public void run() {
        enviarTabuleiro();
        partida();
    }
    
    //enviar as cartas e seus estados para os clientes
    private void enviarTabuleiro(){
        try {
            outTabuleiroJ1 = new ObjectOutputStream(j1.getOutputStream());
            outTabuleiroJ2 = new ObjectOutputStream(j2.getOutputStream());
            outTabuleiroJ1.writeObject(tabuleiro.getValores());
            outTabuleiroJ2.writeObject(tabuleiro.getValores());
            outTabuleiroJ1.writeObject(tabuleiro.getEstado());
            outTabuleiroJ2.writeObject(tabuleiro.getEstado());
        } catch (IOException ex) {
            System.err.println("Erro ao enviar tabuleiro para os clientes.");
        }
    }
    
    //loop principal da partida
    private void partida(){
        try {
            j1Out = new DataOutputStream(j1.getOutputStream());
            j2Out = new DataOutputStream(j2.getOutputStream());
            j1In = new DataInputStream(j1.getInputStream());
            j2In = new DataInputStream(j2.getInputStream());
            
            //jogador1 inicia a partida
            j1Out.writeUTF("SUAVEZ");
            j2Out.writeUTF("OPVEZ");
            
            //loop do resto da partida
            while(!fimDeJogo){
                String mensagemAtual = "";
                mensagemAtual = j1In.readUTF();                
                
                j2Out.writeUTF(processarMensagem(mensagemAtual));
                
                //se o oponente 1 ganhar sai do loop e acaba o jogo
                if (mensagemAtual.contains("GANHEI")) break;
                mensagemAtual = j2In.readUTF();
                
                j1Out.writeUTF(processarMensagem(mensagemAtual));
            }
            
            //fechamento dos sockets e streams
            j1Out.close();
            j2Out.close();
            j1In.close();
            j2In.close();
            j1.close();
            j2.close();
        } catch (IOException ex) {
            System.err.println("Erro ao enviar mensagens durante a partida.");
        }
        
    }
    
    //processa o protocolo de mensagens
    private String processarMensagem(String s){
        String[] s2 = s.split(" ");
        
        switch (s2[0]){
            case "ACERTEI":
                return "OPACERTOU " + s2[1] + " " + s2[2] + " " + s2[3] + " " +
                        s2[4];
            case "ERREI":
                return "SUAVEZ";
            case "GANHEI":
                fimDeJogo = true;
                //manda as ultimas cartas abertas
                return "OPGANHOU " + s2[1] + " " + s2[2] + " " + s2[3] + " " +
                        s2[4];
            case "PERDI":
                return "OPPERDEU " + s2[1] + " " + s2[2] + " " + s2[3] + " " +
                        s2[4];
        }
        return null;
    }
    
}
