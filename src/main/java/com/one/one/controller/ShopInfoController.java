package com.one.one.controller;


import com.one.one.dto.Result;
import com.one.one.service.impl.ShopInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YQ
 * @since 2022-11-12
 */
@RestController
@RequestMapping("/shopInfo")
public class ShopInfoController {
    @Autowired
    private ShopInfoServiceImpl service;
    @GetMapping("get")
    public Result getInfo(int shopId){
        return service.getInfo(shopId);
    }
}
