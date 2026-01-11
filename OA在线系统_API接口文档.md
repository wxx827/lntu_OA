# OA在线系统 API接口文档

## 1. 文档说明

### 1.1 接口规范

- **基础URL**: `http://localhost:8080`
- **请求格式**: JSON
- **响应格式**: JSON
- **认证方式**: JWT Token (Bearer Token)
- **字符编码**: UTF-8

### 1.2 统一响应格式

```json
{
    "success": true,
    "code": 200,
    "message": "操作成功",
    "data": {}
}
```

### 1.3 公共错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权/Token无效 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 1.4 请求头说明

| 参数名 | 必填 | 说明 |
|--------|------|------|
| Authorization | 是(除登录注册外) | Bearer {token} |
| Content-Type | 是 | application/json |

---

## 2. 认证模块 (Auth)

### 2.1 用户登录

**请求路径**: `POST /api/auth/login`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

**请求示例**:
```json
{
    "username": "admin",
    "password": "123456"
}
```

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "message": "",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "role": "ADMIN",
        "username": "admin"
    }
}
```

### 2.2 用户注册

**请求路径**: `POST /api/auth/register`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |
| role | String | 否 | 角色 |
| sex | String | 否 | 性别 |
| tel | String | 否 | 电话 |
| email | String | 否 | 邮箱 |

**请求示例**:
```json
{
    "username": "zhangsan",
    "password": "123456",
    "role": "EMPLOYEE",
    "sex": "男",
    "tel": "13800138000",
    "email": "zhangsan@example.com"
}
```

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": "Registration successful"
}
```

### 2.3 获取用户信息

**请求路径**: `GET /api/auth/userInfo`

**请求头**: 需要Authorization

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": {
        "empId": "E001",
        "empName": "admin",
        "role": "ADMIN",
        "sex": "男",
        "tel": "13800138000",
        "email": "admin@example.com"
    }
}
```

---

## 3. 考勤模块 (Attendance)

### 3.1 上班打卡

**请求路径**: `POST /api/attendance/clock-in`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| location | String | 否 | 打卡位置 |

**响应示例**:
```json
{
    "success": true,
    "message": "Success",
    "data": {
        "id": 1,
        "userId": 1,
        "clockInTime": "2026-01-10 09:00:00",
        "location": "公司总部"
    }
}
```

### 3.2 下班打卡

**请求路径**: `POST /api/attendance/clock-out`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| location | String | 否 | 打卡位置 |

### 3.3 获取今日考勤

**请求路径**: `GET /api/attendance/today`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

### 3.4 获取考勤记录

**请求路径**: `GET /api/attendance/my-records`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| startDate | Date | 是 | 开始日期(yyyy-MM-dd) |
| endDate | Date | 是 | 结束日期(yyyy-MM-dd) |

### 3.5 请假申请

**请求路径**: `POST /api/attendance/leave/apply`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| leaveType | String | 是 | 请假类型(年假/事假/病假等) |
| startTime | Date | 是 | 开始时间 |
| endTime | Date | 是 | 结束时间 |
| reason | String | 是 | 请假原因 |

### 3.6 获取我的请假申请

**请求路径**: `GET /api/attendance/leave/my-requests`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

### 3.7 审批请假

**请求路径**: `POST /api/attendance/leave/approve`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| requestId | Long | 是 | 请假申请ID |
| approverId | Long | 是 | 审批人ID |
| status | String | 是 | 审批状态(approved/rejected) |
| remark | String | 否 | 审批备注 |

### 3.8 加班申请

**请求路径**: `POST /api/attendance/overtime/apply`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| startTime | Date | 是 | 开始时间 |
| endTime | Date | 是 | 结束时间 |
| reason | String | 是 | 加班原因 |

### 3.9 获取我的加班申请

**请求路径**: `GET /api/attendance/overtime/my-requests`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

### 3.10 审批加班

**请求路径**: `POST /api/attendance/overtime/approve`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| requestId | Long | 是 | 加班申请ID |
| approverId | Long | 是 | 审批人ID |
| status | String | 是 | 审批状态 |
| remark | String | 否 | 审批备注 |

### 3.11 出差申请

**请求路径**: `POST /api/attendance/trip/apply`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| destination | String | 是 | 出差目的地 |
| startTime | Date | 是 | 开始时间 |
| endTime | Date | 是 | 结束时间 |
| reason | String | 是 | 出差原因 |

### 3.12 获取我的出差申请

**请求路径**: `GET /api/attendance/trip/my-requests`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

### 3.13 审批出差

**请求路径**: `POST /api/attendance/trip/approve`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| tripId | Long | 是 | 出差申请ID |
| approverId | Long | 是 | 审批人ID |
| status | String | 是 | 审批状态 |
| remark | String | 否 | 审批备注 |

### 3.14 获取月度考勤汇总

**请求路径**: `GET /api/attendance/summary/{userId}/{yearMonth}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| yearMonth | String | 是 | 年月(如2026-01) |

