package com.oa.module.notification.controller;

import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.notification.entity.OaNotification;
import com.oa.module.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
@Tag(name = "Notification Management", description = "")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/list")
    @Operation(summary = "")
    public Result<List<OaNotification>> getList(
            @RequestParam(required = false) String type,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);
        
        return Result.success(notificationService.getList(userId, type));
    }

    @PutMapping("/read/{id}")
    @Operation(summary = "")
    public Result<String> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return Result.success("");
    }

    @PutMapping("/read-all")
    @Operation(summary = "")
    public Result<String> markAllAsRead(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);
        
        notificationService.markAllAsRead(userId);
        return Result.success("");
    }

    @GetMapping("/unread-count")
    @Operation(summary = "")
    public Result<Long> getUnreadCount(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);
        
        return Result.success(notificationService.getUnreadCount(userId));
    }
}