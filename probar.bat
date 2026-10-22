@echo off
chcp 65001 >nul
if not exist out mkdir out
javac -encoding UTF-8 -d out src\enums\*.java src\modelo\*.java src\negocio\*.java src\app\*.java
if errorlevel 1 (pause & exit /b 1)
java -Dfile.encoding=UTF-8 -cp out app.PruebasBiblioteca
pause
