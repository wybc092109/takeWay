package com.one.one.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.one.one.dto.Result;
import com.one.one.entity.ShopInfo;
import com.one.one.entity.ToShop;
import com.one.one.entity.ToUser;
import com.one.one.mapper.ShopInfoMapper;
import com.one.one.mapper.ToShopMapper;
import com.one.one.mapper.ToUserMapper;
import com.one.one.service.IShopInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YQ
 * @since 2022-11-12
 */
@Service
public class ShopInfoServiceImpl extends ServiceImpl<ShopInfoMapper, ShopInfo> implements IShopInfoService {
    @Autowired
    private ToShopMapper toShopMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public Result getInfo(int shopId) {
        String key="shopInfo:"+shopId;
        String shopInfo = redisTemplate.opsForValue().get(key);

        if (StrUtil.isNotBlank(shopInfo)){
            ShopInfo shop = BeanUtil.toBean(shopInfo, ShopInfo.class);
            return Result.ok(shop);
        }
        if (shopInfo!=null){
            return Result.fail("无数据");
        }
        ToShop toUser = toShopMapper.selectById(shopId);
        LambdaQueryWrapper<ShopInfo> la=new LambdaQueryWrapper<>();
        la.eq(ShopInfo::getShopId,shopId);
        ShopInfo one = getOne(la);
        one.setShop_add(toUser.getAddress());
        String shopTre = JSONUtil.toJsonStr(one);
        redisTemplate.opsForValue().set(key,shopTre,2, TimeUnit.HOURS);
        return Result.ok(one);
    }
}
