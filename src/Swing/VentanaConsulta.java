package Swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class VentanaConsulta extends JFrame {
    private static final String URL = "http://localhost:8080/socios";
    private JButton btnConsultar = new JButton("Consultar socios");
    private JLabel lblEstado = new JLabel("Listo para consultar");
    private JProgressBar barraProgreso = new JProgressBar();
    private JTextArea areaDatos = new JTextArea();

    public VentanaConsulta() {
        super("Consulta al servidor");
        barraProgreso.setIndeterminate(false);
        barraProgreso.setVisible(false);

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.add(btnConsultar);
        panelSuperior.add(lblEstado);
        panelSuperior.add(barraProgreso);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(areaDatos), BorderLayout.CENTER);

        btnConsultar.addActionListener(e -> consultar());
        setSize(520, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void consultar() {
        btnConsultar.setEnabled(false);
        lblEstado.setText("Consultando...");
        areaDatos.setText("");
        barraProgreso.setVisible(true);
        barraProgreso.setIndeterminate(true);

        SwingWorker<String, Void> trabajador = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                HttpClient cliente = HttpClient.newHttpClient();
                HttpRequest peticion = HttpRequest.newBuilder(URI.create(URL)).build();
                HttpResponse<String> respuesta = cliente.send(
                        peticion,
                        HttpResponse.BodyHandlers.ofString()
                );
                return respuesta.body();
            }

            @Override
            protected void done() {
                try {
                    String json = get();
                    areaDatos.setText(json.replace("}, {", "}\n{"));
                    lblEstado.setText("Datos recibidos");
                } catch (InterruptedException | ExecutionException ex) {
                    lblEstado.setText("Error");
                    JOptionPane.showMessageDialog(
                            VentanaConsulta.this,
                            "No se pudo conectar con el servidor",
                            "Error de conexion",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    barraProgreso.setIndeterminate(false);
                    barraProgreso.setVisible(false);
                    btnConsultar.setEnabled(true);
                }
            }
        };

        trabajador.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaConsulta().setVisible(true));
    }
}