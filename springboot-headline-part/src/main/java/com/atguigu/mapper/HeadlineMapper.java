package com.atguigu.mapper;

import com.atguigu.pojo.Headline;
import com.atguigu.pojo.vo.PortalVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author youren
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2023-11-24 23:53:49
* @Entity com.atguigu.pojo.Headline
*/
public interface HeadlineMapper extends BaseMapper<Headline> {

//    通过使用 @Param 注解，你可以在 SQL 语句中使用这个别名(portalVo)来引用方法参数。
    IPage<Map> selectMyPage(IPage iPage,@Param("portalVo") PortalVo portalVo);

    Map quryDetailMap(Integer hid);
}




