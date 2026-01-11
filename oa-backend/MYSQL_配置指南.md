# MySQL 快速配置指南

## 方案1: 使用自动脚本 (推荐)

双击运行: `oa-backend/setup_mysql.bat`

脚本会自动:
1. 检查 MySQL 是否安装
2. 创建 `oa_system` 数据库
3. 导入表结构
4. 显示配置信息

---

## 方案2: 手动配置

### 1. 查找 MySQL 安装路径

打开 PowerShell,运行:
```powershell
Get-Command mysql
```

或查看常见路径:
- `C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe`
- `C:\ProgramData\MySQL\MySQL Server 8.4\bin\mysql.exe`

### 2. 添加到环境变量 (如果 mysql 命令不可用)

```powershell
# 临时添加 (本次会话有效)
$env:Path += ";C:\Program Files\MySQL\MySQL Server 8.4\bin"

# 或永久添加
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Program Files\MySQL\MySQL Server 8.4\bin", "User")
```

### 3. 初始化 MySQL (首次安装)

```bash
# 如果 MySQL 要求设置 root 密码
# 运行 MySQL 配置向导
"C:\Program Files\MySQL\MySQL Server 8.4\bin\mysqld.exe" --initialize-insecure
```

### 4. 创建数据库

```bash
# 方式1: 使用命令行
mysql -u root -p

# 进入 MySQL 后执行:
CREATE DATABASE oa_system DEFAULT CHARACTER SET utf8mb4;
USE oa_system;
SOURCE c:/Users/wjx/Desktop/OA在线系统/db/schema_mysql.sql;
EXIT;
```

```bash
# 方式2: 一行命令
mysql -u root -p -e "CREATE DATABASE oa_system;"
mysql -u root -p oa_system < c:/Users/wjx/Desktop/OA在线系统/db/schema_mysql.sql
```

---

## 方案3: 使用 MySQL Workbench (图形界面)

1. 下载安装: https://dev.mysql.com/downloads/workbench/
2. 打开 MySQL Workbench
3. 连接到 localhost
4. 执行 SQL:
   ```sql
   CREATE DATABASE oa_system;
   USE oa_system;
   ```
5. File → Run SQL Script → 选择 `schema_mysql.sql`

---

## 验证安装

```bash
# 检查数据库是否创建成功
mysql -u root -p -e "SHOW DATABASES;"

# 检查表是否创建成功
mysql -u root -p oa_system -e "SHOW TABLES;"

# 应该看到:
# OA_MAT_APPLY
# OA_MATERIAL
# OA_MEETING_BOOK
# OA_MEETING_ROOM
# SYS_DEPARTMENT
# SYS_EMPLOYEE
# SYS_POSITION
```

---

## 常见问题

### Q: "mysql 不是内部或外部命令"

**解决**: 添加 MySQL 到环境变量 (见上方步骤2)

### Q: "Access denied for user 'root'"

**解决**: 
```bash
# 重置 root 密码
mysqld --skip-grant-tables
mysql -u root
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;
```

### Q: 找不到 MySQL 服务

**解决**:
```bash
# 启动 MySQL 服务
net start MySQL
# 或
net start MySQL84
```

---

## 下一步

配置完成后:
1. 修改 `application.yml` 中的密码 (如果不是 `root`)
2. 启动后端: `cd oa-backend && mvn spring-boot:run`
3. 访问: http://localhost:8080/doc.html
