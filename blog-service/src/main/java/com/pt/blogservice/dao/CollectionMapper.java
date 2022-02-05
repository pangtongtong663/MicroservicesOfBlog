package com.pt.blogservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pt.blogservice.po.Collection;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {
}
