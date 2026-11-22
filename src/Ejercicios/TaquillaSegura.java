package Ejercicios;

public class TaquillaSegura {
    private int disponibles = 3;

    public synchronized boolean comprar(String cliente) {
        if (disponibles > 0) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                return false;
            }
            disponibles--;
            System.out.println(cliente + " compro. Quedan " + disponibles);
            return true;
        }
        System.out.println(cliente + ": agotado");
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        TaquillaSegura t = new TaquillaSegura();
        Thread[] clientes = new Thread[5];
        for (int i = 0; i < 5; i++) {
            String nombre = "Cliente " + (i + 1);
            clientes[i] = new Thread(() -> t.comprar(nombre));
            clientes[i].start();
        }
        for (Thread c : clientes) c.join();
        System.out.println("Disponibles al final: " + t.disponibles);
    }
}