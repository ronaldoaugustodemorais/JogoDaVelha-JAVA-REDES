
package jogovelhaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Partida implements Runnable
{
    private Socket j1, j2;
    private boolean fimDeJogo;
    private DataInputStream j1In, j2In;
    private DataOutputStream j1Out, j2Out;
    
    public Partida(Socket j1, Socket j2){
        this.j1 = j1;
        this.j2 = j2;
        this.fimDeJogo = false;
    }

    @Override
    public void run()
    {
        String mensagemAtual;
        ativarStreams();
        
        try {
            j1Out.writeUTF("SUAVEZ");
            j2Out.writeUTF("OPVEZ");
            
            while (!fimDeJogo){
                mensagemAtual = j1In.readUTF();
                j2Out.writeUTF(processarMensagem(mensagemAtual));
                
                if ((mensagemAtual.contains("GANHEI")) || (mensagemAtual.contains("VELHA"))) break;
                
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
            System.err.println("Erro de comunicacao com os clientes.");
            ex.printStackTrace();
        }
    }
    
    private String processarMensagem(String s){
        String[] mensagem = s.split(" ");
        
        switch (mensagem[0]){
            case "GANHEI":
                fimDeJogo = true;
                return "PERDEU " + mensagem[1] + " " + mensagem[2];
            case "VELHA":
                fimDeJogo = true;
                return "VELHA " + mensagem[1] + " " + mensagem[2];
            case "TIMEOUT":
                return "SUAVEZ";
            default:
                return mensagem[0] + " " + mensagem[1];
        }
    }
    
    private void ativarStreams(){
        try {
            j1In = new DataInputStream(j1.getInputStream());
            j1Out = new DataOutputStream(j1.getOutputStream());
            j2In = new DataInputStream(j2.getInputStream());
            j2Out = new DataOutputStream(j2.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Erro ao abrir streams.");
            ex.printStackTrace();
        }
    }
}