**响应示例**:
```json
{
    "success": true,
    "data": {
        "userId": 1,
        "yearMonth": "2026-01",
        "workDays": 22,
        "actualDays": 20,
        "lateDays": 1,
        "leaveDays": 2
    }
}
```

### 3.15 获取考勤统计

**请求路径**: `GET /api/attendance/stats/{userId}/{yearMonth}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| yearMonth | String | 是 | 年月(如2026-01) |

---

## 4. 公告模块 (Announcement)

### 4.1 获取公告列表

**请求路径**: `GET /api/announcement/list`

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": [
        {
            "id": "1",
            "title": "关于春节放假的通知",
            "content": "...",
            "publisherId": "E001",
            "publisherName": "Admin",
            "publishTime": "2026-01-10 09:00:00",
            "isTop": 1,
            "status": 1
        }
    ]
}
```

### 4.2 获取最新公告

**请求路径**: `GET /api/announcement/latest`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limit | int | 否 | 数量限制(默认5) |

### 4.3 获取公告详情

**请求路径**: `GET /api/announcement/{id}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 公告ID |

### 4.4 保存公告

**请求路径**: `POST /api/announcement/save`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 否 | 公告ID(更新时必填) |
| title | String | 是 | 标题 |
| content | String | 是 | 内容 |
| isTop | Integer | 否 | 是否置顶(0否1是) |

### 4.5 删除公告

**请求路径**: `DELETE /api/announcement/{id}`

### 4.6 撤回公告

**请求路径**: `PUT /api/announcement/withdraw/{id}`

### 4.7 切换置顶状态

**请求路径**: `PUT /api/announcement/toggle-top/{id}`

---

## 5. 会议模块 (Meeting)

### 5.1 获取会议室列表

**请求路径**: `GET /api/meeting/room/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| capacity | Integer | 否 | 容纳人数筛选 |

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": [
        {
            "roomId": "R001",
            "roomName": "会议室301",
            "capacity": 20,
            "equipment": "投影仪,白板,视频会议",
            "status": 1
        }
    ]
}
```

### 5.2 预定会议室

**请求路径**: `POST /api/meeting/book/add`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roomId | String | 是 | 会议室ID |
| topic | String | 是 | 会议主题 |
| startTime | Date | 是 | 开始时间 |
| endTime | Date | 是 | 结束时间 |
| attendees | String | 否 | 参会人员 |

**请求示例**:
```json
{
    "roomId": "R001",
    "topic": "项目周会",
    "startTime": "2026-01-10 14:00:00",
    "endTime": "2026-01-10 15:00:00",
    "attendees": "张三,李四,王五"
}
```

### 5.3 获取我的预定

**请求路径**: `GET /api/meeting/book/my`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | Integer | 否 | 状态筛选 |

### 5.4 取消预定

**请求路径**: `PUT /api/meeting/book/cancel/{id}`

---

## 6. 工作流模块 (Workflow)

### 6.1 获取待办任务

**请求路径**: `GET /api/workflow/my-tasks`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | String | 否 | 关键词搜索 |
| priority | String | 否 | 优先级筛选 |

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": [
        {
            "flowId": "F001",
            "flowType": "leave",
            "title": "请假申请-张三",
            "priority": "normal",
            "status": 0,
            "initiatorId": "E002",
            "createTime": "2026-01-10 09:00:00"
        }
    ]
}
```

### 6.2 审批流程

**请求路径**: `POST /api/workflow/approve/{id}`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| action | String | 是 | 操作(pass/reject) |
| comment | String | 否 | 审批意见 |

### 6.3 获取流程详情

**请求路径**: `GET /api/workflow/detail/{id}`

### 6.4 获取我发起的流程

**请求路径**: `GET /api/workflow/my-initiated`

### 6.5 创建流程

