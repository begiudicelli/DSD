package ex05;

public class Leitor implements Runnable {
    private final BancoDeDados bancoDeDados;
    private final int id;
    private final String mensagem;

    public Leitor(BancoDeDados bancoDeDados, int id, String mensagem) {
        this.bancoDeDados = bancoDeDados;
        this.id = id;
        this.mensagem = mensagem;
    }

    public void ler() throws InterruptedException{
        bancoDeDados.comecarALer();
        System.out.println("Leitor " + id + " lendo: " + mensagem);
        Thread.sleep(500);
        bancoDeDados.terminarDeLer();
        System.out.println("Leitor " + id + " terminou de ler.");
        Thread.sleep(1000);
    }

    @Override
    public void run() {
        while (true) {
            try {
                ler();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
