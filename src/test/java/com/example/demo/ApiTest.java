package com.example.demo;

import com.example.demo.spring.service.model.job.JobDetailedInfo;
import com.example.demo.spring.service.service.job.ApiConverterUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.logging.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=UploadApplication.class)
public class ApiTest {

    @Autowired
    private ApiConverterUtil apiConverterUtil;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Test
    public void test() throws JsonProcessingException {
        Integer totalcount = apiConverterUtil.getTotalcount(new HashMap<>());
        logger.info(totalcount.toString());


        JobDetailedInfo jobInfo = apiConverterUtil.getJobInfo(96499041L);
        logger.info(jobInfo.getConame());
    }

}
