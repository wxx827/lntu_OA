@echo off
chcp 65001 >nul
cls
echo ========================================
echo 导入公告管理表到数据库
echo ========================================
echo.

set "MYSQL_BIN=C:\Program Files\MySQL\MySQL Server 8.4\bin"

echo 正在导入公告表...
echo.
echo 请输入 MySQL root 密码:
set /p MYSQL_PASSWORD=

echo.
echo 正在执行SQL脚本...
"%MYSQL_BIN%\mysql.exe" -u root -p%MYSQL_PASSWORD% oa_system < "%~dp0db\schema_announcement.sql"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo [错误] 导入失败
    echo ========================================
    echo.
    echo 可能的原因:
    echo 1. MySQL密码错误
    echo 2. oa_system 数据库不存在
    echo 3. SQL文件路径错误
    echo.
    echo 请检查后重试
) else (
    echo.
    echo ========================================
    echo [成功] 公告表导入完成！
    echo ========================================
    echo.
    echo 已创建表: OA_ANNOUNCEMENT
    echo 已插入示例数据: 3条公告
    echo.
    echo 下一步:
    echo 1. 重启后端服务 (.\START_BACKEND_ONLY.bat)
    echo 2. 刷新浏览器查看效果
)

echo.
pause
