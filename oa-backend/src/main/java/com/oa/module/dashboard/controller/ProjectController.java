package com.oa.module.dashboard.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.Result;
import com.oa.module.dashboard.entity.OaProject;
import com.oa.module.dashboard.mapper.OaProjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@Tag(name = "Project Management", description = "项目任务管理")
public class ProjectController {

    @Autowired
    private OaProjectMapper projectMapper;

    @GetMapping("/list")
    @Operation(summary = "获取项目列表")
    public Result<List<OaProject>> getList() {
        List<OaProject> projects = projectMapper.selectList(
            new LambdaQueryWrapper<OaProject>().orderByDesc(OaProject::getCreateTime)
        );
        return Result.success(projects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取项目详情")
    public Result<OaProject> getById(@PathVariable String id) {
        OaProject project = projectMapper.selectById(id);
        return Result.success(project);
    }

    @PostMapping("/add")
    @Operation(summary = "新增项目")
    public Result<String> add(@RequestBody OaProject project) {
        project.setCreateTime(new Date());
        project.setUpdateTime(new Date());
        if (project.getProgress() == null) {
            project.setProgress(0);
        }
        if (project.getStatus() == null) {
            project.setStatus("已计划");
        }
        if (project.getColor() == null) {
            project.setColor("#409eff");
        }
        if (project.getIcon() == null) {
            project.setIcon("Platform");
        }
        projectMapper.insert(project);
        return Result.success("项目创建成功");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "更新项目")
    public Result<String> update(@PathVariable String id, @RequestBody OaProject project) {
        project.setProjectId(id);
        project.setUpdateTime(new Date());
        projectMapper.updateById(project);
        return Result.success("项目更新成功");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除项目")
    public Result<String> delete(@PathVariable String id) {
        projectMapper.deleteById(id);
        return Result.success("项目删除成功");
    }

    @PutMapping("/progress/{id}")
    @Operation(summary = "更新项目进度")
    public Result<String> updateProgress(@PathVariable String id, @RequestBody OaProject project) {
        OaProject updateProject = new OaProject();
        updateProject.setProjectId(id);
        updateProject.setProgress(project.getProgress());
        updateProject.setUpdateTime(new Date());
        projectMapper.updateById(updateProject);
        return Result.success("进度更新成功");
    }
}
