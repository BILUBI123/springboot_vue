package com.atguigu.interceptor;


import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//登录包含拦截器，检查请求头是否包含有效token,没有，无效，返回504

@Component
public class LoginProtectedIntertector implements HandlerInterceptor {

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从请求头中获取token
        String token = request.getHeader("token");

        //检查是否有效
        boolean expiration = jwtHelper.isExpiration(token);

        //有效放行
        if (!expiration){
            return true;
        }

        //无效返回504的状态jsor
        Result<Object> result = Result.build(null, ResultCodeEnum.NOTLOGIN);

//        使用 writeValueAsString 方法将 Result 对象转换成 JSON 格式的字符串。
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
//        将 JSON 字符串写入响应体：
        response.getWriter().print(json);

        return false;


    }
}
