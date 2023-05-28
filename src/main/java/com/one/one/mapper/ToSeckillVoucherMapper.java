package com.one.one.mapper;

import com.one.one.entity.ToSeckillVoucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 秒杀优惠券表，与优惠券是一对一关系 Mapper 接口
 * </p>
 *
 * @author YQ
 * @since 2022-12-11
 */
@Mapper
public interface ToSeckillVoucherMapper extends BaseMapper<ToSeckillVoucher> {

}
