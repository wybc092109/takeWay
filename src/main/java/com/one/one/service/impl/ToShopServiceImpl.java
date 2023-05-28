package com.one.one.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.one.one.entity.ToDishDoc;
import com.one.one.entity.ToShop;
import com.one.one.entity.ToShopDoc;
import com.one.one.mapper.ToShopMapper;
import com.one.one.service.IToShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.one.utils.PageResult;
import com.one.one.utils.RequestParams;
import org.apache.lucene.search.BooleanQuery;
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
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商家 服务实现类
 * </p>
 *
 * @author YQ
 * @since 2022-10-13
 */
@Service
public class ToShopServiceImpl extends ServiceImpl<ToShopMapper, ToShop> implements IToShopService {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageResult search(RequestParams params) {
        Boolean aBoolean = redisTemplate.opsForZSet().
                addIfAbsent("ranking:food", params.getKey(), 1);
        if (Boolean.FALSE.equals(aBoolean)) {
            redisTemplate.opsForZSet().
                    incrementScore("ranking:food", params.getKey(), 1);
        }
        try {
            // 1.准备Request
            SearchRequest request = new SearchRequest("to_shop");
            // 2.准备DSL
            // 2.1.query
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            if (params.getKey() == null) {
                boolQueryBuilder.must(QueryBuilders.matchAllQuery());
//                request.source().query(QueryBuilders.matchAllQuery());
                request.source().query(QueryBuilders.termQuery("status",1));
            } else {

                boolQueryBuilder.must(QueryBuilders.
                        matchQuery("all", params.getKey()));
//                request.source().query(QueryBuilders.matchQuery("all",params.getKey()));
//                boolQueryBuilder.filter(QueryBuilders.termQuery("all",1));
            }
            request.source().query(boolQueryBuilder);
            // 2.2.分页
            int page = params.getPage();
            int size = params.getSize();
            request.source().from((page - 1) * size).size(size);

            // 2.3.排序
            String location = params.getLocal();
            if (location != null && !location.equals("")) {
                request.source().sort(SortBuilders
                        .geoDistanceSort("location", new GeoPoint(location))
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS)
                );
            }

            // 3.发送请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            // 4.解析响应
            return handleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        try {
            // 1.准备Request
            SearchRequest request = new SearchRequest("to_shop");
            // 2.准备DSL
            request.source().suggest(new SuggestBuilder().addSuggestion(
                    "suggestions",
                    SuggestBuilders.completionSuggestion("suggestion")
                            .prefix(prefix)
                            .skipDuplicates(true)
                            .size(10)
            ));
            // 3.发起请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            // 4.解析结果
            Suggest suggest = response.getSuggest();
            // 4.1.根据补全查询名称，获取补全结果
            CompletionSuggestion suggestions = suggest.getSuggestion("suggestions");
            // 4.2.获取options
            List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();
            // 4.3.遍历
            List<String> list = new ArrayList<>(options.size());
            for (CompletionSuggestion.Entry.Option option : options) {
                String text = option.getText().toString();
                list.add(text);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 结果解析
    private com.one.one.utils.PageResult handleResponse(SearchResponse response) {
        // 4.解析响应
        SearchHits searchHits = response.getHits();

        // 4.1.获取总条数
        long total = searchHits.getTotalHits().value;
        // 4.2.文档数组
        SearchHit[] hits = searchHits.getHits();

        // 4.3.遍历
        List<ToShopDoc> toShopDocs = new ArrayList<>();
        for (SearchHit hit : hits) {
            // 获取文档source
            String json = hit.getSourceAsString();

//            System.out.println(json);
            // 反序列化
            ToShopDoc toShopDoc = JSON.parseObject(json, ToShopDoc.class);
            Object[] sortValues = hit.getSortValues();
            if (sortValues.length > 0) {
                    Double sortValue = (Double) sortValues[0];
                double time = (sortValue / 40 + 15/60)*60;
                BigDecimal bigDecimal=new BigDecimal(time);
                double timeTwo = bigDecimal.setScale(2, BigDecimal.ROUND_UP).doubleValue();
                toShopDoc.setArrivedTime(timeTwo);
                toShopDoc.setDistance(sortValue);
            }
            // 放入集合
            toShopDocs.add(toShopDoc);
        }
        // 4.4.封装返回
        return new com.one.one.utils.PageResult(total, Collections.singletonList(toShopDocs));
    }
}
