package com.example.demo.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.web.controller.component.webmagic.Mima;
import com.example.demo.web.controller.component.webmagic.MimaPipeline;
import com.example.demo.web.controller.component.webmagic.MongoSaveMoviePipeline;
import com.example.demo.web.controller.component.webmagic.MovieProcessor;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

@RestController
@RequestMapping("webmagic")
public class WebMagicController {

    @Autowired(required = false)
    private MongoSaveMoviePipeline mongoSaveMoviePipeline;
    @Autowired
    private MimaPipeline mimaPipeline;
    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    /**
     * 电影爬虫 存至 mongo
     */
    @RequestMapping(value = "getMovieToMongo", method = RequestMethod.GET)
    @ApiOperation("电影爬虫")
    public void getMovie() {
        String[] urls =new String[21];
        int initYear =1997;
        for (int i = 0; i <=20; i++) {
            String url= "http://www.80s.tw/movie/list/-"+(initYear+i)+"---";
            urls[i]=url;
        }
        //暂时使用 一个线程 等 后面加了代理池在 修改
        Spider.create(new MovieProcessor())
                .addUrl(urls)
                .addPipeline(new ConsolePipeline())
                .addPipeline(mongoSaveMoviePipeline)
                .thread(1)
                .run();
    }

    /**
     * 电影爬虫 存至 mongo
     */
    @RequestMapping(value = "getNoveToMongo", method = RequestMethod.GET)
    @ApiOperation("nove")
    public void getNove() throws InterruptedException {
        Mima mima = new Mima();
        //调用selenium，进行模拟登录
        mima.login();
        Spider.create(mima)
//                .addUrl("https://www.shuaigay.win/forum.php")
                .addUrl("https://www.shuaigay.win/thread-893321-1-1.html")
                .addPipeline(mimaPipeline)
                .thread(10)
                .run();
    }
}
