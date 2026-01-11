@echo off
chcp 65001 >nul
cls
echo ========================================
echo 仅导入数据库（假设 MySQL 已运行）
echo ========================================
echo.

set "MYSQL_BIN=C:\Program Files\MySQL\MySQL Server 8.4\bin"

echo 正在连接 MySQL 并导入数据库...
echo.
echo 请输入 MySQL root 密码：
set /p MYSQL_PASSWORD=

"%MYSQL_BIN%\mysql.exe" -u root -p%MYSQL_PASSWORD% -e "CREATE DATABASE IF NOT EXISTS oa_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo 错误: 无法连接到 MySQL 或密码错误
    pause
    exit /b 1
)

"%MYSQL_BIN%\mysql.exe" -u root -p%MYSQL_PASSWORD% oa_system < "%~dp0db\schema_mysql.sql"
if %ERRORLEVEL% NEQ 0 (
    echo 警告: schema 导入可能失败
) else (
    echo.
    echo ========================================
    echo 数据库导入成功！
    echo ========================================
)

echo.
echo 如果密码不是 "root"，请修改：
echo   oa-backend\src\main\resources\application.yml
echo   将 password: root 改为 password: %MYSQL_PASSWORD%
echo.
pause
