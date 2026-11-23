import javax.swing.*;
import java.util.ArrayList;

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

    // Pestaña BUSCAR Y EDITAR
    private JTextField txtBuscarTexto;
    private JButton btnBuscarTexto;
    private JTextArea txtResultados;
    private JTextField txtBuscarNumero;
    private JButton btnBuscarNumero;
    private JLabel lblIndice;
    private JLabel lblNumeroSeleccionado;
    private JTextField txtEditTitulo;
    private JTextField txtEditDirector;
    private JComboBox<String> cboEditGenero;
    private JTextField txtEditAnio;
    private JLabel lblMeGusta;
    private JButton btnGuardar;
    private JButton btnMeGusta;
    private JButton btnEliminar;

    // Pestaña RANKING
    private JList<String> lstRanking;
    private JButton btnActualizarRanking;

    private Lista catalogo = new Lista();
    private DefaultListModel<Pelicula> modeloPeliculas = new DefaultListModel<>();
    private DefaultListModel<String> modeloRanking = new DefaultListModel<>();
    private int numeroSeleccionado = 0;   // 0 = no hay película seleccionada
    private String textoBuscado = null;   // null = todavía no se buscó ningún texto

    public Ventana() {
        catalogo.cargarDatosIniciales();   // R1: las 20 películas se cargan una sola vez
        lstPeliculas.setModel(modeloPeliculas);
        lstRanking.setModel(modeloRanking);

        btnRegistrar.addActionListener(e -> registrar());
        btnActualizarRanking.addActionListener(e -> actualizarRanking());
        btnBuscarTexto.addActionListener(e -> buscarTexto());
        btnBuscarNumero.addActionListener(e -> buscarNumero());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnMeGusta.addActionListener(e -> darMeGusta());
        btnEliminar.addActionListener(e -> eliminarPelicula());

        limpiarSeleccion();
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

    // R3: búsqueda lineal; muestra las películas que tienen el texto exacto en el título o el director.
    private void buscarTexto() {
        try {
            ArrayList<Pelicula> resultados = catalogo.buscarPorTexto(txtBuscarTexto.getText());
            textoBuscado = txtBuscarTexto.getText();
            mostrarResultados(resultados);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(principal, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarResultados(ArrayList<Pelicula> resultados) {
        txtResultados.setText("Coincidencias con \"" + textoBuscado + "\": " + resultados.size() + "\n");
        if (resultados.isEmpty()) {
            txtResultados.append("Ninguna película tiene ese texto en el título ni en el director.\n");
        }
        for (Pelicula pelicula : resultados) {
            txtResultados.append(pelicula + "\n");
        }
    }

    // Si ya se buscó un texto, se repite la búsqueda para que los resultados muestren los cambios.
    private void recalcularBusqueda() {
        if (textoBuscado != null) {
            mostrarResultados(catalogo.buscarPorTexto(textoBuscado));
        }
    }

    // R4: búsqueda binaria por número. Primero se limpia la selección anterior,
    // así ningún cambio cae sobre otra película si el número no existe.
    private void buscarNumero() {
        limpiarSeleccion();
        try {
            int numero = catalogo.leerNumero(txtBuscarNumero.getText());
            int indice = catalogo.busquedaBinaria(numero);
            lblIndice.setText("Índice devuelto por la búsqueda binaria: " + indice);
            if (indice == -1) {
                JOptionPane.showMessageDialog(principal, "No existe una película con el número " + numero + ".",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                mostrarSeleccion(catalogo.obtener(indice));
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(principal, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarSeleccion(Pelicula pelicula) {
        numeroSeleccionado = pelicula.getNumeroID();
        lblNumeroSeleccionado.setText("NÚMERO: " + pelicula.getNumeroID());
        txtEditTitulo.setText(pelicula.getTitulo());
        txtEditDirector.setText(pelicula.getDirector());
        cboEditGenero.setSelectedItem(pelicula.getGenero());
        txtEditAnio.setText(String.valueOf(pelicula.getAnio()));
        lblMeGusta.setText("ME GUSTA: " + pelicula.getMeGusta());
    }

    private void limpiarSeleccion() {
        numeroSeleccionado = 0;
        lblNumeroSeleccionado.setText("NÚMERO: (ninguna)");
        txtEditTitulo.setText("");
        txtEditDirector.setText("");
        cboEditGenero.setSelectedIndex(0);
        txtEditAnio.setText("");
        lblMeGusta.setText("ME GUSTA: -");
        lblIndice.setText("Índice devuelto por la búsqueda binaria: -");
    }

    // Sin una película buscada por número no se puede editar, eliminar ni dar me gusta.
    private boolean haySeleccion() {
        if (numeroSeleccionado == 0) {
            JOptionPane.showMessageDialog(principal, "Primero busque una película por su número.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private Pelicula peliculaSeleccionada() {
        return catalogo.obtener(catalogo.busquedaBinaria(numeroSeleccionado));
    }

    // R4: guarda título, director, género y año. Si un dato falla, Lista no cambia nada.
    private void guardarCambios() {
        if (!haySeleccion()) {
            return;
        }
        try {
            catalogo.editar(numeroSeleccionado, txtEditTitulo.getText(), txtEditDirector.getText(),
                    (String) cboEditGenero.getSelectedItem(), txtEditAnio.getText());
            mostrarSeleccion(peliculaSeleccionada());
            actualizarVistas();
            JOptionPane.showMessageDialog(principal, "Se actualizaron los datos de la película " + numeroSeleccionado + ".");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(principal, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // R5: suma un me gusta a la película seleccionada.
    private void darMeGusta() {
        if (!haySeleccion()) {
            return;
        }
        int total = catalogo.darMeGusta(numeroSeleccionado);
        lblMeGusta.setText("ME GUSTA: " + total);
        actualizarVistas();
    }

    // R4: elimina del ArrayList la película seleccionada, después de confirmar.
    private void eliminarPelicula() {
        if (!haySeleccion()) {
            return;
        }
        Pelicula pelicula = peliculaSeleccionada();
        int respuesta = JOptionPane.showConfirmDialog(principal,
                "¿Eliminar la película " + pelicula.getNumeroID() + " - " + pelicula.getTitulo() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        catalogo.eliminar(numeroSeleccionado);
        limpiarSeleccion();
        actualizarVistas();
        JOptionPane.showMessageDialog(principal, "Se eliminó la película " + pelicula.getNumeroID() + " - "
                + pelicula.getTitulo() + ".");
    }

    // Refresca las vistas con los datos actuales, sin volver a cargar las 20 películas iniciales.
    private void actualizarVistas() {
        actualizarListado();
        actualizarRanking();
        recalcularBusqueda();
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
