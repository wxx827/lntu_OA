package com.oa.module.role.controller;

import com.oa.common.Result;
import com.oa.module.role.entity.OaRole;
import com.oa.module.role.mapper.OaRoleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/role")
@Tag(name = "Role Management", description = "Role Management API")
public class RoleController {

    @Autowired
    private OaRoleMapper roleMapper;

    @GetMapping("/list")
    @Operation(summary = "Get role list")
    public Result<List<OaRole>> getList() {
        return Result.success(roleMapper.selectList(null));
    }

    @PostMapping("/add")
    @Operation(summary = "Add role")
    public Result<String> add(@RequestBody OaRole role) {
        role.setCreateTime(new Date());
        roleMapper.insert(role);
        return Result.success("");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update role")
    public Result<String> update(@PathVariable String id, @RequestBody OaRole role) {
        role.setRoleId(id);
        roleMapper.updateById(role);
        return Result.success("");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete role")
    public Result<String> delete(@PathVariable String id) {
        roleMapper.deleteById(id);
        return Result.success("");
    }

    @GetMapping("/permissions")
    @Operation(summary = "Get permissions tree")
    public Result<List<Map<String, Object>>> getPermissions() {
        List<Map<String, Object>> tree = new ArrayList<>();
        
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("id", 1);
        dashboard.put("label", "工作台");
        List<Map<String, Object>> dashboardChildren = new ArrayList<>();
        dashboardChildren.add(createNode(11, "查看统计"));
        dashboardChildren.add(createNode(12, "查看动态"));
        dashboard.put("children", dashboardChildren);
        tree.add(dashboard);
        
        Map<String, Object> attendance = new HashMap<>();
        attendance.put("id", 2);
        attendance.put("label", "考勤管理");
        List<Map<String, Object>> attendanceChildren = new ArrayList<>();
        attendanceChildren.add(createNode(21, "打卡记录"));
        attendanceChildren.add(createNode(22, "请假审批"));
        attendanceChildren.add(createNode(23, "加班审批"));
        attendanceChildren.add(createNode(24, "出差审批"));
        attendanceChildren.add(createNode(25, "考勤统计"));
        attendance.put("children", attendanceChildren);
        tree.add(attendance);
        
        Map<String, Object> workflow = new HashMap<>();
        workflow.put("id", 3);
        workflow.put("label", "流程审批");
        List<Map<String, Object>> workflowChildren = new ArrayList<>();
        workflowChildren.add(createNode(31, "发起申请"));
        workflowChildren.add(createNode(32, "审批流程"));
        workflowChildren.add(createNode(33, "历史记录"));
        workflow.put("children", workflowChildren);
        tree.add(workflow);
        
        Map<String, Object> meeting = new HashMap<>();
        meeting.put("id", 4);
        meeting.put("label", "会议协作");
        List<Map<String, Object>> meetingChildren = new ArrayList<>();
        meetingChildren.add(createNode(41, "会议室预定"));
        meetingChildren.add(createNode(42, "我的会议"));
        meeting.put("children", meetingChildren);
        tree.add(meeting);
        
        Map<String, Object> material = new HashMap<>();
        material.put("id", 5);
        material.put("label", "资产物资");
        List<Map<String, Object>> materialChildren = new ArrayList<>();
        materialChildren.add(createNode(51, "物资管理"));
        materialChildren.add(createNode(52, "申请记录"));
        material.put("children", materialChildren);
        tree.add(material);
        
        Map<String, Object> hr = new HashMap<>();
        hr.put("id", 6);
        hr.put("label", "人力资源");
        List<Map<String, Object>> hrChildren = new ArrayList<>();
        hrChildren.add(createNode(61, "部门管理"));
        hrChildren.add(createNode(62, "员工档案"));
        hrChildren.add(createNode(63, "组织架构"));
        hr.put("children", hrChildren);
        tree.add(hr);
        
        Map<String, Object> finance = new HashMap<>();
        finance.put("id", 7);
        finance.put("label", "财务管理");
        List<Map<String, Object>> financeChildren = new ArrayList<>();
        financeChildren.add(createNode(71, "费用报销"));
        financeChildren.add(createNode(72, "报销审核"));
        finance.put("children", financeChildren);
        tree.add(finance);
        
        Map<String, Object> system = new HashMap<>();
        system.put("id", 8);
        system.put("label", "系统设置");
        List<Map<String, Object>> systemChildren = new ArrayList<>();
        systemChildren.add(createNode(81, "员工管理"));
        systemChildren.add(createNode(82, "角色权限"));
        systemChildren.add(createNode(83, "公告管理"));
        system.put("children", systemChildren);
        tree.add(system);
        
        return Result.success(tree);
    }
    
    private Map<String, Object> createNode(int id, String label) {
        Map<String, Object> node = new HashMap<>();
        node.put("id", id);
        node.put("label", label);
        return node;
    }
}