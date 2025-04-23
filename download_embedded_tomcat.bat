@echo off
echo ====================================
echo  Downloading Dependencies
echo ====================================
echo.

set LIBS_DIR=lib
mkdir %LIBS_DIR%
cd %LIBS_DIR%

echo Downloading JSTL...
curl -L -O https://repo1.maven.org/maven2/jstl/jstl/1.2/jstl-1.2.jar

echo Downloading SQLite JDBC...
curl -L -O https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.40.1.0/sqlite-jdbc-3.40.1.0.jar

echo Downloading Embedded Tomcat...
curl -L -O https://repo1.maven.org/maven2/org/apache/tomcat/embed/tomcat-embed-core/9.0.70/tomcat-embed-core-9.0.70.jar
curl -L -O https://repo1.maven.org/maven2/org/apache/tomcat/embed/tomcat-embed-jasper/9.0.70/tomcat-embed-jasper-9.0.70.jar
curl -L -O https://repo1.maven.org/maven2/org/apache/tomcat/embed/tomcat-embed-el/9.0.70/tomcat-embed-el-9.0.70.jar
curl -L -O https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-annotations-api/9.0.70/tomcat-annotations-api-9.0.70.jar
curl -L -O https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-api/9.0.70/tomcat-api-9.0.70.jar
curl -L -O https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-util/9.0.70/tomcat-util-9.0.70.jar
curl -L -O https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-util-scan/9.0.70/tomcat-util-scan-9.0.70.jar

cd ..

echo.
echo Dependencies downloaded!
echo Now you can run run_embedded.bat to start the application.
echo.
pause 