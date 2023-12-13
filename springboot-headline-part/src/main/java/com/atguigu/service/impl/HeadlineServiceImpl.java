package com.atguigu.service.impl;

import com.atguigu.pojo.vo.PortalVo;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.Headline;
import com.atguigu.service.HeadlineService;
import com.atguigu.mapper.HeadlineMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author youren
 * @description 针对表【news_headline】的数据库操作Service实现
 * @createDate 2023-11-24 23:53:49
 */
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
        implements HeadlineService {
    @Autowired
    private JwtHelper jwtHelper;
    @Resource
    private HeadlineMapper headlineMapper;

    //    首页数据查询
//    1.进行分页数据查询
//    2.分页数据，拼接到result即可
//    注意1:查询需要自定义语句 自定义mapper的方法，携带分页
//    注意2: 返回的结果List<Map>
    @Override
    public Result findNewsPage(PortalVo portalVo) {

//        IPage<Map> iPage= new Page<>();： 这里创建了一个分页对象 iPage，类型是 IPage，
//        并且实例化为 Page。Page 是 MyBatis-Plus 提供的一个实现了 IPage 接口的分页对象。
//        这个对象用于包含查询结果以及分页的相关信息，比如总记录数、当前页码等。

        IPage<Map> iPage = new Page<>(portalVo.getPageNum(), portalVo.getPageSize());

//        这个方法执行数据库查询，根据传入的 portalVo 中的条件动态生成 SQL，将查询结果封装到 iPage 中。

        headlineMapper.selectMyPage(iPage, portalVo);

        List<Map> records = iPage.getRecords();
        Map data = new HashMap();
        data.put("pageData", records);
        data.put("pageNum", iPage.getCurrent());
        data.put("pageSize", iPage.getSize());
        data.put("totalPage", iPage.getPages());
        data.put("totalSize", iPage.getTotal());


        Map pageInfo = new HashMap();
        pageInfo.put("pageInfo", data);

        return Result.ok(pageInfo);
    }

    //    根据id查询详情
//        2、查询对应的数据即可 [多表，头条和用户表，方法需要自定义 返回map即可]
//        1、修改阅读量 + 1 [version乐观锁，当前数据对应的版本]
    @Override
    public Result showHeadLineDetail(Integer hid) {
        Map map = headlineMapper.quryDetailMap(hid);

        if (map != null) {
            // 创建一个新的映射以保存更新后的值
            Map headlineMap = new HashMap();
            headlineMap.put("headline", map);

            // 修改 pageViews 的值
            Integer pageViews = (Integer) map.get("pageViews");
//            if (pageViews != null) {

            // 更新数据库
            Headline headline1 = new Headline();
            headline1.setHid((Integer) map.get("hid"));
            headline1.setVersion((Integer) map.get("version"));
            headline1.setPageViews(pageViews + 1); // 更新后的 pageViews
            headlineMapper.updateById(headline1);
//            }

            return Result.ok(headlineMap);
        } else {
            return null;
        }
    }

    //    发布头条方法
//        1.补全数据
    @Override
    public Result publish(Headline headline, String token) {

        //token查询用户id
        int i = jwtHelper.getUserId(token).intValue();

        //数据装配
        headline.setPublisher(i);
        headline.setPageViews(0);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());

        headlineMapper.insert(headline);
        return Result.ok(null);
    }

    //    修改头条数据
//    1.hid查询数据的最新version
//    2.修改数据的修改时间为当前节点
    @Override
    public Result updateData(Headline headline) {

        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();

        headline.setVersion(version);   //乐观锁
        headline.setUpdateTime(new Date());

        headlineMapper.updateById(headline);

        return Result.ok(null);

    }

}




