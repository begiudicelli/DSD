package ex05;

public class Principal{
    public static void main(String[] args) {
        BancoDeDados bd = new BancoDeDados();

        for (int i = 1; i <= 3; i++) {
            new Thread(new Leitor(bd, i, "Consulta de dados")).start();
        }

        for (int i = 1; i <= 2; i++) {
            new Thread(new Escritor(bd, i, "Atualização de registros")).start();
        }
    }
}
