package com.oa.module.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "OA_MEETING_ROOM", autoResultMap = true)
public class OaMeetingRoom {
    @TableId(value = "ROOM_ID", type = IdType.ASSIGN_ID)
    @TableField("ROOM_ID")
    private String roomId;
    
    @TableField("ROOM_NAME")
    private String roomName;
    
    @TableField("LOCATION")
    private String location;
    
    @TableField("CAPACITY")
    private Integer capacity;
    
    @TableField("HAS_MEDIA")
    private Integer hasMedia; // 0:No, 1:Yes
    
    @TableField("STATUS")
    private Integer status; // 0:Free, 1:Occupied, 2:Maintenance
}