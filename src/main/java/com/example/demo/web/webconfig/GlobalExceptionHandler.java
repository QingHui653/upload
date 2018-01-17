package com.example.demo.web.webconfig;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler  {
    public static final String DEFAULT_ERROR_VIEW = "common/error";

    // 不能 有两个 相同 的 @ExceptionHandler
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e){
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

    /**
     * 创建 自定义的 json异常
     * 捕获 抛出 json
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Object jsonErrorHandler(HttpServletRequest req, Exception e){
        Map error = new HashMap();
        return error;
    }
}
