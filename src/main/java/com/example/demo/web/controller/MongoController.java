package com.example.demo.web.controller;

import com.example.demo.spring.service.model.Movie;
import com.example.demo.spring.service.service.IMongoService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MongoController {

    @Autowired(required = false)
    private IMongoService mongoService;

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @GetMapping("findAll")
    public <T>List<T> findAll(){
        return mongoService.findAll(Movie.class);
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
