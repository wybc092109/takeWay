package com.one.one.service;

import com.one.one.dto.Result;
import com.one.one.entity.ShopDiscount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YQ
 * @since 2022-12-30
 */
public interface IShopDiscountService extends IService<ShopDiscount> {

    Result get();

}
