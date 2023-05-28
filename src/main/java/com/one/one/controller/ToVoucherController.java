package com.one.one.controller;


import com.one.one.dto.Result;
import com.one.one.service.impl.ToVoucherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YQ
 * @since 2022-12-11
 */
@RestController
@RequestMapping("/toVoucher")
public class ToVoucherController {
    @Autowired
    private ToVoucherServiceImpl toVoucherService;

    @PostMapping("allVoucher")
    public Result allVoucher(){
        return toVoucherService.allVoucher();
    }
}
