package com.oa.module.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("OA_ACTIVITY")
public class OaActivity {
    @TableId
    private String id;
    private String title;
    private String description;
    private String type;
    private String userId;
    private String userName;
    private Date createTime;
}
