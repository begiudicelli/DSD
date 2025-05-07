package geral;

import java.util.concurrent.Semaphore;

public class ReaderWriter {
    private volatile int numReaders = 0;
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore wlock = new Semaphore(1);
    public void startRead() throws InterruptedException {
        mutex.acquire();
        numReaders++;
        if (numReaders == 1) wlock.acquire();
        mutex.release();
    }
    public void endRead() throws InterruptedException {
        mutex.acquire();
        numReaders--;
        if (numReaders == 0) wlock.release();
        mutex.release();
    }
    public void startWrite() throws InterruptedException {
        wlock.acquire();
    }
    public void endWrite() {
        wlock.release();
    }
}
