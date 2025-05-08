package ex02;

public class Cliente implements Runnable {
    private final Barbearia barbershop;

    public Cliente(Barbearia barbershop) {
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
