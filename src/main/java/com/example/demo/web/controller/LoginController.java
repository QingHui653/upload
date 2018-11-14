package com.example.demo.web.controller;

import com.example.demo.web.bean.Menu;
import com.example.demo.web.bean.ResultBean;
import com.example.demo.web.tool.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
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

    //jwt 登陆
    // 还 需要 一个  filter 来判断 jwt 的 过期 时间
    // 前台 随 请求 放 在 header 部分
    //TODO 还需要 一个 refreshToken 存储在 服务端
    //当 token 过去，refreshToken未过期，可以使用 refreshToken 刷新token
    @PostMapping("jwtlogin")
    public Object jwtlogin(HttpServletRequest request, String userName, String passWord) {
        if ("admin".equals(userName) && "123".equals(passWord)) {
            String token = JWTUtil.sign(userName,passWord);

            ResultBean resultBean = new ResultBean();
            resultBean.setCode("200");
            resultBean.setMessage("登陆成功");
            resultBean.setData(token);

            return resultBean;
        }
        return null;
    }

    @GetMapping("/sysmenu")
    public List<Menu> sysmenu() {
        List<Menu> menus = mongoTemplate.findAll(Menu.class);
        return menus;
    }

    @PostMapping({"/token/switchRole","/switchRole"})
    public List<Menu> switchRole(Boolean stitch) {
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
