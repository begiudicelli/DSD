package ex03;

public class Incrementador implements Runnable {
    private final ContadorLimitado contador;

    public Incrementador(ContadorLimitado contador) {
        this.contador = contador;
    }

    @Override
    public void run() {
        while (true) {
            try {
                contador.incrementa();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
