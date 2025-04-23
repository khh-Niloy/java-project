@echo off
echo ====================================
echo  Personal Expense Tracker Builder
echo ====================================
echo.

rem Set paths
set TOMCAT_PATH=C:\Program Files\Apache Software Foundation\Tomcat 9.0
set PROJECT_PATH=%CD%
set WEBAPP_PATH=%PROJECT_PATH%\src\main\webapp
set CLASSES_PATH=%PROJECT_PATH%\target\classes
set BUILD_PATH=%PROJECT_PATH%\target
set WAR_NAME=expense-tracker.war

echo Setting up directories...
mkdir %CLASSES_PATH%

echo Compiling Java files...
javac -cp "%TOMCAT_PATH%\lib\*" -d %CLASSES_PATH% src\main\java\com\expensetracker\model\*.java src\main\java\com\expensetracker\util\*.java src\main\java\com\expensetracker\controller\*.java src\main\java\com\expensetracker\servlet\*.java

if %ERRORLEVEL% NEQ 0 (
  echo Compilation failed!
  pause
  exit /b 1
)

echo Copying web files...
xcopy /E /Y %WEBAPP_PATH%\* %BUILD_PATH%\

echo Creating WAR file...
cd %BUILD_PATH%
jar -cf %WAR_NAME% *

echo WAR file created: %BUILD_PATH%\%WAR_NAME%
echo.
echo To run this application:
echo 1. Download and install Apache Tomcat 9 (if not already installed)
echo 2. Copy %BUILD_PATH%\%WAR_NAME% to %TOMCAT_PATH%\webapps\
echo 3. Start Tomcat
echo 4. Open http://localhost:8080/expense-tracker in your browser
echo.

echo Would you like to install the WAR to Tomcat now? (Y/N)
set /p INSTALL_WAR=

if /i "%INSTALL_WAR%"=="Y" (
  if exist "%TOMCAT_PATH%\webapps" (
    echo Copying WAR to Tomcat webapps...
    copy /Y %BUILD_PATH%\%WAR_NAME% "%TOMCAT_PATH%\webapps\"
    echo Done!
  ) else (
    echo Tomcat not found at %TOMCAT_PATH%
    echo Please install Tomcat or update the TOMCAT_PATH in this script.
  )
)

echo.
pause 