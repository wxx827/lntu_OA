@echo off
chcp 65001 >nul
title OA 系统启动器

echo ========================================
echo    OA 协同办公系统 - 快速启动
echo ========================================
echo.

:: 检查 MySQL 服务
echo [检查] MySQL 服务状态...
sc query MySQL84 | find "RUNNING" >nul
if %errorlevel% equ 0 (
    echo [√] MySQL 已运行
) else (
    echo [×] MySQL 未运行，正在启动...
    net start MySQL84
    if %errorlevel% neq 0 (
        echo [错误] MySQL 启动失败，请检查服务配置
        pause
        exit /b 1
    )
    echo [√] MySQL 启动成功
)

:: 等待 MySQL 就绪
echo [等待] MySQL 初始化...
timeout /t 3 /nobreak >nul

:: 启动后端服务
echo.
echo [启动] 后端服务...
cd /d "%~dp0oa-backend"
start "OA-后端" cmd /k "set MAVEN_OPTS=--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED && mvn tomcat7:run"

:: 启动前端服务
echo [启动] 前端服务...
cd /d "%~dp0oa-frontend"
start "OA-前端" cmd /k "npm run dev"

:: 完成
echo.
echo ========================================
echo [完成] 系统启动中，请等待约 1-2 分钟
echo ========================================
echo.
echo 访问地址:
echo   前端: http://localhost:5173
echo   后端: http://localhost:8080/oa-backend
echo.
echo 默认账号: Admin / 123456
echo.
echo 注意: 请保持所有窗口打开！
echo ========================================
echo.
pause
