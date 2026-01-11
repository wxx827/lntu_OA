# OA后端部署指南 (Spring MVC + Oracle)

## 前置条件

### 1. 安装 Maven
```bash
# 下载 Maven 3.8+
# https://maven.apache.org/download.cgi

# 配置环境变量
setx MAVEN_HOME "C:\apache-maven-3.8.6"
setx PATH "%PATH%;%MAVEN_HOME%\bin"
```

### 2. 安装 Oracle 数据库
- Oracle 11g/12c/19c
- 默认端口: 1521
- SID: orcl

### 3. 安装 Tomcat 9
```bash
# 下载 Tomcat 9.x
# https://tomcat.apache.org/download-90.cgi
# 解压到: C:\apache-tomcat-9.0.xx
```

## 数据库配置

### 步骤1: 创建用户
```sql
-- 以 SYSDBA 身份连接
sqlplus sys/password@localhost:1521/orcl as sysdba

-- 创建用户
CREATE USER oa_system IDENTIFIED BY oa123456;
GRANT CONNECT, RESOURCE, DBA TO oa_system;
```

### 步骤2: 导入表结构
```bash
# 连接到新用户
sqlplus oa_system/oa123456@localhost:1521/orcl

# 执行脚本 (注意:需要将MySQL脚本转换为Oracle语法)
@db/schema_oracle.sql
```

### 步骤3: 修改连接配置
编辑 `src/main/resources/jdbc.properties`:
```properties
jdbc.url=jdbc:oracle:thin:@YOUR_HOST:1521:YOUR_SID
jdbc.username=oa_system
jdbc.password=oa123456
```

## 构建项目

### 方式1: 使用 Maven 命令行
```bash
cd oa-backend
mvn clean package -DskipTests
```

### 方式2: 使用 IDE (推荐)
1. 用 IntelliJ IDEA 打开项目
2. Maven -> Lifecycle -> package
3. 生成的 WAR 文件在 `target/oa-backend.war`

## 部署到 Tomcat

### 步骤1: 复制 WAR 文件
```bash
copy target\oa-backend.war C:\apache-tomcat-9.0.xx\webapps\
```

### 步骤2: 启动 Tomcat
```bash
cd C:\apache-tomcat-9.0.xx\bin
startup.bat
```

### 步骤3: 验证部署
打开浏览器访问:
```
http://localhost:8080/oa-backend/
```

## API 测试

### 测试车辆管理
```bash
# 获取车辆列表
curl http://localhost:8080/oa-backend/api/car/list

# 新增车辆
curl -X POST http://localhost:8080/oa-backend/api/car/add ^
  -H "Content-Type: application/json" ^
  -d "{\"carLicence\":\"京A88888\",\"brand\":\"奥迪\",\"carType\":\"轿车\",\"seats\":5}"
```

### 测试日程管理
```bash
curl http://localhost:8080/oa-backend/api/schedule/my-events
```

## 常见问题

### 问题1: Maven 未找到
**解决**: 确保 Maven 已添加到 PATH 环境变量,重启命令行

### 问题2: Oracle 连接失败
**检查**:
- Oracle 服务是否启动
- 监听器状态: `lsnrctl status`
- 防火墙设置

### 问题3: Tomcat 启动失败
**检查**:
- 端口 8080 是否被占用
- 查看日志: `logs/catalina.out`
- JAVA_HOME 是否配置

### 问题4: 404 Not Found
**检查**:
- WAR 文件是否成功部署到 webapps
- 访问路径是否正确: `/oa-backend/api/...`

## 下一步

1. 配置前端 CORS 地址
2. 导入测试数据
3. 配置 JWT 密钥
4. 启用 HTTPS (生产环境)
