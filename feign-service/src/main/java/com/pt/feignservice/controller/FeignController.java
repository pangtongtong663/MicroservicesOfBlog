package com.pt.feignservice.controller;

import com.pt.feignservice.dto.*;
import com.pt.feignservice.po.Blog;
import com.pt.feignservice.po.User;
import com.pt.feignservice.service.AccountService;
import com.pt.feignservice.service.BlogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class FeignController {
    @Resource
    AccountService accountService;

    @Resource
    BlogService blogService;


    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public String createUser(@RequestBody SignUpDto user) {
        return accountService.createUser(user);
    }


    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String write(@RequestBody BlogDto blogDto) {
        return blogService.write(blogDto);
    }

    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public String collect(@RequestBody CollectDto collectDto) {
        return blogService.collect(collectDto);
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public String comment(@RequestBody CommentDto commentDto) {
        return blogService.comment(commentDto);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public String modify(@RequestBody ModifyDto modifyDto) {
        return blogService.modify(modifyDto);
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public List<Blog> select(Integer userId) {
        return blogService.select(userId);
    }

    @RequestMapping(value = "/look", method = RequestMethod.POST)
    public String look(@RequestBody LookDto lookDto) {
        return blogService.look(lookDto);
    }

    @RequestMapping(value = "/delete-blog", method = RequestMethod.POST)
    public String deleteBlog(Integer record) {
        return blogService.deleteBlog(record);
    }

    @RequestMapping(value = "/delete-comment", method = RequestMethod.POST)
    public String deleteComment(Integer record) {
        return blogService.deleteComment(record);
    }

    @RequestMapping(value = "/delete-collection", method = RequestMethod.POST)
    public String deleteCollection(Integer record) {
        return blogService.deleteCollection(record);
    }

}
