package ex05;

public class Escritor implements Runnable {
    private final BancoDeDados bancoDeDados;
    private final int id;
    private final String mensagem;

    public Escritor(BancoDeDados bancoDeDados, int id, String mensagem) {
        this.bancoDeDados = bancoDeDados;
        this.id = id;
        this.mensagem = mensagem;
    }

    public void escrever() throws InterruptedException{
        bancoDeDados.comecarAEscrever();
        System.out.println("Escritor " + id + " escrevendo: " + mensagem);
        Thread.sleep(800);
        bancoDeDados.terminarDeEscrever();
        System.out.println("Escritor " + id + " terminou de escrever.");
        Thread.sleep(1500);
    }

    @Override
    public void run() {
        while (true) {
            try {
               escrever();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
