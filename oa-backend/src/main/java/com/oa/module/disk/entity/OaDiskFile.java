package com.oa.module.disk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 云盘文件实体类
 */
@Data
@TableName("OA_DISK_FILE")
public class OaDiskFile {
    
    @TableId(value = "FILE_ID", type = IdType.ASSIGN_ID)
    private String fileId;
    
    private String fileName;
    
    private String fileType;  // 'file' or 'folder'
    
    private Long fileSize;    // File size in bytes
    
    private String parentId;  // Parent folder ID, 'ROOT' for top-level
    
    private String ownerId;   // Owner employee ID
    
    private String filePath;  // Full path for display
    
    private String mimeType;  // MIME type for files
    
    private Integer isDeleted; // 0:Active, 1:Deleted
    
    private Date createTime;
    
    private Date updateTime;
}
