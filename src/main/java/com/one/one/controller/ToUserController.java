package com.one.one.controller;


import com.one.one.dto.Result;
import com.one.one.entity.ToUser;
import com.one.one.service.impl.ToUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author YQ
 * @since 2022-10-04
 */
@RestController
@RequestMapping("/toUser")
public class ToUserController {
    @Autowired
    private ToUserServiceImpl toUserService;
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone){
        return toUserService.sendCode(phone);
    }
    @PostMapping("loginWithPw")
    public Result loginWithPw(@RequestParam("accountNumber") String accountNumber,@RequestParam("password") String password){
        return toUserService.loginWithPw(accountNumber,password);
    }
    @PostMapping("loginWithPc")
    public  Result loginWithPc(@RequestParam("phone") String phone,@RequestParam("code") String code){
        return toUserService.loginWithPc(phone,code);
    }
    @PostMapping("improve")
    public Result improve(@RequestBody() ToUser toUser) {
        return toUserService.improve(toUser);
    }
}
