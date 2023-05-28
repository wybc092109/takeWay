package com.one.one.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 秒杀优惠券表，与优惠券是一对一关系
 * </p>
 *
 * @author YQ
 * @since 2022-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ToSeckillVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的优惠券的id
     */
    private Long voucherId;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 生效时间
     */
    private LocalDateTime beginTime;

    /**
     * 失效时间
     */
    private LocalDateTime endTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
