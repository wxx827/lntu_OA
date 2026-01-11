@echo off
chcp 65001 >nul
echo ========================================
echo   OA系统 - 完整数据库初始化
echo ========================================
echo.

set MYSQL_USER=root
set MYSQL_PASS=123456
set MYSQL_HOST=localhost
set MYSQL_PORT=3306

echo [1/2] 正在初始化数据库...
mysql -u%MYSQL_USER% -p%MYSQL_PASS% -h%MYSQL_HOST% -P%MYSQL_PORT% < "%~dp0db\init_complete.sql"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [2/2] 数据库初始化成功！
    echo.
    echo ========================================
    echo   初始账号信息:
    echo   用户名: admin    密码: 123456
    echo   用户名: manager  密码: 123456
    echo   用户名: staff01  密码: 123456
    echo ========================================
) else (
    echo.
    echo [错误] 数据库初始化失败，请检查MySQL服务是否启动
)

echo.
pause
