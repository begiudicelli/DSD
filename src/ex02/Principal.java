package ex02;

public class Principal {
    public static void main(String[] args) {
        Barbershop barbershop = new Barbershop(3);
        new Thread(new Barber(barbershop), "Barbeiro").start();


        for (int i = 1; i <= 5; i++) {
            new Thread(new Customer(barbershop), "Cliente " + i).start();
        }
    }
}