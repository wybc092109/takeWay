package com.one.one;

import com.alibaba.fastjson.JSON;
import com.one.one.dto.Result;
import com.one.one.entity.*;
import com.one.one.mapper.ToDishMapper;
import com.one.one.mapper.ToShopMapper;
import com.one.one.service.impl.ShopDiscountServiceImpl;
import com.one.one.service.impl.ToDishServiceImpl;
import com.one.one.service.impl.ToShopServiceImpl;
import com.one.one.utils.PageResult;
import com.one.one.utils.Ranging;
import com.one.one.utils.RequestParams;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SpringBootTest()
class OneApplicationTests {

    @Autowired
    private RestHighLevelClient restClient;
    @Autowired
    private ToDishMapper toDishMapper;
    @Autowired
    private ToShopMapper toShopMapper;
    @Autowired
    private ToDishServiceImpl toDishService;
    @Autowired
    private ToShopServiceImpl toShopService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ShopDiscountServiceImpl service;
    @Test
    void contextLoads() {
//        for (int i = 0; i < 100; i++) {
            IndexRequest requset=new IndexRequest("to_shop").id("3");
            ToShop toDish=new ToShop();
            toDish.setId(1L);
            toDish.setShopName("猪蹄");
            toDish.setDescription("好吃");
            toDish.setImage("localhost");
            toDish.setLatitude("23.25");
            toDish.setLongitude("23.25");
            toDish.setScore(1);
//            int insert = toShopMapper.insert(toDish);
//            System.out.println(insert);
            ToShopDoc toDishDoc=new ToShopDoc(toDish);
            System.out.println(toDishDoc);
            String s = JSON.toJSONString(toDishDoc);
            System.out.println(s);
            requset.source(s,XContentType.JSON);
            try {
                IndexResponse index = restClient.index(requset, RequestOptions.DEFAULT);
                System.out.println(index);
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }

    }

    @Test
    void test(){
        RequestParams parn=new RequestParams();
        parn.setKey("猪蹄");
        parn.setLocal("25.12,13.16");
        PageResult search = toShopService.search(parn);
        System.out.println(search);
        List<String> jitui = toShopService.getSuggestions("鸡腿");
        System.out.println(jitui);

    }
    @Test
    public void ranking() {
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
        System.out.println(rangings);
    }
    @Test
    void setToDishMapper(){
        IndexRequest requset=new IndexRequest("to_dish");
        ToDish toDish=new ToDish();
        toDish.setShopid(1l);
        toDish.setUpdateUser(1l);
        toDish.setCreateUser(1l);
        toDish.setCreateTime(LocalDateTime.now());
        toDish.setUpdateTime(LocalDateTime.now());
        toDish.setName("清蒸3");
        toDish.setIsMoney(true);
//        toDish.setIsRecommend(true);
        ToDishDoc toDishDoc=new ToDishDoc(toDish);
        System.out.println(toDishDoc);
        String s = JSON.toJSONString(toDishDoc);
        System.out.println(s);
        requset.source(s,XContentType.JSON);
        try {
            IndexResponse index = restClient.index(requset, RequestOptions.DEFAULT);
            System.out.println(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void save(){
        ShopDiscount shopDiscount=new ShopDiscount();
        shopDiscount.setId(1);
        shopDiscount.setBeforeMoney(1);
        shopDiscount.setHasNum(1);
        shopDiscount.setIfSevenday("yes");
        shopDiscount.setShopingId(1);
        shopDiscount.setNowMoney(5);
        shopDiscount.setPic("666");
    }
}
