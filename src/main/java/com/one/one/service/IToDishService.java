package com.one.one.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.one.one.dto.Result;
import com.one.one.entity.ToDish;
import com.one.one.utils.PageResult;
import com.one.one.utils.RequestParams;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author YQ
 * @since 2022-10-12
 */
public interface IToDishService extends IService<ToDish> {
    Result get(Long typeId, Long shopId);

    PageResult search(RequestParams params);
}
