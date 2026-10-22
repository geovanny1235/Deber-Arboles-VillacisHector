package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

/** Pruebas reproducibles del enunciado y algunos casos adicionales. */
public class PruebasBiblioteca {
    private static int total;
    private static int correctas;
    private static final List<String> errores = new ArrayList<>();

    public static void main(String[] args) {
        total = 0;
        correctas = 0;
        errores.clear();
        System.out.println("=== PRUEBAS BIBLIOTECA FISEI ===");

        ArbolBiblioteca soloRaiz = nuevaBibliotecaVacia();
        comprobar("Solo raíz: altura 0", soloRaiz.altura() == 0);
        comprobar("Solo raíz: 1 hoja", soloRaiz.contarHojas() == 1);
        comprobar("Solo raíz: 0 libros", soloRaiz.contarLibrosTotal() == 0);

        ArbolBiblioteca arbol = DatosPrueba.crearInicial();
        comprobar("Inicial: altura 2", arbol.altura() == 2);
        comprobar("Inicial: 4 hojas", arbol.contarHojas() == 4);
        comprobar("Inicial: 13 libros", arbol.contarLibrosTotal() == 13);
        comprobar("Nivel BIB = 0", arbol.nivel("BIB") == 0);
        comprobar("Nivel sw = 1 (ignora mayúsculas)", arbol.nivel("sw") == 1);
        comprobar("Nivel SW-E2 = 2", arbol.nivel("SW-E2") == 2);
        comprobar("Nivel XYZ = -1", arbol.nivel("XYZ") == -1);
        comprobar("Buscar sw (sin distinguir mayúsculas)", arbol.buscar("sw") != null);
        comprobar("Buscar unidad inexistente es null", arbol.buscar("XYZ") == null);
        comprobar("Estante directo bajo biblioteca es false",
                !arbol.agregar("BIB", new UnidadBiblioteca("ERR", "Incorrecto", TipoUnidad.ESTANTE)));
        comprobar("Código de unidad repetido es false",
                !arbol.agregar("BIB", new UnidadBiblioteca("SW", "Repetido", TipoUnidad.AREA)));
        comprobar("Rechaza estante debajo de estante",
                !arbol.agregar("SW-E1", new UnidadBiblioteca("EST2", "Incorrecto", TipoUnidad.ESTANTE)));
        comprobar("Jerarquía preorden con sangría", arbol.mostrarJerarquia().contains("    SW-E1"));

        CatalogoABB abb = arbol.buscar("SW-E1").getCatalogo();
        comprobar("SW-E1 tiene 9 libros", abb.contar() == 9);
        comprobar("SW-E1: preorden exacto",
                codigos(abb, TipoRecorrido.PREORDEN).equals("60 35 20 45 40 80 70 75 90"));
        comprobar("SW-E1: inorden exacto",
                codigos(abb, TipoRecorrido.INORDEN).equals("20 35 40 45 60 70 75 80 90"));
        comprobar("SW-E1: posorden exacto",
                codigos(abb, TipoRecorrido.POSORDEN).equals("20 40 45 35 75 70 90 80 60"));
        comprobar("Inserción código repetido es false",
                !abb.insertar(new Libro(60, "Duplicado", "Prueba", 2026)));
        comprobar("Buscar existente 45", abb.buscar(45) != null);
        comprobar("Buscar inexistente es null", abb.buscar(999) == null);
        comprobar("caminoBusqueda(42) = [60, 35, 45, 40]",
                abb.caminoBusqueda(42).equals(Arrays.asList(60, 35, 45, 40)));
        comprobar("contarEnRango(40,75) = 5", abb.contarEnRango(40, 75) == 5);
        comprobar("Rango invertido: 0", abb.contarEnRango(75, 40) == 0);
        comprobar("Traza empieza con entra 60", abb.trazaRecorrido(TipoRecorrido.PREORDEN).startsWith("entra 60\n"));
        comprobar("Traza registra null -> regresa", abb.trazaRecorrido(TipoRecorrido.PREORDEN).contains("null -> regresa"));
        comprobar("Traza termina en secuencia completa", abb.trazaRecorrido(TipoRecorrido.PREORDEN)
                .endsWith("Secuencia PREORDEN: 60 35 20 45 40 80 70 75 90"));
        comprobar("esABBValido en inicial", abb.esABBValido());

        comprobar("Modificar libro existente", abb.modificar(45, "Título nuevo", "Autor nuevo", 2026));
        comprobar("Modificar conserva código", abb.buscar(45).getCodigo() == 45);
        comprobar("Modificar actualiza título", abb.buscar(45).getTitulo().equals("Título nuevo"));
        comprobar("Modificar inexistente devuelve false", !abb.modificar(888, "Otro", "Otro", 2026));
        comprobar("Eliminar inexistente devuelve false", !abb.eliminar(888));
        comprobar("Datos inválidos lanzan IllegalArgumentException", lanzaExcepcion(() ->
                new Libro(-5, "Libro", "Autor", 2026)));
        comprobar("Año inválido lanza IllegalArgumentException", lanzaExcepcion(() ->
                new Libro(99, "Libro", "Autor", 1899)));
        comprobar("Título vacío lanza IllegalArgumentException", lanzaExcepcion(() ->
                new Libro(99, "  ", "Autor", 2020)));
        comprobar("Actualización inválida no cambia título", lanzaExcepcion(() ->
                abb.modificar(45, "Cambiaría", "Autor", 2027)) &&
                abb.buscar(45).getTitulo().equals("Título nuevo"));

        comprobar("Insertar 42", abb.insertar(new Libro(42, "Libro 42", "Autor", 2026)));
        comprobar("Eliminar 20 (hoja)", abb.eliminar(20));
        comprobar("Eliminar 70 (un hijo)", abb.eliminar(70));
        comprobar("Eliminar 60 (dos hijos/sucesor)", abb.eliminar(60));
        comprobar("Preorden final exigido",
                codigos(abb, TipoRecorrido.PREORDEN).equals("75 35 45 40 42 80 90"));
        comprobar("ABB válido después de eliminaciones", abb.esABBValido());
        comprobar("Conteo final SW-E1 = 7", abb.contar() == 7);
        comprobar("Total biblioteca refleja cambios", arbol.contarLibrosTotal() == 11);

        CatalogoABB dañado = new CatalogoABB();
        for (int codigo : new int[] {50, 30, 70}) dañado.insertar(new Libro(codigo, "L" + codigo, "Autor", 2026));
        NodoLibro nietoIncorrecto = new NodoLibro(new Libro(55, "Mal ubicado", "Autor", 2026));
        dañado.getRaiz().getIzquierdo().setDerecho(nietoIncorrecto); // 55 > 50 en rama izquierda
        comprobar("ABB inválido por nieto mal ubicado", !dañado.esABBValido());

        int[] original = {60, 35, 20, 45, 40, 80, 70, 75, 90};
        CatalogoABB reconstruido = CatalogoABB.desdePreorden(original);
        comprobar("Extra: reconstrucción respeta preorden",
                codigos(reconstruido, TipoRecorrido.PREORDEN).equals("60 35 20 45 40 80 70 75 90"));
        comprobar("Extra: reconstrucción respeta ABB", reconstruido.esABBValido());
        comprobar("Extra: preorden inválido rechazado", lanzaExcepcion(() ->
                CatalogoABB.desdePreorden(new int[] {50, 70, 30})));

        System.out.printf("\nRESULTADO: %d/%d pruebas OK%n", correctas, total);
        if (!errores.isEmpty()) {
            System.out.println("Fallaron: " + errores);
            // Desde consola, devolver código de error para automatización.
            System.exit(1);
        }
    }

    private static ArbolBiblioteca nuevaBibliotecaVacia() {
        return new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
    }

    private static void comprobar(String descripcion, boolean condicion) {
        total++;
        if (condicion) correctas++;
        else errores.add(descripcion);
        System.out.printf("[%s] %s%n", condicion ? "OK" : "FALLA", descripcion);
    }

    private static boolean lanzaExcepcion(Runnable accion) {
        try { accion.run(); return false; }
        catch (IllegalArgumentException esperado) { return true; }
    }

    private static String codigos(CatalogoABB abb, TipoRecorrido tipo) {
        StringBuilder sb = new StringBuilder();
        for (Libro libro : abb.recorrer(tipo)) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(libro.getCodigo());
        }
        return sb.toString();
    }
}
