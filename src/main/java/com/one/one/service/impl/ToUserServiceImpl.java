package com.one.one.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.one.one.dto.Result;
import com.one.one.dto.UserDTO;
import com.one.one.entity.ToUser;
import com.one.one.mapper.ToUserMapper;
import com.one.one.service.IToUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.one.one.utils.RedisConstants.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author YQ
 * @since 2022-10-04
 */
@Service
public class ToUserServiceImpl extends ServiceImpl<ToUserMapper, ToUser> implements IToUserService {
    @Autowired
    private ToUserMapper toUserMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public Result sendCode(String phone) {
        String code = RandomUtil.randomNumbers(6);
        //验证码存进redis
        redisTemplate.opsForValue().
                set(LOGIN_CODE_KEY+phone, code,LOGIN_CODE_TTL, TimeUnit.MINUTES);
        return Result.ok(code);
    }

    @Override
    public Result loginWithPw(String accountNumber, String password) {
        LambdaQueryWrapper<ToUser> la=new LambdaQueryWrapper<>();
        la.eq(ToUser::getAccountnumber,accountNumber);
        la.eq(ToUser::getPassword,password);
        ToUser toUser = toUserMapper.selectOne(la);
        if (Objects.isNull(toUser)){
            return Result.fail("账号或密码错误");
        }
//        if (toUser.getLoginStatus()==1){
//            return Result.fail("该用户已在别处登入");
//        }
        LambdaQueryWrapper<ToUser> ula=new LambdaQueryWrapper<>();
        ula.eq(ToUser::getAccountnumber,accountNumber);
        toUser.setLoginStatus(1);
        boolean update = update(toUser, ula);

        //保存用户信息到redis中用uuid作为唯一标识
        return retuenToken(toUser);
    }

    @Override
    public Result loginWithPc(String phone, String code) {
        String rcode = redisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        if (!Objects.equals(rcode, code)){
            return Result.fail("验证码错误，请重新输入");
        }
        LambdaQueryWrapper<ToUser> la=new LambdaQueryWrapper<>();
        la.eq(ToUser::getPhonenumber,phone);
        ToUser toUser = toUserMapper.selectOne(la);
        if (Objects.nonNull(toUser)){
            //保存用户信息到redis中用uuid作为唯一标识
            return retuenToken(toUser);
        }
        Map<String, Object> map=new Hashtable<>();
//        Result result = retuenToken(toUser);
//        Object data = result.getData();
        map.put("status",250);
        map.put("accountNumber",RandomUtil.randomNumbers(10));
//        map.put("token",data);
        return Result.ok(map);
    }

    @Override
    public Result improve(ToUser toUser) {
        Result result = createuserwithphone(toUser);
        return result ;
    }

    private Result retuenToken(ToUser toUser) {
        String token = UUID.randomUUID().toString();
        String tokenkey = LOGIN_USER_KEY + token;
        UserDTO userDTO = BeanUtil.copyProperties(toUser, UserDTO.class);
        String json = JSONUtil.toJsonStr(userDTO);
        redisTemplate.opsForValue().set(tokenkey, json);
        //设置有效期30min
        redisTemplate.expire(tokenkey, 30, TimeUnit.MINUTES);
        //把token返回
        Map<String, Object> map=new Hashtable<>();
        map.put("token",token);
        return Result.ok(map);
    }
    private Result createuserwithphone(ToUser toUser) {

        toUser.setUsername(RandomUtil.randomString(6));
        toUser.setLoginStatus(1);
        save(toUser);
        Result token = retuenToken(toUser);
        return token;
    }
}
