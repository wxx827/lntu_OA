@echo off
chcp 65001 >nul
cls
echo ========================================
echo 导入考勤管理表
echo ========================================
echo.

REM 查找MySQL
set "MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
if not exist "%MYSQL_PATH%" (
    set "MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe"
)
if not exist "%MYSQL_PATH%" (
    set "MYSQL_PATH=C:\mysql\bin\mysql.exe"
)

if not exist "%MYSQL_PATH%" (
    echo 未找到MySQL，请手动执行 db\schema_attendance_mysql.sql
    echo.
    echo 或者使用Navicat等工具导入该SQL文件
    pause
    exit /b 1
)

echo 正在导入考勤管理表...
echo.
"%MYSQL_PATH%" -u root -p123456 oa_system < "%~dp0db\schema_attendance_mysql.sql"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo 导入成功！
    echo ========================================
    echo.
    echo 已创建以下表：
    echo - OA_ATTENDANCE (考勤记录)
    echo - OA_LEAVE_REQUEST (请假申请)
    echo - OA_OVERTIME_REQUEST (加班申请)
    echo - OA_BUSINESS_TRIP (出差申请)
    echo - OA_ATTENDANCE_RULE (考勤规则)
    echo - OA_ATTENDANCE_SUMMARY (考勤汇总)
    echo.
) else (
    echo.
    echo 导入失败，错误代码: %ERRORLEVEL%
    echo 请检查MySQL密码或手动执行SQL
    echo.
)

pause
