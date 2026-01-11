<div align="center">

# 🏢 LNTU_OA

**企业级OA办公自动化系统**

[![Spring MVC](https://img.shields.io/badge/Spring%20MVC-5.3.20-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.5-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

基于 Spring MVC + Vue 3 构建的现代化企业办公自动化系统

[功能特性](#-功能特性) • [技术栈](#-技术栈) • [快速开始](#-快速开始) • [项目结构](#-项目结构)

</div>

---

## ✨ 功能特性

<table>
<tr>
<td width="50%">

### 📊 工作台仪表盘
- 实时数据统计概览
- ECharts 交互式图表
- 待办事项与通知中心
- 快捷操作入口

### 📅 会议室管理
- 会议室预约系统
- 会议日程安排
- 资源分配管理
- 日历集成

### 🚗 车辆管理
- 车辆预约系统
- 司机调度分配
- 用车记录追踪
- 维护保养记录

</td>
<td width="50%">

### 📋 流程审批
- 自定义工作流设计
- 多级审批链
- 流程进度追踪
- 消息通知提醒

### 💰 财务报销
- 费用报销申请
- 发票管理
- 预算追踪
- 审批工作流

### 👥 考勤管理
- 打卡签到系统
- 请假申请管理
- 加班申请
- 考勤报表统计

</td>
</tr>
<tr>
<td width="50%">

### 📁 网盘系统
- 文件上传下载
- 文件夹管理
- 文件分享
- 存储配额管理

</td>
<td width="50%">

### 📢 公告管理
- 全公司公告发布
- 分类管理
- 已读追踪
- 优先级设置

</td>
</tr>
</table>

---

## 🛠 技术栈

### 后端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring MVC | 5.3.20 | Web框架 |
| MyBatis-Plus | 3.5.3 | ORM框架 |
| MySQL | 8.0 | 数据库 |
| JWT | - | 身份认证 |
| Knife4j | 3.0 | API文档 |

### 前端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue.js | 3.5 | 前端框架 |
| Element Plus | 2.13 | UI组件库 |
| Vite | 5.0 | 构建工具 |
| ECharts | 6.0 | 数据可视化 |
| Axios | - | HTTP客户端 |

---

## 🚀 快速开始

### 环境要求

```
JDK 8+  |  Maven 3.6+  |  Node.js 16+  |  MySQL 8.0+
```

### 1️⃣ 克隆项目

```bash
git clone https://github.com/wxx827/lntu_OA.git
cd lntu_OA
```

### 2️⃣ 数据库配置

```sql
CREATE DATABASE oa_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

```bash
mysql -u root -p oa_system < db/init_complete.sql
```

### 3️⃣ 后端配置

```bash
cd oa-backend/src/main/resources
cp application.yml.example application.yml
cp jdbc.properties.example jdbc.properties
# 编辑配置文件，填入数据库连接信息
```

### 4️⃣ 启动服务

**启动后端：**
```bash
cd oa-backend && mvn tomcat7:run
```

**启动前端：**
```bash
cd oa-frontend && npm install && npm run dev
```

### 5️⃣ 访问系统

| 服务 | 地址 |
|------|------|
| 🌐 前端页面 | http://localhost:5173 |
| 🔧 后端API | http://localhost:8080/api |
| 📚 API文档 | http://localhost:8080/doc.html |

---

## 🐳 Docker 部署

```bash
docker-compose up -d
```

---

## 🏗 项目结构

```
lntu_OA/
├── oa-backend/                    # Spring MVC 后端
│   ├── src/main/java/com/oa/
│   │   ├── common/                # 公共模块
│   │   ├── config/                # 配置类
│   │   └── module/                # 业务模块
│   │       ├── auth/              # 认证模块
│   │       ├── dashboard/         # 工作台
│   │       ├── attendance/        # 考勤管理
│   │       ├── workflow/          # 流程审批
│   │       └── finance/           # 财务管理
│   └── src/main/resources/
│       └── mapper/                # MyBatis XML
│
├── oa-frontend/                   # Vue 3 前端
│   └── src/
│       ├── views/                 # 页面组件
│       ├── components/            # 公共组件
│       ├── router/                # 路由配置
│       └── store/                 # 状态管理
│
├── db/                            # 数据库脚本
├── docker-compose.yml             # Docker编排
└── README.md
```

---

## 📄 开源协议

本项目采用 MIT 协议开源

---

<div align="center">

**辽宁工程技术大学 课程设计项目**

如果觉得有帮助，欢迎 ⭐ Star！

</div>
