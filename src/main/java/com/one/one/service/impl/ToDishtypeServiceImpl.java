package com.one.one.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.one.one.dto.Result;
import com.one.one.entity.ToDish;
import com.one.one.entity.ToDishtype;
import com.one.one.mapper.ToDishtypeMapper;
import com.one.one.service.IToDishtypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 商家 服务实现类
 * </p>
 *
 * @author YQ
 * @since 2022-10-25
 */
@Service
public class ToDishtypeServiceImpl extends ServiceImpl<ToDishtypeMapper, ToDishtype> implements IToDishtypeService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ToDishtypeMapper toDishtypeMapper;
    @Override
    public Result get(Long id) {

        String typeKey="type:"+id;
        String typeList = redisTemplate.opsForValue().get(typeKey);
        if (StrUtil.isNotBlank(typeList)){
            List<ToDishtype> toDishtypes = JSONUtil.toList(typeList, ToDishtype.class);
            return Result.ok(toDishtypes);
        }
        if(typeList !=null){
            return Result.fail("店铺不存在");
        }
        LambdaQueryWrapper<ToDishtype> la=new LambdaQueryWrapper<>();
        la.eq(ToDishtype::getShopid,id);
        List<ToDishtype> toDishtypes = toDishtypeMapper.selectList(la);
        if (toDishtypes==null){
            redisTemplate.opsForValue().set(typeKey,"",20, TimeUnit.MINUTES);
        }
        String toTypes = JSONUtil.toJsonStr(toDishtypes);
        redisTemplate.opsForValue().set(typeKey,toTypes ,20, TimeUnit.HOURS);
        return Result.ok(toDishtypes);
    }
}
