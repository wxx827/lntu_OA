package com.oa.module.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "OA_MEETING_BOOK", autoResultMap = true)
public class OaMeetingBook {
    @TableId(value = "BOOK_ID", type = IdType.ASSIGN_ID)
    @TableField("BOOK_ID")
    private String bookId;
    
    @TableField("ROOM_ID")
    private String roomId;
    
    @TableField("EMP_ID")
    private String empId;
    
    @TableField("TOPIC")
    private String topic;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("START_TIME")
    private Date startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("END_TIME")
    private Date endTime;
    
    @TableField("STATUS")
    private Integer status; // 0: Pending, 1: Approved, 2: Rejected
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("CREATE_TIME")
    private Date createTime;
}