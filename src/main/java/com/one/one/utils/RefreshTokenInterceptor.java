package com.one.one.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import com.one.one.dto.UserDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.one.one.utils.RedisConstants.LOGIN_USER_KEY;
import static com.one.one.utils.RedisConstants.LOGIN_USER_TTL;


//@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的token
        String pathInfo = request.getPathInfo();
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            return true;
        }
//        String token="392ccd21-8c0a-404a-89d7-cb50584ed659";
        // 2.基于TOKEN获取redis中的用户
        String key  = LOGIN_USER_KEY + token;
        String userMap = stringRedisTemplate.opsForValue().get(key);
        // 3.判断用户是否存在
        if (userMap == null) {
            return true;
        }
        // 5.将查询到的hash数据转为UserDTO
        UserDTO userDTO = BeanUtil.toBean(userMap,UserDTO.class);
        // 6.存在，保存用户信息到 ThreadLocal
        UserHolder.saveUser(userDTO);
        // 7.刷新token有效期
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        // 8.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }
}
