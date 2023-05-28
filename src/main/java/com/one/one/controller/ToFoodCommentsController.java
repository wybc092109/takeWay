package com.one.one.controller;


import com.one.one.dto.Result;
import com.one.one.service.impl.ToFoodCommentsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YQ
 * @since 2022-10-30
 */
@RestController
@RequestMapping("/toFoodComments")
public class ToFoodCommentsController {
    @Autowired
    private ToFoodCommentsServiceImpl service;
    @PostMapping("comment")
    public Result comment(long id){
        return service.comment(id);
    }
    @PostMapping("/good")
    public Result good(Long id){
        return service.good(id);
    }
}
