package com.pt.feignservice.fallback;

import com.pt.feignservice.dto.*;
import com.pt.feignservice.po.Blog;
import com.pt.feignservice.service.BlogService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlogServiceFallback implements BlogService {

    @Override
    public String write(BlogDto blogDto) {
        return "创建失败！";
    }

    @Override
    public String collect(CollectDto collectDto) {
        return "收藏失败！";
    }

    @Override
    public String comment(CommentDto commentDto) {
        return "评论失败！";
    }

    @Override
    public String modify(ModifyDto modifyDto) {
        return "修改失败！";
    }

    @Override
    public List<Blog> select(Integer userId) {
        return null;
    }

    @Override
    public String look(LookDto lookDto) {
        if (lookDto.getLike().equals("true")){
            return "点赞失败!";
        } else {
            return "浏览失败!";
        }
    }

    @Override
    public String deleteBlog(Integer record) {
        return "删除失败！";
    }

    @Override
    public String deleteComment(Integer record) {
        return "删除失败！";
    }

    @Override
    public String deleteCollection(Integer record) {
        return "删除失败！";
    }
}
