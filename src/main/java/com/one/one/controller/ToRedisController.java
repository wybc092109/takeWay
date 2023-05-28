package com.one.one.controller;

import com.one.one.dto.Result;
import com.one.one.utils.Ranging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/redis")
public class ToRedisController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @GetMapping("/search")
    public Result ranking() {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.
                opsForZSet().reverseRangeByScoreWithScores("ranking:food",0,Integer.MAX_VALUE,0,10);

        Iterator<ZSetOperations.TypedTuple<String>> iterator2 = typedTuples.iterator();
        List<Ranging> rangings = new ArrayList<>();
        while (iterator2.hasNext()) {
            ZSetOperations.TypedTuple<String> t = iterator2.next();
            Ranging ranging = new Ranging();
            ranging.setName(t.getValue());
            ranging.setScore(t.getScore());
            System.out.println("value:" + t.getValue() + "score:" + t.getScore());
            rangings.add(ranging);

        }
        return Result.ok(rangings);
    }
}
