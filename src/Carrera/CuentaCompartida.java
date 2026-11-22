package Carrera;

public class CuentaCompartida {
    private int saldo;

    public CuentaCompartida(int saldo) {
        this.saldo = saldo;
    }

    public void retirar(String cajero, int monto) throws SaldoInsuficienteException {
        if (monto > saldo) {
            throw new SaldoInsuficienteException(
                    "Saldo insuficiente para retirar " + monto,
                    monto - saldo
            );
        }
        pausa();
        saldo -= monto;
        System.out.println(cajero + " retiro " + monto + " -> saldo " + saldo);
    }

    private void pausa() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getSaldo() {
        return saldo;
    }

    public static void main(String[] args) throws InterruptedException {
        CuentaCompartida cuenta = new CuentaCompartida(100);
        Runnable cajero = () -> {
            String nombre = Thread.currentThread().getName();
            try {
                cuenta.retirar(nombre, 80);
            } catch (SaldoInsuficienteException e) {
                System.out.println(nombre + ": " + e.getMessage() + " (faltan $" + e.getFaltante() + ")");
            }
        };

        Thread a = new Thread(cajero, "Cajero A");
        Thread b = new Thread(cajero, "Cajero B");
        a.start();
        b.start();
        a.join();
        b.join();
        System.out.println("Saldo final: " + cuenta.getSaldo());
    }
}