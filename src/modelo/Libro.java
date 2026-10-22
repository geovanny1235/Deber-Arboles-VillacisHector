package modelo;

/** Datos de un libro; su código identificador nunca se puede cambiar. */
public class Libro {
    private final int codigo;
    private String titulo;
    private String autor;
    private int anio;

    public Libro(int codigo, String titulo, String autor, int anio) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser un entero positivo.");
        }
        validarDatos(titulo, autor, anio);
        this.codigo = codigo;
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.anio = anio;
    }

    private static void validarDatos(String titulo, String autor, int anio) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        if (anio < 1900 || anio > 2026) {
            throw new IllegalArgumentException("El año debe estar entre 1900 y 2026.");
        }
    }

    /** Valida todo primero y luego cambia los tres datos; código permanece fijo. */
    public void actualizar(String titulo, String autor, int anio) {
        validarDatos(titulo, autor, anio);
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.anio = anio;
    }

    public int getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnio() { return anio; }

    public void setTitulo(String titulo) { actualizar(titulo, autor, anio); }
    public void setAutor(String autor) { actualizar(titulo, autor, anio); }
    public void setAnio(int anio) { actualizar(titulo, autor, anio); }

    @Override
    public String toString() {
        return codigo + " | " + titulo + " | " + autor + " | " + anio;
    }
}
