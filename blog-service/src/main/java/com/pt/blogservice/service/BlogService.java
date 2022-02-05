package com.pt.blogservice.service;

import com.pt.blogservice.dto.ModifyDto;
import com.pt.blogservice.po.Blog;
import com.pt.blogservice.po.Comment;
import com.pt.blogservice.po.Collection;

import java.util.List;

public interface BlogService {
    Blog write(Integer userId, String title, String content);

    Collection collect(Integer userId, Integer collectId);

    Comment comment(Integer userId, Integer commentedRecord, String content);

    Blog modify(ModifyDto blog);

    List<Blog> select(Integer userId);

    Blog like (int record);

    Blog view (int record);

    Blog deleteBlog (int record);

    Comment deleteComment (int record);

    Collection deleteCollection (int record);
}
