package com.oa.module.schedule.controller;

import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.meeting.mapper.OaMeetingBookMapper;
import com.oa.module.meeting.entity.OaMeetingBook;
import com.oa.module.workflow.mapper.OaWorkflowMapper;
import com.oa.module.workflow.entity.OaWorkflow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/schedule")
@Tag(name = "Schedule Management", description = "")
public class ScheduleController {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private OaMeetingBookMapper meetingBookMapper;
    
    @Autowired
    private OaWorkflowMapper workflowMapper;

    @GetMapping("/my-events")
    @Operation(summary = "")
    public Result<List<Map<String, Object>>> getMyEvents(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);
        
        List<Map<String, Object>> events = new ArrayList<>();
        
        // 1. ?
        List<OaMeetingBook> meetings = meetingBookMapper.selectList(
            new LambdaQueryWrapper<OaMeetingBook>()
                .eq(OaMeetingBook::getEmpId, userId)
        );
        for (OaMeetingBook meeting : meetings) {
            Map<String, Object> event = new HashMap<>();
            event.put("id", "M_" + meeting.getBookId());
            event.put("title", "? " + meeting.getTopic());
            event.put("start", meeting.getStartTime());
            event.put("end", meeting.getEndTime());
            event.put("type", "meeting");
            event.put("color", "#409EFF"); // ?
            events.add(event);
        }
        
        // 2. ?(ll-day event?
        List<OaWorkflow> tasks = workflowMapper.selectList(
            new LambdaQueryWrapper<OaWorkflow>()
                .eq(OaWorkflow::getCurrentApproverId, userId)
                .eq(OaWorkflow::getStatus, 0)
        );
        for (OaWorkflow task : tasks) {
            Map<String, Object> event = new HashMap<>();
            event.put("id", "T_" + task.getFlowId());
            event.put("title", "? " + task.getTitle());
            event.put("start", task.getCreateTime());
            event.put("allDay", true);
            event.put("type", "task");
            event.put("color", "#E6A23C"); // ?
            events.add(event);
        }
        
        // 3. ?(?'car' )
        // ??JSON flowData
        List<OaWorkflow> carFlows = workflowMapper.selectList(
            new LambdaQueryWrapper<OaWorkflow>()
                .eq(OaWorkflow::getInitiatorId, userId)
                .eq(OaWorkflow::getFlowType, "car")
                .eq(OaWorkflow::getStatus, 1)
        );
        for (OaWorkflow flow : carFlows) {
            Map<String, Object> event = new HashMap<>();
            event.put("id", "C_" + flow.getFlowId());
            event.put("title", "? " + flow.getTitle());
            event.put("start", flow.getCreateTime()); // lowData?
            event.put("allDay", true);
            event.put("type", "car");
            event.put("color", "#67C23A"); // ?
            events.add(event);
        }
        
        return Result.success(events);
    }
}