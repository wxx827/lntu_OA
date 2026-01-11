@echo off
chcp 65001 >nul
echo ========================================
echo 导入内部消息表
echo ========================================

set MYSQL_USER=root
set MYSQL_PASS=123456
set DB_NAME=oa_system

echo 正在导入 schema_message.sql...
mysql -u%MYSQL_USER% -p%MYSQL_PASS% %DB_NAME% < db\schema_message.sql

if %ERRORLEVEL% EQU 0 (
    echo [成功] 内部消息表导入完成！
) else (
    echo [失败] 导入失败，请检查MySQL连接和文件路径
)

pause
