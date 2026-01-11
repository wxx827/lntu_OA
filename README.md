# LNTU_OA - 企业级OA办公自动化系统

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20MVC-5.3.20-green?style=flat-square&logo=spring" alt="Spring MVC">
  <img src="https://img.shields.io/badge/Vue-3.5-brightgreen?style=flat-square&logo=vue.js" alt="Vue 3">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" alt="License">
</p>

<p align="center">
  <b>辽宁工程技术大学 综合课程设计项目</b><br>
  基于 Spring MVC + Vue 3 + Element Plus 的现代化企业办公自动化解决方案
</p>

---

## 项目概述

LNTU_OA 是一套功能完善的企业级OA办公自动化系统，涵盖日常办公所需的核心功能模块。系统采用前后端分离架构，后端基于Spring MVC + MyBatis-Plus，前端使用Vue 3 + Element Plus，支持Docker一键部署。

## 技术架构

```
┌─────────────────────────────────────────────────────────────┐
│                        Frontend                              │
│  Vue 3 + Vite + Element Plus + ECharts + Axios              │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      RESTful API                             │
│                    JWT Authentication                        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        Backend                               │
│  Spring MVC 5.3 + MyBatis-Plus 3.5 + Knife4j (Swagger)      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        Database                              │
│                     MySQL 8.0 + Redis                        │
└─────────────────────────────────────────────────────────────┘
```

## 功能模块

| 模块 | 功能描述 | 状态 |
|------|----------|------|
| **工作台** | 数据统计仪表盘、待办事项、公告通知 | ✅ |
| **会议管理** | 会议室预约、会议安排、资源管理 | ✅ |
| **物资管理** | 物资申领、库存管理、审批流程 | ✅ |
| **车辆管理** | 车辆预约、用车审批、车辆调度 | ✅ |
| **流程审批** | 自定义工作流、多级审批、流程追踪 | ✅ |
| **费用报销** | 报销申请、发票管理、财务审核 | ✅ |
| **考勤管理** | 打卡签到、请假申请、加班管理 | ✅ |
| **日程管理** | 个人日历、日程提醒、任务安排 | ✅ |
| **通讯录** | 组织架构、员工信息、部门管理 | ✅ |
| **网盘系统** | 文件上传下载、文件夹管理、分享 | ✅ |
| **公告系统** | 公告发布、分类管理、已读追踪 | ✅ |
| **消息中心** | 站内信、系统通知、消息推送 | ✅ |

## 快速开始

### 环境要求

- JDK 8+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+

### 1. 克隆项目

```bash
git clone https://github.com/wxx827/lntu_OA.git
cd lntu_OA
```

### 2. 数据库配置

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE oa_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入数据
mysql -u root -p oa_system < db/init_complete.sql
```

### 3. 后端配置

```bash
cd oa-backend/src/main/resources

# 复制配置模板
cp application.yml.example application.yml
cp jdbc.properties.example jdbc.properties

# 编辑配置文件，修改数据库连接信息
```

### 4. 启动后端

```bash
cd oa-backend
mvn tomcat7:run
```

### 5. 启动前端

```bash
cd oa-frontend
npm install
npm run dev
```

### 6. 访问系统

| 服务 | 地址 |
|------|------|
| 前端页面 | http://localhost:5173 |
| 后端API | http://localhost:8080/api |
| API文档 | http://localhost:8080/doc.html |

## Docker 部署

```bash
# 一键启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 停止服务
docker-compose down
```

## 项目结构

```
lntu_OA/
├── oa-backend/                 # 后端项目
│   ├── src/main/java/com/oa/
│   │   ├── common/             # 公共模块（Result、异常处理、工具类）
│   │   ├── config/             # 配置类（CORS、Security、Swagger）
│   │   └── module/             # 业务模块
│   │       ├── auth/           # 认证模块
│   │       ├── dashboard/      # 工作台
│   │       ├── attendance/     # 考勤管理
│   │       ├── workflow/       # 流程审批
│   │       ├── finance/        # 财务报销
│   │       ├── hr/             # 人事管理
│   │       └── ...
│   └── src/main/resources/
│       ├── mapper/             # MyBatis XML
│       └── application.yml     # 配置文件
│
├── oa-frontend/                # 前端项目
│   ├── src/
│   │   ├── views/              # 页面组件
│   │   ├── components/         # 公共组件
│   │   ├── router/             # 路由配置
│   │   ├── store/              # 状态管理
│   │   └── utils/              # 工具函数
│   └── vite.config.js
│
├── db/                         # 数据库脚本
│   ├── init_complete.sql       # 完整初始化脚本
│   └── schema_*.sql            # 各模块表结构
│
├── docker-compose.yml          # Docker编排
├── Dockerfile.backend          # 后端镜像
├── Dockerfile.frontend         # 前端镜像
└── nginx.conf                  # Nginx配置
```

## API 文档

系统集成 Knife4j (Swagger) 接口文档，启动后端后访问：

```
http://localhost:8080/doc.html
```

详细接口说明请参考 [OA在线系统_API接口文档.md](./OA在线系统_API接口文档.md)

## 系统截图

<details>
<summary>点击展开截图</summary>

### 登录页面
![Login](docs/screenshots/login.png)

### 工作台仪表盘
![Dashboard](docs/screenshots/dashboard.png)

### 考勤管理
![Attendance](docs/screenshots/attendance.png)

</details>

## 开发指南

### 代码规范

- 后端遵循阿里巴巴Java开发规范
- 前端遵循Vue 3 Composition API风格
- 使用ESLint + Prettier进行代码格式化

### 新增模块

1. 后端：在 `module/` 下创建新模块目录
2. 创建 `controller/`、`service/`、`mapper/`、`entity/` 子目录
3. 前端：在 `views/` 下创建对应页面
4. 配置路由和菜单

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 致谢

- [Spring Framework](https://spring.io/)
- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [MyBatis-Plus](https://baomidou.com/)

---

<p align="center">
  <sub>Made with ❤️ at LNTU</sub>
</p>
