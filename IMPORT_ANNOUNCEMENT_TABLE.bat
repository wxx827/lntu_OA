@echo off
chcp 65001 >nul
echo ========================================
echo 导入公告表到数据库
echo ========================================

set MYSQL_USER=root
set MYSQL_PASSWORD=123456
set MYSQL_DB=oa_system
set MYSQL_HOST=localhost
set MYSQL_PORT=3306

echo 正在导入公告表结构和数据...
mysql -h%MYSQL_HOST% -P%MYSQL_PORT% -u%MYSQL_USER% -p%MYSQL_PASSWORD% %MYSQL_DB% < db\schema_announcement.sql

if %errorlevel% equ 0 (
    echo.
    echo [成功] 公告表导入完成！
    echo.
) else (
    echo.
    echo [错误] 导入失败，请检查MySQL配置
    echo.
)

pause
