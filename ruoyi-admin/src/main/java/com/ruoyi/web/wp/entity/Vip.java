package com.ruoyi.web.wp.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

@Data
public class Vip {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer status;
    private String cookie;
    private Date createTime;
    private Date updateTime;
}
