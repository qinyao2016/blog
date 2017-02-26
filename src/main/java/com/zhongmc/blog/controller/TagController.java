package com.zhongmc.blog.controller;

import com.zhongmc.blog.dao.BlogMapper;
import com.zhongmc.blog.dao.TagMapper;
import com.zhongmc.blog.domain.Blog;
import com.zhongmc.blog.domain.Tag;
import com.zhongmc.blog.utils.Page;
import com.zhongmc.blog.utils.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZMC on 2017/1/25.
 */
@Controller
public class TagController {
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    TagMapper tagMapper;
    //通过标签id查找所有的博客
    @RequestMapping(value = "/tag-blogs/{tagid}",method = RequestMethod.GET)
    public String tagBlogs(@PathVariable("tagid") int id, Model model, HttpServletRequest request){
        Tag tag = tagMapper.findOneById(id);
        model.addAttribute("tag",tag);

        int totalSize = blogMapper.CountTagBlogs(id);
        Page<Blog> blogPage = new Page<>();
        blogPage.setPageSize(1);

        blogPage.setTotalRecord(totalSize);
        int index = 1;
        String page = request.getParameter("page");
        if (page!=null){
            index = Integer.parseInt(page);
            if (index<1){
                index = 1;
            }else if (index>blogPage.getTotalPage()){
                index = blogPage.getTotalPage();
            }
        }
        blogPage.setPageNo(index);
        int startIndex = blogPage.getPageSize()*(blogPage.getPageNo()-1);
        Map<String,Integer> tmp = new HashMap<>();
        tmp.put("tagid",id);
        tmp.put("startIndex",startIndex);
        tmp.put("pageSize",blogPage.getPageSize());
        List<Blog> blogList = blogMapper.findBlogsByTagId(tmp);
        model.addAttribute("blogList",blogList);
        String pageStr = PagingUtil.getPagelink(index,blogPage.getTotalRecord()/blogPage.getPageSize(),"","/tag-blogs/"+id);
        model.addAttribute("pageStr",pageStr);

        return "tag_list";
    }
}