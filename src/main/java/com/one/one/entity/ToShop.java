package com.one.one.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商家
 * </p>
 *
 * @author YQ
 * @since 2022-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ToShop implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家名称
     */
    private String shopName;

    /**
     * 图片
     */
    private String image;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 地址
     */
    private String address;


    private Integer status;


    @TableField(exist = false)
    private Double arrivedTime;



}
