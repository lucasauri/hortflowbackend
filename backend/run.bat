@echo off
echo ========================================
echo    SISTEMA HORTIFRUTI - BACKEND
echo ========================================
echo.

echo Verificando se o Java esta instalado...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Java nao encontrado!
    echo Por favor, instale o Java 17 ou superior.
    pause
    exit /b 1
)

echo Verificando se o Maven esta instalado...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Maven nao encontrado!
    echo Por favor, instale o Maven 3.6 ou superior.
    pause
    exit /b 1
)

echo.
echo Compilando o projeto...
call mvn clean compile
if %errorlevel% neq 0 (
    echo ERRO: Falha na compilacao!
    pause
    exit /b 1
)

echo.
echo Iniciando o servidor...
echo.
echo ========================================
echo    SERVIDOR INICIADO COM SUCESSO!
echo ========================================
echo.
echo API disponivel em: http://localhost:8080/api
echo Swagger UI: http://localhost:8080/api/swagger-ui/index.html
echo Health Check: http://localhost:8080/api/produtos/health
echo.
echo Pressione Ctrl+C para parar o servidor
echo.

call mvn spring-boot:run

pause 