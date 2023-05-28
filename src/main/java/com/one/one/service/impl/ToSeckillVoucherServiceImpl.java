package com.one.one.service.impl;

import com.one.one.entity.ToSeckillVoucher;
import com.one.one.mapper.ToSeckillVoucherMapper;
import com.one.one.service.IToSeckillVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秒杀优惠券表，与优惠券是一对一关系 服务实现类
 * </p>
 *
 * @author YQ
 * @since 2022-12-11
 */
@Service
public class ToSeckillVoucherServiceImpl extends ServiceImpl<ToSeckillVoucherMapper, ToSeckillVoucher> implements IToSeckillVoucherService {

}
