package com.example.demo.web.controller.component.webmagic;


import com.example.demo.spring.service.model.Movie;
import org.apache.http.HttpHost;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieProcessor implements PageProcessor {

    private HttpHost httpProxy = new HttpHost("127.0.0.1",8888);
    private Site site = Site.me()
//            .setHttpProxy(httpProxy)
            .setTimeOut(15*1000)
            .setCycleRetryTimes(10)
            .setRetryTimes(10)
            //1000为 5s
            .setSleepTime(5*1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .setCharset("UTF-8");

    @Override
    public void process(Page page) {
        String url =  page.getUrl().toString();
        Pattern pattern1 = Pattern.compile("http://www.80s.tw/movie/list/-(\\d*)+---(-p\\d*)?");
        Matcher matcher1 = pattern1.matcher(url);

        Pattern pattern2 = Pattern.compile("/movie/\\d+");
        Matcher matcher2 = pattern2.matcher(url);
//        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all());
        
        page.addTargetRequests(page.getHtml().links().regex("http://www\\.80s\\.tw/movie/list/-[\\d*]+---[-p\\d*]?").all());
        
        //列表页面
        if(matcher1.find()){
            //电影详情页URL集合
            List<String> moviePageUrls = page.getHtml().xpath("//ul[@class='me1 clearfix']/li/a/@href").all();

            if(moviePageUrls != null && moviePageUrls.size() > 0){
                //将当前列表页的所有电影页面添加进去
                page.addTargetRequests(moviePageUrls);
            }

            //当前列表页中的其他列表页的链接
            List<String> listUrls = page.getHtml().xpath("//div[@class='pager']/a/@href").all();

            if(listUrls != null && listUrls.size() > 0){            
                page.addTargetRequests(listUrls);
            }

        }else if(matcher2.find()){  //电影页面   
        	String movieId = page.getUrl().toString().substring(page.getUrl().toString().lastIndexOf("/")+1);
            //获取电影名字
            String movieName= page.getHtml().xpath("//div[@class='info']/h1/text()").toString()+  "----  "+page.getUrl().toString();
            //获取电影播放链接
            String movieUrl = page.getHtml().xpath("//li[@class='clearfix dlurlelement backcolor1']/span[@class='dlname nm']/input/@value").toString();

            Movie movie = new Movie(movieName, movieUrl);
            movie.setId(movieId);
            
            page.putField("movie", movie);  //后面做数据的持久化
        }    
    }


    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String[] urls =new String[21];
        int initYear =1997;
        for (int i = 0; i <=20; i++) {
            String url= "http://www.80s.tw/movie/list/-"+(initYear+i)+"---";
            urls[i]=url;
        }
        Spider.create(new MovieProcessor())
        .addUrl(urls)
        .addPipeline(new ConsolePipeline())
        .thread(1)
        .run();
        System.out.println("****************************************************");
    }
}
