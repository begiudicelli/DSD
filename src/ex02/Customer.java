package ex02;

public class Customer implements Runnable {
    private final Barbershop barbershop;

    public Customer(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        while (true) {
            try {
                barbershop.entrarComoCliente();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " foi interrompido.");
                break;
            }
        }
    }
}
