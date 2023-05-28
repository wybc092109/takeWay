package com.one.one.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2022-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ShopInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商家所属id
     */
    private Integer shopId;

    /**
     * 所属服务
     */
    private String serverWhere;

    /**
     * 食品安全
     */
    private String foodSave;

    /**
     * 营业时间
     */
    private String openTime;

    /**
     * 商家公告
     */
    private String shopSay;


    @TableField(exist = false)
    private String shop_add;


}
