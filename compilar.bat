@echo off
chcp 65001 >nul
if not exist out mkdir out
javac -encoding UTF-8 -d out src\enums\*.java src\modelo\*.java src\negocio\*.java src\app\*.java
if errorlevel 1 (
  echo ERROR: No se pudo compilar. Verifica que tengas instalado JDK 17 o superior.
  pause
  exit /b 1
)
echo Compilacion correcta.
pause
