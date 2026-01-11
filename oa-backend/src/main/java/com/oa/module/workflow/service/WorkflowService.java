package com.oa.module.workflow.service;

import com.oa.module.workflow.entity.OaWorkflow;

import java.util.List;

public interface WorkflowService {
    List<OaWorkflow> getMyTasks(String approverId, String keyword, String priority);
    
    void approve(String flowId, String approverId, String action, String comment);
    
    OaWorkflow getDetail(String flowId);
    
    List<OaWorkflow> getMyInitiated(String initiatorId);
    
    OaWorkflow createWorkflow(String flowType, String title, String priority, 
                              String flowData, String initiatorId, String approverId);
    
    List<OaWorkflow> getDrafts(String initiatorId);
    
    void saveDraft(String flowType, String title, String flowData, String initiatorId);
    
    void deleteDraft(String draftId);
    
    List<OaWorkflow> getDoneList(String approverId);
}