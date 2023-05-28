package com.one.one.mqListen;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.one.one.entity.TbVoucherOrder;
import com.one.one.entity.kill;
import com.one.one.mapper.TbVoucherOrderMapper;
import com.one.one.mapper.ToSeckillVoucherMapper;
import com.one.one.service.impl.TbVoucherOrderServiceImpl;
import com.one.one.service.impl.ToSeckillVoucherServiceImpl;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import jodd.util.StringUtil;
import org.apache.ibatis.cache.Cache;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class killListen {
    @Autowired
    private TbVoucherOrderMapper tbVoucherOrderMapper;
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private ToSeckillVoucherServiceImpl toSeckillVoucherService;
    @RabbitListener(queues = "killQueue")
    public void listenerWork(Message msg, Channel channel){
        try {
            String killMes = new String(msg.getBody());
            com.one.one.entity.kill kill = JSON.parseObject(killMes, com.one.one.entity.kill.class);
            //创建订单
            String value = template.opsForValue().get("mi:" + msg.getMessageProperties().getDeliveryTag());
            if (StringUtil.isNotBlank(value)){
                template.delete("mi:"+msg.getMessageProperties().getDeliveryTag());
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
                return;
            }
            TbVoucherOrder tbVoucherOrder = new TbVoucherOrder();
            //添加订单id，用户id，代金卷id
            tbVoucherOrder.setId(22222L);
            tbVoucherOrder.setUserId(kill.getUserId());
            tbVoucherOrder.setVoucherId(kill.getVecheerId());
            int insert = tbVoucherOrderMapper.insert(tbVoucherOrder);

            //扣减库存
            toSeckillVoucherService.update().setSql("stock = stock - 1")
                    .eq("voucher_id", kill.getVecheerId()).
                    gt("stock", 0).update();

            channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);

            template.opsForValue().set("mi:"+msg.getMessageProperties().getDeliveryTag(),"1");
            template.opsForValue().set("one:"+kill.getOrderId(),"2");
            template.opsForValue().set("one:" + kill.getUserId()+":"+kill.getVecheerId(),String.valueOf(kill.getOrderId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
