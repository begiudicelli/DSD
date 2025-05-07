package ex03;

public class Decrementador implements Runnable {
    private final ContadorLimitado contador;

    public Decrementador(ContadorLimitado contador) {
        this.contador = contador;
    }

    @Override
    public void run() {
        while (true) {
            try {
                contador.decrementa();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
