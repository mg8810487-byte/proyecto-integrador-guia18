package Web;

public class Socio {
    private int id;
    private String nombre;
    private int saldo;

    public Socio(int id, String nombre, int saldo) {
        this.id = id;
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public int getSaldo() {
        return saldo;
    }

    public String toJson() {
        return "{\"id\": " + id + ", \"nombre\": \"" + nombre + "\", \"saldo\": " + saldo + "}";
    }
}