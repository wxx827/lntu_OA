# 华为云部署指南

## 一、准备工作

### 1.1 华为云资源准备

1. **购买ECS云服务器**
   - 推荐配置: 2核4G内存, 40G系统盘
   - 操作系统: Ubuntu 22.04 LTS 或 CentOS 7.9
   - 开放端口: 22(SSH), 80(HTTP), 443(HTTPS), 8080(后端API)

2. **购买RDS数据库(可选)**
   - 如果需要高可用，建议使用华为云RDS MySQL
   - 或者在ECS上自建MySQL

3. **购买域名并备案**
   - 在华为云购买域名
   - 完成ICP备案(中国大陆服务器必须)

### 1.2 本地准备

确保本地已安装:
- Git
- Node.js 18+
- Maven 3.8+
- JDK 8

## 二、服务器环境配置

### 2.1 连接服务器

```bash
ssh root@your-server-ip
```

### 2.2 安装Docker

```bash
# Ubuntu
curl -fsSL https://get.docker.com | sh
systemctl start docker
systemctl enable docker

# 安装docker-compose
curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
```

### 2.3 配置防火墙

```bash
# 开放端口
ufw allow 22
ufw allow 80
ufw allow 443
ufw allow 8080
ufw enable
```

## 三、部署步骤

### 3.1 上传项目到服务器

```bash
# 方式1: 从GitHub克隆
git clone https://github.com/your-username/oa-system.git /opt/oa-system

# 方式2: 使用scp上传
scp -r ./OA在线系统 root@your-server-ip:/opt/oa-system
```

### 3.2 配置环境变量

```bash
cd /opt/oa-system
cp .env.example .env
vim .env

# 修改以下配置
MYSQL_ROOT_PASSWORD=your_secure_password
DOMAIN_NAME=your-domain.com
```

### 3.3 启动服务

```bash
# 构建并启动所有服务
docker-compose up -d --build

# 查看日志
docker-compose logs -f

# 查看服务状态
docker-compose ps
```

## 四、域名配置

### 4.1 DNS解析

在华为云DNS控制台添加解析记录:
- 记录类型: A
- 主机记录: @ 或 www
- 记录值: 你的ECS公网IP

### 4.2 配置HTTPS (Let's Encrypt免费证书)

```bash
# 安装certbot
apt install certbot python3-certbot-nginx -y

# 获取证书
certbot --nginx -d your-domain.com -d www.your-domain.com

# 自动续期
certbot renew --dry-run
```

### 4.3 更新Nginx配置支持HTTPS

创建 `nginx-ssl.conf`:

```nginx
server {
    listen 80;
    server_name your-domain.com www.your-domain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name your-domain.com www.your-domain.com;

    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;
    
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://backend:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

## 五、GitHub开源

### 5.1 创建.gitignore

```bash
# 已创建，确保包含敏感文件
.env
*.log
node_modules/
target/
.idea/
*.iml
```

### 5.2 初始化Git仓库

```bash
cd /path/to/OA在线系统
git init
git add .
git commit -m "Initial commit: OA Office Automation System"
```

### 5.3 推送到GitHub

```bash
# 在GitHub创建新仓库后
git remote add origin https://github.com/your-username/oa-system.git
git branch -M main
git push -u origin main
```

## 六、运维命令

```bash
# 查看所有容器状态
docker-compose ps

# 重启服务
docker-compose restart

# 查看日志
docker-compose logs -f backend
docker-compose logs -f frontend

# 更新部署
git pull
docker-compose up -d --build

# 备份数据库
docker exec oa-mysql mysqldump -u root -p123456 oa_system > backup.sql

# 恢复数据库
docker exec -i oa-mysql mysql -u root -p123456 oa_system < backup.sql
```

## 七、常见问题

### Q1: 前端无法访问后端API
检查nginx配置中的proxy_pass地址是否正确

### Q2: 数据库连接失败
检查docker网络和环境变量配置

### Q3: HTTPS证书过期
运行 `certbot renew` 续期

## 八、监控建议

1. 使用华为云监控服务监控ECS资源
2. 配置日志收集到华为云LTS
3. 设置告警规则(CPU、内存、磁盘)
