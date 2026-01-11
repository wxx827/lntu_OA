# lntu_课设 - OA在线办公系统

辽宁工程技术大学综合课程设计项目 - 基于 Spring MVC + Vue 3 的企业级OA办公自动化系统。

## 技术栈

### 后端
- Spring MVC 5.3.20
- MyBatis-Plus 3.5.3
- MySQL 8.0
- JWT 认证

### 前端
- Vue 3.5
- Element Plus 2.13
- Vite 5
- Axios
- ECharts 6

## 功能模块

- 工作台仪表盘
- 会议室管理与预约
- 物资管理与申领
- 车辆管理
- 流程审批
- 费用报销
- 公告管理
- 考勤管理
- 日程管理
- 通讯录
- 网盘文件管理

## 快速开始

### 本地开发

1. **克隆项目**
```bash
git clone https://github.com/your-username/lntu_课设.git
cd lntu_课设
```

2. **配置数据库**
```bash
# 创建MySQL数据库
mysql -u root -p -e "CREATE DATABASE oa_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入数据
mysql -u root -p oa_system < db/init_complete.sql
```

3. **启动后端**
```bash
cd oa-backend
mvn tomcat7:run
```

4. **启动前端**
```bash
cd oa-frontend
npm install
npm run dev
```

5. **访问系统**
- 前端: http://localhost:5173
- 后端API: http://localhost:8080/api

### Docker部署

```bash
# 使用docker-compose一键部署
docker-compose up -d

# 访问系统
http://localhost
```

## 华为云部署指南

详见 [DEPLOY_HUAWEICLOUD.md](./DEPLOY_HUAWEICLOUD.md)

## 项目结构

```
OA在线系统/
├── oa-backend/          # 后端Spring MVC项目
│   ├── src/main/java/   # Java源码
│   └── src/main/resources/  # 配置文件
├── oa-frontend/         # 前端Vue3项目
│   ├── src/views/       # 页面组件
│   └── src/components/  # 公共组件
├── db/                  # 数据库脚本
├── docker-compose.yml   # Docker编排文件
└── README.md
```

## 默认账号

- 管理员: admin / admin123
- 普通用户: user / user123

## License

MIT License

## 贡献

欢迎提交 Issue 和 Pull Request！
