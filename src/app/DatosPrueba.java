package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;

/** Construye exactamente la estructura y los códigos exigidos por la guía. */
public final class DatosPrueba {
    private DatosPrueba() { }

    public static ArbolBiblioteca crearInicial() {
        ArbolBiblioteca arbol = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));

        arbol.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        arbol.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));
        arbol.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));

        // La consigna proporciona los CÓDIGOS, no títulos/autores/años.
        // Estos textos y años son datos ficticios exclusivamente para practicar.
        cargar(arbol.buscar("SW-E1").getCatalogo(), new int[] {60,35,80,20,45,70,90,40,75});
        cargar(arbol.buscar("SW-E2").getCatalogo(), new int[] {15,10,25});
        cargar(arbol.buscar("TI-E1").getCatalogo(), new int[] {50});
        return arbol;
    }

    private static void cargar(CatalogoABB catalogo, int[] codigos) {
        for (int codigo : codigos) {
            catalogo.insertar(new Libro(codigo, "Libro de ejemplo " + codigo,
                    "Autor de ejemplo", 2024));
        }
    }
}
