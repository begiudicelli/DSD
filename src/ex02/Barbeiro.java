package ex02;

public class Barbeiro implements Runnable {
    private final Barbearia barbershop;

    public Barbeiro(Barbearia barbershop) {
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