@echo off
chcp 65001 >nul
cls
echo ========================================
echo 仅启动前端服务
echo ========================================
echo.

cd /d "%~dp0oa-frontend"

echo 正在启动前端服务...
echo.

npm run dev

pause
