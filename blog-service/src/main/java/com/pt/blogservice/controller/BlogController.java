package com.pt.blogservice.controller;

import com.pt.blogservice.dao.BlogMapper;
import com.pt.blogservice.dao.CollectionMapper;
import com.pt.blogservice.dao.CommentMapper;
import com.pt.blogservice.dto.*;
import com.pt.blogservice.po.Blog;
import com.pt.blogservice.po.Collection;
import com.pt.blogservice.po.Comment;
import com.pt.blogservice.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/blog", produces = "application/json")
public class BlogController {
    @Autowired
    BlogService blogService;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    CommentMapper commentMapper;

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String write(@RequestBody BlogDto blogDto) {
        Blog blog = blogService.write(blogDto.getUserId(), blogDto.getTitle(), blogDto.getContent());
        blogMapper.insert(blog);
        return "博客已发布！";
    }

    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public String collect(@RequestBody CollectDto collectDto) {
        Collection collection = blogService.collect(collectDto.getUserId(), collectDto.getCollectId());
        collectionMapper.insert(collection);
        return "收藏成功！";
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public String comment(@RequestBody CommentDto commentDto) {
        Comment comment = blogService.comment(commentDto.getUserId(), commentDto.getCommentedRecord(), commentDto.getContent());
        commentMapper.insert(comment);
        return "评论成功！";
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public String modify(@RequestBody ModifyDto modifyDto) {
        blogService.modify(modifyDto);
        return "修改成功！";
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public List<Blog> select(Integer userId) {
        return blogService.select(userId);
    }

    @RequestMapping(value = "/look", method = RequestMethod.POST)
    public String look(@RequestBody LookDto lookDto) {
        if ("true".equals(lookDto.getLike())) {
            blogService.like(lookDto.getRecord());
            return "点赞成功!";
        } else {
            blogService.view(lookDto.getRecord());
            return "已浏览!";
        }
    }

    @RequestMapping(value = "/delete-blog", method = RequestMethod.POST)
    public String deleteBlog(Integer record) {
        blogService.deleteBlog(record);
        return "文章删除成功";
    }

    @RequestMapping(value = "/delete-comment", method = RequestMethod.POST)
    public String deleteComment(Integer record) {
        blogService.deleteComment(record);
        return "评论删除成功";
    }

    @RequestMapping(value = "/delete-collection", method = RequestMethod.POST)
    public String deleteCollection(Integer record) {
        blogService.deleteCollection(record);
        return "收藏已取消！";
    }


}
