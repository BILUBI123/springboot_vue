package com.atguigu.controller;

import com.atguigu.pojo.Headline;
import com.atguigu.service.HeadlineService;
import com.atguigu.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("headline")
@CrossOrigin
public class HeadlineController {

    @Autowired
    private HeadlineService headlineService;


    //登录以后才可以访问
    @PostMapping("publish")
    public Result publish(@RequestBody Headline headline,@RequestHeader String token) {
        Result result = headlineService.publish(headline,token);
        return result;
    }

    @PostMapping("findHeadlineByHid")
    public Result findHeadlineByHid(Integer hid) {
        Headline headline = headlineService.getById(hid);
        Map map = new HashMap();
        map.put("headline",headline);
        return Result.ok(map);
    }

    @PostMapping("update")
    public Result update(@RequestBody Headline headline) {
        Result result = headlineService.updateData(headline);
        return result;
    }


    @PostMapping("removeByHid")
    public Result removeByHid(Integer hid) {
        headlineService.removeById(hid);
        return Result.ok(null);
    }
}
