package geral;

import java.util.concurrent.Semaphore;

public class BoundedBuffer {
    private static final int SIZE = 10;
    private volatile double[] buffer = new double[SIZE];
    private volatile int inBuf = 0, outBuf = 0;
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore isEmpty = new Semaphore(0);
    private final Semaphore isFull = new Semaphore(SIZE);

    public void deposit(double value) throws InterruptedException {
			isFull.acquire(); // wait if buffer is full
            mutex.acquire(); // ensures mutual exclusion
            buffer[inBuf] = value; // update the buffer
            inBuf = (inBuf + 1) % SIZE;
            mutex.release();
            isEmpty.release();  // notify any waiting consumer
    }
    public double fetch() throws InterruptedException {
        double value;
        isEmpty.acquire(); // wait if buffer is empty
        mutex.acquire();  // ensures mutual exclusion
        value = buffer[outBuf]; //read from buffer
        outBuf = (outBuf + 1) % SIZE;
        mutex.release();
        isFull.release(); // notify any waiting producer
        return value;
    }
}

