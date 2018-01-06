package com.example.demo.web.controller.component.webmagic;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 1.加入 maven 项目
 * 2.自动引入 jar 包
 * 3.下载 对应的 selenium chrome驱动
 */
public class web12306 {
    //是否 仅仅 高铁
    // 不是高铁 就是 普快
    private boolean onlyG = false;
    //是否仅仅普快
    private boolean onlyK =false;
    //已经完成 下单的 次数
    private int hasOrder =0;
    //是否有票
    private int hasTic =0;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private SimpleDateFormat stf = new SimpleDateFormat("HH:mm");

    private static Calendar calendar = Calendar.getInstance();

    private Date searchDate = calendar.getTime();

    private String searchDateStr = sdf.format(searchDate);
    // 出发时间
    private Date startDateStr =stf.parse("08:00");
    // 出发时间
    private Date endDateStr = stf.parse("12:00");

    //已经 查询 的 车次 数量
    private Map<String,Integer> clickTrainNum = new HashMap<>();
    //下单 的 次数
    private Integer clickNum = 0;


    static{
        calendar.add(Calendar.DATE,29);
    }

    public web12306() throws ParseException {
    }

    @Test
    public void doom() throws ParseException {
        try {
            String os = System.getProperty("os.name");
            WebDriver driver;
            System.out.println(os);
            if(os.contains("Windows")){
                driver =new ChromeDriver();
            }else {
                driver=new HtmlUnitDriver(true);
            }

            //登陆  需要手动点击 验证码
            login(driver);
            search(driver);
            while (hasOrder<1){
                booming(driver); // click 之后会自动跳转
                order(driver);
                Thread.sleep(1*1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     */
    private void login(WebDriver driver) throws InterruptedException {
        driver.get("https://kyfw.12306.cn/otn/login/init");

        //可以不用写在代码中 全部手动输入 即可
        driver.findElement(By.id("username")).sendKeys("");  //账号
        driver.findElement(By.id("password")).sendKeys("");  //密码
        //判断是否还在登陆页面
        driver.findElement(By.id("loginSub")).click();
        while ("https://kyfw.12306.cn/otn/login/init#".equals(driver.getCurrentUrl())){
            //等待 js
            Thread.sleep(5000);
            driver.findElement(By.id("loginSub")).click();
            Thread.sleep(4000);
        }

        //是否进入我的 12306 页面
        while (!"https://kyfw.12306.cn/otn/index/initMy12306".equals(driver.getCurrentUrl())){
            Thread.sleep(3000);
        }
    }

    /**
     *  查询
     */
    private void search(WebDriver driver) throws InterruptedException {

        //跳转 查询 页面
        driver.get("https://kyfw.12306.cn/otn/leftTicket/init");
        //等待加载
        Thread.sleep(500);

        JavascriptExecutor jse = (JavascriptExecutor)driver;

        //出发地
        driver.findElement(By.id("fromStationText")).clear();
        driver.findElement(By.id("fromStationText")).sendKeys("广州"); //出发站
        String fromStation="document.getElementById('fromStation').value='GZQ'";  // 出发站 代码 需要自己 去找
        jse.executeScript(fromStation);

        //目的地
        driver.findElement(By.id("toStationText")).clear();
        driver.findElement(By.id("toStationText")).sendKeys("衡阳"); //目的地
        String toStation="document.getElementById('toStation').value='HYQ'"; // 目的地 代码 需要自己 去找
        jse.executeScript(toStation);

        //日期
        String datejs ="document.getElementById('train_date').removeAttribute('readonly')";
        String date="document.getElementById('train_date').value='"+searchDateStr+"'"; //searchDateStr  时间  抢最新的 差 29天
        jse.executeScript(datejs);
        jse.executeScript(date);

        //是否仅 高铁
        List<WebElement> ccList = driver.findElements(By.xpath("//input[@name='cc_type']"));

        if(onlyG){
            ccList.get(0).click();
        }else if(true){ // D
            ccList.get(2).click();
            ccList.get(3).click();
            ccList.get(4).click();
        }

        //是否 需要 区分时间段

        //其他 要求 可以 加在此处

    }

    /**
     *  点击 预定 按钮
     */
    private void booming(WebDriver driver) throws InterruptedException, ParseException {
        // 判断 是否在搜索页面 不在 重新 进 搜索
        if(!"https://kyfw.12306.cn/otn/leftTicket/init".equals(driver.getCurrentUrl())){
            search(driver);
        }
        //点击搜索
        driver.findElement(By.id("query_ticket")).click();
        //等待js
        Thread.sleep(500);

        WebElement queryLeftTable = driver.findElement(By.id("queryLeftTable"));
        //查询全部 tr
        List<WebElement> trList = queryLeftTable.findElements(By.xpath("//tr[@class='bgc']"));

        WebElement tr;
        for (int i = 1; i <trList.size(); i++) {
            tr = trList.get(i);
            // 查询 全部 td
            List<WebElement> tdList = tr.findElements(By.xpath("//tr[@class='bgc']["+i+"]/td"));

//            System.out.println(tdList.get(0).getText());
//            System.out.println(tdList.get(1).getText()); //商务座
//            System.out.println(tdList.get(3).getText()); //二等座
//            System.out.println(tdList.get(7).getText()); //硬卧
//            System.out.println(tdList.get(12).getText()); //按钮

            //车次
            List<WebElement> trainNumList = tr.findElements(By.xpath("//tr[@class='bgc'][" + i + "]/td[1]/div/div[1]"));
            String trainNum = trainNumList.get(0).getText();
//            System.out.println(trainNumList.get(0).getText());
            //出发站
            List<WebElement> stationList = tr.findElements(By.xpath("//tr[@class='bgc'][" + i + "]/td[1]/div/div[2]"));
//            System.out.println(stationList.get(0).getText());
            //出发时间
            List<WebElement> startList = tr.findElements(By.xpath("//tr[@class='bgc'][" + i + "]/td[1]/div/div[3]/strong[1]"));
            String time = startList.get(0).getText();

            //取到时间 可以 根据 时间 判断
//            System.out.println(startList.get(0).getText());

            //高铁 查询 二等座
            int j =3;
            if(onlyG){
                //高铁 查询 二等座 是否 有
                j=3;
            // 普通查询 卧铺
            } else {
                j=7;
            }
            // 判断 是否有票
            if(!"无".equals(tdList.get(j).getText())&&!"1".equals(tdList.get(j).getText())){
                if(!"*".equals(tdList.get(j).getText())&&!"--".equals(tdList.get(j).getText())){
                    Date timeDate = stf.parse(time);
                    //判断 是否已经 点击过
                    if(clickTrainNum.get(trainNum)==null){
                        if(timeDate.after(startDateStr)&&timeDate.before(endDateStr)){
                            tdList.get(12).click();
                            hasTic++;
                            clickTrainNum.put(trainNum,clickNum+1);
                            break;
                        }
                    }

                }
            }

            if(i==trList.size()-1){
                //已经循环 一次 列表 全部没票 清空 重新循环
                clickTrainNum= new HashMap<>();
            }
        }
    }

    /**
     * 下单
     * @param driver
     * @throws InterruptedException
     */
    private void order (WebDriver driver) throws InterruptedException {
        //等待js
        if(hasTic>0){
            while (!"https://kyfw.12306.cn/otn/confirmPassenger/initDc".equals(driver.getCurrentUrl())){
                Thread.sleep(3*1000);
            }
            //选择 乘客   两个乘客
            //如果只是一位 请注释 2
            driver.findElement(By.id("normalPassenger_0")).click();
            driver.findElement(By.id("normalPassenger_1")).click();
            //提交订单
            driver.findElement(By.id("submitOrder_id")).click();
            Thread.sleep(1*1000);

            // 难度大 不做了
            /*try {
                if (onlyG) {
                    // 高铁选位
                    // 选位 无法完成
                    JavascriptExecutor jse = (JavascriptExecutor)driver;
                    String f ="document.getElementById('1F').setAttribute('class','cur')";
                    jse.executeScript(f);
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("暂不支持自选座位");
            }*/
            //确定
            try {
                clickNum++;
                hasTic--;
                driver.findElement(By.id("qr_submit_id")).click();
                hasOrder++;

            }catch (Exception e){
                System.out.println(clickTrainNum);
                System.out.println("没有余票");
            }


        }
    }
}
