package com.example.demo.web.controller;

import com.example.demo.spring.service.model.Movie;
import com.example.demo.spring.service.model.nove.Nove;
import com.example.demo.spring.service.model.nove.Title;
import com.example.demo.spring.service.service.IMongoService;
import com.example.demo.web.tool.Pager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MongoController {

    @Autowired(required = false)
    private IMongoService mongoService;

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @Autowired(required = false)
    private MongoClient mongoClient;

    @GetMapping("findAll")
    public <T>List<T> findAll(){
        return mongoService.findAll(Movie.class);
    }

    @PostMapping("findPage")
    public Object findPage(@RequestParam(required = false) Map<String,String> map, Pager pager){
        log.info(map.toString());
        Criteria criteria = new Criteria();
        if(map.get("movieName")!=null){
            criteria =Criteria.where("movieName").regex(map.get("movieName"));
        }
        Query query = new Query();
        query.addCriteria(criteria);
        long count = mongoTemplate.count(query, Movie.class);

        query.skip((pager.getCurrentPage()-1)*pager.getPageSize());
        query.limit(pager.getPageSize());
        List list = mongoTemplate.find(query,Movie.class);

        pager.setList(list);
        pager.setCount((int)count);
        return pager;
    }

    @GetMapping("insertAll")
    public void insertAll(){
        List movielist = new ArrayList();
        Movie movie;
        for (int i = 0; i < 1000; i++) {

            movie=new Movie(String.valueOf(i),String.valueOf(i));

            movielist.add(movie);
        }
        mongoTemplate.insertAll(movielist);
    }

    @GetMapping("findNovePage")
    public Object findNovePage(@RequestParam(required = false) Map<String,String> map, Pager pager){
        Criteria criteria = new Criteria();
        pager.setCurrentPage(Integer.valueOf(map.get("page")));
        pager.setPageSize(Integer.valueOf(map.get("limit")));
        if(map.get("key[name]")!=null&&map.get("key[name]").length()>0){
            criteria.andOperator(Criteria.where("name").regex(map.get("key[name]")));
        }
        if(map.get("key[noveType]")!=null&&map.get("key[noveType]").length()>0){
            criteria.andOperator(Criteria.where("noveType").regex(map.get("key[noveType]")));
        }
        if(map.get("key[titleType]")!=null&&map.get("key[titleType]").length()>0){
            criteria.andOperator(Criteria.where("titleType").regex(map.get("key[titleType]")));
        }
        Query query = new Query();
        query.addCriteria(criteria);
        long count = mongoTemplate.count(query, Nove.class);

        query.skip((pager.getCurrentPage()-1)*pager.getPageSize());
        query.limit(pager.getPageSize());
        List list = mongoTemplate.find(query,Nove.class);

        /*pager.setList(list);
        pager.setCount((int)count);*/
        Map retMap = new HashMap();
        retMap.put("code",0);
        retMap.put("count",(int)count);
        retMap.put("data",list);
        retMap.put("msg","");
        return retMap;
    }

    @GetMapping("findTitlePage")
    public Object findTitlePage(@RequestParam(required = false) Map<String,String> map,String noveId, Pager pager){
        pager.setPageSize(20);
        if(map.get("page")!=null){
            pager.setCurrentPage(Integer.valueOf(map.get("page")));
        }

        MongoDatabase mongoDatabase = mongoClient.getDatabase("upload");

        long count = mongoDatabase.getCollection("title")
                .count(new Document("tid", new Document("$eq", noveId)));
        FindIterable<Document> findIterable = mongoDatabase.getCollection("title")
                .find(new Document("tid",new Document("$eq",noveId)))
                .sort(Sorts.ascending("index"))
                .skip((pager.getCurrentPage()-1)*pager.getPageSize())
                .limit(pager.getPageSize());

        List<Title> list =new ArrayList();
        for (Document document : findIterable) {
            Title title = new Gson().fromJson(document.toJson(),Title.class);
            list.add(title);
        }


        Map retMap = new HashMap();
        retMap.put("code",0);
        retMap.put("count",(int)count);
        retMap.put("data",list);
        retMap.put("msg","");
        return retMap;
    }
}
