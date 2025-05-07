package ex02;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Barbershop {
    private static final int TEMPO_CORTE = 3000;
    private static final int TEMPO_NOVO_CORTE = 9000;

    private final Semaphore mutex;
    private final Semaphore clientesEsperando;
    private final Semaphore barbeiroDisponivel;

    private int cadeirasLivres;

    private final Random r = new Random();

    public Barbershop(int numeroCadeiras) {
        this.cadeirasLivres = numeroCadeiras;
        this.mutex = new Semaphore(1);
        this.clientesEsperando = new Semaphore(0);
        this.barbeiroDisponivel = new Semaphore(0);
    }

    public void entrarComoCliente() throws InterruptedException {
        mutex.acquire();
        if (cadeirasLivres > 0) {
            cadeirasLivres--;
            System.out.println(Thread.currentThread().getName() + " sentou. Cadeiras livres: " + cadeirasLivres);
            clientesEsperando.release();
            mutex.release();
            barbeiroDisponivel.acquire();
            System.out.println(Thread.currentThread().getName() + " está cortando o cabelo.");
        }
        else {
            System.out.println(Thread.currentThread().getName() + " não encontrou cadeira e foi embora.");
            mutex.release();
        }
        Thread.sleep(r.nextInt(TEMPO_NOVO_CORTE));
    }

    public void trabalharComoBarbeiro() throws InterruptedException {
        clientesEsperando.acquire();
        mutex.acquire();
        cadeirasLivres++;
        System.out.println("Barbeiro começou a cortar cabelo. Cadeiras livres: " + cadeirasLivres);
        barbeiroDisponivel.release();
        mutex.release();
        Thread.sleep(TEMPO_CORTE);
        System.out.println("Barbeiro terminou de cortar cabelo.");
    }
}