package com.one.one.controller;


import com.one.one.dto.Result;
import com.one.one.service.impl.ToDishtypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商家 前端控制器
 * </p>
 *
 * @author YQ
 * @since 2022-10-25
 */
@RestController
@RequestMapping("/toDishtype")
public class ToDishtypeController {
    @Autowired
    private ToDishtypeServiceImpl toDishtypeService;
    @PostMapping("/get")
    public Result get(@RequestParam("id") Long id){
        return toDishtypeService.get(id);
    }

}
