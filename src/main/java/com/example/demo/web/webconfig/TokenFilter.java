package com.example.demo.web.webconfig;

import com.example.demo.web.bean.ResultBean;
import com.example.demo.web.tool.JWTUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * token 拦截器
 * @author woshizbh
 *
 */
@WebFilter(filterName = "tokenFilter", urlPatterns = "/token/*" )
public class TokenFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String token = httpServletRequest.getHeader("token");
        System.out.println(httpServletRequest.getMethod());
        if(!"OPTIONS".equals(httpServletRequest.getMethod())&&StringUtils.isBlank(token)) {
            PrintWriter out = response.getWriter();
            ResultBean resultBean = new ResultBean();
                resultBean.setCode("403");
                resultBean.setMessage("没有足够的权限");
            out.println(new Gson().toJson(resultBean));
            out.flush();
            out.close();
            return;
        }
//		System.out.println("--------自定义执行过滤操作");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("--------自定义过滤器销毁");
    }
}
