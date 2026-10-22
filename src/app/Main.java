package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

/** Menú de consola solicitado en la Parte E de la guía. */
public class Main {
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static ArbolBiblioteca arbol = DatosPrueba.crearInicial();

    public static void main(String[] args) {
        System.out.println("=== CATÁLOGO DE LA BIBLIOTECA FISEI ===");
        System.out.println("Estructura inicial cargada (7 unidades, 4 hojas, 13 libros).");
        while (true) {
            mostrarMenuPrincipal();
            int opcion = pedirInt("Elige una opción: ");
            try {
                switch (opcion) {
                    case 1 -> cargarInicial();
                    case 2 -> menuBiblioteca();
                    case 3 -> menuLibros();
                    case 4 -> menuConsultasABB();
                    case 5 -> PruebasBiblioteca.main(new String[0]);
                    case 0 -> {
                        System.out.println("Programa finalizado.");
                        return;
                    }
                    default -> System.out.println("Opción incorrecta. Intenta otra vez.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n========= MENÚ PRINCIPAL =========");
        System.out.println("1. Cargar / restablecer biblioteca");
        System.out.println("2. Gestionar biblioteca");
        System.out.println("3. Gestionar libros");
        System.out.println("4. Consultas y recorridos ABB");
        System.out.println("5. Ejecutar pruebas");
        System.out.println("0. Salir");
    }

    private static void menuBiblioteca() {
        while (true) {
            System.out.println("\n------ GESTIONAR BIBLIOTECA ------");
            System.out.println("1. Mostrar jerarquía (preorden)");
            System.out.println("2. Agregar área o estante");
            System.out.println("3. Buscar unidad");
            System.out.println("4. Consultar altura, hojas, nivel y total de libros");
            System.out.println("0. Volver al menú principal");
            int opcion = pedirInt("Elige una opción: ");
            if (opcion == 0) return;
            try {
                switch (opcion) {
                    case 1 -> System.out.println("\n" + arbol.mostrarJerarquia());
                    case 2 -> agregarUnidad();
                    case 3 -> buscarUnidad();
                    case 4 -> estadisticas();
                    default -> System.out.println("Opción incorrecta. Intenta otra vez.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void menuLibros() {
        while (true) {
            System.out.println("\n---------- GESTIONAR LIBROS ----------");
            System.out.println("1. Insertar libro");
            System.out.println("2. Buscar libro");
            System.out.println("3. Modificar libro");
            System.out.println("4. Eliminar libro");
            System.out.println("0. Volver al menú principal");
            int opcion = pedirInt("Elige una opción: ");
            if (opcion == 0) return;
            try {
                switch (opcion) {
                    case 1 -> insertarLibro();
                    case 2 -> buscarLibro();
                    case 3 -> modificarLibro();
                    case 4 -> eliminarLibro();
                    default -> System.out.println("Opción incorrecta. Intenta otra vez.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void menuConsultasABB() {
        while (true) {
            System.out.println("\n------ CONSULTAS Y RECORRIDOS ABB ------");
            System.out.println("1. Recorridos y trazas (pre/in/pos)");
            System.out.println("2. Contar libros en rango");
            System.out.println("3. Camino de búsqueda");
            System.out.println("4. Validar ABB de un estante");
            System.out.println("5. Extra: reconstruir ABB desde preorden");
            System.out.println("0. Volver al menú principal");
            int opcion = pedirInt("Elige una opción: ");
            if (opcion == 0) return;
            try {
                switch (opcion) {
                    case 1 -> mostrarRecorridos();
                    case 2 -> rango();
                    case 3 -> caminoBusqueda();
                    case 4 -> verificarABB();
                    case 5 -> reconstruirPreorden();
                    default -> System.out.println("Opción incorrecta. Intenta otra vez.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void cargarInicial() {
        arbol = DatosPrueba.crearInicial();
        System.out.println("Estructura inicial restablecida.");
    }

    private static void agregarUnidad() {
        String codigoPadre = pedirTexto("Código del padre: ");
        String codigo = pedirTexto("Código de la unidad nueva: ");
        String nombre = pedirTexto("Nombre de la unidad nueva: ");
        System.out.println("Tipo: 1 = AREA, 2 = ESTANTE");
        int opcion = pedirInt("Tipo: ");
        if (opcion != 1 && opcion != 2) {
            System.out.println("Tipo no válido."); return;
        }
        TipoUnidad tipo = opcion == 1 ? TipoUnidad.AREA : TipoUnidad.ESTANTE;
        boolean ok = arbol.agregar(codigoPadre, new UnidadBiblioteca(codigo, nombre, tipo));
        System.out.println(ok ? "Unidad agregada correctamente." :
                "No se pudo agregar (código repetido, padre no encontrado o jerarquía inválida).");
    }

    private static void buscarUnidad() {
        NodoUnidad nodo = arbol.buscar(pedirTexto("Código de unidad: "));
        if (nodo == null) {
            System.out.println("Unidad no encontrada.");
        } else {
            System.out.println("Encontrada: " + nodo.getDato());
            System.out.println("Hijos: " + nodo.getHijos().size());
            if (nodo.getCatalogo() != null) System.out.println("Libros: " + nodo.getCatalogo().contar());
        }
    }

    private static void estadisticas() {
        System.out.println("Altura (aristas): " + arbol.altura());
        System.out.println("Número de hojas: " + arbol.contarHojas());
        System.out.println("Total de libros: " + arbol.contarLibrosTotal());
        String codigo = pedirTexto("Código a consultar nivel (BIB / SW / SW-E2 / XYZ...): ");
        System.out.println("Nivel de " + codigo + ": " + arbol.nivel(codigo));
    }

    private static CatalogoABB seleccionarEstante() {
        String codigo = pedirTexto("Código del estante (SW-E1 / SW-E2 / TI-E1): ");
        NodoUnidad nodo = arbol.buscar(codigo);
        if (nodo == null || nodo.getDato().getTipo() != TipoUnidad.ESTANTE) {
            System.out.println("No existe un estante con ese código.");
            return null;
        }
        return nodo.getCatalogo();
    }

    private static void insertarLibro() {
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        Libro libro = leerLibro();
        System.out.println(abb.insertar(libro) ? "Libro insertado." : "Código duplicado: no insertado.");
    }

    private static Libro leerLibro() {
        int codigo = pedirInt("Código (> 0): ");
        String titulo = pedirTexto("Título: ");
        String autor = pedirTexto("Autor: ");
        int anio = pedirInt("Año (1900-2026): ");
        return new Libro(codigo, titulo, autor, anio);
    }

    private static void buscarLibro() {
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        int codigo = pedirInt("Código a buscar: ");
        Libro libro = abb.buscar(codigo);
        System.out.println(libro == null ? "No existe ese libro." : "Encontrado: " + libro);
    }

    private static void modificarLibro() {
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        int codigo = pedirInt("Código del libro que quieres modificar: ");
        if (abb.buscar(codigo) == null) { System.out.println("Libro no encontrado."); return; }
        String titulo = pedirTexto("Nuevo título: ");
        String autor = pedirTexto("Nuevo autor: ");
        int anio = pedirInt("Nuevo año: ");
        System.out.println(abb.modificar(codigo, titulo, autor, anio)
                ? "Libro actualizado sin cambiar su código." : "No encontrado.");
    }

    private static void eliminarLibro() {
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        int codigo = pedirInt("Código a eliminar: ");
        System.out.println(abb.eliminar(codigo) ? "Libro eliminado." : "No existe ese código.");
    }

    private static void mostrarRecorridos() {
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        System.out.println("1. PREORDEN | 2. INORDEN | 3. POSORDEN | 4. TODOS");
        int seleccion = pedirInt("Recorrido: ");
        if (seleccion < 1 || seleccion > 4) { System.out.println("Opción incorrecta."); return; }
        for (TipoRecorrido tipo : TipoRecorrido.values()) {
            if (seleccion != 4 && tipo.ordinal() != seleccion - 1) continue;
            System.out.println("\n" + tipo + ": " + codigos(abb.recorrer(tipo)));
            System.out.println(abb.trazaRecorrido(tipo));
        }
    }

    private static String codigos(List<Libro> libros) {
        StringBuilder resultado = new StringBuilder();
        for (Libro libro : libros) {
            if (resultado.length() > 0) resultado.append(' ');
            resultado.append(libro.getCodigo());
        }
        return resultado.toString();
    }

    private static void rango() {
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        int a = pedirInt("Límite inferior: ");
        int b = pedirInt("Límite superior: ");
        System.out.println("Número de códigos en [" + a + ", " + b + "]: " + abb.contarEnRango(a, b));
    }

    private static void caminoBusqueda() {
        CatalogoABB abb = seleccionarEstante();
        if (abb == null) return;
        int codigo = pedirInt("Código a buscar: ");
        System.out.println("Camino: " + abb.caminoBusqueda(codigo));
        System.out.println(abb.buscar(codigo) == null ? "El código no existe." : "Código encontrado.");
    }

    private static void verificarABB() {
        CatalogoABB abb = seleccionarEstante();
        if (abb != null) System.out.println("¿Es un ABB válido? " + abb.esABBValido());
    }

    private static void reconstruirPreorden() {
        String linea = pedirTexto("Códigos en PREORDEN separados por espacios (ej. 60 35 20 45 80): ");
        try {
            int[] preorden = Arrays.stream(linea.trim().split("\\s+"))
                    .mapToInt(Integer::parseInt).toArray();
            CatalogoABB reconstruido = CatalogoABB.desdePreorden(preorden);
            System.out.println("ABB reconstruido sin insertar uno por uno.");
            System.out.println("Preorden: " + codigos(reconstruido.recorrer(TipoRecorrido.PREORDEN)));
            System.out.println("Inorden : " + codigos(reconstruido.recorrer(TipoRecorrido.INORDEN)));
        } catch (NumberFormatException e) {
            System.out.println("Escribe solo enteros separados por espacios.");
        }
    }

    private static String pedirTexto(String mensaje) {
        System.out.print(mensaje);
        return ENTRADA.nextLine().trim();
    }

    private static int pedirInt(String mensaje) {
        while (true) {
            String entrada = pedirTexto(mensaje);
            try { return Integer.parseInt(entrada); }
            catch (NumberFormatException e) { System.out.println("Debes ingresar un número entero válido."); }
        }
    }
}
