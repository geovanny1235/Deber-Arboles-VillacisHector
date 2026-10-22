package negocio;

import enums.TipoRecorrido;
import java.util.ArrayList;
import java.util.List;
import modelo.Libro;

/** ABB ordenado exclusivamente por el código entero de cada libro. */
public class CatalogoABB {
    private NodoLibro raiz;

    public NodoLibro getRaiz() { return raiz; }
    public void setRaiz(NodoLibro raiz) { this.raiz = raiz; }

    public boolean insertar(Libro libro) {
        if (libro == null) throw new IllegalArgumentException("Libro obligatorio.");
        if (buscar(libro.getCodigo()) != null) return false;
        raiz = insertarRec(raiz, libro);
        return true;
    }

    private NodoLibro insertarRec(NodoLibro actual, Libro libro) {
        if (actual == null) return new NodoLibro(libro);
        if (libro.getCodigo() < actual.getDato().getCodigo()) {
            actual.setIzquierdo(insertarRec(actual.getIzquierdo(), libro));
        } else {
            actual.setDerecho(insertarRec(actual.getDerecho(), libro));
        }
        return actual;
    }

    public Libro buscar(int codigo) {
        NodoLibro encontrado = buscarRec(raiz, codigo);
        return encontrado == null ? null : encontrado.getDato();
    }

    private NodoLibro buscarRec(NodoLibro actual, int codigo) {
        if (actual == null) return null;
        if (codigo == actual.getDato().getCodigo()) return actual;
        return codigo < actual.getDato().getCodigo()
                ? buscarRec(actual.getIzquierdo(), codigo)
                : buscarRec(actual.getDerecho(), codigo);
    }

    public int contar() { return contarRec(raiz); }

    private int contarRec(NodoLibro actual) {
        if (actual == null) return 0;
        return 1 + contarRec(actual.getIzquierdo()) + contarRec(actual.getDerecho());
    }

    public List<Libro> recorrer(TipoRecorrido tipo) {
        if (tipo == null) throw new IllegalArgumentException("Recorrido obligatorio.");
        List<Libro> libros = new ArrayList<>();
        recorrerRec(raiz, tipo, libros);
        return libros;
    }

    private void recorrerRec(NodoLibro actual, TipoRecorrido tipo, List<Libro> resultado) {
        if (actual == null) return;
        if (tipo == TipoRecorrido.PREORDEN) resultado.add(actual.getDato());
        recorrerRec(actual.getIzquierdo(), tipo, resultado);
        if (tipo == TipoRecorrido.INORDEN) resultado.add(actual.getDato());
        recorrerRec(actual.getDerecho(), tipo, resultado);
        if (tipo == TipoRecorrido.POSORDEN) resultado.add(actual.getDato());
    }

    /** Registra las llamadas recursivas y la visita de cada nodo. */
    public String trazaRecorrido(TipoRecorrido tipo) {
        if (tipo == null) throw new IllegalArgumentException("Recorrido obligatorio.");
        StringBuilder sb = new StringBuilder();
        List<Integer> secuencia = new ArrayList<>();
        trazaRec(raiz, tipo, 0, sb, secuencia);
        sb.append("Secuencia ").append(tipo).append(": ");
        for (int i = 0; i < secuencia.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(secuencia.get(i));
        }
        return sb.toString();
    }

    private void trazaRec(NodoLibro actual, TipoRecorrido tipo, int nivel,
                          StringBuilder sb, List<Integer> secuencia) {
        String sangria = "  ".repeat(nivel);
        if (actual == null) {
            sb.append(sangria).append("null -> regresa\n");
            return;
        }
        int codigo = actual.getDato().getCodigo();
        sb.append(sangria).append("entra ").append(codigo).append('\n');
        if (tipo == TipoRecorrido.PREORDEN) visitar(codigo, sangria, sb, secuencia);
        trazaRec(actual.getIzquierdo(), tipo, nivel + 1, sb, secuencia);
        if (tipo == TipoRecorrido.INORDEN) visitar(codigo, sangria, sb, secuencia);
        trazaRec(actual.getDerecho(), tipo, nivel + 1, sb, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) visitar(codigo, sangria, sb, secuencia);
    }

