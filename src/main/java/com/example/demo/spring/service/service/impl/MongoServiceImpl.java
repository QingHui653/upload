package com.example.demo.spring.service.service.impl;

import com.example.demo.spring.service.dao.MongoDAO;
import com.example.demo.spring.service.service.IMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoServiceImpl implements IMongoService {

    @Autowired(required = false)
    private MongoDAO mongoDAO;

    @Override
    public <T> List<T> findAll(Class clazz){
        return mongoDAO.findAll(clazz);
    }
}
