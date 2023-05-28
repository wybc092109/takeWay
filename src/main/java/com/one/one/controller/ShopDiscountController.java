package com.one.one.controller;


import com.one.one.dto.Result;
import com.one.one.service.impl.ShopDiscountServiceImpl;
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
 * @since 2022-12-30
 */
@RestController
@RequestMapping("/shopDiscount")
public class ShopDiscountController {

    @Autowired
    private ShopDiscountServiceImpl service;
    @GetMapping("get")
    public Result get(){
        return service.get();
    }
}
