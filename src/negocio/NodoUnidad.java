package negocio;

import enums.TipoUnidad;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.UnidadBiblioteca;

/** Nodo del árbol general n-ario: puede tener muchos hijos. */
public class NodoUnidad {
    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos;
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        if (dato == null) throw new IllegalArgumentException("Unidad obligatoria.");
        this.dato = dato;
        this.hijos = new ArrayList<>();
        this.catalogo = dato.getTipo() == TipoUnidad.ESTANTE ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() { return dato; }
    public List<NodoUnidad> getHijos() { return Collections.unmodifiableList(hijos); }
    public CatalogoABB getCatalogo() { return catalogo; }

    void agregarHijo(NodoUnidad hijo) { hijos.add(hijo); }
}
