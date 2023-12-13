package com.atguigu.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.MD5Util;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youren
 * @description 针对表【news_user】的数据库操作Service实现
 * @createDate 2023-11-24 23:53:49
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private JwtHelper jwtHelper;

    //    1.根据账号，查询用户对象 - LoginUser
//  2.如果用户对象为nulL，查询失败，账号错误! 501
//   3.对比，密码 ，失败 返回503的错误
//  4.根据用户id生成一个token，token ->result 返回
    @Override
    public Result login(User user) {
        //根据账号查询数据

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

//        User::getUsername 是一个方法引用，指向 User 类中的 getUsername 方法
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());

//        selectOne 方法用于期望查询结果只有一条记录，
//        如果有多条或者没有匹配到记录，返回结果会是 null
        User user1 = userMapper.selectOne(lambdaQueryWrapper);

        if (user1 == null) {
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //对比密码
        if (!StringUtils.isEmpty(user.getUserPwd())
                && MD5Util.encrypt(user.getUserPwd()).equals(user1.getUserPwd())) {
            //登录成功
            //根据用户id生成 token
            // 将token封装到result返回
            String token = jwtHelper.createToken(Long.valueOf(user1.getUid()));
            //将token封装到result返回
            Map data = new HashMap();
            data.put("token", token);
            return Result.ok(data);
        }
        //密码错误
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    //    1. token是否在有效期
//    2.根据token解析userId
//    3.根据用户id查询用数据
//    4.去密码，封装result结果返回即可
    @Override
    public Result getUserInfo(String token) {
        //是否过期 true过期
        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {

            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }

        int userId = jwtHelper.getUserId(token).intValue();

//        pojo的实体类主键加上@TableId，selectById才能用
        User user = userMapper.selectById(userId);
        user.setUserPwd("");

        Map data = new HashMap();
        data.put("loginUser", user);

        return Result.ok(data);
    }

    //    检查账号是否可用
//    1.根据账号进行count查询
//    2.count==0可用
//    3.count>0不可用
    @Override
    public Result checkUserName(String username) {

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);

        Long count = userMapper.selectCount(lambdaQueryWrapper);

        if (count == 0) {
            return Result.ok(null);
        }

        return Result.build(null, ResultCodeEnum.USERNAME_USED);
    }

    //    注册业务
//    1.依然检查账号是否已经被注册
//    2.密码加密处理
//    3.账号数据保存
//    4.返回结果
    @Override
    public Result regist(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());

        Long count = userMapper.selectCount(lambdaQueryWrapper);

        if (count > 0) {
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }

        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));

        userMapper.insert(user);

        return  Result.ok(null);
    }
}




