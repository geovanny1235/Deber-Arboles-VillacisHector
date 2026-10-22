# Catálogo de la Biblioteca FISEI: árbol general y ABB en Java

**Asignatura:** Estructura de Datos  
**Nivel:** Tercero, Ingeniería en Software  
**Modalidad:** Trabajo individual  
**Estudiante:** Héctor Villacís  
**IDE:** Visual Studio Code  
**Lenguaje:** Java 17 o superior  
**Entrega indicada en la guía:** jueves 22 de octubre de 2026, 07:00 (Moodle)

> Antes de entregar: lee el código, ejecuta las pruebas en tu computadora, personaliza `TRAZAS.md` con tus cuatro dígitos, realiza el desarrollo y los commits en tu propio repositorio y reemplaza las evidencias de ejemplo por capturas auténticas. La guía requiere trabajo individual y comprensión del código.

## 1. Descripción del problema

La Biblioteca FISEI necesita una aplicación que represente su jerarquía mediante un **árbol general n-ario**: una BIBLIOTECA contiene AREAS, y cada AREA contiene ESTANTES. Cada estante tiene un **árbol binario de búsqueda (ABB)** con libros ordenados por **código**.

La estructura inicial exigida es:

```text
BIB Biblioteca FISEI
├── SW Software
│   ├── SW-E1 → 60, 35, 80, 20, 45, 70, 90, 40, 75
│   └── SW-E2 → 15, 10, 25
├── TI Tecnologías de la Información
│   └── TI-E1 → 50
└── TEL Telecomunicaciones
```

Son **7 unidades, 4 hojas, altura 2 en aristas y 13 libros**. Los códigos y su orden de inserción proceden de la guía; los títulos, autores y años son datos de ejemplo, porque la guía no los especifica.

## 2. Carpetas y clases

```text
Deber-Arboles-VillacisHector/
├── src/
│   ├── enums/
│   │   ├── TipoUnidad.java
│   │   └── TipoRecorrido.java
│   ├── modelo/
│   │   ├── Libro.java
│   │   └── UnidadBiblioteca.java
│   ├── negocio/
│   │   ├── NodoUnidad.java
│   │   ├── NodoLibro.java
│   │   ├── ArbolBiblioteca.java
│   │   └── CatalogoABB.java
│   └── app/
│       ├── Main.java
│       ├── DatosPrueba.java
│       ├── PruebasBiblioteca.java
│       └── GeneradorTrazas.java       (ayuda adicional)
├── .vscode/
│   ├── settings.json
│   └── launch.json
├── README.md
├── TRAZAS.md                    (EJEMPLO; debe personalizarse)
├── INFORME_PARA_COMPLETAR.docx (editable, con espacios para pruebas reales)
├── compilar.bat
├── ejecutar.bat
├── probar.bat
└── .gitignore
```

**Lo que hace cada paquete:**

- `enums`: define valores permitidos para `TipoUnidad` y `TipoRecorrido`.
- `modelo`: valida y conserva datos de libros y unidades.
- `negocio`: implementa ambas estructuras con operaciones recursivas.
- `app`: prepara datos, permite manejar el menú, prueba funciones y genera un ejemplo de trazas.

## 3. Cómo ejecutarlo en Visual Studio Code (Windows)

1. Instala **JDK 17 o superior** y la extensión **Extension Pack for Java** de VS Code.
2. Descomprime el ZIP y abre la carpeta `Deber-Arboles-VillacisHector` en VS Code.
3. Ejecuta **`ejecutar.bat`** desde el Explorador de archivos para abrir el menú, o selecciona `app.Main` y `Run Java`.
4. También puedes abrir la terminal integrada de VS Code y ejecutar los siguientes comandos:

```powershell
mkdir out
javac -encoding UTF-8 -d out src/enums/*.java src/modelo/*.java src/negocio/*.java src/app/*.java
java -Dfile.encoding=UTF-8 -cp out app.Main
```

Si `out` ya existe, no es necesario volver a crearlo. Para ejecutar las pruebas:

```powershell
java -Dfile.encoding=UTF-8 -cp out app.PruebasBiblioteca
```

O abre `probar.bat`. **Resultado verificado al preparar el proyecto: `51/51 pruebas OK`.** Ese resultado debe volver a obtenerse en tu computadora y capturarse para la entrega.

## 4. Opciones del menú

| Opción | Función |
|---|---|
| 1 | Restablecer la estructura inicial. |
| 2 | Mostrar jerarquía con preorden y sangría de dos espacios. |
| 3 | Añadir un área o estante respetando las relaciones permitidas. |
| 4 | Buscar unidades sin distinguir mayúsculas. |
| 5 | Consultar altura, hojas, nivel y libros totales. |
| 6 | Insertar un libro en un estante. |
| 7 | Buscar un libro por código. |
| 8 | Modificar título, autor y año sin modificar el código. |
| 9 | Eliminar un libro (hoja, un hijo o dos hijos). |
| 10 | Ver recorridos y trazas recursivas. |
| 11 | Contar códigos dentro de un rango con poda. |
| 12 | Mostrar la ruta de búsqueda de un código. |
| 13 | Verificar que el ABB cumpla todos sus límites. |
| 14 | Ejecutar las pruebas automáticas. |
| 15 | Extra: reconstruir un ABB desde su preorden sin insertar uno por uno. |
| 0 | Salir. |

