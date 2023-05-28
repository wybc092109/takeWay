package com.one.one.mapper;

import com.one.one.entity.ToUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author YQ
 * @since 2022-10-04
 */
@Mapper
public interface ToUserMapper extends BaseMapper<ToUser> {
}
