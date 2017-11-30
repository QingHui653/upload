package com.example.demo.initBean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;

public class TestBean implements InitializingBean {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void init() {
        System.out.println("init-method is called");
        System.out.println("******************************");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("******************************");
        System.out.println("------调用afterPropertiesSet()");
        System.out.println("******************************");
    }
}
