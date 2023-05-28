package com.one.one.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.one.one.dto.UserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author YQ
 * @since 2022-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ToFoodComments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 探店id
     */
    private Long blogId;

    /**
     * 关联的1级评论id，如果是一级评论，则值为0
     */
    private Long parentId;

    /**
     * 回复的评论id
     */
    private Long answerId;

    /**
     * 回复的内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer liked;

    /**
     * 状态，0：正常，1：被举报，2：禁止查看
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用于聚合的差评的评论id
     */
    private Long badId;

    /**
     * 聚合好评的id的评论id
     */
    private Long goodId;

    /**
     * 聚合有图片的评论id
     */
    private Long picId;

    /**
     * 聚合味道好的评论id
     */
    private Long goodEatId;

    @TableField(exist = false)
    private Boolean isGood;

    @TableField(exist = false)
    private UserDTO userDTO;

    @TableField(exist = false)
    private String stringCreateTime;
}
