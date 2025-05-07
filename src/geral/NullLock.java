package geral;

/**
 * Implementa��o vazia de Lock.
 * N�o fornece exclus�o m�tua.
 */
public class NullLock implements Lock {
    @Override
    public void releaseCS(int pid) {
    }

    @Override
    public void requestCS(int pid) {
    }
}
