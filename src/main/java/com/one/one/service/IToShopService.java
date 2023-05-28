package com.one.one.service;

import com.one.one.entity.ToShop;
import com.baomidou.mybatisplus.extension.service.IService;
import com.one.one.utils.PageResult;
import com.one.one.utils.RequestParams;

import java.util.List;

/**
 * <p>
 * 商家 服务类
 * </p>
 *
 * @author YQ
 * @since 2022-10-13
 */
public interface IToShopService extends IService<ToShop> {

    PageResult search(RequestParams params);

    List<String> getSuggestions(String prefix);
}
