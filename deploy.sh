#!/bin/bash
# 华为云一键部署脚本
# 使用方法: chmod +x deploy.sh && ./deploy.sh

set -e

echo "=========================================="
echo "  OA系统 华为云部署脚本"
echo "=========================================="

# 检查Docker
if ! command -v docker &> /dev/null; then
    echo "正在安装Docker..."
    curl -fsSL https://get.docker.com | sh
    systemctl start docker
    systemctl enable docker
fi

# 检查docker-compose
if ! command -v docker-compose &> /dev/null; then
    echo "正在安装docker-compose..."
    curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
fi

# 创建环境配置
if [ ! -f .env ]; then
    echo "创建环境配置文件..."
    cp .env.example .env
    echo "请编辑 .env 文件配置数据库密码和域名"
fi

# 构建并启动
echo "正在构建并启动服务..."
docker-compose up -d --build

echo ""
echo "=========================================="
echo "  部署完成!"
echo "=========================================="
echo "前端访问: http://$(hostname -I | awk '{print $1}')"
echo "后端API: http://$(hostname -I | awk '{print $1}'):8080/api"
echo ""
echo "查看日志: docker-compose logs -f"
echo "停止服务: docker-compose down"
echo "=========================================="
