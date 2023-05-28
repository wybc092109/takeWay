package com.one.one.service.impl;

import com.one.one.dto.Result;
import com.one.one.entity.ShopDiscount;
import com.one.one.mapper.ShopDiscountMapper;
import com.one.one.service.IShopDiscountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YQ
 * @since 2022-12-30
 */
@Service
public class ShopDiscountServiceImpl extends ServiceImpl<ShopDiscountMapper, ShopDiscount> implements IShopDiscountService {

    @Override
    public Result get() {
        List<ShopDiscount> list = list();
        return Result.ok(list);
    }
}
