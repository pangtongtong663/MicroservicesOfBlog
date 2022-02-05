package com.pt.blogservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pt.blogservice.dao.BlogMapper;
import com.pt.blogservice.dao.CommentMapper;
import com.pt.blogservice.dao.CollectionMapper;
import com.pt.blogservice.dao.UserMapper;
import com.pt.blogservice.dto.ModifyDto;
import com.pt.blogservice.po.Blog;
import com.pt.blogservice.po.Comment;
import com.pt.blogservice.po.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService{
    @Autowired
    UserMapper userMapper;

    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    BlogMapper blogMapper;

    @Override
    public Blog write(Integer userId, String title, String content) {
        Blog blog = Blog.builder().userId(userId).title(title).content(content).views(0).likes(0).build();
        blogMapper.insert(blog);
        return blog;
    }

    @Override
    public Collection collect(Integer userId, Integer collectId) {
        Collection collection = Collection.builder().userId(userId).collectId(collectId).build();
        collectionMapper.insert(collection);
        return collection;
    }

    @Override
    public Comment comment(Integer userId, Integer commentedRecord, String content) {
        Comment comment = Comment.builder().userId(userId).commentedRecord(commentedRecord).content(content).build();
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public Blog modify(ModifyDto modifyDto) {
        UpdateWrapper<Blog>  uw = new UpdateWrapper<>();
        uw.eq("record", modifyDto.getRecord()).set("title", modifyDto.getTitle());
        uw.eq("record", modifyDto.getRecord()).set("content", modifyDto.getContent());
        Blog blog = blogMapper.selectById(modifyDto.getRecord());
        blog.setEditTime(null);
        blogMapper.update(blog, uw);
        return blog;
    }

    @Override
    public List<Blog> select(Integer userId) {
            QueryWrapper<Blog> qw = new QueryWrapper<>();
            qw.eq("userId", userId);
            List<Blog> list = blogMapper.selectList(qw);
            return list;
    }

    @Override
    public Blog like(int record) {
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("record", record);
        List<Blog> list = blogMapper.selectList(qw);
        Blog pre = list.get(0);
        Blog current = Blog.builder().record(record).userId(pre.getUserId()).content(pre.getContent())
                .likes(pre.getLikes() + 1).views(pre.getViews() + 1).build();
        blogMapper.updateById(current);
        return current;
    }

    @Override
    public Blog view(int record) {
        QueryWrapper<Blog> qw = new QueryWrapper<>();
        qw.eq("record", record);
        List<Blog> list = blogMapper.selectList(qw);
        Blog pre = list.get(0);
        Blog current = Blog.builder().record(record).userId(pre.getUserId()).title(pre.getTitle()).content(pre.getContent())
                .likes(pre.getLikes()).views(pre.getViews() + 1).build();
        blogMapper.updateById(current);
        return current;
    }

    @Override
    public Blog deleteBlog(int record) {
        UpdateWrapper<Blog> uw = new UpdateWrapper<>();
        uw.eq("record", record).set("deleted", 1);
        Blog blog = blogMapper.selectById(record);
        blog.setEditTime(null);
        blogMapper.update(blog, uw);
        return blog;
    }

    @Override
    public Comment deleteComment(int record) {
        UpdateWrapper<Comment> uw = new UpdateWrapper<>();
        uw.eq("record", record).set("deleted", 1);
        Comment comment = commentMapper.selectById(record);
        comment.setEditTime(null);
        commentMapper.update(comment, uw);
        return comment;
    }

    @Override
    public Collection deleteCollection(int record) {
        UpdateWrapper<Collection> uw = new UpdateWrapper<>();
        uw.eq("record", record).set("deleted", 1);
        Collection collection = collectionMapper.selectById(record);
        collection.setEditTime(null);
        collectionMapper.update(collection, uw);
        return collection;
    }
}
