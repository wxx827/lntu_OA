@echo off
chcp 65001 >nul
cls
echo ========================================
echo 仅启动后端服务
echo ========================================
echo.

set "MAVEN_BIN=C:\apache-maven-3.9.6\bin"
set PATH=%MAVEN_BIN%;%PATH%
set "MAVEN_OPTS=--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED"

cd /d "%~dp0oa-backend"

echo 正在启动后端服务...
echo 请等待约 30-60 秒...
echo.

"%MAVEN_BIN%\mvn.cmd" tomcat7:run

pause
