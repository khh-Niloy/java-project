@echo off
echo ====================================
echo  Checking Port 8080
echo ====================================
echo.

netstat -ano | findstr :8080
if %ERRORLEVEL% == 0 (
  echo.
  echo Port 8080 is currently in use by another application.
  echo.
  echo Options:
  echo 1. Close the application using port 8080
  echo 2. Run the application on a different port
  echo.
  
  set /p choice=Enter your choice (1 or 2): 
  
  if "%choice%" == "1" (
    echo.
    echo Please close the application using port 8080 and then run run_embedded.bat again.
  ) else if "%choice%" == "2" (
    echo.
    set /p newPort=Enter a new port number (e.g., 8081): 
    echo.
    echo To use port %newPort%, run the following command:
    echo set PORT=%newPort% ^& run_embedded.bat
    echo.
    echo Would you like to run it now? (Y/N)
    set /p runNow=
    if /i "%runNow%" == "Y" (
      set PORT=%newPort%
      call run_embedded.bat
    )
  )
) else (
  echo.
  echo Port 8080 is available. You can run your application using run_embedded.bat
  echo.
  echo Would you like to run it now? (Y/N)
  set /p runNow=
  if /i "%runNow%" == "Y" (
    call run_embedded.bat
  )
)

pause 