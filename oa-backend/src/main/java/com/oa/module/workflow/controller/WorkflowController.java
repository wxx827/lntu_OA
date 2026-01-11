package com.oa.module.workflow.controller;

import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.workflow.entity.OaWorkflow;
import com.oa.module.workflow.service.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
@Tag(name = "Workflow Management", description = "")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/my-tasks")
    @Operation(summary = "")
    public Result<List<OaWorkflow>> getMyTasks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String priority,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String approverId = jwtUtil.extractUserId(token);
        
        List<OaWorkflow> tasks = workflowService.getMyTasks(approverId, keyword, priority);
        return Result.success(tasks);
    }

    @PostMapping("/approve/{id}")
    @Operation(summary = "")
    public Result<String> approve(
            @PathVariable String id,
            @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String approverId = jwtUtil.extractUserId(token);
        
        String action = params.get("action"); // pass or reject
        String comment = params.get("comment");
        
        workflowService.approve(id, approverId, action, comment);
        return Result.success("");
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "")
    public Result<OaWorkflow> getDetail(@PathVariable String id) {
        return Result.success(workflowService.getDetail(id));
    }

    @GetMapping("/my-initiated")
    @Operation(summary = "")
    public Result<List<OaWorkflow>> getMyInitiated(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String initiatorId = jwtUtil.extractUserId(token);
        
        return Result.success(workflowService.getMyInitiated(initiatorId));
    }

    @PostMapping("/create")
    @Operation(summary = "")
    public Result<OaWorkflow> createWorkflow(
            @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String initiatorId = jwtUtil.extractUserId(token);
        
        String flowType = params.get("flowType");
        String title = params.get("title");
        String priority = params.get("priority");
        String flowData = params.get("flowData");
        
        // ? E001
        String approverId = "E001";
        
        OaWorkflow workflow = workflowService.createWorkflow(
            flowType, title, priority, flowData, initiatorId, approverId
        );
        
        return Result.success(workflow);
    }

    @GetMapping("/drafts")
    @Operation(summary = "")
    public Result<List<OaWorkflow>> getDrafts(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String initiatorId = jwtUtil.extractUserId(token);
        
        return Result.success(workflowService.getDrafts(initiatorId));
    }

    @PostMapping("/save-draft")
    @Operation(summary = "")
    public Result<String> saveDraft(
            @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String initiatorId = jwtUtil.extractUserId(token);
        
        String flowType = params.get("flowType");
        String title = params.get("title");
        String flowData = params.get("flowData");
        
        workflowService.saveDraft(flowType, title, flowData, initiatorId);
        return Result.success("");
    }

    @DeleteMapping("/draft/{id}")
    @Operation(summary = "")
    public Result<String> deleteDraft(@PathVariable String id) {
        workflowService.deleteDraft(id);
        return Result.success("");
    }

    @GetMapping("/done-list")
    @Operation(summary = "")
    public Result<List<OaWorkflow>> getDoneList(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String approverId = jwtUtil.extractUserId(token);
        
        return Result.success(workflowService.getDoneList(approverId));
    }
}