package com.oa.module.disk.controller;

import com.oa.common.Result;
import com.oa.common.utils.JwtUtil;
import com.oa.module.disk.entity.OaDiskFile;
import com.oa.module.disk.service.DiskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 云盘文件管理控制器
 */
@RestController
@RequestMapping("/api/disk")
@Tag(name = "云盘管理", description = "Cloud Disk Management")
public class DiskController {
    
    @Autowired
    private DiskService diskService;
    
    @Autowired(required = false)
    private JwtUtil jwtUtil;
    
    /**
     * 获取文件列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取文件列表")
    public Result<List<OaDiskFile>> listFiles(
            @RequestParam(defaultValue = "ROOT") String parentId,
            HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        List<OaDiskFile> files = diskService.listFiles(parentId, userId);
        return Result.success(files);
    }
    
    /**
     * 创建文件夹
     */
    @PostMapping("/folder")
    @Operation(summary = "创建文件夹")
    public Result<OaDiskFile> createFolder(
            @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        String folderName = params.get("fileName");
        String parentId = params.getOrDefault("parentId", "ROOT");
        
        if (folderName == null || folderName.trim().isEmpty()) {
            return Result.error("文件夹名称不能为空");
        }
        
        OaDiskFile folder = diskService.createFolder(folderName, parentId, userId);
        return Result.success("创建成功", folder);
    }
    
    /**
     * 上传文件 (占位符实现)
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<OaDiskFile> uploadFile(
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) Long fileSize,
            @RequestParam(defaultValue = "ROOT") String parentId,
            @RequestParam(required = false) String mimeType,
            HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        
        // 如果是真实文件上传
        if (file != null && !file.isEmpty()) {
            fileName = file.getOriginalFilename();
            fileSize = file.getSize();
            mimeType = file.getContentType();
            // TODO: 实际文件存储逻辑 (保存到服务器磁盘或云存储)
        }
        
        // 如果是模拟上传 (前端测试用)
        if (fileName == null || fileName.trim().isEmpty()) {
            return Result.error("文件名不能为空");
        }
        
        if (fileSize == null) {
            fileSize = 0L;
        }
        
        OaDiskFile uploadedFile = diskService.uploadFile(fileName, fileSize, parentId, userId, mimeType);
        return Result.success("上传成功", uploadedFile);
    }
    
    /**
     * 删除文件或文件夹
     */
    @DeleteMapping("/{fileId}")
    @Operation(summary = "删除文件")
    public Result<String> deleteFile(@PathVariable String fileId) {
        boolean success = diskService.deleteFile(fileId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    /**
     * 获取文件详情
     */
    @GetMapping("/{fileId}")
    @Operation(summary = "获取文件详情")
    public Result<OaDiskFile> getFileDetail(@PathVariable String fileId) {
        OaDiskFile file = diskService.getFileDetail(fileId);
        if (file == null) {
            return Result.error(404, "文件不存在");
        }
        return Result.success(file);
    }
    
    /**
     * 获取存储空间使用情况
     */
    @GetMapping("/storage/usage")
    @Operation(summary = "获取存储空间使用情况")
    public Result<Map<String, Object>> getStorageUsage(HttpServletRequest request) {
        String userId = getUserIdFromRequest(request);
        Long usedSpace = diskService.calculateUsedSpace(userId);
        Long totalSpace = 10L * 1024 * 1024 * 1024; // 10GB 总空间
        
        Map<String, Object> usage = new HashMap<>();
        usage.put("usedSpace", usedSpace);
        usage.put("totalSpace", totalSpace);
        usage.put("usedPercentage", totalSpace > 0 ? (usedSpace * 100.0 / totalSpace) : 0);
        
        return Result.success(usage);
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
