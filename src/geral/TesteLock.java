package geral;

import java.util.Random;
public class TesteLock extends Thread {
    private final int myId;
    private final Lock lock;
    private final Random r = new Random();
    public TesteLock(int id, Lock lock) {
        myId = id;
        this.lock = lock;
    }
    private void nonCriticalSection() {
        System.out.println(myId + " nao esta na cs");
        Util.mySleep(r.nextInt(1000));
    }
    private void CriticalSection() {
        System.out.println(myId + " entrou na CS *****");
        // critical section code
        Util.mySleep(r.nextInt(1000));
        System.out.println(myId + " saiu da CS *****");
    }
    public void run() {
        while (true) {
            lock.requestCS(myId);
            CriticalSection();
            lock.releaseCS(myId);
            nonCriticalSection();
        }
    }
    public static void main(String[] args) throws Exception {
        TesteLock[] t;
        int N = 2;
        t = new TesteLock[N];
        Lock lock = new PetersonLock();
        for (int i = 0; i < N; i++) {
            t[i] = new TesteLock(i, lock);
            t[i].start();
        }
    }
}
