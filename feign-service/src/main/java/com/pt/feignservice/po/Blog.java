package com.pt.feignservice.po;

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
public class Blog implements Serializable {
    private Integer record;

    private Integer userId;

    private String title;

    private String content;

    private Integer likes;

    private Integer views;

    private LocalDateTime createTime;

    private LocalDateTime editTime;

    private Integer deleted;
}
