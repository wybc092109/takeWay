package com.one.one.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.one.one.dto.Result;
import com.one.one.entity.ToVoucher;
import com.one.one.mapper.ToVoucherMapper;
import com.one.one.service.IToVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.one.utils.UserHolder;
import jodd.time.TimeUtil;
import jodd.util.StringUtil;
import net.sf.jsqlparser.statement.select.KSQLWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
public class ToVoucherServiceImpl extends ServiceImpl<ToVoucherMapper, ToVoucher> implements IToVoucherService {

    @Autowired
    private StringRedisTemplate template;
    @Override
    public Result allVoucher() {
        Long userId = UserHolder.getUser().getId();
        String key ="allVoucher:exit"+userId;
        LocalDateTime zeroTme = LocalDateTime.now().
                withHour(24).withMinute(0).withSecond(0);
        LocalDateTime loginTime = LocalDateTime.now();
        long zeroS = zeroTme.atZone(ZoneOffset.systemDefault()).toEpochSecond();
        long loginS = loginTime.atZone(ZoneOffset.systemDefault()).toEpochSecond();
        String exitLogin = template.opsForValue().get(key);

        if (StringUtil.isNotBlank(exitLogin)){
            template.opsForValue().set(key,"login",zeroS-loginS,TimeUnit.SECONDS);
            Random random=new Random();
            int monNum = random.nextInt(20);
            if (monNum>=11){
                List<Integer> list=new ArrayList<>();
                list.add(monNum);
                list.add(25);
                return Result.ok(list);
            }else {
                List<Integer> list=new ArrayList<>();
                list.add(monNum);
                list.add(15);
                return Result.ok(list);
            }
        }else {
            return Result.fail("450");
        }
    }
}
