package ex01;

public class LetraC implements Runnable {
    ControleImpressao controleImpressao = new ControleImpressao();

    public LetraC(ControleImpressao controleImpressao) {
        this.controleImpressao = controleImpressao;
    }

    public void run() {
        while (true) {
            try {
                controleImpressao.imprimirC();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
