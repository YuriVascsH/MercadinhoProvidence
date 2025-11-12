@echo off
setlocal

echo ================================
echo Empacotando PDVProvidence...
echo ================================

:: 1. Criar pasta target\app/
if not exist target\app (
    echo Criando pasta target\app...
    mkdir target\app
)

:: 2. Copiar arquivos de configuração para target\app/
echo Copiando .env e bd_providence.sql para target\app...
copy /Y src\main\resources\.env target\app\.env
copy /Y src\main\resources\bd_providence.sql target\app\bd_providence.sql

:: 3. Limpar empacotamentos anteriores (opcional)
echo Limpando empacotamentos anteriores...
if exist PDVProvidence rmdir /S /Q PDVProvidence
if exist output rmdir /S /Q output

:: 4. Executar jpackage
echo Executando jpackage...

jpackage ^
  --name PDVProvidence ^
  --input target ^
  --main-jar pdv_providence-0.0.1-SNAPSHOT.jar ^
  --main-class br.com.mercadinhoprovidence.MainApplication ^
  --type app-image ^
  --icon src/main/resources/images/icon.ico ^
  --dest output ^
  --app-version 1.0 ^
  --resource-dir target/app

echo ================================
echo Empacotamento concluído!
echo Pasta de saída: output\
echo ================================
pause
