package ex01;

public class Principal {
    public static void main(String[] args) {
        new Thread(new Letra('A')).start();
        new Thread(new Letra('B')).start();
        new Thread(new Letra('C')).start();
    }
}