    private void visitar(int codigo, String sangria, StringBuilder sb, List<Integer> secuencia) {
        sb.append(sangria).append("VISITA ").append(codigo).append('\n');
        secuencia.add(codigo);
    }

    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        Libro libro = buscar(codigo);
        if (libro == null) return false;
        libro.actualizar(titulo, autor, anio);
        return true;
    }

    /** Borra hojas, nodos con un hijo y nodos con dos hijos (sucesor inorden). */
    public boolean eliminar(int codigo) {
        if (buscar(codigo) == null) return false;
        raiz = eliminarRec(raiz, codigo);
        return true;
    }

    private NodoLibro eliminarRec(NodoLibro actual, int codigo) {
        if (actual == null) return null;
        int actualCodigo = actual.getDato().getCodigo();
        if (codigo < actualCodigo) {
            actual.setIzquierdo(eliminarRec(actual.getIzquierdo(), codigo));
        } else if (codigo > actualCodigo) {
            actual.setDerecho(eliminarRec(actual.getDerecho(), codigo));
        } else {
            if (actual.getIzquierdo() == null) return actual.getDerecho();
            if (actual.getDerecho() == null) return actual.getIzquierdo();
            NodoLibro sucesor = minimo(actual.getDerecho());
            actual.setDato(sucesor.getDato());
            actual.setDerecho(eliminarRec(actual.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return actual;
    }

    private NodoLibro minimo(NodoLibro actual) {
        return actual.getIzquierdo() == null ? actual : minimo(actual.getIzquierdo());
    }

    /** Cuenta códigos a <= código <= b, omitiendo subárboles imposibles. */
    public int contarEnRango(int a, int b) {
        if (a > b) return 0;
        return contarRangoRec(raiz, a, b);
    }

    private int contarRangoRec(NodoLibro actual, int a, int b) {
        if (actual == null) return 0;
        int codigo = actual.getDato().getCodigo();
        if (codigo < a) return contarRangoRec(actual.getDerecho(), a, b);
        if (codigo > b) return contarRangoRec(actual.getIzquierdo(), a, b);
        return 1 + contarRangoRec(actual.getIzquierdo(), a, b)
                 + contarRangoRec(actual.getDerecho(), a, b);
    }

    /** Devuelve los códigos visitados, aun cuando el buscado no exista. */
    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        caminoRec(raiz, codigo, camino);
        return camino;
    }

    private void caminoRec(NodoLibro actual, int codigo, List<Integer> camino) {
        if (actual == null) return;
        int presente = actual.getDato().getCodigo();
        camino.add(presente);
        if (codigo < presente) caminoRec(actual.getIzquierdo(), codigo, camino);
        if (codigo > presente) caminoRec(actual.getDerecho(), codigo, camino);
    }

    /** Controla cada nodo con límites heredados de todos sus antecesores. */
    public boolean esABBValido() { return validarRec(raiz, Long.MIN_VALUE, Long.MAX_VALUE); }

    private boolean validarRec(NodoLibro actual, long min, long max) {
        if (actual == null) return true;
        int codigo = actual.getDato().getCodigo();
        return codigo > min && codigo < max
                && validarRec(actual.getIzquierdo(), min, codigo)
                && validarRec(actual.getDerecho(), codigo, max);
    }

    /** Extra: reconstrucción directa O(n) desde un preorden válido. No usa insertar().
     * Solo se conocen los códigos: se generan títulos y autores de demostración.
     */
    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null) throw new IllegalArgumentException("Preorden obligatorio.");
        int[] posicion = {0};
        CatalogoABB resultado = new CatalogoABB();
        resultado.raiz = reconstruirRec(preorden, posicion, Long.MIN_VALUE, Long.MAX_VALUE);
        if (posicion[0] != preorden.length) {
            throw new IllegalArgumentException("La secuencia no representa un preorden ABB válido.");
        }
        return resultado;
    }

    private static NodoLibro reconstruirRec(int[] codigos, int[] posicion, long min, long max) {
        if (posicion[0] >= codigos.length) return null;
        int codigo = codigos[posicion[0]];
        if (codigo <= min || codigo >= max) return null;
        if (codigo <= 0) throw new IllegalArgumentException("No se permiten códigos no positivos.");
        posicion[0]++;
        NodoLibro actual = new NodoLibro(new Libro(codigo, "Libro " + codigo, "Autor de ejemplo", 2026));
        actual.setIzquierdo(reconstruirRec(codigos, posicion, min, codigo));
        actual.setDerecho(reconstruirRec(codigos, posicion, codigo, max));
        return actual;
    }
}
