package ex02;

public class Principal {
    public static void main(String[] args) {
        Barbearia barbershop = new Barbearia(3);
        new Thread(new Barbeiro(barbershop), "Barbeiro").start();


        for (int i = 1; i <= 5; i++) {
            new Thread(new Cliente(barbershop), "Cliente " + i).start();
        }
    }
}