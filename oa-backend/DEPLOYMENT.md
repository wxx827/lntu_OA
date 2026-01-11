# OA系统后端部署指南

## 快速部署 (3步)

### 步骤1: 启动 MySQL

双击运行: `oa-backend/setup_mysql.bat`

或手动启动:
```bash
# 方式1: 使用服务
net start MySQL

# 方式2: 直接启动 mysqld
cd "C:\Program Files\MySQL\MySQL Server 8.4\bin"
.\mysqld.exe --console --datadir="c:\Users\wjx\Desktop\OA在线系统\mysql_data"
```

---

### 步骤2: 导入新模块数据库表

```bash
# 确保 MySQL 已启动后执行
mysql -u root -p oa_system < db/schema_new_modules.sql
# 输入密码: 123456 (或您设置的密码)
```

验证表是否创建成功:
```bash
mysql -u root -p oa_system -e "SHOW TABLES;"
```

应该看到10张表:
- OA_DOCUMENT ⭐ 新增
- OA_MAT_APPLY
- OA_MATERIAL
- OA_MEETING_BOOK
- OA_MEETING_ROOM
- OA_WORKFLOW ⭐ 新增
- OA_WORKFLOW_LOG ⭐ 新增
- SYS_DEPARTMENT
- SYS_EMPLOYEE
- SYS_POSITION

---

### 步骤3: 启动后端

```bash
cd oa-backend
mvn spring-boot:run
```

启动成功后会看到:
```
Started OaSystemApplication in X.XXX seconds
```

---

## 验证部署

### 1. 访问 API 文档
http://localhost:8080/doc.html

### 2. 测试登录
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"Admin\",\"password\":\"123456\"}"
```

### 3. 测试新接口

**文档管理**:
```bash
# 获取文件列表
curl http://localhost:8080/api/document/list \
  -H "Authorization: Bearer <token>"
```

**工作流**:
```bash
# 获取待办任务
curl http://localhost:8080/api/workflow/my-tasks \
  -H "Authorization: Bearer <token>"
```

**员工搜索**:
```bash
# 搜索员工
curl "http://localhost:8080/api/system/employee/search?keyword=Admin" \
  -H "Authorization: Bearer <token>"
```

---

## 常见问题

### Q1: MySQL 连接失败 (10061)

**原因**: MySQL 服务未启动

**解决**:
1. 检查进程: `tasklist | findstr mysqld`
2. 如果没有输出,启动 MySQL (见步骤1)

---

### Q2: 端口 8080 被占用

**解决**: 修改 `application.yml`
```yaml
server:
  port: 8081  # 改成其他端口
```

---

### Q3: 表已存在错误

**原因**: 之前已导入过

**解决**: 跳过此步骤,或删除后重新导入
```sql
DROP TABLE IF EXISTS OA_WORKFLOW_LOG;
DROP TABLE IF EXISTS OA_WORKFLOW;
DROP TABLE IF EXISTS OA_DOCUMENT;
```

---

## 完整 API 清单

### 核心模块 (29个接口)

| 模块 | 接口数 |
|------|--------|
| 认证 | 2 |
| 系统管理 | 6 |
| 会议管理 | 4 |
| 物资管理 | 4 |
| AI功能 | 3 |
| 文档管理 | 6 |
| 工作流 | 4 |

详见: `walkthrough.md`

---

## 下一步

1. ✅ 数据库表已就绪
2. ✅ 后端代码已完成
3. 🔄 启动后端服务
4. 🔄 前端对接测试

**状态**: 准备就绪,等待启动!
