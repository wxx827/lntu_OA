# OA系统最终执行方案 (v2.0)

**目标读者:** Codex (AI 开发者)
**任务目标:** 将当前的 OA 系统半成品重构为架构规范、逻辑真实、无 Mock 数据的企业级应用。

---

## 第一部分：架构重构与规范 (最高优先级)

### 1. 后端架构重组 (Backend Refactoring)
**现状:** 包结构混乱，`com.oa.service` 与 `com.oa.module.*` 混用。
**指令:**
1.  **统一包路径:** 所有业务代码必须移入 `com.oa.module.[模块名]`。
    *   将 `com.oa.service.CarService` -> `com.oa.module.administrative.service.CarService`
    *   将 `com.oa.service.ScheduleService` -> `com.oa.module.schedule.service.ScheduleService`
2.  **清理通用包:** `com.oa.service` 包下不应保留任何具体业务 Service，只保留通用接口（如果必要）。
3.  **ID 策略统一:** 
    *   **新模块 (会议/物资/车辆/工作流):** 实体类主键必须使用 `@TableId(type = IdType.ASSIGN_ID)` (生成 19 位数字 ID 或 UUID 字符串)，匹配数据库 `VARCHAR(32)` 或 `BIGINT` 定义。
    *   **必须检查:** 确保 `Controller` 接收 ID 参数的类型与 `Entity` 定义一致。

### 2. 标准化响应与异常
**指令:**
1.  **统一返回:** 所有 Controller 方法必须返回 `Result<T>`。
    *   `Result.success(data)`
    *   `Result.error(code, msg)`
2.  **全局异常:** 必须在 `com.oa.common.exception` 下实现 `GlobalExceptionHandler`，拦截 `RuntimeException` 并返回标准 JSON 错误格式，禁止直接抛出 500 堆栈信息给前端。

---

## 第二部分：去伪存真专项 (必须执行)

### 1. 工作台 (Dashboard) 去伪
**现状:** `Dashboard.vue` 中 "项目进度"、"团队成员" 是写死的 Mock 数据。
**执行任务:**
1.  **后端:** 在 `DashboardController` 中注入 `WorkflowService` 和 `SysUserService`。
    *   `getPendingTasks()`: 查询 `OA_WORKFLOW` 表中 `status = 'PENDING'` 且 `assignee = current_user` 的数量。
    *   `getTeamStatus()`: 查询 `SYS_USER` 表，简单返回前 5 个用户，状态默认 "在线"。
2.  **API:** 将 `/api/dashboard/statistics` 改为真实 SQL 统计 (`count(*)` from users/workflows)。
3.  **前端:** 删除 `const projects = [...]`，改为 `onMounted -> axios.get -> projects.value = res.data`。

### 2. 会议与物资列表去伪
**现状:** API 失败时会加载备用假数据 ("301会议室", "A4纸")。
**执行任务:**
1.  **彻底删除:** 在 `RoomList.vue` 和 `MaterialList.vue` 中，**删除所有** `catch` 块中的 Mock 数据赋值。
2.  **错误处理:** `catch` 块中只能写 `ElMessage.error('无法加载数据，请检查网络')` 和 `loading.value = false`。
3.  **空状态:** 如果数据库为空，页面应显示 Element Plus 的 `<el-empty description="暂无可用资源" />`。

---

## 第三部分：模块深度功能需求

### 3.1 会议管理 (Meeting) - 核心难点
**逻辑要求:**
1.  **冲突检测:** 预定接口 `addBooking` **必须**包含以下逻辑：
    ```java
    // 伪代码
    int count = meetingMapper.selectCount(new QueryWrapper<OaMeetingBook>()
        .eq("room_id", roomId)
        .and(w -> w
            .lt("start_time", newEndTime)
            .gt("end_time", newStartTime)
        )
        .ne("status", "CANCELLED") // 排除已取消的
    );
    if (count > 0) throw new BusinessException("该时间段会议室已被占用");
    ```
2.  **我的预定:** API `/api/meeting/book/my` 需支持按状态筛选 (已结束/进行中)。

### 3.2 流程审批 (Workflow) - 业务心脏
**逻辑要求:**
1.  **通用表设计:** 使用 `OA_WORKFLOW` (主表) + `OA_WORKFLOW_NODE` (审批节点表)。
2.  **发起流程:**
    *   前端提交申请 -> 后端保存业务数据 -> 创建 `OA_WORKFLOW` 记录 -> 创建第一个 `OA_WORKFLOW_NODE` (待审批)。
3.  **审批动作:**
    *   同意: 更新当前 Node 状态 -> 检查是否还有下一级 -> 若无，更新主表为 "APPROVED"。
    *   驳回: 更新当前 Node 为 "REJECTED" -> 更新主表为 "REJECTED"。

### 3.3 物资管理 (Inventory)
**逻辑要求:**
1.  **领用扣减:** 审批通过瞬间，**必须**调用 `MaterialService.decreaseStock(matId, count)`。
2.  **库存检查:** 发起申请时，若 `applyCount > currentStock`，直接拦截报错，不允许提交。

---

## 第四部分：数据库初始化 (Database Init)
**执行任务:**
1.  开发者必须提供 `init_data.sql`，预置至少：
    *   3个会议室 (大会议室, 小会议室, 培训室)。
    *   5种基础物资 (电脑, 纸, 笔)。
    *   2辆公车。
    *   3个初始用户 (admin, manager, staff01)。
2.  **禁止**让用户对着空系统发呆。

---

## 第五部分：交付验证 (Checklist)
Codex 完成工作后，用户应能通过以下路径验收：
1.  [ ] **登录** admin 账号。
2.  [ ] **工作台** 看到真实的 "待办: 0"。
3.  [ ] **会议室** 看到数据库预置的 3 个会议室。
4.  [ ] **物资** 看到 "A4纸" 库存 100。
5.  [ ] **预定** 提交一个 10:00-11:00 的会议预定 -> 成功。
6.  [ ] **冲突测试** 再次预定同一会议室 10:30-11:30 -> **报错** "已被占用"。
7.  [ ] **工作台** 看到 "待办" 数量可能有变化 (取决于流程设计)。
