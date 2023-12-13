package com.atguigu.controller;


import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@RestController 是 @Controller 和 @ResponseBody 的组合。@Controller 用于定义控制器，
//而 @ResponseBody 用于指示方法的返回值应该直接写入响应体中，而不是通过视图解析器解析为视图。

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Result login(@RequestBody User user) {
        Result result = userService.login(user);
        return result;
    }

//    @RequestHeader 用于从 HTTP 请求头中获取名为 token 的信息。
    @GetMapping("getUserInfo")
    public Result getUserInfo(@RequestHeader String token) {
        Result result = userService.getUserInfo(token);
        return result;
    }

    @PostMapping("checkUserName")
    public Result checkUserName(String username) {
        Result result = userService.checkUserName(username);
        return result;
    }

    @PostMapping("regist")
    public Result regist(@RequestBody User user) {
        Result result = userService.regist(user);
        return result;
    }

    @GetMapping("checkLogin")
    public Result checkLogin(@RequestHeader String token) {
        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration){
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }

        return Result.ok(null);
    }
}
