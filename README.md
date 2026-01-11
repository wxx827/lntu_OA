<div align="center">

# 🏢 LNTU_OA

**Enterprise Office Automation System**

[![Spring MVC](https://img.shields.io/badge/Spring%20MVC-5.3.20-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.5-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

A modern, full-featured Office Automation system built with Spring MVC and Vue 3.

[Features](#-features) • [Tech Stack](#-tech-stack) • [Quick Start](#-quick-start) • [Screenshots](#-screenshots) • [Architecture](#-architecture)

</div>

---

## ✨ Features

<table>
<tr>
<td width="50%">

### 📊 Dashboard & Analytics
- Real-time statistics overview
- Interactive charts with ECharts
- Task & notification center
- Quick action shortcuts

### 📅 Meeting Management
- Room reservation system
- Meeting scheduling
- Resource allocation
- Calendar integration

### 🚗 Vehicle Management
- Fleet booking system
- Driver assignment
- Usage tracking
- Maintenance records

</td>
<td width="50%">

### 📋 Workflow & Approval
- Custom workflow designer
- Multi-level approval chains
- Process tracking
- Email notifications

### 💰 Finance & Expense
- Expense claim submission
- Invoice management
- Budget tracking
- Approval workflow

### 👥 HR & Attendance
- Clock in/out system
- Leave management
- Overtime requests
- Attendance reports

</td>
</tr>
<tr>
<td width="50%">

### 📁 Cloud Drive
- File upload & download
- Folder management
- File sharing
- Storage quota

</td>
<td width="50%">

### 📢 Announcements
- Company-wide notices
- Category management
- Read tracking
- Priority levels

</td>
</tr>
</table>

---

## 🛠 Tech Stack

### Backend
| Technology | Version | Description |
|------------|---------|-------------|
| Spring MVC | 5.3.20 | Web framework |
| MyBatis-Plus | 3.5.3 | ORM framework |
| MySQL | 8.0 | Database |
| JWT | - | Authentication |
| Knife4j | 3.0 | API documentation |

### Frontend
| Technology | Version | Description |
|------------|---------|-------------|
| Vue.js | 3.5 | Frontend framework |
| Element Plus | 2.13 | UI component library |
| Vite | 5.0 | Build tool |
| ECharts | 6.0 | Data visualization |
| Axios | - | HTTP client |

---

## 🚀 Quick Start

### Prerequisites

```bash
JDK 8+  |  Maven 3.6+  |  Node.js 16+  |  MySQL 8.0+
```

### 1️⃣ Clone Repository

```bash
git clone https://github.com/wxx827/lntu_OA.git
cd lntu_OA
```

### 2️⃣ Database Setup

```sql
CREATE DATABASE oa_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

```bash
mysql -u root -p oa_system < db/init_complete.sql
```

### 3️⃣ Backend Configuration

```bash
cd oa-backend/src/main/resources
cp application.yml.example application.yml
cp jdbc.properties.example jdbc.properties
# Edit files with your database credentials
```

### 4️⃣ Start Services

**Backend:**
```bash
cd oa-backend && mvn tomcat7:run
```

**Frontend:**
```bash
cd oa-frontend && npm install && npm run dev
```

### 5️⃣ Access Application

| Service | URL |
|---------|-----|
| 🌐 Frontend | http://localhost:5173 |
| 🔧 Backend API | http://localhost:8080/api |
| 📚 API Docs | http://localhost:8080/doc.html |

---

## 🐳 Docker Deployment

```bash
docker-compose up -d
```

---

## 📸 Screenshots

<details>
<summary><b>Click to expand</b></summary>

| Dashboard | Meeting Room |
|-----------|--------------|
| ![Dashboard](docs/img/dashboard.png) | ![Meeting](docs/img/meeting.png) |

| Workflow | Attendance |
|----------|------------|
| ![Workflow](docs/img/workflow.png) | ![Attendance](docs/img/attendance.png) |

</details>

---

## 🏗 Architecture

```
lntu_OA/
├── oa-backend/                    # Spring MVC Backend
│   ├── src/main/java/com/oa/
│   │   ├── common/                # Utils, Result, Exceptions
│   │   ├── config/                # CORS, Security, Swagger
│   │   └── module/                # Business Modules
│   │       ├── auth/              # Authentication
│   │       ├── dashboard/         # Dashboard
│   │       ├── attendance/        # Attendance
│   │       ├── workflow/          # Workflow
│   │       ├── finance/           # Finance
│   │       └── ...
│   └── src/main/resources/
│       └── mapper/                # MyBatis XML
│
├── oa-frontend/                   # Vue 3 Frontend
│   └── src/
│       ├── views/                 # Page Components
│       ├── components/            # Shared Components
│       ├── router/                # Vue Router
│       └── store/                 # Pinia Store
│
├── db/                            # SQL Scripts
├── docker-compose.yml             # Docker Compose
└── README.md
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**Made with ❤️ by LNTU Students**

⭐ Star this repo if you find it helpful!

</div>
