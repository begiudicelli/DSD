package ex01;

public class LetraA implements Runnable {
    ControleImpressao controleImpressao = new ControleImpressao();

    public LetraA(ControleImpressao controleImpressao) {
        this.controleImpressao = controleImpressao;
    }

    public void run() {
        while (true) {
            try {
                controleImpressao.imprimirA();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
