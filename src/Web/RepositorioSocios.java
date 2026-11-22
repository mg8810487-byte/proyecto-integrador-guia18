package Web;

import java.util.List;

public class RepositorioSocios {
    private List<Socio> socios = List.of(
            new Socio(1, "Ana", 175),
            new Socio(2, "Luis", 250),
            new Socio(3, "Marta", 80)
    );

    public String listarJson() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < socios.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(socios.get(i).toJson());
        }
        return sb.append("]").toString();
    }

    public int saldoTotal() {
        int total = 0;
        for (Socio s : socios) {
            total += s.getSaldo();
        }
        return total;
    }

    public Socio buscar(String idTexto) throws SocioNoEncontradoException {
        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("El id debe ser un numero: " + idTexto);
        }
        for (Socio s : socios) {
            if (s.getId() == id) {
                return s;
            }
        }
        throw new SocioNoEncontradoException(id);
    }
}
