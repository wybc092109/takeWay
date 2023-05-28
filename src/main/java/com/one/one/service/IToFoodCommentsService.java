package com.one.one.service;

import com.one.one.dto.Result;
import com.one.one.entity.ToFoodComments;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YQ
 * @since 2022-10-30
 */
public interface IToFoodCommentsService extends IService<ToFoodComments> {

    Result comment(long id);

    Result good(Long id);
}
