package com.example.demo.spring.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoDAO {
    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    public <T> List<T> findAll(Class clazz){
        return mongoTemplate.findAll(clazz);
    }
}
