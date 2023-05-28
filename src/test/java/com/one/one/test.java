package com.one.one;

import com.one.one.entity.ToDishDoc;
import com.one.one.entity.ToUser;
import com.one.one.service.impl.ToDishServiceImpl;
import com.one.one.service.impl.ToUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@SpringBootTest
public class test {
//    public static void main(String[] args) {
////        ToUserServiceImpl toUserService=new ToUserServiceImpl();
////        ToUser byId = toUserService.getById(1);
////        System.out.println(byId);
//
//    }

    @Resource
    private ToDishServiceImpl toDishService;

//    @Test
//    public void testRomdon() {
//        List<ToDishDoc> toDishes = new ArrayList<>();
//        ToDishDoc toDishDoc1 = new ToDishDoc();
//        toDishDoc1.setScoreEs(1);
//        ToDishDoc toDishDoc2 = new ToDishDoc();
//        toDishDoc2.setScoreEs(2);
//        toDishDoc2.setDescription("aaa");
//        ToDishDoc toDishDoc3= new ToDishDoc();
//        toDishDoc3.setScoreEs(2);
//        toDishDoc3.setDescription("bbb");
//        ToDishDoc toDishDoc4= new ToDishDoc();
//        toDishDoc4.setScoreEs(2);
//        toDishDoc4.setDescription("ccc");
//        ToDishDoc toDishDoc5 = new ToDishDoc();
//        toDishDoc5.setScoreEs(3);
//        ToDishDoc toDishDoc6 = new ToDishDoc();
//        toDishDoc6.setScoreEs(3);
//        ToDishDoc toDishDoc7 = new ToDishDoc();
//        toDishDoc7.setScoreEs(3);
//        ToDishDoc toDishDoc8 = new ToDishDoc();
//        toDishDoc8.setScoreEs(3);
//        ToDishDoc toDishDoc9 = new ToDishDoc();
//        toDishDoc9.setScoreEs(3);
//        toDishes.add(toDishDoc1);
//        toDishes.add(toDishDoc2);
//        toDishes.add(toDishDoc3);
//        toDishes.add(toDishDoc4);
//        toDishes.add(toDishDoc5);
//        toDishes.add(toDishDoc6);
//        toDishes.add(toDishDoc7);
//        toDishes.add(toDishDoc8);
//        toDishes.add(toDishDoc9);
//        List<ToDishDoc> romdon = toDishService.romdon(toDishes);
//        for (int i = 0; i < romdon.size(); i++) {
//            System.out.println(romdon.get(i).getDescription());
//        }
//        System.out.println(romdon.size());




}
