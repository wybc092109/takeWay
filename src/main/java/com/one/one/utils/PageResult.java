package com.one.one.utils;

import lombok.Data;

import java.util.List;

@Data
public class PageResult {
    private Long total;
    private List<Object> hotels;

    public PageResult() {
    }

    public PageResult(Long total, List<Object> hotels) {
        this.total = total;
        this.hotels = hotels;
    }
}