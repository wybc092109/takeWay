package com.one.one.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.one.one.dto.Result;
import com.one.one.entity.ToDish;
import com.one.one.entity.ToDishDoc;
import com.one.one.entity.ToShopDoc;
import com.one.one.mapper.ToDishMapper;
import com.one.one.service.IToDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.one.utils.PageResult;
import com.one.one.utils.RequestParams;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author YQ
 * @since 2022-10-12
 */
@Service
public class ToDishServiceImpl extends ServiceImpl<ToDishMapper, ToDish> implements IToDishService {
    @Autowired
    private ToDishMapper toDishMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RestHighLevelClient rest;

    @Override
    public Result get(Long typeId, Long shopId) {
        String typeKey = "type:" + typeId + ":" + shopId;
        String typeList = redisTemplate.opsForValue().get(typeKey);


        if (StrUtil.isNotBlank(typeList)) {
            List<ToDish> toDishList = JSONUtil.toList(typeList, ToDish.class);
            return Result.ok(toDishList);
        }
        if (typeList != null) {
            return Result.fail("店铺不存在");
        }
        LambdaQueryWrapper<ToDish> la = new LambdaQueryWrapper<>();
        la.eq(ToDish::getCategoryId, typeId);
        la.eq(ToDish::getShopid, shopId);
        List<ToDish> toDishes = toDishMapper.selectList(la);
        if (toDishes == null) {
            redisTemplate.opsForValue().set(typeKey, "", 20, TimeUnit.MINUTES);
        }
        String toDish = JSONUtil.toJsonStr(toDishes);
        redisTemplate.opsForValue().set(typeKey, toDish, 20, TimeUnit.HOURS);
        return Result.ok(toDishes);
    }


    @Override
    public PageResult search(RequestParams params) {
        try {
            // 1.准备Request
            SearchRequest request = new SearchRequest("to_dish");
            // 2.准备DSL
            // 2.1.query
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
            // 2.2.分页

            request.source().from((params.getPage() - 1) * params.getSize()).size(params.getSize());


            //算法算法
            FunctionScoreQueryBuilder function = new FunctionScoreQueryBuilder(boolQueryBuilder,
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                            new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                    QueryBuilders.termQuery("isMoney", "true"),
                                    ScoreFunctionBuilders.weightFactorFunction(100)
                            ),
                            new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                    QueryBuilders.termQuery("isRecommend", "true"),
                                    ScoreFunctionBuilders.weightFactorFunction(200)
                            ),

                    }

            );
            request.source().query(function);


            // 3.发送请求
            SearchResponse response = rest.search(request, RequestOptions.DEFAULT);

            // 4.解析响应
            return handleResponse(response, params);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 结果解析
    private com.one.one.utils.PageResult handleResponse(SearchResponse response, RequestParams params) {
        // 4.解析响应
        SearchHits searchHits = response.getHits();

        // 4.1.获取总条数
        long total = searchHits.getTotalHits().value;
        // 4.2.文档数组
        SearchHit[] hits = searchHits.getHits();

        // 4.3.遍历
        List<ToDishDoc> toDishes = new ArrayList<>();
        List<ToDishDoc> romdon = null;
        for (SearchHit hit : hits) {
            // 获取文档source
            String json = hit.getSourceAsString();


//            System.out.println(json);
            // 反序列化
            ToDishDoc toDishDoc = JSON.parseObject(json, ToDishDoc.class);
            float score = hit.getScore();
            toDishDoc.setScoreEs(score);

            localAndTime(toDishDoc, params.getLocal());
            // 放入集合
            toDishes.add(toDishDoc);

        }
        //随机
        romdon = romdon(toDishes);
        System.out.println(romdon.size());
//        for (int i = 0; i < romdon.size(); i++) {
//            System.out.println(romdon.get(i));
//        }
        // 4.4.封装返回
        return new com.one.one.utils.PageResult(total, Collections.singletonList(romdon));
    }

    public List<ToDishDoc> romdon(List<ToDishDoc> toDishes) {
        for (int i = 0; i < toDishes.size(); i++) {
            System.out.println(toDishes.get(i));
        }
        List<ToDishDoc> list = new ArrayList<>(toDishes.size());
        int count = 0;
        int startNum = 0;
        int endNum = 0;
        Random random = new Random();
        ArrayList<ToDishDoc> tmpList = null;
        for (int i = 0; i < toDishes.size(); i++) {
            if (toDishes.get(i).getScoreEs() != 0) {
                count++;
            }
            if (i == toDishes.size() - 1 || toDishes.get(i).getScoreEs() != toDishes.get(i + 1).getScoreEs()) {
                endNum = i;
                tmpList = new ArrayList<>(count);
                for (int j = startNum; j < endNum + 1; j++) {
                    if (j >= startNum && j <= endNum) {
                        tmpList.add(toDishes.get(j));
                        count = 0;
                    }
                }
                startNum = endNum + 1;
            }
            if (i == toDishes.size() - 1 || toDishes.get(i).getScoreEs() != toDishes.get(i + 1).getScoreEs()) {
                if (tmpList != null) {
                    int size = tmpList.size();
                    for (int k = 0; k < size; k++) {
                        int dish = random.nextInt(tmpList.size());
                        System.out.println(tmpList.size());
                        System.out.println(dish);
                        list.add(tmpList.get(dish));
                        System.out.println(tmpList.get(dish));
                        tmpList.remove(dish);
                    }
                }
            }
        }
//        for (int i = 0; i <list.size(); i++) {
//            System.out.println(list.get(i));
//        }
        return list;
    }

    private void localAndTime(ToDishDoc toDishDoc, String local) {
        try {
            SearchRequest request = new SearchRequest("to_shop");
            // 2.准备DSL
            // 2.1.query
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(QueryBuilders.termQuery("id", toDishDoc.getShopid()));
            request.source().query(boolQueryBuilder);
            if (local != null && !local.equals("")) {
                request.source().sort(SortBuilders
                        .geoDistanceSort("location", new GeoPoint(local))
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS)
                );
            }
            // 3.发送请求
            SearchResponse response = rest.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits) {
                // 获取文档source
                // 反序列化
                Object[] sortValues = hit.getSortValues();
                if (sortValues.length > 0) {
                    Double sortValue = (Double) sortValues[0];
                    double time = (sortValue / 40 + 15 / 60) * 60;
                    BigDecimal bigDecimal = new BigDecimal(time);
                    double timeTwo = bigDecimal.setScale(2, BigDecimal.ROUND_UP).doubleValue();
                    toDishDoc.setArrivedTime(timeTwo);
                    toDishDoc.setDistance(sortValue);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
