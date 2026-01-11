package com.oa.module.announcement.controller;

import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.announcement.entity.OaAnnouncement;
import com.oa.module.announcement.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 公告管理控制器
 */
@RestController
@RequestMapping("/api/announcement")
@Tag(name = "公告管理", description = "Announcement Management")
public class AnnouncementController {
    
    @Autowired
    private AnnouncementService announcementService;
    
    @Autowired(required = false)
    private JwtUtil jwtUtil;
    
    /**
     * 获取所有已发布的公告列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取公告列表")
    public Result<List<OaAnnouncement>> getList() {
        List<OaAnnouncement> list = announcementService.getPublishedList();
        return Result.success(list);
    }
    
    /**
     * 获取最新公告(用于Dashboard展示)
     */
    @GetMapping("/latest")
    @Operation(summary = "获取最新公告")
    public Result<List<OaAnnouncement>> getLatest(@RequestParam(defaultValue = "5") int limit) {
        List<OaAnnouncement> list = announcementService.getLatestAnnouncements(limit);
        return Result.success(list);
    }
    
    /**
     * 获取公告详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取公告详情")
    public Result<OaAnnouncement> getDetail(@PathVariable String id) {
        OaAnnouncement announcement = announcementService.getDetail(id);
        if (announcement == null) {
            return Result.error(404, "公告不存在");
        }
        return Result.success(announcement);
    }
    
    /**
     * 创建/更新公告
     */
    @PostMapping("/save")
    @Operation(summary = "保存公告")
    public Result<String> save(@RequestBody OaAnnouncement announcement, HttpServletRequest request) {
        // 获取当前用户信息
        String userId = getUserIdFromRequest(request);
        
        if (announcement.getId() == null) {
            // 新建公告,设置发布人信息
            announcement.setPublisherId(userId);
            // TODO: 从用户服务获取用户名,这里暂时使用ID
            announcement.setPublisherName("Admin");
        }
        
        boolean success = announcementService.publishAnnouncement(announcement);
        return success ? Result.success("保存成功") : Result.error(500, "保存失败");
    }
    
    /**
     * 删除公告
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除公告")
    public Result<String> delete(@PathVariable String id) {
        boolean success = announcementService.removeById(id);
        return success ? Result.success("删除成功") : Result.error(500, "删除失败");
    }
    
    /**
     * 撤回公告
     */
    @PutMapping("/withdraw/{id}")
    @Operation(summary = "撤回公告")
    public Result<String> withdraw(@PathVariable String id) {
        boolean success = announcementService.withdrawAnnouncement(id);
        return success ? Result.success("撤回成功") : Result.error(500, "撤回失败");
    }
    
    /**
     * 置顶/取消置顶
     */
    @PutMapping("/toggle-top/{id}")
    @Operation(summary = "切换置顶状态")
    public Result<String> toggleTop(@PathVariable String id) {
        boolean success = announcementService.toggleTop(id);
        return success ? Result.success("操作成功") : Result.error(500, "操作失败");
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
