package ex05;

import java.util.concurrent.Semaphore;

public class BancoDeDados {
    private int leitores = 0;
    private int escritoresEsperando = 0;

    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore leitura = new Semaphore(1);
    private final Semaphore escrita = new Semaphore(1);

    public void comecarALer() throws InterruptedException {
        leitura.acquire();
        mutex.acquire();
        if (escritoresEsperando > 0) System.err.println("[ERROR] Leitor tentando ler enquanto há escritores esperando.");
        leitores++;
        if (leitores == 1) escrita.acquire();
        mutex.release();
        leitura.release();
        System.out.println("[DEBUG] Leitores ativos: " + leitores);
    }

    public void terminarDeLer() throws InterruptedException {
        mutex.acquire();
        leitores--;
        if (leitores == 0) escrita.release();
        mutex.release();
        System.out.println("[DEBUG] Leitores ativos após término: " + leitores);
    }
    public void comecarAEscrever() throws InterruptedException {
        mutex.acquire();
        escritoresEsperando++;
        if (escritoresEsperando == 1) leitura.acquire();  // Bloqueia a leitura se for o primeiro escritor
        mutex.release();
        escrita.acquire();
        if (leitores > 0) System.err.println("[ERROR] Escritor tentando escrever enquanto há leitores ativos. Leitores ativos: " + leitores);
        System.out.println("[DEBUG] Iniciando escrita. Esperando escritores: " + escritoresEsperando);
    }

    public void terminarDeEscrever() throws InterruptedException {
        escrita.release();
        mutex.acquire();
        escritoresEsperando--;
        if (escritoresEsperando == 0) leitura.release();
        mutex.release();
        System.out.println("[DEBUG] Escritores esperando após término: " + escritoresEsperando);
    }
}
