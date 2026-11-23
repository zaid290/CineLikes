import java.util.ArrayList;

/**
 * Las 20 películas con las que arranca el programa.
 * Van desordenadas a propósito: Lista las ordena por número (de mayor a menor) al cargarlas.
 * Los números no se repiten; géneros, años, directores, me gusta y hasta un título sí se repiten.
 */
public class DatosIniciales {

    public static ArrayList<Pelicula> crearPeliculas() {
        ArrayList<Pelicula> datos = new ArrayList<>();
        datos.add(new Pelicula(2160, "Coco", "Lee Unkrich", "Animación", 2017, 48));
        datos.add(new Pelicula(2100, "El Padrino", "Francis Ford Coppola", "Drama", 1972, 45));
        datos.add(new Pelicula(2250, "Intensa-Mente", "Pete Docter", "Animación", 2015, 35));
        datos.add(new Pelicula(2130, "Bowling for Columbine", "Michael Moore", "Documental", 2002, 9));
        datos.add(new Pelicula(2290, "El señor de los anillos: la comunidad del anillo", "Peter Jackson", "Aventura", 2001, 45));
        datos.add(new Pelicula(2110, "Toy Story", "John Lasseter", "Animación", 1995, 35));
        datos.add(new Pelicula(2200, "Oppenheimer", "Christopher Nolan", "Drama", 2023, 31));
        datos.add(new Pelicula(2270, "Roma", "Alfonso Cuarón", "Drama", 2018, 27));
        datos.add(new Pelicula(2150, "Mujercitas", "Gillian Armstrong", "Drama", 1994, 12));
        datos.add(new Pelicula(2220, "Mi pobre angelito", "Chris Columbus", "Comedia", 1990, 25));
        datos.add(new Pelicula(2180, "El viaje de Chihiro", "Hayao Miyazaki", "Animación", 2001, 40));
        datos.add(new Pelicula(2120, "Tiempos modernos", "Charles Chaplin", "Comedia", 1936, 22));
        datos.add(new Pelicula(2240, "Mujercitas", "Greta Gerwig", "Drama", 2019, 20));
        datos.add(new Pelicula(2140, "Interestelar", "Christopher Nolan", "Aventura", 2014, 40));
        datos.add(new Pelicula(2190, "Buena Vista Social Club", "Wim Wenders", "Documental", 1999, 14));
        datos.add(new Pelicula(2210, "Up", "Pete Docter", "Animación", 2009, 27));
        datos.add(new Pelicula(2280, "Free Solo", "Elizabeth Chai Vasarhelyi y Jimmy Chin", "Documental", 2018, 20));
        datos.add(new Pelicula(2170, "Volver al futuro", "Robert Zemeckis", "Comedia", 1985, 38));
        datos.add(new Pelicula(2260, "El gran dictador", "Charles Chaplin", "Comedia", 1940, 15));
        datos.add(new Pelicula(2230, "Jurassic Park", "Steven Spielberg", "Aventura", 1993, 33));
        return datos;
    }
}
