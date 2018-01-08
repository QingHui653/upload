package com.example.demo.web.controller.component.webmagic;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.Test;

import java.util.List;

public class web12306 {
    //是否 仅仅 高铁
    private boolean onlyG = true;
    //是否仅仅普快
    private boolean onlyK =false;
    //已经完成 下单的 次数
    private int hasOrder =0;

    @Test
    public void doom(){
        try {
            String os = System.getProperty("os.name");
            WebDriver driver;
            System.out.println(os);
            if(os.contains("Windows")){
                driver =new ChromeDriver();
                //driver=new HtmlUnitDriver(true);
            }else {
                driver=new HtmlUnitDriver(true);
            }

            //登陆  需要手动点击 验证码
            login(driver);
            while (hasOrder<3){
                search(driver);
                booming(driver); // click 之后会自动跳转
                order(driver);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void login(WebDriver driver) throws InterruptedException {
        driver.get("https://kyfw.12306.cn/otn/login/init");

        driver.findElement(By.id("username")).sendKeys("15173961640");
        driver.findElement(By.id("password")).sendKeys("h7262140");
        Thread.sleep(5000);
        // 手动点击 验证码
        driver.findElement(By.id("loginSub")).click();
        //等待 js
        Thread.sleep(5000);
    }

    private void search(WebDriver driver) throws InterruptedException {
        //判断是否还在登陆页面
        while ("https://kyfw.12306.cn/otn/login/init".equals(driver.getCurrentUrl())){
            Thread.sleep(5000);
        }

        //跳转 查询 页面
        driver.get("https://kyfw.12306.cn/otn/leftTicket/init");
        //等待加载
        Thread.sleep(500);

        JavascriptExecutor jse = (JavascriptExecutor)driver;

        //出发地
        driver.findElement(By.id("fromStationText")).clear();
        driver.findElement(By.id("fromStationText")).sendKeys("广州");
        String fromStation="document.getElementById('fromStation').value='GZQ'";
        jse.executeScript(fromStation);

        //目的地
        driver.findElement(By.id("toStationText")).clear();
        driver.findElement(By.id("toStationText")).sendKeys("衡阳");
        String toStation="document.getElementById('toStation').value='HYQ'";
        jse.executeScript(toStation);

        //日期
        String datejs ="document.getElementById('train_date').removeAttribute('readonly')";
        String date="document.getElementById('train_date').value='2018-02-03'";
        jse.executeScript(datejs);
        jse.executeScript(date);

        //是否仅 高铁
        if(onlyG){
            driver.findElement(By.name("cc_type")).click();
        }

        //是否 需要 区分时间段

        //其他 要求 可以 加在此处

    }

    private void booming(WebDriver driver) throws InterruptedException {
        //点击搜索
        driver.findElement(By.id("query_ticket")).click();
        //等待js
        Thread.sleep(500);

        WebElement queryLeftTable = driver.findElement(By.id("queryLeftTable"));
        //查询全部 tr
        List<WebElement> trList = queryLeftTable.findElements(By.xpath("//tr"));

        for (WebElement tr : trList) {
            // 查询 全部 td.
            List<WebElement> tdList = tr.findElements(By.xpath("//td"));
            if(onlyG){
                //高铁 查询 二等座 是否 有
            }else {

            }
        }

        // 此处需要判断 是否拥有 座次
        //查询全部预定
        List<WebElement> clickBtn = queryLeftTable.findElements(By.className("btn72"));

        //一次 只能 选择一个  下次 卡死 需要循环
        for (WebElement click : clickBtn) {
            click.click();
            break;
        }
    }

    /**
     * 下单
     * @param driver
     * @throws InterruptedException
     */
    private void order (WebDriver driver) throws InterruptedException {
        //等待js
        Thread.sleep(2*1000);
        //选择 乘客
        driver.findElement(By.id("normalPassenger_0")).click();
        driver.findElement(By.id("normalPassenger_1")).click();
        //提交订单
        driver.findElement(By.id("submitOrder_id")).click();

        // 是否确认 判断... 各种条件

        hasOrder++;
    }
}
