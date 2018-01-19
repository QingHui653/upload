package com.example.demo.web.controller;

import com.example.demo.web.bean.ResultBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @PostMapping("login")
    public Object login (HttpServletRequest request,String userName ,String passWord){
        if("admin".equals(userName)&&"123".equals(passWord)){
            ResultBean resultBean = new ResultBean();
            resultBean.setCode("200");
            resultBean.setMessage("登陆成功");
            Map user = new HashMap();
            user.put("name","admin");
            user.put("userface","泡馍");
            resultBean.setData(user);
            return  resultBean;
        }
        return null;
    }
}
