package com.one.one.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author YQ
 * @since 2022-10-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ToUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField("userName")
    private String username;

    /**
     * 姓名
     */
    @TableField("realName")
    private String realname;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 密码
     */
    @TableField("passWord")
    private String password;

    /**
     * 账号
     */
    @TableField("accountNumber")
    private String accountnumber;

    /**
     * 头像
     */
    private String profile;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号码
     */
    @TableField("phoneNumber")
    private String phonenumber;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 登入状态
     */
    @TableField("loginStatus")
    private Integer loginStatus;

}
