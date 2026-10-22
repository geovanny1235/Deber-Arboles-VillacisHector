package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

/** Árbol general para biblioteca -> áreas -> estantes. */
public class ArbolBiblioteca {
    private final NodoUnidad raiz;

    public ArbolBiblioteca(UnidadBiblioteca biblioteca) {
        if (biblioteca == null || biblioteca.getTipo() != TipoUnidad.BIBLIOTECA) {
            throw new IllegalArgumentException("La raíz debe ser de tipo BIBLIOTECA.");
        }
        this.raiz = new NodoUnidad(biblioteca);
    }

    public NodoUnidad getRaiz() { return raiz; }

    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (nueva == null || codigoPadre == null || buscar(nueva.getCodigo()) != null) return false;
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null || !padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) return false;
        padre.agregarHijo(new NodoUnidad(nueva));
        return true;
    }

    public NodoUnidad buscar(String codigo) {
        if (codigo == null) return null;
        return buscarRec(raiz, codigo.trim());
    }

    private NodoUnidad buscarRec(NodoUnidad actual, String codigo) {
        if (actual.getDato().getCodigo().equalsIgnoreCase(codigo)) return actual;
        for (NodoUnidad hijo : actual.getHijos()) {
            NodoUnidad encontrado = buscarRec(hijo, codigo);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    /** Altura en número de aristas: una raíz aislada tiene altura 0. */
    public int altura() { return alturaRec(raiz); }

    private int alturaRec(NodoUnidad actual) {
        int mayor = 0;
        for (NodoUnidad hijo : actual.getHijos()) {
            int profundidad = 1 + alturaRec(hijo);
            if (profundidad > mayor) mayor = profundidad;
        }
        return mayor;
    }

    public int contarHojas() { return contarHojasRec(raiz); }

    private int contarHojasRec(NodoUnidad actual) {
        if (actual.getHijos().isEmpty()) return 1;
        int total = 0;
        for (NodoUnidad hijo : actual.getHijos()) total += contarHojasRec(hijo);
        return total;
    }

    /** Nivel 0 para la raíz; -1 si la unidad no existe. */
    public int nivel(String codigo) {
        if (codigo == null) return -1;
        return nivelRec(raiz, codigo.trim(), 0);
    }

    private int nivelRec(NodoUnidad actual, String codigo, int profundidad) {
        if (actual.getDato().getCodigo().equalsIgnoreCase(codigo)) return profundidad;
        for (NodoUnidad hijo : actual.getHijos()) {
            int encontrado = nivelRec(hijo, codigo, profundidad + 1);
            if (encontrado != -1) return encontrado;
        }
        return -1;
    }

    /** Preorden: imprime cada unidad antes de visitar a sus hijos. */
    public String mostrarJerarquia() {
        StringBuilder sb = new StringBuilder();
        mostrarRec(raiz, 0, sb);
        return sb.toString().stripTrailing();
    }

    private void mostrarRec(NodoUnidad actual, int profundidad, StringBuilder sb) {
        sb.append("  ".repeat(profundidad)).append(actual.getDato()).append('\n');
        for (NodoUnidad hijo : actual.getHijos()) mostrarRec(hijo, profundidad + 1, sb);
    }

    public int contarLibrosTotal() { return contarLibrosRec(raiz); }

    private int contarLibrosRec(NodoUnidad actual) {
        int total = actual.getCatalogo() == null ? 0 : actual.getCatalogo().contar();
        for (NodoUnidad hijo : actual.getHijos()) total += contarLibrosRec(hijo);
        return total;
    }
}
