package com.example.demo.spring.service.service.job;

import com.example.demo.spring.service.model.job.JobBaseDetailedInfo;
import com.example.demo.spring.service.model.job.JobBaseListDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface JobApiService {

    /**
     *
     * @param map
     * @param pageno
     * @param pagesize 显示的记录数
     * @return
     */
    @GET("/api/job/search_job_list.php")
    Call<JobBaseListDto> jobApiXml(@QueryMap Map<String, Object> map
            , @Query("pageno") Integer pageno
            , @Query("pagesize") Integer pagesize
    );


    @GET("/api/job/get_job_info.php")
    Call<JobBaseDetailedInfo> jobInfoXml(@Query("jobid") Long jobid);



}
