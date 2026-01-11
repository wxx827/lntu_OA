package com.oa.module.disk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.module.disk.entity.OaDiskFile;
import com.oa.module.disk.mapper.OaDiskFileMapper;
import com.oa.module.disk.service.DiskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 云盘文件服务实现类
 */
@Service
public class DiskServiceImpl extends ServiceImpl<OaDiskFileMapper, OaDiskFile> 
        implements DiskService {
    
    @Override
    public List<OaDiskFile> listFiles(String parentId, String ownerId) {
        return this.list(new LambdaQueryWrapper<OaDiskFile>()
                .eq(OaDiskFile::getParentId, parentId)
                .eq(OaDiskFile::getOwnerId, ownerId)
                .eq(OaDiskFile::getIsDeleted, 0)
                .orderByDesc(OaDiskFile::getFileType) // 文件夹优先
                .orderByDesc(OaDiskFile::getCreateTime));
    }
    
    @Override
    @Transactional
    public OaDiskFile createFolder(String folderName, String parentId, String ownerId) {
        OaDiskFile folder = new OaDiskFile();
        folder.setFileName(folderName);
        folder.setFileType("folder");
        folder.setFileSize(0L);
        folder.setParentId(parentId);
        folder.setOwnerId(ownerId);
        folder.setIsDeleted(0);
        
        // 构建文件路径
        String path = buildFilePath(parentId, folderName);
        folder.setFilePath(path);
        
        this.save(folder);
        return folder;
    }
    
    @Override
    @Transactional
    public OaDiskFile uploadFile(String fileName, Long fileSize, String parentId, String ownerId, String mimeType) {
        OaDiskFile file = new OaDiskFile();
        file.setFileName(fileName);
        file.setFileType("file");
        file.setFileSize(fileSize);
        file.setParentId(parentId);
        file.setOwnerId(ownerId);
        file.setMimeType(mimeType);
        file.setIsDeleted(0);
        
        // 构建文件路径
        String path = buildFilePath(parentId, fileName);
        file.setFilePath(path);
        
        this.save(file);
        return file;
    }
    
    @Override
    @Transactional
    public boolean deleteFile(String fileId) {
        OaDiskFile file = this.getById(fileId);
        if (file != null) {
            file.setIsDeleted(1);
            return this.updateById(file);
        }
        return false;
    }
    
    @Override
    public OaDiskFile getFileDetail(String fileId) {
        return this.getOne(new LambdaQueryWrapper<OaDiskFile>()
                .eq(OaDiskFile::getFileId, fileId)
                .eq(OaDiskFile::getIsDeleted, 0));
    }
    
    @Override
    public Long calculateUsedSpace(String ownerId) {
        List<OaDiskFile> files = this.list(new LambdaQueryWrapper<OaDiskFile>()
                .eq(OaDiskFile::getOwnerId, ownerId)
                .eq(OaDiskFile::getFileType, "file")
                .eq(OaDiskFile::getIsDeleted, 0));
        
        return files.stream()
                .mapToLong(f -> f.getFileSize() != null ? f.getFileSize() : 0L)
                .sum();
    }
    
    /**
     * 构建文件路径
     */
    private String buildFilePath(String parentId, String fileName) {
        if ("ROOT".equals(parentId) || parentId == null) {
            return "/" + fileName;
        }
        
        OaDiskFile parent = this.getById(parentId);
        if (parent != null && parent.getFilePath() != null) {
            return parent.getFilePath() + "/" + fileName;
        }
        
        return "/" + fileName;
    }
}
