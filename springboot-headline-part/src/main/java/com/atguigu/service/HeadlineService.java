package com.atguigu.service;

import com.atguigu.pojo.Headline;
import com.atguigu.pojo.vo.PortalVo;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author youren
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2023-11-24 23:53:49
*/
public interface HeadlineService extends IService<Headline> {

//    首页数据查询
    Result findNewsPage(PortalVo portalVo);

    Result showHeadLineDetail(Integer hid);

//    发布头条
    Result publish(Headline headline,String token);

    Result updateData(Headline headline);
}
