import javax.swing.*;

/**
 * Ventana de CineLikes hecha con el Swing UI Designer (Ventana.form).
 * Aquí solo están los eventos de la interfaz: toda la lógica está en la clase Lista.
 */
public class Ventana {
    private JPanel principal;
    private JTabbedPane tabbedPane1;

    // Pestaña CATÁLOGO: listado y registro
    private JList<Pelicula> lstPeliculas;
    private JLabel lblCantidad;
    private JTextField txtCodigo;
    private JTextField txtTitulo;
    private JTextField txtDirector;
    private JComboBox<String> cboGenero;
    private JTextField txtAnio;
    private JButton btnRegistrar;

    // Pestaña RANKING
    private JList<String> lstRanking;
    private JButton btnActualizarRanking;

    private Lista catalogo = new Lista();
    private DefaultListModel<Pelicula> modeloPeliculas = new DefaultListModel<>();
    private DefaultListModel<String> modeloRanking = new DefaultListModel<>();

    public Ventana() {
        catalogo.cargarDatosIniciales();   // R1: las 20 películas se cargan una sola vez
        lstPeliculas.setModel(modeloPeliculas);
        lstRanking.setModel(modeloRanking);

        btnRegistrar.addActionListener(e -> registrar());
        btnActualizarRanking.addActionListener(e -> actualizarRanking());

        actualizarVistas();
    }

    // R2: registra una película nueva; siempre empieza con 0 me gusta.
    private void registrar() {
        try {
            Pelicula nueva = catalogo.registrar(txtCodigo.getText(), txtTitulo.getText(), txtDirector.getText(),
                    (String) cboGenero.getSelectedItem(), txtAnio.getText());
            actualizarVistas();
            limpiarFormulario();
            JOptionPane.showMessageDialog(principal, "Película registrada:\n" + nueva);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(principal, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtTitulo.setText("");
        txtDirector.setText("");
        cboGenero.setSelectedIndex(0);
        txtAnio.setText("");
    }

    // Refresca las vistas con los datos actuales, sin volver a cargar las 20 películas iniciales.
    private void actualizarVistas() {
        actualizarListado();
        actualizarRanking();
    }

    // R1: lista principal (número de mayor a menor) y cantidad actual de películas.
    private void actualizarListado() {
        modeloPeliculas.clear();
        for (Pelicula pelicula : catalogo.getPeliculas()) {
            modeloPeliculas.addElement(pelicula);
        }
        lblCantidad.setText("CANTIDAD DE PELÍCULAS: " + catalogo.getCantidad());
    }

    // R6: ranking por me gusta. Es una copia ordenada, la lista principal no cambia.
    private void actualizarRanking() {
        modeloRanking.clear();
        for (Pelicula pelicula : catalogo.obtenerRanking()) {
            modeloRanking.addElement(pelicula.getNumeroID() + " | " + pelicula.getTitulo() + " | "
                    + pelicula.getMeGusta() + " me gusta");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CineLikes");
        frame.setContentPane(new Ventana().principal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