**请求路径**: `POST /api/workflow/create`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| flowType | String | 是 | 流程类型(leave/overtime/trip/car等) |
| title | String | 是 | 标题 |
| priority | String | 否 | 优先级(low/normal/high) |
| flowData | String | 否 | 流程数据(JSON格式) |

### 6.6 获取草稿列表

**请求路径**: `GET /api/workflow/drafts`

### 6.7 保存草稿

**请求路径**: `POST /api/workflow/save-draft`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| flowType | String | 是 | 流程类型 |
| title | String | 是 | 标题 |
| flowData | String | 否 | 流程数据 |

### 6.8 删除草稿

**请求路径**: `DELETE /api/workflow/draft/{id}`

### 6.9 获取已办列表

**请求路径**: `GET /api/workflow/done-list`

---

## 7. 内部消息模块 (Message)

### 7.1 获取消息列表

**请求路径**: `GET /api/message/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| box | String | 否 | 邮箱类型(inbox/outbox/drafts,默认inbox) |

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": [
        {
            "id": "M001",
            "subject": "会议通知",
            "content": "...",
            "senderId": "E001",
            "senderName": "Admin",
            "receiverId": "E002",
            "isRead": 0,
            "createTime": "2026-01-10 09:00:00"
        }
    ]
}
```

### 7.2 获取消息详情

**请求路径**: `GET /api/message/detail/{id}`

### 7.3 发送消息

**请求路径**: `POST /api/message/send`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| receiverId | String | 是 | 收件人ID |
| subject | String | 是 | 主题 |
| content | String | 否 | 内容 |

### 7.4 标记为已读

**请求路径**: `PUT /api/message/read/{id}`

### 7.5 删除消息

**请求路径**: `DELETE /api/message/{id}`

### 7.6 获取未读消息数量

**请求路径**: `GET /api/message/unread/count`

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": {
        "count": 5
    }
}
```

---

## 8. 云盘模块 (Disk)

### 8.1 获取文件列表

**请求路径**: `GET /api/disk/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| parentId | String | 否 | 父目录ID(默认ROOT) |

### 8.2 创建文件夹

**请求路径**: `POST /api/disk/folder`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| fileName | String | 是 | 文件夹名称 |
| parentId | String | 否 | 父目录ID(默认ROOT) |

### 8.3 上传文件

**请求路径**: `POST /api/disk/upload`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| file | MultipartFile | 否 | 文件 |
| fileName | String | 否 | 文件名 |
| fileSize | Long | 否 | 文件大小 |
| parentId | String | 否 | 父目录ID |
| mimeType | String | 否 | 文件类型 |

### 8.4 删除文件

**请求路径**: `DELETE /api/disk/{fileId}`

### 8.5 获取文件详情

**请求路径**: `GET /api/disk/{fileId}`

### 8.6 获取存储空间使用情况

