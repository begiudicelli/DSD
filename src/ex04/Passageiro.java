package ex04;

public class Passageiro implements Runnable {
    private final Carro carro;
    private final int id;

    public Passageiro(Carro carro, int id) {
        this.carro = carro;
        this.id = id;
    }

    public void embarcar() throws InterruptedException {
        carro.getPodeEmbarcar().acquire();
        System.out.println("Passageiro " + id + " embarcou.");

        carro.getMutex().acquire();
        carro.incrementaPassageirosEmbarcados();
        if (carro.getPassageirosEmbarcados() == carro.getAssentos()) {
            carro.getPodeAndar().release();
        }
        carro.getMutex().release();
    }

    public void desembarcar() throws InterruptedException {
        carro.getPodeDesembarcar().acquire();
        System.out.println("Passageiro " + id + " desembarcou.");

        carro.getMutex().acquire();
        carro.incrementaPassageirosDesembarcados();
        if (carro.getPassageirosDesembarcados() == carro.getAssentos()) {
            carro.getPodeRecomecar().release();
        }
        carro.getMutex().release();
    }

    @Override
    public void run() {
        try {
            while (true) {
                embarcar();
                desembarcar();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
