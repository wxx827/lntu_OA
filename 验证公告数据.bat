@echo off
chcp 65001 >nul
cls
echo ========================================
echo 验证公告表数据
echo ========================================
echo.

set "MYSQL_BIN=C:\Program Files\MySQL\MySQL Server 8.4\bin"

echo 请输入 MySQL root 密码:
set /p MYSQL_PASSWORD=

echo.
echo 正在查询公告表数据...
echo.

"%MYSQL_BIN%\mysql.exe" -u root -p%MYSQL_PASSWORD% oa_system -e "SELECT id, title, type, status, is_top, publisher_name FROM OA_ANNOUNCEMENT;"

echo.
echo ========================================
echo 查询完成
echo ========================================
echo.
pause
