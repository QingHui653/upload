package com.example.demo;

import com.example.demo.web.tool.JWTUtil;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JWTTests {

    @Test
    public void createJWT(){
        String token = JWTUtil.sign("admin","123");
        System.out.println("--- "+token);
    }

    @Test
    public void valiJWT(){
        String token = JWTUtil.sign("admin","123");
        boolean isLogin = JWTUtil.verify(token, "admin", "123");
        System.out.println(isLogin);
    }

    @Test
    public void hashmap(){

        Map map = Collections.synchronizedMap (new HashMap()) ;
        map.put("1","1");
    }
}
