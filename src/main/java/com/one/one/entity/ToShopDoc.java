package com.one.one.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class ToShopDoc {
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


    private String location;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 地址
     */
    private String address;
    // 排序时的 距离值
    private Object distance;

    private List<String> suggestion;

    private Double arrivedTime;



    public ToShopDoc(ToShop toShop){
        this.id=toShop.getId();
        this.description=toShop.getDescription();
        this.score=toShop.getScore();
        this.address=toShop.getAddress();
        this.image=toShop.getImage();
        this.shopName=toShop.getShopName();
        this.location = toShop.getLatitude() + ", " + toShop.getLongitude();
        if(this.shopName.contains("/")){
            // business有多个值，需要切割
            String[] arr = this.shopName.split("/");
            // 添加元素
            this.suggestion = new ArrayList<>();
            Collections.addAll(this.suggestion, arr);
        }else {
            this.suggestion = Collections.singletonList(this.shopName);
        }
    }
}
