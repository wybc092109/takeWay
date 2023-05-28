package com.one.one.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商家
 * </p>
 *
 * @author YQ
 * @since 2022-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("to_dishType")
public class ToDishtype implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜品类型
     */
    @TableField("typeName")
    private String typename;

    /**
     * 对应的商家
     */
    @TableField("shopId")
    private Integer shopid;

    /**
     * 菜品id
     */
    @TableField("typeId")
    private Integer typeid;


}
