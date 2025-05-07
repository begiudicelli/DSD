package ex01;

import geral.Util;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Letra implements Runnable {

    private final char letter;
    private static final Random r = new Random();

    private static final Semaphore mutex = new Semaphore(1);
    private static final Semaphore podeC = new Semaphore(0);

    private static int numC = 0;
    private static int numAB = 0;

    public Letra(char letter) {
        this.letter = letter;
    }

    private void printLetter() throws InterruptedException {
        if (letter == 'A' || letter == 'B') {
            mutex.acquire();
            numAB++;
            System.out.printf("%c\t(A + B = %d, C = %d)\n", letter, numAB, numC);
            podeC.release();
            mutex.release();
        }
        else {
            podeC.acquire();
            mutex.acquire();
            numC++;
            System.out.printf("%c\t(A + B = %d, C = %d)\n", letter, numAB, numC);
            mutex.release();
        }
        Util.mySleep(r.nextInt(1000));
    }

    public void run() {
        while (true) {
            try {
                printLetter();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
