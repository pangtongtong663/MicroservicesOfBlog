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
@TableName("blog")
public class Blog implements Serializable {

    @TableId(value = "record", type = IdType.AUTO)
    private Integer record;

    @TableField(value = "userId")
    private Integer userId;

    @TableField(value = "title")
    private String title;

    @TableField(value = "content")
    private String content;

    @TableField(value = "likes")
    private Integer likes;

    @TableField(value = "views")
    private Integer views;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime editTime;

    @TableField(value = "deleted")
    private Integer deleted;
}
