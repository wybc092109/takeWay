package com.one.one.service;

import com.one.one.dto.Result;
import com.one.one.entity.ShopInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YQ
 * @since 2022-11-12
 */
public interface IShopInfoService extends IService<ShopInfo> {

    Result getInfo(int shopId);
}
