@echo off
chcp 65001 >nul
cls
echo ========================================
echo OA 系统一键启动
echo ========================================
echo.

REM 设置环境变量
set "MAVEN_BIN=C:\apache-maven-3.9.6\bin"
set "PROJECT_DIR=%~dp0"
set PATH=%MAVEN_BIN%;%PATH%
set "MAVEN_OPTS=--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED"

echo [1/3] 启动 MySQL...
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo MySQL 已在运行
) else (
    echo 正在启动 MySQL 进程...
    start "MySQL Server" /MIN "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysqld.exe" --console
    echo 等待 MySQL 启动...
    timeout /t 10 /nobreak >nul
)
echo MySQL 已就绪

echo.
echo [2/3] 启动后端服务...
cd /d "%PROJECT_DIR%oa-backend"
start "OA-Backend" cmd /k "%MAVEN_BIN%\mvn.cmd tomcat7:run"
echo 后端启动中，请等待约 30-60 秒...

echo.
echo [3/3] 启动前端服务...
cd /d "%PROJECT_DIR%oa-frontend"
start "OA-Frontend" cmd /k "npm run dev"
echo 前端启动中，请等待约 10 秒...

echo.
echo ========================================
echo 启动完成！
echo ========================================
echo.
echo 请等待约 1-2 分钟让所有服务完全启动
echo.
echo 访问地址:
echo   前端: http://localhost:5173
echo   后端API文档: http://localhost:8080/oa-backend/doc.html
echo.
echo 默认账号: Admin / 123456
echo.
echo 注意: 保持所有窗口打开！
echo.
pause
