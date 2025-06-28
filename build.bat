@echo off
echo Building Custom Click GUI Mod...

REM Создаем директории для сборки
if not exist "build" mkdir build
if not exist "build\classes" mkdir build\classes
if not exist "build\libs" mkdir build\libs

REM Компилируем Java файлы (пока без зависимостей)
echo Compiling Java files...
javac -d build\classes -cp "libs\*" src\main\java\ru\mby\*.java src\main\java\ru\mby\commands\*.java src\main\java\ru\mby\data\*.java src\client\java\ru\mby\client\*.java src\client\java\ru\mby\gui\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Note: This is a basic compilation test. For full mod building, you need Gradle with Fabric dependencies.
echo.
pause 