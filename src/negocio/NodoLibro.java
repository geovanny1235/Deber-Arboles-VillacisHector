package negocio;

import modelo.Libro;

/** Nodo con hasta dos hijos para un libro del ABB. */
public class NodoLibro {
    private Libro dato;
    private NodoLibro izquierdo;
    private NodoLibro derecho;

    public NodoLibro(Libro dato) {
        if (dato == null) throw new IllegalArgumentException("Libro obligatorio.");
        this.dato = dato;
    }

    public Libro getDato() { return dato; }
    public void setDato(Libro dato) {
        if (dato == null) throw new IllegalArgumentException("Libro obligatorio.");
        this.dato = dato;
    }
    public NodoLibro getIzquierdo() { return izquierdo; }
    public void setIzquierdo(NodoLibro izquierdo) { this.izquierdo = izquierdo; }
    public NodoLibro getDerecho() { return derecho; }
    public void setDerecho(NodoLibro derecho) { this.derecho = derecho; }
}
