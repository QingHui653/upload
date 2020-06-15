package com.example.demo.web.controller;

import cn.shuibo.annotation.Decrypt;
import cn.shuibo.annotation.Encrypt;
import com.example.demo.spring.service.model.Movie;
import com.example.demo.web.controller.component.webmagic.MongoSaveMoviePipeline;
import com.example.demo.web.controller.component.webmagic.MovieProcessor;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.net.MalformedURLException;

@RestController
@RequestMapping("webmagic")
public class WebMagicController {

    @Autowired(required = false)
    private MongoSaveMoviePipeline mongoSaveMoviePipeline;
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

    @Encrypt
    @GetMapping("/encryption")
    @ResponseBody
    public Movie encryption(){
        Movie testBean = new Movie();
        testBean.setMovieName("shuibo.cn");
        testBean.setId("18");
        return testBean;
    }

    @Decrypt
    @PostMapping("/decryption")
    @ResponseBody
    public String decryption(@RequestBody Movie testBean){
        return testBean.toString();
    }
}
