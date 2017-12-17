package com.example.demo.web.controller;

import com.example.demo.spring.service.model.Movie;
import com.example.demo.spring.service.service.IMongoService;
import com.example.demo.web.tool.Pager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MongoController {

    @Autowired(required = false)
    private IMongoService mongoService;

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

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
}
