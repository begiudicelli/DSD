package geral;

import java.util.Random;

public class ProducerConsumer {
    public static void main(String[] args) {
        BoundedBuffer buffer = new BoundedBuffer();
        //BoundedBufferMonitor buffer = new BoundedBufferMonitor();
        Producer producer = new Producer(buffer);
        new Thread(producer).start();
        Consumer consumer = new Consumer(buffer);
        new Thread(consumer).start();
    }
}

class Producer implements Runnable {
    private final BoundedBuffer b;
    //private final BoundedBufferMonitor b;
    public Producer(BoundedBuffer initb) {
    //public Producer(BoundedBufferMonitor initb) {
        b = initb;
    }
    public void run() {
        double item;
        Random r = new Random();
        while (true) {
            item = r.nextDouble();
            System.out.println("produced item " + item);
            try {
				b.deposit(item);
			} catch (InterruptedException e) {}
            Util.mySleep(200);
        }
    }
}

class Consumer implements Runnable {
    private final BoundedBuffer b;
    //private final BoundedBufferMonitor;
    public Consumer(BoundedBuffer initb) {
    //public Consumer(BoundedBufferMonitor initb) {
        b = initb;
    }
    public void run() {
        double item;
        while (true) {
            try {
				item = b.fetch();
	            System.out.println("fetched item " + item);
			} catch (InterruptedException e) {}
            Util.mySleep(50);
        }
    }
}
