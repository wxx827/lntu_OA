package com.oa.module.disk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.module.disk.entity.OaDiskFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 云盘文件服务接口
 */
public interface DiskService extends IService<OaDiskFile> {
    
    /**
     * 获取指定目录下的文件和文件夹列表
     */
    List<OaDiskFile> listFiles(String parentId, String ownerId);
    
    /**
     * 创建文件夹
     */
    OaDiskFile createFolder(String folderName, String parentId, String ownerId);
    
    /**
     * 上传文件 (占位符 - 实际文件上传逻辑待实现)
     */
    OaDiskFile uploadFile(String fileName, Long fileSize, String parentId, String ownerId, String mimeType);
    
    /**
     * 删除文件或文件夹 (软删除)
     */
    boolean deleteFile(String fileId);
    
    /**
     * 获取文件详情
     */
    OaDiskFile getFileDetail(String fileId);
    
    /**
     * 计算用户已使用的存储空间
     */
    Long calculateUsedSpace(String ownerId);
}
