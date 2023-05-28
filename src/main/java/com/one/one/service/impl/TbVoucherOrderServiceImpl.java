package com.one.one.service.impl;

import com.alibaba.fastjson.JSON;
import com.one.one.dto.Result;
import com.one.one.entity.TbVoucherOrder;
import com.one.one.entity.kill;
import com.one.one.mapper.TbVoucherOrderMapper;
import com.one.one.service.ITbVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.one.utils.MyRedisIdWork;
import com.one.one.utils.UserHolder;
import jodd.util.StringUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YQ
 * @since 2022-12-11
 */
@Service
public class TbVoucherOrderServiceImpl extends ServiceImpl<TbVoucherOrderMapper, TbVoucherOrder> implements ITbVoucherOrderService {
    @Autowired
    private ToSeckillVoucherServiceImpl tbSeckillVoucherService;
    @Autowired
    private RedissonClient redissonClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MyRedisIdWork myRedisIdWork;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public Result seckillVoucher(Long vecheerId,Boolean isTrue) {
        String keyV="v:1";
        String num = stringRedisTemplate.opsForValue().get(keyV);
        if (Long.parseLong(num)<0){
            stringRedisTemplate.delete(keyV);
            return Result.fail("该卷以抢完");
        }
        stringRedisTemplate.opsForValue().decrement(keyV);
        long orderId = myRedisIdWork.nextId("order");
        Long userId = UserHolder.getUser().getId();
//        Long userId = 5L;
        CorrelationData correlationData = new CorrelationData( String.valueOf(orderId));

        String key = stringRedisTemplate.opsForValue().get("one:" + userId+":"+vecheerId);

        if (key!=null){
            long orderKey= Long.parseLong(key);
            if (isTrue){
                stringRedisTemplate.delete("one:"+orderKey);
            }
        }

        String exit = stringRedisTemplate.opsForValue().get("one:" + key);
        if (StringUtil.isNotBlank(exit)){
            return Result.fail("您在短时间内有一笔相同的订单是否重复购买");
        }
        RLock oneLock = redissonClient.getLock("oneLock:"+userId);
        //判断结果是否为零
        boolean tryLock = false;
        try {
            tryLock = oneLock.tryLock(2, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            stringRedisTemplate.opsForValue().increment(keyV);
            e.printStackTrace();
        }
        if (!tryLock){
            return Result.fail("不允许重复下单");
        }


        try {
            Integer count = query().eq("user_id", userId).eq("voucher_id",
                    vecheerId).count();
            if (count > 0) {
                return Result.fail("您已经抢购过该商品啦！请下次再来");
            }
            //mq进行业务的异步处理
            //创建订单
            orderId = myRedisIdWork.nextId("order");
            kill kill=new kill();
            kill.setOrderId(orderId);
            kill.setUserId(userId);
            kill.setVecheerId(vecheerId);
            String string = JSON.toJSONString(kill);
            rabbitTemplate.convertAndSend("kill","routingKey",string,correlationData);
        } catch (AmqpException e) {
            stringRedisTemplate.opsForValue().increment(keyV);
            e.printStackTrace();
        } finally {
            oneLock.lock();
        }
        //为有购买资格，把下单信息保留进阻塞队列
        //生成订单id
        return Result.ok(orderId);
    }
}
