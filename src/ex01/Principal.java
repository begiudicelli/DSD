package ex01;

public class Principal {
    public static void main(String[] args) {
        ControleImpressao controleImpressao = new ControleImpressao();

        for(int i = 1; i < 10; i++){
            new Thread(new LetraA(controleImpressao)).start();
            new Thread(new LetraB(controleImpressao)).start();
            new Thread(new LetraC(controleImpressao)).start();
        }
    }
}