**请求路径**: `GET /api/disk/storage/usage`

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": {
        "usedSpace": 1073741824,
        "totalSpace": 10737418240,
        "usedPercentage": 10.0
    }
}
```

---

## 9. 人力资源模块 (HR)

### 9.1 获取部门列表

**请求路径**: `GET /api/hr/department/list`

**响应示例**:
```json
{
    "success": true,
    "data": [
        {
            "id": 1,
            "deptName": "技术研发部",
            "parentId": 0,
            "managerId": 1,
            "status": 1
        }
    ]
}
```

### 9.2 获取部门树

**请求路径**: `GET /api/hr/department/tree`

### 9.3 获取部门详情

**请求路径**: `GET /api/hr/department/{id}`

### 9.4 新增部门

**请求路径**: `POST /api/hr/department/add`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| deptName | String | 是 | 部门名称 |
| parentId | Long | 否 | 父部门ID |
| managerId | Long | 否 | 部门负责人ID |

### 9.5 更新部门

**请求路径**: `PUT /api/hr/department/update`

### 9.6 删除部门

**请求路径**: `DELETE /api/hr/department/{id}`

### 9.7 获取职位列表

**请求路径**: `GET /api/hr/position/list`

### 9.8 根据部门获取职位

**请求路径**: `GET /api/hr/position/dept/{deptId}`

### 9.9 新增职位

**请求路径**: `POST /api/hr/position/add`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionName | String | 是 | 职位名称 |
| deptId | Long | 是 | 所属部门ID |
| level | Integer | 否 | 职级 |

### 9.10 更新职位

**请求路径**: `PUT /api/hr/position/update`

### 9.11 删除职位

**请求路径**: `DELETE /api/hr/position/{id}`

### 9.12 获取员工档案

**请求路径**: `GET /api/hr/employee/profile/{userId}`

### 9.13 获取员工档案列表

**请求路径**: `GET /api/hr/employee/profile/list`

### 9.14 根据部门获取员工

**请求路径**: `GET /api/hr/employee/dept/{deptId}`

### 9.15 创建员工档案

**请求路径**: `POST /api/hr/employee/profile/create`

### 9.16 更新员工档案

**请求路径**: `PUT /api/hr/employee/profile/update`

---

## 10. 财务模块 (Finance)

### 10.1 提交报销申请

**请求路径**: `POST /api/finance/expense/submit`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| claim | Object | 是 | 报销单信息 |
| invoices | Array | 否 | 发票列表 |

### 10.2 获取我的报销申请

**请求路径**: `GET /api/finance/expense/my-claims`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

### 10.3 获取待审批报销

**请求路径**: `GET /api/finance/expense/pending`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| approverId | Long | 是 | 审批人ID |

### 10.4 审批报销

**请求路径**: `POST /api/finance/expense/approve`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| claimId | Long | 是 | 报销单ID |
| approverId | Long | 是 | 审批人ID |
| status | String | 是 | 审批状态 |
| remark | String | 否 | 审批备注 |

### 10.5 获取报销详情

**请求路径**: `GET /api/finance/expense/detail/{claimId}`

### 10.6 获取费用类型列表

**请求路径**: `GET /api/finance/expense-types`

---

## 11. 通知模块 (Notification)

### 11.1 获取通知列表

**请求路径**: `GET /api/notification/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | String | 否 | 通知类型 |

### 11.2 标记为已读

**请求路径**: `PUT /api/notification/read/{id}`

### 11.3 全部标记为已读

**请求路径**: `PUT /api/notification/read-all`

### 11.4 获取未读数量

**请求路径**: `GET /api/notification/unread-count`

---

## 12. 日程模块 (Schedule)

### 12.1 获取我的日程事件

**请求路径**: `GET /api/schedule/my-events`

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": [
        {
            "id": "M_001",
            "title": "会议 项目周会",
            "start": "2026-01-10 14:00:00",
            "end": "2026-01-10 15:00:00",
            "type": "meeting",
            "color": "#409EFF"
        },
        {
            "id": "T_001",
            "title": "待办 审批请假申请",
            "start": "2026-01-10 09:00:00",
            "allDay": true,
            "type": "task",
            "color": "#E6A23C"
        }
    ]
}
```

---

## 13. 仪表盘模块 (Dashboard)

### 13.1 获取统计数据

**请求路径**: `GET /api/dashboard/statistics`

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": {
        "pendingTasks": 5,
        "pendingApprovals": 3,
        "todayMeetings": 2,
        "userCount": 158,
        "workflowCount": 45
    }
}
```

### 13.2 获取最近活动

**请求路径**: `GET /api/dashboard/activities`

### 13.3 获取团队状态

**请求路径**: `GET /api/dashboard/team-status`

### 13.4 获取公告

**请求路径**: `GET /api/dashboard/announcements`

### 13.5 获取项目进度

**请求路径**: `GET /api/dashboard/projects`

### 13.6 发布活动

**请求路径**: `POST /api/dashboard/activity`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| title | String | 是 | 标题 |
| description | String | 否 | 描述 |
| type | String | 否 | 类型(默认primary) |
| userName | String | 否 | 用户名 |

---

## 14. 报表模块 (Report)

### 14.1 获取概览统计

**请求路径**: `GET /api/report/overview`

**响应示例**:
```json
{
    "success": true,
    "data": {
        "totalEmployees": 158,
        "activeProjects": 12,
        "pendingTasks": 45,
        "monthlyExpenses": 68500.00,
        "revenue": 1250000.00,
        "clientSatisfaction": 4.8
    }
}
```

### 14.2 获取考勤趋势图表

**请求路径**: `GET /api/report/chart/attendance`

### 14.3 获取部门分布图表

**请求路径**: `GET /api/report/chart/department`

### 14.4 获取项目状态雷达图

**请求路径**: `GET /api/report/chart/project-status`

### 14.5 获取漏斗图数据

**请求路径**: `GET /api/report/chart/funnel`

### 14.6 获取绩效数据

**请求路径**: `GET /api/report/chart/performance`

---

