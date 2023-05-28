package com.one.one.service;

import com.one.one.dto.Result;
import com.one.one.entity.ToDishtype;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商家 服务类
 * </p>
 *
 * @author YQ
 * @since 2022-10-25
 */
public interface IToDishtypeService extends IService<ToDishtype> {

    Result get(Long id);
}
