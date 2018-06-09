//Tabuleiro de cada partida
package servidorterralibras;


public class Tabuleiro {

    private char valores[][];
    private boolean estado[][];

    public Tabuleiro() {
        valores = new char[5][10];
        estado = new boolean[5][10];
        montarTabuleiro();
        embaralharCartas();
        imprimirTabuleiro();

    }

    public void montarTabuleiro() {
        int i, j;
        for (i = 0; i < 5; ++i) {
            for (j = 0; j < 10; ++j) {
                estado[i][j] = false;
                valores[i][j] = '*';
            }
        }

    }

    public void imprimirTabuleiro() {
        int i, j;

        for (i = 0; i < 5; ++i) {
            for (j = 0; j < 10; ++j) {
                System.err.print(" " + valores[i][j]);
            }
            System.err.println(" ");
        }
    }

    public void embaralharCartas() {
        int qtLetras = 0;
        int controle;
        int local;
        int linha, coluna;
        int inicio, fim;

        while (qtLetras < 20) {
            controle = 0;
            while (controle < 2) {
                local = (int) (Math.random() * 50);
                linha = local / 10; //escolhendo a linha
                coluna = local % 10; //escolhendo a coluna

                if (valores[linha][coluna] == '*') {
                    if (controle == 0) {
                        valores[linha][coluna] = (char) (qtLetras + 97);
                    } else {
                        valores[linha][coluna] = (char) (qtLetras + 65);
                        ++qtLetras;
                    }
                    ++controle;

                }
            }
        }

        inicio = 0;
        fim = 49;
        while (qtLetras < 25) {
            do {
                linha = inicio / 10;
                coluna = inicio % 10;
                ++inicio;
            } while (valores[linha][coluna] != '*');

            valores[linha][coluna] = (char) (qtLetras + 65);

            do {
                linha = fim / 10;
                coluna = fim % 10;
                --fim;
            } while (valores[linha][coluna] != '*');

            valores[linha][coluna] = (char) (qtLetras + 97);

            ++qtLetras;
        }

    }

    public char[][] getValores() {
        return valores;
    }

    public void setValores(char[][] valores) {
        this.valores = valores;
    }

    public boolean[][] getEstado() {
        return estado;
    }

    public void setEstado(boolean[][] estado) {
        this.estado = estado;
    }
}

