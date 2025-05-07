package ex04;

import java.util.concurrent.Semaphore;

public class Carro implements Runnable {
    private final int assentos;
    private int passageirosEmbarcados = 0;
    private int passageirosDesembarcados = 0;

    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore podeEmbarcar = new Semaphore(0);
    private final Semaphore podeAndar = new Semaphore(0);
    private final Semaphore podeDesembarcar = new Semaphore(0);
    private final Semaphore podeRecomecar = new Semaphore(0);

    public Carro(int assentos) {
        this.assentos = assentos;
    }

    public void carregar() throws InterruptedException {
        passageirosEmbarcados = 0;
        System.out.println("Carro liberando embarque.");
        podeEmbarcar.release(assentos);
        podeAndar.acquire();
    }

    public void andar() throws InterruptedException {
        System.out.println("Carro comecou a andar...");
        Thread.sleep(1000);
        System.out.println("Carro terminou de andar!");
    }

    public void descarregar() throws InterruptedException {
        passageirosDesembarcados = 0;
        System.out.println("Carro liberando desembarque.");
        podeDesembarcar.release(assentos);
        podeRecomecar.acquire();
        System.out.println("Todos os passageiros desembarcaram.");
    }

    @Override
    public void run() {
        try {
            while (true) {
                carregar();
                andar();
                descarregar();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public Semaphore getPodeEmbarcar() {
        return podeEmbarcar;
    }

    public Semaphore getPodeDesembarcar() {
        return podeDesembarcar;
    }

    public Semaphore getPodeAndar(){
        return podeAndar;
    }

    public Semaphore getPodeRecomecar(){
        return podeRecomecar;
    }

    public Semaphore getMutex(){
        return mutex;
    }

    public int getPassageirosEmbarcados(){
        return passageirosEmbarcados;
    }

    public int getPassageirosDesembarcados(){
        return passageirosDesembarcados;
    }

    public int getAssentos(){
        return assentos;
    }

    public void incrementaPassageirosEmbarcados(){
        passageirosEmbarcados++;
    }

    public void incrementaPassageirosDesembarcados(){
        passageirosDesembarcados++;
    }

}
