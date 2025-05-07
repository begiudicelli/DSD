package ex01;

public class LetraB implements Runnable {
    ControleImpressao controleImpressao = new ControleImpressao();

    public LetraB(ControleImpressao controleImpressao) {
        this.controleImpressao = controleImpressao;
    }

    public void run() {
        while (true) {
            try {
                controleImpressao.imprimirB();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
