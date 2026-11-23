/**
 * Película del catálogo del cineclub.
 * El numeroID no cambia después del registro (es final y no tiene setter).
 * El contador meGusta no tiene setter: solo aumenta con darMeGusta().
 */
public class Pelicula {
    private final int numeroID;
    private String titulo;
    private String director;
    private String genero;
    private int anio;
    private int meGusta;

    // Película nueva (formulario de registro): siempre empieza con 0 me gusta.
    public Pelicula(int numeroID, String titulo, String director, String genero, int anio) {
        this(numeroID, titulo, director, genero, anio, 0);
    }

    // Constructor completo: lo usa DatosIniciales para las 20 películas precargadas.
    public Pelicula(int numeroID, String titulo, String director, String genero, int anio, int meGusta) {
        if (meGusta < 0) {
            throw new IllegalArgumentException("El contador de me gusta no puede ser negativo.");
        }
        this.numeroID = numeroID;
        this.titulo = titulo;
        this.director = director;
        this.genero = genero;
        this.anio = anio;
        this.meGusta = meGusta;
    }

    public int getNumeroID() {
        return numeroID;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getMeGusta() {
        return meGusta;
    }

    // R5: cada clic en "Me gusta +1" suma exactamente uno.
    public void darMeGusta() {
        meGusta++;
    }

    @Override
    public String toString() {
        return numeroID + " | " + titulo + " | " + director + " | " + genero + " | " + anio + " | Me gusta: " + meGusta;
    }
}
