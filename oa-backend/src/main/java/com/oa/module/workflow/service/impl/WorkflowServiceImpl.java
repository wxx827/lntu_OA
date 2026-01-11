package com.oa.module.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.exception.BusinessException;
import com.oa.module.system.entity.SysEmployee;
import com.oa.module.system.mapper.SysEmployeeMapper;
import com.oa.module.workflow.entity.OaWorkflow;
import com.oa.module.workflow.entity.OaWorkflowLog;
import com.oa.module.workflow.mapper.OaWorkflowLogMapper;
import com.oa.module.workflow.mapper.OaWorkflowMapper;
import com.oa.module.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private OaWorkflowMapper workflowMapper;
    
    @Autowired
    private OaWorkflowLogMapper logMapper;
    
    @Autowired(required = false)
    private SysEmployeeMapper employeeMapper;

    @Override
    public List<OaWorkflow> getMyTasks(String approverId, String keyword, String priority) {
        LambdaQueryWrapper<OaWorkflow> query = new LambdaQueryWrapper<>();
        query.eq(OaWorkflow::getCurrentApproverId, approverId)
             .eq(OaWorkflow::getStatus, 0); // 待审批
        
        if (keyword != null && !keyword.isEmpty()) {
            query.like(OaWorkflow::getTitle, keyword);
        }
        
        if (priority != null && !priority.isEmpty()) {
            query.eq(OaWorkflow::getPriority, priority);
        }
        
        query.orderByDesc(OaWorkflow::getCreateTime);
        List<OaWorkflow> list = workflowMapper.selectList(query);
        fillInitiatorName(list);
        return list;
    }
    
    // 填充发起人姓名
    private void fillInitiatorName(List<OaWorkflow> list) {
        if (list == null || list.isEmpty() || employeeMapper == null) return;
        for (OaWorkflow wf : list) {
            if (wf.getInitiatorId() != null) {
                try {
                    SysEmployee emp = employeeMapper.selectById(wf.getInitiatorId());
                    if (emp != null) {
                        wf.setInitiatorName(emp.getUserName());
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
            if (wf.getInitiatorName() == null) {
                wf.setInitiatorName("用户" + (wf.getInitiatorId() != null ? wf.getInitiatorId().substring(0, Math.min(4, wf.getInitiatorId().length())) : ""));
            }
        }
    }

    @Override
    @Transactional
    public void approve(String flowId, String approverId, String action, String comment) {
        OaWorkflow workflow = workflowMapper.selectById(flowId);
        if (workflow == null) {
            throw new BusinessException("");
        }
        
        if (!approverId.equals(workflow.getCurrentApproverId())) {
            throw new BusinessException("");
        }
        
        if (workflow.getStatus() != 0) {
            throw new BusinessException("");
        }
        
        // ?
        workflow.setStatus("pass".equals(action) ? 1 : 2);
        workflow.setUpdateTime(new Date());
        workflowMapper.updateById(workflow);
        
        // 
        OaWorkflowLog log = new OaWorkflowLog();
        log.setFlowId(flowId);
        log.setApproverId(approverId);
        log.setAction(action);
        log.setComment(comment);
        log.setActionTime(new Date());
        logMapper.insert(log);
    }

    @Override
    public OaWorkflow getDetail(String flowId) {
        return workflowMapper.selectById(flowId);
    }

    @Override
    public List<OaWorkflow> getMyInitiated(String initiatorId) {
        List<OaWorkflow> list = workflowMapper.selectList(new LambdaQueryWrapper<OaWorkflow>()
                .eq(OaWorkflow::getInitiatorId, initiatorId)
                .ne(OaWorkflow::getStatus, -1) // 排除草稿
                .orderByDesc(OaWorkflow::getCreateTime));
        fillInitiatorName(list);
        return list;
    }

    @Override
    @Transactional
    public OaWorkflow createWorkflow(String flowType, String title, String priority,
                                     String flowData, String initiatorId, String approverId) {
        OaWorkflow workflow = new OaWorkflow();
        workflow.setFlowType(flowType);
        workflow.setTitle(title);
        workflow.setPriority(priority);
        workflow.setFlowData(flowData);
        workflow.setInitiatorId(initiatorId);
        workflow.setCurrentApproverId(approverId);
        workflow.setStatus(0); // ?
        workflow.setCreateTime(new Date());
        workflow.setUpdateTime(new Date());
        
        workflowMapper.insert(workflow);
        return workflow;
    }

    @Override
    public List<OaWorkflow> getDrafts(String initiatorId) {
        return workflowMapper.selectList(new LambdaQueryWrapper<OaWorkflow>()
                .eq(OaWorkflow::getInitiatorId, initiatorId)
                .eq(OaWorkflow::getStatus, -1) // -1
                .orderByDesc(OaWorkflow::getUpdateTime));
    }

    @Override
    public void saveDraft(String flowType, String title, String flowData, String initiatorId) {
        OaWorkflow draft = new OaWorkflow();
        draft.setFlowType(flowType);
        draft.setTitle(title);
        draft.setFlowData(flowData);
        draft.setInitiatorId(initiatorId);
        draft.setStatus(-1); // ?
        draft.setCreateTime(new Date());
        draft.setUpdateTime(new Date());
        
        workflowMapper.insert(draft);
    }

    @Override
    public void deleteDraft(String draftId) {
        workflowMapper.deleteById(draftId);
    }

    @Override
    public List<OaWorkflow> getDoneList(String approverId) {
        List<OaWorkflow> list = workflowMapper.selectList(new LambdaQueryWrapper<OaWorkflow>()
                .eq(OaWorkflow::getCurrentApproverId, approverId)
                .in(OaWorkflow::getStatus, 1, 2) // 已通过或已驳回
                .orderByDesc(OaWorkflow::getUpdateTime));
        fillInitiatorName(list);
        return list;
    }
}