## 5. Comprobaciones relevantes de la guía

Sin insertar códigos adicionales, SW-E1 produce:

```text
Preorden: 60 35 20 45 40 80 70 75 90
Inorden:  20 35 40 45 60 70 75 80 90
Posorden: 20 40 45 35 75 70 90 80 60
caminoBusqueda(42): [60, 35, 45, 40]
contarEnRango(40, 75): 5
```

Después de **insertar 42 y eliminar 20 (hoja), 70 (un hijo) y 60 (dos hijos)**, en ese orden:

```text
Preorden final: 75 35 45 40 42 80 90
```

La eliminación con dos hijos usa el **sucesor inorden**: el menor del subárbol derecho.

## 6. TRAZAS.md: NECESITA TU INTERVENCIÓN

La consigna requiere construir tres códigos `ab`, `bc` y `cd` a partir de los últimos **cuatro dígitos de tu cédula**. No hace falta enviarme el número completo.

`TRAZAS.md` viene con un ejemplo usando **1234**, que **no es tu resultado personal**. Para generar una referencia con tus últimos cuatro dígitos, con el proyecto compilado, ejecuta:

```powershell
java -Dfile.encoding=UTF-8 -cp out app.GeneradorTrazas 1234
```

**Sustituye `1234` por tus cuatro dígitos**, conservando cualquier cero inicial. Se reemplazará `TRAZAS.md` con tus códigos, el ABB en Mermaid, recorridos, traza preorden y tres eliminaciones. Este generador **solo ayuda a comprobar**: el profesor solicita que resuelvas los dibujos y recorridos *a mano*, así que debes revisar y completar personalmente ese documento, especialmente el apartado «¿Coincide con mi solución a mano?». No subas el ejemplo de 1234 como si fuera tuyo.

## 7. Control de versiones, rama y pull request

Debes hacerlo **en tu propia cuenta**: no existe un historial real de GitHub en el ZIP. Para crear el historial de manera progresiva y verificable, en la terminal de la carpeta principal (Git configurado en tu equipo):

```bash
git init
git branch -M main
git add .gitignore .vscode
# Primer registro de preparación del entorno
git commit -m "Inicializar estructura y configuración del proyecto"
git switch -c desarrollo

# Primera etapa: estructura de datos
git add src/enums src/modelo src/negocio
git commit -m "Implementar modelo, árbol general y ABB recursivo"

# Segunda etapa: aplicación, datos y pruebas
git add src/app compilar.bat ejecutar.bat probar.bat
git commit -m "Agregar menú y pruebas de biblioteca"

# Tercera etapa: documentación y trazas personalizadas
git add README.md TRAZAS.md INFORME_PARA_COMPLETAR.docx
# Agrega cualquier otro archivo de evidencias que desees versionar
git commit -m "Documentar proyecto y trazas verificadas"
```

Estos commits describen grupos de cambios incluidos en el paquete: **revisa y comprende cada etapa antes de confirmarla**. Luego:

1. En GitHub crea el repositorio **público** `Deber-Arboles-VillacisHector` (nombre según «ApellidoNombre»).
2. Copia la dirección HTTPS de tu repositorio y reemplaza la URL de ejemplo:

```bash
git remote add origin https://github.com/TU-USUARIO/Deber-Arboles-VillacisHector.git
git push -u origin main
git push -u origin desarrollo
```

3. En GitHub abre un **Pull Request** desde `desarrollo` hacia `main`.
4. Revisa que los archivos estén completos y realiza el **merge** antes del plazo.
5. Captura el historial de commits, el pull request y la rama `main` ya actualizada.

Si ya habías creado el repositorio, ajusta estos pasos a su historial; no sobrescribas trabajos existentes.

## 8. Informe de evidencias

Abre `INFORME_PARA_COMPLETAR.docx` y reemplaza los campos `[PENDIENTE]` por:

- Tu URL real del repositorio **público**.
- Capturas **auténticas** de tu ejecución en VS Code o terminal (menú, jerarquía, recorridos y `PruebasBiblioteca`).
- Captura del historial de commits, la rama `desarrollo`, el PR y su merge.
- Reflexión personal revisada con tus propias palabras.

Finalmente exporta a PDF con nombre `Deber-Arboles-VillacisHector.pdf` y súbelo a Moodle antes de la hora límite. **No envíes el borrador con campos pendientes**.

## 9. Restricciones respetadas

Las operaciones de árboles usan recursividad. No se usan `TreeSet`, `TreeMap`, `PriorityQueue`, `NavigableSet`, `NavigableMap`, `Collections.sort`, `List.sort` ni ordenaciones de listas para producir recorridos.

## 10. Ejemplos para interpretar el código

- **Recursividad:** `contarRec` cuenta el nodo actual y llama a la misma función con su hijo izquierdo y derecho.
- **ABB:** si el código es menor al actual, busca en la izquierda; si es mayor, en la derecha.
- **Inorden:** izquierda → nodo → derecha, lo que produce los códigos de menor a mayor en un ABB válido.
- **Complejidad de búsqueda:** en ABB, `O(h)` por seguir una sola rama; en el árbol general, `O(N)` en el peor caso porque puede visitar todas las unidades.

**Fuente del enunciado:** `Deber_Biblioteca_Arboles.pdf`, proporcionado por el docente. La implementación y las pruebas de esta carpeta se prepararon para cumplir dicha guía.
