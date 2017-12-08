package com.example.demo.spring.service.service;

import java.util.List;

public interface IMongoService {

    <T> List<T> findAll(Class clazz);
}
