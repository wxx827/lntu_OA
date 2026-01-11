# OA系统后端 - MySQL快速启动指南

## 🚀 快速开始 (5分钟)

### 1. 安装 MySQL

**下载地址**: https://dev.mysql.com/downloads/mysql/

或使用包管理器:
```bash
# Windows (使用 Chocolatey)
choco install mysql

# 或下载安装包
# https://dev.mysql.com/downloads/installer/
```

**安装后设置密码**: 默认用户 `root`, 密码设为你自己的密码 (并修改 application.yml)

---

### 2. 创建数据库并导入

打开命令行:

```bash
# 登录 MySQL
mysql -u root -p
# 输入你的MySQL密码

# 创建数据库
CREATE DATABASE oa_system DEFAULT CHARACTER SET utf8mb4;

# 使用数据库
USE oa_system;

# 导入表结构
SOURCE c:/Users/wjx/Desktop/OA在线系统/db/schema_mysql.sql;

# 退出
EXIT;
```

---

### 3. 启动后端

```bash
cd c:\Users\wjx\Desktop\OA在线系统\oa-backend
mvn spring-boot:run
```

---

### 4. 测试接口

**访问 Swagger 文档**: http://localhost:8080/doc.html

**测试登录**:
```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"Admin\",\"password\":\"your_password\"}"
```

**预期响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

---

## 📊 数据库信息

**连接信息**:
- Host: `localhost:3306`
- Database: `oa_system`
- Username: `root`
- Password: `your_password`

**默认账号**:
- 用户名: `Admin`
- 密码: `your_password` (首次运行需在数据库中设置)
- EMP_ID: `E001`

**测试数据**:
- 2个部门 (D01, D02)
- 2个职位 (P01, P02)
- 1个员工 (E001)
- 2个会议室 (R101, R102)
- 2种物资 (M001, M002)

---

## 🔧 如果遇到问题

### MySQL 连接失败

检查 `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/oa_system?...
    username: root
    password: your_password  # 改成你的MySQL密码
```

### 端口冲突

如果 8080 端口被占用,修改 `application.yml`:
```yaml
server:
  port: 8081  # 改成其他端口
```

### Maven 依赖下载慢

使用阿里云镜像,编辑 `~/.m2/settings.xml`:
```xml
<mirror>
  <id>aliyun</id>
  <mirrorOf>central</mirrorOf>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

---

## 📱 前端对接

在 `oa-frontend` 中配置:

```javascript
// src/main.js 或 axios配置文件
axios.defaults.baseURL = 'http://localhost:8080';
```

---

## ✅ 验证清单

- [ ] MySQL 已安装并启动
- [ ] 数据库 `oa_system` 已创建
- [ ] `schema_mysql.sql` 已导入
- [ ] 后端启动成功 (看到 "Started OaSystemApplication")
- [ ] Swagger 文档可访问
- [ ] 登录接口测试通过

---

**状态**: ✅ MySQL版本已准备就绪,可以开始开发!