## 15. 物资模块 (Material)

### 15.1 获取物资列表

**请求路径**: `GET /api/material/list`

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": [
        {
            "matId": "MAT001",
            "matName": "办公椅",
            "category": "办公家具",
            "stock": 50,
            "unit": "把"
        }
    ]
}
```

### 15.2 申请物资

**请求路径**: `POST /api/material/apply`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| matId | String | 是 | 物资ID |
| quantity | Integer | 是 | 申请数量 |
| reason | String | 否 | 申请原因 |

### 15.3 获取我的申请

**请求路径**: `GET /api/material/apply/my`

### 15.4 审批申请

**请求路径**: `POST /api/material/approve/{id}`

---

## 16. 角色模块 (Role)

### 16.1 获取角色列表

**请求路径**: `GET /api/role/list`

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": [
        {
            "roleId": "R001",
            "roleName": "管理员",
            "roleCode": "ADMIN",
            "permissions": "1,2,3,4,5",
            "createTime": "2026-01-01 00:00:00"
        }
    ]
}
```

### 16.2 新增角色

**请求路径**: `POST /api/role/add`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roleName | String | 是 | 角色名称 |
| roleCode | String | 是 | 角色编码 |
| permissions | String | 否 | 权限ID列表 |

### 16.3 更新角色

**请求路径**: `PUT /api/role/update/{id}`

### 16.4 删除角色

**请求路径**: `DELETE /api/role/delete/{id}`

### 16.5 获取权限树

**请求路径**: `GET /api/role/permissions`

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": [
        {
            "id": 1,
            "label": "工作台",
            "children": [
                {"id": 11, "label": "查看统计"},
                {"id": 12, "label": "查看动态"}
            ]
        }
    ]
}
```

---

## 17. 个人中心模块 (Profile)

### 17.1 获取个人信息

**请求路径**: `GET /api/profile/info`

### 17.2 更新个人信息

**请求路径**: `PUT /api/profile/update`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| tel | String | 否 | 电话 |
| email | String | 否 | 邮箱 |

### 17.3 修改密码

**请求路径**: `PUT /api/profile/change-password`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| oldPassword | String | 是 | 旧密码 |
| newPassword | String | 是 | 新密码 |

### 17.4 获取活动日志

**请求路径**: `GET /api/profile/activity-log`

### 17.5 获取用户统计

**请求路径**: `GET /api/profile/stats`

---

## 18. 系统模块 (System)

### 18.1 获取员工列表

**请求路径**: `GET /api/system/employees`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | String | 否 | 搜索关键词 |

### 18.2 获取员工统计

**请求路径**: `GET /api/system/employees/stats`

### 18.3 获取员工详情

**请求路径**: `GET /api/system/employees/{id}`

### 18.4 获取部门树

**请求路径**: `GET /api/system/departments`

### 18.5 获取所有配置

**请求路径**: `GET /api/system/configs`

### 18.6 按分组获取配置

**请求路径**: `GET /api/system/configs/group/{group}`

### 18.7 获取单个配置

**请求路径**: `GET /api/system/configs/{key}`

### 18.8 更新配置

**请求路径**: `PUT /api/system/configs/{key}`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| value | String | 是 | 配置值 |

### 18.9 刷新配置缓存

**请求路径**: `POST /api/system/configs/refresh`

---

## 19. AI模块 (AI)

### 19.1 会议室推荐

**请求路径**: `GET /api/ai/meeting/recommend`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| peopleCount | Integer | 否 | 参会人数 |

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": {
        "roomName": "Meeting Room 301",
        "matchScore": 95,
        "reason": "Suitable for 24 people, rating 4.8/5"
    }
}
```

### 19.2 物资库存预测

**请求路径**: `GET /api/ai/material/prediction`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| materialId | String | 否 | 物资ID |

### 19.3 AI对话

**请求路径**: `POST /api/ai/chat`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| message | String | 是 | 用户消息 |

**响应示例**:
```json
{
    "success": true,
    "code": 200,
    "data": {
        "reply": "I'm here to assist you with meetings, materials, and more"
    }
}
```

---

## 20. 数据模型

### 20.1 员工 (SysEmployee)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| empId | String | 员工ID |
| empName | String | 员工姓名 |
| password | String | 密码(MD5加密) |
| role | String | 角色 |
| sex | String | 性别 |
| tel | String | 电话 |
| email | String | 邮箱 |

