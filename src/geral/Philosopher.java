package geral;

public class Philosopher implements Runnable {
    private final int id;
    private final Resource r;
    public Philosopher(int initId, Resource initr) {
        id = initId;
        r = initr;
    }
    public void run() {
        while (true) {
            try {
                System.out.println("Phil " + id + " thinking");
                Thread.sleep(30);
                System.out.println("Phil " + id + " hungry");
                r.acquire(id);
                System.out.println("Phil " + id + " eating");
                Thread.sleep(40);
                r.release(id);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
