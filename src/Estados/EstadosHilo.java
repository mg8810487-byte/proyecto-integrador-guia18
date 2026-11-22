package Estados;

public class EstadosHilo {
    private static final Object candado = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread dormilon = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }
        });

        System.out.println("1. Recien creado: " + dormilon.getState());
        dormilon.start();
        System.out.println("2. Tras start(): " + dormilon.getState());
        Thread.sleep(100);
        System.out.println("3. Durante sleep: " + dormilon.getState());

        Thread esperador = new Thread(() -> {
            try {
                dormilon.join();
            } catch (InterruptedException e) {
                return;
            }
        });
        esperador.start();
        Thread.sleep(100);
        System.out.println("4. Esperando join: " + esperador.getState());

        Thread bloqueado = new Thread(() -> {
            synchronized (candado) {
                System.out.println("(bloqueado obtuvo el candado)");
            }
        });

        synchronized (candado) {
            bloqueado.start();
            Thread.sleep(100);
            System.out.println("5. Esperando candado: " + bloqueado.getState());
        }

        bloqueado.join();
        dormilon.join();
        esperador.join();
        System.out.println("6. Al terminar: " + dormilon.getState());
    }
}