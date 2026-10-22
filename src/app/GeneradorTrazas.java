package app;

import enums.TipoRecorrido;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import modelo.Libro;
import negocio.CatalogoABB;
import negocio.NodoLibro;

/**
 * Guía automática para comprobar las TRAZAS que la tarea exige resolver a mano.
 * Genera diagramas Mermaid y la salida real de las funciones Java.
 * NO sustituye la comprobación manual que pide el docente.
 */
public class GeneradorTrazas {
    public static void main(String[] args) throws IOException {
        String ultimosCuatro;
        if (args.length >= 1) {
            ultimosCuatro = args[0];
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.print("Escribe los últimos 4 dígitos de tu cédula (solo 4 números): ");
            ultimosCuatro = sc.nextLine().trim();
        }
        if (!ultimosCuatro.matches("[0-9]{4}")) {
            System.out.println("Debes proporcionar exactamente 4 dígitos, incluidos ceros iniciales.");
            return;
        }

        CatalogoABB abb = DatosPrueba.crearInicial().buscar("SW-E1").getCatalogo();
        int[] nuevos = calcularPersonales(ultimosCuatro, abb);
        StringBuilder md = new StringBuilder();
        md.append("# TRAZAS - Biblioteca FISEI\n\n");
        md.append("> **IMPORTANTE:** este documento es una referencia generada con Java. "
                + "La guía exige resolver las trazas **a mano**: reproduce y verifica "
                + "personalmente los recorridos, los dibujos y los resultados.\n\n");
        if (ultimosCuatro.equals("1234")) {
            md.append("> **EJEMPLO NO PERSONALIZADO:** se usaron `1234` solo para demostrar el proceso. "
                    + "Vuelve a generar el archivo con tus propios cuatro dígitos.\n\n");
        }
        md.append("## 1. Tres códigos personales\n\n");
        md.append("Cuatro dígitos usados: `").append(ultimosCuatro).append("` (a b c d).\n\n");
        md.append("- `ab` = ").append(ultimosCuatro.substring(0, 2)).append(" → ")
                .append(nuevos[0]).append("\n");
        md.append("- `bc` = ").append(ultimosCuatro.substring(1, 3)).append(" → ")
                .append(nuevos[1]).append("\n");
        md.append("- `cd` = ").append(ultimosCuatro.substring(2, 4)).append(" → ")
                .append(nuevos[2]).append("\n\n");
        md.append("Si un código es 0, está repetido o ya está en SW-E1, se incrementa en 1 "
                + "hasta encontrar uno permitido.\n\n");

        md.append("## 2. Inserciones y ABB resultante\n\n");
        md.append("Orden inicial obligatorio: `60, 35, 80, 20, 45, 70, 90, 40, 75`.\n\n");
        for (int codigo : nuevos) abb.insertar(new Libro(codigo, "Libro personal " + codigo, "Autor de ejemplo", 2026));
        md.append("Se agregan a continuación: `").append(nuevos[0]).append(", ")
                .append(nuevos[1]).append(", ").append(nuevos[2]).append("`.\n\n");
        md.append("```mermaid\n").append(mermaid(abb)).append("```\n\n");

        md.append("## 3. Recorridos obtenidos con Java\n\n");
        for (TipoRecorrido tipo : TipoRecorrido.values()) {
            md.append("- **").append(tipo).append(":** `")
                    .append(codigos(abb, tipo)).append("`\n");
        }
        md.append("\n## 4. Traza detallada del PREORDEN\n\n");
        md.append("Dos espacios por nivel. Al entrar al nodo se imprime `entra X`; "
                + "al visitarlo, `VISITA X`; y al encontrar un hijo vacío, `null -> regresa`.\n\n");
        md.append("```text\n").append(abb.trazaRecorrido(TipoRecorrido.PREORDEN))
                .append("\n```\n\n");

        md.append("## 5. Tres eliminaciones\n\n");
        md.append("Los tres borrados se aplican **en secuencia**, seleccionando en cada "
                + "momento un nodo del tipo solicitado. Se conserva el ABB válido.\n\n");
        int[] eleccion = elegirTresEliminaciones(abb);
        if (eleccion == null) {
            md.append("No se encontró automáticamente una combinación válida de tres casos "
                    + "secuenciales. Selecciona y documenta manualmente los tres casos.\n\n");
        } else {
            String[] tipos = {"hoja (sin hijos)", "nodo con un hijo", "nodo con dos hijos"};
            for (int i = 0; i < eleccion.length; i++) {
                int codigo = eleccion[i];
                md.append("### ").append(i + 1).append(". Eliminar ").append(codigo)
                        .append(" - ").append(tipos[i]).append("\n\n");
                boolean ok = abb.eliminar(codigo);
                md.append("- `eliminar(").append(codigo).append(")`: `").append(ok).append("`\n");
                md.append("- Preorden obtenido: `").append(codigos(abb, TipoRecorrido.PREORDEN))
                        .append("`\n");
                md.append("- ABB válido: `").append(abb.esABBValido()).append("`\n\n");
                md.append("```mermaid\n").append(mermaid(abb)).append("```\n\n");
            }
        }
        md.append("## 6. Comparación con resolución manual\n\n");
        md.append("| Comprobación | Salida de Java | ¿Coincide con mi solución a mano? |\n")
                .append("|---|---|---|\n")
                .append("| Códigos ab, bc y cd | ").append(nuevos[0]).append(", ")
                .append(nuevos[1]).append(", ").append(nuevos[2]).append(" | Pendiente de verificar |\n")
                .append("| Recorridos | Apartado 3 | Pendiente de verificar |\n")
                .append("| Traza preorden | Apartado 4 | Pendiente de verificar |\n")
                .append("| Eliminaciones | Apartado 5 | Pendiente de verificar |\n\n");
        md.append("**Observación personal:** [Escribe qué comprobaste al reproducir los tres "
                + "casos de eliminación y si tu dibujo a mano coincide con Java.]\n");
        Path ruta = Path.of("TRAZAS.md");
        Files.writeString(ruta, md.toString(), StandardCharsets.UTF_8);
        System.out.println("Generado: " + ruta.toAbsolutePath());
        System.out.println("Códigos elegidos: " + nuevos[0] + ", " + nuevos[1] + ", " + nuevos[2]);
    }

