package com.pt.blogservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pt.blogservice.po.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}
