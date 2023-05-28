package com.one.one.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author YQ
 * @since 2022-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ShopDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品未打折所属id
     */
    private Integer shopingId;

    /**
     * 七天是否可退还
     */
    private String ifSevenday;

    /**
     * 商品图片
     */
    private String pic;

    /**
     * 现在价钱
     */
    private Integer nowMoney;

    /**
     * 以前价钱
     */
    private Integer beforeMoney;

    /**
     * 库存
     */
    private Integer hasNum;


    private String goodsName;


}
