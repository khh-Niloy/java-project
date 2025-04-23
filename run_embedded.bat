@echo off
echo ====================================
echo  Personal Expense Tracker Runner
echo ====================================
echo.

set PROJECT_PATH=%CD%
set CLASSES_PATH=%PROJECT_PATH%\target\classes
set LIB_PATH=%PROJECT_PATH%\lib
set WEBAPP_PATH=%PROJECT_PATH%\src\main\webapp

if not exist "%LIB_PATH%" (
  echo Dependencies not found!
  echo Please run download_embedded_tomcat.bat first.
  pause
  exit /b 1
)

echo Setting up directories...
if not exist "%CLASSES_PATH%" mkdir "%CLASSES_PATH%" 2>nul

echo Compiling Java files...
javac -cp "%LIB_PATH%\*" -d "%CLASSES_PATH%" src\main\java\com\expensetracker\model\*.java src\main\java\com\expensetracker\util\*.java src\main\java\com\expensetracker\controller\*.java src\main\java\com\expensetracker\servlet\*.java src\main\java\com\expensetracker\*.java

if %ERRORLEVEL% NEQ 0 (
  echo Compilation failed!
  pause
  exit /b 1
)

echo Starting application with embedded Tomcat...
echo.
echo The application will be available at:
echo http://localhost:8080/expense-tracker
echo.
echo Press Ctrl+C to stop the server
echo.

java -cp "%CLASSES_PATH%;%LIB_PATH%\*" com.expensetracker.EmbeddedTomcatServer

pause 