package Carrera;

import java.util.concurrent.atomic.AtomicInteger;

public class ContadorAtomico {
    private AtomicInteger valor = new AtomicInteger(0);

    public void incrementar() {
        valor.incrementAndGet();
    }

    public int getValor() {
        return valor.get();
    }

    public static void main(String[] args) throws InterruptedException {
        ContadorAtomico c = new ContadorAtomico();
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
