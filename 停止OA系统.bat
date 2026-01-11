@echo off
chcp 65001 >nul
title 停止 OA 系统

echo ========================================
echo    停止 OA 系统所有服务
echo ========================================
echo.

:: 关闭所有 Java 进程（后端）
echo [关闭] 后端服务...
taskkill /F /FI "WINDOWTITLE eq OA-后端*" 2>nul
if %errorlevel% equ 0 (
    echo [√] 后端已关闭
) else (
    echo [!] 未找到运行中的后端服务
)

:: 关闭所有 Node 进程（前端）
echo [关闭] 前端服务...
taskkill /F /FI "WINDOWTITLE eq OA-前端*" 2>nul
if %errorlevel% equ 0 (
    echo [√] 前端已关闭
) else (
    echo [!] 未找到运行中的前端服务
)

echo.
echo ========================================
echo [完成] OA 系统已停止
echo ========================================
echo.
pause
