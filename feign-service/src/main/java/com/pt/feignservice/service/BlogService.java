package com.pt.feignservice.service;

import com.pt.feignservice.dto.*;
import com.pt.feignservice.fallback.BlogServiceFallback;
import com.pt.feignservice.po.Blog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "blog-service", fallback = BlogServiceFallback.class)
public interface BlogService {

    @RequestMapping(value = "/blog/write", method = RequestMethod.POST)
    String write(@RequestBody BlogDto blogDto);

    @RequestMapping(value = "/blog/collect", method = RequestMethod.POST)
    String collect(@RequestBody CollectDto collectDto);

    @RequestMapping(value = "/blog/comment", method = RequestMethod.POST)
    String comment(@RequestBody CommentDto commentDto);

    @RequestMapping(value = "/blog/modify", method = RequestMethod.POST)
    String modify(@RequestBody ModifyDto modifyDto);

    @RequestMapping(value = "/blog/select", method = RequestMethod.GET)
    List<Blog> select(@RequestParam("userId") Integer userId);

    @RequestMapping(value = "/blog/look", method = RequestMethod.POST)
    String look(@RequestBody LookDto lookDto);

    @RequestMapping(value = "/blog/delete-blog", method = RequestMethod.POST)
    String deleteBlog(@RequestParam("record") Integer record);

    @RequestMapping(value = "/blog/delete-comment", method = RequestMethod.POST)
    String deleteComment(@RequestParam("record") Integer record);

    @RequestMapping(value = "/blog/delete-collection", method = RequestMethod.POST)
    String deleteCollection(@RequestParam("record") Integer record);


}