### 20.2 公告 (OaAnnouncement)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 公告ID |
| title | String | 标题 |
| content | String | 内容 |
| publisherId | String | 发布人ID |
| publisherName | String | 发布人姓名 |
| publishTime | Date | 发布时间 |
| isTop | Integer | 是否置顶(0否1是) |
| status | Integer | 状态(0草稿1已发布2已撤回) |

### 20.3 考勤记录 (OaAttendance)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 记录ID |
| userId | Long | 用户ID |
| clockInTime | Date | 上班打卡时间 |
| clockOutTime | Date | 下班打卡时间 |
| location | String | 打卡位置 |
| status | String | 状态(正常/迟到/早退) |

### 20.4 请假申请 (OaLeaveRequest)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 申请ID |
| userId | Long | 用户ID |
| leaveType | String | 请假类型 |
| startTime | Date | 开始时间 |
| endTime | Date | 结束时间 |
| reason | String | 请假原因 |
| status | String | 状态(pending/approved/rejected) |
| approverId | Long | 审批人ID |
| approveTime | Date | 审批时间 |
| remark | String | 审批备注 |

### 20.5 会议室 (OaMeetingRoom)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| roomId | String | 会议室ID |
| roomName | String | 会议室名称 |
| capacity | Integer | 容纳人数 |
| equipment | String | 设备 |
| status | Integer | 状态 |

### 20.6 会议预定 (OaMeetingBook)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| bookId | String | 预定ID |
| roomId | String | 会议室ID |
| empId | String | 预定人ID |
| topic | String | 会议主题 |
| startTime | Date | 开始时间 |
| endTime | Date | 结束时间 |
| attendees | String | 参会人员 |
| status | Integer | 状态 |

### 20.7 工作流 (OaWorkflow)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| flowId | String | 流程ID |
| flowType | String | 流程类型 |
| title | String | 标题 |
| priority | String | 优先级 |
| status | Integer | 状态(0待审批1已通过2已拒绝-1草稿) |
| initiatorId | String | 发起人ID |
| currentApproverId | String | 当前审批人ID |
| flowData | String | 流程数据(JSON) |
| createTime | Date | 创建时间 |

### 20.8 内部消息 (OaMessage)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 消息ID |
| subject | String | 主题 |
| content | String | 内容 |
| senderId | String | 发送人ID |
| senderName | String | 发送人姓名 |
| receiverId | String | 接收人ID |
| isRead | Integer | 是否已读(0否1是) |
| createTime | Date | 创建时间 |

### 20.9 云盘文件 (OaDiskFile)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| fileId | String | 文件ID |
| fileName | String | 文件名 |
| fileType | String | 文件类型(file/folder) |
| fileSize | Long | 文件大小(字节) |
| parentId | String | 父目录ID |
| ownerId | String | 所有者ID |
| mimeType | String | MIME类型 |
| createTime | Date | 创建时间 |

### 20.10 部门 (OaDepartment)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 部门ID |
| deptName | String | 部门名称 |
| parentId | Long | 父部门ID |
| managerId | Long | 部门负责人ID |
| status | Integer | 状态 |

### 20.11 角色 (OaRole)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| roleId | String | 角色ID |
| roleName | String | 角色名称 |
| roleCode | String | 角色编码 |
| permissions | String | 权限ID列表 |
| createTime | Date | 创建时间 |

---

## 附录

### A. 接口汇总表

| 模块 | 接口数量 | 基础路径 |
|------|----------|----------|
| 认证模块 | 3 | /api/auth |
| 考勤模块 | 15 | /api/attendance |
| 公告模块 | 7 | /api/announcement |
| 会议模块 | 4 | /api/meeting |
| 工作流模块 | 9 | /api/workflow |
| 内部消息模块 | 6 | /api/message |
| 云盘模块 | 6 | /api/disk |
| 人力资源模块 | 16 | /api/hr |
| 财务模块 | 6 | /api/finance |
| 通知模块 | 4 | /api/notification |
| 日程模块 | 1 | /api/schedule |
| 仪表盘模块 | 6 | /api/dashboard |
| 报表模块 | 6 | /api/report |
| 物资模块 | 4 | /api/material |
| 角色模块 | 5 | /api/role |
| 个人中心模块 | 5 | /api/profile |
| 系统模块 | 9 | /api/system |
| AI模块 | 3 | /api/ai |
| **总计** | **115** | - |

### B. 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0 | 2026-01-10 | 初始版本 |
