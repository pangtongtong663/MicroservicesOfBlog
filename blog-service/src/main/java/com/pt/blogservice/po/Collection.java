package com.pt.blogservice.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("collection")
public class Collection implements Serializable {
    @TableId(value = "record", type = IdType.AUTO)
    private Integer record;

    @TableField(value = "userId")
    private Integer userId;

    @TableField(value = "collectId")
    private Integer collectId;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime editTime;

    @TableField(value = "deleted")
    private Integer deleted;
}
