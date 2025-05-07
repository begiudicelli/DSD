package geral;

import java.util.concurrent.Semaphore;

public class DiningPhilosopher implements Resource {
    private final int n;
    private final Semaphore[] fork;
    public DiningPhilosopher(int initN) {
        n = initN;
        fork = new Semaphore[n];
        for (int i = 0; i < n; i++) {
            fork[i] = new Semaphore(1);
        }
    }
    public void acquire(int i) throws InterruptedException {
        fork[i].acquire();
        fork[(i + 1) % n].acquire();
    }
    public void release(int i) {
        fork[i].release();
        fork[(i + 1) % n].release();
    }
    public static void main(String[] args) {
        DiningPhilosopher dp = new DiningPhilosopher(5);
        for (int i = 0; i < 5; i++)
            new Thread(new Philosopher(i, dp)).start();
    }
}
