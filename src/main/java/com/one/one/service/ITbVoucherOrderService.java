package com.one.one.service;

import com.one.one.dto.Result;
import com.one.one.entity.TbVoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YQ
 * @since 2022-12-11
 */
public interface ITbVoucherOrderService extends IService<TbVoucherOrder> {

    Result seckillVoucher(Long vecheerId,Boolean isTrue);
}
