package com.one.one.controller;


import com.one.one.service.impl.ToShopServiceImpl;
import com.one.one.utils.PageResult;
import com.one.one.utils.RequestParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商家 前端控制器
 * </p>
 *
 * @author YQ
 * @since 2022-10-13
 */
@RestController
@RequestMapping("/toShop")
public class ToShopController {
    @Autowired
    private ToShopServiceImpl toShopService;

    @PostMapping("/search")
    PageResult search(@RequestBody RequestParams params) {
        return toShopService.search(params);
    }

    @GetMapping("suggestion")
    public List<String> getSuggestions(@RequestParam("key") String prefix) {
        return toShopService.getSuggestions(prefix);
    }
}
