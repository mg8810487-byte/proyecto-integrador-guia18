package Carrera;

public class ContadorInseguro {
    private int valor = 0;

    public void incrementar() {
        valor++;
    }

    public int getValor() {
        return valor;
    }

    public static void main(String[] args) throws InterruptedException {
        ContadorInseguro c = new ContadorInseguro();
        Runnable tarea = () -> {
            for (int i = 0; i < 100000; i++) {
                c.incrementar();
            }
        };

        Thread t1 = new Thread(tarea);
        Thread t2 = new Thread(tarea);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Esperado: 200000 Obtenido: " + c.getValor());
    }
}