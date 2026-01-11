# Codex 执行提示词集

以下提示词 (Prompts) 是为您精心设计的，可以直接复制发送给 Codex 或其他 AI 助手，以分阶段执行 `project_requirements.md` 中的任务。

---

## 阶段一：架构重构与基础修复 (Backend Foundation)

**Prompt 1: 重构包结构与 ID 策略**
```markdown
请根据 `project_requirements.md` 中的“第一部分：架构重构与规范”执行以下任务：

1.  **移动文件:** 将 `com.oa.service.CarService` 移动到 `com.oa.module.administrative.service` 包下，并同步移动实现类。
2.  **移动文件:** 将 `com.oa.service.ScheduleService` 移动到 `com.oa.module.schedule.service` 包下。
3.  **检查 ID 类型:** 检查 `OaMeetingBook`、`OaWorkflow`、`OaMaterial` 等新模块实体类，确保它们的主键使用 `@TableId(type = IdType.ASSIGN_ID)` (MyBatis-Plus 策略)，并且字段类型为 String (如果数据库是 VARCHAR) 或 Long (如果数据库是 BIGINT)。
4.  **全局异常:** 在 `com.oa.common.exception` 包下创建 `GlobalExceptionHandler`，使用 `@RestControllerAdvice` 统一捕获 `RuntimeException`，返回 `{code: 500, message: e.getMessage()}`。

请提供移动后的文件结构确认和 GlobalExceptionHandler 的代码。
```

## 阶段二：数据库数据初始化 (Database Init)

**Prompt 2: 生成初始化数据脚本**
```markdown
为了避免系统启动后数据为空，请根据 `project_requirements.md` 的“第四部分：数据库初始化”要求，编写一个 `init_data.sql` 脚本。

脚本需包含：
1.  **用户数据:** Admin, Manager, Staff 三个用户 (密码加密后的字符串)。
2.  **会议室数据:** 插入 3 个会议室 (大会议室-301, 小会议室-202, 培训教室-405)。
3.  **物资数据:** 插入 5 种常用办公物资 (A4纸, 黑色签字笔, 订书机等)，库存设置在 50-200 之间。
4.  **车辆数据:** 插入 2 辆公务车 (奥迪A6, 别克GL8)。

请确保 SQL 语句符合 MySQL 8.0 语法。
```

## 阶段三：核心业务逻辑实现 (Core Logic)

**Prompt 3: 实现会议预定冲突检测**
```markdown
请参考 `project_requirements.md` 中的“3.1 会议管理”要求，完善 `MeetingService.addBooking` 方法。

核心要求：
1.  **冲突检测:** 在保存预定前，使用 MyBatis-Plus `QueryWrapper` 查询数据库，检查 `roomId` 在 `startTime` 和 `endTime` 之间是否已有 **未取消** 的预定。
2.  **抛出异常:** 如果存在冲突，抛出自定义异常 `BusinessException("该会议室在此时段已被预订")`。
3.  **事务控制:** 确保方法上有 `@Transactional` 注解。

请给出修改后的 `MeetingServiceImpl.java` 代码。
```

**Prompt 4: 实现工作台真实数据聚合**
```markdown
请参考 `project_requirements.md` 中的“2.1 工作台去伪”要求，重构 `DashboardController`。

1.  **注入依赖:** 注入 `WorkflowService`, `SysUserService`, `MeetingService`。
2.  **getStatistics:** 返回真实的 `Map`，包含 `pendingTasks` (待办数), `onlineUsers` (在线人数, 可查登录日志或直接 count), `todayMeetings` (今日会议数)。
3.  **getProjects:** (可选) 如果没有项目表，可以建立一个静态的 List 返回，但必须通过 Service 层获取，为未来扩展留口子。

请给出重构后的 `DashboardController.java`。
```

## 阶段四：前端清理 Mock (Frontend Cleanup)

**Prompt 5: 前端去伪存真**
```markdown
请执行 `project_requirements.md` 中的“第二部分：去伪存真专项”对应的前端任务。

目标文件：
1.  `src/views/meeting/RoomList.vue`
2.  `src/views/material/MaterialList.vue`
3.  `src/views/Dashboard.vue`

**严格要求:**
1.  **删除 Mock:** 搜索并删除所有 `catch (e) { list = [MockData...] }` 的代码块。
2.  **错误提示:** 改为 `ElMessage.error('数据加载失败')` 并设置 `loading = false`。
3.  **空状态:** 确保 `<el-table>` 的父容器在 `list.length === 0` 且 `!loading` 时显示 `<el-empty>` 组件。

请提供修改后的 Vue 代码片段。
```
