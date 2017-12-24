package com.example.demo.web.controller.component.webmagic;

import java.util.List;
import java.util.Set;

import org.apache.http.HttpHost;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class Mima implements PageProcessor {

    private Site site = Site.me()
            .setRetryTimes(5)
            .setSleepTime(1000)
            .setTimeOut(5000)
            .setCycleRetryTimes(10)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            ;

    //用来存储cookie信息
    private Set<Cookie> cookies;

    @Override
    public void process(Page page) {
        //原创
        List a = page.getHtml().xpath("//dt[@class='nav_ico03']/li[4]/a/@href").all();
        //转载
        List a1 = page.getHtml().xpath("//dt[@class='nav_ico03']/li[3]/a/@href").all();

        if(a!=null&&a.size()>0){
            a.addAll(a1);
            page.addTargetRequests(a);
        }
        //详情页面
        //判断 是否是楼主
        //只有page=1时才判断 是否楼主
        if(page.getUrl().get().contains("-1-")){

            String isAuthor = page.getHtml().xpath("//div[@id='postlist']/div[1]/table/tbody/tr[1]/td[2]/div[1]/div[2]/div[2]/text()").toString();

            if(isAuthor.contains("楼主")) {
                //只添加 为 楼主的 url
                List noves = page.getHtml().xpath("//div[@id='postlist']/div[1]/table/tbody/tr[1]/td[2]/div[1]/div[2]/div[2]/a[1]/@href").all();

                //进入 详情页面
                if (noves != null && noves.size() > 0) {
                    page.addTargetRequests(noves);
                    //列表页面
                }
            }
        }else {
            //添加 列表 也 文章 链接
            List<String> urls = page.getHtml().xpath("//th[@class='common']/a[2]/@href").all();

            if(urls != null && urls.size() > 0){
                page.addTargetRequests(urls);
            }
            //添加 下面 分页 链接
            List<String> pagerUrls = page.getHtml().xpath("//div[@class='pg']/a/@href").all();

            if(pagerUrls != null && pagerUrls.size() > 0){
                page.addTargetRequests(pagerUrls);
            }
        }

        if(page.getUrl().get().contains("authorid")){
                System.out.println("进入 只看楼主 页面");
                int tidIndex = page.getUrl().get().indexOf("&") + 5;
                String tidsub = page.getUrl().get().substring(tidIndex);
                int subIndex = tidsub.indexOf("&");
                String tid = tidsub.substring(0, subIndex);
                String noveType = page.getHtml().xpath("//div[@id='pt']/div/a[4]/text()").toString();
                String titleType = page.getHtml().xpath("//div[@id='postlist']/table[1]/tbody/tr/td[2]/h1/a/text()").toString();
                String name = page.getHtml().xpath("//div[@id='postlist']/table[1]/tbody/tr/td[2]/h1/span/text()").toString();
                List<String> index = page.getHtml().xpath("//div[@class='pi']/strong/a/em/text()").all();
                List<String> content = page.getHtml().xpath("//div[@class='pcb']/div[1]/table/tbody/tr/").all();

                page.putField("tid", tid);
                page.putField("name", name);
                page.putField("noveType", noveType);
                page.putField("titleType", titleType);
                page.putField("index", index);
                page.putField("content", content);

                List<String> pagerUrls = page.getHtml().xpath("//div[@class='pg']/a/@href").all();
                if (pagerUrls != null && pagerUrls.size() > 0) {
                    page.addTargetRequests(pagerUrls);
                }
        }

    }

    //使用 selenium 来模拟用户的登录获取cookie信息
    public void login() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.shuaigay.win/forum.php");

        driver.findElement(By.name("username")).clear();

        //在******中填你的用户名
        driver.findElement(By.name("username")).sendKeys("k7262140");

        driver.findElement(By.name("password")).clear();
        //在*******填你密码
        driver.findElement(By.name("password")).sendKeys("726214");

        //模拟点击登录按钮
        driver.findElement(By.xpath("//form[@id='lsform']/div/div/table/tbody/tr[2]/td[3]/button")).click();

        Thread.sleep(5000);
        //获取cookie信息
        cookies = driver.manage().getCookies();
        System.out.println(cookies.size());
        driver.close();
    }
    @Override
    public Site getSite() {
        //将获取到的cookie信息添加到webmagic中
        for (Cookie cookie : cookies) {
            site.addCookie(cookie.getName().toString(),cookie.getValue().toString());
        }

//        HttpHost httpProxy = new HttpHost("localhost",8888);
//        site.setHttpProxy(httpProxy);
        //return site.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1");
        return site;
    }

    public static void main(String[] args) throws InterruptedException {
        Mima mima = new Mima();

        //调用selenium，进行模拟登录
        mima.login();
        Spider.create(mima)
                .addUrl("https://www.shuaigay.win/thread-893321-1-1.html")
                .addPipeline(new ConsolePipeline())
                .thread(1)
                .run();
    }
}
