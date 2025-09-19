@echo off
echo ========================================
echo    Ejecutando Tests del User-MS
echo ========================================

echo.
echo 1. Ejecutando tests unitarios...
mvn test -Dtest="*Test" -DfailIfNoTests=false

echo.
echo 2. Ejecutando tests de integración...
mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false

echo.
echo 3. Generando reporte de cobertura...
mvn jacoco:report

echo.
echo ========================================
echo    Tests completados
echo ========================================
echo.
echo Reportes disponibles en:
echo - target/site/jacoco/index.html
echo - target/surefire-reports/
echo.
pause
