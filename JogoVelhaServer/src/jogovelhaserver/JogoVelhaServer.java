package jogovelhaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class JogoVelhaServer {

    
    public static void main(String[] args)
    {
        Executor exec = Executors.newCachedThreadPool();
        Socket j1, j2;
        int nPartidas = 0;
        try {
            ServerSocket s = new ServerSocket(2222);
            System.out.println("Servidor ligado.");
            while(true){
                j1 = s.accept();
                System.out.println("Jogador 1 conectado");
                j2 = s.accept();
                System.out.println("Jogador 2 conectado");
                exec.execute(new Partida(j1,j2));
                ++nPartidas;
                System.out.println("Partida "+ nPartidas + " iniciada");
            }
        } catch (IOException ex) {
            System.err.println("Erro na abertura do servidor.");
        }
    }
    
}
