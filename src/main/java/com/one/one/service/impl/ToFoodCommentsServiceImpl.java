package com.one.one.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.one.one.dto.Result;
import com.one.one.dto.UserDTO;
import com.one.one.entity.ToFoodComments;
import com.one.one.entity.ToUser;
import com.one.one.mapper.ToFoodCommentsMapper;
import com.one.one.mapper.ToUserMapper;
import com.one.one.service.IToFoodCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.one.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YQ
 * @since 2022-10-30
 */
@Service
public class ToFoodCommentsServiceImpl extends ServiceImpl<ToFoodCommentsMapper, ToFoodComments> implements IToFoodCommentsService {
    @Autowired
    private ToFoodCommentsMapper mapper;
    @Autowired
    private ToUserMapper toUserMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result comment(long id) {
        if (id==0){
            List<ToFoodComments> list = list();
            if (list!=null){
                List<ToFoodComments> likes = isGood(list);
                return Result.ok(likes);
            }
            return Result.fail("评论不存在");
        }
        if (id==1){
            //差评
            LambdaQueryWrapper<ToFoodComments> la=new LambdaQueryWrapper<>();
            la.eq(ToFoodComments::getGoodId,id);
            List<ToFoodComments> toFoodComments = mapper.selectList(la);
            if (toFoodComments!=null){
                List<ToFoodComments> likes = isGood(toFoodComments);
                return Result.ok(likes);
            }
            return Result.fail("评论不存在");
        }
        if (id==2){
            //差评
            LambdaQueryWrapper<ToFoodComments> la=new LambdaQueryWrapper<>();
            la.eq(ToFoodComments::getBadId,id);
            List<ToFoodComments> toFoodComments = mapper.selectList(la);
            if (toFoodComments!=null){
                List<ToFoodComments> likes = isGood(toFoodComments);
                return Result.ok(likes);
            }
            return Result.fail("评论不存在");
        }
        if (id==3){
            //味道好
            LambdaQueryWrapper<ToFoodComments> la=new LambdaQueryWrapper<>();
            la.eq(ToFoodComments::getGoodEatId,id);
            List<ToFoodComments> toFoodComments = mapper.selectList(la);
            if (toFoodComments!=null){
                List<ToFoodComments> likes = isGood(toFoodComments);
                return Result.ok(likes);
            }
            return Result.fail("评论不存在");
        }
        if (id==4){
            //有图片
            LambdaQueryWrapper<ToFoodComments> la=new LambdaQueryWrapper<>();
            la.eq(ToFoodComments::getParentId,id);
            List<ToFoodComments> toFoodComments = mapper.selectList(la);
            if (toFoodComments!=null){
                List<ToFoodComments> likes = isGood(toFoodComments);
                return Result.ok(likes);
            }
            return Result.fail("评论不存在");
        }
        return Result.ok();
    }

    @Override
    public Result good(Long id) {
        // 1.获取登录用户
        Long userId = UserHolder.getUser().getId();
        String key="good:"+id;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        if (score == null) {
            // 3.如果未点赞，可以点赞
            // 3.1.数据库点赞数 + 1
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            // 3.2.保存用户到Redis的set集合  zadd key value score
            if (isSuccess) {
                stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
            }
        } else {
            // 4.如果已点赞，取消点赞
            // 4.1.数据库点赞数 -1
            boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
            // 4.2.把用户从Redis的set集合移除
            if (isSuccess) {
                stringRedisTemplate.opsForZSet().remove(key, userId.toString());
            }
        }
        return Result.ok();
    }

    private List<ToFoodComments> isGood(List<ToFoodComments> list){
        List<ToFoodComments> comments=new ArrayList<>();
        for (ToFoodComments toFoodComments : list) {
            Long userId = UserHolder.getUser().getId();
            Long id = toFoodComments.getId();
            String key="good:"+id;
            Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
            ToUser toUser = toUserMapper.selectById(toFoodComments.getUserId());
            UserDTO userDTO = BeanUtil.copyProperties(toUser, UserDTO.class);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String localTime = df.format(toFoodComments.getCreateTime());
            toFoodComments.setStringCreateTime(localTime);
            toFoodComments.setUserDTO(userDTO);
            comments.add(toFoodComments);
            if (score!=null){
                toFoodComments.setIsGood(true);
            }
        }
        return comments;
    }
}
