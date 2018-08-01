package com.example.demo.web.controller;

import com.example.demo.spring.service.service.LongTermTaskCallback;
import com.example.demo.spring.service.service.LongTimeAsyncCallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.Callable;

@Controller
@Slf4j
public class AsyncController {

    @Autowired
    private LongTimeAsyncCallService longTimeAsyncCallService;


    @GetMapping("/upload")
    @ResponseBody
    public Callable<String> processUpload(String smoe) {

        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "someView";
            }
        };

    }

    @RequestMapping(value="/asynctask", method = RequestMethod.GET)
    @Async
    public DeferredResult<ModelAndView> mvcAsync(){
        DeferredResult<ModelAndView> deferredResult = new DeferredResult<ModelAndView>();
        longTimeAsyncCallService.makeRemoteCallAndUnknownWhenFinish(new LongTermTaskCallback() {
            @Override
            public void callback(Object result) {
                System.out.println("异步调用执行完成, thread id is : " + Thread.currentThread().getId());
                ModelAndView mav = new ModelAndView("thymeleaf");
                mav.addObject("result", result);
                deferredResult.setResult(mav);
            }
        });
        return deferredResult;
    }

    @RequestMapping(value="/longtimetask", method = RequestMethod.GET)
    public WebAsyncTask longTimeTask(){
        System.out.println("/longtimetask被调用 thread id is : " + Thread.currentThread().getId());
        Callable<ModelAndView> callable = new Callable<ModelAndView>() {
            @Override
            public ModelAndView call() throws Exception {
                Thread.sleep(3000); //假设是一些长时间任务
                ModelAndView mav = new ModelAndView("thymeleaf");
                mav.addObject("result", "执行成功");
                System.out.println("执行成功 thread id is : " + Thread.currentThread().getId());
                return mav;
            }
        };
        return new WebAsyncTask(callable);
    }

    @RequestMapping(value="/longtimetaskout", method = RequestMethod.GET)
    public WebAsyncTask longTimeTaskOut(){
        System.out.println("/longtimetask被调用 thread id is : " + Thread.currentThread().getId());
        Callable<ModelAndView> callable = new Callable<ModelAndView>() {
            public ModelAndView call() throws Exception {
                Thread.sleep(3000); //假设是一些长时间任务
                ModelAndView mav = new ModelAndView("thymeleaf");
                mav.addObject("result", "执行成功");
                System.out.println("执行成功 thread id is : " + Thread.currentThread().getId());
                return mav;
            }
        };


        WebAsyncTask asyncTask = new WebAsyncTask(2000, callable);
        asyncTask.onTimeout(
                new Callable<ModelAndView>() {
                    @Override
                    public ModelAndView call() throws Exception {
                        ModelAndView mav = new ModelAndView("thymeleaf");
                        mav.addObject("result", "执行超时");
                        System.out.println("执行超时 thread id is ：" + Thread.currentThread().getId());
                        return mav;
                    }
                }
        );
        return new WebAsyncTask(3000, callable);
    }

    @RequestMapping(value = "/asynctaskout", method = RequestMethod.GET)
    public DeferredResult<ModelAndView> asyncTask() {
        DeferredResult<ModelAndView> deferredResult = new DeferredResult<ModelAndView>(2000L);
        System.out.println("/asynctask 调用！thread id is : " + Thread.currentThread().getId());
        longTimeAsyncCallService.makeRemoteCallAndUnknownWhenFinish(new LongTermTaskCallback() {
            @Override
            public void callback(Object result) {
                System.out.println("异步调用执行完成, thread id is : " + Thread.currentThread().getId());
                ModelAndView mav = new ModelAndView("thymeleaf");
                mav.addObject("result", result);
                deferredResult.setResult(mav);
            }
        });

        deferredResult.onTimeout(new Runnable() {
            @Override
            public void run() {
                System.out.println("异步调用执行超时！thread id is : " + Thread.currentThread().getId());
                ModelAndView mav = new ModelAndView("thymeleaf");
                mav.addObject("result", "异步调用执行超时");
                deferredResult.setResult(mav);
            }
        });

        return deferredResult;
    }
}
