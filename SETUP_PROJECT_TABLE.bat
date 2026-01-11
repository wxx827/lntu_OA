@echo off
chcp 65001 >nul
echo ========================================
echo 执行项目表SQL并重启后端
echo ========================================
echo.

REM 查找MySQL
set "MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
if not exist "%MYSQL_PATH%" (
    set "MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe"
)
if not exist "%MYSQL_PATH%" (
    echo 未找到MySQL，请手动执行 db\schema_project.sql
    pause
    exit /b 1
)

echo 正在执行SQL...
"%MYSQL_PATH%" -u root -p123456 oa_system < "%~dp0db\schema_project.sql"

if %ERRORLEVEL% EQU 0 (
    echo SQL执行成功！
) else (
    echo SQL执行失败，错误代码: %ERRORLEVEL%
    echo 请检查MySQL密码或手动执行
)

echo.
echo 按任意键继续重启后端...
pause >nul

REM 重启后端
cd /d "%~dp0"
start "" ".\START_BACKEND_ONLY.bat"

echo.
echo 后端正在重启中...
echo.
pause
