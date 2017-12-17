package com.example.demo.web.controller;

import com.example.demo.spring.service.model.job.JobInfo;
import com.example.demo.spring.service.service.job.ApiConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
public class JobController {
    private final Integer defaultPageSize = 30;

    @Autowired
    private ApiConverterUtil apiConverterUtil;


    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        // 自动转换日期类型的字段格式
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }

    /**
     * 数据
     * @param map
     * @param pageno
     * @return
     */
    @RequestMapping(value="/list")
    @ResponseBody
    public List<JobInfo> list(@RequestParam Map<String,Object> map, Integer pageno) {
        return apiConverterUtil.getByPage(map,pageno,defaultPageSize);
    }

    @RequestMapping(value="/get")
    @ResponseBody
    public Map list(Long jobid) {
        return Collections.singletonMap("key",apiConverterUtil.getJobInfo(jobid));
    }


    /**
     * 根据过滤表单查询接口分页数据
     * @param map
     * @return
     */
    @RequestMapping(value="/totalcount")
    @ResponseBody
    public Map totalcount(@RequestParam Map<String,Object> map){
        Integer totalcount = apiConverterUtil.getTotalcount(map);
        Integer pageSize = totalcount % defaultPageSize ==0 ? totalcount / defaultPageSize : (totalcount/defaultPageSize)+1;
        Map m = new HashMap(2);
        m.put("totalcount",totalcount);
        m.put("pageSize",pageSize);
        return m;
    }




    /**
     * 装b专用
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/banner")
    @ResponseBody
    public Map banner ()throws IOException {
        return Collections.singletonMap("key",IOUtils.toString(getClass().getClassLoader().getResourceAsStream("banner.txt"), "UTF-8"));
    }

    /**
     * 区域信息
     * @param code
     * @return
     */
    @RequestMapping(value="/areaJson")
    @ResponseBody
    public String areaJson(String code){
        return apiConverterUtil.areaJson(code);
    }

    @RequestMapping(value="/import")
    public void importData (String data, HttpServletResponse httpServletResponse)  {
        OutputStream os = null;
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + new String("51job.json".getBytes(), "ISO-8859-1"));
            httpServletResponse.setContentType("application/txt");
            httpServletResponse.setCharacterEncoding("UTF-8");
            os = httpServletResponse.getOutputStream();
            IOUtils.write(data,os,"UTF-8");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
