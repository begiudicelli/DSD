package ex03;

import java.util.concurrent.Semaphore;

public class ContadorLimitado {
    private final int minimo;
    private final int maximo;
    private int valorAtual;

    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore podeIncrementar = new Semaphore(0);
    private final Semaphore podeDecrementar = new Semaphore(0);

    public ContadorLimitado(int minimo, int maximo, int valorAtual) {
        this.minimo = minimo;
        this.maximo = maximo;
        this.valorAtual = valorAtual;
    }

    public void incrementa() throws InterruptedException {
        while (true) {
            mutex.acquire();
            if (valorAtual < maximo) {
                valorAtual++;
                System.out.println(Thread.currentThread().getName() + " incrementou: " + valorAtual);
                mutex.release();
                podeDecrementar.release();
                break;
            } else {
                mutex.release();
                podeIncrementar.acquire();
            }
        }
    }

    public void decrementa() throws InterruptedException {
        while (true) {
            mutex.acquire();
            if (valorAtual > minimo) {
                valorAtual--;
                System.out.println(Thread.currentThread().getName() + " decrementou: " + valorAtual);
                mutex.release();
                podeIncrementar.release();
                break;
            } else {
                mutex.release();
                podeDecrementar.acquire();
            }
        }
    }

    public int getValorAtual() {
        return valorAtual;
    }
}
