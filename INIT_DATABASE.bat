@echo off
chcp 65001 >nul
cls
echo ========================================
echo 数据库初始化脚本
echo ========================================
echo.

set "MYSQL_BIN=C:\Program Files\MySQL\MySQL Server 8.4\bin"

echo [1/3] 启动 MySQL...
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo MySQL 进程已在运行
) else (
    echo 正在启动 MySQL 进程...
    start "MySQL Server" /MIN "%MYSQL_BIN%\mysqld.exe" --console
)

echo 等待 MySQL 完全启动...
set /a count=0
:wait_mysql
netstat -ano | findstr "LISTENING" | findstr ":3306" >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo MySQL 已就绪（3306端口监听中）
    goto mysql_ready
)
set /a count+=1
if %count% LSS 30 (
    timeout /t 1 /nobreak >nul
    goto wait_mysql
)
echo 警告: MySQL 启动超时，但继续尝试...
:mysql_ready

echo.
echo [2/3] 等待 MySQL 启动...
timeout /t 3 /nobreak >nul

echo.
echo [3/3] 创建数据库并导入 schema...
"%MYSQL_BIN%\mysql.exe" -u root -proot -e "CREATE DATABASE IF NOT EXISTS oa_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo 错误: 无法连接到 MySQL
    echo 请检查:
    echo   1. MySQL 服务是否正在运行
    echo   2. root 密码是否为 "root"
    echo.
    echo 如果密码不是 "root", 请修改:
    echo   - 本脚本中的 -proot
    echo   - oa-backend\src\main\resources\application.yml 中的 password
    echo.
    pause
    exit /b 1
)

"%MYSQL_BIN%\mysql.exe" -u root -proot oa_system < "%~dp0db\schema_mysql.sql"
if %ERRORLEVEL% NEQ 0 (
    echo 警告: schema 导入可能失败，请检查 db\schema_mysql.sql 文件
) else (
    echo schema 导入成功
)

echo.
echo ========================================
echo 数据库初始化完成！
echo ========================================
echo.
echo 数据库名称: oa_system
echo 默认账号: Admin / 123456
echo.
pause
