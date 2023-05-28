package com.one.one.service;

import com.one.one.dto.Result;
import com.one.one.entity.ToUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author YQ
 * @since 2022-10-04
 */
public interface IToUserService extends IService<ToUser> {

    Result sendCode(String phone);

    Result loginWithPw(String accountNumber, String password);

    Result loginWithPc(String phone, String code);

    Result improve(ToUser toUser);
}
