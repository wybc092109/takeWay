package com.one.one.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class ToDishDoc {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private Long shopid;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 菜品分类id
     */
    private Long categoryId;

    /**
     * 菜品价格
     */
    private BigDecimal price;

    /**
     * 图片
     */
    private String image;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 0 停售 1 起售
     */
    private Integer status;

    /**
     * 分数
     */
    private Integer score;

    private String location;

    private Boolean isMoney;

    private Boolean isRecommend;

    private Double arrivedTime;
    private Object distance;

    private double scoreEs;

//    private Object distance;
//    private List<String> suggestion;
    public ToDishDoc(ToDish toDish){
        this.id=toDish.getId();
        this.categoryId=toDish.getCategoryId();
        this.description=toDish.getDescription();
        this.image=toDish.getImage();
        this.name=toDish.getName();
        this.score=toDish.getScore();
        this.status=toDish.getStatus();
        this.price=toDish.getPrice();
        this.shopid=toDish.getShopid();

        this.location = toDish.getLatitude() + ", " + toDish.getLongitude();
//        // 组装suggestion
//        if(this.name.contains("/")){
//            // business有多个值，需要切割
//            String[] arr = this.name.split("/");
//            // 添加元素
//            this.suggestion = new ArrayList<>();
//            this.suggestion.add(this.brand);
//            Collections.addAll(this.suggestion, arr);
//        }else {
//            this.suggestion = Arrays.asList(this.brand, this.name);
//        }
        if (toDish.getIsMoney()!=null){
            this.isMoney=toDish.getIsMoney();
        }else {
            this.isMoney=false;
        }
        if (toDish.getIsRecommend()!=null){
            this.isRecommend=toDish.getIsRecommend();
        }else {
            this.isRecommend=false;
        }
    }
}
