package com.oa.module.message.controller;

import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.message.entity.OaMessage;
import com.oa.module.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内部消息控制器
 */
@RestController
@RequestMapping("/api/message")
@Tag(name = "内部消息", description = "Internal Message Management")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired(required = false)
    private JwtUtil jwtUtil;
    
    /**
     * 获取消息列表 (收件箱/发件箱)
     */
    @GetMapping("/list")
    @Operation(summary = "获取消息列表")
    public Result<List<OaMessage>> getMessageList(
            @RequestParam(defaultValue = "inbox") String box,
            HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        
        List<OaMessage> messages;
        if ("outbox".equalsIgnoreCase(box)) {
            messages = messageService.getOutbox(userId);
        } else if ("drafts".equalsIgnoreCase(box)) {
            messages = messageService.getDrafts(userId);
        } else {
            // 默认为收件箱
            messages = messageService.getInbox(userId);
        }
        
        return Result.success(messages);
    }
    
    /**
     * 获取消息详情
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "获取消息详情")
    public Result<OaMessage> getDetail(
            @PathVariable String id,
            HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        OaMessage message = messageService.getDetail(id);
        
        if (message == null) {
            return Result.error(404, "消息不存在");
        }
        
        // 如果是接收者查看消息,自动标记为已读
        if (userId.equals(message.getReceiverId()) && message.getIsRead() == 0) {
            messageService.markAsRead(id);
            message.setIsRead(1);
        }
        
        return Result.success(message);
    }
    
    /**
     * 发送消息
     */
    @PostMapping("/send")
    @Operation(summary = "发送消息")
    public Result<String> sendMessage(
            @RequestBody OaMessage message,
            HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        
        // 设置发送者ID
        message.setSenderId(userId);
        
        // 验证必填字段
        if (message.getReceiverId() == null || message.getReceiverId().trim().isEmpty()) {
            return Result.error("收件人不能为空");
        }
        if (message.getSubject() == null || message.getSubject().trim().isEmpty()) {
            return Result.error("主题不能为空");
        }
        
        boolean success = messageService.sendMessage(message);
        return success ? Result.success("发送成功") : Result.error("发送失败");
    }
    
    /**
     * 标记消息为已读
     */
    @PutMapping("/read/{id}")
    @Operation(summary = "标记为已读")
    public Result<String> markAsRead(@PathVariable String id) {
        boolean success = messageService.markAsRead(id);
        return success ? Result.success("操作成功") : Result.error("操作失败");
    }
    
    /**
     * 删除消息
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除消息")
    public Result<String> deleteMessage(
            @PathVariable String id,
            HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        boolean success = messageService.deleteMessage(id, userId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread/count")
    @Operation(summary = "获取未读消息数量")
    public Result<Map<String, Object>> getUnreadCount(HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        Long count = messageService.getUnreadCount(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        
        return Result.success(result);
    }
    
    /**
     * 从请求中获取用户ID
     */
    private String getUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ") && jwtUtil != null) {
            return jwtUtil.extractUserId(authHeader.substring(7));
        }
        return "E001"; // 默认管理员
    }
}
