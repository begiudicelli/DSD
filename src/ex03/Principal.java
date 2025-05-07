package ex03;

public class Principal {
    public static void main(String[] args) {
        ContadorLimitado contador = new ContadorLimitado(-10, 10, 0);

        for (int i = 1; i <= 5000; i++) {
            new Thread(new Incrementador(contador), "Thread: " + i).start();
            new Thread(new Decrementador(contador), "Thread: " + i).start();
        }
    }
}
