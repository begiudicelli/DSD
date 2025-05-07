package geral;

public class Attempt1 implements Lock {
    private volatile boolean openDoor = true;
    public void requestCS(int i) {
        while (!openDoor) Thread.onSpinWait();
        openDoor = false;
    }
    public void releaseCS(int i) {
        openDoor = true;
    }
}
