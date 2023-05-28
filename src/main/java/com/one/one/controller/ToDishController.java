package com.one.one.controller;


import com.one.one.dto.Result;
import com.one.one.service.impl.ToDishServiceImpl;
import com.one.one.utils.PageResult;
import com.one.one.utils.RequestParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author YQ
 * @since 2022-10-12
 */
@RestController
@RequestMapping("/toDish")
public class ToDishController {
    @Autowired
    private ToDishServiceImpl toDishService;
    @GetMapping("search")
    public PageResult search(@RequestBody RequestParams params){
        return toDishService.search(params);
    }
    @PostMapping("/get")
    public Result get(@RequestParam("typeId") Long typeId,@RequestParam("shopId") Long shopId){
        return toDishService.get(typeId,shopId);
    }

}
