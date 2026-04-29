@echo off
echo Resetting MySQL database 'visa_db'...

set MYSQL_USER=root
set MYSQL_PASS=
set MYSQL_PATH="C:\xampp\mysql\bin\mysql.exe"

if not exist %MYSQL_PATH% (
    echo MySQL not found at %MYSQL_PATH%. Please install MySQL or update the path in this script.
    pause
    exit /b 1
)

%MYSQL_PATH% -u %MYSQL_USER% -p%MYSQL_PASS% -e "DROP DATABASE IF EXISTS visa_db; CREATE DATABASE visa_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
if %errorlevel% equ 0 (
    echo Database 'visa_db' has been reset successfully.
    echo Executing script.sql...
    %MYSQL_PATH% -u %MYSQL_USER% -p%MYSQL_PASS% visa_db < "%~dp0script.sql"
    if %errorlevel% equ 0 (
        echo script.sql executed successfully.
        echo Executing donneeref.sql...
        %MYSQL_PATH% -u %MYSQL_USER% -p%MYSQL_PASS% visa_db < "%~dp0donneeref.sql"
        if %errorlevel% equ 0 (
            echo donneeref.sql executed successfully.
        ) else (
            echo Error executing donneeref.sql.
        )
    ) else (
        echo Error executing script.sql.
    )
) else (
    echo Error resetting the database. Please check your credentials and MySQL installation.
)
pause
