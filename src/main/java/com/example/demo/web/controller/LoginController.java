package com.example.demo.web.controller;

import com.example.demo.web.bean.Menu;
import com.example.demo.web.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("login")
    public Object login(HttpServletRequest request, String userName, String passWord) {
        if ("admin".equals(userName) && "123".equals(passWord)) {
            ResultBean resultBean = new ResultBean();
            resultBean.setCode("200");
            resultBean.setMessage("登陆成功");
            Map user = new HashMap();
            user.put("name", "admin");
            user.put("userface", "泡馍");
            resultBean.setData(user);
            return resultBean;
        }
        return null;
    }

    @RequestMapping("/sysmenu")
    public List<Menu> sysmenu() {
        List<Menu> menus = mongoTemplate.findAll(Menu.class);
        return menus;
    }

    @RequestMapping("/switchRole")
    public List<Menu> switchRole(String stitch) {
        List<Menu> menus = mongoTemplate.findAll(Menu.class);
        Iterator<Menu> iterator = menus.iterator();
        if(stitch){
            int i = 0;
            while (iterator.hasNext()){
                iterator.next();
                if(i >=2){
                    iterator.remove();
                }
                i++;
            }

        }
        return menus;
    }

}
