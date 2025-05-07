package ex01;

import java.util.concurrent.Semaphore;

public class ControleImpressao implements Runnable{

    private static final Semaphore mutex = new Semaphore(1);
    private static final Semaphore podeC = new Semaphore(0);

    private static int numA = 0;
    private static int numB = 0;
    private static int numC = 0;


    public void imprimirA() throws InterruptedException{
        mutex.acquire();
        numA++;
        System.out.printf("(A = %d, B = %d, C = %d)\n", numA, numB, numC);
        podeC.release();
        mutex.release();
        Thread.sleep(1000);
    }

    public void imprimirB() throws InterruptedException{
        mutex.acquire();
        numB++;
        System.out.printf("(A = %d, B = %d, C = %d)\n", numA, numB, numC);
        podeC.release();
        mutex.release();
        Thread.sleep(1000);
    }

    public void imprimirC() throws InterruptedException{
        podeC.acquire();
        mutex.acquire();
        numC++;
        System.out.printf("(A = %d, B = %d, C = %d)\n", numA, numB, numC);
        mutex.release();
        Thread.sleep(1000);
    }
    @Override
    public void run() {
        while (true) {
            try {
                imprimirA();
                imprimirB();
                imprimirC();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
