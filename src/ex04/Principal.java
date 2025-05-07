package ex04;

public class Principal {
    public static void main(String[] args) {
        int totalPassageiros = 10;
        int capacidadeCarro = 4;

        Carro carro = new Carro(capacidadeCarro);
        Thread carroThread = new Thread(carro);
        carroThread.start();

        for (int i = 0; i < totalPassageiros; i++) {
            new Thread(new Passageiro(carro, i)).start();
        }
    }
}