    private static int[] calcularPersonales(String cuatro, CatalogoABB abb) {
        int[] res = new int[3];
        for (int i = 0; i < 3; i++) {
            int candidato = Integer.parseInt(cuatro.substring(i, i + 2));
            boolean ocupado;
            do {
                ocupado = candidato <= 0 || abb.buscar(candidato) != null;
                for (int j = 0; j < i; j++) if (candidato == res[j]) ocupado = true;
                if (ocupado) candidato++;
            } while (ocupado);
            res[i] = candidato;
        }
        return res;
    }

    private static String codigos(CatalogoABB abb, TipoRecorrido tipo) {
        StringBuilder sb = new StringBuilder();
        for (Libro libro : abb.recorrer(tipo)) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(libro.getCodigo());
        }
        return sb.toString();
    }

    private static String mermaid(CatalogoABB abb) {
        StringBuilder sb = new StringBuilder("graph TD\n");
        if (abb.getRaiz() == null) return "graph TD\n    vacio[\"Árbol vacío\"]\n";
        int[] indice = {0};
        nodoMermaid(abb.getRaiz(), sb, indice);
        return sb.toString();
    }

    private static int nodoMermaid(NodoLibro actual, StringBuilder sb, int[] indice) {
        int id = indice[0]++;
        sb.append("    N").append(id).append("[\"").append(actual.getDato().getCodigo())
                .append("\"]\n");
        if (actual.getIzquierdo() != null) {
            int hijo = nodoMermaid(actual.getIzquierdo(), sb, indice);
            sb.append("    N").append(id).append(" -->|I| N").append(hijo).append("\n");
        }
        if (actual.getDerecho() != null) {
            int hijo = nodoMermaid(actual.getDerecho(), sb, indice);
            sb.append("    N").append(id).append(" -->|D| N").append(hijo).append("\n");
        }
        return id;
    }

    private static CatalogoABB copia(CatalogoABB origen) {
        int[] p = origen.recorrer(TipoRecorrido.PREORDEN).stream()
                .mapToInt(Libro::getCodigo).toArray();
        return CatalogoABB.desdePreorden(p);
    }

    private static List<Integer> nodosConHijos(CatalogoABB abb, int numHijos) {
        List<Integer> resultado = new ArrayList<>();
        recopilar(abb.getRaiz(), numHijos, resultado);
        return resultado;
    }

    private static void recopilar(NodoLibro actual, int numHijos, List<Integer> resultado) {
        if (actual == null) return;
        int hijos = (actual.getIzquierdo() == null ? 0 : 1)
                + (actual.getDerecho() == null ? 0 : 1);
        if (hijos == numHijos) resultado.add(actual.getDato().getCodigo());
        recopilar(actual.getIzquierdo(), numHijos, resultado);
        recopilar(actual.getDerecho(), numHijos, resultado);
    }

    private static List<Integer> preferir(List<Integer> candidatos, int preferido) {
        candidatos.sort(Comparator.comparingInt((Integer n) -> n == preferido ? 0 : 1)
                .thenComparingInt(Integer::intValue));
        return candidatos;
    }

    private static int[] elegirTresEliminaciones(CatalogoABB origen) {
        for (int hoja : preferir(nodosConHijos(origen, 0), 20)) {
            CatalogoABB despuesHoja = copia(origen);
            despuesHoja.eliminar(hoja);
            for (int uno : preferir(nodosConHijos(despuesHoja, 1), 70)) {
                CatalogoABB despuesUno = copia(despuesHoja);
                despuesUno.eliminar(uno);
                List<Integer> candidatosDos = preferir(nodosConHijos(despuesUno, 2), 60);
                if (!candidatosDos.isEmpty()) return new int[] {hoja, uno, candidatosDos.get(0)};
            }
        }
        return null;
    }
}
