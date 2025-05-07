package ex02;

public class Barber implements Runnable {
    private final Barbershop barbershop;

    public Barber(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        System.out.println("Barbeiro começou o dia de trabalho.");
        while (true) {
            try {
                barbershop.trabalharComoBarbeiro();
            } catch (InterruptedException e) {
                System.out.println("Barbeiro foi interrompido.");
                break;
            }
        }
    }
}