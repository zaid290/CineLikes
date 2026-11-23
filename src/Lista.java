import java.time.Year;
import java.util.ArrayList;

/**
 * Lógica del catálogo de películas. No usa nada de Swing: la Ventana solo llama a estos métodos.
 * La colección principal siempre queda ordenada por numeroID de MAYOR a MENOR,
 * porque la búsqueda binaria solo funciona sobre una lista ordenada.
 */
public class Lista {
    // Géneros permitidos. Deben ser iguales a los ítems de los JComboBox de Ventana.form.
    public static final String[] GENEROS = {"Drama", "Aventura", "Comedia", "Animación", "Documental"};
    public static final int ANIO_MINIMO = 1930;

    private ArrayList<Pelicula> peliculas;
    private boolean datosCargados;   // evita cargar las 20 películas más de una vez

    public Lista() {
        peliculas = new ArrayList<>();
        datosCargados = false;
    }

    // ===================== R1: carga inicial =====================

    // Carga las 20 películas UNA sola vez por ejecución.
    // No acepta números repetidos; género, año y me gusta sí pueden repetirse.
    public void cargarDatosIniciales() {
        if (datosCargados) {
            return;
        }
        ArrayList<Pelicula> iniciales = DatosIniciales.crearPeliculas();
        for (Pelicula pelicula : iniciales) {
            if (existeNumero(pelicula.getNumeroID())) {
                System.out.println("No se cargó la película " + pelicula.getNumeroID() + ": número repetido.");
            } else {
                peliculas.add(pelicula);
                ordenarPorNumeroDescendente();
            }
        }
        datosCargados = true;
    }

    // Devuelve una copia: así la ventana puede recorrerla sin desordenar la lista principal.
    public ArrayList<Pelicula> getPeliculas() {
        return new ArrayList<>(peliculas);
    }

    public int getCantidad() {
        return peliculas.size();
    }

    public Pelicula obtener(int indice) {
        return peliculas.get(indice);
    }

    // ===================== Ordenamiento por inserción =====================

    // Deja la lista principal ordenada por numeroID de mayor a menor.
    // Se llama después de cada registro, porque un código nuevo puede ser menor que los existentes.
    public void ordenarPorNumeroDescendente() {
        for (int i = 1; i < peliculas.size(); i++) {
            Pelicula actual = peliculas.get(i);
            int j = i - 1;
            // Se corren a la derecha las películas con número MENOR que el actual.
            while (j >= 0 && peliculas.get(j).getNumeroID() < actual.getNumeroID()) {
                peliculas.set(j + 1, peliculas.get(j));
                j--;
            }
            peliculas.set(j + 1, actual);
        }
    }

    // ===================== R2: registro =====================

