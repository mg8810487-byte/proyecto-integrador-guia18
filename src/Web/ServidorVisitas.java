package Web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorVisitas {
    private static int visitas = 0;
    private static AtomicInteger visitasSeguras = new AtomicInteger(0);
    private static RepositorioSocios repo = new RepositorioSocios();

    private static synchronized void contarVisita() {
        visitas++;
    }

    static void responder(HttpExchange ex, int codigo, String tipo, String cuerpo) throws IOException {
        byte[] datos = cuerpo.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", tipo + "; charset=utf-8");
        ex.sendResponseHeaders(codigo, datos.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(datos);
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);

        servidor.createContext("/visita", ex -> {
            contarVisita();
            responder(ex, 200, "application/json", "{\"ok\": true}");
        });

        servidor.createContext("/visita-segura", ex -> {
            visitasSeguras.incrementAndGet();
            responder(ex, 200, "application/json", "{\"ok\": true}");
        });

        servidor.createContext("/conteo", ex ->
                responder(ex, 200, "application/json",
                        "{\"visitas\": " + visitas + ", \"visitas_seguras\": " + visitasSeguras.get() + "}")
        );

        servidor.createContext("/socios", ex -> {
            try {
                Thread.sleep(1500);
                responder(ex, 200, "application/json", repo.listarJson());
            } catch (InterruptedException e) {
                return;
            }
        });

        servidor.createContext("/", ex ->
                responder(ex, 200, "text/html", Files.readString(Path.of("index.html")))
        );

        servidor.setExecutor(Executors.newFixedThreadPool(8));
        servidor.start();
        System.out.println("Servidor en http://localhost:8080");
    }
}
