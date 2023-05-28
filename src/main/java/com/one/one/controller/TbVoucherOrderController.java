package com.one.one.controller;


import com.one.one.dto.Result;
import com.one.one.service.impl.TbVoucherOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YQ
 * @since 2022-12-11
 */
@RestController
@RequestMapping("/tbVoucherOrder")
public class TbVoucherOrderController {
    @Autowired
    private TbVoucherOrderServiceImpl tbVoucherOrderService;
    @PostMapping("seckill/{id}")
    public Result seckillVoucher(@PathVariable("id")Long vecheerId,Boolean isTrue){

        return tbVoucherOrderService.seckillVoucher(vecheerId,isTrue);
    }
}