    // Valida TODOS los datos antes de crear la película. Si algo falla lanza la excepción
    // con todos los errores juntos y no se agrega nada (no hay registros a medias).
    public Pelicula registrar(String textoNumero, String titulo, String director, String genero, String textoAnio) {
        String errores = "";
        int numero = 0;
        try {
            numero = Integer.parseInt(limpiar(textoNumero));
            if (numero <= 0) {
                errores += "- El código debe ser un número entero positivo.\n";
            } else if (existeNumero(numero)) {
                errores += "- Ya existe una película con el código " + numero + ".\n";
            }
        } catch (NumberFormatException e) {
            errores += "- El código es obligatorio y debe ser un número entero positivo.\n";
        }
        errores += validarDatosEditables(titulo, director, genero, textoAnio);

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException("No se registró la película. Revise:\n" + errores);
        }
        Pelicula nueva = new Pelicula(numero, limpiar(titulo), limpiar(director), genero, Integer.parseInt(limpiar(textoAnio)));
        peliculas.add(nueva);
        ordenarPorNumeroDescendente();
        return nueva;
    }

    // Validaciones que comparten registrar y editar: título, director, género y año.
    // Devuelve el texto con los errores, o un texto vacío si todo está bien.
    private String validarDatosEditables(String titulo, String director, String genero, String textoAnio) {
        String errores = "";
        if (limpiar(titulo).isEmpty()) {
            errores += "- El título es obligatorio.\n";
        }
        if (limpiar(director).isEmpty()) {
            errores += "- El director es obligatorio.\n";
        }
        if (!esGeneroValido(genero)) {
            errores += "- Seleccione un género de la lista.\n";
        }
        int anioActual = Year.now().getValue();
        try {
            int anio = Integer.parseInt(limpiar(textoAnio));
            if (anio < ANIO_MINIMO || anio > anioActual) {
                errores += "- El año debe estar entre " + ANIO_MINIMO + " y " + anioActual + ".\n";
            }
        } catch (NumberFormatException e) {
            errores += "- El año es obligatorio y debe ser un número entre " + ANIO_MINIMO + " y " + anioActual + ".\n";
        }
        return errores;
    }

    private boolean esGeneroValido(String genero) {
        for (String permitido : GENEROS) {
            if (permitido.equals(genero)) {
                return true;
            }
        }
        return false;
    }

    // ===================== R3: búsqueda lineal =====================

    // Recorre toda la lista y devuelve las películas cuyo título o director CONTIENE el texto exacto.
    // contains distingue mayúsculas: "Nolan" encuentra a "Christopher Nolan", pero "nolan" no.
    public ArrayList<Pelicula> buscarPorTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Escriba un texto para buscar: no puede estar vacío ni tener solo espacios.");
        }
        ArrayList<Pelicula> resultados = new ArrayList<>();
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getTitulo().contains(texto) || pelicula.getDirector().contains(texto)) {
                resultados.add(pelicula);
            }
        }
        return resultados;
    }

    // ===================== R4: búsqueda binaria =====================

    // Búsqueda binaria propia sobre la lista principal, que está ordenada de MAYOR a MENOR.
    // Devuelve el índice de la película o -1 si no existe. Rechaza números no positivos.
    public int busquedaBinaria(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número debe ser un entero positivo.");
        }
        int inicio = 0;
        int fin = peliculas.size() - 1;
        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            int numeroMedio = peliculas.get(medio).getNumeroID();
            if (numeroMedio == numero) {
                return medio;
            } else if (numeroMedio > numero) {
                inicio = medio + 1;   // orden descendente: los números menores están a la derecha
            } else {
                fin = medio - 1;      // y los mayores, a la izquierda
            }
        }
        return -1;
    }

    public boolean existeNumero(int numero) {
        return busquedaBinaria(numero) != -1;
    }

    // Convierte el texto de la caja de búsqueda en un número. Rechaza letras, 0 y negativos.
    public int leerNumero(String texto) {
        int numero;
        try {
            numero = Integer.parseInt(limpiar(texto));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Escriba un número entero positivo (solo dígitos).");
        }
        if (numero <= 0) {
            throw new IllegalArgumentException("El número debe ser positivo: " + numero + " no es válido.");
        }
        return numero;
    }

    // ===================== R4: editar y eliminar =====================

    // Primero valida TODO; si un dato falla no se cambia ningún atributo.
    // El numeroID y el contador de me gusta no se pueden editar.
    public void editar(int numero, String titulo, String director, String genero, String textoAnio) {
        int indice = busquedaBinaria(numero);
        if (indice == -1) {
            throw new IllegalArgumentException("No existe una película con el número " + numero + ".");
        }
        String errores = validarDatosEditables(titulo, director, genero, textoAnio);
        if (!errores.isEmpty()) {
            throw new IllegalArgumentException("No se guardó ningún cambio. Revise:\n" + errores);
        }
        Pelicula pelicula = peliculas.get(indice);
        pelicula.setTitulo(limpiar(titulo));
        pelicula.setDirector(limpiar(director));
        pelicula.setGenero(genero);
        pelicula.setAnio(Integer.parseInt(limpiar(textoAnio)));
    }

    // Quita la película de la lista principal. Quitar un elemento no desordena la lista.
    public Pelicula eliminar(int numero) {
        int indice = busquedaBinaria(numero);
        if (indice == -1) {
            throw new IllegalArgumentException("No existe una película con el número " + numero + ".");
        }
        return peliculas.remove(indice);
    }

    // ===================== R5: me gusta =====================

    // Suma exactamente un me gusta a la película con ese número y devuelve el contador nuevo.
    public int darMeGusta(int numero) {
        int indice = busquedaBinaria(numero);
        if (indice == -1) {
            throw new IllegalArgumentException("No existe una película con el número " + numero + ".");
        }
        Pelicula pelicula = peliculas.get(indice);
        pelicula.darMeGusta();
        return pelicula.getMeGusta();
    }

    // ===================== R6: ranking =====================

    // Devuelve una COPIA ordenada por me gusta de mayor a menor (también por inserción).
    // La lista principal no se toca, así la búsqueda binaria sigue funcionando.
    // Con empates se conservan todas las películas, en el orden de la lista principal.
    public ArrayList<Pelicula> obtenerRanking() {
        ArrayList<Pelicula> ranking = new ArrayList<>(peliculas);
        for (int i = 1; i < ranking.size(); i++) {
            Pelicula actual = ranking.get(i);
            int j = i - 1;
            while (j >= 0 && ranking.get(j).getMeGusta() < actual.getMeGusta()) {
                ranking.set(j + 1, ranking.get(j));
                j--;
            }
            ranking.set(j + 1, actual);
        }
        return ranking;
    }

    // Quita los espacios de los extremos; si el texto es null devuelve "".
    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }
}
