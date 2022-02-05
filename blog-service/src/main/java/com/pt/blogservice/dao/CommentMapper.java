package com.pt.blogservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pt.blogservice.po.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
